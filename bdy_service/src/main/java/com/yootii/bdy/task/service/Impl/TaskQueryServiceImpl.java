package com.yootii.bdy.task.service.Impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.constraints.Null;

import org.springframework.stereotype.Component;
import org.apache.ibatis.javassist.compiler.ast.Keyword;
import org.apache.log4j.Logger;

import com.sun.corba.se.spi.ior.ObjectKey;
import com.yootii.bdy.agency.dao.AgencyMapper;
import com.yootii.bdy.agency.dao.AgencyUserMapper;
import com.yootii.bdy.agency.model.Agency;
import com.yootii.bdy.bill.model.Bill;
import com.yootii.bdy.common.Constants;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.Globals;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.customer.model.Customer;
import com.yootii.bdy.customer.service.CustomerService;
import com.yootii.bdy.mail.MailSenderInfo;
import com.yootii.bdy.mail.SendMailUtil;
import com.yootii.bdy.material.model.Material;
import com.yootii.bdy.model.User;
import com.yootii.bdy.process.service.ProcessService;
import com.yootii.bdy.security.service.AuthenticationService;
import com.yootii.bdy.task.dao.MailRecordMapper;
import com.yootii.bdy.task.dao.TaskRecordMapper;
import com.yootii.bdy.task.model.BillToDoList;
import com.yootii.bdy.task.model.MailRecord;
import com.yootii.bdy.task.model.TaskCondition;
import com.yootii.bdy.task.model.TaskRecord;
import com.yootii.bdy.task.model.TmCaseTaskToDoList;
import com.yootii.bdy.task.model.ToDoList;
import com.yootii.bdy.task.model.ToDolistetail;
import com.yootii.bdy.task.model.UserTask;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseProcessMapper;
import com.yootii.bdy.tmcase.model.IssuanceNumber;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.TradeMarkCaseProcess;
import com.yootii.bdy.tmcase.service.TradeMarkCaseService;
import com.yootii.bdy.user.service.UserService;
import com.yootii.bdy.util.DateTool;
import com.yootii.bdy.util.GraspUtil;
import com.yootii.bdy.util.JsonUtil;
import com.yootii.bdy.util.ObjectUtil;
import com.yootii.bdy.util.ServiceUrlConfig;
import com.yootii.bdy.util.TaskTool;

//---- 客户与代理机构公用接口实现类 -------
@Component
public class TaskQueryServiceImpl {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private ProcessService processService;
	
	@Resource
	private MailRecordMapper mailRecordMapper;

	@Resource
	private TaskRecordMapper taskRecordMapper;

	@Resource
	private AgencyMapper agencyMapper;
	
	@Resource
	private AgencyUserMapper agencyUserMapper;	
	
		
	@Resource
	private TradeMarkCaseMapper tradeMarkCaseMapper;
	
	@Resource
	private TradeMarkCaseProcessMapper tradeMarkCaseProcessMapper;	
	

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
	private TradeMarkCaseService tradeMarkCaseService;
	
	

