package com.yootii.bdy.task.service.Impl;


import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yootii.bdy.agency.model.Agency;
import com.yootii.bdy.agency.service.AgencyService;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.process.service.ProcessService;
import com.yootii.bdy.remind.service.RemindService;
import com.yootii.bdy.task.dao.TaskRecordMapper;
import com.yootii.bdy.task.model.ReturnToDoAmount;
import com.yootii.bdy.task.model.TaskRecord;
import com.yootii.bdy.task.model.TmCaseTaskToDoList;
import com.yootii.bdy.task.model.ToDoList;
import com.yootii.bdy.task.service.TradeMarkCaseTaskService;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseMapper;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.service.TradeMarkCaseService;
import com.yootii.bdy.trademark.dao.TrademarkMapper;
import com.yootii.bdy.trademark.service.TradeMarkService;


@Service
public class TradeMarkCaseTaskServiceImpl implements TradeMarkCaseTaskService {

	private final Logger logger = Logger.getLogger(this.getClass());


	@Resource
	private TaskRecordMapper taskRecordMapper;

	@Resource
	private TaskCommonImpl taskCommonImpl;
	
	@Resource
	private CustomerTaskServiceImpl customerTaskServiceImpl;
	
	@Resource
	private AgencyTaskServiceImpl agencyTaskServiceImpl;
	
	@Resource
	private AgencyService agencyService;
	
	@Resource
	private TaskQueryServiceImpl taskQueryServiceImpl;
	
	@Resource
	private TaskBasicServiceImpl taskBasicServiceImpl;
	
	@Resource
	private TradeMarkCaseMapper tradeMarkCaseMapper;
	
	@Resource
	private TradeMarkCaseTaskManageImpl tradeMarkCaseTaskManageImpl;
	
	@Resource
	private MultiProcessServiceImpl multiProcessServiceImpl;
	
	@Resource
	private ProcessService processService;
	
	@Resource
	private TradeMarkService tradeMarkService;
	
	@Resource
	private TradeMarkCaseService tradeMarkCaseService;
	

	@Resource
	private RemindService remindService;
	
	@Resource
	private TrademarkMapper trademarkMapper;
	
	
	
	
	
	public ReturnInfo autoSaveGuanWen(String fileName, TradeMarkCase tradeMarkCase, String taskId){
		return agencyTaskServiceImpl.autoSaveGuanWen(fileName, tradeMarkCase, taskId);
	}
	

	// ---- 客户与代理机构公用接口 -------
	// 查询待办事项总数的接口
	public Long queryToDoListCount(GeneralCondition gcon, String userId, String customerId, Integer agencyId) throws Exception{
		return taskQueryServiceImpl.queryToDoListCount(gcon, userId, customerId, agencyId);
	}
	
	// 查询当前案件待办事项
	@Override
	public ReturnInfo queryCaseDaiban(String userId,
			String customerId, Integer pageId, TradeMarkCase tmcase, ToDoList toDoList) {
		ReturnInfo rtnInfo = taskQueryServiceImpl.queryCaseDaiban(userId, customerId, pageId, tmcase, toDoList);
		return rtnInfo;
	}

	// 查询待办事项列表
	@Override
	public ReturnInfo queryToDoList(GeneralCondition gcon, String userId,
			String customerId, Integer pageId,TradeMarkCase tmcase) {
		ReturnInfo  rtnInfo = new ReturnInfo();
		
			 rtnInfo = taskQueryServiceImpl.queryToDoList(gcon, userId, customerId,pageId,tmcase);
		
		return rtnInfo;
	}	

	// 待办事项详情接口
	public ReturnInfo toDoListDetail(GeneralCondition gcon, String customerId,
			String userId, String taskId, TmCaseTaskToDoList toDoList) {
		ReturnInfo rtnInfo = taskQueryServiceImpl.toDoListDetail(gcon, customerId, userId, taskId, toDoList);
		return rtnInfo;
	}	
	
	// 拒绝接口
	public ReturnInfo refuse(GeneralCondition gcon, String userId,
			String customerId, TmCaseTaskToDoList toDoList, Map<String, Object> proMap) {
		ReturnInfo rtnInfo = taskBasicServiceImpl.refuse(gcon, userId, customerId, toDoList,proMap);
		return rtnInfo;
	}
	
