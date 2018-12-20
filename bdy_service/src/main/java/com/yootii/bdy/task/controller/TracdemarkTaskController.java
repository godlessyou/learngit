package com.yootii.bdy.task.controller;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yootii.bdy.common.CommonController;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.security.service.AuthenticationService;
import com.yootii.bdy.task.service.TracdemarkTaskService;

/**
 * 商标续展提醒任务
 * @author 
 *
 */
@Controller
@RequestMapping("interface/trademarkTask")
public class TracdemarkTaskController extends CommonController{
	private final Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private TracdemarkTaskService tracdemarkTaskService;
	@Resource
	private AuthenticationService authenticationService;
	
	/**
	 * 查询续展通知列表
	 * @param request
	 * @param gcon
	 * @return
	 */
	@RequestMapping(value="queryList",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryList(HttpServletRequest request,GeneralCondition gcon){
		ReturnInfo returnInfo = authenticationService.authorize(gcon);
		if(returnInfo!=null&& !returnInfo.getSuccess()){
			return returnInfo;
		}
		try{
			returnInfo = tracdemarkTaskService.list(1);
			returnInfo.setSuccess(true);
			returnInfo.setMessage("查询 成功");
		}catch (Exception e) {
			e.printStackTrace();
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询失败");
		}
		return returnInfo;
	}
	
	/**
	 * 商标续展 客户查询
	 */
	@RequestMapping(value="queryMailsInfor",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryMailsInfor(HttpServletRequest request,GeneralCondition gcon,Integer custId){
		ReturnInfo returnInfo = authenticationService.authorize(gcon);
		if(returnInfo!=null&& !returnInfo.getSuccess()){
			return returnInfo;
		}
		try{
			returnInfo = tracdemarkTaskService.findTmList(custId);
			returnInfo.setMessage("查询成功");
			returnInfo.setSuccess(true);
		}catch (Exception e) {
			e.printStackTrace();
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询失败");
		}
		return returnInfo;
	}
	
	/**
	 * 查看续展时限提醒记录接口
	 */
	@RequestMapping(value="queryRenewalRemindList",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryRenewalRemindList(HttpServletRequest request,GeneralCondition gcon,Integer custId){
		
		ReturnInfo returnInfo = authenticationService.authorize(gcon);
		
		if(custId==null){
			returnInfo.setMessage("custId参数不能为空");
			returnInfo.setSuccess(false);
			return returnInfo;
		}
		makeOffsetAndRows(gcon);
	//	if(returnInfo !=null && returnInfo.getSuccess()){
			try{
				returnInfo = tracdemarkTaskService.queryRenewalRemindList(gcon,custId);
				returnInfo.setSuccess(true);
				returnInfo.setCurrPage(gcon.getPageNo());
				returnInfo.setMessage("查询 成功");
			}catch (Exception e) {
				e.printStackTrace();
				returnInfo.setSuccess(false);
				returnInfo.setMessage("查询失败");
			}
		
	//	}
		return returnInfo;
	}
	
	
	
}
