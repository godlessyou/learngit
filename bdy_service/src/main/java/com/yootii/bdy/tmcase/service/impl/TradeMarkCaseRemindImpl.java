package com.yootii.bdy.tmcase.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


import org.springframework.stereotype.Component;



import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.customer.service.ApplicantService;
import com.yootii.bdy.remind.model.Remind;
import com.yootii.bdy.remind.service.RemindService;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseMapper;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.trademark.dao.TrademarkMapper;
import com.yootii.bdy.trademark.model.Trademark;
import com.yootii.bdy.util.DateTool;


@Component
public class TradeMarkCaseRemindImpl {	

	@Resource
	private ApplicantService applicantService;

	@Resource
	private TradeMarkCaseMapper tradeMarkCaseMapper;

	@Resource
	private RemindService remindService;

	@Resource
	private TrademarkMapper trademarkMapper;

	@Resource
	private TradeMarkCaseQueryImpl tradeMarkCaseQueryImpl;

	
	
	/**
	 * 		优先权时限
		b)	补正时限
		c)	驳回时限
		d)	异议答辩时限
		e)	续展时限
	 * 统计商标案件的截止日期各个时期的数量
	 * 思路：根据创建时间 和案件的截止时间  计算出两个时间相差的天数，然后 把相差的天数分成 3等份，
	 * 再计算现在的时间是出于3个时间段的哪一个阶段，进而统计出数值。
	 * 3个阶段：security(安全期) warn(提醒期) danger(危险期)
	 */
	public ReturnInfo statisticTmCaseDeadline(GeneralCondition gcon,Integer userId,Integer custId)throws Exception {
		ReturnInfo returnInfo = new ReturnInfo();
		/*ReturnInfo returnInfo = remindService.selectRemindList(remind, gcon);
		List<Remind> list= (List<Remind>)returnInfo.getData();*/
		/*List<Remind> list = tradeMarkCaseMapper.selectRemindList(userId);*/
		
		List<Remind> priorityList = tradeMarkCaseMapper.selectRemindList(userId, custId,"优先权时限");
		List<Remind> supplementList = tradeMarkCaseMapper.selectRemindList(userId, custId, "补正时限");
		List<Remind> rejectList = tradeMarkCaseMapper.selectRemindList(userId, custId, "驳回复审时限");
		List<Remind> dissentList = tradeMarkCaseMapper.selectRemindList(userId, custId, "异议答辩时限");
		List<Remind> extensionList = tradeMarkCaseMapper.selectRemindList(userId, custId, "续展时限");
		
		Map<String, Object> map1 = dataWar(priorityList, "优先权时限");
		Map<String, Object> map2 = dataWar(supplementList, "补正时限");
		Map<String, Object> map3 = dataWar(rejectList, "驳回复审时限");
		Map<String, Object> map4 = dataWar(dissentList, "异议答辩时限");
		Map<String, Object> map5 = dataWar(extensionList, "续展时限");
		List<Map<String, Object>> data = new ArrayList<>();
		data.add(map1);
		data.add(map2);
		data.add(map3);
		data.add(map4);
		data.add(map5);
		returnInfo.setData(data);
		return returnInfo;
	}

	// 根据条件查询 时限数据

