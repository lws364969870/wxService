<%@page import="com.lws.domain.model.SessionData"%>
<%@page import="com.lws.domain.utils.request.CryptUtil"%>
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

String defaulKey =CryptUtil.getDefaultSessionKey();
String defaulIv =CryptUtil.getDefaultSessionIv();
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
	.btnDiv .btn{float: left; margin: 8px;}
	.btn, .btn.size-M {  padding: 4px 10px;}
	input[name=pushCheck],input[name=checknAll]{cursor: pointer;}
</style>
</head>
<body style="min-width: 1300px;">
<nav class="breadcrumb"><a class="btn btn-success radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新"><i class="Hui-iconfont"></i></a></nav>
<div class="page-container">
	<div class="text-c">
		<input type="text"  id="container_title" placeholder="标题" style="width:250px" class="input-text">
		<button class="btn btn-success" onclick="init(1);"><i class="Hui-iconfont">&#xe665;</i> 搜索</button>
	</div>
	<div class="cl pd-5 bg-1 bk-gray mt-20"> 
		<span class="l"> 
			<a style="margin-right: 10px;" class="btn btn-primary radius"  onclick="article_add('资讯添加','editArticlePanel')" href="javascript:;"><i class="Hui-iconfont">&#xe600;</i> 添加发布信息</a>
			<!--<a class="btn btn-primary radius"  onclick="checkArticle()" href="javascript:;"><i class="Hui-iconfont">&#xe600;</i> 生成素材</a>-->
		</span>
	</div>
	<div class="mt-20">
		<ul class="createSelect"></ul>
		<div style="clear: both;"></div>
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
<script src="static/js/public.js"></script>
<script type="text/javascript">

var userKey = new Object();
	userKey.key = "<%=sessionKey%>"
	userKey.iv = "<%=sessionIv%>"
var defaulKey = new Object();	 
	defaulKey.key  = "<%=defaulKey%>"
	defaulKey.iv = "<%=defaulIv%>"
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


var getWordbookArr = new Array();
var getWordbookList = function(){
	jsonDataList.url = 'queryWordbook';
	jsonDataList.data = encryptionFun({parentsBookCode:"H001"},defaulKey.key,defaulKey.iv);
	jsonDataList.dataType = "text";
	var res = showAjaxFun(jsonDataList,"");
		res =  decryptionFun(res,defaulKey.key,defaulKey.iv);
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
	jsonDataList.data = encryptionFun({parentsBookCode:"H003"},defaulKey.key,defaulKey.iv);
	jsonDataList.dataType = "text";
	var res = showAjaxFun(jsonDataList,"");
		res =  decryptionFun(res,defaulKey.key,defaulKey.iv);
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
	var obj = {
		title:$("#container_title").val(),//标题 
		pageNo:currentPage,//当前页 
		pageSize:pageSize,//当前页显示数量
		articleType:loc
	};
	jsonDataList.data = encryptionFun(obj,userKey.key,userKey.iv);
	jsonDataList.dataType = "text";
	var res = showAjaxFun(jsonDataList,"");
		res =  decryptionFun(res,userKey.key,userKey.iv);
	if(res.flag == "1"){
		$(".createSelect").children().remove();
		var str = '', li = res.data.list;
		$("#page").children().remove();
		$(".dataTables_info").html("");
		if(li.length > 0 ){
			for(var i=0 ; i<li.length;i++){
				str += '<li>';
					var arr = getWordbookSign(li[i]["status"]);
					var sendFlagArr = getMassSign(li[i]["sendFlag"]);
					str += '<div style="margin-bottom:10px; border-bottom: 1px solid #000;">';
						str += '<h5>更新于:'+conversionTime(li[i]["createDate"]["time"])+'</h5>';
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
							str += '<button class="btn btn-success radius " onclick="article_shenqing(\'审批\',\''+li[i]["articleId"]+'\',\'H001003\');">审批通过</button>';  
							str += '<button class="btn btn-primary  radius " onclick="article_edit(\'修改\',\'editArticlePanel\',\''+li[i]["articleId"]+'\');">修改</button>';
							str += '<button class="btn btn-danger radius " onclick="article_del(\''+li[i]["articleId"]+'\');">删除</button>';
						}
						str += '<button class="btn btn-primary radius " onclick="preview_push(\''+li[i]["articleId"]+'\');">推送预览</button>'; 
						if(sendFlagArr["wordbookCode"] == "H003001"){
							str += '<button class="btn btn-danger radius " onclick="massTexting(\''+li[i]["articleId"]+'\');">群发</button>';
							str += '<button class="btn btn-primary radius " onclick="article_shenqing(\'审批\',\''+li[i]["articleId"]+'\',\'H001001\');">审批不通过</button>';
						}
						str += '</div>';
					str += '<div style="clear: both;"></div>';
				str += '</li>';

			}
			$(".createSelect").html(str);
			setPagination("page", Math.ceil(res["data"]["totalRecords"]/pageSize), currentPage, "init",res["data"]["totalRecords"],pageSize);
		}
	}else{
		layer.alert(res.Message);
	}
}

var massTexting = function(str_id){
	jsonDataList.url = "massMaterial";
	jsonDataList.data = encryptionFun({articleId:str_id},userKey.key,userKey.iv);
	jsonDataList.dataType = "text";
	var res = showAjaxFun(jsonDataList,"");
	res =  decryptionFun(res,userKey.key,userKey.iv);
	layer.msg(res.message,{icon:1,time:3000});
	if(res.flag == "1"){
		init(currentPage);
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
		jsonDataList.data = encryptionFun({articleId:id},userKey.key,userKey.iv);
		jsonDataList.dataType = "text";
		var res = showAjaxFun(jsonDataList,"");
			res =  decryptionFun(res,userKey.key,userKey.iv);
		layer.msg(res.message,{icon:1,time:3000});
		if(res.flag == "1"){
			init(currentPage);
		}
	});
}

