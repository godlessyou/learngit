package com.yootii.bdy.util;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;





import org.apache.log4j.Logger;

import com.yootii.bdy.customer.model.Applicant;
import com.yootii.bdy.data.model.ImportResult;
import com.yootii.bdy.data.model.Reason;
import com.yootii.bdy.tmcase.model.TmCategory;
import com.yootii.bdy.trademark.model.Trademark;
import com.yootii.bdy.trademark.model.TrademarkProcess;


public class CheckDataService {

	public static Logger logger = Logger.getLogger(CheckDataService.class);


	// 对从excel表格中读取的数据进行有效性检查，对于不符合要求的，记录行号和问题原因
	public boolean checkTmCategoryData(ImportResult caseImportResult, TmCategory tmCategory)
			throws Exception {		

		boolean dataCorrect=true;
		List<Reason> list =caseImportResult.getReasons();

		String lineNumber = tmCategory.getLineNo();
		int lineNo = Integer.parseInt(lineNumber);	

		String classess = tmCategory.getClasses();
		String msg = tmCategory.getReason();

		// 检查案件编号
		if (classess == null || classess.equals("")) {
			msg = "商标国际分类为空";
			dataCorrect=false;			
		}

		// 检查商标注册号
		String goods = tmCategory.getGoods();
		if (goods == null || goods.equals("")) {
			String s = "商品/服务描述为空";
			if (msg == null) {
				msg = s;
			} else {
				msg = msg + "；" + s;
			}
		}

		if (msg != null) {			

			// 保存不符合要求的数据的问题原因
			Reason reason = new Reason();
			reason.setReason(msg);
			reason.setLineNo(lineNo);
			list.add(reason);
			
		}

		return dataCorrect;

	}
	
	
	
	// 对从excel表格中读取的数据进行有效性检查，对于不符合要求的，记录行号和问题原因
	public boolean checkTradMarkData(ImportResult importResult, Trademark trademark)
			throws Exception {		

		boolean dataCorrect=true;
		List<Reason> list =importResult.getReasons();

		String lineNumber = trademark.getLineNo();
		int lineNo = Integer.parseInt(lineNumber);	
		
		String msg = trademark.getReason();

		
		// 检查商标注册号
		String regNumber = trademark.getRegNumber();
		if (regNumber == null || regNumber.equals("")) {
			String s = "申请/注册号为空";
			if (msg == null) {
				msg = s;
			} else {
				msg = msg + "；" + s;
			}
			dataCorrect=false;			
		}
		
		// 检查国际分类
		String tmType = trademark.getTmType();
		if (tmType == null || tmType.equals("")) {
			String s = "国际分类为空";
			if (msg == null) {
				msg = s;
			} else {
				msg = msg + "；" + s;
			}
			dataCorrect=false;			
		}

		if (msg != null) {			

			// 保存不符合要求的数据的问题原因
			Reason reason = new Reason();
			reason.setReason(msg);
			reason.setLineNo(lineNo);
			list.add(reason);
			
		}

		return dataCorrect;

	}
	
	
	
	

	// 对从excel表格中读取的数据进行有效性检查，对于不符合要求的，记录行号和问题原因
	public boolean checkTradMarkProcessData(ImportResult importResult, TrademarkProcess trademark)
			throws Exception {		

		boolean dataCorrect=true;
		List<Reason> list =importResult.getReasons();

		String lineNumber = trademark.getLineNo();
		int lineNo = Integer.parseInt(lineNumber);	
		
		String msg = trademark.getReason();

		
		// 检查商标注册号
		String regNumber = trademark.getRegNumber();
		if (regNumber == null || regNumber.equals("")) {
			String s = "申请/注册号为空";
			if (msg == null) {
				msg = s;
			} else {
				msg = msg + "；" + s;
			}
			dataCorrect=false;			
		}
		
	

		if (msg != null) {			

			// 保存不符合要求的数据的问题原因
			Reason reason = new Reason();
			reason.setReason(msg);
			reason.setLineNo(lineNo);
			list.add(reason);
			
		}

		return dataCorrect;

	}
	
	
	
	// 对从excel表格中读取的数据进行有效性检查，对于不符合要求的，记录行号和问题原因
	public boolean checkApplicantData(ImportResult importResult, Applicant applicant)
			throws Exception {		

		boolean dataCorrect=true;
		List<Reason> list =importResult.getReasons();		

		String lineNumber = applicant.getLineNo();
		int lineNo = Integer.parseInt(lineNumber);	
		
		String msg = applicant.getReason();

		
		// 检查商标注册号
		String applicantName = applicant.getApplicantName();
		if (applicantName == null || applicantName.equals("")) {
			String s = "申请人中文名称为空";
			if (msg == null) {
				msg = s;
			} else {
				msg = msg + "；" + s;
			}
			dataCorrect=false;			
		}
		
	
		if (msg != null) {			

			// 保存不符合要求的数据的问题原因
			Reason reason = new Reason();
			reason.setReason(msg);
			reason.setLineNo(lineNo);
			list.add(reason);
			
		}

		return dataCorrect;

	}
	
	
	
	
	private boolean checkDate(Date date){
		boolean hasError=false;
		// 检查案件递交日期
		if (date != null) {
			
			String strDate=DateTool.getDate(date);
			int pos=strDate.indexOf("-");
			if (pos<0){
				hasError=true;				
			}else{
				String year=strDate.substring(0, pos);
				if (year.length()!=4){
					hasError=true;
				}
				if (!StringUtils.isNum(year)){
					hasError=true;
				}else{
					int yearValue=Integer.parseInt(year);
					if(yearValue>2050 || yearValue<1950){
						hasError=true;
					}
				}
			}
			
			
		}
		return hasError;

	}
	
	

	
	

	
	//将检查结果进行合并，主要是对同一行的问题，合并到一起，成功行数，失败行数进行修改
	public void mergeResult(int total, int failNumber, ImportResult caseImportResult1 , ImportResult caseImportResult2 ){
				
		List<Reason> list1=caseImportResult1.getReasons();
		
		List<Reason> list2=caseImportResult2.getReasons();
		for(Reason res2:list2){
			int lineNo2=res2.getLineNo();
			boolean findSameLine=false;
			for(Reason res1:list1){
				int lineNo1=res1.getLineNo();
				if (lineNo1==lineNo2){
					String msg1=res1.getReason();
					String msg2=res2.getReason();
					msg1=msg1 + "；" +msg2;
					findSameLine=true;
					break;
				}
			}
			if (!findSameLine){
				list1.add(res2);
			
			}
		}
		
		
		int successNumber = total - failNumber;
		caseImportResult1.setSuccess(successNumber);
		caseImportResult1.setFail(failNumber);
			
		
		
	}

}
