package com.ace.member.main.home.money.deposit_cash;

import com.ace.member.base.BaseBean;
import com.ace.member.bean.Balance;
import com.google.gson.annotations.SerializedName;
import com.og.utils.JsonUtil;

import org.json.JSONObject;

public class DepositCashResult extends BaseBean {

	@SerializedName("not_data")
	private boolean notData;

	private String time;

	@SerializedName("data")
	private Balance balance;

	public boolean isNotData() {
		return notData;
	}

	public void setNotData(boolean notData) {
		this.notData = notData;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Balance getData() {
		return balance;
	}

	public void setData(JSONObject data) {
		this.balance = JsonUtil.jsonToBean(data.toString(), Balance.class);
	}
}
