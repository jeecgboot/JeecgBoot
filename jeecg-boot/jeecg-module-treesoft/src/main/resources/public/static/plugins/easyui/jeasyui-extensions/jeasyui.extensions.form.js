/**
* jQuery EasyUI 1.3.6
* Copyright (c) 2009-2014 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL or commercial licenses
* To use it on other terms please contact author: info@jeasyui.com
* http://www.gnu.org/licenses/gpl.txt
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI form Extensions 1.0 beta
* jQuery EasyUI form 组件扩展
* jeasyui.extensions.form.js
*
* 依赖项：
*   1、jquery.jdirk.js v1.0 beta late
*   2、jeasyui.extensions.js v1.0 beta late
*   3、jeasyui.extensions.validatebox.js v1.0 beta late
*
* Copyright (c) 2013-2014 ChenJianwei personal All rights reserved.
* http://www.chenjianwei.org
*/
(function ($, undefined) {


    $.fn.form.extensions = {};


    function getData(target, param) {
        if (!param) {
            var t = $(target), state = $.data(target, "form"), opts = state ? state.options : $.fn.form.defaults;
            param = opts.serializer;
        }
        return $(target).serializeObject(param);
    };


    var _submit = $.fn.form.methods.submit;
    function submit(target, options) {
        var t = $(target), state = $.data(target, "form"),
            isForm = (/^(?:form)$/i.test(target.nodeName) && state) ? true : false,
            opts = $.extend(
                {}, (state ? state.options : $.fn.form.defaults), (typeof options == "string") ? { url: options } : ($.isFunction(options) ? { success: options } : options || {})
            ),
            loading = function () {
                if (opts.showLoading) {
                    $.easyui.loading({ msg: opts.loadingMessage, locale: opts.loadingLocale });
                }
            },
            loaded = function () {
                if (opts.showLoading) {
                    if (opts.loadedDelay) {
                        $.util.exec(function () { $.easyui.loaded(opts.loadingLocale); }, opts.loadedDelay);
                    } else {
                        $.easyui.loaded(opts.loadingLocale);
                    }
                }
            };

        if (!opts.url) { opts.url = window.location.href; }
        if (isForm) { return _submit.call(t, t, opts); }

        var param = t.form("getData");
        if ($.isFunction(opts.onSubmit) && opts.onSubmit.call(target, param) == false) { return loaded(); }
        var beforeSend = $.ajaxSettings.beforeSend, complete = $.ajaxSettings.complete;
        $.ajax({
            url: opts.url, type: opts.method, data: param,
            success: function (data) {
                if ($.isFunction(opts.success)) { return opts.success.call(target, data); }
            },
            beforeSend: function () {
                var ret = $.isFunction(beforeSend) ? beforeSend.apply(this, arguments) : undefined;
                loading();
                return ret;
            },
            complete: function () {
                var ret = $.isFunction(complete) ? complete.apply(this, arguments) : undefined;
                loaded();
                return ret;
            }
        });

        //$[opts.method](opts.url, param, function (data) { opts.success(data); });
    };

    function load(target, data) {
        if (!$.data(target, 'form')) {
            $.data(target, 'form', {
                options: $.extend({}, $.fn.form.defaults)
            });
        }
        var t = $(target), opts = $.data(target, 'form').options;

        if (typeof data == 'string') {
            var param = {};
            if (opts.onBeforeLoad.call(target, param) == false) { return; }
            $.ajax({
                url: data, data: param, dataType: 'json', type: opts.method,
                success: function (data) { _load(data); },
                error: function () { opts.onLoadError.apply(target, arguments); }
            });
        } else {
            _load(data);
        }

        function _load(data) {
            for (var name in data) {
                var val = data[name];
                var rr = _checkField(name, val);
                if (!rr.length) {
                    var count = _loadOther(name, val);
                    if (!count) {
                        $.each($.fn.form.valueMarkList, function (i, mark) {
                            $(mark + '[name="' + name + '"]', t).val(val);
                        });
                        $.each($.fn.form.textMarkList, function (i, mark) {
                            $(mark + '[name="' + name + '"]', t).text(val);
                        });
                    }
                }
                _loadCombo(name, val);
            }
            opts.onLoadSuccess.call(target, data);
            t.form("validate");
        }

        /**
		 * check the checkbox and radio fields
		 */
        function _checkField(name, val) {
            var rr = t.find('input[name="' + name + '"][type=radio], input[name="' + name + '"][type=checkbox]');
            rr._propAttr('checked', false);
            rr.each(function () {
                var f = $(this);
                if (f.val() == String(val) || $.inArray(f.val(), $.isArray(val) ? val : [val]) >= 0) {
                    f._propAttr('checked', true);
                }
            });
            return rr;
        }

        function _loadOther(name, val) {
            var count = 0;
            var pp = $.fn.form.otherList;
            for (var i = 0; i < pp.length; i++) {
                var p = pp[i];
                var f = t.find('[' + p + 'Name="' + name + '"]');
                if (f.length) {
                    f[p]('setValue', val);
                    count += f.length;
                }
            }
            return count;
        }

        function _loadCombo(name, val) {
            var cc = $.fn.form.comboList;
            var c = t.find('[comboName="' + name + '"]');
            if (c.length) {
                for (var i = cc.length - 1; i >= 0; i--) {
                    //for (var i = 0; i < cc.length; i++) {
                    var type = cc[i];
                    if (c.hasClass(type + '-f')) {
                        if (c[type]('options').multiple) {
                            c[type]('setValues', val);
                        } else {
                            c[type]('setValue', val);
                        }
                        return;
                    }
                }
            }
        }
    };


    function clear(target) {
        $($.fn.form.valueMarkList.join(","), target).each(function () {
            var t = this.type, tag = this.tagName.toLowerCase();
            if (t == 'text' || t == 'hidden' || t == 'password' || tag == 'textarea') {
                this.value = '';
            } else if (t == 'file') {
                var file = $(this), newfile = file.clone().insertAfter(file).val('');
                if (file.data('validatebox')) {
                    file.validatebox('destroy');
                    newfile.validatebox();
                } else {
                    file.remove();
                }
            } else if (t == 'checkbox' || t == 'radio') {
                this.checked = false;
            } else if (tag == 'select') {
                this.selectedIndex = -1;
            }
        });

        var t = $(target),
            plugins = $.array.distinct($.array.merge([], $.fn.form.otherList, $.fn.form.spinnerList, $.fn.form.comboList));
        for (var i = 0; i < plugins.length; i++) {
            var plugin = plugins[i],
                r = t.find('.' + plugin + '-f');
            if (r.length && $.fn[plugin] && $.fn[plugin]["methods"]) {
                $.util.tryExec(function () {
                    r[plugin]("clear");
                });
            }
        }
        t.form("validate");
    };

    function reset(target) {
        var t = $(target), state = $.data(target, "form"), isForm = /^(?:form)$/i.test(target.nodeName) && state ? true : false;
        if (isForm) {
            target.reset();
        }
        var plugins = $.array.distinct($.array.merge([], $.fn.form.otherList, $.fn.form.spinnerList, $.fn.form.comboList));
        for (var i = 0; i < plugins.length; i++) {
            var plugin = plugins[i];
            var r = t.find('.' + plugin + '-f');
            if (r.length && $.fn[plugin] && $.fn[plugin]["methods"]) {
                $.util.tryExec(function () {
                    r[plugin]("reset");
                });
            }
        }
        t.form("validate");
    };


    function validate(target) {
        var t = $(target);

        if ($.fn.validatebox) {
            t.find('.validatebox-text:not(:disabled)').validatebox('validate');
            var invalidbox = t.find('.validatebox-invalid');
            invalidbox.filter(':not(:disabled):first').focus();
            if (invalidbox.length) {
                return false;
            }
        }

        var plugins = $.array.distinct($.array.merge([], $.fn.form.otherList, $.fn.form.spinnerList, $.fn.form.comboList));
        for (var i = 0; i < plugins.length; i++) {
            var plugin = plugins[i];
            var r = t.find('.' + plugin + '-f');
            if (r.length && $.fn[plugin] && $.fn[plugin]["methods"]) {
                if ($.util.tryExec(function () { return r[plugin]("isValid"); }) === false) {
                    $.util.tryExec({
                        code: function () { r[plugin]("focus"); },
                        error: function () { r[plugin]("textbox").focus(); },
                        tryError: true
                    });
                    return false;
                }
            }
        }

        return true;
    }

    function setValidation(target, novalidate) {
        var t = $(target);
        t.find('.validatebox-text:not(:disabled)').validatebox(novalidate ? 'disableValidation' : 'enableValidation');

        var plugins = $.array.distinct($.array.merge([], $.fn.form.otherList, $.fn.form.spinnerList, $.fn.form.comboList));
        for (var i = 0; i < plugins.length; i++) {
            var plugin = plugins[i];
            var r = t.find('.' + plugin + '-f');
            if (r.length && $.fn[plugin] && $.fn[plugin]["methods"]) {
                $.util.tryExec(function () {
                    r[plugin](novalidate ? 'disableValidation' : 'enableValidation');
                });
            }
        }
    };

    function setFormDisabled(target, disabled, withButton) {
        var t = $(target), state = $.data(target, "form");
        disabled = disabled ? true : false;

        if (state && state.options) { state.disabled = disabled; }
        var cc = withButton ? t.find("input, select, textarea") : t.find("input, select, textarea, button, a.l-btn, .m-btn, .s-btn");
        if (withButton) {
            $.each(cc, function (i, elem) {
                var item = $(elem);
                if (item.is(".s-btn")) {
                    item.splitbutton(disabled ? "disable" : "enable");
                } else if (item.is(".m-btn")) {
                    item.menubutton(disabled ? "disable" : "enable");
                } else if (item.is("a.l-btn")) {
                    item.linkbutton(disabled ? "disable" : "enable");
                } else {
                    disabled ? item.attr("disabled", true) : item.removeAttr("disabled");
                }
            });
        }

        if ($.fn.validatebox) {
            t.find('.validatebox-text').validatebox(disabled ? "disable" : "enable");
        }
        var plugins = $.array.distinct($.array.merge([], $.fn.form.otherList, $.fn.form.spinnerList, $.fn.form.comboList));
        for (var i = 0; i < plugins.length; i++) {
            var plugin = plugins[i];
            var r = t.find('.' + plugin + '-f');
            if (r.length && $.fn[plugin] && $.fn[plugin]["methods"]) {
                $.util.tryExec(function () {
                    r[plugin](disabled ? "disable" : "enable");
                });
            }
        }
    };



    var methods = $.fn.form.extensions.methods = {
        //  获取 easyui-form 控件容器内所有表单控件的 JSON 序列化数据；该方法的参数 param 可以定义为如下格式：
        //      1、JSON-Object  ：该对象定义如下属性：
        //          onlyEnabled:    表示返回的结果数据中是否仅包含启用(disabled == false)的 HTML 表单控件；Boolean 类型值，默认为 false。
        //          transcript :    表示当范围内存在重名(name 相同时)的 DOM 元素时，对重复元素的取值规则；
        ///                 这是一个 String 类型值，可选的值限定在以下范围：
        //              cover  :    覆盖方式，只取后面元素 的值，丢弃前面元素的值；默认值；
        //              discard:    丢弃后面元素的值，只取前面元素的值；
        //              overlay:    将所有元素的值进行叠加；
        //          overtype   :    元素叠加方式，当 transcript 的值定义为 "overlay" 时，此属性方有效；
        //                  这是一个 String 类型值，可选的值限定在以下范围：
        //              array  :    将所有重复的元素叠加为一个数组；
        //              append :    将所有的重复元素叠加为一个字符串；默认值；
        //          separator  :    元素叠加的分隔符，定义将所有重名元素叠加为一个字符串时用于拼接字符串的分隔符；
        //                  这是一个 String 类型值，默认为 ","；当 transcript 的值定义为 "overlay" 且 overtype 的值定义为 "append" 时，此属性方有效。
        //      2、String 类型值:   表示当范围内存在重名(name 相同时)的 DOM 元素时，对重复元素的取值规则；
        //              其取值范围和当参数格式为 JSON-Object 时的属性 transcript 一样。
        //  返回值：该方法返回一个 JSON Object，返回对象中的每个数据都表示一个表单控件值。
        getData: function (jq, param) { return getData(jq[0], param); },

        //  重写 easyui-form 控件的 submit 方法，使之除了支持 form 标签提交外，还支持 div 等其他容器标签的提交。
        //      该方法中的参数 options 可以同 easyui-form 的原生方法 submit 参数格式一样；
        //      也可以是一个 String 类型值表示提交的服务器端 url 地址；
        //      也可以是一个 function 回调函数表示 ajax 提交成功后的回调函数；
        submit: function (jq, options) { return jq.each(function () { submit(this, options); }); },

        //  重写 easyui-form 控件的 clear 方法，使其支持扩展的 easyui 插件操作；
        clear: function (jq) { return jq.each(function () { clear(this); }); },

        //  重写 easyui-form 控件的 reset 方法，使其支持扩展的 easyui 插件操作；
        reset: function (jq) { return jq.each(function () { reset(this); }); },

        //  重写 easyui-form 控件的 validate 方法，使其支持扩展的 easyui 插件操作；
        validate: function (jq) { return validate(jq[0]); },

        //  重写 easyui-form 控件的 enableValidation 方法，使其支持扩展的 easyui 插件操作；
        enableValidation: function (jq) { return jq.each(function () { setValidation(this, false); }); },

        //  重写 easyui-form 控件的 disableValidation 方法，使其支持扩展的 easyui 插件操作；
        disableValidation: function (jq) { return jq.each(function () { setValidation(this, true); }); },

        //  增加 easyui-form 控件的自定义方法；启用该表单 DOM 所有子级节点的输入效果(移除所有子级可输入控件的 disabled 效果)
        //  该方法的参数 withButton 表示是否连同表单中的按钮控件(html-button、html-input-button、easyui-menu|linkbutton|menubutton|splitbutton)一并启用；
        enable: function (jq, withButton) { return jq.each(function () { setFormDisabled(this, false, withButton); }); },

        //  增加 easyui-form 控件的自定义方法；禁用该表单 DOM 所有子级节点的输入效果(给所有子级可输入控件增加 disabled 效果)
        //  该方法的参数 withButton 表示是否连同表单中的按钮控件(html-button、html-input-button、easyui-menu|linkbutton|menubutton|splitbutton)一并禁用；
        disable: function (jq, withButton) { return jq.each(function () { setFormDisabled(this, true, withButton); }); },

        //  重写 easyui-form 控件的 load 方法。
        load: function (jq, data) { return jq.each(function () { load(this, data); }); }
    };
    var defaults = $.fn.form.extensions.defaults = {

        method: "post",

        showLoading: true, 
        
        loadingLocale: "body",
        
        loadingMessage: "正在将数据发送至服务器...", 
        
        loadedDelay: 300,

        serializer: { onlyEnabled: true, transcript: "overlay", overtype: "append", separator: "," }
    };

    $.extend($.fn.form.defaults, defaults);
    $.extend($.fn.form.methods, methods);

    $.fn.form.comboList = ['combo', 'datebox', 'datetimebox', 'combogrid', 'combotree', 'combobox'];
    //$.fn.form.comboList = ['combobox', 'combotree', 'combogrid', 'datetimebox', 'datebox', 'combo'];
    $.fn.form.spinnerList = ['timespinner', 'numberspinner', 'spinner'];
    $.fn.form.valueMarkList = ["input", "textarea", "select"];
    $.fn.form.textMarkList = ["span", "label", "div", "p"];
    $.fn.form.otherList = ["numberbox", "slider"];

})(jQuery);