package com.lws.domain.service;

import com.lws.domain.dao.SyconfigDAO;
import com.lws.domain.entity.Subscribe;
import com.lws.domain.entity.Syconfig;
import com.lws.domain.model.wechat.AccessToken;
import com.lws.domain.model.wechat.CheckModel;
import com.lws.domain.utils.StringUtils;
import com.lws.domain.utils.properties.WechatErrorCodePropertiesUtils;
import com.lws.domain.utils.wechat.EncoderHandler;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WechatService {

	@Resource
	SyconfigDAO syconfigDAO;

	@Transactional
	public String validate(String wxToken, CheckModel tokenModel) {
		String signature = tokenModel.getSignature();
		Long timestamp = tokenModel.getTimestamp();
		Long nonce = tokenModel.getNonce();
		String echostr = tokenModel.getEchostr();
		if (signature != null)
			if ((((timestamp != null) ? 1 : 0) & ((nonce != null) ? 1 : 0)) != 0) {
				String[] str = { wxToken, timestamp.toString(), nonce.toString() };
				Arrays.sort(str);
				String bigStr = str[0] + str[1] + str[2];

				String digest = EncoderHandler.encode("SHA1", bigStr).toLowerCase();

				if (digest.equals(signature)) {
					return echostr;
				}
			}
		return "error";
	}

	public AccessToken getAccessToken() throws Exception {
		AccessToken token = new AccessToken();
		String access_token = this.syconfigDAO.getValueByKey("access_token");
		String expires_in = this.syconfigDAO.getValueByKey("expires_in");
		if ((access_token == null) || (access_token.isEmpty()) || (expires_in == null) || (expires_in.isEmpty())) {
			return getWechatAccessToken();
		}

		long expires = Long.parseLong(expires_in);
		Long time = Long.valueOf(new Date().getTime());
		if (time.longValue() < expires) {
			token.setAccess_token(access_token);
			token.setExpires_in(expires);
			return token;
		}

		return getWechatAccessToken();
	}

	public AccessToken getWechatAccessToken() throws Exception {
		AccessToken token = new AccessToken();
		String appID = this.syconfigDAO.getValueByKey("appID");
		String appsecret = this.syconfigDAO.getValueByKey("appsecret");
		if ((appID == null) || (appID.isEmpty()) || (appsecret == null) || (appsecret.isEmpty())) {
			throw new Exception("系统查询不到appID和appsecret对应的健值");
		}

		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appID + "&secret=" + appsecret;
		URL getUrl = new URL(url);
		HttpsURLConnection https = (HttpsURLConnection) getUrl.openConnection();
		https.setRequestMethod("GET");
		https.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		https.setDoOutput(true);
		https.setDoInput(true);
		https.connect();
		InputStream is = https.getInputStream();
		int size = is.available();
		byte[] b = new byte[size];
		is.read(b);
		String message = new String(b, "UTF-8");
		JSONObject json = JSONObject.fromObject(message);

		String i = json.getString("access_token");
		Long time = Long.valueOf(new Date().getTime() + new Integer(json.getString("expires_in")).intValue() * 1000);
		token.setAccess_token(json.getString("access_token"));
		token.setExpires_in(time.longValue());

		List list1 = this.syconfigDAO.findSyconfigByKey("access_token");
		List list2 = this.syconfigDAO.findSyconfigByKey("expires_in");
		if ((list1.size() < 1) || (list2.size() < 1)) {
			throw new Exception("系统查询不到access_token和expires_in对应的健值");
		}
		Syconfig syconfig1 = (Syconfig) list1.get(0);
		Syconfig syconfig2 = (Syconfig) list2.get(0);

		syconfig1.setConfigValue(json.getString("access_token"));
		syconfig2.setConfigValue(time.toString());

		this.syconfigDAO.save(syconfig1);
		this.syconfigDAO.save(syconfig2);
		return token;
	}

	/**
	 * 上传缩略图获取缩略图ID
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public Map putWechatThumbPhoto(String filePath) throws Exception {
		Map map = new HashMap();
		AccessToken accessToken = getAccessToken();
		if ((accessToken.getAccess_token() == null) || (accessToken.getAccess_token().length() < 1)) {
			throw new Exception("获取accessToken失败");
		}
		String type = "thumb";
		String token = accessToken.getAccess_token();

		String wechatURL = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=" + token + "&type=" + type;

		map = sendPhoto(wechatURL, filePath);
		if (map.get("mediaId") != null) {
			map.put("thumbMediaId", ((String) map.get("mediaId")).toString());
		}
		return map;
	}

	/**
	 * 上传图片获取URL
	 */
	public Map putWechatUrlPhoto(String filePath) throws Exception {
		Map map = new HashMap();
		AccessToken accessToken = getAccessToken();
		if ((accessToken.getAccess_token() == null) || (accessToken.getAccess_token().length() < 1)) {
			throw new Exception("获取accessToken失败");
		}
		String token = accessToken.getAccess_token();
		String wechatURL = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=" + accessToken.getAccess_token();
		map = sendPhoto(wechatURL, filePath);
		return map;
	}

	public Map sendPhoto(String wechatURL, String filePath) throws Exception {
		Map map = new HashMap();

		String result = null;
		File file = new File(filePath);
		if ((!(file.exists())) || (!(file.isFile()))) {
			throw new IOException("文件不存在");
		}

		URL urlObj = new URL(wechatURL);
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);

		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");

		String BOUNDARY = "----------" + System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

		StringBuilder sb = new StringBuilder();
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition: form-data;name=\"media\";filelength=\"" + file.length() + "\";filename=\"" + file.getName() + "\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");
		byte[] head = sb.toString().getBytes("utf-8");

		OutputStream out = new DataOutputStream(con.getOutputStream());

		out.write(head);

		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while ((bytes = in.read(bufferOut)) != -1) {
			out.write(bufferOut, 0, bytes);
		}
		in.close();
		byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");
		out.write(foot);
		out.flush();
		out.close();
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			if (result == null) {
				result = buffer.toString();
			}
		} catch (IOException e) {
			System.out.println("发送POST请求出现异常！" + e);
			e.printStackTrace();
			throw new IOException("发送POST请求出现异常！");
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		JSONObject json = JSONObject.fromObject(result);

		if (json.get("errcode") != null) {
			String errorcode = json.getString("errcode");
			if (!("0".equals(errorcode))) {
				String errmsg = WechatErrorCodePropertiesUtils.getValue("ERROR_" + errorcode);
				throw new Exception(errorcode + ":" + errmsg);
			}
			if ("40001".equals(errorcode)) {
				getWechatAccessToken();
				throw new Exception(errorcode + ":" + "因微信请求超时，上传失败，请继续尝试！");
			}
		}

		if (json.get("url") != null) {
			String urll = json.getString("url");
			map.put("url", urll);
		}
		if (json.get("media_id") != null) {
			String mediaId = json.getString("media_id");
			map.put("mediaId", mediaId);
		}
		if (json.get("thumb_media_id") != null) {
			String thumbMediaId = json.getString("thumb_media_id");
			map.put("thumbMediaId", thumbMediaId);
		}

		return map;
	}

	public Map sendPhotoTemp(String wechatURL, String filePath) throws Exception {
		Map map = new HashMap();

		String result = null;
		File file = new File(filePath);
		if ((!(file.exists())) || (!(file.isFile()))) {
			throw new IOException("文件不存在");
		}

		URL urlObj = new URL(wechatURL);
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);

		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");

		String BOUNDARY = "----------" + System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

		StringBuilder sb = new StringBuilder();
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");
		byte[] head = sb.toString().getBytes("utf-8");

		OutputStream out = new DataOutputStream(con.getOutputStream());

		out.write(head);

		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while ((bytes = in.read(bufferOut)) != -1) {
			out.write(bufferOut, 0, bytes);
		}
		in.close();

		byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");
		out.write(foot);
		out.flush();
		out.close();
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			if (result == null) {
				result = buffer.toString();
			}
		} catch (IOException e) {
			System.out.println("发送POST请求出现异常！" + e);
			e.printStackTrace();
			throw new IOException("发送POST请求出现异常！");
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		JSONObject json = JSONObject.fromObject(result);

		if (json.get("errcode") != null) {
			String errorcode = json.getString("errcode");
			if (!("0".equals(errorcode))) {
				String errmsg = WechatErrorCodePropertiesUtils.getValue("ERROR_" + errorcode);
				throw new Exception(errorcode + ":" + errmsg);
			}
			if ("40001".equals(errorcode)) {
				getWechatAccessToken();
				throw new Exception(errorcode + ":" + "因微信请求超时，上传失败，请继续尝试！");
			}
		}

		if (json.get("url") != null) {
			String urll = json.getString("url");
			map.put("url", urll);
		}
		if (json.get("media_id") != null) {
			String mediaId = json.getString("media_id");
			map.put("mediaId", mediaId);
		}
		if (json.get("thumb_media_id") != null) {
			String thumbMediaId = json.getString("thumb_media_id");
			map.put("thumbMediaId", thumbMediaId);
		}

		return map;
	}

	/**
	 * 单独推送
	 */
	public Map examineMaterial(String materialId, String OpenID) throws Exception {
		Map map = new HashMap();
		AccessToken accessToken = getAccessToken();
		if ((accessToken.getAccess_token() == null) || (accessToken.getAccess_token().length() < 1)) {
			throw new Exception("获取accessToken失败");
		}

		String token = accessToken.getAccess_token();
		String wechatURL = "https://api.weixin.qq.com/cgi-bin/message/mass/preview?access_token=" + token;
		String jsondata = "{\"touser\":\"" + OpenID + "\",\"mpnews\":{\"media_id\":\"" + materialId + "\"},\"msgtype\":\"mpnews\"}";
		return sendNews(wechatURL, jsondata);
	}
	
	/**
	 * 群发推送
	 * @param jsondata
	 * @return
	 * @throws Exception
	 */
	public Map massMaterial(String mediaId) throws Exception {
		AccessToken accessToken = getAccessToken();
		if ((accessToken.getAccess_token() == null) || (accessToken.getAccess_token().length() < 1)) {
			throw new Exception("获取accessToken失败");
		}

		String token = accessToken.getAccess_token();
		String wechatURL = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=" + token;
		String jsondata = "{\"filter\":{\"is_to_all\":true},\"mpnews\":{\"media_id\":\""+mediaId+"\"},\"msgtype\":\"mpnews\",\"send_ignore_reprint\":0}";
		
		return sendNews(wechatURL, jsondata);
	}

	/**
	 * 创建素材
	 */
	public Map createWechatMaterial(String jsondata) throws Exception {
		AccessToken accessToken = getAccessToken();
		if ((accessToken.getAccess_token() == null) || (accessToken.getAccess_token().length() < 1)) {
			throw new Exception("获取accessToken失败");
		}

		String token = accessToken.getAccess_token();

		String wechatURL = "https://api.weixin.qq.com/cgi-bin/material/add_news?access_token=" + token;

		return sendNews(wechatURL, jsondata);
	}

	/**
	 * 修改素材
	 */
	public Map updateWechatMaterial(String jsondata) throws Exception {
		AccessToken accessToken = getAccessToken();
		if ((accessToken.getAccess_token() == null) || (accessToken.getAccess_token().length() < 1)) {
			throw new Exception("获取accessToken失败");
		}
		String token = accessToken.getAccess_token();

		String wechatURL = "https://api.weixin.qq.com/cgi-bin/material/update_news?access_token=" + token;
		return sendNews(wechatURL, jsondata);
	}

	/**
	 * 删除素材
	 */
	public Map deleteWechatMaterial(String jsondata) throws Exception {
		AccessToken accessToken = getAccessToken();
		if ((accessToken.getAccess_token() == null) || (accessToken.getAccess_token().length() < 1)) {
			throw new Exception("获取accessToken失败");
		}
		String token = accessToken.getAccess_token();

		String wechatURL = "https://api.weixin.qq.com/cgi-bin/material/del_material?access_token=" + token;

		return sendNews(wechatURL, jsondata);
	}

	
	public Map sendNews(String posturl, String jsondata) throws Exception {
		Map map = new HashMap();

		HttpClient httpClient = new HttpClient();
		PostMethod method = new PostMethod(posturl);
		if ((jsondata != null) && (!(jsondata.trim().equals("")))) {
			RequestEntity requestEntity = new StringRequestEntity(jsondata, "text/xml", "UTF-8");
			method.setRequestEntity(requestEntity);
		}
		method.releaseConnection();
		httpClient.executeMethod(method);
		String responseContent = method.getResponseBodyAsString();

		JSONObject json = JSONObject.fromObject(responseContent);

		if (json.get("errcode") != null) {
			String errorcode = json.getString("errcode");
			if ("40001".equals(errorcode)) {
				getWechatAccessToken();
				throw new Exception(errorcode + ":" + "因微信请求超时，上传失败，请继续尝试！");
			}
			if ("0".equals(errorcode)) {
				map.put("errcode", "0");
			}
			if (!"0".equals(errorcode)) {
				String errmsg = WechatErrorCodePropertiesUtils.getValue("ERROR_" + errorcode);
				throw new Exception(errorcode + ":" + errmsg);
			}
		}
		if (json.get("media_id") != null) {
			String mediaId = json.getString("media_id");
			map.put("mediaId", mediaId);
		}
		if (json.get("msg_id") != null) {
			String msgId = json.getString("msg_id");
			map.put("msgId", msgId);
		}

		return map;
	}

	/**
	 * 刷新粉丝
	 * @return
	 * @throws Exception
	 */
	public List refreshSubscribe() throws Exception {

		List list = new ArrayList();
		AccessToken accessToken = getAccessToken();
		if ((accessToken.getAccess_token() == null) || (accessToken.getAccess_token().length() < 1)) {
			throw new Exception("获取accessToken失败");
		}

		String token = accessToken.getAccess_token();

		String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=" + token;
		String nextUrl = url;
		boolean flag = false;
		Map<String, String> map = new HashMap();

		while (!(flag)) {
			String jsondata = sendGet(nextUrl);
			System.out.println(jsondata);
			JSONObject json = JSONObject.fromObject(jsondata);
			if (json.get("data") != null) {
				JSONObject datastr = JSONObject.fromObject(json.get("data"));
				String openidArr = datastr.get("openid").toString();
				openidArr = openidArr.replace("\"", "");
				openidArr = openidArr.replace("[", "");
				openidArr = openidArr.replace("]", "");

				String[] s = openidArr.split(",");
				for (int i = 0; i < s.length; ++i) {
					map.put(s[i], s[i]);
				}
			}
			if (json.get("nextOpenid") != null) {
				String nextOpenid = json.get("nextOpenid").toString();
				nextUrl = url + "&next_openid=" + nextOpenid;
			} else {
				flag = true;
			}

		}

		url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + token + "&openid=";
		nextUrl = "";
		for (String key : map.keySet()) {
			nextUrl = url + key + "&lang=zh_CN";
			String jsondata = sendGet(nextUrl);
			JSONObject json = JSONObject.fromObject(jsondata);
			if ((json.get("remark") == null) || (StringUtils.isEmpty(json.getString("remark"))))
				continue;
			Subscribe subscribe = new Subscribe();

			subscribe.setRemark(json.get("remark").toString());

			if (json.get("openid") != null) {
				subscribe.setOpenid(json.get("openid").toString());
			}

			if (json.get("nickname") != null) {
				subscribe.setNickname(json.get("nickname").toString());
			}

			if (json.get("headimgurl") != null) {
				subscribe.setHeadimgurl(json.get("headimgurl").toString());
			}

			if (json.get("sex") != null) {
				String sex = json.get("sex").toString();
				if (sex.equals("0"))
					subscribe.setSex("未知");
				else if (sex.equals("1"))
					subscribe.setSex("男");
				else if (sex.equals("2")) {
					subscribe.setSex("女");
				}
			}
			subscribe.setIsShow("Y");
			list.add(subscribe);
		}

		return list;
	}

	/**
	 * 发送get请求
	 */
	public String sendGet(String urlNameString) throws Exception {
		String result = "";
		BufferedReader in = null;
		try {
			String str1;
			String line;
			URL realUrl = new URL(urlNameString);

			URLConnection connection = realUrl.openConnection();

			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

			connection.connect();

			Map map = connection.getHeaderFields();

			for (Iterator localIterator = map.keySet().iterator(); localIterator.hasNext(); str1 = (String) localIterator.next())
				;
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

			while ((line = in.readLine()) != null)
				result = result + line;
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}
}