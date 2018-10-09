package com.og.event;

import android.os.Message;

public class MessageEvent {
	private int mCode;
	private String mString;
	private Message mMsg;

	public MessageEvent() {
	}

	public MessageEvent(int code) {
		mCode = code;
	}

	public MessageEvent(int code, String string) {
		mCode = code;
		mString = string;
	}

	public MessageEvent(Message msg) {
		mMsg = msg;
	}

	public int getCode() {
		return mCode;
	}

	public void setCode(int code) {
		mCode = code;
	}

	public String getStr() {
		return mString;
	}

	public void setStr(String string) {
		mString = string;
	}

	public Message getMsg(){
		return mMsg;
	}

	public void setMsg(Message msg){
		mMsg = msg;
	}

}
