var setPagination = function(divName,tPage,cPage,callBack,totalRecords,row){
	tPage=parseInt(tPage);
	cPage=parseInt(cPage);
    $("#"+divName).children().remove();
    skipTotalityPage=tPage;
    skipPagcallBack=callBack;
    skipDivName=divName;
    var h = "";
    if(tPage >= 1){//总页数
		$(".dataTables_info").html("共 "+totalRecords+" 条数据，每页显示 "+row+" 记录");
    	if(tPage == 1){
			h += "<li class='disabled pgeLi previous'><a>上一页</a></li>";
	    	h += "<li class='disabled pgeLi on'><a href='javascript:;'>"+cPage+"</a></li>";
			h += "<li class='disabled pgeLi next'><a>下一页</a></li>";
    	}else{
    		if(cPage == 1){
	            h += "<li class='disabled pgeLi previous'><a>上一页</a></li>";
	        }else{
	            h += "<li class='pgeLi previous' onclick='"+callBack+"(" + (cPage-1) + ")'><a href='javascript:void(0)'>上一页</a></li>";
	        }
	        var startIndex = 1;
	        var endIndex = 5;
			
			
			
	        if(cPage >=5 ){
	            startIndex = cPage-parseInt(3);
	            endIndex = cPage+parseInt(1);
	            h +='<li class="pgeLi previous" onclick="'+callBack+'('+1+');'+'"><a href="javascript:void(0);">首页</a></li>';
	        }
	        if(endIndex > tPage)endIndex = tPage;
	        for(var i =startIndex ;i <= endIndex;i++){
	            if(i==tPage)continue;
	            if(cPage == i){
	                h += "<li class='disabled pgeLi on'><a href='javascript:;'>"+i+"</a></li>";
	            }else{
	                h += "<li class='pgeLi' onclick='"+callBack+"("+i+")'><a href='javascript:void(0)'>"+i+"</a></li>"
	            }
	        }
	        if((cPage+parseInt(2))<=tPage && tPage > parseInt(5)){
	        	var next = cPage+5;
	        	if(tPage < next){
	        		
	        	}else{
	        		h+='<li class="pgeLi next"  onclick="'+callBack+'('+next+');'+'"><a href="javascript:void(0);">下5页</li>';
	        	} 
	            h+='<li class="pgeLi next"  onclick="'+callBack+'('+tPage+');'+'"><a href="javascript:void(0);">最后一页</li>';
	        }else{
	            if(tPage==cPage){
	                h+='<li  class="disabled pgeLi on" ><a href="javascript:void(0);" style="display: inline;" >'+tPage+'</a></li>';
	            }else{
	                h+='<li class="pgeLi"  onclick="'+callBack+'('+tPage+');'+'"><a href="javascript:void(0);">'+tPage+'</a></li>';
	            }
	        }
			
			if(tPage == cPage){
	            h += "<li class='disabled pgeLi next'><a>下一页</a></li>";
	        }else{
	          	h += "<li class='pgeLi next' onclick='"+callBack+"(" + (cPage+1) + ")'><a href='javascript:void(0)' >下一页</a></li>";
	       
	        }
    	}
    }else{
        h = "";
    }
    $("#"+divName).html(h);
};