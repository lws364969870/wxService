package com.lws.domain.base;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class BaseHibernateDAO extends HibernateDaoSupport {

	@Resource
	private SessionFactory sessionFactory;

	@Autowired
	public void setSessionFactoryOverride(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	public Session getCurrentSession() {
		return this.sessionFactory.getCurrentSession();
	}

	// public synchronized List exectSQL(String sql, Object[] params) {
	// return querySQLMappingPages(sql, params, 2147483647, 0);
	// }

//	public List<?> querySQLMappingPages(String sql, Object[] params, int pageSize, int pageNum) {
//		List temps = (List) getHibernateTemplate().execute(new HibernateCallback(sql, params, pageNum, pageSize) {
//			public Object doInHibernate(Session arg0) throws HibernateException {
//				SQLQuery query = arg0.createSQLQuery(this.val$sql);
//				if ((this.val$params != null) && (this.val$params.length > 0)) {
//					for (int i = 0; i < this.val$params.length; ++i) {
//						query.setParameter(i, this.val$params[i]);
//					}
//				}
//				query = (SQLQuery) query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//				query.setFirstResult(this.val$pageNum * this.val$pageSize);
//				query.setMaxResults(this.val$pageSize);
//				return query.list();
//			}
//		});
//		return temps;
//	}

	public void updateObject(Object object) {
		try {
			getHibernateTemplate().update(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeObject(Object object) {
		try {
			getHibernateTemplate().delete(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	public List queryObjects(String hql, Object[] parameters, int pageNumber, int pageSize) {
//		List list = (List) getHibernateTemplate().execute(new HibernateCallback(hql, parameters, pageSize, pageNumber) {
//			public Object doInHibernate(Session session) {
//				try {
//					Query query = session.createQuery(this.val$hql);
//					if (this.val$parameters != null) {
//						int i = 0;
//						for (int l = this.val$parameters.length; i < l; ++i) {
//							query.setParameter(i, this.val$parameters[i]);
//						}
//					}
//					if (this.val$pageSize > 0) {
//						query.setMaxResults(this.val$pageSize);
//						query.setFirstResult((this.val$pageNumber - 1) * this.val$pageSize);
//					}
//					return query.list();
//				} catch (Exception e) {
//					throw e;
//				}
//			}
//		});
//		return list;
//	}

	public int queryAllCounts(String hql, Object[] parameters) {
		System.out.println(hql);
		List list = new ArrayList();
		try {
			list = getHibernateTemplate().find(hql, parameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list.size();
	}
}