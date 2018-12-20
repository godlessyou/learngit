package com.yootii.bdy.tmcase.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.tmcase.model.TradeMarkCaseColRecord;


public interface TradeMarkCaseColRecordMapper {
    int insert(TradeMarkCaseColRecord record);

    int insertSelective(TradeMarkCaseColRecord record);
    
    List<TradeMarkCaseColRecord> selecTmCaseColRecordList(@Param("tmcase")TradeMarkCaseColRecord tmcase);

}