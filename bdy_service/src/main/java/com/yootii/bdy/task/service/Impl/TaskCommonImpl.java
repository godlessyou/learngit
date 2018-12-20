package com.yootii.bdy.task.service.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.apache.log4j.Logger;

import com.ctc.wstx.util.StringUtil;
import com.yootii.bdy.agency.dao.AgencyUserMapper;
import com.yootii.bdy.agency.service.AgencyService;
import com.yootii.bdy.customer.model.Applicant;
import com.yootii.bdy.customer.model.CustomerContact;
import com.yootii.bdy.common.Constants;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.customer.model.Customer;
import com.yootii.bdy.customer.service.ApplicantService;
import com.yootii.bdy.customer.service.CustomerService;
import com.yootii.bdy.datasyn.service.DataSynService;
import com.yootii.bdy.mail.MailSenderInfo;
import com.yootii.bdy.mail.SendMailUtil;
import com.yootii.bdy.mail.service.MailService;
import com.yootii.bdy.material.dao.MaterialMapper;
import com.yootii.bdy.material.model.Material;
import com.yootii.bdy.material.model.MaterialCondition;
import com.yootii.bdy.model.User;
import com.yootii.bdy.process.service.ProcessService;
import com.yootii.bdy.security.service.AuthenticationService;
import com.yootii.bdy.task.dao.TaskRecordMapper;
import com.yootii.bdy.task.model.MailRecord;
import com.yootii.bdy.task.model.TaskCondition;
import com.yootii.bdy.task.model.TmCaseTaskToDoList;
import com.yootii.bdy.task.model.UserTask;
import com.yootii.bdy.tmcase.dao.TradeMarkAssociationMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseProcessMapper;
import com.yootii.bdy.tmcase.model.TradeMarkAssociation;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.TradeMarkCaseProcess;
import com.yootii.bdy.tmcase.service.TradeMarkCaseService;
import com.yootii.bdy.user.service.UserService;
import com.yootii.bdy.util.AESUtil;
import com.yootii.bdy.util.DateTool;
import com.yootii.bdy.util.GraspUtil;
import com.yootii.bdy.util.ImageUtils;
import com.yootii.bdy.util.JsonUtil;
import com.yootii.bdy.util.ServiceUrlConfig;
import com.yootii.bdy.util.TaskTool;

//---- 客户与代理机构公用接口实现类 -------
@Component
public class TaskCommonImpl {

	private final Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private TradeMarkCaseMapper  tradeMarkCaseMapper;
	

	@Resource
	private ProcessService processService;
	
	@Resource
	private ServiceUrlConfig serviceUrlConfig;

	@Resource
	private MailSenderInfo mailSenderInfo;

	@Resource
	private CustomerService customerService;

	
	@Resource
	private UserService userService;
	
	@Resource
	private AgencyUserMapper agencyUserMapper;
	
	@Resource
	private TradeMarkCaseProcessMapper tradeMarkCaseProcessMapper;
	
	@Resource
	private TradeMarkAssociationMapper tradeMarkAssociationMapper;
	
	@Resource
	private MaterialMapper materialMapper;

	@Resource
	private MailService mailService;
	
	@Resource
	private ApplicantService applicantService;
	
	@Resource
	private AgencyService agencyService;	
	
	@Resource
	private DataSynService dataSynService;
	

	public TaskCondition getTaskCondition(GeneralCondition gcon,
			List<UserTask> userTaskList) throws Exception {

		List<Integer> caseIds = null;

		TaskCondition taskCondition = new TaskCondition();

		if (userTaskList != null && userTaskList.size() > 0) {
			caseIds = new ArrayList<Integer>();
			// 构造查询所用的案件编号集合
			for (UserTask userTask : userTaskList) {
				String caseId = userTask.getProMap().get("caseId").toString();
				Integer id = new Integer(caseId);
				if (!caseIds.contains(id)) {
					caseIds.add(id);
				}
			}

			taskCondition.setCaseIds(caseIds);
		}

		return taskCondition;
	}

	public TaskCondition getBillTaskCondition(GeneralCondition gcon,
			List<UserTask> userTaskList) throws Exception {

		List<Integer> billIds = null;

		TaskCondition taskCondition = new TaskCondition();

		if (userTaskList != null && userTaskList.size() > 0) {
			billIds = new ArrayList<Integer>();
			// 构造查询所用的案件编号集合
			for (UserTask userTask : userTaskList) {				
				String billId = (String)userTask.getProMap().get("billId");
				if (billId==null || billId.equals("")){
					continue;
				}
				Integer id = new Integer(billId);
				if (!billIds.contains(id)) {
					billIds.add(id);
				}
			}

			taskCondition.setBillIds(billIds);
		}

		return taskCondition;
	}

	public List<Map<String, Object>> findUserTaskUrl(String userId, Integer toDoListType)
			throws Exception {
		String url = serviceUrlConfig.getProcessEngineUrl()
				+ "/Task/findusertask?userId=" + userId;
		if(toDoListType!=null){
			url = url+"&toDoListType="+ toDoListType.toString();
		}
//		logger.info(url);
		String jsonString;

		jsonString = GraspUtil.getText(url);
		ReturnInfo rtnInfo = JsonUtil.toObject(jsonString, ReturnInfo.class);

		List<Map<String, Object>> userList = (List<Map<String, Object>>) rtnInfo
				.getData();
		return userList;
	}
	
	public List<Map<String, Object>> findAllCustomerTaskUrl(String agencyId, Integer toDoListType)
			throws Exception {
		String url = serviceUrlConfig.getProcessEngineUrl()
				+ "/Task/findAllCustomerTask?agencyId=" + agencyId;
		if(toDoListType!=null){
			url = url+"&toDoListType="+ toDoListType.toString();
		}
//		logger.info(url);
		String jsonString;

		jsonString = GraspUtil.getText(url);
		ReturnInfo rtnInfo = JsonUtil.toObject(jsonString, ReturnInfo.class);

		List<Map<String, Object>> userList = (List<Map<String, Object>>) rtnInfo
				.getData();
		return userList;
	}
	
