package com.og.http;

public class Response {
	private int err_code = 0;
	private String err_message = "";//系统级别错误
	private Object result = null;//json解析实体类字符串
	private String unique_token = "";

	public void setErrorCode(int code) {
		this.err_code = code;
	}

	public int getErrorCode() {
		return this.err_code;
	}

	public void setErrorMessage(String message) {
		this.err_message = message;
	}

	public String getErrorMessage() {
		return this.err_message;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public Object getResult() {
		return this.result;
	}

	public void setUniqueToken(String unique_token) {
		this.unique_token = unique_token;
	}

	public String getUniqueToken() {
		return this.unique_token;
	}
}
