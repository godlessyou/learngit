package com.yootii.bdy.customer.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.Globals;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.customer.dao.ApplicantMapper;
import com.yootii.bdy.customer.model.Applicant;
import com.yootii.bdy.customer.service.ApplicantService;
//import com.yootii.bdy.customer.service.CustomerService;
import com.yootii.bdy.util.GraspUtil;
import com.yootii.bdy.util.JsonUtil;
import com.yootii.bdy.util.ServiceUrlConfig;


@Service("ApplicantService")
public class ApplicantServiceImpl implements ApplicantService{
	
	@Resource
	private ApplicantMapper applicantMapper;	

//	@Resource
//	private CustomerService customerService;
	@Resource
	private ServiceUrlConfig serviceUrlConfig;
	
	
	@Override
	public ReturnInfo checkAndSaveApplicant(GeneralCondition gcon, Applicant applicant, Integer customerId) {
		ReturnInfo rtnInfo = new ReturnInfo();
		try {
		String tokenID=gcon.getTokenID();
		String url=serviceUrlConfig.getBdysysmUrl()+"/applicant/checkAndSaveApplicant?tokenID="+tokenID+"&customerId="+ customerId +GraspUtil.GetQueryByClass(applicant);
		String jsonString = GraspUtil.getText(url);		
		
		rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);
		
		
		} catch (Exception e) {
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
		}
		
