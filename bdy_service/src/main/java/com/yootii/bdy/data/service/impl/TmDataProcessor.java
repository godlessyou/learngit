package com.yootii.bdy.data.service.impl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;









import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.yootii.bdy.common.Constants;
import com.yootii.bdy.customer.model.Applicant;
import com.yootii.bdy.customer.model.CustomerApplicant;
import com.yootii.bdy.trademark.model.Trademark;
import com.yootii.bdy.trademark.model.TrademarkCategory;
import com.yootii.bdy.trademark.model.TrademarkProcess;


@Component
// Spring Service Bean的标识.
//@Transactional
public class TmDataProcessor extends CommonDataProcessor {
	

	private static Logger logger = Logger.getLogger(TmDataProcessor.class);

	
	
	public void processApplicant(Integer custId, List<Applicant> list){
		if (list==null){
			return ;
		}
		
		List<String> AppNameList=new ArrayList<String>();
		for (Applicant applicant : list) {
			String applicantName=applicant.getApplicantName();
			if (!AppNameList.contains(applicantName)){
				AppNameList.add(applicantName);
			}	
		}
		
		Map<String, Object>map=new HashMap<String, Object>();
		map.put("AppNameList", AppNameList);
		
		//获取申请人对应的商标
		List<Applicant> applicantList=applicantMapper.selectByAppNameList(map);
		for(Applicant applicant:list){
			
			String applicantName=applicant.getApplicantName();	
			String applicantEnName=applicant.getApplicantEnName();
			
			applicant.setAppType("法人或其他组织");
			applicant.setSendType("国外");
			boolean addApplicant=true;
			Integer appId=null;
			for(Applicant app:applicantList){					
				String appName=app.getApplicantName();
				String appEnName=app.getApplicantEnName();	
				if (appName!=null && applicantName!=null && appName.equalsIgnoreCase(applicantName)){
					if ((applicantEnName==null || applicantEnName.equals("")) && (appEnName==null || appEnName.equals(""))){
						appId=app.getMainAppId();
						addApplicant=false;
						break;
					}
					if (appEnName!=null && applicantEnName!=null && appEnName.equalsIgnoreCase(applicantEnName)){
						appId=app.getMainAppId();
						addApplicant=false;
						break;
					}
	
					applicant.setMainAppId(app.getMainAppId());	
				}						
			}
			
			
			//如果该申请人的mainAppId为空，那么是一个新的申请人名字
			//如果custId为空，无法创建Customer与Applicant的对应关系
			//这样在查看客户商标时，无法通过客户找到该申请人名称，查询结果不准确。
			if (custId==null){
				if (applicant.getMainAppId()==null){					
					continue;
				}
			}			
			
			
			if(addApplicant){
				applicantMapper.insertSelective(applicant);
				
				if (applicant.getMainAppId()==null){
					applicant.setMainAppId(applicant.getId());
					applicantMapper.updateByPrimaryKeySelective(applicant);
				}	
				
				appId=applicant.getMainAppId();
				
				//由于applicantList是在本次插入数据库在操作之前从数据库获取到的申请人列表，没有包含本次插入的申请人
				//因此，应该将当前申请人数据要加到这个列表中，这样在处理后续从excel表中获取的同名申请人时
                //才能进行比较出数据库中已经存在同名申请人，获取到当前这个申请人的mainAppId
				applicantList.add(applicant);
			}
			
			if (custId==null){
				continue;
			}
			
			boolean addCustomerApplicant=true;
			
			List<CustomerApplicant> customerApplicantList=customerApplicantMapper.selectbyAppId(appId);
			if (customerApplicantList!=null && customerApplicantList.size()>0){		
				for(CustomerApplicant ca:customerApplicantList){
					Integer cId=ca.getCustId();	
					if (cId.intValue()==custId.intValue()){
						addCustomerApplicant=false;
						break;
					}							
				}												
			}
			
			if(addCustomerApplicant){				
				CustomerApplicant customerApplicant=new CustomerApplicant();
				customerApplicant.setCustId(custId);
				customerApplicant.setAppId(appId);
				customerApplicantMapper.insertSelective(customerApplicant);
			}				
				
			
		}
		
	}

	
	
	
	
