package com.og.exception;

import android.os.AsyncTask;

import com.og.LibGlobal;
import com.og.LibSession;
import com.og.utils.NetUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
public class LibCrash extends AsyncTask<String, String, String> {
	private static final int CONN_TIMEOUT = 5000;
	private static final int SOCKET_TIMEOUT = 5000;

	public LibCrash() {}

	protected void onPreExecute() {}

	protected String doInBackground(String... params) {
		Map<String, String> p = new HashMap<>();
		p.put("ver", params[0]);
		p.put("msg", params[1]);
		String url = LibSession.sServiceUrl.replace("/web/", "/") + "crash.php";
		httpConn(url, p);
		return null;
	}

	protected void onPostExecute(String result) {}

	private void httpConn(String url, Map<String, String> params) {
		try {
			URL resourceUrl = new URL(NetUtils.getUrlStr(url, params, LibGlobal.UTF8));
			HttpURLConnection conn = (HttpURLConnection) resourceUrl.openConnection();
			conn.setRequestMethod("GET");
			conn.setUseCaches(false);
			conn.setConnectTimeout(CONN_TIMEOUT);
			conn.setReadTimeout(SOCKET_TIMEOUT);
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", LibGlobal.UTF8);
			conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
			conn.connect();
			conn.getResponseCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
