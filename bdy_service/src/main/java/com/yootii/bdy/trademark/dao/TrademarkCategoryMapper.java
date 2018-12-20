package com.yootii.bdy.trademark.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.trademark.model.TrademarkCategory;

public interface TrademarkCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TrademarkCategory record);

    int insertSelective(TrademarkCategory record);

    TrademarkCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TrademarkCategory record);

    int updateByPrimaryKey(TrademarkCategory record);

	List<TrademarkCategory> selectByTmId(Integer tmId);	
	
	List<TrademarkCategory> selectByTrademarkCategory(@Param("trademarkCategory")TrademarkCategory trademarkCategory,@Param("gcon") GeneralCondition gcon);

	void deleteByTmIdList(Map<String, Object>map);
	
	void deleteDuplicatedData();
	
	List<TrademarkCategory> selectByTmIdAndTmType(Map<String, Object>map);
	
	void deleteByTmIdAndTmType(Map<String, Object>map);
	
	void updateTmId(Map<String, Object>map);	
	
	List<TrademarkCategory> selectByRegNumberList(Map<String, Object>map);


}