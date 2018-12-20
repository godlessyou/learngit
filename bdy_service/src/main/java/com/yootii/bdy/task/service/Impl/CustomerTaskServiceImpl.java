package com.yootii.bdy.task.service.Impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Resource;

import com.yootii.bdy.common.Constants;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.Globals;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.customer.model.Customer;
import com.yootii.bdy.customer.service.CustomerService;
import com.yootii.bdy.ipservice.model.AgencyService;
import com.yootii.bdy.mail.service.MailService;
import com.yootii.bdy.model.User;
import com.yootii.bdy.process.service.ProcessService;
import com.yootii.bdy.remind.service.RemindService;
import com.yootii.bdy.task.model.MailRecord;
import com.yootii.bdy.task.model.TmCaseTaskToDoList;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseCategoryMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseColRecordMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseJoinAppRecordMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseProcessMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseCategoryRecordMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseRecordMapper;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.TradeMarkCaseCategory;
import com.yootii.bdy.tmcase.model.TradeMarkCaseColRecord;
import com.yootii.bdy.tmcase.model.TradeMarkCaseJoinApp;
import com.yootii.bdy.tmcase.model.TradeMarkCaseJoinAppRecord;
import com.yootii.bdy.tmcase.model.TradeMarkCaseProcess;
import com.yootii.bdy.tmcase.model.TradeMarkCaseCategoryRecord;
import com.yootii.bdy.tmcase.model.TradeMarkCaseRecord;
import com.yootii.bdy.trademark.model.Trademark;
import com.yootii.bdy.trademark.service.TradeMarkService;
import com.yootii.bdy.user.service.UserService;
import com.yootii.bdy.util.ObjectUtil;
import com.yootii.bdy.util.ServiceUrlConfig;
import com.yootii.bdy.util.TaskTool;

import org.springframework.objenesis.instantiator.sun.MagicInstantiator;
import org.springframework.stereotype.Component;


//---- 客户使用的接口实现类 -------
@Component
public class CustomerTaskServiceImpl  {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private ProcessService processService;

	
	@Resource
	private ServiceUrlConfig serviceUrlConfig;

	@Resource
	private TaskCommonImpl taskCommonImpl;
	

	@Resource
	private TaskBasicServiceImpl taskBasicServiceImpl;
	
	@Resource
	private MultiProcessServiceImpl multiProcessServiceImpl;
	
	@Resource
	private UserService userService;
	
	@Resource
	private CustomerService customerService;
	
	@Resource
	private TradeMarkCaseProcessMapper tradeMarkCaseProcessMapper;
	
	@Resource
	private TradeMarkCaseMapper tradeMarkCaseMapper;
	
	@Resource
	private TradeMarkCaseCategoryMapper tradeMarkCaseCategoryMapper;
	
	@Resource
	private TradeMarkCaseRecordMapper trademarkCaseRecordMapper;	
	
	@Resource
	private TradeMarkCaseCategoryRecordMapper trademarkCaseCategoryRecordMapper;
	
	@Resource
	private TradeMarkCaseJoinAppRecordMapper trademarkCaseJoinAppRecordMapper;
	
	@Resource
	private TradeMarkCaseColRecordMapper trademarkCaseColRecordMapper;	
	
	@Resource
	private	CaseChargeRecordServiceImpl caseChargeRecordServiceImpl;
	
	@Resource
	private	MailService mailService;
	
	@Resource
	private RemindService remindService;
	
	
	@Resource
	private TradeMarkService tradeMarkService;
	
	
	// ---- 客户使用的接口 -------

