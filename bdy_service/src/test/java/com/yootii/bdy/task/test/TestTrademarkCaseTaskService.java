package com.yootii.bdy.task.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.LoginReturnInfo;
import com.yootii.bdy.customer.model.Customer;
import com.yootii.bdy.model.User;
import com.yootii.bdy.security.service.AuthenticationService;
import com.yootii.bdy.task.model.TaskCondition;
import com.yootii.bdy.task.model.TaskRecord;
import com.yootii.bdy.task.model.TmCaseTaskToDoList;
import com.yootii.bdy.task.model.ToDoList;
import com.yootii.bdy.task.service.TradeMarkCaseTaskService;
import com.yootii.bdy.task.service.Impl.TaskCommonImpl;
import com.yootii.bdy.task.service.Impl.TaskQueryServiceImpl;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.util.DateTool;
import com.yootii.bdy.util.JsonUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class TestTrademarkCaseTaskService {
	
	@Resource
	private AuthenticationService authenticationService;
	@Resource
	private TaskCommonImpl taskCommonImpl;
	
	@Resource
	private TradeMarkCaseTaskService tradeMarkCaseTaskService;
	private static final Logger logger = Logger.getLogger(TestTrademarkCaseTaskService.class);
	@Resource
	private TaskQueryServiceImpl taskQueryServiceImpl;
	
	
	//测试 手工错误处理任务接口
	@Test 
	public void runAgainTest() {		
		GeneralCondition gcon = getGcon();			
	
        Integer taskId=3205222;
        
        Integer userId=1;
        
        TmCaseTaskToDoList toDoList= new TmCaseTaskToDoList();
        
        toDoList.setTaskId(taskId);
        
		Object info =tradeMarkCaseTaskService.runAgain(gcon, userId.toString(), toDoList);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("testCreateRejectCase测试通过");
		}	
	}
	
//	@Test
	public void testStartTmRegisterProcess() {		
		GeneralCondition gcon = new GeneralCondition();		
		Customer customer=new Customer();
		Integer customerId=3;
				
		customer.setUserType(20);
		customer.setId(customerId);
		customer.setUsername("taikang");
		customer.setPassword("123456");		
//		customer.setUsername("wangwang");
//		customer.setPassword("123456");		
		TradeMarkCase tmCase = new TradeMarkCase();
		
		Integer caseId=8;
		Integer agencyId=1;
		tmCase.setId(caseId);
		tmCase.setAgencyId(agencyId);
					
		String tokenID=customerLogin(customer);
		gcon.setTokenID(tokenID);
		String agencyServiceId="1";		
		int serviceId=9;
		String userId=null;
		Map<String, Object> map=new HashMap<String, Object>();
        map.put("gcon", gcon);
        map.put("userId", userId);
        map.put("customerId", customerId);
        map.put("agencyServiceId", agencyServiceId);
        map.put("serviceId", serviceId);
        map.put("tmCase", tmCase);
				
		Object info =tradeMarkCaseTaskService.startTmRegisterProcess(map);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("startTmRegisterProcess测试通过");
		}	
	}
	
	
	
	@Test
	public void queryMailRecordTest() {
		String customerId=null;
		//测试代理人的待办事项
		String userId="26";
//		GeneralCondition gcon=getGcon();	
		//测试客户的待办事项
//		customerId="5";
//		GeneralCondition gcon=getCustomerGcon();
		Integer caseId=4;
		TmCaseTaskToDoList toDoList= new TmCaseTaskToDoList();
		toDoList.setCaseId(caseId);
//		Object info = tradeMarkCaseTaskService.queryMailRecord(toDoList);
//		if (info != null) {
//			logger.info(JsonUtil.toJson(info));
//			logger.info("queryMailRecordTest测试通过");
//		}		
	}
	
	
	@Test
	public void queryCaseDaibanTest() {
		String customerId=null;
		//测试代理人的待办事项
		String userId="3";
//		String password="123456";
		//GeneralCondition gcon=getGcon(userId, password);
		GeneralCondition gcon = new GeneralCondition();
		int pageId = 6;
		//测试客户的待办事项
//		customerId="5";
//		GeneralCondition gcon=getCustomerGcon();
		TradeMarkCase tmcase = new TradeMarkCase();
		tmcase.setId(23546);
//		tmcase.setCaseTypeId(1);
//		tmcase.setAppCnName("莱雅公司");
		
		ToDoList toDoList=new ToDoList();
		
		toDoList.setTaskId(3212572);
		toDoList.setTaskName("案件分配");
  

		Object info = tradeMarkCaseTaskService.queryCaseDaiban(userId, customerId, pageId, tmcase, toDoList);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryCaseDaiban测试通过");
		}		
	}
		

	
	@Test
	public void queryToDoListTest() {
		String customerId=null;
		String userId=null;
		//测试代理人的待办事项
		userId="1";
		
		String password="123456";
		GeneralCondition gcon=getGcon(userId, password);
//		GeneralCondition gcon = new GeneralCondition();
		//String docDate = "2018-10-15";
		Integer pageId=null;
		//pageId = 10;
		//gcon.setKeyword("系统");
		//gcon.setDocType(303);
		//测试客户的待办事项
//		customerId="1";
//		GeneralCondition gcon=getCustomerGcon();
		TradeMarkCase tmcase = new TradeMarkCase();
//		tmcase.setCaseTypeId(1);
//		tmcase.setAppCnName("莱雅公司");
		//gcon.setDocDate(docDate);
		//gcon.setFlag(1);
		//gcon.setKeyword("2019-04-16");
		gcon.setOffset(0);
		gcon.setRows(10);
		Object info = tradeMarkCaseTaskService.queryToDoList(gcon, userId, customerId,pageId,tmcase);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryToDoList测试通过");
		}		
	}
	
	//
