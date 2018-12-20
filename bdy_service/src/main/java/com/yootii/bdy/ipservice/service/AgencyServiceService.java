package com.yootii.bdy.ipservice.service;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.ipservice.model.AgencyService;



public interface AgencyServiceService {
	
	public AgencyService getAgencyService(GeneralCondition gcon, String agencyId, String agencyServiceId);
	
	public AgencyService queryAgencyServiceDetail(GeneralCondition gcon, String agencyServiceId);
	
	public AgencyService queryAgencyService(GeneralCondition gcon, Integer caseTypeId, Integer agencyId,
			Integer serviceType);
  
   
}