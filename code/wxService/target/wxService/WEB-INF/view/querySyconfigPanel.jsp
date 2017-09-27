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
	.m_r10{
		margin-right: 10px;
	}
	.layui-layer-btn{text-align:center !important}
</style>
</head>
<body>
<nav class="breadcrumb"><a class="btn btn-success radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新"><i class="Hui-iconfont"></i></a></nav>
<div class="page-container">
	<!--<div class="text-c">
		<input type="text"  id="container_title" placeholder="标题" style="width:250px" class="input-text">
		<button name="" id="" class="btn btn-success" onclick="init(1);"><i class="Hui-iconfont">&#xe665;</i> 搜索条件</button>
	</div>-->
	<div class="mt-20">
		<table class="table table-border table-bordered table-bg table-hover table-sort">
			<thead>
				<tr class="text-c">
					<th width="30">序号</th>
					<th width="120">参数名称 </th>
					<th width="250">参数值</th>
					<th width="170">备注</th>
					<th width="80">操作</th>
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
<script type="text/javascript" src="static/js/pc/My97DatePicker/4.8/WdatePicker.js"></script> 
<script type="text/javascript" src="static/js/pc/datatables/1.10.0/jquery.dataTables.min.js"></script> 
<script type="text/javascript" src="static/js/publicAjax.js"></script>
<script type="text/javascript" src="static/js/page.js"></script>
<script type="text/javascript">
var currentPage = 1,pageSize = 10 ;
var init = function(page){
	if(page != undefined){currentPage = page;}
	jsonDataList.url = "querySyconfig";
	var res = showAjaxFun(jsonDataList,"");
	if(res.flag == "1"){
		$(".queryTbody").children().remove();
		var str = '', li = res.data.list;
		if(li.length >0 ){
			for(var i=0 ; i<li.length;i++){
				str += '<tr class="text-c">';
					str += '<td>'+(i+1)+'</td>';
					str += '<td>'+li[i]["configKey"]+'</td>';
					str += '<td class="configValue">'+li[i]["configValue"]+'</td>';
					str += '<td>'+li[i]["remark"]+'</td>';
					str += '<td class="f-14 td-manage">';
						str += '<button class="btn btn-primary radius m_r10" onclick="updateType(this,\''+li[i]["syConfigId"]+'\',\''+li[i]["isShow"]+'\');">编辑</button>'; 
					str += '</td>';
				str += '</tr>';
			}
			$(".queryTbody").html(str);
			setPagination("page", Math.ceil(res["data"]["totalRecords"]/pageSize), currentPage, "init",res["data"]["totalRecords"],pageSize);
		}
	}else{
		alert(res.Message);
	}
}

var updateType = function(_this,_syConfigId,_isShow){
	var configValue = $(_this).parents("tr").children(".configValue").html();
	$(_this).parents("tr").children(".configValue").html('<input type="text" style="width:100%" class="configValueStr" value="'+configValue+'"/>');
	$(_this).parents("td").html('<button class="btn btn-primary radius m_r10" onclick="update(this,\''+_syConfigId+'\',\''+_isShow+'\');">保存</button>');
	
}

var update = function(_this,_syConfigId,_isShow){
	var is = confirm("是否确定保存！");
	if(is){
		var configValueStr = $(_this).parents("tr").children(".configValue").find("input").val().trim();
		jsonDataList.url = "saveSyconfig";
		jsonDataList.data = {
			syConfigId:_syConfigId,
			configValue:configValueStr,
			isShow:_isShow
		};
		var res = showAjaxFun(jsonDataList,"");
		alert(res.message);
		if(res.flag == "1"){
			$(_this).parents("tr").children(".configValue").html(configValueStr);
			$(_this).parents("td").html('<button class="btn btn-primary radius m_r10" onclick="updateType(this,\''+_syConfigId+'\',\''+_isShow+'\');">编辑</button>');
		}
	}
}

$(function(){
	init(1);
})



</script> 
</body>
</html>