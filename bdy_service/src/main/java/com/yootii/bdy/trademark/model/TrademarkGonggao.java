package com.yootii.bdy.trademark.model;

import java.util.Date;

public class TrademarkGonggao {
    private Integer id;

    private Integer ggTypeId;

    private Integer ggQihao;

    private Date ggDate;

    private String regNumber;

    private String tmName;

    private String tmType;

    private Date appDate;

    private String agent;

    private String oldAppCnName;

    private String oldAppEnName;

    private String oldAppCnAddress;

    private String oldAppEnAddress;

    private String appCnName;

    private String appEnName;

    private String appCnAddress;

    private String appEnAddress;

    private String joinAppName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGgTypeId() {
        return ggTypeId;
    }

    public void setGgTypeId(Integer ggTypeId) {
        this.ggTypeId = ggTypeId;
    }

    public Integer getGgQihao() {
        return ggQihao;
    }

    public void setGgQihao(Integer ggQihao) {
        this.ggQihao = ggQihao;
    }

    public Date getGgDate() {
        return ggDate;
    }

    public void setGgDate(Date ggDate) {
        this.ggDate = ggDate;
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

    public Date getAppDate() {
        return appDate;
    }

    public void setAppDate(Date appDate) {
        this.appDate = appDate;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent == null ? null : agent.trim();
    }

    public String getOldAppCnName() {
        return oldAppCnName;
    }

    public void setOldAppCnName(String oldAppCnName) {
        this.oldAppCnName = oldAppCnName == null ? null : oldAppCnName.trim();
    }

    public String getOldAppEnName() {
        return oldAppEnName;
    }

    public void setOldAppEnName(String oldAppEnName) {
        this.oldAppEnName = oldAppEnName == null ? null : oldAppEnName.trim();
    }

    public String getOldAppCnAddress() {
        return oldAppCnAddress;
    }

    public void setOldAppCnAddress(String oldAppCnAddress) {
        this.oldAppCnAddress = oldAppCnAddress == null ? null : oldAppCnAddress.trim();
    }

    public String getOldAppEnAddress() {
        return oldAppEnAddress;
    }

    public void setOldAppEnAddress(String oldAppEnAddress) {
        this.oldAppEnAddress = oldAppEnAddress == null ? null : oldAppEnAddress.trim();
    }

    public String getAppCnName() {
        return appCnName;
    }

    public void setAppCnName(String appCnName) {
        this.appCnName = appCnName == null ? null : appCnName.trim();
    }

    public String getAppEnName() {
        return appEnName;
    }

    public void setAppEnName(String appEnName) {
        this.appEnName = appEnName == null ? null : appEnName.trim();
    }

    public String getAppCnAddress() {
        return appCnAddress;
    }

    public void setAppCnAddress(String appCnAddress) {
        this.appCnAddress = appCnAddress == null ? null : appCnAddress.trim();
    }

    public String getAppEnAddress() {
        return appEnAddress;
    }

    public void setAppEnAddress(String appEnAddress) {
        this.appEnAddress = appEnAddress == null ? null : appEnAddress.trim();
    }

    public String getJoinAppName() {
        return joinAppName;
    }

    public void setJoinAppName(String joinAppName) {
        this.joinAppName = joinAppName == null ? null : joinAppName.trim();
    }
}