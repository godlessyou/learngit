package com.yootii.bdy.tmcase.service.impl;


import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.customer.model.Applicant;
import com.yootii.bdy.remind.model.Remind;
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.tmcase.dao.IssuanceNumberMapper;
import com.yootii.bdy.tmcase.model.GoodsPlan;
import com.yootii.bdy.tmcase.model.IssuanceNumber;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.TradeMarkCaseSolr;
import com.yootii.bdy.tmcase.service.TradeMarkCaseService;


@Service("tradeMarkCaseService")
public class TradeMarkCaseServiceImpl implements TradeMarkCaseService{
	
	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private TradeMarkCaseCommonImpl tradeMarkCaseCommonImpl;
	
	@Resource
	private TradeMarkCaseManageImpl tradeMarkCaseManageImpl;
	
	@Resource
	private	TradeMarkCaseQueryImpl tradeMarkCaseQueryImpl;	
	
	@Resource
	private	TradeMarkCaseGoodsImpl tradeMarkCaseGoodsImpl;
	
	@Resource
	private	TradeMarkCaseRemindImpl tradeMarkCaseRemindImpl;
	
	@Resource
	private	TradeMarkCasePropertyImpl tradeMarkCasePropertyImpl;
	
	@Resource
	private IssuanceNumberMapper issuanceNumberMapper;	

	
	@Override
	public ReturnInfo createTradeMarkCase(TradeMarkCase tradeMarkCase, GeneralCondition gcon) {
		ReturnInfo info = tradeMarkCaseManageImpl.createTradeMarkCase(tradeMarkCase, gcon);
		return info;
	}
	
	
	
	/**
	 * 创建异议申请入口
	 */
	@Override
	public ReturnInfo createAppCase(
			TradeMarkCase tradeMarkCase, String tmNumber,
			GeneralCondition gcon) {		
		ReturnInfo info = tradeMarkCaseManageImpl.createAppCase(tradeMarkCase, tmNumber, gcon);
		return info;
	}

	/**
	 * 创建答辩入口
	 */
	@Override
	public ReturnInfo createDissentReplyEntrance(TradeMarkCase tradeMarkCase,
			String tmNumber, GeneralCondition gcon) {		
		ReturnInfo info = tradeMarkCaseManageImpl.createDissentReplyEntrance(tradeMarkCase, tmNumber, gcon);
		return info;
	}
	
	
	
	
	
	
	@Override
	public ReturnInfo createTradeMarkCaseByTmNumber(TradeMarkCase tradeMarkCase,String tmNumber,GeneralCondition gcon) {		
		ReturnInfo info =tradeMarkCaseManageImpl.createTradeMarkCaseByTmNumber(tradeMarkCase, tmNumber, gcon);
		return info;
	}
	
	
	
	@Override
	public ReturnInfo createTradeMarkCaseByAppName(TradeMarkCase tradeMarkCase, GeneralCondition gcon) {
		ReturnInfo info = tradeMarkCaseManageImpl.createTradeMarkCaseByAppName(tradeMarkCase, gcon);
		return info;
	}
	
	@Override
	public ReturnInfo createTradeMarkCaseByTmNumberList(TradeMarkCase tradeMarkCase,List<String> tmNumberlist,GeneralCondition gcon) {
		ReturnInfo info = tradeMarkCaseManageImpl.createTradeMarkCaseByTmNumberList(tradeMarkCase, tmNumberlist, gcon);
		return info;
	}
		

	
	
	@Override
	public ReturnInfo modifyTradeMarkCase(TradeMarkCase tradeMarkCase, GeneralCondition gcon) {
		ReturnInfo info = tradeMarkCaseManageImpl.modifyTradeMarkCase(tradeMarkCase, gcon);		
		return info;
	}
	
	
	
	public ReturnInfo createChildCase(Integer caseId, String caseType, Integer caseTypeId) {
		ReturnInfo info = new ReturnInfo();
		tradeMarkCaseManageImpl.createChildCase(caseId, caseType, caseTypeId);		
		return info;
	}
	
	
	

	/**
	 * 创建商标异议答辩 类型的案件
	 * 如果该商标的异议申请在本平台办理，则直接可从数据库中查到相关异议人的信息，否则需要从官方发来的
	 * 文档中获取 在页面填写 保存 
	 */
	@Override
	public ReturnInfo createDissentTmCase(TradeMarkCase tradeMarkCase,String tmNumber,Applicant applicant,
			GeneralCondition gCondition){
		ReturnInfo returnInfo = tradeMarkCaseManageImpl.createDissentTmCase(tradeMarkCase, tmNumber, applicant, gCondition);
		return returnInfo;
	}


	
	//修改 异议答辩案件
	@Override
	public ReturnInfo modifyDissentTmCase(TradeMarkCase tradeMarkCase,
			GeneralCondition gCondition) {
		ReturnInfo returnInfo = tradeMarkCaseManageImpl.modifyDissentTmCase(tradeMarkCase, gCondition);
		return returnInfo;
	}

	


