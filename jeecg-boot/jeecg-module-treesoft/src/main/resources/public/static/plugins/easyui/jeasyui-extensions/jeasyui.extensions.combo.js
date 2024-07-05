/**
* jQuery EasyUI 1.3.6
* Copyright (c) 2009-2014 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL or commercial licenses
* To use it on other terms please contact author: info@jeasyui.com
* http://www.gnu.org/licenses/gpl.txt
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI combo Extensions 1.0 beta
* jQuery EasyUI combo 组件扩展
* jeasyui.extensions.combo.js 
* 依赖项：
*   1、jquery.jdirk.js v1.0 beta late
*   2、jeasyui.extensions.js v1.0 beta late
*
* Copyright (c) 2013-2014 ChenJianwei personal All rights reserved.
* http://www.chenjianwei.org
*/
(function ($, undefined) {

    $.fn.combo.extensions = {};

    function setPrompt(target, prompt) {
        var t = $(target), opts = t.combo("options"), textbox = t.combo("textbox");
        opts.prompt = prompt;
        textbox.validatebox("setPrompt", prompt);
    };

    function setIcon(target, iconCls) {
        var t = $(target), state = $.data(target, "combo"), combo = state.combo;
        var arrow = combo.find("span.combo-arrow").removeAttr("class").addClass("combo-arrow");
        if (iconCls) { arrow.addClass(iconCls); }
        t.combo("options").iconCls = iconCls;
    }

    function setRequired(target, required) {
        var t = $(target), opts = t.combo("options"), textbox = t.combo("textbox");
        opts.required = textbox.validatebox("options").required = required;
    };

    var _destroy = $.fn.combo.methods.destroy;
    function destroy(target) {
        var t = $(target), opts = t.combo("options");
        if ($.isFunction(opts.onBeforeDestroy) && opts.onBeforeDestroy.call(target) == false) { return; }
        _destroy.call(target, t);
        if ($.isFunction(opts.onDestroy)) { opts.onDestroy.call(target); }
    };

    function getCombo(target) {
        return $.data(target, "combo").combo;
    };


    var _setValues = $.fn.combo.methods.setValues;
    function setValues(target, values) {
        var t = $(target), opts = t.combo("options");
        if (!opts.nullable && $.array.isNullOrEmpty(values)) {
            values = [null];
        }
        return _setValues.call(t, t, values);
    };

    function clear(target) {
        var state = $.data(target, "combo"), opts = state.options, combo = state.combo;
        if (opts.multiple) {
            combo.find("input.combo-value" + (opts.nullable ? "" : ":gt(0)")).remove();
        }
        combo.find("input.combo-value,input.combo-text").val("");
    };




    function initialize(target) {
        var t = $(target), state = $.data(target, "combo"),
            opts = t.combo("options"), panel = state.panel,
            combo = state.combo, //arrow = combo.find(".combo-arrow"),
            exts = opts.extensions ? opts.extensions : opts.extensions = {};
        if (!exts._initialized) {
            t.combo("textbox").focus(function () {
                if (opts.autoShowPanel && panel.is(":hidden")) { t.combo("showPanel"); }
            });
            if (opts.iconCls) { t.combo("setIcon", opts.iconCls); }
            if ($.util.browser.msie && combo._outerWidth() != opts.width) {
                $.util.exec(function () { t.combo("resize", opts.width); });
            }
            exts._initialized = true;
        }
    }


    var _combo = $.fn.combo;
    $.fn.combo = function (options, param) {
        if (typeof options == "string") {
            return _combo.apply(this, arguments);
        }
        options = options || {};
        return this.each(function () {
            var jq = $(this), hasInit = $.data(this, "combo") ? true : false,
                opts = hasInit ? options : $.extend({}, $.fn.combo.parseOptions(this), $.parser.parseOptions(this, [
                    "iconCls", { autoShowPanel: "boolean" }
                ]), options);
            _combo.call(jq, opts);
            initialize(this);
        });
    };
    $.union($.fn.combo, _combo);


    var defaults = $.fn.combo.extensions.defaults = {
        //  增加 easyui-combo 的自定义扩展属性；表示该 combo 组件的 iconCls 图标样式类；
        //  String 类型值，默认为 null。
        iconCls: null,

        //  增加 easyui-combo 的自定义扩展属性；表示该 combox 组件是否在 textbox 文本显示框获取焦点时自动执行 showPanel 方法显示下拉 panel 面板；
        //  Boolean 类型值，默认为 true。
        autoShowPanel: true,

        //  增加 easyui-combo 的自定义扩展属性；表示该 combox 组件是否允许值为 Null。
        //  注意：nullable 值为 true 时，表示当该 combo 组件没有值(例如执行 clear 方法后，或者 setValue/setValues 设置为的值为 null/undefined/空数组 后)时，
        //      该组件内的所有 input:hidden.combo-value DOM标签将会被清空。
        //      而当 nullable 的值设置为 false 时，在该 combo 组件没有值时，也会保留一个值为空字符串("")的 input:hidden.combo-value DOM标签。
        //  Boolean 类型值，默认为 true。
        nullable: true,

        onBeforeDestroy: function () { },

        onDestroy: function () { }
    };

    var methods = $.fn.combo.extensions.methods = {
        //  扩展 easyui-combo 组件的自定义方法；用于设置 easyui-combo 控件的右侧显示图标，该方法定义如下参数：
        //      iconCls:    String 类型的值，表示需要设置的图标的 css 类样式名，例如 "icon-ok", "icon-save"
        //  返回值：返回表示当前 easyui-combo 控件的 jQuery 链式对象。
        setIcon: function (jq, iconCls) { return jq.each(function () { setIcon(this, iconCls); }); },

        //  扩展 easyui-combo 组件的自定义方法；用于设置启用或者禁用 easyui-combo 控件的表单验证功能，该方法定义如下参数：
        //      required:   Boolean 类型的值，表示启用或者禁用 easyui-combo 控件的表单验证功能。
        //  返回值：返回表示当前 easyui-combo 控件的 jQuery 链式对象。
        setRequired: function (jq, required) { return jq.each(function () { setRequired(this, required); }); },

        //  扩展 easyui-combo 组件的自定义方法；用于设置该 combo 的 textbox 输入框的 prompt(输入提示文字) 值；该方法定义如下参数：
        //      prompt: String 类型值，表示要被设置的 prompt 值；
        //  返回值：返回表示当前 easyui-combo 控件的 jQuery 链式对象。
        setPrompt: function (jq, prompt) { return jq.each(function () { setPrompt(this, prompt); }); },

        destroy: function (jq) { return jq.each(function () { destroy(this); }); },

        combo: function (jq) { return getCombo(jq[0]); },

        //  重写 easyui-combo 组件的 setValues 方法，以支持相应扩展功能。
        setValues: function (jq, values) { return jq.each(function () { setValues(this, values); }); },

        //  重写 easyui-combo 组件的 setValues 方法，以支持相应扩展功能。
        clear: function (jq) { return jq.each(function () { clear(this); }); }
    };
    $.extend($.fn.combo.defaults, defaults);
    $.extend($.fn.combo.methods, methods);





    //  下面这段代码实现即使在跨 IFRAME 的情况下，一个 WEB-PAGE 中也只能同时显示一个 easyui-combo panel 控件。
    $.easyui.bindPageNestedFunc("mousedown", "jdirkCombo", "combo", function (win, e) {
        var jq = win.jQuery, p = jq(e.target).closest("span.combo,div.combo-p");
        if (p.length) {
            jq(p).find(".combo-f").each(function () {
                var panel = jq(this).combo("panel");
                if (panel.is(":visible")) {
                    panel.panel("close");
                }
            });
            if (e.target && e.target.ownerDocument == win.document) {
                return;
            }
        }
        jq("body>div.combo-p>div.combo-panel:visible").panel("close");
    });


})(jQuery);