package com.yootii.bdy.bill.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yootii.bdy.agency.service.AgencyService;
import com.yootii.bdy.bill.dao.BillAmountMapper;
import com.yootii.bdy.bill.dao.BillCaseMapper;
import com.yootii.bdy.bill.dao.BillChargeRecordMapper;
import com.yootii.bdy.bill.dao.BillMapper;
import com.yootii.bdy.bill.dao.BillSolrMapper;
import com.yootii.bdy.bill.dao.ChargeRecordMapper;
import com.yootii.bdy.bill.model.Bill;
import com.yootii.bdy.bill.model.BillChargeRecord;
import com.yootii.bdy.bill.model.BillSolr;
import com.yootii.bdy.bill.model.ChargeRecord;
import com.yootii.bdy.bill.model.PayAcount;
import com.yootii.bdy.bill.model.ReturnBillAmount;
import com.yootii.bdy.bill.service.BillService;
import com.yootii.bdy.common.Constants;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.LoginReturnInfo;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.datasyn.service.DataSynService;
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.solr.SolrData;
import com.yootii.bdy.solr.SolrInfo;
import com.yootii.bdy.solr.SolrSend;
import com.yootii.bdy.solr.SolrSendBill;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.util.GraspUtil;
import com.yootii.bdy.util.JsonUtil;
import com.yootii.bdy.util.SolrUtil;

@Service
public class BillServiceImpl implements BillService{
	
	@Resource
	private SolrInfo solrInfo;

	@Resource
	private BillMapper billMapper;	
	
	@Resource
	private BillCaseMapper billCaseMapper;	
	
	@Resource
	private BillSolrMapper billSolrMapper;

	@Resource
	private BillChargeRecordMapper billChargeRecordMapper;

	@Resource
	private BillAmountMapper billAmountMapper;

	@Resource
	private ChargeRecordMapper chargeRecordMapper;
	
	@Resource
	private DataSynService dataSynService;
	
	@Resource
	private AgencyService agencyService;
	

