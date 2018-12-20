package com.yootii.bdy.bill.dao;

import java.util.Map;

import com.yootii.bdy.bill.model.BillCase;

public interface BillCaseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BillCase record);

    int insertSelective(BillCase record);

    BillCase selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BillCase record);

    int updateByPrimaryKey(BillCase record);
    
    int batchInsertBillCase(Map<String, Object> params);
}