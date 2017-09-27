package com.lws.domain.dao;

import com.lws.domain.base.BaseHibernateDAO;
import com.lws.domain.entity.Attached;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AttachedDAO extends BaseHibernateDAO
{
  public void save(Attached transientarticle)
    throws Exception
  {
    getCurrentSession().save(transientarticle);
    getCurrentSession().flush();
  }

  public Attached findById(Long id) throws Exception {
    Attached syconfig = (Attached)getCurrentSession().get("com.lws.domain.entity.Attached", id);
    return syconfig;
  }

  public List findByArticleId(Long articleId) throws Exception {
    try {
      String queryString = "from Attached where articleId = ? ";
      Query queryObject = getCurrentSession().createQuery(queryString);
      queryObject.setParameter(0, articleId);
      return queryObject.list();
    } catch (RuntimeException re) {
      throw re;
    }
  }

  public int deleteById(Long id) throws Exception {
    try {
      String sql = "delete from Attached where attachedId = '" + id + "' ";
      SQLQuery sQLQuery = getCurrentSession().createSQLQuery(sql);
      return sQLQuery.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace(); }
    return 0;
  }

  public List queryNullArticleIdList() throws Exception
  {
    try {
      String queryString = "from Attached where articleId is null ";
      Query queryObject = getCurrentSession().createQuery(queryString);
      return queryObject.list();
    } catch (RuntimeException re) {
      throw re;
    }
  }

  public List queryForCondition(Attached attached) throws Exception {
    DetachedCriteria dc = DetachedCriteria.forClass(Attached.class);
    if (attached.getArticleId() != null) {
      dc.add(Restrictions.eq("articleId", attached.getArticleId()));
    }
    return getHibernateTemplate().findByCriteria(dc);
  }

  public int updateArticleIdByNetwordURL(Long articleId, String networdURL) {
    try {
      String sql = "update Attached t set t.articleId = ? where t.articleId is null and t.networdURL = ? ";
      SQLQuery sQLQuery = getCurrentSession().createSQLQuery(sql);
      sQLQuery.setParameter(0, articleId);
      sQLQuery.setParameter(1, networdURL);
      return sQLQuery.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace(); }
    return 0;
  }

  public int updateArticleIdByWechatURL(Long articleId, String wechatURL)
  {
    try {
      String sql = "update Attached t set t.articleId = ? where t.articleId is null and t.wechatURL = ? ";
      SQLQuery sQLQuery = getCurrentSession().createSQLQuery(sql);
      sQLQuery.setParameter(0, articleId);
      sQLQuery.setParameter(1, wechatURL);
      return sQLQuery.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace(); }
    return 0;
  }
}