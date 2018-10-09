package com.ace.member.login;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;

import com.ace.member.R;
import com.ace.member.base.LoginBasePresenter;
import com.ace.member.bean.CountryCodeBean;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.M;
import com.ace.member.utils.Session;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

final class LoginPresenter extends LoginBasePresenter implements LoginContract.LoginPresenter {

	private final LoginContract.LoginView mLoginView;
	private Context mContext;

	@Inject
	LoginPresenter(Context context, LoginContract.LoginView mLoginView) {
		super(context);
		this.mContext = context;
		this.mLoginView = mLoginView;
	}

	@Override
	protected void onSuccess(String gesture, boolean resetPassword) {
		try {
			if (resetPassword) {
				mLoginView.toResetPasswordActivity();
			} else {
				if (mLoginView.getActionType() == LoginActivity.ACTION_TYPE_2_FORGOT_GESTURE) {
					mLoginView.toLockSetupActivity();
				} else {
					if (!TextUtils.isEmpty(gesture)) {
						Session.flagUseGesturePwd = true;
						mLoginView.getPreferencesUser()
							.edit()
							.putString(AppGlobal.USER_PHONE, Session.user.getPhone())
							.putString(AppGlobal.PREFERENCES_GESTURE_USER, gesture)
							.apply();
					}
					//记录登录时的账号
					mLoginView.getPreferencesUser()
						.edit()
						.putString(AppGlobal.LOGIN_USER_PHONE, Session.user.getPhone())
						.apply();
					mLoginView.toMainActivity();
				}
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	@Override
	protected void onFailure(int code) {
		try {
			if (code == M.MessageCode.ERR_1001_LOGGED_ON_OTHER_DEVICE) {
				mLoginView.showPrompt();
			} else if (code > 0) {
				showFailError(M.get(mContext, code), M.MessageCode.ERR_1002_LOGIN_FAIL);
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	protected void againLogin() {
		mLoginView.setAgainLogin();
	}

	@Override
	public void getLoginData() {
		try {
			Map<String, String> params = new HashMap<>();
			params.put("_a", "system");
			params.put("_b", "aj");
			params.put("cmd", "getLoginData");
			submit(params, new SimpleRequestListener() {
				@Override
				public void loadSuccess(String result, String token) {
					try {
						LoginData loginData = JsonUtil.jsonToBean(result, LoginData.class);
						assert loginData != null;
						CountryCodeBean countryCodeBean = new CountryCodeBean(loginData.getCountryCodeRowData());
						mLoginView.initCountryCodeList(countryCodeBean.getCountryCodeList());
						mLoginView.showRegister(loginData.getRegisterIsRunning());
						boolean isLoginRunning = loginData.getLoginIsRunning();
						if (!isLoginRunning){
							Utils.showToast(AppUtils.getFunctionPauseMsg(M.FunctionCode.FUNCTION_102_MEMBER_LOGIN_ANDROID), Snackbar.LENGTH_LONG);
						}
					} catch (Exception e) {
						FileUtils.addErrorLog(e);
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}
}
