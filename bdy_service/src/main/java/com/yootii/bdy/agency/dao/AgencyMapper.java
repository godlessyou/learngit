package com.yootii.bdy.agency.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.agency.model.Agency;
import com.yootii.bdy.common.GeneralCondition;

public interface AgencyMapper {

	List<Integer> selectAgencyList();
	
	Agency selectByPrimaryKey(Integer id);

	Map<String, Object> selectbasictotal(GeneralCondition gcon);
	
	Agency selectAgencyByUserId(Integer userId);
	
	Agency selectAgencyByName(String agencyName);
	
	 List<Map<String, Object>> selectAgencyContact(@Param("custId")Integer custId,@Param("agencyId")Integer agencyId);
}
