package com.yootii.bdy.util;

import java.util.ArrayList;
import java.util.List;

import com.yootii.bdy.common.Globals;
import com.yootii.bdy.common.ReturnInfo;

public class TaskTool {
	
//	private static String decisionName={""};
	
	
	public static String getActivityId(String fileName){
		String activityId=null;
		if(fileName.equals("305")){ // 商标注册案件-补正通知反馈给客户后，捕获客户决定的消息
			activityId="catchCustomerDecisionMessage1";
		}
		if(fileName.equals("303") || fileName.equals("302")){// 商标注册案件-驳回通知反馈给客户后，捕获客户决定的消息
			activityId="catchCustomerDecisionMessage2";
		}
		if(fileName.equals("340")){// 商标注册案件-异议答辩通知反馈给客户后，捕获客户决定的消息
			activityId="catchCustomerDecisionMessage3";
		}
//		if(fileName.equals("307")){// 商标注册案件-不予注册答辩通知反馈给客户后，捕获客户决定的消息
//			activityId="catchCustomerDecisionMessage4";
//		}		
		if(fileName.equals("325")){// 驳回复审案件-评审意见反馈给客户后，捕获客户决定的消息
			activityId="catchCustomerDecisionMessage7";
		}		
		if(fileName.equals("342") || fileName.equals("343") 
				|| fileName.equals("344") ){// 异议答辩案件-异议质证反馈给客户后，捕获客户决定的消息			
			activityId="catchCustomerDecisionMessage7";
		}		
				
		if(fileName.equals("311") || fileName.equals("312") || fileName.equals("339")){ //参与不予注册复审通知书/撤销复审答辩通知/无效宣告答辩通知
			activityId = "catchCustomerDecisionMessage8";
		}
		if(fileName.equals("350") || fileName.equals("359")){// 驳回复审案件-裁定输，或者部分赢反馈给客户后，捕获客户决定的消息			
			activityId="catchCustomerDecisionMessage6";
		}
		if(fileName.equals("354") || fileName.equals("355") ){ //不予注册复审，裁定输，或者部分赢
			activityId = "catchCustomerDecisionMessage8";
		}
		if(fileName.equals("356") || fileName.equals("357") ){ //撤销复审，裁定输，或者部分赢
			activityId = "catchCustomerDecisionMessage8";
		}
		if(fileName.equals("313") || fileName.equals("364")){// 撤销答辩案件-裁定输，或者部分赢反馈给客户后，捕获客户决定的消息		
			activityId="catchCustomerDecisionMessage8";
		}
		if(fileName.equals("361")  || fileName.equals("362")){// 无效答辩案件-裁定输，或者部分赢反馈给客户后，捕获客户决定的消息		
			activityId="catchCustomerDecisionMessage8";
		}
		if(fileName.equals("503")  || fileName.equals("504")){// 异议答辩案件-裁定输，或者部分赢反馈给客户后，捕获客户决定的消息		
			activityId="catchCustomerDecisionMessage8";
		}
		
		
		
		return activityId;
	}
	
	
	

