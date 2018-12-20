package com.yootii.bdy.tmcase.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.tmcase.model.TradeMarkCaseJoinApp;

public interface TradeMarkCaseJoinAppMapper {
	
	int deleteByCaseId(Integer caseId);
	
    int deleteByPrimaryKey(Integer id);

    int insert(TradeMarkCaseJoinApp record);

    int insertSelective(TradeMarkCaseJoinApp record);

    TradeMarkCaseJoinApp selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TradeMarkCaseJoinApp record);

    int updateByPrimaryKey(TradeMarkCaseJoinApp record);
    
    int copyTmCaseJoinAppRecord(@Param("assoCaseId")Integer assoCaseId,@Param("caseId")Integer caseId,@Param("agencyId")Integer agencyId);
    
    List<TradeMarkCaseJoinApp> selectByCustIdAndAgencyId(@Param("custId")Integer custId,@Param("agencyId")Integer agencyId);
    
    int deleteByCustIdAndAgencyId(@Param("custId")Integer custId,@Param("agencyId")Integer agencyId, @Param("casePreId")Integer casePreId);
    
    List<TradeMarkCaseJoinApp> selectByCasePreId(Integer casePreId);
    
    List<TradeMarkCaseJoinApp> selectByTradeMarkCaseJoinApp(TradeMarkCaseJoinApp joinApp);
}