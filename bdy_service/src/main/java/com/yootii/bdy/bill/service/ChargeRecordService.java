package com.yootii.bdy.bill.service;

import com.yootii.bdy.bill.model.ChargeRecord;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.security.model.Token;

public interface ChargeRecordService {
	public ReturnInfo createChargeRecord(ChargeRecord chargeRecord,Token token);
	public ReturnInfo deleteChargeRecord(ChargeRecord chargeRecord);
	public ReturnInfo modifyChargeRecord(ChargeRecord chargeRecord);
	public ReturnInfo queryChargeRecordList(GeneralCondition gcon,ChargeRecord chargeRecord,Token token);
	public ReturnInfo queryChargeRecordDetail(ChargeRecord chargeRecord);
	/**
	 * 根据多个caseId查询 未核销 账单记录。
	 * @param caseIds
	 * @return
	 */
	public ReturnInfo queryChargeRecordByCases(String caseIds);
	
}
