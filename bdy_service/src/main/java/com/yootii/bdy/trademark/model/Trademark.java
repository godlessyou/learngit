package com.yootii.bdy.trademark.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Trademark {
    private Integer tmId;

    private String regNumber;		//商标序号

    private String tmType;			//商标类型

    private String tmGroup;           //商标组

    private String tmName;           //商标名称

    private String applicantName;     //申请人名称    

    private String applicantAddress;          //申请  者地址

    private String applicantEnName;     //申请  者英文名称

    private String applicantEnAddress;      //申请  者 英文住址

    private String gtApplicantName;

    private String gtApplicantAddress;

    private String status;           //商标状态

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date appDate;             //申请时间

    private String approvalNumber;
    
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date approvalDate;               //批准时间

    private String regnoticeNumber;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date regNoticeDate;               //注册公告日期            

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date validStartDate;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date validEndDate;

    private String tmCategory;                 //商标 类别

    private String agent;               //代理机构

    private String classify;

    private String imgFileUrl;       //图像路径

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date modifyDate;

    private String imgFilePath;   

	private List<TrademarkCategory> trademarkCategories;
    
    private List<TrademarkProcess> trademarkProcesses;
    
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date gjRegDate;            //国际注册日期
    
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date hqzdDate;
    
  
    private String priorDate;
    
    private String ifShareTm;  //是否共有商标
    
    private String tmForm; //商标形式
    
    private String imgUrl; //商标图片官网地址
    
    private String tmStatus; //商标总状态
    
    
    
	private String reason; //excel文件中这条数据不正确的原因
	
	@JsonIgnore	
	private String lineNo; // excel文件中的行号    
	
    

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

    public String getTmType() {
        return tmType;
    }

    public void setTmType(String tmType) {
        this.tmType = tmType == null ? null : tmType.trim();
    }

    public String getTmGroup() {
        return tmGroup;
    }

    public void setTmGroup(String tmGroup) {
        this.tmGroup = tmGroup == null ? null : tmGroup.trim();
    }

    public String getTmName() {
        return tmName;
    }

    public void setTmName(String tmName) {
        this.tmName = tmName == null ? null : tmName.trim();
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName == null ? null : applicantName.trim();
    }

    public String getApplicantAddress() {
        return applicantAddress;
    }

    public void setApplicantAddress(String applicantAddress) {
        this.applicantAddress = applicantAddress == null ? null : applicantAddress.trim();
    }

    public String getApplicantEnName() {
        return applicantEnName;
    }

    public void setApplicantEnName(String applicantEnName) {
        this.applicantEnName = applicantEnName == null ? null : applicantEnName.trim();
    }

    public String getApplicantEnAddress() {
        return applicantEnAddress;
    }

    public void setApplicantEnAddress(String applicantEnAddress) {
        this.applicantEnAddress = applicantEnAddress == null ? null : applicantEnAddress.trim();
    }

    public String getGtApplicantName() {
        return gtApplicantName;
    }

    public void setGtApplicantName(String gtApplicantName) {
        this.gtApplicantName = gtApplicantName == null ? null : gtApplicantName.trim();
    }

    public String getGtApplicantAddress() {
        return gtApplicantAddress;
    }

    public void setGtApplicantAddress(String gtApplicantAddress) {
        this.gtApplicantAddress = gtApplicantAddress == null ? null : gtApplicantAddress.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getAppDate() {
        return appDate;
    }

    public void setAppDate(Date appDate) {
        this.appDate = appDate;
    }

    public String getApprovalNumber() {
        return approvalNumber;
    }

    public void setApprovalNumber(String approvalNumber) {
        this.approvalNumber = approvalNumber == null ? null : approvalNumber.trim();
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getRegnoticeNumber() {
        return regnoticeNumber;
    }

    public void setRegnoticeNumber(String regnoticeNumber) {
        this.regnoticeNumber = regnoticeNumber == null ? null : regnoticeNumber.trim();
    }

    public Date getRegNoticeDate() {
        return regNoticeDate;
    }

    public void setRegNoticeDate(Date regNoticeDate) {
        this.regNoticeDate = regNoticeDate;
    }

    public Date getValidStartDate() {
        return validStartDate;
    }

    public void setValidStartDate(Date validStartDate) {
        this.validStartDate = validStartDate;
    }

    public Date getValidEndDate() {
        return validEndDate;
    }

    public void setValidEndDate(Date validEndDate) {
        this.validEndDate = validEndDate;
    }

    public String getTmCategory() {
        return tmCategory;
    }

    public void setTmCategory(String tmCategory) {
        this.tmCategory = tmCategory == null ? null : tmCategory.trim();
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent == null ? null : agent.trim();
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify == null ? null : classify.trim();
    }

    public String getImgFileUrl() {
        return imgFileUrl;
    }

    public void setImgFileUrl(String imgFileUrl) {
        this.imgFileUrl = imgFileUrl == null ? null : imgFileUrl.trim();
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getImgFilePath() {
        return imgFilePath;
    }

    public void setImgFilePath(String imgFilePath) {
        this.imgFilePath = imgFilePath == null ? null : imgFilePath.trim();
    }

	public List<TrademarkCategory> getTrademarkCategories() {
		return trademarkCategories;
	}

	public void setTrademarkCategories(List<TrademarkCategory> trademarkCategories) {
		this.trademarkCategories = trademarkCategories;
	}

	public List<TrademarkProcess> getTrademarkProcesses() {
		return trademarkProcesses;
	}

	public void setTrademarkProcesses(List<TrademarkProcess> trademarkProcesses) {
		this.trademarkProcesses = trademarkProcesses;
	}
	
	 
    public Date getGjRegDate() {
		return gjRegDate;
	}

	public void setGjRegDate(Date gjRegDate) {
		this.gjRegDate = gjRegDate;
	}

	public Date getHqzdDate() {
		return hqzdDate;
	}

	public void setHqzdDate(Date hqzdDate) {
		this.hqzdDate = hqzdDate;
	}

	public String getPriorDate() {
		return priorDate;
	}

	public void setPriorDate(String priorDate) {
		this.priorDate = priorDate;
	}
	
	public String getIfShareTm() {
		return ifShareTm;
	}

	public void setIfShareTm(String ifShareTm) {
		this.ifShareTm = ifShareTm;
	}

	public String getTmForm() {
		return tmForm;
	}

	public void setTmForm(String tmForm) {
		this.tmForm = tmForm;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getTmStatus() {
		return tmStatus;
	}

	public void setTmStatus(String tmStatus) {
		this.tmStatus = tmStatus;
	}
	


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


}