	// 获取客户/用户的案件待办任务
	public List<UserTask> findUserTask(String userId,Integer pageId) throws Exception {

		List<Map<String, Object>> userList = findUserTaskUrl(userId, 1);

		List<UserTask> list = getUserTask(userId, pageId, userList);
		return list;
	}
	
	
	// 获取某个代理所的所有客户的案件待办任务
	public List<UserTask> findAllCustomerTask(String agencyId,Integer pageId) throws Exception {

		List<Map<String, Object>> userList = findAllCustomerTaskUrl(agencyId, 1);

		List<UserTask> list = getUserTask(null, pageId, userList);
		return list;
	}


	
	private List<UserTask> getUserTask(String userId, Integer pageId, List<Map<String, Object>>userList) throws Exception {

		List<UserTask> list = new ArrayList<UserTask>();

		if (userList != null) {
			for (Map<String, Object> user2 : userList) {
//				logger.info(user2);


				String caseId = (String) ((Map<String, Object>) user2.get("proMap")).get("caseId");
				String agencyId = (String) ((Map<String, Object>) user2.get("proMap")).get("agencyId");
				String transfer = (String) ((Map<String, Object>) user2.get("proMap")).get("transfer");
				String guanwenName = (String) ((Map<String, Object>) user2.get("proMap")).get("guanwenName");

				if (caseId != null) {
					String taskId = (String) user2.get("taskId");
					String taskName = (String) user2.get("taskName");
					String remarks = (String) user2.get("remarks");

//					logger.info("findUserTask: taskId=" + taskId + " taskName="
//							+ taskName + " caseId=" + caseId + " agencyId="+agencyId+" remarks="
//							+ remarks);
					Map<String, Object> proMap = new HashMap<String, Object>();
					proMap.put("caseId", caseId);
					proMap.put("agencyId", agencyId);
					proMap.put("transfer", transfer);
					proMap.put("guanwenName", guanwenName);

					UserTask userTask = new UserTask();
					userTask.setProMap(proMap);
					userTask.setTaskId(taskId);
					userTask.setTaskName(taskName);
					userTask.setRemarks(remarks);
					Boolean isCustomer = false;
					if(userId!=null && pageId!=null) {
						if(userId.indexOf(Constants.customer_prefix)>0) isCustomer = true;
						if(TaskTool.getPageId(taskName, isCustomer,(transfer=="1")) ==pageId) {
							list.add(userTask);

						} else {
							
						}
						
					} else {
						list.add(userTask);
					}				

				}
			}
		}

		return list;
	}
	
	
	
	// 获取客户/用户的账单待办任务
	public List<UserTask> findBillTask(String userId) throws Exception {

		List<Map<String, Object>> userList = findUserTaskUrl(userId, 2);

		List<UserTask> list = new ArrayList<UserTask>();

		if (userList != null) {
			for (Map<String, Object> user2 : userList) {
				String billId = (String) ((Map<String, Object>) user2.get("proMap")).get("billId");
				if (billId != null) {
					String taskId = (String) user2.get("taskId");
					String taskName = (String) user2.get("taskName");
					String remarks = (String) user2.get("remarks");

					Map<String, Object> proMap = new HashMap<String, Object>();
					proMap.put("billId", billId);
					
					UserTask userTask = new UserTask();
					userTask.setProMap(proMap);
					userTask.setTaskId(taskId);
					userTask.setTaskName(taskName);
					userTask.setRemarks(remarks);
					
					list.add(userTask);
				}
			}
		}

		return list;
	}
	
	
	

	public Map<String, Object> getProcessVarialble(String taskId)
			throws Exception {

		ReturnInfo rtInfo = processService.showtaskvariables(taskId);
		if (rtInfo.getSuccess() != true) {
			throw new Exception("获取流程中的参数失败|" + rtInfo.getMessage());
		}
		Map<String, Object> proMap = (Map<String, Object>) rtInfo.getData();
		
		if (proMap==null || proMap.size()==0){
			throw new Exception("获取流程中的参数失败，当前taskId: "+ taskId);
		}

		return proMap;
	}
	
	
	public List<Map<String, Object>> getProcessVarialbleByPro(String processInstanceId)
			throws Exception {

		ReturnInfo rtInfo = processService.showtaskvariablesbypro(processInstanceId);
		if (rtInfo.getSuccess() != true) {
			throw new Exception("获取流程中的参数失败|" + rtInfo.getMessage());
		}
		List<Map<String, Object>> proMapList = (List<Map<String, Object>>) rtInfo.getData();

		return proMapList;
	}

	// 返回的待办事项中增加任务相关的taskId,taskTitle,remarks
	public void getToDoList(List<TmCaseTaskToDoList> tmcases,
			List<UserTask> userTaskList, boolean isCustomer, String userId) {
		// logger.info("getToDoList start");

		for (TmCaseTaskToDoList toToList : tmcases) {
			
			Integer agentUserId=toToList.getAgentUserId();//负责该客户的代理人
			
			Integer caseId = toToList.getCaseId();
			String cId = caseId.toString();
			String agencyId =null;
			String transfer =null;
			String guanwenName =null;
			if (userTaskList != null && userTaskList.size() > 0) {
				for (UserTask userTask : userTaskList) {
					String id = userTask.getProMap().get("caseId").toString();
					
					if (id.equals(cId)) {
						Object obj= userTask.getProMap().get("agencyId");
						if (obj!=null){
							agencyId = obj.toString();
						}
						obj= userTask.getProMap().get("transfer");
						if (obj!=null){
							transfer = obj.toString();
						}
						obj= userTask.getProMap().get("guanwenName");
						if (obj!=null){
							guanwenName = obj.toString();
						}
						String taskId = userTask.getTaskId();
						String taskName = userTask.getTaskName();
						String remarks = userTask.getRemarks();
						Integer tId = new Integer(taskId);
						toToList.setTaskId(tId);
						String currentTask=taskName;
						if(currentTask.indexOf("形式审查结果")>-1){
							currentTask=currentTask.replaceAll("形式审查结果", "形审");
						}
						else if(currentTask.indexOf("实质审查结果")>-1){
							currentTask=currentTask.replaceAll("实质审查结果", "实审");
						}
						toToList.setTaskName(currentTask);
						toToList.setRemarks(remarks);
						
						if(guanwenName!=null && !guanwenName.equals("")){
							List<String> fileNameList=new ArrayList<String>();							
							StringTokenizer idtok = new StringTokenizer(guanwenName, ",");
							while (idtok.hasMoreTokens()) {
								String value = idtok.nextToken();												
								fileNameList.add(value);
							}
							toToList.setFileNameList(fileNameList);
						}
						
						break;
					}
				}
			}

			String taskName = toToList.getTaskName();
			
			if (taskName==null || taskName.equals("")){
				toToList.setPageId(0);
			}else{
				Integer userAgencyId=null;
				boolean caseTransfered=false;
				if (transfer!=null && transfer.equals("1")){
					caseTransfered=true;
				}else{
					if (userId!=null && !userId.equals("")){
						Integer id=new Integer(userId);
						userAgencyId=agencyUserMapper.selectAgencyIdByUserId(id);						
						//Modification start, 2018-12-10
						//to resolve BDY2-32
						if(userAgencyId!=null){
							String aId=userAgencyId.toString();
							if(aId!=null && agencyId!=null && !aId.equals(agencyId)){
								caseTransfered=true;
							}
						}
						//Modification end
					}
				}
				
				Integer pageId = null;
				//如果当前代理机构中，负责该客户的代理人为空，设置pageI=16，用来表示该待办事项是属于“待分配”的任务
				//需要由处理该待办事项的代理所的/一级部门负责人，将该案件分配给代理人，或者将该客户分配给代理人
				if(!isCustomer && agentUserId==null){
					pageId=16;
				}else{				
					pageId = TaskTool.getPageId(taskName,isCustomer, caseTransfered);
				}
				toToList.setPageId(pageId);
			}
			
            //Modification start, 2018-05-24
			//because get caseTypeId from db, close following code 
//			String caseType = toToList.getCaseType();
//			Integer caseTypeId = TaskTool.getCaseTypeId(caseType);
//
//			toToList.setCaseTypeId(caseTypeId);

//			logger.info("getToDoList: pageId=" + pageId + " taskName="
//					+ taskName + " caseId=" + caseId + " caseTypeId="
//					+ toToList.getCaseId());

		}
		// logger.info("getToDoList end");

	}

