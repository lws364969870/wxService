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

import com.lws.domain.model.DataResult;
import com.lws.domain.service.SyconfigService;
import com.lws.domain.service.WechatService;
import com.lws.domain.utils.RandomUtils;
import com.lws.domain.utils.StringUtils;
import com.lws.domain.utils.properties.ApplicationsPropertiesUtils;

@Controller
public class FileController {

	@Resource
	WechatService wechatService;

	@Resource
	SyconfigService syconfigService;

	@ResponseBody
	@RequestMapping(value="/upWxPhoto")
	public DataResult doUploadPic(@RequestParam("upWxPhoto") MultipartFile upWxPhoto, HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		DataResult dataResult = new DataResult();
		Map map = new HashMap();
		try {
			OutputStream out;
			String basePath = ApplicationsPropertiesUtils.getValue("uploading.url");

			String visitURL = ApplicationsPropertiesUtils.getValue("visit.url");

			String ext = StringUtils.getExt(upWxPhoto.getOriginalFilename());
			String fileName = String.valueOf(System.currentTimeMillis()).concat("_").concat(RandomUtils.getRandom(6)).concat(".").concat(ext);
			long size = upWxPhoto.getSize();
			StringBuilder filePath = new StringBuilder();

			if (!("jpg".equals(ext))) {
				dataResult.setFlag("3");
				dataResult.setMessage("上传失败，只能上传jpg格式的图片");
				return dataResult;
			}
			if (65536L <= size) {
				dataResult.setFlag("3");
				dataResult.setMessage("图片不能大于64Kb");
				return dataResult;
			}

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

			basePath = basePath + "/" + sdf.format(new Date()) + "/";
			visitURL = visitURL + sdf.format(new Date()) + "/";

			filePath.append(basePath).append("/").append(fileName);
			visitURL = visitURL.concat(fileName);

			File f = new File(filePath.toString());

			String absoluteURL = f.getAbsolutePath();
			System.out.println(absoluteURL);

			System.out.println(visitURL);

			String networdURL = StringUtils.getServerRequestURL(request.getRequestURL().toString()) + visitURL;

			String netWork = this.syconfigService.getValueByKey("netWork");
			if (StringUtils.isEmpty(netWork)) {
				netWork = "N";
			}

			if ("Y".equals(netWork)) {
				try {
					if (!(f.exists())) {
						f.getParentFile().mkdirs();
						System.out.println(filePath.toString());
						out = new FileOutputStream(f);
						FileCopyUtils.copy(upWxPhoto.getInputStream(), out);
					}

					map = this.wechatService.putWechatThumbPhoto(filePath.toString());
				} catch (Exception e) {
					if (f.exists()) {
						f.delete();
					}
					dataResult.setFlag("3");
					dataResult.setMessage("上传失败，" + e.getMessage());
					return dataResult;
				}
			}
			if (!(f.exists())) {
				f.getParentFile().mkdirs();
				out = new FileOutputStream(f);
				FileCopyUtils.copy(upWxPhoto.getInputStream(), out);
			}

			map.put("networdURL", networdURL);
			dataResult.setData(map);
			dataResult.setMessage("上传成功");
			return dataResult;
		} catch (Exception e) {
			dataResult.setFlag("3");
			dataResult.setMessage("上传失败，" + e.getMessage());
		}
		return dataResult;
	}
}