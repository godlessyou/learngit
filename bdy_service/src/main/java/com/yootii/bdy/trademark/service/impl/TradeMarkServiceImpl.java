package com.yootii.bdy.trademark.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yootii.bdy.agency.model.Agency;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.Globals;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.common.TrademarkProcessStatus;
import com.yootii.bdy.customer.dao.ApplicantMapper;
import com.yootii.bdy.customer.model.Applicant;
import com.yootii.bdy.customer.service.ApplicantService;
import com.yootii.bdy.ipinfo.service.ForbidContentService;
import com.yootii.bdy.security.service.AuthenticationService;
import com.yootii.bdy.solr.SolrData;
import com.yootii.bdy.solr.SolrInfo;
import com.yootii.bdy.solr.SolrSend;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseMapper;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.trademark.dao.TrademarkCategoryMapper;
import com.yootii.bdy.trademark.dao.TrademarkMapper;
import com.yootii.bdy.trademark.dao.TrademarkProcessMapper;
import com.yootii.bdy.trademark.dao.TrademarkSolrMapper;
import com.yootii.bdy.trademark.model.TmStatusDTO;
import com.yootii.bdy.trademark.model.Trademark;
import com.yootii.bdy.trademark.service.TradeMarkService;
import com.yootii.bdy.util.ServiceUrlConfig;
import com.yootii.bdy.util.TrademarkCategoryUtil;


