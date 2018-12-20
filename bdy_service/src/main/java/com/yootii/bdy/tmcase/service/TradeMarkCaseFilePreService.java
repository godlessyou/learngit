package com.yootii.bdy.tmcase.service;

import javax.servlet.http.HttpServletRequest;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.tmcase.model.TradeMarkCaseFilePre;
import com.yootii.bdy.tmcase.model.TradeMarkCasePre;

public interface TradeMarkCaseFilePreService {
	
	public ReturnInfo uploadCaseFile(HttpServletRequest request,TradeMarkCasePre tradeMarkCasePre, TradeMarkCaseFilePre tmcaseFilePre, GeneralCondition gcon, Token token, Integer applicantId);
}
