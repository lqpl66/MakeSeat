
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
<meta charset="utf-8">
<title>${title}</title>
<meta name="viewport"
	content="width=device-width, initial-scale=0.5, minimum-scale=1.0, maximum-scale=1.0, user-scalable=yes" />
<meta content="yes" name="apple-mobile-web-app-capable" />
<meta content="black" name="apple-mobile-web-app-status-bar-style" />
<meta content="telephone=no" name="format-detection" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>CSS/main.css">
<script type="text/javascript" language="javascript"
	src="<%=basePath%>JS/jquery-1.9.1.min.js"></script>
<script>
	setInterval("myInterval()", 1000);//1000为1秒钟
	var time = 3;
	function myInterval() {
		time--;
		if (time > 0) {
			$("#timenum").text(time);
		} else {
			var orderNo =  $("#orderNo").val();
			var openid = $("#openid").val();
			parent.window.location.href = "<%=basePath%>order/orderDetails?openid="+openid+"&orderNo="+orderNo;
		}
	}
	$(function() {
		$(".btn-open").on("touchend",function(){
			var orderNo =  $("#orderNo").val();
			var openid = $("#openid").val();
			location.href = "<%=basePath%>order/orderDetails?openid="+openid+"&orderNo="+orderNo;
		});
	});
</script>


</head>
<body>
	<div class="pay-main">
		<ul class="ul-style">
			<li><img class="play-icon" src="<%=basePath%>Image/icon.png" /></li>
			<li class="font-success">支付成功</li>
			<li><label class="font-time"><span id="timenum">3</span>秒后自动跳转至订单详情界面...</label>
			</li>
		</ul>
		<input type="hidden" id="openid" value="${openid}"> <input
			type="hidden" id="orderNo" value="${orderNo}"> <a
			class="btn-open"> 立即跳转 </a>
	</div>
</body>
</html>