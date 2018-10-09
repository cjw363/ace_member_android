package com.ace.member.bean;


import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class Transaction extends BaseBean {

	/**
	 * time : 2017-05-10 14:18:01
	 * type : 2
	 * sub_type : 203
	 * currency : USD
	 * amount : 213123.00
	 * biz_id : 76
	 * main_id : 0
	 * staff_id : 1000
	 * remark :
	 * staff_name : ADMIN
	 * main_sub_type : 203
	 * main_biz_id : 75
	 */

	private String date;

	private String time;

	private String type;

	@SerializedName("sub_type")
	private int subType;

	private String currency;

	private String amount;

	@SerializedName("biz_id")
	private int bizID;

	@SerializedName("main_id")
	private int mainID;

	@SerializedName("staff_id")
	private String staffID;

	private String remark;

	@SerializedName("staff_name")
	private String staffName;

	@SerializedName("main_sub_type")
	private int mainSubType;

	@SerializedName("main_biz_id")
	private int mainBizID;

	private boolean flagSameDate;

	private String total;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSubType() {
		return subType;
	}

	public void setSubType(int subType) {
		this.subType = subType;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public int getBizID() {
		return bizID;
	}

	public void setBizID(int bizID) {
		this.bizID = bizID;
	}

	public int getMainID() {
		return mainID;
	}

	public void setMainID(int mainID) {
		this.mainID = mainID;
	}

	public String getStaffID() {
		return staffID;
	}

	public void setStaffID(String staffID) {
		this.staffID = staffID;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public int getMainSubType() {
		return mainSubType;
	}

	public void setMainSubType(int mainSubType) {
		this.mainSubType = mainSubType;
	}

	public int getMainBizID() {
		return mainBizID;
	}

	public void setMainBizID(int mainBizID) {
		this.mainBizID = mainBizID;
	}

	public boolean isFlagSameDate() {
		return flagSameDate;
	}

	public void setFlagSameDate(boolean flagSameDate) {
		this.flagSameDate = flagSameDate;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}
}
