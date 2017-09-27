package com.lws.domain.dao;

import com.lws.domain.base.BaseHibernateDAO;
import com.lws.domain.entity.Subscribe;
import com.lws.domain.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SubscribeDAO extends BaseHibernateDAO
{
  public void save(Subscribe transientarticle)
    throws Exception
  {
    getCurrentSession().save(transientarticle);
    getCurrentSession().flush();
  }

  public int deleteById(Long id) throws Exception {
    try {
      String sql = "delete from Subscribe where subscribeId = ? ";
      SQLQuery sQLQuery = getCurrentSession().createSQLQuery(sql);
      sQLQuery.setParameter(0, id);
      return sQLQuery.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace(); }
    return 0;
  }

  public int deleteAll() throws Exception
  {
    try {
      String sql = "delete from Subscribe ";
      SQLQuery sQLQuery = getCurrentSession().createSQLQuery(sql);
      return sQLQuery.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace(); }
    return 0;
  }

  public List findSubscribeByOpenid(String openid) throws Exception
  {
    String hql = " from Subscribe where openid = ? ";
    Query query = getCurrentSession().createQuery(hql);
    query.setParameter(0, openid);
    return query.list();
  }

  public Subscribe findById(Long id) throws Exception {
    Subscribe subscribe = (Subscribe)getCurrentSession().get("com.lws.domain.entity.Subscribe", id);
    return subscribe;
  }

  public int queryAllCounts(Subscribe subscribe) throws Exception
  {
    StringBuffer hql = new StringBuffer(" select A.subscribeId from Subscribe A where 1 = 1 ");

    List parameList = new ArrayList();
    if (subscribe.getSubscribeId() != null) {
      hql.append(" and A.subscribeId = ? ");
      parameList.add(subscribe.getSubscribeId());
    }
    if (!(StringUtils.isEmpty(subscribe.getNickname()))) {
      hql.append(" and A.nickname like ? ");
      parameList.add("%" + subscribe.getNickname() + "%");
    }
    if (!(StringUtils.isEmpty(subscribe.getRemark()))) {
      hql.append(" and A.remark like ? ");
      parameList.add("%" + subscribe.getRemark() + "%");
    }
    if (!(StringUtils.isEmpty(subscribe.getOpenid()))) {
      hql.append(" and A.openid = ? ");
      parameList.add(subscribe.getOpenid());
    }
    if (!(StringUtils.isEmpty(subscribe.getIsShow()))) {
      hql.append(" and A.isShow = ? ");
      parameList.add(subscribe.getIsShow());
    }

    return queryAllCounts(hql.toString(), parameList.toArray());
  }

  public List queryForPageCondition(Subscribe subscribe, int pageNo, int pageSize) throws Exception {
    DetachedCriteria dc = DetachedCriteria.forClass(Subscribe.class);
    if (subscribe.getSubscribeId() != null) {
      dc.add(Restrictions.eq("subscribeId", subscribe.getSubscribeId()));
    }
    if (!(StringUtils.isEmpty(subscribe.getNickname()))) {
      dc.add(Restrictions.like("nickname", subscribe.getNickname(), MatchMode.ANYWHERE));
    }
    if (!(StringUtils.isEmpty(subscribe.getRemark()))) {
      dc.add(Restrictions.like("remark", subscribe.getRemark(), MatchMode.ANYWHERE));
    }
    if (!(StringUtils.isEmpty(subscribe.getOpenid()))) {
      dc.add(Restrictions.eq("openid", subscribe.getOpenid()));
    }
    if (!(StringUtils.isEmpty(subscribe.getIsShow()))) {
      dc.add(Restrictions.eq("openid", subscribe.getIsShow()));
    }

    return getHibernateTemplate().findByCriteria(dc, pageNo, pageSize);
  }
}