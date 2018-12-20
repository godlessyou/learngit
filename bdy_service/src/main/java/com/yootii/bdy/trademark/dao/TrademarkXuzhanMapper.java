package com.yootii.bdy.trademark.dao;

import com.yootii.bdy.trademark.model.TrademarkXuzhan;

public interface TrademarkXuzhanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TrademarkXuzhan record);

    int insertSelective(TrademarkXuzhan record);

    TrademarkXuzhan selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TrademarkXuzhan record);

    int updateByPrimaryKey(TrademarkXuzhan record);
}