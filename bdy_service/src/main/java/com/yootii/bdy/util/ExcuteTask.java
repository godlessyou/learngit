package com.yootii.bdy.util;

import java.util.List;

import com.yootii.bdy.data.service.ImportExcelDataService;
import com.yootii.bdy.trademark.service.TradeMarkService;

/**
 * 执行任务，实现Runable方式
 *
 */
public class ExcuteTask implements Runnable {

	String opt = null;
	String custId = null;
	String custName = null;
	List<String> appNames = null;
	
	String filePath=null;

	String message = null;
	String url=null;	

	ImportExcelDataService dataService = null;
	
	TradeMarkService tradeMarkService=null;
	
	



	Integer importDataType=null; //1:导入申请人数据，2：导入商标数据
	

	public ExcuteTask() {

	}

	public String getOpt() {
		return opt;
	}

	public void setOpt(String opt) {
		this.opt = opt;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public List<String> getAppNames() {
		return appNames;
	}

	public void setAppNames(List<String> appNames) {
		this.appNames = appNames;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	
	public ImportExcelDataService getDataService() {
		return dataService;
	}

	public void setDataService(ImportExcelDataService dataService) {
		this.dataService = dataService;
	}
	
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	

	public Integer getImportDataType() {
		return importDataType;
	}

	public void setImportDataType(Integer importDataType) {
		this.importDataType = importDataType;
	}


	
	public TradeMarkService getTradeMarkService() {
		return tradeMarkService;
	}

	public void setTradeMarkService(TradeMarkService tradeMarkService) {
		this.tradeMarkService = tradeMarkService;
	}

	

	@Override
	public void run() {
		try {
			
			if(importDataType!=null && importDataType.intValue()==1){
				dataService.importAppData(filePath,custId);
			}
			else if(importDataType!=null && importDataType.intValue()==2){
				dataService.importTmData(filePath,custId);
				
				tradeMarkService.updateSolrDate();
			}				
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	

}