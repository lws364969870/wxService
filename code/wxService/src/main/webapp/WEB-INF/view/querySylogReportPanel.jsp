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
<script type="text/javascript">
	var roleType = "<%=sessionRoleType%>";
</script>
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
<link rel="stylesheet" type="text/css" href="static/js/jedate/skin/jedate.css" />
<!--[if IE 6]>
<script type="text/javascript" src="lib/DD_belatedPNG_0.0.8a-min.js" ></script>
<script>DD_belatedPNG.fix('*');</script>
<![endif]-->
<title>佛山市统计后台登录</title>
<style type="text/css">
	
	.dataTables_info{
		width:300px;
	}
	h5{padding: 10px 0; margin: 0; border-bottom: 1px solid #000;}
	h4{margin:0 ; text-align: left;}
	.createSelect>li{width: 16%;  float: left; border: 1px solid #000; margin-left: 10px; padding: 0 13px; margin-bottom: 15px;}
	.describe{cursor: pointer; height: 64px; display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden; margin-bottom: 10px;}
	.btnDiv{height: 106px;}
	.btnDiv .btn{float: left; margin: 8px;}
	.btn, .btn.size-M {  padding: 4px 10px;}
	input[name=pushCheck],input[name=checknAll]{cursor: pointer;}
</style>
</head>
<body style="min-width: 1300px;">
<nav class="breadcrumb"><a class="btn btn-success radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新"><i class="Hui-iconfont"></i></a></nav>
<div class="page-container">
	<div class="text-c">
		<input type="text" placeholder="开始日期" style="width:250px;" class="input-text logdateFrom" readonly>
		<button class="btn btn-success" onclick="init(1);"><i class="Hui-iconfont">&#xe665;</i> 搜索</button>
	</div>
	<div class="mt-20">
		<table class="table table-border table-bordered table-bg table-hover table-sort">
			<thead>
				<tr class="text-c">
					<tr class="text-c">
					<th width="30">序号</th>
					<th width="120">月份</th>
					<th width="120">操作类型</th>
					<th width="120">频率</th>
				</tr>
			</thead>
			<tbody class="queryTbody">
				
			</tbody>
		</table>
		<div class="page textC">
			<div>
				<div class=" fl dataTables_info"></div>
				<ul id="page" class="fr">
				</ul>
			</div>
		</div>
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
<script type="text/javascript" src="static/js/page.js"></script>
<script type="text/javascript" src="static/js/public.js"></script>
<script type="text/javascript" src="static/js/jedate/jquery.jedate.js"></script>
<script type="text/javascript">
var userKey = new Object();
	userKey.key = "<%=sessionKey%>"
	userKey.iv = "<%=sessionIv%>"
var init = function(){
	jsonDataList.url = "querySylogReportForYear";
	var dateTime = $(".logdateFrom").val();
	if(dateTime == "" || dateTime == null){
		dateTime = new Date();
		dateTime = dateTime.getFullYear()
	}
	jsonDataList.data = encryptionFun({year:dateTime},userKey.key,userKey.iv);
	jsonDataList.dataType = "text";
	var res = showAjaxFun(jsonDataList,"");
		res =  decryptionFun(res,userKey.key,userKey.iv);
	if(res.flag == "1"){
		$(".queryTbody").children().remove();
		var str = '', li = res.data;
		if(li.length >0 ){
			for(var i=0 ; i<li.length;i++){
				str += '<tr class="text-c">';
					str += '<td>'+(i+1)+'</td>';
					str += '<td>'+li[i]["MONTH"]+'月</td>';
					str += '<td>'+li[i]["LOGTYPENAME"]+'</td>';
					str += '<td>'+li[i]["COUNT"]+'</td>';
				str += '</tr>';
			}
			$(".queryTbody").html(str);
		}else{
			$("#page,.dataTables_info").html("");
		}
	}else{
		layer.alert(res.Message);
	}
}

$(".logdateFrom").bind('click',function(){
	$.jeDate('.logdateFrom',{
		format:"YYYY",
		isClear:false,
	});
})


$(function(){
	init();
})
</script>
</body>
</html>