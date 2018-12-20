package com.yootii.bdy.downloadapplicant.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yootii.bdy.common.CommonController;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.downloadapplicant.service.DownloadApplicantService;
import com.yootii.bdy.material.model.Material;
import com.yootii.bdy.material.service.MaterialService;
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.tmcase.model.TradeMarkCase;

@Controller
@RequestMapping("/interface/applicant")
public class DownloadApplicantController extends CommonController{
	
	@Resource
	private DownloadApplicantService downloadApplicantService;
	@Resource
	private MaterialService materialService;
	
	@RequestMapping(value = "/download", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo downloadApplicant(HttpServletRequest request, HttpServletResponse response,Integer caseId,
			String fileName,GeneralCondition gcon,Integer isDownload){
		ReturnInfo rtnInfo = this.checkUser(gcon);
		
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
			try{
				Token token = (Token)rtnInfo.getData();
				rtnInfo = downloadApplicantService.downloadApplicant(request,response,caseId, fileName,token,gcon,isDownload);
			}catch(Exception e){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("下载申请书失败："+e.getMessage());
				return rtnInfo;
			}
		}
		return rtnInfo;
	}
	
	/**
	 *  
	 * @param request
	 * @param response
	 * @param gcon
	 * @param fileName
	 * @param caseId
	 * @return
	 */
	@RequestMapping(value="downLoadDoc",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo downLoadRejectRechickDoc(HttpServletRequest request,HttpServletResponse response,
			GeneralCondition gcon,String fileName,Integer caseId,Integer userId,Integer docTypeId,Integer flag){
		ReturnInfo returnInfo = this.checkUser(gcon);
		if(caseId ==null || userId == null){
			returnInfo.setSuccess(false);
			returnInfo.setMessage("参数不能为空");
			return returnInfo;
		}
		if(returnInfo !=null && returnInfo.getSuccess()){
			try{
				Token token = (Token)returnInfo.getData();
				returnInfo = downloadApplicantService.downLoadRejectRechickDoc(request, response, caseId, fileName, token,gcon,userId,docTypeId,flag);
				returnInfo.setSuccess(true);
				returnInfo.setMessage("下载文件成功！");
			}catch (Exception e) {
				e.printStackTrace();
				returnInfo.setSuccess(false);
				returnInfo.setMessage("下载申请书失败："+e.getMessage());
			}
		}
		return returnInfo;
	}
	
	
	//生成委托书
	@RequestMapping(value = "createProxyPicture", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo createProxyPicture(HttpServletRequest request, HttpServletResponse response,Integer caseId,
			String fileName,GeneralCondition gcon,String appCnName,String appEnName,
			String country,String agenName,String tmName,String goodClass){
		ReturnInfo rtnInfo = this.checkUser(gcon);
		//判断参数
		if(appCnName == null || appCnName.equals("")){
			rtnInfo.setMessage("申请人中文名称不能为空");
			rtnInfo.setSuccess(false);
			return rtnInfo;
		}
		if(appEnName == null || appEnName.equals("")){
			rtnInfo.setMessage("申请人英文名称不能为空");
			rtnInfo.setSuccess(false);
			return rtnInfo;
		}
		if(country ==null ||country.equals("")){
			rtnInfo.setMessage("申请人国籍不能为空");
			rtnInfo.setSuccess(false);
			return rtnInfo;
		}
		if( agenName.equals("") || agenName == null  ){
			rtnInfo.setMessage("代理机构不能为空");
			rtnInfo.setSuccess(false);
			return rtnInfo;
		}
		if(goodClass == null || goodClass.equals("")){
			rtnInfo.setMessage("类别不能为空");
			rtnInfo.setSuccess(false);
			return rtnInfo;		
		}
		if(tmName == null || tmName.equals("")){
			rtnInfo.setMessage("商标名称不能为空");
			rtnInfo.setSuccess(false);
			return rtnInfo;
		}
		if(rtnInfo != null && rtnInfo.getSuccess()){//通过身份验证
			try{
				Token token = (Token)rtnInfo.getData();
				rtnInfo = downloadApplicantService.createProxyPicture(request,response,caseId,fileName,token,gcon,
						appCnName,appEnName,country,agenName,tmName,goodClass);
			}catch(Exception e){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("下载申请书失败："+e.getMessage());
				return rtnInfo;
			}
		}
		return rtnInfo;
	}
	
	
	
	
	
	/**
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value="upLoad",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo upLoad(HttpServletRequest request,Material material,
			Integer custId,Integer applicantId,String fileName,String creater,
			TradeMarkCase tradeMarkCase,Integer caseId,GeneralCondition gcon){
		
		ReturnInfo returnInfo = this.checkUser(gcon);
		if(custId ==null){
			returnInfo.setSuccess(false);
			returnInfo.setMessage("参数不能为空");
			return returnInfo;
		}
		if(returnInfo !=null && returnInfo.getSuccess()){
			try{
				returnInfo = downloadApplicantService.upload(request, fileName, custId, applicantId, creater, caseId);
				returnInfo.setSuccess(true);
				returnInfo.setMessage("上传成功！");
			}catch (Exception e) {
				returnInfo.setSuccess(false);
				returnInfo.setMessage("上传失败！");
				e.printStackTrace();
			}
		}
		return returnInfo;
	}
	
	
	
	
	
	
	
	
}
