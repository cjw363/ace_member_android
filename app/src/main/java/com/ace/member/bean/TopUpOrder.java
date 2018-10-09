package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;


public class TopUpOrder extends BaseBean {
	private int id;

	@SerializedName("time")
	private String mTime;

	@SerializedName("currency")
	private String mCurrency;

	@SerializedName("face_value")
	private int mFaceValue;

	@SerializedName("top_up_type")
	private int mTopUpType;

	@SerializedName("phone")
	private String mPhone;

	@SerializedName("pincode2")
	private String mPincode2;

	@SerializedName("price")
	private double mPrice;

	@SerializedName("phone_company_name")
	private String mPhoneCompanyName;

	@SerializedName("sn")
	private String mSn;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTime() {
		return mTime;
	}

	public void setTime(String time) {
		mTime = time;
	}

	public String getCurrency() {
		return mCurrency;
	}

	public void setCurrency(String currency) {
		mCurrency = currency;
	}

	public int getFaceValue() {
		return mFaceValue;
	}

	public void setFaceValue(int faceValue) {
		mFaceValue = faceValue;
	}

	public int getTopUpType() {
		return mTopUpType;
	}

	public void setTopUpType(int topUpType) {
		mTopUpType = topUpType;
	}

	public String getPhone() {
		return mPhone;
	}

	public void setPhone(String phone) {
		mPhone = phone;
	}

	public String getPincode2() {
		return mPincode2;
	}

	public void setPincode2(String pincode2) {
		mPincode2 = pincode2;
	}

	public double getPrice() {
		return mPrice;
	}

	public void setPrice(double price) {
		mPrice = price;
	}

	public String getPhoneCompanyName() {
		return mPhoneCompanyName;
	}

	public void setPhoneCompanyName(String phoneCompanyName) {
		mPhoneCompanyName = phoneCompanyName;
	}

	public String getSn() {
		return mSn;
	}

	public void setSn(String sn) {
		mSn = sn;
	}
}
