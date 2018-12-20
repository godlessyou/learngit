package com.yootii.bdy.data.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.yootii.bdy.util.CheckDataService;
import com.yootii.bdy.util.ExcelRow;
import com.yootii.bdy.util.ExcelUtil;
import com.yootii.bdy.util.ExcuteTask;
import com.yootii.bdy.util.ExecutorProcessPool;
import com.yootii.bdy.common.ExcelCells;
import com.yootii.bdy.common.Globals;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.data.model.ImportResult;
import com.yootii.bdy.data.model.Reason;
import com.yootii.bdy.tmcase.model.TmCategory;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.trademark.model.Trademark;
import com.yootii.bdy.trademark.model.TrademarkProcess;
import com.yootii.bdy.trademark.service.TradeMarkService;
import com.yootii.bdy.util.BasicCaseDataService;
import com.yootii.bdy.common.Constants;
import com.yootii.bdy.customer.model.Applicant;
import com.yootii.bdy.data.service.ImportExcelDataService;
import com.yootii.bdy.trademark.dao.TrademarkMapper;

@Service
public class ImportExcelDataServiceImpl implements ImportExcelDataService {

	private final Logger logger = Logger.getLogger(this.getClass());

	private static String trademarkcase_uploadDir = Constants.upload_dir
			+ "/trademarkcase";
	
	private static String trademark_uploadDir = Constants.upload_dir
			+ "/trademark";
	
	private static String applicant_uploadDir = Constants.upload_dir
			+ "/applicant";

	private String testFilePath = null;
	
	@Resource
	private TrademarkMapper trademarkMapper;
	
	@Resource
	private TmDataProcessor tmDataProcessor;
	
	
	@Resource
	private TradeMarkService tradeMarkService;
	
	private final ExecutorProcessPool pool = ExecutorProcessPool.getInstance();
	
	

	// 导入商品服务数据
	private ReturnInfo importCategoryData(String filePath) throws Exception {

		ReturnInfo returnInfo = new ReturnInfo();

		ImportResult caseImportResult1 = new ImportResult();

		// 获取excel表中的案件的基本属性的数据
		String className = "com.yootii.bdy.tmcase.model.TmCategory";

		BasicCaseDataService basicCaseDataService = new BasicCaseDataService();

		// 获取基本属性的数据
		List<Object> tradeMarkCategoryDataList = basicCaseDataService
				.getDataFromFile(filePath, className, ExcelCells.CategoryData);

		CheckDataService checkDataService = new CheckDataService();

		String data = null;
		for (Object tmCategoryObj : tradeMarkCategoryDataList) {

			TmCategory tmCategory = (TmCategory) tmCategoryObj;

			// 检查数据的正确性
			boolean dataCorrect = checkDataService.checkTmCategoryData(
					caseImportResult1, tmCategory);

			// 如果数据不正确，就返回
			if (!dataCorrect) {
				returnInfo.setSuccess(false);
				returnInfo.setMessage(caseImportResult1.getMessage());
				return returnInfo;
			}

			String goodDesc = tmCategory.getClasses() + ","
					+ tmCategory.getGoods();
			if (data == null) {
				data = goodDesc;
			} else {
				data = data + "|" + goodDesc;
			}

		}

		Long total = (long) 1;

		returnInfo.setSuccess(true);
		returnInfo.setData(data);
		returnInfo.setTotal(total);
		returnInfo.setMessage("数据导入成功");
		
		return returnInfo;

	}
	
	
	
