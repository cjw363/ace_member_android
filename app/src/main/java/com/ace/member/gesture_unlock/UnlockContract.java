package com.ace.member.gesture_unlock;

import android.content.SharedPreferences;

import java.util.Map;

public interface UnlockContract {
	interface UnlockView {
		SharedPreferences getPreferencesUser();
		void toMainActivity();
		void showPrompt();
		void selAgainLogin();
	}

	interface UnlockPresenter {
		void login(Map<String,String> p);
		void clearDeviceID(String phone);
	}

}
