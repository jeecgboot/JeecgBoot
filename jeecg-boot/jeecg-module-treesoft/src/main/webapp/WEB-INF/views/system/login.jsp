<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>IT 数据库管理</title>
<meta name="Keywords" content="IT 数据库管理">
<meta name="Description" content="IT 数据库管理 V2.3.1 ">
<meta name="viewport" content="width=device-width, initial-scale=0.85">
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/bglogin.css" />
<link rel="icon" href="${ctx}/favicon.ico" mce_href="${ctx}/favicon.ico" type="image/x-icon">
<link rel="shortcut icon" href="${ctx}/favicon.ico" mce_href="${ctx}/favicon.ico" type="image/x-icon">
<script>
	var captcha;
	function refreshCaptcha() {
		document.getElementById("img_captcha").src = "${ctx}/static/images/securityCode.jpg?t="
				+ Math.random();
	}

	function check() {
		if (document.getElementById("username1").value == "") {
			document.getElementById("login_main_errortip").innerHTML = "请输入用户名!";
			return false;
		}
		if (document.getElementById("password1").value == "") {
			document.getElementById("login_main_errortip").innerHTML = "请输入密码!";
			return false;
		}
		if (document.getElementById("captcha").value == "") {
			document.getElementById("login_main_errortip").innerHTML = "请输入验证码!";
			return false;
		}

		if (document.getElementById("username1").value.length > 15) {
			document.getElementById("login_main_errortip").innerHTML = "请输入有效的用户名!";
			return false;
		}
		return true;
	}
</script>
</head>
<body>
	<div>
		<form id="loginForm" action="${ctx}/treesoft/loginVaildate" method="post">
			<div class="login_top">
				<div class="login_title">
					<span style="margin-left: 10px; margin-top: 10px; color: #fff"> <img src="${ctx}/static/images/logo.png">IT 数据库管理
						 <span style="color: #fffl; font-size: 12px;">V2.3.1</span>
					</span>
				</div>
			</div>
			<div style="float: left; width: 100%;">
				<div class="login_main">
					<div class="login_main_top"></div>
					<div class="login_main_errortip" id="login_main_errortip">&nbsp; ${message}</div>
					<div class="login_main_ln">
						<input type="text" id="username1" name="username1" value="guest" />
					</div>
					<div class="login_main_pw">
						<input type="password" id="password1" name="password1" value="guest#123" />
					</div>
					<div class="login_main_yzm" style="display: none">
						<input type="text" id="captcha" name="captcha" value="1234" />
						<img alt="验证码" src="${ctx}/static/images/securityCode.jpg" title="点击更换" id="img_captcha" onclick="javascript:refreshCaptcha();" />
					</div>
					<div class="login_main_remb">
						<input id="rm" name="rememberMe" type="hidden" />
						<!-- <label for="rm"><span>记住我</span></label> -->
					</div>
					<div class="login_main_submit">
						<input type="submit" value="" onclick="return check()" />
					</div>
				</div>
			</div>
		</form>
	</div>
</body>
</html>
