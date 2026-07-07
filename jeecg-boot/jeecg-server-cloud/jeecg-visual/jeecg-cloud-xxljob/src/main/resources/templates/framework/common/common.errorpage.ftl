<!DOCTYPE html>
<html>
<head>
	<#-- import macro -->
	<#import "./common.macro.ftl" as netCommon>

	<meta charset="UTF-8">
	<title>Error</title>
    <style type="text/css"> 
        body { background-color: #fff; color: #666; text-align: center; font-family: arial, sans-serif; }
        div.dialog {
            width: 80%;
            padding: 1em 4em;
            margin: 4em auto 0 auto;
            border: 1px solid #ccc;
            border-right-color: #999;
            border-bottom-color: #999;
        }
        h1 { font-size: 100%; color: #f00; line-height: 1.5em; }
    </style>
    
</head> 
</head>
<body> 

	<div class="dialog"> 
	    <h1>System Error</h1>
		<p><#if exceptionMsg?exists>${exceptionMsg}<#else>Unknown Error.</#if></p>
		<a href="javascript:window.location.href='${request.contextPath}/'" id="go_back" >Back</a>
	    </p> 
	</div>

<!-- 3-script start -->
<script src="${request.contextPath}/static/adminlte/bower_components/jquery/jquery.min.js"></script>
<script src="${request.contextPath}/static/biz/common/admin.util.js"></script>
<script>
	$(function() {
		if (isOpenWithTab()) {
			$('#go_back').hide();
		}
	});
</script>
<!-- 3-script end -->

</body>
</html>