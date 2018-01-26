<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String file = "files";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=0.5, minimum-scale=1.0, maximum-scale=1.0, user-scalable=yes" />
<meta content="yes" name="apple-mobile-web-app-capable" />
<meta content="black" name="apple-mobile-web-app-status-bar-style" />
<meta content="telephone=no" name="format-detection" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>CSS/main.css">
<!-- <script type="text/javascript" language="javascript" -->
<%-- 	src="<%=basePath%>JS/jquery-1.9.1.min.js"></script> --%>
<script src="<%=basePath%>JS/zepto.min.js"></script>
<script src="<%=basePath%>JS/dist/dropload.min.js"></script>
<%-- <script src="<%=basePath%>JS/dist/dropload.js"></script> --%>
<link rel="stylesheet" href="<%=basePath%>JS/dist/dropload.css">
<%-- <link rel="stylesheet" href="<%=basePath%>refresh/reset.css" /> --%>
<%-- <link rel="stylesheet" href="<%=basePath%>refresh/pullToRefresh.css" /> --%>
<%-- <script src="<%=basePath%>refresh/iscroll.js"></script> --%>
<%-- <script src="<%=basePath%>refresh/pullToRefresh.js"></script> --%>
<%-- <script src="<%=basePath%>refresh/colorful.js"></script> --%>

<script>
	$(function() {
		$(".click").live("touchend",function(){
			var orderNo = $(this).children('input','#orderNo').val();
			var openid = $("#openid").val();
			location.href = "<%=basePath%>order/orderDetails?openid="+openid+"&orderNo="+orderNo;
// 			 e.preventDefault();
		});
// 		$(".click").live("click",function(e) {
// 			var orderNo = $(this).children('input','#orderNo').val();
// 			var openid = $("#openid").val();
<%-- 			location.href = "<%=basePath%>order/orderDetails?openid="+openid+"&orderNo="+orderNo; --%>
// 			 e.preventDefault();
// 		});
  	     var pageNum =$("#pageNum").val();
	     var openid =$("#openid").val();
	      var s ={};
	      s["openid"]=openid;
	      s["num"]=10;
	      s["pageNum"]=pageNum;
	    $(".order-main").dropload({
	         scrollArea : window,
	         click:true,
	         loadDownFn : function(me){
	             // 拼接HTML
	             $.ajax({
	             	  url:'<%=basePath%>order/orderAjaxList', //后台处理程序 --%>
	 		 		   type:'post',         //数据发送方式
	 		 		  contentType : 'application/json;charset=utf-8',
	 		 		  dataType : 'json',
	 				   data:JSON.stringify(s),
	 				   async: true,
	                 success: function(data){
	                 	 if(data.code=="error"){
	 		        		 alert(data.message); 
	 		        	      // 锁定
	 	                        me.lock();
	 	                        // 无数据
	 	                        me.noData();
	 		        	 }else{
	 		  	      	   var result = "" ;
	 		  	   	 if(data.list.length<10){
	        			   me.lock();
	                       me.noData();	 
	        		 }
// 	 		  	      	 $("#pageNum").val(++pageNum)
                          s["pageNum"]=++pageNum;
// 	  		  	      	  alert(data.list.length);
	 			      	   $(data.list).each(function(){
	 			    		   var scenic = $(this)[0];
	 			    			result += "<li >"+
	 			    			"<ul class='list-info'>"+
	 			    		   "<li class='click'>"+
	 			    		  "<input type='hidden' class='orderNo' value='"+scenic.orderNo+"'>"+
	 			    		   scenic.scenicName+
	 			    			"<img class='img-icon' src='<%=basePath%>Image/chose.png' />"
	 														+ "</li>"
	 														+ "<li>租用时间：<span class='list-info-time'>"
	 														+ scenic.addTime
	 														+ "</span>";
	 												var status = "";
	 												if (scenic.statusStr == '1') {
	 													status = "<span class='used list-info-status'>已支付</span>";
	 												} else if (scenic.statusStr == '2') {
	 													status = "<span class='unused list-info-status'>已退款</span>";
	 												} else if (scenic.statusStr == '3') {
	 													status = "<span class='useing list-info-status'>设备已认领</span>";
	 												} else if (scenic.statusStr == '4') {
	 													status = "<span class='unused list-info-status'>押金已退</span>";
	 												}
	 												result += status + "</li>"
	 														+ "</ul>" + "</li>";
	 											});
	                     }
	                     // 插入数据到页面，放到最后面
                         $('#thelist').append(result);
                         // 每次数据插入，必须重置
                         me.resetload();
	                     // 为了测试，延迟1秒加载
// 	                     setTimeout(function(){
	                  
// 	                     },1000);
	                 },
	                 error: function(xhr, type){
	                     alert('Ajax error!');
	                     // 即使加载出错，也得重置
	                     me.resetload();
	                 }
	             });
	         }
	     });
	})
</script>
<title>${title}</title>
</head>
<body>
	<div class="order-main" id="wrapper">
		<ul class="order-list" id="thelist">
<%-- 			<c:forEach var="orderGuide" items="${list}"> --%>
<!-- 				<li class="click"><input type="hidden" id="orderNo" -->
<%-- 					value="${orderGuide.orderNo}"> --%>
<!-- 					<ul class="list-info"> -->
<%-- 						<li>${orderGuide.scenicName}<img class="img-icon" --%>
<%-- 							src="<%=basePath%>Image/chose.png" /></li> --%>
<%-- 						<li>租用时间：<span class="list-info-time">${orderGuide.addTime}</span> --%>
<%-- 							<c:choose> --%>
<%-- 								<c:when test="${orderGuide.statusStr=='1'}"> --%>
<!-- 									<span class="used list-info-status">已支付</span> -->
<%-- 								</c:when> --%>
<%-- 								<c:when test="${orderGuide.statusStr=='2'}"> --%>
<!-- 									<span class="unused list-info-status">已退款</span> -->
<%-- 								</c:when> --%>
<%-- 								<c:when test="${orderGuide.statusStr=='4'}"> --%>
<!-- 									<span class="unused list-info-status">押金已退</span> -->
<%-- 								</c:when> --%>
<%-- 								<c:when test="${orderGuide.statusStr=='3'}"> --%>
<!-- 									<span class="useing list-info-status">设备已认领</span> -->
<%-- 								</c:when> --%>
<%-- 							</c:choose> --%>
<!-- 						</li> -->
<!-- 					</ul></li> -->
<%-- 			</c:forEach> --%>
		</ul>
<!-- 		<div id="pullUp" style="display: none; text-align: center;"> -->
<!-- 			<span class="pullUpLabel"></span> -->
<!-- 		</div> -->
	</div>
	<input type="hidden" id="pageNum" value="1">
	<input type="hidden" id="openid" value="${openid}">
</body>
</html>