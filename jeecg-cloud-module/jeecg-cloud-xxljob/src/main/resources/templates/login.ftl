<!DOCTYPE html>
<html>
<head>
  	<#import "./common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/iCheck/square/blue.css">
	<title>${I18n.admin_name}</title>
</head>
<body class="hold-transition login-page">
	<div class="login-box">
		<div class="login-logo">
			<a><b>XXL</b>JOB</a>
		</div>
		<form id="loginForm" method="post" >
			<div class="login-box-body">
				<p class="login-box-msg">${I18n.admin_name}</p>
				<div class="form-group has-feedback">
	            	<input type="text" name="userName" class="form-control" placeholder="${I18n.login_username_placeholder}"  maxlength="18" >
	            	<span class="glyphicon glyphicon-envelope form-control-feedback"></span>
				</div>
	          	<div class="form-group has-feedback">
	            	<input type="password" name="password" class="form-control" placeholder="${I18n.login_password_placeholder}"  maxlength="18" >
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
<@netCommon.commonScript />
<script src="${request.contextPath}/static/adminlte/plugins/iCheck/icheck.min.js"></script>
<script src="${request.contextPath}/static/js/login.1.js"></script>

</body>
</html>
