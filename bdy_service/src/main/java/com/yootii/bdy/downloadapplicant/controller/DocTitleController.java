package com.yootii.bdy.downloadapplicant.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yootii.bdy.common.CommonController;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.downloadapplicant.model.DocTitle;
import com.yootii.bdy.downloadapplicant.model.DocTitleWithBLOBs;
import com.yootii.bdy.downloadapplicant.service.DocTitleService;
import com.yootii.bdy.security.model.Token;

@Controller
@RequestMapping("/interface/docTitle")
public class DocTitleController extends CommonController{
	@Resource
	private DocTitleService docTitleService;
	@RequestMapping(value = "/queryDocTitle", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryDocTitle(HttpServletRequest request, DocTitleWithBLOBs docTitle,GeneralCondition gcon){
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
			makeOffsetAndRows(gcon);
			try{
				Token token = (Token)rtnInfo.getData();
				rtnInfo = docTitleService.queryDocTitle(docTitle, token);
			}catch(Exception e){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("查询文书标题失败："+e.getMessage());
				return rtnInfo;
			}
		}
		return rtnInfo;
	}
	@RequestMapping(value = "/createDocTitle", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo createDocTitle(HttpServletRequest request, DocTitleWithBLOBs docTitle,GeneralCondition gcon){
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
			try{
				Token token = (Token)rtnInfo.getData();
				rtnInfo = docTitleService.createDocTitle(docTitle, token);
			}catch(Exception e){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("创建文书标题失败："+e.getMessage());
				return rtnInfo;
			}
		}
		return rtnInfo;
	}
	@RequestMapping(value = "/deleteDocTitle", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo deleteDocTitle(HttpServletRequest request, Integer titleId,GeneralCondition gcon){
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
			try{
				Token token = (Token)rtnInfo.getData();
				rtnInfo =docTitleService.deleteDocTitle(titleId, token);
			}catch(Exception e){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("删除文书标题失败："+e.getMessage());
				return rtnInfo;
			}
		}
		return rtnInfo;
	}
	@RequestMapping(value = "/modifyDocTitle", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo modifyDocTitle(HttpServletRequest request, DocTitleWithBLOBs docTitle, GeneralCondition gcon){
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
			try{
				Token token = (Token)rtnInfo.getData();
				rtnInfo =docTitleService.modifyDocTitle(docTitle, token);
			}catch(Exception e){
				e.printStackTrace();
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("修改标题失败："+e.getMessage());
				return rtnInfo;
			}
		}
		return rtnInfo;
	}
	@RequestMapping(value = "/modifyChecked", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo modifyDocTitleChecked(HttpServletRequest request, DocTitle docTitle, GeneralCondition gcon){
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
			try{
				rtnInfo =docTitleService.modifyDocTitleChecked(docTitle);
			}catch(Exception e){
				e.printStackTrace();
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("修改标题失败："+e.getMessage());
				return rtnInfo;
			}
		}
		return rtnInfo;
	}
}
