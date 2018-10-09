package com.ace.member.main.me;


import com.ace.member.base.BaseBean;
import com.ace.member.bean.Balance;
import com.google.gson.annotations.SerializedName;

import java.util.List;

class MeData extends BaseBean {

	@SerializedName("balance_list")
	private List<Balance> mBalance;

	@SerializedName("phone_verified")
	private boolean phoneVerified;

	@SerializedName("id_verified")
	private boolean idVerified;

	@SerializedName("fingerprint_verified")
	private boolean fingerprintVerified;

	private String portrait;

	public List<Balance> getBalance() {
		return mBalance;
	}

	public void setBalance(List<Balance> balance) {
		mBalance = balance;
	}

	public boolean isPhoneVerified() {
		return phoneVerified;
	}

	public void setPhoneVerified(boolean phoneVerified) {
		this.phoneVerified = phoneVerified;
	}

	public boolean isIdVerified() {
		return idVerified;
	}

	public void setIdVerified(boolean idVerified) {
		this.idVerified = idVerified;
	}

	public boolean isFingerprintVerified() {
		return fingerprintVerified;
	}

	public void setFingerprintVerified(boolean fingerprintVerified) {
		this.fingerprintVerified = fingerprintVerified;
	}

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}
}
