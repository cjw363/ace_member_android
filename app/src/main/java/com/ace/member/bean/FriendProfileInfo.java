package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class FriendProfileInfo extends BaseBean {
	private int id;
	private String name;
	@SerializedName("country_code")
	private int countryCode;
	private String phone;
	@SerializedName("is_application")
	private boolean isApplication;
	@SerializedName("member_id")
	private int memberId;//申请发起人
	private int status;
	@SerializedName("type_add")
	private int typeAdd;
	@SerializedName("name_remark")
	private String nameRemark;
	private String portrait;

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public String getNameRemark() {
		return nameRemark;
	}

	public void setNameRemark(String nameRemark) {
		this.nameRemark = nameRemark;
	}

	public int getTypeAdd() {
		return typeAdd;
	}

	public void setTypeAdd(int typeAdd) {
		this.typeAdd = typeAdd;
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

	public int getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(int countryCode) {
		this.countryCode = countryCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isApplication() {
		return isApplication;
	}

	public void setApplication(boolean application) {
		isApplication = application;
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
}
