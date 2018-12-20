package com.yootii.bdy.solr;

import java.util.List;
import java.util.Map;

public class SolrData {
	
	private List<Map<String,Object>> dataTable;
	private List<String> columnName;
	private String idName;
	
	
	public List<Map<String, Object>> getDataTable() {
		return dataTable;
	}
	public void setDataTable(List<Map<String, Object>> dataTable) {
		this.dataTable = dataTable;
	}
	public List<String> getColumnName() {
		return columnName;
	}
	public void setColumnName(List<String> columnName) {
		this.columnName = columnName;
	}
	public String getIdName() {
		return idName;
	}
	public void setIdName(String idName) {
		this.idName = idName;
	}
}