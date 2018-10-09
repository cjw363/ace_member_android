package com.og.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import com.og.LibGlobal;
import com.og.event.MessageEvent;
import com.og.http.OkHttpClientManager;
import okhttp3.Request;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

public class NetUtils {

	/**
	 * Check if a network available（网络）
	 */
	public static boolean isNetworkAvailable(Context context) {
		boolean connected = false;
		try{
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm != null) {
				NetworkInfo ni = cm.getActiveNetworkInfo();
				if (ni != null) {
					connected = ni.isConnected();
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return connected;
	}

	// for method get
	public static String getUrlStr(String url, Map<String, String> params, String encode) {
		return getEncodeParameters("GET", params, encode, url);
	}

	// for method post
	public static String getEntityStr(Map<String, String> params, String encode) {
		return getEncodeParameters("POST", params, encode, "");
	}

	private static String getEncodeParameters(String method, Map<String, String> params, String encode, String url) {
		StringBuilder builder;

		if (params == null) {
			params = new TreeMap<>();
		}

		try {
			if (method.equals("GET")) {
				builder = new StringBuilder(url);
				if (params.size() > 0) builder.append('?');
			} else {
				builder = new StringBuilder();
			}
			if (params.size() > 0) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					if (builder.length() > 0) {
						builder.append("&");
					}
					builder.append(URLEncoder.encode(entry.getKey(), encode));
					builder.append('=');
					String v = entry.getValue() == null ? "" : entry.getValue();
					builder.append(URLEncoder.encode(v, encode));
				}
			}

			return builder.toString();
		} catch (Exception e) {
			throw new RuntimeException("Encoding not supported: " + encode, e);
		}
	}

//	public static void getIPCity(String ip){
//		OkHttpClientManager.getAsyn( "http://ip-api.com/json/"+ip, new OkHttpClientManager.ResultCallback<String>() {
//			@Override
//			public void onError(Request request, Exception e) {
//			}
//			@Override
//			public void onResponse(String response) {
//				try {
//					JSONObject obj =  new JSONObject(response);
//					MessageEvent me = new MessageEvent(LibGlobal.MSG_6001_GET_IP_CITY);
//					Message mg = new Message();
//					Bundle b = new Bundle();
//					b.putString("ip", obj.getString("query"));
//					b.putString("city", obj.getString("city"));
//					mg.setData(b);
//					me.setMsg(mg);
//					EventBus.getDefault().post(me);
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

}
