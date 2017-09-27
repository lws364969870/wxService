package com.lws.domain.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.lws.domain.base.BaseHibernateDAO;
import com.lws.domain.entity.Syrole;

@Repository
public class SyroleDAO extends BaseHibernateDAO {

	public void save(Syrole syrole) throws Exception {
		try {
			getCurrentSession().save(syrole);
			getCurrentSession().flush();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List findAll() throws Exception {
		try {
			String queryString = "from Syrole";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}