package com.lws.web.controller;

import java.sql.Timestamp;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lws.domain.entity.Author;
import com.lws.domain.entity.Sylog;
import com.lws.domain.model.DataResult;
import com.lws.domain.model.SessionData;
import com.lws.domain.service.AuthorService;
import com.lws.domain.service.SylogService;
import com.lws.domain.utils.CommonParameter;
import com.lws.domain.utils.StringUtils;
import com.lws.domain.utils.pwd.AES;
import com.lws.domain.utils.request.RequestUtil;

@Controller
public class AuthorController {

	@Resource
	AuthorService authorService;
	@Resource
	SylogService sylogService;

	@RequestMapping({ "/queryAuthorPanel" })
	public String doQueryAuthorPanel(HttpServletRequest request, HttpServletResponse response) {
		return "/queryAuthorPanel";
	}

	@RequestMapping({ "/editAuthorPanel" })
	public String doEditAuthorPanel(HttpServletRequest request, HttpServletResponse response) {
		return "/editAuthorPanel";
	}

	@ResponseBody
	@RequestMapping({ "/findAllAuthor" })
	public String doFindAllAuthor(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);
		try {
			dataResult.setData(this.authorService.findAll());
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
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return StringUtils.replaceBlank(s);
		}
	}

	@ResponseBody
	@RequestMapping({ "/queryAuthor" })
	public String doQueryAuthor(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);

		try {
			String jsonData = RequestUtil.decryptRequestJson(request, sessionData);
			JSONObject json = JSONObject.fromObject(jsonData);

			String authorId = json.get("authorId") == null ? "" : json.getString("authorId");

			String isHide = json.get("isHide") == null ? "" : json.getString("isHide");

			Author author = new Author();
			if (!(StringUtils.isEmpty(authorId))) {
				author.setAuthorId(Long.valueOf(Long.parseLong(authorId)));
			}
			if (!(StringUtils.isEmpty(isHide))) {
				author.setIsHide(isHide);
			}
			dataResult.setData(this.authorService.queryForCondition(author));
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
	@RequestMapping({ "/findAuthorById" })
	public String doFindAuthorById(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);

		try {
			String jsonData = RequestUtil.decryptRequestJson(request, sessionData);
			JSONObject json = JSONObject.fromObject(jsonData);

			String authorId = json.get("authorId") == null ? "" : json.getString("authorId");
			if (StringUtils.isEmpty(authorId)) {
				dataResult.setFlag("3");
				dataResult.setMessage("主键不能为空");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}
			Author author = this.authorService.findById(Long.valueOf(Long.parseLong(authorId)));
			dataResult.setData(author);
			returnJson = JSONObject.fromObject(dataResult);
			String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
			return StringUtils.replaceBlank(s);
		} catch (Exception e) {
			e.printStackTrace();
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
	@RequestMapping({ "/saveAuthor" })
	public String doSaveAuthor(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);

		try {
			String jsonData = RequestUtil.decryptRequestJson(request, sessionData);
			JSONObject json = JSONObject.fromObject(jsonData);
			String sessionIp = sessionData.getIp();
			String sessionUserid = sessionData.getUserId();
			String sessionUserName = sessionData.getUserName();

			String authorId = json.get("authorId") == null ? "" : json.getString("authorId");
			String authorName = json.get("authorName") == null ? "" : json.getString("authorName");
			String logType = CommonParameter.LOGFLAG_H004001;

			if (StringUtils.isEmpty(authorName)) {
				dataResult.setFlag("3");
				dataResult.setMessage("作者名称不能为空");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}
			Author author = new Author();
			if (!(StringUtils.isEmpty(authorId))) {
				logType = CommonParameter.LOGFLAG_H004002;
				author = this.authorService.findById(Long.valueOf(Long.parseLong(authorId)));
			}
			author.setAuthorName(authorName);
			this.authorService.save(author);

			/**
			 * 保存日志
			 */
			Sylog sylog = new Sylog();
			sylog.setTableid(author.getAuthorId());
			sylog.setTablename("author");
			sylog.setSyuserid(Long.parseLong(sessionUserid));
			sylog.setLogtype(logType);
			sylog.setLogdate(new Timestamp(new Date().getTime()));
			if (logType.equals(CommonParameter.LOGFLAG_H004001)) {
				sylog.setContent(sessionUserName + "：" + sessionIp + "增加了作者《" + author.getAuthorName() + "》");
			} else if (logType.equals(CommonParameter.LOGFLAG_H004002)) {
				sylog.setContent(sessionUserName + "：" + sessionIp + "修改了作者《" + author.getAuthorName() + "》");
			}
			sylogService.save(sylog);

			dataResult.setData(author);
			dataResult.setMessage("保存成功");
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
	@RequestMapping({ "/changeAuthorIsHide" })
	public String doChangeAuthorIsHide(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);

		try {
			String jsonData = RequestUtil.decryptRequestJson(request, sessionData);
			JSONObject json = JSONObject.fromObject(jsonData);

			String authorId = json.get("authorId") == null ? "" : json.getString("authorId");
			String isHide = json.get("isHide") == null ? "" : json.getString("isHide");

			if ((StringUtils.isEmpty(authorId)) || (StringUtils.isEmpty(isHide))) {
				dataResult.setFlag("3");
				dataResult.setMessage("主键、显示状态不能为空");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}
			Author author = this.authorService.findById(Long.valueOf(Long.parseLong(authorId)));
			author.setIsHide(isHide);
			this.authorService.save(author);
			dataResult.setMessage("设置成功");
			returnJson = JSONObject.fromObject(dataResult);
			String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
			return StringUtils.replaceBlank(s);
		} catch (Exception e) {
			e.printStackTrace();
			dataResult.setFlag("3");
			dataResult.setMessage("设置显示状态失败");
			returnJson = JSONObject.fromObject(dataResult);
			String s = "";
			try {
				s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return StringUtils.replaceBlank(s);
		}
	}

	@ResponseBody
	@RequestMapping({ "/deleteAuthor" })
	public String doDeleteAuthor(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);

		try {
			String jsonData = RequestUtil.decryptRequestJson(request, sessionData);
			JSONObject json = JSONObject.fromObject(jsonData);
			String sessionIp = sessionData.getIp();
			String sessionUserid = sessionData.getUserId();
			String sessionUserName = sessionData.getUserName();

			String authorId = json.get("authorId") == null ? "" : json.getString("authorId");
			if (StringUtils.isEmpty(authorId)) {
				dataResult.setFlag("3");
				dataResult.setMessage("主键不能为空");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}

			Author author = authorService.findById(Long.parseLong(authorId));
			int result = this.authorService.deleteById(Long.valueOf(Long.parseLong(authorId)));
			if (result < 1) {
				dataResult.setFlag("3");
				dataResult.setMessage("删除失败，找不到该ID");
			} else {
				dataResult.setMessage("删除成功");
				/**
				 * 保存日志
				 */
				Sylog sylog = new Sylog();
				sylog.setTableid(Long.parseLong(authorId));
				sylog.setTablename("syuser");
				sylog.setSyuserid(Long.parseLong(sessionUserid));
				sylog.setLogtype(CommonParameter.LOGFLAG_H004003);
				sylog.setLogdate(new Timestamp(new Date().getTime()));
				sylog.setContent(sessionUserName + "：" + sessionIp + "删除了作者《" + author.getAuthorName() + "》");

				sylogService.save(sylog);
			}
			returnJson = JSONObject.fromObject(dataResult);
			String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
			return StringUtils.replaceBlank(s);
		} catch (Exception e) {
			dataResult.setFlag("3");
			dataResult.setMessage("删除失败，" + e.getMessage());
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