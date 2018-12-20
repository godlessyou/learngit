package com.yootii.bdy.trademark.dao;

import com.yootii.bdy.trademark.model.TrademarkDongtai;

public interface TrademarkDongtaiMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TrademarkDongtai record);

    int insertSelective(TrademarkDongtai record);

    TrademarkDongtai selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TrademarkDongtai record);

    int updateByPrimaryKey(TrademarkDongtai record);
}