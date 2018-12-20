package com.yootii.bdy.material.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Material {
    private Integer materialId;

    private Integer custId;

    private Integer applicantId;
    
    private String applicantName;
    
    private String applicantEnName;

    private String title;

    private String subject;

    private String desc;

    private Integer type;

    private String format;

    private Integer status;

    private String creater;
    
        
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    private String modifier;
    
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date modifyTime;

    private String tmNumber;

    private Integer caseId;

    private String address;

    private Integer size;
    
    private Integer versionNo;
    
    private List<MaterialVersion> versions;
    
    private Integer sortId; //资料分类编号
    
    private Integer fileName;
    
    private Integer joinAppId;
    
    private Integer precase;
    
    
    
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date docDate;
    
    

	private List<MaterialSort> materialSort;
    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public Integer getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(Integer applicantId) {
        this.applicantId = applicantId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject == null ? null : subject.trim();
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format == null ? null : format.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier == null ? null : modifier.trim();
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getTmNumber() {
        return tmNumber;
    }

    public void setTmNumber(String tmNumber) {
        this.tmNumber = tmNumber == null ? null : tmNumber.trim();
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

	public String getApplicantName() {
		return applicantName;
	}

	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}

	public String getApplicantEnName() {
		return applicantEnName;
	}

	public void setApplicantEnName(String applicantEnName) {
		this.applicantEnName = applicantEnName;
	}

	public Integer getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	public List<MaterialVersion> getVersions() {
		return versions;
	}

	public void setVersions(List<MaterialVersion> versions) {
		this.versions = versions;
	}

	public Integer getFileName() {
		return fileName;
	}

	public void setFileName(Integer fileName) {
		this.fileName = fileName;
	}

	public Integer getSortId() {
		return sortId;
	}

	public void setSortId(Integer sortId) {
		this.sortId = sortId;
	}

	public List<MaterialSort> getMaterialSort() {
		return materialSort;
	}

	public void setMaterialSort(List<MaterialSort> materialSort) {
		this.materialSort = materialSort;
	}
	
	public Date getDocDate() {
		return docDate;
	}

	public void setDocDate(Date docDate) {
		this.docDate = docDate;
	}

	public Integer getJoinAppId() {
		return joinAppId;
	}

	public void setJoinAppId(Integer joinAppId) {
		this.joinAppId = joinAppId;
	}

	public Integer getPrecase() {
		return precase;
	}

	public void setPrecase(Integer precase) {
		this.precase = precase;
	}
	
}