package com.ace.member.base;


import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;

import com.ace.member.BuildConfig;
import com.ace.member.R;
import com.ace.member.main.me.system_update.SystemUpdateActivity;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.M;
import com.ace.member.utils.Session;
import com.ace.member.utils.SnackBarUtil;
import com.og.LibApplication;
import com.og.LibGlobal;
import com.og.LibSession;
import com.og.event.MessageEvent;
import com.og.event.ToastEvent;
import com.og.update.PermissionsChecker;
import com.og.utils.CustomDialog;
import com.og.utils.DialogFactory;
import com.og.utils.FileUtils;
import com.og.utils.FingerprintHelper;
import com.og.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {

	static {
		AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
	}

	public SharedPreferences mPreferencesUser;
	public SharedPreferences mPreferencesData; //保存到这来的信息不会被清空.
	protected Context mContext;
	//要加入6.0以后的限制权限
	protected int PERMISSION_REQUEST_CODE = 0; // 系统权限管理页面的参数
	protected String[] PERMISSIONS;//= new String[]{android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
	protected PermissionsChecker mPermissionsChecker;
	private String TAG = "BaseActivity";
	private Unbinder mUnbinder;

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPermissionsChecker = new PermissionsChecker(this);
		mContext = this;
		setContentView(getContentViewID());
		mUnbinder = ButterKnife.bind(this);
		mPreferencesUser = getSharedPreferences(BuildConfig.PREFERENCES_USER, Context.MODE_PRIVATE);
		mPreferencesData = getSharedPreferences(LibGlobal.PRE_DATA_KEY, MODE_PRIVATE);
		EventBus.getDefault().register(this);
	}

	protected abstract int getContentViewID();

	@Override
	protected void onStart() {
		super.onStart();
		if (mPreferencesUser == null) {
			mPreferencesUser = getSharedPreferences(BuildConfig.PREFERENCES_USER, MODE_PRIVATE);
		}
		if (mPreferencesData == null) {
			mPreferencesData = getSharedPreferences(LibGlobal.PRE_DATA_KEY, MODE_PRIVATE);
		}
	}

	protected void initActivity() {
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onMessageEvent(MessageEvent messageEvent) {
		int code = messageEvent.getCode();
		switch (code) {
			case com.og.M.MessageCode.ERR_401_NETWORK_INVALID:
			case com.og.M.MessageCode.ERR_402_NETWORK_ERROR:
				showNetworkError(mContext.getString(R.string.network_error));
				break;
			case com.og.M.MessageCode.ERR_403_NETWORK_TIMEOUT:
				showNetworkError(mContext.getString(R.string.connect_timeout));
				break;
			case com.og.M.MessageCode.ERR_420_DATA_ERROR:
				showNetworkError(mContext.getString(R.string.result_converting_error));
				break;
			case com.og.M.MessageCode.ERR_421_DATA_PARSING_ERROR:
				showNetworkError(mContext.getString(R.string.result_parsing_error));
				break;
			case com.og.M.MessageCode.ERR_400_FATAL_ERROR:
				String result = messageEvent.getStr();
				showNetworkError(result);
				break;
			case com.og.M.MessageCode.ERR_102_SESSION_TIMEOUT:
				String str = mContext.getResources().getString(R.string.msg_102);
				showPrompt(str);
				break;
			//			case LibGlobal.MSG_9904_GESTURE_LOCK:
			//				//弹出手势锁,成功就关闭手势锁,失败就退回登录页面
			//				gotoLogin();
			//				break;
			case com.og.M.MessageCode.ERR_430_SESSION_TIME_OUT_TO_LOGIN: //TODO NOT USED?
				restartApp();
				finish();
				break;
			case com.og.M.MessageCode.ERR_441_CLOSE_BLOCKING_DIALOG:
				DialogFactory.unblock();
				break;
			case com.og.M.MessageCode.ERR_440_SAVE_GESTURE_FAIL:
				saveGestureFail();
				break;
			case com.og.M.MessageCode.ERR_500_NOT_ALLOW_LOGIN:
				String s = AppUtils.getFunctionPauseMsg(M.FunctionCode.FUNCTION_102_MEMBER_LOGIN_ANDROID);
				showPrompt(s, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Session.isSessionTimeOut = 1;
					}
				});
				break;
			case M.MessageCode.ERR_1002_LOGIN_FAIL:
				loginFail();
				break;
			case M.MessageCode.ERR_1003_GESTURE_LOGIN_FAIL:
				gestureLoginFail();
				break;
			case com.og.M.MessageCode.ERR_104_NOT_SUPPORTED_APK_VERSION:
				showVersionError();
				break;
			default:
				break;
		}
	}

	protected void loginFail() {
	}

	protected void gestureLoginFail() {
	}

	protected void saveGestureFail() {
	}

	public void showPrompt(String str) {
		try {
			Dialog mDialog = new CustomDialog.Builder(mContext).setMessage(str)
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Session.isSessionTimeOut = 1;
						restartApp();
					}
				})
				.create();
			mDialog.setCancelable(false);
			mDialog.show();
		} catch (Exception e) {
			e.printStackTrace();
			FileUtils.addErrorLog(e);
		}
	}

	public void showPrompt(String str, DialogInterface.OnClickListener onClickListener) {
		try {
			Dialog mDialog = new CustomDialog.Builder(mContext).setMessage(str)
				.setIcon(R.drawable.ic_warining)
				.setPositiveButton(R.string.ok, onClickListener)
				.create();
			mDialog.setCancelable(false);
			mDialog.show();
		} catch (Exception e) {
			e.printStackTrace();
			FileUtils.addErrorLog(e);
		}
	}

	public void showVersionError() {
		try {
			String msg = M.get(mContext, com.og.M.MessageCode.ERR_104_NOT_SUPPORTED_APK_VERSION);
			Dialog mDialog = new CustomDialog.Builder(mContext, R.layout.dialog_error_layout).setMessage(msg)
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						LibSession.sVersionStatus = LibGlobal.APK_VERSION_STATUS_2_UNSUPPORTED;
						Intent it = new Intent(mContext, SystemUpdateActivity.class);
						startActivity(it);
					}
				}).create();
			mDialog.setCancelable(false);
			mDialog.show();
		} catch (Exception e) {
			e.printStackTrace();
			FileUtils.addErrorLog(e);
		}
	}

	public void showNetworkError(String msg) {
		if (msg == null || msg.equals("") || mContext == null) return;
		DialogFactory.ToastDialog2(mContext, msg, 0, R.layout.dialog_error_layout, R.color.clr_error_red);
	}

	//	public void showErrorStr(String msg){
	//		if(TextUtils.isEmpty(msg)||msg.equals("")||mContext==null) return;
	//		DialogFactory.ToastDialog(mContext,"",msg,0);
	//	}

	protected void restartApp() {
		try {
			Intent i = getPackageManager().getLaunchIntentForPackage(getPackageName());
			assert i != null;
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.addCategory(Intent.CATEGORY_HOME);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public void onBackPressed() {
		try {
			DialogFactory.unblock();
			super.onBackPressed();
		} catch (Exception e) {
			e.printStackTrace();
			FileUtils.addErrorLog(e);
		}
	}

	public void clearFingerprintLoginData() {
		try {
			FingerprintHelper helper = new FingerprintHelper.Login(mContext);
			helper.clearData();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	public void clearFingerprintPayData() {
		try {
			FingerprintHelper helper = new FingerprintHelper.Pay(mContext);
			helper.clearData();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	protected void onDestroy() {
		try {
			DialogFactory.unblock();
			EventBus.getDefault().unregister(this);
			super.onDestroy();
			if(mUnbinder != null) mUnbinder.unbind();
			setContentView(R.layout.view_null);
		} catch (Exception e) {
			e.printStackTrace();
			FileUtils.addErrorLog(e);
		}
	}

	protected void exitApp() {
		try {
			int currentVersion = android.os.Build.VERSION.SDK_INT;
			if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
				Intent startMain = new Intent(Intent.ACTION_MAIN);
				startMain.addCategory(Intent.CATEGORY_HOME);
				startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(startMain);
				Process.killProcess(LibApplication.getMyPid());
				System.exit(0);
			} else {// android2.1
				ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
				am.restartPackage(getPackageName());
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}


	protected void setPermissions() {
		//		PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
		try {
			ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);
		} catch (Exception e) {
			e.printStackTrace();
			FileUtils.addErrorLog(e);
		}
	}

	@Subscribe
	public void onToastEvent(ToastEvent event) {
		if (event != null) {
			if (!TextUtils.isEmpty(event.getMsg())) {
				if (event.getDuration() >= -1) {
					SnackBarUtil.show(getWindow().findViewById(android.R.id.content), event.getMsg(), event.getDuration(), event
						.getCallback());
				} else {
					SnackBarUtil.show(getWindow().findViewById(android.R.id.content), event.getMsg(), event.getCallback());
				}
			} else if (event.getMsgRes() > 0) {
				if (event.getDuration() >= -1) {
					SnackBarUtil.show(getWindow().findViewById(android.R.id.content), event.getMsgRes(), event
						.getDuration(), event.getCallback());
				} else {
					SnackBarUtil.show(getWindow().findViewById(android.R.id.content), event.getMsgRes(), event
						.getCallback());
				}
			}
		}
	}

	public void chkTradingPasswordStatus() {
		if (Session.user.getStatusTradingPassword() == AppGlobal.TRADING_PASSWORD_STATUS_4_TO_SET) {
			Utils.showToast(R.string.please_set_trading_password);
		}
	}
}
