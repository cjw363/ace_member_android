package com.ace.member.utils;

public class AppGlobal {

	public static final String LOGIN_USER_PHONE = "login_user_phone";
	public static final String USER_PHONE = "user_phone";
	public static final String PREFERENCES_GESTURE_USER = "gesture_user";
	public static final String FINGERPRINT = "_fingerprint";//是否提示指纹标识
	public static final String GESTURE = "_gesture";//是否提示手势
	public static final String TRADING_PASSWORD = "_trading_password";//是否提示交易密码
	public static final String HINT_COUNT = "_hint_count";
	public static final int ACTION_TYPE_1_START = 1;
	public static final int ACTION_TYPE_2_SWITCH_ON = 2;
	public static final int ACTION_TYPE_3_SWITCH_OFF = 3;
	public static final int ACTION_TYPE_4_TO_SET_GESTURE = 4;//手势设置
	public static final int ACTION_TYPE_5_TO_RESET_GESTURE = 5;//手势重置
	public static final int ACTION_TYPE_6_FORGOT_GESTURE = 6;//忘记手势
	public static final int ACTION_TYPE_7_CLOSE_GESTURE = 7;//关闭手势
	public static final int ACTION_TYPE_8_CLEAR_GESTURE = 8;//清除手势
	public static final int ACTION_TYPE_9_FINGER_PAY_ON = 9;//指纹支付开启
	public static final int ACTION_TYPE_10_FINGER_PAY_OFF = 10;//指纹支付关闭
	public static final int FROM_LOGIN = 1;//从登陆页面跳转

	public static final String USD = "USD";
	public static final String KHR = "KHR";
	public static final String VND = "VND";
	public static final String THB = "THB";

	public static final int COUNTRY_CODE_84_VIETNAM = 84;
	public static final int COUNTRY_CODE_855_CAMBODIA = 855;
	public static final int COUNTRY_CODE_86_CHINA = 86;
	public static final int COUNTRY_CODE_66_THAILAND = 66;
	public static final int COUNTRY_CODE_60_MALAYSIA = 60;
	public static final int COUNTRY_CODE_62_INDONESIA = 62;
	public static final int COUNTRY_CODE_33_FRENCH = 33;

	public static final int ACTIVITY_15_RESET_PASSWORD = 15;//重置登录密码
	public static final int ACTIVITY_16_UPDATE_PASSWORD = 16;//更改登录密码

	public static final int SETTING_HINT_DAYS = 3; //提醒设置天数，如交易密码未设置的提醒，提醒3天，之后不再提醒

	public static final int STATUS_1_PENDING = 1;
	public static final int STATUS_2_APPROVED = 2;
	public static final int STATUS_3_COMPLETED = 3;
	public static final int STATUS_4_CANCELLED = 4;
	public static final int STATUS_8_LOCKED = 8;

	public static final int USER_VERIFY_STATUS_1_PENDING = 1;
	public static final int USER_VERIFY_STATUS_2_ACCEPTED = 2;
	public static final int USER_VERIFY_STATUS_4_REJECTED = 4;

	public static final int SITE_TYPE_BRANCH = 1;
	public static final int SITE_TYPE_AGENT = 2;

	public static final int TOP_UP_1_SHOW_PINCODE = 1;
	public static final int TOP_UP_2_DIRECTLY_TOP_UP = 2;
	public static final int TOP_UP_3_SEND_SMS = 3;

	public static final int TRADING_PASSWORD_STATUS_1_ACTIVE = 1;
	public static final int TRADING_PASSWORD_STATUS_3_INACTIVE = 3;
	public static final int TRADING_PASSWORD_STATUS_4_TO_SET = 4;
	public static final int TRADING_PASSWORD_STATUS_6_TO_VERIFY_CERTIFICATE = 6;

	public static final int CERTIFICATE_STATUS_1_PENDING = 1;
	public static final int CERTIFICATE_STATUS_2_ACCEPTED = 2;
	public static final int CERTIFICATE_STATUS_4_REJECTED = 4;

	public static final int PAYMENT_TYPE_1_EDC = 1;
	public static final int PAYMENT_TYPE_2_WSA = 2;

	public static final double EDC_SPLIT_1_100000 = 100000;
	public static final double WSA_SPLIT_1_50000 = 50000;
	public static final double WSA_SPLIT_2_400000 = 400000;

	public static final int PHONE_TYPE_0_NOT_REGISTER = 0;
	public static final int PHONE_TYPE_1_MEMBER = 1;

	public static final int IMAGE_CODE_1_PICKER = 1;
	public static final int IMAGE_CODE_2_CAMERA = 2;

	public static final int MEMBER_LEVEL_1_STANDARD = 1;
	public static final int MEMBER_LEVEL_2_SILVER = 2;
	public static final int MEMBER_LEVEL_3_GOLD = 3;
	public static final int MEMBER_LEVEL_4_DIAMOND = 4;

	public static final int FLOW_TYPE_1_BEGINNING = 1;
	public static final int FLOW_TYPE_2_DEPOSIT = 2;
	public static final int FLOW_TYPE_3_WITHDRAW = 3;

	public static final int APPLICATION_TYPE_2_DEPOSIT = 2;
	public static final int APPLICATION_TYPE_3_WITHDRAW = 3;

	public static final int CERTIFICATE_TYPE_1_ID = 1;
	public static final int CERTIFICATE_TYPE_2_PASSPORT = 2;
	public static final int CERTIFICATE_TYPE_3_DRIVE_LICENSE = 3;
	public static final int CERTIFICATE_TYPE_4_FAMILY_BOOK = 4;

	public static final int FLAG_LOCK_DEFAULT = 0;
	public static final int FLAG_LOCK_YES = 1;
	public static final int FLAG_LOCK_NO = 2;
	public static final int FLAG_YES = 1;
	public static final int FLAG_NO = 2;

	public static final int SEX_DEFAULT = 0;
	public static final int SEX_MALE = 1;
	public static final int SEX_FEMALE = 2;

	public static final int USER_TYPE_1_MEMBER = 1;
	public static final int USER_TYPE_2_AGENT = 2;
	public static final int USER_TYPE_3_PARTNER = 3;
	public static final int USER_TYPE_4_MERCHANT = 4;
	public static final int USER_TYPE_7_BRANCH = 7;

	public static final int USER_INPUT_TYPE_1_BY_HAND = 1;
	public static final int USER_INPUT_TYPE_2_QR_CODE = 2;


	public static final int LOTTO_BUY_TICKET_11_TYPE = 11;
	public static final int LOTTO_BUY_TICKET_12_TYPE = 12;
	public static final int LOTTO_BUY_TICKET_21_TYPE = 21;
	public static final int LOTTO_BUY_TICKET_22_TYPE = 22;

	public static final int ENTER_SELECT_BALL_1_TYPE = 1;
	public static final int ENTER_SELECT_BALL_2_TYPE = 2;

	public static final int CODE_TYPE_1_BAR_CODE = 1;
	public static final int CODE_TYPE_2_QR_CODE = 2;

	public static final int FLAG_MUTE_NOTIFICATIONS_1_YES = 1;
	public static final int FLAG_MUTE_NOTIFICATIONS_2_NO = 2;

	public static final int FINISH_CODE_BUILD_CHAT_GROUP_SUCCESS = 1;

}
