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
@Table(name = "sylog")
public class Sylog implements Serializable {

	@Id
	@GeneratedValue
	private Long sylogid;
	private Long syuserid;
	private String tablename;
	private Long tableid;
	private String content;
	private String logtype;
	private Timestamp logdate;

	public Long getSylogid() {
		return sylogid;
	}

	public void setSylogid(Long sylogid) {
		this.sylogid = sylogid;
	}

	public Long getSyuserid() {
		return syuserid;
	}

	public void setSyuserid(Long syuserid) {
		this.syuserid = syuserid;
	}

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public Long getTableid() {
		return tableid;
	}

	public void setTableid(Long tableid) {
		this.tableid = tableid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLogtype() {
		return logtype;
	}

	public void setLogtype(String logtype) {
		this.logtype = logtype;
	}

	public Timestamp getLogdate() {
		return logdate;
	}

	public void setLogdate(Timestamp logdate) {
		this.logdate = logdate;
	}

}