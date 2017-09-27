package com.lws.domain.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;

@Entity
@DynamicInsert
@Table(name = "author")
public class Author implements Serializable {

	@Id
	@GeneratedValue
	private Long authorId;
	private String authorName;
	private String isHide;

	public Long getAuthorId() {
		return this.authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public String getAuthorName() {
		return this.authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getIsHide() {
		return this.isHide;
	}

	public void setIsHide(String isHide) {
		this.isHide = isHide;
	}
}