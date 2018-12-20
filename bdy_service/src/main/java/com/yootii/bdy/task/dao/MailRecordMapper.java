package com.yootii.bdy.task.dao;

import java.util.List;

import com.yootii.bdy.task.model.MailRecord;

public interface MailRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MailRecord record);

    int insertSelective(MailRecord record);

    MailRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MailRecord record);

    int updateByPrimaryKey(MailRecord record);
    
    List<MailRecord> getMailRecordList(Integer caseId);
}