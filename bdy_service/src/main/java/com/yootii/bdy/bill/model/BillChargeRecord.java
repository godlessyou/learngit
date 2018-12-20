package com.yootii.bdy.bill.model;

public class BillChargeRecord {
    private Integer id;

    private Integer billId;

    private Integer chargeRecordId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public Integer getChargeRecordId() {
        return chargeRecordId;
    }

    public void setChargeRecordId(Integer chargeRecordId) {
        this.chargeRecordId = chargeRecordId;
    }
}