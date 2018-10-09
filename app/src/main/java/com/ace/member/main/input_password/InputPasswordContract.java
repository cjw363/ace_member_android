package com.ace.member.main.input_password;

public interface InputPasswordContract {

	interface View {
		void toTradingPassword();

		void showIncorrectPwdDlg(int remainFailCount);

		void showTradingPasswordStatusDlg(int status,int remain);

		void showSelf();

		void verifyPasswordSuccess();

		void verifyPasswordFail();

		void clearPassword();

		void onLock();

		void onCancel();

		void setLock(boolean lock);
	}

	interface Presenter {
		void start();

		void checkTradingPwd(String pwd);
	}
}
