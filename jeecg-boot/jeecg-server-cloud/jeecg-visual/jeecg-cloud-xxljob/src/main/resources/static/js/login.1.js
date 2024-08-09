$(function(){

	// input iCheck
    $('input').iCheck({
      checkboxClass: 'icheckbox_square-blue',
      radioClass: 'iradio_square-blue',
      increaseArea: '20%' // optional
    });
    
	// login Form Valid
	var loginFormValid = $("#loginForm").validate({
		errorElement : 'span',  
        errorClass : 'help-block',
        focusInvalid : true,  
        rules : {  
        	userName : {  
        		required : true ,
                minlength: 4,
                maxlength: 18
            },  
            password : {  
            	required : true ,
                minlength: 4,
                maxlength: 18
            } 
        }, 
        messages : {  
        	userName : {  
                required  : I18n.login_username_empty,
                minlength : I18n.login_username_lt_4
            },
            password : {
            	required  : I18n.login_password_empty  ,
                minlength : I18n.login_password_lt_4
                /*,maxlength:"登录密码不应超过18位"*/
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
			$.post(base_url + "/login", $("#loginForm").serialize(), function(data, status) {
				if (data.code == "200") {
                    layer.msg( I18n.login_success );
                    setTimeout(function(){
                        window.location.href = base_url + "/";
                    }, 500);
				} else {
                    layer.open({
                        title: I18n.system_tips,
                        btn: [ I18n.system_ok ],
                        content: (data.msg || I18n.login_fail ),
                        icon: '2'
                    });
				}
			});
		}
	});
});