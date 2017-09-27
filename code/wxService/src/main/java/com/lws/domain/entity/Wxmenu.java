package com.lws.domain.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;

@Entity
@DynamicInsert
@Table(name="wxmenu")
public class Wxmenu
  implements Serializable
{

  @Id
  @GeneratedValue
  private Long wxMenuId;
  private String menuName;
  private String menuUrl;
  private String isCatalog;
  private String menuCode;
  private String parentsCode;
  private Integer menuLevel;

  public Long getWxMenuId()
  {
    return this.wxMenuId;
  }

  public void setWxMenuId(Long wxMenuId) {
    this.wxMenuId = wxMenuId;
  }

  public String getMenuName() {
    return this.menuName;
  }

  public void setMenuName(String menuName) {
    this.menuName = menuName;
  }

  public String getMenuUrl() {
    return this.menuUrl;
  }

  public void setMenuUrl(String menuUrl) {
    this.menuUrl = menuUrl;
  }

  public String getIsCatalog() {
    return this.isCatalog;
  }

  public void setIsCatalog(String isCatalog) {
    this.isCatalog = isCatalog;
  }

  public String getMenuCode() {
    return this.menuCode;
  }

  public void setMenuCode(String menuCode) {
    this.menuCode = menuCode;
  }

  public String getParentsCode() {
    return this.parentsCode;
  }

  public void setParentsCode(String parentsCode) {
    this.parentsCode = parentsCode;
  }

  public Integer getMenuLevel() {
    return this.menuLevel;
  }

  public void setMenuLevel(Integer menuLevel) {
    this.menuLevel = menuLevel;
  }
}