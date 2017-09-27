package com.lws.domain.dao;

import com.lws.domain.base.BaseHibernateDAO;
import com.lws.domain.entity.Syuser;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Session.LockRequest;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SyuserDAO extends BaseHibernateDAO {
	public void save(Syuser transientInstance) {
		try {
			getCurrentSession().save(transientInstance);
			getCurrentSession().flush();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List findByExample(Syuser instance) {
		try {
			List results = getCurrentSession().createCriteria("com.lws.domain.entity.Syuser").add(Example.create(instance)).list();
			return results;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List findAll() {
		try {
			String queryString = "from Syuser";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public Syuser merge(Syuser detachedInstance) {
		try {
			Syuser result = (Syuser) getCurrentSession().merge(detachedInstance);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void attachDirty(Syuser instance) {
		try {
			getCurrentSession().saveOrUpdate(instance);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void attachClean(Syuser instance) {
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(instance);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List queryForPageCondition(Syuser syuser, int pageNo, int pageSize) throws Exception {
		DetachedCriteria dc = DetachedCriteria.forClass(Syuser.class);
		if (syuser.getSyUserId() != null) {
			dc.add(Restrictions.eq("syUserId", syuser.getSyUserId()));
		}
		if (syuser.getLoginName() != null) {
			dc.add(Restrictions.eq("loginName", syuser.getLoginName()));
		}
		if (syuser.getLoginName() != null) {
			dc.add(Restrictions.eq("userName", syuser.getUserName()));
		}
		dc.addOrder(Order.asc("sort"));
		try {
			return getHibernateTemplate().findByCriteria(dc, pageNo, pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}

	public int queryAllCounts(Syuser syuser) throws Exception {
		StringBuffer hql = new StringBuffer(" select A.syUserId from Syuser A where 1 = 1 ");

		List parameList = new ArrayList();
		if (syuser.getSyUserId() != null) {
			hql.append(" and A.syUserId = ? ");
			parameList.add(syuser.getSyUserId());
		}
		if (syuser.getLoginName() != null) {
			hql.append(" and A.loginName = ? ");
			parameList.add(syuser.getLoginName());
		}
		if (syuser.getLoginName() != null) {
			hql.append(" and A.userName = ? ");
			parameList.add(syuser.getUserName());
		}
		return queryAllCounts(hql.toString(), parameList.toArray());
	}

	public int deleteById(Long id) throws Exception {
		try {
			String sql = "delete from Syuser where syUserId = '" + id + "' ";
			SQLQuery sQLQuery = getCurrentSession().createSQLQuery(sql);
			return sQLQuery.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public Syuser findById(Long id) throws Exception {
		try {
			Syuser syuser = (Syuser) getCurrentSession().get("com.lws.domain.entity.Syuser", id);
			return syuser;
		} catch (RuntimeException re) {
			throw re;
		}
	}
}