package com.yootii.bdy.task.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yootii.bdy.trademark.model.Trademark;

/**
 * 商标续展通知
 * @author 
 *
 */
public class Notification {
 
	private Integer id;   //通知id
	private Integer tmId;    //商标id
	private String tmName; //商标名称
	private int status;   //通知状态         0未处理   1已处理
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date tmDeadTime;   //商标失效时间
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date createTime; //通知创建时间
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date modifyTime; //通知修改时间
	private String createUser;   //通知创建人
	private String modifyUser;   //通知修改人
	private String batchNo;      // 批次
	
	private String tmType;      //商标类型
	private String imgFilePath;    //访问本地商标图片的url
	private int appId;       //申请人id
	private String applicantName;//申请人名称
	private String applicantEnName; //申请人英文名称
	private String email;		//客户邮件
	private String name;     //客户名称
	private String contactName; //客户联系人名称
	private Date appDate;
	
	private String regNumber;
	
	public List<Trademark> trademarks;
	
	
	
	
	
	public String getRegNumber() {
		return regNumber;
	}
	public void setRegNumber(String regNumber) {
		this.regNumber = regNumber;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public List<Trademark> getTrademarks() {
		return trademarks;
	}
	public void setTrademarks(List<Trademark> trademarks) {
		this.trademarks = trademarks;
	}
	public Date getAppDate() {
		return appDate;
	}
	public void setAppDate(Date appDate) {
		this.appDate = appDate;
	}
	public String getTmType() {
		return tmType;
	}
	public void setTmType(String tmType) {
		this.tmType = tmType;
	}
	public String getImgFilePath() {
		return imgFilePath;
	}
	public void setImgFilePath(String imgFilePath) {
		this.imgFilePath = imgFilePath;
	}
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public String getApplicantName() {
		return applicantName;
	}
	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}
	public String getApplicantEnName() {
		return applicantEnName;
	}
	public void setApplicantEnName(String applicantEnName) {
		this.applicantEnName = applicantEnName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public void setTmId(Integer tmId) {
		this.tmId = tmId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getTmId() {
		return tmId;
	}
	public void setTmId(int tmId) {
		this.tmId = tmId;
	}
	public String getTmName() {
		return tmName;
	}
	public void setTmName(String tmName) {
		this.tmName = tmName;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getTmDeadTime() {
		return tmDeadTime;
	}
	public void setTmDeadTime(Date tmDeadTime) {
		this.tmDeadTime = tmDeadTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getModifyUser() {
		return modifyUser;
	}
	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
	
	
	
}
