package com.lws.web.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lws.domain.entity.Article;
import com.lws.domain.entity.Attached;
import com.lws.domain.entity.Sylog;
import com.lws.domain.model.ArticleQueryModel;
import com.lws.domain.model.DataResult;
import com.lws.domain.model.Page;
import com.lws.domain.model.SessionData;
import com.lws.domain.service.ArticleService;
import com.lws.domain.service.AttachedService;
import com.lws.domain.service.SyconfigService;
import com.lws.domain.service.SylogService;
import com.lws.domain.service.WechatService;
import com.lws.domain.utils.CommonParameter;
import com.lws.domain.utils.StringUtils;
import com.lws.domain.utils.pwd.AES;
import com.lws.domain.utils.request.CryptUtil;
import com.lws.domain.utils.request.RequestUtil;

@Controller
public class ArticleController {

	private final static Logger log = LoggerFactory.getLogger(ArticleController.class);

	@Resource
	ArticleService articleService;

	@Resource
	AttachedService attachedService;

	@Resource
	SyconfigService syconfigService;

	@Resource
	WechatService wechatService;

	@Resource
	SylogService sylogService;

	@RequestMapping({ "/queryArticlePanel" })
	public String doQueryArticlePanel(HttpServletRequest request, HttpServletResponse response) {
		return "/queryArticlePanel";
	}

	@RequestMapping({ "/editArticlePanel" })
	public String doEditArticlePanel(HttpServletRequest request, HttpServletResponse response) {
		return "/editArticlePanel";
	}

	@RequestMapping({ "/wxQueryArticlePanel" })
	public String doWxQueryArticlePanel(HttpServletRequest request, HttpServletResponse response) {
		return "/wxQueryArticlePanel";
	}

	@RequestMapping({ "/wxEditArticlePanel" })
	public String doWxEditArticlePanel(HttpServletRequest request, HttpServletResponse response) {
		return "/wxEditArticlePanel";
	}

	@RequestMapping({ "/wxMainPanel" })
	public String doWxMainPanel(HttpServletRequest request, HttpServletResponse response) {
		return "/wxMainPanel";
	}

