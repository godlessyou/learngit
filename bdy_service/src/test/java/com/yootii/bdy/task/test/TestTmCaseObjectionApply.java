package com.yootii.bdy.task.test;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sun.media.jfxmedia.control.VideoDataBuffer;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.LoginReturnInfo;
import com.yootii.bdy.customer.model.Customer;
import com.yootii.bdy.model.User;
import com.yootii.bdy.security.service.AuthenticationService;
import com.yootii.bdy.task.model.TmCaseTaskToDoList;
import com.yootii.bdy.task.service.TradeMarkCaseTaskService;
import com.yootii.bdy.task.service.Impl.TaskQueryServiceImpl;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseMapper;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.service.TradeMarkCaseService;
import com.yootii.bdy.util.JsonUtil;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class TestTmCaseObjectionApply {
	@Resource
	private AuthenticationService authenticationService;
	@Resource
	private TradeMarkCaseTaskService tradeMarkCaseTaskService;
	private static final Logger logger = Logger.getLogger(TestTmCaseObjectionApply.class);
	@Resource
	private TaskQueryServiceImpl taskQueryServiceImpl;
	@Resource
	private TradeMarkCaseMapper tradeMarkCaseMapper;
	@Resource
	private TradeMarkCaseService tradeMarkCaseService;
	
	//创建
	//@Test
	public void createCase(){
		TradeMarkCase tradeMarkCase = new TradeMarkCase();
		tradeMarkCase.setGoodClasses("1");
		tradeMarkCase.setAppCnName("匹诺曹");
		tradeMarkCase.setCustId(4);
		String tmNumber = "11788427";
		GeneralCondition gCondition = getGcon();
		Object info ;
		
		try{
			info = tradeMarkCaseService.createAppCase(tradeMarkCase,tmNumber,gCondition);
			if(info != null){
				logger.info(JsonUtil.toJson(info));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//启动
	//@Test
	public void strat(){
		TradeMarkCase tradeMarkCase = new TradeMarkCase();
		GeneralCondition gcon = new GeneralCondition();
		Object info;
		
		Customer customer=new Customer();
		Integer customerId=3;
				
		customer.setUserType(20);
		customer.setId(customerId);
		customer.setUsername("whd_wangfang");
		customer.setPassword("123456");		 
		String tokenID=customerLogin(customer);
		TradeMarkCase tmCase=new TradeMarkCase();
		gcon.setTokenID(tokenID);
		
		tmCase.setId(17201);
		
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
	
	//转交
	//@Test
	public void assginCase(){
		GeneralCondition gcon=getGcon();
		String userId = "3";
		String agencyId = "1";
		String transfer = "2";
		TmCaseTaskToDoList toDoList = new TmCaseTaskToDoList();
		Map<String, Object> proMap = new HashMap<>();
		
		
		toDoList.setCaseId(17201);
		toDoList.setTaskId(2947030);
		
		tradeMarkCaseTaskService.assginCase(gcon, userId, agencyId, transfer, toDoList, proMap);
		
		
	}
	
	//修改案件
	//@Test
	public void modify(){
		
		TradeMarkCase tradeMarkCase = new TradeMarkCase();
		tradeMarkCase.setAgentPerson("王芳");
		tradeMarkCase.setId(17201);
		tradeMarkCase.setAppGJdq("中国大陆");
		tradeMarkCase.setAppCnAddr("北京市海淀区");
		tradeMarkCase.setAppContactZip("123123");
		tradeMarkCase.setDissentGist("有模仿抄袭嫌疑");
		
		tradeMarkCaseMapper.updateByPrimaryKeySelective(tradeMarkCase);
		
	}
	
	//对应页面上的递交按钮
	//@Test
	public void audited(){
		Integer userId=3;
		GeneralCondition gcon=getGcon();
		
		Integer taskId=2947078;
		
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
	
	//递交成功或失败  按钮
	//@Test
	public void appOffLine(){
		Integer userId=3;
		GeneralCondition gcon = getGcon();
		
		Integer taskId=2947106;
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
	
		
		
	
	//录入补正通知官文    
	//@Test
	public void importObjectionOfficeDoc(){
		Integer userId=3;
		GeneralCondition gcon=getGcon();	  
		
		Integer taskId=2947121;
		Integer caseId=17201;
	    
		TmCaseTaskToDoList toDoList= getToDoList(taskId);
	    
	    Object info = null;	    
	    //补正通知
	    String fileName="305";
	    
	    TradeMarkCase tradeMarkCase=new TradeMarkCase();
	    String appNumber="12345678";	   
	    tradeMarkCase.setAppNumber(appNumber);
	    tradeMarkCase.setId(caseId);
		info =tradeMarkCaseTaskService.officalDoc(gcon, userId.toString(),fileName, tradeMarkCase, toDoList);		
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("officalDocTest测试通过");
		}	 
	}
	
	//这是 录入官文后 审核通过不通过 按钮  
	//审核   补正官文
	//@Test	
	public void auditOfficalDoc() {			
		Integer userId=3;		
		GeneralCondition gcon=getGcon();
		
		Integer taskId=2947134;
		
	    TmCaseTaskToDoList toDoList= getToDoList(taskId);
	    Object info = null;	    
	    
	    String fileName="305";	   
	    String auditResult="1";
	    Map<String, Object> proMap=null;
		info =tradeMarkCaseTaskService.auditOfficalDoc(gcon, userId.toString(),auditResult, toDoList, proMap);		
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("auditOfficalDocTest测试通过");
		}
	}
	
	//之后应该是        对应页面上的同意/拒绝   按钮
	//@Test	
	public void processDocTest() {			
		Integer userId=3;
		GeneralCondition gcon=getGcon();	  
		
		Integer taskId=2947152;
		
		
	    TmCaseTaskToDoList toDoList= getToDoList(taskId);
	    
	    Object info = null;	
	    //1表示直接代理人同意
	    String approved="1";
		info =tradeMarkCaseTaskService.processDoc(gcon, userId.toString(), approved, toDoList);		
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("processDoc Test测试通过");
		}		
	}
	
	//测试：提交补正材料   所谓的补正材料  其实和  前面执行的递交 差不多 ,调用的都是appOffLine()方法
	//@Test	
	public void  BzAppOffLineTest() {			
		Integer userId=3;
		GeneralCondition gcon=getGcon();
		
		Integer taskId=2935532;
		
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
	//------------------------------------------------------------------------------
	
	
	
	//  录入  342:质证 通知  
	//@Test
	public void importdoc(){
		Integer userId=3;
		GeneralCondition gcon=getGcon();	  
		
		Integer taskId=2935552;
		Integer caseId=17080;
		
		
	    TmCaseTaskToDoList toDoList= getToDoList(taskId);
	    
	    Object info = null;	    
	    String fileName="342";
	    
	    TradeMarkCase tradeMarkCase=new TradeMarkCase();
	    String appNumber="12345678";	   
	    tradeMarkCase.setAppNumber(appNumber);
	    tradeMarkCase.setId(caseId);
		info =tradeMarkCaseTaskService.officalDoc(gcon, userId.toString(),fileName, tradeMarkCase, toDoList);		
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("officalDocTest测试通过");
		}	 
	}
	
	   //审核   质证 官文
	//	@Test	
		public void auditZHIOfficalDoc() {			
			Integer userId=3;		
			GeneralCondition gcon=getGcon();
			
			Integer taskId=2935564;
			
		    TmCaseTaskToDoList toDoList= getToDoList(taskId);
		    Object info = null;	    
		    
		    String fileName="342";	   
		    String auditResult="1";
		    Map<String, Object> proMap=null;
			info =tradeMarkCaseTaskService.auditOfficalDoc(gcon, userId.toString(),auditResult, toDoList, proMap);		
			if (info != null) {
				logger.info(JsonUtil.toJson(info));
				logger.info("auditOfficalDocTest测试通过");
			}
		}
	
		//之后应该是        对应页面上的同意/拒绝   按钮
		//@Test	
		public void processDocConfirm() {			
			Integer userId=3;
			GeneralCondition gcon=getGcon();	  
			
			Integer taskId=2935596;
			
		    TmCaseTaskToDoList toDoList= getToDoList(taskId);
		    
		    Object info = null;	
		    //1表示直接代理人同意
		    String approved="1";
			info =tradeMarkCaseTaskService.processDoc(gcon, userId.toString(), approved, toDoList);		
			if (info != null) {
				logger.info(JsonUtil.toJson(info));
				logger.info("processDoc Test测试通过");
			}		
		}
		
	
//--------------------------------------------------------------------
	
	
	
	//录入     受理通知书
	//@Test
	public void importAcceptDoc(){
		Integer userId=3;
		GeneralCondition gcon=getGcon();	  
		Integer taskId=2935713;
	    TmCaseTaskToDoList toDoList= getToDoList(taskId);
	    
	    Object info = null;	    
	    String fileName="332";
	    
	    TradeMarkCase tradeMarkCase=new TradeMarkCase();
	    String appNumber="12345678";	   
	    tradeMarkCase.setAppNumber(appNumber);
	    Integer caseId=17080;
	    tradeMarkCase.setId(caseId);
		info =tradeMarkCaseTaskService.officalDoc(gcon, userId.toString(),fileName, tradeMarkCase, toDoList);		
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("officalDocTest测试通过");
		}	 
	}
	
	
	//审核受理通知书
	//@Test
	public void auditAccept(){
		Integer userId=3;		
		GeneralCondition gcon=getGcon();
		
		Integer taskId=2935836;
		
	    TmCaseTaskToDoList toDoList= getToDoList(taskId);
	    Object info = null;	    
	    
	    String fileName="332";	   
	    String auditResult="1";
	    Map<String, Object> proMap=null;
		info =tradeMarkCaseTaskService.auditOfficalDoc(gcon, userId.toString(),auditResult, toDoList, proMap);		
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("auditOfficalDocTest测试通过");
		}
	}
	
	      //之后应该是        对应页面上的同意/拒绝   按钮
		//	@Test	
			public void processDoc() {			
				Integer userId=3;
				GeneralCondition gcon=getGcon();	  
				
				Integer taskId=2935876;
				
			    TmCaseTaskToDoList toDoList= getToDoList(taskId);
			    
			    Object info = null;	
			    //1表示直接代理人同意
			    String approved="1";
				info =tradeMarkCaseTaskService.processDoc(gcon, userId.toString(), approved, toDoList);		
				if (info != null) {
					logger.info(JsonUtil.toJson(info));
					logger.info("processDoc Test测试通过");
				}		
			}
	
	//--------------------------------------------------------------
	
	
	
	
	
	
	//录入  异议审定赢  官文
	//@Test
	public void importEvidenceDoc(){
		Integer userId=3;
		GeneralCondition gcon=getGcon();	  
		Integer taskId=2935911;
	    TmCaseTaskToDoList toDoList= getToDoList(taskId);
	    
	    Object info = null;	    
	    String fileName="502";
	    
	    TradeMarkCase tradeMarkCase=new TradeMarkCase();
	    String appNumber="12345678";	   
	    tradeMarkCase.setAppNumber(appNumber);
	    Integer caseId=17080;
	    tradeMarkCase.setId(caseId);
		info =tradeMarkCaseTaskService.officalDoc(gcon, userId.toString(),fileName, tradeMarkCase, toDoList);		
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("officalDocTest测试通过");
		}	 
	}
	
	// 审核     异议_赢
	//@Test
	public void auditWin(){
		Integer userId=3;		
		GeneralCondition gcon=getGcon();
		
		Integer taskId=2936490;
		
	    TmCaseTaskToDoList toDoList= getToDoList(taskId);
	    Object info = null;	    
	    
	    String fileName="502";	   
	    String auditResult="1";
	    Map<String, Object> proMap=null;
		info =tradeMarkCaseTaskService.auditOfficalDoc(gcon, userId.toString(),auditResult, toDoList, proMap);		
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("auditOfficalDocTest测试通过");
		}
	}
	
	// 客户决定  参不参与 不予异议注册复审
	//@Test
	public void joinReject(){
		Integer userId=3;
		GeneralCondition gcon=getGcon();	  
		
		Integer taskId=2936668;
		
	    TmCaseTaskToDoList toDoList= getToDoList(taskId);
	    
	    Object info = null;	
	    //1表示直接代理人同意
	    String approved="1";
		info =tradeMarkCaseTaskService.processDoc(gcon, userId.toString(), approved, toDoList);		
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("processDoc Test测试通过");
		}	
	}
	
	
	
	//---------------------------------------------------------------
	
	
	//录入  参与不予注册复审通知书
	@Test
	public void importNotRecheckDoc(){
		
	}
	
	
	private  GeneralCondition getGcon(){
		GeneralCondition gcon=new GeneralCondition();
		gcon.setPageNo(1);
		gcon.setOffset(0);
		gcon.setRows(10);
		
		User user=new User();
		
		user.setUsername("whd_wangfang");
		user.setPassword("123456");		
		
	    String tokenID=login(user);	   
	    gcon.setTokenID(tokenID);			
	    return gcon;	
	}
	private TmCaseTaskToDoList getToDoList(Integer taskId){
		TmCaseTaskToDoList toDoList=new TmCaseTaskToDoList();		   
	    Integer caseId=1;
	    toDoList.setCaseId(caseId);
	    
	    
	    toDoList.setTaskId(taskId);
	    toDoList.setCaseType("商标注册");
	    
	    return toDoList;
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
	
}
