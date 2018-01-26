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
<title>${title}</title>
<style>
.weui_msg .weui_text_area {
	 	margin-bottom: 20px; 
	 	padding: 0 10px; 
	    margin-top: 25px;
}

body {
	text-align: center
}

.weui_msg {
	margin-top: 120px
}

.weui_msg_title {
	font-size: 2.6rem;
}

img {
	width: 260px;
}
</style>
</head>
<body>
	<div class='weui_msg'>
		<div class='weui_icon_area'>
			<img src="<%=basePath%>Image/error.png" />
		</div>
		<div class='weui_text_area'>
			<span class='weui_msg_title'>${message}</span>
		</div>
	</div>
</body>
</html>