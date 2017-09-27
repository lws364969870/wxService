package com.lws.domain.utils.properties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public final class WechatErrorCodePropertiesUtils {
	private static volatile Properties eProperties = new Properties();

	static {
		InputStream in = WechatErrorCodePropertiesUtils.class.getResourceAsStream("/wechatErrorCode.properties");
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(in));
			eProperties.load(bf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getValue(String key) {
		if (eProperties == null)
			return "";
		return eProperties.getProperty(key, "");
	}
}