package com.yootii.bdy.tmcase.model;


import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yootii.bdy.bill.model.ChargeRecord;
import com.yootii.bdy.material.model.Material;

public class TradeMarkCase {
    private Integer id;  // 案件编号
    
    private Integer parentId;  // 父案件编号
    
    private Integer billStatus; // 案件的账单状态，0：已核销，1：未核销

    private Integer custId;//客户编号
    
    private Integer custContactId;//客户联系人编号
    
    private String custContactName;//客户联系人名字
    
    private String custContactTel;//客户联系人名字
    
    private String custContactEmail;//客户联系人名字
    
    private String processId;//流程实例的Id

    private String whoIsApp;//流程实例的Id

    private Integer caseGroup;//代理机构编号

    private Integer agencyId;//代理机构编号
    
    private Integer caseTypeId;//案件类型编号
    
    private String agenName;//代理机构名称
    private String agencyTel;//代理机构电话
    private String tmcaseType; //案件类型
    
    private String remarks;
   

	private String caseType;//案件类型
    
    private String tmName;//商标名称
    
    private String appNumber;//商标申请号

    private String regNumber;//商标注册号
    
    private String status;//案件最新状态
    
    private String caseStatus;//案件最新环节做的事情
    
    private String username;//案件最新环节的处理人
    
    private Integer supplement;  // 是否提交补充材料，1：代表提交，null或者其他值代表不提交
    
    private String docNumber;// 发文编号
   
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date statusDate; //案件状态日期  
	
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date appDate;//申请日

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date regDate;//商标注册日

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date rejectDate;//根据案件类型，分别代表被驳回日期/被异议日期/被撤三日期

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date validStartDate;//有效期起始日

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date validEndDate;//有效期截止日

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date objectionDate;//异议截止日期  
    
    private String approvalNumber;//初审公告期号
    
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date approvalDate;//初审公告日    
   
	private String regNoticeNumber;//注册公告期号
	
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date createDate;//创建日期 
	
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date modifyDate;//修改日期 
	
	private String submitType;//递交方式
	
	private Integer cotag;//是否合作代理机构
	private Integer coagencyId;//合作代理机构Id
	private Integer couserId ;//合作方userId

	private String applicantType;//申请人类型, 分别为：法人或其它组织，自然人,
	private String appGJdq ;//书式类型, 分别为：中国大陆，国外，中国台湾/中国香港/中国澳门',
	private String agentNum;//代理文号
	private String agentPerson ;//代理人姓名
	private String attorneyFile;//代理委托书路径
	private String certCode;//统一社会信用代码
	private String appCnName;//申请人名称
	private String appEnName;//申请人英文名称
	private String appCertificate;//证件名称
	private String appCertificateNum;//证件号码
	private String appCertFileIsCn;//证明文件是否为中文
	private String appCertFileCn;//身份证明文件中文
	private String appCertFileEn;//身份证明文件外文
	private String companyCertFileCn;//主体资格证明文件中文
	private String companyCertFileEn;//主体资格证明原文件外文
	private String appRegionalism ;//申请人行政区划,（省市县/区三级行政区划）
	private String appCountryOrRegion;//国家或地区
	private String appCnAddr;//申请人地址
	private String appEnAddr;//申请人地址英文
	private String appContactPerson;//联系人
	private String appContactTel;//联系电话
	private String appContaceFax;//传真
	private String appContactZip;//邮政编码
	private String acceptPerson;//申请人大陆接收人
	private String acceptPersonAddr;//申请人大陆接收人地址
	private String acceptPersonZip;//申请人大陆接收人邮编
	
	private String tmType;//商标类型, 只能是：一般，2：集体，3：证明
	private String isDlbz;//是否地理标志， 允许的值分别为：是，否
	private String memberRule;//集体证明商标使用管理规则
	private String memberRuleFile;//集体证明商标使用管理规则附件
	private String memberNamelist;//集体成员名单
	private String memberNamelistFile;//集体成员名单附件
	private String isAppWithDetectAbility;//是否具备检测能力，允许的值：是，否
	private String appInspectionCertFile;//申请人检测资质证书
	private String appZyjcsbFile;//申请人专业检测设备清单
	private String appZyjsFile;//申请人专业技术人员名单
	private String appJsryzsFile;//申请人技术人员证书
	private String appWtjchtFile;//申请人与具有检测资格的机构签署的委托检测合同
	private String wtfrFile;//受委托机构的单位法人证书
	private String wtzzFile;//受委托机构的资质证书
	private String zyjcsbFile;//专业检测设备清单
	private String zyjsryFile;//专业技术人员名单
	private String regionSignFile1;//地理标志附件一
	private String regionSignFile2;//地理标志附件二
	private String regionSignFile3;//地理标志附件三
	private String regionSignFile4;//地理标志附件四
	private String regionSignFile5;//地理标志附件五
	private String ifSolidTm;//是否三维标志，允许的值：是，否
	private String colorSign;//是否颜色组合，允许的值：是，否
	private String tmVoice;//声音商标，允许的值：是，否
	private String voiceFile;//声音文件
	private String tmDesignDeclare;//商标说明
	
	private String ifShareTm;//是否共同申请, 允许的值：是，否
	private List<TradeMarkCaseJoinApp> joinApps;
	
	private String priorityType;//优先权声明，允许的值：无，在先优先权，展会优先权
	private String isLoadPriorityFile;//是否上传优先权证明文件，允许的值：是，否
	private String priorityFile;//优先权证明文件
	private String priorityBaseCrty;//申请、展出国家、地区
	
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date priorityAppDate;//申请、展出日期
	private String priorityAppNum;//申请号
	
