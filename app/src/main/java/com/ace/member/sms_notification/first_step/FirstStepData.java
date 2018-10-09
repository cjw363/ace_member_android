package com.ace.member.sms_notification.first_step;


import com.ace.member.base.BaseBean;
import com.ace.member.bean.SingleStringBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FirstStepData extends BaseBean {
	@SerializedName("country_code")
	private List<SingleStringBean> countryCodeRowData;
	@SerializedName("register_is_running")
	private boolean RegisterIsRunning;

	public List<SingleStringBean> getCountryCodeRowData() {
		return countryCodeRowData;
	}

	public void setCountryCodeRowData(List<SingleStringBean> countryCodeRowData) {
		this.countryCodeRowData = countryCodeRowData;
	}

	public boolean isRegisterIsRunning() {
		return RegisterIsRunning;
	}

	public void setRegisterIsRunning(boolean registerIsRunning) {
		RegisterIsRunning = registerIsRunning;
	}
}
