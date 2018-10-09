package com.og.utils;

public class PhoneUtils {

	public static String formatPhone(String strPhone) {
		String phone = "";
		try{
			strPhone = strPhone.replaceAll(" ", "");
			if (strPhone == null || "".equals(strPhone) || strPhone.length() < 7 || strPhone.length() > 12)
				return "";
			int i = 0;
			int len = strPhone.length();
			if (len < 10) {
				phone += strPhone.substring(i, i + 3) + " ";
				if (len < 6) {
					phone += strPhone.substring(i + 3, len);
				} else {
					phone += strPhone.substring(i + 3, i + 6) + " ";
					phone += strPhone.substring(i + 6, len);
				}

			} else if (len >= 10) {
				phone += strPhone.substring(i, i + 3) + " ";
				phone += strPhone.substring(i + 3, i + 7) + " ";
				phone += strPhone.substring(i + 7, len);
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		return phone;
	}

	public static String formatPhonesWithHtml(String strPhone) {
		try{
			strPhone = strPhone.replaceAll(" ", "");
			if (strPhone.equals("")) return "";
			String phone = "";
			String[] str = strPhone.split(",");
			for (int i = 0; i < str.length; i++) {
				String _p = formatPhone(str[i]);
				phone += "<a href='tel:" + _p + "'>" + _p + "</a>, ";
			}
			return phone.substring(0, phone.length() - 2);
		}catch (Exception e){
			e.printStackTrace();
		}
		return strPhone;
	}

	public static String formatPhonesWithHtml2(String strPhone) {
		try{
			strPhone = strPhone.replaceAll(" ", "");
			if (strPhone.equals("")) return "";

			String phone = "";
			String[] str = strPhone.split(",");
			for (int i = 0; i < str.length; i++) {
				String _p = formatPhone(str[i]);
				phone += "<a href='tel:" + _p + "'>" + _p + "</a><br/>";
			}
			return phone.substring(0, phone.length() - 1);
		}catch (Exception e){
			e.printStackTrace();
		}
		return strPhone;
	}

	public static String formatPhones(String strPhone) {
		try{
			strPhone = strPhone.replaceAll(" ", "");
			if (strPhone.equals("")) return "";

			String phone = "";
			String[] str = strPhone.split(",");
			for (int i = 0; i < str.length; i++) {
				phone += formatPhone(str[i]) + ", ";
			}
			return phone.substring(0, phone.length() - 2);
		}catch (Exception e){
			e.printStackTrace();
		}
		return strPhone;
	}

	public static Boolean isKhePhone(String phone, Boolean required) {
		try{
			if (null == phone || "".equals(phone)) return !required;
			phone = phone.replaceAll(" ", "");
			if ("".equals(phone)) {
				return !required;
			}
			int pLen = phone.length();
			return !(pLen > 0 && (pLen < 7 || pLen > 12 || !phone.matches("^0[1-9]\\d+")));
		}catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}

}
