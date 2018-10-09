package com.ace.member.main.detail.exchange_detail;

import com.ace.member.bean.CurrencyExchangeBean;

public interface ExchangeDetailContract {

	interface View {
		void setData(CurrencyExchangeBean data);
	}

	interface Presenter {
		void getData(int id);
	}
}
