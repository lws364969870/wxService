package com.lws.web.controller;

import com.lws.domain.entity.Subscribe;
import com.lws.domain.model.DataResult;
import com.lws.domain.model.SessionData;
import com.lws.domain.service.SubscribeService;
import com.lws.domain.service.WechatService;
import com.lws.domain.utils.StringUtils;
import com.lws.domain.utils.pwd.AES;
import com.lws.domain.utils.request.RequestUtil;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SubscribeController {

	@Resource
	SubscribeService subscribeService;

	@Resource
	WechatService wechatService;

	@RequestMapping({ "/querySubscribePanel" })
	public String doQueryUserPanel(HttpServletRequest request, HttpServletResponse response) {
		return "/queryUserPanel";
	}

	@ResponseBody
	@RequestMapping({ "/querySubscribe" })
	public String doQueryUser(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);

		try {
			String jsonData = RequestUtil.decryptRequestJson(request, sessionData);
			JSONObject json = JSONObject.fromObject(jsonData);

			Subscribe subscribe = new Subscribe();
			String pageNoStr = json.get("pageNo") == null ? "" : json.getString("pageNo");
			String pageSizeStr = json.get("pageSize") == null ? "" : json.getString("pageSize");
			int pageNo = 1;
			int pageSize = 20;
			if (!(StringUtils.isEmpty(pageNoStr))) {
				pageNo = Integer.valueOf(pageNoStr).intValue();
			}
			if (!(StringUtils.isEmpty(pageSizeStr))) {
				pageSize = Integer.valueOf(pageSizeStr).intValue();
			}

			String subscribeId = json.get("subscribeId") == null ? "" : json.getString("subscribeId");
			if (!(StringUtils.isEmpty(subscribeId))) {
				subscribe.setSubscribeId(Long.valueOf(Long.parseLong(subscribeId)));
			}

			String nickname = json.get("nickname") == null ? "" : json.getString("nickname");
			if (!(StringUtils.isEmpty(nickname))) {
				subscribe.setNickname(nickname);
			}

			String remark = json.get("remark") == null ? "" : json.getString("remark");
			if (!(StringUtils.isEmpty(remark))) {
				subscribe.setRemark(remark);
			}

			String openid = json.get("openid") == null ? "" : json.getString("openid");
			if (!(StringUtils.isEmpty(openid))) {
				subscribe.setOpenid(openid);
			}

			String isShow = json.get("isShow") == null ? "" : json.getString("isShow");
			if (!(StringUtils.isEmpty(isShow))) {
				subscribe.setIsShow(isShow);
			}
			dataResult.setData(this.subscribeService.queryForPageCondition(subscribe, pageNo, pageSize));
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
	@RequestMapping({ "/deleteSubscribe" })
	public String doDeleteSubscribe(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);
		try {
			String jsonData = RequestUtil.decryptRequestJson(request, sessionData);
			JSONObject json = JSONObject.fromObject(jsonData);

			String subscribeId =  json.get("subscribeId") == null ? "" :json.getString("subscribeId");
			if (StringUtils.isEmpty(subscribeId)) {
				dataResult.setFlag("3");
				dataResult.setMessage("主键不能为空");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}
			int result = this.subscribeService.deleteById(Long.valueOf(Long.parseLong(subscribeId)));
			if (result < 1) {
				dataResult.setFlag("3");
				dataResult.setMessage("删除失败，找不到该ID");
			} else {
				dataResult.setMessage("删除成功");
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

	@ResponseBody
	@RequestMapping({ "/findSubscribeById" })
	public String doFindSubscribeById(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);
		try {
			String jsonData = RequestUtil.decryptRequestJson(request, sessionData);
			JSONObject json = JSONObject.fromObject(jsonData);

			String subscribeId =json.get("subscribeId") == null ? "" : json.getString("subscribeId");
			if (StringUtils.isEmpty(subscribeId)) {
				dataResult.setFlag("3");
				dataResult.setMessage("主键不能为空");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}
			Subscribe subscribe = this.subscribeService.findById(Long.valueOf(Long.parseLong(subscribeId)));
			dataResult.setData(subscribe);
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
	@RequestMapping(value = { "/refreshSubscribe" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String doRefreshSubscribe(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);
		try {
			List<Subscribe> list = this.wechatService.refreshSubscribe();

			this.subscribeService.deleteAll();

			for (Subscribe subscribe : list) {
				this.subscribeService.save(subscribe);
			}
			dataResult.setData(list);
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