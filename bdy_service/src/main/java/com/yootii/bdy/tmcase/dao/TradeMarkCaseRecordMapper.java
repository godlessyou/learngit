package com.yootii.bdy.tmcase.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.TradeMarkCaseRecord;

public interface TradeMarkCaseRecordMapper {
    int deleteByPrimaryKey(Integer recordId);

    int insert(TradeMarkCaseRecord record);

    int insertSelective(TradeMarkCaseRecord record);

    TradeMarkCaseRecord selectByPrimaryKey(Integer recordId);
    
    List<TradeMarkCaseRecord> selecTmCaseRecordList(@Param("tmcase")TradeMarkCaseRecord tmcase);

    int updateByPrimaryKeySelective(TradeMarkCaseRecord record);

    int updateByPrimaryKey(TradeMarkCaseRecord record);
}