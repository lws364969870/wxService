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
	<div class="cl pd-5 bg-1 bk-gray mt-20"> <span class="l"> <a class="btn btn-primary radius"  onclick="article_add('用户添加','editUserPanel')" href="javascript:;"><i class="Hui-iconfont">&#xe600;</i> 添加用户信息</a></span> </div>
	<div class="mt-20">
		<table class="table table-border table-bordered table-bg table-hover table-sort">
			<thead>
				<tr class="text-c">
					<th width="80">序号</th>
					<th width="250">登录帐号</th>
					<th width="250">用户名称</th>
					<th width="250">邮箱</th>
					<th width="250">手机号码</th>
					<th width="250">操作</th>
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
	jsonDataList.url = "queryUser";
	jsonDataList.data = {
		pageNo:currentPage,//当前页 
		pageSize:pageSize,//当前页显示数量
	};
	var res = showAjaxFun(jsonDataList,"");
	if(res.flag == "1"){
		$(".queryTbody").children().remove();
		var str = '', li = res.data.list;
		if(li.length >0 ){
			for(var i=0 ; i<li.length;i++){
				str += '<tr class="text-c">';
					str += '<td>'+(i+1)+'</td>';
					str += '<td>'+li[i]["loginName"]+'</td>';
					str += '<td>'+li[i]["userName"]+'</td>';
					if(li[i]["email"] == null){
						str += '<td></td>';
					}else{
						str += '<td>'+li[i]["email"]+'</td>';
					}
					
					str += '<td>'+li[i]["phone"]+'</td>';
					str += '<td class="f-14 td-manage">';
						str += '<button class="btn btn-primary radius m_r10" onclick="article_edit(\'修改信息\',\''+li[i]["syUserId"]+'\');">修改</button>'; 
						str += '<button class="btn btn-danger radius m_r10" onclick="article_del(\''+li[i]["syUserId"]+'\');">删除</button>';
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

$(function(){
	init(1);
})


/*用户添加*/
function article_add(title,url,w,h){
	var index = layer.open({
		type: 2,
		title: title,
		content: [url,'no'],
		area:['320px','495px'],
		btn:["添加","关闭"],
		resize :false,
		move :false,
		success: function(layero, index){
			var iframeWin = window[layero.find('iframe')[0]['name']]; 
			iframeWin.init();
		},
		yes: function(index, layero){
			var iframeWin = window[layero.find('iframe')[0]['name']]; 
			if(iframeWin.addLayer() == true){
				layer.close(index);
				init(1);
			}
		}
	});
}

/*修改文章*/
function article_edit(title,id){
	var index = layer.open({
		type: 2,
		title: title,
		content: ["editUserPanel","no"],
		area:['320px','495px'],
		btn:["修改","关闭"],
		resize :false,
		move :false,
		success: function(layero, index){
			var iframeWin = window[layero.find('iframe')[0]['name']]; 
			iframeWin.initLayer(id);
		},
		yes: function(index, layero){
			var iframeWin = window[layero.find('iframe')[0]['name']]; 
			if(iframeWin.updateLayer() == true){
				layer.close(index);
				init(1);
			}
		}
	});
}
/*删除文章*/
function article_del(id){
	layer.confirm('确认要删除吗？',function(index){
		jsonDataList.url = "deleteUser";
		jsonDataList.data = {
			syUserId:id//传articleId主键
		};
		var res = showAjaxFun(jsonDataList,"");
		layer.msg(res.message,{icon:1,time:3000});
		if(res.flag == "1"){
			init(1);
		}
	});
}

</script> 
</body>
</html>