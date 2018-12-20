package com.yootii.bdy.tmcase.model;

import javax.xml.bind.annotation.XmlRootElement;

public class Good {
	private String goodClass;//类别
	private String similarGroup;//类似群
	private String goodCode;//编码
	private String goodName;//商品名称
	private String goodKey;//key
	public String getGoodClass() {
		return goodClass;
	}
	public void setGoodClass(String goodClass) {
		this.goodClass = goodClass;
	}
	public String getSimilarGroup() {
		return similarGroup;
	}
	public void setSimilarGroup(String similarGroup) {
		this.similarGroup = similarGroup;
	}
	public String getGoodCode() {
		return goodCode;
	}
	public void setGoodCode(String goodCode) {
		this.goodCode = goodCode;
	}
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	public String getGoodKey() {
		return goodKey;
	}
	public void setGoodKey(String goodKey) {
		this.goodKey = goodKey;
	}
	
	
}
