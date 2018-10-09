package com.ace.member.main.home;


import com.og.event.MessageEvent;

public interface HomeContract {
	interface HomeView {
		void setBalance();

		void showRefreshStatus(boolean isRefreshing);

		void toLockSetupActivity();
		void toSMSNotificationActivity();
		void toPasswordActivity();
	}

	interface HomePresenter {
		void getBalance(boolean isRefresh);

		void doOtherDialogSetting();

		void onMessageEvent(MessageEvent messageEvent);
	}
}
