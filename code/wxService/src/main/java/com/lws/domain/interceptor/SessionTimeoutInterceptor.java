package com.lws.domain.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class SessionTimeoutInterceptor implements HandlerInterceptor {
	public String[] allowUrls;

	public void setAllowUrls(String[] allowUrls) {
		this.allowUrls = allowUrls;
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		String requestUrl = request.getRequestURI().replace(request.getContextPath(), "");
		String[] nurl = { "wechat" };

		response.setHeader("g520", "g520");
		if ((this.allowUrls != null) && (this.allowUrls.length >= 1)) {
			for (String url : this.allowUrls) {
				if (requestUrl.contains(url)) {
					return true;
				}
			}
		}

		Object sezzion = request.getSession().getAttribute("user");
		if (sezzion != null) {
			return true;
		}
		response.setHeader("G", "G");

		throw new SessionTimeoutException();
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception arg3) throws Exception {
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object arg2, ModelAndView arg3) throws Exception {
	}
}