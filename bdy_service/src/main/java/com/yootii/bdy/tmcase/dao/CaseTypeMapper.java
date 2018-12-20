package com.yootii.bdy.tmcase.dao;

import com.yootii.bdy.tmcase.model.CaseType;

public interface CaseTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CaseType record);

    int insertSelective(CaseType record);

    CaseType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CaseType record);

    int updateByPrimaryKey(CaseType record);
}