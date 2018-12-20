
package com.yootii.bdy.material.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.material.model.MaterialVersion;

public interface MaterialVersionMapper {
    int deleteByPrimaryKey(Integer versionId);

    int insert(MaterialVersion record);

    int insertSelective(MaterialVersion record);

    MaterialVersion selectByPrimaryKey(Integer versionId);

    int updateByPrimaryKeySelective(MaterialVersion record);

    int updateByPrimaryKey(MaterialVersion record);
    
    MaterialVersion selectByVersion(@Param("materialId")Integer materialId,@Param("versionNo")Integer versionNo);
    
    List<MaterialVersion> selectByMaterialId(Integer materialId);
}