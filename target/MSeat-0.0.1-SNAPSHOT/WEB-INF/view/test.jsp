<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<title>健康资讯</title>
<script src="jquery.min.js" /></script>
<script type="text/javascript" src="iscroll.js"></script>
<link rel="stylesheet" type="text/css" href="bookBase.css" />
<!--start-->
<style>
html, body {
	background-color: #fff;
}

.m-img-font-box {
	
}

.m-img-font-box a.link {
	display: block;
	border-bottom: 1px solid #ccc;
}

.m-img-font-box a.link dl {
	padding: 5px 10px;
	height: 50px;
	overflow: hidden;
}

.m-img-font-box a.link dl dt {
	width: 60px;
	height: 60px;
	float: left;
}

.m-img-font-box a.link dl dt img {
	width: 100%;
	height: 82%;
}

.m-img-font-box a.link dl dd {
	margin-left: 65px;
	padding-top: 1px;
}

.m-img-font-box a.link dl dd p {
	font-size: 16px;
	color: #bbb;
}

.m-img-font-box a.link dl dd p.title {
	font-size: 16px;
	color: #464646;
	width: 91%;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}

.m-img-font-box a.link dl dd p.font {
	width: 98%;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
	font-size: 14px;
}

h3 {
	font-size: 13px;
	font-weight: normal;
	padding: 5px 10px;
}

#wrapper {
	position: absolute;
	z-index: 1;
	top: 30px;
	bottom: 0;
	left: -9999px;
	width: 100%;
	background: #FFF;
	overflow: auto;
}

#scroller {
	position: absolute;
	z-index: 1;
	-webkit-tap-highlight-color: rgba(0, 0, 0, 0);
	width: 100%;
	padding: 0;
}

#pullUp {
	background: #fff;
	height: 40px;
	line-height: 40px;
	padding: 5px 10px;
	border-bottom: 1px solid #ccc;
	font-weight: bold;
	font-size: 14px;
	color: #888;
}

#pullUp.loading .pullUpLabel {
	background: url(../images/loading/loading.gif) no-repeat center 0;
	width: 100%;
	height: 30px;
	align: center;
	background-size: auto 100%;
}
</style>
<script type="text/javascript">
	var myScroll, pullUpEl, pullUpOffset, generatedCount = 0;

	var currenPage = 0;
	var pageSize = 3;

	function pullUpAction() {
		currenPage++;

		var elInit = $("#thelist");

		var param = {
			"currentPage" : currenPage,
			"pageSize" : pageSize
		};
		$
				.ajax({
					url : "result.json", //后台处理程序
					type : 'post', //数据发送方式
					dataType : 'json',
					data : param,
					async : true,
					success : function(data) {
						var result = "";
						$(data)
								.each(
										function() {
											var checker = $(this)[0];
											result += "<div class='mfb'><a class='link' href='"+checker.url+"'>"
													+ "<dl><dt><img src='"+checker.icon+"'/> </dt>"
													+ "<dd><p class='title'>"
													+ checker.title
													+ "</p>"
													+ "<p class='title'>"
													+ checker.subTitle
													+ "</p>"
													+ "</dd></dl></a></div>";
										});

						elInit.append(result);
						myScroll.refresh();

					}
				});

	}

	function loaded() {

		pullUpEl = document.getElementById('pullUp');
		if (pullUpEl != null) {
			pullUpOffset = pullUpEl.offsetHeight;
		} else {
			pullUpOffset = 0;
		}

		myScroll = new iScroll(
				'wrapper',
				{
					useTransition : true,
					onRefresh : function() {
						if (pullUpEl.className.match('loading')) {
							pullUpEl.className = '';
							$(pullUpEl).css("display", "none");
							pullUpEl.querySelector('.pullUpLabel').innerHTML = 'Pull up to load more...';
						}
					},
					onScrollMove : function() {
						if (this.y < (this.maxScrollY - 5)
								&& !pullUpEl.className.match('flip')) {
							pullUpEl.className = 'flip';
							$(pullUpEl).css("display", "");
							pullUpEl.querySelector('.pullUpLabel').innerHTML = '松开载入更多..';
							this.maxScrollY = this.maxScrollY;
						} else if (this.y > (this.maxScrollY + 5)
								&& pullUpEl.className.match('flip')) {
							pullUpEl.className = '';
							pullUpEl.querySelector('.pullUpLabel').innerHTML = 'Pull up to load more...';
							this.maxScrollY = pullUpOffset;
						}
					},
					onScrollEnd : function() {
						if (pullUpEl.className.match('flip')) {
							pullUpEl.className = 'loading';
							$(pullUpEl).css("display", "");
							pullUpEl.querySelector('.pullUpLabel').innerHTML = '';
							pullUpAction(); // Execute custom function (ajax call?)
						}
					}

				});

		document.getElementById('wrapper').style.left = '0';
	}

	document.addEventListener('touchmove', function(e) {
		e.preventDefault();
	}, false);

	document.addEventListener('DOMContentLoaded', function() {
		setTimeout(loaded, 200);
	}, false);

	//页面加载完成后，向后台请求数据
	$(function() {
		pullUpAction();

	});
</script>
</head>
<body>
	<h3>资讯列表</h3>
	<div class="cm-container-box" id="wrapper">
		<div class="cm-table" id="scroller">
			<div class="m-img-font-box" id="thelist"></div>

			<div id="pullUp" style="display: none; text-align: center;">
				<span class="pullUpLabel"></span>
			</div>

		</div>
	</div>


</body>
</html>