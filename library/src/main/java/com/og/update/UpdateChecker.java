package com.og.update;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.og.LibGlobal;
import com.og.M;
import com.og.R;
import com.og.event.MessageEvent;
import com.og.http.OkHttpClientManager;
import com.og.utils.DialogFactory;
import com.og.utils.NetUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import okhttp3.Request;

public class UpdateChecker extends Fragment {
	private static final String NOTICE_TYPE_KEY = "type";
	private static final String APP_UPDATE_SERVER_URL = "app_update_server_url";
	private static final int NOTICE_NOTIFICATION = 2;
	private static final int NOTICE_DIALOG = 1;
	private static final String TAG = "UpdateChecker";

	private FragmentActivity mContext;
	private int mTypeOfNotice;
	private Boolean mIsCheckOnly = false;

	/**
	 * Show a Dialog if an update is available for download. Callable in a
	 * FragmentActivity. Number of checks after the dialog will be shown:
	 * default, 5
	 *
	 * @param fragmentActivity Required.
	 */
	public void checkForDialog(FragmentActivity fragmentActivity, String url, boolean isCheckOnly) {
		mIsCheckOnly = isCheckOnly;
		checkForDialog(fragmentActivity, url);
	}

	public void checkForDialog(FragmentActivity fragmentActivity, String url) {
		try {
			FragmentTransaction content = fragmentActivity.getSupportFragmentManager().beginTransaction();
			if (!this.isAdded()) {
				Bundle args = new Bundle();
				args.putInt(NOTICE_TYPE_KEY, NOTICE_DIALOG);
				args.putString(APP_UPDATE_SERVER_URL, url);
				args.putBoolean("flagCheckOnly", mIsCheckOnly);
				this.setArguments(args);
				content.add(this, null).commitAllowingStateLoss();
			} else {
				content.remove(this);
				content.add(this, null).commitAllowingStateLoss();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//args.putInt(SUCCESSFUL_CHECKS_REQUIRED_KEY, 5);
	}

	/**
	 * Show a Notification if an update is available for download. Callable in a
	 * FragmentActivity Specify the number of checks after the notification will
	 * be shown.
	 *
	 * @param fragmentActivity Required.
	 */
	public static void checkForNotification(FragmentActivity fragmentActivity, String url) {
		FragmentTransaction content = fragmentActivity.getSupportFragmentManager().beginTransaction();
		UpdateChecker updateChecker = new UpdateChecker();
		Bundle args = new Bundle();
		args.putInt(NOTICE_TYPE_KEY, NOTICE_NOTIFICATION);
		args.putString(APP_UPDATE_SERVER_URL, url);
		//args.putInt(NOTIFICATION_ICON_RES_ID_KEY, notificationIconResId);
		//args.putInt(SUCCESSFUL_CHECKS_REQUIRED_KEY, 5);
		updateChecker.setArguments(args);
		content.add(updateChecker, null).commit();
	}


	/**
	 * This class is a Fragment. Check for the method you have chosen.
	 */
//	@Override
//	public void onAttach(Activity activity) {
//		super.onAttach(activity);
//		mContext = (FragmentActivity) activity;
//		Bundle args = getArguments();
//		mTypeOfNotice = args.getInt(NOTICE_TYPE_KEY);
//		String url = args.getString(APP_UPDATE_SERVER_URL);
//		//mSuccessfulChecksRequired = args.getInt(SUCCESSFUL_CHECKS_REQUIRED_KEY);
//		//mNotificationIconResId = args.getInt(NOTIFICATION_ICON_RES_ID_KEY);
//		checkForUpdates(url);
//	}

	@Override
	public void onAttach(Context context) {
		try {
			super.onAttach(context);
			mContext = (FragmentActivity) context;
//		mContext = getActivity();
			Bundle args = getArguments();
			mTypeOfNotice = args.getInt(NOTICE_TYPE_KEY);
			String url = args.getString(APP_UPDATE_SERVER_URL);
			mIsCheckOnly = args.getBoolean("flagCheckOnly");
			//mSuccessfulChecksRequired = args.getInt(SUCCESSFUL_CHECKS_REQUIRED_KEY);
			//mNotificationIconResId = args.getInt(NOTIFICATION_ICON_RES_ID_KEY);
			checkForUpdates(url);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Heart of the library. Check if an update is available for download
	 * parsing the desktop Play Store page of the app
	 */
	private void checkForUpdates(final String url) {
		try {
			if (mContext != null && !NetUtils.isNetworkAvailable(mContext)) {
				EventBus.getDefault().post(new MessageEvent(M.MessageCode.ERR_401_NETWORK_INVALID));
				DialogFactory.unblock();
				return;
			}

			OkHttpClientManager.getAsyn(url, new OkHttpClientManager.ResultCallback<String>() {
				@Override
				public void onError(Request request, Exception e) {
					Log.e(TAG, " Get Update Info Error: " + e.getMessage() + ", url: " + url);
					EventBus.getDefault().post(new MessageEvent(M.MessageCode.ERR_411_UPDATE_ERROR));
				}

				@Override
				public void onResponse(String response) {
					parseJson(response);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void parseJson(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			String updateMessage = obj.getString(LibGlobal.APK_UPDATE_CONTENT);
			String apkUrl = obj.getString(LibGlobal.APK_DOWNLOAD_URL);
			String apkName = obj.getString(LibGlobal.APK_NAME);
			int apkCode = obj.getInt(LibGlobal.APK_VERSION_CODE);

			int versionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;

			DialogFactory.unblock();
			if (apkCode > versionCode) {
				EventBus.getDefault().post(new MessageEvent(M.MessageCode.ERR_416_NEW_VER_FOUND));
				if (mIsCheckOnly) return;

				if (mTypeOfNotice == NOTICE_NOTIFICATION) {
					showNotification(updateMessage, apkUrl);
				} else if (mTypeOfNotice == NOTICE_DIALOG) {
//					showDialog(updateMessage, apkUrl, apkName);
					showActivity(updateMessage, apkUrl, apkName);
				}
			} else {
				EventBus.getDefault().post(new MessageEvent(M.MessageCode.ERR_415_NO_UPDATE));
			}

		} catch (PackageManager.NameNotFoundException ignored) {
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "parseMediaType json error", e);
		}
	}

	/**
	 * Show dialog
	 */
	public void showDialog(String content, String apkUrl, String apkName) {
		try {
			UpdateDialog d = new UpdateDialog();
			Bundle args = new Bundle();
			args.putString(LibGlobal.APK_UPDATE_CONTENT, content);
			args.putString(LibGlobal.APK_DOWNLOAD_URL, apkUrl);
			args.putString(LibGlobal.APK_NAME, apkName);
			d.setArguments(args);
			d.show(mContext.getSupportFragmentManager(), null);
		} catch (Exception e) {
			EventBus.getDefault().post(new MessageEvent(M.MessageCode.ERR_411_UPDATE_ERROR)); //TODO, 锁屏状态下编译时，这里会抛异常
			e.printStackTrace();
		}
	}

	/**
	 * Show Activity
	 */
	public void showActivity(String content, String apkUrl, String apkName) {
		try {
			Bundle args = new Bundle();
			args.putString(LibGlobal.APK_UPDATE_CONTENT, content);
			args.putString(LibGlobal.APK_DOWNLOAD_URL, apkUrl);
			args.putString(LibGlobal.APK_NAME, apkName);
			Intent intent = new Intent(getActivity(),UpdateActivity.class);
			intent.putExtras(args);
			getActivity().startActivity(intent);
		} catch (Exception e) {
			EventBus.getDefault().post(new MessageEvent(M.MessageCode.ERR_411_UPDATE_ERROR)); //TODO, 锁屏状态下编译时，这里会抛异常
			e.printStackTrace();
		}
	}

	/**
	 * Show Notification
	 */
	public void showNotification(String content, String apkUrl) {
		try {
			android.app.Notification noti;
			Intent myIntent = new Intent(mContext, DownloadService.class);
			myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			myIntent.putExtra(LibGlobal.APK_DOWNLOAD_URL, apkUrl);
			PendingIntent pendingIntent = PendingIntent.getService(mContext, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

			int smallIcon = mContext.getApplicationInfo().icon;
			noti = new NotificationCompat.Builder(mContext).setTicker(getString(R.string.new_update_available)).setContentTitle(getString(R.string.new_update_available)).setContentText(content).setSmallIcon(smallIcon).setContentIntent(pendingIntent).build();

			noti.flags = android.app.Notification.FLAG_AUTO_CANCEL;
			NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.notify(0, noti);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
