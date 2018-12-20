package com.yootii.bdy.trademark.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.trademark.model.Trademark;
import com.yootii.bdy.trademark.model.TrademarkProcess;

public interface TrademarkProcessMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TrademarkProcess record);

    int insertSelective(TrademarkProcess record);

    TrademarkProcess selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TrademarkProcess record);

    int updateByPrimaryKey(TrademarkProcess record);

	List<TrademarkProcess> selectByTmId(Integer tmId);

	List<TrademarkProcess> selectByTrademarkProcess(@Param("trademarkProcess")TrademarkProcess trademarkProcessp, @Param("gcon")GeneralCondition gcon);

	
	List<TrademarkProcess> selectByRegNumber(String regNumber);
	
	void delectByRegNumber(String regNumber);

	void deleteByRegNumberList(Map<String, Object>map);

	List<TrademarkProcess> selectByRegNumberList(Map<String, Object>map);
	
	List<Map<String, Object>> selectAllTmProcess(@Param("i")Integer i);	
}