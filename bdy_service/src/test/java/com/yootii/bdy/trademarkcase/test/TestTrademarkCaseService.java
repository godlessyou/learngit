package com.yootii.bdy.trademarkcase.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.ibatis.javassist.runtime.Desc;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sun.media.jfxmedia.control.VideoDataBuffer;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.Globals;
import com.yootii.bdy.common.LoginReturnInfo;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.downloadapplicant.service.DownloadApplicantService;
import com.yootii.bdy.model.User;
import com.yootii.bdy.remind.model.Remind;
import com.yootii.bdy.remind.service.RemindService;
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.security.service.AuthenticationService;
import com.yootii.bdy.task.service.TradeMarkCaseTaskService;
import com.yootii.bdy.task.service.Impl.TaskCommonImpl;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseMapper;
import com.yootii.bdy.tmcase.model.Goods;
import com.yootii.bdy.tmcase.model.GoodsPlan;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.TradeMarkCaseCategory;
import com.yootii.bdy.tmcase.model.TradeMarkCaseFile;
import com.yootii.bdy.tmcase.model.TradeMarkCaseJoinApp;
import com.yootii.bdy.tmcase.service.TradeMarkCaseFileService;
import com.yootii.bdy.tmcase.service.TradeMarkCaseService;
import com.yootii.bdy.tmcase.service.impl.TradeMarkCaseCommonImpl;
import com.yootii.bdy.user.service.UserService;
import com.yootii.bdy.util.DateTool;
import com.yootii.bdy.util.GonUtil;
import com.yootii.bdy.util.JsonUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class TestTrademarkCaseService {
	@Resource
	private AuthenticationService authenticationService;
	@Resource
	private TradeMarkCaseService tradeMarkCaseService;
	@Resource
	private TradeMarkCaseFileService tradeMarkCaseFileService;
	
	@Resource
	private TaskCommonImpl taskCommonImpl;	
	
	@Resource
	private UserService userService;
	
	@Resource
	private TradeMarkCaseCommonImpl tradeMarkCaseCommonImpl;
	
	@Resource
	private DownloadApplicantService downloadApplicantService;
	
	@Resource
	private TradeMarkCaseMapper tradeMarkCaseMapper;
	@Autowired
	private RemindService remindService;

	@Autowired
	private TradeMarkCaseTaskService tradeMarkCaseTaskService;
	private static final Logger logger = Logger.getLogger(TestTrademarkCaseService.class);
	
	@Test
	public void test1(){
		
		int count = tradeMarkCaseMapper.isExist(2341);
		System.err.println(count);
	}
	
	
//	@Test
	public void createTmcaseTest() {
		TradeMarkCase tradeMarkCase = new TradeMarkCase();
		tradeMarkCase.setCaseType("商标注册");
		tradeMarkCase.setCaseTypeId(1);
		tradeMarkCase.setCustId(3);
		tradeMarkCase.setAgencyId(1);
		tradeMarkCase.setTmName("测试注册一个新商标");
		tradeMarkCase.setAppCnName("达能日尔维公司");
		tradeMarkCase.setAppEnName("COMPAGNIE GERVAIS DANONE");
		tradeMarkCase.setApplicantType("中国大陆");
		String status="申请中";
		tradeMarkCase.setStatus(status);
//		List<TradeMarkCaseJoinApp> joinApps = new ArrayList<TradeMarkCaseJoinApp>();
//		TradeMarkCaseJoinApp joinApp = new TradeMarkCaseJoinApp();
//		joinApp.setJoinAppType("法人或其他组织");
//		joinApp.setJoinAppCoun("中国大陆");
//		joinApp.setNameCn("咯咯咯");
//		joinApp.setAddrCn("北京海淀区");
//		joinApp.setCardName("身份证");
//		joinApp.setCardId("133523585832122");
//		joinApps.add(joinApp);
		TradeMarkCaseCategory good = new TradeMarkCaseCategory();
		List<TradeMarkCaseCategory> goods = new ArrayList<TradeMarkCaseCategory>();
		good.setGoodClass("1");
		good.setGoodName("碱金属");
		good.setSimilarGroup("0111");
		good.setGoodCode("123456");
//		good.setGoodKey("HKHGK544545JHUHd5458dfJUU");
		goods.add(good);
//		tradeMarkCase.setJoinApps(joinApps);
		tradeMarkCase.setGoods(goods);
		
		GeneralCondition gcon=getGcon("whd_wangfang");
		ReturnInfo rtnInfo  =tradeMarkCaseService.createTradeMarkCase(tradeMarkCase, gcon);
		
		String userId=null;
		String customerId="3";
		String caseType="商标注册";
		
		if (rtnInfo != null && rtnInfo.getSuccess()){								
			logger.info("createTradeMarkCase finish");
			Map<String, Object> resData = (Map<String, Object>) rtnInfo.getData();
			
			if (resData!=null){
				Integer  caseId=(Integer)resData.get("caseId");
				Integer agencyId=(Integer)resData.get("agencyId");
				
				//发送商标案件立案通知邮件（英文）
				String mailType="sbaj_create_en";
				boolean sendToCust=true;	
				String tokenID=gcon.getTokenID();
				List<String> userList=null;
				String emailUserId=userId;
				if (emailUserId==null || emailUserId.equals("")){
					String permission="案件分配";
					userList=userService.findUsersByPermission(permission, agencyId.toString(), tokenID);
					if (userList!=null && userList.size()>0){
						emailUserId=userList.get(0);
					}
				}
				Map<String, Object> emailMap=new HashMap<String, Object>();
				emailMap.put("caseId", caseId.toString());						
				emailMap.put("gcon", gcon);
				emailMap.put("userId", emailUserId);
				emailMap.put("custId", customerId);
				emailMap.put("sendToCust", sendToCust);
				emailMap.put("mailType", mailType);
				emailMap.put("caseType", caseType);
				
				try {
//					taskCommonImpl.sendMail(emailMap);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	
//	@Test
	public void createTmcasebyregnumberTest() {
		TradeMarkCase tradeMarkCase = new TradeMarkCase();
		tradeMarkCase.setCaseType("商标注册");
		tradeMarkCase.setCustId(7);
		tradeMarkCase.setAgencyId(1);
		tradeMarkCase.setTmName("测试注册一个新商标");
		tradeMarkCase.setApplicantType("中国大陆");
//		List<TradeMarkCaseJoinApp> joinApps = new ArrayList<TradeMarkCaseJoinApp>();
//		TradeMarkCaseJoinApp joinApp = new TradeMarkCaseJoinApp();
//		joinApp.setJoinAppType("法人或其他组织");
//		joinApp.setJoinAppCoun("中国大陆");
//		joinApp.setNameCn("咯咯咯");
//		joinApp.setAddrCn("北京海淀区");
//		joinApp.setCardName("身份证");
//		joinApp.setCardId("133523585832122");
//		joinApps.add(joinApp);
		String status="申请中";
		tradeMarkCase.setStatus(status);
		TradeMarkCaseCategory good = new TradeMarkCaseCategory();
		List<TradeMarkCaseCategory> goods = new ArrayList<TradeMarkCaseCategory>();
		good.setGoodClass("1");
		good.setGoodName("碱金属");
		good.setSimilarGroup("0111");
		good.setGoodCode("123456");
//		good.setGoodKey("HKHGK544545JHUHd5458dfJUU");
		goods.add(good);
//		tradeMarkCase.setJoinApps(joinApps);
		tradeMarkCase.setGoods(goods);
		
		
		Token token = new Token();
		token.setTokenID("15272395987530");
		Globals.setToken(token );
//		ReturnInfo rtnInfo  =tradeMarkCaseService.createTradeMarkCaseByTmNumber(tradeMarkCase, "G853690");
//		rtnInfo.getClass();
//		
//		String userId=null;
//		String customerId="1";
//		String caseType="商标注册";
//		GeneralCondition gcon=getGcon("whd_wangfang");
//		if (rtnInfo != null && rtnInfo.getSuccess()){								
//			logger.info("createTradeMarkCase finish");
//			Map<String, Object> resData = (Map<String, Object>) rtnInfo.getData();
//			
//			if (resData!=null){
//				Integer  caseId=(Integer)resData.get("caseId");
//				Integer agencyId=(Integer)resData.get("agencyId");
//				
//				//发送商标案件立案通知邮件（英文）
//				String mailType="sbaj_create_en";
//				boolean sendToCust=true;	
//				String tokenID=gcon.getTokenID();
//				List<String> userList=null;
//				String emailUserId=userId;
//				if (emailUserId==null || emailUserId.equals("")){
//					String permission="案件分配";
//					userList=userService.findUsersByPermission(permission, agencyId.toString(), tokenID);
//					if (userList!=null && userList.size()>0){
//						emailUserId=userList.get(0);
//					}
//				}
//				Map<String, Object> emailMap=new HashMap<String, Object>();
//				emailMap.put("caseId", caseId.toString());						
//				emailMap.put("gcon", gcon);
//				emailMap.put("userId", emailUserId);
//				emailMap.put("custId", customerId);
//				emailMap.put("sendToCust", sendToCust);
//				emailMap.put("mailType", mailType);
//				emailMap.put("caseType", caseType);
//				
//				try {
//					taskCommonImpl.sendMail(emailMap);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
	}
	
	
	@Test
	public void createTradeMarkCaseByTmNumberTest() {
		TradeMarkCase tradeMarkCase = new TradeMarkCase();
		tradeMarkCase.setCaseType("商标续展");
		tradeMarkCase.setCaseTypeId(2);
		tradeMarkCase.setCustId(3);
		tradeMarkCase.setAgencyId(1);
		
		tradeMarkCase.setApplicantType("中国大陆");

		GeneralCondition gcon=getGcon("whd_wangfang");
		
		String tmNumber="11788427";
	
//		Object info =tradeMarkCaseService.createTradeMarkCaseByTmNumber(tradeMarkCase, tmNumber, gcon);
//		if (info != null) {
//			logger.info(JsonUtil.toJson(info));
//			logger.info("createTradeMarkCaseByTmNumber测试通过");
//		}
	}
	
	//测试注册一个拒绝复审
	@Test
	public void testcreateRejectRechickTMCase(){
		TradeMarkCase tradeMarkCase = new TradeMarkCase();
		tradeMarkCase.setCustId(3);
		tradeMarkCase.setApplicantType("中国大陆");
		
//		GeneralCondition gCondition = getGcon("whd_wangfang");
//		String tmNumber = "11788427";
//		Object info;
//		try{
//			info = tradeMarkCaseService.createAppCase(tradeMarkCase, tmNumber, gCondition);
//			logger.info("测试完毕");
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
		
	}
	
	
	
	
	
//	@Test
	public void modifyTmcaseTest() {
		TradeMarkCase tradeMarkCase = new TradeMarkCase();
		
		Integer caseId=1;
		tradeMarkCase.setId(caseId);
//		tradeMarkCase.setCustId(1);
//		tradeMarkCase.setAgencyId(1);
//		tradeMarkCase.setTmName("一个怪名字");
//		tradeMarkCase.setApplicantType("法人或其它组织");
		tradeMarkCase.setWhoIsApp("1");
		
//		List<TradeMarkCaseCategory> goods=new ArrayList<TradeMarkCaseCategory>();
//		
//		TradeMarkCaseCategory tradeMarkCaseCategory=new TradeMarkCaseCategory();
//		tradeMarkCaseCategory.setCaseId(caseId);
//		tradeMarkCaseCategory.setGoodClass("32");
//		tradeMarkCaseCategory.setGoodCode("32001");
//		tradeMarkCaseCategory.setGoodKey("1111111111");
//		tradeMarkCaseCategory.setGoodName("测试商品服务描述");
//		goods.add(tradeMarkCaseCategory);
//		tradeMarkCase.setGoods(goods);
//		
//		List<TradeMarkCaseJoinApp> joinApps=new ArrayList<TradeMarkCaseJoinApp>();
//		
//		TradeMarkCaseJoinApp tradeMarkCaseJoinApp=new TradeMarkCaseJoinApp();
//		
//		tradeMarkCaseJoinApp.setAddrCn("测试用的共同申请人中文地址");
//		tradeMarkCaseJoinApp.setAddrEn("测试用的共同申请人英文地址");
//		
//		joinApps.add(tradeMarkCaseJoinApp);
//		tradeMarkCase.setJoinApps(joinApps);
		
//		GeneralCondition gcon=getGcon("whd_wangfang");
//		
//		Object info =tradeMarkCaseService.modifyTradeMarkCase(tradeMarkCase, gcon);
//		if (info != null) {
//			logger.info(JsonUtil.toJson(info));
//			logger.info("queryUserList测试通过");
//		}
	}
	
	
//	@Test
	public void queryTmcaseListTest() throws Exception {
		TradeMarkCase tradeMarkCase = new TradeMarkCase();
//		tradeMarkCase.setTmName("yoo");
//		tradeMarkCase.setAppEnName("tencent");
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		tradeMarkCase.setAppStartTime(sdf.parse("2017-12-20"));
//		tradeMarkCase.setAppEndTime(sdf.parse("2018-2-1"));
//		tradeMarkCase.setRegStartTime(sdf.parse("2018-1-1"));
//		tradeMarkCase.setRegEndTime(sdf.parse("2018-4-1"));
		
//		String username="whd_wangfang";
//		GeneralCondition gcon = getGcon(username);	
		
		
		tradeMarkCase.setCustId(1);
		tradeMarkCase.setAgencyId(1);
		tradeMarkCase.setCotag(0);
		tradeMarkCase.setBillStatus(0);
		GeneralCondition gcon = new GeneralCondition();	
		GonUtil.makeOffsetAndRows(gcon);

		
		Token token = new Token();
//		String tokenID=gcon.getTokenID();
//		token.setTokenID(tokenID);
		boolean isUser=true;
		token.setUser(isUser);
		Integer userID=2; //管理员
//		Integer userID=3; //测试二级部门负责人
//		Integer userID=20; //测试一级部门负责人
//		Integer userID=4; //测试公司领导
		token.setUserID(userID);
		Object info =tradeMarkCaseService.queryTradeMarkCaseList(tradeMarkCase, gcon,token,1);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
	
	
//	@Test
	public void queryTmcaseDetailTest() {
		Object info =tradeMarkCaseService.queryTradeMarkCaseDetail(1);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}

//	@Test
	public void updateSolrTradeMarkCaseTest() {
		Object info =tradeMarkCaseService.updateSolrTradeMarkCase();
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("updateSolrTradeMarkCaseTest测试通过");
		}
	}
	
//	@Test
	public void checkTradeMarkCaseTest() {
		Integer id=5747;
//		TradeMarkCase tradeMarkCase = tradeMarkCaseMapper.selectByPrimaryKey(id);
		
		TradeMarkCase tradeMarkCase =(TradeMarkCase) tradeMarkCaseService.queryTradeMarkCaseForWs(id).getData();
		
		
//		Object info =tradeMarkCaseCommonImpl.checkTradeMarkCase(tradeMarkCase);
//		if (info != null) {
//			logger.info(JsonUtil.toJson(info));
//			logger.info("updateSolrTradeMarkCaseTest测试通过");
//		}
	}
	
	
	
//	@Test
	public void statsTmListTest() {
		Object info =tradeMarkCaseService.statsTmagencyNameList(1, 2015, 2018);
//		Object info =tradeMarkCaseService.statsTmCustTop5List(1,1, null, null);
//		Object info =tradeMarkCaseService.statsTmagencyNameList(1, null, 2015, 2018);
//		Object info =tradeMarkCaseService.statsTmagencyNameList(1, null, 2015, 2018);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
//	@Test
	public void tradeMarkCaseAssociateTest() {
//		TradeMarkCase tradeMarkCase = new TradeMarkCase();
//		tradeMarkCase.setAgencyId(9);
		GeneralCondition gcon=getGcon("whd_wangfang");
		Object info =tradeMarkCaseService.tradeMarkCaseAssociate(77,9,1,gcon);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}

	
//	@Test
	public void queryTradeMarkCaseFileTest() {
		TradeMarkCaseFile tmcaseFile = new TradeMarkCaseFile();
		tmcaseFile.setCaseId(10);
		Object info =tradeMarkCaseFileService.queryCaseFile(tmcaseFile, null);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
//	@Test
	public void queryTradeMarkCaseForWsTest() {
		Object info =tradeMarkCaseService.queryTradeMarkCaseForWs(1);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
	
	
//	@Test
	public void queryTmcaseAppOnlineListTest() throws Exception {
		TradeMarkCase tradeMarkCase = new TradeMarkCase();
		Integer custId=1;
		Integer userId=64;
//		tradeMarkCase.setCustId(custId);
		
//		tradeMarkCase.setAppEnName("tencent");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		tradeMarkCase.setAppStartTime(sdf.parse("2017-12-20"));
//		tradeMarkCase.setAppEndTime(sdf.parse("2018-2-1"));
//		tradeMarkCase.setRegStartTime(sdf.parse("2018-1-1"));
//		tradeMarkCase.setRegEndTime(sdf.parse("2018-4-1"));
		GeneralCondition gcon = new GeneralCondition();
		GonUtil.makeOffsetAndRows(gcon);
		Token token = new Token();
//		token.setCustomerID(custId);
		token.setUser(true);
		token.setUserID(userId);
		Object info =tradeMarkCaseService.queryAppOnlineCaseList(tradeMarkCase, gcon, token);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
	
	@Test
	public void statsTmappCnNameListTest() {
//		Object info =tradeMarkCaseService.statsTmappCnNameList(1, 1, null, 2010, 2018);
//
//		if (info != null) {
//			logger.info(JsonUtil.toJson(info));
//			logger.info("queryUserList测试通过");
//		}
	}
	
	@Test
	public void queryTimeRemind(){
		/*GeneralCondition generalCondition = getGcon(username);*/
		
		
	}
	
	
	@Test
	public void TestStatsDeadLine(){
		GeneralCondition gcon = new GeneralCondition();
		try{
			Object obj = tradeMarkCaseService.statisticTmCaseDeadline(gcon,3,null);
			System.err.println(obj);
			if(obj!=null){
				System.err.println(JsonUtil.toJson(obj));
				System.err.println(obj);
			}
		}catch (Exception e) {
			
		}
	}
	/*
	@Test
	public void Testsss(){
		tradeMarkCaseMapper.selectRemindList();
		
	}*/
	
	@Test
	public void testQueryTmcaseDeadline(){
		GeneralCondition generalCondition = new GeneralCondition();
		Integer custId = null;
		Integer userId = 3;
		String message = "续展时限";
		Integer urgencyType = null;
		
		generalCondition.setOffset(0);
		generalCondition.setRows(10);
		Date limitdate = new Date();
		String create = DateTool.getDate(limitdate);
		Remind remind = new Remind();
		//remind.setLimitdate(limitdate);
		
		remind.setQcreatedate(create);
		//remind.setQlimitdate(create);
		/*generalCondition.setKeyword("2018-10-16");
		generalCondition.setOrderAsc("desc");
		generalCondition.setOrderCol("limitdate");*/
		Object object;
		try{
			
			object = tradeMarkCaseService.queryTmcaseDeadline(generalCondition, custId, userId, message, urgencyType,remind);
			if(object != null){
				logger.info(JsonUtil.toJson(object));
				logger.info("测试完毕");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void testselectRemindList(){
		GeneralCondition gcon = getGcon("whd_wangfang");
		Remind remind = new Remind();
		remind.setCustid(5);
		Object obj;
		try{
			obj = remindService.selectRemindList(remind, gcon);
			if(obj !=null){
				logger.info(JsonUtil.toJson(obj));
				logger.info("测试完毕");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void statsTmcaseProInfoTest() {
//		Object info =tradeMarkCaseService.statsTmcaseProInfo(null, 1, null);
//
//		if (info != null) {
//			logger.info(JsonUtil.toJson(info));
//			logger.info("queryUserList测试通过");
//		}
	}
	
	
//	@Test
	public void queryCaseTypeList() throws Exception {
	
	
		Integer allType=1;

		Object info =tradeMarkCaseService.queryCaseTypeList(allType);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryUserList测试通过");
		}
	}
	
	
	
//	@Test
	public void modifyGoodPlanTest() {
		GoodsPlan goodsPlan = new GoodsPlan();
		goodsPlan.setPlanId(1);
		goodsPlan.setAppName("test test");
		
		ReturnInfo rtnInfo  =tradeMarkCaseService.modifyGoodsPlan(goodsPlan);
		
	
		if (rtnInfo != null && rtnInfo.getSuccess()){								
			logger.info("modifyGoodPlan Test finish");
			
		}
	}
	
	
	@Test
	public void queryGoodPlanTest() {
		GoodsPlan goodsPlan = new GoodsPlan();
		
		goodsPlan.setAppName("达能日尔维公司");
		
		ReturnInfo rtnInfo  =tradeMarkCaseService.queryGoodsPlan(goodsPlan);
		
	
		if (rtnInfo != null && rtnInfo.getSuccess()){	
			logger.info(JsonUtil.toJson(rtnInfo));
			logger.info("queryGoodPlan Test finish");
			
		}
	}
	
	@Test
	public void queryGoodsTest() {
		GoodsPlan goodsPlan = new GoodsPlan();
		
		goodsPlan.setPlanId(2);
		ReturnInfo rtnInfo  =tradeMarkCaseService.queryGoods(goodsPlan);
		
	
		if (rtnInfo != null && rtnInfo.getSuccess()){	
			logger.info(JsonUtil.toJson(rtnInfo));
			logger.info("queryGoodPlan Test finish");
			
		}
	}
	
	
//	@Test
	public void deleteGoodPlanTest() {
		GoodsPlan goodsPlan = new GoodsPlan();
		
		goodsPlan.setPlanId(1);
		
		ReturnInfo rtnInfo  =tradeMarkCaseService.deleteGoodsPlan(goodsPlan);
		
	
		if (rtnInfo != null && rtnInfo.getSuccess()){								
			logger.info("deleteGoodPlan Test finish");
			
		}
	}
	
	
//	@Test
	public void addGoodPlanTest() {
		GoodsPlan goodsPlan = new GoodsPlan();
		
		goodsPlan.setAppName("达能日尔维公司");
		goodsPlan.setPlanName("方案2");
		
		Goods good = new Goods();
		List<Goods> goodsList = new ArrayList<Goods>();
		good.setGoodClass("1");
		good.setGoodName("碱金属");
		good.setSimilarGroup("0111");
		good.setGoodCode("123456");

		goodsList.add(good);

		goodsPlan.setGoods(goodsList);
		
		
		ReturnInfo rtnInfo  =tradeMarkCaseService.addGoodsPlan(goodsPlan);
		
	
		if (rtnInfo != null && rtnInfo.getSuccess()){								
			logger.info("addGoodPlanTest finish");
			
		}
	}
	
	
	
	
	@Test
	public void test8(){
		Arrays.asList("a","b","c").forEach(e->System.err.println(e));
		Arrays.asList("a","v","g").forEach((String e)->System.err.println(e));
		Arrays.asList("","","").forEach(e->{
			System.err.println(e);
			System.err.println(e);
		});
		
		//
		Optional<String> fullname = Optional.ofNullable(null);
		System.err.println(fullname.isPresent());
		System.out.println(fullname.orElse(""));
		
	}
	
	//创建商标异议答辩申请案件
	//@Test
	public void testcreateDissentTmCase(){
		
		TradeMarkCase tradeMarkCase = new  TradeMarkCase();
		tradeMarkCase.setCustId(3);
		tradeMarkCase.setGoodClasses("1");
		tradeMarkCase.setCaseType("异议答辩");
		tradeMarkCase.setCaseTypeId(20);
		GeneralCondition gCondition = getGcon("whd_wangfang");
		String tmNumber="11788427";
		Object info;
		try{
			info = tradeMarkCaseService.createDissentReplyEntrance(tradeMarkCase, tmNumber,gCondition);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//创建异议申请案件
	//@Test
	public void testcreateApplicantDissent(){
		
		TradeMarkCase tradeMarkCase = new TradeMarkCase();
		tradeMarkCase.setGoodClasses("1");
		tradeMarkCase.setAppCnName("匹诺曹");
		tradeMarkCase.setCustId(4);
		String tmNumber = "11788427";
		GeneralCondition gCondition = getGcon("whd_wangfang");
		Object info ;
		try{
			info = tradeMarkCaseService.createAppCase(tradeMarkCase, tmNumber, gCondition);
			if(info != null){
				logger.info(JsonUtil.toJson(info));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//
//	@Test
	public void testmodifyApplicantDissent(){
		GeneralCondition gCondition= getGcon("whd_wangfang");
		TradeMarkCase tradeMarkCase = new TradeMarkCase();
		tradeMarkCase.setId(16349);
		tradeMarkCase.setAppContactPerson("李尔");
		tradeMarkCase.setAppContactTel("010-1223123");
		tradeMarkCase.setAppContactZip("001222");
		try{
			tradeMarkCaseService.modifyApplicantDissent(tradeMarkCase, gCondition);
			logger.info("测试完毕！");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//修改异议答辩案件信息
	//@Test
	public void testmodifyDissentTmCase(){
		GeneralCondition gCondition  = getGcon("whd_wangfang");
		TradeMarkCase tradeMarkCase = new TradeMarkCase();
		tradeMarkCase.setId(16338);
		tradeMarkCase.setAgencyId(1);
		tradeMarkCase.setSupplement(1);
		tradeMarkCase.setAppContactPerson("王芳芳");
		tradeMarkCase.setDocDate(new Date());
		tradeMarkCase.setDissentAddress("北京市和平里胡同");
		tradeMarkCase.setDissentName("阿贵");
		tradeMarkCase.setImageFile("/material/3/16268/1416/4/2.jpg");
		tradeMarkCase.setAcceptPersonAddr("北京市海淀区颐园写字楼");
		tradeMarkCase.setAcceptPersonZip("102120");
		try{
			tradeMarkCaseService.modifyDissentTmCase(tradeMarkCase, gCondition);
			logger.info("测试完毕");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//
	//@Test
	public void testqueryAboutByRegnumber(){
		GeneralCondition gCondition = getGcon("whd_wangfang");
		String regNumber = "21391546";
		String goodClass = "1";
		Object info;
		try{
			info = tradeMarkCaseService.queryAboutByRegnumber(gCondition, regNumber, goodClass);	
			if(info !=null){
				logger.info(JsonUtil.toJson(info));
				logger.info("测试完毕");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	
	
	private  GeneralCondition getGcon(String username){
		GeneralCondition gcon=new GeneralCondition();
		gcon.setPageNo(1);
		gcon.setOffset(0);
		gcon.setRows(10);
		
		User user=new User();
		
		user.setUsername(username);
		user.setPassword("123456");		
		
	    String tokenID=login(user);	   
	    gcon.setTokenID(tokenID);			
	    return gcon;	
	}
	

	private String login(User user) {
		Object obj = authenticationService.login(user);				
		LoginReturnInfo rtnInfo = (LoginReturnInfo)obj;		
		String tokenID = rtnInfo.getTokenID();		
		return tokenID;
		
	}
}