	// 查询待办事项总数的接口
	public Long queryToDoListCount(GeneralCondition gcon, String userId,
			String customerId, Integer agencyId) throws Exception {		
		
		// 检查输入的参数
		if ((userId == null || userId.equals(""))
				&& (agencyId == null || agencyId.equals(""))) {
			throw new Exception("userId与agencyId不能都为空");
			
		}
		

		String userOrCustId=userId;
		
		List<UserTask> userTaskList =null;
		
		Integer pageId=null;
		
		// userId不为空，意味着要查询代理所的代理人的待办事项
		if (userId != null && !userId.equals("")) {			
			 userTaskList = taskCommonImpl.findUserTask(userOrCustId, pageId);
		}else{
			// 意味着要查询代理所的所有客户的待办事项
			 userTaskList = taskCommonImpl.findAllCustomerTask(agencyId.toString(), pageId);
		}

		
		
        Long total=(long)0L;
		
		TaskCondition taskCondition = new TaskCondition();
		if (userTaskList != null && userTaskList.size() > 0) {			
			 taskCondition = taskCommonImpl.getTaskCondition(gcon,
						userTaskList);
		}
		
		if (userId!=null && !userId.equals("")){
			List<String> userIdList = Arrays.asList(userId.split(","));
			List<Integer> userIds = new ArrayList<Integer>();
			for(String s: userIdList){
				Integer id=new Integer(s);
				userIds.add(id);
//				logger.info("userId "+id);
			}
			taskCondition.setUserIds(userIds);
		}		
		else if (customerId!=null && !customerId.equals("")){
			List<String> custIdList = Arrays.asList(customerId.split(","));
			List<Integer> custIds = new ArrayList<Integer>();
			int len=Constants.customer_prefix.length();
			for(String s: custIdList){
				int pos=s.indexOf(Constants.customer_prefix);
				if(pos>-1){
					s=s.substring(len);
				}				
				Integer id=new Integer(s);
				custIds.add(id);
//				logger.info("custId "+id);				
			}
			taskCondition.setCustIds(custIds);
		}
			
		
		// 获取待办事项对应的案件以及其他未完成的案件的数量
		total= taskRecordMapper.getAgencyDaiBanCaseCount(taskCondition, agencyId);
		
		return total;
		
	}
	
	
/*
	public List<TmCaseTaskToDoList> internalQueryToDoList(
			GeneralCondition gcon, String userId, String customerId)
			throws Exception {
		// 检查输入的参数
		ReturnInfo rtnInfo = TaskTool.checkUserParam(userId, customerId);
		if (!rtnInfo.getSuccess()) {
			throw new Exception(rtnInfo.getMessage());
		}

		String userOrCustId=userId;
		
		TradeMarkCase tmcase=new TradeMarkCase();
		
		// customerId不为空，意味着要查询客户的待办事项
		if (customerId != null && !customerId.equals("")) {
			Integer custId=new Integer(customerId);
			tmcase.setCustId(custId);
//			userId = customerId;
			userOrCustId = Constants.customer_prefix + userId;
		}

		// 获取用户/客户的待办任务的Id的集合
		List<UserTask> userTaskList = taskCommonImpl.findUserTask(userOrCustId,0);

		TaskCondition taskCondition = taskCommonImpl.getTaskCondition(gcon,userTaskList);

		gcon.setOffset(0);
		gcon.setRows(10000);
		
		
		
		Integer uId=null;
		if (userId!=null && !userId.equals("")){
			new Integer(userId);
		}

		// 获取待办事项对应的案件列表
		List<TmCaseTaskToDoList> tmcases = taskRecordMapper.getDaiBanCaseList(
				taskCondition, tmcase, uId,null, gcon);

		return tmcases;
	}
	
	*/

	
	/**
	 * 查询待审核的案子 
	 * @param gcon
	 * @param userId
	 * @param customerId
	 * @param pageId
	 * @param tmcase
	 * @return
	 */
	public ReturnInfo queryWaitAuditTmCase(GeneralCondition gcon, String userId,
			String customerId,Integer pageId,TradeMarkCase tmcase){
		ReturnInfo rtnInfo = new ReturnInfo();
		List<TmCaseTaskToDoList> tmcases = new ArrayList<>();
		Long total = 0L;
		boolean isCustomer=false;
		//查询 待审核的案子
		String userOrCustId=userId;
		tmcase =  tmcase==null? new TradeMarkCase():tmcase;
		if (customerId != null && !customerId.equals("")) {
			Integer custId=new Integer(customerId);
			tmcase.setCustId(custId);				
			userOrCustId = Constants.customer_prefix + customerId;
		}
		// 获取用户/客户的待办任务的Id的集合
		try{
			List<UserTask> userTaskList = taskCommonImpl.findUserTask(userOrCustId,pageId);
			String orderCol=gcon.getOrderCol();
			if (orderCol==null || orderCol.equals("")){				
				orderCol="modifyDate";
				gcon.setOrderCol(orderCol);
				gcon.setOrderAsc("desc");
			}
			TaskCondition taskCondition = new TaskCondition();
			if (userTaskList != null &&  userTaskList.size() > 0) {
				taskCondition =taskCommonImpl.getTaskCondition(gcon,userTaskList);
			}
			Integer uId=null;			
			if (userId!=null && !userId.equals("")){
				uId=new Integer(userId);
				isCustomer=true;
			}
			if(gcon.getKeyword()!=null && gcon.getKeyword()!=""){
				String keyword = gcon.getKeyword();
				keyword="录入"+keyword;
				gcon.setKeyword(keyword);
			}
			if (taskCondition.getCaseIds()!=null && taskCondition.getCaseIds().size()>0){
				tmcases = taskRecordMapper.getDaiBanCaseWithTask(taskCondition, tmcase, uId, gcon);
				// 获取待办事项对应的案件数量
				total = taskRecordMapper.getDaiBanCaseWithTaskCount(taskCondition, tmcase, uId, gcon);
			}
		//遍历
			List<TmCaseTaskToDoList> data = new ArrayList<>();
			if(tmcases.size() != 0){
				for(int i=0;i<tmcases.size();i++){
					TmCaseTaskToDoList tmCaseTaskToDo= tmcases.get(i);
					//Map<String, Object> tmCaseTaskToDo= tmcases.get(i);
					Integer caseId = tmCaseTaskToDo.getCaseId();
					String caseStatus = tmCaseTaskToDo.getCaseStatus();
					String fileType = caseStatus.substring(2, caseStatus.length());
					tmCaseTaskToDo.setFileType(fileType);
					
					//转换
					int fileName = TaskTool.getFileName(caseStatus);
					//根据 caseId 和 fileName 查询
					Map<String, Object> map = taskRecordMapper.queryByCaseIdAndFileName(caseId, fileName);
					Date docDate = (Date)map.get("docDate");
					if(docDate !=null){
						tmCaseTaskToDo.setDocDate(docDate);
					}
					data.add(tmCaseTaskToDo);
				}
			}
			if (total > 0) {
				// 返回的待办事项中增加任务相关的taskId,taskName,remarks
				taskCommonImpl.getToDoList(tmcases, userTaskList, isCustomer,userId);
			}
			
			//返回结果
			rtnInfo.setTotal(total);
			rtnInfo.setCurrPage(gcon.getPageNo());
			rtnInfo.setData(data);
			rtnInfo.setSuccess(true);
			rtnInfo.setMessage("待审核案件查询成功！");
		}catch (Exception e) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("查询失败");
			rtnInfo.setMessageType(-2);
			rtnInfo.setMessage(e.getMessage());
			e.printStackTrace();
		}
		return rtnInfo;
	}
	
	

	
	// 查询当前案件的待办事项列表
	public ReturnInfo queryCaseDaiban(String userId,
			String customerId, Integer pageId, TradeMarkCase tmcase, ToDoList toDoList) {
		ReturnInfo rtnInfo = new ReturnInfo();
		Long total = 0L;
		TmCaseTaskToDoList tmCaseDaiban = null;

		try {

			// 检查输入的参数
			rtnInfo = TaskTool.checkUserParam(userId, customerId);
			if (!rtnInfo.getSuccess()) {
				return rtnInfo;
			}

			boolean isCustomer=false;
			
			
			if (customerId != null && !customerId.equals("")) {				
				isCustomer=true;
			}
			
			Integer uId=null;			
			if (userId!=null && !userId.equals("")){
				uId=new Integer(userId);				
			}				
			
			if (isCustomer){
				tmCaseDaiban = taskRecordMapper.getCustomerDaiBan(tmcase, uId);
			}else{	
				tmCaseDaiban = taskRecordMapper.getUserDaiBan(tmcase, uId);
			}
			
			if (tmCaseDaiban!=null){
				Integer taskId=toDoList.getTaskId();
				String taskName=toDoList.getTaskName();
				String remarks=toDoList.getRemarks();
				
				tmCaseDaiban.setTaskId(taskId);
				tmCaseDaiban.setTaskName(taskName);
				tmCaseDaiban.setRemarks(remarks);
				
				tmCaseDaiban.setPageId(pageId);
			}

			rtnInfo.setTotal(total);				
			rtnInfo.setData(tmCaseDaiban);
			rtnInfo.setSuccess(true);
			rtnInfo.setMessage("查询案件待办事项成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			rtnInfo.setSuccess(false);
			rtnInfo.setMessageType(-2);
			rtnInfo.setMessage(e.getMessage());
			return rtnInfo;
		}
		return rtnInfo;
	}
		
		


	// 查询待办事项列表
	public ReturnInfo queryToDoList(GeneralCondition gcon, String userId,
			String customerId,Integer pageId,TradeMarkCase tmcase) {
		ReturnInfo rtnInfo = new ReturnInfo();
		Long total = 0L;
		List<TmCaseTaskToDoList> tmcases = null;

		try {

			// 检查输入的参数
			rtnInfo = TaskTool.checkUserParam(userId, customerId);
			if (!rtnInfo.getSuccess()) {
				return rtnInfo;
			}

			boolean isCustomer=false;
			
			String userOrCustId=userId;
			
			tmcase =  tmcase==null? new TradeMarkCase():tmcase;
			
			// customerId不为空，意味着要查询客户的待办事项
			if (customerId != null && !customerId.equals("")) {
				Integer custId=new Integer(customerId);
				tmcase.setCustId(custId);				
//				userId = customerId;
				userOrCustId = Constants.customer_prefix + customerId;
				isCustomer=true;
			}
		Long long1 = System.currentTimeMillis();
			// 获取用户/客户的待办任务的Id的集合
			List<UserTask> userTaskList = taskCommonImpl.findUserTask(userOrCustId,pageId);
		Long long2 = System.currentTimeMillis();
		logger.debug("1阶段处理时间："+(long2-long1));
		logger.debug(long2-long1);
//			if (userTaskList == null || userTaskList.size() == 0) {
//				rtnInfo.setTotal(total);
//				rtnInfo.setCurrPage(gcon.getPageNo());
//				rtnInfo.setData(tmcases);
//				rtnInfo.setSuccess(true);
//				return rtnInfo;
//			}

			// 设置排序的参数，如果不设置，sql语句执行出错
			String orderCol=gcon.getOrderCol();
			if (orderCol==null || orderCol.equals("")){
//				if (isCustomer){
//					orderCol="id";
//				}else{
//					orderCol="agentUserId,id";
//				}
				orderCol="modifyDate";
				gcon.setOrderCol(orderCol);
				gcon.setOrderAsc("desc");
			}
			
		Long long3 = System.currentTimeMillis();
			TaskCondition taskCondition = new TaskCondition();
			if (userTaskList != null &&  userTaskList.size() > 0) {
				taskCondition =taskCommonImpl.getTaskCondition(gcon,userTaskList);
			}
		Long long4 = System.currentTimeMillis();
		logger.debug("2阶段处理时间："+(long4-long3));
		logger.debug(long4-long3);
			Integer uId=null;			
			if (userId!=null && !userId.equals("")){
				uId=new Integer(userId);				
			}
			
		Long long5 = System.currentTimeMillis();	
			// 获取待办事项对应的案件列表
			if (pageId!=null){
				if (taskCondition.getCaseIds()!=null && taskCondition.getCaseIds().size()>0){
					tmcases = taskRecordMapper.getDaiBanCaseWithTask(taskCondition, tmcase, uId, gcon);
					// 获取待办事项对应的案件数量
					total = taskRecordMapper.getDaiBanCaseWithTaskCount(taskCondition, tmcase, uId, gcon);
				}
			}else{
				if (isCustomer){
					tmcases = taskRecordMapper.getDaiBanCaseListForCust(taskCondition, tmcase, gcon);
					//如果绝限日期部位空 说明有时限  则计算时限紧急程度  
					if(tmcase !=null){
						for(int i= 0;i<tmcases.size();i++){
							TmCaseTaskToDoList tmCaseTaskToDoList = tmcases.get(i);
							this.getUrgecyDegree(tmCaseTaskToDoList);
						}
					}
				}else{	
					tmcases = taskRecordMapper.getDaiBanCaseList(taskCondition, tmcase, uId, gcon);
					//如果绝限日期部位空 说明有时限  则计算时限紧急程度  
					if(tmcase !=null){
						for(int i= 0;i<tmcases.size();i++){
							TmCaseTaskToDoList tmCaseTaskToDoList = tmcases.get(i);
							this.getUrgecyDegree(tmCaseTaskToDoList);
						}
					}
				}
				// 获取待办事项对应的案件数量
				total = taskRecordMapper.getDaiBanCaseCount(taskCondition, tmcase, uId,pageId, gcon);
			}
		Long long6 = System.currentTimeMillis();
		logger.debug("3阶段处理时间："+(long6-long5));
		logger.debug(long6-long5);
		Long long7 = System.currentTimeMillis();
			if (total > 0) {
				// 返回的待办事项中增加任务相关的taskId,taskName,remarks
				taskCommonImpl.getToDoList(tmcases, userTaskList, isCustomer,userId);
			}
		Long long8 = System.currentTimeMillis();
		logger.debug("4阶段处理时间："+(long8-long7));
		logger.debug(long8-long7);
			rtnInfo.setTotal(total);
			rtnInfo.setCurrPage(gcon.getPageNo());
			rtnInfo.setData(tmcases);
			rtnInfo.setSuccess(true);
			rtnInfo.setMessage("查询案件待办事项成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			rtnInfo.setSuccess(false);
			rtnInfo.setMessageType(-2);
			rtnInfo.setMessage(e.getMessage());
			return rtnInfo;
		}
		return rtnInfo;
	}

	//获取紧急程度
	private void getUrgecyDegree(TmCaseTaskToDoList tmCaseTaskToDoList){
		Integer isclose = tmCaseTaskToDoList.getIsclose();
		if(isclose !=null && isclose == 1){
			tmCaseTaskToDoList.setCreatedate(null);
			tmCaseTaskToDoList.setLimitdate(null);
		}
		
		Date begin = tmCaseTaskToDoList.getCreatedate();
		Date end = tmCaseTaskToDoList.getLimitdate();
		String message = tmCaseTaskToDoList.getMessage();
		
		if(begin != null && end !=null){
			
			long  beginMillSeconds= begin.getTime();
			long endMillSeconds = end.getTime();
			long gap = endMillSeconds - beginMillSeconds;
			//获取相差多少天 
			int gapDays = (int)(gap/1000)/(3600*24);
			//分成 3个时期  每个时期的天数
			int avgDays = gapDays/3;
			//安全期  (创建日期向后+一个平均日)
			Date security = DateTool.getDateAfter(begin, avgDays);
			//提醒期(创建日期向后+两个个平均日)
			Date warn =  DateTool.getDateAfter(begin, avgDays*2);
			Date now= new Date();
			if(now.before(security)){
				tmCaseTaskToDoList.setUrgencyDegree("安全期");
				//晚于 安全期  早于 提醒期  处于提醒期
			}else if(now.after(security) && now.before(warn)){
				tmCaseTaskToDoList.setUrgencyDegree("提醒期");
			}else{
				tmCaseTaskToDoList.setUrgencyDegree("危险期");
			}
			
		}
		
	}
	
	
	// 待办事项详情接口
	public ReturnInfo toDoListDetail(GeneralCondition gcon, String customerId,
			String userId, String taskId, TmCaseTaskToDoList toDoList) {

		// 返回结果对象
		ReturnInfo rtnInfo = new ReturnInfo();

		ToDolistetail toDolistetail = new ToDolistetail();

		try {
			
			// 检查输入的用户参数
			rtnInfo = TaskTool.checkUserParam(userId, customerId);
			if (!rtnInfo.getSuccess()) {
				return rtnInfo;
			}
			Integer cId=toDoList.getCaseId();
			
			if (cId==null){
				// 检查taskId
				String name = "taskId";
				String value = taskId;
				rtnInfo = TaskTool.checkId(name, value);
				if (!rtnInfo.getSuccess()) {
					return rtnInfo;
				}
				toDolistetail.setId(cId);
			}
			
			if(taskId==null || taskId.equals("")){
				if (cId == null ) {
					rtnInfo.setSuccess(false);
					rtnInfo.setMessage("caseId不能为空");
					rtnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
					return rtnInfo;
				}				
			}
			
			String caseId = null;
			String taskName = null;
			if (taskId!=null && !taskId.equals("")){
				// 获取流程参数
				ReturnInfo rtInfo = processService.showtaskvariables(taskId);
				if (rtInfo.getSuccess() != true) {
					throw new Exception("获取流程中的参数失败|" + rtInfo.getMessage());
				}
				Map<String, Object> proMap = (Map<String, Object>) rtInfo.getData();
				if (proMap == null) {
					throw new Exception("获取流程中的参数结果为空, taskId:" + taskId);
				}
	
				Object obj = proMap.get("caseId");
				if (obj == null) {
					throw new Exception("获取流程中的caseId结果为空, taskId:" + taskId);
				}
				
				caseId = (String) obj;
	
				obj = proMap.get("taskName");
				if (obj == null) {
					throw new Exception("获取流程中的taskName结果为空, taskId:" + taskId);
				}
				taskName = (String) obj;
	
				obj = proMap.get("remarks");
				if (obj != null) {
					String remarks = (String) obj;
					toDolistetail.setRemarks(remarks);
				}
				//错误消息
				obj = proMap.get("ErroMessage");
				if (obj != null) {
					String ErroMessage = (String) obj;
					toDolistetail.setRemarks(ErroMessage);
				}
				
				toDolistetail.setTaskName(taskName);
				Integer tId = new Integer(taskId);
				toDolistetail.setTaskId(tId);
				cId = new Integer(caseId);
				toDolistetail.setId(cId);
			}
			
			
			//获取案件信息
			TradeMarkCase tmcase = tradeMarkCaseMapper.selectByPrimaryKey(cId);	
			
			//获取模板文件路径
			toDolistetail = this.setDoc(toDolistetail, cId);
			
			if(tmcase!=null){
				//获取fileName
//				List<Material> materials = tmcase.getMaterials();
//				if(materials !=null) {									
//					for(Material m : materials) {
//						String fileName = processFileName(m.getSubject());
//						m.setFileName(fileName);
//						
//					}
//				}
				
				
				// Modification start, by yang guang, 2018-10-31
				// 支持商标驳回复审案件， 按照规则提供默认的发文编号
				String caseType=tmcase.getCaseType();				
				if (caseType.equals("商标驳回复审")){
					String docNumber=tmcase.getDocNumber();
					if (docNumber==null || docNumber.equals("")){
						String cType="商标";
						String fileName="驳回通知";
						IssuanceNumber issNum = tradeMarkCaseService.getIssuanceNumber(cType, fileName);
						String appNumber=tmcase.getAppNumber();
						if (appNumber!=null && !appNumber.equals("")){
							docNumber = issNum.getPrefix().toUpperCase()+appNumber+issNum.getSuffix().toUpperCase();
							if("是".equals(issNum.getIsOrder())){
								docNumber  = docNumber+"01";
							}
						}
						tmcase.setDocNumber(docNumber);						
					}
				}
				// Modification end
				
				//将案件对象的设置到待办事项详情对象中
				ObjectUtil.setProperty(tmcase, toDolistetail);	
				
				toDolistetail.setGoods(tmcase.getGoods());
				toDolistetail.setChargeRecords(tmcase.getChargeRecords());
				toDolistetail.setJoinApps(tmcase.getJoinApps());
				//2018-07-24 added 
				toDolistetail.setMaterials(tmcase.getMaterials());
				
				//Modification start, 待办事项详情中的文件列表，是单独调用获取案件的相关文件接口
				//因此，这里不再需要使用TradeMarkCaseFile对象
				
//				toDolistetail.setTradeMarkCaseFiles(tmcase.getTradeMarkCaseFiles());
				
				//Modification end
				
				//获取案件最新进展
				TradeMarkCaseProcess tradeMarkCaseProcess=tradeMarkCaseProcessMapper.selectByCaseId(cId);
				if (tradeMarkCaseProcess!=null){
					String caseStatus=tradeMarkCaseProcess.getStatus();
					String failReason=tradeMarkCaseProcess.getFailReason();
					toDolistetail.setCaseStatus(caseStatus);
					toDolistetail.setFailReason(failReason);
				}

				
				//合作代理机构Id
				Integer coAgencyId=tmcase.getCoagencyId();
				
				//设置案件来源属性
				if (coAgencyId!=null){			
					Agency agency=agencyMapper.selectByPrimaryKey(coAgencyId);			
					String agencyName=agency.getName();			
					toDolistetail.setCasefrom(agencyName);
				}else if (customerId!=null){
					String tokenID=gcon.getTokenID();
					Customer customer=customerService.getCustById(customerId, tokenID);
					String custName=customer.getName();
					toDolistetail.setCasefrom(custName);
				}
			}
								
			
			rtnInfo.setData(toDolistetail);
			rtnInfo.setSuccess(true);

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			rtnInfo.setSuccess(false);
			rtnInfo.setMessageType(-2);
			rtnInfo.setMessage(e.getMessage());
			return rtnInfo;
		}

		return rtnInfo;

	}

	//设置文档路径
	private ToDolistetail setDoc(ToDolistetail toDolistetail,Integer cId){
		
		Map<String, Object> doc = tradeMarkCaseMapper.selectMaterial(cId);
		Map<String, Object> dissentDoc = tradeMarkCaseMapper.selectMaterialForApplicant(cId);
		Map<String, Object> proxyDoc = tradeMarkCaseMapper.selectMaterialForProxy(cId);
		Map<String, Object> replyDoc = tradeMarkCaseMapper.selectMaterialForReply(cId);
		if(doc !=null){
			String docFile = (String)doc.get("address");
			String docTitle = (String)doc.get("title");
			toDolistetail.setDocFile(docFile);
			toDolistetail.setDocTitle(docTitle);
		}
		if(dissentDoc != null){
			String dissentDocFile = (String)dissentDoc.get("address");
			String dissentDocTitle = (String)dissentDoc.get("title");
			toDolistetail.setDissentDoc(dissentDocFile);
			toDolistetail.setDissentDocTitle(dissentDocTitle);
		}
		if(proxyDoc !=null){
			String proxy = (String)proxyDoc.get("address");
			toDolistetail.setProxyDoc(proxy);
		}
		if(replyDoc !=null){
			Date reply = (Date)replyDoc.get("createTime");
			toDolistetail.setReplyReceiveDate(reply);
		}
		
		return toDolistetail;
	}
	
	
	
	// 查询待办事项列表
	public ReturnInfo queryBillToDoList(GeneralCondition gcon, String userId,
			String customerId) {
		ReturnInfo rtnInfo = new ReturnInfo();
		Long total = 0L;
		List<BillToDoList> bills = null;

		try {

			// 检查输入的参数
			rtnInfo = TaskTool.checkUserParam(userId, customerId);
			if (!rtnInfo.getSuccess()) {
				return rtnInfo;
			}

			// customerId不为空，意味着要查询客户的待办事项
			if (customerId != null && !customerId.equals("")) {
				userId = customerId;
				userId = Constants.customer_prefix + userId;
			}

			// 获取用户/客户的待办任务的Id的集合
			List<UserTask> userTaskList =  taskCommonImpl.findBillTask(userId);

			if (userTaskList == null || userTaskList.size() == 0) {
				rtnInfo.setTotal(total);
				rtnInfo.setCurrPage(gcon.getPageNo());
				rtnInfo.setData(bills);
				rtnInfo.setSuccess(true);
				return rtnInfo;
			}

			TaskCondition taskCondition = taskCommonImpl.getBillTaskCondition(
					gcon, userTaskList);

			// 设置排序的参数，如果不设置，sql语句执行出错

			gcon.setOrderCol("billId");
			gcon.setOrderAsc("desc");
			
			if (taskCondition!=null  && !taskCondition.equals("")){

				// 获取待办事项对应的案件列表
				bills = taskRecordMapper.getDaiBanBillList(taskCondition, gcon);
	
				// 获取待办事项对应的案件数量
				total = taskRecordMapper.getDaiBanBillCount(taskCondition);
	
				if (total > 0) {
					// 返回的待办事项中增加任务相关的taskId,taskName,remarks
					getBillToDoList(bills, userTaskList);
				}
			}

			rtnInfo.setTotal(total);
			rtnInfo.setCurrPage(gcon.getPageNo());
			rtnInfo.setData(bills);
			rtnInfo.setSuccess(true);
			rtnInfo.setMessage("查询案件待办事项成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			rtnInfo.setSuccess(false);
			rtnInfo.setMessageType(-2);
			rtnInfo.setMessage(e.getMessage());
			return rtnInfo;
		}
		return rtnInfo;
	}

	private void getBillToDoList(List<BillToDoList> bills,
			List<UserTask> userTaskList) {
		for (BillToDoList toToList : bills) {
			Integer billId = toToList.getBillId();
			String bId = billId.toString();
			if (userTaskList != null && userTaskList.size() > 0) {
				for (UserTask userTask : userTaskList) {					
					String id = userTask.getProMap().get("billId").toString();
					if (id.equals(bId)) {
						String taskId = userTask.getTaskId();
						String taskName = userTask.getTaskName();
						String remarks = userTask.getRemarks();
						Integer tId = new Integer(taskId);
						toToList.setTaskId(tId);
						toToList.setTaskName(taskName);
						toToList.setRemarks(remarks);
					}
				}
			}

			// String taskName = toToList.getTaskName();
			Integer pageId = 11;
			toToList.setPageId(pageId);

		}

		// logger.info("getToDoList end");

	}

	// 待办事项详情接口
	public ReturnInfo billtoDoListDetail(GeneralCondition gcon,
			String customerId, String userId, String taskId,
			TmCaseTaskToDoList toDoList) {

		// 返回结果对象
		ReturnInfo rtnInfo = new ReturnInfo();

		ToDolistetail toDolistetail = new ToDolistetail();

		try {

			// 检查输入的用户参数
			rtnInfo = TaskTool.checkUserParam(userId, customerId);
			if (!rtnInfo.getSuccess()) {
				return rtnInfo;
			}

			// 检查taskId
			String name = "taskId";
			String value = taskId;
			rtnInfo = TaskTool.checkId(name, value);
			if (!rtnInfo.getSuccess()) {
				return rtnInfo;
			}

			// customerId不为空，意味着要查询客户的待办事项
			if (customerId != null && !customerId.equals("")) {
				userId = customerId;
				userId = Constants.customer_prefix + userId;
			}

			// 获取流程参数
			ReturnInfo rtInfo = processService.showtaskvariables(taskId);
			if (rtInfo.getSuccess() != true) {
				throw new Exception("获取流程中的参数失败|" + rtInfo.getMessage());
			}
			Map<String, Object> proMap = (Map<String, Object>) rtInfo.getData();
			if (proMap == null) {
				throw new Exception("获取流程中的参数结果为空, taskId:" + taskId);
			}

			Object obj = proMap.get("caseId");
			if (obj == null) {
				throw new Exception("获取流程中的caseId结果为空, taskId:" + taskId);
			}
			String caseId = (String) obj;

			obj = proMap.get("taskName");
			if (obj == null) {
				throw new Exception("获取流程中的taskName结果为空, taskId:" + taskId);
			}
			String taskName = (String) obj;

			obj = proMap.get("remarks");
			if (obj != null) {
				String remarks = (String) obj;
				toDolistetail.setRemarks(remarks);
			}

			toDolistetail.setTaskName(taskName);
			Integer tId = new Integer(taskId);
			toDolistetail.setTaskId(tId);
			Integer id = new Integer(caseId);
			toDolistetail.setId(id);
			TradeMarkCase tmcase = tradeMarkCaseMapper.selectByPrimaryKey(id);
			ObjectUtil.setProperty(tmcase, toDolistetail);

			// 获取用户/客户的待办任务的Id的集合

			/*
			 * List<UserTask> userTaskList = findUserTask(userId); if
			 * (userTaskList != null && userTaskList.size() > 0) {
			 * 
			 * for (UserTask userTask : userTaskList) { String cId =
			 * userTask.getCaseId(); String taskName = userTask.getTaskName();
			 * String tId = userTask.getTaskId(); String remarks =
			 * userTask.getRemarks(); if (cId.equals(caseId)) {
			 * toDolistetail.setTaskTitle(taskName); Integer tId = new
			 * Integer(tId); toDolistetail.setTaskId(tId);
			 * toDolistetail.setRemarks(remarks); break; } }
			 * 
			 * Integer id = new Integer(caseId); TradeMarkCase tmcase =
			 * tradeMarkCaseMapper .selectByPrimaryKey(id);
			 * ObjectUtil.setProperty(tmcase, toDolistetail); }
			 */
			

			rtnInfo.setData(toDolistetail);
			rtnInfo.setSuccess(true);

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			rtnInfo.setSuccess(false);
			rtnInfo.setMessageType(-2);
			rtnInfo.setMessage(e.getMessage());
			return rtnInfo;
		}

		return rtnInfo;

	}
	
	
	public ReturnInfo queryMailRecord(TmCaseTaskToDoList toDoList) {
		ReturnInfo rtnInfo = new ReturnInfo();		
		List<MailRecord> list = null;
		try {

			// 检查输入的参数			
			Integer caseId=toDoList.getCaseId();
			if (caseId==null){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("caseId不能为空");
				rtnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
				return rtnInfo;
			}
		
			list=mailRecordMapper.getMailRecordList(caseId);		
			rtnInfo.setData(list);
			rtnInfo.setSuccess(true);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			rtnInfo.setSuccess(false);
			rtnInfo.setMessageType(-2);
			rtnInfo.setMessage(e.getMessage());
			return rtnInfo;
		}
		return rtnInfo;
	}

	private String processFileName(String subject) {
		String s = null;
		if(subject != null) {
			String[] split = subject.split(",");
			for(int i=0;i<split.length;i++) {
				String[] split2 = split[i].split(" : ");
				if("fileName".equals(split2[0])) {
					s=split2[1];
				}
			}
		}
		return s;
		
	}

}