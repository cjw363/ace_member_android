package com.ace.member.utils;


import com.ace.member.bean.Balance;
import com.ace.member.bean.Currency;
import com.ace.member.bean.ServicePoint;
import com.ace.member.bean.SocketServers;
import com.ace.member.bean.User;
import com.og.LibSession;
import com.og.utils.ItemObject;

import org.json.JSONArray;

import java.util.Collections;
import java.util.List;

public class Session extends LibSession {
	public static User user = null;
	public static String deviceID = "";
	//这两个变量的运用时在忘记密码与注册时保存CountryCode与phone的
	public static String verificationCountryCode = "", verificationPhone = "", verificationUserID = "";//

	public static int isSessionTimeOut = 0;

	public static Boolean hasNewVersion = false, flagUseGesturePwd = false, closeDialog = false, isPhoneVerified = false, isIdVerified = false, isFingerprintVerified = false;

	public static JSONArray recentTransaction = null, customerService = null;

	public static List<Balance> balanceList = null;
	public static List<ServicePoint> branchList = null;
	public static List<ServicePoint> agentList = null;
	public static List<ItemObject> countryCodeList = null;
	public static List<Currency> currencyList = null;

	public static SocketServers socketServers = null;

	public static void clear() {
		user = null;
		sSid = "";
		deviceID = "";
		verificationCountryCode = "";
		verificationPhone = "";

		sVersionStatus = 0;
		isSessionTimeOut = 0;

		hasNewVersion = false;
		flagUseGesturePwd = false;//false:未使用手势 true：已使用手势
		closeDialog = false;
		isPhoneVerified = false;
		isIdVerified = false;
		isFingerprintVerified = false;

		recentTransaction = null;
		customerService = null;
		balanceList = null;
		branchList = null;
		agentList = null;
		countryCodeList = null;
	}

	public static void updateBalance(Balance b) {
		if (b == null) return;
		if (balanceList == null) {
			balanceList = Collections.singletonList(b);
			return;
		}
		for (Balance balance : balanceList) {
			if (balance.getCurrency().toUpperCase().equals(b.getCurrency().toUpperCase()))
				balance.setAmount(b.getAmount());
		}
	}
}
