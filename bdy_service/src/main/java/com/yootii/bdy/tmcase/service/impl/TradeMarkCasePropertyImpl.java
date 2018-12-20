package com.yootii.bdy.tmcase.service.impl;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.yootii.bdy.agency.dao.AgencyMapper;
import com.yootii.bdy.agency.service.AgencyContactService;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.customer.model.Applicant;
import com.yootii.bdy.customer.service.ApplicantService;
import com.yootii.bdy.material.dao.MaterialMapper;
import com.yootii.bdy.material.model.Material;
import com.yootii.bdy.material.service.MaterialService;
import com.yootii.bdy.tmcase.dao.IssuanceNumberMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkAssociationMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseCategoryMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseJoinAppMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseSolrMapper;
import com.yootii.bdy.tmcase.model.IssuanceNumber;
import com.yootii.bdy.tmcase.model.TradeMarkAssociation;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.TradeMarkCaseJoinApp;
import com.yootii.bdy.trademark.model.Trademark;
import com.yootii.bdy.trademark.service.TradeMarkService;
import com.yootii.bdy.util.TradeMarkCaseUtil;




@Component
public class TradeMarkCasePropertyImpl {
	
	private final Logger logger = Logger.getLogger(this.getClass());
	
	
	@Resource
	private TradeMarkService tradeMarkService;

	@Resource
	private TradeMarkCaseSolrMapper tradeMarkCaseSolrMapper;
	
	@Resource
	private ApplicantService applicantService;
	
	@Resource
	private TradeMarkCaseMapper tradeMarkCaseMapper;


	@Resource
	private TradeMarkAssociationMapper tradeMarkAssociationMapper;

	@Resource
	private TradeMarkCaseCategoryMapper tradeMarkCaseCategoryMapper;

	@Resource
	private TradeMarkCaseJoinAppMapper tradeMarkCaseJoinAppMapper;
	
	@Resource
	private MaterialMapper materialMapper;
	
	@Resource
	private AgencyMapper agencyMapper;
	
	@Resource
	private AgencyContactService agencyContactService;
	
	@Resource
	private MaterialService materialService;
	

	@Resource
	private IssuanceNumberMapper issuanceNumberMapper;
	
	
	