	public String getProperyOfProcess(Map<String, Object> proMap,
			String taskId, String propertyName) throws Exception {
		Object obj = proMap.get(propertyName);
		if (obj == null) {
			throw new Exception("获取流程中的" + propertyName + "结果为空, taskId:"
					+ taskId);
		}
		String value = obj.toString();
		return value;
	}

	
	public void sendMail(Map<String, Object> proMap, String taskId,
			Boolean result, GeneralCondition gcon, String userId, String custId, String caseType, String mailType) throws Exception {

		String tokenID=gcon.getTokenID();
		String taskName =  getProperyOfProcess(proMap, taskId, "taskName");				
		String caseId = getProperyOfProcess(proMap, taskId, "caseId");
		
		String fileName= null;
		Object objFile = proMap.get("fileName");
		if (objFile != null) {
			fileName=(String)objFile;
		}
		
		boolean sendToCust=true;		
		
		//获取代理人信息，如果流程中的代理人userId为空，意味着是客户启动的该流程，所以，从流程中获取custId
		if (userId==null || userId.equals("")){
			Object obj = proMap.get("userId");
			if (obj != null) {
				userId=(String)obj;				
			}
			sendToCust=false;			
		}
		
		if (custId==null || custId.equals("")){
			Object obj = proMap.get("custId");
			if (obj != null) {
				custId=(String)obj;				
			}			
		}
		
		String agentUserId = null;
		Object obj = proMap.get("agentUserId");
		if (obj != null) {
			agentUserId=(String)obj;				
		}				
		
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("caseIds", caseId);
		map.put("fileName", fileName);
		map.put("taskName", taskName);
		map.put("result", result);
		map.put("gcon", gcon);
		map.put("userId", userId);
		map.put("custId", custId);
		map.put("sendToCust", sendToCust);
		map.put("caseType", caseType);
		map.put("mailType", mailType);
		map.put("agentUserId", agentUserId);
		
		sendMail(map);

	}
	
	
	public void sendMail(Map<String, Object> map) throws Exception {
		
		String caseId=(String)map.get("caseIds");
		String fileName=(String)map.get("fileName");
		String taskName=(String)map.get("taskName");		
		Boolean result=(Boolean)map.get("result");
		GeneralCondition gcon=(GeneralCondition)map.get("gcon");
		String userId=(String)map.get("userId");
		String custId=(String)map.get("custId");
		Boolean sendToCust=(Boolean)map.get("sendToCust");
		String mailType=(String)map.get("mailType");
		String caseType=(String)map.get("caseType");
		String agentUserId=(String)map.get("agentUserId");
		
		String tokenID=gcon.getTokenID();
		if(userId==null){
			logger.info("can not send email, userId is null" );
			return;
		}
		if (agentUserId==null && custId==null){
			logger.info("can not set email , custId is null" );
			return;
		}
		
		
		String mailSubject = null;
		String mailContent = null;
		List<String> list = TaskTool.getMailContent(taskName, caseId, result,fileName);
		if (list!=null && list.size()>0){
			mailSubject = list.get(0);
			mailContent = list.get(1);		
			logger.info("mailSubject:" + mailSubject + " mailContent:"
					+ mailContent);			
		}
		
		String fromAddress = null;
		String toAddress = null;
//		String emailPassword = null;
		String customerName = null;
		String toPerson=null;
		
		User user = userService.getUserById(userId, tokenID);
		
		User user2=null;		
		if (agentUserId!=null && !agentUserId.equals("")){
			user2 = userService.getUserById(agentUserId, tokenID);
		}
		
		Customer customer=null;
		CustomerContact customerContact =null;
		if (custId!=null && custId.indexOf(Constants.customer_prefix)>-1){
			custId=custId.substring(Constants.customer_prefix.length());
		}			
		customer = customerService.getCustById(custId, tokenID);
		customerContact = customerService.getCustomerContact(custId, tokenID);	
		
		//默认是使用英文邮件模板，在此增加对客户的国籍的判断。
		//如果客户的国籍是中国，那么，使用中文邮件模板
		String country=customer.getCountry();
		if (country!=null && (country.equals("中国") || country.equalsIgnoreCase("china"))){
			int pos=mailType.indexOf("_en");
			if (pos>-1){
				mailType=mailType.substring(0,pos);
			}
		}
				
		if (sendToCust){			
			fromAddress = user.getEmail();	
			if (user2!=null){
				toAddress = user2.getEmail();				
			}else{
				toAddress = customer.getEmail();				
			}			
			customerName=customer.getFullname();
			toPerson=customerContact.getName();
//			emailPassword = user.getEmailPass();
		}else{			
			fromAddress = customer.getEmail();
			toAddress = user.getEmail();
//			emailPassword = customer.getEmailPass();
		}
		
		if(mailType.equals("sbaj_create")){
			mailSubject="立案通知";
		}else if(mailType.equals("sbaj_create_en")){
			mailSubject="Notice of filing a case";
		}
		
		
		Map<String, Object> mailMap=new HashMap<String, Object>();
	
		if (userId!=null){	
			mailMap.put("userId", userId);
		}
		if (caseId!=null){	
			mailMap.put("caseIds", caseId);
		}
		if (mailContent!=null && !mailContent.equals("")){	
			mailMap.put("commContent", mailContent);
		}
		if (mailSubject!=null && !mailSubject.equals("")){	
			mailMap.put("subject", mailSubject);
		}
		if (toAddress!=null){	
			mailMap.put("toAddress", toAddress);
		}
		if (toPerson!=null){	
			mailMap.put("toPerson", toPerson);
		}
		if (customerName!=null){	
			mailMap.put("customerName", customerName);
		}
		if (tokenID!=null){	
			mailMap.put("tokenID", tokenID);
		}
		if (caseType!=null){	
			mailMap.put("caseType", caseType);
		}
		String bdyUrl=serviceUrlConfig.getHomeUrl()+"/bdy/bdy/case_xq.html?caseId="+caseId;
		mailMap.put("bdyUrl", bdyUrl);
		
		//针对递交完成，以及后续的官方通知类邮件，设置必要的邮件属性
		setMailProperty(mailType, caseId, mailMap);
		
		// 给客户发送报告的邮件
		ReturnInfo returnInfo=mailService.sendMail(mailType, mailMap);
		if (!returnInfo.getSuccess()){
			logger.info("sendMail fail, return error message: "+returnInfo.getMessage());
		}
		
//		sendEmail(user, customer, sendToCust, mailType, mailMap) ;
		

//		logger.info("send email success to userId:" + userId);
	}
	
	
	
