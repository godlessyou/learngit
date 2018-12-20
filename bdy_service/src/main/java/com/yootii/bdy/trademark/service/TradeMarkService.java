package com.yootii.bdy.trademark.service;  


import java.util.Date;
import java.util.List;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.solr.SolrInfo;
import com.yootii.bdy.trademark.model.Trademark;

  
  
public interface TradeMarkService {  
	
	public ReturnInfo queryAddressList(Trademark trademark);
           
    public ReturnInfo queryTmList(Date appStartDate,
    		Date appEndDate,GeneralCondition gcon, Trademark trademark, Date regStartDate, Date regEndDate,
    		Integer CustID, Integer appId,Integer national, String addressList);
    
            
    public ReturnInfo queryCanProcessTm(Date appStartDate,
    		Date appEndDate,GeneralCondition gcon, Trademark trademark, Date regStartDate, Date regEndDate,
    		Integer CustID, Integer appId,Integer national, Integer caseTypeId, String addressList);
    
    
    public ReturnInfo queryTmDetail(Trademark trademark);
    
//    public ReturnInfo checkTm(String regNumber);
//    
//    public Object exportTm(HttpServletRequest request, HttpServletResponse response,Integer govId,Integer entId,Date appStartDate,
//    		Date appEndDate,GeneralCondition gcon, Trademark trademark, Date regStartDate, Date regEndDate, Date statusStartDate, Date statusEndDate
//    		,String excludeBranch,String entName,String column) ;
//    
//    public List<Trademark> getTrademarkList(Integer govId,Integer entId,Date appStartDate,
//    		Date appEndDate,GeneralCondition gcon, Trademark trademark, Date regStartDate, Date regEndDate, Date statusStartDate, Date statusEndDate
//    		,String excludeBranch,String entName) ;
    
//    public Long getTrademarkListCount(Integer govId,Integer entId,Date appStartDate,
//    		Date appEndDate,GeneralCondition gcon, Trademark trademark, Date regStartDate, Date regEndDate, Date statusStartDate, Date statusEndDate
//    		,String excludeBranch,String entName) ;
    
    public ReturnInfo updateGoods(String goodsPath, String gfPath);
    
    public ReturnInfo updateSolrDate();
    
    public ReturnInfo updateTmProcessSolrDate();
    
    public ReturnInfo updateTmNotificationSolrDate(SolrInfo solrInfo);

//	public ReturnInfo queryTmListByTmId(Integer tmId, GeneralCondition gcon);

    public ReturnInfo statsTmStautsList(Integer CustID,Integer appId,Integer startYear,Integer endYear,GeneralCondition gcon) ;

    public ReturnInfo statsTmAppdateRegDateList(Integer CustID,Integer appId,Integer startYear,Integer endYear,GeneralCondition gcon,Integer flag);

    public ReturnInfo statsTmTypeList(Integer CustID,Integer appId,Integer startYear,Integer endYear,GeneralCondition gcon,Integer flag);

    public ReturnInfo statsTmAgentList(Integer CustID,Integer appId,Integer startYear,Integer endYear,GeneralCondition gcon,Integer flag);

    public ReturnInfo statsTmAppnameList(Integer CustID,Integer startYear,Integer endYear,GeneralCondition gcon,Integer regFlag,int flag);

    public ReturnInfo statsTmNameList(Integer CustID,Integer appId,Integer startYear,Integer endYear,GeneralCondition gcon,Integer flag);

    public ReturnInfo statsTmDongtai(Integer CustID,Integer appId, Integer startYear, Integer endYear, GeneralCondition gcon);

    public ReturnInfo statstmxuzhan(Integer custId,Integer appId, Integer startYear, Integer endYear, GeneralCondition gcon,Integer flag);

	public ReturnInfo queryTmKuanzhanList(GeneralCondition gcon, Integer CustID,Integer appId, Integer national);

    public ReturnInfo queryTmXuzhanList(GeneralCondition gcon, Integer CustID,Integer appId, Integer national);

    public ReturnInfo queryTrademarkBySolr(Integer custId, Integer appId, Integer dateType, Trademark trademark,
			String startYear, String endYear, GeneralCondition gcon, Boolean ignore,int flag);

	public ReturnInfo queryTmChangeList(GeneralCondition gcon, Trademark trademark, Integer appId,Integer national, String addressList);
	
	
	public ReturnInfo queryAgentByAppName(String appName);

	public ReturnInfo queryTmlistSimple(GeneralCondition gcon, List<String> applicantNameList, Integer national,
			Trademark trademark, Trademark nottrademark);

	public Trademark selectTrademarkbyRenumber(String tmNumber,GeneralCondition gcon) throws Exception;

	public ReturnInfo queryTrademarkbyRenumber(Trademark trademark, GeneralCondition gcon);

	public ReturnInfo statsTmBeibohuiDongtai(Integer CustID, Integer appId, Integer startYear, Integer endYear,
			GeneralCondition gcon);

	public ReturnInfo statisByTmProcess(Integer CustID, Integer appId, Integer startYear, Integer endYear,
			GeneralCondition gcon,Integer status,int flag);
	
	
	public ReturnInfo statsTmBeichesanDongtai(Integer CustID, Integer appId, Integer startYear, Integer endYear,
			GeneralCondition gcon);

	public ReturnInfo statsTmBeiwuxiaoDongtai(Integer CustID, Integer appId, Integer startYear, Integer endYear,
			GeneralCondition gcon);

	public ReturnInfo statsTmBeiyiyiDongtai(Integer CustID, Integer appId, Integer startYear, Integer endYear,
			GeneralCondition gcon);

	public ReturnInfo statsTmBiangengDongtai(Integer CustID, Integer appId, Integer startYear, Integer endYear,
			GeneralCondition gcon);

	public ReturnInfo statsTmTongyongmingchengDongtai(Integer CustID, Integer appId, Integer startYear, Integer endYear,
			GeneralCondition gcon);

	
	public ReturnInfo statisticTmStatus(GeneralCondition gcon,Integer custId,Integer appId,Integer startYear,Integer endYear,Integer flag) throws Exception;
	
	
	public ReturnInfo statisticTmDate(GeneralCondition gcon,Integer custId,Integer appId,String status,Integer flag,Integer startYear,Integer endYear)throws Exception;
    
	
	public ReturnInfo statsTmAgentData(GeneralCondition gcon,Integer custId,Integer flag,Integer year,Integer appId) throws Exception;

	public ReturnInfo tmDynamicData(GeneralCondition gcon,Integer custId,Integer flag,Integer appId,Integer year,Integer abnormalType);
}  