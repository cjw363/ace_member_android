package com.ace.member.main.input_password;


public interface InputPasswordCallback {
	void onSuccess();

	void onFail();

	void onCancel();

	void onLock();

	public class SimpleInputPasswordCallbackListener implements InputPasswordCallback {

		@Override
		public void onSuccess() {

		}

		@Override
		public void onFail() {

		}

		@Override
		public void onCancel() {

		}

		@Override
		public void onLock() {

		}
	}
}
