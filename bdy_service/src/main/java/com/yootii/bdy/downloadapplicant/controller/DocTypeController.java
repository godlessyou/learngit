package com.yootii.bdy.downloadapplicant.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yootii.bdy.common.CommonController;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.downloadapplicant.model.DocType;
import com.yootii.bdy.downloadapplicant.service.DocTypeService;
import com.yootii.bdy.model.User;
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.user.service.UserService;

@Controller
@RequestMapping("/interface/docType")
public class DocTypeController extends CommonController{
	@Resource
	private DocTypeService docTypeService;
	@Resource
	private UserService userService;
	@RequestMapping(value = "/queryDocType", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryDocType(HttpServletRequest request, DocType docType,GeneralCondition gcon){
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
			makeOffsetAndRows(gcon);
			try{
				Token token = (Token)rtnInfo.getData();
				rtnInfo = docTypeService.queryDocType(docType, gcon,token);
			}catch(Exception e){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("查询文书类型失败："+e.getMessage());
				return rtnInfo;
			}
		}
		return rtnInfo;
	}
	@RequestMapping(value = "/createDocType", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo createDocType(HttpServletRequest request, DocType docType,GeneralCondition gcon){
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
			makeOffsetAndRows(gcon);
			try{
				Token token = (Token)rtnInfo.getData();
				rtnInfo = docTypeService.createDocType(docType,token);
			}catch(Exception e){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("创建文书类型失败："+e.getMessage());
				return rtnInfo;
			}
		}
		return rtnInfo;
	}
	@RequestMapping(value = "/deleteDocType", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo deleteDocType(HttpServletRequest request, Integer docTypeId,GeneralCondition gcon){
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
			makeOffsetAndRows(gcon);
			try{
				Token token = (Token)rtnInfo.getData();
				rtnInfo = docTypeService.deleteDocType(docTypeId,token);
			}catch(Exception e){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("删除文书类型失败："+e.getMessage());
				return rtnInfo;
			}
		}
		return rtnInfo;
	}
}
