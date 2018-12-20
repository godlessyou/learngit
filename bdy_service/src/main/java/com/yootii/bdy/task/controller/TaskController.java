package com.yootii.bdy.task.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yootii.bdy.bill.model.Bill;




import com.yootii.bdy.common.CommonController;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.Globals;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.customer.service.ApplicantService;
import com.yootii.bdy.ipservice.model.AgencyService;
import com.yootii.bdy.ipservice.model.PlatformService;
import com.yootii.bdy.ipservice.service.AgencyServiceService;
import com.yootii.bdy.process.service.ProcessService;
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.task.model.BillToDoList;
import com.yootii.bdy.task.model.TmCaseTaskToDoList;
import com.yootii.bdy.task.model.ToDoList;
import com.yootii.bdy.task.service.BillTaskService;
import com.yootii.bdy.task.service.TradeMarkCaseTaskService;
import com.yootii.bdy.task.service.Impl.TaskCommonImpl;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseTypeMapper;
import  com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.TradeMarkCaseFile;
import com.yootii.bdy.tmcase.model.TradeMarkCaseType;
import com.yootii.bdy.tmcase.service.TradeMarkCaseFileService;
import  com.yootii.bdy.tmcase.service.TradeMarkCaseService;
import com.yootii.bdy.user.service.UserService;
import com.yootii.bdy.util.StringUtils;
import com.yootii.bdy.util.TaskTool;

@Controller
@RequestMapping("interface/task")
public class TaskController extends CommonController {

	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private TradeMarkCaseService  tradeMarkCaseService;
	
		
	@Resource
	private TradeMarkCaseTaskService tradeMarkCaseTaskService;
	
	@Resource
	private TradeMarkCaseFileService tradeMarkCaseFileService;
	
	@Resource
	private BillTaskService billTaskService;
	
	@Resource
	private AgencyServiceService agencyServiceService;
	
	@Resource
	private TradeMarkCaseMapper tradeMarkCaseMapper;
	
	@Resource
	private TaskCommonImpl taskCommonImpl;	
	
	@Resource
	private UserService userService;
	
	@Resource
	private TradeMarkCaseTypeMapper tradeMarkCaseTypeMapper;
	
	@Resource
	private ApplicantService applicantService;
	
	@Resource
	private ProcessService processService;
	
	
	//批量执行接口
	
