package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;


public class TradingPasswordInfo extends BaseBean {
	private int status;

	@SerializedName("is_valid")
	private boolean isValid;

	private int remain;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean valid) {
		isValid = valid;
	}

	public int getRemain() {
		return remain;
	}

	public void setRemain(int remain) {
		this.remain = remain;
	}
}