	private Map<Integer, List<ExcelRow>> getExcelContent(String filePath) throws Exception {

		Map<Integer, List<ExcelRow>> sheetList=null;
		// 读取excel文件
		File excelFile = new File(filePath);
		boolean readAllSheet = true;

		if (sheetList == null) {
			sheetList = ExcelUtil.readExcel(excelFile, readAllSheet);
		}
		
		return  sheetList;

	}
	
	
	
	
	// 导入商品服务数据
	public ReturnInfo importTmData(String filePath, String custId) {

		ReturnInfo returnInfo = new ReturnInfo();
		Long total =(long)0;
		
		try{

			
			BasicCaseDataService basicCaseDataService = new BasicCaseDataService();
			ImportResult importResult = new ImportResult();			
			List<Reason> reasons =new ArrayList<Reason>();
			importResult.setReasons(reasons);
			
			File file=new File(filePath);
			if (!file.exists()){				
				returnInfo.setSuccess(false);				
				returnInfo.setMessage("excel file is not exist.");				
				return returnInfo;				
			}
			
			// 获取基本属性的数据
			Map<Integer, List<ExcelRow>> sheetList= getExcelContent(filePath);
			
			int size=sheetList.size();
			
			if (sheetList!=null && size>0){
				// 获取excel表中商标数据
				String className = "com.yootii.bdy.trademark.model.Trademark";
				
				List<ExcelRow> excelContent = sheetList.get(0);
				List<Object> tradeMarkList=basicCaseDataService.getDataFromSheet(excelContent, className, ExcelCells.GuoNeiTm);
				List<Trademark> tmList = basicCaseDataService.processTradeMarkData(tradeMarkList, importResult);
				
				Integer cId=null;
				if (custId!=null){
					cId=new Integer(custId);
				}
				if (tmList!=null){
					tmDataProcessor.processTradeMark(cId, tmList);			
					int count=tmList.size();				
					total = (long) count;
				}
				
				
				
				
				//获取excel表中商标流程数据
				className = "com.yootii.bdy.trademark.model.TrademarkProcess";
				
				List<ExcelRow> excelContent2 = sheetList.get(1);		
				List<Object> tradeMarkProcessList=basicCaseDataService.getDataFromSheet(excelContent2, className, ExcelCells.GuoNeiTmProcess);
				List<TrademarkProcess> tmProcessList = basicCaseDataService.processTradeMarkProcessData(tradeMarkProcessList, importResult);
				
				if (tmProcessList!=null){
					tmDataProcessor.processTradeMarkProcess(tmProcessList);	
				}
			}
						
	
			returnInfo.setSuccess(true);
			
			String msg=importResult.getMessage();
			if (msg!=null){
				returnInfo.setMessage(msg);
			}else{
				returnInfo.setMessage("数据导入成功");
			}
				
			returnInfo.setTotal(total);
		}catch(Exception e){
			returnInfo.setSuccess(false);
			returnInfo.setMessage("数据导入失败");
			e.printStackTrace();
		}
		
		return returnInfo;

	}
	
	
	// 导入商品服务数据
	public ReturnInfo importAppData(String filePath, String custId) {

		ReturnInfo returnInfo = new ReturnInfo();
		Long total =(long)0;
		
		try{

			// 获取excel表中的案件的基本属性的数据
			String className = "com.yootii.bdy.customer.model.Applicant";
	
			BasicCaseDataService basicCaseDataService = new BasicCaseDataService();
			ImportResult importResult = new ImportResult();
			List<Reason> reasons =new ArrayList<Reason>();
			importResult.setReasons(reasons);
			
			File file=new File(filePath);
			if (!file.exists()){				
				returnInfo.setSuccess(false);				
				returnInfo.setMessage("excel file is not exist.");				
				return returnInfo;				
			}
			
			// 获取基本属性的数据
			List<Object> list = basicCaseDataService.getDataFromFile(
					filePath, className, ExcelCells.ApplicantData);
	
			List<Applicant> appList = basicCaseDataService.processApplicantData(list, importResult);
			
			if (appList!=null){
				Integer cId=null;
				if (custId!=null){
					cId=new Integer(custId);
				}
				
				tmDataProcessor.processApplicant(cId, appList);		
				int count=appList.size();				
				total = (long) count;
			}
						
	
			returnInfo.setSuccess(true);
			
			String msg=importResult.getMessage();
			if (msg!=null){
				returnInfo.setMessage(msg);
			}else{
				returnInfo.setMessage("数据导入成功");
			}
				
			returnInfo.setTotal(total);
		}catch(Exception e){
			returnInfo.setSuccess(false);
			returnInfo.setMessage("数据导入失败");
			e.printStackTrace();
		}
		
		return returnInfo;

	}
	
	

