package com.ace.member.bean;


import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class CurrencyExchangeBean extends BaseBean {

	/**
	 * id : 21
	 * time : 2017-12-01 15:27:05
	 * currency_source : USD
	 * currency_target : KHR
	 * exchange : 4037.00000000
	 * amount_source : 100.00
	 * amount_target : 403700.00
	 * remark : Currency Exchange, from USD 100.00 to KHR 403,700.00, Rate: 4,037.00000000
	 */

	private int id;
	private String time;
	@SerializedName("currency_source")
	private String sourceCurrency;
	@SerializedName("currency_target")
	private String targetCurrency;
	@SerializedName("exchange")
	private String exchangeRate;
	@SerializedName("amount_source")
	private String sourceAmount;
	@SerializedName("amount_target")
	private String targetAmount;
	private String remark;

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

	public String getSourceCurrency() {
		return sourceCurrency;
	}

	public void setSourceCurrency(String sourceCurrency) {
		this.sourceCurrency = sourceCurrency;
	}

	public String getTargetCurrency() {
		return targetCurrency;
	}

	public void setTargetCurrency(String targetCurrency) {
		this.targetCurrency = targetCurrency;
	}

	public String getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getSourceAmount() {
		return sourceAmount;
	}

	public void setSourceAmount(String sourceAmount) {
		this.sourceAmount = sourceAmount;
	}

	public String getTargetAmount() {
		return targetAmount;
	}

	public void setTargetAmount(String targetAmount) {
		this.targetAmount = targetAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