	public ReturnInfo queryTmcaseDeadline(GeneralCondition gcon,
			Integer custId, Integer userId, String message,
			Integer urgencyType, Remind remind) throws Exception {

		ReturnInfo returnInfo = new ReturnInfo();
		// List<Remind> list = tradeMarkCaseMapper.queryByCondition(gcon,custId,
		// userId,message);
		Map<String, Object> resultMap = new HashMap();
		List<Remind> result = new ArrayList<>();
		List<Remind> list = tradeMarkCaseMapper.selectRemindList(userId,
				custId, message);
		Map<String, Object> map = dataWar(list, message);
		String status = "";
		// 数据处理
		if (map != null) {
			if (urgencyType != null) { // 如果从页面上传过来的不为空，则具体查询某个时期的数据，否则
				// 统计 好的数据
				List<Map<String, Object>> data = (List<Map<String, Object>>) map
						.get(message);
				switch (urgencyType) {
				case 0:
					status = "安全";
					// 解封数据
					/*
					 * for(int i=0;i<data.size();i++){ Map<String, Object>
					 * statusRemind = data.get(i); String sourceStatus =
					 * (String)statusRemind.get("status"); List<Remind>
					 * sourceInclude =(List<Remind>)statusRemind.get("include");
					 * if(sourceStatus.equals(status) && sourceInclude !=null){
					 * //通过rid 遍历查询 Map<String, Object> map2 = new HashMap<>();
					 * map2.put("list", sourceInclude); map2.put("gcon", gcon);
					 * list = tradeMarkCaseMapper.queryByCondition(map2); } }
					 */
					resultMap = this.deArchive(data, gcon, status, remind);
					break;
				case 1:
					status = "提醒";
					resultMap = this.deArchive(data, gcon, status, remind);
					break;
				case 2:
					status = "危险";
					resultMap = this.deArchive(data, gcon, status, remind);
					break;
				default:
					break;
				}
				long total = (Integer) resultMap.get("total");
				returnInfo.setData(resultMap.get("data"));
				returnInfo.setTotal(total);
			} else {
				// 只是查询 某种类型的时限
				List<Remind> list2 = new ArrayList<>();
				int total = 0;
				if (list != null) {
					List<Integer> ids = new ArrayList<>();
					Map<String, Object> map2 = new HashMap<>();
					for (int i = 0; i < list.size(); i++) {
						Integer rid = list.get(i).getRid();
						ids.add(rid);
					}
					map2.put("list", ids);
					map2.put("gcon", gcon);
					map2.put("remind", remind);
					list2 = tradeMarkCaseMapper.queryByCondition(map2);
					total = tradeMarkCaseMapper.queryByConditionCount(map2);
				}

				returnInfo.setData(list2);
				returnInfo.setTotal((long) total);
			}
		}

		return returnInfo;
	}
	
	
	

	// 解封数据，并根据解封的数据 查询
	private Map<String, Object> deArchive(List<Map<String, Object>> data,
			GeneralCondition gcon, String status, Remind remind) {
		List<Remind> list = new ArrayList<>();
		Map<String, Object> resultMap = new HashMap<>();
		int total = 0;
		for (int i = 0; i < data.size(); i++) {
			Map<String, Object> statusRemind = data.get(i);
			String sourceStatus = (String) statusRemind.get("status");
			List<Remind> sourceInclude = (List<Remind>) statusRemind
					.get("include");
			if (sourceStatus.equals(status) && sourceInclude != null) {
				List<Integer> rids = new ArrayList<>();
				for (int j = 0; j < sourceInclude.size(); j++) {
					int rid = sourceInclude.get(j).getRid();
					rids.add(rid);
				}
				// 通过rid 遍历查询
				Map<String, Object> map = new HashMap<>();
				map.put("list", rids);
				map.put("gcon", gcon);
				map.put("remind", remind);
				list = tradeMarkCaseMapper.queryByCondition(map);
				total = tradeMarkCaseMapper.queryByConditionCount(map);
			}
		}
		resultMap.put("data", list);
		resultMap.put("total", total);
		return resultMap;
	}
	
	

	// 数据处理封装
	private Map<String, Object> dataWar(List<Remind> list, String message)
			throws Exception {

		int securityCount = 0;
		int warnCount = 0;
		int dangerCount = 0;
		List<Remind> securityReminds = new ArrayList<>();
		List<Remind> warnReminds = new ArrayList<>();
		List<Remind> dangerReminds = new ArrayList<>();
		// 遍历
		for (Remind rem : list) {
			Date begin = rem.getCreatedate();
			Date end = rem.getLimitdate();
			if (begin == null && end == null)
				throw new Exception();
			// 起始时间 单位毫秒
			long beginMillSeconds = begin.getTime();
			long endMillSeconds = end.getTime();
			long gap = endMillSeconds - beginMillSeconds;
			// 获取相差多少天
			int gapDays = (int) (gap / 1000) / (3600 * 24);
			// 分成 3个时期 每个时期的天数
			int avgDays = gapDays / 3;
			// 安全期 (创建日期向后+一个平均日)
			Date security = DateTool.getDateAfter(begin, avgDays);
			// 提醒期(创建日期向后+两个个平均日)
			Date warn = DateTool.getDateAfter(begin, avgDays * 2);
			Date now = new Date();
			// 如果现在时间早于安全截止时间，则处于安全期
			if (now.before(security)) {
				securityCount++;
				securityReminds.add(rem);
				// 晚于 安全期 早于 提醒期 处于提醒期
			} else if (now.after(security) && now.before(warn)) {
				warnCount++;
				warnReminds.add(rem);
			} else {
				dangerCount++;
				dangerReminds.add(rem);
			}
		}
		Map<String, Object> map1 = new HashMap<>();
		Map<String, Object> map2 = new HashMap<>();
		Map<String, Object> map3 = new HashMap<>();
		map1.put("count", securityCount);
		map1.put("include", securityReminds);
		map1.put("status", "安全");
		map2.put("count", warnCount);
		map2.put("include", warnReminds);
		map2.put("status", "提醒");
		map3.put("count", dangerCount);
		map3.put("include", dangerReminds);
		map3.put("status", "危险");
		List<Map<String, Object>> data = new ArrayList<>();
		data.add(map1);
		data.add(map2);
		data.add(map3);

		Map<String, Object> map = new HashMap<>();
		map.put(message, data);

		return map;
	}

	
	
	
	