	@ResponseBody
	@RequestMapping({ "/findAllArticle" })
	public String doFindAllArticle(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);
		try {
			List list = this.articleService.findAll(request);
			returnJson = JSONObject.fromObject(dataResult);
			String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
			return StringUtils.replaceBlank(s);
		} catch (Exception e) {
			e.printStackTrace();
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
	@RequestMapping({ "/findMaxArticleId" })
	public String doFindMaxArticleId(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);
		try {
			int maxId = this.articleService.findMaxId();
			dataResult.setData(Integer.valueOf(maxId));
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
	@RequestMapping({ "/changeArticleType" })
	public String doChangeArticleType(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);
		try {
			String jsonData = RequestUtil.decryptRequestJson(request, sessionData);
			JSONObject json = JSONObject.fromObject(jsonData);
			String sessionIp = sessionData.getIp();
			String sessionUserid = sessionData.getUserId();
			String sessionUserName = sessionData.getUserName();

			String articleId = null == json.get("articleId") ? "" : json.getString("articleId");
			String articleType = null == json.get("articleType") ? "" : json.getString("articleType");

			boolean requiredFlag = false;
			String requiredMsg = "";
			if (StringUtils.isEmpty(articleId)) {
				requiredFlag = true;
				requiredMsg = "主键";
			}
			if (StringUtils.isEmpty(articleType)) {
				requiredFlag = true;
				requiredMsg = "文章分类";
			}
			if (requiredFlag) {
				dataResult.setFlag("3");
				dataResult.setMessage(requiredMsg + "不能为空");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}

			Article article = this.articleService.findById(Long.valueOf(Long.parseLong(articleId)), request);
			article.setArticleType(articleType);
			this.articleService.save(article, request);
			dataResult.setMessage("设置成功");

			/**
			 * 保存日志
			 */
			Sylog sylog = new Sylog();
			sylog.setTableid(article.getArticleId());
			sylog.setTablename("article");
			sylog.setSyuserid(Long.parseLong(sessionUserid));
			sylog.setLogtype(CommonParameter.LOGFLAG_H004002);
			sylog.setLogdate(new Timestamp(new Date().getTime()));
			sylog.setContent(sessionUserName + "：" + sessionIp + "修改了文章分类《" + article.getTitle() + "》");

			sylogService.save(sylog);

			returnJson = JSONObject.fromObject(dataResult);
			String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
			return StringUtils.replaceBlank(s);
		} catch (Exception e) {
			e.printStackTrace();
			dataResult.setFlag("3");
			dataResult.setMessage("设置文章分类失败");
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
	@RequestMapping({ "/findWxArticle" })
	public String doFindWxArticle(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		try {
			String jsonData = RequestUtil.decryptRequestJson(request, CryptUtil.defaultSessionKey, CryptUtil.defaultSessionIv);
			JSONObject json = JSONObject.fromObject(jsonData);

			String pageNoStr = null == json.get("pageNo") ? "" : json.getString("pageNo");
			String pageSizeStr = null == json.get("pageSize") ? "" : json.getString("pageSize");
			String articleType = null == json.get("articleType") ? "" : json.getString("articleType");

			int pageNo = 1;
			int pageSize = 10;
			if (!(StringUtils.isEmpty(pageNoStr))) {
				pageNo = Integer.valueOf(pageNoStr).intValue();
			}
			if (!(StringUtils.isEmpty(pageSizeStr))) {
				pageSize = Integer.valueOf(pageSizeStr).intValue();
			}
			ArticleQueryModel model = new ArticleQueryModel();
			model.setStatus(CommonParameter.STATUS_H001003);
			if (!(StringUtils.isEmpty(articleType)))
				model.setArticleType(articleType);
			Page page = this.articleService.queryForWxPage(model, request, pageNo, pageSize);
			dataResult.setData(page);
			returnJson = JSONObject.fromObject(dataResult);
			String s = AES.Encrypt(returnJson.toString(), CryptUtil.defaultSessionKey, CryptUtil.defaultSessionIv);
			return StringUtils.replaceBlank(s);
		} catch (Exception e) {
			e.printStackTrace();
			dataResult.setFlag("3");
			dataResult.setMessage(e.getMessage());
			returnJson = JSONObject.fromObject(dataResult);
			String s = "";
			try {
				s = AES.Encrypt(returnJson.toString(), CryptUtil.defaultSessionKey, CryptUtil.defaultSessionIv);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return StringUtils.replaceBlank(s);
		}
	}

	@ResponseBody
	@RequestMapping({ "/findArticleById" })
	public String doFindArticleById(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		try {
			String jsonData = RequestUtil.decryptRequestJson(request, CryptUtil.getDefaultSessionKey(), CryptUtil.getDefaultSessionIv());
			JSONObject json = JSONObject.fromObject(jsonData);

			String articleId = null == json.get("articleId") ? "" : json.getString("articleId");

			if (StringUtils.isEmpty(articleId)) {
				dataResult.setFlag("3");
				dataResult.setMessage("主键不能为空");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), CryptUtil.getDefaultSessionKey(), CryptUtil.getDefaultSessionIv());
				return StringUtils.replaceBlank(s);
			}
			Article article = this.articleService.findById(Long.valueOf(Long.parseLong(articleId)), request);
			dataResult.setData(article);
			returnJson = JSONObject.fromObject(dataResult);
			String s = AES.Encrypt(returnJson.toString(), CryptUtil.getDefaultSessionKey(), CryptUtil.getDefaultSessionIv());
			return StringUtils.replaceBlank(s);
		} catch (Exception e) {
			e.printStackTrace();
			dataResult.setFlag("3");
			dataResult.setMessage(e.getMessage());
			returnJson = JSONObject.fromObject(dataResult);
			String s = "";
			try {
				s = AES.Encrypt(returnJson.toString(), CryptUtil.getDefaultSessionKey(), CryptUtil.getDefaultSessionIv());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return StringUtils.replaceBlank(s);
		}
	}

	@ResponseBody
	@RequestMapping({ "/findArticle" })
	public String doFindArticle(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);

		try {
			String jsonData = RequestUtil.decryptRequestJson(request, sessionData);
			JSONObject json = JSONObject.fromObject(jsonData);

			String roleType = sessionData.getRoleType();

			String title = null == json.get("title") ? "" : json.getString("title");
			String articleType = null == json.get("articleType") ? "" : json.getString("articleType");
			String pageNoStr = null == json.get("pageNo") ? "" : json.getString("pageNo");
			String pageSizeStr = null == json.get("pageSize") ? "" : json.getString("pageSize");
			int pageNo = 1;
			int pageSize = 20;
			if (!(StringUtils.isEmpty(pageNoStr))) {
				pageNo = Integer.valueOf(pageNoStr).intValue();
			}
			if (!(StringUtils.isEmpty(pageSizeStr))) {
				pageSize = Integer.valueOf(pageSizeStr).intValue();
			}
			ArticleQueryModel model = new ArticleQueryModel();
			if (!(StringUtils.isEmpty(title))) {
				model.setTitle(title);
			}
			if (!(StringUtils.isEmpty(articleType))) {
				model.setArticleType(articleType);
			}
			model.setSyroleType(roleType);
			Page page = this.articleService.queryForPageCondition(model, request, pageNo, pageSize);
			dataResult.setData(page);
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
	@RequestMapping({ "/deleteArticle" })
	public String doDeleteArticle(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);
		try {
			String jsonData = RequestUtil.decryptRequestJson(request, sessionData);
			JSONObject json = JSONObject.fromObject(jsonData);
			String sessionIp = sessionData.getIp();
			String sessionUserid = sessionData.getUserId();
			String sessionUserName = sessionData.getUserName();

			String articleId = null == json.get("articleId") ? "" : json.getString("articleId");
			if (StringUtils.isEmpty(articleId)) {
				dataResult.setFlag("3");
				dataResult.setMessage("主键不能为空");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}

			String netWork = this.syconfigService.getValueByKey("netWork");
			if (StringUtils.isEmpty(netWork)) {
				netWork = "N";
			}

			List<Attached> attachedList = this.attachedService.findByAttachedId(Long.valueOf(Long.parseLong(articleId)));
			for (Attached attached : attachedList) {
				this.attachedService.deleteById(attached.getAttachedId());
			}
			Article article = this.articleService.findById(Long.valueOf(Long.parseLong(articleId)), request);

			String mediaId = article.getMediaId();
			if (!StringUtils.isEmpty(mediaId)) {
				if ("N".equals(netWork)) {
					dataResult.setFlag("3");
					dataResult.setMessage("该文章已经与微信同步，请打开网络再进行删除操作！");
					returnJson = JSONObject.fromObject(dataResult);
					String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
					return StringUtils.replaceBlank(s);
				}
				Map map = new HashMap();
				map.put("media_id", mediaId);
				String jsonstr = JSONArray.fromObject(map).toString();
				String jsondata = jsonstr.substring(1, jsonstr.length() - 1);
				this.wechatService.deleteWechatMaterial(jsondata);
			}

			int result = this.articleService.deleteById(Long.valueOf(Long.parseLong(articleId)));
			if (result < 1) {
				dataResult.setFlag("3");
				dataResult.setMessage("删除失败，找不到该ID");
			} else {
				dataResult.setMessage("删除成功");
				/**
				 * 保存日志
				 */
				Sylog sylog = new Sylog();
				sylog.setTableid(article.getArticleId());
				sylog.setTablename("article");
				sylog.setSyuserid(Long.parseLong(sessionUserid));
				sylog.setLogtype(CommonParameter.LOGFLAG_H004003);
				sylog.setLogdate(new Timestamp(new Date().getTime()));
				sylog.setContent(sessionUserName + "：" + sessionIp + "删除了文章《" + article.getTitle() + "》");

				sylogService.save(sylog);
			}
			returnJson = JSONObject.fromObject(dataResult);
			String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
			return StringUtils.replaceBlank(s);
		} catch (Exception e) {
			e.printStackTrace();
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

	@ResponseBody
	@RequestMapping({ "/changeArticleStatus" })
	public String doChangeArticleStatus(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);
		try {
			String jsonData = RequestUtil.decryptRequestJson(request, sessionData);
			JSONObject json = JSONObject.fromObject(jsonData);
			String sessionIp = sessionData.getIp();
			String sessionUserid = sessionData.getUserId();
			String sessionUserName = sessionData.getUserName();

			String articleId = null == json.get("articleId") ? "" : json.getString("articleId");
			String status = null == json.get("status") ? "" : json.getString("status");

			if (StringUtils.isEmpty(articleId)) {
				dataResult.setFlag("3");
				dataResult.setMessage("主键、状态不能为空");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}

			Article article = this.articleService.findById(Long.valueOf(Long.parseLong(articleId)), request);
			article.setStatus(status);
			this.articleService.save(article, request);
			dataResult.setMessage("设置文章状态成功");

			/**
			 * 保存日志
			 */
			Sylog sylog = new Sylog();
			sylog.setTableid(article.getArticleId());
			sylog.setTablename("article");
			sylog.setSyuserid(Long.parseLong(sessionUserid));
			sylog.setLogtype(CommonParameter.LOGFLAG_H004002);
			sylog.setLogdate(new Timestamp(new Date().getTime()));
			sylog.setContent(sessionUserName + "：" + sessionIp + "修改了文章状态《" + article.getTitle() + "》");

			sylogService.save(sylog);
			returnJson = JSONObject.fromObject(dataResult);
			String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
			return StringUtils.replaceBlank(s);
		} catch (Exception e) {
			e.printStackTrace();
			dataResult.setFlag("3");
			dataResult.setMessage("设置文章状态失败");
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
	@RequestMapping({ "/saveArticle" })
	public String doSaveArticle(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);

		Article article = new Article();
		String sessionIp = "";
		String sessionUserid = "";
		String sessionUserName = "";
		String logType = CommonParameter.LOGFLAG_H004001;
		try {
			String jsonData = RequestUtil.decryptRequestJson(request, sessionData);
			JSONObject json = JSONObject.fromObject(jsonData);
			sessionIp = sessionData.getIp();
			sessionUserid = sessionData.getUserId();
			sessionUserName = sessionData.getUserName();

			String title = json.get("title") == null ? "" : json.getString("title");
			String content = json.get("content") == null ? "" : json.getString("content");
			String author = json.get("author") == null ? "" : json.getString("author");
			String wxContent = json.get("wxContent") == null ? "" : json.getString("wxContent");
			String wxPicUrl = json.get("wxPicUrl") == null ? "" : json.getString("wxPicUrl");
			String status = json.get("status") == null ? "" : json.getString("status");
			String articleType = json.get("articleType") == null ? "" : json.getString("articleType");
			String sendFlag = json.get("sendFlag") == null ? "" : json.getString("sendFlag");

			String thumbMediaId = json.get("thumbMediaId") == null ? "" : json.getString("thumbMediaId");
			String articleId = json.get("articleId") == null ? "" : json.getString("articleId");

			boolean requiredFlag = false;
			String requiredMsg = "";
			if (StringUtils.isEmpty(title)) {
				requiredFlag = true;
				requiredMsg = "标题";
			}
			if (StringUtils.isEmpty(content)) {
				requiredFlag = true;
				requiredMsg = "内容";
			}
			if (StringUtils.isEmpty(author)) {
				requiredFlag = true;
				requiredMsg = "作者";
			}
			if (StringUtils.isEmpty(articleType)) {
				requiredFlag = true;
				requiredMsg = "文章分类";
			}
			if (StringUtils.isEmpty(status)) {
				requiredFlag = true;
				requiredMsg = "文章状态";
			}
			if (StringUtils.isEmpty(sendFlag)) {
				requiredFlag = true;
				requiredMsg = "群发状态";
			}
			if (StringUtils.isEmpty(wxContent)) {
				requiredFlag = true;
				requiredMsg = "微信简介";
			}
			if (StringUtils.isEmpty(wxPicUrl)) {
				requiredFlag = true;
				requiredMsg = "微信封面";
			}
			if (requiredFlag) {
				dataResult.setFlag("3");
				dataResult.setMessage(requiredMsg + "不能为空");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}

			if (!(StringUtils.isEmpty(articleId))) {
				logType = CommonParameter.LOGFLAG_H004002;
				try {
					if (Integer.parseInt(articleId) < 0) {
						throw new Exception("系统出错，无法获取ID");
					}
					article = this.articleService.findById(Long.valueOf(Long.parseLong(articleId)), request);
				} catch (Exception e) {
					e.printStackTrace();
					dataResult.setFlag("3");
					dataResult.setMessage(e.getMessage());
					returnJson = JSONObject.fromObject(dataResult);
					String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
					return StringUtils.replaceBlank(s);
				}
			}

			article.setTitle(title);
			article.setContent(content);
			article.setAuthor(author);
			article.setWxContent(wxContent);
			article.setWxPicUrl(wxPicUrl);
			article.setStatus(status);
			article.setSendFlag(sendFlag);
			article.setArticleType(articleType);
			article.setThumbMediaId(thumbMediaId);

			if (article.getArticleId() == null) {
				article.setCreateDate(new Timestamp(new Date().getTime()));
				article.setStatus(CommonParameter.STATUS_H001001);
				article.setSendFlag(CommonParameter.SENDFLAG_H003001);
				article.setCreateUserId(Long.valueOf(Long.parseLong(sessionData.getUserId())));
				article.setEditDate(new Timestamp(new Date().getTime()));
				article.setEditUserId(Long.valueOf(Long.parseLong(sessionData.getUserId())));
			} else {
				article.setEditDate(new Timestamp(new Date().getTime()));
				article.setStatus(status);
				article.setEditUserId(Long.valueOf(Long.parseLong(sessionData.getUserId())));
			}

			this.articleService.save(article, request);
			this.articleService.saveArticlePhotoURL(article);
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

		try {
			String netWork = this.syconfigService.getValueByKey("netWork");
			if (StringUtils.isEmpty(netWork)) {
				netWork = "N";
			}
			if ("Y".equals(netWork)) {
				article = this.articleService.checkArticleMaterial(article, request);

				article = this.articleService.createArticleMaterial(article, request);
			}
		} catch (Exception e) {
			String errorMsg = e.getMessage();
			if (errorMsg.indexOf("40007:不合法的媒体文件id") >= 0) {

				article.setMediaId("");
				try {
					articleService.save(article, request);
				} catch (Exception e1) {
				}
				dataResult.setFlag("3");
				dataResult.setMessage("与微信端文章不同步，请尝试再次点击保存！");
				returnJson = JSONObject.fromObject(dataResult);
				String s = "";
				try {
					s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				return StringUtils.replaceBlank(s);
			} else {
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

		try {
			/**
			 * 保存日志
			 */
			Sylog sylog = new Sylog();
			sylog.setTableid(article.getArticleId());
			sylog.setTablename("article");
			sylog.setSyuserid(Long.parseLong(sessionUserid));
			sylog.setLogtype(logType);
			sylog.setLogdate(new Timestamp(new Date().getTime()));
			if (CommonParameter.LOGFLAG_H004001.equals(logType)) {
				sylog.setContent(sessionUserName + "：" + sessionIp + "增加了文章《" + article.getTitle() + "》");
			} else if (CommonParameter.LOGFLAG_H004002.equals(logType)) {
				sylog.setContent(sessionUserName + "：" + sessionIp + "修改了文章《" + article.getTitle() + "》");
			}
			sylogService.save(sylog);

			dataResult.setData(article);
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

	/**
	 * 单独推送接口
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping({ "/examinArticle" })
	public String doExamineArticle(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);

		try {
			String jsonData = RequestUtil.decryptRequestJson(request, sessionData);
			JSONObject json = JSONObject.fromObject(jsonData);
			String sessionIp = sessionData.getIp();
			String sessionUserid = sessionData.getUserId();
			String sessionUserName = sessionData.getUserName();

			String netWork = this.syconfigService.getValueByKey("netWork");
			if (StringUtils.isEmpty(netWork)) {
				netWork = "N";
			}
			if (!"Y".equals(netWork)) {
				dataResult.setFlag("3");
				dataResult.setMessage("请在联网模式后再进行推送操作！");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}

			String articleId = json.get("articleId") == null ? "" : json.getString("articleId");
			if (StringUtils.isEmpty(articleId)) {
				dataResult.setFlag("3");
				dataResult.setMessage("主键不能为空");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}
			String openIDArrayStr = json.get("openid") == null ? "" : json.getString("openid");
			if (StringUtils.isEmpty(openIDArrayStr)) {
				dataResult.setFlag("3");
				dataResult.setMessage("openID不能为空");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}

			Article article = this.articleService.findById(Long.valueOf(Long.parseLong(articleId)), request);
			String mediaId = article.getMediaId();

			String[] openIDArray = openIDArrayStr.split(",");
			for (int i = 0; i < openIDArray.length; i++) {
				this.wechatService.examineMaterial(mediaId, openIDArray[i]);
				dataResult.setMessage("推送成功");
				/**
				 * 保存日志
				 */
				Sylog sylog = new Sylog();
				sylog.setTableid(article.getArticleId());
				sylog.setTablename("article");
				sylog.setSyuserid(Long.parseLong(sessionUserid));
				sylog.setLogtype(CommonParameter.LOGFLAG_H004005);
				sylog.setLogdate(new Timestamp(new Date().getTime()));
				sylog.setContent(sessionUserName + "：" + sessionIp + "推送预览文章《" + article.getTitle() + "》" + openIDArray[i]);

				sylogService.save(sylog);
			}
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

	/**
	 * 群发接口
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping({ "/massMaterial" })
	public String doMassMaterial(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);

		try {
			String jsonData = RequestUtil.decryptRequestJson(request, sessionData);
			JSONObject json = JSONObject.fromObject(jsonData);
			String sessionIp = sessionData.getIp();
			String sessionUserid = sessionData.getUserId();
			String sessionUserName = sessionData.getUserName();

			String netWork = this.syconfigService.getValueByKey("netWork");
			if (StringUtils.isEmpty(netWork)) {
				netWork = "N";
			}
			if (!"Y".equals(netWork)) {
				dataResult.setFlag("3");
				dataResult.setMessage("请在联网模式后再进行群发操作！");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}

			String articleId = json.get("articleId") == null ? "" : json.getString("articleId");
			if (StringUtils.isEmpty(articleId)) {
				dataResult.setFlag("3");
				dataResult.setMessage("主键不能为空");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}

			Article article = this.articleService.findById(Long.valueOf(Long.parseLong(articleId)), request);

			String status = article.getStatus();
			if (StringUtils.isEmpty(status) || !CommonParameter.STATUS_H001003.equals(status)) {
				dataResult.setFlag("3");
				dataResult.setMessage("文章审核通过才能群发");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}

			String mediaId = article.getMediaId();
			if (StringUtils.isEmpty(mediaId)) {
				dataResult.setFlag("3");
				dataResult.setMessage("文章与微信不同步，请保存文章后再群发！");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}

			Map map = this.wechatService.massMaterial(mediaId);
			if (null != map.get("msgId")) {
				String msgId = map.get("msgId").toString();
				article.setMsgId(msgId);
				article.setMediaDate(new Timestamp(System.currentTimeMillis()));
				article.setSendFlag(CommonParameter.SENDFLAG_H003002);
				articleService.save(article, request);
			}

			/**
			 * 保存日志
			 */
			Sylog sylog = new Sylog();
			sylog.setTableid(article.getArticleId());
			sylog.setTablename("article");
			sylog.setSyuserid(Long.parseLong(sessionUserid));
			sylog.setLogtype(CommonParameter.LOGFLAG_H004005);
			sylog.setLogdate(new Timestamp(new Date().getTime()));
			sylog.setContent(sessionUserName + "：" + sessionIp + "群发文章《" + article.getTitle() + "》");

			sylogService.save(sylog);
			dataResult.setMessage("群发成功");

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

	
	/**
	 * 获取URL
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping({ "/getArticleNetwordURL" })
	public String doGetArticleNetwordURL(HttpServletRequest request, HttpServletResponse response) {

		DataResult dataResult = new DataResult();

		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);
		try {
			String jsonData = RequestUtil.decryptRequestJson(request, sessionData);
			JSONObject json = JSONObject.fromObject(jsonData);
			String articleId = json.get("articleId") == null ? "" : json.getString("articleId");
			if (StringUtils.isEmpty(articleId)) {
				dataResult.setFlag("3");
				dataResult.setMessage("获取失败");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}
			String serverRequestURL = StringUtils.getServerRequestPojoURL(request.getRequestURL().toString());
			dataResult.setData(serverRequestURL + "wxEditArticlePanel?articleId=" + articleId);
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