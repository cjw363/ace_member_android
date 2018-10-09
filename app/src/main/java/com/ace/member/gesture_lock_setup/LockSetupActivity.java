package com.ace.member.gesture_lock_setup;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseCountryCodeActivity;
import com.ace.member.gesture_lock.LockPatternUtils;
import com.ace.member.gesture_lock.LockPatternView;
import com.ace.member.main.MainActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.Session;
import com.og.utils.DialogFactory;
import com.og.utils.EncryptSHA;
import com.og.utils.FileUtils;
import com.og.utils.FingerprintHelper;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static com.ace.member.utils.AppGlobal.FROM_LOGIN;

public class LockSetupActivity extends BaseCountryCodeActivity implements LockSetupContract.LockSetupView, LockPatternView.OnPatternListener, View.OnClickListener {

	@BindView(R.id.lock_pattern)
	LockPatternView mLockPatternView;
	@BindView(R.id.tv_gesture_info)
	TextView mTvGestureInfo;
	@BindView(R.id.btn_cancel)
	Button mLeftButton;

	@Inject
	LockSetupPresenter mLockSetupPresenter;

	private static final int STEP_1 = 1; // 开始
	private static final int STEP_2 = 2; // 第一次设置手势完成
	private static final int STEP_4 = 4; // 第二次设置手势完成

	private int mStep;

	private List<LockPatternView.Cell> mChoosePattern;

	private boolean mConfirm = false;
	private Bundle mBundle;
	public int mActionType = 0;
	public int mFlagFrom = 0;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			mContext = this;
			mBundle = getIntent().getExtras();
			if (mBundle != null) {
				mActionType = mBundle.getInt("action_type");
				mFlagFrom = mBundle.getInt("flag_from", 0);
			}

			mLockPatternView.setOnPatternListener(this);

			DaggerLockSetupComponent.builder().lockSetupPresenterModule(new LockSetupPresenterModule(this, this)).build().inject(this);
			new ToolBarConfig.Builder(this,null).setTvTitleRes(R.string.set_gesture_password).build();
			mStep = STEP_1;
			updateView();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_gesture_lock_setup;
	}

	private void updateView() {
		try {
			switch (mStep) {
				case STEP_1:
					mLeftButton.setText(R.string.cancel);
					mChoosePattern = null;
					mConfirm = false;
					mLockPatternView.clearPattern();
					mLockPatternView.enableInput();
					break;
				case STEP_2:
					mLeftButton.setText(R.string.try_again);
					SystemClock.sleep(200);
					mTvGestureInfo.setText(getResources().getString(R.string.confirm_pattern_password));
					//					String str1 = EncryptSHA.sha1(LockPatternUtils.patternToString(mChoosePattern));
					mLockPatternView.clearPattern();
					mLockPatternView.enableInput();


					break;
				case STEP_4:
					mLeftButton.setText(R.string.cancel);
					if (mConfirm) {
						mLockPatternView.disableInput();
						String str = EncryptSHA.sha1(LockPatternUtils.patternToString(mChoosePattern));
						mLockSetupPresenter.saveGesture(str,mActionType);
						FingerprintHelper helper = new FingerprintHelper.Login(mContext);
						if (helper.getIsUseFingerprint()){
							helper.setIsUseFingerprint(false);
							mLockSetupPresenter.addFingerprintLog(AppGlobal.ACTION_TYPE_3_SWITCH_OFF);
							helper.clearData();
						}

						FingerprintHelper pay = new FingerprintHelper.Pay(mContext);
						if(pay.getIsUseFingerprint()){
							pay.setIsUseFingerprint(false);
							pay.clearData();
						}
						//						}

					} else {
						//DialogFactory.ToastDialog(this, getResources().getString(R.string.title_attention), getResources().getString(R.string.different_from_the_last_time), 0, null);
						mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
						mHandler2.postDelayed(mRunnable, 1000);
					}
					break;
				default:
					break;
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}

	}

	@Override
	protected void saveGestureFail(){
		Utils.showToast(R.string.fail);
		if (mActionType == AppGlobal.ACTION_TYPE_6_FORGOT_GESTURE || mFlagFrom == FROM_LOGIN) {
			Intent it = new Intent(mContext, MainActivity.class);
			startActivity(it);
			finish();
		}
	}

	@Override
	public void toMainActivity(String gesture) {
		try {
			mPreferencesUser.edit().putString(AppGlobal.USER_PHONE, Session.user.getPhone()).putString(AppGlobal.PREFERENCES_GESTURE_USER, gesture).apply();
			Session.flagUseGesturePwd = true;
			if (mActionType == AppGlobal.ACTION_TYPE_6_FORGOT_GESTURE|| mFlagFrom == FROM_LOGIN) {
				Intent it = new Intent(mContext, MainActivity.class);
				startActivity(it);
				finish();
			}else{
				onBackPressed();
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private Handler mHandler2 = new Handler();

	private Runnable mRunnable = new Runnable() {

		public void run() {
			mLockPatternView.clearPattern();
			mLockPatternView.enableInput();
		}
	};

	@Override
	public void onPatternStart() {

	}

	@Override
	public void onPatternCleared() {

	}

	@Override
	public void onPatternCellAdded(List<LockPatternView.Cell> pattern) {

	}

	@Override
	public void onPatternDetected(List<LockPatternView.Cell> pattern) {

		try {
			if (pattern.size() < LockPatternView.MIN_LOCK_PATTERN_SIZE) {
				Utils.showToast(R.string.lockpattern_recording_incorrect_too_short);
				mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
				SystemClock.sleep(300);
				mLockPatternView.clearPattern();
				return;
			}

			if (mChoosePattern == null) {
				mChoosePattern = new ArrayList<>(pattern);
				mStep = STEP_2;
				updateView();
				return;
			}
			mConfirm = mChoosePattern.equals(pattern);
			mStep = STEP_4;
			updateView();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		try {
			switch (v.getId()) {
				case R.id.btn_cancel:

					if (mStep == STEP_1 || mStep == STEP_4) {
						Intent it;
						if (mActionType == AppGlobal.ACTION_TYPE_6_FORGOT_GESTURE|| mFlagFrom == FROM_LOGIN) {
							it = new Intent(this, MainActivity.class);
							startActivity(it);
							finish();
						} else {
							finish();
						}
					} else if (mStep == STEP_2) {
						mStep = STEP_1;
						updateView();
					}
					break;

				default:
					break;
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		DialogFactory.unblock();
		mHandler2.removeCallbacks(mRunnable);
		super.onDestroy();
	}
}
