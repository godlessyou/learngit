package com.yootii.bdy.tmcase.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.TradeMarkCaseProcess;

public interface TradeMarkCaseProcessMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TradeMarkCaseProcess record);

    int insertSelective(TradeMarkCaseProcess record);

    TradeMarkCaseProcess selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TradeMarkCaseProcess record);

    int updateByPrimaryKey(TradeMarkCaseProcess record);
    
    List<TradeMarkCaseProcess> selectByTmCaseProcess(TradeMarkCaseProcess tmCaseProcess);
    
    TradeMarkCaseProcess selectByCaseId(Integer caseId); 
    
    Integer selectUserIdByCaseId(@Param("tmcase")TradeMarkCase tmcase); 
    
    
    
}