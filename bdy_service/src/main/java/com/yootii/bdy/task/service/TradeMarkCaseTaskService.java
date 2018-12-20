package com.yootii.bdy.task.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.customer.model.Customer;
import com.yootii.bdy.model.User;
import com.yootii.bdy.task.model.TaskRecord;
import com.yootii.bdy.task.model.TmCaseTaskToDoList;
import com.yootii.bdy.task.model.TaskCondition;
import com.yootii.bdy.task.model.ToDoList;
import com.yootii.bdy.tmcase.model.TradeMarkCase;

public interface TradeMarkCaseTaskService {
	
	public ReturnInfo createTaskRecord(TaskRecord taskRecord);
	
	public ReturnInfo modifyTaskRecord(TaskRecord taskRecord);
	
	// 自动录入官文接口
	public ReturnInfo autoSaveGuanWen(String fileName, TradeMarkCase tradeMarkCase, String taskId);
	
	// 查询待办事项总数的接口
	public Long queryToDoListCount(GeneralCondition gcon, String userId, String customerId, Integer agencyId) throws Exception;
	
	//---- 客户与代理机构公用接口  -------		

	//查看当前案件的待办事项接口
	public ReturnInfo queryCaseDaiban(String userId, String customerId, Integer pageId, TradeMarkCase tmcase, ToDoList toDoList);
	
	//查看待办事项列表接口
	public ReturnInfo queryToDoList(GeneralCondition gcon, String userId, String customerId, Integer pageId,TradeMarkCase tmcase);

	//待办事项详情接口
	public ReturnInfo toDoListDetail(GeneralCondition gcon,  String customerId, String userId, String taskId, TmCaseTaskToDoList toDoList);
		
	//拒绝接口
	public ReturnInfo refuse(GeneralCondition gcon, String userId, String customerId, TmCaseTaskToDoList toDoList, Map<String, Object> proMap);

	//提交案件接口
	public ReturnInfo submitCase(GeneralCondition  gcon, String userId, String customerId, TmCaseTaskToDoList toDoList);

	//记录案件的交流信息接口
	public ReturnInfo queryMailRecord(TmCaseTaskToDoList toDoList);
		
	
	//---- 客户使用的接口 -------	
	//启动商标变更流程
	public ReturnInfo startTmChangeProcess(Map<String, Object> map);
	
	//启动商标注册流程
	public ReturnInfo startTmRegisterProcess(Map<String, Object> map);
	
	//启动商标的异议申请/商标撤三申请等复杂案件申请的流程
	public ReturnInfo startComplexCaseApp(Map<String, Object>map);
	
	//修改案件接口
	public ReturnInfo modifyCase(Map<String, Object> map, Map<String, Object> proMap);
		
	//同意接口
	public ReturnInfo agree(GeneralCondition gcon, String customerId, TmCaseTaskToDoList toDoList);	
	
		//关闭案件接口
	public ReturnInfo closeCase(GeneralCondition gcon, String customerId, TmCaseTaskToDoList toDoList);
	
	//-------代理人变更系列接口--------
	//变更案件代理人接口
	public ReturnInfo changeCaseAgentUser(String caseId, String beUserId, String afUserId);
	
	//添加案件代理人接口	
	public ReturnInfo addCaseAgentUser(String caseId, String userIds);
		
	//变更案件代理人接口
	public ReturnInfo changeCustomerAgentUser(String customerId,String beUserId, String afUserId);
	
	//添加案件代理人接口	
	public ReturnInfo addCustomerAgentUser(String customerId, String userIds);
			
		
		
	//---- 为商标流程引擎中配置的servicetask所调用的接口 -------	
	//转发案件接口
	public ReturnInfo transferCase(GeneralCondition gcon, String userId, String processInstanceId, String agencyLevel, String agencyServiceId, TradeMarkCase tmCase);
		
	// 报告网申结果消息的接口
	public ReturnInfo notifyAppResult(GeneralCondition gcon, String submitStatus, String processInstanceId);
		
	// 报告拒绝处理的消息的接口
	public ReturnInfo notifyRefuseCase(GeneralCondition gcon, String userId, String processInstanceId, String agencyLevel);
	
	// 报告官方通知的消息的接口
	public ReturnInfo notifyOfficialNotice(GeneralCondition gcon, String userId, String custId, String processInstanceId,String lastAgencyLevel, String fileName, String lastProcessInstanceId);
	
	// 报告客户决定的消息的接口
	public ReturnInfo notifyCustomerDecision(GeneralCondition gcon, String userId, String processInstanceId,String agencyLevel, String lastAgencyLevel, String customerDecision,String fileName);
	
