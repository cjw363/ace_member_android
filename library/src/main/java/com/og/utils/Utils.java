package com.og.utils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.og.LibApplication;
import com.og.LibGlobal;
import com.og.R;
import com.og.event.ToastEvent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;

public class Utils {

	private static long mLastClickTime;
	private static String mLastContextStr = "";
	private static String TAG = "Utils";

	public static Double strToDouble(String value) {
		try {
			if (value == null || value.equals("") || value.equals("null")) return 0.00;
			value = value.replace(",", "");
			return Double.parseDouble(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0.00;
	}

	public static int ceil(String value) {
		String a = Utils.round(strToDouble(value), 0);
		Double b1 = strToDouble(value);
		Double b2 = strToDouble(a);
		if (b2 >= b1) {
			return Double.valueOf(a).intValue();
		} else {
			b2 = b2 + 1;
			return b2.intValue();
		}
	}

	public static JSONArray parseArrayData(JSONArray list, JSONArray keys) {
		try {
			JSONArray l = new JSONArray();
			int len = list.length();
			for (int j = 0; j < len; j++) {
				JSONArray obj = list.getJSONArray(j);
				int len1 = obj.length();
				JSONObject nObj = new JSONObject();
				for (int i = 0; i < len1; i++) {
					nObj.put(keys.getString(i), obj.getString(i));
				}
				l.put(nObj);
			}
			return l;
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return null;
	}

	public static int dip2px(double dpValue) {
		final float scale = getResource().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static int px2dp(double px) {
		float scale = getResource().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	public static JSONObject parseObjectData(JSONArray list, JSONArray keys, String columnIndex) {
		try {
			JSONObject l = new JSONObject();
			int len = list.length();
			for (int i = 0; i < len; i++) {
				JSONObject v = getNewObject(list.getJSONArray(i), keys);
				if ("".equals(columnIndex)) {
					l.put(i + "", v);
				} else {
					if (v != null) {
						l.put(v.getString(columnIndex), v);
					}
				}
			}
			return l;
		} catch (Exception e) {
			Log.e(TAG, "parseObjectData err: " + e.toString());
			FileUtils.addErrorLog(e);
		}
		return null;
	}

	public static JSONObject parseObjectData2(JSONArray list, JSONArray keys, String columnIndex) {
		try {
			JSONObject l = new JSONObject();
			int len = list.length();
			for (int i = 0; i < len; i++) {
				JSONObject v = getNewObject(list.getJSONArray(i), keys);
				l.put(i + "", v);
			}
			return l;
		} catch (Exception e) {
			Log.e(TAG, "parseObjectData err: " + e.toString());
			FileUtils.addErrorLog(e);
		}
		return null;
	}

	public static JSONObject getNewObject(JSONArray old, JSONArray keys) {
		try {
			int len = old.length();
			JSONObject l = new JSONObject();
			for (int i = 0; i < len; i++) {
				l.put(keys.getString(i), old.getString(i));
			}
			return l;
		} catch (Exception e) {
			Log.e(TAG, "getNewObject: " + e.toString());
			e.printStackTrace();
		}
		return null;
	}

	public static JSONArray getNewJsonArray(JSONArray jsonData, ArrayList<String> list) {
		if (jsonData.length() == 0) return null;
		JSONArray obj = new JSONArray();
		try {
			for (int i = 0; i < jsonData.length(); i++) {
				JSONObject data = jsonData.getJSONObject(i);
				String id = data.getString("id");
				boolean flag = false;
				for (int j = 0; j < list.toArray().length; j++) {
					if (list.toArray()[j].equals(id)) {
						flag = true;
					}
				}
				if (!flag) obj.put(data);
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return obj;

	}

	public static int count(JSONArray array) {
		if (null == array) return 0;
		return array.length();
	}

	public static boolean in_array(String str, JSONArray array) {
		if (array == null) return false;

		try {
			int len = array.length();
			String str1 = str.toUpperCase();
			for (int i = 0; i < len; i++) {
				String str2 = array.getString(i).toUpperCase();
				if (str1.equals(str2)) return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean in_list(String str, List<Map<String, Object>> list, String key) {
		int size = list.size();
		if (size == 0) return false;

		Map<String, Object> map;
		for (int i = 0; i < size; i++) {
			map = list.get(i);
			if (str.equals(map.get(key))) return true;
		}
		return false;
	}

	/**
	 * @param separator 分隔符
	 * @param array     待处理数组
	 * @param limit     只处理前limit个数据
	 * @return 返回字符串
	 */
	public static String implode(String separator, JSONArray array, int limit) {
		String s = "";
		try {
			int len = array.length();
			if (len == 0) return "";
			for (int i = 0; i < len; i++) {
				if (limit > 0 && i == limit) break;
				if (s.equals("")) s = array.getString(i);
				else s += separator + array.getString(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return s;
	}


	/**
	 * 四舍五入
	 *
	 * @param number    double类型
	 * @param precision 精度
	 * @return String
	 */
	public static String round(double number, int precision) {
		double d = number;
		try {
			double t = Math.pow(10, precision);
			double t2 = t * 100 / 100.0;
			d = Math.floor(number * t + 0.5 + LibGlobal.EPSILON) / t2;
		} catch (Exception e) {
			e.printStackTrace();
		}
		BigDecimal b = new BigDecimal(d);
		return b.setScale(precision, BigDecimal.ROUND_HALF_UP).toString();
	}

	/**
	 * 四舍五入
	 *
	 * @param number    double类型
	 * @param precision 精度
	 * @return double
	 */
	public static double round2(double number, int precision) {
		double d = number;
		try {
			double t = Math.pow(10, precision);
			double t2 = t * 100 / 100.0;
			d = Math.floor(number * t + 0.5 + LibGlobal.EPSILON) / t2;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return d;
	}


	public static int nextPage(int total, int page, int size) {
		if (size == 0) return 0;
		int pages = Double.valueOf(Math.floor((total - 1) / size)).intValue() + 1;
		if (page < pages) {
			return page + 1;
		}
		return 0;
	}


	public static String format(Number value) {
		if (value == null) return "";
		return format(value.toString(), 0);
	}

	public static String format(Number value, String currency) {
		if (value == null) return "";
		return format(value.toString(), currency);
	}

	public static String format(Number value, int digits) {
		if (value == null) return "";
		return format(value.toString(), digits);
	}

	public static String format(CharSequence value) {
		return format(value, 0);
	}

	public static String format(CharSequence value, String currency) {
		currency = currency.toUpperCase();
		return format(value, currency.equals("KHR") || currency.equals("VND") ? 0 : 2);
	}

	public static String format(CharSequence value, int digits) {
		if (TextUtils.isEmpty(value) || value.equals(".")) return "";
		String s = value.toString().replace(",", "");
		if (TextUtils.isEmpty(s)) return "";
		if (digits < 0) digits = 0;
		if (digits > 10) digits = 10;
		BigDecimal b = new BigDecimal(s);
		double d = b.setScale(digits, BigDecimal.ROUND_HALF_UP).doubleValue();
		//		NumberFormat cur = NumberFormat.getCurrencyInstance(Locale.US);//API-19 时负号会转换成"()"表示
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
		nf.setMaximumFractionDigits(digits);
		nf.setMinimumFractionDigits(digits);
		return nf.format(d);
	}

	public static String formatToInt(String value) {
		if (value == null || value.equals("null")) return "0";
		value = value.trim();
		int len = value.length();
		if (len > 0) return value;
		return "0";
	}

	public static String formatToString(String value) {
		if (value == null || value.equals("null")) return "";
		value = value.trim();
		int len = value.length();
		if (len > 0) return value;
		return "";
	}

	//是把带有分隔符的数值字符串转换为没有分隔符的字符串
	public static String parseStr(String str) {
		if (str == null) str = "";
		String str1 = "";
		if (!TextUtils.isEmpty(str)) {
			String[] strArray = str.split(",");

			for (String s : strArray) {
				str1 += s;
			}
		}
		return str1;
	}

	/**
	 * 填充空白，使字符串达到指定的宽度
	 *
	 * @param width    字符串拼接后的宽度（需求的宽度）
	 * @param str      原始字符串
	 * @param fontSize 字符串使用的字体大小
	 * @param type     1：左填充，2：右填充
	 * @return str + padding
	 */
	public static String str_pad(int width, String str, int fontSize, int type) {
		String padding = "";
		try {
			int len = Utils.getTextWidth(str, fontSize);
			int paddingLen = width - len;
			String fill = "|";
			while (Utils.getTextWidth(fill, fontSize) < paddingLen) {
				fill += "|";
			}
			int curLen = 0;
			while (curLen++ < fill.length()) {
				padding += "&#160;";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return type == 1 ? padding + str : str + padding;
	}

	public static boolean isFastClick(Context context) {
		try {
			if (context == null) return false;
			long t = System.currentTimeMillis();
			long diff = t - mLastClickTime;
			String currContextStr = context.toString();
			if (0 < diff && diff < 1000) {
				if (mLastContextStr.equals("") || !currContextStr.equals(mLastContextStr))
					showToast(getString(R.string.click_too_fast));
				return true;
			}
			mLastClickTime = t;
			mLastContextStr = currContextStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String encodeEntity(String value) {
		try {
			if (value == null || "".equals(value)) return "";
			String str = "";
			str = Html.fromHtml(value).toString();
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public static Boolean isValidAccount(String account) {
		Boolean b = !account.equals("");
		if (!b) {
			Pattern pt = Pattern.compile("^[a-zA-Z][a-zA-Z0-9]{5,15}+$");
			b = pt.matcher(account).matches();
		}
		return b;
	}

	/**
	 * 获取版本信息
	 */
	public static String getVersionInfo() {
		try {
			PackageManager pm = getContext().getPackageManager();
			PackageInfo info = pm.getPackageInfo(getContext().getPackageName(), 0);
			return info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
			return "iphone version is nuKnown";
		}
	}

	/*
	* 获取错误的信息
	* @param arg1
	* @return
	* */
	public static String getErrorInfo(Throwable arg1) {
		Writer writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		arg1.printStackTrace(pw);
		pw.close();
		return writer.toString();
	}

	//是否锁屏
	public static Boolean isScreenLock() {
		KeyguardManager mKeyguardManager = (KeyguardManager) getContext().getSystemService(Context.KEYGUARD_SERVICE);
		return mKeyguardManager.inKeyguardRestrictedInputMode();
	}


	public static void showToast(CharSequence text) {
		showToast(text, Snackbar.LENGTH_SHORT, null);
	}

	public static void showToast(CharSequence text, int duration) {
		EventBusUtil.post(new ToastEvent(text, duration, null));
	}

	public static void showToast(CharSequence text, int duration, Snackbar.Callback callback) {
		EventBusUtil.post(new ToastEvent(text, duration, callback));
	}

	public static void showToast(int resourceId) {
		showToast(resourceId, null);
	}

	public static void showToast(int resourceId, Snackbar.Callback callback) {
		showToast(resourceId, Snackbar.LENGTH_SHORT, callback);
	}

	public static void showToast(int resourceId, int duration) {
		showToast(getString(resourceId), duration, null);
	}

	public static void showToast(int resourceId, int duration, Snackbar.Callback callback) {
		showToast(getString(resourceId), duration, callback);
	}

	public static SpannableStringBuilder getSpannableStringBuilder(String str, int color, int start, int end) {
		SpannableStringBuilder style = new SpannableStringBuilder(str);
		style.setSpan(new BackgroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		return style;
	}

	//获取屏幕像素信息
	public static DisplayMetrics getScreenSize() {
		return getResource().getDisplayMetrics();
	}

	//获取字符串的宽度
	public static int getTextWidth(String txt, int size) {
		//		int iRet = 0;
		//		Paint paint = new Paint();
		//		paint.setTextSize(size);
		//		if (txt != null && txt.length() > 0) {
		//			int len = txt.length();
		//			float[] widths = new float[len];
		//			paint.getTextWidths(txt, widths);
		//			for (int j = 0; j < len; j++) {
		//				iRet += (int) Math.ceil(widths[j]);
		//			}
		//		}
		//		return iRet;
		Paint paint = new Paint();
		Rect rect = new Rect();
		try {
			txt = txt.replaceAll(" ", "|");
			paint.setTextSize(size);
			paint.getTextBounds(txt, 0, txt.length(), rect);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rect.width();
	}

	//获取字符串的高度
	public static int getTextHeight(String txt, int size) {
		Paint paint = new Paint();
		Rect rect = new Rect();
		try {
			paint.setTextSize(size);
			paint.getTextBounds(txt, 0, txt.length(), rect);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rect.height();
	}

	/**
	 * Check if a network available（网络）
	 */
	public static boolean isNetworkAvailable() {
		boolean connected = false;
		try {
			ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm != null) {
				NetworkInfo ni = cm.getActiveNetworkInfo();
				if (ni != null) {
					connected = ni.isConnected();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connected;
	}

	public static Boolean isKhePhone(String phone, Boolean required) {
		try {
			if (null == phone || "".equals(phone)) return !required;
			phone = phone.replaceAll(" ", "");
			if ("".equals(phone)) {
				return !required;
			}
			int pLen = phone.length();
			return !(pLen > 0 && (pLen < 7 || pLen > 12 || !phone.matches("^0[1-9]\\d+")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	//组合算法m中n个的组合个数
	public static int combination(int m, int n) {
		if (0 == n || 0 == m || m == n) {
			return 1;
		}
		if (m < n) {
			return 0;
		}
		return combination(m - 1, n) + combination(m - 1, n - 1);
	}


	/**
	 * @param a:组合数组
	 * @param m:生成组合长度
	 * @return :所有可能的组合数组列表
	 */
	public static List<List<Integer>> combination(List<Integer> a, int m) {
		List<List<Integer>> list = new ArrayList<>();
		int n = a.size();
		boolean end = false; // 是否是最后一种组合的标记
		// 生成辅助数组。首先初始化，将数组前n个元素置1，表示第一个组合为前n个数。
		int[] tempNum = new int[n];
		for (int i = 0; i < n; i++) {
			if (i < m) {
				tempNum[i] = 1;
			} else {
				tempNum[i] = 0;
			}
		}
		list.add(createResult(a, tempNum, m));// 打印第一种默认组合
		int k = 0;//标记位
		while (!end && n > 0 && n >= m) {
			boolean findFirst = false;
			boolean swap = false;
			// 然后从左到右扫描数组元素值的"10"组合，找到第一个"10"组合后将其变为"01"
			for (int i = 0; i < n; i++) {
				int l = 0;
				if (!findFirst && tempNum[i] == 1) {
					k = i;
					findFirst = true;
				}
				if (tempNum[i] == 1 && tempNum[i + 1] == 0) {
					tempNum[i] = 0;
					tempNum[i + 1] = 1;
					swap = true;
					for (l = 0; l < i - k; l++) { // 同时将其左边的所有"1"全部移动到数组的最左端。
						tempNum[l] = tempNum[k + l];
					}
					for (l = i - k; l < i; l++) {
						tempNum[l] = 0;
					}
					if (k == i && i + 1 == n - m) {//假如第一个"1"刚刚移动到第n-m+1个位置,则终止整个寻找
						end = true;
					}
				}
				if (swap) {
					break;
				}
			}
			list.add(createResult(a, tempNum, m));// 添加下一种默认组合
		}
		return list;
	}

	// 根据辅助数组和原始数组生成结果数组
	private static List<Integer> createResult(List<Integer> a, int[] temp, int m) {
		List<Integer> result = new ArrayList<>();
		int j = 0, n = a.size();
		for (int i = 0; i < n; i++) {
			if (temp[i] == 1) {
				result.add(j, a.get(i));
				j++;
			}
		}
		return result;
	}


	public static String getDeviceID(Context context) {
		String deviceId = "";
		final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (Build.VERSION.SDK_INT >= 23) {
			int checkDeviceIDPermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE);
			if (checkDeviceIDPermission != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_PHONE_STATE}, 1);
			} else {
				if (mTelephony.getDeviceId() != null) {
					deviceId = mTelephony.getDeviceId();
				} else {
					deviceId = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
				}
			}
		} else {
			if (mTelephony.getDeviceId() != null) {
				deviceId = mTelephony.getDeviceId();
			} else {
				deviceId = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
			}
		}
		return deviceId;
	}

	/**
	 * 获得屏幕宽度
	 *
	 * @return
	 */
	public static int getScreenWidth() {
		return getScreenSize().widthPixels;
	}

	public static int getScreenDPI() {
		return getScreenSize().densityDpi;
	}

	public static int getSmallScreenWidthDp() {
		Configuration config = getContext().getResources().getConfiguration();
		return config.smallestScreenWidthDp;
	}

	/**
	 * 获得屏幕高度
	 *
	 * @return
	 */
	public static int getScreenHeight() {
		return getScreenSize().heightPixels;
	}

	/**
	 * 某个应用是否存在
	 */
	public static Boolean isPackageInstall(String packageName) {
		Boolean b = true;
		try {
			PackageInfo packageInfo = getContext().getPackageManager().getPackageInfo(packageName, 0);
			if (packageInfo != null) {
				b = false;
			}
		} catch (Exception e) {
			b = true;
		}
		return b;
	}

	/**
	 * 判断应用是否在运行中(这个方法好像有问题)
	 **/
	public static boolean checkAppIsRunning(Context context) {
		String processName = context.getPackageName();
		android.app.ActivityManager activityManager = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

		if (activityManager == null) return false;
		// get running application processes
		List<android.app.ActivityManager.RunningAppProcessInfo> processList = activityManager.getRunningAppProcesses();
		for (android.app.ActivityManager.RunningAppProcessInfo process : processList) {
			if (process.processName.startsWith(processName)) {
				boolean isBackground = (process.importance == android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND || process.importance == android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE);
				boolean isLockedState = keyguardManager.inKeyguardRestrictedInputMode();
				if (isBackground || isLockedState) return true;
				else return false;
			}
		}
		return false;
	}

	//number只能是数字字符串 如：1,21,22,65,66,85
	public static List<Integer> formatIntegerArray(String number) {
		List<Integer> list = new ArrayList<>();
		String[] str = number.split(",");
		for (int i = 0; i < str.length; i++) {
			list.add(i, Integer.valueOf(str[i]));
		}
		return list;
	}

	//判断字符串是否是数字
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	//设置有上标的文本
	public static SpannableString formatSpannableString(String str, int start, int end) {
		SpannableString ss = new SpannableString(str);
		//设置上标字体大小
		ss.setSpan(new RelativeSizeSpan(0.7f), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		//绘制上标
		ss.setSpan(new SuperscriptSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ss;
	}

	public static String decimalFormat(double money) {
		DecimalFormat format = new DecimalFormat(",###0.00");
		return format.format(money);
	}

	public static void runOnUIThread(Runnable runnable) {
		if (android.os.Process.myTid() == LibApplication.getMainTid()) {
			runnable.run();
		} else {
			LibApplication.getHandler().post(runnable);
		}
	}

	public static void runOnUIThread(Runnable runnable, long delay) {
		LibApplication.getHandler().postDelayed(runnable, delay);
	}

	private static Context getContext() {
		return LibApplication.getContext();
	}

	private static Resources getResource() {
		return getContext().getResources();
	}

	public static String getString(int resourceId) {
		return getResource().getString(resourceId);
	}

	public static Drawable getDrawable(int resourceId) {
		return ContextCompat.getDrawable(getContext(), resourceId);
	}

	public static int getDimenPx(int resourceId) {
		return getResource().getDimensionPixelSize(resourceId);
	}

	public static int getDimenDp(int resourceId) {
		return (int) (getResource().getDimension(resourceId) + 0.5);
	}

	public static int getColor(int resourceId) {
		return ContextCompat.getColor(getContext(), resourceId);
	}

	public static SpannableStringBuilder formatNumber(double number) {
		String str = format(number, 2);
		if (number < 0) {
			Color.red(R.color.lib_red);
			return getSpannableStringBuilder(str, R.color.lib_red, 0, str.length());
		}
		return new SpannableStringBuilder(str);
	}

	public static boolean checkViewTextNotNull(TextView... textViews) {
		for (TextView textView : textViews) {
			if (textView.getText().toString().isEmpty()) {
				textView.setHint(getString(R.string.required));
				textView.setHintTextColor(Color.RED);
				return false;
			}
		}
		return true;
	}

	public static boolean isEmptyList(List list) {
		return list == null || list.size() == 0;
	}

	public static <T> boolean isEmptyList(List<T> list, boolean checkContent) {
		if (isEmptyList(list)) return true;
		if (checkContent) {
			T t;
			for (int i = 0, n = list.size(); i < n; i++) {
				t = list.get(i);
				if (t == null) return true;
				if (t instanceof String && TextUtils.isEmpty((CharSequence) t)) return true;
			}
		}
		return false;
	}

	public static boolean isNotEmptyList(List list) {
		return !isEmptyList(list);
	}

	public static <T> boolean isNotEmptyList(List<T> list, boolean checkContent) {
		return !isEmptyList(list, checkContent);
	}

	public static View inflate(int layoutRes) {
		return View.inflate(getContext(), layoutRes, null);
	}

	public static int getDpi(Activity activity) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		int height = 0;
		@SuppressWarnings("rawtypes") Class c;
		try {
			c = Class.forName("android.view.Display");
			@SuppressWarnings("unchecked") Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
			method.invoke(display, dm);
			height = dm.heightPixels;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return height;
	}

	public static int[] getScreenWH(Context poContext) {
		WindowManager wm = (WindowManager) poContext.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();
		return new int[]{width, height};
	}

	public static int getVrtualBtnHeight(Context poContext) {
		int location[] = getScreenWH(poContext);
		int realHeight = getDpi((Activity) poContext);
		int virvalHeight = realHeight - location[1];
		return virvalHeight;
	}

	public static String formatAccountNo(String accountNo) {
		String regex = "(.{4})";
		return accountNo.replaceAll(regex, "$1 ");
	}

	//view还没有构建完毕时不能正常弹出,所以加了定时器
	public static void showKeyboard(final EditText view) {
		view.requestFocus();
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.showSoftInput(view, 0);
				timer.cancel();
			}
		}, 200);
	}

	public static void hideKeyboard(View view) {
		InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	private static String[] simplePasswords = new String[]{"abcd1234", "abc12345", "aa123456", "aaa12345", "aaa123456", "a1234567", "a12345678", "qq123456", "asd12345", "asdf1234", "qwer1234", "1234qwer", "1234abcd", "abcd6789", "uiop7890", "hjkl7890", "poiu0987", "zxcv1234", "Aaaa1234"};

	/**
	 * 检查密码的强度
	 *
	 * @return int 0 OK 1 长度或包含字符类型不对 2 密码过于简单
	 */
	public static int checkPassword(String password) {
		String regEx = "^[A-Za-z].*[0-9]|[0-9].*[A-Za-z]$";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(password);
		boolean b = matcher.matches();
		if (!b || password.length() < 8) {
			return 1;
		}
		//不相同的字符要超过6个
		Map<Character, Integer> map = new HashMap<>();
		char[] temp = password.toCharArray();
		for (int i = 0; i < password.length(); i++) {
			map.put(temp[i], 1);
		}
		if (map.size() < 6) return 2;
		for (String simplePassword : simplePasswords) {
			if (password.equals(simplePassword)) {
				return 2;
			}
		}
		return 0;
	}

	public static void toActivity(Activity activity, Class c) {
		activity.startActivity(new Intent(activity, c));
	}

	public static String[] getStringArray(int arrayRes) {
		return getResource().getStringArray(arrayRes);
	}

	/**
	 * @return 将部分数字替换为*
	 */
	public static String formatPhone(String phone) {
		if (TextUtils.isEmpty(phone)) return "";
		int startTarget = phone.indexOf("-") + 2;
		int endTarget = phone.length() - 4;

		if (endTarget > startTarget) {
			StringBuilder stringBuilder = new StringBuilder(phone);
			phone = stringBuilder.replace(startTarget, endTarget, "****").toString();
		}
		return phone;
	}

	/**
	 * @return 格式化带区号的电话号码
	 */
	public static String formatPhone2(String phone) {
		String countryCode = phone.substring(1, TextUtils.indexOf(phone, "-") - 1);
		phone = phone.substring(TextUtils.indexOf(phone, "-") + 1);
		phone = phone.replaceFirst("^0?", "");
		if (!phone.isEmpty()) {
			phone = '+' + countryCode + '-' + phone;
		}
		return phone;
	}

	/**
	 * (855)1234654
	 * +855-123464
	 * 188-324-1234
	 * @return 将本机联系人的号码去空格和横线
	 */
	public static String formatPhone3(String phone) {
		if (TextUtils.isEmpty(phone)) return "";
		if (phone.charAt(0) == '(') {//带区号
			return phone.split("\\)")[1].replace("-", "").replace(" ", "");
		} else if (phone.charAt(0) == '+') {//带区号
			return phone.split("-")[1].replace("-", "").replace(" ", "");
		}
		return phone.replace("-", "").replace(" ", "");
	}

	public static boolean checkEmptyAmount(String amount) {
		return TextUtils.isEmpty(amount) || Double.parseDouble(amount.replace(",", "")) <= 0;
	}

	public static String dealCurrency(String s) {
		if (TextUtils.isEmpty(s)) return "";
		s = s.replace(",", "");
		StringBuilder sb = new StringBuilder();
		int c;
		for (int i = 0, n = s.length(); i < n; i++) {
			c = s.charAt(i);
			if (c >= '0' && c <= '9' || c == '.') {
				sb.append((char) c);
			} else {
				break;
			}
		}
		return sb.toString();
	}

	public static String getRealPhone(String phone) {
		if (TextUtils.isEmpty(phone)) return "";
		int index = phone.lastIndexOf("-");
		phone = phone.substring(index + 1);
		if (phone.startsWith("0")) phone = phone.replaceFirst("0", "");
		return phone;
	}

	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		}
		if ((obj instanceof List)) {
			return ((List) obj).size() == 0;
		}
		if ((obj instanceof String)) {
			return ((String) obj).trim().equals("");
		}
		return false;
	}

	public static MediaType parseMediaType(String fileName) {
		if (TextUtils.isEmpty(fileName)) return null;
		String extName = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
		if (TextUtils.isEmpty(extName)) return null;
		if (extName.equals("jpg")) extName = "jpeg";
		if (extName.equals("jpeg") || extName.equals("gif") || extName.equals("png") || extName.equals("pjpeg") || extName.equals("wbmp"))
			return MediaType.parse("image/" + extName);
		return null;
	}

	public static String absAmount(String amount) {
		Double amount2 = strToDouble(amount);
		return String.valueOf(Math.abs(amount2));
	}

	//克隆对象
	public static <T> T cloneFrom(T src) throws RuntimeException {
		ByteArrayOutputStream memoryBuffer = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		T dist = null;
		try {
			out = new ObjectOutputStream(memoryBuffer);
			out.writeObject(src);
			out.flush();
			in = new ObjectInputStream(new ByteArrayInputStream(memoryBuffer.toByteArray()));
			dist = (T) in.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (out != null) try {
				out.close();
				out = null;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			if (in != null) try {
				in.close();
				in = null;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return dist;
	}

	public static String captureName(String name) {
		name = name.toLowerCase();
		name = name.substring(0, 1).toUpperCase() + name.substring(1);
		return name;
	}

	//把 yyyy-MM-dd HH:mm:ss 格式 转为 yyyy-MM-dd 格式
	public static String formatDateToYearMonthDay(String s) {
		String date;
		SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		try {
			date = newFormat.format(oldFormat.parse(s));
		} catch (java.text.ParseException e) {
			System.out.println("输入的日期格式有误！");
			Log.e(TAG, "输入的日期格式有误！");
			e.printStackTrace();
			date = s;
		}
		return date;
	}

	public static String formatDateToMonthMinute(String s) {
		String date;
		SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		SimpleDateFormat newFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.ENGLISH);
		try {
			date = newFormat.format(oldFormat.parse(s));
		} catch (java.text.ParseException e) {
			System.out.println("输入的日期格式有误！");
			Log.e(TAG, "输入的日期格式有误！");
			e.printStackTrace();
			date = s;
		}
		return date;
	}

	public static String formatDateToMinute(String s) {
		String date;
		SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
		try {
			date = newFormat.format(oldFormat.parse(s));
		} catch (java.text.ParseException e) {
			System.out.println("输入的日期格式有误！");
			Log.e(TAG, "输入的日期格式有误！");
			e.printStackTrace();
			date = s;
		}
		return date;
	}

	//把 yyyy-MM-dd 格式 转为 MM-dd 格式
	public static String formatDateToMonthDay(String s) {
		String date;
		SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		SimpleDateFormat newFormat = new SimpleDateFormat("MM-dd", Locale.ENGLISH);
		try {
			date = newFormat.format(oldFormat.parse(s));
		} catch (java.text.ParseException e) {
			System.out.println("输入的日期格式有误！");
			Log.e(TAG, "输入的日期格式有误！");
			e.printStackTrace();
			date = s;
		}
		return date;
	}

	//把 yyyy-MM-dd 格式 转为 yyyy-MM 格式
	public static String formatDateToYearMonth(String s) {
		String date;
		SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM", Locale.ENGLISH);
		try {
			date = newFormat.format(oldFormat.parse(s));
		} catch (java.text.ParseException e) {
			System.out.println("输入的日期格式有误！");
			Log.e(TAG, "输入的日期格式有误！");
			e.printStackTrace();
			date = s;
		}
		return date;
	}

}
