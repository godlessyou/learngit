package com.yootii.bdy.task.model;



import com.fasterxml.jackson.annotation.JsonIgnore;

import com.yootii.bdy.tmcase.model.TradeMarkCase;


public class ToDolistetail extends TradeMarkCase {
	
  //------- 待办事项属性
	private String casefrom;// 案件来源	

    private Integer taskId;//任务Id
    
    private String taskName;//待办事项的主题   
	
    private String remarks;//案件备注
    
    private String caseStatus;//案件最新状态    
    
    private String failReason;//失败原因
          
    
    public String getCasefrom() {
		return casefrom;
	}
	public void setCasefrom(String casefrom) {
		this.casefrom = casefrom;
	}
	
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getCaseStatus() {
		return caseStatus;
	}
	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}
	public String getFailReason() {
		return failReason;
	}
	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}	
		
	
	
}