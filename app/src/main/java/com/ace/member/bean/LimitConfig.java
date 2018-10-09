package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class LimitConfig extends BaseBean {
	@SerializedName("currency")
	private String currency;

	@SerializedName("max_balance")
	private int maxBalance;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public int getMaxBalance() {
		return maxBalance;
	}

	public void setMaxBalance(int maxBalance) {
		this.maxBalance = maxBalance;
	}
}