//	@Test
	public void findUserTaskTest(){
		String userId = "3";
		Integer pageId = 10;
		Object info;
		try{
			info = taskCommonImpl.findUserTask(userId, pageId);
			if(info!=null){
				logger.info(JsonUtil.toJson(info));
				logger.info("测试成功");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
//	@Test
	public void toDoListDetailTest() {
		Integer userId=12;
		GeneralCondition gcon=getGcon();	
		
		String taskId=null;
//	    taskId="2087568";
	    Integer caseId=510;
	    TmCaseTaskToDoList tmCaseTaskToDoList=new TmCaseTaskToDoList();
	    
	    tmCaseTaskToDoList.setCaseId(caseId);
	    
		Object info = tradeMarkCaseTaskService.toDoListDetail(gcon, null, userId.toString(), taskId, tmCaseTaskToDoList);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("toDoListDetailTest测试通过");
		}
	}
	
	//
//	@Test
	public void queryWaitAuditTmCaseTest(){
		
		GeneralCondition gcon = new GeneralCondition();
		String userId = "3";
		String customerId =null;
		Integer pageId = 10;
		TradeMarkCase tmcase = new TradeMarkCase();
		gcon.setOffset(0);
		gcon.setRows(10);
		gcon.setKeyword("不予受理通知");
		Object info ;
		
		try{
			info = taskQueryServiceImpl.queryWaitAuditTmCase(gcon, userId, customerId, pageId, tmcase);
			if(info !=null){
				logger.info(JsonUtil.toJson(info));
				logger.info("测试完毕");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	//审核不通过测试
//	@Test
	public void notAuditedTest() {		
		Integer userId=24;
		GeneralCondition gcon=getGcon();	   
		Integer taskId=1470078;
	    TmCaseTaskToDoList toDoList= getToDoList(taskId);
	    toDoList.setRemarks("审核通过");		
	    Object info = null;
		
	    info =tradeMarkCaseTaskService.notAudited(gcon, userId.toString(), toDoList);		
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("notAudited测试通过");
		}
	}
	
	
	//审核通过测试
//	@Test
	public void auditedTest() {		
		Integer userId=3;
		GeneralCondition gcon=getGcon();	   
		Integer taskId=1987572;
	    TmCaseTaskToDoList toDoList= getToDoList(taskId);
	    toDoList.setRemarks("审核通过");		
	    Object info = null;
	    
	    String submitMode="2";
	    Map<String, Object> proMap=null;
	    info =tradeMarkCaseTaskService.audited(gcon, userId.toString(), submitMode, toDoList,false, proMap);		

		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("audited测试通过");
		}
	}
	
	
	
	//客户确认案件信息测试：同意
//	@Test	
//	public void customerDecideCaseInfoTest() {	
//		GeneralCondition gcon=getGcon();	
//		
//		Integer taskId=1627602;
//	    TmCaseTaskToDoList toDoList= getToDoList(taskId);
//	    
//	    toDoList.setRemarks("同意");
//	    
//	    Integer custId=5;
//	    String customerId=custId.toString();
//	    
//	    Object info = null;	    
//	    //受理通知	   
//		info =tradeMarkCaseTaskService.agree(gcon, customerId, toDoList);
//		if (info != null) {
//			logger.info(JsonUtil.toJson(info));
//			logger.info("officalDocTest测试通过");
//		}		
//	
//	}
	
		
	
	//递交申请测试
//	@Test	
//	public void submitAppTest() {	
//		Integer userId=17;
//		
//		GeneralCondition gcon=getGcon();	
//		Integer taskId=1850012;
//	    TmCaseTaskToDoList toDoList= getToDoList(taskId);
//		
//	    Object info = null;
//	    String submitMode="2";
	    
//		info =tradeMarkCaseTaskService.submitApp(gcon, userId.toString(),submitMode, toDoList);		
//		if (info != null) {
//			logger.info(JsonUtil.toJson(info));
//			logger.info("submitApp测试通过");
//		}
//	}
	
	
	
	//直接递交申请测试
//	@Test	
	public void appOffLineTest() {		
		Integer userId=3;
		GeneralCondition gcon=getGcon();	 
		Integer taskId=2270245;
	    TmCaseTaskToDoList toDoList= getToDoList(taskId);
		
	    Object info = null;
	    String submitStatus="1";	    
	    Map<String, Object> proMap=null;
		info =tradeMarkCaseTaskService.appOffLine(gcon, userId.toString(),submitStatus, toDoList, proMap);		
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("appOffLine测试通过");
		}
	}
	
	
	//录入形式审查结果官文测试 -补正通知
//	@Test	
	public void bzDocTest() {			
		Integer userId=3;
//		Integer userId=29; //该用户具备递交官文的权限。
		GeneralCondition gcon=getGcon();	  
		Integer taskId=2005013;
	    TmCaseTaskToDoList toDoList= getToDoList(taskId);
	    
	    Object info = null;	    
	    //补正通知
	    String fileName="305";
	    
	    TradeMarkCase tradeMarkCase=new TradeMarkCase();
	    String appNumber="12345678";	   
	    tradeMarkCase.setAppNumber(appNumber);
	    Integer caseId=126;
	    tradeMarkCase.setId(caseId);
		info =tradeMarkCaseTaskService.officalDoc(gcon, userId.toString(),fileName, tradeMarkCase, toDoList);		
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("officalDocTest测试通过");
		}	 
	}
	
	
	
	//审核形式审查结果官文的测试-补正通知
//	@Test	
	public void auditBzDocTest() {			
		Integer userId=3;		
		GeneralCondition gcon=getGcon();	  
		Integer taskId=2005030;
	    TmCaseTaskToDoList toDoList= getToDoList(taskId);
	    
	    Object info = null;	    
	    //补正通知
	    String fileName="305";	
	    String auditResult="1";
	    Map<String, Object> proMap=null;
		info =tradeMarkCaseTaskService.auditOfficalDoc(gcon, userId.toString(),auditResult, toDoList, proMap);		
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("auditOfficalDocTest测试通过");
		}
	}
	
	
	
	//处理通知测试：向用户反馈官方通知
