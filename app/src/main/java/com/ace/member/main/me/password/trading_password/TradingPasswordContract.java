package com.ace.member.main.me.password.trading_password;


public interface TradingPasswordContract {

	interface TradingPasswordView {
		String getNewPassword();

		String getConfirmPassword();

		String getOldPassword();

		void finishActivity();

		void checkStatus(int statusType, int certificateStatus, String reason);

//		void whetherToVerify();

		void enableSubmit(boolean flag);
	}

	interface TradingPasswordPresenter{
		void request(int status);

		boolean checkData(int status);

//		void checkTradingStatus();
		void addForgotPasswordLog();

	}
}
