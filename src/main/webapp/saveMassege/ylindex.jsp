<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>保存消息</title>
    <link href="<%=basePath%>/resources/bootstrap2.3.2/css/bootstrap.min.css" rel="stylesheet" />
    <link type="text/css" rel="stylesheet" href="<%=basePath%>resources/css/jquery.jerichotab.css"/>
<link type="text/css" rel="stylesheet" href="<%=basePath%>resources/css/Main_body.css"/>
    <link href="<%=basePath%>/css/system/Common.css" rel="stylesheet" />
    <link href="<%=basePath%>/css/system/Index.css" rel="stylesheet" />
    <style type="text/css">
    
    </style>
	<script src="<%=basePath%>/js/system/jquery-1.9.1.min.js"></script>
    <script type="text/javascript">
	 
	</script>
	
	 
</head>
<body>
    <div class="header">
      保存活动消息
      <div>
      <table>
         <tr>
            <td>姓名：</td>
            <td>超级管理员</td>
          </tr>
         <tr>
	         <td> 登录uuid：</td>
	         <td> <input type="text" id="uuid" value="53f9692c7b704179a9e268ec5bc155ba" width="120px;"/></div></td>
         </tr>
      </table>
     </div>
     <div class="content">
        <table>
          <tr><td></td><td></td></tr>
        </table>
     </div>
</body>
</html>
