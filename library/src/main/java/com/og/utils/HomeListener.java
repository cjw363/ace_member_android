package com.og.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class HomeListener {
	static final String TAG = "HomeListener";
	private Context mContext;
	private IntentFilter mFilter;
	private OnHomePressedListener mListener;
	private InnerReceiver mReceiver;

	// 回调接口
	public interface OnHomePressedListener {
		public void onHomePressed();

		public void onHomeLongPressed();
	}

	public HomeListener(Context context) {
		mContext = context;
		mFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
	}

	/**
	 * 设置监听
	 *
	 * @param listener
	 */
	public void setOnHomePressedListener(OnHomePressedListener listener) {
		mListener = listener;
		mReceiver = new InnerReceiver();
	}

	/**
	 * 开始监听，注册广播
	 */
	public void startWatch() {
		try{
			if (mReceiver != null && mContext != null) {
				mContext.registerReceiver(mReceiver, mFilter); //这里也会报错，所以放到try...catch
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 停止监听，注销广播
	 */
	public void stopWatch() {
		try{
			if (mReceiver != null && mContext != null) {
				mContext.unregisterReceiver(mReceiver);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 广播接收者
	 */
	class InnerReceiver extends BroadcastReceiver {
		final String SYSTEM_DIALOG_REASON_KEY = "reason";
		final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
		final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
		final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
				String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
				if (reason != null) {
//                    Log.e(TAG, "action:" + action + ",reason:" + reason);
					if (mListener != null) {
						if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
							// 短按home键
							mListener.onHomePressed();
						} else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
							// 长按home键
							mListener.onHomeLongPressed();
						}
					}
				}
			}
		}
	}
}
