package com.lws.domain.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;

@Entity
@DynamicInsert
@Table(name="wordbook")
public class Wordbook
  implements Serializable
{

  @Id
  @GeneratedValue
  private Long wordbookId;
  private String wordbookName;
  private String wordbookCode;
  private String parentsBookCode;
  private String remarks;

  public Long getWordbookId()
  {
    return this.wordbookId;
  }

  public void setWordbookId(Long wordbookId) {
    this.wordbookId = wordbookId;
  }

  public String getWordbookName() {
    return this.wordbookName;
  }

  public void setWordbookName(String wordbookName) {
    this.wordbookName = wordbookName;
  }

  public String getWordbookCode() {
    return this.wordbookCode;
  }

  public void setWordbookCode(String wordbookCode) {
    this.wordbookCode = wordbookCode;
  }

  public String getParentsBookCode() {
    return this.parentsBookCode;
  }

  public void setParentsBookCode(String parentsBookCode) {
    this.parentsBookCode = parentsBookCode;
  }

  public String getRemarks() {
    return this.remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }
}