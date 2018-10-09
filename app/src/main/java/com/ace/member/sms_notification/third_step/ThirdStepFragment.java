package com.ace.member.sms_notification.third_step;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ace.member.BuildConfig;
import com.ace.member.R;
import com.ace.member.base.BaseFragment;
import com.ace.member.listener.NoDoubleClickListener;
import com.ace.member.login.LoginActivity;
import com.ace.member.main.MainActivity;
import com.ace.member.simple_listener.SimpleTextWatcher;
import com.ace.member.sms_notification.SMSNotificationActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.SPUtil;
import com.ace.member.utils.Session;
import com.ace.member.utils.SnackBarUtil;
import com.og.LibGlobal;
import com.og.LibSession;
import com.og.utils.AllCapTransformationMethod;
import com.og.utils.CustomDialog;
import com.og.utils.FileUtils;
import com.og.utils.FingerprintHelper;
import com.og.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;

import static android.content.Context.MODE_PRIVATE;

public class ThirdStepFragment extends BaseFragment implements ThirdStepContract.ThirdStepView {

	@Inject
	ThirdStepPresenter mPresenter;

	@BindView(R.id.ll_root)
	LinearLayout mLlRoot;
	@BindView(R.id.et_user_name)
	EditText mEtUserName;
	@BindView(R.id.et_password)
	EditText mEtPassword;
	@BindView(R.id.cb_password)
	CheckBox mCbPassword;
	@BindView(R.id.et_pwd_confirm)
	EditText mEtPwdConfirm;
	@BindView(R.id.cb_pwd_confirm)
	CheckBox mCbPwdConfirm;
	@BindView(R.id.ll_user_name)
	LinearLayout mLlUserName;
	@BindView(R.id.btn_submit)
	Button mBtnSubmit;

	private SMSNotificationActivity mActivity;


	@Override
	protected int getContentViewLayout() {
		return R.layout.fragment_notification_third_step;
	}

