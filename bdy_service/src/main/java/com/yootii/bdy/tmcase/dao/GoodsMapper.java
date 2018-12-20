package com.yootii.bdy.tmcase.dao;

import java.util.List;

import com.yootii.bdy.tmcase.model.Goods;

public interface GoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKey(Goods record);
    
    List<Goods> selectByPlanId(Integer planId);
}