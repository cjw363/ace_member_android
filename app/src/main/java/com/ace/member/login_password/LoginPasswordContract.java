package com.ace.member.login_password;

public interface LoginPasswordContract {

	interface ResetPasswordView{
		String getNewPassword();

		String getConfirmPassword();

		String getOldPassword();

		boolean getFlagReset();

		void startMainActivity();

		void finishActivity();

		void clearGesturePassword();
	}

	interface ResetPasswordPresenter{

		void request();

		boolean checkData();

		void saveGestureType(String gesture, int actionType);
	}
}
