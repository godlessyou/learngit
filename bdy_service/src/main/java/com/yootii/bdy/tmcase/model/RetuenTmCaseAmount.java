package com.yootii.bdy.tmcase.model;

public class RetuenTmCaseAmount {
	private Long amount;
	private Long amount1;
	private Long amount2;
	private Integer year;
	private String status;
	private String caseType;
	private Integer agencyId;
	private Integer custId;
	private Integer userId;
	private Integer deptId;
	private String agencyName;
	private String userName;
	private String customerName;
	private String deptName;
	
	
	
	public Long getAmount1() {
		return amount1;
	}
	public void setAmount1(Long amount1) {
		this.amount1 = amount1;
	}
	public Long getAmount2() {
		return amount2;
	}
	public void setAmount2(Long amount2) {
		this.amount2 = amount2;
	}
	public Integer getDeptId() {
		return deptId;
	}
	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getAgencyName() {
		return agencyName;
	}
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCaseType() {
		return caseType;
	}
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}
	public Integer getAgencyId() {
		return agencyId;
	}
	public void setAgencyId(Integer agencyId) {
		this.agencyId = agencyId;
	}
	public Integer getCustId() {
		return custId;
	}
	public void setCustId(Integer custId) {
		this.custId = custId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "RetuenTmCaseAmount [amount=" + amount + ", year=" + year + ", status=" + status + ", caseType="
				+ caseType + ", agencyId=" + agencyId + ", custId=" + custId + ", userId=" + userId + "]";
	}
	
	
}
