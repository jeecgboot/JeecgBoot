<!DOCTYPE html>
<html>
<head>
	<#-- import macro -->
	<#import "../common/common.macro.ftl" as netCommon>

	<!-- 1-style start -->
	<@netCommon.commonStyle />
	<!-- iCheck -->
	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/iCheck/square/blue.css">
	<!-- 1-style end -->

</head>
<body class="hold-transition login-page">

	<!-- 2-biz start -->
	<div class="login-box">
		<div class="login-logo">
			<a><b>XXL-JOB</b></a>
		</div>
		<form id="loginForm" method="post" >
			<div class="login-box-body">
				<p class="login-box-msg">${I18n.admin_name}</p>
				<div class="form-group has-feedback">
	            	<input type="text" name="userName" class="form-control" placeholder="${I18n.login_username_placeholder}"  maxlength="20" >
	            	<span class="glyphicon glyphicon-envelope form-control-feedback"></span>
				</div>
	          	<div class="form-group has-feedback">
	            	<input type="password" name="password" class="form-control" placeholder="${I18n.login_password_placeholder}"  maxlength="20" >
	            	<span class="glyphicon glyphicon-lock form-control-feedback"></span>
	          	</div>
				<div class="row">
					<div class="col-xs-8">
		              	<div class="checkbox icheck">
		                	<label>
		                  		<input type="checkbox" name="ifRemember" > &nbsp; ${I18n.login_remember_me}
		                	</label>
						</div>
		            </div><!-- /.col -->
		            <div class="col-xs-4">
						<button type="submit" class="btn btn-primary btn-block btn-flat">${I18n.login_btn}</button>
					</div>
				</div>
			</div>
		</form>
	</div>
	<!-- 2-biz end -->

<!-- 3-script start -->
<@netCommon.commonScript />
<script src="${request.contextPath}/static/adminlte/plugins/iCheck/icheck.min.js"></script>
<script>
$(function () {

	// input iCheck
	$('input').iCheck({
		checkboxClass: 'icheckbox_square-blue',
		radioClass: 'iradio_square-blue',
		increaseArea: '20%' // optional
	});

	// login Form Valid
	var loginFormValid = $("#loginForm").validate({
		errorElement : 'span',
		errorClass : 'help-block',
		focusInvalid : true,
		rules : {
			userName : {
				required : true ,
				minlength: 4,
				maxlength: 20
			},
			password : {
				required : true ,
				minlength: 4,
				maxlength: 20
			}
		},
		messages : {
			userName : {
				required  : I18n.login_username_empty,
				minlength : I18n.login_username_lt_4
			},
			password : {
				required  : I18n.login_password_empty  ,
				minlength : I18n.login_password_lt_4
				/*,maxlength:"登录密码不应超过18位"*/
			}
		},
		highlight : function(element) {
			$(element).closest('.form-group').addClass('has-error');
		},
		success : function(label) {
			label.closest('.form-group').removeClass('has-error');
			label.remove();
		},
		errorPlacement : function(error, element) {
			element.parent('div').append(error);
		},
		submitHandler : function(form) {
			$.post(base_url + "/auth/doLogin", $("#loginForm").serialize(), function(data, status) {
				if (data.code === 200) {
					layer.msg( I18n.login_success );
					setTimeout(function(){
						window.location.href = base_url + "/";
					}, 500);
				} else {
					layer.open({
						title: I18n.system_tips,
						btn: [ I18n.system_ok ],
						content: (data.msg || I18n.login_fail ),
						icon: '2'
					});
				}
			});
		}
	});

});
</script>
<!-- 3-script end -->


</body>
</html>
