package com.og;

public class LibGlobal{

	public static final String APK_DOWNLOAD_URL = "url";
	public static final String APK_UPDATE_CONTENT = "msg";
	public static final String APK_VERSION_CODE = "code";
	public static final String APK_NAME = "name";
	public static final String GOOGLE_SERVICE_GSF="com.google.android.gsf";//google服务框架
//	public static final String GOOGLE_APP_MAPS="com.google.android.apps.maps";//google maps
	public static final String GOOGLE_PLAY_SERVICE="com.google.android.gms";//google play service
	public static final String GOOGLE_PLAY_STORE="com.android.vending";//google play store

	// check current version is supported by service
	public static final int APK_VERSION_STATUS_0_UNCHECKED = 0;
	public static final int APK_VERSION_STATUS_1_SUPPORTED = 1;
	public static final int APK_VERSION_STATUS_2_UNSUPPORTED = 2;

	public static final Double EPSILON = 0.0001;
	public static final String UTF8 = "UTF-8";
	public static int MILLISECOND_IN_DAY = 86400000;
	public static final String PRE_DATA_KEY = "pre_data";
	public static final int MIN_YEAR = 2017; //配置搜索时的最小年份

}