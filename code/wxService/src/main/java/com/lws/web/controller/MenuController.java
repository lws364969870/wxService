package com.lws.web.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lws.domain.entity.Sylog;
import com.lws.domain.entity.Symenu;
import com.lws.domain.entity.Wxmenu;
import com.lws.domain.model.DataResult;
import com.lws.domain.model.SessionData;
import com.lws.domain.service.SylogService;
import com.lws.domain.service.SymenuService;
import com.lws.domain.service.WxmenuService;
import com.lws.domain.utils.CommonParameter;
import com.lws.domain.utils.StringUtils;
import com.lws.domain.utils.pwd.AES;
import com.lws.domain.utils.request.CryptUtil;
import com.lws.domain.utils.request.RequestUtil;

@Controller
public class MenuController {

	@Resource
	SymenuService symenuService;

	@Resource
	WxmenuService wxmenuService;

	@Resource
	SylogService sylogService;

	@RequestMapping({ "/editMenuPanel" })
	public String doEditMenuPanel(HttpServletRequest request, HttpServletResponse response) {
		return "/editMenuPanel";
	}

	@RequestMapping({ "/queryMenuPanel" })
	public String doQueryMenuPanel(HttpServletRequest request, HttpServletResponse response) {
		return "/queryMenuPanel";
	}

	@RequestMapping({ "/queryWxMenuPanel" })
	public String doQueryWxMenuPanel(HttpServletRequest request, HttpServletResponse response) {
		return "/queryWxMenuPanel";
	}