	//修改  异议申请类型的案件
	@Override
	public ReturnInfo modifyApplicantDissent(TradeMarkCase tradeMarkCase,
			GeneralCondition gCondition) {
		ReturnInfo returnInfo = tradeMarkCaseManageImpl.modifyApplicantDissent(tradeMarkCase, gCondition);
		return returnInfo;
	}

			
	
	
	@Override
	public ReturnInfo tradeMarkCaseAssociate(Integer caseId,
			Integer agencyId,Integer couserId,GeneralCondition gcon) {
		ReturnInfo info = tradeMarkCaseManageImpl.tradeMarkCaseAssociate(caseId, agencyId, couserId, gcon);
		return info;
	}
	
	
	

	@Override
	public ReturnInfo queryTradeMarkCaseList(TradeMarkCase tradeMarkCase,
			GeneralCondition gcon,Token token,int isFinished) {
		ReturnInfo info = tradeMarkCaseQueryImpl.queryTradeMarkCaseList(tradeMarkCase, gcon, token, isFinished);
		
		return info;
	}

	
	@Override
	public ReturnInfo queryTradeMarkCaseDetail(Integer id) {
		ReturnInfo info = tradeMarkCaseQueryImpl.queryTradeMarkCaseDetail(id);
		return info;
	}
	
	
	@Override
	public ReturnInfo queryTradeMarkCaseByProcessId(Integer processId) {
		ReturnInfo info = tradeMarkCaseQueryImpl.queryTradeMarkCaseByProcessId(processId);
		return info;
	}
	
	
	
	@Override
	public ReturnInfo queryAppOnlineCaseList(TradeMarkCase tradeMarkCase,
			GeneralCondition gcon,Token token) {
		ReturnInfo info = tradeMarkCaseQueryImpl.queryAppOnlineCaseList(tradeMarkCase, gcon, token);
		return info;
	}
	
	
	
	/**
	 * 查询网申所需的数据
	 */
	@Override
	public ReturnInfo queryTradeMarkCaseForWs(Integer id) {
		ReturnInfo info =tradeMarkCaseQueryImpl.queryTradeMarkCaseForWs(id);
		return info;
	}
			
	

	
	@Override
	public ReturnInfo queryTradeMarkCaseBySolr(Integer  departId,Integer custId,Integer agencyId,
			TradeMarkCaseSolr tradeMarkCase ,String startYear, String endYear,GeneralCondition gcon){
		ReturnInfo returnInfo = tradeMarkCaseQueryImpl.queryTradeMarkCaseBySolr(departId, custId, agencyId, tradeMarkCase, startYear, endYear, gcon);
	    return returnInfo;
	
	}
	/**
	 * 系统首页工作台 案件统计
	 */
	@Override
	public ReturnInfo statsTmcaseProInfo(Integer custId ,Integer agencyId,Integer departId){
		ReturnInfo returnInfo = tradeMarkCaseQueryImpl.statsTmcaseProInfo(custId, agencyId, departId);
		return returnInfo;
		
	}

	@Override
	public ReturnInfo statsTmagencyNameList(Integer custId, Integer startYear, Integer endYear){
		ReturnInfo returnInfo = tradeMarkCaseQueryImpl.statsTmagencyNameList(custId, startYear, endYear);
		return returnInfo;
		
	}
	
	@Override
	public ReturnInfo statsTmstatusList(Integer custId,Integer agencyId,Integer departId, Integer startYear, Integer endYear){
		ReturnInfo returnInfo =tradeMarkCaseQueryImpl.statsTmstatusList(custId, agencyId, departId, startYear, endYear);
		return returnInfo;
		
	}
	
	@Override
	public ReturnInfo statsTmcaseTypeList(Integer custId,Integer agencyId,Integer departId, Integer startYear, Integer endYear){
		ReturnInfo returnInfo = tradeMarkCaseQueryImpl.statsTmcaseTypeList(custId, agencyId, departId, startYear, endYear);
		return returnInfo;
		
	}
	
	@Override
	public ReturnInfo statsTmappCnNameList(Integer custId,Integer agencyId,Integer departId, Integer startYear, Integer endYear){
		ReturnInfo returnInfo =tradeMarkCaseQueryImpl.statsTmappCnNameList(custId, agencyId, departId, startYear, endYear);
		return returnInfo;
		
	}	
	
	
	@Override
	public ReturnInfo statsTmDateList(Integer custId,Integer agencyId,Integer departId, Integer startYear, Integer endYear){
		ReturnInfo returnInfo = tradeMarkCaseQueryImpl.statsTmDateList(custId, agencyId, departId, startYear, endYear);
		return returnInfo;
		
	}
	
	
	@Override
	public ReturnInfo statsTmCustTop5List(Integer agencyId,Integer departId, Integer startYear, Integer endYear){
		ReturnInfo returnInfo = tradeMarkCaseQueryImpl.statsTmCustTop5List(agencyId, departId, startYear, endYear);
		return returnInfo;		
	}
	
	
	
	
	
