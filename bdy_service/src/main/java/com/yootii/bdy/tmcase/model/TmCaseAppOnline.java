package com.yootii.bdy.tmcase.model;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;


public class TmCaseAppOnline {
    private Integer caseId;  // 案件编号

    private Integer custId;//客户编号
    
	private Integer agencyId;//代理机构编号

    private String caseType;//案件类型    
   
    private String caseStatus;//案件最新状态
    
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date appDate;//申请日
   
	private String agentNum;//代理文号
	
	private String appCnName;//申请人名称
	
	private String appEnName;//申请人英文名称	
	
	private String goodClasses;//所有类别	
	
	private String imageFile;//图样
	
	
	public Integer getCaseId() {
		return caseId;
	}
	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}
	public String getCaseStatus() {
		return caseStatus;
	}
	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}
	public Date getAppDate() {
		return appDate;
	}
	public void setAppDate(Date appDate) {
		this.appDate = appDate;
	}

	public Integer getCustId() {
		return custId;
	}
	public void setCustId(Integer custId) {
		this.custId = custId;
	}
	public Integer getAgencyId() {
		return agencyId;
	}
	public void setAgencyId(Integer agencyId) {
		this.agencyId = agencyId;
	}
	public String getCaseType() {
		return caseType;
	}
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}
	
	
	public String getAgentNum() {
		return agentNum;
	}
	public void setAgentNum(String agentNum) {
		this.agentNum = agentNum;
	}	
	public String getAppCnName() {
		return appCnName;
	}
	public void setAppCnName(String appCnName) {
		this.appCnName = appCnName;
	}
	public String getAppEnName() {
		return appEnName;
	}
	public void setAppEnName(String appEnName) {
		this.appEnName = appEnName;
	}
	public String getGoodClasses() {
		return goodClasses;
	}
	public void setGoodClasses(String goodClasses) {
		this.goodClasses = goodClasses;
	}
	
	public String getImageFile() {
		return imageFile;
	}
	public void setImageFile(String imageFile) {
		this.imageFile = imageFile;
	}
	
	
	
}