	public void sendComunicationMail(Map<String, Object> proMap, String taskId,
			String mailContent, GeneralCondition gcon, String userId, String custId) throws Exception {

		String tokenID=gcon.getTokenID();
		String taskName = getProperyOfProcess(proMap, taskId, "taskName");
		String caseId = getProperyOfProcess(proMap, taskId, "caseId");
		
		String fileName=null;
		Object obj=proMap.get("fileName");
		if (obj!=null){
			fileName=(String)obj;
		}
		
		
		boolean sendToCust=true;
				
		
		String sendFrom="";
		String sendTo="";
		CustomerContact customerContact =null;
		Customer customer = null;
		User user1 = null;
		User user2 = null;
		String country=null;
		
		//流程中保存的userId
		String userId2=null;
		obj = proMap.get("userId");
		if (obj != null) {
			userId2=(String)obj;				
		}	
		user2 = userService.getUserById(userId2, tokenID);
		
		//如果custId!=null，意味着登录的是客户，那么，发件人是客户，接收人是流程变量中保存的用户userId2。		
		if (custId!=null && !custId.equals("")){	
			customerContact = customerService.getCustomerContact(custId, tokenID);
			customer = customerService.getCustById(custId, tokenID);			
			sendFrom=customer.getUsername();
			sendTo=user2.getUsername();			
			sendToCust=false;
		}else if (userId!=null && !userId.equals("")){				
			
			Integer id=new Integer(userId);
			Integer agencyId=agencyUserMapper.selectAgencyIdByUserId(id);
			
			String agencyId2 = getProperyOfProcess(proMap, taskId, "agencyId");
			int aId2=Integer.parseInt(agencyId2);
				
			user1 = userService.getUserById(userId, tokenID);
			
			//1、如果登录用户的代理机构id与流程变量中保存的代理机构id不同，意味着当前调用该方法的代理人（userId）
			//不属于处理当前案件的代理机构，因此，他应该给当前案件的代理人（流程中的代理人）发送邮件。
			//发件人是登录后的用户userId，接收人是流程变量中保存的用户userId2
			
			if (agencyId!=null && agencyId.intValue()!=aId2){				
				sendFrom=user1.getUsername();
				sendTo=user2.getUsername();	
			}else{
				
				//2、否则，如果两个代理机构id相同，他应该给转发案件的人送邮件，
				//这个转发案件的人可能是客户，也可能是上一个代理所的代理人，
				//因此，他应该先判断当前是否能从流程变量（agentUserId）中获取上一个代理所的代理人，
				String agentUserId = null;
				obj = proMap.get("agentUserId");
				if (obj != null) {
					agentUserId=(String)obj;				
				}				
				
				//3、如果能获取上一个代理所的代理人，那么，发件人是登录后的用户userId，接收人是流程变量中保存的用户agentUserId
				if (agentUserId!=null && !agentUserId.equals("")){
					user2 = userService.getUserById(agentUserId, tokenID);
					sendFrom=user1.getUsername();
					sendTo=user2.getUsername();
				}else{				
					//4、如果不能获取agentUserId，证明转发案件的是客户，
					//那么，发件人是登录后的用户userId，接收人是流程变量中保存的客户（custId）				
					obj = proMap.get("custId");
					if (obj != null) {
						custId=(String)obj;				
					}

					if (custId!=null && custId.indexOf(Constants.customer_prefix)>-1){
						custId=custId.substring(Constants.customer_prefix.length());
					}
					customerContact = customerService.getCustomerContact(custId, tokenID);
					customer = customerService.getCustById(custId, tokenID);	
					sendFrom=user1.getUsername();
					sendTo=customer.getUsername();
				}
			}			
		}	
	
		String mailSubject = null;
	
		if(fileName!=null){
			List<String> list = TaskTool.getMailContent(taskName, caseId, null, fileName);
			mailSubject = list.get(0);
			mailContent = list.get(1);
		}else{		
			if (taskName.indexOf("审核")>-1 || taskName.indexOf("修改案件")>-1){
				mailSubject="关于案件编号为"+caseId+"的"+taskName+"的报告";
			}
			else if(taskName.indexOf("通知")>-1){
				mailSubject="关于案件编号为"+caseId+"的官方通知";
			}
		}

		logger.info("mailSubject:" + mailSubject + " mailContent:"
				+ mailContent);	

		//记录代理人与客户的沟通记录		
//		MailRecord mailRecord=new MailRecord();
//		Integer cId=new Integer(caseId);
//		mailRecord.setCaseId(cId);
//		mailRecord.setContent(mailContent);
//		mailRecord.setSendFrom(sendFrom);
//		mailRecord.setSendTo(sendTo);
//		Date sendTime=new Date();
//		mailRecord.setSendTime(sendTime);
				
		String fromAddress = null;
		String toAddress = null;
//		String emailPassword = null;
		String customerName = null;
		String toPerson=null;
		
	
	
		// 给客户发送报告的邮件
		if (customer!=null){	
			//客户的国籍
			country=customer.getCountry();
			
			if (sendToCust){
				if (user1!=null){
					fromAddress = user1.getEmail();			
	//				toAddress = customerContact.getEmail();
					toAddress = customer.getEmail();
					customerName=customer.getFullname();
					toPerson=customerContact.getName();
				}
//				emailPassword = user.getEmailPass();
			}else{
				if (user2!=null){				
					fromAddress = customer.getEmail();
					toAddress = user2.getEmail();
				}
				
//				emailPassword = customer.getEmailPass();
			}
//			sendEmail( user2, customer, sendToCust, mailType, mailMap) ;
		}else if (user1!=null && user2!=null){
			
			fromAddress = user1.getEmail();			
			toAddress = user2.getEmail();
			toPerson = user2.getFullname();
			
//			sendEmail(mailContent, mailSubject, user1, user2) ;
		}
		
		
		Map<String, Object> mailMap=new HashMap<String, Object>();
		
		
		String mailType="sbzcsq_communicate_en";
		
		if(country!=null && (country.equals("中国") || country.equalsIgnoreCase("china"))){
			mailType="sbzcsq_communicate";
		}
		

		if (userId!=null){	
			mailMap.put("userId", userId);
		}
		if (caseId!=null){	
			mailMap.put("caseIds", caseId);
		}
		if (mailContent!=null){	
			mailMap.put("commContent", mailContent);
		}
		if (mailSubject!=null){	
			mailMap.put("subject", mailSubject);
		}	
		if (toAddress!=null){	
			mailMap.put("toAddress", toAddress);
		}
		if (toPerson!=null){	
			mailMap.put("toPerson", toPerson);
		}
		if (customerName!=null){	
			mailMap.put("customerName", customerName);
		}
		if (tokenID!=null){	
			mailMap.put("tokenID", tokenID);
		}	

		String bdyUrl=serviceUrlConfig.getHomeUrl()+"bdy/bdy/case_xq.html?caseId="+caseId;
		mailMap.put("bdyUrl", bdyUrl);	
		
		
		// 给客户发送反馈的邮件
		ReturnInfo returnInfo=mailService.sendMail(mailType, mailMap);
		if (!returnInfo.getSuccess()){
			logger.info("sendMail fail, return error message: "+returnInfo.getMessage());
		}

	}

	
	
//	public void sendEmail(
//			User user, Customer customer, boolean sendToCust, String mailType, Map<String, Object> mailMap) throws Exception {
		
//		String fromAddress = null;
//		String toAddress = null;
////		String emailPassword = null;
//
//		
//				
//		if (sendToCust){			
//			fromAddress = user.getEmail();			
//			toAddress = customer.getEmail();
////			emailPassword = user.getEmailPass();
//		}else{
//			fromAddress = customer.getEmail();
//			toAddress = user.getEmail();
////			emailPassword = customer.getEmailPass();
//		}
		
	
		
		
//		if (emailPassword!=null && !emailPassword.equals("")){			
//			//可能需要先对password进行解密处理，然后再用
//			String password=AESUtil.decrypt(emailPassword);
//			mailSenderInfo.setPassword(password);
//		}else{
//			logger.info("send email fail, email password is null" );
//			return;
//		}
		
//		if (fromAddress!=null && !fromAddress.equals("")){
//			mailSenderInfo.setFromAddress(fromAddress);	
//		}else{
//			logger.info("send email fail, email from address is null");
//			return;
//		}
//		if (toAddress!=null && !toAddress.equals("")){
//			mailSenderInfo.setToAddress(toAddress);		
//		}else{
//			logger.info("send email fail, email to address is null");
//			return;
//		}
		
//		mailSenderInfo.setSubject(mailSubject);
//		mailSenderInfo.setContent(mailContent);		
//		mailSenderInfo.setValidate(true);
//
//		// 发送邮件
//		SendMailUtil sendMailUtil = new SendMailUtil();
//		sendMailUtil.sendmail(mailSenderInfo);
		
		
		
//		logger.info("send email from " + fromAddress + " to " + toAddress + " success");

//	}
	
	
	
