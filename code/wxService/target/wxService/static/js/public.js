/**
 * 验证错误提示
 * @param {Object} obj 参数ID或者class  例如 #id  或者 .class
 */
var submitFun = function(obj) {
	var res = true;
	var objName = "";
	if(obj != undefined){
		objName = $(obj +" .txtValidation");
	}else{
		objName = $(".txtValidation");
	}
	objName.each(function() {
		if($(this).data("type") == "txtStr") {
			if($(this).val() == "") {
				alert($(this).data("value"));
				res = false;
				return res;
			}
		}else if($(this).data("type") == "pwd") {
			if($(this).val() == "") {
				alert($(this).data("value"));
				res = false;
				return res;
			}else if($(this).val().length < 6) {
				alert("密码长度不能少于6位！");
				res = false;
				return res;
			}
		}else if($(this).data("type") == "pwd2") {
				if($(this).val().length < 6) {
					alert($(this).data("value")+"长度不能少于6位！");
					res = false;
					return res;
				}
		}else if($(this).data("type") == "selectStr") {
			if($(this).find("option:selected").text() == "请选择作者") {
				alert($(this).data("value"));
				res = false;
				return res;
			}
		}else if($(this).data("type") == "btnStr") {
			if($(this).find(".objName").html() == "") {
				alert($(this).data("value"));
				res = false;
				return res;
			}else if($(this).find(".bedName").html() == "") {
				alert($(this).data("value"));
				res = false;
				return res;
			}else if($(this).find(".puPersonName").html() == "") {
				alert($(this).data("value"));
				res = false;
				return res;
			}
		}else if($(this).data("type") == "checkStr") {
			var checkedFlase = false;
			$(this).find("input[type=checkbox]").each(function(){
				if($(this).is(":checked") == true){
					checkedFlase = true;
				}
			})
			if(checkedFlase == false){
				alert($(this).data("value"));
				res = false;
				return res;
			}
		}else if($(this).data("type") == "imgStr") {
			if($(this).hasClass("imgValidation")) {
				alert($(this).data("value"));
				res = false;
				return res;
			}
		}
	});
	return res;
}

var selectControl = function(name,arr,arrVal,arrText){
	$(name).children().remove();
	var str = '<option value = "">请选择</option>';
	if(arr.length>0){
		for(var i = 0; i < arr.length ; i++){
			 str += '<option value="'+arr[i][arrVal]+'">'+arr[i][arrText]+'</option>';
		}
	}
	$(name).append(str);
}

/**
 * 时间戳 转换 
 */
var conversionTime = function(ts){
	var datas = new Date(ts);
	var Y = datas.getFullYear() + '-';
	var M = (datas.getMonth() + 1 < 10 ? '0' + (datas.getMonth() + 1) : datas.getMonth() + 1) + '-';
	var D = (datas.getDate()  <10 ? ('0' + datas.getDate()  ):datas.getDate()  ) + ' ';
	var h = (datas.getHours() <10 ? ('0' + datas.getHours() ):datas.getHours() )+ ':';
	var m = (datas.getMinutes() <10 ? ('0' + datas.getMinutes() ):datas.getMinutes() ) ;
	var s = datas.getSeconds();
//	 console.log("转换后时间" + Y + M + D + h + m + s);
	return Y + M + D + h + m;
}