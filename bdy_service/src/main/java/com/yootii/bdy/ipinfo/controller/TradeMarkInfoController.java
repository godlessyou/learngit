package com.yootii.bdy.ipinfo.controller;



import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yootii.bdy.common.CommonController;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.ipinfo.service.ForbidContentService;
import com.yootii.bdy.security.service.AuthenticationService;

@Controller
@RequestMapping("/interface/ipinfo")
public class TradeMarkInfoController extends CommonController {
	@Resource
	private ForbidContentService forbidContentService;
	@Resource
	private AuthenticationService authenticationService;

	
	@RequestMapping(value = "/checkForbidContent", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo checkForbidContent(GeneralCondition gcon,String tmName) throws Exception {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		makeOffsetAndRows(gcon);
		if(rtnInfo == null || !rtnInfo.getSuccess()){			
			return rtnInfo;	
		}	
		ReturnInfo rtnInfo2 = forbidContentService.checkForbidContent(tmName);	
		return rtnInfo2;
		
	}
	
	
	
	@RequestMapping(value = "/checkSameTm", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo checkSameTm(GeneralCondition gcon,String tmName,String tmType,String localSearch) throws Exception {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		makeOffsetAndRows(gcon);
		if(rtnInfo == null || !rtnInfo.getSuccess()){			
			return rtnInfo;	
		}		
		ReturnInfo rtnInfo2 = forbidContentService.checkSameTm(tmName, tmType, localSearch, gcon);	
		return rtnInfo2;
		
	}
	
	
	
	
	@RequestMapping(value = "/querySameTm", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo querySameTm(GeneralCondition gcon,String regNumber)  {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		
		if(rtnInfo == null || !rtnInfo.getSuccess()){			
			return rtnInfo;	
		}
		
		ReturnInfo rtnInfo2 =forbidContentService.querySameTm(regNumber, gcon); 
		return rtnInfo2;	
	}
	
	
	@RequestMapping(value = "/queryTm", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryTm(GeneralCondition gcon,String regNumber,String tmType)  {
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(gcon);
		
		if(rtnInfo == null || !rtnInfo.getSuccess()){			
			return rtnInfo;	
		}
		
		ReturnInfo rtnInfo2 =forbidContentService.queryTm(regNumber, tmType, gcon);
		return rtnInfo2;	
	}
	

}