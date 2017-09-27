package com.lws.web.controller;

import com.lws.domain.dao.WordbookDAO;
import com.lws.domain.entity.Syconfig;
import com.lws.domain.entity.Sylog;
import com.lws.domain.entity.Wordbook;
import com.lws.domain.model.DataResult;
import com.lws.domain.model.SessionData;
import com.lws.domain.service.SyconfigService;
import com.lws.domain.service.SylogService;
import com.lws.domain.utils.CommonParameter;
import com.lws.domain.utils.StringUtils;
import com.lws.domain.utils.pwd.AES;
import com.lws.domain.utils.request.CryptUtil;
import com.lws.domain.utils.request.RequestUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SyconfigController {

	@Resource
	private SyconfigService syconfigService;

	@Resource
	SylogService sylogService;

	@Resource
	private WordbookDAO wordbookDAO;

	@RequestMapping({ "/querySyconfigPanel" })
	public String doQuerySyconfigPanel(HttpServletRequest request, HttpServletResponse response) {
		return "/querySyconfigPanel";
	}

	@ResponseBody
	@RequestMapping({ "/querySyconfig" })
	public String doQuerySyconfig(HttpServletRequest request, HttpServletResponse response) {
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

			String syConfigId = json.get("syConfigId") == null ? "" : json.getString("syConfigId");
			String configKey = json.get("configKey") == null ? "" : json.getString("configKey");
			String configType = json.get("configType") == null ? "" : json.getString("configType");
			String isShow = json.get("isShow") == null ? "" : json.getString("isShow");

			Syconfig syconfig = new Syconfig();
			if (!(StringUtils.isEmpty(syConfigId))) {
				syconfig.setSyConfigId(Long.valueOf(Long.parseLong(syConfigId)));
			}
			if (!(StringUtils.isEmpty(configKey))) {
				syconfig.setConfigKey(configKey);
			}
			if (!(StringUtils.isEmpty(configType))) {
				syconfig.setConfigType(configType);
			}
			if (!(StringUtils.isEmpty(isShow)))
				syconfig.setIsShow(isShow);
			dataResult.setData(this.syconfigService.queryForPageCondition(syconfig, pageNo, pageSize));
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
	@RequestMapping({ "/saveSyconfig" })
	public String doSaveSyconfig(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);

		try {
			String jsonData = RequestUtil.decryptRequestJson(request, sessionData);
			JSONObject json = JSONObject.fromObject(jsonData);
			String sessionIp = sessionData.getIp();
			String sessionUserid = sessionData.getUserId();
			String sessionUserName = sessionData.getUserName();

			String syConfigId = json.get("syConfigId") == null ? "" : json.getString("syConfigId");
			String configValue = json.get("configValue") == null ? "" : json.getString("configValue");
			String isShow = json.get("isShow") == null ? "" : json.getString("isShow");

			if (StringUtils.isEmpty(syConfigId)) {
				dataResult.setFlag("3");
				dataResult.setMessage("主键不能为空");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}

			Syconfig syconfig = this.syconfigService.findById(Long.valueOf(Long.parseLong(syConfigId)));
			if (!(StringUtils.isEmpty(configValue))) {
				syconfig.setConfigValue(configValue);
			}
			if (!(StringUtils.isEmpty(isShow))) {
				syconfig.setIsShow(isShow);
			}
			this.syconfigService.save(syconfig);
			dataResult.setData(syconfig);
			dataResult.setMessage("保存成功");

			Sylog sylog = new Sylog();
			sylog.setTableid(Long.parseLong(syConfigId));
			sylog.setTablename("syuser");
			sylog.setSyuserid(Long.parseLong(sessionUserid));
			sylog.setLogtype(CommonParameter.LOGFLAG_H004002);
			sylog.setLogdate(new Timestamp(new Date().getTime()));
			sylog.setContent(sessionUserName + "：" + sessionIp + "修改了系统配置《" + syconfig.getConfigKey() + "》");

			sylogService.save(sylog);
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
	@RequestMapping({ "/findByNetWork" })
	public String doFindByNetWork(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);
		try {
			String netWork = this.syconfigService.getValueByKey("netWork");
			if (StringUtils.isEmpty(netWork)) {
				dataResult.setFlag("3");
				dataResult.setMessage("系统找不到对应的netWork配置");
			}
			dataResult.setData(netWork);
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
	@RequestMapping({ "/updateByNetWork" })
	public String doUpdateByNetWork(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);
		try {
			String jsonData = RequestUtil.decryptRequestJson(request, sessionData);
			JSONObject json = JSONObject.fromObject(jsonData);
			String sessionIp = sessionData.getIp();
			String sessionUserid = sessionData.getUserId();
			String sessionUserName = sessionData.getUserName();

			String configValue = json.get("configValue") == null ? "" : json.getString("configValue");
			String configKey = "netWork";

			if (StringUtils.isEmpty(configValue)) {
				dataResult.setFlag("3");
				dataResult.setMessage("参数值不能为空");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}

			int result = this.syconfigService.updateByNetWork(configKey, configValue);
			if (result > 0) {
				dataResult.setMessage("保存成功");

				Syconfig config = new Syconfig();
				config.setConfigKey(configKey);
				List syconfigList = syconfigService.queryForCondition(config);
				if (syconfigList.size() < 1) {
					dataResult.setFlag("3");
					dataResult.setMessage("系统找不到对应的netWork配置");
				}
				Syconfig syconfig = (Syconfig) syconfigList.get(0);

				Sylog sylog = new Sylog();
				sylog.setTableid(syconfig.getSyConfigId());
				sylog.setTablename("syuser");
				sylog.setSyuserid(Long.parseLong(sessionUserid));
				sylog.setLogtype(CommonParameter.LOGFLAG_H004002);
				sylog.setLogdate(new Timestamp(new Date().getTime()));
				sylog.setContent(sessionUserName + "：" + sessionIp + "修改了系统配置《" + syconfig.getConfigKey() + "》");

				sylogService.save(sylog);
			} else {
				dataResult.setFlag("3");
				dataResult.setMessage("系统找不到对应的netWork配置");
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

	@ResponseBody
	@RequestMapping({ "/queryWordbook" })
	public String doQqueryWordbook(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		try {
			String jsonData = RequestUtil.decryptRequestJson(request, CryptUtil.defaultSessionKey, CryptUtil.defaultSessionIv);
			JSONObject json = JSONObject.fromObject(jsonData);

			String wordbookId = json.get("wordbookId") == null ? "" : json.getString("wordbookId");
			String wordbookCode = json.get("wordbookCode") == null ? "" : json.getString("wordbookCode");
			String parentsBookCode = json.get("parentsBookCode") == null ? "" : json.getString("parentsBookCode");
			Wordbook wordbook = new Wordbook();
			if (!(StringUtils.isEmpty(wordbookId))) {
				wordbook.setWordbookId(Long.valueOf(Long.parseLong(wordbookId)));
			}
			if (!(StringUtils.isEmpty(wordbookCode))) {
				wordbook.setWordbookCode(wordbookCode);
			}
			if (!(StringUtils.isEmpty(parentsBookCode))) {
				wordbook.setParentsBookCode(parentsBookCode);
			}

			List list = new ArrayList();
			list = this.wordbookDAO.queryForCondition(wordbook);
			dataResult.setData(list);
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
}