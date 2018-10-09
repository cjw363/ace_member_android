package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;


public class WithdrawConfig extends BaseBean {
	private String currency;

	@SerializedName("fee_unit")
	private double feeUnit;

	@SerializedName("fee_amount")
	private double feeAmount;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getFeeUnit() {
		return feeUnit;
	}

	public void setFeeUnit(double feeUnit) {
		this.feeUnit = feeUnit;
	}

	public double getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(double feeAmount) {
		this.feeAmount = feeAmount;
	}
}