	private String goodClasses;//所有类别
	private List<TradeMarkCaseCategory> goods;//类别
	
	private String imageFile;//图样
	private String blackWhiteFile;//黑白稿
	private String isPortraitAsApp;//以肖像作为商标申请注册，允许的值：是，否
	private String portraitFile;//肖像证明文件
	private String relatedFile;//相关说明文件

	@JsonIgnore
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date appStartTime;
	@JsonIgnore
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date appEndTime;
	
	@JsonIgnore
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date regStartTime;
	@JsonIgnore
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date regEndTime;
	
	private List<ChargeRecord> chargeRecords;
	
	private List<Material> materials;
	
	private Integer  applicantId;

	//新加字段 2018-04-22
	private String changeType;  // 变更类型

	private String uploadFileLanguage; 

	private String isChangeAgency;

	private String preChangeCnName;

	private String preChangeEnName;

	private String preChangeCnAdress;

	private String preChangeEnAdress;

	private String beforeChangeMessage;

	private String afterChangeMessage;

	private String assignorCnName;

	private String assignorEnName;

	private String assignorCnAdress;

	private String assignorEnAdress;

	private String assignorNationality;

	private String assignorCountryAndregion;

	private String assignorType;

	private String assignorUploadFileLanguage;

	private String assignorCertificateName;

	private String assignorCertificateNumber;

	private String transferorCnName;

	private String transferorEnName;

	private String transferorCnAdress;

	private String transferorEnAdress;

	private String transferorNationality;

	private String transferorCountryAndregion;

	private String transferorType;

	private String transferorUploadFileLanguage;

	private String transferorCertificateName;

	private String transferorCertificateNumber;

	private String preChangeAgencyName;


	private String isResend;//商标更正是否需重新制发证书文件

	private String transfer;//办理业务：商标转让 、 商标移转
	
	//转让申请文件
	private String transferorAttorneyFile;//转让人代理委托书路径
	private String assigneeAttorneyFile;//受让人委托书
	private String gqcxNameFile;//自然人死亡/企业或其他组织注销证明
	private String transferorCertFileCn;//转让人身份证明文件中文
	private String assigneeCertFileCn;//受让人身份证明文件中文
	private String transferorCertFileEn;//转让人身份证明文件外文
	private String assigneeCertFileEn;//受让人身份证明文件外文
	private String transferorCompanyCertFileCn;//转让人主体资格证明文件中文
	private String assigneeCompanyCertFileCn;//受让人主体资格证明文件中文
	private String transferorCompanyCertFileEn;//转让人主体资格证明原文件外文
	private String assigneeCompanyCertFileEn;//受让人主体资格证明原文件外文
	private String flwsNameFile;//同意转让声明或商标移转证明
	private String gtzqNameFile;//共有人知情转让转移证明
	private String sfzsFile;//受让人法人资质证书
	private String zrhtFile;//转让合同
	//变名变址
	private String bgCertFileCn;//变更证明文件中文
	private String bgCertFileEn;//变更证明文件英文
	
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date docDate;
	
	//
	private String dissentName;
	private String dissentAddress;
	private String dissentAgent;
	private Integer dissentAgentId;
	private String dissentGist;
	
	private String proxyDoc; //代理委托书
	
	private String docFile;      //异议理由书
	private String docTitle;
	
	private String dissentDoc;   //异议申请书
	private String dissentDocTitle;	
	
	//2018-12-05增加
	private String zrCertCode;//转让人统一社会信用代码
	private String srCertCode;//受让人统一社会信用代码
	
	
	//复杂案件类型的特殊字段
	private String cancelPersonName_CN;      //撤销人中文名称
	private String cancelPersonAgent;     //撤销人代理机构
	private String respondentName;       //被申请人名称
	private String respondentAddr;            //被申请人地址
	private String quoteTm;            //(无效宣告中)引证商标
	private String quoteTmClass;      //引证商标类别
	private String quoteRegNumber;              //引证商标注册号
	private String invalidPersonName_CN;         //无效申请人中文名称(无效答辩案件中
	private String invalidPersonAgent;        //无效申请人代理机构(无效答辩案件中)
	private String legalBasic;               //评审请求与法律依据
	
	private String recheckPersonName_CN;   //复审申请人中文名称
	private String recheckPersonAgent;			//复审申请人代理机构
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") 
	private Date cancelSubmitDate;   //撤三提交日期
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date dissentSubmitDate;  //异议提交日期
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date replyReceiveDate;//异议答辩文书收文日期
	
	
	
	public Date getReplyReceiveDate() {
		return replyReceiveDate;
	}

	public void setReplyReceiveDate(Date replyReceiveDate) {
		this.replyReceiveDate = replyReceiveDate;
	}

	public String getProxyDoc() {
		return proxyDoc;
	}

	public void setProxyDoc(String proxyDoc) {
		this.proxyDoc = proxyDoc;
	}

	public Date getDissentSubmitDate() {
		return dissentSubmitDate;
	}

	public void setDissentSubmitDate(Date dissentSubmitDate) {
		this.dissentSubmitDate = dissentSubmitDate;
	}

	public String getDissentDoc() {
		return dissentDoc;
	}

	public void setDissentDoc(String dissentDoc) {
		this.dissentDoc = dissentDoc;
	}

	public String getDissentDocTitle() {
		return dissentDocTitle;
	}

