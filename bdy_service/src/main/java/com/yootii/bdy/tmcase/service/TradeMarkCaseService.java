package com.yootii.bdy.tmcase.service;

import java.util.List;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.customer.model.Applicant;
import com.yootii.bdy.remind.model.Remind;
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.tmcase.model.GoodsPlan;
import com.yootii.bdy.tmcase.model.IssuanceNumber;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.TradeMarkCaseSolr;
import com.yootii.bdy.trademark.model.Trademark;

public interface TradeMarkCaseService {
	
	public ReturnInfo createTradeMarkCase(TradeMarkCase tradeMarkCase, GeneralCondition gcon);
		
	public ReturnInfo modifyTradeMarkCase(TradeMarkCase tradeMarkCase, GeneralCondition gcon);
	
	public ReturnInfo queryTradeMarkCaseList(TradeMarkCase tradeMarkCase,GeneralCondition gcon,Token token,int isFinished);
	
	public ReturnInfo queryTradeMarkCaseDetail(Integer id);
	
	public ReturnInfo queryTradeMarkCaseForWs(Integer id);
	
	public ReturnInfo tradeMarkCaseAssociate(Integer caseId,Integer agencyId,Integer couserId,GeneralCondition gcon);

	public ReturnInfo updateSolrTradeMarkCase();

	public ReturnInfo statsTmagencyNameList(Integer custId, Integer startYear, Integer endYear);

	public ReturnInfo statsTmstatusList(Integer custId, Integer agencyId,Integer departId, Integer startYear, Integer endYear);

	public ReturnInfo statsTmcaseTypeList(Integer custId, Integer agencyId,Integer departId, Integer startYear, Integer endYear);

	public ReturnInfo statsTmappCnNameList(Integer custId, Integer agencyId,Integer departId, Integer startYear, Integer endYear);

	public ReturnInfo statsTmDateList(Integer custId, Integer agencyId,Integer departId, Integer startYear, Integer endYear);

	public ReturnInfo statsTmCustTop5List(Integer agencyId,Integer departId, Integer startYear, Integer endYear);

	public ReturnInfo statsTmcaseProInfo(Integer custId, Integer agencyId, Integer departId);
	
	public ReturnInfo queryAppOnlineCaseList(TradeMarkCase tradeMarkCase,GeneralCondition gcon,Token token);

	public ReturnInfo queryTradeMarkCaseBySolr(Integer departId, Integer custId, Integer agencyId, TradeMarkCaseSolr tradeMarkCase,
			String startYear, String endYear, GeneralCondition gcon);

		
	public ReturnInfo createAppCase(TradeMarkCase tradeMarkCase,String tmNumber,GeneralCondition gcon);
	
	public ReturnInfo createDissentReplyEntrance(TradeMarkCase tradeMarkCase,String tmNumber,GeneralCondition gcon);
	
	
	
	public ReturnInfo createTradeMarkCaseByTmNumber(TradeMarkCase tradeMarkCase, String tmNumber,GeneralCondition gcon);

	public ReturnInfo createTradeMarkCaseByTmNumberList(TradeMarkCase tradeMarkCase, List<String> tmNumberlist,GeneralCondition gcon);

	public ReturnInfo createTradeMarkCaseByAppName(TradeMarkCase tradeMarkCase,GeneralCondition gcon);

	
	
	public ReturnInfo queryTradeMarkCaseByProcessId(Integer processId);
	
	
	public ReturnInfo checkTradeMarkCase(TradeMarkCase tradeMarkCase);
	
	public ReturnInfo queryTmcaseDeadline(GeneralCondition gcon,Integer custId,Integer userId,String message,Integer urgencyType,Remind remind)throws Exception;
	
	public ReturnInfo statisticTmCaseDeadline(GeneralCondition gcon,Integer userId,Integer custId)throws Exception;

	public void updateTmCaseDeadLine(String idString)throws Exception;
	
	
	//创建当前案件的子案件
	public ReturnInfo createChildCase(Integer caseId, String caseType, Integer caseTypeId);
	
	//获取所有案件类型
	public ReturnInfo queryCaseTypeList(Integer allType);
	
	public IssuanceNumber getIssuanceNumber(String caseType,String fileName);
	
	
    public ReturnInfo addGoodsPlan(GoodsPlan goodsPlan);
	
	public ReturnInfo modifyGoodsPlan(GoodsPlan goodsPlan);
	
	public ReturnInfo deleteGoodsPlan(GoodsPlan goodsPlan);
	
	public ReturnInfo queryGoodsPlan(GoodsPlan goodsPlan);
	
	public ReturnInfo queryGoods(GoodsPlan goodsPlan);
	

	
	public ReturnInfo createDissentTmCase(TradeMarkCase tradeMarkCase,String tmNumber,Applicant applicant,GeneralCondition gCondition);
	public ReturnInfo modifyDissentTmCase(TradeMarkCase tradeMarkCase,GeneralCondition gCondition);
	
	public ReturnInfo modifyApplicantDissent(TradeMarkCase tradeMarkCase,GeneralCondition gCondition);

	public String getDocNumber(String appNumber, String caseType);
	
	public ReturnInfo queryAboutByRegnumber(GeneralCondition gCondition,String regNumber,String goodClass);
	

//	public ReturnInfo kindOfTmCase(TradeMarkCase tradeMarkCase,String tmNumber,Integer serviceId,String list,GeneralCondition generalCondition);
//
//	public ReturnInfo kindOfCaseProcessStart(TradeMarkCase tradeMarkCase,Integer serviceId,
//			List<Integer> caseList,Integer agencyId,String userId,
//			GeneralCondition gCondition,String agencyServiceId);
	
	
	public ReturnInfo createRemind(Integer serviceId,Integer caseId);
}


