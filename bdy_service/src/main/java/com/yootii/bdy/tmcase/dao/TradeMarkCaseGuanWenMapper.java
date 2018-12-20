package com.yootii.bdy.tmcase.dao;

import com.yootii.bdy.tmcase.model.TradeMarkCaseGuanWen;

public interface TradeMarkCaseGuanWenMapper {
    int deleteByPrimaryKey(Integer caseId);

    int insert(TradeMarkCaseGuanWen record);

    int insertSelective(TradeMarkCaseGuanWen record);

    TradeMarkCaseGuanWen selectByPrimaryKey(Integer caseId);

    int updateByPrimaryKeySelective(TradeMarkCaseGuanWen record);

    int updateByPrimaryKey(TradeMarkCaseGuanWen record);
}