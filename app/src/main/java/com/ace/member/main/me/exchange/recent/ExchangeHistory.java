package com.ace.member.main.me.exchange.recent;


import com.ace.member.bean.PageBaseBean;
import com.google.gson.annotations.SerializedName;

public class ExchangeHistory extends PageBaseBean<ExchangeHistory> {

	@SerializedName("currency_source")
	private String currencySource;

	private String time;

	@SerializedName("currency_target")
	private String currencyTarget;

	@SerializedName("amount_source")
	private double amountSource;

	@SerializedName("amount_target")
	private double amountTarget;

	private boolean flagSameDate;

	public boolean isFlagSameDate() {
		return flagSameDate;
	}

	public void setFlagSameDate(boolean flagSameDate) {
		this.flagSameDate = flagSameDate;
	}

	public String getCurrencySource() {
		return currencySource;
	}

	public void setCurrencySource(String currencySource) {
		this.currencySource = currencySource;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getCurrencyTarget() {
		return currencyTarget;
	}

	public void setCurrencyTarget(String currencyTarget) {
		this.currencyTarget = currencyTarget;
	}

	public double getAmountSource() {
		return amountSource;
	}

	public void setAmountSource(double amountSource) {
		this.amountSource = amountSource;
	}

	public double getAmountTarget() {
		return amountTarget;
	}

	public void setAmountTarget(double amountTarget) {
		this.amountTarget = amountTarget;
	}
}
