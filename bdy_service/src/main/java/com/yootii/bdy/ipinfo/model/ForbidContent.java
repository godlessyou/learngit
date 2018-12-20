package com.yootii.bdy.ipinfo.model;

public class ForbidContent {
    private Integer id;
    
    private Integer result;//含义， 1：未查到禁止注册内容，2：查到禁止注册的内容

    private String content;

    private String cause;

    private String innerMemo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause == null ? null : cause.trim();
    }

    public String getInnerMemo() {
        return innerMemo;
    }

    public void setInnerMemo(String innerMemo) {
        this.innerMemo = innerMemo == null ? null : innerMemo.trim();
    }

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}
}