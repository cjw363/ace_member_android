package com.ace.member.main.me.password.trading_password;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.keyboard.KeyboardUtil;
import com.ace.member.main.verify_certificate.VerifyCertificateActivity;
import com.ace.member.sms_notification.SMSNotificationActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.view.MyGridPasswordView;
import com.jungly.gridpasswordview.GridPasswordView;
import com.jungly.gridpasswordview.imebugfixer.ImeDelBugFixedEditText;
import com.og.utils.Utils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class TradingPasswordActivity extends BaseActivity implements TradingPasswordContract.TradingPasswordView {

	@Inject
	TradingPasswordPresenter mTradingPresenter;
	@BindView(R.id.tv_pwd_comment)
	TextView mTvPwdComment;
	@BindView(R.id.btn_submit)
	Button mBtnSubmit;
	@BindView(R.id.ll_your_password)
	LinearLayout mLlYourPassword;
	@BindView(R.id.ll_password)
	LinearLayout mLlPassword;
	@BindView(R.id.ll_note)
	LinearLayout mLlNote;

	@BindView(R.id.cb_password_old)
	CheckBox mCbOldPassword;
	@BindView(R.id.cb_password_new)
	CheckBox mCbNewPassword;
	@BindView(R.id.cb_confirm_password)
	CheckBox mCbConfirmPassword;

	@BindView(R.id.v_separator3)
	View mVSeparator3;
	@BindView(R.id.tv_forgot_password)
	TextView mTvForgotPassword;
	@BindView(R.id.et_password_old)
	MyGridPasswordView mEtPasswordOld;
	@BindView(R.id.et_password_new)
	MyGridPasswordView mEtPasswordNew;
	@BindView(R.id.et_confirm_password)
	MyGridPasswordView mEtConfirmPassword;

	@BindView(R.id.tv_certificate)
	TextView mTvCertificate;
	@BindView(R.id.tv_certificate_tips)
	TextView mTvCertificateTips;
	@BindView(R.id.ll_root)
	LinearLayout mLlRoot;
	@BindView(R.id.sv_main)
	ScrollView mSvMain;
	@BindView(R.id.tv_new)
	TextView mTvNew;

	private int mActionType = 2;
	private int mTradingStatus = 0, mCertificateStatus = 0;
	private String mReason = "";
	private KeyboardUtil mKeyboardUtil;
	private MyGridPasswordView mCurrentPasswordView;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerTradingPasswordComponent.builder().tradingPasswordPresenterModule(new TradingPasswordPresenterModule(this, this)).build().inject(this);
		initKeyboard();
		setListener();
		initActivity();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_trading_password;
	}

	public void initActivity() {
		Bundle b = getIntent().getExtras();
		if (b != null) {
			mActionType = b.getInt("action_type");
			mTradingStatus = b.getInt("trading_status");
		}
		ToolBarConfig.Builder builder = new ToolBarConfig.Builder(this, null);
		builder.setTvTitleRes(R.string.trading_password).build();
		if (mActionType == SMSNotificationActivity.ACTION_TYPE_1_RESET_TRADING_PASSWORD || mActionType == SMSNotificationActivity.ACTION_TYPE_6_TO_VERITY_CERTIFICATE) {
			mLlYourPassword.setVisibility(View.VISIBLE);
			//进入页面默认第一行第一个框添加标识
			mEtPasswordOld.getChildAt(0).setBackground(Utils.getDrawable(R.drawable.bg_trading_password_box_focus));
		} else {
			mLlYourPassword.setVisibility(View.GONE);
			//进入页面默认第一行第一个框添加标识
			mEtPasswordNew.getChildAt(0).setBackground(Utils.getDrawable(R.drawable.bg_trading_password_box_focus));
		}
		//解决键盘不会弹出的问题
		mTvNew.requestFocus();
		if (mTradingStatus == AppGlobal.TRADING_PASSWORD_STATUS_1_ACTIVE) {
			mTvForgotPassword.setVisibility(View.VISIBLE);
		} else if (mTradingStatus == AppGlobal.TRADING_PASSWORD_STATUS_6_TO_VERIFY_CERTIFICATE) {
			if (b != null) {
				mCertificateStatus = b.getInt("certificate_status");
				mReason = b.getString("reason", "");
			}
			if (mCertificateStatus == AppGlobal.USER_VERIFY_STATUS_1_PENDING) {
				mTvCertificateTips.setVisibility(View.VISIBLE);
				mTvCertificateTips.setText(getString(R.string.waiting_for_verifying));
			} else {
				mTvCertificate.setVisibility(View.VISIBLE);
				if (!mReason.equals("")) {
					mTvCertificateTips.setVisibility(View.VISIBLE);
					mReason = getString(R.string.reject_reason) + mReason;
					mTvCertificateTips.setText(mReason);
				}
			}
		} else {
			//			mTvForgotPassword.setVisibility(View.GONE);
			mVSeparator3.setVisibility(View.VISIBLE);
		}
		//		mEtPasswordOld.getChildAt(0).setBackground(Utils.getDrawable(R.drawable.bg_trading_password_box_focus));
	}

	private void initKeyboard() {
		//初始化数字键盘
		mKeyboardUtil = new KeyboardUtil(this, mLlRoot, mSvMain);
		mKeyboardUtil.setOnBackSpaceListener(new KeyboardUtil.OnBackSpaceListener() {
			@Override
			public void onBackSpace(EditText editText) {
				if (mCurrentPasswordView != null) {
					ImeDelBugFixedEditText.OnDelKeyEventListener onDelKeyEventListener = mCurrentPasswordView.getOnDelKeyEventListener();
					if (onDelKeyEventListener != null) {
						onDelKeyEventListener.onDeleteClick();
					}
				}
			}
		});
	}

	private void setListener() {
		mCbOldPassword.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mEtPasswordOld.togglePasswordVisibility();
			}
		});
		mCbNewPassword.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mEtPasswordNew.togglePasswordVisibility();
			}
		});
		mCbConfirmPassword.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mEtConfirmPassword.togglePasswordVisibility();

			}
		});

		changeEditTextFocus(mEtPasswordOld, mEtPasswordNew, mEtConfirmPassword);
		changeEditTextFocus(mEtPasswordNew, mEtPasswordOld, mEtConfirmPassword);
		changeEditTextFocus(mEtConfirmPassword, mEtPasswordNew, mEtPasswordOld);
		mKeyboardUtil.hideAllKeyBoard();

		mEtPasswordOld.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
			@Override
			public void onTextChanged(String psw) {
				int length = psw.length();
				for (int i = 0; i < 12; i += 2) {
					mEtPasswordOld.getChildAt(i).setBackground(i == length * 2 ? Utils.getDrawable(R.drawable.bg_trading_password_box_focus) : null);
					mEtPasswordNew.getChildAt(i).setBackground(null);
					mEtConfirmPassword.getChildAt(i).setBackground(null);
				}
			}

			@Override
			public void onInputFinish(String psw) {

			}
		});

		mEtPasswordNew.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
			@Override
			public void onTextChanged(String psw) {
				int length = psw.length();
				for (int i = 0; i < 12; i += 2) {
					mEtPasswordNew.getChildAt(i).setBackground(i == length * 2 ? Utils.getDrawable(R.drawable.bg_trading_password_box_focus) : null);
					mEtPasswordOld.getChildAt(i).setBackground(null);
					mEtConfirmPassword.getChildAt(i).setBackground(null);
				}
			}

			@Override
			public void onInputFinish(String psw) {
			}
		});

		mEtConfirmPassword.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
			@Override
			public void onTextChanged(String psw) {
				int length = psw.length();
				for (int i = 0; i < 12; i += 2) {
					mEtConfirmPassword.getChildAt(i).setBackground(i == length * 2 ? Utils.getDrawable(R.drawable.bg_trading_password_box_focus) : null);
					mEtPasswordOld.getChildAt(i).setBackground(null);
					mEtPasswordNew.getChildAt(i).setBackground(null);
				}
			}

			@Override
			public void onInputFinish(String psw) {
			}
		});

	}

	private void setEditTextFocus(MyGridPasswordView view) {
		mCurrentPasswordView = view;
		ImeDelBugFixedEditText mInputView = (ImeDelBugFixedEditText) view.findViewById(R.id.inputView);
		if (mInputView != null) {
			attachKeyboard(mInputView);
			mInputView.setFocusable(true);
			mInputView.setFocusableInTouchMode(true);
			mInputView.requestFocus();
		}
	}

	private void changeEditTextFocus(final MyGridPasswordView view, final MyGridPasswordView view2, final MyGridPasswordView view3) {
		ImeDelBugFixedEditText mInputView3 = (ImeDelBugFixedEditText) view.findViewById(R.id.inputView);
		mInputView3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					attachKeyboard((EditText) v);
					int length = view.getPassWord().length();
					if (length == 6) {
						view.getChildAt(10).setBackground(Utils.getDrawable(R.drawable.bg_trading_password_box_focus));
					} else {
						for (int i = 0; i < 12; i += 2) {
							view.getChildAt(i).setBackground(i == length * 2 ? Utils.getDrawable(R.drawable.bg_trading_password_box_focus) : null);
						}
					}
					for (int i = 0; i < 12; i += 2) {
						view2.getChildAt(i).setBackground(null);
						view3.getChildAt(i).setBackground(null);
					}
				}
			}
		});
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setEditTextFocus(view);
				int length = view.getPassWord().length();
				if (length == 6) {
					view.getChildAt(10).setBackground(Utils.getDrawable(R.drawable.bg_trading_password_box_focus));
				} else {
					for (int i = 0; i < 12; i += 2) {
						view.getChildAt(i).setBackground(i == length * 2 ? Utils.getDrawable(R.drawable.bg_trading_password_box_focus) : null);
					}
				}
				for (int i = 0; i < 12; i += 2) {
					view2.getChildAt(i).setBackground(null);
					view3.getChildAt(i).setBackground(null);
				}
			}
		});
	}

	@OnClick({R.id.btn_submit, R.id.tv_certificate, R.id.tv_certificate_tips})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.btn_submit:
				mTradingPresenter.request(mActionType);
				break;
			case R.id.tv_certificate:
			case R.id.tv_certificate_tips:
				Intent intent = new Intent(TradingPasswordActivity.this, VerifyCertificateActivity.class);
				startActivity(intent);
				finish();

		}
	}

	@Override
	public String getNewPassword() {
		return mEtPasswordNew.getPassWord();
	}

	@Override
	public String getConfirmPassword() {
		return mEtConfirmPassword.getPassWord();
	}

	@Override
	public String getOldPassword() {
		return mEtPasswordOld.getPassWord();
	}


	@Override
	public void finishActivity() {
		finish();
	}


	@OnClick(R.id.tv_forgot_password)
	public void onViewClicked() {
		mTradingPresenter.addForgotPasswordLog();

	}


	public void enableSubmit(boolean flag) {
		if (flag) {
			mBtnSubmit.setEnabled(true);
		} else {
			mBtnSubmit.setEnabled(false);
		}
	}

	//绑定数字键盘
	private void attachKeyboard(EditText editText) {
		if (mKeyboardUtil != null) {
			if (mKeyboardUtil.getEditText() != null && editText.getId() != mKeyboardUtil.getEditText().getId()) {
				mKeyboardUtil.showKeyBoardLayout(editText, KeyboardUtil.INPUT_TYPE_1_NUM, -1);
			} else if (mKeyboardUtil.getEditText() == null) {
				mKeyboardUtil.showKeyBoardLayout(editText, KeyboardUtil.INPUT_TYPE_1_NUM, -1);
			} else {
				mKeyboardUtil.setKeyBoardCursorNew(editText);
			}
			mKeyboardUtil.setDeleteRepeatable(false);
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		int x = (int) ev.getRawX();
		int y = (int) ev.getRawY();
		if (isTouchPointInView(mEtPasswordOld, x, y)  ) {
			setEditTextFocus(mEtPasswordOld);
		}else if (isTouchPointInView(mEtPasswordNew, x, y)){
			setEditTextFocus(mEtPasswordNew);
		}else if (isTouchPointInView(mEtConfirmPassword, x, y)){
			setEditTextFocus(mEtConfirmPassword);
		}
		return super.dispatchTouchEvent(ev);
	}

	//(x,y)是否在view的区域内
	private boolean isTouchPointInView(View view, int x, int y) {
		if (view == null) {
			return false;
		}
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		int left = location[0];
		int top = location[1];
		int right = left + view.getMeasuredWidth();
		int bottom = top + view.getMeasuredHeight();
		return y >= top && y <= bottom && x >= left && x <= right;
	}

	@Override
	public void onBackPressed() {
		if (mKeyboardUtil.mIsShow){
			mKeyboardUtil.hideAllKeyBoard();
		}else {
			super.onBackPressed();
		}
	}

	public void checkStatus(int tradingStatus, int certificateStatus, String reason) {
		Intent it;
		if (tradingStatus == AppGlobal.TRADING_PASSWORD_STATUS_4_TO_SET) {
			it = new Intent(this, SMSNotificationActivity.class);
			it.putExtra("action_type", SMSNotificationActivity.ACTION_TYPE_4_TO_SET_TRADING_PASSWORD);
			it.putExtra("countryCode", String.valueOf(AppGlobal.COUNTRY_CODE_855_CAMBODIA));
			startActivity(it);
		} else if (tradingStatus == AppGlobal.TRADING_PASSWORD_STATUS_6_TO_VERIFY_CERTIFICATE){
			it = new Intent(TradingPasswordActivity.this, TradingPasswordActivity.class);
			it.putExtra("action_type", SMSNotificationActivity.ACTION_TYPE_6_TO_VERITY_CERTIFICATE);
			it.putExtra("trading_status", tradingStatus);
			it.putExtra("certificate_status",certificateStatus);
			it.putExtra("reason",reason);
			startActivity(it);
		}
		finish();
	}

}