	// 上传数据文件
	private String uploadFile(HttpServletRequest request, String filePath)
			throws Exception {

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFiles("file").get(0);

		if (file.getSize() != 0) {

			String fileName = file.getOriginalFilename();

			filePath = filePath + fileName;

			File fileTarget = new File(filePath);
			// 创建多级目录
			if (!fileTarget.getParentFile().exists()) {
				fileTarget.getParentFile().mkdirs(); // 创建多级
			}

			// 文件保存
			file.transferTo(fileTarget);
			// checkFile(filePath, list,waiBuCase2);

		}

		return filePath;

	}

	public String getTestFilePath() {
		return testFilePath;
	}

	public void setTestFilePath(String testFilePath) {
		this.testFilePath = testFilePath;
	}
	
	
	

	// 导入商标的商品/服务数据
	public ReturnInfo importTradeMarkCategoryData(HttpServletRequest request,  String custId) {

		ReturnInfo rtnInfo = new ReturnInfo();

		// boolean isCaseData = false;
		try {
			

			String filePath = trademarkcase_uploadDir + "/" + custId + "/";

			// 单元测试时，不执行上传文件的操作，而是先把该文件放到这个目录下，然后使用测试文件所在路径
			if (testFilePath == null) {
				// 上传案件文件
				filePath = uploadFile(request, filePath);
			} else {
				filePath = testFilePath;
			}

			logger.info("import filePath: " + filePath);

			rtnInfo = importCategoryData(filePath);

		} catch (Exception e) {
			rtnInfo.setSuccess(false);
			e.printStackTrace();
			rtnInfo.setMessage("案件数据导入失败：" + e.getMessage());
			rtnInfo.setMessageType(Globals.MESSAGE_TYPE_OPERATION_INVALID);
			logger.info(e.getMessage());
		}
		return rtnInfo;
	}
	
	
	

	// 导入商标数据
	public ReturnInfo importTradeMarkData(HttpServletRequest request, String custId) {

		ReturnInfo rtnInfo = new ReturnInfo();

		// boolean isCaseData = false;
		try {

			String filePath = trademark_uploadDir + "/" + custId + "/";

			// 单元测试时，不执行上传文件的操作，而是先把该文件放到这个目录下，然后使用测试文件所在路径
			if (testFilePath == null) {
				// 上传案件文件
				filePath = uploadFile(request, filePath);
			} else {
				filePath = testFilePath;
			}

			logger.info("import filePath: " + filePath);
			
			rtnInfo.setSuccess(true);
			rtnInfo.setMessage("正在导入数据，请稍后查看");
			
			Integer importDataType=2; //1:导入申请人数据，2：导入商标数据			
			
			if (testFilePath == null) {
				updateDataByThread(filePath, custId, importDataType);
			}
			else {
				rtnInfo = importTmData(filePath, custId);
			}
			

		} catch (Exception e) {
			rtnInfo.setSuccess(false);
			e.printStackTrace();
			rtnInfo.setMessage("数据导入失败：" + e.getMessage());
			rtnInfo.setMessageType(Globals.MESSAGE_TYPE_OPERATION_INVALID);
			logger.info(e.getMessage());
		}
		return rtnInfo;
	}
	
	
	

	

