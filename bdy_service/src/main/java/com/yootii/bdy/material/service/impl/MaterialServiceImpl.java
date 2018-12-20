package com.yootii.bdy.material.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.yootii.bdy.common.Constants;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.customer.model.Applicant;
import com.yootii.bdy.customer.model.Customer;
import com.yootii.bdy.customer.service.ApplicantService;
import com.yootii.bdy.material.dao.MaterialMapper;
import com.yootii.bdy.material.dao.MaterialSortMapper;
import com.yootii.bdy.material.dao.MaterialVersionMapper;
import com.yootii.bdy.material.model.Material;
import com.yootii.bdy.material.model.MaterialCondition;
import com.yootii.bdy.material.model.MaterialSort;
import com.yootii.bdy.material.model.MaterialVersion;
import com.yootii.bdy.material.service.MaterialService;
//import com.yootii.bdy.role.service.RoleService;
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCasePreMapper;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.TradeMarkCasePre;
import com.yootii.bdy.tmcase.model.TradeMarkCaseSolr;
import com.yootii.bdy.tmcase.service.TradeMarkCaseService;
import com.yootii.bdy.model.User;
import com.yootii.bdy.util.GraspUtil;
import com.yootii.bdy.util.ImageUtils;
import com.yootii.bdy.util.JsonUtil;
import com.yootii.bdy.util.RequestUtil;
import com.yootii.bdy.util.ServiceUrlConfig;

@Service("materialService")
public class MaterialServiceImpl implements MaterialService {

	@Resource
	private MaterialMapper materialMapper;
	@Resource
	private MaterialSortMapper materialSortMapper;
	@Resource
	private MaterialVersionMapper materialVersionMapper;
	@Resource
	private TradeMarkCaseMapper tradeMarkCaseMapper;
	@Autowired	
	private ServiceUrlConfig serviceUrlConfig;
	@Resource
	private ApplicantService applicantService;
	
	@Resource
	private TradeMarkCaseService tradeMarkCaseService;
	
	@Resource
	private TradeMarkCasePreMapper tradeMarkCasePreMapper;
		
	@Value("${fileUrl}")  
	private String fileUrl;  
	@Override
	@Transactional
	public ReturnInfo createMaterial(HttpServletRequest request,
			Material material, GeneralCondition gcon, Token token) {
		ReturnInfo rinfo = new ReturnInfo();
		// add creater
		material.setCreater(token.getFullname());
		material.setModifier(token.getFullname());
		material.setMaterialId(materialMapper.getMaxId()==null?1:materialMapper.getMaxId() + 1);
		material.setVersionNo(0);

		// check if valid; to do
		String errorMessage = null;
		/*if (material.getApplicantId() == null) {
			errorMessage = "没有申请人";
		}  */
			if (material.getTitle() == null) {
			errorMessage = "没有资源名";
		}

		if (errorMessage != null) {
			rinfo.setSuccess(false);
			rinfo.setMessage("信息不全：" + errorMessage);
			return rinfo;
		}
		
		Integer fileName=material.getFileName();		
		if(fileName != null) {
			MaterialSort materialSort =new MaterialSort();		
			materialSort.setFileName(fileName);
			List<MaterialSort> sort = materialSortMapper.selectByMaterialSort(materialSort, gcon);
			material.setSortId(sort.get(0).getId());
			material.setMaterialSort(sort);
		}
		

		// insert
		materialMapper.insertSelective(material);

		// 上传附件
		rinfo = uploadFile(request, material, gcon, token);
	//	if (!rinfo.getSuccess()) return rinfo;
	//	rinfo.setData(material);
	//	rinfo.setSuccess(true);
	//	rinfo.setMessage("创建资料成功");
		return rinfo;
	}