	@RequestMapping(value = "/dotasklist", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo doTaskList(HttpServletRequest request,TradeMarkCaseFile tradeMarkCaseFile, GeneralCondition gcon, String tasklists, String taskName,String caseGroupList, String caseTypeIdList) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && !rtnInfo.getSuccess()) { // 通过身份验证	
			return rtnInfo;
		}
		try {
			Token token = (Token)rtnInfo.getData();
			this.addToken(token);
			this.addURL(request.getRequestURI());
			
			Map<String,List<ReturnInfo>> ret = new HashMap<String,List<ReturnInfo>>();
			List<ReturnInfo> sucess = new ArrayList<ReturnInfo>();
			List<ReturnInfo> falses = new ArrayList<ReturnInfo>();
			
			if ((tasklists==null || tasklists.equals("")) && (caseGroupList==null || caseGroupList.equals(""))){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("tasklists与caseGroupList不能都为空");
				return rtnInfo;
			}
			
			if (taskName==null || taskName.equals("")){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("taskName参数不能为空");
				return rtnInfo;
			}
			
			
			String taskIds=tasklists;
			
			List<Map<String, Object>> taskDataList=null;			
						
			if (caseGroupList!=null && !caseGroupList.equals("")){	
				
				String caseIdList=null;	
				
				List<String> caseGroups = Arrays.asList(caseGroupList.split(","));
				
				//获取与caseGroup属性值相同的案件
				List<TradeMarkCase> tmCaseList=tradeMarkCaseMapper.selectByCaseGroup(caseGroups);
				
				if (tmCaseList!=null){
					for(TradeMarkCase tmCase: tmCaseList){
						Integer id=tmCase.getId();
						if (caseIdList==null){
							caseIdList=id.toString();
						}else{
							caseIdList=caseIdList+","+id.toString();
						}
						
					}
					if (caseIdList.length()>0){									
							
						if (caseTypeIdList==null){
							caseTypeIdList="1,2";
						}
						//通过一次请求获取所有caseId对应的流程的参数
						taskDataList=processService.queryTaskPropertyByCaseId(caseIdList, caseTypeIdList);
						
						if (tasklists!=null && tasklists.length()>0){						
							boolean removeFlag=false;
							List<String> taskList = Arrays.asList(tasklists.split(","));
							
							for(Map<String, Object> data: taskDataList){
								if (data==null || data.size()==0){	
									continue;
								}							
								String taskId=(String)data.get("taskId");
								Iterator<String> it = taskList.iterator();
								while (it.hasNext()) {
									String tId=it.next();	
									if (taskId!=null && tId!=null && taskId.equals(tId)){
										it.remove(); //删除重复的taskId
										removeFlag=true;
									}
								}							
							}
							
							if (removeFlag){			
								String[] b = taskList.toArray(new String[taskList.size()]);				
								taskIds= String.join(",", b);
							}
						}
					}				
				
				}			
				
			}
			
			
			List<Map<String, Object>> allTaskData= null;
			if (taskIds!=null && !taskIds.equals("")){
					
				if (caseTypeIdList==null){
					caseTypeIdList="1,2";
				}
				//通过一次请求获取所有taskId对应的流程的参数	
				allTaskData=processService.queryTaskPropertyByTaskId(taskIds, caseTypeIdList);	
				logger.info("allTaskData="+allTaskData);
			}	
			
			if (taskDataList!=null  && taskDataList.size()>0){
				if (allTaskData==null){
					allTaskData=new ArrayList<Map<String, Object>>();
				}
				//合并两个参数集合				
				allTaskData.addAll(taskDataList);	
			}
			
			if (allTaskData==null || allTaskData.size()==0){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("没有需要批量处理的案件");
				return rtnInfo;
			}
			
			
			String caseIds = "";
			
			String userId = request.getParameter("userId");
			String remarks = request.getParameter("remarks");
			String submitMode = request.getParameter("submitMode");		
			String transfer = request.getParameter("transfer");
			String agencyId = request.getParameter("agencyId");				
			
			logger.info("taskName:"+taskName );
			
			String msg=null;
			
			int total=allTaskData.size();
			int successNumber=0;
			
			for(Map<String, Object> proMap: allTaskData){
				
				if (proMap==null || proMap.size()==0){	
					continue;
				}
				String caseId=(String)proMap.get("caseId");
				String taskId=(String)proMap.get("taskId");
				
				logger.info("caseId:"+caseId +", taskId:"+taskId );
			
				ReturnInfo rtnInfo1 = null;
				TmCaseTaskToDoList toDoList = new TmCaseTaskToDoList();
				toDoList.setTaskId(Integer.valueOf(taskId));
				
				switch(taskName) {
				case "auditDoc":
						String auditResult = request.getParameter("auditResult");
						rtnInfo1 = tradeMarkCaseTaskService.auditOfficalDoc(gcon, userId, auditResult, toDoList, proMap);	
					break;
				case "appoffline":	
						String submitStatus = request.getParameter("submitStatus");
						rtnInfo1 = tradeMarkCaseTaskService.appOffLine(gcon, userId, submitStatus, toDoList, proMap);
						break;
				case "refuse":	
//						String submitStatus = request.getParameter("submitStatus");						
						toDoList.setRemarks(remarks);
						String customerId=null;
						rtnInfo1 = tradeMarkCaseTaskService.refuse(gcon, userId, customerId,  toDoList, proMap);				
					break;
				case "assginCase":
						toDoList.setRemarks(remarks);						
						rtnInfo1 = tradeMarkCaseTaskService.assginCase(gcon, userId, agencyId, transfer, toDoList, proMap);
					break;
				case "audited":	
						toDoList.setRemarks(remarks);
						rtnInfo1 = tradeMarkCaseTaskService.audited(gcon, userId, submitMode, toDoList,false, proMap);					
					break;				
				}
				
				if(rtnInfo1 != null && rtnInfo1.getSuccess()){
					successNumber++;
//					sucess.add(rtnInfo1);
					caseIds = caseIds+caseId+",";
				} else {
					
					String failMsg=rtnInfo1.getMessage();
					logger.info("failMsg: "+failMsg);
					
					if (msg==null){
						msg="案件"+caseId+"信息不完整：" + failMsg;
					}else{
						msg=msg+";"+"案件"+caseId+"信息不完整：" + failMsg;
					}	
//					falses.add(rtnInfo1);
				}
				
			}
			
		
			if(successNumber>0) {
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("gcon", gcon);
				switch(taskName) {
				case "audited":					
						map.put("userId", userId);
						map.put("custId", null);
						map.put("sendToCust", true);						
						map.put("mailType", "sbzcsq_zjds_en");
						map.put("caseIds", org.apache.commons.lang.StringUtils.substring(caseIds, 0, -1) );
						map.put("taskName", null);					
					break;
				}


				
				if (map.get("caseIds")!=null){				
					taskCommonImpl.sendMail(map);
				}
								
//				Map<String,Integer> data=new HashMap<String,Integer>();				
				
				rtnInfo.setSuccess(true);
//				rtnInfo.setData(data);
				int failNumber=total-successNumber;
				if(failNumber==0){
					rtnInfo.setMessage("成功办理案件数：" + successNumber);
				}else{					
					rtnInfo.setMessage("办理案件成功数量：" + successNumber+", 失败数量："+failNumber+"，原因："+msg);				
				}
			} else {
				
				rtnInfo.setSuccess(false);
//				rtnInfo.setData(ret);
				rtnInfo.setMessage("办理案件失败，原因："+msg);	
			}	
		}catch (Exception e){
			rtnInfo.setSuccess(false);
			rtnInfo.setData(null);
			rtnInfo.setMessage(e.getMessage());
		}
			
