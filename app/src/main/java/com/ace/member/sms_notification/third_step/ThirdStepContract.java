package com.ace.member.sms_notification.third_step;

import android.content.SharedPreferences;

import java.util.Map;

interface ThirdStepContract {

	interface ThirdStepView {
		String getPassword();

		String getConfirmPassword();

		void finish();

		void toMainActivity();

		SharedPreferences getPreferencesGesture();

		void clearFingerprintAndGesture();

		void showToast(String msg);

		void toLogin(String msg);
	}

	interface ThirdStepPresenter {
		void checkRegisterFunction();

		void register(Map<String, String> params);

		void resetPassword(Map<String, String> params);

		boolean checkData();
	}
}
