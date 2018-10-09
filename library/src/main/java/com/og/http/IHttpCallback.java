package com.og.http;


public interface IHttpCallback {
	void onStarted(HttpCall call);

	void onUploading(long total, long current, boolean isUpLoading);

	void onDownloading(long total, long current, boolean isDownloading);

	void onResponse(String s);

	void onError(Throwable throwable);

	class SimpleHttpCallback implements IHttpCallback {

		@Override
		public void onStarted(HttpCall call) {

		}

		@Override
		public void onUploading(long total, long current, boolean isLoading) {

		}

		@Override
		public void onDownloading(long total, long current, boolean isLoading) {

		}

		@Override
		public void onResponse(String s) {

		}

		@Override
		public void onError(Throwable throwable) {

		}
	}
}
