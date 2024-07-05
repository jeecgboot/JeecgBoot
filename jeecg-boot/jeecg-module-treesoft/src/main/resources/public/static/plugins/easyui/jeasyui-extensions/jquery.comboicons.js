/**
* jQuery EasyUI 1.3.6
* Copyright (c) 2009-2014 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL or commercial licenses
* To use it on other terms please contact author: info@jeasyui.com
* http://www.gnu.org/licenses/gpl.txt
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI comboicons Plugin Extensions 1.0 beta
* jQuery EasyUI comboicons 插件扩展
* jquery.comboicons.js
*
* 依赖项：
*   1、jquery.jdirk.js v1.0 beta late
*   2、jeasyui.extensions.js v1.0 beta late
*   3、jeasyui.extensions.combo.js v1.0 beta late
*   4、jeasyui.extensions.menu.js v1.0 beta late
*   5、jeasyui.extensions.panel.js v1.0 beta late
*   6、jeasyui.extensions.window.js v1.0 beta late
*   7、jeasyui.extensions.dialog.js v1.0 beta late
*   8、icons/jeasyui.icons.all.js 和 icons/icon-all.css v1.0 beta late
*   9、jeasyui.extensions.icons.js v1.0 beta late
*
* Copyright (c) 2013-2014 ChenJianwei personal All rights reserved.
* http://www.chenjianwei.org
*/
(function ($, undefined) {

    function create(target) {
        var state = $.data(target, "comboicons"), opts = state.options,
            t = $(target).addClass("comboicons-f").combo($.extend({}, opts, {
                onShowPanel: function () {
                    t.combo("panel").hide();
                    state.dialog = $.easyui.icons.showSelector({
                        width: opts.panelWidth, height: opts.panelHeight, size: opts.size,
                        selected: t.combo(opts.multiple ? "getValues" : "getValue"),
                        multiple: opts.multiple,
                        buttons: [
                            {
                                index: 101.5, text: "清除", iconCls: "icon-hamburg-busy", handler: function (d) {
                                    t.combo("clear");
                                    d.close();
                                }
                            }
                        ],
                        onEnter: function (val) {
                            if (val) {
                                var isArray = $.isArray(val), text = isArray ? val.join(", ") : val;
                                t.combo(isArray ? "setValues" : "setValue", val).combo("setText", text);
                            } else {
                                t.combo("clear");
                            }
                        },
                        onClose: function () {
                            var state = $.data(target, "combo");
                            if (state && state.options) { t.combo("hidePanel"); }
                        }
                    });
                    var dopts = state.dialog.dialog("options");
                    if ((!$.util.isUtilTop && !dopts.topMost) || $.util.isUtilTop) {
                        var textbox = t.combo("textbox"), offset = textbox.offset();
                        state.dialog.dialog("move", $.extend(offset, { top: offset.top + textbox.outerHeight() + 2 }))
                    }
                    if ($.isFunction(opts.onShowPanel)) { opts.onShowPanel.apply(this, arguments); }
                },
                onHidePanel: function () {
                    if (state.dialog) {
                        var dia = state.dialog, dopts = dia.dialog("options");
                        state.dialog = null;
                        if (!dopts.closed) { dia.dialog("close"); }
                    }
                    if ($.isFunction(opts.onHidePanel)) { opts.onHidePanel.apply(this, arguments); }
                },
                onDestroy: function () {
                    if (state.dialog) {
                        state.dialog.dialog("destroy");
                        state.dialog = null;
                    }
                    if ($.isFunction(opts.onDestroy)) { opts.onDestroy.apply(this, arguments); }
                },
                onChange: function (newValue, oldValue) {
                    if ($.isFunction(opts.onChange)) {
                        opts.onChange.apply(this, arguments);
                    }
                }
            }));
        opts.originalValue = opts.value;
        if (opts.value) {
            setValues(target, $.util.likeArrayNotString(opts.value) ? opts.value : [opts.value]);
        }
        t.combo("validate");
    };

    function setValues(target, values) {
        var t = $(target), opts = t.comboicons("options"),
            array = $.util.likeArrayNotString(values) ? values : [values],
            text = array.join(opts.separator);
        t.combo("setValues", array).combo("setText", text);
    };



    $.fn.comboicons = function (options, param) {
        if (typeof options == "string") {
            var method = $.fn.comboicons.methods[options];
            if (method) {
                return method(this, param);
            } else {
                return this.combo(options, param);
            }
        }
        options = options || {};
        return this.each(function () {
            var state = $.data(this, "comboicons");
            if (state) {
                $.extend(state.options, options);
            } else {
                $.data(this, "comboicons", { options: $.extend({}, $.fn.comboicons.defaults, $.fn.comboicons.parseOptions(this), options) });
                create(this);
            }
        });
    };

    $.fn.comboicons.parseOptions = function (target) {
        return $.extend({}, $.fn.combo.parseOptions(target), $.parser.parseOptions(target, ["size", "iconCls"]));
    };

    $.fn.comboicons.methods = {
        options: function (jq) {
            var opts = jq.combo("options"), copts = $.data(jq[0], 'comboicons').options;
            return $.extend(copts, {
                originalValue: opts.originalValue, disabled: opts.disabled, readonly: opts.readonly
            });
        },

        setValues: function (jq, values) { return jq.each(function () { setValues(this, values); }); },

        setValue: function (jq, value) { return jq.each(function () { setValues(this, [value]); }); }
    };

    $.fn.comboicons.defaults = $.extend({}, $.fn.combo.defaults, {
        size: "16",
        iconCls: "icon-hamburg-zoom",
        panelWidth: 500,
        panelHeight: 360,
        autoShowPanel: false,
        editable: false
    });


    if ($.fn.datagrid) {
        $.extend($.fn.datagrid.defaults.editors, {
            comboicons: {
                init: function (container, options) {
                    var box = $("<input type=\"text\"></input>").appendTo(container).comboicons(options);
                    box.comboicons("textbox").addClass("datagrid-editable-input");
                    return box;
                },
                destroy: function (target) {
                    $(target).comboicons("destroy");
                },
                getValue: function (target) {
                    var t = $(target), opts = t.comboicons("options");
                    return t.comboicons(opts.multiple ? "getValues" : "getValue");
                },
                setValue: function (target, value) {
                    var t = $(target), opts = t.comboicons("options");
                    if (value) {
                        if (opts.multiple) {
                            if ($.util.likeArrayNotString(value)) {
                                t.comboicons("setValues", value);
                            } else if (typeof value == "string") {
                                t.comboicons("setValues", value.split(opts.separator));
                            } else {
                                t.comboicons("setValue", value);
                            }
                        } else {
                            t.comboicons("setValue", value);
                        }
                    } else {
                        t.comboicons("clear");
                    }
                },
                resize: function (target, width) {
                    $(target).comboicons("resize", width);
                },
                setFocus: function (target) {
                    $(target).comboicons("textbox").focus();
                }
            }
        });
    }


    $.parser.plugins.push("comboicons");

    if ($.fn.form && $.isArray($.fn.form.comboList)) {
        $.fn.form.comboList.push("comboicons");
        //$.array.insert($.fn.form.comboList, 0, "comboicons");
    }

})(jQuery);