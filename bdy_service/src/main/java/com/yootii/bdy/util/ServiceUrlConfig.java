package com.yootii.bdy.util;

import org.springframework.stereotype.Component;

import com.yootii.bdy.common.Constants;



@Component
public class ServiceUrlConfig {

	// 用户管理等服务接口
	private String bdysysmUrl="";
		
	// 流程服务接口
	private String processEngineUrl="";	
	
	// 邮件服务接口
	private String mailServiceUrl="";	
	
	// 主页的url接口
	private String homeUrl="";	
	
	// 案件相关文件的存储目录
	private String fileUrl="";
	
	
	// 获取官网商标数据接口
	private String bdydataUrl="";
	
	// BDY数据同步到wpm接口
	private String bdydatasynUrl="";
	
	// BDY数据同步到wpm接口
	private boolean bdydatasyn=false;
		
	

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		fileUrl=fileUrl.trim();
		this.fileUrl = fileUrl;
		
		//设置文件存储所用的目录
		boolean initFlag=Constants.isInitFlag();
		if (!initFlag){
			Constants.init(fileUrl);
		}
	}

	public String getHomeUrl() {
		return homeUrl;
	}

	public void setHomeUrl(String homeUrl) {
		homeUrl=homeUrl.trim();
		this.homeUrl = homeUrl;
	}

	public String getBdysysmUrl() {
		return bdysysmUrl;
	}

	public void setBdysysmUrl(String bdysysmUrl) {
		bdysysmUrl=bdysysmUrl.trim();
		this.bdysysmUrl = bdysysmUrl;
	}

	public String getProcessEngineUrl() {
		return processEngineUrl;
	}

	public void setProcessEngineUrl(String processEngineUrl) {
		processEngineUrl=processEngineUrl.trim();
		this.processEngineUrl = processEngineUrl;
	}

	public String getMailServiceUrl() {
		return mailServiceUrl;
	}

	public void setMailServiceUrl(String mailServiceUrl) {
		mailServiceUrl=mailServiceUrl.trim();
		this.mailServiceUrl = mailServiceUrl;
	}

	public String getBdydataUrl() {
		return bdydataUrl;
	}

	public void setBdydataUrl(String bdydataUrl) {
		this.bdydataUrl = bdydataUrl;
	}

	public String getBdydatasynUrl() {
		return bdydatasynUrl;
	}

	public void setBdydatasynUrl(String bdydatasynUrl) {
		this.bdydatasynUrl = bdydatasynUrl;
	}

	public boolean isBdydatasyn() {
		return bdydatasyn;
	}

	public void setBdydatasyn(boolean bdydatasyn) {
		this.bdydatasyn = bdydatasyn;
		
		Constants.initDataSyn(bdydatasyn);
	}
}
