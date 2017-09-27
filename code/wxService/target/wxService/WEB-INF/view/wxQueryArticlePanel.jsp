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
		<style type="text/css">
			.mui-navigate-right span{
				margin-left: 5px; vertical-align: super;
			}
		</style>
	</head>

	<body>
		<!--下拉刷新容器-->
		<div id="pullrefresh" class="mui-content mui-scroll-wrapper">
			<div class="mui-scroll">
				<!--数据列表-->
				<ul id="list" class="mui-table-view mui-table-view-chevron">
					
				</ul>
			</div>
		</div>
		<script src="static/app/js/jquery-1.12.3.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="static/app/js/publicAjax.js" type="text/javascript" charset="utf-8"></script>
		<script src="static/app/js/mui.min.js"></script>
		
		<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript" charset="utf-8"></script>
		<!--在需要调用JS接口的页面引入如下JS文件，（支持https）：http://res.wx.qq.com/open/js/jweixin-1.0.0.js
		
		请注意，如果你的页面启用了https，务必引入 https://res.wx.qq.com/open/js/jweixin-1.0.0.js ，否则将无法在iOS9.0以上系统中成功使用JSSDK-->
		<script>
			//	微信分享
	

			 
			function Share(shareData) {			//分享接口
			    var JsUrl = {Url: window.location.href}
			    var jsApiList = ['checkJsApi', 'onMenuShareTimeline', 'onMenuShareAppMessage', 'onMenuShareQQ', 'onMenuShareWeibo', 'onMenuShareQZone', 'hideMenuItems', 'showMenuItems', 'hideAllNonBaseMenuItem', 'showAllNonBaseMenuItem', 'translateVoice', 'startRecord', 'stopRecord', 'onRecordEnd', 'playVoice', 'pauseVoice', 'stopVoice', 'uploadVoice', 'downloadVoice', 'chooseImage', 'previewImage', 'uploadImage', 'downloadImage', 'getNetworkType', 'openLocation', 'getLocation', 'hideOptionMenu', 'showOptionMenu', 'closeWindow', 'scanQRCode', 'chooseWXPay', 'openProductSpecificView', 'addCard', 'chooseCard', 'openCard'];
			    if (Share.play != true) {
			        $.ajax({
			            type: 'POST',
			            url: shareData.apiHost,
			            data: JsUrl,
			            success: function (msg) {
			                wx.config({
			                    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
			                    appId: msg.Data.appid, // 必填，公众号的唯一标识
			                    timestamp: msg.Data.timestamp, // 必填，生成签名的时间戳
			                    nonceStr: msg.Data.noncestr, // 必填，生成签名的随机串
			                    signature: msg.Data.signature,// 必填，签名，见附录1
			                    jsApiList: jsApiList // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
			                });
			                wxReady();
			            },
			            error: function (XMLHttpRequest, textStatus, errorThrown) {
			            }
			        });
			        Share.play = true;
			    } else {
			        wxReady();
			    }
			    function wxReady() {
			        wx.ready(function () {
			            if (typeof(shareData.callBack) == 'function') {
			                if (shareData.isCallBack != true) {		//回掉函数  只执行一次
			                    shareData.callBack();
			                    shareData.isCallBack = true;
			                }
			            }
			            //转发朋友圈
			            wx.onMenuShareTimeline({
			                title: shareData.friendtitle,
			                link: shareData.link,
			                imgUrl: shareData.img_url, // 分享图标
			                img_width: "300",
			                img_height: "300",
			                trigger: function () {
			                },
			                success: function () {
			                    MtaH5.clickStat('toShare');
			                    // _hmt.push(['_trackEvent', "share", "分享朋友圈"]);
			                },
			                cancel: function () {
			                }
			            });
			            //分享给朋友
			            wx.onMenuShareAppMessage({
			                title: shareData.title, // 分享标题
			                desc: shareData.describe, // 分享描述
			                link: shareData.link, // 分享链接
			                imgUrl: shareData.img_url, // 分享图标
			                img_width: "300",
			                img_height: "300",
			                trigger: function () {
			                },
			                success: function () {
			                    MtaH5.clickStat('toShareFriend');
			                    //_hmt.push(['_trackEvent', "share", "分享朋友"]);
			                },
			                cancel: function () {
			                }
			            });
			        });
			    }
			}
			
			var shareData_list = {
				 describe:'',		//描述
				 title:'',			//title
				 link:'',			//link
				 apiHost:'http://open.域名.cn/api/WechatCommon',			//apiHost
				 img_url:'',			//分享图片
				 callBack:function(){}	//ready后回调
			};
			
			Share(shareData_list);
			
			
			mui.init({
				pullRefresh: {
					container: '#pullrefresh',
//					down: {
//						height:50,//可选,默认50.触发下拉刷新拖动距离,
//					    auto: false,//可选,默认false.自动下拉刷新一次
//					    contentdown : "下拉可以刷新",//可选，在下拉可刷新状态时，下拉刷新控件上显示的标题内容
//					    contentover : "释放立即刷新",//可选，在释放可刷新状态时，下拉刷新控件上显示的标题内容
//					    contentrefresh : "正在刷新...",//可选，正在刷新状态时，下拉刷新控件上显示的标题内容
						//callback: pulldownRefresh//必选，刷新函数，根据具体业务来编写，比如通过ajax从服务器获取新数据；
//					},
					up: {
						contentrefresh: '正在加载...',
						auto: true,//可选,默认false.自动下拉刷新一次
						callback: pullupRefresh
					}
					
				}
			});
			/**
			 * 下拉刷新具体业务实现
			 */
			function pulldownRefresh() {
				setTimeout(function() {
					var table = document.body.querySelector('.mui-table-view');
					var cells = document.body.querySelectorAll('.mui-table-view-cell');
					for (var i = cells.length, len = i + 3; i < len; i++) {
						var li = document.createElement('li');
						li.className = 'mui-table-view-cell';
						li.innerHTML = '<a class="mui-navigate-right" href="wxEditArticlePanel">Item ' + (i + 1) + '</a>';
						//下拉刷新，新纪录插到最前面；
						table.insertBefore(li, table.firstChild);
					}
					mui('#pullrefresh').pullRefresh().endPulldownToRefresh();  //注意，加载完新数据后，必须执行如下代码，注意：若为ajax请求，则需将如下代码放置在处理完ajax响应数据之后
				}, 1500);
			}
			var count = 0;
			/**
			 * 上拉加载具体业务实现
			 */
			var currentPage = 0,pageSize = 10,totalPage = 1;
			function pullupRefresh() {
				if(currentPage < totalPage){
					currentPage = currentPage + 1;
					jsonDataList.url = "findWxArticle";
					jsonDataList.data = {
						pageNo:currentPage,//当前页 
						pageSize:pageSize//当前页显示数量
					};
					var res = showAjaxFun(jsonDataList,"");
					if(res != null){
						setTimeout(function() {
							mui('#pullrefresh').pullRefresh().endPullupToRefresh((++count > totalPage)); //参数为true代表没有更多数据了。
							var table = document.body.querySelector('.mui-table-view');
							var cells = document.body.querySelectorAll('.mui-table-view-cell');
							var listArr = res.data.list;
							totalPage = Math.ceil(res.data.totalRecords/pageSize);
							if(listArr.length>0){
								for(var i = 0; i < listArr.length;i++ ){
									var li = document.createElement('li');
									li.className = 'mui-table-view-cell';
									li.innerHTML = '<a class="mui-navigate-right" href="wxEditArticlePanel?articleId='+listArr[i]["articleId"]+'"><img style="width: 30px;" src="'+listArr[i]["wxPicUrl"]+'"/><span>' + listArr[i]["title"] + '</span></a>';
									mui('body').on('tap','a',function(){document.location.href=this.href;});  
									table.appendChild(li);
								}
	//							for (var i = cells.length, len = i + 20; i < len; i++) {
	//								var li = document.createElement('li');
	//								li.className = 'mui-table-view-cell';
	//								li.innerHTML = '<a class="mui-navigate-right" href="wx.html"><img style="width: 30px;" src="static/js/img/placeholder-img.jpg"/><span>信访局发布最新消息' + (i + 1) + '</span></a>';
	//								mui('body').on('tap','a',function(){document.location.href=this.href;});  
	//								table.appendChild(li);
	//							}
							}
						}, 500);
					}
				}else{
					mui('#pullrefresh').pullRefresh().endPullupToRefresh(true);
				}
			}

			if (mui.os.plus) {
				mui.plusReady(function() {
					setTimeout(function() {
						mui('#pullrefresh').pullRefresh().pullupLoading();
					}, 1000);

				});
			} else {
				mui.ready(function() {
					mui('#pullrefresh').pullRefresh().pullupLoading();
				});
			}
		</script>
	</body>

</html>