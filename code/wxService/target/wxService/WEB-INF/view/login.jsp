<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />

<link rel="stylesheet" type="text/css" href="static/js/h-ui/css/H-ui.min.css" />
<link rel="stylesheet" type="text/css" href="static/js/h-ui.admin/css/H-ui.login.css" />
<link rel="stylesheet" type="text/css" href="static/js/pc/Hui-iconfont/1.0.8/iconfont.css" />

<title>佛山市统计后台登录 </title>
</head>
<body>
<input type="hidden" id="TenantId" name="TenantId" value="" />
<div class="header"></div>
<div class="loginWraper">
  <div id="loginform" class="loginBox">
    <div class="form-horizontal">
      <div class="row cl">
        <label class="form-label col-xs-3"><i class="Hui-iconfont">&#xe60d;</i></label>
        <div class="formControls col-xs-8">
          <input id="loginName"  type="text" data-type="txtStr" data-value="账户不能为空" placeholder="账户" class="txtValidation input-text size-L">
        </div>
      </div>
      <div class="row cl">
        <label class="form-label col-xs-3"><i class="Hui-iconfont">&#xe60e;</i></label>
        <div class="formControls col-xs-8">
          <input id="passWord"  type="password" data-type="pwd" data-value="密码不能为空" placeholder="密码" class="input-text size-L">
        </div>
      </div>
      <div class="row cl">
        <div class="formControls col-xs-8 col-xs-offset-3">
          <label for="online">
            <input type="checkbox" name="online" id="online" value="">
            使我保持登录状态</label>
        </div>
      </div>
      <div class="row cl">
        <div class="formControls col-xs-8 col-xs-offset-3">
			<button onclick="loginFun();" class="btn btn-success radius size-L">&nbsp;登&nbsp;&nbsp;&nbsp;&nbsp;录&nbsp;</button>
        </div>
      </div>
    </div>
  </div>
</div>
<div class="footer">佛山市统计</div>
<script type="text/javascript" src="static/js/pc/jquery/1.9.1/jquery.min.js"></script> 
<script type="text/javascript" src="static/js/public.js"></script>
<script type="text/javascript" src="static/js/jquery.cookie.js" ></script>
<script type="text/javascript">
	var init = function(){
		if($.cookie("checkIn") == "true"){
			$("#loginName").val($.cookie("loginName"));
		  $("#passWord").val($.cookie("passWord"));
		  $("#online").prop("checked",true);
		}
	}
	
	init();
	
	var loginFun = function(){
		if(submitFun()){
			jQuery.ajax({
			url:"checklogin",
			dataType:"JSON",
			type:"POST",
			data:{loginName:$("#loginName").val(),passWord:$("#passWord").val()},
			async:false,
			success:function(data){
					if(data.flag == "1"){
						if($("#online").is(":checked")){
							$.cookie("loginName",$("#loginName").val());
							$.cookie("passWord",$("#passWord").val());
							$.cookie("checkIn",true);
						}
						window.location.href = "main";
					}else{
						alert(data.message);
					}
			},error: function(errorData) {
				alert(errorData);
			}
		});
		}
	}
</script>
</body>
</html>
