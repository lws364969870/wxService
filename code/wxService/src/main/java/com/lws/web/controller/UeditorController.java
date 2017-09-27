package com.lws.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.lws.domain.entity.Attached;
import com.lws.domain.service.AttachedService;
import com.lws.domain.service.SyconfigService;
import com.lws.domain.service.WechatService;
import com.lws.domain.utils.RandomUtils;
import com.lws.domain.utils.StringUtils;
import com.lws.domain.utils.properties.ApplicationsPropertiesUtils;

@Controller
@RequestMapping({ "/resource/upload" })
public class UeditorController {

	@Resource
	WechatService wechatService;

	@Resource
	AttachedService attachedService;

	@Resource
	SyconfigService syconfigService;

	@ResponseBody
	@RequestMapping({ "/images" })
	public Map<String, Object> images(@RequestParam MultipartFile upfile, HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		Map params = new HashMap();
		try {
			String basePath = ApplicationsPropertiesUtils.getValue("uploading.url");

			String visitURL = ApplicationsPropertiesUtils.getValue("visit.url");

			String ext = StringUtils.getExt(upfile.getOriginalFilename());
			String fileName = String.valueOf(System.currentTimeMillis()).concat("_").concat(RandomUtils.getRandom(6)).concat(".").concat(ext);
			long size = upfile.getSize();
			StringBuilder filePath = new StringBuilder();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

			basePath = basePath + "/" + sdf.format(new Date()) + "/";
			visitURL = visitURL + sdf.format(new Date()) + "/";

			filePath.append(basePath).append("/").append(fileName);
			visitURL = visitURL.concat(fileName);

			File f = new File(filePath.toString());

			String absoluteURL = f.getAbsolutePath();
			System.out.println(absoluteURL);

			System.out.println(visitURL);

			String networdURL = ApplicationsPropertiesUtils.getValue("locationhost.url") + visitURL;

			Map map = new HashMap();
			try {
				if (!(f.exists())) {
					f.getParentFile().mkdirs();
					System.out.println(filePath.toString());
					OutputStream out = new FileOutputStream(f);
					FileCopyUtils.copy(upfile.getInputStream(), out);
				}
				Attached attached = new Attached();
				attached.setAbsoluteURL(absoluteURL);
				attached.setNetwordURL(networdURL);
				attached.setVisitURL(visitURL);

				String netWork = this.syconfigService.getValueByKey("netWork");
				if (StringUtils.isEmpty(netWork)) {
					netWork = "N";
				}
				if ("Y".equals(netWork)) {
					map = this.wechatService.putWechatUrlPhoto(filePath.toString());
					if (map.get("url") != null) {
						String wechatURL = map.get("url").toString();
						attached.setWechatURL(wechatURL);
					}
				}
				this.attachedService.save(attached);

				String photoShow = ApplicationsPropertiesUtils.getValue("photo.show");
				if (("wechat".equals(photoShow)) && ("Y".equals(netWork)))
					params.put("url", map.get("url").toString());
				else {
					params.put("url", networdURL);
				}
				params.put("state", "SUCCESS");
				params.put("size", Long.valueOf(upfile.getSize()));
				params.put("original", fileName);
				params.put("type", upfile.getContentType());
			} catch (Exception e) {
				if (f.exists()) {
					f.delete();
				}
				params.put("state", "ERROR");
				return params;
			}
		} catch (Exception e) {
			params.put("state", "ERROR");
		}
		return params;
	}

	public static void main(String[] args) {
		System.out.println(System.getProperty("user.dir"));
	}
}