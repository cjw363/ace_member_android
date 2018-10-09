package com.ace.member.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.ace.member.service.WebSocketService;
import com.og.http.SocketResponse;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;

public abstract class BaseWebSocketActivity extends BaseActivity {

	private Intent mWebSocketServiceIntent;
	protected String mSocketHost = "";

	public BroadcastReceiver imReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			String data = intent.getStringExtra("data");
			SocketResponse response = JsonUtil.jsonToBean(data, SocketResponse.class);
			assert response != null;
			if (WebSocketService.WEB_SOCKET_ACTION.equals(action)) {
				backBroadcastReceiver(response.getCode(), JsonUtil.beanToJson(response.getResult()));
			}

		}
	};

	public void initActivity() {
		try {
//			if(mWebSocketServiceIntent == null){
//				Log.e("========", "initActivity:    " +11111);
//			}else {
//				Log.e("========", "initActivity:    " +22222);
//
//			}
			mWebSocketServiceIntent = new Intent(this, WebSocketService.class);
			Bundle b = new Bundle();
			b.putString("socket_host", mSocketHost);
			mWebSocketServiceIntent.putExtras(b);
			startService(mWebSocketServiceIntent);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public void onBackPressed() {
		try {
//			WebSocketService.closeWebSocket();
//			stopService(mWebSocketServiceIntent);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		super.onBackPressed();
	}


	/*
	* 这里result子类用实体解析去处理相关的业务
	* */
	public void backBroadcastReceiver(int code, String result) {
	}

	@Override
	protected void onStart() {
		super.onStart();
		try {
			IntentFilter filter = new IntentFilter(WebSocketService.WEB_SOCKET_ACTION);
			registerReceiver(imReceiver, filter);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			unregisterReceiver(imReceiver);
			stopService(mWebSocketServiceIntent);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}
}
