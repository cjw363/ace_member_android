package com.ace.member.bean;

import com.ace.member.base.BaseBean;

public class ContactInfo extends BaseBean {
	private String name;
	private String phone;
	private int memberId;
	private String FirstLetter;
	private boolean letterFlag;//是否是字母标识
	private String portrait;

	public ContactInfo() {}

	public ContactInfo(String name, String phone) {
		this.name = name;
		this.phone = phone;
	}

	public ContactInfo(int memberId, String name) {
		this.memberId = memberId;
		this.name = name;
	}

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isLetterFlag() {
		return letterFlag;
	}

	public void setLetterFlag(boolean letterFlag) {
		this.letterFlag = letterFlag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstLetter() {
		return FirstLetter;
	}

	public void setFirstLetter(String firstLetter) {
		FirstLetter = firstLetter;
	}
}
