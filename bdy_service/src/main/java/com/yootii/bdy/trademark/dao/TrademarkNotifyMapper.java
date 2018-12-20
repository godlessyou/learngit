package com.yootii.bdy.trademark.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.task.model.Notification;

public interface TrademarkNotifyMapper {

	
	//根据条件查询商标的通知
	List<Notification> queryTmNotify(@Param("custId")Integer custId,@Param("batchNo")String batchNo,@Param("gcon")GeneralCondition gcon);

	
	// 可续展的 按季度筛选
	List<Map<String, Object>> statisticsTmByQuarter(@Param("beginQuarter")String beginQuarter,@Param("endQuarter")String endQuarter,@Param("custId")Integer custId,@Param("type")int type,@Param("gcon")GeneralCondition gcon);
	Long statisticsTmByQuarterCount(@Param("beginQuarter")String beginQuarter,@Param("endQuarter")String endQuarter,@Param("custId")Integer custId);
	// 已提醒  按季度筛选
	List<Map<String, Object>> statisticsTmAlreadRemindByQuarter(@Param("beginQuarter")String beginQuarter,@Param("endQuarter")String endQuarter,@Param("custId")Integer custId,@Param("type")int type,@Param("gcon")GeneralCondition gcon);	
	Long statisticsTmAlreadRemindByQuarterCount(@Param("beginQuarter")String beginQuarter,@Param("endQuarter")String endQuarter,@Param("custId")Integer custId);
	List<String> queryAllTimes(@Param("custId")Integer custId);
	List<String> queryRenewalRemindTimes(@Param("gcon")GeneralCondition gcon,@Param("custId")Integer custId);
	List<Map<String, Object>> queryNotification(@Param("custId")Integer custId,@Param("createTime")String date,@Param("deadTime") String deadTime,@Param("gcon")GeneralCondition gcon);

	int queryNotificationCount(@Param("custId")Integer custId,@Param("createTime")String date,@Param("deadTime") String deadTime,@Param("gcon")GeneralCondition gcon);
	List<Map<String, Object>> queryAbnormalList(@Param("custId")Integer custId,@Param("gcon")GeneralCondition gcon,@Param("userId")Integer userId);	
	long queryAbnormalCount(@Param("custId")Integer custId,@Param("gcon")GeneralCondition gcon,@Param("userId") Integer userId);
	void modifyTmAbnormal(@Param("id") String id);
	void modifyNotification(@Param("id") String id);
	List<Map<String, Object>> queryGongGao(Map<String, Object> map);
	
	List<Map<String, Object>> queryAnnouncementRemind(@Param("custId")Integer custId,@Param("gcon")GeneralCondition gcon,@Param("userId")Integer userId,@Param("type")Integer type);

	Long queryAnnouncementRemindCount(@Param("gcon")GeneralCondition gcon,@Param("custId")Integer custId,@Param("userId")Integer userId,@Param("type")Integer type);
	
	void updateAnnouncementRemind(@Param("id")String id);
	
	List<Map<String, Object>> queryAnnoucementForMail(@Param("custId")Integer custId,@Param("type")Integer type);	
	
	List<String> queryAllGGQiHao(@Param("custId")Integer custId);
	List<String> selectAbnormalTimes(@Param("custId")Integer custId);
}
