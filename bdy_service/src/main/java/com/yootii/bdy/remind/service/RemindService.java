package com.yootii.bdy.remind.service;

import java.util.Date;
import java.util.List;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.remind.model.Remind;

public interface RemindService {
	
	public ReturnInfo deleteRemind(Integer remindId);
	
	public ReturnInfo insertRemindByType(Integer type,Date date,Integer agencyId,Integer custId,Integer caseId,GeneralCondition gcon);
	
	public ReturnInfo selectRemindList(Remind remind, GeneralCondition gcon);

	
}