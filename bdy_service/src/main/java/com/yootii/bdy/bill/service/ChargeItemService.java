package com.yootii.bdy.bill.service;

import java.util.List;

import com.yootii.bdy.bill.model.ChargeItem;
import com.yootii.bdy.common.GeneralCondition;


public interface ChargeItemService {
	
	public List<ChargeItem> queryChargeItemListById(GeneralCondition gcon,
			String agencyServiceId) ;
	
	public ChargeItem queryChargeItemByAgencyId(GeneralCondition gcon, String agencyId, String chnName) ;
	
	
}
