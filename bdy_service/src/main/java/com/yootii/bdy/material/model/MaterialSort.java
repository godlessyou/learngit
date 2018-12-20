package com.yootii.bdy.material.model;

import java.util.List;

public class MaterialSort {
    private Integer id;

    private Integer parentId;

    private Integer level;

    private String name;
    
    private Integer fileName;
    
    private List<MaterialSort> nextMaterialSort;//资料类型下一级
   

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

	public List<MaterialSort> getNextMaterialSort() {
		return nextMaterialSort;
	}

	public void setNextMaterialSort(List<MaterialSort> nextMaterialSort) {
		this.nextMaterialSort = nextMaterialSort;
	}

	public Integer getFileName() {
		return fileName;
	}

	public void setFileName(Integer fileName) {
		this.fileName = fileName;
	}

	


    
    
}