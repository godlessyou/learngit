package com.yootii.bdy.tmcase.dao;

import java.util.List;

import com.yootii.bdy.tmcase.model.TradeMarkCaseType;

public interface TradeMarkCaseTypeMapper {
    int deleteByPrimaryKey(Integer caseTypeId);

    int insert(TradeMarkCaseType record);

    int insertSelective(TradeMarkCaseType record);

    TradeMarkCaseType selectByPrimaryKey(Integer caseTypeId);

    int updateByPrimaryKeySelective(TradeMarkCaseType record);

    int updateByPrimaryKey(TradeMarkCaseType record);
    
    TradeMarkCaseType selectByCaseType(String caseType);
    
    List<TradeMarkCaseType> selectCaseTypeList();
    
    List<TradeMarkCaseType> selectAvalibleCaseType();
    
}