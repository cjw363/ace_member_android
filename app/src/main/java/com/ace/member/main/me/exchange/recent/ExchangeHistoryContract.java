package com.ace.member.main.me.exchange.recent;


interface ExchangeHistoryContract {

	interface  HistoryView {
		void showHistoryData(int nextPage, boolean isHint);

		void showNextHistoryData(int page);
	}

	interface  HistoryPresenter {
		void getHistoryList(int page);
	}
}
