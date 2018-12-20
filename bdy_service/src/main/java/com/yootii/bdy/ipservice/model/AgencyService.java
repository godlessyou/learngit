package com.yootii.bdy.ipservice.model;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AgencyService {
    private Integer agencyServiceId;

    private Integer agencyId;

    private Integer serviceId;

    private BigDecimal price;

    private BigDecimal reduction;
    
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date deadline;
    
    private String currency;
    
    private String agencyName ;
    
    private Integer chargeItemId;

    private PlatformService platformService;

    public Integer getAgencyServiceId() {
        return agencyServiceId;
    }

    public void setAgencyServiceId(Integer agencyServiceId) {
        this.agencyServiceId = agencyServiceId;
    }

    public Integer getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(Integer agencyId) {
        this.agencyId = agencyId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

	

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getReduction() {
		return reduction;
	}

	public void setReduction(BigDecimal reduction) {
		this.reduction = reduction;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public Integer getChargeItemId() {
		return chargeItemId;
	}

	public void setChargeItemId(Integer chargeItemId) {
		this.chargeItemId = chargeItemId;
	}

	public PlatformService getPlatformService() {
		return platformService;
	}

	public void setPlatformService(PlatformService platformService) {
		this.platformService = platformService;
	}

   
}