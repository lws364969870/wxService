package com.lws.domain.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.lws.domain.base.BaseHibernateDAO;
import com.lws.domain.entity.Wxmenu;

@Repository
public class WxmenuDAO extends BaseHibernateDAO {
	public void save(Wxmenu transientarticle) throws Exception {
		try {
			getCurrentSession().save(transientarticle);
			getCurrentSession().flush();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List findLevelWxMenu(int level) throws Exception {
		try {
			String queryString = "from Wxmenu where menuLevel = ? ";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, Integer.valueOf(level));
			return queryObject.list();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List queryForCondition(Wxmenu wxmenu) throws Exception {
		DetachedCriteria dc = DetachedCriteria.forClass(Wxmenu.class);
		if (wxmenu.getWxMenuId() != null) {
			dc.add(Restrictions.eq("wxMenuId", wxmenu.getWxMenuId()));
		}
		if (wxmenu.getMenuCode() != null) {
			dc.add(Restrictions.eq("menuCode", wxmenu.getMenuCode()));
		}
		if (wxmenu.getIsCatalog() != null) {
			dc.add(Restrictions.eq("isCatalog", wxmenu.getIsCatalog()));
		}
		try {
			return getHibernateTemplate().findByCriteria(dc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}
}