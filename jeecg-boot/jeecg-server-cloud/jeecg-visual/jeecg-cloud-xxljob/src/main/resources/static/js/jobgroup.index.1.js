$(function() {

	// init date tables
	var jobGroupTable = $("#jobgroup_list").dataTable({
		"deferRender": true,
		"processing" : true,
		"serverSide": true,
		"ajax": {
			url: base_url + "/jobgroup/pageList",
			type:"post",
			data : function ( d ) {
				var obj = {};
				obj.appname = $('#appname').val();
				obj.title = $('#title').val();
				obj.start = d.start;
				obj.length = d.length;
				return obj;
			}
		},
		"searching": false,
		"ordering": false,
		//"scrollX": true,	// scroll x，close self-adaption
		"columns": [
			{
				"data": 'id',
				"visible" : false
			},
			{
				"data": 'appname',
				"visible" : true,
				"width":'30%'
			},
			{
				"data": 'title',
				"visible" : true,
				"width":'30%'
			},
			{
				"data": 'addressType',
				"width":'10%',
				"visible" : true,
				"render": function ( data, type, row ) {
					if (row.addressType == 0) {
						return I18n.jobgroup_field_addressType_0;
					} else {
						return I18n.jobgroup_field_addressType_1;
					}
				}
			},
			{
				"data": 'registryList',
				"width":'15%',
				"visible" : true,
				"render": function ( data, type, row ) {
					return row.registryList
						?'<a class="show_registryList" href="javascript:;" _id="'+ row.id +'" >'
							+ I18n.system_show +' ( ' + row.registryList.length+ ' ）</a>'
						:I18n.system_empty;
				}
			},
			{
				"data": I18n.system_opt ,
				"width":'15%',
				"render": function ( data, type, row ) {
					return function(){
						// data
						tableData['key'+row.id] = row;

						// opt
						var html = '<div class="btn-group">\n' +
							'     <button type="button" class="btn btn-primary btn-sm">'+ I18n.system_opt +'</button>\n' +
							'     <button type="button" class="btn btn-primary btn-sm dropdown-toggle" data-toggle="dropdown">\n' +
							'       <span class="caret"></span>\n' +
							'       <span class="sr-only">Toggle Dropdown</span>\n' +
							'     </button>\n' +
							'     <ul class="dropdown-menu" role="menu" _id="'+ row.id +'" >\n' +
							'       <li><a href="javascript:void(0);" class="opt_edit" >'+ I18n.system_opt_edit +'</a></li>\n' +
							'       <li><a href="javascript:void(0);" class="opt_del" >'+ I18n.system_opt_del +'</a></li>\n' +
							'     </ul>\n' +
							'   </div>';

						return html;
					};
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

	// table data
	var tableData = {};

	// search btn
	$('#searchBtn').on('click', function(){
		jobGroupTable.fnDraw();
	});

	// job registryinfo
	$("#jobgroup_list").on('click', '.show_registryList',function() {
		var id = $(this).attr("_id");
		var row = tableData['key'+id];

		var html = '<div>';
		if (row.registryList) {
			for (var index in row.registryList) {
				html += (parseInt(index)+1) + '. <span class="badge bg-green" >' + row.registryList[index] + '</span><br>';
			}
		}
		html += '</div>';

		layer.open({
			title: I18n.jobinfo_opt_registryinfo ,
			btn: [ I18n.system_ok ],
			content: html
		});

	});


	// opt_del
	$("#jobgroup_list").on('click', '.opt_del',function() {
		var id = $(this).parents('ul').attr("_id");

		layer.confirm( (I18n.system_ok + I18n.jobgroup_del + '？') , {
			icon: 3,
			title: I18n.system_tips ,
			btn: [ I18n.system_ok, I18n.system_cancel ]
		}, function(index){
			layer.close(index);

			$.ajax({
				type : 'POST',
				url : base_url + '/jobgroup/remove',
				data : {"id":id},
				dataType : "json",
				success : function(data){
					if (data.code == 200) {
						layer.open({
							title: I18n.system_tips ,
							btn: [ I18n.system_ok ],
							content: (I18n.jobgroup_del + I18n.system_success),
							icon: '1',
							end: function(layero, index){
								jobGroupTable.fnDraw();
							}
						});
					} else {
						layer.open({
							title: I18n.system_tips,
							btn: [ I18n.system_ok ],
							content: (data.msg || (I18n.jobgroup_del + I18n.system_fail)),
							icon: '2'
						});
					}
				},
			});
		});
	});


	// jquery.validate “low letters start, limit contants、 letters、numbers and line-through.”
	jQuery.validator.addMethod("myValid01", function(value, element) {
		var length = value.length;
		var valid = /^[a-z][a-zA-Z0-9-]*$/;
		return this.optional(element) || valid.test(value);
	}, I18n.jobgroup_field_appname_limit );

	$('.add').on('click', function(){
		$('#addModal').modal({backdrop: false, keyboard: false}).modal('show');
	});
	var addModalValidate = $("#addModal .form").validate({
		errorElement : 'span',
		errorClass : 'help-block',
		focusInvalid : true,
		rules : {
			appname : {
				required : true,
				rangelength:[4,64],
				myValid01 : true
			},
			title : {
				required : true,
				rangelength:[4, 12]
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
		highlight : function(element) {
			$(element).closest('.form-group').addClass('has-error');
		},
		success : function(label) {
			label.closest('.form-group').removeClass('has-error');
			label.remove();
		},
		errorPlacement : function(error, element) {
			element.parent('div').append(error);
		},
		submitHandler : function(form) {
			$.post(base_url + "/jobgroup/save",  $("#addModal .form").serialize(), function(data, status) {
				if (data.code == "200") {
					$('#addModal').modal('hide');
					layer.open({
						title: I18n.system_tips ,
                        btn: [ I18n.system_ok ],
						content: I18n.system_add_suc ,
						icon: '1',
						end: function(layero, index){
							jobGroupTable.fnDraw();
						}
					});
				} else {
					layer.open({
						title: I18n.system_tips,
                        btn: [ I18n.system_ok ],
						content: (data.msg || I18n.system_add_fail  ),
						icon: '2'
					});
				}
			});
		}
	});
	$("#addModal").on('hide.bs.modal', function () {
		$("#addModal .form")[0].reset();
		addModalValidate.resetForm();
		$("#addModal .form .form-group").removeClass("has-error");
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

	// opt_edit
	$("#jobgroup_list").on('click', '.opt_edit',function() {
		var id = $(this).parents('ul').attr("_id");
		var row = tableData['key'+id];

		$("#updateModal .form input[name='id']").val( row.id );
		$("#updateModal .form input[name='appname']").val( row.appname );
		$("#updateModal .form input[name='title']").val( row.title );

		// 注册方式
		$("#updateModal .form input[name='addressType']").removeAttr('checked');
		$("#updateModal .form input[name='addressType'][value='"+ row.addressType +"']").click();
		// 机器地址
		$("#updateModal .form textarea[name='addressList']").val( row.addressList );

		$('#updateModal').modal({backdrop: false, keyboard: false}).modal('show');
	});
	var updateModalValidate = $("#updateModal .form").validate({
		errorElement : 'span',
		errorClass : 'help-block',
		focusInvalid : true,
		rules : {
			appname : {
				required : true,
				rangelength:[4,64],
				myValid01 : true
			},
			title : {
				required : true,
				rangelength:[4, 12]
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
		highlight : function(element) {
			$(element).closest('.form-group').addClass('has-error');
		},
		success : function(label) {
			label.closest('.form-group').removeClass('has-error');
			label.remove();
		},
		errorPlacement : function(error, element) {
			element.parent('div').append(error);
		},
		submitHandler : function(form) {
			$.post(base_url + "/jobgroup/update",  $("#updateModal .form").serialize(), function(data, status) {
				if (data.code == "200") {
					$('#updateModal').modal('hide');

					layer.open({
						title: I18n.system_tips ,
                        btn: [ I18n.system_ok ],
						content: I18n.system_update_suc ,
						icon: '1',
						end: function(layero, index){
							jobGroupTable.fnDraw();
						}
					});
				} else {
					layer.open({
						title: I18n.system_tips,
                        btn: [ I18n.system_ok ],
						content: (data.msg || I18n.system_update_fail  ),
						icon: '2'
					});
				}
			});
		}
	});
	$("#updateModal").on('hide.bs.modal', function () {
		$("#updateModal .form")[0].reset();
		addModalValidate.resetForm();
		$("#updateModal .form .form-group").removeClass("has-error");
	});

	
});
