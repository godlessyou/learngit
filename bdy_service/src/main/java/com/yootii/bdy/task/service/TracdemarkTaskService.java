package com.yootii.bdy.task.service;

import java.util.List;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.task.model.Notification;
import com.yootii.bdy.trademark.model.Trademark;

public interface TracdemarkTaskService {

	
	public void scheduling(); 
	
	public ReturnInfo list(int time);
	
	public ReturnInfo findTmList(Integer custId);
	
	public ReturnInfo queryRenewalRemindList(GeneralCondition gcon,Integer custId);
	
	public void autoTimingCreateTmAbnormal();
	
	public void autoTimingCreateAnnouncementRemind();
	
}
