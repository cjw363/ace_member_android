package com.ace.member.main.home.money.withdraw_cash;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;
import com.og.utils.JsonUtil;

import org.json.JSONObject;

public class WithdrawCashResult extends BaseBean {

	@SerializedName("not_data")
	private boolean notData;

	@SerializedName("data")
	private WithdrawCashResultDetail balance;

	public boolean isNotData() {
		return notData;
	}

	public void setNotData(boolean notData) {
		this.notData = notData;
	}

	public WithdrawCashResultDetail getData() {
		return balance;
	}

	public void setData(JSONObject data) {
		this.balance = JsonUtil.jsonToBean(data.toString(), WithdrawCashResultDetail.class);
	}
}
