package com.yootii.bdy.material.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.customer.model.Customer;
import com.yootii.bdy.material.model.Material;
import com.yootii.bdy.material.model.MaterialCondition;
import com.yootii.bdy.model.User;
import com.yootii.bdy.tmcase.model.TradeMarkCaseJoinApp;

public interface MaterialMapper {
    int deleteByPrimaryKey(Integer materialId);

    int insert(Material record);

    int insertSelective(Material record);

    Material selectByPrimaryKey(Integer materialId);

    int updateByPrimaryKeySelective(Material record);

    int updateByPrimaryKey(Material record);
    
    
    Integer getMaxId();
    
    long getMaterialCountWithUser(@Param("user")User user, @Param("material")Material material,@Param("gcon")GeneralCondition gcon);

	List<Material> selectByMaterialWithUser(@Param("user")User user, @Param("material")Material material,  @Param("gcon")GeneralCondition gcon);
	
    long getMaterialCountWithCustomer(@Param("customer")Customer customer, @Param("material")Material material,@Param("gcon")GeneralCondition gcon);

	List<Material> selectByMaterialWithCustomer(@Param("customer")Customer customer, @Param("material")Material material,  @Param("gcon")GeneralCondition gcon);

	List<Material> selectByCustIdAndAgencyId(@Param("custId")Integer custId, @Param("agencyId")Integer agencyId);

	List<Material> selectImageByCaseId(Integer caseId);

	List<Material> selectByCaseIdAndFileNames(@Param("materialCondition")MaterialCondition  materialCondition);

	List<Material> selectMaterialByJoinApp(@Param("tradeMarkCaseJoinApp")TradeMarkCaseJoinApp t);

	List<Material> selectByMaterialWithApplicantId(@Param("material")Material material, @Param("gcon")GeneralCondition gcon);

	void copyTmCaseFileRecord(@Param("assoCaseId")Integer assoCaseId, @Param("caseId")Integer caseId);

	List<Material> selectByTmId(@Param("material")Material material, @Param("tmId")Integer tmId, @Param("gcon")GeneralCondition gcon);

	Long selectCountByTmId(@Param("material")Material material, @Param("tmId")Integer tmId, @Param("gcon")GeneralCondition gcon);

	List<Material> selectByNewMaterial(@Param("material")Material material,  @Param("gcon")GeneralCondition gcon);
	
	long selectByNewMaterialCount(@Param("material")Material material,  @Param("gcon")GeneralCondition gcon);

	List<Material> selectByappId(@Param("material")Material material, @Param("gcon")GeneralCondition gcon);
	
    Material selectByCaseIdLast(Integer caseId);
    
    Map<String, Object> selectByFileAndCase(@Param("custId")Integer custId,@Param("caseId")Integer caseId);

    List<Map<String, Object>> selectByUserId(@Param("userId")Integer userId,@Param("docTypeId")Integer docTypeId);
}