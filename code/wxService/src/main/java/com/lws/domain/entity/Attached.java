package com.lws.domain.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;

@Entity
@DynamicInsert
@Table(name = "attached")
public class Attached implements Serializable {

	@Id
	@GeneratedValue
	private Long attachedId;
	private Long articleId;
	private String absoluteURL;
	private String networdURL;
	private String visitURL;
	private String wechatURL;

	public Long getAttachedId() {
		return this.attachedId;
	}

	public void setAttachedId(Long attachedId) {
		this.attachedId = attachedId;
	}

	public Long getArticleId() {
		return this.articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}

	public String getAbsoluteURL() {
		return this.absoluteURL;
	}

	public void setAbsoluteURL(String absoluteURL) {
		this.absoluteURL = absoluteURL;
	}

	public String getNetwordURL() {
		return this.networdURL;
	}

	public void setNetwordURL(String networdURL) {
		this.networdURL = networdURL;
	}

	public String getVisitURL() {
		return this.visitURL;
	}

	public void setVisitURL(String visitURL) {
		this.visitURL = visitURL;
	}

	public String getWechatURL() {
		return this.wechatURL;
	}

	public void setWechatURL(String wechatURL) {
		this.wechatURL = wechatURL;
	}
}