package com.yootii.bdy.downloadapplicant.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.downloadapplicant.model.DocType;

public interface DocTypeMapper {
    int deleteByPrimaryKey(Integer docTypeId);

    int insert(DocType record);

    int insertSelective(DocType record);

    DocType selectByPrimaryKey(Integer docTypeId);

    int updateByPrimaryKeySelective(DocType record);

    int updateByPrimaryKey(DocType record);
    
    List<DocType> selectByDocType(@Param("docType")DocType docType,@Param("gcon")GeneralCondition gcon);
    
    int selectByDocTypeCount(@Param("docType")DocType docType,@Param("gcon")GeneralCondition gcon);
    
    List<String> checkRole(Integer userId);
}