package com.yootii.bdy.tmcase.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class GoodsPlan {
    private Integer planId;

    private String planName;

    private String appName;
    
    private String goodClasses;//所有类别
    
    @JsonIgnore
    private List<Goods> goods;

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName == null ? null : planName.trim();
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName == null ? null : appName.trim();
    }

	
	public List<Goods> getGoods() {
		return goods;
	}

	public void setGoods(List<Goods> goods) {
		this.goods = goods;
	}

	public String getGoodClasses() {
		return goodClasses;
	}

	public void setGoodClasses(String goodClasses) {
		this.goodClasses = goodClasses;
	}
}