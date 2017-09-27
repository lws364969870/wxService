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
<!DOCTYPE html>  
<html>  
<head>  
<script type="text/javascript">
	var roleType = "<%=sessionRoleType%>";
	var basePath = "<%=basePath%>";  
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">  
<title>佛山市统计后台登录</title>  
<link rel="stylesheet" type="text/css" href="static/js/h-ui/css/H-ui.min.css" />
<link rel="stylesheet" type="text/css" href="static/js/h-ui.admin/css/H-ui.login.css" />
<link rel="stylesheet" type="text/css" href="static/js/pc/Hui-iconfont/1.0.8/iconfont.css" />
<style type="text/css">
	
	
	.preview {
		width: 49%;
		float: left;
	}
	
	.view-bd {
		width: 270px;
		height: 458px;
		padding: 98px 17px 80px 18px;
		background: url(static/js/img/ipone.png) center no-repeat;
		background-size: 100%;
		animation: fadeInDown .3s ease both;
		-webkit-animation: fadeInDown .3s ease both;
		margin-top: 20px;
	}
	
	.rich_media_area_primary {
		position: relative;
		padding: 20px 15px 15px;
		background-color: #fff;
		width: initial;
		height: 422px;
		overflow: auto;
	}
	
	.rich_media_title {
		margin-bottom: 10px;
		line-height: 1.4;
		font-weight: 400;
		font-size: 24px;
	}
	
	.rich_media_meta_list em {
		font-style: normal;
	}
	
	.rich_media_meta_text {
		color: #8c8c8c;
	}
	
	.rich_media_meta {
		display: inline-block;
		vertical-align: middle;
		margin-right: 8px;
		margin-bottom: 10px;
		font-size: 16px;
	}
	
	.wx_content * {
		max-width: 100%!important;
		box-sizing: border-box!important;
		-webkit-box-sizing: border-box!important;
		word-wrap: break-word!important;
	}
	
	/*标题*/
	.inners {
		padding: 10px;
		text-align: left;
	}
	
	.inner .image-cover-container {
		display: inline-block;
		width: 124px;
		margin-left: .2em;
		text-align: center;
	}

	
	.inner .image-cover-container .bg-image {
		width: 120px;
		height: 120px;
		margin: 2px;
		background-repeat: no-repeat;
		background-position: center center;
		background-size: cover;
	}

	.inner .text-container {
		display: inline-block;
		width: 17em;
		margin-left: .5em;
		vertical-align: top;
	}
	
	.inner .image-cover-container .cover {
		position: absolute;
		width: 124px;
		height: 124px;
		margin-top: -124px;
		border: 2px dashed rgba(0,0,0,.5);
		/*background-color: rgba(128,128,0,.1);*/
	}
	
	.inner .image-cover-container .cover .inner {
		position: absolute;
		width: 100%;
		top: 10%;
		color: #fff;
		font-size: 1em;
		text-align: center;
		text-shadow: 0 0 1em #444;
	}
	
	
	.inner .text-container textarea {
		margin-top: .8em;
		height: 5.5em;
	}

	.form-control {
		display: block;
		width: 90%;
		height: 34px;
		padding: 6px 12px;
		font-size: 14px;
		line-height: 1.42857143;
		color: #555;
		background-color: #fff;
		background-image: none;
		border: 1px solid #ccc;
		border-radius: 4px;
		-webkit-box-shadow: inset 0 1px 1px rgba(0,0,0,.075);
		box-shadow: inset 0 1px 1px rgba(0,0,0,.075);
		-webkit-transition: border-color ease-in-out .15s,-webkit-box-shadow ease-in-out .15s;
		-o-transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s;
		transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s;
	}
	
	.hide{display: none;}
	
	.inner_border {
		-webkit-box-sizing: content-box;
		-moz-box-sizing: content-box;
		box-sizing: content-box;
		width: 440px;
		margin-left: 20px;
		margin-bottom: 0;
		border-bottom: 0;
		background-color: #fff;
		border: 1px solid rgba(63,70,82,.5);
		 margin-top: 20px;
	}
	
	#upload{cursor: pointer;}
	
	.authorList{
		display: block;
		width: 100%;
		height: 34px;
		padding: 6px 12px;
		font-size: 14px;
		line-height: 1.42857143;
		color: #555;
		background-color: #fff;
		background-image: none;
		border: 1px solid #ccc;
		border-radius: 4px;
		-webkit-box-shadow: inset 0 1px 1px rgba(0,0,0,.075);
		box-shadow: inset 0 1px 1px rgba(0,0,0,.075);
		-webkit-transition: border-color ease-in-out .15s,-webkit-box-shadow ease-in-out .15s;
		-o-transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s;
		transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s;
		margin-top: 10px;
	}
