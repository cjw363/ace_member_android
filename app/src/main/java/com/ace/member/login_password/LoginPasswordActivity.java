package com.ace.member.login_password;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.login.LoginActivity;
import com.ace.member.main.MainActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.Session;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginPasswordActivity extends BaseActivity implements LoginPasswordContract.ResetPasswordView,View.OnClickListener{

	@Inject
	LoginPasswordPresenter mLoginPasswordPresenter;

	@BindView(R.id.et_password_old)
	EditText mEtOldPassword;
	@BindView(R.id.et_confirm_password)
	EditText mEtConfirmPassword;
	@BindView(R.id.btn_submit)
	Button mBtnSubmit;
	@BindView(R.id.et_password_new)
	EditText mEtNewPassword;
	@BindView(R.id.cb_password_old)
	CheckBox mCbOldPassword;
	@BindView(R.id.cb_password_new)
	CheckBox mCbNewPassword;
	@BindView(R.id.cb_confirm_password)
	CheckBox mCbConfirmPassword;

	public int mActivity;
	boolean mFlagReset = false;
	private String mOldPwd = "";

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerLoginPasswordComponent.builder().loginPasswordPresenterModule(new LoginPasswordPresenterModule(this, this)).build().inject(this);
		setListener();
		initActivity();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_reset_password;
	}

	public void initActivity() {
		mActivity = getIntent().getExtras().getInt("activity");
		ToolBarConfig.Builder builder=new ToolBarConfig.Builder(this,null);
		builder.setTvTitleRes(R.string.reset_password).build();

		if (mActivity == AppGlobal.ACTIVITY_16_UPDATE_PASSWORD) {
			mFlagReset = true;
			findViewById(R.id.ll_your_password).setVisibility(View.VISIBLE);
		} else if (mActivity == AppGlobal.ACTIVITY_15_RESET_PASSWORD) {
			mFlagReset = false;
			mOldPwd = getIntent().getExtras().getString("old_password");
			findViewById(R.id.ll_your_password).setVisibility(View.GONE);
		}

	}

	private void setListener() {
		mCbOldPassword.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (((CheckBox) view).isChecked()) {
					mEtOldPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				} else {
					mEtOldPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
				mEtOldPassword.setSelection(mEtOldPassword.getText().length());
			}
		});
		mCbNewPassword.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (((CheckBox) view).isChecked()) {
					mEtNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				} else {
					mEtNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
				mEtNewPassword.setSelection(mEtNewPassword.getText().length());
			}
		});
		mCbConfirmPassword.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (((CheckBox) view).isChecked()) {
					mEtConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				} else {
					mEtConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
				mEtConfirmPassword.setSelection(mEtConfirmPassword.getText().length());
			}
		});
	}


	@Override
	public String getNewPassword() {
		return mEtNewPassword.getText().toString();
	}

	@Override
	public String getConfirmPassword() {
		return mEtConfirmPassword.getText().toString();
	}

	@Override
	public String getOldPassword() {
		if (mFlagReset){
			return mEtOldPassword.getText().toString();
		}else {
			return mOldPwd;
		}

	}


	@Override
	public boolean getFlagReset() {
		return mFlagReset;
	}

	@Override
	public void startMainActivity() {
		Intent it = new Intent(LoginPasswordActivity.this, MainActivity.class);
		startActivity(it);
		finish();
	}

	@Override
	public void finishActivity() {
		finish();
	}

	@Override
	public void clearGesturePassword() {
		mPreferencesUser.edit().putString(AppGlobal.PREFERENCES_GESTURE_USER,"").apply();
		Session.flagUseGesturePwd = false;
	}

	@Override
	public void onBackPressed() {
		if (mFlagReset){
			finish();
		}else {
			Intent it = new Intent(LoginPasswordActivity.this, LoginActivity.class);
			startActivity(it);
		}
		super.onBackPressed();
	}

	@OnClick({R.id.btn_submit})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_submit:
				mLoginPasswordPresenter.request();
				break;

			default:
				break;
		}
	}
}
