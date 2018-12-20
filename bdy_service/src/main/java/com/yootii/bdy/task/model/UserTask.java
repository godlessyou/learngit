package com.yootii.bdy.task.model;

import java.util.Map;

public class UserTask {
	
	
//	private String billId;
//	private String caseId;
	
	private String taskId;
	
	private String taskName;
	
	private String remarks;
	
	
	private Map<String, Object> proMap;
	
//	public String getCaseId() {
//		return caseId;
//	}
//
//	public void setCaseId(String caseId) {
//		this.caseId = caseId;
//	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

//	public String getBillId() {
//		return billId;
//	}
//
//	public void setBillId(String billId) {
//		this.billId = billId;
//	}

	public Map<String, Object> getProMap() {
		return proMap;
	}

	public void setProMap(Map<String, Object> proMap) {
		this.proMap = proMap;
	}

	

	
	
}