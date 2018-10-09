package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;


public class EdcWsaConfig extends BaseBean {
	@SerializedName("split")
	private int mSplit;

	@SerializedName("fee")
	private double mFee;

	public int getSplit() {
		return mSplit;
	}

	public void setSplit(int split) {
		mSplit = split;
	}

	public double getFee() {
		return mFee;
	}

	public void setFee(double fee) {
		mFee = fee;
	}
}
