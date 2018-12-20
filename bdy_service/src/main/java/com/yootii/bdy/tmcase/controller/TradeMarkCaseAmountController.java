package com.yootii.bdy.tmcase.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yootii.bdy.common.CommonController;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.service.TradeMarkCaseAmountService;


@Controller
@RequestMapping("/interface/tmcase")
public class TradeMarkCaseAmountController extends CommonController{
	@Resource
	private TradeMarkCaseAmountService tradeMarkCaseAmountService;
	
	private final Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 查询某个客户的案件
	 * @param gcon
	 * @param request
	 * @param tradeMarkCase
	 * @param interfacetype
	 * @return
	 */
	@RequestMapping(value = "/queryamountbycustomer", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryTmCaseByCustomer(GeneralCondition gcon,HttpServletRequest request, TradeMarkCase tradeMarkCase,String interfacetype) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				rtnInfo = tradeMarkCaseAmountService.queryTmCaseByCustomer(tradeMarkCase,gcon,interfacetype);
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
	 * 查询某代理人员的案件
	 * @param gcon
	 * @param request
	 * @param tradeMarkCase
	 * @param interfacetype
	 * @return
	 */
	@RequestMapping(value = "/queryamountbyuser", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryTmCaseAmountByAgencyUser(GeneralCondition gcon,HttpServletRequest request, TradeMarkCase tradeMarkCase,String interfacetype,Integer userId) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				rtnInfo = tradeMarkCaseAmountService.queryTmCaseAmountByAgencyUser(tradeMarkCase,gcon,interfacetype,userId);
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
	 * 查询某代理机构案件的案件。
	 * @param gcon
	 * @param request
	 * @param tradeMarkCase
	 * @param interfacetype
	 * @return
	 */
	@RequestMapping(value = "/queryamountbyagency", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryTmCaseAmountByAgency(GeneralCondition gcon,HttpServletRequest request, TradeMarkCase tradeMarkCase,String interfacetype) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				rtnInfo = tradeMarkCaseAmountService.queryTmCaseAmountByAgency(tradeMarkCase,gcon,interfacetype);
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
	 * 查询平台的案件
	 * @param gcon
	 * @param request
	 * @param tradeMarkCase
	 * @param interfacetype
	 * @return
	 */
	@RequestMapping(value = "/queryamountbypalteform", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryTmCaseAmountByPalteform(GeneralCondition gcon,HttpServletRequest request, TradeMarkCase tradeMarkCase,String interfacetype) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				rtnInfo = tradeMarkCaseAmountService.queryTmCaseAmountByPalteform(tradeMarkCase,gcon,interfacetype);
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
	 * 客户案件top10
	 * @param gcon
	 * @param request
	 * @param tradeMarkCase
	 * @param interfacetype
	 * @return
	 */
	@RequestMapping(value = "/statstmcasetop10", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo statstmCasetop10(GeneralCondition gcon,HttpServletRequest request) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				rtnInfo = tradeMarkCaseAmountService.statstmCasetop10(gcon);
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
	
}
