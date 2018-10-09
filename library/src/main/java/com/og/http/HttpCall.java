package com.og.http;


import okhttp3.Call;

public class HttpCall {
	private Call mCall;

	public HttpCall(Call call) {
		mCall = call;
	}

	public Call getCall() {
		return mCall;
	}

	public void setCall(Call call) {
		mCall = call;
	}

	public boolean isCanceled() {
		return mCall != null && mCall.isCanceled();
	}

	public void cancel() {
		if (!isCanceled()) {
			mCall.cancel();
		}
	}
}
