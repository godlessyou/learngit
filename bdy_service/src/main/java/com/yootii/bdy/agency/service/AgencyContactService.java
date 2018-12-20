package com.yootii.bdy.agency.service;

import java.util.List;



import java.util.Map;



public interface AgencyContactService {
	
	List<Map<String, Object>> queryAgencyContactById(String agencyId, String custId, String tokenID);

	
}
