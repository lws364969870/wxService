package com.lws.domain.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;

@Entity
@DynamicInsert
@Table(name="symenu")
public class Symenu
  implements Serializable
{

  @Id
  @GeneratedValue
  private Long syMenuId;
  private String menuName;
  private String menuTitle;
  private String menuCode;
  private String parentsCode;
  private Integer menuLevel;
  private String menuUrl;
  private String isDisable;
  private String isCatalog;
  private String isHide;
  private Integer sort;
  private String remarks;

  public Long getSyMenuId()
  {
    return this.syMenuId;
  }

  public void setSyMenuId(Long syMenuId) {
    this.syMenuId = syMenuId;
  }

  public String getMenuName() {
    return this.menuName;
  }

  public void setMenuName(String menuName) {
    this.menuName = menuName;
  }

  public String getMenuTitle() {
    return this.menuTitle;
  }

  public void setMenuTitle(String menuTitle) {
    this.menuTitle = menuTitle;
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

  public String getMenuUrl() {
    return this.menuUrl;
  }

  public void setMenuUrl(String menuUrl) {
    this.menuUrl = menuUrl;
  }

  public String getIsDisable() {
    return this.isDisable;
  }

  public void setIsDisable(String isDisable) {
    this.isDisable = isDisable;
  }

  public String getIsCatalog() {
    return this.isCatalog;
  }

  public void setIsCatalog(String isCatalog) {
    this.isCatalog = isCatalog;
  }

  public String getIsHide() {
    return this.isHide;
  }

  public void setIsHide(String isHide) {
    this.isHide = isHide;
  }

  public Integer getSort() {
    return this.sort;
  }

  public void setSort(Integer sort) {
    this.sort = sort;
  }

  public String getRemarks() {
    return this.remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }
}