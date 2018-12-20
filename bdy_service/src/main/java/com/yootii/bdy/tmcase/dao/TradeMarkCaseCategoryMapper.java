package com.yootii.bdy.tmcase.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.tmcase.model.TradeMarkCaseCategory;

public interface TradeMarkCaseCategoryMapper {
	
	int deleteByCaseId(Integer caseId);
	
	int deleteByCasePreId(Integer casePreId);

    int deleteByPrimaryKey(Integer id);    

    int insert(TradeMarkCaseCategory record);

    int insertSelective(TradeMarkCaseCategory record);

    TradeMarkCaseCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TradeMarkCaseCategory record);

    int updateByPrimaryKey(TradeMarkCaseCategory record);
    
    int copyTmCaseCategoryRecord(@Param("assoCaseId") Integer assoCaseId,@Param("caseId")Integer caseId);
    
    TradeMarkCaseCategory selectGoodKeyByGoodNameAndClass(@Param("goodName")String goodName,@Param("goodClass")String goodClass);
    
    List<TradeMarkCaseCategory> selectByTradeMarkCaseCategory(TradeMarkCaseCategory good);
    
    TradeMarkCaseCategory selectCaseCategoryDetailByGood(@Param("good")TradeMarkCaseCategory good);

    List<Map<String,Object >> categoryList(@Param("caseId")Integer caseId);
}