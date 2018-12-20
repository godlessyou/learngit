package com.yootii.bdy.task.model;

import java.util.List;
import java.util.Map;

public class MailInfor {

	private String userId;
	private List<Notification> list;
	private Map<String, Object> map;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<Notification> getList() {
		return list;
	}
	public void setList(List<Notification> list) {
		this.list = list;
	}
	public Map<String, Object> getMap() {
		return map;
	}
	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	
	
}
