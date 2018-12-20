package com.yootii.bdy.process.service.impl;

import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;









import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.Globals;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.customer.model.Customer;
import com.yootii.bdy.process.service.ProcessService;
import com.yootii.bdy.model.User;
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.user.service.UserService;
//import com.yootii.bdy.user.service.UserService;
import com.yootii.bdy.util.GraspUtil;
import com.yootii.bdy.util.JsonUtil;
import com.yootii.bdy.util.ServiceUrlConfig;
import com.yootii.bdy.util.TaskTool;

@Service("processService")
public class ProcessServiceImpl implements ProcessService {
	
	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Resource	
	private ServiceUrlConfig serviceUrlConfig;
	
	@Resource	
	private UserService userService;
	

	@Override
	public ReturnInfo doTask(String taskId,Map<String,Object> runMap) {
		// 返回结果对象
		ReturnInfo rtnInfo = new ReturnInfo();
		try {
			String runMapUrl = "";
			for(Map.Entry<String, Object> run :runMap.entrySet()) {
				runMapUrl = runMapUrl +"&run_"+run.getKey()+"="+run.getValue().toString();
				if(run.getKey() == "permission") {
					String taskName = run.getValue().toString();	
					runMapUrl = runMapUrl +"&run_pageId="+TaskTool.getPageId(taskName,false, false );					
					
				}
			}
			String url=serviceUrlConfig.getProcessEngineUrl()+"/Task/dotask?taskId="+ taskId+runMapUrl;
			logger.info(url);
			String jsonString;

			jsonString = GraspUtil.getText(url);
			rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);
			if(!rtnInfo.getSuccess()) {
				throw new Exception(rtnInfo.getMessage());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(-1);
		} 
		return rtnInfo;
	}

	@Override
	public ReturnInfo deleteProcess(String processInstanceId) {
		// 返回结果对象
		ReturnInfo rtnInfo = new ReturnInfo();
		try {

			String url=serviceUrlConfig.getProcessEngineUrl()+"/Process/deleteProcess?processInstanceId="+ processInstanceId;
			logger.info(url);
			String jsonString;

			jsonString = GraspUtil.getText(url);
			rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(-1);
		} 
		return rtnInfo;
	}

	@Override
	public ReturnInfo startProcessByKey(String proKey, Map<String, Object> runMap) {
		// 返回结果对象
		ReturnInfo rtnInfo = new ReturnInfo();
		try {
			String runMapUrl = "";
			for(Map.Entry<String, Object> run :runMap.entrySet()) {
				runMapUrl = runMapUrl +"&run_"+run.getKey()+"="+run.getValue().toString();
				if(run.getKey() == "permission") {
					String taskName = run.getValue().toString();	
					runMapUrl = runMapUrl +"&run_pageId="+TaskTool.getPageId(taskName,false, false );					
					
				}
			}

			String url=serviceUrlConfig.getProcessEngineUrl()+"/Process/startProcessByKey?bpmnKey="+ proKey+runMapUrl;
			logger.info(url);
			String jsonString;

			jsonString = GraspUtil.getText(url);
			rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(-1);
		} 
		return rtnInfo;
	}


	@Override
	public ReturnInfo checkuserstart(String permission,String userId,GeneralCondition gcon) {
		ReturnInfo rtnInfo = new ReturnInfo();
		try {
					
            String tokenID=gcon.getTokenID();
			boolean hasPermission=userService.checkUserPermission(permission,userId,tokenID);
			if (!hasPermission){
				throw new Exception("无权限");
			}
			
			rtnInfo.setSuccess(true);
		} catch (Exception e) {
//			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(Globals.MESSAGE_TYPE_AUTHORTY_INVALID);
		}
		return rtnInfo;
	}
	

