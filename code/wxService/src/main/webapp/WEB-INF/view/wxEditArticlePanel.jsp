<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>  
<%@page import="com.lws.domain.utils.request.CryptUtil"%>
 <%  
    String path = request.getContextPath();  
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";  
    String defaulKey =CryptUtil.getDefaultSessionKey();
	String defaulIv =CryptUtil.getDefaultSessionIv();
 %>  
<!DOCTYPE html>
<html>

	<head>
		<meta charset="utf-8">
		<title>佛山市统计</title>
		<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="apple-mobile-web-app-status-bar-style" content="black">

		<!--标准mui.css-->
		<link rel="stylesheet" href="static/app/css/mui.min.css">
		<style type="text/css">
			body {
				-webkit-touch-callout: none;
				font-family: -apple-system-font, "Helvetica Neue", "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", sans-serif;
				background-color: #f3f3f3;
				line-height: inherit;
			}
			
			.rich_media_area_primary {
				position: relative;
				padding: 20px 15px 15px;
				background-color: #fff;
			}
			
			.rich_media_title {
				margin-bottom: 10px;
				line-height: 1.4;
				font-weight: 400;
				font-size: 24px;
			}
			
			.rich_media_meta_list em {
				font-style: normal;
			}
			
			.rich_media_meta_text {
				color: #8c8c8c;
			}
			
			.rich_media_meta {
				display: inline-block;
				vertical-align: middle;
				margin-right: 8px;
				margin-bottom: 10px;
				font-size: 16px;
			}
			
			.wx_content * {
			    max-width: 100%!important;
			    box-sizing: border-box!important;
			    -webkit-box-sizing: border-box!important;
			    word-wrap: break-word!important;
			}
			
			html,body{
				background:#ffffff center center no-repeat;    
			}
		</style>
	</head>

	<body>
		<header class="mui-bar mui-bar-nav">
			<a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
			<h1 class="mui-title">最新消息</h1>
		</header>
		<div class="mui-content">
			<div class="rich_media_area_primary">
				<h2 class="rich_media_title title"></h2>
				<div class="rich_media_meta_list">
					<em class="rich_media_meta rich_media_meta_text createDate"></em>
					<span class="rich_media_meta rich_media_meta_text author"></span>
				</div>

				<div class="wx_content">
					
				</div>
			</div>
		</div>
		<script src="static/app/js/jquery-1.12.3.min.js" type="text/javascript" charset="utf-8"></script>
		<script type="text/javascript" src="static/js/aes.js"></script>
		<script type="text/javascript" src="static/js/tf_aes.js"></script>
		<script src="static/app/js/publicAjax.js" type="text/javascript" charset="utf-8"></script>
		<script src="static/app/js/mui.min.js"></script>
		<script src="static/js/public.js"></script>
		<script type="text/javascript">
			var defaulKey = new Object();	 
				defaulKey.key  = "<%=defaulKey%>"
				defaulKey.iv = "<%=defaulIv%>"
			var init = function(){
				var url = window.location.search;
				var loc = url.substring(url.lastIndexOf('=')+1, url.length);
				jsonDataList.url = "findArticleById";
				jsonDataList.data = encryptionFun({articleId:loc},defaulKey.key,defaulKey.iv);
				jsonDataList.dataType = "text";
				var res = showAjaxFun(jsonDataList,"");
					res =  decryptionFun(res,defaulKey.key,defaulKey.iv);
				if(res != null){
					$(".title").html(res.data.title);
					$(".createDate").html(conversionTime(res.data.createDate.time));
					$(".author").html(res.data.author);
					$(".wx_content").html(res.data.content);
				}
			}
			
			window.onload = init();
		</script>
	</body>
</html>