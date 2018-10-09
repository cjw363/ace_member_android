package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;


public class PhoneCompany extends BaseBean {
	private int id;

	@SerializedName("country_code")
	private int countryCode;

	private String name;

	private String currency;

	@SerializedName("member_discount")
	private int memberDiscount;

	private int status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(int countryCode) {
		this.countryCode = countryCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public int getMemberDiscount() {
		return memberDiscount;
	}

	public void setMemberDiscount(int memberDiscount) {
		this.memberDiscount = memberDiscount;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
