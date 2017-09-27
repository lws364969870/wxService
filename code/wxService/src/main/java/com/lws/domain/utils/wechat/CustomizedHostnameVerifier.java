package com.lws.domain.utils.wechat;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class CustomizedHostnameVerifier implements HostnameVerifier {
	public boolean verify(String arg0, SSLSession arg1) {
		return true;
	}
}