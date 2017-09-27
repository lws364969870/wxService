package com.lws.domain.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lws.domain.dao.SylogDAO;
import com.lws.domain.entity.Sylog;
import com.lws.domain.model.Page;
import com.lws.domain.model.SylogQueryModel;
import com.lws.domain.utils.CommonParameter;

@Service
public class SylogService {

	@Resource
	SylogDAO sylogDAO;

	public void save(Sylog sylog) throws Exception {
		this.sylogDAO.save(sylog);
	}

	/**
	 * 
	 * @param tableId 主键
	 * @param tableName 表名
	 * @param syuserId 操作人ID
	 * @param logType 日志类型
	 * @param countent 内容
	 * @throws Exception
	 */
	public void save(Long tableId, String tableName, Long syuserId, String logType, String countent) throws Exception {
		Sylog sylog = new Sylog();
		sylog.setTableid(tableId);
		sylog.setTablename(tableName);
		sylog.setSyuserid(syuserId);
		sylog.setLogtype(logType);
		sylog.setLogdate(new Timestamp(new Date().getTime()));
		sylog.setContent(countent);
		this.sylogDAO.save(sylog);
	}

	public Page queryForPageCondition(SylogQueryModel model, int pageNo, int pageSize) throws Exception {
		Page page = new Page();

		int allRow = this.sylogDAO.queryAllCounts(model);
		int offset = page.countOffset(pageNo, pageSize);
		List list = this.sylogDAO.queryForPageCondition(model, offset, pageSize);

		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setTotalRecords(allRow);
		page.setList(list);
		return page;
	}

	public List querySylogReportForYear(int year) {
		return sylogDAO.querySylogReportForYear(year);
	}

}