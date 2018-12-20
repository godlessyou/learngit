package com.yootii.bdy.downloadapplicant.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import sun.misc.BASE64Encoder;

import com.sun.corba.se.spi.ior.ObjectKey;
import com.yootii.bdy.common.Constants;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.downloadapplicant.model.DocTitleWithBLOBs;
import com.yootii.bdy.downloadapplicant.service.DocTitleService;
import com.yootii.bdy.downloadapplicant.service.DownloadApplicantService;
import com.yootii.bdy.material.dao.MaterialMapper;
import com.yootii.bdy.material.dao.MaterialSortMapper;
import com.yootii.bdy.material.dao.MaterialVersionMapper;
import com.yootii.bdy.material.model.Material;
import com.yootii.bdy.material.model.MaterialSort;
import com.yootii.bdy.material.model.MaterialVersion;
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseCategoryMapper;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.TradeMarkCaseCategory;
import com.yootii.bdy.tmcase.model.TradeMarkCaseJoinApp;
import com.yootii.bdy.tmcase.service.TradeMarkCaseService;
import com.yootii.bdy.util.DateTool;
import com.yootii.bdy.util.FileUtil;
import com.yootii.bdy.util.PdfBoxUtil;
import com.yootii.bdy.util.WordTool;

@Service
public class DownloadApplicantServiceImpl implements DownloadApplicantService{
	
