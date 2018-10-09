package com.ace.member.main.me.payment_history;

public interface PaymentHistoryContract {

	interface PaymentHistoryView {
		void showList(int nextPage, boolean isHint);

		void showNextList(int page);
	}

	interface PaymentHistoryPresenter {
		void getList(String DateStart, String DateEnd, String type, int page);
	}

}