	//按照申请人名称从申请人表中获取申请人信息，设置到案件对象中
	public void setApplicantProperty(TradeMarkCase tradeMarkCase, Applicant applicant){
		//
		String applicantType=tradeMarkCase.getApplicantType();
		String appGJdq=tradeMarkCase.getAppGJdq();
		String appCnAddr=tradeMarkCase.getAppCnAddr();
		String appEnAddr=tradeMarkCase.getAppEnAddr();
		String appEnName=tradeMarkCase.getAppEnName();
		String appCnName=tradeMarkCase.getAppCnName();
		String appCountryOrRegion=tradeMarkCase.getAppCountryOrRegion();
		
		Integer caseTypeId=tradeMarkCase.getCaseTypeId();
		//
		String appType=applicant.getAppType();
		String sendType=applicant.getSendType();
		String applicantAddress=applicant.getApplicantAddress();
		String applicantEnAddress=applicant.getApplicantEnAddress();
		String applicantEnName=applicant.getApplicantEnName();
		String applicantCnName=applicant.getApplicantName();	
		
		String country=applicant.getCountry();
			
		
		switch(caseTypeId) {
		case 1:
			if (applicantType==null || applicantType.equals("")){
				if (appType.equals("法人或其他组织") ){//商标注册案件的文字有差别
					appType="法人或其它组织";
				}
				tradeMarkCase.setApplicantType(appType);
			}
			if (appGJdq==null || appGJdq.equals("")){
				tradeMarkCase.setAppGJdq(sendType);
			}				
			if (appCnAddr==null || appCnAddr.equals("")){
				tradeMarkCase.setAppCnAddr(applicantAddress);
			}
			if (appEnAddr==null || appEnAddr.equals("")){
				tradeMarkCase.setAppEnAddr(applicantEnAddress);
			}
			if (appEnName==null || appEnName.equals("")){
				tradeMarkCase.setAppEnName(applicantEnName);
			}
			if (appCnName==null || appCnName.equals("")){
				tradeMarkCase.setAppCnName(applicantCnName);
			}
			if (appCountryOrRegion==null || appCountryOrRegion.equals("")){
				tradeMarkCase.setAppCountryOrRegion(country);
			}
			break;
		case 3:			
			
			if(tradeMarkCase.getTransferorCnName() == null || tradeMarkCase.getTransferorCnName().equals("")){
				tradeMarkCase.setTransferorCnName(applicantCnName);				
			}
			if(tradeMarkCase.getTransferorEnName() == null || tradeMarkCase.getTransferorEnName().equals("")){
				tradeMarkCase.setTransferorEnName(applicantEnName);
			}
			if(tradeMarkCase.getTransferorCnAdress() == null || tradeMarkCase.getTransferorCnAdress().equals("")){
				tradeMarkCase.setTransferorCnAdress(applicantAddress);
			}
			if(tradeMarkCase.getTransferorEnAdress() == null || tradeMarkCase.getTransferorEnAdress().equals("")){
				tradeMarkCase.setTransferorEnAdress(applicantEnAddress);
			}
			
			//Modification start, by yang guang
			//2018-09-03
			if (appCnName==null || appCnName.equals("")){
				tradeMarkCase.setAppCnName(applicantCnName);
			}
			if (appEnName==null || appEnName.equals("")){
				tradeMarkCase.setAppEnName(applicantEnName);
			}
			if (appCnAddr==null || appCnAddr.equals("")){
				tradeMarkCase.setAppCnAddr(applicantAddress);
			}
			if (appEnAddr==null || appEnAddr.equals("")){
				tradeMarkCase.setAppEnAddr(applicantEnAddress);
			}			
			//Modification end
			
			if (applicantType==null || applicantType.equals("")){
				tradeMarkCase.setApplicantType(appType);
			}
			if (appGJdq==null || appGJdq.equals("")){
				tradeMarkCase.setAppGJdq(sendType);
			}
			if (appCountryOrRegion==null || appCountryOrRegion.equals("")){
				tradeMarkCase.setAppCountryOrRegion(country);
			}
			break;
		case 5:
			//Modification start, 2018-07-30
			//如果用户没有填写变更后的申请人信息，那么使用从商标中提取的信息。
			if(tradeMarkCase.getAppCnName() == null || tradeMarkCase.getAppCnName().equals(""))
				tradeMarkCase.setAppCnName(applicantCnName);
			if(tradeMarkCase.getAppEnName() == null || tradeMarkCase.getAppEnName().equals(""))
				tradeMarkCase.setAppEnName(applicantEnName);			
			if(tradeMarkCase.getAppCnAddr() == null || tradeMarkCase.getAppCnAddr().equals(""))
				tradeMarkCase.setAppCnAddr(applicantAddress);
			if(tradeMarkCase.getAppEnAddr() == null || tradeMarkCase.getAppEnAddr().equals(""))
				tradeMarkCase.setAppEnAddr(applicantEnAddress);
			//Modification end
			
			if(tradeMarkCase.getPreChangeCnName() == null || tradeMarkCase.getPreChangeCnName().equals(""))
				tradeMarkCase.setPreChangeCnName(applicantCnName);
			if(tradeMarkCase.getPreChangeEnName() == null || tradeMarkCase.getPreChangeEnName().equals(""))
				tradeMarkCase.setPreChangeEnName(applicantEnName);
			if(tradeMarkCase.getPreChangeCnAdress() == null || tradeMarkCase.getPreChangeCnAdress().equals("")){
				tradeMarkCase.setPreChangeCnAdress(applicantAddress);
//				logger.info("setApplicantProperty tradeMarkCase.setPreChangeCnAdress: "+ applicantAddress);
			}
			if(tradeMarkCase.getPreChangeEnAdress() == null || tradeMarkCase.getPreChangeEnAdress().equals(""))
				tradeMarkCase.setPreChangeEnAdress(applicantEnAddress);
			if (applicantType==null || applicantType.equals("")){
				tradeMarkCase.setApplicantType(appType);
			}
			if (appGJdq==null || appGJdq.equals("")){
				tradeMarkCase.setAppGJdq(sendType);
			}
			if (appCountryOrRegion==null || appCountryOrRegion.equals("")){
				tradeMarkCase.setAppCountryOrRegion(country);
			}
			break;
		case 6:
			if (applicantType==null || applicantType.equals("")){
				if (appType.equals("法人或其他组织") ){//商标注册案件的文字有差别
					appType="法人或其它组织";
				}
				tradeMarkCase.setApplicantType(appType);
			}
			if (applicantType==null || applicantType.equals("")){
				tradeMarkCase.setApplicantType(appType);
			}
			if (appGJdq==null || appGJdq.equals("")){
				tradeMarkCase.setAppGJdq(sendType);
			}				
			if (appCnAddr==null || appCnAddr.equals("")){
				tradeMarkCase.setAppCnAddr(applicantAddress);
			}
			if (appEnAddr==null || appEnAddr.equals("")){
				tradeMarkCase.setAppEnAddr(applicantEnAddress);
			}
			if (appEnName==null || appEnName.equals("")){
				tradeMarkCase.setAppEnName(applicantEnName);
			}
			if (appCnName==null || appCnName.equals("")){
				tradeMarkCase.setAppCnName(applicantCnName);
			}
			if (appCountryOrRegion==null || appCountryOrRegion.equals("")){
				tradeMarkCase.setAppCountryOrRegion(country);
			}
			break;
		default :
			if (applicantType==null || applicantType.equals("")){
				tradeMarkCase.setApplicantType(appType);
			}
			if (appGJdq==null || appGJdq.equals("")){
				tradeMarkCase.setAppGJdq(sendType);
			}				
			if (appCnAddr==null || appCnAddr.equals("")){
				tradeMarkCase.setAppCnAddr(applicantAddress);
			}
			if (appEnAddr==null || appEnAddr.equals("")){
				tradeMarkCase.setAppEnAddr(applicantEnAddress);
			}
			if (appEnName==null || appEnName.equals("")){
				tradeMarkCase.setAppEnName(applicantEnName);
			}
			if (appCnName==null || appCnName.equals("")){
				tradeMarkCase.setAppCnName(applicantCnName);
			}
			if (appCountryOrRegion==null || appCountryOrRegion.equals("")){
				tradeMarkCase.setAppCountryOrRegion(country);
			}
			break;
				
		}
		
	}	
	
	
	
	
	/**
	 * 2018.11.20  添加注释：进行数据转换，把商标信息赋予给案件
	 * @param tradeMarkCase
	 * @param trademark
	 * @return
	 */
	public TradeMarkCase insertTrademarkTotrademarkCase(TradeMarkCase tradeMarkCase, Trademark trademark) {
		
		String imageFilePath=trademark.getImgFilePath();
		String tmName=trademark.getTmName();
		String imageFile=tradeMarkCase.getImageFile();
		String caseTmName=tradeMarkCase.getTmName();
		
		if (imageFile==null || imageFile.equals("")){
			tradeMarkCase.setImageFile(imageFilePath);
		}
		
		if (caseTmName==null || caseTmName.equals("")){
			tradeMarkCase.setTmName(tmName);
		}
		String appCnAddr=tradeMarkCase.getAppCnAddr();
		String appEnAddr=tradeMarkCase.getAppEnAddr();
		String appEnName=tradeMarkCase.getAppEnName();
		String appCnName=tradeMarkCase.getAppCnName();
		
		String applicantCnName=trademark.getApplicantName();
		String applicantEnName=trademark.getApplicantEnName();		
		String applicantAddress=trademark.getApplicantAddress();
		String applicantEnAddress=trademark.getApplicantEnAddress();
		Date approvalDate = trademark.getApprovalDate();
		String approvalNumber = trademark.getApprovalNumber();
		String agent = trademark.getAgent();
		String tmType=trademark.getTmType();
		String regNumber=trademark.getRegNumber();
		Date validStartDate=trademark.getValidStartDate();
		Date validEndDate=trademark.getValidEndDate();
		
		//Modification start, 2018-12-10
		String classify=trademark.getClassify();		
		tradeMarkCase.setTmType(classify);
		//Modification end
		
		switch(tradeMarkCase.getCaseTypeId()) {
		
		case 3: //商标转让
			if(tradeMarkCase.getTransferorCnName() == null || tradeMarkCase.getTransferorCnName().equals("")){				
				tradeMarkCase.setTransferorCnName(appCnName);				
			}
			if(tradeMarkCase.getTransferorEnName() == null || tradeMarkCase.getTransferorEnName().equals("")){				
				tradeMarkCase.setTransferorEnName(appEnName);				
			}
			if(tradeMarkCase.getTransferorCnAdress() == null || tradeMarkCase.getTransferorCnAdress().equals("")){				
				tradeMarkCase.setTransferorCnAdress(appCnAddr);				
			}
			if(tradeMarkCase.getTransferorEnAdress() == null || tradeMarkCase.getTransferorEnAdress().equals("")){				
				tradeMarkCase.setTransferorEnAdress(appEnAddr);				
			}
			
			//Modification start, by yang guang
			//2018-09-03
			if (appCnName==null || appCnName.equals("")){
				tradeMarkCase.setAppCnName(applicantCnName);
			}
			if (appEnName==null || appEnName.equals("")){
				tradeMarkCase.setAppEnName(applicantEnName);
			}
			if (appCnAddr==null || appCnAddr.equals("")){
				tradeMarkCase.setAppCnAddr(applicantAddress);
			}
			if (appEnAddr==null || appEnAddr.equals("")){
				tradeMarkCase.setAppEnAddr(applicantEnAddress);
			}			
			//Modification end
				
			if(tradeMarkCase.getRegNumber() == null || tradeMarkCase.getRegNumber().equals(""))
				tradeMarkCase.setRegNumber(regNumber);
			if(tradeMarkCase.getGoodClasses() == null || tradeMarkCase.getGoodClasses().equals(""))
				tradeMarkCase.setGoodClasses(tmType);
			
			break;
		case 5:       //变更名义地址集体管理规则成员名单
			//Modification start, 2018-07-30
			//如果用户没有填写变更后的申请人信息，那么使用从商标中提取的信息。
			if(tradeMarkCase.getAppCnName() == null || tradeMarkCase.getAppCnName().equals(""))
				tradeMarkCase.setAppCnName(applicantCnName);
			if(tradeMarkCase.getAppEnName() == null || tradeMarkCase.getAppEnName().equals(""))
				tradeMarkCase.setAppEnName(applicantEnName);			
			if(tradeMarkCase.getAppCnAddr() == null || tradeMarkCase.getAppCnAddr().equals(""))
				tradeMarkCase.setAppCnAddr(applicantAddress);
			if(tradeMarkCase.getAppEnAddr() == null || tradeMarkCase.getAppEnAddr().equals(""))
				tradeMarkCase.setAppEnAddr(applicantEnAddress);
			//Modification end
			
			if(tradeMarkCase.getPreChangeCnName() == null || tradeMarkCase.getPreChangeCnName().equals(""))
				tradeMarkCase.setPreChangeCnName(applicantCnName);
			if(tradeMarkCase.getPreChangeEnName() == null || tradeMarkCase.getPreChangeEnName().equals(""))
				tradeMarkCase.setPreChangeEnName(applicantEnName);
			if(tradeMarkCase.getPreChangeCnAdress() == null || tradeMarkCase.getPreChangeCnAdress().equals(""))
				tradeMarkCase.setPreChangeCnAdress(applicantAddress);				
			if(tradeMarkCase.getPreChangeEnAdress() == null || tradeMarkCase.getPreChangeEnAdress().equals(""))
				tradeMarkCase.setPreChangeEnAdress(applicantEnAddress);
			if(tradeMarkCase.getRegNumber() == null || tradeMarkCase.getRegNumber().equals(""))
				tradeMarkCase.setRegNumber(regNumber);
			if(tradeMarkCase.getGoodClasses() == null || tradeMarkCase.getGoodClasses().equals(""))
				tradeMarkCase.setGoodClasses(tmType);
			break;	
		case 8:           //异议申请类型  在此 原商标申请人 变为了现在案件的被异议人
			if(tradeMarkCase.getDissentAddress()==null || tradeMarkCase.getDissentAddress().equals("")){
				tradeMarkCase.setDissentAddress(applicantAddress);
			}
			if(tradeMarkCase.getDissentName() == null || tradeMarkCase.getDissentName().equals("")){
				tradeMarkCase.setDissentName(applicantCnName);;
			}
			if(tradeMarkCase.getGoodClasses() == null || tradeMarkCase.getGoodClasses().equals("")){
				tradeMarkCase.setGoodClasses(tmType);
			}
			if(tradeMarkCase.getApprovalDate() == null){
				tradeMarkCase.setApprovalDate(approvalDate);
			}
			if(tradeMarkCase.getApprovalNumber()==null || tradeMarkCase.getApprovalNumber().equals("")){
				tradeMarkCase.setApprovalNumber(approvalNumber);
			}
			if(tradeMarkCase.getRegNumber() == null || tradeMarkCase.getRegNumber().equals("")){
				tradeMarkCase.setRegNumber(regNumber);
			}
			if(tradeMarkCase.getDissentAgent()==null || tradeMarkCase.getDissentAgent().equals("")){
				tradeMarkCase.setDissentAgent(agent);
			}
			break;
			
		case 11: //商标撤三申请
			if(tradeMarkCase.getRegistrant()==null || tradeMarkCase.getRegistrant().equals("")){
				tradeMarkCase.setRegistrant(applicantCnName);
			}			
			if(tradeMarkCase.getGoodClasses() == null || tradeMarkCase.getGoodClasses().equals("")){
				tradeMarkCase.setGoodClasses(tmType);
			}
			if(tradeMarkCase.getValidStartDate() == null){
				tradeMarkCase.setValidStartDate(validStartDate);
			}
			if(tradeMarkCase.getValidEndDate() == null){
				tradeMarkCase.setValidEndDate(validEndDate);
			}			
			if(tradeMarkCase.getRegNumber() == null || tradeMarkCase.getRegNumber().equals("")){
				tradeMarkCase.setRegNumber(regNumber);
			}
			break;
		case 10:       //商标无效宣告申请
			if(tradeMarkCase.getRespondentName()==null || tradeMarkCase.getRespondentName().equals("")){
				tradeMarkCase.setRespondentName(applicantCnName);
			}
			if(tradeMarkCase.getRespondentAddr() == null || tradeMarkCase.getRespondentAddr().equals("")){
				tradeMarkCase.setRespondentAddr(appCnAddr);
			}
			if(tradeMarkCase.getQuoteTm() == null|| tradeMarkCase.getQuoteTm().equals("") ){
				tradeMarkCase.setQuoteTm(tmName);
			}
			if(tradeMarkCase.getQuoteRegNumber()==null || tradeMarkCase.getQuoteRegNumber().equals("")){
				tradeMarkCase.setQuoteRegNumber(regNumber);
			}
			break;
		default:
			if(tradeMarkCase.getAppCnName() == null || tradeMarkCase.getAppCnName().equals(""))
				tradeMarkCase.setAppCnName(trademark.getApplicantName());
			if(tradeMarkCase.getAppEnName() == null || tradeMarkCase.getAppEnName().equals(""))
				tradeMarkCase.setAppEnName(trademark.getApplicantEnName());
			if(tradeMarkCase.getAppCnAddr() == null || tradeMarkCase.getAppCnAddr().equals(""))
				tradeMarkCase.setAppCnAddr(trademark.getApplicantAddress());
			if(tradeMarkCase.getAppEnAddr() == null || tradeMarkCase.getAppEnAddr().equals(""))
				tradeMarkCase.setAppEnAddr(trademark.getApplicantEnAddress());
			if(tradeMarkCase.getRegNumber() == null || tradeMarkCase.getRegNumber().equals(""))
				tradeMarkCase.setRegNumber(trademark.getRegNumber());
			if(tradeMarkCase.getGoodClasses() == null || tradeMarkCase.getGoodClasses().equals(""))
				tradeMarkCase.setGoodClasses(trademark.getTmType());
			break;
				
		}
		return tradeMarkCase;
	}

	
	
	
	
	
	public TradeMarkCase fulltransferapp(TradeMarkCase tradeMarkCase) {
		if(tradeMarkCase.getWhoIsApp()==null) return tradeMarkCase;
		if(tradeMarkCase.getWhoIsApp()=="商标受让") {			
			tradeMarkCase.setAppCnName(tradeMarkCase.getAssignorCnName());
			tradeMarkCase.setAppEnName(tradeMarkCase.getAssignorEnName());
			tradeMarkCase.setAppCnAddr(tradeMarkCase.getAssignorCnAdress());
			tradeMarkCase.setAppEnAddr(tradeMarkCase.getAssignorEnAdress());
			tradeMarkCase.setAppGJdq(tradeMarkCase.getAssignorNationality());
			tradeMarkCase.setAppCountryOrRegion(tradeMarkCase.getAssignorCountryAndregion());
			tradeMarkCase.setApplicantType(tradeMarkCase.getAssignorType());
			tradeMarkCase.setUploadFileLanguage(tradeMarkCase.getAssignorUploadFileLanguage());
			tradeMarkCase.setAppCertificate(tradeMarkCase.getAssignorCertificateName());
			tradeMarkCase.setAppCertificateNum(tradeMarkCase.getAssignorCertificateNumber());
		
		} else {
			tradeMarkCase.setAppCnName(tradeMarkCase.getTransferorCnName());
			tradeMarkCase.setAppEnName(tradeMarkCase.getAssignorEnName());
			tradeMarkCase.setAppCnAddr(tradeMarkCase.getTransferorCnAdress());
			tradeMarkCase.setAppEnAddr(tradeMarkCase.getTransferorEnAdress());
			tradeMarkCase.setAppGJdq(tradeMarkCase.getTransferorNationality());
			tradeMarkCase.setAppCountryOrRegion(tradeMarkCase.getTransferorCountryAndregion());
			tradeMarkCase.setApplicantType(tradeMarkCase.getTransferorType());
			tradeMarkCase.setUploadFileLanguage(tradeMarkCase.getTransferorUploadFileLanguage());
			tradeMarkCase.setAppCertificate(tradeMarkCase.getTransferorCertificateName());
			tradeMarkCase.setAppCertificateNum(tradeMarkCase.getTransferorCertificateNumber());
		}
		
		
		return tradeMarkCase;
	}


	
	

	
	
	
	
	
	//设置代理所的联系人信息
	public void setAgencyContact(Integer agencyId, Integer custId, GeneralCondition gcon, TradeMarkCase tradeMarkCaseTemp ){
		String tokenID=gcon.getTokenID();		
		List<Map<String, Object>> agencyContactList=agencyContactService.queryAgencyContactById(agencyId.toString(), custId.toString(), tokenID);
		if(agencyContactList!=null && agencyContactList.size()>0){		    	
			Map<String, Object> map= agencyContactList.get(0);
			
			String appContactPerson=(String)map.get("fullname");			
			String appContactTel=(String)map.get("phone");
			String appContactZip=(String)map.get("postcode");
//			String appContaceFax=(String)map.get("fax");
			
			tradeMarkCaseTemp.setAppContactPerson(appContactPerson);
			tradeMarkCaseTemp.setAppContactTel(appContactTel);
			tradeMarkCaseTemp.setAppContactZip(appContactZip);
		    	
		}
	}
	
	
	

