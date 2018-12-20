package com.yootii.bdy.bill.model;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ChargeRecord {
    private Integer chargeRecordId;
    
    private Integer agencyServiceId;

    private Integer agencyId;

    private Integer caseId;

    private Integer chargeItemId;

    private String descChn;

    private String descEng;

    private BigDecimal price;

    private Integer number;

    private BigDecimal amount;

    private String creater;
    
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    private Integer status;
    
    private String verifyPerson;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date verifyDate; 
    
    private String chargeType;//增加价目表的收费类型
    
    private String createrFullname;//创建人的全称
    
    private String verifyFullname;//核销人全称
    
    private String currency;//币种

    public Integer getChargeRecordId() {
        return chargeRecordId;
    }

    public void setChargeRecordId(Integer chargeRecordId) {
        this.chargeRecordId = chargeRecordId;
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

    public Integer getChargeItemId() {
		return chargeItemId;
	}

	public void setChargeItemId(Integer chargeItemId) {
		this.chargeItemId = chargeItemId;
	}

	public String getDescChn() {
        return descChn;
    }

    public void setDescChn(String descChn) {
        this.descChn = descChn == null ? null : descChn.trim();
    }

    public String getDescEng() {
        return descEng;
    }

    public void setDescEng(String descEng) {
        this.descEng = descEng == null ? null : descEng.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater == null ? null : creater.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

	public String getVerifyPerson() {
		return verifyPerson;
	}

	public void setVerifyPerson(String verifyPerson) {
		this.verifyPerson = verifyPerson;
	}

	public Date getVerifyDate() {
		return verifyDate;
	}

	public void setVerifyDate(Date verifyDate) {
		this.verifyDate = verifyDate;
	}

	public String getChargeType() {
		return chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public String getCreaterFullname() {
		return createrFullname;
	}

	public void setCreaterFullname(String createrFullname) {
		this.createrFullname = createrFullname;
	}

	public String getVerifyFullname() {
		return verifyFullname;
	}

	public void setVerifyFullname(String verifyFullname) {
		this.verifyFullname = verifyFullname;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Integer getAgencyServiceId() {
		return agencyServiceId;
	}

	public void setAgencyServiceId(Integer agencyServiceId) {
		this.agencyServiceId = agencyServiceId;
	}

    
}