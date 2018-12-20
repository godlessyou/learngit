package com.yootii.bdy.tmcase.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

public class Trademarkws {
	private Integer caseTypeId;
	private String applicantType;//申请人类型
	private String appGJdq ;//书式类型
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
	private String appRegionalism ;//申请人行政区划
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
	
	private String tmType;//商标类型
	private String isDlbz;//是否地理标志
	private String memberRule;//集体证明商标使用管理规则
	private String memberRuleFile;//集体证明商标使用管理规则附件
	private String memberNamelist;//集体成员名单
	private String memberNamelistFile;//集体成员名单附件
	private String isAppWithDetectAbility;//是否具备检测能力
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
	private String ifSolidTm;//是否三维标志
	private String colorSign;//是否颜色组合
	private String tmVoice;//声音商标
	private String voiceFile;//声音文件
	private String tmDesignDeclare;//商标说明
	
	private String ifShareTm;//是否共同申请
	private List<JointApplicant> joinApps;
	
	private String priorityType;//优先权声明
	private String isLoadPriorityFile;//是否上传优先权证明文件
	private String priorityFile;//优先权证明文件
	private String priorityBaseCrty;//申请、展出国家、地区
	private String priorityAppDate;//申请、展出日期
	private String priorityAppNum;//申请号
	
	private String goodClasses;//所有类别
	private List<Good> goods;//类别
	
	private String imageFile;//图样
	private String blackWhiteFile;//黑白稿
	private String isPortraitAsApp;//以肖像作为商标申请注册
	private String portraitFile;//肖像证明文件
	private String relatedFile;//相关说明文件
	
	private String regNumber;//商标注册号
	
	private String changeType;//变更类型

	private String uploadFileLanguage;//上传文件语言

	private String isChangeAgency;//是否变更代理机构

	private String preChangeCnName;//变更前名义中文

	private String preChangeEnName;//变更前名义英文

	private String preChangeCnAdress;//变更前名义地址中文

	private String preChangeEnAdress;//变更前名义地址英文

	private String beforeChangeMessage;//更正前信息

	private String afterChangeMessage;//更正后信息

	private String assignorCnName;//受让人名称中文

	private String assignorEnName;//受让人名称英文

	private String assignorCnAdress;//受让人名称地址中文

	private String assignorEnAdress;//受让人名称地址英文

	private String assignorNationality;//受让人国籍

	private String assignorCountryAndregion;//受让人国家和地区

	private String assignorType;//受让人类型

	private String assignorUploadFileLanguage;//受让人上传文件语言

	private String assignorCertificateName;//受让人证件名称

	private String assignorCertificateNumber;//受让人证件号码

	private String transferorCnName;//转让人名称中文

	private String transferorEnName;//转让人名称英文

	private String transferorCnAdress;//转让人名称地址中文

	private String transferorEnAdress;//转让人名称地址英文

	private String transferorNationality;//转让人国籍

	private String transferorCountryAndregion;//转让人国家和地区

	private String transferorType;//转让人类型

	private String transferorUploadFileLanguage;//转让人上传文件语言

	private String transferorCertificateName;//转让人证件名称

	private String transferorCertificateNumber;//转让人证件号码

	private String preChangeAgencyName;//变更后代理机构名称
	
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
	
