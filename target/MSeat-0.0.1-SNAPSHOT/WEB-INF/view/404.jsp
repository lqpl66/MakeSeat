 


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
<meta charset="utf-8" />
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no;" name="viewport" />
   <script type="text/javascript">
    function reload(){
    	  window.location.reload(); 
    }  
   </script>
<title>${title}</title>
<style>
.weui_msg .weui_text_area {
	 	margin-bottom: 20px; 
	 	padding: 0 10px; 
	    margin-top: 50px; 
	    font-size: 12px; 
} 

body {
	text-align: center;
	background-color: #f8f8f8;
	color: #000; 
}

.weui_msg {
	margin-top: 120px
}

.weui_msg_title {
	    font-size: 18px;
}

img {
	width: 260px;
}
.buttonDiv{
width: 240px ;
margin: 0px auto;
height: 56px;
background-color: #fff;
	border: 1px solid #ddd; 
    border-radius: 10px 10px 10px 10px;
line-height: 56px; 
font-size: 18px;
}
.buttonDiv:ACTIVE  { 
	background-color: #f2f2f2;
	border: 1px solid #ddd;
    border-radius: 10px 10px 10px 10px;  
}



</style>
</head>
<body>
	<div class='weui_msg'>
		<div class='weui_icon_area'>
				<img src="<%=basePath%>Image/404error.png"     />
		</div>
		<div class='weui_text_area'>
			<span class='weui_msg_title'>当前页面加载错误，请检查您的网络设置</span>
		</div>
		<div class='weui_text_area'> 
			<div class="buttonDiv" onclick="reload()">重新加载</div>
		</div>
	</div>
</body>
</html>