package com.yootii.bdy.agency.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.agency.model.AgencyUser;

public interface AgencyUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AgencyUser record);

    int insertSelective(AgencyUser record);

    AgencyUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AgencyUser record);

    int updateByPrimaryKey(AgencyUser record);

	Integer selectByAgencyAndUserId(@Param("agencyId")Integer id, @Param("userId")Integer userId);

	Integer selectUserCountByAgencId(@Param("agencyId")Integer id);
	
	Integer selectAgencyIdByUserId(Integer userId);
	
	int deleteByUserId(Integer userId);

	List<Integer> selectUserIdByAgencyId(Integer agencyId);
	
}