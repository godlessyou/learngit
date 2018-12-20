package com.yootii.bdy.tmcase.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.tmcase.model.TradeMarkCase;

@Component
public class TradeMarkCaseCommonImpl {

	public static Logger logger = Logger
			.getLogger(TradeMarkCaseCommonImpl.class);

	public void setDefaultProperties(TradeMarkCase tradeMarkCase) {

		Integer billStatus=1; // 案件的账单状态，0：已核销，1：未核销		
		String tmType="一般";// 商标类型, 只能是：一般，2：集体，3：证明
		String isDlbz="否";// 是否地理标志， 允许的值分别为：是，否
//		String isAppWithDetectAbility="否";// 是否具备检测能力，允许的值：是，否
		String ifSolidTm="否";// 是否三维标志，允许的值：是，否
		String colorSign="否";// 是否颜色组合，允许的值：是，否
		String tmVoice="否";// 声音商标，允许的值：是，否
		String ifShareTm="否";// 是否共同申请, 允许的值：是，否
		String priorityType="无";// 优先权声明，允许的值：无，在先优先权，展会优先权
		String isLoadPriorityFile="否";// 是否上传优先权证明文件，允许的值：是，否
		String isPortraitAsApp="否";// 以肖像作为商标申请注册，允许的值：是，否
	
//		String changeType=1; // 变更类型
		String uploadFileLanguage="中文"; // 上传文件语言
		String assignorUploadFileLanguage="中文";// 受然人上传文件语言
		String transferorUploadFileLanguage="中文";// 转让人上传文件语言
		String isResend="否";// 商标更正是否需重新制发证书文件
		String appCertFileIsCn="是";
		
		Integer caseTypeId=tradeMarkCase.getCaseTypeId();
		
		tradeMarkCase.setBillStatus(billStatus);		
		tradeMarkCase.setTmType(tmType);
		tradeMarkCase.setIsDlbz(isDlbz);
//		tradeMarkCase.setIsAppWithDetectAbility(isAppWithDetectAbility);
		tradeMarkCase.setIfSolidTm(ifSolidTm);
		tradeMarkCase.setColorSign(colorSign);
		tradeMarkCase.setTmVoice(tmVoice);
		tradeMarkCase.setIfShareTm(ifShareTm);
		tradeMarkCase.setPriorityType(priorityType);
		tradeMarkCase.setIsLoadPriorityFile(isLoadPriorityFile);
		tradeMarkCase.setIsPortraitAsApp(isPortraitAsApp);
//		tradeMarkCase.setChangeType(changeType);
		tradeMarkCase.setUploadFileLanguage(uploadFileLanguage);
		
		tradeMarkCase.setAppCertFileIsCn(appCertFileIsCn);
		
		if (caseTypeId==3){
			tradeMarkCase.setAssignorUploadFileLanguage(assignorUploadFileLanguage);
			tradeMarkCase.setTransferorUploadFileLanguage(transferorUploadFileLanguage);
		}
		tradeMarkCase.setIsResend(isResend);

	}

