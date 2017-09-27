package com.lws.domain.utils.request;

import javax.servlet.http.HttpServletRequest;

import com.lws.domain.model.SessionData;
import com.lws.domain.utils.StringUtils;
import com.lws.domain.utils.pwd.AES;

public class RequestUtil {

	public static String decryptRequestJson(HttpServletRequest request, SessionData sessionData) throws Exception {

		String jsonData = request.getParameter("jsonData");
		if (StringUtils.isEmpty(jsonData)) {
			throw new Exception("jsonData为空");
		}
		String key = sessionData.getSessionKey();
		String iv = sessionData.getSessionIv();

		System.out.println("jsonData=" + jsonData);
		System.out.println("key=" + key);
		System.out.println("iv=" + iv);

		String decryptStr = AES.Decrypt(jsonData, key, iv);
		return decryptStr;
	}

	public static String decryptRequestJson(HttpServletRequest request, String key, String iv) throws Exception {

		String jsonData = request.getParameter("jsonData");
		if (StringUtils.isEmpty(jsonData)) {
			throw new Exception("jsonData为空");
		}
		
		System.out.println("jsonData=" + jsonData);
		System.out.println("key=" + key);
		System.out.println("iv=" + iv);

		String decryptStr = AES.Decrypt(jsonData, key, iv);
		return decryptStr;
	}

	public static SessionData getRequestSessionUser(HttpServletRequest request) {
		SessionData sessionData = (SessionData) request.getSession().getAttribute("user");
		return sessionData;
	}
}
