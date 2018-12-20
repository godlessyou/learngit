package com.yootii.bdy.tmcase.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.yootii.bdy.common.Constants;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.material.dao.MaterialMapper;
import com.yootii.bdy.material.dao.MaterialSortMapper;
import com.yootii.bdy.material.model.Material;
import com.yootii.bdy.material.model.MaterialSort;
import com.yootii.bdy.material.service.MaterialService;
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseFilePreMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseJoinAppMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCasePreMapper;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.TradeMarkCaseFilePre;
import com.yootii.bdy.tmcase.model.TradeMarkCaseJoinApp;
import com.yootii.bdy.tmcase.model.TradeMarkCasePre;
import com.yootii.bdy.tmcase.service.TradeMarkCaseFilePreService;
import com.yootii.bdy.util.ImageUtils;

@Service
public class TradeMarkCaseFilePreServiceImpl implements TradeMarkCaseFilePreService{
	
	private static String tmcase_uploadDir = Constants.upload_dir+Constants.tmcaseFileDir;
	private static String tmcase_image = Constants.upload_dir+Constants.tmcaseImageDir;
	
	@Resource
	private TradeMarkCaseFilePreMapper tradeMarkCaseFilePreMapper;
	
	@Resource
	private TradeMarkCasePreMapper tradeMarkCasePreMapper;
	
	@Resource
	private TradeMarkCaseJoinAppMapper tradeMarkCaseJoinAppMapper;
	
	@Resource
	private MaterialService materialService;
	
