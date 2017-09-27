package com.lws.domain.model;

/**
 * 返回给前端的session
 * @author WilsonLee
 * 
 */
public class SessionData {
	private String userId;
	private String loginName;
	private String userName;
	private String roleType;
	private String roleName;
	private String ip;
	private String sessionKey;
	private String sessionIv;

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRoleType() {
		return this.roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getSessionIv() {
		return sessionIv;
	}

	public void setSessionIv(String sessionIv) {
		this.sessionIv = sessionIv;
	}

}