	@Override
	public ReturnInfo createBill(GeneralCondition gcon, Bill bill,String chargeRecordIds) {
		ReturnInfo info = new ReturnInfo();
		if(chargeRecordIds==null||"".equals(chargeRecordIds)){
			info.setSuccess(false);
			info.setMessage("账单记录ID不能为空");
			return info;
		}
		
		String caseIds=bill.getCaseIds();
		if(caseIds==null || caseIds.equals("")){
			info.setSuccess(false);
			info.setMessage("caseId不能为空");
			return info;
		}
		
		//代理机构ID从userId获取
		//		Integer agencyId = billMapper.selectAgencyIdByUserId(userId);
		//		bill.setAgencyId(agencyId);
		
		Integer agencyId=bill.getAgencyId();
		if(agencyId==null){
			info.setSuccess(false);
			info.setMessage("代理机构ID不能为空");
			return info;
		}
		if(bill.getReceiverType()==null){
			info.setSuccess(false);
			info.setMessage("账单接收人类型不能为空");
			return info;
		}
		try{
			//在账单与账单记录关联表中增加记录
			String[] ids = chargeRecordIds.split(",");
			//检查这些账单记录是否都是该代理机构下的未核销的，查询未核销的还能避免传入的账单记录Id不正确的情况
			if(ids!=null&&ids.length>0){
				Map<String,Object> params = new HashMap<String, Object>();
				params.put("agencyId", bill.getAgencyId());
				params.put("ids", ids);
				int result = chargeRecordMapper.selectChargeRecordNoVerifyCount(params);
				if(result!=ids.length){
					info.setSuccess(false);
					info.setMessage("账单生成失败：账单记录有误");
					return info;
				}
			}
			
			bill.setCreateDate(new Date());
			bill.setStatus("待审核");
			
			String billNo=bill.getBillNo();
			if(billNo==null || billNo.equals("")){			
				Integer billId=bill.getBillId();				
				// 产生billNo
				ReturnInfo rtnInfo= generateBillNo(billId);
				if (rtnInfo.getSuccess()){
					Map<String, String> map=(Map<String, String>)rtnInfo.getData();
					billNo=map.get("billNo");
					bill.setBillNo(billNo);
				}
			}
			
			billMapper.insertSelective(bill);
			
			Integer billId=bill.getBillId();
			
			//在账单账单记录关联表中批量增加记录
			if(ids!=null&&ids.length>0){
				Map<String,Object> params = new HashMap<String, Object>();
				params.put("billId", billId);
				params.put("ids", ids);
				billChargeRecordMapper.batchInsertBillChargeRecord(params);
			}
						
			//修改账单记录中状态为已核销
			chargeRecordMapper.updateByChargeRecordIds(ids);
			

			//Modification start, by yang guang, 2018-10-19
			//在账单与案件关联表中增加记录
			String[] cIds = caseIds.split(",");
			if(cIds!=null&&cIds.length>0){
				Map<String,Object> params = new HashMap<String, Object>();
				params.put("billId", billId);
				params.put("cIds", cIds);
				billCaseMapper.batchInsertBillCase(params);
			}	
			
			//支持将账单信息同步到wpm 				
			if(Constants.DataSyn){
				Integer whdId=agencyService.getWhdAgencyId();
				if(agencyId!=null && whdId!=null && agencyId.intValue()==whdId.intValue()){					
					//将账单信息同步到wpm		
					dataSynService.billDataSyn(gcon, caseIds, billId);
				}
			}
			//Modification end	
			
		}catch(Exception e){
			e.printStackTrace();
			info.setSuccess(false);
			info.setMessage("创建账单失败");
			return info;
		}
		Map<String, Integer> data = new HashMap<String, Integer>();
		data.put("billId", bill.getBillId());
		info.setData(data);
		info.setSuccess(true);
		info.setMessage("创建成功");
		return info;
	}
	@Override
	public ReturnInfo deleteBill(Bill bill) {
		ReturnInfo info = new ReturnInfo();
		Integer billId = bill.getBillId();
		if(billId==null){
			info.setSuccess(false);
			info.setMessage("账单Id不能为空");
			return info;
		}
		billMapper.deleteByPrimaryKey(billId);
		info.setSuccess(true);
		info.setMessage("删除成功");
		return info;
	}
	@Override
	public ReturnInfo modifyBill(GeneralCondition gcon, Bill bill) {
		ReturnInfo info = new ReturnInfo();
		Integer billId = bill.getBillId();
		if(billId==null){
			info.setSuccess(false);
			info.setMessage("账单Id不能为空");
			return info;
		}
		bill.setStatus(null);//不能修改账单状态
		billMapper.updateByPrimaryKeySelective(bill);
		
		
		//Modification start, by yang guang, 2018-10-19
		Bill billNew=billMapper.selectByPrimaryKey(billId);		
		Integer agencyId=billNew.getAgencyId();
		String caseIds=billNew.getCaseIds();
		
		//支持将账单信息同步到wpm 				
		if(Constants.DataSyn){
			Integer whdId=agencyService.getWhdAgencyId();
			if(agencyId!=null && whdId!=null && agencyId.intValue()==whdId.intValue()){					
				//将账单信息同步到wpm		
				dataSynService.billDataSyn(gcon, caseIds, billId);
			}
		}
		//Modification end	
		
		
		info.setSuccess(true);
		info.setMessage("修改成功");
		return info;
	}
	
	
	@Override
	public boolean modifyBillStatus(Integer billId,String status) {
		if(billId==null||status==null||"".equals(status)){
			return false;
		}
		Bill bill = new Bill();
		bill.setBillId(billId);
		bill.setStatus(status);
		billMapper.updateByPrimaryKeySelective(bill);
		
		
		//更新solr中的账单数据
		ReturnInfo returnInfo =updateSolrBill();
		
		boolean result=returnInfo.getSuccess();
		
		return result;
	}
	
	
	@Override
	public ReturnInfo queryUnPayedBill(GeneralCondition gcon, Bill bill,Token token) {		
		ReturnInfo info = new ReturnInfo();
		if(token==null){
			info.setSuccess(false);
			info.setMessage("查询失败");
			return info;
		}
		Integer userId = null;
		Integer level = null;
		Integer daiban=1;
		List<Bill> bills = new ArrayList<Bill>();
		Long total = 0L;
				
		if(token.isUser()){
			userId = token.getUserID();
			List<String> roles = billMapper.checkRole(userId);
			if(roles!=null&&roles.size()>0){
				for(String r: roles){
					if("代理机构管理员".equals(r)||"公司领导".equals(r)){//查询当前机构所有案件
						
						//查询给客户开的账单
						bills = billMapper.adminSelectBillForCust(gcon, bill, userId, daiban);
						List<Map<String, Long>> counts = billMapper.adminSelectBillForCustCount(gcon, bill, userId, daiban);
						for (Map<String,Long> onecount:counts) {
							total+= onecount.get("count");
						}
						
						//查询代理机构给我方开的账单
						List<Bill> bills2 = billMapper.adminSelectBillMadeByAgency(gcon, bill, userId);
						List<Map<String, Long>> counts2 = billMapper.adminSelectBillMadeByAgencyCount(gcon, bill, userId);
						for (Map<String,Long> onecount:counts2) {
							total+= onecount.get("count");
						}
						bills.addAll(bills2);
						
						info.setSuccess(true);
						info.setData(bills);
						info.setTotal(total);
						info.setCurrPage(gcon.getPageNo());
						info.setMessage("查询成功");
						return info;
					}else if("一级部门负责人".equals(r)){
						level=0;
						break;
					}else if("二级部门负责人".equals(r)){
						level=1;
						break;
					}else if("代理人".equals(r)){
						level=null;
						break;
					}
				}
			}
			
			
			//查询给客户开的账单
			bills = billMapper.selectBillForCust(gcon, bill, userId, level, daiban);
			List<Map<String, Long>> counts = billMapper.selectBillForCustCount(gcon, bill, userId, level, daiban);
			for (Map<String,Long> onecount:counts) {
				total+= onecount.get("count");
			}
			
			//查询代理机构给我方开的账单
			List<Bill> bills2 = billMapper.coagencySelectBillMadeByAgency(gcon, bill, userId, level);
			List<Map<String, Long>> counts2 = billMapper.coagencySelectBillMadeByAgencyCount(gcon, bill, userId, level);
			for (Map<String,Long> onecount:counts2) {
				total+= onecount.get("count");
			}
			bills.addAll(bills2);
		

			info.setSuccess(true);
			info.setData(bills);
			info.setTotal(total);
			info.setCurrPage(gcon.getPageNo());
			info.setMessage("查询成功");
			return info;
		}else{
			//只查询状态为未付款的账单
			bill.setStatus("未付款");
			
			Integer custId = token.getCustomerID();
			bill.setCustId(custId);			
			bills = billMapper.selectBillForCust(gcon, bill, userId, level, daiban);
			List<Map<String, Long>> counts = billMapper.selectBillForCustCount(gcon, bill, userId, level, daiban);
			for (Map<String,Long> onecount:counts) {
				total+= onecount.get("count");
			}
			info.setSuccess(true);
			info.setData(bills);
			info.setTotal(total);
			info.setCurrPage(gcon.getPageNo());
			info.setMessage("查询成功");
			return info;
		}
	}
	
	

