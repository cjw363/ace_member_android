package com.ace.member.utils;


import android.content.Context;
import android.content.res.Resources;
import android.util.SparseIntArray;

import com.ace.member.R;

public class M extends com.og.M {

	public static final class LottoMarket{
		public static final int LOTTO_90_700_MARKET_ID = 700;
		public static final int LOTTO_6_39_710_MARKET_ID = 710;
		public static final int LOTTO_6_49_720_MARKET_ID = 720;
		public static final int LOTTO_18_730_MARKET_ID = 730;
		public static final int LOTTO_27_740_MARKET_ID = 740;
		public static final int LOTTO_4D_750_MARKET_ID = 750;
	}

	public static final class RequestCode{
		public static final int REQUEST_CODE_1_SCAN=1;
		public static final int REQUEST_CODE_2_SET_AMOUNT = 2;
		public static final int REQUEST_CODE_3_SELECT_COUPON = 3;
	}

	public static final class CouponStatus{
		public static final int STATUS_0_DEFAULT=0;
		public static final int STATUS_1_UN_USED=1;
		public static final int STATUS_2_USED=2;
		public static final int STATUS_3_EXPIRED=3;
		public static final int STATUS_4_UN_USED_EXPIRE=4;
	}

	public static final class ImagePicker{
		public static final String TYPE_GIF_UP = "GIF";
		public static final String TYPE_GIF_LOW = "gif";
		public static final String FRAGMENT_NAME = "fragmentName";
		public static final String CURRENT = "current";
		public static final String IMAGES = "images";
		public static final String IMAGE = "image";
		public static final String MAX_SELECT_IMAGE_NUM = "maxSelectImageNum";
		public static final String OPEN_MENU = "openMenu";
		public static final String BAN_DELETE ="banDelete";
	}

	public static final class SocketCode{
		public static final int SOCKET_CODE_1_FIRST_CONNECT=1;
		public static final int SOCKET_CODE_2_DEPOSIT=2;
		public static final int SOCKET_CODE_3_WITHDRAW=3;
		public static final int SOCKET_CODE_4_CHAT=4;
		public static final int SOCKET_CODE_5_RECEIVE_MONEY=5;
	}

	public static final class FunctionCode {
		public static final int FUNCTION_100_MEMBER_REGISTER = 100;
		public static final int FUNCTION_102_MEMBER_LOGIN_ANDROID = 102;
		public static final int FUNCTION_111_MEMBER_DEPOSIT = 111;
		public static final int FUNCTION_112_MEMBER_WITHDRAW = 112;
		public static final int FUNCTION_113_MEMBER_TRANSFER_TO_MEMBER = 113;
		public static final int FUNCTION_114_MEMBER_TRANSFER_TO_NO_MEMBER = 114;
		public static final int FUNCTION_115_MEMBER_TRANSFER_TO_PARTNER = 115;
		public static final int FUNCTION_116_MEMBER_TRANSFER_TO_MERCHANT = 116;
		public static final int FUNCTION_118_MEMBER_RECEIVE_TO_ACCOUNT = 118;
		public static final int FUNCTION_119_MEMBER_EXCHANGE = 119;
		public static final int FUNCTION_121_MEMBER_TOP_UP_SHOW_PIN_CODE = 121;
		public static final int FUNCTION_122_MEMBER_TOP_UP_SEND_SMS = 122;
		public static final int FUNCTION_131_MEMBER_PAY_ELECTRICITY_BILL = 131;
		public static final int FUNCTION_132_MEMBER_PAY_WATER_BILL = 132;
		public static final int FUNCTION_133_MEMBER_PAY_ONLINE = 133;
		public static final int FUNCTION_134_MEMBER_PAY_OFFLINE = 134;
		public static final int FUNCTION_135_MEMBER_PAY_BILL_TO_PARTNER = 135;
		public static final int FUNCTION_141_MEMBER_BUY_LOTTO = 141;
		public static final int FUNCTION_142_MEMBER_BUY_SCRATCH = 142;
	}


	//4位数字，应用级
	public static final class MessageCode {
		private static final int ERR_1000_INACTIVE_ACCOUNT = 1000;
		public static final int ERR_1001_LOGGED_ON_OTHER_DEVICE = 1001;
		public static final int ERR_1002_LOGIN_FAIL = 1002;
		public static final int ERR_1003_GESTURE_LOGIN_FAIL = 1003;
		public static final int ERR_1004_GESTURE_EXPIRED = 1004;
		private static final int ERR_1006_PASSWORD_INVALID = 1006;
		private static final int ERR_1007_NEW_PASSWORD_INVALID = 1007;
		private static final int ERR_1008_NEW_PASSWORD_TOO_SIMPLE = 1008;

