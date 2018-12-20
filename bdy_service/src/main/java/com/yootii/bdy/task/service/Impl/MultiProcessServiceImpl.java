package com.yootii.bdy.task.service.Impl;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import javax.annotation.Resource;

import com.yootii.bdy.bill.model.ChargeItem;
import com.yootii.bdy.bill.service.ChargeItemService;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.Globals;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.ipservice.model.AgencyService;
import com.yootii.bdy.ipservice.service.AgencyServiceService;
import com.yootii.bdy.model.User;
import com.yootii.bdy.process.service.ProcessService;
import com.yootii.bdy.remind.service.RemindService;
import com.yootii.bdy.security.service.AuthenticationService;
import com.yootii.bdy.task.model.TmCaseTaskToDoList;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseProcessMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseTypeMapper;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.TradeMarkCaseProcess;
import com.yootii.bdy.tmcase.model.TradeMarkCaseType;
import com.yootii.bdy.tmcase.service.TradeMarkCaseService;
import com.yootii.bdy.user.service.UserService;
import com.yootii.bdy.util.ServiceUrlConfig;
import com.yootii.bdy.util.TaskTool;
import com.yootii.bdy.common.Constants;

import org.springframework.stereotype.Component;


//---- 代理机构使用的接口实现类 -------
@Component
public class MultiProcessServiceImpl {

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
	private TradeMarkCaseService  tradeMarkCaseService;
	
	@Resource
	private UserService userService;
	
	@Resource
	private ChargeItemService chargeItemService;
	
	@Resource
	private AgencyServiceService agencyServiceService;
	

	@Resource
	private RemindService remindService;
	
	@Resource
	private TradeMarkCaseTypeMapper tradeMarkCaseTypeMapper;

	// ---- 代理机构使用的接口 -------
	

	// 接收案件接口
	public ReturnInfo transferCase(GeneralCondition gcon, String userId, String processInstanceId, String agencyLevel, String agencyServiceId, TradeMarkCase tmCase) {
				
		ReturnInfo rtnInfo = new ReturnInfo();
		
		rtnInfo=TaskTool.checkId("processInstanceId", processInstanceId);
		if (!rtnInfo.getSuccess()) {
			return rtnInfo;
		}
		rtnInfo=TaskTool.checkId("agencyLevel", agencyLevel);
		if (!rtnInfo.getSuccess()) {
			return rtnInfo;
		}	
		
		Integer caseId=tmCase.getId();
		Integer agencyId=tmCase.getAgencyId();	
		Integer couserId=new Integer(userId);
		String tokenID=gcon.getTokenID();
		
		Integer custId=tmCase.getCustId();
		
		
		try {
			
			//创建合作案件
			rtnInfo = tradeMarkCaseService.tradeMarkCaseAssociate(caseId, agencyId, couserId, gcon);
			if (rtnInfo != null && rtnInfo.getSuccess()){			
				logger.info("create associate case finish");
				Map<String, Object> resData = (Map<String, Object>) rtnInfo.getData();			
				if (resData!=null){
					Integer  assoCaseId=(Integer)resData.get("assoCaseId");
					//设置后续待办任务使用新创建的案件的caseId
					tmCase.setId(assoCaseId);
				}
			}
			
			//获取用户
			User user = userService.getUserById(userId, tokenID);
			Integer uId=new Integer(userId);
			String username=user.getUsername();
			
			//记录案件的处理流程
			String caseStatus="准备申请材料";
			taskCommonImpl.recordTmCaseProcess(caseId, caseStatus, username, uId, 0, 0);
			
			
			int level=Integer.parseInt(agencyLevel);
			level=level+1;
			agencyLevel=level+"";	
		
			AgencyService agencyService=agencyServiceService.getAgencyService(gcon, agencyId.toString(), agencyServiceId);
			
			if (agencyService==null){
				throw new Exception("can not get service of cooperative agency, current agency Id is "+ agencyId);
			}
			Integer asId=agencyService.getAgencyServiceId();
			
			String newAencyServiceId=null;
			
			if (asId==null){
				logger.info("can not get aencyServiceId of "+agencyId+" acording to current service");
			}else{			
				newAencyServiceId=asId.toString();
			}
		
			String prokey="trademark_case_start";
			
			boolean appSelf=false;
		
			String permission="案件分配";
			Map<String, Object> map=new HashMap<String, Object>();
            map.put("gcon", gcon);
            map.put("userId", userId);
            map.put("customerId", custId.toString());
            map.put("agencyServiceId", newAencyServiceId);
            map.put("appSelf", appSelf);
            map.put("tmCase", tmCase);
            map.put("permission", permission);
            map.put("processInstanceId", processInstanceId);
            map.put("agencyLevel", agencyLevel);
            map.put("prokey", prokey);
            
			rtnInfo = taskBasicServiceImpl.startTmCaseProcess(map);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());			
		}		
		
