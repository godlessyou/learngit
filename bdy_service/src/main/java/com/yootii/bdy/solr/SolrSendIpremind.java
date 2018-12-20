//package com.yootii.bqt.solr;
//
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.TreeMap;
//
//import org.apache.ibatis.annotations.Param;
//import org.apache.solr.client.solrj.SolrClient;
//import org.apache.solr.client.solrj.SolrQuery;
//import org.apache.solr.client.solrj.SolrServerException;
//import org.apache.solr.client.solrj.impl.HttpSolrClient;
//import org.apache.solr.client.solrj.response.Group;
//import org.apache.solr.client.solrj.response.GroupCommand;
//import org.apache.solr.client.solrj.response.PivotField;
//import org.apache.solr.client.solrj.response.QueryResponse;
//import org.apache.solr.client.solrj.response.RangeFacet.Count;
//import org.apache.solr.client.solrj.response.UpdateResponse;
//import org.apache.solr.common.SolrDocument;
//import org.apache.solr.common.SolrDocumentList;
//import org.apache.solr.common.SolrInputDocument;
//import org.apache.solr.common.util.NamedList;
//import org.springframework.stereotype.Service;
//
//import com.yootii.bqt.common.GeneralCondition;
//import com.yootii.bqt.common.ReturnInfo;
//import com.yootii.bqt.common.StatCondition;
//import com.yootii.bqt.common.StatsReturn;
//import com.yootii.bqt.ipremind.model.Songdagonggao;
//import com.yootii.bqt.ipremind.model.Tmdongtai;
//import com.yootii.bqt.ipremind.model.Xuzhan;
//import com.yootii.bqt.trademark.model.AmountReturnByAllvalid;
//import com.yootii.bqt.trademark.model.AmountReturnByAllvalidYear;
//import com.yootii.bqt.trademark.model.Enterprise;
//import com.yootii.bqt.trademark.model.Government;
//import com.yootii.bqt.trademark.model.Trademark;
//import com.yootii.bqt.util.SolrUtil;
//
//public class SolrSendIpremind  {
//
//
//	private SolrClient createNewSolrClient(SolrInfo solrInfo) {
//		try {
//			System.out.println("server address:" + solrInfo.getSolrHome()+"bqttixing");
//			HttpSolrClient client = new HttpSolrClient(solrInfo.getSolrHome()+"bqttixing");
//			client.setConnectionTimeout(solrInfo.getSolrConnectTimeOut());
//			client.setDefaultMaxConnectionsPerHost(solrInfo.getSolrDefaultMaxConnect());
//			client.setMaxTotalConnections(solrInfo.getSolrMaxTotalConnect());
//			client.setSoTimeout(solrInfo.getSolrConnectTimeOut());
//			return client;
//		} catch (Exception ex) {
//			throw new RuntimeException(ex);
//		}
//	}
//
//
//	public void createDocs(SolrInfo solrInfo,SolrData mainList,List<SolrData> otherDataList) throws Exception {
//        System.out.println("======================add doc ===================");
//        Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
//    	SolrClient client = createNewSolrClient(solrInfo);
//        try {
//        	docs = getSolrDocByMainList(mainList, otherDataList);
//
//            UpdateResponse rsp = client.add(docs);
//            System.out
//                    .println("Add doc size" + docs.size() + " result:" + rsp.getStatus() + " Qtime:" + rsp.getQTime());
//
//            UpdateResponse rspcommit = client.commit();
//            System.out.println("commit doc to index" + " result:" + rsp.getStatus() + " Qtime:" + rsp.getQTime());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        } finally {
//        	client.close();
//        }
//    }
//
//
//	private Collection<SolrInputDocument> getSolrDocByMainList(SolrData mainList, List<SolrData> otherDataList) {
//		Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
//		docs = getSolrDocbyMainlist(mainList);
//		for(SolrData otherData : otherDataList) {
//			docs = changeSolrDocByOtherData(otherData,docs,"appName");
//		}
//		return docs;
//	}
//	
//	
//    public Collection<SolrInputDocument> getSolrDocbyMainlist(SolrData mainList) {
//        Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
//        
//		List<Map<String, Object>> mainMapList = mainList.getDataTable();
//		for (Map<String, Object> mainMap:mainMapList) {
//			SolrInputDocument doc = new SolrInputDocument();
//			for (Map.Entry<String, Object> main : mainMap.entrySet()) {
//				if (main.getKey().equals("tmImage") ) continue;
//				doc.addField(main.getKey(), main.getValue());
//			}
//			docs.add(doc);			
//		}
//        
//        return docs;
//		
//	}
//    
//    public Collection<SolrInputDocument> changeSolrDocByOtherData(SolrData otherData,Collection<SolrInputDocument> docs,String docsIdName) {
//        Map<Object,List<Map<String,Object>>> multMap = new HashMap<Object, List<Map<String,Object>>>();
//    	List<Map<String, Object>> otherDataMapList = otherData.getDataTable();
//		String idName = otherData.getIdName();
//		for (Map<String, Object> otherDataMap:otherDataMapList) {
//			Object key = otherDataMap.get(idName);
//			List<Map<String,Object>> multMapValue = new ArrayList<Map<String,Object>>();
//			if(multMap.get(key)==null) {
//        		
//        	} else {
//        		multMapValue=multMap.get(key);
//        	}
//        	multMapValue.add(otherDataMap);
//        	multMap.put(key, multMapValue);
//		}
//		docs = updateDocByMuitMap(multMap,docs,docsIdName);
//		
//		return docs;
//    }
//
//
//
//    public Collection<SolrInputDocument> updateDocByMuitMap(Map<Object, List<Map<String, Object>>> multMap,Collection<SolrInputDocument> docs,String docsIdName) {
//    	for(SolrInputDocument doc:docs) {
//    		if(doc.get(docsIdName)==null)continue;
//    		Object id = doc.get(docsIdName).getFirstValue();
//    		if (multMap.get(id) != null) {
//    			List<Map<String,Object>> multMapValue= multMap.get(id);
//    			for(Map<String,Object> multMapValueMap:multMapValue) {
//    				for(Map.Entry<String, Object> entry : multMapValueMap.entrySet()) {
//    					if (entry.getKey().equals(docsIdName) ) continue;
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
//    					doc.addField(entry.getKey(), entry.getValue());    					
//    				}
//    			}
//    		}
//    		
//    	}
//    	return docs;
//    }   
//
//
//	public int updateStatusByKey(String id,SolrInfo solrInfo) {
//		System.out.println("======================updateField ===================");
//		SolrClient client = createNewSolrClient(solrInfo);
//		HashMap<String, Object> oper = new HashMap<String, Object>();
//		//	        多值更新方法
//		//	        List<String> mulitValues = new ArrayList<String>();
//		//	        mulitValues.add(fieldName);
//		//	        mulitValues.add((String)fieldValue);
//		oper.put("set", "已处理");
//
//		SolrInputDocument doc = new SolrInputDocument();
//		doc.addField("id", id);
//		doc.addField("status", oper);
//		try {
//			UpdateResponse rsp = client.add(doc);
//			System.out.println("update doc id" + id + " result:" + rsp.getStatus() + " Qtime:" + rsp.getQTime());
//			UpdateResponse rspCommit = client.commit();
//			System.out.println("commit doc to index" + " result:" + rspCommit.getStatus() + " Qtime:" + rspCommit.getQTime());
//			return 1;
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return 0;
//
//	}
//
//	public ReturnInfo selectSongDaByGovId(Integer govId, String ggType,GeneralCondition gcon,SolrInfo solrInfo,Integer lestYear,
//			ReturnInfo returnInfo) throws Exception{
//		SolrClient client = createNewSolrClient(solrInfo);
//		try {
//			Map<String, String> queryMap = new HashMap<String, String>();
//			queryMap.put("q", getQueryStringByselectSongDa(govId, ggType,gcon.getKeyword(),lestYear));
//			queryMap.put("fq", "txType:songdagonggao");
//			queryMap.put("start", gcon.getOffset().toString());
//			queryMap.put("rows", gcon.getRows().toString());
//			if(gcon.getOrderCol()!=null) {
//				String sort = gcon.getOrderCol();
//				if(gcon.getOrderAsc()!=null) sort = sort +" "+gcon.getOrderAsc(); else sort = sort +" DESC";
//				queryMap.put("sort", sort);
//			}
//			QueryResponse response = SolrUtil.solrQuery(queryMap,client);
//			returnInfo.setData(getSongdagonggaolistBySolrResults(response.getResults()));
//			returnInfo.setTotal(response.getResults().getNumFound());
//		} catch (Exception e) {
//			throw e;
//		} finally{
//			client.close();
//
//		}
//		return returnInfo;
//
//	}
//	public ReturnInfo selecttmdongtaiByGovId(Integer govId,GeneralCondition gcon,SolrInfo solrInfo,Integer lestYear,
//			ReturnInfo returnInfo) throws Exception{
//		SolrClient client = createNewSolrClient(solrInfo);
//		try {
//			Map<String, String> queryMap = new HashMap<String, String>();
//			queryMap.put("q", getQueryStringByselectTmDongTai(govId, gcon.getKeyword(),lestYear));
//			queryMap.put("fq", "txType:tmdongtai");
//			queryMap.put("start", gcon.getOffset().toString());
//			queryMap.put("rows", gcon.getRows().toString());
//			if(gcon.getOrderCol()!=null) {
//				String sort = gcon.getOrderCol();
//				if(gcon.getOrderAsc()!=null) sort = sort +" "+gcon.getOrderAsc(); else sort = sort +" DESC";
//				queryMap.put("sort", sort);
//			}
//			QueryResponse response = SolrUtil.solrQuery(queryMap,client);
//			returnInfo.setData(getTmDongTailistBySolrResults(response.getResults()));
//			returnInfo.setTotal(response.getResults().getNumFound());
//		} catch (Exception e) {
//			throw e;
//		} finally{
//			client.close();
//
//		}
//		return returnInfo;
//
//	}
//	public Map<String,List<String>> selecttmdongtaiByKeyWord(List<String> list,SolrInfo solrInfo) throws Exception{
//		SolrClient client = createNewSolrClient(solrInfo);
//		 Map<String,List<String>> ret = new HashMap<String,List<String>>();
//		try {
//			Map<String, String> queryMap = new HashMap<String, String>();
//			queryMap.put("q", "status:未处理");
//			queryMap.put("fq", "txType:tmdongtai");
//			queryMap.put("start", "0");
//			queryMap.put("rows", String.valueOf(Integer.MAX_VALUE));
//			queryMap.put("group.format", "simple");
//			queryMap.put("group", "on");
//			queryMap.put("fl", "regNumber");
//			Map<String,List<String>> multlist = new HashMap<String,List<String>>();
//			List<String> groupQueryList = new ArrayList<String>();
//			for(String val :list) {
//				groupQueryList.add("rTitle:"+val);
//			}
//			multlist.put("group.query", groupQueryList);
//			QueryResponse response = SolrUtil.solrQuery(queryMap,multlist,client);
//			System.out.println(response);
//			if(response.getGroupResponse()!=null) {
//				List<GroupCommand> groupList = response.getGroupResponse().getValues();
//				for(GroupCommand groupValue:groupList) {
//					List<String> regList = new ArrayList<String>();
//					for(Group docVal: groupValue.getValues()) {
//						for(SolrDocument doc:docVal.getResult()) {
//							regList.add((String) doc.get("regNumber"));
//						}
//						docVal.getGroupValue();
//					}
//					ret.put(groupValue.getName().substring(7),regList);
//				}
//			}
//		} catch (Exception e) {
//			throw e;
//		} finally{
//			client.close();
//
//		}
//		return ret;
//
//	}
//	public List<String> selectXuZhanRegByAll(SolrInfo solrInfo) throws Exception{
//		SolrClient client = createNewSolrClient(solrInfo);
//		 List<String> ret = new ArrayList<String>();
//		try {
//			Map<String, String> queryMap = new HashMap<String, String>();
//			queryMap.put("q", "status:未处理");
//			queryMap.put("fq", "txType:xuzhan");
//			queryMap.put("start", "0");
//			queryMap.put("rows", String.valueOf(Integer.MAX_VALUE));
//			queryMap.put("fl", "regNumber");
//			QueryResponse response = SolrUtil.solrQuery(queryMap,client);
//			SolrDocumentList docs = response.getResults();
//			for(SolrDocument doc:docs) {
//				ret.add((String) doc.get("regNumber"));
//			}
//		} catch (Exception e) {
//			throw e;
//		} finally{
//			client.close();
//
//		}
//		return ret;
//
//	}
//	public ReturnInfo selectXuZhanByGovId(Integer govId,GeneralCondition gcon,SolrInfo solrInfo,Integer lestYear,
//			ReturnInfo returnInfo) throws Exception{
//		SolrClient client = createNewSolrClient(solrInfo);
//		try {
//			Map<String, String> queryMap = new HashMap<String, String>();
//			queryMap.put("q", getQueryStringByselectXuZhan(govId, gcon.getKeyword(),lestYear));
//			queryMap.put("fq", "txType:xuzhan");
//			queryMap.put("start", gcon.getOffset().toString());
//			queryMap.put("rows", gcon.getRows().toString());
//			if(gcon.getOrderCol()!=null) {
//				String sort = gcon.getOrderCol();
//				if(gcon.getOrderAsc()!=null) sort = sort +" "+gcon.getOrderAsc(); else sort = sort +" DESC";
//				queryMap.put("sort", sort);
//			}
//			QueryResponse response = SolrUtil.solrQuery(queryMap,client);
//			returnInfo.setData(getXuZhanlistBySolrResults(response.getResults()));
//			returnInfo.setTotal(response.getResults().getNumFound());
//		} catch (Exception e) {
//			throw e;
//		} finally{
//			client.close();
//
//		}
//		return returnInfo;
//
//	}
//
//	private String getQueryStringByselectTmDongTai(Integer govId, String keyWord, Integer lestYear) {
//		String ret ="govIdTree:"+govId.toString()+" AND NOT status:已处理";
//
//		if(keyWord!=null) {
//			ret = ret+" AND (tmName:*"+keyWord+"*";
//			ret = ret+" OR appName:*"+keyWord+"*";
//			ret = ret+" OR regNumber:*"+keyWord+"*";
//			ret = ret+" OR rTitle:*"+keyWord+"*";
//			ret = ret+" OR agent:*"+keyWord+"*)";
//		}
//		if(lestYear!=null) {
//			ret = ret+"rDate:[NOW/YEAR-"+lestYear.toString()+"YEAR TO *]";
//		}
//
//		return ret;
//	}
//
//
//	private String getQueryStringByselectXuZhan(Integer govId, String keyWord, Integer lestYear) {
//		String ret ="govIdTree:"+govId.toString()+" AND NOT status:已处理";
//
//		if(keyWord!=null) {
//			ret = ret+" AND (tmName:*"+keyWord+"*";
//			ret = ret+" OR appName:*"+keyWord+"*";
//			ret = ret+" OR regNumber:*"+keyWord+"*)";
//		}
//		if(lestYear!=null) {
//			ret = ret+"validEndDate:[NOW/YEAR-"+lestYear.toString()+"YEAR TO *]";
//		}
//
//		return ret;
//	}
//
//	private String getQueryStringByselectSongDa(Integer govId, String ggType, String keyWord,Integer lestYear) {
//		String ret ="govIdTree:"+govId.toString()+" AND NOT status:已处理";
//		if(ggType!=null) {
//			ret=ret+" AND ggType:"+ggType;
//		}else {
//			ret=ret+" AND NOT ggType:商标初步审定公告";
//		}
//		if(keyWord!=null) {
//			ret = ret+" AND (tmName:*"+keyWord+"*";
//			ret = ret+" OR appName:*"+keyWord+"*";
//			ret = ret+" OR regNumber:*"+keyWord+"*";
//			ret = ret+" OR ggName:*"+keyWord+"*)";
//		}
//		if(lestYear!=null) {
//			ret = ret+"ggDate:[NOW/YEAR-"+lestYear.toString()+"YEAR TO *]";
//		}
//
//		return ret;
//	}
//
//
//
//
//	//	public Long selectSongDaCountByGovId(Integer govId, String ggType,GeneralCondition gcon);
//
//	//	public int updateGgtypeByPrimaryKey(Integer id, String ggType);
//
//	//	public List<Songdagonggao> selectSongDaByGovIdLast2Years(Integer govId, String ggType,GeneralCondition gcon){
//	//		return null;
//	//		
//	//	}
//
//	//	public Long selectSongDaCountByGovIdLast2Years(Integer govId, String ggType,GeneralCondition gcon);
//
//	private List<Songdagonggao> getSongdagonggaolistBySolrResults(SolrDocumentList docs) {
//		List<Songdagonggao> songdagonggaolist = new ArrayList<Songdagonggao> ();
//		List<String> ignore = new ArrayList<String>();
//		//ignore.add("processStatus");
//		for(SolrDocument doc : docs) {
//			Songdagonggao songdagonggao = SolrUtil.SolrDocumentToClass(Songdagonggao.class,doc,ignore);
//			//trademark.setTmImage("/TmImage/"+trademark.getApplicantName().toString()+"/"+trademark.getRegNumber().toString()+".jpg");			
//			songdagonggaolist.add(songdagonggao);
//		}
//		return songdagonggaolist;
//	}
//	private List<Tmdongtai> getTmDongTailistBySolrResults(SolrDocumentList docs) {
//		List<Tmdongtai> tmdongtailist = new ArrayList<Tmdongtai> ();
//		List<String> ignore = new ArrayList<String>();
//		//ignore.add("processStatus");
//		for(SolrDocument doc : docs) {
//			Tmdongtai tmdongtai = SolrUtil.SolrDocumentToClass(Tmdongtai.class,doc,ignore);
//			//trademark.setTmImage("/TmImage/"+trademark.getApplicantName().toString()+"/"+trademark.getRegNumber().toString()+".jpg");			
//			tmdongtailist.add(tmdongtai);
//		}
//		return tmdongtailist;
//	}
//	private List<Xuzhan> getXuZhanlistBySolrResults(SolrDocumentList docs) {
//		List<Xuzhan> xuzhanlist = new ArrayList<Xuzhan> ();
//		List<String> ignore = new ArrayList<String>();
//		//ignore.add("processStatus");
//		for(SolrDocument doc : docs) {
//			Xuzhan xuzhan = SolrUtil.SolrDocumentToClass(Xuzhan.class,doc,ignore);
//			//trademark.setTmImage("/TmImage/"+trademark.getApplicantName().toString()+"/"+trademark.getRegNumber().toString()+".jpg");			
//			xuzhanlist.add(xuzhan);
//		}
//		return xuzhanlist;
//	}
//
//
//
//}