/*提交或审批上线*/
//function article_shenqing(obj,_id,_status){
//	layer.confirm('确认要'+obj+'吗？',function(index){
//		jsonDataList.url = "changeArticleType";
//		var objList = {
//			articleId:_id,//传articleId主键
//			status:_status//状态
//		};
//		jsonDataList.data = encryptionFun(objList,userKey.key,userKey.iv);
//		jsonDataList.dataType = "text";
//		var res = showAjaxFun(jsonDataList,"");
//			res =  decryptionFun(res,userKey.key,userKey.iv);
//		layer.msg(res.message,{icon:1,time:3000});
//		if(res.flag == "1"){
//			init(currentPage);
//		}
//	});
//}


function article_shenqing(obj,_id,_status){
	layer.confirm('确认要'+obj+'吗？',function(index){
		jsonDataList.url = "changeArticleStatus";
		var objList = {
			articleId:_id,//传articleId主键
			status:_status//状态
		};
		jsonDataList.data = encryptionFun(objList,userKey.key,userKey.iv);
		jsonDataList.dataType = "text";
		var res = showAjaxFun(jsonDataList,"");
			res =  decryptionFun(res,userKey.key,userKey.iv);
		layer.msg(res.message,{icon:1,time:3000});
		if(res.flag == "1"){
			init(currentPage);
		}
	});
}




/*生成文章链接*/
var creatUrl = function(_id){
	jsonDataList.url = "getArticleNetwordURL";
	jsonDataList.data = encryptionFun({articleId:_id},userKey.key,userKey.iv);
	jsonDataList.dataType = "text";
	var res = showAjaxFun(jsonDataList,"");
		res =  decryptionFun(res,userKey.key,userKey.iv);
	if(res.flag =="1"){
		layer.alert(res.data);    
	}
}
/*推送预览*/
var preview_push = function(o_articleId){
	var str = '<div style="padding:20px;">';
			str += '<table class="table table-border table-bordered table-bg table-hover table-sort">';
				str += '<thead>';
					str += '<tr class="text-c">';
						str += '<th width="30"><input type="checkbox" name="checknAll"/></th>';
						str += '<th width="120">用户昵称</th>';
						str += '<th width="120">性别</th>';
						str += '<th width="120">粉丝备注</th>';
						str += '<th width="170">openid</th>';
						str += '<th width="100">用户头像</th>';
					str += '</tr>';
				str += '</thead>';
				str += '<tbody class="pushTbody"></tbody>';
			str += '</table>';
		str += '</div>';
	
	layer.open({
		type: 1,
		title: "选择预览人群",
		content: str,
		area:["1000px","500px"],
		btn:["确定","关闭"],
		success: function(layero, index){
			jsonDataList.url = "querySubscribe";
			jsonDataList.data = encryptionFun({},userKey.key,userKey.iv);
			jsonDataList.dataType = "text";
			var res = showAjaxFun(jsonDataList,"");
			res =  decryptionFun(res,userKey.key,userKey.iv);
			$(".pushTbody").children().remove();
			var strlist = '' , _list = res.data.list;
			if(_list.length > 0){
				for(var i = 0; i <_list.length;i++){
					strlist += '<tr>';
						strlist += '<td class="text-c"><input type="checkbox" name="pushCheck" value="'+_list[i]["openid"]+'"/></td>';
						strlist += '<td class="text-c">'+_list[i]["nickname"]+'</td>';
						strlist += '<td class="text-c">'+_list[i]["sex"]+'</td>';
						strlist += '<td class="text-c">'+_list[i]["remark"]+'</td>';
						strlist += '<td class="text-c">'+_list[i]["openid"]+'</td>';
						strlist += '<td class="text-c"><img style="width:50px; height:50px;" src="'+_list[i]["headimgurl"]+'"/></td>';
					strlist += '<tr>';
				}
				$(".pushTbody").html(strlist);
			}
		},
		yes:function(index, layero){
			var openid_val = '';
			$("input[name=pushCheck]").each(function(){
				if($(this).is(":checked") == true){
					if(openid_val.length > 0 ){openid_val+=','};
					openid_val += $(this).val();
				}
			})
			if(openid_val == ""){layer.alert("请选择推送预览人员！")}
			jsonDataList.url = "examinArticle";
			jsonDataList.data = encryptionFun({articleId:o_articleId,openid:openid_val},userKey.key,userKey.iv);
			jsonDataList.dataType = "text";
			var resList = showAjaxFun(jsonDataList,"");
				resList =  decryptionFun(resList,userKey.key,userKey.iv);
			parent.layer.alert(resList.message,function(index){
				if(resList.flag == 1){
					parent.layer.close(index);
					layer.closeAll();
					init(1);
				}else{
					parent.layer.close(index);
				}
			});
		}
	});
}

$("input[name='checknAll']").click(function() {  
		if($("input[name='checknAll']").is(":checked") == true){
			$("input[name='pushCheck']").each(function() {  
	            $(this).prop("checked", true); 
	        }); 
		}else{
			$("input[name='pushCheck']").each(function() {  
	            $(this).prop("checked", false);
	        }); 
		}
    }); 
</script> 
</body>
</html>