	// 检查所有案件的通用属性
	private ReturnInfo checkCommonProperties(TradeMarkCase tradeMarkCase) {
		ReturnInfo rtnInfo = new ReturnInfo();

		String agentNum = tradeMarkCase.getAgentNum();
		String agentPerson = tradeMarkCase.getAgentPerson();
		String agenName = tradeMarkCase.getAgenName();
		String appContactPerson = tradeMarkCase.getAppContactPerson();
		
		if (agentNum == null || agentNum.equals("")) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("请填写代理文号");
			return rtnInfo;
		}
		if (agentPerson == null || agentPerson.equals("")) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("请填写代理人名称");
			return rtnInfo;
		}
		if (agenName == null || agenName.equals("")) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("请填写代理机构名称");
			return rtnInfo;
		}

		if (appContactPerson == null || appContactPerson.equals("")) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("请填写联系人");
			return rtnInfo;
		}
		rtnInfo.setSuccess(true);
		return rtnInfo;
	}

	// 检查接收人属性
	private ReturnInfo checkAcceptPerson(TradeMarkCase tradeMarkCase) {
		ReturnInfo rtnInfo = new ReturnInfo();
		//Modification start, 2018-10-31, by yang guang
		//to check accept person for foreign applicant
		String appGJdq=tradeMarkCase.getAppGJdq();
		
		if (appGJdq!=null && !appGJdq.equals("中国大陆")){
			String acceptPerson = tradeMarkCase.getAcceptPerson();
			String acceptPersonAddr = tradeMarkCase.getAcceptPersonAddr();
			String acceptPersonZip = tradeMarkCase.getAcceptPersonZip();
	
			if (acceptPerson == null || acceptPerson.equals("")) {
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("请填写国内接收人");
				return rtnInfo;
			}
			if (acceptPersonAddr == null || acceptPersonAddr.equals("")) {
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("请填写国内接收人");
				return rtnInfo;
			}
			if (acceptPersonZip == null || acceptPersonZip.equals("")) {
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("请填写国内接收人邮政编码");
				return rtnInfo;
			}
			//Modification end
		}
		rtnInfo.setSuccess(true);
		return rtnInfo;
	}

	// 检查除了转让案件之外的其他案件的基本属性
	private ReturnInfo checkBasicProperties(TradeMarkCase tradeMarkCase) {
		ReturnInfo rtnInfo = new ReturnInfo();
				
		String appContactZip = tradeMarkCase.getAppContactZip();
		String appContactTel = tradeMarkCase.getAppContactTel();


		String applicantType = tradeMarkCase.getApplicantType();
		String appGJdq = tradeMarkCase.getAppGJdq();
		String appCountryOrRegion = tradeMarkCase.getAppCountryOrRegion();

		String appCnName = tradeMarkCase.getAppCnName();
		String appEnName = tradeMarkCase.getAppEnName();
		String appCnAddr = tradeMarkCase.getAppCnAddr();
		String appEnAddr = tradeMarkCase.getAppEnAddr();
		
		String certCode = tradeMarkCase.getCertCode();//统一社会信用代码
		Integer caseTypeId = tradeMarkCase.getCaseTypeId();//案件类型id
		
		//Modification start, 2018-11-22
		//检查商标国际分类
		String goodClasses=tradeMarkCase.getGoodClasses();
		
		if (goodClasses == null || goodClasses.equals("")) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("请填写商品/服务");
			return rtnInfo;
		}
		//Modification end

		if (applicantType == null || applicantType.equals("")) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("请填写申请人类型");
			return rtnInfo;
		}
		if (appGJdq == null || appGJdq.equals("")) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("请填写申请人国籍");
			return rtnInfo;
		}
		if (!appGJdq.equals("中国大陆") && (appCountryOrRegion == null || appCountryOrRegion.equals(""))) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("请填写申请人国家或地区");
			return rtnInfo;
		}
		if (appCnName == null || appCnName.equals("")) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("请填写申请人名称");
			return rtnInfo;
		}
		if (appCnAddr == null || appCnAddr.equals("")) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("请填写申请人地址");
			return rtnInfo;
		}
		if (appGJdq.equals("中国大陆")) {
			if (appCnAddr.indexOf("北京") < 0 && appCnAddr.indexOf("上海") < 0
					&& appCnAddr.indexOf("天津") < 0
					&& appCnAddr.indexOf("重庆") < 0) {
//				if (appCnAddr.indexOf("省") < 0
//						|| appCnAddr.indexOf("市") < 0
//						|| (appCnAddr.indexOf("县") < 0 && appCnAddr
//								.indexOf("区") < 0)) {
				if (appCnAddr.indexOf("省") < 0) {
					rtnInfo.setSuccess(false);
					rtnInfo.setMessage("请填写申请人冠有省、市、县/区三级区划的详细地址");
					return rtnInfo;
				}
			}
			if((certCode==null||"".equals(certCode))){
				//注册申请 申请人类型为非自然人、申请人国籍为中国大陆，则统一社会信用代码是必填的
				//续展和变代、变名变址、更正都是申请人国籍为中国大陆，则统一社会信用代码是必填的
				if((caseTypeId==1&&!"自然人".equals(applicantType))||caseTypeId==2||caseTypeId==4||caseTypeId==5||caseTypeId==6){
					rtnInfo.setSuccess(false);
					rtnInfo.setMessage("请填写统一社会信用代码");
					return rtnInfo;
				}
			}
			
			if (appContactTel == null || appContactTel.equals("")) {
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("请填写联系电话");
				return rtnInfo;
			}
			if (appContactZip == null || appContactZip.equals("")) {
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("请填写联系人邮政编码");
				return rtnInfo;
			}

		} else {
			if (appEnName == null || appEnName.equals("")) {
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("请填写申请人英文名称");
				return rtnInfo;
			}
			if (appEnAddr == null || appEnAddr.equals("")) {
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("请填写申请人英文地址");
				return rtnInfo;
			}

		}

		rtnInfo.setSuccess(true);
		return rtnInfo;
	}

	// 检查商标注册案件的证明文件的属性
	private ReturnInfo checkRegCaseCertProperties(TradeMarkCase tradeMarkCase) {
		ReturnInfo rtnInfo = new ReturnInfo();

		String applicantType = tradeMarkCase.getApplicantType();
		String companyCertFileCn = tradeMarkCase.getCompanyCertFileCn();
		String companyCertFileEn = tradeMarkCase.getCompanyCertFileEn();
		String appCertFileCn = tradeMarkCase.getAppCertFileCn();
		String appCertFileEn = tradeMarkCase.getAppCertFileEn();
		String appCertFileIsCn = tradeMarkCase.getAppCertFileIsCn();
		
		if (appCertFileIsCn==null || appCertFileIsCn.equals("")){
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("请选择证明文件是否为中文");
			return rtnInfo;
		}

		if (applicantType.equals("自然人")) {
			if (appCertFileCn == null || appCertFileCn.equals("")) {
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("请填写申请人的身份证明");
				return rtnInfo;
			}

			// 商标注册案件，根据appCertFileIsCn的值进行判断
			if (appCertFileIsCn.equals("否")) {
				if (appCertFileEn == null || appCertFileEn.equals("")) {
					rtnInfo.setSuccess(false);
					rtnInfo.setMessage("请填写申请人的英文身份证明");
					return rtnInfo;
				}
			}

		} else {
			if (companyCertFileCn == null || companyCertFileCn.equals("")) {
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("请填写申请人的主体资格证明");
				return rtnInfo;
			}

			if (appCertFileIsCn.equals("否")) {
				if (companyCertFileEn == null || companyCertFileEn.equals("")) {
					rtnInfo.setSuccess(false);
					rtnInfo.setMessage("请填写申请人的英文主体资格证明");
					return rtnInfo;
				}
			}

		}

		rtnInfo.setSuccess(true);
		return rtnInfo;
	}

	// 检查商标注册案件的属性
	private ReturnInfo checkRegisterCase(TradeMarkCase tradeMarkCase) {
		ReturnInfo rtnInfo = new ReturnInfo();

		rtnInfo = checkBasicProperties(tradeMarkCase);
		if (!rtnInfo.getSuccess()) {
			return rtnInfo;
		}
		

		rtnInfo = checkAcceptPerson(tradeMarkCase);
		if (!rtnInfo.getSuccess()) {
			return rtnInfo;
		}

		rtnInfo = checkRegCaseCertProperties(tradeMarkCase);

		return rtnInfo;
	}

	// 检查转让人的属性
	private ReturnInfo checkTransferProperties(TradeMarkCase tradeMarkCase) {
		ReturnInfo rtnInfo = new ReturnInfo();

		String transferorType = tradeMarkCase.getTransferorType();
		String transferorNationality = tradeMarkCase.getTransferorNationality();
		String transferorCountryAndregion = tradeMarkCase
				.getTransferorCountryAndregion();
		String transferorCnName = tradeMarkCase.getTransferorCnName();
		String transferorEnName = tradeMarkCase.getTransferorEnName();
		String transferorCnAdress = tradeMarkCase.getTransferorCnAdress();
		String transferorEnAdress = tradeMarkCase.getTransferorCnAdress();
		String transferorUploadFileLanguage = tradeMarkCase
				.getTransferorUploadFileLanguage();
		String transferorCertFileCn = tradeMarkCase.getTransferorCertFileCn();
		String transferorCertFileEn = tradeMarkCase.getTransferorCertFileEn();
		String transferorCompanyCertFileCn = tradeMarkCase
				.getTransferorCompanyCertFileCn();
		String transferorCompanyCertFileEn = tradeMarkCase
				.getTransferorCompanyCertFileEn();
		
		String zrCertCode = tradeMarkCase.getZrCertCode();

		if (transferorType == null || transferorType.equals("")) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("请填写转让人类型");
			return rtnInfo;
		}
		if (transferorNationality == null || transferorNationality.equals("")) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("请填写转让人国籍");
			return rtnInfo;
		}
		if (transferorCountryAndregion == null
				|| transferorCountryAndregion.equals("")) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("请填写转让人国家或地区");
			return rtnInfo;
		}
		if (transferorCnName == null || transferorCnName.equals("")) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("请填写转让人名称");
			return rtnInfo;
		}
		if (transferorCnAdress == null || transferorCnAdress.equals("")) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("请填写转让人地址");
			return rtnInfo;
		}

		if (transferorUploadFileLanguage == null
				|| transferorUploadFileLanguage.equals("")) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("请选择转让人上传证明文件的语言");
			return rtnInfo;
		}

		if (transferorType.equals("自然人")) {
			if (transferorCertFileCn == null || transferorCertFileCn.equals("")) {
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("请填写转让人的身份证明");
				return rtnInfo;
			}
			if (!transferorUploadFileLanguage.equals("中文")) {
				if (transferorCertFileEn == null
						|| transferorCertFileEn.equals("")) {
					rtnInfo.setSuccess(false);
					rtnInfo.setMessage("请填写转让人的英文身份证明");
					return rtnInfo;
				}
			}
		} else {
			if (transferorCompanyCertFileCn == null
					|| transferorCompanyCertFileCn.equals("")) {
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("请填写转让人的主体资格证明");
				return rtnInfo;
			}
			if (!transferorUploadFileLanguage.equals("中文")) {
				if (transferorCompanyCertFileEn == null
						|| transferorCompanyCertFileEn.equals("")) {
					rtnInfo.setSuccess(false);
					rtnInfo.setMessage("请填写转让人的英文主体资格证明");
					return rtnInfo;
				}
			}
		}

		if (transferorNationality.equals("中国大陆")) {
			if (transferorCnAdress.indexOf("北京") < 0
					&& transferorCnAdress.indexOf("上海") < 0
					&& transferorCnAdress.indexOf("天津") < 0
					&& transferorCnAdress.indexOf("重庆") < 0) {
//				if (transferorCnAdress.indexOf("省") < 0
//						|| transferorCnAdress.indexOf("市") < 0
//						|| (transferorCnAdress.indexOf("县") < 0 && transferorCnAdress
//								.indexOf("区") < 0)) {
				if (transferorCnAdress.indexOf("省") < 0) {
					rtnInfo.setSuccess(false);
					rtnInfo.setMessage("请填写转让人冠有省、市、县/区三级区划的详细地址");
					return rtnInfo;
				}
			}
			if(zrCertCode==null||"".equals(zrCertCode)){//转让人是中国大陆时，转让人统一社会信用代码为必填
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("请填写转让人统一社会信用代码");
				return rtnInfo;
			}

		} else {
			if (transferorEnName == null || transferorEnName.equals("")) {
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("请填写转让人英文名称");
				return rtnInfo;
			}
			if (transferorEnAdress == null || transferorEnAdress.equals("")) {
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("请填写转让人英文地址");
				return rtnInfo;
			}

		}

		rtnInfo.setSuccess(true);
		return rtnInfo;
	}

	// 检查受让人的属性
	private ReturnInfo checkAssigneeProperties(TradeMarkCase tradeMarkCase) {
		ReturnInfo rtnInfo = new ReturnInfo();

		String assignorType = tradeMarkCase.getAssignorType();
		String assignorNationality = tradeMarkCase.getAssignorNationality();
		String assignorCountryAndregion = tradeMarkCase
				.getAssignorCountryAndregion();
		String assignorCnName = tradeMarkCase.getAssignorCnName();
		String assignorEnName = tradeMarkCase.getAssignorEnName();
		String assignorCnAdress = tradeMarkCase.getAssignorCnAdress();
		String assignorEnAdress = tradeMarkCase.getAssignorCnAdress();
		String assignorUploadFileLanguage = tradeMarkCase
				.getAssignorUploadFileLanguage();
		String assigneeCertFileCn = tradeMarkCase.getAssigneeCertFileCn();
		String assigneeCertFileEn = tradeMarkCase.getAssigneeCertFileEn();
		String assigneeCompanyCertFileCn = tradeMarkCase
				.getAssigneeCompanyCertFileCn();
		String assigneeCompanyCertFileEn = tradeMarkCase
				.getAssigneeCompanyCertFileEn();
		String srCertCode = tradeMarkCase.getSrCertCode();
		if (assignorType == null || assignorType.equals("")) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("请填写受让人类型");
			return rtnInfo;
		}
		if (assignorNationality == null || assignorNationality.equals("")) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("请填写受让人国籍");
			return rtnInfo;
		}
		if (assignorCountryAndregion == null
				|| assignorCountryAndregion.equals("")) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("请填写受让国家或地区");
			return rtnInfo;
		}
		if (assignorCnName == null || assignorCnName.equals("")) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("请填写受让人名称");
			return rtnInfo;
		}
		if (assignorCnAdress == null || assignorCnAdress.equals("")) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("请填写受让人地址");
			return rtnInfo;
		}

		if (assignorUploadFileLanguage == null
				|| assignorUploadFileLanguage.equals("")) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("请选择受让人上传证明文件的语言");
			return rtnInfo;
		}

		if (assignorType.equals("自然人")) {
			if (assigneeCertFileCn == null || assigneeCertFileCn.equals("")) {
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("请填写受让人的身份证明");
				return rtnInfo;
			}
			if (!assignorUploadFileLanguage.equals("中文")) {
				if (assigneeCertFileEn == null || assigneeCertFileEn.equals("")) {
					rtnInfo.setSuccess(false);
					rtnInfo.setMessage("请填写受让人的英文身份证明");
					return rtnInfo;
				}
			}
		} else {
			if (assigneeCompanyCertFileCn == null
					|| assigneeCompanyCertFileCn.equals("")) {
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("请填写受让人的主体资格证明");
				return rtnInfo;
			}
			if (!assignorUploadFileLanguage.equals("中文")) {
				if (assigneeCompanyCertFileEn == null
						|| assigneeCompanyCertFileEn.equals("")) {
					rtnInfo.setSuccess(false);
					rtnInfo.setMessage("请填写受让人的英文主体资格证明");
					return rtnInfo;
				}
			}
		}

		if (assignorNationality.equals("中国大陆")) {
			if (assignorCnAdress.indexOf("北京") < 0
					&& assignorCnAdress.indexOf("上海") < 0
					&& assignorCnAdress.indexOf("天津") < 0
					&& assignorCnAdress.indexOf("重庆") < 0) {
//				if (assignorCnAdress.indexOf("省") < 0
//						|| assignorCnAdress.indexOf("市") < 0
//						|| (assignorCnAdress.indexOf("县") < 0 && assignorCnAdress
//								.indexOf("区") < 0)) {
				if (assignorCnAdress.indexOf("省") < 0) {
					rtnInfo.setSuccess(false);
					rtnInfo.setMessage("请填写受让人冠有省、市、县/区三级区划的详细地址");
					return rtnInfo;
				}
			}
			if(srCertCode==null||"".equals(srCertCode)){//受让人是中国大陆时，受让人统一社会信用代码为必填
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("请填写受让人统一社会信用代码");
				return rtnInfo;
			}
		} else {
			if (assignorEnName == null || assignorEnName.equals("")) {
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("请填写受让人英文名称");
				return rtnInfo;
			}
			if (assignorEnAdress == null || assignorEnAdress.equals("")) {
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("请填写受让人英文地址");
				return rtnInfo;
			}
		}

		rtnInfo.setSuccess(true);
		return rtnInfo;
	}

	// 检查商标转让案件的属性
	private ReturnInfo checkTransferCase(TradeMarkCase tradeMarkCase) {
		ReturnInfo rtnInfo = new ReturnInfo();

		rtnInfo = checkAcceptPerson(tradeMarkCase);
		if (!rtnInfo.getSuccess()) {
			return rtnInfo;
		}

		rtnInfo = checkTransferProperties(tradeMarkCase);
		if (rtnInfo.getSuccess() != true) {
			return rtnInfo;
		}

		rtnInfo = checkAssigneeProperties(tradeMarkCase);

		return rtnInfo;
	}

	// 检查除了商标注册案件之外的其他案件的证明文件的属性
	private ReturnInfo checkOtherCaseCertProperties(TradeMarkCase tradeMarkCase) {
		ReturnInfo rtnInfo = new ReturnInfo();

		String applicantType = tradeMarkCase.getApplicantType();
		String companyCertFileCn = tradeMarkCase.getCompanyCertFileCn();
		String companyCertFileEn = tradeMarkCase.getCompanyCertFileEn();
		String appCertFileCn = tradeMarkCase.getAppCertFileCn();
		String appCertFileEn = tradeMarkCase.getAppCertFileEn();
		String uploadFileLanguage = tradeMarkCase.getUploadFileLanguage();

		if (uploadFileLanguage == null || uploadFileLanguage.equals("")) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("请选择申请人上传证明文件的语言");
			return rtnInfo;
		}

		if (applicantType.equals("自然人")) {
			if (appCertFileCn == null || appCertFileCn.equals("")) {
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("请填写申请人的身份证明");
				return rtnInfo;
			}

			if (!uploadFileLanguage.equals("中文")) {// 除了商标注册案件，都根据uploadFileLanguage的值进行判断
				if (appCertFileEn == null || appCertFileEn.equals("")) {
					rtnInfo.setSuccess(false);
					rtnInfo.setMessage("请填写申请人的英文身份证明");
					return rtnInfo;
				}
			}

		} else {
			if (companyCertFileCn == null || companyCertFileCn.equals("")) {
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("请填写申请人的主体资格证明");
				return rtnInfo;
			}

			if (!uploadFileLanguage.equals("中文")) {
				if (companyCertFileEn == null || companyCertFileEn.equals("")) {
					rtnInfo.setSuccess(false);
					rtnInfo.setMessage("请填写申请人的英文主体资格证明");
					return rtnInfo;
				}
			}

		}

		rtnInfo.setSuccess(true);
		return rtnInfo;
	}

	// 检查商标续展案件的属性
	private ReturnInfo checkXuZhanCase(TradeMarkCase tradeMarkCase) {
		ReturnInfo rtnInfo = new ReturnInfo();
		rtnInfo = checkBasicProperties(tradeMarkCase);
		if (!rtnInfo.getSuccess()) {
			return rtnInfo;
		}
		rtnInfo = checkOtherCaseCertProperties(tradeMarkCase);
		return rtnInfo;
	}

	// 检查变更代理人/文件接收人案件的属性
	private ReturnInfo checkChangAgentOrAcceptCase(TradeMarkCase tradeMarkCase) {
		ReturnInfo rtnInfo = new ReturnInfo();
		rtnInfo = checkBasicProperties(tradeMarkCase);
		if (!rtnInfo.getSuccess()) {
			return rtnInfo;
		}
		rtnInfo = checkOtherCaseCertProperties(tradeMarkCase);
		return rtnInfo;
	}

	// 检查变更名义地址集体管理规则成员名单案件的属性
	private ReturnInfo checkChangApplicantCase(TradeMarkCase tradeMarkCase) {
		ReturnInfo rtnInfo = new ReturnInfo();
		rtnInfo = checkBasicProperties(tradeMarkCase);
		if (!rtnInfo.getSuccess()) {
			return rtnInfo;
		}		
		rtnInfo = checkOtherCaseCertProperties(tradeMarkCase);
		
		String appCertFileIsCn = tradeMarkCase.getAppCertFileIsCn();
		
		if (appCertFileIsCn==null || appCertFileIsCn.equals("")){
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("请选择证明文件是否为中文");
			return rtnInfo;
		}
		return rtnInfo;
	}

	// 检查商标更正案件的属性
	private ReturnInfo checkGengZhengCase(TradeMarkCase tradeMarkCase) {
		ReturnInfo rtnInfo = new ReturnInfo();
		rtnInfo = checkBasicProperties(tradeMarkCase);
		if (!rtnInfo.getSuccess()) {
			return rtnInfo;
		}
		rtnInfo = checkOtherCaseCertProperties(tradeMarkCase);
		return rtnInfo;
	}

	
	//检查异议申请案件属性
	private ReturnInfo checkDissentApplicat(TradeMarkCase tradeMarkCase){
		ReturnInfo returnInfo = new ReturnInfo();
		
		//检查基础属性
		returnInfo = checkBasicProperties(tradeMarkCase);
		if(!returnInfo.getSuccess()){
			return returnInfo;
		}
		//检查除了基础属性之外的 额外属性
		String dissentAddress = tradeMarkCase.getDissentAddress();
		String dissentName = tradeMarkCase.getDissentName();
		String dissentGist = tradeMarkCase.getDissentGist();
		if(dissentAddress==null || dissentAddress.equals("")){
			returnInfo.setSuccess(false);
			returnInfo.setMessage("被异议人地址不能为空");
			return returnInfo;
		}
		if(dissentName == null || dissentName.equals("")){
			returnInfo.setSuccess(false);
			returnInfo.setMessage("被异议人名称不能为空");
			return returnInfo;
		}
		if(dissentGist == null || dissentGist.equals("")){
			returnInfo.setSuccess(false);
			returnInfo.setMessage("异议请求和事实依据不能为空");
			return returnInfo;
		}
		
		return returnInfo;
	}
	
	//检查异议答辩案件属性
	private ReturnInfo checkDissentReply(TradeMarkCase tradeMarkCase){
		ReturnInfo returnInfo = new ReturnInfo();
		
		//检查基础属性
		returnInfo = checkBasicProperties(tradeMarkCase);
		if(!returnInfo.getSuccess()){
			return returnInfo;
		}
		//检查额外属性
		String dissentAddress = tradeMarkCase.getDissentAddress();
		String dissentAgent = tradeMarkCase.getDissentAgent();
		String dissentName = tradeMarkCase.getDissentName();
		if(dissentAddress==null || dissentAddress.equals("")){
			returnInfo.setSuccess(false);
			returnInfo.setMessage("被答辩人地址不能为空");
			return returnInfo;
		}
		if(dissentName == null || dissentName.equals("")){
			returnInfo.setSuccess(false);
			returnInfo.setMessage("被答辩人名称不能为空");
			return returnInfo;
		}
		if(dissentAgent==null || dissentAgent.equals("")){
			returnInfo.setSuccess(true);
			returnInfo.setMessage("被答辩人代理机构不能为空");
			return returnInfo;
		}
		
		return returnInfo;
	}
	
	//检查不予复审案件属性
	private ReturnInfo checkNotRecheck(TradeMarkCase tradeMarkCase){
		ReturnInfo returnInfo = new ReturnInfo();
		//检查基础属性
		returnInfo = checkBasicProperties(tradeMarkCase);
		if(!returnInfo.getSuccess()){
			return returnInfo;
		}
		return returnInfo;
	}
	
	//检查商标无效宣告
	private ReturnInfo checkDisabledDeclare(TradeMarkCase tradeMarkCase){
		ReturnInfo returnInfo = new ReturnInfo();
		returnInfo = checkBasicProperties(tradeMarkCase);
		if(!returnInfo.getSuccess()){
			return returnInfo;
		}
		return returnInfo;
	}
	
	//检查商标撤三申请
	private ReturnInfo checkRepealApplicant(TradeMarkCase tradeMarkCase){
		ReturnInfo returnInfo = new ReturnInfo();
		returnInfo = checkBasicProperties(tradeMarkCase);
		if(!returnInfo.getSuccess()){
			return returnInfo;
		}
		return returnInfo;
	}
	
	//检查商标驳回复审
	private ReturnInfo checkRejectRecheck(TradeMarkCase tradeMarkCase){
		ReturnInfo returnInfo = new ReturnInfo();
		returnInfo = checkBasicProperties(tradeMarkCase);
		if(!returnInfo.getSuccess()){
			return returnInfo;
		}
		return returnInfo;
	}
	//商标撤三答辩
	private ReturnInfo checkRepealReply(TradeMarkCase tradeMarkCase){
		ReturnInfo returnInfo = new ReturnInfo();
		returnInfo = checkBasicProperties(tradeMarkCase);
		if(!returnInfo.getSuccess()){
			return returnInfo;
		}
		return returnInfo;
	}
	//检查商标撤销通用名申请
	private ReturnInfo checkRepealCommonNameApplicant(TradeMarkCase tradeMarkCase){
		ReturnInfo returnInfo = new ReturnInfo();
		returnInfo = checkBasicProperties(tradeMarkCase);
		if(!returnInfo.getSuccess()){
			return returnInfo;
		}
		return returnInfo;
	}
	
	//检查商标撤销通用名称答辩
	private ReturnInfo checkRepealCommonNameReply(TradeMarkCase tradeMarkCase){
		ReturnInfo returnInfo = new ReturnInfo();
		returnInfo = checkBasicProperties(tradeMarkCase);
		if(!returnInfo.getSuccess()){
			return returnInfo;
		}
		return returnInfo;
	}
	
	//检查商标变更申请人名义地址案件
	private ReturnInfo checkChangeApplicantAddr(TradeMarkCase tradeMarkCase){
		ReturnInfo returnInfo = new ReturnInfo();
		returnInfo = checkBasicProperties(tradeMarkCase);
		if(!returnInfo.getSuccess()){
			return returnInfo;
		}
		return returnInfo;
	}
	
	
	
	
	
	// 检查案件的属性
	public ReturnInfo checkTradeMarkCase(TradeMarkCase tradeMarkCase) {
		ReturnInfo rtnInfo = new ReturnInfo();
		if(tradeMarkCase==null){
			rtnInfo.setSuccess(false);
//			rtnInfo.setMessage("caseTypeId不能为null");
			return rtnInfo;
		}
		Integer caseTypeId = tradeMarkCase.getCaseTypeId();

		if (caseTypeId == null) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("caseTypeId不能为null");
			return rtnInfo;
		}

		rtnInfo = checkCommonProperties(tradeMarkCase);
		if (!rtnInfo.getSuccess()) {
			return rtnInfo;
		}

		switch (caseTypeId) {
		case 1: // 商标注册案件
			rtnInfo = checkRegisterCase(tradeMarkCase);
			break;
		case 2:// 商标续展案件
			rtnInfo = checkXuZhanCase(tradeMarkCase);
			break;
		case 3:// 商标转让案件
			rtnInfo = checkTransferCase(tradeMarkCase);
			break;
		case 4:// 变更代理人/文件接收人
			rtnInfo = checkChangAgentOrAcceptCase(tradeMarkCase);
			break;
		case 5:// 变更名义地址集体管理规则成员名单
			rtnInfo = checkChangApplicantCase(tradeMarkCase);
			break;
		case 6:// 商标更正
			rtnInfo = checkGengZhengCase(tradeMarkCase);
			break;
		case 8: //商标异议申请
			rtnInfo = checkDissentApplicat(tradeMarkCase);
			break;
		case 9: //商标异议答辩
			rtnInfo = checkDissentReply(tradeMarkCase);
			break;
		case 10: //商标无效宣告
			rtnInfo = checkDisabledDeclare(tradeMarkCase);
			break;
		case 11: //商标撤三申请
			rtnInfo = checkRepealApplicant(tradeMarkCase);
			break;
		case 12:  //商标驳回复审
			rtnInfo = checkRejectRecheck(tradeMarkCase);
			break;
		case 13: //不予注册复审
			rtnInfo = checkNotRecheck(tradeMarkCase);
			break;
		case 15: //商标撤三答辩
			rtnInfo = checkRepealReply(tradeMarkCase);
			break;
		case 16: //撤销通用名称申请
			rtnInfo = checkRepealCommonNameApplicant(tradeMarkCase);
			break;
		case 17:  //撤销通用名称答辩
			rtnInfo = checkRepealCommonNameReply(tradeMarkCase);
			break;
		case 18:   //变更申请人名义地址案件
			rtnInfo = checkChangeApplicantAddr(tradeMarkCase);
			break;
		default:
			break;
		}

		return rtnInfo;
	}

}
