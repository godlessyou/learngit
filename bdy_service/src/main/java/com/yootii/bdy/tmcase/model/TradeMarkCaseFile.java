package com.yootii.bdy.tmcase.model;

import java.util.Date;

public class TradeMarkCaseFile {
    private Integer id;

    private Integer caseId;

    private Integer fileName;

    private String fileUrl;

    private String fileType;

    private String username;

    private Date modifyTime;
    
    private Integer joinAppId;
    
    private String  custId;
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

	public Integer getFileName() {
		return fileName;
	}

	public void setFileName(Integer fileName) {
		this.fileName = fileName;
	}

	public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl == null ? null : fileUrl.trim();
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType == null ? null : fileType.trim();
    }

    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

	public Integer getJoinAppId() {
		return joinAppId;
	}

	public void setJoinAppId(Integer joinAppId) {
		this.joinAppId = joinAppId;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	
	
}