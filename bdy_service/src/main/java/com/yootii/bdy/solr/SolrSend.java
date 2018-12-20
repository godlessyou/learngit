package com.yootii.bdy.solr;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.PivotField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.NamedList;
import com.sun.tools.doclint.Checker.Flag;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.common.TrademarkProcessStatus;
import com.yootii.bdy.trademark.model.Trademark;
import com.yootii.bdy.util.DateTool;
import com.yootii.bdy.util.SolrUtil;

public class SolrSend  {

	private final Logger logger = Logger.getLogger(this.getClass());
	
    private SolrClient createNewSolrClient(SolrInfo solrInfo) {
        try {
//            logger.info("server address:" + solrInfo.getSolrHome()+"bdytrademark");
            HttpSolrClient client = new HttpSolrClient(solrInfo.getSolrHome()+"bdytrademark");
            client.setConnectionTimeout(solrInfo.getSolrConnectTimeOut());
            client.setDefaultMaxConnectionsPerHost(solrInfo.getSolrDefaultMaxConnect());
            client.setMaxTotalConnections(solrInfo.getSolrMaxTotalConnect());
            client.setSoTimeout(solrInfo.getSolrConnectTimeOut());
            return client;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    //创建连接solr核心的的客户端
    private SolrClient createTmProcessSolrClient(SolrInfo solrInfo,String coreName){
    	HttpSolrClient client = new HttpSolrClient(solrInfo.getSolrHome()+coreName);
    	try{
    		logger.info("server address"+solrInfo.getSolrHome()+coreName);
    		client.setConnectionTimeout(solrInfo.getSolrConnectTimeOut());
    		client.setDefaultMaxConnectionsPerHost(solrInfo.getSolrDefaultMaxConnect());
    		client.setMaxTotalConnections(solrInfo.getSolrMaxTotalConnect());
    		client.setSoTimeout(solrInfo.getSolrConnectTimeOut());
    		return client;
    	}catch (Exception e) {
    		e.printStackTrace();
    	}
    	return client;
    }
    
    public ReturnInfo selectTrademark(GeneralCondition gcon, List<String> applicantNameList,
    		Trademark trademark,String startYear,String endYear, String queYear,
			SolrInfo solrInfo,
			ReturnInfo returnInfo,int flag) throws Exception {
		SolrClient client = createNewSolrClient(solrInfo);
    	try {
    		Map<String, String> queryMap = new HashMap<String, String>();
    		//判断国内(2)国外(1) 和全部(0)
    		String nation = " AND NOT regNumber:G*";
    		if(flag == 0){
    			nation ="";
    		}else if(flag ==1){
    			nation = " AND regNumber:G*";
    		}
    		//拼接语句
    		String qString = getQueryStringBySelectTrademark(gcon,applicantNameList, 
	    			trademark, startYear,  endYear,queYear)+ nation  ;
    		
    		
	    	queryMap.put("q", qString);
	    	queryMap.put("start", String.valueOf(gcon.getOffset()));
	    	queryMap.put("rows", String.valueOf(gcon.getrows()));
	    	if(gcon.getOrderCol()!=null) {
	    		String sort = gcon.getOrderCol();
	    		if(gcon.getOrderAsc()!=null) sort = sort +" "+gcon.getOrderAsc(); else sort = sort +" DESC";
	    		queryMap.put("sort", sort);
	    	}
	    	
    		QueryResponse response = SolrUtil.solrQuery(queryMap,client);
    		returnInfo.setData(getTrademarkBySolrResults(response.getResults()));
    		returnInfo.setTotal(response.getResults().getNumFound());
		} catch (Exception e) {
			throw e;
		} finally{
			client.close();
			
		}
    	return returnInfo;
	    	
	}
	
	 private String getQueryStringBySelectTrademark(GeneralCondition gcon, List<String> applicantNameList,
			 Trademark trademark, String startYear, String endYear, String queYear) {
		String queryList = "";
		if(applicantNameList!=null && applicantNameList.size()>0){
	    	queryList = queryList +"applicantName:(";
	    	for(String applicantName:applicantNameList) {
	    		queryList = queryList+"\""+applicantName+"\"   OR ";
	    	}
	    	queryList = queryList.substring(0, queryList.length()-4);
	    	queryList = queryList+") ";
		} else {
			queryList = "*:*";
		}    	
    
    	if((startYear!=null) && (endYear!=null)){
    		String vstartYear ="*";
        	String vendYear ="*";
        	if(startYear!=null)vstartYear=startYear.toString();
        	if(endYear!=null)vendYear=endYear.toString();
        	queryList = queryList+" AND " +queYear+"Year:["+vstartYear+" TO "+vendYear+"]";
    	}
    	
		List<String> ignore = new ArrayList<String>();
		ignore.add("custId");
		ignore.add("agencyId");
		ignore.add("chargeRecords");
		ignore.add("contactUser");
    	queryList = queryList+SolrUtil.GetSolrQueryByClass(trademark, ignore);
    //	logger.info("solr查询语句"+queryList);
		return queryList;
	}
	 
	 public ReturnInfo selectTm(GeneralCondition gcon, List<String> applicantNameList,List<String> addressList,
				Trademark trademark,Date regStartDate, Date regEndDate, 
				SolrInfo solrInfo,Integer national,
				ReturnInfo returnInfo) throws Exception {
		 Trademark nottrademark = new Trademark();
		 return selectTm(gcon, applicantNameList, addressList, trademark, regStartDate, regEndDate, solrInfo,national, returnInfo,nottrademark);
	 }
	
	 /** 商标查询
	  * 输入参数通过SOLR源进行商标查询	
	 * @param govId
	 * @param entId
	 * @param gcon
	 * @param trademark
	 * @param regStartDate
	 * @param regEndDate
	 * @param statusStartDate
	 * @param statusEndDate
	 * @param excludeBranch
	 * @param entName
	 * @param solrInfo SOLR信息
	 * @param returnInfo 返回值
	 * @return 
	 * @throws Exception 错误信息
	 */
    
    
	public ReturnInfo selectTm(GeneralCondition gcon, List<String> applicantNameList,List<String> addressList,
			Trademark trademark,Date regStartDate, Date regEndDate, 
			SolrInfo solrInfo,Integer national,
			ReturnInfo returnInfo,Trademark nottrademark) throws Exception {
		SolrClient client = createNewSolrClient(solrInfo);
    	try {
    	
	    	Map<String, String> queryMap = new HashMap<String, String>();
	    	queryMap.put("q", getQueryStringBySelectTm(gcon, applicantNameList, addressList, trademark
	    			,regStartDate, regEndDate,national,nottrademark));
	    	queryMap.put("start", String.valueOf(gcon.getOffset()));
	    	queryMap.put("rows", String.valueOf(gcon.getrows()));
	    	if(gcon.getOrderCol()!=null) {
	    		String sort = gcon.getOrderCol();
	    		if(gcon.getOrderAsc()!=null) sort = sort +" "+gcon.getOrderAsc(); else sort = sort +" DESC";
	    		queryMap.put("sort", sort);
	    	}
    		QueryResponse response = SolrUtil.solrQuery(queryMap,client);
    		returnInfo.setData(getTrademarkBySolrResults(response.getResults()));
    		returnInfo.setTotal(response.getResults().getNumFound());
		} catch (Exception e) {
			throw e;
		} finally{
			client.close();
			
		}
    	return returnInfo;
	    	
	}
	
	 private List<Trademark> getTrademarkBySolrResults(SolrDocumentList docs) {
		List<Trademark> trademarklist = new ArrayList<Trademark> ();
		List<String> ignore = new ArrayList<String>();
		ignore.add("processStatus");
		for(SolrDocument doc : docs) {
			Trademark trademark = SolrUtil.SolrDocumentToClass(Trademark.class,doc,ignore);
			//trademark.setTmImage("/TmImage/"+trademark.getApplicantName().toString()+"/"+trademark.getRegNumber().toString()+".jpg");			
			trademarklist.add(trademark);
		}
		return trademarklist;
	}



	/** 处理商标查询参数
	 * @param value Map参数，传入查询参数
	 * @return 展现SOLR查询语句。
	 */
	public  String getQueryStringBySelectTm(GeneralCondition gcon,List<String> applicantNameList,List<String> addressList,
			Trademark trademark,Date regStartDate, Date regEndDate,Integer national,Trademark nottrademark) {
	    	String queryList = "";

    		String EndDate,StartDate;
	    	if(applicantNameList!=null && applicantNameList.size()>0){
		    	queryList = queryList +"applicantName:(";
		    	for(String applicantName:applicantNameList) {
		    		queryList = queryList+"\""+applicantName+"\" OR ";
		    	}
		    	queryList = queryList.substring(0, queryList.length()-4);
		    	queryList = queryList+") AND ";
		    	
			}
	    	
	    	//Modification start, by yang guang
	    	//2018-12-18
	    	if(addressList!=null && addressList.size()>0){
		    	queryList = queryList +"applicantAddress:(";
		    	for(String address:addressList) {
		    		queryList = queryList+"\""+address+"\" OR ";
		    	}
		    	queryList = queryList.substring(0, queryList.length()-4);
		    	queryList = queryList+") AND ";
		    	
			}
	    	//Modification end
	    	
	    	if(gcon.getKeyword()!=null) {	    		
	    		queryList = queryList +"(tmName:*"+gcon.getKeyword()+"* OR ";
	    		queryList = queryList +"applicantName:*"+gcon.getKeyword()+"* OR ";
	    		queryList = queryList +"applicantAddress:*"+gcon.getKeyword()+"* OR ";
	    		queryList = queryList +"regNumber:*"+gcon.getKeyword()+"*) AND ";
	    	} else {
		    	if(trademark.getRegNumber()!=null) {		    		
		    		queryList = queryList +"regNumber:*"+trademark.getRegNumber()+"* AND ";
		    	}
		    	if(trademark.getTmName()!=null) {		    		
		    		queryList = queryList +"tmName:*"+trademark.getTmName()+"* AND ";
		    	}
		    	if(trademark.getApplicantName()!=null) {		    		
		    		queryList = queryList +"applicantName:*"+trademark.getApplicantName()+"* AND ";
		    	}
		    
	    	}
	    	
	    	if(trademark.getTmType()!=null&&trademark.getTmType()!="") {
	    		String tmType = trademark.getTmType();
	    		String[] arr = tmType.split(",");
	    		if(arr.length!=0){
	    			for(int i=0;i<arr.length;i++){
	    				queryList = queryList+"tmType:"+arr[i]+" OR ";
	    			}
	    		}else{
	    			queryList = queryList +"tmType:"+trademark.getTmType()+" AND ";
	    		}
	    	}
	    	
	    	if(trademark.getStatus()!=null) {	    	
	    		queryList = queryList +"status:"+trademark.getStatus()+" AND ";
	    	}
	    	
	    	if(gcon.getStartDate()!=null||gcon.getEndDate()!=null) {
		    	if(gcon.getEndDate()!=null) {
		    		EndDate = SolrUtil.getSolrDate(gcon.getEndDate());
		    	} else {
		    		EndDate = "*";
		    	}
		    	if(gcon.getStartDate()!=null) {
		    		StartDate = SolrUtil.getSolrDate(gcon.getStartDate());
		    	} else {
		    		StartDate = "*";
		    	}		    	
		    	queryList = queryList + "appDate:["+StartDate+" TO "+EndDate+"] AND ";
	    	}
	    	if(trademark.getAgent()!=null) {
	    		/*queryList = queryList +"agent:"+trademark.getAgent()+" AND ";*/	    		
	    		queryList = queryList +"agent:"+"*"+trademark.getAgent()+"* AND ";
	    	}
	    	
	    	if(regStartDate!=null||regEndDate!=null) {
		    	if(regEndDate!=null) {
		    		EndDate = SolrUtil.getSolrDate(regEndDate);
		    	} else {
		    		EndDate = "*";
		    	}
		    	if(regStartDate!=null) {
		    		StartDate = SolrUtil.getSolrDate(regStartDate);
		    	} else {
		    		StartDate = "*";
		    	}		    	
		    	queryList = queryList + "regNoticeDate:["+StartDate+" TO "+EndDate+"] AND ";
	    	}
	    		    	
	    	
	    	if(nottrademark.getApplicantAddress()!=null) {	    	
	    		queryList =  queryList + " NOT applicantAddress:"+nottrademark.getApplicantAddress()+" AND ";
	    	}
	    	if(trademark.getValidEndDate()!=null&&nottrademark.getValidEndDate()!=null) {

	    		StartDate = SolrUtil.getSolrDate(trademark.getValidEndDate());
	    		EndDate = SolrUtil.getSolrDate(nottrademark.getValidEndDate());	    		
	    		queryList = queryList + "validEndDate:["+StartDate+" TO "+EndDate+"] AND ";
	    	}
	    	if(national!=null) {
	    		switch(national) {
	    		case 0:
	    			break;
	    		case 1:	    			
		    		queryList =  queryList + " NOT regNumber:G* AND ";
	    			break;
	    		case 2:	    		
		    		queryList =  queryList + " regNumber:G* AND ";
	    			break;
	    		}
	    	}
	    	queryList =queryList+"*:*"; 
	    	return queryList;
	    }
	
	/**
	 * 统计商标各种状态的数量
	 * @return
	 * @throws Exception 
	 */
	public long statisticsTmStatus(SolrInfo solrInfo,String type,String status,String qString,String qTime) throws Exception{
			if(!qString.equals("")){
				qString+="AND ";
			}
		
    		String queryList =qString+type+":"+status+qTime;
	    	long count=0 ;
	    	//获取客户端
	    	SolrClient client = createNewSolrClient(solrInfo);
    		try{
    			Map<String, String> queryMap = new HashMap<>();
    			queryMap.put("q", queryList);
    			System.err.println(queryList);
    			QueryResponse response = SolrUtil.solrQuery(queryMap, client);
    			count = response.getResults().getNumFound();
    		}catch (Exception e) {
    			throw e;
    		}finally {
    			client.close();
			}
    		return count;
	}
	
	/**
	 * 商标状态的数据
	 * @return
	 * @throws Exception 
	 */
	public ReturnInfo statisticsTmStatusData(SolrInfo solrInfo,String type,
			String status,String qString,String qTime,GeneralCondition gcon) throws Exception{
			if(!qString.equals("")){
				qString+="AND ";
			}
    		String queryList =qString+type+":"+status+qTime;
	    	//获取客户端
	    	SolrClient client = createNewSolrClient(solrInfo);
	    	System.err.println(qString);
	    	ReturnInfo returnInfo = new ReturnInfo();
    		try{
    			Map<String, String> queryMap = new HashMap<>();
    			queryMap.put("q", queryList);
    			queryMap.put("start", String.valueOf(gcon.getOffset()));
    	    	queryMap.put("rows", String.valueOf(gcon.getrows()));
    			System.err.println(queryList);
    			QueryResponse response = SolrUtil.solrQuery(queryMap, client);
    			List<Trademark> list=getTrademarkBySolrResults(response.getResults());
    			long total = response.getResults().getNumFound();
    			returnInfo.setData(list);
    			returnInfo.setTotal(total);
    		}catch (Exception e) {
    			throw e;
    		}finally {
    			client.close();
			}
    		return returnInfo;
	}
	
	/**
	 */
	public ReturnInfo statsTmAgentData(GeneralCondition gcon,String qString,SolrInfo solrInfo){
		ReturnInfo returnInfo = new ReturnInfo();
		
		SolrClient client = createNewSolrClient(solrInfo);
		try{
			Map<String, String> queryMap = new HashMap<>();
			queryMap.put("q", qString);
			queryMap.put("start", String.valueOf(gcon.getOffset()));
			queryMap.put("rows", String.valueOf(gcon.getrows()));
			System.err.println(qString);
			QueryResponse response = SolrUtil.solrQuery(queryMap, client);
			List<Trademark> list=getTrademarkBySolrResults(response.getResults());
			long total = response.getResults().getNumFound();
			returnInfo.setData(list);
			returnInfo.setTotal(total);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return returnInfo;
	}
	
	/**
	 * 异常 动态数据查询
	 * @return
	 */
	public ReturnInfo statisTmDynamicData(GeneralCondition gcon,String qString,SolrInfo solrInfo){
		ReturnInfo returnInfo = new ReturnInfo();
		SolrClient client = createNewSolrClient(solrInfo);
		try{
			Map<String, String> queryMap = new HashMap<>();
			queryMap.put("q",qString);
			queryMap.put("start", String.valueOf(gcon.getOffset()));
			queryMap.put("rows", String.valueOf(gcon.getrows()));
			System.err.println(qString);
			QueryResponse response = SolrUtil.solrQuery(queryMap, client);
			List<Trademark> list=getTrademarkBySolrResults(response.getResults());
			long total = response.getResults().getNumFound();
			returnInfo.setData(list);
			returnInfo.setTotal(total);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return returnInfo;
	}
	
	
	
	
	public void createDocs(SolrInfo solrInfo,SolrData trademarklist,List<SolrData> otherDataList) throws Exception {
        logger.info("======================add doc ===================");
        Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
    	SolrClient client = createNewSolrClient(solrInfo);
        try {
        	docs = getSolrDocByTrademark(trademarklist, otherDataList);
        	//Modification start, by yang guang, 2018-12-11
        	//do not delete data
        	UpdateResponse rspcommit = null;        	
        	//先删除原来的数据
//        	client.deleteByQuery("*:*");
//        	UpdateResponse rspcommit = client.commit();
//        	logger.info("删除原来的数据成功");        	
        	//Modification end
        	
            UpdateResponse rsp = client.add(docs);
            logger.info("Add doc size" + docs.size() + " result:" + rsp.getStatus() + " Qtime:" + rsp.getQTime());
            rspcommit = client.commit();
            logger.info("commit doc to index" + " result:" + rsp.getStatus() + " Qtime:" + rsp.getQTime());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
        	client.close();
        }
    }

	public void createTmProcessDocs(SolrInfo solrInfo,SolrData solrData){
		logger.info("======================add doc ===================");
		Collection<SolrInputDocument> documents = new ArrayList<>();
		//创建要更新核心的名称
		SolrClient client  = createTmProcessSolrClient(solrInfo, "bdytrademarkProcess");
		try{
			// 1整理数据
			documents = getSolrDocTmProcess(solrData);
			// 2 删除原数据
			client.deleteByQuery("*:*");
			UpdateResponse response = client.commit();
			logger.info("成功删除原来数据");
			// 3 添加新数据
			UpdateResponse response2 = client.add(documents);
			client.commit();
			logger.info("更新耗时:"+response2.getQTime());
			logger.info("添加数据大小"+documents.size());
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				client.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * bdyNotification 核心
	 * @param solrInfo
	 * @param solrData
	 */
	public void createTmNotificationDocs(SolrInfo solrInfo,SolrData solrData){
		logger.info("-----------------------add doc ------------------");
		Collection<SolrInputDocument> documents = new ArrayList<>();
		SolrClient client = createTmProcessSolrClient(solrInfo, "bdyNotification");
		try{
			
			client.deleteByQuery("*:*");
			UpdateResponse response = client.commit();
			documents = this.getSolrDocTmNotification(solrData);
			UpdateResponse response2 = client.add(documents); 
			client.commit();
			logger.info("更新耗时:"+response2.getQTime());
			logger.info("添加数据大小"+documents.size());
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try{
				client.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//bdyNotification
	public Collection<SolrInputDocument> getSolrDocTmNotification(SolrData solrData){
		
		Collection<SolrInputDocument> documents = new ArrayList<>();
		List<Map<String, Object>> lists = solrData.getDataTable();
		for(Map<String, Object> map:lists){
			SolrInputDocument document = new SolrInputDocument();
			Date createTime = (Date)map.get("createTime");
			if(createTime !=null){
				String create = DateTool.getDate(createTime);
				map.put("createTime", create);
			}
			for(Map.Entry<String, Object> ntm:map.entrySet()){
				document.addField(ntm.getKey(), ntm.getValue());
			}
			documents.add(document);
		}
		return documents;
	}
	
	
	//tmProcess 
	private Collection<SolrInputDocument> getSolrDocTmProcess(SolrData solrData){
		Collection<SolrInputDocument> documents = new ArrayList<>();
		 List<Map<String, Object>> lists  = solrData.getDataTable();
		 for(Map<String,Object> map:lists){
			 SolrInputDocument doc = new SolrInputDocument();
			 String  status = (String)map.get("status");
			 if(status !=null){
					 switch (status) {
						case "等待驳回通知发文":
							map.put("status",1);
							break;
						case "商标注册申请驳回通知发文":
							map.put("status",1);
							break;
						case "商标注册申请等待驳回通知发文":
							map.put("status",1);
							break;
						case "打印驳回或部分驳回通知书":
							map.put("status",1);
							break;
						case "打印驳回通知":
							map.put("status",1);
							break;
						case "商标异议申请中":
							map.put("status",2);
							break;
						case "商标异议（国际）中":
							map.put("status",2);
							break;
						case "无效宣告中":
							map.put("status",3);
							break;
						case "撤销三年不使用待审中":
							map.put("status",4);
							break;
						case "通用名称":
							map.put("status",5);
							break;
						default :
							break;
					}
			 }
			 for(Map.Entry<String, Object> tmProcess:map.entrySet()){
				doc.addField(tmProcess.getKey(),tmProcess.getValue()); 
			 }
			 documents.add(doc);
		 }
		return documents;
	}

	private Collection<SolrInputDocument> getSolrDocByTrademark(SolrData trademarklist,List<SolrData> otherDataList) {
		Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		docs = getSolrDocbyTrademarklist(trademarklist);
		for(SolrData otherData : otherDataList) {
			docs = changeSolrDocByOtherData(otherData,docs,"tmId");
		}
		return docs;
	}
	
	
	
    public Collection<SolrInputDocument> getSolrDocbyTrademarklist(SolrData trademarklist) {
        Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
        
		List<Map<String, Object>> trademarkMapList = trademarklist.getDataTable();
		for (Map<String, Object> trademarkMap:trademarkMapList) {
			SolrInputDocument doc = new SolrInputDocument();
			for (Map.Entry<String, Object> trademark : trademarkMap.entrySet()) {
				if (trademark.getKey().equals("tmType") ) {
					Object tmType = trademark.getValue();
					if(tmType!=null) {
						for(String type:tmType.toString().split(",")) {
							doc.addField(trademark.getKey(), type);							
						}
					}
				}else {
//				if(trademark.getValue() instanceof List)
					
					//Modification start, 2018-12-07
					Object obj=trademark.getValue();
//					if (obj==null){
//						obj=(String)"";
//					}
					doc.addField(trademark.getKey(), obj);
					//Modification end
				}
			}
			docs.add(doc);			
		}
        
        return docs;
		
	}
    
    public Collection<SolrInputDocument> changeSolrDocByOtherData(SolrData otherData,Collection<SolrInputDocument> docs,String docsIdName) {
        Map<Object,List<Map<String,Object>>> multMap = new HashMap<Object, List<Map<String,Object>>>();
    	List<Map<String, Object>> otherDataMapList = otherData.getDataTable();
		String idName = otherData.getIdName();
		for (Map<String, Object> otherDataMap:otherDataMapList) {
			Object key = otherDataMap.get(idName);
			List<Map<String,Object>> multMapValue = new ArrayList<Map<String,Object>>();
			if(multMap.get(key)==null) {
        		
        	} else {
        		multMapValue=multMap.get(key);
        	}
        	multMapValue.add(otherDataMap);
        	multMap.put(key, multMapValue);
		}
		docs = updateDocByMuitMap(multMap,docs,docsIdName);
		
		return docs;
    }



    public Collection<SolrInputDocument> updateDocByMuitMap(Map<Object, List<Map<String, Object>>> multMap,Collection<SolrInputDocument> docs,String docsIdName) {
    	for(SolrInputDocument doc:docs) {
    		Object id = doc.get(docsIdName).getFirstValue();
    		if (multMap.get(id) != null) {
    			List<Map<String,Object>> multMapValue= multMap.get(id);
    			for(Map<String,Object> multMapValueMap:multMapValue) {
    				for(Map.Entry<String, Object> entry : multMapValueMap.entrySet()) {
    					if (entry.getKey().equals(docsIdName) ) continue;
//    	        		if (entry.getKey().equals("govId") ) {
//    	        			doc.addField("govIdTree", entry.getValue()); 
//    	        			doc.addField("govId", entry.getValue()); 
//    	        			continue;
//    	        		}
//    	        		if (entry.getKey().equals("fgovId") ) {
//    	        			doc.addField("govIdTree", entry.getValue()); 
//    	        			continue;
//    	        		}
//    	        		if (entry.getKey().equals("ffgovId") ) {
//    	        			doc.addField("govIdTree", entry.getValue()); 
//    	        			continue;
//    	        		}
    					
    					//Modification start, 2018-12-07
    					Object obj=entry.getValue();
//    					if (obj==null){
//    						obj=(String)"";
//    					}
    					doc.addField(entry.getKey(), obj);   
    					//Modification end
    				}
    			}
    		}
    		
    	}
    	return docs;
    }    
    
    public Map<String,Object> SelectTradeMark(SolrInfo solrInfo,List<String> applicantNameList,Integer startYear,Integer endYear,List<String> fieldList,String queYear,boolean ignore0) throws Exception{
    	if(applicantNameList == null) throw new Exception("applicantNameList为空");
    	Map<String, Object> ret = new HashMap<String,Object>();
    	SolrClient client = createNewSolrClient(solrInfo);
    	Map<String, String> queryMap = new HashMap<String, String>();
    	String queryList = "";
    	queryList = queryList +"applicantName:(";
    	for(String applicantName:applicantNameList) {
    		queryList = queryList+"\""+applicantName+"\"   OR ";
    	}
    	queryList = queryList.substring(0, queryList.length()-4);
    	queryList = queryList+")";    	
    	if((startYear!=null) && (endYear!=null)){
    		String vstartYear ="*";
        	String vendYear ="*";
        	if(startYear!=null)vstartYear=startYear.toString();
        	if(endYear!=null)vendYear=endYear.toString();
        	queryMap.put("q", queYear+"Year:["+vstartYear+" TO "+vendYear+"] AND NOT regNumber:G*");
    	}else{
    		queryMap.put("q", "*:*");
    	}
    	
    	queryMap.put("fq", queryList);
    	queryMap.put("start", "0");
    	queryMap.put("rows", "0");
    	queryMap.put("facet", "on");
    	String facetfield = "";
    	for(String field:fieldList) {
    		facetfield = facetfield +field+",";
    	}
    	queryMap.put("facet.field",facetfield.substring(0, facetfield.length()-1));
    	
    	try {
    		for(FacetField a:SolrUtil.solrQuery(queryMap,client).getFacetFields()) {
    	    	List<Map<String, Object>> ret0list = new ArrayList<Map<String, Object>>();
				for(org.apache.solr.client.solrj.response.FacetField.Count b:a.getValues()) {
					if(b.getCount() == 0&ignore0) continue;
	    			Map<String,Object> ret0 = new HashMap<String,Object>();
	    			if(a.getName().indexOf("Year")>=0) {
	    				ret0.put("Year", b.getName());	    				
	    			}else {
	    				ret0.put(a.getName(), b.getName());
	    			}
	    			ret0.put("count", b.getCount());
	    			ret0list.add(ret0);					
				}
				if(a.getName().indexOf("Year")>=0) {
					orderYear(ret0list,startYear,endYear);		
    			}
    			ret.put(a.getName(), ret0list);
    		}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return ret;
    	
    }
    
    public Object SelectTradeMarkByList(SolrInfo solrInfo,int flag,List<String> applicantNameList,Integer startYear,Integer endYear,List<String> fieldList,String queYear,boolean ignore0) throws Exception{
    	if(applicantNameList == null) throw new Exception("applicantNameList为空");
    	Map<String, Object> ret = new HashMap<String,Object>();
		List<Map<String, Object>> retlist = new ArrayList<Map<String, Object>>();
    	SolrClient client = createNewSolrClient(solrInfo);
    	Map<String, String> queryMap = new HashMap<String, String>();
    	String queryList = "";
    	if (applicantNameList!=null && applicantNameList.size()>0){
	    	queryList = queryList +"applicantName:(";
	    	for(String applicantName:applicantNameList) {
	    		queryList = queryList+"\""+applicantName+"\"   OR ";
	    	}
	    	queryList = queryList.substring(0, queryList.length()-4);
	    	queryList = queryList+")";
    	}
    	
    	
    	if((startYear!=null) && (endYear!=null)){
    		String vstartYear ="*";
        	String vendYear ="*";
        	if(startYear!=null)vstartYear=startYear.toString();
        	if(endYear!=null)vendYear=endYear.toString();
        	//flag 标识 0：全部  1：国外  2：国内
        	if(flag == 0){
        		queryMap.put("q", queYear+"Year:["+vstartYear+" TO "+vendYear+"]");
        	}else if(flag == 1){
        		queryMap.put("q", queYear+"Year:["+vstartYear+" TO "+vendYear+"] AND regNumber:G*");
        	}else{
        		queryMap.put("q", queYear+"Year:["+vstartYear+" TO "+vendYear+"] AND NOT regNumber:G*");
        	}
    	}else{
    		queryMap.put("q", "*:*");
    	}
    	
    	queryMap.put("fq", queryList);
    	queryMap.put("start", "0");
    	queryMap.put("rows", "0");
    	queryMap.put("facet", "on");
    	if(fieldList.size()>1) {
	    	String facetfield = "";
	    	for(String field:fieldList) {
	    		facetfield = facetfield +field+",";
	    	}
	    	queryMap.put("facet.pivot",facetfield.substring(0, facetfield.length()-1));
    	}else {
    		queryMap.put("facet.field",fieldList.get(0));
    	}
    	
    	try {
    		if(fieldList.size()>1) {
    			
    			NamedList<List<PivotField>> res = SolrUtil.solrQuery(queryMap,client).getFacetPivot();
    			
    			//整理结果
	    		retlist =  returnmap(res.getVal(0),startYear,endYear);
	    		if(res.getName(0).indexOf("Year")>=0) {
	    			//排序
	    			Collections.sort(retlist,new orderByYear());
					//orderYear(retlist,startYear,endYear);		
				}
    		}else {
    			List<FacetField>list=SolrUtil.solrQuery(queryMap,client).getFacetFields();
	    		for(FacetField a:list) {
	    	    	List<Map<String, Object>> ret0list = new ArrayList<Map<String, Object>>();
					for(org.apache.solr.client.solrj.response.FacetField.Count b:a.getValues()) {
						if(b.getCount() == 0&ignore0) continue;
		    			Map<String,Object> ret0 = new HashMap<String,Object>();
		    			if(a.getName().indexOf("Year")>=0) {
		    				ret0.put("Year", b.getName());	    				
		    			}else {
		    				ret0.put(a.getName(), b.getName());
		    			}
		    			ret0.put("count", b.getCount());
		    			ret0list.add(ret0);					
					}
					if(a.getName().indexOf("Year")>=0) {
						orderYear(ret0list,startYear,endYear);		
	    			}
	    			ret.put(a.getName(), ret0list);
	    		}
    		}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
    	if(fieldList.size()>1) {
        	return retlist;
    	} else {
    		return ret;
    	}
    }
    //整理查询返回的结果
    List<Map<String, Object>> returnmap(List<PivotField> list, Integer startYear, Integer endYear){
		List<Map<String, Object>> retlist = new ArrayList<Map<String, Object>>();
		for(PivotField a:list) {
			Map<String, Object> ret = new HashMap<String, Object>();
			if(a.getField().indexOf("Year")>=0) {
				ret.put("Year", a.getValue());	    				
			}else {
				ret.put("name", a.getValue());
			}
			if(a.getPivot() == null) {
				
				ret.put("value", a.getCount());
			}else {  
				List<Map<String, Object>> ret0list = returnmap(a.getPivot(),startYear,endYear);
				ret.put("value", ret0list);
			}
			retlist.add(ret);
		}
		return retlist;
	}
    
    //根据时间排序
	private void orderYear(List<Map<String, Object>> ret0list, Integer startYear, Integer endYear) {

    	Collections.sort(ret0list,new orderByYear());  		
		if(startYear ==null) {
			startYear = Integer.valueOf(Collections.min(ret0list,new orderByYear()).get("Year").toString());
		}
		if(endYear ==null) {
			endYear = Integer.valueOf(Collections.max(ret0list,new orderByYear()).get("Year").toString());
		}
		//logger.info(startYear);
	    //	logger.info(endYear);
		for(int i = 0; ; ) {
			if(Integer.valueOf(ret0list.get(i).get("Year").toString()).intValue() < startYear+i) {
				//logger.info(ret0list.get(i));
				ret0list.remove(i);
			}else if((Integer.valueOf(ret0list.get(i).get("Year").toString()).intValue() > startYear+i)&(Integer.valueOf(ret0list.get(i).get("Year").toString()) <= endYear)) {

				Map<String,Object> ret0 = new HashMap<String,Object>();
				ret0.put("Year", startYear+i);
				ret0.put("count", 0);
				ret0list.add(ret0);
				Collections.sort(ret0list,new orderByYear());  	
				//logger.info(ret0list.get(i));
			}  else	if(Integer.valueOf(ret0list.get(i).get("Year").toString()).equals(endYear)) {
				if(ret0list.size()==endYear-startYear+1)
				{
					break;

				}else if(Integer.valueOf(ret0list.get(i+1).get("Year").toString()).intValue() > endYear) {
					//logger.info(ret0list.get(i+1));
					ret0list.remove(i+1);
				}
			}else if(i == ret0list.size() - 1) {

				Map<String,Object> ret0 = new HashMap<String,Object>();
				ret0.put("Year", startYear+i+1);
				ret0.put("count", 0);
				ret0list.add(ret0);
				Collections.sort(ret0list,new orderByYear());
			}else if(Integer.valueOf(ret0list.get(i).get("Year").toString()).equals(startYear+i)) {
				i++;
			}
			//logger.info(ret0list.toString());
			//logger.info(Integer.valueOf(ret0list.get(i).get("Year").toString()));
			//logger.info(ret0list.size());
		}
	}
	class orderByYear implements Comparator<Map<String, Object>> {
		public int compare(Map<String, Object> s1,Map<String, Object> s2) {
			return (Integer.valueOf((s1.get("Year").toString())).compareTo(Integer.valueOf((s2.get("Year").toString()))));
		}
	}
    
	
	public Map<Integer, Object> statsTmBeichesanDongtai(SolrInfo solrInfo,List<String> applicantNameList,Integer startYear,Integer endYear) throws Exception{

    	if(applicantNameList == null) throw new Exception("applicantNameList为空");
    	Map<Integer, Object> ret = new HashMap<Integer,Object>();
    	SolrClient client = createNewSolrClient(solrInfo);
    	Map<String, String> queryMap = new HashMap<String, String>();
    	String query = "";
    	query = query +"applicantName:(";
    	for(String applicantName:applicantNameList) {
    		query = query+""+applicantName+"   OR ";
    	}
    	query = query.substring(0, query.length()-4);
    	query = query+")";	
    	System.err.println(query);
    	queryMap.put("q", "*:*");
    	queryMap.put("fq", query);
    	queryMap.put("start", "0");
    	queryMap.put("rows", "0");
    	queryMap.put("facet", "on");
    	Map<String, List<String>> queryMapList = new HashMap<String, List<String>>();
    	List<String> queryList= new ArrayList<String>();
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		if(startYear ==null) {
			startYear = c.get(Calendar.YEAR) - 4;
		}
		if(endYear ==null) {
			endYear = c.get(Calendar.YEAR);
		}
    	
    	for(int i = 0; i< endYear -startYear+1; i++) {
    		List<String> list= new ArrayList<String>();
    		list.add(TrademarkProcessStatus.beichesan);    		
    		queryList.add(getfacetqueryList(startYear.intValue()+i,TrademarkProcessStatus.chesantitle,list));

    		
    		
    	}
    	queryMapList.put("facet.query",queryList);
    	try {
			for(Map.Entry<String, Integer> a:SolrUtil.solrQuery(queryMap,queryMapList,client).getFacetQuery().entrySet()) {
				String year =a.getKey().substring(5, 9);
				ret.put(Integer.valueOf(year), a.getValue());
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
//    	Collections.sort(retlist,new AmountReturnSortByAmount());

		logger.info(ret.toString());
    	return ret;
	}
	public Map<Integer, Object> statsTmBiangengDongtai(SolrInfo solrInfo,List<String> applicantNameList,Integer startYear,Integer endYear) throws Exception{

    	if(applicantNameList == null) throw new Exception("applicantNameList为空");
    	Map<Integer, Object> ret = new HashMap<Integer,Object>();
    	SolrClient client = createNewSolrClient(solrInfo);
    	Map<String, String> queryMap = new HashMap<String, String>();
    	String query = "";
    	query = query +"applicantName:(";
    	for(String applicantName:applicantNameList) {
    		query = query+""+applicantName+"   OR ";
    	}
    	query = query.substring(0, query.length()-4);
    	query = query+")";
    	queryMap.put("q", "*:*");
    	queryMap.put("fq", query);
    	queryMap.put("start", "0");
    	queryMap.put("rows", "0");
    	queryMap.put("facet", "on");
    	Map<String, List<String>> queryMapList = new HashMap<String, List<String>>();
    	List<String> queryList= new ArrayList<String>();
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		if(startYear ==null) {
			startYear = c.get(Calendar.YEAR) - 4;
		}
		if(endYear ==null) {
			endYear = c.get(Calendar.YEAR);
		}
    	
    	for(int i = 0; i< endYear -startYear+1; i++) {
    		List<String> list= new ArrayList<String>();

    		list.add(TrademarkProcessStatus.biangeng);  
    		queryList.add(getfacetqueryList(startYear.intValue()+i,TrademarkProcessStatus.biangengtitle,list));
    	}
    	queryMapList.put("facet.query",queryList);
    	try {
			for(Map.Entry<String, Integer> a:SolrUtil.solrQuery(queryMap,queryMapList,client).getFacetQuery().entrySet()) {
				String year =a.getKey().substring(5, 9);
				ret.put(Integer.valueOf(year), a.getValue());
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
//    	Collections.sort(retlist,new AmountReturnSortByAmount());

		//logger.info(ret.toString());
    	return ret;
	}	
	public Map<Integer, Object> statsTmBeibohuiDongtai(SolrInfo solrInfo,List<String> applicantNameList,Integer startYear,Integer endYear) throws Exception{

    	if(applicantNameList == null) throw new Exception("applicantNameList为空");
    	Map<Integer, Object> ret = new HashMap<Integer,Object>();
    	SolrClient client = createNewSolrClient(solrInfo);
    	Map<String, String> queryMap = new HashMap<String, String>();
    	String query = "";
    	query = query +"applicantName:(";
    	for(String applicantName:applicantNameList) {
    		query = query+""+applicantName+"   OR ";
    	}
    	query = query.substring(0, query.length()-4);
    	query = query+")";	
    	queryMap.put("q","*:*" );
    	queryMap.put("fq", query);
    	queryMap.put("start", "0");
    	queryMap.put("rows", "0");
    	queryMap.put("facet", "on");
    	Map<String, List<String>> queryMapList = new HashMap<String, List<String>>();
    	List<String> queryList= new ArrayList<String>();
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		if(startYear ==null) {
			startYear = c.get(Calendar.YEAR) - 4;
		}
		if(endYear ==null) {
			endYear = c.get(Calendar.YEAR);
		}
    	
    	for(int i = 0; i< endYear -startYear+1; i++) {
    		List<String> list= new ArrayList<String>();

    		list.add(TrademarkProcessStatus.beibohui_1);  
    		list.add(TrademarkProcessStatus.beibohui_2);  
    		list.add(TrademarkProcessStatus.beibohui_3);  
    		list.add(TrademarkProcessStatus.beibohui_4);  
    		list.add(TrademarkProcessStatus.beibohui_5); 
    		queryList.add(getfacetqueryList(startYear.intValue()+i,TrademarkProcessStatus.bohuititle,list));
    	}
    	queryMapList.put("facet.query",queryList);
    	try {
    		
    		QueryResponse qResponse= SolrUtil.solrQuery(queryMap,queryMapList,client);
    		List<Trademark> list = getTrademarkBySolrResults(qResponse.getResults());
    		Map<String,Integer> map = qResponse.getFacetQuery();
			for(Map.Entry<String, Integer> a:map.entrySet()) {
				String year =a.getKey().substring(5, 9);
				ret.put(Integer.valueOf(year), a.getValue());
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
//    	Collections.sort(retlist,new AmountReturnSortByAmount());

		//logger.info(ret.toString());
    	return ret;
	}	
	
	//从tmProcess核心中统计数据
	public List<Map<String, Object>> statisByTmProcessCore(SolrInfo solrInfo,String query,String qString,Integer startYear,Integer endYear){
		
		List<Map<String, Object>> listResult = new ArrayList<>();
		try{
			SolrClient solrClient =  createTmProcessSolrClient(solrInfo,"bdytrademarkProcess");
			Map<String, String> queryMap = new HashMap<>();
			queryMap.put("q",qString);
	    	queryMap.put("fq", query);
	    	queryMap.put("facet", "on");
	    	Map<String, Integer> map = new HashMap<>();
	    	List<Map<String, Integer>> list = new ArrayList<>();
	    	for(int i=startYear;i<=endYear;i++){
	    		String facetQuery = "";
	    		facetQuery +="statusDate:"+i;
	    		queryMap.put("facet.query",facetQuery);
	    		QueryResponse qResponse = SolrUtil.solrQuery(queryMap, solrClient);
	    		map = qResponse.getFacetQuery();
	//    		System.err.println(map);
	    		list.add(map);
	    	}
	    	if(list.size() != 0){
	    		for(int i=0;i<list.size();i++){
	    			Map<String, Integer> map2 = list.get(i);
	    			Map<String , Object> ret = this.setterData(map2);
	    			listResult.add(ret);
	    		}
	    		System.err.println(listResult);
	    	}
    	}catch (Exception e) {
    		e.printStackTrace();
    	}
		return listResult;
	}
	//整理数据
	private Map<String, Object> setterData(Map<String,Integer> map){
		Map<String, Object> resultMap = new HashMap<>();
		if(map!=null){
    		for(Map.Entry<String, Integer> ret: map.entrySet()){
	    		String year = ret.getKey().substring(11,15);
	    		Integer count = ret.getValue();
	    		resultMap.put(year, count);
	    	}
    	}
		return resultMap;
	}
	
	
	public Map<Integer, Object> statsTmTongyongmingchengDongtai(SolrInfo solrInfo,List<String> applicantNameList,Integer startYear,Integer endYear) throws Exception{

    	if(applicantNameList == null) throw new Exception("applicantNameList为空");
    	Map<Integer, Object> ret = new HashMap<Integer,Object>();
    	SolrClient client = createNewSolrClient(solrInfo);
    	Map<String, String> queryMap = new HashMap<String, String>();
    	String query = "";
    	query = query +"applicantName:(";
    	for(String applicantName:applicantNameList) {
    		query = query+""+applicantName+"   OR ";
    	}
    	query = query.substring(0, query.length()-4);
    	query = query+")";	
    	queryMap.put("q", query);
    	queryMap.put("start", "0");
    	queryMap.put("rows", "0");
    	queryMap.put("facet", "on");
    	Map<String, List<String>> queryMapList = new HashMap<String, List<String>>();
    	List<String> queryList= new ArrayList<String>();
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		if(startYear ==null) {
			startYear = c.get(Calendar.YEAR) - 4;
		}
		if(endYear ==null) {
			endYear = c.get(Calendar.YEAR);
		}
    	
    	for(int i = 0; i< endYear -startYear+1; i++) {
    		List<String> list= new ArrayList<String>();
    		list.add(TrademarkProcessStatus.tongyongmingcheng);  
    		queryList.add(getfacetqueryList(startYear.intValue()+i,TrademarkProcessStatus.tongyongmingchengtitle,list));
    	}
    	queryMapList.put("facet.query",queryList);
    	try {
			for(Map.Entry<String, Integer> a:SolrUtil.solrQuery(queryMap,queryMapList,client).getFacetQuery().entrySet()) {
				String year =a.getKey().substring(5, 9);
				ret.put(Integer.valueOf(year), a.getValue());
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
//    	Collections.sort(retlist,new AmountReturnSortByAmount());

		//logger.info(ret.toString());
    	return ret;
	}	
	
	public Map<Integer, Object> statsTmBeiwuxiaoDongtai(SolrInfo solrInfo,List<String> applicantNameList,Integer startYear,Integer endYear) throws Exception{

    	if(applicantNameList == null) throw new Exception("applicantNameList为空");
    	Map<Integer, Object> ret = new HashMap<Integer,Object>();
    	SolrClient client = createNewSolrClient(solrInfo);
    	Map<String, String> queryMap = new HashMap<String, String>();
    	String query = "";
    	query = query +"applicantName:(";
    	for(String applicantName:applicantNameList) {
    		query = query+""+applicantName+"   OR ";
    	}
    	query = query.substring(0, query.length()-4);
    	query = query+")";	
    	queryMap.put("q","*:*");
    	queryMap.put("fq", query);
    	queryMap.put("start", "0");
    	queryMap.put("rows", "0");
    	queryMap.put("facet", "on");
    	Map<String, List<String>> queryMapList = new HashMap<String, List<String>>();
    	List<String> queryList= new ArrayList<String>();
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		if(startYear ==null) {
			startYear = c.get(Calendar.YEAR) - 4;
		}
		if(endYear ==null) {
			endYear = c.get(Calendar.YEAR);
		}
    	
    	for(int i = 0; i< endYear -startYear+1; i++) {
    		List<String> list= new ArrayList<String>();
    		list.add(TrademarkProcessStatus.beiwuxiao);  
    		queryList.add(getfacetqueryList(startYear.intValue()+i,TrademarkProcessStatus.wuxiaoxuangaotitle,list));
    	}
    	queryMapList.put("facet.query",queryList);
    	try {
			for(Map.Entry<String, Integer> a:SolrUtil.solrQuery(queryMap,queryMapList,client).getFacetQuery().entrySet()) {
				String year =a.getKey().substring(5, 9);
				ret.put(Integer.valueOf(year), a.getValue());
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
//    	Collections.sort(retlist,new AmountReturnSortByAmount());

	//	logger.info(ret.toString());
    	return ret;
	}	
	
	
	public Map<Integer, Object> statsTmBeiyiyiDongtai(SolrInfo solrInfo,List<String> applicantNameList,Integer startYear,Integer endYear) throws Exception{

    	if(applicantNameList == null) throw new Exception("applicantNameList为空");
    	Map<Integer, Object> ret = new HashMap<Integer,Object>();
    	SolrClient client = createNewSolrClient(solrInfo);
    	Map<String, String> queryMap = new HashMap<String, String>();
    	String query = "";
    	query = query +"applicantName:(";
    	for(String applicantName:applicantNameList) {
    		query = query+""+applicantName+"   OR ";
    	}
    	query = query.substring(0, query.length()-4);
    	query = query+")";
    	queryMap.put("q", "*:*");
    	queryMap.put("fq", query);
    	queryMap.put("start", "0");
    	queryMap.put("rows", "0");
    	queryMap.put("facet", "on");
    	Map<String, List<String>> queryMapList = new HashMap<String, List<String>>();
    	List<String> queryList= new ArrayList<String>();
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		if(startYear ==null) {
			startYear = c.get(Calendar.YEAR) - 4;
		}
		if(endYear ==null) {
			endYear = c.get(Calendar.YEAR);
		}
    	
    	for(int i = 0; i< endYear -startYear+1; i++) {
    		List<String> list= new ArrayList<String>();
    		list.add(TrademarkProcessStatus.beiyiyi_1);  
    		list.add(TrademarkProcessStatus.beiyiyi_2); 
       		queryList.add(getfacetqueryList(startYear.intValue()+i,TrademarkProcessStatus.yiyititle,list));  		
    	}
    	queryMapList.put("facet.query",queryList);
    	try {
			for(Map.Entry<String, Integer> a:SolrUtil.solrQuery(queryMap,queryMapList,client).getFacetQuery().entrySet()) {
				String year =a.getKey().substring(5, 9);
				ret.put(Integer.valueOf(year), a.getValue());
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
//    	Collections.sort(retlist,new AmountReturnSortByAmount());

		//logger.info(ret.toString());
    	return ret;
	}	

	public Map<Integer, Object> statsTmDongtai(SolrInfo solrInfo,List<String> applicantNameList,Integer startYear,Integer endYear) throws Exception{

    	if(applicantNameList == null) throw new Exception("applicantNameList为空");
    	Map<Integer, Object> ret = new HashMap<Integer,Object>();
    	SolrClient client = createNewSolrClient(solrInfo);
    	Map<String, String> queryMap = new HashMap<String, String>();
    	String query = "";
    	query = query +"applicantName:(";
    	for(String applicantName:applicantNameList) {
    		query = query+""+applicantName+"   OR ";
    	}
    	query = query.substring(0, query.length()-4);
    	query = query+")";
    	
    	

    	queryMap.put("q", query);
    	queryMap.put("start", "0");
    	queryMap.put("rows", "0");
    	queryMap.put("facet", "on");
    	Map<String, List<String>> queryMapList = new HashMap<String, List<String>>();
    	List<String> queryList= new ArrayList<String>();
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		if(startYear ==null) {
			startYear = c.get(Calendar.YEAR) - 4;
		}
		if(endYear ==null) {
			endYear = c.get(Calendar.YEAR);
		}
    	
    	for(int i = 0; i< endYear -startYear+1; i++) {
    		
    		
    		List<String> list= new ArrayList<String>();
    		list.add(TrademarkProcessStatus.beichesan);    		
    		queryList.add(getfacetqueryList(startYear.intValue()+i,TrademarkProcessStatus.chesantitle,list));

    		list.clear();
    		list.add(TrademarkProcessStatus.biangeng);  
    		queryList.add(getfacetqueryList(startYear.intValue()+i,TrademarkProcessStatus.biangengtitle,list));

    		list.clear();
    		list.add(TrademarkProcessStatus.beibohui_1);  
    		list.add(TrademarkProcessStatus.beibohui_2);  
    		list.add(TrademarkProcessStatus.beibohui_3);  
    		list.add(TrademarkProcessStatus.beibohui_4);  
    		list.add(TrademarkProcessStatus.beibohui_5); 
    		queryList.add(getfacetqueryList(startYear.intValue()+i,TrademarkProcessStatus.bohuititle,list));

    		list.clear();
    		list.add(TrademarkProcessStatus.tongyongmingcheng);  
    		queryList.add(getfacetqueryList(startYear.intValue()+i,TrademarkProcessStatus.tongyongmingchengtitle,list));

    		list.clear();
    		list.add(TrademarkProcessStatus.beiwuxiao);  
    		queryList.add(getfacetqueryList(startYear.intValue()+i,TrademarkProcessStatus.wuxiaoxuangaotitle,list));

    		list.clear();
    		list.add(TrademarkProcessStatus.beiyiyi_1);  
    		list.add(TrademarkProcessStatus.beiyiyi_2); 
       		queryList.add(getfacetqueryList(startYear.intValue()+i,TrademarkProcessStatus.yiyititle,list));
    				
    		
    		
    		
    	}
    	queryMapList.put("facet.query",queryList);
    	try {
			for(Map.Entry<String, Integer> a:SolrUtil.solrQuery(queryMap,queryMapList,client).getFacetQuery().entrySet()) {
				String year =a.getKey().substring(5, 9);
				int i = a.getKey().indexOf('_');
				String title;
				if(i>=0) {
					title=a.getKey().substring(16, i);
				}else {
					title=a.getKey().substring(16, a.getKey().length());
				}
				Map<String,Integer> retyear;
				Object obj = ret.get(Integer.valueOf(year));
				if (obj == null) {
		    		retyear = new HashMap<String,Integer>();
				} else {
					retyear = (Map<String, Integer>) obj;
				}
				Integer j = retyear.get(title);
				if (j == null) {
					retyear.put(title, a.getValue());
				} else {
					retyear.put(title, j+a.getValue());
				}
				ret.put(Integer.valueOf(year), retyear);
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
//    	Collections.sort(retlist,new AmountReturnSortByAmount());

	//	logger.info(ret.toString());
    	return ret;
	}



   
    
	public Map<Integer, Object> statsTmzhongzhi(SolrInfo solrInfo,List<String> applicantNameList,Integer startYear,Integer endYear) throws Exception{

    	if(applicantNameList == null) throw new Exception("applicantNameList为空");
    	Map<Integer, Object> ret = new HashMap<Integer,Object>();
    	SolrClient client = createNewSolrClient(solrInfo);
    	Map<String, String> queryMap = new HashMap<String, String>();
    	String query = "";
    	query = query +"applicantName:(";
    	for(String applicantName:applicantNameList) {
    		query = query+""+applicantName+"   OR ";
    	}
    	query = query.substring(0, query.length()-4);
    	query = query+")";
    	
    	

    	queryMap.put("q", query);
    	queryMap.put("start", "0");
    	queryMap.put("rows", "0");
    	queryMap.put("facet", "on");
    	Map<String, List<String>> queryMapList = new HashMap<String, List<String>>();
    	List<String> queryList= new ArrayList<String>();
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		if(startYear ==null) {
			startYear = c.get(Calendar.YEAR) - 4;
		}
		if(endYear ==null) {
			endYear = c.get(Calendar.YEAR);
		}
    	
    	for(int i = 0; i< endYear -startYear; i++) {
    		
    		
    		queryList.add("validEndDate:[* TO "+SolrUtil.getSolrDate(new Date())+"]");
    		
//    		queryList.add(getfacetqueryList(startYear.intValue()+i,TrademarkProcessStatus.chesantitle,TrademarkProcessStatus.beichesan)+"AND(status:(已销亡 OR 已驳回))");
//    		queryList.add(getfacetqueryList(startYear.intValue()+i,TrademarkProcessStatus.beizhuxiaotitle+"_1",TrademarkProcessStatus.beizhuxiao_1)+"AND(status:(已销亡 OR 已驳回))");
//    		queryList.add(getfacetqueryList(startYear.intValue()+i,TrademarkProcessStatus.beizhuxiaotitle+"_2",TrademarkProcessStatus.beizhuxiao_1)+"AND(status:(已销亡 OR 已驳回))");
//    		queryList.add(getfacetqueryList(startYear.intValue()+i,TrademarkProcessStatus.bohuititle+"_1",TrademarkProcessStatus.beibohui_1)+"AND(status:(已销亡 OR 已驳回))");
//    		queryList.add(getfacetqueryList(startYear.intValue()+i,TrademarkProcessStatus.bohuititle+"_2",TrademarkProcessStatus.beibohui_2)+"AND(status:(已销亡 OR 已驳回))");
//    		queryList.add(getfacetqueryList(startYear.intValue()+i,TrademarkProcessStatus.bohuititle+"_3",TrademarkProcessStatus.beibohui_3)+"AND(status:(已销亡 OR 已驳回))");
//    		queryList.add(getfacetqueryList(startYear.intValue()+i,TrademarkProcessStatus.bohuititle+"_4",TrademarkProcessStatus.beibohui_4)+"AND(status:(已销亡 OR 已驳回))");
//    		queryList.add(getfacetqueryList(startYear.intValue()+i,TrademarkProcessStatus.bohuititle+"_5",TrademarkProcessStatus.beibohui_5)+"AND(status:(已销亡 OR 已驳回))");
//    		queryList.add(getfacetqueryList(startYear.intValue()+i,TrademarkProcessStatus.chehuizhucetitle,TrademarkProcessStatus.chehuizhuce)+"AND(status:(已销亡 OR 已驳回))");
    	}
    	queryMapList.put("facet.query",queryList);
    	try {
			for(Map.Entry<String, Integer> a:SolrUtil.solrQuery(queryMap,queryMapList,client).getFacetQuery().entrySet()) {
				String year =a.getKey().substring(5, 9);
				int i = a.getKey().indexOf('_');
				String title;
				if(i>=0) {
					title=a.getKey().substring(16, i);
				}else {
					title=a.getKey().substring(16, a.getKey().length());
				}
				Map<String,Integer> retyear;
				Object obj = ret.get(Integer.valueOf(year));
				if (obj == null) {
		    		retyear = new HashMap<String,Integer>();
				} else {
					retyear = (Map<String, Integer>) obj;
				}
				Integer j = retyear.get(title);
				if (j == null) {
					retyear.put(title, a.getValue());
				} else {
					retyear.put(title, j+a.getValue());
				}
				ret.put(Integer.valueOf(year), retyear);
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
//    	Collections.sort(retlist,new AmountReturnSortByAmount());

	//	logger.info(ret.toString());
    	return ret;
	}
    
	private String getfacetqueryList(int year, String title, List<String> values) {
		String ret = "{!key=\"year\\:"+String.valueOf(year)+"status\\:"+title+"\"}";
		/*for(String value:values) {
			ret = ret + "(trademarkprocess:*\\\"status\\\"\\:"
				+values.get(0)+",*\\\"statusDate\\\"\\:*"+String.valueOf(year)+"*)    OR ";
		}*/
		
		for(int i=0;i<values.size();i++){
			ret = ret + "(trademarkprocess:*\\\"status\\\"\\:"
					+values.get(i)+",*\\\"statusDate\\\"\\:*"+String.valueOf(year)+"*)    OR ";
		}
		ret = ret.substring(0, ret.length()-4);
		return ret;
	}
  
//	原统计信息，暂时无用，之后增加统计功能时候作为参考。
//  
//    
//    private String qStringByEnt(Enterprise enterprise) {
//    	String ret = "*:*";
//    	if(enterprise.getEntOwner()!=null)ret = ret + " AND entOwner:"+enterprise.getEntOwner().toString();
//    	if(enterprise.getEntType()!=null)ret = ret + " AND entType:"+enterprise.getEntType().toString();
//    	if(enterprise.getEntScale()!=null)ret = ret + " AND entScale:"+enterprise.getEntScale().toString();
//    	if(enterprise.getRstate()!=null) {
//    		String rs="";
//    		if(enterprise.getRstate()==-1)
//    			rs = "(2 OR 3)";
//    		else
//    			rs = enterprise.getRstate().toString();
//    		ret = ret + " AND rstate:"+rs;
//    	}
//		return ret;
//	}
//    
//	class StatsReturnByYear implements Comparator<StatsReturn> {
//	public int compare(StatsReturn s1,StatsReturn s2) {
//		return -(s1.getYear().compareTo(s2.getYear()));
//		//         if (s1.getAge() > s2.getAge())
//		//          return 1;
//		//         return -1;
//	}
//}
//    public List<StatsReturn> SelectTradeMarkAppCountByYear(SolrInfo solrInfo,Enterprise enterprise,Integer year1,Integer year2){
//    	List<StatsReturn> retList = new ArrayList<StatsReturn>();
//    	SolrClient client = createNewSolrClient(solrInfo);
//    	Map<String, String> queryMap = new HashMap<String, String>();
//    	queryMap.put("q", "*:*");
//    	if (enterprise.getEntId()!=null) {
//    		queryMap.put("fq", "entId:"+enterprise.getEntId().toString());
//    	}else {
//    		queryMap.put("fq", "govIdTree:"+enterprise.getGovId().toString());
//    		if (qStringByEnt(enterprise)=="")queryMap.put("q", "*:*") ;else queryMap.put("q", qStringByEnt(enterprise));
//    		
//    	}
//    	queryMap.put("start", "0");
//    	queryMap.put("rows", "10");
//    	queryMap.put("facet", "on");
//    	queryMap.put("facet.range", "appDate");
//    	queryMap.put("facet.range.start", year1.toString()+"-01-01T00:00:00Z");
//    	queryMap.put("facet.range.end", year2.toString()+"-01-01T00:00:00Z");
//    	queryMap.put("facet.range.gap", "+1YEARS");
//    	logger.info(queryMap);
//    	try {
//    		for(Count a:(List<Count>)SolrUtil.solrQuery(queryMap,client).getFacetRanges().get(0).getCounts()) {
//    			StatsReturn ret = new StatsReturn();
//    			ret.setYear(Integer.valueOf(a.getValue().substring(0, 4)));
//    			ret.setAmount(new Long((long)a.getCount()));
//    			logger.info(a.getValue()+"  "+a.getCount());
//    			retList.add(ret);
//    		}
//		} catch (Exception e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}
//    	Collections.sort(retList,new StatsReturnByYear());   
//		return retList;
//	}
//	
//
//
//
//
//	public Long SelectTradeMarkRegCountByAll(SolrInfo solrInfo,Enterprise enterprise, String year){
//    	//TODO
//		return null; 
//	}
//	
//    public List<StatsReturn> SelectTradeMarkNowRegCountByYear(SolrInfo solrInfo,Enterprise enterprise,Integer year1,Integer year2){
//    	List<StatsReturn> retList = new ArrayList<StatsReturn>();
//    	SolrClient client = createNewSolrClient(solrInfo);
//    	Map<String, String> queryMap = new HashMap<String, String>();
//    	queryMap.put("q","*:*");
//    	if (enterprise.getEntId()!=null) {
//    		queryMap.put("fq", "entId:"+enterprise.getEntId().toString());
//    	}else {
//    		queryMap.put("fq", "govIdTree:"+enterprise.getGovId().toString());
//    		if (qStringByEnt(enterprise)=="")queryMap.put("q", "*:*") ;else queryMap.put("q", qStringByEnt(enterprise));
//    	}
//		queryMap.put("qt", "regNoticeDate:*");
//    	queryMap.put("start", "0");
//    	queryMap.put("rows", "10");
//    	queryMap.put("facet", "on");
//    	queryMap.put("facet.range", "appDate");
//    	queryMap.put("facet.range.start", year1.toString()+"-01-01T00:00:00Z");
//    	queryMap.put("facet.range.end", year2.toString()+"-01-01T00:00:00Z");
//    	queryMap.put("facet.range.gap", "+1YEARS");
//    	logger.info(queryMap);
//    	try {
//    		for(Count a:(List<Count>)SolrUtil.solrQuery(queryMap,client).getFacetRanges().get(0).getCounts()) {
//    			StatsReturn ret = new StatsReturn();
//    			ret.setYear(Integer.valueOf(a.getValue().substring(0, 4)));
//    			ret.setAmount(new Long((long)a.getCount()));
//    			logger.info(a.getValue()+"  "+a.getCount());
//    			retList.add(ret);
//    		}
//		} catch (Exception e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}
//    	Collections.sort(retList,new StatsReturnByYear());   
//		return retList;
//	}
//	
//    public List<StatsReturn> SelectTradeMarkRegCountByYear(SolrInfo solrInfo,Enterprise enterprise,Integer year1,Integer year2){
//    	List<StatsReturn> retList = new ArrayList<StatsReturn>();
//    	SolrClient client = createNewSolrClient(solrInfo);
//    	Map<String, String> queryMap = new HashMap<String, String>();
//    	queryMap.put("q", "*:*");
//    	if (enterprise.getEntId()!=null) {
//    		queryMap.put("fq", "entId:"+enterprise.getEntId().toString());
//    	}else {
//    		queryMap.put("fq", "govIdTree:"+enterprise.getGovId().toString());
//    		if (qStringByEnt(enterprise)=="")queryMap.put("q", "*:*") ;else queryMap.put("q", qStringByEnt(enterprise));
//    	}
//    	queryMap.put("start", "0");
//    	queryMap.put("rows", "10");
//    	queryMap.put("facet", "on");
//    	queryMap.put("facet.range", "regNoticeDate");
//    	queryMap.put("facet.range.start", year1.toString()+"-01-01T00:00:00Z");
//    	queryMap.put("facet.range.end", String.valueOf(year2.intValue()+1)+"-01-01T00:00:00Z");
//    	queryMap.put("facet.range.gap", "+1YEARS");
//    	logger.info(queryMap);
//    	try {
//    		for(Count a:(List<Count>)SolrUtil.solrQuery(queryMap,client).getFacetRanges().get(0).getCounts()) {
//    			StatsReturn ret = new StatsReturn();
//    			ret.setYear(Integer.valueOf(a.getValue().substring(0, 4)));
//    			ret.setAmount(new Long((long)a.getCount()));
//    			logger.info(a.getValue().substring(0, 4)+"  "+a.getCount());
//    			retList.add(ret);
//    		}
//		} catch (Exception e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}
//    	Collections.sort(retList,new StatsReturnByYear());   	
//		return retList;
//	}
//	//未启用
//    public List<StatsReturn> SelectTradeMarkStCountByCodeAndYear(SolrInfo solrInfo,Enterprise enterprise,Integer year1,Integer year2,
//			String code1,String code2,String code3){
//    	List<StatsReturn> retList = new ArrayList<StatsReturn>();
//    	SolrClient client = createNewSolrClient(solrInfo);
//    	Map<String, String> queryMap = new HashMap<String, String>();
//    	String qString = "" ;
//    	if(code1!=null||code2!=null||code3!=null) {
//    		qString = qString +"processStatus:(";
//    		if (code1!=null) {
//    			qString = qString + "*"+code1+"* ";
//    		}
//    		if (code2!=null) {
//    			qString = qString +"OR *"+code2+"* ";
//    		}
//    		if (code3!=null) {
//    			qString = qString +"OR *"+code3+"* ";
//    		}
//    		qString = qString +")";
//    	}
//    	queryMap.put("q", qString);
//    	if (enterprise.getEntId()!=null) {
//    		queryMap.put("fq", "entId:"+enterprise.getEntId().toString());
//    	}else {
//    		queryMap.put("fq", "govIdTree:"+enterprise.getGovId().toString());
//    	}
//
//    	queryMap.put("start", "0");
//    	queryMap.put("rows", "0");
//    	queryMap.put("facet", "on");
//    	queryMap.put("facet.range", "processStatusDate");
//    	queryMap.put("facet.range.start", year1.toString()+"-01-01T00:00:00Z");
//    	queryMap.put("facet.range.end", year2.toString()+"-01-01T00:00:00Z");
//    	queryMap.put("facet.range.gap", "+1YEARS");
//    	logger.info(queryMap);
//    	try {
//    		for(Count a:(List<Count>)SolrUtil.solrQuery(queryMap,client).getFacetRanges().get(0).getCounts()) {
//    			StatsReturn ret = new StatsReturn();
//    			ret.setYear(Integer.valueOf(a.getValue().substring(0, 4)));
//    			ret.setAmount(new Long((long)a.getCount()));
//    			logger.info(a.getValue()+"  "+a.getCount());
//    			retList.add(ret);
//    		}
//		} catch (Exception e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}
//		return retList;
//	}
//	//未启用
//    public List<StatsReturn> SelectTradeMarkRegCountByYearHundredsPeople(SolrInfo solrInfo,Integer govId,Integer year1,Integer year2){
//    	//TODO
//    	return null;
//	}
//	class AmountReturnSortByAmount implements Comparator<AmountReturnByAllvalid> {
//		public int compare(AmountReturnByAllvalid s1,AmountReturnByAllvalid s2) {
//			return -(s1.getAmount().compareTo(s2.getAmount()));
//			//         if (s1.getAge() > s2.getAge())
//			//          return 1;
//			//         return -1;
//		}
//	}
//    //以下为原tmTradeMarkAmountMapper内容
//    public List<AmountReturnByAllvalid> selectAllAppliedByArea(SolrInfo solrInfo,Integer govId,List<Government> govmentlist){
//    	Map<Integer,String> govIdList = new HashMap<Integer,String>();
//    	for(Government gov:govmentlist) {
//    		govIdList.put(gov.getGovId(), gov.getGovName());    		
//    	}
//		List<AmountReturnByAllvalid> retlist = new ArrayList<AmountReturnByAllvalid>();
//    	SolrClient client = createNewSolrClient(solrInfo);
//    	Map<String, String> queryMap = new HashMap<String, String>();
//    	queryMap.put("q", "*:*");
//    	queryMap.put("fq", "govIdTree:"+govId.toString());
//    	queryMap.put("start", "0");
//    	queryMap.put("rows", "0");
//    	queryMap.put("facet", "on");
//    	queryMap.put("facet.sort", "count");
//    	Map<String, List<String>> queryMapList = new HashMap<String, List<String>>();
//    	List<String> queryList= new ArrayList<String>();
//    	for(Map.Entry<Integer, String> gid:govIdList.entrySet()) {
//    		Integer id = gid.getKey();
//    		if (id.intValue() == govId.intValue()){
//    			queryList.add("{!key=\""+id.toString()+"\"}govId:"+id.toString());
//    		} else {
//    			queryList.add("{!key=\""+id.toString()+"\"}govIdTree:"+id.toString());
//    		}
//    	}
//    	queryMapList.put("facet.query",queryList);
//    	try {
//    		Map<String, Integer> solrFacetQuery = SolrUtil.solrQuery(queryMap,queryMapList,client).getFacetQuery();
//			for(Map.Entry<String, Integer> a:solrFacetQuery.entrySet()) {
//				String key = a.getKey();
//				Integer retGovId =Integer.valueOf(key);
//    			String retGovName =govIdList.get(retGovId);
//    			AmountReturnByAllvalid ret = new AmountReturnByAllvalid();
//    			ret.setGovId(retGovId);
//    			ret.setGovName(retGovName);
//    			ret.setAmount(new Long(a.getValue().intValue()));
//    			retlist.add(ret);
//			}
//		} catch (Exception e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}
//    	Collections.sort(retlist,new AmountReturnSortByAmount());
//    	return retlist;
//	}
//
//    public List<AmountReturnByAllvalid> selectAllValidByArea(SolrInfo solrInfo,Integer govId,List<Government> govmentlist){
//    	Map<Integer,String> govIdList = new HashMap<Integer,String>();
//    	for(Government gov:govmentlist) {
//    		govIdList.put(gov.getGovId(), gov.getGovName());    		
//    	}
//		List<AmountReturnByAllvalid> retlist = new ArrayList<AmountReturnByAllvalid>();
//    	SolrClient client = createNewSolrClient(solrInfo);
//    	Map<String, String> queryMap = new HashMap<String, String>();
//    	queryMap.put("q", "status:已注册");
//    	queryMap.put("fq", "govIdTree:"+govId.toString());
//    	queryMap.put("start", "0");
//    	queryMap.put("rows", "0");
//    	queryMap.put("facet", "on");
//    	Map<String, List<String>> queryMapList = new HashMap<String, List<String>>();
//    	List<String> queryList= new ArrayList<String>();
//    	for(Map.Entry<Integer, String> gid:govIdList.entrySet()) {
//    		Integer id = gid.getKey();
//    		if (id.intValue() == govId.intValue()){
//    			queryList.add("{!key=\""+id.toString()+"\"}govId:"+id.toString());
//    		} else {
//    			queryList.add("{!key=\""+id.toString()+"\"}govIdTree:"+id.toString());
//    		}
//    	}
//    	queryMapList.put("facet.query",queryList);
//    	try {
//    		QueryResponse res = SolrUtil.solrQuery(queryMap,queryMapList,client);
//			for(Map.Entry<String, Integer> a:res.getFacetQuery().entrySet()) {
//				Integer retGovId =Integer.valueOf(a.getKey());
//    			String retGovName =govIdList.get(retGovId);
//    			AmountReturnByAllvalid ret = new AmountReturnByAllvalid();
//    			ret.setGovId(retGovId);
//    			ret.setGovName(retGovName);
//    			ret.setAmount(new Long(a.getValue().intValue()));
//    			retlist.add(ret);
//			}
//		} catch (Exception e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}
//    	Collections.sort(retlist,new AmountReturnSortByAmount());
//    	return retlist;
//	}
//
//    public List<AmountReturnByAllvalidYear> selectNewappliedbyareayear(SolrInfo solrInfo,Integer govId, StatCondition statCondition,List<Government> govmentlist){
//    	Map<Integer,String> govIdList = new HashMap<Integer,String>();
//    	for(Government gov:govmentlist) {
//    		govIdList.put(gov.getGovId(), gov.getGovName());    		
//    	}
//    	List<AmountReturnByAllvalidYear> retyearlist = new ArrayList<AmountReturnByAllvalidYear>();
//    	SolrClient client = createNewSolrClient(solrInfo);
//    	Map<String, String> queryMap = new HashMap<String, String>();
//    	queryMap.put("q", "*:*");
//    	queryMap.put("fq", "govIdTree:"+govId.toString());
//    	queryMap.put("start", "0");
//    	queryMap.put("rows", "0");
//    	queryMap.put("facet", "on");
//    	Map<String, List<String>> queryMapList = new HashMap<String, List<String>>();
//    	List<String> queryList= new ArrayList<String>();
//    	Integer year1 = statCondition.getStartYear();
//    	Integer year2 = statCondition.getEndYear();
//    	for(int i = 0; i < year2 - year1; i++) {
//    		for(Map.Entry<Integer, String> gid:govIdList.entrySet()) {
//    			Integer id = gid.getKey();
//        		if (id.intValue() == govId.intValue()){
//        			queryList.add("{!key=\""+String.valueOf(year1+i)+"YEAR+"+id.toString()+"\"}govId:"+id.toString()+" appDate:["+String.valueOf(year1+i)+"-01-01T00:00:00Z TO "+String.valueOf(year1+i+1)+"-01-01T00:00:00Z}");
//        		} else {
//        			queryList.add("{!key=\""+String.valueOf(year1+i)+"YEAR+"+id.toString()+"\"}govIdTree:"+id.toString()+" appDate:["+String.valueOf(year1+i)+"-01-01T00:00:00Z TO "+String.valueOf(year1+i+1)+"-01-01T00:00:00Z}");
//        		}
//    		}
//    	}
//    	queryMapList.put("facet.query",queryList);
//    	logger.info(queryMap);
//    	try {
//    		Map<Integer,List<Map<String,Object>>> retyearmap= new HashMap<Integer,List<Map<String,Object>>>();
//    		for(Map.Entry<String, Integer> a:SolrUtil.solrQuery(queryMap,client).getFacetQuery().entrySet()) {
//    			Integer retYear =Integer.valueOf(a.getKey().substring(0, 4));
//    			Integer retGovId =Integer.valueOf(a.getKey().substring(8));
//    			String retGovName =govIdList.get(retGovId);
//    			Map<String,Object> retgovmap = new HashMap<String,Object>();
//    			retgovmap.put("govId", retGovId);
//    			retgovmap.put("govName", retGovName);
//    			retgovmap.put("amount", new Long(a.getValue().intValue()));
//    			if(retyearmap.get(retYear)!=null) {
//    				List<Map<String,Object>> retgovmapList = retyearmap.get(retYear);
//    				retgovmapList.add(retgovmap);
//    				retyearmap.put(retYear, retgovmapList);
//    			}else {
//    				List<Map<String,Object>> retgovmapList = new ArrayList<Map<String,Object>>();
//    				retgovmapList.add(retgovmap);
//    				retyearmap.put(retYear, retgovmapList);
//    			}
//    		}
//    		for(Map.Entry<Integer,List<Map<String,Object>>> a:retyearmap.entrySet()) {
//    			AmountReturnByAllvalidYear retyear = new AmountReturnByAllvalidYear();
//    			retyear.setYear(a.getKey());
//    			List<AmountReturnByAllvalid> amountReturnByAllvalid = new ArrayList<AmountReturnByAllvalid>();
//    			for(Map<String,Object> retmap :a.getValue()) {
//    				AmountReturnByAllvalid retgov = new AmountReturnByAllvalid();
//    				retgov.setAmount((Long) retmap.get("amount"));
//    				retgov.setGovId((Integer) retmap.get("govId"));
//    				retgov.setGovName((String) retmap.get("govName"));
//    				amountReturnByAllvalid.add(retgov);
//    			}    			
//				retyear.setAmountReturnByAllvalid(amountReturnByAllvalid);
//				retyearlist.add(retyear);
//    		}
//    		
//		} catch (Exception e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}
////    	Collections.sort(retlist,new AmountReturnSortByAmount());
//		return retyearlist;
//	}
//
//    public List<AmountReturnByAllvalidYear> selectAllApprovalByAreaYear(SolrInfo solrInfo,Integer govId, StatCondition statCondition,List<Government> govmentlist){
//    	Map<Integer,String> govIdList = new HashMap<Integer,String>();
//    	for(Government gov:govmentlist) {
//    		govIdList.put(gov.getGovId(), gov.getGovName());    		
//    	}
//    	List<AmountReturnByAllvalidYear> retyearlist = new ArrayList<AmountReturnByAllvalidYear>();
//    	SolrClient client = createNewSolrClient(solrInfo);
//    	Map<String, String> queryMap = new HashMap<String, String>();
//    	queryMap.put("q", "regNoticeDate:*");
//    	queryMap.put("fq", "govIdTree:"+govId.toString());
//    	queryMap.put("start", "0");
//    	queryMap.put("rows", "0");
//    	queryMap.put("facet", "on");
//    	Map<String, List<String>> queryMapList = new HashMap<String, List<String>>();
//    	List<String> queryList= new ArrayList<String>();
//    	Integer year1 = statCondition.getStartYear();
//    	Integer year2 = statCondition.getEndYear();
//    	for(int i = 0; i < year2 - year1; i++) {
//    		for(Map.Entry<Integer, String> gid:govIdList.entrySet()) {
//    			Integer id = gid.getKey();
//        		if (id.intValue() == govId.intValue()){
//        			queryList.add("{!key=\""+String.valueOf(year1+i)+"YEAR+"+id.toString()+"\"}govId:"+id.toString()+" appDate:["+String.valueOf(year1+i)+"-01-01T00:00:00Z TO "+String.valueOf(year1+i+1)+"-01-01T00:00:00Z}");
//        		} else {
//        			queryList.add("{!key=\""+String.valueOf(year1+i)+"YEAR+"+id.toString()+"\"}govIdTree:"+id.toString()+" appDate:["+String.valueOf(year1+i)+"-01-01T00:00:00Z TO "+String.valueOf(year1+i+1)+"-01-01T00:00:00Z}");
//        		}
//        	}
//    	}
//    	queryMapList.put("facet.query",queryList);
//    	logger.info(queryMap);
//    	try {
//    		Map<Integer,List<Map<String,Object>>> retyearmap= new HashMap<Integer,List<Map<String,Object>>>();
//    		for(Map.Entry<String, Integer> a:SolrUtil.solrQuery(queryMap,client).getFacetQuery().entrySet()) {
//    			Integer retYear =Integer.valueOf(a.getKey().substring(0, 4));
//    			Integer retGovId =Integer.valueOf(a.getKey().substring(8));
//    			String retGovName =govIdList.get(retGovId);
//    			Map<String,Object> retgovmap = new HashMap<String,Object>();
//    			retgovmap.put("govId", retGovId);
//    			retgovmap.put("govName", retGovName);
//    			retgovmap.put("amount", new Long(a.getValue().intValue()));
//    			if(retyearmap.get(retYear)!=null) {
//    				List<Map<String,Object>> retgovmapList = retyearmap.get(retYear);
//    				retgovmapList.add(retgovmap);
//    				retyearmap.put(retYear, retgovmapList);
//    			}else {
//    				List<Map<String,Object>> retgovmapList = new ArrayList<Map<String,Object>>();
//    				retgovmapList.add(retgovmap);
//    				retyearmap.put(retYear, retgovmapList);
//    			}
//    		}
//    		for(Map.Entry<Integer,List<Map<String,Object>>> a:retyearmap.entrySet()) {
//    			AmountReturnByAllvalidYear retyear = new AmountReturnByAllvalidYear();
//    			retyear.setYear(a.getKey());
//    			List<AmountReturnByAllvalid> amountReturnByAllvalid = new ArrayList<AmountReturnByAllvalid>();
//    			for(Map<String,Object> retmap :a.getValue()) {
//    				AmountReturnByAllvalid retgov = new AmountReturnByAllvalid();
//    				retgov.setAmount((Long) retmap.get("amount"));
//    				retgov.setGovId((Integer) retmap.get("govId"));
//    				retgov.setGovName((String) retmap.get("govName"));
//    				amountReturnByAllvalid.add(retgov);
//    			}    			
//				retyear.setAmountReturnByAllvalid(amountReturnByAllvalid);
//				retyearlist.add(retyear);
//    		}
//    		
//		} catch (Exception e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}
////    	Collections.sort(retlist,new AmountReturnSortByAmount());
//		return retyearlist;
//	}
//
//    public List<AmountReturnByAllvalid> selectAllApprovedByDeadent(SolrInfo solrInfo,Integer govId,List<Government> govmentlist){
//    	Map<Integer,String> govIdList = new HashMap<Integer,String>();
//    	for(Government gov:govmentlist) {
//    		govIdList.put(gov.getGovId(), gov.getGovName());    		
//    	}
//		List<AmountReturnByAllvalid> retlist = new ArrayList<AmountReturnByAllvalid>();
//    	SolrClient client = createNewSolrClient(solrInfo);
//    	Map<String, String> queryMap = new HashMap<String, String>();
//    	queryMap.put("q", "entStatus:(3 OR 2)");
//    	queryMap.put("fq", "govIdTree:"+govId.toString());
//    	queryMap.put("start", "0");
//    	queryMap.put("rows", "0");
//    	queryMap.put("facet", "on");
//    	Map<String, List<String>> queryMapList = new HashMap<String, List<String>>();
//    	List<String> queryList= new ArrayList<String>();
//    	for(Map.Entry<Integer, String> gid:govIdList.entrySet()) {
//    		Integer id = gid.getKey();
//    		if (id.intValue() == govId.intValue()){
//    			queryList.add("{!key=\""+id.toString()+"\"}govId:"+id.toString());
//    		} else {
//    			queryList.add("{!key=\""+id.toString()+"\"}govIdTree:"+id.toString());
//    		}
//    	}
//    	queryMapList.put("facet.query",queryList);
//    	try {
//			for(Map.Entry<String, Integer> a:SolrUtil.solrQuery(queryMap,client).getFacetQuery().entrySet()) {
//				Integer retGovId =Integer.valueOf(a.getKey());
//    			String retGovName =govIdList.get(retGovId);
//    			AmountReturnByAllvalid ret = new AmountReturnByAllvalid();
//    			ret.setGovId(retGovId);
//    			ret.setGovName(retGovName);
//    			ret.setAmount(new Long(a.getValue().intValue()));
//    			retlist.add(ret);
//			}
//		} catch (Exception e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}
//    	Collections.sort(retlist,new AmountReturnSortByAmount());
//    	return retlist;
//	}
//    
//    //以下为原trademarkRankMapper内容
//    public List<Map<String, Object>> newAppliedTop20(SolrInfo solrInfo,Integer govId,StatCondition sc,List<Map<String,Object>> govIdBaseMap){
//    	Map<Integer,String> govIdMap = new HashMap<Integer,String>();
//    	for(Map<String,Object> gov:govIdBaseMap) {
//    		govIdMap.put((Integer)gov.get("govId"),(String)gov.get("name"));    		
//    	}
//    	List<Map<String, Object>> retlist = new ArrayList<Map<String, Object>>();
//    	SolrClient client = createNewSolrClient(solrInfo);
//    	Map<String, String> queryMap = new HashMap<String, String>();
//    	queryMap.put("fq", "govIdTree:"+govId.toString());
//    	String startYear ="*";
//    	String endYear ="*";
//    	if(sc.getStartYear()!=null)startYear=sc.getStartYear().toString();
//    	if(sc.getEndDate()!=null)endYear=sc.getEndDate().toString();
//    	queryMap.put("q", "appDateYear:["+startYear+" TO "+endYear+"]");
//    	queryMap.put("start", "0");
//    	queryMap.put("rows", "0");
//    	queryMap.put("facet", "on");
//    	queryMap.put("facet.pivot","appDateYear,applicantId,applicantName,govId");
//    	
//    	Map<String,Map<Integer,List<String[]>>>  zzlist = new TreeMap<String,Map<Integer,List<String[]>>>(
//                new Comparator<String>() {
//                    public int compare(String obj1, String obj2) {
//                        // 降序排序
//                        return obj2.compareTo(obj1);
//                    }
//                });
//    	try {
//			List<PivotField> list = SolrUtil.solrQuery(queryMap,client).getFacetPivot().getVal(0);
//			for(PivotField yearList:list) {
//				String year = (String) yearList.getValue();
//				Map<Integer,List<String[]>> zzyearlist = new TreeMap<Integer,List<String[]>>(
//		                new Comparator<Integer>() {
//		                    public int compare(Integer obj1, Integer obj2) {
//		                        // 降序排序
//		                        return obj2.compareTo(obj1);
//		                    }
//		                });
//				for(PivotField appIdList:yearList.getPivot()) {
//					String appId = (String) appIdList.getValue();
//					for(PivotField appNameList:appIdList.getPivot()) {
//						String appName = (String) appNameList.getValue();
//						for(PivotField appgovIdList:appNameList.getPivot()) {
//							String appgovId = (String) appgovIdList.getValue();
//							Integer count = appgovIdList.getCount();
//							List<String[]> zzCountList = new ArrayList<String[]>();
//							if(zzyearlist.get(count)!=null) {
//								zzCountList =zzyearlist.get(count);
//							}
//							zzCountList.add(new String[]{appId,appName,appgovId});
//							zzyearlist.put(count, zzCountList);
//						}
//					}
//				}
//				zzlist.put(year, zzyearlist);
//			}
//			for(Map.Entry<String,Map<Integer,List<String[]>>> zz1:zzlist.entrySet()) {
//				Map<String, Object> ret0 = new HashMap<String, Object>();
//				int i = 0;
//				List<Object> ret1list = new ArrayList<Object>();
//				for(Map.Entry<Integer,List<String[]>> zz2:zz1.getValue().entrySet()) {
//					for(String[] zz3 :zz2.getValue()) {
//						if(i>=20)break;
//						Map<String,Object> ret1 = new HashMap<String,Object>();
//						ret1.put("applicantId", zz3[0]);
//						ret1.put("appName", zz3[1]);
//						ret1.put("regionName", govIdMap.get(Integer.valueOf(zz3[2])));
//						ret1.put("amount", zz2.getKey());
//						i++;
//						ret1list.add(ret1);
//					}
//
//				}
//				ret0.put("year", zz1.getKey());
//				ret0.put("govId", govId);
//				ret0.put("applicants", ret1list);
//				retlist.add(ret0);
//			}
//		} catch (Exception e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}
//		return retlist;
//	}
//
//    public List<Map<String, Object>> newAppliedTop20ByArea(SolrInfo solrInfo,Integer govId,StatCondition sc,List<Map<String,Object>> govIdBaseMap){
//    	Map<Integer,Map<String,String>> govIdMap = new HashMap<Integer,Map<String,String>>();
//    	for(Map<String,Object> gov:govIdBaseMap) {
//    		HashMap<String,String> govMap =new HashMap<String,String>();
//    		govMap.put("regName", (String)gov.get("name"));
//    		govMap.put("govName", (String)gov.get("govName"));
//    		govMap.put("regNumber", (String)gov.get("id").toString());
//    		govIdMap.put((Integer)gov.get("govId"),govMap);    		
//    	}
//    	List<Map<String, Object>> retlist = new ArrayList<Map<String, Object>>();
//    	SolrClient client = createNewSolrClient(solrInfo);
//    	Map<String, String> queryMap = new HashMap<String, String>();
//    	queryMap.put("fq", "govIdTree:"+govId.toString());
//    	String startYear ="*";
//    	String endYear ="*";
//    	if(sc.getStartYear()!=null)startYear=sc.getStartYear().toString();
//    	if(sc.getEndDate()!=null)endYear=sc.getEndDate().toString();
//    	queryMap.put("q", "appDateYear:["+startYear+" TO "+endYear+"]");
//    	queryMap.put("start", "0");
//    	queryMap.put("rows", "0");
//    	queryMap.put("facet", "on");
//    	queryMap.put("facet.pivot","appDateYear,govId,applicantId,applicantName");
//    	
//    	Map<String,Map<String,Map<Integer,List<String[]>>>>  zzlist = new TreeMap<String,Map<String,Map<Integer,List<String[]>>>>(
//                new Comparator<String>() {
//                    public int compare(String obj1, String obj2) {
//                        // 降序排序
//                        return obj2.compareTo(obj1);
//                    }
//                });
//    	try {
//			List<PivotField> list = SolrUtil.solrQuery(queryMap,client).getFacetPivot().getVal(0);
//			for(PivotField yearList:list) {
//				String year = (String) yearList.getValue();
//				Map<String,Map<Integer,List<String[]>>> zzgovlist = new HashMap<String,Map<Integer,List<String[]>>>();
//				for(PivotField appgovIdList:yearList.getPivot()) {
//					String appgovId = (String) appgovIdList.getValue();
//					Map<Integer,List<String[]>> zzyearlist = new TreeMap<Integer,List<String[]>>(
//			                new Comparator<Integer>() {
//			                    public int compare(Integer obj1, Integer obj2) {
//			                        // 降序排序
//			                        return obj2.compareTo(obj1);
//			                    }
//			                });
//					for(PivotField appIdList:appgovIdList.getPivot()) {
//						String appId = (String) appIdList.getValue();
//						for(PivotField appNameList:appIdList.getPivot()) {
//							String appName = (String) appNameList.getValue();
//							Integer count = appIdList.getCount();
//							List<String[]> zzCountList = new ArrayList<String[]>();
//							if(zzyearlist.get(count)!=null) {
//								zzCountList =zzyearlist.get(count);
//							}
//							zzCountList.add(new String[]{appId,appName,appgovId});
//							zzyearlist.put(count, zzCountList);
//						}
//					}
//					zzgovlist.put(appgovId, zzyearlist);
//				}
//				zzlist.put(year, zzgovlist);
//			}
//			for(Map.Entry<String,Map<String,Map<Integer,List<String[]>>>> zz0:zzlist.entrySet()) {
//				Map<String, Object> ret0 = new HashMap<String, Object>();
//				List<Object> ret0list = new ArrayList<Object>();
//				for(Map.Entry<String,Map<Integer,List<String[]>>>zz1:zz0.getValue().entrySet()) {
//					if(govIdMap.get(Integer.valueOf(zz1.getKey())) ==null)break;
//					Map<String, Object> ret00 = new HashMap<String, Object>();
//					int i = 0;
//					List<Object> ret1list = new ArrayList<Object>();
//					for(Map.Entry<Integer,List<String[]>> zz2:zz1.getValue().entrySet()) {
//						for(String[] zz3 :zz2.getValue()) {
//							if(i>=20)break;
//							
//							Map<String,Object> ret1 = new HashMap<String,Object>();
//							ret1.put("applicantId", zz3[0]);
//							ret1.put("appName", zz3[1]);
//							ret1.put("regionName", govIdMap.get(Integer.valueOf(zz1.getKey())).get("regName"));
//							ret1.put("amount", zz2.getKey());
//							i++;
//							ret1list.add(ret1);
//						}
//						ret00.put("regionId", govIdMap.get(Integer.valueOf(zz1.getKey())).get("regNumber"));
//						ret00.put("govId", zz1.getKey());
//						ret00.put("govName", govIdMap.get(Integer.valueOf(zz1.getKey())).get("govName"));
//						ret00.put("applicants", ret1list);
//						ret0list.add(ret00);
//
//					}
//					ret0.put("year", zz0.getKey());
//					ret0.put("govId", govId);
//					ret0.put("governments", ret0list);
//					retlist.add(ret0);
//				}
//			}
//		} catch (Exception e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}
//		return retlist;
//	}
//
//	public List<Map<String, Object>> appliedTop20(SolrInfo solrInfo,Integer govId,StatCondition sc,Enterprise enterprise,List<Map<String,Object>> govIdBaseMap){
//    	Map<Integer,String> govIdMap = new HashMap<Integer,String>();
//    	for(Map<String,Object> gov:govIdBaseMap) {
//    		govIdMap.put((Integer)gov.get("govId"),(String)gov.get("name"));    		
//    	}
//    	List<Map<String, Object>> retlist = new ArrayList<Map<String, Object>>();
//    	SolrClient client = createNewSolrClient(solrInfo);
//    	Map<String, String> queryMap = new HashMap<String, String>();
//    	queryMap.put("fq", "govIdTree:"+govId.toString()+" AND "+qStringByEnt(enterprise));
//    	queryMap.put("q", "*:*");
//    	queryMap.put("start", "0");
//    	queryMap.put("rows", "0");
//    	queryMap.put("facet", "on");
//    	queryMap.put("facet.pivot","entId,entName,govId");
//    	
//
//    	try {
//			List<PivotField> list = SolrUtil.solrQuery(queryMap,client).getFacetPivot().getVal(0);
//				Map<Integer,List<String[]>> zzlist = new TreeMap<Integer,List<String[]>>(
//		                new Comparator<Integer>() {
//		                    public int compare(Integer obj1, Integer obj2) {
//		                        // 降序排序
//		                        return obj2.compareTo(obj1);
//		                    }
//		                });
//				for(PivotField appIdList:list) {
//					String appId = (String) appIdList.getValue();
//					for(PivotField appNameList:appIdList.getPivot()) {
//						String appName = (String) appNameList.getValue();
//						for(PivotField appgovIdList:appNameList.getPivot()) {
//							String appgovId = (String) appgovIdList.getValue();
//							Integer count = appgovIdList.getCount();
//							List<String[]> zzCountList = new ArrayList<String[]>();
//							if(zzlist.get(count)!=null) {
//								zzCountList =zzlist.get(count);
//							}
//							zzCountList.add(new String[]{appId,appName,appgovId});
//							zzlist.put(count, zzCountList);
//						}
//					}
//				}
//			
//
//				Map<String, Object> ret0 = new HashMap<String, Object>();
//				int i = 0;
//				for(Map.Entry<Integer,List<String[]>> zz2:zzlist.entrySet()) {
//					for(String[] zz3 :zz2.getValue()) {
//						if(i>=20)break;
//						Map<String,Object> ret1 = new HashMap<String,Object>();
//						ret1.put("entId", zz3[0]);
//						ret1.put("entName", zz3[1]);
//						ret1.put("regionName", govIdMap.get(Integer.valueOf(zz3[2])));
//						ret1.put("amount", zz2.getKey());
//						i++;
//						retlist.add(ret1);
//					}
//
//				}
//
//			
//		} catch (Exception e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}
//		return retlist;
//	}
//
//	public List<Map<String, Object>> approvedTop20(SolrInfo solrInfo,Integer govId,StatCondition sc,Enterprise enterprise,List<Map<String,Object>> govIdBaseMap){
//    	Map<Integer,String> govIdMap = new HashMap<Integer,String>();
//    	for(Map<String,Object> gov:govIdBaseMap) {
//    		govIdMap.put((Integer)gov.get("govId"),(String)gov.get("name"));    		
//    	}
//    	List<Map<String, Object>> retlist = new ArrayList<Map<String, Object>>();
//    	SolrClient client = createNewSolrClient(solrInfo);
//    	Map<String, String> queryMap = new HashMap<String, String>();
//    	queryMap.put("fq", "govIdTree:"+govId.toString()+" AND "+qStringByEnt(enterprise));
//    	queryMap.put("q", "status:已注册");
//    	queryMap.put("start", "0");
//    	queryMap.put("rows", "0");
//    	queryMap.put("facet", "on");
//    	queryMap.put("facet.pivot","entId,entName,govId");
//    	
//
//    	try {
//			List<PivotField> list = SolrUtil.solrQuery(queryMap,client).getFacetPivot().getVal(0);
//				Map<Integer,List<String[]>> zzlist = new TreeMap<Integer,List<String[]>>(
//		                new Comparator<Integer>() {
//		                    public int compare(Integer obj1, Integer obj2) {
//		                        // 降序排序
//		                        return obj2.compareTo(obj1);
//		                    }
//		                });
//				for(PivotField appIdList:list) {
//					String appId = (String) appIdList.getValue();
//					for(PivotField appNameList:appIdList.getPivot()) {
//						String appName = (String) appNameList.getValue();
//						for(PivotField appgovIdList:appNameList.getPivot()) {
//							String appgovId = (String) appgovIdList.getValue();
//							Integer count = appgovIdList.getCount();
//							List<String[]> zzCountList = new ArrayList<String[]>();
//							if(zzlist.get(count)!=null) {
//								zzCountList =zzlist.get(count);
//							}
//							zzCountList.add(new String[]{appId,appName,appgovId});
//							zzlist.put(count, zzCountList);
//						}
//					}
//				}
//			
//
//				Map<String, Object> ret0 = new HashMap<String, Object>();
//				int i = 0;
//				for(Map.Entry<Integer,List<String[]>> zz2:zzlist.entrySet()) {
//					for(String[] zz3 :zz2.getValue()) {
//						if(i>=20)break;
//						Map<String,Object> ret1 = new HashMap<String,Object>();
//						ret1.put("entId", zz3[0]);
//						ret1.put("entName", zz3[1]);
//						ret1.put("regionName", govIdMap.get(Integer.valueOf(zz3[2])));
//						ret1.put("amount", zz2.getKey());
//						i++;
//						retlist.add(ret1);
//					}
//
//				}
//
//			
//		} catch (Exception e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}
//		return retlist;
//	}
//	
//	class SortByAmount implements Comparator<Map<String,Object>> {
//		public int compare(Map<String,Object> s1, Map<String,Object> s2) {
//			return ((Integer)s1.get("amount")).compareTo((Integer)s2.get("amount"));
//			//         if (s1.getAge() > s2.getAge())
//			//          return 1;
//			//         return -1;
//		}
//	}
//	
//	//----------------------------new chart---------------------------
//	
//
//	 /** 获取统计数量
//	 * @param solrInfo
//	 * @param solrQ 额外查询语句
//	 * @param dimension 统计维度	1:地域；2年度 
//	 * @param govmentlist 分地区相关 查询地域队列
//	 * @param enterprise 企业信息
//	 * @param year1 分年相关 开始日期
//	 * @param year2 分年相关 结束日期
//	 * @param rangeDate 分年相关 分年日期字段
//	 * @param cumulative 分年相关 是否统计累计
//	 * @return
//	 */
//	public List<Map<String,Object>> SelectTradeMarkCountForChart(SolrInfo solrInfo, String solrQ, Integer dimension,Enterprise enterprise,
//			List<Government> govmentlist, Integer year,	
//			Integer year1,Integer year2,String rangeDate,Boolean cumulative){
//		Map<Integer,String> govIdList = new HashMap<Integer,String>();
//		if (govmentlist!=null)
//			for(Government gov:govmentlist) {
//				govIdList.put(gov.getGovId(), gov.getGovName());    		
//			}
//		Integer govId = enterprise.getGovId();
//		List<Map<String,Object>> retList = new ArrayList<Map<String,Object>>();
//		SolrClient client = createNewSolrClient(solrInfo);
//		Map<String, String> queryMap = new HashMap<String, String>();
//		queryMap.put("q", "*:*");
//		if (enterprise.getEntId()!=null) {
//			queryMap.put("fq", "entId:"+enterprise.getEntId().toString());
//		}else {
//			queryMap.put("fq", "govIdTree:"+enterprise.getGovId().toString());
//			if (qStringByEnt(enterprise)!="")queryMap.put("q", qStringByEnt(enterprise));
//		}
//		queryMap.put("q", queryMap.get("q").toString()+" AND "+solrQ);
//		queryMap.put("start", "0");
//		queryMap.put("rows", "0");
//		queryMap.put("facet", "on");	    	
//		Map<String, List<String>> queryMapList = new HashMap<String, List<String>>();
//		List<String> queryList = new ArrayList<String>();
//		switch(dimension) {
//		case 1:
//		{
//			for(Map.Entry<Integer, String> gid:govIdList.entrySet()) {
//				Integer id = gid.getKey();
//				if(year == null) {
//					if (id.intValue() == govId.intValue()){
//						queryList.add("{!key=\""+id.toString()+"\"}govId:"+id.toString());
//					} else {
//						queryList.add("{!key=\""+id.toString()+"\"}govIdTree:"+id.toString());
//					}
//				} else {
//					if(cumulative) {
//						if (id.intValue() == govId.intValue()){
//							queryList.add("{!key=\"A_"+id.toString()+"\"}govId:"+id.toString()+" AND "+rangeDate+"Year:[* TO "+year.toString()+"]");
//							queryList.add("{!key=\"B_"+id.toString()+"\"}govId:"+id.toString()+" AND "+rangeDate+"Year:[* TO "+Integer.valueOf(year-1).toString()+"]");
//						} else {
//							queryList.add("{!key=\"A_"+id.toString()+"\"}govIdTree:"+id.toString()+" AND "+rangeDate+"Year:[* TO "+year.toString()+"]");
//							queryList.add("{!key=\"B_"+id.toString()+"\"}govIdTree:"+id.toString()+" AND "+rangeDate+"Year:[* TO "+Integer.valueOf(year-1).toString()+"]");
//						}
//					}
//					else {
//						if (id.intValue() == govId.intValue()){
//							queryList.add("{!key=\"A_"+id.toString()+"\"}govId:"+id.toString()+" AND "+rangeDate+"Year:"+year.toString());
//							queryList.add("{!key=\"B_"+id.toString()+"\"}govId:"+id.toString()+" AND "+rangeDate+"Year:"+Integer.valueOf(year-1).toString());
//						} else {
//							queryList.add("{!key=\"A_"+id.toString()+"\"}govIdTree:"+id.toString()+" AND "+rangeDate+"Year:"+year.toString());
//							queryList.add("{!key=\"B_"+id.toString()+"\"}govIdTree:"+id.toString()+" AND "+rangeDate+"Year:"+Integer.valueOf(year-1).toString());
//						}
//					}
//				}
//			}
//			break;
//		}
//
//		case 2:
//			for(int i = 0; i < year2 - year1+1; i++) {
//				if(cumulative)
//					queryList.add("{!key=\""+String.valueOf(year1+i)+"\"}"+rangeDate+"Year:[* TO "+String.valueOf(year1+i)+"]");
//				else
//					queryList.add("{!key=\""+String.valueOf(year1+i)+"\"}"+rangeDate+"Year:"+String.valueOf(year1+i));
//			}
//			break;
//		
//		}
//
//		queryMapList.put("facet.query",queryList);
//		logger.info(queryMap);
//		logger.info(queryList);
//		try {
//			QueryResponse res = SolrUtil.solrQuery(queryMap,queryMapList,client);
//			logger.info(res);
//			for(Map.Entry<String, Integer> a:res.getFacetQuery().entrySet()) {
//				Map<String,Object> ret = new HashMap<String,Object>();
//				switch(dimension) {
//				case 1:
//					if(year == null) {
//						Integer retGovId =Integer.valueOf(a.getKey());
//						String retGovName =govIdList.get(retGovId);
//						ret.put("govName",retGovName);
//						ret.put("govId",retGovId);
//					} else {
//						Integer retGovId =Integer.valueOf(a.getKey().substring(2));
//						String retGovName =govIdList.get(retGovId);
//						ret.put("govName",retGovName);
//						ret.put("govId",retGovId);
//						if(a.getKey().charAt(0)=='A') {
//							ret.put("year",year);
//						} else {
//							ret.put("year",year-1);
//						}
//						
//					}
//					break;					
//				case 2:
//					Integer retYear =Integer.valueOf(a.getKey());
//					ret.put("year",retYear);
//					break;
//				}
//				ret.put("count",a.getValue());
//				retList.add(ret);
//			}
//		} catch (Exception e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}
//		if(dimension.equals(2))	Collections.sort(retList,new ChartMapByYear());   	
//		return retList;
//	}
//
//
//	class ChartMapByYear implements Comparator<Map<String,Object>> {
//		public int compare(Map<String,Object> s1,Map<String,Object> s2) {
//			return new Integer(-(Integer.valueOf(s1.get("year").toString()).compareTo(Integer.valueOf(s2.get("year").toString())))).intValue();
//		}
//	}
//	
//	 /** 获取统计数量
//	 * @param solrInfo
//	 * @param solrQ 额外查询语句
//	 * @param dimension 统计维度	1:地域；2年度 
//	 * @param govmentlist 分地区相关 查询地域队列
//	 * @param enterprise 企业信息
//	 * @param year1 分年相关 开始日期
//	 * @param year2 分年相关 结束日期
//	 * @param rangeDate 分年相关 分年日期字段
//	 * @param cumulative 分年相关 是否统计累计
//	 * @return
//	 */
//	public List<Map<String,Object>> SelectTradeMarkTopForChart(SolrInfo solrInfo, String solrQ, Integer dimension,Enterprise enterprise,Integer maxCount,
//			List<Government> govmentlist, Integer year,	
//			Integer year1,Integer year2,String rangeDate,Boolean cumulative){
//		Map<Integer,String> govIdList = new HashMap<Integer,String>();
//		if (govmentlist!=null)
//			for(Government gov:govmentlist) {
//				govIdList.put(gov.getGovId(), gov.getGovName());    		
//			}
//		Integer govId = enterprise.getGovId();
//		List<Map<String,Object>> retList = new ArrayList<Map<String,Object>>();
//		SolrClient client = createNewSolrClient(solrInfo);
//		Map<String, String> queryMap = new HashMap<String, String>();
//		queryMap.put("q", "*:*");
//		if (enterprise.getEntId()!=null) {
//			queryMap.put("fq", "entId:"+enterprise.getEntId().toString());
//		}else {
//			queryMap.put("fq", "govIdTree:"+enterprise.getGovId().toString());
//			if (qStringByEnt(enterprise)!="")queryMap.put("q", qStringByEnt(enterprise));
//		}
//		queryMap.put("q", queryMap.get("q").toString()+" AND "+solrQ);
//		queryMap.put("start", "0");
//		queryMap.put("rows", "0");
//		queryMap.put("facet", "on");	 
//		switch(dimension) {
//		case 1:
//			if(year == null) {
//				queryMap.put("facet.pivot","govIdTree,applicantName");
//			} else {
//				queryMap.put("facet.pivot","govIdTree,applicantName");
//				if(cumulative) {
//					queryMap.put("q", queryMap.get("q").toString()+" AND "+rangeDate+"Year:[* TO "+year.toString()+"]");
//				} else {
//					queryMap.put("q", queryMap.get("q").toString()+" AND "+rangeDate+"Year:"+year.toString());
//				}
//			}
//			break;
//		case 2:
//			queryMap.put("q", queryMap.get("q").toString()+" AND "+rangeDate+"Year:["+year1.toString()+" TO "+year2.toString()+"]");
//			queryMap.put("facet.pivot","applicantName,"+rangeDate+"Year");
//		}
//
//		try {
//			List<PivotField> list = SolrUtil.solrQuery(queryMap,client).getFacetPivot().getVal(0);
//			Map<Integer,List<String[]>> zzlist = new TreeMap<Integer,List<String[]>>(
//					new Comparator<Integer>() {
//						public int compare(Integer obj1, Integer obj2) {
//							// 降序排序
//							return obj2.compareTo(obj1);
//						}
//					});
//			switch(dimension) {
//			case 1:
//				for(PivotField appgovIdList:list) {
//					String appgovId = (String) appgovIdList.getValue();
//					for(PivotField appNameList:appgovIdList.getPivot()) {
//						String appName = (String) appNameList.getValue();
//						Integer count = appNameList.getCount();
//						List<String[]> zzCountList = new ArrayList<String[]>();
//						if(zzlist.get(count)!=null) {
//							zzCountList =zzlist.get(count);
//						}
//						zzCountList.add(new String[]{appgovId,appName});
//						zzlist.put(count, zzCountList);
//					}
//
//				}
//
//				break;
//			case 2:					
//				for(PivotField appNameList:list) {
//					String appName = (String) appNameList.getValue();
//					if (!cumulative) {
//						for(PivotField appYearList:appNameList.getPivot()) {
//							String appYear = (String) appYearList.getValue();
//							Integer count = appYearList.getCount();
//							List<String[]> zzCountList = new ArrayList<String[]>();
//							if(zzlist.get(count)!=null) {
//								zzCountList =zzlist.get(count);
//							}
//							zzCountList.add(new String[]{appYear,appName});
//							zzlist.put(count, zzCountList);
//						}
//					} else {
//						Map<Integer,Integer> cumulativelist = changePivotFieldToCumulativeCount(appNameList.getPivot());
//						for(Map.Entry<Integer, Integer> a : cumulativelist.entrySet()) {
//							String appYear = a.getKey().toString();
//							Integer count = a.getValue();
//							List<String[]> zzCountList = new ArrayList<String[]>();
//							if(zzlist.get(count)!=null) {
//								zzCountList =zzlist.get(count);
//							}
//							zzCountList.add(new String[]{appYear,appName});
//							zzlist.put(count, zzCountList);
//						}
//						
//					}
//				}
//			}
//
//
//			Map<Integer,List<Map<String,Object>>> topmap = new TreeMap<Integer,List<Map<String,Object>>>(
//					new Comparator<Integer>() {
//						public int compare(Integer obj1, Integer obj2) {
//							// 降序排序
//							return obj2.compareTo(obj1);
//						}
//					});
//			
//			for(Map.Entry<Integer,List<String[]>> zz2:zzlist.entrySet()) {
//				for(String[] zz3 :zz2.getValue()) {
//					List<Map<String,Object>> apptoplist = new ArrayList<Map<String,Object>>(); 
//					if(topmap.containsKey(Integer.valueOf(zz3[0]))) {
//						apptoplist=topmap.get(Integer.valueOf(zz3[0]));
//					}		
//					if(apptoplist.size()<maxCount) {
//						Map<String,Object> apptop = new HashMap<String,Object>();
//						apptop.put("count",zz2.getKey());
//						apptop.put("appName",zz3[1]);
//						apptoplist.add(apptop);
//						topmap.put(Integer.valueOf(zz3[0]), apptoplist);
//					}
//				}
//			}
//			
//			for(Map.Entry<Integer,List<Map<String,Object>>> topOne:topmap.entrySet()) {
//				Map<String,Object> apptop = new HashMap<String,Object>();
//				apptop.put("data",topOne.getValue());
//				switch(dimension) {
//				case 1:
//					//判断是否回传显示
//					if(!govId.equals(topOne.getKey()))continue;
//					apptop.put("govName",govIdList.get(topOne.getKey()));
//					break;
//				case 2:
//					apptop.put("year",topOne.getKey());
//				}
//				retList.add(apptop);
//				
//			}
//
//		} catch (Exception e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}
//		return retList;
//	}
//
//
//
//	private Map<Integer, Integer> changePivotFieldToCumulativeCount(List<PivotField> pivot) {
//		Map<Integer,Integer> retlist = new HashMap<Integer,Integer>();
//		Map<Integer,Integer> yearmap = new TreeMap<Integer,Integer>(	new Comparator<Integer>() {
//			public int compare(Integer obj1, Integer obj2) {
//				// 降序排序
//				return -obj2.compareTo(obj1);
//			}
//		});
//		for(PivotField appYearList:pivot) {
//			yearmap.put(Integer.valueOf(appYearList.getValue().toString()), appYearList.getCount());
//		}
//		
//		Integer count = 0;
//		
//		for(Map.Entry<Integer, Integer> a:yearmap.entrySet()) {
//			
//			count = count + a.getValue();
//			retlist.put(a.getKey(), count);
//		}
//		
//		return retlist;
//	}
//	

}