</style>

<script type="text/javascript" src="static/js/pc/jquery/1.9.1/jquery.min.js" charset="utf-8"></script>
<script type="text/javascript" src="static/js/pc/layer/2.4/layer.js"></script>
<script type="text/javascript" src="static/ueditor/ueditor.config.js"></script>  
<script type="text/javascript" src="static/ueditor/ueditor.all.js"></script>  
<script type="text/javascript" src="static/ueditor/lang/zh-cn/zh-cn.js"></script>  
<script type="text/javascript" src="static/index/index.js"></script>  
</head>  
<body>  
	<input type="hidden" class="editID"/>
	<input type="hidden" class="editDate"/>
	<input type="hidden" class="wxImg"/>
	<input type="hidden" class="articleType"/>
	<input type="hidden" class="thumbMediaId"/>

 

	
	<div class="inner_border" style="float: left; margin-right: 50px;">
		<div id="btns">
			<div style="width: 402px; text-align: center; margin-top: 20px;">
				<button class="btn btn-primary radius" onclick="save();">保存</button>
				<button class="btn btn-primary radius" onclick="previewFun();">预览</button>
			</div>
		</div>
		<div class="inner inners">
			<div class="image-cover-container">
				<img style="width:124px; height:124px" class="wxPic txtValidation imgValidation" src="static/js/img/wxPic.png" data-type="imgStr"  data-value="请选择图片"/>
				<!--<div class="bg-image" style="background-image: url(static/js/img/placeholder-img.jpg);"></div>-->
				<div class="cover" >
					<!--<div class="inner" >“我的图库”中<br>选择图片<br>图片不小于<br>300X300</div>-->
					<input type="file" id="upload" name="upWxPhoto" style="font-size: 0;opacity: 0;width: 100%;height: 100%;position: absolute;left: 0;top: 0;"/> 
				</div>
				
			</div>
			<div class="text-container">
				<input type="text" class="form-control txtValidation text-container-input" placeholder="图文标题" data-type="txtStr" data-value="图文标题不能为空" />
	            <textarea class="form-control txtValidation text-container-textarea" placeholder="微信分享时的描述，点击左侧图片改封面" data-type="txtStr" data-value="微信分享时的描述不能为空"></textarea>
				
				<select class="authorList txtValidation" data-type="selectStr"  data-value="请选择作者"></select>
			</div>
			
			
		</div>
		<div style="height: 700px;">
		<textarea id="myEditor" type="text/plain" style="width:49%;margin-left: 13px; margin-top: 20px; margin-bottom: 20px;"></textarea>
		</div>
	</div>
	
	<div class="wxPicSave hide"></div>
	
	<div>
		<div class="preview">
			<div class="view-bd">
				<div class="rich_media_area_primary">
					<h2 class="rich_media_title"> </h2>
					<div class="rich_media_meta_list">
						<em class="rich_media_meta rich_media_meta_text rich_media_meta_em"></em>
						<span class="rich_media_meta rich_media_meta_text user"></span>
					</div>

					<div class="wx_content">
						
					</div>
				</div>	
			</div>
		</div>
	</div>
	<script type="text/javascript" src="static/js/ajaxfileupload.js"></script>  
	<script type="text/javascript" src="static/js/public.js"></script>
	<script type="text/javascript" src="static/js/publicAjax.js"></script>
	<script type="text/javascript">
		$("#upload").change(function () {
            UploadImg();
        });
		var UploadImg = function() {
			 if ($("#upload").val().length <= 0) {
				return;
			};
		    $.ajaxFileUpload({
		      url : 'upWxPhoto',//后台请求地址
		      type: 'post',//请求方式 当要提交自定义参数时，这个参数要设置成post
		      secureuri : false,//是否启用安全提交，默认为false。 
		      fileElementId : 'upload',// 需要上传的文件域的ID，即<input type="file">的ID。
		      dataType : 'json',//服务器返回的数据类型。可以为xml,script,json,html。如果不填写，jQuery会自动判断。如果json返回的带pre,这里修改为json即可解决。
		      success : function (data) {//提交成功后自动执行的处理函数，参数data就是服务器返回的数据。
		
				layer.alert(data.message,{icon:1});
				$("#upload").change(function () {
                    UploadImg();
                });
				
				if(data.flag == "1"){
					$(".wxPic").removeClass("imgValidation");
					$(".wxPic").attr("src",data.data.networdURL);
					$(".wxImg").val(data.data.networdURL)
					$(".thumbMediaId").val(data.data.thumbMediaId);
				}
		      },
		      error : function (json, status, e) {//提交失败自动执行的处理函数。
		         
		      }
		    });
		  }
		
		var ue = new UE.ui.Editor({
			initialFrameHeight:500,
			initialFrameWidth: 400 ,
			elementPathEnabled:false,//去掉元素路径
			scaleEnabled:true,//禁止编辑器拉伸
			toolbars: [
				[
					//'fullscreen'//全屏
 					//,'source'//html源代码
					//, '|'
					, 'undo'//撤销
					, 'redo'//重做
					, '|'
					, 'bold'//加粗
					, 'italic'//斜体
					, 'underline'//下划线
					, 'fontborder'//字符边框
					, 'strikethrough'//删除线
					, 'superscript'//上标
					, 'subscript'//下标
					, 'removeformat'//清楚格式
					, 'formatmatch'//格式刷
					, 'autotypeset'//自动排版
					, 'blockquote'//引用
					, 'pasteplain'//纯文本粘贴模式
					, '|'
					, 'forecolor'//字体颜色
					, 'backcolor'//背景色
					, 'insertorderedlist'//有序列表
					, 'insertunorderedlist'//无序列表
					, 'selectall'//全选
					, 'cleardoc'//清空文档
					, '|'
					, 'rowspacingtop'//段前距
					, 'rowspacingbottom'//段后距
					, 'lineheight'//行间距
					, '|'
					, 'customstyle'//自定义标题
					, 'paragraph'//段落
					, 'fontfamily'//字体
					, 'fontsize'//字号
					, '|'
					, 'directionalityltr'//从左向右输入
					, 'directionalityrtl'//从右向左输入
					, 'indent'//首行缩进
					, '|'
					, 'justifyleft'//居左对齐
					, 'justifycenter'//居中对齐
					, 'justifyright'//居右对齐
					, 'justifyjustify'//两端对齐
					, '|'
					, 'touppercase'//字母大写
					, 'tolowercase'//字母小写
					, '|'
					, 'link'//超链接
					, 'unlink'//取消链接
					//, 'anchor'//锚点
					, '|'
					, 'imagenone'//默认
					, 'imageleft'//左浮动
					, 'imageright'//右浮动
					, 'imagecenter'//居中
					, '|'
					, 'insertimage'//图片
					//, 'emotion'//表情
					//, 'scrawl'//涂鸦
					//, 'insertvideo'//视频
					//, 'music'//音乐
					//, 'attachment'//附件
					//, 'map'//百度地图
					//, 'gmap'//google地图
					//, 'insertframe'//插入Iframe
					//, 'insertcode'//代码语言
					//, 'webapp'//百度应用
					//, 'pagebreak'//分页
					, 'template'//模版
					, 'background'//背景
					, '|'
					, 'horizontal'//分割线
					, 'date'//日期
					, 'time'//时间
					, 'spechars'//特殊字符
					//, 'snapscreen'//截图
					, 'wordimage'//图片转存
					, '|'
					, 'inserttable'//插入表格
					, 'deletetable'//删除表格
					, 'insertparagraphbeforetable'//表格前插入行
					, 'insertrow'//前插入行
					, 'deleterow'//删除行
					, 'insertcol'//前插入列
					, 'deletecol'//删除列
					, 'mergecells'//合并多个单元格
					, 'mergeright'//右合并单元格
					, 'mergedown'//下合并单元格
					, 'splittocells'//完全拆分单元格
					, 'splittorows'//拆分成行
					, 'splittocols'//拆分成列
					, 'charts'//图表
					, '|'
					//, 'print'//打印
					//, 'preview'//预览
					//, 'searchreplace'//查找与替换
					//, 'help'//帮助
					//, 'drafts'//草稿箱
				]
			]
		});
		ue.render("myEditor");
	
		var context = "";//存放编辑器里面的内容
		
		/*获取作者*/
		var searchLayer2 = function(_wx){
			$(".articleType").val(_wx);
			jsonDataList.url = "queryAuthor";
			var res = showAjaxFun(jsonDataList,"");
			if(res.flag == "1"){
				$(".authorList").children().remove();
				var str = '<option value = "">请选择作者</option>';
				res = res.data;
				if(res.length>0){
					for(var i = 0; i < res.length ; i++){
						if(res[i].isHide == "N"){
							str += '<option value="'+res[i].authorName+'">'+res[i].authorName+'</option>';
						}
					}
				}
				$(".authorList").append(str);
			}
		}
	
		
		var initLayer = function(_id){
			parent.layer.load(1, {
			  shade: [0.8,'#393D49'], //0.1透明度的白色背景
			});
			jsonDataList.url = "findArticleById";
			jsonDataList.data = {
				articleId:_id//主键
			};
			var res = showAjaxFun(jsonDataList,"");
			if(res.flag == "1"){
				$(".text-container-input").val(res.data.title),//标题 
				$(".text-container-textarea").val(res.data.wxContent),//描述 
				$(".wxImg").val(res.data.wxPicUrl);
				$(".wxPic").removeClass("imgValidation");
				$(".wxPic").attr("src",res.data.wxPicUrl);
				$(".editID").val(res.data.articleId);
				$(".editDate").val(res.data.createDate);
				$(".authorList").val(res.data.author);
				$(".thumbMediaId").val(res.data.thumbMediaId),
				context = res.data.content;
				setTimeout(function(){
					setContent();
					parent.layer.closeAll('loading');
				},1000);
			}
		}
		
		/*预览*/
		function previewFun() {
			if(hasContentsFun() == true) {
				var mydate = new Date();
				var str = "" + mydate.getFullYear() + "-";
				    str+= (mydate.getMonth()+1) + "-";
				    str += mydate.getDate() ;
				$(".rich_media_title").html($(".form-control").val());
				$(".rich_media_meta_em").html(str);
				$(".user").html($(".authorList").find("option:selected").text());
				$(".wx_content").html(UE.getEditor('myEditor').getContent());
			}
		}

		/*保存*/
		function save() {
			if(hasContentsFun() == true) {
				var thumbMediaIdList = '';
				$(".wxPicSave").html(UE.getEditor('myEditor').getContent());
				for(var i = 0 ; i < $(".wxPicSave img").length ; i++){
					if(thumbMediaIdList.length>0){thumbMediaIdList+=',';};
					thumbMediaIdList += jQuery(".wxPicSave img").eq(i).attr("src");
				}
				jsonDataList.url = "saveArticle";
				jsonDataList.data = {
					title:$(".text-container-input").val().trim(),//标题 
					wxContent:$(".text-container-textarea").val().trim(),//描述 
					content:UE.getEditor('myEditor').getContent(),//内容
					status:"H001001",//类型
					articleType:$(".articleType").val(),
					articleId:$(".editID").val(),//主键
					wxPicUrl:$(".wxImg").val(),//图片路径
					author:$(".authorList").val(),//作者
					thumbMediaId:$(".thumbMediaId").val(),
					contentUrlArray:thumbMediaIdList,//文章图片途径
					
				};
				
				var res = showAjaxFun(jsonDataList,"");
				parent.layer.msg(res.message,{icon:1,time:2000}, function(){
					if(res.flag == "1"){
						parent.location.reload();
						var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
						parent.layer.close(index); //再执行关闭   
					}
				});
			}
		}
		/*判断是否填写发布内容*/
		function hasContentsFun() {
			if(submitFun()){
				if(UE.getEditor('myEditor').hasContents() == true) {
					return true;
				} else {
					alert("请填写发布内容！");
				}
			}
		}
		
		
		


		/*修改写入数据*/
		function setContent(isAppendTo) {
			UE.getEditor('myEditor').setContent(context, isAppendTo);
		}

		//  function isFocus(e){
		//      alert(UE.getEditor('myEditor').isFocus());
		//      UE.dom.domUtils.preventDefault(e)
		//  }
		//  function setblur(e){
		//      UE.getEditor('myEditor').blur();
		//      UE.dom.domUtils.preventDefault(e)
		//  }
		//  function insertHtml() {
		//      var value = prompt('插入html代码', '');
		//      UE.getEditor('myEditor').execCommand('insertHtml', value)
		//  }
		//  function createEditor() {
		//      enableBtn();
		//      UE.getEditor('myEditor');
		//  }
		//  function getAllHtml() {
		//      alert(UE.getEditor('myEditor').getAllHtml())
		//  }
		//  function getContent() {
		//      var arr = [];
		//      arr.push("使用editor.getContent()方法可以获得编辑器的内容");
		//      arr.push("内容为：");
		//      arr.push(UE.getEditor('myEditor').getContent());
		//      alert(arr.join("\n"));
		//  }
		//  function getPlainTxt() {
		//      var arr = [];
		//      arr.push("使用editor.getPlainTxt()方法可以获得编辑器的带格式的纯文本内容");
		//      arr.push("内容为：");
		//      arr.push(UE.getEditor('myEditor').getPlainTxt());
		//      alert(arr.join('\n'))
		//  }

		//  function setDisabled() {
		//      UE.getEditor('myEditor').setDisabled('fullscreen');
		//      disableBtn("enable");
		//  }
		//
		//  function setEnabled() {
		//      UE.getEditor('myEditor').setEnabled();
		//      enableBtn();
		//  }
		//
		//  function getText() {
		//      //当你点击按钮时编辑区域已经失去了焦点，如果直接用getText将不会得到内容，所以要在选回来，然后取得内容
		//      var range = UE.getEditor('myEditor').selection.getRange();
		//      range.select();
		//      var txt = UE.getEditor('myEditor').selection.getText();
		//      alert(txt)
		//  }
		//
		//  function getContentTxt() {
		//      var arr = [];
		//      arr.push("使用editor.getContentTxt()方法可以获得编辑器的纯文本内容");
		//      arr.push("编辑器的纯文本内容为：");
		//      arr.push(UE.getEditor('myEditor').getContentTxt());
		//      alert(arr.join("\n"));
		//  }
		//  function hasContent() {
		//      var arr = [];
		//      arr.push("使用editor.hasContents()方法判断编辑器里是否有内容");
		//      arr.push("判断结果为：");
		//      arr.push(UE.getEditor('myEditor').hasContents());
		//      alert(arr.join("\n"));
		//  }
		//  function setFocus() {
		//      UE.getEditor('myEditor').focus();
		//  }
		//  function deleteEditor() {
		//      disableBtn();
		//      UE.getEditor('myEditor').destroy();
		//  }
		//  function disableBtn(str) {
		//      var div = document.getElementById('btns');
		//      var btns = UE.dom.domUtils.getElementsByTagName(div, "button");
		//      for (var i = 0, btn; btn = btns[i++];) {
		//          if (btn.id == str) {
		//              UE.dom.domUtils.removeAttributes(btn, ["disabled"]);
		//          } else {
		//              btn.setAttribute("disabled", "true");
		//          }
		//      }
		//  }
		//  function enableBtn() {
		//      var div = document.getElementById('btns');
		//      var btns = UE.dom.domUtils.getElementsByTagName(div, "button");
		//      for (var i = 0, btn; btn = btns[i++];) {
		//          UE.dom.domUtils.removeAttributes(btn, ["disabled"]);
		//      }
		//  }
		//
		//  function getLocalData () {
		//      alert(UE.getEditor('myEditor').execCommand( "getlocaldata" ));
		//  }
		//
		// 
		//  function clearLocalData () {
		//      UE.getEditor('myEditor').execCommand( "clearlocaldata" );
		//      alert("已清空草稿箱")
		//  }
	</script>
</body>  
</html> 