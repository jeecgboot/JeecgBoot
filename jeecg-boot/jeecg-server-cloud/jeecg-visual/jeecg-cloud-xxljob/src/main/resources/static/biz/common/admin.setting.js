/*!
* Admin Setting for XXL-BOOT
* ================
*
* 1、logout：         退出登录
* 2、updatePwd：      修改密码
* 3、slideToTop：     回到顶部
* 4、change skin：    切换皮肤
* 5、menu/sidebar-toggle： 菜单/侧边来，展示切换
*
* @author       xuxueli
* @repository   https://github.com/xuxueli/xxl-boot
*/
$(function(){

    // ---------------------- logout ----------------------

    /**
     * logout
     */
    $("#logoutBtn").click(function(){
        layer.confirm( I18n.logout_confirm , {
            icon: 3,
            title: I18n.system_tips ,
            btn: [ I18n.system_ok, I18n.system_cancel ]
        }, function(index){
            layer.close(index);

            $.post(base_url + "/auth/logout", function(data, status) {
                if (data.code == "200") {
                    layer.msg( I18n.logout_success );
                    setTimeout(function(){
                        window.location.href = base_url + "/";
                    }, 500);
                } else {
                    layer.open({
                        title: I18n.system_tips ,
                        btn: [ I18n.system_ok ],
                        content: (data.msg || I18n.logout_fail),
                        icon: '2'
                    });
                }
            });
        });

    });

    // ---------------------- update pwd ----------------------

    /**
     * updatePwd Modal
     */
    let updatePwd = $(`
        <!-- 修改密码.模态框 -->
        <div class="modal fade" id="updatePwdModal" tabindex="-1" role="dialog"  aria-hidden="true">
            <div class="modal-dialog ">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                        <h4 class="modal-title" >` + I18n.change_pwd +`</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal form" role="form" >
                            <div class="form-group">
                                <label for="lastname" class="col-sm-2 control-label">` + I18n.change_pwd_field_oldpwd +`<font color="red">*</font></label>
                                <div class="col-sm-10"><input type="text" class="form-control" name="oldPassword" placeholder="` + I18n.system_please_input + I18n.change_pwd_field_oldpwd + `" maxlength="20" ></div>
                            </div>
                            <div class="form-group">
                                <label for="lastname" class="col-sm-2 control-label">` + I18n.change_pwd_field_newpwd +` <font color="red">*</font></label>
                                <div class="col-sm-10"><input type="text" class="form-control" name="password" placeholder="` + I18n.system_please_input + I18n.change_pwd_field_newpwd + `" maxlength="20" ></div>
                            </div>
                            <hr>
                            <div class="form-group">
                                <div class="col-sm-offset-3 col-sm-6">
                                    <button type="submit" class="btn btn-primary"  >` + I18n.system_save +`</button>
                                    <button type="button" class="btn btn-default" data-dismiss="modal">` + I18n.system_cancel +`</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    `);
    $('.wrapper').append(updatePwd);

    /**
     * updatePwd
     */
    $('#updatePwd').on('click', function(){
        $('#updatePwdModal').modal({backdrop: false, keyboard: false}).modal('show');
    });
    var updatePwdModalValidate = $("#updatePwdModal .form").validate({
        errorElement : 'span',
        errorClass : 'help-block',
        focusInvalid : true,
        rules : {
            oldPassword : {
                required : true ,
                rangelength:[4,50]
            },
            password : {
                required : true ,
                rangelength:[4,50]
            }
        },
        messages : {
            oldPassword : {
                required : I18n.system_please_input +I18n.change_pwd_field_oldpwd,
                rangelength : "密码长度限制为4~50"
            },
            password : {
                required : I18n.system_please_input +I18n.change_pwd_field_newpwd,
                rangelength : "密码长度限制为4~50"
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
            $.post(base_url + "/auth/updatePwd",  $("#updatePwdModal .form").serialize(), function(data, status) {
                if (data.code == 200) {
                    $('#updatePwdModal').modal('hide');

                    layer.msg( I18n.change_pwd_suc_to_logout );
                    setTimeout(function(){
                        $.post(base_url + "/auth/logout", function(data, status) {
                            if (data.code == 200) {
                                window.location.href = base_url + "/";
                            } else {
                                layer.open({
                                    icon: '2',
                                    content: (data.msg|| I18n.logout_fail)
                                });
                            }
                        });
                    }, 500);
                } else {
                    layer.open({
                        icon: '2',
                        content: (data.msg|| I18n.change_pwd + I18n.system_fail )
                    });
                }
            });
        }
    });
    $("#updatePwdModal").on('hide.bs.modal', function () {
        $("#updatePwdModal .form")[0].reset();
        updatePwdModalValidate.resetForm();
        $("#updatePwdModal .form .form-group").removeClass("has-error");
    });

    // ---------------------- slideToTop ----------------------

    /**
     * slideToTop Html
     */
    var slideToTop = $("<div />");
    slideToTop.html('<i class="fa fa-chevron-up"></i>');
    slideToTop.css({
        position: 'fixed',
        bottom: '20px',
        right: '25px',
        width: '40px',
        height: '40px',
        color: '#eee',
        'font-size': '',
        'line-height': '40px',
        'text-align': 'center',
        'background-color': '#222d32',
        cursor: 'pointer',
        'border-radius': '5px',
        'z-index': '99999',
        opacity: '.7',
        'display': 'none'
    });
    slideToTop.on('mouseenter', function () {
        $(this).css('opacity', '1');
    });
    slideToTop.on('mouseout', function () {
        $(this).css('opacity', '.7');
    });
    $('.wrapper').append(slideToTop);
    $(window).scroll(function () {
        if ($(window).scrollTop() >= 150) {
            if (!$(slideToTop).is(':visible')) {
                $(slideToTop).fadeIn(500);
            }
        } else {
            $(slideToTop).fadeOut(500);
        }
    });

    /**
     * slideToTop click
     */
    $(slideToTop).click(function () {
        $("html,body").animate({		// firefox ie not support body, chrome support body. but found that new version chrome not support body too.
            scrollTop: 0
        }, 100);
    });

    // ---------------------- change skin ----------------------

    /**
     * skinModal
     */
    let skinModal = $(`
        <!-- 主题切换.模态框 -->
        <div class="modal fade" id="skinModal" tabindex="-1" role="dialog"  aria-hidden="true">
            <div class="modal-dialog ">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                        <h4 class="modal-title" >` + I18n.change_skin +`</h4>
                    </div>
                    <div class="modal-body">
                        <!-- 主题列表 -->
                        <ul class="list-unstyled clearfix" >
                            <!-- level 1 -->
                            <li style="float:left; width: 33.33333%; padding: 5px;">
                                <a href="javascript:" data-skin='skin-blue' style="display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)" class="clearfix full-opacity-hover">
                                    <span style="width: 20%; float: left; height: 13px; background: #367fa9"></span>
                                    <span style="width: 80%; float: left; height: 13px;" class='bg-light-blue'></span>
                                    <span style="width: 20%; float: left; height: 30px; background: #222d32"></span>
                                    <span style="width: 80%; float: left; height: 30px; background: #f4f5f7"></span>
                                </a>
                                <p class="text-center">Blue</p>
                            </li>
                            <li style="float:left; width: 33.33333%; padding: 5px;">
                                <a href="javascript:" data-skin="skin-black" style="display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)" class="clearfix full-opacity-hover">
                                    <span style="width: 20%; float: left; height: 13px; background: #fefefe"></span>
                                    <span style="width: 80%; float: left; height: 13px; background: #fefefe"></span>
                                    <span style="width: 20%; float: left; height: 30px; background: #222d32"></span>
                                    <span style="width: 80%; float: left; height: 30px; background: #f4f5f7"></span>
                                </a>
                                <p class="text-center">Black</p>
                            </li>
                            <li style="float:left; width: 33.33333%; padding: 5px;">
                                <a href="javascript:" data-skin="skin-purple" style="display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)" class="clearfix full-opacity-hover">
                                    <span style="width: 20%; float: left; height: 13px;" class='bg-purple-active'></span>
                                    <span style="width: 80%; float: left; height: 13px;" class='bg-purple'></span>
                                    <span style="width: 20%; float: left; height: 30px; background: #222d32"></span>
                                    <span style="width: 80%; float: left; height: 30px; background: #f4f5f7"></span>
                                </a>
                                <p class="text-center">Purple</p>
                            </li>
                            
                            <!-- level 2 -->
                            <li style="float:left; width: 33.33333%; padding: 5px;">
                                <a href="javascript:" data-skin="skin-green" style="display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)" class="clearfix full-opacity-hover">
                                    <span style="width: 20%; float: left; height: 13px;" class='bg-green-active'></span>
                                    <span style="width: 80%; float: left; height: 13px;" class='bg-green'></span>
                                    <span style="width: 20%; float: left; height: 30px; background: #222d32"></span>
                                    <span style="width: 80%; float: left; height: 30px; background: #f4f5f7"></span>
                                </a>
                                <p class="text-center">Green</p>
                            </li>
                            <li style="float:left; width: 33.33333%; padding: 5px;">
                                <a href="javascript:" data-skin="skin-red" style="display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)" class="clearfix full-opacity-hover">
                                    <span style="width: 20%; float: left; height: 13px;" class='bg-red-active'></span>
                                    <span style="width: 80%; float: left; height: 13px;" class='bg-red'></span>
                                    <span style="width: 20%; float: left; height: 30px; background: #222d32"></span>
                                    <span style="width: 80%; float: left; height: 30px; background: #f4f5f7"></span>
                                </a>
                                <p class="text-center">Red</p>
                            </li>
                            <li style="float:left; width: 33.33333%; padding: 5px;">
                                <a href="javascript:" data-skin="skin-yellow" style="display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)" class="clearfix full-opacity-hover">
                                    <span style="width: 20%; float: left; height: 13px;" class='bg-yellow-active'></span>
                                    <span style="width: 80%; float: left; height: 13px;" class='bg-yellow'></span>
                                    <span style="width: 20%; float: left; height: 30px; background: #222d32"></span>
                                    <span style="width: 80%; float: left; height: 30px; background: #f4f5f7"></span>
                                </a>
                                <p class="text-center">Yellow</p>
                            </li>
                            
                            <!-- level 3 -->
                            <li style="float:left; width: 33.33333%; padding: 5px;">
                                <a href="javascript:" data-skin="skin-blue-light" style="display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)" class="clearfix full-opacity-hover">
                                    <span style="width: 20%; float: left; height: 13px; background: #367fa9"></span>
                                    <span style="width: 80%; float: left; height: 13px;" class='bg-light-blue'></span>
                                    <span style="width: 20%; float: left; height: 30px; background: #f9fafc"></span>
                                    <span style="width: 80%; float: left; height: 30px; background: #f4f5f7"></span>
                                </a>
                                <p class="text-center">Blue(Light)</p>
                            </li>
                            <li style="float:left; width: 33.33333%; padding: 5px;">
                                <a href="javascript:" data-skin="skin-black-light" style="display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)" class="clearfix full-opacity-hover">
                                    <span style="width: 20%; float: left; height: 13px; background: #fefefe"></span>
                                    <span style="width: 80%; float: left; height: 13px; background: #fefefe"></span>
                                    <span style="width: 20%; float: left; height: 30px; background: #f9fafc"></span>
                                    <span style="width: 80%; float: left; height: 30px; background: #f4f5f7"></span>
                                </a>
                                <p class="text-center">Black(Light)</p>
                            </li>
                            <li style="float:left; width: 33.33333%; padding: 5px;">
                                <a href="javascript:" data-skin="skin-purple-light" style="display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)" class="clearfix full-opacity-hover">
                                    <span style="width: 20%; float: left; height: 13px;" class='bg-purple-active'></span>
                                    <span style="width: 80%; float: left; height: 13px;" class='bg-purple'></span>
                                    <span style="width: 20%; float: left; height: 30px; background: #f9fafc"></span>
                                    <span style="width: 80%; float: left; height: 30px; background: #f4f5f7"></span>
                                </a>
                                <p class="text-center">Purple(Light)</p>
                            </li>
                            
                            <!-- level 4 -->
                            <li style="float:left; width: 33.33333%; padding: 5px;">
                                <a href="javascript:" data-skin="skin-green-light" style="display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)" class="clearfix full-opacity-hover">
                                    <span style="width: 20%; float: left; height: 13px;" class='bg-green-active'></span>
                                    <span style="width: 80%; float: left; height: 13px;" class='bg-green'></span>
                                    <span style="width: 20%; float: left; height: 30px; background: #f9fafc"></span>
                                    <span style="width: 80%; float: left; height: 30px; background: #f4f5f7"></span>
                                </a>
                                <p class="text-center">Green(Light)</p>
                            </li>
                            <li style="float:left; width: 33.33333%; padding: 5px;">
                                <a href="javascript:" data-skin="skin-red-light" style="display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)" class="clearfix full-opacity-hover">
                                    <span style="width: 20%; float: left; height: 13px;" class='bg-red-active'></span>
                                    <span style="width: 80%; float: left; height: 13px;" class='bg-red'></span>
                                    <span style="width: 20%; float: left; height: 30px; background: #f9fafc"></span>
                                    <span style="width: 80%; float: left; height: 30px; background: #f4f5f7"></span>
                                </a>
                                <p class="text-center">Red(Light)</p>
                            </li>
                            <li style="float:left; width: 33.33333%; padding: 5px;">
                                <a href="javascript:" data-skin="skin-yellow-light" style="display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)" class="clearfix full-opacity-hover">
                                    <span style="width: 20%; float: left; height: 13px;" class='bg-yellow-active'></span>
                                    <span style="width: 80%; float: left; height: 13px;" class='bg-yellow'></span>
                                    <span style="width: 20%; float: left; height: 30px; background: #f9fafc"></span>
                                    <span style="width: 80%; float: left; height: 30px; background: #f4f5f7"></span>
                                </a>
                                <p class="text-center">Yellow(Light)</p>
                            </li>
                            
                            <!-- style -->
                            <style>
                                .list-unstyled{margin:10px;}
                                .full-opacity-hover{opacity:1;filter:alpha(opacity=1);border:1px solid #fff}
                                .full-opacity-hover:hover{border:1px solid silver;}
                            </style>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    `);
    $('.wrapper').append(skinModal);

    /**
     * skin opt
     */
    $('#changeSkin').on('click', function(){
        $('#skinModal').modal({backdrop: true, keyboard: false}).modal('show');
    });

    /**
     * skin list
     */
    var skins = [
        "skin-blue",
        "skin-black",
        "skin-purple",
        "skin-green",
        "skin-red",
        "skin-yellow",
        "skin-blue-light",
        "skin-black-light",
        "skin-purple-light",
        "skin-green-light",
        "skin-red-light",
        "skin-yellow-light"
    ];

    /**
     * skin change
     */
    $("[data-skin]").on('click', function(e) {
        var skin = $(this).data('skin');
        $.each(skins, function (i) {
            $("body").removeClass(skins[i]);
        });
        $("body").addClass(skin);
        store('admin_skin', skin);
        return false;
    });

    /**
     * skin init
     */
    let skin = loadStore('admin_skin');
    if (skin) {
        $.each(skins, function (i) {
            $("body").removeClass(skins[i]);
        });
        $("body").addClass(skin);
    }

    // ---------------------- localStorage ----------------------

    /**
     * store
     */
    function store(name, val) {
        if (typeof (Storage) !== "undefined") {
            localStorage.setItem(name, val);
        } else {
            window.alert('Please use a modern browser to properly view this template!');
        }
    }

    /**
     * get
     */
    function loadStore(name) {
        if (typeof (Storage) !== "undefined") {
            return localStorage.getItem(name);
        } else {
            window.alert('Please use a modern browser to properly view this template!');
        }
    }

    // ---------------------- menu, sidebar-toggle ----------------------

    // init menu speed
    $('.sidebar-menu').attr('data-animation-speed', 1);

    // init menu status
    let sidebar = loadStore('admin_sidebar_status');
    if ( 'close' === sidebar ) {
        $('body').addClass('sidebar-collapse');
    } else {
        $('body').removeClass('sidebar-collapse');
    }

    // change menu status
    $('.sidebar-toggle').click(function(){
        if ( 'close' === loadStore('admin_sidebar_status') ) {
            store('admin_sidebar_status', 'open')
        } else {
            store('admin_sidebar_status', 'close')
        }
    });

});