	public void setDissentDocTitle(String dissentDocTitle) {
		this.dissentDocTitle = dissentDocTitle;
	}

	public Date getCancelSubmitDate() {
		return cancelSubmitDate;
	}

	public void setCancelSubmitDate(Date cancelSubmitDate) {
		this.cancelSubmitDate = cancelSubmitDate;
	}

	public String getRecheckPersonName_CN() {
		return recheckPersonName_CN;
	}

	public void setRecheckPersonName_CN(String recheckPersonName_CN) {
		this.recheckPersonName_CN = recheckPersonName_CN;
	}

	public String getRecheckPersonAgent() {
		return recheckPersonAgent;
	}

	public void setRecheckPersonAgent(String recheckPersonAgent) {
		this.recheckPersonAgent = recheckPersonAgent;
	}

	public String getCancelPersonName_CN() {
		return cancelPersonName_CN;
	}

	public void setCancelPersonName_CN(String cancelPersonName_CN) {
		this.cancelPersonName_CN = cancelPersonName_CN;
	}

	public String getCancelPersonAgent() {
		return cancelPersonAgent;
	}

	public void setCancelPersonAgent(String cancelPersonAgent) {
		this.cancelPersonAgent = cancelPersonAgent;
	}

	public String getRespondentName() {
		return respondentName;
	}

	public void setRespondentName(String respondentName) {
		this.respondentName = respondentName;
	}

	public String getRespondentAddr() {
		return respondentAddr;
	}

	public void setRespondentAddr(String respondentAddr) {
		this.respondentAddr = respondentAddr;
	}

	public String getQuoteTm() {
		return quoteTm;
	}

	public void setQuoteTm(String quoteTm) {
		this.quoteTm = quoteTm;
	}

	public String getQuoteTmClass() {
		return quoteTmClass;
	}

	public void setQuoteTmClass(String quoteTmClass) {
		this.quoteTmClass = quoteTmClass;
	}

	public String getQuoteRegNumber() {
		return quoteRegNumber;
	}

	public void setQuoteRegNumber(String quoteRegNumber) {
		this.quoteRegNumber = quoteRegNumber;
	}

	public String getInvalidPersonName_CN() {
		return invalidPersonName_CN;
	}

	public void setInvalidPersonName_CN(String invalidPersonName_CN) {
		this.invalidPersonName_CN = invalidPersonName_CN;
	}

	public String getInvalidPersonAgent() {
		return invalidPersonAgent;
	}

	public void setInvalidPersonAgent(String invalidPersonAgent) {
		this.invalidPersonAgent = invalidPersonAgent;
	}

	public String getLegalBasic() {
		return legalBasic;
	}

	public void setLegalBasic(String legalBasic) {
		this.legalBasic = legalBasic;
	}

