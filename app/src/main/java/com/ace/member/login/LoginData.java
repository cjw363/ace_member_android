package com.ace.member.login;


import com.ace.member.base.BaseBean;
import com.ace.member.bean.SingleStringBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginData extends BaseBean {

	@SerializedName("country_code")
	private List<SingleStringBean> countryCodeRowData;
	@SerializedName("register_is_running")
	private boolean RegisterIsRunning;
	@SerializedName("login_is_running")
	private boolean LoginIsRunning;


	public List<SingleStringBean> getCountryCodeRowData() {
		return countryCodeRowData;
	}

	public void setCountryCodeRowData(List<SingleStringBean> countryCodeRowData) {
		this.countryCodeRowData = countryCodeRowData;
	}

	public boolean getRegisterIsRunning() {
		return RegisterIsRunning;
	}

	public void setRegisterIsRunning(boolean registerIsRunning) {
		RegisterIsRunning = registerIsRunning;
	}

	public boolean getLoginIsRunning() {
		return LoginIsRunning;
	}

	public void setLoginIsRunning(boolean loginIsRunning) {
		LoginIsRunning = loginIsRunning;
	}
}
