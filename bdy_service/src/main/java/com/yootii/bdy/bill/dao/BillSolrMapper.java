package com.yootii.bdy.bill.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.trademark.model.Trademark;
import com.yootii.bdy.trademark.model.TrademarkProcess;

public interface BillSolrMapper {


    
    List<Map<String,Object>> selectAllBill(Integer start);
    
    List<Map<String,Object>> selectAllDepart(Integer start);
    

	
}