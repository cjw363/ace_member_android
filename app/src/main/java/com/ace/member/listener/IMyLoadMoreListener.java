package com.ace.member.listener;

public interface IMyLoadMoreListener {

	void onLoading();

	void onSuccess();

	void onFail();

	class MyLoadMoreListenerAdapter implements IMyLoadMoreListener {

		@Override
		public void onLoading() {

		}

		@Override
		public void onSuccess() {

		}

		@Override
		public void onFail() {

		}
	}
}
