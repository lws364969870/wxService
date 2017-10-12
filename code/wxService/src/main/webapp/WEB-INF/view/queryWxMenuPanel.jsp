<%@page import="com.lws.domain.utils.request.CryptUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String defaulKey =CryptUtil.getDefaultSessionKey();
String defaulIv =CryptUtil.getDefaultSessionIv();
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

<link rel="stylesheet" type="text/css" href="static/js/h-ui.admin/css/H-ui.admin.css" />
<link rel="stylesheet" type="text/css" href="static/js/pc/Hui-iconfont/1.0.8/iconfont.css" />
<link rel="stylesheet" type="text/css" href="static/js/h-ui.admin/skin/default/skin.css" id="skin" />
<link rel="stylesheet" type="text/css" href="static/js/h-ui.admin/css/style.css" />
<!--[if IE 6]>
<script type="text/javascript" src="lib/DD_belatedPNG_0.0.8a-min.js" ></script>
<script>DD_belatedPNG.fix('*');</script>
<![endif]-->
<!--<link rel="stylesheet" type="text/css" href="static/js/tree/css/bootstrap.min.css">-->
<link rel="stylesheet" type="text/css" href="static/js/h-ui/css/H-ui.min.css" />
<!--<link rel="stylesheet" type="text/css" href="http://www.jq22.com/jquery/font-awesome.4.6.0.css">-->
<link rel="stylesheet" type="text/css" href="static/js/pc/jOrgChart/css/jquery.jOrgChart.css" />
<link rel="stylesheet" type="text/css" href="static/js/pc/jOrgChart/css/custom.css" />
<link rel="stylesheet" type="text/css" href="static/js/pc/jOrgChart/css/prettify.css" />

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
<script type="text/javascript" src="static/js/pc/jOrgChart/prettify.js"></script>
<script type="text/javascript" src="static/js/pc/jOrgChart/jquery-ui.min.js"></script>
<script type="text/javascript" src="static/js/pc/jOrgChart/jquery.jOrgChart.js"></script>
<script type="text/javascript">
jQuery(document).ready(function() {
    $("#org").jOrgChart({
        chartElement : '#chart',
        dragAndDrop  : false
    });
});
</script>
<title>佛山市统计后台登录</title>
<style type="text/css">
	.m_r10{
		margin-right: 10px;
	}
	.line{border: none;}
	.jOrgChart table{margin: auto;}
</style>
</head>
<body>
<nav class="breadcrumb"><a class="btn btn-success radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新"><i class="Hui-iconfont"></i></a></nav>
<div class="page-container">
	<div class="mt-20">
		<ul id="org" style="display:none">
		    <li>微信菜单栏
		       <ul class="jorg">
		        
		       </ul>
		    </li>
		</ul>    
		<div id="chart" class="orgChart"></div>
		
		<div>
			<table class="table table-border table-bordered table-bg  table-sort" style="background-color: #fff; width: 60%;margin: auto;">
				<tr><th style="text-align: right; " width="80">地址路径：</th><td class="wxUrl"></td></tr>
			</table>
		</div>
	</div>
</div>

<script src="" type="text/javascript" charset="utf-8"></script>
<script>
   var  defaulKey = new Object();
	 	defaulKey.key = "<%=defaulKey%>"
	 	defaulKey.iv = "<%=defaulIv%>"
	var init = function(){
		jsonDataList.url = "findLevelWxMenu";
		jsonDataList.data = encryptionFun({},defaulKey.key,defaulKey.iv);
		jsonDataList.dataType = "text";
		var res = showAjaxFun(jsonDataList,"");
			res =  decryptionFun(res,defaulKey.key,defaulKey.iv);
		if(res.flag =="1"){
			if(res.data.length>0){
				res = res.data;
				var str = '';
				for(var i = 0 ; i < res.length; i++){
					str += '<li>'+res[i]["menuName"];
						if(res[i]["child"] !=null){
							var child = res[i]["child"];
							str += '<ul>';
								for(var idx in child){
									str += '<li><a href ="javascript:;" onclick="showLayer(\''+child[idx]["menuUrl"]+'\');">'+child[idx]["menuName"]+'</a></li>';
								}
							str += '</ul>';	
						}
					str +='</li>';
				}
				
				$(".jorg").html(str);
				
				
			}
		}
	}
	init();
	var showLayer = function(url){
		$(".wxUrl").html(url);
	}
</script>
</body>
</html>