	public static Integer getPageId(String taskName, boolean isCustomer, boolean caseTransfered){
		Integer pageId=0;
		
		if(taskName.equals("修改案件信息")){
			pageId=1;
			if (isCustomer || caseTransfered){
				pageId=13;
			}
		}
		else if(taskName.indexOf("确认案件")>-1){
			pageId=15;
			if (isCustomer || caseTransfered){
				pageId=14;
			}
		}
		else if(taskName.equals("递交申请")){
			pageId=8;
			
		}		
		else if(taskName.indexOf("录入")>-1 && taskName.indexOf("官文")>-1){
			pageId=3;
		}
		else if(taskName.indexOf("处理")>-1 && taskName.indexOf("通知")>-1){
			pageId=4;
		}
		else if(taskName.indexOf("处理")>-1 && taskName.indexOf("决定")>-1){
			pageId=5;
		}
		else if(taskName.indexOf("案件分配")>-1){
			pageId=6;
			if (isCustomer || caseTransfered){
				pageId=13;
			}
		}
		else if(taskName.indexOf("网上递交")>-1){
			pageId=7;
		}	
		else if(taskName.indexOf("直接递交")>-1){
			pageId=8;
		}
//		else if(taskName.indexOf("修改案件")>-1){
//			pageId=9;
//		}	
		else if(taskName.indexOf("审核")>-1 && taskName.indexOf("官文")>-1){
			pageId=10;
		}	
		else if(taskName.indexOf("分配案件承办人")>-1 ){
			pageId=11;
		}
		else if(taskName.indexOf("客户")>-1 && taskName.indexOf("决定")>-1){
			pageId=12;
		}
		
		else if((taskName.indexOf("提交")>-1||taskName.indexOf("递交")>-1) && taskName.indexOf("材料")>-1){
			pageId=8;
		}
		//taskName 为递交异议申请 时 要对应的页面
		else if(taskName.indexOf("递交")>-1&&taskName.indexOf("异议")>-1){
			pageId = 8;
		}		
		else if(taskName.indexOf("错误处理")>-1){
			pageId = 21;
		}
		else if(taskName.indexOf("对方决定")>-1){
			pageId = 22;
		}
		return pageId;
		
	}
	
	
	