	// 导入申请人数据
	public ReturnInfo importApplicantData(HttpServletRequest request, String custId)  {

		ReturnInfo returnInfo = new ReturnInfo();
			
		try{
			
			String filePath = applicant_uploadDir + "/" + custId + "/";

			// 单元测试时，不执行上传文件的操作，而是先把该文件放到这个目录下，然后使用测试文件所在路径
			if (testFilePath == null) {
				// 上传案件文件
				filePath = uploadFile(request, filePath);
			} else {
				filePath = testFilePath;
			}

			logger.info("import filePath: " + filePath);
		
			returnInfo.setSuccess(true);
			returnInfo.setMessage("正在导入数据，请稍后查看");
			
			Integer importDataType=1; //1:导入申请人数据，2：导入商标数据
			if (testFilePath == null) {
				updateDataByThread(filePath, custId, importDataType);
			}
			else {
				returnInfo = importAppData(filePath, custId);
			}
			
		
		
		}catch(Exception e){
			returnInfo.setSuccess(false);
			returnInfo.setMessage(e.getMessage());
		}
		
		
		return returnInfo;

	}
	
	
	// 在独立的线程中，更新某个客户的申请人的商标数据
	/**
	 * @param customerId
	 * @param custName
	 * @param appName
	 * @return
	 * @throws Exception
	 */
	public String updateDataByThread(String filePath, String custId, Integer importDataType) throws Exception{
			
		
		ExcuteTask task=new ExcuteTask();
		
		task.setDataService(this);
		task.setTradeMarkService(tradeMarkService);
		task.setCustId(custId);	
		task.setFilePath(filePath);
		task.setImportDataType(importDataType);
		pool.execute(task);
		
		return null;
	}
		
	
	public ReturnInfo processMultiTmTypeData(){
		ReturnInfo returnInfo = new ReturnInfo();

		try{
			
			int updateCount=0;
			List<Trademark> list=trademarkMapper.selectMultiTmType();
			if (list!=null && list.size()>0){
				for(Trademark tm: list){
					int count1=0;
					int count2=0;
					boolean updateFlag=false;					
					String tmType=tm.getTmType();
					String tmGroup=tm.getTmGroup();
					
					List<String> tmTypeList=new ArrayList<String>();					
					StringTokenizer idtok = new StringTokenizer(tmType, ",");					
					while (idtok.hasMoreTokens()) {
						String value= idtok.nextToken();
						if (value==null || value.equals("")){
							continue;
						}						
						if (!tmTypeList.contains(value)){
							tmTypeList.add(value);
							count1++;
						}else{
							count2++;							
						}						
					}
					
					if (count1>0 && count1==count2){
						updateFlag=true;
					}
					
					String[] a = tmTypeList.toArray(new String[tmTypeList.size()]);					
					String tmTypeResult= String.join(",", a);	
					tm.setTmType(tmTypeResult);					
					
					if (tmGroup!=null && !tmGroup.equals("")){
						List<String> tmGroupList=new ArrayList<String>();		
						StringTokenizer idtok2 = new StringTokenizer(tmGroup, ";");					
						while (idtok2.hasMoreTokens()) {
							String value= idtok2.nextToken();
							if (value==null || value.equals("")){
								continue;
							}
							int len=value.length();
							if (len==8){
								String value2=value.substring(0,4);
								value=value.substring(4);
								if (!tmGroupList.contains(value2)){
									tmGroupList.add(value2);
								}
							}
							if (!tmGroupList.contains(value)){
								tmGroupList.add(value);
							}							
						}
						
						String[] b = tmGroupList.toArray(new String[tmGroupList.size()]);					
						String tmGroupResult= String.join(";", b);
						if (tmGroupResult!=null && !tmGroupResult.endsWith(";")){
							tmGroupResult=tmGroupResult+";";
						}
						tm.setTmGroup(tmGroupResult);
					}
					
					if(updateFlag){
						trademarkMapper.updateByPrimaryKeySelective(tm);
						updateCount++;
						logger.info("updateCount: "+ updateCount);
					}
				}
			}
			
			returnInfo.setSuccess(true);
			returnInfo.setMessage("finished");
			
		}catch(Exception e){
			returnInfo.setSuccess(false);
			returnInfo.setMessage(e.getMessage());
		}
		
		
		return returnInfo;
	}
	
	

}
