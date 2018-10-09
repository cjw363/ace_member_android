package com.ace.member.event;


public class FinishEvent {
	private int code;

	public FinishEvent(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
