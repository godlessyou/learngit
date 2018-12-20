package com.yootii.bdy.tmcase.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TmCategory {
   

    private String classes;
 

    private String goods;
    
    
    @JsonIgnore	
	private String reason; //excel文件中这条数据不正确的原因
	
	@JsonIgnore	
	private String lineNo; // excel文件中的行号    
	
	
	public String getClasses() {
		return classes;
	}


	public void setClasses(String classes) {
		this.classes = classes;
	}


	public String getGoods() {
		return goods;
	}


	public void setGoods(String goods) {
		this.goods = goods;
	}


	

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}


	

   
}