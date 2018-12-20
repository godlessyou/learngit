package com.yootii.bdy.log.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yootii.bdy.agency.service.AgencyService;
import com.yootii.bdy.common.CommonController;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.log.model.AuditLogEntity;
import com.yootii.bdy.log.service.LogService;
@Controller
@RequestMapping("/interface/log")
public class LogController extends CommonController{
	private final Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private LogService  logService;
	
	@RequestMapping(value = "/querylog", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryLog(GeneralCondition gcon,HttpServletRequest request,AuditLogEntity log) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				makeOffsetAndRows(gcon);
				rtnInfo = logService.queryLog(gcon,log);
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
	@RequestMapping(value = "/querylogbytable", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryLogByTable(GeneralCondition gcon,HttpServletRequest request,AuditLogEntity log,Integer id) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			try {
				makeOffsetAndRows(gcon);
				log.setPrimaryKey(id);
				//log.setOperateType("update");
				rtnInfo = logService.queryLogByTable(gcon,log);
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
