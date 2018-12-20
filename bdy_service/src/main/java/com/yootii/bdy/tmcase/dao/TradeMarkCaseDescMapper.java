package com.yootii.bdy.tmcase.dao;

import java.util.List;

import com.yootii.bdy.tmcase.model.TradeMarkCaseDesc;

public interface TradeMarkCaseDescMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TradeMarkCaseDesc record);

    int insertSelective(TradeMarkCaseDesc record);

    TradeMarkCaseDesc selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TradeMarkCaseDesc record);

    int updateByPrimaryKey(TradeMarkCaseDesc record);
    
    List<TradeMarkCaseDesc> selectTradeMarkCaseDescList();
}