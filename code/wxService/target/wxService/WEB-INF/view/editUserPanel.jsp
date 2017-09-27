<%@page import="com.lws.domain.model.SessionData"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
SessionData sessionData = (SessionData)session.getAttribute("user");
String sessionRoleType = sessionData.getRoleType();
String sessionLoginName = sessionData.getLoginName();
String sessionRoleName = sessionData.getRoleName();

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
	<input type="hidden" class="syUserId"/>
		<table>
			<tr>
				<th><span class="red">*</span>用户名称：</th>
				<td><input type="text" class="userName txtValidation" data-type="txtStr" data-value="用户名称不能为空"/></td>
			</tr>
			<tr>
				<th><span class="red">*</span>登录帐号：</th>
				<td><input type="text" class="loginName txtValidation" data-type="txtStr" data-value="登录帐号不能为空"/></td>
			</tr>
			<tr class="pwd">
				<th><span class="red">*</span>密码：</th>
				<td><input maxlength="6" type="password" class="passWord txtValidation" data-type="pwd" data-value="密码不能为空"/></td>
			</tr>
			
			<tr class="pwd">
				<th><span class="red">*</span>确认密码：</th>
				<td><input maxlength="6" type="password" class="passWord txtValidation" data-type="pwd" data-value="确认密码不能为空"/></td>
			</tr>
			
			<tr class="pwd2">
				<th>原密码：</th>
				<td><input maxlength="6" type="password" class="passWord"/></td>
			</tr>
			
			<tr class="pwd2">
				<th>新密码：</th>
				<td><input maxlength="6" type="password" class="passWord txtValidation" data-type="pwd2" data-value="新密码"/></td>
			</tr>
			
			<tr>
				<th>邮箱：</th>
				<td><input type="text" class="email"/></td>
			</tr>
			<tr>
				<th>固定电话：</th>
				<td><input type="text" class="phone" /></td>
			</tr>
			<tr>
				<th>手机号码：</th>
				<td><input type="text" class="mobile" maxlength="11"/></td>
			</tr>
			<tr>
				<th>备注：</th>
				<td><textarea class="remarks" style="height: 80px; resize:none"></textarea></td>
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
<script type="text/javascript" src="static/js/publicAjax.js"></script>
<script type="text/javascript" src="static/js/public.js"></script>
<script type="text/javascript">
	var addLayer = function(){
		var res = null;
		if(submitFun()){
			if($(".passWord").eq(0).val() == $(".passWord").eq(1).val()){
				jsonDataList.url = 'saveSyuser';
				jsonDataList.data = {
					syUserId:'',
					userName:$(".userName").val(),//用户名称
					email:$(".email").val(),
					loginName:$(".loginName").val(),//登录帐号
					passWord:$(".passWord").eq(0).val(),//密码
					phone:$(".phone").val(),//固定电话
					mobile:$(".mobile").val(),//手机号码
					remarks:$(".remarks").val()//备注
				};
				res = showAjaxFun(jsonDataList,"");
				alert(res.message);
				if(res.flag == "1"){
					return true;
				}else{
					return false;
				}
			}else{
				alert("密码不一致！");
			}
			
		}
	}
	
	var init = function(){
		$(".pwd2").remove();
	}
	
	var initLayer = function(_id){
		jsonDataList.url = 'findSyuserById';
		jsonDataList.data = {
			syUserId:_id
		};
		res = showAjaxFun(jsonDataList,"");
		if(res.flag == "1"){
			if(res.message == null){
				res = res.data;
				$(".pwd").remove();
				$(".syUserId").val(res.syUserId);
				$(".email").val(res.email);
				$(".userName").val(res.userName);//用户名称
				$(".loginName").val(res.loginName);//登录帐号
				$(".phone").val(res.phone);//固定电话
				$(".mobile").val(res.mobile);//手机号码
				$(".remarks").val(res.remarks);//备注
				$(".pwd").remove();
			}
		}
	}
	
	var updateLayer = function(){
		var res = null;
		if(submitFun()){
			var pwd = $(".passWord").eq(1).val();
			jsonDataList.data = {
				syUserId:$(".syUserId").val(),
				userName:$(".userName").val(),//用户名称
				loginName:$(".loginName").val(),//登录帐号
				phone:$(".phone").val(),//固定电话
				mobile:$(".mobile").val(),//手机号码
				remarks:$(".remarks").val()//备注
			};
			if(pwd != ""){
				jsonDataList.data.passWord = $(".passWord").eq(1).val();//新密码
			}
			jsonDataList.url = 'saveSyuser';
			res = showAjaxFun(jsonDataList,"");
			alert(res.message);
			if(res.flag == "1"){
				return true;
			}else{
				return false;
			}
		}
	}
	
</script> 
</body>
</html>