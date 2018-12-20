package com.yootii.bdy.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yootii.bdy.customer.model.Applicant;
import com.yootii.bdy.data.model.ImportResult;
import com.yootii.bdy.trademark.model.Trademark;
import com.yootii.bdy.trademark.model.TrademarkProcess;



public class BasicCaseDataService {

	public static Logger logger = Logger.getLogger(BasicCaseDataService.class);

	private Map<Integer, List<ExcelRow>> sheetList = null;
	

	
	// 处理从excel表格中的商标案件数据，规则如下：
	// 1、对于数据库中不存在的商标注册号，商品服务类别，打印提示信息	
	public List<Trademark> processTradeMarkData(List<Object> tradeMarkList, ImportResult importResult) throws Exception {

		List<Trademark> tmList = new ArrayList<Trademark>();
		
		CheckDataService checkDataService = new CheckDataService();
		for (Object obj : tradeMarkList) {
			Trademark trademark = (Trademark) obj;
			
			boolean dataCorrect = checkDataService.checkTradMarkData(
					importResult, trademark);
			
			if(!dataCorrect){
				continue;
			}
		
			tmList.add(trademark);
		}

		return tmList;

	}
	
	
	
	
	// 处理从excel表格中的商标案件数据，规则如下：
	// 1、对于数据库中不存在的商标注册号，商品服务类别，打印提示信息	
	public List<TrademarkProcess> processTradeMarkProcessData(List<Object> tradeMarkList, ImportResult importResult) throws Exception {

		List<TrademarkProcess> tmList = new ArrayList<TrademarkProcess>();
		
		CheckDataService checkDataService = new CheckDataService();
		for (Object obj : tradeMarkList) {
			TrademarkProcess trademark = (TrademarkProcess) obj;
			
			boolean dataCorrect = checkDataService.checkTradMarkProcessData(
					importResult, trademark);
			
			if(!dataCorrect){
				continue;
			}
		
			tmList.add(trademark);
		}

		return tmList;

	}
	
	
	
	// 处理从excel表格中的申请人数据	
	public List<Applicant> processApplicantData(List<Object> list, ImportResult importResult) throws Exception {

		List<Applicant> appList = new ArrayList<Applicant>();
		
		CheckDataService checkDataService = new CheckDataService();
		for (Object obj : list) {
			Applicant applicant = (Applicant) obj;
			
			boolean dataCorrect = checkDataService.checkApplicantData(
					importResult, applicant);
			
			if(!dataCorrect){
				continue;
			}
		
			appList.add(applicant);
		}

		return appList;

	}
	
		

	// 提取excel表格中的数据形成对象
	private List<Object> getData(Map<Integer, List<ExcelRow>> sheetList,List<ExcelRow> excelContent,
			Map<Integer, String> propertyNames, String className, String imagePath)
			throws Exception {

		
		List<Object> list = new ArrayList<Object>();	
		if (sheetList!=null){
			list = ExcelUtil.getCellData(sheetList, propertyNames,
					className, imagePath);
		}else{
			ExcelUtil.getObjectFromCell(excelContent, propertyNames, className, imagePath, list);
		}

		return list;

	}
	
	

	// 提取excel表格中的案件数据
	private List<Object> getDataFromExcel(
			Map<Integer, List<ExcelRow>> sheetList,
			List<ExcelRow> excelContent, String className,
			Map<String, String> map, String imagePath) throws Exception {

		// 获取excel表中的案件的固定属性的数据
		Map<Integer, String> propertyNames = ExcelTool.getProperNameCol(
				excelContent, map);

		// 提取excel表格数据，形成商标数据	
		List<Object> list  = getData(sheetList, excelContent, propertyNames,
					className, imagePath);

		return list;
	}
	
	
	

	

	
	
	public List<Object> getDataFromFile(String filePath, String className,
			Map<String, String> map) throws Exception {

		List<Object> dataList = new ArrayList<Object>();

		// 读取excel文件
		File excelFile = new File(filePath);
		boolean readAllSheet = true;

		if (sheetList == null) {
			sheetList = ExcelUtil.readExcel(excelFile, readAllSheet);
		}
		
		String imagePath = null;
		
		int size = sheetList.size();
		for (int i = 0; i < size; i++) {
			
			List<ExcelRow> excelContent = sheetList.get(i);
			
			// 提取excel表格数据，形成案件数据
			List<Object> list = getDataFromExcel(sheetList, excelContent,
					className, map, imagePath);

			dataList.addAll(list);
						
		}

		return dataList;

	}
	
		
	
	

	public List<Object> getDataFromSheet(List<ExcelRow> excelContent, String className,
			Map<String, String> map) throws Exception {

		List<Object> dataList = new ArrayList<Object>();
				
		String imagePath = null;
		
//		String tempPath = filePath;
//		if (tempPath.indexOf("\\")>-1){
//			tempPath=tempPath.replace("\\", "/");
//		}
//		int pos = tempPath.lastIndexOf("/");
//		if (pos > -1) {
//			tempPath = tempPath.substring(0, pos);
//		}
//		imagePath = tempPath + "/" + "newimages";
//		FileUtil.createFolderIfNotExists(imagePath);

		// 将excel文件中的图片保存到临时目录，从申请号所在列获取申请号，作为文件名
		// 如果申请号不存在，那么取sheet号+"_"+行号作为文件名
//			ExcelUtil.getSheetPictrues2007(filePath, imagePath, -1,i);
		
		// 提取excel表格数据，形成案件数据
		List<Object> list = getDataFromExcel(null, excelContent,
				className, map, imagePath);

		dataList.addAll(list);
		

		return dataList;

	}
	
	



	public static void main(String[] args) {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
