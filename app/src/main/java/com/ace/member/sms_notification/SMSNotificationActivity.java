package com.ace.member.sms_notification;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.ace.member.R;
import com.ace.member.adapter.SMSNotificationFragmentAdapter;
import com.ace.member.base.BaseActivity;
import com.ace.member.base.BaseFragment;
import com.ace.member.login.LoginActivity;
import com.ace.member.sms_notification.first_step.FirstStepFragment;
import com.ace.member.sms_notification.second_step.SecondStepFragment;
import com.ace.member.utils.Session;
import com.ace.member.view.NoScrollViewPager;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;

public class SMSNotificationActivity extends BaseActivity implements SMSNotificationContract.SMSNotificationView {

	@Inject
	SMSNotificationPresenter mPresenter;
	@BindView(R.id.vp_content)
	NoScrollViewPager mVpContent;
	public TimerTask mTimerTask;
	private SMSNotificationFragmentAdapter mFragmentAdapter;

	public static final int ACTION_TYPE_1_RESET_TRADING_PASSWORD = 1;//重置交易密码
	public static final int ACTION_TYPE_2_FORGOT_PASSWORD = 2;//忘记登录密码
	public static final int ACTION_TYPE_3_FORGOT_TRADING_PASSWORD = 3;//忘记交易密码
	public static final int ACTION_TYPE_4_TO_SET_TRADING_PASSWORD = 4;//待设置交易密码
	public static final int ACTION_TYPE_5_REGISTER = 5;//注册
	public static final int ACTION_TYPE_6_TO_VERITY_CERTIFICATE = 6;//待身份验证

	//用于后台识别短信类型
	public static final int SMS_TYPE_1_REGISTER = 1;
	public static final int SMS_TYPE_2_PASSWORD = 2;
	public static final int SMS_TYPE_3_CASH = 3;
	public static final int SMS_TYPE_7_ONLINE_PAY = 7;
	public static final int SMS_TYPE_8_OFFLINE_PAY = 8;

	public int mActionType = 0;
	public int mSMSType = 0;
	public int mResendTime;

	//用于区分密码设置的开启指纹和登录界面的forgot password
	public int mSourcePage = 0;
	public static final int FROM_PASSWORD_SETTING = 1;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerSMSNotificationComponent.builder()
			.sMSNotificationPresenterModule(new SMSNotificationPresenterModule(this, this))
			.build()
			.inject(this);
		init();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_register;
	}

	private void init() {
		Bundle b = getIntent().getExtras();
		if (b != null) {
			mActionType = b.getInt("action_type");
			mSourcePage = b.getInt("source_page");
		}

		mFragmentAdapter = new SMSNotificationFragmentAdapter(getSupportFragmentManager());
		mVpContent.setAdapter(mFragmentAdapter);
		if (mActionType == ACTION_TYPE_4_TO_SET_TRADING_PASSWORD || mActionType == ACTION_TYPE_3_FORGOT_TRADING_PASSWORD || (mActionType == ACTION_TYPE_2_FORGOT_PASSWORD && mSourcePage == FROM_PASSWORD_SETTING)) {
			mVpContent.setCurrentItem(1);
		}
		setSMSType();
		//获取手机设备号
		Session.deviceID = Utils.getDeviceID(this);
	}

	public void toNextStep() {
		mVpContent.setCurrentItem(mVpContent.getCurrentItem() + 1);
		BaseFragment currentFragment = mFragmentAdapter.getPrimaryItem();
		currentFragment.flashData();
	}

	public void toPreviousStep() {
		if (mActionType == ACTION_TYPE_4_TO_SET_TRADING_PASSWORD || mActionType == ACTION_TYPE_3_FORGOT_TRADING_PASSWORD || (mActionType == ACTION_TYPE_2_FORGOT_PASSWORD && mSourcePage == FROM_PASSWORD_SETTING)) {
			finish();
		} else {
			mVpContent.setCurrentItem(mVpContent.getCurrentItem() - 1);
			BaseFragment currentFragment = mFragmentAdapter.getPrimaryItem();
			currentFragment.flashData();
		}
	}

	public void setRendTime(int time) {
		mResendTime = time;
	}

	@Override
	protected void onDestroy() {
		if (mTimerTask != null) {
			mTimerTask.cancel();
		}
		clearData();
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		if (mVpContent.getCurrentItem() >= 1) {
			toPreviousStep();
		} else if (mActionType == ACTION_TYPE_4_TO_SET_TRADING_PASSWORD || mActionType == ACTION_TYPE_3_FORGOT_TRADING_PASSWORD) {
			super.onBackPressed();
		} else {
			toLogin("");
		}
	}

	@Override
	public void toLogin(String msg) {
		Intent it = new Intent(this, LoginActivity.class);
		it.putExtra("msg", msg);
		startActivity(it);
		finish();
	}

	public void clearData() {
		Session.verificationCountryCode = "";
		Session.verificationPhone = "";
	}

	private void setSMSType() {
		if (mActionType == ACTION_TYPE_1_RESET_TRADING_PASSWORD || mActionType == ACTION_TYPE_2_FORGOT_PASSWORD || mActionType == ACTION_TYPE_3_FORGOT_TRADING_PASSWORD || mActionType == ACTION_TYPE_4_TO_SET_TRADING_PASSWORD) {
			mSMSType = SMS_TYPE_2_PASSWORD;
		} else if (mActionType == ACTION_TYPE_5_REGISTER) {
			mSMSType = SMS_TYPE_1_REGISTER;
		}
	}

	public void addFingerprintLog(int flag) {
		mPresenter.addFingerprintLog(flag);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		BaseFragment currencyFragment = mFragmentAdapter.getPrimaryItem();
		if (currencyFragment instanceof FirstStepFragment) {
			if (((FirstStepFragment) currencyFragment).onKeyDown(keyCode, event)) {
				return true;
			}
		} else if (currencyFragment instanceof SecondStepFragment) {
			if (((SecondStepFragment) currencyFragment).onKeyDown(keyCode, event)) {
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	public interface MyTouchListener {
		public void onTouchEvent(MotionEvent event);
	}

	// 保存MyTouchListener接口的列表
	private ArrayList<MyTouchListener> myTouchListeners = new ArrayList<SMSNotificationActivity.MyTouchListener>();

	/**
	 * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
	 */
	public void registerMyTouchListener(MyTouchListener listener) {
		myTouchListeners.add(listener);
	}

	/**
	 * 提供给Fragment通过getActivity()方法来取消注册自己的触摸事件的方法
	 */
	public void unRegisterMyTouchListener(MyTouchListener listener) {
		myTouchListeners.remove( listener );
	}

	/**
	 * 分发触摸事件给所有注册了MyTouchListener的接口
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		for (MyTouchListener listener : myTouchListeners) {
			listener.onTouchEvent(ev);
		}
		return super.dispatchTouchEvent(ev);
	}
}
