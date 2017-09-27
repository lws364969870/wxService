package com.lws.domain.dao;

import com.lws.domain.base.BaseHibernateDAO;
import com.lws.domain.entity.Syconfig;
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
public class SyconfigDAO extends BaseHibernateDAO {
	public void save(Syconfig transientarticle) throws Exception {
		getCurrentSession().save(transientarticle);
		getCurrentSession().flush();
	}

	public Syconfig findById(Long id) throws Exception {
		Syconfig syconfig = (Syconfig) getCurrentSession().get("com.lws.domain.entity.Syconfig", id);
		return syconfig;
	}

	public int deleteById(Long id) throws Exception {
		try {
			String sql = "delete from Attached where attachedId = '" + id + "' ";
			SQLQuery sQLQuery = getCurrentSession().createSQLQuery(sql);
			return sQLQuery.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int queryAllCounts(Syconfig syconfig) throws Exception {
		StringBuffer hql = new StringBuffer(" select A.syConfigId from Syconfig A where 1 = 1 ");

		List parameList = new ArrayList();
		if (syconfig.getSyConfigId() != null) {
			hql.append(" and A.syConfigId = ? ");
			parameList.add(syconfig.getSyConfigId());
		}
		if (syconfig.getConfigKey() != null) {
			hql.append(" and A.configKey = ? ");
			parameList.add(syconfig.getConfigKey());
		}
		if (syconfig.getConfigType() != null) {
			hql.append(" and A.configType = ? ");
			parameList.add(syconfig.getConfigType());
		}
		if (syconfig.getIsShow() != null) {
			hql.append(" and A.isShow = ? ");
			parameList.add(syconfig.getIsShow());
		}
		return queryAllCounts(hql.toString(), parameList.toArray());
	}

	public List queryForPageCondition(Syconfig syconfig, int pageNo, int pageSize) throws Exception {
		DetachedCriteria dc = DetachedCriteria.forClass(Syconfig.class);
		if (syconfig.getSyConfigId() != null) {
			dc.add(Restrictions.eq("syConfigId", syconfig.getSyConfigId()));
		}
		if (syconfig.getConfigKey() != null) {
			dc.add(Restrictions.eq("configKey", syconfig.getConfigKey()));
		}
		if (syconfig.getConfigType() != null) {
			dc.add(Restrictions.eq("configType", syconfig.getConfigType()));
		}
		if (syconfig.getIsShow() != null) {
			dc.add(Restrictions.eq("isShow", syconfig.getIsShow()));
		}
		return getHibernateTemplate().findByCriteria(dc, pageNo, pageSize);
	}

	public List queryForCondition(Syconfig syconfig) throws Exception {
		DetachedCriteria dc = DetachedCriteria.forClass(Syconfig.class);
		if (syconfig.getSyConfigId() != null) {
			dc.add(Restrictions.eq("syConfigId", syconfig.getSyConfigId()));
		}
		if (syconfig.getConfigKey() != null) {
			dc.add(Restrictions.eq("configKey", syconfig.getConfigKey()));
		}
		if (syconfig.getConfigType() != null) {
			dc.add(Restrictions.eq("configType", syconfig.getConfigType()));
		}
		if (syconfig.getIsShow() != null) {
			dc.add(Restrictions.eq("isShow", syconfig.getIsShow()));
		}
		return getHibernateTemplate().findByCriteria(dc);
	}

	public String getValueByKey(String key) throws Exception {
		String hql = " select configValue from Syconfig where configKey = ? ";
		Query query = getCurrentSession().createQuery(hql);
		query.setParameter(0, key);
		List list = query.list();
		if (list.size() > 0) {
			return list.get(0).toString();
		}
		return "";
	}

	public List findSyconfigByKey(String key) throws Exception {
		String hql = " from Syconfig where configKey = ? ";
		Query query = getCurrentSession().createQuery(hql);
		query.setParameter(0, key);
		return query.list();
	}

	public int updateByNetWork(String key, String value) throws Exception {
		try {
			String sql = "update Syconfig t set t.configValue = ? where t.configKey = ? ";
			SQLQuery sQLQuery = getCurrentSession().createSQLQuery(sql);
			sQLQuery.setParameter(0, value);
			sQLQuery.setParameter(1, key);
			return sQLQuery.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}