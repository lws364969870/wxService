package com.lws.domain.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;

@Entity
@DynamicInsert
@Table(name="syconfig")
public class Syconfig
  implements Serializable
{

  @Id
  @GeneratedValue
  private Long syConfigId;
  private String configKey;
  private String configValue;
  private String configType;
  private String remark;
  private String isShow;

  public Long getSyConfigId()
  {
    return this.syConfigId;
  }

  public void setSyConfigId(Long syConfigId) {
    this.syConfigId = syConfigId;
  }

  public String getConfigKey() {
    return this.configKey;
  }

  public void setConfigKey(String configKey) {
    this.configKey = configKey;
  }

  public String getConfigValue() {
    return this.configValue;
  }

  public void setConfigValue(String configValue) {
    this.configValue = configValue;
  }

  public String getConfigType() {
    return this.configType;
  }

  public void setConfigType(String configType) {
    this.configType = configType;
  }

  public String getRemark() {
    return this.remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getIsShow() {
    return this.isShow;
  }

  public void setIsShow(String isShow) {
    this.isShow = isShow;
  }
}