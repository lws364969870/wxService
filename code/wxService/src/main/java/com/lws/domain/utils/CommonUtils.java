package com.lws.domain.utils;

public class CommonUtils {

	public static String getTime(long time) {
		String str = "";
		time = time / 1000;
		int s = (int) (time % 60);
		int m = (int) (time / 60 % 60);
		int h = (int) (time / 3600);
		str = h + "小时" + m + "分" + s + "秒";
		return str;
	}
}