		return rtnInfo;
	}
	
	
	
	
	// 报告拒绝处理的消息的接口
	public ReturnInfo notifyRefuseCase(GeneralCondition gcon, String userId,
			String processInstanceId, String agencyLevel) {
		
		ReturnInfo rtnInfo = new ReturnInfo();
		// 任务状态为1，代表拒绝处理该案件
		String key="tasktatus";
		String value="1";
		Map<String, Object> runMap = new HashMap<String, Object>();		
		runMap.put(key, value);		
					
		String activityId="catchCaseResultMessage";
		String messageName="caseResultMessage";
		rtnInfo=notifyCore(processInstanceId, activityId, messageName, runMap);		
		
		return rtnInfo;
	}
	
	

	
	// 报告拒绝处理的消息的接口
	public ReturnInfo notifyCaseResult(GeneralCondition gcon, String userId,
			String processInstanceId, String agencyLevel) {
		
		ReturnInfo rtnInfo = new ReturnInfo();
		// 任务状态为2，代表结束案件
		String key="tasktatus";
		String value="2";
		Map<String, Object> runMap = new HashMap<String, Object>();		
		runMap.put(key, value);		
					
		String activityId="catchCaseResultMessage";
		String messageName="caseResultMessage";
		rtnInfo=notifyCore(processInstanceId, activityId, messageName, runMap);		
		
		return rtnInfo;
	}
	
	
	// 报告网申结果的接口
	public ReturnInfo notifyAppResult(GeneralCondition gcon, String submitStatus,
			String processInstanceId) {		
		ReturnInfo rtnInfo = new ReturnInfo();		
		
		rtnInfo=TaskTool.checkId("processInstanceId", processInstanceId);
		if (!rtnInfo.getSuccess()) {
			return rtnInfo;
		}			
		
		try {
			Map<String, Object> runMap = new HashMap<String, Object>();
			boolean submitresult=false;
			String taskName="修改案件信息";
			
			if (submitStatus.equals("1")){
				submitresult=true;
				taskName="录入官文";
			}
			
			String permission = TaskTool.getPermission(taskName);
			runMap.put("submitresult", submitresult);
			runMap.put("permission", permission);
				

			String activityId="catchAppResultMessage";
			String messageName="appResultMessage";		
			
			// 给客户发送消息，告知官方通知，以便客户做决定
			//如果该消息启动流程是一个独立流程，将采用startProcessInstanceByMessage方式启动该流程
	        //目前因为是子流程，所以，仍然采用messageEventReceived方式
			rtnInfo=notifyCore(processInstanceId, activityId, messageName, runMap);		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("");
		}
		
		
		
//			processService.startProcessByMessage(messageName, runMap);
		return rtnInfo;
	}
	
	
	
	
	
	
	// 报告子案件结果的接口
	public ReturnInfo notifyChildCaseResult(GeneralCondition gcon, String caseId, 
			String processInstanceId) {		
		ReturnInfo rtnInfo = new ReturnInfo();		
		
		rtnInfo=TaskTool.checkId("processInstanceId", processInstanceId);
		if (!rtnInfo.getSuccess()) {
			return rtnInfo;
		}			
		
		try {
			Integer id=new Integer(caseId);
			TradeMarkCase tmCase=tradeMarkCaseMapper.selectByPrimaryKey(id);
			if (tmCase==null){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("案件不存在，caseId: "+ caseId);
				return rtnInfo;
			}
			String caseType=tmCase.getCaseType();
			String caseStatus=tmCase.getStatus();
			Integer parentId=tmCase.getParentId();
			
			if (parentId==null){
				rtnInfo.setSuccess(true);
				rtnInfo.setMessage("父案件不存在，无需通知");
				return rtnInfo;
			}
			
			
			boolean success=false;
			if (caseType.equals("商标驳回复审") || caseType.equals("商标异议答辩") || caseType.equals("商标不予注册复审") || caseType.equals("商标诉讼案件")){			
				if (caseStatus.equals("成功")){
					success=true;
				}	
			}	
			
			Map<String, Object> runMap = new HashMap<String, Object>();
            String permission = "录入官文";
			//因为驳回复审，异议答辩，不予注册复审这些案件的得出结果后，要恢复父案件的执行
            //从流程图可以看到，下一个任务中有需要录入官文的
            //所以，在此，先把下一个任务所需要的权限传递到过去。
			runMap.put("permission", permission);
			runMap.put("success", success);
			
			String activityId="";
			String messageName="";				
			if (caseType.equals("商标驳回复审")){			
				activityId="catchRejectResultMessage";
				messageName="rejectResultMessage";	
			}
			if (caseType.equals("商标异议答辩")){			
				activityId="catchObjectionDefenseResultMessage";
				messageName="objectionDefenseResultMessage";	
			}
			else {			
				activityId="catchChildCaseResultMessage";
				messageName="childCaseResultMessage";	
			}
			
			
			
			// 给客户发送消息，告知官方通知，以便客户做决定
			//如果该消息启动流程是一个独立流程，将采用startProcessInstanceByMessage方式启动该流程
	        //目前因为是子流程，所以，仍然采用messageEventReceived方式
			rtnInfo=notifyCore(processInstanceId, activityId, messageName, runMap);		
			
			
			//记录父案件的处理流程
			if (parentId!=null){
				caseStatus=caseType+ "结果：" + success;			
				String username="system";
				Integer uId=93;			
				taskCommonImpl.recordTmCaseProcess(parentId, caseStatus, username, uId, 0, 0);
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("");
		}
		
		
		
//					processService.startProcessByMessage(messageName, runMap);
		return rtnInfo;
	}
	
	

	// 报告官方通知的接口
	public ReturnInfo notifyOfficialNotice(GeneralCondition gcon, String userId,String custId,
			String processInstanceId, String lastAgencyLevel, String fileName, String lastProcessInstanceId) {
		
		ReturnInfo rtnInfo = new ReturnInfo();		
		rtnInfo=TaskTool.checkFileName(fileName);
		if (!rtnInfo.getSuccess()) {
			return rtnInfo;
		}		
		rtnInfo=TaskTool.checkId("lastAgencyLevel", lastAgencyLevel);
		if (!rtnInfo.getSuccess()) {
			return rtnInfo;
		}
		rtnInfo=TaskTool.checkId("lastProcessInstanceId", lastProcessInstanceId);
		if (!rtnInfo.getSuccess()) {
			return rtnInfo;
		}
		
		
		
		Map<String, Object> runMap = new HashMap<String, Object>();
		
				
		runMap.put("fileName", fileName);
		runMap.put("lastAgencyLevel", lastAgencyLevel);
		runMap.put("lastProcessInstanceId", lastProcessInstanceId);	

		String activityId="catchOfficialNoticeMessage";
		String messageName="officialNoticeMessage";		
		
		// 给客户发送消息，告知官方通知，以便客户做决定
		//如果该消息启动流程是一个独立流程，将采用startProcessInstanceByMessage方式启动该流程
        //目前因为是子流程，所以，仍然采用messageEventReceived方式
		rtnInfo=notifyCore(processInstanceId, activityId, messageName, runMap);		
		
//		processService.startProcessByMessage(messageName, runMap);
		return rtnInfo;
	}
	
	

	// 报告客户决定的接口
	public ReturnInfo notifyCustomerDecision(GeneralCondition gcon, String userId,
			String processInstanceId,String agencyLevel, String lastAgencyLevel, String customerDecision, String fileName ) {
		
		ReturnInfo rtnInfo = new ReturnInfo();
		
		rtnInfo=TaskTool.checkId("fileName", fileName);
		if (!rtnInfo.getSuccess()) {
			return rtnInfo;
		}
		rtnInfo=TaskTool.checkId("agencyLevel", agencyLevel);
		if (!rtnInfo.getSuccess()) {
			return rtnInfo;
		}
		rtnInfo=TaskTool.checkId("lastAgencyLevel", lastAgencyLevel);
		if (!rtnInfo.getSuccess()) {
			return rtnInfo;
		}
			
	
		try {
			Map<String, Object> runMap = new HashMap<String, Object>();
			
			// approved为true，代表客户同意该案件
			runMap.put("approved", customerDecision);		
						
			
			// 获取流程参数	
			String activityId=null;
			
//			Map<String, Object> proMap = taskCommonImpl.getProcessVarialbleByPro(processInstanceId);
//			Object obj = proMap.get("maxAgencyLevel");
//			if (obj == null) {
			
			int currentLevel=Integer.parseInt(agencyLevel);			
			int lastLevel=Integer.parseInt(lastAgencyLevel);
			int result=lastLevel-currentLevel;
			if (result>1){
				activityId="catchCustomerDecisionMessage";
			}
			else{
				activityId=TaskTool.getActivityId(fileName);
				
				if (activityId==null){
					rtnInfo.setSuccess(false);
					rtnInfo.setMessage("代表官方通知的fileName不正确，获取不到对应的activityId");
					rtnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
					return rtnInfo;
				}
				
				if(activityId.equals("catchCustomerDecisionMessage2") ) {
					TradeMarkCase tradeMarkCase = (TradeMarkCase)tradeMarkCaseService.queryTradeMarkCaseByProcessId(Integer.valueOf(processInstanceId)).getData();
					//判断是用户 是 拒绝的 并且 文件是 部分驳回的 才能创建分割申请
					if(customerDecision.equals("false") && fileName.equals("303")){
						remindService.insertRemindByType(8, new Date(), null, tradeMarkCase.getCustId(), tradeMarkCase.getId(), gcon);
					}
				}
				
			}
			
			String messageName="customerDecisionMessage";		
			//驳回复审的流程图中，消息的名称改了，所以，在此设置一下驳回复审案件中所用的消息名称
			if(fileName.equals("325") || fileName.equals("350")){// 驳回复审案件-评审意见反馈给客户后，捕获客户决定的消息
				messageName="customerDecisionResultMessage";
			}
			rtnInfo=notifyCore(processInstanceId, activityId, messageName, runMap);	
			
			
		} catch (Exception e) {
			// 关闭流程
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			return rtnInfo;
		}	
					
		
		
		
		return rtnInfo;
	}
	
	
	
	
	// 报告消息的核心代码
	public ReturnInfo notifyCore(String processInstanceId, String activityId, String messageName, Map<String, Object> runMap) {
		
		ReturnInfo rtnInfo = new ReturnInfo();
	
		rtnInfo=TaskTool.checkId("processInstanceId", processInstanceId);
		if (!rtnInfo.getSuccess()) {
			return rtnInfo;
		}
//		rtnInfo=TaskTool.checkId("agencyLevel", agencyLevel);
//		if (!rtnInfo.getSuccess()) {
//			return rtnInfo;
//		}		
//		
//		int level=Integer.parseInt(agencyLevel);
//		if (level>1){			
//			level--;			
//		}
//		
//		String currentLevel=level+"";
//		
//		runMap.put("currentLevel", currentLevel);	
//		runMap.put("agencyLevel", currentLevel);	
	
		rtnInfo=taskBasicServiceImpl.resumeProcess(processInstanceId, activityId, messageName, runMap);
		
		return rtnInfo;
	}
	
		
	
	
	
	// 创建驳回复审案件的接口
	public ReturnInfo createRejectCase(GeneralCondition gcon, String userId, String processInstanceId, TradeMarkCase tmCase){
		ReturnInfo rtnInfo = new ReturnInfo();
		
		String caseType="商标驳回复审";
		//获取caseTypeId
		TradeMarkCaseType tradeMarkCaseType=tradeMarkCaseTypeMapper.selectByCaseType(caseType);
		Integer caseTypeId=tradeMarkCaseType.getCaseTypeId();		
		tmCase.setCaseTypeId(caseTypeId);		
		rtnInfo = createNewCase(gcon, userId, processInstanceId, tmCase);
			
		return rtnInfo;

	}
	
	
	
	
	
	// 创建异议答辩案件的接口
	public ReturnInfo createObjectionDefenseCase(GeneralCondition gcon, String userId, String processInstanceId, TradeMarkCase tmCase){
	    ReturnInfo rtnInfo = new ReturnInfo();
		
		String caseType="商标异议答辩";
		//获取caseTypeId
		TradeMarkCaseType tradeMarkCaseType=tradeMarkCaseTypeMapper.selectByCaseType(caseType);
		Integer caseTypeId=tradeMarkCaseType.getCaseTypeId();		
		tmCase.setCaseTypeId(caseTypeId);		
		rtnInfo = createNewCase(gcon, userId, processInstanceId, tmCase);
		
		
		return rtnInfo;
	}
	
	

	
	// 创建商标诉讼案件的接口
	public ReturnInfo createLawsuitCase(GeneralCondition gcon, String userId, String processInstanceId, TradeMarkCase tmCase){
		ReturnInfo rtnInfo = new ReturnInfo();	
	
		String caseType="商标诉讼案件";
		
		//获取caseTypeId
		TradeMarkCaseType tradeMarkCaseType=tradeMarkCaseTypeMapper.selectByCaseType(caseType);
		Integer caseTypeId=tradeMarkCaseType.getCaseTypeId();		
		tmCase.setCaseTypeId(caseTypeId);		
		rtnInfo = createNewCase(gcon, userId, processInstanceId, tmCase);
		
		return rtnInfo;
	}
		

	

	
	// 创建不予注册复审案件的接口
	public ReturnInfo createNewCase(GeneralCondition gcon, String userId, String processInstanceId, TradeMarkCase tmCase){
		ReturnInfo rtnInfo = new ReturnInfo();
		
		Integer id=tmCase.getId();
		Integer custId=tmCase.getCustId();
		Integer agencyId=tmCase.getAgencyId();
		
		//补充创建一个新的案件
//		String caseType="商标不予注册复审";
//		TradeMarkCaseType tradeMarkCaseType=tradeMarkCaseTypeMapper.selectByCaseType(caseType);
//		Integer caseTypeId=tradeMarkCaseType.getCaseTypeId();
		
		Integer caseTypeId=tmCase.getCaseTypeId();
		TradeMarkCaseType tradeMarkCaseType=tradeMarkCaseTypeMapper.selectByPrimaryKey(caseTypeId);
		String caseType=tradeMarkCaseType.getCaseType();
		
		
		rtnInfo = tradeMarkCaseService.createChildCase(id, caseType, caseTypeId);		
		if (!rtnInfo.getSuccess()){			
			return rtnInfo;
		}		
		if (rtnInfo.getData()==null){					
			rtnInfo.setSuccess(false);
			rtnInfo.setMessageType(-2);
			rtnInfo.setMessage("创建新的案件的返回值缺少数据");
			return rtnInfo;
		}		
		
		Map<String, Object> resData = (Map<String, Object>) rtnInfo.getData();
		Integer newCaseId=(Integer)resData.get("childCaseId");	
		tmCase.setId(newCaseId);
		
		String taskName="案件审核";
		
		String permission;
		
		//高级服务
		Integer serviceType=12;
		
		//获取该代理所的该案件类型对应的服务编号
		AgencyService agencyService=agencyServiceService.queryAgencyService(gcon, caseTypeId, agencyId, serviceType);		
		Integer agencyServiceId=agencyService.getAgencyServiceId();		
		try {
			permission = TaskTool.getPermission(taskName);
			
			String prokey=TaskTool.getProcessKey(caseTypeId);
			
			boolean appSelf=false;
			
			String customerId=null;
			if (custId!=null){
				customerId=custId.toString();
			}
			String agencyLevel="1";
			Map<String, Object> map=new HashMap<String, Object>();
            map.put("gcon", gcon);
            map.put("userId", userId);
            map.put("customerId", customerId);
            map.put("agencyServiceId", agencyServiceId.toString());
            map.put("appSelf", appSelf);
            map.put("tmCase", tmCase);
            map.put("permission", permission);
            map.put("processInstanceId", processInstanceId);
            map.put("agencyLevel", agencyLevel);
            map.put("prokey", prokey);
            
			rtnInfo = taskBasicServiceImpl.startTmCaseProcess(map);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
		}		
		rtnInfo.setSuccess(true);
		return rtnInfo;
	}	
	
	

}
