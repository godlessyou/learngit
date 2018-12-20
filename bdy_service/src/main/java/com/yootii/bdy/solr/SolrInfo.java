package com.yootii.bdy.solr;


public class SolrInfo  {
	private String solrHome = "http://192.168.0.8:8983/solr/";
	private Integer solrConnectTimeOut = 30000;
	private Integer solrDefaultMaxConnect = 100;
	private Integer solrMaxTotalConnect = 100;
	private Integer solrSoTimeOut = 30000;
	private Boolean isGreat = false;
    
    

    
	public String getSolrHome() {
		return solrHome;
	}
	public void setSolrHome(String solrHome) {
		this.solrHome = solrHome;
	}

	public Integer getSolrConnectTimeOut() {
		return solrConnectTimeOut;
	}

	public void setSolrConnectTimeOut(Integer solrConnectTimeOut) {
		this.solrConnectTimeOut = solrConnectTimeOut;
	}

	public Integer getSolrDefaultMaxConnect() {
		return solrDefaultMaxConnect;
	}

	public void setSolrDefaultMaxConnect(Integer solrDefaultMaxConnect) {
		this.solrDefaultMaxConnect = solrDefaultMaxConnect;
	}

	public Integer getSolrMaxTotalConnect() {
		return solrMaxTotalConnect;
	}

	public void setSolrMaxTotalConnect(Integer solrMaxTotalConnect) {
		this.solrMaxTotalConnect = solrMaxTotalConnect;
	}

	public Integer getSolrSoTimeOut() {
		return solrSoTimeOut;
	}

	public void setSolrSoTimeOut(Integer solrSoTimeOut) {
		this.solrSoTimeOut = solrSoTimeOut;
	}
	public Boolean getIsGreat() {
		return isGreat;
	}
	public void setIsGreat(Boolean isGreat) {
		this.isGreat = isGreat;
	}
    
}