package com.lws.domain.service;

import com.lws.domain.dao.SymenuDAO;
import com.lws.domain.entity.Symenu;
import com.lws.domain.entity.SymenuV;
import com.lws.domain.model.SymenuModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

@Repository
public class SymenuService {

	@Resource
	SymenuDAO symenuDAO;

	public List findAll(Long sessionUserid) {
		List entitylist = new ArrayList();
		try {
			entitylist = this.symenuDAO.findAll(sessionUserid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entitylist;
	}

	public List findLevelMenu(Long sessionUserid) throws Exception {
		List datalist = new ArrayList();

		List level1 = this.symenuDAO.findLevelMenu(1, sessionUserid);

		List level2 = this.symenuDAO.findLevelMenu(2, sessionUserid);
		for (Iterator localIterator1 = level1.iterator(); localIterator1.hasNext();) {
			Object o1 = localIterator1.next();
			Map map = new HashMap();
			SymenuModel model1 = new SymenuModel();
			SymenuV symenuV1 = (SymenuV) o1;
			model1.copyBean(symenuV1);

			String menuCode1 = model1.getMenuCode();

			for (Iterator localIterator2 = level2.iterator(); localIterator2.hasNext();) {
				Object o2 = localIterator2.next();
				SymenuModel model2 = new SymenuModel();
				SymenuV symenuV2 = (SymenuV) o2;
				model2.copyBean(symenuV2);

				String menuCode2 = model2.getMenuCode();
				String parentsCode2 = model2.getParentsCode();
				if (menuCode1.equals(parentsCode2)) {
					map.put(menuCode2, model2);
				}
			}
			if ((map != null) && (map.size() > 0)) {
				model1.setChild(map);
			}
			datalist.add(model1);
		}

		return datalist;
	}

	public Symenu findById(Long id) throws Exception {
		return this.symenuDAO.findById(id);
	}

	public void save(Symenu symenu) throws Exception {
		this.symenuDAO.save(symenu);
	}
}