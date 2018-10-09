package com.og.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptSHA {
	//byte字节转换成16进制的字符串MD5Utils.hexString
	public static byte[] encrypt(String info, String shaType) throws NoSuchAlgorithmException {
		MessageDigest sha = MessageDigest.getInstance(shaType);
		byte[] srcBytes = info.getBytes();
		// 使用srcBytes更新摘要
		sha.update(srcBytes);
		// 完成哈希计算
		return sha.digest();
	}

	public static String sha1(String info) {
		String str = info;
		try {
			str = hexString(encrypt(info, "SHA1"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return str;
	}

	public static byte[] sha256(String info) throws NoSuchAlgorithmException {
		return encrypt(info, "SHA-256");
	}

	public static byte[] sha384(String info) throws NoSuchAlgorithmException {
		return encrypt(info, "SHA-384");
	}

	public static byte[] sha512(String info) throws NoSuchAlgorithmException {
		return encrypt(info, "SHA-512");
	}

	public static String hexString(byte[] bytes) {
		try{
			StringBuilder b = new StringBuilder();

			for (int i = 0; i < bytes.length; i++) {
				int val = ((int) bytes[i]) & 0xff;
				if (val < 16)
					b.append("0");
				b.append(Integer.toHexString(val));
			}
			return b.toString();
		}catch (Exception e){
			FileUtils.addErrorLog(e);
		}
		return null;
	}
}
