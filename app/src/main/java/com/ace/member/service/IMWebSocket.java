package com.ace.member.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.og.utils.FileUtils;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import de.tavendo.autobahn.WebSocketOptions;

public class IMWebSocket extends Service {
	private static final String TAG = WebSocketService.class.getSimpleName() + "-----------------------";

	public static final String WEB_SOCKET_CHAT_LIST = "com.ace.member.WEB_SOCKET_CHAT_LIST";

	private boolean isClosed = true;
	private WebSocketConnection webSocketConnection;
	private String webSocketHost = "";


	@Override
	public IBinder onBind(Intent intent) {
		return new IMBinder();
	}

	/**
	 * 通过Binder 操作IMWebSocket
	 */
	public class IMBinder extends Binder {
		public void sendMessage() {

		}
	}

	/**
	 * 多次bindService，不会调用该方法。startService会调用
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		connectWebSocket();
		return super.onStartCommand(intent, flags, startId);
	}

	public void closeWebSocket() {
		if (webSocketConnection != null && webSocketConnection.isConnected()) {
			webSocketConnection.disconnect();
			webSocketConnection = null;
		}
	}

	public void connectWebSocket() {
		if (webSocketConnection != null && webSocketConnection.isConnected()) {
			webSocketConnection.disconnect();
		}
		if (isClosed) {
			webSocketConnection = new WebSocketConnection();
			IMWebSocketHandler IMWebSocketHandler = new IMWebSocketHandler();
			WebSocketOptions options = new WebSocketOptions();
			options.setSocketConnectTimeout(30000);
			options.setSocketReceiveTimeout(30000);
			try {
				webSocketConnection.connect(webSocketHost, IMWebSocketHandler, options);
			} catch (WebSocketException e) {
				FileUtils.addErrorLog(e);
				e.printStackTrace();
			}
		}
	}

	private class IMWebSocketHandler extends WebSocketHandler {
		@Override
		public void onOpen() {
			Log.d(TAG, "open");
			isClosed = false;
		}

		@Override
		public void onTextMessage(String msg) {
			Log.d(TAG, "msg = " + msg);
		}

		@Override
		public void onClose(int code, String reason) {
			isClosed = true;
			Log.d(TAG, "code = " + code + " reason = " + reason);
			switch (code) {
				case 1:
					break;
				case 2:
					break;
				case 3://手动断开连接
					break;
				case 4:
					break;
				case 5://网络断开连接
					connectWebSocket();
					break;
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
