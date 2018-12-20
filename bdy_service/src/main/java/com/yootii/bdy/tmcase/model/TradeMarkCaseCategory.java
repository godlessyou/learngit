package com.yootii.bdy.tmcase.model;

public class TradeMarkCaseCategory {
    private Integer id;

    private Integer caseId;

    private String goodClass;

    private String similarGroup;

    private String goodCode;

    private String goodName;
    
    private String goodNameEn;

    private String goodKey;
    
    private Integer casePreId;
    
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

    public String getGoodClass() {
        return goodClass;
    }

    public void setGoodClass(String goodClass) {
        this.goodClass = goodClass == null ? null : goodClass.trim();
    }

    public String getSimilarGroup() {
        return similarGroup;
    }

    public void setSimilarGroup(String similarGroup) {
        this.similarGroup = similarGroup == null ? null : similarGroup.trim();
    }

    public String getGoodCode() {
        return goodCode;
    }

    public void setGoodCode(String goodCode) {
        this.goodCode = goodCode == null ? null : goodCode.trim();
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName == null ? null : goodName.trim();
    }

    public String getGoodNameEn() {
		return goodNameEn;
	}

	public void setGoodNameEn(String goodNameEn) {
		this.goodNameEn = goodNameEn;
	}

	public String getGoodKey() {
        return goodKey;
    }

    public void setGoodKey(String goodKey) {
        this.goodKey = goodKey == null ? null : goodKey.trim();
    }

	public Integer getCasePreId() {
		return casePreId;
	}

	public void setCasePreId(Integer casePreId) {
		this.casePreId = casePreId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((goodClass == null) ? 0 : goodClass.hashCode());
		result = prime * result
				+ ((goodCode == null) ? 0 : goodCode.hashCode());
		result = prime * result + ((goodKey == null) ? 0 : goodKey.hashCode());
		result = prime * result
				+ ((goodName == null) ? 0 : goodName.hashCode());
		result = prime * result
				+ ((goodNameEn == null) ? 0 : goodNameEn.hashCode());
		result = prime * result
				+ ((similarGroup == null) ? 0 : similarGroup.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TradeMarkCaseCategory other = (TradeMarkCaseCategory) obj;
		if (goodClass == null) {
			if (other.goodClass != null)
				return false;
		} else if (!goodClass.equals(other.goodClass))
			return false;
		if (goodCode == null) {
			if (other.goodCode != null)
				return false;
		} else if (!goodCode.equals(other.goodCode))
			return false;
		if (goodKey == null) {
			if (other.goodKey != null)
				return false;
		} else if (!goodKey.equals(other.goodKey))
			return false;
		if (goodName == null) {
			if (other.goodName != null)
				return false;
		} else if (!goodName.equals(other.goodName))
			return false;
		if (goodNameEn == null) {
			if (other.goodNameEn != null)
				return false;
		} else if (!goodNameEn.equals(other.goodNameEn))
			return false;
		if (similarGroup == null) {
			if (other.similarGroup != null)
				return false;
		} else if (!similarGroup.equals(other.similarGroup))
			return false;
		return true;
	}
    
}