package com.lws.domain.utils.pwd;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*******************************************************************************
 * AES加解密算法
 * 
 * @author arix04
 * 
 */

public class AES {

	private final static Logger log = LoggerFactory.getLogger(AES.class);

	// 加密
	public static String Encrypt(String sSrc, String sKey, String iv) throws Exception {
		if (sKey == null) {
			log.info("Key为空null");
			return null;
		}
		// 判断Key是否为16位
		if (sKey.length() != 16) {
			log.info("Key长度不是16位");
			return null;
		}
		byte[] raw = sKey.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// "算法/模式/补码方式"
		IvParameterSpec ivs = new IvParameterSpec(iv.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivs);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes("UTF-8"));
		return Base64.encodeByte(encrypted);// 此处使用BAES64做转码功能，同时能起到2次加密的作用。
	}

	// 解密
	public static String Decrypt(String sSrc, String sKey, String iv) throws Exception {
		try {
			// 判断Key是否正确
			if (sKey == null) {
				log.info("Key为空null");
				return null;
			}
			// 判断Key是否为16位
			if (sKey.length() != 16) {
				log.info("Key长度不是16位");
				return null;
			}
			byte[] raw = sKey.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec ivs = new IvParameterSpec(iv.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivs);
			byte[] encrypted1 = Base64.decodeByte(sSrc);// 先用bAES64解密
			try {
				byte[] original = cipher.doFinal(encrypted1);
				String originalString = new String(original,"UTF-8");
				return originalString;
			} catch (Exception e) {
				log.error("解密1:" + e);
				e.printStackTrace();
				return null;
			}
		} catch (Exception ex) {
			log.error("解密2:" + ex);
			ex.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		AES aes = new AES();

		String content = "12345啊啊啊";
		String key = "0123456789abcdef";
		String iv = "0123456789abcdef";
		try {
			System.out.println(aes.Encrypt(content, key, iv));
			System.out.println(aes.Decrypt("zREuS8L2rS31LLsfetmFIA==", key, iv));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}