package com.yootii.bdy.tmcase.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.yootii.bdy.common.Constants;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.customer.model.Applicant;
import com.yootii.bdy.customer.service.ApplicantService;
import com.yootii.bdy.material.dao.MaterialMapper;
import com.yootii.bdy.material.dao.MaterialSortMapper;
import com.yootii.bdy.material.model.Material;
import com.yootii.bdy.material.model.MaterialCondition;
import com.yootii.bdy.material.model.MaterialSort;
import com.yootii.bdy.material.service.MaterialService;
import com.yootii.bdy.remind.service.RemindService;
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseFileMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseMapper;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.TradeMarkCaseFile;
import com.yootii.bdy.tmcase.model.TradeMarkCaseFilePre;
import com.yootii.bdy.tmcase.model.TradeMarkCasePre;
import com.yootii.bdy.tmcase.service.TradeMarkCaseFileService;
import com.yootii.bdy.tmcase.service.TradeMarkCaseService;
import com.yootii.bdy.trademark.model.Trademark;
import com.yootii.bdy.util.ImageUtils;

@Service
public class TradeMarkCaseFileServiceImpl implements TradeMarkCaseFileService{
	
	private static String tmcase_uploadDir = Constants.upload_dir+Constants.tmcaseFileDir;
	
	@Resource
	private TradeMarkCaseFileMapper tradeMarkCaseFileMapper;
	
	@Resource
	private TradeMarkCaseService tradeMarkCaseService;
	
	@Resource
	private TradeMarkCaseMapper tradeMarkCaseMapper;
	
	@Resource
	private RemindService remindService;
	
	@Resource
	private MaterialService materialService;
	@Resource
	private MaterialMapper materialMapper;
	@Resource
	private MaterialSortMapper materialSortMapper;
	
	@Resource
	private ApplicantService applicantService;
	
