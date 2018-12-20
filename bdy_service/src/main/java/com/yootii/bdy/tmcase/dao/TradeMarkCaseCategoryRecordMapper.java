package com.yootii.bdy.tmcase.dao;

import com.yootii.bdy.tmcase.model.TradeMarkCaseCategoryRecord;

public interface TradeMarkCaseCategoryRecordMapper {
    int deleteByPrimaryKey(Integer recordId);

    int insert(TradeMarkCaseCategoryRecord record);

    int insertSelective(TradeMarkCaseCategoryRecord record);

    TradeMarkCaseCategoryRecord selectByPrimaryKey(Integer recordId);

    int updateByPrimaryKeySelective(TradeMarkCaseCategoryRecord record);

    int updateByPrimaryKey(TradeMarkCaseCategoryRecord record);
}