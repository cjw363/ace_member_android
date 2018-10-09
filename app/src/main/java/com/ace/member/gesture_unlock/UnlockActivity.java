package com.ace.member.gesture_unlock;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.ace.member.BuildConfig;
import com.ace.member.R;
import com.ace.member.base.BaseCountryCodeActivity;
import com.ace.member.gesture_lock.LockPatternUtils;
import com.ace.member.gesture_lock.LockPatternView;
import com.ace.member.gesture_lock_setup.LockSetupActivity;
import com.ace.member.login.LoginActivity;
import com.ace.member.main.MainActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.Session;
import com.og.LibApplication;
import com.og.LibSession;
import com.og.utils.CustomDialog;
import com.og.utils.DialogFactory;
import com.og.utils.EncryptSHA;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;

public class UnlockActivity extends BaseCountryCodeActivity implements LockPatternView.OnPatternListener, UnlockContract.UnlockView {

	private String mPatternString;
	private int mFailedPatternAttemptsSinceLastTimeout = 0;
	private CountDownTimer mCountdownTimer = null;
	private Animation mShakeAnim;
	private String TAG = "UnlockActivity";
	private SharedPreferences mPreferencesUser;
	public static final int ACTION_TYPE_1_START = 1;
	public int mActionType = 0;
	public Bundle mBundle;

	@BindView(R.id.lock_pattern)
	LockPatternView mLockPatternView;
	@BindView(R.id.tv_gesture_info)
	TextView mTvGestureInfo;
	@BindView(R.id.tv_forgot_gesture_pwd)
	TextView mTvForgotGesturePwd;
	@BindView(R.id.tv_version)
	TextView mVersion;

