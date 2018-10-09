package com.ace.member.setting_gesture;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ace.member.R;
import com.ace.member.base.BaseCountryCodeActivity;
import com.ace.member.gesture_lock_setup.LockSetupActivity;
import com.ace.member.gesture_unlock.UnlockActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.Session;
import com.og.utils.CustomDialog;
import com.og.utils.DialogFactory;
import com.og.utils.FileUtils;
import com.og.utils.FingerprintHelper;
import com.og.utils.Utils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingGestureActivity extends BaseCountryCodeActivity implements SettingGestureContract.SettingGestureView {

	@BindView(R.id.et_password)
	EditText mEtPassword;
	@BindView(R.id.btn_submit)
	Button mBtnSubmit;
	@BindView(R.id.v_separator1)
	View mVSeparator1;
	@BindView(R.id.v_separator2)
	View mVSeparator2;
	@BindView(R.id.cb_password)
	CheckBox mCbPasswordOld;
	@BindView(R.id.ll_set_gesture_password)
	LinearLayout mLSetGesture;
	@BindView(R.id.ll_reset_gesture_password)
	LinearLayout mLlResetGesture;
	@BindView(R.id.ll_forgot_gesture_password)
	LinearLayout mLForgotGesture;
	@BindView(R.id.ll_password_confirm)
	LinearLayout mLPasswordConfirm;

	public Dialog mDialogConfirm;
	public Bundle mBundle;
	//	private int status;
	//	mLlResetGesture, mLForgotGesture, mLSetGesture, mLPasswordConfirm;
	@Inject
	SettingGesturePresenter mSettingGesturePresenter;
	private int mActionType = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerSettingGestureComponent.builder()
			.settingGesturePresenterModule(new SettingGesturePresenterModule(this, this))
			.build()
			.inject(this);
		initActivity();
		initListener();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_setttings_gesture;
	}

	private void initListener() {
		try {
			mCbPasswordOld.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					try {
						if (((CheckBox) view).isChecked()) {
							mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
							mEtPassword.setSelection(mEtPassword.getText().length());
						} else {
							mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
							mEtPassword.setSelection(mEtPassword.getText().length());
						}
					} catch (Exception e) {
						FileUtils.addErrorLog(e);
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	protected void initActivity() {

		try {
			new ToolBarConfig.Builder(this,null).setTvTitleRes(R.string.gesture_password).build();
			if (mBundle != null) mActionType = mBundle.getInt("action_type");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onStart() {
		setGestureChecked();
		super.onStart();
	}


	@Override
	public void showConfirmPwdToastDialog() {
		DialogFactory.ToastDialog(SettingGestureActivity.this, getResources().getString(R.string.invalid_confirm_password), 0);
	}

	@Override
	public void closeDialog() {
		if (mDialogConfirm != null) mDialogConfirm.dismiss();
	}

	@Override
	public int getActionType() {
		return mActionType;
	}

	@Override
	public void setStatusType(boolean flag) {

		try {
			mLPasswordConfirm.setVisibility(View.GONE);
			if (!flag) {
				mLSetGesture.setVisibility(View.VISIBLE);
				mVSeparator1.setVisibility(View.VISIBLE);
				mVSeparator2.setVisibility(View.GONE);
				mLlResetGesture.setVisibility(View.GONE);
				mLForgotGesture.setVisibility(View.GONE);
			} else {
				mLSetGesture.setVisibility(View.GONE);
				mVSeparator1.setVisibility(View.GONE);
				mVSeparator2.setVisibility(View.VISIBLE);
				mLlResetGesture.setVisibility(View.VISIBLE);
				mLForgotGesture.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}

	}

	@Override
	public void showPasswordConfirm() {
		mLPasswordConfirm.setVisibility(View.VISIBLE);
	}

	public void setGestureChecked() {
		mSettingGesturePresenter.getGestureConfig();
	}

	@Override
	public void gotoUnlockActivity() {
		Intent it = new Intent(SettingGestureActivity.this, UnlockActivity.class);
		it.putExtra("action_type", AppGlobal.ACTION_TYPE_5_TO_RESET_GESTURE);
		startActivity(it);
	}

	@Override
	public void gotoLockSetupActivity() {
		Intent it = new Intent(mContext, LockSetupActivity.class);
		it.putExtra("action_type", AppGlobal.ACTION_TYPE_4_TO_SET_GESTURE);
		startActivity(it);
	}


	public void dialogTip() {
		try {
			Dialog mDialog = new CustomDialog.Builder(SettingGestureActivity.this).setMessage(R.string.msg_gesture_dlg_tip)
				.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						dialogInterface.dismiss();
						saveGesture();
					}
				})
				.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
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
			e.printStackTrace();
		}

	}

	private void saveGesture() {
		String password = mEtPassword.getText().toString();
		if (TextUtils.isEmpty(password)) {
			Utils.showToast(R.string.msg_1006);
		} else {
			mSettingGesturePresenter.checkPassword(password);
		}
		mEtPassword.setText("");
	}

	public void saveGestureSuccess(String gesture) {
		if (mActionType == AppGlobal.ACTION_TYPE_6_FORGOT_GESTURE) {
			Utils.showToast(R.string.success);
			mPreferencesUser.edit().putString(AppGlobal.PREFERENCES_GESTURE_USER, "").apply();
			Session.flagUseGesturePwd = false;
			mSettingGesturePresenter.getGestureConfig();
		}
	}

	@OnClick({R.id.ll_set_gesture_password, R.id.ll_reset_gesture_password, R.id.ll_forgot_gesture_password, R.id.btn_submit})
	public void onViewClicked(View view) {
		try {
			if (Utils.isFastClick(mContext)) return;

			switch (view.getId()) {
				case R.id.ll_set_gesture_password:
					mActionType = AppGlobal.ACTION_TYPE_4_TO_SET_GESTURE;
					mLPasswordConfirm.setVisibility(View.VISIBLE);
					break;
				case R.id.ll_reset_gesture_password:
					mActionType = AppGlobal.ACTION_TYPE_5_TO_RESET_GESTURE;
					gotoUnlockActivity();
					break;
				case R.id.ll_forgot_gesture_password:
					mActionType = AppGlobal.ACTION_TYPE_6_FORGOT_GESTURE;
					mSettingGesturePresenter.saveGesture("", AppGlobal.ACTION_TYPE_6_FORGOT_GESTURE);
					break;
				case R.id.btn_submit:

					FingerprintHelper helper = new FingerprintHelper.Login(mContext);
					if (helper.getIsUseFingerprint()) {
						dialogTip();
					} else {
						saveGesture();
					}
					break;
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

}