	@ResponseBody
	@RequestMapping({ "/queryMenu" })
	public String doQueryMenu(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);
		try {
			String sessionUserid = sessionData.getUserId();
			dataResult.setData(this.symenuService.findAll(Long.parseLong(sessionUserid)));
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
	@RequestMapping({ "/findLevelMenu" })
	public String doFindLevelMenu(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);
		try {
			String sessionUserid = sessionData.getUserId();
			dataResult.setData(this.symenuService.findLevelMenu(Long.parseLong(sessionUserid)));
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
	@RequestMapping({ "/findLevelWxMenu" })
	public String doFindLevelWxMenu(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		try {
			String jsonData = RequestUtil.decryptRequestJson(request, CryptUtil.defaultSessionKey, CryptUtil.defaultSessionIv);

			dataResult.setData(this.wxmenuService.findLevelWxMenu());
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
	@RequestMapping({ "/findSymenuById" })
	public String doFindSymenuById(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);
		try {
			String jsonData = RequestUtil.decryptRequestJson(request, sessionData);
			JSONObject json = JSONObject.fromObject(jsonData);

			String syMenuId = json.get("syMenuId") == null ? "" : json.getString("syMenuId");
			if (StringUtils.isEmpty(syMenuId)) {
				dataResult.setFlag("3");
				dataResult.setMessage("主键不能为空");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}

			Symenu symenu = this.symenuService.findById(Long.parseLong(syMenuId));
			dataResult.setData(symenu);
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
	@RequestMapping({ "/saveMenu" })
	public String doSaveMenu(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);
		try {
			String jsonData = RequestUtil.decryptRequestJson(request, sessionData);
			JSONObject json = JSONObject.fromObject(jsonData);
			String sessionIp = sessionData.getIp();
			String sessionUserid = sessionData.getUserId();
			String sessionUserName = sessionData.getUserName();

			String syMenuId = json.get("syMenuId") == null ? "" : json.getString("syMenuId");
			String menuTitle = json.get("menuTitle") == null ? "" : json.getString("menuTitle");

			if (StringUtils.isEmpty(syMenuId) || StringUtils.isEmpty(menuTitle)) {
				dataResult.setFlag("3");
				dataResult.setMessage("主键、标题不能为空");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}

			Symenu symenu = this.symenuService.findById(Long.parseLong(syMenuId));
			if (null == symenu) {
				dataResult.setFlag("3");
				dataResult.setMessage("没有权限修改菜单");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}
			symenu.setMenuTitle(menuTitle);
			this.symenuService.save(symenu);

			Wxmenu wxQueryObj = new Wxmenu();
			wxQueryObj.setMenuName(menuTitle);
			wxQueryObj.setMenuCode(symenu.getMenuCode());
			List list = this.wxmenuService.queryForCondition(wxQueryObj);
			if (list.size() > 0) {
				Wxmenu wxmenu = (Wxmenu) list.get(0);
				wxmenu.setMenuName(menuTitle);
				this.wxmenuService.save(wxmenu);
			}

			dataResult.setData(symenu);
			dataResult.setMessage("保存成功");

			/**
			 * 保存日志
			 */
			Sylog sylog = new Sylog();
			sylog.setTableid(Long.parseLong(syMenuId));
			sylog.setTablename("symenu");
			sylog.setSyuserid(Long.parseLong(sessionUserid));
			sylog.setLogtype(CommonParameter.LOGFLAG_H004002);
			sylog.setLogdate(new Timestamp(new Date().getTime()));
			sylog.setContent(sessionUserName + "：" + sessionIp + "修改了菜单《" + symenu.getMenuTitle() + "》");

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
				e1.printStackTrace();
			}
			return StringUtils.replaceBlank(s);
		}
	}

	@ResponseBody
	@RequestMapping({ "/queryWxMenu" })
	public String doQueryWxMenu(HttpServletRequest request, HttpServletResponse response) {

		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		try {
			String jsonData = RequestUtil.decryptRequestJson(request, CryptUtil.defaultSessionKey, CryptUtil.defaultSessionIv);
			JSONObject json = JSONObject.fromObject(jsonData);

			String wxMenuId = json.get("wxMenuId") == null ? "" : json.getString("wxMenuId");
			String menuCode = json.get("menuCode") == null ? "" : json.getString("menuCode");
			String isCatalog = json.get("isCatalog") == null ? "" : json.getString("isCatalog");

			Wxmenu wxmenu = new Wxmenu();
			if (!(StringUtils.isEmpty(wxMenuId))) {
				wxmenu.setWxMenuId(Long.valueOf(Long.parseLong(wxMenuId)));
			}
			if (!(StringUtils.isEmpty(menuCode))) {
				wxmenu.setMenuCode(menuCode);
			}
			if (!(StringUtils.isEmpty(isCatalog))) {
				wxmenu.setIsCatalog(isCatalog);
			}

			List list = this.wxmenuService.queryForCondition(wxmenu);
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

	@ResponseBody
	@RequestMapping({ "/queryMenuTitleByMenuCode" })
	public String doQueryMenuTitleByMenuCode(HttpServletRequest request, HttpServletResponse response) {

		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		try {
			String jsonData = RequestUtil.decryptRequestJson(request, CryptUtil.defaultSessionKey, CryptUtil.defaultSessionIv);
			JSONObject json = JSONObject.fromObject(jsonData);

			String menuCode = json.get("menuCode") == null ? "" : json.getString("menuCode");
			Wxmenu wxmenu = new Wxmenu();
			if (org.apache.commons.lang.StringUtils.isNotEmpty(menuCode)) {
				wxmenu.setMenuCode(menuCode);
			}

			List list = this.wxmenuService.queryForCondition(wxmenu);
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

	@ResponseBody
	@RequestMapping(value = "/lwsFindAllMenu", method = RequestMethod.GET)
	public Object doLwsFindAllMenu(HttpServletRequest request, HttpServletResponse response) {
		return this.symenuService.findAll();
	}

	@ResponseBody
	@RequestMapping(value = "/lwsAddMenu", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	public String doLwsAddMenu(HttpServletRequest request, HttpServletResponse response) {
		// response.setCharacterEncoding("UTF-8");
		// lwsAddMenu?menuName=统计数据&menuTitle=统计数据&menuCode=10099&parentsCode=10000&menuLevel=2&menuUrl=queryArticlePanel?articleType=10099&sort=30
		String menuName = request.getParameter("menuName");
		String menuTitle = request.getParameter("menuTitle");
		String menuCode = request.getParameter("menuCode");
		String parentsCode = request.getParameter("parentsCode");
		Integer menuLevel = Integer.valueOf(request.getParameter("menuLevel"));
		String menuUrl = request.getParameter("menuUrl");
		Integer sort = Integer.valueOf(request.getParameter("sort"));
		if (StringUtils.isEmpty(menuName) || StringUtils.isEmpty(menuTitle) || StringUtils.isEmpty(menuCode) || StringUtils.isEmpty(parentsCode) || StringUtils.isEmpty(menuUrl) || null == sort) {
			return "参数不全";
		}
		Symenu symenu = new Symenu();
		symenu.setMenuName(menuName);
		symenu.setMenuTitle(menuTitle);
		symenu.setMenuCode(menuCode);
		symenu.setParentsCode(parentsCode);
		symenu.setMenuLevel(menuLevel);
		symenu.setMenuUrl(menuUrl);
		symenu.setIsDisable("N");
		symenu.setIsCatalog("N");
		symenu.setIsHide("N");
		symenu.setSort(sort);
		try {
			symenuService.save(symenu);

			symenuService.createSQl("delete from syrolemenu");
			symenuService.createSQl("INSERT INTO syrolemenu ( syrolemenu.syroleId, syrolemenu.syMenuId, syrolemenu.isShow) SELECT syrole.syroleid, symenu.symenuid, 'Y' FROM syrole, symenu");

		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return "保存成功";
	}

	@ResponseBody
	@RequestMapping(value = "/lwsEditMenu", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	public String doLwsEditMenu(HttpServletRequest request, HttpServletResponse response) {
		// lwsEditMenu?syMenuId=xx&menuName=统计数据&menuTitle=统计数据&menuCode=10099&parentsCode=10000&menuLevel=2&menuUrl=queryArticlePanel?articleType=10099&sort=30
		response.setCharacterEncoding("UTF-8");
		String syMenuId = request.getParameter("syMenuId");
		String menuName = request.getParameter("menuName");
		String menuTitle = request.getParameter("menuTitle");
		String menuCode = request.getParameter("menuCode");
		String parentsCode = request.getParameter("parentsCode");
		Integer menuLevel = Integer.valueOf(request.getParameter("menuLevel"));
		String menuUrl = request.getParameter("menuUrl");
		Integer sort = Integer.valueOf(request.getParameter("sort"));
		if (StringUtils.isEmpty(syMenuId) || StringUtils.isEmpty(menuName) || StringUtils.isEmpty(menuTitle) || StringUtils.isEmpty(menuCode) || StringUtils.isEmpty(parentsCode)
				|| StringUtils.isEmpty(menuUrl) || null == sort) {
			return "参数不全";
		}
		Symenu symenu = new Symenu();
		try {
			symenu = symenuService.findById(Long.valueOf(syMenuId));
		} catch (Exception e1) {
			e1.printStackTrace();
			return e1.getMessage();
		}
		symenu.setMenuName(menuName);
		symenu.setMenuTitle(menuTitle);
		symenu.setMenuCode(menuCode);
		symenu.setParentsCode(parentsCode);
		symenu.setMenuLevel(menuLevel);
		symenu.setMenuUrl(menuUrl);
		symenu.setIsDisable("N");
		symenu.setIsCatalog("N");
		symenu.setIsHide("N");
		symenu.setSort(sort);
		try {
			symenuService.save(symenu);
			symenuService.createSQl("delete from syrolemenu");
			symenuService.createSQl("INSERT INTO syrolemenu ( syrolemenu.syroleId, syrolemenu.syMenuId, syrolemenu.isShow) SELECT syrole.syroleid, symenu.symenuid, 'Y' FROM syrole, symenu");

		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return "保存成功";
	}

	@ResponseBody
	@RequestMapping(value = "/lwsDeleteMenu", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	public String doLwsDeleteMenu(HttpServletRequest request, HttpServletResponse response) {
		// lwsDeleteMenu?syMenuId=xx
		response.setCharacterEncoding("UTF-8");
		String syMenuId = request.getParameter("syMenuId");
		if (StringUtils.isEmpty(syMenuId)) {
			return "参数不全";
		}
		try {
			symenuService.deleteById(Long.valueOf(syMenuId));
			symenuService.createSQl("delete from syrolemenu");
			symenuService.createSQl("INSERT INTO syrolemenu ( syrolemenu.syroleId, syrolemenu.syMenuId, syrolemenu.isShow) SELECT syrole.syroleid, symenu.symenuid, 'Y' FROM syrole, symenu");

		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return "删除成功";
	}
}