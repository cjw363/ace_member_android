package com.og;


import android.content.Context;
import android.content.res.Resources;
import android.util.SparseIntArray;

public class M{

	//3位数字，系统级

	public static final class MessageCode {

		public static final int ERR_100_SYSTEM_ERROR = 100;
		private static final int ERR_101_INVALID = 101;
		public static final int ERR_102_SESSION_TIMEOUT = 102;
		private static final int ERR_103_SUBMIT_REPEAT = 103;
		public static final int ERR_104_NOT_SUPPORTED_APK_VERSION = 104;

		private static final int ERR_200_NO_PRIVILEGE = 200;

		public static final int ERR_400_FATAL_ERROR = 400;
		public static final int ERR_401_NETWORK_INVALID = 401;
		public static final int ERR_402_NETWORK_ERROR = 402;
		public static final int ERR_403_NETWORK_TIMEOUT = 403;

		public static final int ERR_411_UPDATE_ERROR = 411;
		public static final int ERR_412_START_DOWNLOAD = 412;
		public static final int ERR_413_DOWNLOAD_FINISH = 413;
		public static final int ERR_414_CANCEL_DOWNLOAD = 414;
		public static final int ERR_415_NO_UPDATE = 415;
		public static final int ERR_416_NEW_VER_FOUND = 416;

		public static final int ERR_420_DATA_ERROR = 420;
		public static final int ERR_421_DATA_PARSING_ERROR = 421;

		public static final int ERR_430_SESSION_TIME_OUT_TO_LOGIN = 430;

		public static final int ERR_440_SAVE_GESTURE_FAIL = 440;
		public static final int ERR_441_CLOSE_BLOCKING_DIALOG = 441;
		public static final int ERR_442_GET_SERVICE_INFO_SUCCESS = 442;

		public static final int ERR_500_NOT_ALLOW_LOGIN = 500;

		public static final int ERR_505_FUNCTION_NOT_RUNNING = 505;
	}
	protected final static SparseIntArray mMessageArray = new SparseIntArray() {
		{
			//系统级
			put(MessageCode.ERR_101_INVALID, R.string.msg_101);
			put(MessageCode.ERR_102_SESSION_TIMEOUT, R.string.msg_102);
			put(MessageCode.ERR_103_SUBMIT_REPEAT, R.string.msg_103);
			put(MessageCode.ERR_104_NOT_SUPPORTED_APK_VERSION, R.string.msg_104);
			put(MessageCode.ERR_200_NO_PRIVILEGE, R.string.msg_200);
		}
	};

	public static String get(Context context, int code) {
		String str = "";

		try {
			str = context.getResources().getString(M.mMessageArray.get(code));
		} catch (Resources.NotFoundException e) {
			e.printStackTrace();
		}
		return str;
	}

}
