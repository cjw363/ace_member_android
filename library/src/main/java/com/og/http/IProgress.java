package com.og.http;


public interface IProgress {
	interface download {
		void onDownloading(long total, long current, boolean isDownloading);
	}

	interface upload {
		void onUploading(long total, long current, boolean isUpLoading);
	}
}
