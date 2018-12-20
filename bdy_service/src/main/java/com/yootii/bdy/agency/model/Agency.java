package com.yootii.bdy.agency.model;

public class Agency {
	private Integer id;

    private String name;

    private String address;

    private String tel;

    private String logo;

    private String status;
    
    private Integer appOnline;  //是否支持网申
   
	private Integer appChannel; //采用哪个代理所的网申通道

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	public Integer getAppOnline() {
			return appOnline;
	}

	public void setAppOnline(Integer appOnline) {
		this.appOnline = appOnline;
	}

	public Integer getAppChannel() {
		return appChannel;
	}

	public void setAppChannel(Integer appChannel) {
		this.appChannel = appChannel;
	}

	
    
    
}