	@Override
	public ReturnInfo setTaskUser(List<String>  proIds,List<String>  adduserIds,List<String>  deluserIds) {
	// 返回结果对象
			ReturnInfo rtnInfo = new ReturnInfo();
			try {
				String runMapUrl = "";
				String proIdsurl = "proIds=";
				proIdsurl += StringUtils.join(proIds.toArray(),",");
				runMapUrl += proIdsurl.length() >= 8 ? proIdsurl:"";
				
				String adduserIdsurl = "adduserIds=";
				adduserIdsurl += StringUtils.join(adduserIds.toArray(),",");
				runMapUrl += adduserIdsurl.length() >= 12 ? adduserIdsurl:"";
				
				String  deluserIdsurl = "deluserIds=";
				deluserIdsurl += StringUtils.join(deluserIds.toArray(),",");
				runMapUrl += deluserIdsurl.length() >= 12 ? deluserIdsurl:"";

				String url=serviceUrlConfig.getProcessEngineUrl()+"/Process/setTaskUser?"+runMapUrl;
				logger.info(url);
				String jsonString;

				jsonString = GraspUtil.getText(url);
				rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage(e.getMessage());
				rtnInfo.setMessageType(-1);
			} 
			return rtnInfo;
		}

	@Override
	public ReturnInfo checkcuststart(String proKey,Customer customer, Map<String, Object> runMap) {
		ReturnInfo rtnInfo = new ReturnInfo();
		try {
			int i = proKey.indexOf(":");
	
			//if(!userService.hasPermission(getDefName(proKey.substring(0, i))+":启动", user)) throw new Exception("无权限");
			runMap.put("group1","");
			runMap.put("group2","");
			runMap.put("group3","");
			//runMap.put("agent",user.getAgency().getId());
			rtnInfo.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(-1);
		}
		return rtnInfo;
	}



	@Override
	public ReturnInfo startProcessById(String bpmnId, Map<String, Object> runMap) {
		// 返回结果对象
		ReturnInfo rtnInfo = new ReturnInfo();
		try {
			String runMapUrl = "";
			for(Map.Entry<String, Object> run :runMap.entrySet()) {
				runMapUrl = runMapUrl +"&run_"+run.getKey()+"="+run.getValue().toString();
			}

			String url=serviceUrlConfig.getProcessEngineUrl()+"/Process/startProcessById?bpmnId="+ bpmnId+runMapUrl;
			logger.info(url);
			String jsonString;

			jsonString = GraspUtil.getText(url);
			rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(-1);
		} 
		return rtnInfo;
	}
	
	
	
	
	public ReturnInfo resumeProcess(String processInstanceId, String activityId, String messageName, Map<String, Object> runMap) {
		// 返回结果对象
		ReturnInfo rtnInfo = new ReturnInfo();
		try {
			String runMapUrl = "";
			for(Map.Entry<String, Object> run :runMap.entrySet()) {
				runMapUrl = runMapUrl +"&run_"+run.getKey()+"="+run.getValue().toString();
			}

			String url=serviceUrlConfig.getProcessEngineUrl()+"/Process/resumeProcess?processInstanceId="+ processInstanceId+"&activityId="+ activityId+"&messageName="+ messageName+runMapUrl;
			logger.info(url);
			String jsonString;

			jsonString = GraspUtil.getText(url);
			rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(-1);
		} 
		return rtnInfo;
	}
	
	
	public ReturnInfo startProcessByMessage(String messageName,  Map<String, Object> runMap ) {
		// 返回结果对象
		ReturnInfo rtnInfo = new ReturnInfo();
		try {
			String runMapUrl = "";
			for(Map.Entry<String, Object> run :runMap.entrySet()) {
				runMapUrl = runMapUrl +"&run_"+run.getKey()+"="+run.getValue().toString();
			}

			String url=serviceUrlConfig.getProcessEngineUrl()+"/Process/startProcessByMessage?messageName="+ messageName;
			logger.info(url);
			String jsonString;

			jsonString = GraspUtil.getText(url);
			rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(-1);
		} 
		return rtnInfo;
	}
	
	
	

	@Override
	public ReturnInfo doTaskByPro(String proId, Map<String, Object> runMap) {
		// 返回结果对象
		ReturnInfo rtnInfo = new ReturnInfo();
		try {
			String runMapUrl = "";
			for(Map.Entry<String, Object> run :runMap.entrySet()) {
				runMapUrl = runMapUrl +"&run_"+run.getKey()+"="+run.getValue().toString();
			}
			String url=serviceUrlConfig.getProcessEngineUrl()+"/Task/dotaskbypro?processInstanceId="+ proId+runMapUrl;
			logger.info(url);
			String jsonString;

			jsonString = GraspUtil.getText(url);
			rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(-1);
		} 
		return rtnInfo;
	}

