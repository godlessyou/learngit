package com.yootii.bdy.tmcase.service;

import javax.servlet.http.HttpServletRequest;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.tmcase.model.TradeMarkCaseFile;

public interface TradeMarkCaseFileService {
	
	public ReturnInfo uploadCaseFile(HttpServletRequest request,Integer caseIdList, TradeMarkCaseFile tmcaseFile, GeneralCondition gcon, Token token, Integer applicantId);
	public ReturnInfo insertCaseFileData(Integer caseId, TradeMarkCaseFile tmcaseFile);
	public ReturnInfo queryCaseFile(TradeMarkCaseFile tmcaseFile,String status);
}
