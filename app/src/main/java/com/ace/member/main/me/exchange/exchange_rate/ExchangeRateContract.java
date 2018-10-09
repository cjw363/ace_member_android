package com.ace.member.main.me.exchange.exchange_rate;

import org.json.JSONObject;

public interface ExchangeRateContract {
	interface ExchangeRateContractView {

		void setExchangeRate(JSONObject rate);

		void finishActivity();

	}

	interface ExchangeRateContractPresenter {

		void getExchangeRate(boolean isLoading);
	}
}
