package com.ace.member.main.home.money.withdraw_cash.confirm;


public interface WithdrawCashConfirmContract {
	interface WithdrawCashConfirmView {
		String getWithdrawID();

		void unregisterSocketReceiver();

		void finishSelf();
	}

	interface WithdrawCashConfirmPresenter {

	}
}
