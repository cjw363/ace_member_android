package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

import java.util.Date;


public class CurrencyMarket extends BaseBean {
	@SerializedName("currency_source")
	private String mCurrencySource;

	@SerializedName("currencyTarget")
	private String mCurrencyTarget;

	@SerializedName("exchange_buy")
	private Double mExchangeBuy;

	@SerializedName("exchange_sell")
	private Double mExchangeSell;

	@SerializedName("exchange_min")
	private Double mExchangeMin;

	@SerializedName("exchange_max")
	private Double mExchangeMax;

	@SerializedName("lmt")
	private Date mLmt;

	public String getCurrencySource() {
		return mCurrencySource;
	}

	public void setCurrencySource(String currencySource) {
		mCurrencySource = currencySource;
	}

	public String getCurrencyTarget() {
		return mCurrencyTarget;
	}

	public void setCurrencyTarget(String currencyTarget) {
		mCurrencyTarget = currencyTarget;
	}

	public Double getExchangeBuy() {
		return mExchangeBuy;
	}

	public void setExchangeBuy(Double exchangeBuy) {
		mExchangeBuy = exchangeBuy;
	}

	public Double getExchangeSell() {
		return mExchangeSell;
	}

	public void setExchangeSell(Double exchangeSell) {
		mExchangeSell = exchangeSell;
	}

	public Double getExchangeMin() {
		return mExchangeMin;
	}

	public void setExchangeMin(Double exchangeMin) {
		mExchangeMin = exchangeMin;
	}

	public Double getExchangeMax() {
		return mExchangeMax;
	}

	public void setExchangeMax(Double exchangeMax) {
		mExchangeMax = exchangeMax;
	}

	public Date getLmt() {
		return mLmt;
	}

	public void setLmt(Date lmt) {
		mLmt = lmt;
	}
}
