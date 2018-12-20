package com.yootii.bdy.tmcase.model;

public class TradeMarkAssociation {
    private Integer id;

    private Integer caseId;

    private Integer relatedCaseId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public Integer getRelatedCaseId() {
        return relatedCaseId;
    }

    public void setRelatedCaseId(Integer relatedCaseId) {
        this.relatedCaseId = relatedCaseId;
    }
}