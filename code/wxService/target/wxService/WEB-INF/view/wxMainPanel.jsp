<%@ page language="java" contentType="text/html; charset=UTF-8"  
    pageEncoding="UTF-8"%>  
 <%  
    String path = request.getContextPath();  
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";  
 %>  
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>佛山市统计</title>
		<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="apple-mobile-web-app-status-bar-style" content="black">

		<link rel="stylesheet" href="static/app/css/mui.min.css">
	</head>

	<body>
		<!--<header id="headerID" class="mui-bar mui-bar-nav">
			<h1 id="title" class="mui-title">发布信息</h1>
		</header>-->
		<div class="mui-content"></div>
	</body>
	<script src="static/app/js/mui.min.js"></script>
	<script type="text/javascript">
		//启用双击监听
		mui.init({
			gestureConfig:{
				doubletap:true
			},
			subpages:[{
				url:'wxQueryArticlePanel',
				id:'pullrefresh_sub',
				styles:{
					top: '0px',
					bottom: '0px',
				}
			}]
		});
	
//		var contentWebview = null;
//		document.querySelector('header').addEventListener('doubletap',function () {
//			if(contentWebview==null){
//				contentWebview = plus.webview.currentWebview().children()[0];
//			}
//			contentWebview.evalJS("mui('#pullrefresh').pullRefresh().scrollTo(0,0,100)");
//		});
	</script>

</html>