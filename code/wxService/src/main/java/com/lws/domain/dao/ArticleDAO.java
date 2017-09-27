package com.lws.domain.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.lws.domain.base.BaseHibernateDAO;
import com.lws.domain.entity.Article;
import com.lws.domain.model.ArticleQueryModel;

@Repository
public class ArticleDAO extends BaseHibernateDAO {
	
	public void save(Article transientarticle) throws Exception {
		try {
			getCurrentSession().save(transientarticle);
			getCurrentSession().flush();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public int deleteById(Long articleId) throws Exception {
		try {
			String sql = "delete from Article where articleId = '" + articleId + "' ";
			SQLQuery sQLQuery = getCurrentSession().createSQLQuery(sql);

			return sQLQuery.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List findAll() throws Exception {
		try {
			String queryString = "from Article";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public Article findById(Long id) throws Exception {
		try {
			Article article = (Article) getCurrentSession().get("com.lws.domain.entity.Article", id);
			return article;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public int findMaxId() throws Exception {
		String hql = " select max(articleId) from Article ";
		Query query = getCurrentSession().createQuery(hql);
		List list = query.list();
		if (list.size() > 0) {
			return (Integer.parseInt(list.get(0).toString()) + 1);
		}
		return -99;
	}

	public List findByHqlExample(ArticleQueryModel model, int pageNo, int pageSize) throws Exception {
		StringBuffer hql = new StringBuffer(" from Article A where 1 = 1 ");
		List parameList = new ArrayList();
		if (model.getTitle() != null) {
			hql.append(" and  A.title = ? ");
			parameList.add(model.getTitle());
		}
		if (model.getCreateDateFrom() != null) {
			hql.append(" and  A.createDate >= ? ");
			parameList.add(model.getCreateDateFrom());
		}
		if (model.getCreateDateTo() != null) {
			hql.append(" and  A.createDate <= ? ");
			parameList.add(model.getCreateDateTo());
		}

		List list = getHibernateTemplate().find(hql.toString(), parameList.toArray());
		return list;
	}

	public List queryForPageCondition(ArticleQueryModel model, int pageNo, int pageSize) throws Exception {
		DetachedCriteria dc = DetachedCriteria.forClass(Article.class);
		if (model.getTitle() != null) {
			dc.add(Restrictions.eq("title", model.getTitle()));
		}
		if (model.getCreateDateFrom() != null) {
			dc.add(Restrictions.le("createDate", model.getCreateDateFrom()));
		}
		if (model.getCreateDateTo() != null) {
			dc.add(Restrictions.ge("createDate", model.getCreateDateTo()));
		}
		if (model.getStatus() != null) {
			dc.add(Restrictions.eq("status", model.getStatus()));
		}
		if (model.getArticleType() != null) {
			dc.add(Restrictions.eq("articleType", model.getArticleType()));
		}
		if ((model.getSyroleType() != null) && (!("RT001".equals(model.getSyroleType()))) && (!("RT002".equals(model.getSyroleType())))) {
			"RT003".equals(model.getSyroleType());
		}

		dc.addOrder(Order.desc("createDate"));
		try {
			return getHibernateTemplate().findByCriteria(dc, pageNo, pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}

	public int queryAllCounts(ArticleQueryModel model) throws Exception {
		StringBuffer hql = new StringBuffer(" select A.articleId from Article A where 1 = 1 ");

		List parameList = new ArrayList();
		if (model.getTitle() != null) {
			hql.append(" and A.title = ? ");
			parameList.add(model.getTitle());
		}
		if (model.getCreateDateFrom() != null) {
			hql.append(" and A.createDate >= ? ");
			parameList.add(model.getCreateDateFrom());
		}
		if (model.getCreateDateTo() != null) {
			hql.append(" and A.createDate <= ? ");
			parameList.add(model.getCreateDateTo());
		}
		if (model.getStatus() != null) {
			hql.append(" and A.status = ? ");
			parameList.add(model.getStatus());
		}
		if (model.getArticleType() != null) {
			hql.append(" and A.articleType = ? ");
			parameList.add(model.getArticleType());
		}

		return queryAllCounts(hql.toString(), parameList.toArray());
	}

	public Article merge(Article article) throws Exception {
		try {
			Article result = (Article) getCurrentSession().merge(article);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}
}