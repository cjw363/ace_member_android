package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class EdcWsaDataWrapper extends BaseBean {

	@SerializedName("config")
	private List<EdcWsaConfig> mEdcWsaConfigs;

	@SerializedName("function_edc")
	private boolean mIsEdcFunctionRunning;

	@SerializedName("function_wsa")
	private boolean mIsWsaFunctionRunning;

	@SerializedName("edc_bill")
	private EdcBill mEdcBill;

	@SerializedName("wsa_bill")
	private WsaBill mWsaBill;

	public List<EdcWsaConfig> getEdcWsaConfigs() {
		return mEdcWsaConfigs;
	}

	public void setEdcWsaConfigs(List<EdcWsaConfig> edcWsaConfigs) {
		mEdcWsaConfigs = edcWsaConfigs;
	}

	public boolean isEdcFunctionRunning() {
		return mIsEdcFunctionRunning;
	}

	public void setEdcFunctionRunning(boolean edcFunctionRunning) {
		mIsEdcFunctionRunning = edcFunctionRunning;
	}

	public boolean isWsaFunctionRunning() {
		return mIsWsaFunctionRunning;
	}

	public void setWsaFunctionRunning(boolean wsaFunctionRunning) {
		mIsWsaFunctionRunning = wsaFunctionRunning;
	}

	public EdcBill getEdcBill() {
		return mEdcBill;
	}

	public void setEdcBill(EdcBill edcBill) {
		mEdcBill = edcBill;
	}

	public WsaBill getWsaBill() {
		return mWsaBill;
	}

	public void setWsaBill(WsaBill wsaBill) {
		mWsaBill = wsaBill;
	}
}
