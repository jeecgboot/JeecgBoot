/**
* jQuery EasyUI 1.3.6
* Copyright (c) 2009-2014 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL or commercial licenses
* To use it on other terms please contact author: info@jeasyui.com
* http://www.gnu.org/licenses/gpl.txt
* http://www.jeasyui.com/license_commercial.php
*
* 依赖项：
*   1、jquery.jdirk.js v1.0 beta late
*   2、jeasyui.extensions.js v1.0 beta late
*   3、jeasyui.extensions.menu.js v1.0 beta late
*   4、jeasyui.extensions.panel.js v1.0 beta late 和 jeasyui.extensions.window.js v1.0 beta late(可选)
*
* Copyright (c) 2013-2014 ChenJianwei personal All rights reserved.
* http://www.chenjianwei.org
*/
(function ($, undefined) {

    $.fn.datagrid.extensions = {};

    /************************  initExtend Methods Begin  ************************/

    var _updateRow = $.fn.datagrid.methods.updateRow;
    var _appendRow = $.fn.datagrid.methods.appendRow;
    var _insertRow = $.fn.datagrid.methods.insertRow;
    var updateRow = function (target, param) {
        if (!param || !param.row || !$.isNumeric(param.index)) { return; }
        var t = $(target), opts = t.datagrid("options");
        if ($.isFunction(opts.onBeforeUpdateRow) && opts.onBeforeUpdateRow.call(target, param.index, param.row) == false) { return; }
        _updateRow.call(t, t, param);
        initHeaderColumnFilterContainer(t, opts);
        initRowDndExtensions(t, opts);
        initColumnRowTooltip(t, opts, param.index, param.row);
        if ($.isFunction(opts.onUpdateRow)) { opts.onUpdateRow.call(target, param.index, param.row); }
    };
    var appendRow = function (target, row) {
        if (!row) { return; }
        var t = $(target), opts = t.datagrid("options");
        if ($.isFunction(opts.onBeforeAppendRow) && opts.onBeforeAppendRow.call(target, row) == false) { return; }
        _appendRow.call(t, t, row);
        var rows = t.datagrid("getRows"), index = rows.length - 1;
        initHeaderColumnFilterContainer(t, opts);
        initRowDndExtensions(t, opts);
        initColumnRowTooltip(t, opts, index, row);
        if ($.isFunction(opts.onAppendRow)) { opts.onAppendRow.call(target, row); }
    };
    var insertRow = function (target, param) {
        if (!param || !param.row || !$.isNumeric(param.index)) { return; }
        var t = $(target), opts = t.datagrid("options");
        if ($.isFunction(opts.onBeforeInsertRow) && opts.onBeforeInsertRow.call(target, param.index, param.row) == false) { return; }
        _insertRow.call(t, t, param);
        initHeaderColumnFilterContainer(t, opts);
        initRowDndExtensions(t, opts);
        initColumnRowTooltip(t, opts, param.index, param.row);
        if ($.isFunction(opts.onInsertRow)) { opts.onInsertRow.call(target, param.index, param.row); }
    };


    var _beginEdit = $.fn.datagrid.methods.beginEdit;
    var beginEdit = function (target, index) {
        var t = $(target), opts = t.datagrid("options"), ret = _beginEdit.call(t, t, index);
        if (opts.autoFocusField) {
            var editors = t.datagrid("getEditors", index);
            if (editors.length) {
                var editor = $.array.first(editors, function (val) { return val.field == opts.autoFocusField; });
                if (!editor) { editor = editors[0]; }
                if (editor) {
                    $.util.exec(function () {
                        if (editor.actions && $.isFunction(editor.actions.setFocus)) {
                            editor.actions.setFocus(editor.target[0]);
                        } else {
                            editor.target.focus();
                        }
                    });
                }
            }
        }
        return ret;
    };




    var isChecked = function (target, index) {
        var t = $(target), rows = t.datagrid("getChecked"),
            list = $.array.map(rows, function (val) { return t.datagrid("getRowIndex", val); });
        return $.array.contains(list, index);
    };

    var isSelected = function (target, index) {
        var t = $(target), rows = t.datagrid("getSelections"),
            list = $.array.map(rows, function (val) { return t.datagrid("getRowIndex", val); });
        return $.array.contains(list, index);
    };

    var isEditing = function (target, index) {
        var t = $(target), panel = t.datagrid("getPanel");
        return panel.find("div.datagrid-view div.datagrid-body table tr.datagrid-row[datagrid-row-index=" + index + "]").hasClass("datagrid-row-editing");
    };

    var getEditingRowIndex = function (target) {
        var array = getEditingRowIndexs(target);
        return array.length ? array[0] : -1;
    };

    var getEditingRowIndexs = function (target) {
        var t = $(target), panel = t.datagrid("getPanel"),
            rows = panel.find("div.datagrid-view div.datagrid-body table tr.datagrid-row.datagrid-row-editing").map(function () {
                return window.parseInt($(this).attr("datagrid-row-index"));
            }),
            array = $.array.distinct($.array.clone(rows));
        return array;
    };

    var freezeColumn = function (target, field) {
        var t = $(target), fields = t.datagrid("getColumnFields"), frozenFields = t.datagrid("getColumnFields", true);
        if (!frozenFields || !frozenFields.length || !$.array.contains(fields, field) || $.array.contains(frozenFields, field)) { return; }
        t.datagrid("moveColumn", { source: field, target: frozenFields[frozenFields.length - 1], point: "after" });
    };

    var unfreezeColumn = function (target, field) {
        var t = $(target), fields = t.datagrid("getColumnFields"), frozenFields = t.datagrid("getColumnFields", true);
        if (!fields || !fields.length || $.array.contains(fields, field) || !$.array.contains(frozenFields, field)) { return; }
        t.datagrid("moveColumn", { source: field, target: fields[0], point: "before" });
    };

    var moveRow = function (target, param) {
        if (!param || !$.isNumeric(param.source) || !$.isNumeric(param.target) || param.source == param.target || !param.point) { return; }
        if (!$.array.contains(["top", "bottom"], param.point)) { param.point = "top"; }
        var t = $(target), opts = t.datagrid("options"), rows = t.datagrid("getRows"),
            sourceRow = rows[param.source], targetRow = rows[param.target];
        if (!sourceRow || !targetRow) { return; }
        if ($.isFunction(opts.onBeforeDrop) && opts.onBeforeDrop.call(target, targetRow, sourceRow, param.point) == false) { return; }
        var row = t.datagrid("popRow", param.source), index = t.datagrid("getRowIndex", targetRow);
        rows = t.datagrid("getRows");
        switch (param.point) {
            case "top": t.datagrid("insertRow", { index: index, row: row }); break;
            case "bottom":
                if (index++ >= rows.length) {
                    t.datagrid("appendRow", row);
                } else {
                    t.datagrid("insertRow", { index: index, row: row });
                }
                break;
            default: break;
        }
        if (row && $.isFunction(opts.onDrop)) { opts.onDrop.call(target, targetRow, sourceRow, param.point); }
    };

    var shiftRow = function (target, param) {
        if (!param || !$.isNumeric(param.index) || !param.point || !$.array.contains(["up", "down"], param.point)) { return; }
        var t = $(target), opts = t.datagrid("options"), index = param.point == "up" ? param.index - 1 : param.index + 1,
            point = param.point == "up" ? "top" : "bottom";
        t.datagrid("moveRow", { source: param.index, target: index, point: point });
    };

    var getNextRow = function (target, index) {
        var t = $(target), rows = t.datagrid("getRows"), i = index + 1;
        return rows[i] ? rows[i] : null;
    };

    var getPrevRow = function (target, index) {
        var t = $(target), rows = t.datagrid("getRows"), i = index - 1;
        return rows[i] ? rows[i] : null;
    };

    var popRow = function (target, index) {
        var t = $(target), rows = t.datagrid("getRows"), row = rows[index];
        if (!row) { return null; }
        t.datagrid("deleteRow", index);
        return row;
    };

    var enableRowDnd = function (target) {
        var t = $(target), opts = t.datagrid("options");
        t.datagrid("getPanel").find("div.datagrid-view div.datagrid-body table tr.datagrid-row").draggable({
            disabled: false, revert: true, cursor: "default", deltaX: 10, deltaY: 5,
            proxy: function (source) {
                var tr = $(source), index = parseInt(tr.attr("datagrid-row-index")),
                    dom = t.datagrid("getRowDom", index).clone(),
                    temp = $("<tr></tr>").addClass("datagrid-row datagrid-row-selected");
                $("<td><span class='tree-dnd-icon tree-dnd-no' ></span></td>").appendTo(temp);
                var td = dom.find("td").each(function (i) { if (i < 6) { temp.append(this); } });
                if (td.length > 6) { $("<td>...</td>").css("width", "40px").appendTo(temp); }
                return $("<table></table>").addClass("tree-node-proxy").appendTo("body").append(temp).hide();
            }, onBeforeDrag: function (e) {
                var tr = $(this), index = parseInt(tr.attr("datagrid-row-index")), row = t.datagrid("getRowData", index);
                if ($.isFunction(opts.onBeforeDrag) && opts.onBeforeDrag.call(target, index, row) == false) { return false; }
                if (e.which != 1) { return false; }
                if (e.target.type == "checkbox") { return false; }
            }, onStartDrag: function () {
                var tr = $(this), index = parseInt(tr.attr("datagrid-row-index")), row = t.datagrid("getRowData", index);
                tr.draggable("proxy").css({ left: -10000, top: -10000 });
                if ($.isFunction(opts.onBeforeDrag)) { opts.onStartDrag.call(target, index, row); }
            }, onStopDrag: function () {
                var tr = $(this), index = parseInt(tr.attr("datagrid-row-index")), row = t.datagrid("getRowData", index);
                if ($.isFunction(opts.onStopDrag)) { opts.onStopDrag.call(target, index, row); }
            }, onDrag: function (e) {
                var x1 = e.pageX, y1 = e.pageY, x2 = e.data.startX, y2 = e.data.startY;
                var d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
                if (d > 15) { $(this).draggable("proxy").show(); }
                this.pageY = e.pageY;
            }
        }).droppable({
            accept: "tr.datagrid-row",
            onDragEnter: function (e, source) {
                var droper = $(this), drager = $(source),
                    droperIndex = parseInt(droper.attr("datagrid-row-index")),
                    dragerIndex = parseInt(drager.attr("datagrid-row-index")),
                    droperRow = t.datagrid("getRowData", droperIndex), dragerRow = t.datagrid("getRowData", dragerIndex),
                    droperRowDom = t.datagrid("getRowDom", droperIndex),
                    mark = droperRowDom.find("td");
                var dnd = droper.data("dnd"), data = {
                    droper: droper, drager: drager, droperIndex: droperIndex, dragerIndex: dragerIndex,
                    droperRow: droperRow, dragerRow: dragerRow, droperRowDom: droperRowDom, mark: mark
                };
                if (!dnd) { droper.data("dnd", data); } else { $.extend(dnd, data); }
                if ($.isFunction(opts.onDragEnter) && opts.onDragEnter.call(target, droperRow, dragerRow) == false) {
                    setDroppableStatus(drager, false);
                    mark.removeClass("datagrid-header-cell-top datagrid-header-cell-bottom");
                    droper.droppable("disable");
                }
            },
            onDragOver: function (e, source) {
                var droper = $(this), dnd = droper.data("dnd"), drager = dnd.drager,
                    droperRow = dnd.droperRow, dragerRow = dnd.dragerRow, mark = dnd.mark;
                if (droper.droppable("options").disabled) { return; }
                var pageY = source.pageY, top = droper.offset().top, height = top + droper.outerHeight();
                setDroppableStatus(drager, true);
                mark.removeClass("datagrid-header-cell-top datagrid-header-cell-bottom");
                if (pageY > top + (height - top) / 2) {
                    mark.addClass("datagrid-header-cell-bottom");
                } else {
                    mark.addClass("datagrid-header-cell-top");
                }
                if (opts.onDragOver.call(target, droperRow, dragerRow) == false) {
                    setDroppableStatus(drager, false);
                    mark.removeClass("datagrid-header-cell-top datagrid-header-cell-bottom");
                    droper.droppable("disable");
                }
            },
            onDragLeave: function (e, source) {
                var droper = $(this), dnd = droper.data("dnd"), drager = dnd.drager,
                    droperRow = dnd.droperRow, dragerRow = dnd.dragerRow, mark = dnd.mark;
                setDroppableStatus(drager, false);
                mark.removeClass("datagrid-header-cell-top datagrid-header-cell-bottom");
                if ($.isFunction(opts.onDragLeave)) { opts.onDragLeave.call(target, droperRow, dragerRow); }
            },
            onDrop: function (e, source) {
                var droper = $(this), dnd = droper.data("dnd"),
                    droperIndex = dnd.droperIndex, dragerIndex = dnd.dragerIndex, mark = dnd.mark,
                    point = mark.hasClass("datagrid-header-cell-top") ? "top" : "bottom";
                t.datagrid("moveRow", { target: droperIndex, source: dragerIndex, point: point });
                mark.removeClass("datagrid-header-cell-top datagrid-header-cell-bottom");
            }
        });
        opts.dndRow = true;
        function setDroppableStatus(source, state) {
            var icon = source.draggable("proxy").find("span.tree-dnd-icon");
            icon.removeClass("tree-dnd-yes tree-dnd-no").addClass(state ? "tree-dnd-yes" : "tree-dnd-no");
        };
    };

    var disableRowDnd = function (target) {
        var t = $(target), opts = t.datagrid("options");
        t.datagrid("getPanel").find("div.datagrid-view div.datagrid-body table tr.datagrid-row").draggable("disable");
        opts.dndRow = false;
    };



    var getNextColumn = function (target, field) {
        var t = $(target),
            fields = $.array.merge([], t.datagrid("getColumnFields", true), t.datagrid("getColumnFields", false)),
            index = $.array.indexOf(fields, field);
        if (index == -1 || index + 1 >= fields.length) { return null; }
        return t.datagrid("getColumnOption", fields[index + 1]);
    };

    var getPrevColumn = function (target, field) {
        var t = $(target),
            fields = $.array.merge([], t.datagrid("getColumnFields", true), t.datagrid("getColumnFields", false)),
            index = $.array.indexOf(fields, field);
        if (index < 1) { return null; }
        return t.datagrid("getColumnOption", fields[index - 1]);
    };


    var moveColumn = function (target, param) {
        if (!param || !param.source || !param.target || param.source == param.target || !param.point) { return; };
        if (!$.array.contains(["before", "after"], param.point)) { param.point = "before"; }
        var t = $(target);
        if (t.datagrid("hasMuliRowHeader")) { return; }
        var opts = t.datagrid("options"), sourceFrozen, targetFrozen,
            fields = t.datagrid("getColumnFields"), frozenFields = t.datagrid("getColumnFields", true);
        if ($.array.contains(fields, param.source)) { sourceFrozen = false; }
        if (sourceFrozen == undefined && $.array.contains(frozenFields, param.source)) { sourceFrozen = true; }
        if ($.array.contains(fields, param.target)) { targetFrozen = false; }
        if (targetFrozen == undefined && $.array.contains(frozenFields, param.target)) { targetFrozen = true; }
        if (sourceFrozen == undefined || targetFrozen == undefined) { return; }
        if ($.isFunction(opts.onBeforeMoveColumn) && opts.onBeforeMoveColumn.call(target, param.source, param.target, param.point) == false) { return; }
        var panel = t.datagrid("getPanel"), view = panel.find("div.datagrid-view"),
            view1 = view.find("div.datagrid-view1"), view2 = view.find("div.datagrid-view2"),
            headerRow1 = view1.find("div.datagrid-header table tr.datagrid-header-row"),
            headerRow2 = view2.find("div.datagrid-header table tr.datagrid-header-row"),
            borderRow1 = view1.find("div.datagrid-body table tr.datagrid-row"),
            borderRow2 = view2.find("div.datagrid-body table tr.datagrid-row"),
            sourceHeaderTd = sourceFrozen ? headerRow1.find("td[field=" + param.source + "]") : headerRow2.find("td[field=" + param.source + "]"),
            targetHeaderTd = targetFrozen ? headerRow1.find("td[field=" + param.target + "]") : headerRow2.find("td[field=" + param.target + "]"),
            sourceRow = sourceFrozen ? borderRow1 : borderRow2,
            targetRow = targetFrozen ? borderRow1 : borderRow2;
        if (sourceRow.length != targetRow.length) { return; }
        targetHeaderTd[param.point](sourceHeaderTd);
        targetRow.each(function (i, n) {
            var targetBodyTd = $(this).find("td[field=" + param.target + "]"), sourceBodyTd = $(sourceRow[i]).find("td[field=" + param.source + "]");
            targetBodyTd[param.point](sourceBodyTd);
        });
        var sourceOpts = t.datagrid("getColumnOption", param.source), targetOpts = t.datagrid("getColumnOption", param.target),
            sourceColumns = sourceFrozen ? opts.frozenColumns[0] : opts.columns[0],
            targetColumns = targetFrozen ? opts.frozenColumns[0] : opts.columns[0],
            exts = opts._extensionsDatagrid ? opts._extensionsDatagrid : (opts._extensionsDatagrid = {});
        $.array.remove(sourceColumns, sourceOpts);
        var index = $.array.indexOf(targetColumns, targetOpts);
        if (index > -1) { $.array.insert(targetColumns, param.point == "before" ? index : index + 1, sourceOpts); }
        t.datagrid("fixColumnSize");
        if (sourceFrozen) {
            if (!targetFrozen) {
                index = $.array.indexOf(exts.fields, param.target);
                $.array.insert(exts.fields, param.point == "before" ? index : index + 1, param.source);
                $.array.insert(exts.fieldOptions, param.point == "before" ? index : index + 1, sourceOpts);
                $.array.insert(exts.fieldOptionsBackup, param.point == "before" ? index : index + 1, $.extend({}, sourceOpts));
            }
        }
        if (!sourceFrozen) {
            index = $.array.indexOf(exts.fields, param.source);
            if (targetFrozen) {
                $.array.removeAt(exts.fields, index);
                $.array.removeAt(exts.fieldOptions, index);
                $.array.removeAt(exts.fieldOptionsBackup, index);
            } else {
                var fieldOpts = exts.fieldOptions[index], fieldOptsBak = exts.fieldOptionsBackup[index];
                $.array.removeAt(exts.fields, index);
                $.array.removeAt(exts.fieldOptions, index);
                $.array.removeAt(exts.fieldOptionsBackup, index);
                index = $.array.indexOf(exts.fields, param.target);
                $.array.insert(exts.fields, param.point == "before" ? index : index + 1, param.source);
                $.array.insert(exts.fieldOptions, param.point == "before" ? index : index + 1, fieldOpts);
                $.array.insert(exts.fieldOptionsBackup, param.point == "before" ? index : index + 1, fieldOptsBak);
            }
        }
        if ($.isFunction(opts.onMoveColumn)) { opts.onMoveColumn.call(target, param.source, param.target, param.point); }
    }

    var shiftColumn = function (target, param) {
        if (!param || !param.field || !param.point) { return; };
        if (!$.array.contains(["before", "after"], param.point)) { param.point = "before"; }
        var t = $(target), fields = t.datagrid("getColumnFields", "all"),
            index = $.array.indexOf(fields, param.field);
        if (index == -1 || (param.point == "before" && index == 0) || (param.point == "after" && index == fields.length - 1)) { return; }
        var target = fields[param.point == "before" ? index - 1 : index + 1];
        t.datagrid("moveColumn", { source: param.field, target: target, point: param.point });
    };


    var deleteColumn = function (target, field) {
        var t = $(target), opts = t.datagrid("options"),
            exts = opts._extensionsDatagrid ? opts._extensionsDatagrid : (opts._extensionsDatagrid = {});
        if ($.isFunction(opts.onBeforeDeleteColumn) && opts.onBeforeDeleteColumn.call(target, field) == false) { return; }
        removeField(opts, field, exts);
        t.datagrid("getColumnDom", { field: field, header: true }).remove();
        if ($.isFunction(opts.onDeleteColumn)) { opts.onDeleteColumn.call(target, field); }
    };

    var popColumn = function (target, field) {
        var t = $(target), colOpts = t.datagrid("getColumnOption", field);
        if (colOpts) { t.datagrid("deleteColumn", field); }
        return colOpts
    };

    var removeField = $.fn.datagrid.extensions.removeField = function (opts, field, exts) {
        var columns, frozen, i = -1, j = -1;
        if ($.array.likeArray(opts.frozenColumns)) {
            $.each(opts.frozenColumns, function (m, x) {
                if ($.array.likeArray(this)) {
                    $.each(this, function (n, y) {
                        if (y.field == field) { j = n; return false; }
                    });
                } else { if (x.field == field) { j = m; return false; } }
                if (j > -1) { i = m; return false; }
            });
            if (j > -1) { frozen = true; }
        }
        if (frozen == undefined && $.array.likeArray(opts.columns)) {
            $.each(opts.columns, function (m, x) {
                if ($.array.likeArray(this)) {
                    $.each(this, function (n, y) {
                        if (y.field == field) { j = n; return false; }
                    });
                } else { if (x.field == field) { j = m; return false; } }
                if (j > -1) { i = m; return false; }
            });
            if (j > -1) { frozen = false; }
        }
        if (j > -1) {
            columns = (frozen ? opts.frozenColumns : opts.columns);
            columns = i > -1 ? columns[i] : columns;
            $.array.removeAt(columns, j);
            index = $.array.indexOf(exts.fields, field);
            $.array.remove(exts.fields, field);
            $.array.removeAt(exts.fieldOptions, index);
            $.array.removeAt(exts.fieldOptionsBackup, index);
        }
    };






    var hasMuliRowHeader = function (target) {
        var t = $(target), opts = t.datagrid("options");
        return (opts.columns && opts.columns.length > 1 && opts.columns[1].length > 0)
            || (opts.frozenColumns && opts.frozenColumns.length > 1 && opts.frozenColumns[1].length > 0);
    };

    var findRows = function (target, param) {
        var t = $(target), rows = t.datagrid("getRows"), ret;
        if ($.isFunction(param)) {
            ret = $.array.filter(rows, param);
        } else if ($.array.likeArray(param) && !$.util.isString(param)) {
            ret = $.array.map(param, function (val) { return findRow(target, val, t, rows); });
            ret = $.array.filter(ret, function (val) { return val != undefined && val != null; });
        } else {
            ret = [findRow(target, param, t, rows)];
        }
        return ret;
    };

    var findRow = function (target, param, grid, rows) {
        var t = grid || $(target), data = rows || t.datagrid("getRows"), opts = t.datagrid("options");
        return $.array.first(data, $.isFunction(param) ? param : function (val) { return val[opts.idField] == param; });
    };

    var _deleteRow = $.fn.datagrid.methods.deleteRow;
    var deleteRow = function (target, param, doFilter) {
        var t = $(target), opts = t.datagrid("options"), isFunc = $.isFunction(param), index, row;
        if (doFilter == undefined || doFilter == null) { doFilter = true; }
        if (isFunc) {
            var rows = t.datagrid("getRows");
            row = $.array.first(rows, param);
            index = t.datagrid("getRowIndex", row);
        } else {
            //index = $.isNumeric(param) ? param : t.datagrid("getRowIndex", param);
            index = param;
            row = t.datagrid("getRowData", index);
        }
        //if ($.isNumeric(index) && index > -1 && $.isFunction(opts.onBeforeDeleteRow) && opts.onBeforeDeleteRow.call(target, index, row) != false) {
        if ($.isNumeric(index) && index > -1 && $.isFunction(opts.onBeforeDeleteRow) && opts.onBeforeDeleteRow.call(target, index, row) == false) { return; }
        _deleteRow.call(t, t, index);
        if ($.isFunction(opts.onDeleteRow)) { opts.onDeleteRow.call(target, index, row); }
        if (doFilter) {
            initHeaderColumnFilterContainer(t, opts);
        }
    };

    var deleteRows = function (target, param) {
        var t = $(target), opts = t.datagrid("options"), isArray = $.array.likeArrayNoString(param);
        if (isArray = $.array.likeArrayNoString(param)) {
            $.each(param, function (index, val) { deleteRow(target, val, false); });
        } else {
            var t = $(target), rows = t.datagrid("getRows");
            $.each(rows, function (index, row) {
                if (param.call(row, row, index, rows) == true) {
                    var i = t.datagrid("getRowIndex", row);
                    _deleteRow.call(t, t, i);
                }
            });
        }
        initHeaderColumnFilterContainer(t, opts);
    };

    var setColumnTitle = function (target, param) {
        if (param && param.field && param.title) {
            var t = $(target), colOpts = t.datagrid("getColumnOption", param.field),
                field = param.field, title = param.title,
                panel = t.datagrid("getPanel"),
                td = panel.find("div.datagrid-view div.datagrid-header tr.datagrid-header-row td[field=" + field + "]");
            if (td.length) { td.find("div.datagrid-cell span:first").html(title); colOpts.title = title; }
        }
    };

    var setColumnWidth = function (target, param) {
        if (param && param.field && param.width && $.isNumeric(param.width)) {
            var state = $.data(target, "datagrid"),
                t = $(target),
                opts = t.datagrid("options"),
                colOpts = t.datagrid("getColumnOption", param.field),
                field = param.field, width = param.width,
                cell = t.datagrid("getPanel").find("div.datagrid-view div.datagrid-header tr.datagrid-header-row td[field=" + field + "] div.datagrid-cell");
            if (cell.length) {
                var diff = cell._outerWidth() - parseInt(cell[0].style.width);
                cell.css("height", "");
                colOpts.width = width;
                colOpts.boxWidth = width - diff;
                colOpts.auto = undefined;
                cell.width(colOpts.boxWidth);
                t.datagrid("fixColumnSize", field);
                t.datagrid("fitColumns");
                opts.onResizeColumn.call(target, field, width);
            }
        }
    };


    $.fn.datagrid.extensions.parseOffset = function (offset) {
        var o = { enable: offset ? true : false };
        if (o.enable) { $.extend(o, offset); }
        o.width = $.isNumeric(o.width) ? o.width : 0;
        o.height = $.isNumeric(o.height) ? o.height : 0;
        return o;
    };
    var setOffset = function (target, offset) {
        var t = $(target), opts = t.datagrid("options"),
            exts = opts._extensionsDatagrid ? opts._extensionsDatagrid : (opts._extensionsDatagrid = {});
        opts.offset = exts.offset = $.fn.datagrid.extensions.parseOffset(offset);
        if (exts.offset && exts.offset.enable) {
            if (!$.isFunction(exts.offsetFunction)) {
                exts.offsetFunction = function () {
                    if (!exts.offset.enable) { return; }
                    var size = $.util.windowSize();
                    t.datagrid("resize", { width: size.width + exts.offset.width, height: size.height + exts.offset.height });
                };
                $(window).resize(exts.offsetFunction);
            }
            exts.offsetFunction();
        }
    };

    var getColumnDom = function (target, param) {
        if ($.string.isNullOrEmpty(param)) { return $(); }
        var t = $(target), panel = t.datagrid("getPanel"),
            isObject = !$.string.isString(param),
            field = isObject ? param.field : param,
            header = isObject ? param.header : false,
            dom = panel.find("div.datagrid-view tr.datagrid-row td[field=" + field + "]");
        if (header) { dom = dom.add(panel.find("div.datagrid-view tr.datagrid-header-row td[field=" + field + "]")); }
        return dom;
    };

    var getColumnData = function (target, field) {
        var t = $(target), rows = t.datagrid("getRows");
        return $.array.map(rows, function (val) { return val[field]; });
    };

    var getRowDom = function (target, index) {
        if (!$.isNumeric(index) || index < 0) { return $(); }
        var t = $(target), panel = t.datagrid("getPanel");
        return panel.find("div.datagrid-view div.datagrid-body table tr.datagrid-row[datagrid-row-index=" + index + "]");
    };

    var getRowData = function (target, index) {
        if (!$.isNumeric(index) || index < 0) { return undefined; }
        var t = $(target), rows = t.datagrid("getRows");
        return rows[index];
    };

    var getCellDom = function (target, pos) {
        if (!pos || !pos.field || !$.isNumeric(pos.index) || pos.index < 0) { return $(); }
        var t = $(target), tr = t.datagrid("getRowDom", pos.index);
        return tr.find("td[field=" + pos.field + "] .datagrid-cell");
    };
    var getCellData = function (target, pos) {
        if (!pos || !pos.field || !$.isNumeric(pos.index) || pos.index < 0) { return; }
        var t = $(target), row = t.datagrid("getRowData", pos.index);
        return row[pos.field];
    };
    var getCellDisplay = function (target, pos) {
        var t = $(target), td = t.datagrid("getCellDom", pos);
        return td && td.length ? td.html() : undefined;
    };
    var getCellDisplayText = function (target, pos) {
        var t = $(target), td = t.datagrid("getCellDom", pos);
        return td && td.length ? td.text() : undefined;
    };

    var _getColumnFields = $.fn.datagrid.methods.getColumnFields;
    var getColumnFields = function (target, frozen) {
        var t = $(target);
        if (frozen == null || frozen == undefined || $.util.isBoolean(frozen)) { return _getColumnFields.call(t, t, frozen); }
        if ($.util.isString(frozen)) {
            return $.array.merge([], _getColumnFields.call(t, t, true), _getColumnFields.call(t, t, false));
        }
    };

    var getDistinctRows = function (target, field) {
        var t = $(target), fields = t.datagrid("getColumnFields", "all");
        if (!$.array.contains(fields, field)) { return []; }
        var rows = t.datagrid("getRows"), data = $.array.clone(rows);
        $.array.distinct(data, function (a, b) { return a[field] == b[field]; });
        return data;
    };

    var getDistinctColumnData = function (target, field) {
        var t = $(target), fields = t.datagrid("getColumnFields", "all");
        if (!$.array.contains(fields, field)) { return []; }
        var data = t.datagrid("getColumnData", field);
        $.array.distinct(data, function (a, b) { return a == b; });
        return data;
    };

    var getColumns = function (target, frozen) {
        var t = $(target), fields = getColumnFields(target, frozen);
        return $.array.map(fields, function (val) { return t.datagrid("getColumnOption", val); });
    };

    var getHiddenColumns = function (target, frozen) {
        var cols = getColumns(target, frozen);
        return $.array.filter(cols, function (val) { return val.hidden ? true : false; });
    };

    var getVisibleColumns = function (target, frozen) {
        var cols = getColumns(target, frozen);
        return $.array.filter(cols, function (val) { return !val.hidden ? true : false; });
    };

    var getHiddenColumnFields = function (target, frozen) {
        var cols = getHiddenColumns(target, frozen);
        return $.array.map(cols, function (val) { return val.field; });
    };

    var getVisibleColumnFields = function (target, frozen) {
        var cols = getVisibleColumns(target, frozen);
        return $.array.map(cols, function (val) { return val.field; });
    };

    var showRow = function (target, param, grid, options, data, extensions, refreshable) {
        var t = grid || $(target), rows = data || t.datagrid("getRows"),
            row = $.isFunction(param) ? findRow(target, param, t, rows) : param, index = t.datagrid("getRowIndex", row),
            refreshable = (refreshable == null || refreshable == undefined || refreshable == true) ? true : false;
        if (index > -1) {
            var opts = options || t.datagrid("options"), rowData = t.datagrid("getRowData", index),
                exts = extensions || (opts._extensionsDatagrid ? opts._extensionsDatagrid : (opts._extensionsDatagrid = {}));
            exts.filterData = $.isArray(exts.filterData) ? exts.filterData : exts.filterData = [];
            t.datagrid("getRowDom", index).show();
            $.array.remove(exts.filterData, rowData);
            if (refreshable) { refreshColumnFilterStatus(t, opts, exts, rows); }
        }
    };

    var hideRow = function (target, param, grid, options, data, extensions, refreshable) {
        var t = grid || $(target), rows = data || t.datagrid("getRows"),
            row = $.isFunction(param) ? findRow(target, param, t, rows) : param, index = t.datagrid("getRowIndex", row),
            refreshable = refreshable == null || refreshable == undefined || refreshable == true ? true : false;
        if (index > -1) {
            var opts = options || t.datagrid("options"), rowData = t.datagrid("getRowData", index),
                exts = extensions || (opts._extensionsDatagrid ? opts._extensionsDatagrid : (opts._extensionsDatagrid = {}));
            exts.filterData = $.isArray(exts.filterData) ? exts.filterData : [];
            t.datagrid("unselectRow", index).datagrid("uncheckRow", index).datagrid("getRowDom", index).hide();
            $.array.attach(exts.filterData, rowData);
            if (refreshable) { refreshColumnFilterStatus(t, opts, exts, rows); }
        }
    };

    var showRows = function (target, param) {
        var t = $(target), opts = t.datagrid("options"), rows = t.datagrid("getRows"), array,
            exts = opts._extensionsDatagrid ? opts._extensionsDatagrid : (opts._extensionsDatagrid = {});
        if (param === true) {
            exts.filterData = [];
            var panel = t.datagrid("getPanel"), icons = panel.find("div.datagrid-header-filter-item-icon");
            panel.find(".datagrid-view .datagrid-body tr.datagrid-row").show();
            setItemIconCls(icons, "tree-checkbox1");
            refreshColumnFilterPagerStatus(t, opts);
        } else if ($.isFunction(param)) {
            array = $.array.filter(rows, param);
        } else if ($.array.likeArray(param) && !$.util.isString(param)) {
            array = param;
        } else { array = [param]; }
        if (array) {
            $.each(array, function (index, val) { showRow(target, val, t, opts, rows, exts, false); });
            refreshColumnFilterStatus(t, opts, exts, rows);
        }
    };

    var hideRows = function (target, param) {
        var t = $(target), opts = t.datagrid("options"), rows = t.datagrid("getRows"), array,
            exts = opts._extensionsDatagrid ? opts._extensionsDatagrid : (opts._extensionsDatagrid = {});
        if (param === true) {
            t.datagrid("unselectAll").datagrid("uncheckAll");
            exts.filterData = $.array.clone(rows);
            var panel = t.datagrid("getPanel"), icons = panel.find("div.datagrid-header-filter-item-icon");
            panel.find(".datagrid-view .datagrid-body tr.datagrid-row").hide();
            setItemIconCls(icons, "tree-checkbox0");
            refreshColumnFilterPagerStatus(t, opts);
        } else if ($.isFunction(param)) {
            array = $.array.filter(rows, param);
        } else if ($.array.likeArray(param) && !$.util.isString(param)) {
            array = param;
        } else { array = [param]; }
        if (array) {
            $.each(array, function (index, val) { hideRow(target, val, t, opts, rows, exts, false); });
            refreshColumnFilterStatus(t, opts, exts, rows);
        }
    };

    var getHiddenRows = function (target) {
        var t = $(target), opts = t.datagrid("options"),
            exts = opts._extensionsDatagrid ? opts._extensionsDatagrid : (opts._extensionsDatagrid = {});
        return exts.filterData;
    };

    var getVisibleRows = function (target) {
        var t = $(target), opts = t.datagrid("options"), rows = t.datagrid("getRows"),
            exts = opts._extensionsDatagrid ? opts._extensionsDatagrid : (opts._extensionsDatagrid = {}),
            filterData = $.isArray(exts.filterData) ? exts.filterData : [];
        return $.array.filter(rows, function (val) { return $.array.contains(filterData, val) ? false : true; });
    };

    var setColumnFilter = function (target, columnFilter) {
        var t = $(target),
            opts = t.datagrid("options"), exts = opts._extensionsDatagrid ? opts._extensionsDatagrid : (opts._extensionsDatagrid = {}),
            panel = t.datagrid("getPanel"),
            selector = "div.datagrid-view div.datagrid-header tr.datagrid-header-row div.datagrid-header-filter-container";
        if (!columnFilter) {
            var headerFields = panel.find(selector),
                length = headerFields.length, i = 0;
            headerFields.slideUp("slow", function () {
                if (++i == length) {
                    clearHeaderColumnFilter(t, opts);
                    opts.columnFilter = columnFilter;
                }
            });
        } else {
            opts.columnFilter = columnFilter;
            initHeaderColumnFilterContainer(t, opts, exts);
            $.util.exec(function () {
                panel.find(selector).hide().slideDown("slow");
            });
        }
    };

    var columnFilterSelect = function (target, param) {
        var t = $(target);
        if ($.util.isBoolean(param)) { t.datagrid(param ? "showRows" : "hideRows", true); return; }
        if (!param || !param.field) { return; }
        var field = param.field, value = param.value, isArray = $.array.likeArray(value) && !$.util.isString(value),
            finder = isArray ? function (val) { return $.array.contains(value, val[field]); } : function (val) { return value == val[field]; },
            rows = t.datagrid("findRows", finder);
        t.datagrid(param.selected ? "showRows" : "hideRows", rows);
    };




    var highlightColumn = function (target, field) {
        var t = $(target);
        var state = $.data(t[0], "datagrid"), opts = state.options;
        if (state.highlightField) {
            t.datagrid("getColumnDom", { field: state.highlightField, header: true }).removeClass("datagrid-row-over");
        }
        t.datagrid("getColumnDom", { field: field, header: true }).filter(function () {
            return !$(this).parent().hasClass("datagrid-row-selected");
        }).addClass("datagrid-row-over");
        state.highlightField = field;
    };

    var livesearch = function (target, param) {
        var t = $(target), panel = t.datagrid("getPanel"), cells, field, value = param, regular = false, ignoreCase = true, regexp;
        if ($.isPlainObject(param)) {
            value = param.value;
            field = param.field;
            regular = param.regular;
            ignoreCase = param.ignoreCase;
            cells = panel.find("div.datagrid-body tr.datagrid-row td[" + (field ? "field=" + field : "field") + "] div.datagrid-cell");
        } else {
            cells = panel.find("div.datagrid-body tr.datagrid-row td[field] div.datagrid-cell");
        }
        regexp = regular ? new RegExp(value, ignoreCase ? "gm" : "igm") : value;
        cells.each(function () {
            var cell = $(this);
            cell.find("span.datagrid-cell-hightlight").replaceWith(function () { return $(this).text(); });
            if (!value) { return; }
            var text = cell.html(); if (!text) { return; }
            cell.html($.string.replaceAll(text, value, "<span class='datagrid-cell-hightlight'>" + value + "</span>"));
        });
    };

    var exportGrid = function (target, isAll) {
        isAll = $.string.toBoolean(isAll);
        alert("导出" + (isAll ? "全部" : "当前页") + "数据");
    };

    /************************  initExtend Methods   End  ************************/


    var initColumnExtendProperties = $.fn.datagrid.extensions.initColumnExtendProperties = function (t, exts) {
        if (exts._initializedExtendProperties) { return; }
        var cols = t.datagrid("getColumns", "all");
        $.each(cols, function (i, n) {
            $.union(n, $.fn.datagrid.extensions.columnOptions);
        });
        exts._initializedExtendProperties = true;
    };

    var initRowDndExtensions = $.fn.datagrid.extensions.initRowDndExtensions = function (t, opts) {
        opts = opts || t.datagrid("options");
        if (opts.dndRow) { t.datagrid("enableRowDnd"); }
    };


    /************************  initExtend ColumnFilter Begin  ************************/
    function initHeaderColumnFilterContainer(t, opts, exts) {
        exts = exts || (opts._extensionsDatagrid ? opts._extensionsDatagrid : (opts._extensionsDatagrid = {}));
        initColumnExtendProperties(t, exts);
        var data = t.datagrid("getData"), oldData = exts.oldData;
        if (data != oldData) { exts.filterData = []; }
        clearHeaderColumnFilter(t, opts);
        refreshColumnFilterPagerStatus(t, opts);
        if (!opts.columnFilter) { return; }
        exts.oldData = data;
        var header = t.datagrid("getPanel").find("div.datagrid-view div.datagrid-header"),
            headerRows = header.find("table.datagrid-htable tr.datagrid-header-row"),
            headerFields = headerRows.find("td[field]").filter(function () {
                var td = $(this), colspan = td.attr("colspan");
                return (!colspan || colspan == "1") && !td.find("div.datagrid-header-check,div.datagrid-header-rownumber").length ? true : false;
            }),
            columnFilter = opts.columnFilter = $.extend({ panelHeight: 100, position: "top" }, opts.columnFilter),
            position = $.array.contains(["top", "bottom"], columnFilter.position) ? columnFilter.position : "top",
            panelHeight = columnFilter.panelHeight = $.isNumeric(columnFilter.panelHeight) && columnFilter.panelHeight >= 60 ? columnFilter.panelHeight : 60,
            height = header.height(), rows = t.datagrid("getRows");
        headerFields.each(function () {
            var td = $(this).addClass("datagrid-header-filter").removeClass("datagrid-header-filter-top datagrid-header-filter-bottom"),
                cell = td.find("div.datagrid-cell").addClass("datagrid-header-filter-cell"),
                field = td.attr("field"), colOpts = t.datagrid("getColumnOption", field), colWidth = colOpts.width,
                line = $("<hr />").addClass("datagrid-header-filter-line")[position == "top" ? "prependTo" : "appendTo"](this),
                container = $("<div></div>").attr("field", field).addClass("datagrid-header-filter-container").css({
                    height: columnFilter.panelHeight, width: colWidth
                })[position == "top" ? "prependTo" : "appendTo"](this);
            td.addClass(position == "top" ? "datagrid-header-filter-top" : "datagrid-header-filter-bottom");
            if (field) { initColumnFilterField(t, opts, exts, container, colOpts, rows, headerFields); }
        });
        if (exts.filterData && exts.filterData.length) {
            t.datagrid("hideRows", exts.filterData);
        } else {
            refreshColumnFilterStatus(t, opts, exts, rows, headerFields);
        }
    };

    function clearHeaderColumnFilter(t, opts) {
        if (!opts.columnFilter) { return; }
        var headerFields = t.datagrid("getPanel").find("div.datagrid-view div.datagrid-header table.datagrid-htable tr.datagrid-header-row td[field]").filter(function () {
            var td = $(this), colspan = td.attr("colspan");
            return (!colspan || colspan == "1") && !td.find("div.datagrid-header-check,div.datagrid-header-rownumber").length ? true : false;
        });
        headerFields.removeClass("datagrid-header-filter datagrid-header-filter-top datagrid-header-filter-bottom").find("div.datagrid-cell").removeClass("datagrid-header-filter-cell");
        headerFields.find("hr.datagrid-header-filter-line,div.datagrid-header-filter-container").remove();
        var fields = t.datagrid("getColumnFields", "all");
        t.datagrid("fixColumnSize", fields[fields.length - 1]);
    };

    function initColumnFilterField(t, opts, exts, container, colOpts, rows, headerFields) {
        if (!colOpts.filterable) { return; }
        var field = colOpts.field, distinctVals = t.datagrid("getDistinctColumnData", field),
            filter = $.array.contains(["checkbox", "livebox", "caps", "lower", "none"], colOpts.filter) ? colOpts.filter : "checkbox",
            precision = colOpts.precision, step = colOpts.step;
        switch (filter) {
            case "checkbox": initColumnFilterFieldCheckBox(t, exts, container, field, rows, distinctVals); break;
            case "livebox": initColumnFilterFieldLiveBox(t, container, field, rows); break;
            case "caps":
                initColumnFilerFieldSlider(t, container, field, step, precision, rows, distinctVals, "<=", opts.columnFilter.panelHeight, headerFields);
                break;
            case "lower":
                initColumnFilerFieldSlider(t, container, field, step, precision, rows, distinctVals, ">=", opts.columnFilter.panelHeight, headerFields);
                break;
            case "none": break;
        }
    };

    function initColumnFilterFieldCheckBox(t, exts, container, field, rows, distinctVals) {
        $.each(distinctVals, function (index, text) {
            var item = $("<div></div>").addClass("datagrid-header-filter-item").attr("text", text).appendTo(container),
                itemText = $("<div></div>").addClass("datagrid-header-filter-item-text").text(text).appendTo(item),
                icon = $("<div></div>").addClass("datagrid-header-filter-item-icon").appendTo(item),
                handler = function () {
                    var filterRows = $.array.filter(rows, function (value) { return value[field] == text; }),
                        hiddenRows = $.array.filter(exts.filterData, function (value) { return value[field] == text; });
                    t.datagrid(hiddenRows.length ? "showRows" : "hideRows", filterRows);
                };
            item.click(handler);
        });
    };

    function initColumnFilterFieldLiveBox(t, container, field, rows) {
        $("<div></div>").addClass("datagrid-header-filter-livebox-text").text("模糊过滤：").appendTo(container);
        var input = $("<input />").addClass("datagrid-header-filter-livebox").appendTo(container);
        var btn = $("<a />").linkbutton({ plain: true, iconCls: "icon-search" }).appendTo(container).click(function () {
            t.datagrid("showRows", true);
            var val = input.val();
            if ($.string.isNullOrEmpty(val)) { input.focus(); return; }
            var filterRows = $.array.filter(rows, function (value) { return String(value[field]).indexOf(val) == -1; });
            t.datagrid("hideRows", filterRows);
            input.focus();
        });
        $("<a />").linkbutton({ plain: true, iconCls: "icon-undo" }).appendTo(container).click(function () {
            var val = input.val();
            if (val) { input.val("").focus(); btn.click(); } else { input.focus(); }
        });
        input.keypress(function (e) { if (e.which == 13) { btn.click(); } });
    };

    function initColumnFilerFieldSlider(t, container, field, step, precision, rows, distinctVals, type, panelHeight, headerFileds) {
        var array = $.array.map(distinctVals, function (val) { val = parseFloat(val); return $.isNumeric(val) ? val : 0; }),
            min = array.length ? $.array.min(array) : 0, max = array.length ? $.array.max(array) : 0,
            maxPrecisionVal = array.length ? $.array.max(array, function (a, b) {
                return $.util.compare($.number.precision(a), $.number.precision(b));
            }) : 0,
            maxPrecision = array.length ? $.number.precision(maxPrecisionVal) : 0,
            height = panelHeight - 45,
            itemWrap = $("<div></div>").addClass("datagrid-header-filter-itemwrap").text(type).appendTo(container),
            sliderWrap = $("<div></div>").addClass("datagrid-header-filter-sliderwrap").css({
                height: height + 10
            })[type == "<=" ? "appendTo" : "prependTo"](container),
            input = $("<input />").addClass("datagrid-header-filter-numeric").appendTo(itemWrap),
            slider = $("<input />").addClass("datagrid-header-filter-slider").appendTo(sliderWrap),
            handler = function (newValue, oldValue) {
                changeSliderValue(t, field, rows, newValue, type, input, slider, headerFileds);
            };
        input.numberbox({ value: type == "<=" ? max : min, min: min, max: max, precision: precision, onChange: handler, height: 18 });
        input.keypress(function (e) { if (e.which == 13) { var val = input.val(); input.numberbox("setValue", $.isNumeric(val) ? val : 0); } });
        slider.slider({
            height: height, mode: "v", showTip: true, value: type == "<=" ? max : min,
            min: min, max: max, rule: [min, "|", max], step: step, onComplete: handler,
            tipFormatter: function (val) { return $.number.round(val || 0, maxPrecision); }
        });
    };

    function changeSliderValue(t, field, rows, value, type, input, slider, headerFileds) {
        var headerFields = headerFileds || t.datagrid("getPanel").find("div.datagrid-view div.datagrid-header table.datagrid-htable tr.datagrid-header-row td[field]").filter(function () {
            var td = $(this), colspan = td.attr("colspan");
            return (!colspan || colspan == "1") && !td.find("div.datagrid-header-check,div.datagrid-header-rownumber").length ? true : false;
        });
        var headerField = headerFields.filter(function () { return $(this).attr("field") == field; });
        input = input ? input : headerField.find(".datagrid-header-filter-numeric");
        slider = slider ? slider : headerField.find(".datagrid-header-filter-slider");
        var filterRows = $.array.filter(rows, function (val) {
            val = parseFloat(val[field]);
            val = $.isNumeric(val) ? val : 0;
            return type == ">=" ? (val < value) : (val > value);
        });
        t.datagrid("showRows", true).datagrid("hideRows", filterRows);
        input.numberbox("setValue", value);
        slider.slider("setValue", value);
    };



    function refreshColumnFilterStatus(t, opts, exts, rows, headerFields) {
        refreshColumnFilterPagerStatus(t, opts);
        if (!opts.columnFilter) { return; }
        headerFields = headerFields || t.datagrid("getPanel").find("div.datagrid-view div.datagrid-header table.datagrid-htable tr.datagrid-header-row td[field]").filter(function () {
            var td = $(this), colspan = td.attr("colspan");
            return (!colspan || colspan == "1") && !td.find("div.datagrid-header-check,div.datagrid-header-rownumber").length ? true : false;
        });
        headerFields.each(function () {
            var td = $(this), field = td.attr("field");
            refreshColumnFilterCellStatus(t, exts, rows, td, field);
        });
    };

    function refreshColumnFilterPagerStatus(t, opts) {
        if (!opts.pagination) { return; }
        var pager = t.datagrid("getPager");
        if (pager && pager.length) {
            var len = t.datagrid("getVisibleRows").length, total = t.datagrid("getRows").length,
                isShow = len < total ? true : false, visible = pager.find("div.pagination-visiblerows");
            if (opts.showFilterText == false || ((opts.showFilterText == null || opts.showFilterText == undefined) && !isShow)) {
                return visible.remove();
            }
            if (visible.length) {
                visible.html("当前页显示" + len + "/" + total + "行");
            } else {
                pager.find("div.pagination-info").before("<div class=\"pagination-visiblerows\">当前页显示" + len + "/" + total + "行</div>");
            }
        }
    };

    function refreshColumnFilterCellStatus(t, exts, rows, td, field) {
        var colOpts = colOpts = t.datagrid("getColumnOption", field), precision = colOpts.precision,
            filter = $.array.contains(["checkbox", "livebox", "caps", "lower", "none"], colOpts.filter) ? colOpts.filter : "checkbox";
        switch (filter) {
            case "checkbox": refreshColumnFilterCheckbox(t, exts, rows, td, field); break;
            case "livebox": refreshColumnFilterLiveBox(t, exts, rows, td, field); break;
            case "caps": refreshColumnFilterCaps(t, exts, rows, td, field); break;
            case "lower": refreshColumnFilterLower(t, exts, rows, td, field); break;
            case "none": break;
        };
    };


    function refreshColumnFilterCheckbox(t, exts, rows, td, field) {
        td.find("div.datagrid-header-filter-item").each(function () {
            var item = $(this), text = item.attr("text"), icon = item.find("div.datagrid-header-filter-item-icon");
            var length = $.array.sum(rows, function (val) { return val[field] == text ? 1 : 0; }),
                hiddenLength = $.array.sum(exts.filterData, function (val) { return val[field] == text ? 1 : 0; }),
                iconCls = hiddenLength == 0 ? "tree-checkbox1" : (hiddenLength >= length ? "tree-checkbox0" : "tree-checkbox2");
            $.easyui.tooltip.init(item, { content: ($.string.isNullOrEmpty(text) ? "空白" : text) + ": 共" + length + "个元素" });
            setItemIconCls(icon, iconCls);
        });
    };
    function setItemIconCls(icon, iconCls) { icon.removeClass("tree-checkbox0 tree-checkbox1 tree-checkbox2").addClass(iconCls); };

    //  当过滤器组件进行值筛选操作后，livebox 以及 slider 不更新，所以下面这三个方法未实现。
    function refreshColumnFilterLiveBox(t, exts, rows, td, field) { };
    function refreshColumnFilterCaps(t, exts, rows, td, field) { };
    function refreshColumnFilterLower(t, exts, rows, td, field) { };


    /************************  initExtend ColumnFilter   End  ************************/



    /************************  initExtend initContextMenu & initDblClickRow Begin  ************************/
    function initHeaderContextMenu(t, opts, exts) {
        exts.onHeaderContextMenuBak = opts.onHeaderContextMenu;
        opts.onHeaderContextMenu = function (e, field) {
            if ($.isFunction(exts.onHeaderContextMenuBak)) { exts.onHeaderContextMenuBak.apply(this, arguments); }
            if (!opts.enableHeaderContextMenu) { return; }
            var eventData = $.fn.datagrid.extensions.parseContextMenuEventData(t, opts, e),
                items = parseHeaderContextMenuItems(t, opts, exts, e, field, eventData);
            $.easyui.showMenu({ items: items, left: e.pageX, top: e.pageY, hideDisabledMenu: opts.hideDisabledMenu });
            e.preventDefault();
        };
    };

    function initRowContextMenu(t, opts, exts) {
        exts.onRowContextMenuBak = opts.onRowContextMenu;
        opts.onRowContextMenu = function (e, rowIndex, rowData) {
            if ($.isFunction(exts.onRowContextMenuBak)) { exts.onRowContextMenuBak.apply(this, arguments); }
            if (opts.selectOnRowContextMenu) { t.datagrid("selectRow", rowIndex); }
            if (!opts.enableRowContextMenu) { return; }
            var eventData = $.fn.datagrid.extensions.parseContextMenuEventData(t, opts, e),
                items = parseRowContextMenuItems(t, opts, exts, e, rowIndex, rowData, eventData);
            if (opts.autoBindDblClickRow && opts.dblClickRowMenuIndex >= 0 && $.util.likeArray(opts.rowContextMenu) && !$.util.isString(opts.rowContextMenu)
                && opts.rowContextMenu.length > opts.dblClickRowMenuIndex) {
                items[opts.dblClickRowMenuIndex].bold = true;
            }
            $.easyui.showMenu({ items: items, left: e.pageX, top: e.pageY, hideDisabledMenu: opts.hideDisabledMenu });
            e.preventDefault();
        };
    };

    function initHeaderClickMenu(t, opts, exts) {
        if (!opts.enableHeaderClickMenu) { return; }
        t.datagrid("getPanel").find(".datagrid-view .datagrid-header table.datagrid-htable tr.datagrid-header-row td[field]").filter(function () {
            var td = $(this), colspan = td.attr("colspan");
            return (!colspan || colspan == "1") && !td.find("div.datagrid-header-check,div.datagrid-header-rownumber").length ? true : false;
        }).find("div.datagrid-cell").each(function () { initHeaderCellClickMenu(t, opts, exts, this); });
    };

    function initHeaderCellClickMenu(t, opts, exts, cell) {
        var arrow = $("<span class='s-btn-downarrow datagrid-header-cell-arrow'>&nbsp;</span>").click(function (e) {
            var span = $(this), offset = span.offset(), height = span.outerHeight(),
                field = span.parent().parent().attr("field"),
                eventData = $.fn.datagrid.extensions.parseContextMenuEventData(t, opts, e),
                items = parseHeaderContextMenuItems(t, opts, exts, e, field, eventData);
            var mm = $.easyui.showMenu({ items: items, left: offset.left, top: offset.top + height }),
                mmOpts = mm.menu("options"), onHide = mmOpts.onHide;
            arrow.hidable = false;
            mmOpts.onHide = function () {
                arrow.hidable = true;
                arrow.removeClass("datagrid-header-cell-arrow-show");
                onHide.apply(this, arguments);
            };
            return false;
        }).prependTo(cell);
        $(cell).off(".hoverArrow").on({
            "mouseenter.hoverArrow": function () {
                arrow.addClass("datagrid-header-cell-arrow-show");
            },
            "mouseleave.hoverArrow": function () {
                if (!$.util.isBoolean(arrow.hidable) || arrow.hidable) {
                    arrow.removeClass("datagrid-header-cell-arrow-show");
                }
            }
        });
    };


    function initDblClickRowEvent(t, opts, exts) {
        exts.onDblClickRowBak = opts.onDblClickRow;
        opts.onDblClickRow = function (rowIndex, rowData) {
            if ($.isFunction(exts.onDblClickRowBak)) { exts.onDblClickRowBak.apply(this, arguments); }
            //  t.datagrid("selectRow", rowIndex);
            var eventData = $.fn.datagrid.extensions.parseContextMenuEventData(t, opts, null);
            items = parseRowContextMenuItems(t, opts, exts, null, rowIndex, rowData, eventData);
            if (opts.autoBindDblClickRow && opts.dblClickRowMenuIndex >= 0 && $.util.likeArray(opts.rowContextMenu)
                && !$.util.isString(opts.rowContextMenu) && opts.rowContextMenu.length > opts.dblClickRowMenuIndex) {
                var item = items[opts.dblClickRowMenuIndex], handler = item.handler || item.onclick;
                if (!item.disabled) {
                    return handler(null, rowIndex, rowData, eventData, t, item, null);
                }
            }
            //if (opts.autoEditing) { t.datagrid("beginEdit", rowIndex); }
        };
    };

    function initAutoEditingEvent(t, opts, exts) {
        exts[opts.autoEditingEvent] = opts[opts.autoEditingEvent];
        opts[opts.autoEditingEvent] = function (rowIndex, rowData) {
            if ($.isFunction(exts[opts.autoEditingEvent])) { exts[opts.autoEditingEvent].apply(this, arguments); }
            if (opts.autoEditing) { t.datagrid("beginEdit", rowIndex); }
        }
    }

    function initFinishEditEvent(t, opts, exts) {
        $(opts.finishEditLocale).click(function (e) {
            if (opts.finishEditOnBlur && $.data(t[0], "datagrid")) {
                var body = t.datagrid("getPanel"), rows = t.datagrid("getEditingRowIndexs");
                if (!$.contains(body[0], e.target)) {
                    $.each(rows, function (ii, i) { t.datagrid(opts.finishEditMethod, i); });
                }
            }
        });
    };


    function parseHeaderContextMenuItems(t, opts, exts, e, field, eventData) {
        var items = [], contextMenu = $.util.likeArray(opts.headerContextMenu) && !$.util.isString(opts.headerContextMenu) ? opts.headerContextMenu : [];
        if (contextMenu.length) { $.array.merge(items, contextMenu); }
        var baseItems = parseHeaderBaseContextMenuItems(t, opts, exts, e, field, eventData);
        if (baseItems.length) { $.array.merge(items, "-", baseItems); }
        items = $.fn.datagrid.extensions.parseHeaderContextMenuMap(e, field, eventData, items, t);
        if (items[0] == "-") { $.array.removeAt(items, 0); }
        return items;
    }

    function parseRowContextMenuItems(t, opts, exts, e, rowIndex, rowData, eventData) {
        var items = [], contextMenu = $.util.likeArray(opts.rowContextMenu) && !$.util.isString(opts.rowContextMenu) ? opts.rowContextMenu : [];
        if (contextMenu.length) { $.array.merge(items, contextMenu); }
        var baseItems = parseRowBaseContextMenuItems(t, opts, exts, e, rowIndex, rowData, eventData);
        if (baseItems.length) { $.array.merge(items, "-", baseItems); }
        items = $.fn.datagrid.extensions.parseRowContextMenuMap(e, rowIndex, rowData, eventData, items, t);
        if (items[0] == "-") { $.array.removeAt(items, 0); }
        return items;
    }



    function parseHeaderBaseContextMenuItems(t, opts, exts, e, field, eventData) {
        var mm = [], exp = opts.exportMenu,
            colOpts = t.datagrid("getColumnOption", field), sortable = colOpts.sortable;
        if (typeof exp == "object") { exp = $.extend({ current: false, all: false, submenu: true }, exp); }
        var m1 = {
            text: "升序", iconCls: "icon-standard-hmenu-asc", disabled: sortable != true,
            handler: function () { return t.datagrid("sort", { sortName: field, sortOrder: "asc" }); }
        };
        var m2 = {
            text: "降序", iconCls: "icon-standard-hmenu-desc", disabled: sortable != true,
            handler: function () { return t.datagrid("sort", { sortName: field, sortOrder: "desc" }); }
        };
        var m3 = {
            text: "显示/隐藏列", iconCls: "icon-standard-application-view-columns", disabled: false, children: [
                {
                    text: "显示全部列", iconCls: function () {
                        var len = exts.fields ? exts.fields.length : 0;
                        var count = $.array.sum(exts.fieldOptions, function (val) { return val.hidden ? 0 : 1; });
                        return count >= len ? "tree-checkbox1" : (count == 0 ? "tree-checkbox0" : "tree-checkbox2");
                    }, hideOnClick: false, handler: function (e, field, eventData, t, item, menu) {
                        $.each(exts.fields, function () { t.datagrid("showColumn", this); });
                        $(this).parent().children("div.menu-item:not(:eq(1))").each(function () {
                            menu.menu("setIcon", { target: this, iconCls: "tree-checkbox1" });
                            menu.menu("enableItem", this);
                        });
                    }
                },
                {
                    text: "还原默认", iconCls: "icon-standard-application-view-tile", hideOnClick: false, handler: function (e, field, eventData, t, item, menu) {
                        $.each(exts.fieldOptionsBackup, function () { t.datagrid(this.hidden == true ? "hideColumn" : "showColumn", this.field); });
                        var mm = $(this).parent();
                        mm.children("div.menu-item:gt(1)").each(function () {
                            var title = $(this).text(), colOpts = $.array.first(exts.fieldOptions, function (val) { return val.title == title; });
                            if (colOpts) { menu.menu("setIcon", { target: this, iconCls: colOpts.hidden ? "tree-checkbox0" : "tree-checkbox1" }); }
                            menu.menu("enableItem", this);
                        });
                        mm.children("div.menu-item:first").each(function () {
                            var len = exts.fields ? exts.fields.length : 0;
                            var count = $.array.sum(exts.fieldOptions, function (val) { return val.hidden ? 0 : 1; });
                            menu.menu("setIcon", { target: this, iconCls: count >= len ? "tree-checkbox1" : (count == 0 ? "tree-checkbox0" : "tree-checkbox2") });
                        });
                    }
                },
                "-"
            ]
        };
        var m4 = { text: "过滤/显示", iconCls: "icon-standard-application-view-list", disabled: !colOpts.filterable, children: [] };
        var m5 = { text: "导出当前页", iconCls: "icon-standard-page-white-put", disabled: !(exp == true || exp.current == true), handler: function () { return t.datagrid("exportExcel", false); } };
        var m6 = { text: "导出全部", iconCls: "icon-standard-page-white-stack", disabled: !(exp == true || exp.all == true), handler: function () { return t.datagrid("exportExcel", true); } };
        $.util.merge(m3.children, parseHeaderColumnsShowHideMenu(t, opts, exts, e, field, eventData));
        if (colOpts.filterable) { $.util.merge(m4.children, parseHeaderRowsShowHideMenu(t, opts, exts, e, field, eventData)); }
        $.util.merge(mm, [m1, m2, "-", m3, m4]);
        var expMenu = [m5, m6];
        if (exp) { $.array.merge(mm, "-", typeof exp == "object" && !exp.submenu ? expMenu : { text: "导出数据", iconCls: "icon-standard-page-save", children: expMenu }); }
        return mm;
    };

    function parseHeaderColumnsShowHideMenu(t, opts, exts, e, field, eventData) {
        return $.array.map(exts.fieldOptions, function (val) {
            var handler = function (e, field, eventData, t, item, menu) {
                var m = $(this),
                    count = m.parent().find(".menu-item:gt(1) .tree-checkbox1").length;
                if ((count == 1 && !val.hidden) || !val.hidable) { return; }
                t.datagrid(val.hidden ? "showColumn" : "hideColumn", val.field);
                menu.menu("setIcon", { target: this, iconCls: val.hidden ? "tree-checkbox0" : "tree-checkbox1" });
                count = $.array.sum(exts.fieldOptions, function (val) { return val.hidden ? 0 : 1; });
                var len = exts.fields ? exts.fields.length : 0;
                menu.menu("setIcon", {
                    target: m.parent().children("div.menu-item:first"),
                    iconCls: count >= len ? "tree-checkbox1" : (count == 0 ? "tree-checkbox0" : "tree-checkbox2")
                });
                var mm = m.parent().find(".menu-item:gt(1)").filter(function () { return $(".tree-checkbox1", this).length ? true : false; });
                mm.each(function () { menu.menu(mm.length > 1 ? "enableItem" : "disableItem", this); });
            };
            return {
                text: val.title || val.field, iconCls: val.hidden ? "tree-checkbox0" : "tree-checkbox1", hideOnClick: false,
                disabled: !val.hidable ? true : false, handler: handler
            };
        });
    };

    function parseHeaderRowsShowHideMenu(t, opts, exts, e, field, eventData) {
        var rows = t.datagrid("getRows"), distinctVals = t.datagrid("getDistinctColumnData", field),
            mm = [
                {
                    text: "全部", hideOnClick: false,
                    iconCls: (!exts.filterData || !exts.filterData.length) ? "tree-checkbox1" : (exts.filterData.length >= rows.length ? "tree-checkbox0" : "tree-checkbox2"),
                    handler: function (e, field, eventData, t, item, menu) {
                        if (exts.filterData && exts.filterData.length) {
                            t.datagrid("showRows", true);
                        } else {
                            t.datagrid("hideRows", true);
                        }
                        $(this).parent().children("div.menu-item[hideOnClick=false]").each(function () {
                            menu.menu("setIcon", { target: this, iconCls: exts.filterData && exts.filterData.length ? "tree-checkbox0" : "tree-checkbox1" });
                        });
                    }
                }, "-"
            ];
        var hasMore = distinctVals.length >= 15,
            data = hasMore ? $.array.left(distinctVals, 10) : distinctVals,
            items = $.array.map(data, function (val) {
                var filterRows = $.array.filter(rows, function (value) { return value[field] == val; }),
                    filterLength = filterRows.length,
                    hiddenLength = $.array.sum(exts.filterData, function (value) { return value[field] == val ? 1 : 0; }),
                    iconCls = !hiddenLength ? "tree-checkbox1" : (hiddenLength >= filterLength ? "tree-checkbox0" : "tree-checkbox2");
                var handler = function (e, field, eventData, t, item, menu) {
                    var hiddenLength = $.array.sum(exts.filterData, function (value) { return value[field] == val ? 1 : 0; });
                    t.datagrid(hiddenLength ? "showRows" : "hideRows", filterRows);
                    menu.menu("setIcon", { target: this, iconCls: hiddenLength ? "tree-checkbox1" : "tree-checkbox0" });
                    $(this).parent().children("div.menu-item:first").each(function () {
                        menu.menu("setIcon", {
                            target: this,
                            iconCls: (!exts.filterData || !exts.filterData.length) ? "tree-checkbox1" : (exts.filterData.length >= rows.length ? "tree-checkbox0" : "tree-checkbox2")
                        });
                    });
                };
                return { text: val, iconCls: iconCls, hideOnClick: false, handler: handler };
            });
        $.array.merge(mm, items);
        if (hasMore) {
            var colOpt = t.datagrid("getColumnOption", field), title = colOpt.title ? colOpt.title : colOpt.field, handler = function () {
                var checkAll = $("<input type=\"button\" value=\"全部选择\" />").click(function () {
                    t.datagrid("showRows", true);
                    $(this).parent().find(":checkbox").each(function () { this.checked = true; });
                });
                var uncheckAll = $("<input type=\"button\" value=\"全部不选\" />").click(function () {
                    t.datagrid("hideRows", true);
                    $(this).parent().find(":checkbox").each(function () { this.checked = false; });
                });
                $("<div></div>").append("<div>列：" + title + "，共" + distinctVals.length + "项</div><hr />").css({
                    padding: "10px"
                }).append(checkAll).append(uncheckAll).append("<hr />").each(function () {
                    var win = $(this), ul = $("<ul></ul>").css({ "list-style-type": "decimal", "padding-left": "40px", "line-height": "18px" }).appendTo(win);
                    $.each(distinctVals, function (index, text) {
                        var id = "itemCheckbox_" + $.util.guid("N"),
                            checked = !$.array.some(exts.filterData, function (val) { return val[field] == text; }),
                            itemWrap = $("<li></li>").appendTo(ul),
                            item = $("<input />").attr({ type: "checkbox", id: id, checked: checked }).appendTo(itemWrap),
                            itemText = $("<label></label>").attr("for", id).text(text).appendTo(itemWrap),
                            handler = function () {
                                var filterRows = $.array.filter(rows, function (val) { return val[field] == text; }),
                                    hiddenLength = $.array.sum(exts.filterData, function (val) { return val[field] == text ? 1 : 0; });
                                t.datagrid(hiddenLength ? "showRows" : "hideRows", filterRows);
                            };
                        item.click(handler);
                    });
                }).dialog({
                    title: "过滤/显示", iconCls: "icon-standard-application-view-detail", height: 260, width: 220, left: e.pageX, top: e.pageY,
                    collapsible: false, minimizable: false, maximizable: false, closable: true, modal: true, resizable: true,
                    onClose: function () { $(this).dialog("destroy"); }
                }).dialog("open");
            };
            $.array.merge(mm, ["-", { text: "处理更多(共" + distinctVals.length + "项)...", iconCls: "icon-standard-application-view-detail", handler: handler }]);
        }
        return mm;
    };



    function parseRowBaseContextMenuItems(t, opts, exts, e, rowIndex, rowData, eventData) {
        var mm = [], paging = opts.pagingMenu, move = opts.moveMenu, exp = opts.exportMenu;
        if (typeof paging == "object") { paging = $.extend({ disabled: false, submenu: true }, paging); }
        if (typeof move == "object") { move = $.extend({ up: false, down: false, submenu: true }, move); }
        if (typeof exp == "object") { exp = $.extend({ current: false, all: false, submenu: true }, exp); }
        var m1 = {
            text: "刷新当前页", iconCls: "pagination-load", disabled: !opts.refreshMenu,
            handler: function () { t.datagrid("reload"); }
        };
        var m2 = {
            text: "首页", iconCls: "pagination-first", disabled: function () { return !opts.pagination || eventData.page <= 1; },
            handler: function () { if (eventData.page > 1) { eventData.pager.pagination("select", 1); } }
        };
        var m3 = {
            text: "上一页", iconCls: "pagination-prev", disabled: function () { return !opts.pagination || eventData.page <= 1; },
            handler: function () { if (eventData.page > 1) { eventData.pager.pagination("select", eventData.page - 1); } }
        };
        var m4 = {
            text: "下一页", iconCls: "pagination-next", disabled: function () { return !opts.pagination || eventData.page >= eventData.pageCount; },
            handler: function () { if (eventData.page < eventData.pageCount) { eventData.pager.pagination("select", eventData.page + 1); } }
        };
        var m5 = {
            text: "末页", iconCls: "pagination-last", disabled: function () { return !opts.pagination || eventData.page >= eventData.pageCount; },
            handler: function () { if (eventData.page < eventData.pageCount) { eventData.pager.pagination("select", eventData.pageCount); } }
        };
        var m6 = { text: "移至最上", iconCls: "icon-standard-arrow-up", disabled: !(move == true || move.top == true), handler: function () { t.datagrid("moveRow", { source: rowIndex, target: 0, point: "top" }); } };
        var m7 = { text: "上移", iconCls: "icon-standard-up", disabled: !(move == true || move.up == true), handler: function () { t.datagrid("shiftRow", { point: "up", index: rowIndex }); } };
        var m8 = { text: "下移", iconCls: "icon-standard-down", disabled: !(move == true || move.down == true), handler: function () { t.datagrid("shiftRow", { point: "down", index: rowIndex }); } };
        var m9 = {
            text: "移至最下", iconCls: "icon-standard-arrow-down", disabled: !(move == true || move.bottom == true), handler: function () {
                var rows = t.datagrid("getRows");
                t.datagrid("moveRow", { source: rowIndex, target: rows.length - 1, point: "bottom" });
            }
        };
        var m10 = { text: "导出当前页", iconCls: "icon-standard-page-white-put", disabled: !(exp == true || exp.current == true), handler: function () { return t.datagrid("exportExcel", false); } };
        var m11 = { text: "导出全部", iconCls: "icon-standard-page-white-stack", disabled: !(exp == true || exp.all == true), handler: function () { return t.datagrid("exportExcel", true); } };
        mm.push(m1);
        var pagingMenu = [m2, m3, m4, m5], moveMenu = [m6, m7, "-", m8, m9], expMenu = [m10, m11];
        if (paging) { $.array.merge(mm, "-", typeof paging == "object" && !paging.submenu ? pagingMenu : { text: "翻页", iconCls: "", disabled: !(paging == true || !paging.disabled), children: pagingMenu }); }
        if (move) { $.array.merge(mm, "-", typeof move == "object" && !move.submenu ? moveMenu : { text: "上/下移动", iconCls: "", disabled: !move, children: moveMenu }); }
        if (exp) { $.array.merge(mm, "-", typeof exp == "object" && !exp.submenu ? expMenu : { text: "导出数据", iconCls: "icon-standard-page-save", disabled: !exp, children: expMenu }); }
        return mm;
    };


    $.fn.datagrid.extensions.parseHeaderContextMenuMap = function (e, field, eventData, contextMenu, t) {
        return $.array.map(contextMenu, function (value, index) {
            if (!value || $.util.isString(value)) { return value; }
            var ret = $.extend({}, value);
            ret.id = $.isFunction(value.id) ? value.id.call(ret, e, field, eventData, t) : value.id;
            ret.text = $.isFunction(value.text) ? value.text.call(ret, e, field, eventData, t) : value.text;
            ret.iconCls = $.isFunction(value.iconCls) ? value.iconCls.call(ret, e, field, eventData, t) : value.iconCls;
            ret.disabled = $.isFunction(value.disabled) ? value.disabled.call(ret, e, field, eventData, t) : value.disabled;
            ret.hideOnClick = $.isFunction(value.hideOnClick) ? value.hideOnClick.call(ret, e, field, eventData, t) : value.hideOnClick;
            ret.onclick = $.isFunction(value.onclick) ? function (e, item, menu) { value.onclick.call(this, e, field, eventData, t, item, menu); } : value.onclick;
            ret.handler = $.isFunction(value.handler) ? function (e, item, menu) { value.handler.call(this, e, field, eventData, t, item, menu); } : value.handler;
            if (ret.children && ret.children.length) { ret.children = $.fn.datagrid.extensions.parseHeaderContextMenuMap(e, field, eventData, ret.children, t); }
            return ret;
        });
    };

    $.fn.datagrid.extensions.parseRowContextMenuMap = function (e, rowIndex, rowData, eventData, contextMenu, t) {
        return $.array.map(contextMenu, function (value, index) {
            if (!value || $.util.isString(value)) { return value; }
            var ret = $.extend({}, value);
            ret.id = $.isFunction(value.id) ? value.id.call(ret, e, rowIndex, rowData, eventData, t) : value.id;
            ret.text = $.isFunction(value.text) ? value.text.call(ret, e, rowIndex, rowData, eventData, t) : value.text;
            ret.iconCls = $.isFunction(value.iconCls) ? value.iconCls.call(ret, e, rowIndex, rowData, eventData, t) : value.iconCls;
            ret.disabled = $.isFunction(value.disabled) ? value.disabled.call(ret, e, rowIndex, rowData, eventData, t) : value.disabled;
            ret.hideOnClick = $.isFunction(value.hideOnClick) ? value.hideOnClick.call(ret, e, rowIndex, rowData, eventData, t) : value.hideOnClick;
            ret.onclick = $.isFunction(value.onclick) ? function (e, item, menu) { value.onclick.call(this, e, rowIndex, rowData, eventData, t, item, menu); } : value.onclick;
            ret.handler = $.isFunction(value.handler) ? function (e, item, menu) { value.handler.call(this, e, rowIndex, rowData, eventData, t, item, menu); } : value.handler;
            if (ret.children && ret.children.length) { ret.children = $.fn.datagrid.extensions.parseRowContextMenuMap(e, rowIndex, rowData, eventData, ret.children, t); }
            return ret;
        });
    };


    $.fn.datagrid.extensions.parseContextMenuEventData = function (t, opts, e) {
        var queryParams = $.fn.datagrid.extensions.parseRemoteQueryParams(opts);
        var pagingParams = $.fn.datagrid.extensions.parsePaginationParams(t, opts);
        return $.extend({}, queryParams, pagingParams, { e: e, grid: t });
    };

    $.fn.datagrid.extensions.parsePaginationParams = function (t, opts) {
        var ret = {};
        if (opts.pagination) {
            var pager = t.datagrid("getPager");
            var pagerOptions = pager.pagination("options");
            var total = pagerOptions.total;
            var pageCount = Math.ceil(parseFloat(total) / parseFloat(pagerOptions.pageSize));
            $.extend(ret, { pager: pager, total: total, pageCount: pageCount });
        }
        return ret;
    };

    $.fn.datagrid.extensions.parseRemoteQueryParams = function (opts) {
        var ret = $.extend({}, opts.queryParams);
        if (opts.pagination) { $.extend(ret, { page: opts.pageNumber, rows: opts.pageSize }); }
        if (opts.sortName) { $.extend(ret, { sort: opts.sortName, order: opts.sortOrder }); }
        ret = $.fn.datagrid.extensions.parsePagingQueryParams(opts, ret);
        return ret;
    };
    /************************  initExtend initContextMenu & initDblClickRow   End  ************************/



    /************************  initExtend initColumnTooltip Begin  ************************/
    var initColumnTooltip = function (t, opts) {
        var rows = t.datagrid("getRows");
        t.datagrid("getPanel").find("div.datagrid-view div.datagrid-body table tr.datagrid-row").each(function () {
            var tr = $(this), index = parseInt(tr.attr("datagrid-row-index")), row = rows[index];
            initColumnRowTooltip(t, opts, index, row, tr);
        });
    };

    var initColumnRowTooltip = function (t, opts, index, row, tr) {
        tr = tr || t.datagrid("getRowDom", index);
        if (opts.rowTooltip) {
            var onShow = function (e) {
                var tt = $(this), text = $.isFunction(opts.rowTooltip) ? opts.rowTooltip.call(tr, index, row) : buildText(row);
                tt.tooltip("update", text);
            };
            tr.each(function () { $.easyui.tooltip.init(this, { onShow: onShow }); });
        } else {
            tr.children("td[field]").each(function () {
                var td = $(this), field = td.attr("field"), colOpts = t.datagrid("getColumnOption", field);
                if (!colOpts || !colOpts.tooltip) { return; }
                var cell = td.find("div.datagrid-cell"), onShow = function (e) {
                    var tt = $(this), text = $.isFunction(colOpts.tooltip) ? colOpts.tooltip.call(cell, row[field], index, row) : row[field];
                    tt.tooltip("update", text);
                };
                $.easyui.tooltip.init(cell, { onShow: onShow });
            });
        }
        function buildText(row) {
            var cols = t.datagrid("getColumns", "all"), content = $("<table></table>").css({ padding: "5px" });;
            $.each(cols, function (i, colOpts) {
                if (!colOpts || !colOpts.field || !colOpts.title) { return; }
                var msg = t.datagrid("getCellDisplay", { field: colOpts.field, index: index });
                content.append("<tr><td style='text-align: right; width: 150px;'>" + colOpts.title + ":</td><td style='width: 250px;'>" + msg + "</td></tr>");
            });
            return content;
        };
    };


    /************************  initExtend initColumnTooltip   End  ************************/
    var initializeRowExtEditor = function (t, opts, index) {
        if (!opts.extEditing) { return; }
        var tr = t.datagrid("getRowDom", index);
        if (!tr.length) { return; }
        var view = t.datagrid("getPanel").find("div.datagrid-view"),
            view1 = view.find("div.datagrid-view1"),
            view2 = view.find("div.datagrid-view2"),
            body = view2.find("div.datagrid-body").css("position", "relative"),
            width = view1.outerWidth(), height = tr.outerHeight(), pos = tr.position(),
            top = pos.top + height + body.scrollTop() - view2.find("div.datagrid-header").outerHeight();
        var p = $("<div class=\"dialog-button datagrid-rowediting-panel\"></div>").appendTo(body).css("top", top).attr("datagrid-row-index", index);
        $("<a></a>").appendTo(p).linkbutton({ plain: false, iconCls: "icon-ok", text: "保存999" }).click(function () {
            t.datagrid("endEdit", index);
            disposeRowExtEditor(t, opts, index);
        });
        $("<a></a>").appendTo(p).linkbutton({ plain: false, iconCls: "icon-cancel", text: "取消" }).click(function () {
            t.datagrid("cancelEdit", index);
            disposeRowExtEditor(t, opts, index);
        });
        var diff = (opts.width - p.outerWidth()) / 2 - width, left = diff > 0 ? diff : 0;
        p.css("left", left);
    };

    var removeRowExtEditor = function (t, body, index) {
        body = body || t.datagrid("getPanel").find("div.datagrid-view div.datagrid-view2 div.datagrid-body");
        body.find("div.datagrid-rowediting-panel[datagrid-row-index=" + index + "]").remove();
    };

    var disposeRowExtEditor = function (t, opts, index) {
        if (!opts.extEditing) { return; }
        body = t.datagrid("getPanel").find("div.datagrid-view div.datagrid-view2 div.datagrid-body");
        removeRowExtEditor(t, body, index);
    };

    var initSingleEditing = function (t, opts, index) {
        var exts = opts._extensionsDatagrid ? opts._extensionsDatagrid : (opts._extensionsDatagrid = {});
        if (opts.singleEditing) { t.datagrid("endEdit", exts.lastEditingIndex); }
        exts.lastEditingIndex = index;
    };
    /************************  initExtend ExtEditor Begin  ************************/


    /************************  initExtend ExtEditor   End  ************************/




    /************************  initExtend Base Begin  ************************/
    var initExtensions = $.fn.datagrid.extensions.initExtensions = function (t, opts) {
        var exts = opts._extensionsDatagrid ? opts._extensionsDatagrid : (opts._extensionsDatagrid = {});
        if (exts._initialized) { return; }

        var fields = t.datagrid("getColumnFields", false);
        exts.fields = $.array.filter(fields, function (val) { return t.datagrid("getColumnOption", val).title ? true : false; });
        exts.fieldOptions = $.array.map(exts.fields, function (val) { return t.datagrid("getColumnOption", val); });
        exts.fieldOptionsBackup = $.array.map(exts.fieldOptions, function (val) { return $.extend({}, val); });
        exts.filterData = [];

        initColumnExtensions();
        initOffset();
        initContextMenu();
        initDblClickRow();
        initAutoEditing();
        initFinishEdit();
        function initColumnExtensions() { initColumnExtendProperties(t, exts); };
        function initOffset() { t.datagrid("setOffset", opts.offset); };
        function initContextMenu() { initHeaderContextMenu(t, opts, exts); initRowContextMenu(t, opts, exts); initHeaderClickMenu(t, opts, exts); };
        function initDblClickRow() { initDblClickRowEvent(t, opts, exts); };
        function initAutoEditing() { initAutoEditingEvent(t, opts, exts); };
        function initFinishEdit() { initFinishEditEvent(t, opts, exts); };

        var rows = t.datagrid("getRows");
        if (!rows || !rows.length) { initHeaderColumnFilterContainer(t, opts, exts); }

        exts._initialized = true;
    };

    $.fn.datagrid.extensions.parseOrderbyParams = function (sortName, sortOrder) {
        sortName = $.string.isNullOrWhiteSpace(sortName) ? "" : $.trim(sortName);
        sortOrder = $.string.isNullOrWhiteSpace(sortOrder) ? "" : $.trim(sortOrder);
        sortOrder = sortOrder.toLowerCase();
        if (sortOrder != "asc" && sortOrder != "desc") { sortOrder = "asc"; }
        return $.trim(sortName + " " + sortOrder);
    };

    $.fn.datagrid.extensions.parsePagingQueryParams = function (opts, param) {
        var ret = $.util.parseMapFunction(param);
        if (opts.pagination) {
            ret.pageNumber = ret.page;
            ret.pageSize = ret.rows;
            ret.pageIndex = ret.pageNumber - 1;
        }
        ret.orderby = $.fn.datagrid.extensions.parseOrderbyParams(ret.sort, ret.order);
        return ret;
    };

    var loader = $.fn.datagrid.extensions.loader = function (param, success, error) {
        var t = $(this), opts = t.datagrid("options");
        initExtensions(t, opts);
        if (!opts.url) { return false; }
        param = $.fn.datagrid.extensions.parsePagingQueryParams(opts, param);
        $.ajax({
            type: opts.method, url: opts.url, data: param, dataType: "json",
            success: function (data) { success(data); },
            error: function () { error.apply(this, arguments); }
        });
    };

    var loadFilter = function (data) {
        return data ? ($.isArray(data) ? { total: data.length, rows: data } : data) : { total: 0, rows: [] };
    };

    var _onLoadSuccess = $.fn.datagrid.defaults.onLoadSuccess;
    var onLoadSuccess = $.fn.datagrid.extensions.onLoadSuccess = function (data) {
        if ($.isFunction(_onLoadSuccess)) { _onLoadSuccess.apply(this, arguments); }
        var t = $(this), opts = t.datagrid("options"),
            exts = opts._extensionsDatagrid ? opts._extensionsDatagrid : (opts._extensionsDatagrid = {});
        initHeaderColumnFilterContainer(t, opts, exts);
        initRowDndExtensions(t, opts);
        initColumnTooltip(t, opts);
    };

    var _onResizeColumn = $.fn.datagrid.defaults.onResizeColumn;
    var onResizeColumn = $.fn.datagrid.extensions.onResizeColumn = function (field, width) {
        if ($.isFunction(_onResizeColumn)) { _onResizeColumn.apply(this, arguments); }
        var t = $(this), opts = t.datagrid("options");
        if (opts.columnFilter) {
            var panel = t.datagrid("getPanel"), colOpts = t.datagrid("getColumnOption", field),
                container = panel.find("div.datagrid-header-filter-container[field=" + field + "]");
            container.width(colOpts.width);
        }
    };

    var _onBeforeEdit = $.fn.datagrid.defaults.onBeforeEdit;
    var onBeforeEdit = $.fn.datagrid.extensions.onBeforeEdit = function (index, row) {
        if ($.isFunction(_onBeforeEdit)) { _onBeforeEdit.apply(this, arguments); }
        var t = $(this), opts = t.datagrid("options");
        initializeRowExtEditor(t, opts, index); //20160520
        initSingleEditing(t, opts, index);
        t.datagrid("getPanel").find("div.datagrid-view div.datagrid-body table tr.datagrid-row").draggable("disable");
    }

    var _onAfterEdit = $.fn.datagrid.defaults.onAfterEdit;
    var onAfterEdit = $.fn.datagrid.extensions.onAfterEdit = function (index, row, changes) {
        if ($.isFunction(_onAfterEdit)) { _onAfterEdit.apply(this, arguments); }
        var t = $(this), opts = t.datagrid("options"),
            exts = opts._extensionsDatagrid ? opts._extensionsDatagrid : (opts._extensionsDatagrid = {});
        disposeRowExtEditor(t, opts, index);
        initHeaderColumnFilterContainer(t, opts, exts);
        initRowDndExtensions(t, opts);
        initColumnRowTooltip(t, opts, index, row);
    }

    var _onCancelEdit = $.fn.datagrid.defaults.onCancelEdit;
    var onCancelEdit = $.fn.datagrid.extensions.onCancelEdit = function (index, row) {
        if ($.isFunction(_onCancelEdit)) { _onCancelEdit.apply(this, arguments); }
        var t = $(this), opts = t.datagrid("options");
        disposeRowExtEditor(t, opts, index);
        initRowDndExtensions(t, opts);
        initColumnRowTooltip(t, opts, index, row);
    };


    /************************  initExtend Base   End  ************************/





    var methods = $.fn.datagrid.extensions.methods = {

        //  覆盖 easyui-datagrid 的原生方法，以支持相应属性、事件和扩展功能；
        updateRow: function (jq, param) { return jq.each(function () { updateRow(this, param); }); },

        //  覆盖 easyui-datagrid 的原生方法，以支持相应属性、事件和扩展功能；
        appendRow: function (jq, row) { return jq.each(function () { appendRow(this, row); }); },

        //  覆盖 easyui-datagrid 的原生方法，以支持相应属性、事件和扩展功能；
        insertRow: function (jq, param) { return jq.each(function () { insertRow(this, param); }); },

        //  覆盖 easyui-datagrid 的原生方法，以支持相应属性、事件和扩展功能；
        beginEdit: function (jq, index) { return jq.each(function () { beginEdit(this, index); }); },

        //  扩展 easyui-datagrid 的自定义方法；判断指定的 data-row(数据行) 是否被 check；该方法的参数 index 表示要判断的行的索引号，从 0 开始计数；
        //  返回值：如果参数 index 所表示的 data-row(数据行) 被 check，则返回 true，否则返回 false。
        isChecked: function (jq, index) { return isChecked(jq[0], index); },

        //  扩展 easyui-datagrid 的自定义方法；判断指定的 data-row(数据行) 是否被 select；该方法的参数 index 表示要判断的行的索引号，从 0 开始计数；
        //  返回值：如果参数 index 所表示的 data-row(数据行) 被 select，则返回 true，否则返回 false。
        isSelected: function (jq, index) { return isSelected(jq[0], index); },

        //  扩展 easyui-datagrid 的自定义方法；判断指定的 data-row(数据行) 是否开启行编辑状态；该方法的参数 index 表示要判断的行的索引号，从 0 开始计数；
        //  返回值：如果参数 index 所表示的 data-row(数据行) 正开启行编辑状态，则返回 true，否则返回 false。
        isEditing: function (jq, index) { return isEditing(jq[0], index); },

        //  扩展 easyui-datagrid 的自定义方法；获取当前表格中第一个开启了编辑状态的数据行的索引号(从 0 开始计数)；
        //  返回值：如果当前表格中存在开启了行编辑状态的行，则返回第一个编辑行的行索引号(从 0 开始计数)；否则返回 -1。
        getEditingRowIndex: function (jq) { return getEditingRowIndex(jq[0]); },

        //  扩展 easyui-datagrid 的自定义方法；获取当前表格中所有开启了行编辑状态的行的索引号(从 0 开始计数)
        //  返回值：返回一个数组，数组中包含当前表格中所有已经开启了行编辑状态的行的索引号(从 0 开始计数)。
        getEditingRowIndexs: function (jq) { return getEditingRowIndexs(jq[0]); },

        //  扩展 easyui-datagrid 的自定义方法；冻结指定的列；该方法的参数 field 表示要冻结的列的 field 值。
        //  返回值：返回表示当前 easyui-datagrid 的 jQuery 链式对象。
        //  注意：此方法在多行表头情况下无效。
        //      当前表格在执行此方法前必须存在至少一个冻结列，否则此方法无效；
        freezeColumn: function (jq, field) { return jq.each(function () { freezeColumn(this, field); }); },

        //  扩展 easyui-datagrid 的自定义方法；取消冻结指定的列；该方法的参数 field 表示要取消冻结的列的 field 值。
        //  返回值：返回表示当前 easyui-datagrid 的 jQuery 链式对象。
        //  注意：此方法在多行表头情况下无效。
        //      当前表格在执行此方法前必须存在至少一个非冻结列，否则此方法无效；
        unfreezeColumn: function (jq, field) { return jq.each(function () { unfreezeColumn(this, field); }); },

        //  扩展 easyui-datagrid 的自定义方法；移动 easyui-datagrid 中的指定 data-row(数据行) ；该方法的参数 param 为 JSON-Object 类型，包含如下属性定义：
        //      target: 表示目标位置的 data-row(数据行) 索引号(从 0 开始计数)；
        //      source: 表示要移动的 data-row(数据行) 索引号(从 0 开始计数)；
        //      point:  表示移动到目标节点 target 的位置，String 类型，可能的值包括：
        //          "top":      表示移动到目标位置 target 的上一格位置；
        //          "bottom":   表示追加为目标位置 target 的下一格位置；
        //  返回值：返回表示当前 easyui-datagrid 的 jQuery 链式对象。
        //  备注：该方法会触发移动行数据的相应事件；
        moveRow: function (jq, param) { return jq.each(function () { moveRow(this, param); }); },

        //  扩展 easyui-datagrid 的自定义方法；移动 easyui-datagrid 中的指定 data-row(数据行) 一行位置；该方法的参数 param 为 JSON-Object 类型，包含如下属性定义：
        //      index: 表示要移动的 data-row(数据行) 索引号(从 0 开始计数)；
        //      point:  表示移动到目标节点 target 的位置，String 类型，可能的值包括：
        //          "up":      表示移动到目标位置 target 的上一格位置；
        //          "down":   表示追加为目标位置 target 的下一格位置；
        //  返回值：返回表示当前 easyui-datagrid 的 jQuery 链式对象。
        //  备注：该方法会触发移动行数据的相应事件；
        shiftRow: function (jq, param) { return jq.each(function () { shiftRow(this, param); }); },

        //  扩展 easyui-datagrid 的自定义方法；获取指定行的下一行数据；该方法的参数 index 表示指定行的行号(从 0 开始)；
        //  返回值：返回指定行的下一行数据，返回值是一个 JSON-Object 对象；
        //      如果指定的行没有下一行数据 (例如该行为最后一行的情况下)，则返回 null。
        nextRow: function (jq, index) { return getNextRow(jq[0], index); },

        //  扩展 easyui-datagrid 的自定义方法；获取指定行的上一行数据；该方法的参数 index 表示指定行的行号(从 0 开始)；
        //  返回值：返回指定行的上一行数据，返回值是一个 JSON-Object 对象；
        //      如果指定的行没有上一行数据 (例如该行为第一行的情况下)，则返回 null。
        prevRow: function (jq, index) { return getPrevRow(jq[0], index); },

        //  扩展 easyui-datagrid 的自定义方法；从 easyui-datagrid 当前页中删除指定的行，并返回该行数据；
        //  该方法的参数 index 表示指定行的行号(从 0 开始)；
        //  返回值：返回 index 所表示的行的数据，返回值是一个 JSON-Object 对象；
        //      如果不存在指定的行(例如 easyui-datagrid 当前页没有数据或者 index 超出范围)，则返回 null。
        popRow: function (jq, index) { return popRow(jq[0], index); },


        //  扩展 easyui-datagrid 的自定义方法；启用当前表格的行拖动功能；该方法无参数；
        //  返回值：返回表示当前 easyui-datagrid 的 jQuery 链式对象。
        enableRowDnd: function (jq) { return jq.each(function () { enableRowDnd(this); }); },

        //  扩展 easyui-datagrid 的自定义方法；禁用当前表格的行拖动功能；该方法无参数；
        //  返回值：返回表示当前 easyui-datagrid 的 jQuery 链式对象。
        disableRowDnd: function (jq) { return jq.each(function () { disableRowDnd(this); }); },

        //  扩展 easyui-datagrid 的自定义方法；移动指定的列到另一位置；该方法的参数 param 为一个 JSON-Object，定义包含如下属性：
        //      target: 表示目标位置列的 field 值；
        //      source: 表示要移动的列的 field 值；
        //      point:  表示移动到目标列的位置，String 类型，可选的值包括：
        //          before: 表示将 source 列移动至 target 列的左侧；
        //          after:  表示将 source 列移动值 target 列的右侧；
        //  返回值：返回表示当前 easyui-datagrid 的 jQuery 链式对象。
        //  注意：此方法在多行表头情况下无效。
        moveColumn: function (jq, param) { return jq.each(function () { moveColumn(this, param); }); },

        //  扩展 easyui-datagrid 的自定义方法；移动指定的列挪动一格位置；该方法的参数 param 为一个 JSON-Object，定义包含如下属性：
        //      field:  表示要挪动的列的 field 值；
        //      porint: 表示挪动 field 列的方式，String 类型，可选的值包括：
        //          before: 表示将该列向左挪动一格；
        //          after:  表示将该列向右挪动一格；
        //  返回值：返回表示当前 easyui-datagrid 的 jQuery 链式对象。
        //  注意：此方法在多行表头情况下无效。
        shiftColumn: function (jq, param) { return jq.each(function () { shiftColumn(this, param); }); },

        //  扩展 easyui-datagrid 的自定义方法；获取指定列的下一格位置列的 列属性(columnOption) 信息；该方法的参数 field 表示指定列的 field 值。
        //  返回值：当前指定列的下一格位置的列的 列属性(columnOption) 信息。
        //      如果不存在指定的列，或者指定列的下一格位置没有其他列，则返回 null。
        nextColumn: function (jq, field) { return getNextColumn(jq[0], field); },

        //  扩展 easyui-datagrid 的自定义方法；获取指定列的上一格位置列的 列属性(columnOption) 信息；该方法的参数 field 表示指定列的 field 值。
        //  返回值：当前指定列的上一格位置的列的 列属性(columnOption) 信息。
        //      如果不存在指定的列，或者指定列的上一格位置没有其他列，则返回 null。
        prevColumn: function (jq, field) { return getPrevColumn(jq[0], field); },

        //  扩展 easyui-datagrid 的自定义方法；删除指定的列；该方法的参数 field 表示要删除的列的 field 值；
        //  返回值：返回表示当前 easyui-datagrid 的 jQuery 链式对象。
        deleteColumn: function (jq, field) { return jq.each(function () { deleteColumn(this, field); }); },

        //  扩展 easyui-datagrid 的自定义方法；删除指定的列并返回该列的 ColumnOption 值；该方法的参数 field 表示要删除的列的 field 值；
        //  返回值：返回参数 field 值所表示的列的 ColumnOption 值。如果当前 easyui-datagrid 不存在该列，则返回 null。
        popColumn: function (jq, field) { return popColumn(jq[0], param); },


        //  获取 easyui-datagrid 中当前页指定列的 DOM-jQuery 元素对象；该函数定义如下参数：
        //      param: 该参数可以定位以下类型：
        //          String 类型：表示要获取的 DOM-jQuery 元素所在的列的 field 名；
        //          JSON-Object 类型：如果定义为该类型，则该参数定义如下属性：
        //              field:  表示要获取的 DOM-jQuery 元素所在的列的 field 名；
        //              header: Boolean 类型值，默认为 false，表示返回的 DOM-jQuery 对象中是否包含 field 表示的列的表头；
        //  返回值：如果当前页存在 field 值指定的列，则返回该列中指定行的 DOM-jQuery 对象，该对象中包含的 DOM 节点级别为一个 td[field=field] 对象；
        //          否则返回一个空的 jQuery 对象。
        //          如果 param 参数定义为 JSON-Object 类型，且 param.header = true，则返回的 DOM-jQuery 对象中将会包含列的表头元素；
        //          如果 param 参数定义为 String 类型或者即使定义为 JSON-Object 类型但 param.header = false，则返回的 DOM-jQuery 对象中不包含列的表头元素。
        getColumnDom: function (jq, param) { return getColumnDom(jq[0], param); },

        //  获取 easyui-datagrid 中当前也指定列所有行的单元格数据所构成的一个数组；该函数定义如下参数：
        //      field: 要获取的数据的列的 field 名；
        //  返回值：返回一个数组，数组中每一个元素都是其数据行的该列的值，数组的长度等于 grid.datagrid("getRows") 的长度；
        //          如果传入的列名不存在，则返回数组的长度同样等于 grid.datagrid("getRows") 的长度，只是每个元素的值都为 undefined.
        getColumnData: function (jq, field) { return getColumnData(jq[0], field); },

        //  获取 easyui-datagrid 中当前页指定行的 DOM-jQuery 对象元素集合；该函数定义如下参数：
        //      index: 表示要获取的 DOM-Jquery 对象元素集合所在当前页的行索引号；
        //  返回值：如果当前页存在 index 指示的行，则返回该行的 DOM-jQuery 对象集合，该集合中包含的 DOM 节点级别为一组 tr class="datagrid-row" 对象；
        //          否则返回一个空的 jQuery 对象。
        getRowDom: function (jq, index) { return getRowDom(jq[0], index); },

        //  获取 easyui-datagrid 中当前页指定行的 rowData；该函数定义如下参数：
        //      index: 表示要获取的 rowData 所在当前页的行索引号，从 0 开始；
        //  返回值：如果当前页存在 index 指示的行，则返回该行的行数据对象（JSON Object 格式）；否则返回 undefined。
        getRowData: function (jq, index) { return getRowData(jq[0], index); },

        //  获取 easyui-datagrid 中当前页指定单元格的 Dom-jQuery 对象元素；该函数定义如下参数：
        //      pos：表示单元格的位置，为一个 JSON-Object 对象，该 JSON 定义如下属性：
        //          field:  表示要获取的单元格位于哪个列；
        //          index:  表示要获取的单元格位于哪个行的行索引号，从 0 开始；
        //  返回值：如果当前页存在指定列的指定行，则返回该列中指定行的 DOM-jQuery 对象，该对象中包含的 DOM 节点级别为一个 div class="datagrid-cell" 对象；
        //          否则返回一个空的 jQuery 对象。
        getCellDom: function (jq, pos) { return getCellDom(jq[0], pos); },

        //  获取 easyui-datagrid 中当前页指定单元格的数据；该函数定义如下参数：
        //  pos：表示单元格的位置，为一个 JSON-Object 对象，该 JSON 定义如下属性：
        //          field:  表示要获取的单元格位于哪个列；
        //          index:  表示要获取的单元格位于哪个行的行索引号，从 0 开始；
        //  返回值：如果当前页存在指定列的指定行，则返回该列中指定行及指定列的单元格数据；否则返回 undefined。
        getCellData: function (jq, pos) { return getCellData(jq[0], pos); },

        //  获取 easyui-datagrid 中当前页指定单元格的显示数据(经过 formatter 格式化后的显示数据)；该函数定义如下参数：
        //  pos：表示单元格的位置，为一个 JSON-Object 对象，该 JSON 定义如下属性：
        //          field:  表示要获取的单元格位于哪个列；
        //          index:  表示要获取的单元格位于哪个行的行索引号，从 0 开始；
        //  返回值：如果当前页存在指定列的指定行，则返回该列中指定行的单元格的显示数据(经过 formatter 格式化后的显示数据)；否则返回 undefined。
        getCellDisplay: function (jq, pos) { return getCellDisplay(jq[0], pos); },

        //  获取 easyui-datagrid 中当前页指定单元格的显示文本(经过 formatter 格式化后的显示文本)；该函数定义如下参数：
        //  pos：表示单元格的位置，为一个 JSON-Object 对象，该 JSON 定义如下属性：
        //          field:  表示要获取的单元格位于哪个列；
        //          index:  表示要获取的单元格位于哪个行的行索引号，从 0 开始；
        //  返回值：如果当前页存在指定列的指定行，则返回该列中指定行的单元格的显示文本(经过 formatter 格式化后的显示文本)；否则返回 undefined。
        getCellDisplayText: function (jq, pos) { return getCellDisplayText(jq[0], pos); },

        //  获取 easyui-datagrid 所有列的 field 所组成的一个数组集合；参数 frozen 可以定义为如下格式：
        //      Boolean 类型值：如果是 true，则表示返回的结果集中包含 frozen(冻结)列，如果是 false 则表示返回的结果集中不包含 frozen(冻结)列；
        //      String 类型值：如果该参数定义为任意 String 类型值，则返回所有列信息(包括 frozen 冻结列和非冻结列)；
        //  返回值：如果 frozen 参数定义为 Boolean 且为 true，则返回所有 frozen(冻结) 列的 field 所构成的一个 Array 数组对象；
        //      如果 frozen 参数定义为 false，则返回所有非 frozen(非冻结) 列的 field 所构成的一个 Array 数组对象；
        //      如果 frozen 定义为任意的 String 类型值，则返回所有列的 field 所构成的一个 Array 数组对象。
        getColumnFields: function (jq, frozen) { return getColumnFields(jq[0], frozen); },

        //  获取 easyui-datagrid 按指定列的去重复项后的行数据集合；该函数定义如下参数：
        //      field:  要获取的数据的列的 field 名；
        //  返回值：返回一个数组，数组中每一个元素都表示一个行数据；
        //      其结果相当于当前 easyui-datagrid 控件调用 getRows 返回后并经过对指定列去重复项后的结果；
        //      如果传入的列名不存在，则返回一个长度为 0 的数组对象.
        getDistinctRows: function (jq, field) { return getDistinctRows(jq[0], field); },

        //  获取 easyui-datagrid 指定列的值去重复项后的数据集合；该函数定义如下参数；
        //      field:  要获取的数据的列的 field 名；
        //  返回值：返回一个数组，数组中的每一个元素都表示某一行的相应 field 属性的值；
        //      其结果相当于当前 easyui-datagrid 控件调用 getColumnData 返回后并经过对指定列去重复项后的结果；
        //      如果传入的列名不存在，则返回一个长度为 0 的数组对象.
        getDistinctColumnData: function (jq, field) { return getDistinctColumnData(jq[0], field); },

        //  获取 easyui-datagrid 所有列的 columnOption 所组成的一个数组集合；参数 frozen 可以定义为如下格式：
        //      Boolean 类型值：如果是 true，则表示返回的结果集中包含 frozen(冻结)列，如果是 false 则表示返回的结果集中不包含 frozen(冻结)列；
        //      String 类型值：如果该参数定义为任意 String 类型值，则返回所有列信息(包括 frozen 冻结列和非冻结列)；
        //  返回值：如果 frozen 参数定义为 Boolean 且为 true，则返回所有 frozen(冻结) 列的 columnOption 所构成的一个 Array 数组对象；
        //      如果 frozen 参数定义为 false，则返回所有非 frozen(非冻结) 列的 columnOption 所构成的一个 Array 数组对象；
        //      如果 frozen 定义为任意的 String 类型值，则返回所有列的 columnOption 所构成的一个 Array 数组对象。
        getColumns: function (jq, frozen) { return getColumns(jq[0], frozen); },

        //  同 getColumns 方法，但是仅返回列的 columnOption.hidden 值为 true 的列。
        getHiddenColumns: function (jq, frozen) { return getHiddenColumns(jq[0], frozen); },

        //  同 getColumns 方法，但是仅返回列的 columnOption.hidden 值为 false 的列。
        getVisibleColumns: function (jq, frozen) { return getVisibleColumns(jq[0], frozen); },

        //  同 getColumnFields 方法，但是仅返回列的 columnOption.hidden 值为 true 的列。
        getHiddenColumnFields: function (jq, frozen) { return getHiddenColumnFields(jq[0], frozen); },

        //  同 getColumnFields 方法，但是仅返回列的 columnOption.hidden 值为 false 的列。
        getVisibleColumnFields: function (jq, frozen) { return getVisibleColumnFields(jq[0], frozen); },

        //  显示当前 easyui-datagrid 当前页数据中指定行的数据；该方法的参数 param 可以是以下两种类型：
        //      待查找的行数据的 idField(主键) 字段值；
        //      function 类型，该回调函数签名为 function(row, index, rows)，其中 row 表示行数据对象、index 表示行索引号、rows 表示当前 easyui-datagrid 调用 getRows 返回的结果集；
        //          如果 param 参数为 function 类型，则 findRow 方法会对当前 easyui-datagrid 的当前页的每一行数据调用该回调函数；
        //          当回调函数返回 true 时，则表示找到需要查找的结果，立即停止循环调用并显示该行数据；
        //          如果回调函数始终未返回 true，则该回调函数会一直遍历 rows 直到最后。
        //  返回值：返回表示当前 easyui-datagrid 组件的 jQuery 链式对象。
        showRow: function (jq, param) { return jq.each(function () { showRow(this, param); }); },

        //  隐藏当前 easyui-datagrid 当前页数据中指定行的数据；该方法的参数 param 可以是以下两种类型：
        //      待查找的行数据的 idField(主键) 字段值；
        //      function 类型，该回调函数签名为 function(row, index, rows)，其中 row 表示行数据对象、index 表示行索引号、rows 表示当前 easyui-datagrid 调用 getRows 返回的结果集；
        //          如果 param 参数为 function 类型，则 findRow 方法会对当前 easyui-datagrid 的当前页的每一行数据调用该回调函数；
        //          当回调函数返回 true 时，则表示找到需要查找的结果，立即停止循环调用并隐藏该行数据；
        //          如果回调函数始终未返回 true，则该回调函数会一直遍历 rows 直到最后。
        //  返回值：返回表示当前 easyui-datagrid 组件的 jQuery 链式对象。
        hideRow: function (jq, param) { return jq.each(function () { hideRow(this, param); }); },

        //  显示当前 easyui-datagrid 当前页数据中指定多行的数据；该方法的参数 param 可以是以下三种类型：
        //      Function 类型，该回调函数签名为 function(row, index, rows)，其中 row 表示行数据对象、index 表示行索引号、rows 表示当前 easyui-datagrid 调用 getRows 返回的结果集；
        //          如果 param 参数为 Function 类型，则 showRows 方法会对当前 easyui-datagrid 的当前页的每一行数据调用该回调函数；
        //          当回调函数返回 true 时，则该行数据将会被显示；
        //      Array 类型，数组中的每一项都可以定义为如下类型：
        //          待查找的行数据的 idField(主键) 字段值；
        //          Function 类型；具体回调函数签名参考 showRow 方法中 param 参数为 function 类型时的定义；
        //          当 param 参数定义为 Array 类型时，则 showRows 方法会对数组中的每一项循环调用 showRow 方法；
        //      Boolean 类型且为 true：则 showRows 将会显示 easyui-datagrid 当前页的所有数据。
        //  返回值：返回表示当前 easyui-datagrid 组件的 jQuery 链式对象。
        showRows: function (jq, param) { return jq.each(function () { showRows(this, param); }); },

        //  隐藏当前 easyui-datagrid 当前页数据中指定多行的数据；该方法的参数 param 可以是以下三种类型：
        //      Function 类型，该回调函数签名为 function(row, index, rows)，其中 row 表示行数据对象、index 表示行索引号、rows 表示当前 easyui-datagrid 调用 getRows 返回的结果集；
        //          如果 param 参数为 Function 类型，则 hideRows 方法会对当前 easyui-datagrid 的当前页的每一行数据调用该回调函数；
        //          当回调函数返回 true 时，则该行数据将会被隐藏；
        //      Array 类型，数组中的每一项都可以定义为如下类型：
        //          待查找的行数据的 idField(主键) 字段值；
        //          Function 类型；具体回调函数签名参考 hideRow 方法中 param 参数为 function 类型时的定义；
        //          当 param 参数定义为 Array 类型时，则 hideRows 方法会对数组中的每一项循环调用 hideRow 方法；
        //      Boolean 类型且为 true：则 hideRows 将会隐藏 easyui-datagrid 当前页的所有数据。
        //  返回值：返回表示当前 easyui-datagrid 组件的 jQuery 链式对象。
        hideRows: function (jq, param) { return jq.each(function () { hideRows(this, param); }); },

        //  获取当前 easyui-datagrid 当前页所有隐藏的行数据所构成的一个 Array 对象。
        getHiddenRows: function (jq) { return getHiddenRows(jq[0]); },

        //  获取当前 easyui-datagrid 当前页所有显示的行数据所构成的一个 Array 对象。
        getVisibleRows: function (jq) { return getVisibleRows(jq[0]); },

        //  使当前 easyui-datagrid 中指定的列 DOM 对象高亮显示；该函数定义如下参数：
        //      field: 要高亮显示的列的 field 名；
        //  返回值：返回表示当前 easyui-datagrid 组件的 jQuery 链式对象。
        highlightColumn: function (jq, field) { return jq.each(function () { highlightColumn(this, field); }); },

        //  对当前 easyui-datagrid 中进行高亮关键词查询；该方法的 param 可以定义为如下两种类型：
        //      1、String 类型值：表示要对所有列进行的高亮查询关键词；
        //      2、JSON-Object：表示对特定列进行高亮查询的参数，该对象类型参数包含如下属性：
        //          field:      表示要进行高亮查询的列；
        //          value:      表示要进行高亮查询的关键词；
        //          regular:    Boolean 类型值，默认为 false；指示该关键词是否为正则表达式；
        //          ignoreCase: Boolean 类型值，默认为 true；指示高亮查询时是否忽略大小写。
        //  返回值：返回表示当前 easyui-datagrid 组件的 jQuery 链式对象。
        livesearch: function (jq, param) { return jq.each(function () { livesearch(this, param); }); },

        //  检测当前 easyui-datagrid 控件是否存在多行表头；
        //  返回值：如果当前 easyui-datagrid 控件设置了多行表头，则返回 true，否则返回 false。
        hasMuliRowHeader: function (jq) { return hasMuliRowHeader(jq[0]); },

        //  查找当前数据页上的行数据，返回的是一个 JSON 对象；参数 param 表示查找的内容；该方法的参数 param 可以是以下两种类型：
        //      待查找的行数据的 idField(主键) 字段值；
        //      function 类型，该回调函数签名为 function(row, index, rows)，其中 row 表示行数据对象、index 表示行索引号、rows 表示当前 easyui-datagrid 调用 getRows 返回的结果集；
        //          如果 param 参数为 function 类型，则 findRow 方法会对当前 easyui-datagrid 的当前页的每一行数据调用该回调函数；
        //          当回调函数返回 true 时，则表示找到需要查找的结果，立即停止循环调用并返回该行数据；
        //          如果回调函数始终未返回 true，则该回调函数会一直遍历 rows 直到最后并返回 null。
        //  返回值：返回一个 JSON-Object，表示一个行数据对象；如果未找到相应数据，则返回 null。
        findRow: function (jq, param) { return findRow(jq[0], param); },

        //  查找当前数据页上的行数据；该方法的参数 param 可以是以下两种类型：
        //      Function 类型，该回调函数签名为 function(row, index, rows)，其中 row 表示行数据对象、index 表示行索引号、rows 表示当前 easyui-datagrid 调用 getRows 返回的结果集；
        //          如果 param 参数为 Function 类型，则 findRows 方法会对当前 easyui-datagrid 的当前页的每一行数据调用该回调函数；
        //          当回调函数返回 true 时，则返回的结果集中将会包含该行数据；
        //          如果该回调函数始终未返回 true，则该方法最终返回一个长度为 0 的数组对象。
        //      Array 类型，数组中的每一项都可以定义为如下类型：
        //          待查找的行数据的 idField(主键) 字段值；
        //          Function 类型；具体回调函数签名参考 findRow 方法中 param 参数为 function 类型时的定义；
        //          当 param 参数定义为 Array 类型时，则 findRows 方法会对数组中的每一项循环调用 findRow 方法，并过滤掉 findRow 方法返回 null 的结果行；
        //  返回值：返回一个 Array 数组对象；数组中的每一项都是 JSON-Object 类型，表示一个行数据对象；如果未找到相应数据，则返回一个长度为 0 的数组对象。
        findRows: function (jq, param) { return findRows(jq[0], param); },

        //  删除一行数据，重写 easyui-datagrid 本身的 deleteRow 方法；参数 param 表示要删除的内容；该参数可以是以下三种类型：
        //      Number 类型，表示要删除的行索引号；
        //      //表示要删除的行数据的 idField(主键) 字段值，或者行数据对象；
        //      Function 类型，该回调函数签名为 function(row, index, rows)，其中 row 表示行数据对象、index 表示行索引号、rows 表示当前 easyui-datagrid 调用 getRows 返回的结果集；
        //          如果 param 参数为 Function 类型，则 deleteRow 方法会对当前 easyui-datagrid 的当前页的每一行数据调用该回调函数；
        //          当回调函数返回 true 时，则表示查找到了需要被删除的行，deleteRow 方法将会删除该行数据并立即停止和跳出循环操作；
        //  返回值：返回表示当前 easyui-datagrid 组件的 jQuery 链式对象。
        deleteRow: function (jq, param) { return jq.each(function () { deleteRow(this, param); }); },

        //  删除多行数据，参数 param 表示要删除的内容；该参数可以是以下两种类型：
        //      Function 类型，该回调函数签名为 function(row, index, rows)，其中 row 表示行数据对象、index 表示行索引号、rows 表示当前 easyui-datagrid 调用 getRows 返回的结果集；
        //          如果 param 参数为 Function 类型，则 deleteRows 方法会对当前 easyui-datagrid 的当前页的每一行数据调用该回调函数；
        //          当回调函数返回 true 时，则表示查找到了需要被删除的行，deleteRows 方法将会删除该行数据，并遍历下一行数据直至数数据集的末尾；
        //      Array 类型，数组中的每一项目均表示要删除的行的行索引号或者 idField(主键) 字段值或者行数据对象
        //  返回值：返回表示当前 easyui-datagrid 组件的 jQuery 链式对象。
        deleteRows: function (jq, param) { return jq.each(function () { deleteRows(this, param); }); },

        //  设置 easyui-datagrid 中列的标题；参数 param 是一个 json 对象，包含如下属性：
        //      field: 列字段名称
        //      title: 列的新标题
        //  返回值：返回表示当前 easyui-datagrid 组件的 jQuery 链式对象。
        setColumnTitle: function (jq, param) { return jq.each(function () { setColumnTitle(this, param); }); },

        //  设置 easyui-datagrid 中列的宽度；参数 param 是一个 JSON 对象，该 JSON 对象定义如下属性：
        //      field: 要设置列宽的的列 field 值；
        //      width: 要设置的列宽度，Number 类型值。
        //  返回值：返回表示当前 easyui-datagrid 组件的 jQuery 链式对象。
        setColumnWidth: function (jq, param) { return jq.each(function () { setColumnWidth(this, param); }); },

        //  设置当前 easyui-datagrid 控件的 offset 属性；该操作能让 offset 即可随浏览器窗口大小调整而生效或禁用；
        //  备注： 参数 offset 格式参考扩展属性 offset。
        //  返回值：返回表示当前 easyui-datagrid 组件的 jQuery 链式对象。
        setOffset: function (jq, offset) { return jq.each(function () { setOffset(this, offset); }); },

        //  设置当前 easyui-datagrid 控件的表头过滤器；该函数提供如下参数：
        //      columnFilter: 参见属性 columnFilter
        //  返回值：返回表示当前 easyui-datagrid 组件的 jQuery 链式对象。
        setColumnFilter: function (jq, columnFilter) { return jq.each(function () { setColumnFilter(this, columnFilter); }); },

        //  对当前 easyui-datagrid 控件按特定条件进行行过滤/显示操作；该方法的参数 param 可以定义为如下两种类型：
        //      1、Boolean 类型：如果定义为该类型，则：
        //          如果值定义为 true，则表示选中所有的数据全部不过滤；
        //          如果值定义为 false，则表示清空所有的数据全部过滤掉而不显示；
        //      2、JSON-Object 类型：如果定义为该类型，则该参数定义包含如下属性：
        //          field:  String 类型，表示要操作的列的 field 值；
        //          selected：Boolean，表示要对 field 所指示的列进行过滤操作的类型：
        //              如果定义为 true，则表示进行选中操作；
        //              如果定义为 false，则表示进行过滤操作；
        //          value:  表示要对 field 所指示的列进行过滤操作的值，该参数可以定义为如下类型：
        //              Array 类型：表示一组要进行过滤操作的值；
        //              非 Array 类型：表示要进行过滤操作的值；
        //  返回值：返回表示当前 easyui-datagrid 组件的 jQuery 链式对象。
        columnFilterSelect: function (jq, param) { return jq.each(function () { columnFilterSelect(this, param); }); },

        //  将当前表格数据导出为 excel 文件；该函数定义了一个参数 isAll；
        //  参数 isAll 指示是否导出全部而非仅当前页数据，如果不传入该参数默认为 false 即导出当前页数据。
        //  当参数 isAll 为 true 并且 remotePaging 为 true 时，需要当前 easyui-datagrid 控件的 url 属性指示的服务器数据源支持查询所有数据
        //      （以 rows: 0 方式不分页查询所有数据）。
        //  返回值：返回表示当前 easyui-datagrid 组件的 jQuery 链式对象。
        exportExcel: function (jq, isAll) { return jq.each(function () { exportGrid(this, isAll); }); }

    };
    var defaults = $.fn.datagrid.extensions.defaults = {

        //  增加 easyui-datagrid 的自定义扩展属性，该属性表示当屏幕大小调整时候随屏幕大小尺寸调整而自身大小调整的偏移量；
        //  该参数为一个 JSON 格式对象，该对象定义如下属性：
        //      width: 表示相对于浏览器窗口宽度的偏移量，如果是正数则其宽度比浏览器窗口大，反之则其宽度比浏览器窗口小，int类型；
        //      height: 表示相对于浏览器窗口高度的偏移量，如果是正数则其高度比浏览器窗口大，反之则其高度比浏览器窗口小，int类型；
        //  备注：该参数默认为 null，表示不随屏幕尺寸大小调整而调整；
        //      如果未定义 width 或者 width: 0，则表示屏幕大小调整时 easyui-datagrid 的 width 属性撑满屏幕宽度；
        //      如果未定义 height 或者 height: 0，则表示屏幕大小调整时 easyui-datagrid 的 height 属性撑满屏幕宽度；
        offset: null,

        //  覆盖 easyui-datagrid 的原生属性 loadFilter，以支持相应扩展功能。
        loadFilter: loadFilter,

        //  增加 easyui-datagrid 的自定义扩展属性；
        //      该属性表示当设定了属性 rowContextMenu 时，是否将双击数据行 onDblClickRow 事件的响应函数
        //      设置为 rowContextMenu 的第 "dblClickRowMenuIndex" 个菜单项的点击响应函数，并将该菜单项的字体加粗；
        //  Boolean 类型值，默认为 true；
        //  备注：当设置了有效的属性 rowContextMenu 时候，该功能方有效。
        //      自动绑定的 onDblClickRow 的回调函数中将会调用 rowContextMenu 的第 "dblClickRowMenuIndex" 个菜单项的点击响应函数，但是回调函数中不能用到参数 e 和 menu。
        autoBindDblClickRow: true,

        //  增加 easyui-datagrid 的自定义扩展属性；
        //  该属性表示当设定了属性 autoBindDblClickRow: true，双击行数据触发的右键菜单项事件的索引号；
        //      意即触发第几个右键菜单项上的事件。
        //  Number 类型值，从 0 开始计数，默认为 0；
        //  备注：当设置了自定义属性 autoBindDblClickRow: true并且设置了有效的属性 rowContextMenu 时，该功能方有效；
        //      如果此索引值超出菜单数量范围，则无效。
        dblClickRowMenuIndex: 0,

        //  增加 easyui-datagrid 的自定义扩展属性，该属性表示是否启用右键点击表头或者行数据时候弹出菜单中具有 "导出数据" 菜单的功能；
        //  该属性可以定义为以下类型：
        //      Boolean 类型值，表示是否启用右键菜单中的“导出数据”菜单项功能，默认为 false。
        //      JSON-Object 类型，该 JSON-Object 可以包含如下属性：
        //          current:   Boolean 类型值，表示是否启用“导出当前页”的菜单项，默认为 true；
        //          all:   Boolean 类型值，表示是否启用“导出全部”的菜单项，默认为 true；
        //          submenu:    表示这四个菜单项是否以子菜单方式呈现，默认为 true；
        //  备注：当 enableRowContextMenu 属性设置为 true 时，该属性才有效。
        //  导出数据功能的方法尚未实现，所以...就让它保持默认为 false 吧。
        exportMenu: false,

        //  增加 easyui-datagrid 的自定义扩展属性，Boolean 类型值，该属性表示是否启用：
        //      当右键单击行数据时选择右键当前单击的行的功能，默认为 true；
        //  注意：当此参数设置为 true 时，右键点击行会对性能产生一定影响；当时数据量大(单页数据超过 100 行)时不建议使用。
        selectOnRowContextMenu: false,

        //  增加 easyui-datagrid 的自定义扩展属性，Boolean 类型值，该属性表示是否启用：
        //      右键(表头右键或行右键)点击时弹出的菜单项，如果是 disabled: true ，则不显示的功能，默认为 false；
        hideDisabledMenu: false,

        //  增加 easyui-datagrid 的自定义扩展属性，该属性表示表列头右键菜单，为一个 Array 对象；数组中的每一个元素都具有如下属性:
        //      id:         表示菜单项的 id；
        //      text:       表示菜单项的显示文本；
        //      iconCls:    表示菜单项的左侧显示图标；
        //      disabled:   表示菜单项是否被禁用(禁用的菜单项点击无效)；
        //      hideOnClick:    表示该菜单项点击后整个右键菜单是否立即自动隐藏；
        //      bold:           Boolean 类型值，默认为 false；表示该菜单项是否字体加粗；
        //      style:          JSON-Object 类型值，默认为 null；表示要附加到该菜单项的样式；
        //      handler:    表示菜单项的点击事件，该事件函数格式为 function(e, field, eventData, grid, item, menu)，其中 this 指向菜单项本身
        //  备注：具体格式参考 easyui-datagrid 的 toolbar 属性为 Array 对象类型的格式；
        headerContextMenu: null,

        //  增加 easyui-datagrid 的自定义扩展属性，该属性表示数据行右键菜单，为一个 Array 对象；；数组中的每一个元素都具有如下属性:
        //      id:         表示菜单项的 id；
        //      text:       表示菜单项的显示文本；
        //      iconCls:    表示菜单项的左侧显示图标；
        //      disabled:   表示菜单项是否被禁用(禁用的菜单项点击无效)；
        //      hideOnClick:    表示该菜单项点击后整个右键菜单是否立即自动隐藏；
        //      bold:           Boolean 类型值，默认为 false；表示该菜单项是否字体加粗；
        //      style:          JSON-Object 类型值，默认为 null；表示要附加到该菜单项的样式；
        //      handler:    表示菜单项的点击事件，该事件函数格式为 function(e, rowIndex, rowData, eventData, grid, item, menu)，其中 this 指向菜单项本身
        //  备注：具体格式参考 easyui-datagrid 的 toolbar 属性为 Array 对象类型的格式；
        rowContextMenu: null,

        //  增加 easyui-datagrid 的自定义扩展属性，该属性表示是否启用 easyui-datagrid 的表头列点击按钮菜单；
        //  Boolean 类型值，默认为 true。 
        enableHeaderClickMenu: true,

        //  增加 easyui-datagrid 的自定义扩展属性，该属性表示是否启用 easyui-datagrid 的表头右键菜单；
        //  Boolean 类型值，默认为 true。
        enableHeaderContextMenu: true,

        //  增加 easyui-datagrid 的自定义扩展属性，该属性表示是否启用 easyui-datagrid 的行右键菜单；
        //  Boolean 类型值，默认为 true。
        enableRowContextMenu: true,

        //  扩展 easyui-datagrid 的自定义属性，表示是否启用右键菜单中的“上移、下移”菜单项的功能；
        //  该属性可以定义为以下类型：
        //      Boolean 类型，表示是否启用这四个菜单项，默认为 false；
        //      JSON-Object 类型，该 JSON-Object 可以包含如下属性：
        //          top:        布尔类型的值，也可是一个返回布尔值的函数，表示是否显示“移至最上”菜单；
        //          up:         布尔类型的值，也可是一个返回布尔值的函数，表示是否显示“上移”菜单；
        //          down:       布尔类型的值，也可是一个返回布尔值的函数，表示是否显示“下移”菜单；
        //          bottom:     布尔类型的值，也可是一个返回布尔值的函数，表示是否显示“移至最上”菜单；
        //          submenu:    表示这四个菜单项是否以子菜单方式呈现，默认为 true；
        //          上面四个属性，如果参数的值为函数，则函数的签名为 function(e, node, datagrid, item, menu)。
        //  备注：当 enableRowContextMenu 属性设置为 true 时，该属性才有效。
        //      这四个菜单点击时，会自动触发 easyui-datagrid 的 onDrop 事件。
        moveMenu: false,

        //  增加 easyui-datagrid 的自定义扩展属性，该属性表示是否启用右键菜单中的“翻页”菜单项的功能；
        //  该属性可以定义为以下类型：
        //      Boolean 类型值，表示是否启用右键菜单中的“翻页”菜单项功能，默认为 true。
        //      JSON-Object 类型，该 JSON-Object 可以包含如下属性：
        //          disabled:   Boolean 类型值，表示是否禁用右键菜单中的“翻页”菜单项功能，默认为 false；
        //          submenu:    表示这四个菜单项是否以子菜单方式呈现，默认为 true；
        //  备注：当 enableRowContextMenu 属性设置为 true 时，该属性才有效。
        pagingMenu: { submenu: false },

        //  增加 easyui-datagrid 的自定义扩展属性，该属性表示是否启用右键菜单中的“刷新当前页”菜单项的功能；
        //  Boolean 类型值，默认为 true。
        refreshMenu: true,

        //  增加 easyui-datagrid 的自定义扩展属性，该属性表示是否启用表格的行节点拖动功能；
        //  Boolean 类型值，默认为 false。
        dndRow: false,

        //  增加 easyui-datagrid 的自定义扩展属性，该属性表示是否启用行数据的 tooltip 功能；
        //  该属性可以是一个 Boolean 类型值；也可以是一个格式为 function(rowIndex, rowData) 的回调函数；
        //  如果该参数是一个回调函数，则表示启用行数据的 tooltip 功能，并且该函数的返回值为 tooltip 的 content 值。
        //  默认为 Boolean 类型，值为 false。
        //  注意：当启用该配置属性后，所有列的 tooltip 属性就会自动失效。
        rowTooltip: false,

        //  增加 easyui-datagrid 的自定义扩展属性，该属性表示在触发 beginEdit 事件后，是否构建仿 ext-grid-rowediting 行编辑的“保存”和“取消”按钮面板；
        //  Boolean 类型值，默认为 true。
        extEditing: true,

        //  增加 easyui-datagrid 的自定义扩展属性，该属性表示在双击 data-row(数据行) 时，是否自动启用该行的编辑功能(执行 beginEdit 操作)；
        //  Boolean 类型值，默认为 false。
        autoEditing: false,

        //  增加 easyui-datagrid 的自定义扩展属性，该属性表示在 autoEditing: true 时自动触发行编辑效果的事件(双击行还是单击行)。
        //  String 类型值，可选的值为 "onClickRow" 和 "onDblClickRow"，默认为 "onDblClickRow"
        //  注意：该参数仅在 autoEditing: true 时才有效。
        //        因 "onDblClickRow" 在 autoBindDblClickRow: true 时会自动将右键菜单第一项绑定至行双击事件中；
        //        所以建议在 autoBindDblClickRow: true 且行右键菜单第一项为行编辑功能时，autoEditing 设置为 false 或 autoEditingEvent 设置为 "onClickRow"
        autoEditingEvent: "onDblClickRow",

        //  增加 easyui-datagrid 的自定义扩展属性，该属性表示在表格失去焦点(逻辑上失去焦点，实际上是判断页面上表格外的其他部分被点击)后，表格是否自动关闭行编辑状态。
        //  Boolean 类型值，默认为 false。
        finishEditOnBlur: false,

        //  增加 easyui-datagrid 的自定义扩展属性，该属性表示当 finishEditOnBlur: true，点击哪个区域会导致当前表格自动关闭行编辑状态。
        //  该属性可以是一个 HTML-DOM 对象、也可以是一个 jQuery-DOM 对象、或者一个 jquery-DOM selector。默认为 window.document。
        finishEditLocale: window.document,

        //  增加 easyui-datagrid 的自定义扩展属性，该属性表示当 finishEditOnBlur: true 时并且在表格失去焦点后将要触发表格自动关闭行编辑状态时，关闭行编辑状态所使用的方法。
        //  String 类型值，可选的值为 "endEdit" 或 "cancelEdit"，默认为 "endEdit"。
        finishEditMethod: "endEdit",

        //  增加 easyui-datagrid 的自定义扩展属性，该属性表示是否在一个时刻只允许一行数据开启编辑状态(当某行数据开启编辑状态时，其他正在编辑的行将会被自动执行 endEdit 操作)；
        //  Boolean 类型值，默认为 true。
        singleEditing: true,

        //  增加 easyui-datagrid 的自定义扩展属性，该属性表示在对某行执行 beginEdit 后，是否让特定字段的编辑器对象自动获取输入焦点；
        //  该属性可以为 Boolean（默认，true） 类型或者 String 类型值；
        //  如果是 Boolean 类型，则表示是否启用编辑器对象自动获取焦点功能，在值为 true 的情况下该行的第一个编辑器对象将在 beginEdit 操作后自动获取焦点；
        //  如果是 String 类型，其值表示指定的 field 名称，则表示启用该功能并且指定的 field 将在 beginEdit 操作后自动获取焦点。
        autoFocusField: true,

        //  增加 easyui-datagrid 的自定义扩展属性，该属性表示 easyui-datagrid 的数据行在可编辑状态下，当正在进行编辑操作时按下 Enter 键是否对当前行执行 endEdit 操作并对下一行执行 beginEdit 操作。
        //  Boolean 类型值，默认为 true。
        autoWrapEdit: true,

        //  增加 easyui-datagrid 的自定义扩展属性，该属性表示 easyui-datagrid 的数据行在可编辑状态下，当按下 ESC 键时自动执行 cancelEdit 操作。
        cancelEditOnEsc: true,

        //  增加 easyui-datagrid 的自定义扩展属性，该属性表示当前表格的列过滤器设置参数；该参数是一个 JSON 格式的对象，该对象定义如下属性：
        //      panelHeight: 列过滤器控件面板的高度，默认为 100，该值最小为 60；
        //      position:   表示列过滤器的位置，String 类型，可以填入的值限定在以下范围：
        //          "top":  表示列过滤器被放置在表头的上方；
        //          "bottom":   表示列过滤器被放置在表头的下方；默认值。
        //  备注：关于列过滤器组件中每个列具体的过滤效果设置，参见扩展的 ColumnOption 属性(见本源码文件后面注释)；
        //  注意：
        //      1、如果不定义该参数，则表示当前 easyui-datagrid 控件不启用列过滤器功能；该参数不影响表头右键过滤功能；
        //      2、该功能支持多行表头；但不保证在多行表头情况下布局不会出现排版错误；
        //      3、该功能仅实现本地数据过滤，也就是说该插件不会在处理远程数据请求时将过滤参数信息发送到远程服务器；
        //      4、当启用该功能时，easyui-datagrid 的属性 fitColumns 请保持默认值为 false，否则列头过滤器组件可能导致表头列不能对齐而布局混乱。
        columnFilter: null,

        //  增加 easyui-datagrid 的自定义扩展属性，该属性表示是否在分页栏显示当前页面的行过滤数据条目数；
        //      boolean 类型值；
        //  如果该值为 null 或 undefined，则表示分页栏的行过滤数据条目数文本自动判断是否显示；
        showFilterText: undefined,

        //  覆盖 easyui-datagrid 的原生属性 loader，以支持相应扩展功能。调用者请勿在自己的代码中使用该属性，否则扩展功能无效。
        loader: loader,

        //  覆盖 easyui-datagrid 的原生属性事件 onLoadSuccess，以支持相应扩展功能。
        //  注意：如果调用者需要在自己的代码中使用该事件，请以覆盖方式重写，而非直接重写。
        //  覆盖方式重写示例：
        //      grid.datagrid({
        //          onLoadSuccess: function(data) {
        //              $.fn.datagrid.extensions.onLoadSuccess.apply(this, arguments);  //这句一定要加上。
        //              ...                                     //这里是调用者的其他附加逻辑代码
        //          }
        //      });
        onLoadSuccess: onLoadSuccess,

        //  覆盖 easyui-datagrid 的原生属性事件 onResizeColumn，以支持相应扩展功能。
        //  注意：如果调用者需要在自己的代码中使用该事件，请以覆盖方式重写，而非直接重写。
        //  覆盖方式重写示例：
        //      grid.datagrid({
        //          onResizeColumn: function(data) {
        //              $.fn.datagrid.extensions.onResizeColumn.apply(this, arguments);  //这句一定要加上。
        //              ...                                     //这里是调用者的其他附加逻辑代码
        //          }
        //      });
        onResizeColumn: onResizeColumn,

        //  覆盖 easyui-datagrid 的原生属性事件 onBeforeEdit，以支持相应扩展功能。
        onBeforeEdit: onBeforeEdit,

        //  覆盖 easyui-datagrid 的原生属性事件 onAfterEdit，以支持相应扩展功能。
        //  注意：如果调用者需要在自己的代码中使用该事件，请以覆盖方式重写，而非直接重写。
        //  覆盖方式重写示例：
        //      grid.datagrid({
        //          onAfterEdit: function(data) {
        //              $.fn.datagrid.extensions.onAfterEdit.apply(this, arguments);  //这句一定要加上。
        //              ...                                     //这里是调用者的其他附加逻辑代码
        //          }
        //      });
        onAfterEdit: onAfterEdit,

        //  覆盖 easyui-datagrid 的原生属性事件 onCancelEdit，以支持相应扩展功能。
        onCancelEdit: onCancelEdit,

        //  扩展 easyui-datagrid 的自定义事件；该事件表示删除指定的列前触发的动作；该事件回调函数提供如下参数：
        //      field:  表示要被删除的列的 field 值。
        //  备注：如果该事件回调函数返回 false，则不进行删除列的操作。
        //  该事件函数中的 this 指向当前 easyui-datagrid 的 DOM 对象(非 jQuery 对象)；
        onBeforeDeleteColumn: function (field) { },

        //  扩展 easyui-datagrid 的自定义事件；该事件表示删除指定的列后触发的动作；该事件回调函数提供如下参数：
        //      field:  表示要被删除的列的 field 值。
        //  该事件函数中的 this 指向当前 easyui-datagrid 的 DOM 对象(非 jQuery 对象)；
        onDeleteColumn: function (field) { },

        //  扩展 easyui-datagrid 的自定义事件；该事件表示移动指定的列前触发的动作；该事件回调函数提供如下参数：
        //      source:  表示要被移动的列的 field 值。
        //      target:  表示目标位置的列的 field 值。
        //      point :  表示移动的方式；这是一个 String 类型值，可能的值包括：
        //          "before":   表示将列 source 移动至列 target 的前一格位置；
        //          "after" :   表示将列 source 移动至列 target 的后一格位置；
        //  备注：如果该事件回调函数返回 false，则不进行删除列的操作。
        //  该事件函数中的 this 指向当前 easyui-datagrid 的 DOM 对象(非 jQuery 对象)；
        onBeforeMoveColumn: function (source, target, point) { },

        //  扩展 easyui-datagrid 的自定义事件；该事件表示移动指定的列后触发的动作；该事件回调函数提供如下参数：
        //      source:  表示要被移动的列的 field 值。
        //      target:  表示目标位置的列的 field 值。
        //      point :  表示移动的方式；这是一个 String 类型值，可能的值包括：
        //          "before":   表示将列 source 移动至列 target 的前一格位置；
        //          "after" :   表示将列 source 移动至列 target 的后一格位置；
        //  该事件函数中的 this 指向当前 easyui-datagrid 的 DOM 对象(非 jQuery 对象)；
        onMoveColumn: function (source, target, point) { },

        //  扩展 easyui-datagrid 的自定义事件；该事件表示移动 data-row(数据行) 之前触发的动作；该事件回调函数提供如下三个参数：
        //      target: 表示目标位置的 data-row(数据行) 索引号(从 0 开始计数)；
        //      source: 表示要移动的 data-row(数据行) 索引号(从 0 开始计数)；
        //      point:  表示移动到目标节点 target 的位置，String 类型，可能的值包括：
        //          "top":      表示移动到目标位置 target 的上一格位置；
        //          "bottom":   表示追加为目标位置 target 的下一格位置；
        //  该事件函数中的 this 指向当前 easyui-datagrid 的 DOM 对象(非 jQuery 对象)；
        //  如果该事件函数返回 false，则会立即停止移动数据行操作；
        onBeforeDrop: function (target, source, point) { },

        //  扩展 easyui-datagrid 的自定义事件；该事件表示移动 data-row(数据行) 之后触发的动作；该事件回调函数提供如下三个参数：
        //      target: 表示目标位置的 data-row(数据行) 索引号(从 0 开始计数)；
        //      source: 表示要移动的 data-row(数据行) 索引号(从 0 开始计数)；
        //      point:  表示移动到目标节点 target 的位置，String 类型，可能的值包括：
        //          "top":      表示移动到目标位置 target 的上一格位置；
        //          "bottom":   表示追加为目标位置 target 的下一格位置；
        //  该事件函数中的 this 指向当前 easyui-datagrid 的 DOM 对象(非 jQuery 对象)；
        onDrop: function (target, source, point) { },

        //  扩展 easyui-datagrid 的自定义事件；该事件表示拖动 data-row(数据行) 之前触发的动作；该事件回调函数提供如下两个参数：
        //      index:  表示被拖动的 data-row(数据行) 的索引号，从 0 开始计数；
        //      row:    表示被拖动的 data-row(数据行) 的行数据对象，是一个 JSON-Object。
        //  该事件函数中的 this 指向当前 easyui-datagrid 的 DOM 对象(非 jQuery 对象)；
        //  备注：如果该事件函数返回 false，则取消当前的拖动 data-row(数据行) 操作。
        onBeforeDrag: function (index, row) { },

        //  扩展 easyui-datagrid 的自定义事件；该事件表示开始拖动 data-row(数据行) 时触发的动作；该事件回调函数提供如下两个参数：
        //      index:  表示被拖动的 data-row(数据行) 的索引号，从 0 开始计数；
        //      row:    表示被拖动的 data-row(数据行) 的行数据对象，是一个 JSON-Object。
        //  该事件函数中的 this 指向当前 easyui-datagrid 的 DOM 对象(非 jQuery 对象)；
        onStartDrag: function (index, row) { },

        //  扩展 easyui-datagrid 的自定义事件；该事件表示结束拖动 data-row(数据行) 时触发的动作；该事件回调函数提供如下两个参数：
        //      index:  表示被拖动的 data-row(数据行) 的索引号，从 0 开始计数；
        //      row:    表示被拖动的 data-row(数据行) 的行数据对象，是一个 JSON-Object。
        //  该事件函数中的 this 指向当前 easyui-datagrid 的 DOM 对象(非 jQuery 对象)；
        onStopDrag: function (index, row) { },

        //  扩展 easyui-datagrid 的自定义事件；该事件表示当有其他的 data-row(数据行) 被拖动至当前 data-row(数据行) 时所触发的动作；该事件回调函数提供如下两个参数：
        //      target: 表示当前 data-row(数据行) 的行数据对象，是一个 JSON-Object；
        //      source: 表示拖动过来的 data-row(数据行) 行数据对象，是一个 JSON-Object。
        //  该事件函数中的 this 指向当前 easyui-datagrid 的 DOM 对象(非 jQuery 对象)；
        //  备注：如果该事件函数返回 false，则立即取消当前的 data-row(数据行) 接收拖动过来对象的操作，并禁用当前 data-row(数据行) 的 droppable 效果；
        onDragEnter: function (target, source) { },

        //  扩展 easyui-datagrid 的自定义事件；该事件表示当有其他的 data-row(数据行) 被拖动至当前 data-row(数据行) 后并在上面移动时所触发的动作；该事件回调函数提供如下两个参数：
        //      target: 表示当前 data-row(数据行) 的行数据对象，是一个 JSON-Object；
        //      source: 表示拖动过来的 data-row(数据行) 行数据对象，是一个 JSON-Object。
        //  该事件函数中的 this 指向当前 easyui-datagrid 的 DOM 对象(非 jQuery 对象)；
        //  备注：如果该事件函数返回 false，则立即取消当前的 data-row(数据行) 接收拖动过来对象的操作，并禁用当前 data-row(数据行) 的 droppable 效果；
        onDragOver: function (target, source) { },

        //  扩展 easyui-datagrid 的自定义事件；该事件表示当有其他的 data-row(数据行) 被拖动至当前 data-row(数据行) 后并拖动离开时所触发的动作；该事件回调函数提供如下两个参数：
        //      target: 表示当前 data-row(数据行) 的行数据对象，是一个 JSON-Object；
        //      source: 表示拖动过来的 data-row(数据行) 行数据对象，是一个 JSON-Object。
        //  该事件函数中的 this 指向当前 easyui-datagrid 的 DOM 对象(非 jQuery 对象)；
        onDragLeave: function (target, source) { },

        //  扩展 easyui-datagrid 的自定义事件；该事件表示执行 updateRow 方法前所触发的动作；该事件回调函数提供如下两个参数：
        //      index:  表示要进行 updateRow 的行的索引号，从 0 开始计数；
        //      row:    表示要进行更新操作的新的行数据对象；
        //  该事件函数中的 this 指向当前 easyui-datarid 的 DOM 对象(非 jQuery 对象)；
        //  备注：如果该事件回调函数返回 false，则立即取消即将要执行的 updateRow 操作。
        onBeforeUpdateRow: function (index, row) { },

        //  扩展 easyui-datagrid 的自定义事件；该事件表示执行 updateRow 方法后所触发的动作；该事件回调函数提供如下两个参数：
        //      index:  表示要进行 updateRow 的行的索引号，从 0 开始计数；
        //      row:    表示要进行更新操作的新的行数据对象；
        //  该事件函数中的 this 指向当前 easyui-datarid 的 DOM 对象(非 jQuery 对象)；
        onUpdateRow: function (index, row) { },

        //  扩展 easyui-datagrid 的自定义事件；该事件表示执行 appendRow 方法前所触发的动作；该事件回调函数提供如下参数：
        //      row:    表示要进行添加行操作的新的行数据对象；
        //  该事件函数中的 this 指向当前 easyui-datarid 的 DOM 对象(非 jQuery 对象)；
        //  备注：如果该事件回调函数返回 false，则立即取消即将要执行的 appendRow 操作。
        onBeforeAppendRow: function (row) { },

        //  扩展 easyui-datagrid 的自定义事件；该事件表示执行 appendRow 方法后所触发的动作；该事件回调函数提供如下参数：
        //      row:    表示要进行添加行操作的新的行数据对象；
        //  该事件函数中的 this 指向当前 easyui-datarid 的 DOM 对象(非 jQuery 对象)；
        onAppendRow: function (row) { },

        //  扩展 easyui-datagrid 的自定义事件；该事件表示执行 insertRow 方法前所触发的动作；该事件回调函数提供如下两个参数：
        //      index:  表示要进行 insertRow 的行的索引号，从 0 开始计数；
        //      row:    表示要进行插入行操作的新的行数据对象；
        //  该事件函数中的 this 指向当前 easyui-datarid 的 DOM 对象(非 jQuery 对象)；
        //  备注：如果该事件回调函数返回 false，则立即取消即将要执行的 insertRow 操作。
        onBeforeInsertRow: function (index, row) { },

        //  扩展 easyui-datagrid 的自定义事件；该事件表示执行 insertRow 方法后所触发的动作；该事件回调函数提供如下两个参数：
        //      index:  表示要进行 insertRow 的行的索引号，从 0 开始计数；
        //      row:    表示要进行插入行操作的新的行数据对象；
        //  该事件函数中的 this 指向当前 easyui-datarid 的 DOM 对象(非 jQuery 对象)；
        onBeforeRow: function (index, row) { },

        onBeforeDeleteRow: function (index, row) { },

        onDeleteRow: function (index, row) { }
    };

    //  另，增加了 easyui-datagrid 中列 columnOption 的部分自定义扩展属性：
    //      tooltip:    可以是一个 Boolean 类型，也可以是一个回调函数类型，表示是否启用该列的 tooptip 效果；
    //          如果该属性为 Boolean 类型，表示是否启用该列的 tooltip；
    //          如果该属性为 Function 类型，则其格式为 function (value, rowIndex, rowData)，表示为该列启用 tooltip 的方式；
    //              该回调函数返回一个 String 类型值，表示 tooltip 的 content 内容。
    //          默认为 Boolean 类型值为 false。
    //      filterable: Boolean 类型，默认为 true；表示是否禁用该列右键菜单中的 "过滤/显示" 菜单；
    //      hidable:    Boolean 类型，默认为 true；表示该列是否可隐藏。
    //      filter:     String 类型；表示该列的过滤器组件的类型；可选的值限定在以下范围：
    //          "none":     表示过滤器为空，即该列无过滤器效果。
    //          "checkbox": 表示过滤器的类型为一组 checkbox 复选框，默认值。
    //          "livebox":  表示过滤器的类型为模糊查询过滤方式；即过滤器组件为一个输入框，改变该输入框的值后，对该列进行按输入值的过滤匹配。
    //          "caps":     表示过滤器的类型为 slider 拖动条控件，且过滤的结果为只显示小于或等于 slider 选定的值；只有该列全部为数值数据时，才能设置为该类型。
    //          "lower":    表示过滤器的类型为 slider 拖动条控件，且过滤的结果为只显示大于或等于 slider 选定的值；只有该列全部为数值数据时，才能设置为该类型。
    //      precision:  Number 类型，默认为 1；表示过滤器类型(filter)为 caps 或 lower 时候，slider 组合控件的输入框的改变值的精度(保留的小数位数)。
    //      step:       Number 类型，默认为 1；表示过滤器类型(filter)为 caps 或 lower 时候，移动 slider 控件时值的改变值的精度(最小改变的刻度)。
    //
    //  备注： 当 filterable 的值设置为 true 时，参数 filter 方有效；
    //         当 filterable 的值设置为 true 且 filter 的值为 "caps" 或 "lower" 时，参数 precision 和 step 方有效。
    var columnOptions = $.fn.datagrid.extensions.columnOptions = {
        tooltip: false,
        filterable: true,
        hidable: true,
        filter: "checkbox",
        precision: 1,
        step: 1
    };


    $.extend($.fn.datagrid.defaults, defaults);
    $.extend($.fn.datagrid.methods, methods);



    var editors = $.fn.datagrid.defaults.editors,
        checkbox_init = editors.checkbox.init,
        datebox_init = editors.datebox.init,
        
        combobox_init = editors.combobox.init,
        combotree_init = editors.combotree.init,
        combogrid_init = editors.combogrid.init;
    $.extend(editors.checkbox, {
        init: function (container, options) {
            return checkbox_init.apply(this, arguments).addClass("datagrid-editable-input datagrid-editable-checkbox");
        },
        setFocus: function (target) {
            $(target).datebox("textbox").focus();
        }
    });
    $.extend(editors.datebox, {
        init: function (container, options) {
            var box = datebox_init.apply(this, arguments);
            box.datebox("textbox").addClass("datagrid-editable-input");
            return box;
        },
        setFocus: function (target) {
            $(target).datebox("textbox").focus();
        }
    });

  

    $.extend(editors.combobox, {
        init: function (container, options) {
            var box = combobox_init.apply(this, arguments);
            box.combobox("textbox").addClass("datagrid-editable-input");
            return box;
        },
        setFocus: function (target) {
            $(target).combobox("textbox").focus();
        }
    });
    $.extend(editors.combotree, {
        init: function (container, options) {
            var box = combotree_init.apply(this, arguments);
            box.combotree("textbox").addClass("datagrid-editable-input");
            return box;
        },
        setFocus: function (target) {
            $(target).combotree("textbox").focus();
        }
    });
    $.extend(editors.combogrid, {
        init: function (container, options) {
            var box = combogrid_init.apply(this, arguments);
            box.combogrid("textbox").addClass("datagrid-editable-input");
            return box;
        },
        setValue: function (target, value) {
            var t = $(target), opts = t.combogrid("options");
            if (value) {
                if (opts.multiple) {
                    if ($.util.likeArrayNotString(value)) {
                        t.combogrid("setValues", value);
                    } else if (typeof value == "string") {
                        t.combogrid("setValues", value.split(opts.separator));
                    } else {
                        t.combogrid("setValue", value);
                    }
                } else {
                    t.combogrid("setValue", value);
                }
            } else {
                t.combogrid("clear");
            }
        },
        setFocus: function (target) {
            $(target).combogrid("textbox").focus();
        }
    });



    $(document).on("keydown", "div.datagrid div.datagrid-editable input.datagrid-editable-input", function (e) {
        switch (e.which) {
            case 13: autoNextRowEdit(); break;
            case 27: autoCancelEdit(); break;
            default: break;
        }
        function autoNextRowEdit() {
            var input = $(e.target), t = input.currentDatagrid(), opts = t.datagrid("options"), isTg = $.data(t[0], "treegrid") ? true : false;
            if (!opts.autoWrapEdit || isTg) { return; }
            var rows = t.datagrid("getRows");
            if (!rows || !rows.length) { return; }
            var len = rows.length, field = input.closest("td[field]").attr("field"),
                index = window.parseInt(input.closest("tr[datagrid-row-index]").attr("datagrid-row-index"));
            t.datagrid("endEdit", index);
            if (index < len - 1) { t.datagrid("beginEdit", index + 1); }
        }
        function autoCancelEdit() {
            var input = $(e.target), t = input.currentDatagrid(), opts = t.datagrid("options"), isTg = $.data(t[0], "treegrid") ? true : false;
            if (!opts.cancelEditOnEsc || isTg) { return; }
            var index = window.parseInt(input.closest("tr[datagrid-row-index]").attr("datagrid-row-index"));
            t.datagrid("cancelEdit", index);
        }
    });

})(jQuery);

