package com.ace.member.start;

import com.og.event.MessageEvent;

public interface StartContract {

	interface StartView {
		void showCheckForDialog();

		void showBlock(String msg);

		void showVersionNotSupported();

		void hideVersionNotSupported();

		void setIsLogin(boolean isLogin);

		boolean getIsLogin();

		String getGesture();

		void setVersion(String version);

		void superMessageEvent(MessageEvent messageEvent);

		void showUnlock();

		void showFingerprintDialog(String phone);

		void showLogin();

		void showPrompt();

		void setAgainLogin();

		void finishSelf();

		void toMainActivity();
	}

	interface StartPresenter {
		void start();

		void onMessageEvent(MessageEvent messageEvent);
	}
}