	// 处理商标数据
	public void processTradeMark(Integer custId, List<Trademark> tmList) throws Exception{
		if (tmList==null || tmList.size()==0){
			return ;
		}		
		
		List<String> regNumberList=getNumberList(tmList);
		List<Trademark> tradeMarkList=getTmList(regNumberList);
		
		List<TrademarkCategory> tradeMarkCategoryList=getTmCategoryList(regNumberList);
		
		List<Trademark> finishedList=new ArrayList<Trademark>();
		
		List<Applicant> applicantList=new ArrayList<Applicant>();
				
		int count=0;
		
		for(Trademark tm:tmList){
			
			
			String regNumber=tm.getRegNumber();
			String tmType=tm.getTmType();	
			String tmCategory = tm.getTmCategory();
			
			if (regNumber==null || regNumber.equals("")){
				continue;
			}
			if (tmType==null || tmType.equals("")){
				continue;
			}
			
			if (regNumber.equals("G1151678")){
				logger.info("debug ");			
			}
			
			//由于在进行插入/更新数据库之前，进行了一次数据库查询，
			//所以，使用数据库查询的结果与待处理的数据行比较并不全面；
			//还需要将已经处理过的数据，与待处理的进行比较，以避免excel中重复的数据被插入数据库。
			boolean processed=false;
			for(Trademark fTM:finishedList){
				String regNumber1=fTM.getRegNumber();
				String tmType1=fTM.getTmType();
				String imageFilePath=fTM.getImgFilePath();
				if (imageFilePath==null || imageFilePath.equals("")){
					continue;							
				}
				if (regNumber1.equals(regNumber)){
					if(compareTmType(tmType, tmType1)){
						processed=true;	
						break;
					}
				}				
			}
			
			//如果该记录已经被处理过，忽略该条记录
			if(processed){
				count++;
				logger.info("line: "+count + " regNumber:"+ regNumber);			
				continue;
			}
			
	
			
			//需要将处理过的数据缓存起来备用。
			finishedList.add(tm);
			
										
			//1 、处理商标图片
			processTmImage(tm);
			
			//2 、处理商标状态信息
			processTradeMarkStatus(tm);			
			
			//3 、处理商标基本信息			
			int flag=0;//0：代表增加, 1:代表更新
			Integer tmId=null;
			if (tradeMarkList!=null && tradeMarkList.size()>0){
				for(Trademark tradeMark:tradeMarkList){	
					tmId=tradeMark.getTmId();
					String regNumber2=tradeMark.getRegNumber();
					String tmType2=tradeMark.getTmType();
					String tmGroup2=tradeMark.getTmGroup();
					if (regNumber2==null || !regNumber2.equals(regNumber)){
						continue;
					}
					if (tmType2==null || tmType2.equals("")){
						continue;
					}
					
					boolean hasSameOne=false;
					StringTokenizer idtok = new StringTokenizer(tmType2, ",");					
					while (idtok.hasMoreTokens()) {
						String value= idtok.nextToken();
						if (tmType.equals(value)){
							hasSameOne=true;
							break;
						}
					}
					
					if(!hasSameOne){
						continue;
					}	
					
					tm.setTmId(tmId);
					
					//与数据库中原有数据进行对比
					boolean sameOne=compareString(tradeMark, tm);						
					if (!sameOne){
						flag=1;
						tm.setTmType(tmType2);							
						if (tmGroup2!=null && !tmGroup2.equals("")){
							//合并tmGroup;
							String tmGroup=tm.getTmGroup();
							String tmGroupResult=processTmGroup(tmType, tmGroup, tmGroup2);
							tmGroup2=tmGroupResult;
							tm.setTmGroup(tmGroupResult);
						}
						
					}else{
						flag=2;
					}
					
					break;
				}
			}
			
						
			Date modifyDate=new Date();			
			tm.setModifyDate(modifyDate);
			//这个数据很大，而且后面已经把该数据进行处理后保存到商品/服务表中了，
			//因此，商标表中，已经没有必要再保存该数据，所以，设置该属性为null
			tm.setTmCategory(null);
			
			switch(flag){
				case 0:
					trademarkMapper.insertSelective(tm);
					break;
					
				case 1:
					trademarkMapper.updateByPrimaryKeySelective(tm);
					break;
				
				case 2:					
					Trademark tradeM=new Trademark();					
					tradeM.setTmId(tmId);
					tradeM.setModifyDate(modifyDate);
					
					trademarkMapper.updateByPrimaryKeySelective(tradeM);
					break;
					
				default:					
					break;	
			}
						
		
			//4 、处理商标的商品/服务数据
			
			processTmCategory(tmId, regNumber, tmType, tmCategory,  tradeMarkCategoryList);
			
			//5、处理商标的申请人数据;
			processApplicantOfTm(tm, applicantList);
			
			count++;			
			
			logger.info("line: "+count + " regNumber:"+ regNumber);			
			
		}
		
		
		
		//因为一标多类的商标，如果每条记录对应该商标的一个国际分类，
		//在案件办理过程中，页面显示的是多条记录，导致用户可能只选择了某个记录，也即选择了商标的某个国际分类，而漏掉其它的
		//6、将一标多类的商标的多条记录，合并成一条记录，可以避免在续展案件中，用户在选择时遗漏。
		mergeTmData(regNumberList);
		
		
		//7、将商标中提取的申请人插入数据库。
		processApplicant(custId, applicantList);
		
	}
	
	
	
	
	public void processTradeMarkProcess(List<TrademarkProcess> tmPs) throws Exception{
		if (tmPs==null){
			return ;
		}
		
	

		List<String> regNumberList=getNumberListByProcess(tmPs);
		List<Trademark> tmList=getTmList(regNumberList);
		
		List<TrademarkProcess> tmProcessList=getTmProcessList(regNumberList);
	
		//删除商标流程数据
//		trademarkProcessMapper.deleteByRegNumberList(map);
//		
//		for (TrademarkProcess trademarkProcess : tmPs) {	
//			Integer tmId=trademarkProcess.getTmId();
//			if (tmId==null){
//				continue;
//			}
//			
//			trademarkProcessMapper.insertSelective(trademarkProcess);			
//		}
		
		
		
		Map<String, List<TrademarkProcess>> map=new HashMap<String, List<TrademarkProcess>>();
		for (TrademarkProcess trademarkProcess : tmPs) {	
			String regNumber=trademarkProcess.getRegNumber();			
			if (regNumber==null || regNumber.equals("")){
				continue;
			}
			for (Trademark trademark : tmList) {
				String regNum=trademark.getRegNumber();
				if (regNum!=null && regNum.equals(regNumber)){
					Integer tmId=trademark.getTmId();
					trademarkProcess.setTmId(tmId);
					break;
				}
			}
			if (!map.containsKey(regNumber)){
				List<TrademarkProcess> list=new ArrayList<TrademarkProcess>();
				list.add(trademarkProcess);
				map.put(regNumber, list);
			}else{
				Iterator<Entry<String, List<TrademarkProcess>>> iter = map.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry<String, List<TrademarkProcess>> entry = (Map.Entry<String, List<TrademarkProcess>>) iter
							.next();
					String key=entry.getKey();
					List<TrademarkProcess> list=entry.getValue();
					if (key.equals(regNumber)){
						list.add(trademarkProcess);
						break;
					}
				}
			}			
		}
		
