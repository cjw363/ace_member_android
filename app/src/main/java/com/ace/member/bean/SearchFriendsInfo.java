package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class SearchFriendsInfo extends BaseBean {
	private int id;
	private String name;
	private String content;
	@SerializedName("member_id")
	private int memberId;
	private int status;
	@SerializedName("type_add")
	private int typeAdd;
	@SerializedName("is_application")
	private boolean isApplication;
	private int type;
	private String portrait;

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isApplication() {
		return isApplication;
	}

	public void setApplication(boolean application) {
		isApplication = application;
	}
}
