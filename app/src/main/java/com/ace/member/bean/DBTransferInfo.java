package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class DBTransferInfo extends BaseBean {
	private int id;
	private String time;
	@SerializedName("user_type")
	private int userType;
	@SerializedName("user_id")
	private int userID;
	private String currency;
	private double amount;
	private double fee;
	@SerializedName("to_user_type")
	private int toUserType;
	@SerializedName("to_user_id")
	private int toUserID;
	private int status;
	private String remark;
	private String lmt;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public int getToUserType() {
		return toUserType;
	}

	public void setToUserType(int toUserType) {
		this.toUserType = toUserType;
	}

	public int getToUserID() {
		return toUserID;
	}

	public void setToUserID(int toUserID) {
		this.toUserID = toUserID;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getLmt() {
		return lmt;
	}

	public void setLmt(String lmt) {
		this.lmt = lmt;
	}
}
