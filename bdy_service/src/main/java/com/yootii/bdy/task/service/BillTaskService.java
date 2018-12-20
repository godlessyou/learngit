package com.yootii.bdy.task.service;

import com.yootii.bdy.bill.model.Bill;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.task.model.BillToDoList;


public interface BillTaskService {
	
	
	//启动账单审核流程
	public ReturnInfo startBillReviewProcess(GeneralCondition gcon, String userId, String customerId,  Bill bill,String chargeRecordIds);
	
	
	//审核账单
	public ReturnInfo audited(GeneralCondition gcon, String userId, BillToDoList toDoList,boolean audited);
	
	//修改账单
	public ReturnInfo modifyBill(GeneralCondition gcon, String userId, BillToDoList toDoList,  Bill bill);


	public ReturnInfo queryToDoList(GeneralCondition gcon, String userId, String customerId);
	
	//修改账单状态
	public ReturnInfo modifyBillStatus(GeneralCondition gcon, String userId,  Bill bill);

	
	
}
