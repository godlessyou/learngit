package com.yootii.bdy.trademark.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.agency.model.Agency;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.trademark.model.Trademark;

public interface TrademarkMapper {
    int deleteByPrimaryKey(Integer tmId);

    int insert(Trademark record);

    int insertSelective(Trademark record);

    Trademark selectByPrimaryKey(Integer tmId);

    Trademark selectByRegnumber(String regNumber);
    
    int updateByPrimaryKeySelective(Trademark record);

    int updateByPrimaryKey(Trademark record);

	List<Map<String, String>> statsTmTop10(@Param("gcon")GeneralCondition gcon);

	List<Trademark> selectByTrademark(@Param("trademark")Trademark trademark, @Param("gcon")GeneralCondition gcon);
	
	List<Map<String, String>> selectCustIdByApplicantName();
	
	List<Trademark> selectByTmName(String tmName);
	

	List<Agency> selectAgentByAppName(String appName);
	
	List<Trademark> selectByAppNameList(Map<String, Object>map);
	
	List<Trademark> selectMultiTmType();
	
	List<Trademark> selectByRegNumberList(Map<String, Object>map);
	
	void mergeTmData(Map<String, Object>map);
	
	void deleteDuplicateTmData(Map<String, Object>map);
	
	List<Map<String, Object>> queryTmNotificationSolrData();
	
	Map<String, Object> selectTmByRegNumber(@Param("regNumber")String regNumber);
	
	List<Trademark> selectAddressList(@Param("trademark")Trademark trademark);
	
	List<Trademark> selectEnAddressList(@Param("trademark")Trademark trademark);
}