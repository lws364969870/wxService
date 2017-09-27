package com.lws.domain.model.wechat;

public class ArticlesModel
{
  private String thumb_media_id;
  private String author;
  private String title;
  private String content_source_url;
  private String content;
  private String digest;
  private String show_cover_pic;

  public String getThumb_media_id()
  {
    return this.thumb_media_id;
  }

  public void setThumb_media_id(String thumb_media_id) {
    this.thumb_media_id = thumb_media_id;
  }

  public String getAuthor() {
    return this.author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent_source_url() {
    return this.content_source_url;
  }

  public void setContent_source_url(String content_source_url) {
    this.content_source_url = content_source_url;
  }

  public String getContent() {
    return this.content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getDigest() {
    return this.digest;
  }

  public void setDigest(String digest) {
    this.digest = digest;
  }

  public String getShow_cover_pic() {
    return this.show_cover_pic;
  }

  public void setShow_cover_pic(String show_cover_pic) {
    this.show_cover_pic = show_cover_pic;
  }
}