	@Override
	protected void initView() {
		DaggerThirdStepComponent.builder()
			.thirdStepPresenterModule(new ThirdStepPresenterModule(this, getContext()))
			.build()
			.inject(this);

		mActivity = (SMSNotificationActivity) getActivity();
		int title = R.string.register;
		if (mActivity.mActionType == SMSNotificationActivity.ACTION_TYPE_2_FORGOT_PASSWORD) {
			title = R.string.reset_password;
		}

		ToolBarConfig.builder(null, getView())
			.setTvTitleRes(title)
			.setEnableBack(true)
			.setBackListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					CustomDialog.Builder builder = new CustomDialog.Builder(mActivity);
					builder.setMessage(R.string.give_up_password_reset)
						.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								if (Utils.isFastClick(mActivity)) return;
								toLoginActivity();
								dialog.dismiss();
							}
						})
						.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});
					CustomDialog mCurrentDlg = builder.create();
					mCurrentDlg.setCancelable(false);
					mCurrentDlg.show();
				}
			})
			.build();

		setListener();

		if (mActivity.mActionType == SMSNotificationActivity.SMS_TYPE_1_REGISTER) {
			Utils.showKeyboard(mEtUserName);
		} else if (mActivity.mActionType == SMSNotificationActivity.ACTION_TYPE_2_FORGOT_PASSWORD) {
			Utils.showKeyboard(mEtPassword);
		}
	}

	@Override
	public void flashData() {
		if (mActivity.mActionType == SMSNotificationActivity.ACTION_TYPE_5_REGISTER) {
			mPresenter.checkRegisterFunction();
		}
		clear();
		if (mActivity.mActionType == SMSNotificationActivity.ACTION_TYPE_2_FORGOT_PASSWORD) {
			mLlUserName.setVisibility(View.GONE);
		}
	}

	private void setListener() {
		mEtUserName.setTransformationMethod(new AllCapTransformationMethod());

		mEtUserName.addTextChangedListener(new SimpleTextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.toString().indexOf(" ") == 0) {
					mEtUserName.setText(s.toString().substring(1));
				}

				if (s.toString().contains("  ")) {
					mEtUserName.setText(s.toString().replace("  ", " "));
					mEtUserName.setSelection(mEtUserName.getText().toString().length());
				}
			}
		});

		mCbPassword.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (((CheckBox) view).isChecked()) {
					mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				} else {
					mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
				if (mEtPassword.hasFocus()) {
					String password = mEtPassword.getText().toString();
					mEtPassword.setSelection(password.length());
				}
			}
		});

		mCbPwdConfirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (((CheckBox) view).isChecked()) {
					mEtPwdConfirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				} else {
					mEtPwdConfirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
				if (mEtPwdConfirm.hasFocus()) {
					String password = mEtPwdConfirm.getText().toString();
					mEtPwdConfirm.setSelection(password.length());
				}
			}
		});

		mEtPassword.addTextChangedListener(new MyTextWatcher());
		mEtPwdConfirm.addTextChangedListener(new MyTextWatcher());

		mBtnSubmit.setOnClickListener(new NoDoubleClickListener() {
			@Override
			protected void onNoDoubleClick(View v) {
				Utils.hideKeyboard(v);
				if (checkPassword()) {
					if (mActivity.mSMSType == SMSNotificationActivity.SMS_TYPE_1_REGISTER) {
						register();
					} else if (mActivity.mActionType == SMSNotificationActivity.ACTION_TYPE_2_FORGOT_PASSWORD) {
						resetPassword();
					}
				}
			}
		});
	}

	private Map<String, String> getParams() {
		Map<String, String> p = new HashMap<>();
		try {
			Session.deviceID = Utils.getDeviceID(mActivity);
			p.put("password", mEtPassword.getText().toString());
			p.put("country_code", Session.verificationCountryCode);
			p.put("phone", Session.verificationCountryCode + "-" + Session.verificationPhone);
			p.put("version_name", LibSession.sVersionName);
			p.put("device_id", Session.deviceID);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return p;
	}

	private void register() {
		boolean paramsAreReady = Utils.checkViewTextNotNull(mEtUserName, mEtPassword, mEtPwdConfirm);
		Map<String, String> params = getParams();
		params.put("user_name", mEtUserName.getText().toString().trim());
		if (paramsAreReady) {
			mPresenter.register(params);
		}
	}

	private void resetPassword() {
		boolean paramsAreReady = Utils.checkViewTextNotNull(mEtPassword, mEtPwdConfirm);
		Map<String, String> params = getParams();
		if (paramsAreReady) {
			mPresenter.resetPassword(params);
		}
	}

	@Override
	public String getPassword() {
		return mEtPassword.getText().toString();
	}

	@Override
	public String getConfirmPassword() {
		return mEtPwdConfirm.getText().toString();
	}

	@Override
	public void finish() {
		getActivity().finish();
	}

	@Override
	public void toMainActivity() {
		Intent it = new Intent(mActivity, MainActivity.class);
		startActivity(it);
		finish();
	}

	@Override
	public SharedPreferences getPreferencesGesture() {
		return mActivity.getSharedPreferences(BuildConfig.PREFERENCES_USER, MODE_PRIVATE);
	}

	@Override
	public void clearFingerprintAndGesture() {
		FingerprintHelper loginHelper = new FingerprintHelper.Login(getContext());
		loginHelper.setIsUseFingerprint(false);
		loginHelper.clearData();
		FingerprintHelper pay = new FingerprintHelper.Pay(getContext());
		pay.setIsUseFingerprint(false);
		pay.clearData();
		getPreferencesGesture().edit().putString(AppGlobal.PREFERENCES_GESTURE_USER, "").apply();
		SPUtil.putString(LibGlobal.PRE_DATA_KEY, Session.user.getPhone() + AppGlobal.GESTURE, "");
		SPUtil.putInt(LibGlobal.PRE_DATA_KEY, Session.user.getPhone() + AppGlobal.HINT_COUNT, 0);
	}

	@Override
	public void showToast(String msg) {
		SnackBarUtil.show(mLlRoot, msg);
	}

	private void toLoginActivity() {
		Intent intent = new Intent(mActivity, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	private void clear() {
		mEtUserName.setText("");
		mEtUserName.setHint("");
		mEtPassword.setText("");
		mEtPassword.setHint("");
		mEtPwdConfirm.setText("");
		mEtPwdConfirm.setHint("");
	}

	private boolean checkPassword() {
		String password = mEtPassword.getText().toString();
		String pwdConfirm = mEtPwdConfirm.getText().toString();
		if (!password.equals(pwdConfirm)) {
			Utils.showToast(R.string.invalid_confirm_password);
			return false;
		}
		int code = Utils.checkPassword(password);
		if (code == 1) {
			Utils.showToast(R.string.msg_1006);
			return false;
		} else if (code == 2) {
			Utils.showToast(R.string.pwd_too_simple);
			return false;
		}
		return true;
	}

	private class MyTextWatcher extends SimpleTextWatcher {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			mBtnSubmit.setEnabled(checkTextViewContent(mEtPassword, mEtPwdConfirm));
		}
	}

	@Override
	public void toLogin(String msg) {
		mActivity.toLogin(msg);
	}
}
