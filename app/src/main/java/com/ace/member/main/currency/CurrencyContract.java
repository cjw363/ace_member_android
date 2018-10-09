package com.ace.member.main.currency;


import com.ace.member.bean.BalanceRecord;

import java.util.List;

public class CurrencyContract {
	interface View {
		void showRefreshStatus(boolean isRefreshing);

		void showRefreshResult(String msg);

		void setRecordList(List<BalanceRecord> list);

		void setBalance(String balance);

		String getBalance();

		void showDeposit();

		void showWithDraw();

		void showMsg(String msg);

		void enableDeposit(boolean result);

		void enableWithdraw(boolean result);
	}

	interface Presenter {
		void start(String currency, boolean isRefresh);

		void getBalanceRecord(String currency, boolean isRefresh);

		void deposit();

		void withdraw(String balance);
	}
}
