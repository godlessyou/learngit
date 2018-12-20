package com.yootii.bdy.trademark.service.impl;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.common.util.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.javafx.geom.AreaOp.IntOp;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.task.dao.TracdemarkTaskMapper;
import com.yootii.bdy.task.model.Notification;
import com.yootii.bdy.trademark.dao.TrademarkNotifyMapper;
import com.yootii.bdy.trademark.service.TradeMarkNotifyService;
import com.yootii.bdy.util.DateTool;

@Service("tradeMarkNotifyService")
public class TradeMarkNotifyServiceImpl implements TradeMarkNotifyService {

	@Autowired
	private TrademarkNotifyMapper trademarkNotifyMapper;
	@Autowired
	private TracdemarkTaskMapper tracdemarkTaskMapper;
	
	
	
	/**
	 * 查询发送邮件的详情   ，也即 商标提醒信息，根据发送邮件的规则，根据发送的序号，查找对应的数据
	 */
	@Override
	public ReturnInfo queryTmNotifyList(Integer custId,String batchNo,GeneralCondition gcon,int no) {
		
		ReturnInfo returnInfo = new  ReturnInfo();
			List<Notification> notifications = trademarkNotifyMapper.queryTmNotify(custId, batchNo, gcon);
			List<Notification> data = new ArrayList<>();
			//  每次发100封，总共能分几次发
			int count = notifications.size()/100;
			if(notifications.size()%100 == 0){
				if(count == 0){
					count+=1;
				}
			}else{
				count +=1;
			}
			//查询最后一页
			if(no==count){
				int begin = (no-1)*100;
				int end = notifications.size();
				for(int i=begin;i<end;i++){
					Notification notification = notifications.get(i);
					data.add(notification);
				}
			}else{
				int begin = (no-1)*100;
				int end = begin+99;
				for(int i=begin;i<end;i++){
					Notification notification= notifications.get(i);
					data.add(notification);
				}
			}
			
			returnInfo.setData(data);
			return returnInfo;
	}
	
	
	/**
	 * 、支持对客户续展的3个方面的数量的统计
	 * 	      客户的可续展的商标数量
		1）已经提醒续展的商标数量
		3）已经进行续展的商标数量
		2、支持按照年来统计，当选择了某个年之后，后端提供4个季度的统计值
		
		思路：可续展的数量即是 notification 表中的数据    ；已提醒的数量即是发送了邮件的；已进行续展的数量 是在商标表中的状态为
		
	 * @return
	 */
	@Override
	public ReturnInfo statisticTmNotifyStatus(GeneralCondition gcon,String year,Integer custId) {
		ReturnInfo returnInfo = new ReturnInfo();
		year =year+"-01-01";
		Date yDate = DateTool.StringToDate(year);
		// 某一年的四个季度
		Calendar calendar = Calendar.getInstance();
		//获取年
		calendar.setTime(yDate);
		int nowYear = calendar.get(Calendar.YEAR);
		//第一季度 始末
		String firstQuarterBegin = nowYear+"-01-01";
		String firstQuarterEnd = nowYear +"-03-31";
		//第二季度 始末
		String secondQuarterBegin = nowYear +"-04-01";
		String secondQuarterEnd = nowYear + "-06-30";
		//第三季度
		String thirdQuarterBegin = nowYear +"-07-01";
		String thirdQuarterEnd = nowYear +"-09-31";
		//第四季度
		String fourthQuarterBegin = nowYear +"-10-01";
		String fourthQuarterEnd = nowYear +"-12-31";
		//查询可续展的商标  第一 季度可续展商标
		List<Map<String, Object>> firstAbleExtension= trademarkNotifyMapper.statisticsTmByQuarter(firstQuarterBegin,firstQuarterEnd,custId,0,gcon);
		List<Map<String, Object>> secondAbleExtension = trademarkNotifyMapper.statisticsTmByQuarter(secondQuarterBegin, secondQuarterEnd,custId,0,gcon);
		List<Map<String, Object>> thirdAbleExtension = trademarkNotifyMapper.statisticsTmByQuarter(thirdQuarterBegin, thirdQuarterEnd,custId,0,gcon);
		List<Map<String, Object>> fourthAbleExtension = trademarkNotifyMapper.statisticsTmByQuarter(fourthQuarterBegin, fourthQuarterEnd,custId,0,gcon);
		Map<String, Object> firstAbleExtMap = new HashMap<>();
		Map<String, Object> secondAbleExtMap = new HashMap<>();
		Map<String, Object> thirdAbleExtMap = new HashMap<>();
		Map<String, Object> fourAbleExtMap = new HashMap<>();
		//
		firstAbleExtMap.put("count", firstAbleExtension.size());
		firstAbleExtMap.put("可续展第一季度", firstAbleExtension);
		//
		secondAbleExtMap.put("count",secondAbleExtension.size());
		secondAbleExtMap.put("可续展第二季度",secondAbleExtension);
		//
		thirdAbleExtMap.put("count",thirdAbleExtension.size());
		thirdAbleExtMap.put("可续展第三季度",thirdAbleExtension);
		
		//
		fourAbleExtMap.put("count", fourthAbleExtension.size());
		fourAbleExtMap.put("可续展第四季度", fourthAbleExtension);
		
		long  l1 = trademarkNotifyMapper.statisticsTmAlreadRemindByQuarterCount(firstQuarterBegin, firstQuarterEnd, custId);
		long l2 = trademarkNotifyMapper.statisticsTmAlreadRemindByQuarterCount(secondQuarterBegin, secondQuarterEnd, custId);
		long l3 = trademarkNotifyMapper.statisticsTmAlreadRemindByQuarterCount(thirdQuarterBegin, thirdQuarterEnd, custId);
		long l4 = trademarkNotifyMapper.statisticsTmAlreadRemindByQuarterCount(fourthQuarterBegin, fourthQuarterEnd, custId);
		
		//查询已进行续展de数量
		
		long firstCount = 0;
		Map<String, Object> firstProcessorMap = new HashMap<>();
		for(Map<String, Object> map : firstAbleExtension){
			Date tmDeadTime= (Date)map.get("tmDeadTime");
			Date  validEndDate= (Date)map.get("validEndDate");
			//如果当时记录存储的tmDeadTime 不等于 商标中的 validEndDate 时 ; 则这条数据即是已进行的
			if(!tmDeadTime.equals(validEndDate)){
			//	firstProcessor.add(map);
				firstCount++;
			}
		}
		
		//二季度
		long secondCount = 0;
		
		for(Map<String, Object> map:secondAbleExtension){
			Date tmDeadTime= (Date)map.get("tmDeadTime");
			Date  validEndDate= (Date)map.get("validEndDate");
			if(!tmDeadTime.equals(validEndDate)){
			//	secondProcessor.add(map);
				secondCount++;
			}
		}
		
		//三季度
		long thirdCount = 0;
		for(Map<String, Object> map:thirdAbleExtension){
			Date tmDeadTime= (Date)map.get("tmDeadTime");
			Date validEndDate= (Date)map.get("validEndDate");
			if(!tmDeadTime.equals(validEndDate)){
				//thirdProcessor.add(map);
				thirdCount++;
			}
		}
		
		//四季度
		long fourthCount = 0;
		
		for(Map<String, Object> map:fourthAbleExtension){
			Date tmDeadTime= (Date)map.get("tmDeadTime");
			Date validEndDate= (Date)map.get("validEndDate");
			if(!tmDeadTime.equals(validEndDate)){
			//	fourthProcessor.add(map);
				fourthCount++;
			}
		}
		
		
		List<Map<String, Object>> list1 = new ArrayList<>();
		int[]  aObject1  = {firstAbleExtension.size(),secondAbleExtension.size(),thirdAbleExtension.size(),fourthAbleExtension.size()};
		for(int i=0;i<=3;i++){
			Map<String, Object> map= new HashMap<>();
			map.put("Count",aObject1[i]);
			map.put("Quarter",i+1);
			list1.add(map);
		}
		List<Map<String, Object>> list2 = new ArrayList<>();
		long[] aObject2 = {l1,l2,l3,l4};
		for(int i=0;i<=3;i++){
			Map<String, Object> map  = new HashMap<>();
			map.put("Count",aObject2[i]);
			map.put("Quarter",i+1);
			list2.add(map);
		}
		
		List<Map<String, Object>> list3 = new ArrayList<>();
		long[] aObject3 ={firstCount,secondCount,thirdCount,fourthCount};
		for(int i=0;i<=3;i++){
			Map<String, Object> map  = new HashMap<>();
			map.put("Count",aObject3[i]);
			map.put("Quarter",i+1);
			list3.add(map);
		}
		List<Object> data = new ArrayList<>();
		data.add(list1);
		data.add(list2);
		data.add(list3);
		returnInfo.setData(data);
		return returnInfo;
	}

	
	
	
	
