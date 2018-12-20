package  com.yootii.bdy.task.model;


import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class TmCaseTaskToDoList extends ToDoList{
	
	@JsonIgnore
    private Integer id;  // 待办事项编号
    
    private Integer pageId; //页的编号

    private Integer caseTypeId; //案件类型对应的编号
    
    private Integer caseGroup; //案件类型对应的编号
    
    //------- 待办事项对应的案件的属性   
    
    private String agentNum;//代理文号
    
    private Integer caseId; //案件编号      
    
    private String caseType; //案件类型   
    
    private String caseStatus; //案件状态  
        

    private String appCnName;//申请人名称
	
	private String appEnName;//申请人英文名称	
	
	private Date appDate;//申请日  
    
    private String goodClasses;//商标的国际分类，多个类别之间用分号隔开

	private String imageFile;//图样	
    
    private String tmName;//商标名称    

    private String regNumber;//商标注册号   
        
    private Date regDate;//商标注册日

    private Date rejectDate;//根据案件类型，分别代表被驳回日期/被异议日期/被撤三日期
    
    private Date statusTime;//最新状态更新时间
    
    private String submitStatus;//递交状态
    
    private String processPerson;//案件处理人
    
    private String agencyName;//合作代理所
    
    private String custName;//客户名称
    
    private Integer agentUserId;// 负责该客户的代理人的Id
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")  
    private Date modifyDate;
    
    //------- 待办事项属性
    private Date docDate;
    private String fileType;    //官文类型

    //新增
    private String message;
    private Date limitdate;
    private Date createdate;
    private String urgencyDegree;
    private Integer isclose;
     
   
    
    
    
	public Integer getIsclose() {
		return isclose;
	}
	public void setIsclose(Integer isclose) {
		this.isclose = isclose;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getUrgencyDegree() {
		return urgencyDegree;
	}
	public void setUrgencyDegree(String urgencyDegree) {
		this.urgencyDegree = urgencyDegree;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getLimitdate() {
		return limitdate;
	}
	public void setLimitdate(Date limitdate) {
		this.limitdate = limitdate;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public Date getDocDate() {
		return docDate;
	}
	public void setDocDate(Date docDate) {
		this.docDate = docDate;
	}
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
	

	

	public String getAppCnName() {
		return appCnName;
	}
	public void setAppCnName(String appCnName) {
		this.appCnName = appCnName;
	}
	public String getAppEnName() {
		return appEnName;
	}
	public void setAppEnName(String appEnName) {
		this.appEnName = appEnName;
	}
		
	public String getGoodClasses() {
		return goodClasses;
	}
	public void setGoodClasses(String goodClasses) {
		this.goodClasses = goodClasses;
	}
	
	public String getImageFile() {
		return imageFile;
	}
	public void setImageFile(String imageFile) {
		this.imageFile = imageFile;
	}
	
	public String getTmName() {
		return tmName;
	}
	public void setTmName(String tmName) {
		this.tmName = tmName;
	}
	public String getRegNumber() {
		return regNumber;
	}
	public void setRegNumber(String regNumber) {
		this.regNumber = regNumber;
	}

	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	public Date getAppDate() {
		return appDate;
	}
	public void setAppDate(Date appDate) {
		this.appDate = appDate;
	}
	
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	public Date getRejectDate() {
		return rejectDate;
	}
	public void setRejectDate(Date rejectDate) {
		this.rejectDate = rejectDate;
	}
	public String getCaseType() {
		if (caseType!=null && caseType.equals("变更名义地址集体管理规则成员名单")){
			caseType="变更名义/地址";
		}
		return caseType;
	}
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}
	public Integer getPageId() {
		return pageId;
	}
	public void setPageId(Integer pageId) {
		this.pageId = pageId;
	}

	public String getCaseStatus() {
		return caseStatus;
	}
	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}
	public Integer getCaseTypeId() {
		return caseTypeId;
	}
	public void setCaseTypeId(Integer caseTypeId) {
		this.caseTypeId = caseTypeId;
	}
	public String getAgentNum() {
		return agentNum;
	}
	public void setAgentNum(String agentNum) {
		this.agentNum = agentNum;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") 
	public Date getStatusTime() {
		return statusTime;
	}
	public void setStatusTime(Date statusTime) {
		this.statusTime = statusTime;
	}
	public String getSubmitStatus() {
		return submitStatus;
	}
	public void setSubmitStatus(String submitStatus) {
		this.submitStatus = submitStatus;
	}
	public String getProcessPerson() {
		return processPerson;
	}
	public void setProcessPerson(String processPerson) {
		this.processPerson = processPerson;
	}
	public String getAgencyName() {
		return agencyName;
	}
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public Integer getAgentUserId() {
		return agentUserId;
	}
	public void setAgentUserId(Integer agentUserId) {
		this.agentUserId = agentUserId;
	}
	public Integer getCaseGroup() {
		return caseGroup;
	}
	public void setCaseGroup(Integer caseGroup) {
		this.caseGroup = caseGroup;
	}
	
	
}