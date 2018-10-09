package com.ace.member.main.me.transaction;


interface TransactionContract {

	interface TransactionView {
		void showTransactionData(int nextPage, boolean isHint);

		void showNextTransactionData(int page);
	}

	interface TransactionPresenter {
		void getTransactionList(int page);
	}
}
