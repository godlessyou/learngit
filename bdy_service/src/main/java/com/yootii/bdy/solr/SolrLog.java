package com.yootii.bdy.solr;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.xml.Log4jEntityResolver;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.log.model.AuditLogEntity;
import com.yootii.bdy.trademark.model.Trademark;
import com.yootii.bdy.util.SolrUtil;

public class SolrLog {
	
	 private SolrClient createNewSolrClient(SolrInfo solrInfo) {
	        try {
	            System.out.println("server address:" + solrInfo.getSolrHome()+"bdylog");
	            HttpSolrClient client = new HttpSolrClient(solrInfo.getSolrHome()+"bdylog");
	            client.setConnectionTimeout(solrInfo.getSolrConnectTimeOut());
	            client.setDefaultMaxConnectionsPerHost(solrInfo.getSolrDefaultMaxConnect());
	            client.setMaxTotalConnections(solrInfo.getSolrMaxTotalConnect());
	            client.setSoTimeout(solrInfo.getSolrConnectTimeOut());
	            return client;
	        } catch (Exception ex) {
	            throw new RuntimeException(ex);
	        }
	    }
	 public void createDocs(SolrInfo solrInfo,SolrData loglist) throws Exception {
	        System.out.println("======================add doc ===================");
	        Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
	    	SolrClient client = createNewSolrClient(solrInfo);
	        try {
	        	docs = getSolrDocByLog(loglist);

	            UpdateResponse rsp = client.add(docs);
	            System.out
	                    .println("Add doc size" + docs.size() + " result:" + rsp.getStatus() + " Qtime:" + rsp.getQTime());

	            UpdateResponse rspcommit = client.commit();
	            System.out.println("commit doc to index" + " result:" + rsp.getStatus() + " Qtime:" + rsp.getQTime());

	        } catch (Exception e) {
	            e.printStackTrace();
	            throw e;
	        } finally {
	        	client.close();
	        }
	    }
	private Collection<SolrInputDocument> getSolrDocByLog(SolrData loglist) {
		  Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
	        
			List<Map<String, Object>> list = loglist.getDataTable();
			for (Map<String, Object> map:list) {
				SolrInputDocument doc = new SolrInputDocument();
				for (Map.Entry<String, Object> m : map.entrySet()) {
					//if (trademark.getKey().equals("tmImage") ) continue;
					doc.addField(m.getKey(), m.getValue());
				}
				docs.add(doc);			
			}
	        
	        return docs;
		
		
	}
	/*
	 * 日志查询
	 * 输入参数通过SOLR源进行日志查询	
	 */
	public ReturnInfo selectLog(GeneralCondition gcon,
			AuditLogEntity log,SolrInfo solrInfo,
			ReturnInfo returnInfo) throws Exception {
		SolrClient client = createNewSolrClient(solrInfo);
    	try {
    		
	    	Map<String, String> queryMap = new HashMap<String, String>();
	    	queryMap.put("q", getQueryStringBySelectLog(gcon, log));
	    	queryMap.put("start", String.valueOf(gcon.getOffset()));
	    	queryMap.put("rows", String.valueOf(gcon.getrows()));
	    	if(gcon.getOrderCol()!=null) {
	    		String sort = gcon.getOrderCol();
	    		if(gcon.getOrderAsc()!=null) sort = sort +" "+gcon.getOrderAsc(); else sort = sort +" DESC";
	    		queryMap.put("sort", sort);
	    	}
    		QueryResponse response = SolrUtil.solrQuery(queryMap,client);
    		List<AuditLogEntity> list = getLogBySolrResults(response.getResults());
    		
    		returnInfo.setData(list);
    		//returnInfo.setData(response.getResults());
    		returnInfo.setTotal(response.getResults().getNumFound());
    		returnInfo.setSuccess(true);
		} catch (Exception e) {
			throw e;
		} finally{
			client.close();
			
		}
    	return returnInfo;
	    	
	}
	
