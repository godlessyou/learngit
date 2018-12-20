package com.yootii.bdy.remind.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Remind {
    private Integer rid;

    private String title;

    private String message;

    private Integer isclose;
    @JsonIgnore
    private Integer depid;
    @JsonIgnore
    private Integer userid;
    @JsonIgnore
    private Integer custid;
    @JsonIgnore
    private Integer agencyid;

    private String createuser;
    
    private String closeuser;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date limitdate;
    
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createdate;
    
    private Date closedate;
    private Integer caseId;
    
    private String qcreatedate;
    private String qlimitdate;
    
    
    

	public String getQcreatedate() {
		return qcreatedate;
	}

	public void setQcreatedate(String qcreatedate) {
		this.qcreatedate = qcreatedate;
	}

	public String getQlimitdate() {
		return qlimitdate;
	}

	public void setQlimitdate(String qlimitdate) {
		this.qlimitdate = qlimitdate;
	}

	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	private String username;

    private String custname;

    
    
    
	public Integer getRid() {
		return rid;
	}

	public void setRid(Integer rid) {
		this.rid = rid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getIsclose() {
		return isclose;
	}

	public void setIsclose(Integer isclose) {
		this.isclose = isclose;
	}

	public Integer getDepid() {
		return depid;
	}

	public void setDepid(Integer depid) {
		this.depid = depid;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Integer getCustid() {
		return custid;
	}

	public void setCustid(Integer custid) {
		this.custid = custid;
	}

	public Integer getAgencyid() {
		return agencyid;
	}

	public void setAgencyid(Integer agencyid) {
		this.agencyid = agencyid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCustname() {
		return custname;
	}

	public void setCustname(String custname) {
		this.custname = custname;
	}

	public String getCreateuser() {
		return createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	public String getCloseuser() {
		return closeuser;
	}

	public void setCloseuser(String closeuser) {
		this.closeuser = closeuser;
	}

	public Date getLimitdate() {
		return limitdate;
	}

	public void setLimitdate(Date limitdate) {
		this.limitdate = limitdate;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Date getClosedate() {
		return closedate;
	}

	public void setClosedate(Date closedate) {
		this.closedate = closedate;
	}
	
        
}