package com.yootii.bdy.tmcase.model;

public class TradeMarkCaseCategoryDetil {
    private Integer id;

    private Integer agencyId;

    private Integer caseId;

    private String goodClass;

    private String similarGroup;

    private String goodCode;

    private String goodName;

    private String goodKey;
    
    private String goodEnName;
   
    private String goodNotes;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(Integer agencyId) {
		this.agencyId = agencyId;
	}

	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public String getGoodClass() {
		return goodClass;
	}

	public void setGoodClass(String goodClass) {
		this.goodClass = goodClass;
	}

	public String getSimilarGroup() {
		return similarGroup;
	}

	public void setSimilarGroup(String similarGroup) {
		this.similarGroup = similarGroup;
	}

	public String getGoodCode() {
		return goodCode;
	}

	public void setGoodCode(String goodCode) {
		this.goodCode = goodCode;
	}

	public String getGoodName() {
		return goodName;
	}

	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}

	public String getGoodKey() {
		return goodKey;
	}

	public void setGoodKey(String goodKey) {
		this.goodKey = goodKey;
	}

	public String getGoodEnName() {
		return goodEnName;
	}

	public void setGoodEnName(String goodEnName) {
		this.goodEnName = goodEnName;
	}

	public String getGoodNotes() {
		return goodNotes;
	}

	public void setGoodNotes(String goodNotes) {
		this.goodNotes = goodNotes;
	}

	@Override
	public String toString() {
		return "TradeMarkCaseCategoryDetil [id=" + id + ", agencyId=" + agencyId + ", caseId=" + caseId + ", goodClass="
				+ goodClass + ", similarGroup=" + similarGroup + ", goodCode=" + goodCode + ", goodName=" + goodName
				+ ", goodKey=" + goodKey + ", goodEnName=" + goodEnName + ", goodNotes=" + goodNotes + "]";
	}
    
	
    
   
}