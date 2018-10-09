package com.ace.member.bean;

import com.ace.member.R;
import com.ace.member.base.BaseBean;
import com.ace.member.utils.AppGlobal;
import com.google.gson.annotations.SerializedName;
import com.og.utils.Utils;


public class MemberProfile extends BaseBean {
	private int sex;
	private int nationality;
	@SerializedName("date_of_birth")
	private String dateOfBirth;
	@SerializedName("flag_lock")
	private int flagLock;

	public MemberProfile() {
	}

	public MemberProfile(int sex, int nationality) {
		this.sex = sex;
		this.nationality = nationality;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getNationality() {
		return nationality;
	}

	public void setNationality(int nationality) {
		this.nationality = nationality;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public int getFlagLock() {
		return flagLock;
	}

	public void setFlagLock(int flagLock) {
		this.flagLock = flagLock;
	}
}
