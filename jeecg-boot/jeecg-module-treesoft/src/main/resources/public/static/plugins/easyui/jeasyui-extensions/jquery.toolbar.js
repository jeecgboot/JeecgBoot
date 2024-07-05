/**
* jQuery EasyUI 1.3.6
* Copyright (c) 2009-2014 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL or commercial licenses
* To use it on other terms please contact author: info@jeasyui.com
* http://www.gnu.org/licenses/gpl.txt
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI toolbar Plugin Extensions 1.0 beta
* jQuery EasyUI toolbar 插件扩展
* jquery.toolbar.js

*
* 依赖项：
*   1、jquery.jdirk.js
*   2、jeasyui.extensions.js
*   3、jeasyui.extensions.linkbutton
*   4、jeasyui.extensions.validatebox
*   5、combo
*   5、numberbox
*   6、numberspinner
*   6、timespinner
*   7、datebox
*   7、datetimebox
*   7、combobox
*   7、combotree
*   7、combogrid
*   8、jeasyui.extensions.combo
*   9、jeasyui.extensions.combobox
*   10、jeasyui.extensions.menu
*   11、jeasyui.extensions.panel
*   12、jeasyui.extensions.window
*   13、jeasyui.extensions.dialog
*   14、jeasyui.extensions.tree
*   15、jeasyui.extensions.combotree
*   16、jeasyui.extensions.datagrid
*   17、jeasyui.extensions.combogrid
*   18、jeasyui.extensions.searchbox
*   19、jeasyui.extensions.comboicons
*   20、jeasyui.extensions.comboselector
*   21、jeasyui.extensions.my97
*
* Copyright (c) 2013-2014 ChenJianwei personal All rights reserved.
* http://www.chenjianwei.org
*/
(function ($, undefined) {

    function initialize(target) {
        var t = $(target), isDiv = /^(?:div)$/i.test(target.nodeName), cc = t.children(),
            toolbar = isDiv ? t : $("<div></div>").insertAfter(t).append(t);
        if (!isDiv) {
            toolbar.attr({ "class": t.attr("class"), "style": t.attr("style") }).removeClass("easyui-toolbar");
            t.children().each(function () { toolbar.append(this); });
            t.hide();
        }
        var state = $.data(target, "toolbar"), opts = state.options;
        state.toolbar = toolbar.addClass("dialog-toolbar toolbar");
        t.addClass("toolbar-f");

        state.wrapper = $("<table class='toolbar-wrapper' cellspacing='0' cellpadding='0' ></table>").appendTo(toolbar);
        appendItem(target, cc);

        setSize(target, { width: opts.width, height: opts.height });
        toolbar.bind("_resize", function () {
            setSize(target);
        });
    };

    function setSize(target, size) {
        var t = $(target), state = $.data(target, "toolbar"),
            toolbar = state.toolbar, opts = state.options;
        size = $.extend({ width: opts.width, height: opts.height }, size || {});
        toolbar.css({
            width: size.width, height: size.height
        });
        $.extend(opts, size);
        $.util.exec(function () {
            setAlign(target, opts.align);
            setValign(target, opts.valign);
        });
        opts.onResize.call(target, $.isNumeric(size.width) ? size.width : toolbar.width(), $.isNumeric(size.height) ? size.height : toolbar.height());
    };

    function setAlign(target, align) {
        align = String(align);
        if (!$.array.contains(["left", "center", "right"], align.toLowerCase())) { return; }
        var t = $(target), state = $.data(target, "toolbar"),
            wrapper = state.wrapper, opts = state.options, left = 0;
        opts.align = align;
        wrapper.removeClass("toolbar-align-left toolbar-align-center toolbar-align-right").addClass("toolbar-align-" + align);
    };

    function setValign(target, valign) {
        valign = String(valign);
        if (!$.array.contains(["top", "middle", "bottom"], valign.toLowerCase())) { return; }
        var t = $(target), state = $.data(target, "toolbar"),
            toolbar = state.toolbar, wrapper = state.wrapper, opts = state.options,
            outerHeight = toolbar.height(), height = wrapper.height(), top;
        opts.valign = valign;
        wrapper.removeClass("toolbar-valign-top toolbar-valign-middle toolbar-valign-bottom").addClass("toolbar-valign-" + valign);
        switch (valign) {
            case "top": top = 0; break;
            case "middle": top = (outerHeight - height) / 2; break;
            case "bottom": top = (outerHeight - height); break;
        }
        wrapper.css("top", Math.max(top, 0));
    };



    function appendItemToContainer(target, container, item) {
        var state = $.data(target, "toolbar"), opts = state.options;
        if ($.util.isDOM(item)) {
            var cell = $(item).addClass("toolbar-item").appendTo(container), text = cell.text();
            if (/^(?:div|span)$/i.test(cell[0].nodeName) && $.array.contains(["-", "—", "|"], text)) {
                cell.addClass("dialog-tool-separator").empty();
                $.data(cell[0], "toolbar-item-data", {
                    actions: opts.itemOptions.separator, target: cell, options: {}, type: "separator", container: container
                });
            } else {
                $.data(cell[0], "toolbar-item-data", {
                    actions: null, target: cell, options: null, type: "custom", container: container
                });
            }
        } else if ($.array.contains(["string", "number", "date"], $.type(item))) {
            item = $.string.trim(item);
            if ($.array.contains(["-", "—", "|"], item)) {
                appendItemToContainer(target, container, { type: "separator" });
            } else if ($.string.isHtmlText(item)) {
                $(item).each(function () { appendItemToContainer(target, container, this); });
            } else {
                appendItemToContainer(target, container, { type: "label", options: { text: item } });
            }
        } else {
            var itemOpts = $.extend({}, opts.itemOptions, item || {}),
                actions = opts.itemTypes[itemOpts.type];
            if (!actions || !actions.init) { return; }
            var tItem = actions.init(container[0], itemOpts.options || itemOpts).addClass("toolbar-item");
            if (itemOpts.id) { tItem.attr("id", itemOpts.id); }
            if (itemOpts.name) { tItem.attr("name", itemOpts.name); }
            if (itemOpts.cls) { container.addClass(itemOpts.cls); }
            if (itemOpts.itemCls) { tItem.addClass(itemOpts.itemCls); }
            if (itemOpts.style) { container.css(itemOpts.style); }
            if (itemOpts.itemStyle) { tItem.css(itemOpts.itemStyle); }
            if (itemOpts.width) { container.css("width", itemOpts.width); }
            if (itemOpts.align) { container.css("text-align", itemOpts.align); }
            if (itemOpts.htmlAttr) { tItem.attr(itemOpts.htmlAttr); }
            $.data(tItem[0], "toolbar-item-data", {
                actions: actions, target: tItem, options: itemOptions.options, type: itemOpts.type, container: container
            });
        }
    };

    function appendItemOption(target, item) {
        if (!item) { return; }
        var state = $.data(target, "toolbar"),
            tr = state.wrapper.find("tr:last");
        if (!tr.length) { tr = $("<tr class='toolbar-row'></tr>").appendTo(state.wrapper); }
        var container = $("<td class='toolbar-item-container'></td>").appendTo(tr);
        appendItemToContainer(target, container, item);
    };

    function appendItem(target, item) {
        if (!item) { return; }
        if ($.array.likeArrayNotString(item)) {
            if (item.length) {
                $.each(item, function (i, n) { appendItem(target, n); });
            }
        } else if ($.isFunction(item)) {
            appendItem(target, item.call(target), false);
        } else {
            appendItemOption(target, item);
        }
    }




    function getItemIndex(target, item) {
        var ret = -1;
        if (!item) { return ret; }
        item = $(item);
        var t = $(target), wrapper = t.toolbar("wrapper"), tr = wrapper.find("tr:last");
        if (!tr.length || $.contains(tr[0], item[0])) { return ret; }
        ret = item.closest("toolbar-item-container").index();
        return ret;
    }

    function getItem(target, index) {
        if (index == null || index == undefined) { return null; }
        var item = null, itemEle, t = $(target), wrapper = t.toolbar("wrapper"), tr = wrapper.find("tr:last");
        if (!tr.length) { return item; }
        var tds = tr.find(">td.toolbar-item-container"), td = null;
        if ($.isNumeric(index)) {
            if (tds.length >= index && index >= 0) { itemEle = tds.eq(index).find(".toolbar-item"); }
        }
        if (itemEle && itemEle.length) {
            item = $.data(itemEle[0], "toolbar-item-data");
        }
        return item;
    };

    function getItems(target) {
        var ret = [], t = $(target), wrapper = t.toolbar("wrapper");
        wrapper.find("tr:last>td.toolbar-item-container .toolbar-item").each(function () {
            var item = $.data(this, "toolbar-item-data");
            if (item) { ret.push(item); }
        });
        return ret;
    };

    function removeItem(target, index) {
        var item = getItem(target, index);
        if (item) {
            if (item.actions && $.isFunction(item.actions.destroy)) { actions.destroy(item.target[0]); }
            item.container.remove();
        }
    };

    function updateItem(target, param) {
        if (!param || !$.isNumeric(param.index) || !param.item) { return; }
        var item = getItem(target, param.index), container = item.container.empty();
        appendItemToContainer(target, container, param.item)
    };




    function clear(target) {
        var state = $.data(target, "toolbar");
        state.wrapper.empty();
        state.data = null;
    };

    function loadData(target, data) {
        var state = $.data(target, "toolbar"), opts = state.options;
        state.data = opts.loadFilter.call(target, data);
        state.wrapper.empty();
        appendItem(target, state.data);
        opts.onLoadSuccess.call(target, data);
    };

    function request(target, queryParams) {
        var state = $.data(target, "toolbar"), opts = state.options;
        opts.queryParams = queryParams || {};
        if (opts.onBeforeLoad.call(target, opts.queryParams) == false) return;
        opts.loader.call(target, opts.queryParams, function (data) {
            loadData(target, data);
        }, function () {
            opts.onLoadError.apply(this, arguments);
        });
    };

    function getData(target) {
        return $.data(target, "toolbar").data;
    };

    function getValues(target) {
        return $(target).serializeObject();
    };




    function setItemFocus(target, index) {
        var item = getItem(target, index);
        if (item) {
            if (item.actions && $.isFunction(item.actions.setFocus)) {
                item.actions.setFocus(item.target[0]);
            } else {
                item.target.focus();
            }
        }
    };

    function setItemValue(target, param) {
        if (!param || !$.isNumeric(param.index)) { return; }
        var item = getItem(target, param.index);
        if (item && item.actions && $.isFunction(item.actions.setValue)) {
            item.actions.setValue(item.target[0], param.value);
        }
    };

    function getItemValue(target, index) {
        var item = getItem(target, index), ret = null;
        if (item) {
            ret = item.actions && $.isFunction(item.actions.getValue) ? item.actions.getValue(item.target[0]) : item.target[0].value;
        }
        return ret;
    };

    function resizeItem(target, param) {
        if (!param || !$.isNumeric(param.index) || !param.width) { return; }
        var item = getItem(target, index);
        if (item) {
            if (item.actions && $.isFunction(item.actions.resize)) {
                item.actions.resize(item.target[0], param.width);
            } else {
                item.target.width(param.width);
            }
        }
    };


    function enableItem(target, index) {
        var item = getItem(target, index);
        if (item && item.actions && $.isFunction(item.actions.enable)) {
            item.actions.enable(item.target[0]);
        }
    };

    function disableItem(target, index) {
        var item = getItem(target, index);
        if (item && item.actions && $.isFunction(item.actions.disable)) {
            item.actions.disable(item.target[0]);
        }
    };

    function enable(target) {
        var items = getItems(target);
        $.each(items, function (i, item) {
            if (item.actions && $.isFunction(item.actions.enable)) {
                item.actions.enable(item.target[0]);
            }
        });
    };

    function disable(target) {
        var items = getItems(target);
        $.each(items, function (i, item) {
            if (item.actions && $.isFunction(item.actions.disable)) {
                item.actions.disable(item.target[0]);
            }
        });
    };

    function destroy(target) {
        var t = $(target), state = $.data(target, "toolbar");
        if (state.toolbar) {
            state.toolbar.each(function () { destroyContent(this); }).remove();
        }
        t.remove();
    };

    function destroyContent(target) {
        var t = $(target);
        t.find(".combo-f").each(function () { $(this).combo("destroy"); });
        t.find(".m-btn").each(function () { $(this).menubutton("destroy"); });
        t.find(".s-btn").each(function () { $(this).splitbutton("destroy"); });
        t.find(".tooltip-f").each(function () { $(this).tooltip("destroy"); });
        t.children("div").each(function () { $(this)._fit(false); });
    };



    var itemTypes = {
        separator: {
            init: function (container) {
                return $("<div class='dialog-tool-separator'></div>").appendTo(container);
            }
        },
        label: {
            defaults: { text: " " },
            init: function (container, options) {
                var opts = $.extend({}, this.defaults, options || {});
                return $("<span class='toolbar-item-label'></span>").text(opts.text).appendTo(container);
            },
            enable: function (target) {
                $(target).removeClass("toolbar-item-label-disabled");
            },
            disable: function (target) {
                $(target).addClass("toolbar-item-label-disabled");
            }
        },
        button: {
            defaults: { plain: true, iconCls: "icon-ok" },
            init: function (container, options) {
                var opts = $.extend({}, this.defaults, options || {}),
                    handler = opts.onclick || opts.handler,
                    btn = $("<a class='toolbar-item-button'></a>").appendTo(container);
                if (handler) {
                    handler = $.string.toFunction(handler);
                }
                return btn.linkbutton($.extend(opts, {
                    onClick: function () {
                        if ($.isFunction(handler)) {
                            handler.call(this, $(container).closest("table.toolbar-wrapper")[0]);
                        }
                    }
                }));
            },
            enable: function (target) {
                $(target).linkbutton("enable");
            },
            disable: function (target) {
                $(target).linkbutton("disable");
            }
        },
        textbox: {
            defaults: { value: null, disabled: false, width: null },
            init: function (container, options) {
                var opts = $.extend({}, this.defaults, options || {}),
                    box = $("<input type='text' class='toolbar-item-input' />").appendTo(container);
                if (opts.value) { this.setValue(box[0], opts.value); }
                if (opts.disabled) { this.disable(box[0]); }
                if (opts.width) { this.resize(box[0], opts.width); }
                return box;
            },
            setValue: function (target, value) {
                $(target).val(value);
            },
            getValue: function (target) {
                return $(target).val();
            },
            resize: function (target, width) {
                $(target)._outerWidth(width);
            },
            enable: function (target) {
                $(target).removeAttr("disabled", true);
            },
            disable: function (target) {
                $(target).attr("disabled", true);
            }
        },
        checkbox: {
            defaults: { checked: false, disabled: false, text: " " },
            init: function (container, options) {
                options = options || {};
                var opts = $.extend({}, this.defaults, $.util.isString(options) ? { text: options } : options),
                    label = $("<label class='toolbar-item-checkbox'></label>").appendTo(container),
                    box = $("<input type='checkbox' class='toolbar-item-checkbox-input' />").appendTo(label),
                    span = $("<span class='toolbar-item-checkbox-text'></span>").text(opts.text).appendTo(label);
                if (opts.checked) { this.setValue(box[0], opts.checked); }
                if (opts.disabled) { this.disable(box[0]); }
                return box;
            },
            setValue: function (target, value) {
                $(target).attr("checked", value ? true : false);
            },
            getValue: function (target) {
                return $(target)[0].checked;
            },
            enable: function (target) {
                $(target).removeAttr("disabled").parent().find(">span.toolbar-item-checkbox-text").removeClass("toolbar-item-checkbox-disabled");
            },
            disable: function (target) {
                $(target).attr("disabled", true).parent().find(">span.toolbar-item-checkbox-text").addClass("toolbar-item-checkbox-disabled");
            }
        },
        validatebox: {
            defaults: { value: null, disabled: false, width: null },
            init: function (container, options) {
                var opts = $.extend({}, this.defaults, options || {}),
                    box = $("<input type='text' class='toolbar-item-input' />").appendTo(container).validatebox(opts);
                if (opts.value) { this.setValue(box[0], opts.value); }
                if (opts.disabled) { this.disable(box[0]); }
                if (opts.width) { this.resize(box[0], opts.width); }
                return box;
            },
            setValue: function (target, value) {
                $(target).val(value);
            },
            getValue: function (target) {
                return $(target).val();
            },
            resize: function (target, width) {
                $(target)._outerWidth(width);
            },
            enable: function (target) {
                $(target).removeAttr("disabled");
            },
            disable: function (target) {
                $(target).attr("disabled", true);
            }
        },
        numberbox: {
            defaults: { width: null },
            init: function (container, options) {
                var opts = $.extend({}, this.defaults, options || {}),
                    box = $("<input type='text' class='toolbar-item-input' />").appendTo(container).numberbox(opts);
                if (opts.width) { this.resize(box[0], opts.width); }
                return box;
            },
            destroy: function (target) {
                $(target).numberbox("destroy");
            },
            setValue: function (target, value) {
                $(target).numberbox("setValue", value);
            },
            getValue: function (target) {
                return $(target).numberbox("getValue");
            },
            resize: function (target, width) {
                $(target)._outerWidth(width);
            },
            enable: function (target) {
                $(target).numberbox("enable");
            },
            disable: function (target) {
                $(target).numberbox("disable");
            }
        },
        numberspinner: {
            defaults: {},
            init: function (container, options) {
                var opts = $.extend({}, this.defaults, options || {}),
                    box = $("<input type='text' class='toolbar-item-input' />").appendTo(container).numberspinner(opts);
                return box;
            },
            destroy: function (target) {
                $(target).numberspinner("destroy");
            },
            setValue: function (target, value) {
                $(target).numberspinner("setValue", value);
            },
            getValue: function (target) {
                return $(target).numberspinner("getValue");
            },
            resize: function (target, width) {
                $(target).numberspinner("resize", width);
            },
            enable: function (target) {
                $(target).numberspinner("enable");
            },
            disable: function (target) {
                $(target).numberspinner("disable");
            }
        },
        timespinner: {
            defaults: {},
            init: function (container, options) {
                var opts = $.extend({}, this.defaults, options || {}),
                    box = $("<input type='text' class='toolbar-item-input' />").appendTo(container).timespinner(opts);
                return box;
            },
            destroy: function (target) {
                $(target).timespinner("destroy");
            },
            setValue: function (target, value) {
                $(target).timespinner("setValue", value);
            },
            getValue: function (target) {
                return $(target).timespinner("getValue");
            },
            resize: function (target, width) {
                $(target).timespinner("resize", width);
            },
            enable: function (target) {
                $(target).timespinner("enable");
            },
            disable: function (target) {
                $(target).timespinner("disable");
            }
        },
        datebox: {
            defaults: {},
            init: function (container, options) {
                var opts = $.extend({}, this.defaults, options || {}),
                    box = $("<input type='text' class='toolbar-item-input' />").appendTo(container).datebox(opts);
                return box;
            },
            destroy: function (target) {
                $(target).datebox("destroy");
            },
            setValue: function (target, value) {
                $(target).datebox("setValue", value);
            },
            getValue: function (target) {
                return $(target).datebox("getValue");
            },
            resize: function (target, width) {
                $(target).datebox("resize", width);
            },
            enable: function (target) {
                $(target).datebox("enable");
            },
            disable: function (target) {
                $(target).datebox("disable");
            },
            setFocus: function (target) {
                $(target).datebox("textbox").focus();
            }
        },
        datetimebox: {
            defaults: {},
            init: function (container, options) {
                var opts = $.extend({}, this.defaults, options || {}),
                    box = $("<input type='text' class='toolbar-item-input' />").appendTo(container).datetimebox(opts);
                return box;
            },
            destroy: function (target) {
                $(target).datetimebox("destroy");
            },
            setValue: function (target, value) {
                $(target).datetimebox("setValue", value);
            },
            getValue: function (target) {
                return $(target).datetimebox("getValue");
            },
            resize: function (target, width) {
                $(target).datetimebox("resize", width);
            },
            enable: function (target) {
                $(target).datetimebox("enable");
            },
            disable: function (target) {
                $(target).datetimebox("disable");
            },
            setFocus: function (target) {
                $(target).datetimebox("textbox").focus();
            }
        },
        combo: {
            defaults: {},
            init: function (container, options) {
                var opts = $.extend({}, this.defaults, options || {}),
                    box = $("<input type='text' class='toolbar-item-input' />").appendTo(container).combo(opts);
                return box;
            },
            destroy: function (target) {
                $(target).combo("destroy");
            },
            setValue: function (target, value) {
                $(target).combo($.util.likeArrayNotString(value) ? "setValues" : "setValue", value);
            },
            getValue: function (target) {
                var combo = $(target), opts = combo.combo("options");
                return $(target).combo(opts.multiples ? "getValues" : "getValue");
            },
            resize: function (target, width) {
                $(target).combo("resize", width);
            },
            enable: function (target) {
                $(target).combo("enable");
            },
            disable: function (target) {
                $(target).combo("disable");
            },
            setFocus: function (target) {
                $(target).combo("textbox").focus();
            }
        },
        combobox: {
            defaults: {},
            init: function (container, options) {
                var opts = $.extend({}, this.defaults, options || {}),
                    box = $("<input type='text' class='toolbar-item-input' />").appendTo(container).combobox(opts);
                return box;
            },
            destroy: function (target) {
                $(target).combobox("destroy");
            },
            setValue: function (target, value) {
                $(target).combobox($.util.likeArrayNotString(value) ? "setValues" : "setValue", value);
            },
            getValue: function (target) {
                var combo = $(target), opts = combo.combobox("options");
                return $(target).combobox(opts.multiples ? "getValues" : "getValue");
            },
            resize: function (target, width) {
                $(target).combobox("resize", width);
            },
            enable: function (target) {
                $(target).combobox("enable");
            },
            disable: function (target) {
                $(target).combobox("disable");
            },
            setFocus: function (target) {
                $(target).combobox("textbox").focus();
            }
        },
        combotree: {
            defaults: {},
            init: function (container, options) {
                var opts = $.extend({}, this.defaults, options || {}),
                    box = $("<input type='text' class='toolbar-item-input' />").appendTo(container).combotree(opts);
                return box;
            },
            destroy: function (target) {
                $(target).combotree("destroy");
            },
            setValue: function (target, value) {
                $(target).combotree($.util.likeArrayNotString(value) ? "setValues" : "setValue", value);
            },
            getValue: function (target) {
                var combo = $(target), opts = combo.combotree("options");
                return $(target).combotree(opts.multiples ? "getValues" : "getValue");
            },
            resize: function (target, width) {
                $(target).combotree("resize", width);
            },
            enable: function (target) {
                $(target).combotree("enable");
            },
            disable: function (target) {
                $(target).combotree("disable");
            },
            setFocus: function (target) {
                $(target).combotree("textbox").focus();
            }
        },
        combogrid: {
            defaults: {},
            init: function (container, options) {
                var opts = $.extend({}, this.defaults, options || {}),
                    box = $("<input type='text' class='toolbar-item-input' />").appendTo(container).combogrid(opts);
                return box;
            },
            destroy: function (target) {
                $(target).combogrid("destroy");
            },
            setValue: function (target, value) {
                $(target).combogrid($.util.likeArrayNotString(value) ? "setValues" : "setValue", value);
            },
            getValue: function (target) {
                var combo = $(target), opts = combo.combogrid("options");
                return $(target).combogrid(opts.multiples ? "getValues" : "getValue");
            },
            resize: function (target, width) {
                $(target).combogrid("resize", width);
            },
            enable: function (target) {
                $(target).combogrid("enable");
            },
            disable: function (target) {
                $(target).combogrid("disable");
            },
            setFocus: function (target) {
                $(target).combogrid("textbox").focus();
            }
        },
        comboicons: {
            defaults: {},
            init: function (container, options) {
                var opts = $.extend({}, this.defaults, options || {}),
                    box = $("<input type='text' class='toolbar-item-input' />").appendTo(container).comboicons(opts);
                return box;
            },
            destroy: function (target) {
                $(target).comboicons("destroy");
            },
            setValue: function (target, value) {
                $(target).comboicons($.util.likeArrayNotString(value) ? "setValues" : "setValue", value);
            },
            getValue: function (target) {
                var combo = $(target), opts = combo.comboicons("options");
                return $(target).comboicons(opts.multiples ? "getValues" : "getValue");
            },
            resize: function (target, width) {
                $(target).comboicons("resize", width);
            },
            enable: function (target) {
                $(target).comboicons("enable");
            },
            disable: function (target) {
                $(target).comboicons("disable");
            },
            setFocus: function (target) {
                $(target).comboicons("textbox").focus();
            }
        },
        comboselector: {
            defaults: {},
            init: function (container, options) {
                var opts = $.extend({}, this.defaults, options || {}),
                    box = $("<input type='text' class='toolbar-item-input' />").appendTo(container).comboselector(opts);
                return box;
            },
            destroy: function (target) {
                $(target).comboselector("destroy");
            },
            setValue: function (target, value) {
                $(target).comboselector($.util.likeArrayNotString(value) ? "setValues" : "setValue", value);
            },
            getValue: function (target) {
                var combo = $(target), opts = combo.comboselector("options");
                return $(target).comboselector(opts.multiples ? "getValues" : "getValue");
            },
            resize: function (target, width) {
                $(target).comboselector("resize", width);
            },
            enable: function (target) {
                $(target).comboselector("enable");
            },
            disable: function (target) {
                $(target).comboselector("disable");
            },
            setFocus: function (target) {
                $(target).comboselector("textbox").focus();
            }
        },
        my97: {
            defaults: {},
            init: function (container, options) {
                var opts = $.extend({}, this.defaults, options || {}),
                    box = $("<input type='text' class='toolbar-item-input' />").appendTo(container).my97(opts);
                return box;
            },
            destroy: function (target) {
                $(target).my97("destroy");
            },
            setValue: function (target, value) {
                $(target).my97($.util.likeArrayNotString(value) ? "setValues" : "setValue", value);
            },
            getValue: function (target) {
                var combo = $(target), opts = combo.my97("options");
                return $(target).my97(opts.multiples ? "getValues" : "getValue");
            },
            resize: function (target, width) {
                $(target).my97("resize", width);
            },
            enable: function (target) {
                $(target).my97("enable");
            },
            disable: function (target) {
                $(target).my97("disable");
            },
            setFocus: function (target) {
                $(target).my97("textbox").focus();
            }
        },
        searchbox: {
            defaults: {},
            init: function (container, options) {
                var opts = $.extend({}, this.defaults, options || {}),
                    box = $("<input type='text' class='toolbar-item-input' />").appendTo(container);
                if (opts.searcher) {
                    opts.searcher = $.string.toFunction(opts.searcher);
                }
                return box.searchbox(opts);
            },
            destroy: function (target) {
                $(target).searchbox("destroy");
            },
            setValue: function (target, value) {
                $(target).searchbox("setValue", value);
            },
            getValue: function (target) {
                return $(target).searchbox("getValue");
            },
            resize: function (target, width) {
                $(target).searchbox("resize", width);
            },
            enable: function (target) {
                $(target).searchbox("enable");
            },
            disable: function (target) {
                $(target).searchbox("disable");
            },
            setFocus: function (target) {
                $(target).searchbox("textbox").focus();
            }
        }
    }, itemOptions = {
        id: null,
        name: null,
        type: "button",
        options: null,
        cls: null,
        style: null,
        itemCls: null,
        itemStyle: null,
        width: null,
        align: null,
        htmlAttr: null
    }, loader = function (param, success, error) {
        var opts = $(this).toolbar("options");
        if (!opts.url) { return false; }
        $.ajax({
            type: opts.method, url: opts.url, data: param, dataType: "json",
            success: function (data) {
                success(data);
            }, error: function () {
                error.apply(this, arguments);
            }
        });
    }, loadFilter = function (data) {
        return $.array.likeArrayNotString(data) ? data : [];
    };

    itemTypes.text = itemTypes.textbox;
    itemTypes.linkbutton = itemTypes.button;





    $.fn.toolbar = function (options, param) {
        if (typeof options == "string") {
            return $.fn.toolbar.methods[options](this, param);
        }
        options = options || {};
        return this.each(function () {
            var state = $.data(this, "toolbar");
            if (state) {
                $.extend(state.options, options);
            } else {
                state = $.data(this, "toolbar", {
                    options: $.extend({}, $.fn.toolbar.defaults, $.fn.toolbar.parseOptions(this), options)
                });
            }
            initialize(this);
            if (state.options.data) {
                loadData(this, state.options.data);
            }
            request(this);
        });
    };

    $.fn.toolbar.parseOptions = function (target) {
        return $.extend({}, $.parser.parseOptions(target, ["url", "data", "method", "width", "height", "align", "valign"]));
    };


    $.fn.toolbar.methods = {

        //  获取当前 easyui-toolbar 控件的 options 参数对象；
        //  返回值：返回当前 easyui-toolbar 控件的 options 参数对象，为一个 JSON-Object。
        options: function (jq) { return $.data(jq[0], 'toolbar').options; },

        //  获取当前 easyui-toolbar 控件的工具栏包装器对象；
        //  返回值：返回当前 easyui-toolbar 控件的工具栏包装器对象；该方法返回一个包含 html-table 的 jQuery 对象。
        wrapper: function (jq) { return $.data(jq[0], "toolbar").wrapper; },

        //  获取当前 easyui-toolbar 控件的工具栏外框对象；
        //  返回值：返回当前 easyui-toolbar 控件的工具栏外框对象；该方法返回一个包含 html-div 的 jQuery 对象。
        toolbar: function (jq) { return $.data(jq[0], "toolbar").toolbar; },

        //  设置当前 easyui-toolbar 控件的尺寸大小；该方法的参数 size 为一个 JSON-Object，该参数定义如下属性：
        //      width : 表示工具栏的新宽度；如果设置为数值类型，则表示像素宽度；如果设置为 "auto"，则表示自适应最大宽度；
        //      height: 表示工具栏的新高度；如果设置为数值类型，则表示像素高度；如果设置为 "auto"，则表示自适应一行按钮的高度；
        //  返回值：返回表示当前 easyui-toolbar 控件的 jQuery 链式对象。
        resize: function (jq, size) { return jq.each(function () { setSize(this, size); }); },

        //  设置当前 easyui-toolbar 控件的工具栏项水平居中方式；该方法的参数 align 为一个 String 类型值，其可以被定义的值限定为如下范围：
        //      left  :
        //      right :
        //      center:
        //  返回值：返回表示当前 easyui-toolbar 控件的 jQuery 链式对象。
        align: function (jq, align) { return jq.each(function () { setAlign(this, align); }); },

        //  设置当前 easyui-toolbar 控件的工具栏项垂直居中方式；该方法的参数 valign 为一个 String 类型值，其可以被定义的值限定为如下范围：
        //      top   :
        //      middle:
        //      bottom:
        //  返回值：返回表示当前 easyui-toolbar 控件的 jQuery 链式对象。
        valign: function (jq, valign) { return jq.each(function () { setValign(this, valign); }); },

        //  获取当前 easyui-toolbar 控件的所有工具栏子项数据；
        //  返回值：返回表示当前 easyui-toolbar 控件的所有工具栏子项数据所构成的一个数组，数组的每个元素格式参考 getItem 方法的返回值。
        getItems: function (jq) { return getItems(jq[0]); },

        //  获取当前 easyui-toolbar 控件的指定位置的工具栏子项数据；该方法的参数 index 表示要获取的工具栏子项的索引号，从 0 开始计数；
        //  返回值：返回表示当前 easyui-toolbar 控件指定位置的工具栏子项数据所构成的一个包含如下属性的 JSON-Object：
        //      actions:    表示该工具栏子项的初始化构造器；其值为 $.fn.toolbar.defaults.itemTypes 中的一个子项；
        //      options:    表示该工具栏子项初始化时的 options 参数数据；
        //      target :    表示该工具栏子项包含 "toolbar-item" 样式类的元素的 jQuery-DOM 对象；
        //      type   :    表示该工具栏子项的类型，如果是自定义加载的 html-DOM 对象则为 "custom"
        //      container:  表示该工具栏子项所在的 jQuery-DOM(html-td) 对象；
        getItem: function (jq, index) { return getItem(jq[0], index); },

        //  获取指定的工具栏子项在当前 easyui-toolbar 控件中的索引号；
        //      该方法的参数 item 表示一个工具栏子项，为一个 String 格式的 jQuery 选择器，或者是一个 HTML-DOM 对象，或者是一个 jQuery-DOM 对象。
        //  返回值：返回指定的工具栏子项在当前 easyui-toolbar 控件中的索引号，从 0 开始计数；
        //      如果 item 所示的对象不存在于当前 easyui-toolbar 中，则返回 -1。
        getItemIndex: function (jq, item) { return getItemIndex(jq[0], item); },

        //  获取当前 easyui-toolbar 控件加载的所有数据；仅在初始化该控件指定的 data 参数、通过 loadData 方法加载的数据和通过 url 远程加载的数据，才会被返回；
        //  返回值：返回一个数组对象，数组中的每一项都表示一个工具栏子项的数据格式(返回数据的格式参考 loadData 方法的参数 data 的数据格式)。
        getData: function (jq) { return getData(jq[0]); },

        //  获取当前 easyui-toolbar 控件中所有输入控件的输入值集合；
        //  返回值：返回一个 JSON-Object 对象，该对象中每个属性表示工具栏中一个具备输入值功能的子项控件，其值为输入控件当前的值；
        getValues: function (jq) { return getValues(jq[0]); },

        //  在当前 easyui-toolbar 中增加一个工具栏项；该方法的参数 item 可以定义为如下类型：
        //      1、jQuery-DOM 对象：
        //      2、HTML-DOM 对象：
        //      3、String 类型：可以为以下类型：
        //          a："-"、"—"、"|"，表示分割线的 separator
        //          b："<" 开头和 ">" 结尾切字符串度大于等于3，表示 HTML 代码段；
        //          c："\t"、"\n"，表示换行
        //          d：其他长度大于 0 的字符串，表示 label。
        //      4、JSON-Object 对象：
        //          id      : 
        //          type    : $.fn.toolbar.defaults.itemTypes 中定义的工具栏项类型，例如 separator、label、button、textbox、checkbox、numberbox、validatebox、combobox、combotree、combogrid 等；
        //          options : 初始化该工具栏项的参数；
        //          style   :
        //          itemCls :
        //          width   :
        //          align   :
        //      5、Array 数组类型：
        //  返回值：返回表示当前 easyui-toolbar 控件的 jQuery 链式对象。
        appendItem: function (jq, item) { return jq.each(function () { appendItem(this, item); }); },

        //  在当前的 easyui-toolbar 中移除一个工具栏项；该方法的参数 index 表示要删除的工具栏项的索引号，从 0 开始计数；
        //  返回值：返回表示当前 easyui-toolbar 控件的 jQuery 链式对象。
        removeItem: function (jq, index) { return jq.each(function () { removeItem(this, index); }); },

        //  将当前 easyui-toolbar 中指定位置的工具栏项替换成另一个工具栏项；该方法的参数 param 为一个 JSON-Object，包含如下属性定义：
        //      index:  表示要替换的工具栏项的索引号，从 0 开始计数；
        //      item:   参考 appendItem 方法的参数 item。
        //  返回值：返回表示当前 easyui-toolbar 控件的 jQuery 链式对象。
        updateItem: function (jq, param) { return jq.each(function () { updateItem(this, param); }); },

        //  启用当前 easyui-toolbar 控件的某个工具栏子项；该方法的参数 index 表示要操作的工具栏子项的索引号，从 0 开始计数；
        //  返回值：返回表示当前 easyui-toolbar 控件的 jQuery 链式对象。
        enableItem: function (jq, index) { return jq.each(function () { enableItem(this, index); }); },

        //  禁用当前 easyui-toolbar 控件的某个工具栏子项；该方法的参数 index 表示要操作的工具栏子项的索引号，从 0 开始计数；
        //  返回值：返回表示当前 easyui-toolbar 控件的 jQuery 链式对象。
        disableItem: function (jq, index) { return jq.each(function () { disableItem(this, index); }); },

        //  设置当前 easyui-toolbar 控件中指定索引号工具栏子项使其获取焦点；该方法的参数 param 为一个 JSON-Object，包含如下属性定义：
        //      index: 表示要设置值的工具栏子项的索引号，从 0 开始计数；
        //      value: 表示要设置的新值；
        //  返回值：返回表示当前 easyui-toolbar 控件的 jQuery 链式对象。
        setItemFocus: function (jq, index) { return jq.each(function () { setItemFocus(this, index); }); },

        //  设置当前 easyui-toolbar 控件中指定索引号工具栏子项的值；该方法的参数 param 为一个 JSON-Object，包含如下属性定义：
        //      index: 表示要设置值的工具栏子项的索引号，从 0 开始计数；
        //      value: 表示要设置的新值；
        //  返回值：返回表示当前 easyui-toolbar 控件的 jQuery 链式对象。
        //  备注：仅当 index 指定的工具栏子项存在且支持赋值(setValue)操作时，该方法才生效；
        setItemValue: function (jq, param) { return jq.each(function () { setItemValue(this, param); }); },

        //  获取当前 easyui-toolbar 控件中指定索引号工具栏子项的值；方法的参数 index 表示要获取值的工具栏子项的索引号，从 0 开始计数；
        //  返回值：返回指定索引号位置的工具栏子项的值；
        //  备注：仅当 index 指定的工具栏子项存在且支持取值(getValue)操作时，该方法才生效；
        getItemValue: function (jq, index) { return getItemValue(jq[0], index); },

        //  重新设置当前 easyui-toolbar 控件中指定索引号工具栏子项的宽度值；该方法的参数 param 为一个 JSON-Object，包含如下属性定义：
        //      index: 表示要设置宽度值的工具栏子项的索引号，从 0 开始计数；
        //      width: 表示要设置的新宽度值；
        //  返回值：返回表示当前 easyui-toolbar 控件的 jQuery 链式对象。
        //  备注：仅当 index 指定的工具栏子项存在且支持 resize 操作时，该方法才生效；
        resizeItem: function (jq, param) { return jq.each(function () { resizeItem(this, param); }); },

        //  启用当前 easyui-toolbar 控件的所有工具栏子项；
        //  返回值：返回表示当前 easyui-toolbar 控件的 jQuery 链式对象。
        enable: function (jq) { return jq.each(function () { enable(this); }); },

        //  禁用当前 easyui-toolbar 控件的所有工具栏子项；
        //  返回值：返回表示当前 easyui-toolbar 控件的 jQuery 链式对象。
        disable: function (jq) { return jq.each(function () { disable(this); }); },

        //  清空当前 easyui-toolbar 控件的所有工具栏子项；
        //  返回值：返回表示当前 easyui-toolbar 控件的 jQuery 链式对象。
        clear: function (jq) { return jq.each(function () { clear(this); }); },

        //  请求远程 url 地址所示的服务器数据并重新加载当前 easyui-toolbar 控件的所有工具栏子项；该方法的参数 queryParams 为一个 JSON-Object，表示请求远程数据时发送的查询参数；
        //  返回值：返回表示当前 easyui-toolbar 控件的 jQuery 链式对象。
        //  备注：执行该方法会清空当前 easyui-toolbar 控件中原来的所有子项控件。
        load: function (jq, queryParams) { return jq.each(function () { request(this, queryParams); }); },

        //  以指定的数据重新加载当前 easyui-toolbar 控件的所有工具栏子项；该方法的参数 data 为一个数组，表示请求远程数据时发送的查询参数；数组中的每项都表示一个待加载的工具栏子项，其可以定义为如下类型：
        //      1、jQuery-DOM 对象：
        //      2、HTML-DOM 对象：
        //      3、String 类型：可以为以下类型：
        //          a："-"、"—"、"|"，表示分割线的 separator
        //          b："<" 开头和 ">" 结尾切字符串度大于等于3，表示 HTML 代码段；
        //          c："\t"、"\n"，表示换行
        //          d：其他长度大于 0 的字符串，表示 label。
        //      4、JSON-Object 对象：
        //          id      : 
        //          type    : $.fn.toolbar.defaults.itemTypes 中定义的工具栏项类型，例如 separator、label、button、textbox、checkbox、numberbox、validatebox、combobox、combotree、combogrid 等；
        //          options : 初始化该工具栏项的参数；
        //          style   :
        //          itemCls :
        //          width   :
        //          align   :
        //  返回值：返回表示当前 easyui-toolbar 控件的 jQuery 链式对象。
        //  备注：执行该方法会清空当前 easyui-toolbar 控件中原来的所有子项控件。
        loadData: function (jq, data) { return jq.each(function () { loadData(this, data); }); },

        destroy: function (jq) { return jq.each(function () { destroy(this); }); }
    };

    $.fn.toolbar.defaults = {

        //  表示远程服务器访问地址，用于从远程服务器加载工具栏数据；
        url: null,

        //  表示 easyui-toolbar 控件在初始时需要加载的内容数据；
        data: null,

        method: "post",

        //  表示 easyui-toolbar 控件的宽度，Number 类型数值；默认为 auto；
        width: "auto",

        //  表示 easyui-toolbar 控件的高度，Number 类型数值；默认为 auto；
        height: "auto",

        //  表示 easyui-toolbar 控件的横向对齐方式，可选的值为 "left"、"center" 或 "right"；默认为 "left"；
        align: "left",

        //  表示 easyui-toolbar 控件的纵向对齐方式，可选的值为 "top"、"middle" 或 "bottom"；默认为 "middle"；
        valign: "middle",

        //  表示 easyui-toolbar 的尺寸大小重置事件；当控件大小被调整后触发；该事件回调函数定义如下参数：
        //      width: 被设置的新的宽度；
        //      height: 被设置的新的告诉。
        //  回调函数中的 this 表示当前 easyui-toolbar 的 DOM 对象。
        onResize: function (width, height) { },

        onLoadSuccess: function (data) { },

        onLoadError: function () { },

        onBeforeLoad: function (param) { },

        //  定义 easyui-toolbar 插件能够添加的工具栏项类型；
        //  开发人员可以通过扩展 $.fn.toolbar.defaults.itemTypes 属性来实现其自定义的 easyui-toolbar 工具栏项类型；
        //      就像扩展 $.fn.datagrid.defaults.editors 一样。
        //  已经内置的工具栏项类型有：
        //      separator   :
        //      label       :
        //      button      :
        //      textbox     :
        //      checkbox    :
        //      validatebox :
        //      numberbox   :
        //      datebox     :
        //      combobox    :
        //      combotree   :
        //      combogrid   :
        itemTypes: itemTypes,

        loader: loader,

        loadFilter: loadFilter,

        itemOptions: itemOptions
    };



    $.parser.plugins.push("toolbar");

})(jQuery);