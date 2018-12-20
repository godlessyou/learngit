package com.yootii.bdy.data.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.yootii.bdy.common.Constants;
import com.yootii.bdy.customer.dao.ApplicantMapper;
import com.yootii.bdy.customer.dao.CustomerApplicantMapper;
import com.yootii.bdy.trademark.dao.TrademarkCategoryMapper;
import com.yootii.bdy.trademark.dao.TrademarkMapper;
import com.yootii.bdy.trademark.dao.TrademarkProcessMapper;
import com.yootii.bdy.trademark.model.Trademark;
import com.yootii.bdy.trademark.model.TrademarkCategory;
import com.yootii.bdy.trademark.model.TrademarkProcess;
import com.yootii.bdy.util.DateTool;
import com.yootii.bdy.util.FileUtil;
import com.yootii.bdy.util.ImageUtils;
import com.yootii.bdy.util.StringUtils;

@Component
// Spring Service Bean的标识.
// @Transactional
public class CommonDataProcessor {

	protected static Logger logger = Logger
			.getLogger(CommonDataProcessor.class);

	@Resource
	protected TrademarkMapper trademarkMapper;

	@Resource
	protected TrademarkCategoryMapper trademarkCategoryMapper;

	@Resource
	protected TrademarkProcessMapper trademarkProcessMapper;

	@Resource
	protected ApplicantMapper applicantMapper;

	@Resource
	protected CustomerApplicantMapper customerApplicantMapper;
	
	
	
	
	// 合并商标数据，将一标多类的多条记录合并成一条记录
	protected void mergeTmData(List<String> regNumberList) throws Exception {
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("regNumberList", regNumberList);
		
		//将多条记录合并为一条
		trademarkMapper.mergeTmData(paramMap);
		
		//删除内容相同的重复的数据
		trademarkMapper.deleteDuplicateTmData(paramMap);		
		
		//修改被删除的tmId所对应的的商品/服务记录中的tmId
		//将tmId设置为向合并后的商标记录的tmId		
		trademarkCategoryMapper.updateTmId(paramMap);
		
	}
	
	
	protected  List<String>  getNumberListByProcess(List<TrademarkProcess> tmPs){
		List<String> regNumberList=new ArrayList<String>();
		for (TrademarkProcess trademarkProcess : tmPs) {			
			String regNumber=trademarkProcess.getRegNumber();		
			if (regNumber==null || regNumber.equals("")){
				continue;
			}
			if (!regNumberList.contains(regNumber)){
				regNumberList.add(regNumber);
			}
		}
				
		return regNumberList;
	}
	
	
	protected List<String>  getNumberList(List<Trademark> tms){
		List<String> regNumberList=new ArrayList<String>();
		for (Trademark trademark : tms) {			
			String regNumber=trademark.getRegNumber();		
			if (regNumber==null || regNumber.equals("")){
				continue;
			}
			if (!regNumberList.contains(regNumber)){
				regNumberList.add(regNumber);
			}
		}		
		
		return regNumberList;
	}
	
	
	protected List<Trademark>  getTmList(List<String> regNumberList){		
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("regNumberList", regNumberList);
		
		List<Trademark> tmList=trademarkMapper.selectByRegNumberList(paramMap);
		
		return tmList;
	}
	
	
	protected List<TrademarkCategory>  getTmCategoryList(List<String> regNumberList){		
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("regNumberList", regNumberList);
		
		List<TrademarkCategory> tmList=trademarkCategoryMapper.selectByRegNumberList(paramMap);
		
		return tmList;
	}
	
	
	protected List<TrademarkProcess>  getTmProcessList(List<String> regNumberList){		
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("regNumberList", regNumberList);
		
		List<TrademarkProcess> tmProcessList=trademarkProcessMapper.selectByRegNumberList(paramMap);
		
		return tmProcessList;
	}
	