	@Override
	public ReturnInfo statisticTmNotifyData(GeneralCondition gcon, String year,
			Integer custId, int quarter,int type) {
			
		ReturnInfo returnInfo = new ReturnInfo();
		year =year+"-01-01";
		Date yDate = DateTool.StringToDate(year);
		// 某一年的四个季度
		Calendar calendar = Calendar.getInstance();
		//获取年
		calendar.setTime(yDate);
		int nowYear = calendar.get(Calendar.YEAR);
		//第一季度 始末
		String firstQuarterBegin = nowYear+"-01-01";
		String firstQuarterEnd = nowYear +"-03-31";
		//第二季度 始末
		String secondQuarterBegin = nowYear +"-04-01";
		String secondQuarterEnd = nowYear + "-06-30";
		//第三季度
		String thirdQuarterBegin = nowYear +"-07-01";
		String thirdQuarterEnd = nowYear +"-09-31";
		//第四季度
		String fourthQuarterBegin = nowYear +"-10-01";
		String fourthQuarterEnd = nowYear +"-12-31";
		
		String start = firstQuarterBegin;
		String end = firstQuarterEnd;
		if(quarter ==2){
			start = secondQuarterBegin;
			end = secondQuarterEnd;
		}else if(quarter ==3){
			start = thirdQuarterBegin;
			end = thirdQuarterEnd;
		}else if(quarter ==4){
			start = fourthQuarterBegin;
			end = fourthQuarterEnd;
		}
		//类型
		List<Map<String, Object>> data1 = trademarkNotifyMapper.statisticsTmByQuarter(start,end,custId,1,gcon);
		List<Map<String, Object>> data = trademarkNotifyMapper.statisticsTmByQuarter(start,end,custId,0,gcon);
		if(type == 0){
			returnInfo.setData(data1);
			returnInfo.setTotal(trademarkNotifyMapper.statisticsTmByQuarterCount(start, end, custId));
		}else if(type ==1){
			List<Map<String, Object>> data2 = trademarkNotifyMapper.statisticsTmAlreadRemindByQuarter(start, end, custId,1,gcon);
			returnInfo.setData(data2);
			returnInfo.setTotal(trademarkNotifyMapper.statisticsTmAlreadRemindByQuarterCount(start, end, custId));
		}else{
			List<Map<String, Object>> lists = new ArrayList<>();
			for(Map<String, Object> map:data){
				Date tmDeadTime= (Date)map.get("tmDeadTime");
				Date validEndDate= (Date)map.get("validEndDate");
				if(!tmDeadTime.equals(validEndDate)){
					lists.add(map);
				}
			}
			int total = lists.size();
			returnInfo.setData(lists);
			returnInfo.setTotal((long)total);
		}
		return returnInfo;
	}
	
	
	
