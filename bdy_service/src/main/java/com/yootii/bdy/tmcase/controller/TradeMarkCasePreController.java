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
import com.yootii.bdy.tmcase.model.TradeMarkCasePre;
import com.yootii.bdy.tmcase.service.TradeMarkCaseFilePreService;
import com.yootii.bdy.tmcase.service.TradeMarkCasePreService;

@Controller
@RequestMapping("/interface/tmcasepre")
public class TradeMarkCasePreController extends CommonController{
	private final Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private TradeMarkCasePreService  tradeMarkCasePreService;

	@Resource
	private TradeMarkCaseFilePreService tradeMarkCaseFilePreService;
	
	@Resource
	private AuthenticationService authenticationService;
	
	//增加预立案
	@RequestMapping(value = "/createcase", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo createTradeMarkCasePre(GeneralCondition gcon, TradeMarkCasePre tradeMarkCasePre,HttpServletRequest request) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { //通过身份验证
			try {
				Token token = (Token)rtnInfo.getData();
				this.addToken(token);//审计日志需要用到token信息
				this.addURL(request.getRequestURI());
				rtnInfo = tradeMarkCasePreService.createTradeMarkCasePre(tradeMarkCasePre);
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
	//修改预立案
	@RequestMapping(value = "/modifycase", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo modifyTradeMarkCasePre(GeneralCondition gcon, TradeMarkCasePre tradeMarkCasePre) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			Token token = (Token)rtnInfo.getData();
			Integer custId = token.getCustomerID();
			try {
				if(custId==null){
					rtnInfo.setSuccess(false);
					rtnInfo.setMessage("数据异常");
					return rtnInfo;
				}
				tradeMarkCasePre.setCustId(custId);
				rtnInfo = tradeMarkCasePreService.modifyTradeMarkCasePre(tradeMarkCasePre);
			}
			catch (Exception e) {
				e.getMessage();
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	//查询预立案详细信息
	@RequestMapping(value = "/querycasepredetail", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryTradeMarkCasePreDetail(GeneralCondition gcon, TradeMarkCasePre tradeMarkCasePre) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			Integer custId = tradeMarkCasePre.getCustId();
			Integer agencyId = tradeMarkCasePre.getAgencyId();
			try {
				rtnInfo = tradeMarkCasePreService.queryDetailByCustIdAndAgencyId(custId, agencyId);
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
