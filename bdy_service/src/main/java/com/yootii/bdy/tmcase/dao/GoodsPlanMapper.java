package com.yootii.bdy.tmcase.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.tmcase.model.GoodsPlan;


public interface GoodsPlanMapper {
    int deleteByPrimaryKey(Integer planId);

    int insert(GoodsPlan record);

    int insertSelective(GoodsPlan record);

    GoodsPlan selectByPrimaryKey(Integer planId);

    int updateByPrimaryKeySelective(GoodsPlan record);

    int updateByPrimaryKey(GoodsPlan record);
    
    
    List<GoodsPlan> selectByGoodsPlan(@Param("goodsPlan")GoodsPlan goodsPlan);
    
  
}