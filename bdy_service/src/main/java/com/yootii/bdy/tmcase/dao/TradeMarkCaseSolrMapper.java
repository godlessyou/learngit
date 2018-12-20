package com.yootii.bdy.tmcase.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.trademark.model.Trademark;
import com.yootii.bdy.trademark.model.TrademarkProcess;

public interface TradeMarkCaseSolrMapper {


    
    List<Map<String,Object>> selectAllTradeMarkCase(Integer start);    
    
    List<Map<String,Object>> selectAllTradeMarkCaseUser(Integer start);    
    
    List<Map<String,Object>> selectAllTradeMarkCaseDepart(Integer start);
    

	
}