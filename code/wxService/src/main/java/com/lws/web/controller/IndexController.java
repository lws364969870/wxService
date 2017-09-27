package com.lws.web.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lws.domain.entity.Syuser;
import com.lws.domain.entity.SyuserV;
import com.lws.domain.model.DataResult;
import com.lws.domain.model.SessionData;
import com.lws.domain.service.SylogService;
import com.lws.domain.service.SyuserService;
import com.lws.domain.utils.CommonParameter;
import com.lws.domain.utils.CommonUtils;
import com.lws.domain.utils.StringUtils;
import com.lws.domain.utils.properties.ApplicationsPropertiesUtils;
import com.lws.domain.utils.pwd.AES;
import com.lws.domain.utils.pwd.MD5;
import com.lws.domain.utils.request.CryptUtil;
import com.lws.domain.utils.request.RequestUtil;

@Controller
public class IndexController {

	@Resource
	SyuserService syuserService;
	@Resource
	SylogService sylogService;

	@RequestMapping({ "/" })
	public String doLogin(Model model) {
		return "/login";
	}

	@RequestMapping({ "/login" })
	public String doLogin(HttpServletRequest request, HttpServletResponse response) {
		return "/login";
	}

	/**
	 * 检查登录
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping({ "/checklogin" })
	public String checklogin(HttpServletRequest request, HttpServletResponse response) {
		DataResult dataResult = new DataResult();
		JSONObject returnJson = null;

		try {
			String jsonData = RequestUtil.decryptRequestJson(request, CryptUtil.defaultSessionKey, CryptUtil.defaultSessionIv);
			JSONObject json = JSONObject.fromObject(jsonData);

			String loginName = json.get("loginName") == null ? "" : json.getString("loginName");
			String passWord = json.get("passWord") == null ? "" : json.getString("passWord");
			if (StringUtils.isEmpty(loginName) || StringUtils.isEmpty(passWord)) {
				dataResult.setFlag("3");
				dataResult.setMessage("录入信息不能为空");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), CryptUtil.defaultSessionKey, CryptUtil.defaultSessionIv);
				return StringUtils.replaceBlank(s);
			}

			List userList = this.syuserService.getAccount(loginName);
			if (null == userList || userList.size() == 0) {
				dataResult.setFlag("3");
				dataResult.setMessage("用户名不存在");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), CryptUtil.defaultSessionKey, CryptUtil.defaultSessionIv);
				return StringUtils.replaceBlank(s);
			}

			SyuserV currentAccount = (SyuserV) userList.get(0);

			Timestamp errorTime = currentAccount.getErrortime();
			Timestamp nowTime = new Timestamp(new Date().getTime());
			if (null != errorTime && errorTime.after(nowTime)) {
				dataResult.setFlag("3");
				dataResult.setMessage("因输入错误密码次数超过上限，" + CommonUtils.getTime(errorTime.getTime() - nowTime.getTime()) + "后解除登录限制");
				returnJson = JSONObject.fromObject(dataResult);
				String s = AES.Encrypt(returnJson.toString(), CryptUtil.defaultSessionKey, CryptUtil.defaultSessionIv);
				return StringUtils.replaceBlank(s);
			}

			Syuser queryUser = new Syuser();
			queryUser.setLoginName(loginName);
			List syuserList = syuserService.findByExample(queryUser);
			Syuser syuser = (Syuser) syuserList.get(0);

			if (!MD5.MD5_upper32(passWord).equals(currentAccount.getPassWord())) {
				String accountErrorCountStr = ApplicationsPropertiesUtils.getValue("account.errorcount");
				String accountErrorTimeStr = ApplicationsPropertiesUtils.getValue("account.errortime");
				// 错误限制次数
				int accountErrorCount = Integer.parseInt(accountErrorCountStr);
				// 错误限制时间
				long accountErrorTime = Integer.parseInt(accountErrorTimeStr) * 60 * 1000;

				int errorCount = 0;
				if (null != syuser.getErrorcount()) {
					errorCount = Integer.parseInt(syuser.getErrorcount().toString());
				}

				if (errorCount < accountErrorCount) {
					syuser.setErrorcount(errorCount + 1l);
					syuserService.save(syuser);
					dataResult.setFlag("3");
					dataResult.setMessage("密码错误,你还有" + (accountErrorCount - errorCount) + "次尝试机会");
					returnJson = JSONObject.fromObject(dataResult);
					String s = AES.Encrypt(returnJson.toString(), CryptUtil.defaultSessionKey, CryptUtil.defaultSessionIv);
					return StringUtils.replaceBlank(s);
				} else {
					syuser.setErrorcount(0l);
					syuser.setErrortime(new Timestamp(new Date().getTime() + accountErrorTime));
					syuserService.save(syuser);
					dataResult.setFlag("3");
					dataResult.setMessage("因输入错误密码次数超过上限，" + CommonUtils.getTime(accountErrorTime) + "后解除登录限制");
					// 保存日志
					sylogService.save(currentAccount.getSyUserId(), "syuser", currentAccount.getSyUserId(), CommonParameter.LOGFLAG_H004004, currentAccount.getUserName() + "：" + getIpAddr(request)
							+ "因输入错误密码次数超过上限,帐号被限制登录！");
					returnJson = JSONObject.fromObject(dataResult);
					String s = AES.Encrypt(returnJson.toString(), CryptUtil.defaultSessionKey, CryptUtil.defaultSessionIv);
					return StringUtils.replaceBlank(s);
				}
			}

			SessionData sessionData = new SessionData();
			sessionData.setUserId(currentAccount.getSyUserId().toString());
			sessionData.setRoleName(currentAccount.getRoleName());
			sessionData.setRoleType(currentAccount.getRoleType());
			sessionData.setLoginName(currentAccount.getLoginName());
			sessionData.setUserName(currentAccount.getUserName());
			sessionData.setIp(getIpAddr(request));
			sessionData.setSessionKey(CryptUtil.createKeyStr());
			sessionData.setSessionIv(CryptUtil.createIVStr());

			request.getSession().setAttribute("user", sessionData);

			// 保存日志
			sylogService.save(currentAccount.getSyUserId(), "syuser", currentAccount.getSyUserId(), CommonParameter.LOGFLAG_H004004, currentAccount.getUserName() + "：" + getIpAddr(request) + "登录");

			returnJson = JSONObject.fromObject(dataResult);
			String s = AES.Encrypt(returnJson.toString(), CryptUtil.defaultSessionKey, CryptUtil.defaultSessionIv);
			return StringUtils.replaceBlank(s);
		} catch (Exception e) {
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

	@RequestMapping({ "/loginOut" })
	public String doLoginOut(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().invalidate();
		return "login";
	}

	/**
	 * 获取IP
	 * @param request
	 * @return
	 */
	public String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if ((StringUtils.isEmpty(ip)) || ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if ((StringUtils.isEmpty(ip)) || ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if ((StringUtils.isEmpty(ip)) || ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	@RequestMapping({ "/main" })
	public String doMain(HttpServletRequest request, HttpServletResponse response) {
		return "/main";
	}

	@ResponseBody
	@RequestMapping({ "/test" })
	public String doTest(HttpServletRequest request, HttpServletResponse response) {
		StringBuffer url = request.getRequestURL();
		return StringUtils.getServerRequestURL(url.toString());
	}
}