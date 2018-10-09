package com.ace.member.bean;

import com.ace.member.BuildConfig;
import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;


public class BaseEdcWsaBill extends BaseBean {
	protected int id;

	protected String time;

	protected int type;

	@SerializedName("customer_phone")
	protected String phone;

	protected String currency;

	protected String amount;

	protected String fee;

	protected int status;

	protected String filename;

	@SerializedName("applicant_remark")
	protected String remark;

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getRealNormalFileName() {
		return BuildConfig.FILE_BASE_URL + "images/normal/" + filename;
	}

	public String getRealThumbnailFileName() {
		return BuildConfig.FILE_BASE_URL + "images/thumbnails/" + filename;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTotal() {
		try {
			return String.valueOf(Double.parseDouble(amount) + Double.parseDouble(fee));
		} catch (Exception e) {
			return "0";
		}
	}
}
