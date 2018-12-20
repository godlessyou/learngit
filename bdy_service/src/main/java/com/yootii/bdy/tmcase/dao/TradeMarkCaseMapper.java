package com.yootii.bdy.tmcase.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.remind.model.Remind;
import com.yootii.bdy.task.model.TmCaseTaskToDoList;
import com.yootii.bdy.tmcase.model.TmCaseAppOnline;
import com.yootii.bdy.tmcase.model.TradeMarkCase;

public interface TradeMarkCaseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TradeMarkCase record);

    int insertSelective(TradeMarkCase record);

    TradeMarkCase selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TradeMarkCase record);

    int updateByPrimaryKey(TradeMarkCase record);
    
    List<String> checkRole(Integer userId);
    
    List<TradeMarkCase> selectByTmCase(@Param("tmcase")TradeMarkCase tmcase,@Param("gcon") GeneralCondition gcon,@Param("userId")Integer userId,@Param("isFinished")int isFinished);
    
    List<Map<String,Long>> selectByTmCaseCount(@Param("tmcase")TradeMarkCase tmcase,@Param("gcon") GeneralCondition gcon,@Param("userId")Integer userId,@Param("isFinished")int isFinished);
    
    List<TradeMarkCase> selectAllByTmCase(@Param("tmcase")TradeMarkCase tmcase,@Param("gcon") GeneralCondition gcon,@Param("userId")Integer userId,@Param("isFinished")int isFinished);

    List<Map<String,Long>> selectAllByTmCaseCount(@Param("tmcase")TradeMarkCase tmcase,@Param("gcon") GeneralCondition gcon,@Param("userId")Integer userId,@Param("isFinished")int isFinished);
    
    List<TradeMarkCase> selectTmCaseByDept(@Param("tmcase")TradeMarkCase tmcase,@Param("gcon") GeneralCondition gcon,@Param("userId")Integer userId,@Param("level") Integer level,@Param("isFinished")int isFinished);
    
    List<Map<String,Long>> selectTmCaseByDeptCount(@Param("tmcase")TradeMarkCase tmcase,@Param("gcon") GeneralCondition gcon,@Param("userId")Integer userId,@Param("level") Integer level,@Param("isFinished")int isFinished);
    
    int copyTrademarkCase(Integer id);
    
    int insertAgencyCustomer(@Param("agencyId") Integer agencyId,@Param("custId") Integer custId,@Param("cotag") Integer cotag);

    Integer getMaxCaseGroup();
    
	List<TmCaseAppOnline> getAppOnlineCaseList(@Param("tmcase")TradeMarkCase tmcase, @Param("gcon") GeneralCondition gcon,@Param("userId")Integer userId);

	
	Long getAppOnlineCaseCount(@Param("tmcase")TradeMarkCase tmcase, @Param("gcon") GeneralCondition gcon,@Param("userId")Integer userId);

	List<TradeMarkCase> selectAllAppOnlineCaseList(@Param("tmcase")TradeMarkCase tmcase, @Param("gcon") GeneralCondition gcon,@Param("userId")Integer userId);

	List<Map<String, Long>> selectAllAppOnlineCaseCount(@Param("tmcase")TradeMarkCase tmcase, @Param("gcon") GeneralCondition gcon,@Param("userId")Integer userId);

	List<TradeMarkCase> selectAppOnlineCaseListByDept(@Param("tmcase")TradeMarkCase tmcase,@Param("gcon") GeneralCondition gcon,@Param("userId")Integer userId,@Param("level") Integer level);

	List<Map<String, Long>> selectAppOnlineCaseListByDeptCount(@Param("tmcase")TradeMarkCase tmcase,@Param("gcon") GeneralCondition gcon,@Param("userId")Integer userId,@Param("level") Integer level);

	List<TradeMarkCase> selectByAppNameGoodClass(@Param("tmcase")TradeMarkCase tmcase);

	TradeMarkCase selectByTmCaseSimple(@Param("tmcase")TradeMarkCase tmcase);
	
	TradeMarkCase selectTmCaseByProcessId(@Param("tmcase")TradeMarkCase tmcase);
	
	List<TradeMarkCase> selectByCaseGroup(@Param("caseGroups")List<String> caseGroups);
	    
	List<Remind> selectRemindList(@Param("userId")Integer userId,@Param("custId")Integer custId,@Param("message")String message);
	
	void updateTmCaseDeadLine(@Param("rid")String rid);
	
	Map<String, Object> selectRemindByMessage(@Param("message")String message,@Param("caseId")Integer caseId);
	
	void deleteById(@Param("rid")int rid);
	void updateById(Map<String, Object> map);
	List<Remind> queryByCondition(Map<String, Object> map);
	int queryByConditionCount(Map<String, Object> map);
	void  updatePriority(@Param("id")int id);
	List<TradeMarkCase> selectByRegNumberList(Map<String, Object>map);
	
	TradeMarkCase selectByParentId(Integer parentId);

	void createDissentTmCase(TradeMarkCase tradeMarkCase);
	void modifyDissentTmCase(TradeMarkCase tradeMarkCase);
	void modifyApplicantDissent(TradeMarkCase tradeMarkCase);
	Integer createApplicantDissent(TradeMarkCase tradeMarkCase);
	
	Map<String, Object> selectByReg(@Param("regNumber")String regNumber);
	Map<String, Object> selectByRegAndType(@Param("regNumber")String regNumber,@Param("caseTypeId") Integer caseTypeId);
	Map<String,Object> selectByRegFromTM(@Param("regNumber") String regNumber);
	
	Map<String, Object> selectContactByCustId(@Param("custId")Integer custId);

	List<Map<String, Object>> selectUserByAgencyId(@Param("agencyId")Integer agencyId);
	
	int isExist(@Param("caseId") Integer caseId);
	
	Map<String, Object> selectMaterial(@Param("caseId") Integer caseId);
	Map<String, Object> selectMaterialForApplicant(@Param("caseId") Integer caseId);
	Map<String, Object> selectMaterialForProxy(@Param("caseId") Integer caseId);
	Map<String, Object> selectMaterialForReply(@Param("caseId") Integer caseId);
	int updateMaterialForReply(Map<String, Object> map);
	List<TradeMarkCase> selectProcessId(String appCnName);
	
}