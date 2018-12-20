package com.yootii.bdy.tmcase.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.jsoup.helper.StringUtil;
import org.springframework.stereotype.Service;

import com.yootii.bdy.agency.dao.AgencyUserMapper;
import com.yootii.bdy.agency.service.AgencyService;
import com.yootii.bdy.common.Constants;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.Globals;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.material.dao.MaterialMapper;
import com.yootii.bdy.material.dao.MaterialSortMapper;
import com.yootii.bdy.material.dao.MaterialVersionMapper;
import com.yootii.bdy.material.model.Material;
import com.yootii.bdy.material.model.MaterialSort;
import com.yootii.bdy.material.model.MaterialVersion;
import com.yootii.bdy.process.service.ProcessService;
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.task.model.ReturnToDoAmount;
import com.yootii.bdy.task.model.UserTask;
import com.yootii.bdy.task.service.TradeMarkCaseTaskService;
import com.yootii.bdy.task.service.Impl.TaskCommonImpl;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseGuanWenMapper;
import com.yootii.bdy.tmcase.dao.TradeMarkCaseMapper;
import com.yootii.bdy.tmcase.service.GuanWenService;
import com.yootii.bdy.tmcase.model.TradeMarkCase;
import com.yootii.bdy.tmcase.model.TradeMarkCaseGuanWen;
import com.yootii.bdy.util.DateTool;
import com.yootii.bdy.util.FileUtil;
import com.yootii.bdy.util.ImageSimilarity;
import com.yootii.bdy.util.PdfBoxUtil;
import com.yootii.bdy.util.ServiceUrlConfig;


@Service
public class GuanWenServiceImpl implements GuanWenService{
	
	public static Logger logger = Logger.getLogger(GuanWenServiceImpl.class);
	
	@Resource
	private TradeMarkCaseGuanWenMapper tradeMarkCaseGuanWenMapper;
	
	@Resource
	private TradeMarkCaseMapper tradeMarkCaseMapper;
	
	@Resource
	private MaterialMapper materialMapper;
	
	@Resource
	private MaterialSortMapper materialSortMapper;
	
	@Resource
	private MaterialVersionMapper materialVersionMapper;
	
	
	
	@Resource
	private AgencyUserMapper agencyUserMapper;
	
	@Resource
	private TaskCommonImpl taskCommonImpl;
	
	@Resource
	private TradeMarkCaseTaskService tradeMarkCaseTaskService;
	
	@Resource
	private ServiceUrlConfig serviceUrlConfig;
	
	
	@Resource
	private ProcessService processService;
	
	
	
