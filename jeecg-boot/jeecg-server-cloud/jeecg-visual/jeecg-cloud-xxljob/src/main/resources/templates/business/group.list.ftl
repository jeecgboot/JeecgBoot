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
							<span class="input-group-addon">AppName</span>
							<input type="text" class="form-control" id="appname" placeholder="${I18n.system_please_input}AppName" >
						</div>
					</div>
					<div class="col-xs-3">
						<div class="input-group">
							<span class="input-group-addon">${I18n.jobgroup_field_title}</span>
							<input type="text" class="form-control" id="title" placeholder="${I18n.jobgroup_field_title}" >
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
						<button class="btn btn-sm btn-danger selectOnlyOne delete" type="button"><i class="fa fa-remove "></i>${I18n.system_opt_del}</button>
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

		<!-- 注册列表查看.模态框 -->
		<div class="modal fade" id="showRegistryListModal" tabindex="-1" role="dialog"  aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<h4 class="modal-title" >${I18n.jobinfo_opt_registryinfo}</h4>
					</div>
					<div class="modal-body">
						<div class="data" style="word-wrap: break-word;"></div>
					</div>
					<div class="modal-footer">
						<div class="text-center" >
							<button type="button" class="btn btn-info ok" data-dismiss="modal" >${I18n.system_ok}</button>
						</div>
					</div>
				</div>
			</div>
		</div>

		<!-- 新增.模态框 -->
		<div class="modal fade" id="addModal" tabindex="-1" role="dialog"  aria-hidden="true">
			<div class="modal-dialog ">
				<div class="modal-content">
					<div class="modal-header">
						<h4 class="modal-title" >${I18n.jobgroup_add}</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal form" role="form" >
							<div class="form-group">
								<label for="lastname" class="col-sm-2 control-label">AppName<font color="red">*</font></label>
								<div class="col-sm-10"><input type="text" class="form-control" name="appname" placeholder="${I18n.system_please_input}AppName" maxlength="64" ></div>
							</div>
							<div class="form-group">
								<label for="lastname" class="col-sm-2 control-label">${I18n.jobgroup_field_title}<font color="red">*</font></label>
								<div class="col-sm-10"><input type="text" class="form-control" name="title" placeholder="${I18n.system_please_input}${I18n.jobgroup_field_title}" maxlength="64" ></div>
							</div>
							<div class="form-group">
								<label for="lastname" class="col-sm-2 control-label">${I18n.jobgroup_field_addressType}<font color="red">*</font></label>
								<div class="col-sm-10">
									<input type="radio" name="addressType" value="0" checked />${I18n.jobgroup_field_addressType_0}
									&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="radio" name="addressType" value="1" />${I18n.jobgroup_field_addressType_1}
								</div>
							</div>
							<div class="form-group">
								<label for="lastname" class="col-sm-2 control-label">${I18n.jobgroup_field_registryList}<font color="red">*</font></label>
								<div class="col-sm-10">
									<textarea class="textarea" name="addressList" maxlength="20000" placeholder="${I18n.jobgroup_field_registryList_placeholder}" readonly="readonly" style="background-color:#eee; width: 100%; height: 100px; font-size: 14px; line-height: 15px; border: 1px solid #dddddd; padding: 5px;"></textarea>
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
			<div class="modal-dialog ">
				<div class="modal-content">
					<div class="modal-header">
						<h4 class="modal-title" >${I18n.jobgroup_edit}</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal form" role="form" >
							<div class="form-group">
								<label for="lastname" class="col-sm-2 control-label">AppName<font color="red">*</font></label>
								<div class="col-sm-10"><input type="text" class="form-control" name="appname" placeholder="${I18n.system_please_input}AppName" maxlength="64" ></div>
							</div>
							<div class="form-group">
								<label for="lastname" class="col-sm-2 control-label">${I18n.jobgroup_field_title}<font color="red">*</font></label>
								<div class="col-sm-10"><input type="text" class="form-control" name="title" placeholder="${I18n.system_please_input}${I18n.jobgroup_field_title}" maxlength="64" ></div>
							</div>
							<div class="form-group">
								<label for="lastname" class="col-sm-2 control-label">${I18n.jobgroup_field_addressType}<font color="red">*</font></label>
								<div class="col-sm-10">
									<input type="radio" name="addressType" value="0" />${I18n.jobgroup_field_addressType_0}
									&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="radio" name="addressType" value="1" />${I18n.jobgroup_field_addressType_1}
								</div>
							</div>
							<div class="form-group">
								<label for="lastname" class="col-sm-2 control-label">${I18n.jobgroup_field_registryList}<font color="red">*</font></label>
								<div class="col-sm-10">
									<textarea class="textarea" name="addressList" maxlength="20000" placeholder="${I18n.jobgroup_field_registryList_placeholder}" readonly="readonly" style="background-color:#eee; width: 100%; height: 100px; font-size: 14px; line-height: 15px; border: 1px solid #dddddd; padding: 5px;"></textarea>
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
			url: base_url + "/jobgroup/pageList",
			queryParams: function (params) {
				var obj = {};
				obj.appname = $('#appname').val();
				obj.title = $('#title').val();
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
					title: 'AppName',
					field: 'appname',
					width: '30',
					widthUnit: '%',
					align: 'left'
				},{
					title: I18n.jobgroup_field_title,
					field: 'title',
					width: '30',
					widthUnit: '%'
				},{
					title: I18n.jobgroup_field_addressType,
					field: 'addressType',
					width: '10',
					widthUnit: '%',
					formatter: function(value, row, index) {
						if (row.addressType == 0) {
							return I18n.jobgroup_field_addressType_0;
						} else {
							return I18n.jobgroup_field_addressType_1;
						}
					}
				},{
					title: 'OnLine' + I18n.jobgroup_field_registryList,
					field: 'registryList',
					width: '15',
					widthUnit: '%',
					formatter: function(value, row, index) {
						tableData['key' + row.id] = row;
						return row.registryList
								?'<a class="show_registryList" href="javascript:;" _id="'+ row.id +'" >' + I18n.system_show +' ( ' + row.registryList.length+ ' ）</a>'
								:I18n.system_empty;
					}
				}
			]
		});

		// job registryinfo
		var tableData = {};
		$("#data_list").on('click', '.show_registryList',function() {
			var id = $(this).attr("_id");
			var row = tableData['key'+id];

			var html = '<table class="table table-bordered"><tbody>';
			if (row.registryList) {
				for (var index in row.registryList) {
					html += '<tr><th class="col-md-3" >' + (parseInt(index)+1) + '</th>';
					html += '<th class="col-md-9" ><span class="badge bg-green" >' + row.registryList[index] + '</span></th><tr>';
				}
			}
			html += '</tbody></table>';

			$('#showRegistryListModal .data').html(html);
			$('#showRegistryListModal').modal({backdrop: false, keyboard: false}).modal('show');
		});

		/**
		 * init delete
		 */
		$.adminTable.initDelete({
			url: base_url + "/jobgroup/delete"
		});

		/**
		 * init add
		 */
		// add validator method
		jQuery.validator.addMethod("myValid01", function(value, element) {
			var length = value.length;
			var valid = /^[a-z][a-zA-Z0-9-]*$/;
			return this.optional(element) || valid.test(value);
		}, I18n.jobgroup_field_appname_limit );
		$.adminTable.initAdd( {
			url: base_url + "/jobgroup/insert",
			rules : {
				appname : {
					required : true,
					rangelength:[4,64],
					myValid01 : true
				},
				title : {
					required : true,
					rangelength:[4, 64]
				}
			},
			messages : {
				appname : {
					required : I18n.system_please_input+"AppName",
					rangelength: I18n.jobgroup_field_appname_length ,
					myValid01: I18n.jobgroup_field_appname_limit
				},
				title : {
					required : I18n.system_please_input + I18n.jobgroup_field_title ,
					rangelength: I18n.jobgroup_field_title_length
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

		// addressType change
		$("#addModal input[name=addressType], #updateModal input[name=addressType]").click(function(){
			var addressType = $(this).val();
			var $addressList = $(this).parents("form").find("textarea[name=addressList]");
			if (addressType == 0) {
				$addressList.css("background-color", "#eee");	// 自动注册
				$addressList.attr("readonly","readonly");
				$addressList.val("");
			} else {
				$addressList.css("background-color", "white");
				$addressList.removeAttr("readonly");
			}
		});

		/**
		 * init update
		 */
		$.adminTable.initUpdate( {
			url: base_url + "/jobgroup/update",
			writeFormData: function(row) {

				$("#updateModal .form input[name='id']").val( row.id );
				$("#updateModal .form input[name='appname']").val( row.appname );
				$("#updateModal .form input[name='title']").val( row.title );

				// 注册方式
				$("#updateModal .form input[name='addressType']").removeAttr('checked');
				$("#updateModal .form input[name='addressType'][value='"+ row.addressType +"']").click();
				// 机器地址
				$("#updateModal .form textarea[name='addressList']").val( row.addressList );

			},
			rules : {
				appname : {
					required : true,
					rangelength:[4,64],
					myValid01 : true
				},
				title : {
					required : true,
					rangelength:[4, 64]
				}
			},
			messages : {
				appname : {
					required : I18n.system_please_input+"AppName",
					rangelength: I18n.jobgroup_field_appname_length ,
					myValid01: I18n.jobgroup_field_appname_limit
				},
				title : {
					required : I18n.system_please_input + I18n.jobgroup_field_title ,
					rangelength: I18n.jobgroup_field_title_length
				}
			},
			readFormData: function() {
				// request
				return $("#updateModal .form").serializeArray();
			}
		});

	});

</script>
<!-- 3-script end -->

</body>
</html>