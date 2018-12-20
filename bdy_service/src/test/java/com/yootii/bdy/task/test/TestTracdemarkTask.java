package com.yootii.bdy.task.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.task.dao.TracdemarkTaskMapper;
import com.yootii.bdy.task.model.Notification;
import com.yootii.bdy.task.model.TmCaseTaskToDoList;
import com.yootii.bdy.task.service.TracdemarkTaskService;
import com.yootii.bdy.task.service.TradeMarkCaseTaskService;
import com.yootii.bdy.task.service.Impl.AgencyTaskServiceImpl;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.service.TradeMarkCaseService;
import com.yootii.bdy.trademark.service.TradeMarkNotifyService;
import com.yootii.bdy.util.DateTool;
import com.yootii.bdy.util.JsonUtil;

import sun.java2d.loops.GeneralRenderer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class TestTracdemarkTask {

	
	@Autowired
	private TracdemarkTaskService tracdemarkTaskService;
	
	@Autowired
	private TradeMarkCaseService tradeMarkCaseService;
	
	@Autowired
	private TracdemarkTaskMapper tracdemarkTaskMapper;
	@Autowired
	private TradeMarkCaseTaskService tradeMarkCaseTaskService;
	@Resource
	private AgencyTaskServiceImpl agencyTaskServiceImpl;
//	@Test
	public void Test1(){
		Date date = new Date();
		Date dd = DateTool.getDateBeforeMonth(date, 6);
		Date aa = DateTool.getDateAfterMonth(date,6);
		
		tracdemarkTaskMapper.selectTMByDate(dd, aa);
		System.out.println("sdfsd");
	}
	
//	@Test
	public void Test2(){
		ArrayList<Notification> list = new ArrayList<>();
		Notification notification=new Notification();
		notification.setTmId(181913);
		notification.setTmName("无限极海豹油");
		notification.setTmDeadTime(new Date());
//		list.add(notification);
//		tracdemarkTaskMapper.insert(list);
	}
	
//	@Test
	public void TestCreateNotication(){
		
		try{
			tracdemarkTaskService.scheduling();
			System.err.println("成功");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	@Test
	public void TestFindTmList(){
	
		Object object = tracdemarkTaskService.findTmList(13);
		
		if(object!=null){
			System.out.println(JsonUtil.toJson(object));
			
			System.out.println("成功");
		}
		
	}
	
	@Test
	public void Test4(){
		GeneralCondition gcon = new GeneralCondition();
		gcon.setOffset(0);
		gcon.setRows(10);
		/*tracdemarkTaskMapper.queryRenewalRemindListCount(gcon,3);
		
		Object info = tracdemarkTaskMapper.queryRenewalRemindList(gcon,3);*/
		
//		Object info =tracdemarkTaskService.queryRenewalRemindList(gcon, 5);
//		if (info != null) {
//			System.out.println(JsonUtil.toJson(info));
//			System.out.println("queryRenewalRemindListCount测试通过");
//		}
	}
	
//	@Test
	public void Test3(){
		
		/*List<MailDTO> list =tracdemarkTaskMapper.selectTmForMails();
		System.err.println(list.size());
		System.err.println(list.get(0).getTrademarks().size());
		List<Trademark> trademarks = list.get(0).getTrademarks();
		System.out.println(list.get(0).getCustId());
		for(Trademark trademark:trademarks){
			System.out.println(trademark.getTmId());
		}*/
	}
	
	//@Test
	public void TestautoTimingCreateTmAbnormal(){
		
		System.err.println("开始");
		long start = System.currentTimeMillis();
		tracdemarkTaskService.autoTimingCreateTmAbnormal();
		long end = System.currentTimeMillis();
		System.err.println(end-start);
		System.err.println("结束");
	}
	
	//@Test
	public void TestAutoTimingCreateAnnouncementRemind(){
		
		System.err.println("开始");
		long start = System.currentTimeMillis();
		try{
			tracdemarkTaskService.autoTimingCreateAnnouncementRemind();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		long end = System.currentTimeMillis();
		System.err.println(end-start);
		System.err.println("结束");
	}
	
	@Test
	public void TestqueryRenewalRemindList(){
		
		GeneralCondition gcon  = new GeneralCondition();
		Integer custId = 3;
		gcon.setType(2);
		gcon.setKeyword("R");
		gcon.setCreateTime("2018-10-09");
		gcon.setRows(11);
		gcon.setOffset(0);
		Object info;
		try{
			info = tracdemarkTaskService.queryRenewalRemindList(gcon, custId);
			if(info != null){
				System.err.println(JsonUtil.toJson(info));
				System.err.println("测试成功");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	@Test
	public void testCreat(){
		TradeMarkCase tradeMarkCase = new TradeMarkCase();
		
		GeneralCondition gcon = new GeneralCondition();
		gcon.setTokenID("");
		//	String tmNumber = "";
		
	//	tradeMarkCaseService.createDissentApplicantEntrance(tradeMarkCase,tmNumber, gcon);
	
		//
		Object info;
		TradeMarkCase tmCase=new TradeMarkCase();
		tmCase.setId(16831);
		tmCase.setAgencyId(1);
		Map<String, Object> map = new HashMap<>();
		map.put("gcon", gcon);
		map.put("userId", "3");
		map.put("customerId", "3");
		map.put("agencyServiceId","1");
		map.put("serviceId", "37");
		map.put("tmCase", tmCase);
		
		info = tradeMarkCaseTaskService.startComplexCaseApp(map);
	
	}
	
	//直接递交
//	@Test
	public void testappoffline(){
		GeneralCondition gcon = new GeneralCondition();
		String userId = "3";
		String submitStatus = "";
		TmCaseTaskToDoList toDoList = new TmCaseTaskToDoList();
		//toDoList.setTaskId();
		String submitMode = "2";
		boolean send = true;
		
		Map<String, Object> proMap = new HashMap<>();
		
		//agencyTaskServiceImpl.appOffLine(gcon, userId, submitStatus, toDoList, proMap);
		agencyTaskServiceImpl.audited(gcon, userId, submitMode, toDoList,send, proMap);
	
	}
	
	
	
	
}
