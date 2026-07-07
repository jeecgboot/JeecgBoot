<!DOCTYPE html>
<html>
<head>
	<#-- import macro -->
	<#import "../common/common.macro.ftl" as netCommon>

	<!-- 1-style start -->
	<@netCommon.commonStyle />
	<!-- 1-style end -->

</head>
<body class="hold-transition" style="background-color: #ecf0f5;">
<div class="wrapper">
	<section class="content">

		<#-- 2-biz start -->
		<div class="callout callout-info">
			<h4>${I18n.admin_name_full}</h4>
			<br>
			<p>

				<a target="_blank" href="https://github.com/xuxueli/xxl-job">Github</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<iframe src="https://ghbtns.com/github-btn.html?user=xuxueli&repo=xxl-job&type=star&count=true" frameborder="0" scrolling="0" width="170px" height="20px" style="margin-bottom:-5px;"></iframe>
				<br><br>
				<a target="_blank" href="https://www.xuxueli.com/xxl-job/">${I18n.admin_help_document}</a>
				<br><br>

			</p>
			<p></p>
		</div>
		<#-- 2-biz end -->

	</section>
</div>

<!-- 3-script start -->
<@netCommon.commonScript />
<!-- 3-script end -->

</body>
</html>