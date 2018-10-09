package com.ace.member.utils;


import android.text.TextUtils;

import com.ace.member.R;
import com.og.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class ColorUtil {
	public static Map<String, Integer> sColorMap = new HashMap<>();

	static {
		sColorMap.put("ABA", 0xFFCB05);
	}


	public static int getStatusColor(int status) {
		int color;
		switch (status) {
			case AppGlobal.STATUS_1_PENDING:
				color = R.color.color_pending;
				break;
			case AppGlobal.STATUS_2_APPROVED:
				color = R.color.color_approved;
				break;
			case AppGlobal.STATUS_3_COMPLETED:
				color = R.color.color_completed;
				break;
			case AppGlobal.STATUS_4_CANCELLED:
				color = R.color.color_canceled;
				break;
			case AppGlobal.STATUS_8_LOCKED:
				color = R.color.color_locked;
				break;
			default:
				color = R.color.black;
				break;
		}
		return Utils.getColor(color);
	}

	public static int getBankBgColor(String bankCode) {
		if (TextUtils.isEmpty(bankCode)) return Utils.getColor(R.color.color_bg_bank_aba);
		if (bankCode.equals("ABA") || bankCode.equals("FTB") || bankCode.equals("SCB") || bankCode.equals("WING")) {
			return Utils.getColor(R.color.color_bg_bank_aba);
		} else if (bankCode.equals("ACL") || bankCode.equals("CBP") || bankCode.equals("MAY")) {
			return Utils.getColor(R.color.color_bg_bank_acl);
		} else if (bankCode.equals("PB") || bankCode.equals("CIMB")) {
			return Utils.getColor(R.color.color_bg_bank_pb);
		} else if (bankCode.equals("SPB")) {
			return Utils.getColor(R.color.color_bg_bank_spb);
		}
		return Utils.getColor(R.color.color_bg_bank_aba);
	}

	public static int getBankTextColor(String bankCode) {
		if (TextUtils.isEmpty(bankCode)) return Utils.getColor(R.color.color_text_bank2);
		if (bankCode.equals("ACL") || bankCode.equals("CBP") || bankCode.equals("MAY"))
			return Utils.getColor(R.color.color_text_bank1);
		return Utils.getColor(R.color.color_text_bank2);
	}

	public static int getBankIndicatorColor(String bankCode) {
		if (TextUtils.isEmpty(bankCode)) return Utils.getColor(R.color.color_indicator_bank2);
		if (bankCode.equals("ACL") || bankCode.equals("CBP") || bankCode.equals("MAY"))
			return Utils.getColor(R.color.color_indicator_bank2);
		return Utils.getColor(R.color.color_indicator_bank1);
	}
}
