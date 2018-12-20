package com.yootii.bdy.tmcase.model;

public class IssuanceNumber {
    private Integer issuanceNumId;

    private String caseType;

    private String fileName;

    private String prefix;

    private String suffix;

    private String isOrder;

    public Integer getIssuanceNumId() {
        return issuanceNumId;
    }

    public void setIssuanceNumId(Integer issuanceNumId) {
        this.issuanceNumId = issuanceNumId;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType == null ? null : caseType.trim();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix == null ? null : prefix.trim();
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix == null ? null : suffix.trim();
    }

    public String getIsOrder() {
        return isOrder;
    }

    public void setIsOrder(String isOrder) {
        this.isOrder = isOrder == null ? null : isOrder.trim();
    }
}