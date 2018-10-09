package com.ace.member.bean;

import com.ace.member.base.BaseBean;


public class SocketMessage extends BaseBean {
	private String msg;
	private int type;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
