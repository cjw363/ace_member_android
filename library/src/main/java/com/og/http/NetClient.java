package com.og.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.og.LibSession;
import com.og.M;
import com.og.R;
import com.og.event.MessageEvent;
import com.og.utils.DialogFactory;
import com.og.utils.EventBusUtil;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.NetUtils;
import com.og.utils.Utils;

import java.util.Map;

import okhttp3.Request;

public class NetClient {
	private Context mContext;
	private String mUrl;
	private boolean isUnblock = true;

	public NetClient(Context context, String Url) {
		mContext = context;
		mUrl = Url;
	}

	public void requestServer(Map<String, String> params, final IDataRequestListener listener) {
		requestServer(params, listener, true);
	}

	public void requestServer(Map<String, String> params, final IDataRequestListener listener, final boolean showLoading) {
		try {
			if (mContext != null && !NetUtils.isNetworkAvailable(mContext)) {
				sendMessage(M.MessageCode.ERR_401_NETWORK_INVALID);
				return;
			}
			if (showLoading) {
				showBlock(R.string.waiting);
			}

			//暂时方便查看数据用，后期删除
			System.out.println(params.toString());

			OkHttpClientManager.postAsyn(mUrl, new OkHttpClientManager.ResultCallback<String>() {
				@Override
				public void onError(Request request, Exception e) {
					Log.e("NetClient", "Request Fail " + e.toString());
					//网络异常
					sendMessage(M.MessageCode.ERR_402_NETWORK_ERROR);
					e.printStackTrace();
				}

				@Override
				public void onResponse(String str) {
					//暂时方便查看数据用，后期删除
					System.out.println(str);
					if (showLoading) {
						unblock();
					}
					try {
						if (!TextUtils.isEmpty(str)) {
							Response response = JsonUtil.jsonToBean(str, Response.class);
							assert response != null;
							int errorCode = response.getErrorCode();
							if (errorCode > 0) {
								showError(errorCode, response, listener);
							} else {
								listener.loadSuccess(JsonUtil.beanToJson(response.getResult()), response.getUniqueToken());
							}
						} else {
							sendMessage(M.MessageCode.ERR_420_DATA_ERROR);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, params);
		} catch (Exception e) {
			unblock();
			e.printStackTrace();
		}
	}

	private void showError(int errorCode, Response response, final IDataRequestListener listener) {
		try {
			MessageEvent messageEvent=new MessageEvent();
			switch (errorCode) {
				case M.MessageCode.ERR_100_SYSTEM_ERROR:
					messageEvent.setCode(M.MessageCode.ERR_400_FATAL_ERROR);
					if (LibSession.sDebug) {
						messageEvent.setStr("["+Utils.getString(R.string.fatal_error)+"]"+response.getErrorMessage());
					} else {
						messageEvent.setStr(Utils.getString(R.string.fatal_error));
					}
					sendMessage(messageEvent);
					break;
				case M.MessageCode.ERR_102_SESSION_TIMEOUT:
				case M.MessageCode.ERR_104_NOT_SUPPORTED_APK_VERSION:
				case M.MessageCode.ERR_500_NOT_ALLOW_LOGIN:
					sendMessage(errorCode);
					break;
				default:
					listener.loadFailure(errorCode, JsonUtil.beanToJson(response.getResult() != null ? response
							.getResult() : ""), response.getUniqueToken());
					break;
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}


	public void requestServerHint(Map<String, String> params, final IDataRequestListener listener) {
		try {
			if (mContext != null && !Utils.isNetworkAvailable()) {
				sendMessage(M.MessageCode.ERR_401_NETWORK_INVALID);
				return;
			}
			OkHttpClientManager.postAsyn(mUrl, new OkHttpClientManager.ResultCallback<String>() {
				@Override
				public void onError(Request request, Exception e) {
					sendMessage(M.MessageCode.ERR_402_NETWORK_ERROR);
					e.printStackTrace();
				}

				@Override
				public void onResponse(String str) {
					unblock();
					try {
						if (!TextUtils.isEmpty(str)) {
							Response response = JsonUtil.jsonToBean(str, Response.class);
							assert response != null;
							int errorCode = response.getErrorCode();
							if (errorCode > 0) {
								showError(errorCode, response, listener);
							} else {
								listener.loadSuccess(JsonUtil.beanToJson(response.getResult()), response.getUniqueToken());
							}
						} else {
							sendMessage(M.MessageCode.ERR_420_DATA_ERROR);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void showBlock(Integer msgID) {
		try {
			DialogFactory.block(mContext, mContext.getResources().getString(msgID));
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	protected void unblock() {
		if (isUnblock) DialogFactory.unblock();
	}

	//有些处理数据多，比较久，可以设置先不关闭load窗口
	public void setUnblock(boolean unblock) {
		isUnblock = unblock;
	}

	private void sendMessage(MessageEvent event) {
		EventBusUtil.post(event);
	}

	private void sendMessage(int msgID) {
		EventBusUtil.post(new MessageEvent(msgID));
	}
}
