package com.yootii.bdy.tmcase.model;


public class JointApplicant {
	//共有人类型
	private String joinAppType;
	//共有人国籍
	private String joinAppCoun;
	//共有人中文名称
	private String nameCn;
	//共有人英文名称
	private String nameEn;
	//共有人中文地址
	private String addrCn;
	//英文地址
	private String addrEn;
	//证件名称
	private String cardName;
	//证件号
	private String cardId;
	//证明文件原件是否为中文
	private String getFileIsEn;
	//身份证明文件
	private String getSfFile;
	//身份证明文件外文
	private String getSfFileEn;
	//主体资格证明文件
	private String getZtFile;
	//主体资格证明文件外文
	private String getZtFileEn;
	//共有人注意事项
	private Integer type;
	//共有人注意事项
	private String getCheck;//checkbox共有人为非自然人、非政府机关
	public String getJoinAppType() {
		return joinAppType;
	}
	public void setJoinAppType(String joinAppType) {
		this.joinAppType = joinAppType;
	}
	public String getJoinAppCoun() {
		return joinAppCoun;
	}
	public void setJoinAppCoun(String joinAppCoun) {
		this.joinAppCoun = joinAppCoun;
	}
	public String getNameCn() {
		return nameCn;
	}
	public void setNameCn(String nameCn) {
		this.nameCn = nameCn;
	}
	public String getNameEn() {
		return nameEn;
	}
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}
	public String getAddrCn() {
		return addrCn;
	}
	public void setAddrCn(String addrCn) {
		this.addrCn = addrCn;
	}
	public String getAddrEn() {
		return addrEn;
	}
	public void setAddrEn(String addrEn) {
		this.addrEn = addrEn;
	}
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getGetFileIsEn() {
		return getFileIsEn;
	}
	public void setGetFileIsEn(String getFileIsEn) {
		this.getFileIsEn = getFileIsEn;
	}
	public String getGetSfFile() {
		return getSfFile;
	}
	public void setGetSfFile(String getSfFile) {
		this.getSfFile = getSfFile;
	}
	public String getGetSfFileEn() {
		return getSfFileEn;
	}
	public void setGetSfFileEn(String getSfFileEn) {
		this.getSfFileEn = getSfFileEn;
	}
	public String getGetZtFile() {
		return getZtFile;
	}
	public void setGetZtFile(String getZtFile) {
		this.getZtFile = getZtFile;
	}
	public String getGetZtFileEn() {
		return getZtFileEn;
	}
	public void setGetZtFileEn(String getZtFileEn) {
		this.getZtFileEn = getZtFileEn;
	}
	public String getGetCheck() {
		return getCheck;
	}
	public void setGetCheck(String getCheck) {
		this.getCheck = getCheck;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
}