	public void sendEmail(String mailContent, String mailSubject,
			User user1, User user2) throws Exception {
		
		String fromAddress = null;
		String toAddress = null;
		String emailPassword = null;
					
		fromAddress = user1.getEmail();			
		toAddress = user2.getEmail();
		emailPassword = user1.getEmailPass();
		
		if (emailPassword!=null && !emailPassword.equals("")){
			//可能需要先对password进行解密处理，然后再用
			String password=emailPassword;
			mailSenderInfo.setPassword(password);
		}else{
			logger.info("send email fail, email password is null" );
			return;
		}
		
		if (fromAddress!=null && !fromAddress.equals("")){
			mailSenderInfo.setFromAddress(fromAddress);	
		}else{
			logger.info("send email fail, email from address is null");
			return;
		}
		if (toAddress!=null && !toAddress.equals("")){
			mailSenderInfo.setToAddress(toAddress);		
		}else{
			logger.info("send email fail, email to address is null");
			return;
		}
		
		mailSenderInfo.setSubject(mailSubject);
		mailSenderInfo.setContent(mailContent);		
		mailSenderInfo.setValidate(true);

		// 发送邮件
		SendMailUtil sendMailUtil = new SendMailUtil();
		sendMailUtil.sendmail(mailSenderInfo);
		
		logger.info("send email from " + fromAddress + " to " + toAddress + " success");

	}
	
	
	//记录案件的处理流程
	public void recordTmCaseProcess(Integer caseId, String caseStatus, String username, Integer userId, Integer custId, int level){
				
		TradeMarkCaseProcess tradeMarkCaseProcess=new TradeMarkCaseProcess();
		tradeMarkCaseProcess.setCaseId(caseId);
		tradeMarkCaseProcess.setStatus(caseStatus);
		Date statusTime=new Date();
		if (caseStatus!=null && caseStatus.equals("网上递交申请")){
			//对于自助网申的案件，立案与网上递交申请这两个task连续执行，保存到数据库中的任务时间相同，不方便查询任务状态
			//为了跟上一个任务的时间有所区别，需要将时间增加1秒
			statusTime=new Date(System.currentTimeMillis()+1000);			
		}
		tradeMarkCaseProcess.setStatusTime(statusTime);
		tradeMarkCaseProcess.setUsername(username);
		tradeMarkCaseProcess.setUserId(userId);
		tradeMarkCaseProcess.setCustId(custId);
		
		tradeMarkCaseProcess.setLevel(level);
		tradeMarkCaseProcessMapper.insertSelective(tradeMarkCaseProcess);		
		
		//设置相关案件的流程
		while(true){			
			TradeMarkAssociation tradeMarkAssociation=tradeMarkAssociationMapper.selectByCaseId(caseId);			
			if (tradeMarkAssociation==null){
				break;
			}else{
				caseId=tradeMarkAssociation.getRelatedCaseId();
				if(caseId==null){
					break;
				}
				TradeMarkCaseProcess tcPocess=new TradeMarkCaseProcess();
				tcPocess.setCaseId(caseId);
				tcPocess.setStatus(caseStatus);				
				tcPocess.setStatusTime(statusTime);
				tcPocess.setUsername(username);
				tcPocess.setUserId(userId);
				tcPocess.setCustId(custId);
				tcPocess.setLevel(level);
				tradeMarkCaseProcessMapper.insertSelective(tcPocess);	
			}
		}
		
	}
	
	
	
	//修改案件状态
	public void recordTmCaseStatus(TradeMarkCase tradeMarkCase){
		
		//修改当前案件状态
		tradeMarkCaseMapper.updateByPrimaryKeySelective(tradeMarkCase);
		
		Integer caseId=tradeMarkCase.getId();
		String status=tradeMarkCase.getStatus();
		Date statusDate=tradeMarkCase.getCreateDate();
		
		//设置相关案件的状态
		while(true){			
			TradeMarkAssociation tradeMarkAssociation=tradeMarkAssociationMapper.selectByCaseId(caseId);			
			if (tradeMarkAssociation==null){
				break;
			}else{
				caseId=tradeMarkAssociation.getRelatedCaseId();
				if(caseId==null){
					break;
				}
				TradeMarkCase tmCase=new TradeMarkCase();
				tmCase.setId(caseId);
				tmCase.setStatus(status);			
				tmCase.setStatusDate(statusDate);
				tradeMarkCaseMapper.updateByPrimaryKeySelective(tmCase);
			}
		}
		
	}
	
	
	
