package com.lws.web.controller;

import java.sql.Timestamp;
import java.util.Calendar;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lws.domain.model.DataResult;
import com.lws.domain.model.SessionData;
import com.lws.domain.model.SylogQueryModel;
import com.lws.domain.service.SylogService;
import com.lws.domain.utils.StringUtils;
import com.lws.domain.utils.pwd.AES;
import com.lws.domain.utils.request.RequestUtil;

@Controller
public class SylogController {

	@Resource
	private SylogService sylogService;

	@RequestMapping({ "/querySylogPanel" })
	public String doQuerySylogPanel(HttpServletRequest request, HttpServletResponse response) {
		return "/querySylogPanel";
	}

	@RequestMapping({ "/querySylogReportPanel" })
	public String doQuerySylogReportPanel(HttpServletRequest request, HttpServletResponse response) {
		return "/querySylogReportPanel";
	}

	@ResponseBody
	@RequestMapping({ "/querySylogReportForYear" })
	public String doQuerySylogReportForYear(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);
		try {
			String jsonData = RequestUtil.decryptRequestJson(request, sessionData);
			JSONObject json = JSONObject.fromObject(jsonData);

			String yearStr = json.get("year") == null ? "" : json.getString("year");
		
			Calendar a=Calendar.getInstance();
			int year = a.get(Calendar.YEAR);
			if(!yearStr.isEmpty()){
				year = Integer.parseInt(yearStr);
			}
			
			dataResult.setData(this.sylogService.querySylogReportForYear(year));
			returnJson = JSONObject.fromObject(dataResult);
			String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
			return StringUtils.replaceBlank(s);
		} catch (Exception e) {
			e.printStackTrace();
			dataResult.setFlag("3");
			dataResult.setMessage(e.getMessage());
			returnJson = JSONObject.fromObject(dataResult);
			String s = "";
			try {
				s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return StringUtils.replaceBlank(s);
		}
	}

	@ResponseBody
	@RequestMapping({ "/querySylog" })
	public String doQuerySylog(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);
		try {
			String jsonData = RequestUtil.decryptRequestJson(request, sessionData);
			JSONObject json = JSONObject.fromObject(jsonData);

			String pageNoStr = json.get("pageNo") == null ? "" : json.getString("pageNo");
			String pageSizeStr = json.get("pageSize") == null ? "" : json.getString("pageSize");
			int pageNo = 1;
			int pageSize = 10;
			if (!(StringUtils.isEmpty(pageNoStr))) {
				pageNo = Integer.valueOf(pageNoStr).intValue();
			}
			if (!(StringUtils.isEmpty(pageSizeStr))) {
				pageSize = Integer.valueOf(pageSizeStr).intValue();
			}

			String tablename = json.get("tablename") == null ? "" : json.getString("tablename");// 表名
			String loginName = json.get("loginName") == null ? "" : json.getString("loginName");// 登录名
			String userName = json.get("userName") == null ? "" : json.getString("userName");// 用户名
			String content = json.get("content") == null ? "" : json.getString("content");// 内容
			String logdateFrom = json.get("logdateFrom") == null ? "" : json.getString("logdateFrom");// 操作日期从
			String logdateTo = json.get("logdateTo") == null ? "" : json.getString("logdateTo");// 操作日期至
			String logtype = json.get("logtype") == null ? "" : json.getString("logtype");// 操作类型

			SylogQueryModel model = new SylogQueryModel();
			if (!StringUtils.isEmpty(tablename)) {
				model.setTablename(tablename);
			}
			if (!StringUtils.isEmpty(loginName)) {
				model.setLoginName(loginName);
			}
			if (!StringUtils.isEmpty(userName)) {
				model.setUserName(userName);
			}
			if (!StringUtils.isEmpty(content)) {
				model.setContent(content);
			}
			if (!StringUtils.isEmpty(logdateFrom) && !"NaN".equals(logdateFrom)) {
				model.setLogdateFrom(new Timestamp(Long.parseLong(logdateFrom)));
			}
			if (!StringUtils.isEmpty(logdateTo) && !"NaN".equals(logdateTo)) {
				model.setLogdateTo(new Timestamp(Long.parseLong(logdateTo)));
			}
			if (!StringUtils.isEmpty(logtype)) {
				model.setLogtype(logtype);
			}

			dataResult.setData(this.sylogService.queryForPageCondition(model, pageNo, pageSize));
			returnJson = JSONObject.fromObject(dataResult);
			String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
			return StringUtils.replaceBlank(s);
		} catch (Exception e) {
			e.printStackTrace();
			dataResult.setFlag("3");
			dataResult.setMessage(e.getMessage());
			returnJson = JSONObject.fromObject(dataResult);
			String s = "";
			try {
				s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return StringUtils.replaceBlank(s);
		}
	}
}