	//调用转换日期格式
	private void convertDateFormat(List<Map<String,Object>>list){
		for(Map<String, Object> map :list){
			String createTime= DateTool.getDate((Date)map.get("createTime"));
			map.put("createTime", createTime);
		}
	}


	
	//根据custId 查询商标续展通知
	@Override
	public ReturnInfo queryTmNotifyByCustId(Integer custId,String createTime,GeneralCondition gcon) {
		ReturnInfo returnInfo =new ReturnInfo();
		
		/*List<Map<String, Object>> list = tracdemarkTaskMapper.queryNotification(custId);*/
		
		Date enDate = DateTool.StringToDateTime(createTime);
		enDate = DateTool.getDateAfter(enDate, 1);
		String deadTime = DateTool.getDate(enDate);
		long count = trademarkNotifyMapper.queryNotificationCount(custId, createTime, deadTime, gcon);
		List<Map<String, Object>> list =trademarkNotifyMapper.queryNotification(custId, createTime,deadTime,gcon);
		if(list!=null && list.size()!=0){
			
			//转换时间格式
			for(Map<String, Object> map :list){
				Date appDate = (Date)map.get("appDate");
				Date valid = (Date)map.get("validEndDate");
				Date create = (Date)map.get("createTime");
				String appDate1="";
				String validEndDate ="";
				String createTime1 ="";
				if(appDate!=null){
					 appDate1 = DateTool.getDate(appDate);
				}
				if(valid!=null){
					 validEndDate = DateTool.getDate(valid);
				}
				if(create!=null){
					 createTime1 = DateTool.getDate(create);
				}
				map.put("appDate", appDate1);
				map.put("validEndDate", validEndDate);
				map.put("createTime", createTime1);
			}
		}
		returnInfo.setData(list);
		returnInfo.setTotal(count);
		return returnInfo;
	}


	@Override
	public ReturnInfo queryAllTimes(Integer custId) {
		ReturnInfo returnInfo = new ReturnInfo();
		List<String> data = trademarkNotifyMapper.queryAllTimes(custId);
		List<String> result = new ArrayList<>();
		if(data!=null && data.size()>0){
			for(String date:data){
				String dd = date.substring(0,10);
				result.add(dd);
			}	
		}
		returnInfo.setData(result);
		return returnInfo;
	}

