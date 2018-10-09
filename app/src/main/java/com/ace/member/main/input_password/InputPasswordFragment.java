package com.ace.member.main.input_password;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.ace.member.R;
import com.ace.member.adapter.GVKeyboardAdapter;
import com.ace.member.simple_listener.SimplePasswordChangeListener;
import com.ace.member.sms_notification.SMSNotificationActivity;
import com.ace.member.utils.AppUtils;
import com.jungly.gridpasswordview.GridPasswordView;
import com.og.utils.CustomDialog;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class InputPasswordFragment extends BottomSheetDialogFragment implements InputPasswordContract.View {
	@Inject
	InputPasswordPresenter mPresenter;
	@BindView(R.id.rl_pwd)
	RelativeLayout mRlPwd;
	@BindView(R.id.gpv_password)
	GridPasswordView mGpvPassword;
	@BindView(R.id.gv_keyboard)
	GridView mGvKeyboard;
	private Unbinder mUnbinder;

	private String mCurrentPwd;
	private FragmentManager mManager;
	private String mTag;
	private InputPasswordCallback mPasswordCallback;
	private Context mContext;
	private boolean mIsLock;

	public InputPasswordFragment() {
		Bundle bundle = new Bundle();
		bundle.putString("name", "InputPasswordFragment");
		setArguments(bundle);
	}

	public void setPasswordCallback(InputPasswordCallback passwordCallback) {
		mPasswordCallback = passwordCallback;
	}

	public void setContext(Context context) {
		mContext = context;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_input_password1, null);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		try {
			mUnbinder = ButterKnife.bind(this, view);
			EditText editText = (EditText) mGpvPassword.getRootView().findViewById(R.id.inputView);
			editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					v.setFocusable(false);
					v.setFocusableInTouchMode(false);
				}
			});
			final MyPwdChangeListener listener = new MyPwdChangeListener();
			mGpvPassword.setOnPasswordChangedListener(listener);
			final GVKeyboardAdapter gvKeyboardAdapter = new GVKeyboardAdapter(getContext());
			mGvKeyboard.setAdapter(gvKeyboardAdapter);
			mGvKeyboard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					if (mIsLock) return;
					mCurrentPwd = mGpvPassword.getPassWord();
					if (id == -1) {
						if (TextUtils.isEmpty(mCurrentPwd)) return;
						if (mCurrentPwd.length() <= 1) {
							mGpvPassword.clearPassword();
						} else {
							mGpvPassword.setPassword(mCurrentPwd.substring(0, mCurrentPwd.length() - 1));
						}
					} else if (id != -2 && mCurrentPwd.length() < 6) {
						mGpvPassword.setPassword(mCurrentPwd + id);
					}
					listener.onTextChanged(mGpvPassword.getPassWord());
				}
			});

			mRlPwd.getViewTreeObserver()
				.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						View view = InputPasswordFragment.this.getDialog()
							.getWindow()
							.findViewById(android.support.design.R.id.design_bottom_sheet);
						BottomSheetBehavior.from(view).setPeekHeight(mRlPwd.getHeight());
						mRlPwd.getViewTreeObserver().removeOnGlobalLayoutListener(this);
					}
				});
			mGpvPassword.getChildAt(0)
				.setBackground(Utils.getDrawable(R.drawable.bg_trading_password_box_focus));
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public void onDestroy() {
		try {
			if (mUnbinder != null) mUnbinder.unbind();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	@Override
	public void toTradingPassword() {
		Intent intent = new Intent(mContext, SMSNotificationActivity.class);
		intent.putExtra("action_type", SMSNotificationActivity.ACTION_TYPE_3_FORGOT_TRADING_PASSWORD);
		mContext.startActivity(intent);
	}

	@Override
	public void showIncorrectPwdDlg(int remainFailCount) {
		CustomDialog.Builder builder = new CustomDialog.Builder(getContext());
		builder.setMessage(String.format(Utils.getString(remainFailCount > 1 ? R.string.invalid_pwd_for_chances_left : R.string.invalid_pwd_for_chance_left), remainFailCount))
			.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					clearPassword();
					setLock(false);
				}
			});
		CustomDialog dialog = builder.create();
		dialog.setCancelable(false);
		dialog.show();
	}

	@Override
	public void showTradingPasswordStatusDlg(int status, int remain) {
		CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
		builder.setMessage(remain <= 0 ? Utils.getString(R.string.trading_pwd_is_lock) : AppUtils.getMsgByTradingPasswordStatus(status));
		builder.setPositiveButton(Utils.getString(R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				onLock();
			}
		});
		CustomDialog dialog = builder.create();
		dialog.setCancelable(false);
		dialog.show();
	}

	@Override
	public void showSelf() {
		super.show(mManager, mTag);
	}

	@Override
	public void verifyPasswordSuccess() {
		if (mPasswordCallback != null) mPasswordCallback.onSuccess();
		super.dismiss();
	}

	@Override
	public void verifyPasswordFail() {
		if (mPasswordCallback != null) mPasswordCallback.onFail();
		super.dismiss();
	}

	@Override
	public void clearPassword() {
		mGpvPassword.clearPassword();
		mGpvPassword.getChildAt(0)
			.setBackground(Utils.getDrawable(R.drawable.bg_trading_password_box_focus));
	}

	@Override
	public void onLock() {
		if (mPasswordCallback != null) mPasswordCallback.onLock();
		super.dismiss();
	}

	@OnClick({R.id.fl_close, R.id.tv_forgot_password})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.fl_close:
				onCancel();
				break;
			case R.id.tv_forgot_password:
				toTradingPassword();
				break;
		}
	}

	@Override
	public void show(FragmentManager manager, String tag) {
		this.mManager = manager;
		this.mTag = tag;
		DaggerInputPasswordComponent.builder()
			.inputPasswordPresenterModule(new InputPasswordPresenterModule(this, mContext))
			.build()
			.inject(this);
		mPresenter.start();
	}

	@Override
	public void onCancel() {
		if (mPasswordCallback != null) mPasswordCallback.onCancel();
		super.dismiss();
	}

	@Override
	public void setLock(boolean lock) {
		mIsLock = lock;
	}

	private class MyPwdChangeListener extends SimplePasswordChangeListener {
		@Override
		public void onTextChanged(String psw) {
			try {
				int length = psw.length();
				for (int i = 0; i < 12; i += 2) {
					mGpvPassword.getChildAt(i)
						.setBackground(i == length * 2 ? Utils.getDrawable(R.drawable.bg_trading_password_box_focus) : null);
				}
				mCurrentPwd = psw;
				if (!TextUtils.isEmpty(psw) && length == 6) {
					mPresenter.checkTradingPwd(psw);
				}
			} catch (Exception e) {
				FileUtils.addErrorLog(e);
			}
		}
	}
}