	@Inject
	UnlockPresenter mUnlockPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionType();
		initActivity();

	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_gesture_lock;
	}

	protected void initActivity() {
		try {
			mContext = this;
			mPreferencesUser = getSharedPreferences(BuildConfig.PREFERENCES_USER, Context.MODE_PRIVATE);
			mPatternString = mPreferencesUser.getString(AppGlobal.PREFERENCES_GESTURE_USER, "");
			if (TextUtils.isEmpty(mPatternString)) {
				Intent it = new Intent(UnlockActivity.this, LoginActivity.class);
				startActivity(it);
				finish();
				return;
			}

			DaggerUnlockComponent.builder()
				.unlockPresenterModule(new UnlockPresenterModule(this, this))
				.build()
				.inject(this);
			new ToolBarConfig.Builder(this, null).setTvTitleRes(R.string.enter_pattern_password)
				.build();
			if (mActionType == ACTION_TYPE_1_START) {
				mTvForgotGesturePwd.setVisibility(View.VISIBLE);
			}

			mLockPatternView.setOnPatternListener(this);
			//		mLockPatternView.setOnPatternListener(mChooseNewLockPatternListener);
			mShakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake_x);

			MyOnClickListener onClick = new MyOnClickListener();
			mTvForgotGesturePwd.setOnClickListener(onClick);

			super.initActivity();
			String ver = getString(R.string.member_system) + " " + String.format(this.getString(R.string.version_name), LibSession.sVersionName, (Session.sServiceVersion
				.equals("") ? "..." : Session.sServiceVersion));
			mVersion.setText(ver);
			checkLock();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}

	}

	private void initActionType() {
		mBundle = getIntent().getExtras();
		if (mBundle != null) mActionType = mBundle.getInt("action_type");
	}

	@Override
	public SharedPreferences getPreferencesUser() {
		return mPreferencesUser;
	}

	protected void checkLock() {
		try {
			long curTime = System.currentTimeMillis();
			long time = curTime - LockPatternUtils.START_LOCK_TIME;
			if (LockPatternUtils.FAILED_ATTEMPT_TIMEOUT_MS > time) {
				LockPatternUtils.COUNT_DOWN_TIME = LockPatternUtils.FAILED_ATTEMPT_TIMEOUT_MS - time;
				//				mTvGestureInfo.setTextColor(Utils.getColor(R.color.clr_error_red));
				mTvGestureInfo.startAnimation(mShakeAnim);
				LibApplication.getHandler().removeCallbacks(attemptLockout);
				LibApplication.getHandler().postDelayed(attemptLockout, 1);
			}
			mFailedPatternAttemptsSinceLastTimeout = mPreferencesUser.getInt("handle_times", 0);
			int retry = LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT - mFailedPatternAttemptsSinceLastTimeout;
			if (mFailedPatternAttemptsSinceLastTimeout > 0 && retry > 0) {
				mTvGestureInfo.setText(String.format(getResources().getString(R.string.gesture_invalid_pattern), retry));
				mTvGestureInfo.setTextColor(Utils.getColor(R.color.clr_error_red));
				mTvGestureInfo.startAnimation(mShakeAnim);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onPatternStart() {
		mLockPatternView.removeCallbacks(mClearPatternRunnable);
	}

	@Override
	public void onPatternCleared() {
		mLockPatternView.removeCallbacks(mClearPatternRunnable);
	}

	@Override
	public void toMainActivity() {
		Intent it = new Intent(this, MainActivity.class);
		startActivity(it);
		finish();
	}

	@Override
	public void onPatternCellAdded(List<LockPatternView.Cell> pattern) {
	}

	@Override
	public void onPatternDetected(List<LockPatternView.Cell> pattern) {
		try {
			//验证手势密码
			if (EncryptSHA.sha1(LockPatternUtils.patternToString(pattern)).equals(mPatternString)) {
				if (mBundle == null) {
				} else {
					if (mActionType == ACTION_TYPE_1_START) {
						mPreferencesUser.edit().putInt("handle_times", 0).apply();
						Map<String, String> p = getParams();
						mUnlockPresenter.login(p);

					} else if (mActionType == AppGlobal.ACTION_TYPE_5_TO_RESET_GESTURE) {
						Intent i = new Intent(UnlockActivity.this, LockSetupActivity.class);
						i.putExtra("action_type", AppGlobal.ACTION_TYPE_5_TO_RESET_GESTURE);
						mPreferencesUser.edit().putInt("handle_times", 0).apply();
						startActivity(i);
						finish();
					}
				}
			} else {
				mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
				mFailedPatternAttemptsSinceLastTimeout++;
				if (mPreferencesUser != null) mPreferencesUser.edit()
					.putInt("handle_times", mFailedPatternAttemptsSinceLastTimeout)
					.apply();
				int retry = LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT - mFailedPatternAttemptsSinceLastTimeout;
				if (retry >= 0) {
					if (retry == 0)
						showToast(getResources().getString(R.string.gesture_lock_forbidden));
					mTvGestureInfo.setText(String.format(getResources().getString(R.string.gesture_invalid_pattern), retry));
					mTvGestureInfo.setTextColor(Utils.getColor(R.color.clr_error_red));
					mTvGestureInfo.startAnimation(mShakeAnim);
				}
				if (mFailedPatternAttemptsSinceLastTimeout >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT) {
					//记录被锁的时间
					mLockPatternView.setEnabled(false);
					LockPatternUtils.START_LOCK_TIME = System.currentTimeMillis();
					LockPatternUtils.COUNT_DOWN_TIME = LockPatternUtils.FAILED_ATTEMPT_TIMEOUT_MS;
					LibApplication.getHandler().removeCallbacks(attemptLockout);
					LibApplication.getHandler().postDelayed(attemptLockout, 1000);
				} else {
					mLockPatternView.postDelayed(mClearPatternRunnable, 2000);
				}
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	private Map<String, String> getParams() {
		Map<String, String> p = new HashMap<>();
		try {
			String phone = mPreferencesUser.getString(AppGlobal.USER_PHONE, "");
			Session.deviceID = Utils.getDeviceID(mContext);
			p.put("phone", phone);
			p.put("password", "");
			p.put("version_name", LibSession.sVersionName);
			p.put("device_id", Session.deviceID);
			p.put("is_device", Session.isSessionTimeOut + "");
			p.put("gesture", mPatternString);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return p;
	}

	@Override
	public void selAgainLogin() {
		try {
			Map<String, String> p = getParams();
			clearFingerprintLoginData();
			mUnlockPresenter.login(p);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	protected void gestureLoginFail() {
		//清除记录登录账号
		mPreferencesUser.edit().putString(AppGlobal.LOGIN_USER_PHONE, "").apply();
		Intent it = new Intent(this, LoginActivity.class);
		it.putExtra("action_type", LoginActivity.ACTION_TYPE_2_FORGOT_GESTURE);
		startActivity(it);
		finish();
	}

	protected void loginFail() {
		Intent it = new Intent(this, LoginActivity.class);
		it.putExtra("action_type", 0);
		startActivity(it);
		finish();
	}


	@Override
	protected void onDestroy() {
		DialogFactory.unblock();
		super.onDestroy();
		if (mCountdownTimer != null) mCountdownTimer.cancel();
	}

	private Runnable mClearPatternRunnable = new Runnable() {
		public void run() {
			if (mLockPatternView != null) mLockPatternView.clearPattern();
		}
	};

	Runnable attemptLockout = new Runnable() {

		@Override
		public void run() {
			try {
				mLockPatternView.clearPattern();
				mLockPatternView.setEnabled(false);
				mCountdownTimer = new CountDownTimer(LockPatternUtils.COUNT_DOWN_TIME + 1, 1000) {
					@Override
					public void onTick(long millisUntilFinished) {
						int secondsRemaining = (int) (millisUntilFinished / 1000) - 1;
						if (secondsRemaining > 0) {
							mTvGestureInfo.setText(String.format(getResources().getString(R.string.gesture_lock_forbidden_time_left), secondsRemaining));
						} else {
							//							mGestureInfo.setText(getResources().getString(R.string.enter_pattern_password));
							//							mGestureInfo.setTextColor(Color.WHITE);
							mLockPatternView.setEnabled(true);
							mFailedPatternAttemptsSinceLastTimeout = 0;
							mPreferencesUser.edit()
								.putInt("handle_times", mFailedPatternAttemptsSinceLastTimeout)
								.apply();
						}

					}

					@Override
					public void onFinish() {
						mLockPatternView.setEnabled(true);
						mFailedPatternAttemptsSinceLastTimeout = 0;
						mPreferencesUser.edit()
							.putInt("handle_times", mFailedPatternAttemptsSinceLastTimeout)
							.apply();
					}
				}.start();
			} catch (Exception e) {
				FileUtils.addErrorLog(e);
				e.printStackTrace();
			}
		}
	};

	private void showToast(CharSequence msg) {
		Utils.showToast(msg);
	}

	private class MyOnClickListener implements View.OnClickListener {

		public void onClick(View v) {
			if (Utils.isFastClick(mContext)) return;

			int vid = v.getId();
			if (vid == R.id.tv_forgot_gesture_pwd) {
				mPreferencesUser.edit()
					.putInt("handle_times", 0)
					.putString(AppGlobal.LOGIN_USER_PHONE, "")
					.apply();
				Intent it = new Intent(UnlockActivity.this, LoginActivity.class);
				it.putExtra("action_type", LoginActivity.ACTION_TYPE_2_FORGOT_GESTURE);
				startActivity(it);
				finish();
			}
		}
	}


	@Override
	public void showPrompt() {
		Dialog mDialog = new CustomDialog.Builder(mContext).setMessage(R.string.login_again2)
			.setIcon(R.drawable.ic_warining)
			.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					String phone = mPreferencesUser.getString(AppGlobal.USER_PHONE, "");
					mUnlockPresenter.clearDeviceID(phone);
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

}