	@Override
	public ReturnInfo queryRenewalRemindTimes(GeneralCondition gCondition,Integer custId) {
		ReturnInfo returnInfo = new ReturnInfo();
		List<String> data = trademarkNotifyMapper.queryRenewalRemindTimes(gCondition,custId);
		returnInfo.setData(data);
		return returnInfo;
	}

	
	@Override
	public ReturnInfo queryTmAbnormalList(Integer custId,
			GeneralCondition gcon,Integer userId) {
		ReturnInfo returnInfo = new ReturnInfo();
		
		List<Map<String, Object>>data = trademarkNotifyMapper.queryAbnormalList(custId, gcon,userId);
		long count = trademarkNotifyMapper.queryAbnormalCount(custId, gcon,userId);
		if(data!=null && data.size()!= 0){
			//时间格式问题
			for(Map<String, Object> map:data){
				Date create = (Date)map.get("createDate");
				Date statusDate = (Date)map.get("statusDate");
				if(create !=null){
					 String createDate = DateTool.getDate(create);
					 map.put("createDate", createDate);
				}
				if((Date)map.get("modifyDate")!=null){
					String modifyDate = DateTool.getDate((Date)map.get("modifyDate"));
					map.put("modifyDate", modifyDate);
				}
				if(statusDate !=null){
					String statusD = DateTool.getDate(statusDate);
					map.put("statusDate", statusD);
				}
				int abnormalType = (int)map.get("abnormalType");
				switch (abnormalType) {
					case 1 :
						map.put("abnormalType", "被驳回");
						break;
					case 2 :
						map.put("abnormalType", "被异议");
						break;
					case 3 :
						map.put("abnormalType", "被撤三");
						break;
					case 4 :
						map.put("abnormalType", "被无效");
						break;
					case 5 :
						map.put("abnormalType", "被通用");
						break;
					default :
						break;
				}
			}
		}
		returnInfo.setTotal(count);
		returnInfo.setData(data);
		return returnInfo;
	}
	//更新商标异常通知
	@Override
	public void modifyTmAbnormal(List<String> ids) {
		// TODO Auto-generated method stub
		for(int i=0;i<ids.size();i++){
			String id = ids.get(i);
			trademarkNotifyMapper.modifyTmAbnormal(id);
		}
		
	}
	//更新商标续展数据
	@Override
	public void modifyNotification(List<String> idStrings) {
		if(idStrings.size() != 0){
			for(int i= 0;i<idStrings.size();i++){
				String id = idStrings.get(i);
				trademarkNotifyMapper.modifyNotification(id);
			}
		}
		
	}
	
	

	//查询 商标初审公告 信息
	@Override
	public ReturnInfo queryAnnouncementRemind(GeneralCondition gcon,
			Integer custId, Integer appId,Integer userId,Integer type) {
		ReturnInfo returnInfo = new ReturnInfo();
		
		List<Map<String, Object>> list = trademarkNotifyMapper.queryAnnouncementRemind(custId,gcon,userId,type);
		long count = trademarkNotifyMapper.queryAnnouncementRemindCount(gcon,custId,userId,type);
		//转换时间格式
		for(int i=0;i<list.size();i++){
			Map<String, Object> map = list.get(i);
			Date ggDate = (Date)map.get("ggDate");
			Date appDate = (Date)map.get("appDate");
			Date createDate = (Date)map.get("createDate");
			if(ggDate != null){
				map.put("ggDate",DateTool.getDate(ggDate));
			}
			if(appDate !=null){
				map.put("appDate", DateTool.getDate(appDate));
			}
			if(createDate !=null){
				map.put("createDate", DateTool.getDate(createDate));
			}
		}
		returnInfo.setData(list);
		returnInfo.setTotal(count);
		
		return returnInfo;
	}

	//更新公告通知
	@Override
	public void updateAnnouncementRemind(List<String> ids) {
		ReturnInfo returnInfo= new ReturnInfo();
		for(int i=0;i<ids.size();i++){
			String id = ids.get(i);
			trademarkNotifyMapper.updateAnnouncementRemind(id);
		}
	}


	//提供给邮件服务 的公告数据
	@Override
	public ReturnInfo queryAnnoucementForMail(Integer custId,Integer type) {
		ReturnInfo returnInfo = new ReturnInfo();
		List<Map<String, Object>>list = trademarkNotifyMapper.queryAnnoucementForMail(custId,type);
		if(list.size()!=0){
			returnInfo.setData(list);
		}
		return returnInfo;
	}


	@Override
	public ReturnInfo queryAllGGQiao(Integer custId) {
		ReturnInfo returnInfo = new ReturnInfo();
		List<String> qiHao = trademarkNotifyMapper.queryAllGGQiHao(custId);
		returnInfo.setData(qiHao);
		return returnInfo;
	}


	@Override
	public ReturnInfo queryAbnormalTimes(Integer custId) {
		ReturnInfo returnInfo = new ReturnInfo();
		List<String> times = trademarkNotifyMapper.selectAbnormalTimes(custId);
		returnInfo.setData(times);
		return returnInfo;
	}





	


	
	
	

	
	
	
	
	
	
	
}
