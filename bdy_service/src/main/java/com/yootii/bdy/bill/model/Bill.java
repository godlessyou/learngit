package com.yootii.bdy.bill.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yootii.bdy.model.User;
import com.yootii.bdy.tmcase.model.CaseInfo;

public class Bill {
    private Integer billId;

    private String billType;

    private String status;

    private Integer custId;
    
    private Integer agencyId;
    
    private String customer;//客户名称
    
    private String agencyName;//代理机构名称

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;

    private String groupName;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createDate;

    private String creater;
    
    private String createrFullname;

    private BigDecimal discount;

    private String currency;

    private BigDecimal exchangeRate;

    private BigDecimal sum;
    
    private BigDecimal foreignSum;

    private BigDecimal serviceCost;

    private BigDecimal applyCost;

    private BigDecimal otherCost;

    private String memo;

    private String billTitle;

    private Integer contactId;

    private Integer payAcountId;   
    
    @JsonIgnore
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createDateStart;
    
    @JsonIgnore
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createDateEnd;
    
    private CustomerContact contact;
    
    private PayAcount payAcount;
    
    private List<ChargeRecord> chargeRecords;
    
    private String billNo;
    
    private Integer coagencyId;
    
    private String coagencyName;
    
    private Integer contactUserId;
    
    private Integer receiverType;//1客户，2合作代理机构
    
    private User contactUser;
    
    //构成当前账单的案件列表   
    private List<CaseInfo> caseList;
    
    private String caseIds;
    
    
    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

	public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType == null ? null : billType.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater == null ? null : creater.trim();
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public BigDecimal getServiceCost() {
        return serviceCost;
    }

    public void setServiceCost(BigDecimal serviceCost) {
        this.serviceCost = serviceCost;
    }

    public BigDecimal getApplyCost() {
        return applyCost;
    }

    public void setApplyCost(BigDecimal applyCost) {
        this.applyCost = applyCost;
    }

    public BigDecimal getOtherCost() {
        return otherCost;
    }

    public void setOtherCost(BigDecimal otherCost) {
        this.otherCost = otherCost;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public String getBillTitle() {
        return billTitle;
    }

    public void setBillTitle(String billTitle) {
        this.billTitle = billTitle == null ? null : billTitle.trim();
    }

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public Integer getPayAcountId() {
        return payAcountId;
    }

    public void setPayAcountId(Integer payAcountId) {
        this.payAcountId = payAcountId;
    }

	public BigDecimal getForeignSum() {
		return foreignSum;
	}

	public void setForeignSum(BigDecimal foreignSum) {
		this.foreignSum = foreignSum;
	}

	public String getCreaterFullname() {
		return createrFullname;
	}

	public void setCreaterFullname(String createrFullname) {
		this.createrFullname = createrFullname;
	}

	public Date getCreateDateStart() {
		return createDateStart;
	}

	public void setCreateDateStart(Date createDateStart) {
		this.createDateStart = createDateStart;
	}

	public Date getCreateDateEnd() {
		return createDateEnd;
	}

	public void setCreateDateEnd(Date createDateEnd) {
		this.createDateEnd = createDateEnd;
	}

	public CustomerContact getContact() {
		return contact;
	}

	public void setContact(CustomerContact contact) {
		this.contact = contact;
	}

	public PayAcount getPayAcount() {
		return payAcount;
	}

	public void setPayAcount(PayAcount payAcount) {
		this.payAcount = payAcount;
	}

	public Integer getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(Integer agencyId) {
		this.agencyId = agencyId;
	}

	public List<ChargeRecord> getChargeRecords() {
		return chargeRecords;
	}

	public void setChargeRecords(List<ChargeRecord> chargeRecords) {
		this.chargeRecords = chargeRecords;
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Integer getCoagencyId() {
		return coagencyId;
	}

	public void setCoagencyId(Integer coagencyId) {
		this.coagencyId = coagencyId;
	}

	public Integer getContactUserId() {
		return contactUserId;
	}

	public void setContactUserId(Integer contactUserId) {
		this.contactUserId = contactUserId;
	}

	public Integer getReceiverType() {
		return receiverType;
	}

	public void setReceiverType(Integer receiverType) {
		this.receiverType = receiverType;
	}

	public String getCoagencyName() {
		return coagencyName;
	}

	public void setCoagencyName(String coagencyName) {
		this.coagencyName = coagencyName;
	}

	public User getContactUser() {
		return contactUser;
	}

	public void setContactUser(User contactUser) {
		this.contactUser = contactUser;
	}

	@Override
	public String toString() {
		return "Bill [billId=" + billId + ", billType=" + billType + ", status=" + status + ", custId=" + custId
				+ ", agencyId=" + agencyId + ", customer=" + customer + ", agencyName=" + agencyName + ", startDate="
				+ startDate + ", endDate=" + endDate + ", groupName=" + groupName + ", description=" + description
				+ ", createDate=" + createDate + ", creater=" + creater + ", createrFullname=" + createrFullname
				+ ", discount=" + discount + ", currency=" + currency + ", exchangeRate=" + exchangeRate + ", sum="
				+ sum + ", foreignSum=" + foreignSum + ", serviceCost=" + serviceCost + ", applyCost=" + applyCost
				+ ", otherCost=" + otherCost + ", memo=" + memo + ", billTitle=" + billTitle + ", contactId="
				+ contactId + ", payAcountId=" + payAcountId + ", createDateStart=" + createDateStart
				+ ", createDateEnd=" + createDateEnd + ", contact=" + contact + ", payAcount=" + payAcount
				+ ", chargeRecords=" + chargeRecords + ", billNo=" + billNo + ", coagencyId=" + coagencyId
				+ ", coagencyName=" + coagencyName + ", contactUserId=" + contactUserId + ", receiverType="
				+ receiverType + ", contactUser=" + contactUser + "]";
	}


	public List<CaseInfo> getCaseList() {
		return caseList;
	}

	public void setCaseList(List<CaseInfo> caseList) {
		this.caseList = caseList;
	}

	public String getCaseIds() {
		return caseIds;
	}

	public void setCaseIds(String caseIds) {
		this.caseIds = caseIds;
	}

	
	
	
}