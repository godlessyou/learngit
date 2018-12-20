package com.yootii.bdy.tmcase.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yootii.bdy.common.CommonController;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.material.model.Material;
import com.yootii.bdy.material.service.MaterialService;
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.security.service.AuthenticationService;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.TradeMarkCaseFile;
import com.yootii.bdy.tmcase.service.TradeMarkCaseFileService;
import com.yootii.bdy.tmcase.service.TradeMarkCaseService;

@Controller
@RequestMapping("/interface/tmcasefile")
public class TradeMarkCaseFileController extends CommonController{
	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private TradeMarkCaseFileService tradeMarkCaseFileService;
	
	@Resource
	private MaterialService materialService;
	
	
	@RequestMapping(value = "/uploadcasefile", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object uploadCaseFile(HttpServletRequest request, String caseIdList, TradeMarkCaseFile tradeMarkCaseFile,GeneralCondition gcon,Integer applicantId) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if(rtnInfo==null || !rtnInfo.getSuccess()){
			return rtnInfo;		
		}
		
		Token token = (Token)rtnInfo.getData();
		this.addToken(token);//审计日志需要用到token信息
		this.addURL(request.getRequestURI());
		
		if (tradeMarkCaseFile==null){
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("缺少参数");
			return rtnInfo;		
		}
		
		Integer caseId=tradeMarkCaseFile.getCaseId();
		
		if (caseId==null && (caseIdList==null || caseIdList.equals(""))){
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("案件编号不能为空");
			return rtnInfo;				
		}
		
		if (caseIdList==null ||  caseIdList.equals("")){
			caseIdList=caseId.toString();
		}
					
		StringTokenizer idtok = new StringTokenizer(caseIdList, ",");
		int count=1;		
		Material material =null;
		String msg=null;
		String address=null;
		
		while (idtok.hasMoreTokens()) {
			String value = idtok.nextToken();
			String id = value.trim();
			Integer cId = Integer.parseInt(id);
			
			tradeMarkCaseFile.setCaseId(cId);
			
			try{
				//针对第一个案件，新建material,并且上传文件
				if (count==1){	
					rtnInfo = tradeMarkCaseFileService.uploadCaseFile(request,cId,tradeMarkCaseFile, gcon, token,applicantId);
					if (rtnInfo == null || !rtnInfo.getSuccess()){	
						//如果针对第一个案子的文件上传已经失败
						//那么就无法从返回结果rtnInfo.getData()中获取到Material
						//因此，结束对案件的处理。
						return rtnInfo;
					}
					else{	
						 material = (Material) rtnInfo.getData();
						 
						 //Modification start, by yang guang, 2018-10-16
						 //返回文件的地址
						 address= material.getAddress();	
						 //Modification end
					}
				}	
				//从第二个案件开始，只新建material,不上传文件	
				else{		
					if (material!=null){
						material.setCaseId(cId);		
						rtnInfo = materialService.createMaterialSimple(material, null, gcon,false,token);
						if (rtnInfo == null || !rtnInfo.getSuccess()){					
							if (msg==null){
								msg="案件"+cId.toString()+"的文件上传失败：" + rtnInfo.getMessage();
							}else{
								msg=msg+";"+"案件"+cId.toString()+"的文件上传失败：" + rtnInfo.getMessage();
							}	
						}
					}
				}

				count++;
				
			}catch(Exception e){
				e.printStackTrace();
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
				
		}	
				
		if (msg==null){
			msg="案件文件上传成功";	
		}
		rtnInfo.setMessage(msg);	
		rtnInfo.setSuccess(true);
		
		//Modification start, by yang guang, 2018-10-16
		//返回文件的地址
		Map<String, String> data = new HashMap<String, String>();
		if(address!=null){
			data.put("address", address);
		}
		rtnInfo.setData(data);
		//Modification end
		
		return rtnInfo;
	}
	
	
	
	@RequestMapping(value = "/querycasefile", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object queryCaseFile(HttpServletRequest request,TradeMarkCaseFile tradeMarkCaseFile,String status ,GeneralCondition gcon) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if(rtnInfo!=null&&rtnInfo.getSuccess()){
			Token token = (Token)rtnInfo.getData();
			this.addToken(token);//审计日志需要用到token信息
			this.addURL(request.getRequestURI());
			try{
				rtnInfo = tradeMarkCaseFileService.queryCaseFile(tradeMarkCaseFile, status);
			}catch(Exception e){
				e.printStackTrace();
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}
		return rtnInfo;
	}
}
