package com.yootii.bdy.bill.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yootii.bdy.bill.model.ChargeItem;
import com.yootii.bdy.bill.service.ChargeItemService;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.util.GonUtil;
import com.yootii.bdy.util.GraspUtil;
import com.yootii.bdy.util.JsonUtil;
import com.yootii.bdy.util.ServiceUrlConfig;

@Service("ChargeItemService")
public class ChargeItemServiceImpl implements ChargeItemService{
	

	private static final Logger logger = Logger.getLogger(ChargeItemServiceImpl.class);
	
	@Autowired	
	private ServiceUrlConfig serviceUrlConfig;
	
	public List<ChargeItem> queryChargeItemListById(GeneralCondition gcon,
			String agencyServiceId) {
		ReturnInfo rtnInfo = new ReturnInfo();
		
		String tokenID=gcon.getTokenID();
		
		List<ChargeItem> list=new ArrayList<ChargeItem>();
		try {
					
			String url=serviceUrlConfig.getBdysysmUrl()+"/bill/querychargeitemlistbyid?agencyServiceId="+agencyServiceId+"&tokenID="+ tokenID;
//			logger.info(url);
			String jsonString = GraspUtil.getText(url);
			rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);
			if (rtnInfo!=null && rtnInfo.getSuccess()){
				List<Map<String, Object>> chargeItemList = (List<Map<String, Object>>) rtnInfo
						.getData();
				
				for (Map<String, Object> map : chargeItemList) {

					ChargeItem item=new ChargeItem();					
					item.setChargeItemId((Integer)map.get("chargeItemId"));
					Object obj=map.get("price");
					if (obj!=null){						
						Double aaa =(Double)obj;
						BigDecimal bbb=new BigDecimal(aaa);						
						item.setPrice(bbb);
					}
					obj=map.get("caseType");
					if (obj!=null){
						item.setCaseType(obj.toString());
					}
					obj=map.get("chargeType");
					if (obj!=null){
						item.setChargeType(obj.toString());
					}
					obj=map.get("chnName");
					if (obj!=null){
						item.setChnName(obj.toString());
					}
					obj=map.get("engName");
					if (obj!=null){
						item.setEngName(obj.toString());
					}
					
					list.add(item);
				}			
			
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(-1);
		} 		
		
		return list;
	}
	
	
	
	public ChargeItem queryChargeItemByAgencyId(GeneralCondition gcon, String agencyId, String chnName){
		ReturnInfo rtnInfo = new ReturnInfo();
		
		String tokenID=gcon.getTokenID();
		
		GonUtil.makeOffsetAndRows(gcon);
		
		
		try {
								
			String url=serviceUrlConfig.getBdysysmUrl()+"/bill/querychargeitemlist?agencyId="+agencyId+"&chnName="+ chnName+"&tokenID="+ tokenID;
//			logger.info(url);
			String jsonString = GraspUtil.getText(url);
			rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);
			if (rtnInfo!=null && rtnInfo.getSuccess()){
				List<Map<String, Object>> chargeItemList = (List<Map<String, Object>>) rtnInfo
						.getData();
				
				for (Map<String, Object> map : chargeItemList) {

					ChargeItem item=new ChargeItem();					
					item.setChargeItemId((Integer)map.get("chargeItemId"));					
					return item;	
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
	


}
