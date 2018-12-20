package com.yootii.bdy.tmcase.service.impl;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.yootii.bdy.agency.dao.AgencyMapper;
import com.yootii.bdy.agency.service.AgencyContactService;
import com.yootii.bdy.agency.service.AgencyService;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.customer.model.Applicant;
import com.yootii.bdy.customer.service.ApplicantService;
import com.yootii.bdy.datasyn.service.DataSynService;
import com.yootii.bdy.ipinfo.service.ForbidContentService;
import com.yootii.bdy.material.dao.MaterialMapper;

import com.yootii.bdy.material.service.MaterialService;
import com.yootii.bdy.remind.service.RemindService;

import com.yootii.bdy.solr.SolrInfo;


import com.yootii.bdy.task.service.Impl.TaskCommonImpl;
import com.yootii.bdy.tmcase.dao.GoodsMapper;
import com.yootii.bdy.tmcase.dao.IssuanceNumberMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkAssociationMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseCategoryMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseFileMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseFilePreMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseJoinAppMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCasePreMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseSolrMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseTypeMapper;

import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.service.TradeMarkCaseService;
import com.yootii.bdy.trademark.dao.TrademarkMapper;
import com.yootii.bdy.trademark.model.Trademark;
import com.yootii.bdy.trademark.service.TradeMarkService;


@Component
public class TradeMarkCaseManageImpl {
	
	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private SolrInfo solrInfo;
	
	@Resource
	private TradeMarkService tradeMarkService;

	@Resource
	private TradeMarkCaseSolrMapper tradeMarkCaseSolrMapper;
	
	@Resource
	private ApplicantService applicantService;
	
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
	private AgencyMapper agencyMapper;
	
	@Resource
	private AgencyContactService agencyContactService;
	
	@Resource
	private MaterialService materialService;
	
	@Resource
	private TradeMarkCaseCommonImpl tradeMarkCaseCommonImpl;
	
	@Resource
	private RemindService remindService;
	
	@Resource
	private DataSynService dataSynService;
	
	@Resource
	private AgencyService agencyService;
	
	@Resource
	private TradeMarkCaseTypeMapper tradeMarkCaseTypeMapper;
	
	@Resource
	private IssuanceNumberMapper issuanceNumberMapper;
	
	@Resource
	private GoodsMapper goodsMapper;

	@Resource
	private TrademarkMapper trademarkMapper;

		
	@Resource
	private ForbidContentService forbidContentService;
	@Resource
	private TaskCommonImpl taskCommonImpl;
	
	@Resource
	private	TradeMarkCaseQueryImpl tradeMarkCaseQueryImpl;	
	
	@Resource
	private	TradeMarkCaseGoodsImpl tradeMarkCaseGoodsImpl;
	
	@Resource
	private	TradeMarkCaseRemindImpl tradeMarkCaseRemindImpl;
	
	@Resource
	private	TradeMarkCaseBaseImpl tradeMarkCaseBaseImpl;
	
	@Resource
	private	TradeMarkCasePropertyImpl tradeMarkCasePropertyImpl;
	@Resource
	private TradeMarkCaseService tradeMarkCaseService;
	
	
	public ReturnInfo createTradeMarkCase(TradeMarkCase tradeMarkCase, GeneralCondition gcon) {
		ReturnInfo info = tradeMarkCaseBaseImpl.createTradeMarkCase(tradeMarkCase, gcon);
		return info;
	}	
	
	
	public ReturnInfo createTradeMarkCaseByTmNumber(TradeMarkCase tradeMarkCase,String tmNumber,GeneralCondition gcon) {		
		ReturnInfo info = tradeMarkCaseBaseImpl.createTradeMarkCaseByTmNumber(tradeMarkCase, tmNumber, gcon);
		return info;
	}
	
	
	
	public ReturnInfo createTradeMarkCaseByAppName(TradeMarkCase tradeMarkCase, GeneralCondition gcon) {
		ReturnInfo info = tradeMarkCaseBaseImpl.createTradeMarkCaseByAppName(tradeMarkCase, gcon);
		return info;
	}
	
	
	public ReturnInfo createTradeMarkCaseByTmNumberList(TradeMarkCase tradeMarkCase,List<String> tmNumberlist,GeneralCondition gcon) {
		ReturnInfo info = tradeMarkCaseBaseImpl.createTradeMarkCaseByTmNumberList(tradeMarkCase, tmNumberlist, gcon);
		return info;
	}
	
