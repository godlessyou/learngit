package com.yootii.bdy.task.service.Impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import javax.annotation.Resource;

import com.sun.org.apache.bcel.internal.generic.FNEG;
import com.yootii.bdy.activemq.ProducerService;
import com.yootii.bdy.bill.model.Bill;
import com.yootii.bdy.bill.service.BillService;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.Globals;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.agency.service.AgencyService;
import com.yootii.bdy.ipservice.service.AgencyServiceService;
import com.yootii.bdy.material.dao.MaterialMapper;
import com.yootii.bdy.material.model.Material;
import com.yootii.bdy.material.model.MaterialCondition;
import com.yootii.bdy.model.User;
import com.yootii.bdy.process.service.ProcessService;
import com.yootii.bdy.remind.model.Remind;
import com.yootii.bdy.remind.service.RemindService;
import com.yootii.bdy.security.service.AuthenticationService;
import com.yootii.bdy.task.dao.TaskRecordMapper;
import com.yootii.bdy.task.model.TmCaseTaskToDoList;
import com.yootii.bdy.task.model.ToDoList;
import com.yootii.bdy.task.service.TradeMarkCaseTaskService;
import com.yootii.bdy.tmcase.dao.TradeMarkAssociationMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCasePreMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseProcessMapper;
import com.yootii.bdy.tmcase.model.TradeMarkAssociation;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.TradeMarkCasePre;
import com.yootii.bdy.tmcase.model.TradeMarkCaseProcess;
import com.yootii.bdy.tmcase.service.TradeMarkCaseService;
import com.yootii.bdy.trademark.service.TradeMarkService;
import com.yootii.bdy.user.service.UserService;
import com.yootii.bdy.util.DateTool;
import com.yootii.bdy.util.GraspUtil;
import com.yootii.bdy.util.JsonUtil;
import com.yootii.bdy.util.ServiceUrlConfig;
import com.yootii.bdy.util.TaskTool;
import com.yootii.bdy.common.Constants;
import com.yootii.bdy.customer.model.Customer;
import com.yootii.bdy.customer.service.CustomerService;
import com.yootii.bdy.datasyn.service.DataSynService;

import org.springframework.stereotype.Component;


//---- 代理机构使用的接口实现类 -------
@Component
public class AgencyTaskServiceImpl {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private ProcessService processService;
	
	@Resource
	private TradeMarkCaseMapper tradeMarkCaseMapper;
	
	@Resource
	private TradeMarkCaseProcessMapper tradeMarkCaseProcessMapper;

	@Resource
	private ServiceUrlConfig serviceUrlConfig;

	@Resource
	private TaskCommonImpl taskCommonImpl;
	
	@Resource
	private TaskBasicServiceImpl taskBasicServiceImpl;
	
	@Resource
	private AuthenticationService authenticationService;
	
	@Resource
	private MultiProcessServiceImpl multiProcessServiceImpl;
	
	@Resource
	private UserService userService;
	
	@Resource
	private CustomerService customerService;
	
	@Resource
	private ProducerService producerService;
	
	@Resource
	private	CaseChargeRecordServiceImpl caseChargeRecordServiceImpl;
	
	@Resource
	private BillService billService;	
	
	@Resource
	private TradeMarkAssociationMapper tradeMarkAssociationMapper;
	
	@Resource
	private AgencyServiceService agencyServiceService;
	
	@Resource
	private MaterialMapper materialMapper;
	
	@Resource
	private TradeMarkCaseService tradeMarkCaseService;
	@Resource
	private RemindService remindService;
	@Resource
	private TradeMarkCaseTaskService tradeMarkCaseTaskService;
	
	@Resource
	private AgencyService agencyService;	
	
	@Resource
	private DataSynService dataSynService;
	
	@Resource
	private TaskRecordMapper taskRecordMapper;
	

	// ---- 代理机构使用的接口 -------
	// 处理案件，几种可能的处理方式：转发案件，接收案件，拒绝案件
	// 转发案件接口
	public ReturnInfo assginCase(GeneralCondition gcon, String userId,
			String agencyId, String transfer, TmCaseTaskToDoList toDoList, Map<String, Object> proMap) {
		
		ReturnInfo rtnInfo = new ReturnInfo();
		try{
			Integer tId = toDoList.getTaskId();
			// 检查必要的参数
			rtnInfo = TaskTool.checkTaskParam(userId, tId);
			if (!rtnInfo.getSuccess()) {
				return rtnInfo;
			}
			if (transfer == null || transfer.equals("")) {
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("transfer不能为空");
				rtnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
				return rtnInfo;
			}
			String taskId = tId.toString();			
			String remarks = toDoList.getRemarks();		
		
			String key="transfer";
			String value=transfer;
			String permission="案件分配";	
			
					
			// 获取流程参数	
			if (proMap==null || proMap.size()==0){
				proMap = taskCommonImpl.getProcessVarialble(taskId);
			}
		    String caseId= taskCommonImpl.getProperyOfProcess(proMap, taskId, "caseId");
		    
		    String taskName=taskCommonImpl.getProperyOfProcess(proMap, taskId, "taskName");
				
			Map<String, Object> runMap = new HashMap<String, Object>();		
			runMap.put(key, value);		
			String taskResult="";
			if(transfer.equals("1")){
				taskResult="案件分配给代理机构"+ agencyId+"处理";
				runMap.put("assginedAgency", agencyId);
			}
			else if(transfer.equals("2")){
				taskResult="案件由本代理机构处理";
				permission="审核案件";	
				
				String agencyLevel=taskCommonImpl.getProperyOfProcess(proMap, taskId, "agencyLevel");
				String processInstanceId=taskCommonImpl.getProperyOfProcess(proMap, taskId, "processInstanceId");
				int level= Integer.parseInt(agencyLevel);
				level++;
				agencyLevel=level+"";
				//为新启动的callactivity类型的流程设置agencyLevel和parentProcessInstanceId
				runMap.put("agencyLevel", agencyLevel);				
				runMap.put("parentProcessInstanceId", processInstanceId);
				
			}
			else if(transfer.equals("3")){
				taskResult="拒绝处理";
			}
			
			
			// 设置任务执行所必须的参数			
			if (permission != null && !permission.equals("")) {
				runMap.put("permission", permission);
			}
			if (remarks != null && !remarks.equals("")) {
				runMap.put("remarks", remarks);
			}
			
			//注意：必须将代理人的信息保存到流程中，后续需要使用
			runMap.put("userId", userId);
			
			
			rtnInfo = taskBasicServiceImpl.userDoTask( gcon,userId,toDoList,taskResult,proMap,runMap);
			
			if (rtnInfo==null || !rtnInfo.getSuccess()){
				return rtnInfo;
			}
			
			//Modification start, by yang guang, 2018-10-17
			//in order to return taskId, pageId
			if(transfer.equals("2")){	
				
				//获取该案件的处于流程中的task的Id
				String caseTypeIds="1,2";
				List<Map<String, Object>> taskDataList=processService.queryTaskProperty(caseId, caseTypeIds);
				
				if (taskDataList!=null && taskDataList.size()>0){
					Map<String, Object> data=taskDataList.get(0);					
					String taskIdString=(String)data.get("taskId");	
					taskName=(String)data.get("taskName");
					toDoList.setTaskName(taskName);
					if (taskIdString!=null){
						Integer tkId=Integer.parseInt(taskIdString);
						toDoList.setTaskId(tkId);
					}else{
						rtnInfo.setSuccess(false);
						rtnInfo.setMessage("接受案件失败");
						return rtnInfo;
					}
				}
				
				TradeMarkCase tmcase=new TradeMarkCase();
				Integer tmCaseId=Integer.parseInt(caseId);
				tmcase.setId(tmCaseId);					
				Integer pageId=null;
				if (userId!=null && !userId.equals("")){
					pageId=1;
				}else{
					pageId=13;
				}				
				String customerId=null;
				rtnInfo=tradeMarkCaseTaskService.queryCaseDaiban(userId, customerId, pageId, tmcase, toDoList);	
			}
			//Modification end
			
			
			// 给客户发送邮件			
//			taskCommonImpl.sendMail(proMap, taskId, null, gcon, userId, null);
		 
		} catch (Exception e) {
			// 关闭流程
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			return rtnInfo;
		}
		return rtnInfo;
	}
	


