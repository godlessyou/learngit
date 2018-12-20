package com.yootii.bdy.security.model;

import java.io.Serializable;

public class Token {


	private String tokenID;
	
	private Integer userID;
	
	private Integer customerID;
		
	private String username;
	
	private String fullname;
	
	private String createTime;//创建时间
	
	private String checkTime;//最近验证时间
	
	private Integer loginAsId;	
	
	private boolean isUser;//true 表示user,false表示 customer
	
	private boolean isLoginAs;
	
	private String loginAsName;
	
	//用户页面文字所用的语言
	private String language;
	
	

	public String getTokenID() {
		return tokenID;
	}

	public void setTokenID(String tokenID) {
		this.tokenID = tokenID;
	}

	public Integer getUserID() {
		return userID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	public Integer getLoginAsId() {
		return loginAsId;
	}

	public void setLoginAsId(Integer loginAsId) {
		this.loginAsId = loginAsId;
	}

	public String getLoginAsName() {
		return loginAsName;
	}

	public void setLoginAsName(String loginAsName) {
		this.loginAsName = loginAsName;
	}

	public boolean isLoginAs() {
		return isLoginAs;
	}

	public void setLoginAs(boolean isLoginAs) {
		this.isLoginAs = isLoginAs;
	}


	public Integer getCustomerID() {
		return customerID;
	}

	public void setCustomerID(Integer customerID) {
		this.customerID = customerID;
	}

	public boolean isUser() {
		return isUser;
	}

	public void setUser(boolean isUser) {
		this.isUser = isUser;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	
	
	
}
