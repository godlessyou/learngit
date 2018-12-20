package com.yootii.bdy.solr;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FieldStatsInfo;
import org.apache.solr.client.solrj.response.PivotField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.RangeFacet.Count;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.NamedList;
import org.springframework.stereotype.Service;

import com.yootii.bdy.bill.model.Bill;
import com.yootii.bdy.bill.model.BillSolr;
import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.common.TrademarkProcessStatus;
import com.yootii.bdy.trademark.model.Trademark;
import com.yootii.bdy.util.SolrUtil;

public class SolrSendBill  {
	private final Logger logger = Logger.getLogger(this.getClass());

    private SolrClient createNewSolrClient(SolrInfo solrInfo) {
        try {
            System.out.println("server address:" + solrInfo.getSolrHome()+"bdybill");
            HttpSolrClient client = new HttpSolrClient(solrInfo.getSolrHome()+"bdybill");
            client.setConnectionTimeout(solrInfo.getSolrConnectTimeOut());
            client.setDefaultMaxConnectionsPerHost(solrInfo.getSolrDefaultMaxConnect());
            client.setMaxTotalConnections(solrInfo.getSolrMaxTotalConnect());
            client.setSoTimeout(solrInfo.getSolrConnectTimeOut());
            return client;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public ReturnInfo selectBill(GeneralCondition gcon, Integer departId,Integer custId,Integer agencyId,Integer coagencyId,
    		Integer receiverType, BillSolr bill,String startYear,String endYear, String queYear,
			SolrInfo solrInfo,
			ReturnInfo returnInfo) throws Exception {
		SolrClient client = createNewSolrClient(solrInfo);
    	try {
    		Map<String, String> queryMap = new HashMap<String, String>();
	    	queryMap.put("q", getQueryStringBySelectBill(gcon,departId,custId, agencyId, coagencyId,
	        		 receiverType,  bill, startYear,  endYear,queYear));
	    	queryMap.put("start", String.valueOf(gcon.getOffset()));
	    	queryMap.put("rows", String.valueOf(gcon.getrows()));
	    	if(gcon.getOrderCol()!=null) {
	    		String sort = gcon.getOrderCol();
	    		if(gcon.getOrderAsc()!=null) sort = sort +" "+gcon.getOrderAsc(); else sort = sort +" DESC";
	    		queryMap.put("sort", sort);
	    	}
    		QueryResponse response = SolrUtil.solrQuery(queryMap,client);
    		returnInfo.setData(getBillBySolrResults(response.getResults()));
    		returnInfo.setTotal(response.getResults().getNumFound());
		} catch (Exception e) {
			throw e;
		} finally{
			client.close();
			
		}
    	return returnInfo;
	    	
	}
	
	 private String getQueryStringBySelectBill(GeneralCondition gcon, Integer departId, Integer custId, Integer agencyId,
			Integer coagencyId, Integer receiverType, BillSolr bill, String startYear, String endYear, String queYear) {
		String queryList = "";
		if(custId != null) {
    		queryList = "custId:"+custId.toString()+" AND receiverType:1";
    	}
    	if(agencyId != null) {
    		queryList = "agencyId:"+agencyId.toString()+" AND receiverType:"+receiverType.toString()+" AND departIdTree:"+departId.toString();
    	}    	
    	if(coagencyId != null) {
    		queryList = "coagencyId:"+coagencyId.toString()+" AND receiverType:2";
    	}

    	String vstartYear ="*";
    	String vendYear ="*";
    	if(startYear!=null)vstartYear=startYear;
    	if(endYear!=null)vendYear=endYear;
    	queryList = queryList+" AND " +queYear+":["+vstartYear+" TO "+vendYear+"]";
		List<String> ignore = new ArrayList<String>();
		ignore.add("custId");
		ignore.add("agencyId");
		ignore.add("chargeRecords");
		ignore.add("contactUser");
    	queryList = queryList+SolrUtil.GetSolrQueryByClass(bill, ignore);
		return queryList;
	}

	private List<Bill> getBillBySolrResults(SolrDocumentList docs) {
		List<Bill> billlist = new ArrayList<Bill> ();
		List<String> ignore = new ArrayList<String>();
		for(SolrDocument doc : docs) {
			Bill bill = SolrUtil.SolrDocumentToClass(Bill.class,doc,ignore);
			//trademark.setTmImage("/TmImage/"+trademark.getApplicantName().toString()+"/"+trademark.getRegNumber().toString()+".jpg");			
			billlist.add(bill);
		}
		return billlist;
	}
	
	 public void createDocs(SolrInfo solrInfo,SolrData billDataList,List<SolrData> otherDataList) throws Exception {
        System.out.println("======================add doc ===================");
        Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
    	SolrClient client = createNewSolrClient(solrInfo);
        try {
        	docs = getSolrDocByBill(billDataList, otherDataList);
        	
        	//先删除原来的数据
        	client.deleteByQuery("*:*");
        	
        	UpdateResponse rspcommit = client.commit();
        	
        	logger.info("删除原来的数据成功");



            UpdateResponse rsp = client.add(docs);
            System.out
                    .println("Add doc size" + docs.size() + " result:" + rsp.getStatus() + " Qtime:" + rsp.getQTime());

             rspcommit = client.commit();
            System.out.println("commit doc to index" + " result:" + rsp.getStatus() + " Qtime:" + rsp.getQTime());

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
        	client.close();
        }
    }



	private Collection<SolrInputDocument> getSolrDocByBill(SolrData billList,List<SolrData> otherDataList) {
		Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		docs = getSolrDocbyTBilllist(billList);
		for(SolrData otherData : otherDataList) {
			docs = changeSolrDocByOtherData(otherData,docs,"billId");
		}
		return docs;
	}
	
	
    public Collection<SolrInputDocument> getSolrDocbyTBilllist(SolrData billList) {
        Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
        
		List<Map<String, Object>> billMapList = billList.getDataTable();
		for (Map<String, Object> billMap:billMapList) {
			SolrInputDocument doc = new SolrInputDocument();
			for (Map.Entry<String, Object> bill : billMap.entrySet()) {
				//if (trademark.getKey().equals("tmImage") ) continue;
				if(bill.getValue() instanceof BigDecimal) {
					doc.addField(bill.getKey(), ((BigDecimal)bill.getValue()).doubleValue());
				} else {
					doc.addField(bill.getKey(), bill.getValue());
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
    					if (entry.getKey().equals("departName_2") ) {
    	        			doc.addField("departNameTree", entry.getValue());
    	        			continue;
    	        		}
    	        		if (entry.getKey().equals("departName_1") ) {
    	        			doc.addField("departNameTree", entry.getValue());
    	        			continue;
    	        		}
    	        		if (entry.getKey().equals("departName") ) {
    	        			doc.addField("departNameTree", entry.getValue());
    	        			continue;
    	        		}
    	        		if (entry.getKey().equals("departId_2") ) {
    	        			doc.addField("departIdTree", entry.getValue());
    	        			continue;
    	        		}
    	        		if (entry.getKey().equals("departId_1") ) {
    	        			doc.addField("departIdTree", entry.getValue());
    	        			continue;
    	        		}
    	        		if (entry.getKey().equals("departId") ) {
    	        			doc.addField("departIdTree", entry.getValue());
    	        			continue;
    	        		}
    					doc.addField(entry.getKey(), entry.getValue());    					
    				}
    			}
    		}
    		
    	}
    	return docs;
    }    

    public Object SelectBillCount(SolrInfo solrInfo,Integer custId,Integer agencyId,Integer departId,Integer coagencyId,
    		Integer receiverType,Integer startYear,Integer endYear,String queYear,String otherque) throws Exception{
    	List<Map<String, Object>> retlist = new ArrayList<Map<String, Object>>();
    	SolrClient client = createNewSolrClient(solrInfo);
    	Map<String, String> queryMap = new HashMap<String, String>();
    	String queryList = "*:*";
    	
    	if(agencyId != null) {
    		queryList = "agencyId:"+agencyId.toString()+" AND receiverType:"+receiverType.toString()+" AND departIdTree:"+departId.toString();
    		 if(custId != null) {
    	    		queryList = "custId:"+custId.toString()+" AND "+queryList;
    	    }
    	}else if(coagencyId != null) {
    		queryList = "coagencyId:"+coagencyId.toString()+" AND receiverType:2";
   		 	if(custId != null) {
	    		queryList = "custId:"+custId.toString()+" AND "+queryList;
   		 	}
    	}else if(custId != null) {
    		queryList = "custId:"+custId.toString()+" AND receiverType:1";
    	}
    	    	
		String vstartYear ="*";
    	String vendYear ="*";
    	if(startYear!=null)vstartYear=startYear.toString();
    	if(endYear!=null)vendYear=endYear.toString();
    	
    	
    	String query= queYear+"Year:["+vstartYear+" TO "+vendYear+"]";
    	if(otherque !=null)query = query+" AND "+otherque;
    	
//    	queryMap.put("q", queYear+"Year:["+vstartYear+" TO "+vendYear+"]");
    	queryMap.put("q", query);
    	queryMap.put("fq", queryList);
    	queryMap.put("start", "0");
    	queryMap.put("rows", "0");

    	
    	try {
    		
    		return SolrUtil.solrQuery(queryMap,client).getResults().getNumFound();

//    		NamedList<List<PivotField>> res = SolrUtil.solrQuery(queryMap,client).getFacetPivot();
//    		retlist =  returnmap(res.getVal(0),startYear,endYear);
//    		if(res.getName(0).indexOf("Year")>=0) {
//				orderYear(retlist,startYear,endYear);		
//			}
    		//ret.put("data", ret0list);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return 0;
	}
    
    public Map<String, Object> SelectBillByList(SolrInfo solrInfo,Integer departId,Integer custId,Integer agencyId,Integer coagencyId,
    		Integer receiverType,String startYear,String endYear,String field,String queYear,String otherque) throws Exception{

    	Map<String, Object> retMap = new HashMap<String,Object>();
    	SolrClient client = createNewSolrClient(solrInfo);
    	Map<String, String> queryMap = new HashMap<String, String>();
    	String queryList = "*:*";
    	
    	if(agencyId != null) {
    		queryList = "agencyId:"+agencyId.toString()+" AND receiverType:"+receiverType.toString()+" AND departIdTree:"+departId.toString();
    		 if(custId != null) {
    	    		queryList = "custId:"+custId.toString()+" AND "+queryList;
    	    }
    	}else if(coagencyId != null) {
    		queryList = "coagencyId:"+coagencyId.toString()+" AND receiverType:2";
   		 	if(custId != null) {
	    		queryList = "custId:"+custId.toString()+" AND "+queryList;
   		 	}
    	}else if(custId != null) {
    		queryList = "custId:"+custId.toString()+" AND receiverType:1";
    	}
    	if(otherque !=null) {
    		queryList = queryList+" AND "+otherque;
    	}
    	String vstartYear ="*";
    	String vendYear ="*";
    	if(startYear!=null)vstartYear=startYear;
    	if(endYear!=null)vendYear=endYear;
    	queryMap.put("q", queYear+":["+vstartYear+" TO "+vendYear+"]");
    	queryMap.put("fq", queryList);
    	queryMap.put("start", "0");
    	queryMap.put("rows", "0");
    	queryMap.put("stats", "true");
    	queryMap.put("stats.field", "sum");
    	if (field!=null) {
    		queryMap.put("stats.facet", field);
    	}
    	
    	
    	try {
    		FieldStatsInfo sum = SolrUtil.solrQuery(queryMap,client).getFieldStatsInfo().get("sum");
    		List<FieldStatsInfo> statslist = new ArrayList<FieldStatsInfo>();
    		if (field!=null) {
    			statslist = sum.getFacets().get(field);
        	} else {
        		statslist.add(sum);
        	}
    		
			for(FieldStatsInfo stats:statslist)
			{
				
				if (stats.getName().equals(""))continue;
				Map<String,Object> ret = new HashMap<String,Object>();
				
				ret.put("count", stats.getCount());
				ret.put("sum", stats.getSum());
				
				retMap.put(stats.getName().toString(), ret);
    		}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return retMap;
    	
    }
    
    public List<Map<String, Object>> SelectBillByListPovit(SolrInfo solrInfo,Integer departId,Integer custId,Integer agencyId,Integer coagencyId,
    		Integer receiverType,String startYear,String endYear,List<String> fields,String queYear,String otherque) throws Exception{

    	Map<String, Object> retMap = new HashMap<String,Object>();
    	SolrClient client = createNewSolrClient(solrInfo);
    	Map<String, String> queryMap = new HashMap<String, String>();
    	String queryList = "*:*";
    	
    	if(agencyId != null) {
    		queryList = "agencyId:"+agencyId.toString()+" AND receiverType:"+receiverType.toString()+" AND departIdTree:"+departId.toString();
    		 if(custId != null) {
    	    		queryList = "custId:"+custId.toString()+" AND "+queryList;
    	    }
    	}else if(coagencyId != null) {
    		queryList = "coagencyId:"+coagencyId.toString()+" AND receiverType:2";
   		 	if(custId != null) {
	    		queryList = "custId:"+custId.toString()+" AND "+queryList;
   		 	}
    	}else if(custId != null) {
    		queryList = "custId:"+custId.toString()+" AND receiverType:1";
    	}
    	if(otherque !=null) {
    		queryList = queryList+" AND "+otherque;
    	}
    	String vstartYear ="*";
    	String vendYear ="*";
    	if(startYear!=null)vstartYear=startYear;
    	if(endYear!=null)vendYear=endYear;
    	queryMap.put("q", queYear+":["+vstartYear+" TO "+vendYear+"]");
    	queryMap.put("fq", queryList);
    	queryMap.put("start", "0");
    	queryMap.put("rows", "0");
    	queryMap.put("facet", "on");

    	String pivot = "";
    	for(String field:fields) {
    		pivot = pivot+field +",";
    	}
    	queryMap.put("facet.pivot", pivot+"sum");
    	
    	
    	try {
    		List<PivotField> sumlist = SolrUtil.solrQuery(queryMap,client).getFacetPivot().getVal(0);
    		
    		
    		retMap = getpivotfieldlist(sumlist);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return (List<Map<String, Object>>) retMap.get("value");
    	
    }
	private Map<String, Object> getpivotfieldlist(List<PivotField> sumlist) {
		Map<String, Object> ret = new HashMap<String, Object>();
		if(sumlist.size()==0) return ret;
		if(sumlist.get(0).getField().equals("sum")) {
			int count = 0;
			double sum = 0;
			for(PivotField a:sumlist) {
				count = count+a.getCount();
				sum = sum + (a.getCount()*Double.valueOf(a.getValue().toString()).intValue());		
			}
			ret.put("count", count);
			ret.put("sum", sum);
			
			
		} else {
			List<Map<String, Object>> retlist = new ArrayList<Map<String, Object>>();
			for(PivotField a:sumlist) {
				Map<String, Object> ret0 = new HashMap<String, Object>();	

				ret0.put("name", a.getValue());

				if(a.getPivot() == null) {
					ret0.put("count", a.getCount());
				}else {  
					ret0.putAll(getpivotfieldlist(a.getPivot()));	
					
				}		
				retlist.add(ret0);	
			}
			ret.put("value", retlist);
		}
		
		
		
		


		return ret;
	}



	private void orderYear(List<Map<String, Object>> ret0list, Integer startYear, Integer endYear) {

    	Collections.sort(ret0list,new orderByYear());  		
		if(startYear ==null) {
			startYear = Integer.valueOf(Collections.min(ret0list,new orderByYear()).get("Year").toString());
		}
		if(endYear ==null) {
			endYear = Integer.valueOf(Collections.max(ret0list,new orderByYear()).get("Year").toString());
		}
		System.out.println(startYear);
		System.out.println(endYear);
		for(int i = 0; ; ) {
			if(Integer.valueOf(ret0list.get(i).get("Year").toString()).intValue() < startYear+i) {
				System.out.println(ret0list.get(i));
				ret0list.remove(i);
			}else if((Integer.valueOf(ret0list.get(i).get("Year").toString()).intValue() > startYear+i)&(Integer.valueOf(ret0list.get(i).get("Year").toString()) <= endYear)) {

				Map<String,Object> ret0 = new HashMap<String,Object>();
				ret0.put("Year", startYear+i);
				ret0.put("count", 0);
				ret0list.add(ret0);
				Collections.sort(ret0list,new orderByYear());  	
				System.out.println(ret0list.get(i));
			}  else	if(Integer.valueOf(ret0list.get(i).get("Year").toString()).equals(endYear)) {
				if(ret0list.size()==endYear-startYear+1)
				{
					break;

				}else if(Integer.valueOf(ret0list.get(i+1).get("Year").toString()).intValue() > endYear) {
					System.out.println(ret0list.get(i+1));
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
			System.out.println(ret0list.toString());
			System.out.println(Integer.valueOf(ret0list.get(i).get("Year").toString()));
			System.out.println(ret0list.size());
			

		}
	
		
	}
	class orderByYear implements Comparator<Map<String, Object>> {
	public int compare(Map<String, Object> s1,Map<String, Object> s2) {
		return (Integer.valueOf((s1.get("Year").toString())).compareTo(Integer.valueOf((s2.get("Year").toString()))));
	}
}
    

	private String getfacetqueryList(int year, String title, String value) {
		return"{!key=\"year\\:"+String.valueOf(year)+"status\\:"+title+"\"}(trademarkprocess:*\\\"status\\\"\\:"
			+value+",*\\\"statusDate\\\"\\:*"+String.valueOf(year)+"*)";
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
//    	System.out.println(queryMap);
//    	try {
//    		for(Count a:(List<Count>)SolrUtil.solrQuery(queryMap,client).getFacetRanges().get(0).getCounts()) {
//    			StatsReturn ret = new StatsReturn();
//    			ret.setYear(Integer.valueOf(a.getValue().substring(0, 4)));
//    			ret.setAmount(new Long((long)a.getCount()));
//    			System.out.println(a.getValue()+"  "+a.getCount());
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
//    	System.out.println(queryMap);
//    	try {
//    		for(Count a:(List<Count>)SolrUtil.solrQuery(queryMap,client).getFacetRanges().get(0).getCounts()) {
//    			StatsReturn ret = new StatsReturn();
//    			ret.setYear(Integer.valueOf(a.getValue().substring(0, 4)));
//    			ret.setAmount(new Long((long)a.getCount()));
//    			System.out.println(a.getValue()+"  "+a.getCount());
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
//    	System.out.println(queryMap);
//    	try {
//    		for(Count a:(List<Count>)SolrUtil.solrQuery(queryMap,client).getFacetRanges().get(0).getCounts()) {
//    			StatsReturn ret = new StatsReturn();
//    			ret.setYear(Integer.valueOf(a.getValue().substring(0, 4)));
//    			ret.setAmount(new Long((long)a.getCount()));
//    			System.out.println(a.getValue().substring(0, 4)+"  "+a.getCount());
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
//    	System.out.println(queryMap);
//    	try {
//    		for(Count a:(List<Count>)SolrUtil.solrQuery(queryMap,client).getFacetRanges().get(0).getCounts()) {
//    			StatsReturn ret = new StatsReturn();
//    			ret.setYear(Integer.valueOf(a.getValue().substring(0, 4)));
//    			ret.setAmount(new Long((long)a.getCount()));
//    			System.out.println(a.getValue()+"  "+a.getCount());
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
//    	System.out.println(queryMap);
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
//    	System.out.println(queryMap);
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
//		System.out.println(queryMap);
//		System.out.println(queryList);
//		try {
//			QueryResponse res = SolrUtil.solrQuery(queryMap,queryMapList,client);
//			System.out.println(res);
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
