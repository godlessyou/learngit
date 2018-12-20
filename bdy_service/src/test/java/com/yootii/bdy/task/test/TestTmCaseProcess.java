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
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.customer.model.Customer;
import com.yootii.bdy.ipservice.model.AgencyService;
import com.yootii.bdy.ipservice.service.AgencyServiceService;
import com.yootii.bdy.model.User;
import com.yootii.bdy.process.service.ProcessService;
import com.yootii.bdy.security.service.AuthenticationService;
import com.yootii.bdy.task.model.TaskCondition;
import com.yootii.bdy.task.model.TaskRecord;
import com.yootii.bdy.task.model.TmCaseTaskToDoList;
import com.yootii.bdy.task.service.TradeMarkCaseTaskService;
import com.yootii.bdy.task.service.Impl.AgencyTaskServiceImpl;
import com.yootii.bdy.task.service.Impl.CustomerTaskServiceImpl;
import com.yootii.bdy.task.service.Impl.MultiProcessServiceImpl;
import com.yootii.bdy.task.service.Impl.TaskCommonImpl;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseMapper;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.TradeMarkCaseCategory;
import com.yootii.bdy.tmcase.model.TradeMarkCaseJoinApp;
import com.yootii.bdy.tmcase.model.TradeMarkCaseRecord;
import com.yootii.bdy.tmcase.service.GuanWenService;
import com.yootii.bdy.tmcase.service.TradeMarkCaseRecordService;
import com.yootii.bdy.tmcase.service.TradeMarkCaseService;
import com.yootii.bdy.trademark.model.Trademark;
import com.yootii.bdy.util.GraspUtil;
import com.yootii.bdy.util.JsonUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class TestTmCaseProcess {
	
	@Resource
	private GuanWenService guanWenService;
	
	@Resource
	private AuthenticationService authenticationService;
	
	
	
	@Resource
	private TradeMarkCaseTaskService tradeMarkCaseTaskService;
	private static final Logger logger = Logger.getLogger(TestTmCaseProcess.class);
	
	@Resource
	private CustomerTaskServiceImpl customerTaskServiceImpl;
	
	@Resource
	private TradeMarkCaseRecordService tradeMarkCaseRecordService;
	
	@Resource
	private AgencyServiceService agencyServiceService;
	
	@Resource
	private TradeMarkCaseMapper tradeMarkCaseMapper;
	
	@Resource
	private TradeMarkCaseService tradeMarkCaseService;
	
	@Resource
	private ProcessService processService;
	
	@Resource
	private TaskCommonImpl taskCommonImpl;
	
	@Resource
	private MultiProcessServiceImpl multiProcessServiceImpl;
	
	@Resource
	private AgencyTaskServiceImpl agencyTaskServiceImpl;
	
	

	
	
	//测试 创建驳回复审案件的接口
//	@Test 
	public void testCreateRejectCase() {		
		GeneralCondition gcon = getGcon();		
		
	
		TradeMarkCase tmCase = new TradeMarkCase();
		
		Integer caseId=16162;
		Integer custId=1;
		Integer agencyId=1;
		tmCase.setId(caseId);
		tmCase.setCustId(custId);
		tmCase.setAgencyId(agencyId);
				
		String userId="3";		
        
        String processInstanceId="2668118";
        
		Object info =multiProcessServiceImpl.createRejectCase(gcon, userId, processInstanceId, tmCase);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("testCreateRejectCase测试通过");
		}	
	}
	
	
	
	//测试 通知驳回复审案件结果的接口
//	@Test 
	public void notifyChildCaseResultTest() {		
		GeneralCondition gcon = getGcon();	
		
		Integer caseId=16351;
        
        String processInstanceId="2810219";
        
		Object info =multiProcessServiceImpl.notifyChildCaseResult(gcon, caseId.toString(), processInstanceId);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("testCreateRejectCase测试通过");
		}	
	}
	
	
	
	
