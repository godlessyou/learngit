package com.yootii.bdy.trademark.dao;

import com.yootii.bdy.trademark.model.TrademarkGonggao;

public interface TrademarkGonggaoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TrademarkGonggao record);

    int insertSelective(TrademarkGonggao record);

    TrademarkGonggao selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TrademarkGonggao record);

    int updateByPrimaryKey(TrademarkGonggao record);
}