package com.lws.domain.model;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;

@Entity
@DynamicInsert
@Table(name = "article")
public class ArticleQueryModel implements Serializable {
	private Long articleId;
	private String title;
	private String content;
	private Timestamp createDate;
	private Long createUserId;
	private Timestamp editDate;
	private Long editUserId;
	private String createDateFrom;
	private String author;
	private String status;
	private String articleType;
	private String wxContent;
	private String wxPicUrl;
	private String syroleType;
	private String sendFlag;
	private String thumbMediaId;
	private Timestamp mediaDate;
	private String mediaConten;
	private Timestamp sendDate;
	private String mediaId;
	private String msgId;
	private String createDateTo;

	public String getCreateDateFrom() {
		return this.createDateFrom;
	}

	public void setCreateDateFrom(String createDateFrom) {
		this.createDateFrom = createDateFrom;
	}

	public String getCreateDateTo() {
		return this.createDateTo;
	}

	public void setCreateDateTo(String createDateTo) {
		this.createDateTo = createDateTo;
	}

	public Long getArticleId() {
		return this.articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Long getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Timestamp getEditDate() {
		return this.editDate;
	}

	public void setEditDate(Timestamp editDate) {
		this.editDate = editDate;
	}

	public Long getEditUserId() {
		return this.editUserId;
	}

	public void setEditUserId(Long editUserId) {
		this.editUserId = editUserId;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getWxPicUrl() {
		return this.wxPicUrl;
	}

	public void setWxPicUrl(String wxPicUrl) {
		this.wxPicUrl = wxPicUrl;
	}

	public String getWxContent() {
		return this.wxContent;
	}

	public void setWxContent(String wxContent) {
		this.wxContent = wxContent;
	}

	public String getSyroleType() {
		return this.syroleType;
	}

	public void setSyroleType(String syroleType) {
		this.syroleType = syroleType;
	}

	public String getSendFlag() {
		return this.sendFlag;
	}

	public void setSendFlag(String sendFlag) {
		this.sendFlag = sendFlag;
	}

	public String getThumbMediaId() {
		return this.thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

	public Timestamp getMediaDate() {
		return this.mediaDate;
	}

	public void setMediaDate(Timestamp mediaDate) {
		this.mediaDate = mediaDate;
	}

	public Timestamp getSendDate() {
		return this.sendDate;
	}

	public void setSendDate(Timestamp sendDate) {
		this.sendDate = sendDate;
	}

	public String getMediaId() {
		return this.mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getMsgId() {
		return this.msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getArticleType() {
		return this.articleType;
	}

	public void setArticleType(String articleType) {
		this.articleType = articleType;
	}

	public String getMediaConten() {
		return this.mediaConten;
	}

	public void setMediaConten(String mediaConten) {
		this.mediaConten = mediaConten;
	}
}