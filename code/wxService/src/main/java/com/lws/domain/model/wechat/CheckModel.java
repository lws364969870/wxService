package com.lws.domain.model.wechat;

public class CheckModel extends ErrorCodeModel
{
  String signature;
  Long timestamp;
  Long nonce;
  String echostr;

  public String getSignature()
  {
    return this.signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }

  public Long getTimestamp() {
    return this.timestamp;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }

  public Long getNonce() {
    return this.nonce;
  }

  public void setNonce(Long nonce) {
    this.nonce = nonce;
  }

  public String getEchostr() {
    return this.echostr;
  }

  public void setEchostr(String echostr) {
    this.echostr = echostr;
  }
}