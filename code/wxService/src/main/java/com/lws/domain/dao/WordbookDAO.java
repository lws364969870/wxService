package com.lws.domain.dao;

import com.lws.domain.base.BaseHibernateDAO;
import com.lws.domain.entity.Wordbook;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WordbookDAO extends BaseHibernateDAO
{
  public List queryForCondition(Wordbook wordbook)
    throws Exception
  {
    DetachedCriteria dc = DetachedCriteria.forClass(Wordbook.class);
    if (wordbook.getWordbookId() != null) {
      dc.add(Restrictions.eq("wordbookId", wordbook.getWordbookId()));
    }
    if (wordbook.getWordbookCode() != null) {
      dc.add(Restrictions.eq("wordbookCode", wordbook.getWordbookCode()));
    }
    if (wordbook.getParentsBookCode() != null) {
      dc.add(Restrictions.eq("parentsBookCode", wordbook.getParentsBookCode()));
    }
    return getHibernateTemplate().findByCriteria(dc);
  }
}