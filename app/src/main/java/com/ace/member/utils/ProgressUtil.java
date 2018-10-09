package com.ace.member.utils;

import java.text.DecimalFormat;

public class ProgressUtil {

	public static String calculateFlowProgress(long total, long current) {
		DecimalFormat df = new DecimalFormat("######0.00");
		if (total != 0) {
			long KB = total / 1024;
			if (KB == 0) {
				return current + " B" + " / " + total + " B";
			}

			long MB = KB / 1024;
			if (MB == 0) {
				long cur = current / 1024;
				if (cur == 0) {
					return current + " B" + " / " + df.format(((double) total) / 1024) + " KB";
				}
				return df.format(((double) current) / 1024) + " KB" + " / " + df.format(((double) total) / 1024) + " KB";
			}

			long GB = MB / 1024;
			if (GB == 0) {
				long cur = current / 1024;
				if (cur == 0) {
					return current + " B" + " / " + ((double) total) / 1024 / 1024 + " MB";
				} else {
					cur /= 1024;
					if (cur == 0) {
						return df.format(((double) current) / 1024) + " KB" + " / " + df.format(((double) total) / 1024 / 1024) + " MB";
					} else {
						return df.format(((double) current) / 1024 / 1024) + " MB" + " / " + df.format(((double) total) / 1024 / 1024) + " MB";
					}
				}
			}

			double TB = ((double) GB) / 1024;

			long cur = current / 1024;
			if (cur == 0) {
				return current + " B" + " / " + ((double) total) / 1024 / 1024 / 1024 + " GB";
			} else {
				cur /= 1024;
				if (cur == 0) {
					return df.format(((double) current) / 1024) + " KB" + " / " + df.format(((double) total) / 1024 / 1024 / 1024) + " GB";
				} else {
					cur /= 1024;
					if (cur == 0) {
						return df.format(((double) current) / 1024 / 1024) + " MB" + " / " + df.format(((double) total) / 1024 / 1024 / 1024) + " GB";
					} else {
						return df.format(((double) current) / 1024 / 1024 / 1024) + " GB" + " / " + df.format(((double) total) / 1024 / 1024 / 1024) + " GB";
					}
				}
			}

		}
		return "未知大小";
	}

	public static String calculateFlowProgress(long speed, int second) {
		if (speed < 0) {
			return "未知";
		}
		DecimalFormat df = new DecimalFormat("######0.00");
		long KB = speed / 1024;
		if (KB == 0) {
			return speed / second + " B" + " / S";
		}

		long MB = KB / 1024;
		if (MB == 0) {
			return df.format(((double) speed) / 1024 / second) + " KB" + " / S";
		}

		long GB = MB / 1024;
		if (GB == 0) {
			return df.format(((double) speed) / 1024 / 1024 / second) + " MB" + " / S";
		}

		long TB = GB / 1024;
		if (TB == 0) {
			return df.format(((double) speed) / 1024 / 1024 / 1024 / second) + " GB" + " / S";
		} else {
			return df.format(((double) speed) / 1024 / 1024 / 1024 / 1024 / second) + " TB" + " / S";
		}
	}

	public static String calculateFileSize(long size) {
		if (size < 0) {
			return "未知";
		}
		DecimalFormat df = new DecimalFormat("######0.00");
		long KB = size / 1024;
		if (KB == 0) {
			return size + " B";
		}

		long MB = KB / 1024;
		if (MB == 0) {
			return df.format(((double) size) / 1024) + " KB";
		}

		long GB = MB / 1024;
		if (GB == 0) {
			return df.format(((double) size) / 1024 / 1024) + " MB";
		}

		long TB = GB / 1024;
		if (TB == 0) {
			return df.format(((double) size) / 1024 / 1024 / 1024) + " GB";
		} else {
			return df.format(((double) size) / 1024 / 1024 / 1024 / 1024) + " TB";
		}
	}


}
