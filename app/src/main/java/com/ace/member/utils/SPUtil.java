package com.ace.member.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.og.utils.JsonUtil;

public class SPUtil {

	private static final String DEFAULT_SHARE = "config";

	public static SharedPreferences getSharePreferences() {
		return getSharePreferences(DEFAULT_SHARE);
	}

	public static SharedPreferences getSharePreferences(String shareName) {
		return BaseApplication.getContext().getSharedPreferences(shareName, Context.MODE_PRIVATE);
	}

	public static String getString(String key, String defaultValue) {
		return getString(DEFAULT_SHARE, key, defaultValue);
	}

	public static String getString(String shareName, String key, String defaultValue) {
		return getSharePreferences(shareName).getString(key, defaultValue);
	}

	public static void putString(String key, String value) {
		putString(DEFAULT_SHARE, key, value);
	}

	public static void putString(String shareName, String key, String value) {
		getSharePreferences(shareName).edit().putString(key, value).apply();
	}

	public static int getInt(String key, int defaultValue) {
		return getInt(DEFAULT_SHARE, key, defaultValue);
	}

	public static int getInt(String shareName, String key, int defaultValue) {
		return getSharePreferences(shareName).getInt(key, defaultValue);
	}

	public static void putInt(String key, int value) {
		putInt(DEFAULT_SHARE, key, value);
	}

	public static void putInt(String shareName, String key, int value) {
		getSharePreferences(shareName).edit().putInt(key, value).apply();
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		return getBoolean(DEFAULT_SHARE, key, defaultValue);
	}

	public static boolean getBoolean(String shareName, String key, boolean defaultValue) {
		return getSharePreferences(shareName).getBoolean(key, defaultValue);
	}

	public static void putBoolean(String key, boolean value) {
		putBoolean(DEFAULT_SHARE, key, value);
	}

	public static void putBoolean(String shareName, String key, boolean value) {
		getSharePreferences(shareName).edit().putBoolean(key, value).apply();
	}

	public static <T> void putObject(String key, T t) {
		putObject(DEFAULT_SHARE, key, t);
	}

	public static <T> void putObject(String shareName, String key, T t) {
		putString(shareName, key, JsonUtil.beanToJson(t));
	}

	public static <T> T getObject(String key, Class<T> tClass) {
		return getObject(DEFAULT_SHARE, key, tClass);
	}

	public static <T> T getObject(String shareName, String key, Class<T> tClass) {
		return JsonUtil.jsonToBean(getString(key, null), tClass);
	}

	public static void remove(String key) {
		remove(DEFAULT_SHARE, key);
	}

	public static void remove(String shareName, String key) {
		if (getSharePreferences(shareName).contains(key)) {
			getSharePreferences(shareName).edit().remove(key).apply();
		}
	}
}