	public static String getPermission(String taskName) throws Exception{
		if (taskName==null){
			throw new Exception("taskName为空");
		}
		String permission=taskName;
//		if (taskName.equals("提交分割申请")) {
//				permission="提交分割申请";
//		}
//		else if (taskName.equals("提交补正材料")) {
//				permission="提交补正材料";
//		}
		if ((taskName.indexOf("提交")>-1||taskName.indexOf("递交")>-1) && taskName.indexOf("材料")>-1){
			permission="提交材料";
		}
		else if (taskName.indexOf("修改")>-1 && taskName.indexOf("案件")>-1){
			permission="审核案件";
		}
		else if (taskName.indexOf("审核")>-1 && taskName.indexOf("案件")>-1){
			permission="审核案件";
		}
		else if (taskName.indexOf("递交")>-1 && taskName.indexOf("申请")>-1){
			permission="递交申请";
		}		
		else if (taskName.indexOf("录入")>-1 && (taskName.indexOf("官文")>-1 || taskName.indexOf("通知")>-1)){
			permission="录入官文";
		}
		else if (taskName.indexOf("审核")>-1 && taskName.indexOf("官文")>-1){
			permission="审核官文";
		}
		else if (taskName.indexOf("处理")>-1 && taskName.indexOf("通知")>-1){
			permission="处理通知";
		}
		else if (taskName.indexOf("反馈")>-1 && taskName.indexOf("通知")>-1){
			permission="反馈通知";
		}
		else if (taskName.indexOf("通知客户")>-1){
			permission="处理通知";
		}
		else if (taskName.indexOf("修改案件状态")>-1){
			permission="反馈通知";
		}
		else if (taskName.indexOf("客户")>-1 && taskName.indexOf("决定")>-1){
			permission="处理决定";
		}
		else if (taskName.indexOf("代理人")>-1 && taskName.indexOf("决定")>-1){
			permission="处理决定";
		}
		else if (taskName.indexOf("处理")>-1 && taskName.indexOf("决定")>-1){
			permission="处理决定";
		}
		else if (taskName.indexOf("错误处理")>-1 ){
			permission="错误处理";
		}
		else if (taskName.indexOf("录入对方决定")>-1 ){
			permission="录入官文";
		}
		
//		else if (taskName.indexOf("客户")>-1 && taskName.indexOf("确认案件")>-1){
//			permission="处理决定";
//		}
//		else if (taskName.indexOf("生成案件清单")>-1){
//			permission="审核案件";
//		}
		return permission;
	}
	
	
	public static String getPermissionByFileName(String fileName){
		String permission="";

		if (fileName.equals("332")){//受理通知
			permission="录入官文";
		}		
		else if (fileName.equals("305")){//补正通知
			permission="处理通知";
		}
		else if (fileName.equals("306")){//不予受理通知
			permission="修改案件状态";
		}
		else if (fileName.equals("316")){//初步审定公告
			permission="录入官文";
		}
		else if (fileName.equals("303")){//部分驳回通知
			permission="处理通知";
		}
		else if (fileName.equals("302")){//驳回通知
			permission="处理通知";
		}
		else if (fileName.equals("307")){//不予注册通知
			permission="处理通知";
		}
		else if (fileName.equals("308")){//裁定-部分赢
			permission="处理通知";
		}
		else if (fileName.equals("309")){//裁定-输
			permission="处理通知";
		}
		else if (fileName.equals("310")){//裁定-赢
			permission="处理通知";
		}
		else if (fileName.equals("320")){//注册公告
			permission="处理通知";
		}
		else if (fileName.equals("340")){//异议答辩通知
			permission="处理通知";
		}		
		else if (fileName.equals("311")){//不予注册复审通知
			permission="处理通知";
		}
		else if (fileName.equals("349")){//驳回复审裁定-赢
			permission="处理通知";
		}
		else if (fileName.equals("350")){//驳回复审裁定-输
			permission="处理通知";
		}
		else if (fileName.equals("351")){//异议答辩裁定-赢
			permission="处理通知";
		}
		else if (fileName.equals("352")){//异议答辩裁定-输
			permission="处理通知";
		}
		else if (fileName.equals("353")){//不予注册复审决定-赢
			permission="处理通知";
		}
		else if (fileName.equals("354")){//不予注册复审决定-输
			permission="处理通知";
		}
		else if (fileName.equals("355")){//不予注册复审决定-部分赢
			permission="处理通知";
		}
		else if (fileName.equals("356")){//撤销复审决定-部分赢
			permission="处理通知";
		}
		else if (fileName.equals("357")){//撤销复审决定-输
			permission="处理通知";
		}
		else if (fileName.equals("358")){//撤销复审决定-赢
			permission="处理通知";
		}
		else if(fileName.equals("501") || fileName.equals("502")){ //异议申请 裁定输  
			permission = "处理通知";
		}else if(fileName.equals("502")){
			permission = "处理通知";
		}
		return permission;
	}
	
	
	//获取邮件内容
	public static List<String> getMailContent(String taskName, String caseId, Boolean result, String fileName){
		List<String> list=new ArrayList<String>();
		String mailSubject="";
		String mailContent="";
		
		if (taskName==null){
			return list;
		}
		
		if (taskName.indexOf("修改案件")>-1){
			mailSubject="案件"+caseId+"的申请材料准备情况";
			if (result!=null){
				if (result.booleanValue()==true){
					mailContent="案件"+caseId+"申请材料准备完成。";
				}
			}
		}		
		else if (taskName.indexOf("处理")>-1 && taskName.indexOf("通知")>-1){
			if (fileName!=null){
				String realFileName=getRealFileName(fileName);
				mailSubject=realFileName;
				int pos=realFileName.indexOf("通知");			
			    String eventName=realFileName.substring(0,pos);
			    mailContent="是否同意"+eventName+"？"+ "请您在待办事项中对此案件进行处理";
			}
			
			list.add(mailSubject);
			list.add(mailContent);
		}
		else if (taskName.indexOf("处理")>-1 && (taskName.indexOf("决定")>-1 || taskName.indexOf("裁定")>-1)){
			int pos=taskName.indexOf("处理");
			int pos2=taskName.indexOf("决定");
			if (pos2<0){
				pos2=taskName.indexOf("裁定");
			}			
			mailSubject="案件"+caseId+"的"+taskName.substring(pos+2);	
		    String eventName=taskName.substring(pos+2, pos2);
		    if (result!=null){
				if (result.booleanValue()==true){
					mailContent=eventName+"为赢";
				}
				else{
					mailContent=eventName+"为赢";
				}
		    }
			
			list.add(mailSubject);
			list.add(mailContent);
		}
		else if (taskName.indexOf("提交")>-1 && taskName.indexOf("材料")>-1){
			mailSubject=taskName+"完成";		   
			mailContent=mailSubject;
			list.add(mailSubject);
			list.add(mailContent);
		}
		else if (taskName.indexOf("递交")>-1 ){
			mailSubject="案件"+caseId+"的"+taskName+"完成";		   
			mailContent=mailSubject;
			list.add(mailSubject);
			list.add(mailContent);
		}

		list.add(mailSubject);			
		list.add(mailContent);
		return list;
	}
	
	
	
