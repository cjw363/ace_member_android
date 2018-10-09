package com.ace.member.bean;

import com.google.gson.annotations.SerializedName;


public class WsaBill extends BaseEdcWsaBill {
	@SerializedName("bill_number")
	private String mNumber;

	public String getNumber() {
		return mNumber;
	}

	public void setNumber(String number) {
		mNumber = number;
	}
}
