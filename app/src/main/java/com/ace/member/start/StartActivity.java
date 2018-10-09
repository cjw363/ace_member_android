package com.ace.member.start;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.ace.member.BuildConfig;
import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.gesture_unlock.UnlockActivity;
import com.ace.member.login.LoginActivity;
import com.ace.member.main.MainActivity;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.Session;
import com.og.LibSession;
import com.og.event.MessageEvent;
import com.og.update.UpdateChecker;
import com.og.utils.*;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;

public class StartActivity extends BaseActivity implements StartContract.StartView {

	public static final int ACTION_TYPE_1_START = 1;
	@Inject
	StartPresenter mStartPresenter;
	@BindView(R.id.tv_version)
	TextView mVersion;
	private UpdateChecker mUpdateChecker = new UpdateChecker();
	private boolean mIsLogin = true;
	private boolean isFirst = true;
	private String mFingerprintPhone;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!isTaskRoot()) {
			finish();
			return;
		}
		PERMISSIONS = new String[]{android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
		DaggerStartComponent.builder().startPresenterModule(new StartPresenterModule(this, this)).build().inject(this);
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_start;
	}

	//指纹的登录失败处理
	protected void loginFail() {
		showLogin();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
			setPermissions();
		} else {
			if (isFirst) {
				initActivity();
			}
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		isFirst = false;
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onMessageEvent(MessageEvent messageEvent) {
		mStartPresenter.onMessageEvent(messageEvent);
	}

	protected void initActivity() {
		super.initActivity();
		mStartPresenter.start();
	}

	@Override
	public void setVersion(String version) {
		version = getString(R.string.member_system) + " " + version;
		mVersion.setText(version);
	}

	@Override
	public void superMessageEvent(MessageEvent messageEvent) {
		super.onMessageEvent(messageEvent);
	}

	@Override
	public void showBlock(String msg) {
		DialogFactory.block(StartActivity.this, msg);
	}

	@Override
	public void showCheckForDialog() {
		mUpdateChecker.checkForDialog(StartActivity.this, BuildConfig.UPGRADE_URL);
	}

	@Override
	public void showVersionNotSupported() {
		findViewById(R.id.tvVersionNotSupported).setVisibility(View.VISIBLE);
	}

	@Override
	public void hideVersionNotSupported() {
		findViewById(R.id.tvVersionNotSupported).setVisibility(View.GONE);
	}

	@Override
	public boolean getIsLogin() {
		return mIsLogin;
	}

	@Override
	public void setIsLogin(boolean isLogin) {
		mIsLogin = isLogin;
	}

	@Override
	public String getGesture() {
		return mPreferencesUser.getString(AppGlobal.PREFERENCES_GESTURE_USER, "");
	}

	@Override
	public void showUnlock() {
		Intent intent = new Intent(StartActivity.this, UnlockActivity.class);
		intent.putExtra("action_type", ACTION_TYPE_1_START);
		startActivity(intent);
		finish();
	}

	@Override
	public void showFingerprintDialog(final String phone) {
		final FingerprintDialog dialog = FingerprintDialog.newInstance(this);
		mFingerprintPhone = phone;
		dialog.setCancelable(false);
		FragmentTransaction content = getSupportFragmentManager().beginTransaction();
		content.add(dialog, null).commitAllowingStateLoss();
		//dialog.show(getSupportFragmentManager(),"dialog");
		dialog.startAuth(new FingerprintDialog.PartAuthResult() {
			@Override
			public void cancelFingerprint() {
				dialog.dismiss();
				mStartPresenter.notFingerprintLogin(getGesture());
				//清除记录登录账号
				mPreferencesUser.edit().putString(AppGlobal.LOGIN_USER_PHONE, "").apply();
				finish();
			}

			@Override
			public void authSuccess() {
				dialog.dismiss();
				Map<String, String> p = new HashMap<>();
				Session.deviceID = Utils.getDeviceID(mContext);
				p.put("phone", phone);
				p.put("password", "");
				p.put("version_name", LibSession.sVersionName);
				p.put("device_id", Session.deviceID);
				p.put("is_device", Session.isSessionTimeOut + "");
				p.put("use_fingerprint", "true");
				mStartPresenter.login(p);
			}

			@Override
			public void authFail() {
				dialog.dismiss();
				mStartPresenter.notFingerprintLogin(getGesture());
				finish();
			}
		});
	}


	//密码验证成功后的异地登录弹窗
	public void showPrompt() {
		Dialog mDialog = new CustomDialog.Builder(mContext).setMessage(R.string.login_again2).setIcon(R.drawable.ic_warining).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				mStartPresenter.clearDeviceID(mFingerprintPhone);
			}
		}).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				System.exit(0);
			}
		}).create();
		mDialog.setCancelable(false);
		mDialog.show();
	}

	public void setAgainLogin() {
		try {
			Map<String, String> p = new HashMap<>();
			Session.deviceID = Utils.getDeviceID(mContext);
			p.put("phone", mFingerprintPhone);
			p.put("password", "");
			p.put("version_name", LibSession.sVersionName);
			p.put("device_id", Session.deviceID);
			p.put("is_device", Session.isSessionTimeOut + "");
			p.put("use_fingerprint", "true");
			mStartPresenter.login(p);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public void toMainActivity() {
		Intent it = new Intent(this, MainActivity.class);
		startActivity(it);
		finish();
	}

	@Override
	public void showLogin() {
		Intent intent = new Intent(StartActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void finishSelf() {
		finish();
	}
}