	// 启动商标注册流程
	public ReturnInfo startTmRegisterProcess(Map<String, Object> map) {
		ReturnInfo rtnInfo = new ReturnInfo();
		String userId=(String)map.get("userId");
		String customerId=(String)map.get("customerId");
		GeneralCondition gcon=(GeneralCondition)map.get("gcon");
		TradeMarkCase tmCase=(TradeMarkCase)map.get("tmCase");
		Integer serviceId=(Integer)map.get("serviceId");
		String agencyServiceId=(String)map.get("agencyServiceId");
		
		String tokenID=gcon.getTokenID();
		Integer caseId=tmCase.getId();	
		
		Integer agencyId=tmCase.getAgencyId();
		
		//获取用户
		User user;
		try {
			String username=null;
			Integer uId=0;
			Integer custId=0;
			if (customerId!=null && !customerId.equals("")){
				Customer customer = customerService.getCustById(customerId, tokenID);
				custId=new Integer(customerId);
				username=customer.getUsername();
			}
			if (userId!=null && !userId.equals("")){
				user = userService.getUserById(userId, tokenID);
				uId=new Integer(userId);
				username=user.getUsername();
			}			
            String agencyLevel=null;
            String prokey= null;
            boolean appSelf=false;
            String caseStatus="立案";
            String taskName="案件分配";    	
            
            String submitType=null;
            
//            boolean submitresult=false;
            if (serviceId==8){
            	prokey="trademark_case_self";
            	appSelf=true;
//            	caseStatus="网上递交申请";
            	taskName="录入公告";
            	submitType="网上申请";            		
//            	submitresult=true;
            }
            else if (serviceId==9){
            	prokey="trademark_case_start";   
            } 
            
            
          //创建账单记录	
			caseChargeRecordServiceImpl.createChargeRecords(gcon, userId, caseId.toString(), agencyId.toString(), agencyServiceId);
		
            
            
            if (submitType!=null){
	            // 如果用户选择自助注册，那么设置案件的递交方式为"网上申请"
            	// 如果用户选择高级注册，根据用户选择的递交方式再进行
	            Integer id=tmCase.getId();
	            if (id!=null){
		            TradeMarkCase record=new TradeMarkCase();
		            record.setId(id);
		            record.setSubmitType(submitType);
		            tradeMarkCaseMapper.updateByPrimaryKeySelective(record);
	            }
            }
            
            
            
            String permission = TaskTool.getPermission(taskName);            
            String processInstanceId=null;
       
            map.put("appSelf", appSelf);         
            map.put("permission", permission);
            map.put("processInstanceId", processInstanceId);
            map.put("agencyLevel", agencyLevel);
            map.put("prokey", prokey);  
            
          //为了测试时，流程能走通，暂且设置该变量
//            if(submitresult){
//            	map.put("submitresult", true);
//            }
            
            
          //记录案件的处理流程
			taskCommonImpl.recordTmCaseProcess(caseId, caseStatus, username, uId, custId, 0);	
          	           
			rtnInfo = taskBasicServiceImpl.startTmCaseProcess(map);
			
						
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());				
		}			
		
		

