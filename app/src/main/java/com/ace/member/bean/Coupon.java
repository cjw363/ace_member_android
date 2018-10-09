package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;


public class Coupon extends BaseBean {
	private int id;
	private String time;
	@SerializedName("date_expire")
	private String dateExpire;
	private int type;
	private int status;
	@SerializedName("time_use")
	private String timeUse;

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

	public String getDateExpire() {
		return dateExpire;
	}

	public void setDateExpire(String dateExpire) {
		this.dateExpire = dateExpire;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTimeUse() {
		return timeUse;
	}

	public void setTimeUse(String timeUse) {
		this.timeUse = timeUse;
	}
}
