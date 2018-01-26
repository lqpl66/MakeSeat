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
<script type="text/javascript">
	var ua = navigator.userAgent.toLowerCase();
	var isWeixin = ua.indexOf('micromessenger') != -1;
	var isAndroid = ua.indexOf('android') != -1;
	var isIos = (ua.indexOf('iphone') != -1) || (ua.indexOf('ipad') != -1);
	if (!isWeixin) {
		document.head.innerHTML = '<title>抱歉，出错了</title><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=0">';
		// 		document.body.innerHTM="ss";
		// 				document.body.innerHTML = "<div class='weui_msg'><div class='weui_icon_area'></div><div class='weui_text_area'><h4 class='weui_msg_title'>请在微信客户端打开链接</h4></div></div>";
	}
</script>
<style>
.weui_msg .weui_text_area {
	margin-bottom: 25px;
	padding: 0 20px;
}

body {
	text-align: center
}
.weui_msg{
margin-top: 40px
}
img {
	height: 104px;
	width: 104px;
}
</style>
<body>
	<div class='weui_msg'>
		<div class='weui_icon_area'>
			<img src="<%=basePath%>Image/weixin.png" />
		</div>
		<div class='weui_text_area'>
			<h4 class='weui_msg_title'>请在微信客户端打开链接</h4>
		</div>
	</div>
</body>
</html>