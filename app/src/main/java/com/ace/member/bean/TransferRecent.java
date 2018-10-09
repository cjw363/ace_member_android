package com.ace.member.bean;

import android.text.TextUtils;

import com.ace.member.R;
import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class TransferRecent extends BaseBean {

	private int id;

	@SerializedName("user_id")
	private int userID;

	@SerializedName(value = "target", alternate = {"target_phone"})
	private String targetPhone;

	private String time;

	@SerializedName("currency")
	private String mCurrency;

	@SerializedName("phone")
	private String mPhone;

	@SerializedName("amount")
	private double mAmount;

	@SerializedName("fee")
	private double mFee;

	@SerializedName("status")
	private int mStatus;

	@SerializedName("name")
	private String mName;

	@SerializedName("accept_code")
	private String mAcceptCode;

	@SerializedName("remark")
	private String mRemark;

	@SerializedName("source_phone")
	private String sourcePhone;

	@SerializedName("source_name")
	private String sourceName;

	@SerializedName("target_name")
	private String targetName;

	private boolean flagSameDate;

	private boolean isDateEnd = false;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getTargetPhone() {
		return targetPhone;
	}

	public void setTargetPhone(String targetPhone) {
		this.targetPhone = targetPhone;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getCurrency() {
		return mCurrency;
	}

	public void setCurrency(String currency) {
		mCurrency = currency;
	}

	public String getPhone() {
		return mPhone;
	}

	public void setPhone(String phone) {
		mPhone = phone;
	}

	public double getAmount() {
		return mAmount;
	}

	public void setAmount(double amount) {
		mAmount = amount;
	}

	public double getFee() {
		return mFee;
	}

	public void setFee(double fee) {
		mFee = fee;
	}

	public int getStatus() {
		return mStatus;
	}

	public void setStatus(int status) {
		mStatus = status;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public int getTitle() {
		if (!TextUtils.isEmpty(mName)) {
			return R.string.to_member;
		} else {
			return R.string.to_non_member;
		}
	}

	public String getAcceptCode() {
		return mAcceptCode;
	}

	public void setAcceptCode(String acceptCode) {
		mAcceptCode = acceptCode;
	}

	public String getRemark() {
		return mRemark;
	}

	public void setRemark(String remark) {
		mRemark = remark;
	}

	public String getSourcePhone() {
		return sourcePhone;
	}

	public void setSourcePhone(String sourcePhone) {
		this.sourcePhone = sourcePhone;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public boolean isFlagSameDate() {
		return flagSameDate;
	}

	public void setFlagSameDate(boolean flagSameDate) {
		this.flagSameDate = flagSameDate;
	}

	public boolean isDateEnd() {
		return isDateEnd;
	}

	public void setDateEnd(boolean dateEnd) {
		isDateEnd = dateEnd;
	}
}
