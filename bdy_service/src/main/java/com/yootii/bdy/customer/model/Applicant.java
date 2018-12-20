package com.yootii.bdy.customer.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yootii.bdy.material.model.Material;
import com.yootii.bdy.trademark.model.Trademark;

public class Applicant {
    private Integer id;

    private String applicantName;

    private String applicantAddress;

    private String applicantEnName;

    private String applicantEnAddress;

    private String usertName;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date modifyTime;
    
    private String country;
    
    private String appType;
    
    private String sendType;
    
    private String unifiedNumber;
    
    private String cardName;
    
    private String cardNumber;
    
    private Integer mainAppId;
    
    private Integer hasTm;    
   
    
    private List<Applicant> applicants;
    
    private List<Trademark> trademarks;
    
    private List<Material> material;
    
    @JsonIgnore	
  	private String reason; //excel文件中这条数据不正确的原因
  	
  	@JsonIgnore	
  	private String lineNo; // excel文件中的行号    
  	
    //逻辑申请人所代表的所有申请人的商标总数
    private Integer tradeMarkCount;
        
    //当前申请人的商标数
    private Integer tmCount;
    
  	
    
    public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
    	if (applicantName!=null){
    		applicantName=applicantName.trim();
    		if (applicantName.indexOf("?")>-1){
    			applicantName=applicantName.replace("?", " ");
    		}
    	}
        this.applicantName = applicantName;
    }

    public String getApplicantAddress() {
        return applicantAddress;
    }

    public void setApplicantAddress(String applicantAddress) {
    	if (applicantAddress!=null){
    		applicantAddress=applicantAddress.trim();
    		if (applicantAddress.indexOf("?")>-1){
    			applicantAddress=applicantAddress.replace("?", " ");
    		}
    	}
        this.applicantAddress = applicantAddress;
    }

    public String getApplicantEnName() {
        return applicantEnName;
    }

    public void setApplicantEnName(String applicantEnName) {
    	if (applicantEnName!=null){
    		applicantEnName=applicantEnName.trim();
    		if (applicantEnName.indexOf("?")>-1){
    			applicantEnName=applicantEnName.replace("?", " ");
    		}
    	}
        this.applicantEnName = applicantEnName ;
    }

    public String getApplicantEnAddress() {
        return applicantEnAddress;
    }

    public void setApplicantEnAddress(String applicantEnAddress) {
    	if (applicantEnAddress!=null){
    		applicantEnAddress=applicantEnAddress.trim();
    		if (applicantEnAddress.indexOf("?")>-1){
    			applicantEnAddress=applicantEnAddress.replace("?", " ");
    		}
    	}
        this.applicantEnAddress = applicantEnAddress;
    }

    public String getUsertName() {
        return usertName;
    }

    public void setUsertName(String usertName) {
        this.usertName = usertName == null ? null : usertName.trim();
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

	public List<Trademark> getTrademarks() {
		return trademarks;
	}

	public void setTrademarks(List<Trademark> trademarks) {
		this.trademarks = trademarks;
	}

	public List<Material> getMaterial() {
		return material;
	}

	public void setMaterial(List<Material> material) {
		this.material = material;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public String getUnifiedNumber() {
		return unifiedNumber;
	}

	public void setUnifiedNumber(String unifiedNumber) {
		this.unifiedNumber = unifiedNumber;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public Integer getMainAppId() {
		return mainAppId;
	}

	public void setMainAppId(Integer mainAppId) {
		this.mainAppId = mainAppId;
	}

	public Integer getHasTm() {
		return hasTm;
	}

	public void setHasTm(Integer hasTm) {
		this.hasTm = hasTm;
	}

	public List<Applicant> getApplicants() {
		return applicants;
	}

	public void setApplicants(List<Applicant> applicants) {
		this.applicants = applicants;
	}

	public Integer getTradeMarkCount() {
		return tradeMarkCount;
	}

	public void setTradeMarkCount(Integer tradeMarkCount) {
		this.tradeMarkCount = tradeMarkCount;
	}

	public Integer getTmCount() {
		return tmCount;
	}

	public void setTmCount(Integer tmCount) {
		this.tmCount = tmCount;
	}


	
	
    
    
}