package com.ace.member.main.me.password;


public interface PasswordContract {

	interface PasswordView {
		void checkStatus(int tradingStatus, int certificateStatus, String reason);
		void chkInputPasswordSuccess();
		void showCheckPasswordDialog(int msg);
	}

	interface PasswordPresenter{
		void checkTradingStatus();
		void checkInputPassword(String password);
		void clearGesturePassword();
	}
}
