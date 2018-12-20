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
import com.yootii.bdy.tmcase.model.TradeMarkCaseProcess;
import com.yootii.bdy.tmcase.service.TradeMarkCaseProcessService;

@Controller
@RequestMapping("/interface/tmcaseprocess")
public class TradeMarkCaseProcessController extends CommonController{
	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private TradeMarkCaseProcessService tradeMarkCaseProcessService;
	
	
	@RequestMapping(value = "/querycaseprocess", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object queryCaseProcess(HttpServletRequest request,TradeMarkCaseProcess tradeMarkCaseProcess,GeneralCondition gcon) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if(rtnInfo!=null&&rtnInfo.getSuccess()){
			Token token = (Token)rtnInfo.getData();
			this.addToken(token);//审计日志需要用到token信息
			this.addURL(request.getRequestURI());
			try{
				rtnInfo = tradeMarkCaseProcessService.queryTradeMarkCaseProcess(tradeMarkCaseProcess);
			}catch(Exception e){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}
		return rtnInfo;
	}
}
