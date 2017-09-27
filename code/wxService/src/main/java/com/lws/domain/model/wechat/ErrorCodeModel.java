package com.lws.domain.model.wechat;

public class ErrorCodeModel
{
  private String errcode;
  private String errmsg;

  public String getErrcode()
  {
    return this.errcode;
  }

  public void setErrcode(String errcode) {
    this.errcode = errcode;
  }

  public String getErrmsg() {
    return this.errmsg;
  }

  public void setErrmsg(String errmsg) {
    this.errmsg = errmsg;
  }
}