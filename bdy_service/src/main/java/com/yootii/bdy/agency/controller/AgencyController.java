package com.yootii.bdy.agency.controller;

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

@Controller
@RequestMapping("/interface/agency")
public class AgencyController extends CommonController{
	private final Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private AgencyService  agencyService;
	
	/**
	 *  基本信息统计（户代理机构总数、客总数、案件总数，客户商标总数，待办任务总数）;
	 * @param gcon
	 * @param request
	 
	 * @return
	 */
		@RequestMapping(value = "/basicinfobyplateform", produces = "application/json;charset=UTF-8")
		@ResponseBody
		public ReturnInfo basicInfoByPlateForm(GeneralCondition gcon,HttpServletRequest request) {
			ReturnInfo rtnInfo = this.checkUser(gcon);
			if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
				try {
					
					rtnInfo = agencyService.basicinfobyplateform(gcon);
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