	@Override
	public ReturnInfo showtaskvariablesbypro(String proId) {
		// 返回结果对象
		ReturnInfo rtnInfo = new ReturnInfo();
		try {
			String url=serviceUrlConfig.getProcessEngineUrl()+"/Task/showtaskvariablesbypro?processInstanceId="+ proId;
			logger.info(url);
			String jsonString;

			jsonString = GraspUtil.getText(url);
			rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(-1);
		} 
		return rtnInfo;
	}

	@Override
	public ReturnInfo showtaskvariables(String taskId) {
		// 返回结果对象
		ReturnInfo rtnInfo = new ReturnInfo();
		try {
			String url=serviceUrlConfig.getProcessEngineUrl()+"/Task/showtaskvariables?taskId="+ taskId;
			logger.info(url);
			String jsonString;

			jsonString = GraspUtil.getText(url);
			rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(-1);
		} 
		return rtnInfo;
	}
	
	
	@Override
	public ReturnInfo setVariableByProId(String proId, Map<String, Object> runMap) {
		// 返回结果对象
		ReturnInfo rtnInfo = new ReturnInfo();
		try {
			String runMapUrl = "";
			for(Map.Entry<String, Object> run :runMap.entrySet()) {
				runMapUrl = runMapUrl +"&run_"+run.getKey()+"="+run.getValue().toString();
			}
			String url=serviceUrlConfig.getProcessEngineUrl()+"/Task/setVariableByProId?processInstanceId="+ proId+runMapUrl;
			logger.info(url);
			String jsonString;

			jsonString = GraspUtil.getText(url);
			rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(-1);
		} 
		return rtnInfo;
	}

	@Override
	public ReturnInfo checkTask(String taskId, User user) {
		ReturnInfo rtnInfo = new ReturnInfo();
		try {
			checkuser(user,taskId);
			rtnInfo.setSuccess(true);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(-1);
		} 
		return rtnInfo;
	}
	@Override
	public ReturnInfo checkTaskbByPro(String proId, User user) {
		ReturnInfo rtnInfo = new ReturnInfo();
		try {
			checkuserByPro(user,proId);
			rtnInfo.setSuccess(true);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(-1);
		} 
		return rtnInfo;
	}
	
	
	private ReturnInfo checkTask_private(String taskId) {
		ReturnInfo rtnInfo = new ReturnInfo();
		try {
			String url=serviceUrlConfig.getProcessEngineUrl()+"/Task/showtaskvariables?taskId="+ taskId;
			logger.info(url);
			String jsonString;

			jsonString = GraspUtil.getText(url);
			rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(-1);
		} 
		return rtnInfo;
	}
	
