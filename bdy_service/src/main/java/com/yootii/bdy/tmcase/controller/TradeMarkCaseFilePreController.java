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
import com.yootii.bdy.tmcase.model.TradeMarkCaseFilePre;
import com.yootii.bdy.tmcase.model.TradeMarkCasePre;
import com.yootii.bdy.tmcase.service.TradeMarkCaseFilePreService;

@Controller
@RequestMapping("/interface/tmcasefilepre")
public class TradeMarkCaseFilePreController extends CommonController{
	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private TradeMarkCaseFilePreService tradeMarkCaseFilePreService;
	
	@Resource
	private AuthenticationService authenticationService;
	
	@RequestMapping(value = "/uploadcasefile", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object uploadCaseFile(HttpServletRequest request,Integer caseId,TradeMarkCasePre tradeMarkCasePre,TradeMarkCaseFilePre tradeMarkCaseFilePre,GeneralCondition gcon,Integer applicantId) {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try{
				Token token = (Token)rtnInfo.getData();
				this.addToken(token);//审计日志需要用到token信息
				this.addURL(request.getRequestURI());
				tradeMarkCasePre.setId(caseId);
			rtnInfo = tradeMarkCaseFilePreService.uploadCaseFile(request,tradeMarkCasePre,tradeMarkCaseFilePre,gcon,token,applicantId);
			}catch(Exception e) {
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
