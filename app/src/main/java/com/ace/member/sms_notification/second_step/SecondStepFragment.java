package com.ace.member.sms_notification.second_step;


import android.content.Intent;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseFragment;
import com.ace.member.keyboard.KeyboardUtil;
import com.ace.member.main.me.password.trading_password.TradingPasswordActivity;
import com.ace.member.sms_notification.SMSNotificationActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.Session;
import com.ace.member.utils.SnackBarUtil;
import com.ace.member.view.VerificationCodeView;
import com.og.utils.Utils;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;

public class SecondStepFragment extends BaseFragment implements SecondStepContract.SecondStepView {

	@Inject
	SecondStepPresenter mPresenter;

	@BindView(R.id.ll_root)
	LinearLayout mLlRoot;
	@BindView(R.id.sv_main)
	ScrollView mSvMain;
	@BindView(R.id.verify_code)
	VerificationCodeView mVerificationCodeView;
	@BindView(R.id.tv_message)
	TextView mTvMessage;
	@BindView(R.id.time)
	Button mBtnTime;

	private Timer mTimer;
	private MyTimerTask mTimerTask;
	private SMSNotificationActivity mActivity;
	private KeyboardUtil mKeyboardUtil;

	@Override
	protected int getContentViewLayout() {
		return R.layout.fragment_notification_second_step;
	}