@Service("tradeMarkService")
public class TradeMarkServiceImpl implements
		TradeMarkService {
	
	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private AuthenticationService authenticationService ;
	
	@Resource
	private SolrInfo solrInfo ;
	
	@Resource
	private TrademarkMapper trademarkMapper;
	
	@Resource
	private TrademarkCategoryMapper trademarkCategoryMapper;
	
	@Resource
	private TrademarkProcessMapper trademarkProcessMapper;
	
	@Resource
	private TrademarkSolrMapper trademarkSolrMapper;
	
	@Resource
	private ServiceUrlConfig serviceUrlConfig;
	
	@Resource
	private ForbidContentService forbidContentService;
	
	@Resource
	private ApplicantService applicantService;
	@Autowired
	private ApplicantMapper applicantMapper;
	
	@Resource
	private TrademarkCategoryUtil trademarkCategoryUtil;
	
	
	@Resource
	private TradeMarkCaseMapper tradeMarkCaseMapper;
	
	
	public ReturnInfo queryAddressList(Trademark trademark){
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			List<Trademark> list=null;
			String applicantName=trademark.getApplicantName();	
			String applicantEnName=trademark.getApplicantEnName();
			if (applicantName!=null){
				list=trademarkMapper.selectAddressList(trademark);
			}else if (applicantEnName!=null){
				list=trademarkMapper.selectEnAddressList(trademark);
			}
			returnInfo.setData(list);
			returnInfo.setSuccess(true);		
			returnInfo.setMessage("查询成功");
			return returnInfo;
		
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage(e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}

	}
	
	
	@Override
	public ReturnInfo queryTmList(Date appStartDate,
    		Date appEndDate,GeneralCondition gcon, Trademark trademark, 
    		Date regStartDate, Date regEndDate, Integer CustID,Integer appId,Integer national, String applicantAddressList) {
		ReturnInfo returnInfo = new ReturnInfo();
		Integer pageNo = gcon.getPageNo();	
		gcon.setStartDate(appStartDate);
		gcon.setEndDate(appEndDate);	

		try {
			if(CustID == null&&appId == null) throw new Exception("客户信息有误");
			List<String> applicantNameList = new ArrayList<String>();
			ReturnInfo apprtnInfo = null;
			if(appId!=null) {
				apprtnInfo = applicantService.queryAllApplicantByAppId(gcon, appId);
			} else {
				apprtnInfo = applicantService.queryAllApplicantByCustId(gcon, CustID);
			}

			if(!apprtnInfo.getSuccess()) throw new Exception("申请人查询失败");
			List<Applicant> applicants = (List<Applicant>) apprtnInfo.getData();
			if(applicants!=null && applicants.size()>0){
				for(Applicant app:applicants) {
					String appName=app.getApplicantName();
					if (!applicantNameList.contains(appName)){
						applicantNameList.add(appName);
					}				
				}
			}	
			SolrSend solr = new SolrSend();
			
			List<String>addressList=null;
			
			if (applicantAddressList!=null && !applicantAddressList.equals("")){
				addressList=new ArrayList<String>();
				StringTokenizer idtok3 = new StringTokenizer(applicantAddressList, "#");					
				while (idtok3.hasMoreTokens()) {
					String address= idtok3.nextToken();
					addressList.add(address);
				}
			}
						
			
			
			returnInfo = solr.selectTm(gcon, applicantNameList, addressList, trademark, regStartDate, regEndDate, solrInfo,national, returnInfo);
			returnInfo.setSuccess(true);
			returnInfo.setCurrPage(pageNo);
			returnInfo.setMessage("查询成功");
			return returnInfo;
			
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage(e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}

	}
	
	
	
	@Override
	public ReturnInfo queryCanProcessTm(Date appStartDate,
    		Date appEndDate,GeneralCondition gcon, Trademark trademark, 
    		Date regStartDate, Date regEndDate, Integer CustID,Integer appId,Integer national, Integer caseTypeId, String addressList) {
		ReturnInfo returnInfo =new ReturnInfo();
		int offset=gcon.getOffset();
		int rows=gcon.getrows();
		
		gcon.setRows(99999);
		
		if (caseTypeId==2){
			returnInfo = queryTmXuzhanList(gcon , CustID, appId, national);					
		}else if (caseTypeId==5){//变更名义地址集体管理规则成员名单
			returnInfo = queryTmChangeList(gcon , trademark, appId, national, addressList);					
		}else {
			returnInfo = queryTmList(appStartDate,appEndDate,gcon, trademark,
					regStartDate,regEndDate, CustID, appId, national, null);
		}
		
		//Modification start, by yang guang, 2018-10-18
		//在返回结果中，去掉已经立案的商标
		if (returnInfo!=null && returnInfo.getSuccess()){
			List<Trademark> trademarklist=(List<Trademark>)returnInfo.getData();						
			if (trademarklist!=null && trademarklist.size()>0){				
				List<Trademark> resultList=new ArrayList<Trademark>();				
				long total=processReturnInfo(trademarklist, resultList, caseTypeId, offset, rows);
				returnInfo.setData(resultList);	
				returnInfo.setTotal(total);
			}
		}		
		//Modification end
		
		return returnInfo;

	}
	
	
	@Override
	public ReturnInfo queryTmlistSimple(GeneralCondition gcon ,List<String> applicantNameList,Integer national,Trademark trademark ,Trademark nottrademark){
		ReturnInfo returnInfo = new ReturnInfo();
		SolrSend solr = new SolrSend();
		try {
			Trademark trademarknull = new Trademark();
			nottrademark= nottrademark==null?trademarknull:nottrademark;
			List<String> addressList=null;
			returnInfo = solr.selectTm(gcon, applicantNameList,addressList, trademark, null, null, solrInfo,national, returnInfo,nottrademark);
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
		return returnInfo;	
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage(e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}
	
	
	@Override
	public ReturnInfo queryTmXuzhanList(GeneralCondition gcon ,Integer CustID,Integer appId,Integer national) {
		ReturnInfo returnInfo = new ReturnInfo();
		Integer pageNo = gcon.getPageNo();	
	



		try {
			if(CustID == null&&appId == null) throw new Exception("客户信息有误");
			List<String> applicantNameList = new ArrayList<String>();
			ReturnInfo apprtnInfo = null;
			if(appId!=null) {
				apprtnInfo = applicantService.queryAllApplicantByAppId(gcon, appId);
			} else {
				apprtnInfo = applicantService.queryAllApplicantByCustId(gcon, CustID);
			}

			if(!apprtnInfo.getSuccess()) throw new Exception("申请人查询失败");
			List<Applicant> applicants = (List<Applicant>) apprtnInfo.getData();
			if(applicants!=null && applicants.size()>0){
				for(Applicant app:applicants) {
					String appName=app.getApplicantName();
					if (!applicantNameList.contains(appName)){
						applicantNameList.add(appName);
					}				
				}
			}	
			
			Trademark trademark =new Trademark();
			Trademark nottrademark =new Trademark();
			SolrSend solr = new SolrSend();
			Date Today = new Date();
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(Today);
					
			Date regStartDate  = calendar.getTime();
			calendar.add(Calendar.YEAR, 1);
			Date regEndDate = calendar.getTime();
			trademark.setValidEndDate(regStartDate);
			nottrademark.setValidEndDate(regEndDate);
			List<String> addressList=null;
			returnInfo = solr.selectTm(gcon, applicantNameList, addressList, trademark, null, null, solrInfo,national, returnInfo,nottrademark);
			returnInfo.setSuccess(true);
			returnInfo.setCurrPage(pageNo);
			returnInfo.setMessage("查询成功");
			return returnInfo;
			
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage(e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}

	}
	@Override
	public ReturnInfo queryTmKuanzhanList(GeneralCondition gcon ,Integer CustID,Integer appId,Integer national) {
		ReturnInfo returnInfo = new ReturnInfo();
		Integer pageNo = gcon.getPageNo();	
	



		try {
			if(CustID == null&&appId == null) throw new Exception("客户信息有误");
			List<String> applicantNameList = new ArrayList<String>();
			ReturnInfo apprtnInfo = null;
			if(appId!=null) {
				apprtnInfo = applicantService.queryAllApplicantByAppId(gcon, appId);
			} else {
				apprtnInfo = applicantService.queryAllApplicantByCustId(gcon, CustID);
			}

			if(!apprtnInfo.getSuccess()) throw new Exception("申请人查询失败");
			List<Applicant> applicants = (List<Applicant>) apprtnInfo.getData();
			if(applicants!=null && applicants.size()>0){
				for(Applicant app:applicants) {
					String appName=app.getApplicantName();
					if (!applicantNameList.contains(appName)){
						applicantNameList.add(appName);
					}				
				}
			}	
			
			Trademark trademark =new Trademark();
			SolrSend solr = new SolrSend();
			Date Today = new Date();
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(Today);
			calendar.add(Calendar.DATE, +1);
			calendar.add(Calendar.YEAR, -10);
					
			Date regEndDate  = calendar.getTime();
			calendar.add(Calendar.MONTH, -6);
			Date regStartDate  = calendar.getTime();
			List<String>addressList=null;
			returnInfo = solr.selectTm(gcon, applicantNameList, addressList,trademark, regStartDate, regEndDate, solrInfo,national, returnInfo);
			returnInfo.setSuccess(true);
			returnInfo.setCurrPage(pageNo);
			returnInfo.setMessage("查询成功");
			return returnInfo;
			
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage(e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}

	}
	

	
	@Override
	public ReturnInfo queryTmChangeList(GeneralCondition gcon, Trademark trademark,Integer appId,Integer national, String applicantAddressList) {
		ReturnInfo returnInfo = new ReturnInfo();
		Integer pageNo = gcon.getPageNo();	
	

		try {
			if(appId == null) throw new Exception("参数appId不能为空");
			List<String> applicantNameList = new ArrayList<String>();
			
//			Applicant applicant=new Applicant();
//			applicant.setId(appId);
//			ReturnInfo apprtnInfo=applicantService.queryApplicantDetail(applicant, gcon);				
//			List<Applicant>  applist= (List<Applicant>) apprtnInfo.getData();				
//			if (applist==null || applist.size()==0){
//				returnInfo.setSuccess(false);
//				returnInfo.setMessage("没有查到对应的申请人。");					
//				return returnInfo;
//			}
//				Applicant  appdeail= (Applicant) applist.get(0);
//				String applicantAddress=appdeail.getApplicantAddress();
//				if(applicantAddress == null) {
//					returnInfo.setSuccess(true);
//					returnInfo.setCurrPage(pageNo);
//					returnInfo.setMessage("查询成功");
//					return returnInfo;
//				}
//				if(applicantAddress.equals("")) {
//					returnInfo.setSuccess(true);
//					returnInfo.setCurrPage(pageNo);
//					returnInfo.setMessage("查询成功");
//					return returnInfo;
//				}
			Trademark nottrademark =new Trademark();
//				nottrademark.setApplicantAddress(applicantAddress);
			
			ReturnInfo apprtnInfo=applicantService.queryAllApplicantByAppId(gcon, appId);	
			List<Applicant> applicants = (List<Applicant>) apprtnInfo.getData();
			if(applicants!=null && applicants.size()>0){
				for(Applicant app:applicants) {
					String appName=app.getApplicantName();
					if (!applicantNameList.contains(appName)){
						applicantNameList.add(appName);
					}				
				}
			}
			
		
			SolrSend solr = new SolrSend();			
			
			List<String>addressList=null;
			if (applicantAddressList!=null && !applicantAddressList.equals("")){
				addressList=new ArrayList<String>();
				StringTokenizer idtok3 = new StringTokenizer(applicantAddressList, "#");					
				while (idtok3.hasMoreTokens()) {
					String address= idtok3.nextToken();
					addressList.add(address);
				}
			}
			
			returnInfo = solr.selectTm(gcon, applicantNameList, addressList, trademark, null, null, solrInfo,national, returnInfo,nottrademark);
			returnInfo.setSuccess(true);
			returnInfo.setCurrPage(pageNo);
			returnInfo.setMessage("查询成功");
			return returnInfo;
			
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage(e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}


	@Override
	public ReturnInfo updateSolrDate(){
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			//创建主表，由于信息过大所以分片查询
			SolrData trademark = new SolrData();
			Boolean goon = true;
			int i = 0;
			List<Map<String,Object>> trademarktable = new ArrayList<Map<String,Object>>();
			while(goon) {
				List<Map<String,Object>> trademarktablePart = trademarkSolrMapper.selectAllTrademark(i);
				if(trademarktablePart.size()<50000) goon = false;
				trademarktable.addAll(trademarktablePart);
				i=i+50000;
			}
			trademark.setDataTable(trademarktable);
			trademark.setIdName("tmId");
			//创建商标其他信息表队列
			List<SolrData> otherData = new ArrayList<SolrData>();

			SolrData trademarkPro = new SolrData();
			goon = true;
			i = 0;
			List<Map<String,Object>> trademarkProtable = new ArrayList<Map<String,Object>>();
			while(goon) {
				List<Map<String,Object>> trademarkProtablePart = trademarkSolrMapper.selectAllTrademarkProcess(i);
				if(trademarkProtablePart.size()==0) goon = false;
				trademarkProtable.addAll(trademarkProtablePart);
				i=i+50000;
			}
			trademarkPro.setDataTable(trademarkProtable);
			trademarkPro.setIdName("tmId");
			otherData.add(trademarkPro);
			
			SolrSend solr = new SolrSend();
			solr.createDocs(solrInfo, trademark, otherData);

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
	//更新tmProcess核心 的数据
	@Override
	public ReturnInfo updateTmProcessSolrDate() {
		ReturnInfo  returnInfo = new ReturnInfo();
		try{
		
			SolrData solrData = new SolrData();
			List<Map<String, Object>> obj = new ArrayList<>();
			Boolean continu = true;
			int i=0;
			while (continu) {
				//查询数据
				//每次查询10000条数据，如果哪次查询少于10000，说明是最后一次查询
				long start = System.currentTimeMillis();
				List<Map<String, Object>>tmProcess= trademarkProcessMapper.selectAllTmProcess(i);
				long end = System.currentTimeMillis();
				System.err.println("数据库查询耗时:"+(end-start));
				if(tmProcess.size()<5000)
				continu=false;
				obj.addAll(tmProcess);
				i += 5000;
			}
			solrData.setDataTable(obj);
			solrData.setIdName("id");
			
			SolrSend solrSend = new SolrSend();
			//整理数据并更新
			solrSend.createTmProcessDocs(solrInfo, solrData);
			returnInfo.setSuccess(true);
			returnInfo.setMessage("更新成功");
			return returnInfo;
		}catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("更新失败");
			e.printStackTrace();
		}
		return returnInfo;
	}
	
	//更新solr 数据
	@Override
	public ReturnInfo updateTmNotificationSolrDate(SolrInfo solrInfo) {

		ReturnInfo returnInfo =new ReturnInfo();
		try{
			SolrData solrData = new SolrData();
			List<Map<String, Object>> obj = new ArrayList<>();
			//查询 数据
			obj  = trademarkMapper.queryTmNotificationSolrData();
			solrData.setDataTable(obj);
			solrData.setIdName("id");
			//整理数据并开始更新
			SolrSend solrSend = new SolrSend();
			solrSend.createTmNotificationDocs(solrInfo, solrData);
			returnInfo.setSuccess(true);
			returnInfo.setMessage("更新 成功");
		}catch (Exception e) {
			e.printStackTrace();
			returnInfo.setSuccess(false);
			returnInfo.setMessage("更新 失败");
		}
		
		
		return returnInfo;
	}
	
	
	
	
	
	@Override
	public Trademark selectTrademarkbyRenumber(String tmNumber,GeneralCondition gcon) throws Exception {
		Trademark trademark=new Trademark();
		trademark.setRegNumber(tmNumber);			
//		GeneralCondition gcon = new GeneralCondition();
//		gcon.setTokenID(Globals.getToken().getTokenID());
//		gcon.setRows(1);
		
	  
		List<Trademark> trademarklist =(List<Trademark>) queryTrademarkBySolr(null,null, null, trademark, null, null, gcon,true,2).getData();
	    if(trademarklist==null || trademarklist.size()==0){
	    	ReturnInfo rtnInfo= forbidContentService.querySameTm(tmNumber, gcon);	    	
	    	if(rtnInfo.getData() == null) {
	    		throw new Exception("商标不存在");
	    	}
	    }else{
	    	trademark=trademarklist.get(0);
			  
	    }	
	    
	    return trademark;
		
	}
	

	@Override
	public ReturnInfo queryTrademarkBySolr(Integer custId,Integer appId,Integer dateType,
			Trademark trademark ,String startYear, String endYear,GeneralCondition gcon,Boolean ignore,int flag){
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			if(custId == null&&appId == null&&(!ignore)) throw new Exception("客户信息有误");
			List<String> applicantNameList = new ArrayList<String>();
			/*if(custId!=null&appId!=null) {*/
			ReturnInfo apprtnInfo = null;
			String applicantName=trademark.getApplicantName();
			
			//Modification start, by yang guang, 
			//to resolve BDY-745
			if (applicantName==null || applicantName.equals("")){
				if(appId!=null) {
					apprtnInfo = applicantService.queryAllApplicantByAppId(gcon, appId);
				} else {
					apprtnInfo = applicantService.queryAllApplicantByCustId(gcon, custId);
				}
				if(!apprtnInfo.getSuccess()) throw new Exception("申请人查询失败");
				List<Applicant> applicants = (List<Applicant>) apprtnInfo.getData();
				if(applicants!=null && applicants.size()>0){
					for(Applicant app:applicants) {
						String appName=app.getApplicantName();
						if (!applicantNameList.contains(appName)){
							applicantNameList.add(appName);
						}				
					}
				}	
			}
			//Modification end
	
					
			/*}*/
			
			SolrSend solr = new SolrSend();
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			returnInfo.setCurrPage(gcon.getPageNo());

			if(dateType==null) {
				dateType =0;
			}
			
			String dateName = "";
			switch (dateType) {
			case 0:
				dateName = "appDate";
				break;
			case 1:
				dateName = "regNoticeDate";
				break;
			case 2:
				dateName = "validEndDate";
				break;
			}
			//if(dateType.intValue()==1) dateName = "regNoticeDate";
			
			return solr.selectTrademark(gcon,applicantNameList, trademark, startYear, endYear,
					dateName, solrInfo, returnInfo,flag);
			
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误"+":"+e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}

	@Override
	public ReturnInfo queryTrademarkbyRenumber(Trademark trademark, GeneralCondition gcon) {
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			Trademark ret = selectTrademarkbyRenumber(trademark.getRegNumber(), gcon);
			returnInfo.setSuccess(true);
			returnInfo.setData(ret);
			returnInfo.setMessage("查询成功");
			return returnInfo;
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误"+":"+e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}

	

	@Override
	public ReturnInfo queryTmDetail(Trademark trademark) {
		ReturnInfo returnInfo = new ReturnInfo();
		Trademark ret = tmDetail(trademark.getTmId());
		returnInfo.setSuccess(true);
		returnInfo.setData(ret);
		returnInfo.setMessage("查询成功");
		return returnInfo;
	}
	
	private Trademark tmDetail(Integer tmId){
		Trademark trademark = trademarkMapper.selectByPrimaryKey(tmId);
		trademark.setTrademarkCategories(trademarkCategoryMapper.selectByTmId(tmId));
		trademark.setTrademarkProcesses(trademarkProcessMapper.selectByTmId(tmId));
		return trademark;
	}
	
	
	//商标状态统计实现（以前的实现）
	@Override
	public ReturnInfo statsTmStautsList(Integer custId,Integer appId, Integer startYear,Integer endYear,GeneralCondition gcon)  {
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			if(custId == null && appId == null) {
				returnInfo.setSuccess(false);
				returnInfo.setMessage("参数错误：custId和appId都为空");				
				return returnInfo;
			}
			List<String> applicantNameList = new ArrayList<String>();
			ReturnInfo apprtnInfo = null;
			
			long startGetImag = System.currentTimeMillis();			
			
			if(appId!=null) {
				apprtnInfo = applicantService.queryAllApplicantByAppId(gcon, appId);
			} else {
				apprtnInfo = applicantService.queryAllApplicantByCustId(gcon, custId);
			}			

			long endGetImag = System.currentTimeMillis();			
			
			long time=endGetImag-startGetImag;
			
			logger.debug("queryAllApplicant time: "+ time +" ms");
			

			if(!apprtnInfo.getSuccess()) throw new Exception("申请人查询失败");
			List<Applicant> applicants = (List<Applicant>) apprtnInfo.getData();
			if(applicants!=null && applicants.size()>0){
				for(Applicant app:applicants) {
					String appName=app.getApplicantName();
					if (!applicantNameList.contains(appName)){
						applicantNameList.add(appName);
					}				
				}
			}	
			SolrSend solr = new SolrSend();
			List<String> fieldList = new ArrayList<String>();
			fieldList.add("tmStatus");
			
			startGetImag = System.currentTimeMillis();	

			returnInfo.setData(solr.SelectTradeMarkByList(solrInfo,2,applicantNameList, startYear, endYear,fieldList,"appDate",false));

			
			endGetImag = System.currentTimeMillis();	
			
			time=endGetImag-startGetImag;
			
			logger.debug("solr.SelectTradeMarkByList time: "+ time +" ms");
			
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			return returnInfo;
			
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误"+":"+e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}

	}


	@Override
	public ReturnInfo statsTmAppdateRegDateList(Integer CustID,Integer appId,  Integer startYear, Integer endYear,
			GeneralCondition gcon,Integer flag) {
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			if(CustID == null&&appId == null) throw new Exception("客户信息有误");
			List<String> applicantNameList = new ArrayList<String>();
			ReturnInfo apprtnInfo = null;
			if(appId!=null) {
				apprtnInfo = applicantService.queryAllApplicantByAppId(gcon, appId);
			} else {
				apprtnInfo = applicantService.queryAllApplicantByCustId(gcon, CustID);
			}

			if(!apprtnInfo.getSuccess()) throw new Exception("申请人查询失败");
			List<Applicant> applicants = (List<Applicant>) apprtnInfo.getData();
			if(applicants!=null && applicants.size()>0){
				for(Applicant app:applicants) {
					String appName=app.getApplicantName();
					if (!applicantNameList.contains(appName)){
						applicantNameList.add(appName);
					}				
				}
			}		
//			List<String> applicantNameList = new ArrayList<String>();
//			applicantNameList.add("七彩云南通用航空有限责任公司");
			SolrSend solr = new SolrSend();
			List<String> fieldList = new ArrayList<String>();
			fieldList.add("appDateYear");
			Map<String,Object> ret = (Map<String,Object>)solr.SelectTradeMarkByList(solrInfo,flag, applicantNameList, startYear, endYear,fieldList,"appDate",false);
			List<String> fieldList2 = new ArrayList<String>();
			fieldList2.add("regNoticeDateYear");			
			Map<String,Object> ret0 = (Map<String,Object>)solr.SelectTradeMarkByList(solrInfo,flag, applicantNameList, startYear, endYear,fieldList2,"regNoticeDate",false);
			ret.putAll(ret0);
			orderbyyear(ret);
			returnInfo.setData(ret);
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			return returnInfo;
			
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误"+":"+e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}


	private void orderbyyear(Map<String, Object> ret) {
		// TODO 自动生成的方法存根
		
	}


	@Override
	public ReturnInfo statsTmTypeList(Integer CustID,Integer appId, Integer startYear, Integer endYear, GeneralCondition gcon,Integer flag){
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			if(CustID == null&&appId == null) throw new Exception("客户信息有误");
			List<String> applicantNameList = new ArrayList<String>();
			ReturnInfo apprtnInfo = null;
			if(appId!=null) {
				apprtnInfo = applicantService.queryAllApplicantByAppId(gcon, appId);
			} else {
				apprtnInfo = applicantService.queryAllApplicantByCustId(gcon, CustID);
			}

			if(!apprtnInfo.getSuccess()) throw new Exception("申请人查询失败");
			List<Applicant> applicants = (List<Applicant>) apprtnInfo.getData();
			if(applicants!=null && applicants.size()>0){
				for(Applicant app:applicants) {
					String appName=app.getApplicantName();
					if (!applicantNameList.contains(appName)){
						applicantNameList.add(appName);
					}				
				}
			}		
			SolrSend solr = new SolrSend();
			List<String> fieldList = new ArrayList<String>();
			fieldList.add("tmType");
			returnInfo.setData(solr.SelectTradeMarkByList(solrInfo,flag, applicantNameList, startYear, endYear,fieldList,"appDate",true));
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			return returnInfo;
			
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误"+":"+e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}


	@Override
	public ReturnInfo statsTmAgentList(Integer CustID,Integer appId,  Integer startYear, Integer endYear, GeneralCondition gcon,Integer flag){
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			if(CustID == null&&appId == null) throw new Exception("客户信息有误");
			List<String> applicantNameList = new ArrayList<String>();
			ReturnInfo apprtnInfo = null;
			if(appId!=null) {
				apprtnInfo = applicantService.queryAllApplicantByAppId(gcon, appId);
			} else {
				apprtnInfo = applicantService.queryAllApplicantByCustId(gcon, CustID);
			}

			if(!apprtnInfo.getSuccess()) throw new Exception("申请人查询失败");
			List<Applicant> applicants = (List<Applicant>) apprtnInfo.getData();
			if(applicants!=null && applicants.size()>0){
				for(Applicant app:applicants) {
					String appName=app.getApplicantName();
					if (!applicantNameList.contains(appName)){
						applicantNameList.add(appName);
					}				
				}
			}		
			SolrSend solr = new SolrSend();
			List<String> fieldList = new ArrayList<String>();
			fieldList.add("appDateYear");
			fieldList.add("agent");
			returnInfo.setData(fullTwoDeep(solr.SelectTradeMarkByList(solrInfo,flag, applicantNameList, startYear, endYear,fieldList,"appDate",true)));
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			return returnInfo;
			
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误"+":"+e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}


	@Override
	public ReturnInfo statsTmAppnameList(Integer CustID, Integer startYear, Integer endYear, GeneralCondition gcon, Integer regFlag,int flag){
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			if(CustID == null) throw new Exception("客户信息有误");
			List<String> applicantNameList = new ArrayList<String>();
			
			ReturnInfo apprtnInfo =  applicantService.queryAllApplicantByCustId(gcon, CustID);
			
			if(!apprtnInfo.getSuccess()) throw new Exception("申请人查询失败");
			List<Applicant> applicants = (List<Applicant>) apprtnInfo.getData();
			for(Applicant app:applicants) {
				String appName=app.getApplicantName();
				if (!applicantNameList.contains(appName)){
					applicantNameList.add(appName);
				}				
			}	
			
			SolrSend solr = new SolrSend();
			List<String> fieldList = new ArrayList<String>();
			fieldList.add("applicantName");
			
			//Modification start, by yang guang, 2018-12-19
			//由于很多商标的注册日期这个属性的值是空的，导致统计的结果与实际商标数量差距很大
			//因此，即使统计，也按申请日期统计
			//如果regFlag=1，是执行统计，否则，是获取该申请人的商标数量
			//统计使用注册日期，即：regNoticeDate，否则，使用申请日期，即：appDate
//			String tongjiObject=null;
//			if (regFlag==null || regFlag==1){
//				tongjiObject="regNoticeDate";
//			}else{
//				tongjiObject="appDate";				
//			}
			
			//Modification end
			
			String  tongjiObject="appDate";		
			
			returnInfo.setData(fullAppNameList((Map<String, Object>) solr.SelectTradeMarkByList(solrInfo,flag,applicantNameList, startYear, endYear,fieldList,tongjiObject,true),applicants));
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			return returnInfo;
			
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误"+":"+e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}


	private Map<String, Object> fullAppNameList(Map<String, Object> selectTradeMarkByList, List<Applicant> applicants) {
		Map<String, Object> ret = new HashMap<String, Object>();
		List<Map<String, Object>> retlist = new ArrayList<Map<String, Object>>();
		if(selectTradeMarkByList.get("applicantName") == null) return selectTradeMarkByList;
		List<Map<String, Object>> appnamelist = (List<Map<String, Object>>)selectTradeMarkByList.get("applicantName");
		Map<Integer, Integer> namecount = new HashMap<Integer, Integer>();
		Map<Integer, Object> namelist = new HashMap<Integer, Object>();
		Map<Integer, Object> ennamelist = new HashMap<Integer, Object>();
		Map<String, Integer> mainlist = new HashMap<String, Integer>();
		for(Applicant app:applicants) {
			Integer id=app.getId();
			String applicantName=app.getApplicantName();
			String applicantEnName=app.getApplicantEnName();
			Integer mainAppId=app.getMainAppId();
			namelist.put(id, applicantName);
			mainlist.put(applicantName,mainAppId);
			ennamelist.put(id, applicantEnName);
		}
		for(Map<String, Object> appname:appnamelist) {
			Integer count = namecount.get(mainlist.get(appname.get("applicantName").toString()));
			if(count == null) {
				count = Integer.valueOf(appname.get("count").toString());
			} else {
				count = Integer.valueOf(appname.get("count").toString()) + count;
			}
			namecount.put(mainlist.get(appname.get("applicantName").toString()), count);
		}
		for(Map.Entry<Integer, Integer> namecountmap:namecount.entrySet()) {
			Map<String, Object> retur = new HashMap<String, Object>();
			retur.put("applicantName", namelist.get(namecountmap.getKey()));
			retur.put("applicantEnName", ennamelist.get(namecountmap.getKey()));
			retur.put("count", namecountmap.getValue());
			retlist.add(retur);
		}
		//统计的商标的名称 结果为0 的 默认有值
		List<Map<String, Object>> m1 = new ArrayList<>();
		if(retlist.size()!=applicants.size()){
			for(Applicant app:applicants){
				String applicantName = app.getApplicantName();
				String applicantEnName = app.getApplicantEnName();
				int count = 0;
				for(Map<String, Object> result:retlist){
					if(!result.get("applicantName").equals(applicantName)){
						count++;
					}
				}
				if(count == retlist.size()){
					Map<String, Object> map = new HashMap<>();
					map.put("applicantName",applicantName);
					map.put("applicantEnName",applicantEnName);
					map.put("count",0);
					m1.add(map);	
				}
			}
		}
		retlist.addAll(m1);
		ret.put("applicantName", retlist);
		return ret;
	}


	@Override
	public ReturnInfo statsTmNameList(Integer CustID,Integer appId,  Integer startYear, Integer endYear, GeneralCondition gcon,Integer flag){
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			if(CustID == null&&appId == null) throw new Exception("客户信息有误");
			List<String> applicantNameList = new ArrayList<String>();
			ReturnInfo apprtnInfo = null;
			if(appId!=null) {
				apprtnInfo = applicantService.queryAllApplicantByAppId(gcon, appId);
			} else {
				apprtnInfo = applicantService.queryAllApplicantByCustId(gcon, CustID);
			}

			if(!apprtnInfo.getSuccess()) throw new Exception("申请人查询失败");
			List<Applicant> applicants = (List<Applicant>) apprtnInfo.getData();
			if(applicants!=null && applicants.size()>0){
				for(Applicant app:applicants) {
					String appName=app.getApplicantName();
					if (!applicantNameList.contains(appName)){
						applicantNameList.add(appName);
					}				
				}
			}		
			SolrSend solr = new SolrSend();
			List<String> fieldList = new ArrayList<String>();
			fieldList.add("tmName");
			Object object = solr.SelectTradeMarkByList(solrInfo,flag, applicantNameList, startYear, endYear,fieldList,"appDate",true);
			returnInfo.setData(object);
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			return returnInfo;
			
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误"+":"+e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}
	
	
	@Override
	public ReturnInfo statsTmDongtai(Integer CustID,Integer appId,  Integer startYear, Integer endYear, GeneralCondition gcon){
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			if(CustID == null&&appId == null) throw new Exception("客户信息有误");
			List<String> applicantNameList = new ArrayList<String>();
			
			ReturnInfo apprtnInfo = null;
			if(appId!=null) {
				apprtnInfo = applicantService.queryAllApplicantByAppId(gcon, appId);
			} else {
				apprtnInfo = applicantService.queryAllApplicantByCustId(gcon, CustID);
			}

			if(!apprtnInfo.getSuccess()) throw new Exception("申请人查询失败");
			List<Applicant> applicants = (List<Applicant>) apprtnInfo.getData();
			if(applicants!=null && applicants.size()>0){
				for(Applicant app:applicants) {
					String appName=app.getApplicantName();
					if (!applicantNameList.contains(appName)){
						applicantNameList.add(appName);
					}				
				}
			}		
			

			SolrSend solr = new SolrSend();
			List<String> fieldList = new ArrayList<String>();
			fieldList.add("tmName");
			returnInfo.setData(solr.statsTmDongtai(solrInfo, applicantNameList, startYear, endYear));
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			return returnInfo;
			
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误"+":"+e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}
	@Override
	public ReturnInfo statsTmBeibohuiDongtai(Integer CustID,Integer appId,  Integer startYear, Integer endYear, GeneralCondition gcon){
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			if(CustID == null&&appId == null) throw new Exception("客户信息有误");
			List<String> applicantNameList = new ArrayList<String>();
			ReturnInfo apprtnInfo = null;
			long start = System.currentTimeMillis();
			if(appId!=null) {
				apprtnInfo = applicantService.queryAllApplicantByAppId(gcon, appId);
			} else {
				apprtnInfo = applicantService.queryAllApplicantByCustId(gcon, CustID);
			}
			
			if(!apprtnInfo.getSuccess()) throw new Exception("申请人查询失败");
			List<Applicant> applicants = (List<Applicant>) apprtnInfo.getData();
			if(applicants!=null && applicants.size()>0){
				for(Applicant app:applicants) {
					String appName=app.getApplicantName();
					if (!applicantNameList.contains(appName)){
						applicantNameList.add(appName);
					}				
				}
			}		
			long end = System.currentTimeMillis();
			System.err.println("数据库查询用时："+(end-start)+"毫秒");
			SolrSend solr = new SolrSend();
			List<String> fieldList = new ArrayList<String>();
			fieldList.add("tmName");
			long startSolr = System.currentTimeMillis();
			Map<Integer, Object> map = solr.statsTmBeibohuiDongtai(solrInfo, applicantNameList, startYear, endYear);
			long endSolr = System.currentTimeMillis();
			System.err.println("solr查询用时："+(endSolr-startSolr)+"毫秒");
			returnInfo.setData(map);
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			return returnInfo;
			
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误"+":"+e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}
	
	
	//通过tmProcessCore 来统计异常
	@Override
	public ReturnInfo statisByTmProcess(Integer custId, Integer appId,
			Integer startYear, Integer endYear, GeneralCondition gcon,Integer status,int flag) {
		ReturnInfo returnInfo =new ReturnInfo();
		SolrSend solrSend = new SolrSend();
		String query ="";
		if(appId !=null){
			query = "mainAppId:"+appId+" AND status:"+status;
		}else{
			query = "custId:"+custId+" AND status:"+status;
		}
		String qString = "";
		if(flag == 0){
			qString = "*:*";
		}else if(flag == 1){
			qString+="regNumber:G*";
		}else{
			qString+="NOT regNumber:G*";
		}
		
		List<Map<String, Object>>result = solrSend.statisByTmProcessCore(solrInfo, query,qString, startYear, endYear);
		returnInfo.setData(result);
		
		return returnInfo;
	}
	
	
	
	
	@Override
	public ReturnInfo statsTmBeichesanDongtai(Integer CustID,Integer appId,  Integer startYear, Integer endYear, GeneralCondition gcon){
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			if(CustID == null&&appId == null) throw new Exception("客户信息有误");
			List<String> applicantNameList = new ArrayList<String>();
			ReturnInfo apprtnInfo = null;
			long start1 = System.currentTimeMillis();
			if(appId!=null) {
				apprtnInfo = applicantService.queryAllApplicantByAppId(gcon, appId);
			} else {
				apprtnInfo = applicantService.queryAllApplicantByCustId(gcon, CustID);
			}
			if(!apprtnInfo.getSuccess()) throw new Exception("申请人查询失败");
			List<Applicant> applicants = (List<Applicant>) apprtnInfo.getData();
			if(applicants!=null && applicants.size()>0){
				for(Applicant app:applicants) {
					String appName=app.getApplicantName();
					if (!applicantNameList.contains(appName)){
						applicantNameList.add(appName);
					}				
				}
			}		
			long end1 = System.currentTimeMillis();
			System.err.println("数据库查询用时"+(end1-start1)+"毫秒");
			SolrSend solr = new SolrSend();
			List<String> fieldList = new ArrayList<String>();
			fieldList.add("tmName");
			long start2 = System.currentTimeMillis();
			returnInfo.setData(solr.statsTmBeichesanDongtai(solrInfo, applicantNameList, startYear, endYear));
			long end2 = System.currentTimeMillis();
			System.err.println("solr查询用时"+(end2-start2)+"毫秒");
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			return returnInfo;
			
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误"+":"+e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}
	@Override
	public ReturnInfo statsTmBeiwuxiaoDongtai(Integer CustID,Integer appId,  Integer startYear, Integer endYear, GeneralCondition gcon){
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			if(CustID == null&&appId == null) throw new Exception("客户信息有误");
			List<String> applicantNameList = new ArrayList<String>();
			ReturnInfo apprtnInfo = null;
			long start1 = System.currentTimeMillis();
			if(appId!=null) {
				apprtnInfo = applicantService.queryAllApplicantByAppId(gcon, appId);
			} else {
				apprtnInfo = applicantService.queryAllApplicantByCustId(gcon, CustID);
			}
			
			if(!apprtnInfo.getSuccess()) throw new Exception("申请人查询失败");
			List<Applicant> applicants = (List<Applicant>) apprtnInfo.getData();
			if(applicants!=null && applicants.size()>0){
				for(Applicant app:applicants) {
					String appName=app.getApplicantName();
					if (!applicantNameList.contains(appName)){
						applicantNameList.add(appName);
					}				
				}
			}		
			long end1 = System.currentTimeMillis();
			System.err.println("数据库查询耗时"+(end1-start1)+"毫秒");
			SolrSend solr = new SolrSend();
			List<String> fieldList = new ArrayList<String>();
			fieldList.add("tmName");
			long start2 = System.currentTimeMillis();
			returnInfo.setData(solr.statsTmBeiwuxiaoDongtai(solrInfo, applicantNameList, startYear, endYear));
			long end2 = System.currentTimeMillis();
			System.err.println("solr查询耗时"+(end2-start2)+"毫秒");
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			return returnInfo;
			
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误"+":"+e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}
	@Override
	public ReturnInfo statsTmBeiyiyiDongtai(Integer CustID,Integer appId,  Integer startYear, Integer endYear, GeneralCondition gcon){
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			if(CustID == null&&appId == null) throw new Exception("客户信息有误");
			List<String> applicantNameList = new ArrayList<String>();
			ReturnInfo apprtnInfo = null;
			long start = System.currentTimeMillis();
			if(appId!=null) {
				apprtnInfo = applicantService.queryAllApplicantByAppId(gcon, appId);
			} else {
				apprtnInfo = applicantService.queryAllApplicantByCustId(gcon, CustID);
			}

			if(!apprtnInfo.getSuccess()) throw new Exception("申请人查询失败");
			List<Applicant> applicants = (List<Applicant>) apprtnInfo.getData();
			if(applicants!=null && applicants.size()>0){
				for(Applicant app:applicants) {
					String appName=app.getApplicantName();
					if (!applicantNameList.contains(appName)){
						applicantNameList.add(appName);
					}				
				}
			}		
			long end = System.currentTimeMillis();
			System.err.println("数据库用时:"+(end-start)+"毫秒");
			SolrSend solr = new SolrSend();
			List<String> fieldList = new ArrayList<String>();
			fieldList.add("tmName");
			long start2 = System.currentTimeMillis();
			returnInfo.setData(solr.statsTmBeiyiyiDongtai(solrInfo, applicantNameList, startYear, endYear));
			long end2 = System.currentTimeMillis();
			System.err.println("solr耗时"+(end2-start2)+"毫秒");
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			return returnInfo;
			
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误"+":"+e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}
	@Override
	public ReturnInfo statsTmBiangengDongtai(Integer CustID,Integer appId,  Integer startYear, Integer endYear, GeneralCondition gcon){
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			if(CustID == null&&appId == null) throw new Exception("客户信息有误");
			List<String> applicantNameList = new ArrayList<String>();
			ReturnInfo apprtnInfo = null;
			long start1 = System.currentTimeMillis();
			if(appId!=null) {
				apprtnInfo = applicantService.queryAllApplicantByAppId(gcon, appId);
			} else {
				apprtnInfo = applicantService.queryAllApplicantByCustId(gcon, CustID);
			}

			if(!apprtnInfo.getSuccess()) throw new Exception("申请人查询失败");
			List<Applicant> applicants = (List<Applicant>) apprtnInfo.getData();
			if(applicants!=null && applicants.size()>0){
				for(Applicant app:applicants) {
					String appName=app.getApplicantName();
					if (!applicantNameList.contains(appName)){
						applicantNameList.add(appName);
					}				
				}
			}		
			long end1 = System.currentTimeMillis();
			System.err.println("数据库查询耗时:"+(end1-start1)+"毫秒");
			SolrSend solr = new SolrSend();
			List<String> fieldList = new ArrayList<String>();
			fieldList.add("tmName");
			long start2 = System.currentTimeMillis();
			returnInfo.setData(solr.statsTmBiangengDongtai(solrInfo, applicantNameList, startYear, endYear));
			long end2 = System.currentTimeMillis();
			System.err.println("solr查询耗时"+(end2-start2)+"毫秒");
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			return returnInfo;
			
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误"+":"+e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}
	@Override
	public ReturnInfo statsTmTongyongmingchengDongtai(Integer CustID,Integer appId,  Integer startYear, Integer endYear, GeneralCondition gcon){
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			if(CustID == null&&appId == null) throw new Exception("客户信息有误");
			List<String> applicantNameList = new ArrayList<String>();
			ReturnInfo apprtnInfo = null;
			if(appId!=null) {
				apprtnInfo = applicantService.queryAllApplicantByAppId(gcon, appId);
			} else {
				apprtnInfo = applicantService.queryAllApplicantByCustId(gcon, CustID);
			}

			if(!apprtnInfo.getSuccess()) throw new Exception("申请人查询失败");
			List<Applicant> applicants = (List<Applicant>) apprtnInfo.getData();
			if(applicants!=null && applicants.size()>0){
				for(Applicant app:applicants) {
					String appName=app.getApplicantName();
					if (!applicantNameList.contains(appName)){
						applicantNameList.add(appName);
					}				
				}
			}		

			SolrSend solr = new SolrSend();
			List<String> fieldList = new ArrayList<String>();
			fieldList.add("tmName");
			returnInfo.setData(solr.statsTmTongyongmingchengDongtai(solrInfo, applicantNameList, startYear, endYear));
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			return returnInfo;
			
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误"+":"+e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}
	
	
	
	@Override
	public ReturnInfo statstmxuzhan(Integer custId,Integer appId, Integer startYear, Integer endYear, GeneralCondition gcon,Integer flag) {
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			if(custId == null&&appId == null) throw new Exception("客户信息有误");
			List<String> applicantNameList = new ArrayList<String>();
			ReturnInfo apprtnInfo = null;
			if(appId!=null) {
				apprtnInfo = applicantService.queryAllApplicantByAppId(gcon, appId);
			} else {
				apprtnInfo = applicantService.queryAllApplicantByCustId(gcon, custId);
			}

			if(!apprtnInfo.getSuccess()) throw new Exception("申请人查询失败");
			List<Applicant> applicants = (List<Applicant>) apprtnInfo.getData();
			if(applicants!=null && applicants.size()>0){
				for(Applicant app:applicants) {
					String appName=app.getApplicantName();
					if (!applicantNameList.contains(appName)){
						applicantNameList.add(appName);
					}				
				}
			}		
			SolrSend solr = new SolrSend();
			List<String> fieldList = new ArrayList<String>();
			fieldList.add("validEndDateYear");
			returnInfo.setData(solr.SelectTradeMarkByList(solrInfo,flag, applicantNameList, startYear, endYear,fieldList,"validEndDate",true));
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			return returnInfo;
			
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误"+":"+e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}


	public ReturnInfo statsTmzhongzhi(Integer CustID, Integer startYear, Integer endYear, GeneralCondition gcon){
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			if(CustID == null) throw new Exception("客户信息有误");
			List<String> applicantNameList = new ArrayList<String>();
			
			ReturnInfo apprtnInfo =  applicantService.queryAllApplicantByCustId(gcon, CustID);
			
			if(!apprtnInfo.getSuccess()) throw new Exception("申请人查询失败");
			List<Applicant> applicants = (List<Applicant>) apprtnInfo.getData();
			for(Applicant app:applicants) {
				String appName=app.getApplicantName();
				if (!applicantNameList.contains(appName)){
					applicantNameList.add(appName);
				}				
			}	
			SolrSend solr = new SolrSend();
			List<String> fieldList = new ArrayList<String>();
			fieldList.add("tmName");
//			returnInfo.setData(solr.statsTmDongtai(solrInfo, applicantNameList, startYear, endYear));
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
			return returnInfo;
			
		} catch (Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询有误"+":"+e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}
	}
	
	
	@Override
	public ReturnInfo queryAgentByAppName(String appName){
		ReturnInfo rtnInfo = new ReturnInfo();
		if (appName==null || appName.equals("")){
		
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("参数appName不能为空");
			rtnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
			return rtnInfo;
		}
		
		List<Agency> agencyIdList = trademarkMapper.selectAgentByAppName(appName);
		
		rtnInfo.setSuccess(true);
		rtnInfo.setData(agencyIdList);
		rtnInfo.setMessage("查询该申请人的商标的代理所成功");
		
		
		return rtnInfo;
	}

	//对查询的数据进行二次封装
	private Object fullTwoDeep(Object list) {
		List<String> titlelist = new ArrayList<String>();
		List<Map<String, Object>> retlist = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> a:(List<Map<String, Object>>) list) {				
			if(a.get("value") instanceof  List) {
				for(Map<String, Object> b:(List<Map<String, Object>>) a.get("value")) {
					if(!titlelist.contains(b.get("name"))) {
						titlelist.add(b.get("name").toString());
					}
				}
			}				
		}
		for(Map<String, Object> a:(List<Map<String, Object>>) list) {				
			if(a.get("value") instanceof  List) {
				for(String title:titlelist) {
					if(!mapexitvalue((List<Map<String, Object>>)a.get("value"),title)) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("value", 0);
						map.put("name", title);
						((List<Map<String, Object>>)a.get("value")).add(map);
					}
				}
				
			} else {
				List<Map<String, Object>> maplist = new ArrayList<Map<String, Object>>();
				for(String title:titlelist) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("value", 0);
					map.put("name", title);
					maplist.add(map);
				}
				a.put("value", maplist);
			}
			retlist.add(a);
		}
		return retlist;
	}

	private boolean mapexitvalue(List<Map<String, Object>> list, String title) {
		for(Map<String, Object> a:list) {
			if(a.get("name").toString().equals(title)) return true;
		}
		return false;
	}

	

	
	private List<TmStatusDTO> getStatus(List<String> applicanNames,String time) throws Exception{
		SolrSend solrSend = new SolrSend();
		String qString ="";
		if(applicanNames.size()==0){
			
		}else{
			//拼接查询语句
			qString = qString+"applicantName:(";
			for(String applicantName:applicanNames) {
				qString = qString+"\""+applicantName+"\"   OR ";
	    	}
			qString = qString.substring(0, qString.length()-4);
			qString = qString+")";
		}
		//根据tmStatus
		long count1 = solrSend.statisticsTmStatus(solrInfo, "tmStatus","已注册",qString,time);
		long count2 = solrSend.statisticsTmStatus(solrInfo, "tmStatus","已无效",qString,time);
		long count3 = solrSend.statisticsTmStatus(solrInfo, "tmStatus","申请中",qString,time);
		//根据status
		long statusCount1 = solrSend.statisticsTmStatus(solrInfo, "status","注册",qString,time);
		long statusCount2 = solrSend.statisticsTmStatus(solrInfo, "status","注册公告",qString,time);
		long statusCount3 = solrSend.statisticsTmStatus(solrInfo, "status","已注册",qString,time);
		
		long statusCount4 = solrSend.statisticsTmStatus(solrInfo, "status","已销亡",qString,time);
		long statusCount5 = solrSend.statisticsTmStatus(solrInfo, "status","已驳回",qString,time);
		long statusCount6 = solrSend.statisticsTmStatus(solrInfo, "status","其他情形",qString,time);
		long statusCount7 = solrSend.statisticsTmStatus(solrInfo, "status","无效（仅供参考）",qString,time);
		
		long statusCount8 = solrSend.statisticsTmStatus(solrInfo, "status","已初审",qString,time);
		long statusCount9 = solrSend.statisticsTmStatus(solrInfo, "status","待审中",qString,time);
		long statusCount10 = solrSend.statisticsTmStatus(solrInfo, "status","初审公告",qString,time);
		long statusCount11 = solrSend.statisticsTmStatus(solrInfo, "status","等待实质审查",qString,time);
		long statusCount12 = solrSend.statisticsTmStatus(solrInfo, "status","驳回复审中",qString,time);
		long statusCount13 = solrSend.statisticsTmStatus(solrInfo, "status","撤销/无效宣告申请审查中",qString,time);
		long statusCount14 = solrSend.statisticsTmStatus(solrInfo, "status","异议中", qString, time);
		long statusCount15 = solrSend.statisticsTmStatus(solrInfo, "status", "此商标正等待受理，暂无法查询详细信息", qString, time);
		
		
		Map<String, Long> map1 = new  HashMap<>();
		Map<String, Long> map2 = new HashMap<>();
		Map<String, Long> map3 = new HashMap<>();
		map1.put("注册", statusCount1);
		map1.put("注册公告", statusCount2);
		map1.put("已注册", statusCount3);
		map1.put("其他情形", statusCount6);
		
		map2.put("已销亡", statusCount4);
		map2.put("已驳回", statusCount5);
		map2.put("无效(仅供参考)", statusCount7);
		
		map3.put("已初审", statusCount8);
		map3.put("待审中", statusCount9);
		map3.put("初审公告", statusCount10);
		map3.put("等待实质审查", statusCount11);
		map3.put("驳回复审中", statusCount12);
		map3.put("撤销/无效宣告申请审查中",statusCount13);
		map3.put("异议中", statusCount14);
		map3.put("此商标正等待受理，暂无法查询详细信息", statusCount15);
		
		Map<String,Object> mapObj1 = new HashMap<>();
		Map<String,Object> mapObj2 = new HashMap<>();
		Map<String,Object> mapObj3 = new HashMap<>();
		mapObj1.put("已注册", map1);
		mapObj2.put("已无效", map2);
		mapObj3.put("申请中", map3);
		
		String[] arr =new String[]{"已注册","已无效","申请中"};
		Long[] countArr = new Long[]{count1,count2,count3};
		Object[] mapArr = new Object[]{mapObj1,mapObj2,mapObj3};
		List<TmStatusDTO> lists = new ArrayList<>();
		for(int i=0;i<arr.length;i++){
			TmStatusDTO statusDTO = new TmStatusDTO();
			statusDTO.setCount(countArr[i]);
			statusDTO.setName(arr[i]);
			statusDTO.setDetailType((Map<String, Object>)mapArr[i]);
			lists.add(statusDTO);
		}
		return lists;
	}
	
	@Override
	public ReturnInfo statisticTmStatus(GeneralCondition gcon, Integer custId,
			Integer appId,Integer startYear,Integer endYear,Integer flag) throws Exception {
		
		String time= "";	
		//Modification start, by yang guang, 2018-12-19
		if (startYear!=null && endYear!=null){
			time= " AND appDateYear:["+startYear+" TO "+endYear+"]";
		}
		//Modification end
		
		
		time+= "AND NOT regNumber:G*";
		//0 查全部 ，默认查国内;
		if(flag == 0){
    		time+=" ";
    	}else if(flag == 1){
    		time+=" AND regNumber:G*";
    	}
		//登录的用户id 查询出所管辖的申请人
			//根据申请人获取名称
			//根据状态和申请人名称一起查
		List<Applicant> applicants;
		if(appId!=null){
			 applicants = applicantMapper.selectAllApplicantById(appId);
		}else{
			if(custId == null) throw new Exception();
			applicants= applicantMapper.selectAllApplicantByCustId(custId);
		}
		//申请名称列表
		List<String> applicantName = new ArrayList<>();
		for(Applicant app:applicants){
			String name = app.getApplicantName();
			if(!applicantName.contains(name))applicantName.add(name);
		}
			List<TmStatusDTO> lists = this.getStatus(applicantName,time);
			ReturnInfo returnInfo = new ReturnInfo();
			returnInfo.setData(lists);
			return returnInfo;
		
	}

	/**
	 * 根据状态(tmStatus)查询商标状态数据
	 */
	@Override
	public ReturnInfo statisticTmDate(GeneralCondition gcon, Integer custId,Integer appId,
			String tmStatus,Integer flag,Integer startYear,Integer endYear)throws Exception {
		ReturnInfo returnInfo = new ReturnInfo();
		String time="";
		
		//Modification start, by yang guang, 2018-12-19
		if (startYear!=null && endYear!=null){
			time= " AND appDateYear:["+startYear+" TO "+endYear+"]";
		}
		//Modification end
		
		time+= "AND NOT regNumber:G*";
		//0 查全部 ，默认查国内;
		if(flag == 0){
    		time+=" ";
    	}else if(flag == 1){
    		time+=" AND regNumber:G*";
    	}
		//登录的用户id 查询出所管辖的申请人
			//根据申请人获取名称
			//根据状态和申请人名称一起查
		List<Applicant> applicants;
		if(custId == null&&appId == null) throw new Exception("客户信息有误");
		if(appId!=null){
			 applicants = applicantMapper.selectAllApplicantById(appId);
		}else{
			applicants= applicantMapper.selectAllApplicantByCustId(custId);
		}
		//申请名称列表
		List<String> applicantNames = new ArrayList<>();
		for(Applicant app:applicants){
			String name = app.getApplicantName();
			if(!applicantNames.contains(name))applicantNames.add(name);
		}
		//
		SolrSend solrSend = new SolrSend();
		String qString ="";
		if(applicantNames.size()==0){
			
		}else{
			//拼接查询语句
			qString = qString+"applicantName:(";
			for(String applicantName:applicantNames) {
				qString = qString+"\""+applicantName+"\"   OR ";
	    	}
			qString = qString.substring(0, qString.length()-4);
			qString = qString+")";
		}
		//
		
		returnInfo = solrSend.statisticsTmStatusData(solrInfo,"tmStatus", tmStatus, qString, time, gcon);
		
		
		return returnInfo;
	}

	/**
	 * 查询申请机构的数据
	 */
	@Override
	public ReturnInfo statsTmAgentData(GeneralCondition gcon, Integer custId,
			Integer flag, Integer year,Integer appId) throws Exception {
		ReturnInfo returnInfo = new ReturnInfo();
		List<Applicant> applicants;
		if(custId == null&&appId == null) throw new Exception("客户信息有误");
		if(appId!=null){
			 applicants = applicantMapper.selectAllApplicantById(appId);
		}else{
			applicants= applicantMapper.selectAllApplicantByCustId(custId);
		}
		List<String> applicantNames = new ArrayList<>();
		for(Applicant app:applicants){
			String name = app.getApplicantName();
			if(!applicantNames.contains(name))applicantNames.add(name);
		}
		//拼接语句
		String time="appDateYear:"+year;
		String qFlag = " AND NOT regNumber:G*";
		if(flag ==0){
			qFlag =" ";
		}else if(flag == 1){
			qFlag =" AND regNumber:G*";
		}
		String qString = "";
		if(applicantNames.size()==0){
		}else{
			//拼接查询语句
			qString = qString+" AND applicantName:(";
			for(String applicantName:applicantNames) {
				qString = qString+"\""+applicantName+"\"   OR ";
	    	}
			qString = qString.substring(0, qString.length()-4);
			qString = qString+")";
		}
		//连接solr
		SolrSend solrSend = new SolrSend();
		String query = time+qFlag+qString;
		returnInfo = solrSend.statsTmAgentData(gcon, query, solrInfo);
		
		return returnInfo;
	}

	//查询商标异常数据
	@Override
	public ReturnInfo tmDynamicData(GeneralCondition gcon, Integer custId,
			Integer flag, Integer appId, Integer year, Integer abnormalType) {
		ReturnInfo returnInfo = new ReturnInfo();
		//根据appId 或者custId 查询数据
		List<String> appNames = queryBelongApplicant(appId, custId);
		
		//trademarkprocess:*\"status\"\:*等待驳回通知发文*,*\"statusDate\"\:*2015* AND applicantName:(达能（中国）食品饮料有限公司 OR 莱雅公司(法国)) AND NOT regNumber:G*
		
		// "(trademarkprocess:*\\\"status\\\"\\:"
		//	+values.get(0)+",*\\\"statusDate\\\"\\:*"+String.valueOf(year)+"*)    OR "
		//拼接语句
		//String qMainBody = " trademarkprocess:*\\\"status\\\"\\:"+abnormalType+",*\\\"statusDate\\\"\\:*"+year+"*";
		String qMainBody = ""; 
		String qjoint = this.jointQuery(abnormalType, year);
		qMainBody +=qjoint;
		
		// Modification start,2018-09-21，
		// in order to resolve error search string
		
		//String nation = "AND NOT regNumber:G*";
		String nation = " AND NOT regNumber:G*";
		
		// Modification end
		
		if(flag ==0){
			nation =" ";
		}else if(flag == 1){
			nation = " AND regNumber:G*";
		}
		qMainBody+=nation;
		//机构拼接
		if(appNames.size()==0){
		}else{
			//拼接查询语句
			qMainBody = qMainBody+" AND applicantName:(";
			for(String applicantName:appNames) {
				qMainBody = qMainBody+"\""+applicantName+"\"   OR ";
	    	}
			qMainBody = qMainBody.substring(0, qMainBody.length()-4);
			qMainBody = qMainBody+")";
		}
		//solr 查询
		SolrSend solrSend = new SolrSend();
		returnInfo = solrSend.statisTmDynamicData(gcon,qMainBody,solrInfo);
		
		return returnInfo;
	}
	
	//拼接查询语句  默认：(0:被驳回  1:被提起异议申请  2:被提起撤三申请  3:被提起无效宣告申请  4:被提起撤销通用名称申请  )
	private String jointQuery(Integer abnormalType,int year){
		String qString = "";
		if(abnormalType ==0){
			/*目标语句：(trademarkprocess:*\"status\"\:*等待驳回通知发文*,*\"statusDate\"\:*2010*)  OR
					 (trademarkprocess:*\"status\"\:*商标注册申请驳回通知发文*,*\"statusDate\"\:*2010*)  OR
					 (trademarkprocess:*\"status\"\:*商标注册申请等待驳回通知发文*,*\"statusDate\"\:*2010*)  OR 
					 (trademarkprocess:*\"status\"\:*打印驳回或部分驳回通知书*,*\"statusDate\"\:*2010*)  OR 
					 (trademarkprocess:*\"status\"\:*打印驳回通知*,*\"statusDate\"\:*2010*) 
			 */	
			String [] type = {TrademarkProcessStatus.beibohui_1,
							  TrademarkProcessStatus.beibohui_2,
							  TrademarkProcessStatus.beibohui_3,
							  TrademarkProcessStatus.beibohui_4,
							  TrademarkProcessStatus.beibohui_5,
							};
			String reject = "";
			for(int i=0;i<=4;i++){	
				reject +="(trademarkprocess:*\\\"status\\\"\\:"+type[i]+",*\\\"statusDate\\\"\\:*"+year+"-*)  OR ";
			}
			reject =reject.substring(0,reject.length()-4);		
			// Modification start,2018-09-21，
			// in order to resolve error search string
			if (!reject.equals("")){
				reject="("+reject+")";
			}
			// Modification end
			return reject;
		}else if(abnormalType == 1){
			String [] type = {TrademarkProcessStatus.beiyiyi_1,
								TrademarkProcessStatus.beiyiyi_2
							 };
			String beiYiyi = "";
			for(int i=0;i<=1;i++){
				beiYiyi +="(trademarkprocess:*\\\"status\\\"\\:"+type[i]+",*\\\"statusDate\\\"\\:*"+year+"*)  OR ";
			}
			beiYiyi = beiYiyi.substring(0, beiYiyi.length()-4);	
			if (!beiYiyi.equals("")){
				beiYiyi="("+beiYiyi+")";
			}
			return beiYiyi;
		}else if(abnormalType == 2){
			String beiCheSan = "(trademarkprocess:*\\\"status\\\"\\:"+TrademarkProcessStatus.beichesan+",*\\\"statusDate\\\"\\:*"+year+"*)";
			if (!beiCheSan.equals("")){
				beiCheSan="("+beiCheSan+")";
			}
			return beiCheSan;
		}else if(abnormalType == 3){
			String beiWuXiao = "(trademarkprocess:*\\\"status\\\"\\:"+TrademarkProcessStatus.beiwuxiao+",*\\\"statusDate\\\"\\:*"+year+"*)";
			if (!beiWuXiao.equals("")){
				beiWuXiao="("+beiWuXiao+")";
			}
			return beiWuXiao;
		}else {
			String beiTongYong = "(trademarkprocess:*\\\"status\\\"\\:"+TrademarkProcessStatus.tongyongmingcheng+",*\\\"statusDate\\\"\\:*"+year+"*)";
			if (!beiTongYong.equals("")){
				beiTongYong="("+beiTongYong+")";
			}
			return beiTongYong;
		}
		
	}
	
	private List<String> queryBelongApplicant(Integer appId,Integer custId){
		
		List<Applicant> applicants;
		if(appId!=null){
			 applicants = applicantMapper.selectAllApplicantById(appId);
		}else{
			applicants= applicantMapper.selectAllApplicantByCustId(custId);
		}
		List<String> applicantNames = new ArrayList<>();
		for(Applicant app:applicants){
			String name = app.getApplicantName();
			if(!applicantNames.contains(name))applicantNames.add(name);
		}
		
		return applicantNames;
	}

	
	private List<String>  getNumberList(List<Trademark> tms){
		List<String> regNumberList=new ArrayList<String>();
		for (Trademark trademark : tms) {			
			String regNumber=trademark.getRegNumber();		
			if (regNumber==null || regNumber.equals("")){
				continue;
			}
			if (!regNumberList.contains(regNumber)){
				regNumberList.add(regNumber);
			}
		}		
		
		return regNumberList;
	}
	
	
	private long processReturnInfo(List<Trademark>trademarklist, List<Trademark> resultList, Integer caseTypeId, int offset, int rows){
		
		List<String> regNumberList=getNumberList(trademarklist);
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("regNumberList", regNumberList);
		paramMap.put("caseTypeId", caseTypeId);
		List<TradeMarkCase> tmCaseList=tradeMarkCaseMapper.selectByRegNumberList(paramMap);
		
		if (tmCaseList!=null && tmCaseList.size()>0){			
			for(TradeMarkCase tmc:tmCaseList){
				String regNum=tmc.getRegNumber();
				Iterator<Trademark> it = trademarklist.iterator();
				while (it.hasNext()) {
					Trademark tm=it.next();	
					String regNumber=tm.getRegNumber();							
					if (regNumber!=null && regNum!=null && regNumber.equals(regNum)){
//						it.remove();
						tm.setReason("1");
						break;
					}
				}					
			}
		}					
		
		
		int size=trademarklist.size();
		long total=(long)size;
		
		int start=offset;
		int end=offset+rows;
		if (end>size){
			end=size;
		}
		
		for(int i=start;i<end;i++){
			resultList.add(trademarklist.get(i));
		}
		
		return total;
	}

	

	@Override
	public ReturnInfo updateGoods(String goodsPath, String gfPath){
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			
			String basePath=serviceUrlConfig.getFileUrl();
			if (goodsPath.startsWith("/")){
				goodsPath=basePath+ goodsPath;
			}else{
				goodsPath=basePath+"/"+ goodsPath;
			}
			if (gfPath.startsWith("/")){
				gfPath=basePath+ gfPath;				
			}else{
				gfPath=basePath+"/"+ gfPath;
			}
			
			trademarkCategoryUtil.setGoodsName(goodsPath);
			
			trademarkCategoryUtil.setGoodsEnName(gfPath);

			returnInfo.setSuccess(true);
			returnInfo.setMessage("更新商品/服务数据成功");
			return returnInfo;
		} catch(Exception e) {
			returnInfo.setSuccess(false);
			returnInfo.setMessage("商品/服务数据更新失败"+e.getMessage());
			e.printStackTrace();
			return returnInfo;
		}

	}
	
	
	/**
	 * 检测系统中是否存在该注册号
	 */
//	@Override
//	public ReturnInfo checkTm(String regNumber) {
//		ReturnInfo returnInfo = new ReturnInfo();
//		Trademark trademark = tmDetail(regNumber);
//		Map<String, String> map = new HashMap<String, String>();
//		if(trademark!=null){
//			map.put("result", "yes");//存在
//		}else{
//			map.put("result", "no");//不存在
//		}
//		returnInfo.setSuccess(true);
//		returnInfo.setData(map);
//		returnInfo.setMessage("查询成功");
//		return returnInfo;
//	}

	

//	@Override
//	public ReturnInfo queryTmListByTmId(Integer tmId, GeneralCondition gcon) {
//		ReturnInfo returnInfo = new ReturnInfo() ;
//		List<Trademark> trademarks=trademarkMapper.selectTrademarkByTmId(tmId);
//		returnInfo.setSuccess(true);
//		returnInfo.setData(trademarks);			
//		returnInfo.setMessage("查询成功");
//		return returnInfo;
//		
//	}

}
