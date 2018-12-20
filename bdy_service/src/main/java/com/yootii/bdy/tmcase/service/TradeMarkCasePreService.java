package com.yootii.bdy.tmcase.service;

import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.tmcase.model.TradeMarkCasePre;

public interface TradeMarkCasePreService {
	
	public ReturnInfo createTradeMarkCasePre(TradeMarkCasePre tradeMarkCasePre);
	
	public ReturnInfo modifyTradeMarkCasePre(TradeMarkCasePre tradeMarkCasePre);
	
//	public ReturnInfo queryTradeMarkCasePreList(TradeMarkCasePre tradeMarkCasePre,GeneralCondition gcon);
	
	public ReturnInfo queryTradeMarkCasePreDetail(Integer id);
	
	public ReturnInfo queryDetailByCustIdAndAgencyId(Integer custId,Integer agencyId);
}
