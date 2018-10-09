package com.ace.member.sms_notification.first_step;

import android.content.Context;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.CountryCodeBean;
import com.ace.member.bean.SingleIntBean;
import com.ace.member.sms_notification.SMSNotificationActivity;
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

class FirstStepPresenter extends BasePresenter implements FirstStepContract.FirstStepPresenter {

	private final FirstStepContract.FirstStepView mView;

	@Inject
	FirstStepPresenter(FirstStepContract.FirstStepView view, Context context) {
		super(context);
		mView = view;
	}

	@Override
	public void getConfig(final int actionType) {
		try {
			Map<String, String> params = new HashMap<>();
			params.put("_a", "user");
			params.put("_b", "aj");
			params.put("cmd", "getSmsConfig");
			params.put("action_type", actionType + "");
			submit(params, new SimpleRequestListener() {
				@Override
				public void loadSuccess(String result, String token) {
					try {
						FirstStepData firstStepData = JsonUtil.jsonToBean(result, FirstStepData.class);
						assert firstStepData != null;
						CountryCodeBean countryCodeBean = new CountryCodeBean(firstStepData.getCountryCodeRowData());
						mView.initCountryCodeList(countryCodeBean.getCountryCodeList());
						if (actionType == SMSNotificationActivity.ACTION_TYPE_5_REGISTER) {
							boolean isRunning = firstStepData.isRegisterIsRunning();
							if (!isRunning) {
								mView.toLogin(AppUtils.getFunctionPauseMsg(M.FunctionCode.FUNCTION_100_MEMBER_REGISTER));
							}
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

	public void checkPhone(String countryCode, String phone, final int smsType) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "user");
		params.put("_b", "aj");
		params.put("cmd", "checkPhone");
		params.put("phone", countryCode + "-" + phone);
		params.put("device_id", Session.deviceID);
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					SingleIntBean bean = JsonUtil.jsonToBean(result, SingleIntBean.class);
					assert bean != null;
					int phoneType = bean.getValue();
					mView.saveVerification();
					if (smsType == SMSNotificationActivity.SMS_TYPE_1_REGISTER) {
						checkRegister(phoneType);
					} else {
						checkForgotPassword(phoneType);
					}
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int code, String result, String uniqueToken) {
				mView.showToast(M.get(mContext, code));
			}
		});
	}

	private void checkRegister(int type) {
		if (type == AppGlobal.PHONE_TYPE_0_NOT_REGISTER) {
			mView.toNextStep();
		} else {
			mView.showToast(Utils.getString(R.string.duplicated));
		}
	}

	private void checkForgotPassword(int type) {
		if (type == AppGlobal.PHONE_TYPE_1_MEMBER) {
			mView.toNextStep();
		} else {
			mView.showToast(Utils.getString(R.string.invalid_phone));
		}
	}
}
