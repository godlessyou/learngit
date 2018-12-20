package com.yootii.bdy.task.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.task.model.MailDTO;
import com.yootii.bdy.task.model.MailInfor;
import com.yootii.bdy.task.model.Notification;
import com.yootii.bdy.trademark.model.Trademark;

import java.util.List;
import java.util.Map;



public interface TracdemarkTaskMapper {
	
	public List<Trademark> selectTMByDate(@Param("beforeDate")Date beforeDate,@Param("afterDate")Date afterDate);

	
	void insert(ArrayList<Notification> notifications);
	
	public List<MailInfor> selectNotificationList(@Param("time")int time);
	
	public List<MailDTO> selectTmForMails();
	
	public Integer queryUserByCustId(@Param("custId")int custId);
	
	public List<Map<String, Object>> queryNotification(@Param("custId")Integer custId);
	
	public List<Map<String,Object>> queryRenewalRemindList(@Param("gcon")GeneralCondition gcon,@Param("custId")Integer custId);	
	
	public Long queryRenewalRemindListCount(@Param("gcon")GeneralCondition gcon,@Param("custId")Integer custId);
	
	public List<Map<String, Object>> queryTmProcess(@Param("beganDate")String beganDate,@Param("endDate")String endDate,@Param("status")String status);	

	public List<Map<String, Object>> queryTmAbnormal(@Param("beganDate")String beganDate,@Param("endDate")String endDate,@Param("abnormalType")int abnormalType);	
	public int isRepeat(HashMap<String, Object> map);
	public List<Map<String, Object>> queryRejectTmProcess(Map<String, Object> map);
	public void insertTmAbnormal(Map<String, Object> map);
	
	public int isExist(HashMap<String,Object> map);
	
	
	public List<Map<String, Object>> queryGongGao();	
	public List<Map<String, Object>> queryAnnouncementRemind();
	void insertAnnouncementRemind(List<Map<String, Object>> list);
	int isExists(@Param("ggId")Integer ggId);
	
}