	// 批量关闭 时限提醒
	public void updateTmCaseDeadLine(String idString) throws Exception {
		ReturnInfo returnInfo = new ReturnInfo();
		List<String> ids = new ArrayList<>();
		String[] arr = idString.split(",");
		if (arr.length == 1) {
			String id = idString;
			ids.add(id);
		} else {
			ids = Arrays.asList(arr);
		}
		// 关闭时限提醒
		if (ids.size() != 0) {
			for (int i = 0; i < ids.size(); i++) {
				String rid = ids.get(i);
				tradeMarkCaseMapper.updateTmCaseDeadLine(rid);
			}
		}
	}
	
	
	
	

	public ReturnInfo createRemind(Integer serviceId, Integer caseId) {
		ReturnInfo info = new ReturnInfo();
		/***** 创建案件时限 ********/
		Date date = new Date();

		TradeMarkCase tradeMarkCase = (TradeMarkCase) tradeMarkCaseQueryImpl
				.queryTradeMarkCaseDetail(caseId).getData();
		GeneralCondition gcon = new GeneralCondition();
		gcon.setOffset(0);
		gcon.setRows(1);
		Trademark trademark = new Trademark();
		switch (serviceId) {
		// 注册
		case 8:
		case 9:
			// 如果优先权类型不为 “无” 则创建优先权时限
			if (!tradeMarkCase.getPriorityType().equals("无")) {
				date = tradeMarkCase.getPriorityAppDate();
				// 如果优先权日期不为空，则生成通知，否则不执行
				if (date != null)
					remindService.insertRemindByType(11, date, null,
							tradeMarkCase.getCustId(), caseId, gcon);
				// 发送 优先权时限 邮件

			}
			break;
		// 续展 创建续展时限 以此此商标的截止日期 作为续展截止日期的标准
		case 23:
		case 24:
			trademark.setRegNumber(tradeMarkCase.getRegNumber());
			// date = ((List<Trademark>)tradeMarkService.queryTmlistSimple(gcon
			// , null, 0, trademark, null).getData()).get(0).getRegNoticeDate();
			String regNumber = trademark.getRegNumber();
			if (regNumber != null && regNumber != "") {
				// 根据注册号查询出 此商标的截止日期
				Map<String, Object> map = trademarkMapper
						.selectTmByRegNumber(regNumber);
				Date validEndDate = (Date) map.get("validEndDate");
				if (validEndDate != null) {
					date = validEndDate;
				}
			}
			if (date != null) {
				remindService.insertRemindByType(1, date, null,
						tradeMarkCase.getCustId(), caseId, gcon);
				// 发送 续展时限 邮件
				String mailType = "sbzcsq_xzsx_en";
				Map<String, Object> emailMap = new HashMap<>();
			}

			break;
		// 创建异议申请案件时限
		case -1:
		case 37:
			remindService.insertRemindByType(2, date, null,
					tradeMarkCase.getCustId(), caseId, gcon);
			break;
		}

		/********************/

		info.setSuccess(true);
		info.setMessage("实现创建成功");
		return info;
	}

}
