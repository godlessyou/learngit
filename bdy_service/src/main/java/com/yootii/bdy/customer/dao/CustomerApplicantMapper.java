package com.yootii.bdy.customer.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.customer.model.CustomerApplicant;

public interface CustomerApplicantMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerApplicant record);

    int insertSelective(CustomerApplicant record);

    CustomerApplicant selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerApplicant record);

    int updateByPrimaryKey(CustomerApplicant record);

	void unbindApplicant(CustomerApplicant record);

	void bindApplicant(CustomerApplicant record);

	List<CustomerApplicant> selectbyAppIdAndMainId(@Param("id")Integer id,@Param("mainId") Integer mainId);

	List<CustomerApplicant> selectbyAppId(Integer id);
}