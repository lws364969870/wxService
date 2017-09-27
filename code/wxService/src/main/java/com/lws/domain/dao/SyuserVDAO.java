package com.lws.domain.dao;

import com.lws.domain.base.BaseHibernateDAO;
import com.lws.domain.entity.SyuserV;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.springframework.stereotype.Repository;

@Repository
public class SyuserVDAO extends BaseHibernateDAO
{
  public SyuserV findById(Integer id)
  {
    try
    {
      SyuserV instance = (SyuserV)getCurrentSession().get("com.lws.domain.entity.SyuserV", id);
      return instance;
    } catch (RuntimeException re) {
      throw re;
    }
  }

  public List findByExample(SyuserV instance) {
    try {
      List results = getCurrentSession().createCriteria("com.lws.domain.entity.SyuserV").add(Example.create(instance)).list();
      return results;
    } catch (RuntimeException re) {
      throw re;
    }
  }

  public List findAll() {
    try {
      String queryString = "from SyuserV";
      Query queryObject = getCurrentSession().createQuery(queryString);
      return queryObject.list();
    } catch (RuntimeException re) {
      throw re;
    }
  }
}