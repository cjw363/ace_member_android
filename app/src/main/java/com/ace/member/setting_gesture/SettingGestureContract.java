package com.ace.member.setting_gesture;

public interface SettingGestureContract {

	interface SettingGestureView{

		void gotoUnlockActivity();

		void gotoLockSetupActivity();

		void showConfirmPwdToastDialog();

		void closeDialog();

		int getActionType();

		void setStatusType(boolean flag);

		void showPasswordConfirm();

		void saveGestureSuccess(String gesture);
	}

	interface SettingGesturePresenter {

		void getGestureConfig();
	}

}
