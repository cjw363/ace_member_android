package com.ace.member.start;

import android.content.Context;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.ace.member.R;
import com.ace.member.base.LoginBasePresenter;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.M;
import com.ace.member.utils.Session;
import com.og.LibGlobal;
import com.og.LibSession;
import com.og.event.MessageEvent;
import com.og.utils.DialogFactory;
import com.og.utils.FileUtils;
import com.og.utils.FingerprintHelper;
import com.og.utils.Utils;

import javax.inject.Inject;

public class StartPresenter extends LoginBasePresenter implements StartContract.StartPresenter {
	@NonNull
	private final StartContract.StartView mStartView;

	@Inject
	public StartPresenter(@NonNull StartContract.StartView mStartView, Context context) {
		super(context);
		this.mStartView = mStartView;
	}

	@Override
	public void start() {
		long start = System.currentTimeMillis();
		getVersionInfo();
		mStartView.setVersion(String.format(Utils.getString(R.string.version_name), LibSession.sVersionName, (LibSession.sServiceVersion.equals("") ? "..." : LibSession.sServiceVersion)));
		mStartView.showBlock(Utils.getString(R.string.checking_update));
		long interval = System.currentTimeMillis() - start;
		Utils.runOnUIThread(new Runnable() {
			@Override
			public void run() {
				mStartView.showCheckForDialog();
			}
		}, interval > 1500 ? 0 : 1500 - interval);
	}

	@Override
	public void onMessageEvent(MessageEvent messageEvent) {
		int code = messageEvent.getCode();
		Message msg = messageEvent.getMsg();
		switch (code) {
			case com.og.M.MessageCode.ERR_413_DOWNLOAD_FINISH:
				mStartView.finishSelf();
				break;
			case com.og.M.MessageCode.ERR_414_CANCEL_DOWNLOAD:
			case com.og.M.MessageCode.ERR_411_UPDATE_ERROR:
			case com.og.M.MessageCode.ERR_415_NO_UPDATE:
				if (Session.sVersionStatus == LibGlobal.APK_VERSION_STATUS_2_UNSUPPORTED) {
					mStartView.showVersionNotSupported();
				} else {
					mStartView.hideVersionNotSupported();
					attemptLogin(mStartView.getGesture());
				}
				break;
			case com.og.M.MessageCode.ERR_416_NEW_VER_FOUND:
				DialogFactory.unblock();
				Session.hasNewVersion = true;
				break;
			case com.og.M.MessageCode.ERR_442_GET_SERVICE_INFO_SUCCESS:
				LibSession.sFlagDev = (msg.getData().getInt("flag_dev") == 1);
				if (TextUtils.isEmpty(LibSession.sServiceVersion)) {
					if (LibSession.sFlagDev)
						LibSession.sServiceVersion = msg.getData().getString("version") + " DEV";
					else LibSession.sServiceVersion = msg.getData().getString("version");
				}
				mStartView.setVersion(String.format(Utils.getString(R.string.version_name), LibSession.sVersionName, LibSession.sServiceVersion));
				boolean supported = msg.getData().getBoolean("supported", false);
				Session.sVersionStatus = supported ? LibGlobal.APK_VERSION_STATUS_1_SUPPORTED : LibGlobal.APK_VERSION_STATUS_2_UNSUPPORTED;
				break;
			default:
				mStartView.superMessageEvent(messageEvent);
				break;
		}
	}

	protected void onFailure(int code) {
		try {
			if (code == M.MessageCode.ERR_1001_LOGGED_ON_OTHER_DEVICE) {
				mStartView.showPrompt();
			} else if (code > 0) {
				showFailError(M.get(mContext, code), M.MessageCode.ERR_1002_LOGIN_FAIL);
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	//指纹登录成功后执行的,其它方式的登录不调用
	@Override
	protected void onSuccess(String gesture, boolean resetPassword) {
		try {
			mStartView.toMainActivity();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private void attemptLogin(String gesture) {
		boolean canShowFingerprint = false;
		FingerprintHelper helper = new FingerprintHelper.Login(mContext);
		Boolean isUseFingerprint = helper.getIsUseFingerprint();
		String dataPhone = "";
		try {
			if (isUseFingerprint) {
				String fingerprintPhone = helper.decryptFingerprintData();
				dataPhone = helper.getData(FingerprintHelper.originalPhone);

				if (fingerprintPhone == null) {
					helper.setIsUseFingerprint(false);
					addFingerprintLog(AppGlobal.ACTION_TYPE_3_SWITCH_OFF);
				} else if (helper.checkAvailable(false) && !fingerprintPhone.isEmpty() && fingerprintPhone.equals(dataPhone)) {
					canShowFingerprint = true;
				}
			}
		} catch (Exception e) {
			notFingerprintLogin(gesture);
		}
		if (canShowFingerprint && !dataPhone.equals("")) mStartView.showFingerprintDialog(dataPhone);
		else notFingerprintLogin(gesture);
	}

	public void notFingerprintLogin(String gesture) {
		if (!TextUtils.isEmpty(gesture)) {
			mStartView.showUnlock();
		} else {
			mStartView.showLogin();
		}
	}

	protected void againLogin() {
		mStartView.setAgainLogin();
	}



}
