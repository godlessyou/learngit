package com.yootii.bdy.trademark.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class TrademarkProcess {
    private Integer id;

    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date statusDate;

    private Integer tmId;

    private String regNumber;
    
    private String spwNumber;//商评委文号  
    

	private String bussName;//业务名称
    
    @JsonIgnore	
  	private String reason; //excel文件中这条数据不正确的原因
  	
  	@JsonIgnore	
  	private String lineNo; // excel文件中的行号    
  	
  	
  	

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }

    public Integer getTmId() {
        return tmId;
    }

    public void setTmId(Integer tmId) {
        this.tmId = tmId;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber == null ? null : regNumber.trim();
    }
    
    
    public String getSpwNumber() {
		return spwNumber;
	}

	public void setSpwNumber(String spwNumber) {
		this.spwNumber = spwNumber;
	}

	public String getBussName() {
		return bussName;
	}

	public void setBussName(String bussName) {
		this.bussName = bussName;
	}
}