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
	table td input ,table td textarea ,select {width:95%;}
	.pwd2 th{width: 85px;}
</style>
</head>
<body>


<div class="page-container">
	<div >
		<input type="hidden" class="syUserId"/>
		<table class="t1 hide">
			<tr>
				<th><span class="red">*</span>用户名称：</th>
				<td><input type="text" class="form-control userName txtValidation" data-type="txtStr" data-value="用户名称不能为空"/></td>
			</tr>
			<tr>
				<th><span class="red">*</span>角色：</th>
				<td><select class="txtValidation selectRale" data-type="selectStr" data-value="请选择角色"></select></td>
			</tr>
			<tr>
				<th><span class="red">*</span>登录帐号：</th>
				<td><input type="text" class="form-control loginName txtValidation" data-type="txtStr" data-value="登录帐号不能为空"/></td>
			</tr>
			<tr>
				<th><span class="red">*</span>密码：</th>
				<td><input maxlength="18" type="password" class="form-control passWord txtValidation" data-type="pwd" data-value="密码不能为空"/></td>
			</tr>
			
			<tr>
				<th><span class="red">*</span>确认密码：</th>
				<td><input maxlength="18" type="password" class="form-control passWord txtValidation" data-type="pwd" data-value="确认密码不能为空"/></td>
			</tr>
			
			<tr>
				<th>邮箱：</th>
				<td><input type="text" class="form-control email"/></td>
			</tr>
			<tr>
				<th>固定电话：</th>
				<td><input type="text" class="form-control phone" /></td>
			</tr>
			<tr>
				<th>手机号码：</th>
				<td><input type="text" class="form-control mobile" maxlength="11"/></td>
			</tr>
			<tr>
				<th>备注：</th>
				<td><textarea class="form-control remarks" style="height: 80px; resize:none"></textarea></td>
			</tr>
		</table>
		
		<table class="t2 hide">
			<tr>
				<th>用户名称：</th>
				<td><input type="text" class="form-control userName txtValidation" data-type="txtStr" data-value="用户名称不能为空" readonly="readonly"/></td>
			</tr>
			<tr>
				<th>登录帐号：</th>
				<td class="loginName"></td>
			</tr>
			<tr>
				<th>角色：</th>
				<td><select class="txtValidation selectRale" disabled="disabled"></select></td>
			</tr>
			<tr>
				<th>邮箱：</th>
				<td><input type="text" class="form-control email" readonly="readonly"/></td>
			</tr>
			<tr>
				<th>固定电话：</th>
				<td><input type="text" class="form-control phone" readonly="readonly"/></td>
			</tr>
			<tr>
				<th>手机号码：</th>
				<td><input type="text" class="form-control mobile" maxlength="11" readonly="readonly"/></td>
			</tr>
			<tr>
				<th>备注：</th>
				<td><textarea class="form-control remarks" style="height: 80px; resize:none" readonly="readonly"></textarea></td>
			</tr>
		</table>
		
		<table class="t3 hide">
			<tr class="pwd2">
				<th>原密码：</th>
				<td><input maxlength="18" type="password" class="form-control passWord txtValidation" data-type="pwd2" data-value="原密码"/></td>
			</tr>
			
			<tr class="pwd2">
				<th>新密码：</th>
				<td><input maxlength="18" type="password" class="form-control passWord txtValidation" data-type="pwd2" data-value="新密码"/></td>
			</tr>
			
			<tr class="pwd2">
				<th>确认新密码：</th>
				<td><input maxlength="18" type="password" class="form-control passWord txtValidation" data-type="pwd2" data-value="确认新密码"/></td>
			</tr>
		</table>
		
		<table class="t4 hide">
			<tr>
				<th>用户名称：</th>
				<td><input type="text" class="form-control userName txtValidation" data-type="txtStr" data-value="用户名称不能为空"/></td>
			</tr>
			<tr>
				<th>登录帐号：</th>
				<td class="loginName"></td>
			</tr>
			<tr>
				<th>邮箱：</th>
				<td><input type="text" class="form-control email"/></td>
			</tr>
			<tr>
				<th>固定电话：</th>
				<td><input type="text" class="form-control phone"/></td>
			</tr>
			<tr>
				<th>手机号码：</th>
				<td><input type="text" class="form-control mobile" maxlength="11"/></td>
			</tr>
			<tr>
				<th>备注：</th>
				<td><textarea class="form-control remarks" style="height: 80px; resize:none"></textarea></td>
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
	
	var selectRole = function(){
		jsonDataList.url = 'queryAllSyrole';
		jsonDataList.data = encryptionFun({},userKey.key,userKey.iv);
		jsonDataList.dataType = "text";
		var res = showAjaxFun(jsonDataList,"");
			res =  decryptionFun(res,userKey.key,userKey.iv);
		if(res.flag == "1"){
			res  = res.data; 
			if(res.length > 0 ){
				selectControl(".selectRale",res,"syRoleId","roleName");
			}
		}
	}
	
	var addLayer = function(){
		var res = null;
		if(submitFun()){
			if($(".passWord").eq(0).val() == $(".passWord").eq(1).val()){
				jsonDataList.url = 'addSyuser';
				var objList = {
					syUserId:'',
					userName:$(".userName").val(),//用户名称
					email:$(".email").val(),
					loginName:$(".loginName").val(),//登录帐号
					passWord:$(".passWord").eq(0).val(),//密码
					againPassWord:$(".passWord").eq(1).val(),//密码
					syRoleId:$(".selectRale").val(),//角色ID
					phone:$(".phone").val(),//固定电话
					mobile:$(".mobile").val(),//手机号码
					remarks:$(".remarks").val()//备注
				};
				jsonDataList.data = encryptionFun(objList,userKey.key,userKey.iv);
				jsonDataList.dataType = "text";
				res = showAjaxFun(jsonDataList,"");
				return decryptionFun(res,userKey.key,userKey.iv);
			}else{
				parent.layer.alert("密码不一致！");
			}
			
		}
	}
	
	var init = function(){
		$(".t1").removeClass("hide");
		$(".t2,.t3,.t4").remove();
		selectRole();
	}
	
	var initLayer = function(_id){
		jsonDataList.url = 'findSyuserById';
		jsonDataList.data = encryptionFun({syUserId:_id},userKey.key,userKey.iv);
		jsonDataList.dataType = "text";
		res = showAjaxFun(jsonDataList,"");
		res =  decryptionFun(res,userKey.key,userKey.iv);
		$(".t4").removeClass("hide");
		$(".t1,.t3,.t2").remove();
		if(res.flag == "1"){
			if(res.message == null||res.message == ""){
				res = res.data;
				$(".pwd").remove();
				$(".syUserId").val(res.syUserId);
				$(".email").val(res.email);
				$(".userName").val(res.userName);//用户名称
				$(".loginName").html(res.loginName);//登录帐号
				$(".phone").val(res.phone);//固定电话
				$(".mobile").val(res.mobile);//手机号码
				$(".remarks").val(res.remarks);//备注
				$(".pwd").remove();
			}
		}
	}
	
	var examine = function(_id){
		jsonDataList.url = 'findSyuserById';
		jsonDataList.data = encryptionFun({syUserId:_id},userKey.key,userKey.iv);
		jsonDataList.dataType = "text";
		res = showAjaxFun(jsonDataList,"");
		res =  decryptionFun(res,userKey.key,userKey.iv);
		$(".t2").removeClass("hide");
		$(".t1,.t3,.t4").remove();
		if(res.flag == "1"){
			if(res.message == null||res.message == ""){
				selectRole();				
				res = res.data;
				$(".pwd").remove();
				$(".selectRale").val(res.syRoleId);
				$(".syUserId").val(res.syUserId);
				$(".email").val(res.email);
				$(".userName").val(res.userName);//用户名称
				$(".loginName").html(res.loginName);//登录帐号
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
			var pwd = $(".passWord").eq(0).val();
			var pwd2 = $(".passWord").eq(1).val();
			var objList = {
				syUserId:$(".syUserId").val(),
				userName:$(".userName").val(),//用户名称
				phone:$(".phone").val(),//固定电话
				mobile:$(".mobile").val(),//手机号码
				remarks:$(".remarks").val(),//备注
				email:$(".email").val()
			};
			if(pwd != "" && pwd2 != ""){
				objList.passWord = $(".passWord").eq(0).val();//新密码
			}
			if($(".passWord").eq(0).val() == $(".passWord").eq(1).val()){
				jsonDataList.url = 'saveSyuserInfo';
				jsonDataList.data = encryptionFun(objList,userKey.key,userKey.iv);
				jsonDataList.dataType = "text";
				res = showAjaxFun(jsonDataList,"");
				res = decryptionFun(res,userKey.key,userKey.iv);
				return res;
			}else{
				parent.layer.alert("密码不一致！");
			}
		}
	}
	
	
	var updatePwd = function(_id){
		$(".t3").removeClass("hide");
		$(".t1,.t2,.t4").remove();
		$(".syUserId").val(_id);
	}
	
	var updatePwd2 = function(_id){
		var res = null;
		if(submitFun()){
			var pwd0 = $(".passWord").eq(0).val();
			var pwd1 = $(".passWord").eq(1).val();
			var pwd2 = $(".passWord").eq(2).val();
			var objList = {
				syUserId:$(".syUserId").val(),
				oldPassWord:pwd0,
				newPassWord:pwd1,
				againPassWord:pwd2,
			};
			if(pwd1 == pwd2){
				jsonDataList.url = 'editUserPassWord';
				jsonDataList.data = encryptionFun(objList,userKey.key,userKey.iv);
				jsonDataList.dataType = "text";
				res = showAjaxFun(jsonDataList,"");
				res =  decryptionFun(res,userKey.key,userKey.iv);
				return res;
			}else{
				parent.layer.alert("密码不一致！");
			}
		}
	}
</script> 
</body>
</html>