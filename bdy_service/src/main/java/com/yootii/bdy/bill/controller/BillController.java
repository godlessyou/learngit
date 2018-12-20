package com.yootii.bdy.bill.controller;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yootii.bdy.bill.model.Bill;
import com.yootii.bdy.bill.model.BillSolr;
import com.yootii.bdy.bill.model.ChargeRecord;
import com.yootii.bdy.bill.service.BillService;
import com.yootii.bdy.bill.service.ChargeRecordService;
import com.yootii.bdy.common.CommonController;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.security.service.AuthenticationService;

@Controller
@RequestMapping("/interface/bill")
public class BillController extends CommonController{
	private final Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private BillService  billService;

	@Resource
	private ChargeRecordService chargeRecordService;
	
	// 增加更新solr中的账单数据的接口
	@RequestMapping(value = "/updateBill", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo updateSolrBill(GeneralCondition gcon,HttpServletRequest request) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				//Integer userId = ((Token)rtnInfo.getData()).getUserID();
				Token token = (Token)rtnInfo.getData();
				this.addToken(token);
				this.addURL(request.getRequestURI());
				rtnInfo = billService.updateSolrBill();
			}
			catch (Exception e) {
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}

	// 增加账单
	@RequestMapping(value = "/createbill", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo createBill(GeneralCondition gcon,HttpServletRequest request, Bill bill,String chargeRecordIds) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				//Integer userId = ((Token)rtnInfo.getData()).getUserID();
				Token token = (Token)rtnInfo.getData();
				this.addToken(token);
				this.addURL(request.getRequestURI());
				rtnInfo = billService.createBill(gcon, bill,chargeRecordIds);
			}
			catch (Exception e) {
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	// 删除账单
	@RequestMapping(value = "/deletebill", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo deleteBill(GeneralCondition gcon,HttpServletRequest request, Bill bill) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				Token token = (Token)rtnInfo.getData();
				this.addToken(token);
				this.addURL(request.getRequestURI());
				 ThreadLocal<Object> local = new ThreadLocal<>();
				 local.set(1);
				rtnInfo = billService.deleteBill(bill);
			}
			catch (Exception e) {
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	// 修改账单
	@RequestMapping(value = "/modifybill", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo modifyBill(GeneralCondition gcon,HttpServletRequest request, Bill bill) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				Token token = (Token)rtnInfo.getData();
				
				this.addURL(request.getRequestURI());
				this.addToken(token);
				rtnInfo = billService.modifyBill(gcon, bill);
			}
			catch (Exception e) {
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	// 查询账单列表
	@RequestMapping(value = "/querybilllist", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryBillList(GeneralCondition gcon,Integer selectType, Bill bill) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		makeOffsetAndRows(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { //通过身份验证
			try {
				Token token = (Token)rtnInfo.getData();
				rtnInfo = billService.queryBillList(gcon,bill,token,selectType);
			}
			catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	// 查询账单详情
	@RequestMapping(value = "/querybilldetail", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryBillDetail(GeneralCondition gcon,HttpServletRequest request, Bill bill) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				rtnInfo = billService.queryBillDetail(bill);
			}
			catch (Exception e) {
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	
	
	
	// 查询未付款的账单列表
	@RequestMapping(value = "/queryUnPayedBill", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryUnPayedBill(GeneralCondition gcon,Bill bill) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		makeOffsetAndRows(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { //通过身份验证
			try {
				Token token = (Token)rtnInfo.getData();
				rtnInfo = billService.queryUnPayedBill(gcon,bill,token);
			}
			catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	
	
	
	
	
	// 创建账单记录
	@RequestMapping(value = "/createchargerecord", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo createChargeRecord(GeneralCondition gcon,HttpServletRequest request, ChargeRecord chargeRecord) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证

			try {
				
				String username = authenticationService.getUsername(gcon);//创建人
				chargeRecord.setCreater(username);
				Token token = (Token)rtnInfo.getData();
				this.addURL(request.getRequestURI());
				this.addToken(token);
				rtnInfo = chargeRecordService.createChargeRecord(chargeRecord,token);
			}
			catch (Exception e) {
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	// 删除账单记录
	@RequestMapping(value = "/deletechargerecord", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo deleteChargeRecord(GeneralCondition gcon,HttpServletRequest request, ChargeRecord chargeRecord) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				Token token = (Token)rtnInfo.getData();
				this.addToken(token);
				this.addURL(request.getRequestURI());
				rtnInfo = chargeRecordService.deleteChargeRecord(chargeRecord);
			}
			catch (Exception e) {
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	// 修改账单记录
	@RequestMapping(value = "/modifychargerecord", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo modifyChargeRecord(GeneralCondition gcon,HttpServletRequest request, ChargeRecord chargeRecord) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				Token token = (Token)rtnInfo.getData();
				this.addToken(token);
				this.addURL(request.getRequestURI());
				rtnInfo = chargeRecordService.modifyChargeRecord(chargeRecord);
			}
			catch (Exception e) {
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	// 查询账单记录列表
	@RequestMapping(value = "/querychargerecordlist", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryChargeRecordList(GeneralCondition gcon,HttpServletRequest request, ChargeRecord chargeRecord) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		makeOffsetAndRows(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				Token token = (Token)rtnInfo.getData();
				rtnInfo = chargeRecordService.queryChargeRecordList(gcon, chargeRecord,token);
			}
			catch (Exception e) {
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	// 查询账单详细信息
	@RequestMapping(value = "/querychargerecorddetail", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryChargeRecordDetail(GeneralCondition gcon,HttpServletRequest request, ChargeRecord chargeRecord) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				rtnInfo = chargeRecordService.queryChargeRecordDetail(chargeRecord);
			}
			catch (Exception e) {
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	// 根据多个caseId查询未核销账单记录。
	@RequestMapping(value = "/querychargerecordbycases", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryChargeRecordByCases(GeneralCondition gcon,HttpServletRequest request, String caseIds) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				rtnInfo = chargeRecordService.queryChargeRecordByCases(caseIds);
			}
			catch (Exception e) {
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	// 案件生成账单,暂时未用
	@RequestMapping(value = "/casegeneratebill", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo caseGenerateBill(GeneralCondition gcon,HttpServletRequest request, String caseIds) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				Token token = (Token)rtnInfo.getData();
				this.addToken(token);
				this.addURL(request.getRequestURI());
				rtnInfo = billService.caseGenerateBill(caseIds);
			}
			catch (Exception e) {
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	// 案件生成账单，暂时未用
	@RequestMapping(value = "/recordgeneratebill", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo recordGenerateBill(GeneralCondition gcon,HttpServletRequest request, String chargeRecordIds) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				Token token = (Token)rtnInfo.getData();
				this.addToken(token);
				this.addURL(request.getRequestURI());
				rtnInfo = billService.recordGenerateBill(chargeRecordIds);
			}
			catch (Exception e) {
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}

	// 客户账单统计
	@RequestMapping(value = "/statsbycustomer", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statsByCustomer(GeneralCondition gcon,HttpServletRequest request, Integer custId,String interfacetype) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
//				rtnInfo = billService.statsByCustomer(custId,gcon,interfacetype);
				rtnInfo.setData(null);
			}
			catch (Exception e) {
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	// 客户账单统计
	@RequestMapping(value = "/statsbyagencyuser", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statsByAgencyUser(GeneralCondition gcon,HttpServletRequest request, Integer userId,Integer agencyId,String interfacetype) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
//				rtnInfo = billService.statsByAgencyUser(userId,agencyId,gcon,interfacetype);
				rtnInfo.setData(null);
			}
			catch (Exception e) {
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	// 客户账单统计
	@RequestMapping(value = "/statsbyagency", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statsByAgency(GeneralCondition gcon,HttpServletRequest request, Integer agencyId,String interfacetype) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
//				rtnInfo = billService.statsByAgency(agencyId,gcon,interfacetype);
				rtnInfo.setData(null);
			}
			catch (Exception e) {
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	@RequestMapping(value = "/statsbilltop10", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statsBillTop10(GeneralCondition gcon,HttpServletRequest request, Integer agencyId,String interfacetype) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
//				rtnInfo = billService.statsBillTop10(gcon);
				rtnInfo.setData(null);
			}
			catch (Exception e) {
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	@RequestMapping(value = "/generatebillno", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo generateBillNo(GeneralCondition gcon,Integer billId) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				Token token = (Token)rtnInfo.getData();
				this.addToken(token);
				rtnInfo = billService.generateBillNo(billId);
			}
			catch (Exception e) {
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	
	@RequestMapping(value = "/statsBillProInfo", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statsBillProInfo(GeneralCondition gcon,Integer custId,Integer agencyId,Integer departId) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
		
			
			rtnInfo = billService.statsBillProInfo(custId, agencyId, departId);

		}
		return rtnInfo;
	}	
	
	@RequestMapping(value = "/statsBillagency", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statsBillagency(GeneralCondition gcon,Integer custId,String startyear,String endyear) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
			
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());

			if(startyear == null) {
				startyear = String.valueOf(c.get(Calendar.YEAR) - 4);
			} 
			if(endyear == null) {
				endyear = String.valueOf(c.get(Calendar.YEAR));
			}  
					
			
			rtnInfo = billService.statsBillagency(custId, startyear, endyear)   ;

		}
		return rtnInfo;
	}

	@RequestMapping(value = "/statsBillbillType", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statsBillbillType(GeneralCondition gcon,Integer custId,Integer agencyId,Integer departId,String startyear,String endyear,
			Integer timetype) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证

			Calendar c = Calendar.getInstance();
			if(timetype.intValue() == 1) {
				if(startyear == null) {
					c.setTime(new Date());
					startyear = quarter(c,4);
				} 
				if(endyear == null) {
					c.setTime(new Date());
					endyear = quarter(c,0);
				} 
			}else if(timetype.intValue() == 2) {
				if(startyear == null) {
					c.setTime(new Date());
					startyear = mounth(c,6);
				} 
				if(endyear == null) {
					c.setTime(new Date());
					endyear = mounth(c,0);
				} 
			}else {
				if(startyear == null) {
					startyear = String.valueOf(c.get(Calendar.YEAR) - 4);
				} 
				if(endyear == null) {
					endyear = String.valueOf(c.get(Calendar.YEAR));
				} 
					
			}	
					
			if(departId==null)departId=0;
					
			
			rtnInfo = billService.statsBillbillType(custId, agencyId,departId, startyear, endyear, timetype);

		}
		return rtnInfo;
	}
	
	@RequestMapping(value = "/statsBillcreaterName", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statsBillcreaterName(GeneralCondition gcon,Integer custId,Integer agencyId,Integer departId,String startyear,String endyear,
			Integer timetype) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证

			Calendar c = Calendar.getInstance();
			if(timetype.intValue() == 1) {
				if(startyear == null) {
					c.setTime(new Date());
					startyear = quarter(c,4);
				} 
				if(endyear == null) {
					c.setTime(new Date());
					endyear = quarter(c,0);
				} 
			}else if(timetype.intValue() == 2) {
				if(startyear == null) {
					c.setTime(new Date());
					startyear = mounth(c,6);
				} 
				if(endyear == null) {
					c.setTime(new Date());
					endyear = mounth(c,0);
				} 
			}else {
				if(startyear == null) {
					startyear = String.valueOf(c.get(Calendar.YEAR) - 4);
				} 
				if(endyear == null) {
					endyear = String.valueOf(c.get(Calendar.YEAR));
				} 
					
			}
			
			if(departId==null)departId=0;
					
			
			rtnInfo = billService.statsBillcreaterName(custId,agencyId,departId, startyear, endyear, timetype);

		}
		return rtnInfo;
	}
	
	@RequestMapping(value = "/queryBillBySolr", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryBillBySolr(GeneralCondition gcon,Integer agencyId,Integer custId,Integer departId,BillSolr bill,
			String startyear,String endyear,Integer timetype,Integer bodytype) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证

			makeOffsetAndRows(gcon);
			Calendar c = Calendar.getInstance();
			if(timetype.intValue() == 1) {
				if(startyear == null) {
					c.setTime(new Date());
					startyear = quarter(c,4);
				} 
				if(endyear == null) {
					c.setTime(new Date());
					endyear = quarter(c,0);
				} 
			}else if(timetype.intValue() == 2) {
				if(startyear == null) {
					c.setTime(new Date());
					startyear = mounth(c,6);
				} 
				if(endyear == null) {
					c.setTime(new Date());
					endyear = mounth(c,0);
				} 
			}else {
				if(startyear == null) {
					startyear = String.valueOf(c.get(Calendar.YEAR) - 4);
				} 
				if(endyear == null) {
					endyear = String.valueOf(c.get(Calendar.YEAR));
				} 
					
			}
			
			if(departId==null)departId=0;
					
			
			rtnInfo = billService.queryBillBySolr(departId, custId, agencyId, bodytype, bill, timetype, startyear, endyear, gcon);

		}
		return rtnInfo;
	}
	
	@RequestMapping(value = "/statsBillcustomer", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statsBillcustomer(GeneralCondition gcon,Integer agencyId,Integer departId,String startyear,String endyear,
			Integer timetype) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证

			Calendar c = Calendar.getInstance();
			if(timetype.intValue() == 1) {
				if(startyear == null) {
					c.setTime(new Date());
					startyear = quarter(c,4);
				} 
				if(endyear == null) {
					c.setTime(new Date());
					endyear = quarter(c,0);
				} 
			}else if(timetype.intValue() == 2) {
				if(startyear == null) {
					c.setTime(new Date());
					startyear = mounth(c,6);
				} 
				if(endyear == null) {
					c.setTime(new Date());
					endyear = mounth(c,0);
				} 
			}else {
				if(startyear == null) {
					startyear = String.valueOf(c.get(Calendar.YEAR) - 4);
				} 
				if(endyear == null) {
					endyear = String.valueOf(c.get(Calendar.YEAR));
				} 
					
			}
			
			if(departId==null)departId=0;	
					
			
			rtnInfo = billService.statsBillcustomer(agencyId,departId, startyear, endyear, timetype);

		}
		return rtnInfo;
	}
	
	@RequestMapping(value = "/statsBillmonth", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statsBillmonth(GeneralCondition gcon,Integer agencyId,Integer departId,String startyear,String endyear,Integer timetype) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证

			Calendar c = Calendar.getInstance();
			if(timetype.intValue() == 1) {
				if(startyear == null) {
					c.setTime(new Date());
					startyear = quarter(c,4);
				} 
				if(endyear == null) {
					c.setTime(new Date());
					endyear = quarter(c,0);
				} 
			}else if(timetype.intValue() == 2) {
				if(startyear == null) {
					c.setTime(new Date());
					startyear = mounth(c,6);
				} 
				if(endyear == null) {
					c.setTime(new Date());
					endyear = mounth(c,0);
				} 
			}else {
				if(startyear == null) {
					startyear = String.valueOf(c.get(Calendar.YEAR) - 4);
				} 
				if(endyear == null) {
					endyear = String.valueOf(c.get(Calendar.YEAR));
				} 
					
			}
			
			if(departId==null)departId=0;	
			
			rtnInfo = billService.statsBillmonth(agencyId,departId, timetype, startyear, endyear);

		}
		return rtnInfo;
	}
	@RequestMapping(value = "/statsBillmonthcust", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statsBillmonthcust(GeneralCondition gcon,Integer agencyId,Integer departId,Integer custId,String startyear,String endyear,Integer timetype) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证

			Calendar c = Calendar.getInstance();
			if(timetype.intValue() == 1) {
				if(startyear == null) {
					c.setTime(new Date());
					startyear = quarter(c,4);
				} 
				if(endyear == null) {
					c.setTime(new Date());
					endyear = quarter(c,0);
				} 
			}else if(timetype.intValue() == 2) {
				if(startyear == null) {
					c.setTime(new Date());
					startyear = mounth(c,6);
				} 
				if(endyear == null) {
					c.setTime(new Date());
					endyear = mounth(c,0);
				} 
			}else {
				if(startyear == null) {
					startyear = String.valueOf(c.get(Calendar.YEAR) - 4);
				} 
				if(endyear == null) {
					endyear = String.valueOf(c.get(Calendar.YEAR));
				} 
					
			}
			
			if(departId==null)departId=0;	
			
			rtnInfo = billService.statsBillmonthcust(agencyId,departId,custId, timetype, startyear, endyear);

		}
		return rtnInfo;
	}
	@RequestMapping(value = "/statsBillmonthcoagency", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statsBillmonthcoagency(GeneralCondition gcon,Integer agencyId,Integer departId,String startyear,String endyear,Integer timetype) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证

			Calendar c = Calendar.getInstance();
			if(timetype.intValue() == 1) {
				if(startyear == null) {
					c.setTime(new Date());
					startyear = quarter(c,4);
				} 
				if(endyear == null) {
					c.setTime(new Date());
					endyear = quarter(c,0);
				} 
			}else if(timetype.intValue() == 2) {
				if(startyear == null) {
					c.setTime(new Date());
					startyear = mounth(c,6);
				} 
				if(endyear == null) {
					c.setTime(new Date());
					endyear = mounth(c,0);
				} 
			}else {
				if(startyear == null) {
					startyear = String.valueOf(c.get(Calendar.YEAR) - 4);
				} 
				if(endyear == null) {
					endyear = String.valueOf(c.get(Calendar.YEAR));
				} 
					
			}
			
			if(departId==null)departId=0;
			
			rtnInfo = billService.statsBillmonthcoagency(agencyId,departId, timetype, startyear, endyear);

		}
		return rtnInfo;
	}
	@RequestMapping(value = "/statsBillmonthagency", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statsBillmonthagency(GeneralCondition gcon,Integer agencyId,Integer departId,String startyear,String endyear,Integer timetype) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证

			Calendar c = Calendar.getInstance();
			if(timetype.intValue() == 1) {
				if(startyear == null) {
					c.setTime(new Date());
					startyear = quarter(c,4);
				} 
				if(endyear == null) {
					c.setTime(new Date());
					endyear = quarter(c,0);
				} 
			}else if(timetype.intValue() == 2) {
				if(startyear == null) {
					c.setTime(new Date());
					startyear = mounth(c,6);
				} 
				if(endyear == null) {
					c.setTime(new Date());
					endyear = mounth(c,0);
				} 
			}else {
				if(startyear == null) {
					startyear = String.valueOf(c.get(Calendar.YEAR) - 4);
				} 
				if(endyear == null) {
					endyear = String.valueOf(c.get(Calendar.YEAR));
				} 
					
			}
			if(departId==null)departId=0;	
			
			rtnInfo = billService.statsBillmonthagency(agencyId,departId, timetype, startyear, endyear);

		}
		return rtnInfo;
	}
	private String quarter(Calendar c, int i) {
		c.add(Calendar.MONTH,-(i*3));
		
		return String.valueOf(c.get(Calendar.YEAR))+"-"+getSeason(c);
	}
	private String mounth(Calendar c, int i) {
		c.add(Calendar.MONTH,-i);
		
		return String.valueOf(c.get(Calendar.YEAR))+"-"+String.valueOf(c.get(Calendar.MONTH));
	}
	 public static String getSeason( Calendar c) {  
		  
	        Integer season = 0;  
	  
	        int month = c.get(Calendar.MONTH);  
	        switch (month) {  
	        case Calendar.JANUARY:  
	        case Calendar.FEBRUARY:  
	        case Calendar.MARCH:  
	            season = 1;  
	            break;  
	        case Calendar.APRIL:  
	        case Calendar.MAY:  
	        case Calendar.JUNE:  
	            season = 2;  
	            break;  
	        case Calendar.JULY:  
	        case Calendar.AUGUST:  
	        case Calendar.SEPTEMBER:  
	            season = 3;  
	            break;  
	        case Calendar.OCTOBER:  
	        case Calendar.NOVEMBER:  
	        case Calendar.DECEMBER:  
	            season = 4;  
	            break;  
	        default:  
	            break;  
	        }  
	        return season.toString();  
	    }  
	private static void formatQueryYear(Integer startYear, Integer endYear, Boolean ismonth) {
		
		
	}
	
	
	
	
	
	
}
