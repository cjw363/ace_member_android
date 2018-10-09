package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;


public class PayDataWrapper extends BaseBean {
	private User user;

	@SerializedName("function_running")
	private boolean functionRunning;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isFunctionRunning() {
		return functionRunning;
	}

	public void setFunctionRunning(boolean functionRunning) {
		this.functionRunning = functionRunning;
	}
}
