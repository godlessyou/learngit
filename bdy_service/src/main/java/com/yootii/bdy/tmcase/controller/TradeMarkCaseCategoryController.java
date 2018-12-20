package com.yootii.bdy.tmcase.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yootii.bdy.common.CommonController;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.security.service.AuthenticationService;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.TradeMarkCaseCategory;
import com.yootii.bdy.tmcase.service.TradeMarkCaseCategoryService;

@Controller
@RequestMapping("/interface/tmcasecategory")
public class TradeMarkCaseCategoryController extends CommonController{
	private final Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private TradeMarkCaseCategoryService tradeMarkCaseCategoryService;

	@Resource
	private AuthenticationService authenticationService;

	//增加商品或服务
	@RequestMapping(value = "/creategood", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo createTradeMarkCaseCategory(GeneralCondition gcon, TradeMarkCaseCategory good,HttpServletRequest request) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { //通过身份验证
			try {
				Token token = (Token)rtnInfo.getData();
				this.addToken(token);//审计日志需要用到token信息
				this.addURL(request.getRequestURI());
				rtnInfo = tradeMarkCaseCategoryService.createTradeMarkCaseCategory(good);
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
	//修改商品或服务
	@RequestMapping(value = "/modifygood", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo modifyTradeMarkCaseCategory(GeneralCondition gcon,TradeMarkCaseCategory good) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				rtnInfo = tradeMarkCaseCategoryService.modifyTradeMarkCaseCategory(good);
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
	//查询商品或服务详细信息
	@RequestMapping(value = "/querygooddetail", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryTradeMarkCaseDetail(GeneralCondition gcon, TradeMarkCaseCategory good) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				rtnInfo = tradeMarkCaseCategoryService.queryTradeMarkCaseCategoryById(good.getId());
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
	//删除商品或服务
	@RequestMapping(value = "/deletegood", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo deleteTradeMarkCaseCategory(GeneralCondition gcon, TradeMarkCaseCategory good) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				rtnInfo = tradeMarkCaseCategoryService.deleteTradeMarkCaseCategoryById(good.getId());
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
	//检查商品或服务
	@RequestMapping(value = "/checkGoods", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo checkTradeMarkCaseCategory(GeneralCondition gcon, TradeMarkCase tmcase) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				List<TradeMarkCaseCategory> goods = tmcase.getGoods();
				rtnInfo = tradeMarkCaseCategoryService.checkTradeMarkCaseCategoryList(goods);
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
}
