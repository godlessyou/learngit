package com.yootii.bdy.tmcase.model;

import java.util.List;

import com.yootii.bdy.material.model.Material;

public class TradeMarkCaseJoinApp {
    private Integer id;

    private Integer caseId;

    private String joinAppType;

    private String joinAppCoun;

    private String nameCn;

    private String nameEn;

    private String addrCn;

    private String addrEn;

    private String cardName;

    private String cardId;

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
    private String getCheck;//checkbox共有人为非自然人、非政府机关
    
    private Integer custId;
    
    private Integer agencyId;
    
    private Integer casePreId;
    
    private Integer type;
    
    private List<Material> materials;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public String getJoinAppType() {
        return joinAppType;
    }

    public void setJoinAppType(String joinAppType) {
        this.joinAppType = joinAppType == null ? null : joinAppType.trim();
    }

    public String getJoinAppCoun() {
        return joinAppCoun;
    }

    public void setJoinAppCoun(String joinAppCoun) {
        this.joinAppCoun = joinAppCoun == null ? null : joinAppCoun.trim();
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn == null ? null : nameCn.trim();
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn == null ? null : nameEn.trim();
    }

    public String getAddrCn() {
        return addrCn;
    }

    public void setAddrCn(String addrCn) {
        this.addrCn = addrCn == null ? null : addrCn.trim();
    }

    public String getAddrEn() {
        return addrEn;
    }

    public void setAddrEn(String addrEn) {
        this.addrEn = addrEn == null ? null : addrEn.trim();
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName == null ? null : cardName.trim();
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId == null ? null : cardId.trim();
    }

    public String getGetFileIsEn() {
        return getFileIsEn;
    }

    public void setGetFileIsEn(String getFileIsEn) {
        this.getFileIsEn = getFileIsEn == null ? null : getFileIsEn.trim();
    }

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public Integer getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(Integer agencyId) {
		this.agencyId = agencyId;
	}

	public Integer getCasePreId() {
		return casePreId;
	}

	public void setCasePreId(Integer casePreId) {
		this.casePreId = casePreId;
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

	public List<Material> getMaterials() {
		return materials;
	}

	public void setMaterials(List<Material> materials) {
		this.materials = materials;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
    
}