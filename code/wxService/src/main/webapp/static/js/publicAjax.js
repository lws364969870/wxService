/**
 * Created by Administrator on 2016/10/27.
 */
/**
 * Created by Administrator on 2016/9/26.
 */
/**
 * 显示数据
 * @param dataList 显示的数据
 * @param name	 例:success或error
 * @param ajaxName ajax方法名称
 */



var jsonDataList = {
    url:"",
    dataType:"JSON",
    type:"POST",
    data:"",
    async:false
};

var clearJsonDataList = function(){
	jsonDataList.url = "";
	jsonDataList.dataType="JSON";
    jsonDataList.type="POST";
    jsonDataList.data="";
    jsonDataList.async=false;
}
/**
 * 请求服务器，返回数据
 * @param jsonData	请求服务器所需的参数
 * @param ajaxFun  ajax方法名称
 */

var  stringifyFun = function(obj){
	return JSON.stringify(obj);
}

var  publicDebug = true;
var showConsoleLogFun = function(ajaxName,name,dataList){
//  if(publicDebug){
//  	if(window.console){
//  		if(name === "error"){
//				console.log("================================="+ajaxName  +"错误提示START=================================");
//	            console.log(dataList);
//				console.log("================================="+ajaxName  +"错误提示END=================================");
//	        }else if(name === "success"){
//	            console.log("================================="+ajaxName  +"方法START=================================");
//	            console.log(dataList);
//	            console.log("================================="+ajaxName  +"方法END=================================");
//	        }
//  	}
//  }
}

var showConsole = function(dataList){
	if(publicDebug){
    	if(window.console){
	       console.log(dataList);
    	}
    }
}

var encryptionFun = function(objData,str_key,str_vi){
	var encrtWord = Encrypt(JSON.stringify(objData),str_key,str_vi);
//	showConsole(objData);
//	showConsole(str_key);
//	showConsole(str_vi);
    return "jsonData="+encrtWord;
}

var decryptionFun = function(objData,str_key,str_vi){
		var decRes = JSON.parse(Decrypt(objData,str_key,str_vi));
//		showConsole(decRes);
//		showConsole(str_key);
//		showConsole(str_vi);
		return decRes;
}

var judge = true;
var showAjaxFun = function(jsonDataStr,ajaxFun){
	var result = "";
	if(judge == true){
		judge = false;
		jQuery.support.cors = true;
	    jQuery.ajax({
	        url:jsonDataStr.url,
	        dataType:jsonDataStr.dataType,
	        type:jsonDataStr.type,
	        data:jsonDataStr.data,
	        async:jsonDataStr.async,
	        success:function(data){
	        	judge = true;
	           	result = data;
	        },error: function(errorData) {
	        	judge = true;
	        }
	    });
	}
	clearJsonDataList();
    return result;
}


