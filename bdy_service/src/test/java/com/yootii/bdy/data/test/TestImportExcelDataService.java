package com.yootii.bdy.data.test;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import com.yootii.bdy.util.JsonUtil;

import com.yootii.bdy.tmcase.model.*;
import com.yootii.bdy.data.service.ImportExcelDataService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class TestImportExcelDataService {
	@Resource
	private ImportExcelDataService importExcelDataService;
	
	private static final Logger logger = Logger.getLogger(TestImportExcelDataService.class);
	
	
	
	
//	@Test
	public void testImportGuoNeiCaseData() {
		
		String testFilePath="C:/test/category/111.xlsx";
		
		importExcelDataService.setTestFilePath(testFilePath);
		
		String custId="10";	
			
//		Object info = importExcelDataService.importTradeMarkCategoryData(null, custId);
//		if (info != null) {
//			logger.info(JsonUtil.toJson(info));
//			logger.info("testImportGuoNeiCaseData测试通过");
//		}
		
	}
	
	
	
//	@Test
	public void testImportApplicantData() {		
		
		String testFilePath="C:/support/biaodeyi/applicant/GERMAIN & MAUREAU申请人信息-卢.xlsx";
		
		importExcelDataService.setTestFilePath(testFilePath);
	
		
		String custId="79";
			
		Object info = importExcelDataService.importApplicantData(null,custId);
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("testImportApplicantData测试通过");
		}
		
	}
	
	
	
	
	@Test
	public void testImportTradeMarkData() {		
				
		String testFilePath="C:/support/biaodeyi/MICROSOFT.xlsx";
		
		importExcelDataService.setTestFilePath(testFilePath);
		
		String custId="10";
			
//		Object info = importExcelDataService.importTradeMarkData(null, custId);					
//		if (info != null) {
//			logger.info(JsonUtil.toJson(info));
//			logger.info("testImportTradeMarkData测试通过");
//		}
		
	}
	
	
//	@Test
	public void processMultiTmTypeDataTest() {		
				
		
		Object info = importExcelDataService.processMultiTmTypeData();					
		if (info != null) {
			logger.info(JsonUtil.toJson(info));
			logger.info("processMultiTmTypeDataTest测试通过");
		}
		
	}
	
	
	
	
	
}
