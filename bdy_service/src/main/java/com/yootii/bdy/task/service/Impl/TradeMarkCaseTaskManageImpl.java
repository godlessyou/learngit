package com.yootii.bdy.task.service.Impl;


import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


import org.springframework.stereotype.Service;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.ipservice.model.AgencyService;
import com.yootii.bdy.ipservice.model.PlatformService;
import com.yootii.bdy.ipservice.service.AgencyServiceService;
import com.yootii.bdy.task.model.ToDoList;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.service.TradeMarkCaseService;
import com.yootii.bdy.user.service.UserService;


@Service
public class TradeMarkCaseTaskManageImpl  {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private TaskCommonImpl taskCommonImpl;
	
	@Resource
	private CustomerTaskServiceImpl customerTaskServiceImpl;
	
	@Resource
	private TaskQueryServiceImpl taskQueryServiceImpl;	
	
	@Resource
	private TradeMarkCaseService tradeMarkCaseService;
	
	
	@Resource
	private AgencyServiceService agencyServiceService;
	
	@Resource
	private UserService userService;	
	

	
	public ReturnInfo createTradeMarkCase(GeneralCondition gcon, String userId, String agencyServiceId, TradeMarkCase tradeMarkCase,
			String tmNumber,String tmNumberList){
		
		ReturnInfo rtnInfo=new ReturnInfo();
		
		String tokenID=gcon.getTokenID();
		try {
			//参数非空判断
			if(agencyServiceId==null || agencyServiceId.equals("")){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage("can not create TradeMark Case becauesof agencyServiceId is null");
				return rtnInfo;
			}
			//获取代理机构服务费用等详细信息
			AgencyService ipAgencyService=agencyServiceService.queryAgencyServiceDetail(gcon, agencyServiceId);

			Integer sId=ipAgencyService.getServiceId();

			PlatformService platformService = ipAgencyService.getPlatformService();

			if (sId==null){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage("agencyServiceId is error.");
				return rtnInfo;
			}

			int serviceId=sId.intValue();	

			String caseType = platformService.getCaseType();				
			Integer caseTypeId=platformService.getCaseTypeId();
			Boolean muilt = false;
			Integer custId=tradeMarkCase.getCustId();
			String customerId=custId.toString();
			tradeMarkCase.setCaseType(caseType);
			tradeMarkCase.setCaseTypeId(caseTypeId);
			tradeMarkCase.setAgencyId(ipAgencyService.getAgencyId());
			
			String status="申请中";
			tradeMarkCase.setStatus(status);
			
			String appCnName=tradeMarkCase.getAppCnName();
					
			//在此根据不同的服务id进行  创建不同类型的案子
			/*
			if(serviceId==25) {
				//商标变更
				rtnInfo = tradeMarkCaseService.createTradeMarkCaseByAppName(tradeMarkCase, gcon);
				muilt =true;
			} else 
			*/
			if(tmNumber != null) {
				rtnInfo = tradeMarkCaseService.createTradeMarkCaseByTmNumber(tradeMarkCase, tmNumber, gcon);
			} else if(tmNumberList != null){
				List<String> list = Arrays.asList(tmNumberList.split(","));
				rtnInfo = tradeMarkCaseService.createTradeMarkCaseByTmNumberList(tradeMarkCase,list, gcon);
				muilt = true;
			}else if(serviceId == 37 || serviceId == 43 || serviceId == 47){
				//创建异议申请案件/创建商标撤三申请/创建不予注册复审案件。。。。
				rtnInfo = tradeMarkCaseService.createAppCase(tradeMarkCase,tmNumber, gcon);
			}else if(serviceId == 41 || serviceId ==74 || serviceId == 76 || serviceId==86){
				//创建异议答辩案件/参与不予注册复审 /无效宣告答辩/撤三答辩/商标撤销复审答辩
				rtnInfo = tradeMarkCaseService.createDissentReplyEntrance(tradeMarkCase,tmNumber,gcon);
			} else {
				if (appCnName!=null && !appCnName.equals("")){
					//按照申请人名称从申请人表中获取申请人信息，设置到案件对象中
					taskCommonImpl.setApplicantProperty(tradeMarkCase, tokenID);
				}
				rtnInfo = tradeMarkCaseService.createTradeMarkCase(tradeMarkCase, gcon);
			}
			
			//优化后
		//	rtnInfo = tradeMarkCaseService.kindOfTmCase(tradeMarkCase, tmNumber, serviceId, tmNumberList, gcon);
			
			
			//以下是创建案子完的后续操作
			if (rtnInfo != null && rtnInfo.getSuccess()){

				logger.info("createTradeMarkCase finish");

				if ( rtnInfo.getData()==null){					
					rtnInfo.setSuccess(false);
					rtnInfo.setMessageType(-2);
					rtnInfo.setMessage("createTradeMarkCase的返回值缺少数据");
					return rtnInfo;
				}
				
				Integer agencyId;
				List<Integer> caseIdList = new ArrayList<Integer>();
				if(muilt){
				/*if(rtnInfo.getMuilt()){  */
					Map<String,List> resData = (Map<String, List>) rtnInfo.getData();
					List<Map<String, Integer>> successData = resData.get("success");
					agencyId = successData.get(0).get("agencyId");
					for(Map<String, Integer> map:successData) {
						caseIdList.add(map.get("caseId"));
					}
					//TODO 批量发送邮件

				}else {
					Map<String, Object> resData = (Map<String, Object>) rtnInfo.getData();
					Integer caseId=(Integer)resData.get("caseId");
					agencyId=(Integer)resData.get("agencyId");
					caseIdList.add(caseId);

					//发送商标案件立案通知邮件（英文）
					String mailType="sbaj_create_en";
					boolean sendToCust=true;	
				
					List<String> userList=null;
					String emailUserId=userId;
					if (emailUserId==null || emailUserId.equals("")){
						String permission="案件分配";
						userList=userService.findUsersByPermission(permission, agencyId.toString(), tokenID);
						if (userList!=null && userList.size()>0){
							emailUserId=userList.get(0);
						}
					}
					Map<String, Object> emailMap=new HashMap<String, Object>();
					emailMap.put("caseId", caseId.toString());						
					emailMap.put("gcon", gcon);
					emailMap.put("userId", emailUserId);
					emailMap.put("custId", customerId);
					emailMap.put("sendToCust", sendToCust);
					emailMap.put("mailType", mailType);
					emailMap.put("caseType", caseType);

					taskCommonImpl.sendMail(emailMap);
				}
				String msg=null;
				ReturnInfo startProcessInfo=null;
				Integer taskId=null;
				String taskName=null;
				String remarks="startcase";
				Integer tmCaseId=null;
				for(Integer caseId:caseIdList) {
					if(tmCaseId==null){
						tmCaseId=caseId;
					}
					
					TradeMarkCase tmCase=new TradeMarkCase();
					tmCase.setId(caseId);
					tmCase.setAgencyId(agencyId);
					tmCase.setCaseTypeId(caseTypeId);

					if (serviceId==8 || serviceId==9){ //8:商标自助注册,9:商标高级注册
						Map<String, Object> map=new HashMap<String, Object>();
						map.put("gcon", gcon);
						map.put("userId", userId);
						map.put("customerId", customerId);
						map.put("agencyServiceId", agencyServiceId);
						map.put("serviceId", sId);
						map.put("tmCase", tmCase);

						logger.info("start, caseId:"+ caseId + " agencyId:"+ agencyId 
								+ " userId:"+ userId + " custId:"+ custId + 
								" agencyServiceId:"+ agencyServiceId + " serviceId:"+ serviceId);

						startProcessInfo=customerTaskServiceImpl.startTmRegisterProcess(map);

					}
					//23:商标续展自助服务,24:商标续展高级服务
					//32:商标转让自助服务,31:商标转让高级服务
					//28:商标变更代理人/文件接收人自助服务,27:商标变更代理人/文件接收人高级服务
					//26:商标变更名义地址集体管理规则成员名单自助服务,25:商标变更名义地址集体管理规则成员名单高级服务
					//30:商标更正自助服务,29:商标更正高级服务  
					else if (serviceId==23 || serviceId==24||serviceId==31 || serviceId==32||serviceId==27 || serviceId==28
							||serviceId==29 || serviceId==30||serviceId==25 || serviceId==26){ 
						Map<String, Object> map=new HashMap<String, Object>();
						map.put("gcon", gcon);
						map.put("userId", userId);
						map.put("customerId", customerId);
						map.put("agencyServiceId", agencyServiceId);
						map.put("serviceId", sId);
						map.put("tmCase", tmCase);

						logger.info("start, caseId:"+ caseId + " agencyId:"+ agencyId 
								+ " userId:"+ userId + " custId:"+ custId + 
								" agencyServiceId:"+ agencyServiceId + " serviceId:"+ serviceId);

						startProcessInfo=customerTaskServiceImpl.startTmChangeProcess(map);

					}
//					else if(serviceId == 37 || serviceId == 43){ //启动商标的异议申请/商标撤三申请等复杂案件申请的流程
					else{
						Map<String, Object> map = new HashMap<>();
						map.put("gcon", gcon);
						map.put("userId", userId);
						map.put("customerId", customerId);
						map.put("agencyServiceId", agencyServiceId);
						map.put("serviceId", serviceId);
						map.put("tmCase", tmCase);
						
						logger.info("start, caseId:"+ caseId + " agencyId:"+ agencyId 
								+ " userId:"+ userId + " custId:"+ custId + 
								" agencyServiceId:"+ agencyServiceId + " serviceId:"+ serviceId);
						
						startProcessInfo = customerTaskServiceImpl.startComplexCaseApp(map);
					}
					
					//Modification start, by yang guang, 2018-10-16
					//in order to return taskId, pageId
					if (startProcessInfo == null || !startProcessInfo.getSuccess()){					
						if (msg==null){
							msg="案件"+caseId+"启动失败：" + rtnInfo.getMessage();
						}else{
							msg=msg+";"+"案件"+caseId+"启动失败：" + rtnInfo.getMessage();
						}	
					}else{							
						if (taskId==null){	
							Map<String, Object> resData = (Map<String, Object>) startProcessInfo.getData();	
							Object obj=resData.get("taskList");
							if (obj!=null){
								List<Map<String,Object>> taskList =(List<Map<String,Object>>)obj;	
								if (taskList!=null && taskList.size()>0){
									Map<String,Object> taskmap =taskList.get(0);
									String tId=(String)taskmap.get("taskId");
									taskId=Integer.parseInt(tId);
									taskName=(String)taskmap.get("taskName");
								}
							}
						}
													
						tradeMarkCaseService.createRemind(serviceId, caseId);
					}
				}
				
				//封装后的启动流程
			//	rtnInfo = tradeMarkCaseService.kindOfCaseProcessStart(tradeMarkCase, serviceId,caseIdList,agencyId,userId,gcon,agencyServiceId);
				
				
				TradeMarkCase tmcase=new TradeMarkCase();
				tmcase.setId(tmCaseId);
				
				ToDoList toDoList=new ToDoList();									
				toDoList.setTaskId(taskId);
				toDoList.setTaskName(taskName);
				toDoList.setRemarks(remarks);
				
				Integer pageId=null;
				if (userId!=null && !userId.equals("")){
					pageId=6;
					customerId=null;
				}else{
					pageId=13;
				}
				
				rtnInfo=taskQueryServiceImpl.queryCaseDaiban(userId, customerId, pageId, tmcase, toDoList);
									
				//Modification end

			}
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			rtnInfo.setSuccess(false);
			rtnInfo.setMessageType(-2);
			rtnInfo.setMessage(e.getMessage());
			return rtnInfo;
		}
		
		return rtnInfo;
		
	}
	
	



	

	


}
