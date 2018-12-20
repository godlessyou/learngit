package com.yootii.bdy.tmcase.service.impl;

import java.util.ArrayList;


import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Component;


import com.yootii.bdy.agency.service.AgencyService;
import com.yootii.bdy.common.Constants;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.customer.model.Applicant;
import com.yootii.bdy.customer.service.ApplicantService;
import com.yootii.bdy.datasyn.service.DataSynService;

import com.yootii.bdy.material.dao.MaterialMapper;
import com.yootii.bdy.material.model.Material;
import com.yootii.bdy.material.service.MaterialService;
import com.yootii.bdy.remind.service.RemindService;
import com.yootii.bdy.solr.SolrData;
import com.yootii.bdy.solr.SolrInfo;
import com.yootii.bdy.solr.SolrSendTrademarkCase;

import com.yootii.bdy.tmcase.dao.TradeMarkAssociationMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseCategoryMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseFileMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseFilePreMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseJoinAppMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCasePreMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseSolrMapper;

import com.yootii.bdy.tmcase.model.IssuanceNumber;
import com.yootii.bdy.tmcase.model.TradeMarkAssociation;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.TradeMarkCaseJoinApp;
import com.yootii.bdy.tmcase.model.TradeMarkCasePre;

import com.yootii.bdy.trademark.model.Trademark;
import com.yootii.bdy.trademark.service.TradeMarkService;
import com.yootii.bdy.util.ObjectUtil;
import com.yootii.bdy.util.TradeMarkCaseUtil;


@Component
public class TradeMarkCaseBaseImpl {
	
	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private SolrInfo solrInfo;

	@Resource
	private TradeMarkCaseSolrMapper tradeMarkCaseSolrMapper;
	
	@Resource
	private TradeMarkCaseMapper tradeMarkCaseMapper;

	@Resource
	private TradeMarkCasePreMapper tradeMarkCasePreMapper;

	@Resource
	private TradeMarkCaseFileMapper tradeMarkCaseFileMapper;

	@Resource
	private TradeMarkCaseFilePreMapper tradeMarkCaseFilePreMapper;

	@Resource
	private TradeMarkAssociationMapper tradeMarkAssociationMapper;

	@Resource
	private TradeMarkCaseCategoryMapper tradeMarkCaseCategoryMapper;

	@Resource
	private TradeMarkCaseJoinAppMapper tradeMarkCaseJoinAppMapper;
	
	@Resource
	private MaterialMapper materialMapper;	
	
	@Resource
	private TradeMarkService tradeMarkService;
	
	@Resource
	private ApplicantService applicantService;
	
	@Resource
	private MaterialService materialService;	
	
	
	@Resource
	private RemindService remindService;
	
	@Resource
	private DataSynService dataSynService;
	
	@Resource
	private AgencyService agencyService;
	
	@Resource
	private	TradeMarkCaseGoodsImpl tradeMarkCaseGoodsImpl;
	
	@Resource
	private	TradeMarkCasePropertyImpl tradeMarkCasePropertyImpl;
	
	@Resource
	private TradeMarkCaseCommonImpl tradeMarkCaseCommonImpl;
	
	
	public ReturnInfo createTradeMarkCase(TradeMarkCase tradeMarkCase, GeneralCondition gcon) {
		ReturnInfo info = new ReturnInfo();
		Integer custId = tradeMarkCase.getCustId();
		Integer agencyId = tradeMarkCase.getAgencyId();
		if(custId==null||agencyId==null){
			info.setSuccess(false);
			info.setMessage("客户和代理所ID不能为空");
			return info;
		}
		String caseType = tradeMarkCase.getCaseType();
		if(caseType==null){
			info.setSuccess(false);
			info.setMessage("案件类型不能为空");
			return info;
		}
		
		
		//Modificaiton start, by yang guang,2018-10-31
		//设置是否补充材料
		if(caseType.equals("商标驳回复审")){
			Integer supplement=1;
			tradeMarkCase.setSupplement(supplement);
			String cType="商标";
			String fileName="驳回通知";
			String appNumber=tradeMarkCase.getAppNumber();
			if (appNumber!=null && !appNumber.equals("")){
				IssuanceNumber issNum = tradeMarkCasePropertyImpl.getIssuanceNumber(cType, fileName);
				String docNumber = issNum.getPrefix().toUpperCase()+appNumber+issNum.getSuffix().toUpperCase();
				if("是".equals(issNum.getIsOrder())){
					docNumber  = docNumber+"01";
				}
				tradeMarkCase.setDocNumber(docNumber);		
			}
		}
		//Modificaiton end
		
		//2018-07-24, added by yang guang
		String tmDesignDeclare=tradeMarkCase.getTmDesignDeclare();
		if (tmDesignDeclare==null || tmDesignDeclare.equals("")){
			tradeMarkCase.setTmDesignDeclare("无");
		}
		
		
		//2018-07-30, added by yang guang
		tradeMarkCaseCommonImpl.setDefaultProperties(tradeMarkCase);
		
		
//		if("商标注册".equals(caseType)){
			processTmRegCase(tradeMarkCase,info, gcon);
//		}else{
//			processTmOtherCase(tradeMarkCase, info);//目前对其他案件没有进行单独处理，以后有需要会增加
//		}
		//Modification start, by yang guang, 2018-04-17
		//将下面的info.setSuccess(true);给屏蔽掉，原因是：
		//processTmRegCase，processTmOtherCase中当执行出问题时，已经将info的success设置为false
		//但在这里又用true给覆盖了，导致实际返回的info中的success结果是错误的。
		//info.setSuccess(true);
		//Modification end
		return info;
	}	

	
	
