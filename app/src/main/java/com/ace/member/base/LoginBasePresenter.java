package com.ace.member.base;

import android.content.Context;

import com.ace.member.bean.Currency;
import com.ace.member.bean.SocketServers;
import com.ace.member.bean.User;
import com.ace.member.utils.Session;
import com.google.gson.reflect.TypeToken;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.ItemObject;
import com.og.utils.JsonUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginBasePresenter extends BasePresenter {

	public LoginBasePresenter(Context context) {
		super(context);
	}

	public void login(Map<String, String> p) {
		try {
			p.put("_a", "user");
			p.put("_b", "aj");
			p.put("cmd", "login");
			submit(p, new SimpleRequestListener() {
				@Override
				public void loadSuccess(String result, String token) {
					try {
						JSONObject jsonObject = new JSONObject(result);
						initSession(jsonObject);
						onSuccess(Session.user.getGesturePassword(),Session.user.getResetPassword());
					} catch (Exception e) {
						FileUtils.addErrorLog(e);
					}
				}

				@Override
				public void loadFailure(int code, String result, String uniqueToken) {
					onFailure(code);
				}

			},false);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}


	}

	public void clearDeviceID(String phone) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "user");
		params.put("_b", "aj");
		params.put("cmd", "clearDeviceID");
		params.put("phone", phone);
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				againLogin();
			}
		},false);
	}

	protected void againLogin() {

	}

	protected void onSuccess(String gesture, boolean resetPassword) {
	}

	protected void onFailure(int code) {
	}

	protected void initSession(JSONObject object) {

		if (object != null) {
			try {
				Session.isSessionTimeOut = 0;
				LibSession.sSid = object.optString("sid");
				LibSession.sFlagDev = (object.optInt("flag_dev") == 1);
				JSONObject userObj=object.getJSONObject("user");
				Session.user= JsonUtil.jsonToBean(userObj.toString(), User.class);
				Session.customerService = object.optJSONArray("customer_service");
				JSONArray exchangeList=object.getJSONArray("exchange_list");
				Session.currencyList=JsonUtil.jsonToList(exchangeList.toString(),new TypeToken<List<Currency>>(){});
				JSONArray countryCodeList = object.getJSONArray("country_code_list");
				Session.countryCodeList = initCountryCodeList(countryCodeList);
				Session.socketServers = JsonUtil.jsonToBean(object.getJSONObject("socket_servers").toString(), SocketServers.class);
			} catch (Exception e) {
				FileUtils.addErrorLog(e);
				e.printStackTrace();
			}
		}
	}

	private List<ItemObject> initCountryCodeList(JSONArray list){
		List<ItemObject> nList = new ArrayList<>();
		try {
			int len=list.length();
			for (int i = 0; i < len; i++) {
				JSONObject it = list.getJSONObject(i);
				ItemObject iObj = new ItemObject();
				iObj.setKey(it.getString("code"));
				iObj.setValue("+ " + it.getString("code"));
				nList.add(i, iObj);
			}
		}catch (Exception e){
			FileUtils.addErrorLog(e);
		}
		return nList;
	}


}