//	@Test	
	public void processDocTest() {			
		Integer userId=3;
		
		GeneralCondition gcon=getGcon();	  
		Integer taskId=2005044;
	    TmCaseTaskToDoList toDoList= getToDoList(taskId);
	    
	    Object info = null;	    
	   
	   
	    String approved="3";
	    
		info =tradeMarkCaseTaskService.processDoc(gcon, userId.toString(), approved, toDoList);		
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("processDoc Test测试通过");
		}		
	
	}
	
	
	//客户决定测试：同意
//	@Test	
	public void agreeTest() {			
	
		GeneralCondition gcon=getGcon();	
		
		Integer taskId=2005060;
	    TmCaseTaskToDoList toDoList= getToDoList(taskId);
	    
	    toDoList.setRemarks("同意");
	    
	    Integer custId=1;
	    String customerId=custId.toString();
	    
	    Object info = null;	    
	    //受理通知
	    String fileName="305";	   
		info =tradeMarkCaseTaskService.agree(gcon, customerId, toDoList);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("officalDocTest测试通过");
		}		
	
	}
	
	
	
	
	//测试：提交补正材料
//	@Test	
	public void  BzAppOffLineTest() {			
	
		Integer userId=3;
		GeneralCondition gcon=getGcon();	 
		Integer taskId=2005066;
	    TmCaseTaskToDoList toDoList= getToDoList(taskId);
		
	    Object info = null;
	    String submitStatus="1";	    
	    Map<String, Object> proMap=null;
		info =tradeMarkCaseTaskService.appOffLine(gcon, userId.toString(),submitStatus, toDoList, proMap);		
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("BzAppOffLineTest测试通过");
		}
	
	}
	
	
	
	//录入形式审查结果官文测试 -受理通知
