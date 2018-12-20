package com.yootii.bdy.agency.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yootii.bdy.agency.dao.AgencyMapper;
import com.yootii.bdy.agency.dao.AgencyUserMapper;
import com.yootii.bdy.agency.model.Agency;
import com.yootii.bdy.agency.service.AgencyService;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.task.model.ReturnToDoAmount;
import com.yootii.bdy.task.service.TradeMarkCaseTaskService;
import com.yootii.bdy.util.GraspUtil;
import com.yootii.bdy.util.JsonUtil;
import com.yootii.bdy.util.ServiceUrlConfig;
@Service
public class AgencyServiceImpl implements AgencyService{

	@Autowired	
	private ServiceUrlConfig serviceUrlConfig;
	@Autowired	
	private AgencyMapper agencyMapper;
	@Autowired	
	private AgencyUserMapper agencyUserMapper;
	@Autowired	
	private TradeMarkCaseTaskService tradeMarkCaseTaskService;

	@Override
	public ReturnToDoAmount queryCustIdandUserIdByagency(Integer agencyId, String tokenID) {
		boolean result=false;
		ReturnInfo rtnInfo = new ReturnInfo();
		ReturnToDoAmount r = new ReturnToDoAmount();
		try {
			
		
			String url=serviceUrlConfig.getBdysysmUrl()+"/agency/querycustidanduseridbyagency?agencyId="+ agencyId +"&tokenID="+ tokenID;
//			System.out.println(url);
			String jsonString = GraspUtil.getText(url);
			rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);
			if (rtnInfo!=null && rtnInfo.getSuccess()){
				Map<String, Object> data=(Map<String, Object>)rtnInfo.getData();
				List<Integer> custIds=(List<Integer>) data.get("custId");
				List<Integer> userIds=(List<Integer>) data.get("userId");
				r.setCustId(custIds);
				r.setUserId(userIds);
				
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(-1);
		} 		
		
		return r;
	}

	@Override
	public List<Integer> queryAgencyList() {
		return agencyMapper.selectAgencyList();
		
	}

	@Override
	public Agency queryAgencyById(Integer agencyId) {
		return agencyMapper.selectByPrimaryKey(agencyId);
	}
	
	@Override
	public Agency queryAgencyByName(String agencyName) {
		return agencyMapper.selectAgencyByName(agencyName);
	}
	
	@Override
	public Integer getWhdAgencyId() {
		Integer agencyId=null;
		String name="万慧达";
		String agencyName="%"+name+"%";
		Agency agency= agencyMapper.selectAgencyByName(agencyName);
		if (agency!=null){
			agencyId=agency.getId();
		}
		return agencyId;
	}
	
	

	@Override
	public ReturnInfo basicinfobyplateform(GeneralCondition gcon) {
		ReturnInfo rtnInfo = new ReturnInfo();
		List<Integer> agencyIdList = queryAgencyList();
		Map<String,Object> maps  = new HashMap<String,Object>();
		List<Map<String,Object>> list = tradeMarkCaseTaskService.statsCountToDo(gcon);
		//todoAmount 待办任务总数
		Long todoAmount = 0l;
		if(list.size()>0) {
			for(Map<String,Object> map : list) {
				Long amountByAgency;
				try {
					
					if(map.get("todoAmount") != null) {
						amountByAgency = (Long) map.get("todoAmount");
						todoAmount +=amountByAgency;
					}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		}
		Map<String,Object> m= agencyMapper.selectbasictotal(gcon);
		m.put("todoAmount", todoAmount);
		rtnInfo.setData(m);
		rtnInfo.setSuccess(true);
		rtnInfo.setMessage("创建成功");
		return rtnInfo;
	}

	@Override
	public Integer getAgentIdByUserId(Integer userId) {
		return agencyUserMapper.selectAgencyIdByUserId(userId);
	}
}