	//获取邮件内容
	public static String getMailSubject(String taskName){
		
		String mailSubject="";
		
		if (taskName.indexOf("审核")>-1){
			mailSubject="案件信息反馈";			
		}		
		
		
		return mailSubject;
	}
		
	
	
	
	
	
	public static Integer getCaseTypeId(String caseType){
		Integer caseTypeId=0;
		
		if(caseType.equals("商标注册")){
			caseTypeId=1;
		}
		else if(caseType.indexOf("续展")>-1){
			caseTypeId=2;
		}
		else if(caseType.indexOf("转让")>-1){
			caseTypeId=3;
		}
		else if(caseType.indexOf("变更代理人/文件接收人")>-1 ){
			caseTypeId=4;
		}
		else if(caseType.indexOf("变更名义地址集体管理规则成员名单")>-1){
			caseTypeId=5;
		}
		else if(caseType.indexOf("商标更正")>-1){
			caseTypeId=6;
		}
		else if(caseType.indexOf("商标变更")>-1){
			caseTypeId=7;
		}
		else if(caseType.indexOf("商标异议申请")>-1){
			caseTypeId=8;
		}
		else if(caseType.indexOf("商标异议答辩")>-1){
			caseTypeId=9;
		}
		else if(caseType.indexOf("商标无效宣告")>-1){
			caseTypeId=10;
		}
		else if(caseType.indexOf("商标撤三申请")>-1){
			caseTypeId=11;
		}
		else if(caseType.indexOf("商标驳回复审")>-1){
			caseTypeId=12;
		}
		else if(caseType.indexOf("商标不予注册复审")>-1){
			caseTypeId=13;
		}
		
	
		
		return caseTypeId;
		
	}
	
	
	public static String getFileType(String fileName){
		String fileType="";
		
		if (fileName.equals("332")){//受理通知
			fileType="1";
		}		
		else if (fileName.equals("305")){//补正通知
			fileType="2";
		}
		else if (fileName.equals("306")){//不予受理通知
			fileType="3";
		}
		else if (fileName.equals("316")){//初步审定公告
			fileType="4";
		}
		else if (fileName.equals("303")){//部分驳回通知
			fileType="5";
		}
		else if (fileName.equals("302")){//驳回通知
			fileType="6";
		}
		else if (fileName.equals("320")){//注册公告
			fileType="7";
		}
		else if (fileName.equals("340")){//异议答辩通知
			fileType="8";
		}
		else if (fileName.equals("348")){//部分不予注册复审通知
			fileType="9";
		}
		else if (fileName.equals("311")){//不予注册复审通知
			fileType="10";
		}
		else if (fileName.equals("349")){//驳回复审裁定-赢
			fileType="11";
		}
		else if (fileName.equals("350")){//驳回复审裁定-输
			fileType="12";
		}
		else if (fileName.equals("351")){//异议答辩裁定-赢
			fileType="13";
		}
		else if (fileName.equals("352")){//异议答辩裁定-输
			fileType="14";
		}
		else if (fileName.equals("353")){//不予注册复审决定-赢
			fileType="15";
		}
		else if (fileName.equals("354")){//不予注册复审决定-输
			fileType="16";
		}
		
		return fileType;
		
	}
	
	
	public static String getRealFileName(String fileName){
		String realFileName="";
		
		if (fileName.equals("332")){//受理通知
			realFileName="受理通知";
		}		
		else if (fileName.equals("305")){//补正通知
			realFileName="补正通知";
		}
		else if (fileName.equals("306")){//不予受理通知
			realFileName="不予受理通知";
		}
		else if (fileName.equals("316")){//初步审定公告
			realFileName="初步审定公告";
		}
		else if (fileName.equals("303")){//部分驳回通知
			realFileName="部分驳回通知";
		}
		else if (fileName.equals("302")){//驳回通知
			realFileName="驳回通知";
		}
		else if (fileName.equals("320")){//注册公告
			realFileName="注册公告";
		}
		else if (fileName.equals("340")){//异议答辩通知
			realFileName="异议答辩通知";
		}
		else if (fileName.equals("307")){//不予注册通知
			realFileName="不予注册通知";
		}
		else if (fileName.equals("311")){//不予注册复审通知
			realFileName="不予注册复审通知";
		}
		else if (fileName.equals("349")){//驳回复审裁定-赢
			realFileName="驳回复审裁定-赢";
		}
		else if (fileName.equals("350")){//驳回复审裁定-输
			realFileName="驳回复审裁定-输";
		}
		else if (fileName.equals("342")){//质证通知
			realFileName="质证通知";
		}
		else if (fileName.equals("351")){//异议答辩裁定-赢
			realFileName="异议答辩裁定-赢";
		}
		else if (fileName.equals("352")){//异议答辩裁定-输
			realFileName="异议答辩裁定-输";
		}
		else if (fileName.equals("353")){//不予注册复审决定-赢
			realFileName="不予注册复审决定-赢";
		}
		else if (fileName.equals("354")){//不予注册复审决定-输
			realFileName="不予注册复审决定-输";
		}
		else if(fileName.equals("505")){  //异议审定_赢
			realFileName = "异议审定_赢";
		}
		else if(fileName.equals("504")){
			realFileName = "异议审定_输";
		}
		else if(fileName.equals("503")){
			realFileName = "异议审定_部分赢";
		}
		return realFileName;
		
	}
	
