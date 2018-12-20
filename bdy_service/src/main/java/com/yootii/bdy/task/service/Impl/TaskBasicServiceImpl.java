package com.yootii.bdy.task.service.Impl;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.apache.log4j.Logger;

import com.yootii.bdy.bill.model.Bill;
import com.yootii.bdy.common.Constants;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.Globals;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.customer.model.Customer;
import com.yootii.bdy.customer.service.CustomerService;
import com.yootii.bdy.mail.MailSenderInfo;
import com.yootii.bdy.model.User;
import com.yootii.bdy.process.service.ProcessService;
import com.yootii.bdy.security.service.AuthenticationService;
import com.yootii.bdy.task.dao.TaskRecordMapper;
import com.yootii.bdy.task.model.TaskRecord;
import com.yootii.bdy.task.model.TmCaseTaskToDoList;
import com.yootii.bdy.task.model.ToDoList;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseMapper;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.TradeMarkCaseProcess;
import com.yootii.bdy.tmcase.service.TradeMarkCaseProcessService;
import com.yootii.bdy.user.service.UserService;
import com.yootii.bdy.util.ServiceUrlConfig;
import com.yootii.bdy.util.TaskTool;

//---- 客户与代理机构公用接口实现类 -------
@Component
public class TaskBasicServiceImpl {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private ProcessService processService;
	@Resource
	private TaskRecordMapper taskRecordMapper;
	
	@Resource
	private TradeMarkCaseMapper tradeMarkCaseMapper;
	

	@Resource
	private ServiceUrlConfig serviceUrlConfig;

	@Resource
	private MailSenderInfo mailSenderInfo;
	
	@Resource
	private CustomerService customerService;
	@Resource
	private UserService userService;
	@Resource
	private AuthenticationService authenticationService;
	
	@Resource
	private TaskCommonImpl taskCommonImpl;
	
	@Resource
	private MultiProcessServiceImpl multiProcessServiceImpl;
	
	
	@Resource
	private TradeMarkCaseProcessService	tradeMarkCaseProcessService;
	
		
	
