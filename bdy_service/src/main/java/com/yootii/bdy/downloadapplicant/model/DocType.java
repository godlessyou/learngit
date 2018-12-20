package com.yootii.bdy.downloadapplicant.model;

public class DocType {
    private Integer docTypeId;

    private String docTypeName;
    
    private Integer agencyId;

    public Integer getDocTypeId() {
        return docTypeId;
    }

    public void setDocTypeId(Integer docTypeId) {
        this.docTypeId = docTypeId;
    }

    public String getDocTypeName() {
        return docTypeName;
    }

    public void setDocTypeName(String docTypeName) {
        this.docTypeName = docTypeName == null ? null : docTypeName.trim();
    }

	public Integer getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(Integer agencyId) {
		this.agencyId = agencyId;
	}
    
}