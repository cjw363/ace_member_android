package com.ace.member.gesture_unlock;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.ace.member.base.LoginBasePresenter;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.M;
import com.ace.member.utils.Session;
import com.og.utils.FileUtils;

import javax.inject.Inject;

public class UnlockPresenter extends LoginBasePresenter implements UnlockContract.UnlockPresenter {
	@NonNull
	private final UnlockContract.UnlockView mUnlockView;

	@Inject
	UnlockPresenter(@NonNull UnlockContract.UnlockView unlockView, Context context) {
		super(context);
		mUnlockView = unlockView;
	}

	protected void onSuccess(String gesture, boolean resetPassword) {
		try {
			if (!TextUtils.isEmpty(gesture)) {
				Session.flagUseGesturePwd = true;
				mUnlockView.getPreferencesUser()
					.edit()
					.putString(AppGlobal.USER_PHONE, Session.user.getPhone())
					.putString(AppGlobal.PREFERENCES_GESTURE_USER, gesture)
					.apply();
			}
			//记录登录账号
			mUnlockView.getPreferencesUser()
				.edit()
				.putString(AppGlobal.LOGIN_USER_PHONE, Session.user.getPhone())
				.apply();
			mUnlockView.toMainActivity();

		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	protected void againLogin() {
		mUnlockView.selAgainLogin();
	}

	@Override
	protected void onFailure(int code) {
		try {
			if (code == M.MessageCode.ERR_1004_GESTURE_EXPIRED) {
				showFailError(M.get(mContext, code), M.MessageCode.ERR_1003_GESTURE_LOGIN_FAIL);
			} else if (code == M.MessageCode.ERR_1001_LOGGED_ON_OTHER_DEVICE) {
				mUnlockView.showPrompt();
			} else if (code == com.og.M.MessageCode.ERR_505_FUNCTION_NOT_RUNNING) {
				showFailError(AppUtils.getFunctionPauseMsg(M.FunctionCode.FUNCTION_102_MEMBER_LOGIN_ANDROID), M.MessageCode.ERR_1002_LOGIN_FAIL);
			} else if (code > 0) {
				showFailError(M.get(mContext, code), M.MessageCode.ERR_1002_LOGIN_FAIL);
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}


}
