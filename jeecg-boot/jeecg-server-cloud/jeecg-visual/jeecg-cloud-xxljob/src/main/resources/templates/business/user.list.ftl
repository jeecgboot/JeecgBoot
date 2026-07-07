<!DOCTYPE html>
<html>
<head>
	<#-- import macro -->
	<#import "../framework/common/common.macro.ftl" as netCommon>

	<!-- 1-style start -->
	<@netCommon.commonStyle />
	<link rel="stylesheet" href="${request.contextPath}/static/plugins/bootstrap-table/bootstrap-table.min.css">
	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/iCheck/square/blue.css">
	<!-- 1-style end -->

</head>
<body class="hold-transition" style="background-color: #ecf0f5;">
<div class="wrapper">
	<section class="content">

		<!-- 2-content start -->

		<#-- 查询区域 -->
		<div class="box" style="margin-bottom:9px;">
			<div class="box-body">
				<div class="row" id="data_filter" >

					<div class="col-xs-3">
						<div class="input-group">
							<span class="input-group-addon">${I18n.user_role}</span>
							<select class="form-control" id="role" >
								<option value="-1" >${I18n.system_all}</option>
								<option value="1" >${I18n.user_role_admin}</option>
								<option value="0" >${I18n.user_role_normal}</option>
							</select>
						</div>
					</div>
					<div class="col-xs-3">
						<div class="input-group">
							<span class="input-group-addon">${I18n.user_username}</span>
							<input type="text" class="form-control" id="username" autocomplete="on" >
						</div>
					</div>

					<div class="col-xs-1">
						<button class="btn btn-block btn-primary searchBtn" >${I18n.system_search}</button>
					</div>
					<div class="col-xs-1">
						<button class="btn btn-block btn-default resetBtn" >${I18n.system_reset}</button>
					</div>
				</div>
			</div>
		</div>

		<#-- 数据表格区域 -->
		<div class="row">
			<div class="col-xs-12">
				<div class="box">
					<div class="box-header pull-left" id="data_operation" >
						<button class="btn btn-sm btn-info add" type="button"><i class="fa fa-plus" ></i>${I18n.system_opt_add}</button>
						<button class="btn btn-sm btn-warning selectOnlyOne update" type="button"><i class="fa fa-edit"></i>${I18n.system_opt_edit}</button>
						<button class="btn btn-sm btn-danger selectAny delete" type="button"><i class="fa fa-remove "></i>${I18n.system_opt_del}</button>
					</div>
					<div class="box-body" >
						<table id="data_list" class="table table-bordered table-striped" width="100%" >
							<thead></thead>
							<tbody></tbody>
							<tfoot></tfoot>
						</table>
					</div>
				</div>
			</div>
		</div>

		<!-- 新增.模态框 -->
		<div class="modal fade" id="addModal" tabindex="-1" role="dialog"  aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<h4 class="modal-title" >${I18n.user_add}</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal form" role="form" >
							<div class="form-group">
								<label for="lastname" class="col-sm-2 control-label">${I18n.user_username}<font color="red">*</font></label>
								<div class="col-sm-8"><input type="text" class="form-control" name="username" placeholder="${I18n.system_please_input}${I18n.user_username}" maxlength="20" ></div>
							</div>
							<div class="form-group">
								<label for="lastname" class="col-sm-2 control-label">${I18n.user_password}<font color="red">*</font></label>
								<div class="col-sm-8"><input type="text" class="form-control" name="password" placeholder="${I18n.system_please_input}${I18n.user_password}" maxlength="20" ></div>
							</div>
							<div class="form-group">
								<label for="lastname" class="col-sm-2 control-label">${I18n.user_role}<font color="red">*</font></label>
								<div class="col-sm-10">
									<input type="radio" name="role" value="0" checked />${I18n.user_role_normal}
									&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="radio" name="role" value="1" />${I18n.user_role_admin}
								</div>
							</div>
							<div class="form-group">
								<label for="lastname" class="col-sm-2 control-label">${I18n.user_permission}<font color="black">*</font></label>
								<div class="col-sm-10">
									<#if groupList?exists && groupList?size gt 0>
										<#list groupList as item>
											<input type="checkbox" name="permission" value="${item.id}" />&nbsp;&nbsp;${item.title}：${item.appname}
											<br>
										</#list>
									</#if>
								</div>
							</div>

							<hr>
							<div class="form-group">
								<div class="col-sm-offset-3 col-sm-6">
									<button type="submit" class="btn btn-primary"  >${I18n.system_save}</button>
									<button type="button" class="btn btn-default" data-dismiss="modal">${I18n.system_cancel}</button>
								</div>
							</div>

						</form>
					</div>
				</div>
			</div>
		</div>

		<!-- 更新.模态框 -->
		<div class="modal fade" id="updateModal" tabindex="-1" role="dialog"  aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<h4 class="modal-title" >${I18n.user_update}</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal form" role="form" >
							<div class="form-group">
								<label for="lastname" class="col-sm-2 control-label">${I18n.user_username}<font color="red">*</font></label>
								<div class="col-sm-8"><input type="text" class="form-control" name="username" placeholder="${I18n.system_please_input}${I18n.user_username}" maxlength="20" readonly ></div>
							</div>
							<div class="form-group">
								<label for="lastname" class="col-sm-2 control-label">${I18n.user_password}<font color="red">*</font></label>
								<div class="col-sm-8"><input type="text" class="form-control" name="password" placeholder="${I18n.user_password_update_placeholder}" maxlength="20" ></div>
							</div>
							<div class="form-group">
								<label for="lastname" class="col-sm-2 control-label">${I18n.user_role}<font color="red">*</font></label>
								<div class="col-sm-10">
									<input type="radio" name="role" value="0" />${I18n.user_role_normal}
									&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="radio" name="role" value="1" />${I18n.user_role_admin}
								</div>
							</div>
							<div class="form-group">
								<label for="lastname" class="col-sm-2 control-label">${I18n.user_permission}<font color="black">*</font></label>
								<div class="col-sm-10">
									<#if groupList?exists && groupList?size gt 0>
										<#list groupList as item>
											<input type="checkbox" name="permission" value="${item.id}" />${item.title}(${item.appname})<br>
										</#list>
									</#if>
								</div>
							</div>

							<hr>
							<div class="form-group">
								<div class="col-sm-offset-3 col-sm-6">
									<button type="submit" class="btn btn-primary"  >${I18n.system_save}</button>
									<button type="button" class="btn btn-default" data-dismiss="modal">${I18n.system_cancel}</button>
									<input type="hidden" name="id" >
								</div>
							</div>

						</form>
					</div>
				</div>
			</div>
		</div>

		<!-- 2-content end -->

	</section>
