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
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.BaseApplication;
import com.ace.member.utils.Session;
import com.google.gson.Gson;
import com.og.http.SocketResponse;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import de.tavendo.autobahn.WebSocketOptions;

public class IMWebSocketService extends Service {

	private static final String TAG = IMWebSocketService.class.getSimpleName() + "-----------------------";

	public static final String WEB_SOCKET_CHAT_LIST = "com.ace.member.WEB_SOCKET_CHAT_LIST";

	private static BroadcastReceiver connectionReceiver;
	private static boolean isClosed = true;
	private static WebSocketConnection webSocketConnection;
	private static WebSocketOptions options = new WebSocketOptions();
	private static WebSocketHandler mWsHandler;

	private static String mSocketHost = ""; //webSocket服务端的url

	private static long sendTime = 0L;
	private static final long HEART_BEAT_RATE = 30000;//心跳检测时间，30秒

	public static final int IM_SOCKET_TYPE_11_CHAT_MSG = 11;//聊天消息
	public static final int IM_SOCKET_TYPE_22_REQUEST_MSG = 22;//好友请求消息

	private static List<String> unSendMsgList = Collections.synchronizedList(new ArrayList<String>());//用来记录未发送成功的消息

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {
			if (intent != null) {
				Bundle b = intent.getExtras();
				if (b != null) {
					mSocketHost = b.getString("socket_host", "");
				}
				if (connectionReceiver == null) {
					connectionReceiver = new BroadcastReceiver() {
						@Override
						public void onReceive(Context context, Intent intent) {
							ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
							NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

							if (networkInfo == null || !networkInfo.isAvailable()) {
								Utils.showToast(R.string.connect_timeout);
							} else {
								mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);// 初始化成功后，就准备发送心跳包
								if (webSocketConnection != null) {
									webSocketConnection.disconnect();
								}
								if (isClosed) {
									connect();
								}
							}

						}
					};
				}
				IntentFilter intentFilter = new IntentFilter();
				intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
				registerReceiver(connectionReceiver, intentFilter);
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	// 发送心跳包
	private static Handler mHandler = BaseApplication.getHandler();
	private Runnable heartBeatRunnable = new Runnable() {
		@Override
		public void run() {
			Log.d(TAG, "状态" + webSocketConnection.isConnected());
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
			if (webSocketConnection != null && !webSocketConnection.isConnected()) {
				try {
					webSocketConnection = new WebSocketConnection();
					webSocketConnection.connect(mSocketHost, mWsHandler, options);
				} catch (WebSocketException e) {
					FileUtils.addErrorLog(e);
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

	public static void closeConnect() {
		try {
			isClosed = true;
			if (webSocketConnection != null && webSocketConnection.isConnected()) {
				webSocketConnection.disconnect();
				webSocketConnection = null;
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	public void connect() {
		webSocketConnection = new WebSocketConnection();
		try {
			mWsHandler = new WebSocketHandler() {

				//webSocket启动时候的回调
				@Override
				public void onOpen() {
					Log.d(TAG, "open成功连上");
					isClosed = false;
					UserCommunication comm = new UserCommunication();
					comm.setSource(Session.user.getType() + "+" + Session.user.getId());
					comm.setTarget("");
					Gson gson = new Gson();
					String commList = gson.toJson(comm);
					sendMsg(commList);

					if (Utils.isEmptyList(unSendMsgList)) {
						for (String msg : unSendMsgList) {
							Log.d(TAG, "发送未发送的消息");
							webSocketConnection.sendTextMessage(msg);
							sendTime = System.currentTimeMillis();// 每次发送成功数据，就改一下最后成功发送的时间，节省心跳间隔时间
						}
						unSendMsgList.clear();
					}
				}

				//webSocket接收到消息后的回调
				@Override
				public void onTextMessage(String data) {
					Log.d(TAG, "收到msg = " + data);
					try {
						if (!TextUtils.isEmpty(data)) {
							SocketResponse response = JsonUtil.jsonToBean(data, SocketResponse.class);
							assert response != null;
							String message = JsonUtil.beanToJson(response.getResult());
							Intent intent = new Intent(WEB_SOCKET_CHAT_LIST);
							intent.putExtra("data", message);
							sendBroadcast(intent);
						}
					} catch (Exception e) {
						e.printStackTrace();
						FileUtils.addErrorLog(e);
					}
				}

				//webSocket关闭时候的回调
				@Override
				public void onClose(int code, String reason) {
					Log.d(TAG, "断了code = " + code + " reason = " + reason);
					isClosed = true;
					switch (code) {
						case 1:
						case 2://网络断开连接,不然会一直调用
							break;
						case 3://手动断开连接
						case 4:
						case 5://Connection reset by peer
							closeConnect();
							resetConnect();
							break;
					}
				}
			};
			if (!TextUtils.isEmpty(mSocketHost))
				webSocketConnection.connect(mSocketHost, mWsHandler, options);
		} catch (WebSocketException e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	public static void sendMsg(String s) {
		try {
			if (!TextUtils.isEmpty(s)) {
				if (webSocketConnection != null) {
					if (!webSocketConnection.isConnected()) {//断了
						unSendMsgList.add(s);//保存未发送的消息
						resetConnect();
					} else {
						webSocketConnection.sendTextMessage(s);
						sendTime = System.currentTimeMillis();// 每次发送成功数据，就改一下最后成功发送的时间，节省心跳间隔时间
					}
				}
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	public static void sendFriendMsg(int userId, int imType) {
		try {
			if (userId != 0) {
				if (webSocketConnection != null) {
					UserCommunication conn = new UserCommunication();
					conn.setSource(Session.user.getType() + "+" + Session.user.getId());
					conn.setTarget(AppGlobal.USER_TYPE_1_MEMBER + "+" + userId);
					conn.setType(imType);
					Gson gson = new Gson();
					String message = gson.toJson(conn);
					if (!webSocketConnection.isConnected()) {//断了
						unSendMsgList.add(message);//保存未发送的消息
						resetConnect();
					} else {
						webSocketConnection.sendTextMessage(message);
						sendTime = System.currentTimeMillis();// 每次发送成功数据，就改一下最后成功发送的时间，节省心跳间隔时间
					}
				}
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			if (connectionReceiver != null) {
				unregisterReceiver(connectionReceiver);
				connectionReceiver = null;
				mHandler.removeCallbacks(heartBeatRunnable);
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}
}