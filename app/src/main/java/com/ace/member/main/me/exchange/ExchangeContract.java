package com.ace.member.main.me.exchange;

import com.ace.member.bean.Currency;

import java.util.List;
import java.util.Map;

public interface ExchangeContract {
	interface ExchangeContractView {
		void updateBalance(boolean isRefresh,boolean function);

		void setCurrencyList(List<Currency> data);

		void setExchangeRate(ExchangeInfoBean exchangeInfoBean);

		void clearExchangeInfo();

		void finishActivity();

		void setSubmitEnables(boolean flag);
	}

	interface ExchangeContractPresenter {

		void submitExchange(Map<String, String> params);

		void getBalanceInfo(boolean isLoading);
		void getExchangeRate(boolean isLoading);
	}
}
