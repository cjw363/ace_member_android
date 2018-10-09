package com.ace.member.bean;

import com.google.gson.annotations.SerializedName;


public class EdcBill extends BaseEdcWsaBill {
	@SerializedName("consumer_number")
	private String mNumber;

	public String getNumber() {
		return mNumber;
	}

	public void setNumber(String number) {
		mNumber = number;
	}
}
