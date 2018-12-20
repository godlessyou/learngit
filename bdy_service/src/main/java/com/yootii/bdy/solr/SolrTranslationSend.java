package com.yootii.bdy.solr;

import java.util.HashMap;

import java.util.Map;



import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;

import com.yootii.bdy.util.SolrUtil;

public class SolrTranslationSend  {


    private SolrClient createNewSolrClient(SolrInfo solrInfo) {
        try {
            System.out.println("server address:" + solrInfo.getSolrHome()+"bdytranslation");
            HttpSolrClient client = new HttpSolrClient(solrInfo.getSolrHome()+"bdytranslation");
            client.setConnectionTimeout(solrInfo.getSolrConnectTimeOut());
            client.setDefaultMaxConnectionsPerHost(solrInfo.getSolrDefaultMaxConnect());
            client.setMaxTotalConnections(solrInfo.getSolrMaxTotalConnect());
            client.setSoTimeout(solrInfo.getSolrConnectTimeOut());
            return client;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
	
	public String translation(	SolrInfo solrInfo,String value,String bflanguage,String aflanguage) throws Exception {
		SolrClient client = createNewSolrClient(solrInfo);
		String ret = "";
    	try {
    		
    		
	    	Map<String, String> queryMap = new HashMap<String, String>();
	    	queryMap.put("q",languageToQuery(bflanguage)+":"+value);
	    	queryMap.put("start", "0");
	    	queryMap.put("rows", "1");
    		QueryResponse response = SolrUtil.solrQuery(queryMap,client);
    		if(response.getResults().getNumFound()==(long)0){
    			ret = value;    			
    		} else {
    			ret = response.getResults().get(0).get(languageToQuery(aflanguage)).toString();
    		}
		} catch (Exception e) {
			throw e;
		} finally{
			client.close();
			
		}
	    	return ret;
	}
	
	
	String languageToQuery(String language) throws Exception {
		String ret = "";
		switch(language) {
		case "cn":
			ret= "cnname";
			break;
		case "en":
			ret= "enname";
			break;
		default:
			throw new Exception("language erro");
			
		}
		
		return ret;
		
	}
  

}