	// 这是以前的接口：暂时无用。 审核不通过接口
	public ReturnInfo notAudited(GeneralCondition gcon, String userId,
			TmCaseTaskToDoList toDoList) {
		
		ReturnInfo rtnInfo = new ReturnInfo();
		try{
			Integer tId = toDoList.getTaskId();
			// 检查必要的参数
			rtnInfo = TaskTool.checkTaskParam(userId, tId);
			if (!rtnInfo.getSuccess()) {
				return rtnInfo;
			}
			String taskId = tId.toString();			
			String remarks = toDoList.getRemarks();	
			String key="audited";
			boolean value=false;
			String permission="审核案件";	
				
			Map<String, Object> runMap = new HashMap<String, Object>();		
			runMap.put(key, value);	
			boolean result=true;
			runMap.put("result", result);
			
			String taskResult="审核不通过";
			// 获取流程参数			
			Map<String, Object> proMap = taskCommonImpl.getProcessVarialble(taskId);
			// 设置任务执行所必须的参数			
			if (permission != null && !permission.equals("")) {
				runMap.put("permission", permission);
			}
			if (remarks != null && !remarks.equals("")) {
				runMap.put("remarks", remarks);
			}
			
			//注意：必须将代理人的信息保存到流程中，后续需要使用
			runMap.put("userId", userId);
			rtnInfo = taskBasicServiceImpl.userDoTask( gcon,userId,toDoList,taskResult,proMap,runMap);
			
			// 给客户发送邮件			
//			taskCommonImpl.sendMail(proMap, taskId, result, gcon, userId, null);
		
		} catch (Exception e) {
			// 关闭流程
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			return rtnInfo;
		}
		return rtnInfo;
	}	
	

	public ReturnInfo audited(GeneralCondition gcon, String userId, String submitMode,TmCaseTaskToDoList toDoList,Boolean send, Map<String, Object> proMap) {
		ReturnInfo rtnInfo = new ReturnInfo();
		try{			
				
			
			Integer tId = toDoList.getTaskId();
			// 检查必要的参数
			rtnInfo = TaskTool.checkTaskParam(userId, tId);
			if (!rtnInfo.getSuccess()) {
				return rtnInfo;
			}
			
			rtnInfo = TaskTool.checkId("submitMode", submitMode);
			if (!rtnInfo.getSuccess()) {
				return rtnInfo;
			}
			
			String taskId = tId.toString();	
		
			// 获取流程参数	
			if (proMap==null || proMap.size()==0){
				proMap = taskCommonImpl.getProcessVarialble(taskId);
			}
			
			String caseId=taskCommonImpl.getProperyOfProcess(proMap, taskId, "caseId");			
			
			Integer id=new Integer(caseId);
			
			//获取案件信息
//			TradeMarkCase tmCase=tradeMarkCaseMapper.selectByPrimaryKey(id);
			
			TradeMarkCase tmCaseNew =(TradeMarkCase) tradeMarkCaseService.queryTradeMarkCaseForWs(id).getData();
			
			//检查案件信息，如果有必填项没有填，或者没有按照官网要求填写，返回提示信息			
			//增加对案件类型的判断，只有商标注册类型的案件，才检查案件信息的完整性
			String caseType=tmCaseNew.getCaseType();
			Integer caseTypeId2 = tmCaseNew.getCaseTypeId();
			/*if (caseType!=null && caseType.equals("商标注册")){
				rtnInfo = tradeMarkCaseService.checkTradeMarkCase(tmCaseNew);
				if (!rtnInfo.getSuccess()) {
					return rtnInfo;
				}
			}*/
			//其他案件类型的 完整性检查
			if(caseType!=null && caseTypeId2!=null){
				rtnInfo = tradeMarkCaseService.checkTradeMarkCase(tmCaseNew);
				if(!rtnInfo.getSuccess()){
					return rtnInfo;
				}
			}
			
			String remarks = toDoList.getRemarks();	
			String key="audited";
			boolean value=true;
			String permission="录入官文";
			Map<String, Object> runMap = new HashMap<String, Object>();		
			runMap.put(key, value);	
			boolean result=true;
			runMap.put("result", result);
			
			String taskResult="审核通过";
			
		
						
			Object obj = proMap.get("agencyLevel");
			if (obj== null) {
				obj = proMap.get("maxAgencyLevel");
				if (obj != null) {
					String maxAencyLevel = obj.toString();
					Integer level=new Integer(maxAencyLevel);
					int lv=level.intValue();
					lv=lv+1;
					String levelStr=lv+"";
					runMap.put("agencyLevel", levelStr);
				}else{
					runMap.put("agencyLevel", "1");
				}
			}
			
			
			// 设置任务执行所必须的参数			
			if (permission != null && !permission.equals("")) {
				runMap.put("permission", permission);
			}
			if (remarks != null && !remarks.equals("")) {
				runMap.put("remarks", remarks);
			}
			
			String submitType=null;
			
			if (submitMode.equals("1")){
				runMap.put("appOnline", true);
				submitType="网上申请";
			}
			else if (submitMode.equals("2")){
				runMap.put("appOnline", false);	
            	submitType="直接递交";		
			}			

			
			String agencyId=taskCommonImpl.getProperyOfProcess(proMap, taskId, "agencyId");
			Integer cId=new Integer(caseId);
			 
            if (submitType!=null){
	            // 如果用户选择网上申请，那么设置案件的递交方式为"网上申请"
            	// 否则设置递交方式为"直接递交"           
	            if (cId!=null){
		            TradeMarkCase record=new TradeMarkCase();
		            record.setId(cId);
		            record.setSubmitType(submitType);
		            //更新案件状态
					String caseStatus="申请中";						
					record.setStatus(caseStatus);
					Date statusDate=new Date();
					record.setStatusDate(statusDate);
			
					taskCommonImpl.recordTmCaseStatus(record);
	            }
            }
                    
			String tokenID=gcon.getTokenID();
        	//获取用户
			User user = userService.getUserById(userId, tokenID);	
			Integer uId=new Integer(userId);
			String username=user.getUsername();
						
			String caseStatus="申请材料审核完成";
			
			//记录案件的处理流程
			taskCommonImpl.recordTmCaseProcess(cId, caseStatus, username, uId, 0, 1);	
					
			
			//注意：必须将代理人的信息保存到流程中，后续需要使用
			runMap.put("userId", userId);
			rtnInfo = taskBasicServiceImpl.userDoTask( gcon,userId,toDoList,taskResult,proMap,runMap);
			
				
			String agencyServiceId=taskCommonImpl.getProperyOfProcess(proMap, taskId, "agencyServiceId");
			
			//创建账单记录	
			caseChargeRecordServiceImpl.createChargeRecords(gcon, userId, caseId, agencyId, agencyServiceId);
			
			if(send==null) send = true;
			
			if(send) {
				TradeMarkCase tradeMarkCase=tradeMarkCaseMapper.selectByPrimaryKey(cId);
				
	//			String caseType=tradeMarkCase.getCaseType();				
				Integer caseTypeId=tradeMarkCase.getCaseTypeId();				
				String caseTypeName=caseTypeId.toString();			
				
				//商标注册申请递交类型的邮件
				String mailType="sbzcsq_confirm_en";
				// 给客户发送邮件			
				taskCommonImpl.sendMail(proMap, taskId, result, gcon, userId, null, caseTypeName, mailType);
			}
		
		} catch (Exception e) {
			// 关闭流程
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			return rtnInfo;
		}
		return rtnInfo;
	}
	
	
	
