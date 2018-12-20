package com.yootii.bdy.tmcase.service;

import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.tmcase.model.TradeMarkCaseJoinApp;

public interface TradeMarkCaseJoinAppService {
	public ReturnInfo createTradeMarkCaseJoinApp(TradeMarkCaseJoinApp joinApp); 
	public ReturnInfo modifyTradeMarkCaseJoinApp(TradeMarkCaseJoinApp joinApp);
	public ReturnInfo queryTradeMarkCaseJoinAppById(Integer id);
	public ReturnInfo deleteTradeMarkCaseJoinAppById(Integer id);
	public ReturnInfo queryTradeMarkCaseJoinAppList(TradeMarkCaseJoinApp joinApp);
}
