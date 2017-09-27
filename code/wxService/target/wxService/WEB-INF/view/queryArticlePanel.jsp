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
	.btnDiv .btn:nth-child(even){float: right; margin: 8px 0;}
	.btnDiv .btn:nth-child(odd){float: left; margin: 8px 0;}
	.btn, .btn.size-M {  padding: 4px 10px;}
</style>
</head>
<body>
<nav class="breadcrumb"><a class="btn btn-success radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新"><i class="Hui-iconfont"></i></a></nav>
<div class="page-container">
	<div class="text-c">
		<input type="text"  id="container_title" placeholder="标题" style="width:250px" class="input-text">
		<button class="btn btn-success" onclick="init(1);"><i class="Hui-iconfont">&#xe665;</i> 搜索条件</button>
	</div>
	<div class="cl pd-5 bg-1 bk-gray mt-20"> 
		<span class="l"> 
			<a style="margin-right: 10px;" class="btn btn-primary radius"  onclick="article_add('资讯添加','editArticlePanel')" href="javascript:;"><i class="Hui-iconfont">&#xe600;</i> 添加发布信息</a>
			<!--<a class="btn btn-primary radius"  onclick="checkArticle()" href="javascript:;"><i class="Hui-iconfont">&#xe600;</i> 生成素材</a>-->
		</span>
	</div>
	<div class="mt-20">
		<ul class="createSelect">
			
		</ul>
		<div style="clear: both;"></div>
		<!--<table class="table table-border table-bordered table-bg table-hover table-sort">
			<thead>
				<tr class="text-c">
					<th width="25"><input type="checkbox"  onclick="saveAllBox(this);" value=""></th>
					<th width="80">ID</th>
					<th>标题</th>
					<th width="120">更新时间</th>
					<th width="60">发布状态</th>
					<th width="350">操作</th>
				</tr>
			</thead>
			<tbody class="queryTbody">
			</tbody>
		</table>-->
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
<!--<script type="text/javascript" src="static/js/pc/datatables/1.10.0/jquery.dataTables.min.js"></script>--> 
<script type="text/javascript" src="static/js/publicAjax.js"></script>
<script type="text/javascript" src="static/js/page.js"></script>
<script src="static/js/public.js"></script>
<script type="text/javascript">


var judeUser = function(list){
	var str = '';
	switch (list){
		case "RT001":
			str += '<td class="td-status"><span class="label label-primary radius">制单中</span></td>';
			break;
		case "RT002":
			str += '<td class="td-status"><span class="label label-primary radius ">已提交</span></td>';
			break;
		case "RT003":
			str += '<td class="td-status"><span class="label label-success radius">审批通过</span></td>';
			break;
	}
	
}

/**

 * 删除数组中某个元素

 * @param {Object} _array 数组

 * @param {Object} _item  数组中的值

 * @param {Object} delList 要删除的值或下标

 */

var delArray = function(_array,_item,delList){
	$.each(_array,function(index,item){  

		if(_item != null){

			//对比值

        	if(item == delList){

       	        _array.splice(index,1);

    	    }

		}else{

			//对比索引值

			if(index == delList){

       	        _array.splice(index,1);

    	    }

		}

	});

}

/*保存选中的文章数据*/
var storageArr = new Array();
var saveAllBox = function(_this){
	if($(_this).is(":checked")){
		for(var i = 0 ; i <$(".checkBox").length;i++){
			if(isstorage($(".checkBox").eq(i).val()) == true){
				storageArr.push($(".checkBox").eq(i).val());
			}
		}
	}else{
		for(var i = 0 ; i <$(".checkBox").length;i++){
			delArray(storageArr,0,$(".checkBox").eq(i).val());
		}
	}
}

var saveBox = function(_this){
	if($(_this).val() != ""){
		if(isstorage($(_this).val()) == true){
			storageArr.push($(_this).val());
		}
	}
}


/*判断该参数是否已经保存过*/
var isstorage = function(_storageArr){
	var is = true;
	if(storageArr.length > 0){
		for(var i = 0 ; i < storageArr.length; i++){
			if(storageArr[i] == _storageArr){
				is =  false;
			}
		}
	}else{
		is = true;
	}
	return is ;
}

/*反显选中的文章*/
var checkBoxFun = function(){
	$("input[type=checkbox]").each(function(){
		$(this).prop("checked",false);
	})
//	if(storageArr.length > 0){
//		$("input[type=checkbox]").each(function(){
//			$(this).prop("checked",false);
//		})
//		
//		
//		$(".checkBox").each(function(){
//			if(isstorage($(this).val()) == false){
//				$(this).prop("checked",true);
//			}
//		})
//	}else{
//		
//	}
	
}

