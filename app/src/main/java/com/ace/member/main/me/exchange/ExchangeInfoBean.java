package com.ace.member.main.me.exchange;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class ExchangeInfoBean extends BaseBean {

	@SerializedName("exchange_rate")
	private Map<String, String> exchangeRate;

	public Map<String, String> getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Map<String, String> exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
}