</div>

<!-- 3-script start -->
<@netCommon.commonScript />
<script src="${request.contextPath}/static/plugins/bootstrap-table/bootstrap-table.min.js"></script>
<script src="${request.contextPath}/static/plugins/bootstrap-table/locale/<#if I18n.admin_i18n?? && I18n.admin_i18n == 'en'>bootstrap-table-en-US.min.js<#else>bootstrap-table-zh-CN.min.js</#if>"></script>
<script src="${request.contextPath}/static/adminlte/plugins/iCheck/icheck.min.js"></script>
<#-- admin table -->
<script src="${request.contextPath}/static/biz/common/admin.table.js"></script>
<script>
	$(function() {

		/**
		 * init table
		 */
		$.adminTable.initTable({
			table: '#data_list',
			url: base_url + "/user/pageList",
			queryParams: function (params) {
				var obj = {};
				obj.username = $('#username').val();
				obj.role = $('#role').val();
				obj.offset = params.offset;
				obj.pagesize = params.limit;
				return obj;
			},
			columns:[
				{
					checkbox: true,
					field: 'state',
					width: '5',
					widthUnit: '%',
					align: 'center',
					valign: 'middle'
				},{
					title: I18n.user_username,
					field: 'username',
					width: '20',
					widthUnit: '%',
					align: 'left'
				},{
					title: I18n.user_password,
					field: 'password',
					width: '20',
					widthUnit: '%',
					formatter: function(value, row, index) {
						return '******';
					}
				},{
					title: I18n.user_role,
					field: 'role',
					width: '10',
					widthUnit: '%',
					formatter: function(value, row, index) {
						let result = value;
						$('#data_filter #role option').each(function(){
							if ( value+"" === $(this).val() ) {
								result = $(this).text();
							}
						});
						return result;
					}
				}
			]
		});

		/**
		 * init delete
		 */
		$.adminTable.initDelete({
			url: base_url + "/user/delete"
		});

		/**
		 * init add
		 */
		// add validator method
		jQuery.validator.addMethod("myValid01", function(value, element) {
			var length = value.length;
			var valid = /^[a-z][a-z0-9]*$/;
			return this.optional(element) || valid.test(value);
		}, I18n.user_username_valid );
		$.adminTable.initAdd( {
			url: base_url + "/user/insert",
			rules : {
				username : {
					required : true,
					rangelength:[4, 20],
					myValid01: true
				},
				password : {
					required : true,
					rangelength:[4, 20]
				}
			},
			messages : {
				username : {
					required : I18n.system_please_input + I18n.user_username,
					rangelength: I18n.system_length_limit + "[4-20]"
				},
				password : {
					required : I18n.system_please_input + I18n.user_password,
					rangelength: I18n.system_length_limit + "[4-20]"
				}
			},
			writeFormData: function() {
				$("#addModal .form input[name='role'][value='0']").change();
			},
			readFormData: function() {
				// request
				return $("#addModal .form").serializeArray();
			}
		});

		// add role
		$("#addModal .form input[name=role]").change(function () {
			var role = $(this).val();
			if (role == 1) {
				$("#addModal .form input[name=permission]").parents('.form-group').hide();
			} else {
				$("#addModal .form input[name=permission]").parents('.form-group').show();
			}
			$("#addModal .form input[name='permission']").prop("checked",false);
		});

		/**
		 * init update
		 */
		$.adminTable.initUpdate( {
			url: base_url + "/user/update",
			writeFormData: function(row) {

				// base data
				$("#updateModal .form input[name='id']").val( row.id );
				$("#updateModal .form input[name='username']").val( row.username );
				$("#updateModal .form input[name='password']").val( '' );
				$("#updateModal .form input[name='role'][value='"+ row.role +"']").click();
				var permissionArr = [];
				if (row.permission) {
					permissionArr = row.permission.split(",");
				}
				$("#updateModal .form input[name='permission']").each(function () {
					if($.inArray($(this).val(), permissionArr) > -1) {
						$(this).prop("checked",true);
					} else {
						$(this).prop("checked",false);
					}
				});

			},
			readFormData: function() {
				// request
				return $("#updateModal .form").serializeArray();
			}
		});

		// update role
		$("#updateModal .form input[name=role]").change(function () {
			var role = $(this).val();
			if (role == 1) {
				$("#updateModal .form input[name=permission]").parents('.form-group').hide();
			} else {
				$("#updateModal .form input[name=permission]").parents('.form-group').show();
			}
			$("#updateModal .form input[name='permission']").prop("checked",false);
		});

	});

</script>
<!-- 3-script end -->

</body>
</html>