/**
* jQuery EasyUI 1.3.6
* Copyright (c) 2009-2014 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL or commercial licenses
* To use it on other terms please contact author: info@jeasyui.com
* http://www.gnu.org/licenses/gpl.txt
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI uploadify Plugin Extensions 1.0 beta
* jQuery EasyUI uploadify 插件扩展
* jquery.euploadify.js
*
* 依赖项：
*   1、jquery.jdirk.js v1.0 beta late
*   2、jeasyui.extensions.js v1.0 beta late
*   3、jeasyui.extensions.menu.js v1.0 beta late
*   4、jeasyui.extensions.panel.js v1.0 beta late 和 jeasyui.extensions.window.js v1.0 beta late(可选)
*   5、jeasyui.extensions.progressbar.js v1.0 beta late

*   6、uploadify/jquery.uploadify.js
*   7、uploadify/uploadify.css
*   8、uploadify/uploadify.swf
*   9、uploadify/uploadify.php|uploadify.ashx|uploadify.jsp
*
* Copyright (c) 2013-2014 ChenJianwei personal All rights reserved.
* http://www.chenjianwei.org
*/
(function ($, undefined) {

    function create(target) {
        var t = $(target).addClass("euploadify-f").hide(),
            state = $.data(target, "euploadify"), opts = state.options,
            name = state.name = t.attr("name");

        state.uploadifyID = "euploadify_" + $.util.guid("N");
        state.queueID = "euploadify_queue_" + $.util.guid("N");
        state.lastFileID = null;

        state.panel = $("<div class=\"euploadify-panel\"></div>").insertAfter(t).append(t).panel($.extend({}, opts, {
            onResize: function (width, height) {
                $.fn.panel.defaults.onResize.apply(this, arguments);
                $.extend(opts, { width: width, height: height });
                setSize(target);
                if ($.isFunction(opts.onResize)) { opts.onResize.apply(target, arguments); }
            }
        }));
        state.wrapper = state.panel.panel("body").addClass("euploadify-wrapper");
        state.queueDOM = $("<div id=\"" + state.queueID + "\" class=\"euploadify-queue\"></div>").appendTo(state.wrapper);
        state.queues = [];

        t.removeAttr(name).attr("euploadifyName", name);

        opts.originalValue = opts.value;
        opts.originalFormData = $.extend({}, opts.formData);

        initializeUploadify(target);
        setValues(target, opts.value);
        setSize(target);
        if (opts.disabled) { disable(target, opts.disabled); }
        setValidation(target)
    };


    function refreshQueueStatus(queues, file, value) {
        var q = $.array.first(queues, function (f) { return f.id == file.id; })
        if (q) {
            q.filestatus = file.filestatus;
            if (value != null) { q.value = value; }
        }
    };


    function initializeUploadify(target) {
        var t = $(target), state = $.data(target, "euploadify"), opts = state.options,
            template = String(opts.multiTemplate).toLowerCase();
        if (!$.array.contains(["uploadify", "simple", "bootstrap", "grid"], template)) {
            template = $.fn.euploadify.defaults.multiTemplate;
        }
        var uopts = {
            auto: opts.auto, method: opts.method, multi: opts.multi,
            buttonClass: opts.buttonClass, buttonCursor: opts.buttonCursor, buttonImage: opts.buttonImage, buttonText: opts.buttonText,
            checkExisting: opts.checkExisting, debug: opts.debug,
            fileObjName: opts.fileObjName, fileSizeLimit: opts.fileSizeLimit, fileTypeDesc: opts.fileTypeDesc, fileTypeExts: opts.fileTypeExts,
            formData: opts.formData, progressData: opts.progressData,
            width: opts.buttonWidth, height: opts.buttonHeight, queueID: state.queueID,
            itemTemplate: opts.multi && template != "custome" ? opts.multiTemplateData[template] : opts.itemTemplate,
            overrideEvents: opts.overrideEvents, preventCaching: opts.preventCaching,
            queueSizeLimit: opts.queueSizeLimit, uploadLimit: opts.uploadLimit,
            removeCompleted: opts.removeCompleted, removeTimeout: opts.removeTimeout, requeueErrors: opts.requeueErrors,
            swf: opts.swf, uploader: opts.uploader, successTimeout: opts.successTimeout,

            onCancel: function (file) {
                $.array.remove(state.queues, file, function (q) { return file.id == q.id; });
                return opts.onCancel.apply(target, arguments);
            },
            onClearQueue: function () {
                state.queues = [];
                return opts.onClearQueue.apply(target, arguments);
            },
            onDestroy: function () {
                state.queues = null;
                return opts.onDestroy.apply(target, arguments);
            },
            onDialogClose: function () { return opts.onDialogClose.apply(target, arguments); },
            onDialogOpen: function () { return opts.onDialogOpen.apply(target, arguments); },
            onDisable: function () { return opts.onDisable.apply(target, arguments); },
            onEnable: function () { return opts.onEnable.apply(target, arguments); },
            onFallback: function () { return opts.onFallback.apply(target, arguments); },
            onInit: function () { return opts.onInit.apply(target, arguments); },
            onQueueComplete: function () { return opts.onQueueComplete.apply(target, arguments); },
            onSelect: function (file) {
                state.queues.push($.extend({}, file));
                return opts.onSelect.apply(target, arguments);
            },
            onSelectError: function () { return opts.onSelectError.apply(target, arguments); },
            onSWFReady: function () { return opts.onSWFReady.apply(target, arguments); },
            onUploadComplete: function (file) {
                refreshQueueStatus(state.queues, file);
                return opts.onUploadComplete.apply(target, arguments);
            },
            onUploadError: function (file, errorCode, errorMsg, errorString) {
                refreshQueueStatus(state.queues, file);
                return opts.onUploadError.apply(target, arguments);
            },
            onUploadProgress: function (file, bytesUploaded, bytesTotal, totalBytesUploaded, totalBytesTotal) {
                //refreshQueueStatus(state.queues, file);
                return opts.onUploadProgress.apply(target, arguments);
            },
            onUploadStart: function (file) {
                refreshQueueStatus(state.queues, file);
                return opts.onUploadStart.apply(target, arguments);
            },
            onUploadSuccess: function (file, data, response) {
                var obj = opts.responseFilter.call(target, data, file),
                    val = file.value = obj && obj.value != null && obj.value != undefined ? obj.value : null;
                refreshQueueStatus(state.queues, file, val);

                var values = opts.multi ? $.array.map(getQueuesByStatus(target, -4), function (f) { return f.value; }) : [val];
                setValues(target, values);
                return opts.onUploadSuccess.apply(target, arguments);
            }
        };
        if (opts.multi) {
            state.wrapper.addClass("euploadify-wrapper-multi euploadify-wrapper-multi-" + template);
            state.buttonbar = $("<div class=\"euploadify-buttonbar\"></div>").insertBefore(state.queueDOM);
            if (template == "grid") {
                buildGridPanel(target, uopts);
            } else {
                buildMultiPanel(target, uopts);
            }
        } else {
            state.wrapper.addClass("euploadify-wrapper-single");
            state.queueDOM.addClass("euploadify-hidden");
            state.progressbar = $("<div class=\"euploadify-progressbar\"></div>").insertBefore(state.queueDOM).progressbar({
                width: 400, height: 18, value: 0, text: opts.emptyText
            });
            state.buttonbar = $("<div class=\"euploadify-buttonbar\"></div>").insertAfter(state.progressbar);

            var onCancel = uopts.onCancel, onSelect = uopts.onSelect,
                onUploadStart = uopts.onUploadStart, onUploadProgress = uopts.onUploadProgress,
                onUploadError = uopts.onUploadError, onUploadSuccess = uopts.onUploadSuccess;
            $.extend(uopts, {
                onCancel: function (file) {
                    var ret = onCancel.apply(this, arguments), queues = getQueues(target);
                    if (!queues.length) {
                        state.progressbar.progressbar("setText", opts.emptyText).progressbar("setValue", 0);
                        return ret;
                    }
                    if (queues.length) {
                        state.progressbar.progressbar("setText", getFileName(target, queues[0]) + "(" + $.number.toFileSize(file.size) + ") - 0%").progressbar("setValue", 0);
                    } else {
                        state.progressbar.progressbar("setText", opts.emptyText).progressbar("setValue", 0);
                    }
                    return ret;
                },
                onSelect: function (file) {
                    var ret = onSelect.apply(this, arguments);
                    if (state.lastFileID && !opts.multi) {
                        state.uploadify.uploadify("cancel", state.lastFileID);
                    }
                    if (file) {
                        state.progressbar.progressbar("setText", getFileName(target, file) + "(" + $.number.toFileSize(file.size) + ") - 0%").progressbar("setValue", 0).
                            find(".progressbar-value .progressbar-text").removeClass("progressbar-text-success progressbar-text-error");
                        state.lastFileID = file.id;
                    } else {
                        state.progressbar.progressbar("setText", opts.emptyText).progressbar("setValue", 0).
                            find(".progressbar-value .progressbar-text").removeClass("progressbar-text-success progressbar-text-error");
                    }
                    return ret;
                },
                onUploadStart: function (file) {
                    var ret = onUploadStart.apply(this, arguments);
                    t.euploadify("resetFormData");
                    var data = $.extend({}, file, { customeName: file.name }),
                        formData = opts.requestFilter.call(target, data, file);
                    state.uploadify.uploadify("settings", "formData", formData);
                    return ret;
                },
                onUploadProgress: function (file, bytesUploaded, bytesTotal, totalBytesUploaded, totalBytesTotal) {
                    var ret = onUploadProgress.apply(this, arguments),
                        val = !bytesTotal ? 0 : (bytesUploaded / bytesTotal * 100).round(2);
                    state.progressbar.progressbar("setText", getFileName(target, file) + "(" + $.number.toFileSize(file.size) + ") - {value}%").progressbar("setValue", val).
                        find(".progressbar-value .progressbar-text").removeClass("progressbar-text-success progressbar-text-error");
                    return ret;
                },
                onUploadError: function (file, errorCode, errorMsg, errorString) {
                    var ret = onUploadError.apply(this, arguments);
                    if (!$.array.contains(["Cancelled", "Stopped"], errorString)) {
                        state.progressbar.progressbar("setText", getFileName(target, file) + "(" + opts.errorText + ":" + errorString + ")").
                            find(".progressbar-value .progressbar-text").removeClass("progressbar-text-success progressbar-text-error").addClass("progressbar-text-error");
                    }
                    return ret;
                },
                onUploadSuccess: function (file, data, response) {
                    var ret = onUploadSuccess.apply(this, arguments);
                    state.progressbar.progressbar("setText", getFileName(target, file) + "(" + opts.finishText + ")").progressbar("setValue", 100).
                        find(".progressbar-value .progressbar-text").removeClass("progressbar-text-success progressbar-text-error").addClass("progressbar-text-success");
                    return ret;
                }
            });
        }

        state.button = $("<a class=\"l-btn l-btn-small euploadify-button euploadify-select" + (opts.buttonPlain ? " l-btn-plain" : "") +
            "\"><span class=\"l-btn-left l-btn-icon-left\"><span class=\"l-btn-icon " + opts.buttonIcon + "\">&nbsp;</span></span></a>").appendTo(state.buttonbar);
        state.uploadify = $("<input id=\"" + state.uploadifyID + "\" type=\"file\" />").insertBefore(state.button.find("span.l-btn-icon")).uploadify(uopts);
        state.uploadify = $("#" + state.uploadifyID);

        state.uploadButton = $("<a class=\"euploadify-button euploadify-upload" + (opts.auto && !opts.multi ? " euploadify-hidden" : "") + "\"></a>").appendTo(state.buttonbar).linkbutton({
            plain: opts.buttonPlain, text: opts.uploadText, iconCls: opts.uploadIcon,
            onClick: function () {
                var queues = getQueues(target),
                    ff = $.array.first(queues, function (val) { return val.filestatus != -4; });
                if (!queues || !queues.length || !ff) {
                    return $.easyui.messager.show(opts.message.queueEmpty);
                }
                state.uploadify.uploadify("upload", "*");
            }
        });

        state.stopButton = $("<a class=\"euploadify-button euploadify-stop" + (!opts.showStop ? " euploadify-hidden" : "") + "\"></a>").appendTo(state.buttonbar).linkbutton({
            plain: opts.buttonPlain, text: opts.stopText, iconCls: opts.stopIcon,
            onClick: function () {
                state.uploadify.uploadify("stop");
                return opts.onButtonStop.call(target, getQueues(target), this);
            }
        });

        state.cancelButton = $("<a class=\"euploadify-button euploadify-cancel" + (!opts.showCancel ? " euploadify-hidden" : "") + "\"></a>").appendTo(state.buttonbar).linkbutton({
            plain: opts.buttonPlain, text: opts.cancelText, iconCls: opts.cancelIcon,
            onClick: function () { state.uploadify.uploadify("cancel", "*"); }
        });

        if (opts.multi && template == "grid") {
            $("<span>-</span>").insertAfter(state.button);
            if (!opts.auto) {
                $("<span>-</span>").insertAfter(state.uploadButton);
            }
            if (opts.showStop || opts.showCancel) {
                $("<span>-</span>").appendTo(state.buttonbar);
            }

            state.removeButton = $("<a class=\"euploadify-button\"></a>").appendTo(state.buttonbar).linkbutton({
                plain: opts.buttonPlain, text: opts.removeText, iconCls: "icon-hamburg-busy",
                onClick: function () {
                    var rows = state.grid.datagrid("getChecked"), array = $.array.clone(rows);
                    if (!rows || !rows.length) { return $.easyui.messager.show(opts.message.mustNotEmpty); }
                    $.each(array, function (i, row) {
                        var index = state.grid.datagrid("getRowIndex", row);
                        if (index > -1) { state.grid.datagrid("deleteRow", index); }
                        state.uploadify.uploadify("cancel", row.id);
                        $.array.remove(state.queues, row, function (q) { return row.id == q.id; });
                    });
                    var values = $.array.map(getQueuesByStatus(target, -4), function (f) { return f.value; });
                    setValues(target, values);
                }
            });

            state.buttonbar.toolbar();
        }

        if (opts.tooltip) {
            var tooltip = opts.tooltip === true ? $.fn.euploadify.defaults.tooltip : opts.tooltip;
            if (state.button) { state.button.tooltip({ content: tooltip.button }); }
            if (state.uploadButton) { state.uploadButton.tooltip({ content: tooltip.upload }); }
            if (state.stopButton) { state.stopButton.tooltip({ content: tooltip.stop }); }
            if (state.cancelButton) { state.cancelButton.tooltip({ content: tooltip.cancel }); }
            if (state.removeButton) { state.removeButton.tooltip({ content: tooltip.remove }); }
        }
    };


    function buildMultiPanel(target, uopts) {
        var t = $(target), state = $.data(target, "euploadify"), opts = state.options,
            onUploadStart = uopts.onUploadStart;
        state.queueDOM.on("click", ".uploadify-queue-item .cancel a", function () {
            var item = $(this).closest(".uploadify-queue-item"), id = item.attr("id");
            $.array.remove(state.queues, id, function (q) { return q.id == id; });
            var queues = getQueuesByStatus(target, -4),
                values = $.array.map(queues, function (q) { return q.value; });
            setValues(target, values.length ? values : [null])
        });
        $.extend(uopts, {
            onUploadStart: function (file) {
                var ret = onUploadStart.apply(this, arguments);
                t.euploadify("resetFormData");
                var data = $.extend({}, file, { customeName: file.name }),
                    formData = opts.requestFilter.call(target, data, file);
                state.uploadify.uploadify("settings", "formData", formData);
                return ret;
            }
        });
    };


    function buildGridPanel(target, uopts) {
        var t = $(target), state = $.data(target, "euploadify"), opts = state.options,
            layout = state.layout = $("<div class=\"euploadify-gridpanel-layout\"></div>").insertBefore(state.queueDOM),
            north = state.north = $("<div class=\"euploadify-gridpanel-north\" data-options=\"region: 'north', split: false, border: false\" style=\"height: 33px;\"></div>").appendTo(layout).append(state.buttonbar),
            center = state.center = $("<div class=\"euploadify-gridpanel-center\" data-options=\"region: 'center', border: false\"></div>").appendTo(layout),
            grid = state.grid = $("<table class=\"euploadify-gridpanel-grid\"></table>").appendTo(center);
        state.queueDOM.addClass("euploadify-hidden");
        layout.layout({ fit: true });
        grid.datagrid({
            fit: true, border: false, idField: "id", rownumbers: true, refreshMenu: false, extEditing: true, singleEditing: true,
            frozenColumns: [[
                    { field: "ck", checkbox: true, filterable: false },
                    { field: "name", title: "全文件名", width: 100, filterable: false }
            ]],
            columns: [[
                { field: "customeName", title: opts.gridColumnNames.customeName, width: 120, editor: "text", filterable: false },
                { field: "type", title: opts.gridColumnNames.type, width: 60, filterable: false },
                {
                    field: "size", title: opts.gridColumnNames.size, width: 80, filterable: false,
                    formatter: function (val) { return $.number.toFileSize(val); }
                },
                {
                    field: "progress", title: opts.gridColumnNames.progress, width: 80, align: "center", filterable: false,
                    formatter: function (val, row) {
                        return "<div class=\"euploadify-gridpanel-progress\">" +
                            "<div class=\"euploadify-gridpanel-progress-value" +
                            (row.filestatus == -3 ? " euploadify-gridpanel-progress-error" : (row.filestatus == -4 ? " euploadify-gridpanel-progress-success" : "")) +
                            "\" style=\"width: " + String(val || 0) + "%;\"></div>" +
                            "</div>";
                    }
                },
                {
                    field: "progressValue", title: opts.gridColumnNames.progressValue, width: 50, filterable: false,
                    formatter: function (val) { return String(val || 0) + "%"; }
                },
                {
                    field: "filestatus", title: opts.gridColumnNames.filestatus, width: 80, filterable: false,
                    formatter: function (val, row, index) {
                        var str = opts.FILE_STATUS[val], ret = val == -3 ?
                            ("<span title=\"" + row.errorString + "\" class=\"euploadify-gridpanel-status euploadify-gridpanel-status-error\">" + str + "</span>") :
                            (val == -4 ? ("<span class=\"euploadify-gridpanel-status euploadify-gridpanel-status-success\">" + str + "</span>") : str);
                        return ret;
                    }
                },
                {
                    field: "operate", title: opts.gridColumnNames.operate, width: 80, filterable: false,
                    formatter: function (val, row) {
                        return "<div title=\"" + opts.gridRowButtonNames.cancel + "\" class=\"euploadify-button-mini\" onclick=\"javascript: return $.fn.euploadify.cancelQueue(this);\">\
                                    <span class=\"euploadify-button-mini-icon icon-standard-delete\">&nbsp;</span>\
                                </div>\
                                <div title=\"" + opts.gridRowButtonNames.upload + "\" class=\"euploadify-button-mini\" onclick=\"javascript: return $.fn.euploadify.uploadQueue(this);\">\
                                    <span class=\"euploadify-button-mini-icon icon-standard-arrow-up\">&nbsp;</span>\
                                </div>";
                    }
                }
            ]],
            rowContextMenu: [
                {
                    text: opts.gridRowContextNames.edit, iconCls: "icon-edit",
                    disabled: function () { return opts.disabled || state.wrapper.is(".euploadify-disabled"); },
                    handler: function (e, index, row) { grid.datagrid("beginEdit", index); }
                },
                {
                    text: opts.gridRowContextNames.cancel, iconCls: "icon-standard-delete",
                    disabled: function () { return opts.disabled || state.wrapper.is(".euploadify-disabled"); },
                    handler: function (e, index, row) {
                        if (row.filestatus == -4) {
                            $.easyui.messager.show(opts.message.fileUploadSuccessCanntCancel);
                        } else {
                            state.uploadify.uploadify("cancel", row.id);
                        }
                    }
                },
                {
                    text: opts.gridRowContextNames.upload, iconCls: "icon-standard-arrow-up",
                    disabled: function () { return opts.disabled || state.wrapper.is(".euploadify-disabled"); },
                    handler: function (e, index, row) {
                        if (row.filestatus == -4) {
                            $.easyui.messager.show(opts.message.fileUploadSuccess);
                        } else {
                            state.uploadify.uploadify("upload", row.id);
                        }
                    }
                }
            ],
            onBeginEdit: function (index, row) {
                if ($.array.contains([-2, -4, -5], row.filestatus)) {
                    var msg = $.string.format(opts.message.fileCannotRename, opts.FILE_STATUS[row.filestatus]);
                    $.easyui.messager.show(msg);
                    grid.datagrid("cancelEdit", index);
                }
                return $.fn.datagrid.defaults.onBeginEdit.apply(this, arguments);
            },
            onAfterEdit: function (index, row, changes) {
                if (!row.customeName) {
                    $.easyui.messager.show(opts.message.fileNameCannotEmpty);
                    var array = String(row.name).split("."), temp = $.array.removeAt(array, array.length - 1), name = temp.join("");
                    grid.datagrid("updateRow", { index: index, row: { customeName: name } });
                }
                return $.fn.datagrid.defaults.onAfterEdit.apply(this, arguments);
            }
        });

        var onCancel = uopts.onCancel, onSelect = uopts.onSelect,
            onUploadStart = uopts.onUploadStart, onUploadProgress = uopts.onUploadProgress,
            onUploadError = uopts.onUploadError, onUploadComplete = uopts.onUploadComplete;
        $.extend(uopts, {
            onCancel: function (file) {
                var ret = onCancel.apply(this, arguments),
                    id = file.id, index = grid.datagrid("getRowIndex", id);
                if (index > -1) { grid.datagrid("deleteRow", index); }
                return ret;
            },
            onSelect: function (file) {
                var ret = onSelect.apply(this, arguments),
                    nameArray = String(file.name).split("."),
                    tempName = $.array.removeAt(nameArray, nameArray.length - 1), name = tempName.join(""),
                    row = $.extend({}, file, { customeName: name, progress: 0, progressValue: 0 });
                grid.datagrid("appendRow", row);
                return ret;
            },
            onUploadStart: function (file) {
                var ret = onUploadStart.apply(this, arguments),
                    id = file.id, index = grid.datagrid("getRowIndex", id), row = index > -1 ? grid.datagrid("getRowData", index) : null;
                if (row) {
                    t.euploadify("resetFormData");
                    var data = $.extend({}, row, { customeName: row.customeName + file.type }),
                        formData = opts.requestFilter.call(target, data, file);
                    state.uploadify.uploadify("settings", "formData", formData);
                    grid.datagrid("updateRow", { index: index, row: { filestatus: file.filestatus } });
                }
                return ret;
            },
            onUploadProgress: function (file, bytesUploaded, bytesTotal, totalBytesUploaded, totalBytesTotal) {
                var ret = onUploadProgress.apply(this, arguments),
                    val = !bytesTotal ? 0 : (bytesUploaded / bytesTotal * 100).round(2), id = file.id, index = grid.datagrid("getRowIndex", id);
                if (index > -1) {
                    grid.datagrid("updateRow", { index: index, row: { progress: val, progressValue: val, filestatus: file.filestatus } });
                }
                return ret;
            },
            onUploadError: function (file, errorCode, errorMsg, errorString) {
                var ret = onUploadError.apply(this, arguments),
                    id = file.id, index = grid.datagrid("getRowIndex", id), row = index > -1 ? grid.datagrid("getRowData", index) : null;
                if (row) {
                    state.uploadify.uploadify("settings", "formData", row);
                    grid.datagrid("updateRow", { index: index, row: { errorString: errorCode + "," + errorMsg + "," + errorString } });
                }
                return onUploadError.apply(this, arguments);
            },
            onUploadComplete: function (file) {
                var ret = onUploadComplete.apply(this, arguments),
                    id = file.id, index = grid.datagrid("getRowIndex", id);
                if (index > -1) {
                    grid.datagrid("updateRow", { index: index, row: { filestatus: file.filestatus } });
                }
                return ret;
            }
        });
    };



    function getOptions(target) {
        var state = $.data(target, "euploadify"), opts = state.panel.panel("options");
        return $.extend(state.options, {
            title: opts.title, iconCls: opts.iconCls, width: opts.width, height: opts.height, left: opts.left, top: opts.top,
            cls: opts.cls, headerCls: opts.headerCls, bodyCls: opts.bodyCls, style: opts.style, fit: opts.fit, border: opts.border,
            doSize: opts.doSize, noheader: opts.noheader, collapsible: opts.collapsible, minimizable: opts.minimizable, maximizable: opts.maximizable,
            closable: opts.closable, collapsed: opts.collapsed, minimized: opts.minimized, maximized: opts.maximized, closed: opts.closed
        });
    };





    function getUploadify(target) {
        return $.data(target, "euploadify").uploadify;
    };

    function getButtons(target) {
        return $.data(target, "euploadify").buttonbar.find("a.euploadify-button");
    };

    function getFileName(target, file) {
        if (!file || !file.name) { return $(target).euploadify("options").emptyText; }
        return $.string.getByteLen(file.name) > 28 ? $.string.leftBytes(file.name, 25) + "..." : file.name;
    };

    function getQueueDOM(target) {
        return $.data(target, "euploadify").queueDOM;
    };

    function getGrid(target) {
        return $.data(target, "euploadify").grid;
    };

    function getQueues(target) {
        return $.array.clone($.data(target, "euploadify").queues);
    };

    function getQueuesByStatus(target, status) {
        return $.array.filter(getQueues(target), function (q) { return q.filestatus == status; });
    };

    function setSize(target) {
        var t = $(target), state = $.data(target, "euploadify"), opts = state.options;
        if (state.buttonbar && state.progressbar && !opts.multi) {
            var fit = t._fit(false), width = fit.width - state.buttonbar.outerWidth() - 15;
            state.progressbar.progressbar("resize", width);
        }
    };

    function cancel(target, param) {
        var state = $.data(target, "euploadify");
        state.uploadify.uploadify("cancel", param);
    };

    function destroy(target) {
        var t = $(target), state = $.data(target, "euploadify");
        state.uploadify.uploadify("destroy");
        state.panel.panel("destroy");
        t.remove();
    }

    function disable(target, setDisabled) {
        if (setDisabled == null || setDisabled == undefined) {
            setDisabled = true;
        }
        var t = $(target), state = $.data(target, "euploadify"), opts = state.options;
        opts.disabled = setDisabled;
        var buttons = state.buttonbar.find(".euploadify-button:not(.euploadify-select)");
        if (setDisabled) {
            t.attr("disabled", true);
            state.wrapper.addClass("euploadify-disabled");
            state.button.addClass("l-btn-disabled");
            if (opts.buttonPlain) { state.button.addClass("l-btn-plain-disabled"); }
            $.util.tryExec(function () {
                state.uploadify.uploadify("stop");
            });
            buttons.linkbutton("disable");
        } else {
            t.removeAttr("disabled");
            state.wrapper.removeClass("euploadify-disabled");
            state.button.removeClass("l-btn-disabled");
            if (opts.buttonPlain) { state.button.removeClass("l-btn-plain-disabled"); }
            buttons.linkbutton("enable");
        }
        $.util.tryExec(function () {
            state.uploadify.uploadify("disable", setDisabled);
        });
    };

    function enable(target) {
        disable(target, false);
    };

    function stop(target) {
        $.data(target, "euploadify").uploadify.uploadify("stop");
    };

    function upload(target, param) {
        $.data(target, "euploadify").uploadify.uploadify("upload", param);
    };

    function resize(target, param) {
        var state = $.data(target, "euploadify");
        state.panel.panel("resize", param);
    };


    function settings(target, param) {
        var t = $(target);
        if (!param) { return t; }
        var state = $.data(target, "euploadify"), opts = state.options;
        if (typeof param == "string") {
            return opts[param];
        } else {
            if (param.name) {
                opts[param.name] = param.value;
                state.uploadify.uploadify("settings", param.name, param.value);
            }
            return t;
        }
    };


    function setFormData(target, param) {
        param = param || {};
        var state = $.data(target, "euploadify"), opts = state.options,
            swfuploadify = state.uploadify.data("uploadify"),
			settings = swfuploadify.settings;
        settings.formData = {};
        swfuploadify.setPostParams(opts.formData = $.extend(settings.formData, param));
    };

    function resetFormData(target) {
        var state = $.data(target, "euploadify"), opts = state.options;
        setFormData(target, opts.originalFormData);
    };

    function clearFormData(target) {
        setFormData(target, {});
    };




    function hideTip(target) {
        var state = $.data(target, "euploadify");
        state.tip = false;
        state.wrapper.tooltip("hide");
    };

    function showTip(target) {
        var state = $.data(target, "euploadify"), opts = state.options;
        state.wrapper.tooltip($.extend({}, opts.tipOptions, { content: opts.missingMessage, position: opts.tipPosition, deltaX: opts.deltaX })).tooltip("show");
        state.tip = true;
    };

    function repositionTip(target) {
        var state = $.data(target, "euploadify");
        if (state && state.tip) {
            state.wrapper.tooltip("reposition");
        }
    };

    function initializeValidate(target) {
        var t = $(target), state = $.data(target, "euploadify"), opts = state.options;
        state.wrapper.unbind(".euploadify");
        if (opts.novalidate) {
            return;
        }
        state.wrapper.bind("focus.euploadify", function () {
            state.validating = true;
            state.values = undefined;
            (function () {
                if (state.validating) {
                    var values = t.euploadify("getValues").join(opts.separator);
                    if (state.values != values) {
                        state.values = values;
                        if (state.timer) {
                            clearTimeout(state.timer);
                        }
                        state.timer = setTimeout(function () { t.euploadify("validate"); }, opts.delay);
                    } else {
                        repositionTip(target);
                    }
                    setTimeout(arguments.callee, 200);
                }
            })();
        }).bind("blur.euploadify", function () {
            if (state.timer) {
                clearTimeout(state.timer);
                state.timer = undefined;
            }
            state.validating = false;
            hideTip(target);
        }).bind("mouseenter.euploadify", function () {
            if (state.wrapper.hasClass("euploadify-invalid")) {
                showTip(target);
            }
        }).bind("mouseleave.euploadify", function () {
            if (!state.validating) {
                hideTip(target);
            }
        });
    };

    function validate(target) {
        var t = $(target), state = $.data(target, "euploadify"), opts = state.options,
            values = t.euploadify("getValues"), str = values.join(opts.separator);
        state.wrapper.removeClass("euploadify-invalid");
        hideTip(target);
        if (!opts.novalidate && opts.required && (!values.length || !str) && !t.is(":disabled") && !state.wrapper.hasClass("euploadify-disabled")) {
            state.wrapper.addClass("euploadify-invalid");
            if (state.validating) {
                showTip(target);
            }
            return false;
        }
        return true;
    };

    function setValidation(target, novalidate) {
        var state = $.data(target, "euploadify"), opts = state.options;
        if (novalidate != undefined && novalidate != null) {
            opts.novalidate = novalidate;
        }
        if (opts.novalidate) {
            state.wrapper.removeClass("euploadify-invalid");
            hideTip(target);
        }
        initializeValidate(target);
    };



    function getValues(target) {
        var state = $.data(target, "euploadify"), opts = state.options,
            fields = state.wrapper.find(".euploadify-value"),
            values = $.array.map(fields, function (val) { return $(val).val(); });
        state.values = values.join(opts.separator);
        return opts.values = values;
    };

    function getValue(target) {
        var values = getValues(target);
        return values[0];
    };


    function setValues(target, values) {
        var t = $(target), state = $.data(target, "euploadify"), opts = state.options,
            original = t.euploadify("getValues"),
            array = $.util.likeArrayNotString(values) ? values : [values];
        state.wrapper.find(".euploadify-value").remove();
        $.each(array, function (i, val) {
            var field = $("<input class=\"euploadify-value\" type=\"hidden\" />").appendTo(state.wrapper).val(val);
            if (state.name) {
                field.attr("name", state.name);
            }
        });
        var temps = $.array.filter(array, function (val) {
            return $.array.contains(original, val) ? false : true;
        });
        if (temps.length != array.length || array.length != original.length) {
            if (opts.multi) {
                opts.onChange.call(target, array, original);
            } else {
                opts.onChange.call(target, array[0], original[0]);
            }
        }
        state.values = array.join(opts.separator);
        opts.values = array;
    };

    function setValue(target, value) {
        setValues(target, [value]);
    };

    function clear(target) { setValues(target, []); };

    function reset(target) {
        var state = $.data(target, "euploadify"), opts = state.options;
        setValues(target, opts.originalValue);
    };




    function show(target) {
        var state = $.data(target, "euploadify");
        state.panel.panel("open");
        return getOptions(target);
    };

    function hide(target) {
        var state = $.data(target, "euploadify");
        state.panel.panel("close");
        return getOptions(target);
    };






    $.fn.euploadify = function (options, param) {
        if (typeof options == "string") {
            var method = $.fn.euploadify.methods[options];
            if (method) {
                return method(this, param);
            } else {
                return this.euploadify("panel").panel(options, param);
            }
        }
        options = options || {};
        return this.each(function () {
            var state = $.data(this, "euploadify");
            if (state) {
                $.extend(state.options, options);
            } else {
                $.data(this, "euploadify", { options: $.extend({}, $.fn.euploadify.defaults, $.fn.euploadify.parseOptions(this), options) });
                create(this);
            }
        });
    };



    function operateQueue(mini, method) {
        var btn = $(mini), t = btn.closest("div.euploadify-wrapper").find(".euploadify-f"),
            state = $.data(t[0], "euploadify"), opts = state.options;
        if (!opts.disabled && !state.wrapper.is(".euploadify-disabled")) {
            var index = window.parseInt(btn.closest("tr.datagrid-row").attr("datagrid-row-index")),
                row = state.grid.datagrid("getRowData", index);
            if (row.filestatus == -4) {
                var msg = {
                    cancel: opts.message.fileUploadSuccessCanntRemove,
                    upload: opts.message.fileUploadSuccess
                };
                $.easyui.messager.show(msg[method]);
            } else {
                state.uploadify.uploadify(method, row.id);
            }
        }
        if (window.event && window.event.stopPropagation) {
            window.event.stopPropagation();
        }
        return false;
    };

    $.fn.euploadify.cancelQueue = function (mini) {
        return operateQueue(mini, "cancel");
    };

    $.fn.euploadify.uploadQueue = function (mini) {
        return operateQueue(mini, "upload");
    };



    $.fn.euploadify.parseOptions = function (target) {
        return $.extend({}, $.parser.parseOptions(target, [
            "buttonClass", "buttonCursor", "buttonImage", "buttonText", "checkExisting", "fileObjName",
            "fileTypeDesc", "fileTypeExts", "itemTemplate", "method", "progressData", "queueID", "swf", "uploader",
            "uploadText", "stopText", "cancelText", "removeText", "emptyText", "finishText", "errorText",
            "buttonIcon", "stopIcon", "cancelIcon", "uploadIcon", "multiTemplate",
            "value", "width", "height", "missingMessage", "tipPosition", "separator",
            {
                auto: "boolean", debug: "boolean", multi: "boolean", preventCaching: "boolean", removeCompleted: "boolean", requeueErrors: "boolean",
                disabled: "boolean", buttonPlain: "boolean", showStop: "boolean", showCancel: "boolean", required: "boolean", novalidate: "boolean"
            },
            {
                fileSizeLimit: "number", height: "number", queueSizeLimit: "number", removeTimeout: "number", successTimeout: "number", uploadLimit: "number", width: "number",
                buttonWidth: "number", buttonHeight: "number", deltaX: "number"
            }
        ]));
    };





    //  提供外部调用公共方法 $.easyui.showEuploadifyDialog 用于快速创建 euploadify 文件上传对话框；
    //  该方法的参数 options 参考继承于方法 $.easyui.showDialog 和插件 easyui-euploadify 的参数格式，在在此基础上增加了如下描述的属性：
    //      title   : string
    //      value   : string
    //      onEnter : function(val, euploadify, uploadify, btn)
    //      onCancel: function(val, euploadify, uploadify, btn)
    //      ... 
    //  返回值：返回创建的 easyui-dialog 对象；
    $.easyui.showEuploadifyDialog = function (options) {
        if (options && options.topMost && $ != $.util.$ && $.util.$.easyui.showEuploadifyDialog) {
            return $.util.$.easyui.showEuploadifyDialog.apply(this, options);
        }
        var opts = $.extend({}, $.easyui.showEuploadifyDialog.defaults, options || {}),
            euploadify, uploadify, value = opts.value,
            dia = $.easyui.showDialog($.extend({}, opts, {
                content: "<input class=\"euploadify-dialog-container\" />",
                enableApplyButton: false, topMost: false,
                height: opts.height == "auto" ? (opts.multi ? 480 : 160) : opts.height,
                onBeforeDestroy: function () { if (euploadify) { euploadify.euploadify("destroy"); } },
                onSave: function () { return opts.onEnter.call(dia, getValue(), euploadify, uploadify, this); },
                onClose: function () { return opts.onCancel.call(dia, getValue(), euploadify, uploadify, this); }
            }));
        $.util.exec(function () {
            var container = dia.find(".euploadify-dialog-container").euploadify($.extend({}, opts, {
                fit: true, border: false, noheader: true, value: value
            }));
            dia.euploadify = euploadify = container;
            uploadify = container.euploadify("uploadify");
            euploadify.euploadify("resize");
        });
        function getValue() {
            if (!euploadify || !uploadify) { return value; }
            var eopts = euploadify.euploadify("options");
            return euploadify.euploadify(eopts.multi ? "getValues" : "getValue");
        };
        return dia;
    };

    $.easyui.showEuploadifyDialog.defaults = {
        title: "文件上传对话框",
        iconCls: "icon-hamburg-publish",
        width: 750, height: "auto", minWidth: 400, minHeight: 160,
        maximizable: true, collapsible: true,
        multi: false, value: null,
        onEnter: function (val, editor, btn) { },
        onCancel: function (val, editor, btn) { }
    };








    $.fn.euploadify.methods = {

        options: function (jq) { return getOptions(jq[0]); },

        panel: function (jq) { return $.data(jq[0], "euploadify").panel; },

        uploadify: function (jq) { return getUploadify(jq[0]); },

        buttons: function (jq) { return getButtons(jq[0]); },

        queueDOM: function (jq) { return getQueueDOM(jq[0]); },

        grid: function (jq) { return getGrid(jq[0]); },

        //  从当前上传队列中取消一个或多个文件的上传；该方法的参数 param 可以定义为如下数据类型：
        //      String  : 表示要取消上传的文件的 id 值；如果不定义该参数，则取消队列中第一个文件的上传；如果该值定义为 "*"，则取消队列中所有文件上传；
        //      Boolean : 默认为 false；如果定义为 true，则执行该方法时将不触发 onUploadCancel 事件；
        //  返回值：返回表示当前 easyui-euploadify 控件的 jQuery 链式对象。
        cancel: function (jq, param) { return jq.each(function () { cancel(this, param); }); },

        //  从当前文档上下文中销毁该 easyui-euploadify 控件；该方法将触发 onDestroy 事件；
        //  返回值：返回表示当前 easyui-euploadify 控件的 jQuery 链式对象。
        destroy: function (jq) { return jq.each(function () { destroy(this); }); },

        //  禁用该 easyui-euploadify 控件；该方法将触发 onDisable 事件；
        //      该方法的参数 setDisabled 为 Boolean 类型值，为 true 时表示禁用上传按钮；为 false 表示启用上传按钮；
        //  注意：该方法会导致当前的上传任务停止；
        //  返回值：返回表示当前 easyui-euploadify 控件的 jQuery 链式对象。
        disable: function (jq, setDisabled) { return jq.each(function () { disable(this, setDisabled); }); },

        //  获取或设置 easyui-euploadify 控件中指定名称的属性值；该方法的参数 param 可以定义为如下数据类型：
        //      String  : 该参数指定一个属性名称，该属性名所示的属性值将会被返回；
        //      Object  : 格式如 { name: string, value: object }；设定指定名称的属性为指定的值。
        //  返回值：返回表示当前 easyui-euploadify 控件中指定名称的属性值；或者返回表示当前 easyui-euploadify 控件的 jQuery 链式对象。
        settings: function (jq, param) { return settings(jq[0], param); },

        //  停止当前上传队列中所有文件的上传。
        //  返回值：返回表示当前 easyui-euploadify 控件的 jQuery 链式对象。
        stop: function (jq) { return jq.each(function () { stop(this); }); },

        //  获取当前上传队列中有多少文件数量；
        //  返回值：返回一个 array 类型数组，数组中的每一项都是一个 file 对象；
        queues: function (jq) { return getQueues(jq[0]); },

        //  立即上传当前队列中的特定文件或所有文件。该方法的参数 param 可以定义为如下数据类型：
        //      String  : 表示要立即上传的文件 id 值；如果不定义该参数，则上传队列中的第一个文件；如果该值定义为 "*"，则上传队列中所有文件；
        //  返回值：返回表示当前 easyui-euploadify 控件的 jQuery 链式对象。
        upload: function (jq, param) { return jq.each(function () { upload(this, param); }); },



        setFormData: function (jq, param) { return jq.each(function () { setFormData(this, param); }); },

        resetFormData: function (jq) { return jq.each(function () { resetFormData(this); }); },

        clearFormData: function (jq) { return jq.each(function () { clearFormData(this); }); },

        resize: function (jq, param) { return jq.each(function () { resize(this, param); }); },

        enable: function (jq) { return jq.each(function () { enable(this); }); },



        validate: function (jq) { return jq.each(function () { validate(this); }); },

        isValid: function (jq) { return validate(jq[0]); },

        enableValidation: function (jq) { return jq.each(function () { setValidation(this, false) }); },

        disableValidation: function (jq) { return jq.each(function () { setValidation(this, true) }); },

        clear: function (jq) { return jq.each(function () { clear(this); }); },

        reset: function (jq) { return jq.each(function () { reset(this); }); },

        getValues: function (jq) { return getValues(jq[0]); },

        setValues: function (jq, values) { return jq.each(function () { setValues(this, values); }); },

        getValue: function (jq) { return getValue(jq[0]); },

        setValue: function (jq, value) { return jq.each(function () { setValue(this, value); }); },


        show: function (jq) { return jq.each(function () { show(this); }); },

        hide: function (jq) { return jq.each(function () { hide(this); }); }
    };

    $.fn.euploadify.defaults = {


        //  表示 "上传" 按钮的宽度(像素值)；
        width: 120,

        //  表示 "上传" 按钮的高度(像素值)；
        height: 30,

        //  表示当选择了待上传文件后，是否自动执行 upload 方法以上传文件；
        auto: true,

        //  表示 uploadify.swf SWFObject Flash 对象的相对路径；该参数必须设置正确，否则上传控件将无效；
        swf: "uploadify.swf",

        //  表示服务器端接收上传数据的 url 相对路径；该参数必须设置正确，否则上传控件将无效；
        uploader: "uploadify.php",

        //  表示能够上传文件的最大数量；当文件数超出此限制时，将会触发 onUploadError 事件；
        uploadLimit: 999,

        //  表示文件上传时进行 ajax 提交的方法；可选的值为 "post" 或 "get"；
        method: "post",

        //  表示文件在上传过程中，每一个文件块的上传超时事件(秒)；
        successTimeout: 30,

        //  表示被添加到 "上传" 按钮的 html-css 样式类名
        buttonClass: "",

        //  表示当鼠标移动至 "上传" 按钮时，鼠标显示的样式，可能的值为 "hand" 或 "arrow"
        buttonCursor: "hand",

        //  表示 "上传" 按钮附加的背景图片；如果需要设置一个 "上传" 按钮的鼠标悬停时显示的图标，可以用 buttonClass 属性并定义一个具有 hover 效果的 css；
        buttonImage: null,

        //  表示 "上传" 按钮显示的按钮文本；该值也可以定义为 html 标签包含的文本内容，例如 <b>SELECT FILES</b>；
        buttonText: "SELECT FILES",

        //  表示是否校验服务器目标文件夹中已经存在待上传的文件名；
        //      该值如果定义为布尔值 false，则表示不进行校验；
        //      如果定义为一个服务器 url 地址例如 "/uploadify/check-exists.php"，则表示进行服务器校验；该地址如果返回 1 则表示服务器上存在指定的文件名；如果返回 0 则表示不存在。
        checkExisting: false,

        // 表示是否开启 SWFUpload 对象的调试模式
        debug: false,

        //  表示待上传文件对象提交至服务器时，采用的表单字段名；例如当该值定义为 "Filedata" 时，ASP.NET 服务器端可以用 HttpContext.Current.Request["Filedata"] 获取文件的二进制对象；
        fileObjName: "Filedata",

        //  表示文件上传大小上限；
        //      该值可以定义为一个 Number 值，表示文件最大字节数(KB为单位)；
        //      也可以定义为一个以 "KB"、"MB" 或 "GB" 结尾的 String 值；
        //  如果该属性值为 0，则表示不限制文件的上传大小；
        fileSizeLimit: 0,

        //  表示可选择的上传文件类型的描述；
        fileTypeDesc: "All Files",

        //  表示可选择的上传文件的扩展名列表，多个扩展名用半角分号隔开，例如 "*.jpg; *.png; *.gif"
        //  注意：手动输入的文件名将会绕过此验证规则，因此在服务端还需要进行文件类型的校验；
        fileTypeExts: "*.*",

        //  表示随待上传文件被提交至服务器端时，被一并发送至服务器的表单参数；可以在 onUploadStart 事件中动态设置这些参数；
        formData: {},

        //  表示上传文件队列的 HTML 页面显示模板；该值定义为 false 表示按照默认格式显示；该模版中可以使用如下四个变量标签：
        //      instanceID:
        //      fileID:
        //      fileName:
        //      fileSize:
        //  模板使用变量标签的格式如：${fileName}.
        itemTemplate: false,


        //  表示是否能够同时上传多个文件；
        multi: true,

        //  一个 array: <string> 格式数组，数组中的每一项都是一个表示 uploadify 事件名称的字符串；
        //  该属性表示哪些事件在 uploadify 的生命周期中将不会被触发执行；
        overrideEvents: [],

        //  表示浏览器是否缓存 SWFObject；如果该值设置为 false，则 SWF 文件的 url 中将会被添加一个随机值参数，以实现 SWFObject 不缓存效果；
        preventCaching: true,

        //  表示文件上传时的文件上传进度显示方式；
        //      percentage: 表示上传时显示上传进度百分比
        //      speed:  表示上传时显示上传速度
        progressData: "percentage",

        //  一个 HTML-DOM 元素的 ID 值，用于作为文件上传队列显示的容器；如果定义该值，则文件上传队列中每个上传的元素将会被附加到这个 DOM 对象中；
        //      如果该值定义为 false，则文件上传队列显示容器将会动态生成；
        queueID: false,

        //  表示能够同时执行上传操作的文件最大数量；这并不限制该控件可上传文件的最大数量；要限制可上传文件最大数量请用 uploadLimit 参数；
        //      如果添加到上传队列中的文件数量超过此限制，将会触发 onSelectError 事件；
        queueSizeLimit: 999,

        //  表示是否可以移除已经上传完成的文件；
        removeCompleted: true,

        //  表示文件上传完成后将会延迟多少秒后采用上传队列中被移除；
        removeTimeout: 3,

        //  表示文件在上传过程中如果出错是否自动重新上传；
        requeueErrors: false,




        //  当上传队列中的一个或多个文件被执行 cancel 方法而从队列中取消时，该事件将会被触发；
        //      file    : object 类型值；表示被取消上传的文件对象；
        onCancel: function (file) { },

        //  当 cancel 方法被执行并且参数值为 "*"，该事件将会被触发；
        //      queueItemCount: number 类型值；表示取消上传的文件总数量
        onClearQueue: function () { },

        //  当 destroy 方法被调用时，该事件将会被触发；
        onDestroy: function () { },

        //  当浏览文件对话框被关闭时，该事件将会被触发；如果该事件会添加至 overrideEvents 属性，则在将文件添加到上传队列中时如果出现错误，将不会弹出警告消息；
        //      queueData: object 类型值，格式如 { filesSelected: number, filesQueued: number, filesReplaced: number, filesCancelled: number, filesErrored: number }
        onDialogClose: function (queueData) { },

        //  当打开浏览文件对话框后，该事件将会被触发；注意，该事件函数不会在浏览文件对话框打开时被立即执行，而是在窗口关闭时执行。
        onDialogOpen: function () { },

        //  当 disable 方法被调用时，该事件将会被触发；
        onDisable: function () { },

        //  当 disable 方法被调用并且 setDisabled 值为 false 以启用按钮时，该事件将会被触发；
        onEnable: function () { },

        //  在上传控件初始化过程中，如果当前浏览器的 Flash 插件版本不兼容，该事件将会被触发；
        onFallback: function () { },

        //  在 easyui-euploadify 控件第一次被初始化完成后，该事件将会被触发；
        //      instance:   表示 uploadify 对象；
        onInit: function (instance) { },

        //  当上传队列中所有文件被处理完成后，该事件将会被触发；
        //      queueData:  object 类型值，格式如 { uploadsSuccessful: number, uploadsErrored: number }
        onQueueComplete: function (queueData) { },

        //  当打开文件浏览器对话框并选择了要上传的文件之后，该事件将会被触发；
        //      file:   表示被选择的待上传文件；
        onSelect: function (file) { },

        //  当打开文件浏览器对话框并在选择文件出错后，该事件将会被触发；针对每个选择文件出错后，该事件都会被触发一次；
        //      file:   表示触发异常事件的文件对象；
        //      errorCode:表示错误代码，可能的值为如下几种：
        //          QUEUE_LIMIT_EXCEEDED:   选定的文件数量超过限制；
        //          FILE_EXCEEDS_SIZE_LIMIT:选定的文件大小超过限制；
        //          INVALID_FILETYPE:       选定的文件类型超过限制；
        //      errorMsg:表示触发该事件时的错误消息；
        onSelectError: function (file, errorCode, errorMsg) { },

        //  当该控件的 Flash 插件对象加载完成后，该事件将会被触发；
        onSWFReady: function () { },

        //  当文件上传完成(不管成功还是失败)，该事件将会被触发；针对每个上传的文件，该事件都会触发一次；
        //      file:   表示触发该事件的文件对象；
        onUploadComplete: function (file) { },

        //  当文件上传失败后，该事件将会被触发；针对每个上传失败的文件，该事件都会触发一次；
        //      file:   表示触发该事件的文件对象；
        //      errorCode:  表示错误编号
        //      errorMsg:   表示错误消息
        //      errorString:表示错误消息字符串，可能包含所有的错误细节的可读内容；
        onUploadError: function (file, errorCode, errorMsg, errorString) { },

        //  当文件上传进度每次更新时，该事件将会被触发；针对每个上传文件的每次进度更新，该事件都会触发一次；
        //      file:   表示触发该事件的文件对象；
        //      bytesUploaded: 表示该文件自开始上传时至当前时刻已上传的字节总数；
        //      bytesTotal:    表示该文件的字节总数；
        //      totalBytesUploaded: 表示目前为止所有文件的上传字节总数；
        //      totalBytesTotal: 表示所有文件的字节总数
        onUploadProgress: function (file, bytesUploaded, bytesTotal, totalBytesUploaded, totalBytesTotal) { },

        //  当文件上传进度每次更新时，该事件将会被触发；
        //      file:   表示将要被上传的文件对象；
        onUploadStart: function (file) { },

        //  当文件上传成功完成时，该事件将会被触发；针对每个上传成功的文件，该事件都会触发一次；
        //      file:   表示上传完成的文件对象；
        //      data:   文件上传完成后，由服务器端返回的数据；
        //      response: 表示文件是否上传成功；boolean 类型值；如果为 true 则表示文件上传成功；
        //          如果该值为 false，则表示当提交上传后并超过 successTimeout 属性设置的超时时间后，服务器任未有数据返回(文件上传失败)；
        onUploadSuccess: function (file, data, response) { }
    };


    $.extend($.fn.euploadify.defaults, {

        successTimeout: 3600,
        removeCompleted: false,
        formData: { folder: "uploads" },
        buttonText: "选择文件...",
        multi: false,
        auto: true,

        uploadText: "上传",
        stopText: "停止上传",
        cancelText: "取消上传",
        removeText: "移除选择",

        emptyText: "未选择文件",
        finishText: "上传完成!",
        errorText: "上传失败",

        //  设置按钮提示文字；
        //  该属性可以为一个格式如 { button: string, upload: string, stop: string, cancel: string, remove: string } 的对象，表示每个按钮的提示文字内容；
        //  也可以是一个 boolean 类型值，表示是否启用按钮文字提示；
        tooltip: {
            button: "打开文件浏览器窗口并选择需要上传的文件",
            upload: "上传当前文件队列中所有未上传的文件",
            stop: "停止当前文件上传队列中所有文件的上传动作",
            cancel: "取消当前文件队列中所有未上传文件的上传任务",
            remove: "取消选定行的上传任务并删除该行"
        },
        message: {
            queueEmpty: "当前上传队列中没有待上传文件.",
            mustNotEmpty: "请至少选择一行数据.",
            fileUploadSuccessCanntCancel: "该文件已经上传完成，无法取消！",
            fileUploadSuccess: "该文件已经上传完成！",
            fileCannotRename: "该文件处于\"{0}\"状态，不能编辑\"自定义文件名\"，编辑状态已撤销.",
            fileNameCannotEmpty: "自定义文件名不能为空（已经恢复）",
            fileUploadSuccessCanntRemove: "该文件已经上传完成，无法取消(如想移除该行请选中该行后点击上方按钮\"移除选择\")！"
        },
        gridColumnNames: {
            name: "全文件名", customeName: "自定义文件名", type: "类型", size: "大小",
            progress: "上传进度", progressValue: "进度值", filestatus: "状态", operate: "操作"
        },
        gridRowButtonNames: {
            cancel: "取消", upload: "上传"
        },
        gridRowContextNames: {
            edit: "编辑文件名", cancel: "取消该文件", upload: "上传该文件"
        },

        FILE_STATUS: { "-1": "等待上传", "-2": "正在上传...", "-3": "上传出错", "-4": "上传完成", "-5": "已取消" },

        buttonIcon: "icon-search",
        stopIcon: "icon-hamburg-stop",
        cancelIcon: "icon-standard-cancel",
        uploadIcon: "icon-hamburg-publish",

        response: { "id": null, "status": false, "message": null, "value": null, "url": null },
        requestFilter: function (data, file) { return data; },
        responseFilter: function (data, file) {
            return $.util.tryExec(function () { return $.util.parseJSON(data); });
        },

        //  在设置控件允许可以同时上传多个文件时(multi: true)，多文件列表的显示方式；String 类型值，可选的值限定如下范围：
        //      uploadify:
        //      simple:
        //      list:
        //      grid:
        multiTemplate: "grid",

        multiTemplateData: {
            uploadify: $.fn.euploadify.defaults.itemTemplate,
            simple: "<div id=\"${fileID}\" class=\"uploadify-queue-item\">\
            		<div class=\"cancel\">\
            			<a href=\"javascript:$(\'#${instanceID}\').uploadify(\'cancel\', \'${fileID}\')\">X</a>\
            		</div>\
            		<span class=\"fileName\">${fileName} (${fileSize})</span><span class=\"data\"></span>\
            	 </div>",
            bootstrap: ""
        },

        value: null,

        disabled: false,

        buttonPlain: true,

        width: "auto",

        height: "auto",

        buttonWidth: 90,

        buttonHeight: 24,

        showStop: false,

        showCancel: false,

        required: false,
        missingMessage: "附件(文件/档)不能为空,请上传.",
        //  "left", "right", "top", "bottom"
        tipPosition: "right",
        deltaX: 0,
        tipOptions: $.fn.validatebox.defaults.tipOptions,
        separator: ",",
        novalidate: false,


        onChange: function (newValue, oldValue) { },

        onButtonStop: function (queues, btnDOM) { }
    });


    $.parser.plugins.push("euploadify");

    if ($.fn.form && $.isArray($.fn.form.otherList)) {
        $.fn.form.otherList.push("euploadify");
        //$.array.insert($.fn.form.otherList, 0, "euploadify");
    }

})(jQuery);