package com.og.http.request_body;

import com.og.http.IProgress;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;


public class ProgressResponseBody extends ResponseBody {
	private final ResponseBody responseBody;
	private final IProgress.download progressListener;
	private BufferedSource bufferedSource;

	public ProgressResponseBody(ResponseBody responseBody, IProgress.download progressListener) {
		this.responseBody = responseBody;
		this.progressListener = progressListener;
	}

	@Override
	public MediaType contentType() {
		return responseBody.contentType();
	}

	@Override
	public long contentLength() {
		return responseBody.contentLength();
	}

	@Override
	public BufferedSource source() {
		if (bufferedSource == null) {
			bufferedSource = Okio.buffer(source(responseBody.source()));
		}
		return bufferedSource;
	}

	private Source source(Source source) {
		return new ForwardingSource(source) {
			long totalLength = 0L;
			long totalBytesRead = 0L;
			long lastTime = 0L;
			long currentTime = 0L;

			@Override
			public long read(Buffer sink, long byteCount) throws IOException {
				long bytesRead = super.read(sink, byteCount);
				if (totalLength == 0L) {
					totalLength = responseBody.contentLength();
				}
				totalBytesRead += bytesRead != -1 ? bytesRead : 0;
				currentTime = System.currentTimeMillis();
				if (currentTime - lastTime >= 2000 || totalBytesRead == totalLength || bytesRead == -1) {
					progressListener.onDownloading(totalLength, totalBytesRead, bytesRead != -1);
					lastTime = currentTime;
				}
				return bytesRead;
			}
		};
	}
}