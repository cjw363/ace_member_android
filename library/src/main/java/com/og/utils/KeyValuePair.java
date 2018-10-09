package com.og.utils;

public class KeyValuePair {
	public String key;
	public String value;

	public KeyValuePair() {
		this.key = "";
		this.value = "";
	}

	public KeyValuePair(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String toString() {
		return value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}
}
