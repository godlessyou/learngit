package com.yootii.bdy.agency.service;

import java.util.List;

import com.yootii.bdy.agency.model.Agency;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.task.model.ReturnToDoAmount;

public interface AgencyService {
	
	ReturnToDoAmount queryCustIdandUserIdByagency( Integer agencyId, String tokenID);

	List<Integer> queryAgencyList();

	Agency queryAgencyById(Integer agencyId);

	ReturnInfo basicinfobyplateform(GeneralCondition gcon);
	
	Agency queryAgencyByName(String agencyName);
	
	Integer getWhdAgencyId();
	
	Integer getAgentIdByUserId(Integer userId);
}
