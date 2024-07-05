/**
* jQuery EasyUI 1.3.6
* Copyright (c) 2009-2014 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL or commercial licenses
* To use it on other terms please contact author: info@jeasyui.com
* http://www.gnu.org/licenses/gpl.txt
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI ueditor Plugin Extensions 1.0 beta
* jQuery EasyUI ueditor 插件扩展
* jquery.ueditor.js

*
* 依赖项：
*   1、jquery.jdirk.js v1.0 beta late
*   2、jeasyui.extensions.js v1.0 beta late

*   3、ueditor/themes/default/css/ueditor.css
*   4、ueditor/ueditor.config.js
*   5、ueditor/ueditor.all.js
*   6、ueditor/lang/zh-cn/zh-cn.js
*
* Copyright (c) 2013-2014 ChenJianwei personal All rights reserved.
* http://www.chenjianwei.org
*/
(function ($, undefined) {

    function create(target) {
        var t = $(target).addClass("ueditor-f"),
            isDiv = /^(?:div)$/i.test(target.nodeName), isText = /^(?:textarea)$/i.test(target.nodeName),
            html = isText ? (t.val() || t.text()) : t.html(),
            state = $.data(target, "ueditor"), opts = state.options,
            name = t.attr("name"), id = state.editorId = "ueditor_" + $.util.guid("N"),
            panel = state.panel = isDiv ? t.addClass("ueditor-panel").empty() : $("<div class=\"ueditor-panel\"></div>").insertAfter(t.hide().empty()).append(t),
            textarea = state.textarea = $("<textarea id=\"" + id + "\"" + (name ? " name=\"" + name + "\"" : "") + "></textarea>").appendTo(panel);
        if (name) {
            t.attr("ueditorName", name).removeAttr("name");
        }
        if (html) { textarea.val(html); }

        if (opts.value) {
            opts.initialContent = String(opts.value);
        }
        opts.originalValue = html || opts.initialContent;
        if (opts.template) {
            opts.toolbars = opts.toolbarsTemplate[opts.template]
        }

        state.panel.panel($.extend({}, opts, {
            noheader: true,
            onDestroy: $.fn.panel.defaults.onDestroy,
            onResize: function (width, height) {
                $.fn.panel.defaults.onResize.apply(this, arguments);
                $.extend(opts, { width: width, height: height });
                setEditorSize(target);
                if ($.isFunction(opts.onResize)) { opts.onResize.apply(target, arguments); }
            }
        }));

        state.wrapper = state.panel.panel("body").addClass("ueditor-wrapper");

        state.editor = UE.getEditor(id, opts);

        initialEvents(target);
        initialState(target);
        setValidation(target);
    };

    function initialEvents(target) {
        var state = $.data(target, "ueditor"), opts = state.options;
        $.each($.fn.ueditor.events, function (i, n) {
            var eventName = "on" + n, e = opts[eventName];
            state.editor.addListener(n, function () {
                if (n == "contentChange") {
                    var val = opts.value = $(target).ueditor("getValue");
                    state.textarea.val(val);
                }
                if (n == "setHeight") {
                    var body = $(state.editor.body), height = body.parent().height();
                    body.height(height);
                }
                if ($.isFunction(e)) { return e.apply(target, arguments); }
            });
        });
    };

    function initialState(target) {
        var state = $.data(target, "ueditor"), opts = state.options, checkCount = 20;
        checkState();
        function checkState() {
            state.edui = state.textarea.prev();
            state.eduieditor = state.edui.find(">div.edui-editor");
            state.toolbarbox = state.eduieditor.find(">div.edui-editor-toolbarbox");
            state.iframeholder = state.eduieditor.find(">div.edui-editor-iframeholder");
            state.bottomContainer = state.eduieditor.find(">div.edui-editor-bottomContainer");
            state.scalelayer = state.eduieditor.find(">div[id$=scalelayer]");

            if (state.editor.iframe && state.editor.iframe.parentNode && state.editor.iframe.parentNode.style && state.editor.body) {
                setEditorSize(target);
                setEditorDisabled(target, opts.disabled);
            } else {
                if (--checkCount) {
                    $.util.exec(checkState, 100);
                }
            }
        };
    };


    function getOptions(target) {
        var state = $.data(target, "ueditor"), opts = state.panel.panel("options");
        return $.extend(state.options, {
            title: opts.title, iconCls: opts.iconCls, width: opts.width, height: opts.height, left: opts.left, top: opts.top,
            cls: opts.cls, headerCls: opts.headerCls, bodyCls: opts.bodyCls, style: opts.style, fit: opts.fit, border: opts.border,
            doSize: opts.doSize, noheader: opts.noheader, collapsible: opts.collapsible, minimizable: opts.minimizable, maximizable: opts.maximizable,
            closable: opts.closable, collapsed: opts.collapsed, minimized: opts.minimized, maximized: opts.maximized, closed: opts.closed
        });
    };




    function setEditorSize(target) {
        var t = $(target), state = $.data(target, "ueditor");
        if (state && state.toolbarbox && state.bottomContainer && state.scalelayer && state.iframeholder && state.eduieditor && state.editor) {
            var opts = state.options, size = t._fit(),
                vwidth = size.width - (opts.border ? 0 : 2);
            state.iframeholder.width(vwidth);
            state.eduieditor.width(vwidth);

            var vheight = size.height - state.toolbarbox.height() - state.bottomContainer.height() - state.scalelayer.height() - (opts.border ? 0 : 2);
            state.editor.setHeight(vheight);
        }
    };

    function resize(target, param) {
        var state = $.data(target, "ueditor");
        state.panel.panel("resize", param);
    };



    function setEditorDisabled(target, disabled, except) {
        var state = $.data(target, "ueditor"), opts = state.options, d = disabled ? true : false;
        opts.disabled = d;
        return state.editor[d ? "setDisabled" : "setEnabled"](except);
    };

    function showEditor(target) {
        var state = $.data(target, "ueditor"), opts = state.options;
        opts.isShow = true;
        state.panel.panel("open");
        state.editor.setShow();
        return getOptions(target);
    };

    function hideEditor(target) {
        var state = $.data(target, "ueditor"), opts = state.options;
        opts.isShow = false;
        state.editor.setHide();
        state.panel.panel("close");
        return getOptions(target);
    };


    function destroy(target) {
        var t = $(target), state = $.data(target, "ueditor");
        if (state) {
            if (state.editor) {
                if (state.editor.fontset) {
                    state.editor.fontset.dialog("destroy");
                }
                state.editor.destroy();
            }
            if (state.panel) { state.panel.panel("destroy"); }
        }
        t.remove();
    };

    function sync(target, formId) {
        var state = $.data(target, "ueditor");
        if (formId) {
            return state.editor.sync();
        } else {
            return state.editor.sync(formId);
        }
    };

    function addshortcutkey(target, param) {
        var state = $.data(target, "ueditor");
        return state.editor.addshortcutkey(param);
    };

    function setFocus(target, toEnd) {
        var state = $.data(target, "ueditor");
        if (typeof toEnd == "boolean") {
            return state.editor.focus(toEnd);
        } else {
            return state.editor.focus();
        }
    };

    function execCommand(target, cmdName) {
        var state = $.data(target, "ueditor");
        return state.editor.execCommand(cmdName);
    };

    function queryCommandState(target, cmdName) {
        var state = $.data(target, "ueditor");
        return state.editor.queryCommandState(cmdName);
    };

    function queryCommandValue(target, cmdName) {
        var state = $.data(target, "ueditor");
        return state.editor.queryCommandValue(cmdName);
    };

    function hasContents(target, tags) {
        var state = $.data(target, "ueditor");
        if (tags) {
            var array = $.util.likeArrayNotString(tags) ? tags : []
            return state.editor.hasContents(array);
        } else {
            return state.editor.hasContents();
        }
    };

    function reset(target) {
        var state = $.data(target, "ueditor"), opts = state.options;
        setValue(target, opts.originalValue);
        return state.editor.reset();
    };

    function getLang(target, path) {
        var state = $.data(target, "ueditor");
        return state.editor.getLang(path);
    };

    function getContentLength(target, ingoneHtml) {
        var state = $.data(target, "ueditor");
        if (typeof ingoneHtml == "boolean") {
            return state.editor.getContentLength(ingoneHtml);
        } else {
            return state.editor.getContentLength();
        }
    };

    function addInputRule(target, rule) {
        var state = $.data(target, "ueditor");
        return state.editor.addInputRule(rule);
    };

    function addOutputRule(target, rule) {
        var state = $.data(target, "ueditor");
        return state.editor.addOutputRule(rule);
    };





    function getValue(target, param) {
        var state = $.data(target, "ueditor"), opts = state.options;
        return $(target).ueditor(opts.valueMethod, param);
    };

    function getContent(target, filter) {
        var state = $.data(target, "ueditor");
        return $.isFunction(filter) ? state.editor.getContent(filter) : state.editor.getContent();
    };

    function getAllHtml(target) {
        var state = $.data(target, "ueditor");
        return state.editor.getAllHtml();
    };

    function getPlainTxt(target) {
        var state = $.data(target, "ueditor");
        return state.editor.getPlainTxt();
    };

    function getContentTxt(target) {
        var state = $.data(target, "ueditor");
        return state.editor.getContentTxt();
    };


    function setValue(target, value, isAppendTo) {
        var state = $.data(target, "ueditor");
        if (isAppendTo) {
            return state.editor.setContent(value, true);
        } else {
            return state.editor.setContent(value);
        }
    };

    function setContent(target, param) {
        var state = $.data(target, "ueditor");
        param = param || "";
        if (typeof param == "string") {
            return state.editor.setContent(param);
        } else {
            return state.editor.setContent(param.value, param.isAppendTo);
        }
    };

    function clear(target) {
        var state = $.data(target, "ueditor");
        state.editor.execCommand("cleardoc");
    };





    function hideTip(target) {
        var state = $.data(target, "ueditor");
        state.tip = false;
        state.wrapper.tooltip("hide");
    };

    function showTip(target) {
        var state = $.data(target, "ueditor"), opts = state.options;
        state.wrapper.tooltip($.extend({}, opts.tipOptions, { content: opts.missingMessage, position: opts.tipPosition, deltaX: opts.deltaX })).tooltip("show");
        state.tip = true;
    };

    function repositionTip(target) {
        var state = $.data(target, "ueditor");
        if (state && state.tip) {
            state.wrapper.tooltip("reposition");
        }
    };

    function initializeValidate(target) {
        var t = $(target), state = $.data(target, "ueditor"), opts = state.options;
        state.wrapper.unbind(".ueditor");
        if (opts.novalidate) {
            return;
        }
        state.wrapper.bind("focus.ueditor", function () {
            state.validating = true;
            state.value = undefined;
            (function () {
                if (state.validating) {
                    var value = t.ueditor("getContentTxt");
                    if (state.value != value) {
                        state.value = value;
                        if (state.timer) {
                            clearTimeout(state.timer);
                        }
                        state.timer = setTimeout(function () { t.ueditor("validate"); }, opts.delay);
                    } else {
                        repositionTip(target);
                    }
                    setTimeout(arguments.callee, 200);
                }
            })();
        }).bind("blur.ueditor", function () {
            if (state.timer) {
                clearTimeout(state.timer);
                state.timer = undefined;
            }
            state.validating = false;
            hideTip(target);
        }).bind("mouseenter.ueditor", function () {
            if (state.wrapper.hasClass("ueditor-invalid")) {
                showTip(target);
            }
        }).bind("mouseleave.ueditor", function () {
            if (!state.validating) {
                hideTip(target);
            }
        });
    };

    function validate(target) {
        var t = $(target), state = $.data(target, "ueditor"), opts = state.options,
            value = getValue(target);
        state.wrapper.removeClass("ueditor-invalid");
        hideTip(target);
        if (!opts.novalidate && opts.required && $.string.isNullOrWhiteSpace(value) && !t.is(":disabled") && !state.wrapper.hasClass("ueditor-disabled")) {
            state.wrapper.addClass("ueditor-invalid");
            if (state.validating) {
                showTip(target);
            }
            return false;
        }
        return true;
    };

    function setValidation(target, novalidate) {
        var state = $.data(target, "ueditor"), opts = state.options;
        if (novalidate != undefined && novalidate != null) {
            opts.novalidate = novalidate;
        }
        if (opts.novalidate) {
            state.wrapper.find("div.edui-editor").removeClass("ueditor-invalid");
            hideTip(target);
        }
        initializeValidate(target);
    };









    $.fn.ueditor = function (options, param) {
        if (typeof options == "string") {
            return $.fn.ueditor.methods[options](this, param);
        }
        options = options || {};
        return this.each(function () {
            var state = $.data(this, "ueditor");
            if (state) {
                $.extend(state.options, options);
            } else {
                $.data(this, "ueditor", { options: $.extend({}, $.fn.ueditor.defaults, $.fn.ueditor.parseOptions(this), options) });
                create(this);
            }
        });
    };


    $.fn.ueditor.events = [
        "ready", "destroy", "reset", "focus", "langReady",
        "beforeExecCommand", "afterExecCommand", "firstBeforeExecCommand",
        "beforeGetContent", "afterGetContent", "getAllHtml", "beforeSetContent", "afterSetContent",
        "selectionchange", "beforeSelectionChange", "afterSelectionChange", "contentChange", "setHeight"
    ];
    $.fn.ueditor.toolbars = window.UEDITOR_CONFIG.toolbars;
    //$.fn.ueditor.toolbars = [[
    //    'fullscreen', 'source', '|', 'undo', 'redo', '|',
    //    'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', 'selectall', 'cleardoc', '|',
    //    'rowspacingtop', 'rowspacingbottom', 'lineheight', '|',
    //    'customstyle', 'paragraph', 'fontfamily', 'fontsize', '|',
    //    'directionalityltr', 'directionalityrtl', 'indent', '|',
    //    'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|', 'touppercase', 'tolowercase', '|',
    //    'link', 'unlink', 'anchor', '|', 'imagenone', 'imageleft', 'imageright', 'imagecenter', '|',
    //    'insertimage', 'emotion', 'scrawl', 'insertvideo', 'music', 'attachment', 'map', 'gmap', 'insertframe', 'insertcode', 'webapp', 'pagebreak', 'template', 'background', '|',
    //    'horizontal', 'date', 'time', 'spechars', 'snapscreen', 'wordimage', '|',
    //    'inserttable', 'deletetable', 'insertparagraphbeforetable', 'insertrow', 'deleterow', 'insertcol', 'deletecol', 'mergecells', 'mergeright', 'mergedown', 'splittocells', 'splittorows', 'splittocols', 'charts', '|',
    //    'print', 'preview', 'searchreplace', 'help', 'drafts'
    //]];
    $.fn.ueditor.parseOptions = function (target) {
        return $.extend({}, $.parser.parseOptions(target, [
            "lang", "theme", "charset", "initialContent", "textarea", "wordCountMsg", "tabNode", "sourceEditor", "value", "templet", "valueMethod",
            "missingMessage", "tipPosition",
            {
                enableAutoSave: "boolean", isShow: "boolean", focus: "boolean", fullscreen: "boolean", readonly: "boolean",
                imagePopup: "boolean", emotionLocalization: "boolean", pasteplain: "boolean", wordCount: "boolean", elementPathEnabled: "boolean",
                autoHeightEnabled: "boolean", scaleEnabled: "boolean", tableDragable: "boolean", sourceEditorFirst: "boolean",
                fit: "boolean", disabled: "boolean", border: "boolean",
                required: "boolean", novalidate: "boolean", autosave: "boolean"
            },
            {
                saveInterval: "number", zIndex: "number", maximumWords: "number", tabSize: "number", width: "number", height: "number",
                deltaX: "number"
            }
        ]));
    };








    $.fn.ueditor.buttons = {
        defaults: {
            name: null, label: null, iconCss: "background-position: 0 0;",
            execCommand: function (cmd) { },
            queryCommandState: function () { },
            queryCommandValue: function () { },
            onInit: function (editor) { }
        },
        list: [],

        styleTagID: "ueditorCss_" + $.util.guid("N"),

        append: function (button, init) {
            var b = $.extend({}, $.fn.ueditor.buttons.defaults, button),
                editorui = baidu.editor.ui, ci = b.name;

            UEDITOR_CONFIG.toolbars[0].push(b.name);
            for (var lang in b.label) {
                UE.I18N[lang].labelMap[b.name] = b.label[lang];
            }

            editorui[ci] = function (cmd) {
                return function (editor) {
                    var ui = new editorui.Button({
                        className: 'edui-for-' + cmd,
                        title: editor.options.labelMap[cmd] || editor.getLang("labelMap." + cmd) || '',
                        onclick: function () {
                            editor.execCommand(cmd);
                        },
                        theme: editor.options.theme,
                        showText: false
                    });
                    editorui.buttons[cmd] = ui;
                    editor.addListener('selectionchange', function (type, causeByUi, uiReady) {
                        var state = editor.queryCommandState(cmd);
                        if (state == -1) {
                            ui.setDisabled(true);
                            ui.setChecked(false);
                        } else {
                            if (!uiReady) {
                                ui.setDisabled(false);
                                ui.setChecked(state);
                            }
                        }
                    });
                    //下面这行代码为该插件添加的，使得ueditor编辑器在初始化该按钮时执行一个事件函数；
                    if ($.isFunction(b.onInit)) { $.util.exec(function () { b.onInit.call(ui, editor); }); }
                    return ui;
                };
            }(ci);

            UE.commands[b.name] = $.extend({}, b);

            var styleText = " .edui-for-" + b.name + " .edui-icon {" + (b.iconCss ? b.iconCss : "") + "}",
                style = $("#" + $.fn.ueditor.buttons.styleTagID);
            if (style.length) {
                styleText = (style.text() || style.html()) + "\n" + styleText;
                if ($.util.browser.msie && $.util.browser.version < 9) {
                    style.remove();
                    $.util.addCss(styleText).attr("id", $.fn.ueditor.buttons.styleTagID);
                } else {
                    style.text(styleText);
                }
            } else {
                $.util.addCss(styleText).attr("id", $.fn.ueditor.buttons.styleTagID);
            }

            if (!init) {
                $.fn.ueditor.buttons.list.push(b);
            }
        }
    };
    $.each($.fn.ueditor.buttons.list, function (i, btn) {
        $.fn.ueditor.buttons.append(btn, true);
    });







    $.fn.ueditor.buttons.refreshFontSet = {
        name: "fontset", iconCss: "background-position: -720px 0;",
        label: { "zh-cn": "字体设置" },
        execCommand: function (cmd) {
            var editor = this, offset = $(editor.container).offset(),
                fontset = editor.fontset ? editor.fontset : editor.fontset = createDialog(),
                fopts = fontset.dialog("options");
            fopts.closed ? fontset.dialog("open").dialog("move", { top: offset.top - 50, left: offset.left }) : fontset.dialog("close");

            function createDialog() {
                var fontset = $.easyui.showDialog({
                    noheader: true, modal: false, resizable: false, draggable: false, closed: true, autoDestroy: false, border: false, topMost: false,
                    width: 260, height: 50, top: offset.top - 50, left: offset.left,
                    enableSaveButton: false, enableCloseButton: false, enableApplyButton: false,
                    onOpen: function () { $.fn.ueditor.buttons.refreshFontSet.refresh(editor); },
                    content: "<div class=\"ueditor-fontset-container\"></div>"
                });
                $.util.exec(function () {
                    var cc = fontset.find(".ueditor-fontset-container"), config = UE.I18N[editor.ui.lang], fontfamily = [], fontsize = [];
                    for (var n in config.fontfamily) {
                        fontfamily.push({ value: config.fontfamily[n], text: config.fontfamily[n] });
                    }
                    for (var i = 0; i < editor.ui.fontsize.length; i++) {
                        fontsize.push({ value: editor.ui.fontsize[i] + "px", text: editor.ui.fontsize[i] + "px" });
                    }
                    fontset.fontfamily = $("<input class=\"ueditor-fontset-fontfamily\" type=\"text\" />").appendTo(cc).combobox({
                        width: 70, panelWidth: 120, editable: false, data: fontfamily,
                        formatter: function (r) {
                            return "<span style=\"font-family: " + r.text + ";\">" + r.text + "</span>";
                        },
                        onSelect: function (r) { editor.execCommand("fontfamily", r.text); },
                        onShowPanel: function () { fontset.fontsize.combobox("hidePanel"); }
                    });
                    fontset.fontsize = $("<input class=\"ueditor-fontset-fontsize\" type=\"text\" />").appendTo(cc).combobox({
                        width: 70, panelWidth: 120, editable: false, data: fontsize,
                        formatter: function (r) {
                            return "<span style=\"font-size: " + r.text + ";\">" + r.text + "</span>";
                        },
                        onSelect: function (r) { editor.execCommand("fontsize", r.text); },
                        onShowPanel: function () { fontset.fontfamily.combobox("hidePanel"); }
                    });
                    fontset.bold = $("<a></a>").appendTo(cc).linkbutton({
                        plain: true, toggle: true, iconCls: "icon-standard-text-bold", onClick: function () {
                            editor.execCommand("bold");
                        }
                    });
                    fontset.italic = $("<a></a>").appendTo(cc).linkbutton({
                        plain: true, toggle: true, iconCls: "icon-standard-text-italic", onClick: function () {
                            editor.execCommand("italic");
                        }
                    });
                    fontset.underline = $("<a></a>").appendTo(cc).linkbutton({
                        plain: true, toggle: true, iconCls: "icon-standard-text-underline", onClick: function () {
                            editor.execCommand("underline");
                        }
                    });

                    editor.addListener("focus", function () {
                        cc.find(".combobox-f").combobox("hidePanel");
                    });
                });
                return fontset;
            };
        },
        queryCommandState: function () {
            var editor = this;
            $.fn.ueditor.buttons.refreshFontSet.refresh(editor);
            return editor.fontset ? (editor.fontset.dialog("options").closed ? 0 : 1) : 0;
        },
        refresh: function (editor) {
            var fontfamily = editor.queryCommandValue("fontfamily"), fontsize = editor.queryCommandValue("fontsize"),
                bold = editor.queryCommandState("bold"), italic = editor.queryCommandState("italic"), underline = editor.queryCommandState("underline");
            if (editor.fontset && !editor.fontset.dialog("options").closed) {
                if (editor.fontset.fontfamily && fontfamily) {
                    var data = editor.fontset.fontfamily.combobox("getData"), array = fontfamily.split(","), b = false;
                    $.each(array, function (i, family) {
                        if (b) { return; }
                        family = $.trim(family);
                        if ($.array.contains(data, family, function (r) { return r.value == family; })) {
                            editor.fontset.fontfamily.combobox("setValue", family);
                            b = true;
                        }
                    });
                    if (!b) { editor.fontset.fontfamily.combobox("setValue", "arial"); }
                }
                if (editor.fontset.fontsize) {
                    editor.fontset.fontsize.combobox("setValue", fontsize);
                }
                if (editor.fontset.bold) {
                    editor.fontset.bold.linkbutton(bold == 1 ? "select" : "unselect");
                }
                if (editor.fontset.italic) {
                    editor.fontset.italic.linkbutton(italic == 1 ? "select" : "unselect");
                }
                if (editor.fontset.underline) {
                    editor.fontset.underline.linkbutton(underline == 1 ? "select" : "unselect");
                }
            }
        }
    };
    $.fn.ueditor.buttons.append($.fn.ueditor.buttons.refreshFontSet);




    //  提供外部调用公共方法 $.easyui.showUeditorDialog 用于快速创建 Ueditor 富文本 HTML 编辑器对话框；
    //  该方法的参数 options 参考继承于方法 $.easyui.showDialog 和插件 easyui-ueditor 的参数格式，在在此基础上增加了如下描述的属性：
    //      title   : string
    //      value   : string
    //      onEnter : function(val, eueditor, editor, btn)
    //      onCancel: function(val, eueditor, editor, btn)
    //      ... 
    //  返回值：返回创建的 easyui-dialog 对象；
    $.easyui.showUeditorDialog = function (options) {
        if (options && options.topMost && $ != $.util.$ && $.util.$.easyui.showUeditorDialog) {
            return $.util.$.easyui.showUeditorDialog.apply(this, options);
        }
        var opts = $.extend({}, $.easyui.showUeditorDialog.defaults, options || {}),
            ueditor, editor, value = opts.value,
            dia = $.easyui.showDialog($.extend({}, opts, {
                content: "<div class=\"ueditor-dialog-container\"></div>",
                enableApplyButton: false, topMost: false,
                onDestroy: function () { if (ueditor) { ueditor.ueditor("destroy"); } },
                onResize: function () {
                    var ret = $.fn.dialog.defaults.onResize.apply(this, arguments);
                    if ($.isFunction(opts.onResize)) { opts.onResize.apply(this, arguments); }
                    return ret;
                },
                onSave: function () { return opts.onEnter.call(dia, value, ueditor, editor, this); },
                onClose: function () {
                    if (editor && editor.fontset) { editor.fontset.dialog("close"); }
                    return opts.onCancel.call(dia, value, ueditor, editor, this);
                },
                onMove: function () {
                    var ret = $.fn.dialog.defaults.onMove.apply(this, arguments);
                    if (editor && editor.fontset && !editor.fontset.dialog("options").closed) {
                        var offset = $(editor.container).offset();
                        editor.fontset.dialog("open").dialog("move", { top: offset.top - 50, left: offset.left })
                    }
                    return ret;
                },
                onBeforeDestroy: function () {
                    if (editor && editor.fontset) { editor.fontset.dialog("destroy"); }
                }
            }));
        $.util.exec(function () {
            var container = dia.find(".ueditor-dialog-container").ueditor($.extend({}, opts, {
                fit: true, border: false, value: value, autosave: false, enableAutoSave: false,
                onResize: $.fn.ueditor.defaults.onResize,
                oncontentChange: function () {
                    value = $(this).ueditor("options").value;
                    if ($.isFunction(opts.oncontentChange)) { opts.oncontentChange.apply(this, arguments); }
                }
            }));
            dia.ueditor = ueditor = container;
            dia.editor = editor = container.ueditor("editor");
        });
        return dia;
    };


    $.easyui.showUeditorDialog.defaults = {
        title: "Html 编辑器窗口",
        iconCls: "icon-standard-application-form-edit",
        width: 665, height: 480, minWidth: 400, minHeight: 240,
        maximizable: true,
        collapsible: true,
        value: null, zIndex: 20000, focus: true, template: "normal",
        onEnter: function (val, editor, btn) { },
        onCancel: function (val, editor, btn) { }
    };







    $.fn.ueditor.methods = {

        options: function (jq) { return getOptions(jq[0]); },

        panel: function (jq) { return $.data(jq[0], "ueditor").panel; },

        editor: function (jq) { return $.data(jq[0], "ueditor").editor; },

        textarea: function (jq) { return $.data(jq[0], "ueditor").textarea; },

        resize: function (jq, size) { return jq.each(function () { resize(this, size); }); },


        //  销毁编辑器实例
        destroy: function (jq) { return jq.each(function () { destroy(this); }); },

        //  同步数据到编辑器所在的form 从编辑器的容器节点向上查找form元素
        //      若找到，就同步编辑内容到找到的form里，为提交数据做准备，主要用于是手动提交的情况 后台取得数据的键值，使用你容器上的name属性
        //      如果没有就使用参数里的textarea项
        sync: function (jq, formId) { return jq.each(function () { sync(this, formId); }); },

        //  为编辑器的编辑命令提供快捷键 这个接口是为插件扩展提供的接口,主要是为新添加的插件，如果需要添加快捷键，所提供的接口
        //  该方法的参数 param 表示命令名和快捷键键值对对象，多个按钮的快捷键用“＋”分隔，格式如 { "Bold" : "ctrl+66", "Italic" : "ctrl+73" }
        addshortcutkey: function (jq, param) { return jq.each(function () { return jq.each(function () { addshortcutkey(this, param); }); }); },

        //  获取编辑器的内容。 可以通过参数定义编辑器内置的判空规则
        getContent: function (jq, filter) { return getContent(jq[0], filter); },

        //  取得完整的html代码，可以直接显示成完整的html文档
        getAllHtml: function (jq) { return getAllHtml(jq[0]); },

        //  得到编辑器的纯文本内容，但会保留段落格式
        getPlainTxt: function (jq) { return getPlainTxt(jq[0]); },

        //  获取编辑器中的纯文本内容,没有段落格式
        getContentTxt: function (jq) { return getContentTxt(jq[0]); },

        //  设置编辑器的内容，可修改编辑器当前的html内容；该方法的参数 param 可以为以下类型；
        //      String：value
        //      Object：{ value, isAppendTo }
        setContent: function (jq, param) { return jq.each(function () { setContent(this, param); }); },

        //  让编辑器获得焦点，默认focus到编辑器头部，toEnd确定focus位置
        focus: function (jq, toEnd) { return jq.each(function () { setFocus(this, toEnd); }); },

        //  执行编辑命令cmdName，完成富文本编辑效果
        execCommand: function (jq, cmdName) { return execCommand(jq[0], cmdName); },

        //  根据传入的command命令，查选编辑器当前的选区，根据命令返回相关的值
        queryCommandState: function (jq, cmdName) { return queryCommandState(jq[0], cmdName); },

        //  根据传入的command命令，查选编辑器当前的选区，根据命令返回相关的值
        queryCommandValue: function (jq, cmdName) { return queryCommandValue(jq[0], cmdName); },

        //  检查编辑区域中是否有内容，若包含参数tags中的节点类型，直接返回true
        hasContents: function (jq, tags) { return hasContents(jq[0], tags); },

        //  重置编辑器，可用来做多个tab使用同一个编辑器实例
        reset: function (jq) { return jq.each(function () { reset(this); }); },

        //  设置当前编辑区域可以编辑,except中的命令除外
        setEnabled: function (jq, except) { return jq.each(function () { setEditorDisabled(this, false, except); }); },

        //  设置当前编辑区域不可编辑,except中的命令除外
        setDisabled: function (jq, except) { return jq.each(function () { setEditorDisabled(this, true, except); }); },

        //  显示编辑器
        setShow: function (jq) { return jq.each(function () { showEditor(this); }); },

        //  隐藏编辑器
        setHide: function (jq) { return jq.each(function () { hideEditor(this); }); },

        //  根据指定的路径，获取对应的语言资源
        getLang: function (jq, path) { return getLang(jq[0], path); },

        //  计算编辑器html内容或纯文本字符串的长度
        getContentLength: function (jq, ingoneHtml) { return getContentLength(jq[0], ingoneHtml); },

        //  注册输入过滤规则
        addInputRule: function (jq, rule) { return jq.each(function () { addInputRule(this, rule); }); },

        //  注册输出过滤规则
        addOutputRule: function (jq, rule) { return jq.each(function () { addOutputRule(this, rule); }); },


        //  同 setShow 方法
        show: function (jq) { return jq.each(function () { showEditor(this); }); },

        //  同 setHide 方法
        hide: function (jq) { return jq.each(function () { hideEditor(this); }); },

        //  同 setEnabled 方法
        enable: function (jq, except) { return jq.each(function () { setEditorDisabled(this, false, except); }); },

        //  同 setDisabled 方法
        disable: function (jq, except) { return jq.each(function () { setEditorDisabled(this, true, except); }); },

        //  参数 param 同 getContent 的参数 filter。
        getValue: function (jq, param) { return getValue(jq[0], param); },

        //
        setValue: function (jq, value) { return jq.each(function () { setValue(this, value); }); },

        //
        append: function (jq, value) { return jq.each(function () { setValue(this, value, true); }); },

        //  同 focus 方法；
        setFocus: function (jq, toEnd) { return jq.each(function () { setFocus(this, toEnd); }); },

        //  清空文档
        clear: function (jq) { return jq.each(function () { clear(this); }); },


        validate: function (jq) { return jq.each(function () { validate(this); }); },

        isValid: function (jq) { return validate(jq[0]); },

        enableValidation: function (jq) { return jq.each(function () { setValidation(this, false) }); },

        disableValidation: function (jq) { return jq.each(function () { setValidation(this, true) }); }
    };

    $.fn.ueditor.defaults = {

        //  工具栏上的所有的功能按钮和下拉框，可以在new编辑器的实例时选择自己需要的从新定义
        toolbars: $.fn.ueditor.toolbars,
        //  语言配置项,默认是zh-cn
        lang: "zh-cn",
        //  是否启用自动保存
        enableAutoSave: true,
        //  自动保存间隔时间， 单位ms
        saveInterval: 500,
        //  主题配置项,默认是default。
        theme: "default",
        //  针对getAllHtml方法，会在对应的head标签中增加该编码设置。
        charset: "utf-8",
        //  常用配置项目
        isShow: true,
        //  初始化编辑器的内容,也可以通过textarea/script给值，看官网例子
        initialContent: "",
        //  提交表单时，服务器获取编辑器提交内容的所用的参数
        textarea: "editorValue",
        //  初始化时，是否让编辑器获得焦点true或false
        focus: false,
        //  是否开启初始化时即全屏，默认为 false
        fullscreen: false,
        //  编辑器初始化结束后,编辑区域是否是只读的，默认是false
        readonly: false,
        //  编辑器层级的基数,默认是900
        zIndex: 900,
        //  图片操作的浮层开关，默认打开
        imagePopup: true,
        //  是否开启表情本地化，默认关闭。若要开启请确保emotion文件夹下包含官网提供的images表情文件夹
        emotionLocalization: false,
        //  是否默认为纯文本粘贴。false为不使用纯文本粘贴，true为使用纯文本粘贴
        pasteplain: false,
        //  字号列表
        fontsize: [10, 11, 12, 14, 16, 18, 20, 24, 36],
        //  段落格式列表，值留空时支持多语言自动识别，若配置，则以配置值为准
        paragraph: { p: "", h1: "", h2: "", h3: "", h4: "", h5: "", h6: "" },
        //  段前间距 值和显示的名字相同
        rowspacingtop: ['5', '10', '15', '20', '25'],
        //  段后间距 值和显示的名字相同
        rowspacingbottom: ['5', '10', '15', '20', '25'],
        //行内间距 值和显示的名字相同
        lineheight: ['1', '1.5', '1.75', '2', '3', '4', '5'],
        //快捷菜单，即选中部分文字后浮现出的快捷菜单内容
        shortcutMenu: ["fontfamily", "fontsize", "bold", "italic", "underline", "forecolor", "backcolor", "insertorderedlist", "insertunorderedlist"],
        //  是否开启字数统计
        wordCount: true,
        //  允许的最大字符数
        maximumWords: 10000,
        //  字数统计提示，{#count}代表当前字数，{#leave}代表还可以输入多少字符数,留空支持多语言自动切换，否则按此配置显示
        wordCountMsg: "当前已输入 {#count} 个字符，您还可以输入{#leave} 个字符",
        //  点击tab键时移动的距离,tabSize倍数
        tabSize: 4,
        //  tabNode什么字符做为单位
        tabNode: "&nbsp;",
        //  是否启用元素路径，默认是显示
        elementPathEnabled: true,
        //  是否自动长高,默认true
        autoHeightEnabled: true,
        //  是否可以拉伸长高,默认false
        scaleEnabled: false,
        //  表格是否可以拖拽
        tableDragable: true,
        //  源码的查看方式,codemirror 是代码高亮，textarea是文本框,默认是codemirror
        sourceEditor: "codemirror",
        //  编辑器初始化完成后是否进入源码模式，默认为 false
        sourceEditorFirst: false,

        //  开关右键菜单，默认为true
        enableContextMenu: true,

        //  指定产出的列表中是否嵌套P标签，默认是false
        disablePInList: false,

        //  指定粘贴时是否是只保留标签模式，默认是false
        retainOnlyLabelPasted: false,

        //  指定编辑器是否启用自动保存草稿功能，默认是true
        autosave: true,


        //  编辑器准备就绪后会触发该事件
        onready: function () { },

        //  执行destroy方法,会触发该事件
        ondestroy: function () { },

        //  执行reset方法,会触发该事件
        onreset: function () { },

        //  执行focus方法,会触发该事件
        onfocus: function () { },

        //  语言加载完成会触发该事件
        onlangReady: function () { },

        //  运行命令之后会触发该命令
        onbeforeExecCommand: function () { },

        //  运行命令之后会触发该命令
        onafterExecCommand: function () { },

        //  运行命令之前会触发该命令
        onfirstBeforeExecCommand: function () { },

        //  在getContent方法执行之前会触发该事件
        onbeforeGetContent: function () { },

        //  在getContent方法执行之后会触发该事件
        onafterGetContent: function () { },

        //  在getAllHtml方法执行时会触发该事件
        ongetAllHtml: function () { },

        //  在setContent方法执行之前会触发该事件
        onbeforeSetContent: function () { },

        //  在setContent方法执行之后会触发该事件
        onafterSetContent: function () { },

        //  每当编辑器内部选区发生改变时，将触发该事件
        onselectionchange: function () { },

        //  在所有selectionchange的监听函数执行之前，会触发该事件
        onbeforeSelectionChange: function () { },

        //  在所有selectionchange的监听函数执行完之后，会触发该事件
        onafterSelectionChange: function () { },

        //  编辑器内容发生改变时会触发该事件
        oncontentChange: function () { },

        //  当调用 setHeight 方法重置编辑器高度时触发
        onsetHeight: function () { }
    };

    $.extend($.fn.ueditor.defaults, {

        fit: false,

        border: true,

        width: 600,

        height: 150,
        //  同 initialContent，表示初始化编辑器的内容；不过其优先级高于 initialContent 和 textarea/script 的 innerText；
        //  当设置了该属性后，初始化编辑器的内容将被强制设为该属性值，而无论是否设置了 initialContent 或 textarea/script 的 innerText；
        value: null,

        toolbarsTemplate: {
            //
            simple: [[
                'fontset', '|', 'forecolor', 'backcolor',
                //'fontfamily', 'fontsize', 'forecolor', 'backcolor', 'bold', 'italic', 'underline',
                '|', 'emotion', 'scrawl', '|', 'snapscreen', 'insertimage', 'attachment', '|', 'source'//, 'preview'
            ]],
            normal: [[
                'bold', 'italic', 'underline',
                '|', 'fontfamily', 'fontsize', 'forecolor', 'backcolor',
                '|', 'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify',
                '|', 'insertorderedlist', 'insertunorderedlist',
                '|', 'indent', 'blockquote', 'link', 'formatmatch',
                '|', 'source'//, 'preview'
            ]],
            //
            rich: [[]],
            full: $.fn.ueditor.toolbars
        },

        template: "normal",
        //  getValue 方法所使用的内部取值方法
        valueMethod: "getContent",
        //  编辑器初始化完成后是否立即将其执行 setDisabled 操作使其禁用；
        disabled: false,


        codeMirrorJsUrl: undefined,
        codeMirrorCssUrl: undefined,
        autoHeightEnabled: false,
        scaleEnabled: false,


        required: false,
        missingMessage: "该编辑框不能为空.",
        //  "left", "right", "top", "bottom"
        tipPosition: "right",
        deltaX: 0,
        tipOptions: $.fn.validatebox.defaults.tipOptions,
        novalidate: false,


        //  当调整编辑器大小时，触发该事件
        onResize: function (width, height) { }
    });


    $.parser.plugins.push("ueditor");

    if ($.fn.form && $.isArray($.fn.form.otherList)) {
        $.fn.form.otherList.push("ueditor");
        //$.array.insert($.fn.form.otherList, 0, "ueditor");
    }

})(jQuery);