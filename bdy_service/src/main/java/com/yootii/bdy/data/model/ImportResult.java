package com.yootii.bdy.data.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ImportResult {

	@JsonIgnore	
    private String message;
	
	int success;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	int fail;
	
	List<Reason> Reasons;

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public int getFail() {
		return fail;
	}

	public void setFail(int fail) {
		this.fail = fail;
	}

	public List<Reason> getReasons() {
		return Reasons;
	}

	public void setReasons(List<Reason> reasons) {
		Reasons = reasons;
	}
	
	
}
