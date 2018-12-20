package com.yootii.bdy.task.model;

import java.util.List;

import com.yootii.bdy.trademark.model.Trademark;

public class MailDTO {

	private String toAddress;      //客户邮箱地址
	private String subject;          //邮件主题
	private String customerName;        //客户名称
	private String toPerson;          //客户联系人名称
	private Integer custId;
	List<Trademark> contents;
	
	
	public Integer getCustId() {
		return custId;
	}
	public void setCustId(Integer custId) {
		this.custId = custId;
	}
	public String getToAddress() {
		return toAddress;
	}
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getToPerson() {
		return toPerson;
	}
	public void setToPerson(String toPerson) {
		this.toPerson = toPerson;
	}
	public List<Trademark> getContents() {
		return contents;
	}
	public void setContents(List<Trademark> contents) {
		this.contents = contents;
	}
	
	
}
