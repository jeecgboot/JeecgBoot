/**
* jQuery EasyUI 1.3.6
* Copyright (c) 2009-2014 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL or commercial licenses
* To use it on other terms please contact author: info@jeasyui.com
* http://www.gnu.org/licenses/gpl.txt
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI searchbox Extensions 1.0 beta
* jQuery EasyUI searchbox 组件扩展
* jeasyui.extensions.searchbox.js

*
* 依赖项：
*   1、jquery.jdirk.js v1.0 beta late
*   2、jeasyui.extensions.js v1.0 beta late
*   3、jeasyui.extensions.menu.js v1.0 beta late
*   4、jeasyui.extensions.linkbutton.js v1.0 beta late
*
* Copyright (c) 2013-2014 ChenJianwei personal All rights reserved.
* http://www.chenjianwei.org
*/
(function ($, undefined) {

    $.fn.searchbox.extensions = {};


    function initialize(target) {
        var state = $.data(target, "searchbox"), opts = state.options;
        if (!opts._initialized) {
            state.searchbox.find("input.searchbox-text").validatebox(opts);
            opts._initialized = true;
        }
    };



    var _destroy = $.fn.searchbox.methods.destroy;
    function destroy(target) {
        var t = $(target), textbox = t.searchbox("textbox");
        textbox.validatebox("destroy");
        _destroy.call(t, t);
    };



    var _searchbox = $.fn.searchbox;
    $.fn.searchbox = function (options, param) {
        if (typeof options == "string") {
            var method = $.fn.searchbox.methods[options];
            if (method) {
                return method(this, param);
            } else {
                return this.each(function () {
                    $(this).searchbox("textbox").validatebox(options, param);
                });
            }
        }
        options = options || {};
        return this.each(function () {
            var jq = $(this), hasInit = $.data(this, "searchbox") ? true : false,
                opts = hasInit ? options : $.extend({}, $.fn.validatebox.parseOptions(this), options);
            _searchbox.call(jq, opts);
            initialize(this);
        });
    };
    $.union($.fn.searchbox, _searchbox);


    var methods = $.fn.searchbox.extensions.methods = {

        destroy: function (jq) { return jq.each(function () { destroy(this); }); }
    };
    var defaults = $.fn.searchbox.extensions.defaults = {
    };

    $.extend($.fn.searchbox.defaults, defaults);
    $.extend($.fn.searchbox.methods, methods);

    if ($.fn.form && $.isArray($.fn.form.otherList)) {
        $.fn.form.otherList.push("searchbox");
        //$.array.insert($.fn.form.otherList, 0, "searchbox");
    }


    $.extend($.fn.datagrid.defaults.editors, {
        searchbox: {
            init: function (container, options) {
                var box = $("<input type=\"text\"></input>").appendTo(container).searchbox(options);
                box.searchbox("textbox").addClass("datagrid-editable-input");
                return box;
            },
            destroy: function (target) {
                $(target).searchbox("destroy");
            },
            getValue: function (target) {
                return $(target).searchbox("getValue");
            },
            setValue: function (target, value) {
                $(target).searchbox("setValue", value);
            },
            resize: function (target, width) {
                $(target).searchbox("resize", width);
            },
            setFocus: function (target) {
                $(target).searchbox("textbox").focus();
            }
        }
    });


})(jQuery);