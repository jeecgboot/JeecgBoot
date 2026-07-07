<!DOCTYPE html>
<html>
<head>
	<#-- import macro -->
	<#import "../framework/common/common.macro.ftl" as netCommon>

	<!-- 1-style start -->
	<@netCommon.commonStyle />
	<link rel="stylesheet" href="${request.contextPath}/static/plugins/codemirror/lib/codemirror.css">
	<link rel="stylesheet" href="${request.contextPath}/static/plugins/codemirror/addon/hint/show-hint.css">
	<title>${I18n.admin_name}</title>
	<style type="text/css" >
		.CodeMirror {
			font-size:16px;
			width: 100%;
			height: 100%;
			/*bottom: 0;
            top: 0px;*/
			position: absolute;
		}
	</style>
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
					<a class="navbar-brand" href="javascript:void(0);" ><b>Web</b>IDE</a>
					<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse">
						<i class="fa fa-bars"></i>
					</button>
				</div>

				<#-- left nav -->
				<div class="collapse navbar-collapse pull-left" id="navbar-collapse">
					<ul class="nav navbar-nav">
						<li class="active" >
							<a href="javascript:;">${I18n.jobinfo_job}：${jobInfo.jobDesc} ｜<#list GlueTypeEnum as item><#if item == jobInfo.glueType>${item.desc}</#if></#list></a>
						</li>
					</ul>
				</div>

				<#-- right nav -->
				<div class="navbar-custom-menu">
					<ul class="nav navbar-nav">
						<li class="dropdown">
							<a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false">${I18n.jobinfo_glue_rollback} <span class="caret"></span></a>
							<ul class="dropdown-menu" role="menu">
								<li <#if jobLogGlues?exists && jobLogGlues?size gt 0 >style="display: none;"</#if> >
									<a href="javascript:;" class="source_version" version="version_now" glueType="${jobInfo.glueType}" >
										<#list GlueTypeEnum as item><#if item == jobInfo.glueType>${item.desc}</#if></#list>： ${jobInfo.glueRemark}
									</a>
								</li>
								<textarea id="version_now" style="display:none;" >${jobInfo.glueSource}</textarea>
								<#if jobLogGlues?exists && jobLogGlues?size gt 0 >
									<#list jobLogGlues as glue>
										<li>
											<a href="javascript:;" class="source_version" version="version_${glue.id}" glueType="${glue.glueType}" >
												${glue.addTime?string["yyyy-MM-dd HH:mm:ss"]}： ${glue.glueRemark}
											</a>
										</li>
										<textarea id="version_${glue.id}" style="display:none;" >${glue.glueSource}</textarea>
									</#list>
								</#if>
							</ul>
						</li>
						<li id="save" >
							<a href="javascript:;" >
								<i class="fa fa-fw fa-save" ></i>
								${I18n.system_save}
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
	<div class="content-wrapper" id="ideWindow" >
	</div>

	<!-- 保存.模态框 -->
	<div class="modal fade" id="saveModal" tabindex="-1" role="dialog"  aria-hidden="true">
		<div class="modal-dialog ">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" ><i class="fa fa-fw fa-save"></i>${I18n.system_save}</h4>
				</div>
				<div class="modal-body">
					<div class="form-horizontal form" role="form" >
						<div class="form-group">
							<label for="lastname" class="col-sm-2 control-label">${I18n.jobinfo_glue_remark}<font color="red">*</font></label>
							<div class="col-sm-10"><input type="text" class="form-control" id="glueRemark" placeholder="${I18n.system_please_input}${I18n.jobinfo_glue_remark}" maxlength="64" ></div>
						</div>
						<hr>
						<div class="form-group">
							<div class="col-sm-offset-3 col-sm-6">
								<button type="button" class="btn btn-primary ok" >${I18n.system_save}</button>
								<button type="button" class="btn btn-default" data-dismiss="modal">${I18n.system_cancel}</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
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
<#-- glueModel -->
<#assign glueTypeModeSrc = "${request.contextPath}/static/plugins/codemirror/mode/clike/clike.js" />
<#assign glueTypeIdeMode = "text/x-java" />

<#if jobInfo.glueType == "GLUE_GROOVY" >
	<#assign glueTypeModeSrc = "${request.contextPath}/static/plugins/codemirror/mode/clike/clike.js" />
	<#assign glueTypeIdeMode = "text/x-java" />
<#elseif jobInfo.glueType == "GLUE_SHELL" >
	<#assign glueTypeModeSrc = "${request.contextPath}/static/plugins/codemirror/mode/shell/shell.js" />
	<#assign glueTypeIdeMode = "text/x-sh" />
