package com.lws.domain.model;

import com.lws.domain.entity.Wxmenu;
import java.io.Serializable;

public class WxmenuModel
  implements Serializable
{
  private Long wxMenuId;
  private String menuName;
  private String menuUrl;
  private String isCatalog;
  private String menuCode;
  private String parentsCode;
  private Integer menuLevel;
  private Object child;

  public void copyBean(Wxmenu wxmenu)
  {
    setWxMenuId(wxmenu.getWxMenuId());
    setMenuName(wxmenu.getMenuName());
    setMenuUrl(wxmenu.getMenuUrl());
    setIsCatalog(wxmenu.getIsCatalog());
    setMenuCode(wxmenu.getMenuCode());
    setParentsCode(wxmenu.getParentsCode());
    setMenuLevel(wxmenu.getMenuLevel());
  }

  public Long getWxMenuId() {
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

  public Object getChild() {
    return this.child;
  }

  public void setChild(Object child) {
    this.child = child;
  }
}