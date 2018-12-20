package com.yootii.bdy.bill.model;

public class PayAcount {
    private Integer payAcountId;

    private String payAcountName;

    private String bankAcountName;

    private String bankAcountAddress;

    private String bankName;

    private String bankAcount;

    private String bankNo;

    private String swiftCode;

    public Integer getPayAcountId() {
        return payAcountId;
    }

    public void setPayAcountId(Integer payAcountId) {
        this.payAcountId = payAcountId;
    }

    public String getPayAcountName() {
        return payAcountName;
    }

    public void setPayAcountName(String payAcountName) {
        this.payAcountName = payAcountName == null ? null : payAcountName.trim();
    }

    public String getBankAcountName() {
        return bankAcountName;
    }

    public void setBankAcountName(String bankAcountName) {
        this.bankAcountName = bankAcountName == null ? null : bankAcountName.trim();
    }

    public String getBankAcountAddress() {
        return bankAcountAddress;
    }

    public void setBankAcountAddress(String bankAcountAddress) {
        this.bankAcountAddress = bankAcountAddress == null ? null : bankAcountAddress.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getBankAcount() {
        return bankAcount;
    }

    public void setBankAcount(String bankAcount) {
        this.bankAcount = bankAcount == null ? null : bankAcount.trim();
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo == null ? null : bankNo.trim();
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode == null ? null : swiftCode.trim();
    }
}