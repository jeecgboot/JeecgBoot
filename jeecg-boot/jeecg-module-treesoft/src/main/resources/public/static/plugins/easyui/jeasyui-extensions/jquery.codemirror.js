/**
* jQuery EasyUI 1.3.6
* Copyright (c) 2009-2014 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL or commercial licenses
* To use it on other terms please contact author: info@jeasyui.com
* http://www.gnu.org/licenses/gpl.txt
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI CodeMirror Plugin Extensions 1.0 beta
* jQuery EasyUI CodeMirror 插件扩展
* jquery.codemirror.js
*
* 依赖项：
*   1、jquery.jdirk.js v1.0 beta late
*   2、jeasyui.extensions.js v1.0 beta late

*   3、codemirror-4.3/lib/codemirror.css
*   4、codemirror-4.3/lib/codemirror.js
*   5、codemirror-4.3/mode/xml/xml.js
*   6、codemirror-4.3/mode/javascript/javascript.js
*   7、codemirror-4.3/mode/vbscript/vbscript.js
*   8、codemirror-4.3/mode/css/css.js
*   9、codemirror-4.3/mode/htmlmixed/htmlmixed.js
*
* Copyright (c) 2013-2014 ChenJianwei personal All rights reserved.
* http://www.chenjianwei.org
*/
(function ($, undefined) {

    function create(target) {
        var t = $(target).addClass("codemirror-f"),
            isDiv = /^(?:div)$/i.test(target.nodeName), isText = /^(?:textarea)$/i.test(target.nodeName),
            html = isText ? (t.val() || t.text()) : t.html(),
            state = $.data(target, "codemirror"), opts = state.options,
            name = t.attr("name"), id = state.codemirrorId = "codemirror_" + $.util.guid("N"),
            panel = state.panel = isDiv ? t.addClass("codemirror-panel").empty() : $("<div class=\"codemirror-panel\"></div>").insertAfter(t.hide().empty()).append(t),
            panelId = state.panelId = panel.attr("id"),
            textarea = state.textarea = $("<textarea id=\"" + id + "\"" + (name ? " name=\"" + name + "\"" : "") + "></textarea>").appendTo(panel);
        if (name) { t.attr("codemirrorName", name).removeAttr("name"); }

        opts.originalValue = html || opts.value;
        if (opts.originalValue) { textarea.val(opts.originalValue); }

        if (!panelId) { panel.attr("id", panelId = state.panelId = "codemirror_panel_" + $.util.guid("N")); }
        if (opts.fontSize || opts.lineHeight) {
            state.style = $.util.addCss("#" + panelId + " .CodeMirror { " +
                (opts.fontSize ? "font-size: " + opts.fontSize + ";" : "") +
                (opts.lineHeight ? "line-height: " + opts.lineHeight + ";" : "") +
                " }").appendTo(panel);
        }

        var param = $.extend({}, opts);
        if (opts.disabled) { param.readOnly = "nocursor"; }
        state.cm = CodeMirror.fromTextArea(textarea[0], param);
        state.doc = state.cm.getDoc();

        panel.panel($.extend({}, opts, {
            onResize: function (width, height) {
                $.fn.panel.defaults.onResize.apply(this, arguments);
                $.extend(opts, { width: width, height: height });
                var size = textarea._fit();
                state.cm.setSize(size.width, size.height);
                if ($.isFunction(opts.onResize)) { opts.onResize.apply(target, arguments); }
            },
            loader: $.fn.panel.defaults.loader,
            id: panelId
        }));

        state.wrapper = panel.panel("body").addClass("codemirror-wrapper");

        initialEvents(target);
        setDisabled(target, opts.disabled);
        setValidation(target);
    };


    function initialEvents(target) {
        var state = $.data(target, "codemirror"), opts = state.options;
        $.each($.fn.codemirror.events, function (i, n) {
            var eventName = "on" + n, e = opts[eventName];
            state.cm.on(n, function () {
                if (n == "change") {
                    var val = opts.value = $(target).codemirror("getValue");
                    //state.textarea.text(val);
                    state.textarea.val(val);
                }
                if ($.isFunction(e)) { return e.apply(target, arguments); }
            });
        });
    };



    function getOptions(target) {
        var state = $.data(target, "codemirror"), opts = state.panel.panel("options");
        return $.extend(state.options, {
            title: opts.title, iconCls: opts.iconCls, width: opts.width, height: opts.height, left: opts.left, top: opts.top,
            cls: opts.cls, headerCls: opts.headerCls, bodyCls: opts.bodyCls, style: opts.style, fit: opts.fit, border: opts.border,
            doSize: opts.doSize, noheader: opts.noheader, collapsible: opts.collapsible, minimizable: opts.minimizable, maximizable: opts.maximizable,
            closable: opts.closable, collapsed: opts.collapsed, minimized: opts.minimized, maximized: opts.maximized, closed: opts.closed
        });
    };

    function resize(target, param) {
        var state = $.data(target, "codemirror");
        return state.panel.panel("resize", param);
    };

    function destroy(target) {
        var t = $(target), state = $.data(target, "codemirror");
        state.cm.toTextArea();
        state.panel.panel("destroy");
        if (state.style) { state.style.remove(); }
        t.remove();
    };

    function show(target) {
        var state = $.data(target, "codemirror");
        state.panel.panel("open");
        return $(target).codemirror("options");
    };

    function hide(target) {
        var state = $.data(target, "codemirror");
        state.panel.panel("close");
        return $(target).codemirror("options");
    };


    function setDisabled(target, disabled) {
        var state = $.data(target, "codemirror"), opts = state.options, d = disabled ? true : false;
        state.panel[d ? "addClass" : "removeClass"]("codemirror-disabled");
        state.cm.setOption("disabled", d);
        state.cm.setOption("readOnly", d);
        opts.readOnly = d;
        opts.disabled = d;
    };

    function getValue(target, separator) {
        var state = $.data(target, "codemirror");
        return state.doc.getValue(separator);
    };

    function setValue(target, value) {
        var state = $.data(target, "codemirror");
        return state.doc.setValue(value);
    };

    function reset(target) {
        var state = $.data(target, "codemirror"), opts = state.options;
        return state.doc.setValue(opts.originalValue);
    };

    function focus(target) {
        var state = $.data(target, "codemirror");
        return state.cm.focus();
    };

    function clear(target) {
        var state = $.data(target, "codemirror");
        return state.doc.setValue("");
    };



    function getRange(target, param) {
        if (!param || !param.from || !param.to) { return; }
        var state = $.data(target, "codemirror");
        return param.separator ? state.doc.getRange(param.from, param.to, param.separator) : state.doc.getRange(param.from, param.to);
    };

    function replaceRange(target, param) {
        if (!param || !param.replacement || !param.from || !param.to) { return; }
        var state = $.data(target, "codemirror");
        return param.origin ? state.doc.replaceRange(param.replacement, param.from, param.to, param.origin) : state.doc.replaceRange(param.replacement, param.from, param.to);
    };

    function getLine(target, index) {
        return $.data(target, "codemirror").doc.getLine(index);
    };

    function lineCount(target) {
        return $.data(target, "codemirror").doc.lineCount();
    };

    function firstline(target) {
        return $.data(target, "codemirror").doc.firstline();
    };

    function lastLine(target) {
        return $.data(target, "codemirror").doc.lastLine();
    };

    function getLineHandle(target, index) {
        return $.data(target, "codemirror").doc.getLineHandle(index);
    };

    function getLineNumber(target, handle) {
        return $.data(target, "codemirror").doc.getLineNumber(handle);
    };

    function eachLine(target, param) {
        if (!param) { return; }
        var state = $.data(target, "codemirror"), isFunc = $.isFunction(param);
        return isFunc ? state.doc.eachLine(param) : state.doc.eachLine(param.start, param.end, param.f);
    };

    function markClean(target) {
        return $.data(target, "codemirror").doc.markClean();
    };

    function changeGeneration(target, closeEvent) {
        var state = $.data(target, "codemirror");
        closeEvent = closeEvent ? true : false;
        return state.doc.changeGeneration(closeEvent);
    };

    function isClean(target, generation) {
        var state = $.data(target, "codemirror"), isNumber = $.isNumeric(generation);
        return isNumber ? state.doc.isClean(generation) : state.doc.isClean();
    };

    function getSelection(target, lineSep) {
        var state = $.data(target, "codemirror");
        return lineSep ? state.doc.getSelection(lineSep) : state.doc.getSelection();
    };

    function getSelections(target, lineSep) {
        var state = $.data(target, "codemirror");
        return lineSep ? state.doc.getSelections(lineSep) : state.doc.getSelections();
    };

    function replaceSelection(replacement, select) {
        if (!param || !param.replacement) { return; }
        var state = $.data(target, "codemirror");
        return param.select ? state.doc.replaceSelection(param.replacement, param.select) : state.doc.replaceSelection(param.replacement);
    };

    function replaceSelections(replacement, select) {
        if (!param || !param.replacements) { return; }
        var state = $.data(target, "codemirror");
        return param.select ? state.doc.replaceSelections(param.replacements, param.select) : state.doc.replaceSelections(param.replacements);
    };

    function getCursor(target, start) {
        var state = $.data(target, "codemirror");
        return start ? state.doc.getCursor(start) : state.doc.getCursor();
    };

    function listSelections(target) {
        return $.data(target, "codemirror").doc.listSelections();
    };

    function somethingSelected(target) {
        return $.data(target, "codemirror").doc.somethingSelected();
    };

    function setCursor(target, param) {
        if (!param || !param.pos) { return; }
        var state = $.data(target, "codemirror");
        return state.doc.setCursor(param.pos, param.ch, param.options);
    };

    function setSelection(target, param) {
        if (!param || !param.anchor) { return; }
        var state = $.data(target, "codemirror");
        return state.doc.setSelection(param.anchor, param.head, param.options);
    };

    function setSelections(target, param) {
        if (!param || !param.ranges) { return; }
        var state = $.data(target, "codemirror");
        return state.doc.setSelections(param.ranges, param.primary, param.options);
    };

    function addSelection(target, param) {
        if (!param || !param.anchor) { return; }
        var state = $.data(target, "codemirror");
        return state.doc.addSelection(param.anchor, param.head);
    };

    function extendSelection(target, param) {
        if (!param || !param.from) { return; }
        var state = $.data(target, "codemirror");
        return state.doc.extendSelection(param.from, param.to, param.options);
    };

    function extendSelections(target, param) {
        if (!param || !param.heads) { return; }
        var state = $.data(target, "codemirror");
        return state.doc.extendSelections(param.heads, param.options);
    };

    function extendSelectionsBy(target, param) {
        if (!param || !$.isFunction(param.f)) { return; }
        var state = $.data(target, "codemirror");
        return state.doc.extendSelectionsBy(param.f, param.options);
    };

    function setExtending(target, value) {
        return $.data(target, "codemirror").doc.setExtending(value);
    };

    function getExtending(target) {
        return $.data(target, "codemirror").doc.getExtending();
    };

    function hasFocus(target) {
        return $.data(target, "codemirror").cm.hasFocus();
    };

    function findPosH(target, param) {
        if (!param || !param.start || !param.amount || !param.unit || !param.visually) { return; }
        var state = $.data(target, "codemirror");
        return state.cm.findPosH(param.start, param.amount, param.unit, param.visually);
    };

    function findPosV(target, param) {
        if (!param || !param.start || !param.amount || !param.unit) { return; }
        var state = $.data(target, "codemirror");
        return state.cm.findPosV(param.start, param.amount, param.unit);
    };




    function setOption(target, param) {
        if (!param || !param.option) { return; }
        var state = $.data(target, "codemirror");
        return state.cm.setOption(param.option, param.value);
    }

    function getOption(target, option) {
        if (!option) { return; }
        return $.data(target, "codemirror").cm.getOption(option);
    };

    function addKeyMap(target, param) {
        if (!param || !param.map) { return; }
        return $.data(target, "codemirror").cm.addKeyMap(param.map, param.bottom);
    };

    function removeKeyMap(target, map) {
        return $.data(target, "codemirror").cm.removeKeyMap(map);
    };

    function addOverlay(target, param) {
        if (!param || !param.mode) { return; }
        return $.data(target, "codemirror").cm.addOverlay(param.mode, param.options);
    };

    function removeOverlay(target, mode) {
        return $.data(target, "codemirror").cm.removeOverlay(mode);
    };

    function on(target, param) {
        if (!param || !param.type || !param.func) { return; }
        return $.data(target, "codemirror").cm.on(param.type, param.func);
    };

    function off(target, param) {
        if (!param || !param.type || !param.func) { return; }
        return $.data(target, "codemirror").cm.off(param.type, param.func);
    };




    function swapDoc(target, doc) {
        if (!doc) { return; }
        return $.data(target, "codemirror").cm.swapDoc(doc);
    };

    function copy(target, copyHistory) {
        return $.data(target, "codemirror").doc.target(copyHistory);
    };

    function linkedDoc(target, param) {
        return $.data(target, "codemirror").doc.linkedDoc(param);
    };

    function unlinkDoc(target, doc) {
        return $.data(target, "codemirror").doc.unlinkDoc(doc);
    };

    function iterLinkedDocs(target, f) {
        if (!$.isFunction(f)) { return; }
        return $.data(target, "codemirror").doc.iterLinkedDocs(f);
    };



    function undo(target) { return $.data(target, "codemirror").doc.undo(); };

    function redo(target) { return $.data(target, "codemirror").doc.redo(); };

    function undoSelection(target) { return $.data(target, "codemirror").doc.undoSelection(); };

    function redoSelection(target) { return $.data(target, "codemirror").doc.redoSelection(); };

    function historySize(target) { return $.data(target, "codemirror").doc.historySize(); };

    function clearHistory(target) { return $.data(target, "codemirror").doc.clearHistory(); };

    function getHistory(target) { return $.data(target, "codemirror").doc.getHistory(); };

    function setHistory(target, history) { return $.data(target, "codemirror").doc.setHistory(history); };




    function markText(target, param) {
        if (!param || !param.from || !param.to) { return; }
        return param.options ? $.data(target, "codemirror").doc.markText(param.from, param.to, param.options) : $.data(target, "codemirror").doc.markText(param.from, param.to);
    };

    function setBookmark(target, param) {
        if (!param || !param.pos) { return; }
        return param.options ? $.data(target, "codemirror").doc.setBookmark(param.pos, param.options) : $.data(target, "codemirror").doc.setBookmark(param.pos);
    };

    function findMarks(target, param) {
        if (!param || !param.from || !param.to) { return; }
        return $.data(target, "codemirror").doc.findMarks(param.from, param.to);
    };

    function findMarksAt(target, pos) {
        return $.data(target, "codemirror").doc.findMarksAt(pos);
    };

    function getAllMarks(target) {
        return $.data(target, "codemirror").doc.getAllMarks();
    };




    function setGutterMarker(target, param) {
        if (!param || !param.line || !param.gutterID || !param.value) { return; }
        return $.data(target, "codemirror").cm.setGutterMarker(param.line, param.gutterID, param.value);
    };

    function clearGutter(target, gutterID) {
        return $.data(target, "codemirror").cm.clearGutter(gutterID);
    };

    function addLineClass(target, param) {
        if (!param || !param.line || !param.where || !param.cls) { return; }
        return $.data(target, "codemirror").cm.addLineClass(param.line, param.where, param.cls);
    };

    function removeLineClass(target, param) {
        if (!param || !param.line || !param.where || !param.cls) { return; }
        return $.data(target, "codemirror").cm.removeLineClass(param.line, param.where, param.cls);
    };

    function lineInfo(target, line) {
        return $.data(target, "codemirror").cm.lineInfo(line);
    };

    function addWidget(target, param) {
        if (!param || !param.pos || !param.node || !param.scrollIntoView) { return; }
        return $.data(target, "codemirror").cm.addWidget(param.pos, param.node, param.scrollIntoView);
    };

    function addLineWidget(target, param) {
        if (!param || !param.line || !param.node) { return; }
        return param.options ? $.data(target, "codemirror").cm.addLineWidget(param.line, param.node, param.options) : $.data(target, "codemirror").cm.addLineWidget(param.line, param.node);
    };




    function scrollTo(target, param) {
        if (!param || !param.width || !param.height) { return; }
        return $.data(target, "codemirror").cm.scrollTo(param.width, param.height);
    };

    function getScrollInfo(target) {
        return $.data(target, "codemirror").cm.getScrollInfo();
    };

    function scrollIntoView(target, param) {
        if (!param || !param.what || !param.height) { return; }
        return $.data(target, "codemirror").cm.scrollIntoView(param.what, param.margin);
    };

    function cursorCoords(target, param) {
        if (!param || !param.where || !param.mode) { return; }
        return $.data(target, "codemirror").cm.cursorCoords(param.where, param.mode);
    };

    function charCoords(target, param) {
        if (!param || !param.pos) { return; }
        return param.mode ? $.data(target, "codemirror").cm.charCoords(param.pos, param.mode) : $.data(target, "codemirror").cm.charCoords(param.pos);
    };

    function coordsChar(target, param) {
        if (!param || !param.line) { return; }
        return param.mode ? $.data(target, "codemirror").cm.coordsChar(param.line, param.mode) : $.data(target, "codemirror").cm.coordsChar();
    };

    function lineAtHeight(target, param) {
        if (!param || !param.object) { return; }
        return param.mode ? $.data(target, "codemirror").cm.lineAtHeight(param.object, param.mode) : $.data(target, "codemirror").cm.lineAtHeight(param.object);
    };

    function heightAtLine(target, param) {
        if (!param || !param.height) { return; }
        return param.mode ? $.data(target, "codemirror").cm.heightAtLine(param.height, param.mode) : $.data(target, "codemirror").cm.heightAtLine(param.height);
    };

    function defaultTextHeight(target) {
        return $.data(target, "codemirror").cm.defaultTextHeight();
    };

    function defaultCharWidth(target) {
        return $.data(target, "codemirror").cm.defaultCharWidth();
    };

    function getViewport(target) {
        return $.data(target, "codemirror").cm.getViewport();
    };




    function getTokenAt(target, param) {
        if (!param || !param.pos) { return; }
        return $.data(target, "codemirror").doc.getTokenAt(param.pos, param.precise);
    };

    function getHelpers(target, param) {
        if (!param || !param.pos || !param.type) { return; }
        return $.data(target, "codemirror").doc.getHelpers(param.pos, param.type);
    };

    function getHelper(target, param) {
        if (!param || !param.pos || !param.type) { return; }
        return $.data(target, "codemirror").doc.getHelper(param.pos, param.type);
    };

    function getStateAfter(target, param) {
        return $.data(target, "codemirror").cm.getStateAfter(param.line, param.precise);
    };




    function indentLine(target, param) {
        if (!param || !param.line) { return; }
        return $.data(target, "codemirror").cm.indentLine(param.line, param.dir);
    };

    function toggleOverwrite(target, value) {
        return $.data(target, "codemirror").cm.toggleOverwrite(value);
    };

    function execCommand(target, name) {
        return $.data(target, "codemirror").cm.execCommand(name);
    };

    function posFromIndex(target, index) {
        return $.data(target, "codemirror").doc.posFromIndex(index);
    };

    function indexFromPos(target, param) {
        return $.data(target, "codemirror").doc.indexFromPos(param);
    };

    function getInputField(target) {
        return $.data(target, "codemirror").cm.getInputField();
    };

    function getWrapperElement(target) {
        return $.data(target, "codemirror").cm.getWrapperElement();
    };

    function getScrollerElement(target) {
        return $.data(target, "codemirror").cm.getScrollerElement();
    };

    function getGutterElement(target) {
        return $.data(target, "codemirror").cm.getGutterElement();
    };






    function hideTip(target) {
        var state = $.data(target, "codemirror");
        state.tip = false;
        state.wrapper.tooltip("hide");
    };

    function showTip(target) {
        var state = $.data(target, "codemirror"), opts = state.options;
        state.wrapper.tooltip($.extend({}, opts.tipOptions, { content: opts.missingMessage, position: opts.tipPosition, deltaX: opts.deltaX })).tooltip("show");
        state.tip = true;
    };

    function repositionTip(target) {
        var state = $.data(target, "codemirror");
        if (state && state.tip) {
            state.wrapper.tooltip("reposition");
        }
    };

    function initializeValidate(target) {
        var t = $(target), state = $.data(target, "codemirror"), opts = state.options;
        state.wrapper.unbind(".codemirror");
        if (opts.novalidate) {
            return;
        }
        state.wrapper.bind("focus.codemirror", function () {
            state.validating = true;
            state.value = undefined;
            (function () {
                if (state.validating) {
                    var value = t.codemirror("getValue");
                    if (state.value != value) {
                        state.value = value;
                        if (state.timer) {
                            clearTimeout(state.timer);
                        }
                        state.timer = setTimeout(function () { t.codemirror("validate"); }, opts.delay);
                    } else {
                        repositionTip(target);
                    }
                    setTimeout(arguments.callee, 200);
                }
            })();
        }).bind("blur.codemirror", function () {
            if (state.timer) {
                clearTimeout(state.timer);
                state.timer = undefined;
            }
            state.validating = false;
            hideTip(target);
        }).bind("mouseenter.codemirror", function () {
            if (state.wrapper.hasClass("codemirror-invalid")) {
                showTip(target);
            }
        }).bind("mouseleave.codemirror", function () {
            if (!state.validating) {
                hideTip(target);
            }
        });
    };

    function validate(target) {
        var t = $(target), state = $.data(target, "codemirror"), opts = state.options,
            value = t.codemirror("getValue");
        state.wrapper.removeClass("codemirror-invalid");
        hideTip(target);
        if (!opts.novalidate && opts.required && $.string.isNullOrWhiteSpace(value) && !t.is(":disabled") && !state.wrapper.hasClass("codemirror-disabled")) {
            state.wrapper.addClass("codemirror-invalid");
            if (state.validating) {
                showTip(target);
            }
            return false;
        }
        return true;
    };

    function setValidation(target, novalidate) {
        var state = $.data(target, "codemirror"), opts = state.options;
        if (novalidate != undefined && novalidate != null) {
            opts.novalidate = novalidate;
        }
        if (opts.novalidate) {
            state.wrapper.removeClass("codemirror-invalid");
            hideTip(target);
        }
        initializeValidate(target);
    };









    function loader(param, success, error) {
        var opts = $(this).codemirror("options");
        if (!opts.url) { return false; }
        $.ajax({
            type: opts.method, url: opts.url, data: param, dataType: "text",
            success: function (data) {
                success(data);
            }, error: function () {
                error.apply(this, arguments);
            }
        });
    };

    function ajaxRequest(target) {
        var state = $.data(target, "codemirror"), opts = state.options, param = $.extend({}, opts.queryParams);
        if (opts.url && opts.onBeforeLoad.call(target, param) != false) {
            opts.loader.call(target, param, function (data) {
                setValue(target, opts.loadFilter.call(target, data));
                opts.onLoad.apply(target, arguments);
            }, function () {
                opts.onLoadError.apply(this, arguments);
            });
        }
        state.cm.refresh();
    };

    function refresh(target, url) {
        var state = $.data(target, "codemirror"), opts = state.options;
        if (url) {
            if (typeof url == "string") {
                opts.url = url;
            } else {
                opts.queryParams = url;
            }
        }
        ajaxRequest(target);
    };



    $.fn.codemirror = function (options, param) {
        if (typeof options == "string") {
            var method = $.fn.codemirror.methods[options];
            if (method) {
                return method(this, param);
            } else {
                return this.codemirror("panel").panel(options, param);
            }
        }
        options = options || {};
        return this.each(function () {
            var state = $.data(this, "codemirror");
            if (state) {
                $.extend(state.options, options);
            } else {
                $.data(this, "codemirror", { options: $.extend({}, $.fn.codemirror.defaults, $.fn.codemirror.parseOptions(this), options) });
                create(this);
            }
            ajaxRequest(this);
        });
    };




    //  var cm = CodeMirror.fromTextArea("#t1");
    $.fn.codemirror.events = [
        "change", "changes", "beforeChange", "cursorActivity", "keyHandled", "inputRead", "beforeSelectionChange", "viewportChange", "swapDoc",
        "gutterClick", "gutterContextMenu", "focus", "blur", "scroll", "update", "renderLine",
        "mousedown", "dblclick", "contextmenu", "keydown", "keypress", "keyup", "dragstart", "dragenter", "dragover", "drop"
    ];
    /*
    $.fn.codemirror.properties = [
        //string
        "width", "height", "value", "mode", "theme", "keyMap",
        //boolean
        "fit", "disabled", "smartIndent", "indentWithTabs", "electricChars", "rtlMoveVisually", "lineWrapping", "lineNumbers", "fixedGutter", "coverGutterNextToScrollbar",
        "readOnly", "showCursorWhenSelecting", "autofocus", "dragDrop", "resetSelectionOnContextMenu", "flattenSpans", "addModeClass",
        //number
        "indentUnit", "tabSize", "firstLineNumber", "undoDepth", "historyEventDelay", "tabindex", "cursorBlinkRate", "cursorScrollMargin", "cursorHeight", "workTime",
        "workDelay", "pollInterval", "maxHighlightLength", "viewportMargin"
    ];
    //  var doc = cm.getDoc();
    $.fn.codemirror.documentEvents = [
        "change", "beforeChange", "cursorActivity", "beforeSelectionChange"
    ];
    //  var handle = doc.getLineHandle(num: integer);
    $.fn.codemirror.lineHandlerEvents = [
        "delete", "change"
    ];
    // var mark = doc.setBookmark(pos: {line, ch}, ?options: object);
    $.fn.codemirror.markedRangHandlerEvents = [
        "beforeCursorEnter", "clear", "hide", "unhide"
    ];
    // var widget = cm.addLineWidget(line: integer|LineHandle, node: Element, ?options: object);
    $.fn.codemirror.lineWidgetEvents = [
        "redraw"
    ];
    $.fn.codemirror.docMethods = [
        "getValue", "setValue", "getRange", "replaceRange", "getLine", "lineCount", "firstLine", "lastLine", "getLineHandle",
        "getLineNumber", "eachLine", "markClean", "changeGeneration", "isClean", "getSelection", "getSelections", "replaceSelection", "replaceSelections",
        "getCursor", "listSelections", "somethingSelected", "setCursor", "setSelection", "setSelections", "addSelection", "extendSelection", "extendSelections",
        "extendSelectionsBy", "setExtending", "getExtending", "getEditor", "copy", "linkedDoc",
        "unlinkDoc", "iterLinkedDocs", "undo", "redo", "undoSelection", "redoSelection", "historySize", "clearHistory", "getHistory",
        "setHistory", "markText", "setBookmark", "findMarks", "findMarksAt", "getAllMarks", "getMode", "getModeAt", "posFromIndex",
        "indexFromPos",
    ];
    $.fn.codemirror.cmMethods = [
        "hasFocus", "findPosH", "findPosV", "setOption", "getOption", "addKeyMap", "removeKeyMap", "addOverlay", "removeOverlay",
        "on", "off", "getDoc", "swapDoc", "setGutterMarker", "clearGutter", "addLineClass", "removeLineClass", "lineInfo",
        "addWidget", "addLineWidget", "setSize", "scrollTo", "getScrollInfo", "scrollIntoView", "cursorCoords", "charCoords", "coordsChar",
        "lineAtHeight", "heightAtLine", "defaultTextHeight", "defaultCharWidth", "getViewport", "refresh", "getTokenAt", "getTokenTypeAt", "getHelpers",
        "getHelper", "getStateAfter", "operation", "indentLine", "toggleOverwrite", "execCommand", "focus", "getInputField",
        "getWrapperElement", "getScrollerElement", "getGutterElement"
    ];
    */

    $.fn.codemirror.parseOptions = function (target) {
        return $.extend({}, $.fn.panel.parseOptions(target), $.parser.parseOptions(target, [
            "width", "height", "value", "mode", "theme", "keyMap", "fontSize", "lineHeight", "url",
            "missingMessage", "tipPosition",
            {
                fit: "boolean", disabled: "boolean", smartIndent: "boolean", indentWithTabs: "boolean", electricChars: "boolean",
                rtlMoveVisually: "boolean", lineWrapping: "boolean", lineNumbers: "boolean", fixedGutter: "boolean",
                coverGutterNextToScrollbar: "boolean", readOnly: "boolean", showCursorWhenSelecting: "boolean", autofocus: "boolean",
                dragDrop: "boolean", resetSelectionOnContextMenu: "boolean", flattenSpans: "boolean", addModeClass: "boolean",
                required: "boolean", novalidate: "boolean"
            },
            {
                indentUnit: "number", tabSize: "number", firstLineNumber: "number", undoDepth: "number", historyEventDelay: "number",
                tabindex: "number", cursorBlinkRate: "number", cursorScrollMargin: "number", cursorHeight: "number", workTime: "number",
                workDelay: "number", pollInterval: "number", maxHighlightLength: "number", viewportMargin: "number",
                deltaX: "number"
            }
        ]));
    };


    $.fn.codemirror.methods = {

        options: function (jq) { return getOptions(jq[0]); },

        panel: function (jq) { return $.data(jq[0], "codemirror").panel; },

        textarea: function (jq) { return $.data(jq[0], "codemirror").textarea; },

        cm: function (jq) { return $.data(jq[0], "codemirror").cm; },

        doc: function (jq) { return $.data(jq[0], "codemirror").doc; },




        //  设置 CodeMirror 代码编辑器大小，参数 size 是一个格式为 { width: number, height: number } 的 JSON-Object。
        resize: function (jq, size) { return jq.each(function () { resize(this, size); }); },

        //  销毁编辑器实例
        destroy: function (jq) { return jq.each(function () { destroy(this); }); },

        //
        show: function (jq) { return jq.each(function () { show(this); }); },

        //
        hide: function (jq) { return jq.each(function () { hide(this); }); },

        //
        enable: function (jq) { return jq.each(function () { setDisabled(this, false); }); },

        //
        disable: function (jq) { return jq.each(function () { setDisabled(this, true); }); },

        //  
        getValue: function (jq, separator) { return getValue(jq[0], separator); },

        //
        setValue: function (jq, value) { return jq.each(function () { setValue(this, value); }); },

        //
        reset: function (jq) { return jq.each(function () { reset(this); }); },

        //
        focus: function (jq) { return jq.each(function () { focus(this); }); },

        //
        setFocus: function (jq) { return jq.each(function () { focus(this); }); },

        //  清空文档
        clear: function (jq) { return jq.each(function () { clear(this); }); },

        //
        refresh: function (jq, url) { return jq.each(function () { refresh(this, url); }); },

        //  param: { from: {line, ch}, to: {line, ch}, ?separator: string }
        getRange: function (jq, param) { return getRange(jq[0], param); },

        // param: { replacement: string, from: {line, ch}, to: {line, ch}, ?origin: string }
        replaceRange: function (jq, param) { return jq.each(function () { replaceRange(this, param); }); },

        getLine: function (jq, index) { return getLine(jq[0], index); },

        lineCount: function (jq) { return lineCount(jq[0]); },

        lastLine: function (jq) { return lastLine(jq[0]); },

        getLineHandle: function (jq, index) { return getLineHandle(jq[0], index); },

        getLineNumber: function (jq, handle) { return getLineNumber(jq[0], handle); },

        // param: function(line:LineHandler) | { start: integer, end: integer, f: (line: LineHandle) }
        eachLine: function (jq, param) { return jq.each(function () { eachLine(this, param); }); },

        markClean: function (jq) { return jq.each(function () { markClean(this); }); },

        changeGeneration: function (jq, closeEvent) { return changeGeneration(jq[0], closeEvent); },

        isClean: function (jq, generation) { return isClean(jq[0], generation); },

        getSelection: function (jq, lineSep) { return getSelection(jq[0], lineSep); },

        getSelections: function (jq, lineSep) { return getSelections(jq[0], lineSep); },

        // param: { replacement: string, ?select: string }
        replaceSelection: function (jq, param) { return jq.each(function () { replaceSelection(this, param); }); },

        // param: { replacements: array<string>, ?select: string }
        replaceSelections: function (jq, param) { return jq.each(function () { replaceSelections(this, param); }); },

        //  ?start: string
        //  return: {line, ch}
        getCursor: function (jq, start) { return getCursor(jq[0], start); },

        //  return: array<{ anchor, head }>
        listSelections: function (jq) { return listSelections(jq[0]); },

        somethingSelected: function (jq) { return somethingSelected(jq[0]); },

        //  param: { line, ch}|number, ?ch: number, ?options: object }
        setCursor: function (jq, param) { return jq.each(function () { setCursor(this, param); }); },

        //  param: { anchor: {line, ch}, ?head: {line, ch}, ?options: object }
        setSelection: function (jq, param) { return jq.each(function () { setSelection(this, param); }); },

        //  param: { ranges: array<{anchor, head}>, ?primary: integer, ?options: object }
        setSelections: function (jq, param) { return jq.each(function () { setSelections(this, param); }); },

        //  param: { anchor: {line, ch}, ?head: {line, ch} }
        addSelection: function (jq, param) { return jq.each(function () { addSelection(this, param); }); },

        //  param: { from: {line, ch}, ?to: {line, ch}, ?options: object }
        extendSelection: function (jq, param) { return jq.each(function () { extendSelection(this, param); }); },

        //  param: { heads: array<{line, ch}>, ?options: object }
        extendSelections: function (jq, param) { return jq.each(function () { extendSelections(this, param); }); },

        //  param: { f: function(range: {anchor, head}) → {anchor, head}), ?options: object }
        extendSelectionsBy: function (jq, param) { return jq.each(function () { extendSelectionsBy(this, param); }); },

        setExtending: function (jq, value) { return jq.each(function () { setExtending(this, value); }); },

        //  return: boolean
        getExtending: function (jq) { return getExtending(jq[0]); },

        //  return: boolean
        hasFocus: function (jq) { return hasFocus(jq[0]); },

        //  param: { start: {line, ch}, amount: integer, unit: string, visually: boolean }
        //  return: { line, ch, ?hitSide: boolean }
        findPosH: function (jq, param) { return findPosH(jq[0], param); },

        //  param: { start: {line, ch}, amount: integer, unit: string }
        //  return: { line, ch, ?hitSide: boolean }
        findPosV: function (jq, param) { return findPosV(jq[0], param); },


        //  param: { option: string, value: any }
        setOption: function (jq, param) { return jq.each(function () { setOption(this, param); }); },

        //  option: string
        //  return: any
        getOption: function (jq, option) { return getOption(jq[0], option); },

        //  param: { map: object, bottom: boolean }
        addKeyMap: function (jq, param) { return jq.each(function () { addKeyMap(this, param); }); },

        //  map: object
        removeKeyMap: function (jq, map) { return jq.each(function () { removeKeyMap(this, map); }); },

        // param: { mode: string|object, ?options: object }
        addOverlay: function (jq, param) { return jq.each(function () { addOverlay(this, param); }); },

        //  mode: string|object
        removeOverlay: function (jq, mode) { return jq.each(function () { removeOverlay(this, mode); }); },

        //  param: { type: string, func: (...args) }
        on: function (jq, param) { return jq.each(function () { on(this, param); }); },

        //  param: { type: string, func: (...args) }
        off: function (jq, param) { return jq.each(function () { off(this, param); }); },


        //  return: CodeMirror.Doc
        getDoc: function (jq) { return $.data(jq[0], "codemirror").doc; },

        //  return: CodeMirror
        getEditor: function (jq) { return $.data(jq[0], "codemirror").cm; },

        //  doc: CodeMirror.Doc
        //  return: CodeMirror.Doc
        swapDoc: function (jq, doc) { return swapDoc(jq[0], doc); },

        //  copyHistory: boolean
        //  return: CodeMirror.Doc
        copy: function (jq, copyHistory) { return copy(jq[0], copyHistory); },

        //  param: { shareHist: boolean, from: integer, to: integer, mode: string|object }
        linkedDoc: function (jq, param) { return linkedDoc(jq[0], param); },

        //  doc: CodeMirror.Doc
        unlinkDoc: function (jq, doc) { return jq.each(function () { unlinkDoc(this, doc); }); },

        //  f: function: (doc: CodeMirror.Doc, sharedHist: boolean)
        iterLinkedDocs: function (jq, f) { return jq.each(function () { iterLinkedDocs(this, f); }); },



        undo: function (jq) { return jq.each(function () { undo(this); }); },

        redo: function (jq) { return jq.each(function () { redo(this); }); },

        undoSelection: function (jq) { return jq.each(function () { undoSelection(this); }); },

        redoSelection: function (jq) { return jq.each(function () { redoSelection(this); }); },

        //  return: { undo: integer, redo: integer }
        historySize: function (jq) { return historySize(jq[0]); },

        clearHistory: function (jq) { return jq.each(function () { clearHistory(this); }); },

        //  return: object
        getHistory: function (jq) { return getHistory(jq[0]); },

        //  history: object
        setHistory: function (jq, history) { return jq.each(function () { setHistory(this, history); }); },



        //  param: { from: {line, ch}, to: {line, ch}, ?options: {
        //          className: string, inclusiveLeft: boolean, inclusiveRight: boolean, atomic: boolean, collapsed: boolean,
        //          clearOnEnter: boolean, clearWhenEmpty: boolean, replacedWidth: Element, handleMouseEvents: Boolean,
        //          readOnly: boolean, addToHistory: boolean, startStyle: string, endStyle: string, title: string, shared: boolean
        //      } }
        //  return: TextMarker
        markText: function (jq, param) { return markText(jq[0], param); },

        //  param: { pos: {line, ch}, ?options: { widget: Element, insertLeft: boolean, shared: boolean } }
        //  return: TextMarker
        setBookmark: function (jq, param) { return setBookmark(jq[0], param); },

        //  param:  { from: {line, ch}, to: {line, ch} }
        //  return: array<TextMarker>
        findMarks: function (jq, param) { return findMarks(jq[0], param); },

        //  pos:    { line, ch }
        //  return: array<TextMarker>
        findMarksAt: function (jq, pos) { return findMarksAt(jq[0], pos); },

        //  return: array<TextMarker>
        getAllMarks: function (jq) { return getAllMarks(jq[0]); },



        //  param:  { line: integer|LineHandle, gutterID: string, value: Element }
        //  return: LineHandle
        setGutterMarker: function (jq, param) { return setGutterMarker(jq[0], param); },

        //  gutterID: string
        clearGutter: function (jq, gutterID) { return jq.each(function () { clearGutter(this, gutterID); }); },

        //  param:  { line: integer|LineHandle, where: string, cls: string }
        //  return: LineHandle
        addLineClass: function (jq, param) { return addLineClass(jq[0], param); },

        //  param:  { line: integer|LineHandle, where: string, cls: string }
        //  return: LineHandle
        removeLineClass: function (jq, param) { return removeLineClass(jq[0], param); },

        //  line:   integer|LineHandle
        //  return: object
        lineInfo: function (jq, line) { return lineInfo(jq[0], line); },

        //  param:  { pos: {line, ch}, node: Element, scrollIntoView: boolean }
        addWidget: function (jq, param) { return jq.each(function () { addWidget(this, param); }); },

        //  param:  { line: integer|LineHandle, node: Element, ?options: { 
        //          coverGutter: boolean, noHScroll: boolean, above: boolean, handleMouseEvents: boolean, insertAt: integer
        //      } }
        //  return: LineWidget{ methods: [clear(), changed()] }
        addLineWidget: function (jq, param) { return addLineWidget(jq[0], param); },



        //  param:  { width: number|string, height: number|string }
        setSize: function (jq, param) { return jq.each(function () { resize(this, param); }); },

        //  param:  { x: number, y: number }
        scrollTo: function (jq, param) { return jq.each(function () { scrollTo(this, param); }); },

        //  return: { left, top, width, height, clientWidth, clientHeight }
        getScrollInfo: function (jq) { return getScrollInfo(jq[0]); },

        //  param:  { what: {line, ch}|{left, top, right, bottom}|{from, to}|null, ?margin: number }
        scrollIntoView: function (jq, param) { return jq.each(function () { scrollIntoView(this, param); }); },

        //  param:  { where: boolean|{line, ch}, mode: string }
        //  return: { left, top, bottom }
        cursorCoords: function (jq, param) { return cursorCoords(jq[0], param); },

        //  param:  { pos: {line, ch}, ?mode: string }
        //  return: { left, right, top, bottom }
        charCoords: function (jq, param) { return charCoords(jq[0], param); },

        //  param:  { object: {left, top}, ?mode: string }
        //  return: { line, ch }
        coordsChar: function (jq, param) { return coordsChar(jq[0], param); },

        //  param:  { height: number, ?mode: string }
        //  return: number
        lineAtHeight: function (jq, param) { return lineAtHeight(jq[0], param); },

        //  param:  { line: number, ?mode: string }
        //  return: number
        heightAtLine: function (jq, param) { return heightAtLine(jq[0], param); },

        //  return: number
        defaultTextHeight: function (jq) { return defaultTextHeight(jq[0]); },

        //  return: number
        defaultCharWidth: function (jq) { return defaultCharWidth(jq[0]); },

        //  return: { from: number, to: number }
        getViewport: function (jq) { return getViewport(jq[0]); },



        //  return: object
        getMode: function (jq) { return $.data(jq[0], "codemirror").doc.getMode(); },

        //  pos: { line, ch }
        //  return: object
        getModeAt: function (jq, param) { return $.data(jq[0], "codemirror").doc.getModeAt(param); },

        //  param: { pos: {line, ch}, ?precise: boolean }
        //  return: { start, end, string, type, state }
        getTokenAt: function (jq, param) { return getTokenAt(jq[0], param); },

        //  param: { pos: { line, ch } }
        //  return: string
        getTokenTypeAt: function (jq, pos) { return $.data(jq[0], "codemirror").cm.getTokenTypeAt(pos); },

        //  param:  { pos: {line, ch}, type: string }
        //  return: array<helper>
        getHelpers: function (jq, param) { return getHelpers(jq[0], param); },

        //  param: { pos: {line, ch}, type: string }
        //  return: helper
        getHelper: function (jq, param) { return getHelper(jq[0], param); },

        //  param: { ?line: integer, ?precise: boolean }
        //  return: object
        getStateAfter: function (jq, param) { return getStateAfter(jq[0], param); },




        //  func: func: () → any
        //  return: any
        operation: function (jq, func) { return $.data(jq[0], "codemirror").cm.operation(func); },

        //  param:  { line: integer, ?dir: string|integer }
        indentLine: function (jq, param) { return jq.each(function () { indentLine(this, param); }); },

        //  ?value: boolean
        toggleOverwrite: function (jq, value) { return jq.each(function () { toggleOverwrite(this, value); }); },

        //  name: string
        execCommand: function (jq, name) { return jq.each(function () { execCommand(this, name); }); },

        //  index: integer
        //  return: { line, ch }
        posFromIndex: function (jq, index) { return posFromIndex(jq[0], index); },

        //  param:  { line, ch }
        //  return : integer
        indexFromPos: function (jq, param) { return indexFromPos(jq[0], param); },

        //  return: TextAreaElement
        getInputField: function (jq) { return getInputField(jq[0]); },

        //  return: Element
        getWrapperElement: function (jq) { return getWrapperElement(jq[0]); },

        //  return: Element
        getScrollerElement: function (jq) { return getScrollerElement(jq[0]); },

        //  return: Element
        getGutterElement: function (jq) { return getGutterElement(jq[0]); },


        validate: function (jq) { return jq.each(function () { validate(this); }); },

        isValid: function (jq) { return validate(jq[0]); },

        enableValidation: function (jq) { return jq.each(function () { setValidation(this, false) }); },

        disableValidation: function (jq) { return jq.each(function () { setValidation(this, true) }); }
    };

    $.fn.codemirror.defaults = $.extend({}, $.fn.panel.defaults, {

        //  CodeMirror 初始化时编辑器中的内容；string|CodeMirror.Doc
        value: "",
        //  CodeMirror 编辑器的代码类型模式；String 类型值，其值可以是 text/html、javascript、json 等；
        //  当使用指定的模式 mode 后，页面上同时需加载特定的模式 js 文件；
        //  如果不定义该参数，则默认取第一个加载的编辑器 mode
        //  string|object
        mode: null,
        //  CodeMirror 编辑器的主题 css 风格；string
        theme: "default",
        //  CodeMirror 编辑器的代码缩进空格数；integer；CodeMirror 官方默认为 2；此处修改为 4。
        indentUnit: 4,
        //  指定 CodeMirror 编辑器的进行换行时代码是否自动缩进；boolean
        smartIndent: true,
        //  指定 CodeMirror 编辑器的制表符宽度；integer
        tabSize: 4,
        //  指定 CodeMirror 编辑器中是否用制表符来替换缩进空格；boolean
        indentWithTabs: false,
        //  指定 CodeMirror 编辑器是否在当行内容改变时自动判断缩进；boolean
        electricChars: true,
        //  指定 CodeMirror 编辑器中哪些字符将会被占位符替代；RegExp
        specialChars: CodeMirror.defaults.specialChars,
        //  function 类型值
        specialCharPlaceholder: CodeMirror.defaults.specialCharPlaceholder,
        //  指定 CodeMirror 编辑器中在光标移动位置时指针是否可见；boolean
        rtlMoveVisually: CodeMirror.defaults.rtlMoveVisually,
        //  string 类型值；指定 CodeMirror 编辑器中的键盘映射配置；
        //  配置详情参见 http://codemirror.net/doc/manual.html#keymaps
        keyMap: "default",
        //  JSON-Object 类型；指定 CodeMirror 编辑器中除 keyMap 外额外的键盘快捷键配置
        //  配置详情参见 http://codemirror.net/doc/manual.html#option_keyMap
        extraKeys: null,
        //  boolean 类型值；指定 CodeMirror 编辑器中当行代码过长时是否自动换行（如果自动换行则不显示横向滚动条）
        lineWrapping: false,
        //  boolean 类型值；指定 CodeMirror 编辑器是否显示行号
        lineNumbers: false,
        //  integer 类型值；指定 CodeMirror 编辑器以代码中的那一行作为起始行；行索引号从 1 开始计数；
        firstLineNumber: 1,
        //  function 类型值；这是一个回调函数，用于格式化行号
        lineNumberFormatter: CodeMirror.defaults.lineNumberFormatter,
        //
        gutters: CodeMirror.defaults.gutters,
        //  boolean 类型值；指定 CodeMirror 编辑器中滚动条在移动时，标尺部分（行号和工具栏）是否固定不动
        fixedGutter: true,
        //  boolean 类型值；指定 CodeMirror 编辑器在 fixedGutter 参数为 true 时，控件的左右滚动条是否另起一个 scrollbar 显示；
        coverGutterNextToScrollbar: false,
        //  boolean | string 类型值；指定 CodeMirror 编辑器是否为只读不可编辑状态
        //  该参数值可以为 true、false、或者字符串值 "nocursor"；如果值为 "nocursor" 则编辑器不仅不可编辑同时也不能得到输入焦点；
        readOnly: false,
        //  boolean 类型值；指定 CodeMirror 编辑器在用鼠标进行内容选择时是否始终显示输入焦点
        showCursorWhenSelecting: false,
        //  integer 类型值；指定 CodeMirror 编辑器所能支持的最大撤销次数
        undoDepth: 200,
        //  integer 类型值；该值表示当 CodeMirror 编辑器内容发生改变时的事件调用延迟时间(毫秒数)；
        historyEventDelay: 1250,
        // integer 类型值；指定　CodeMirror 编辑器在页面上被分配的 HTML-tabIndex 属性值；如果不指定该属性值，将会被自动分配；
        tabindex: null,
        //  boolean 类型值；指定 CodeMirror 编辑器在初始化后是否自动获取焦点；
        autofocus: null,

        /************************************************/
        //  boolean 类型值；指定 CodeMirror 编辑器是否启用文本拖放功能；
        dragDrop: true,
        //  number 类型值；指定 CodeMirror 编辑器中输入光标的闪烁速率(毫秒数)；如果该参数为 0，则输入光标不闪烁；
        cursorBlinkRate: 530,
        //  number 类型值；指定 CodeMirror 编辑器中输入光标的边界空白位置margin
        cursorScrollMargin: 0,
        //  number 类型值；指定 CodeMirror 编辑器中输入光标的高度；默认是1表示占满整行高度；
        //  例如，可以设置为 0.5 表示半行高度，设置为 2 表示占满 2 行高度；
        cursorHeight: 1,
        //  boolean 类型值；指定 CodeMirror 编辑器控件中点击鼠标右键后，是否自动清空区域文本选中效果；
        resetSelectionOnContextMenu: true,
        //  number 类型值；代码的高亮效果显示是通过一个伪后台线程执行来完成的，以下两个参数表示这个伪后台线程的工作和延迟时间(毫秒数)；
        //  通过改变如下两个参数的值，可以增强高亮实时刷新的效果或者降低对浏览器性能的开销；
        workTime: 100, workDelay: 100,
        //  number 类型值；指定 CodeMirror 编辑器在获取输入焦点后自动检测编辑器内文本内容变化的时间频率(毫秒数)；
        //  该值的大小将影响到 change 事件的调用延迟时间；
        pollInterval: 100,
        //  boolean 类型值；指定 CodeMirror 在处理代码高亮效果显示时，对于相邻的同类型字符块是否合并到一个 span 中来显示；
        //  如果该值设置为 true，将能一定程度提高代码高亮解析的效率，但是可能会改变某些特殊类型样式(例如圆角)的显示效果
        flattenSpans: true,
        //  boolean 类型值；指定 CodeMirror 在解析不同 mode 类型代码块高亮效果时，是否根据不同的 mode 设定在不同标记前加一个用于标记的 css 类；
        addModeClass: false,
        //  number 类型值；指定 CodeMirror 编辑器进行代码高亮解析的最大标签处理数量；该值设置的越大，同一个代码块中所能解析的文本域更大，但是也将会消耗更多的浏览器性能；
        maxHighlightLength: 10000,
        //  number 类型值；
        viewportMargin: 10,


        onchange: function () { },
        onchanges: function () { },
        onbeforeChange: function () { },
        oncursorActivity: function () { },
        onkeyHandled: function () { },
        oninputRead: function () { },
        onbeforeSelectionChange: function () { },
        onviewportChange: function () { },
        onswapDoc: function () { },
        ongutterClick: function () { },
        ongutterContextMenu: function () { },
        onfocus: function () { },
        onblur: function () { },
        onscroll: function () { },
        onupdate: function () { },
        onrenderLine: function () { },
        onmousedown: function () { },
        ondblclick: function () { },
        oncontextmenu: function () { },
        onkeydown: function () { },
        onkeypress: function () { },
        onkeyup: function () { },
        ondragstart: function () { },
        ondragenter: function () { },
        ondragover: function () { },
        ondrop: function () { }
    });

    $.extend($.fn.codemirror.defaults, {

        disabled: false,

        mode: "text/html",

        lineNumbers: true,

        fontSize: null,

        lineHeight: null,

        url: null,

        method: "get",

        queryParams: {},

        loader: loader,

        loadFilter: function (data) { return data; },


        required: false,
        missingMessage: "该编辑框不能为空.",
        //  "left", "right", "top", "bottom"
        tipPosition: "right",
        deltaX: 0,
        tipOptions: $.fn.validatebox.defaults.tipOptions,
        novalidate: false,


        onResize: function (width, height) { },

        onBeforeLoad: function (param) { },

        onLoad: function () { },

        onLoadError: function () { }
    });


    $.parser.plugins.push("codemirror");

    if ($.fn.form && $.isArray($.fn.form.otherList)) {
        $.fn.form.otherList.push("codemirror");
        //$.array.insert($.fn.form.otherList, 0, "codemirror");
    }

})(jQuery);