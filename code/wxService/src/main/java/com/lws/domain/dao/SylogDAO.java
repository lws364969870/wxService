package com.lws.domain.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.lws.domain.base.BaseHibernateDAO;
import com.lws.domain.entity.Sylog;
import com.lws.domain.entity.SylogV;
import com.lws.domain.model.SylogQueryModel;
import com.lws.domain.utils.StringUtils;

@Repository
public class SylogDAO extends BaseHibernateDAO {

	public void save(Sylog sylog) throws Exception {
		try {
			getCurrentSession().save(sylog);
			getCurrentSession().flush();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public int queryAllCounts(SylogQueryModel model) throws Exception {
		StringBuffer hql = new StringBuffer(" select A.sylogid from SylogV A where 1 = 1 ");

		List parameList = new ArrayList();
		if (!StringUtils.isEmpty(model.getTablename())) {
			hql.append(" and A.tablename = ? ");
			parameList.add(model.getTablename());
		}
		if (!StringUtils.isEmpty(model.getUserName())) {
			hql.append(" and A.userName = ? ");
			parameList.add(model.getUserName());
		}
		if (!StringUtils.isEmpty(model.getLoginName())) {
			hql.append(" and A.loginName = ? ");
			parameList.add(model.getLoginName());
		}

		if (!StringUtils.isEmpty(model.getContent())) {
			hql.append(" and A.content like ? ");
			parameList.add("%" + model.getContent() + "%");
		}
		if (!StringUtils.isEmpty(model.getLogtype())) {
			hql.append(" and A.logtype = ? ");
			parameList.add(model.getLogtype());
		}
		// 日期从>=le
		if (null != model.getLogdateFrom()) {
			hql.append(" and A.logdate >= ? ");
			parameList.add(model.getLogdateFrom());
		}
		// 日期至<=ge
		if (null != model.getLogdateTo()) {
		}
		return queryAllCounts(hql.toString(), parameList.toArray());
	}

	public List queryForPageCondition(SylogQueryModel model, int pageNo, int pageSize) throws Exception {
		DetachedCriteria dc = DetachedCriteria.forClass(SylogV.class);
		if (!StringUtils.isEmpty(model.getTablename())) {
			dc.add(Restrictions.eq("tablename", model.getTablename()));
		}

		if (!StringUtils.isEmpty(model.getUserName())) {
			dc.add(Restrictions.eq("userName", model.getUserName()));
		}
		if (!StringUtils.isEmpty(model.getLoginName())) {
			dc.add(Restrictions.eq("loginName", model.getLoginName()));
		}

		if (!StringUtils.isEmpty(model.getContent())) {
			dc.add(Restrictions.like("content", model.getContent(), MatchMode.ANYWHERE));
		}
		// 日期从>=ge
		if (null != model.getLogdateFrom()) {
			System.out.println(model.getLogdateFrom());
			dc.add(Restrictions.ge("logdate", model.getLogdateFrom()));
		}
		// 日期至<=le
		if (null != model.getLogdateTo()) {
			System.out.println(model.getLogdateTo());
			dc.add(Restrictions.le("logdate", model.getLogdateTo()));
		}

		if (!StringUtils.isEmpty(model.getLogtype())) {
			dc.add(Restrictions.eq("logtype", model.getLogtype()));
		}
		dc.addOrder(Order.desc("logdate"));
		return getHibernateTemplate().findByCriteria(dc, pageNo, pageSize);
	}

	/**
	 * 查询日志报表（年）
	 * @param year
	 * @return
	 */
	public List querySylogReportForYear(int year) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT t1.MONTH, t1.wordbookname LOGTYPENAME, COUNT(t2.logtype) COUNT            ");
		sql.append("   FROM (SELECT MONTH, wordbookname, wordbookcode                                 ");
		sql.append("           FROM sylogmonth, wordbook                                              ");
		sql.append("          WHERE parentsbookcode = 'H004'                                          ");
		sql.append("          ORDER BY MONTH, wordbookcode) t1                                        ");
		sql.append(" LEFT JOIN (SELECT logtype, logdate FROM sylog WHERE YEAR(logdate) = '"+year+"') t2 ");
		sql.append("     ON t1.wordbookcode = t2.logtype                                              ");
		sql.append("    AND t1.month = MONTH(t2.logdate)                                              ");
		sql.append("  GROUP BY t1. MONTH, t1.wordbookname                                             ");
		// 获得SQLQuery对象
		SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		// 设定结果结果集中的每个对象为Map类型
		query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}
}