	protected String  processTmGroup(String tmType, String tmGroup, String tmGroup2){
		
				
		if (tmGroup==null || tmGroup.equals("")){			
			return tmGroup2;
		}
		
		int tmTypeLen=tmType.length();
		if (tmTypeLen==1){
			tmType="0"+tmType;
			tmTypeLen=2;
		}
		
		List<String> tmGroupList2 = Arrays.asList(tmGroup2.split(";"));
		List<String> tmGroupList = Arrays.asList(tmGroup.split(";"));
		
		List<String> arrayList2 = new ArrayList<String>(tmGroupList2);
		
		Iterator<String> it = arrayList2.iterator();
		while (it.hasNext()) {
			String group=it.next();	
			if (group==null || group.equals("")){
				it.remove();
				continue;
			}
			int len=group.length();	
			if (len>tmTypeLen){
				String s=group.substring(0,tmTypeLen);
				if (s.equals(tmType)){
					it.remove();
				}
			}			
		}
		
		
		for (String group: tmGroupList){
			int len=group.length();
			if (len<4){
				continue;
			}
			if (!arrayList2.contains(group)){
				arrayList2.add(group);
			}
		}
		
		try{
			//当数据如：0301;0301,0306;0305;0306;0501;1605;1606;
			//其中有这种数据：0301,0306，将导致StringUtils.listSort执行排序时出现异常：
			//java.lang.NumberFormatException: For input string: "0301,0306"
			//因此，对此进行处理，直接忽略这个异常
			//排序
			StringUtils.listSort(arrayList2);
		}catch(Exception e){
			logger.info(e.getMessage()+ ", tmGroup2: "+tmGroup2 + ", tmGroup:"+tmGroup);
		}
		
		String[] b = arrayList2.toArray(new String[arrayList2.size()]);
		String tmGroupResult= String.join(";", b);
		if(tmGroupResult!=null && !tmGroupResult.endsWith(";")){
			tmGroupResult=tmGroupResult+";";
		}
		
		return tmGroupResult;
		
	}
	
	
	
	protected boolean  compareTmType(String tmType, String tmType2){
				
		if (tmType==null && tmType2==null){
			return true;
		}
		
		List<String> tmTypeList2 = Arrays.asList(tmType2.split(","));
		List<String> tmTypeList = Arrays.asList(tmType.split(","));
		
		for (String type: tmTypeList){
			boolean find=false;
			for (String type2: tmTypeList2){
				if (type2.equals(type)){
					find=true;
					break;
				}
			}
			if (!find){
				return false;
			}
		}
		return true;
	}
	
	

	protected void getTmGroup(String serviceDescription,
			List<TrademarkCategory> list, TrademarkCategory trademarkCategory) {

		int count = 0;
		String tmGroup = null;
		String name = null;
		if (serviceDescription.startsWith("_")){
			name=serviceDescription.substring(1);
		}else{
			StringTokenizer idtok3 = new StringTokenizer(serviceDescription, "_");	
			while (idtok3.hasMoreTokens()) {
				String value = idtok3.nextToken();
				if (count == 0) {
					tmGroup = value;
				}
				if (count>0) {
					name = value;
				}
				count++;
			}
		}
		
		if (name != null) {
			int len = name.length();
			if (len > 1500) {				
				name = name.substring(0, 1500);
			}
		}
		trademarkCategory.setName(name);		
				
		if (tmGroup != null) {
			int maxNumber=0;
			int len = tmGroup.length();
			if (len > 500) {				
				tmGroup = tmGroup.substring(0, 500);
			}			
			//遍历，查找最大值
			for (TrademarkCategory tmc : list) {
				String tGroup = tmc.getTmGroup();
				if (tGroup != null && tGroup.equals(tmGroup)) {
					Integer tNo = tmc.getNo();	
					int number = tNo.intValue();
					if (number>maxNumber){
						maxNumber=number;
					}
				}				
			}			
			maxNumber++;	
			
			trademarkCategory.setTmGroup(tmGroup);
			trademarkCategory.setNo(maxNumber);
		}else{
			
			int maxNumber =0;
			int size=list.size();
			if (size>0){
				int index=size-1;
				TrademarkCategory tc=list.get(index);
				tmGroup=tc.getTmGroup();
				Integer tNo = tc.getNo();
				if (tNo!=null){
					int number = tNo.intValue();
				    maxNumber=number+1;
				    trademarkCategory.setNo(maxNumber);
				}			
			    trademarkCategory.setTmGroup(tmGroup);
				
			}			
		}		

		list.add(trademarkCategory);
	}
	
	