	@Override
	public ReturnInfo queryBillList(GeneralCondition gcon, Bill bill,Token token,Integer selectType) {
		ReturnInfo info = new ReturnInfo();
		if(token==null){
			info.setSuccess(false);
			info.setMessage("查询失败");
			return info;
		}
		Integer userId = null;
		Integer level = null;
		Integer daiban = null;
		List<Bill> bills = new ArrayList<Bill>();
		Long total = 0L;
		if(token.isUser()){
			userId = token.getUserID();
			List<String> roles = billMapper.checkRole(userId);
			if(roles!=null&&roles.size()>0){
				for(String r: roles){
					if("代理机构管理员".equals(r)||"公司领导".equals(r)){//查询当前机构所有案件
						if(selectType==null){//查询所有账单
							bills = billMapper.adminSelectBillForCust(gcon, bill, userId, daiban);
							List<Map<String, Long>> counts = billMapper.adminSelectBillForCustCount(gcon, bill, userId, daiban);
							for (Map<String,Long> onecount:counts) {
								total+= onecount.get("count");
							}
							List<Bill> bills2 = billMapper.adminSelectBillMadeByAgency(gcon, bill, userId);
							List<Map<String, Long>> counts2 = billMapper.adminSelectBillMadeByAgencyCount(gcon, bill, userId);
							for (Map<String,Long> onecount:counts2) {
								total+= onecount.get("count");
							}
							bills.addAll(bills2);
						}else if(selectType==1){//查询给客户开的账单
							bills = billMapper.adminSelectBillForCust(gcon, bill, userId, daiban);
							List<Map<String, Long>> counts = billMapper.adminSelectBillForCustCount(gcon, bill, userId, daiban);
							for (Map<String,Long> onecount:counts) {
								total+= onecount.get("count");
							}
						}else if(selectType==2){//查询代理机构给我方开的账单
							bills = billMapper.adminSelectBillMadeByAgency(gcon, bill, userId);
							List<Map<String, Long>> counts = billMapper.adminSelectBillMadeByAgencyCount(gcon, bill, userId);
							for (Map<String,Long> onecount:counts) {
								total+= onecount.get("count");
							}
						}else{
							info.setSuccess(false);
							info.setMessage("查询失败");
							return info;
						}
						info.setSuccess(true);
						info.setData(bills);
						info.setTotal(total);
						info.setCurrPage(gcon.getPageNo());
						info.setMessage("查询成功");
						return info;
					}else if("一级部门负责人".equals(r)){
						level=0;
						break;
					}else if("二级部门负责人".equals(r)){
						level=1;
						break;
					}else if("代理人".equals(r)){
						level=null;
						break;
					}
				}
			}
			if(selectType==null){//查询所有账单
				bills = billMapper.selectBillForCust(gcon, bill, userId, level, daiban);
				List<Map<String, Long>> counts = billMapper.selectBillForCustCount(gcon, bill, userId, level, daiban);
				for (Map<String,Long> onecount:counts) {
					total+= onecount.get("count");
				}
				List<Bill> bills2 = billMapper.coagencySelectBillMadeByAgency(gcon, bill, userId, level);
				List<Map<String, Long>> counts2 = billMapper.coagencySelectBillMadeByAgencyCount(gcon, bill, userId, level);
				for (Map<String,Long> onecount:counts2) {
					total+= onecount.get("count");
				}
				bills.addAll(bills2);
			}else if(selectType==1){//查询给客户开的账单
				bills = billMapper.selectBillForCust(gcon, bill, userId, level, daiban);
				List<Map<String, Long>> counts = billMapper.selectBillForCustCount(gcon, bill, userId, level, daiban);
				for (Map<String,Long> onecount:counts) {
					total+= onecount.get("count");
				}
			}else if(selectType==2){//查询代理机构给我方开的账单
				bills = billMapper.coagencySelectBillMadeByAgency(gcon, bill, userId, level);
				List<Map<String, Long>> counts = billMapper.coagencySelectBillMadeByAgencyCount(gcon, bill, userId, level);
				for (Map<String,Long> onecount:counts) {
					total+= onecount.get("count");
				}
			}else{
				info.setSuccess(false);
				info.setMessage("查询失败");
				return info;
			}
			info.setSuccess(true);
			info.setData(bills);
			info.setTotal(total);
			info.setCurrPage(gcon.getPageNo());
			info.setMessage("查询成功");
			return info;
		}else{
			
			daiban=1;
			
			Integer custId = token.getCustomerID();
			bill.setCustId(custId);
			
			bills = billMapper.selectBillForCust(gcon, bill, userId, level, daiban);
			List<Map<String, Long>> counts = billMapper.selectBillForCustCount(gcon, bill, userId, level, daiban);
			for (Map<String,Long> onecount:counts) {
				total+= onecount.get("count");
			}
			info.setSuccess(true);
			info.setData(bills);
			info.setTotal(total);
			info.setCurrPage(gcon.getPageNo());
			info.setMessage("查询成功");
			return info;
		}
	}
	@Override
	public ReturnInfo queryBillDetail(Bill bill) {
		ReturnInfo info = new ReturnInfo();
		Integer billId = bill.getBillId();
		if(billId==null){
			info.setSuccess(false);
			info.setMessage("账单Id不能为空");
			return info;
		}
		Bill data = billMapper.selectByPrimaryKey(billId);
		info.setSuccess(true);
		info.setData(data);
		info.setMessage("查询成功");
		return info;
	}
	@Override
	public ReturnInfo exportBill( Bill bill) {
		ReturnInfo info = new ReturnInfo();
		return info;
	}
	/**
	 * 此方法暂时未用
	 */
	@Override
	public ReturnInfo caseGenerateBill(String caseIds) {
		ReturnInfo info = new ReturnInfo();
		Bill bill= new Bill();
		try{
			String[] caseIdArr = null;
			if(caseIds!=null){
				caseIdArr = caseIds.split(",");
			}else {
				info.setSuccess(false);
				info.setMessage("请传入案件ID");
				return info;
			}
			bill = billMapper.generateBillByCases(caseIdArr);
			bill.setStatus("待审核");
		}catch(Exception e){
			e.printStackTrace();
			info.setSuccess(false);
			info.setMessage("请传入正确的案件ID");
			return info;
		}
		info.setSuccess(true);
		info.setData(bill);
		info.setMessage("查询成功");
		return info;
	}
	/**
	 * 此方法暂时未用
	 */
	@Override
	public ReturnInfo recordGenerateBill(String chargeRecordIds) {
		ReturnInfo info = new ReturnInfo();
		Bill bill= new Bill();
		try{
			String[] chargeRecordIdArr = null;
			if(chargeRecordIds!=null){
				chargeRecordIdArr = chargeRecordIds.split(",");
			}else {
				info.setSuccess(false);
				info.setMessage("请传入案件ID");
				return info;
			}
			bill = billMapper.generateBillByChargeRecords(chargeRecordIdArr);
			bill.setStatus("待审核");
		}catch(Exception e){
			e.printStackTrace();
			info.setSuccess(false);
			info.setMessage("请传入正确的案件ID");
			return info;
		}
		info.setSuccess(true);
		info.setData(bill);
		info.setMessage("查询成功");
		return info;
	}

//	@Override
//	public ReturnInfo statsByCustomer(Integer custId, GeneralCondition gcon, String interfacetype) {
//		ReturnInfo info = new ReturnInfo();
//		List<ReturnBillAmount>  rb = billAmountMapper.selectStatsByCustomer(custId,gcon,interfacetype);
//		info.setSuccess(true);
//		info.setData(rb);
//		info.setMessage("查询成功");
//		return info;
//	}
//
//	@Override
//	public ReturnInfo statsByAgencyUser(Integer userId, Integer agencyId, GeneralCondition gcon, String interfacetype) {
//		ReturnInfo info = new ReturnInfo();
//		List<ReturnBillAmount>  rb = billAmountMapper.selectStatsByAgencyUser(userId,agencyId,gcon,interfacetype);
//		info.setSuccess(true);
//		info.setData(rb);
//		info.setMessage("查询成功");
//		return info;
//	}
//
//	@Override
//	public ReturnInfo statsByAgency(Integer agencyId, GeneralCondition gcon, String interfacetype) {
//		ReturnInfo info = new ReturnInfo();
//		List<ReturnBillAmount>  rb = billAmountMapper.selectStatsByAgency(agencyId,gcon,interfacetype);
//		info.setSuccess(true);
//		info.setData(rb);
//		info.setMessage("查询成功");
//		return info;
//	}
//	@Override
//	public ReturnInfo statsBillTop10(GeneralCondition gcon) {
//		ReturnInfo info = new ReturnInfo();
//		List<ReturnBillAmount>  rb = billAmountMapper.statsBillTop10(gcon);
//		info.setSuccess(true);
//		info.setData(rb);
//		info.setMessage("查询成功");
//		return info;
//	}
	@Override
	public ReturnInfo generateBillNo(Integer billId) {
		ReturnInfo info = new ReturnInfo();
		if(billId==null){
			info.setSuccess(false);
			info.setMessage("账单编号不能为空");
			return info;
		}
		String nextBillNo = "";
		//查出该代理机构，在该年中最大的billNo
		String billNoMax = billMapper.selectMaxBillNo(billId);
		if(billNoMax==null){//当年还没有值
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			String year = sdf.format(new Date());
			nextBillNo = year+"000001";
		}else {
			try{
				nextBillNo = String.valueOf(Integer.parseInt(billNoMax)+1);
			}catch(Exception e){
				info.setSuccess(false);
				info.setMessage("账单号生成失败");
				return info;
			}
		}
		Map<String, String> data = new HashMap<String, String>();
		data.put("billNo", nextBillNo);
			
		info.setSuccess(true);
		info.setData(data);
		info.setMessage("账单号生成完毕");
		return info;
	}
	
	
	
	
	@Override
	public ReturnInfo queryBillBySolr(Integer  departId,Integer custId,Integer agencyId,Integer bodytype,
			BillSolr bill,Integer timetype ,String startYears, String endYears,GeneralCondition gcon){
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			//if(custId == null) throw new Exception("客户信息有误");
			
			SolrSendBill solr = new SolrSendBill();
			
			String startYear =getstartYear(startYears,timetype);
			String endYear =getendYear(endYears,timetype);
			
			Integer coagencyId = agencyId;
			Integer receiverType = null;
			
			if (bodytype.equals(1)) {
				//代理所客户往来
				receiverType=1;
				custId =null;
				coagencyId = null;
			}else if (bodytype.equals(2)) {
				//代理所发送代理所
				custId =null;
				coagencyId = null;
				receiverType = 2;
			}else if (bodytype.equals(3)) {
				//代理所接收代理所
				custId =null;
				departId = null;
				agencyId = null;
			}else {
				//客户
				departId = null;
				coagencyId = null;
			}
			
			
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			returnInfo.setCurrPage(gcon.getPageNo());
			return solr.selectBill(gcon, departId, custId, agencyId, coagencyId, receiverType, bill, startYear, endYear,
					"createDate", solrInfo, returnInfo);
			
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误"+":"+e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}
	
	@Override
	public ReturnInfo updateSolrBill() {
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			//创建主表，由于信息过大所以分片查询
			SolrData bill = new SolrData();
			Boolean goon = true;
			int i = 0;
			List<Map<String,Object>> billtable = new ArrayList<Map<String,Object>>();
			while(goon) {
				List<Map<String,Object>> billtablePart = billSolrMapper.selectAllBill(i);
				if(billtablePart.size()<50000) goon = false;
				billtable.addAll(billtablePart);
				i=i+50000;
			}
			bill.setDataTable(billtable);
			bill.setIdName("billId");
			//创建商标其他信息表队列
			List<SolrData> otherData = new ArrayList<SolrData>();
			SolrData billDepart = new SolrData();
			goon = true;
			i = 0;
			List<Map<String,Object>> billDeparttable = new ArrayList<Map<String,Object>>();
			while(goon) {
				List<Map<String,Object>> billtableDepartPart = billSolrMapper.selectAllDepart(i);
				if(billtableDepartPart.size()<50000) goon = false;
				billDeparttable.addAll(billtableDepartPart);
				i=i+50000;
			}
			billDepart.setDataTable(billDeparttable);
			billDepart.setIdName("billId");
			otherData.add(billDepart);
			SolrSendBill solr = new SolrSendBill();
			solr.createDocs(solrInfo, bill, otherData);

			returnInfo.setSuccess(true);
			returnInfo.setMessage("更新成功");
			return returnInfo;
		} catch(Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("更新失败:");
			e.printStackTrace();
			return returnInfo;
		}

	}

