package com.yootii.bdy.task.model;

import java.math.BigDecimal;
import java.util.Date;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yootii.bdy.bill.model.PayAcount;

public class BillToDoList extends ToDoList {

	private Integer PageId;

	@JsonIgnore
	private Integer id;

	private String billNo;

	private Integer billId;

	private Integer agencyId;
	
	private Integer custId;

	private String billTitle;

	private String groupName;

	private BigDecimal sum;

	private BigDecimal foreignSum;
	
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date createDate;

	private String creater;
	
	private String createrFullname;

	private String status;

	private String custname;

	private String description;

	private BigDecimal discount;

	private String currency;

	private BigDecimal exchangeRate;

	private BigDecimal serviceCost;

	private BigDecimal applyCost;

	private BigDecimal otherCost;
	
	private String memo;

	private Integer contactId;

    private Integer payAcountId;
    
    private PayAcount payAcount;
    
    private Integer coagencyId;
    
    private Integer receiverType;//1客户，2合作代理机构
    
    private String coagencyName; //合作代理机构名称
    
   

	private String customer;//客户名称
    
    private String agencyName;//代理机构名称

    
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
		this.currency = currency;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public Integer getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(Integer agencyId) {
		this.agencyId = agencyId;
	}

	public String getBillTitle() {
		return billTitle;
	}

	public void setBillTitle(String billTitle) {
		this.billTitle = billTitle;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public BigDecimal getSum() {
		return sum;
	}

	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}

	public BigDecimal getForeignSum() {
		return foreignSum;
	}

	public void setForeignSum(BigDecimal foreignSum) {
		this.foreignSum = foreignSum;
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
		this.creater = creater;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getPageId() {
		return PageId;
	}

	public void setPageId(Integer pageId) {
		PageId = pageId;
	}

	public String getCustname() {
		return custname;
	}

	public void setCustname(String custname) {
		this.custname = custname;
	}
	

    public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

	public PayAcount getPayAcount() {
		return payAcount;
	}

	public void setPayAcount(PayAcount payAcount) {
		this.payAcount = payAcount;
	}

	public Integer getCoagencyId() {
		return coagencyId;
	}

	public void setCoagencyId(Integer coagencyId) {
		this.coagencyId = coagencyId;
	}

	public Integer getReceiverType() {
		return receiverType;
	}

	public void setReceiverType(Integer receiverType) {
		this.receiverType = receiverType;
	}

	public String getCreaterFullname() {
		return createrFullname;
	}

	public void setCreaterFullname(String createrFullname) {
		this.createrFullname = createrFullname;
	}

	
	 public String getCoagencyName() {
			return coagencyName;
		}

		public void setCoagencyName(String coagencyName) {
			this.coagencyName = coagencyName;
		}

		public String getCustomer() {
			return customer;
		}

		public void setCustomer(String customer) {
			this.customer = customer;
		}

		public String getAgencyName() {
			return agencyName;
		}

		public void setAgencyName(String agencyName) {
			this.agencyName = agencyName;
		}

		public Integer getCustId() {
			return custId;
		}

		public void setCustId(Integer custId) {
			this.custId = custId;
		}
}