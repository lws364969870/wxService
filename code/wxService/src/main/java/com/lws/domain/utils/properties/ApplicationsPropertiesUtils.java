package com.lws.domain.utils.properties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public final class ApplicationsPropertiesUtils {
	private static volatile Properties mProperties = new Properties();

	static {
		InputStream in = ApplicationsPropertiesUtils.class.getResourceAsStream("/applications.properties");
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(in));
			mProperties.load(bf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getValue(String key) {
		if (mProperties == null)
			return "";
		return mProperties.getProperty(key, "");
	}
}