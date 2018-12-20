package com.yootii.bdy.tmcase.service;

import com.yootii.bdy.common.ReturnInfo;

import com.yootii.bdy.tmcase.model.TradeMarkCaseRecord;
public interface TradeMarkCaseRecordService {
	
	
	public ReturnInfo queryTradeMarkCaseRecordList(TradeMarkCaseRecord tradeMarkCaseRecord);
	
	public ReturnInfo queryTradeMarkCaseRecordDetail(Integer id) ;
	
	
}
