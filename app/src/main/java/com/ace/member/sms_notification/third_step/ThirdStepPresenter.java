package com.ace.member.sms_notification.third_step;

import android.content.Context;
import android.text.TextUtils;

import com.ace.member.R;
import com.ace.member.base.LoginBasePresenter;
import com.ace.member.bean.SingleBooleanBean;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.M;
import com.ace.member.utils.Session;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

class ThirdStepPresenter extends LoginBasePresenter implements ThirdStepContract.ThirdStepPresenter {

	private final ThirdStepContract.ThirdStepView mView;

	@Inject
	ThirdStepPresenter(ThirdStepContract.ThirdStepView view, Context context) {
		super(context);
		mView = view;
	}

	public void checkRegisterFunction() {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "system");
		params.put("_b", "aj");
		params.put("cmd", "checkIsRunningByCode");
		params.put("code", String.valueOf(M.FunctionCode.FUNCTION_100_MEMBER_REGISTER));
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				SingleBooleanBean bean = JsonUtil.jsonToBean(result, SingleBooleanBean.class);
				assert bean != null;
				if (!bean.getValue()) {
					mView.toLogin(AppUtils.getFunctionPauseMsg(M.FunctionCode.FUNCTION_100_MEMBER_REGISTER));
				}
			}
		}, false);
	}

	@Override
	public void register(Map<String, String> params) {
		if (checkData()) {
			params.put("_b", "aj");
			params.put("_a", "user");
			params.put("cmd", "register");
			submit(params, new SimpleRequestListener() {
				@Override
				public void loadSuccess(String result, String token) {
					try {
						JSONObject object = new JSONObject(result);
						initSession(object);
						mView.getPreferencesGesture()
							.edit()
							.putString(AppGlobal.USER_PHONE, Session.user.getPhone())
							.putString(AppGlobal.PREFERENCES_GESTURE_USER, Session.user.getGesturePassword())
							.apply();
						mView.toMainActivity();
						clearFingerprintInfo();
					} catch (Exception e) {
						FileUtils.addErrorLog(e);
					}
				}

				@Override
				public void loadFailure(int code, String result, String uniqueToken) {
					String msg;
					if (code == com.og.M.MessageCode.ERR_505_FUNCTION_NOT_RUNNING) {
						msg = AppUtils.getFunctionPauseMsg(M.FunctionCode.FUNCTION_100_MEMBER_REGISTER);
					} else {
						msg = M.get(mContext, code);
					}
					if (TextUtils.isEmpty(msg)) {
						msg = mContext.getResources()
							.getString(R.string.msg_1006);
					}
					mView.showToast(msg);
				}
			});
		}
	}

	@Override
	public void resetPassword(Map<String, String> params) {
		if (checkData()) {
			params.put("_b", "aj");
			params.put("_a", "user");
			params.put("cmd", "resetPassword");
			params.put("user_id", Session.verificationUserID);

			submit(params, new SimpleRequestListener() {
				@Override
				public void loadSuccess(String result, String token) {
					try {
						JSONObject object = new JSONObject(result);
						initSession(object);
						onSuccess(Session.user.getGesturePassword(), Session.user.getResetPassword());
					} catch (Exception e) {
						FileUtils.addErrorLog(e);
						e.printStackTrace();
					}
				}

				@Override
				public void loadFailure(int code, String result, String uniqueToken) {
					String msg = M.get(mContext, code);
					if (TextUtils.isEmpty(msg)) {
						msg = mContext.getResources()
							.getString(R.string.error);
					}
					mView.showToast(msg);
				}
			});
		}
	}

	@Override
	public boolean checkData() {
		try {
			String password = mView.getPassword();
			String confirmPassword = mView.getConfirmPassword();
			if (TextUtils.isEmpty(password)) {
				mView.showToast(Utils.getString(R.string.new_password_required));
				return false;
			}
			if (password.equals(Session.verificationPhone)) {
				mView.showToast(Utils.getString(R.string.msg_1006));
				return false;
			}
			if (TextUtils.isEmpty(confirmPassword)) {
				mView.showToast(Utils.getString(R.string.confirm_password_required));
				return false;
			}
			if (!password.equals(confirmPassword)) {
				mView.showToast(Utils.getString(R.string.invalid_confirm_password));
				return false;
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
		return true;
	}

	@Override
	protected void onSuccess(String gesture, boolean resetPassword) {
		//清除手势密码指纹登录
		mView.clearFingerprintAndGesture();
		mView.toMainActivity();
	}

	private void clearFingerprintInfo() {
		mView.clearFingerprintAndGesture();
	}

}
