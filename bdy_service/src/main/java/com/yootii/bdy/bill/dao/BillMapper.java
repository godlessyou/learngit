package com.yootii.bdy.bill.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.bill.model.Bill;
import com.yootii.bdy.common.GeneralCondition;

public interface BillMapper {
    int deleteByPrimaryKey(Integer billId);

    int insert(Bill record);

    int insertSelective(Bill record);
    
    Bill selectByPrimaryKey(Integer billId);

    int updateByPrimaryKeySelective(Bill record);

    int updateByPrimaryKey(Bill record);
    
    Bill generateBillByCases(String[] caseIdArr);
    
    Bill generateBillByChargeRecords(String[] chargeRecordIdArr);
    
    List<Bill> selectBillForCust(@Param("gcon") GeneralCondition gcon,@Param("bill")Bill bill,@Param("userId") Integer userId,@Param("level")Integer level, @Param("daiban") Integer daiban);

    List<Map<String, Long>> selectBillForCustCount(@Param("gcon") GeneralCondition gcon,@Param("bill")Bill bill,@Param("userId") Integer userId,@Param("level")Integer level, @Param("daiban") Integer daiban);
    
    List<Bill> coagencySelectBillMadeByAgency(@Param("gcon") GeneralCondition gcon,@Param("bill")Bill bill,@Param("userId") Integer userId,@Param("level")Integer level);

    List<Map<String, Long>> coagencySelectBillMadeByAgencyCount(@Param("gcon") GeneralCondition gcon,@Param("bill")Bill bill,@Param("userId") Integer userId,@Param("level")Integer level);
    
    List<Bill> adminSelectBillForCust(@Param("gcon") GeneralCondition gcon,@Param("bill")Bill bill,@Param("userId") Integer userId, @Param("daiban") Integer daiban);

    List<Map<String, Long>> adminSelectBillForCustCount(@Param("gcon") GeneralCondition gcon,@Param("bill")Bill bill,@Param("userId") Integer userId, @Param("daiban") Integer daiban);
    
    List<Bill> adminSelectBillMadeByAgency(@Param("gcon") GeneralCondition gcon,@Param("bill")Bill bill,@Param("userId") Integer userId);

    List<Map<String, Long>> adminSelectBillMadeByAgencyCount(@Param("gcon") GeneralCondition gcon,@Param("bill")Bill bill,@Param("userId") Integer userId);
    
    List<String> checkRole(Integer userId);
    
    Integer selectAgencyIdByUserId(Integer userId);
    
    String selectMaxBillNo(Integer billId);
}