	public String getDocTitle() {
		return docTitle;
	}

	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}

	public String getDocFile() {
		return docFile;
	}

	public void setDocFile(String docFile) {
		this.docFile = docFile;
	}

	//Modification start, 2018-12-13
	//为撤三案件增加属性
	private String reason;//撤销理由	
	private String registrant;//商标注册人
	
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getRegistrant() {
		return registrant;
	}

	public void setRegistrant(String registrant) {
		this.registrant = registrant;
	}
	//Modification end
	
	
	
	public String getDissentGist() {
		return dissentGist;
	}

	public void setDissentGist(String dissentGist) {
		this.dissentGist = dissentGist;
	}

	public Integer getDissentAgentId() {
		return dissentAgentId;
	}

	public void setDissentAgentId(Integer dissentAgentId) {
		this.dissentAgentId = dissentAgentId;
	}

	public String getDissentName() {
		return dissentName;
	}

	public void setDissentName(String dissentName) {
		this.dissentName = dissentName;
	}

	public String getDissentAddress() {
		return dissentAddress;
	}

	public void setDissentAddress(String dissentAddress) {
		this.dissentAddress = dissentAddress;
	}

	public String getDissentAgent() {
		return dissentAgent;
	}

	public void setDissentAgent(String dissentAgent) {
		this.dissentAgent = dissentAgent;
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
	public Integer getCustId() {
		return custId;
	}
	public void setCustId(Integer custId) {
		this.custId = custId;
	}
	
	public Integer getCustContactId() {
		return custContactId;
	}
	public void setCustContactId(Integer custContactId) {
		this.custContactId = custContactId;
	}
	public String getCustContactName() {
		return custContactName;
	}
	public void setCustContactName(String custContactName) {
		this.custContactName = custContactName;
	}
	public String getCustContactTel() {
		return custContactTel;
	}
	public void setCustContactTel(String custContactTel) {
		this.custContactTel = custContactTel;
	}
	
	
	public String getCustContactEmail() {
		return custContactEmail;
	}
	public void setCustContactEmail(String custContactEmail) {
		this.custContactEmail = custContactEmail;
	}
		
	public Integer getAgencyId() {
		return agencyId;
	}
	public void setAgencyId(Integer agencyId) {
		this.agencyId = agencyId;
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
	public String getTmName() {
		return tmName;
	}
	 public String getTmcaseType() {
			return tmcaseType;
		}

		public void setTmcaseType(String tmcaseType) {
			this.tmcaseType = tmcaseType;
		}

	public void setTmName(String tmName) {
		this.tmName = tmName;
	}
	
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getRegNumber() {
		return regNumber;
	}
	public void setRegNumber(String regNumber) {
		this.regNumber = regNumber;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getStatusDate() {
		return statusDate;
	}
	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}
	public String getAgencyTel() {
		return agencyTel;
	}

	public void setAgencyTel(String agencyTel) {
		this.agencyTel = agencyTel;
	}
	public Date getAppDate() {
		return appDate;
	}
	public void setAppDate(Date appDate) {
		this.appDate = appDate;
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	public Date getRejectDate() {
		return rejectDate;
	}
	public void setRejectDate(Date rejectDate) {
		this.rejectDate = rejectDate;
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
	public Date getObjectionDate() {
		return objectionDate;
	}
	public void setObjectionDate(Date objectionDate) {
		this.objectionDate = objectionDate;
	}
	
	
	public String getApprovalNumber() {
			return approvalNumber;
	}
	public void setApprovalNumber(String approvalNumber) {
		this.approvalNumber = approvalNumber;
	}
	public Date getApprovalDate() {
		return approvalDate;
	}
	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}
	public String getRegNoticeNumber() {
		return regNoticeNumber;
	}
	public void setRegNoticeNumber(String regNoticeNumber) {
		this.regNoticeNumber = regNoticeNumber;
	}
	public Integer getCotag() {
		return cotag;
	}
	public void setCotag(Integer cotag) {
		this.cotag = cotag;
	}
	public Integer getCoagencyId() {
		return coagencyId;
	}
	public void setCoagencyId(Integer coagencyId) {
		this.coagencyId = coagencyId;
	}
	public Integer getCouserId() {
		return couserId;
	}
	public void setCouserId(Integer couserId) {
		this.couserId = couserId;
	}
	
	public String getApplicantType() {
		return applicantType;
	}
	public void setApplicantType(String applicantType) {
		this.applicantType = applicantType;
	}
	public String getAppGJdq() {
		return appGJdq;
	}
	public void setAppGJdq(String appGJdq) {
		this.appGJdq = appGJdq;
	}
	public String getAgentNum() {
		return agentNum;
	}
	public void setAgentNum(String agentNum) {
		this.agentNum = agentNum;
	}
	public String getAgentPerson() {
		return agentPerson;
	}
	public void setAgentPerson(String agentPerson) {
		this.agentPerson = agentPerson;
	}
	public String getAttorneyFile() {
		return attorneyFile;
	}
	public void setAttorneyFile(String attorneyFile) {
		this.attorneyFile = attorneyFile;
	}
	public String getCertCode() {
		return certCode;
	}
	public void setCertCode(String certCode) {
		this.certCode = certCode;
	}
	public String getAppCnName() {
		return appCnName;
	}
	public void setAppCnName(String appCnName) {
		if (appCnName!=null){
			appCnName=appCnName.trim();
    		if (appCnName.indexOf("?")>-1){
    			appCnName=appCnName.replace("?", " ");
    		}
    	}   
		this.appCnName = appCnName;
	}
	public String getAppEnName() {
		return appEnName;
	}
	public void setAppEnName(String appEnName) {
		if (appEnName!=null){
			appEnName=appEnName.trim();
    		if (appEnName.indexOf("?")>-1){
    			appEnName=appEnName.replace("?", " ");
    		}
    	}       
		this.appEnName = appEnName;
	}
	public String getAppCertificate() {
		return appCertificate;
	}
	public void setAppCertificate(String appCertificate) {
		this.appCertificate = appCertificate;
	}
	public String getAppCertificateNum() {
		return appCertificateNum;
	}
	public void setAppCertificateNum(String appCertificateNum) {
		this.appCertificateNum = appCertificateNum;
	}
	public String getAppCertFileIsCn() {
		return appCertFileIsCn;
	}
	public void setAppCertFileIsCn(String appCertFileIsCn) {
		this.appCertFileIsCn = appCertFileIsCn;
	}
	public String getAppCertFileCn() {
		return appCertFileCn;
	}
	public void setAppCertFileCn(String appCertFileCn) {
		this.appCertFileCn = appCertFileCn;
	}
	public String getAppCertFileEn() {
		return appCertFileEn;
	}
	public void setAppCertFileEn(String appCertFileEn) {
		this.appCertFileEn = appCertFileEn;
	}
	public String getCompanyCertFileCn() {
		return companyCertFileCn;
	}
	public void setCompanyCertFileCn(String companyCertFileCn) {
		this.companyCertFileCn = companyCertFileCn;
	}
	public String getCompanyCertFileEn() {
		return companyCertFileEn;
	}
	public void setCompanyCertFileEn(String companyCertFileEn) {
		this.companyCertFileEn = companyCertFileEn;
	}
	public String getAppRegionalism() {
		return appRegionalism;
	}
	public void setAppRegionalism(String appRegionalism) {
		this.appRegionalism = appRegionalism;
	}
	public String getAppCountryOrRegion() {
		return appCountryOrRegion;
	}
	public void setAppCountryOrRegion(String appCountryOrRegion) {
		this.appCountryOrRegion = appCountryOrRegion;
	}
	public String getAppCnAddr() {
		return appCnAddr;
	}
	public void setAppCnAddr(String appCnAddr) {
		if (appCnAddr!=null){
			appCnAddr=appCnAddr.trim();
    		if (appCnAddr.indexOf("?")>-1){
    			appCnAddr=appCnAddr.replace("?", " ");
    		}
    	} 
		this.appCnAddr = appCnAddr;
	}
	public String getAppEnAddr() {
		return appEnAddr;
	}
	public void setAppEnAddr(String appEnAddr) {
		if (appEnAddr!=null){
			appEnAddr=appEnAddr.trim();
    		if (appEnAddr.indexOf("?")>-1){
    			appEnAddr=appEnAddr.replace("?", " ");
    		}
    	} 
		this.appEnAddr = appEnAddr;
	}
	public String getAppContactPerson() {
		return appContactPerson;
	}
	public void setAppContactPerson(String appContactPerson) {
		this.appContactPerson = appContactPerson;
	}
	public String getAppContactTel() {
		return appContactTel;
	}
	public void setAppContactTel(String appContactTel) {
		this.appContactTel = appContactTel;
	}
	public String getAppContaceFax() {
		return appContaceFax;
	}
	public void setAppContaceFax(String appContaceFax) {
		this.appContaceFax = appContaceFax;
	}
	public String getAppContactZip() {
		return appContactZip;
	}
	public void setAppContactZip(String appContactZip) {
		this.appContactZip = appContactZip;
	}
	public String getAcceptPerson() {
		return acceptPerson;
	}
	public void setAcceptPerson(String acceptPerson) {
		this.acceptPerson = acceptPerson;
	}
	public String getAcceptPersonAddr() {
		return acceptPersonAddr;
	}
	public void setAcceptPersonAddr(String acceptPersonAddr) {
		this.acceptPersonAddr = acceptPersonAddr;
	}
	public String getAcceptPersonZip() {
		return acceptPersonZip;
	}
	public void setAcceptPersonZip(String acceptPersonZip) {
		this.acceptPersonZip = acceptPersonZip;
	}
	public String getTmType() {
		return tmType;
	}
	public void setTmType(String tmType) {
		this.tmType = tmType;
	}
	public String getIsDlbz() {
		return isDlbz;
	}
	public void setIsDlbz(String isDlbz) {
		this.isDlbz = isDlbz;
	}
	public String getMemberRule() {
		return memberRule;
	}
	public void setMemberRule(String memberRule) {
		this.memberRule = memberRule;
	}
	public String getMemberRuleFile() {
		return memberRuleFile;
	}
	public void setMemberRuleFile(String memberRuleFile) {
		this.memberRuleFile = memberRuleFile;
	}
	public String getMemberNamelist() {
		return memberNamelist;
	}
	public void setMemberNamelist(String memberNamelist) {
		this.memberNamelist = memberNamelist;
	}
	public String getMemberNamelistFile() {
		return memberNamelistFile;
	}
	public void setMemberNamelistFile(String memberNamelistFile) {
		this.memberNamelistFile = memberNamelistFile;
	}
	public String getIsAppWithDetectAbility() {
		return isAppWithDetectAbility;
	}
	public void setIsAppWithDetectAbility(String isAppWithDetectAbility) {
		this.isAppWithDetectAbility = isAppWithDetectAbility;
	}
	public String getAppInspectionCertFile() {
		return appInspectionCertFile;
	}
	public void setAppInspectionCertFile(String appInspectionCertFile) {
		this.appInspectionCertFile = appInspectionCertFile;
	}
	public String getAppZyjcsbFile() {
		return appZyjcsbFile;
	}
	public void setAppZyjcsbFile(String appZyjcsbFile) {
		this.appZyjcsbFile = appZyjcsbFile;
	}
	public String getAppZyjsFile() {
		return appZyjsFile;
	}
	public void setAppZyjsFile(String appZyjsFile) {
		this.appZyjsFile = appZyjsFile;
	}
	public String getAppJsryzsFile() {
		return appJsryzsFile;
	}
	public void setAppJsryzsFile(String appJsryzsFile) {
		this.appJsryzsFile = appJsryzsFile;
	}
	public String getAppWtjchtFile() {
		return appWtjchtFile;
	}
	public void setAppWtjchtFile(String appWtjchtFile) {
		this.appWtjchtFile = appWtjchtFile;
	}
	public String getWtfrFile() {
		return wtfrFile;
	}
	public void setWtfrFile(String wtfrFile) {
		this.wtfrFile = wtfrFile;
	}
	public String getWtzzFile() {
		return wtzzFile;
	}
	public void setWtzzFile(String wtzzFile) {
		this.wtzzFile = wtzzFile;
	}
	public String getZyjcsbFile() {
		return zyjcsbFile;
	}
	public void setZyjcsbFile(String zyjcsbFile) {
		this.zyjcsbFile = zyjcsbFile;
	}
	public String getZyjsryFile() {
		return zyjsryFile;
	}
	public void setZyjsryFile(String zyjsryFile) {
		this.zyjsryFile = zyjsryFile;
	}
	public String getRegionSignFile1() {
		return regionSignFile1;
	}
	public void setRegionSignFile1(String regionSignFile1) {
		this.regionSignFile1 = regionSignFile1;
	}
	public String getRegionSignFile2() {
		return regionSignFile2;
	}
	public void setRegionSignFile2(String regionSignFile2) {
		this.regionSignFile2 = regionSignFile2;
	}
	public String getRegionSignFile3() {
		return regionSignFile3;
	}
	public void setRegionSignFile3(String regionSignFile3) {
		this.regionSignFile3 = regionSignFile3;
	}
	public String getRegionSignFile4() {
		return regionSignFile4;
	}
	public void setRegionSignFile4(String regionSignFile4) {
		this.regionSignFile4 = regionSignFile4;
	}
	public String getRegionSignFile5() {
		return regionSignFile5;
	}
	public void setRegionSignFile5(String regionSignFile5) {
		this.regionSignFile5 = regionSignFile5;
	}
	public String getIfSolidTm() {
		return ifSolidTm;
	}
	public void setIfSolidTm(String ifSolidTm) {
		this.ifSolidTm = ifSolidTm;
	}
	public String getColorSign() {
		return colorSign;
	}
	public void setColorSign(String colorSign) {
		this.colorSign = colorSign;
	}
	public String getTmVoice() {
		return tmVoice;
	}
	public void setTmVoice(String tmVoice) {
		this.tmVoice = tmVoice;
	}
	public String getVoiceFile() {
		return voiceFile;
	}
	public void setVoiceFile(String voiceFile) {
		this.voiceFile = voiceFile;
	}
	public String getTmDesignDeclare() {
		return tmDesignDeclare;
	}
	public void setTmDesignDeclare(String tmDesignDeclare) {
		this.tmDesignDeclare = tmDesignDeclare;
	}
	public String getIfShareTm() {
		return ifShareTm;
	}
	public void setIfShareTm(String ifShareTm) {
		this.ifShareTm = ifShareTm;
	}
	public List<TradeMarkCaseJoinApp> getJoinApps() {
		return joinApps;
	}
	public void setJoinApps(List<TradeMarkCaseJoinApp> joinApps) {
		this.joinApps = joinApps;
	}
	public String getPriorityType() {
		return priorityType;
	}
	public void setPriorityType(String priorityType) {
		this.priorityType = priorityType;
	}
	public String getIsLoadPriorityFile() {
		return isLoadPriorityFile;
	}
	public void setIsLoadPriorityFile(String isLoadPriorityFile) {
		this.isLoadPriorityFile = isLoadPriorityFile;
	}
	public String getPriorityFile() {
		return priorityFile;
	}
	public void setPriorityFile(String priorityFile) {
		this.priorityFile = priorityFile;
	}
	public String getPriorityBaseCrty() {
		return priorityBaseCrty;
	}
	public void setPriorityBaseCrty(String priorityBaseCrty) {
		this.priorityBaseCrty = priorityBaseCrty;
	}
	
	public Date getPriorityAppDate() {
		return priorityAppDate;
	}
	public void setPriorityAppDate(Date priorityAppDate) {
		this.priorityAppDate = priorityAppDate;
	}
	public String getPriorityAppNum() {
		return priorityAppNum;
	}
	public void setPriorityAppNum(String priorityAppNum) {
		this.priorityAppNum = priorityAppNum;
	}
	public String getGoodClasses() {
		return goodClasses;
	}
	public void setGoodClasses(String goodClasses) {
		this.goodClasses = goodClasses;
	}
	public List<TradeMarkCaseCategory> getGoods() {
		return goods;
	}
	public void setGoods(List<TradeMarkCaseCategory> goods) {
		this.goods = goods;
	}
	public String getImageFile() {
		return imageFile;
	}
	public void setImageFile(String imageFile) {
		this.imageFile = imageFile;
	}
	public String getBlackWhiteFile() {
		return blackWhiteFile;
	}
	public void setBlackWhiteFile(String blackWhiteFile) {
		this.blackWhiteFile = blackWhiteFile;
	}
	public String getIsPortraitAsApp() {
		return isPortraitAsApp;
	}
	public void setIsPortraitAsApp(String isPortraitAsApp) {
		this.isPortraitAsApp = isPortraitAsApp;
	}
	public String getPortraitFile() {
		return portraitFile;
	}
	public void setPortraitFile(String portraitFile) {
		this.portraitFile = portraitFile;
	}
	public String getRelatedFile() {
		return relatedFile;
	}
	public void setRelatedFile(String relatedFile) {
		this.relatedFile = relatedFile;
	}
	public Date getAppStartTime() {
		return appStartTime;
	}
	public void setAppStartTime(Date appStartTime) {
		this.appStartTime = appStartTime;
	}
	public Date getAppEndTime() {
		return appEndTime;
	}
	public void setAppEndTime(Date appEndTime) {
		this.appEndTime = appEndTime;
	}
	public Date getRegStartTime() {
		return regStartTime;
	}
	public void setRegStartTime(Date regStartTime) {
		this.regStartTime = regStartTime;
	}
	public Date getRegEndTime() {
		return regEndTime;
	}
	public void setRegEndTime(Date regEndTime) {
		this.regEndTime = regEndTime;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getSubmitType() {
		return submitType;
	}
	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}
	public String getAppNumber() {
		return appNumber;
	}
	public void setAppNumber(String appNumber) {
		this.appNumber = appNumber;
	}
	public List<ChargeRecord> getChargeRecords() {
		return chargeRecords;
	}
	public void setChargeRecords(List<ChargeRecord> chargeRecords) {
		this.chargeRecords = chargeRecords;
	}
	public List<Material> getMaterials() {
		return materials;
	}
	public void setMaterials(List<Material> materials) {
		this.materials = materials;
	}
	public Integer getApplicantId() {
		return applicantId;
	}
	public void setApplicantId(Integer applicantId) {
		this.applicantId = applicantId;
	}
	public String getChangeType() {
		return changeType;
	}
	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}
	public String getUploadFileLanguage() {
		return uploadFileLanguage;
	}
	public void setUploadFileLanguage(String uploadFileLanguage) {
		this.uploadFileLanguage = uploadFileLanguage;
	}
	public String getIsChangeAgency() {
		return isChangeAgency;
	}
	public void setIsChangeAgency(String isChangeAgency) {
		this.isChangeAgency = isChangeAgency;
	}
	public String getPreChangeCnName() {
		return preChangeCnName;
	}
	public void setPreChangeCnName(String preChangeCnName) {
		this.preChangeCnName = preChangeCnName;
	}
	public String getPreChangeEnName() {
		return preChangeEnName;
	}
	public void setPreChangeEnName(String preChangeEnName) {
		this.preChangeEnName = preChangeEnName;
	}
	public String getPreChangeCnAdress() {
		return preChangeCnAdress;
	}
	public void setPreChangeCnAdress(String preChangeCnAdress) {		
		this.preChangeCnAdress = preChangeCnAdress;
	}
	public String getPreChangeEnAdress() {
		return preChangeEnAdress;
	}
	public void setPreChangeEnAdress(String preChangeEnAdress) {
		this.preChangeEnAdress = preChangeEnAdress;
	}
	public String getBeforeChangeMessage() {
		return beforeChangeMessage;
	}
	public void setBeforeChangeMessage(String beforeChangeMessage) {
		this.beforeChangeMessage = beforeChangeMessage;
	}
	public String getAfterChangeMessage() {
		return afterChangeMessage;
	}
	public void setAfterChangeMessage(String afterChangeMessage) {
		this.afterChangeMessage = afterChangeMessage;
	}
	public String getAssignorCnName() {
		return assignorCnName;
	}
	public void setAssignorCnName(String assignorCnName) {
		this.assignorCnName = assignorCnName;
	}
	public String getAssignorEnName() {
		return assignorEnName;
	}
	public void setAssignorEnName(String assignorEnName) {
		this.assignorEnName = assignorEnName;
	}
	public String getAssignorCnAdress() {
		return assignorCnAdress;
	}
	public void setAssignorCnAdress(String assignorCnAdress) {
		this.assignorCnAdress = assignorCnAdress;
	}
	public String getAssignorEnAdress() {
		return assignorEnAdress;
	}
	public void setAssignorEnAdress(String assignorEnAdress) {
		this.assignorEnAdress = assignorEnAdress;
	}
	public String getAssignorNationality() {
		return assignorNationality;
	}
	public void setAssignorNationality(String assignorNationality) {
		this.assignorNationality = assignorNationality;
	}
	public String getAssignorCountryAndregion() {
		return assignorCountryAndregion;
	}
	public void setAssignorCountryAndregion(String assignorCountryAndregion) {
		this.assignorCountryAndregion = assignorCountryAndregion;
	}
	public String getAssignorType() {
		return assignorType;
	}
	public void setAssignorType(String assignorType) {
		this.assignorType = assignorType;
	}
	public String getAssignorUploadFileLanguage() {
		return assignorUploadFileLanguage;
	}
	public void setAssignorUploadFileLanguage(String assignorUploadFileLanguage) {
		this.assignorUploadFileLanguage = assignorUploadFileLanguage;
	}
	public String getAssignorCertificateName() {
		return assignorCertificateName;
	}
	public void setAssignorCertificateName(String assignorCertificateName) {
		this.assignorCertificateName = assignorCertificateName;
	}
	public String getAssignorCertificateNumber() {
		return assignorCertificateNumber;
	}
	public void setAssignorCertificateNumber(String assignorCertificateNumber) {
		this.assignorCertificateNumber = assignorCertificateNumber;
	}
	public String getTransferorCnName() {
		return transferorCnName;
	}
	public void setTransferorCnName(String transferorCnName) {
		this.transferorCnName = transferorCnName;
	}
	public String getTransferorEnName() {
		return transferorEnName;
	}
	public void setTransferorEnName(String transferorEnName) {
		this.transferorEnName = transferorEnName;
	}
	public String getTransferorCnAdress() {
		return transferorCnAdress;
	}
	public void setTransferorCnAdress(String transferorCnAdress) {
		this.transferorCnAdress = transferorCnAdress;
	}
	public String getTransferorEnAdress() {
		return transferorEnAdress;
	}
	public void setTransferorEnAdress(String transferorEnAdress) {
		this.transferorEnAdress = transferorEnAdress;
	}
	public String getTransferorNationality() {
		return transferorNationality;
	}
	public void setTransferorNationality(String transferorNationality) {
		this.transferorNationality = transferorNationality;
	}
	public String getTransferorCountryAndregion() {
		return transferorCountryAndregion;
	}
	public void setTransferorCountryAndregion(String transferorCountryAndregion) {
		this.transferorCountryAndregion = transferorCountryAndregion;
	}
	public String getTransferorType() {
		return transferorType;
	}
	public void setTransferorType(String transferorType) {
		this.transferorType = transferorType;
	}
	public String getTransferorUploadFileLanguage() {
		return transferorUploadFileLanguage;
	}
	public void setTransferorUploadFileLanguage(String transferorUploadFileLanguage) {
		this.transferorUploadFileLanguage = transferorUploadFileLanguage;
	}
	public String getTransferorCertificateName() {
		return transferorCertificateName;
	}
	public void setTransferorCertificateName(String transferorCertificateName) {
		this.transferorCertificateName = transferorCertificateName;
	}
	public String getTransferorCertificateNumber() {
		return transferorCertificateNumber;
	}
	public void setTransferorCertificateNumber(String transferorCertificateNumber) {
		this.transferorCertificateNumber = transferorCertificateNumber;
	}
	 

    public String getCaseStatus() {
		return caseStatus;
	}
	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAgenName() {
		return agenName;
	}
	public void setAgenName(String agenName) {
		this.agenName = agenName;
	}
	public String getPreChangeAgencyName() {
		return preChangeAgencyName;
	}
	public void setPreChangeAgencyName(String preChangeAgencyName) {
		this.preChangeAgencyName = preChangeAgencyName;
	}
	public String getIsResend() {
		return isResend;
	}
	public void setIsResend(String isResend) {
		this.isResend = isResend;
	}
	public String getTransfer() {
		return transfer;
	}
	public void setTransfer(String transfer) {
		this.transfer = transfer;
	}
	public Integer getCaseTypeId() {
		return caseTypeId;
	}
	public void setCaseTypeId(Integer caseTypeId) {
		this.caseTypeId = caseTypeId;
	}
	public String getTransferorAttorneyFile() {
		return transferorAttorneyFile;
	}
	public void setTransferorAttorneyFile(String transferorAttorneyFile) {
		this.transferorAttorneyFile = transferorAttorneyFile;
	}
	public String getGqcxNameFile() {
		return gqcxNameFile;
	}
	public void setGqcxNameFile(String gqcxNameFile) {
		this.gqcxNameFile = gqcxNameFile;
	}
	public String getTransferorCertFileCn() {
		return transferorCertFileCn;
	}
	public void setTransferorCertFileCn(String transferorCertFileCn) {
		this.transferorCertFileCn = transferorCertFileCn;
	}
	public String getTransferorCertFileEn() {
		return transferorCertFileEn;
	}
	public void setTransferorCertFileEn(String transferorCertFileEn) {
		this.transferorCertFileEn = transferorCertFileEn;
	}
	public String getTransferorCompanyCertFileCn() {
		return transferorCompanyCertFileCn;
	}
	public void setTransferorCompanyCertFileCn(String transferorCompanyCertFileCn) {
		this.transferorCompanyCertFileCn = transferorCompanyCertFileCn;
	}
	public String getTransferorCompanyCertFileEn() {
		return transferorCompanyCertFileEn;
	}
	public void setTransferorCompanyCertFileEn(String transferorCompanyCertFileEn) {
		this.transferorCompanyCertFileEn = transferorCompanyCertFileEn;
	}
	public String getFlwsNameFile() {
		return flwsNameFile;
	}
	public void setFlwsNameFile(String flwsNameFile) {
		this.flwsNameFile = flwsNameFile;
	}
	public String getGtzqNameFile() {
		return gtzqNameFile;
	}
	public void setGtzqNameFile(String gtzqNameFile) {
		this.gtzqNameFile = gtzqNameFile;
	}
	public String getSfzsFile() {
		return sfzsFile;
	}
	public void setSfzsFile(String sfzsFile) {
		this.sfzsFile = sfzsFile;
	}
	public String getZrhtFile() {
		return zrhtFile;
	}
	public void setZrhtFile(String zrhtFile) {
		this.zrhtFile = zrhtFile;
	}
	
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public Integer getBillStatus() {
		return billStatus;
	}
	public void setBillStatus(Integer billStatus) {
		this.billStatus = billStatus;
	}
	public String getBgCertFileCn() {
		return bgCertFileCn;
	}
	public void setBgCertFileCn(String bgCertFileCn) {
		this.bgCertFileCn = bgCertFileCn;
	}
	public String getBgCertFileEn() {
		return bgCertFileEn;
	}
	public void setBgCertFileEn(String bgCertFileEn) {
		this.bgCertFileEn = bgCertFileEn;
	}
	public String getAssigneeAttorneyFile() {
		return assigneeAttorneyFile;
	}
	public void setAssigneeAttorneyFile(String assigneeAttorneyFile) {
		this.assigneeAttorneyFile = assigneeAttorneyFile;
	}
	public String getAssigneeCertFileCn() {
		return assigneeCertFileCn;
	}
	public void setAssigneeCertFileCn(String assigneeCertFileCn) {
		this.assigneeCertFileCn = assigneeCertFileCn;
	}
	public String getAssigneeCertFileEn() {
		return assigneeCertFileEn;
	}
	public void setAssigneeCertFileEn(String assigneeCertFileEn) {
		this.assigneeCertFileEn = assigneeCertFileEn;
	}
	public String getAssigneeCompanyCertFileCn() {
		return assigneeCompanyCertFileCn;
	}
	public void setAssigneeCompanyCertFileCn(String assigneeCompanyCertFileCn) {
		this.assigneeCompanyCertFileCn = assigneeCompanyCertFileCn;
	}
	public String getAssigneeCompanyCertFileEn() {
		return assigneeCompanyCertFileEn;
	}
	public void setAssigneeCompanyCertFileEn(String assigneeCompanyCertFileEn) {
		this.assigneeCompanyCertFileEn = assigneeCompanyCertFileEn;
	}
	public String getWhoIsApp() {
		return whoIsApp;
	}
	public void setWhoIsApp(String whoIsApp) {
		this.whoIsApp = whoIsApp;
	}
	public Integer getCaseGroup() {
		return caseGroup;
	}
	public void setCaseGroup(Integer caseGroup) {
		this.caseGroup = caseGroup;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getSupplement() {
		return supplement;
	}

	public void setSupplement(Integer supplement) {
		this.supplement = supplement;
	}

	public String getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

	public String getZrCertCode() {
		return zrCertCode;
	}

	public void setZrCertCode(String zrCertCode) {
		this.zrCertCode = zrCertCode;
	}

	public String getSrCertCode() {
		return srCertCode;
	}

	public void setSrCertCode(String srCertCode) {
		this.srCertCode = srCertCode;
	}
	
}