	// 提交案件接口
	public ReturnInfo submitCase(GeneralCondition gcon, 
			String userId, String customerId, TmCaseTaskToDoList toDoList) {	
		ReturnInfo rtnInfo = customerTaskServiceImpl.submitCase(gcon, userId, customerId, toDoList);
		return rtnInfo;
	}
	
	
	
	// 记录客户与搭理人的交流信息的接口
	public ReturnInfo queryMailRecord(TmCaseTaskToDoList toDoList) {	
		ReturnInfo rtnInfo = taskQueryServiceImpl.queryMailRecord(toDoList);
		return rtnInfo;
	}



	
	//---- 为商标流程引擎中配置的servicetask所调用的接口 -------	
	// 转发案件接口
	public ReturnInfo transferCase(GeneralCondition gcon, String userId, String processInstanceId,String agencyLevel, String agencyServiceId, TradeMarkCase tmCase) {		
		ReturnInfo rtnInfo = multiProcessServiceImpl.transferCase(gcon, userId, processInstanceId, agencyLevel, agencyServiceId, tmCase);
		return rtnInfo;
	}
	
	// 报告拒绝处理的消息的接口
	public ReturnInfo notifyRefuseCase(GeneralCondition gcon, String userId, String processInstanceId, String agencyLevel){
		ReturnInfo rtnInfo = multiProcessServiceImpl.notifyRefuseCase(gcon, userId, processInstanceId, agencyLevel);
		return rtnInfo;
	}
	
	// 报告案件结果的消息的接口
	public ReturnInfo notifyCaseResult(GeneralCondition gcon, String userId, String processInstanceId,String agencyLevel) {		
		ReturnInfo rtnInfo = multiProcessServiceImpl.notifyCaseResult(gcon, userId, processInstanceId, agencyLevel);
		return rtnInfo;
	}
	
	// 报告子案件结果的消息的接口
	public ReturnInfo notifyChildCaseResult(GeneralCondition gcon, String caseId, String processInstanceId) {		
		ReturnInfo rtnInfo = multiProcessServiceImpl.notifyChildCaseResult(gcon, caseId, processInstanceId);
		return rtnInfo;
	}

	public ReturnInfo notifyAppResult(GeneralCondition gcon,  String submitStatus, String processInstanceId){
		ReturnInfo rtnInfo = multiProcessServiceImpl.notifyAppResult(gcon, submitStatus, processInstanceId);
		return rtnInfo;
	}
	
	// 报告官方通知的消息的接口
	public ReturnInfo notifyOfficialNotice(GeneralCondition gcon, String userId, String custId, String processInstanceId,String lastAgencyLevel, String fileName, String lastProcessInstanceId) {		
		ReturnInfo rtnInfo = multiProcessServiceImpl.notifyOfficialNotice(gcon, userId, custId, processInstanceId, lastAgencyLevel,fileName, lastProcessInstanceId);
		return rtnInfo;
	}
	
	// 报告客户决定的消息的接口
	public ReturnInfo notifyCustomerDecision(GeneralCondition gcon, String userId, String processInstanceId,String agencyLevel, String lastAgencyLevel, String customerDecision, String fileName){
		ReturnInfo rtnInfo = multiProcessServiceImpl.notifyCustomerDecision(gcon, userId, processInstanceId, agencyLevel, lastAgencyLevel, customerDecision,fileName);
		return rtnInfo;
	}	
	
		
	// 创建驳回复审案件的接口
	public ReturnInfo createRejectCase(GeneralCondition gcon, String userId, String processInstanceId, TradeMarkCase tmCase){
		ReturnInfo rtnInfo = multiProcessServiceImpl.createRejectCase(gcon, userId, processInstanceId, tmCase);
		return rtnInfo;

	}
	
	
	// 创建异议答辩案件的接口
	public ReturnInfo createObjectionDefenseCase(GeneralCondition gcon, String userId, String processInstanceId, TradeMarkCase tmCase){
		ReturnInfo rtnInfo = multiProcessServiceImpl.createObjectionDefenseCase(gcon, userId, processInstanceId, tmCase);
		return rtnInfo;
	}

	
	// 创建不予注册复审案件的接口
	public ReturnInfo createNewCase(GeneralCondition gcon, String userId, String processInstanceId, TradeMarkCase tmCase){
		ReturnInfo rtnInfo = multiProcessServiceImpl.createNewCase(gcon, userId, processInstanceId, tmCase);
		return rtnInfo;
	}
	
	
	// 创建商标诉讼案件的接口
	public ReturnInfo createLawsuitCase(GeneralCondition gcon, String userId, String processInstanceId, TradeMarkCase tmCase){
		ReturnInfo rtnInfo = multiProcessServiceImpl.createLawsuitCase(gcon, userId, processInstanceId, tmCase);
		return rtnInfo;

	}

	
	//修改案件状态的接口
	public ReturnInfo modifyCaseStatus(GeneralCondition gcon, String userId, String caseId, String fileName, String refuse, String caseResult, String processInstanceId){
		ReturnInfo rtnInfo = taskBasicServiceImpl.modifyCaseStatus(gcon, userId, caseId, fileName, refuse, caseResult, processInstanceId);
		return rtnInfo;
	}
	
	

