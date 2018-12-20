package com.yootii.bdy.common;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class GeneralCondition {
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date inputTime;// 插入时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date modifyTime;// 修改时间
	private String inputName;// 插入人Id
	private String modifyName;// 修改人Id
	@JsonIgnore
	private String tokenID;// 用户身份唯一认证
	private int pageNo;// 当前页
	private int pageSize; //页面大小
	
	private int offset; //数据表查询结果的起始位置
	private int rows; //取查询结果的行数
	
	private String queryPath; //web请求url串, 从根路径径开始,用于标识功能资源
	
	private String keyword; //通用搜索词
	
	private Date startDate;//意见反馈统计的开始时间
	
	private Date endDate;//意见反馈统计的结束时间
	
	private String orderCol;
	
	private String orderAsc;
	
	private Integer startYear;// 

	private Integer endYear;//
	
	private String docDate;
	
	private int flag;        //标志
	
	private Integer type;
	private String createTime;
	private Integer docType;
	private Integer ggQihao;
	
	private Integer createType; //创建案件的类型
	
	
	
	public Integer getCreateType() {
		return createType;
	}
	public void setCreateType(Integer createType) {
		this.createType = createType;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public Integer getGgQihao() {
		return ggQihao;
	}
	public void setGgQihao(Integer ggQihao) {
		this.ggQihao = ggQihao;
	}
	public Integer getDocType() {
		return docType;
	}
	public void setDocType(Integer docType) {
		this.docType = docType;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getDocDate() {
		return docDate;
	}
	public void setDocDate(String docDate) {
		this.docDate = docDate;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword (String keyword) {
		this.keyword = keyword;
	}
	public String getQueryPath() {
		return queryPath;
	}
	public void setQueryPath(String queryPath) {
		this.queryPath=queryPath;
	}
	
	

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getrows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}


	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}


	public Date getInputTime() {
		return inputTime;
	}

	public void setInputTime(Date inputTime) {
		this.inputTime = inputTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getInputName() {
		return inputName;
	}

	public void setInputName(String inputName) {
		this.inputName = inputName;
	}

	public String getModifyName() {
		return modifyName;
	}

	public void setModifyName(String modifyName) {
		this.modifyName = modifyName;
	}


	public String getTokenID() {
		return tokenID;
	}

	public void setTokenID(String tokenID) {
		this.tokenID = tokenID;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public String getOrderCol() {
		return orderCol;
	}
	
	public void setOrderCol(String orderCol) {
		this.orderCol = orderCol;
	}
	public String getOrderAsc() {
		return orderAsc;
	}
	public void setOrderAsc(String orderAsc) {
		this.orderAsc = orderAsc;
	}
	
	
	
	public Integer getStartYear() {
		return startYear;
	}
	public void setStartYear(Integer startYear) {
		this.startYear = startYear;
	}
	public Integer getEndYear() {
		return endYear;
	}
	public void setEndYear(Integer endYear) {
		this.endYear = endYear;
	}
	
	@Override
	public String toString() {
		return "Base [inputTime=" + inputTime + ", modifyTime=" + modifyTime + ", inputName=" + inputName
				+ ", modifyName=" + modifyName  + ", tokenID=" + tokenID
				+ ", pageNo=" + pageNo + ", pageSize=" + pageSize 
				+ ", orderCol=" + orderCol + ", orderAsc=" + orderAsc + "]";
	}

}