	// 报告案件结果的消息的接口
	public ReturnInfo notifyCaseResult(GeneralCondition gcon, String userId, String processInstanceId,String agencyLevel);
	
	//处理网申状态
	public ReturnInfo resetTask(GeneralCondition gcon,String appCnName);
	
	
	

	
	//报告子案件结果的消息的接口
	//例如：驳回复审案件，异议答辩案件，不予注册复审案件作为商标注册案件的子案件，
	//需要将这些案件的结果返回给商标注册案件，因为商标注册案件的流程中停留在等待这些案件结果的消息上
	//只有接收到这些案件的结果，才能驱动商标注册案件流程往下执行。
	public ReturnInfo notifyChildCaseResult(GeneralCondition gcon, String caseId, String processInstanceId);

	
	// 创建驳回复审案件的接口
	public ReturnInfo createRejectCase(GeneralCondition gcon, String userId, String processInstanceId, TradeMarkCase tmCase);
	
	
	// 创建异议答辩案件的接口
	public ReturnInfo createObjectionDefenseCase(GeneralCondition gcon, String userId, String processInstanceId, TradeMarkCase tmCase);

	
	// 创建不予注册复审案件的接口
	public ReturnInfo createNewCase(GeneralCondition gcon, String userId, String processInstanceId, TradeMarkCase tmCase);

	
	// 创建商标诉讼案件的接口
	public ReturnInfo createLawsuitCase(GeneralCondition gcon, String userId, String processInstanceId, TradeMarkCase tmCase);
			
		
	// 修改案件状态的接口
	public ReturnInfo modifyCaseStatus(GeneralCondition gcon, String userId, String caseId, String fileName, String refuse, String caseResult, String processInstanceId);	

	
	
	
	//---- 代理机构使用的接口 -------
	
	//处理案件，几种可能的处理方式：转发案件，接收案件，拒绝案件
	public ReturnInfo assginCase(GeneralCondition gcon, String userId,
				String agencyId, String transfer, TmCaseTaskToDoList toDoList, Map<String, Object> proMap);
	
	//审核不通过接口
	public ReturnInfo notAudited(GeneralCondition gcon, String userId, TmCaseTaskToDoList toDoList);
	
	//审核通过接口
	public ReturnInfo audited(GeneralCondition gcon, String userId, String submitMode, TmCaseTaskToDoList toDoList,Boolean send, Map<String, Object> proMap);
	
	//网上申请接口	
	public ReturnInfo submitApp(GeneralCondition gcon, String userId, String custId, String caseId, String processInstanceId);
	
	//直接递交接口	
	public ReturnInfo appOffLine(GeneralCondition gcon,  String userId, String submitStatus, TmCaseTaskToDoList toDoList, Map<String, Object> proMap);
		
	//设置网上申请结果接口	
	public ReturnInfo setAppOnLineResult(GeneralCondition gcon,  String userId, String submitStatus, TmCaseTaskToDoList toDoList);
	
	//官文录入接口
	public ReturnInfo officalDoc(GeneralCondition gcon,  String userId, String fileName, TradeMarkCase tradeMarkCase, TmCaseTaskToDoList toDoList);
	
	//审核官文接口
	public ReturnInfo auditOfficalDoc(GeneralCondition gcon, String userId, String auditResult,
			TmCaseTaskToDoList toDoList, Map<String, Object> proMap);	
	
	//处理官文接口
	public ReturnInfo processDoc(GeneralCondition gcon,  String userId, String approved, TmCaseTaskToDoList toDoList);
		
	//向客户报告接口
	public ReturnInfo feedback(GeneralCondition gcon,  String userId, TmCaseTaskToDoList toDoList);
	
	//处理客户决定接口	
	public ReturnInfo processDecision(GeneralCondition gcon,  String userId, TmCaseTaskToDoList toDoList);

	//
	public ReturnInfo oppositeDecision(GeneralCondition gcon,String userId,TmCaseTaskToDoList toDoList);
	
	public ReturnInfo statsByAgency(Integer agencyId, GeneralCondition gcon);

	public ReturnInfo statsToDoTop10(GeneralCondition gcon);
	
	public List<Map<String,Object>> statsCountToDo(GeneralCondition gcon);

	
	
	//错误处理接口	
	public ReturnInfo runAgain(GeneralCondition gcon,  String userId, TmCaseTaskToDoList toDoList);
		
    //创建和启动案件流程的接口
	public ReturnInfo createTradeMarkCase(GeneralCondition gcon, String userId, String agencyServiceId, TradeMarkCase tradeMarkCase,
			String tmNumber,String tmNumberList) ;
	

}
