package com.ace.member.main.me.password;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.login_password.LoginPasswordActivity;
import com.ace.member.main.input_password.InputPasswordCallback;
import com.ace.member.main.me.password.trading_password.TradingPasswordActivity;
import com.ace.member.setting_gesture.SettingGestureActivity;
import com.ace.member.sms_notification.SMSNotificationActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.InputPasswordUtil;
import com.ace.member.utils.Session;
import com.ace.member.utils.SnackBarUtil;
import com.og.utils.CustomDialog;
import com.og.utils.FileUtils;
import com.og.utils.FingerprintDialog;
import com.og.utils.FingerprintHelper;
import com.og.utils.Utils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class PasswordActivity extends BaseActivity implements PasswordContract.PasswordView {

	@Inject
	PasswordPresenter mPasswordPresenter;
	@BindView(R.id.btn_login_password)
	LinearLayout mBtnLoginPassword;
	@BindView(R.id.btn_trading_password)
	LinearLayout mBtnTradingPassword;
	@BindView(R.id.btn_gesture_password)
	LinearLayout mBtnGesturePassword;
	@BindView(R.id.sw_setting_fingerprint)
	SwitchCompat mSwFingerprint;

	public static final int ACTION_TYPE_1_FROM_HOME = 1;
	public int mActionType = 0;
	public CustomDialog mChkPasswordDialog;
	private InputLoginPasswordFragment fragment;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerPasswordComponent.builder()
			.passwordPresenterModule(new PasswordPresenterModule(this, this))
			.build()
			.inject(this);
		initView();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_password;
	}

	private void initView() {
		ToolBarConfig.builder(this, null).setBackListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		}).setTvTitleRes(R.string.password).build();

		Bundle b = getIntent().getExtras();
		if (b != null) {mActionType = b.getInt("action_type");}

		initFingerprintLoginView();
		initFingerprintPayView();
	}

	private void checkSettingFingerPrintLogin() {
		if (mActionType == ACTION_TYPE_1_FROM_HOME) {
			mSwFingerprint.setChecked(true);
		}
	}

	private void initFingerprintPayView() {
		final SwitchCompat swFingerprintPay = (SwitchCompat) findViewById(R.id.sw_setting_fingerprint_pay);
		final FingerprintHelper PayHelper = new FingerprintHelper.Pay(this);
		if (PayHelper.getIsUseFingerprint()) swFingerprintPay.setChecked(true);
		else swFingerprintPay.setChecked(false);

		swFingerprintPay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				settingFingerprintPay(isChecked);
			}

			private void configureFingerprint() {
				PayHelper.saveFingerprintData(Session.user.getPhone());
				PayHelper.setIsUseFingerprint(true);
				mPasswordPresenter.addFingerprintLog(AppGlobal.ACTION_TYPE_9_FINGER_PAY_ON);
				//PayHelper.test();
				Utils.showToast(R.string.success);
			}

			private void clearFingerprint() {
				swFingerprintPay.setChecked(false);
				if (PayHelper.getIsUseFingerprint()) {
					mPasswordPresenter.addFingerprintLog(AppGlobal.ACTION_TYPE_10_FINGER_PAY_OFF);
				}
				PayHelper.setIsUseFingerprint(false);
				PayHelper.clearData();
				//Utils.showToast(R.string.msg_switch_on_failure);
			}

			private void settingFingerprintPay(boolean isChecked) {
				if (isChecked && PayHelper.checkAvailable(true)) {
					InputPasswordUtil.inputPassword(PasswordActivity.this, new InputPasswordCallback.SimpleInputPasswordCallbackListener() {
						@Override
						public void onSuccess() {
							showFingerprintPayDlg();
							//configureFingerprint();
						}

						@Override
						public void onFail() {
							clearFingerprint();
						}

						@Override
						public void onCancel() {
							clearFingerprint();
						}

						@Override
						public void onLock() {
							clearFingerprint();
						}
					});
				} else {
					clearFingerprint();
				}
			}

			private void showFingerprintPayDlg() {
				final FingerprintDialog dialog = FingerprintDialog.newInstance(PasswordActivity.this);
				dialog.setCancelable(false);
				dialog.show(getSupportFragmentManager(), "dialog_pay");
				dialog.startAuth(new FingerprintDialog.PartAuthResult() {
					@Override
					public void cancelFingerprint() {
						dialog.dismiss();
						clearFingerprint();
					}

					@Override
					public void authSuccess() {
						dialog.dismiss();
						configureFingerprint();
					}

					@Override
					public void authFail() {
						dialog.dismiss();
						clearFingerprint();
					}
				});
			}
		});
	}


	private void initFingerprintLoginView() {
		//helper = new FingerprintHelper(this,FingerprintHelper.PRE_LOGIN_KEY,FingerprintHelper.KEY_STORE_LOGIN_KEY);
		final FingerprintHelper loginHelper = new FingerprintHelper.Login(this);
		if (loginHelper.getIsUseFingerprint()) mSwFingerprint.setChecked(true);
		else mSwFingerprint.setChecked(false);

		mSwFingerprint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked && loginHelper.checkAvailable(true)) {
					if (Session.flagUseGesturePwd) dialogTip();
					else dialogPassword();
				} else {
					setOffFingerprintLogin();
				}
			}
		});
	}

	public void setOffFingerprintLogin() {
		FingerprintHelper LoginHelper = new FingerprintHelper.Login(this);
		mSwFingerprint.setChecked(false);
		if (LoginHelper.getIsUseFingerprint()) {
			mPasswordPresenter.addFingerprintLog(AppGlobal.ACTION_TYPE_3_SWITCH_OFF);
		}
		LoginHelper.setIsUseFingerprint(false);
		LoginHelper.clearData();
		if (mActionType == ACTION_TYPE_1_FROM_HOME) {
			finish();
		}
	}

	private void showFingerprintLoginDlg() {
		final FingerprintHelper LoginHelper = new FingerprintHelper.Login(this);
		final FingerprintDialog dialog = FingerprintDialog.newInstance(PasswordActivity.this);
		dialog.setCancelable(false);
		dialog.show(getSupportFragmentManager(), "dialog");
		dialog.startAuth(new FingerprintDialog.PartAuthResult() {
			@Override
			public void cancelFingerprint() {
				dialog.dismiss();
				mSwFingerprint.setChecked(false);
				if (LoginHelper.getIsUseFingerprint()) {
					mPasswordPresenter.addFingerprintLog(AppGlobal.ACTION_TYPE_3_SWITCH_OFF);
				}
				LoginHelper.setIsUseFingerprint(false);
				LoginHelper.clearData();
				if (mActionType == ACTION_TYPE_1_FROM_HOME) {
					finish();
				}
			}

			@Override
			public void authSuccess() {
				dialog.dismiss();
				mPreferencesUser.edit().putString(AppGlobal.PREFERENCES_GESTURE_USER, "").apply();
				//				mPreferencesGesture.edit().clear().apply();
				Session.flagUseGesturePwd = false;
				mPasswordPresenter.clearGesturePassword();
				LoginHelper.saveFingerprintData(Session.user.getPhone());
				LoginHelper.setIsUseFingerprint(true);
				mPasswordPresenter.addFingerprintLog(AppGlobal.ACTION_TYPE_2_SWITCH_ON);
				//LoginHelper.test();
				Utils.showToast(R.string.success);

				if (mActionType == ACTION_TYPE_1_FROM_HOME) {
					finish();
				}
			}

			@Override
			public void authFail() {
				dialog.dismiss();
				setOffFingerprintLogin();
				Utils.showToast(R.string.fail);
			}
		});
	}

	public void dialogTip() {
		Dialog mDialog = new CustomDialog.Builder(PasswordActivity.this).setMessage(R.string.confirm_to_use_fingerprint)
			.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					dialogInterface.dismiss();
					dialogPassword();
				}
			})
			.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					dialogInterface.dismiss();
					setOffFingerprintLogin();
				}
			})
			.create();
		mDialog.setCancelable(false);
		//		if (mDialog.getWindow() != null) {
		//			mDialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
		//		}
		mDialog.show();
	}


	public void dialogPassword() {
		fragment = new InputLoginPasswordFragment();
		fragment.setCancelable(false);
		fragment.show(getSupportFragmentManager(), "InputLogin", new InputLoginPasswordFragment.Callback() {
			@Override
			public void close() {
				fragment.dismiss();
				setOffFingerprintLogin();
			}

			@Override
			public void confirm() {
				mPasswordPresenter.checkInputPassword(fragment.getPassword());
			}

			@Override
			public void forgotPassword() {
				Intent intent = new Intent(PasswordActivity.this, SMSNotificationActivity.class);
				intent.putExtra("action_type", SMSNotificationActivity.ACTION_TYPE_2_FORGOT_PASSWORD);
				intent.putExtra("source_page", SMSNotificationActivity.FROM_PASSWORD_SETTING);
				startActivity(intent);
			}
		});

	}

	@Override
	public void chkInputPasswordSuccess() {
		fragment.dismiss();
		showFingerprintLoginDlg();
	}

	@OnClick({R.id.btn_login_password, R.id.btn_trading_password, R.id.btn_gesture_password})
	public void onViewClicked(View view) {
		Intent it;
		switch (view.getId()) {
			case R.id.btn_login_password:
				it = new Intent(PasswordActivity.this, LoginPasswordActivity.class);
				it.putExtra("activity", AppGlobal.ACTIVITY_16_UPDATE_PASSWORD);
				startActivity(it);
				break;
			case R.id.btn_trading_password:
				mPasswordPresenter.checkTradingStatus();
				break;
			case R.id.btn_gesture_password:
				it = new Intent(PasswordActivity.this, SettingGestureActivity.class);
				startActivity(it);
				break;
		}
	}

	@Override
	public void checkStatus(int tradingStatus, int certificateStatus, String reason) {
		Intent it;
		if (tradingStatus == AppGlobal.TRADING_PASSWORD_STATUS_1_ACTIVE) {
			it = new Intent(PasswordActivity.this, TradingPasswordActivity.class);
			it.putExtra("action_type", SMSNotificationActivity.ACTION_TYPE_1_RESET_TRADING_PASSWORD);
			it.putExtra("trading_status", tradingStatus);
			//			it.putExtra("verified", VERIFY_0_NO);
			startActivity(it);
		} else if (tradingStatus == AppGlobal.TRADING_PASSWORD_STATUS_3_INACTIVE) {
			showCloseDialog(R.string.invalid_trading_password);
		} else if (tradingStatus == AppGlobal.TRADING_PASSWORD_STATUS_4_TO_SET) {
			it = new Intent(this, SMSNotificationActivity.class);
			it.putExtra("action_type", SMSNotificationActivity.ACTION_TYPE_4_TO_SET_TRADING_PASSWORD);
			it.putExtra("countryCode", String.valueOf(AppGlobal.COUNTRY_CODE_855_CAMBODIA));
			startActivity(it);
		} else if (tradingStatus == AppGlobal.TRADING_PASSWORD_STATUS_6_TO_VERIFY_CERTIFICATE) {
			it = new Intent(PasswordActivity.this, TradingPasswordActivity.class);
			it.putExtra("action_type", SMSNotificationActivity.ACTION_TYPE_6_TO_VERITY_CERTIFICATE);
			it.putExtra("trading_status", tradingStatus);
			it.putExtra("certificate_status", certificateStatus);
			it.putExtra("reason", reason);
			startActivity(it);
		}
	}

	private void showCloseDialog(int msg) {
		try {
			Dialog mDialog = new CustomDialog.Builder(mContext).setMessage(msg)
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						dialogInterface.dismiss();
					}
				})
				.create();
			mDialog.setCancelable(false);
			mDialog.show();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public void showCheckPasswordDialog(int msg) {
		SnackBarUtil.show(getWindow().findViewById(android.R.id.content), msg);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onResume() {
		initFingerprintLoginView();
		initFingerprintPayView();
		checkSettingFingerPrintLogin();
		super.onResume();
	}
}