	// 设置邮件模板必要的属性
	private void setMailProperty(String mailType, String caseId, Map<String, Object>mailMap){
		if(mailType.equals("sbzcsq_zjds_en") 
				|| mailType.equals("sbzcsq_zcsqst_en") 
				|| mailType.equals("sbzcsq_csgg_en")
				|| mailType.equals("sbzcsq_zcgg_en")){	
			Integer id=new Integer(caseId);
			TradeMarkCase tmCase=tradeMarkCaseMapper.selectByPrimaryKey(id);
			
			//各种邮件模板都需要的共性属性
			//商标图样
			String trademarkImage="";
			List<Material> materialList = materialMapper.selectImageByCaseId(id);
			if(materialList != null ) {
				for(Material material:materialList){					
					Integer materialId=material.getMaterialId();
					if (materialId!=null){
						trademarkImage=materialId.toString();
						break;
					}
				}
			}	
			//申请人
			String trademarkShenqingren=tmCase.getAppEnName();
			if (trademarkShenqingren==null || trademarkShenqingren.equals("") ){				
				trademarkShenqingren=tmCase.getAppCnName();
			}	
			
			//商品/服务类别
			String trademarkClass=tmCase.getGoodClasses();			
			//申请日期
			Date appDate=tmCase.getAppDate();
			//商标申请号号
			String trademarkNumber=tmCase.getAppNumber();
			//代理文号
			String agentNum=tmCase.getAgentNum();
			//商标名称
			String tmName=tmCase.getTmName();
			
			if (trademarkImage!=null){
				mailMap.put("trademarkImage", trademarkImage);	
			}
			if (trademarkShenqingren!=null){
				mailMap.put("trademarkShenqingren", trademarkShenqingren);	
			}
			if (trademarkClass!=null){
				mailMap.put("trademarkClass", trademarkClass);		
			}
			if (appDate!=null){
				String date=DateTool.getDate(appDate);
				mailMap.put("appDate", date);		
			}			
			if (trademarkNumber!=null){
				mailMap.put("trademarkNumber", trademarkNumber);		
			}	
			if (agentNum!=null){
				mailMap.put("docId", agentNum);		
			}
			
			mailMap.put("subject", "Trademark Application for "+ tmName +" in class "+ trademarkClass+" in China");	
		
			//商标受理通知书
			if(mailType.equals("sbzcsq_zcsqst_en")){	
				//生成邮件附件名称
				String destFileName="商标受理通知书";
				mailMap.put("destFileName", destFileName);		
				
				//附件文件的资料Id
				String materialId=null;
				List<Integer> fileList=new ArrayList<Integer>();
				fileList.add(332);
				Integer cId=new Integer(caseId);
				
				MaterialCondition materialCondition=new MaterialCondition();
				materialCondition.setCaseId(cId);
				materialCondition.setFileNames(fileList);
				List<Material> guanwenList= materialMapper.selectByCaseIdAndFileNames(materialCondition);
				if(guanwenList != null ) {
					for(Material material:guanwenList){					
						Integer mId=material.getMaterialId();
						if (mId!=null){
							materialId=mId.toString();
							break;
						}
					}
				}	
				if (materialId!=null){
					mailMap.put("ftpFilePath", materialId);	
				}
			
//				mailMap.put("subject", "Trademark Application for "+ tmName +" in class "+ trademarkClass+" in China");	
				
			}else if(mailType.equals("sbzcsq_csgg_en")){	//商标初步审定公告
				String destFileName="商标初步审定公告";
				mailMap.put("destFileName", destFileName);	
				
				String materialId=null;
				List<Integer> fileList=new ArrayList<Integer>();
				fileList.add(316);
				Integer cId=new Integer(caseId);
				MaterialCondition materialCondition=new MaterialCondition();
				materialCondition.setCaseId(cId);
				materialCondition.setFileNames(fileList);
				List<Material> guanwenList= materialMapper.selectByCaseIdAndFileNames(materialCondition);
				if(guanwenList != null ) {
					for(Material material:guanwenList){					
						Integer mId=material.getMaterialId();
						if (mId!=null){
							materialId=mId.toString();
							break;
						}
					}
				}	
				if (materialId!=null){
					mailMap.put("ftpFilePath", materialId);	
				}	
				
				//初审公告期
				String trademarkCsggQi=tmCase.getApprovalNumber();
				if (trademarkCsggQi!=null){
					mailMap.put("trademarkCsggQi", trademarkCsggQi);		
				}
				//初审公告日
				Date date=tmCase.getApprovalDate();
				if (date!=null){
					String trademarkCsggDay=DateTool.getDate(date);
					if (trademarkCsggDay!=null){
						mailMap.put("trademarkCsggDay", trademarkCsggDay);		
					}
				}	
//				mailMap.put("subject", "Trademark Application for "+ tmName +" in class "+ trademarkClass+" in China");	
				
			}
			else if(mailType.equals("sbzcsq_zcgg_en")){	 //商标注册公告
				String destFileName="商标注册公告";
				mailMap.put("destFileName", destFileName);					
				
				String materialId=null;
				List<Integer> fileList=new ArrayList<Integer>();
				fileList.add(320);
				Integer cId=new Integer(caseId);	
				
				MaterialCondition materialCondition=new MaterialCondition();
				materialCondition.setCaseId(cId);
				materialCondition.setFileNames(fileList);
				List<Material> guanwenList= materialMapper.selectByCaseIdAndFileNames(materialCondition);
				if(guanwenList != null ) {
					for(Material material:guanwenList){					
						Integer mId=material.getMaterialId();
						if (mId!=null){
							materialId=mId.toString();
							break;
						}
					}
				}	
				if (materialId!=null){
					mailMap.put("ftpFilePath", materialId);	
				}
				
				//有效期起始日
				Date date=tmCase.getValidStartDate();
				if (date!=null){
					String invalidDateStart=DateTool.getDate(date);
					if (invalidDateStart!=null){
						mailMap.put("invalidDateStart", invalidDateStart);		
					}
				}
				//有效期截止日
				date=tmCase.getValidEndDate();
				if (date!=null){
					String invalidDateEnd=DateTool.getDate(date);
					if (invalidDateEnd!=null){
						mailMap.put("invalidDateEnd", invalidDateEnd);		
					}
				}				
				
//				mailMap.put("subject", "Trademark Application for "+ tmName +" in class "+ trademarkClass+" in China");	
			}
			
		}
	}
	
	
	
