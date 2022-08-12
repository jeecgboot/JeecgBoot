$(function() {

	// jobGroup change, job list init and select
	$("#jobGroup").on("change", function () {
		var jobGroup = $(this).children('option:selected').val();
		$.ajax({
			type : 'POST',
            async: false,   // async, avoid js invoke pagelist before jobId data init
			url : base_url + '/joblog/getJobsByGroup',
			data : {"jobGroup":jobGroup},
			dataType : "json",
			success : function(data){
				if (data.code == 200) {
					$("#jobId").html( '<option value="0" >'+ I18n.system_all +'</option>' );
					$.each(data.content, function (n, value) {
                        $("#jobId").append('<option value="' + value.id + '" >' + value.jobDesc + '</option>');
                    });
                    if ($("#jobId").attr("paramVal")){
                        $("#jobId").find("option[value='" + $("#jobId").attr("paramVal") + "']").attr("selected",true);
                    }
				} else {
					layer.open({
						title: I18n.system_tips ,
                        btn: [ I18n.system_ok ],
						content: (data.msg || I18n.system_api_error ),
						icon: '2'
					});
				}
			},
		});
	});
	if ($("#jobGroup").attr("paramVal")){
		$("#jobGroup").find("option[value='" + $("#jobGroup").attr("paramVal") + "']").attr("selected",true);
        $("#jobGroup").change();
	}

	// filter Time
    var rangesConf = {};
    rangesConf[I18n.daterangepicker_ranges_recent_hour] = [moment().subtract(1, 'hours'), moment()];
    rangesConf[I18n.daterangepicker_ranges_today] = [moment().startOf('day'), moment().endOf('day')];
    rangesConf[I18n.daterangepicker_ranges_yesterday] = [moment().subtract(1, 'days').startOf('day'), moment().subtract(1, 'days').endOf('day')];
    rangesConf[I18n.daterangepicker_ranges_this_month] = [moment().startOf('month'), moment().endOf('month')];
    rangesConf[I18n.daterangepicker_ranges_last_month] = [moment().subtract(1, 'months').startOf('month'), moment().subtract(1, 'months').endOf('month')];
    rangesConf[I18n.daterangepicker_ranges_recent_week] = [moment().subtract(1, 'weeks').startOf('day'), moment().endOf('day')];
    rangesConf[I18n.daterangepicker_ranges_recent_month] = [moment().subtract(1, 'months').startOf('day'), moment().endOf('day')];

	$('#filterTime').daterangepicker({
        autoApply:false,
        singleDatePicker:false,
        showDropdowns:false,        // 是否显示年月选择条件
		timePicker: true, 			// 是否显示小时和分钟选择条件
		timePickerIncrement: 10, 	// 时间的增量，单位为分钟
        timePicker24Hour : true,
        opens : 'left', //日期选择框的弹出位置
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
        },
        startDate: rangesConf[I18n.daterangepicker_ranges_today][0],
        endDate: rangesConf[I18n.daterangepicker_ranges_today][1]
	});

	// init date tables
	var logTable = $("#joblog_list").dataTable({
		"deferRender": true,
		"processing" : true, 
	    "serverSide": true,
		"ajax": {
	        url: base_url + "/joblog/pageList" ,
            type:"post",
	        data : function ( d ) {
	        	var obj = {};
	        	obj.jobGroup = $('#jobGroup').val();
	        	obj.jobId = $('#jobId').val();
                obj.logStatus = $('#logStatus').val();
				obj.filterTime = $('#filterTime').val();
	        	obj.start = d.start;
	        	obj.length = d.length;
                return obj;
            }
	    },
	    "searching": false,
	    "ordering": false,
	    //"scrollX": false,
	    "columns": [
					{
						"data": 'jobId',
						"visible" : true,
                        "width":'10%',
						"render": function ( data, type, row ) {

							var jobhandler = '';
                            if (row.executorHandler) {
                                jobhandler = "<br>JobHandler：" + row.executorHandler;
                            }

							var temp = '';
							temp += I18n.joblog_field_executorAddress + '：' + (row.executorAddress?row.executorAddress:'');
							temp += jobhandler;
							temp += '<br>'+ I18n.jobinfo_field_executorparam +'：' + row.executorParam;

							return '<a class="logTips" href="javascript:;" >'+ row.jobId +'<span style="display:none;">'+ temp +'</span></a>';
						}
					},
					{ "data": 'jobGroup', "visible" : false},
					{
						"data": 'triggerTime',
                        "width":'20%',
						"render": function ( data, type, row ) {
							return data?moment(new Date(data)).format("YYYY-MM-DD HH:mm:ss"):"";
						}
					},
					{
						"data": 'triggerCode',
                        "width":'10%',
						"render": function ( data, type, row ) {
							var html = data;
							if (data == 200) {
								html = '<span style="color: green">'+ I18n.system_success +'</span>';
							} else if (data == 500) {
								html = '<span style="color: red">'+ I18n.system_fail +'</span>';
							} else if (data == 0) {
                                html = '';
							}
                            return html;
						}
					},
					{
						"data": 'triggerMsg',
                        "width":'10%',
						"render": function ( data, type, row ) {
							return data?'<a class="logTips" href="javascript:;" >'+ I18n.system_show +'<span style="display:none;">'+ data +'</span></a>':I18n.system_empty;
						}
					},
	                { 
	                	"data": 'handleTime',
                        "width":'20%',
	                	"render": function ( data, type, row ) {
	                		return data?moment(new Date(data)).format("YYYY-MM-DD HH:mm:ss"):"";
	                	}
	                },
	                {
						"data": 'handleCode',
                        "width":'10%',
						"render": function ( data, type, row ) {
                            var html = data;
                            if (data == 200) {
                                html = '<span style="color: green">'+ I18n.joblog_handleCode_200 +'</span>';
                            } else if (data == 500) {
                                html = '<span style="color: red">'+ I18n.joblog_handleCode_500 +'</span>';
                            } else if (data == 502) {
                                html = '<span style="color: red">'+ I18n.joblog_handleCode_502 +'</span>';
                            } else if (data == 0) {
                                html = '';
                            }
                            return html;
						}
	                },
	                { 
	                	"data": 'handleMsg',
                        "width":'10%',
	                	"render": function ( data, type, row ) {
	                		return data?'<a class="logTips" href="javascript:;" >'+ I18n.system_show +'<span style="display:none;">'+ data +'</span></a>':I18n.system_empty;
	                	}
	                },
	                {
						"data": 'handleMsg' ,
						"bSortable": false,
                        "width":'10%',
	                	"render": function ( data, type, row ) {
	                		// better support expression or string, not function
	                		return function () {
		                		if (row.triggerCode == 200 || row.handleCode != 0){

		                			/*var temp = '<a href="javascript:;" class="logDetail" _id="'+ row.id +'">'+ I18n.joblog_rolling_log +'</a>';
		                			if(row.handleCode == 0){
		                				temp += '<br><a href="javascript:;" class="logKill" _id="'+ row.id +'" style="color: red;" >'+ I18n.joblog_kill_log +'</a>';
		                			}*/
		                			//return temp;

									var html = '<div class="btn-group">\n' +
										'     <button type="button" class="btn btn-primary btn-sm">'+ I18n.system_opt +'</button>\n' +
										'     <button type="button" class="btn btn-primary btn-sm dropdown-toggle" data-toggle="dropdown">\n' +
										'       <span class="caret"></span>\n' +
										'       <span class="sr-only">Toggle Dropdown</span>\n' +
										'     </button>\n' +
										'     <ul class="dropdown-menu" role="menu" _id="'+ row.id +'" >\n' +
										'       <li><a href="javascript:void(0);" class="logDetail" _id="'+ row.id +'" >'+ I18n.joblog_rolling_log +'</a></li>\n' +
										'       <li class="divider"></li>\n' +
										'       <li><a href="javascript:void(0);" class="logKill" _id="'+ row.id +'" >'+ I18n.joblog_kill_log +'</a></li>\n' +
										'     </ul>\n' +
										'   </div>';

		                			return html;
		                		}
		                		return null;	
	                		}
	                	}
	                }
	            ],
        "language" : {
            "sProcessing" : I18n.dataTable_sProcessing ,
            "sLengthMenu" : I18n.dataTable_sLengthMenu ,
            "sZeroRecords" : I18n.dataTable_sZeroRecords ,
            "sInfo" : I18n.dataTable_sInfo ,
            "sInfoEmpty" : I18n.dataTable_sInfoEmpty ,
            "sInfoFiltered" : I18n.dataTable_sInfoFiltered ,
            "sInfoPostFix" : "",
            "sSearch" : I18n.dataTable_sSearch ,
            "sUrl" : "",
            "sEmptyTable" : I18n.dataTable_sEmptyTable ,
            "sLoadingRecords" : I18n.dataTable_sLoadingRecords ,
            "sInfoThousands" : ",",
            "oPaginate" : {
                "sFirst" : I18n.dataTable_sFirst ,
                "sPrevious" : I18n.dataTable_sPrevious ,
                "sNext" : I18n.dataTable_sNext ,
                "sLast" : I18n.dataTable_sLast
            },
            "oAria" : {
                "sSortAscending" : I18n.dataTable_sSortAscending ,
                "sSortDescending" : I18n.dataTable_sSortDescending
            }
        }
	});
    logTable.on('xhr.dt',function(e, settings, json, xhr) {
        if (json.code && json.code != 200) {
            layer.msg( json.msg || I18n.system_api_error );
        }
    });
	
	// logTips alert
	$('#joblog_list').on('click', '.logTips', function(){
		var msg = $(this).find('span').html();
		ComAlertTec.show(msg);
	});
	
	// search Btn
	$('#searchBtn').on('click', function(){
		logTable.fnDraw();
	});
	
	// logDetail look
	$('#joblog_list').on('click', '.logDetail', function(){
		var _id = $(this).attr('_id');
		
		window.open(base_url + '/joblog/logDetailPage?id=' + _id);
		return;
	});

	/**
	 * log Kill
	 */
	$('#joblog_list').on('click', '.logKill', function(){
		var _id = $(this).attr('_id');

        layer.confirm( (I18n.system_ok + I18n.joblog_kill_log + '?'), {
        	icon: 3,
			title: I18n.system_tips ,
            btn: [ I18n.system_ok, I18n.system_cancel ]
		}, function(index){
            layer.close(index);

            $.ajax({
                type : 'POST',
                url : base_url + '/joblog/logKill',
                data : {"id":_id},
                dataType : "json",
                success : function(data){
                    if (data.code == 200) {
                        layer.open({
                            title: I18n.system_tips,
                            btn: [ I18n.system_ok ],
                            content: I18n.system_opt_suc ,
                            icon: '1',
                            end: function(layero, index){
                                logTable.fnDraw();
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
	$('#clearLog').on('click', function(){

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
						logTable.fnDraw();
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
