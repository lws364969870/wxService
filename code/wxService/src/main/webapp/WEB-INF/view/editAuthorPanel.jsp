<%@page import="com.lws.domain.model.SessionData"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
SessionData sessionData = (SessionData)session.getAttribute("user");
String sessionRoleType = sessionData.getRoleType();
String sessionLoginName = sessionData.getLoginName();
String sessionRoleName = sessionData.getRoleName();
String sessionKey = sessionData.getSessionKey();
String sessionIv = sessionData.getSessionIv();
%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<!--[if lt IE 9]>
<script type="text/javascript" src="lib/html5shiv.js"></script>
<script type="text/javascript" src="lib/respond.min.js"></script>
<![endif]-->

<link rel="stylesheet" type="text/css" href="static/js/h-ui/css/H-ui.min.css" />
<link rel="stylesheet" type="text/css" href="static/js/h-ui.admin/css/H-ui.admin.css" />
<link rel="stylesheet" type="text/css" href="static/js/pc/Hui-iconfont/1.0.8/iconfont.css" />
<link rel="stylesheet" type="text/css" href="static/js/h-ui.admin/skin/default/skin.css" id="skin" />
<link rel="stylesheet" type="text/css" href="static/js/h-ui.admin/css/style.css" />
<!--[if IE 6]>
<script type="text/javascript" src="lib/DD_belatedPNG_0.0.8a-min.js" ></script>
<script>DD_belatedPNG.fix('*');</script>
<![endif]-->
<title>佛山市统计后台登录</title>
<style type="text/css">
	.red{color: red;}
	table th{width:80px;text-align: right;font-weight: bold;    padding: 10px;}
	table td input ,table td textarea{width:95%;}
</style>
</head>
<body>


<div class="page-container">
	<div >
	<input type="hidden" class="authorId"/>
		<table>
			<tr>
				<td><span class="red">*</span>作者名称：</td>
				<td><input type="text" class="form-control authorName txtValidation" data-type="txtStr" data-value="作者名称不能为空"/></td>
			</tr>
		</table>
	</div>
</div>
<!--_footer 作为公共模版分离出去-->
<script type="text/javascript" src="static/js/pc/jquery/1.9.1/jquery.min.js"></script> 
<script type="text/javascript" src="static/js/pc/layer/2.4/layer.js"></script>
<script type="text/javascript" src="static/js/h-ui/js/H-ui.min.js"></script> 
<script type="text/javascript" src="static/js/h-ui.admin/js/H-ui.admin.js"></script>
<!--/_footer 作为公共模版分离出去-->

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="static/js/aes.js"></script>
<script type="text/javascript" src="static/js/tf_aes.js"></script>
<script type="text/javascript" src="static/js/publicAjax.js"></script>
<script type="text/javascript" src="static/js/public.js"></script>
<script type="text/javascript">
	var userKey = new Object();
	userKey.key = "<%=sessionKey%>"
	userKey.iv = "<%=sessionIv%>"

	var addLayer = function(){
		var res = null;
		if(submitFun()){
			jsonDataList.url = 'saveAuthor';
			jsonDataList.data = encryptionFun({authorName:$(".authorName").val(),isHide:"N"},userKey.key,userKey.iv);
			jsonDataList.dataType = "text";
			res = showAjaxFun(jsonDataList,"");
			res =  decryptionFun(res,userKey.key,userKey.iv);
			parent.layer.alert(res.message);
			if(res.flag == "1"){
				return true;
			}else{
				return false;
			}
		}
	}
	
	var initLayer = function(_id){
		jsonDataList.url = 'queryAuthor';
		jsonDataList.data = encryptionFun({authorId:_id},userKey.key,userKey.iv);
		jsonDataList.dataType = "text";
		res = showAjaxFun(jsonDataList,"");
		res =  decryptionFun(res,userKey.key,userKey.iv);
		if(res.flag == "1"){
			if(res.message == null){
				res = res.data;
				$(".authorId").val(res.authorId);
				$(".authorName").val(res.authorName);
			}
		}
	}
</script> 
</body>
</html>