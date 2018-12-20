package com.yootii.bdy.task.model;

import java.util.List;

public class ReturnToDoAmount {
	
	private Long amountByCust;
	private Long amountByAgency;
	private List<Integer> custId;
	private List<Integer> userId;
	public Long getAmountByCust() {
		return amountByCust;
	}
	public void setAmountByCust(Long amountByCust) {
		this.amountByCust = amountByCust;
	}
	public Long getAmountByAgency() {
		return amountByAgency;
	}
	public void setAmountByAgency(Long amountByAgency) {
		this.amountByAgency = amountByAgency;
	}
	public List<Integer> getCustId() {
		return custId;
	}
	public void setCustId(List<Integer> custId) {
		this.custId = custId;
	}
	public List<Integer> getUserId() {
		return userId;
	}
	public void setUserId(List<Integer> userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "ReturnToDoAmount [amountByCust=" + amountByCust + ", amountByAgency=" + amountByAgency + ", custId="
				+ custId + ", userId=" + userId + "]";
	}
	
	
}
