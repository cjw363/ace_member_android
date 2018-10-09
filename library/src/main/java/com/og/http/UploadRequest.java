package com.og.http;

import java.io.File;
import java.util.Map;

public class UploadRequest {
	private String url;
	private Map<String, String> mParamMap;
	private Map<String, File> mFileMap;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getParamMap() {
		return mParamMap;
	}

	public void setParamMap(Map<String, String> paramMap) {
		mParamMap = paramMap;
	}

	public Map<String, File> getFileMap() {
		return mFileMap;
	}

	public void setFileMap(Map<String, File> fileMap) {
		mFileMap = fileMap;
	}
}