	public static int getFileName(String caseStatus){
		int fileName = 0;
		switch (caseStatus) {
			case "录入受理通知" :
				fileName = 332;
				break;
			case "录入驳回通知":
				fileName = 302;
				break;
			case "录入不予受理通知":
				fileName = 306;
				break;
			case "录入补正通知":
				fileName = 305;
				break;
			default :
				break;
		}
		return fileName;
	}
	
	
	
	public static String getCaseStatus(String fileName, boolean refuse,  String caseResult){
		String caseStatus=null;
		
		if (fileName.equals("306")){//不予受理通知
			caseStatus="失败";
		}
		else if (fileName.equals("305")){//补正通知
			if (refuse){
				caseStatus="失败";
			}
		}
		else if (fileName.equals("332")){//受理通知
			caseStatus="受理中";
		}	
		else if (fileName.equals("303")){//部分驳回通知
			//caseStatus="不变";
		}
		else if (fileName.equals("302")){//驳回通知
			if (refuse){
				caseStatus="失败";
			}
		}
		else if (fileName.equals("349")){//驳回复审裁定-赢
			caseStatus="成功";		
		}
		else if (fileName.equals("350")){//驳回复审裁定-输
			if(caseResult==null || caseResult.equals("false")){
				caseStatus="失败";
			}else{
				caseStatus="成功";
			}
		}
		else if (fileName.equals("316")){//初步审定公告
			caseStatus="申请中";
		}	
		else if (fileName.equals("340")){//异议答辩通知
//			caseStatus="不变";
		}
		else if (fileName.equals("351")){//异议答辩裁定-赢
			caseStatus="成功";
		}
		else if (fileName.equals("352")){//异议答辩裁定-输
			if(caseResult==null || caseResult.equals("false")){
				caseStatus="失败";
			}else{
				caseStatus="成功";
			}
		}
		else if (fileName.equals("348")){//部分不予注册复审通知		
//			caseStatus="不变";			
		}
		else if (fileName.equals("307")){//不予注册通知
//			caseStatus="不变";	
		}
		else if (fileName.equals("311")){//不予注册复审通知
			if (refuse){
				caseStatus="失败";
			}
		}		
		else if (fileName.equals("353") || fileName.equals("358")){//裁定-赢
			caseStatus="成功";
		}
		else if (fileName.equals("354") || fileName.equals("355") || fileName.equals("356") || fileName.equals("357")){//裁定-输, 裁定-部分赢
			if(caseResult==null || caseResult.equals("false")){
				caseStatus="失败";
			}else{
				caseStatus="成功";
			}
		}		
		else if (fileName.equals("320")){//注册公告
			caseStatus="成功";
		}else if("504".equals(fileName)||"503".equals(fileName)){//异议审定_输
			caseStatus = "失败";
		}else if("505".equals(fileName)){  //异议审定 赢
			caseStatus = "成功";
		}
		return caseStatus;
		
	}
	
	
	
