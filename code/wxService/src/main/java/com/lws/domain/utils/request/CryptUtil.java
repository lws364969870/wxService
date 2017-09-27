package com.lws.domain.utils.request;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.RandomStringUtils;

import com.lws.domain.utils.pwd.AES;

public class CryptUtil {
	
	public static final String defaultSessionKey = "0123456789abcdef";
	public static final String defaultSessionIv = "0123456789abcdef";

	/**
	 * 对json串进行加密
	 * @param sessionKey
	 * @param jsonData
	 * @return
	 */
	public static String Encrypt(String key, String iv, String jsonData) throws RuntimeException {
		try {
			return AES.Encrypt(jsonData, key, iv);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 对数据字符串进行解密，返回String
	 * @param sessionKey
	 * @param dataStr
	 * @return
	 */
	public static String Decrypt(String key, String iv, String dataStr) throws RuntimeException {
		try {
			if (key != null) {
				return AES.Decrypt(dataStr, key, iv);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String createKeyStr() {
		// TODO 生成一个16位的key串
		String s = RandomStringUtils.randomAlphanumeric(16);
		return s;
	}

	public static String createIVStr() {
		// TODO 生成一个16位的IV串
		Date now = new Date();
		SimpleDateFormat outFormat = new SimpleDateFormat("yyMMddHHmmss");
		// 随机数
		String currTime = outFormat.format(now);
		// 留位随机数
		String strRandom = buildRandom(4) + "";
		// 16位序列号,可以自行调整。
		return currTime + strRandom;
	}

	/**
	 * 取出一个指定长度大小的随机正整数.
	 * 
	 * @param length int 设定所取出随机数的长度。length小于11
	 * @return int 返回生成的随机数。
	 */
	public static int buildRandom(int length) {
		int num = 1;
		double random = Math.random();
		if (random < 0.1) {
			random = random + 0.1;
		}
		for (int i = 0; i < length; i++) {
			num = num * 10;
		}
		return (int) (random * num);
	}
	
	public static String getDefaultSessionKey(){
		return defaultSessionKey;
	}

	public static String getDefaultSessionIv(){
		return defaultSessionIv;
	}
	
}
