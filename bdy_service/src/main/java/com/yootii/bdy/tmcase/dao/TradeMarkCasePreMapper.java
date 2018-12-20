package com.yootii.bdy.tmcase.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.tmcase.model.TradeMarkCasePre;

public interface TradeMarkCasePreMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TradeMarkCasePre record);

    int insertSelective(TradeMarkCasePre record);

    TradeMarkCasePre selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TradeMarkCasePre record);

    int updateByPrimaryKey(TradeMarkCasePre record);
    
    List<TradeMarkCasePre> selectByTmCase(@Param("tmcase")TradeMarkCasePre tmcase,@Param("gcon") GeneralCondition gcon);
    
    List<Map<String,Long>> selectByTmCaseCount(@Param("tmcase")TradeMarkCasePre tmcase,@Param("gcon") GeneralCondition gcon);
    
    int deleteByCustIdAndAgencyId(@Param("custId")Integer custId,@Param("agencyId")Integer agencyId);
    
    TradeMarkCasePre selectByCustIdAndAgencyId(@Param("custId")Integer custId,@Param("agencyId")Integer agencyId);
}