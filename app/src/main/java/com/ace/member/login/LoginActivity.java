package com.ace.member.login;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseCountryCodeActivity;
import com.ace.member.gesture_lock_setup.LockSetupActivity;
import com.ace.member.listener.NoDoubleClickListener;
import com.ace.member.login_password.LoginPasswordActivity;
import com.ace.member.main.MainActivity;
import com.ace.member.simple_listener.SimpleTextWatcher;
import com.ace.member.sms_notification.SMSNotificationActivity;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.Session;
import com.og.LibSession;
import com.og.M;
import com.og.event.MessageEvent;
import com.og.utils.CustomDialog;
import com.og.utils.FileUtils;
import com.og.utils.ItemObject;
import com.og.utils.Utils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;

public class LoginActivity extends BaseCountryCodeActivity implements LoginContract.LoginView {

	@Inject
	LoginPresenter mLoginPresenter;
	@BindView(R.id.et_phone)
	EditText mEtPhone;
	@BindView(R.id.et_password)
	EditText mEtPassword;
	@BindView(R.id.cb_password_change)
	CheckBox mCbPasswordChange;
	@BindView(R.id.tv_forgot_password)
	TextView mTvForgotPassword;
	@BindView(R.id.tv_login_register)
	TextView mTvRegister;
	@BindView(R.id.btn_submit)
	Button mBtnSubmit;
	@BindView(R.id.tv_version)
	TextView mTvVersion;
	@BindView(R.id.tv_country_code)
	TextView mTvCountryCode;
	@BindView(R.id.ll_country_code)
	LinearLayout mLlCountryCode;
	@BindView(R.id.ll_main_login)
	LinearLayout mLlMain;
	@BindView(R.id.ll_line)
	LinearLayout mLlLine;
	@BindView(R.id.iv_phone)
	ImageView mIvPhone;
	@BindView(R.id.iv_password)
	ImageView mIvPassword;
	@BindView(R.id.iv_delete)
	AppCompatImageView mIvDelete;

	public static final int ACTION_TYPE_2_FORGOT_GESTURE = 2;//点击忘记密码登陆方式

