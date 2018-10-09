package com.og.event;


import android.support.design.widget.Snackbar;

public class ToastEvent {
	private int mMsgRes;
	private CharSequence mMsg;
	private int mDuration;
	private Snackbar.Callback mCallback;

	public ToastEvent() {
	}

	public ToastEvent(int msgRes) {
		this.mMsgRes = msgRes;
	}

	public ToastEvent(int msgRes, int duration) {
		this.mMsgRes = msgRes;
		this.mDuration = duration;
	}

	public ToastEvent(CharSequence msg) {
		this.mMsg = msg;
	}

	public ToastEvent(CharSequence msg, int duration) {
		this.mMsg = msg;
		this.mDuration = duration;
	}

	public ToastEvent(CharSequence msg, int duration, Snackbar.Callback callback) {
		mMsg = msg;
		mDuration = duration;
		mCallback = callback;
	}

	public int getMsgRes() {
		return mMsgRes;
	}

	public void setMsgRes(int msgRes) {
		this.mMsgRes = msgRes;
	}

	public CharSequence getMsg() {
		return mMsg;
	}

	public void setMsg(CharSequence msg) {
		this.mMsg = msg;
	}

	public int getDuration() {
		return mDuration;
	}

	public void setDuration(int duration) {
		this.mDuration = duration;
	}

	public Snackbar.Callback getCallback() {
		return mCallback;
	}

	public void setCallback(Snackbar.Callback callback) {
		mCallback = callback;
	}
}
