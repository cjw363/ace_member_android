package com.ace.member.main.me.exchange;

import com.ace.member.base.BaseBean;
import com.ace.member.bean.Balance;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExchangeResultBead extends BaseBean {
	@SerializedName("id")
	private int mID;

	@SerializedName("source_currency")
	private String mSourceCurrency;

	@SerializedName("source_amount")
	private double mSourceAmount;

	@SerializedName("target_currency")
	private String mTargetCurrency;

	@SerializedName("target_amount")
	private double mTargetAmount;

	@SerializedName("time")
	private String mTime;
	@SerializedName("balance")

	public int getID() {
		return mID;
	}

	public void setID(int ID) {
		mID = ID;
	}

	public String getSourceCurrency() {
		return mSourceCurrency;
	}

	public void setSourceCurrency(String sourceCurrency) {
		mSourceCurrency = sourceCurrency;
	}

	public double getSourceAmount() {
		return mSourceAmount;
	}

	public void setSourceAmount(double sourceAmount) {
		mSourceAmount = sourceAmount;
	}

	public String getTargetCurrency() {
		return mTargetCurrency;
	}

	public void setTargetCurrency(String targetCurrency) {
		mTargetCurrency = targetCurrency;
	}

	public double getTargetAmount() {
		return mTargetAmount;
	}

	public void setTargetAmount(double targetAmount) {
		mTargetAmount = targetAmount;
	}

	public String getTime() {
		return mTime;
	}

	public void setTime(String time) {
		mTime = time;
	}
}