	@Override
	public ReturnInfo findMaterial(Material material, GeneralCondition gcon,
			Token token) {
		ReturnInfo info = new ReturnInfo();
		List<Material> materials = new ArrayList<Material>();
		long total;

		if (token.isUser()) {
			User user = getUser(token);
			// Set<String> roles =
			// roleService.getRoleByUserId(user.getUserId()); to do
			materials = materialMapper.selectByMaterialWithUser(user, material,
					gcon);
			total = materialMapper.getMaterialCountWithUser(user, material,
					gcon);
		} else {
			Customer customer = getCustomer(token);
			materials = materialMapper.selectByMaterialWithCustomer(customer,
					material, gcon);
			total = materialMapper.getMaterialCountWithCustomer(customer,
					material, gcon);
		}
		info.setData(materials);
		info.setTotal(total);
		info.setCurrPage(gcon.getPageNo());
		info.setSuccess(true);
		info.setMessage("查询资料成功");
		return info;
	}

	@Override
	@Transactional
	public ReturnInfo modifyMaterial(HttpServletRequest request,
			Material material, GeneralCondition gcon, Token token) {
		// add modifier
		ReturnInfo rinfo = new ReturnInfo();
		material.setModifier(token.getFullname());

		// update material properties
		materialMapper.updateByPrimaryKeySelective(material);

		Material material2 = materialMapper.selectByPrimaryKey(material
				.getMaterialId());
		// upload attached file
		rinfo = uploadFile(request, material2, gcon, token);

		rinfo.setSuccess(true);
		rinfo.setMessage("修改资料成功");
		return rinfo;

	}

	@Override
	public ReturnInfo deleteMaterial(Material material, GeneralCondition gcon,
			Token token) {
		ReturnInfo info = new ReturnInfo();

		Material material2 = materialMapper.selectByPrimaryKey(material
				.getMaterialId());
		// delete material
		materialMapper.deleteByPrimaryKey(material.getMaterialId());

		// delete material attach file
		deleteFile(material2.getAddress());
		info.setSuccess(true);
		info.setMessage("删除Material成功");
		return info;
	}

	private void deleteFile(String address) {
		if (address == null || address == "")
			return;

		File file1 = new File(Constants.app_dir + address);
		if (file1.exists() && !file1.isDirectory()) {
			file1.delete();
		}
	}

	@Override
	public ReturnInfo uploadFile(HttpServletRequest request, Material material,
			GeneralCondition gcon, Token token) {

		ReturnInfo rtnInfo = new ReturnInfo();
		if (material == null || material.getMaterialId() == null) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("No meterial privided!");
			return rtnInfo;
		}
		Material materialNew = new Material();
		materialNew.setMaterialId(material.getMaterialId());

		materialNew.setModifier(token.getFullname());
		material = materialMapper.selectByPrimaryKey(material.getMaterialId());
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = multipartRequest.getFiles("attachFile").get(0);