		return rtnInfo;
	}
	
	
	//---- 客户与代理机构公用接口实现类 -------	
	
	// 查询待办事项
	@RequestMapping(value = "/querytodolist", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryToDoList(GeneralCondition gcon, String userId, String customerId, Integer pageId,TradeMarkCase tmcase) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证			
			
			makeOffsetAndRows(gcon);
			
			rtnInfo = tradeMarkCaseTaskService.queryToDoList(gcon, userId, customerId, pageId,tmcase);			
		}	
		return rtnInfo;
	}
	
	
	// 待办事项详情
	@RequestMapping(value = "/todolistdetail", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo toDoListDetail(GeneralCondition gcon,  String customerId, String userId, String taskId, TmCaseTaskToDoList toDoList) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证			
			rtnInfo = tradeMarkCaseTaskService.toDoListDetail(gcon, customerId, userId, taskId, toDoList);			
		}	
		return rtnInfo;
	}	
	
	
	//拒绝接口
	@RequestMapping(value = "/refuse", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo refuse(GeneralCondition gcon, String userId, String customerId, TmCaseTaskToDoList toDoList,HttpServletRequest request) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证			
			Token token = (Token)rtnInfo.getData();
			this.addToken(token); //审计日志需要用到token信息
			this.addURL(request.getRequestURI());
			rtnInfo = tradeMarkCaseTaskService.refuse(gcon, userId, customerId,  toDoList, null);
		}	
		return rtnInfo;
	}
	
	
	//---- 为商标流程引擎中配置的servicetask所调用的接口 -------
	// 转发案件接口
	@RequestMapping(value = "/transferCase", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo transferCase(GeneralCondition gcon, String userId, String processInstanceId, String agencyLevel, String agencyServiceId, TradeMarkCase tmCase,HttpServletRequest request) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		Token token = (Token)rtnInfo.getData();
		this.addToken(token);//审计日志需要用到token信息
		this.addURL(request.getRequestURI());
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证			
			
			rtnInfo = tradeMarkCaseTaskService.transferCase(gcon, userId, processInstanceId, agencyLevel, agencyServiceId, tmCase);
		}	
		return rtnInfo;
	}
	
	
	// 反馈官方通知的消息的接口
	@RequestMapping(value = "/notifyAppResult", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo notifyAppResult(GeneralCondition gcon,  String submitStatus, String processInstanceId, HttpServletRequest request) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		Token token = (Token)rtnInfo.getData();
		this.addToken(token);
		this.addURL(request.getRequestURI());
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证			
			rtnInfo = tradeMarkCaseTaskService.notifyAppResult(gcon, submitStatus, processInstanceId);
		}	
		return rtnInfo;
	}
	
	
	
	// 反馈拒绝处理的消息的接口
	@RequestMapping(value = "/notifyRefuseCase", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo notifyRefuseCase(GeneralCondition gcon, String userId, String processInstanceId, String agencyLevel,HttpServletRequest request) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		Token token = (Token)rtnInfo.getData();//审计日志需要用到token信息
		this.addToken(token);
		this.addURL(request.getRequestURI());
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证			
			rtnInfo = tradeMarkCaseTaskService.notifyRefuseCase(gcon, userId, processInstanceId, agencyLevel);
		}	
		return rtnInfo;
	}
	

	// 反馈案件结果的消息的接口
	@RequestMapping(value = "/notifyCaseResult", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo notifyCaseResult(GeneralCondition gcon, String userId, String processInstanceId, String agencyLevel,HttpServletRequest request) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		Token token = (Token)rtnInfo.getData();//审计日志需要用到token信息
		this.addToken(token);
		this.addURL(request.getRequestURI());
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证			
			rtnInfo = tradeMarkCaseTaskService.notifyCaseResult(gcon, userId, processInstanceId, agencyLevel);
		}	
		return rtnInfo;
	}
	
	
	// 反馈子案件结果的消息的接口
	@RequestMapping(value = "/notifyChildCaseResult", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo notifyChildCaseResult(GeneralCondition gcon, String caseId, String processInstanceId,  HttpServletRequest request) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		Token token = (Token)rtnInfo.getData();//审计日志需要用到token信息
		this.addToken(token);
		this.addURL(request.getRequestURI());
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证			
			rtnInfo = tradeMarkCaseTaskService.notifyChildCaseResult(gcon, caseId, processInstanceId);
		}	
		return rtnInfo;
	}
	
	
	
	
	
	// 反馈官方通知的消息的接口
	@RequestMapping(value = "/notifyOfficialNotice", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo notifyOfficialNotice(GeneralCondition gcon, String userId, String custId, String processInstanceId, String lastAgencyLevel, String fileName, String lastProcessInstanceId,HttpServletRequest request) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		Token token = (Token)rtnInfo.getData();
		this.addToken(token);
		this.addURL(request.getRequestURI());
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证			
			rtnInfo = tradeMarkCaseTaskService.notifyOfficialNotice(gcon, userId, custId, processInstanceId, lastAgencyLevel, fileName, lastProcessInstanceId);
		}	
		return rtnInfo;
	}
	
	
	// 反馈客户决定的消息的接口
	@RequestMapping(value = "/notifyCustomerDecision", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo notifyCustomerDecision(GeneralCondition gcon, String userId, String processInstanceId, String agencyLevel, String lastAgencyLevel, String customerDecision, String fileName,HttpServletRequest request) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		Token token = (Token)rtnInfo.getData();
		this.addToken(token);
		this.addURL(request.getRequestURI());
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证			
			rtnInfo = tradeMarkCaseTaskService.notifyCustomerDecision(gcon, userId, processInstanceId, agencyLevel, lastAgencyLevel, customerDecision, fileName);
		}	
		return rtnInfo;
	}
	
	
	// 创建驳回复审案件的接口
	@RequestMapping(value = "/createRejectCase", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo createRejectCase(GeneralCondition gcon, String userId, String processInstanceId, TradeMarkCase tmCase,HttpServletRequest request) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		Token token = (Token)rtnInfo.getData();
		this.addToken(token);
		this.addURL(request.getRequestURI());
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证			
			rtnInfo = tradeMarkCaseTaskService.createRejectCase(gcon, userId, processInstanceId, tmCase);
		}	
		return rtnInfo;
	}
	
	
	
	
	
	// 创建异议答辩案件的接口
	@RequestMapping(value = "/createObjectionDefenseCase", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo createObjectionDefenseCase(GeneralCondition gcon, String userId, String processInstanceId, TradeMarkCase tmCase,HttpServletRequest request){
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		Token token = (Token)rtnInfo.getData();
		this.addToken(token);
		this.addURL(request.getRequestURI());
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证			
			rtnInfo = tradeMarkCaseTaskService.createObjectionDefenseCase(gcon, userId, processInstanceId, tmCase);
		}	
		return rtnInfo;	
	}
	
	//创建异议申请案件的接口
	@RequestMapping(value="createObjectionApplyCase",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo createObjectionApplyCase(GeneralCondition gCondition,String userId,String processInstanceId,TradeMarkCase tradeMarkCase,HttpServletRequest request){
		
		ReturnInfo returnInfo = (ReturnInfo)authenticationService.authorize(gCondition);
		Token token = (Token)returnInfo.getData();
		this.addToken(token);
		this.addURL(request.getRequestURI());
		if(returnInfo != null && returnInfo.getSuccess()){
			
		}
		return returnInfo;
	}
	
	
//	
//	// 创建不予注册复审案件的接口
//	@RequestMapping(value = "/createNotAllowedCase", produces = "application/json;charset=UTF-8")
//	@ResponseBody
//	public ReturnInfo createNotAllowedCase(GeneralCondition gcon, String userId, String processInstanceId, TradeMarkCase tmCase,HttpServletRequest request){
//		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
//		Token token = (Token)rtnInfo.getData();
//		this.addToken(token);
//		this.addURL(request.getRequestURI());
//		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证			
//			rtnInfo = tradeMarkCaseTaskService.createNotAllowedCase(gcon, userId, processInstanceId, tmCase);
//		}	
//		return rtnInfo;	
//	}	
//	
	
	

	
	
	// 创建不予注册复审/撤销复审答辩等新案件的接口
	@RequestMapping(value = "/createNewCase", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo createNewCase(GeneralCondition gcon, String userId, String processInstanceId, TradeMarkCase tmCase,HttpServletRequest request){
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		Token token = (Token)rtnInfo.getData();
		this.addToken(token);
		this.addURL(request.getRequestURI());
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证			
			rtnInfo = tradeMarkCaseTaskService.createNewCase(gcon, userId, processInstanceId, tmCase);
		}	
		return rtnInfo;	
	}	
	
	

	//修改案件状态的接口
	@RequestMapping(value = "/modifyCaseStatus", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo modifyCaseStatus(GeneralCondition gcon, String userId, String caseId, String fileName, String refuse, String caseResult, String processInstanceId, HttpServletRequest request){
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		Token token = (Token)rtnInfo.getData();
		this.addToken(token);
		this.addURL(request.getRequestURI());
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证			
			rtnInfo = tradeMarkCaseTaskService.modifyCaseStatus(gcon, userId, caseId, fileName, refuse, caseResult, processInstanceId);
		}	
		return rtnInfo;	
	}
	
		
	
	

	//---- 代理机构使用的接口实现类 -------	

	//处理案件，几种可能的处理方式：转发案件，接收案件，拒绝案件
	@RequestMapping(value = "/assginCase", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo assginCase(GeneralCondition gcon, String userId, String agencyId, String transfer, TmCaseTaskToDoList toDoList,HttpServletRequest request) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		Token token = (Token)rtnInfo.getData();
		this.addToken(token);
		this.addURL(request.getRequestURI());
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证			
			rtnInfo = tradeMarkCaseTaskService.assginCase(gcon, userId, agencyId, transfer, toDoList, null);
		}	
		return rtnInfo;
	}
		

	// 审核不通过接口
	@RequestMapping(value = "/notAudited", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo notAudited(GeneralCondition gcon, String userId, TmCaseTaskToDoList toDoList,HttpServletRequest request) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		Token token = (Token)rtnInfo.getData();
		this.addToken(token);
		this.addURL(request.getRequestURI());
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证			
			rtnInfo = tradeMarkCaseTaskService.notAudited(gcon, userId, toDoList);	
		}	
		return rtnInfo;
	}	
	
	
	// 递交
	@RequestMapping(value = "/audited", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo audited(GeneralCondition gcon,String userId, TmCaseTaskToDoList toDoList, String submitMode, HttpServletRequest request,Boolean send) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		Token token = (Token)rtnInfo.getData();
		this.addToken(token);
		this.addURL(request.getRequestURI());
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证			
			rtnInfo = tradeMarkCaseTaskService.audited(gcon, userId, submitMode, toDoList,send, null);	
		}	
		return rtnInfo;
	}
	
	
	// 网上申请接口
	@RequestMapping(value = "/submitapp", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo submitapp(GeneralCondition gcon, String userId, String custId, String caseId, String processInstanceId,HttpServletRequest request) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		Token token = (Token)rtnInfo.getData();
		this.addToken(token);
		this.addURL(request.getRequestURI());
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证			
			rtnInfo = tradeMarkCaseTaskService.submitApp(gcon, userId, custId, caseId, processInstanceId);	
		}	
		return rtnInfo;
	}
	
	
	// 直接递交申请接口
	@RequestMapping(value = "/appoffline", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo appOffLine(GeneralCondition gcon,String userId, String submitStatus, TmCaseTaskToDoList toDoList,HttpServletRequest request) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		Token token = (Token)rtnInfo.getData();
		this.addToken(token);
		this.addURL(request.getRequestURI());
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证			
			rtnInfo = tradeMarkCaseTaskService.appOffLine(gcon, userId, submitStatus, toDoList, null);	
		}	
		return rtnInfo;
	}
	
	
	// 官文录入
	@RequestMapping(value = "/officaldoc", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo officalDoc(HttpServletRequest request,String caseId,TradeMarkCaseFile tradeMarkCaseFile, GeneralCondition gcon,String userId, TradeMarkCase tradeMarkCase, TmCaseTaskToDoList toDoList) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		Token token = (Token)rtnInfo.getData();
		this.addToken(token);
		this.addURL(request.getRequestURI());
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证	
			String name="caseId";
			String value=caseId;			
			rtnInfo =TaskTool.checkId(name, value);			
			if (!rtnInfo.getSuccess()) {
				return rtnInfo;
			}
			
			Integer id=new Integer(caseId);
			tradeMarkCase.setId(id);
			
			//上传案件文件
			String fileType="官方通知";
			tradeMarkCaseFile.setFileType(fileType);			

			Integer fileNameInt= tradeMarkCaseFile.getFileName();
			
			if (fileNameInt==null){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("fileName不能为空");
				rtnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
				return rtnInfo;
			}
			
			String fileName=fileNameInt.toString();
			
			
			rtnInfo = tradeMarkCaseTaskService.officalDoc(gcon, userId, fileName, tradeMarkCase, toDoList);	
			
		}	
		return rtnInfo;
	}
		
	

	// 向客户反馈
	@RequestMapping(value = "/auditdoc", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo auditDoc(GeneralCondition gcon,String userId, String auditResult, TmCaseTaskToDoList toDoList,HttpServletRequest request) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证		
			Token token = (Token)rtnInfo.getData();
			this.addToken(token);
			this.addURL(request.getRequestURI());
			rtnInfo = tradeMarkCaseTaskService.auditOfficalDoc(gcon, userId, auditResult, toDoList, null);	
		}	
		return rtnInfo;
	}
	
	
	// 处理官文
	@RequestMapping(value = "/processdoc", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo processdoc(GeneralCondition gcon, String userId, String approved, TmCaseTaskToDoList toDoList,HttpServletRequest request) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证		
			Token token = (Token)rtnInfo.getData();
			this.addToken(token);
			this.addURL(request.getRequestURI());
			rtnInfo = tradeMarkCaseTaskService.processDoc(gcon, userId, approved, toDoList);	
		}	
		return rtnInfo;
	}
	
	// 向客户反馈
	@RequestMapping(value = "/feedback", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo feedback(GeneralCondition gcon,String userId, TmCaseTaskToDoList toDoList,HttpServletRequest request) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证	
			Token token = (Token)rtnInfo.getData();
			this.addToken(token);
			this.addURL(request.getRequestURI());
			rtnInfo = tradeMarkCaseTaskService.feedback(gcon, userId, toDoList);	
		}	
		return rtnInfo;
	}
	
	
	// 处理客户决定
	@RequestMapping(value = "/processdecision", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo processDecision(GeneralCondition gcon,String userId, TmCaseTaskToDoList toDoList,HttpServletRequest request) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证		
			Token token = (Token)rtnInfo.getData();
			this.addToken(token);
			this.addURL(request.getRequestURI());
			rtnInfo = tradeMarkCaseTaskService.processDecision(gcon, userId, toDoList);	
		}	
		return rtnInfo;
	}
	
	
	//异议裁定赢后，等待对方是否要决定复审
	@RequestMapping(value="/oppositeDecision",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo oppositeDecision(GeneralCondition gcon,String userId,TmCaseTaskToDoList toDoList,HttpServletRequest request){
		
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证		
			Token token = (Token)rtnInfo.getData();
			this.addToken(token);
			this.addURL(request.getRequestURI());
			rtnInfo = tradeMarkCaseTaskService.processDecision(gcon, userId, toDoList);	
		}	
		return rtnInfo;
	}
	
	
	
	
	
	//---- 客户使用的接口实现类 -------	
	
	// 增加新的案件
	@RequestMapping(value = "/createcase", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo createTradeMarkCase(GeneralCondition gcon, String userId, String agencyServiceId, TradeMarkCase tradeMarkCase,
			HttpServletRequest request,String tmNumber,String tmNumberList) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		
		if (rtnInfo != null && rtnInfo.getSuccess()) {// 通过身份验证			
			Token token = (Token)rtnInfo.getData();
			this.addToken(token);
			this.addURL(request.getRequestURI());
			makeOffsetAndRows(gcon);
			
			rtnInfo = tradeMarkCaseTaskService.createTradeMarkCase(gcon, userId, agencyServiceId, tradeMarkCase, tmNumber, tmNumberList);
		}
		
		return rtnInfo;
	}
	
		
	
		

	
	//修改案件
	@RequestMapping(value = "/modifycase", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo modifyeTradeMarkCase(GeneralCondition gcon, String userId, 
			TradeMarkCase tradeMarkCase, String caseId, String taskId, String remarks, HttpServletRequest request) {
		ReturnInfo rtnInfo = this.checkUser(gcon);	
		if (rtnInfo == null || !rtnInfo.getSuccess()) { 
			return rtnInfo;
		}	
		
		Token token = (Token)rtnInfo.getData();
		this.addToken(token);
		this.addURL(request.getRequestURI());
			
		String customerId=null;
		Integer custId=tradeMarkCase.getCustId();
		if(custId!=null){
			customerId=custId.toString();
		}
		
		if (caseId==null || caseId.equals("")){	
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("案件编号不能为空");
			return rtnInfo;
		}
		
		//批量案件的标志，1：代表批量案件
	    Integer piliangCase=0;	    
	    if (caseId.indexOf(",")>-1){
	    	piliangCase=1;
	    }
		
		
		String msg=null;
		
		int errCount=0;
		int count=0;
		
		List<Map<String, Object>> taskDataList=null;			
		
		String caseTypeIdList="1,2";
		try {
			//通过一次请求获取所有caseId对应的流程的参数
			taskDataList=processService.queryTaskPropertyByCaseId(caseId, caseTypeIdList);

			for(Map<String, Object> proMap: taskDataList){			
				if (proMap==null || proMap.size()==0){	
					continue;
				}
				String tmCaseId=(String)proMap.get("caseId");
				String tmTaskId=(String)proMap.get("taskId");
				if (tmCaseId==null || tmCaseId.equals("")){
					logger.info("从流程中获取的caseId为空");
					continue;
				}				
				if (tmTaskId==null || tmTaskId.equals("")){
					logger.info("从流程中获取的taskId为空");
					continue;
				}				
			    Integer cId=Integer.parseInt(tmCaseId);
			    Integer tId=Integer.parseInt(tmTaskId);			
								
				TradeMarkCase oldData=null;
				if(cId!=null){	
					//获取修改前的案件信息
					oldData= tradeMarkCaseMapper.selectByPrimaryKey(cId);
				}
				
				//根据输入的案件类型的Id，获取案件的类型的文字描述				
				tradeMarkCase.setId(cId);				
				Integer caseTypeId = null;
				String caseType=tradeMarkCase.getCaseType();				
				if (caseType!=null && !caseType.equals("")){
					if(StringUtils.isNum(caseType)){
						caseTypeId=new Integer(caseType);
						TradeMarkCaseType tradeMarkCaseType=tradeMarkCaseTypeMapper.selectByPrimaryKey(caseTypeId);
						caseType=tradeMarkCaseType.getCaseType();
						tradeMarkCase.setCaseType(caseType);
					}
				}
				if(remarks !=null && remarks!=""){
					tradeMarkCase.setRemarks(remarks);
				}
				
				//调用修改案件接口
				rtnInfo = tradeMarkCaseService.modifyTradeMarkCase(tradeMarkCase, gcon);				
				
				if (rtnInfo == null || !rtnInfo.getSuccess()){					
					if (msg==null){
						msg="案件"+cId.toString()+"修改失败：" + rtnInfo.getMessage();
					}else{
						msg=msg+";"+"案件"+cId.toString()+"修改失败：" + rtnInfo.getMessage();
					}	
					errCount++;
					
				}else{
					TmCaseTaskToDoList toDoList=new TmCaseTaskToDoList();
					toDoList.setCaseId(cId);
					toDoList.setTaskId(tId);
					toDoList.setRemarks(remarks);
					
					Map<String, Object> map=new HashMap<String, Object>();
		            map.put("gcon", gcon);
		            map.put("userId", userId);
		            map.put("customerId", customerId);
		            map.put("toDoList", toDoList);
		            map.put("oldData", oldData);
		            map.put("newData", tradeMarkCase);
		            map.put("piliangCase", piliangCase);
									
					//调用提交案件后的流程处理接口
					rtnInfo = tradeMarkCaseTaskService.modifyCase(map, proMap);	
					if(rtnInfo == null || !rtnInfo.getSuccess()){
						if (msg==null){
							msg="更新案件"+cId.toString()+"的其它属性失败：" + rtnInfo.getMessage();
						}else{
							msg=msg+";"+"更新案件"+cId.toString()+"的的其它属性失败：" + rtnInfo.getMessage();
						}
					}
				}
			}
			count++;
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessageType(-2);
			rtnInfo.setMessage(e.getMessage());
			return rtnInfo;
		}
	
		
		if (msg==null){
			msg="案件修改成功";	
		}
		rtnInfo.setMessage(msg);	
		
		if (count>1){
			if(errCount==count){//如果全部失败，那么设置 success为false
				rtnInfo.setSuccess(false);		
			}else{
				rtnInfo.setSuccess(true);	
			}
		}			
				
		return rtnInfo;
	}
	
	
	
	
	
	
	//查询案件办理过程中的客户，以及代理人的反馈信息
	@RequestMapping(value = "/queryMailRecord", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryMailRecord(GeneralCondition gcon, TmCaseTaskToDoList toDoList) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			rtnInfo=tradeMarkCaseTaskService.queryMailRecord(toDoList);
		}	
		return rtnInfo;
	}
	
	
	
	
	//同意接口
	@RequestMapping(value = "/agree", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo agree(GeneralCondition gcon, String customerId, TmCaseTaskToDoList toDoList,HttpServletRequest request) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证		
			Token token = (Token)rtnInfo.getData();
			this.addToken(token);//审计日志需要用到token信息
			this.addURL(request.getRequestURI());
			rtnInfo = tradeMarkCaseTaskService.agree(gcon, customerId, toDoList);
		}	
		return rtnInfo;
	}
	
	
		
	
	//关闭案件
	@RequestMapping(value = "/closecase", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo closeCase(GeneralCondition gcon, String customerId, TmCaseTaskToDoList toDoList,HttpServletRequest request) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证		
			Token token = (Token)rtnInfo.getData();
			this.addToken(token);//审计日志需要用到token信息
			this.addURL(request.getRequestURI());
			rtnInfo = tradeMarkCaseTaskService.closeCase(gcon,customerId, toDoList);
		}	
		return rtnInfo;
	}	
	
	//
	@RequestMapping(value = "/addcaseagentuser", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo addCaseAgentUser(GeneralCondition gcon, HttpServletRequest request,String caseId, String userIds) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证		
			Token token = (Token)rtnInfo.getData();
			this.addToken(token);//审计日志需要用到token信息
			this.addURL(request.getRequestURI());
			rtnInfo = tradeMarkCaseTaskService.addCaseAgentUser(caseId, userIds);
		}	
		return rtnInfo;
	}	
	
	//
	@RequestMapping(value = "/addcustomeragentuser", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo addCustomerAgentUser(GeneralCondition gcon, HttpServletRequest request,String customerId, String userIds) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证		
			Token token = (Token)rtnInfo.getData();
			this.addToken(token);//审计日志需要用到token信息
			this.addURL(request.getRequestURI());
			rtnInfo = tradeMarkCaseTaskService.addCustomerAgentUser(customerId, userIds);
		}	
		return rtnInfo;
	}	
	
	//
	@RequestMapping(value = "/changecaseagentuser", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo changeCaseAgentUser(GeneralCondition gcon, HttpServletRequest request,String caseId, String beUserId, String afUserId) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证		
			Token token = (Token)rtnInfo.getData();
			this.addToken(token);//审计日志需要用到token信息
			this.addURL(request.getRequestURI());
			rtnInfo = tradeMarkCaseTaskService.changeCaseAgentUser(caseId, beUserId, afUserId);
		}	
		return rtnInfo;
	}	
	
	//
	@RequestMapping(value = "/changecustomeragentuser", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo changeCustomerAgentUser(GeneralCondition gcon, HttpServletRequest request,String customerId, String beUserId, String afUserId) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证		
			Token token = (Token)rtnInfo.getData();
			this.addToken(token);//审计日志需要用到token信息
			this.addURL(request.getRequestURI());
			rtnInfo = tradeMarkCaseTaskService.changeCustomerAgentUser(customerId, beUserId, afUserId);
		}	
		return rtnInfo;
	}	
		
	
	//代理机构下待办事项的统计
	@RequestMapping(value = "/statsbyagency", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statsByAgency(GeneralCondition gcon, Integer agencyId) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				//
				rtnInfo = tradeMarkCaseTaskService.statsByAgency(agencyId,gcon);
				
			}
			catch (Exception e) {
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	/* 暂时不用这个接口
		@RequestMapping(value = "/statstodotop10", produces = "application/json;charset=UTF-8")
		@ResponseBody
		public ReturnInfo statsToDoTop10(GeneralCondition gcon) {
			ReturnInfo rtnInfo = this.checkUser(gcon);
			
			if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
				try {
					//
					rtnInfo = tradeMarkCaseTaskService.statsToDoTop10(gcon);
					
				}
				catch (Exception e) {
					logger.error(e.getMessage());
					rtnInfo.setSuccess(false);
					rtnInfo.setMessageType(-2);
					rtnInfo.setMessage(e.getMessage());
					return rtnInfo;
				}
			}	
			return rtnInfo;
		}*/
		
		
	//-------账单接口实现---------
	
	@RequestMapping(value = "/creatbill", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo creatbill(GeneralCondition gcon, String userId ,Bill bill,String chargeRecordIds,HttpServletRequest request) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		try{
			if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证	
				Token token = (Token)rtnInfo.getData();
				this.addToken(token);//审计日志需要用到token信息
				this.addURL(request.getRequestURI());
				//创建账单
				rtnInfo = billTaskService.startBillReviewProcess(gcon, token.getUserID()+"", bill.getCustId().toString(), bill, chargeRecordIds);
			}	
		}catch(Exception e){
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("创建账单失败");
		}
		return rtnInfo;
	}
	
	
	// 查询待办事项
	@RequestMapping(value = "/auditedbill", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo audited(GeneralCondition gcon, String userId, String customerId,BillToDoList toDoList,Boolean audited) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证			
			
			makeOffsetAndRows(gcon);
			
			rtnInfo = billTaskService.audited(gcon, userId, toDoList, audited)	;	
		}	
		return rtnInfo;
	}
	
	// 查询待办事项
	@RequestMapping(value = "/modifybill", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo modifyBill(GeneralCondition gcon, String userId, String customerId,BillToDoList toDoList,Bill bill,HttpServletRequest request) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证			
			
			makeOffsetAndRows(gcon);
			Token token = (Token)rtnInfo.getData();
			this.addToken(token);//审计日志需要用到token信息
			this.addURL(request.getRequestURI());
			rtnInfo = billTaskService.modifyBill(gcon, userId, toDoList, bill);				
		}	
		return rtnInfo;
	}
	
	// 查询待办事项
	@RequestMapping(value = "/querybilltotolist", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo querybilltotolist(GeneralCondition gcon, String userId, String customerId) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证			
			
			makeOffsetAndRows(gcon);
			
			rtnInfo = billTaskService.queryToDoList(gcon, userId, customerId);	
		}	
		return rtnInfo;
	}
	
	
	
	
	// 账单付费
	@RequestMapping(value = "/paybill", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo paybill(GeneralCondition gcon, String userId, Bill bill, HttpServletRequest request) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证			
			
			makeOffsetAndRows(gcon);
			Token token = (Token)rtnInfo.getData();
			this.addToken(token);//审计日志需要用到token信息
			this.addURL(request.getRequestURI());
			
			String currentTaskPermission="账单审核:组长审核";
			
			// 检查当前用户是否有权限执行该任务
			ReturnInfo checkret = processService.checkuserstart(
					currentTaskPermission, userId, gcon);
			if (!checkret.getSuccess()) {
				return checkret;
			}
			
			
			rtnInfo = billTaskService.modifyBillStatus(gcon, userId, bill);
		}	
		return rtnInfo;
	}
	
	
	
	// 错误处理接口
	@RequestMapping(value = "/runAgain", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo runAgain(GeneralCondition gcon,String userId, TmCaseTaskToDoList toDoList,HttpServletRequest request) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		Token token = (Token)rtnInfo.getData();
		this.addToken(token);
		this.addURL(request.getRequestURI());
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证			
			rtnInfo = tradeMarkCaseTaskService.runAgain(gcon, userId, toDoList);	
		}	
		return rtnInfo;
	}
	
	
	
	// 任务回退的接口
	@RequestMapping(value = "/resetTask", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo resetTask(GeneralCondition gcon,HttpServletRequest request,String appCnName) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		Token token = (Token)rtnInfo.getData();//审计日志需要用到token信息
		this.addToken(token);
		this.addURL(request.getRequestURI());
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证			
			
			if (appCnName==null || appCnName.equals("")){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("appCnName参数不能为空");
				return rtnInfo;
			}
			rtnInfo = tradeMarkCaseTaskService.resetTask(gcon,appCnName);
		}	
		return rtnInfo;
	}
	
	
		
	
}