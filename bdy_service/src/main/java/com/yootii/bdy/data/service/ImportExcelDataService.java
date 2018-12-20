package com.yootii.bdy.data.service;

import javax.servlet.http.HttpServletRequest;

import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.trademark.model.Trademark;




public interface ImportExcelDataService {	
	

	//导入excel中的商品/服务数据
	public ReturnInfo importTradeMarkData(HttpServletRequest request, String custId);
	
	//导入excel中的申请人数据
	public ReturnInfo importApplicantData(HttpServletRequest request,  String custId);	
	
	//导入excel中的商品/服务数据
	public ReturnInfo importTradeMarkCategoryData(HttpServletRequest request,  String custId);
	

	public void setTestFilePath(String testFilePath);
	
	public ReturnInfo importTmData(String filePath, String custId);
	
	public ReturnInfo importAppData(String filePath, String custId);
	
	
	public ReturnInfo processMultiTmTypeData();
	
	
}
