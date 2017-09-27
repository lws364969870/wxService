package com.lws.web.controller;

import com.lws.domain.entity.Syconfig;
import com.lws.domain.model.wechat.AccessToken;
import com.lws.domain.model.wechat.CheckModel;
import com.lws.domain.service.SyconfigService;
import com.lws.domain.service.WechatService;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({ "/wechat" })
public class WechatController {

	@Resource
	private WechatService wechatService;

	@Resource
	private SyconfigService syconfigService;

	@ResponseBody
	@RequestMapping(value = { "/check" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET }, produces = { "text/plain" })
	public String validate(CheckModel tokenModel) throws ParseException, IOException {
		List list = new ArrayList();
		String wxToken = "error";
		try {
			Syconfig syconfig = new Syconfig();
			syconfig.setConfigKey("wxToken");
			list = this.syconfigService.queryForCondition(syconfig);
			if (list.size() > 0) {
				syconfig = (Syconfig) list.get(0);
				wxToken = syconfig.getConfigValue();
				return this.wechatService.validate(wxToken, tokenModel);
			}
			return wxToken;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wxToken;
	}

	@ResponseBody
	@RequestMapping({ "/getAccessToken" })
	public AccessToken doGetAccessToken() {
		try {
			String userDir = System.getProperty("user.dir");
			System.out.println();
			return this.wechatService.getAccessToken();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}