	//2018-12-05增加
	private String zrCertCode;//转让人统一社会信用代码
	private String srCertCode;//受让人统一社会信用代码
	
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
		this.appCnName = appCnName;
	}
	public String getAppEnName() {
		return appEnName;
	}
	public void setAppEnName(String appEnName) {
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
		this.appCnAddr = appCnAddr;
	}
	public String getAppEnAddr() {
		return appEnAddr;
	}
	public void setAppEnAddr(String appEnAddr) {
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
	public List<JointApplicant> getJoinApps() {
		return joinApps;
	}
	public void setJoinApps(List<JointApplicant> joinApps) {
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
	public String getPriorityAppDate() {
		return priorityAppDate;
	}
	public void setPriorityAppDate(String priorityAppDate) {
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
	public List<Good> getGoods() {
		return goods;
	}
	public void setGoods(List<Good> goods) {
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
	public Integer getCaseTypeId() {
		return caseTypeId;
	}
	public void setCaseTypeId(Integer caseTypeId) {
		this.caseTypeId = caseTypeId;
	}
	public String getRegNumber() {
		return regNumber;
	}
	public void setRegNumber(String regNumber) {
		this.regNumber = regNumber;
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
	public String getTransferorAttorneyFile() {
		return transferorAttorneyFile;
	}
	public void setTransferorAttorneyFile(String transferorAttorneyFile) {
		this.transferorAttorneyFile = transferorAttorneyFile;
	}
	public String getAssigneeAttorneyFile() {
		return assigneeAttorneyFile;
	}
	public void setAssigneeAttorneyFile(String assigneeAttorneyFile) {
		this.assigneeAttorneyFile = assigneeAttorneyFile;
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
	public String getAssigneeCertFileCn() {
		return assigneeCertFileCn;
	}
	public void setAssigneeCertFileCn(String assigneeCertFileCn) {
		this.assigneeCertFileCn = assigneeCertFileCn;
	}
	public String getTransferorCertFileEn() {
		return transferorCertFileEn;
	}
	public void setTransferorCertFileEn(String transferorCertFileEn) {
		this.transferorCertFileEn = transferorCertFileEn;
	}
	public String getAssigneeCertFileEn() {
		return assigneeCertFileEn;
	}
	public void setAssigneeCertFileEn(String assigneeCertFileEn) {
		this.assigneeCertFileEn = assigneeCertFileEn;
	}
	public String getTransferorCompanyCertFileCn() {
		return transferorCompanyCertFileCn;
	}
	public void setTransferorCompanyCertFileCn(String transferorCompanyCertFileCn) {
		this.transferorCompanyCertFileCn = transferorCompanyCertFileCn;
	}
	public String getAssigneeCompanyCertFileCn() {
		return assigneeCompanyCertFileCn;
	}
	public void setAssigneeCompanyCertFileCn(String assigneeCompanyCertFileCn) {
		this.assigneeCompanyCertFileCn = assigneeCompanyCertFileCn;
	}
	public String getTransferorCompanyCertFileEn() {
		return transferorCompanyCertFileEn;
	}
	public void setTransferorCompanyCertFileEn(String transferorCompanyCertFileEn) {
		this.transferorCompanyCertFileEn = transferorCompanyCertFileEn;
	}
	public String getAssigneeCompanyCertFileEn() {
		return assigneeCompanyCertFileEn;
	}
	public void setAssigneeCompanyCertFileEn(String assigneeCompanyCertFileEn) {
		this.assigneeCompanyCertFileEn = assigneeCompanyCertFileEn;
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
	@Override
	public String toString() {
		return "Trademarkws [caseTypeId=" + caseTypeId + ", applicantType="
				+ applicantType + ", appGJdq=" + appGJdq + ", agentNum="
				+ agentNum + ", agentPerson=" + agentPerson + ", attorneyFile="
				+ attorneyFile + ", certCode=" + certCode + ", appCnName="
				+ appCnName + ", appEnName=" + appEnName + ", appCertificate="
				+ appCertificate + ", appCertificateNum=" + appCertificateNum
				+ ", appCertFileIsCn=" + appCertFileIsCn + ", appCertFileCn="
				+ appCertFileCn + ", appCertFileEn=" + appCertFileEn
				+ ", companyCertFileCn=" + companyCertFileCn
				+ ", companyCertFileEn=" + companyCertFileEn
				+ ", appRegionalism=" + appRegionalism
				+ ", appCountryOrRegion=" + appCountryOrRegion + ", appCnAddr="
				+ appCnAddr + ", appEnAddr=" + appEnAddr
				+ ", appContactPerson=" + appContactPerson + ", appContactTel="
				+ appContactTel + ", appContaceFax=" + appContaceFax
				+ ", appContactZip=" + appContactZip + ", acceptPerson="
				+ acceptPerson + ", acceptPersonAddr=" + acceptPersonAddr
				+ ", acceptPersonZip=" + acceptPersonZip + ", tmType=" + tmType
				+ ", isDlbz=" + isDlbz + ", memberRule=" + memberRule
				+ ", memberRuleFile=" + memberRuleFile + ", memberNamelist="
				+ memberNamelist + ", memberNamelistFile=" + memberNamelistFile
				+ ", isAppWithDetectAbility=" + isAppWithDetectAbility
				+ ", appInspectionCertFile=" + appInspectionCertFile
				+ ", appZyjcsbFile=" + appZyjcsbFile + ", appZyjsFile="
				+ appZyjsFile + ", appJsryzsFile=" + appJsryzsFile
				+ ", appWtjchtFile=" + appWtjchtFile + ", wtfrFile=" + wtfrFile
				+ ", wtzzFile=" + wtzzFile + ", zyjcsbFile=" + zyjcsbFile
				+ ", zyjsryFile=" + zyjsryFile + ", regionSignFile1="
				+ regionSignFile1 + ", regionSignFile2=" + regionSignFile2
				+ ", regionSignFile3=" + regionSignFile3 + ", regionSignFile4="
				+ regionSignFile4 + ", regionSignFile5=" + regionSignFile5
				+ ", ifSolidTm=" + ifSolidTm + ", colorSign=" + colorSign
				+ ", tmVoice=" + tmVoice + ", voiceFile=" + voiceFile
				+ ", tmDesignDeclare=" + tmDesignDeclare + ", ifShareTm="
				+ ifShareTm + ", joinApps=" + joinApps + ", priorityType="
				+ priorityType + ", isLoadPriorityFile=" + isLoadPriorityFile
				+ ", priorityFile=" + priorityFile + ", priorityBaseCrty="
				+ priorityBaseCrty + ", priorityAppDate=" + priorityAppDate
				+ ", priorityAppNum=" + priorityAppNum + ", goodClasses="
				+ goodClasses + ", goods=" + goods + ", imageFile=" + imageFile
				+ ", blackWhiteFile=" + blackWhiteFile + ", isPortraitAsApp="
				+ isPortraitAsApp + ", portraitFile=" + portraitFile
				+ ", relatedFile=" + relatedFile + ", regNumber=" + regNumber
				+ ", changeType=" + changeType + ", uploadFileLanguage="
				+ uploadFileLanguage + ", isChangeAgency=" + isChangeAgency
				+ ", preChangeCnName=" + preChangeCnName + ", preChangeEnName="
				+ preChangeEnName + ", preChangeCnAdress=" + preChangeCnAdress
				+ ", preChangeEnAdress=" + preChangeEnAdress
				+ ", beforeChangeMessage=" + beforeChangeMessage
				+ ", afterChangeMessage=" + afterChangeMessage
				+ ", assignorCnName=" + assignorCnName + ", assignorEnName="
				+ assignorEnName + ", assignorCnAdress=" + assignorCnAdress
				+ ", assignorEnAdress=" + assignorEnAdress
				+ ", assignorNationality=" + assignorNationality
				+ ", assignorCountryAndregion=" + assignorCountryAndregion
				+ ", assignorType=" + assignorType
				+ ", assignorUploadFileLanguage=" + assignorUploadFileLanguage
				+ ", assignorCertificateName=" + assignorCertificateName
				+ ", assignorCertificateNumber=" + assignorCertificateNumber
				+ ", transferorCnName=" + transferorCnName
				+ ", transferorEnName=" + transferorEnName
				+ ", transferorCnAdress=" + transferorCnAdress
				+ ", transferorEnAdress=" + transferorEnAdress
				+ ", transferorNationality=" + transferorNationality
				+ ", transferorCountryAndregion=" + transferorCountryAndregion
				+ ", transferorType=" + transferorType
				+ ", transferorUploadFileLanguage="
				+ transferorUploadFileLanguage + ", transferorCertificateName="
				+ transferorCertificateName + ", transferorCertificateNumber="
				+ transferorCertificateNumber + ", preChangeAgencyName="
				+ preChangeAgencyName + "]";
	}
	
}
