/**
* jQuery EasyUI 1.3.6
* Copyright (c) 2009-2014 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL or commercial licenses
* To use it on other terms please contact author: info@jeasyui.com
* http://www.gnu.org/licenses/gpl.txt
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI linkbutton Extensions 1.0 beta
* jQuery EasyUI linkbutton 组件扩展
* jeasyui.extensions.linkbutton.js
*
* 依赖项：
*   1、jquery.jdirk.js v1.0 beta late
*   2、jeasyui.extensions.js v1.0 beta late
*
* Copyright (c) 2013-2014 ChenJianwei personal All rights reserved.
* http://www.chenjianwei.org
*/
(function ($, undefined) {

    $.fn.linkbutton.extensions = {};

    function initialize(target) {
        var t = $(target), opts = t.linkbutton("options"),
            exts = opts.extensions ? opts.extensions : opts.extensions = {};
        if (!exts._initialized) {
            setStyle(target, opts.style);
            setTooltip(target, opts.tooltip);
            exts._initialized = true;
        }
    };

    function setIcon(target, iconCls) {
        var t = $(target), opts = t.linkbutton("options"), icon = t.find("span.l-btn-icon");
        if (iconCls) {
            if (icon.length) {
                icon.removeClass(opts.iconCls).addClass(iconCls);
            } else {
                t.find("span.l-btn-text").after("<span class=\"l-btn-icon " + iconCls + "\">&nbsp;</span>");
            }
        } else {
            icon.remove();
        }
        opts.iconCls = iconCls;
    };

    function setText(target, text) {
        var t = $(target), opts = t.linkbutton("options"), textSpan = t.find("span.l-btn-text");
        if (text) {
            textSpan.removeClass("l-btn-empty").text(text);
        } else {
            textSpan.addClass("l-btn-empty").html("&nbsp;");
        }
        opts.text = text;
    };

    function setIconAlign(target, iconAlign) {
        var t = $(target), opts = t.linkbutton("options"), span = t.find(">span");
        span.removeClass("l-btn-icon-left l-btn-icon-right l-btn-icon-top l-btn-icon-bottom").addClass("l-btn-icon-" + iconAlign);
        opts.iconAlign = iconAlign;
    }

    function setStyle(target, style) {
        if (style) {
            $(target).css(style);
        }
    };

    function setTooltip(target, tooltip) {
        var t = $(target), opts = t.linkbutton("options"), isFunc = $.isFunction(tooltip);
        opts.tooltip = tooltip;
        if (opts.tooltip) {
            var topts = { content: !isFunc ? tooltip : null };
            if (isFunc) {
                $.extend(topts, {
                    onShow: function (e) {
                        $(this).tooltip("update", tooltip.call(target, e));
                    }
                });
            }
            t.tooltip(topts);
        }
        if (opts.tooltip == false) {
            t.tooltip("destroy");
        }
    };

    function setPlain(target, plain) {
        var t = $(target), opts = t.linkbutton("options");
        t[(opts.plain = plain ? true : false) ? "addClass" : "removeClass"]("l-btn-plain");
    };

    function setSize(target, size) {
        var t = $(target), opts = t.linkbutton("options");
        t.removeClass("l-btn-small l-btn-large").addClass("l-btn-" + size);
        opts.size = size;
    };



    var _linkbutton = $.fn.linkbutton;
    $.fn.linkbutton = function (options, param) {
        if (typeof options == "string") {
            return _linkbutton.apply(this, arguments);
        }
        options = options || {};
        return this.each(function () {
            var jq = $(this), hasInit = $.data(this, "linkbutton") ? true : false,
                opts = hasInit ? options : $.extend({}, $.fn.linkbutton.parseOptions(this), $.parser.parseOptions(this, [
                    "tooltip"
                ]), options);
            _linkbutton.call(jq, opts);
            initialize(this);
        });
    };
    $.union($.fn.linkbutton, _linkbutton);


    var defaults = $.fn.linkbutton.extensions.defaults = {
        //  增加 easyui-linkbutton 控件的自定义属性；表示 linkbutton 按钮的自定义样式。
        style: null,

        //  增加 easyui-linkbutton 控件的自定义属性；表示 linkbutton 按钮鼠标放置提示。
        //      String 类型或 Function 类型，
        tooltip: null
    };

    var methods = $.fn.linkbutton.extensions.methods = {
        //  增加 easyui-linkbutton 控件的自定义扩展方法；设置 linkbutton 按钮的图标；该方法定义如下参数：
        //      iconCls:    String 类型值，表示要设置的新的图标样式
        //  返回值：返回表示当前 easyui-linkbutton 控件的 jQuery 链式对象；
        setIcon: function (jq, iconCls) { return jq.each(function () { setIcon(this, iconCls); }); },

        //  增加 easyui-linkbutton 控件的自定义扩展方法；设置 linkbutton 按钮的文字；该方法定义如下参数：
        //      text:   String 类型值，表示要设置的新的按钮文本内容
        //  返回值：返回表示当前 easyui-linkbutton 控件的 jQuery 链式对象；
        setText: function (jq, text) { return jq.each(function () { setText(this, text); }); },

        //  增加 easyui-linkbutton 控件的自定义扩展方法；设置 linkbutton 按钮的图标位置；该方法定义如下参数：
        //      iconAlign:   String 类型值，表示要设置的按钮的图标位置；该参数限定取值 "left"、"right"、"top"、"bottom"
        //  返回值：返回表示当前 easyui-linkbutton 控件的 jQuery 链式对象；
        setIconAlign: function (jq, iconAlign) { return jq.each(function () { setIconAlign(this, iconAlign); }); },

        //  增加 easyui-linkbutton 控件的自定义扩展方法；设置 linkbutton 按钮的自定义样式；该方法定义如下参数：
        //      style:   JSON-Object 类型，表示要设置的按钮的样式
        //  返回值：返回表示当前 easyui-linkbutton 控件的 jQuery 链式对象；
        setStyle: function (jq, style) { return jq.each(function () { setStyle(this, style); }); },

        //  增加 easyui-linkbutton 控件的自定义扩展方法；设置 linkbutton 按钮的 plain 属性；该方法定义如下参数：
        //      plain:   Boolean 类型，表示要设置的按钮的 plain 属性值
        //  返回值：返回表示当前 easyui-linkbutton 控件的 jQuery 链式对象；
        setPlain: function (jq, plain) { return jq.each(function () { setPlain(this, plain); }); },

        //  增加 easyui-linkbutton 控件的自定义扩展方法；设置 linkbutton 按钮的 size 属性；该方法定义如下参数：
        //      size:   String 类型，表示要设置的按钮的 size 属性值；该参数限定取值 'small','large'
        //  返回值：返回表示当前 easyui-linkbutton 控件的 jQuery 链式对象；
        setSize: function (jq, size) { return jq.each(function () { setSize(this, size); }); },

        //  增加 easyui-linkbutton 控件的自定义扩展方法；设置 linkbutton 按钮的 tooltip 属性；该方法定义如下参数：
        //      tooltip: String 类型或 Function 类型，表示要设置的按钮的 prompt 属性值；如果该参数值为 false，则表示销毁该按钮的 easyui-tooltip 效果；
        setTooltip: function (jq, tooltip) { return jq.each(function () { setTooltip(this, tooltip); }) }
    };

    $.extend($.fn.linkbutton.defaults, defaults);
    $.extend($.fn.linkbutton.methods, methods);

})(jQuery);