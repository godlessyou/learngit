package com.yootii.bdy.task.service.Impl;



import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yootii.bdy.bill.model.Bill;
import com.yootii.bdy.bill.service.BillService;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.Globals;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.task.dao.TaskRecordMapper;
import com.yootii.bdy.task.model.BillToDoList;
import com.yootii.bdy.task.model.TaskRecord;
import com.yootii.bdy.task.model.TmCaseTaskToDoList;
import com.yootii.bdy.task.service.BillTaskService;
import com.yootii.bdy.task.service.TradeMarkCaseTaskService;
import com.yootii.bdy.tmcase.model.TradeMarkCase;


@Service
public class BillTaskServiceImpl implements BillTaskService {

	private final Logger logger = Logger.getLogger(this.getClass());


	@Resource
	private TaskRecordMapper taskRecordMapper;

	@Resource
	private TaskQueryServiceImpl taskQeuryServiceImpl;
	
	@Resource
	private TaskBasicServiceImpl taskBasicServiceImpl;
	
	@Resource
	private BillService billService;
	

	@Override
	public ReturnInfo startBillReviewProcess(GeneralCondition gcon, String userId, String customerId,  Bill bill,String chargeRecordIds)  {
		//创建账单
		
		ReturnInfo ret1 = billService.createBill(gcon, bill,chargeRecordIds);
		Map<String, Object> data1 = (Map<String, Object>) ret1.getData();
		if(!ret1.getSuccess()) return ret1;
		
		Integer billId = (Integer) data1.get("billId");
		bill.setBillId(billId);
		//开始任务
		ReturnInfo rtnInfo = taskBasicServiceImpl
				.startBillReviewProcess(gcon, userId, customerId, bill);
		
		//更新solr中的账单数据
//		billService.updateSolrBill();
		
		return rtnInfo;
	}

	@Override
	public ReturnInfo queryToDoList(GeneralCondition gcon, String userId, String customerId)  {
		//创建账单
		

		ReturnInfo rtnInfo = taskQeuryServiceImpl.queryBillToDoList(gcon, userId, customerId);
		return rtnInfo;
	}

	@Override
	public ReturnInfo audited(GeneralCondition gcon, String userId, BillToDoList toDoList, boolean audited) {
		String key="audited";
		Map<String, Object> runMap = new HashMap<String, Object>();		
		runMap.put(key, audited);
		String permission="";
		String taskResult="";
		
		ReturnInfo rtnInfo = new ReturnInfo();
		
		Integer billId=toDoList.getBillId();
		
		
		if (billId==null){
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("缺少billId参数");
			rtnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
			return rtnInfo;
		}
		
		try{
		
	//		Bill bill=new Bill();
	//		
	//		bill.setBillId(billId);		
	//				
	//		bill.setStatus("已审核");
//			if (rtnInfo!=null && rtnInfo.getSuccess()!=true){
//				return rtnInfo;
//			}
			
					
			
			if(audited) {
//				String status="已审核";
				String status="未付款";
				
				
				boolean result = billService.modifyBillStatus(billId, status);
				
				if (!result){
					rtnInfo.setSuccess(false);
					rtnInfo.setMessage("修改账单状态出错");
					return rtnInfo;
				}					
				
				permission="";
				taskResult="审核通过";			
			} else {
				permission="账单审核:重新修改";
				taskResult="审核不通过";			
			}
			
			rtnInfo = taskBasicServiceImpl.userDoBillTask( gcon,userId,toDoList,permission,taskResult,runMap);	
		
		}catch(Exception e){
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("账单审核出错"+e.getMessage());
		}
		
		return rtnInfo;
	}

	@Override
	public ReturnInfo modifyBill(GeneralCondition gcon, String userId, BillToDoList toDoList,  Bill bill) {
		//修改账单		
		ReturnInfo rtnInfo = billService.modifyBill(gcon, bill);

		//无需执行流程
//		String permission="账单审核:组长审核";
//		Map<String, Object> runMap = new HashMap<String, Object>();		
//		String taskResult="完成";
//		ReturnInfo rtnInfo = taskBasicServiceImpl.userDoBillTask( gcon,userId,toDoList,permission,taskResult,runMap);	
		return rtnInfo;
	}
	
	
	

	@Override
	public ReturnInfo modifyBillStatus(GeneralCondition gcon, String userId, Bill bill) {
		ReturnInfo rtnInfo =new ReturnInfo();
		try{
			Integer billId=bill.getBillId();			
			if(billId==null){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("缺少必要的参数billId");
				return rtnInfo;
			}
			
			String status="已付款";
			
			//修改账单		
			boolean success = billService.modifyBillStatus(billId, status);
			rtnInfo.setSuccess(success);
			if (!success){
				rtnInfo.setMessage("账单付费状态修改成功");
				return rtnInfo;
			}
		
		}catch(Exception e){
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("账单付费状态修改出错"+e.getMessage());
		}

 	    return rtnInfo;
	}
	
	
	


}
