package com.yootii.bdy.material.model;

import java.util.List;


public class MaterialCondition {
   
    private Integer caseId;
    
    public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public List<Integer> getFileNames() {
		return fileNames;
	}

	public void setFileNames(List<Integer> fileNames) {
		this.fileNames = fileNames;
	}

	private List<Integer> fileNames;
    
    
  
}