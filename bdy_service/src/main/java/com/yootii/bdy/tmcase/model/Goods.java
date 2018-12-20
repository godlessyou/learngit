package com.yootii.bdy.tmcase.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Goods {
    private Integer id;

    @JsonIgnore
    private Integer planId;

    private String goodClass;

    private String similarGroup;

    private String goodCode;

    private String goodName;

    private String goodNameEn;

    private String goodKey;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public String getGoodClass() {
        return goodClass;
    }

    public void setGoodClass(String goodClass) {
        this.goodClass = goodClass == null ? null : goodClass.trim();
    }

    public String getSimilarGroup() {
        return similarGroup;
    }

    public void setSimilarGroup(String similarGroup) {
        this.similarGroup = similarGroup == null ? null : similarGroup.trim();
    }

    public String getGoodCode() {
        return goodCode;
    }

    public void setGoodCode(String goodCode) {
        this.goodCode = goodCode == null ? null : goodCode.trim();
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName == null ? null : goodName.trim();
    }

    public String getGoodNameEn() {
        return goodNameEn;
    }

    public void setGoodNameEn(String goodNameEn) {
        this.goodNameEn = goodNameEn == null ? null : goodNameEn.trim();
    }

    public String getGoodKey() {
        return goodKey;
    }

    public void setGoodKey(String goodKey) {
        this.goodKey = goodKey == null ? null : goodKey.trim();
    }
}