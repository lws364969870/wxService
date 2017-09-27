package com.lws.domain.service;

import com.lws.domain.dao.AuthorDAO;
import com.lws.domain.entity.Author;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class AuthorService
{

  @Resource
  AuthorDAO authorDAO;

  public List findAll()
  {
    List entitylist = new ArrayList();
    try {
      entitylist = this.authorDAO.findAll();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return entitylist;
  }

  public Author findById(Long id) throws Exception {
    return this.authorDAO.findById(id);
  }

  public void save(Author author) throws Exception {
    this.authorDAO.save(author);
  }

  public int deleteById(Long authorId) throws Exception {
    return this.authorDAO.deleteById(authorId);
  }

  public List queryForCondition(Author author) throws Exception {
    return this.authorDAO.queryForCondition(author);
  }
}