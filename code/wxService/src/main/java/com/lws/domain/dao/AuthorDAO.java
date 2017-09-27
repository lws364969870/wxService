package com.lws.domain.dao;

import com.lws.domain.base.BaseHibernateDAO;
import com.lws.domain.entity.Author;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AuthorDAO extends BaseHibernateDAO {
	
	public void save(Author transientarticle) throws Exception {
		try {
			getCurrentSession().save(transientarticle);
			getCurrentSession().flush();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public int deleteById(Long authorId) throws Exception {
		try {
			String sql = "delete from Author where authorId = '" + authorId + "' ";
			SQLQuery sQLQuery = getCurrentSession().createSQLQuery(sql);
			return sQLQuery.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List findAll() throws Exception {
		try {
			String queryString = "from Author";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public Author findById(Long id) throws Exception {
		try {
			Author author = (Author) getCurrentSession().get("com.lws.domain.entity.Author", id);
			return author;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List queryForCondition(Author author) throws Exception {
		DetachedCriteria dc = DetachedCriteria.forClass(Author.class);
		if (author.getIsHide() != null) {
			dc.add(Restrictions.eq("isHide", author.getIsHide()));
		}
		try {
			return getHibernateTemplate().findByCriteria(dc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}
}