	// ---- 客户使用的接口 -------

	// 启动商标注册流程
	public ReturnInfo startTmRegisterProcess(Map<String, Object> map) {
		ReturnInfo rtnInfo = customerTaskServiceImpl.startTmRegisterProcess(map);
		return rtnInfo;
	}
	
	// 启动商标变更流程
	public ReturnInfo startTmChangeProcess(Map<String, Object> map) {
		ReturnInfo rtnInfo = customerTaskServiceImpl.startTmChangeProcess(map);
		return rtnInfo;
	}

	//启动商标的异议申请/商标撤三申请等复杂案件申请的流程
	public ReturnInfo startComplexCaseApp(Map<String, Object> map){
		ReturnInfo returnInfo = customerTaskServiceImpl.startComplexCaseApp(map);
		return returnInfo;
	}
	
	// 同意接口
	public ReturnInfo agree(GeneralCondition gcon, 
			String customerId, TmCaseTaskToDoList toDoList) {	
		ReturnInfo rtnInfo = customerTaskServiceImpl.agree(gcon,customerId, toDoList);
		return rtnInfo;
	}

	
	// 关闭案件接口
	public ReturnInfo closeCase(GeneralCondition gcon,
			String customerId, TmCaseTaskToDoList toDoList) {	
		ReturnInfo rtnInfo = customerTaskServiceImpl.closeCase(gcon, customerId, toDoList);
		return rtnInfo;
	}
	
	// 修改案件接口
	public ReturnInfo modifyCase(Map<String, Object> map, Map<String, Object> proMap){
		ReturnInfo rtnInfo = customerTaskServiceImpl.modifyCase(map,proMap);
		return rtnInfo;
	}
		
	
	
		
	// ---- 代理机构使用的接口 -------		
	// 处理案件，几种可能的处理方式：转发案件，接收案件，拒绝案件
	public ReturnInfo assginCase(GeneralCondition gcon, String userId,
			String agencyId, String transfer, TmCaseTaskToDoList toDoList, Map<String, Object> proMap){
		ReturnInfo rtnInfo = agencyTaskServiceImpl.assginCase(gcon, userId, agencyId, transfer, toDoList, proMap);
		return rtnInfo;
	}

	// 审核不通过接口
	public ReturnInfo notAudited(GeneralCondition gcon, String userId,
			TmCaseTaskToDoList toDoList) {		
		ReturnInfo rtnInfo = agencyTaskServiceImpl.notAudited(gcon, userId, toDoList);
		return rtnInfo;
	}

	//
	public ReturnInfo audited(GeneralCondition gcon, String userId, String submitMode, TmCaseTaskToDoList toDoList,Boolean send, Map<String, Object> proMap) {
		ReturnInfo rtnInfo = agencyTaskServiceImpl.audited(gcon, userId, submitMode, toDoList,send, proMap);
		return rtnInfo;
	}

	// 递交申请接口
	public ReturnInfo submitApp(GeneralCondition gcon, String userId, String custId, String caseId, String processInstanceId) {
		ReturnInfo rtnInfo = agencyTaskServiceImpl.submitApp(gcon, userId, custId, caseId, processInstanceId);
		return rtnInfo;
	}
	