	public ReturnInfo startTmCaseProcess(Map<String, Object> map) {		
		
		ReturnInfo rtnInfo = new ReturnInfo();      		
		try {			
			rtnInfo = startProcess(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
		}	

		rtnInfo.setSuccess(true);
		return rtnInfo;
	}


	
	
	public ReturnInfo startBillReviewProcess(GeneralCondition gcon, String userId, String customerId,  Bill bill) {
		ReturnInfo rtnInfo = new ReturnInfo();
		String prokey="billreview";
		String taskName="账单审核:启动";
		
		String permission;
		try {
			permission = TaskTool.getPermission(taskName);
			rtnInfo = startBillProcess(gcon,  userId, customerId, bill, permission, prokey);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
		}
		
		rtnInfo.setSuccess(true);
		return rtnInfo;
	}
		
	
	public ReturnInfo startBillProcess(GeneralCondition gcon, String userId, String customerId, Bill bill, String permission, String prokey) {
		ReturnInfo rtnInfo = new ReturnInfo();
			
		try {
			//权限测试			
			checkUser(gcon, userId, null, permission);
			
			Map<String, Object> runMap = new HashMap<String, Object>();
			
			Integer billId=bill.getBillId()	;
			String depName=bill.getGroupName();		
			Integer agencyId=bill.getAgencyId();
			
			String tokenID=gcon.getTokenID();
			
			//指定案件的修改人/提交人
			runMap.put("userId", userId);
			
			//任务种类
			runMap.put("todotasktype", 2);
			
			//获取任务候选人的时候需要这三个参数
			runMap.put("permission", permission);
			runMap.put("firstAgencyId", agencyId.toString());	
			runMap.put("agencyId", agencyId.toString());			
			runMap.put("tokenID", tokenID);
			runMap.put("billId", billId.toString());
			runMap.put("depName", depName);
			runMap.put("remarks", "startbill");
			
		
			//启动流程			
			rtnInfo = processService.startProcessByKey(prokey, runMap);
			if(!rtnInfo.getSuccess()) {
				throw new Exception("流程启动失败|"+rtnInfo.getMessage());
			}
			
			
			rtnInfo.setSuccess(true);

			
		} catch (Exception e) {
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			return rtnInfo;
		}

		return rtnInfo;
	}
	
	
	
	
	
	private void checkUser(GeneralCondition gcon,String userId, String customerId, String permission) throws Exception {
		if ((userId==null || userId.equals("")) && (customerId==null || customerId.equals(""))){
			throw new Exception("userId与customerId不能都为空");
		}
//	    if ((userId!=null && !userId.equals("")) && (customerId!=null && !customerId.equals(""))){
//	    	throw new Exception("userId与customerId必须有一个为空");
//		}
		
		if (userId!=null){
			ReturnInfo checkret = processService.checkuserstart(permission, userId, gcon);
			if(!checkret.getSuccess()) {
				throw new Exception(checkret.getMessage());
			}
		}
		
	}


	public ReturnInfo startProcess(Map<String, Object> map) {
		ReturnInfo rtnInfo = new ReturnInfo();
		
		GeneralCondition gcon=(GeneralCondition)map.get("gcon");
        String userId=(String)map.get("userId");
        String customerId=(String)map.get("customerId");
        String agencyServiceId=(String)map.get("agencyServiceId");
        Boolean obj=(Boolean)map.get("appSelf");
        boolean appSelf=obj.booleanValue();
        TradeMarkCase tmCase=(TradeMarkCase)map.get("tmCase");
        String permission=(String)map.get("permission");
        String prokey=(String)map.get("prokey");
        String processInstanceId=(String)map.get("processInstanceId");
        String agencyLevel=(String)map.get("agencyLevel");
//        Boolean submitresultObj=(Boolean)map.get("submitresult");
//        boolean submitresult=submitresultObj.booleanValue();
        
		try {
			//权限测试
			checkUser(gcon, userId, customerId, permission);
			
			Map<String, Object> runMap = new HashMap<String, Object>();
					
			Integer caseId=tmCase.getId();			
			Integer agencyId=tmCase.getAgencyId();			

			//任务种类
			runMap.put("todotasktype", 1);			

			//指定案件的修改人/提交人			
			if (userId==null || userId.equals("")){	
				runMap.put("agentUserId", "");
			}else{
				runMap.put("userId", userId);
				runMap.put("agentUserId", userId);				
			}		
			if (customerId!=null){			
				String cId=Constants.customer_prefix+customerId;
				runMap.put("custId", cId);
			}
			
			//获取任务候选人的时候需要这个参数
			runMap.put("permission", permission);
			
			if (processInstanceId!=null && !processInstanceId.equals("")){
				//为新启动的流程设置parentProcessInstanceId
				runMap.put("parentProcessInstanceId", processInstanceId);	
				logger.info("set parentProcessInstanceId: "+ processInstanceId);
			}else{
				runMap.put("firstAgencyId", agencyId.toString());
			}
			
			if (agencyLevel!=null){
				runMap.put("agencyLevel", agencyLevel);	
			}else{
				runMap.put("agencyLevel", "1");	
			}
			
			runMap.put("agencyId", agencyId.toString());
			runMap.put("caseId", caseId.toString());
			runMap.put("remarks", "startcase");
			runMap.put("agencyServiceId", agencyServiceId);
			runMap.put("appSelf", appSelf);
			
			//为了测试时，流程能走通，暂且设置该变量
//			runMap.put("submitresult", submitresult);
			
			
//			logger.info("runMap:"+ runMap);
		
			//启动流程			
			rtnInfo = processService.startProcessByKey(prokey, runMap);
			if(!rtnInfo.getSuccess()) {
				throw new Exception("流程启动失败|"+rtnInfo.getMessage());
			}
			
			//设置该案件的流程实例的Id
			Map<String, Object> resData = (Map<String, Object>) rtnInfo.getData();			
			String processId=resData.get("ProcessInstanceId").toString();			
			TradeMarkCase tradeMarkCase=new TradeMarkCase();
			tradeMarkCase.setId(caseId);
			tradeMarkCase.setProcessId(processId);			
			tradeMarkCaseMapper.updateByPrimaryKeySelective(tradeMarkCase);
		
			//启动流程成功			
			rtnInfo.setSuccess(true);

			
		} catch (Exception e) {
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			return rtnInfo;
		}

		return rtnInfo;
	}	
		
	
	
	
	// 拒绝接口
	public ReturnInfo refuse(GeneralCondition gcon, String userId,
			String customerId, TmCaseTaskToDoList toDoList, Map<String, Object> proMap) {
		
		ReturnInfo rtnInfo = new ReturnInfo();
		try{
			Integer tId = toDoList.getTaskId();
			if (tId == null) {
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("待办事项的id不能为空");
				rtnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
				return rtnInfo;
			}
			// 检查必要的参数
			rtnInfo = TaskTool.checkUserParam(null, customerId);
			if (!rtnInfo.getSuccess()) {
				return rtnInfo;
			}
			String taskId = tId.toString();			
			String remarks = toDoList.getRemarks();		

			String key = "approved";
			boolean value = false;
			String permission = "";
			String taskResult="拒绝";
	
			Map<String, Object> runMap = new HashMap<String, Object>();		
			runMap.put(key, value);
			runMap.put("result", true);	
			String customerDecition=value+"";	
			
			// 获取流程参数	
			if (proMap==null || proMap.size()==0){
				proMap = taskCommonImpl.getProcessVarialble(taskId);
			}
			
			// 设置任务执行所必须的参数			
			if (permission != null && !permission.equals("")) {
				runMap.put("permission", permission);
			}
			if (remarks != null && !remarks.equals("")) {
				runMap.put("remarks", remarks);
			}
			rtnInfo = doTask(gcon, userId, customerId, toDoList,
				 taskResult, proMap, runMap);
		
			String fileName=(String)proMap.get("fileName");			
			String caseId=(String)proMap.get("caseId");
			
			String tokenID=gcon.getTokenID();
			
		
			if (caseId!=null){		
				Integer id=new Integer(caseId);			
				//获取案件状态
				String caseStatus=TaskTool.getCaseStatus(fileName, true, null);				
				if(caseStatus!=null){
					TradeMarkCase tradeMarkCase=new TradeMarkCase();			
					tradeMarkCase.setId(id);	
					tradeMarkCase.setStatus(caseStatus);
					Date statusDate=new Date();
					tradeMarkCase.setStatusDate(statusDate);
					
					//更新案件状态
					taskCommonImpl.recordTmCaseStatus(tradeMarkCase);
				}			
				Integer uId=0;
				Integer custId=0;
				String userName=null;
				if (userId!=null && !userId.equals("")){			
					User user=userService.getUserById(userId, tokenID);
					uId=new Integer(userId);
					userName=user.getUsername();
					caseStatus="客户回复：";
				}else if (customerId!=null && !customerId.equals("")){			
					Customer customer=customerService.getCustById(customerId, tokenID);
					custId=new Integer(customerId);
					userName=customer.getUsername();	
					caseStatus="客户回复：";
				}
				String operation="放弃";
				if (fileName!=null){
					String realFileName=Constants.fileNameList.get(fileName);				
					caseStatus=caseStatus+operation+realFileName;
					/*
					if(realFileName!=null ){
						int pos=realFileName.indexOf("通知");
						if (pos>-1){
							caseStatus=caseStatus+realFileName.substring(0,pos);
						}
					}
					*/
				}				
				
				//记录案件的处理流程
				taskCommonImpl.recordTmCaseProcess(id, caseStatus, userName, uId, custId, 1);
			}	
			
//			if(customerId!=null && !customerId.equals("")){
//				//发送客户决定的消息，使等该该消息的商标注册流程捕获该消息后继续执行
//				String lastProcessInstanceId=taskCommonImpl.getProperyOfProcess(proMap, taskId, "processInstanceId");	
//				multiProcessServiceImpl.notifyCustomerDecition(gcon, null, lastProcessInstanceId, "2", customerDecition, fileName);			
//			}
	
		} catch (Exception e) {
			// 关闭流程
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			return rtnInfo;
		}
		return rtnInfo;
	}
	
	
	
		
	//给上一级流程发消息，让处于等待消息状态的上一级流程恢复运行	
	public ReturnInfo resumeProcess(String parentProcessInstanceId, String activityId, String messageName, Map<String, Object> runMap){
	
		return processService.resumeProcess(parentProcessInstanceId,  activityId,  messageName, runMap);
	}
	
	
	public void feedBackDocResult(Map<String, Object> proMap, Map<String, Object> runMap){
		Object obj=proMap.get("fileName");
		if (obj!=null){
			String fileName = obj.toString();
			
			if(fileName.equals("349") || fileName.equals("350") ){					
				obj=proMap.get("parentProcessInstanceId");				
				if (obj!=null){
					String parentProcessInstanceId=obj.toString();
					
					//如果当前流程是驳回复审流程，异议答辩流程，不予注册复审流程，并且已经有了最终的结果（上述裁定书）
					//并且，如果这些案件流程是由商标注册流程启动的(parentProcessInstanceId!=null)
					//那么，可以让处于等待消息状态的商标注册流程恢复运行
					String activityId="catchRejectResultMessage";
					String messageName="rejectResultMessage"; 
					resumeProcess(parentProcessInstanceId, activityId, messageName, runMap);
				}					
			}else if( fileName.equals("351") || fileName.equals("352")){					
				obj=proMap.get("parentProcessInstanceId");				
				if (obj!=null){
					String parentProcessInstanceId=obj.toString();
					
					//如果当前流程是驳回复审流程，异议答辩流程，不予注册复审流程，并且已经有了最终的结果（上述裁定书）
					//并且，如果这些案件流程是由商标注册流程启动的(parentProcessInstanceId!=null)
					//那么，可以让处于等待消息状态的商标注册流程恢复运行
					String activityId="catchObjectionDefenseResultMessage";
					String messageName="objectionDefenseResultMessage"; 
					resumeProcess(parentProcessInstanceId, activityId, messageName, runMap);
				}					
			}else if(fileName.equals("353") || fileName.equals("354")|| fileName.equals("355")){					
				obj=proMap.get("parentProcessInstanceId");				
				if (obj!=null){
					String parentProcessInstanceId=obj.toString();
					
					//如果当前流程是驳回复审流程，异议答辩流程，不予注册复审流程，并且已经有了最终的结果（上述裁定书）
					//并且，如果这些案件流程是由商标注册流程启动的(parentProcessInstanceId!=null)
					//那么，可以让处于等待消息状态的商标注册流程恢复运行
					String activityId="catchNotAllowedToRegisterResultMessage";
					String messageName="notAllowedToRegisterResultMessge"; 
					resumeProcess(parentProcessInstanceId, activityId, messageName, runMap);
				}					
			}
		}
	}
	
	
	
	// 代理人通用执行任务接口
		public ReturnInfo userDoTask(GeneralCondition gcon, String userId,
				ToDoList toDoList, String taskResult, Map<String, Object> proMap, Map<String, Object> runMap) {
			ReturnInfo rtnInfo = new ReturnInfo();
			try {
				Integer tId = toDoList.getTaskId();
				String remarks = toDoList.getRemarks();
				String taskId = tId.toString();		
				
				String taskName =(String) proMap.get("taskName");
			
				// 每个任务名称，对应一个权限
				String currentTaskPermission = TaskTool.getPermission(taskName);

				// 检查当前用户是否有权限执行该任务
				ReturnInfo checkret = processService.checkuserstart(
						currentTaskPermission, userId, gcon);
				if (!checkret.getSuccess()) {
					throw new Exception(checkret.getMessage());
				}

				// 执行任务
				rtnInfo = processService.doTask(taskId, runMap);
				if (!rtnInfo.getSuccess()) {
					throw new Exception("流程执行任务失败|" + rtnInfo.getMessage());
				}
				
				// 任务执行记录
				//根据taskResult是否为空来判断是否记录任务执行记录
//				if (taskResult!=null){
				
					String username = authenticationService.getUsername(gcon);//创建人
					String caseId=taskCommonImpl.getProperyOfProcess(proMap, taskId, "caseId");
					Integer cId=new Integer(caseId);
					
					saveTaskRecord(cId, tId, taskName, taskResult, remarks, username);
					
//				}

				rtnInfo.setSuccess(true);
				
			} catch (Exception e) {
				// 关闭流程
				e.printStackTrace();
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
			return rtnInfo;
		}
		
		
		// 执行任务接口
		public ReturnInfo userDoTask(
				String taskId, String username, String taskResult, Map<String, Object> proMap, Map<String, Object> runMap) {
			ReturnInfo rtnInfo = new ReturnInfo();
			try {
							
				Integer tId=new Integer(taskId);
				String taskName =(String) proMap.get("taskName");					

				// 执行任务
				rtnInfo = processService.doTask(taskId, runMap);
				if (!rtnInfo.getSuccess()) {
					throw new Exception("流程执行任务失败|" + rtnInfo.getMessage());
				}				
					 
				String caseId=taskCommonImpl.getProperyOfProcess(proMap, taskId, "caseId");
				Integer cId=new Integer(caseId);
				
				String remarks="";
				
				saveTaskRecord(cId, tId, taskName, taskResult, remarks, username);
					
				rtnInfo.setSuccess(true);
				
			} catch (Exception e) {
				// 关闭流程
				e.printStackTrace();
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
			return rtnInfo;
		}
				
		

		// 代理人通用执行任务接口
		public ReturnInfo userDoBillTask(GeneralCondition gcon, String userId,
				ToDoList toDoList, String permission, String taskResult, Map<String, Object> runMap) {
			ReturnInfo rtnInfo = new ReturnInfo();
			try {
				Integer tId = toDoList.getTaskId();
				String remarks = toDoList.getRemarks();
//				String tokenID=gcon.getTokenID();

				// 检查必要的参数
				rtnInfo = TaskTool.checkTaskParam(userId, tId);
				if (!rtnInfo.getSuccess()) {
					return rtnInfo;
				}

				String taskId = tId.toString();

				
				// 获取流程参数
				ReturnInfo rtInfo = processService.showtaskvariables(taskId);
				if (rtInfo.getSuccess() != true) {
					throw new Exception("获取流程中的参数失败|" + rtInfo.getMessage());
				}
				Map<String, Object> proMap = (Map<String, Object>) rtInfo.getData();
				if(proMap==null){
					throw new Exception("获取流程中的参数结果为空, taskId:" +taskId);
				}
				
				Object obj=proMap.get("taskName");
				if (obj==null){
					throw new Exception("获取流程中的taskName结果为空, taskId:" +taskId);
				}
				String taskName = obj.toString();
				
				
				boolean audited=false;
				Object auditedObj=runMap.get("audited");
				if (auditedObj!=null){
					audited=((Boolean)auditedObj).booleanValue();
				}
				
				//只有审核通过，才需要执行流程
				if (audited){
				
					obj=proMap.get("userId");
					if (obj==null){
						throw new Exception("获取流程中的userId结果为空, taskId:" +taskId);
					}
									
					String creatUserId = obj.toString();
					//customerId=customerId.substring(Constants.customer_prefix.length());
					
					obj=proMap.get("billId");
					if (obj==null){
						throw new Exception("获取流程中的billId结果为空, taskId:" +taskId);
					}
					String billId = obj.toString();
				
					
					// 每个任务名称，对应一个权限
					String currentTaskPermission = TaskTool.getPermission("账单审核:"+taskName);
	
					// 检查当前用户是否有权限执行该任务
					ReturnInfo checkret = processService.checkuserstart(
							currentTaskPermission, userId, gcon);
					if (!checkret.getSuccess()) {
						throw new Exception(checkret.getMessage());
					}
	
					// 设置任务执行所必须的参数			
					if (permission != null && !permission.equals("")) {
						runMap.put("permission", permission);
					}
					if (remarks != null && !remarks.equals("")) {
						runMap.put("remarks", remarks);
					}
						
	
					// 执行任务
					rtnInfo = processService.doTask(taskId, runMap);
					if (!rtnInfo.getSuccess()) {
						throw new Exception("流程执行任务失败|" + rtnInfo.getMessage());
					}
					
					//为了在当前方法外面使用caseId，将caseId保存到runMap中
					if (billId != null && !billId.equals("")) {
						runMap.put("billId", billId);
					}
				
				}
				
				
				String username = null;
				
				// 任务执行记录
				//根据taskResult是否为空来判断是否记录任务执行记录
				if (taskResult!=null){
					TaskRecord taskRecord=new TaskRecord();
//					Integer cId=new Integer(billId);
//					taskRecord.setCaseId(cId);
					taskRecord.setTaskId(tId);
					taskRecord.setTaskName(taskName);
					username = authenticationService.getUsername(gcon);//创建人
					taskRecord.setTaskUser(username);
					taskRecord.setTaskResult(taskResult);
					Date taskTime=new Date();
					taskRecord.setTaskTime(taskTime);
					taskRecord.setRemarks(remarks);
					taskRecordMapper.insertSelective(taskRecord);
				}
							
				
//				if (username != null && !username.equals("")) {
//					runMap.put("username", caseId);
//				}
				
				

				rtnInfo.setSuccess(true);
				
			} catch (Exception e) {
				// 关闭流程
				e.printStackTrace();
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
			return rtnInfo;
		}
		
		
		

		// 客户通用执行任务接口
		public ReturnInfo doTask(GeneralCondition gcon, String userId,
				String customerId, TmCaseTaskToDoList toDoList, String taskResult, Map<String, Object> proMap, Map<String, Object> runMap) {
			ReturnInfo rtnInfo = new ReturnInfo();

			try {

				Integer tId = toDoList.getTaskId();
				String remarks = toDoList.getRemarks();


				String taskId = tId.toString();
				// 获取流程参数				
				String taskName = proMap.get("taskName").toString();

				if (userId != null) {
					// 每个任务名称，对应一个权限
					String currentTaskPermission = TaskTool.getPermission(taskName);

					// 检查当前用户是否有权限执行该任务
					ReturnInfo checkret = processService.checkuserstart(
							currentTaskPermission, userId, gcon);
					if (!checkret.getSuccess()) {
						throw new Exception(checkret.getMessage());
					}
				}
				
				Object obj=proMap.get("caseId");
				if (obj==null){
					throw new Exception("获取流程中的caseId结果为空, taskId:" +taskId);
				}
				String caseId = (String)proMap.get("caseId");

				// 执行任务
				rtnInfo = processService.doTask(taskId, runMap);
				if (!rtnInfo.getSuccess()) {
					throw new Exception("流程执行任务失败|" + rtnInfo.getMessage());
				}
				
				// 任务执行记录
				String username = authenticationService.getUsername(gcon);//创建人				
				Integer cId=new Integer(caseId);				
				saveTaskRecord(cId, tId, taskName, taskResult, remarks, username);

				rtnInfo.setSuccess(true);

			} catch (Exception e) {
				e.printStackTrace();
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}

			return rtnInfo;
		}
		
		
		// 客户通用执行任务接口
		public ReturnInfo doTaskByPro(GeneralCondition gcon, String userId,
				String taskResult, String remarks, String proId, String caseId, Map<String, Object> runMap) {
			ReturnInfo rtnInfo = new ReturnInfo();

			try {
				
				// 执行任务				
				rtnInfo = processService.doTaskByPro(proId, runMap);
				if (!rtnInfo.getSuccess()) {
					throw new Exception("流程执行任务失败|" + rtnInfo.getMessage());
				}
				
				Map<String, Object> proMap = (Map<String, Object>) rtnInfo.getData();
				
				String taskId=(String)proMap.get("taskId");
				String taskName=(String)proMap.get("taskName");
				Integer tId=new Integer(taskId);
				
				// 任务执行记录
				TaskRecord taskRecord=new TaskRecord();
				Integer cId=new Integer(caseId);
				taskRecord.setCaseId(cId);
				taskRecord.setTaskId(tId);
				taskRecord.setTaskName(taskName);
				String username = userId;//创建人
				taskRecord.setTaskUser(username);
				taskRecord.setTaskResult(taskResult);
				Date taskTime=new Date();
				taskRecord.setTaskTime(taskTime);
				taskRecord.setRemarks(remarks);				
				taskRecordMapper.insertSelective(taskRecord);
				rtnInfo.setSuccess(true);

			} catch (Exception e) {
				e.printStackTrace();
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}

			return rtnInfo;
		}
		

		
		public ReturnInfo modifyCaseStatus(GeneralCondition gcon, String userId, String caseId, String fileName, String refuse, String caseResult, String processInstanceId){
			ReturnInfo rtnInfo = new ReturnInfo();
			try {
				String name="caseId";
				String value=caseId;
				rtnInfo =TaskTool.checkId(name, value);			
				if (!rtnInfo.getSuccess()) {
					return rtnInfo;
				}
				rtnInfo =TaskTool.checkFileName(fileName);		
				if (!rtnInfo.getSuccess()) {
					return rtnInfo;
				}
				Integer id=new Integer(caseId);
				//Modification start, 2018-12-05
				boolean refuseResult=false;
				if (refuse!=null && refuse.equals("true")){
					refuseResult=true;
				}
				//Modification end
				String caseStatus=TaskTool.getCaseStatus(fileName, refuseResult, caseResult);
				if (caseStatus!=null){
					TradeMarkCase tradeMarkCase=new TradeMarkCase();	
					
					tradeMarkCase.setId(id);	
					tradeMarkCase.setStatus(caseStatus);
					Date statusDate=new Date();
					tradeMarkCase.setStatusDate(statusDate);					
					taskCommonImpl.recordTmCaseStatus(tradeMarkCase);	
					
					//通知上级案件，当前案件的状态
//					TradeMarkCase tmCase=tradeMarkCaseMapper.selectByPrimaryKey(id);				
//					String caseType=tmCase.getCaseType();
//					boolean success=false;
//					if (caseType.equals("商标驳回复审") || caseType.equals("商标异议答辩") || caseType.equals("商标不予注册复审") || caseType.equals("商标诉讼案件")){			
//						if (caseStatus.equals("成功")){
//							success=true;
//						}					
//						multiProcessServiceImpl.notifyChildCaseResult(gcon, caseType, success, processInstanceId);	
//					}	
				}	
				
							

			} catch (Exception e) {
				e.printStackTrace();
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
			return rtnInfo;
		}
		
		
		
		
		// 拒绝接口
		public ReturnInfo resetTask(GeneralCondition gcon,String appCnName) {
			
			ReturnInfo rtnInfo = new ReturnInfo();
			try{		
				
				Integer submitStatus = 2;	
				
				List<TradeMarkCase>list=tradeMarkCaseMapper.selectProcessId(appCnName);	
				
				for(TradeMarkCase tmCase:list){
					String processId=tmCase.getProcessId();
					
					logger.info("resetTask processId: " + processId);
					
					multiProcessServiceImpl.notifyAppResult(null, submitStatus.toString(), processId);
					
					logger.info("resetTask end");
					
					TradeMarkCaseProcess tradeMarkCaseProcess = new TradeMarkCaseProcess();
					tradeMarkCaseProcess.setCaseId(tmCase.getId());
					tradeMarkCaseProcess.setUsername("系统");
					tradeMarkCaseProcess.setUserId(0);
					tradeMarkCaseProcess.setCustId(0);
					tradeMarkCaseProcess.setLevel(1);	
					tradeMarkCaseProcess.setStatus("网申提交失败");
					tradeMarkCaseProcess.setSubmitStatus("提交失败");
					tradeMarkCaseProcess.setFailReason("超时未返回结果"); 					
					tradeMarkCaseProcessService.createTradeMarkCaseProcess(tradeMarkCaseProcess);
					
				}
				
				rtnInfo.setSuccess(true);
				rtnInfo.setMessage("案件待办任务重置成功");
				
			} catch (Exception e) {
				// 关闭流程
				e.printStackTrace();
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
			return rtnInfo;
		}
		
		
		
		public void saveTaskRecord(Integer cId, Integer tId, String taskName, String taskResult, String remarks, String username){
			
			TaskRecord taskRecord=new TaskRecord();					
			taskRecord.setCaseId(cId);
			taskRecord.setTaskId(tId);
			taskRecord.setTaskName(taskName);					
			taskRecord.setTaskUser(username);
			taskRecord.setTaskResult(taskResult);
			Date taskTime=new Date();
			taskRecord.setTaskTime(taskTime);
			taskRecord.setRemarks(remarks);
			taskRecordMapper.insertSelective(taskRecord);
		}

}