	public ReturnInfo createTradeMarkCaseByTmNumber(TradeMarkCase tradeMarkCase,String tmNumber,GeneralCondition gcon) {
		List<String> tmNumberlist=new ArrayList<String>();
		tmNumberlist.add(tmNumber);
		List<Applicant> ApplicantList=applicantService.queryApplicantByRegNumbers(tmNumberlist);
		Applicant applicant=null;
		if (ApplicantList!=null && ApplicantList.size()>0){
		  applicant=ApplicantList.get(0);
		}
		ReturnInfo info =createTradeMarkCaseInternal(tradeMarkCase,tmNumber,applicant, gcon);
		return info;
	}
	

	
	
	public ReturnInfo createTradeMarkCaseInternal(TradeMarkCase tradeMarkCase,String tmNumber, Applicant applicant,GeneralCondition gcon) {
		ReturnInfo info = new ReturnInfo();
		try{
			Trademark trademark=tradeMarkService.selectTrademarkbyRenumber(tmNumber,gcon);
		  //设置申请人国籍，申请书式等属性
			if (applicant!=null){			
				tradeMarkCasePropertyImpl.setApplicantProperty(tradeMarkCase, applicant);
			}
		    tradeMarkCase = tradeMarkCasePropertyImpl.insertTrademarkTotrademarkCase(tradeMarkCase,trademark);	
					    
			info = createTradeMarkCase(tradeMarkCase, gcon);
			
			if(info.getSuccess()){
			
				 //根据商标图片，创建案件图片
			    materialService.createMaterialByCase(tradeMarkCase, gcon);
			    
			    Integer id=tradeMarkCase.getId();		    
			    String imageFile=tradeMarkCase.getImageFile();	
			    if (imageFile!=null && !imageFile.equals("")){
				    TradeMarkCase tmCase=new TradeMarkCase();
				    tmCase.setId(id);
				    tmCase.setImageFile(imageFile);			    
				    tradeMarkCaseMapper.updateByPrimaryKeySelective(tradeMarkCase);
			    }
			}
			
			
			//更新solr中的案件数据
			//updateSolrTradeMarkCase();
		    
		}catch(Exception e){
			e.printStackTrace();
			info.setSuccess(false);      
			info.setMessage("注册失败");
			e.printStackTrace();
		}
		return info;
	}
	
	
	
	
	public ReturnInfo createTradeMarkCaseByAppName(TradeMarkCase tradeMarkCase, GeneralCondition gcon) {
		ReturnInfo info = new ReturnInfo();
		try{
			tradeMarkCase.setCaseGroup(tradeMarkCaseMapper.getMaxCaseGroup());
			Trademark trademark=new Trademark();
			trademark.setApplicantName(tradeMarkCase.getPreChangeCnName());
//			GeneralCondition gcon = new GeneralCondition();
//			gcon.setTokenID(Globals.getToken().getTokenID());
			gcon.setRows(10000);
			
			//Modification start, by yang guang, 2018-12-10
			//to resolve BDY-745
			//flag设置为0，查询全部商标（包括：国际，国内）
			//List<Trademark> trademarklist =(List<Trademark>) tradeMarkService.queryTrademarkBySolr(tradeMarkCase.getCustId(),null, null, trademark, null, null, gcon,true,2).getData();
			List<Trademark> trademarklist =(List<Trademark>) tradeMarkService.queryTrademarkBySolr(tradeMarkCase.getCustId(),null, null, trademark, null, null, gcon,true,0).getData();
			//Modification end
			
			if(trademarklist.size()>0) {
				Map<String,List> data = new HashMap<String,List>();
				List<Map<String, Integer>> successData = new ArrayList<Map<String, Integer>>();
				List<Map<String, String>> failsData = new ArrayList<Map<String, String>>();
				
				List<String> tmNumberlist=new ArrayList<String>();
				List<Applicant> ApplicantList = null;	
				for(Trademark tm:trademarklist) {
					String tmNumber=tm.getRegNumber();
					if (tmNumber!=null && !tmNumber.equals("")){						
						tmNumberlist.add(tmNumber);										 
					}
				}
				//因为创建案件时，会保存申请人信息，就会把变更前的申请人信息覆盖掉
                //因此，在创建案件之前，进行申请人信息的查询
				ApplicantList=applicantService.queryApplicantByRegNumbers(tmNumberlist);	
				
				for(Trademark tm:trademarklist) {
					TradeMarkCase tradeMarkCaseNew = new TradeMarkCase();
					ObjectUtil.setProperty(tradeMarkCase,tradeMarkCaseNew);					
					String tmNumber=tm.getRegNumber();					
					if(ApplicantList!=null){
					    //设置申请人国籍，申请书式等属性
					    for(Applicant applicant: ApplicantList){
					    	List<Trademark> list=applicant.getTrademarks();
					    	boolean find=false;
					    	for(Trademark tradeMark: list){
					    		String regNumber=tradeMark.getRegNumber();
					    		if (regNumber!=null && tmNumber.equals(regNumber)){
					    			find=true;
					    			break;
					    		}
					    	}
					    	if (find){
					    		tradeMarkCasePropertyImpl.setApplicantProperty(tradeMarkCaseNew, applicant);
					    		break;
					    	}
					    }
				    }
					
					tradeMarkCaseNew = tradeMarkCasePropertyImpl.insertTrademarkTotrademarkCase(tradeMarkCaseNew,tm);
										
					ReturnInfo info1 = createTradeMarkCase(tradeMarkCaseNew, gcon);
					
					if(info1 != null && info1.getSuccess()) {
						successData.add((Map<String, Integer>)info1.getData());					
					} else {
						Map<String, String> fails = new HashMap<String, String>();
						fails.put("tmNumber", tm.getRegNumber());
						if(info1 != null)fails.put("reason", info1.getMessage());
						failsData.add(fails);
					}
				}
				data.put("success", successData);
				data.put("fails", failsData);
				info.setData(data);
				if (successData.size()>0){
					info.setSuccess(true);
					info.setMessage("批量创建案件完成");				
				}else{
					info.setSuccess(false);
					info.setMessage("存在不能创建案件的商标号");
				}
				
			}else {
				throw new Exception("商标号查询失败");
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
			info.setSuccess(false);
			info.setMessage("案件创建失败");
			e.printStackTrace();
		}
		return info;
	}
	
	
	
	
	public ReturnInfo createTradeMarkCaseByTmNumberList(TradeMarkCase tradeMarkCase,List<String> tmNumberlist,GeneralCondition gcon) {
		ReturnInfo info = new ReturnInfo();
		try{
			tradeMarkCase.setCaseGroup(tradeMarkCaseMapper.getMaxCaseGroup());
			Map<String,List> data = new HashMap<String,List>();
			List<Map<String, Integer>> successData = new ArrayList<Map<String, Integer>>();
			List<Map<String, String>> failsData = new ArrayList<Map<String, String>>();
					
		
			if (tmNumberlist!=null && tmNumberlist.size()>0){
				//按照商标号获取对应的申请人
			   List<Applicant> ApplicantList = applicantService.queryApplicantByRegNumbers(tmNumberlist);
			   
			   for(String tmNumber:tmNumberlist) {
				   //2018-07-20, added by yang guang
				   TradeMarkCase tradeMarkCaseNew = new TradeMarkCase();
				   ObjectUtil.setProperty(tradeMarkCase,tradeMarkCaseNew);
				   
				   Applicant applicant1 = null;
				   if(ApplicantList!=null){
					    //设置申请人国籍，申请书式等属性
					    for(Applicant applicant: ApplicantList){
					    	List<Trademark> list=applicant.getTrademarks();
					    	boolean find=false;
					    	for(Trademark tradeMark: list){
					    		String regNumber=tradeMark.getRegNumber();
					    		if (regNumber!=null && tmNumber.equals(regNumber)){
					    			find=true;
					    			applicant1= applicant;
					    			break;
					    		}
					    	}
					    	if (find){
					    		break;
					    	}					    	
					    }
				    }
				   
					ReturnInfo info1 = createTradeMarkCaseInternal(tradeMarkCaseNew,tmNumber, applicant1, gcon);
					if(info1 != null && info1.getSuccess()) {
						successData.add((Map<String, Integer>)info1.getData());					
					} else {
						Map<String, String> fails = new HashMap<String, String>();
						fails.put("tmNumber", tmNumber);
						if(info1 != null)fails.put("reason", info1.getMessage());
						failsData.add(fails);
					}
				}
			}	
			
			
			//更新solr中的案件数据
			updateSolrTradeMarkCase();
			
			data.put("success", successData);
			data.put("fails", failsData);
			info.setData(data);
			
			if (successData.size()>0){
				info.setSuccess(true);
				info.setMessage("批量创建案件完成");				
			}else{
				info.setSuccess(false);
				info.setMessage("存在不能创建案件的商标号");
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
			info.setSuccess(false);
			info.setMessage("注册失败");
		}
		return info;
	}
		
	
	
	
	
	
	
	private void processTmRegCase(TradeMarkCase tradeMarkCase,ReturnInfo info, GeneralCondition gcon){
		Integer custId = tradeMarkCase.getCustId();
		Integer agencyId = tradeMarkCase.getAgencyId();	
		try{
			TradeMarkCase tradeMarkCaseTemp = new TradeMarkCase();//用于更新图样路径和文号
			TradeMarkCasePre tradeMarkCasePreTemp = tradeMarkCasePreMapper.selectByCustIdAndAgencyId(custId, agencyId);
			List<Material> materials = null;
			/*if(tradeMarkCasePreTemp!=null){
				 materials  =	materialMapper.selectImageByCaseId(tradeMarkCasePreTemp.getId());
			}*/
			/*if(tradeMarkCasePreTemp==null||tradeMarkCasePreTemp.getImageFile()==null){
				info.setSuccess(false);
				info.setMessage("请上传商标图样");
				return ;
			}*/
			
			/*if(materials==null||materials.size()<1){
				info.setSuccess(false);
				info.setMessage("请上传商标图样");
				return ;
			}*/
			//Material material = materials.get(materials.size()-1);
			//商标图样
			if(tradeMarkCasePreTemp!=null){
				tradeMarkCase.setImageFile(tradeMarkCasePreTemp.getImageFile());
			}
			//申请人信息保存
			saveApplicantByTm(tradeMarkCase, gcon);
			
			tradeMarkCase.setId(null);//避免传入预立案Id
			//填充转让案件申请人信息
			tradeMarkCase = tradeMarkCasePropertyImpl.fulltransferapp(tradeMarkCase);
			tradeMarkCaseMapper.insertSelective(tradeMarkCase);

			//将共同申请人的caseId设置成正式案件的Id
			//List<TradeMarkCaseJoinApp> joinApps = tradeMarkCaseJoinAppMapper.selectByCustIdAndAgencyId(custId, agencyId);
			if(tradeMarkCasePreTemp!=null){
				List<TradeMarkCaseJoinApp> joinApps = tradeMarkCaseJoinAppMapper.selectByCasePreId(tradeMarkCasePreTemp.getId());
				if(joinApps!=null&&joinApps.size()>0){
					for(TradeMarkCaseJoinApp joinApp : joinApps){
						joinApp.setCaseId(tradeMarkCase.getId());
						//保存共同申请人
						saveApplicantByJoinApp(joinApp,tradeMarkCase.getCustId(), gcon);
						tradeMarkCaseJoinAppMapper.updateByPrimaryKeySelective(joinApp);
					}
				}
			}
			
			//tradeMarkCaseTemp.setImageFile(tradeMarkCasePreTemp.getImageFile());//查询预立案中，图样路径更新到正式案件中
			//tradeMarkCaseTemp.setImageFile(material.getAddress());
			//将预立案文件转成正式文件
			/*List<TradeMarkCaseFilePre> tratradeMarkCaseFilePres = tradeMarkCaseFilePreMapper.selectByCustIdAndAgencyId(custId,agencyId);
			if(tratradeMarkCaseFilePres.size()>0){
				for(TradeMarkCaseFilePre tmFilePre : tratradeMarkCaseFilePres){
					TradeMarkCaseFile record = new TradeMarkCaseFile();
					record.setCaseId(tradeMarkCase.getId());
					record.setFileName(tmFilePre.getFileName());
					record.setFileType(tmFilePre.getFileType());
					record.setFileUrl(tmFilePre.getFileUrl());
					record.setUsername(tmFilePre.getUsername());
					//				record.setModifyTime(tmFilePre.getModifyTime());
					record.setJoinAppId(tmFilePre.getJoinAppId());
					tradeMarkCaseFileMapper.insertSelective(record);
				}
				//删除预立案文件
				tradeMarkCaseFilePreMapper.deleteByCustIdAndAgencyId(custId,agencyId);
			}
			 */
			//将预立案文件转成正式文件
			List<Material> materialss = materialMapper.selectByCustIdAndAgencyId(custId,agencyId);
//			System.out.println(materialss);
			if(materialss.size()>0) {
				for(Material m: materialss) {
					if(tradeMarkCase.getId()!=null) {
						m.setCaseId(tradeMarkCase.getId());
						//2018-07-24 added
						m.setPrecase(2);
					}
					
//					m.setSubject(processSubject(m.getSubject()));
					materialMapper.updateByPrimaryKey(m);
				}
			}
			//删除预立案中信息
			if(tradeMarkCasePreTemp!=null){
				//删除预立案商品服务
				tradeMarkCaseCategoryMapper.deleteByCasePreId(tradeMarkCasePreTemp.getId());
			}
			tradeMarkCasePreMapper.deleteByCustIdAndAgencyId(custId,agencyId);
			
			Integer caseId = tradeMarkCase.getId();
			String agentNum =TradeMarkCaseUtil.generateAgentNum(caseId);
			tradeMarkCaseTemp.setId(caseId);
			tradeMarkCaseTemp.setAgentNum(agentNum);
			tradeMarkCaseMapper.updateByPrimaryKeySelective(tradeMarkCaseTemp);

			//添加商品和服务
			tradeMarkCaseGoodsImpl.addGoods(tradeMarkCase);

			info.setSuccess(true);
			Map<String, Integer> data = new HashMap<String, Integer>();
			data.put("caseId",caseId);

			//add for trademark register process start, 2018-03-07 
			data.put("agencyId", agencyId);
			//add end

			info.setData(data);//刚插入的记录，自增长的id已存在
			info.setMessage(tradeMarkCase.getCaseType()+"立案成功");
			
			//Modification start, 2018-10-17
			//支持将案件信息同步到wpm 				
			if(Constants.DataSyn){
				Integer whdId=agencyService.getWhdAgencyId();
				if(agencyId!=null && whdId!=null && agencyId.intValue()==whdId.intValue()){
					Integer type=1;
					//将案件信息同步到wpm		
					dataSynService.caseDataSyn(gcon, caseId.toString(), type);
				}
			}
			//Modification end
			
			
		}catch(Exception e){
			e.printStackTrace();
			info.setSuccess(false);
			info.setMessage("注册失败");
			e.printStackTrace();
		}
	}
	
	
	
	
	

	private void saveApplicantByJoinApp(TradeMarkCaseJoinApp joinApp, Integer custId, GeneralCondition gcon) {
		
		TradeMarkCase tradeMarkCase = new TradeMarkCase();
		tradeMarkCase.setCustId(custId);
		
		tradeMarkCase.setAppCnAddr(joinApp.getAddrCn());
		tradeMarkCase.setAppEnAddr(joinApp.getAddrEn());
		tradeMarkCase.setAppCnName(joinApp.getNameCn());
		tradeMarkCase.setAppEnName(joinApp.getNameEn());
		tradeMarkCase.setApplicantType(joinApp.getJoinAppType());
		tradeMarkCase.setAppGJdq(joinApp.getJoinAppCoun());			
		
		saveApplicantByTm(tradeMarkCase, gcon);
		
	}
	
	
	
	private void saveApplicantByTm(TradeMarkCase tradeMarkCase, GeneralCondition gcon) {
		Applicant applicant = new Applicant();
		
//		logger.info("saveApplicantByTm applicantAddress: "+tradeMarkCase.getAppCnAddr());		
		
		applicant.setApplicantAddress(tradeMarkCase.getAppCnAddr());
		applicant.setApplicantEnAddress(tradeMarkCase.getAppEnAddr());
		
		applicant.setApplicantEnName(tradeMarkCase.getAppEnName());
		applicant.setApplicantName(tradeMarkCase.getAppCnName());
		applicant.setCountry(tradeMarkCase.getAppCountryOrRegion());
		Integer caseTypeId=tradeMarkCase.getCaseTypeId();
		String applicantType=tradeMarkCase.getApplicantType();
		
		//数据库中统一保存为 法人或其他组织
		if (caseTypeId!=null){
			if (caseTypeId==1 || caseTypeId==6){
				if (applicantType!=null && applicantType!=null && applicantType.equals("法人或其它组织") ){//商标注册，商标更正案件的文字有差别
					applicantType="法人或其他组织";
				}	
			}
		}
		
		applicant.setAppType(applicantType);
		applicant.setCardName(tradeMarkCase.getAppCertificate());
		applicant.setCardNumber(tradeMarkCase.getAppCertificateNum());
		applicant.setSendType(tradeMarkCase.getAppGJdq());
		applicant.setUnifiedNumber(tradeMarkCase.getCertCode());
		
		if(applicant.getApplicantName()!=null) {
			if(!applicant.getApplicantName().equals("")) {
				applicantService.checkAndSaveApplicant(gcon, applicant, tradeMarkCase.getCustId());
			}
		}		
		
	}
	

	private void updateJoinApps(TradeMarkCase tradeMarkCase, GeneralCondition gcon){
		List<TradeMarkCaseJoinApp> joinApps = tradeMarkCase.getJoinApps();
		if(joinApps!=null && joinApps.size()>0){
			Integer caseId=tradeMarkCase.getId();
			tradeMarkCaseJoinAppMapper.deleteByCaseId(caseId);
			for(TradeMarkCaseJoinApp joinApp : joinApps){				
				joinApp.setCaseId(caseId);
				//保存共同申请人
				saveApplicantByJoinApp(joinApp,tradeMarkCase.getCustId(), gcon);
				tradeMarkCaseJoinAppMapper.insertSelective(joinApp);
			}
		}
	}
	
	

	private void processTmOtherCase(TradeMarkCase tradeMarkCase,ReturnInfo info){
		Integer custId = tradeMarkCase.getCustId();
		Integer agencyId = tradeMarkCase.getAgencyId();
		try{
			TradeMarkCase tradeMarkCaseTemp = new TradeMarkCase();//用于更新图样路径和文号
			TradeMarkCasePre tradeMarkCasePreTemp = tradeMarkCasePreMapper.selectByCustIdAndAgencyId(custId, agencyId);
			List<Material> materials = null;
			if(tradeMarkCasePreTemp!=null){
				 materials  =	materialMapper.selectImageByCaseId(tradeMarkCasePreTemp.getId());
			}
			/*if(tradeMarkCasePreTemp==null||tradeMarkCasePreTemp.getImageFile()==null){
				info.setSuccess(false);
				info.setMessage("请上传商标图样");
				return ;
			}*/
			
			/*if(materials==null||materials.size()<1){
				info.setSuccess(false);
				info.setMessage("请上传商标图样");
				return ;
			}*/
			//Material material = materials.get(materials.size()-1);
			
			//填充转让案件申请人信息
			tradeMarkCase = tradeMarkCasePropertyImpl.fulltransferapp(tradeMarkCase);
			tradeMarkCaseMapper.insertSelective(tradeMarkCase);

			//将共同申请人的caseId设置成正式案件的Id
			//List<TradeMarkCaseJoinApp> joinApps = tradeMarkCaseJoinAppMapper.selectByCustIdAndAgencyId(custId, agencyId);
			if(tradeMarkCasePreTemp!=null){
				List<TradeMarkCaseJoinApp> joinApps = tradeMarkCaseJoinAppMapper.selectByCasePreId(tradeMarkCasePreTemp.getId());
				if(joinApps!=null&&joinApps.size()>0){
					for(TradeMarkCaseJoinApp joinApp : joinApps){
						joinApp.setCaseId(tradeMarkCase.getId());
						tradeMarkCaseJoinAppMapper.updateByPrimaryKeySelective(joinApp);
					}
				}
			}
			
			//tradeMarkCaseTemp.setImageFile(tradeMarkCasePreTemp.getImageFile());//查询预立案中，图样路径更新到正式案件中
			//tradeMarkCaseTemp.setImageFile(material.getAddress());
			//将预立案文件转成正式文件
			/*List<TradeMarkCaseFilePre> tratradeMarkCaseFilePres = tradeMarkCaseFilePreMapper.selectByCustIdAndAgencyId(custId,agencyId);
			if(tratradeMarkCaseFilePres.size()>0){
				for(TradeMarkCaseFilePre tmFilePre : tratradeMarkCaseFilePres){
					TradeMarkCaseFile record = new TradeMarkCaseFile();
					record.setCaseId(tradeMarkCase.getId());
					record.setFileName(tmFilePre.getFileName());
					record.setFileType(tmFilePre.getFileType());
					record.setFileUrl(tmFilePre.getFileUrl());
					record.setUsername(tmFilePre.getUsername());
					//				record.setModifyTime(tmFilePre.getModifyTime());
					record.setJoinAppId(tmFilePre.getJoinAppId());
					tradeMarkCaseFileMapper.insertSelective(record);
				}
				//删除预立案文件
				tradeMarkCaseFilePreMapper.deleteByCustIdAndAgencyId(custId,agencyId);
			}
			 */
			//将预立案文件转成正式文件
			List<Material> materialss = materialMapper.selectByCustIdAndAgencyId(custId,agencyId);
			//System.out.println(materialss);
			if(materialss.size()>0) {
				for(Material m: materialss) {
					if(tradeMarkCase.getId()!=null) {
						m.setCaseId(tradeMarkCase.getId());
					}
					
//					m.setSubject(processSubject(m.getSubject()));
					materialMapper.updateByPrimaryKey(m);
				}
			}
			//删除预立案中信息
			tradeMarkCasePreMapper.deleteByCustIdAndAgencyId(custId,agencyId);
			Integer caseId = tradeMarkCase.getId();

			String agentNum =TradeMarkCaseUtil.generateAgentNum(caseId);
			tradeMarkCaseTemp.setId(caseId);
			tradeMarkCaseTemp.setAgentNum(agentNum);
			tradeMarkCaseMapper.updateByPrimaryKeySelective(tradeMarkCaseTemp);

			//添加商品和服务
			tradeMarkCaseGoodsImpl.addGoods(tradeMarkCase);

			info.setSuccess(true);
			Map<String, Integer> data = new HashMap<String, Integer>();
			data.put("caseId",caseId);

			//add for trademark register process start, 2018-03-07 
			data.put("agencyId", agencyId);
			//add end

			info.setData(data);//刚插入的记录，自增长的id已存在
			info.setMessage("商标注册成功");
		}catch(Exception e){
			info.setSuccess(false);
			info.setMessage("案件创建失败");
			e.printStackTrace();
		}

	}
	//更新异议答辩文件的收文日期
	private void updateMaterial(TradeMarkCase tradeMarkCase){
		Integer caseId=tradeMarkCase.getId();
		if(tradeMarkCase.getReplyReceiveDate() != null){
			Map<String, Object> map = new HashMap<>();
			Map<String, Object> replyDoc = tradeMarkCaseMapper.selectMaterialForReply(caseId);
			Integer materialId = (Integer)replyDoc.get("materialId");
			map.put("createTime", tradeMarkCase.getReplyReceiveDate());
			map.put("materialId", materialId);
			tradeMarkCaseMapper.updateMaterialForReply(map);
		}
	}
	
	
	
	
	
	public ReturnInfo modifyTradeMarkCase(TradeMarkCase tradeMarkCase, GeneralCondition gcon) {
		ReturnInfo info = new ReturnInfo();
		
		Integer caseId=tradeMarkCase.getId();
		
		if(caseId==null){
			info.setSuccess(false);
			info.setMessage("案件ID不能为空");
			return info;
		}
		this.updateMaterial(tradeMarkCase);
		tradeMarkCase = tradeMarkCasePropertyImpl.fulltransferapp(tradeMarkCase);
		tradeMarkCaseMapper.updateByPrimaryKeySelective(tradeMarkCase);
		if(tradeMarkCase.getIfShareTm()!=null&&tradeMarkCase.getIfShareTm().equals("否")){
			tradeMarkCaseJoinAppMapper.deleteByCaseId(caseId);
		}
		TradeMarkCase tradeMarkCase11 = tradeMarkCaseMapper.selectByPrimaryKey(caseId);
		tradeMarkCase.setCustId(tradeMarkCase11.getCustId());
		tradeMarkCase.setAppCnName(tradeMarkCase11.getAppCnName());
		//申请人信息保存
		saveApplicantByTm(tradeMarkCase, gcon);
		
		//Modification start, 2018-04-18
		//in order to update goods and update joinApps
		//更新商品和服务
		tradeMarkCaseGoodsImpl.updateGoods(tradeMarkCase);
		
		//更新共同申请人信息
		updateJoinApps(tradeMarkCase, gcon);
		
		//Modification end
		
		//Modification start, 2018-10-17
		//支持将案件信息同步到wpm  
		if(Constants.DataSyn){
			Integer agencyId=tradeMarkCase.getAgencyId();
			//获取北京万慧达知识产权代理有限公司的agencyId			
			Integer whdId=agencyService.getWhdAgencyId();
			if(agencyId!=null && whdId!=null && agencyId.intValue()==whdId.intValue()){
				Integer type=1;				
				dataSynService.caseDataSyn(gcon, caseId.toString(), type);
			}
		}
		//Modification end
		
		
		
		//判断修改是否涉及到了优先权问题    修改优先权时，如果只修改了展出日期，则前端不会再传 prioritType 字段
		String priority = tradeMarkCase.getPriorityType();
		Integer custId = tradeMarkCase11.getCustId();
	
		Date date = tradeMarkCase.getPriorityAppDate();
		if(priority != null){
			if(!priority.equals("无")){
				//首先查询原来是否已经创建了优先权时限  selectBy(caseId,"优先权时限");
				Map<String, Object> map = tradeMarkCaseMapper.selectRemindByMessage("优先权时限", caseId);
				//如果以前没有 则这次就新增优先权时限 
				if(map == null){
					remindService.insertRemindByType(11, date, null,custId, caseId, gcon);
				}else{
					//可能用户修改了时间   “申请/展出日期”    则此时要同步更新 对应的优先权时限
					if(date != null){
						GregorianCalendar gc=new GregorianCalendar(); 
						gc.setTime(date);
						gc.add(2, 6);
						gc.add(5, -1);
						int rid = (int)map.get("rid");
						Date limitdate = gc.getTime();
						Map<String, Object> map2 = new HashMap<>();
						map2.put("rid", rid);
						map2.put("limitdate",limitdate);
						tradeMarkCaseMapper.updateById(map2);
					}
				}
			}else{ //这种情况是  取消了优先权 选项 或者是原来默认的没有优先权
				//第一种  如果取消了优先权
				Map<String, Object> map = tradeMarkCaseMapper.selectRemindByMessage("优先权时限", caseId);
				if(map != null){
					//如果 查询不为空 则删除这条优先权时限,同时得把trademark_case 表的信息同步（即消去有关优先权的信息）
					int rid = (int)map.get("rid");
					tradeMarkCaseMapper.deleteById(rid);
					tradeMarkCaseMapper.updatePriority(caseId);
				}
			}
			
		}else{ //此种情况是 只变动展出时间，而其他都不变的条件下 执行
			if(date !=null){
				Map<String, Object> map = tradeMarkCaseMapper.selectRemindByMessage("优先权时限", caseId);
				GregorianCalendar gc=new GregorianCalendar(); 
				gc.setTime(date);
				gc.add(2, 6);
				gc.add(5, -1);
				int rid = (int)map.get("rid");
				Date limitdate = gc.getTime();
				Map<String, Object> map2 = new HashMap<>();
				map2.put("rid", rid);
				map2.put("limitdate",limitdate);
				tradeMarkCaseMapper.updateById(map2);
			}
		}
		info.setSuccess(true);
		info.setMessage("修改案件成功");
		return info;
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
		
		tradeMarkCasePropertyImpl.setAgencyContact(agencyId, custId, gcon, tradeMarkCaseTemp );
	    
	    //Modification end
	  //填充转让案件申请人信息
	    tradeMarkCaseTemp = tradeMarkCasePropertyImpl.fulltransferapp(tradeMarkCaseTemp);
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
	
	
	
	
	public ReturnInfo createChildCase(Integer caseId, String caseType, Integer caseTypeId) {
		ReturnInfo info = new ReturnInfo();

		if(caseId==null){
			info.setSuccess(false);
			info.setMessage("主案件ID不能为空");
			return info;
		}
		
		TradeMarkCase tradeMarkCaseOld = tradeMarkCaseMapper.selectByPrimaryKey(caseId);
		if(tradeMarkCaseOld==null){
			info.setSuccess(false);
			info.setMessage("主案件不存在");
			return info;
		}
	
		Integer agencyId=tradeMarkCaseOld.getAgencyId();
		TradeMarkCase tradeMarkCaseTemp = tradeMarkCaseOld;
		tradeMarkCaseTemp.setId(null);//主键ID为空，插入时自动生成
		tradeMarkCaseTemp.setAgentNum(null);//代理文号需要修改		
		tradeMarkCaseTemp.setParentId(caseId);
		tradeMarkCaseTemp.setCaseType(caseType);
		tradeMarkCaseTemp.setCaseTypeId(caseTypeId);
		tradeMarkCaseTemp.setStatus("申请中");
		tradeMarkCaseTemp.setCaseTypeId(caseTypeId);
		
		
		//Modificaiton start, by yang guang,2018-10-31
		//设置是否补充材料，发文编号
		if(caseType!=null ){
			Integer supplement=1;
			tradeMarkCaseTemp.setSupplement(supplement);		
			String appNumber=tradeMarkCaseTemp.getAppNumber();
			if (appNumber!=null && !appNumber.equals("")){
				String docNumber =tradeMarkCasePropertyImpl.getDocNumber(appNumber, caseType);				
				tradeMarkCaseTemp.setDocNumber(docNumber);		
			}
		}
		//Modificaiton end
	
		
		tradeMarkCaseMapper.insertSelective(tradeMarkCaseTemp);
		
		Integer childCaseId = tradeMarkCaseTemp.getId();
		//更新文号
		String agentNum = TradeMarkCaseUtil.generateAgentNum(childCaseId);
		TradeMarkCase updateAgentNum = new TradeMarkCase();
		updateAgentNum.setAgentNum(agentNum);
		updateAgentNum.setId(childCaseId);
		tradeMarkCaseMapper.updateByPrimaryKeySelective(updateAgentNum);
		
		//案件文件关联，不包括共同申请人文件
		//tradeMarkCaseFileMapper.copyTmCaseFileRecord(assoCaseId,caseId);//joinAppId = null
		materialMapper.copyTmCaseFileRecord(childCaseId,caseId);
		//查询共同申请人文件
		//List<TradeMarkCaseFile> joinAppFiles = tradeMarkCaseFileMapper.selectByCaseId(caseId);//joinAppId 不为null
		TradeMarkCaseJoinApp t= new TradeMarkCaseJoinApp();
		t.setCaseId(caseId);
		List<Material> materialByJoinApp = materialMapper.selectMaterialByJoinApp(t);
		Integer joinAppIdOld =0;
		if(materialByJoinApp!=null&&materialByJoinApp.size()>0){
			for(Material joinAppFileOld : materialByJoinApp){
				
				joinAppIdOld=joinAppFileOld.getJoinAppId();				
				
				TradeMarkCaseJoinApp joinAppOld = tradeMarkCaseJoinAppMapper.selectByPrimaryKey(joinAppIdOld);
				if(joinAppOld != null) {
					TradeMarkCaseJoinApp joinAppNew = joinAppOld;
					joinAppNew.setId(null);
					joinAppNew.setAgencyId(agencyId);
					joinAppNew.setCaseId(childCaseId);
					tradeMarkCaseJoinAppMapper.insertSelective(joinAppNew);
					//关联共同申请人文件
					Integer joinAppIdNewId = joinAppNew.getId();
					
					Material material = joinAppFileOld;
					material.setCaseId(childCaseId);

					material.setJoinAppId(joinAppIdNewId);
					materialMapper.insertSelective(material);
				}
				
			}
		}
				
		//案件商品和类别关联
		tradeMarkCaseCategoryMapper.copyTmCaseCategoryRecord(childCaseId,caseId);	
		
		info.setSuccess(true);
		
		//Modification start, 2018-04-05
		Map<String, Integer> data = new HashMap<String, Integer>();
		data.put("childCaseId",childCaseId);
		info.setData(data);
		//Modification end, 2018-04-05
		
		info.setMessage("子案件创建成功");
		return info;
	}
	

	
	public ReturnInfo updateSolrTradeMarkCase() {
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			//创建主表，由于信息过大所以分片查询
			SolrData trcase = new SolrData();
			Boolean goon = true;
			int i = 0;
			List<Map<String,Object>> trcasetable = new ArrayList<Map<String,Object>>();
			while(goon) {
				List<Map<String,Object>> trcasetablePart = tradeMarkCaseSolrMapper.selectAllTradeMarkCase(i);
				if(trcasetablePart.size()<50000) goon = false;
				trcasetable.addAll(trcasetablePart);
				i=i+50000;
			}
			trcase.setDataTable(trcasetable);
			trcase.setIdName("id");
			//创建商标其他信息表队列
			List<SolrData> otherData = new ArrayList<SolrData>();

			SolrData trcasedept = new SolrData();
			goon = true;
			i = 0;
			List<Map<String,Object>> trcasedepttable = new ArrayList<Map<String,Object>>();
			while(goon) {
				List<Map<String,Object>> trcasetabledeptPart = tradeMarkCaseSolrMapper.selectAllTradeMarkCaseDepart(i);
				if(trcasetabledeptPart.size()<50000) goon = false;
				trcasedepttable.addAll(trcasetabledeptPart);
				i=i+50000;
			}
			trcasedept.setDataTable(trcasedepttable);
			trcasedept.setIdName("id");
			otherData.add(trcasedept);
			
			SolrData trcaseuser = new SolrData();
			goon = true;
			i = 0;
			List<Map<String,Object>> trcaseusertable = new ArrayList<Map<String,Object>>();
			while(goon) {
				List<Map<String,Object>> trcasetableuserPart = tradeMarkCaseSolrMapper.selectAllTradeMarkCaseUser(i);
				if(trcasetableuserPart.size()<50000) goon = false;
				trcaseusertable.addAll(trcasetableuserPart);
				i=i+50000;
			}
			trcaseuser.setDataTable(trcaseusertable);
			trcaseuser.setIdName("id");
			otherData.add(trcaseuser);
			
			
			
			SolrSendTrademarkCase solr = new SolrSendTrademarkCase();
			solr.createDocs(solrInfo, trcase, otherData);

			returnInfo.setSuccess(true);
			returnInfo.setMessage("更新成功");
			return returnInfo;
		} catch(Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("更新失败:");
			e.printStackTrace();
			return returnInfo;
		}

	}
		
		
	
	
	
		
	
}
