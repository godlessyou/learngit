package com.yootii.bdy.downloadapplicant.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.downloadapplicant.model.DocTitle;
import com.yootii.bdy.downloadapplicant.model.DocTitleWithBLOBs;

public interface DocTitleMapper {
    int deleteByPrimaryKey(Integer titleId);

    int insert(DocTitleWithBLOBs record);

    int insertSelective(DocTitleWithBLOBs record);

    DocTitleWithBLOBs selectByPrimaryKey(Integer titleId);

    int updateByPrimaryKeySelective(DocTitleWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(DocTitleWithBLOBs record);

    int updateByPrimaryKey(DocTitle record);
    
    List<String> checkRole(Integer userId);
    
    List<DocTitleWithBLOBs> selectByDocType(@Param("docTypeId")Integer docTypeId,@Param("parentId")Integer parentId,@Param("level")Integer level);
    
    List<DocTitleWithBLOBs> selectByUserIdAndDocType(@Param("userId")Integer userId,@Param("docTypeId")Integer docTypeId,@Param("parentId")Integer parentId,@Param("level")Integer level);
    
    int deleteByParentId(Integer parentId);
    
    List<DocTitleWithBLOBs> selectByParentId(Integer parentId);
    
    int updateOrderNumByDel(DocTitleWithBLOBs record);
    
    int updateOrderNumByIns(DocTitleWithBLOBs record);
    
    int modifyCheckedByTitleIds(@Param("checked")Integer checked,@Param("titleId")Integer titleId);
    
}