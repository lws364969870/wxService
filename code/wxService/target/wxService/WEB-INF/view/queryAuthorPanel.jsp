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
	<div class="cl pd-5 bg-1 bk-gray mt-20"> <span class="l"> <a class="btn btn-primary radius"  onclick="article_add('添加','editAuthorPanel','400','400')" href="javascript:;"><i class="Hui-iconfont">&#xe600;</i> 添加信息</a></span> </div>
	<div class="mt-20">
		<table class="table table-border table-bordered table-bg table-hover table-sort">
			<thead>
				<tr class="text-c">
					<th width="80">序号</th>
					<th width="250">作者名称</th>
					<th width="120">是否隐藏</th>
					<th width="250">操作</th>
				</tr>
			</thead>
			<tbody class="queryTbody">
				
			</tbody>
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
<script type="text/javascript" src="static/js/pc/My97DatePicker/4.8/WdatePicker.js"></script> 
<script type="text/javascript" src="static/js/pc/datatables/1.10.0/jquery.dataTables.min.js"></script> 
<script type="text/javascript" src="static/js/publicAjax.js"></script>
<script type="text/javascript" src="static/js/page.js"></script>
<script type="text/javascript">
var currentPage = 1,pageSize = 10 ;
var init = function(page){
	if(page != undefined){currentPage = page;}
	jsonDataList.url = "queryAuthor";
	jsonDataList.data = {
		pageNo:currentPage,//当前页 
		pageSize:pageSize,//当前页显示数量
	};
	var res = showAjaxFun(jsonDataList,"");
	if(res.flag == "1"){
		$(".queryTbody").children().remove();
		var str = '', li = res.data, _is = "Y",_isStr ="";
		if(li.length >0 ){
			for(var i=0 ; i<li.length;i++){
				str += '<tr class="text-c">';
					str += '<td>'+(i+1)+'</td>';
					str += '<td>'+li[i]["authorName"]+'</td>';
					if(li[i]["isHide"] == "Y"){
						_is = "N";
						_isStr = "启用";
						str += '<td>禁止</td>';
					}else{
						_is = "Y";
						_isStr = "禁止";
						str += '<td>启用</td>';
						
					}
					str += '<td class="f-14 td-manage">';
						str += '<button class="btn btn-primary radius m_r10" onclick="isHideFun(\''+_is+'\',\''+li[i]["authorId"]+'\');">'+_isStr+'</button>';  
						//str += '<button class="btn btn-primary radius m_r10" onclick="article_edit(\'修改信息\',\''+li[i]["authorId"]+'\');">修改</button>';  
						str += '<button class="btn btn-danger radius m_r10" onclick="article_del(\''+li[i]["authorId"]+'\');">删除</button>';  
						
					str += '</td>';
				str += '</tr>';
			}
			
			$(".queryTbody").html(str);
		}
	}else{
		alert(res.Message);
	}
}

$(function(){
	init(1);
})

var isHideFun = function(_isHide,_id){
	jsonDataList.url = "changeAuthorIsHide";
	jsonDataList.data = {
		isHide:_isHide,
		authorId:_id,
	};
	var res = showAjaxFun(jsonDataList,"");
	alert(res.message);
	if(res.flag == "1"){init(1);}
}

/*用户添加*/
function article_add(title,url,w,h){
	var index = layer.open({
		type: 2,
		title: title,
		content: [url,'no'],
		area:['320px','150px'],
		btn:["添加","关闭"],
		resize :false,
		move :false,
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
		content: ["editAuthorPanel","no"],
		success: function(layero, index){
			var iframeWin = window[layero.find('iframe')[0]['name']]; 
			iframeWin.initLayer(id);
		},
		area:['320px','150px'],
		btn:["修改","关闭"],
		resize :false,
		move :false,
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
		jsonDataList.url = "deleteAuthor";
		jsonDataList.data = {
			authorId:id//传articleId主键
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