package com.yootii.bdy.datasyn.service;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;




public interface DataSynService {	
	

	//同步案件数据
	public ReturnInfo caseDataSyn(GeneralCondition gcon, String caseId, Integer type);
	
	//同步账单数据
	public ReturnInfo billDataSyn(GeneralCondition gcon, String caseIds, Integer billId);	
	

	
}