		return rtnInfo;
	}
	
	
	/*
	
	@Override
	public ReturnInfo checkAndSaveApplicant(Applicant  applicant,Integer customerId) {
		ReturnInfo rtnInfo = new ReturnInfo();
		Applicant applicant1 = applicantMapper.selectByApplicantCNName(applicant);
		
			if(applicant1 !=null) {
				customerService.unBindApplicant(customerId, applicant1.getId());
				customerService.bindApplicant(customerId, applicant1.getId());
				applicant.setId(applicant1.getId());
				
				//从数据库中获取的申请人信息				
				String applicantEnName1=applicant1.getApplicantEnName();
				String applicantAddress1=applicant1.getApplicantAddress();
				String applicantEnAddress1=applicant1.getApplicantEnAddress();
				String appType1=applicant1.getAppType();
				String sendType1=applicant1.getSendType();
				String country1=applicant1.getCountry();
				
				//从案件中提取的申请人信息				
				String applicantEnName=applicant.getApplicantEnName();
				String applicantAddress=applicant.getApplicantAddress();
				String applicantEnAddress=applicant.getApplicantEnAddress();
				String appType=applicant.getAppType();
				String sendType=applicant.getSendType();
				String country=applicant.getCountry();
											
		
				//如果数据库中的申请人信息与案件信息中提取的申请人信息不同，那么，执行更新操作。				
				boolean needUpdate=false;				
					
				if  (applicantEnName!=null && !applicantEnName.equals("")){
					if (applicantEnName1==null || applicantEnName1.equals("") ){
						needUpdate=true;						
					}else{
						if  (!applicantEnName.equals(applicantEnName1)) {				
							needUpdate=true;
						}
					}
				}				
				if (!needUpdate){
					if  (applicantAddress!=null && !applicantAddress.equals("") ){						
						if (applicantAddress1==null || applicantAddress1.equals("") ){
							needUpdate=true;						
						}else{
							if  (!applicantAddress.equals(applicantAddress1)){
								needUpdate=true;
							}
						}
					}
				}
				if (!needUpdate){
					if  (applicantEnAddress!=null && !applicantEnAddress.equals("")){
						if (applicantEnAddress1==null || applicantEnAddress1.equals("") ){
							needUpdate=true;
						}else{
							if  (!applicantEnAddress.equals(applicantEnAddress1)){
								needUpdate=true;
							}
						}
					}
				}
				if (!needUpdate){
					if  (appType!=null && !appType.equals("")){
						if (appType1==null || appType1.equals("") ){							
							needUpdate=true;							
						}else{
							if(!appType.equals(appType1)){
								needUpdate=true;
							}
						}
					}
				}
				if (!needUpdate){
					if  (sendType!=null && !sendType.equals("")){
						if (sendType1==null || sendType1.equals("") ){							
							needUpdate=true;							
						}else{
							if(!sendType.equals(sendType)){
								needUpdate=true;
							}
						}
					}					
				}
				if (!needUpdate){
					if  (country!=null && !country.equals("")){
						if (country1==null || country1.equals("") ){							
							needUpdate=true;							
						}else{
							if(!country.equals(country1)){
								needUpdate=true;
							}
						}
					}
				}
								
				
				if (needUpdate){
					applicantMapper.updateByPrimaryKeySelective(applicant);
				}
				
				rtnInfo.setData(applicant);
				rtnInfo.setSuccess(true);
				rtnInfo.setMessageType(4);
				rtnInfo.setMessage("保存申请人成功");
				return rtnInfo;
			}else {
				applicantMapper.insertSelective(applicant);
				customerService.bindApplicant(customerId, applicant.getId());
				rtnInfo.setData(applicant);
				rtnInfo.setSuccess(true);
				rtnInfo.setMessageType(4);
				rtnInfo.setMessage("保存申请人成功");
				return rtnInfo;
			}
	}
	
	*/
	
	
	
	
	@Override
	public ReturnInfo queryApplicant(Applicant applicant, GeneralCondition gcon, Integer customerId) {
		ReturnInfo info = new ReturnInfo();
		List<Applicant> applicants = new ArrayList<>();
		Long total = (long) 0;
		if(customerId != null) {
			 applicants = applicantMapper.selectByApplicantAndCustId(applicant, gcon,customerId);
			 total = applicantMapper.selectByApplicantCountAndCustId(applicant, gcon,customerId);
		}else {
			 applicants = applicantMapper.selectByApplicant(applicant, gcon);
			 total = applicantMapper.selectByApplicantCount(applicant, gcon);

		}
		
		info.setData(applicants);
		info.setTotal(total);
		info.setCurrPage(gcon.getPageNo());
		info.setSuccess(true);
		info.setMessage("查询申请人成功");
		return info ;
	}
	
	
	@Override
	public ReturnInfo queryApplicantDetail(Applicant applicant, GeneralCondition gcon) {
		ReturnInfo info = new ReturnInfo();
		List<Applicant> applicants = applicantMapper.selectByApplicantAndTm(applicant, gcon);
		Long total = applicantMapper.selectByApplicantAndTmCount(applicant, gcon);
		info.setData(applicants);
		info.setTotal(total);
		info.setCurrPage(gcon.getPageNo());
		info.setSuccess(true);
		info.setMessage("查询申请人成功");
		return info ;
		
	}
	
	
	@Override
	public ReturnInfo queryApplicantNameByCustId(GeneralCondition gcon, Integer customerId) {
		ReturnInfo info = new ReturnInfo();
		List<Applicant> applicants = applicantMapper.selectApplicantNameByCustId(customerId, gcon);
		Long total = (long)0;
		if(applicants!=null){
			total=(long)applicants.size();
		}
		info.setData(applicants);
		info.setTotal(total);
		info.setCurrPage(gcon.getPageNo());
		info.setSuccess(true);
		info.setMessage("查询申请人成功");
		return info ;
	}
		

	@Override
	public ReturnInfo queryApplicantByCustId(GeneralCondition gcon, Integer customerId) {
		ReturnInfo info = new ReturnInfo();
		List<Applicant> applicants = applicantMapper.selectByApplicantByCustId(customerId, gcon);
		Long total = applicantMapper.selectByApplicantByCustIdCount(customerId, gcon);
		info.setData(applicants);
		info.setTotal(total);
		info.setCurrPage(gcon.getPageNo());
		info.setSuccess(true);
		info.setMessage("查询申请人成功");
		return info ;
	}
	
	
	@Override
	public ReturnInfo queryAllApplicantByCustId(GeneralCondition gcon, Integer customerId) {
		ReturnInfo info = new ReturnInfo();
		List<Applicant> applicants = applicantMapper.selectAllApplicantByCustId(customerId);
		//Long total = applicantmapper.selectByApplicantByCustIdCount(customerId, gcon);
		info.setData(applicants);
		//info.setTotal(total);
		info.setCurrPage(gcon.getPageNo());
		info.setSuccess(true);
		info.setMessage("查询申请人成功");
		return info ;
	}
	
	
	
	@Override
	public ReturnInfo queryAllApplicantByAppId(GeneralCondition gcon, Integer AppId) {
		ReturnInfo info = new ReturnInfo();
		List<Applicant> applicants = applicantMapper.selectAllApplicantById(AppId);
		//Long total = applicantmapper.selectByApplicantByCustIdCount(customerId, gcon);
		info.setData(applicants);
		//info.setTotal(total);
		info.setCurrPage(gcon.getPageNo());
		info.setSuccess(true);
		info.setMessage("查询申请人成功");
		return info ;
	}
	
	
	
	
	@Override
	public ReturnInfo queryAndCreateAapplicant(GeneralCondition gcon, Applicant applicant, Integer custId) {
		ReturnInfo rtnInfo = new ReturnInfo();
		Applicant app = applicantMapper.selectByName(applicant);
		
	/*	if(app == null) {
			int i = applicantmapper.insertSelective(applicant);
			customerService.bindApplicant(custId, applicant.getId());
			rtnInfo.setData(applicant);
		}else {
			customerService.bindApplicant(custId, app.getId());
			rtnInfo.setData(app);
		}	*/
		rtnInfo.setData(app);
		rtnInfo.setCurrPage(gcon.getPageNo());
		rtnInfo.setSuccess(true);
		rtnInfo.setMessage("查询申请人成功");
		return rtnInfo ;
	}
	
	
	@Override
	public ReturnInfo queryApplicantbyappcnname(String applicantCnName, GeneralCondition gcon) {
		ReturnInfo rtnInfo = new ReturnInfo();
		Applicant app =new Applicant();
		
		app.setApplicantName(applicantCnName);
		Applicant applicant1 = applicantMapper.selectByApplicantCNName(app);
		rtnInfo.setData(applicant1);
		rtnInfo.setSuccess(true);
		rtnInfo.setMessage("查询申请人成功");
		return rtnInfo ;
	}
	
	
	@Override
	public ReturnInfo queryApplicantbyappname(String appCnName, String appEnName, GeneralCondition gcon) {
		ReturnInfo rtnInfo = new ReturnInfo();
		if(appCnName ==null && appEnName ==null) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("查询申请人失败");
			return rtnInfo ;
		}
		if(appCnName !=null) {
			Applicant app =new Applicant();
			
			app.setApplicantName(appCnName);
			Applicant applicant = applicantMapper.selectByApplicantCNName(app);
			rtnInfo.setData(applicant);
			rtnInfo.setSuccess(true);
			rtnInfo.setMessage("查询申请人成功");
			return rtnInfo ;
		}
		if(appEnName !=null) {
			Applicant app =new Applicant();
			
			app.setApplicantEnName(appEnName);
			List<Applicant> applicants = applicantMapper.selectByApplicant(app, gcon);
			Applicant applicant =applicants.get(0);
			rtnInfo.setData(applicant);
			rtnInfo.setSuccess(true);
			rtnInfo.setMessage("查询申请人成功");
			return rtnInfo ;
		}
		rtnInfo.setSuccess(false);
		rtnInfo.setMessage("查询申请人失败");
		return rtnInfo ;
		
	}
	
	
	
	@Override
	public Applicant queryApplicantByAppName(String appCnName, String tokenID) {
	
		Applicant applicant=new Applicant();		
		
		applicant.setApplicantName(appCnName);
		applicant=applicantMapper.selectByApplicantCNName(applicant);
		
		return applicant;
	}
	
	
	@Override
	public Applicant getApplicantByName(Applicant applicant, String tokenID){
		
		String appEnName=applicant.getApplicantEnName();		
		List<Applicant> applicantList=applicantMapper.getByApplicantName(applicant);
		
		Applicant app=null;
		
		if (applicantList!=null && applicantList.size()>0){
			for(Applicant applicant1:applicantList){				
				String applicantEnName=applicant1.getApplicantEnName();
				//精确匹配：申请人中文名字相同，并且英文名字也相同的
				if (applicantEnName!=null && appEnName!=null && applicantEnName.equals(appEnName)){
					app=applicant1;
					break;
				}					
				if (app==null){
					Integer id=applicant1.getId();
					Integer mainAppId=applicant1.getMainAppId();
					//主申请人
					if (id!=null && mainAppId!=null && id.intValue()==mainAppId.intValue()){
						app=applicant1;					
					}
				}
			}
			if (app==null){
				app=applicantList.get(0);
			}
		}
		
		return app;
	}
	
	
	@Override
	public List<Applicant> queryApplicantByRegNumbers(List<String>regNumbers){
	
		List<Applicant> applicantList=new ArrayList<Applicant>();	
		
		applicantList=applicantMapper.getApplicantList(regNumbers);
		
		return applicantList;
	}

}
