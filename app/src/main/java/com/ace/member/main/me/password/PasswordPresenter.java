package com.ace.member.main.me.password;


import android.content.Context;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.SingleIntBean;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.Session;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class PasswordPresenter extends BasePresenter implements PasswordContract.PasswordPresenter {

	private final PasswordContract.PasswordView mPasswordView;

	@Inject
	public PasswordPresenter(Context context, PasswordContract.PasswordView mView) {
		super(context);
		this.mPasswordView = mView;
	}

	@Override
	public void checkTradingStatus() {
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
						mPasswordView.checkStatus(tradingStatus, userVerifyStatus, remark);
					} else if (userVerifyStatus == AppGlobal.USER_VERIFY_STATUS_1_PENDING) {
						remark = mContext.getResources().getString(R.string.to_verify_certificate);
						mPasswordView.checkStatus(tradingStatus, userVerifyStatus, remark);
					} else if (userVerifyStatus == AppGlobal.USER_VERIFY_STATUS_2_ACCEPTED && tradingStatus == AppGlobal.TRADING_PASSWORD_STATUS_6_TO_VERIFY_CERTIFICATE) {
						changeTradingStatus(userVerifyStatus, remark);
					} else {
						mPasswordView.checkStatus(tradingStatus, userVerifyStatus, remark);
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
					mPasswordView.checkStatus(status, certificateStatus, remark);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void checkInputPassword(String password) {
		if (password == null || password.isEmpty()) {
			showFailError(mContext.getString(R.string.please_input_login_password), R.string.please_input_login_password);
			return;
		}

		Map<String, String> p = new HashMap<>();
		p.put("_a", "user");
		p.put("_b", "aj");
		p.put("cmd", "confirmPassword");
		p.put("_s", LibSession.sSid);
		p.put("pwd", password);
		submit(p, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					mPasswordView.chkInputPasswordSuccess();
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				showFailError(mContext.getString(R.string.msg_1006), R.string.msg_1006);
			}
		});
	}

	@Override
	public void clearGesturePassword() {
		saveGesture("", AppGlobal.ACTION_TYPE_7_CLOSE_GESTURE);
	}

}
