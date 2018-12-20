package com.yootii.bdy.remind.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TrademarkGonggao {
    private String id;

    private String regNumber;

    private String tmName;

    private String tmType;

    private String ggName;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date ggDate;

    private String ggNo;

    private String appName;

    private String status;

    private String agent;

    private Integer ggQihao;

    private Integer ggPage;

    private String ggType;

    private Integer tixing;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date ggFaWenDate;

    private String ggFileUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber == null ? null : regNumber.trim();
    }

    public String getTmName() {
        return tmName;
    }

    public void setTmName(String tmName) {
        this.tmName = tmName == null ? null : tmName.trim();
    }

    public String getTmType() {
        return tmType;
    }

    public void setTmType(String tmType) {
        this.tmType = tmType == null ? null : tmType.trim();
    }

    public String getGgName() {
        return ggName;
    }

    public void setGgName(String ggName) {
        this.ggName = ggName == null ? null : ggName.trim();
    }

    public Date getGgDate() {
        return ggDate;
    }

    public void setGgDate(Date ggDate) {
        this.ggDate = ggDate;
    }

    public String getGgNo() {
        return ggNo;
    }

    public void setGgNo(String ggNo) {
        this.ggNo = ggNo == null ? null : ggNo.trim();
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName == null ? null : appName.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent == null ? null : agent.trim();
    }

    public Integer getGgQihao() {
        return ggQihao;
    }

    public void setGgQihao(Integer ggQihao) {
        this.ggQihao = ggQihao;
    }

    public Integer getGgPage() {
        return ggPage;
    }

    public void setGgPage(Integer ggPage) {
        this.ggPage = ggPage;
    }

    public String getGgType() {
        return ggType;
    }

    public void setGgType(String ggType) {
        this.ggType = ggType == null ? null : ggType.trim();
    }

    public Integer getTixing() {
        return tixing;
    }

    public void setTixing(Integer tixing) {
        this.tixing = tixing;
    }

    public Date getGgFaWenDate() {
        return ggFaWenDate;
    }

    public void setGgFaWenDate(Date ggFaWenDate) {
        this.ggFaWenDate = ggFaWenDate;
    }

    public String getGgFileUrl() {
        return ggFileUrl;
    }

    public void setGgFileUrl(String ggFileUrl) {
        this.ggFileUrl = ggFileUrl == null ? null : ggFileUrl.trim();
    }
}