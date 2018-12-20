package com.yootii.bdy.trademark.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.trademark.model.Trademark;
import com.yootii.bdy.trademark.model.TrademarkProcess;

public interface TrademarkSolrMapper {


    
    List<Map<String,Object>> selectAllTrademark(Integer start);
    
    List<Map<String,Object>> selectAllTrademarkProcess(Integer start);
    
    List<Map<String,Object>> selectAllTrademarkEntGov(Integer start);
    
    List<Map<String,Object>> selectAllTrademarkProc(Integer start);
    

	
}