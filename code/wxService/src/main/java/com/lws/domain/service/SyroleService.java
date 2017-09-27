package com.lws.domain.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.lws.domain.dao.SyroleDAO;
import com.lws.domain.entity.Syrole;

@Repository
public class SyroleService {

	@Resource
	SyroleDAO syroleDAO;

	public List findAll() {
		List entitylist = new ArrayList();
		try {
			entitylist = this.syroleDAO.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entitylist;
	}

	public void save(Syrole syrole) throws Exception {
		this.syroleDAO.save(syrole);
	}

}