package com.yootii.bdy.trademark.test;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.EDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.validator.PublicClassValidator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sun.tools.javac.comp.Infer;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.LoginReturnInfo;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.model.User;
import com.yootii.bdy.security.service.AuthenticationService;
import com.yootii.bdy.solr.SolrInfo;
import com.yootii.bdy.solr.SolrSend;
import com.yootii.bdy.trademark.model.Trademark;
import com.yootii.bdy.trademark.service.TradeMarkService;
import com.yootii.bdy.util.JsonUtil;






@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class TestTrademarkService {
	@Resource
	private TradeMarkService tradeMarkService;
	@Resource
	private SolrInfo solrInfo ;
	
	@Resource
	private AuthenticationService authenticationService;
	
	private static final Logger logger = Logger.getLogger(TestTrademarkService.class);

//	@Test
	public void queryDetailTest() {
		
		GeneralCondition gcon = new GeneralCondition();
		Trademark trademark = new Trademark();
		Object info;
		try {
//			trademark.setTmId(181072);
//			info = tradeMarkService.queryTmDetail(trademark);
//			if (info != null) {
//				logger.info(JsonUtil.toJson(info));
//				logger.info("queryUserList测试通过");
//			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	
	//@Test
	public void testSolr(){
		SolrSend solrSend = new SolrSend();
		try {
			/*long count = solrSend.statisticsTmStatus(solrInfo,"status","待审中",ss);
			long count2 = solrSend.statisticsTmStatus(solrInfo, "tmStatus", "申请中",ss);*/
			/*System.out.println(count);
			System.out.println(count2);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
//	@Test
	public void statisticTmStatusTest(){
		
		GeneralCondition gcon = new GeneralCondition();
		int custId = 5;
		Integer appId = null;
		int startYear = 2014;
		int endYear  = 2018;
		int flag  = 2;
		Object info;
		try{
			info = tradeMarkService.statisticTmStatus(gcon, custId, appId, startYear, endYear, flag);
			if(info!=null){
				logger.info(JsonUtil.toJson(info));
				logger.info("查询完毕");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	@Test
	public void statisticsTmStatusDataTest(){
		
		GeneralCondition gCondition = new GeneralCondition();
		gCondition.setOffset(1);
		gCondition.setRows(9);
		int custId = 5;
		Integer appId = null;
		int startYear = 2014;
		int endYear = 2018;
		int flag = 2;
		String tmStatus = "已注册";
		//String tmStatus = "已无效";
		try{
			Object infor;
			infor=tradeMarkService.statisticTmDate(gCondition, custId, appId,tmStatus,flag,startYear, endYear);
			if(infor!=null){
				logger.info(JsonUtil.toJson(infor));
				logger.info("查询完毕");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
//	@Test
	public void statsTmDongtaiTest() {
		
		GeneralCondition gcon = new GeneralCondition();
		try {
			Object info;
			info=tradeMarkService.statsTmDongtai(1,2,2014, 2017, gcon);
			if (info != null) {
				logger.info(JsonUtil.toJson(info));
				logger.info("queryUserList测试通过");
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	//@Test
	public void statstmagentTest(){
		int custId = 16;
		GeneralCondition gcon  = new GeneralCondition();
		Integer appId = null;
		int startYear = 2014;
		int endYear = 2018;
		int flag = 2;
		
		try{
			Object info ;
			
			info = tradeMarkService.statsTmAgentList(custId, appId, startYear, endYear, gcon, flag);
			if(info!=null){
				logger.info(JsonUtil.toJson(info));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void statsTmAgentDataTest(){
		
		GeneralCondition gcon = new GeneralCondition();
		Integer custId   = 16;
		int flag = 2;
		int year = 2017;
		Integer appId = null;
		Object info;
		gcon.setOffset(0);
		gcon.setRows(10);
		try{
			info = tradeMarkService.statsTmAgentData(gcon, custId, flag, year, appId);
			if(info !=null){
				logger.info(JsonUtil.toJson(info));
				logger.info("测试成功");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void statTmStautsListTest() {
		
		GeneralCondition gcon = new GeneralCondition();
		
		Integer CustID=3;
		
//		Calendar c = Calendar.getInstance();
//		c.setTime(new Date());
//		Integer endYear = c.get(Calendar.YEAR);
		
		Integer endYear =null;
		
		try {
			Object info;
			info=tradeMarkService.statsTmStautsList(CustID, null, null, endYear, gcon);
			if (info != null) {
				logger.info(JsonUtil.toJson(info));
				logger.info("statTmStautsList测试通过");
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
//	@Test
	public void TestStatisticTmStatus(){
		GeneralCondition gCondition= new GeneralCondition();
		
		int custId = 3;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		Integer endYear = calendar.get(Calendar.YEAR);
		int startYear =2010;
		int flag = 2;
		try {
			Object info = tradeMarkService.statisticTmStatus(gCondition, custId, null, startYear, endYear,flag);
			if(info!=null)
			logger.info(JsonUtil.toJson(info));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 被驳回测试
	 */
//	@Test
	public void statsTmBeibohuiDongtaiTest(){
		
		int custId = 5;
		Integer appId =null;
		int startYear = 2010;
		int endYear = 2018;
		GeneralCondition gcon = new GeneralCondition();
		Object info;
		try {
			
			info = tradeMarkService.statsTmBeibohuiDongtai(custId, appId, startYear, endYear, gcon);
			if(info !=null){
				logger.info(JsonUtil.toJson(info));
				logger.info("测试成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//
//	@Test
	public void tmDynamicDataTest(){
		GeneralCondition gcon = new GeneralCondition();
		int custId = 5;
		int flag = 2;
		Integer appId = null;
		int year = 2015;
		int abnormalType = 0;
		Object info;
		try{
			info = tradeMarkService.tmDynamicData(gcon, custId, flag, appId, year, abnormalType);
			if(info != null){
				logger.info(JsonUtil.toJson(info));
				logger.info("测试完毕");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 被撤三
	 */
//	@Test
	public void statstmbeichesandongtaiTest(){
		
		
		int custId = 5;
		Integer appId =null;
		int startYear = 2010;
		int endYear = 2018;
		GeneralCondition gcon = new GeneralCondition();
		Object info;
		try{
			info = tradeMarkService.statsTmBeichesanDongtai(custId, appId, startYear, endYear, gcon);
			if(info != null){
				logger.info(JsonUtil.toJson(info));
				logger.info("测试成功");
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 被无效
	 */
//	@Test
	public void statstmbeiwuxiaodongtaiTest(){
		int custId = 5;
		Integer appId =null;
		int startYear = 2010;
		int endYear = 2018;
		GeneralCondition gcon = new GeneralCondition();
		Object info;
		try{
			info = tradeMarkService.statsTmBeiwuxiaoDongtai(custId, appId, startYear, endYear, gcon);
			if(info != null){
				logger.info(JsonUtil.toJson(info));
				logger.info("测试成功");
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 通用名称
	 */
//	@Test
	public void statsTmTongyongmingchengDongtaiTest(){
		int custId = 5;
		Integer appId =null;
		int startYear = 2010;
		int endYear = 2018;
		GeneralCondition gcon = new GeneralCondition();
		Object info;
		try{
			tradeMarkService.statsTmTongyongmingchengDongtai(custId, appId, startYear, endYear, gcon);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 异议申请
	 */
//	@Test
	public void statstmbeiyiyidongtaiTest(){
		int custId = 3;
		Integer appId =null;
		int startYear = 2010;
		int endYear = 2018;
		GeneralCondition gcon = new GeneralCondition();
		Object info;
		try{
			info = tradeMarkService.statsTmBeiyiyiDongtai(custId, appId, startYear, endYear, gcon);
			if(info != null){
				logger.info(JsonUtil.toJson(info));
				logger.info("测试完成");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
//	@Test
	public void statisticBianGeng(){
		GeneralCondition gCondition= new GeneralCondition();
		int custId = 10;
		Integer appId = null;
		int startYear = 2014;
		int endYear = 2018;
		
		Object info ;
		try{
			info = tradeMarkService.statsTmBiangengDongtai(custId, appId, startYear, endYear, gCondition);
			if(info!=null){
				logger.info(JsonUtil.toJson(info));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	@Test
	public void statsTmNameListTest(){
		GeneralCondition gCondition= new GeneralCondition();
		int custId = 10;
		Integer appId = null;
		int startYear = 2014;
		int endYear = 2018;
		
		Object info ;
		try{
			info = tradeMarkService.statsTmNameList(custId, appId, startYear, endYear, gCondition,2);
			if(info!=null){
				logger.info(JsonUtil.toJson(info));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	//更新solr
//	@Test
	public void updateTmProcessSolrDateTest(){
		
		try{
			ReturnInfo obj =  tradeMarkService.updateTmProcessSolrDate();
			logger.info(obj.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	//
//	@Test
	public void testStatisByTmProcess(){
		
		Integer custId = 8;
		Integer appId = null;
		Integer startYear = 2013;
		Integer endYear = 2016;
		Integer status = 1;
		int flag = 0;
		GeneralCondition gcon = new GeneralCondition();
		Object info ;
		
		try{
			info = tradeMarkService.statisByTmProcess(custId, appId, startYear, endYear, gcon,status,flag);
			if(info !=null){
				logger.info(JsonUtil.toJson(info));
				logger.info("测试完毕");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//@Test
	public void statsTmTypeListTest(){
		
		int custId = 3;
		Integer appId = null;
		int flag =2;
		Integer startYear = 2014;
		Integer endYear = 2018;
		GeneralCondition gcon = new GeneralCondition();
		Object info ;
		try{
			info = tradeMarkService.statsTmTypeList(custId, appId, startYear, endYear, gcon, flag);
			if(info != null){
				logger.info(JsonUtil.toJson(info));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
//	@Test
	public void queryTmChangeListTest() {
		
		GeneralCondition gcon = new GeneralCondition();
		
		Object info;
		try {
			
			Integer appId=15;
			Integer national=1;
			Trademark trademark=new Trademark();	
			
			Integer caseTypeId=5;
//			trademark.setStatus("已驳回");
			
			String addressList="广东省深圳市宝安区石岩镇上屋村狮子山#中国广东省深圳市宝安区石岩镇上屋村狮子山";
		
			info = tradeMarkService.queryTmChangeList(gcon, trademark, appId, national, addressList);
			if (info != null) {
				logger.info(JsonUtil.toJson(info));
				logger.info("queryTmChangeList测试通过");
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	
	
	@Test
	public void queryCanProcessTmTest() {
		
		GeneralCondition gcon = new GeneralCondition();
		gcon.setOffset(0);
		gcon.setRows(1000);
	
//		gcon.setKeyword("10022004");
	
		Integer CustID=3;
		Integer appId=null;
		appId=15;
		
		Trademark trademark=new Trademark();
		
		
		Integer caseTypeId=5;
		
		String addressList="广东省深圳市宝安区石岩镇上屋村狮子山#中国广东省深圳市宝安区石岩镇上屋村狮子山";
		
		
		try {
			Object info;
			info=tradeMarkService.queryCanProcessTm(null, null, gcon, trademark, null, null, CustID, appId, 0, caseTypeId,addressList);
			if (info != null) {
				logger.info(JsonUtil.toJson(info));
				logger.info("queryCanProcessTm测试通过");
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	
//	@Test
	public void queryTmListTest() {
		
		GeneralCondition gcon = new GeneralCondition();
//		gcon.setKeyword("10022004");
		Integer CustID=null;
		 CustID=3;
		Integer appId=null;
		appId=16;
				
		Trademark trademark=new Trademark();
//		trademark.setApplicantName("达能日尔维公司");
		
		trademark.setStatus("已驳回");
		
		try {
			Object info;
			String addressList="法国，巴黎75009．奥斯曼大街17号#法国巴黎75009奥斯曼大街17号";
			info=tradeMarkService.queryTmList(null, null, gcon, trademark, null, null, CustID, appId, 0, addressList);
			if (info != null) {
				logger.info(JsonUtil.toJson(info));
				logger.info("queryTmListTest测试通过");
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	
	
//	@Test
	public void queryAddressListTest() {
		
		GeneralCondition gcon = new GeneralCondition();
	
				
		Trademark trademark=new Trademark();
		trademark.setApplicantName("达能日尔维公司");
		
		
		try {
			Object info;
		
			info=tradeMarkService.queryAddressList(trademark);
			if (info != null) {
				logger.info(JsonUtil.toJson(info));
				logger.info("queryAddressListTest测试通过");
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void statsTmAppnameListtTest() {
		
		GeneralCondition gcon = new GeneralCondition();
		Integer regFlag=1;
		Integer CustID=76;
		Integer flag=0;
		try {
			Object info;
			info=tradeMarkService.statsTmAppnameList(CustID,1971, 2018, gcon, regFlag,flag);
			if (info != null) {
				logger.info(JsonUtil.toJson(info));
				logger.info("statsTmAppnameListtTest测试通过");
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void queryTrademarkbyRenumberTest() {
		
		GeneralCondition gcon = getGcon("whd_wangfang");
		Trademark trademark=new Trademark();
		trademark.setRegNumber("75562");
		try {
			Object info;
			info=tradeMarkService.queryTrademarkbyRenumber(trademark, gcon);
			if (info != null) {
				logger.info(JsonUtil.toJson(info));
				logger.info("statsTmAppnameListtTest测试通过");
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
//	@Test
	public void updateSolrDatTest() {
		
		GeneralCondition gcon = new GeneralCondition();
		try {
			Object info;
			info=tradeMarkService.updateSolrDate();
			if (info != null) {
				logger.info(JsonUtil.toJson(info));
				logger.info("updateSolrDatTest测试通过");
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	//
	//@Test
	public void updateTmNotificationSolrDateTest(){
		
		Object info;
		try{
			SolrInfo solrInfo = new SolrInfo();
			solrInfo.setSolrHome("http://localhost:8983/solr/");
			info = tradeMarkService.updateTmNotificationSolrDate(solrInfo);
			if(info!=null){
				logger.info(JsonUtil.toJson(info));
				logger.info("测试通过");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	//@Test
	public void queryTrademarkBySolrTest(){
		Integer custId = 3;
		Integer appId = 2;
		Integer dateType = null;
		Trademark trademark = new Trademark();
		trademark.setTmStatus("已注册");;
		String startYear = "2014-09-18";
		String endYear = "2018-09-18";
		GeneralCondition gcon= new GeneralCondition();
		try{
			Object info ;
			info = tradeMarkService.queryTrademarkBySolr(custId, appId, dateType, trademark, startYear, endYear, gcon, true,2);
			if(info!=null){
				logger.info(JsonUtil.toJson(info));
				logger.info("测试通过");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//根据名称来查询数据
	@Test
	public void queryTrademarkBySolrOfTmName(){
		
		GeneralCondition gcon  = new GeneralCondition();
		Object info;
		int custId = 5;
		Integer appId = null;
		int dateType = 0;
		Trademark trademark = new Trademark();
//		trademark.setTmName("DANCING ON THE MOON");
		String startYear = null;
		String endYear = null;
//		startYear = "2014";
//		endYear = "2018";
		
		trademark.setApplicantName("法国");
		
		try{
//			info = tradeMarkService.queryTrademarkBySolr(custId, appId, dateType, trademark, startYear, endYear, gcon, false,0);
//			if(info!=null){
//				logger.info(JsonUtil.toJson(info));
//				logger.info("测试完毕");
//			}
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
