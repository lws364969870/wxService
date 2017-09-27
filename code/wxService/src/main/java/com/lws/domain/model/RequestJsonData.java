package com.lws.domain.model;

/**
 * controller接口中转换json数据
 * @author WilsonLee
 * 
 */
public class RequestJsonData {

	private String sessionKey;
	private String sessionData;

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getSessionData() {
		return sessionData;
	}

	public void setSessionData(String sessionData) {
		this.sessionData = sessionData;
	}

}
