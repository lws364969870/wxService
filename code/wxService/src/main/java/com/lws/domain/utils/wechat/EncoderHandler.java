package com.lws.domain.utils.wechat;

import java.security.MessageDigest;

public class EncoderHandler {
	private static final String ALGORITHM = "MD5";
	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String encode(String algorithm, String str) {
		if (str == null)
			return null;
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			messageDigest.update(str.getBytes());
			return getFormattedText(messageDigest.digest());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String encodeByMD5(String str) {
		if (str == null)
			return null;
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(str.getBytes());
			return getFormattedText(messageDigest.digest());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String getFormattedText(byte[] bytes) {
		int len = bytes.length;
		StringBuilder buf = new StringBuilder(len * 2);

		for (int j = 0; j < len; ++j) {
			buf.append(HEX_DIGITS[(bytes[j] >> 4 & 0xF)]);
			buf.append(HEX_DIGITS[(bytes[j] & 0xF)]);
		}
		return buf.toString();
	}
}