	@Value("${fileUrl}")  
	private String fileUrl;
	
	
	
	
	@Override
	public ReturnInfo uploadCaseFile(HttpServletRequest request,
			Integer caseId, TradeMarkCaseFile tmcaseFile, GeneralCondition gcon, Token token, Integer applicantId) {
		ReturnInfo rtnInfo= new ReturnInfo();
		tmcaseFile.setCaseId(caseId);
		if(caseId==null){
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("案件ID不能为空");
			return rtnInfo;
		}
		if(tmcaseFile.getFileName()==null){
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("文件类型不能为空");
			return rtnInfo;
		}
		if(tmcaseFile.getUsername()==null){
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("用户名不能为空");
			return rtnInfo;
		}
		try {
			//rtnInfo = uploadFile(request, tmcaseFile);
			rtnInfo = uploadFileByMaterial(request, tmcaseFile, gcon, token,applicantId);
		} catch (IOException e) {
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("文件上传失败"); 
		}
		if(rtnInfo.getSuccess()==false){
			return rtnInfo;
		}
		tradeMarkCaseFileMapper.insertSelective(tmcaseFile);

		TradeMarkCase tradeMarkCase = (TradeMarkCase)tradeMarkCaseService.queryTradeMarkCaseDetail(caseId).getData();
		Trademark trademark = new Trademark();
		Date date = new Date();
		Integer type = 0;
		switch (tmcaseFile.getFileName().intValue()) {
		case 340:
			type = 3;
			break;
		case 341:
			type = 4;
			break;
		case 305:
			type = 5;
			break;
		case 303:
			type = 6;
			break;
		case 302:
			type = 7;
			break;
//		case :
//			type = 8;
//			break;
//		case :
//			type = 9;
//			break;
		case 311:
			type = 10;
			break;
			
			
		}
		
		if(type.intValue()!=0)
		remindService.insertRemindByType(type, date, null, tradeMarkCase.getCustId(), caseId, gcon);
		
		rtnInfo.setSuccess(true);
		return rtnInfo;
	}
	private ReturnInfo uploadFile(HttpServletRequest request,TradeMarkCaseFile tmcaseFile) throws IOException {
		ReturnInfo rtnInfo= new ReturnInfo();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFiles("file").get(0);
		Properties properties =  new  Properties();
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("config.properties");  
		properties.load(inputStream);
		String case_maxfilesize = properties.getProperty("case.maxfilesize");
		String case_maxallfilesize = properties.getProperty("case.maxallfilesize");
		Double doublenum = Double.parseDouble(case_maxfilesize);
		long case_maxfilesize1 = doublenum.longValue()*1024*1024;
		try {
			if(file.getSize()!=0) {
				//单个文件大小超过case_maxfilesize1 ,上传失败
				if(file.getSize()>case_maxfilesize1) {
					rtnInfo.setSuccess(false);
					rtnInfo.setMessage("文件上传失败,单个文件大小不能超过 :"+case_maxfilesize+"M");
					return rtnInfo;
				}
				Integer custId = (tradeMarkCaseMapper.selectByPrimaryKey(tmcaseFile.getCaseId())).getCustId();
				String fileName = file.getOriginalFilename();
				//当前时间戳
				Long timestamp = System.currentTimeMillis();
				String filePath = tmcase_uploadDir+custId+"/"+timestamp+"/"+ fileName;
//				tmcaseFile.setFileName(fileName);
				String databasePath ="/"+Constants.uploadDir+ Constants.tmcaseFileDir+custId+"/"+ timestamp+"_"+fileName;;
				tmcaseFile.setFileUrl(databasePath);
				if(tmcaseFile.getFileType()==null){
					tmcaseFile.setFileType("递交官方");
				}
				File fileTarget = new File(filePath);
				// 创建多级目录
				if (!fileTarget.getParentFile().exists()) {
					fileTarget.getParentFile().mkdirs(); // 创建多级
				}
				// 文件保存
				file.transferTo(fileTarget);
			}

		} catch (Exception e) {
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("文件上传失败"); 
			return rtnInfo;
			
		} 
		rtnInfo.setSuccess(true);
		return rtnInfo;
	}
	@Override
	public ReturnInfo insertCaseFileData(Integer caseId,
			TradeMarkCaseFile tmcaseFile) {
		ReturnInfo rtnInfo= new ReturnInfo();
		if(caseId==null){
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("案件ID不能为空");
			return rtnInfo;
		}
		if(tmcaseFile.getFileName()==null){
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("文件类型不能为空");
			return rtnInfo;
		}
		if(tmcaseFile.getUsername()==null){
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("用户名不能为空");
			return rtnInfo;
		}
		tmcaseFile.setCaseId(caseId);
		tradeMarkCaseFileMapper.insertSelective(tmcaseFile);
		rtnInfo.setSuccess(true);
		return rtnInfo;
	}
	@Override
	public ReturnInfo queryCaseFile(TradeMarkCaseFile tmcaseFile, String status) {
		ReturnInfo rtnInfo= new ReturnInfo();
		Integer caseId = tmcaseFile.getCaseId();
		if(caseId==null){
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("案件ID不能为空");
			return rtnInfo;
		}
		String fileNames = null;
		if("准备申请材料".equals(status)||"递交申请材料".equals(status)||"递交补正材料".equals(status)||"递交分割申请材料".equals(status)){
			fileNames = "递交官方";
		}else if("不予受理通知".equals(status)){
			fileNames="306";
		}else if("补正通知".equals(status)){
			fileNames="305";
		}else if("驳回通知".equals(status)){
			fileNames="302";
		}else if("部分驳回通知".equals(status)){
			fileNames="303";
		}else if("驳回复审裁定-赢".equals(status)){
			fileNames="349";
		}else if("驳回复审裁定-输".equals(status)){
			fileNames="350";
		}else if("受理通知".equals(status)){
			fileNames="332";
		}else if("初步审定公告".equals(status)){
			fileNames="316";
		}else if("异议答辩通知".equals(status)){
			fileNames="340";
		}else if("异议答辩裁定-赢".equals(status)){
			fileNames="351";
		}else if("异议答辩裁定-输".equals(status)){
			fileNames="352";
		}else if("不予核准通知".equals(status)){
			fileNames="307";
		}else if("不予注册复审决定-赢".equals(status)){
			fileNames="353";
		}else if("不予注册复审决定-输".equals(status)){
			fileNames="354";
		}else if("核准证明".equals(status)){
			fileNames="320";
		}
		String fn = "";
		List<Integer> list = null;
		if("准备申请材料".equals(status)||"递交申请材料".equals(status)||"递交补正材料".equals(status)||"递交分割申请材料".equals(status)){
			list =new ArrayList<Integer>();
			//获取所有递交官方的资料
			List<MaterialSort> materialSortList=materialSortMapper.selectDijiao();
			if (materialSortList!=null){
				for(MaterialSort ms:materialSortList){
					list.add(ms.getFileName());
				}
			}
			
		} 
		
		
		MaterialCondition materialCondition=new MaterialCondition();
		materialCondition.setCaseId(caseId);
		materialCondition.setFileNames(list);
		List<Material> m= materialMapper.selectByCaseIdAndFileNames(materialCondition);
		//List<TradeMarkCaseFile> tmcaseFiles = tradeMarkCaseFileMapper.selectByCaseIdAndFileNames(caseId, fileNames);
		rtnInfo.setSuccess(true);
		rtnInfo.setData(m);
		rtnInfo.setMessage("查询成功");
		return rtnInfo;
	}
	/*private ReturnInfo uploadFileByMaterial(HttpServletRequest request,TradeMarkCaseFile tmcaseFile, GeneralCondition gcon, Token token, Integer applicantId) throws IOException {
		//ReturnInfo rtnInfo= new ReturnInfo();
		//上传预立案
		Material material = new Material();
		String subject = "fileName : "+tmcaseFile.getFileName()+","+"joinAppId : "+tmcaseFile.getJoinAppId();
		material.setApplicantId(applicantId);
		material.setSubject(subject);
		material.setCaseId(tmcaseFile.getId());
		if(tmcaseFile.getFileType()==null){
			material.setSubject(subject+","+"递交官方");
		}
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFiles("attachFile").get(0);

		
		if (file != null && file.getSize() != 0) {			
			if (file.getOriginalFilename()!=null) {
				material.setTitle(file.getOriginalFilename());
			}else {
				material.setTitle("");
			}
			
		}
		//material.setTitle(file.getOriginalFilename());
		ReturnInfo info = materialService.createMaterial(request, material, gcon, token);
		return info;
	}*/
	private ReturnInfo uploadFileByMaterial(HttpServletRequest request,TradeMarkCaseFile tmcaseFile, GeneralCondition gcon, Token token, Integer applicantId) throws IOException {
		
		ReturnInfo info  =new ReturnInfo();
		Material material = new Material();
		if(tmcaseFile.getFileName() != null &&tmcaseFile.getFileName().toString().matches("[0-9]+")) {
			MaterialSort materialSort =new MaterialSort();
			materialSort.setFileName(tmcaseFile.getFileName());
			List<MaterialSort> sort = materialSortMapper.selectByMaterialSort(materialSort, gcon);
			if (sort!=null && sort.size()>0){
				material.setSortId(sort.get(0).getId());
			}
			material.setMaterialSort(sort);
		}
//		String subject = "fileName : "+tmcaseFile.getFileName()+","+"joinAppId : "+tmcaseFile.getJoinAppId();
		
		// 通过tmcase 找custId和applicantId		
		if(tmcaseFile.getCustId() != null &&tmcaseFile.getCustId().matches("[0-9]+")) {
			Integer cId=new Integer(tmcaseFile.getCustId());
			material.setCustId(cId) ;
		}else {
			TradeMarkCase tmcase = tradeMarkCaseMapper.selectByPrimaryKey(tmcaseFile.getCaseId());
			if(tmcase !=null) {				
				material.setCustId(tmcase.getCustId()) ;
			}
		}
		
		if (applicantId!=null){		
			material.setApplicantId(applicantId);
		}else{		
			TradeMarkCase tmcase = tradeMarkCaseMapper.selectByPrimaryKey(tmcaseFile.getCaseId());
			if(tmcase !=null) {				
				String appCnName=tmcase.getAppCnName();
				if(appCnName != null) {
					String tokenID=null;
					Applicant applicant=applicantService.queryApplicantByAppName(appCnName, tokenID);					
					if (applicant!=null){
						material.setApplicantId(applicant.getId());						
					}
				}
			}
		}
				
			
		System.out.println("custId   "+ material.getCustId());
		
		material.setFileName(tmcaseFile.getFileName());
		material.setJoinAppId(tmcaseFile.getJoinAppId());
		
		material.setSubject("递交官方");
		material.setCaseId(tmcaseFile.getCaseId());
		if(tmcaseFile.getFileType()==null){
			material.setSubject("递交官方");
		}
		
		//2018-07-24 added
		material.setPrecase(2);
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFiles("attachFile").get(0);		
		if (file != null && file.getSize() != 0) {			
			if (file.getOriginalFilename()!=null) {
				material.setTitle(file.getOriginalFilename());
			}else {
				material.setTitle("");
			}
			
		}
		
		Integer fileName =tmcaseFile.getFileName();
		List<Material> materials = materialMapper.selectByMaterialWithApplicantId(material,  gcon);
		if(material.getCaseId() !=null && materials!=null&&materials.size()>0) {
			System.out.println("jijiji");
				for(Material m : materials) {
					int a= 0;
					m.setJoinAppId(tmcaseFile.getJoinAppId());
//					m.setSubject("joinAppId : "+tmcaseFile.getJoinAppId());		
					/*if(tmcaseFile.getFileName()!=null&&tmcaseFile.getFileName()==24){//24代表图样
						String address =null;
						if (material.getCaseId() != null) {
							address = Constants.material_dir + '/'
									+ material.getCustId() + '/' + material.getCaseId()
									+ '/' + material.getMaterialId() + '/'
									+ material.getVersionNo() + '/'+file.getOriginalFilename();
						} else {
							address = Constants.material_dir + '/'
									+ material.getCustId() + '/' + material.getMaterialId()
									+ '/' + material.getVersionNo() + '/'+file.getOriginalFilename();

						}	
						TradeMarkCase tradeMarkCase = tradeMarkCaseMapper.selectByPrimaryKey(tmcaseFile.getCaseId());
						tradeMarkCase.setImageFile(address);
						tradeMarkCaseMapper.updateByPrimaryKeySelective(tradeMarkCase);
						}*/
					if(m.getTitle().equals(material.getTitle())) {		
						
						if(tmcaseFile.getJoinAppId()==null || materialMapper.selectByMaterialWithApplicantId(material,  gcon).size()>0) {
							material.setMaterialId(m.getMaterialId());
							materialMapper.updateByPrimaryKeySelective(material);
							info = materialService.uploadFile(multipartRequest, material, gcon, token);
							ReturnInfo info2 = materialService.checkFile(material,tmcaseFile.getFileName());
							if(!info2.getSuccess()) {	
								Integer materialId=material.getMaterialId();
								materialMapper.deleteByPrimaryKey(materialId);
								return info2;
							}
							if(tmcaseFile.getFileName()!=null&&tmcaseFile.getFileName()==24){//24代表图样
								Material material2 = (Material) info.getData();
								String address =material2.getAddress();
								TradeMarkCase tradeMarkCase = tradeMarkCaseMapper.selectByPrimaryKey(tmcaseFile.getCaseId());
								tradeMarkCase.setImageFile(address);
								tradeMarkCaseMapper.updateByPrimaryKeySelective(tradeMarkCase);
								}
							
							info.setSuccess(true);
							info.setMessage("上传成功");
							return info;
						}	else {
							a++;
						}		
						if(a>0) {
							
							info = materialService.createMaterial(request, material, gcon, token);
							ReturnInfo info2 = materialService.checkFile(material,tmcaseFile.getFileName());
							if(!info2.getSuccess()) {
								Integer materialId=material.getMaterialId();
								materialMapper.deleteByPrimaryKey(materialId);
								return info2;
							}
							if(tmcaseFile.getFileName()!=null&&tmcaseFile.getFileName()==24){//24代表图样
								Material material2 = (Material) info.getData();
								String address =material2.getAddress();
								TradeMarkCase tradeMarkCase = tradeMarkCaseMapper.selectByPrimaryKey(tmcaseFile.getCaseId());
								tradeMarkCase.setImageFile(address);
								tradeMarkCaseMapper.updateByPrimaryKeySelective(tradeMarkCase);
								}
							info.setSuccess(true);
							info.setMessage("上传成功");
							return info;
						}
					}
					else {
						
						info = materialService.createMaterial(request, material, gcon, token);
						
						ReturnInfo info2 = materialService.checkFile(material,tmcaseFile.getFileName());
						if(!info2.getSuccess()) {	
							Integer materialId=material.getMaterialId();
							materialMapper.deleteByPrimaryKey(materialId);
							return info2;
						}
						if(tmcaseFile.getFileName()!=null&&tmcaseFile.getFileName()==24){//24代表图样
							
							Material material2 = (Material) info.getData();
							String address =material2.getAddress();
							
							TradeMarkCase tradeMarkCase = tradeMarkCaseMapper.selectByPrimaryKey(tmcaseFile.getCaseId());
							tradeMarkCase.setImageFile(address);
							tradeMarkCaseMapper.updateByPrimaryKeySelective(tradeMarkCase);
							}
						info.setSuccess(true);
						info.setMessage("上传成功");
						return info;
					}
				}
				
			}else {
				
				
				info = materialService.createMaterial(request, material, gcon, token);
				ReturnInfo info2 = materialService.checkFile(material,tmcaseFile.getFileName());
				if(!info2.getSuccess()) {
					Integer materialId=material.getMaterialId();
					materialMapper.deleteByPrimaryKey(materialId);
					return info2;
				}
				if(tmcaseFile.getFileName()!=null&&tmcaseFile.getFileName()==24){//24代表图样
					Material material2 = (Material) info.getData();
					String address =material2.getAddress();
					
					TradeMarkCase tradeMarkCase = tradeMarkCaseMapper.selectByPrimaryKey(tmcaseFile.getCaseId());
					tradeMarkCase.setImageFile(address);
					
					tradeMarkCaseMapper.updateByPrimaryKeySelective(tradeMarkCase);
					}
				info.setSuccess(true);
				info.setMessage("上传成功");
				return info;
			}
		
		//Material m1 =(Material) info.getData();
						
		return info;
	}
//	private Boolean checkImage(Material material,int fileName) {
//		
//		boolean b = true;
//		if(fileName !=24) {
//			return false;
//		}
//		List<Material> materialNew = materialMapper.selectImageByCaseId(material.getCaseId());
//		if(materialNew != null && materialNew.size()>0) {
//			 b = ImageUtils.checkImagePixel(fileUrl+materialNew.get(0).getAddress());
//		}
//		
//		return b;
//		
//	}
}