	// 直接递交接口
	public ReturnInfo appOffLine(GeneralCondition gcon, String userId,
			String submitStatus, TmCaseTaskToDoList toDoList, Map<String, Object> proMap) {	
		ReturnInfo rtnInfo = agencyTaskServiceImpl.appOffLine(gcon, userId, submitStatus, toDoList, proMap);
		return rtnInfo;
	}
	
	
	//设置网上申请结果接口	
	public ReturnInfo setAppOnLineResult(GeneralCondition gcon,  String userId, String submitStatus,TmCaseTaskToDoList toDoList){
		ReturnInfo rtnInfo = agencyTaskServiceImpl.setAppOnLineResult(gcon, userId,  submitStatus, toDoList);
		return rtnInfo;
	}
	
		
	// 官文录入接口
	public ReturnInfo officalDoc(GeneralCondition gcon, String userId, String fileName,
			TradeMarkCase tradeMarkCase, TmCaseTaskToDoList toDoList) {		
		ReturnInfo rtnInfo = agencyTaskServiceImpl.officalDoc(gcon, userId, fileName, tradeMarkCase, toDoList);
		return rtnInfo;
	}	
	
	public ReturnInfo auditOfficalDoc(GeneralCondition gcon, String userId, String auditResult, 
			TmCaseTaskToDoList toDoList, Map<String, Object> proMap){
		ReturnInfo rtnInfo = agencyTaskServiceImpl.auditOfficalDoc(gcon, userId, auditResult, toDoList, proMap);
		return rtnInfo;
	}
	
	// 向客户报告接口
	public ReturnInfo processDoc(GeneralCondition gcon, String userId, String approved,
			TmCaseTaskToDoList toDoList) {	
		ReturnInfo rtnInfo = agencyTaskServiceImpl.processDoc(gcon, userId, approved, toDoList);
		return rtnInfo;
	}

	// 向客户报告接口
	public ReturnInfo feedback(GeneralCondition gcon, String userId,
			TmCaseTaskToDoList toDoList) {	
		ReturnInfo rtnInfo = agencyTaskServiceImpl.feedback(gcon, userId, toDoList);
		return rtnInfo;
	}

	// 处理客户决定接口
	public ReturnInfo processDecision(GeneralCondition gcon, String userId,
			TmCaseTaskToDoList toDoList) {		
		ReturnInfo rtnInfo = agencyTaskServiceImpl.processDecision(gcon, userId, toDoList);
		return rtnInfo;
	}
	
	//  等待对方的决定
	@Override
	public ReturnInfo oppositeDecision(GeneralCondition gcon, String userId,
			TmCaseTaskToDoList toDoList) {
		
		ReturnInfo returnInfo = agencyTaskServiceImpl.oppositeDecision(gcon, userId, toDoList);
		return returnInfo;
	}
	
	
	// 错误处理接口
	public ReturnInfo runAgain(GeneralCondition gcon, String userId,
			TmCaseTaskToDoList toDoList) {	
		ReturnInfo rtnInfo = agencyTaskServiceImpl.runAgain(gcon, userId, toDoList);
		return rtnInfo;
	}
	
	

	@Override
	public ReturnInfo createTaskRecord(TaskRecord taskRecord) {
		ReturnInfo info = new ReturnInfo();
		taskRecordMapper.insertSelective(taskRecord);
		info.setSuccess(true);
		info.setMessage("添加案件待办事项成功");
		return info;
	}

	
	
	
	@Override
	public ReturnInfo modifyTaskRecord(
			TaskRecord taskRecord) {
		ReturnInfo info = new ReturnInfo();
		if (taskRecord.getId() == null) {
			info.setSuccess(false);
			info.setMessage("案件待办事项Id不能为空");
			return info;
		}
		taskRecordMapper.updateByPrimaryKeySelective(taskRecord);
		info.setSuccess(true);
		info.setMessage("修改案件成功");
		return info;
	}