	@Override
	public ReturnInfo queryCaseTypeList(Integer allType) {
		ReturnInfo info = tradeMarkCaseQueryImpl.queryCaseTypeList(allType);
		return info;
	}
	
	


	//
	@Override
	public ReturnInfo queryAboutByRegnumber(GeneralCondition gCondition,
			String regNumber, String goodClass) {
		ReturnInfo returnInfo = tradeMarkCaseQueryImpl.queryAboutByRegnumber(gCondition, regNumber, goodClass);
		return returnInfo;
	}
	
	
	
	
	/**
	 * 		优先权时限
		b)	补正时限
		c)	驳回时限
		d)	异议答辩时限
		e)	续展时限
	 * 统计商标案件的截止日期各个时期的数量
	 * 思路：根据创建时间 和案件的截止时间  计算出两个时间相差的天数，然后 把相差的天数分成 3等份，
	 * 再计算现在的时间是出于3个时间段的哪一个阶段，进而统计出数值。
	 * 3个阶段：security(安全期) warn(提醒期) danger(危险期)
	 */
	public ReturnInfo statisticTmCaseDeadline(GeneralCondition gcon,Integer userId,Integer custId)throws Exception {
		ReturnInfo returnInfo = tradeMarkCaseRemindImpl.statisticTmCaseDeadline(gcon, userId, custId);
		return returnInfo;
	}
	


	//根据条件查询 时限数据
	@Override
	public ReturnInfo queryTmcaseDeadline(GeneralCondition gcon, Integer custId,
			Integer userId, String message, Integer urgencyType,Remind remind) throws Exception {
		ReturnInfo returnInfo = tradeMarkCaseRemindImpl.queryTmcaseDeadline(gcon, custId, userId, message, urgencyType, remind);
			
		return returnInfo;
	}
	
	

	
	@Override
	public ReturnInfo createRemind(Integer serviceId,Integer caseId) {
		ReturnInfo returnInfo = tradeMarkCaseRemindImpl.createRemind(serviceId, caseId);
		return returnInfo;
	}
	
		


	//批量关闭 时限提醒
	@Override
	public void updateTmCaseDeadLine(String idString)throws Exception {
		tradeMarkCaseRemindImpl.updateTmCaseDeadLine(idString);
		
	}

	
		
	
	@Override
	public ReturnInfo queryGoodsPlan(GoodsPlan goodsPlan) {
		ReturnInfo info = tradeMarkCaseGoodsImpl.queryGoodsPlan(goodsPlan);
		return info;
	}
	
	
	@Override
	public ReturnInfo queryGoods(GoodsPlan goodsPlan) {
		ReturnInfo info = tradeMarkCaseGoodsImpl.queryGoods(goodsPlan);
		return info;
	}

	
	@Override
	public ReturnInfo addGoodsPlan(GoodsPlan goodsPlan) {
		ReturnInfo info = tradeMarkCaseGoodsImpl.addGoodsPlan(goodsPlan);
		return info;
	}
	
	
	@Override
	public ReturnInfo modifyGoodsPlan(GoodsPlan goodsPlan) {
		ReturnInfo info = tradeMarkCaseGoodsImpl.modifyGoodsPlan(goodsPlan);
		return info;
	}
	
	
	
	@Override
	public ReturnInfo deleteGoodsPlan(GoodsPlan goodsPlan) {
		ReturnInfo info = tradeMarkCaseGoodsImpl.deleteGoodsPlan(goodsPlan);
		return info;
	}

		
	
	public ReturnInfo checkTradeMarkCase(TradeMarkCase tradeMarkCase){
		ReturnInfo rtnInfo = tradeMarkCaseCommonImpl.checkTradeMarkCase(tradeMarkCase);			
		return rtnInfo;
	}
	
	

	public IssuanceNumber getIssuanceNumber(String caseType,String fileName){
		return tradeMarkCasePropertyImpl.getIssuanceNumber(caseType,fileName);
	}
	
	@Override
	public String getDocNumber(String appNumber, String caseType) {
		String docNumber=tradeMarkCasePropertyImpl.getDocNumber(appNumber, caseType);
		return docNumber;
	}
	
	



	@Override
	public ReturnInfo updateSolrTradeMarkCase() {
		ReturnInfo returnInfo = tradeMarkCaseManageImpl.updateSolrTradeMarkCase();
		return returnInfo;

	}

	
	
	
	
	
	
}
