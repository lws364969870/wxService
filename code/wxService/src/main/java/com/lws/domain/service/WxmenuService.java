package com.lws.domain.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.lws.domain.dao.WxmenuDAO;
import com.lws.domain.entity.Wxmenu;
import com.lws.domain.model.WxmenuModel;

@Repository
public class WxmenuService {

	@Resource
	WxmenuDAO wxmenuDAO;

	public List findAll() {
		List entitylist = new ArrayList();
		try {
			entitylist = this.wxmenuDAO.queryForCondition(new Wxmenu());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entitylist;
	}

	public List findLevelWxMenu() throws Exception {
		List datalist = new ArrayList();

		List level1 = this.wxmenuDAO.findLevelWxMenu(1);

		List level2 = this.wxmenuDAO.findLevelWxMenu(2);
		for (Iterator localIterator1 = level1.iterator(); localIterator1.hasNext();) {
			Object o1 = localIterator1.next();
			Map map = new HashMap();
			WxmenuModel model1 = new WxmenuModel();
			Wxmenu wxmenu1 = (Wxmenu) o1;
			model1.copyBean(wxmenu1);

			String menuCode1 = model1.getMenuCode();

			for (Iterator localIterator2 = level2.iterator(); localIterator2.hasNext();) {
				Object o2 = localIterator2.next();
				WxmenuModel model2 = new WxmenuModel();
				Wxmenu wxmenu2 = (Wxmenu) o2;
				model2.copyBean(wxmenu2);

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

	public Wxmenu findById(Long id) throws Exception {
		Wxmenu wxmenu = new Wxmenu();
		wxmenu.setWxMenuId(id);
		List list = this.wxmenuDAO.queryForCondition(new Wxmenu());
		if (list.size() == 0) {
			return new Wxmenu();
		}
		return ((Wxmenu) list.get(0));
	}

	public List queryForCondition(Wxmenu wxmenu) throws Exception {
		return this.wxmenuDAO.queryForCondition(wxmenu);
	}

	public void save(Wxmenu wxmenu) throws Exception {
		this.wxmenuDAO.save(wxmenu);
	}
}