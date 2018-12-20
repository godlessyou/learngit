package com.yootii.bdy.downloadapplicant.model;

import java.util.List;

public class DocTitle {
    private Integer titleId;

    private Integer docTypeId;

    private String orderNum;

    private Integer parentId;

    private Integer level;

    private Integer userId;

    private Integer isBase;
    
    private List<DocTitleWithBLOBs>  children;
    
    private Integer checked;

    public Integer getTitleId() {
        return titleId;
    }

    public void setTitleId(Integer titleId) {
        this.titleId = titleId;
    }

    public Integer getDocTypeId() {
        return docTypeId;
    }

    public void setDocTypeId(Integer docTypeId) {
        this.docTypeId = docTypeId;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum == null ? null : orderNum.trim();
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getIsBase() {
        return isBase;
    }

    public void setIsBase(Integer isBase) {
        this.isBase = isBase;
    }

	public List<DocTitleWithBLOBs> getChildren() {
		return children;
	}

	public void setChildren(List<DocTitleWithBLOBs> children) {
		this.children = children;
	}

	public Integer getChecked() {
		return checked;
	}

	public void setChecked(Integer checked) {
		this.checked = checked;
	}
}