	@Resource
	private MaterialMapper materialMapper;
	@Resource
	private MaterialSortMapper materialSortMapper;
	@Value("${fileUrl}")  
	private String fileUrl;
	@Override
	public ReturnInfo uploadCaseFile(HttpServletRequest request,
			TradeMarkCasePre tradeMarkCasePre, TradeMarkCaseFilePre tmcaseFile, GeneralCondition gcon, Token token,Integer applicantId) {
		ReturnInfo rtnInfo= new ReturnInfo();
		Integer custId = tradeMarkCasePre.getCustId();
		Integer agencyId = tradeMarkCasePre.getAgencyId();
		if(custId==null||agencyId==null){
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("客户和代理所ID不能为空");
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
		//如果前端传入caseId,则不需要插入
		if(tradeMarkCasePre.getId()==null){
			//往tmcase数据库插入一条
			try{
				tradeMarkCasePreMapper.deleteByCustIdAndAgencyId(custId, agencyId);//如果有相同custId和agencyId的数据，则删除
				tradeMarkCasePreMapper.insertSelective(tradeMarkCasePre);
			}catch(Exception e){
				e.printStackTrace();
				rtnInfo.setSuccess(false);
				return rtnInfo;
			}
		}else{
			tradeMarkCasePreMapper.updateByPrimaryKeySelective(tradeMarkCasePre);
			
		}
		if(tmcaseFile.getFileName()!=null&&tmcaseFile.getFileName()>100&&tmcaseFile.getFileName()<105){//101~104是共同申请人文件
			if(tmcaseFile.getJoinAppId()==null){
				//新建一条共同申请人信息
				TradeMarkCaseJoinApp record = new TradeMarkCaseJoinApp();
				record.setCustId(custId);
				record.setAgencyId(agencyId);
				record.setCasePreId(tradeMarkCasePre.getId());//设置预立案ID
				tradeMarkCaseJoinAppMapper.deleteByCustIdAndAgencyId(custId,agencyId,tradeMarkCasePre.getId());//删除预立案ID小于tradeMarkCasePre.getId()，且caseId为空的数据删除
				tradeMarkCaseJoinAppMapper.insertSelective(record);
				tmcaseFile.setJoinAppId(record.getId());
			}
		}
		try {
			//rtnInfo = uploadFile(request,tradeMarkCasePre, tmcaseFile);
			rtnInfo = uploadFileByMaterial(request,tradeMarkCasePre,tmcaseFile, gcon,token,applicantId);
		} catch (IOException e) {
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("文件上传失败"); 
		}
		if(rtnInfo.getSuccess()==false){
			return rtnInfo;
		}
		
		Material material2 = (Material) rtnInfo.getData();
		String address =material2.getAddress();
		
		if(tmcaseFile.getFileName()!=null&&tmcaseFile.getFileName()==24){//24代表图样
			tradeMarkCasePreMapper.updateByPrimaryKeySelective(tradeMarkCasePre);//修改图样的路径
		}
		tmcaseFile.setCaseId(tradeMarkCasePre.getId());
		//检查该文件的名称是否重复
		/*List<TradeMarkCaseFilePre> tradeMarkCaseFilePres = tradeMarkCaseFilePreMapper.selectByCaseId(tradeMarkCasePre.getId());
		for(TradeMarkCaseFilePre tmCaseFilePre:tradeMarkCaseFilePres){
			if(tmcaseFile.getFileName().equals(tmCaseFilePre.getFileName())
					&&(tmcaseFile.getJoinAppId()==tmCaseFilePre.getJoinAppId())){//如果该案件有相同文件名称的文件,并且不是共同申请人文件或者共同申请人ID相同，则更新
				tmcaseFile.setId(tmCaseFilePre.getId());
				tradeMarkCaseFilePreMapper.updateByPrimaryKeySelective(tmcaseFile);
				Map<String, Integer> data = new HashMap<String, Integer>();
				data.put("caseId", tradeMarkCasePre.getId());
				data.put("joinAppId", tmcaseFile.getJoinAppId());
				rtnInfo.setSuccess(true);
				rtnInfo.setData(data);
				return rtnInfo;
			}
		}
		tmcaseFile.setId(null);//前端传入的id，不能够判别是caseId还是caseFileId,在这把文件id设置为空，避免用到前端传入的caseid
		tradeMarkCaseFilePreMapper.insertSelective(tmcaseFile);*/
		
		
		//Modification start, by yang guang, 2018-10-16
		//返回文件的地址
		Map<String, String> data = new HashMap<String, String>();
		Integer caseId=tradeMarkCasePre.getId();		
		if (caseId!=null){		
			data.put("caseId", caseId.toString());
		}
		Integer joinAppId=tmcaseFile.getJoinAppId();	
		if (joinAppId!=null){		
			data.put("joinAppId", joinAppId.toString());
		}	
		if(address!=null){
			data.put("address", address);
		}
		//Modification end
		
		rtnInfo.setSuccess(true);
		rtnInfo.setMessage("上传成功");
		rtnInfo.setData(data);
		return rtnInfo;
	}
	
	private ReturnInfo uploadFile(HttpServletRequest request,TradeMarkCasePre tradeMarkCasePre,TradeMarkCaseFilePre tmcaseFile) throws IOException {
		ReturnInfo rtnInfo= new ReturnInfo();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFiles("file").get(0);
		Properties properties =  new  Properties();
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("config.properties");  
		properties.load(inputStream);
		String case_maxfilesize = properties.getProperty("case.maxfilesize");
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
				String fileName = file.getOriginalFilename();
				//当前时间戳
				Long timestamp = System.currentTimeMillis();
				String filePath ="";
				String databasePath = "";
				if(tmcaseFile.getFileName()!=null&&tmcaseFile.getFileName()==24){//24代表图样
					filePath =tmcase_image+tradeMarkCasePre.getCustId()+"/"+ timestamp+"_"+fileName;
					databasePath = "/"+Constants.uploadDir+ Constants.tmcaseImageDir +tradeMarkCasePre.getCustId()+"/"+ timestamp+"_"+fileName;
					tradeMarkCasePre.setImageFile(databasePath);
				}else{
					filePath =tmcase_uploadDir+tradeMarkCasePre.getCustId()+"/"+ timestamp+"_"+fileName;
					databasePath = "/"+Constants.uploadDir+ Constants.tmcaseFileDir+tradeMarkCasePre.getCustId()+"/"+ timestamp+"_"+fileName;
				}
//				tmcaseFile.setFileName(fileName);
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
	private ReturnInfo uploadFileByMaterial(HttpServletRequest request,TradeMarkCasePre tradeMarkCasePre,TradeMarkCaseFilePre tmcaseFile, GeneralCondition gcon, Token token, Integer applicantId) throws IOException {
		//上传预立案
		ReturnInfo info  =new ReturnInfo();
		Material material = new Material();
		Integer fileName=tmcaseFile.getFileName();
		if(fileName != null && fileName.toString().matches("[0-9]+")) {
			MaterialSort materialSort =new MaterialSort();
			materialSort.setFileName(fileName);
			List<MaterialSort> sort = materialSortMapper.selectByMaterialSort(materialSort, gcon);
			material.setSortId(sort.get(0).getId());
			material.setMaterialSort(sort);
		}	
		
//		String subject = "precase"+","+"fileName : "+tmcaseFile.getFileName()+","+"joinAppId : "+tmcaseFile.getJoinAppId();
		
		material.setCustId(tradeMarkCasePre.getCustId());
		material.setPrecase(1);		
		material.setFileName(fileName);
		material.setJoinAppId(tmcaseFile.getJoinAppId());
		
		String subject="递交官方";
		
		String name=fileName.toString();		
	    if (fileName!=null && Constants.fileNameList.containsKey(name)){		
			subject="官方通知";
		}
	    
		material.setSubject(subject);
		material.setCaseId(tradeMarkCasePre.getId());
		
		//added start, 2018-06-19
		material.setDocDate(tradeMarkCasePre.getDocDate());
		//added end
		
		if(tmcaseFile.getFileType()==null){
			material.setSubject("递交官方");
		}		
		material.setApplicantId(applicantId);
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFiles("attachFile").get(0);		
		if (file != null && file.getSize() != 0) {			
			if (file.getOriginalFilename()!=null) {
				material.setTitle(file.getOriginalFilename());
			}else {
				material.setTitle("");
			}
			
		}
		
		List<Material> materials = materialMapper.selectByMaterialWithApplicantId(material,  gcon);
		if(material.getCaseId() !=null && materials!=null&&materials.size()>0) {
				for(Material m : materials) {
					int a= 0;
					m.setJoinAppId(tmcaseFile.getJoinAppId());
//					m.setSubject("joinAppId : "+tmcaseFile.getJoinAppId());	
					if(tmcaseFile.getFileName()!=null&&tmcaseFile.getFileName()==24){//24代表图样
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
						tradeMarkCasePre.setImageFile(address);
						tradeMarkCasePreMapper.updateByPrimaryKeySelective(tradeMarkCasePre);
						}
					//如果该案件有相同文件名称的文件,并且不是共同申请人文件或者共同申请人ID相同，则更新
					if(m.getTitle().equals(material.getTitle())) {	
						
						if(tmcaseFile.getJoinAppId()==null || materialMapper.selectByMaterialWithApplicantId(material,  gcon).size()>0) {
							material.setMaterialId(m.getMaterialId());
							materialMapper.updateByPrimaryKeySelective(material);
							info=materialService.uploadFile(multipartRequest, material, gcon, token);
							ReturnInfo info2 = materialService.checkFile(material,tmcaseFile.getFileName());
							if(!info2.getSuccess()) {
								Integer materialId=material.getMaterialId();
								materialMapper.deleteByPrimaryKey(materialId);
								return info2;
							}
							if(tmcaseFile.getFileName()!=null&&tmcaseFile.getFileName()==24){//24代表图样
								Material material2 = (Material) info.getData();
								String address =material2.getAddress();
								tradeMarkCasePre.setImageFile(address);
								tradeMarkCasePreMapper.updateByPrimaryKeySelective(tradeMarkCasePre);
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
								tradeMarkCasePre.setImageFile(address);
								tradeMarkCasePreMapper.updateByPrimaryKeySelective(tradeMarkCasePre);
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
							tradeMarkCasePre.setImageFile(address);
							tradeMarkCasePreMapper.updateByPrimaryKeySelective(tradeMarkCasePre);
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
					tradeMarkCasePre.setImageFile(address);
					tradeMarkCasePreMapper.updateByPrimaryKeySelective(tradeMarkCasePre);
					}
				info.setSuccess(true);
				info.setMessage("上传成功");
				return info;
			}
		
		//Material m1 =(Material) info.getData();
						
		return info;
	}
	
	
	
	
}
