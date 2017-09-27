package com.lws.domain.utils.pwd;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	public static final String MD5_lower32(String plainText) {
		String md5Str = null;
		try {
			StringBuffer buf = new StringBuffer();
			MessageDigest md = MessageDigest.getInstance("MD5");

			md.update(plainText.getBytes());

			byte[] b = md.digest();

			for (int offset = 0; offset < b.length; ++offset) {
				int i = b[offset];
				if (i < 0) {
					i += 256;
				}
				if (i < 16) {
					buf.append("0");
				}

				buf.append(Integer.toHexString(i));
			}

			md5Str = buf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return md5Str;
	}

	public static String MD5_upper32(String plainText) {
		StringBuffer hexString = null;
		byte[] defaultBytes = plainText.getBytes();
		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(defaultBytes);
			byte[] messageDigest = algorithm.digest();

			hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; ++i) {
				if (Integer.toHexString(0xFF & messageDigest[i]).length() == 1) {
					hexString.append(0);
				}
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			}
			messageDigest.toString();

			plainText = hexString.toString();
		} catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
		}
		return hexString.toString().toUpperCase();
	}

	
	
	public static void main(String[] arge) {
		System.out.println(MD5_lower32("aa"));
		System.out.println(MD5_upper32("aa"));

		System.out.println(MD5_upper32("1"));
	}
}