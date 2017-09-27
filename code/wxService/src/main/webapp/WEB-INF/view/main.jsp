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

String sessionUserId = sessionData.getUserId();
%>
<!DOCTYPE HTML>
<html>
	<head>

		<meta charset="utf-8">
		<meta name="renderer" content="webkit|ie-comp|ie-stand">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
		<meta http-equiv="Cache-Control" content="no-siteapp" />
		<link rel="Bookmark" href="/favicon.ico">
		<link rel="Shortcut Icon" href="/favicon.ico" />
		<!--[if lt IE 9]>
<script type="text/javascript" src="lib/html5shiv.js"></script>
<script type="text/javascript" src="lib/respond.min.js"></script>
<![endif]-->

		<link rel="stylesheet" type="text/css" href="static/js/h-ui/css/H-ui.min.css" />
		<link rel="stylesheet" type="text/css" href="static/js/h-ui.admin/css/H-ui.admin.css" />
		<link rel="stylesheet" type="text/css" href="static/js/pc/Hui-iconfont/1.0.8/iconfont.css" />
		<link rel="stylesheet" type="text/css" href="static/js/h-ui.admin/skin/default/skin.css" id="skin" />
		<!--[if IE 6]>
<script type="text/javascript" src="lib/DD_belatedPNG_0.0.8a-min.js" ></script>
<script>DD_belatedPNG.fix('*');</script>
<![endif]-->
		<title>佛山市统计后台登录</title>
		<!--_footer 作为公共模版分离出去-->
		<script type="text/javascript" src="static/js/pc/jquery/1.9.1/jquery.min.js"></script>
		<script type="text/javascript" src="static/js/pc/layer/2.4/layer.js"></script>
		<script type="text/javascript" src="static/js/h-ui/js/H-ui.min.js"></script>
		<script type="text/javascript" src="static/js//h-ui.admin/js/H-ui.admin.js"></script>
		<!--/_footer 作为公共模版分离出去-->
		<style type="text/css">
			.show {
				display: block !important;
			}
			
			.Hui-aside .menu_dropdown li a.blue{color: #148cf1;}
		</style>
	</head>

	<body>
		<header class="navbar-wrapper">
			<div class="navbar navbar-fixed-top">
				<div class="container-fluid cl"><span class="logo navbar-logo f-l mr-10 hidden-xs">佛山市统计后台登录</span>
					<a aria-hidden="false" class="nav-toggle Hui-iconfont visible-xs" href="javascript:;">&#xe667;</a>
					<nav id="Hui-userbar" class="nav navbar-nav navbar-userbar hidden-xs">
						<ul class="cl">
							<li>互联网开关</li>
							<li class="dropDown dropDown_hover networkShow">
								<!--<a href="#" class="networkShow">开 <i class="Hui-iconfont">&#xe6d5;</i></a>-->
								<!--<ul class="dropDown-menu  radius box-shadow">
									<li onClick="network">
										<a href="#">关</a>
									</li>
								</ul>-->
							</li>
							<!--<li class="dropDown dropDown_hover" style="margin-right: 10px;">网络开关：<label><input class="mui-switch" type="checkbox"></label></li>-->
							<li class="dropDown_UserPer">超级管理员</li>
							<li class="dropDown dropDown_hover">
								<a href="#" class="dropDown_A dropDown_User">admin <i class="Hui-iconfont">&#xe6d5;</i></a>
								<ul class="dropDown-menu menu radius box-shadow">
									<!--<li><a href="javascript:;" onClick="myselfinfo()">个人信息</a></li>
							<li><a href="#">切换账户</a></li>-->
									<li onclick="updatePwd();"><a href="#">修改密码</a></li>
									<li onclick="updateInfo();"><a href="#">修改信息</a></li>
									<li onclick="out();"><a href="#">退出</a></li>
								</ul>
							</li>
							<li id="Hui-skin" class="dropDown right dropDown_hover">
								<a href="javascript:;" class="dropDown_A" title="换肤"><i class="Hui-iconfont" style="font-size:18px">&#xe62a;</i></a>
								<ul class="dropDown-menu menu radius box-shadow">
									<li>
										<a href="javascript:;" data-val="default" title="默认（黑色）">默认（黑色）</a>
									</li>
									<li>
										<a href="javascript:;" data-val="blue" title="蓝色">蓝色</a>
									</li>
									<li>
										<a href="javascript:;" data-val="green" title="绿色">绿色</a>
									</li>
									<li>
										<a href="javascript:;" data-val="red" title="红色">红色</a>
									</li>
									<li>
										<a href="javascript:;" data-val="yellow" title="黄色">黄色</a>
									</li>
									<li>
										<a href="javascript:;" data-val="orange" title="橙色">橙色</a>
									</li>
								</ul>
							</li>
						</ul>
						<script type="text/javascript">
							var roleType = "<%=sessionRoleType%>";
							var roleName = "<%=sessionRoleName%>";
							var loginName = "<%=sessionLoginName%>";
							$(".dropDown_UserPer").html('<span style="margin-right: 10px;">|</span>'+roleName);
							$(".dropDown_User").html(loginName + '<i class="Hui-iconfont">&#xe6d5;</i>');
						</script>
					</nav>
				</div>
			</div>
		</header>
		<aside class="Hui-aside">
			<div class="menu_dropdown bk_2">
			
			</div>
		</aside>
		<div class="dislpayArrow hidden-xs">
			<a class="pngfix" href="javascript:void(0);" onClick="displaynavbar(this)"></a>
		</div>
		<section class="Hui-article-box">
			<div id="Hui-tabNav" class="Hui-tabNav hidden-xs">
				<div class="Hui-tabNav-wp">
					<ul id="min_title_list" class="acrossTab cl">
						<li class="active">
							<span title="微信菜单管理" data-href="queryArticlePanel">微信菜单管理</span>
							<em></em></li>
					</ul>
				</div>
				<div class="Hui-tabNav-more btn-group">
					<a id="js-tabNav-prev" class="btn radius btn-default size-S" href="javascript:;"><i class="Hui-iconfont">&#xe6d4;</i></a>
					<a id="js-tabNav-next" class="btn radius btn-default size-S" href="javascript:;"><i class="Hui-iconfont">&#xe6d7;</i></a>
				</div>
			</div>
			<div id="iframe_box" class="Hui-article">
				<div class="show_iframe">
					<div style="display:none" class="loading"></div>
					<iframe scrolling="yes" frameborder="0" src="queryWxMenuPanel"></iframe>
				</div>
			</div>
		</section>

		<div class="contextMenu" id="Huiadminmenu">
			<ul>
				<li id="closethis">关闭当前 </li>
				<li id="closeall">关闭全部 </li>
			</ul>
		</div>
		
		<script type="text/javascript" src="static/js/aes.js"></script>
		<script type="text/javascript" src="static/js/tf_aes.js"></script>
		<script type="text/javascript" src="static/js/publicAjax.js"></script>
		<script type="text/javascript" src="static/js/jquery.cookie.js"></script>
		<script type="text/javascript" src="static/js/pc/jquery.contextmenu/jquery.contextmenu.r2.js"></script>
		<script type="text/javascript">

			var userKey = new Object();
				userKey.key = "<%=sessionKey%>"
				userKey.iv = "<%=sessionIv%>"
				userKey.userid = "<%=sessionUserId%>"
				
			/*修改密码*/
			var updatePwd = function(){
				var index = layer.open({
					type: 2,
					title: '修改用户密码',
					content: ["editUserPanel","no"],
					area:['320px','230px'],
					btn:["确认","关闭"],
					resize :false,
					move :false,
					success: function(layero, index){
						var iframeWin = window[layero.find('iframe')[0]['name']]; 
						iframeWin.updatePwd(userKey.userid);
					},
					yes: function(index, layero){
						var iframeWin = window[layero.find('iframe')[0]['name']]; 
						var pwd = iframeWin.updatePwd2();
						if(pwd != undefined){
							parent.layer.alert(pwd.message,function(index){
								if(pwd.flag == "1"){
									out();
								}
								layer.close(index);
							});
						}
					}
				});
			}
			
			/*修改用户信息*/
			var updateInfo = function(){
				var index = layer.open({
					type: 2,
					title: '修改用户信息',
					content: ["editUserPanel","no"],
					area:['320px','405px'],
					btn:["保存","关闭"],
					resize :false,
					move :false,
					success: function(layero, index){
						var iframeWin = window[layero.find('iframe')[0]['name']]; 
						iframeWin.initLayer(userKey.userid);
					},
					yes: function(index, layero){
						var iframeWin = window[layero.find('iframe')[0]['name']]; 
						var returnInfo = iframeWin.updateLayer();
						console.log(returnInfo)
						if(returnInfo != undefined){
							parent.layer.alert(returnInfo.message,{
								btn:["确定"],
								yes: function(index, layero){
									parent.layer.close(index);
									if(returnInfo.flag == '1'){
										window.location.href = 'main';
									}
								}
							});
						}
					}
				});
			}
			
			/*退出登陆*/
			var out = function() {
				jsonDataList.url = "loginOut";
				var res = showAjaxFun(jsonDataList, "");
				if(res !== null) {
					$.cookie("login", null);
					window.location.href = "login";
				}
			}
			
			/*显示菜单*/
			var initLoad = function() {
				jsonDataList.url = "queryMenu";
				jsonDataList.data = encryptionFun({},userKey.key,userKey.iv);
				jsonDataList.dataType = "text";
				var res = showAjaxFun(jsonDataList, "");
					res =  decryptionFun(res,userKey.key,userKey.iv);
				if(res.flag == "1") {
					if(res.data.length > 0) {
						res = res.data;
						var str = '',
							menuCode = '',
							arrList = new Array();
						for(idx in res) {
							if(res[idx]["isHide"] == "N") {
								if(res[idx]["menuLevel"] == 1) {
									str += '<dl id="menu-member' + res[idx]["menuCode"] + '">';
										str += '<dt onclick="showChildrenMenu(this)"><i class="Hui-iconfont">&#xe60d;</i> ' + res[idx]["menuTitle"] + '<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i></dt>';
										str += '<dd>';
											str += '<ul class="' + res[idx]["menuCode"] + '">';
											str += '</ul>';
										str += '</dd>';
									str += '</dl>';
									$(".menu_dropdown").append(str);
									str = '';
								} else if(res[idx]["menuLevel"] == 2) {
									if($(".menu_dropdown ul").hasClass(res[idx]["parentsCode"])) {
										str += '<li onclick="upload(this);"><a data-href="' + res[idx]["menuUrl"] + '" data-title="' + res[idx]["menuTitle"] + '" href="javascript:void(0)">' + res[idx]["menuTitle"] + '</a></li>';
									}
									$("." + res[idx]["parentsCode"]).append(str);
									str = '';
								}
							}
						}
					}
				} else {
					layer.alert(res.message);
				}
			}
			
			/*查询网络状态*/
			var findByNetWork = function(){
				jsonDataList.url = "findByNetWork";
				jsonDataList.data = encryptionFun({},userKey.key,userKey.iv);
				jsonDataList.dataType = "text";
				var res = showAjaxFun(jsonDataList, ""), str = '';
					res =  decryptionFun(res,userKey.key,userKey.iv);
				if(res.flag == "1"){
					if(res.data == "Y"){
						str += '<a href="#">开 <i class="Hui-iconfont">&#xe6d5;</i></a>';
						str += '<ul class="dropDown-menu  radius box-shadow">';
							str += '<li onclick="updateNetwork(\'N\');"><a href="#">关</a></li>';
						str += '</ul>';
					}else if(res.data == "N"){
						str += '<a href="#">关 <i class="Hui-iconfont">&#xe6d5;</i></a>';
						str += '<ul class="dropDown-menu  radius box-shadow">';
							str += '<li onclick="updateNetwork(\'Y\');"><a href="#">开</a></li>';
						str += '</ul>';
					}
					$(".networkShow").html(str);
				}
			}
			
			/*更改网络开关状态*/
			var updateNetwork = function(type){
				var _type = "";
				if(type == "Y"){
					_type = "打开";
				}else{
					_type = "关闭";
				}
				layer.confirm('是否要'+_type+'网络？', {icon: 5, title:'提示'}, function(index){
				  	jsonDataList.url = "updateByNetWork";
				  	jsonDataList.data = encryptionFun({configValue:type},userKey.key,userKey.iv);
					jsonDataList.dataType = "text";
					var res = showAjaxFun(jsonDataList, "");
						res =  decryptionFun(res,userKey.key,userKey.iv);
					layer.alert(res.message);    
					if(res.flag == "1") {
						findByNetWork();
					}
				    layer.close(index);
				});
			}
			
			/*显示二级菜单*/
			var showChildrenMenu= function(_this) {
				if($(_this).hasClass("selected")){
					$(_this).removeClass("selected");
					$(_this).next("dd").removeClass("show");
				}else{
					$(".menu_dropdown dl").children("dt").removeClass("selected");
					$(".menu_dropdown dl").children("dd").removeClass("show");
					$(_this).addClass("selected");
					$(_this).next("dd").addClass("show");
				}
			}
			
			var upload = function(str_this){
				$(".menu_dropdown li a").removeClass("blue");
				$(str_this).children().addClass("blue");
			}
			
			var alertFun = function(){
				layer.alert("该功能暂未开放！");    
			}
			
			
			$(function() {
				initLoad();
				findByNetWork();
			})
		</script>
	</body>

</html>