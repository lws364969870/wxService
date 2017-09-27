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

import com.lws.domain.entity.Sylog;
import com.lws.domain.entity.Syuser;
import com.lws.domain.model.DataResult;
import com.lws.domain.model.SessionData;
import com.lws.domain.service.SylogService;
import com.lws.domain.service.SyroleService;
import com.lws.domain.service.SyuserService;
import com.lws.domain.utils.CommonParameter;
import com.lws.domain.utils.StringUtils;
import com.lws.domain.utils.pwd.AES;
import com.lws.domain.utils.pwd.MD5;
import com.lws.domain.utils.pwd.RegExpValidatorUtils;
import com.lws.domain.utils.request.RequestUtil;

@Controller
public class UserController {

	@Resource
	SyuserService syuserService;

	@Resource
	SylogService sylogService;

	@Resource
	SyroleService syroleService;

	@RequestMapping({ "/queryUserPanel" })
	public String doQueryUserPanel(HttpServletRequest request, HttpServletResponse response) {
		return "/queryUserPanel";
	}

	@RequestMapping({ "/editUserPanel" })
	public String doEditUserPanel(HttpServletRequest request, HttpServletResponse response) {
		return "/editUserPanel";
	}

	@RequestMapping({ "/editUserPassWordPanel" })
	public String doEditUserPassWordPanel(HttpServletRequest request, HttpServletResponse response) {
		return "/editUserPassWordPanel";
	}

	@ResponseBody
	@RequestMapping({ "/queryUser" })
	public String doQueryUser(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);
		try {
			String jsonData = RequestUtil.decryptRequestJson(request, sessionData);
			JSONObject json = JSONObject.fromObject(jsonData);

			Syuser syuser = new Syuser();
			String pageNoStr = json.get("pageNo") == null ? "" : json.getString("pageNo");
			String pageSizeStr = json.get("pageSize") == null ? "" : json.getString("pageSize");
			String syUserId = json.get("syUserId") == null ? "" : json.getString("syUserId");

			int pageNo = 1;
			int pageSize = 20;
			if (!StringUtils.isEmpty(pageNoStr)) {
				pageNo = Integer.valueOf(pageNoStr).intValue();
			}
			if (!StringUtils.isEmpty(pageSizeStr)) {
				pageSize = Integer.valueOf(pageSizeStr).intValue();
			}
			if (!StringUtils.isEmpty(syUserId)) {
				syuser.setSyUserId(Long.valueOf(Long.parseLong(syUserId)));
			}
			dataResult.setData(this.syuserService.queryForPageCondition(syuser, pageNo, pageSize));
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
	@RequestMapping({ "/findSyuserById" })
	public String doFindSyuserById(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);

		try {
			String jsonData = RequestUtil.decryptRequestJson(request, sessionData);
			JSONObject json = JSONObject.fromObject(jsonData);

			String syUserId = json.get("syUserId") == null ? "" : json.getString("syUserId");

			if ((syUserId == null) && (syUserId.length() > 0)) {
				dataResult.setFlag("3");
				dataResult.setMessage("主键不能为空");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}
			Syuser syuser = this.syuserService.findById(Long.valueOf(Long.parseLong(syUserId)));
			dataResult.setData(syuser);
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
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return StringUtils.replaceBlank(s);
		}
	}

	@ResponseBody
	@RequestMapping({ "/deleteUser" })
	public String doDeleteUser(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);

		try {
			String jsonData = RequestUtil.decryptRequestJson(request, sessionData);
			JSONObject json = JSONObject.fromObject(jsonData);
			String sessionIp = sessionData.getIp();
			String sessionUserid = sessionData.getUserId();
			String sessionUserName = sessionData.getUserName();
			String roleType = sessionData.getRoleType();
			if (!"RT001".equals(roleType)) {
				dataResult.setFlag("3");
				dataResult.setMessage("当前角色不具有删除用户权限");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}

			String syUserId = json.get("syUserId") == null ? "" : json.getString("syUserId");
			if (syUserId == null) {
				dataResult.setFlag("3");
				dataResult.setMessage("主键不能为空");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}

			Syuser syuser = syuserService.findById(Long.parseLong(syUserId));
			int result = this.syuserService.deleteById(Long.valueOf(Long.parseLong(syUserId)));
			if (result < 1) {
				dataResult.setFlag("3");
				dataResult.setMessage("删除失败，找不到该ID");
			} else {
				dataResult.setMessage("删除成功");
				/**
				 * 保存日志
				 */
				Sylog sylog = new Sylog();
				sylog.setTableid(Long.parseLong(syUserId));
				sylog.setTablename("syuser");
				sylog.setSyuserid(Long.parseLong(sessionUserid));
				sylog.setLogtype(CommonParameter.LOGFLAG_H004003);
				sylog.setLogdate(new Timestamp(new Date().getTime()));
				sylog.setContent(sessionUserName + "：" + sessionIp + "删除了用户《" + syuser.getUserName() + "》");

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
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return StringUtils.replaceBlank(s);
		}
	}