	public String getAvalibleGuanWen (String imputedGuanwen, String fileName, String caseType){
		String  guanwenName= "";
		if (caseType.equals("商标注册")){
			guanwenName=getRegistDoc(imputedGuanwen, fileName);
		}else if (caseType.equals("商标驳回复审")){
			guanwenName=getBoHuiDoc(imputedGuanwen, fileName);
		}else if(caseType.equals("商标异议申请")){
			guanwenName=getDissentApply(imputedGuanwen, fileName);
		}else if (caseType.equals("商标异议答辩")){
			guanwenName=getYydbDoc(imputedGuanwen, fileName);
		}else if (caseType.equals("商标不予注册复审")){
			guanwenName=getByzcfsDoc(imputedGuanwen, fileName);
		}else if (caseType.equals("参加不予注册复审")){
			guanwenName=getCanJiaByzcfsDoc(imputedGuanwen, fileName);
		}else if(caseType.equals("商标撤三申请")){
			guanwenName=getCheSanApply(imputedGuanwen, fileName);
		}else if (caseType.equals("商标撤三答辩")){
			guanwenName=getCheSanDefense(imputedGuanwen, fileName);
		}else if (caseType.equals("撤销商标复审")){
			guanwenName=getCheXiaoFuShenDoc(imputedGuanwen, fileName);
		}else if (caseType.equals("撤销复审答辩")){
			guanwenName=getCheXiaoFushenDefense(imputedGuanwen, fileName);
		}else if (caseType.equals("无效宣告")){
			guanwenName=getWuXiaoApply(imputedGuanwen, fileName);
		}else if (caseType.equals("无效宣告答辩")){
			guanwenName=getWuXiaoDefense(imputedGuanwen, fileName);
		}else{//续展，转让，变更类型案件
			guanwenName=getXzbDoc(imputedGuanwen, fileName);
		}
		
		//caseType为变更名称/地址， 相当于caseType为变更名义地址集体管理规则成员名单
		return guanwenName;
	}
	
	//续展，转让，变更类型案件可能录入的官文
	public String getXzbDoc(String imputedGuanwen, String fileName){
		String guanwenName="305,332";		
		if (imputedGuanwen!=null){
			//如果已经录入过官文，那么，只能录入下一个阶段的官文
			if (fileName!=null && fileName.equals("305")) {//补正通知
				guanwenName="332";
			}				
			else if (fileName!=null && fileName.equals("332")) {//受理通知
				guanwenName="320";
			}				
		}
		
		return guanwenName;
	}
	
	
	
	//商标注册案件可能录入的官文
	public String getRegistDoc(String imputedGuanwen, String fileName){
		String guanwenName="305,306,332";		
		if (imputedGuanwen!=null){
			//如果已经录入过官文，那么，只能录入下一个阶段的官文
			if (imputedGuanwen.indexOf("305")>-1 || imputedGuanwen.indexOf("306")>-1 || imputedGuanwen.indexOf("332")>-1){				
				if (fileName!=null && fileName.equals("305")) {//补正通知
					guanwenName="306,332";
				}
				else if (fileName!=null && fileName.equals("306")) {//不予受理通知书
					guanwenName="306";
				}
				else if (fileName!=null && fileName.equals("332")) {//受理通知
					guanwenName="302,303,316";
				}				
			}
			else if (imputedGuanwen.indexOf("302")>-1 || imputedGuanwen.indexOf("303")>-1
					|| imputedGuanwen.indexOf("316")>-1 ){				
				if (fileName!=null && fileName.equals("302") ||  fileName.equals("303")){//驳回通知
					guanwenName="316";					
				}
				else if (fileName!=null && fileName.equals("316")){//初步审定公告
					guanwenName="320,340";	                
				}
			}else{				
				if (fileName!=null && fileName.equals("340") ){//异议答辩通知
					guanwenName="320";
				}	
			}			
		}		
		return guanwenName;
	}
	
	
	
	//商标驳回复审可能录入的官文
	public String getBoHuiDoc(String imputedGuanwen, String fileName){
		String guanwenName="332";		
		if (imputedGuanwen!=null){
			//如果已经录入过332:受理通知，那么，只能录入下一个阶段的官文,325:评审意见书,349:裁定-赢,350:裁定-输,359:裁定-部分赢
			if (fileName.indexOf("332")>-1 ){	
				guanwenName="325,349,350,359";			
			}else if (fileName.indexOf("325")>-1 ){				
				guanwenName="349,350,359";		
			}
		}		
		return guanwenName;
	}
	
	
	
	
	//商标异议申请可能录入的官文
	public String getDissentApply(String imputedGuanwen,String fileName){
		String guanwenName = "305,332,342,503,504,505";
		if(imputedGuanwen !=null){
			//如果已经录入过305:补正通知，那么，只能录入下一个阶段的官文,332:受理通知书 ,
			//342:异议质证通知，504:异议决定_输, 503:异议决定_部分赢, 505:异议决定_赢
			if (fileName.indexOf("305")>-1 ){	
				guanwenName="332";			
			}else if(fileName.indexOf("332")>-1){
				guanwenName = "342,503,504,505";
			}else if(fileName.indexOf("342")>-1){
				guanwenName = "342,503,504,505";
			}else if(fileName.indexOf("505")>-1){
				guanwenName = "311"; //参加不予注册复审通知
			}
		}
		return guanwenName;
	}
	
	
	
	//商标异议答辩可能录入的官文
	public String getYydbDoc(String imputedGuanwen, String fileName){
		String guanwenName="305,342,503,504,505";		
		if (imputedGuanwen!=null){
			//如果已经录入过305:补正通知，那么，只能录入下一个阶段的官文,342:异议质证通知，351:异议答辩裁定-赢,或者352:异议答辩裁定-输
			if (fileName.indexOf("305")>-1 ){	
				guanwenName="342,351,503,504,505";			
			}else if(fileName.indexOf("342")>-1){//modify by zz 2018-11-09 342:异议质证通知可以录入多次
				guanwenName="342,503,504,505";	
			}
		}
		return guanwenName;
	}
	
	
	
	//商标不予注册复审可能录入的官文
	public String getByzcfsDoc(String imputedGuanwen, String fileName){
		String guanwenName="305,332";		
		if (imputedGuanwen!=null){
			//如果已经录入过343:证据交换通知，344:证据再交换通知，353:决定-赢,或者354:决定-输,或者355:部分赢
			if (fileName.indexOf("305")>-1 ){	
				guanwenName="332";			
			}
			if (fileName.indexOf("332")>-1 ){	
				guanwenName="343,344,353,354,355";			
			}
			if (fileName.indexOf("343")>-1 ){	
					guanwenName="344,353,354,355";			
			}
			if (fileName.indexOf("344")>-1 ){	
				guanwenName="353,354,355";			
			}
		}
		
		return guanwenName;
	}
	
	
	//参加不予注册复审可能录入的官文
	public String getCanJiaByzcfsDoc(String imputedGuanwen, String fileName){
		String guanwenName="305";		
		if (imputedGuanwen!=null){
			//353:决定-赢,或者354:决定-输,或者355:部分赢
			if (fileName.indexOf("305")>-1 ){
				guanwenName="353,354,355";			
			}			
		}
		
		return guanwenName;
	}
	
	
	
	
	//商标撤三申请可能录入的官文
	public String getCheSanApply(String imputedGuanwen,String fileName){
		String guanwenName = "305,332";
		if(imputedGuanwen !=null){
			//如果已经录入过305:补正通知，那么，只能录入下一个阶段的官文,
			//332:受理通知书 ,343:证据交换通知，344:证据再交换通知，
			//313:撤销决定-输，314:撤销决定--赢,或者 364:撤销决定-部分赢
			if (fileName.indexOf("305")>-1 ){	
				guanwenName="332";			
			}else if(fileName.indexOf("332")>-1){
				guanwenName="343,344,313,314,364";	
			}else if(fileName.indexOf("343")>-1){
				guanwenName = "344,313,314,364";
			}else if(fileName.indexOf("344")>-1){
				guanwenName = "313,314,364";
			}else if(fileName.indexOf("314")>-1){
				guanwenName = "312";//撤销复审答辩通知
			}
		}
		return guanwenName;
	}
	
	

