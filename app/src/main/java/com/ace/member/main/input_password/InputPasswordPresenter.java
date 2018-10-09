package com.ace.member.main.input_password;

import android.content.Context;
import android.support.design.widget.Snackbar;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.TradingPasswordInfo;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Rsa;
import com.og.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

final class InputPasswordPresenter extends BasePresenter implements InputPasswordContract.Presenter {

	private final InputPasswordContract.View mView;
	private TradingPasswordInfo mTradingPasswordInfo;

	@Inject
	public InputPasswordPresenter(Context context, InputPasswordContract.View view) {
		super(context);
		mView = view;
	}

	@Override
	public void start() {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "user");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "checkTradingPasswordStatus");
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					mTradingPasswordInfo = JsonUtil.jsonToBean(result, TradingPasswordInfo.class);
					assert mTradingPasswordInfo != null;
					if (mTradingPasswordInfo.isValid()) {
						mView.showSelf();
					} else {
						Utils.showToast(mTradingPasswordInfo.getRemain() <= 0 ? Utils.getString(R.string.trading_pwd_is_lock) : AppUtils.getMsgByTradingPasswordStatus(mTradingPasswordInfo.getStatus()), Snackbar.LENGTH_LONG, null);
						mView.onCancel();
					}
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}
		});
	}

	@Override
	public void checkTradingPwd(String pwd) {
		mView.setLock(true);
		Map<String, String> params = new HashMap<>();
		params.put("_a", "user");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "checkTradingPassword");
		params.put("pwd", Rsa.encryptByPublic(pwd));
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					mTradingPasswordInfo = JsonUtil.jsonToBean(result, TradingPasswordInfo.class);
					assert mTradingPasswordInfo != null;
					if (mTradingPasswordInfo.isValid()) mView.verifyPasswordSuccess();
					else {
						int status = mTradingPasswordInfo.getStatus();
						int remainTradingCount = mTradingPasswordInfo.getRemain();
						if (status == AppGlobal.TRADING_PASSWORD_STATUS_1_ACTIVE && remainTradingCount > 0) {
							mView.showIncorrectPwdDlg(remainTradingCount);
						} else {
							mView.showTradingPasswordStatusDlg(status, remainTradingCount);
						}
					}
				} catch (Exception e) {
					loadFailure(0, "", "");
				} finally {
					mView.clearPassword();
				}
			}

			@Override
			public void loadFailure(int code, String result, String token) {
				mView.clearPassword();
				mView.verifyPasswordFail();
			}
		});
	}
}
