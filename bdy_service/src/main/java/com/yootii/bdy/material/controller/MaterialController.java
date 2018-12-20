package com.yootii.bdy.material.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yootii.bdy.common.CommonController;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.material.model.Material;
import com.yootii.bdy.material.model.MaterialSort;
import com.yootii.bdy.material.service.MaterialService;
import com.yootii.bdy.material.service.MaterialSortService;
import com.yootii.bdy.security.model.Token;

@Controller
@RequestMapping("/interface/material")
public class MaterialController extends CommonController {
	
	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private MaterialService materialService;
	@Resource
	private MaterialSortService materialsortService;
	@RequestMapping(value = "/creatematerial", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo createMaterial(HttpServletRequest request,Material material,GeneralCondition gcon){
		Token token=null;
		ReturnInfo info = this.checkUser(gcon);
		if (info != null && info.getSuccess()) { // 通过身份验证
			token = (Token)info.getData();
			this.addToken(token);//在线程中加入thread local参数token.
			this.addURL(request.getRequestURI());
			try{
				info = materialService.createMaterial(request,material, gcon,token);
			}catch(Exception e){
				logger.error(e.getMessage());
				info.setSuccess(false);
				info.setMessage("创建资料失败");
				info.setMessageType(-2);
				return info;
			}
		}
		return info;
	}
	/**
	 * 只新建material,不上传文件
	 * @param request
	 * @param material
	 * @param gcon
	 * @return
	 */
	@RequestMapping(value = "/creatematerialsimple", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo createMaterialSimple(HttpServletRequest request, Integer materialId, String caseId,  String caseIdList, String custId, String applicantId,  String agencyId, GeneralCondition gcon,boolean precase){
		Token token=null;
		ReturnInfo info = this.checkUser(gcon);
		if (info == null || !info.getSuccess()) { // 通过身份验证			
			return info;			
		}
		
		token = (Token)info.getData();
		this.addToken(token);//在线程中加入thread local参数token.
		this.addURL(request.getRequestURI());
		
		
		Material material =new Material();
		material.setMaterialId(materialId);
		
		if(custId !=null  &&  custId.matches("[0-9]+")) {
			material.setCustId(Integer.parseInt(custId));
		}
				
		if(applicantId !=null  &&  applicantId.matches("[0-9]+")) {
			material.setApplicantId(Integer.parseInt(applicantId));
		}
		
		if (caseIdList==null || caseIdList.equals("")){
			caseIdList=caseId;
		}
		
		if(custId==null  ||  !custId.matches("[0-9]+") && (agencyId==null || !agencyId.matches("[0-9]+"))) {
			if (caseIdList==null || caseIdList.equals("")){
				info.setSuccess(false);
				info.setMessage("参数caseId/custId/agencyId不正确");
				return info;				
			}
		}
		
		
		
		if (caseIdList==null || caseIdList.equals("")){
			if(custId ==null  ||  !custId.matches("[0-9]+") && (agencyId==null || !agencyId.matches("[0-9]+"))) {
				info.setSuccess(false);
				info.setMessage("参数caseId/custId/agencyId不正确");
				return info;	
			}
		}
		
		
		if(custId !=null  &&  custId.matches("[0-9]+") &&  agencyId!=null && agencyId.matches("[0-9]+")) {			
			try{
				Integer aId=new Integer(agencyId);					
				info = materialService.createMaterialSimple(material, aId, gcon,precase,token);
				if (info == null || !info.getSuccess()) { 	
					return info;			
				}
				
				//Modification start, by yang guang, 2018-10-16
				//返回文件的地址
				material = (Material) info.getData();	
				String address= material.getAddress();
				Map<String, String> data = new HashMap<String, String>();
				if(address!=null){
					data.put("address", address);
				}
				info.setData(data);
				//Modification end
				
				return info;		
			}catch(Exception e){
				logger.error(e.getMessage());
				info.setSuccess(false);
				info.setMessage("创建资料失败");
				info.setMessageType(-2);
				return info;
			}
		}
		
		if (caseIdList!=null && !caseIdList.equals("")){			
			StringTokenizer idtok = new StringTokenizer(caseIdList, ",");
			int count=0;
			int errCount=0;
			String msg=null;
			
			String address= null;
			
			while (idtok.hasMoreTokens()) {
				String value = idtok.nextToken();
				String id = value.trim();
				Integer cId = Integer.parseInt(id);
				material.setCaseId(cId);
				
				try{
					info = materialService.createMaterialSimple(material, null, gcon,precase,token);
					if (info == null || !info.getSuccess()){					
						if (msg==null){
							msg="案件"+cId.toString()+"的文件上传失败：" + info.getMessage();
						}else{
							msg=msg+";"+"案件"+cId.toString()+"的文件上传失败：" + info.getMessage();
						}
						
						errCount++;
					}else{
						
						//Modification start, by yang guang, 2018-10-16
						//返回文件的地址						
						if (address==null){
							material = (Material) info.getData();	
							address= material.getAddress();	
						}
						//Modification end
					}
				}catch(Exception e){
					logger.error(e.getMessage());
					info.setSuccess(false);
					info.setMessage("创建资料失败");
					info.setMessageType(-2);
					return info;
				}
				
				count++;
			}
			
			if (msg==null){
				msg="案件文件上传成功";	
			}
			info.setMessage(msg);	
			if (count>1){
				if(errCount==count){//如果全部失败，那么设置 success为false
					info.setSuccess(false);		
				}else{
					info.setSuccess(true);	
					//Modification start, by yang guang, 2018-10-16
					//返回文件的地址	
					Map<String, String> data = new HashMap<String, String>();
					if(address!=null){
						data.put("address", address);
					}
					info.setData(data);
					//Modification end
				}
			}	
		}
		
		return info;
	}

	

	@RequestMapping(value = "/modifymaterial", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo modifyMaterial(HttpServletRequest request,Material material,GeneralCondition gcon){
		Token token=null;
		ReturnInfo info = this.checkUser(gcon);
		if (info != null && info.getSuccess()) { // 通过身份验证
			try{
				token=(Token)info.getData();
				this.addToken(token);//在线程中加入thread local参数token.
				this.addURL(request.getRequestURI());
				info = materialService.modifyMaterial(request,material, gcon, token);
			}catch(Exception e){
				logger.error(e.getMessage());
				info.setSuccess(false);
				info.setMessage("修改资料失败");
				info.setMessageType(-2);
				return info;
			}
		}
		return info;
	}		


	@RequestMapping(value = "/deletematerial", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo deleteMaterial(HttpServletRequest request, Material material,GeneralCondition gcon){
		Token token=null;
		ReturnInfo info = this.checkUser(gcon);
		if (info != null && info.getSuccess()) { // 通过身份验证
			token=(Token)info.getData();
			this.addToken(token);//在线程中加入thread local参数token.
			this.addURL(request.getRequestURI());
			try{
				info = materialService.deleteMaterial(material, gcon,token);
			}catch(Exception e){
				logger.error(e.getMessage());
				info.setSuccess(false);
				info.setMessage("删除资料失败");
				info.setMessageType(-2);
				return info;
			}
		}
		return info;
	}		


	@RequestMapping(value = "/querymaterial", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryMaterial(HttpServletRequest request, Material material,GeneralCondition gcon){
		Token token=null;
		ReturnInfo info = this.checkUser(gcon);

		if (info != null && info.getSuccess()) { // 通过身份验证
			token=(Token)info.getData();
			makeOffsetAndRows(gcon);
			try{
				
				info = materialService.findMaterial(material, gcon,token);
				
			}catch(Exception e){
				logger.error(e.getMessage());
				info.setSuccess(false);
				e.printStackTrace();
				info.setMessage("查询资料失败");
				info.setMessageType(-2);
			}
		}
		return info;
	}	
	
	@RequestMapping(value = "/downloadfile", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object downloadFile(HttpServletRequest request,HttpServletResponse response, Material material,GeneralCondition gcon){
		Token token=null;
		ReturnInfo info = this.checkUser(gcon);

		if (info != null && info.getSuccess()) { // 通过身份验证
			token=(Token)info.getData();
			try{
				Object obj = materialService.downloadFile(request, response, material, gcon,token);
				if (obj instanceof ReturnInfo ) { 
					  info= (ReturnInfo) obj;
					
					Map<String,String> map = new HashMap<String,String>();
					map.put("下载失败", info.getMessage());
					return map;
				} else return obj;	
				
			}catch(Exception e){
				logger.error(e.getMessage());
				info.setSuccess(false);
				e.printStackTrace();
				info.setMessage("查询资料失败");
				info.setMessageType(-2);
				return info;
			}
		}
		return info;
	}	

	@RequestMapping(value = "/uploadfile", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object uploadFile(HttpServletRequest request,HttpServletResponse response, Material material,GeneralCondition gcon){
		Token token=null;
		ReturnInfo info = this.checkUser(gcon);

		if (info != null && info.getSuccess()) { // 通过身份验证
			token=(Token)info.getData();
			try{
				info = materialService.uploadFile(request, material, gcon,token);			
			}catch(Exception e){
				logger.error(e.getMessage());
				info.setSuccess(false);
				info.setMessage("上传附件失败");
				info.setMessageType(-2);
				return info;
			}
		}
		return info;
	}	
	
	@RequestMapping(value = "/deletefile", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object deleteFile(HttpServletRequest request, Material material,GeneralCondition gcon){
		Token token=null;
		ReturnInfo info = this.checkUser(gcon);
		if (info != null && info.getSuccess()) { // 通过身份验证
			token=(Token)info.getData();
			this.addToken(token);//在线程中加入thread local参数token.
			this.addURL(request.getRequestURI());
			try{
				info = materialService.deleteFile(request, material, gcon,token);			
			}catch(Exception e){
				logger.error(e.getMessage());
				info.setSuccess(false);
				info.setMessage("删除附件失败");
				info.setMessageType(-2);
				return info;
			}
		}
		return info;
	}	
	@RequestMapping(value = "/querymaterialsort", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryMaterialSort(HttpServletRequest request, MaterialSort materialSort,GeneralCondition gcon){
		Token token=null;
		ReturnInfo info = this.checkUser(gcon);

		if (info != null && info.getSuccess()) { // 通过身份验证
			token=(Token)info.getData();
			makeOffsetAndRows(gcon);
			try{
				info = materialsortService.queryMaterialSort(materialSort, gcon);
			}catch(Exception e){
				logger.error(e.getMessage());
				info.setSuccess(false);
				e.printStackTrace();
				info.setMessage("查询资料类型失败");
				info.setMessageType(-2);
				return info;
			}
		}
		return info;
	}	
	@RequestMapping(value = "/creatematerialsort", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo createMaterialSort(HttpServletRequest request, MaterialSort materialSort,GeneralCondition gcon){
		Token token=null;
		ReturnInfo info = this.checkUser(gcon);

		if (info != null && info.getSuccess()) { // 通过身份验证
			token=(Token)info.getData();
			this.addToken(token);//在线程中加入thread local参数token.
			this.addURL(request.getRequestURI());
			makeOffsetAndRows(gcon);
			try{
				info = materialsortService.createMaterialSort(materialSort, gcon);
			}catch(Exception e){
				logger.error(e.getMessage());
				info.setSuccess(false);
				e.printStackTrace();
				info.setMessage("创建资料类型失败");
				info.setMessageType(-2);
				return info;
			}
		}
		return info;
	}	
	@RequestMapping(value = "/modifymaterialsort", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo modifyMaterialSort(HttpServletRequest request, MaterialSort materialSort,GeneralCondition gcon){
		Token token=null;
		ReturnInfo info = this.checkUser(gcon);

		if (info != null && info.getSuccess()) { // 通过身份验证
			token=(Token)info.getData();
			this.addToken(token);//在线程中加入thread local参数token.
			this.addURL(request.getRequestURI());
			makeOffsetAndRows(gcon);
			try{
				info = materialsortService.modifyMaterialSort(materialSort, gcon);
			}catch(Exception e){
				logger.error(e.getMessage());
				info.setSuccess(false);
				e.printStackTrace();
				info.setMessage("修改资料类型失败");
				info.setMessageType(-2);
				return info;
			}
		}
		return info;
	}	
	@RequestMapping(value = "/deletematerialsort", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo deleteMaterialSort(HttpServletRequest request, MaterialSort materialSort,GeneralCondition gcon){
		Token token=null;
		ReturnInfo info = this.checkUser(gcon);

		if (info != null && info.getSuccess()) { // 通过身份验证
			token=(Token)info.getData();
			this.addToken(token);//在线程中加入thread local参数token.
			this.addURL(request.getRequestURI());
			makeOffsetAndRows(gcon);
			try{
				info = materialsortService.deleteMaterialSort(materialSort, gcon);
			}catch(Exception e){
				logger.error(e.getMessage());
				info.setSuccess(false);
				e.printStackTrace();
				info.setMessage("删除资料类型失败");
				info.setMessageType(-2);
				return info;
			}
		}
		return info;
	}	
	@RequestMapping(value = "/querymaterialbyapp", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryMaterialByApp(HttpServletRequest request, Material material,GeneralCondition gcon,Integer applicantId){
		Token token=null;
		ReturnInfo info = this.checkUser(gcon);

		if (info != null && info.getSuccess()) { // 通过身份验证
			token=(Token)info.getData();
			makeOffsetAndRows(gcon);
			try{
				info = materialService.queryMaterialByApp(material, gcon,token);
			}catch(Exception e){
				logger.error(e.getMessage());
				info.setSuccess(false);
				e.printStackTrace();
				info.setMessage("查询资料失败");
				info.setMessageType(-2);
				return info;
			}
		}
		return info;
	}	
	@RequestMapping(value = "/querymaterialbytmId", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryMaterialByTmId(HttpServletRequest request, Material material,GeneralCondition gcon,Integer tmId){
		Token token=null;
		ReturnInfo info = this.checkUser(gcon);

		if (info != null && info.getSuccess()) { // 通过身份验证
			token=(Token)info.getData();
			makeOffsetAndRows(gcon);
			try{
				info = materialService.queryMaterialByTmId(material, gcon,tmId);
			}catch(Exception e){
				logger.error(e.getMessage());
				info.setSuccess(false);
				e.printStackTrace();
				info.setMessage("查询资料失败");
				info.setMessageType(-2);
				return info;
			}
		}
		return info;
	}	
	@RequestMapping(value = "/querymaterialbyregnumber", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo querymaterialbyregnumber(HttpServletRequest request, Material material,GeneralCondition gcon,String regNumber){
		Token token=null;
		ReturnInfo info = this.checkUser(gcon);

		if (info != null && info.getSuccess()) { // 通过身份验证
			token=(Token)info.getData();
			makeOffsetAndRows(gcon);
			try{
				info = materialService.queryMaterialByRegNumber(material, gcon,regNumber);
			}catch(Exception e){
				logger.error(e.getMessage());
				info.setSuccess(false);
				e.printStackTrace();
				info.setMessage("查询资料失败");
				info.setMessageType(-2);
				return info;
			}
		}
		return info;
	}	
	/*
	 * 返回每种文件类型修改时间最新的文件
	 */
	@RequestMapping(value = "/querymaterialbyappname", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo querymaterialbyappname(HttpServletRequest request, String appCnName,String appEnName,GeneralCondition gcon){
		Token token=null;
		ReturnInfo info = this.checkUser(gcon);

		if (info != null && info.getSuccess()) { // 通过身份验证
			token=(Token)info.getData();
			makeOffsetAndRows(gcon);
			try{
				info = materialService.queryMaterialByAppName( appCnName,appEnName, gcon,token.getTokenID());
			}catch(Exception e){
				logger.error(e.getMessage());
				info.setSuccess(false);
				e.printStackTrace();
				info.setMessage("查询资料失败");
				info.setMessageType(-2);
				return info;
			}
		}
		return info;
	}	
	/**
	 * 参数为案件编号，caseId）返回该案件最新录入的官文的文件名称，以及该文件的链接地址。
	 * @param request
	 * @param appCnName
	 * @param appEnName
	 * @param gcon
	 * @return
	 */
	@RequestMapping(value = "/querymaterialbycaseidlast", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryMaterialByCaseIdLast(Integer caseId,GeneralCondition gcon){
		ReturnInfo info = this.checkUser(gcon);
		if (info != null && info.getSuccess()) { // 通过身份验证
			try{
				info = materialService.queryMaterialByCaseIdLast(caseId);
			}catch(Exception e){
				logger.error(e.getMessage());
				info.setSuccess(false);
				e.printStackTrace();
				info.setMessage("查询资料失败");
				info.setMessageType(-2);
				return info;
			}
		}
		return info;
	}	
}