		private static final int ERR_1706_AMOUNT_INVALID = 1706;
		private static final int ERR_1707_SECURITY_CODE_INVALID = 1707;
		private static final int ERR_1708_CANNOT_FIND_THE_MEMBER = 1708;
		public static final int ERR_1709_NOT_ENOUGH_BALANCE = 1709;
		private static final int ERR_1710_BANK_ACCOUNT_EXISTS = 1710;
		private static final int ERR_1711_SMS_OVER_DAILY_LIMIT = 1711;

		private static final int MSG_1714_EXCEED_SINGLE_TRANSFER_CASH_CAP = 1714;
		private static final int MSG_1715_EXCEED_TRANSFER_CASH_CAP_PER_DAY = 1715;
		private static final int MSG_1721_ONLY_TRANSFER_TO_MEMBER = 1721;
		private static final int MSG_1722_FORBIDDEN_TO_TRANSFER_TO_SELF = 1722;
		private static final int MSG_1723_OVER_MAX_BALANCE_LIMIT = 1723; //超过了最大余额限制
		public static final int MSG_1724_OVER_MAX_AMOUNT = 1724;//超过配置最大值
		public static final int MSG_1725_OVER_BILL_ID_LENGTH = 1725;

		private static final int MSG_1750_INVALID_ACCEPT_CODE = 1750;

		private static final int MSG_1822_OVER_ONE_TIME_LIMIT = 1822;
		private static final int MSG_1823_OVER_DAILY_LIMIT = 1823;
		public static final int MSG_1824_OVER_MAX_BALANCE = 1824;
		public static final int MSG_1825_OVER_LOAN_CONFIG_LIMIT = 1825;
		public static final int MSG_1826_OVER_LOAN_LIMIT = 1826;
		public static final int MSG_1827_OVER_MEMBER_MAX_LIMIT = 1827;
		public static final int MSG_1828_INPUT_AMOUNT_TIMES = 1828;
		public static final int MSG_1830_OVER_MEMBER_COUNT = 1830;
		public static final int MSG_1831_OVER_CREDIT = 1831;
		public static final int MSG_1832_SERVICE_CHARGE_NOT_MATCH = 1832;
		public static final int MSG_1833_NEED_GREATER_THAN_SERVICE_CHARGE = 1833;
		public static final int MSG_1834_CAN_NOT_LESS_THAN_MIN_LOAN = 1834;
		public static final int MSG_1836_CAN_NOT_GREATER_THAN_MAX_LOAN = 1836;
		public static final int ERR_1837_CONFIG_EXPIRED = 1837;
		public static final int MSG_1838_OVER_RETURN_LOAN = 1838;

		private static final int ERR_1910_INVALID_NUMBER = 1910; //Invalid Number
		private static final int ERR_1911_TOO_MANY_STARS = 1911; //Invalid Number (Too Many Stars)
		private static final int ERR_1912_DETAILS_MISMATCH = 1912; //Details Mismatch
		private static final int ERR_1913_INVALID_TYPE = 1913; //Invalid Type/Sub Type
		private static final int ERR_1914_WRONG_TIMES = 1914; //Wrong Times
		private static final int ERR_1915_OVER_SPECIAL_MAX_TIMES = 1915; //Over Special Max Times
		private static final int ERR_1916_BETTING_AMOUNT_MISMATCH = 1916; //Betting Amount Mismatch
		private static final int ERR_1917_WRONG_DETAIL_NUMBER = 1917;  //Wrong Detail Number
		private static final int ERR_1918_OVER_MULTIPLE_MAX_NUMBER = 1918;  //Over Multiple Max Numbers
	}