//	@Test
	public void dataSynTest() {		
		GeneralCondition gcon = getGcon();	
		Integer caseId=15803;
		Integer agencyId=1;
		Integer type=1;
		
		try {
			taskCommonImpl.dataSyn(gcon, agencyId, caseId, type);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
//	@Test
	public void querytaskpropertyTest() {
		
	
		//调用修改案件接口
		List<Map<String, Object>> list;
		
		String caseIds="5637,5638";
		String caseTypeIds="1";
		try {
			list = processService.queryTaskProperty(caseIds,caseTypeIds);
			if (list != null) {
				logger.info(JsonUtil.toJson(list));
				logger.info("querytaskpropertyTest测试通过");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
//	@Test
	public void guanWenServiceTest() {
		
	
		//调用修改案件接口
		ReturnInfo rtnInfo = (ReturnInfo)guanWenService.processGuanWen();
		if (rtnInfo != null) {
			logger.info(JsonUtil.toJson(rtnInfo));
			logger.info("guanWenService测试通过");
		}
		
	}
	
//	@Test
	public void modifyTmcaseTest() {
		
		
		GeneralCondition gcon = getGcon();		
		
		Integer caseId=3244;
		TradeMarkCase tmCase = new TradeMarkCase();
		tmCase.setId(caseId);	
		tmCase.setTmName("测试修改商标名称459");
		
		String customerId=null;
		
		TmCaseTaskToDoList toDoList=new TmCaseTaskToDoList();		   
	    
	    toDoList.setCaseId(caseId);
	    Integer taskId=2350021;
	    
	    toDoList.setTaskId(taskId);
	    
	    String remarks="针对商品和服务分类进行了修改，请查看并确认";
	    
	    toDoList.setRemarks(remarks);
	    
	    String userId="3";
	  
        
        TradeMarkCase oldData=null;
		if(caseId!=null){	
			//获取修改前的案件信息
			oldData= tradeMarkCaseMapper.selectByPrimaryKey(caseId);
		}
		
		//调用修改案件接口
		ReturnInfo rtnInfo = tradeMarkCaseService.modifyTradeMarkCase(tmCase, gcon);
		
		if (rtnInfo != null && rtnInfo.getSuccess()){					
			Map<String, Object> map=new HashMap<String, Object>();
            map.put("gcon", gcon);
            map.put("userId", userId);
            map.put("customerId", customerId);
            map.put("toDoList", toDoList);
            map.put("oldData", oldData);
            map.put("newData", tmCase);
							
			//调用提交案件后的流程处理接口
			rtnInfo = tradeMarkCaseTaskService.modifyCase(map, null);	
			if (rtnInfo != null) {
				logger.info(JsonUtil.toJson(rtnInfo));
				logger.info("modifyTmcaseTest测试通过");
			}
		}

	
		
	}
	
//	@Test
	public void queryAgencyServiceDetailTest() {
		GeneralCondition gcon = getGcon();		
		String agencyServiceId="1";
		AgencyService agencyService=agencyServiceService.queryAgencyServiceDetail(gcon, agencyServiceId);
		Integer sId=agencyService.getServiceId();
		
		int serviceId=sId.intValue();				
		
		logger.info("queryAgencyServiceDetailTest测试通过, serviceId: "+ serviceId);
		
		
	}
	
	

	
	
	


//	@Test
	public void modifyRecordTest() {
		GeneralCondition gcon = getGcon();		
		TradeMarkCase tradeMarkCase = new TradeMarkCase();
		
		Integer caseId=1;
		tradeMarkCase.setId(caseId);
		tradeMarkCase.setCustId(1);
		tradeMarkCase.setAgencyId(1);
		tradeMarkCase.setTmName("测试用商标名");
		tradeMarkCase.setApplicantType("法人或其它组织");
		
		List<TradeMarkCaseCategory> goods=new ArrayList<TradeMarkCaseCategory>();
		
		TradeMarkCaseCategory tradeMarkCaseCategory=new TradeMarkCaseCategory();
		tradeMarkCaseCategory.setId(223);
		tradeMarkCaseCategory.setCaseId(caseId);
		tradeMarkCaseCategory.setGoodClass("35");
		tradeMarkCaseCategory.setGoodCode("35001");
		tradeMarkCaseCategory.setGoodKey("122211");
		tradeMarkCaseCategory.setGoodName("测试商品服务描述2");
		goods.add(tradeMarkCaseCategory);
		tradeMarkCase.setGoods(goods);
		
		List<TradeMarkCaseJoinApp> joinApps=new ArrayList<TradeMarkCaseJoinApp>();
		
		TradeMarkCaseJoinApp tradeMarkCaseJoinApp=new TradeMarkCaseJoinApp();
		tradeMarkCaseJoinApp.setId(28);
		tradeMarkCaseJoinApp.setCaseId(caseId);
		tradeMarkCaseJoinApp.setAddrCn("测试用的共同申请人中文地址55");
		tradeMarkCaseJoinApp.setAddrEn("测试用的共同申请人英文地址55");
		
		joinApps.add(tradeMarkCaseJoinApp);
		tradeMarkCase.setJoinApps(joinApps);
		
		
		
		String userId="17";
		String customerId=null;
		Object info;
		TradeMarkCase oldData=null;
		if(caseId!=null){	
			//获取修改前的案件信息
			oldData= tradeMarkCaseMapper.selectByPrimaryKey(caseId);
		}
		
	    Map<String, Object> map=new HashMap<String, Object>();
        map.put("gcon", gcon);
        map.put("userId", userId);
        map.put("customerId", customerId);  
        map.put("oldData", oldData);
        map.put("newData", tradeMarkCase);
        
		try {
			info = customerTaskServiceImpl.modifyRecord(map);
			if (info != null) {
				logger.info(JsonUtil.toJson(info));
				logger.info("queryUserList测试通过");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	

//	@Test
	public void queryRecordTest() {
		GeneralCondition gcon = getGcon();		
		TradeMarkCaseRecord tradeMarkCaseRecord = new TradeMarkCaseRecord();
		Integer caseId=265;
		tradeMarkCaseRecord.setId(caseId);
//		tradeMarkCaseRecord.setRecordId(1);
		
		
	
		Object info;
		try {
			info = tradeMarkCaseRecordService.queryTradeMarkCaseRecordList(tradeMarkCaseRecord);
			if (info != null) {
				logger.info(JsonUtil.toJson(info));
				logger.info("queryUserList测试通过");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	//商标自助注册测试
//	@Test
	public void testStartTmRegisterSelf() {		
		GeneralCondition gcon = new GeneralCondition();		
		Customer customer=new Customer();
		Integer customerId=1;
				
		customer.setUserType(20);
		customer.setId(customerId);
		customer.setUsername("taikang");
		customer.setPassword("123456");		
	
		TradeMarkCase tmCase = new TradeMarkCase();
		
		Integer caseId=1;
		Integer agencyId=1;
		tmCase.setId(caseId);
		tmCase.setAgencyId(agencyId);
					
		String tokenID=customerLogin(customer);
		gcon.setTokenID(tokenID);
		
		String agencyServiceId="1";
		
		Integer serviceId=8;//商标自助注册
		
		String userId=null;
		
		Map<String, Object> map=new HashMap<String, Object>();
        map.put("gcon", gcon);
        map.put("userId", userId);
        map.put("customerId", customerId.toString());
        map.put("agencyServiceId", agencyServiceId);
        map.put("serviceId", serviceId);
        map.put("tmCase", tmCase);
				
		Object info =tradeMarkCaseTaskService.startTmRegisterProcess(map);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("startTmRegisterProcess测试通过");
		}	
	}
	
	
	
	//商标高级注册测试
//	@Test 
	public void testStartTmRegisterProcess() {		
		GeneralCondition gcon = new GeneralCondition();		
		Customer customer=new Customer();
		Integer customerId=1;
				
		customer.setUserType(20);
		customer.setId(customerId);
		customer.setUsername("taikang");
		customer.setPassword("123456");		
	
		TradeMarkCase tmCase = new TradeMarkCase();
		
		Integer caseId=1;
		Integer agencyId=1;
		tmCase.setId(caseId);
		tmCase.setAgencyId(agencyId);
					
		String tokenID=customerLogin(customer);
		gcon.setTokenID(tokenID);
		
		String agencyServiceId="1";
				
		Integer serviceId=9; //商标高级注册
		
		String userId=null;
		
		Map<String, Object> map=new HashMap<String, Object>();
        map.put("gcon", gcon);
        map.put("userId", userId);
        map.put("customerId", customerId+"");
        map.put("agencyServiceId", agencyServiceId);
        map.put("serviceId", serviceId);
        map.put("tmCase", tmCase);
        
		Object info =tradeMarkCaseTaskService.startTmRegisterProcess(map);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("startTmRegisterProcess测试通过");
		}	
	}
	
	
	
	
	
	
	//转发案件测试
//	@Test
	public void assginCaseTest() {	
		
		Integer userId=3;
		GeneralCondition gcon=getGcon();	
		
		//注意，每执行一个任务，走到下一个任务后，taskId是新创建的任务的id，
		//所以，每次测试的时候，需要先查一下数据库中的taskId，然后替换下面的taskId参数的值		
		Integer taskId=2620107;
		
	    TmCaseTaskToDoList toDoList= getToDoList(taskId);
//	    toDoList.setRemarks("转发给第二个代理所处理");		
	    Object info = null;
	    
	    String agencyId="2";
	    
	    //转发案件
	    String transfer="1";
	    Map<String, Object> proMap=null;
		
	    info = tradeMarkCaseTaskService.assginCase(gcon, userId.toString(), agencyId, transfer, toDoList, proMap);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("assginCaseTest测试通过");
		}		
		
	}
	
	
	
	
	//接收案件测试
//	@Test
	public void secondAssginCaseTest() {		
		Integer userId=16;
		GeneralCondition gcon=getGcon();	   
		Integer taskId=1885027;
	    TmCaseTaskToDoList toDoList= getToDoList(taskId);
	    toDoList.setRemarks("接收案件进行处理");		
	    Object info = null;
	    
	    String agencyId="2";
	    
	    //接收案件
	    String transfer="2";
	   
	    Map<String, Object> proMap=null;
	    info = tradeMarkCaseTaskService.assginCase(gcon, userId.toString(), agencyId, transfer, toDoList, proMap);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("audited测试通过");
		}
	}
	


	@Test
	public void queryToDoListTest() {
		//测试代理人的待办事项
		String customerId=null;
		String userId=null;
		userId="3";
		GeneralCondition gcon=getGcon();	
		
		//测试客户的待办事项
//		customerId="61";
//		GeneralCondition gcon=getCustomerGcon();
		
		TradeMarkCase tmcase=new TradeMarkCase();
		
//		String keyword="5747";
//		gcon.setKeyword(keyword);
		
		Object info = tradeMarkCaseTaskService.queryToDoList(gcon, userId, customerId,null,tmcase);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("queryToDoList测试通过");
		}		
	}
	
	
//	@Test
	public void queryToDoListCountTest() {
		//测试代理人的待办事项
		String customerId=null;
		String userId=null;
//		userId="3";
//		GeneralCondition gcon=getGcon();	
		
		//测试客户的待办事项
		customerId="cust_5";
		GeneralCondition gcon=getCustomerGcon();		
		
		Integer agencyId=1;
		
		Object info;
		try {
//			info = tradeMarkCaseTaskService.queryToDoListCount(gcon, userId, customerId, agencyId);
//			if (info != null) {
//				logger.info(JsonUtil.toJson(info));
//				logger.info("queryToDoListCount测试通过");
//			}	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	
	
	
//	@Test
	public void toDoListDetailTest() {
		Integer userId=3;
		GeneralCondition gcon=getGcon();
		String taskId=null;
	    Integer caseId=16240;
	    TmCaseTaskToDoList tmCaseTaskToDoList=new TmCaseTaskToDoList();
	    tmCaseTaskToDoList.setCaseId(caseId);
		Object info = tradeMarkCaseTaskService.toDoListDetail(gcon, null, userId.toString(), taskId, tmCaseTaskToDoList);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("toDoListDetail测试通过");
		}
	}
	
	


	
	
	//测试
//	@Test
	public void queryUserByCustIdTest() {	
		
		ReturnInfo rtnInfo = new ReturnInfo();
		
		String url="http://localhost:8080/bdy_sysm/interface/user/queryUserByCustId?custId=1&tokenID=15220643261373";
		String jsonString;
		try {
			jsonString = GraspUtil.getText(url);
			rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);
			if (rtnInfo!=null && rtnInfo.getSuccess()){
				List<Map<String, Object>> userCustomerList=(List<Map<String, Object>> )rtnInfo.getData();			
				if (userCustomerList!=null){					
					for(Map<String, Object> user2: userCustomerList){
						Integer userId=(Integer)user2.get("userId");		
						logger.info("getUserByCustId result: "+userId);
						
					}
				}				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	
//	@Test
//	public void closeRemindByCaseIdTest() {
//		GeneralCondition gcon = getGcon();		
//		Integer caseId=4373;
//		String message="补正时限";
//		agencyTaskServiceImpl.closeRemindByCaseId(caseId, gcon, message);
//			
//		
//		logger.info("closeRemindByCaseId测试通过");
//		
//		
//	}
	

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
		
		gcon.setOffset(0);
		gcon.setRows(10);
		
		User user=new User();
		
		user.setUsername("whd_wangfang");
		user.setPassword("123456");		
		
	    String tokenID=login(user);	   
	    gcon.setTokenID(tokenID);	
	    
	    authenticationService.authorize(gcon);
	    
	    return gcon;	
	}
	
	private  GeneralCondition getCustomerGcon(){
		GeneralCondition gcon=new GeneralCondition();
		gcon.setPageNo(1);
		gcon.setOffset(0);
		gcon.setRows(10);
		
		Customer customer=new Customer();
		
		customer.setUsername("taikang");
		customer.setPassword("123456");		
		
	    String tokenID=customerLogin(customer);	   
	    gcon.setTokenID(tokenID);			
	    return gcon;	
	}
	
	
	
	
	
}
