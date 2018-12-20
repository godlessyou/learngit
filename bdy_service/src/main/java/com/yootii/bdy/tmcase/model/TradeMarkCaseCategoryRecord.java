package com.yootii.bdy.tmcase.model;

import java.util.Date;

public class TradeMarkCaseCategoryRecord extends TradeMarkCaseCategory{
    private Integer recordId;

    private Date modifyTime;

    private String modifyUserName;

    private Integer modifyFlag;

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }
  

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyUserName() {
        return modifyUserName;
    }

    public void setModifyUserName(String modifyUserName) {
        this.modifyUserName = modifyUserName == null ? null : modifyUserName.trim();
    }

    public Integer getModifyFlag() {
        return modifyFlag;
    }

    public void setModifyFlag(Integer modifyFlag) {
        this.modifyFlag = modifyFlag;
    }
}