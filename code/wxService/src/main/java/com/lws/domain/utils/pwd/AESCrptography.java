package com.lws.domain.utils.pwd;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class AESCrptography {
	public static void main(String[] args) {

		
		
		String content = "jwoApABGpRAEX9Tc1BJDBQ==";
		String key = "0123456789abcdef";
		String iv = "0123456789abcdef";

		System.out.println("加密前：" + hexStr2Str(byteToHexString(content.getBytes())));
		byte[] encrypted = AES_CBC_Encrypt(content.getBytes(), key.getBytes(), iv.getBytes());
		System.out.println("加密后：" + byteToHexString(encrypted));
		System.out.println("加密后：" + hexStr2Str(byteToHexString(encrypted)));
		
		byte[] decrypted = AES_CBC_Decrypt(Base64.getByteFromBase64(content), key.getBytes(), iv.getBytes());
		System.out.println("解密后：" + byteToHexString(decrypted));
		System.out.println("解密后：" + hexStr2Str(byteToHexString(decrypted)));
	}

	public static byte[] AES_CBC_Encrypt(byte[] content, byte[] keyBytes, byte[] iv) {

		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			keyGenerator.init(128, new SecureRandom(keyBytes));
			SecretKey key = keyGenerator.generateKey();
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
			byte[] result = cipher.doFinal(content);
			return result;
		} catch (Exception e) {
			System.out.println("exception:" + e.toString());
		}
		return null;
	}

	public static byte[] AES_CBC_Decrypt(byte[] content, byte[] keyBytes, byte[] iv) {

		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			keyGenerator.init(128, new SecureRandom(keyBytes));// key长可设为128，192，256位，这里只能设为128
			SecretKey key = keyGenerator.generateKey();
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
			byte[] result = cipher.doFinal(content);
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("exception:" + e.toString());
		}
		return null;
	}

	public static String byteToHexString(byte[] bytes) {
		StringBuffer sb = new StringBuffer(bytes.length);
		String sTemp;
		for (int i = 0; i < bytes.length; i++) {
			sTemp = Integer.toHexString(0xFF & bytes[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 十六进制转换字符串
	 * @param String str Byte字符串(Byte之间无分隔符 如:[616C6B])
	 * @return String 对应的字符串
	 */
	public static String hexStr2Str(String hexStr) {
		String str = "0123456789ABCDEF";
		char[] hexs = hexStr.toCharArray();
		byte[] bytes = new byte[hexStr.length() / 2];
		int n;

		for (int i = 0; i < bytes.length; i++) {
			n = str.indexOf(hexs[2 * i]) * 16;
			n += str.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (n & 0xff);
		}
		return new String(bytes);
	}

}
