package com.yootii.bdy.util;

import java.util.List;

public class ExcelRow {
	
	int rowNumber;
	
	List<Object> colList;
	
	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public List<Object> getColList() {
		return colList;
	}

	public void setColList(List<Object> colList) {
		this.colList = colList;
	}

	@Override
	public String toString() {
		return "ExcelRow [rowNumber=" + rowNumber + ", colList=" + colList + "]";
	}

	

	
}
