package com.yootii.bdy.material.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.material.model.MaterialSort;

public interface MaterialSortMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MaterialSort record);

    int insertSelective(MaterialSort record);

    MaterialSort selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MaterialSort record);

    int updateByPrimaryKey(MaterialSort record);
    
    List<MaterialSort>  selectDijiao();
    
	List<MaterialSort> selectByMaterialSort(@Param("materialSort")MaterialSort materialSort, @Param("gcon")GeneralCondition gcon);

	List<MaterialSort> selectChildMaterialSort(@Param("materialSort")MaterialSort materialSort, @Param("gcon")GeneralCondition gcon);
}