	@Override
	public ReturnInfo statsByAgency(Integer agencyId, GeneralCondition gcon) {
		ReturnInfo info = new ReturnInfo();
		ReturnToDoAmount toDo=statsCountByAgency( agencyId,  gcon);
		
		ReturnToDoAmount toDo2=statsCountByCust( agencyId,  gcon);
		
		toDo.setAmountByCust(toDo2.getAmountByCust());
		
		info.setData(toDo);
		info.setSuccess(true);
		info.setMessage("查询成功");
		return info;
	}
	
	
	public ReturnToDoAmount statsCountByAgency(Integer agencyId, GeneralCondition gcon) {
		
		ReturnToDoAmount toDo = new ReturnToDoAmount();
		ReturnToDoAmount toDoAmount = agencyService.queryCustIdandUserIdByagency(agencyId, gcon.getTokenID());
		
		List<Integer> userIdList = new ArrayList<>();
		
		if(toDoAmount.getUserId() != null && toDoAmount.getUserId().size()>0) {
			userIdList = toDoAmount.getUserId();
		}
		
		Long amountByAgency=0L;
		if(userIdList!=null && userIdList.size()>0) {
			String userIds=null;
			for(Integer userId:userIdList) {
				if (userIds==null){
					userIds=userId.toString();
				}else{
					userIds=userIds+","+userId.toString();
				}
			}
			try {
				amountByAgency = queryToDoListCount(gcon, userIds, null, agencyId);
				toDo.setAmountByAgency(amountByAgency);			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return toDo;
	}
	
	
	public ReturnToDoAmount statsCountByCust(Integer agencyId, GeneralCondition gcon) {
		
		ReturnToDoAmount toDo = new ReturnToDoAmount();
			
		Long custtodoAmount=0L;	
		Long totalCount=(long)0;
		
		try {
			 totalCount= queryToDoListCount(gcon, null, null, agencyId);
			 custtodoAmount=new Long(totalCount);
			 toDo.setAmountByCust(custtodoAmount);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return toDo;
	}
	
	
	

	@Override
	public ReturnInfo statsToDoTop10(GeneralCondition gcon) {
		ReturnInfo info = new ReturnInfo();
		List<Integer> agencyIds = agencyService.queryAgencyList();
		List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();
		List<Long> l= new ArrayList<Long>();
		if(agencyIds.size()>0) {
			
			for(Integer agencyId : agencyIds){
				Map<String,Object> map = new HashMap<String,Object>();
				  ReturnToDoAmount toDoAmount = statsCountByAgency(agencyId,gcon);
				  Agency agency = agencyService.queryAgencyById(agencyId);
				  Long amountByAgency = toDoAmount.getAmountByAgency();
				  if(amountByAgency>0) {
					  map.put("todoAmount", amountByAgency);
				  }else {
					  map.put("todoAmount", 0);
				  }
				  
				  map.put("agencyName", agency.getName());
				  map.put("agencyId", agencyId);
				  l.add(amountByAgency);
				  list.add(map);
			}
			
		}
		List<Map<String,Object>> listTop10 =new ArrayList<Map<String,Object>>();
		
		for(int i = 0 ; i<l.size()-1; i++) {
			for(int j = 0; j<list.size()-1-i; j++) {
				if(l.get(j) < l.get(j+1)) {
					long temp = l.get(j);  
					l.set(j, l.get(j+1));
					l.set(j+1, l.get(j) );
					 
				}
			}
		}
		if(l.size()>10) {
			l.subList(0, 9);
		}
		
		for(int i = 0 ; i<l.size();i++) {
			for(Map<String,Object> map : list) {
				if(map.get("todoAmount") == l.get(i)) {
					listTop10.add(map);
				}
			}
		}
		if(listTop10.size()>10) {
			listTop10.subList(0, 9);
		}
		
		
		
		
		
		//if(agencyIds.size()<=10) {
		//	info.setData(list);
		//}else {
			info.setData(listTop10);
		//}
		
		info.setSuccess(true);
		info.setMessage("查询成功");
		return info;
	}
	@Override
	public List<Map<String,Object>> statsCountToDo(GeneralCondition gcon) {
		ReturnInfo info = new ReturnInfo();
		List<Integer> agencyIds = agencyService.queryAgencyList();
		List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();
		if(agencyIds.size()>0) {
			
			for(Integer agencyId : agencyIds){
				Map<String,Object> map = new HashMap<String,Object>();
				  ReturnToDoAmount toDoAmount = statsCountByAgency(agencyId,gcon);
//				  Agency agency = agencyService.queryAgencyById(agencyId);
				  Long amountByAgency = toDoAmount.getAmountByAgency();
				  map.put("todoAmount", amountByAgency);
//				  map.put("agencyName", agency.getName());
//				  map.put("agencyId", agencyId);
				  list.add(map);
			}
			
		}
		
		return list;
	}


	@Override
	public ReturnInfo changeCaseAgentUser(String caseId, String beUserId, String afUserId) {
		ReturnInfo info = new ReturnInfo();
		if (caseId == null || beUserId == null || afUserId == null) {
			info.setSuccess(false);
			info.setMessage("caseId不能为空");
			return info;
		}
		TradeMarkCase tmcase = tradeMarkCaseMapper.selectByPrimaryKey(Integer.valueOf(caseId));
        List<String> proIds = new ArrayList<String>();
        List<String> adduserIds = new ArrayList<String>();
        List<String> deluserIds = new ArrayList<String>();
        
        proIds.add(tmcase.getProcessId());
        adduserIds.add(afUserId);
        deluserIds.add(beUserId);
        
		processService.setTaskUser(proIds, adduserIds, deluserIds);		
		info.setSuccess(true);
		info.setMessage("修改案件成功");
		return info;
	}


	@Override
	public ReturnInfo addCaseAgentUser(String caseId, String userIds) {
		ReturnInfo info = new ReturnInfo();
		if (caseId == null || userIds==null) {
			info.setSuccess(false);
			info.setMessage("caseId不能为空");
			return info;
		}
		TradeMarkCase tmcase = tradeMarkCaseMapper.selectByPrimaryKey(Integer.valueOf(caseId));
        List<String> proIds = new ArrayList<String>();
        List<String> adduserIds = new ArrayList<String>();
        List<String> deluserIds = new ArrayList<String>();
        
        proIds.add(tmcase.getProcessId());
        adduserIds.addAll(Arrays.asList(userIds.split(",")));
        
		processService.setTaskUser(proIds, adduserIds, deluserIds);		
		info.setSuccess(true);
		info.setMessage("修改案件成功");
		return info;
	}


	@Override
	public ReturnInfo changeCustomerAgentUser(String customerId, String beUserId,
			String afUserId) {
		ReturnInfo info = new ReturnInfo();
		if (customerId == null) {
			info.setSuccess(false);
			info.setMessage("customerId不能为空");
			return info;
		}
		GeneralCondition gcon = new GeneralCondition();
		TradeMarkCase tmcase = new TradeMarkCase();
		tmcase.setCustId(Integer.valueOf(customerId));
		List<TradeMarkCase> tmcases = tradeMarkCaseMapper.selectByTmCase(tmcase, gcon, null,0);
        List<String> proIds = new ArrayList<String>();
        List<String> adduserIds = new ArrayList<String>();
        List<String> deluserIds = new ArrayList<String>();
        
        tmcases.forEach(n -> proIds.add(n.getProcessId()));
        adduserIds.add(afUserId);
        deluserIds.add(beUserId);
        
		processService.setTaskUser(proIds, adduserIds, deluserIds);		
		
		info.setSuccess(true);
		info.setMessage("修改案件成功");
		return info;
	}


	@Override
	public ReturnInfo addCustomerAgentUser(String customerId, String userIds) {
		ReturnInfo info = new ReturnInfo();
		if (customerId == null) {
			info.setSuccess(false);
			info.setMessage("customerId不能为空");
			return info;
		}
		GeneralCondition gcon = new GeneralCondition();
		TradeMarkCase tmcase = new TradeMarkCase();
		tmcase.setCustId(Integer.valueOf(customerId));
		List<TradeMarkCase> tmcases = tradeMarkCaseMapper.selectByTmCase(tmcase, gcon, null,0);
        List<String> proIds = new ArrayList<String>();
        List<String> adduserIds = new ArrayList<String>();
        List<String> deluserIds = new ArrayList<String>();
        
        if (tmcases!=null && tmcases.size()>0){        
	        tmcases.forEach(n -> proIds.add(n.getProcessId()));
	        adduserIds.addAll(Arrays.asList(userIds.split(",")));	        
	        info=processService.setTaskUser(proIds, adduserIds, deluserIds);
	        
	        return info;
        }
		info.setSuccess(true);
		info.setMessage("修改案件的处理人成功");
		return info;
	}

	
	
	
	public ReturnInfo createTradeMarkCase(GeneralCondition gcon, String userId, String agencyServiceId, TradeMarkCase tradeMarkCase,
			String tmNumber,String tmNumberList) {
		ReturnInfo info =tradeMarkCaseTaskManageImpl.createTradeMarkCase(gcon, userId, agencyServiceId, tradeMarkCase, tmNumber, tmNumberList);
		return info;
	
	}

	
	public ReturnInfo resetTask(GeneralCondition gcon,String appCnName){
		ReturnInfo rtnInfo = taskBasicServiceImpl.resetTask(gcon, appCnName);
		return rtnInfo;
	}

	


}
