package com.yootii.bdy.datasyn.service.impl;



import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.datasyn.service.DataSynService;
import com.yootii.bdy.util.GraspUtil;
import com.yootii.bdy.util.JsonUtil;
import com.yootii.bdy.util.ServiceUrlConfig;


@Service
public class DataSynServiceImpl implements DataSynService {

	private final Logger logger = Logger.getLogger(this.getClass());

	
	@Resource
	private ServiceUrlConfig serviceUrlConfig;
	
	
	
	//同步案件数据
	public ReturnInfo caseDataSyn(GeneralCondition gcon, String caseId, Integer type){

		ReturnInfo rtnInfo = new ReturnInfo();
		try {
		String tokenID=gcon.getTokenID();
		String url=serviceUrlConfig.getBdydatasynUrl()+"/data/casedatasyn?tokenID="+tokenID+"&caseId="+ caseId + "&type="+ type.toString();
		String jsonString = GraspUtil.getText(url);		
		
		rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);
		
		
		} catch (Exception e) {
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
		}
		
		return rtnInfo;

	}
	
	
	//同步账单数据
	public ReturnInfo billDataSyn(GeneralCondition gcon, String caseIds, Integer billId){

		ReturnInfo rtnInfo = new ReturnInfo();
		try {
		String tokenID=gcon.getTokenID();
		String url=serviceUrlConfig.getBdydatasynUrl()+"/data/billdatasyn?tokenID="+tokenID+"&caseId="+ caseIds + "&billId="+ billId.toString();
		String jsonString = GraspUtil.getText(url);		
		
		rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);
		
		
		} catch (Exception e) {
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
		}
		
		return rtnInfo;

	}
	

}
