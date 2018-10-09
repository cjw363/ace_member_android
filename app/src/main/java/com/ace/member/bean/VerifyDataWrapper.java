package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;


public class VerifyDataWrapper extends BaseBean {
	private Verify verify;

	private MemberProfile profile;

	public Verify getVerify() {
		return verify;
	}

	public void setVerify(Verify verify) {
		this.verify = verify;
	}

	public MemberProfile getProfile() {
		return profile;
	}

	public void setProfile(MemberProfile profile) {
		this.profile = profile;
	}
}
