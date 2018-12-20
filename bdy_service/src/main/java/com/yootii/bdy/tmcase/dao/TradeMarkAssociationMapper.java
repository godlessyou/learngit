package com.yootii.bdy.tmcase.dao;

import com.yootii.bdy.tmcase.model.TradeMarkAssociation;

public interface TradeMarkAssociationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TradeMarkAssociation record);

    int insertSelective(TradeMarkAssociation record);

    TradeMarkAssociation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TradeMarkAssociation record);

    int updateByPrimaryKey(TradeMarkAssociation record);
    
    TradeMarkAssociation selectByCaseId(Integer caseId);
    
    
}