package com.yootii.bdy.tmcase.controller;

import java.util.Calendar;
import java.util.Date;




import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.cmp.GenRepContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sun.org.apache.bcel.internal.generic.RETURN;
import com.sun.org.apache.regexp.internal.recompile;
import com.yootii.bdy.common.CommonController;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.Globals;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.remind.model.Remind;
import com.yootii.bdy.remind.service.RemindService;
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.tmcase.model.GoodsPlan;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.TradeMarkCaseRecord;
import com.yootii.bdy.tmcase.model.TradeMarkCaseSolr;
import com.yootii.bdy.tmcase.service.TradeMarkCaseFileService;
import com.yootii.bdy.tmcase.service.TradeMarkCaseRecordService;
import com.yootii.bdy.tmcase.service.TradeMarkCaseService;
import com.yootii.bdy.trademark.service.TradeMarkService;


@Controller
@RequestMapping("/interface/tmcase")
public class TradeMarkCaseController extends CommonController {
	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private TradeMarkCaseService  tradeMarkCaseService;
	
	@Resource
	private TradeMarkCaseFileService tradeMarkCaseFileService;
	
	@Resource
	private TradeMarkCaseRecordService  tradeMarkCaseRecordService;
	
	@Autowired
	private RemindService remindService;
	
	
	/**
	 * 增加案件
	 * @param gcon
	 * @param request
	 * @param tradeMarkCase
	 * @return
	 */
	@RequestMapping(value = "/createcase", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo createTradeMarkCase(GeneralCondition gcon,HttpServletRequest request, TradeMarkCase tradeMarkCase) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				Token token = (Token)rtnInfo.getData();
				this.addToken(token);//审计日志需要用到token信息
				this.addURL(request.getRequestURI());
				rtnInfo = tradeMarkCaseService.createTradeMarkCase(tradeMarkCase, gcon);
			}
			catch (Exception e) {
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	
	/**
	 * 修改案件
	 * @param gcon
	 * @param request
	 * @param tradeMarkCase
	 * @return
	 */
	@RequestMapping(value = "/modifycase", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo modifyeTradeMarkCase(GeneralCondition gcon,HttpServletRequest request, TradeMarkCase tradeMarkCase) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				Token token = (Token)rtnInfo.getData();
				this.addToken(token);//审计日志需要用到token信息
				this.addURL(request.getRequestURI());
				rtnInfo = tradeMarkCaseService.modifyTradeMarkCase(tradeMarkCase, gcon);
			}
			catch (Exception e) {
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	
	/**
	 *查询案件列表
	 * @param gcon
	 * @param request
	 * @param tradeMarkCase
	 * @return
	 */
	@RequestMapping(value = "/querycaselist", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryTradeMarkCaseList(GeneralCondition gcon,HttpServletRequest request, TradeMarkCase tradeMarkCase) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			Token token =(Token)rtnInfo.getData();//token中包含当前登录用户的一些信息
			makeOffsetAndRows(gcon);
			try {
				rtnInfo = tradeMarkCaseService.queryTradeMarkCaseList(tradeMarkCase, gcon,token,0);
			}
			catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	/**
	 * 查询状态为进行中的案件
	 * @param gcon
	 * @param request
	 * @param tradeMarkCase
	 * @param unFinished
	 * @return
	 */
	@RequestMapping(value = "/queryUnfinishedCaselist", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryUnfinishedTradeMarkCase(GeneralCondition gcon,HttpServletRequest request,TradeMarkCase tradeMarkCase,int isFinished){
		ReturnInfo returnInfo = this.checkUser(gcon);
		if(returnInfo!=null && returnInfo.getSuccess()){
			Token token =(Token)returnInfo.getData(); //token中包含当前登录用户的一些信息
			makeOffsetAndRows(gcon);
			try{
				returnInfo = tradeMarkCaseService.queryTradeMarkCaseList(tradeMarkCase, gcon,token,isFinished);

			}catch(Exception e){
				e.printStackTrace();
				returnInfo.setSuccess(false);
				returnInfo.setMessageType(-2);
				returnInfo.setMessage(e.getMessage());
			}
		}
		return returnInfo;
	}
	
	/**
	 * 查询案件详细信息
	 * @param gcon
	 * @param request
	 * @param tradeMarkCase
	 * @return
	 */
	@RequestMapping(value = "/querycasedetail", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryTradeMarkCaseDetail(GeneralCondition gcon,HttpServletRequest request, TradeMarkCase tradeMarkCase) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()){ //通过身份验证
			try {
				
				rtnInfo = tradeMarkCaseService.queryTradeMarkCaseDetail(tradeMarkCase.getId());
			}
			catch (Exception e) {
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	
	/**
	 * 案件关联
	 * @param gcon
	 * @param request
	 * @param caseId
	 * @param agencyId
	 * @param couserId
	 * @return
	 */
	@RequestMapping(value = "/createcaseassociate", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo tradeMarkCaseAssociate(GeneralCondition gcon,HttpServletRequest request,
			Integer caseId, Integer agencyId, Integer couserId) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()){ //通过身份验证
			try {
				Token token = (Token)rtnInfo.getData();
				this.addToken(token);//审计日志需要用到token信息
				this.addURL(request.getRequestURI());
				rtnInfo = tradeMarkCaseService.tradeMarkCaseAssociate(caseId,agencyId,couserId,gcon);
			}
			catch (Exception e) {
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	
	/**
	 * 系统首页工作台 案件统计
	 * @param gcon
	 * @param custId
	 * @param agencyId
	 * @param departId
	 * @return
	 */
	@RequestMapping(value = "/querytmcaseproinfo", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo querytmcaseproinfo(GeneralCondition gcon,Integer custId,Integer agencyId,Integer departId) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
			rtnInfo = tradeMarkCaseService.statsTmcaseProInfo(custId, agencyId, departId);

		}
		return rtnInfo;
		
	}
	
	/**
	 * 
	 * @param gcon
	 * @param custId
	 * @param agencyId
	 * @param departId
	 * @param startYear
	 * @param endYear
	 * @param tradeMarkCase
	 * @return
	 */
	@RequestMapping(value = "/queryTradeMarkCaseBySolr", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryTradeMarkCaseBySolr(GeneralCondition gcon,Integer custId,Integer agencyId,Integer departId,Integer startYear,Integer endYear,TradeMarkCaseSolr tradeMarkCase) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证

			makeOffsetAndRows(gcon);
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			if(startYear == null) {
				startYear = c.get(Calendar.YEAR) - 4;
			} 
			if(endYear == null) {
				endYear = c.get(Calendar.YEAR);
			} 
			
			rtnInfo = tradeMarkCaseService.queryTradeMarkCaseBySolr(departId, custId, agencyId, tradeMarkCase, startYear.toString(), endYear.toString(), gcon);

		}
		return rtnInfo;
	}
	
	/**
	 * 
	 * @param gcon
	 * @param custId
	 * @param startYear
	 * @param endYear
	 * @return
	 */
	@RequestMapping(value = "/statsTmagencyNameList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statsTmagencyNameList(GeneralCondition gcon,Integer custId,Integer startYear,Integer endYear) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
			
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			if(startYear == null) {
				startYear = c.get(Calendar.YEAR) - 4;
			} 
			if(endYear == null) {
				endYear = c.get(Calendar.YEAR);
			} 
			
			rtnInfo = tradeMarkCaseService.statsTmagencyNameList(custId, startYear, endYear);

		}
		return rtnInfo;
	}
	/**
	 * 
	 * @param gcon
	 * @param custId
	 * @param agencyId
	 * @param departId
	 * @param startYear
	 * @param endYear
	 * @return
	 */
	@RequestMapping(value = "/statsTmappCnNameList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statsTmappCnNameList(GeneralCondition gcon,Integer custId,Integer agencyId,Integer departId,Integer startYear,Integer endYear) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
			
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			if(startYear == null) {
				startYear = c.get(Calendar.YEAR) - 4;
			} 
			if(endYear == null) {
				endYear = c.get(Calendar.YEAR);
			} 

			rtnInfo = tradeMarkCaseService.statsTmappCnNameList(custId, agencyId, departId, startYear, endYear);

		}
		return rtnInfo;
	}
	
	
	/**
	 * 
	 * @param gcon
	 * @param custId
	 * @param agencyId
	 * @param departId
	 * @param startYear
	 * @param endYear
	 * @return
	 */
	@RequestMapping(value = "/statsTmcaseTypeList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statsTmcaseTypeList(GeneralCondition gcon,Integer custId,Integer agencyId,Integer departId,Integer startYear,Integer endYear) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
			
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			if(startYear == null) {
				startYear = c.get(Calendar.YEAR) - 4;
			} 
			if(endYear == null) {
				endYear = c.get(Calendar.YEAR);
			} 
			
			rtnInfo = tradeMarkCaseService.statsTmcaseTypeList(custId, agencyId, departId, startYear, endYear);

		}
		return rtnInfo;
	}
	
	/**
	 * 
	 * @param gcon
	 * @param custId
	 * @param agencyId
	 * @param departId
	 * @param startYear
	 * @param endYear
	 * @return
	 */
	@RequestMapping(value = "/statsTmCustTop5List", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statsTmCustTop5List(GeneralCondition gcon,Integer custId,Integer agencyId,Integer departId,Integer startYear,Integer endYear) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证

			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			if(startYear == null) {
				startYear = c.get(Calendar.YEAR) - 4;
			} 
			if(endYear == null) {
				endYear = c.get(Calendar.YEAR);
			} 
			
			rtnInfo = tradeMarkCaseService.statsTmCustTop5List(agencyId, departId, startYear, endYear);

		}
		return rtnInfo;
	}
	
	
	/**
	 * 
	 * @param gcon
	 * @param custId
	 * @param agencyId
	 * @param departId
	 * @param startYear
	 * @param endYear
	 * @return
	 */
	@RequestMapping(value = "/statsTmDateList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statsTmDateList(GeneralCondition gcon,Integer custId,Integer agencyId,Integer departId,Integer startYear,Integer endYear) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证

			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			if(startYear == null) {
				startYear = c.get(Calendar.YEAR) - 4;
			} 
			if(endYear == null) {
				endYear = c.get(Calendar.YEAR);
			} 
			
			
			rtnInfo = tradeMarkCaseService.statsTmDateList(custId, agencyId, departId, startYear, endYear);

		}
		return rtnInfo;
	}
	
	/**
	 * 
	 * @param gcon
	 * @param custId
	 * @param agencyId
	 * @param departId
	 * @param startYear
	 * @param endYear
	 * @return
	 */
	@RequestMapping(value = "/statsTmstatusList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statsTmstatusList(GeneralCondition gcon,Integer custId,Integer agencyId,Integer departId,Integer startYear,Integer endYear) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			if(startYear == null) {
				startYear = c.get(Calendar.YEAR) - 4;
			} 
			if(endYear == null) {
				endYear = c.get(Calendar.YEAR);
			} 
			
			rtnInfo = tradeMarkCaseService.statsTmstatusList(custId, agencyId, departId, startYear, endYear);

		}
		return rtnInfo;
	}
	
	
	
	
	/**
	 * //查询案件修改记录
	 * @param gcon
	 * @param request
	 * @param caseId
	 * @param tradeMarkCaseRecord
	 * @return
	 */
	@RequestMapping(value = "/querycaserecord", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryTradeMarkCaseRecord(GeneralCondition gcon,HttpServletRequest request, String caseId, TradeMarkCaseRecord tradeMarkCaseRecord) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()){ //通过身份验证
			try {	
				if (caseId==null || caseId.equals("")){
					rtnInfo.setSuccess(false);
					rtnInfo.setMessageType(-2);
					rtnInfo.setMessage("缺少必要的参数caseId");
					return rtnInfo;
				}
				Integer id=new Integer(caseId);
				tradeMarkCaseRecord.setId(id);
				rtnInfo = tradeMarkCaseRecordService.queryTradeMarkCaseRecordList(tradeMarkCaseRecord);
			}
			catch (Exception e) {
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	
	
	
	/**
	 * //查询案件列表
	 * @param gcon
	 * @param request
	 * @param tradeMarkCase
	 * @return
	 */
	@RequestMapping(value = "/queryAppOnlineCaseList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryAppOnlineCaseList(GeneralCondition gcon,HttpServletRequest request, TradeMarkCase tradeMarkCase) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			Token token =(Token)rtnInfo.getData();//token中包含当前登录用户的一些信息
			makeOffsetAndRows(gcon);
			try {
				rtnInfo = tradeMarkCaseService.queryAppOnlineCaseList(tradeMarkCase, gcon,token);
			}
			catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	
	
	/**
	 * 
	 * @param gcon
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/update", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo updateTmCaseDetail(GeneralCondition gcon,HttpServletRequest request) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
			Token token = (Token)rtnInfo.getData();
			this.addToken(token);//审计日志需要用到token信息
			this.addURL(request.getRequestURI());
			rtnInfo = tradeMarkCaseService.updateSolrTradeMarkCase();
		}
		return rtnInfo;
	}
	
	/**
	 * 查询案件的 时限提醒
	 * @param request
	 * @param remind
	 * @param gcon
	 * @return
	 */
	@RequestMapping(value="queryTimeRemind",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryTimeRemind(HttpServletRequest request,Remind remind,Integer custId,GeneralCondition gcon){
		ReturnInfo returnInfo =(ReturnInfo)authenticationService.authorize(gcon);
			try {
				if(custId!=null){
					remind.setCustid(custId);
				}
				returnInfo = remindService.selectRemindList(remind, gcon);
				returnInfo.setSuccess(true);
				returnInfo.setMessage("查询成功");
			} catch (Exception e) {
				returnInfo.setSuccess(false);
				returnInfo.setMessage("查询失败");
			}
		return  returnInfo;
	}
	
	/**
	 * 按条件查询案件时限
	 * @param request
	 * @param custId
	 * @param gCondition
	 * @param message  时限类型
	 * @param urgencyType  紧急程度  0：安全期 1：提醒期 2：警告期
	 * @return
	 */
	@RequestMapping(value="queryTmcaseDeadline",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryTmcaseDeadline(HttpServletRequest request,Integer custId,Integer userId,
			GeneralCondition gCondition,String message,Integer urgencyType,Remind remind){
		ReturnInfo returnInfo = authenticationService.authorize(gCondition);
		//非空检验
		if(custId == null && userId == null){
			returnInfo.setSuccess(false);
			returnInfo.setMessage("参数不能为空");
			returnInfo.setMessageType(-2);
			return returnInfo;
		}
		if(message == null){
			returnInfo.setSuccess(false);
			returnInfo.setMessage("参数不能为空");
			returnInfo.setMessageType(-2);
			return returnInfo;
		}
		
		makeOffsetAndRows(gCondition);
		try{
			returnInfo = tradeMarkCaseService.queryTmcaseDeadline(gCondition, custId, userId, message, urgencyType,remind);
			returnInfo.setCurrPage(gCondition.getPageNo());
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询成功");
		}catch (Exception e) {
			e.printStackTrace();
			returnInfo.setMessage("查询失败");
			returnInfo.setSuccess(false);
		}
		return returnInfo;
	}
	
	
	
	
	/**
	 * 统计案件截止日各个状态的数量（安全期，提醒期，危险期）
	 * @param request
	 * @param gcon
	 * @return
	 */
	@RequestMapping(value="statisticTmCaseDeadline",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statisticTmCaseDeadline(HttpServletRequest request,Integer userId,Integer custId,GeneralCondition gcon){
		ReturnInfo returnInfo = authenticationService.authorize(gcon);
		//参数判断  
		if(userId ==null && custId ==null){
			returnInfo.setMessage("参数不能为空 !");
			returnInfo.setSuccess(false);
			return returnInfo;
		}
		if(returnInfo!=null && returnInfo.getSuccess()){
			try{
				returnInfo = tradeMarkCaseService.statisticTmCaseDeadline(gcon,userId,custId);
				returnInfo.setMessage("统计成功");
				returnInfo.setSuccess(true);
			}catch (Exception e) {
				e.printStackTrace();
				returnInfo.setSuccess(false);
				returnInfo.setMessage("统计失败");
			}
		}
		return returnInfo;
	}
	
	/**
	 * 案件时限提醒 de 批量关闭
	 * @return
	 */
	@RequestMapping(value="updateTmCaseDeadLine",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo updateTmCaseDeadLine(HttpServletRequest request,String ids,GeneralCondition gCondition){
		
		ReturnInfo returnInfo = authenticationService.authorize(gCondition);
		if(ids==null || ids==""){
			returnInfo.setMessage("参数不能为空！");
			returnInfo.setSuccess(false);
			returnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
			return returnInfo;
		}
		if(returnInfo != null && returnInfo.getSuccess()){
			try{
				tradeMarkCaseService.updateTmCaseDeadLine(ids);
				returnInfo.setSuccess(true);
				returnInfo.setMessage("更新成功");
			}catch (Exception e) {
				returnInfo.setSuccess(false);
				returnInfo.setMessage("更新失败");
				e.printStackTrace();
			}
		}
		return returnInfo;
	}
	
	
	/**
	 * 获取所有可能的案件类型
	 * @param gcon	 
	 * @return
	 */
	@RequestMapping(value = "/queryCaseTypeList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryCaseTypeList(GeneralCondition gcon, Integer allType) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()){ //通过身份验证
			try {
				if(allType==null){
					allType=0;
				}
				rtnInfo = tradeMarkCaseService.queryCaseTypeList(allType);
			}
			catch (Exception e) {
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	
	
	
	@RequestMapping(value = "/addGoodsPlan", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo addGoodsPlan(GeneralCondition gcon,HttpServletRequest request, GoodsPlan goodsPlan) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				Token token = (Token)rtnInfo.getData();
				this.addToken(token);//审计日志需要用到token信息
				this.addURL(request.getRequestURI());
				rtnInfo = tradeMarkCaseService.addGoodsPlan(goodsPlan);
			}
			catch (Exception e) {
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	
	@RequestMapping(value = "/modifyGoodsPlan", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo modifyGoodsPlan(GeneralCondition gcon,HttpServletRequest request, GoodsPlan goodsPlan) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				Token token = (Token)rtnInfo.getData();
				this.addToken(token);//审计日志需要用到token信息
				this.addURL(request.getRequestURI());
				rtnInfo = tradeMarkCaseService.modifyGoodsPlan(goodsPlan);
			}
			catch (Exception e) {
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	
	
	@RequestMapping(value = "/deleteGoodsPlan", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo deleteGoodsPlan(GeneralCondition gcon,HttpServletRequest request, GoodsPlan goodsPlan) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				Token token = (Token)rtnInfo.getData();
				this.addToken(token);//审计日志需要用到token信息
				this.addURL(request.getRequestURI());
				rtnInfo = tradeMarkCaseService.deleteGoodsPlan(goodsPlan);
			}
			catch (Exception e) {
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	
	
	
	@RequestMapping(value = "/queryGoodsPlan", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryGoodsPlan(GeneralCondition gcon,HttpServletRequest request, GoodsPlan goodsPlan) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				Token token = (Token)rtnInfo.getData();
				this.addToken(token);//审计日志需要用到token信息
				this.addURL(request.getRequestURI());
				rtnInfo = tradeMarkCaseService.queryGoodsPlan(goodsPlan);
			}
			catch (Exception e) {
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	
	
	@RequestMapping(value = "/queryGoods", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryGoods(GeneralCondition gcon,HttpServletRequest request, GoodsPlan goodsPlan) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				Token token = (Token)rtnInfo.getData();
				this.addToken(token);//审计日志需要用到token信息
				this.addURL(request.getRequestURI());
				rtnInfo = tradeMarkCaseService.queryGoods(goodsPlan);
			}
			catch (Exception e) {
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	
	/**
	 *  在异议申请 案件中 被异议人其实是指商标现有的申请人，而在异议答辩案件中，被异议人则是申请异议案件的申请人。
	 * 创建异议答辩类型的案件
	 * @param gcon
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/createDissentTmCase", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo createDissentTmCase(GeneralCondition gcon,HttpServletRequest request,TradeMarkCase tradeMarkCase){
		ReturnInfo rtnInfo = this.checkUser(gcon);
		//参数非空判断
		Integer custId = tradeMarkCase.getCustId();
		String appCnName = tradeMarkCase.getAppCnName();
		String regNumber = tradeMarkCase.getRegNumber();
		String classes = tradeMarkCase.getGoodClasses();
		if(custId ==null){
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("客户 不能为空");
			return  rtnInfo;
		}
		if(appCnName ==null|| appCnName.equals("")){
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("申请人不能为空");
			return  rtnInfo;
		}
		if(regNumber == null || regNumber.equals("")){
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("注册号不能为空");
			return  rtnInfo;		
		}
		if(classes ==null || regNumber.equals("")){
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("商标类别不能为空");
			return  rtnInfo;
		}
		if(rtnInfo.getSuccess() && rtnInfo!=null){
			try{
		//		rtnInfo = tradeMarkCaseService.createDissentTmCase(tradeMarkCase,gcon);	
			}catch (Exception e) {
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("创建失败");
				e.printStackTrace();
			}
		}
		return rtnInfo;
	}
	
	
	/**
	 *
	 * 修改 异议答辩类案件
	 * @param request
	 * @param gcon
	 * @param tradeMarkCase
	 * @return
	 */
	@RequestMapping(value="modifyDissentTmCase",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo modifyDissentTmCase(HttpServletRequest request,GeneralCondition gcon,TradeMarkCase  tradeMarkCase){
		
		ReturnInfo rtnInfo = this.checkUser(gcon);
		Integer id = tradeMarkCase.getId();
		String regNumber = tradeMarkCase.getRegNumber();
		String classes = tradeMarkCase.getGoodClasses();
		if(id ==null){
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("参数不能为空");
			return rtnInfo;
		}
		if(regNumber==null || regNumber.equals("")){
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("注册号不能为空");
			return rtnInfo;
		}
		if(classes ==null || regNumber.equals("")){
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("商标类别不能为空");
			return  rtnInfo;
		}
		if(rtnInfo.getSuccess() &&  rtnInfo!=null){
			try{
				rtnInfo = tradeMarkCaseService.modifyDissentTmCase(tradeMarkCase,gcon);
				rtnInfo.setSuccess(true);
				rtnInfo.setMessage("更新成功");
			}catch (Exception e) {
				e.printStackTrace();
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("更新失败");
			}
		}
		return rtnInfo;
	}
	
	/**
	 * 在异议申请 案件中 被异议人其实是指商标现有的申请人，而在异议答辩案件中，被异议人则是申请异议案件的申请人。
	 * 创建 异议申请案件
	 * @param request
	 * @param gCondition
	 * @param tradeMarkCase
	 * @return
	 */
	@RequestMapping(value="createApplicantDissent",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo createApplicantDissent(HttpServletRequest request,GeneralCondition gCondition,TradeMarkCase tradeMarkCase){
		
		ReturnInfo rtnInfo = this.checkUser(gCondition);
		Integer custId = tradeMarkCase.getCustId();
		String regNumber = tradeMarkCase.getRegNumber();
		String goodClasses = tradeMarkCase.getGoodClasses();
		String appCnName = tradeMarkCase.getAppCnName();
		//参数非空判断
		if(appCnName == null || appCnName.equals("")){
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("异议人不能为空");
			return rtnInfo;
		}
		if(custId == null){
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("客户不能为空");
			return rtnInfo;
		}
		if(regNumber == null || regNumber.equals("")){
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("注册号不能为空");
			return rtnInfo;
		}
		if(goodClasses == null  || goodClasses.equals("")){
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("商标类别不能为空");
			return rtnInfo;
		}
		if(rtnInfo.getSuccess() && rtnInfo != null){
			try{
			//  rtnInfo = tradeMarkCaseService.createApplicantDissent(tradeMarkCase, gCondition);
			}catch (Exception e) {
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("创建失败");
				e.printStackTrace();
			}
		}
		return rtnInfo;
	}
	
	/**
	 * 
	 * 修改  异议申请案件 
	 * @param request
	 * @param gcon
	 * @param tradeMarkCase
	 * @return
	 */
	@RequestMapping(value="modifyApplicantDissent",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo modifyApplicantDissent(HttpServletRequest request,GeneralCondition gcon,TradeMarkCase tradeMarkCase){
		
		ReturnInfo rtnInfo = this.checkUser(gcon);
		Integer id = tradeMarkCase.getId(); 
		//参数非空判断
		if(id == null){
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("案件号不能为空");
			return rtnInfo;
		}
		if(rtnInfo.getSuccess() && rtnInfo !=null){
			try{
				tradeMarkCaseService.modifyApplicantDissent(tradeMarkCase,gcon);
				rtnInfo.setSuccess(true);
				rtnInfo.setMessage("修改成功！");
			}catch (Exception e) {
				e.printStackTrace();
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("修改失败");
			}
		}
		return rtnInfo;
	}
	
	/**
	 * 查询出被异议商标的信息  返回前端
	 * @param request
	 * @param gCondition
	 * @param regNumber
	 * @param goodClass
	 * @return
	 */
	@RequestMapping(value="queryAboutByRegnumber",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryAboutByRegnumber(HttpServletRequest request,GeneralCondition gCondition,
			String regNumber,String goodClass){
		ReturnInfo rtnInfo = this.checkUser(gCondition);
		if(regNumber ==null || regNumber.equals("")){
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("商标号不能为空");
			return rtnInfo;
		}
		if(goodClass == null || goodClass.equals("")){
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("类别不能为空");
			return rtnInfo;
		}
		if(rtnInfo !=null && rtnInfo.getSuccess()){
			try{
				rtnInfo = tradeMarkCaseService.queryAboutByRegnumber(gCondition, regNumber, goodClass);
			}catch (Exception e) {
				e.printStackTrace();
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("查询失败");
			}
		}
		
		return rtnInfo;
		
	}
	
	
	
				
}
