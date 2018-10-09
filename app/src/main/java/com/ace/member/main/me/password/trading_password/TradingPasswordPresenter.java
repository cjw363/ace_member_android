package com.ace.member.main.me.password.trading_password;

import android.content.Context;
import android.text.TextUtils;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.SingleIntBean;
import com.ace.member.main.me.password.PasswordConfigBean;
import com.ace.member.sms_notification.SMSNotificationActivity;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.M;
import com.ace.member.utils.Session;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;


public class TradingPasswordPresenter extends BasePresenter implements TradingPasswordContract.TradingPasswordPresenter {

	private String mTitle;
	private String mToken;
	private final TradingPasswordContract.TradingPasswordView mTradingPasswordView;

	@Inject
	public TradingPasswordPresenter(Context context, TradingPasswordContract.TradingPasswordView mTradingPasswordView) {
		super(context);
		this.mTradingPasswordView = mTradingPasswordView;
		mTitle = Utils.getString(R.string.reset_password);
	}


	@Override
	public void request(int status) {
		try {
			if (checkData(status)) {
				Map<String, String> params = new HashMap<>();
				params.put("_a", "user");
				params.put("_b", "aj");
				params.put("cmd", "updateTradingPassword");
				params.put("action_type", String.valueOf(status));
				params.put("_s", LibSession.sSid);
				params.put("user_id", String.valueOf(Session.user.getId()));
				params.put("new_password", mTradingPasswordView.getNewPassword());
				params.put("old_password", mTradingPasswordView.getOldPassword());
//				params.put("unique_token", mToken);
				mTradingPasswordView.enableSubmit(false);
				submit(params, new SimpleRequestListener() {
					@Override
					public void loadSuccess(String result, String token) {
						try {

							Utils.showToast(R.string.success);
							JSONObject object = new JSONObject(result);
							Session.user.setStatusTradingPassword(object.getInt("status_trading_password"));
							Timer timer = new Timer();
							timer.schedule(new TimerTask() {
								public void run() {
									mTradingPasswordView.finishActivity();
									this.cancel();
								}
							}, 1000);
						} catch (Exception e) {
							FileUtils.addErrorLog(e);
							e.printStackTrace();
						}
					}

					@Override
					public void loadFailure(int code, String result, String uniqueToken) {
						Utils.showToast(M.get(mContext, code));
						mTradingPasswordView.enableSubmit(true);
					}
				});
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	@Override
	public boolean checkData(int status) {
		try {
			String newPassword = mTradingPasswordView.getNewPassword();
			String confirmPassword = mTradingPasswordView.getConfirmPassword();
			String oldPassword = mTradingPasswordView.getOldPassword();
			int len = newPassword.length();
			if (status == SMSNotificationActivity.ACTION_TYPE_1_RESET_TRADING_PASSWORD) {
				if (TextUtils.isEmpty(oldPassword) || oldPassword.length() != 6 || !oldPassword.matches("[0-9]+")) {
					Utils.showToast(R.string.trading_password_required);
					return false;
				}
			}
			if (TextUtils.isEmpty(newPassword) || len != 6  || !newPassword.matches("[0-9]+")) {

				Utils.showToast(R.string.new_password_required);
				return false;
			}
//			if (newPassword.equals(Session.user.getPhone())) {
//				Utils.showToast(R.string.new_password_invalid);
//				return false;
//			}
			if (TextUtils.isEmpty(confirmPassword)) {
				Utils.showToast(R.string.confirm_password_required);
				return false;
			}
			if (!newPassword.equals(confirmPassword)) {
				Utils.showToast(R.string.invalid_confirm_password);
				return false;
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void addForgotPasswordLog() {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "setting");
		params.put("_b", "aj");
		params.put("cmd", "addForgotPasswordLog");
		params.put("_s", LibSession.sSid);
		submit(params, new SimpleRequestListener() {

			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try{
					checkTradingStatus();
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}, false);
	}

	private void checkTradingStatus() {
		Map<String, String> p = new HashMap<>();
		p.put("_a", "setting");
		p.put("_b", "aj");
		p.put("cmd", "getTradingPasswordConfig");
		p.put("_s", LibSession.sSid);
		p.put("device_id", Session.deviceID);
		p.put("phone", Session.user.getPhone());
		submit(p, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					PasswordConfigBean bean= JsonUtil.jsonToBean(result,PasswordConfigBean.class);
					assert bean != null;
					int userVerifyStatus = bean.getUserVerifyStatus();;
					int tradingStatus = bean.getTradingPasswordStatus();
					String remark = "";
					if (userVerifyStatus == AppGlobal.USER_VERIFY_STATUS_4_REJECTED) {
						remark = bean.getRemark();
						mTradingPasswordView.checkStatus(tradingStatus, userVerifyStatus, remark);
					} else if (userVerifyStatus == AppGlobal.USER_VERIFY_STATUS_1_PENDING) {
						remark = mContext.getResources().getString(R.string.to_verify_certificate);
						mTradingPasswordView.checkStatus(tradingStatus, userVerifyStatus, remark);
					} else if (userVerifyStatus == AppGlobal.USER_VERIFY_STATUS_2_ACCEPTED && tradingStatus == AppGlobal.TRADING_PASSWORD_STATUS_6_TO_VERIFY_CERTIFICATE) {
						changeTradingStatus(userVerifyStatus, remark);
					} else {
						mTradingPasswordView.checkStatus(tradingStatus, userVerifyStatus, remark);
					}
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}
		});
	}

	private void changeTradingStatus(final int certificateStatus, final String remark) {
		Map<String, String> p = new HashMap<>();
		p.put("_a", "setting");
		p.put("_b", "aj");
		p.put("cmd", "changeTradingStatus");
		p.put("_s", LibSession.sSid);
		submit(p, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					SingleIntBean bean=JsonUtil.jsonToBean(result,SingleIntBean.class);
					assert bean != null;
					int status=bean.getValue();
					mTradingPasswordView.checkStatus(status, certificateStatus, remark);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}
		});
	}

}
