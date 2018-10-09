package com.ace.member.sms_notification.second_step;

import android.content.Context;

import com.ace.member.base.BasePresenter;
import com.ace.member.bean.SingleIntBean;
import com.ace.member.bean.SingleStringBean;
import com.ace.member.sms_notification.SMSNotificationActivity;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.M;
import com.ace.member.utils.Session;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

class SecondStepPresenter extends BasePresenter implements SecondStepContract.SecondStepPresenter {

	private final SecondStepContract.SecondStepView mView;

	@Inject
	SecondStepPresenter(SecondStepContract.SecondStepView view, Context context) {
		super(context);
		mView = view;
	}

	//发送验证码到手机
	@Override
	public void sendVerificationCode(String phone, int smsType, int actionType) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "user");
		params.put("_b", "aj");
		params.put("cmd", "sendVerificationCode");
		params.put("phone", phone);
		params.put("type", String.valueOf(smsType));
		params.put("action_type", String.valueOf(actionType));
		params.put("device_id", Session.deviceID);
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String time) {
				try {
					SingleIntBean bean = JsonUtil.jsonToBean(result, SingleIntBean.class);
					assert bean != null;
					mView.setResendTime(bean.getValue());
					mView.startClock();
				} catch (Exception e) {
					mView.showResendTimeError();
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}

			@Override
			public void loadFailure(int code, String result, String uniqueToken) {
				if (code == com.og.M.MessageCode.ERR_505_FUNCTION_NOT_RUNNING) {
					mView.toLogin(AppUtils.getFunctionPauseMsg(M.FunctionCode.FUNCTION_100_MEMBER_REGISTER));
				} else {
					String msg = M.get(mContext, code);
					mView.showResendTimeError();
					mView.showToast(msg);
				}
			}
		});
	}

	@Override
	public void register(String code, String fullPhone) {
		verifyCode(code, fullPhone, SMSNotificationActivity.SMS_TYPE_1_REGISTER, SMSNotificationActivity.ACTION_TYPE_5_REGISTER);
	}

	@Override
	public void resetPassword(String code, String fullPhone) {
		verifyCode(code, fullPhone, SMSNotificationActivity.SMS_TYPE_2_PASSWORD, SMSNotificationActivity.ACTION_TYPE_2_FORGOT_PASSWORD);
	}

	@Override
	public void resetStatus(String code, String fullPhone) {
		verifyCode(code, fullPhone, SMSNotificationActivity.SMS_TYPE_2_PASSWORD, SMSNotificationActivity.ACTION_TYPE_4_TO_SET_TRADING_PASSWORD);
	}

	private void verifyCode(String code, String fullPhone, final int smsType, final int actionType) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "user");
		params.put("_b", "aj");
		params.put("cmd", "verifyCode");
		params.put("code", code);
		params.put("phone", fullPhone);
		params.put("type", String.valueOf(smsType));
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					if (actionType == SMSNotificationActivity.ACTION_TYPE_2_FORGOT_PASSWORD) {
						SingleStringBean bean = JsonUtil.jsonToBean(result, SingleStringBean.class);
						assert bean != null;
						Session.verificationUserID = bean.getValue();
						mView.toNextStep();
					} else if (actionType == SMSNotificationActivity.ACTION_TYPE_4_TO_SET_TRADING_PASSWORD) {
						mView.toTradingPassword();
					} else if (actionType == SMSNotificationActivity.ACTION_TYPE_5_REGISTER) {
						mView.toNextStep();
					}
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				mView.showFailure();
			}
		});
	}
}