	// 商品/服务信息是用逗号分隔，以及下划线分割的字符串，需要对字符串进行处理，获取服务群组和服务名称
	// 例如：2801_视频游戏互动遥控装置,2801_与外置显示屏或者显示器连用的视频游戏控制台
	protected void processServiceDes(String key,String serviceInfo, String regNumber, String tmType, Integer tmId,
			List<TrademarkCategory> tmcList) {
		
		Integer tType = new Integer(tmType);
		String tmTypeStr=tmType;
		int len=tmType.length();
		if (len==1){
			tmTypeStr="0"+tmType;			
		}else if (len==1){
			tmTypeStr=tmType;			
		}
		if(serviceInfo==null || serviceInfo.equals("")){
			return;
		}
		List<String> serviceInfoList = Arrays.asList(serviceInfo.split(key));
		List<String> arrayList2 = new ArrayList<String>(serviceInfoList);
		
		int count=0;
		Iterator<String> it = arrayList2.iterator();
		while (it.hasNext()) {
			String goodDes=it.next();	
			if (goodDes==null){
				continue;
			}
			if (goodDes!=null && !goodDes.startsWith(tmTypeStr) && !goodDes.startsWith("_")){
				if (count>0){
					int index=count-1;
					String s=arrayList2.get(index);
					s=s+key+goodDes;
					arrayList2.set(index, s);
					it.remove();
					count--;
				}
			}
			count++;
		}
		
		for(String serviceDescription: arrayList2){		
			TrademarkCategory trademarkCategory = new TrademarkCategory();
			trademarkCategory.setTmId(tmId);
			trademarkCategory.setRegNumber(regNumber);			
			trademarkCategory.setTmType(tType);
			
			getTmGroup(serviceDescription, tmcList, trademarkCategory);
		}

	}
	
	

	protected void insertTradeMarkCategoryTable(List<TrademarkCategory> tmcList) {
		for (TrademarkCategory trademarkCategory : tmcList) {
			// 插入新的商品/服务数据
			trademarkCategoryMapper.insertSelective(trademarkCategory);
		}

	}

	
	
	protected void deleteTradeMarkCategory(String regNumber, Integer tmType) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("regNumber", regNumber);
		map.put("tmType", tmType);

