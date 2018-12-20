package com.yootii.bdy.bill.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yootii.bdy.bill.dao.ChargeRecordMapper;
import com.yootii.bdy.bill.model.ChargeRecord;
import com.yootii.bdy.bill.service.ChargeRecordService;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
@Service
public class ChargeRecordServiceImpl implements ChargeRecordService{
	
	@Resource
	private ChargeRecordMapper chargeRecordMapper;
	
	@Override
	public ReturnInfo createChargeRecord(ChargeRecord chargeRecord,Token token) {
		ReturnInfo info = new ReturnInfo();
		if(!token.isUser()){//不是代理机构用户
			info.setSuccess(false);
			info.setMessage("没有权限");
			return info;
		}
		if(chargeRecord.getAgencyId()==null){
			info.setSuccess(false);
			info.setMessage("代理机构ID不能为空");
			return info;	
		}
		if(chargeRecord.getCaseId()==null&&chargeRecord.getChargeItemId()==null){
			info.setSuccess(false);
			info.setMessage("案件ID和价目ID不能为空");
			return info;	
		}
		try{
			chargeRecord.setStatus(0);//0代表未核销
			chargeRecordMapper.insertSelective(chargeRecord);
//			chargeRecordMapper.insertSelectiveWithUserId(chargeRecord,Integer userId);
		}catch(Exception e){
			info.setSuccess(false);
			info.setMessage("创建失败");
			return info;
		}
		info.setSuccess(true);
		info.setMessage("创建成功");
		return info;
	}

	@Override
	public ReturnInfo deleteChargeRecord(ChargeRecord chargeRecord) {
		ReturnInfo info = new ReturnInfo();
		Integer chargeRecordId = chargeRecord.getChargeRecordId();
		if(chargeRecordId==null){
			info.setSuccess(false);
			info.setMessage("账单记录ID不能为空");
			return info;
		}
		chargeRecordMapper.deleteByPrimaryKey(chargeRecordId);
		info.setSuccess(true);
		info.setMessage("删除成功");
		return info;
	}

	@Override
	public ReturnInfo modifyChargeRecord(ChargeRecord chargeRecord) {
		ReturnInfo info = new ReturnInfo();
		Integer chargeRecordId = chargeRecord.getChargeRecordId();
		if(chargeRecordId==null){
			info.setSuccess(false);
			info.setMessage("账单记录ID不能为空");
			return info;
		}
		chargeRecordMapper.updateByPrimaryKeySelective(chargeRecord);
		info.setSuccess(true);
		info.setMessage("修改成功");
		return info;
	}

	@Override
	public ReturnInfo queryChargeRecordList(GeneralCondition gcon,
			ChargeRecord chargeRecord,Token token) {
		ReturnInfo info = new ReturnInfo();
		if(token==null){
			info.setSuccess(false);
			info.setMessage("查询失败");
			return info;
		}
		Integer userId = null;
		Integer custId = null;
		if(token.isUser()){
			userId = token.getUserID();
		}else{
			custId = token.getCustomerID();
		}
		List<ChargeRecord> chargeRecords = null;
		chargeRecords = chargeRecordMapper.selectByChargeRecord(gcon, chargeRecord,userId,custId);
		List<Map<String,Long>> counts = chargeRecordMapper.selectChargeRecordCount(gcon, chargeRecord,userId,custId);
		Long total = 0L;
		for (Map<String,Long> onecount:counts) {
			total+= onecount.get("count");
		} 
		info.setData(chargeRecords);
		info.setTotal(total);
		info.setCurrPage(gcon.getPageNo());
		info.setSuccess(true);
		info.setMessage("查询成功");
		return info;
	}

	@Override
	public ReturnInfo queryChargeRecordDetail(ChargeRecord chargeRecord) {
		ReturnInfo info = new ReturnInfo();
		Integer chargeRecordId = chargeRecord.getChargeRecordId();
		if(chargeRecordId==null){
			info.setSuccess(false);
			info.setMessage("账单记录ID不能为空");
			return info;
		}
		ChargeRecord data = chargeRecordMapper.selectByPrimaryKey(chargeRecordId);
		info.setSuccess(true);
		info.setData(data);
		info.setMessage("查询成功");
		return info;
	}

	@Override
	public ReturnInfo queryChargeRecordByCases(String caseIds) {
		ReturnInfo info = new ReturnInfo();
		List<ChargeRecord> chargeRecords = new ArrayList<ChargeRecord>();
		try{
			String[] idArr = null;
			if(caseIds!=null){
				idArr = caseIds.split(",");
			}else {
				info.setSuccess(false);
				info.setMessage("请传入案件ID");
				return info;
			}
			chargeRecords = chargeRecordMapper.selectByCaseIds(idArr);
		}catch(Exception e){
			e.printStackTrace();
			info.setSuccess(false);
			info.setMessage("请传入正确的案件ID");
			return info;
		}
		info.setSuccess(true);
		info.setData(chargeRecords);
		info.setMessage("查询成功");
		return info;
	}

}
