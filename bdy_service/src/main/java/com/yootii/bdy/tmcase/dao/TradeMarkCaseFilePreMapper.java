package com.yootii.bdy.tmcase.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.tmcase.model.TradeMarkCaseFilePre;

public interface TradeMarkCaseFilePreMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TradeMarkCaseFilePre record);

    int insertSelective(TradeMarkCaseFilePre record);

    TradeMarkCaseFilePre selectByPrimaryKey(Integer id);
    
    List<TradeMarkCaseFilePre> selectByCaseId(Integer caseId);

    int updateByPrimaryKeySelective(TradeMarkCaseFilePre record);

    int updateByPrimaryKey(TradeMarkCaseFilePre record);
    
    List<TradeMarkCaseFilePre> selectByCustIdAndAgencyId(@Param("custId")Integer custId,@Param("agencyId")Integer agencyId);
    
    int deleteByCustIdAndAgencyId(@Param("custId")Integer custId,@Param("agencyId")Integer agencyId);
}