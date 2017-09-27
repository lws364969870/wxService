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
	.w_all{width: 100%;}
	
	.m_r10{
		margin-right: 10px;
	}
	.layui-layer-btn{text-align:center !important}
	
	.text_ellipsis{
		width:200px;
		overflow:hidden;
		text-overflow:ellipsis;
		-o-text-overflow:ellipsis;
		-webkit-text-overflow:ellipsis;
		-moz-text-overflow:ellipsis;
		white-space:nowrap;
		display: inline-block;
	}
	
	table tr td{cursor: pointer;}
</style>
</head>
<body>
<nav class="breadcrumb"><a class="btn btn-success radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新"><i class="Hui-iconfont"></i></a></nav>
<div class="page-container">
	<!--<div class="text-c">
		<input type="text"  id="container_title" placeholder="openid" style="width:250px" class="input-text">
		<button class="btn btn-success" onclick="init(1);"><i class="Hui-iconfont">&#xe665;</i> 搜索条件</button>
	</div>-->
	<div class="mt-20">
		<table class="table table-border table-bordered table-bg table-hover table-sort">
			<thead>
				<tr class="text-c">
					<tr class="text-c">
					<th width="30">序号</th>
					<th width="120">创建日期</th>
					<th width="120">推送日期</th>
					<th width="170">备注</th>
					<th width="50">状态</th>
					<th width="200">操作</th>
					
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
<script type="text/javascript" src="static/js/pc/datatables/1.10.0/jquery.dataTables.min.js"></script> 
<script type="text/javascript" src="static/js/publicAjax.js"></script>
<script type="text/javascript" src="static/js/page.js"></script>
<script type="text/javascript" src="static/js/public.js"></script>
<script type="text/javascript">

/*获取发布状态 */
var getWordbookArr = new Array();
var getWordbookList = function(){
	jsonDataList.url = 'queryWordbook';
	jsonDataList.data = {parentsBookCode:"H003"};
	var res = showAjaxFun(jsonDataList,"");
	if(res.flag == "1"){
		if(res.data.length > 0){
			getWordbookArr = res.data
		}
	}
}

/*根据参数匹配数组成功后，返回数组中的某个对象*/
var getWordbookSign = function(_name){
	var is = "";
	for (idx in getWordbookArr) {
		var li = getWordbookArr[idx];
		if(li["wordbookCode"] == _name){
			return li;
		}
	}
	return is;
}

var isNull = function(_str){
	if(_str != null){
		return _str
	}else{
		return "";
	}
}


var currentPage = 1,pageSize = 10, alllist = null;
var init = function(page){
	if(page != undefined){currentPage = page;}
	jsonDataList.url = "queryMaterial";
	jsonDataList.data = {
		pageNo:currentPage,//当前页 
		pageSize:pageSize,//当前页显示数量
	};
	var res = showAjaxFun(jsonDataList,"");
	if(res.flag == "1"){
		$(".queryTbody").children().remove();
		var str = '', li = res.data.list;
		if(li.length >0 ){
			alllist = li;
			for(var i=0 ; i<li.length;i++){
				str += '<tr class="text-c">';
					str += '<td>'+(i+1)+'</td>';
					str += '<td>'+conversionTime(li[i]["mediaDate"])+'</td>';//创建日期
					if(li[i]["sendDate"] != null){
						str += '<td>'+conversionTime(li[i]["sendDate"]);+'</td>';//推送日期
					}else{
						str += '<td></td>';//推送日期
					}
					str += '<td title="'+isNull(li[i]["remark"])+'"><span class="text_ellipsis">'+isNull(li[i]["remark"])+'</span></td>';//备注
					str += '<td>'+getWordbookSign(li[i]["mediaType"])["wordbookName"]+'</td>';//状态
					str += '<td>';
						str += '<button class="btn btn-primary  radius m_r10" onclick="showUpdateInfo(\''+li[i]["materialId"]+'\');">修改</button>';
						str += '<button class="btn btn-primary  radius m_r10" onclick="auditBtn(\''+li[i]["materialId"]+'\');">审核</button>';
						str += '<button class="btn btn-primary  radius m_r10" onclick="massBtn(\''+li[i]["materialId"]+'\');">群发</button>';
						str += '<button class="btn btn-primary  radius m_r10" onclick="switchBtn(\''+li[i]["materialId"]+'\');">切换</button>';
					str +='</td>';
				str += '</tr>';
			}
			$(".queryTbody").html(str);
			setPagination("page", Math.ceil(res["data"]["totalRecords"]/pageSize), currentPage, "init",res["data"]["totalRecords"],pageSize);
		}
	}else{
		alert(res.Message);
	}
}

var getInfoBtn = function (_materialId){
	for(idx in  alllist){
		var obj = alllist[idx];
		if(obj["materialId"] == _materialId){
			return obj;
		}
	}
}

var showUpdateInfo = function(_materialId){
	var index = layer.open({
		type: 1,
		title: "修改",
		content: '<div style="margin: 16px 16px 0 16px;"><textarea class="remarkStr w_all" style="height:150px;"></textarea></div>',
		btn:["修改","关闭"],
		area:["400px"],
		scrollbar :false,
		success: function(layero, index){
			$(".remarkStr").val(isNull(getInfoBtn(_materialId)["remark"]));
		},
		yes:function(index,layero){
			if(updateBtn(_materialId,$(".remarkStr").val()) == true){init(currentPage); layer.close(index);}
		}
	});
}


/*修改*/
var updateBtn = function(_materialId,_remark){
	jsonDataList.url = "saveMaterial";
	jsonDataList.data = {
		materialId:_materialId,
		remark:_remark
	};
	var res = showAjaxFun(jsonDataList,"");
	alert(res.message);
	if(res.flag == "1"){return true}else{return false}
}

/*审核素材*/
var auditBtn = function(_materialId){
	jsonDataList.url = "examineMaterial";
	jsonDataList.data = {
		materialId:_materialId
	};
	var res = showAjaxFun(jsonDataList,"");
}
/*群发接口*/
var  massBtn = function(_materialId){
	jsonDataList.url = "sendallMaterial";
	jsonDataList.data = {
		materialId:_materialId
	};
	var res = showAjaxFun(jsonDataList,"");
	console.log(res);
}
/*切换  撤回素材*/
var switchBtn = function(_materialId){
	jsonDataList.url = "revokeMaterial";
	jsonDataList.data = {
		materialId:_materialId
	};
	var res = showAjaxFun(jsonDataList,"");
	console.log(res);
}

var init_fun = function(){
	getWordbookList();
	init(1);
}
 
$(function(){
	init_fun();
})
</script> 
</body>
</html>