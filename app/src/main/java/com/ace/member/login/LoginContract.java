package com.ace.member.login;

import android.content.SharedPreferences;

import android.support.v4.app.FragmentManager;

import com.og.utils.ItemObject;

import java.util.List;
import java.util.Map;

public interface LoginContract {

	interface LoginView{
		String getPhone();
		String getPassword();
		SharedPreferences getPreferencesUser();
		void toMainActivity();
		void toResetPasswordActivity();
		void toLockSetupActivity();
		int getActionType();
		void showPrompt();
		void setAgainLogin();
		void showLogin();
		void clearPassword();
		void initCountryCodeList(List<ItemObject> list);
		FragmentManager getInstance();
		void showRegister(boolean functionRegister);
	}
	interface LoginPresenter{
		void getLoginData();
		void login(Map<String,String> p);
	}
}