//	@Test	
	public void stDocTest() {			
		Integer userId=3;
//		Integer userId=29; //该用户具备递交官文的权限。
		GeneralCondition gcon=getGcon();	  
		Integer taskId=2617521;
	    TmCaseTaskToDoList toDoList= getToDoList(taskId);
	    
	    TradeMarkCase tradeMarkCase=new TradeMarkCase();
	    
	    String appNumber="888999";
	    String dateString="2018-09-01";
	    Date appDate=DateTool.StringToDate(dateString);
	    tradeMarkCase.setAppNumber(appNumber);
	    tradeMarkCase.setAppDate(appDate);
	    Integer caseId=4346;
	    tradeMarkCase.setId(caseId);
	    Date docDate=new Date();
	    tradeMarkCase.setDocDate(docDate);
	    Object info = null;	    
	    //受理通知
	    String fileName="332";	   
		info =tradeMarkCaseTaskService.officalDoc(gcon, userId.toString(),fileName, tradeMarkCase, toDoList);		
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("officalDocTest测试通过");
		}	
	}
	
	
	
	
	//审核形式审查结果官文的测试-审核受理通知
//	@Test	
	public void auditStDocTest() {			
		Integer userId=3;		
		GeneralCondition gcon=getGcon();	  
		Integer taskId=2005105;
	    TmCaseTaskToDoList toDoList= getToDoList(taskId);
	    
	    Object info = null;	    
	    //受理通知
	    String fileName="332";	
	    String auditResult="1";
	    Map<String, Object> proMap=null;
		info =tradeMarkCaseTaskService.auditOfficalDoc(gcon, userId.toString(),auditResult, toDoList, proMap);		
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("auditOfficalDocTest测试通过");
		}
	}
	
	
	
	
	
	//录入实质审查结果官文测试 -初步审定公告
