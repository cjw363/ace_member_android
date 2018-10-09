package com.og.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequestParams {
	private static final String ENCODING = "UTF-8";
	protected ConcurrentHashMap<String, String> urlParams;
	protected ConcurrentHashMap<String, FileWrapper> fileParams;
	protected ConcurrentHashMap<String, ArrayList<String>> urlParamsWithArray;

	public RequestParams() {
		this.init();
	}

	public RequestParams(Map<String, String> source) {
		this.init();
		Iterator var3 = source.entrySet().iterator();
		while (var3.hasNext()) {
			Map.Entry entry = (Map.Entry) var3.next();
			this.put((String) entry.getKey(), (String) entry.getValue());
		}
	}

	public RequestParams(String key, String value) {
		this.init();
		this.put(key, value);
	}

	public RequestParams(Object... keysAndValue) {
		this.init();
		int len = keysAndValue.length;
		if (len % 2 != 0) {
			throw new IllegalArgumentException("Supplied arguments must be even");
		} else {
			for (int i = 0; i < len; i += 2) {
				String key = String.valueOf(keysAndValue[i]);
				String val = String.valueOf(keysAndValue[i + 1]);
				this.put(key, val);
			}
		}
	}

	public void put(String key, String value) {
		if (key != null && value != null) {
			this.urlParams.put(key, value);
		}
	}

	public void put(String key, File file) throws FileNotFoundException {
		this.put(key, new FileInputStream(file), file.getName());
	}

	public void put(String key, ArrayList<String> values) {
		if (key != null && values != null) {
			this.urlParamsWithArray.put(key, values);
		}
	}

	public void put(String key, InputStream stream) {
		this.put(key, stream, (String) null);
	}

	public void put(String key, InputStream stream, String fileName) {
		this.put(key, stream, fileName, (String) null);
	}

	public void put(String key, InputStream stream, String fileName, String contentType) {
		if (key != null && stream != null) {
			this.fileParams.put(key, new RequestParams.FileWrapper(stream, fileName, contentType));
		}
	}

	public int size() {
		return this.urlParams.size();
	}

	public void remove(String key) {
		this.urlParams.remove(key);
		this.urlParams.remove(key);
		this.urlParamsWithArray.remove(key);
	}

	public String toString() {
		StringBuilder result = new StringBuilder();
		try {
			Iterator var3 = this.urlParams.entrySet().iterator();

			Map.Entry entry;
			while (var3.hasNext()) {
				entry = (Map.Entry) var3.next();
				if (result.length() > 0) {
					result.append("&");
				}
				result.append(URLEncoder.encode((String) entry.getKey(), ENCODING));
				result.append("=");
				result.append(URLEncoder.encode((String) entry.getValue(), ENCODING));
			}

			var3 = this.fileParams.entrySet().iterator();

			while (var3.hasNext()) {
				entry = (Map.Entry) var3.next();
				if (result.length() > 0) {
					result.append("&");
				}
				result.append(URLEncoder.encode((String) entry.getKey(), ENCODING));
				result.append("=");
				result.append("FILE");
			}

			var3 = this.urlParamsWithArray.entrySet().iterator();
			while (var3.hasNext()) {
				entry = (Map.Entry) var3.next();
				if (result.length() > 0) {
					result.append("&");
				}
				ArrayList values = (ArrayList) entry.getValue();
				for (int i = 0; i < values.size(); ++i) {
					if (i != 0) {
						result.append("&");
					}
					result.append(URLEncoder.encode((String) entry.getKey(), ENCODING));
					result.append("=");
					result.append(URLEncoder.encode((String) values.get(i), ENCODING));
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Encoding not supported: " + ENCODING, e);
		}

		return result.toString();
	}

	private void init() {
		this.urlParams = new ConcurrentHashMap<>();
		this.fileParams = new ConcurrentHashMap<>();
		this.urlParamsWithArray = new ConcurrentHashMap<>();
	}

	private static class FileWrapper {
		public InputStream inputStream;
		public String fileName;
		public String contentType;

		public FileWrapper(InputStream inputStream, String fileName, String contentType) {
			this.inputStream = inputStream;
			this.fileName = fileName;
			this.contentType = contentType;
		}

		public String getFileName() {
			return this.fileName != null ? this.fileName : "nofilename";
		}
	}
}
