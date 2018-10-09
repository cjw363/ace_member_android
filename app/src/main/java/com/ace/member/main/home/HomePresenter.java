package com.ace.member.main.home;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.Balance;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.Session;
import com.google.gson.reflect.TypeToken;
import com.og.LibGlobal;
import com.og.LibSession;
import com.og.event.MessageEvent;
import com.og.http.SimpleRequestListener;
import com.og.utils.CustomDialog;
import com.og.utils.DateUtils;
import com.og.utils.FileUtils;
import com.og.utils.FingerprintHelper;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static android.content.Context.MODE_PRIVATE;

public class HomePresenter extends BasePresenter implements HomeContract.HomePresenter {
	private final HomeContract.HomeView mHomeView;
	private SharedPreferences mSharedPreferences;

	@Inject
	public HomePresenter(HomeContract.HomeView view, Context context) {
		super(context);
		mHomeView = view;
		mSharedPreferences = context.getSharedPreferences(LibGlobal.PRE_DATA_KEY, MODE_PRIVATE);
	}

	@Override
	public void getBalance(final boolean isRefresh) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "balance");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getBalance");

		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				if (isRefresh) {
					mHomeView.showRefreshStatus(false);
					Utils.showToast(R.string.success);
				}
				try {
					Session.balanceList = JsonUtil.jsonToList(result, new TypeToken<List<Balance>>() {});
					mHomeView.setBalance();
				} catch (Exception e) {
					e.printStackTrace();
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int code, String result, String token) {
				if (isRefresh) {
					mHomeView.showRefreshStatus(false);
					Utils.showToast(R.string.fail);
				}
			}
		}, false);
	}

	@Override
	public void doOtherDialogSetting() {
		try {
			String today = DateUtils.getToday();
			String fingerprintData = "", gestureData = "", tradingPasswordData = "";
			int hintCount = 1;
			if (mSharedPreferences != null && Session.user != null) {
				fingerprintData = mSharedPreferences.getString(Session.user.getPhone() + AppGlobal.FINGERPRINT, "");
				gestureData = mSharedPreferences.getString(Session.user.getPhone() + AppGlobal.GESTURE, "");
				tradingPasswordData = mSharedPreferences.getString(Session.user.getPhone() + AppGlobal.TRADING_PASSWORD, "");
				hintCount = mSharedPreferences.getInt(Session.user.getPhone() + AppGlobal.HINT_COUNT, 1);
			}
			FingerprintHelper loginHelper = new FingerprintHelper.Login(mContext);
			if (hintCount > AppGlobal.SETTING_HINT_DAYS) return;//一些关键设置如交易密码，若没有设置，则在前三天打开应用的时候提示

			if (!loginHelper.getIsUseFingerprint() && loginHelper.checkAvailable(false) && !today.equals(fingerprintData)) {//指纹提示
				dialogFingerprintLogin();
			} else if (!loginHelper.getIsUseFingerprint() & !Session.flagUseGesturePwd && !today.equals(gestureData)) {////手势提示
				dialogGesturePassword();
			} else if (Session.user.getStatusTradingPassword() == AppGlobal.TRADING_PASSWORD_STATUS_4_TO_SET && !today
				.equals(tradingPasswordData)) {//交易密码提示
				confirmToVerify();
			}
			if (mSharedPreferences != null && Session.user != null) {
				mSharedPreferences.edit()
					.putInt(Session.user.getPhone() + AppGlobal.HINT_COUNT, hintCount + 1)
					.apply();
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public void onMessageEvent(MessageEvent messageEvent) {
		int code = messageEvent.getCode();
		switch (code) {
			case HomeFragment.MSG_FINISH_SETTING_FINGERPRINT_LOGIN:
				try {
					FingerprintHelper loginHelper = new FingerprintHelper.Login(mContext);
					if (!loginHelper.getIsUseFingerprint() && !Session.flagUseGesturePwd)
						dialogGesturePassword();
					else {
						if (Session.user.getStatusTradingPassword() == 4) {
							confirmToVerify();
						}
					}
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
				break;
			case HomeFragment.MSG_FINISH_SETTING_GESTURE:
				if (Session.user.getStatusTradingPassword() == 4) {
					confirmToVerify();
				}
				break;
		}
	}

	private void dialogFingerprintLogin() {
		Dialog mDialog = new CustomDialog.Builder(mContext).setMessage(R.string.ask_switch_on_fingerprint_login)
			.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(final DialogInterface dialog, int which) {
					dialog.dismiss();
					mSharedPreferences.edit()
						.putString(Session.user.getPhone() + AppGlobal.FINGERPRINT, DateUtils.getToday())
						.apply();
					mHomeView.toPasswordActivity();
				}
			})
			.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mSharedPreferences.edit()
						.putString(Session.user.getPhone() + AppGlobal.FINGERPRINT, DateUtils.getToday())
						.apply();
					dialog.dismiss();
					doOtherDialogSetting();
				}
			})
			.create();
		mDialog.setCancelable(false);
		//		if (mDialog.getWindow() != null) {
		//			mDialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
		//		}
		mDialog.show();
	}

	private void dialogGesturePassword() {
		try {
			CustomDialog.Builder builder = new CustomDialog.Builder(mContext, R.layout.dlg_gesture_layout);
			builder.setMessage(mContext.getResources().getString(R.string.confirm_to_set_gesture_password));
			builder.setPositiveButton(com.og.R.string.yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					if (Utils.isFastClick(mContext)) return;
					dialog.dismiss();
					mSharedPreferences.edit()
						.putString(Session.user.getPhone() + AppGlobal.GESTURE, DateUtils.getToday())
						.apply();
					mHomeView.toLockSetupActivity();
				}
			});
			builder.setNegativeButton(com.og.R.string.no, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					mSharedPreferences.edit()
						.putString(Session.user.getPhone() + AppGlobal.GESTURE, DateUtils.getToday())
						.apply();
					doOtherDialogSetting();
				}
			});
			CustomDialog dialog = builder.create();
			dialog.setCancelable(false);
			dialog.show();
		} catch (Resources.NotFoundException e) {
			FileUtils.addErrorLog(e);
		}
	}

	private void confirmToVerify() {
		try {
			Dialog mDialog = new CustomDialog.Builder(mContext).setMessage(R.string.to_set_trading)
				.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						mSharedPreferences.edit()
							.putString(Session.user.getPhone() + AppGlobal.TRADING_PASSWORD, DateUtils.getToday())
							.apply();
						mHomeView.toSMSNotificationActivity();
					}
				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						mSharedPreferences.edit()
							.putString(Session.user.getPhone() + AppGlobal.TRADING_PASSWORD, DateUtils.getToday())
							.apply();
					}
				})
				.create();
			mDialog.setCancelable(false);
			mDialog.show();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

}