	public ReturnInfo tradeMarkCaseAssociate(Integer caseId,
			Integer agencyId,Integer couserId,GeneralCondition gcon) {
		ReturnInfo info = tradeMarkCaseBaseImpl.tradeMarkCaseAssociate(caseId, agencyId, couserId, gcon);
		return info;
	}
	
	
	
	public ReturnInfo createChildCase(Integer caseId, String caseType, Integer caseTypeId) {
		ReturnInfo info = tradeMarkCaseBaseImpl.createChildCase(caseId, caseType, caseTypeId);
		return info;
	}
	

	
	public ReturnInfo updateSolrTradeMarkCase() {
		ReturnInfo returnInfo = tradeMarkCaseBaseImpl.updateSolrTradeMarkCase();
		return returnInfo;
	}
		
	
	public ReturnInfo modifyTradeMarkCase(TradeMarkCase tradeMarkCase, GeneralCondition gcon) {
		ReturnInfo info = tradeMarkCaseBaseImpl.modifyTradeMarkCase(tradeMarkCase, gcon);		
		return info;
	}


	
	/**
	 * 创建复杂的案件的申请入口
	 */	
	public ReturnInfo createAppCase(
			TradeMarkCase tradeMarkCase, String tmNumber,
			GeneralCondition gcon) {
		List<String> tmNumberlist=new ArrayList<String>();
		tmNumberlist.add(tmNumber);
		List<Applicant> ApplicantList=applicantService.queryApplicantByRegNumbers(tmNumberlist);
		Applicant applicant=null;
		if (ApplicantList!=null && ApplicantList.size()>0){
		  applicant=ApplicantList.get(0);
		}
		ReturnInfo info = createApplicantCase(tradeMarkCase,tmNumber,applicant,gcon);
		return info;
	}
	
	

	/**
	 * 创建答辩案件入口
	 */	
	public ReturnInfo createDissentReplyEntrance(TradeMarkCase tradeMarkCase,
			String tmNumber, GeneralCondition gcon) {
		List<String> tmNumberlist=new ArrayList<String>();
		tmNumberlist.add(tmNumber);
		List<Applicant> ApplicantList=applicantService.queryApplicantByRegNumbers(tmNumberlist);
		Applicant applicant=null;
		if (ApplicantList!=null && ApplicantList.size()>0){
		  applicant=ApplicantList.get(0);
		}
		ReturnInfo info = createReply(tradeMarkCase,tmNumber,applicant,gcon);
		return info;
	}	
	

	/**
	 * 创建商标异议答辩 类型的案件
	 * 如果该商标的异议申请在本平台办理，则直接可从数据库中查到相关异议人的信息，否则需要从官方发来的
	 * 文档中获取 在页面填写 保存 
	 */
	
