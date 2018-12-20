package com.yootii.bdy.ipservice.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.ipservice.model.AgencyService;
import com.yootii.bdy.ipservice.model.PlatformService;
import com.yootii.bdy.ipservice.service.AgencyServiceService;
import com.yootii.bdy.util.GraspUtil;
import com.yootii.bdy.util.JsonUtil;
import com.yootii.bdy.util.ServiceUrlConfig;

@Service
public class AgencyServiceServiceImpl implements AgencyServiceService{
	
	private static final Logger logger = Logger.getLogger(AgencyServiceServiceImpl.class);
	
	@Autowired	
	private ServiceUrlConfig serviceUrlConfig;
	
	public AgencyService getAgencyService(GeneralCondition gcon, String agencyId, String agencyServiceId){
		ReturnInfo rtnInfo = new ReturnInfo();
		
		String tokenID=gcon.getTokenID();
		
				
		try {
								
			String url=serviceUrlConfig.getBdysysmUrl()+"/agencyservice/getagencyservice?agencyId="+agencyId+"&agencyServiceId="+ agencyServiceId +"&tokenID="+ tokenID;
			logger.info(url);
			String jsonString = GraspUtil.getText(url);
			rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);
			if (rtnInfo!=null && rtnInfo.getSuccess()){
				Map<String, Object> map = (Map<String, Object>) rtnInfo.getData();	
				if(map.size()>0){
					AgencyService as=new AgencyService();
						
					as.setAgencyServiceId((Integer)map.get("agencyServiceId"));	
					as.setChargeItemId((Integer)map.get("chargeItemId"));
					as.setServiceId((Integer)map.get("serviceId"));
					return as;	
				}
				
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(-1);
		} 		
		
		return null;
	}
	
	
	
	
	public AgencyService queryAgencyService(GeneralCondition gcon, Integer caseTypeId, Integer agencyId,
			Integer serviceType){
		ReturnInfo rtnInfo = new ReturnInfo();		
		String tokenID=gcon.getTokenID();						
		try {
								
			String url=serviceUrlConfig.getBdysysmUrl()+"/agencyservice/queryagencyservice?agencyId="+ agencyId.toString()  +"&caseTypeId="+ caseTypeId.toString()  +"&serviceType="+ serviceType.toString() +"&tokenID="+ tokenID;
//			logger.info(url);
			String jsonString = GraspUtil.getText(url);
			rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);
			if (rtnInfo!=null && rtnInfo.getSuccess()){
				Map<String, Object> map = (Map<String, Object>) rtnInfo.getData();	
				
				AgencyService as=new AgencyService();		
				as.setAgencyServiceId((Integer)map.get("agencyServiceId"));	
				
				
				return as;			
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(-1);
		} 		
		
		return null;
	}
	
	
	public AgencyService queryAgencyServiceDetail(GeneralCondition gcon, String agencyServiceId){
		ReturnInfo rtnInfo = new ReturnInfo();		
		String tokenID=gcon.getTokenID();						
		try {
								
			String url=serviceUrlConfig.getBdysysmUrl()+"/agencyservice/queryagencyservicedetail?agencyServiceId="+ agencyServiceId +"&tokenID="+ tokenID;
			logger.info(url);
			String jsonString = GraspUtil.getText(url);
			rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);
			if (rtnInfo!=null && rtnInfo.getSuccess()){
				Map<String, Object> map = (Map<String, Object>) rtnInfo.getData();	
				
				AgencyService as=new AgencyService();		
				as.setAgencyServiceId((Integer)map.get("agencyServiceId"));	
				as.setChargeItemId((Integer)map.get("chargeItemId"));
				as.setServiceId((Integer)map.get("serviceId"));
				as.setAgencyId((Integer)map.get("agencyId"));
				PlatformService ps = new PlatformService();
				Map<String, Object> map1 = (Map<String, Object>)map.get("platformService");
				ps.setCaseType((String) map1.get("caseType"));
				ps.setCaseTypeId((Integer) map1.get("caseTypeId"));
				as.setPlatformService(ps);
				return as;			
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(-1);
		} 		
		
		return null;
	}
	
	
	
}