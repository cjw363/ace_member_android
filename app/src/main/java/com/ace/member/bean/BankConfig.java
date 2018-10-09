package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;


public class BankConfig extends BaseBean {
	@SerializedName("bank_code")
	private String bankCode;

	@SerializedName("transfer_amount_unit")
	private double transferAmountUnit;

	@SerializedName("transfer_amount_unit_fee")
	private double transferAmountUnitFee;

	@SerializedName("transfer_amount_max_per_day")
	private double transferAmountMaxPerDay;

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public double getTransferAmountUnit() {
		return transferAmountUnit;
	}

	public void setTransferAmountUnit(Double transferAmountUnit) {
		this.transferAmountUnit = transferAmountUnit;
	}

	public double getTransferAmountUnitFee() {
		return transferAmountUnitFee;
	}

	public void setTransferAmountUnitFee(Double transferAmountUnitFee) {
		this.transferAmountUnitFee = transferAmountUnitFee;
	}

	public double getTransferAmountMaxPerDay() {
		return transferAmountMaxPerDay;
	}

	public void setTransferAmountMaxPerDay(double transferAmountMaxPerDay) {
		this.transferAmountMaxPerDay = transferAmountMaxPerDay;
	}
}
