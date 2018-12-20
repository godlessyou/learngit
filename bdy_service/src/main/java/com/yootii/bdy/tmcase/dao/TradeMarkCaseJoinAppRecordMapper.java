package com.yootii.bdy.tmcase.dao;

import com.yootii.bdy.tmcase.model.TradeMarkCaseJoinAppRecord;

public interface TradeMarkCaseJoinAppRecordMapper {
    int deleteByPrimaryKey(Integer recordId);

    int insert(TradeMarkCaseJoinAppRecord record);

    int insertSelective(TradeMarkCaseJoinAppRecord record);

    TradeMarkCaseJoinAppRecord selectByPrimaryKey(Integer recordId);

    int updateByPrimaryKeySelective(TradeMarkCaseJoinAppRecord record);

    int updateByPrimaryKey(TradeMarkCaseJoinAppRecord record);
}