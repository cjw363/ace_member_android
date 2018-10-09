package com.ace.member.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.ace.member.R;
import com.ace.member.bean.UserCommunication;
import com.ace.member.utils.M;
import com.ace.member.utils.Session;
import com.google.gson.Gson;
import com.og.http.SocketResponse;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import de.tavendo.autobahn.WebSocketOptions;

public class WebSocketService extends Service {

	private static final String TAG = WebSocketService.class.getSimpleName();

	public static final String WEB_SOCKET_ACTION = "com.ace.member.WEB_SOCKET_ACTION";

	private static BroadcastReceiver mConnectionReceiver;
	private static boolean mIsClosed = true;
	private static WebSocketConnection mWebSocketConnection;
	private static WebSocketOptions mOptions = new WebSocketOptions();
	private static WebSocketHandler mWsHandler;

	private static String mSocketHost = ""; //webSocket服务端的url

	private static long sendTime = 0L;
	private static final long HEART_BEAT_RATE = 30000;//心跳检测时间，30秒

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {
			if (intent != null) {
				Bundle b = intent.getExtras();
				if (b != null) {
					mSocketHost = b.getString("socket_host", "");
				}
				if (mConnectionReceiver == null) {
					mConnectionReceiver = new BroadcastReceiver() {
						@Override
						public void onReceive(Context context, Intent intent) {
							ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
							NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

							if (networkInfo == null || !networkInfo.isAvailable()) {
								Utils.showToast(R.string.connect_timeout);
							} else {
								mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);// 初始化成功后，就准备发送心跳包
								if (mWebSocketConnection != null) {
									mWebSocketConnection.disconnect();
								}
								if (mIsClosed) {
									connect();
								}
							}

						}
					};
				}
				IntentFilter intentFilter = new IntentFilter();
				intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
				registerReceiver(mConnectionReceiver, intentFilter);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	// 发送心跳包
	private Handler mHandler = new Handler();
	private Runnable heartBeatRunnable = new Runnable() {
		@Override
		public void run() {
			resetConnect();
			if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
				sendMsg("");// 就发送一个\r\n过去
			}
			mHandler.postDelayed(this, HEART_BEAT_RATE);
		}
	};

	//重置连接
	private static void resetConnect() {
		if (!TextUtils.isEmpty(mSocketHost)) {
			if (mWebSocketConnection != null && !mWebSocketConnection.isConnected()) {
				try {
					mWebSocketConnection.connect(mSocketHost, mWsHandler, mOptions);
				} catch (WebSocketException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public static void closeWebSocket() {
		try {
			mIsClosed = true;
			if (mWebSocketConnection != null && mWebSocketConnection.isConnected()) {
				mWebSocketConnection.disconnect();
				mWebSocketConnection = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void connect() {
		mWebSocketConnection = new WebSocketConnection();
		try {
			mWsHandler = new WebSocketHandler() {

				//webSocket启动时候的回调
				@Override
				public void onOpen() {
					mIsClosed = false;
					UserCommunication comm = new UserCommunication();
					comm.setSource(Session.user.getType() + "+" + Session.user.getId());
					comm.setTarget("");
					Gson gson = new Gson();
					String commList = gson.toJson(comm);
					sendMsg(commList);

					//连接成功回调事件
					//EventBusUtil.post(new WebSocketEvent());
				}

				//webSocket接收到消息后的回调
				@Override
				public void onTextMessage(String data) {
					try {
						Log.i(TAG, data);
						if (!TextUtils.isEmpty(data)) {
							SocketResponse response = JsonUtil.jsonToBean(data, SocketResponse.class);
							assert response != null;
							switch (response.getCode()) {
								case M.SocketCode.SOCKET_CODE_1_FIRST_CONNECT:
								case M.SocketCode.SOCKET_CODE_2_DEPOSIT:
								case M.SocketCode.SOCKET_CODE_3_WITHDRAW:
								case M.SocketCode.SOCKET_CODE_5_RECEIVE_MONEY:
									Intent intent1 = new Intent(WEB_SOCKET_ACTION);
									intent1.putExtra("data", data);
									sendBroadcast(intent1);
									break;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						FileUtils.addErrorLog(e);
					}
				}

				//webSocket关闭时候的回调
				@Override
				public void onClose(int code, String reason) {
					mIsClosed = true;
					switch (code) {
						case 1:
						case 2://网络断开连接
						case 3://手动断开连接
						case 4:
						case 5://Connection reset by peer
							resetConnect();
							break;
					}
				}
			};
			if (!TextUtils.isEmpty(mSocketHost))
				mWebSocketConnection.connect(mSocketHost, mWsHandler, mOptions);
		} catch (WebSocketException e) {
			e.printStackTrace();
		}
	}

	public static void sendMsg(String s) {
		try {
			if (!TextUtils.isEmpty(s)) {
				if (mWebSocketConnection != null) {
					resetConnect();
					mWebSocketConnection.sendTextMessage(s);
					sendTime = System.currentTimeMillis();// 每次发送成功数据，就改一下最后成功发送的时间，节省心跳间隔时间
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendMsgWithUid(int memberId) {
		try {
			if (memberId != 0) {
				if (mWebSocketConnection != null) {
					resetConnect();
					UserCommunication conn = new UserCommunication();
					conn.setSource(Session.user.getType() + "+" + Session.user.getId());
					conn.setTarget(Session.user.getType() + "+" + memberId);
					Gson gson = new Gson();
					String message = gson.toJson(conn);
					mWebSocketConnection.sendTextMessage(message);
					sendTime = System.currentTimeMillis();// 每次发送成功数据，就改一下最后成功发送的时间，节省心跳间隔时间
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			if (mConnectionReceiver != null) {
				unregisterReceiver(mConnectionReceiver);
				mConnectionReceiver = null;
				mHandler.removeCallbacks(heartBeatRunnable);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}