<#elseif jobInfo.glueType == "GLUE_PYTHON" >
	<#assign glueTypeModeSrc = "${request.contextPath}/static/plugins/codemirror/mode/python/python.js" />
	<#assign glueTypeIdeMode = "text/x-python" />
<#elseif jobInfo.glueType == "GLUE_PYTHON2" >
	<#assign glueTypeModeSrc = "${request.contextPath}/static/plugins/codemirror/mode/python/python.js" />
	<#assign glueTypeIdeMode = "text/x-python" />
<#elseif jobInfo.glueType == "GLUE_PHP" >
	<#assign glueTypeModeSrc = "${request.contextPath}/static/plugins/codemirror/mode/php/php.js" />
	<#assign glueTypeIdeMode = "text/x-php" />
	<#assign glueTypeModeSrc02 = "${request.contextPath}/static/plugins/codemirror/mode/clike/clike.js" />
<#elseif jobInfo.glueType == "GLUE_NODEJS" >
	<#assign glueTypeModeSrc = "${request.contextPath}/static/plugins/codemirror/mode/javascript/javascript.js" />
	<#assign glueTypeIdeMode = "text/javascript" />
<#elseif jobInfo.glueType == "GLUE_POWERSHELL" >
	<#assign glueTypeModeSrc = "${request.contextPath}/static/plugins/codemirror/mode/powershell/powershell.js" />
	<#assign glueTypeIdeMode = "powershell" />
</#if>

<#-- script -->
<@netCommon.commonScript />
<script src="${request.contextPath}/static/biz/common/admin.setting.js?v=${I18n.admin_version}"></script>
<#-- glue ide -->
<script src="${request.contextPath}/static/plugins/codemirror/lib/codemirror.js"></script>
<script src="${glueTypeModeSrc}"></script>
<#if glueTypeModeSrc02?exists>
	<script src="${glueTypeModeSrc02}"></script>
</#if>
<script src="${request.contextPath}/static/plugins/codemirror/addon/hint/show-hint.js"></script>
<script src="${request.contextPath}/static/plugins/codemirror/addon/hint/anyword-hint.js"></script>
<script>
	$(function() {
		// init param
		var id = '${jobInfo.id}';
		var ideMode = '${glueTypeIdeMode}';

		/**
		 * init code editor
 		 */
		var codeEditor;
		function initIde(glueSource) {
			if (codeEditor == null) {
				codeEditor = CodeMirror(document.getElementById("ideWindow"), {
					mode : ideMode,
					lineNumbers : true,
					matchBrackets : true,
					value: glueSource
				});
			} else {
				codeEditor.setValue(glueSource);
			}
		}
		initIde($("#version_now").val());

		// code change
		$(".source_version").click(function(){
			var sourceId = $(this).attr('version');
			var temp = $( "#" + sourceId ).val();

			//codeEditor.setValue('');
			initIde(temp);
		});

		// code source save
		$("#save").click(function() {
			$('#saveModal').modal({backdrop: false, keyboard: false}).modal('show');
		});
		$("#saveModal .ok").click(function() {

			var glueSource = codeEditor.getValue();
			var glueRemark = $("#glueRemark").val();

			if (!glueRemark) {
				layer.open({
					title: I18n.system_tips,
					btn: [ I18n.system_ok],
					content: I18n.system_please_input + I18n.jobinfo_glue_remark ,
					icon: '2'
				});
				return;
			}
			if (glueRemark.length <4 || glueRemark.length > 100) {
				layer.open({
					title: I18n.system_tips ,
					btn: [ I18n.system_ok ],
					content: I18n.jobinfo_glue_remark_limit ,
					icon: '2'
				});
				return;
			}

			$.ajax({
				type : 'POST',
				url : base_url + '/jobcode/save',
				data : {
					'id' : id,
					'glueSource' : glueSource,
					'glueRemark' : glueRemark
				},
				dataType : "json",
				success : function(data){
					if (data.code == 200) {
						layer.open({
							title: I18n.system_tips,
							btn: [ I18n.system_ok ],
							content: (I18n.system_save + I18n.system_success) ,
							icon: '1',
							end: function(layero, index){
								//$(window).unbind('beforeunload');
								window.location.reload();
							}
						});
					} else {
						layer.open({
							title: I18n.system_tips,
							btn: [ I18n.system_ok ],
							content: (data.msg || (I18n.system_save + I18n.system_fail) ),
							icon: '2'
						});
					}
				}
			});

		});

	});
</script>
<!-- 5-script end -->

</body>
</html>