	@Override
	protected void initView() {
		DaggerSecondStepComponent.builder()
			.secondStepPresenterModule(new SecondStepPresenterModule(this, getContext()))
			.build()
			.inject(this);
		mActivity = (SMSNotificationActivity) getActivity();
		ToolBarConfig.builder(null, getView())
			.setTvTitleRes(R.string.verification_code)
			.setEnableBack(true)
			.setBackListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mActivity.toPreviousStep();
				}
			})
			.build();
		mBtnTime.setClickable(false);
		mBtnTime.setEnabled(false);

		if (mActivity.mActionType == SMSNotificationActivity.ACTION_TYPE_4_TO_SET_TRADING_PASSWORD || mActivity.mActionType == SMSNotificationActivity.ACTION_TYPE_3_FORGOT_TRADING_PASSWORD || (mActivity.mActionType == SMSNotificationActivity.ACTION_TYPE_2_FORGOT_PASSWORD && mActivity.mSourcePage == SMSNotificationActivity.FROM_PASSWORD_SETTING)) {
			flashData();
		}

		mVerificationCodeView.clear(false);
		initKeyboard();
		initListener();
	}

	private void initKeyboard() {
		//初始化数字键盘
		mKeyboardUtil = new KeyboardUtil(getContext(), mLlRoot, mSvMain);
		//VerificationView设置数字键盘
		mVerificationCodeView.setKeyboardUtil(mKeyboardUtil);
	}

	private void initListener() {
		mVerificationCodeView.setOnCompleteListener(new VerificationCodeView.Listener() {
			@Override
			public void onComplete(String content) {
				if (mActivity.mSMSType == SMSNotificationActivity.SMS_TYPE_1_REGISTER) {
					mPresenter.register(content, getFullPhone());
				} else if (mActivity.mActionType == SMSNotificationActivity.ACTION_TYPE_4_TO_SET_TRADING_PASSWORD || mActivity.mActionType == SMSNotificationActivity.ACTION_TYPE_3_FORGOT_TRADING_PASSWORD) {
					mPresenter.resetStatus(content, getFullPhone());
				} else if (mActivity.mActionType == SMSNotificationActivity.ACTION_TYPE_2_FORGOT_PASSWORD) {
					mPresenter.resetPassword(content, getFullPhone());
				}
				mKeyboardUtil.hideAllKeyBoard();
			}

		});

		mBtnTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mPresenter.sendVerificationCode(getFullPhone(), mActivity.mSMSType, mActivity.mActionType);
			}
		});

		SMSNotificationActivity.MyTouchListener myTouchListener = new SMSNotificationActivity.MyTouchListener() {
			@Override
			public void onTouchEvent(MotionEvent event) {
				// 处理手势事件
				mKeyboardUtil.hideKeyboard(event, mVerificationCodeView);
			}
		};
		// 将myTouchListener注册到分发列表
		((SMSNotificationActivity) this.getActivity()).registerMyTouchListener(myTouchListener);
	}

	@Override
	public void flashData() {
		mVerificationCodeView.clear(false);
		String phone = getFullPhone();
		mTvMessage.setText(phone);
		mPresenter.sendVerificationCode(phone, mActivity.mSMSType, mActivity.mActionType);
	}

	//开始倒计时
	@Override
	public void startClock() {
		mTimer = new Timer(true);
		mBtnTime.setText(String.format(getResources().getString(R.string.next_resend_time), mActivity.mResendTime));
		if (mTimer != null) {
			if (mTimerTask != null) {
				mTimerTask.cancel();  //将原任务从队列中移除
			}
			mTimerTask = new MyTimerTask();  // 新建一个任务
			mActivity.mTimerTask = mTimerTask;
			mTimer.schedule(mTimerTask, 1000, 1000);
			mBtnTime.setClickable(false);
			mBtnTime.setEnabled(false);
			mBtnTime.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
		}
	}

	@Override
	public void toNextStep() {
		mActivity.toNextStep();
	}

	@Override
	public void toPreviousStep() {
		mActivity.toPreviousStep();
	}

	@Override
	public void finish() {
		mTimerTask.cancel();
		mTimer.cancel();
		clearSession();
		getActivity().finish();
	}

	@Override
	public void toTradingPassword() {
		Intent intent = new Intent(getActivity(), TradingPasswordActivity.class);
		intent.putExtra("action_type", mActivity.mActionType);
		intent.putExtra("trading_status", 0);
		startActivity(intent);
		finish();
	}

	@Override
	public void showFailure() {
		Utils.hideKeyboard(mVerificationCodeView);
		mVerificationCodeView.clear(true);
		Utils.showToast(R.string.wrong_code);
		Animation shakeAnim = AnimationUtils.loadAnimation(getContext(), R.anim.shake_x);
		mVerificationCodeView.startAnimation(shakeAnim);
	}

	@Override
	public void setResendTime(int time) {
		mBtnTime.setText(String.format(Utils.getString(R.string.next_resend_time), time));
		mActivity.setRendTime(time);
	}

	@Override
	public void showResendTimeError() {
		mBtnTime.setText(Utils.getString(R.string.error));
		mBtnTime.setEnabled(false);
	}

	@Override
	public void showToast(String msg) {
		SnackBarUtil.show(mLlRoot, msg);
	}

	@Override
	public void toLogin(String msg) {
		mActivity.toLogin(msg);
	}

	private class MyTimerTask extends TimerTask {
		@Override
		public void run() {
			Utils.runOnUIThread(new Runnable() {
				@Override
				public void run() {
					mActivity.mResendTime--;
					mBtnTime.setText(String.format(Utils.getString(R.string.next_resend_time), mActivity.mResendTime));
					if (mActivity.mResendTime <= 0) {
						mTimer.cancel();
						mBtnTime.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
						mBtnTime.setText(Utils.getString(R.string.resend));
						mBtnTime.setClickable(true);
						mBtnTime.setEnabled(true);
					} else {
						mBtnTime.setClickable(false);
						mBtnTime.setEnabled(false);
					}
				}
			});
		}
	}

	private String getFullPhone() {
		if (mActivity.mActionType == SMSNotificationActivity.ACTION_TYPE_4_TO_SET_TRADING_PASSWORD || mActivity.mActionType == SMSNotificationActivity.ACTION_TYPE_3_FORGOT_TRADING_PASSWORD || (mActivity.mActionType == SMSNotificationActivity.ACTION_TYPE_2_FORGOT_PASSWORD && mActivity.mSourcePage == SMSNotificationActivity.FROM_PASSWORD_SETTING)) {
			return Session.user.getPhone();
		} else {
			return Session.verificationCountryCode + "-" + Session.verificationPhone;
		}
	}

	private void clearSession() {
		Session.verificationCountryCode = "";
		Session.verificationPhone = "";
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (mKeyboardUtil.mIsShow) {
				mKeyboardUtil.hideAllKeyBoard();
				return true;
			}
		}
		return false;
	}

}