//	@Test	
	public void csDocTest() {			
		Integer userId=3;
//		Integer userId=29; //该用户具备递交官文的权限。
		GeneralCondition gcon=getGcon();	  
		Integer taskId=2005114;
	    TmCaseTaskToDoList toDoList= getToDoList(taskId);
	    
	    Object info = null;	    	   	
	
		//初步审定公告
	    String fileName="316";	 
	    TradeMarkCase tradeMarkCase=new TradeMarkCase();
	    Integer caseId=126;
	    tradeMarkCase.setId(caseId);
	    String approvalNumber="1773";	   
	    Date approvalDate=new Date();
	    tradeMarkCase.setApprovalNumber(approvalNumber);
	    tradeMarkCase.setApprovalDate(approvalDate);
	    info =tradeMarkCaseTaskService.officalDoc(gcon, userId.toString(),fileName, tradeMarkCase, toDoList);		
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("officalDocTest测试通过");
		}
		
	}
	
	
	
	
	
	//审核实质审查结果官文测试-审核初步审定公告
//	@Test	
	public void auditCsDocTest() {			
		Integer userId=3;		
		GeneralCondition gcon=getGcon();	  
		Integer taskId=2012525;
	    TmCaseTaskToDoList toDoList= getToDoList(taskId);
	    
	    Object info = null;	    
	    //受理通知
	    String fileName="316";	 
	    String auditResult="1";
	    Map<String, Object> proMap=null;
		info =tradeMarkCaseTaskService.auditOfficalDoc(gcon, userId.toString(),auditResult, toDoList, proMap);		
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("auditOfficalDocTest测试通过");
		}
	}
		
	
	
	
	
	//录入异议期的官文测试 -注册公告
//	@Test	
	public void regDocTest() {			
		Integer userId=3;
//		Integer userId=29; //该用户具备递交官文的权限。
		GeneralCondition gcon=getGcon();	  
		Integer taskId=2572568;
	    TmCaseTaskToDoList toDoList= getToDoList(taskId);
	    
	    Object info = null;	    
	  		
		//注册公告
	    String  fileName="320";	   
	    TradeMarkCase tradeMarkCase=new TradeMarkCase();
	    String regNumber="12345";	
	    String regNoticeNumber="1881";
	    Date regDate=new Date();
	    Integer caseId=126;
	    tradeMarkCase.setId(caseId);
	    tradeMarkCase.setRegNumber(regNumber);
	    tradeMarkCase.setRegNoticeNumber(regNoticeNumber);	   
	    tradeMarkCase.setRegDate(regDate);
	    String dateString="2018-05-01";
	    Date validStartDate=DateTool.StringToDate(dateString);
	    tradeMarkCase.setValidStartDate(validStartDate);
	    dateString="2028-04-30";
	    Date validEndDate=DateTool.StringToDate(dateString);
	    tradeMarkCase.setValidEndDate(validEndDate);	  
	    Date docDate=new Date();
	    tradeMarkCase.setDocDate(docDate);
	    
	    info =tradeMarkCaseTaskService.officalDoc(gcon, userId.toString(),fileName,tradeMarkCase, toDoList);		
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("officalDocTest测试通过");
		}
		
	}
	
	

	//审核异议期的官文测试-注册公告
	@Test	
	public void auditRegDocTest() {			
		Integer userId=3;		
		GeneralCondition gcon=getGcon();	  
		Integer taskId=2575002;
	    TmCaseTaskToDoList toDoList= getToDoList(taskId);
	    
	    Object info = null;	    
	    
	    String fileName="320";	   
	    String auditResult="1";
	    Map<String, Object> proMap=null;
		info =tradeMarkCaseTaskService.auditOfficalDoc(gcon, userId.toString(),auditResult, toDoList, proMap);		
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("auditOfficalDocTest测试通过");
		}
	}
	
	
	
	
	
	
	
//	@Test	
	public void closeCaseTest() {			
		Integer userId=17;
		
		GeneralCondition gcon=getGcon();	  
		Integer taskId=1560177;
	    TmCaseTaskToDoList toDoList= getToDoList(taskId);
	    
	    Object info = null;	    
	    //受理通知
	    String fileName="320";	   
		info =tradeMarkCaseTaskService.feedback(gcon, userId.toString(), toDoList);		
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("officalDocTest测试通过");
		}		
	
	}
	
	
	
	
	
