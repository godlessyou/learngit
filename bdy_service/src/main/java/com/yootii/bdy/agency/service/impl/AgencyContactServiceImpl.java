package com.yootii.bdy.agency.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yootii.bdy.agency.dao.AgencyMapper;
import com.yootii.bdy.agency.model.Agency;
import com.yootii.bdy.agency.service.AgencyContactService;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.task.model.ReturnToDoAmount;
import com.yootii.bdy.task.service.TradeMarkCaseTaskService;
import com.yootii.bdy.util.GraspUtil;
import com.yootii.bdy.util.JsonUtil;
import com.yootii.bdy.util.ServiceUrlConfig;
@Service
public class AgencyContactServiceImpl implements AgencyContactService{

	@Autowired	
	private ServiceUrlConfig serviceUrlConfig;
	@Autowired	
	private AgencyMapper agencyMapper;
	@Autowired	
	private TradeMarkCaseTaskService tradeMarkCaseTaskService;

	@Override
	public List<Map<String, Object>> queryAgencyContactById(String agencyId, String custId, String tokenID) {
		
		ReturnInfo rtnInfo = new ReturnInfo();
		List<Map<String, Object>> agencyContactList = new ArrayList<Map<String, Object>>();
		try {			
		
			String url=serviceUrlConfig.getBdysysmUrl()+"/agency/queryagencycontactbycust?agencyId="+ agencyId +"&custId="+custId+"&tokenID="+ tokenID;
//			
			String jsonString = GraspUtil.getText(url);
			rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);
			if (rtnInfo!=null && rtnInfo.getSuccess()){
				agencyContactList =(List<Map<String, Object>>)rtnInfo.getData();
			   
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(-1);
		} 		
		
		return agencyContactList;
	}

	
	
}
