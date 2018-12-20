package com.yootii.bdy.tmcase.model;

import java.util.Date;

//异议人/被异议人
public class DissentPerson {

	private String personName;       //被异议人/异议人  名称
	private String tmName;        //商标名称
	private String approvalNumber;  //审定号
	private Date approvalDate;      //审定日期
	private String personAddress;   //被异议人/异议人  地址
	private String agentName;     //代理机构名称
	private String imgFilePath;
	
	
	
	public String getImgFilePath() {
		return imgFilePath;
	}
	public void setImgFilePath(String imgFilePath) {
		this.imgFilePath = imgFilePath;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getTmName() {
		return tmName;
	}
	public void setTmName(String tmName) {
		this.tmName = tmName;
	}
	public String getApprovalNumber() {
		return approvalNumber;
	}
	public void setApprovalNumber(String approvalNumber) {
		this.approvalNumber = approvalNumber;
	}
	public Date getApprovalDate() {
		return approvalDate;
	}
	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}
	public String getPersonAddress() {
		return personAddress;
	}
	public void setPersonAddress(String personAddress) {
		this.personAddress = personAddress;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	
	
	
	
	
	
	
	
}
