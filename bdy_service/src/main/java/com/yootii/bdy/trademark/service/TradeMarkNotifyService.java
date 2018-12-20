package com.yootii.bdy.trademark.service;

import java.util.List;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;

public interface TradeMarkNotifyService {

	public ReturnInfo queryTmNotifyList(Integer custId,String batchNo,GeneralCondition gcon,int no);
	
	
	public ReturnInfo statisticTmNotifyStatus(GeneralCondition gcon,String year,Integer custId);

	public ReturnInfo statisticTmNotifyData(GeneralCondition gcon,String year,Integer custId,int quarter,int type);
	
	public ReturnInfo queryTmNotifyByCustId(Integer custId,String createTime,GeneralCondition gCondition);

	public ReturnInfo queryAllTimes(Integer custId);
	
	public ReturnInfo queryTmAbnormalList(Integer custId,GeneralCondition gcon,Integer userId);
	public void modifyTmAbnormal(List<String> ids);
	public void modifyNotification(List<String> idStrings);
	public ReturnInfo queryRenewalRemindTimes(GeneralCondition gcon,Integer custId);
	
	
	public ReturnInfo queryAnnouncementRemind(GeneralCondition gcon,Integer custId,Integer appId,Integer userId,Integer type);
	
	public void updateAnnouncementRemind(List<String> ids);
	
	public ReturnInfo queryAnnoucementForMail(Integer custId,Integer type);
	public ReturnInfo queryAllGGQiao(Integer custId);
	
	public ReturnInfo queryAbnormalTimes(Integer custId);
	
}
