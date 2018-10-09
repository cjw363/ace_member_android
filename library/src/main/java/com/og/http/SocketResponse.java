package com.og.http;

public class SocketResponse {
	private int code = 0;
	private Object result = null;//json解析实体类字符串

	public void setCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public Object getResult() {
		return this.result;
	}

}