			String address = null;
			if (file.getSize() != 0) {
				material.setVersionNo(material.getVersionNo()+1);
				address = upload(material, file, rtnInfo);
				materialNew.setAddress(address);
				materialNew.setSize((int) file.getSize());
				String suffix = null;
				if (file.getOriginalFilename().lastIndexOf('.') != -1) {
					suffix = file.getOriginalFilename().substring(
							file.getOriginalFilename().lastIndexOf('.') + 1);
				}
				materialNew.setFormat(suffix);
				materialNew.setVersionNo(material.getVersionNo());
				if (rtnInfo.getSuccess()) {
					materialMapper.updateByPrimaryKeySelective(materialNew);
					MaterialVersion mv = new MaterialVersion();
					mv.setCreater(token.getFullname());
					mv.setFormat(materialNew.getFormat());
					mv.setAddress(materialNew.getAddress());
					mv.setMaterialId(materialNew.getMaterialId());
					mv.setSize(materialNew.getSize());
					mv.setVersionNo(materialNew.getVersionNo());
					materialVersionMapper.insertSelective(mv);
					rtnInfo.setSuccess(true);
					rtnInfo.setMessage("上传附件文件成功");
					material = materialMapper.selectByPrimaryKey(material.getMaterialId());
					rtnInfo.setData(material);
				}
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("上传附件失败：" + e.getMessage());
		}
		return rtnInfo;
	}

	private String upload(Material material, MultipartFile uploadFile,
			ReturnInfo rtnInfo) {

		String address = null;
		if (uploadFile != null) {
			// 文件目录
			try {
				String fileName = uploadFile.getOriginalFilename();
				String savePath;

				if (material.getCaseId() != null) {
					address = Constants.material_dir + '/'
							+ material.getCustId() + '/' + material.getCaseId()
							+ '/' + material.getMaterialId() + '/'
							+ material.getVersionNo() + '/';
				} else {
					address = Constants.material_dir + '/'
							+ material.getCustId() + '/' + material.getMaterialId()
							+ '/' + material.getVersionNo() + '/';

				}		
				//savePath = Constants.app_dir + address;
				savePath =fileUrl+ address;
				File file = new File(savePath);
				if (!file.exists())
					file.mkdirs();
				uploadFile.transferTo(new File(savePath + fileName));
				address = address + fileName;
				rtnInfo.setSuccess(true);
				rtnInfo.setMessage("文件上传成功");
			} catch (Exception e) {
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("文件上传失败，失败原因：" + e.getMessage());
			}

		}
		return address;
	}

	@Override
	public Object downloadFile(HttpServletRequest request,
			HttpServletResponse response, Material material,
			GeneralCondition gcon, Token token) {
		ReturnInfo rtnInfo = new ReturnInfo();

		// Map<String, Object> paramMap = new HashMap<String, Object>();
		Material material2 = materialMapper.selectByPrimaryKey(material
				.getMaterialId());

		ResponseEntity<byte[]> fileStream = null;
		try {

			String filePath = null;
			String fileName = null;
			if (material.getVersionNo() != null) {
				MaterialVersion mv = materialVersionMapper
						.selectByVersion(material.getMaterialId(),material.getVersionNo());
				if (mv != null && mv.getAddress() != null
						&& !mv.getAddress().equals("")) {
					filePath = Constants.app_dir + mv.getAddress();
					fileName = filePath
							.substring(filePath.lastIndexOf('/') + 1);
					if (fileName != null) {
						fileName = new String(fileName.getBytes(), "ISO-8859-1");// URLEncoder.encode(fileName,"UTF-8");
					}
				}

			} else {
				if (material2.getAddress() != null
						&& !material2.getAddress().equals("")) {

					//filePath = Constants.app_dir + material2.getAddress();
					filePath = fileUrl + material2.getAddress();
					fileName = filePath
							.substring(filePath.lastIndexOf('/') + 1);
					if (fileName != null) {
						fileName = new String(fileName.getBytes(), "ISO-8859-1");// URLEncoder.encode(fileName,"UTF-8");
					}
				}
			}
			fileStream = RequestUtil.export(filePath, fileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block;
			// rtnInfo=;
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("文件下载失败。 " + e.getMessage());
			return rtnInfo;

		}
		return fileStream;
	}

	/**
	 * 修改资料属性，内部接口。
	 */
	@Override
	public Object updateMaterial(Material material, GeneralCondition gcon,
			Token token) {
		ReturnInfo rinfo = new ReturnInfo();
		material.setModifier(token.getFullname());

		// update material properties
		materialMapper.updateByPrimaryKeySelective(material);

		rinfo.setSuccess(true);
		rinfo.setMessage("修改资料属性成功");
		return rinfo;
	}

	@Override
	public ReturnInfo deleteFile(HttpServletRequest request, Material material,
			GeneralCondition gcon, Token token) {
		ReturnInfo info = new ReturnInfo();
		Material materialNew = new Material();
		materialNew.setMaterialId(material.getMaterialId());
		materialNew.setSize(0);
		materialNew.setAddress("");
		materialNew.setFormat("");
		materialMapper.updateByPrimaryKeySelective(materialNew);
		// 删除可能的附件
		deleteFile(material.getAddress());
		info.setSuccess(true);
		info.setMessage("删除文件成功");
		return info;

	}

	private User getUser(Token token) {
		User user = new User();
		user.setUserId(token.getUserID());
		user.setUsername(token.getUsername());
		user.setFullname(token.getFullname());
		return user;
	}

	private Customer getCustomer(Token token) {
		Customer customer = new Customer();
		customer.setId(token.getCustomerID());
		customer.setUsername(token.getUsername());
		customer.setFullname(token.getFullname());
		return customer;
	}

	@Override
	public ReturnInfo queryMaterialByApp(Material material, GeneralCondition gcon, Token token) {
		ReturnInfo rtnInfo = new ReturnInfo();
		
		List<Material> materials = new ArrayList<Material>();
		/*
		String subject =null;
		if(material.getSubject()!=null) {
//			if(fileName != null ) {
//				subject =material.getSubject()+"fileName : "+fileName;
//				material.setSubject(subject);
//			}
			materials = materialMapper.selectByNewMaterial(material,gcon);
			if(materials!=null) {
				//Material material1 =materials.get(0);
				rtnInfo.setSuccess(true);
				rtnInfo.setData(materials);
				rtnInfo.setMessage("查询成功");
				return rtnInfo;
			}
		}else if(fileName != null ){
//			subject = "fileName : "+fileName;
//			material.setSubject(subject);
			
			material.setFileName(fileName);
			materials = materialMapper.selectByNewMaterial(material,gcon);
			if(materials!=null && materials.size()>0) {
				//Material material1 =materials.get(0);
				rtnInfo.setSuccess(true);
				rtnInfo.setData(materials);
				rtnInfo.setMessage("查询成功");
				return rtnInfo;
			}
		}		
		*/
		
		long total=(long)0;
		
		materials = materialMapper.selectByNewMaterial(material,gcon);
		long count=materialMapper.selectByNewMaterialCount(material, gcon);
		if (count>0){
			total=count;
		}
		rtnInfo.setSuccess(true);
		rtnInfo.setData(materials);
		rtnInfo.setTotal(total);
		rtnInfo.setMessage("查询成功");
		return rtnInfo;	
		
//		if(fileName == null) {
//			materials = materialMapper.selectByMaterialWithApplicantId(material,fileName,
//					 gcon);
//		}
		
		
		
	}

	@Override
	public ReturnInfo createMaterialSimple(Material material, Integer agencyId, GeneralCondition gcon, boolean precase, Token token) throws Exception {
		ReturnInfo rtnInfo = new ReturnInfo();
		Material material1 =new Material();
		String tokenID=token.getTokenID();
		String creater=token.getFullname();
		
		Integer materailId=material.getMaterialId();
		Integer custId=material.getCustId();
		Integer caseId=material.getCaseId();		
		
		if(materailId != null) {
			material1 = materialMapper.selectByPrimaryKey(materailId);
			material1.setCaseId(caseId);
			material1.setMaterialId(null);
			material1.setCreateTime(null);
			material1.setModifyTime(null);			
		}
		
		material1.setCustId(custId);
		material1.setCreater(creater);
		
		if (precase){
			material1.setPrecase(1);
		}
		
		if (caseId==null){
			Integer preCaseId=null;
			TradeMarkCasePre tradeMarkCasePre=new TradeMarkCasePre();		
			TradeMarkCasePre tradeMarkCasePreTemp = tradeMarkCasePreMapper.selectByCustIdAndAgencyId(custId, agencyId);
			if (tradeMarkCasePreTemp==null){				
				tradeMarkCasePre.setCustId(custId);
				tradeMarkCasePre.setAgencyId(agencyId);
				tradeMarkCasePreMapper.deleteByCustIdAndAgencyId(custId, agencyId);//如果有相同custId和agencyId的数据，则删除
				
				tradeMarkCasePreMapper.insertSelective(tradeMarkCasePre);				
			}else{
				tradeMarkCasePre=tradeMarkCasePreTemp;				
			}
			
			preCaseId=tradeMarkCasePre.getId();
			material1.setCaseId(preCaseId);	
			materialMapper.insertSelective(material1);
							
			Integer fileName=material1.getFileName();
			if (fileName!=null && fileName==24){//24代表图样
				String address = material1.getAddress();	
				if (address!=null && !address.equals("")){
					tradeMarkCasePre.setImageFile(address);
					tradeMarkCasePreMapper.updateByPrimaryKeySelective(tradeMarkCasePre);
				}
			}
				
			
			rtnInfo.setSuccess(true);
			rtnInfo.setData(material1);
			rtnInfo.setMessage("资料创建成功");
			return rtnInfo;		
		}
		
		if(caseId!=null ) {
			material1.setCaseId(caseId);
		}
		
		
			
		//原有案件
		TradeMarkCase tmcase = tradeMarkCaseMapper.selectByPrimaryKey(caseId);
		if(tmcase !=null) {
			material1.setCustId(tmcase.getCustId());
			String appCnName=tmcase.getAppCnName();
			if(appCnName != null) {				
				Applicant applicant=applicantService.queryApplicantByAppName(appCnName, tokenID);				
				if (applicant!=null){
					material1.setApplicantId(applicant.getId());
					material1.setApplicantName(appCnName);
				}
			}			
		}else {		
			material1.setApplicantId(material.getApplicantId());			
		}
		
				
		
		List<Integer> fileNames = null;
//		fileNames.add(material1.getSubject());
		Integer fileName=material1.getFileName();
		if (fileName!=null){
			fileNames = new ArrayList<Integer>();
			fileNames.add(fileName);
		}
		
		MaterialCondition materialCondition=new MaterialCondition();
		materialCondition.setCaseId(caseId);
		materialCondition.setFileNames(fileNames);
		
		List<Material> list = materialMapper.selectByCaseIdAndFileNames(materialCondition);
		boolean b=true;
		if(list != null && list.size()>0) {
			for(Material m : list) {
				if(m.getTitle().equals(material1.getTitle())) {
					m.setCreater(token.getFullname());
					m.setCreateTime(null);
					m.setModifyTime(null);
					materialMapper.updateByPrimaryKeySelective(m);
					b=false;
				}
			}
		}
		if(b) {
			materialMapper.insertSelective(material1);
		}
		
		
		rtnInfo.setSuccess(true);
		rtnInfo.setData(material1);
		rtnInfo.setMessage("资料创建成功");
		return rtnInfo;		
	}
	
	
	
	
	
	
	public void createMaterialByCase(TradeMarkCase tmCase, GeneralCondition gcon) throws Exception {
	
		
//		String tokenID=gcon.getTokenID();
		
		Integer custId=tmCase.getCustId();
		Integer caseId=tmCase.getId();	
		String imageFile=tmCase.getImageFile();
		
		if (imageFile==null || imageFile.equals("")){
			return;
		}			
		
		String fromFilePath=Constants.image_dir+imageFile;
		
		File fromFile=new File(fromFilePath);
		if (!fromFile.exists()){
			return;
		}
		
		int pos=imageFile.indexOf("\\");
		if (pos>-1){
			imageFile=imageFile.replaceAll("\\\\", "/");
		}
		
		String fileName=null;
		pos=imageFile.lastIndexOf("/");
		if (pos>-1){
			fileName=imageFile.substring(pos+1);
		}
		
		Material material =new Material();
		material.setCustId(custId);
		material.setCaseId(caseId);
		material.setVersionNo(1);
		material.setTitle(fileName);
		material.setSubject("递交官方");
		
		String creater="system";
		material.setCreater(creater);	
		material.setFileName(24);
		
		String address = Constants.material_dir + '/'
				+ material.getCustId() + '/' + material.getCaseId()
				+ '/' + "1" + '/'
				+ material.getVersionNo() + '/'+fileName;				
		String toFilePath=Constants.app_dir+address;
		
		File toFile=new File(toFilePath);
		FileUtil.copyFile(fromFile, toFile);
		
		long len=toFile.length();
		int size=(int)len;
		
		
		material.setAddress(address);
		material.setFormat("jpg");
		material.setSize(size);		
		
		
		MaterialSort materialSort =new MaterialSort();
		materialSort.setFileName(24);	
		
		List<MaterialSort> sort = materialSortMapper.selectByMaterialSort(materialSort, gcon);
		material.setSortId(sort.get(0).getId());
		material.setMaterialSort(sort);	
				
	
//		String appCnName=tmCase.getAppCnName();
//		if(appCnName != null) {				
//			Applicant applicant=applicantService.queryApplicantByAppName(appCnName, tokenID);				
//			if (applicant!=null){
//				material.setApplicantId(applicant.getId());
//				material.setApplicantName(appCnName);
//			}
//		}			
				
		
		materialMapper.insertSelective(material);	
		
		tmCase.setImageFile(address);
			
	}
	
	

	public ReturnInfo createMaterialPre(Material material, Integer agencyId, GeneralCondition gcon, boolean precase, Token token) throws Exception {
		ReturnInfo rtnInfo = new ReturnInfo();
		Material material1 =new Material();
		String tokenID=token.getTokenID();
		
		Integer custId=material.getCaseId();
		if(material.getMaterialId() != null) {
			material1 = materialMapper.selectByPrimaryKey(material.getMaterialId());
			material1.setCaseId(material.getCaseId());
			material1.setMaterialId(null);
			material1.setCreateTime(null);
			material1.setModifyTime(null);
		}
		if(material.getCaseId()==null ) {
			TradeMarkCasePre tradeMarkCasePreTemp = tradeMarkCasePreMapper.selectByCustIdAndAgencyId(custId, agencyId);
			if (tradeMarkCasePreTemp==null){
				TradeMarkCasePre record=new TradeMarkCasePre();
				record.setCustId(custId);
				record.setAgencyId(agencyId);
				tradeMarkCasePreMapper.insertSelective(record);
			}
			
			material1.setCaseId(material.getCaseId());
		
			//原有案件
			TradeMarkCase tmcase = tradeMarkCaseMapper.selectByPrimaryKey(material.getCaseId());
			if(tmcase !=null) {
				material1.setCustId(tmcase.getCustId());
				String appCnName=tmcase.getAppCnName();
				if(appCnName != null) {
//					String url=serviceUrlConfig.getBdysysmUrl()+"/applicant/queryapplicantbyappcnname?appCnName="+ tmcase.getAppCnName() +"&tokenID="+ token.getTokenID();
//					System.out.println(url);
//					String jsonString = GraspUtil.getText(url);
//					rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);					
//					if (rtnInfo!=null && rtnInfo.getSuccess()){
//						Map<String, Object> data=(Map<String, Object>)rtnInfo.getData();
//						if(data !=null && (Integer) data.get("id")!=null) {
//							Integer appId=(Integer) data.get("id");
//							material1.setApplicantId(appId);
//						}	
//						material1.setApplicantName(tmcase.getAppCnName());						
//					}
					
					Applicant applicant=applicantService.queryApplicantByAppName(appCnName, tokenID);
					
					if (applicant!=null){
						material1.setApplicantId(applicant.getId());
						material1.setApplicantName(appCnName);
					}
				}
				
			}else {
				material1.setCustId(material.getCustId());
				material1.setApplicantId(material.getApplicantId());
				material1.setCaseId(material.getCaseId());
			}
		}
		
		if (precase){
			material1.setPrecase(1);
		}
			
		
		material1.setCreater(token.getFullname());
		List<Integer> fileNames = null;
//		fileNames.add(material1.getSubject());
		Integer fileName=material1.getFileName();
		if (fileName!=null){
			fileNames = new ArrayList<Integer>();
			fileNames.add(fileName);
		}
		
		MaterialCondition materialCondition=new MaterialCondition();
		materialCondition.setCaseId(material.getCaseId());
		materialCondition.setFileNames(fileNames);
		
		List<Material> list = materialMapper.selectByCaseIdAndFileNames(materialCondition);
		boolean b=true;
		if(list != null && list.size()>0) {
			for(Material m : list) {
				if(m.getTitle().equals(material1.getTitle())) {
					m.setCreater(token.getFullname());
					m.setCreateTime(null);
					m.setModifyTime(null);
					materialMapper.updateByPrimaryKeySelective(m);
					b=false;
				}
			}
		}
		if(b) {
			materialMapper.insertSelective(material1);
		}
		
		rtnInfo.setSuccess(true);
		rtnInfo.setData(material1);
		rtnInfo.setMessage("资料创建成功");
		return rtnInfo;
		
	}

	@Override
	public ReturnInfo queryMaterialByTmId(Material material, GeneralCondition gcon, Integer tmId) {
		ReturnInfo rtnInfo = new ReturnInfo();
		List<Material> materials=materialMapper.selectByTmId(material,tmId, gcon);
		Long count =materialMapper.selectCountByTmId(material,tmId, gcon);
		rtnInfo.setSuccess(true);
		rtnInfo.setTotal(count);
		rtnInfo.setData(materials);
		rtnInfo.setMessage("查询成功");
		return rtnInfo;
	}

	@Override
	public ReturnInfo queryMaterialByRegNumber(Material material, GeneralCondition gcon, String regNumber) {
		ReturnInfo rtnInfo = new ReturnInfo();
		TradeMarkCaseSolr tradeMarkCase =new TradeMarkCaseSolr();
		tradeMarkCase.setRegNumber(regNumber);
		ReturnInfo returnInfo = tradeMarkCaseService.queryTradeMarkCaseBySolr(null, null, null, tradeMarkCase, null, null, gcon);
		List<TradeMarkCase> tradeMarkCaselist =(List<TradeMarkCase>) returnInfo.getData();
		if(tradeMarkCaselist != null && tradeMarkCaselist.size()>0) {
			List<Material> materials =new ArrayList<>();
			for(TradeMarkCase  t : tradeMarkCaselist) {
				material.setCaseId(t.getId());
				//为了获取案件的全部文件，设置一个比较大的值
				gcon.setRows(1000);
				List<Material> m=materialMapper.selectByMaterialWithApplicantId(material, gcon);
				materials.addAll(m);
			}
			
			rtnInfo.setData(materials);
			rtnInfo.setTotal(new Long(materials.size()));
		}else {
			rtnInfo.setData(null);
			rtnInfo.setTotal(0L);
		}
		rtnInfo.setMessage("查询成功");
		return rtnInfo;
	}

	@Override
	public ReturnInfo queryMaterialByAppId(Material material, GeneralCondition gcon) {
		ReturnInfo rtnInfo = new ReturnInfo();
		List<Material> materials = materialMapper.selectByappId(material,  gcon);
		rtnInfo.setSuccess(true);
		
		rtnInfo.setData(materials);
		rtnInfo.setMessage("查询成功");
		return rtnInfo;
	}

	@Override
	public ReturnInfo queryMaterialByAppName(String appCnName, String appEnName, GeneralCondition gcon,String tokenID) throws Exception {
		ReturnInfo rtnInfo = new ReturnInfo();
//		Customer customer=new Customer();
		
		
		Integer id =null;
		/*
		String url=serviceUrlConfig.getBdysysmUrl()+"/applicant/queryapplicantbyappname?appCnName="+ appCnName +"&appEnName"+appEnName+"&tokenID="+ tokenID;
//			System.out.println(url);
		String jsonString = GraspUtil.getText(url);
		rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);
		
		if (rtnInfo!=null && rtnInfo.getSuccess()){
			
			Map<String, Object> data=(Map<String, Object>)rtnInfo.getData();
			if (data!=null){
				id=(Integer) data.get("id");	
				
			}
		}
		*/
		
		Applicant applicant=applicantService.queryApplicantByAppName(appCnName, tokenID);
		id=applicant.getId();
		
		Material material =new Material();
		if(id != null) {
			material.setApplicantId(id);
			List<Material> materials = materialMapper.selectByappId(material,  gcon);
			rtnInfo.setSuccess(true);
			
			rtnInfo.setData(materials);
			rtnInfo.setMessage("查询成功");
			return rtnInfo;
		}else {
			rtnInfo.setSuccess(false);						
			rtnInfo.setMessage("查询失败");
			return rtnInfo;
		}
		
		
	}

	@Override
	public ReturnInfo queryMaterialByCaseIdLast(Integer caseId) {
		//根据caseId查询最新录入的官文
		ReturnInfo rtnInfo = new ReturnInfo();
		if(caseId==null){
			rtnInfo.setMessage("案件编号不能为空");
			return rtnInfo;
		}
		Material material = materialMapper.selectByCaseIdLast(caseId);
		rtnInfo.setSuccess(true);
		rtnInfo.setData(material);
		rtnInfo.setMessage("查询成功");
		return rtnInfo;
	}
	
	
	public ReturnInfo checkFile(Material material,int fileName) {
		ReturnInfo info=new ReturnInfo();
		boolean success = true;
		
		long fileSizeLimit=2*1024*1024; //2M
		int minLimit=0;				
		int maxLimit=0;			
		
		String msg=null;
		String imgFormatMsg="请使用jpg文件";
		String imgPixMsg="图片像素不满足官网要求";
		String sizeMsg="文件大小超过2M，不满足官网要求";
		
		if(fileName ==24) {//商标图样的像素大小限制，文件大小限制
			fileSizeLimit=200*1024; //200k
			minLimit=400;				
			maxLimit=1500;
			imgPixMsg="商标图样的像素不满足官网要求";
			sizeMsg="商标图样的大小超过200k，不满足官网要求";
		}
		if(fileName ==1 || fileName ==401 || fileName ==402) {//代理委托书图片的像素大小限制，文件大小限制
			fileSizeLimit=2*1024*1024; //2M
			minLimit=600;				
			maxLimit=4000;
			imgPixMsg="代理委托书的图片像素不满足官网要求";			
		}
		
		
		List<Material> materialNew = null;
		
		
		if(fileName ==24) {
			materialNew = materialMapper.selectImageByCaseId(material.getCaseId());
		}else{
			List<Integer> fileNames=new ArrayList<Integer>();
			fileNames.add(fileName);
			MaterialCondition materialCondition=new MaterialCondition();
			materialCondition.setCaseId(material.getCaseId());
			materialCondition.setFileNames(fileNames);
			materialNew = materialMapper.selectByCaseIdAndFileNames(materialCondition);
		}
		
		File file= null;
		if(materialNew != null && materialNew.size()>0) {
			String filePath=fileUrl+materialNew.get(0).getAddress();
			int pos=filePath.lastIndexOf(".");
			if (pos>-1){
				String extName=filePath.substring(pos+1);
				if(fileName ==24 || fileName ==1) {
					if (extName.equalsIgnoreCase("pdf")){
						info.setSuccess(false);
						info.setMessage(imgFormatMsg);
						return info;
					}
					if (!extName.equalsIgnoreCase("jpg")){
						info.setSuccess(false);
						info.setMessage(imgFormatMsg);
						return info;
					}
				}
			}
			file=new File(filePath);	
			
			//检查文件的大小
			if (file.exists() && file.isFile()){
				long fileSize=file.length();
				if (fileSize>fileSizeLimit){
					success=false;					
					msg=sizeMsg;
					info.setMessage(msg);	
				}
			}else{
				info.setSuccess(false);
				info.setMessage("要进行检查的文件不存在");
				return info;
			}
			
			//检查图片的像素大小
			if(fileName ==24 || fileName ==1) {
				success = ImageUtils.checkImagePixel(filePath, minLimit, maxLimit);		
				if(!success){
					if (msg==null){
						msg=imgPixMsg;						
					}else{
						msg=msg+" "+imgPixMsg;
					}
					info.setMessage(msg);	
				}
			}	
			
		}
		
		info.setSuccess(success);
		
		if (!success){
			if (file!=null){
				file.delete();
			}
			
		}
		
		return info;
		
	}
}