	public static String getMailType(String fileName){
		String mailType="";

		if (fileName.equals("332")){//受理通知
			mailType="sbzcsq_zcsqst_en";
		}		
		else if (fileName.equals("305")){//补正通知
			mailType="";
		}
		else if (fileName.equals("306")){//不予受理通知
			mailType="";
		}
		else if (fileName.equals("316")){//初步审定公告
			mailType="sbzcsq_csgg_en";
		}
		else if (fileName.equals("303")){//部分驳回通知
			mailType="";
		}
		else if (fileName.equals("302")){//驳回通知
			mailType="";
		}
		else if (fileName.equals("307")){//不予注册通知
			mailType="";
		}
		else if (fileName.equals("320")){//注册公告
			mailType="sbzcsq_zcgg_en";
		}
		else if (fileName.equals("340")){//异议答辩通知
			mailType="";
		}		
		else if (fileName.equals("311")){//不予注册复审通知
			mailType="";
		}
		else if (fileName.equals("349")){//驳回复审裁定-赢
			mailType="";
		}
		else if (fileName.equals("350")){//驳回复审裁定-输
			mailType="";
		}
		else if (fileName.equals("351")){//异议答辩裁定-赢
			mailType="";
		}
		else if (fileName.equals("352")){//异议答辩裁定-输
			mailType="";
		}
		else if (fileName.equals("353") || fileName.equals("354") || fileName.equals("355")){//不予注册复审决定
			mailType="";
		}
		else if (fileName.equals("356") || fileName.equals("357") || fileName.equals("358")){//撤销复审决定-输
			mailType="";
		}
	
		return mailType;
	}
	
	
	
	
	// 检查输入的userId/customerId参数
	public static ReturnInfo checkUserParam(String userId, String customerId) {
		ReturnInfo rtnInfo = new ReturnInfo();

		if ((userId == null || userId.equals(""))
				&& (customerId == null || customerId.equals(""))) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("userId与customerId不能同时为空");
			rtnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
			return rtnInfo;
		}
		if ((userId != null && !userId.equals(""))
				&& (customerId != null && !customerId.equals(""))) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("userId与customerId必须有一个为空");
			rtnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
			return rtnInfo;
		}

		rtnInfo.setSuccess(true);
		return rtnInfo;
	}
	
	

