package com.ace.member.utils;


import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import com.ace.member.R;
import com.og.utils.Utils;

public class StringUtil {
	public static SpannableStringBuilder dealUpdateLog(String log) {
		SpannableStringBuilder builder = new SpannableStringBuilder();
		ForegroundColorSpan span;
		AbsoluteSizeSpan sizeSpan;
		String[] logs = log.split("\\n");
		for (String log1 : logs) {
			if (log1.toLowerCase().startsWith("ver")) {
				span = new ForegroundColorSpan(Utils.getColor(R.color.clr_common_content));
				builder.append(log1);
				builder.setSpan(span, builder.length() - log1.length(), builder.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
				builder.append("\n");
			} else {
				sizeSpan = new AbsoluteSizeSpan(Utils.getDimenPx(R.dimen.txtSize14));
				builder.append(log1);
				builder.setSpan(sizeSpan, builder.length() - log1.length(), builder.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
				builder.append("\n");
			}
		}
		return builder;
	}

	/**
	 * 电话号码替换，保留后四位
	 *
	 * 如果电话号码为空 或者 null ,返回null ；否则，返回替换后的字符串；
	 *
	 * @param phoneNumber 电话号码
	 */
	public static String phoneReplaceWithStar(String phoneNumber) {
		if (phoneNumber.isEmpty()) {
			return null;
		} else {
			return replaceAction(phoneNumber, "(?<=\\d{0})\\d(?=\\d{4})");
		}
	}

	private static String replaceAction(String str, String regular) {
		return str.replaceAll(regular, "*");
	}


	/**
	 * @param str       字符串只能为两位小数或者整数
	 * @param isDecimal 是否是小数
	 * @Description 格式化字符串，每三位用逗号隔开
	 */
	public static String addComma(String str, boolean isDecimal) {
		//先将字符串颠倒顺序
		str = new StringBuilder(str).reverse().toString();
		if (str.equals("0")) {
			return str;
		}
		String str2 = "";
		for (int i = 0; i < str.length(); i++) {
			if (i * 3 + 3 > str.length()) {
				str2 += str.substring(i * 3, str.length());
				break;
			}
			str2 += str.substring(i * 3, i * 3 + 3) + ",";
		}
		if (str2.endsWith(",")) {
			str2 = str2.substring(0, str2.length() - 1);
		}
		//最后再将顺序反转过来
		String temp = new StringBuilder(str2).reverse().toString();
		if (isDecimal) {
			//去掉最后的","
			return temp.substring(0, temp.lastIndexOf(",")) + temp.substring(temp.lastIndexOf(",") + 1, temp.length());
		} else {
			return temp;
		}
	}

	/**
	 * 检验字符串，为空返回空串
	 */
	public static String checkStr(String str) {
		return (str == null ? "" : str);
	}
}