		trademarkCategoryMapper.deleteByTmIdAndTmType(map);

	}
	
	

	protected List<TrademarkCategory> seleteTradeMarkCategory(String regNumber,
			Integer tmType) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("regNumber", regNumber);
		map.put("tmType", tmType);

		List<TrademarkCategory> list = trademarkCategoryMapper
				.selectByTmIdAndTmType(map);

		return list;

	}
	
	

	// 更新商品/服务数据
	protected void updateTmGoods(String regNumber, Integer tmType,
			List<TrademarkCategory> tmcList, List<TrademarkCategory> tradeMarkCategoryList) throws Exception {

		// 查找数据库中是否已经有该注册号的商品和服务数据
//		List<TrademarkCategory> list = seleteTradeMarkCategory(regNumber,
//				tmType);
		
		// select database once replace select every regNumber
		List<TrademarkCategory> list =new ArrayList<TrademarkCategory>();
		if(tradeMarkCategoryList!=null){
			for (TrademarkCategory tradeMarkCategory : tradeMarkCategoryList) {
				String regNum = tradeMarkCategory.getRegNumber();
				Integer tType=tradeMarkCategory.getTmType();
				if (regNum!=null && regNum.equals(regNumber)){
					if (tType!=null && tType.intValue()==tmType.intValue()){
						list.add(tradeMarkCategory);
					}
				}
			}
		}

		if (list == null || list.size() == 0) {
			insertTradeMarkCategoryTable(tmcList);
			return;
		}

		boolean deleteFlag = false;
		// 判断数据库中的数据与本次获取的数据是否完全相同，
		// 如果有不同的，将删除表中的数据，
		for (TrademarkCategory tradeMarkCategory : tmcList) {
			String group1 = tradeMarkCategory.getTmGroup();
			String name1 = tradeMarkCategory.getName();
			boolean hasSameOne = false;
			for (TrademarkCategory tmc : list) {
				String group2 = tmc.getTmGroup();
				String name2 = tmc.getName();
				if (group1 != null && group2 != null && group1.equals(group2)) {
					if (name1 != null && name2 != null && name1.equals(name2)) {
						hasSameOne = true;
						break;
					}
				}
			}
			if (!hasSameOne) {
				deleteFlag = true;
				break;
			}
		}

		if (deleteFlag) {
			// 删除表中的数据，插入排序后的数据
			deleteTradeMarkCategory(regNumber, tmType);
			insertTradeMarkCategoryTable(tmcList);
		}

	}
	
	

	// 处理商品/服务数据
	protected void processTmCategory(Integer tmId, String regNumber, String tmType, String tmCategory, List<TrademarkCategory> tradeMarkCategoryList) throws Exception {


		List<TrademarkCategory> tmcList = new ArrayList<TrademarkCategory>();
		
		String key = ",";
		//String key2 = "，";
		
		
		// TrademarkCategory中的一些特殊情况：例如：0901_用于照片和图形位置定位的计算机软件,0901_用于从照片,图像以及三维模型中获取三维数据的计算机软件,
		// 这里面每个商品服务的描述数据是用逗号分隔，但在一个商品服务的描述数据中又有逗号
		// 这种情况下，如果只是把逗号作为分隔符去截取上述字符串，会得到3个子串，
		// 这样得到的结果是不正确的，因此，需要用 逗号+国际分类作为分隔符去截取，才能得到2个子串，分别是：
		// 01_用于照片和图形位置定位的计算机软件,
		// 01_用于从照片,图像以及三维模型中获取三维数据的计算机软件,
		// 后面再使用这个数据的时候，还需要再把前面的国际分类给补充上，才能还原回来正确的结果，主要是字符串中用逗号分隔造成。
		// 如果抓取完的数据，用一个比较特殊的符号作为分割符号，比如用竖线作为分隔符，然后再保存到excel，就可以减少处理上的麻烦，
		
		processServiceDes(key, tmCategory, regNumber, tmType, tmId,
				tmcList);
		
		if (tmcList==null || tmcList.size()==0){
			return;
		}

		Integer tType = new Integer(tmType);

		updateTmGoods(regNumber, tType, tmcList, tradeMarkCategoryList);

		/*
		 * List<Integer>tmIdList=null; if (tmcList!=null && tmcList.size()>0){
		 * tmIdList=new ArrayList<Integer>(); tmIdList.add(tmId);
		 * 
		 * if (tmIdList!=null && tmIdList.size()>0){ Map<String, Object>map=new
		 * HashMap<String, Object>(); map.put("tmIdList", tmIdList);
		 * //删除原有的商品/服务数据 trademarkCategoryMapper.deleteByTmIdList(map); }
		 * 
		 * for(TrademarkCategory trademarkCategory : tmcList){ //插入新的商品/服务数据
		 * trademarkCategoryMapper.insertSelective(trademarkCategory); }
		 * 
		 * }
		 */

	}

	
	
	protected void insertTradeMarkProcessTable(List<TrademarkProcess> tmPs) {
		for (TrademarkProcess trademarkProcess : tmPs) {
			Integer tmId = trademarkProcess.getTmId();
			if (tmId == null) {
				continue;
			}
			trademarkProcessMapper.insertSelective(trademarkProcess);
		}
	}
	

	protected void deleteTradeMarkProcess(String regNumber) {
		trademarkProcessMapper.delectByRegNumber(regNumber);
	}
	

	protected List<TrademarkProcess> getTradeMarkProcessList(String regNumber) {
		List<TrademarkProcess> list = trademarkProcessMapper
				.selectByRegNumber(regNumber);
		return list;
	}

	
	

	// 更新商标的流程数据
	protected void updateTmProcessData(String regNumber,
			List<TrademarkProcess> tmPs, List<TrademarkProcess> tmProcessList) throws Exception {
		
		if (tmPs == null ||  tmPs.size() == 0) {
			return;
		}

		// 从数据库获取该商标的流程数据
//		List<TrademarkProcess> tradeMarkProcessList = getTradeMarkProcessList(regNumber);
		
		List<TrademarkProcess> tradeMarkProcessList=new ArrayList<TrademarkProcess>();
		for(TrademarkProcess tp:tmProcessList){
			String regNum=tp.getRegNumber();
			if (regNum!=null && regNum.equals(regNumber)){
				tradeMarkProcessList.add(tp);
			}
		}

		if (tradeMarkProcessList == null || tradeMarkProcessList.size() == 0) {
			// 如果数据库中没有该商标的流程数据，那么插入
			insertTradeMarkProcessTable(tmPs);

		} else {
			
			int size1 = tradeMarkProcessList.size();
			int size2 = tmPs.size();

			boolean deleteFlag = false;

			if (size1 != size2) {
				deleteFlag = true;
			} else {

				// 判断数据库中的数据与本次获取的数据是否完全相同，
				// 如果有不同的，将删除表中的数据，
				// 而使用本次获取的数据，以减少误差。
				// 场景：数据库中有一条status为“商标注册申请等待驳回通知发文”的数据
				// 本次获取的数据中有一条status为“商标注册申请驳回通知发文”，二者时间相同，
				// 唯独status有细小差别，实际含义是相同的。如果不删除原有数据，而插入当前获取的数据
				// 将使数据数据库中存在相近的流程数据
				Iterator<TrademarkProcess> it = tmPs.iterator();
				while (it.hasNext()) {
					TrademarkProcess tmp = it.next();
					String status = tmp.getStatus();
					Date statusdate = tmp.getStatusDate();

					boolean hasSameOne = false;
					for (TrademarkProcess tradeMarkProcess : tradeMarkProcessList) {
						String pStatus = tradeMarkProcess.getStatus();
						Date pStatusdate = tradeMarkProcess.getStatusDate();
						if (status != null && pStatus != null
								&& status.equals(pStatus)) {
							if (statusdate == null && pStatusdate == null) {
								hasSameOne = true;
								break;
							}
							if (statusdate != null && pStatusdate != null
									&& statusdate.equals(pStatusdate)) {
								hasSameOne = true;
								break;
							}

						}
					}
					if (!hasSameOne) {
						deleteFlag = true;
						break;
					}
				}
			}

			if (deleteFlag) {
				// 删除原有数据
				deleteTradeMarkProcess(regNumber);

				// 插入当前获取的数据
				insertTradeMarkProcessTable(tmPs);
			}

		}

	}
	
	
	
	
	
	
	// 从API接口获取图片，并保存到本地
	protected void getTmImage(String imgFileName, String regNumber,
			String imagePath, String url) throws Exception {

		// 处理商标图片
		String fileName = regNumber + ".jpg";
		String imageFile = imagePath + File.separator + fileName;

		File localFile = new File(imageFile);
		if (!localFile.exists()) {
			ImageUtils.getImageFromUrl(url, imageFile);
		}
	}
		
		

	// 压缩图片
	protected boolean compressTmImage(String regNumber, String imagePath,
			String tempDir, Trademark tm) throws Exception {

		boolean success = false;
		int limit = 300;
		float baseSize = 100;

		// 处理商标图片
		String fileName = regNumber + ".jpg";
		String fileName2 = regNumber + "_1.jpg";
		String sourceFile = tempDir + "/" + fileName;
		String tempFile = tempDir + "/" + fileName2;

		String destFile = imagePath + "/" + fileName;
		File file3 = new File(destFile);
		if (!file3.exists()) {
			File file1 = new File(sourceFile);
			if (file1.exists()) {
				// 对图片进行另存为处理，减小文件尺寸
				success = ImageUtils.cutAndCompressImage(sourceFile, tempFile,
						destFile, limit, baseSize);
				String imgFilePath = null;
				int len = Constants.image_dir.length();
				if (success) {
					imgFilePath = destFile.substring(len);
				} else {
					imgFilePath = sourceFile.substring(len);
				}
				tm.setImgFilePath(imgFilePath);
			}

		}
		return success;

	}
	
	

	// 处理商标图片
	protected void processTmImage(Trademark tm) {
		String regNumber = tm.getRegNumber();
		String appName = tm.getApplicantName();
				
		String dirName=appName;
		
		if (dirName==null ||dirName.equals("")){
			dirName="noAppName";
		}
		
		//替换目录名不允许的/\\\\:*?<>|等非法的字符
		dirName=StringUtils.replaceChar(dirName);

		String imagePath = Constants.image_dir + "/" + dirName;
		// 需要进行图片缩小的操作，所以需要临时目录
		String tempDir = imagePath + "/" + "temp";
		FileUtil.createFolderIfNotExists(tempDir);

		String imgUrl = tm.getImgUrl();

		// if (appName!=null && appName.equals("MICROSOFT CORPORATION")){
		// logger.info("MICROSOFT CORPORATION");
		// }

		try {
			// 处理商标图片
			String fileName = regNumber + ".jpg";
			String imageFile = imagePath + "/" + fileName;

			boolean imageIsExist = imageExist(imageFile);
			if (!imageIsExist) {
				// imageFile = tempDir + "/" + fileName;
				// 获取商标图片，并保存到临时目录下
				if (imgUrl != null && !imgUrl.equals("")
						&& imgUrl.indexOf("http") > -1) {
					boolean success = ImageUtils.getImageFromUrl(imgUrl,
							imageFile);
					// if (success){
					// //对图片截取白边，并进行压缩
					// success=compressTmImage(regNumber, imagePath, tempDir,
					// tm);
					// }
				}

			} else {
				int len = Constants.image_dir.length();
				String imgFilePath = imageFile.substring(len);
				tm.setImgFilePath(imgFilePath);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	// 从API接口获取图片，并保存到本地
	protected void getTmImage(String imgUrl, String imageFile) throws Exception {

		File localFile = new File(imageFile);
		if (!localFile.exists()) {
			ImageUtils.getImageFromUrl(imgUrl, imageFile);
		}
	}
	
	

	// 查看商标图片是否存在
	protected boolean imageExist(String imageFile) throws Exception {

		boolean exist = false;

		File localFile = new File(imageFile);
		if (localFile.exists()) {
			exist = true;
		}
		return exist;
	}
	
	

	protected String processAppName(String appName) {

		// 将申请人中的中文括号，替换为英文括号，
		// 否则将无法查找到该申请人的商标
		String newName = appName;

		if (newName.indexOf("（") > -1) {
			newName = newName.replaceAll("（", "(");
		}
		if (newName.indexOf("）") > -1) {
			newName = newName.replaceAll("）", ")");
		}

		return newName;
	}
	
	

	protected boolean compareString(Trademark tradeMark, Trademark tm) {

		// 计算hash值
		int hashValue = getStringHash(tradeMark);

		int hashValue2 = getStringHash(tm);

		if (hashValue != hashValue2) {
			return false;
		}

		return true;

	}
	
	

	// 获取数据库中该商标的属性相加构成一个字符串，获取该字符串的hash值
	protected int getStringHash(Trademark tradeMark) {

		String data = "";

		Date appdate = tradeMark.getAppDate();
		if (appdate != null) {
			String date = DateTool.getDate(appdate);
			data = StringUtils.addString(data, date);
		}

		Date regDate = tradeMark.getRegNoticeDate();
		if (regDate != null) {
			String date = DateTool.getDate(regDate);
			data = StringUtils.addString(data, date);
		}

		Date validStart = tradeMark.getValidStartDate();
		if (validStart != null) {
			String date = DateTool.getDate(validStart);
			data = StringUtils.addString(data, date);
		}

		Date validEnd = tradeMark.getValidEndDate();
		if (validEnd != null) {
			String date = DateTool.getDate(validEnd);
			data = StringUtils.addString(data, date);
		}

		Date gjRegDate = tradeMark.getGjRegDate();
		if (gjRegDate != null) {
			String date = DateTool.getDate(gjRegDate);
			data = StringUtils.addString(data, date);
		}

		Date hqzdDate = tradeMark.getHqzdDate();
		if (hqzdDate != null) {
			String date = DateTool.getDate(hqzdDate);
			data = StringUtils.addString(data, date);
		}

		String priorDate = tradeMark.getPriorDate();
		

		// String tmGroup=tradeMark.getTmGroup();
		// data=StringUtils.addString(data, tmGroup);

		String tmName = tradeMark.getTmName();
		data = StringUtils.addString(data, tmName);

		String approveNumber = tradeMark.getApprovalNumber();
		data = StringUtils.addString(data, approveNumber);

		String regnoticeNumber = tradeMark.getRegnoticeNumber();
		data = StringUtils.addString(data, regnoticeNumber);

		String agent = tradeMark.getAgent();
		data = StringUtils.addString(data, agent);

		String applicantName = tradeMark.getApplicantName();
		data = StringUtils.addString(data, applicantName);

		String applicantEnName = tradeMark.getApplicantEnName();
		data = StringUtils.addString(data, applicantEnName);

		String applicantAddress = tradeMark.getApplicantAddress();
		data = StringUtils.addString(data, applicantAddress);

		String applicantEnAddress = tradeMark.getApplicantEnAddress();
		data = StringUtils.addString(data, applicantEnAddress);

//		String tmcategory = tradeMark.getTmCategory();
//		data = StringUtils.addString(data, tmcategory);

		String status = tradeMark.getStatus();
		data = StringUtils.addString(data, status);

		String ifStareTm = tradeMark.getIfShareTm();
		data = StringUtils.addString(data, ifStareTm);

		String gtApplicantName = tradeMark.getGtApplicantName();
		data = StringUtils.addString(data, gtApplicantName);

		String tmForm = tradeMark.getTmForm();
		data = StringUtils.addString(data, tmForm);

		String classify = tradeMark.getClassify();
		data = StringUtils.addString(data, classify);

		String imgUrl = tradeMark.getImgUrl();
		data = StringUtils.addString(data, imgUrl);

		String imgFilePath = tradeMark.getImgFilePath();
		data = StringUtils.addString(data, imgFilePath);

		// 计算字符串的hash值
		int hashValue = StringUtils.getStringHash(data);

		return hashValue;

	}

}
