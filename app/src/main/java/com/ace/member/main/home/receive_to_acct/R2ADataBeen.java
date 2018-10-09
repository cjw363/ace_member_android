package com.ace.member.main.home.receive_to_acct;

import com.ace.member.base.BaseBean;
import com.ace.member.bean.PageBaseBean;
import com.ace.member.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

public class R2ADataBeen extends PageBaseBean<R2ADataBeen> {

	@SerializedName("source_type")
	private String sourceType;

	@SerializedName("source")
	private String source;

	@SerializedName("remark")
	private String remark;

	@SerializedName("status")
	private int status;

	@SerializedName("accept_code")
	private String acceptCode;

	@SerializedName("currency")
	private String currency;

	@SerializedName("phone_source")
	private String phoneSource;

	@SerializedName("amount")
	private String amount;

	@SerializedName("time")
	private String time;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getAmount() {
		return AppUtils.simplifyAmount(currency, amount);
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPhoneSource() {
		return phoneSource;
	}

	public void setPhoneSource(String phoneSource) {
		this.phoneSource = phoneSource;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAcceptCode() {
		return acceptCode;
	}

	public void setAcceptCode(String acceptCode) {
		this.acceptCode = acceptCode;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSourceName() {
		switch (Integer.parseInt(sourceType)) {
			case 1:
				return "[Member] " + source;
			case 2:
				return "[Agent] " + source;
			case 3:
				return "[Branch]" + source;
			case 99:
				return "[System]";
			default:
				return "";
		}
	}

	public String getSender() {
		if(Integer.parseInt(sourceType) == 99) return "[System]";
		else return this.phoneSource;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

}
