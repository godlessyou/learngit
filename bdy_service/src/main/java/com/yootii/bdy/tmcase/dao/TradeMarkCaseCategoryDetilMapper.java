package com.yootii.bdy.tmcase.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.tmcase.model.TradeMarkCaseCategoryDetil;

public interface TradeMarkCaseCategoryDetilMapper {
    void deleteData();
    
    int deleteByPrimaryKey(Integer id);

    int insert(TradeMarkCaseCategoryDetil record);

    int insertSelective(TradeMarkCaseCategoryDetil record);

    TradeMarkCaseCategoryDetil selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TradeMarkCaseCategoryDetil record);

    int updateByPrimaryKey(TradeMarkCaseCategoryDetil record);

	int insertSelectiveList(List<TradeMarkCaseCategoryDetil> readExcel);

	TradeMarkCaseCategoryDetil selectByGoodName(String goodName);

	TradeMarkCaseCategoryDetil selectByGoodNameAndGroup(@Param("tradeMarkCaseCategoryDetil")TradeMarkCaseCategoryDetil tradeMarkCaseCategoryDetil);
	List<TradeMarkCaseCategoryDetil> selectAll();
}