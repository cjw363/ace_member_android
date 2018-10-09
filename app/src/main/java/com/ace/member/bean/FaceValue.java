package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;


public class FaceValue extends BaseBean {

	@SerializedName("face_value")
	private int mFaceValue;

	@SerializedName("status")
	private int mStatus;

	@SerializedName("count")
	private int mCount;

	public int getFaceValue() {
		return mFaceValue;
	}

	public void setFaceValue(int faceValue) {
		mFaceValue = faceValue;
	}

	public int getStatus() {
		return mStatus;
	}

	public void setStatus(int status) {
		mStatus = status;
	}

	public int getCount() {
		return mCount;
	}

	public void setCount(int count) {
		mCount = count;
	}
}
