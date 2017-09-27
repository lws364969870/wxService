package com.lws.domain.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

@Entity
@DynamicInsert
@Table(name = "syuser")
public class Syuser implements Serializable {

	@Id
	@GeneratedValue
	private Long syUserId;
	private String userName;
	private String loginName;
	private String passWord;
	private Long syRoleId;
	private Long syUnitId;
	private String phone;
	private String mobile;
	private String email;
	private String isDisable;
	private Long sort;
	private String remarks;
	private Long errorcount;
	private Timestamp errortime;

	public Long getSyUserId() {
		return this.syUserId;
	}

	public void setSyUserId(Long syUserId) {
		this.syUserId = syUserId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassWord() {
		return this.passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public Long getSyRoleId() {
		return this.syRoleId;
	}

	public void setSyRoleId(Long syRoleId) {
		this.syRoleId = syRoleId;
	}

	public Long getSyUnitId() {
		return this.syUnitId;
	}

	public void setSyUnitId(Long syUnitId) {
		this.syUnitId = syUnitId;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIsDisable() {
		return this.isDisable;
	}

	public void setIsDisable(String isDisable) {
		this.isDisable = isDisable;
	}

	public Long getSort() {
		return this.sort;
	}

	public void setSort(Long sort) {
		this.sort = sort;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long getErrorcount() {
		return errorcount;
	}

	public void setErrorcount(Long errorcount) {
		this.errorcount = errorcount;
	}

	public Timestamp getErrortime() {
		return errortime;
	}

	public void setErrortime(Timestamp errortime) {
		this.errortime = errortime;
	}

}