package com.yootii.bdy.data.controller;



import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yootii.bdy.common.CommonController;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.Globals;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.trademark.model.Trademark;
import com.yootii.bdy.util.GraspUtil;
import com.yootii.bdy.util.JsonUtil;
import com.yootii.bdy.util.ServiceUrlConfig;
import com.yootii.bdy.data.service.ImportExcelDataService;


@Controller
@RequestMapping("/interface/tmdata")
public class ImportDateController extends CommonController {
	private final Logger logger = Logger.getLogger(this.getClass());
	

	@Resource
	private ImportExcelDataService importExcelDataService;
	@Resource
	private ServiceUrlConfig serviceUrlConfig;
	
	
		
	//从文件导入商品/服务
	@RequestMapping(value = "/importGoods", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object importGoods(GeneralCondition gcon,HttpServletRequest request, String custId) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			
			try {
				rtnInfo = importExcelDataService.importTradeMarkCategoryData(request, custId);
			}
			catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	
	
	
	//从文件导入商标数据
	@RequestMapping(value = "/importTradeMarkData", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object importTradeMarData(GeneralCondition gcon,HttpServletRequest request, String custId) {
		ReturnInfo rtnInfo = this.checkUser(gcon);
		if (rtnInfo != null && rtnInfo.getSuccess()) { // 通过身份验证
			
			try {
				rtnInfo = importExcelDataService.importTradeMarkData(request, custId);
			}
			catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				rtnInfo.setSuccess(false);
				rtnInfo.setMessageType(-2);
				rtnInfo.setMessage(e.getMessage());
				return rtnInfo;
			}
		}	
		return rtnInfo;
	}
	
	
	
	//获取申请人的商标数据
	@RequestMapping(value = "/searchtmdata", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo searchTmData(String appName, GeneralCondition gcon){
		// 返回结果对象
		ReturnInfo returnInfo=new ReturnInfo();	
		
		try {	
			
			String tokenID=gcon.getTokenID();				
			
			String url=serviceUrlConfig.getBdydataUrl()+"/trademark/searchtmdata?appName="+ appName + "&tokenID="+ tokenID;

			String jsonString = GraspUtil.getText(url);
			returnInfo=  JsonUtil.toObject(jsonString, ReturnInfo.class);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询同名商标失败");
			returnInfo.setMessageType(Globals.MESSAGE_TYPE_OPERATION_INVALID);
		} 		
		
		return returnInfo;

	}
	
	
	
	
	//获取官网商标数据更新情况
	@RequestMapping(value = "/tmdataprogress", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo tmDataProgress(String appName, GeneralCondition gcon){
		// 返回结果对象
		ReturnInfo returnInfo=new ReturnInfo();	
		
		try {	
			
			String tokenID=gcon.getTokenID();				
			
			String url=serviceUrlConfig.getBdydataUrl()+"/trademark/tmdataprogress?appName="+ appName + "&tokenID="+ tokenID;

			String jsonString = GraspUtil.getText(url);
			returnInfo=  JsonUtil.toObject(jsonString, ReturnInfo.class);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询同名商标失败");
			returnInfo.setMessageType(Globals.MESSAGE_TYPE_OPERATION_INVALID);
		} 		
		
		return returnInfo;

	}
	
	
	
	
	//获取申请人的商标总数	
	@RequestMapping(value = "/querytmcount", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnInfo queryTmCount(String appName, GeneralCondition gcon){
		// 返回结果对象
		ReturnInfo returnInfo=new ReturnInfo();	
		
		try {	
			
			String tokenID=gcon.getTokenID();				
			
			String url=serviceUrlConfig.getBdydataUrl()+"/trademark/querytmcount?appName="+ appName + "&tokenID="+ tokenID;

			String jsonString = GraspUtil.getText(url);
			returnInfo=  JsonUtil.toObject(jsonString, ReturnInfo.class);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询同名商标失败");
			returnInfo.setMessageType(Globals.MESSAGE_TYPE_OPERATION_INVALID);
		} 		
		
		return returnInfo;

	}
	
	
	
	
	
				
}