var getWordbookArr = new Array();
var getWordbookList = function(){
	jsonDataList.url = 'queryWordbook';
	jsonDataList.data = {parentsBookCode:"H001"};
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


/*获取群发状态 */
var getMassArr = new Array();
var getMasskList = function(){
	jsonDataList.url = 'queryWordbook';
	jsonDataList.data = {parentsBookCode:"H003"};
	var res = showAjaxFun(jsonDataList,"");
	if(res.flag == "1"){
		if(res.data.length > 0){
			getMassArr = res.data
		}
	}
}

var getMassSign = function(_name){
	var is = "";
	for (idx in getMassArr) {
		var li = getMassArr[idx];
		if(li["wordbookCode"] == _name){
			return li;
		}
	}
	return is;
}

var currentPage = 1,pageSize = 10 ;
var loc ="";
var init = function(page){
	var url = window.location.search;
	loc = url.substring(url.lastIndexOf('=')+1, url.length);
	currentPage = page;
	jsonDataList.url = "findArticle";
	jsonDataList.data = {
		title:$("#container_title").val(),//标题 
		pageNo:currentPage,//当前页 
		pageSize:pageSize,//当前页显示数量
		articleType:loc
	};
	var res = showAjaxFun(jsonDataList,"");
	if(res.flag == "1"){
		$(".queryTbody").children().remove();
		var str = '', li = res.data.list;
		$("#page").children().remove();
		$(".dataTables_info").html("");
		if(li.length > 0 ){
			for(var i=0 ; i<li.length;i++){
//				str += '<tr class="text-c">';
//					str += '<td><input class="checkBox" type="checkbox" value="'+li[i]["articleId"]+'" name="" onclick="saveBox(this)"></td>';
//					str += '<td>'+(i+1)+'</td>';
//					str += '<td>'+li[i]["title"]+'</td>';
//					str += '<td>'+conversionTime(li[i]["createDate"])+'</td>';
//					if(getWordbookArr.length > 0){
//						var arr = getWordbookSign(li[i]["status"]);
//						str += '<td class="td-status"><span class="label label-primary radius">'+arr["wordbookName"]+'</span></td>';
//					
//						str += '<td class="f-14 td-manage">';
//							str += '<button class="btn btn-primary  radius m_r10" onclick="creatUrl(\''+li[i]["articleId"]+'\');">生成URL</button>';
//							if(arr["wordbookCode"] == "H001001"){
//								str += '<button class="btn btn-success radius m_r10" onclick="article_shenqing(\'审批\',\''+li[i]["articleId"]+'\',\'H001003\');">审批通过</button>';  //领导才能审判
//								str += '<button class="btn btn-primary  radius m_r10" onclick="article_edit(\'修改\',\'editArticlePanel\',\''+li[i]["articleId"]+'\');">修改</button>';
//								str += '<button class="btn btn-danger radius m_r10" onclick="article_del(\''+li[i]["articleId"]+'\');">删除</button>';
//							}else{
//								str += '<button class="btn btn-primary radius m_r10" onclick="article_shenqing(\'审批\',\''+li[i]["articleId"]+'\',\'H001001\');">审批不通过</button>';  //领导才能审判
//							}
//						str += '</td>';
//					}
//				str += '</tr>';
				str += '<li>';
					var arr = getWordbookSign(li[i]["status"]);
					var sendFlagArr = getMassSign(li[i]["sendFlag"]);
					str += '<div style="margin-bottom:10px; border-bottom: 1px solid #000;">';
						str += '<h5>更新于:'+conversionTime(li[i]["createDate"])+'</h5>';
						str += '<h5>审批状态:<span style="color:#5a98de;">'+arr["wordbookName"]+'<span></h5>';
						if(sendFlagArr["wordbookCode"] == "H003002"){
							str += '<h5>群发状态:<span style="color:#5eb95e;">'+sendFlagArr["wordbookName"]+'</span></h5>';
						}else{
							str += '<h5>群发状态:<span style="color:red;">'+sendFlagArr["wordbookName"]+'</span></h5>';
						}
						
						str += '<h4>'+li[i]["title"]+'</h4>';
						str += '<div style="padding: 0; margin: auto;"><img src="'+li[i]["wxPicUrl"]+'" class="w_All" style="min-width:178px; height:167px;"/></div>';
						str += '<div class="describe" title="'+li[i]["wxContent"]+'">'+li[i]["wxContent"]+'</div>';
					str += '</div>';
					str += '<div class="btnDiv">';
						str += '<button class="btn btn-primary  radius" onclick="creatUrl(\''+li[i]["articleId"]+'\');">生成URL</button>';
						if(arr["wordbookCode"] == "H001001"){
							str += '<button class="btn btn-success radius " onclick="article_shenqing(\'审批\',\''+li[i]["articleId"]+'\',\'H001003\');">审批通过</button>';  //领导才能审判
							str += '<button class="btn btn-primary  radius " onclick="article_edit(\'修改\',\'editArticlePanel\',\''+li[i]["articleId"]+'\');">修改</button>';
							str += '<button class="btn btn-danger radius " onclick="article_del(\''+li[i]["articleId"]+'\');">删除</button>';
						}else{
							str += '<button class="btn btn-primary radius " onclick="article_shenqing(\'审批\',\''+li[i]["articleId"]+'\',\'H001001\');">审批不通过</button>';  //领导才能审判
						}
						str += '</div>';
					str += '<div style="clear: both;"></div>';
				str += '</li>';

			}
//			$(".queryTbody").html(str);
			$(".createSelect").html(str);

			setPagination("page", Math.ceil(res["data"]["totalRecords"]/pageSize), currentPage, "init",res["data"]["totalRecords"],pageSize);
		}
	}else{
		alert(res.Message);
	}
}

var init_fun = function(){
	getWordbookList();
	getMasskList();
	init(1);
}
 
$(function(){
	init_fun();
})

/*资讯-添加*/
function article_add(title,url,w,h){
	var index = layer.open({
		type: 2,
		title: title,
		content: url,
		success: function(layero, index){
			var iframeWin = window[layero.find('iframe')[0]['name']]; 
			iframeWin.searchLayer2(loc);
		}
	});
	layer.full(index);
}
/*编辑文章*/
function article_edit(title,url,id){
	var index = layer.open({
		type: 2,
		title: title,
		content: url,
		success: function(layero, index){
			var iframeWin = window[layero.find('iframe')[0]['name']]; 
			iframeWin.searchLayer2(loc);
			iframeWin.initLayer(id);
		}
	});
	layer.full(index);
}
/*删除文章*/
function article_del(id){
	layer.confirm('确认要删除吗？',function(index){
		jsonDataList.url = "deleteArticle";
		jsonDataList.data = {
			articleId:id//传articleId主键
		};
		var res = showAjaxFun(jsonDataList,"");
		layer.msg(res.message,{icon:1,time:3000});
		if(res.flag == "1"){
			init(currentPage);
		}
	});
}

/*提交或审批上线*/
function article_shenqing(obj,_id,_status){
	layer.confirm('确认要'+obj+'吗？',function(index){
		jsonDataList.url = "changeArticleType";
		jsonDataList.data = {
			articleId:_id,//传articleId主键
			status:_status//状态
		};
		var res = showAjaxFun(jsonDataList,"");
		layer.msg(res.message,{icon:1,time:3000});
		if(res.flag == "1"){
			init(currentPage);
		}
	});
}


//var checkArticle = function(){
//	var boxArray = new Array(),boxStr = '';
//	$(".checkBox").each(function(){
//		if($(this).is(":checked")){
//			if(boxStr.length > 0 ){boxStr += ','}
//			boxArray.push($(this).val())
//			boxStr += $(this).val();
//		}
//	})
//	
//	if(boxArray.length > 8){
//		layer.alert("您所选择的数量已超出，所选择的文章不能超过八个！");    
//	}else if(boxArray.length > 0){
//		jsonDataList.url = "checkArticleBuildMaterial";
//		jsonDataList.data = {
//			articleIdArray:boxStr
//		};
//		var res = showAjaxFun(jsonDataList,"");
//		if(res.flag == "1"){
//			create(boxStr);
//		}else if(res.flag == "2"){
//			layer.confirm('确认要'+res.message+'吗？',function(index){
//				create(boxStr);
//			});
//		}else if(res.flag == "3"){
//			layer.alert(res.message);    
//		}
//	}else{
//		layer.alert("请选择下列文章！");    
//	}
//}

/*生成图文素材*/
//var create = function(_boxArray){
//	jsonDataList.url = "createMaterial";
//	jsonDataList.data = {
//		articleIdArray:_boxArray
//	};
//	var res = showAjaxFun(jsonDataList,"");
//	alert(res.message);    
//	if(res.flag == "1"){
//		init(1);
//	}
//}

/*生成文章链接*/
var creatUrl = function(_id){
	jsonDataList.url = "getArticleNetwordURL";
	jsonDataList.data = {
		articleId:_id,//传articleId主键
	};
	var res = showAjaxFun(jsonDataList,"");
	if(res.flag =="1"){
		layer.alert(res.data);    
	}
}
</script> 
</body>
</html>