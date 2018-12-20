package com.yootii.bdy.tmcase.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.tmcase.model.TradeMarkCaseFile;

public interface TradeMarkCaseFileMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TradeMarkCaseFile record);

    int insertSelective(TradeMarkCaseFile record);

    TradeMarkCaseFile selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TradeMarkCaseFile record);

    int updateByPrimaryKey(TradeMarkCaseFile record);
    
    int copyTmCaseFileRecord(@Param("assoCaseId") Integer assoCaseId,@Param("caseId")Integer caseId);
    
    List<TradeMarkCaseFile> selectByCaseId(@Param("caseId")Integer caseId);
    
    List<TradeMarkCaseFile> selectByCaseIdAndFileNames(@Param("caseId")Integer caseId,@Param("fileNames")String fileNames);
}