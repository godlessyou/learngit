package com.yootii.bdy.tmcase.model;

import java.util.Date;

public class TradeMarkCaseGuanWen {
    private Integer caseId;

    private String agentNum;

    private String appCnName;

    private String appEnName;

    private String tmName;

    private String goodClasses;

    private String appNumber;

    private String regNumber;

    private Date appDate;

    private Date regDate;

    private Date validStartDate;

    private Date validEndDate;

    private Date objectionDate;

    private String approvalNumber;

    private Date approvalDate;

    private String regNoticeNumber;

    private String imagePath;

    private Integer similarity;

    private Date modifyDate;
    
    private String docDate;
    //原始的官文所在路径
    private String docPath;
        
    //拷贝后的官文所在路径
    private String docFile;

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public String getAgentNum() {
        return agentNum;
    }

    public void setAgentNum(String agentNum) {
        this.agentNum = agentNum == null ? null : agentNum.trim();
    }

    public String getAppCnName() {
        return appCnName;
    }

    public void setAppCnName(String appCnName) {
        this.appCnName = appCnName == null ? null : appCnName.trim();
    }

    public String getAppEnName() {
        return appEnName;
    }

    public void setAppEnName(String appEnName) {
        this.appEnName = appEnName == null ? null : appEnName.trim();
    }

    public String getTmName() {
        return tmName;
    }

    public void setTmName(String tmName) {
        this.tmName = tmName == null ? null : tmName.trim();
    }

    public String getGoodClasses() {
        return goodClasses;
    }

    public void setGoodClasses(String goodClasses) {
        this.goodClasses = goodClasses == null ? null : goodClasses.trim();
    }

    public String getAppNumber() {
        return appNumber;
    }

    public void setAppNumber(String appNumber) {
        this.appNumber = appNumber == null ? null : appNumber.trim();
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber == null ? null : regNumber.trim();
    }

    public Date getAppDate() {
        return appDate;
    }

    public void setAppDate(Date appDate) {
        this.appDate = appDate;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public Date getValidStartDate() {
        return validStartDate;
    }

    public void setValidStartDate(Date validStartDate) {
        this.validStartDate = validStartDate;
    }

    public Date getValidEndDate() {
        return validEndDate;
    }

    public void setValidEndDate(Date validEndDate) {
        this.validEndDate = validEndDate;
    }

    public Date getObjectionDate() {
        return objectionDate;
    }

    public void setObjectionDate(Date objectionDate) {
        this.objectionDate = objectionDate;
    }

    public String getApprovalNumber() {
        return approvalNumber;
    }

    public void setApprovalNumber(String approvalNumber) {
        this.approvalNumber = approvalNumber == null ? null : approvalNumber.trim();
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getRegNoticeNumber() {
        return regNoticeNumber;
    }

    public void setRegNoticeNumber(String regNoticeNumber) {
        this.regNoticeNumber = regNoticeNumber == null ? null : regNoticeNumber.trim();
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath == null ? null : imagePath.trim();
    }

    public Integer getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Integer similarity) {
        this.similarity = similarity;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

	public String getDocDate() {
		return docDate;
	}

	public void setDocDate(String docDate) {
		this.docDate = docDate;
	}

	public String getDocPath() {
		return docPath;
	}

	public void setDocPath(String docPath) {
		this.docPath = docPath;
	}

	public String getDocFile() {
		return docFile;
	}

	public void setDocFile(String docFile) {
		this.docFile = docFile;
	}
}