	@ResponseBody
	@RequestMapping({ "/saveSyuser" })
	public String doSaveSyuser(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);

		try {
			String jsonData = RequestUtil.decryptRequestJson(request, sessionData);
			JSONObject json = JSONObject.fromObject(jsonData);
			String logType = CommonParameter.LOGFLAG_H004001;
			String sessionIp = sessionData.getIp();
			String sessionUserid = sessionData.getUserId();
			String sessionUserName = sessionData.getUserName();
			String roleType = sessionData.getRoleType();

			String syUserId = json.get("syUserId") == null ? "" : json.getString("syUserId");
			String userName = json.get("userName") == null ? "" : json.getString("userName");
			String loginName = json.get("loginName") == null ? "" : json.getString("loginName");
			String passWord = json.get("passWord") == null ? "" : json.getString("passWord");
			String phone = json.get("phone") == null ? "" : json.getString("phone");
			String mobile = json.get("mobile") == null ? "" : json.getString("mobile");
			String remarks = json.get("remarks") == null ? "" : json.getString("remarks");

			if (StringUtils.isEmpty(userName)) {
				dataResult.setFlag("3");
				dataResult.setMessage("用户名称不能为空");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}
			if (StringUtils.isEmpty(loginName)) {
				dataResult.setFlag("3");
				dataResult.setMessage("登录帐号不能为空");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}

			Syuser syuser = new Syuser();

			if (!StringUtils.isEmpty(syUserId)) {
				logType = CommonParameter.LOGFLAG_H004002;
				syuser = this.syuserService.findById(Long.valueOf(Long.parseLong(syUserId)));
			}
			if (this.syuserService.checkExistsLoginName(loginName)) {
				dataResult.setFlag("3");
				dataResult.setMessage("登录名已存在");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}

			syuser.setUserName(userName);
			syuser.setLoginName(loginName);

			if (!StringUtils.isEmpty(passWord)) {
				if (!"RT001".equals(roleType)) {
					dataResult.setFlag("3");
					dataResult.setMessage("当前角色不具有修改用户权限");
					returnJson = JSONObject.fromObject(dataResult);
					String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
					return StringUtils.replaceBlank(s);
				}

				if (!RegExpValidatorUtils.IsNotInSymbol(passWord)) {
					dataResult.setFlag("3");
					dataResult.setMessage("密码存在不能识别的特殊符号");
					returnJson = JSONObject.fromObject(dataResult);
					String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
					return StringUtils.replaceBlank(s);
				}
				if (!RegExpValidatorUtils.IsPasswLength(passWord) || !RegExpValidatorUtils.IsPassword(passWord)) {
					dataResult.setFlag("3");
					dataResult.setMessage("密码长度8至18位，且至少包含大小写字母、数字、特殊字符中的3类");
					returnJson = JSONObject.fromObject(dataResult);
					String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
					return StringUtils.replaceBlank(s);
				}
			}

			if (!StringUtils.isEmpty(passWord)) {
				syuser.setPassWord(MD5.MD5_upper32(passWord));
			}

			String syRoleId = json.get("syRoleId") == null ? "" : json.getString("syRoleId");
			if (StringUtils.isEmpty(syRoleId)) {
				syuser.setSyRoleId(Long.valueOf(1L));
			}

			syuser.setPhone(phone);
			syuser.setMobile(mobile);
			syuser.setRemarks(remarks);

			this.syuserService.save(syuser);
			dataResult.setData(syuser);
			dataResult.setMessage("保存成功");

			/**
			 * 保存日志
			 */
			Sylog sylog = new Sylog();
			sylog.setTableid(syuser.getSyUserId());
			sylog.setTablename("syuser");
			sylog.setSyuserid(Long.parseLong(sessionUserid));
			sylog.setLogtype(logType);
			sylog.setLogdate(new Timestamp(new Date().getTime()));
			if (logType.equals(CommonParameter.LOGFLAG_H004001)) {
				sylog.setContent(sessionUserName + "：" + sessionIp + "增加了用户《" + syuser.getUserName() + "》");
			} else if (logType.equals(CommonParameter.LOGFLAG_H004002)) {
				sylog.setContent(sessionUserName + "：" + sessionIp + "修改了用户《" + syuser.getUserName() + "》");
			}
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
	@RequestMapping({ "/addSyuser" })
	public String doAddSyuser(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);
		try {
			String jsonData = RequestUtil.decryptRequestJson(request, sessionData);
			JSONObject json = JSONObject.fromObject(jsonData);
			String sessionIp = sessionData.getIp();
			String sessionUserid = sessionData.getUserId();
			String sessionUserName = sessionData.getUserName();
			String roleType = sessionData.getRoleType();

			String userName = json.get("userName") == null ? "" : json.getString("userName");
			String loginName = json.get("loginName") == null ? "" : json.getString("loginName");
			String passWord = json.get("passWord") == null ? "" : json.getString("passWord");
			String againPassWord = json.get("againPassWord") == null ? "" : json.getString("againPassWord");
			String syRoleId = json.get("syRoleId") == null ? "" : json.getString("syRoleId");
			String email = json.get("email") == null ? "" : json.getString("email");
			String phone = json.get("phone") == null ? "" : json.getString("phone");
			String mobile = json.get("mobile") == null ? "" : json.getString("mobile");
			String remarks = json.get("remarks") == null ? "" : json.getString("remarks");

			boolean returnFlag = false;
			String returnMsg = "";
			if (StringUtils.isEmpty(userName)) {
				returnMsg = "用户名称";
				returnFlag = true;
			}
			if (StringUtils.isEmpty(loginName)) {
				returnMsg = "登录帐号";
				returnFlag = true;
			}
			if (StringUtils.isEmpty(passWord)) {
				returnMsg = "密码";
				returnFlag = true;
			}
			if (StringUtils.isEmpty(againPassWord)) {
				returnMsg = "确认密码";
				returnFlag = true;
			}
			if (StringUtils.isEmpty(syRoleId)) {
				returnMsg = "角色ID";
				returnFlag = true;
			}
			if (returnFlag) {
				dataResult.setFlag("3");
				dataResult.setMessage("参数" + returnMsg + "不能为空");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}

			// 检查登录名是否重复
			if (this.syuserService.checkExistsLoginName(loginName)) {
				dataResult.setFlag("3");
				dataResult.setMessage("登录名已存在");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}

			// 检查密码
			if (!StringUtils.isEmpty(passWord)) {
				// 校验新密码和确认密码是否一致
				if (!passWord.equals(againPassWord)) {
					dataResult.setFlag("3");
					dataResult.setMessage("密码和确认密码不一致");
					returnJson = JSONObject.fromObject(dataResult);
					String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
					return StringUtils.replaceBlank(s);
				}

				if (!"RT001".equals(roleType)) {
					dataResult.setFlag("3");
					dataResult.setMessage("当前角色不具有增加用户权限");
					returnJson = JSONObject.fromObject(dataResult);
					String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
					return StringUtils.replaceBlank(s);
				}

				if (!RegExpValidatorUtils.IsNotInSymbol(passWord)) {
					dataResult.setFlag("3");
					dataResult.setMessage("密码存在不能识别的特殊符号");
					returnJson = JSONObject.fromObject(dataResult);
					String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
					return StringUtils.replaceBlank(s);
				}
				if (!RegExpValidatorUtils.IsPasswLength(passWord) || !RegExpValidatorUtils.IsPassword(passWord)) {
					dataResult.setFlag("3");
					dataResult.setMessage("密码长度8至18位，且至少包含大小写字母、数字、特殊字符中的3类");
					returnJson = JSONObject.fromObject(dataResult);
					String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
					return StringUtils.replaceBlank(s);
				}
			}

			Syuser syuser = new Syuser();
			syuser.setUserName(userName);
			syuser.setLoginName(loginName);
			syuser.setPassWord(MD5.MD5_upper32(passWord));
			syuser.setSyRoleId(Long.parseLong(syRoleId));
			syuser.setEmail(email);
			syuser.setPhone(phone);
			syuser.setMobile(mobile);
			syuser.setRemarks(remarks);

			this.syuserService.save(syuser);
			dataResult.setData(syuser);
			dataResult.setMessage("保存成功");

			/**
			 * 保存日志
			 */
			Sylog sylog = new Sylog();
			sylog.setTableid(syuser.getSyUserId());
			sylog.setTablename("syuser");
			sylog.setSyuserid(Long.parseLong(sessionUserid));
			sylog.setLogtype(CommonParameter.LOGFLAG_H004001);
			sylog.setLogdate(new Timestamp(new Date().getTime()));
			sylog.setContent(sessionUserName + "：" + sessionIp + "增加了用户《" + syuser.getUserName() + "》");
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

	/**
	 * 保存用户基本资料
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping({ "/saveSyuserInfo" })
	public String doSaveSyuserInfo(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);

		try {
			String jsonData = RequestUtil.decryptRequestJson(request, sessionData);
			JSONObject json = JSONObject.fromObject(jsonData);
			String sessionIp = sessionData.getIp();
			String sessionUserid = sessionData.getUserId();
			String sessionUserName = sessionData.getUserName();

			String syUserId = json.get("syUserId") == null ? "" : json.getString("syUserId");
			String userName = json.get("userName") == null ? "" : json.getString("userName");
			String email = json.get("email") == null ? "" : json.getString("email");
			String phone = json.get("phone") == null ? "" : json.getString("phone");
			String mobile = json.get("mobile") == null ? "" : json.getString("mobile");
			String remarks = json.get("remarks") == null ? "" : json.getString("remarks");

			if (StringUtils.isEmpty(syUserId)) {
				dataResult.setFlag("3");
				dataResult.setMessage("主键不能为空");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}
			if (StringUtils.isEmpty(userName)) {
				dataResult.setFlag("3");
				dataResult.setMessage("用户名称不能为空");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}
			Syuser syuser = syuserService.findById(Long.parseLong(syUserId));
			if (null == syuser) {
				dataResult.setFlag("3");
				dataResult.setMessage("没有找到该主键对应的数据");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}

			syuser.setUserName(userName);
			syuser.setEmail(email);
			syuser.setPhone(phone);
			syuser.setMobile(mobile);
			syuser.setRemarks(remarks);

			this.syuserService.save(syuser);
			dataResult.setData(syuser);
			dataResult.setMessage("保存成功");

			/**
			 * 保存日志
			 */
			Sylog sylog = new Sylog();
			sylog.setTableid(syuser.getSyUserId());
			sylog.setTablename("syuser");
			sylog.setSyuserid(Long.parseLong(sessionUserid));
			sylog.setLogtype(CommonParameter.LOGFLAG_H004002);
			sylog.setLogdate(new Timestamp(new Date().getTime()));
			sylog.setContent(sessionUserName + "：" + sessionIp + "修改了用户基本资料《" + syuser.getUserName() + "》");
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

	/**
	 * 修改密码接口
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping({ "/editUserPassWord" })
	public String doEditUserPassWord(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);
		try {
			String jsonData = RequestUtil.decryptRequestJson(request, sessionData);
			JSONObject json = JSONObject.fromObject(jsonData);
			String sessionIp = sessionData.getIp();
			String sessionUserid = sessionData.getUserId();
			String sessionUserName = sessionData.getUserName();

			String syUserId = json.get("syUserId") == null ? "" : json.getString("syUserId");
			String oldPassWord = json.get("oldPassWord") == null ? "" : json.getString("oldPassWord");
			String newPassWord = json.get("newPassWord") == null ? "" : json.getString("newPassWord");
			String againPassWord = json.get("againPassWord") == null ? "" : json.getString("againPassWord");

			boolean returnFlag = false;
			String returnMsg = "";
			if (StringUtils.isEmpty(syUserId)) {
				returnMsg = "主键";
				returnFlag = true;
			}
			if (StringUtils.isEmpty(oldPassWord)) {
				returnMsg = "原密码";
				returnFlag = true;
			}
			if (StringUtils.isEmpty(newPassWord)) {
				returnMsg = "新密码";
				returnFlag = true;
			}
			if (StringUtils.isEmpty(againPassWord)) {
				returnMsg = "确认密码";
				returnFlag = true;
			}
			if (returnFlag) {
				dataResult.setFlag("3");
				dataResult.setMessage("参数" + returnMsg + "不能为空");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}
			Syuser syuser = syuserService.findById(Long.parseLong(syUserId));
			if (null == syuser) {
				dataResult.setFlag("3");
				dataResult.setMessage("没有找到该主键对应的数据");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}

			// 校验原密码
			if (!MD5.MD5_upper32(oldPassWord).equals(syuser.getPassWord())) {
				dataResult.setFlag("3");
				dataResult.setMessage("原密码不正确");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}

			// 校验新密码和确认密码是否一致
			if (!newPassWord.equals(againPassWord)) {
				dataResult.setFlag("3");
				dataResult.setMessage("新密码和确认密码不一致");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}

			// 校验新密码强度
			if (!RegExpValidatorUtils.IsNotInSymbol(newPassWord)) {
				dataResult.setFlag("3");
				dataResult.setMessage("密码存在不能识别的特殊符号");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}
			if (!RegExpValidatorUtils.IsPasswLength(newPassWord) || !RegExpValidatorUtils.IsPassword(newPassWord)) {
				dataResult.setFlag("3");
				dataResult.setMessage("密码长度8至18位，且至少包含大小写字母、数字、特殊字符中的3类");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}

			syuser.setPassWord(MD5.MD5_upper32(newPassWord));
			syuserService.save(syuser);
			dataResult.setMessage("密码修改成功");

			/**
			 * 保存日志
			 */
			Sylog sylog = new Sylog();
			sylog.setTableid(syuser.getSyUserId());
			sylog.setTablename("syuser");
			sylog.setSyuserid(Long.parseLong(sessionUserid));
			sylog.setLogtype(CommonParameter.LOGFLAG_H004002);
			sylog.setLogdate(new Timestamp(new Date().getTime()));
			sylog.setContent(sessionUserName + "：" + sessionIp + "修改了用户密码《" + syuser.getUserName() + "》");
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
	@RequestMapping({ "/queryAllSyrole" })
	public String doQueryAllSyrole(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);
		try {

			dataResult.setData(this.syroleService.findAll());
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
	@RequestMapping({ "/resetPassWord" })
	public String doResetPassWord(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;
		SessionData sessionData = RequestUtil.getRequestSessionUser(request);
		try {
			String jsonData = RequestUtil.decryptRequestJson(request, sessionData);
			JSONObject json = JSONObject.fromObject(jsonData);
			String sessionIp = sessionData.getIp();
			String sessionUserid = sessionData.getUserId();
			String sessionUserName = sessionData.getUserName();
			String roleType = sessionData.getRoleType();

			if (!"RT001".equals(roleType)) {
				dataResult.setFlag("3");
				dataResult.setMessage("当前角色不具有增加用户权限");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}

			String syUserId = json.get("syUserId") == null ? "" : json.getString("syUserId");
			if (StringUtils.isEmpty(syUserId)) {
				dataResult.setFlag("3");
				dataResult.setMessage("主键不能为空");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}
			Syuser syuser = syuserService.findById(Long.parseLong(syUserId));
			if (null == syuser) {
				dataResult.setFlag("3");
				dataResult.setMessage("没有找到该主键对应的数据");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), sessionData.getSessionKey(), sessionData.getSessionIv());
				return StringUtils.replaceBlank(s);
			}
			syuser.setPassWord(MD5.MD5_upper32("fstjj123#"));
			syuserService.save(syuser);

			/**
			 * 保存日志
			 */
			Sylog sylog = new Sylog();
			sylog.setTableid(syuser.getSyUserId());
			sylog.setTablename("syuser");
			sylog.setSyuserid(Long.parseLong(sessionUserid));
			sylog.setLogtype(CommonParameter.LOGFLAG_H004002);
			sylog.setLogdate(new Timestamp(new Date().getTime()));
			sylog.setContent(sessionUserName + "：" + sessionIp + "重置了用户密码《" + syuser.getUserName() + "》");
			sylogService.save(sylog);

			dataResult.setMessage("重置成功");
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

	public static void main(String[] args) {
		System.out.println(MD5.MD5_upper32("fstjj123#"));
		// C0F0BC0417016E5CACE83A704D2E57AA
	}
}