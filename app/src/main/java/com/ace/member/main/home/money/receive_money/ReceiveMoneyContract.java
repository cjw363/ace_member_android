package com.ace.member.main.home.money.receive_money;


public interface ReceiveMoneyContract {

	interface View{
		void unregisterSocketReceiver();

		void initCode(String code);

		void setCurrencyAmount(String currencyAmount);

		void setCheckRbUsd();
		void setCheckRbKhr();
		void setCheckRbVnd();
		void setCheckRbThb();

		void showReceiveMoney(ReceiveMoneyResult result);
	}

	interface Presenter{
		void start();

		void requestResult();

		void onSelectCurrency(String currency,String amount);
	}

}
