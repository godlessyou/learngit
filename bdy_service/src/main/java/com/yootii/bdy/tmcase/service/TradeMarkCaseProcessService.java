package com.yootii.bdy.tmcase.service;

import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.tmcase.model.TradeMarkCaseProcess;

public interface TradeMarkCaseProcessService {
	public ReturnInfo createTradeMarkCaseProcess(TradeMarkCaseProcess tradeMarkCaseProcess);
	public ReturnInfo modifyTradeMarkCaseProcess(TradeMarkCaseProcess tradeMarkCaseProcess);
	public ReturnInfo queryTradeMarkCaseProcess(TradeMarkCaseProcess tradeMarkCaseProcess);
}
