package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class TransferCashConfig extends BaseBean {
	private String currency;

	@SerializedName("split_amount")
	private int splitAmount;

	@SerializedName("fee_amount")
	private double feeAmount;

	@SerializedName("commission_amount_from")
	private double commissionAmountFrom;

	@SerializedName("commission_amount_to")
	private double commissionAmountTo;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public int getSplitAmount() {
		return splitAmount;
	}

	public void setSplitAmount(int splitAmount) {
		this.splitAmount = splitAmount;
	}

	public double getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(double feeAmount) {
		this.feeAmount = feeAmount;
	}

	public double getCommissionAmountFrom() {
		return commissionAmountFrom;
	}

	public void setCommissionAmountFrom(double commissionAmountFrom) {
		this.commissionAmountFrom = commissionAmountFrom;
	}

	public double getCommissionAmountTo() {
		return commissionAmountTo;
	}

	public void setCommissionAmountTo(double commissionAmountTo) {
		this.commissionAmountTo = commissionAmountTo;
	}
}
