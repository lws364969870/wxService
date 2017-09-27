package com.lws.domain.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;

@Entity
@DynamicInsert
@Table(name="subscribe")
public class Subscribe
  implements Serializable
{

  @Id
  @GeneratedValue
  private Long subscribeId;
  private String nickname;
  private String sex;
  private String remark;
  private String openid;
  private String isShow;
  private String headimgurl;

  public Long getSubscribeId()
  {
    return this.subscribeId;
  }

  public void setSubscribeId(Long subscribeId) {
    this.subscribeId = subscribeId;
  }

  public String getNickname() {
    return this.nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getSex() {
    return this.sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public String getRemark() {
    return this.remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getOpenid() {
    return this.openid;
  }

  public void setOpenid(String openid) {
    this.openid = openid;
  }

  public String getHeadimgurl() {
    return this.headimgurl;
  }

  public void setHeadimgurl(String headimgurl) {
    this.headimgurl = headimgurl;
  }

  public String getIsShow() {
    return this.isShow;
  }

  public void setIsShow(String isShow) {
    this.isShow = isShow;
  }
}