	// 网上递交申请接口
	public ReturnInfo submitApp(GeneralCondition gcon, String userId, String custId, String caseId, String processInstanceId) {		
		ReturnInfo rtnInfo = new ReturnInfo();
		try{
			
			String tokenID=gcon.getTokenID();			
			// 检查必要的参数			
			rtnInfo = TaskTool.checkId("processInstanceId", processInstanceId);
			if (!rtnInfo.getSuccess()) {
				return rtnInfo;				
			}			
			if ((userId == null || userId.equals(""))
					&& (custId == null || custId.equals(""))) {
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("userId与custId不能为空");
				rtnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
				return rtnInfo;
			}
			
			//如果custId为空，意味着是申请人递交申请，因此，需要检查权限。
			if (custId==null && userId != null) {
				// 每个任务名称，对应一个权限
				String currentTaskPermission = "递交申请";

				// 检查当前用户是否有权限执行该任务
				ReturnInfo checkret = processService.checkuserstart(
						currentTaskPermission, userId, gcon);
				if (!checkret.getSuccess()) {
					throw new Exception(checkret.getMessage());
				}
			}
				
			//此处调用网申服务的接口
			Integer id=new Integer(caseId);
			//获取案件信息
//			TradeMarkCase tradeMarkCase=tradeMarkCaseMapper.selectByPrimaryKey(id);
			
			TradeMarkCase tradeMarkCase =(TradeMarkCase) tradeMarkCaseService.queryTradeMarkCaseForWs(id).getData();
			
			//更新案件的流程实例Id
			TradeMarkCase record=new TradeMarkCase();			
			record.setId(id);
			record.setProcessId(processInstanceId);	
			tradeMarkCaseMapper.updateByPrimaryKeySelective(record);
						
			Integer caseProId=new Integer(processInstanceId);			
			Integer caseTypeId=tradeMarkCase.getCaseTypeId();	
			
			Integer agencyId=tradeMarkCase.getAgencyId();
			
			String changeType=tradeMarkCase.getChangeType();
//			logger.info("111111111111111111 changeType"+ changeType);
						
			boolean submitResult=producerService.sendMapMessage(tradeMarkCase, caseProId,  agencyId);
				
			
			//修改案件状态
			if (caseId!=null){	
				String caseStatus="申请中";	
				TradeMarkCase tradeMarkCase2=new TradeMarkCase();			
				tradeMarkCase2.setId(id);	
				tradeMarkCase2.setStatus(caseStatus);
				Date statusDate=new Date();
				tradeMarkCase2.setStatusDate(statusDate);
				//更新案件状态
				taskCommonImpl.recordTmCaseStatus(tradeMarkCase2);
			}
		
				
			//记录案件的处理流程
			String caseStatus="网上递交申请";
			String username="系统";
			taskCommonImpl.recordTmCaseProcess(id, caseStatus, username, 0, 0, 0);
			
			//Modification start, 2018-10-17
			//支持将案件信息同步到wpm  
			Integer type=TaskTool.getDataSynTypeByCaseType(caseTypeId);
			if (type!=null){				
				taskCommonImpl.dataSyn(gcon, agencyId, id, type);
			}
			//Modification end
			
			//Modification start, 2018-10-22
			//网上递交后，自动关闭续展提醒、优先权提醒
			//根据caseId，找出该案件的所有的提醒，如果该案件提醒时续展或优先权，则关闭
			Remind remind = new Remind();
			remind.setCaseId(id);
			ReturnInfo info = remindService.selectRemindList(remind, gcon);
			if(info.getSuccess()){
				//Modification start, 2018-12-14, by yang guang
				//to resolve BDY-752
				List<Map<String, Object>> list = (List<Map<String, Object>>)info.getData();				
				if(list!=null){
					for(Map<String, Object> map:list){						
						Integer rid=(Integer)map.get("rid");
						String message=(String)map.get("message");
						Remind remind2=new Remind();
						remind2.setRid(rid);
						remind2.setMessage(message);
						//Modification end
						if("续展时限".equals(remind2.getMessage())||"优先权时限".equals(remind2.getMessage())){
							Integer rId = remind2.getRid();
							remindService.deleteRemind(rId);//关闭时限提醒
						}
					}
				}
			}
			//Modification end
			/*
			// 设置任务执行所必须的参数
			Map<String, Object> runMap = new HashMap<String, Object>();
			// 后续task根据submitresult的值来选择执行路径，必须设置该参数
			runMap.put("submitresult", submitResult);		
				
			//如果递交成功，会执行到录入公告的task
			//而 task获取candidate user时，需要permission参数，必须设置该参数。
			String permission="录入公告";							
			runMap.put("permission", permission);
			
			
			//由于是service task自动执行，不需要再调用processService的doTask去执行任务
			//但后续流程需要上述必须的参数。所以，在此进行设置。
			processService.setVariableByProId(processInstanceId,runMap);
			
			*/
			
			
			/*
			
			//任务执行结果
			String taskResult="";			
			if(submitResult){
				taskResult="申请递交成功";
			}else{
				taskResult="申请递交失败";
			}
			String remarks="";
			
			Map<String, Object> proMap = taskCommonImpl.getProcessVarialbleByPro(processInstanceId);			
			String taskId=(String)proMap.get("taskId");			
			String taskName=(String)proMap.get("taskId");			
			Integer cId=new Integer(caseId);
			Integer tId=new Integer(taskId);			
			
			String username="";			
			
			//虽然是自动执行的网申，但应该将案件申请的提交人记录到任务执行记录表中。
			if (custId!=null && !custId.equals("")){			
				Customer customer = customerService.getCustById(custId, tokenID);
				username=customer.getUsername();
			}else{
				User user=userService.getUserById(userId, tokenID);
				username=user.getUsername();
			}			
			
			//保存任务执行记录
			taskBasicServiceImpl.saveTaskRecord(cId, tId, taskName, taskResult, remarks, username);
			
			// 给客户/申请人发送邮件			
			taskCommonImpl.sendMail(proMap, null, null, gcon, userId, custId);		
			
			*/
				
			
			
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
		

	
	// 直接递交接口
	public ReturnInfo appOffLine(GeneralCondition gcon, String userId, String submitStatus, TmCaseTaskToDoList toDoList, Map<String, Object> proMap) {	
		ReturnInfo rtnInfo = new ReturnInfo();
		try{
			Integer tId = toDoList.getTaskId();
			String tokenID = gcon.getTokenID();
			// 检查必要的参数
			rtnInfo = TaskTool.checkTaskParam(userId, tId);
			if (!rtnInfo.getSuccess()) {
				return rtnInfo;
			}			
			String taskId = tId.toString();			
			String remarks = toDoList.getRemarks();	
			
			// 检查必要的参数
			rtnInfo=TaskTool.checkSubmitStatus(submitStatus);
			if (!rtnInfo.getSuccess()){
				return rtnInfo;
			}
			
			String key="submitresult";
			boolean value=false;
			String permission="审核案件";
			
			//如果submitStatus等于1的含义是递交成功，;
			if(submitStatus.equals("1")){
				permission="录入官文"; 
				value=true;
			}
			
			Map<String, Object> runMap = new HashMap<String, Object>();				
			// 设置任务执行所必须的参数	
			runMap.put(key, value);
			runMap.put("permission", permission);
			if (remarks != null && !remarks.equals("")) {
				runMap.put("remarks", remarks);
			}
			
			String taskResult="直接递交申请成功";
						
			// 获取流程参数	
			if (proMap==null || proMap.size()==0){
				proMap = taskCommonImpl.getProcessVarialble(taskId);
			}
			
			//注意：必须将代理人的信息保存到流程中，后续需要使用
			runMap.put("userId", userId);
			
			
			//可录入的官文名字
			Object obj1=proMap.get("guanwenName");
			Object obj2=proMap.get("fileName");
			String imputedGuanwen=null;
			String fileName=null;
			if(obj1!=null){
				imputedGuanwen=obj1.toString();
			}
			if(obj2!=null){
				fileName=obj2.toString();
			}
			
			
			String cId=taskCommonImpl.getProperyOfProcess(proMap, taskId, "caseId");
			Integer caseId=new Integer(cId);			
			TradeMarkCase tradeMarkCase=tradeMarkCaseMapper.selectByPrimaryKey(caseId);			
			String caseType=tradeMarkCase.getCaseType();				
			Integer caseTypeId=tradeMarkCase.getCaseTypeId();				
			
	
			String guanwenName=taskCommonImpl.getAvalibleGuanWen(imputedGuanwen, fileName, caseType);
			runMap.put("guanwenName", guanwenName);
		
			
			String processInstanceId=taskCommonImpl.getProperyOfProcess(proMap, taskId, "processInstanceId");
			//更新案件的流程实例Id
			TradeMarkCase record=new TradeMarkCase();			
			record.setId(caseId);
			record.setProcessId(processInstanceId);	
			tradeMarkCaseMapper.updateByPrimaryKeySelective(record);
			
			// 执行流程
			rtnInfo = taskBasicServiceImpl.userDoTask(gcon,userId,toDoList,taskResult,proMap,runMap);
					
			
			if(submitStatus.equals("1")){

				//获取用户
				User user = userService.getUserById(userId, tokenID);	
				Integer uId=new Integer(userId);
				String username=user.getUsername();				
		
				String caseStatus="线下递交申请完成";
				
				//记录案件的处理流程
				taskCommonImpl.recordTmCaseProcess(caseId, caseStatus, username, uId, 0, 0);		
				
				// 修改案件的申请日期
				TradeMarkCase tmCase=new TradeMarkCase();
				tmCase.setId(caseId);
				Date appDate=new Date();
				tmCase.setAppDate(appDate);
				
				
				tradeMarkCaseMapper.updateByPrimaryKeySelective(tmCase);
				
				
			
				
				
				//Modification start, 2018-10-17
				//支持将案件信息同步到wpm  
				Integer type=TaskTool.getDataSynTypeByCaseType(caseTypeId);
				if (type!=null){
					Integer agencyId=tradeMarkCase.getAgencyId();
					taskCommonImpl.dataSyn(gcon, agencyId, caseId, type);
				}
				//Modification end
				
				//Modification start, 2018-10-22
				//直接递交后，自动关闭续展提醒、优先权提醒
				//根据caseId，找出该案件的所有的提醒，如果该案件提醒是续展或优先权，则关闭
				closeRemindByCaseId(caseId, gcon, "续展时限");
				closeRemindByCaseId(caseId, gcon, "优先权时限");
				//关闭异议时限
				closeRemindByCaseId(caseId, gcon, "异议时限");
				closeRemindByCaseId(caseId, gcon, "补充材料时限");
				if("305".equals(fileName)){
					closeRemindByCaseId(caseId, gcon, "补正时限");
				}else if("303".equals(fileName)){
					closeRemindByCaseId(caseId, gcon, "部分驳回复审时限");//部分驳回，递交分割申请后，自动关闭驳回时限
				}
				//如果该案件是驳回复审，则查询他的父案件的驳回时限，再自动关闭
				//如果该案件是异议答辩，则查询他的父案件的异议答辩时限，再自动关闭
				//如果该案件是部分驳回，则查询驳回时限，自动关闭
				if(caseTypeId==12){//驳回复审
					Integer parentCaseId = tradeMarkCase.getParentId();
					if(parentCaseId!=null){
						closeRemindByCaseId(parentCaseId, gcon, "驳回复审时限");//关闭父案件的驳回复审时限 或 部分驳回复审时限
						closeRemindByCaseId(parentCaseId, gcon, "部分驳回复审时限");
					}
				}else if(caseTypeId==9){//异议答辩
					Integer parentCaseId = tradeMarkCase.getParentId();
					if(parentCaseId!=null){
						closeRemindByCaseId(parentCaseId, gcon, "异议答辩时限");//关闭父案件的异议答辩时限
					}
				}
				//Modification end
				
				//商标注册申请递交类型的邮件
				String mailType="sbzcsq_zjds_en";
				// 给客户发送邮件			
				taskCommonImpl.sendMail(proMap, taskId, null, gcon, userId, null, caseTypeId.toString(), mailType);
			}
		} catch (Exception e) {			
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			return rtnInfo;
		}
		return rtnInfo;	
	}
	
	void SendMessage(){
		
	}
	
	
	
	
	// 网申结果接口
	public ReturnInfo setAppOnLineResult(GeneralCondition gcon,String userId, String submitStatus, TmCaseTaskToDoList toDoList) {
		ReturnInfo rtnInfo = new ReturnInfo();
		try{
			String tokenID=gcon.getTokenID();
			
			Integer tId = toDoList.getTaskId();
			// 检查必要的参数
			rtnInfo = TaskTool.checkTaskParam(userId, tId);
			if (!rtnInfo.getSuccess()) {
				return rtnInfo;
			}
			String taskId = tId.toString();			
			String remarks = toDoList.getRemarks();	
			//检查当前用户是否有权限执行该任务
			if(!userId.equals("sysmUser")){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("该用户没有权限执行网申");
				rtnInfo.setMessageType(Globals.MESSAGE_TYPE_AUTHORTY_INVALID);
				return rtnInfo;
			}	
			rtnInfo=TaskTool.checkSubmitStatus(submitStatus);
			if (!rtnInfo.getSuccess()){
				return rtnInfo;
			}
			
			// 获取流程参数			
			Map<String, Object> proMap = taskCommonImpl.getProcessVarialble(taskId);
			Map<String, Object> runMap = new HashMap<String, Object>();		
			String key="submitresult";
			boolean value=false;
			String permission="";
			//如果用户选择网申，那么设置流程所需的变量appOnline=true;
			String taskResult="网上递交申请未完成";
			if(submitStatus.equals("1")){
				permission="录入公告"; 
				value=true;
				taskResult="网上递交申请成功";
				//可录入的官文名字
				Object obj1=proMap.get("guanwenName");
				Object obj2=proMap.get("fileName");
				String imputedGuanwen=null;
				String fileName=null;
				if(obj1!=null){
					imputedGuanwen=obj1.toString();
				}
				if(obj2!=null){
					fileName=obj2.toString();
				}	
				
				String cId=taskCommonImpl.getProperyOfProcess(proMap, taskId, "caseId");
				Integer caseId=new Integer(cId);
				TradeMarkCase tradeMarkCase=tradeMarkCaseMapper.selectByPrimaryKey(caseId);				
				String caseType=tradeMarkCase.getCaseType();		
			
				String guanwenName=taskCommonImpl.getAvalibleGuanWen(imputedGuanwen, fileName, caseType);
				runMap.put("guanwenName", guanwenName);
			}
			
			runMap.put(key, value);
		
			
			// 设置任务执行所必须的参数			
			if (permission != null && !permission.equals("")) {
				runMap.put("permission", permission);
			}
			if (remarks != null && !remarks.equals("")) {
				runMap.put("remarks", remarks);
			}
			rtnInfo = taskBasicServiceImpl.userDoTask( gcon,userId,toDoList,taskResult,proMap,runMap);
		
			if(submitStatus.equals("1")){
				
				//获取用户				
				String username="系统";
				
				String cId=taskCommonImpl.getProperyOfProcess(proMap, taskId, "caseId");
				Integer caseId=new Integer(cId);
				
				String caseStatus="网上递交申请完成";
				//记录案件的处理流程
				taskCommonImpl.recordTmCaseProcess(caseId, caseStatus, username, 0, 0, 1);	
				
				//更新案件状态,修改案件的申请日期		
				TradeMarkCase tmCase=new TradeMarkCase();			
				tmCase.setId(caseId);	
				Date date=new Date();							
				tmCase.setAppDate(date);
				tradeMarkCaseMapper.updateByPrimaryKeySelective(tmCase);				
								

				
				TradeMarkCase tradeMarkCase=tradeMarkCaseMapper.selectByPrimaryKey(caseId);
				
//				String caseType=tradeMarkCase.getCaseType();				
				Integer caseTypeId=tradeMarkCase.getCaseTypeId();				
				String caseType=caseTypeId.toString();				
				
				//商标注册申请递交类型的邮件
				String mailType="sbzcsq_zjds_en";
				// 给客户发送邮件			
				taskCommonImpl.sendMail(proMap, taskId, null, gcon, userId, null, caseType, mailType);
				
			}
		
		
		} catch (Exception e) {
			// 关闭流程
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			return rtnInfo;
		}	
		return rtnInfo;	
	}

	

	// 官文录入接口
	public ReturnInfo officalDoc(GeneralCondition gcon, String userId, String fileName, 
			TradeMarkCase tradeMarkCase, TmCaseTaskToDoList toDoList) {
		ReturnInfo rtnInfo = new ReturnInfo();
		try{
			//更新案件信息，将官文属性保存到案件信息中
			updateTradeMarkCase(fileName,tradeMarkCase);
			
			Integer tId = toDoList.getTaskId();
			// 检查必要的参数
			rtnInfo = TaskTool.checkTaskParam(userId, tId);
			if (!rtnInfo.getSuccess()) {
				return rtnInfo;
			}
			String taskId = tId.toString();			
			String remarks = toDoList.getRemarks();	
			
			// 获取流程参数			
			Map<String, Object> proMap = taskCommonImpl.getProcessVarialble(taskId);
			// 检查录入的官文是否正确
			String taskName=taskCommonImpl.getProperyOfProcess(proMap, taskId, "taskName");
			String caseId=taskCommonImpl.getProperyOfProcess(proMap, taskId, "caseId");
			rtnInfo = TaskTool.checkOfficalDoc(taskName, fileName);
			if (!rtnInfo.getSuccess()) {
				return rtnInfo;
			}
			
			String realFileName=Constants.fileNameList.get(fileName);		
			Integer cId= null;
			Date docDate=tradeMarkCase.getDocDate();
			if (caseId!=null && !caseId.equals("")){	
				cId=Integer.parseInt(caseId);			
				List<Integer> fileNames=new ArrayList<Integer>();
				Integer flleNumber=Integer.parseInt(fileName);
				fileNames.add(flleNumber);
				MaterialCondition materialCondition=new MaterialCondition();
				materialCondition.setCaseId(cId);
				materialCondition.setFileNames(fileNames);
				List<Material> materialList = materialMapper.selectByCaseIdAndFileNames(materialCondition);
				
				if (materialList!=null && materialList.size()>0){	
					if (docDate!=null){
						Material material=materialList.get(0);					
						//设置官文的收文日期
						material.setDocDate(docDate);
						materialMapper.updateByPrimaryKeySelective(material);
					}
				}else{
					rtnInfo.setSuccess(false);
					rtnInfo.setMessage("该案件没有录入"+ realFileName);
					return rtnInfo;
				}
			}
			
			
			
		
			Map<String, Object> runMap = new HashMap<String, Object>();		
		
			String taskResult="录入" + realFileName ;
			
			String permission="审核官文";				
			
			String key="fileName";
			String value=fileName;		
			runMap.put(key, value);			
			
			// 设置任务执行所必须的参数			
			if (permission != null && !permission.equals("")) {
				runMap.put("permission", permission);
			}
			if (remarks != null && !remarks.equals("")) {
				runMap.put("remarks", remarks);
			}
			
			
			rtnInfo = taskBasicServiceImpl.userDoTask( gcon,userId,toDoList,taskResult,proMap,runMap);
			
			String tokenID=gcon.getTokenID();
			
			User user=userService.getUserById(userId, tokenID);
			Integer uId=new Integer(userId);
			String username=user.getUsername();

			if (caseId!=null && !caseId.equals("")){			
						
				//记录案件的处理流程
				String caseStatus="录入" + realFileName;
				
				taskCommonImpl.recordTmCaseProcess(cId, caseStatus, username, uId, 0, 0);
			}
			
			// 给客户发送邮件			
//			taskCommonImpl.sendMail(proMap, taskId, null, gcon, userId, null);	
			//创建质证时限    342
			if(fileName.equals("342")){
				remindService.insertRemindByType(12,docDate, null, tradeMarkCase.getCustId(), Integer.valueOf(caseId), gcon);
			}
		
		} catch (Exception e) {
			// 关闭流程
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			return rtnInfo;
		}	
		
		return rtnInfo;	
	}
	
	
	
	
	//审核官文入接口
	public ReturnInfo auditOfficalDoc(GeneralCondition gcon, String userId, String auditResult,
			TmCaseTaskToDoList toDoList, Map<String, Object> proMap) {
		ReturnInfo rtnInfo = new ReturnInfo();
		try{
			Integer tId = toDoList.getTaskId();
			// 检查必要的参数
			rtnInfo = TaskTool.checkTaskParam(userId, tId);
			if (!rtnInfo.getSuccess()) {
				return rtnInfo;
			}
			String taskId = tId.toString();			
			String remarks = toDoList.getRemarks();	
		
			//从流程中获取参数
			if(proMap==null || proMap.size()==0){
				proMap = taskCommonImpl.getProcessVarialble(taskId);	
			}
				
			String fileName="";
			Object obj=proMap.get("fileName");
			if (obj!=null){
				fileName=(String)obj;
			}
			
		
			Map<String, Object> runMap = new HashMap<String, Object>();		
			
			String key="fileName";
			String value=fileName;		
			runMap.put(key, value);
			
			rtnInfo=TaskTool.checkAuditResult(auditResult);
			if (!rtnInfo.getSuccess()){
				return rtnInfo;
			}
			
			boolean result=false;
			
			String caseId=taskCommonImpl.getProperyOfProcess(proMap, taskId, "caseId");
			Integer cId=new Integer(caseId);
			TradeMarkCase tradeMarkCase=tradeMarkCaseMapper.selectByPrimaryKey(cId);
			String caseType=tradeMarkCase.getCaseType();				
			Integer caseTypeId=tradeMarkCase.getCaseTypeId();				
			
		
			// 对官文的审核结果	
			if(auditResult.equals("1")){
				result=true;
				//可录入的官文名字
				Object obj1=proMap.get("guanwenName");
				Object obj2=proMap.get("fileName");
				String imputedGuanwen=null;
				if(obj1!=null){
					imputedGuanwen=obj1.toString();
				}						
				String guanwenName=taskCommonImpl.getAvalibleGuanWen(imputedGuanwen, fileName, caseType);
				runMap.put("guanwenName", guanwenName);
				
				
				//Modification start, 2018-10-17
				//支持将案件信息同步到wpm  
				Integer type=TaskTool.getDataSynType(fileName);
				if (type!=null){
					
					String agencyId=taskCommonImpl.getProperyOfProcess(proMap, taskId, "agencyId");
					Integer aId=new Integer(agencyId);
					taskCommonImpl.dataSyn(gcon, aId, cId, type);
				}
				//Modification end
				
			}else{
				String permission="录入官文"; 
				runMap.put("permission", permission);
			}
			//审核结果
			runMap.put("audited", result);
//			//直接客户
//			runMap.put("directCustomer", true);		
			
			//Modification start, by yang guang, 2018-11-05
			//子案件的状态通过在子案件中的修改状态接口进行设置，而不是在这里设置
			/*
			//如果录入的官文是驳回复审，异议答辩裁定，不予注册复审决定，那么将裁定结果保存到流程中
			if (fileName.equals("349") || fileName.equals("351") || fileName.equals("353")){			
				runMap.put("success", true);
			}else if(fileName.equals("350") || fileName.equals("352") || fileName.equals("354")){
				runMap.put("success", false);
			}				
			*/
			//Modification end
			
			String realFileName=Constants.fileNameList.get(fileName);			
			String taskResult="审核" + realFileName  ;			
			String permission=TaskTool.getPermissionByFileName(fileName);	
				
					
			// 设置任务执行所必须的参数			
			if (permission != null && !permission.equals("")) {
				runMap.put("permission", permission);
			}
			if (remarks != null && !remarks.equals("")) {
				runMap.put("remarks", remarks);
			}
			
			//注意：必须将代理人的信息保存到流程中，后续需要使用
			runMap.put("userId", userId);
			rtnInfo = taskBasicServiceImpl.userDoTask( gcon,userId,toDoList,taskResult,proMap,runMap);
			
			
			String tokenID=gcon.getTokenID();
			
			String mailType=TaskTool.getMailType(fileName);
			//目前官方通知的邮件模板只支持了，受理通知，初步审定公告，注册证
			//所以，如果不属于上述类型的通知，只能现有sbzcsq_confirm_en这个模板
			if (mailType==null || mailType.equals("")){
				mailType="sbzcsq_confirm_en";
			}
			
			
			//根据filename 来创建 案件（驳回复审，异议答辩裁定，不予注册复审决定）时限
			TradeMarkCase tmcase = (TradeMarkCase)tradeMarkCaseService.queryTradeMarkCaseDetail(Integer.valueOf(caseId)).getData();
			Date date = new Date();
			//获取docDate
			if(fileName !=null && fileName !=""){
				int fn = Integer.valueOf(fileName);
				Map<String, Object> materail = taskRecordMapper.queryByCaseIdAndFileName(cId,fn);
				if(materail != null){
					Date docDate = (Date)materail.get("docDate");
					if(docDate != null){
						date = docDate;
					}
				}
			}
			Integer type = 0;
			
			switch (fileName) {
				case "302" :           //驳回通知
					type = 7;
					mailType = "sbzcsq_bh_en";
					break;
				case "303":               //部分驳回通知
					type =6;
					mailType = "sbzcsq_bh_en";
					break;
				case "340":          //异议答辩通知
					type = 3;
					break;
				case "305":       //补正通知
					type = 5;
					mailType = "sbzcsq_bbz_en";
					break;
				case "342":            //建立质证时限
					type=12;
					mailType ="";
					break;
				default :
					break;
			}
			if(type!=0){
				remindService.insertRemindByType(type,date, null, tmcase.getCustId(), Integer.valueOf(caseId), gcon);
			}
			
				
			// 给客户发送邮件			
			taskCommonImpl.sendMail(proMap, taskId, null, gcon, userId, null, caseTypeId.toString(), mailType);
			User user=userService.getUserById(userId, tokenID);
			Integer uId=new Integer(userId);
			String username=user.getUsername();

			if (caseId!=null && !caseId.equals("")){			
				String caseStatus="审核"+ realFileName;				
				//记录案件的处理流程
				taskCommonImpl.recordTmCaseProcess(cId, caseStatus, username, uId, 0, 1);
				//更新案件状态
				if(auditResult.equals("1")){				
					Integer id=new Integer(caseId);			
					caseStatus=TaskTool.getCaseStatus(fileName, false, null);
					if (caseStatus!=null){
						TradeMarkCase tmCase=new TradeMarkCase();			
						tmCase.setId(id);	
						tmCase.setStatus(caseStatus);
						Date statusDate=new Date();
						tmCase.setStatusDate(statusDate);
						
						taskCommonImpl.recordTmCaseStatus(tmCase);
					}
				}
			}
			
			
				
		} catch (Exception e) {
			// 关闭流程
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			return rtnInfo;
		}	
		
		return rtnInfo;	
	}
	
	
	
	// 处理官文接口
	public ReturnInfo processDoc(GeneralCondition gcon, String userId, String approved,
			TmCaseTaskToDoList toDoList) {
		ReturnInfo rtnInfo = new ReturnInfo();
		try{
			Integer tId = toDoList.getTaskId();
			// 检查必要的参数
			rtnInfo = TaskTool.checkTaskParam(userId, tId);
			if (!rtnInfo.getSuccess()) {
				return rtnInfo;
			}
			String taskId = tId.toString();			
			String remarks = toDoList.getRemarks();	

			// 检查必要的参数
			rtnInfo = TaskTool.checkTaskParam(userId, tId);
			if (!rtnInfo.getSuccess()) {
				return rtnInfo;
			}
			
			rtnInfo = TaskTool.checkId("approved", approved);
			if (!rtnInfo.getSuccess()) {
				return rtnInfo;
			}
			

			// 获取流程参数			
			Map<String, Object> proMap = taskCommonImpl.getProcessVarialble(taskId);
			
			String fileName="";
			Object obj=proMap.get("fileName");
			if (obj!=null){
				fileName=(String)obj;
			}
			
			String tokenID=gcon.getTokenID();
			//获取用户
			User user = userService.getUserById(userId, tokenID);	
			Integer uId=new Integer(userId);
			String username=user.getUsername();
			
			
			String cId=taskCommonImpl.getProperyOfProcess(proMap, taskId, "caseId");
			Integer caseId=new Integer(cId);
			String caseStatus="";
			
			String permission="处理决定";		
			String decideByCust="2";
			String operation=null;
			if (approved.equals("3")){//3：向客户反馈
				decideByCust="1"; // 向客户报告
				permission="反馈通知";	
				caseStatus="向客户报告：";		
				operation="";
			}else if (approved.equals("1")){//1：同意
				caseStatus="代理人决定：";				
				operation=Constants.agreeOperation.get(fileName);
				if (operation==null){
					operation="提交";
				}
			}else  if (approved.equals("2")){//2：拒绝
				caseStatus="代理人决定：";
				operation="放弃";
			}
			
			//Modification start, by yang guang, 2018-10-29
			
			
			if (fileName!=null){
				String realFileName=Constants.fileNameList.get(fileName);				
				if(realFileName!=null ){
					/*
					if (approved.equals("3")){
						caseStatus=caseStatus+realFileName;
					}else{	
						int pos=realFileName.indexOf("通知");
						if (pos>-1){
							caseStatus=caseStatus+realFileName.substring(0,pos);
						}							
					}
					*/
					caseStatus=caseStatus+operation+realFileName;
					
				}
			}
			
			//Modification end
			
			//Modification start, 2018-10-22
			if(approved.equals("2")){
				if("305".equals(fileName)){//自动关闭补正时限
					closeRemindByCaseId(caseId, gcon, "补正时限");
				}else if("302".equals(fileName)){//自动关闭驳回时限
					closeRemindByCaseId(caseId, gcon, "驳回复审时限");
				}else if("340".equals(fileName)){//自动关闭异议答辩时限
					closeRemindByCaseId(caseId, gcon, "异议答辩时限");
				}else if("342".equals(fileName)){//自动关闭质证时限
					closeRemindByCaseId(caseId, gcon,"质证时限");
				}
			}
			//Modification end
			//创建 递交补充材料时限
			if(approved.equals("1")){
				//查看是否重复插入
				int isExist = tradeMarkCaseMapper.isExist(caseId);
				if(isExist == 0){
					if("305".equals(fileName)){
						Date date = new Date();
						int type= 13;
						remindService.insertRemindByType(type,date, null,Integer.valueOf(userId), Integer.valueOf(caseId), gcon);
					}
				}
				
			}
			
			
			//记录案件的处理流程
			taskCommonImpl.recordTmCaseProcess(caseId, caseStatus, username,uId, 0, 1);		
			
			Map<String, Object> runMap = new HashMap<String, Object>();		

			String taskResult="完成";
			
			
			// 判断官文是否为新案件的决定书，那么执行恢复上级流程运行的操作
//			taskBasicServiceImpl.feedBackDocResult(proMap, runMap);
		
			// 设置任务执行所必须的参数			
			if (permission != null && !permission.equals("")) {
				runMap.put("permission", permission);
			}
			if (remarks != null && !remarks.equals("")) {
				runMap.put("remarks", remarks);
			}
			
			//注意：必须将代理人的信息保存到流程中，后续需要使用
			runMap.put("userId", userId);
			runMap.put("decideByCust", decideByCust);
			
			if (approved.equals("1")){
				runMap.put("approved", true);
			}else{
				runMap.put("approved", false);
				
			}
			rtnInfo = taskBasicServiceImpl.userDoTask( gcon,userId,toDoList,taskResult,proMap,runMap);
						
			// 给客户发送邮件			
//			taskCommonImpl.sendMail(proMap, taskId, null, gcon, userId, null);
		
		} catch (Exception e) {
			// 关闭流程
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			return rtnInfo;
		}	
		
		return rtnInfo;	
	}
		
	private void closeRemindByCaseId(Integer caseId,GeneralCondition gcon,String message){
		//根据caseId，找出该案件的所有的提醒，如果该案件提醒是message，则关闭
		Remind remind = new Remind();
		remind.setCaseId(caseId);
		ReturnInfo info = remindService.selectRemindList(remind, gcon);
		if(info.getSuccess()){
			List<Map<String, Object>> reminds = (List<Map<String, Object>>)info.getData();
			if(reminds!=null){
				for(Map<String, Object> remind2:reminds){
					Integer isclose = (Integer)remind2.get("isclose");
					String msg = (String)remind2.get("message");
					if(message.equals(msg)&&(isclose==0)){
						Integer rId = (Integer)remind2.get("rid");
						remindService.deleteRemind(rId);//关闭时限提醒
					}
				}
			}
		}	
	}
	
	// 以前的接口，暂时无用：向客户报告接口
	public ReturnInfo feedback(GeneralCondition gcon, String userId,
			TmCaseTaskToDoList toDoList) {
		ReturnInfo rtnInfo = new ReturnInfo();
		try{
			Integer tId = toDoList.getTaskId();
			// 检查必要的参数
			rtnInfo = TaskTool.checkTaskParam(userId, tId);
			if (!rtnInfo.getSuccess()) {
				return rtnInfo;
			}
			String taskId = tId.toString();			
			String remarks = toDoList.getRemarks();	

			// 检查必要的参数
			rtnInfo = TaskTool.checkTaskParam(userId, tId);
			if (!rtnInfo.getSuccess()) {
				return rtnInfo;
			}
			
			String key="success";
			Object value="" ;
			String permission="";		
			
			Map<String, Object> runMap = new HashMap<String, Object>();		
	//		runMap.put(key, value);
			String taskResult="完成";
			
			// 获取流程参数			
			Map<String, Object> proMap = taskCommonImpl.getProcessVarialble(taskId);
			
			// 判断官文是否为新案件的决定书，那么执行恢复上级流程运行的操作
//			taskBasicServiceImpl.feedBackDocResult(proMap, runMap);
		
			// 设置任务执行所必须的参数			
			if (permission != null && !permission.equals("")) {
				runMap.put("permission", permission);
			}
			if (remarks != null && !remarks.equals("")) {
				runMap.put("remarks", remarks);
			}
			
			//注意：必须将代理人的信息保存到流程中，后续需要使用
			runMap.put("userId", userId);
			rtnInfo = taskBasicServiceImpl.userDoTask( gcon,userId,toDoList,taskResult,proMap,runMap);
			
			String tokenID= gcon.getTokenID();
			//获取用户
			User user = userService.getUserById(userId, tokenID);		
			Integer uId=new Integer(userId);
			String username=user.getUsername();
			
			String cId=taskCommonImpl.getProperyOfProcess(proMap, taskId, "caseId");
			Integer caseId=new Integer(cId);
			String caseStatus="向客户报告官方通知";
			
			//记录案件的处理流程
			taskCommonImpl.recordTmCaseProcess(caseId, caseStatus, username, uId, 0, 1);			
			
			// 给客户发送邮件			
//			taskCommonImpl.sendMail(proMap, taskId, null, gcon, userId, null);
		
		} catch (Exception e) {
			// 关闭流程
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			return rtnInfo;
		}	
		
		return rtnInfo;	
	}
	
	
		

	// 处理客户决定接口
	public ReturnInfo processDecision(GeneralCondition gcon, String userId,
			TmCaseTaskToDoList toDoList) {		
		ReturnInfo rtnInfo = new ReturnInfo();
		try{
			Integer tId = toDoList.getTaskId();
			// 检查必要的参数
			rtnInfo = TaskTool.checkTaskParam(userId, tId);
			if (!rtnInfo.getSuccess()) {
				return rtnInfo;
			}
			String taskId = tId.toString();			
			String remarks = toDoList.getRemarks();	
			String key="success";
			Object value="false";
			String permission="";		
			
			
			Map<String, Object> runMap = new HashMap<String, Object>();		
			runMap.put(key, value);
			
			String taskResult="完成";
			// 获取流程参数			
			Map<String, Object> proMap = taskCommonImpl.getProcessVarialble(taskId);
			
			// 设置任务执行所必须的参数			
			if (permission != null && !permission.equals("")) {
				runMap.put("permission", permission);
			}
			if (remarks != null && !remarks.equals("")) {
				runMap.put("remarks", remarks);
			}
			
			//注意：必须将代理人的信息保存到流程中，后续需要使用
			runMap.put("userId", userId);
			rtnInfo = taskBasicServiceImpl.userDoTask( gcon,userId,toDoList,taskResult,proMap,runMap);
						
		
		} catch (Exception e) {
			// 关闭流程
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			return rtnInfo;
		}	
	
		return rtnInfo;	
	}
	
	//等待对方的决定
	public ReturnInfo oppositeDecision(GeneralCondition gcon, String userId,
			TmCaseTaskToDoList toDoList){
		ReturnInfo returnInfo = new ReturnInfo();
		try{
			Map<String, Object> runMap = new HashMap<>();
			
			
				
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return returnInfo;
	}
	
	
	

	// 错误处理接口
	public ReturnInfo runAgain(GeneralCondition gcon, String userId,
			TmCaseTaskToDoList toDoList) {		
		ReturnInfo rtnInfo = new ReturnInfo();
		try{
			Integer tId = toDoList.getTaskId();
			// 检查必要的参数
			rtnInfo = TaskTool.checkTaskParam(userId, tId);
			if (!rtnInfo.getSuccess()) {
				return rtnInfo;
			}
			String taskId = tId.toString();			
			String remarks = toDoList.getRemarks();	
		
			Map<String, Object> runMap = new HashMap<String, Object>();	
			
			String taskResult="完成";
			// 获取流程参数			
			Map<String, Object> proMap = taskCommonImpl.getProcessVarialble(taskId);
			
			// 设置任务执行所必须的参数	
			if (remarks != null && !remarks.equals("")) {
				runMap.put("remarks", remarks);
			}
			
			//Modification start, by yang guang,
			//不需要调用出错时所要调用的url
			//只需要让当前任务往下执行，就又可以调用之前执行出错的那个任务，在那个任务中，会去执行。
			/*			
			String ErroUrl=taskCommonImpl.getProperyOfProcess(proMap, taskId, "ErroUrl");			
			String tokenID=gcon.getTokenID();			 
			String reqUrl=ErroUrl+"&tokenID="+ tokenID;			 
			logger.info(reqUrl);				
			String jsonString = GraspUtil.getText(reqUrl);				
			logger.info("jsonString: "+ jsonString);
			rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);			
			if (rtnInfo!=null ){
				boolean success=rtnInfo.getSuccess();
				String msg=rtnInfo.getMessage();
				if(success){
					//当上述url执行成功，往下执行流程
					rtnInfo = taskBasicServiceImpl.userDoTask( gcon,userId,toDoList,taskResult,proMap,runMap);
				}else{
					//否则，流程仍然还在当前任务上
					logger.info(msg);
				}
			}	
			*/
			
			rtnInfo = taskBasicServiceImpl.userDoTask( gcon,userId,toDoList,taskResult,proMap,runMap);
		
		} catch (Exception e) {
			// 关闭流程
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			return rtnInfo;
		}	
	
		return rtnInfo;	
	}
	
	
	
	
	//更新案件信息，将官文属性保存到案件信息中
	private void updateTradeMarkCase(String fileName, TradeMarkCase tradeMarkCase){
		 
		//保存必要的属性
		if(fileName.equals("305")){ //补正通知					
			String appNumber=tradeMarkCase.getAppNumber();
			if (appNumber!=null){
				tradeMarkCaseMapper.updateByPrimaryKeySelective(tradeMarkCase);
			}					
		}else if(fileName.equals("332")){ //受理通知
			String appNumber=tradeMarkCase.getAppNumber();
			Date date=tradeMarkCase.getAppDate();
			/*
			if (docDate!=null && !docDate.equals("")){
				Integer caseId=tradeMarkCase.getId();
				List<String> list=new ArrayList<String>();
				list.add("332");
				List<Material> mList=materialMapper.selectByCaseIdAndFileNames(caseId, list);
				if (mList!=null && mList.size()>0){
					Material material=mList.get(0);
					Date documentDate=DateTool.StringToDate(docDate);
					material.setDocDate(documentDate);
					materialMapper.updateByPrimaryKeySelective(material);				
				}
			}
			*/
			if (appNumber!=null || date!=null){
				tradeMarkCaseMapper.updateByPrimaryKeySelective(tradeMarkCase);
			}
			
			
		}else if(fileName.equals("316")){ //初步审定公告
			String approvalNumber=tradeMarkCase.getApprovalNumber();
			Date appDate=tradeMarkCase.getApprovalDate();
			Date objectionDate=tradeMarkCase.getObjectionDate();
			if (approvalNumber!=null || appDate!=null || objectionDate!=null){
				tradeMarkCaseMapper.updateByPrimaryKeySelective(tradeMarkCase);
			}						
		}else if(fileName.equals("320")){ //注册公告
			String regNumber=tradeMarkCase.getRegNumber();
			String regNoticeNumber=tradeMarkCase.getRegNoticeNumber();
			Date regDate=tradeMarkCase.getRegDate();
			Date validStartDate=tradeMarkCase.getValidStartDate();
			Date validEndDate=tradeMarkCase.getValidEndDate();
			if ( regNumber!=null || regNoticeNumber!=null 
					|| regDate!=null || validStartDate!=null 
					||  validEndDate!=null){
				tradeMarkCaseMapper.updateByPrimaryKeySelective(tradeMarkCase);
			}					
		}
	}
	
	
	/*
	public void sendMessage(GeneralCondition gcon,String userId, String fileName, Map<String, Object> proMap){
		String realFileName=Constants.fileNameList.get(fileName);
		if (fileName.equals("305") || fileName.equals("303") || fileName.equals("302") || fileName.equals("340")){
			String agencyLevel=(String)proMap.get("agencyLevel");
			String processInstanceId=(String)proMap.get("processInstanceId");
			multiProcessServiceImpl.notifyOfficialNotice(gcon, userId, processInstanceId, agencyLevel, realFileName);
		}
		else if(fileName.equals("306")){
			String agencyLevel=(String)proMap.get("agencyLevel");
			String processInstanceId=(String)proMap.get("processInstanceId");
			multiProcessServiceImpl.notifyCaseResult(gcon, userId, processInstanceId, agencyLevel);			
		}
		
	}
	*/
	
	
	
	// 官文录入接口
	public ReturnInfo autoSaveGuanWen(String fileName, TradeMarkCase tradeMarkCase, String taskId) {
		ReturnInfo rtnInfo = new ReturnInfo();
		try{
			//更新案件信息，将官文属性保存到案件信息中
			updateTradeMarkCase(fileName,tradeMarkCase);				
			
			// 获取流程参数			
			Map<String, Object> proMap = taskCommonImpl.getProcessVarialble(taskId);
			// 检查录入的官文是否正确
			String taskName=taskCommonImpl.getProperyOfProcess(proMap, taskId, "taskName");						
			String caseId=taskCommonImpl.getProperyOfProcess(proMap, taskId, "caseId");
			rtnInfo = TaskTool.checkOfficalDoc(taskName, fileName);
			if (!rtnInfo.getSuccess()) {
				return rtnInfo;
			}
		
			Map<String, Object> runMap = new HashMap<String, Object>();		
			String realFileName=Constants.fileNameList.get(fileName);		
			String taskResult="录入" + realFileName ;
			
			String permission="审核官文";				
			
			String key="fileName";
			String value=fileName;		
			runMap.put(key, value);			
			
			// 设置任务执行所必须的参数			
			if (permission != null && !permission.equals("")) {
				runMap.put("permission", permission);
			}
			
			
			String username="系统";

			rtnInfo = taskBasicServiceImpl.userDoTask(taskId, username, taskResult, proMap, runMap);
						
			if (caseId!=null && !caseId.equals("")){			
				Integer cId=new Integer(caseId);				
				//记录案件的处理流程
				String caseStatus="录入" + realFileName;
				
				taskCommonImpl.recordTmCaseProcess(cId, caseStatus, username, 0, 0, 0);
			}
			
			
		
		} catch (Exception e) {
			// 关闭流程
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			return rtnInfo;
		}	
		
		return rtnInfo;	
	}

}