	private int mActionType = 0;//0代表正常进入登录页面
	private String mCountryCode = String.valueOf(AppGlobal.COUNTRY_CODE_855_CAMBODIA); //当前输入的手势

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mEtPhone.requestFocus();
		DaggerLoginComponent.builder()
			.loginPresenterModule(new LoginPresenterModule(this, this))
			.build()
			.inject(this);
		initPhone();
		setVersion(String.format("%s [%s]", LibSession.sVersionName, LibSession.sServiceVersion));
		initData();
		initListener();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_login;
	}

	@Override
	protected void onResume() {
		super.onResume();
		getData();
	}

	private void initPhone() {
		String phone = getPreferencesUser().getString(AppGlobal.LOGIN_USER_PHONE, "");
		if (!TextUtils.isEmpty(phone)) {
			setPhone(phone);
		} else {
			mEtPhone.requestFocus();
		}
	}

	//这里只有公用了忘记手势密码所以检测登录动作
	private void initData() {
		try {
			Bundle b = getIntent().getExtras();
			if (b != null) {
				mActionType = b.getInt("action_type");
				String msg = b.getString("msg");
				if (!TextUtils.isEmpty(msg)) Utils.showToast(msg);
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private void initListener() {
		try {
			mLlCountryCode.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					showCountryCode(mTvCountryCode);
				}
			});

			mCbPasswordChange.setOnClickListener(new View.OnClickListener() {
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

			mBtnSubmit.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					Map<String, String> params = getParams();
					clearFingerprintLoginData();
					clearFingerprintPayData();
					mLoginPresenter.login(params);
				}
			});

			//		mBtnSubmit.setClickable(false);

			mEtPhone.addTextChangedListener(new SimpleTextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if (s.length() > 0) {
						mIvDelete.setVisibility(View.VISIBLE);
					} else {
						mIvDelete.setVisibility(View.GONE);
					}
					mBtnSubmit.setEnabled(checkTextViewContent(mEtPassword, mEtPhone));
				}

				@Override
				public void afterTextChanged(Editable editable) {
					if (!TextUtils.isEmpty(editable)) {
						mLlLine.setBackgroundColor(Utils.getColor(R.color.clr_login_text));
						mIvPhone.setImageResource(R.drawable.ic_phone_login_selected);
					} else {
						mLlLine.setBackgroundColor(Utils.getColor(R.color.clr_login_text_invalid));
						mIvPhone.setImageResource(R.drawable.ic_phone_login_normal);
					}
				}
			});

			mIvDelete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mEtPhone.setText("");
				}
			});

			mEtPassword.addTextChangedListener(new SimpleTextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					mBtnSubmit.setEnabled(checkTextViewContent(mEtPassword, mEtPhone));
				}

				@Override
				public void afterTextChanged(Editable editable) {

					if (!TextUtils.isEmpty(editable)) {
						mIvPassword.setImageResource(R.drawable.ic_password_login_selected);
					} else {
						mIvPassword.setImageResource(R.drawable.ic_password_login_normal);
					}
				}
			});

			mTvRegister.setOnClickListener(new NoDoubleClickListener() {
				@Override
				protected void onNoDoubleClick(View v) {
					toRegisterActivity();
				}
			});

			mTvForgotPassword.setOnClickListener(new NoDoubleClickListener() {
				@Override
				protected void onNoDoubleClick(View v) {
					toForgotPwdActivity();
				}
			});
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}

	}

	public void getData() {
		mLoginPresenter.getLoginData();
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onMessageEvent(MessageEvent messageEvent) {
		int code = messageEvent.getCode();
		Message msg = messageEvent.getMsg();
		switch (code) {
			case M.MessageCode.ERR_442_GET_SERVICE_INFO_SUCCESS:
				try {
					LibSession.sFlagDev = (msg.getData().getInt("flag_dev") == 1);
					if (TextUtils.isEmpty(LibSession.sServiceVersion)) {
						LibSession.sServiceVersion = msg.getData().getString("version");
						if (LibSession.sFlagDev) LibSession.sServiceVersion += " DEV";
					}
					setVersion(String.format(this.getString(R.string.version_name), LibSession.sVersionName, LibSession.sServiceVersion));
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
				break;
			default:
				super.onMessageEvent(messageEvent);
				break;
		}
	}

	private Map<String, String> getParams() {
		Map<String, String> params = new HashMap<>();
		try {
			String phone = getPhone();
			params.put("phone", phone);
			params.put("version_name", LibSession.sVersionName);
			Session.deviceID = Utils.getDeviceID(mContext);
			params.put("device_id", Session.deviceID);
			params.put("is_device", Session.isSessionTimeOut + "");
			params.put("password", getPassword());
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return params;
	}

	@Override
	public void initCountryCodeList(List<ItemObject> list) {
		Session.countryCodeList = list;
		String curValue = "+" + mCountryCode;
		mTvCountryCode.setText(curValue);
	}

	public void setCountryCode(String curValue, TextView textView) {
		mCountryCode = curValue.replace("+", "").trim();
		mTvCountryCode.setText(curValue);
	}

	public void setVersion(String value) {
		if (!"".equals(value) && mTvVersion != null) {
			value = getString(R.string.member_system) + " " + value;
			mTvVersion.setText(value);
		}
	}

	@Override
	public void toLockSetupActivity() {
		Intent it = new Intent(this, LockSetupActivity.class);
		it.putExtra("action_type", AppGlobal.ACTION_TYPE_5_TO_RESET_GESTURE);
		it.putExtra("flag_from", AppGlobal.FROM_LOGIN);
		startActivity(it);
		finish();
	}

	private void setPhone(String value) {
		if (!"".equals(value) && mEtPhone != null) {
			String[] phone = value.split("-");
			if (phone.length == 2) {
				mCountryCode = phone[0].replace("+", "");
				String phoneStr = phone[1];
				if (!TextUtils.isEmpty(phoneStr)) {
					mEtPhone.setText(phoneStr);
					mEtPhone.setSelection(phoneStr.length());
					mIvPhone.setImageResource(R.drawable.ic_phone_selected_login);
					mIvDelete.setVisibility(View.VISIBLE);
					mEtPassword.requestFocus();
				}
			}
		}
	}

	@Override
	public String getPhone() {
		return "+" + mCountryCode + "-" + mEtPhone.getText().toString();
	}


	@Override
	public String getPassword() {
		return mEtPassword.getText().toString();
	}

	@Override
	public SharedPreferences getPreferencesUser() {
		return mPreferencesUser;
	}

	@Override
	public void toMainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void toResetPasswordActivity() {
		mPreferencesUser.edit().putString(AppGlobal.USER_PHONE, getPhone()).apply();
		Intent intent = new Intent(this, LoginPasswordActivity.class);
		intent.putExtra("activity", AppGlobal.ACTIVITY_15_RESET_PASSWORD);
		intent.putExtra("old_password", getPassword());
		startActivity(intent);
		finish();
	}

	@Override
	public int getActionType() {
		return mActionType;
	}

	protected void loginFail() {
		clearPassword();
		showLogin();
	}

	@Override
	public void showPrompt() {
		Dialog mDialog = new CustomDialog.Builder(mContext).setMessage(R.string.login_again2)
			.setIcon(R.drawable.ic_warining)
			.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					String phone = getPhone();
					mLoginPresenter.clearDeviceID(phone);
				}
			})
			.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					System.exit(0);
				}
			})
			.create();
		mDialog.setCancelable(false);
		mDialog.show();
	}

	public void setAgainLogin() {
		if (checkUser()) {
			Map<String, String> params = getParams();
			clearFingerprintLoginData();
			mLoginPresenter.login(params);
		}
	}

	@Override
	public void showLogin() {
		mLlMain.setVisibility(View.VISIBLE);
	}

	private boolean checkUser() {
		String phone = getPhone();
		String password = getPassword();
		if (TextUtils.isEmpty(phone)) {
			showNetworkError(getResources().getString(R.string.phone_required));
			return false;
		}
		if (TextUtils.isEmpty(password)) {
			showNetworkError(getResources().getString(R.string.password_required));
			return false;
		}
		return true;
	}

	private void toRegisterActivity() {
		Intent intent = new Intent(this, SMSNotificationActivity.class);
		intent.putExtra("action_type", SMSNotificationActivity.ACTION_TYPE_5_REGISTER);
		startActivity(intent);
		finish();
	}

	private void toForgotPwdActivity() {
		Intent intent = new Intent(this, SMSNotificationActivity.class);
		intent.putExtra("action_type", SMSNotificationActivity.ACTION_TYPE_2_FORGOT_PASSWORD);
		String phone = mEtPhone.getText().toString();
		if (!TextUtils.isEmpty(phone)) {
			Session.verificationPhone = phone;
			Session.verificationCountryCode = mTvCountryCode.getText().toString();
		}
		startActivity(intent);
		finish();
	}

	public void clearPassword() {
		mEtPassword.setText("");
	}

	@Override
	public FragmentManager getInstance() {
		return getSupportFragmentManager();
	}

	@Override
	public void showRegister(boolean functionRegister) {
		if (functionRegister) {
			mTvRegister.setVisibility(View.VISIBLE);
		} else {
			mTvRegister.setVisibility(View.GONE);
		}
	}
}
