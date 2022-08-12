$(function() {

	// init code editor
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
	
	// before upload
	/*$(window).bind('beforeunload',function(){
		return 'Glue尚未保存，确定离开Glue编辑器？';
	});*/
	
});
