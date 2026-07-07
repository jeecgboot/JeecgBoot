<#-- import: style -->
<#macro commonStyle>

<#-- i18n -->
<#global I18n = I18nUtil.getMultString()?eval />
<#-- title、favicon、meta -->
<title>${I18n.admin_name_full}</title>
<link rel="icon" href="${request.contextPath}/static/favicon.ico" />
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
<#-- css -->
<link rel="stylesheet" href="${request.contextPath}/static/adminlte/bower_components/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${request.contextPath}/static/adminlte/bower_components/font-awesome/css/font-awesome.min.css">
<link rel="stylesheet" href="${request.contextPath}/static/adminlte/bower_components/Ionicons/css/ionicons.min.css">
<link rel="stylesheet" href="${request.contextPath}/static/adminlte/dist/css/AdminLTE.min.css">
<link rel="stylesheet" href="${request.contextPath}/static/adminlte/dist/css/skins/_all-skins.min.css">
<!--[if lt IE 9]>
<script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
<script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
<![endif]-->
<link rel="stylesheet" href="${request.contextPath}/static/plugins/nprogress/nprogress.css">

</#macro>

<#-- import: script -->
<#macro commonScript>

<script src="${request.contextPath}/static/adminlte/bower_components/jquery/jquery.min.js"></script>
<script src="${request.contextPath}/static/adminlte/bower_components/bootstrap/js/bootstrap.min.js"></script>
<script src="${request.contextPath}/static/adminlte/dist/js/adminlte.min.js"></script>
<script src="${request.contextPath}/static/adminlte/bower_components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
<script src="${request.contextPath}/static/adminlte/bower_components/fastclick/fastclick.js"></script>
<script src="${request.contextPath}/static/plugins/jquery/jquery.validate.min.js"></script>
<script src="${request.contextPath}/static/plugins/layer/layer.js"></script>
<script src="${request.contextPath}/static/plugins/nprogress/nprogress.js"></script>
<script src="${request.contextPath}/static/plugins/fullscreen/jquery.fullscreen.js"></script>
<script>
	// init page param
	var base_url = '${request.contextPath}';
	var I18n = ${I18nUtil.getMultString()};
</script>

</#macro>