	private final static SparseIntArray mMessageArray = new SparseIntArray() {
		{
			//应用级
			put(MessageCode.ERR_1000_INACTIVE_ACCOUNT, R.string.msg_1000);
			put(MessageCode.ERR_1004_GESTURE_EXPIRED, R.string.msg_1004);
			put(MessageCode.ERR_1001_LOGGED_ON_OTHER_DEVICE, R.string.msg_1001);
			put(MessageCode.ERR_1002_LOGIN_FAIL, R.string.msg_1002);
			put(MessageCode.ERR_1006_PASSWORD_INVALID, R.string.msg_1006);
			put(MessageCode.ERR_1007_NEW_PASSWORD_INVALID, R.string.msg_1007);
			put(MessageCode.ERR_1008_NEW_PASSWORD_TOO_SIMPLE, R.string.msg_1008);

			put(MessageCode.ERR_1706_AMOUNT_INVALID, R.string.msg_1706);
			put(MessageCode.ERR_1707_SECURITY_CODE_INVALID, R.string.msg_1707);
			put(MessageCode.ERR_1708_CANNOT_FIND_THE_MEMBER, R.string.msg_1708);
			put(MessageCode.ERR_1709_NOT_ENOUGH_BALANCE, R.string.msg_1709);
			put(MessageCode.ERR_1710_BANK_ACCOUNT_EXISTS, R.string.msg_1710);
			put(MessageCode.ERR_1711_SMS_OVER_DAILY_LIMIT, R.string.msg_1711);

			put(MessageCode.MSG_1714_EXCEED_SINGLE_TRANSFER_CASH_CAP, R.string.msg_1714);
			put(MessageCode.MSG_1715_EXCEED_TRANSFER_CASH_CAP_PER_DAY, R.string.msg_1715);
			put(MessageCode.MSG_1721_ONLY_TRANSFER_TO_MEMBER, R.string.msg_1721);
			put(MessageCode.MSG_1722_FORBIDDEN_TO_TRANSFER_TO_SELF, R.string.msg_1722);
			put(MessageCode.MSG_1723_OVER_MAX_BALANCE_LIMIT, R.string.msg_1723);

			put(MessageCode.MSG_1750_INVALID_ACCEPT_CODE, R.string.invalid_accept_code);

			put(MessageCode.MSG_1822_OVER_ONE_TIME_LIMIT, R.string.over_one_time_limit);
			put(MessageCode.MSG_1823_OVER_DAILY_LIMIT, R.string.over_daily_limit);
			put(MessageCode.MSG_1824_OVER_MAX_BALANCE, R.string.over_max_balance_limit);
			put(MessageCode.MSG_1830_OVER_MEMBER_COUNT, R.string.over_member_count_limit);
			put(MessageCode.MSG_1831_OVER_CREDIT, R.string.over_credit);
			put(MessageCode.MSG_1832_SERVICE_CHARGE_NOT_MATCH, R.string.service_charge_not_match);
			put(MessageCode.MSG_1833_NEED_GREATER_THAN_SERVICE_CHARGE, R.string.need_greater_than_service_charge);
			put(MessageCode.MSG_1834_CAN_NOT_LESS_THAN_MIN_LOAN, R.string.less_than_min_loan);
			put(MessageCode.MSG_1836_CAN_NOT_GREATER_THAN_MAX_LOAN, R.string.msg_1836);
			put(MessageCode.ERR_1837_CONFIG_EXPIRED, R.string.msg_1837);
			put(MessageCode.MSG_1838_OVER_RETURN_LOAN, R.string.return_loan);

			put(MessageCode.ERR_1910_INVALID_NUMBER,R.string.msg_1910);
			put(MessageCode.ERR_1911_TOO_MANY_STARS,R.string.msg_1911);
			put(MessageCode.ERR_1912_DETAILS_MISMATCH,R.string.msg_1912);
			put(MessageCode.ERR_1913_INVALID_TYPE,R.string.msg_1913);
			put(MessageCode.ERR_1914_WRONG_TIMES,R.string.msg_1914);
			put(MessageCode.ERR_1915_OVER_SPECIAL_MAX_TIMES,R.string.msg_1915);
			put(MessageCode.ERR_1916_BETTING_AMOUNT_MISMATCH,R.string.msg_1916);

			put(MessageCode.ERR_1917_WRONG_DETAIL_NUMBER,R.string.msg_1917);
			put(MessageCode.ERR_1918_OVER_MULTIPLE_MAX_NUMBER,R.string.msg_1918);
		}
	};

	public static String get(Context context, int code) {
		String str = "";

		try {
			int k = M.mMessageArray.get(code);
			if (k == 0) k = com.og.M.mMessageArray.get(code);
			str = context.getResources().getString(k);
		} catch (Resources.NotFoundException e) {
			e.printStackTrace();
		}
		return str;
	}

}