	public ReturnInfo createDissentTmCase(TradeMarkCase tradeMarkCase,String tmNumber,Applicant applicant,
			GeneralCondition gCondition){
		ReturnInfo returnInfo = new ReturnInfo();
		Integer custId = tradeMarkCase.getCustId();
		if(custId == null){
			returnInfo.setSuccess(false);
			returnInfo.setMessage("参数不能为空");
			return returnInfo;
		}
		try{
			tradeMarkCase.setCaseType("商标异议答辩");
			tradeMarkCase.setCaseTypeId(9);
			
			tradeMarkCase = this.createCommonCase(tradeMarkCase, tmNumber, custId, gCondition);
			//根据注册号查询商标信息
			Trademark trademark=tradeMarkService.selectTrademarkbyRenumber(tmNumber,gCondition);
			//查找本地商标异议案件信息
			Map<String, Object> map = tradeMarkCaseMapper.selectByReg(tmNumber);
			if(map!=null){
				//信息置换  对于不同类型的案件置换不同 的属性
				tradeMarkCase = this.transverterMapToTrademarkCase(tradeMarkCase,map);
			}
			returnInfo = this.autoImplTmCase(tradeMarkCase, returnInfo, applicant, gCondition, tmNumber, trademark);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return returnInfo;
	}

	/**
	 * 创建答辩类案件   （异议答辩   撤三答辩      撤销复审答辩    无效宣告答辩     参加不予注册复审）  这些案件都有一个共同点，
	 * 即可以根据各自对应的申请类 案件进行信息转移到本案件中来。
	 * @param tradeMarkCase
	 * @param tmNumber
	 * @param applicant
	 * @param gCondition
	 * @return
	 */
	public ReturnInfo createReply(TradeMarkCase tradeMarkCase,String tmNumber,Applicant applicant,
			GeneralCondition gCondition){
		ReturnInfo returnInfo = new ReturnInfo();
		Integer custId = tradeMarkCase.getCustId();
		Integer caseTypeId = tradeMarkCase.getCaseTypeId();
		if(custId == null){
			returnInfo.setSuccess(false);
			returnInfo.setMessage("参数不能为空");
			return returnInfo;
		}
		try{
			tradeMarkCase = this.createCommonCase(tradeMarkCase, tmNumber, custId, gCondition);
			//根据注册号查询商标信息
			Trademark trademark=tradeMarkService.selectTrademarkbyRenumber(tmNumber,gCondition);
			//查找本地商标案件信息
			Map<String, Object> map = this.queryCaseByCaseTypeId(caseTypeId,tmNumber);
			if(map!=null){
				//信息置换  对于不同类型的案件置换不同 的属性
				tradeMarkCase = this.transverterInfomation(tradeMarkCase, map);
			}
			returnInfo = this.autoImplTmCase(tradeMarkCase, returnInfo, applicant, gCondition, tmNumber, trademark);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return returnInfo;
	}
	
	
	
	
	

	private Map<String, Object> queryCaseByCaseTypeId(Integer caseTypeId,String tmNumber){
		Map<String,Object> map = new HashMap<>();
		switch (caseTypeId) {
			case 9 :         //如果是要创建异议 答辩类型的案件，则先查异议申请案件   以下同理
				caseTypeId = 8;
				break;
			case 15:          //商标撤三答辩
				caseTypeId = 11;
				break;
			case 20:          //参与不予注册复审
				caseTypeId =13;
				break;
			case 22:          //商标无效宣告答辩
				caseTypeId = 10;
				break;
			default :
				break;
		}
		
		
		map = tradeMarkCaseMapper.selectByRegAndType(tmNumber, caseTypeId);
		return map;
	}
	
	//通用设置
	public TradeMarkCase createCommonCase(TradeMarkCase tradeMarkCase,String tmNumber,Integer custId,GeneralCondition gCondition){
			Integer agencyId = tradeMarkCase.getAgencyId();
			//默认是万慧达
			if(agencyId == null){
				tradeMarkCase.setAgencyId(1);
			}
			Integer agencyId2= tradeMarkCase.getAgencyId();
			//设置联系人
			tradeMarkCase = tradeMarkCasePropertyImpl.agencyContatc(agencyId2,custId,gCondition,tradeMarkCase);
		return tradeMarkCase;
	}
	
	
	
	
	
	/**
	 * 对创建不同类型案件相同代码的封装
	 * @param tradeMarkCase
	 * @param returnInfo
	 * @param applicant
	 * @return
	 */
	private ReturnInfo autoImplTmCase(TradeMarkCase tradeMarkCase,ReturnInfo returnInfo,
			Applicant applicant,GeneralCondition gCondition,String tmNumber,Trademark trademark) throws Exception{
		//设置申请人国籍，申请书式等属性
		if (applicant!=null){			
			tradeMarkCasePropertyImpl.setApplicantProperty(tradeMarkCase, applicant);
		}
		//把商标信息移植到案件信息中去
		tradeMarkCase = tradeMarkCasePropertyImpl.insertTrademarkTotrademarkCase(tradeMarkCase,trademark);
		String caseType = tradeMarkCase.getCaseType();
		//生成发文编号
		String docNumber = tradeMarkCaseService.getDocNumber(tmNumber, caseType);
		tradeMarkCase.setDocNumber(docNumber);
		// 真正的创建实现
		returnInfo = this.createTradeMarkCase(tradeMarkCase,gCondition);
		if(returnInfo.getSuccess()){
			 //根据商标图片，创建案件图片
		    materialService.createMaterialByCase(tradeMarkCase, gCondition);
		    Integer id=tradeMarkCase.getId();		    
		    String imageFile=tradeMarkCase.getImageFile();	
		    if (imageFile!=null && !imageFile.equals("")){
			    TradeMarkCase tmCase=new TradeMarkCase();
			    tmCase.setId(id);
			    tmCase.setImageFile(imageFile);			    
			    tradeMarkCaseMapper.updateByPrimaryKeySelective(tradeMarkCase);
		    }
		}
		returnInfo.setSuccess(true);
		returnInfo.setMessage("创建成功");
		
		return returnInfo;
	}
	
	/**
	 * 答辩类案件信息转移
	 * @return
	 */
	private TradeMarkCase transverterInfomation(TradeMarkCase tradeMarkCase,Map<String, Object> map){
		Integer caseTypeId = tradeMarkCase.getCaseTypeId();
		//获取答辩类案件 所对应的申请类案件的  申请人信息
		String  appCnName = (String)map.get("appCnName");
		String appCnAddr=(String)map.get("appCnAddr");
		Integer agencyId = (Integer)map.get("agencyId");
		String agencyName = (String)map.get("name");
		
		switch(caseTypeId) {
			case 9:				//异议答辩
				if(tradeMarkCase.getDissentName() == null || tradeMarkCase.getDissentName().equals("")){
					tradeMarkCase.setDissentName(appCnName);
				}
				if(tradeMarkCase.getDissentAddress()==null || tradeMarkCase.getDissentAddress().equals("")){
					tradeMarkCase.setDissentAddress(appCnAddr);
				}
				if(tradeMarkCase.getDissentAgentId() == null){
					tradeMarkCase.setDissentAgentId(agencyId);
				}
				if(tradeMarkCase.getDissentAgent() == null || tradeMarkCase.getDissentAgent().equals("")){
					tradeMarkCase.setDissentAgent(agencyName);
				}
				break;
			case  15: 			//撤三答辩   赋值撤销人名称和撤销人代理机构
				if(tradeMarkCase.getCancelPersonName_CN() ==null ||tradeMarkCase.getCancelPersonName_CN().equals("")){
					tradeMarkCase.setCancelPersonName_CN(appCnName);
				}
				if(tradeMarkCase.getCancelPersonAgent()==null || tradeMarkCase.getCancelPersonAgent().equals("")){
					tradeMarkCase.setCancelPersonAgent(agencyName);
				}
				break;
			case 19: 			//撤销商标复审答辩       赋值
				if(tradeMarkCase.getRecheckPersonName_CN()==null || tradeMarkCase.getRecheckPersonName_CN().equals("")){
					tradeMarkCase.setRecheckPersonName_CN(appCnName);
				}
				if(tradeMarkCase.getRecheckPersonAgent()==null || tradeMarkCase.getRecheckPersonAgent().equals("")){
					tradeMarkCase.setRecheckPersonAgent(agencyName);
				}
				break;
			case 20:       		//参与不予注册复审
				if(tradeMarkCase.getRecheckPersonName_CN()==null || tradeMarkCase.getRecheckPersonName_CN().equals("")){
					tradeMarkCase.setRecheckPersonName_CN(appCnName);
				}
				if(tradeMarkCase.getRecheckPersonAgent()==null || tradeMarkCase.getRecheckPersonAgent().equals("")){
					tradeMarkCase.setRecheckPersonAgent(agencyName);
				}
				break;
			case 22:   			//无效宣告答辩      赋值  无效申请人中文名称，无效申请人代理机构
				if(tradeMarkCase.getInvalidPersonName_CN()==null || tradeMarkCase.getInvalidPersonName_CN().equals("")){
					tradeMarkCase.setInvalidPersonName_CN(appCnName);
				}
				if(tradeMarkCase.getInvalidPersonAgent()== null || tradeMarkCase.getInvalidPersonAgent().equals("")){
						tradeMarkCase.setInvalidPersonAgent(agencyName);
				}
				break;
			default :
				break;
		}
		return tradeMarkCase;
		
	}
	
	
	
	/**
	 * 数据进行转移，如果商标异议案件时在本平台上办理的，那么异议案件的申请人 应该是答辩案件的  异议人
	 * @param tradeMarkCase
	 * @param map
	 * @return
	 */
	private TradeMarkCase transverterMapToTrademarkCase(TradeMarkCase tradeMarkCase,Map<String, Object> map){
		
		String dissentName = (String)map.get("appCnName");
		String dissAddr = (String)map.get("appCnAddr");
		Integer dissentAgencyId = (Integer)map.get("agencyId");
		
		
		if(tradeMarkCase.getDissentName() == null || tradeMarkCase.getDissentName().equals("")){
			tradeMarkCase.setDissentName(dissentName);
		}
		if(tradeMarkCase.getDissentAddress()==null || tradeMarkCase.getDissentAddress().equals("")){
			tradeMarkCase.setDissentAddress(dissAddr);
		}
		if(tradeMarkCase.getDissentAgentId() == null){
			tradeMarkCase.setDissentAgentId(dissentAgencyId);
			if(dissentAgencyId == 1){
				tradeMarkCase.setDissentAgent("北京万慧达知识产权代理公司");
			}
		}
		return tradeMarkCase;
	}
	
	
	//修改 异议答辩案件
	
	public ReturnInfo modifyDissentTmCase(TradeMarkCase tradeMarkCase,
			GeneralCondition gCondition) {
		ReturnInfo returnInfo = new ReturnInfo();
		String regNumber = tradeMarkCase.getRegNumber();
		String tmType = tradeMarkCase.getGoodClasses();
		tradeMarkCaseGoodsImpl.updateGoods(tradeMarkCase);
		return returnInfo;
	}


	/**
	 * 创建 异议申请 类型案件
	 * 在异议申请 案件中 被异议人其实是指商标现有的申请人，而在异议答辩案件中，被异议人则是申请异议案件的申请人。
	 * 所以在创建异议申请时，  被异议人的信息 其实是商标信息表的申请人信息，而商标信息有可以到本地库中去查询或者通过
	 * 接口查询
	 */

	public ReturnInfo createApplicantCase(TradeMarkCase tradeMarkCase,String tmNumber,Applicant applicant,
			GeneralCondition gCondition) {
		ReturnInfo returnInfo = new ReturnInfo();
		String goodClass = tradeMarkCase.getGoodClasses();
		Integer custId = tradeMarkCase.getCustId();
		if(custId == null){
			returnInfo.setSuccess(false);
			returnInfo.setMessage("客户id不能为空");
			return returnInfo;
		}
		if(goodClass == null  || goodClass.equals("")){
			returnInfo.setSuccess(false);
			returnInfo.setMessage("商标类别不能为空");
			return returnInfo;
		}
		try{
			//Modification start, 2018-12-14
			//此处不宜写死为某一个案件类型，在taskController中已经根据请求参数
			//获取了当前的平台服务，自动设置了caseType和caseTypeId
			
//			tradeMarkCase.setCaseType("商标异议申请");
//			tradeMarkCase.setCaseTypeId(8);
			
			//Modification end
			
			Integer agencyId = tradeMarkCase.getAgencyId();
			//默认是万慧达
			if(agencyId== null){
				tradeMarkCase.setAgencyId(1);
			}
			Integer agencyId2 = tradeMarkCase.getAgencyId();
			//设置联系人
			tradeMarkCase = tradeMarkCasePropertyImpl.agencyContatc(agencyId2,custId,gCondition,tradeMarkCase);
			//根据注册号查询商标信息
			Trademark trademark=tradeMarkService.selectTrademarkbyRenumber(tmNumber,gCondition);
			trademark.setRegNumber(tmNumber);
			//如果表中查询不到，要根据接口查询了
			if(trademark == null){
				
			}
			//设置申请人国籍，申请书式等属性
			if (applicant!=null){			
				tradeMarkCasePropertyImpl.setApplicantProperty(tradeMarkCase, applicant);
			}
			//把商标信息移植到案件信息中去
			tradeMarkCase = tradeMarkCasePropertyImpl.insertTrademarkTotrademarkCase(tradeMarkCase,trademark);
			// 真正的创建实现
			returnInfo = createTradeMarkCase(tradeMarkCase,gCondition);
			if(returnInfo.getSuccess()){
				 //根据商标图片，创建案件图片
			    materialService.createMaterialByCase(tradeMarkCase, gCondition);
			    Integer id=tradeMarkCase.getId();		    
			    String imageFile=tradeMarkCase.getImageFile();	
			    if (imageFile!=null && !imageFile.equals("")){
				    TradeMarkCase tmCase=new TradeMarkCase();
				    tmCase.setId(id);
				    tmCase.setImageFile(imageFile);			    
				    tradeMarkCaseMapper.updateByPrimaryKeySelective(tradeMarkCase);
			    }
			}
			//
			returnInfo.setSuccess(true);
			returnInfo.setMessage("创建成功");
		}catch (Exception e) {
			e.printStackTrace();
		}	
				
		return returnInfo;
	}



	//修改  异议申请类型的案件
	
	public ReturnInfo modifyApplicantDissent(TradeMarkCase tradeMarkCase,
			GeneralCondition gCondition) {
		ReturnInfo returnInfo = new ReturnInfo();
		
		//tradeMarkCaseMapper.modifyApplicantDissent(tradeMarkCase);
		
		tradeMarkCaseMapper.updateByPrimaryKeySelective(tradeMarkCase);
		//修改服务
		tradeMarkCaseGoodsImpl.updateGoods(tradeMarkCase);
		return returnInfo;
	}

	
}
