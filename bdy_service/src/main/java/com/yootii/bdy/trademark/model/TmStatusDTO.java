package com.yootii.bdy.trademark.model;

import java.util.Map;

public class TmStatusDTO {

	public long count ;
	public String name;
	public Map<String, Object> detailType;
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, Object> getDetailType() {
		return detailType;
	}
	public void setDetailType(Map<String, Object> detailType) {
		this.detailType = detailType;
	}
	
	
	
}
