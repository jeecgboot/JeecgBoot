/**
* jQuery EasyUI 1.3.6
* Copyright (c) 2009-2014 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL or commercial licenses
* To use it on other terms please contact author: info@jeasyui.com
* http://www.gnu.org/licenses/gpl.txt
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI validatebox Extensions 1.0 beta
* jQuery EasyUI validatebox 组件扩展
* jeasyui.extensions.validatebox.js
*
* 依赖项：
*   1、jquery.jdirk.js v1.0 beta late
*   2、jeasyui.extensions.js v1.0 beta late
*
* Copyright (c) 2013-2014 ChenJianwei personal All rights reserved.
* http://www.chenjianwei.org
*/
(function ($, undefined) {


    $.fn.validatebox.extensions = {};


    var rules = {
        //  只允许输入英文字母或数字
        engNum: {
            validator: function (value) {
                return /^[0-9a-zA-Z]*$/.test(value);
            },
            message: '请输入英文字母或数字'
        },
        //  只允许汉字、英文字母或数字
        chsEngNum: {
            validator: function (value, param) {
                return /^([\u4E00-\uFA29]|[\uE7C7-\uE7F3]|[a-zA-Z0-9])*$/.test(value);
            },
            message: '只允许汉字、英文字母或数字。'
        },
        //  只允许汉字、英文字母、数字及下划线
        code: {
            validator: function (value, param) {
                return /^[\u0391-\uFFE5\w]+$/.test(value);
            },
            message: '只允许汉字、英文字母、数字及下划线.'
        },
        //  验证是否为合法的用户名
        name: {
            validator: function (value) { return value.isUserName(); },
            message: "用户名不合法(字母开头，允许6-16字节，允许字母数字下划线)"
        },
        //  指定字符最小长度
        minLength: {
            validator: function (value, param) { return $.string.trim(value).length >= param[0]; },
            message: "最少输入 {0} 个字符."
        },
        //  指定字符最大长度
        maxLength: {
            validator: function (value, param) { return $.string.trim(value).length <= param[0]; },
            message: "最多输入 {0} 个字符."
        },
        //  必须包含指定的内容
        contains: {
            validator: function (value, param) { return $.string.contains(value, param[0]); },
            message: "输入的内容必须包含 {0}."
        },
        //  以指定的字符开头
        startsWith: {
            validator: function (value, param) { return $.string.startsWith(value, param[0]); },
            message: "输入的内容必须以 {0} 作为起始字符."
        },
        //  以指定的字符结束
        endsWith: {
            validator: function (value, param) { return $.string.endsWith(value, param[0]); },
            message: "输入的内容必须以 {0} 作为起始字符."
        },
        //  长日期时间(yyyy-MM-dd hh:mm:ss)格式
        longDate: {
            validator: function (value) { return $.string.isLongDate(value); },
            message: "输入的内容必须是长日期时间(yyyy-MM-dd hh:mm:ss)格式."
        },
        //  短日期(yyyy-MM-dd)格式
        shortDate: {
            validator: function (value) { return $.string.isShortDate(value); },
            message: "输入的内容必须是短日期(yyyy-MM-dd)格式."
        },
        //  长日期时间(yyyy-MM-dd hh:mm:ss)或短日期(yyyy-MM-dd)格式
        date: {
            validator: function (value) { return $.string.isDate(value); },
            message: "输入的内容必须是长日期时间(yyyy-MM-dd hh:mm:ss)或短日期(yyyy-MM-dd)格式."
        },
        //  电话号码(中国)格式
        tel: {
            validator: function (value) { return $.string.isTel(value); },
            message: "输入的内容必须是电话号码(中国)格式."
        },
        //  移动电话号码(中国)格式
        mobile: {
            validator: function (value) { return $.string.isMobile(value); },
            message: "输入的内容必须是移动电话号码(中国)格式."
        },
        //  电话号码(中国)或移动电话号码(中国)格式
        telOrMobile: {
            validator: function (value) { return $.string.isTelOrMobile(value); },
            message: "输入的内容必须是电话号码(中国)或移动电话号码(中国)格式."
        },
        //  传真号码(中国)格式
        fax: {
            validator: function (value) { return $.string.isFax(value); },
            message: "输入的内容必须是传真号码(中国)格式."
        },
        //  邮政编码(中国)格式
        zipCode: {
            validator: function (value) { return $.string.isZipCode(value); },
            message: "输入的内容必须是邮政编码(中国)格式."
        },
        //  必须包含中文汉字
        existChinese: {
            validator: function (value) { return $.string.existChinese(value); },
            message: "输入的内容必须是包含中文汉字."
        },
        //  必须是纯中文汉字
        chinese: {
            validator: function (value) { return $.string.isChinese(value); },
            message: "输入的内容必须是纯中文汉字."
        },
        //  必须是纯英文字母
        english: {
            validator: function (value) { return $.string.isEnglish(value); },
            message: "输入的内容必须是纯英文字母."
        },
        //  必须是合法的文件名(不能包含字符 \\/:*?\"<>|)
        fileName: {
            validator: function (value) { return $.string.isFileName(value); },
            message: "输入的内容必须是合法的文件名(不能包含字符 \\/:*?\"<>|)."
        },
        //  必须是正确的 IP地址v4 格式
        ip: {
            validator: function (value) { return $.string.isIPv4(value); },
            message: "输入的内容必须是正确的 IP地址v4 格式."
        },
        //  必须是正确的 url 格式
        url: {
            validator: function (value) { return $.string.isUrl(value); },
            message: "输入的内容必须是正确的 url 格式."
        },
        //  必须是正确的 IP地址v4 或 url 格式
        ipurl: {
            validator: function (value) { return $.string.isUrlOrIPv4(value); },
            message: "输入的内容必须是正确的 IP地址v4 或 url 格式."
        },
        //  必须是正确的货币金额(阿拉伯数字表示法)格式
        currency: {
            validator: function (value) { return $.string.isCurrency(value); },
            message: "输入的内容必须是正确的货币金额(阿拉伯数字表示法)格式."
        },
        //  必须是正确 QQ 号码格式
        qq: {
            validator: function (value) { return $.string.isQQ(value); },
            message: "输入的内容必须是正确 QQ 号码格式."
        },
        //  必须是正确 MSN 账户名格式
        msn: {
            validator: function (value) { return $.string.isMSN(value); },
            message: "输入的内容必须是正确 MSN 账户名格式."
        },
        unNormal: {
            validator: function (value) { return $.string.isUnNormal(value); },
            message: "输入的内容必须是不包含空格和非法字符Z."
        },
        //  必须是合法的汽车车牌号码格式
        carNo: {
            validator: function (value) { return $.string.isCarNo(value); },
            message: "输入的内容必须是合法的汽车车牌号码格式."
        },
        //  必须是合法的汽车发动机序列号格式
        carEngineNo: {
            validator: function (value) { return $.string.isCarEngineNo(value); },
            message: "输入的内容必须是合法的汽车发动机序列号格式."
        },
        //  必须是合法的身份证号码(中国)格式
        idCard: {
            validator: function (value) { return $.string.isIDCard(value); },
            message: "输入的内容必须是合法的身份证号码(中国)格式."
        },
        //  必须是合法的整数格式
        integer: {
            validator: function (value) { return $.string.isInteger(value); },
            message: "输入的内容必须是合法的整数格式."
        },
        //  必须是合法的整数格式且值介于 {0} 与 {1} 之间
        integerRange: {
            validator: function (value, param) {
                return $.string.isInteger(value) && ((param[0] || value >= param[0]) && (param[1] || value <= param[1]));
            },
            message: "输入的内容必须是合法的整数格式且值介于 {0} 与 {1} 之间."
        },
        //  必须是指定类型的数字格式
        numeric: {
            validator: function (value, param) { return $.string.isNumeric(value, param ? param[0] : undefined); },
            message: "输入的内容必须是指定类型的数字格式."
        },
        //  必须是指定类型的数字格式且介于 {0} 与 {1} 之间
        numericRange: {
            validator: function (value, param) {
                return $.string.isNumeric(value, param ? param[2] : undefined) && ((param[0] || value >= param[0]) && (param[1] || value <= param[1]));
            },
            message: "输入的内容必须是指定类型的数字格式且介于 {0} 与 {1} 之间."
        },
        //  必须是正确的 颜色(#FFFFFF形式) 格式
        color: {
            validator: function (value) { return $.string.isColor(value); },
            message: "输入的内容必须是正确的 颜色(#FFFFFF形式) 格式."
        },
        //  必须是安全的密码字符(由字符和数字组成，至少 6 位)格式
        password: {
            validator: function (value) { return $.string.isSafePassword(value); },
            message: "输入的内容必须是安全的密码字符(由字符和数字组成，至少 6 位)格式."
        },
        //  输入的字符必须是指定的内容相同
        equals: {
            validator: function (value, param) {
                var val = param[0], type = param[1];
                if (type) {
                    switch (String(type).toLowerCase()) {
                        case "jquery":
                        case "dom":
                            val = $(val).val();
                            break;
                        case "id":
                            val = $("#" + val).val();
                            break;
                        case "string":
                        default:
                            break;
                    }
                }
                return value === val;
            },
            message: "输入的内容不匹配."
        }
    };
    $.extend($.fn.validatebox.defaults.rules, rules);




    function initialize(target) {
        var t = $(target);
        var opts = t.validatebox("options");
        if (!opts._initialized) {
            t.addClass("validatebox-f").change(function () {
                opts.value = $(this).val();
                if ($.isFunction(opts.onChange)) {
                    opts.onChange.call(target, opts.value);
                }
            });
            opts.originalValue = opts.value;
            if (opts.value) {
                setValue(target, opts.value);
            }
            if (opts.width && !t.parent().is("span.combo,span.spinner,span.searchbox")) {
                resize(target, opts.width);
            }
            setPrompt(target, opts.prompt, opts);
            if (opts.autoFocus) {
                $.util.exec(function () { t.focus(); });
            }
            if (!opts.autovalidate) {
                t.validatebox("disableValidation").validatebox("enableValidation");
            }
            if (opts.defaultClass) {
                t.addClass(opts.defaultClass);
            }
            setDisabled(target, opts.disabled);
            opts._initialized = true;
        }
    };

    function setPrompt(target, prompt, opts) {
        var t = $(target);
        opts = opts || t.validatebox("options");
        opts.prompt = prompt;
        if ($.html5.testProp("placeholder", t[0].nodeName)) {
            t.attr("placeholder", prompt);
        } else {
            if (!$.isFunction(opts.promptFocus)) {
                opts.promptFocus = function () {
                    if (t.hasClass("validatebox-prompt")) {
                        t.removeClass("validatebox-prompt");
                        if (t.val() == opts.prompt) { t.val(""); }
                    }
                };
                t.focus(opts.promptFocus);
            }
            if (!$.isFunction(opts.promptBlur)) {
                opts.promptBlur = function () {
                    if ($.string.isNullOrEmpty(t.val())) { t.addClass("validatebox-prompt").val(opts.prompt); }
                }
                t.blur(opts.promptBlur);
            }
            if ($.string.isNullOrEmpty(t.val()) && !$.string.isNullOrEmpty(opts.prompt)) {
                $.util.exec(function () {
                    t.addClass("validatebox-prompt").val(opts.prompt);
                });
            }
        }
    }

    var _validate = $.fn.validatebox.methods.isValid;
    function validate(target) {
        var t = $(target);
        if (t.hasClass("validatebox-prompt")) {
            t.removeClass("validatebox-prompt").val("");
        }
        return _validate.call(t, t);
    };


    function setValue(target, value) {
        var t = $(target), opts = t.validatebox("options"), val = t.val();
        if (val != value) {
            t.val(opts.value = (value ? value : ""));
        }
        validate(target);
    };

    function getValue(target) {
        return $(target).val();
    };

    function clear(target) {
        var t = $(target), opts = t.validatebox("options");
        t.validatebox("setValue", "");
    };

    function reset(target) {
        var t = $(target), opts = t.validatebox("options");
        t.validatebox("setValue", opts.originalValue ? opts.originalValue : "");
    };

    function resize(target, width) {
        var t = $(target), opts = t.validatebox("options");
        t._outerWidth(opts.width = width);
    };

    function setDisabled(target, disabled) {
        var t = $(target), state = $.data(target, "validatebox");
        if (disabled) {
            if (state && state.options) { state.options.disabled = true; }
            t.attr("disabled", true);
        } else {
            if (state && state.options) { state.options.disabled = false; }
            t.removeAttr("disabled");
        }
    };


    var _validatebox = $.fn.validatebox;
    $.fn.validatebox = function (options, param) {
        if (typeof options == "string") {
            return _validatebox.apply(this, arguments);
        }
        options = options || {};
        return this.each(function () {
            var jq = $(this), hasInit = $.data(this, "validatebox") ? true : false,
                opts = hasInit ? options : $.extend({}, $.fn.validatebox.parseOptions(this), $.parser.parseOptions(this, [
                    "prompt", { autoFocus: "boolean" }
                ]), options);
            opts.value = opts.value || jq.val();
            _validatebox.call(jq, opts);
            initialize(this);
        });
    };
    $.union($.fn.validatebox, _validatebox);


    var methods = $.fn.validatebox.extensions.methods = {
        //  扩展 easyui-validatebox 的自定义扩展方法；设置当前 easyui-validatebox 控件的 prompt 值；该方法的参数 prompt 表示将被设置的 prompt 值；
        //  返回值：返回表示当前 easyui-validatebox 的 jQuery 链式对象。
        setPrompt: function (jq, prompt) { return jq.each(function () { setPrompt(this, prompt); }); },

        //  重写 easyui-validatebox 的原生方法；以支持相应扩展功能或属性。
        //  返回值：返回表示当前 easyui-validatebox 的 jQuery 链式对象。
        validate: function (jq) { return jq.each(function () { validate(this); }) },

        //  重写 easyui-validatebox 的原生方法；以支持相应扩展功能或属性。
        isValid: function (jq) { return validate(jq[0]); },

        setValue: function (jq, value) { return jq.each(function () { setValue(this, value); }); },

        getValue: function (jq) { return getValue(jq[0]); },

        clear: function (jq) { return jq.each(function () { clear(this); }); },

        reset: function (jq) { return jq.each(function () { reset(this); }); },

        resize: function (jq, width) { return jq.each(function () { resize(this, width); }); },

        enable: function (jq) { return jq.each(function () { setDisabled(this, false); }); },

        disable: function (jq) { return jq.each(function () { setDisabled(this, true); }); }
    };
    var defaults = $.fn.validatebox.extensions.defaults = {
        //  增加 easyui-validatebox 的扩展属性 prompt，该属性功能类似于 easyui-searchbox 的 prompt 属性。
        //  表示该验证输入框的提示文本；String 类型值，默认为 null。
        prompt: null,

        //  增加 easyui-validatebox 的扩展属性 autoFocus，该属性表示在当前页面加载完成后，该 easyui-validatebox 控件是否自动获得焦点。
        //  Boolean 类型值，默认为 false。
        autoFocus: false,

        //  增加 easyui-validatebox 的扩展属性 value，表示其初始化时的值
        value: null,

        //  增加 easyui-validatebox 的扩展属性 width，表示其初始化时的宽度值
        width: null,

        //  增加 easyui-validatebox 的扩展属性 autovalidate，表示是否在该控件初始化完成后立即进行一次验证；默认为 true。
        autovalidate: true,

        //  增加 easyui-validatebox 的扩展属性 disabled，表示该控件在初始化完成后是否设置其为禁用状态(disabled)；默认为 false。
        disabled: false,

        //  增加 easyui-validatebox 的扩展属性 defaultClass，表示 easyui-validatebox 初始化时默认需要加载的样式类名；
        //  该值将会被作为 html-class 属性在 easyui-validatebox 初始化完成后加载至 html 标签上。
        defaultClass: "textbox",

        //  增加 easyui-validatebox 的扩展事件 onChange，表示输入框在值改变时所触发的事件
        onChange: function (value) { }
    };

    $.extend($.fn.validatebox.defaults, defaults);
    $.extend($.fn.validatebox.methods, methods);


    if ($.fn.form && $.isArray($.fn.form.otherList)) {
        $.fn.form.otherList.push("validatebox");
        //$.array.insert($.fn.form.otherList, 0, "validatebox");
    }



    //  修改 jQuery 本身的成员方法 val；使之支持 easyui-validatebox 的扩展属性 prompt。
    var core_val = $.fn.val;
    $.fn.val = function (value) {
        if (this.length > 0 && this.is(".validatebox-text.validatebox-prompt") && !$.html5.testProp("placeholder", this[0].nodeName)) {
            var val, opts = this.validatebox("options");
            if (arguments.length == 0) {
                val = core_val.apply(this, arguments);
                return val == opts.prompt ? "" : val;
            }
            if (value && value != opts.prompt) {
                this.removeClass("validatebox-prompt");
            }
        }
        return core_val.apply(this, arguments);
    };


})(jQuery);