	@Resource
	private TradeMarkCaseService tradeMarkCaseService;
	@Resource
	private MaterialSortMapper materialSortMapper;
	@Resource
	private MaterialMapper materialMapper ;
	@Resource
	private MaterialVersionMapper materialVersionMapper;
	@Resource
	private TradeMarkCaseCategoryMapper tradeMarkCaseCategoryMapper;
	@Resource
	private DocTitleService docTitleService;
	@Value("${fileUrl}")  
	private String fileUrl;
	@Resource
	private VelocityEngine ve ;
	@Override
	public ReturnInfo downloadApplicant(HttpServletRequest request, HttpServletResponse response,Integer caseId, String fileName,
			Token token,GeneralCondition gCondition,Integer isDownload) {
		ReturnInfo info = new ReturnInfo();
		if(caseId==null){
			info.setMessage("案件ID不能为空");
			info.setSuccess(false);
			return info;
		}
		//处理数据
		info = tradeMarkCaseService.queryTradeMarkCaseDetail(caseId);
		TradeMarkCase tradeMarkCase = (TradeMarkCase)info.getData();
		if(tradeMarkCase==null){
			info.setMessage("案件不存在");
			info.setSuccess(false);
			return info;
		} 
		String type = Constants.templateList.get(fileName);
		Map<String, Object> contentMap = new HashMap<String, Object>();
		String agentNum = tradeMarkCase.getAgentNum();
		//下载图样，生成base64编码
		Long currTime = System.currentTimeMillis();
		String workDir = fileUrl + Constants.templateDir+agentNum+"/"+fileName+"/"+currTime;
		FileUtil.createFolderIfNotExists(workDir);
		//如果是商标注册类型  走如下处理
		if("sbzc".equals(type)){
			String imageFile = tradeMarkCase.getImageFile();
			if(imageFile!=null){
				try {
					//downloadImage(workDir,imageFile,contentMap);
					dealImage(imageFile, contentMap);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//处理商品服务
			processTradeMarkCaseCategory(tradeMarkCase, contentMap);
			//处理国家或地区字段  “FG  法兰西共和国”不带前面字母
			processAppCountryOrRegion(tradeMarkCase);
		}
		//如果是 商标评审代理委托书 或者 商标代理委托  类型  走如下流程
		if("sbpsdlwt".equals(type) || "sbdlwt".equals(type)){
			int flag = gCondition.getFlag();
			String caseType = tradeMarkCase.getCaseType();
			if(caseType !=null){
				if(caseType.equals("商标驳回复审") || caseType.equals("商标不予注册复审")){
					if(flag == 1){
						type = "sbpsdlwt_EN";
					}else{
						type = "sbpsdlwt";
					}
				}
			}
			//英文版
			if(flag == 1 && "sbdlwt".equals(type)){
				type = "sbdlwt_EN";
			}
			
			//判断参数
			String appCnName = tradeMarkCase.getAppCnName();
			String appEnName = tradeMarkCase.getAppEnName();
			String country = tradeMarkCase.getAppCountryOrRegion();
			String agenName = tradeMarkCase.getAgenName();
			String goodClass = tradeMarkCase.getGoodClasses();
			String regNumber = tradeMarkCase.getRegNumber();
			String appProval = tradeMarkCase.getApprovalNumber();
			String tmName = tradeMarkCase.getTmName();
			Integer typeId = tradeMarkCase.getCaseTypeId();
			if(appCnName == null || appCnName.equals("")){
				info.setMessage("申请人中文名称不能为空");
				info.setSuccess(false);
				return info;
			}
			if(appEnName == null || appEnName.equals("")){
				info.setMessage("申请人英文名称不能为空");
				info.setSuccess(false);
				return info;
			}
			if(country ==null ||country.equals("")){
				info.setMessage("申请人国籍不能为空");
				info.setSuccess(false);
				return info;
			}
			if( agenName.equals("") || agenName == null  ){
				info.setMessage("代理机构不能为空");
				info.setSuccess(false);
				return info;
			}
			if(goodClass == null || goodClass.equals("")){
				info.setMessage("类别不能为空");
				info.setSuccess(false);
				return info;		
			}
			if(tmName == null || tmName.equals("")){
				info.setMessage("商标名称不能为空");
				info.setSuccess(false);
				return info;
			}
			/*info = this.exclude(info, tradeMarkCase);
			if(!info.getSuccess()){
				return info;
			}*/
			//下载图片生成base64编码
			Integer custId = tradeMarkCase.getCustId();
			Map<String, Object> map = materialMapper.selectByFileAndCase(custId, caseId);
			if(map != null){
				String addr = (String)map.get("address");
				//addr = fileUrl+addr;
				if(addr !=null){
					try{
						//这里的操作主要是根据路径读取到图片后，以base64编码后存到contentMap中
						//根据目录去读取jpg图片后       "D:\\bdydoc"+
						System.err.println(addr);
						FileInputStream fin = new FileInputStream(new File("D:\\bdydoc"+addr));
						byte[] data = new byte[fin.available()];  
						fin.read(data);
						//重编码
						BASE64Encoder encoder = new BASE64Encoder();
						contentMap.put("trademark_base_imgpath", "file:///C:/3D7FA17F/file4591.files/image1.png");
						contentMap.put("trademark_base_img", encoder.encode(data));
					}catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					info.setMessage("上传公章图片");
					info.setSuccess(false);
					return info;
				}
			}else{
				info.setMessage("上传公章图片");
				info.setSuccess(false);
				return info;
			}
		}
		
		//处理共同申请人
		String ifShareTm = tradeMarkCase.getIfShareTm();
		if("是".equals(ifShareTm)){
			List<TradeMarkCaseJoinApp> joinApps = tradeMarkCase.getJoinApps();
			if("sbbgsqrmydz".equals(type)){
				if(joinApps!=null&&joinApps.size()>0){
					List<TradeMarkCaseJoinApp> befJoinApps = new ArrayList<TradeMarkCaseJoinApp>();
					List<TradeMarkCaseJoinApp> aftJoinApps = new ArrayList<TradeMarkCaseJoinApp>();
					for(TradeMarkCaseJoinApp joinApp :joinApps){
						if(joinApp.getType()==1){
							aftJoinApps.add(joinApp);
						}else if(joinApp.getType()==4){
							befJoinApps.add(joinApp);
						}
					}
					contentMap.put("befJoinApps", befJoinApps);
					contentMap.put("aftJoinApps", aftJoinApps);
				}
			}
			contentMap.put("joinApps", joinApps);
		}
		
		//生成模板  生成附件的模板
		String wordTemplate = Constants.velocityDir + type	+ ".vm";
		VelocityContext context = new VelocityContext();

		context.put("tradeMarkCase", tradeMarkCase);
		context.put("contentMap", contentMap);
		StringWriter writer = new StringWriter();
		Template t =null;
		try {
			t= ve.getTemplate(wordTemplate);
		} catch (Exception e) {
			info.setSuccess(false);
			info.setMessage("该类型申请书不存在");
			return info;
		}
		if(t==null){
			info.setSuccess(false);
			info.setMessage("该类型申请书不存在");
			return info;
		}
		// 产生正文
		t.merge(context, writer);
		String htmlContent = writer.toString();

		String appFileName = "";

		appFileName = Constants.templateFileNameList.get(type)+ ".doc";
		String docFile = workDir+ "/" + appFileName;

		// 将内容保存成word文件。
		FileUtil.saveAsFile(docFile, htmlContent);
		
		//尝试生成pdf文件  上一步是生成了word
		String pdfFileName = Constants.templateFileNameList.get(type)+".pdf";
		String toFileName = workDir +"/"+ pdfFileName;
		WordTool.wordToPdf(docFile, toFileName);
		
		//再把pdf转成jpg图片
		String imgName = Constants.templateFileNameList.get(type)+".jpg";
		String disImgName = workDir +"/"+imgName;
		PdfBoxUtil.pdf2multiImage(toFileName,disImgName);
		
		
		//如果是委托书的话，插入资料表中的应是  jpg 图片
		if(type.equals("sbdlwt") || type.equals("sbpsdlwt") || type.equals("sbdlwt_EN") || type.equals("sbpsdlwt_EN")){
			createMaterial(tradeMarkCase, token, disImgName, fileName);
		}else{
			//插入资料管理表中
			createMaterial(tradeMarkCase, token,docFile,fileName);
		}
		//把生成的图片路径返回
		disImgName = disImgName.substring(9, disImgName.length());
		info.setData(disImgName);
		//支持下载
		if(type!="sbdlwt" && type!="sbpsdlwt" && type!="sbdlwt_EN" && type!="sbpsdlwt_EN"){
			if(isDownload ==null){
				isDownload = 1;     //默认返回下载
			}
			//判断是否下载 还是 返回地址
			if(isDownload == 0){
		    	Map<String, Object> map = new HashMap<>();
		    	map.put("docFile", docFile);
		    	info.setData(map);
		    }else{
		    	try{
					FileUtil.fileDownLoad(request, response, appFileName, docFile);
				} catch (ServletException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		}
		info.setSuccess(true);
		info.setMessage("申请书生成完毕");
		return info;
		
	}

	
	
	private void downloadImage(String workDir,String imageFile,Map<String, Object> contentMap) throws Exception{
		if(imageFile==null||"".equals(imageFile)){
			return ; 
		}
		String filePath = FileUtil.saveImage(workDir, imageFile);
		//将图片的base64格式数据，传递到模板
		if(new File(workDir+ File.separator + "image002.jpg").exists()){
			FileInputStream fin = new FileInputStream(new File(workDir+ File.separator + "image002.jpg"));
			byte[] data = new byte[fin.available()];  
			fin.read(data);

			// 对字节数组Base64编码  
			BASE64Encoder encoder = new BASE64Encoder();
			//显示
			contentMap.put("trademark_base_imgpath", "file:///C:/3D7FA17F/file4591.files/image1.png");
			contentMap.put("trademark_base_img", encoder.encode(data));
			fin = new FileInputStream(new File(workDir+ File.separator + "image002.jpg"));
			BufferedImage bufferedImg = ImageIO.read(fin);
			int imgWidth = bufferedImg.getWidth();
			int imgHeight = bufferedImg.getHeight();
//			System.out.println("imgWidth:"+imgWidth+","+imgHeight);
			contentMap.put("fujian_img_width", (imgWidth*(186.0/imgWidth))+"");//157.5宽大概为186,宽为固定的，高随比例改变
			contentMap.put("fujian_img_height", (imgHeight*(186.0/imgWidth))+"");
			fin.close();
		}else{
			FileInputStream fin = new FileInputStream(new File(workDir+ File.separator + "image001.jpg"));
			byte[] data = new byte[fin.available()];  
			fin.read(data);

			// 对字节数组Base64编码  
			BASE64Encoder encoder = new BASE64Encoder();
			//显示
			contentMap.put("trademark_base_imgpath", "file:///C:/3D7FA17F/file4591.files/image1.png");
			contentMap.put("trademark_base_img", encoder.encode(data));
			fin = new FileInputStream(new File(workDir+ File.separator + "image001.jpg"));
			BufferedImage bufferedImg = ImageIO.read(new File(workDir+ File.separator + "image001.jpg"));
			int imgWidth = bufferedImg.getWidth();
			int imgHeight = bufferedImg.getHeight();
//			System.out.println("imgWidth:"+imgWidth+","+imgHeight);
			contentMap.put("fujian_img_width", (imgWidth*(186.0/imgWidth))+"");//157.5宽大概为186,宽为固定的，高随比例改变
			contentMap.put("fujian_img_height", (imgHeight*(186.0/imgWidth))+"");
			fin.close();
		}
	}
	private void processTradeMarkCaseCategory(TradeMarkCase tradeMarkCase,Map<String, Object> contentMap){
		String goodClasses = tradeMarkCase.getGoodClasses();
		String[] classes =  goodClasses.split(";");
		List<TradeMarkCaseCategory> goods = tradeMarkCase.getGoods();
		Map<String, String> goodMap = new HashMap<String, String>();
		if(goods!=null&&goods.size()>0){
			for(String cls: classes){
				if(cls.startsWith("0")){
					cls = cls.substring(1);
				}
				int i=0;
				String goodNames = "";
				for(TradeMarkCaseCategory good : goods){
					String goodClass = good.getGoodClass();
					if(goodClass.startsWith("0")){
						goodClass = goodClass.substring(1);
					}
					String goodName = good.getGoodName();
					if(cls.equals(goodClass)){
						i++;
						if(!"".equals(goodNames)&&goodNames!=null){
							goodNames = goodNames +"；";
						}
						goodNames += i+"、"+goodName;
					}
				}
				goodMap.put(cls, goodNames+"（截止）");
			}
		}
		contentMap.put("trademark_category", goodMap);
	}
	private void processAppCountryOrRegion(TradeMarkCase tradeMarkCase){
		String appCountryOrRegion = tradeMarkCase.getAppCountryOrRegion();
		if(appCountryOrRegion!=null){
			appCountryOrRegion = appCountryOrRegion.substring(appCountryOrRegion.indexOf(" ")).trim();
			tradeMarkCase.setAppCountryOrRegion(appCountryOrRegion);
		}
	}
	private void createMaterial(TradeMarkCase tradeMarkCase,Token token,String docFile,String fileName){
		String appFileName = docFile.substring(docFile.lastIndexOf("/")+1);
		String address = docFile.substring(docFile.indexOf( Constants.templateDir));
		int fileSize = FileUtil.getFileSize(docFile);
		
		Material material = new Material();
		String fullname = token.getFullname();
		material.setCreater(fullname);
		material.setModifier(fullname);
		material.setVersionNo(1);
		material.setTitle(appFileName);
		material.setCustId(tradeMarkCase.getCustId());
		if(fileName!=null && StringUtil.isNumeric(fileName)){		
			Integer fileNameInt=new Integer(fileName);
			material.setFileName(fileNameInt);
		}
//		material.setSubject("fileName : "+fileName+",joinAppId : null,递交官方");
		if(fileName.equals("209")){
			material.setFormat("jpg");
		}else{
			material.setFormat("doc");
		}
		material.setStatus(1);
		material.setTmNumber(tradeMarkCase.getAppNumber());
		material.setCaseId(tradeMarkCase.getId());
		material.setAddress(address);
		material.setSize(fileSize);
		material.setSortId(1);
		MaterialSort materialSort =new MaterialSort();
		if(fileName!=null && StringUtil.isNumeric(fileName)){		
			materialSort.setFileName(Integer.parseInt(fileName));
		}
		List<MaterialSort> sorts = materialSortMapper.selectByMaterialSort(materialSort, null);
		if(sorts!=null&&sorts.size()>0){
			material.setSortId(sorts.get(0).getId());
		}
		GeneralCondition gcon = new GeneralCondition();
		List<Material> materials = materialMapper.selectByMaterialWithApplicantId(material,gcon);
		if(material.getCaseId() !=null && materials!=null&&materials.size()>0) {
			for(Material m : materials) {
				material.setMaterialId(m.getMaterialId());
				material.setVersionNo(m.getVersionNo()+1);
				materialMapper.updateByPrimaryKeySelective(material);
			}
		}else{
			materialMapper.insertSelective(material);
		}
		MaterialVersion mv = new MaterialVersion();
		mv.setCreater(token.getFullname());
		mv.setFormat(material.getFormat());
		mv.setAddress(material.getAddress());
		mv.setMaterialId(material.getMaterialId());
		mv.setSize(material.getSize());
		mv.setVersionNo(material.getVersionNo());
		materialVersionMapper.insertSelective(mv);
	}


	@Override
	public ReturnInfo downLoadRejectRechickDoc(HttpServletRequest request,
			HttpServletResponse response, Integer caseId, String fileName,
			Token token,GeneralCondition gCondition,Integer userId,Integer docTypeId,Integer flag) {
		
		ReturnInfo info = new ReturnInfo();
		Map<String, Object> contentMap = new HashMap<>();
		//案件信息
		info = tradeMarkCaseService.queryTradeMarkCaseDetail(caseId);
		TradeMarkCase tradeMarkCase = (TradeMarkCase)info.getData();
		if(tradeMarkCase==null){
			info.setMessage("案件不存在");
			info.setSuccess(false);
			return info;
		} 
		//商标类别信息
		List<Map<String, Object>>categoryList = tradeMarkCaseCategoryMapper.categoryList(caseId);
		//商标图片
		Integer custId = tradeMarkCase.getCustId();
		Map<String, Object> map = materialMapper.selectByFileAndCase(custId, caseId);
		if(map != null){
			String addr = (String)map.get("address");
			//addr = fileUrl+addr;
			if(addr !=null){
				try{
					//这里的操作主要是根据路径读取到图片后，以base64编码后存到contentMap中
					FileInputStream fin = new FileInputStream(new File("D:\\bdydoc"+addr));
					byte[] data = new byte[fin.available()];  
					fin.read(data);
					//重编码
					BASE64Encoder encoder = new BASE64Encoder();
					contentMap.put("trademark_base_imgpath", "file:///C:/3D7FA17F/file4591.files/image1.png");
					contentMap.put("trademark_base_img", encoder.encode(data));
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		//获取标题数据
		if(docTypeId == null){
			docTypeId = 1;
		}
		List<Map<String, Object>> titles = materialMapper.selectByUserId(userId,docTypeId);
		if(titles == null){
			//如果查询为空，则默认选择一套
			titles = materialMapper.selectByUserId(3,1);
		}
		int year = DateTool.getCurrentYear();
		int month = DateTool.getCurrentMonth();
		int day = DateTool.getCurrentDay();
		contentMap.put("year", year);
		contentMap.put("month", month);
		contentMap.put("day", day);
		//代理文号
		String type = Constants.templateList.get(fileName);
		
		List<String> list = new ArrayList<>();
		if(categoryList.size() !=0){
			for(int i=0;i<categoryList.size();i++){
				Map<String, Object> cate = categoryList.get(i);
				String goodName = (String)cate.get("goodName");
				list.add(goodName);
			}
			contentMap.put("list", list);
		}
		contentMap = this.setContext(contentMap,userId);
		//生成异议理由书模板
		//------------------------------------------------------------------
		Long currTime = System.currentTimeMillis();
		String workDir = fileUrl + Constants.templateDir+currTime;
		FileUtil.createFolderIfNotExists(workDir);
		String wordTemplate = Constants.velocityDir + type +".vm";
		VelocityContext context = new VelocityContext();
		context.put("contentMap", contentMap);
		context.put("tm",tradeMarkCase);
		//---------------------------------------------------------------------------
	
		
		//------------------------------------------
		StringWriter writer = new StringWriter();
		Template template= null;
		try{
			template = ve.getTemplate(wordTemplate);
		}catch (Exception e) {
			e.printStackTrace();
		}
		if(template == null){
			info.setSuccess(false);
			info.setMessage("该类型申请书不存在");
			return info;
		}
		//产生正文
		template.merge(context, writer);
		String htmlContent = writer.toString();
		String appFileName = "";
		appFileName = Constants.templateFileNameList.get(type)+".doc";
		String docFile = workDir+"/"+appFileName;
		System.err.println(docFile);
	
		//将内容保存成word文件
		FileUtil.saveAsFile(docFile, htmlContent);
		if(flag == null){
			flag = 1;
		}
		if(flag == 1){
			//支持下载
			try{
				FileUtil.fileDownLoad(request, response, appFileName, docFile);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			//返回链接
			Map<String, Object> map2 = new HashMap<>();
			map2.put("docFile", docFile);
			info.setData(map2);
		}
		//保存文件
		this.createMaterial(tradeMarkCase, token,docFile,fileName);
		return info;
	}

	//生成标题
	private Map<String, Object> setContext(Map<String, Object> contextMap,Integer userId){
		
		DocTitleWithBLOBs docTitle = new DocTitleWithBLOBs();
		docTitle.setDocTypeId(1);
		Token token = new Token();
		token.setUserID(userId);
		try {
			
			ReturnInfo result = docTitleService.queryDocTitle(docTitle, token);
			List<DocTitleWithBLOBs> docTitles = (List<DocTitleWithBLOBs>)result.getData();
			contextMap.put("docTitles", docTitles);
			List<DocTitleWithBLOBs> docTitles2 = new ArrayList<>();
			List<DocTitleWithBLOBs> docTitles3 = new ArrayList<>();
			List<DocTitleWithBLOBs> docTitles4 = new ArrayList<>();
			for(DocTitleWithBLOBs dt : docTitles){
				System.out.println("1"+dt.getTitle());
				 docTitles2 = dt.getChildren();
				
				for(DocTitleWithBLOBs dt2:docTitles2){
					 docTitles3 = dt2.getChildren();
					for(DocTitleWithBLOBs dt3:docTitles3){
						 docTitles4 = dt3.getChildren();
						for(DocTitleWithBLOBs dt4:docTitles4){
							List<DocTitleWithBLOBs> docTitles5 = dt4.getChildren();
							for(DocTitleWithBLOBs dt5:docTitles5){
								
							}
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contextMap;
	}
	
	
	
	
	
	//上传图章文件
	@Override
	public ReturnInfo upload(HttpServletRequest request, String fileName,
			Integer custId,Integer applicantId,String creater,Integer caseId) {

		ReturnInfo returnInfo = new ReturnInfo();
		//保存图片到服务器磁盘上
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFiles("attachFile").get(0);
		long currentTime = System.currentTimeMillis();
		String workDir = fileUrl+Constants.material_dir+"/"+fileName+"/"+currentTime;
		//图片原始名称
		String picName = file.getOriginalFilename();
		//保存路径
		String savePath = workDir + picName; 
		try{
			//保存到磁盘
			file.transferTo(new File(savePath));
		}catch (Exception e) {
			e.printStackTrace();
		}
		//保存数据到material表中
		//this.createMaterial(tradeMarkCase, token, docFile, fileName);
		//insertMaterial(custId, applicantId, creater, caseId, workDir,picName);
		//返回结果
		return returnInfo;
	}
	
	// 图章  生成materail记录
	public void insertMaterial(Integer custId,Integer applicantId,
			String creater,Integer caseId,String address,String fileName){
		Material material = new Material();
		material.setCustId(custId);
		material.setApplicantId(applicantId);
		material.setTitle(fileName);
		material.setSubject("递交官方");
		//material.setDesc();
		material.setType(2);
		material.setFormat("jpg");
		material.setCreater(creater);
		//material.setCreateTime(createTime);
		//material.setTmNumber(tmNumber);
		material.setCustId(custId);
		material.setAddress(address);
		//material.setSize(size);
		material.setSortId(136);
		material.setFileName(105);
		//判断是否已经有数据  如果有 则只是更新 否则  插入
		GeneralCondition gcon = new GeneralCondition();
		List<Material> materials = materialMapper.selectByMaterialWithApplicantId(material,gcon);
		if(material.getCaseId() !=null && materials!=null&&materials.size()>0) {
			for(Material m : materials) {
				material.setMaterialId(m.getMaterialId());
				material.setVersionNo(m.getVersionNo()+1);
				materialMapper.updateByPrimaryKeySelective(material);
			}
		}else{
			materialMapper.insertSelective(material);
		}
		
	}

	//进行非空排查
	private ReturnInfo exclude(ReturnInfo info,TradeMarkCase tradeMarkCase){
		//进行非空判断，生成的委托书内容 不能为空
		String appCnName = tradeMarkCase.getAppCnName();
		String appEnName = tradeMarkCase.getAppEnName();
		String country = tradeMarkCase.getAppCountryOrRegion();
		String agenName = tradeMarkCase.getAgenName();
		String goodClass = tradeMarkCase.getGoodClasses();
		String regNumber = tradeMarkCase.getRegNumber();
		String appProval = tradeMarkCase.getApprovalNumber();
		String tmName = tradeMarkCase.getTmName();
		Integer typeId = tradeMarkCase.getCaseTypeId();
		if(appCnName == null || appCnName ==""){
			info.setMessage("申请人中文名称不能为空");
			info.setSuccess(false);
			return info;
		}
		if(appEnName == null || appEnName==""){
			info.setMessage("申请人英文名称不能为空");
			info.setSuccess(false);
			return info;
		}
		if(country== null || country ==""){
			info.setMessage("申请人国籍不能为空");
			info.setSuccess(false);
			return info;
		}
		if(agenName == null || agenName ==""){
			info.setMessage("代理机构不能为空");
			info.setSuccess(false);
			return info;
		}
		if(goodClass == null || goodClass ==""){
			info.setMessage("类别不能为空");
			info.setSuccess(false);
			return info;		
		}
		if(tmName == null || tmName ==""){
			info.setMessage("商标名称不能为空");
			info.setSuccess(false);
			return info;
		}
		if(typeId == null){
			info.setMessage("案件类型不能为空");
			info.setSuccess(false);
			return info;
		}
		info.setSuccess(true);
		return info;
	}
	
	private void dealImage(String dir,Map<String, Object> contentMap){
		try{
			FileInputStream fin = new FileInputStream(new File(dir));
			byte[] data = new byte[fin.available()];  
			fin.read(data);
			// 对字节数组Base64编码  
			BASE64Encoder encoder = new BASE64Encoder();
			//显示
			contentMap.put("trademark_base_imgpath", "file:///C:/3D7FA17F/file4591.files/image1.png");
			contentMap.put("trademark_base_img", encoder.encode(data));
			BufferedImage bufferedImg = ImageIO.read(fin);
			int imgWidth = bufferedImg.getWidth();
			int imgHeight = bufferedImg.getHeight();
//			System.out.println("imgWidth:"+imgWidth+","+imgHeight);
			contentMap.put("fujian_img_width", (imgWidth*(186.0/imgWidth))+"");//157.5宽大概为186,宽为固定的，高随比例改变
			contentMap.put("fujian_img_height", (imgHeight*(186.0/imgWidth))+"");
			fin.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 *生成代理委托 
	 */
	@Override
	public ReturnInfo createProxyPicture(HttpServletRequest request, HttpServletResponse response,Integer caseId, String fileName,
			Token token,GeneralCondition gCondition,  String appCnName,String appEnName,
			String country,String agenName,String tmName,String goodClass) {
		ReturnInfo info = new ReturnInfo();
		if(caseId==null){
			info.setMessage("案件ID不能为空");
			info.setSuccess(false);
			return info;
		}
		//处理数据
		info = tradeMarkCaseService.queryTradeMarkCaseDetail(caseId);
		TradeMarkCase tradeMarkCase = (TradeMarkCase)info.getData();
		if(tradeMarkCase==null){
			info.setMessage("案件不存在");
			info.setSuccess(false);
			return info;
		} 
		String type = Constants.templateList.get(fileName);
		Map<String, Object> contentMap = new HashMap<String, Object>();
		String agentNum = tradeMarkCase.getAgentNum();
		//下载图样，生成base64编码
		Long currTime = System.currentTimeMillis();
		String workDir = fileUrl + Constants.templateDir+agentNum+"/"+fileName+"/"+currTime;
		FileUtil.createFolderIfNotExists(workDir);
		//如果是商标注册类型  走如下处理
		if("sbzc".equals(type)){
			String imageFile = tradeMarkCase.getImageFile();
			if(imageFile!=null){
				try {
					dealImage(imageFile, contentMap);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//处理商品服务
			processTradeMarkCaseCategory(tradeMarkCase, contentMap);
			//处理国家或地区字段  “FG  法兰西共和国”不带前面字母
			processAppCountryOrRegion(tradeMarkCase);
		}
		//如果是 商标评审代理委托书 或者 商标代理委托  类型  走如下流程
		if("sbpsdlwt".equals(type) || "sbdlwt".equals(type)){
			int flag = gCondition.getFlag();
			String caseType = tradeMarkCase.getCaseType();
			if(caseType !=null){
				if(caseType.equals("商标驳回复审") || caseType.equals("商标不予注册复审")){
					if(flag == 1){
						type = "sbpsdlwt_EN";
					}else{
						type = "sbpsdlwt";
					}
				}
			}
			//英文版
			if(flag == 1 && "sbdlwt".equals(type)){
				type = "sbdlwt_EN";
			}
			
			
			if(goodClass == null || goodClass.equals("")){
				info.setMessage("类别不能为空");
				info.setSuccess(false);
				return info;		
			}
			if(tmName == null || tmName.equals("")){
				info.setMessage("商标名称不能为空");
				info.setSuccess(false);
				return info;
			}
			
			//把必填数据赋给对象
			tradeMarkCase.setAppCnName(appCnName);
			tradeMarkCase.setAppEnName(appEnName);
			tradeMarkCase.setAppCountryOrRegion(country);
			tradeMarkCase.setAgenName(agenName);
			tradeMarkCase.setTmName(tmName);
			tradeMarkCase.setGoodClasses(goodClass);
			//下载图片生成base64编码
			Integer custId = tradeMarkCase.getCustId();
			Map<String, Object> map = materialMapper.selectByFileAndCase(custId, caseId);
			if(map != null){
				String addr = (String)map.get("address");
				//addr = fileUrl+addr;
				if(addr !=null){
					try{
						//这里的操作主要是根据路径读取到图片后，以base64编码后存到contentMap中
						//根据目录去读取jpg图片后       "D:\\bdydoc"+
						System.err.println(addr);
						FileInputStream fin = new FileInputStream(new File("D:\\bdydoc"+addr));
						byte[] data = new byte[fin.available()];  
						fin.read(data);
						//重编码
						BASE64Encoder encoder = new BASE64Encoder();
						contentMap.put("trademark_base_imgpath", "file:///C:/3D7FA17F/file4591.files/image1.png");
						contentMap.put("trademark_base_img", encoder.encode(data));
					}catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					info.setMessage("上传公章图片");
					info.setSuccess(false);
					return info;
				}
			}else{
				info.setMessage("上传公章图片");
				info.setSuccess(false);
				return info;
			}
		}
		//处理共同申请人
		
		//生成模板  生成附件的模板
		String wordTemplate = Constants.velocityDir + type	+ ".vm";
		VelocityContext context = new VelocityContext();

		context.put("tradeMarkCase", tradeMarkCase);
		context.put("contentMap", contentMap);
		StringWriter writer = new StringWriter();
		Template t =null;
		try {
			t= ve.getTemplate(wordTemplate);
		} catch (Exception e) {
			info.setSuccess(false);
			info.setMessage("该类型申请书不存在");
			return info;
		}
		if(t==null){
			info.setSuccess(false);
			info.setMessage("该类型申请书不存在");
			return info;
		}
		// 产生正文
		t.merge(context, writer);
		String htmlContent = writer.toString();

		String appFileName = "";

		appFileName = Constants.templateFileNameList.get(type)+ ".doc";
		String docFile = workDir+ "/" + appFileName;

		// 将内容保存成word文件。
		FileUtil.saveAsFile(docFile, htmlContent);
		
		//尝试生成pdf文件  上一步是生成了word
		String pdfFileName = Constants.templateFileNameList.get(type)+".pdf";
		String toFileName = workDir +"/"+ pdfFileName;
		WordTool.wordToPdf(docFile, toFileName);
		
		//再把pdf转成jpg图片
		String imgName = Constants.templateFileNameList.get(type)+".jpg";
		String disImgName = workDir +"/"+imgName;
		PdfBoxUtil.pdf2multiImage(toFileName,disImgName);
		
		
		//如果是委托书的话，插入资料表中的应是  jpg 图片
		if(type.equals("sbdlwt") || type.equals("sbpsdlwt") || type.equals("sbdlwt_EN") || type.equals("sbpsdlwt_EN")){
			createMaterial(tradeMarkCase, token, disImgName, fileName);
		}else{
			//插入资料管理表中
			createMaterial(tradeMarkCase, token,docFile,fileName);
		}
		//把生成的图片路径返回
		disImgName = disImgName.substring(9, disImgName.length());
		info.setData(disImgName);
		
		info.setSuccess(true);
		info.setMessage("申请书生成完毕");
		return info;
		
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
