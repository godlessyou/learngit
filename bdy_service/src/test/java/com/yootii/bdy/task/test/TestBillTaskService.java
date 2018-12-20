package com.yootii.bdy.task.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yootii.bdy.bill.model.Bill;
import com.yootii.bdy.bill.service.BillService;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.LoginReturnInfo;
import com.yootii.bdy.customer.model.Customer;
import com.yootii.bdy.model.User;
import com.yootii.bdy.security.service.AuthenticationService;
import com.yootii.bdy.task.model.BillToDoList;
import com.yootii.bdy.task.model.TaskCondition;
import com.yootii.bdy.task.model.TaskRecord;
import com.yootii.bdy.task.model.TmCaseTaskToDoList;
import com.yootii.bdy.task.service.BillTaskService;
import com.yootii.bdy.task.service.TradeMarkCaseTaskService;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.util.JsonUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class TestBillTaskService {
	
	@Resource
	private AuthenticationService authenticationService;
	

	@Resource
	private BillTaskService  billTaskService;
	
	@Resource
	private TradeMarkCaseTaskService tradeMarkCaseTaskService;
	private static final Logger logger = Logger.getLogger(TestBillTaskService.class);
	
	



	
	
	
	
//	@Test
	public void startBillReviewProcessTest() {		
		GeneralCondition gcon=new GeneralCondition();
		gcon.setPageNo(1);
		gcon.setOffset(0);
		gcon.setRows(10);
		
		Integer userId=24;
		
		User user=new User();
		user.setUserId(userId);
		user.setUsername("agentA");
		user.setPassword("123456");		
		
	    String tokenID=login(user);
	   
	    gcon.setTokenID(tokenID);
	   
	    String agencyId="1";
	    TmCaseTaskToDoList toDoList=new TmCaseTaskToDoList();
	   
	    Integer caseId=1;
	    toDoList.setCaseId(caseId);
	    
	    Integer taskId=107512;
	    toDoList.setTaskId(taskId);
	    toDoList.setCaseType("商标注册");
	    toDoList.setRemarks("审核通过");
		
	    Object info = null;
		Bill bill = new Bill();
		bill.setAgencyId(1);
		bill.setGroupName("国内部");
		
		
		info = billTaskService.startBillReviewProcess(gcon, userId.toString(), null, bill , "9");
		
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("createTradeMarkCaseTask测试通过");
		}
	}
	
	
	
	
//	@Test
	public void auditedTest() {
		GeneralCondition gcon=new GeneralCondition();
		gcon.setPageNo(1);
		gcon.setOffset(0);
		gcon.setRows(10);
		
		Integer userId=24;
		
		User user=new User();
		user.setUserId(userId);
		user.setUsername("agentA");
		user.setPassword("123456");		
		
	    String tokenID=login(user);
	   
	    gcon.setTokenID(tokenID);
	   
	    String agencyId="1";
	    BillToDoList toDoList=new BillToDoList();
	   
//	    Integer caseId=1;
//	    toDoList.setCaseId(caseId);
	    
	    Integer taskId=27513;
	    toDoList.setTaskId(taskId);
	    toDoList.setRemarks("审核不通过");
		
	    Object info = null;
		Bill bill = new Bill();
		bill.setAgencyId(1);
		bill.setGroupName("国内部");
		
		info =billTaskService.audited(gcon, userId.toString(), toDoList, false);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("createTradeMarkCaseTask测试通过");
		}
	}
	
	
	
	
	@Test
	public void queryToDoListTest() {
		GeneralCondition gcon=new GeneralCondition();
		gcon.setPageNo(1);
		gcon.setOffset(0);
		gcon.setRows(10);
		
		Integer userId=2;
		String customerId=null;
		
		User user=new User();
		user.setUserId(userId);
		user.setUsername("whd_wangfang");
		user.setPassword("123456");		
		
	    String tokenID=login(user);
	   
	    gcon.setTokenID(tokenID);
	   
	    String agencyId="1";
	    BillToDoList toDoList=new BillToDoList();
	   
//	    Integer caseId=1;
//	    toDoList.setCaseId(caseId);
	    
//	    Integer taskId=25019;
//	    toDoList.setTaskId(taskId);
//	    toDoList.setRemarks("审核不通过");
		
	    Object info = null;
		Bill bill = new Bill();
		bill.setAgencyId(1);
//		bill.setGroupName("国内部");
		
		info =billTaskService.queryToDoList(gcon, userId.toString(), customerId);
//		info =billTaskService.modifyBill(gcon, userId.toString(), toDoList, bill);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("createTradeMarkCaseTask测试通过");
		}
	}
//	@Test
	public void createTmcaseTaskTest() {
		GeneralCondition gcon=new GeneralCondition();
		gcon.setPageNo(1);
		gcon.setOffset(0);
		gcon.setRows(10);
		
		Integer userId=24;
		
		User user=new User();
		user.setUserId(userId);
		user.setUsername("agentA");
		user.setPassword("123456");		
		
	    String tokenID=login(user);
	   
	    gcon.setTokenID(tokenID);
	   
	    String agencyId="1";
	    BillToDoList toDoList=new BillToDoList();
	   
//	    Integer caseId=1;
//	    toDoList.setCaseId(caseId);
	    
	    Integer taskId=25051;
	    toDoList.setTaskId(taskId);
	    toDoList.setRemarks("审核不通过");
		
	    Object info = null;
		Bill bill = new Bill();
		bill.setAgencyId(1);
		bill.setGroupName("国内部");
		
//		info = billTaskService.queryToDoList(gcon, userId.toString(), null);
//		if (info != null) {
//			logger.info(JsonUtil.toJson(info));
//			logger.info("createTradeMarkCaseTask测试通过");
//		}
	}
	
//	@Test
	public void modifyTradeMarkCaseTaskTest() {
		TaskRecord record = new TaskRecord();
		record.setId(1);
		record.setTaskName("提交商标注册申请");
		GeneralCondition gcon = new GeneralCondition();
		gcon.setPageNo(1);
		gcon.setOffset(0);
		gcon.setRows(10);
	
		Object info =tradeMarkCaseTaskService.modifyTaskRecord(record);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("modifyTradeMarkCaseTask测试通过");
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
	
	
}
