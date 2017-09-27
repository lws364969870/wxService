package com.lws.domain.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtils {
	public static boolean isEmpty(String str) {
		return (str == null) || (str.length() == 0);
	}

	public static String getExt(String name) {
		if ((name == null) || ("".equals(name)) || (!(name.contains("."))))
			return "";
		return name.substring(name.lastIndexOf(".") + 1);
	}

	public static String getFileName(String name) {
		if ((name == null) || ("".equals(name)) || (!(name.contains("."))) || (!(name.contains("/")))) {
			return "";
		}
		return name.substring(name.lastIndexOf("/") + 1, name.length());
	}

	public static String getImgArrayString(String url) {
		if ((url == null) || ("".equals(url)) || (!(url.contains("."))) || (!(url.contains("/"))))
			return "";
		Pattern ATTR_PATTERN = Pattern.compile("<img[^<>]*?\\ssrc=['\"]?(.*?)['\"]?(\\s.*?)?/>");
		Matcher matcher = ATTR_PATTERN.matcher(url);
		StringBuffer str = new StringBuffer("");
		while (matcher.find()) {
			if (str.length() < 1)
				str.append(matcher.group(1));
			else {
				str.append("," + matcher.group(1));
			}
		}
		return str.toString();
	}
	
	public static String replaceBlank(String str) {
		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	public static String getServerRequestURL(String url) {
		if (isEmpty(url))
			return "";
		return url.substring(0, url.indexOf("wxService/") - 1);
	}

	public static String getServerRequestPojoURL(String url) {
		if (isEmpty(url))
			return "";
		return url.substring(0, url.indexOf("wxService/") + 10);
	}

	public static String getVisitURL(String url) {
		if (isEmpty(url))
			return "";
		return url.substring(url.indexOf("/upload/"), url.length());
	}
}