	private ReturnInfo checkTaskbByPro_private(String proId) {
		// 返回结果对象
		ReturnInfo rtnInfo = new ReturnInfo();
		try {
			String url=serviceUrlConfig.getProcessEngineUrl()+"/Task/showtaskvariablesbypro?processInstanceId="+ proId;
			logger.info(url);
			String jsonString;

			jsonString = GraspUtil.getText(url);
			rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);

		} catch (Exception e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage(e.getMessage());
				rtnInfo.setMessageType(-1);
		}
		return rtnInfo;
	}

	/*
	private String getBqtProcessUrl() throws Exception {
		Properties prop = new Properties(); 
		try {
//			InputStream in = new BufferedInputStream (new FileInputStream("config.properties"));
//
//			prop.load(in);     ///加载属性列表
//			Iterator<String> it=prop.stringPropertyNames().iterator();
//			while(it.hasNext()){
//				String key=it.next();
//				logger.info(key+":"+prop.getProperty(key));
//			}
//			in.close();

			return "http://localhost:8080/bdybusinessengine/interface";//prop.getProperty("ProcessUrl");
		} catch (Exception e) {

			e.printStackTrace();
			throw e;
		}
	}
	*/
	
	private void checkuser(User user,String taskId) throws Exception {

		ReturnInfo ret = new ReturnInfo();
		ret = checkTask_private(taskId);

		
		Map<String,Object> task = (Map<String, Object>) ret.getData();
	
		
//		if(!userService.hasPermission(getpermissionString(taskPermission), user)) throw new Exception("无权限");
	
		
		
		
	}
	private void checkuserByPro(User user,String proId) throws Exception {

		ReturnInfo ret = new ReturnInfo();
		ret = checkTaskbByPro_private(proId);

		
		Map<String,Object> task = (Map<String, Object>) ret.getData();
		
		
//		if(!userService.hasPermission(getpermissionString(taskPermission), user)) throw new Exception("无权限");
	
		
		
		
		
	}


	
	
	private String getDefName(String defId) {

		switch(defId) {
		case "agent_open":
			return "代理机构开通";
		case "coope_open":
			return "合作机构开通";
		case "cust_bind":
			return "客户绑定";
		case "cust_open":
			return "客户开通";
		case "":
			return "";
		}
		
		return null;
	}

	
	@Override
	public ReturnInfo custstartProByKey(String prokey, Map<String, Object> runMap) {
		// TODO 自动生成的方法存根
		return null;
	}



	@Override
	public ReturnInfo custstartPro(String proid, Map<String, Object> runMap) {
		// TODO 自动生成的方法存根
		return null;
	}



	@Override
	public ReturnInfo custdoTask(String taskId, Map<String, Object> runMap) {
		// TODO 自动生成的方法存根
		return null;
	}



	@Override
	public ReturnInfo custdoTaskByPro(String proId, Map<String, Object> runMap) {
		// TODO 自动生成的方法存根
		return null;
	}
	
	@Override
	public List<Map<String, Object>> findUserTaskUrl(String userId)
			throws Exception {
		String url = serviceUrlConfig.getProcessEngineUrl()
				+ "/Task/findusertask?userId=" + userId;
		logger.info(url);
		String jsonString;

		jsonString = GraspUtil.getText(url);
		ReturnInfo rtnInfo = JsonUtil.toObject(jsonString, ReturnInfo.class);

		List<Map<String, Object>> userList = (List<Map<String, Object>>) rtnInfo
				.getData();
		return userList;
	}
	
	
	@Override
	public List<Map<String, Object>> queryTaskProperty(String caseIds, String caseTypeIds) throws Exception{
		String url = serviceUrlConfig.getProcessEngineUrl()
				+ "/Task/querytaskproperty?caseIds="+ caseIds +"&caseTypeIds="+ caseTypeIds;
		logger.info(url);
		String jsonString;

		jsonString = GraspUtil.getText(url);
		ReturnInfo rtnInfo = JsonUtil.toObject(jsonString, ReturnInfo.class);

		List<Map<String, Object>> list= (List<Map<String, Object>>) rtnInfo
				.getData();
		return list;
	}
	
	
	
	@Override
	public List<Map<String, Object>> queryTaskPropertyByCaseId(String caseIds, String caseTypeIds) throws Exception{
		String url = serviceUrlConfig.getProcessEngineUrl()
				+ "/Task/queryTaskPropertyByCaseId?caseIds="+ caseIds +"&caseTypeIds="+ caseTypeIds;
		logger.info(url);
		String jsonString;

		jsonString = GraspUtil.getText(url);
		ReturnInfo rtnInfo = JsonUtil.toObject(jsonString, ReturnInfo.class);

		List<Map<String, Object>> list= (List<Map<String, Object>>) rtnInfo
				.getData();
		return list;
	}
	
	
	@Override
	public List<Map<String, Object>> queryTaskPropertyByTaskId(String taskIds, String caseTypeIds) throws Exception{
		String url = serviceUrlConfig.getProcessEngineUrl()
				+ "/Task/queryTaskPropertyByTaskId?taskIds="+ taskIds+"&caseTypeIds="+ caseTypeIds;
		logger.info(url);
		String jsonString;

		jsonString = GraspUtil.getText(url);
		ReturnInfo rtnInfo = JsonUtil.toObject(jsonString, ReturnInfo.class);

		List<Map<String, Object>> list= (List<Map<String, Object>>) rtnInfo
				.getData();
		return list;
	}
	
	
	
	
	
	

}