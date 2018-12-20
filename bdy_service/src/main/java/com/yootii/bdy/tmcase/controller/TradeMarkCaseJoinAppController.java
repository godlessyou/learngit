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
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.security.service.AuthenticationService;
import com.yootii.bdy.tmcase.model.TradeMarkCaseJoinApp;
import com.yootii.bdy.tmcase.service.TradeMarkCaseJoinAppService;

@Controller
@RequestMapping("/interface/tmcasejoinapp")
public class TradeMarkCaseJoinAppController extends CommonController{
	private final Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private TradeMarkCaseJoinAppService  tradeMarkCaseJoinAppService;

	@Resource
	private AuthenticationService authenticationService;
	
	//增加共同申请人
	@RequestMapping(value = "/createjoinapp", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo createTradeMarkCaseJoinApp(GeneralCondition gcon, TradeMarkCaseJoinApp joinApp,HttpServletRequest request) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { //通过身份验证
			try {
				Token token = (Token)rtnInfo.getData();
				this.addToken(token);//审计日志需要用到token信息
				this.addURL(request.getRequestURI());
				rtnInfo = tradeMarkCaseJoinAppService.createTradeMarkCaseJoinApp(joinApp);
			}catch (Exception e) {
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	//修改共同申请人
	@RequestMapping(value = "/modifyjoinapp", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo modifyTradeMarkCaseJoinApp(GeneralCondition gcon, TradeMarkCaseJoinApp joinApp) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				rtnInfo = tradeMarkCaseJoinAppService.modifyTradeMarkCaseJoinApp(joinApp);
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
	//查询共同申请人详细信息
	@RequestMapping(value = "/queryjoinappdetail", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryTradeMarkCaseDetail(GeneralCondition gcon, TradeMarkCaseJoinApp joinApp) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				rtnInfo = tradeMarkCaseJoinAppService.queryTradeMarkCaseJoinAppById(joinApp.getId());
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
	//删除共同申请人详细信息
	@RequestMapping(value = "/deletejoinapp", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo deleteTradeMarkCaseJoinApp(GeneralCondition gcon, TradeMarkCaseJoinApp joinApp) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				rtnInfo = tradeMarkCaseJoinAppService.deleteTradeMarkCaseJoinAppById(joinApp.getId());
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
	//查询共同申请人详细信息
		@RequestMapping(value = "/queryjoinapplist", produces = "application/json;charset=UTF-8")
		@ResponseBody
		public ReturnInfo queryTradeMarkCaseJoinAppList(GeneralCondition gcon, TradeMarkCaseJoinApp joinApp) {
			ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
			if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
				try {
					rtnInfo = tradeMarkCaseJoinAppService.queryTradeMarkCaseJoinAppList(joinApp);
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