		int size=map.size();
		logger.info("total regNumber: "+size);
		
		int count=0;
		Iterator<Entry<String, List<TrademarkProcess>>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, List<TrademarkProcess>> entry = (Map.Entry<String, List<TrademarkProcess>>) iter
					.next();
			String regNumber=entry.getKey();
			List<TrademarkProcess> list=entry.getValue();
			updateTmProcessData(regNumber, list, tmProcessList);
			count++;
			int remain=size-count;
			logger.info("remain regNumber: "+remain);
		}
		
	}
	
	
	
	private void processApplicantOfTm(Trademark tm, List<Applicant> applicantList){
		String applicantName=tm.getApplicantName();
		String applicantEnName=tm.getApplicantEnName();
		String applicantAddress=tm.getApplicantAddress();
		String applicantEnAddress=tm.getApplicantEnAddress();
		
		if (applicantName==null || applicantName.equals("") || applicantName.equals("\\N")){
			return;
		}
		
		boolean hasSameOne=false;
		for(Applicant app:applicantList){
			String appName=app.getApplicantName();
			String appEnName=app.getApplicantEnName();
			if (appName==null || appName.equals("")){
				return;
			}
			if(appName.equalsIgnoreCase(applicantName)){
				if ((applicantEnName==null || applicantEnName.equals("")) && (appEnName==null || appEnName.equals(""))){
					hasSameOne=true;
					break;
				}
				if (appEnName!=null &&  applicantEnName!=null && appEnName.equalsIgnoreCase(applicantEnName)){
					hasSameOne=true;
					break;
				}
			}		
			
		}
		if (!hasSameOne){
			Applicant applicant=new Applicant();
			applicant.setApplicantName(applicantName);
			applicant.setApplicantEnName(applicantEnName);
			applicant.setApplicantAddress(applicantAddress);
			applicant.setApplicantEnAddress(applicantEnAddress);
			applicantList.add(applicant);
		}
	}

	
	
	private void processTradeMarkStatus(Trademark tm){
		String status=tm.getStatus();
		
		if (status==null || status.equals("")){
			return;
		}
		
		
		status=status.trim();
		
		int pos=status.lastIndexOf(" ");		
		if (pos>-1){		
			int len=status.length();			
			status=status.substring(pos+1, len);
		}
		
		if(status.endsWith(";")){
			int len=status.length();
			status=status.substring(0,len-1);
		}
		tm.setStatus(status);	
		
		Iterator<Entry<String, String>> iter = Constants.tmstatus_new.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) iter
					.next();
			String tmStatus = entry.getKey();
			String staList = entry.getValue();
			
			StringTokenizer idtok = new StringTokenizer(staList, ",");
			while (idtok.hasMoreTokens()) {
				String value = idtok.nextToken();
				value = value.trim();
				if (status.indexOf(value)>-1){
					tm.setTmStatus(tmStatus);
					return;
				}
			}			
		}
			
		
	}
			
	
	
	
	
}
