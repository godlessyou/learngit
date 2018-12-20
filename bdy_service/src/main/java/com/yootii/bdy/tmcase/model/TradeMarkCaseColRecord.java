package com.yootii.bdy.tmcase.model;

public class TradeMarkCaseColRecord {
    private Integer id;

    private Integer recordId;

    private String modifiedCol;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public String getModifiedCol() {
        return modifiedCol;
    }

    public void setModifiedCol(String modifiedCol) {
        this.modifiedCol = modifiedCol == null ? null : modifiedCol.trim();
    }
}