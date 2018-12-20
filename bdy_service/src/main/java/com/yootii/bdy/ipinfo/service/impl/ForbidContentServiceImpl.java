package com.yootii.bdy.ipinfo.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Resource;



import org.springframework.stereotype.Service;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.Globals;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.common.LoginReturnInfo;
import com.yootii.bdy.ipinfo.dao.ForbidContentMapper;
import com.yootii.bdy.ipinfo.model.ForbidContent;
import com.yootii.bdy.ipinfo.service.ForbidContentService;
import com.yootii.bdy.security.service.AuthenticationService;
import com.yootii.bdy.solr.SolrInfo;
import com.yootii.bdy.solr.SolrSend;
import com.yootii.bdy.trademark.dao.TrademarkMapper;
import com.yootii.bdy.trademark.model.Trademark;
import com.yootii.bdy.trademark.model.TrademarkCategory;
import com.yootii.bdy.util.DateTool;
import com.yootii.bdy.util.GraspUtil;
import com.yootii.bdy.util.JsonUtil;
import com.yootii.bdy.util.ServiceUrlConfig;




@Service("ForbidContentService")
public class ForbidContentServiceImpl implements
		ForbidContentService {
	

	@Resource
	private SolrInfo solrInfo ;
	
	@Resource
	private ForbidContentMapper forbidContentMapper;
	@Resource
	protected AuthenticationService authenticationService;
	
	@Resource
	private ServiceUrlConfig serviceUrlConfig;
	
	@Resource
	private TrademarkMapper trademarkMapper;
	
	

	/**
	 * 检测商标名是否是禁止注册内容
	 */
	@Override
	public ReturnInfo checkForbidContent(String  content) {
		// 返回结果对象
		ReturnInfo rtnInfo=new ReturnInfo();

		try {
			
			ForbidContent fc=forbidContentMapper.selectByContent(content);
			if (fc==null) {				
				fc=new ForbidContent();
				fc.setContent(content);
				fc.setResult(1);
			}else{
				fc.setResult(2);
			}
			
			rtnInfo.setData(fc);
			rtnInfo.setSuccess(true);
		} catch (Exception e) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("获取数据异常：" + e.getMessage());
			rtnInfo.setMessageType(Globals.MESSAGE_TYPE_GETDATA_FAILED);
		}

		return rtnInfo;

	}
	
	public ReturnInfo querySameTm(String regNumber,  GeneralCondition gcon)  {

		// 返回结果对象
		ReturnInfo returnInfo=new ReturnInfo();
		
		
		String tokenID=gcon.getTokenID();				
		//http://192.168.0.169:8080/bdy_data/interface/trademark/queryTradeMark?regNumber=9104470&tokenID=1531990836317774
		String url=serviceUrlConfig.getBdydataUrl()+"/trademark/queryTradeMark?regNumber="+ regNumber +"&tokenID="+ tokenID;

		String jsonString;
		try {
			jsonString = GraspUtil.getText(url);
			LoginReturnInfo loginReturnInfo=  JsonUtil.toObject(jsonString, LoginReturnInfo.class);				
			if (loginReturnInfo==null || !loginReturnInfo.getSuccess()){
				String message=loginReturnInfo.getMessage();
				Integer messageType=loginReturnInfo.getMessageType();
				returnInfo.setMessage("没有获取到商标数据, error message:"+ message);
				if (messageType!=null){
					returnInfo.setMessageType(messageType);
				}else{
					returnInfo.setMessageType(Globals.MESSAGE_TYPE_OPERATION_INVALID);
				}
				return returnInfo;
			}
			
			Map<String, Object> data=(Map<String, Object>)loginReturnInfo.getData();							
			if (data==null || data.size()==0){					
				returnInfo.setMessage("已经发布公告的商标数据中没有商标号为"+regNumber+"的商标");						
			}else{				
				String applicantName=(String)data.get("applicantName");
				String applicantAddress=(String)data.get("applicantAddress");
				String applicantEnName=(String)data.get("applicantEnName");
				String applicantEnAddress=(String)data.get("applicantEnAddress");
				String agent=(String)data.get("agent");
				String imgFilePath=(String)data.get("imgFilePath");
				String tmType=(String)data.get("tmType");
				
				String approvalNumber = (String)data.get("approvalNumber");
				Date approvalDate = (Date)data.get("approvalDate");
				
				Trademark tm=new Trademark();
				tm.setApplicantName(applicantName);
				tm.setApplicantAddress(applicantAddress);
				tm.setApplicantEnName(applicantEnName);
				tm.setApplicantEnAddress(applicantEnAddress);
				tm.setAgent(agent);
				tm.setImgFilePath(imgFilePath);
				tm.setTmType(tmType);
				tm.setRegNumber(regNumber);							
				tm.setApprovalNumber(approvalNumber);
				tm.setApprovalDate(approvalDate);
					
				returnInfo.setData(tm);
			}
					
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询相同商标失败");
		}
				
		returnInfo.setSuccess(true);				
		return returnInfo;
	}
	
	
	
	
	
	public ReturnInfo queryTm(String regNumber, String tmType, GeneralCondition gcon)  {

		// 返回结果对象
		ReturnInfo returnInfo=new ReturnInfo();		
		
		String tokenID=gcon.getTokenID();				
		String url=serviceUrlConfig.getBdydataUrl()+"/trademark/queryTradeMark?regNumber="+ regNumber + "&tmType="+ tmType+"&tokenID="+ tokenID;

		String jsonString;
		try {
			jsonString = GraspUtil.getText(url);
			LoginReturnInfo loginReturnInfo=  JsonUtil.toObject(jsonString, LoginReturnInfo.class);	
			if (loginReturnInfo==null || !loginReturnInfo.getSuccess()){
				String message=loginReturnInfo.getMessage();
				Integer messageType=loginReturnInfo.getMessageType();
				returnInfo.setMessage("没有获取到商标数据, error message:"+ message);
				if (messageType!=null){
					returnInfo.setMessageType(messageType);
				}else{
					returnInfo.setMessageType(Globals.MESSAGE_TYPE_OPERATION_INVALID);
				}
				return returnInfo;
			}
			
			Map<String, Object> data=(Map<String, Object>)loginReturnInfo.getData();							
			if (data==null || data.size()==0){					
				returnInfo.setMessage("已经发布公告的商标数据中没有商标号为"+regNumber+"的商标");	
					
			}else{					  
				String tmName=(String)data.get("tmName");
				String applicantName=(String)data.get("applicantName");
				String applicantAddress=(String)data.get("applicantAddress");
				String applicantEnName=(String)data.get("applicantEnName");
				String applicantEnAddress=(String)data.get("applicantEnAddress");
				String agent=(String)data.get("agent");
				String imgFilePath=(String)data.get("imgFilePath");
				String tmType2=(String)data.get("tmType");
				String approvalNumber=(String)data.get("approvalNumber");
				String approvalDate=(String)data.get("approvalDate");
				
				
				Trademark tm=new Trademark();
				tm.setTmName(tmName);
				tm.setApplicantName(applicantName);
				tm.setApplicantAddress(applicantAddress);
				tm.setApplicantEnName(applicantEnName);
				tm.setApplicantEnAddress(applicantEnAddress);
				tm.setAgent(agent);
				tm.setImgFilePath(imgFilePath);
				tm.setTmType(tmType2);
				tm.setApprovalNumber(approvalNumber);
				if (approvalDate!=null){
					Date date=DateTool.StringToDateTime(approvalDate);
					tm.setApprovalDate(date);
				}
				List<Map<String, Object>> tradeMarkCategoryList=(List<Map<String, Object>>)data.get("tradeMarkCategoryList");
				
				if (tradeMarkCategoryList!=null){
					List<TrademarkCategory> trademarkCategories=new ArrayList<TrademarkCategory>();
					
					for(Map<String, Object> map:tradeMarkCategoryList){
						String name=(String)map.get("name");
						String enName=(String)map.get("enName");
						String tmGroup=(String)map.get("tmGroup");
						String tmType3=(String)map.get("tmType");
						String no=(String)map.get("no");
						
						TrademarkCategory tmc=new TrademarkCategory();							
						tmc.setName(name);
						tmc.setEnName(enName);
						tmc.setTmGroup(tmGroup);
						if (tmType3!=null){
							Integer type=new Integer(tmType3);
							tmc.setTmType(type);
						}
						if (no!=null){
							Integer num=new Integer(no);
							tmc.setNo(num);
						}							
						
						trademarkCategories.add(tmc);
					}
					
					tm.setTrademarkCategories(trademarkCategories);
				}
				

				
				returnInfo.setData(tm);
			}
					
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询相同商标失败");
		}
				
		returnInfo.setSuccess(true);				
		return returnInfo;
	}
	
	
	
	public ReturnInfo checkSameTm(String tmName, String tmType, String localSearch, GeneralCondition gcon){
		// 返回结果对象
		ReturnInfo returnInfo=new ReturnInfo();
		
		List<Trademark> trademarklist =new ArrayList<Trademark>();

		long total=0;
		
		try {	
			
			if (tmName==null || tmName.equals("")){
				returnInfo.setSuccess(true);
				returnInfo.setMessage("tmName为空，不进行查询");
				returnInfo.setTotal(total);
				return returnInfo;
			}
			
			int len=tmName.length();
			if (len<2){
				returnInfo.setSuccess(false);
				returnInfo.setMessage("不支持对长度小于2的商标文字进行查询");
				returnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
				return returnInfo;
			}
			if (tmName.equals("图形")){
				returnInfo.setSuccess(false);
				returnInfo.setMessage("不支持对商标文字为图形的进行查询");
				returnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
				return returnInfo;
			}
			
			
			if (localSearch==null || localSearch.equals("")){
				returnInfo.setSuccess(false);
				returnInfo.setMessage("参数localSearch不能为空");
				returnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
				return returnInfo;
			}
//			if (localSearch.equals("2")){
//				int len2=tmType.length();
//				if (len2>1){
//					returnInfo.setSuccess(false);
//					returnInfo.setMessage("每次只允许从已经发布公告的商标数据中查询一个国际分类的商标");
//					returnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
//					return returnInfo;
//				}
//			}
			
						
//			list=trademarkMapper.selectByTmName(tmName);
			
			
			String message=null;
			if (localSearch.equals("1")){
				SolrSend solr = new SolrSend();
				Trademark trademark=new Trademark();
				trademark.setTmName(tmName);
				
				returnInfo = solr.selectTm(gcon, null, null,trademark, null, null, solrInfo, null, returnInfo);
				total=returnInfo.getTotal();
				trademarklist =(List<Trademark>)returnInfo.getData();
				if (trademarklist!=null && trademarklist.size()>0 ){
					//如果不是查询所有国际分类，那么剔除其它国际分类的商标，返回指定国际分类的商标
					if (tmType!=null && !tmType.equals("all")){
						Iterator<Trademark> it = trademarklist.iterator();
						while (it.hasNext()) {
							Trademark tm=it.next();	
							String type=tm.getTmType();
							boolean hasSameTmType=false;
							StringTokenizer idtok = new StringTokenizer(tmType, ";");
							while (idtok.hasMoreTokens()) {
								String value = idtok.nextToken();
								String tmClass = value.trim();
								if (tmClass.equals(type)){
									hasSameTmType=true;							
									break;
								}
							}							
							if(!hasSameTmType){
								it.remove();
							}
						} 
					}	
				}else{
					message="标得宜的客户商标数据中没有与"+tmName+"同名的商标";				
				}
			}
			else if (localSearch.equals("2")){
				if (tmType==null || tmType.equals("")){
					returnInfo.setSuccess(false);
					returnInfo.setMessage("参数tmType不能为空");
					returnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
					return returnInfo;
				}
				String tokenID=gcon.getTokenID();				
				
				String url=serviceUrlConfig.getBdydataUrl()+"/trademark/querySameTm?tmName="+ tmName +"&tmType="+tmType+"&tokenID="+ tokenID;
//				System.out.println(url);
				String jsonString = GraspUtil.getText(url);
				LoginReturnInfo loginReturnInfo=  JsonUtil.toObject(jsonString, LoginReturnInfo.class);
				
				if (loginReturnInfo==null || !loginReturnInfo.getSuccess()){
					message=loginReturnInfo.getMessage();
					Integer messageType=loginReturnInfo.getMessageType();
					returnInfo.setMessage("没有获取到商标数据, error message:"+ message);
					if (messageType!=null){
						returnInfo.setMessageType(messageType);
					}else{
						returnInfo.setMessageType(Globals.MESSAGE_TYPE_OPERATION_INVALID);
					}
					return returnInfo;
				}
				
				List<Map<String, Object>> data=(List<Map<String, Object>>)loginReturnInfo.getData();
								
				if (data==null || data.size()==0){					
					message="已经发布公告的商标数据中没有与"+tmName+"同名的商标";				
				}else{	
					

					List<Trademark> list =new ArrayList<Trademark>();
					
					for (Map<String, Object> map: data){
						String regNumber=(String)map.get("regNumber");
						String tradeMarkType=(String)map.get("tmType");
						String applicantName=(String)map.get("applicantName");
						String newTmName=(String)map.get("tmName");
						
//							String imgFilePath=(String)map.get("imgFilePath");						
						Trademark tm=new Trademark();
						tm.setTmName(tmName);
						tm.setRegNumber(regNumber);
						tm.setTmType(tradeMarkType);
						tm.setApplicantName(applicantName);
						tm.setTmName(newTmName);
						
//							tm.setImgFilePath(imgFilePath);
						list.add(tm);		
						
					}
					
					
					if(list!=null){
						int size=list.size();
						int offSet=gcon.getOffset();
						int rows=gcon.getrows();							
						int start=offSet;
						int end=start+rows;
						if (size<end){
							end=size;
						}
						for(int i=start; i<end; i++){
							trademarklist.add(list.get(i));
						}
						total=(long)size;
					}	
				}
			}
			
			
			
			
			returnInfo.setSuccess(true);		
			returnInfo.setTotal(total);
			returnInfo.setData(trademarklist);
			if (message!=null){
				returnInfo.setMessage(message);
			}			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			returnInfo.setSuccess(false);
			returnInfo.setMessage("查询同名商标失败");
			returnInfo.setMessageType(Globals.MESSAGE_TYPE_OPERATION_INVALID);
		} 		
		
		return returnInfo;

	}


}