	/** 处理日志查询参数
	 * @param value Map参数，传入查询参数
	 * @return 展现SOLR查询语句。
	 */
	public  String getQueryStringBySelectLog(GeneralCondition gcon,
			AuditLogEntity log) {
	    	String queryList = "";
	    	
	    	if(gcon.getKeyword()!=null) {
	    		queryList = queryList +"(operateUserName:*"+gcon.getKeyword()+"* OR ";
	    		queryList = queryList +"operateCustName:*"+gcon.getKeyword()+"* OR ";
	    		queryList = queryList +"tableName:*"+gcon.getKeyword()+"* OR ";
	    		queryList = queryList +"operate:*"+gcon.getKeyword()+"*) AND ";
	    	} else {
		    	if(log.getOperateUserName()!=null) {
		    		queryList = queryList +"operateUserName:*"+log.getOperateUserName()+"* AND ";
		    	}
		    	if(log.getOperateCustName()!=null) {
		    		queryList = queryList +"operateCustName:*"+log.getOperateCustName()+"* AND ";
		    	}
		    	if(log.getTableName()!=null) {
		    		queryList = queryList +"tableName:*"+log.getTableName()+"* AND ";
		    	}
		    	if(log.getOperate()!=null) {
		    		queryList = queryList +"operate:*"+log.getOperate()+"* AND ";
		    	}
		    
	    	}
	    	
	    	if(log.getOperateType()!=null) {
	    		queryList = queryList +"operateType:"+log.getOperateType()+" AND ";
	    	}
	    	
	    	if(log.getLogId()!=null) {
	    		queryList = queryList +"logId:"+log.getLogId()+" AND ";
	    	}
	    	if(log.getOperateUserId()>0) {
	    		queryList = queryList +"operateUserId:"+log.getOperateUserId()+" AND ";
	    	}
	    	if(log.getOperateCustId()>0) {
	    		queryList = queryList +"operateCustId:"+log.getOperateCustId()+" AND ";
	    	}
	    	if(log.getAgencyId()>0) {
	    		queryList = queryList +"agencyId:"+log.getAgencyId()+" AND ";
	    	}
	    	if(log.getAgencyName()!=null) {
	    		queryList = queryList +"agencyName:"+log.getAgencyName()+" AND ";
	    	}
	    	if(log.getPrimaryKey()>0) {
	    		queryList = queryList +"primaryKey:"+log.getPrimaryKey()+" AND ";
	    	}
	    	
	    	if(gcon.getStartDate()!=null||gcon.getEndDate()!=null) {
	    		String EndDate,StartDate;
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
		    	queryList = queryList + "operateDate:["+StartDate+" TO "+EndDate+"] AND ";
	    	}
	    	queryList =queryList+"*:*"; 
	    	return queryList;
	    }
	
	private List<AuditLogEntity> getLogBySolrResults(SolrDocumentList docs) {
		List<AuditLogEntity> loglist = new ArrayList<AuditLogEntity> ();
		List<String> ignore = new ArrayList<String>();
		//ignore.add("processStatus");
		for(SolrDocument doc : docs) {
			AuditLogEntity log = SolrUtil.SolrDocumentToClass(AuditLogEntity.class,doc,ignore);
			//trademark.setTmImage("/TmImage/"+trademark.getApplicantName().toString()+"/"+trademark.getRegNumber().toString()+".jpg");			
			loglist.add(log);
		}
		return loglist;
	}
	
	
	
	public ReturnInfo selectLogByTable(GeneralCondition gcon, AuditLogEntity log, SolrInfo solrInfo, ReturnInfo returnInfo) throws Exception {
		
		SolrClient client = createNewSolrClient(solrInfo);
    	try {
    		
	    	Map<String, String> queryMap = new HashMap<String, String>();
	    	queryMap.put("q", getQueryStringBySelectLog(gcon, log));
	    	queryMap.put("start", String.valueOf(gcon.getOffset()));
	    	queryMap.put("rows", String.valueOf(gcon.getrows()));
	    	if(gcon.getOrderCol()!=null) {
	    		String sort = gcon.getOrderCol();
	    		if(gcon.getOrderAsc()!=null) sort = sort +" "+gcon.getOrderAsc(); else sort = sort +" DESC";
	    		queryMap.put("sort", sort);
	    	}
    		QueryResponse response = SolrUtil.solrQuery(queryMap,client);
    		List<AuditLogEntity> list = getLogBySolrResults(response.getResults());
    		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		AuditLogEntity logEntity =null;
    		if(list.size()>0) {
    			logEntity = list.get(0);
    		}
    		 
    		
    			
    		for(int j=1; j< list.size();j++) {
				if(formatter.parse(logEntity.getOperateDate()).getTime()<formatter.parse(list.get(j).getOperateDate()).getTime()) {
					logEntity = list.get(j);
				}
			}
    		
    		returnInfo.setData(logEntity);
    		//returnInfo.setData(response.getResults());
    		//returnInfo.setTotal(response.getResults().getNumFound());
    		returnInfo.setSuccess(true);
		} catch (Exception e) {
			throw e;
		} finally{
			client.close();
			
		}
    	return returnInfo;
	}
	public static void main(String[] args) {
		SolrLog c = new SolrLog();
		SolrInfo solrInfo =new SolrInfo();
		ReturnInfo returnInfo =new ReturnInfo();
		GeneralCondition gcon =  new GeneralCondition();
		gcon.setRows(10);
		AuditLogEntity log = new AuditLogEntity();
		log.setPrimaryKey(1);
		log.setTableName("tradeMarkCase");
		ReturnInfo selectLog;
		try {
			//selectLog = c.selectLog(gcon, log, solrInfo, returnInfo);
			selectLog = c.selectLogByTable(gcon, log, solrInfo, returnInfo);
			System.out.println(selectLog.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
