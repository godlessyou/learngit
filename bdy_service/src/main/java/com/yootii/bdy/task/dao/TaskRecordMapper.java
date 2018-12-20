package com.yootii.bdy.task.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.task.model.BillToDoList;
import com.yootii.bdy.task.model.TaskCondition;
import com.yootii.bdy.task.model.TaskRecord;
import com.yootii.bdy.task.model.TmCaseTaskToDoList;
import com.yootii.bdy.tmcase.model.TradeMarkCase;

public interface TaskRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TaskRecord record);

    int insertSelective(TaskRecord record);

    TaskRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TaskRecord record);

    int updateByPrimaryKey(TaskRecord record);
    
    TmCaseTaskToDoList getUserDaiBan(@Param("tmcase")TradeMarkCase tmcase,@Param("userId")Integer userId);
    
    
    TmCaseTaskToDoList getCustomerDaiBan(@Param("tmcase")TradeMarkCase tmcase,@Param("userId")Integer userId);
    
    
    List<TmCaseTaskToDoList> getDaiBanCaseList(@Param("taskCondition")TaskCondition taskCondition, @Param("tmcase")TradeMarkCase tmcase,@Param("userId")Integer userId,@Param("gcon") GeneralCondition gcon);
    
    List<TmCaseTaskToDoList> getDaiBanCaseListForCust(@Param("taskCondition")TaskCondition taskCondition, @Param("tmcase")TradeMarkCase tmcase, @Param("gcon") GeneralCondition gcon);

    long getDaiBanCaseCount(@Param("taskCondition")TaskCondition taskCondition,@Param("tmcase")TradeMarkCase tmcase,@Param("userId")Integer userId,@Param("pageId")Integer pageId, @Param("gcon") GeneralCondition gcon);

	long getDaiBanBillCount(@Param("taskCondition")TaskCondition taskCondition);

	List<BillToDoList> getDaiBanBillList(@Param("taskCondition")TaskCondition taskCondition, @Param("gcon") GeneralCondition gcon);

	long getAgencyDaiBanCaseCount(@Param("taskCondition")TaskCondition taskCondition, @Param("agencyId")Integer agencyId);

	
	List<TmCaseTaskToDoList> getDaiBanCaseWithTask(@Param("taskCondition")TaskCondition taskCondition, @Param("tmcase")TradeMarkCase tmcase,@Param("userId")Integer userId, @Param("gcon") GeneralCondition gcon);
    
	long getDaiBanCaseWithTaskCount(@Param("taskCondition")TaskCondition taskCondition,@Param("tmcase")TradeMarkCase tmcase,@Param("userId")Integer userId,@Param("gcon") GeneralCondition gcon);

	Map<String , Object> queryByCaseIdAndFileName(@Param("caseId")Integer caseId,@Param("fileName")int fileName);

	
}