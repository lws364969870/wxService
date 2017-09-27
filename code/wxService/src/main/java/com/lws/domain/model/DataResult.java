package com.lws.domain.model;

/**
 * 返回前端封装包
 * @author WilsonLee
 * 
 */
public class DataResult {
	private String message;
	private String flag;
	private Object data;

	public DataResult() {
		this.flag = "1";
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFlag() {
		return this.flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Object getData() {
		return this.data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}