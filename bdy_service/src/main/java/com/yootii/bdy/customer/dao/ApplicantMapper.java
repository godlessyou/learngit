package com.yootii.bdy.customer.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.customer.model.Applicant;

public interface ApplicantMapper {
    	
   
	int deleteByPrimaryKey(Integer id);

    int insert(Applicant record);

    int insertSelective(Applicant record);

    Applicant selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Applicant record);

    int updateByPrimaryKey(Applicant record);

    int updateMainAppId(Applicant record);

	List<Applicant> selectByApplicant(@Param("applicant")Applicant applicant, @Param("gcon")GeneralCondition gcon);

	Long selectByApplicantCount(@Param("applicant")Applicant applicant, @Param("gcon")GeneralCondition gcon);

	List<Applicant> selectByApplicantAndTm(@Param("applicant")Applicant applicant, @Param("gcon")GeneralCondition gcon);

	Long selectByApplicantAndTmCount(@Param("applicant")Applicant applicant, @Param("gcon")GeneralCondition gcon);

	List<Applicant> selectByApplicantAndCustId(@Param("applicant")Applicant applicant, @Param("gcon")GeneralCondition gcon, @Param("customerId")Integer customerId);

	Long selectByApplicantCountAndCustId(@Param("applicant")Applicant applicant, @Param("gcon")GeneralCondition gcon, @Param("customerId")Integer customerId);

	Applicant selectByApplicantCNName(@Param("applicant")Applicant applicant);
	
	List<Applicant> getByApplicantName(@Param("applicant")Applicant applicant);

	List<Applicant> selectByApplicantByCustId(@Param("customerId")Integer customerId, @Param("gcon")GeneralCondition gcon);

	Long selectByApplicantByCustIdCount(@Param("customerId")Integer customerId, @Param("gcon")GeneralCondition gcon);

	Applicant selectAppAndMaterialByPrimaryKey(Integer id);

	Applicant selectByName(Applicant applicant);
	
	List<Applicant> selectAllApplicantById(@Param("id")Integer id);
	
	List<Applicant> selectAllApplicantByCustId(@Param("customerId")Integer customerId);
	
	List<Applicant> getApplicantList(@Param("regNumbers")List<String> regNumbers);
	
	List<Applicant> selectByAppNameList(Map<String, Object>map);
	
	List<Applicant> selectApplicantNameByCustId(@Param("customerId")Integer customerId, @Param("gcon")GeneralCondition gcon);

}