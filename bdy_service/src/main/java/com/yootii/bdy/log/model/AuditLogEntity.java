package com.yootii.bdy.log.model;

import java.util.Date;
import java.util.Map;

/**
 * 日志审计
 * 
 *
 */

public class AuditLogEntity {
  
  
    private String data;//日志数据

    private String logId;//日志id
    
    private int operateUserId;//操作者(身份用户)id

    private String operateUserName;//操作者(身份用户)
    
    private int operateCustId;//操作者(身份客户)id

    private String operateCustName;//操作者(身份客户)

    private String tableName;//操作表名

    private String operateDate;//操作时间
    
    private String operateURL;//操作URL
    
    private String operate;//操作
    
    private String operateType;//操作类型
    
    private int agencyId;//操作
    
    private String agencyName;//操作类型
    
    private int primaryKey;//操作者(身份用户)id

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	

	public int getOperateUserId() {
		return operateUserId;
	}

	public void setOperateUserId(int operateUserId) {
		this.operateUserId = operateUserId;
	}

	public int getOperateCustId() {
		return operateCustId;
	}

	public void setOperateCustId(int operateCustId) {
		this.operateCustId = operateCustId;
	}

	public String getOperateUserName() {
		return operateUserName;
	}

	public void setOperateUserName(String operateUserName) {
		this.operateUserName = operateUserName;
	}

	

	public String getOperateCustName() {
		return operateCustName;
	}

	public void setOperateCustName(String operateCustName) {
		this.operateCustName = operateCustName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(String operateDate) {
		this.operateDate = operateDate;
	}

	public String getOperateURL() {
		return operateURL;
	}

	public void setOperateURL(String operateURL) {
		this.operateURL = operateURL;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	
	
	
	public int getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(int agencyId) {
		this.agencyId = agencyId;
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public int getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Override
	public String toString() {
		return "AuditLogEntity [data=" + data + ", logId=" + logId + ", operateUserId=" + operateUserId
				+ ", operateUserName=" + operateUserName + ", operateCustId=" + operateCustId + ", operateCustName="
				+ operateCustName + ", tableName=" + tableName + ", operateDate=" + operateDate + ", operateURL="
				+ operateURL + ", operate=" + operate + ", operateType=" + operateType + ", agencyId=" + agencyId
				+ ", agencyName=" + agencyName + ", primaryKey=" + primaryKey + "]";
	}

	

    
    
}