	//商标商标撤三答辩可能录入的官文
	public String getCheSanDefense(String imputedGuanwen,String fileName){
		String guanwenName = "305,313,314,364";
		if(imputedGuanwen !=null){
			//314:决定-赢,或者313:决定-输,或者364:部分赢
			if (fileName.indexOf("305")>-1 ){	
				guanwenName="313,314,364";			
			}		
		}
		return guanwenName;
	}
	
	
	//商标撤销复审可能录入的官文
	public String getCheXiaoFuShenDoc(String imputedGuanwen, String fileName){
		String guanwenName="305,332";		
		if (imputedGuanwen!=null){
			//如果已经录入过343:证据交换通知，那么，只能录入下一个阶段的官文,344:证据再交换通知，358:决定-赢,或者357:决定-输,或者356:部分赢
			if (fileName.indexOf("305")>-1 ){	
				guanwenName="332";			
			}
			if (fileName.indexOf("332")>-1 ){	
				guanwenName="343,344,356,357,358";			
			}
			if (fileName.indexOf("343")>-1 ){	
					guanwenName="344,356,357,358";			
			}
			if (fileName.indexOf("344")>-1 ){	
				guanwenName="356,357,358";			
			}
		}
		
		return guanwenName;
	}
	
	
	//商标撤销复审答辩可能录入的官文
	public String getCheXiaoFushenDefense(String imputedGuanwen,String fileName){
		String guanwenName = "305,356,357,358";
		if(imputedGuanwen !=null){
			//如果已经录入过305:补正通知，那么，只能录入下一个阶段的官文,
			//358:决定-赢,或者357:决定-输,或者356:部分赢
			if (fileName.indexOf("305")>-1 ){	
				guanwenName="356,357,358";			
			}		
		}
		return guanwenName;
	}		
			
	

	//商标无效申请可能录入的官文
	public String getWuXiaoApply(String imputedGuanwen,String fileName){
		String guanwenName = "305,332,343,344,361,362,363";
		if(imputedGuanwen !=null){
			//如果已经录入过305:补正通知，那么，只能录入下一个阶段的官文,
			//332:受理通知书 ,343:证据交换通知，344:证据再交换通知，361:决定-输,或者 362:决定-部分赢，363:决定--赢
			if (fileName.indexOf("305")>-1 ){	
				guanwenName="332,343,344,361,362,363";			
			}else if(fileName.indexOf("332")>-1){
				guanwenName="343,344,361,362,363";	
			}else if(fileName.indexOf("343")>-1){
				guanwenName = "344,361,362,363";
			}else if(fileName.indexOf("344")>-1){
				guanwenName = "361,362,363";
			}else if(fileName.indexOf("363")>-1){
				guanwenName = "339"; //无效宣告答辩通知
			}
		}
		return guanwenName;
	}
	
	

	//商标商标无效答辩可能录入的官文
	public String getWuXiaoDefense(String imputedGuanwen,String fileName){
		String guanwenName = "305,361,362,363";
		if(imputedGuanwen !=null){
			//如果已经录入过305:补正通知，那么，只能录入下一个阶段的官文,361:决定-输,或者 362:决定-部分赢，363:决定--赢
			if (fileName.indexOf("305")>-1 ){	
				guanwenName="361,362,363";			
			}		
		}
		return guanwenName;
	}
	
	
	
	//按照申请人名称从申请人表中获取申请人信息，设置到案件对象中
	public void setApplicantProperty(TradeMarkCase tradeMarkCase, String tokenID){
		String applicantType=tradeMarkCase.getApplicantType();
		String appGJdq=tradeMarkCase.getAppGJdq();
		String appCnAddr=tradeMarkCase.getAppCnAddr();
		String appEnAddr=tradeMarkCase.getAppEnAddr();
		String appEnName=tradeMarkCase.getAppEnName();
		String appCnName=tradeMarkCase.getAppCnName();
		Integer caseTypeId=tradeMarkCase.getCaseTypeId();
		
		if (appCnName!=null && !appCnName.equals("")){	
			
			//Modification start, by yang guang, 2018-10-25
			//to get applicant by name
//			Applicant applicant=applicantService.queryApplicantByAppName(appCnName, tokenID);
			
			Applicant app=new Applicant();
			app.setApplicantName(appCnName);
			app.setApplicantEnName(appEnName);
			
			Applicant applicant=applicantService.getApplicantByName(app, tokenID);
			
			//Modification end
			
			if (applicant==null){
				return;
			}
			String appType=applicant.getAppType();
			String sendType=applicant.getSendType();
			String applicantAddress=applicant.getApplicantAddress();
			String applicantEnAddress=applicant.getApplicantEnAddress();
			String applicantEnName=applicant.getApplicantEnName();
			String applicantCnName=applicant.getApplicantName();
			
			if (applicantType==null || applicantType.endsWith("")){
				if (appType!=null && appType.equals("法人或其他组织") && (caseTypeId==1 || caseTypeId==6)){
					//商标注册，商标更正案件的文字有差别
					appType="法人或其它组织";
				}				
				tradeMarkCase.setApplicantType(appType);
			}
			
			if (appGJdq==null || appGJdq.endsWith("")){
				tradeMarkCase.setAppGJdq(sendType);
			}				
			if (appCnAddr==null || appCnAddr.endsWith("")){
				tradeMarkCase.setAppCnAddr(applicantAddress);
			}
			if (appEnAddr==null || appEnAddr.endsWith("")){
				tradeMarkCase.setAppEnAddr(applicantEnAddress);
			}
			if (appEnName==null || appEnName.endsWith("")){
				tradeMarkCase.setAppEnName(applicantEnName);
			}
			if (appCnName==null || appCnName.equals("")){
				tradeMarkCase.setAppCnName(applicantCnName);
			}
			
		}
	}
	
	
	
	public void dataSyn(GeneralCondition gcon, Integer agencyId, Integer caseId, Integer type){	
		//Modification start, 2018-10-17
		//支持将案件信息同步到wpm  
		//获取当前案件的agencyId
		if(Constants.DataSyn){		
			//获取北京万慧达知识产权代理有限公司的agencyId			
			Integer whdId=agencyService.getWhdAgencyId();
			if(agencyId!=null && whdId!=null && agencyId.intValue()==whdId.intValue()){						
				dataSynService.caseDataSyn(gcon, caseId.toString(), type);
			}
		}
		//Modification end
	}
	

}