		return rtnInfo;
	}
	
	// 启动商标变更流程
	public ReturnInfo startTmChangeProcess(Map<String, Object> map) {
		ReturnInfo rtnInfo = new ReturnInfo();
		String userId=(String)map.get("userId");
		String customerId=(String)map.get("customerId");
		GeneralCondition gcon=(GeneralCondition)map.get("gcon");
		TradeMarkCase tmCase=(TradeMarkCase)map.get("tmCase");
		Integer serviceId=(Integer)map.get("serviceId");
		
		String agencyServiceId=(String)map.get("agencyServiceId");
		
		String tokenID=gcon.getTokenID();
		Integer caseId=tmCase.getId();	
		
		Integer agencyId=tmCase.getAgencyId();
		
		//获取用户
		User user;
		try {
			Integer uId=0;
			Integer custId=0;
			String username=null;			
			if (customerId!=null && !customerId.equals("")){
				Customer customer = customerService.getCustById(customerId, tokenID);
				custId=customer.getId();
				username=customer.getUsername();
			}			
			if (userId!=null && !userId.equals("")){
				user = userService.getUserById(userId, tokenID);
				uId=user.getUserId();
				username=user.getUsername();
			}
						
            String agencyLevel=null;
            String prokey= null;
            boolean appSelf=false;
            String caseStatus="立案";
            String taskName="案件分配";    
            
            String submitType=null;
            
            if (serviceId==23||serviceId==26||serviceId==28||serviceId==30||serviceId==32){
            	prokey="trademark_change_self";
            	appSelf=true;
//            	caseStatus="网上递交申请";
            	taskName="录入公告";
            	submitType="网上申请";
//            	submitresult=true;
      		
            }
            else if (serviceId==24||serviceId==25||serviceId==27||serviceId==29||serviceId==31){
            	prokey="trademark_change_start";            	
            }   
            
          	//创建账单记录	
        	caseChargeRecordServiceImpl.createChargeRecords(gcon, userId, caseId.toString(), agencyId.toString(), agencyServiceId);
        
            
            if (submitType!=null){
	            // 如果用户选择自助注册，那么设置案件的递交方式为"网上申请"
            	// 如果用户选择高级注册，根据用户选择的递交方式再进行
	            Integer id=tmCase.getId();
	            if (id!=null){
		            TradeMarkCase record=new TradeMarkCase();
		            record.setId(id);
		            record.setSubmitType(submitType);
		            tradeMarkCaseMapper.updateByPrimaryKeySelective(record);
	            }
            }
            String permission = TaskTool.getPermission(taskName);            
            String processInstanceId=null;
       
            map.put("appSelf", appSelf);         
            map.put("permission", permission);
            map.put("processInstanceId", processInstanceId);
            map.put("agencyLevel", agencyLevel);
            map.put("prokey", prokey);  
            
          //为了测试时，流程能走通，暂且设置该变量
//            if(submitresult){
//            	map.put("submitresult", true);
//            }
            
            //记录案件的处理流程
			taskCommonImpl.recordTmCaseProcess(caseId, caseStatus, username, uId, custId, 0);
			
			
			rtnInfo = taskBasicServiceImpl.startTmCaseProcess(map);
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());				
		}			
		
		

		return rtnInfo;
	}
	
	
	
	//启动商标异议申请案件的流程
	public ReturnInfo startComplexCaseApp(Map<String, Object> map){
		ReturnInfo rtnInfo = new ReturnInfo();
		String taskName = "审核案件";
		String customerId=(String)map.get("customerId");
		String userId=(String)map.get("userId");
		GeneralCondition gcon=(GeneralCondition)map.get("gcon");
		
		TradeMarkCase tradeMarkCase=(TradeMarkCase)map.get("tmCase");
		
		String tokenID=gcon.getTokenID();
		User user;
		try{
			Integer uId=0;
			Integer custId=0;
			String username=null;			
			if (customerId!=null && !customerId.equals("")){
				Customer customer = customerService.getCustById(customerId, tokenID);
				custId=customer.getId();
				username=customer.getUsername();
			}			
			if (userId!=null && !userId.equals("")){
				user = userService.getUserById(userId, tokenID);
				uId=user.getUserId();
				username=user.getUsername();
			}
			
			
          String permission = TaskTool.getPermission(taskName);            
          String processInstanceId=null;
          boolean appSelf = true;
          String agencyLevel="1";
          //trademark_objectionApply_start     trademark_objectionApply
//          String prokey="trademark_complexcase_start"; 
          
          //Modification start, 2018-12-17
          Integer caseTypeId=tradeMarkCase.getCaseTypeId();
          String prokey=TaskTool.getProcessKey(caseTypeId);
          //Modification end
          
          map.put("appSelf", appSelf);         
          map.put("permission", permission);
          map.put("processInstanceId", processInstanceId);
          map.put("agencyLevel", agencyLevel);
          map.put("prokey", prokey);  
          
         
          Integer caseId = tradeMarkCase.getId();
          String caseStatus="立案";
         
          //记录案件的处理流程
		    taskCommonImpl.recordTmCaseProcess(caseId, caseStatus, username, uId, custId, 0);	
			rtnInfo = taskBasicServiceImpl.startTmCaseProcess(map);
		}catch (Exception e) {
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
		}
		rtnInfo.setSuccess(true);
		return rtnInfo;
	}
	
	
	
	
	//修改商标注册申请材料
	public ReturnInfo modifyCase(Map<String, Object> map, Map<String, Object> proMap){
		ReturnInfo rtnInfo = new ReturnInfo();
		try{
			Integer piliangCase=(Integer)map.get("piliangCase");
			String userId=(String)map.get("userId");
			String customerId=(String)map.get("customerId");
			GeneralCondition gcon=(GeneralCondition)map.get("gcon");
			TradeMarkCase tmCase=(TradeMarkCase)map.get("newData");
			TmCaseTaskToDoList toDoList=(TmCaseTaskToDoList)map.get("toDoList");
			// 检查必要的参数
			rtnInfo = TaskTool.checkUserParam(userId, customerId);
			if (!rtnInfo.getSuccess()) {
				return rtnInfo;
			}
			
			// 记录每次修改的数据
			rtnInfo = modifyRecord(map);
			if (!rtnInfo.getSuccess()) {
				return rtnInfo;
			}
						
			Integer tId = toDoList.getTaskId();
			if (tId == null) {
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("待办事项的id不能为空");
				rtnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
				return rtnInfo;
			}
			
			if (tId.intValue()==0){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("待办事项的id为0");
				rtnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
				return rtnInfo;
			}
			
			String tokenID=gcon.getTokenID();
			String taskId = tId.toString();			
			String remarks = toDoList.getRemarks();	
			
			Integer caseId=tmCase.getId();
			
			Integer agencyId=tmCase.getAgencyId();
									
				
			// 获取流程参数	
			if (proMap==null || proMap.size()==0){
				proMap = taskCommonImpl.getProcessVarialble(taskId);
			}
			
			// 修改案件信息不会让流程往下走，只有审核通过，流程才能往下执行
//			rtnInfo = taskBasicServiceImpl.doTask(gcon,userId,customerId,toDoList,taskResult,proMap,runMap);
						
			//修改账单记录	
			String agencyServiceId=taskCommonImpl.getProperyOfProcess(proMap, taskId, "agencyServiceId");
			
			String processInstanceId=taskCommonImpl.getProperyOfProcess(proMap, taskId, "processInstanceId");
			//更新案件的流程实例Id
			TradeMarkCase record=new TradeMarkCase();			
			record.setId(caseId);
			record.setProcessId(processInstanceId);	
			tradeMarkCaseMapper.updateByPrimaryKeySelective(record);
			
			caseChargeRecordServiceImpl.createChargeRecords(gcon, userId, caseId.toString(), agencyId.toString(), agencyServiceId);
			
			String caseStatus="修改申请材料";
			Integer uId=null;
			String username=null;
			if (userId!=null && !userId.equals("")){
				User user=userService.getUserById(userId, tokenID);
				uId=new Integer(userId);
				username=user.getUsername();
				
			}else{
				Customer customer=customerService.getCustById(customerId, tokenID);				
				username=customer.getUsername();
			}
			

			//创建案件的流程数据
			taskCommonImpl.recordTmCaseProcess(caseId, caseStatus, username, uId, 0, 1);
			
			
		
			if(piliangCase==0){ //如果是一个案件，那么给用户发送一封邮件，如果是批量案件，不发送邮件。
				// 给客户发送邮件	
				if (remarks!=null && !remarks.equals("")){
					taskCommonImpl.sendComunicationMail(proMap, taskId, remarks, gcon, userId, customerId);				
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

	
	
	// 同意接口
	public ReturnInfo agree(GeneralCondition gcon, 
			String customerId, TmCaseTaskToDoList toDoList) {
		
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
		
		
			String key="approved";
			boolean value=true;
			String permission="提交材料";
			Map<String, Object> runMap = new HashMap<String, Object>();		
			runMap.put(key, value);
			runMap.put("result", true);
			String taskResult="完成";
			String customerDecition=value+"";
		
			// 设置任务执行所必须的参数			
			if (permission != null && !permission.equals("")) {
				runMap.put("permission", permission);
			}
			if (remarks != null && !remarks.equals("")) {
				runMap.put("remarks", remarks);
			}
			
			// 获取流程参数			
			Map<String, Object> proMap = taskCommonImpl.getProcessVarialble(taskId);
			
			rtnInfo = taskBasicServiceImpl.doTask( gcon, null, customerId, toDoList, taskResult, proMap,runMap);
			
			//发送客户决定的消息，使等该该消息的商标注册流程捕获该消息后继续执行
//			String lastProcessInstanceId=taskCommonImpl.getProperyOfProcess(proMap, taskId, "processInstanceId");			
//			String fileName=taskCommonImpl.getProperyOfProcess(proMap, taskId, "fileName");									
//			multiProcessServiceImpl.notifyCustomerDecition(gcon, null, lastProcessInstanceId, "2", customerDecition, fileName);
			
			String tokenID=gcon.getTokenID();
			//获取用户
			Customer customer = customerService.getCustById(customerId, tokenID);
			Integer custId=new Integer(customerId);
			String username=customer.getUsername();
			
			String cId=taskCommonImpl.getProperyOfProcess(proMap, taskId, "caseId");
			Integer caseId=new Integer(cId);
			
			String fileName="";
			Object obj=proMap.get("fileName");
			if (obj!=null){
				fileName=(String)obj;
			}
			
			//Modification start, by yang guang, 2018-10-29
			String caseStatus="客户回复：";
			String operation=null;
			if (fileName!=null){
				String realFileName=Constants.fileNameList.get(fileName);
				operation=Constants.agreeOperation.get(fileName);
				if (operation==null){
					operation="提交";
				}
				caseStatus=caseStatus+operation+realFileName;
				/*
				if(realFileName!=null ){
					int pos=realFileName.indexOf("通知");
					if (pos>-1){
						caseStatus=caseStatus+realFileName.substring(0,pos);
					}					
				}
				*/
				//Modification end
			}
			
			
			//记录案件的处理流程
			taskCommonImpl.recordTmCaseProcess(caseId, caseStatus, username, 0, custId, 1);	
		
		} catch (Exception e) {
			// 关闭流程
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			return rtnInfo;
		}
		return rtnInfo;
	}

	
	
	// 这是以前的接口，暂时不用：提交案件接口
	public ReturnInfo submitCase(GeneralCondition gcon, 
			String userId, String customerId, TmCaseTaskToDoList toDoList) {
		
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
			// 返回结果对象
			String key="success";
			Object value=null;
			String permission="审核案件";
			Map<String, Object> runMap = new HashMap<String, Object>();		
			runMap.put(key, value);
			String taskResult="完成";
			// 设置任务执行所必须的参数			
			if (permission != null && !permission.equals("")) {
				runMap.put("permission", permission);
			}
			// 获取流程参数			
			Map<String, Object> proMap = taskCommonImpl.getProcessVarialble(taskId);	
			if (remarks != null && !remarks.equals("")) {
				runMap.put("remarks", remarks);
				boolean sendToCust = true;
				if (customerId!=null){
					sendToCust = false;
				}
				
				// 给客户发送邮件			
				taskCommonImpl.sendComunicationMail(proMap, taskId, remarks, gcon, userId, customerId);
				
			}
						
			
			
			rtnInfo = taskBasicServiceImpl.doTask(gcon,userId,customerId,toDoList,taskResult,proMap,runMap);	
		} catch (Exception e) {
			// 关闭流程
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			return rtnInfo;
		}
		return rtnInfo;
	}
	
	

	// 关闭案件接口
	public ReturnInfo closeCase(GeneralCondition gcon, 
			String customerId, TmCaseTaskToDoList toDoList) {
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
			String key="success";
			Object value=null;
			String permission="";
			Map<String, Object> runMap = new HashMap<String, Object>();		
			runMap.put(key, value);
			String taskResult="完成";
			// 设置任务执行所必须的参数			
			if (permission != null && !permission.equals("")) {
				runMap.put("permission", permission);
			}
			if (remarks != null && !remarks.equals("")) {
				runMap.put("remarks", remarks);
			}
								
			// 获取流程参数			
			Map<String, Object> proMap = taskCommonImpl.getProcessVarialble(taskId);
			rtnInfo = taskBasicServiceImpl.doTask(gcon,null,customerId,toDoList,taskResult,proMap,runMap);	
		} catch (Exception e) {
			// 关闭流程
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			return rtnInfo;
		}
		return rtnInfo;
	}
	
	
	
	
	//记录对案件数据的修改
	public ReturnInfo modifyRecord(Map<String, Object> map) throws Exception{		
		
		ReturnInfo rtnInfo = new ReturnInfo();
		
		String userId=(String)map.get("userId");
		String customerId=(String)map.get("customerId");
		GeneralCondition gcon=(GeneralCondition)map.get("gcon");
		TradeMarkCase oldData=(TradeMarkCase)map.get("oldData");
		TradeMarkCase newData=(TradeMarkCase)map.get("newData");
				
		String tokenID=gcon.getTokenID();		
		
		String modifyUserName=null;
		if (userId!=null && !userId.equals("")){			
			User user=userService.getUserById(userId, tokenID);
			modifyUserName=user.getUsername();			 
		}else if (customerId!=null && !customerId.equals("")){			
			Customer customer=customerService.getCustById(customerId, tokenID);
			modifyUserName=customer.getUsername();			
		}
			
		//案件编号
		Integer id=oldData.getId();
		
		//获取本次修改的属性
		List<String> propertyNameList=ObjectUtil.getPropertyNamesWithValue(newData);
		
		//与原来数据库的数据进行比较，获取属性值发生了变化的属性集合
		List<String> propertyNames=ObjectUtil.getDifferenceProperty(oldData, newData, propertyNameList);			
		
	
		//记录修改前的数据	
		//只需要将未修改前的对象中的上述属性的值保存到数据库
		TradeMarkCaseRecord trademarkCaseRecord=new TradeMarkCaseRecord();	
		ObjectUtil.setObjectProperty(oldData, trademarkCaseRecord,propertyNames);
		
		Date modifyTime=new Date();
		int modifyFlag=0;
		trademarkCaseRecord.setId(id);
		trademarkCaseRecord.setModifyFlag(modifyFlag);
		trademarkCaseRecord.setModifyTime(modifyTime);
		trademarkCaseRecord.setModifyUserName(modifyUserName);	
		trademarkCaseRecordMapper.insertSelective(trademarkCaseRecord);		
		int oldRecordId=trademarkCaseRecord.getRecordId();
		
				
		//记录修改后的数据			
		TradeMarkCaseRecord trademarkCaseRecord2=new TradeMarkCaseRecord();			
		ObjectUtil.setObjectProperty(newData, trademarkCaseRecord2,propertyNames);	
		modifyFlag=1;	
		trademarkCaseRecord2.setId(id);
		trademarkCaseRecord2.setModifyFlag(modifyFlag);
		trademarkCaseRecord2.setModifyTime(modifyTime);
		trademarkCaseRecord2.setModifyUserName(modifyUserName);
		trademarkCaseRecordMapper.insertSelective(trademarkCaseRecord2);		
		int newRecordId=trademarkCaseRecord2.getRecordId();
			
		//对于商品和服务的数据是否进行过修改进行判断，如果进行过修改，也保存到数据库，否则，从修改的字段集合中删除属性名goods
		processGoods(oldData, newData, propertyNames, oldRecordId, newRecordId, modifyTime,modifyUserName);
		
		//对于共同申请人数据是否进行过修改进行判断，如果进行过修改，也保存到数据库，否则，从修改的字段集合中删除属性名joinApps
		processJoinApps(oldData, newData, propertyNames, oldRecordId, newRecordId, modifyTime,modifyUserName);
				
		
		//将修改的字段名用逗号分隔的方式保存到数据库中
		if(propertyNames!=null && propertyNames.size()>0){
			String modifiedCol= StringUtils.join(propertyNames,",");		
			TradeMarkCaseColRecord tradeMarkCaseColRecord=new TradeMarkCaseColRecord();
			
			tradeMarkCaseColRecord.setId(id);
			tradeMarkCaseColRecord.setRecordId(oldRecordId);
			tradeMarkCaseColRecord.setModifiedCol(modifiedCol);		
			trademarkCaseColRecordMapper.insertSelective(tradeMarkCaseColRecord);
		}
				
		
		rtnInfo.setSuccess(true);
		
		return rtnInfo;
	}
	
	
	private void processGoods(TradeMarkCase oldData, TradeMarkCase newData, List<String>propertyNames, int oldRecordId, int newRecordId, Date modifyTime,String modifyUserName){
		
		boolean hasDifference=false;
		int modifyFlag=0;
		
		List<TradeMarkCaseCategory> newCategoryList=newData.getGoods();		
		List<TradeMarkCaseCategory> oldCategoryList=oldData.getGoods();
		
		for (String name: propertyNames){
			if (name.equals("goods")){
				hasDifference=true;
				break;
			}
		}
		
//		if((newCategoryList==null && oldCategoryList!=null) || (newCategoryList!=null && oldCategoryList==null)){
//			hasDifference=true;
//		}else{	
//			List<Object> list1=new ArrayList<Object>();
//			List<Object> list2=new ArrayList<Object>();			
//			list1.addAll(oldCategoryList);
//			list2.addAll(newCategoryList);
//			
//			hasDifference=ObjectUtil.compareListObject(list1, list2);
//		}	
		
		if (hasDifference){				
				
			//记录修改前的商品服务数据
			if(oldCategoryList!=null){		
				modifyFlag=0;
				for(TradeMarkCaseCategory tcc:oldCategoryList){				
					TradeMarkCaseCategoryRecord record=new TradeMarkCaseCategoryRecord();				
					ObjectUtil.setProperty(tcc, record);
					
					record.setModifyFlag(modifyFlag);			
					record.setModifyTime(modifyTime);
					record.setModifyUserName(modifyUserName);
					record.setRecordId(oldRecordId);
					trademarkCaseCategoryRecordMapper.insertSelective(record);				
				}
			}
			
			//记录修改后的商品服务数据
			if(newCategoryList!=null){
				modifyFlag=1;							
				for(TradeMarkCaseCategory tcc:newCategoryList){				
					TradeMarkCaseCategoryRecord record=new TradeMarkCaseCategoryRecord();				
					ObjectUtil.setProperty(tcc, record);
					
					record.setModifyFlag(modifyFlag);			
					record.setModifyTime(modifyTime);
					record.setModifyUserName(modifyUserName);
					record.setRecordId(newRecordId);
					trademarkCaseCategoryRecordMapper.insertSelective(record);				
				}
			}	
		}
		/*
		else{
			
			//删除goods字段
			Iterator<String> it = propertyNames.iterator();
			while (it.hasNext()) {
				String name=it.next();	
				if (name.equals("goods")){
					it.remove();
					break;
				}
			} 
			
		}*/
		
	}
	
	
	
	private void processJoinApps(TradeMarkCase oldData, TradeMarkCase newData, List<String>propertyNames, int oldRecordId, int newRecordId, Date modifyTime,String modifyUserName){
		boolean hasDifference=false;
		int modifyFlag=0;
		
		List<TradeMarkCaseJoinApp> newJoinAppList=newData.getJoinApps();
		List<TradeMarkCaseJoinApp> oldJoinAppList=oldData.getJoinApps();
		
		for (String name: propertyNames){
			if (name.equals("joinApps")){
				hasDifference=true;
				break;
			}
		}
		
//		if((newJoinAppList==null && oldJoinAppList!=null) || (newJoinAppList!=null && oldJoinAppList==null)){
//			hasDifference=true;
//		}else{	
//			List<Object> list1=new ArrayList<Object>();
//			List<Object> list2=new ArrayList<Object>();			
//			list1.addAll(oldJoinAppList);
//			list2.addAll(newJoinAppList);
//			hasDifference=ObjectUtil.compareListObject(list1,list2);
//		}		
		
		if (hasDifference){				
			//记录修改前的共同申请人数据
			if(oldJoinAppList!=null){		
				modifyFlag=0;
				for(TradeMarkCaseJoinApp tcc:oldJoinAppList){				
					TradeMarkCaseJoinAppRecord record=new TradeMarkCaseJoinAppRecord();				
					ObjectUtil.setProperty(tcc, record);
					
					record.setModifyFlag(modifyFlag);			
					record.setModifyTime(modifyTime);
					record.setModifyUserName(modifyUserName);
					record.setRecordId(oldRecordId);
					trademarkCaseJoinAppRecordMapper.insertSelective(record);				
				}
			}
			
			//记录修改后的共同申请人数据
			if(newJoinAppList!=null){	
				modifyFlag=1;							
				for(TradeMarkCaseJoinApp tcc:newJoinAppList){				
					TradeMarkCaseJoinAppRecord record=new TradeMarkCaseJoinAppRecord();				
					ObjectUtil.setProperty(tcc, record);
					
					record.setModifyFlag(modifyFlag);			
					record.setModifyTime(modifyTime);
					record.setModifyUserName(modifyUserName);
					record.setRecordId(newRecordId);
					trademarkCaseJoinAppRecordMapper.insertSelective(record);				
				}
			}
			
		}
		/*
		else{
			
			//删除joinApps字段
			Iterator<String> it = propertyNames.iterator();
			while (it.hasNext()) {
				String name=it.next();	
				if (name.equals("joinApps")){
					it.remove();
					break;
				}
			}
 			
		}*/
		
	}

				
	
	
}
