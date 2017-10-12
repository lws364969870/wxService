package com.lws.domain.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.lws.domain.base.BaseHibernateDAO;
import com.lws.domain.entity.Symenu;

@Repository
public class SymenuDAO extends BaseHibernateDAO {
	public void save(Symenu transientarticle) throws Exception {
		try {
			getCurrentSession().save(transientarticle);
			getCurrentSession().flush();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public int deleteById(Long syMenuId) throws Exception {
		try {
			String sql = "delete from Symenu where syMenuId = '" + syMenuId + "' ";
			SQLQuery sQLQuery = getCurrentSession().createSQLQuery(sql);
			return sQLQuery.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List findAll() throws Exception {
		try {
			String queryString = "from Symenu ";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List findAll(Long sessionUserid) throws Exception {
		try {
			String queryString = "from SymenuV where syUserId = " + sessionUserid;
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List findLevelMenu(int level, Long sessionUserid) throws Exception {
		try {
			String queryString = "from SymenuV where menuLevel = ? and syUserId = ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, Integer.valueOf(level));
			queryObject.setParameter(1, Long.valueOf(sessionUserid));
			return queryObject.list();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public Symenu findById(Long id) throws Exception {
		Symenu symenu = (Symenu) getCurrentSession().get("com.lws.domain.entity.Symenu", id);
		return symenu;
	}
}