//	@Test
	public void createTmcaseTaskTest() {
		TaskRecord record = new TaskRecord();
		record.setCaseId(1);
		
		Object info =tradeMarkCaseTaskService.createTaskRecord(record);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("createTradeMarkCaseTask测试通过");
		}
	}
	
	
//	@Test
	public void modifyTradeMarkCaseTaskTest() {
		TaskRecord record = new TaskRecord();
		record.setId(1);
		record.setTaskName("提交商标注册申请");
	
		Object info =tradeMarkCaseTaskService.modifyTaskRecord(record);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("modifyTradeMarkCaseTask测试通过");
		}
	}
	
	
	
//	@Test
	public void statsByAgencyTest() {
		String customerId=null;
		//测试代理人的待办事项
		String userId="28";
		String username="luodeman";
		GeneralCondition gcon=getGcon(username);	
		//测试客户的待办事项
//		customerId="5";
//		GeneralCondition gcon=getCustomerGcon();
  
		Integer agencyId=13;
		Object info = tradeMarkCaseTaskService.statsByAgency(agencyId, gcon);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("statsByAgencyTest测试通过");
		}		
	}
	
	

	private String login(User user) {
		Object obj = authenticationService.login(user);				
		LoginReturnInfo rtnInfo = (LoginReturnInfo)obj;		
		String tokenID = rtnInfo.getTokenID();		
		return tokenID;
		
	}
	
	
	private String customerLogin(Customer customer) {		
		Object obj = authenticationService.customerin(customer);				
		LoginReturnInfo rtnInfo = (LoginReturnInfo)obj;		
		String tokenID = rtnInfo.getTokenID();		
		return tokenID;		
	}
	
	
	private TmCaseTaskToDoList getToDoList(Integer taskId){
		TmCaseTaskToDoList toDoList=new TmCaseTaskToDoList();		   
	    Integer caseId=1;
	    toDoList.setCaseId(caseId);
	    
	    
	    toDoList.setTaskId(taskId);
	    toDoList.setCaseType("商标注册");
	    
	    return toDoList;
	}
	
	
	private  GeneralCondition getGcon(){
		GeneralCondition gcon=new GeneralCondition();
		gcon.setPageNo(1);
		gcon.setOffset(0);
		gcon.setRows(10);
		
		User user=new User();
		
		user.setUsername("yd_lina");
		user.setPassword("123456");		
		
	    String tokenID=login(user);	   
	    gcon.setTokenID(tokenID);			
	    return gcon;	
	}
	
	private  GeneralCondition getGcon(String userName){
		GeneralCondition gcon=new GeneralCondition();
		gcon.setPageNo(1);
		gcon.setOffset(0);
		gcon.setRows(10);
		
		User user=new User();
		
		user.setUsername(userName);
		user.setPassword("123456");		
		
	    String tokenID=login(user);	   
	    gcon.setTokenID(tokenID);			
	    return gcon;	
	}
	
	
	private  GeneralCondition getGcon(String userName,String password){
		GeneralCondition gcon=new GeneralCondition();
		gcon.setPageNo(1);
		gcon.setOffset(0);
		gcon.setRows(10);
		
		User user=new User();
		
		user.setUsername(userName);
		user.setPassword(password);		
		
	    String tokenID=login(user);	   
	    gcon.setTokenID(tokenID);			
	    return gcon;	
	}
	
	
	private  GeneralCondition getCustomerGcon(){
		GeneralCondition gcon=new GeneralCondition();
		gcon.setPageNo(1);
		gcon.setOffset(0);
		gcon.setRows(10);
		
		Customer customer=new Customer();
		
		customer.setUsername("daneng");
		customer.setPassword("123456");		
		
	    String tokenID=customerLogin(customer);	   
	    gcon.setTokenID(tokenID);			
	    return gcon;	
	}
	
	
}