	public TradeMarkCase agencyContatc(Integer agencyId,Integer custId, GeneralCondition gcon, TradeMarkCase tradeMarkCase){
		
		String tokenID=gcon.getTokenID();		
		//List<Map<String, Object>> agencyContactList=agencyContactService.queryAgencyContactById(agencyId.toString(), custId.toString(), tokenID);
		
		List<Map<String, Object>> agencyContactList = agencyMapper.selectAgencyContact(custId,agencyId);
		if(agencyContactList!=null && agencyContactList.size()>0){		    	
			Map<String, Object> map= agencyContactList.get(0);
			String appContactPerson=(String)map.get("fullname");			
			String appContactTel=(String)map.get("phone");
			String appContactZip=(String)map.get("postcode");
			
			tradeMarkCase.setAppContactPerson(appContactPerson);
			tradeMarkCase.setAppContactTel(appContactTel);
			tradeMarkCase.setAppContactZip(appContactZip);
		    	
		}
		return tradeMarkCase;
	}
	
	
	
	
	public ReturnInfo tradeMarkCaseAssociate(Integer caseId,
			Integer agencyId,Integer couserId,GeneralCondition gcon) {
		ReturnInfo info = new ReturnInfo();
//		Integer agencyId = tradeMarkCase.getAgencyId();
		if(agencyId==null){
			info.setSuccess(false);
			info.setMessage("代理所ID不能为空");
			return info;
		}
		if(caseId==null){
			info.setSuccess(false);
			info.setMessage("主案件ID不能为空");
			return info;
		}
		if(couserId==null){
			info.setSuccess(false);
			info.setMessage("合作方userId不能为空");
			return info;
		}
		TradeMarkCase tradeMarkCaseOld = tradeMarkCaseMapper.selectByPrimaryKey(caseId);
		if(tradeMarkCaseOld==null){
			info.setSuccess(false);
			info.setMessage("主案件不存在");
			return info;
		}
		Integer coagencyId = tradeMarkCaseOld.getAgencyId();
		Integer custId = tradeMarkCaseOld.getCustId();
		TradeMarkCase tradeMarkCaseTemp = tradeMarkCaseOld;
		tradeMarkCaseTemp.setId(null);//主键ID为空，插入时自动生成
		tradeMarkCaseTemp.setAgentNum(null);//代理文号需要修改
		tradeMarkCaseTemp.setAgencyId(agencyId);
		tradeMarkCaseTemp.setCotag(1);//是合作案件
		tradeMarkCaseTemp.setCoagencyId(coagencyId);//原代理机构Id
		tradeMarkCaseTemp.setCouserId(couserId);//合作方userId
		
		//Modification start, 2017-07-05		
		//设置代理所的联系人信息
		
	    setAgencyContact(agencyId, custId, gcon, tradeMarkCaseTemp );
	    
	    //Modification end
	  //填充转让案件申请人信息
	    tradeMarkCaseTemp = fulltransferapp(tradeMarkCaseTemp);
		tradeMarkCaseMapper.insertSelective(tradeMarkCaseTemp);
		Integer assoCaseId = tradeMarkCaseTemp.getId();
		//更新文号
		String agentNum = TradeMarkCaseUtil.generateAgentNum(assoCaseId);
		TradeMarkCase updateAgentNum = new TradeMarkCase();
		updateAgentNum.setAgentNum(agentNum);
		updateAgentNum.setId(assoCaseId);
		tradeMarkCaseMapper.updateByPrimaryKeySelective(updateAgentNum);
		//插入案件关联表
		TradeMarkAssociation tradeMarkAssociation = new TradeMarkAssociation();
		tradeMarkAssociation.setCaseId(assoCaseId);
		tradeMarkAssociation.setRelatedCaseId(caseId);
		tradeMarkAssociationMapper.insertSelective(tradeMarkAssociation);
		//案件文件关联，不包括共同申请人文件
				//tradeMarkCaseFileMapper.copyTmCaseFileRecord(assoCaseId,caseId);//joinAppId = null
				materialMapper.copyTmCaseFileRecord(assoCaseId,caseId);
				//查询共同申请人文件
				//List<TradeMarkCaseFile> joinAppFiles = tradeMarkCaseFileMapper.selectByCaseId(caseId);//joinAppId 不为null
				TradeMarkCaseJoinApp t= new TradeMarkCaseJoinApp();
				t.setCaseId(caseId);
				List<Material> materialByJoinApp = materialMapper.selectMaterialByJoinApp(t);
				Integer joinAppIdOld =0;
				if(materialByJoinApp!=null&&materialByJoinApp.size()>0){
					for(Material joinAppFileOld : materialByJoinApp){
						
						//Modification start, by yang guang, 2018-07-13
						//关联共同申请人
						//处理字符串获得共同申请人id
						/*
						if(joinAppFileOld.getSubject() != null && joinAppFileOld.getSubject().length()>0) {
							String[] split = joinAppFileOld.getSubject().split("joinAppId : ");
							if(split.length>1) {
								String[] split2 = split[1].split(",");
								if(split2 !=null && split2.length>0 && !"null".equals(split2[0]) ) {
									
									joinAppIdOld=Integer.parseInt(split2[0]);
								}
							}
						}
						*/
						
						joinAppIdOld=joinAppFileOld.getJoinAppId();
						
						//Modification end
						
						//Integer joinAppIdOld = joinAppFileOld.getJoinAppId();
						TradeMarkCaseJoinApp joinAppOld = tradeMarkCaseJoinAppMapper.selectByPrimaryKey(joinAppIdOld);
						if(joinAppOld != null) {
							TradeMarkCaseJoinApp joinAppNew = joinAppOld;
							joinAppNew.setId(null);
							joinAppNew.setAgencyId(agencyId);
							joinAppNew.setCaseId(assoCaseId);
							tradeMarkCaseJoinAppMapper.insertSelective(joinAppNew);
							//关联共同申请人文件
							Integer joinAppIdNewId = joinAppNew.getId();
							/*TradeMarkCaseFile joinAppFileNew = joinAppFileOld;
							joinAppFileNew.setId(null);
							joinAppFileNew.setCaseId(assoCaseId);
							joinAppFileNew.setJoinAppId(joinAppIdNewId);
							joinAppFileNew.setModifyTime(null);
							tradeMarkCaseFileMapper.insertSelective(joinAppFileNew);*/
							Material material = joinAppFileOld;
							material.setCaseId(assoCaseId);
//							String replace ="joinAppId : "+joinAppIdNewId;
//							String string = joinAppFileOld.getSubject().replace( "joinAppId : "+joinAppIdOld,replace);							
//							material.setSubject(string);
							material.setJoinAppId(joinAppIdNewId);
							materialMapper.insertSelective(material);
						}
						
					}
				}
				/*if(joinAppFiles!=null&&joinAppFiles.size()>0){
					for(TradeMarkCaseFile joinAppFileOld : joinAppFiles){
						//关联共同申请人
						Integer joinAppIdOld = joinAppFileOld.getJoinAppId();
						TradeMarkCaseJoinApp joinAppOld = tradeMarkCaseJoinAppMapper.selectByPrimaryKey(joinAppIdOld);
						TradeMarkCaseJoinApp joinAppNew = joinAppOld;
						joinAppNew.setId(null);
						joinAppNew.setAgencyId(agencyId);
						joinAppNew.setCaseId(assoCaseId);
						tradeMarkCaseJoinAppMapper.insertSelective(joinAppNew);
						//关联共同申请人文件
						Integer joinAppIdNewId = joinAppNew.getId();
						TradeMarkCaseFile joinAppFileNew = joinAppFileOld;
						joinAppFileNew.setId(null);
						joinAppFileNew.setCaseId(assoCaseId);
						joinAppFileNew.setJoinAppId(joinAppIdNewId);
						joinAppFileNew.setModifyTime(null);
						tradeMarkCaseFileMapper.insertSelective(joinAppFileNew);
					}
				}*/
		//案件商品和类别关联
		tradeMarkCaseCategoryMapper.copyTmCaseCategoryRecord(assoCaseId,caseId);
		
		//数据库表加标志位
		Integer cotag = 1;//是合作客户
		//如果该客户和代理机构不是合作关系，则把客户和代理所关联
		tradeMarkCaseMapper.insertAgencyCustomer(agencyId,custId,cotag);
		info.setSuccess(true);
		
		//Modification start, 2018-04-05
		Map<String, Integer> data = new HashMap<String, Integer>();
		data.put("assoCaseId",assoCaseId);
		info.setData(data);
		//Modification end, 2018-04-05
		
		info.setMessage("关联成功");
		return info;
	}
	
	
	

	public IssuanceNumber getIssuanceNumber(String caseType, String fileName) {
		return issuanceNumberMapper.selectIssuanceNumber(caseType, fileName);
	}

	
	
	public String getDocNumber(String appNumber, String caseType) {
		String cType = null;
		String fileName = null;
		if (caseType.equals("商标驳回复审")) {
			cType = "商标";
			fileName = "驳回通知";
		} else if (caseType.equals("异议答辩")) {
			cType = "异议";
			fileName = "不予通知书";
		} else if (caseType.equals("商标不予注册复审")) {
			cType = "注册申请";
			fileName = "不予通知书";
		} 
		//发布号
		IssuanceNumber issNum = getIssuanceNumber(cType, fileName);
		String docNumber = issNum.getPrefix().toUpperCase() + appNumber
				+ issNum.getSuffix().toUpperCase();
		if ("是".equals(issNum.getIsOrder())) {
			docNumber = docNumber + "01";
		}

		return docNumber;
	}

		
	
}
