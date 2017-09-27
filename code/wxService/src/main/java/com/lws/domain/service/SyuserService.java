package com.lws.domain.service;

import com.lws.domain.dao.SyuserDAO;
import com.lws.domain.dao.SyuserVDAO;
import com.lws.domain.entity.Syuser;
import com.lws.domain.entity.SyuserV;
import com.lws.domain.model.Page;
import com.lws.domain.utils.pwd.MD5;

import java.io.PrintStream;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service
public class SyuserService {

	@Resource
	SyuserDAO syuserDAO;

	@Resource
	SyuserVDAO syuserVDAO;

	/**
	 * 检查登录名是否存在（存在返回true，不存在返回false）
	 * @param loginName
	 * @return
	 */
	public boolean checkExistsLoginName(String loginName) {
		Syuser syuser = new Syuser();
		syuser.setLoginName(loginName);
		List list = this.syuserDAO.findByExample(syuser);
		return list.size() > 0;
	}
	
	public List getAccount(String loginName){
		SyuserV syuserV = new SyuserV();
		syuserV.setLoginName(loginName);
		List list = this.syuserVDAO.findByExample(syuserV);
		return list;
	}

	public List getAccount(String loginName, String passWord) {
		SyuserV syuserV = new SyuserV();
		syuserV.setLoginName(loginName);
		String md5Pwd = MD5.MD5_upper32(passWord);
		syuserV.setPassWord(md5Pwd);
		List list = this.syuserVDAO.findByExample(syuserV);
		return list;
	}

	public void insertData(Syuser syuser) {
		this.syuserDAO.save(syuser);
	}

	public List<Syuser> querySyuserByPage(int offset, int length) {
		List entitylist = null;
		return entitylist;
	}

	public Page queryForPageCondition(Syuser syuser, int pageNo, int pageSize) throws Exception {
		Page page = new Page();

		int allRow = this.syuserDAO.queryAllCounts(syuser);

		int offset = page.countOffset(pageNo, pageSize);

		List list = this.syuserDAO.queryForPageCondition(syuser, offset, pageSize);

		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setTotalRecords(allRow);
		page.setList(list);
		return page;
	}

	public Syuser findById(Long id) throws Exception {
		return this.syuserDAO.findById(id);
	}
	
	public List findByExample(Syuser syuser){
		return this.syuserDAO.findByExample(syuser);
	}

	public int deleteById(Long id) throws Exception {
		return this.syuserDAO.deleteById(id);
	}

	public void save(Syuser syuser) throws Exception {
		this.syuserDAO.save(syuser);
	}
}