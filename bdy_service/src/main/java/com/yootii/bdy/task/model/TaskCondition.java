package com.yootii.bdy.task.model;


import java.util.List;



public class TaskCondition {
	

	List<Integer>taskIds;
	
	List<Integer>caseIds;
	
	List<Integer>userIds;	
	
	List<Integer>custIds;
	
	List<Integer>billIds;

	public List<Integer> getBillIds() {
		return billIds;
	}

	public void setBillIds(List<Integer> billIds) {
		this.billIds = billIds;
	}

	public List<Integer> getCaseIds() {
		return caseIds;
	}

	public void setCaseIds(List<Integer> caseIds) {
		this.caseIds = caseIds;
	}

	public List<Integer> getTaskIds() {
		return taskIds;
	}

	public void setTaskIds(List<Integer> taskIds) {
		this.taskIds = taskIds;
	}

	public List<Integer> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Integer> userIds) {
		this.userIds = userIds;
	}

	public List<Integer> getCustIds() {
		return custIds;
	}

	public void setCustIds(List<Integer> custIds) {
		this.custIds = custIds;
	}
}