	public Object processGuanWen() {
		ReturnInfo info = new ReturnInfo();
		
		try {
			//从邮件中获取的官文保存在这个目录下			
			String guanwenDir=Constants.guanwen_download_dir;
			FileUtil.createFolderIfNotExists(guanwenDir);
			
			String[] extNames={"pdf"};
			List<String> FilePathList=new ArrayList<String>();
			
			//获取该目录下的所有文件
			FileUtil.getFile(guanwenDir, extNames, FilePathList);
			
			//pdf中有商标局的公章的图片，以及二维码的图片，需要比较后不保存这两个图片
			String gongzhangFile = Constants.app_dir+Constants.guanwen_dir+"/default/gongzhang.jpg";
			String erweimaFile = Constants.app_dir+Constants.guanwen_dir+"/default/erweima.jpg";
			
			Integer agencyId=1;
//			List<UserTask> userTaskList =getUserTaskList(agencyId);
			
			
			
			for (String filePath:FilePathList){
				//读取每个pdf文件，获取其内容
				TradeMarkCaseGuanWen tradeMarkCaseGuanWen=PdfBoxUtil.readPdf(filePath, gongzhangFile, erweimaFile);
				
				String appCnName=tradeMarkCaseGuanWen.getAppCnName();
				String goodClasses=tradeMarkCaseGuanWen.getGoodClasses();
				String appNumber=tradeMarkCaseGuanWen.getAppNumber();
				
				if (filePath.indexOf("\\") > -1) {
					filePath = filePath.replaceAll("\\\\", "/");
				}
				tradeMarkCaseGuanWen.setDocPath(filePath);
				
				TradeMarkCase tmcase=new TradeMarkCase();
				tmcase.setAppNumber(appNumber);
				
				//根据申请号查询案件
				List<TradeMarkCase> list=tradeMarkCaseMapper.selectByAppNameGoodClass(tmcase);
				
				if (list==null || list.size()==0){
					tmcase.setAppNumber(null);
					tmcase.setAppCnName(appCnName);
					tmcase.setGoodClasses(goodClasses);
					
					//根据申请人、商品服务类别查询符合条件的案件
					list=tradeMarkCaseMapper.selectByAppNameGoodClass(tmcase);
					
					if (list==null || list.size()==0){
						continue;
					}	
				}
				
				processGuanWen(tradeMarkCaseGuanWen);
				
				// 找到官文对应的案件
				internalProcess(tradeMarkCaseGuanWen, list);
							
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			info.setSuccess(false);
			info.setMessage(e.getMessage());
			info.setMessageType(Globals.MESSAGE_TYPE_OPERATION_EXCEPTION);
		}
		
		info.setSuccess(true);
		info.setMessage("创建成功");
		return info;
	}
	
	
	
	private void processGuanWen(TradeMarkCaseGuanWen tradeMarkCaseGuanWen){
		Date date=new Date();
		
		String currentDate=DateTool.getDate(date);
		
		String fileUrl=serviceUrlConfig.getFileUrl();
		
		String guanWenDir=fileUrl+Constants.guanwen_dir+"/"+currentDate;
		
		FileUtil.createFolderIfNotExists(guanWenDir);
				
		String docPath=tradeMarkCaseGuanWen.getDocPath();
		
		int pos=docPath.lastIndexOf("/");
		
		String orgfileName=docPath.substring(pos+1);
		
		String docFile=guanWenDir+"/"+orgfileName;
		
		FileUtil.copyFile(docPath, docFile);
		
		tradeMarkCaseGuanWen.setDocFile(docFile);
		
	}
	
	
	public void internalProcess(TradeMarkCaseGuanWen tradeMarkCaseGuanWen, List<TradeMarkCase> list) throws Exception {
		
		//资料存储的根目录
		String fileUrl=serviceUrlConfig.getFileUrl();
		String fileName="332";
		String docFile=tradeMarkCaseGuanWen.getDocFile();
		String docDate=tradeMarkCaseGuanWen.getDocDate();
				
				
		List<UserTask> userTaskList=null;
		int size=list.size();
		if (size==1){
			//如果符合条件的案件只有一个，那么当前官文属于该案件。
			TradeMarkCase tmCase=list.get(0);
			Integer cId=tmCase.getId();
			Integer agencyId=tmCase.getAgencyId();
			String caseId=cId.toString();
			tradeMarkCaseGuanWen.setCaseId(cId);
			tradeMarkCaseGuanWen.setSimilarity(100);
			tradeMarkCaseGuanWenMapper.insertSelective(tradeMarkCaseGuanWen);
			
						
			createMaterial(tmCase, docFile, fileName,docDate);
			
			//获取该案件的处于流程中的task的Id
			String caseTypeIds="1,2";
			List<Map<String, Object>> taskDataList=processService.queryTaskProperty(caseId, caseTypeIds);
			
			if (taskDataList!=null && taskDataList.size()>0){
				Map<String, Object> data=taskDataList.get(0);					
				String taskId=(String)data.get("taskId");	
				
				String appNumber=tradeMarkCaseGuanWen.getAppNumber();
				Date appDate=tradeMarkCaseGuanWen.getAppDate();	
				TradeMarkCase tradeMarkCase=new TradeMarkCase();
				tradeMarkCase.setAppNumber(appNumber);
				tradeMarkCase.setAppDate(appDate);
				tradeMarkCaseTaskService.autoSaveGuanWen(fileName, tradeMarkCase, taskId);	
			}
			
			return;
		}
		
		
		//从官文中提取的商标图样所在的路径
		String imagePath=tradeMarkCaseGuanWen.getImagePath();		
		if (imagePath==null || imagePath.equals("")){
			return;
		}
		File file=new File(imagePath);
		if (!file.exists()){
			return;
		}
		
		ImageSimilarity fp1 = new ImageSimilarity(
				ImageIO.read(new File(imagePath)));
		
		TradeMarkCase result=null;
		float lastResult=0;
		
		//如果有多个符合条件的案件，根据官文中提取的商标图样与案件的商标图样进行对比，
		//查找出相似性最高的案件
		for(TradeMarkCase tradeMarkCase:list){
			Integer caseId=tradeMarkCase.getId();
			String trademarkImage=null;
			List<Material> materialList = materialMapper.selectImageByCaseId(caseId);
			if(materialList == null ) {
				continue;
			}
			//获取案件的商标图样
			for(Material material:materialList){					
				trademarkImage=material.getAddress();
				if (trademarkImage!=null && !trademarkImage.equals("")){		
					trademarkImage=fileUrl+trademarkImage;					
					break;
				}
			}
				
			//该案件中没有商标图样
			if (trademarkImage==null){
				continue;
			}
			File file2=new File(trademarkImage);
			if (!file2.exists()){
				continue;
			}
			ImageSimilarity fp2 = new ImageSimilarity(
					ImageIO.read(new File(trademarkImage)));

			// 两个图片进行相似性比较
			float compResult = fp1.compare(fp2);
			
			// 留下相似性最高的
			if (compResult > lastResult) {
				result=tradeMarkCase;	
				lastResult=compResult;
			}
		}
		
		if (result!=null){
			Integer cId=result.getId();			
			String caseId=cId.toString();
			int similarity=(int)lastResult*100;
			tradeMarkCaseGuanWen.setCaseId(cId);
			tradeMarkCaseGuanWen.setSimilarity(similarity);
			tradeMarkCaseGuanWenMapper.deleteByPrimaryKey(cId);
			tradeMarkCaseGuanWenMapper.insertSelective(tradeMarkCaseGuanWen);
		
			
			createMaterial(result, docFile, fileName,docDate);
			
			//获取该案件的处于流程中的task的Id
			String caseTypeIds="1,2";
			List<Map<String, Object>> taskDataList=processService.queryTaskProperty(caseId, caseTypeIds);
			
			if (taskDataList!=null && taskDataList.size()>0){
				Map<String, Object> data=taskDataList.get(0);				
				String taskId=(String)data.get("taskId");
				
				String appNumber=tradeMarkCaseGuanWen.getAppNumber();
				Date appDate=tradeMarkCaseGuanWen.getAppDate();								
				TradeMarkCase tradeMarkCase=new TradeMarkCase();
				tradeMarkCase.setAppNumber(appNumber);
				tradeMarkCase.setAppDate(appDate);
				tradeMarkCaseTaskService.autoSaveGuanWen(fileName, tradeMarkCase, taskId);				
			}							
			
		}
	
	}

	
	
	public List<UserTask> getUserTaskList(Integer agencyId) {
		
		List<Integer> userIdList=agencyUserMapper.selectUserIdByAgencyId(agencyId);
		
		List<UserTask> userTaskList =new ArrayList<UserTask>();
		
		if(userIdList!=null) {
			//用户代办事项总合
			for(Integer id:userIdList) {
				try {
					String userId=id.toString();					
					// 获取用户/客户的待办任务的Id的集合
					List<UserTask> list = taskCommonImpl.findUserTask(userId,0);
					
					for(UserTask task:list){
						String taskId=task.getTaskId();
						boolean hasSameOne=false;
						for(UserTask userTask:userTaskList){
							String userTaskId=userTask.getTaskId();
							if(taskId!=null && userTaskId!=null && taskId.equals(userTaskId)){
								hasSameOne=true;
								break;
							}							
						}
						if (!hasSameOne){
							userTaskList.add(task);
						}						
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
			
		}	
		
		return userTaskList;
	}
	

	//保存官方通知文件
	private void createMaterial(TradeMarkCase tradeMarkCase,String docFile,String fileName, String docDate){
		String appFileName = docFile.substring(docFile.lastIndexOf("/")+1);
		String address = docFile.substring(docFile.indexOf( Constants.guanwen_dir));
		
		int fileSize = FileUtil.getFileSize(docFile);
		
		Material material = new Material();
		String fullname = "system";
		material.setCreater(fullname);
		material.setModifier(fullname);
		material.setVersionNo(1);
		material.setTitle(appFileName);
		material.setCustId(tradeMarkCase.getCustId());
		if(fileName!=null && StringUtil.isNumeric(fileName)){		
			Integer fileNameInt=new Integer(fileName);
			material.setFileName(fileNameInt);
		}
//		material.setSubject("fileName : "+fileName+",joinAppId : null,官方通知");
		material.setFormat("pdf");
		material.setStatus(1);
		material.setTmNumber(tradeMarkCase.getAppNumber());
		material.setCaseId(tradeMarkCase.getId());
		material.setAddress(address);
		material.setSize(fileSize);
		material.setSortId(1);		
		material.setFileName(Integer.parseInt(fileName));
		
		Date documentDate=DateTool.StringToDate(docDate);
		material.setDocDate(documentDate);
		
		MaterialSort materialSort =new MaterialSort();
		materialSort.setFileName(Integer.parseInt(fileName));
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
		mv.setCreater(fullname);
		mv.setFormat(material.getFormat());
		mv.setAddress(material.getAddress());
		mv.setMaterialId(material.getMaterialId());
		mv.setSize(material.getSize());
		mv.setVersionNo(material.getVersionNo());
		materialVersionMapper.insertSelective(mv);
	}
	

}