	@Override
	public ReturnInfo statsBillagency(Integer custId, String startYears, String endYears){
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			//if(custId == null) throw new Exception("客户信息有误");
			
			SolrSendBill solr = new SolrSendBill();
			String startYear =getstartYear(startYears,0);
			String endYear =getendYear(endYears,0);
			
			returnInfo.setData(MapToList(solr.SelectBillByList(solrInfo, null, custId, null,null,null, startYear, endYear, "agencyName", "createDate",null)));
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			return returnInfo;
			
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误"+":"+e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}
	
	@Override
	public ReturnInfo statsBillProInfo(Integer custId,Integer agencyId,Integer departId){
		ReturnInfo returnInfo = new ReturnInfo();
		try {

			
			SolrSendBill solr = new SolrSendBill();
//			String startYear =getstartYear(startYears,0);
//			String endYear =getendYear(endYears,0);
			
			if(departId==null)departId=0;
			Map<String, Object> data = new HashMap<String, Object>();
			
			long finishedCount=(long)solr.SelectBillCount(solrInfo, custId, agencyId,departId, null,1, null, null,  "createDate","status:已付款");			
//			long unFinishedCount=(long)solr.SelectBillCount(solrInfo, custId, agencyId,departId, null,1, null, null,  "createDate","(status:未付款 OR status:待审核)");
			long unFinishedCount=(long)solr.SelectBillCount(solrInfo, custId, agencyId,departId, null,1, null, null,  "createDate","status:未付款");
			
			long totalCount=finishedCount+unFinishedCount;
			
			data.put("已付款", finishedCount);
			data.put("未付款", unFinishedCount);
			data.put("总数", totalCount);
			

			returnInfo.setData(data);
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			return returnInfo;
			
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误"+":"+e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}
	
	@Override
	public ReturnInfo statsBillbillType(Integer custId,Integer agencyId,Integer departId, String startYears, String endYears,Integer timetype){
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			String field = "";			
			if (timetype.equals(1)) {
				field = "createDateYearQuarter";
			}else if (timetype.equals(2)) {
				field = "createDateYearMonth";
			}else {
				field = "createDateYear";
			}

			
			SolrSendBill solr = new SolrSendBill();
			String startYear =getstartYear(startYears,0);
			String endYear =getendYear(endYears,0);
			
			List<String> fields = new ArrayList<String>();
			fields.add(field);
			fields.add("billType");
			
			List<Map<String, Object>> data = solr.SelectBillByListPovit(solrInfo, departId, custId, agencyId,null,1, startYear, endYear, fields, "createDate",null);
			
			if(data==null) {
				returnInfo.setData(fullList(data,timetype,startYears, endYears));
			}else {
			
				if (!timetype.equals(0)) {
					Collections.sort(data, new SortByMonth());
				}else {
					Collections.sort(data, new SortByYear());
				}
				returnInfo.setData(fullList(data,timetype,startYears, endYears));
			}
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			return returnInfo;
			
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误"+":"+e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}
	
	@Override
	public ReturnInfo statsBillcustomer(Integer agencyId,Integer departId, String startYears, String endYears,Integer timetype){
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			String field = "";			
			if (timetype.equals(1)) {
				field = "createDateYearQuarter";
			}else if (timetype.equals(2)) {
				field = "createDateYearMonth";
			}else {
				field = "createDateYear";
			}
			
			SolrSendBill solr = new SolrSendBill();
			String startYear =getstartYear(startYears,0);
			String endYear =getendYear(endYears,0);
			
			List<String> fields = new ArrayList<String>();
			fields.add(field);
			fields.add("customer");
			
			List<Map<String, Object>> data = solr.SelectBillByListPovit(solrInfo, departId, null, agencyId,null,1, startYear, endYear, fields, "createDate",null);
			
			
			
			if (!timetype.equals(0)) {
				Collections.sort(data, new SortByMonth());
			}else {
				Collections.sort(data, new SortByYear());
			}
			returnInfo.setData(fullList(data,timetype,startYears, endYears));
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			return returnInfo;
			
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误"+":"+e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}
	
	@Override
	public ReturnInfo statsBillcreaterName(Integer custId,Integer agencyId,Integer departId, String startYears, String endYears,Integer timetype){
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			String field = "";			
			if (timetype.equals(1)) {
				field = "createDateYearQuarter";
			}else if (timetype.equals(2)) {
				field = "createDateYearMonth";
			}else {
				field = "createDateYear";
			}
			
			SolrSendBill solr = new SolrSendBill();
			String startYear =getstartYear(startYears,0);
			String endYear =getendYear(endYears,0);
			
			List<String> fields = new ArrayList<String>();
			fields.add(field);
			fields.add("createrName");
			
			List<Map<String, Object>> data = solr.SelectBillByListPovit(solrInfo, departId, custId, agencyId,null,1, startYear, endYear, fields, "createDate",null);
			

			if(data==null) {
				returnInfo.setData(fullList(data,timetype,startYears, endYears));
			}else {
			
				if (!timetype.equals(0)) {
					Collections.sort(data, new SortByMonth());
				}else {
					Collections.sort(data, new SortByYear());
				}
				returnInfo.setData(fullList(data,timetype,startYears, endYears));
			}
			
//			if (!timetype.equals(0)) {
//				Collections.sort(data, new SortByMonth());
//			}else {
//				Collections.sort(data, new SortByYear());
//			}
//			returnInfo.setData(fullList(data,timetype,startYears, endYears));
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			return returnInfo;
			
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误"+":"+e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}
	
	@Override
	public ReturnInfo statsBillmonth(Integer agencyId,Integer departId,Integer timetype ,String startYears, String endYears){
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			//if(custId == null) throw new Exception("客户信息有误");
			String field = "";			
			if (timetype.equals(1)) {
				field = "createDateYearQuarter";
			}else if (timetype.equals(2)) {
				field = "createDateYearMonth";
			}else {
				field = "createDateYear";
			}
			SolrSendBill solr = new SolrSendBill();

			String startYear =getstartYear(startYears,timetype);
			String endYear =getendYear(endYears,timetype);
			
			Map<String,Map<String, Object>> selectLists = new HashMap<String,Map<String, Object>>();
			selectLists.put("cust", fullmap(solr.SelectBillByList(solrInfo, departId, null, agencyId,null,1, startYear, endYear, field, "createDate",null),timetype,startYears,endYears));
			selectLists.put("inagency", fullmap(solr.SelectBillByList(solrInfo, departId, null, agencyId,null,2, startYear, endYear, field, "createDate",null),timetype,startYears,endYears));
			selectLists.put("outagency", fullmap(solr.SelectBillByList(solrInfo, departId, null, null,agencyId,null, startYear, endYear, field, "createDate",null),timetype,startYears,endYears));

			List<Map<String, Object>> data = MapToLists(selectLists);
			if (!timetype.equals(0)) {
				Collections.sort(data, new SortByMonth());
			}else {
				Collections.sort(data, new SortByYear());
			}
			returnInfo.setData(data);
			
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			return returnInfo;
			
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误"+":"+e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}
	
	@Override
	public ReturnInfo statsBillmonthcust(Integer agencyId,Integer departId,Integer custId,Integer timetype ,String startYears, String endYears){
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			//if(custId == null) throw new Exception("客户信息有误");
			String field = "";
			
			if (timetype.equals(1)) {
				field = "createDateYearQuarter";
			}else if (timetype.equals(2)) {
				field = "createDateYearMonth";
			}else {
				field = "createDateYear";
			}
			SolrSendBill solr = new SolrSendBill();

			String startYear =getstartYear(startYears,timetype);
			String endYear =getendYear(endYears,timetype);
			
			Map<String,Map<String, Object>> selectLists = new HashMap<String,Map<String, Object>>();
			selectLists.put("pay", fullmap(solr.SelectBillByList(solrInfo, departId, custId, agencyId,null,1, startYear, endYear, field, "createDate","status:已付款"),timetype,startYears,endYears));
			selectLists.put("nopay", fullmap(solr.SelectBillByList(solrInfo, departId, custId, agencyId,null,1, startYear, endYear, field, "createDate","status:未付款"),timetype,startYears,endYears));

			List<Map<String, Object>> data = MapToLists(selectLists);
			if (!timetype.equals(0)) {
				Collections.sort(data, new SortByMonth());
			}else {
				Collections.sort(data, new SortByYear());
			}
			returnInfo.setData(data);
			
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			return returnInfo;
			
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误"+":"+e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}
	
	
	
	@Override
	public ReturnInfo statsBillmonthcoagency(Integer agencyId,Integer departId,Integer timetype ,String startYears, String endYears){
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			//if(custId == null) throw new Exception("客户信息有误");
			String field = "";
			
			if (timetype.equals(1)) {
				field = "createDateYearQuarter";
			}else if (timetype.equals(2)) {
				field = "createDateYearMonth";
			}else {
				field = "createDateYear";
			}
			SolrSendBill solr = new SolrSendBill();
			
			String startYear =getstartYear(startYears,timetype);
			String endYear =getendYear(endYears,timetype);
			
			Map<String,Map<String, Object>> selectLists = new HashMap<String,Map<String, Object>>();
			selectLists.put("pay", fullmap(solr.SelectBillByList(solrInfo, null, null, null,agencyId,null, startYear, endYear, field, "createDate","status:已付款"),timetype,startYears,endYears));
			selectLists.put("nopay", fullmap(solr.SelectBillByList(solrInfo, null, null, null,agencyId,null, startYear, endYear, field, "createDate","status:未付款"),timetype,startYears,endYears));

			List<Map<String, Object>> data = MapToLists(selectLists);
			if (!timetype.equals(0)) {
				Collections.sort(data, new SortByMonth());
			}else {
				Collections.sort(data, new SortByYear());
			}
			returnInfo.setData(data);
			
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			return returnInfo;
			
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误"+":"+e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}
	
	@Override
	public ReturnInfo statsBillmonthagency(Integer agencyId,Integer departId,Integer timetype ,String startYears, String endYears){
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			//if(custId == null) throw new Exception("客户信息有误");
			String field = "";
			
			if (timetype.equals(1)) {
				field = "createDateYearQuarter";
			}else if (timetype.equals(2)) {
				field = "createDateYearMonth";
			}else {
				field = "createDateYear";
			}
			SolrSendBill solr = new SolrSendBill();

			String startYear =getstartYear(startYears,timetype);
			String endYear =getendYear(endYears,timetype);
			
			Map<String,Map<String, Object>> selectLists = new HashMap<String,Map<String, Object>>();
			selectLists.put("pay", fullmap(solr.SelectBillByList(solrInfo, departId, null, agencyId,null,2, startYear, endYear, field, "createDate","status:已付款"),timetype,startYears,endYears));
			selectLists.put("nopay", fullmap(solr.SelectBillByList(solrInfo, departId, null, agencyId,null,2, startYear, endYear, field, "createDate","status:未付款"),timetype,startYears,endYears));

			List<Map<String, Object>> data = MapToLists(selectLists);
			if (!timetype.equals(0)) {
				Collections.sort(data, new SortByMonth());
			}else {
				Collections.sort(data, new SortByYear());
			}
			returnInfo.setData(data);
			
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			return returnInfo;
			
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误"+":"+e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}
	
	private String getendYear(String endYears, Integer timetype) throws ParseException {
		String endYear = "";
		if (timetype.equals(1)) {
			
        	int l1 =endYears.indexOf('-');
        	Integer i1 = Integer.valueOf(endYears.substring(l1, endYears.length()));
        	
        	SimpleDateFormat df = new SimpleDateFormat("yyyy");
			Date date=new Date();
			date = df.parse(endYears.substring(0, l1));
        	

			
			
			endYear = SolrUtil.getSolrDate(getLastDateOfMonth(getSeasonDate(date,i1)[2]));
		}else if (timetype.equals(2)) {
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
			Date date=new Date();
			date = df.parse(endYears);
			
			
			endYear = SolrUtil.getSolrDate(getLastDateOfMonth(date));
		}else {			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
			Date date=new Date();
			date = df.parse(endYears+"-12");
			
			
			endYear = SolrUtil.getSolrDate(getLastDateOfMonth(date));
		}
		return endYear;
	}

	private String getstartYear(String startYears, Integer timetype) throws ParseException {
		String startYear = "";
		if (timetype.equals(1)) {
			
        	int l1 =startYears.indexOf('-');
        	Integer i1 = Integer.valueOf(startYears.substring(l1, startYears.length()));
        	
        	SimpleDateFormat df = new SimpleDateFormat("yyyy");
			Date date=new Date();
			date = df.parse(startYears.substring(0, l1));
        	

			
			
			startYear = SolrUtil.getSolrDate(getFirstDateOfMonth(getSeasonDate(date,i1)[0]));
		}else if (timetype.equals(2)) {
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
			Date date=new Date();
			date = df.parse(startYears);
			
			
			startYear = SolrUtil.getSolrDate(getFirstDateOfMonth(date));
		}else {			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
			Date date=new Date();
			date = df.parse(startYears+"-01");
			
			
			startYear = SolrUtil.getSolrDate(getFirstDateOfMonth(date));
		}
		return startYear;
	}
	public static Date getFirstDateOfMonth(Date date) {  
        Calendar c = Calendar.getInstance();  
        c.setTime(date);  
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));  
        return c.getTime();  
    }  
	
    public static Date getLastDateOfMonth(Date date) {  
        Calendar c = Calendar.getInstance();  
        c.setTime(date);  
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));  
        return c.getTime();  
    } 
    
    public static Date[] getSeasonDate(Date date,int nSeason) {  
        Date[] season = new Date[3];  
  
        Calendar c = Calendar.getInstance();  
        c.setTime(date); 
        if (nSeason == 1) {// 第一季度  
            c.set(Calendar.MONTH, Calendar.JANUARY);  
            season[0] = c.getTime();  
            c.set(Calendar.MONTH, Calendar.FEBRUARY);  
            season[1] = c.getTime();  
            c.set(Calendar.MONTH, Calendar.MARCH);  
            season[2] = c.getTime();  
        } else if (nSeason == 2) {// 第二季度  
            c.set(Calendar.MONTH, Calendar.APRIL);  
            season[0] = c.getTime();  
            c.set(Calendar.MONTH, Calendar.MAY);  
            season[1] = c.getTime();  
            c.set(Calendar.MONTH, Calendar.JUNE);  
            season[2] = c.getTime();  
        } else if (nSeason == 3) {// 第三季度  
            c.set(Calendar.MONTH, Calendar.JULY);  
            season[0] = c.getTime();  
            c.set(Calendar.MONTH, Calendar.AUGUST);  
            season[1] = c.getTime();  
            c.set(Calendar.MONTH, Calendar.SEPTEMBER);  
            season[2] = c.getTime();  
        } else if (nSeason == 4) {// 第四季度  
            c.set(Calendar.MONTH, Calendar.OCTOBER);  
            season[0] = c.getTime();  
            c.set(Calendar.MONTH, Calendar.NOVEMBER);  
            season[1] = c.getTime();  
            c.set(Calendar.MONTH, Calendar.DECEMBER);  
            season[2] = c.getTime();  
        }  
        return season;  
    }  
	class SortByYear implements Comparator <Map<String, Object>> {
        public int compare(Map<String, Object> o1, Map<String, Object> o2) {
            return Integer.valueOf(o2.get("name").toString()).compareTo(Integer.valueOf(o1.get("name").toString()));
        }
	}
	class SortByMonth implements Comparator <Map<String, Object>> {
        public int compare(Map<String, Object> o1, Map<String, Object> o2) {

        	
        	String  s2 =o2.get("name").toString();
        	int l2 =s2.indexOf('-');
        	Integer i2 = Integer.valueOf(s2.substring(l2+1, s2.length()));
        	Integer y2 =Integer.valueOf(s2.substring(0, l2));
        	
        	String  s1 =o1.get("name").toString();
        	int l1 =s1.indexOf('-');
        	Integer i1 = Integer.valueOf(s1.substring(l1+1, s1.length()));
        	Integer y1 =Integer.valueOf(s1.substring(0, l1));
        	
        	if(y1.intValue()!=y2.intValue()) return y1.compareTo(y2); else return i1.compareTo(i2);
        	
            
        }
	}
	private List<Map<String, Object>>  fullList(List<Map<String, Object>> list, Integer timetype, String startYears,
			String endYears){
		Map<String, Object> maplist = new HashMap<String, Object> ();
		if(list == null) {
			
		}else {
			for(Map<String, Object> map:list) {
				
				maplist.put(map.get("name").toString(), map.get("value"));
				
			}
		}
		Map<String, Object> retmap = fullmap(maplist,timetype,startYears,endYears);
		
		List<Map<String, Object>> retlist = new ArrayList<Map<String, Object>> ();
		
		for(Entry<String, Object> ret:retmap.entrySet()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", ret.getKey());
			map.put("value", ret.getValue());
			retlist.add(map);
			
		}
		retlist = fullTwoDeep(retlist);
		
		
		if (!timetype.equals(0)) {
			Collections.sort(retlist, new SortByMonth());
		}else {
			Collections.sort(retlist, new SortByYear());
		}
		
		return retlist;
	}
	
	
	private Map<String, Object> fullmap(Map<String, Object> selectBillByList, Integer timetype, String startYears,
			String endYears) {
		Map<String,Object> nul = new HashMap<String,Object>();
		
		nul.put("count", 0);
		nul.put("sum", 0);
		
		Map<String,Integer> startYearm = new HashMap<String,Integer>();
		Map<String,Integer> endYearm = new HashMap<String,Integer>();
		if(timetype.equals(1)) {
			       	
        	int startl =startYears.indexOf('-');
        	startYearm.put("quarter",Integer.valueOf(startYears.substring(startl+1, startYears.length())));
        	startYearm.put("year",Integer.valueOf(startYears.substring(0, startl)));
        	
        	int endl =endYears.indexOf('-');
        	endYearm.put("quarter",Integer.valueOf(endYears.substring(endl+1, endYears.length())));
        	endYearm.put("year",Integer.valueOf(endYears.substring(0, endl)));
				
			
			for(int i = 0;checkquarter(startYearm,i,endYearm);i++) {
				if (selectBillByList.get(printquarter(startYearm,i)) == null) {
					selectBillByList.put(printquarter(startYearm,i), nul);
				}
			}

			
			
			
			
			
		} else if(timetype.equals(2)) {
			
			
        	int startl =startYears.indexOf('-');
        	startYearm.put("month",Integer.valueOf(startYears.substring(startl+1, startYears.length())));
        	startYearm.put("year",Integer.valueOf(startYears.substring(0, startl)));
        	
        	int endl =endYears.indexOf('-');
        	endYearm.put("month",Integer.valueOf(endYears.substring(endl+1, endYears.length())));
        	endYearm.put("year",Integer.valueOf(endYears.substring(0, endl)));
			
			for(int i = 0;checkmonth(startYearm,i,endYearm);i++) {
				if (selectBillByList.get(printmonth(startYearm,i)) == null) {
					selectBillByList.put(printmonth(startYearm,i), nul);
				}
			}
		} else {
			Integer startYear = Integer.valueOf(startYears);
			Integer endYear = Integer.valueOf(endYears);
			
			
			for(int i = 0;i<endYear - startYear+1;i++) {
				if (selectBillByList.get(String.valueOf(startYear+i)) == null) {
					selectBillByList.put(String.valueOf(startYear+i), nul);
				}
			}
		}
		return selectBillByList;
	}
	private String printquarter(Map<String, Integer> startYearm, int i) {
		Integer startquarter =  startYearm.get("quarter")-1;
		Integer startYear = startYearm.get("year");
		//String.format("%02d",x)
		// TODO 自动生成的方法存根
		
		
		
		
		return String.valueOf(startYear+((startquarter+i)/4))+"-"+String.valueOf((startquarter+i)%4+1);
	}
	private Boolean checkquarter(Map<String, Integer> startYearm, int i, Map<String, Integer> endYearm) {
		Boolean ret = false;
		
		Integer startquarter =  startYearm.get("quarter")-1;
		Integer startYear = startYearm.get("year");
		
		if(startYear+((startquarter+i)/4) > endYearm.get("year")) return false;
		if((((startquarter+i)%4)+1 > endYearm.get("quarter")) && (startYear+((startquarter+i)/4) == endYearm.get("year")))return false;
		
		
		return true;
	}
	
	
	private String printmonth(Map<String, Integer> startYearm, int i) {
		Integer startquarter =  startYearm.get("month")-1;
		Integer startYear = startYearm.get("year");
		//String.format("%02d",x)
		// TODO 自动生成的方法存根
		
		
		
		
		return String.valueOf(startYear+((startquarter+i)/12))+"-"+String.format("%02d",(startquarter+i)%12+1);
	}
	private Boolean checkmonth(Map<String, Integer> startYearm, int i, Map<String, Integer> endYearm) {
		
		Integer startquarter =  startYearm.get("month")-1;
		Integer startYear = startYearm.get("year");
		
		if(startYear+((startquarter+i)/12) > endYearm.get("year")) return false;
		if((((startquarter+i)%12)+1 > endYearm.get("month")) && (startYear+((startquarter+i)/12) == endYearm.get("year")))return false;
		
		
		return true;
	}
	private Object MapToList(Map<String, Object> selectList) {
		List<Map<String, Object>> retlist = new ArrayList<Map<String, Object>>();
		for(Entry<String, Object> select:selectList.entrySet()) {
			Map<String, Object> ret = new HashMap<String, Object>();
			
			ret.put("name", select.getKey());
			if(select.getValue() instanceof Map) {
				ret.putAll((Map<String,Object>) select.getValue());
			} else {
				ret.put("value", select.getValue());
			}
			retlist.add(ret);
		}
		
		return retlist;
	}
	
	private List<Map<String,Object>> MapToLists(Map<String,Map<String, Object>> selectLists) {
		List<Map<String, Object>> retlist = new ArrayList<Map<String, Object>>();
		
		String[] keys = selectLists.keySet().toArray(new String[0]);
		
		for(Entry<String, Object> select0:selectLists.get(keys[0]).entrySet()) {
			Map<String, Object> ret = new HashMap<String, Object>();
			
			ret.put("name", select0.getKey());
			for(Entry<String,Map<String, Object>> selectList:selectLists.entrySet()) {
				
				Object select = selectList.getValue().get(select0.getKey());
				ret.put(selectList.getKey(), selectList.getValue().get(select0.getKey()));
			}
			retlist.add(ret);
		}
		
		return retlist;
	}
	private List<Map<String, Object>> fullTwoDeep(List<Map<String, Object>> list) {
		List<String> titlelist = new ArrayList<String>();
		List<Map<String, Object>> retlist = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> a: list) {				
			if(a.get("value") instanceof  List) {
				for(Map<String, Object> b:(List<Map<String, Object>>) a.get("value")) {
					if(!titlelist.contains(b.get("name"))) {
						titlelist.add(b.get("name").toString());
					}
				}
			}				
		}
		for(Map<String, Object> a:(List<Map<String, Object>>) list) {				
			if(a.get("value") instanceof  List) {
				for(String title:titlelist) {
					if(!mapexitvalue((List<Map<String, Object>>)a.get("value"),title)) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("count", 0);
						map.put("sum", Double.valueOf(0.0));
						map.put("name", title);
						((List<Map<String, Object>>)a.get("value")).add(map);
					}
				}
				
			} else {
				List<Map<String, Object>> maplist = new ArrayList<Map<String, Object>>();
				for(String title:titlelist) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("count", 0);
					map.put("sum", Double.valueOf(0.0));
					map.put("name", title);
					maplist.add(map);
				}
				a.put("value", maplist);
			}
			retlist.add(a);
		}
		return retlist;
	}
	private boolean mapexitvalue(List<Map<String, Object>> list, String title) {
		for(Map<String, Object> a:list) {
			if(a.get("name").toString().equals(title)) return true;
		}
		return false;
	}
}
