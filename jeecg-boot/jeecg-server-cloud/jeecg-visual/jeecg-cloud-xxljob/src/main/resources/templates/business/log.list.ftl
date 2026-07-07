<!DOCTYPE html>
<html>
<head>
	<#-- import macro -->
	<#import "../framework/common/common.macro.ftl" as netCommon>

	<!-- 1-style start -->
	<#-- select2：should before adminlte -->
	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/bower_components/select2/select2.min.css">
	<@netCommon.commonStyle />
	<link rel="stylesheet" href="${request.contextPath}/static/plugins/bootstrap-table/bootstrap-table.min.css">
	<!-- daterangepicker -->
	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/bower_components/bootstrap-daterangepicker/daterangepicker.css">
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

					<div class="col-xs-2">
						<div class="input-group">
							<span class="input-group-addon">${I18n.jobinfo_field_jobgroup}</span>
							<select class="form-control" id="jobGroup"  >
								<#list JobGroupList as group>
									<option value="${group.id}" <#if jobGroup==group.id>selected</#if> >${group.title}</option>
								</#list>
							</select>
						</div>
					</div>
					<div class="col-xs-3">
						<div class="input-group">
							<span class="input-group-addon">${I18n.jobinfo_job}</span>
							<select class="form-control" id="jobId" >
								<option value="0" >${I18n.system_all}</option>
								<#if jobInfoList?size gt 0>
									<#list jobInfoList as jobItem>
										<option value="${jobItem.id}" >${jobItem.jobDesc}</option>
									</#list>
								</#if>
							</select>
						</div>
					</div>
					<div class="col-xs-1">
						<div class="input-group">
							<#--<span class="input-group-addon">${I18n.joblog_status}</span>-->
							<select class="form-control" id="logStatus" >
								<option value="-1" >${I18n.joblog_status_all}</option>
								<option value="1" >${I18n.joblog_status_suc}</option>
								<option value="2" >${I18n.joblog_status_fail}</option>
								<option value="3" >${I18n.joblog_status_running}</option>
							</select>
						</div>
					</div>
					<div class="col-xs-4">
						<div class="input-group">
                		<span class="input-group-addon">
	                  		${I18n.joblog_field_triggerTime}
	                	</span>
							<input type="text" class="form-control" id="filterTime" readonly >
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
						<button class="btn btn-sm btn-warning selectOnlyOne logKill" type="button">${I18n.joblog_kill_log}</button>
						<button class="btn btn-sm btn-danger selectAny clearLog" type="button">${I18n.joblog_clean_log}</button>
						｜
						<button class="btn btn-sm btn-primary selectOnlyOne logDetail" type="button"><#--<i class="fa fa-edit"></i>-->${I18n.joblog_rolling_log}</button>
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

		<!-- 日志清理.模态框 -->
		<div class="modal fade" id="clearLogModal" tabindex="-1" role="dialog"  aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<h4 class="modal-title" >${I18n.joblog_clean_log}</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal form" role="form" >
							<div class="form-group">
								<label class="col-sm-3 control-label">${I18n.jobinfo_field_jobgroup}：</label>
								<div class="col-sm-9">
									<input type="text" class="form-control jobGroupText" readonly >
									<input type="hidden" name="jobGroup" >
								</div>
							</div>

							<div class="form-group">
								<label class="col-sm-3 control-label">${I18n.jobinfo_job}：</label>
								<div class="col-sm-9">
									<input type="text" class="form-control jobIdText" readonly >
									<input type="hidden" name="jobId" >
								</div>
							</div>

							<div class="form-group">
								<label class="col-sm-3 control-label">${I18n.joblog_clean_type}：</label>
								<div class="col-sm-9">
									<select class="form-control" name="type" >
										<option value="1" >${I18n.joblog_clean_type_1}</option>
										<option value="2" >${I18n.joblog_clean_type_2}</option>
										<option value="3" >${I18n.joblog_clean_type_3}</option>
										<option value="4" >${I18n.joblog_clean_type_4}</option>
										<option value="5" >${I18n.joblog_clean_type_5}</option>
										<option value="6" >${I18n.joblog_clean_type_6}</option>
										<option value="7" >${I18n.joblog_clean_type_7}</option>
										<option value="8" >${I18n.joblog_clean_type_8}</option>
										<option value="9" >${I18n.joblog_clean_type_9}</option>
									</select>
								</div>
							</div>

							<hr>
							<div class="form-group">
								<div class="col-sm-offset-3 col-sm-6">
									<button type="button" class="btn btn-primary ok" >${I18n.system_ok}</button>
									<button type="button" class="btn btn-default" data-dismiss="modal">${I18n.system_cancel}</button>
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
<#--daterangepicker-->
<script src="${request.contextPath}/static/adminlte/bower_components/moment/moment.min.js"></script>
<script src="${request.contextPath}/static/adminlte/bower_components/bootstrap-daterangepicker/daterangepicker.js"></script>
<#-- select2 -->
<script src="${request.contextPath}/static/adminlte/bower_components/select2/select2.min.js"></script>
<#-- admin table -->
<script src="${request.contextPath}/static/biz/common/admin.table.js"></script>
<script>
	$(function() {

		// ---------------------- filter ----------------------

		// select2: init
		$('#jobGroup').select2();
		$('#jobId').select2();

		/**
		 * jobGroup change
 		 */
		$('#jobGroup').on('change', function(){
			//reload
			var jobGroup = $('#jobGroup').val();
			window.location.href = base_url + "/joblog?jobGroup=" + jobGroup;
		});

		/**
		 * filter Time
		 */
		var rangesConf = {};
		rangesConf[I18n.daterangepicker_ranges_today] = [moment().startOf('day'), moment().endOf('day')];
		rangesConf[I18n.daterangepicker_ranges_yesterday] = [moment().subtract(1, 'days').startOf('day'), moment().subtract(1, 'days').endOf('day')];
		rangesConf[I18n.daterangepicker_ranges_this_month] = [moment().startOf('month'), moment().endOf('month')];
		rangesConf[I18n.daterangepicker_ranges_last_month] = [moment().subtract(1, 'months').startOf('month'), moment().subtract(1, 'months').endOf('month')];
		rangesConf[I18n.daterangepicker_ranges_recent_week] = [moment().subtract(1, 'weeks').startOf('day'), moment().endOf('day')];
		rangesConf[I18n.daterangepicker_ranges_recent_month] = [moment().subtract(1, 'months').startOf('day'), moment().endOf('day')];

		$('#filterTime').daterangepicker({
			autoApply:false,
			singleDatePicker:false,		// 范围选择 or 单时间选择
			showDropdowns:true,         // 年月 选择条件是否为下拉框
			timePicker: true,
			timePicker24Hour: true,
			timePickerSeconds: true,	// 时间选择是否显示秒
			opens : 'left', 			// 日期选择框的弹出位置
			ranges: rangesConf,
			locale : {
				format: 'YYYY-MM-DD HH:mm:ss',
				separator : ' - ',
				customRangeLabel : I18n.daterangepicker_custom_name ,
				applyLabel : I18n.system_ok ,
				cancelLabel : I18n.system_cancel ,
				fromLabel : I18n.daterangepicker_custom_starttime ,
				toLabel : I18n.daterangepicker_custom_endtime ,
				daysOfWeek : I18n.daterangepicker_custom_daysofweek.split(',') ,        // '日', '一', '二', '三', '四', '五', '六'
				monthNames : I18n.daterangepicker_custom_monthnames.split(',') ,        // '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'
				firstDay : 1
			}/*,
			startDate: rangesConf[I18n.daterangepicker_ranges_today][0],
			endDate: rangesConf[I18n.daterangepicker_ranges_today][1]*/
		});

		// init filter
		var jobGroup = '${jobGroup}';
		var jobId = '${jobId}';
		function resetFilter(){
			$('#filterTime').data("daterangepicker").setStartDate( rangesConf[I18n.daterangepicker_ranges_recent_week][0] );
			$('#filterTime').data("daterangepicker").setEndDate( rangesConf[I18n.daterangepicker_ranges_recent_week][1] );

			$("#jobGroup").val( jobGroup );
			$("#jobId").val( jobId ).trigger("change");		// select2: change 	; $("#jobId").val( jobId );
			$('#logStatus').prop('selectedIndex', 0);
		}
		resetFilter();

		// ---------------------- page ----------------------

		/**
		 * init table
		 */
		$.adminTable.initTable({
			table: '#data_list',
			url: base_url + "/joblog/pageList",
			queryParams: function (params) {
				var obj = {};
				obj.jobGroup = $('#jobGroup').val();
				obj.jobId = $('#jobId').val();
				obj.logStatus = $('#logStatus').val();
				obj.filterTime = $('#filterTime').val();
				obj.offset = params.offset;
				obj.pagesize = params.limit;
				return obj;
			},
			resetHandler : function() {
				// reset filter
				resetFilter();
			},
			columns:[
				{
					checkbox: true,
					field: 'state',
					width: '5',
					widthUnit: '%',
					align: 'center',
					valign: 'middle'
				},
                {
                    title: I18n.joblog_name + 'ID',
                    field: 'id',
                    width: '10',
                    widthUnit: '%',
                    align: 'left'
                },
                {
					title: I18n.jobinfo_job,
					field: 'jobId',
					width: '15',
					widthUnit: '%',
					align: 'left',
					formatter: function(value, row, index) {
                        // build
                        let jobShow = '【'+ row.jobId +'】';
                        let jobDesc = $("#jobId").find("option[value='"+ row.jobId +"']").text();
                        if (jobDesc) {
                            jobShow += jobDesc;
                        }
                        if (jobShow.length > 10) {
                            jobShow = jobShow.substr(0, 10) + '...';
                        }
						// show
						return jobShow;
					}
				},{
					title: I18n.joblog_field_triggerTime,
					field: 'triggerTime',
					width: '15',
					widthUnit: '%',
					formatter: function(value, row, index) {
						return value?moment(value).format("YYYY-MM-DD HH:mm:ss"):"";
					}
				},{
					title: I18n.joblog_field_triggerCode,
					field: 'triggerCode',
					width: '10',
					widthUnit: '%',
					formatter: function(value, row, index) {
						var html = value;
						if (value == 200) {			// 200, success
							html = '<span style="color: green">'+ I18n.system_success +'</span>';
						} else if (value > 0) {		// >0 or 500, fail
							html = '<span style="color: red">'+ I18n.system_fail +'</span>';
						} else if (value == 0) {		// 0, original pass
							html = '';
						}
						return html;
					}
				},{
					title: I18n.joblog_field_triggerMsg,
					field: 'triggerMsg',
					width: '10',
					widthUnit: '%',
					formatter: function(value, row, index) {
						return value?'<a class="logTips" href="javascript:;" >'+ I18n.system_show +'<span style="display:none;">'+ value +'</span></a>':I18n.system_empty;
					}
				},{
					title: I18n.joblog_field_handleTime,
					field: 'handleTime',
					width: '15',
					widthUnit: '%',
					formatter: function(value, row, index) {
						return value?moment(value).format("YYYY-MM-DD HH:mm:ss"):"";
					}
				},{
					title: I18n.joblog_field_handleCode,
					field: 'handleCode',
					width: '10',
					widthUnit: '%',
					formatter: function(value, row, index) {
						var html = value;
						if (value == 200) {			// 200, success
							html = '<span style="color: green">'+ I18n.joblog_handleCode_200 +'</span>';
						} else if (value == 502) {	// 502, timeout
							html = '<span style="color: red">'+ I18n.joblog_handleCode_502 +'</span>';
						} else if (value > 0) {		// >0 or 500, fail
							html = '<span style="color: red">'+ I18n.joblog_handleCode_500 +'</span>';
						} else if (value == 0) {		// 0, original pass
							html = '';
						}
						return html;
					}
				},{
					title: I18n.joblog_field_handleMsg,
					field: 'handleMsg',
					width: '10',
					widthUnit: '%',
					formatter: function(value, row, index) {
						return value?'<a class="logTips" href="javascript:;" >'+ I18n.system_show +'<span style="display:none;">'+ value +'</span></a>':I18n.system_empty;
					}
				}
			]
		});

		/**
		 * logDetail
		 */
		$("#data_operation").on('click', '.logDetail',function() {
			// get select rows
			var rows = $.adminTable.table.bootstrapTable('getSelections');

			// find select row
			if (rows.length !== 1) {
				layer.msg(I18n.system_please_choose + I18n.system_one + I18n.system_data);
				return;
			}
			var row = rows[0];

			window.open(base_url + '/joblog/logDetailPage?id=' + row.id);
		});

		/**
		 * log Kill
		 */
		$('#data_operation').on('click', '.logKill', function(){
			// get select rows
			var rows = $.adminTable.table.bootstrapTable('getSelections');

			// find select row
			if (rows.length !== 1) {
				layer.msg(I18n.system_please_choose + I18n.system_one + I18n.system_data);
				return;
			}
			var row = rows[0];

			// do kill
			layer.confirm( (I18n.system_ok + I18n.joblog_kill_log + '?'), {
				icon: 3,
				title: I18n.system_tips ,
				btn: [ I18n.system_ok, I18n.system_cancel ]
			}, function(index){
				layer.close(index);

				$.ajax({
					type : 'POST',
					url : base_url + '/joblog/logKill',
					data : {
						"id": row.id
					},
					dataType : "json",
					success : function(data){
						if (data.code == 200) {
							layer.open({
								title: I18n.system_tips,
								btn: [ I18n.system_ok ],
								content: I18n.system_opt_suc ,
								icon: '1',
								end: function(layero, index){
									// refresh table
									$('#data_filter .searchBtn').click();
								}
							});
						} else {
							layer.open({
								title: I18n.system_tips,
								btn: [ I18n.system_ok ],
								content: (data.msg || I18n.system_opt_fail ),
								icon: '2'
							});
						}
					},
				});
			});

		});

		/**
		 * clear Log
		 */
		$('#data_operation').on('click', '.clearLog', function(){

			var jobGroup = $('#jobGroup').val();
			var jobId = $('#jobId').val();

			var jobGroupText = $("#jobGroup").find("option:selected").text();
			var jobIdText = $("#jobId").find("option:selected").text();

			$('#clearLogModal input[name=jobGroup]').val(jobGroup);
			$('#clearLogModal input[name=jobId]').val(jobId);

			$('#clearLogModal .jobGroupText').val(jobGroupText);
			$('#clearLogModal .jobIdText').val(jobIdText);

			$('#clearLogModal').modal('show');

		});
		$("#clearLogModal .ok").on('click', function(){
			$.post(base_url + "/joblog/clearLog",  $("#clearLogModal .form").serialize(), function(data, status) {
				if (data.code == "200") {
					$('#clearLogModal').modal('hide');
					layer.open({
						title: I18n.system_tips ,
						btn: [ I18n.system_ok ],
						content: (I18n.joblog_clean_log + I18n.system_success) ,
						icon: '1',
						end: function(layero, index){
							// refresh table
							$('#data_filter .searchBtn').click();
						}
					});
				} else {
					layer.open({
						title: I18n.system_tips ,
						btn: [ I18n.system_ok ],
						content: (data.msg || (I18n.joblog_clean_log + I18n.system_fail) ),
						icon: '2'
					});
				}
			});
		});
		$("#clearLogModal").on('hide.bs.modal', function () {
			$("#clearLogModal .form")[0].reset();
		});

		// ---------------------- ComAlertTec ----------------------

		/**
		 * logTips alert
		 */
		$('body').on('click', '.logTips', function(){
			var msg = $(this).find('span').html();
			ComAlertTec.show(msg);
		});

		// Com Alert by Tec theme
		var ComAlertTec = {
			html:function(){
				var html =
						'<div class="modal fade" id="ComAlertTec" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">' +
						'	<div class="modal-dialog modal-lg-">' +
						'		<div class="modal-content-tec">' +
						'			<div class="modal-body">' +
						'				<div class="alert" style="color:#fff;word-wrap: break-word;">' +
						'				</div>' +
						'			</div>' +
						'				<div class="modal-footer">' +
						'				<div class="text-center" >' +
						'					<button type="button" class="btn btn-info ok" data-dismiss="modal" >'+ I18n.system_ok +'</button>' +
						'				</div>' +
						'			</div>' +
						'		</div>' +
						'	</div>' +
						'</div>';
				return html;
			},
			show:function(msg, callback){
				// dom init
				if ($('#ComAlertTec').length == 0){
					$('body').append(ComAlertTec.html());
				}

				// init com alert
				$('#ComAlertTec .alert').html(msg);
				$('#ComAlertTec').modal('show');

				$('#ComAlertTec .ok').click(function(){
					$('#ComAlertTec').modal('hide');
					if(typeof callback == 'function') {
						callback();
					}
				});
			}
		};

	});
</script>
<!-- 3-script end -->

</body>
</html>