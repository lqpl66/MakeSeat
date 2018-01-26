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
<script type="text/javascript" language="javascript"
	src="<%=basePath%>JS/jquery-1.9.1.min.js"></script>
<script src="<%=basePath%>JS/JsBarcode.all.min.js"></script>
<script>
	$(function() {
		var orderNo = $(this).find('input#orderNo').val();
		JsBarcode(".barcode", orderNo);
		$(".pay-button").on("touchend",function(){
			var orderNo =  $("#orderNo").val();
			var openid = $("#openid").val();
			var s ={}
			s["orderNo"] = orderNo;
			s["openid"] = openid;
	        $.ajax({
	       	  url:'<%=basePath%>order/orderRefund', 
						type : 'post', //数据发送方式
						contentType : 'application/json;charset=utf-8',
						dataType : 'json',
						data : JSON.stringify(s),
						async : true,
						success : function(data) {
							if (data.code == "error") {
								alert(data.message);
							} else {
								 $(".pay-button").hide();
								 $(".status").empty(); 
								 $(".status").append("<span class='used'>已退款</span>");
								alert(data.message);    
							}
						},
						error : function(xhr, type) {
							alert('Ajax error!');
						}
					});
		});
	})
</script>
<style>
#barcode {
	width: 100px;
}
</style>
<title>${title}</title>
</head>
<body>
	<div class="top-verify">
		<ul class="ul-style">

			<li class="font-gray font-size-small">向工作人员出示下方校验码兑换导览笔</li>
			<li class="font-gray font-size-small">可在服务号历史订单内查看租用记录</li>
			<li><svg class="barcode"> </svg></li>
		</ul>
	</div>
	<div class="parking-info">
		<table class="tab" cellpadding="0" cellspacing="0">
			<tr>
				<td class="font-left">游乐景区：</td>
				<td class="font-right">${orderGuide.scenicName}</td>
			</tr>
			<tr>
				<td class="font-left">押金<span class="useing">（可退还）：</span></td>
				<td class="font-right"><span class="font-red">￥${orderGuide.price}</span><span
					class="font-gray-ch font-size-small">&nbsp;x${orderGuide.guideNum}</span></td>
			</tr>
			<tr>
				<td class="font-left">使用费：</td>
				<td class="font-right"><span class="font-red">￥${orderGuide.usePrice}</span><span
					class="font-gray-ch font-size-small">&nbsp;x${orderGuide.guideNum}</span></td>
			</tr>
			<tr>
				<td class="font-left border-none">总计金额：</td>
				<td class="font-right border-none"><span
					class="font-red font-bold">￥${orderGuide.paymentAmount}</span></td>
			</tr>
		</table>

	</div>
	<div class="parking-info">
		<table class="tab" cellpadding="0" cellspacing="0">
			<tr>
				<td class="font-left border-none">租用状态：</td>
				<td class="font-right border-none status"><c:choose>
						<c:when test="${orderGuide.statusStr=='1'}">
							<span class="used">已支付</span>
						</c:when>
						<c:when test="${orderGuide.statusStr=='2'}">
							<span class="used">已退款</span>
						</c:when>
						<c:when test="${orderGuide.statusStr=='3'}">
							<span class="used">设备已认领</span>
						</c:when>
						<c:when test="${orderGuide.statusStr=='4'}">
							<span class="used">押金已退</span>
						</c:when>
					</c:choose></td>
			</tr>
		</table>
	</div>
	<c:if test="${orderGuide.statusStr=='1'}">
		<a class="pay-button" > 申请退款 </a>
	</c:if>
	<input type="hidden" id="openid" value="${orderGuide.openID}">
	<input type="hidden" id="orderNo" value="${orderGuide.orderNo}">
</body>
</html>