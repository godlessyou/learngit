package com.yootii.bdy.bill.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.bill.model.ChargeRecord;
import com.yootii.bdy.common.GeneralCondition;

public interface ChargeRecordMapper {
    int deleteByPrimaryKey(Integer chargeRecordId);

    int insert(ChargeRecord record);

    int insertSelective(ChargeRecord record);
    
//    int insertSelectiveWithUserId(@Param("record")ChargeRecord record,@Param("userId")Integer userId);

    ChargeRecord selectByPrimaryKey(Integer chargeRecordId);

    int updateByPrimaryKeySelective(ChargeRecord record);

    int updateByPrimaryKey(ChargeRecord record);
    
    List<ChargeRecord> selectByChargeRecord(@Param("gcon") GeneralCondition gcon, 
    		@Param("chargeRecord") ChargeRecord chargeRecord,@Param("userId") Integer userId,@Param("custId") Integer custId);
    
    List<Map<String,Long>> selectChargeRecordCount(@Param("gcon") GeneralCondition gcon, 
    		@Param("chargeRecord") ChargeRecord chargeRecord,@Param("userId") Integer userId,@Param("custId") Integer custId);
    
    List<ChargeRecord> selectByCaseIds(String[] caseIds);
    
    int selectChargeRecordNoVerifyCount(Map<String, Object> params);//返回值为传入的ids中未核销记录数
    
    int updateByChargeRecordIds(String[] chargeRecordIds);
        
    ChargeRecord selectByCaseId(Integer caseId);
}