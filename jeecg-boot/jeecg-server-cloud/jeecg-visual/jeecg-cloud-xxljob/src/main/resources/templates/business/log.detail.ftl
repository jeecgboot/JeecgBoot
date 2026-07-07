<!DOCTYPE html>
<html>
<head>
	<#-- import macro -->
	<#import "../framework/common/common.macro.ftl" as netCommon>

	<!-- 1-style start -->
	<@netCommon.commonStyle />
	<!-- 1-style end -->

</head>
<body class="hold-transition skin-blue layout-top-nav" >
<div class="wrapper" >

	<!-- 2-header start -->
	<header class="main-header">
		<nav class="navbar navbar-static-top">
			<div class="container">
				<#-- icon -->
				<div class="navbar-header">
					<a class="navbar-brand" href="javascript:void(0);" ><b>${I18n.joblog_rolling_log}</b> Console</a>
					<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse">
						<i class="fa fa-bars"></i>
					</button>
				</div>

				<#-- left nav -->
				<div class="collapse navbar-collapse pull-left" id="navbar-collapse">
					<ul class="nav navbar-nav">
						<li class="active" >
							<a href="javascript:;">${I18n.jobinfo_job}：${jobInfo.jobDesc}</a>
						</li>
					</ul>
				</div>

				<#-- right nav -->
				<div class="navbar-custom-menu">
					<ul class="nav navbar-nav">
						<li>
							<a href="javascript:window.location.reload();" >
								<i class="fa fa-fw fa-refresh" ></i>
								${I18n.joblog_rolling_log_refresh}
							</a>
						</li>
						<li>
							<a href="javascript:window.close();" >
								<i class="fa fa-fw fa-close" ></i>
								${I18n.system_close}
							</a>
						</li>
					</ul>
				</div>

			</div>
		</nav>
	</header>
	<!-- 2-header end -->

	<!-- 3-content start -->
	<div class="content-wrapper">
		<section class="content">
			<!-- rolling log -->
			<pre style="font-size:12px;position:relative;" >
				<div id="logConsole"></div>
				<li class="fa fa-refresh fa-spin" style="font-size: 20px;float: left;" id="logConsoleRunning" ></li>
			</pre>
		</section>
	</div>
	<!-- 3-content end -->

	<!-- 4-footer start -->
	<footer class="main-footer">
		Powered by <b>XXL-JOB</b> ${I18n.admin_version}
		<div class="pull-right hidden-xs">
			<strong>Copyright &copy; 2015-${.now?string('yyyy')} &nbsp;
				<a href="https://www.xuxueli.com/" target="_blank" >xuxueli</a>
				&nbsp;
				<a href="https://github.com/xuxueli/xxl-job" target="_blank" >github</a>
			</strong><!-- All rights reserved. -->
		</div>
	</footer>
	<!-- 4-footer end -->

</div>

<!-- 5-script start -->
<@netCommon.commonScript />
<script src="${request.contextPath}/static/biz/common/admin.setting.js?v=${I18n.admin_version}"></script>
<script>
	$(function() {

		// init param
		var triggerCode = '${triggerCode}';
		var handleCode = '${handleCode}';
		var logId = '${logId}';

		// trigger fail and not handle
        if (triggerCode != 200 && handleCode == 0) {
			$('#logConsoleRunning').hide();
			$('#logConsole').append('<span style="color: red;">['+ I18n.joblog_rolling_log_triggerfail +']</span>');
			return;
		}

		/**
		 * pull log
		 */
		var fromLineNum = 1;    // [from, to], start as 1
		var pullFailCount = 0;
		function pullLog() {
            // limit max pull-fail count, max=20
			if (pullFailCount++ > 20) {
				logRunStop('<span style="color: red;">['+ I18n.joblog_rolling_log_failoften +']</span>');
				return;
			}

			// load
			console.log("pullLog, fromLineNum:" + fromLineNum);
			$.ajax({
				type : 'POST',
				async: false,   // sync, make log ordered
				url : base_url + '/joblog/logDetailCat',
				data : {
					"logId":logId,
					"fromLineNum":fromLineNum
				},
				dataType : "json",
				success : function(data){

					if (data.code == 200) {
                        // pull fail
						if (!data.data) {
							console.log('pullLog fail');
							return;
						}
						if (fromLineNum != data.data.fromLineNum) {
							console.log('pullLog fromLineNum not match');
							return;
						}
                        // pull to end
						if (fromLineNum > data.data.toLineNum ) {
							console.log('pullLog already line-end');

							// valid end
							if (data.data.end) {
								logRunStop('<br><span style="color: green;">[Rolling Log End]</span>');
								return;
							}
							return;
						}

						// append content
						fromLineNum = data.data.toLineNum + 1;
						$('#logConsole').append(data.data.logContent);
						pullFailCount = 0;

						// scroll to bottom
						scrollTo(0, document.body.scrollHeight);        // $('#logConsolePre').scrollTop( document.body.scrollHeight + 300 );

					} else {
                        // pull fail
						console.log('pullLog fail:'+data.msg);
                        $('#logConsole').append('<span style="color: red;">[Rolling Log Error]: '+ data.msg +'</span>');
					}
				}
			});
		}

		// pull first page
		pullLog();

		// if handle already callback, stop cycle pull
		if (handleCode > 0) {
			logRunStop('<br><span style="color: green;">[Rolling Log Finish]</span>');
			return;
		}

		/**
		 * cycle pull, until end
		 */
		var logRun = setInterval(function () {
			pullLog()
		}, 3000);
		function logRunStop(content){
			$('#logConsoleRunning').hide();
			logRun = window.clearInterval(logRun);
			$('#logConsole').append(content);
		}

	});
</script>
<!-- 5-script end -->

</body>
</html>