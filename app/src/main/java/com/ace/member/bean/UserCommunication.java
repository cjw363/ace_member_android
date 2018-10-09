package com.ace.member.bean;

import java.io.Serializable;

public class UserCommunication implements Serializable {

	private String source;
	private String target;
	private int type;//im类型：11聊天消息，22好友请求

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getTarget() {
		return target;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSource() {
		return source;
	}

}
