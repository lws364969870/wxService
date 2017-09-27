package com.lws.domain.service;

import com.lws.domain.dao.ArticleDAO;
import com.lws.domain.dao.AttachedDAO;
import com.lws.domain.dao.SyconfigDAO;
import com.lws.domain.entity.Article;
import com.lws.domain.entity.Attached;
import com.lws.domain.model.ArticleQueryModel;
import com.lws.domain.model.Page;
import com.lws.domain.model.wechat.ArticlesModel;
import com.lws.domain.utils.StringUtils;
import com.lws.domain.utils.properties.ApplicationsPropertiesUtils;
import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {

	@Resource
	ArticleDAO articleDAO;

	@Resource
	AttachedDAO attachedDAO;

	@Resource
	SyconfigDAO syconfigDAO;

	@Resource
	AttachedService attachedService;

	@Resource
	WechatService wechatService;

	public void save(Article article, HttpServletRequest request) throws Exception {
		if ((article != null) && (article.getArticleId() != null)) {
			String newContent = changeContentPhotoURL(request, article.getContent(), article.getArticleId());
			String newWxPicUrl = changeVisitWxPicURL(request, article.getWxPicUrl());
			article.setContent(newContent);
			article.setWxPicUrl(newWxPicUrl);
		}
		this.articleDAO.save(article);
	}

	public List findAll(HttpServletRequest request) throws Exception {
		List<Article> list = this.articleDAO.findAll();
		for (Article article : list) {
			String newContent = changeContentPhotoURL(request, article.getContent(), article.getArticleId());
			String newWxPicUrl = changeVisitWxPicURL(request, article.getWxPicUrl());
			article.setContent(newContent);
			article.setWxPicUrl(newWxPicUrl);
		}
		return list;
	}

	public List findByHqlExample(ArticleQueryModel model, HttpServletRequest request, int pageNo, int pageSize) throws Exception {
		List<Article> list = this.articleDAO.findByHqlExample(model, pageNo, pageSize);
		for (Article article : list) {
			String newContent = changeContentPhotoURL(request, article.getContent(), article.getArticleId());
			String newWxPicUrl = changeVisitWxPicURL(request, article.getWxPicUrl());
			article.setContent(newContent);
			article.setWxPicUrl(newWxPicUrl);
		}
		return list;
	}

	public int deleteById(Long articleId) throws Exception {
		return this.articleDAO.deleteById(articleId);
	}

	public Article findById(Long id, HttpServletRequest request) throws Exception {
		Article article = this.articleDAO.findById(id);
		if (article != null) {
			String newContent = changeContentPhotoURL(request, article.getContent(), article.getArticleId());
			String newWxPicUrl = changeVisitWxPicURL(request, article.getWxPicUrl());
			article.setContent(newContent);
			article.setWxPicUrl(newWxPicUrl);
		}
		return article;
	}

	public int findMaxId() throws Exception {
		return this.articleDAO.findMaxId();
	}

	public Page queryForPageCondition(ArticleQueryModel model, HttpServletRequest request, int pageNo, int pageSize) throws Exception {
		Page page = new Page();

		int allRow = this.articleDAO.queryAllCounts(model);

		int offset = page.countOffset(pageNo, pageSize);

		List<Article> list = this.articleDAO.queryForPageCondition(model, offset, pageSize);
		for (Article article : list) {
			String newContent = changeContentPhotoURL(request, article.getContent(), article.getArticleId());
			String newWxPicUrl = changeVisitWxPicURL(request, article.getWxPicUrl());
			article.setContent(newContent);
			article.setWxPicUrl(newWxPicUrl);
		}

		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setTotalRecords(allRow);
		page.setList(list);
		return page;
	}

	public Page queryForWxPage(ArticleQueryModel model, HttpServletRequest request, int pageNo, int pageSize) throws Exception {
		Page page = new Page();

		int allRow = this.articleDAO.queryAllCounts(model);

		int offset = page.countOffset(pageNo, pageSize);

		List<Article> list = this.articleDAO.queryForPageCondition(model, offset, pageSize);

		for (Article article : list) {
			article.setContent("");
			String newWxPicUrl = changeVisitWxPicURL(request, article.getWxPicUrl());
			article.setWxPicUrl(newWxPicUrl);
		}
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setTotalRecords(allRow);
		page.setList(list);
		return page;
	}

	public String changeContentPhotoURL(HttpServletRequest request, String content, Long articleId) throws Exception {
		String serverRequestURL = StringUtils.getServerRequestURL(request.getRequestURL().toString());
		String newContent = content;
		String imgArryStr = StringUtils.getImgArrayString(newContent);
		String[] imgArry = imgArryStr.split(",");

		List<Attached> attachedList = this.attachedDAO.findByArticleId(articleId);
		if (imgArry.length > 0) {
			for (Attached attached : attachedList) {
				for (int i = 0; i < imgArry.length; ++i) {
					String imgFileName = StringUtils.getFileName(imgArry[i]);
					String netFileName = StringUtils.getFileName(attached.getNetwordURL());
					if (imgFileName.equals(netFileName)) {
						String networdUrl = serverRequestURL + attached.getVisitURL();
						newContent = newContent.replaceAll(imgArry[i], networdUrl);
					}
				}
			}
		}
		return newContent;
	}

	public String changeVisitWxPicURL(HttpServletRequest request, String url) {
		String serverRequestURL = StringUtils.getServerRequestURL(request.getRequestURL().toString());
		String visitURL = StringUtils.getVisitURL(url);
		return serverRequestURL + visitURL;
	}

	public void saveArticlePhotoURL(Article article) throws Exception {
		String contentUrlArray = StringUtils.getImgArrayString(article.getContent());

		if (!(StringUtils.isEmpty(contentUrlArray))) {
			String[] urlArray = contentUrlArray.split(",");
			for (int i = 0; i < urlArray.length; ++i) {
				String url = urlArray[i];
				if (StringUtils.isEmpty(url))
					continue;
				this.attachedService.updateArticleIdByNetwordURL(article.getArticleId(), url);

				this.attachedService.updateArticleIdByWechatURL(article.getArticleId(), url);
			}
		}
	}

	public Article checkArticleMaterial(Article article, HttpServletRequest request) throws Exception {

		boolean articleSaveFlag = false;
		String wxPicUrl = article.getWxPicUrl();

		if (StringUtils.isEmpty(article.getThumbMediaId())) {
			String visitUrl = ApplicationsPropertiesUtils.getValue("visit.url");

			String uploadingUrl = ApplicationsPropertiesUtils.getValue("uploading.url");

			String absoluteURL = uploadingUrl + wxPicUrl.substring(wxPicUrl.indexOf(visitUrl) + visitUrl.length());
			File file = new File(absoluteURL);
			if (!(file.exists())) {
				throw new Exception("文章的封面图片未上传！");
			}
			try {
				Map map = this.wechatService.putWechatThumbPhoto(absoluteURL);
				if (map.get("thumbMediaId") == null) {
					throw new Exception("文章的封面图片上传微信失败！");
				}
				article.setThumbMediaId(map.get("thumbMediaId").toString());
				articleSaveFlag = true;
			} catch (Exception e) {
				throw new Exception("文章的封面图片上传微信失败！" + e.getMessage());
			}
		}

		List<Attached> attachedList = this.attachedService.findByAttachedId(article.getArticleId());
		for (Attached attached : attachedList) {
			String wechatURL = attached.getWechatURL();

			String absoluteURL = attached.getAbsoluteURL();
			if ((StringUtils.isEmpty(wechatURL)) && (!(StringUtils.isEmpty(absoluteURL)))) {
				File file = new File(absoluteURL);
				if (file.exists()) {
					Map map = this.wechatService.putWechatUrlPhoto(absoluteURL);
					if (map.get("url") != null) {
						attached.setWechatURL(map.get("url").toString());
					}
				}
			}
			this.attachedService.save(attached);
		}

		if (articleSaveFlag) {
			save(article, request);
		}
		return article;
	}

	/**
	 * 创建素材
	 * @param article
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Article createArticleMaterial(Article article, HttpServletRequest request) throws Exception {
		String json;
		String content = article.getContent();
		String mediaConten = content;
		String contentArrayUrl = StringUtils.getImgArrayString(content);
		String[] contentArray = contentArrayUrl.split(",");
		List<Attached> attachedList = this.attachedService.findByAttachedId(article.getArticleId());
		for (Attached attached : attachedList) {
			for (int i = 0; i < contentArray.length; ++i) {
				if (!(StringUtils.isEmpty(contentArray[i]))) {
					String urlFileName = StringUtils.getFileName(contentArray[i]);
					String netFileName = StringUtils.getFileName(attached.getNetwordURL());
					if (urlFileName.equals(netFileName)) {
						mediaConten = mediaConten.replaceAll(contentArray[i], attached.getWechatURL());
					}
				}
			}
		}

		List articlesModelList = new ArrayList();
		Map map = new HashMap();
		ArticlesModel articlesModel = new ArticlesModel();
		articlesModel.setAuthor(article.getAuthor());
		articlesModel.setTitle(article.getTitle());
		articlesModel.setContent_source_url("");
		articlesModel.setContent(mediaConten);
		articlesModel.setDigest(article.getWxContent());
		articlesModel.setShow_cover_pic("0");
		articlesModel.setThumb_media_id(article.getThumbMediaId());
		articlesModelList.add(articlesModel);

		Map resultMap = new HashMap();
		if (StringUtils.isEmpty(article.getMediaId())) {
			map.put("articles", articlesModelList);
			json = JSONArray.fromObject(map).toString();
			json = json.substring(1, json.length() - 1);

			resultMap = this.wechatService.createWechatMaterial(json);
		} else {
			map.put("articles", articlesModel);
			map.put("media_id", article.getMediaId());
			json = JSONArray.fromObject(map).toString();
			json = json.substring(1, json.length() - 1);
			resultMap = this.wechatService.updateWechatMaterial(json);
		}

		if ((resultMap.get("errcode") != null) && ("0".equals(resultMap.get("errcode").toString()))) {
			article.setMediaDate(new Timestamp(new Date().getTime()));
		} else if (resultMap.get("mediaId") != null) {
			article.setMediaId(resultMap.get("mediaId").toString());
			article.setMediaDate(new Timestamp(new Date().getTime()));
			article.setMediaConten(mediaConten);
		} else {
			throw new Exception("生成素材失败！");
		}
		save(article, request);
		return article;
	}
}