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
</style>
<script type="text/javascript" language="javascript"
	src="<%=basePath%>JS/jquery-1.9.1.min.js"></script>
<script>
	$(function() {
		var ua = navigator.userAgent.toLowerCase();
		if (/iphone|ipad|ipod/.test(ua)) {
			ios();
		} else if (/android/.test(ua)) {
			window.location.href = "http://a.app.qq.com/o/simple.jsp?pkgname=com.uto168.yl";
		}
		$(".im")
				.click(
						function() {
							var ua = navigator.userAgent.toLowerCase();
							if (/iphone|ipad|ipod/.test(ua)) {
								ios();
							} else if (/android/.test(ua)) {
								window.location.href = "http://a.app.qq.com/o/simple.jsp?pkgname=com.uto168.yl";
							}
						});
	});
	function ios() {
		window.location.href = "uto://";
		window.setTimeout(
						function() {
							window.location.href = "https://itunes.apple.com/cn/app/che-ling-ling-fu-wu-ban/id1148541314?mt=8";
							/***下载app的地址***/
						}, 1000)
	};
</script>
</head>
<body>
</body>
</html>