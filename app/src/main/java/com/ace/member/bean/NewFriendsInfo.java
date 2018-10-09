package com.ace.member.bean;

import com.ace.member.base.BaseBean;

public class NewFriendsInfo extends BaseBean {
	private int id;
	private String time;
	private String name;
	private int type;
	private String content;
	private int typeAdd;
	private int status;
	private String portrait;
	private String phone;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public int getTypeAdd() {
		return typeAdd;
	}

	public void setTypeAdd(int typeAdd) {
		this.typeAdd = typeAdd;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}

