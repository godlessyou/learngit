package com.yootii.bdy.tmcase.dao;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.tmcase.model.IssuanceNumber;

public interface IssuanceNumberMapper {
    int deleteByPrimaryKey(Integer issuanceNumId);

    int insert(IssuanceNumber record);

    int insertSelective(IssuanceNumber record);

    IssuanceNumber selectByPrimaryKey(Integer issuanceNumId);

    int updateByPrimaryKeySelective(IssuanceNumber record);

    int updateByPrimaryKey(IssuanceNumber record);
    
    IssuanceNumber selectIssuanceNumber(@Param("caseType") String caseType,@Param("fileName") String fileName);
}