//	// 检查caseId的参数
//	public static ReturnInfo checkTaskId(String taskId) {
//		ReturnInfo rtnInfo = new ReturnInfo();
//
//		if (taskId == null || taskId.equals("")) {
//			rtnInfo.setSuccess(false);
//			rtnInfo.setMessage("taskId不能为空");
//			rtnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
//			return rtnInfo;
//		}
//
//		rtnInfo.setSuccess(true);
//		return rtnInfo;
//	}
//	

	// 检查各种Id参数
	public static ReturnInfo checkId(String name, String value) {
		ReturnInfo rtnInfo = new ReturnInfo();

		if (value == null || value.equals("")) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(name+"不能为空");
			rtnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
			return rtnInfo;
		}

		rtnInfo.setSuccess(true);
		return rtnInfo;
	}

	
	
	// 检查处理待办事项必要的参数
	public static ReturnInfo checkTaskParam(String userId, Integer tId) {
		ReturnInfo rtnInfo = new ReturnInfo();

		if (tId == null) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("待办事项的id不能为空");
			rtnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
			return rtnInfo;
		}
		if (userId == null || userId.equals("")) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("userId不能为空");
			rtnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
			return rtnInfo;
		}
		rtnInfo.setSuccess(true);
		return rtnInfo;
	}
	
	

	// 检查处理待办事项必要的参数
	public static ReturnInfo checkSubmitMode(String submitMode) {
		ReturnInfo rtnInfo = new ReturnInfo();
		if (submitMode == null || submitMode.equals("")) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("submitMode不能为空");
			rtnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
			return rtnInfo;
		}
		rtnInfo.setSuccess(true);
		return rtnInfo;
	}
	
	

	// 检查处理待办事项必要的参数
	public static ReturnInfo checkSubmitStatus(String submitStatus) {
		ReturnInfo rtnInfo = new ReturnInfo();
		if (submitStatus == null || submitStatus.equals("")) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("submitStatus不能为空");
			rtnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
			return rtnInfo;
		}
		rtnInfo.setSuccess(true);
		return rtnInfo;
	}
	
	
	// 检查录入官文的参数
	public static ReturnInfo checkFileName(String fileName) {
		ReturnInfo rtnInfo = new ReturnInfo();
		if (fileName == null || fileName.equals("")) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("fileName不能为空");
			rtnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
			return rtnInfo;
		}
		rtnInfo.setSuccess(true);
		return rtnInfo;
	}
	
	
	
	// 检查官文的审核结果
	public static ReturnInfo checkAuditResult(String auditResult) {
		ReturnInfo rtnInfo = new ReturnInfo();
		if (auditResult == null || auditResult.equals("")) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("auditResult不能为空");
			rtnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
			return rtnInfo;
		}
		rtnInfo.setSuccess(true);
		return rtnInfo;
	}
	
	
	// 检查录入的官文的类型是否正确
	public static ReturnInfo checkOfficalDoc(String taskName, String fileName) {
		ReturnInfo rtnInfo = new ReturnInfo();
		
		//如果当前taskName不是录入xxx官文，那么直接返回
		if (taskName.indexOf("录入")<0){
			rtnInfo.setSuccess(false);
			rtnInfo.setMessageType(Globals.MESSAGE_TYPE_AUTHORTY_INVALID);
			rtnInfo.setMessage("当前任务是"+ taskName+ ", 不能录入官文");
			return rtnInfo;
		}

		if (taskName.indexOf("形式审查")>-1){
			if (!fileName.equals("332") && !fileName.equals("305") && !fileName.equals("306")){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("请录入形式审查结果的官文：受理通知/补正通知/不予受理通知");
				rtnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
				return rtnInfo;
			}
		}
		else if (taskName.indexOf("实质审查")>-1){
			if (!fileName.equals("316") && !fileName.equals("303") && !fileName.equals("302")){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("请录入实质审查结果的官文：初步审定公告/驳回通知/部分驳回通知");
				rtnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
				return rtnInfo;
			}
		}
		else if (taskName.indexOf("异议期审查")>-1){
			if (!fileName.equals("320") && !fileName.equals("340") && !fileName.equals("307")){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("请录入异议期审查结果的官文：注册公告/异议答辩通知/不予注册通知");
				rtnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
				return rtnInfo;
			}
		}
		rtnInfo.setSuccess(true);
		return rtnInfo;
	}
		
	
	

	public static Integer  getFileNameByCaseType(int caseTypeId){
		
		Integer fileName=201;
		
		switch(caseTypeId) {
			case 1:
				fileName=202;
				break;
			case 2:
				fileName=203;
				break;
			case 3:
				fileName=204;
				break;
			case 4:
				fileName=206;
				break;
			case 5:
				fileName=207;
				break;
			case 6:
				fileName=205;
				break;
			case 8:
				
				break;
			case 9:
				
				break;
			case 10:
				
				break;
			case 11:
				
				break;
			case 12:
				fileName=210;
				break;
			case 13:
				
				break;
		}
		
		return fileName;
		
	}
	
	
	public static Integer getDataSynType(String fileName){
		Integer type=null;
		
		if (fileName.equals("332")){//受理通知
			type=4;
		}		
		else if (fileName.equals("305")){//补正通知
			type=3;
		}
		else if (fileName.equals("306")){//不予受理通知
			
		}
		else if (fileName.equals("316")){//初步审定公告
			type=5;
		}
		else if (fileName.equals("303")){//部分驳回通知
			
		}
		else if (fileName.equals("302")){//驳回通知
			
		}
		else if (fileName.equals("320")){//注册公告
			type=6;
		}
		else if (fileName.equals("340")){//异议答辩通知
			
		}
		else if (fileName.equals("307")){//不予注册通知
			
		}
		else if (fileName.equals("311")){//不予注册复审通知
			
		}
		else if (fileName.equals("349")){//驳回复审裁定-赢
			
		}
		else if (fileName.equals("350")){//驳回复审裁定-输
			
		}
		else if (fileName.equals("351")){//异议答辩裁定-赢
			
		}
		else if (fileName.equals("352")){//异议答辩裁定-输
			
		}
		else if (fileName.equals("353")){//不予注册复审决定-赢
			
		}
		else if (fileName.equals("354")){//不予注册复审决定-输
			
		}
		
		return type;
		
	}
	
	
	public static Integer getDataSynTypeByCaseType(Integer caseTypeId){
		Integer type=null;
		
		if (caseTypeId==1){
			type=2;
		}else if (caseTypeId==2){
			type=7;
		}else if (caseTypeId==3){
			type=8;
		}else if (caseTypeId==5){
			type=9;
		}else if (caseTypeId==4){
			type=10;
		}else if (caseTypeId==6){
			type=11;
		}
		
		return type;
		
	}
	
	

	public static String getProcessKey(Integer caseTypeId){
		String processkey=null;
		
		switch(caseTypeId){

			case 8://商标异议申请				
				processkey="trademark_complexcase_start";
				break;
			case 9://商标异议答辩				
				processkey="trademark_defense_start";
				break;		
			case 10://商标无效宣告				
				processkey="trademark_complexcase_start";
				break;
			case 11://商标撤三申请				
				processkey="trademark_complexcase_start";
				break;
			case 12://商标驳回复审				
				processkey="trademark_rejectRetrial_start";
				break;
			case 13://商标不予注册复审						
				processkey="trademark_fushencase_start";
				break;
			case 14://商标诉讼案件				
//				processkey="";
				break;		
			case 15://商标撤三答辩
				processkey="trademark_defense_start";
				break;
			case 18://撤销商标复审				
				processkey="trademark_fushencase_start";
				break;		
			case 19://撤销复审答辩				
				processkey="trademark_fushencase_start";
				break;
			case 20://参与不予注册复审
				processkey="trademark_noregister_defense_start";
				break;			
			case 22://商标无效宣告答辩
				processkey="trademark_fushencase_start";
				break;
			default:			
				
				break;
		}
		
		return processkey;
	}
	
	
	



}
