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
<script type="text/javascript" language="javascript" src="<%=basePath%>JS/jquery-1.9.1.min.js"></script>

    <link href="<%=basePath%>CSS/bootstrap.css?version=1.1" rel="stylesheet"  />
    <script src="<%=basePath%>JS/bootstrap.min.js?version=1.1"></script>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>CSS/main.css?version=1.1">
	 <style>
        .small-er{position:fixed;z-index:999;border:1px solid #dcdcdc;top:350px;right:0px;   }
        .small-er img{width:50px;height:50px;}
        .big-er img{width:100%;}
        .big-er{width:80% !important;left:10% !important;margin-left: 0px !important; margin-top:25%;}
        .modal-body{overflow-y:visible; margin-top:-20px;}
        .modal-header{border-bottom:0px;height:20px;}
        .close{font-size:30px;}
       .follow{
       margin-top: 30px;
       }
    </style>
<script>
	function checked(obj) {
		if ($(obj).attr("imgcheck") == "true") {
			$(obj).children().attr("src", "<%=basePath%>Image/nochecked.png");
			$(obj).attr("imgcheck", "false");
			$(".pay-button").css("background-color", "#d2d2d2");
		} else {
			$(obj).children().attr("src", 
					"<%=basePath%>Image/checked.png");
			$(obj).attr("imgcheck", "true");
			$(".pay-button").css("background-color", "#00BBD4");
		}
	}
	$(function() {
	  $('.clickModel').click(function(){
			$('#myModal').modal('show');
	  });	
	
		var startcount = parseInt($(".txtcount").val() == "" ? "0" : $(
				".txtcount").val());
		if (startcount > 0 || startcount <= 99) {
			$("#jian").show();
			$("#jia").show();
			$(".itemCount").css("width", "8rem");
		} else {
			$("#jian").hide();
			$(".itemCount").css("width", 8 - 2 + "rem");
		}
// 		$(".click").live("touchend",function(){
	$(".pay-button").on("touchend",function(){
		var scenicId = $("#scenicId").val();
		var buyNum = $(".txtcount").val();
		var openid = $("#openid").val();
		var s ={}
		s["scenicId"] = scenicId;
		s["buyNum"] =buyNum ;
		s["openid"] = openid;
        $.ajax({
       	  url:'<%=basePath%>order/orderGeneration', 
					type : 'post', //数据发送方式
					contentType : 'application/json;charset=utf-8',
					dataType : 'json',
					data : JSON.stringify(s),
					async : true,
					success : function(data) {
						if (data.code == "error") {
							alert(data.message);
						} else {
							//onBridgeReady(appId, timeStamp, nonceStr, prepayid, paySign)
							onBridgeReady(data.data.appId, data.data.timeStamp,
									data.data.nonceStr,
									data.data.prepay_id,data.data.sign,data.openid,data.orderNo);
						}
					},
					error : function(xhr, type) {
						alert('Ajax error!');
					}
				});
			});
	});
	function addGoodCount(obj, Sid) {
		var startcount = parseInt($(obj).prev().val() == "" ? "0" : $(obj)
				.prev().val());
		if (startcount == 99) {
			$("#jia").hide();
			$(".itemCount").css("width", 8 - 2 + "rem");
		} else {
			var nowcount = startcount + 1;
			startcount = startcount + 1;
			var strcount = nowcount.toString();
			$(obj).prev().val(strcount);
			$("#jia").show();
			$("#jian").show();
			$(".itemCount").css("width", "8rem")
		}
		countprice(startcount);
	}
	function reduceCount(obj, Sid) {
		var count = $(obj).next().val();
		var startcount = parseInt(count == "" ? "0" : count == "1" ? "1"
				: count);
		if (startcount > 1 && startcount <= 99) {
			var nowcount = startcount - 1;
			var strcount = nowcount.toString();
			$(obj).next().val(strcount);
			$("#jian").show();
			$("#jia").show();
			$(".itemCount").css("width", "8rem");
		} else {
			var nowcount = 1;
			var strcount = nowcount.toString();
			$(obj).next().val(strcount);
			$("#jian").hide();
			$(".itemCount").css("width", 8 - 2 + "rem");
		}
		countprice(strcount);
	}
	function checknum(obj) {
		var startcount = parseInt($(obj).val() == "" ? "0" : $(obj).val());
		if (startcount > 99) {
			$(obj).val(99);
			startcount = 99;
			$("#jian").show();
			$("#jia").hide();
			$(".itemCount").css("width", 8 - 2 + "rem");
		} else if (startcount >= 0 && startcount <= 99) {
			$("#jian").show();
			$("#jia").show();
			$(".itemCount").css("width", "8rem");
		}
		countprice(startcount);
	}
	function countprice(count) {
		var totalprice = $("#totalprice").val();
		totalprice = totalprice * count;
		$(".total").text(totalprice.toFixed(2));
	}
	function onBridgeReady(appId, timeStamp, nonceStr, prepayid, paySign,openid,orderNo) {
		WeixinJSBridge.invoke('getBrandWCPayRequest', {
			"appId" : "wxc9e385e97966ab61", //公众号名称，由商户传入     
			"timeStamp" : timeStamp, //时间戳，自1970年以来的秒数     
			"nonceStr" : nonceStr, //随机串     
			"package" :  prepayid,
			"signType" : "MD5", //微信签名方式：     
			"paySign" : paySign
		//微信签名 
		}, function(res) {
			if (res.err_msg == "get_brand_wcpay_request:ok") {  
				location.href = "<%=basePath%>order/orderSuccess?openid="+openid+"&orderNo="+orderNo;
			} else if(res.err_msg == "get_brand_wcpay_request:cancel") {// 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
        	                  window.reload();
        	}else{
        	        	location.href = "<%=basePath%>order/orderSuccess";
        	        }
		});
	}
// 	if (typeof WeixinJSBridge == "undefined") {
// 		if (document.addEventListener) {
// 			document.addEventListener('WeixinJSBridgeReady', onBridgeReady,
// 					false);
// 		} else if (document.attachEvent) {
// 			document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
// 			document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
// 		}
// 	} else {
// 		onBridgeReady();
// 	}
	
</script>
<style>
.modal{display: none;}
</style>
<title>导览笔租用</title>
</head>
<body>
	<div class="parking-info">
		<table class="tab" cellpadding="0" cellspacing="0">
			<tr>
				<td class="font-left">游乐景区：</td>
				<td class="font-right">${scenic.scenicName}</td>
			</tr>
			<tr>
				<td class="font-left">押金<span class="useing">（可退还）：</span></td>
				<td class="font-right"><span class="font-red">￥${scenic.price }</span><span
					class="font-gray-ch">（单个）</span></td>
			</tr>
			<tr>
				<td class="font-left">使用费：</td>
				<td class="font-right"><span class="font-red">￥${scenic.usePrice}</span><span
					class="font-gray-ch">（单个）</span></td>
			</tr>
			<tr>
				<td class="font-left border-none">选择数量：</td>
				<td class="border-none" style="padding-right: 20px;" align="right">
					<div class="itemCount">
						<input type="button" class="btnds"id="jian"onclick="reduceCount(this)" value="-">
<!--                         <a class="btnds" id="jian" onclick="reduceCount(this)">-</a> -->
                        <input type="tel" max="10" value='1' class="txtcount" onkeyup="javascript:this.value=this.value.replace(/[^0-9]/g,'')"   onblur="checknum(this)" />
                        <input type="button" class="btnds"id="jia"onclick="addGoodCount(this)" value="+">
<!--                         <a onclick="addGoodCount(this)" id="jia"class="btnds">+</a> -->
                    </div>
				</td>
			</tr>
		</table>

	</div>
	<div class="parking-info">
		<input type="hidden" id="totalprice"
			value="${scenic.price +scenic.usePrice}" />
		<table class="tab" cellpadding="0" cellspacing="0">
			<tr>
				<td class="font-left border-none">总计金额</td>
				<td class="font-right border-none font-red font-bold">￥<span
					class="total">${scenic.price +scenic.usePrice}</span></td>
			</tr>
		</table>
	</div>
	<div>
	<img alt="" class="follow" src="<%=basePath%>Image/follow.png">
	</div>
	<div class="foot-attention"></div>
	<input type="hidden" id="openid" value="${openid}">
	<input type="hidden" id="scenicId" value="${scenic.scenicId}">
	<a class="pay-button" > 支&nbsp;&nbsp;付 </a>
<!-- 	data-target="#myModal" -->
<!-- 	 <div class="small-er clickModel" data-toggle="modal" > -->
<%--         <img src="<%=basePath%>Image/uto_er.jpg" /> --%>
<!--     </div> -->
<!--     模态框（Modal） -->
<!--     <div class="modal fade big-er" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> -->
<!--         <div class="modal-dialog"> -->
<!--             <div class="modal-content"> -->
<!--                 <div class="modal-header"> -->
<!--                     <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button> -->
<!--                 </div> -->
<%--                 <div class="modal-body"><img src="<%=basePath%>Image/uto_er.jpg" /></div> --%>
<!--             </div> -->
<!--         </div> -->
<!--     </div> -->
	
	
</body>
</html>