package com.yootii.bdy.bill.dao;

import java.util.Map;

import com.yootii.bdy.bill.model.BillChargeRecord;

public interface BillChargeRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BillChargeRecord record);

    int insertSelective(BillChargeRecord record);

    BillChargeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BillChargeRecord record);

    int updateByPrimaryKey(BillChargeRecord record);
    
    int batchInsertBillChargeRecord(Map<String, Object> params);
}