/**
* jQuery EasyUI 1.3.6
* Copyright (c) 2009-2014 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL or commercial licenses
* To use it on other terms please contact author: info@jeasyui.com
* http://www.gnu.org/licenses/gpl.txt
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI treegrid Extensions 1.0 beta
* jQuery EasyUI treegrid 组件扩展
* jeasyui.extensions.treegrid.js
* http://jqext.sinaapp.com/
*
* 依赖项：
*   1、jquery.jdirk.js v1.0 beta late
*   2、jeasyui.extensions.js v1.0 beta late
*   3、jeasyui.extensions.menu.js v1.0 beta late
*   4、jeasyui.extensions.tree.js v1.0 beta late
*   6、jeasyui.extensions.datagrid.js v1.0 beta late
*   6、jeasyui.extensions.panel.js v1.0 beta late 和 jeasyui.extensions.window.js v1.0 beta late(可选)
*
* Copyright (c) 2013-2014 ChenJianwei personal All rights reserved.
* http://www.chenjianwei.org
*/
(function ($, undefined) {

    $.fn.treegrid.extensions = {};

    /************************  initExtend Methods Begin  ************************/

    var _update = $.fn.treegrid.methods.update;
    var _append = $.fn.treegrid.methods.append;
    var _insert = $.fn.treegrid.methods.insert;
    var updateRow = function (target, param) {
        if (!param || param.id == undefined || !param.row) { return; }
        var t = $(target), opts = t.treegrid("options");
        if ($.isFunction(opts.onBeforeUpdate) && opts.onBeforeUpdate.call(target, param.id, param.row) == false) { return; }
        _update.call(t, t, param);
        initHeaderColumnFilterContainer(t, opts);
        initRowDndExtensions(t, opts);
        initColumnRowTooltip(t, opts, param.id, param.row);
        if ($.isFunction(opts.onUpdate)) { opts.onUpdate.call(target, param.id, param.row); }
    };
    var appendRow = function (target, param) {
        if (!param || !param.data) { return; }
        var t = $(target), opts = t.treegrid("options");
        if ($.isFunction(opts.onBeforeAppend) && opts.onBeforeAppend.call(target, param.parent, param.data) == false) { return; }
        _append.call(t, t, param);
        initHeaderColumnFilterContainer(t, opts);
        initRowDndExtensions(t, opts);
        $.each(param.data, function () { initColumnRowTooltip(t, opts, this[opts.idField], this); });
        if ($.isFunction(opts.onAppend)) { opts.onAppend.call(target, param.parent, param.data); }
    };
    var insertRow = function (target, param) {
        if (!param || !param.data || (!param.before && !param.after)) { return; }
        var t = $(target), opts = t.treegrid("options");
        if ($.isFunction(opts.onBeforeInsert) && opts.onBeforeInsert.call(target, param.before, param.after, param.data) == false) { return; }
        _insert.call(t, t, param);
        initHeaderColumnFilterContainer(t, opts);
        initRowDndExtensions(t, opts);
        initColumnRowTooltip(t, opts, param.data[opts.idField], param.data);
        resetTeeIndent(t, opts, param.data[opts.idField], opts.treeField);
        if ($.isFunction(opts.onInsert)) { opts.onInsert.call(target, param.before, param.after, param.data); }
    };
    var resetTeeIndent = function (t, opts, id, field) {
        var dom = t.treegrid("getCellDom", { field: field, id: id }),
            level = t.treegrid("getLevel", id), child = t.treegrid("getChildren", id);
        while (dom.find("span.tree-indent,span.tree-hit").length < level) {
            $("<span></span>").addClass("tree-indent").prependTo(dom);
        }
        $.each(child, function () { resetTeeIndent(t, opts, this[opts.idField], field); });
    };

    var getLevel = function (target, id) {
        var t = $(target), tr = t.treegrid("getRowDom", id);
        if (!tr || !tr.length) { return 0; }
        return tr.eq(0).parentsUntil("div.datagrid-body", "tr.treegrid-tr-tree").length + 1;
    };


    var freezeColumn = function (target, field) {
        var t = $(target), fields = t.treegrid("getColumnFields"), frozenFields = t.treegrid("getColumnFields", true);
        if (!frozenFields || !frozenFields.length || !$.array.contains(fields, field) || $.array.contains(frozenFields, field)) { return; }
        t.treegrid("moveColumn", { source: field, target: frozenFields[frozenFields.length - 1], point: "after" });
    };

    var unfreezeColumn = function (target, field) {
        var t = $(target), fields = t.treegrid("getColumnFields"), frozenFields = t.treegrid("getColumnFields", true);
        if (!fields || !fields.length || $.array.contains(fields, field) || !$.array.contains(frozenFields, field)) { return; }
        t.treegrid("moveColumn", { source: field, target: fields[0], point: "before" });
    };

    var moveColumn = function (target, param) {
        if (!param || !param.source || !param.target || param.source == param.target || !param.point) { return; };
        if (!$.array.contains(["before", "after"], param.point)) { param.point = "before"; }
        var t = $(target);
        if (t.treegrid("hasMuliRowHeader")) { return; }
        var opts = t.treegrid("options"), sourceFrozen, targetFrozen,
            fields = t.treegrid("getColumnFields"), frozenFields = t.treegrid("getColumnFields", true);
        if ($.array.contains(fields, param.source)) { sourceFrozen = false; }
        if (sourceFrozen == undefined && $.array.contains(frozenFields, param.source)) { sourceFrozen = true; }
        if ($.array.contains(fields, param.target)) { targetFrozen = false; }
        if (targetFrozen == undefined && $.array.contains(frozenFields, param.target)) { targetFrozen = true; }
        if (sourceFrozen == undefined || targetFrozen == undefined) { return; }
        if ($.isFunction(opts.onBeforeMoveColumn) && opts.onBeforeMoveColumn.call(target, param.source, param.target, param.point) == false) { return; }
        var panel = t.treegrid("getPanel"), view = panel.find("div.datagrid-view"),
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
            var targetBodyTr = $(this), id = targetBodyTr.attr("node-id");
            var targetBodyTd = targetBodyTr.find("td[field=" + param.target + "]"), sourceBodyTd = $(sourceRow[i]).find("td[field=" + param.source + "]");
            targetBodyTd[param.point](sourceBodyTd);
        });

        var sourceOpts = t.treegrid("getColumnOption", param.source), targetOpts = t.treegrid("getColumnOption", param.target),
            sourceColumns = sourceFrozen ? opts.frozenColumns[0] : opts.columns[0],
            targetColumns = targetFrozen ? opts.frozenColumns[0] : opts.columns[0],
            exts = opts._extensionsTreegrid ? opts._extensionsTreegrid : (opts._extensionsTreegrid = {});
        $.array.remove(sourceColumns, sourceOpts);
        var index = $.array.indexOf(targetColumns, targetOpts);
        if (index > -1) { $.array.insert(targetColumns, param.point == "before" ? index : index + 1, sourceOpts); }

        var treeOpts = opts;
        opts = t.datagrid("options");
        sourceOpts = t.datagrid("getColumnOption", param.source);
        targetOpts = t.datagrid("getColumnOption", param.target);
        sourceColumns = sourceFrozen ? opts.frozenColumns[0] : opts.columns[0];
        targetColumns = targetFrozen ? opts.frozenColumns[0] : opts.columns[0];
        $.array.remove(sourceColumns, sourceOpts);
        var index = $.array.indexOf(targetColumns, targetOpts);
        if (index > -1) { $.array.insert(targetColumns, param.point == "before" ? index : index + 1, sourceOpts); }
        opts = treeOpts;

        if (sourceFrozen || targetFrozen && sourceFrozen != targetFrozen) { var data = t.treegrid("getData"); t.treegrid("loadData", data); } else { t.treegrid("fixColumnSize"); }
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
        var t = $(target), fields = t.treegrid("getColumnFields", "all"),
            index = $.array.indexOf(fields, param.field);
        if (index == -1 || (param.point == "before" && index == 0) || (param.point == "after" && index == fields.length - 1)) { return; }
        var target = fields[param.point == "before" ? index - 1 : index + 1];
        t.treegrid("moveColumn", { source: param.field, target: target, point: param.point });
    };

    var deleteColumn = function (target, field) {
        var t = $(target), opts = t.treegrid("options"),
            exts = opts._extensionsTreegrid ? opts._extensionsTreegrid : (opts._extensionsTreegrid = {});
        if ($.isFunction(opts.onBeforeDeleteColumn) && opts.onBeforeDeleteColumn.call(target, field) == false) { return; }
        $.fn.datagrid.extensions.removeField(opts, field, exts);
        t.treegrid("getColumnDom", { field: field, header: true }).remove();
        if ($.isFunction(opts.onDeleteColumn)) { opts.onDeleteColumn.call(target, field); }
    };

    var popColumn = function (target, field) {
        var t = $(target), colOpts = t.treegrid("getColumnOption", field);
        if (colOpts) { t.treegrid("deleteColumn", field); }
        return colOpts
    };


    var isChecked = function (target, id) {
        var t = $(target), opts = t.treegrid("options"), rows = t.treegrid("getChecked");
        return $.array.contains(rows, id, function (val) { return val[opts.idField] == id; });
    };

    var isSelected = function (target, id) {
        var t = $(target), opts = t.treegrid("options"), rows = t.treegrid("getSelections");
        return $.array.contains(rows, id, function (val) { return val[opts.idField] == id; });
    };

    var isEditing = function (target, id) {
        var t = $(target), panel = t.treegrid("getPanel");
        return panel.find("div.datagrid-view div.datagrid-body table tr.datagrid-row[node-id=" + id + "]").hasClass("datagrid-row-editing");
    };

    var getEditingNodeId = function (target) {
        var array = getEditingNodeIds(target);
        return array.length ? array[0] : undefined;
    };

    var getEditingNodeIds = function (target) {
        var t = $(target), panel = t.treegrid("getPanel"),
            rows = panel.find("div.datagrid-view div.datagrid-body table tr.datagrid-row.datagrid-row-editing").map(function () {
                return $(this).attr("node-id");
            }),
            array = $.array.distinct($.array.clone(rows));
        return array;
    };

    var isRootNode = function (target, id) {
        var t = $(target), roots = t.treegrid("getRoots"), node = t.treegrid("find", id);
        return node && $.array.contains(roots, node);
    };

    var moveRow = function (target, param) {
        if (!param || !param.source || !param.target || !param.point) { return; }
        if (!$.array.contains(["append", "top", "bottom"], param.point)) { param.point = "append"; }
        var t = $(target), opts = t.treegrid("options"),
            sourceNode = t.treegrid("find", param.source), targetNode = t.treegrid("find", param.target);
        if (!sourceNode || !targetNode || sourceNode == targetNode) { return; }
        if ($.isFunction(opts.onBeforeDrop) && opts.onBeforeDrop.call(target, targetNode, sourceNode, param.point) == false) { return; }
        if (t.treegrid("isParent", { id1: param.source, id2: param.target })) { return; }
        var node = t.treegrid("pop", param.source);
        switch (param.point) {
            case "append": t.treegrid("append", { parent: param.target, data: [node] }); break;
            case "top": t.treegrid("insert", { before: param.target, data: node }); break;
            case "bottom": t.treegrid("insert", { after: param.target, data: node }); break;
            default: t.treegrid("append", { parent: param.target, data: [node] }); break;
        }
        if (node && $.isFunction(opts.onDrop)) { opts.onDrop.call(target, targetNode, sourceNode, param.point); }
    };

    var shiftRow = function (target, param) {
        if (!param || !param.id || !param.point || !$.array.contains(["up", "upLevel", "down", "downLevel"], param.point)) { return; }
        var t = $(target), opts = t.treegrid("options"), node;
        switch (param.point) {
            case "up": node = t.treegrid("prevRow", param.id); break;
            case "upLevel": node = t.treegrid("getParent", param.id); break;
            case "down": node = t.treegrid("nextRow", param.id); break;
            case "downLevel": node = t.treegrid("prevRow", param.id); break;
            default: break;
        }
        if (!node) { return; }
        t.treegrid("moveRow", { target: node[opts.idField], source: param.id, point: param.point == "up" ? "top" : (param.point == "downLevel" ? "append" : "bottom") });
    };

    var compareNode = function (target, param) {
        if (isChild(target, param)) { return "child"; }
        if (isParent(target, param)) { return "parent"; }
        if (isSibling(target, param)) { return "sibling"; }
        return "normal";
    };

    var isParent = function (target, param) {
        var t = $(target), node = t.treegrid("find", param.id2);
        var children = t.treegrid("getChildren", param.id1);
        return $.array.contains(children, node);
    };

    var isChild = function (target, param) {
        var t = $(target), node = t.treegrid("find", param.id1);
        var children = t.treegrid("getChildren", param.id2);
        return $.array.contains(children, node);
    };

    var isSibling = function (target, param) {
        var t = $(target), p1 = t.treegrid("getParent", param.id1), p2 = t.treegrid("getParent", param.id2);
        return p1 && p2 && p1 == p2;
    };

    var getNextRow = function (target, id) {
        var t = $(target);
        var row = t.treegrid("getRowDom", id).nextAll("tr.datagrid-row:first"), rowId = row.attr("node-id");
        if (!row.length || !rowId) { return null; }
        return t.treegrid("find", rowId);
    };

    var getPrevRow = function (target, id) {
        var t = $(target);
        var row = t.treegrid("getRowDom", id).prevAll("tr.datagrid-row:first"), rowId = row.attr("node-id");
        if (!row.length || !rowId) { return null; }
        return t.treegrid("find", rowId);
    };

    var getNears = function (target, id) {
        var t = $(target), opts = t.treegrid("options");
        if (t.treegrid("isRoot", id)) { return t.treegrid("getRoots"); }
        var p = t.treegrid("getParent", id);
        if (!p) { return t.treegrid("getRoots"); }
        return t.treegrid("getNearChildren", p[opts.idField]);
    };

    var getNearChildren = function (target, id) {
        var t = $(target), opts = t.treegrid("options"),
            children = t.treegrid("getChildren", id);
        return $.array.filter(children, function (val) { return t.treegrid("getParent", val[opts.idField])[opts.idField] == id; });
    };


    var enableRowDnd = function (target) {
        var t = $(target), opts = t.treegrid("options");
        t.treegrid("getPanel").find("div.datagrid-view div.datagrid-body table tr.datagrid-row").draggable({
            disabled: false, revert: true, cursor: "default", deltaX: 10, deltaY: 5,
            proxy: function (source) {
                var tr = $(source), id = tr.attr("node-id"), dom = t.treegrid("getRowDom", id).clone();
                var temp = $("<tr></tr>").addClass("datagrid-row datagrid-row-selected");
                $("<td><span class='tree-dnd-icon tree-dnd-no' ></span></td>").appendTo(temp);
                var td = dom.find("td").each(function (i) { if (i < 6) { temp.append(this); } });
                if (td.length > 6) { $("<td>...</td>").css("width", "40px").appendTo(temp); }
                return $("<table></table>").addClass("tree-node-proxy").appendTo("body").append(temp).hide();
            }, onBeforeDrag: function (e) {
                var tr = $(this), id = tr.attr("node-id"), row = t.treegrid("find", id);
                if ($.isFunction(opts.onBeforeDrag) && opts.onBeforeDrag.call(target, row) == false) { return false; }
                if (e.which != 1) { return false; }
                if (e.target.type == "checkbox") { return false; }
                t.treegrid("getRowDom", { id: id, cascade: true }).droppable({ accept: "no-accept" });
            }, onStartDrag: function () {
                var tr = $(this), id = tr.attr("node-id"), row = t.treegrid("find", id);
                tr.draggable("proxy").css({ left: -10000, top: -10000 });
                if ($.isFunction(opts.onBeforeDrag)) { opts.onStartDrag.call(target, row); }
            }, onStopDrag: function () {
                var tr = $(this), id = tr.attr("node-id"), row = t.treegrid("find", id);
                t.treegrid("getRowDom", { id: id, cascade: true }).droppable({ accept: "tr.datagrid-row" });
                if ($.isFunction(opts.onStopDrag)) { opts.onStopDrag.call(target, row); }
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
                    droperId = droper.attr("node-id"), dragerId = drager.attr("node-id"),
                    droperRow = t.treegrid("find", droperId), dragerRow = t.treegrid("find", dragerId),
                    droperRowDom = t.treegrid("getRowDom", droperId),
                    mark = droperRowDom.find("td"), treeFieldDom = droperRowDom.find("td[field=" + opts.treeField + "]");
                var dnd = droper.data("dnd"), data = {
                    droper: droper, drager: drager, droperId: droperId, dragerId: dragerId,
                    droperRow: droperRow, dragerRow: dragerRow, droperRowDom: droperRowDom, mark: mark, treeFieldDom: treeFieldDom
                };
                if (!dnd) { droper.data("dnd", data); } else { $.extend(dnd, data); }
                if ($.isFunction(opts.onDragEnter) && opts.onDragEnter.call(target, droperRow, dragerRow) == false) {
                    setDroppableStatus(drager, false);
                    mark.removeClass("datagrid-header-cell-top datagrid-header-cell-bottom");
                    treeFieldDom.removeClass("datagrid-header-cell-append");
                }
            },
            onDragOver: function (e, source) {
                var droper = $(this), dnd = droper.data("dnd"), drager = dnd.drager,
                    droperId = dnd.droperId, dragerId = dnd.dragerId,
                    droperRow = dnd.droperRow, dragerRow = dnd.dragerRow,
                    mark = dnd.mark, treeFieldDom = dnd.treeFieldDom;
                if (droper.droppable("options").disabled) { return; }
                var pageY = source.pageY, top = droper.offset().top, height = top + droper.outerHeight();
                setDroppableStatus(drager, !t.treegrid("isParent", { id1: dragerId, id2: droperId }));
                mark.removeClass("datagrid-header-cell-top datagrid-header-cell-bottom");
                treeFieldDom.removeClass("datagrid-header-cell-append");
                if (pageY > top + (height - top) / 2) {
                    if (height - pageY < 5) {
                        mark.addClass("datagrid-header-cell-bottom");
                    } else {
                        treeFieldDom.addClass("datagrid-header-cell-append");
                    }
                } else {
                    if (pageY - top < 5) {
                        mark.addClass("datagrid-header-cell-top");
                    } else {
                        treeFieldDom.addClass("datagrid-header-cell-append");
                    }
                }
                if (opts.onDragOver.call(target, droperRow, dragerRow) == false) {
                    setDroppableStatus(drager, false);
                    mark.removeClass("datagrid-header-cell-top datagrid-header-cell-bottom");
                    treeFieldDom.removeClass("datagrid-header-cell-append");
                    droper.droppable("disable");
                }
            },
            onDragLeave: function (e, source) {
                var droper = $(this), dnd = droper.data("dnd"), drager = dnd.drager,
                    droperRow = dnd.droperRow, dragerRow = dnd.dragerRow, mark = dnd.mark, treeFieldDom = dnd.treeFieldDom;
                setDroppableStatus(drager, false);
                mark.removeClass("datagrid-header-cell-top datagrid-header-cell-bottom");
                treeFieldDom.removeClass("datagrid-header-cell-append");
                if ($.isFunction(opts.onDragLeave)) { opts.onDragLeave.call(target, droperRow, dragerRow); }
            },
            onDrop: function (e, source) {
                var droper = $(this), dnd = droper.data("dnd"),
                    droperId = dnd.droperId, dragerId = dnd.dragerId, mark = dnd.mark, treeFieldDom = dnd.treeFieldDom,
                    point = treeFieldDom.hasClass("datagrid-header-cell-append") ? "append" : (mark.hasClass("datagrid-header-cell-top") ? "top" : "bottom");
                t.treegrid("moveRow", { target: droperId, source: dragerId, point: point });
                mark.removeClass("datagrid-header-cell-top datagrid-header-cell-bottom");
                treeFieldDom.removeClass("datagrid-header-cell-append");
            }
        });
        opts.dndRow = true;
        function setDroppableStatus(source, state) {
            var icon = source.draggable("proxy").find("span.tree-dnd-icon");
            icon.removeClass("tree-dnd-yes tree-dnd-no").addClass(state ? "tree-dnd-yes" : "tree-dnd-no");
        };
    };

    var disableRowDnd = function (target) {
        var t = $(target), opts = t.treegrid("options");
        t.treegrid("getPanel").find("div.datagrid-view div.datagrid-body table tr.datagrid-row").draggable("disable");
        opts.dndRow = false;
    };







    var getRows = function (target, cascade) {
        var t = $(target), rows = t.treegrid("getRoots"), opts = t.treegrid("options");
        rows = rows && rows.length ? rows : [];
        return cascade ? $.array.reduce(rows, function (prev, val, index) {
            prev.push(val);
            var cc = t.treegrid("getChildren", val[opts.idField]);
            if (cc && cc.length) { $.array.merge(prev, cc); }
            return prev;
        }, []) : rows;
    };

    var getColumnData = function (target, param) {
        param = $.isPlainObject(param) ? param : { field: param, cascade: false };
        var field = param.field, cascade = param.cascade,
            t = $(target), rows = t.treegrid("getRows", cascade);
        return $.array.map(rows, function (val) { return val[field]; });
    };

    var getRowDom = function (target, param) {
        param = $.isPlainObject(param) ? param : { id: param, cascade: false };
        var id = param.id, cascade = param.cascade ? true : false,
            t = $(target), opts = t.treegrid("options"), panel = t.treegrid("getPanel"),
            dom = panel.find(".datagrid-view .datagrid-body tr.datagrid-row[node-id=" + id + "]");
        if (cascade) {
            var children = t.treegrid("getChildren", id);
            $.each(children, function (i, n) { var d = getRowDom(target, n[opts.idField]); dom = dom.add(d); });
        }
        return dom;
    };

    var getNode = function (target, id) {
        return $(target).treegrid("find", id);
    };

    var getCellDom = function (target, pos) {
        if (!pos || !pos.field || pos.id == null || pos.id == undefined) { return $(); }
        var t = $(target), tr = t.treegrid("getRowDom", pos.id);
        return tr.find("td[field=" + pos.field + "] .datagrid-cell");
    };
    var getCellData = function (target, pos) {
        if (!pos || !pos.field || pos.id == null || pos.id == undefined) { return undefined; }
        var t = $(target), row = t.treegrid("find", pos.id);
        return row[pos.field];
    };
    var getCellDisplay = function (target, pos) {
        var t = $(target), cell = t.treegrid("getCellDom", pos);
        return cell && cell.length ? cell.text() : undefined;
    };

    var getDistinctRows = function (target, param) {
        param = $.isPlainObject(param) ? param : { field: param, cascade: false };
        var field = param.field, cascade = param.cascade,
            t = $(target), fields = t.treegrid("getColumnFields", "all");
        if (!$.array.contains(fields, field)) { return []; }
        var rows = t.treegrid("getRows", cascade), data = $.array.clone(rows);
        $.array.distinct(data, function (a, b) { return a[field] == b[field]; });
        return data;
    };

    var getDistinctColumnData = function (target, param) {
        param = $.isPlainObject(param) ? param : { field: param, cascade: false };
        var field = param.field, cascade = param.cascade,
            t = $(target), fields = t.treegrid("getColumnFields", "all");
        if (!$.array.contains(fields, field)) { return []; }
        var data = t.treegrid("getColumnData", { field: field, cascade: cascade });
        $.array.distinct(data, function (a, b) { return a == b; });
        return data;
    };

    var _find = $.fn.treegrid.methods.find;
    var findRow = function (target, param, grid) {
        var t = grid || $(target);
        if (!$.isFunction(param)) { return _find.call(t, t, param); }
        var rows = t.treegrid("getRows", true);
        return $.array.first(rows, param);
    };

    var findRows = function (target, param) {
        var t = $(target), ret;
        if ($.isFunction(param)) {
            ret = $.array.filter(t.treegrid("getRows", true), param);
        } else if ($.array.likeArray(param) && !$.util.isString(param)) {
            ret = $.array.map(param, function (val) { return findRow(target, val, t); });
            ret = $.array.filter(ret, function (val) { return val != undefined && val != null; });
        } else {
            ret = [findRow(target, param, t)];
        }
        return ret;
    };

    var showRow = function (target, param, grid, options, extensions, refreshable) {
        var t = grid || $(target), opts = options || t.treegrid("options"),
            isFunc = $.isFunction(param), val = isFunc ? findRow(target, param, t) : null,
            id = isFunc ? (val ? val[opts.idField] : null) : ($.isPlainObject(param) && (opts.idField in param) ? param[opts.idField] : param);
        if (id == null || id == undefined) { return; }
        var dom = t.treegrid("getRowDom", { id: id, cascade: true }),
            refreshable = (refreshable == null || refreshable == undefined || refreshable == true) ? true : false;
        if (dom.length) {
            var row = isFunc ? val : t.treegrid("find", id),
                exts = extensions || (opts._extensionsTreegrid ? opts._extensionsTreegrid : (opts._extensionsTreegrid = {}));
            exts.filterData = $.isArray(exts.filterData) ? exts.filterData : exts.filterData = [];
            dom.show();
            $.array.remove(exts.filterData, row);
            var children = t.treegrid("getChildren", id);
            $.each(children, function () { $.array.remove(exts.filterData, this); });
            if (refreshable) { refreshColumnFilterStatus(t, opts, exts, rows); }
        }
    };

    var hideRow = function (target, param, grid, options, extensions, refreshable) {
        var t = grid || $(target), opts = options || t.treegrid("options"),
            isFunc = $.isFunction(param), val = isFunc ? findRow(target, param, t) : null,
            id = isFunc ? (val ? val[opts.idField] : null) : ($.isPlainObject(param) && (opts.idField in param) ? param[opts.idField] : param);
        if (id == null || id == undefined) { return; }
        var dom = t.treegrid("getRowDom", { id: id, cascade: true }),
            refreshable = (refreshable == null || refreshable == undefined || refreshable == true) ? true : false;
        if (dom.length) {
            var row = isFunc ? val : t.treegrid("find", id),
                exts = extensions || (opts._extensionsTreegrid ? opts._extensionsTreegrid : (opts._extensionsTreegrid = {}));
            exts.filterData = $.isArray(exts.filterData) ? exts.filterData : exts.filterData = [];
            t.treegrid("unselectRow", { id: id, cascade: true }).treegrid("uncheckRow", { id: id, cascade: true });
            dom.hide();
            $.array.unique(exts.filterData, row, t.treegrid("getChildren", id));
            if (refreshable) { refreshColumnFilterStatus(t, opts, exts, rows); }
        }
    };

    var showRows = function (target, param) {
        var t = $(target), opts = t.treegrid("options"), rows = t.treegrid("getRows", true), array,
            exts = opts._extensionsTreegrid ? opts._extensionsTreegrid : (opts._extensionsTreegrid = {});
        if (param === true) {
            exts.filterData = [];
            var panel = t.treegrid("getPanel"), icons = panel.find("div.datagrid-header-filter-item-icon");
            panel.find(".datagrid-view .datagrid-body tr.datagrid-row").show();
            setItemIconCls(icons, "tree-checkbox1");
            refreshColumnFilterPagerStatus(t, opts);
        } else if ($.isFunction(param)) {
            array = $.array.filter(rows, param);
            array = $.array.map(array, function (val) { return val[opts.idField]; });
        } else if ($.array.likeArray(param) && !$.util.isString(param)) {
            array = param;
        } else { array = [param]; }
        if (array) {
            $.each(array, function (index, val) { showRow(target, val, t, opts, exts, false); });
            refreshColumnFilterStatus(t, opts, exts, t.treegrid("getRows"));
        }
    };

    var hideRows = function (target, param) {
        var t = $(target), opts = t.treegrid("options"), rows = t.treegrid("getRows", true), array,
            exts = opts._extensionsTreegrid ? opts._extensionsTreegrid : (opts._extensionsTreegrid = {});
        if (param === true) {
            t.treegrid("unselectAll").treegrid("uncheckAll");
            exts.filterData = $.array.clone(rows);
            var panel = t.treegrid("getPanel"), icons = panel.find("div.datagrid-header-filter-item-icon");
            panel.find(".datagrid-view .datagrid-body tr.datagrid-row").hide();
            setItemIconCls(icons, "tree-checkbox0");
            refreshColumnFilterPagerStatus(t, opts);
        } else if ($.isFunction(param)) {
            array = $.array.filter(rows, param);
            array = $.array.map(array, function (val) { return val[opts.idField]; });
        } else if ($.array.likeArray(param) && !$.util.isString(param)) {
            array = param;
        } else { array = [param]; }
        if (array) {
            $.each(array, function (index, val) { hideRow(target, val, t, opts, exts, false); });
            refreshColumnFilterStatus(t, opts, exts, t.treegrid("getRows"));
        }
    };

    var getHiddenRows = function (target, cascade) {
        var t = $(target), opts = t.treegrid("options"),
            exts = opts._extensionsTreegrid ? opts._extensionsTreegrid : (opts._extensionsTreegrid = {});
        if (cascade) { return exts.filterData; }
        var roots = t.treegrid("getRoots");
        return $.array.filter(exts.filterData, function (val) { return $.array.contains(roots, val); });
    };

    var getVisibleRows = function (target, cascade) {
        var t = $(target), opts = t.treegrid("options"), rows = t.treegrid("getRows", cascade),
            exts = opts._extensionsTreegrid ? opts._extensionsTreegrid : (opts._extensionsTreegrid = {});
        return $.array.filter(rows, function (val) { return $.array.contains(exts.filterData, val) ? false : true; });
    };


    var _remove = $.fn.treegrid.methods.remove;
    var removeRow = function (target, id) {
        var t = $(target), opts = t.treegrid("options"), row = t.treegrid("find", id);
        if (row && $.isFunction(opts.onBeforeRemove) && opts.onBeforeRemove.call(target, row) != false) {
            _remove.call(t, t, id);
            initHeaderColumnFilterContainer(t, opts);
            if ($.isFunction(opts.onRemove)) { opts.onRemove.call(target, row); }
        }
    };


    var deleteRow = function (target, param) {
        var t = $(target);
        if (!$.isFunction(param)) { t.treegrid("remove", param); }
        var rows = t.treegrid("getRows", true), opts = t.treegrid("options"), row = $.array.first(rows, param), id = row ? row[opts.idField] : null;
        t.treegrid("remove", id);
    };

    var deleteRows = function (target, param) {
        var isArray = $.array.likeArray(param) && !$.util.isString(param);
        if (isArray) { $.each(param, function (index, val) { deleteRow(target, val); }); return; }
        if ($.isFunction(param)) {
            var t = $(target), opts = t.treegrid("options"), rows = t.treegrid("getRows", true), data = $.array.filter(rows, param);
            $.each(function () {
                var node = findRow(target, this[opts.idField], t);
                if (node != null && node != undefined) { t.treegrid("remove", this[opts.idField]); }
            });
        }
    };

    var setColumnTitle = function (target, param) {
        if (param && param.field && param.title) {
            var t = $(target), colOpts = t.treegrid("getColumnOption", param.field);
            colOpts.title = param.title;
            t.datagrid("setColumnTitle", param);
        }
    };

    var _select = $.fn.treegrid.methods.select;
    var _unselect = $.fn.treegrid.methods.unselect;
    var selectRow = function (target, param) {
        param = $.isPlainObject(param) ? param : { id: param, cascade: false };
        var id = param.id, cascade = param.cascade ? true : false, t = $(target);
        _select.call(t, t, id);
        if (cascade) {
            var opts = t.treegrid("options");
            $.each(t.treegrid("getChildren", id), function () { _select.call(t, t, this[opts.idField]); });
        }
    };

    var unselectRow = function (target, param) {
        param = $.isPlainObject(param) ? param : { id: param, cascade: false };
        var id = param.id, cascade = param.cascade ? true : false, t = $(target);
        _unselect.call(t, t, id);
        if (cascade) {
            var opts = t.treegrid("options");
            $.each(t.treegrid("getChildren", id), function () { _unselect.call(t, t, this[opts.idField]); });
        }
    };

    var checkRow = function (target, param) {
        param = $.isPlainObject(param) ? param : { id: param, cascade: false };
        var id = param.id, cascade = param.cascade ? true : false, t = $(target);
        t.datagrid("checkRow", id);
        if (cascade) {
            var opts = t.treegrid("options");
            $.each(t.treegrid("getChildren", id), function () { t.datagrid("checkRow", this[opts.idField]); });
        }
    };

    var uncheckRow = function (target, param) {
        param = $.isPlainObject(param) ? param : { id: param, cascade: false };
        var id = param.id, cascade = param.cascade ? true : false, t = $(target);
        t.datagrid("uncheckRow", id);
        if (cascade) {
            var opts = t.treegrid("options");
            $.each(t.treegrid("getChildren", id), function () { t.datagrid("uncheckRow", this[opts.idField]); });
        }
    };


    var setColumnFilter = function (target, columnFilter) {
        var t = $(target), opts = t.treegrid("options"),
            exts = opts._extensionsTreegrid ? opts._extensionsTreegrid : (opts._extensionsTreegrid = {}),
            panel = t.treegrid("getPanel"),
            selector = "div.datagrid-view div.datagrid-header tr.datagrid-header-row div.datagrid-header-filter-container";
        if (!columnFilter) {
            var headerFields = panel.find(selector), length = headerFields.length, i = 0;
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
        if ($.util.isBoolean(param)) { t.treegrid(param ? "showRows" : "hideRows", true); return; }
        if (!param || !param.field) { return; }
        var field = param.field, value = param.value, isArray = $.array.likeArray(value) && !$.util.isString(value),
            finder = isArray ? function (val) { return $.array.contains(value, val[field]); } : function (val) { return value == val[field]; },
            rows = t.treegrid("findRows", finder);
        t.treegrid(param.selected ? "showRows" : "hideRows", rows);
    };

    var setOffset = function (target, offset) {
        var t = $(target), opts = t.treegrid("options"),
            exts = opts._extensionsTreegrid ? opts._extensionsTreegrid : (opts._extensionsTreegrid = {});
        opts.offset = exts.offset = $.fn.datagrid.extensions.parseOffset(offset);
        t.datagrid("setOffset", opts.offset);
    };

    var livesearch = function (target, param) {
        var t = $(target), panel = t.treegrid("getPanel"), opts = t.treegrid("options"), treeField = opts.treeField,
            cells, field, value = param, regular = false, ignoreCase = true, regexp;
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
            var cell = $(this), td = cell.parent(), field = td.attr("field");
            if (field == treeField) { cell = cell.find("span.tree-title"); }
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



    var initRowDndExtensions = $.fn.treegrid.extensions.initRowDndExtensions = function (t, opts) {
        opts = opts || t.treegrid("options");
        if (opts.dndRow) { t.treegrid("enableRowDnd"); }
    };

    /************************  initExtend ColumnFilter Begin  ************************/
    function initHeaderColumnFilterContainer(t, opts, exts) {
        exts = exts || (opts._extensionsTreegrid ? opts._extensionsTreegrid : (opts._extensionsTreegrid = {}));
        $.fn.datagrid.extensions.initColumnExtendProperties(t, exts);
        var data = t.treegrid("getData"), oldData = exts.oldData;
        if (data != oldData) { exts.filterData = []; }
        clearHeaderColumnFilter(t, opts);
        refreshColumnFilterPagerStatus(t, opts);
        if (!opts.columnFilter) { return; }
        exts.oldData = data;
        var header = t.treegrid("getPanel").find("div.datagrid-view div.datagrid-header"),
            headerRows = header.find("table.datagrid-htable tr.datagrid-header-row"),
            headerFields = headerRows.find("td[field]").filter(function () {
                var td = $(this), colspan = td.attr("colspan");
                return (!colspan || colspan == "1") && !td.find("div.datagrid-header-check,div.datagrid-header-rownumber").length ? true : false;
            }),
            columnFilter = opts.columnFilter = $.extend({ panelHeight: 100, position: "top" }, opts.columnFilter),
            position = $.array.contains(["top", "bottom"], columnFilter.position) ? columnFilter.position : "top",
            panelHeight = columnFilter.panelHeight = $.isNumeric(columnFilter.panelHeight) && columnFilter.panelHeight >= 60 ? columnFilter.panelHeight : 60,
            height = header.height(), rows = t.treegrid("getRows");
        headerFields.each(function () {
            var td = $(this).addClass("datagrid-header-filter").removeClass("datagrid-header-filter-top datagrid-header-filter-bottom"),
                cell = td.find("div.datagrid-cell").addClass("datagrid-header-filter-cell"),
                field = td.attr("field"), colOpts = t.treegrid("getColumnOption", field), colWidth = colOpts.width,
                line = $("<hr />").addClass("datagrid-header-filter-line")[position == "top" ? "prependTo" : "appendTo"](this),
                container = $("<div></div>").attr("field", field).addClass("datagrid-header-filter-container").css({
                    height: columnFilter.panelHeight, width: colWidth
                })[position == "top" ? "prependTo" : "appendTo"](this);
            td.addClass(position == "top" ? "datagrid-header-filter-top" : "datagrid-header-filter-bottom");
            if (field) { initColumnFilterField(t, opts, exts, container, colOpts, rows, headerFields); }
        });
        if (exts.filterData && exts.filterData.length) {
            t.treegrid("hideRows", exts.filterData);
        } else {
            refreshColumnFilterStatus(t, opts, exts, rows, headerFields);
        }
    };

    function clearHeaderColumnFilter(t, opts) {
        if (!opts.columnFilter) { return; }
        var headerFields = t.treegrid("getPanel").find("div.datagrid-view div.datagrid-header table.datagrid-htable tr.datagrid-header-row td[field]").filter(function () {
            var td = $(this), colspan = td.attr("colspan");
            return (!colspan || colspan == "1") && !td.find("div.datagrid-header-check,div.datagrid-header-rownumber").length ? true : false;
        });
        headerFields.removeClass("datagrid-header-filter datagrid-header-filter-top datagrid-header-filter-bottom").find("div.datagrid-cell").removeClass("datagrid-header-filter-cell");
        headerFields.find("hr.datagrid-header-filter-line,div.datagrid-header-filter-container").remove();
        var fields = t.treegrid("getColumnFields", "all");
        t.datagrid("fixColumnSize", fields[fields.length - 1]);
    };

    function initColumnFilterField(t, opts, exts, container, colOpts, rows, headerFields) {
        if (!colOpts.filterable) { return; }
        var field = colOpts.field, distinctVals = t.treegrid("getDistinctColumnData", field),
            filter = $.array.contains(["checkbox", "livebox", "caps", "lower", "none"], colOpts.filter) ? colOpts.filter : "checkbox",
            precision = colOpts.precision, step = colOpts.step;
        switch (filter) {
            case "checkbox": initColumnFilterFieldCheckBox(t, opts, exts, container, field, rows, distinctVals); break;
            case "livebox": initColumnFilterFieldLiveBox(t, opts, container, field, rows); break;
            case "caps":
                initColumnFilerFieldSlider(t, opts, container, field, step, precision, rows, distinctVals, "<=", opts.columnFilter.panelHeight, headerFields);
                break;
            case "lower":
                initColumnFilerFieldSlider(t, opts, container, field, step, precision, rows, distinctVals, ">=", opts.columnFilter.panelHeight, headerFields);
                break;
            case "none": break;
        }
    };

    function initColumnFilterFieldCheckBox(t, opts, exts, container, field, rows, distinctVals) {
        $.each(distinctVals, function (index, text) {
            var item = $("<div></div>").addClass("datagrid-header-filter-item").attr("text", text).appendTo(container),
                itemText = $("<div></div>").addClass("datagrid-header-filter-item-text").text(text).appendTo(item),
                icon = $("<div></div>").addClass("datagrid-header-filter-item-icon").appendTo(item),
                handler = function () {
                    var filterRows = $.array.filter(rows, function (value) { return value[field] == text; }),
                        filterData = t.treegrid("getHiddenRows"),
                        hiddenRows = $.array.filter(filterData, function (value) { return value[field] == text; });
                    t.treegrid(hiddenRows.length ? "showRows" : "hideRows", $.array.map(filterRows, function (val) { return val[opts.idField]; }));
                };
            item.click(handler);
        });
    };

    function initColumnFilterFieldLiveBox(t, opts, container, field, rows) {
        $("<div></div>").addClass("datagrid-header-filter-livebox-text").text("模糊过滤：").appendTo(container);
        var input = $("<input />").addClass("datagrid-header-filter-livebox").appendTo(container);
        var btn = $("<a />").linkbutton({ plain: true, iconCls: "icon-search" }).appendTo(container).click(function () {
            t.treegrid("showRows", true);
            var val = input.val();
            if ($.string.isNullOrEmpty(val)) { input.focus(); return; }
            var filterRows = $.array.filter(rows, function (value) { return String(value[field]).indexOf(val) == -1; });
            t.treegrid("hideRows", $.array.map(filterRows, function (val) { return val[opts.idField]; }));
            input.focus();
        });
        $("<a />").linkbutton({ plain: true, iconCls: "icon-undo" }).appendTo(container).click(function () {
            var val = input.val();
            if (val) { input.val("").focus(); btn.click(); } else { input.focus(); }
        });
        input.keypress(function (e) { if (e.which == 13) { btn.click(); } });
    };

    function initColumnFilerFieldSlider(t, opts, container, field, step, precision, rows, distinctVals, type, panelHeight, headerFileds) {
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
                changeSliderValue(t, opts, field, rows, newValue, type, input, slider, headerFileds);
            };
        input.numberbox({ value: type == "<=" ? max : min, min: min, max: max, precision: precision, onChange: handler, height: 18 });
        input.keypress(function (e) { if (e.which == 13) { var val = input.val(); input.numberbox("setValue", $.isNumeric(val) ? val : 0); } });
        slider.slider({
            height: height, mode: "v", showTip: true, value: type == "<=" ? max : min,
            min: min, max: max, rule: [min, "|", max], step: step, onComplete: handler,
            tipFormatter: function (val) { return $.number.round(val || 0, maxPrecision); }
        });
    };

    function changeSliderValue(t, opts, field, rows, value, type, input, slider, headerFileds) {
        var headerFields = headerFileds || t.treegrid("getPanel").find("div.datagrid-view div.datagrid-header table.datagrid-htable tr.datagrid-header-row td[field]").filter(function () {
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
        t.treegrid("showRows", true).treegrid("hideRows", $.array.map(filterRows, function (val) { return val[opts.idField]; }));
        input.numberbox("setValue", value);
        slider.slider("setValue", value);
    };



    function refreshColumnFilterStatus(t, opts, exts, rows, headerFields) {
        refreshColumnFilterPagerStatus(t, opts);
        if (!opts.columnFilter) { return; }
        headerFields = headerFields || t.treegrid("getPanel").find("div.datagrid-view div.datagrid-header table.datagrid-htable tr.datagrid-header-row td[field]").filter(function () {
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
        var pager = t.treegrid("getPager");
        if (pager && pager.length) {
            var len = t.treegrid("getVisibleRows").length, total = t.datagrid("getRows").length,
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
    }

    function refreshColumnFilterCellStatus(t, exts, rows, td, field) {
        var colOpts = t.treegrid("getColumnOption", field), precision = colOpts.precision,
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
                filterData = t.treegrid("getHiddenRows"),
                hiddenLength = $.array.sum(filterData, function (val) { return val[field] == text ? 1 : 0; }),
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


    /************************  initContextMenu Begin  ************************/
    function initHeaderContextMenu(t, opts, exts) {
        var dgOpts = t.datagrid("options");
        exts.onHeaderContextMenuBak = opts.onHeaderContextMenu;
        opts.onHeaderContextMenu = dgOpts.onHeaderContextMenu = function (e, field) {
            if ($.isFunction(exts.onHeaderContextMenuBak)) { exts.onHeaderContextMenuBak.apply(this, arguments); }
            if (!opts.enableHeaderContextMenu) { return; }
            var eventData = $.fn.datagrid.extensions.parseContextMenuEventData(t, opts, e),
                items = parseHeaderContextMenuItems(t, opts, exts, e, field, eventData);
            $.easyui.showMenu({ items: items, left: e.pageX, top: e.pageY, hideDisabledMenu: opts.hideDisabledMenu });
            e.preventDefault();
        };
    };

    function initRowContextMenu(t, opts, exts) {
        exts.onContextMenuBak = opts.onContextMenu;
        opts.onContextMenu = function (e, row) {
            if ($.isFunction(exts.onContextMenuBak)) { exts.onContextMenuBak.apply(this, arguments); }
            if (opts.selectOnRowContextMenu) { t.treegrid("select", row[opts.idField]); }
            if (!opts.enableRowContextMenu) { return; }
            var eventData = $.fn.datagrid.extensions.parseContextMenuEventData(t, opts, e),
                items = parseRowContextMenuItems(t, opts, exts, e, row, eventData);
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
        t.treegrid("getPanel").find(".datagrid-view .datagrid-header table.datagrid-htable tr.datagrid-header-row td[field]").filter(function () {
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


    function parseHeaderContextMenuItems(t, opts, exts, e, field, eventData) {
        var items = [], contextMenu = $.util.likeArray(opts.headerContextMenu) && !$.util.isString(opts.headerContextMenu) ? opts.headerContextMenu : [];
        if (contextMenu.length) { $.array.merge(items, contextMenu); }
        var baseItems = parseHeaderBaseContextMenuItems(t, opts, exts, e, field, eventData);
        if (baseItems.length) { $.array.merge(items, "-", baseItems); }
        items = $.fn.datagrid.extensions.parseHeaderContextMenuMap(e, field, eventData, items, t);
        if (items[0] == "-") { $.array.removeAt(items, 0); }
        return items;
    };

    function parseRowContextMenuItems(t, opts, exts, e, row, eventData) {
        var items = [], contextMenu = $.util.likeArray(opts.rowContextMenu) && !$.util.isString(opts.rowContextMenu) ? opts.rowContextMenu : [];
        if (contextMenu.length) { $.array.merge(items, contextMenu); }
        var baseItems = parseRowBaseContextMenuItems(t, opts, exts, e, row, eventData);
        if (baseItems.length) { $.array.merge(items, "-", baseItems); }
        items = $.fn.treegrid.extensions.parseRowContextMenuMap(e, row, eventData, items, t);
        if (items[0] == "-") { $.array.removeAt(items, 0); }
        return items;
    };


    function parseHeaderBaseContextMenuItems(t, opts, exts, e, field, eventData) {
        var mm = [], exp = opts.exportMenu,
            colOpts = t.treegrid("getColumnOption", field), sortable = colOpts.sortable;
        if (typeof exp == "object") { exp = $.extend({ current: false, all: false, submenu: true }, exp); }
        var m1 = {
            text: "升序", iconCls: "icon-standard-hmenu-asc", disabled: sortable != true,
            handler: function () { return t.treegrid("sort", { sortName: field, sortOrder: "asc" }); }
        };
        var m2 = {
            text: "降序", iconCls: "icon-standard-hmenu-desc", disabled: sortable != true,
            handler: function () { return t.treegrid("sort", { sortName: field, sortOrder: "desc" }); }
        };
        var m3 = {
            text: "显示/隐藏列", iconCls: "icon-standard-application-view-columns", disabled: false, children: [
                {
                    text: "显示全部列", iconCls: function () {
                        var len = exts.fields ? exts.fields.length : 0;
                        var count = $.array.sum(exts.fieldOptions, function (val) { return val.hidden ? 0 : 1; });
                        return count >= len ? "tree-checkbox1" : (count == 0 ? "tree-checkbox0" : "tree-checkbox2");
                    }, hideOnClick: false, handler: function (e, field, eventData, t, item, menu) {
                        $.each(exts.fields, function () { t.treegrid("showColumn", this); });
                        $(this).parent().children("div.menu-item:not(:eq(1))").each(function () {
                            menu.menu("setIcon", { target: this, iconCls: "tree-checkbox1" });
                            menu.menu("enableItem", this);
                        });
                    }
                },
                {
                    text: "还原默认", iconCls: "icon-standard-application-view-tile", hideOnClick: false, handler: function (e, field, eventData, t, item, menu) {
                        $.each(exts.fieldOptionsBackup, function () { t.treegrid(this.hidden == true ? "hideColumn" : "showColumn", this.field); });
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
        var m5 = { text: "导出当前页", iconCls: "icon-standard-page-white-put", disabled: !(exp == true || exp.current == true), handler: function () { return t.treegrid("exportExcel", false); } };
        var m6 = { text: "导出全部", iconCls: "icon-standard-page-white-stack", disabled: !(exp == true || exp.all == true), handler: function () { return t.treegrid("exportExcel", true); } };
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
                if (val.field == opts.treeField) { $.messager.show("树节点列不能被隐藏。"); return; }
                var m = $(this),
                    count = m.parent().find(".menu-item:gt(1) .tree-checkbox1").length;
                if ((count == 1 && !val.hidden) || !val.hidable) { return; }
                t.treegrid(val.hidden ? "showColumn" : "hideColumn", val.field);
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
                disabled: !val.hidable || val.field == opts.treeField ? true : false, handler: handler
            };
        });
    };

    function parseHeaderRowsShowHideMenu(t, opts, exts, e, field, eventData) {
        var rows = t.treegrid("getRows"), distinctVals = t.treegrid("getDistinctColumnData", field),
            mm = [
                {
                    text: "全部", hideOnClick: false,
                    iconCls: (!exts.filterData || !exts.filterData.length) ? "tree-checkbox1" : (exts.filterData.length >= rows.length ? "tree-checkbox0" : "tree-checkbox2"),
                    handler: function (e, field, eventData, t, item, menu) {
                        if (exts.filterData && exts.filterData.length) {
                            t.treegrid("showRows", true);
                        } else {
                            t.treegrid("hideRows", true);
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
                    filterData = t.treegrid("getHiddenRows"),
                    hiddenLength = $.array.sum(filterData, function (value) { return value[field] == val ? 1 : 0; }),
                    iconCls = !hiddenLength ? "tree-checkbox1" : (hiddenLength >= filterLength ? "tree-checkbox0" : "tree-checkbox2");
                var handler = function (e, field, eventData, t, item, menu) {
                    var filterData = t.treegrid("getHiddenRows"),
                        hiddenLength = $.array.sum(filterData, function (value) { return value[field] == val ? 1 : 0; });
                    t.treegrid(hiddenLength ? "showRows" : "hideRows", $.array.map(filterRows, function (val) { return val[opts.idField]; }));
                    menu.menu("setIcon", { target: this, iconCls: hiddenLength ? "tree-checkbox1" : "tree-checkbox0" });
                    $(this).parent().children("div.menu-item:first").each(function () {
                        var filterData = t.treegrid("getHiddenRows");
                        menu.menu("setIcon", {
                            target: this,
                            iconCls: (!exts.filterData.length) ? "tree-checkbox1" : (filterData.length >= rows.length ? "tree-checkbox0" : "tree-checkbox2")
                        });
                    });
                };
                return { text: val, iconCls: iconCls, hideOnClick: false, handler: handler };
            });
        $.array.merge(mm, items);
        if (hasMore) {
            var colOpt = t.treegrid("getColumnOption", field), title = colOpt.title ? colOpt.title : colOpt.field, handler = function () {
                var checkAll = $("<input type=\"button\" value=\"全部选择\" />").click(function () {
                    t.treegrid("showRows", true);
                    $(this).parent().find(":checkbox").each(function () { this.checked = true; });
                });
                var uncheckAll = $("<input type=\"button\" value=\"全部不选\" />").click(function () {
                    t.treegrid("hideRows", true);
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
                                t.treegrid(hiddenLength ? "showRows" : "hideRows", $.array.map(filterRows, function (val) { return val[opts.idField]; }));
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


    function parseRowBaseContextMenuItems(t, opts, exts, e, row, eventData) {
        var mm = [], paging = opts.pagingMenu, toggle = opts.toggleMenu, move = opts.moveMenu, exp = opts.exportMenu, id = row[opts.idField];
        if (typeof paging == "object") { paging = $.extend({ disabled: false, submenu: true }, paging); }
        if (typeof toggle == "object") {
            toggle = $.extend({ expand: true, expandAll: true, collapse: true, collapseAll: true, submenu: false }, toggle);
        }
        if (typeof move == "object") {
            move = $.extend({ up: false, upLevel: false, down: false, downLevel: false, submenu: false }, move);
        }
        if (typeof exp == "object") { exp = $.extend({ current: false, all: false, submenu: true }, exp); }
        var m1 = {
            text: "刷新当前页", iconCls: "pagination-load", disabled: !opts.refreshMenu,
            handler: function () { t.treegrid("reload"); }
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

        var m6 = { text: "展开当前所有", iconCls: "icon-metro-expand", disabled: !(toggle == true || toggle.expandAll == true), handler: function () { t.treegrid("expandAll", id); } };
        var m7 = { text: "展开当前", iconCls: "icon-metro-expand2", disabled: !(toggle == true || toggle.expand == true), handler: function () { t.treegrid("expand", id); } };
        var m8 = { text: "折叠当前", iconCls: "icon-metro-contract2", disabled: !(toggle == true || toggle.collapse == true), handler: function () { t.treegrid("collapse", id); } };
        var m9 = { text: "折叠当前所有", iconCls: "icon-metro-contract", disabled: !(toggle == true || toggle.collapseAll == true), handler: function () { t.treegrid("collapseAll", id); } };
        var m10 = { text: "上移一级", iconCls: "icon-standard-arrow-up", disabled: !(move == true || move.upLevel == true), handler: function () { t.treegrid("shiftRow", { point: "upLevel", id: id }); } };
        var m11 = { text: "上移", iconCls: "icon-standard-up", disabled: !(move == true || move.up == true), handler: function () { t.treegrid("shiftRow", { point: "up", id: id }); } };
        var m12 = { text: "下移", iconCls: "icon-standard-down", disabled: !(move == true || move.down == true), handler: function () { t.treegrid("shiftRow", { point: "down", id: id }); } };
        var m13 = { text: "下移一级", iconCls: "icon-standard-arrow-down", disabled: !(move == true || move.downLevel == true), handler: function () { t.treegrid("shiftRow", { point: "downLevel", id: id }); } };
        var m14 = { text: "导出当前页", iconCls: "icon-standard-page-white-put", disabled: !(exp == true || exp.current == true), handler: function () { return t.treegrid("exportExcel", false); } };
        var m15 = { text: "导出全部", iconCls: "icon-standard-page-white-stack", disabled: !(exp == true || exp.all == true), handler: function () { return t.treegrid("exportExcel", true); } };
       
        
         var m16 = {
            text: "删除", iconCls: "icon-standard-delete", disabled: !opts.refreshMenu,
            handler: function ( id) { alert("删除对象" + id )}
        };
        
        mm.push(m1);
       // mm.push(m16 );
        
        var pagingMenu = [m2, m3, m4, m5], toggleMenu = [m6, m7, m8, m9], moveMenu = [m10, m11, m12, m13], expMenu = [m14, m15];
        if (t.treegrid("isRoot", id)) {
            $.array.insertRange(toggleMenu, 0, [
                { text: "展开所有", iconCls: "icon-standard-arrow-out", handler: function () { t.treegrid("expandAll"); } },
                { text: "折叠所有", iconCls: "icon-standard-arrow-in", handler: function () { t.treegrid("collapseAll"); } }, "-"
            ]);
        }
        if (paging) { $.array.merge(mm, "-", typeof paging == "object" && !paging.submenu ? pagingMenu : { text: "翻页", iconCls: "", disabled: !(paging == true || !paging.disabled), children: pagingMenu }); }
        if (toggle) { $.array.merge(mm, "-", typeof toggle == "object" && !toggle.submenu ? toggleMenu : { text: "展开/折叠", iconCls: "", disabled: !toggle, children: toggleMenu }); }
        if (move) { $.array.merge(mm, "-", typeof move == "object" && !move.submenu ? moveMenu : { text: "上/下移动", iconCls: "", disabled: !move, children: moveMenu }); }
        if (exp) { $.array.merge(mm, "-", typeof exp == "object" && !exp.submenu ? expMenu : { text: "导出数据", iconCls: "icon-standard-page-save", disabled: !exp, children: expMenu }); }
        return mm;
    };


    var parseRowContextMenuMap = $.fn.treegrid.extensions.parseRowContextMenuMap = function (e, row, eventData, contextMenu, t) {
        return $.array.map(contextMenu, function (value, index) {
            if (!value || $.util.isString(value)) { return value; }
            var ret = $.extend({}, value);
            ret.id = $.isFunction(value.id) ? value.id.call(ret, e, row, eventData, t) : value.id;
            ret.text = $.isFunction(value.text) ? value.text.call(ret, e, row, eventData, t) : value.text;
            ret.iconCls = $.isFunction(value.iconCls) ? value.iconCls.call(ret, e, row, eventData, t) : value.iconCls;
            ret.disabled = $.isFunction(value.disabled) ? value.disabled.call(ret, e, row, eventData, t) : value.disabled;
            ret.hideOnClick = $.isFunction(value.hideOnClick) ? value.hideOnClick.call(ret, e, row, eventData, t) : value.hideOnClick;
            ret.onclick = $.isFunction(value.onclick) ? function (e, item, menu) { value.onclick.call(this, e, row, eventData, t, item, menu); } : value.onclick;
            ret.handler = $.isFunction(value.handler) ? function (e, item, menu) { value.handler.call(this, e, row, eventData, t, item, menu); } : value.handler;
            if (ret.children && ret.children.length) { ret.children = parseRowContextMenuMap(e, row, eventData, ret.children, t); }
            return ret;
        });
    };
    /************************  initContextMenu   End  ************************/


    /************************  initDblClickRow Begin  ************************/
    function initDblClickRowEvent(t, opts, exts) {
        exts.onDblClickRowBak = opts.onDblClickRow;
        opts.onDblClickRow = function (row) {
            if ($.isFunction(exts.onDblClickRowBak)) { exts.onDblClickRowBak.apply(this, arguments); }
            //  t.treegrid("select", row[opts.idField]);
            var eventData = $.fn.datagrid.extensions.parseContextMenuEventData(t, opts, null);
            items = parseRowContextMenuItems(t, opts, exts, null, row, eventData);
            if (opts.autoBindDblClickRow && opts.dblClickRowMenuIndex >= 0 && $.util.likeArray(opts.rowContextMenu)
                && !$.util.isString(opts.rowContextMenu) && opts.rowContextMenu.length > opts.dblClickRowMenuIndex) {
                var item = items[opts.dblClickRowMenuIndex], handler = item.handler || item.onclick;
                if (!item.disabled) {
                    return handler(null, row, eventData, t, item, null);
                }
            }
            //if (opts.autoEditing) { t.treegrid("beginEdit", row[opts.idField]); }
        };
    };
    /************************  initDblClickRow   End  ************************/

    function initAutoEditingEvent(t, opts, exts) {
        exts[opts.autoEditingEvent] = opts[opts.autoEditingEvent];
        opts[opts.autoEditingEvent] = function (row) {
            if ($.isFunction(exts[opts.autoEditingEvent])) { exts[opts.autoEditingEvent].apply(this, arguments); }
            if (opts.autoEditing) { t.treegrid("beginEdit", row[opts.idField]); }
        }
    }

    function initFinishEditEvent(t, opts, exts) {
        $(opts.finishEditLocale).click(function (e) {
            if (opts.finishEditOnBlur && $.data(t[0], "treegrid")) {
                var body = t.treegrid("getPanel"), rows = t.treegrid("getEditingNodeIds");
                if (!$.contains(body[0], e.target)) {
                    $.each(rows, function (ii, id) { t.treegrid(opts.finishEditMethod, id); });
                }
            }
        });
    };


    var initTreeGridExtensions = $.fn.treegrid.extensions.initTreeGridExtensions = function (t, opts, exts) {
        opts = opts || t.treegrid("options");
        exts = exts || (opts._extensionsTreegrid ? opts._extensionsTreegrid : (opts._extensionsTreegrid = {}));

        exts.onClickRowBak = opts.onClickRow;
        opts.onClickRow = function (row) {
            if ($.isFunction(exts.onClickRowBak)) { exts.onClickRowBak.apply(this, arguments); }
            if (opts.toggleOnClick) { t.treegrid("toggle", row[opts.idField]); }
        };

        exts.onCheckBak = opts.onCheck;
        opts.onCheck = function (row) {
            if ($.isFunction(exts.onCheckBak)) { exts.onCheckBak.apply(this, arguments); }
            if (opts.cascadeCheck) {
                if (opts.checkOnSelect && opts.singleSelect) { return; }
                var idField = opts.idField, id = row[idField], children, checked, parent = t.treegrid("getParent", id);
                while (parent) {
                    children = t.treegrid("getChildren", parent[idField]);
                    checked = t.treegrid("getChecked");
                    if (!$.array.some(children, function (val) { return !$.array.contains(checked, val); })) {
                        if (!t.treegrid("isChecked", parent[idField])) { t.treegrid("check", parent[idField]); }
                    }
                    parent = t.treegrid("getParent", parent[idField]);
                }
                $.each(t.treegrid("getChildren", id), function (i, n) {
                    if (!t.treegrid("isChecked", n[idField])) { t.treegrid("check", n[idField]); }
                });
            }
        };

        exts.onUncheckBak = opts.onUncheck;
        opts.onUncheck = function (row) {
            if ($.isFunction(exts.onUncheckBak)) { exts.onUncheckBak.apply(this, arguments); }
            if (opts.cascadeCheck) {
                if (opts.checkOnSelect && opts.singleSelect) { return; }
                var idField = opts.idField, id = row[idField], children, checked, parent = t.treegrid("getParent", id);
                while (parent) {
                    children = t.treegrid("getChildren", parent[idField]);
                    checked = t.treegrid("getChecked");
                    if (!$.array.some(children, function (val) { return $.array.contains(checked, val); })) {
                        if (t.treegrid("isChecked", parent[idField])) { t.treegrid("uncheck", parent[idField]); }
                    }
                    parent = t.treegrid("getParent", parent[idField]);
                }
                $.each(t.treegrid("getChildren", id), function (i, n) {
                    t.treegrid("uncheck", n[idField]);
                });
            }
        };
    };


    /************************  initExtend initColumnTooltip Begin  ************************/
    var initColumnTooltip = function (t, opts) {
        t.treegrid("getPanel").find("div.datagrid-view div.datagrid-body table tr.datagrid-row").each(function () {
            var tr = $(this), id = tr.attr("node-id"), row = t.treegrid("find", id);
            initColumnRowTooltip(t, opts, id, row, tr);
        });
    };

    var initColumnRowTooltip = function (t, opts, id, row, tr) {
        tr = tr || t.treegrid("getRowDom", id);
        if (opts.rowTooltip) {
            var onShow = function (e) {
                var tt = $(this), text = $.isFunction(opts.rowTooltip) ? opts.rowTooltip.call(tt, row) : buildText(row);
                tt.tooltip("update", text);
            };
            tr.each(function () { $.easyui.tooltip.init(this, { onShow: onShow }); });
        } else {
            tr.children("td[field]").each(function () {
                var td = $(this), field = td.attr("field"), colOpts = t.treegrid("getColumnOption", field);
                if (!colOpts || !colOpts.tooltip) { return; }
                var cell = td.find("div.datagrid-cell"), onShow = function (e) {
                    var tt = $(this), text = $.isFunction(colOpts.tooltip) ? colOpts.tooltip.call(cell, row[field], id, row) : row[field];
                    tt.tooltip("update", text);
                };
                $.easyui.tooltip.init(cell, { onShow: onShow });
            });
        }
        function buildText(row) {
            var cols = t.treegrid("getColumns", "all"), content = $("<table></table>").css({ padding: "5px" });;
            $.each(cols, function (i, colOpts) {
                if (!colOpts || !colOpts.field || !colOpts.title) { return; }
                var msg = t.treegrid("getCellDisplay", { field: colOpts.field, id: id });
                content.append("<tr><td style='text-align: right; width: 150px;'>" + colOpts.title + ":</td><td style='width: 250px;'>" + msg + "</td></tr>");
            });
            return content;
        };
    };
    /************************  initExtend initColumnTooltip   End  ************************/


    /************************  initExtend initColumnTooltip   End  ************************/
    var initializeRowExtEditor = function (t, opts, id) {
        if (!opts.extEditing) { return; }
        var tr = t.treegrid("getRowDom", id);
        if (!tr.length) { return; }
        var view = t.treegrid("getPanel").find("div.datagrid-view"),
            view1 = view.find("div.datagrid-view1"),
            view2 = view.find("div.datagrid-view2"),
            body = view2.find("div.datagrid-body"),
            width = view1.outerWidth(), pos = view.position(),
            left = diff > 0 ? diff : 0;
        body.css("position", "relative");
        var height = tr.outerHeight(),
            top = tr.position().top + height + body.scrollTop() - view2.find("div.datagrid-header").outerHeight();
        var p = $("<div></div>").addClass("dialog-button datagrid-rowediting-panel").appendTo(body).css({
            "position": "absolute",
            "display": "block",
            "border": "1px solid #ddd",
            "top": top,
            "padding": '5px 5px'
        }).attr("node-id", id);
        $("<a></a>").linkbutton({ plain: false, iconCls: "icon-ok", text: "保存" }).appendTo(p).click(function () {
            t.treegrid("endEdit", id);
            disposeRowExtEditor(t, opts, id);
        });
        $("<a></a>").linkbutton({ plain: false, iconCls: "icon-cancel", text: "取消" }).appendTo(p).click(function () {
            t.treegrid("cancelEdit", id);
            disposeRowExtEditor(t, opts, id);
        });
        var diff = (opts.width - p.outerWidth()) / 2 - width, left = diff > 0 ? diff : 0;
        p.css("left", left);
    };

    var removeRowExtEditor = function (t, body, id) {
        body = body || t.treegrid("getPanel").find("div.datagrid-view div.datagrid-view2 div.datagrid-body");
        body.find("div.datagrid-rowediting-panel[node-id=" + id + "]").remove();
    };

    var disposeRowExtEditor = function (t, opts, id) {
        if (!opts.extEditing) { return; }
        body = t.treegrid("getPanel").find("div.datagrid-view div.datagrid-view2 div.datagrid-body");
        removeRowExtEditor(t, body, id);
    };

    var initSingleEditing = function (t, opts, id) {
        var exts = opts._extensionsTreegrid ? opts._extensionsTreegrid : (opts._extensionsTreegrid = {});
        if (opts.singleEditing) { t.treegrid("endEdit", exts.lastEditingId); }
        exts.lastEditingId = id;
    };
    /************************  initExtend ExtEditor Begin  ************************/



    /******************** initExtensions Begin ********************/
    var initExtensions = $.fn.treegrid.extensions.initExtensions = function (t, opts) {
        var exts = opts._extensionsTreegrid ? opts._extensionsTreegrid : (opts._extensionsTreegrid = {});
        if (exts._initialized) { return; }

        var fields = t.treegrid("getColumnFields", false);
        exts.fields = $.array.filter(fields, function (val) { return t.treegrid("getColumnOption", val).title ? true : false; });
        exts.fieldOptions = $.array.map(exts.fields, function (val) { return t.treegrid("getColumnOption", val); });
        exts.fieldOptionsBackup = $.array.map(exts.fieldOptions, function (val) { return $.extend({}, val); });
        exts.filterData = [];

        initColumnExtensions();
        initOffset();
        initContextMenu();
        initDblClickRow();
        initAutoEditing();
        initFinishEdit();
        initTreeExtensions();
        function initColumnExtensions() { $.fn.datagrid.extensions.initColumnExtendProperties(t, exts); };
        function initOffset() { t.treegrid("setOffset", opts.offset); };
        function initContextMenu() { initHeaderContextMenu(t, opts, exts); initRowContextMenu(t, opts, exts); initHeaderClickMenu(t, opts, exts); };
        function initDblClickRow() { initDblClickRowEvent(t, opts, exts); };
        function initAutoEditing() { initAutoEditingEvent(t, opts, exts); };
        function initFinishEdit() { initFinishEditEvent(t, opts, exts); };
        function initTreeExtensions() { initTreeGridExtensions(t, opts, exts); };

        var rows = t.datagrid("getRows");
        if (!rows || !rows.length) { initHeaderColumnFilterContainer(t, opts, exts); }

        exts._initialized = true;
    };

    var loader = $.fn.treegrid.extensions.loader = function (param, success, error) {
        var t = $(this), opts = t.treegrid("options");
        initExtensions(t, opts);
        if (!opts.url) { return false; }
        param = $.fn.datagrid.extensions.parsePagingQueryParams(opts, param);
        $.ajax({
            type: opts.method, url: opts.url, data: param, dataType: "json",
            success: function (data) { success(data); },
            error: function () { error.apply(this, arguments); }
        });
    };

    var _onLoadSuccess = $.fn.treegrid.defaults.onLoadSuccess;
    var onLoadSuccess = $.fn.treegrid.extensions.onLoadSuccess = function (data) {
        if ($.isFunction(_onLoadSuccess)) { _onLoadSuccess.apply(this, arguments); }
        var t = $(this), opts = t.treegrid("options"),
            exts = opts._extensionsTreegrid ? opts._extensionsTreegrid : (opts._extensionsTreegrid = {});
        initHeaderColumnFilterContainer(t, opts, exts);
        initRowDndExtensions(t, opts);
        initColumnTooltip(t, opts);
    };

    var _onResizeColumn = $.fn.treegrid.defaults.onResizeColumn;
    var onResizeColumn = $.fn.treegrid.extensions.onResizeColumn = function (field, width) {
        if ($.isFunction(_onResizeColumn)) { _onResizeColumn.apply(this, arguments); }
        var t = $(this), opts = t.treegrid("options");
        if (opts.columnFilter) {
            var panel = t.treegrid("getPanel"), colOpts = t.treegrid("getColumnOption", field),
                container = panel.find("div.datagrid-header-filter-container[field=" + field + "]");
            container.width(colOpts.width);
        }
    };

    var _onBeforeEdit = $.fn.treegrid.defaults.onBeforeEdit;
    var onBeforeEdit = $.fn.treegrid.extensions.onBeforeEdit = function (row) {
        if ($.isFunction(_onBeforeEdit)) { _onBeforeEdit.apply(this, arguments); }
        var t = $(this), opts = t.datagrid("options");
        initializeRowExtEditor(t, opts, row[opts.idField]);
        initSingleEditing(t, opts, row[opts.idField]);
        t.treegrid("getPanel").find("div.datagrid-view div.datagrid-body table.datagrid-btable tr.datagrid-row").draggable("disable");
    }

    var _onAfterEdit = $.fn.treegrid.defaults.onAfterEdit;
    var onAfterEdit = $.fn.treegrid.extensions.onAfterEdit = function (row, changes) {
        if ($.isFunction(_onAfterEdit)) { _onAfterEdit.apply(this, arguments); }
        var t = $(this), opts = t.treegrid("options"),
            exts = opts._extensionsTreegrid ? opts._extensionsTreegrid : (opts._extensionsTreegrid = {});
        disposeRowExtEditor(t, opts, row[opts.idField]);
        initHeaderColumnFilterContainer(t, opts, exts);
        initRowDndExtensions(t, opts);
        initColumnRowTooltip(t, opts, row[opts.idField], row);
    }

    var _onCancelEdit = $.fn.treegrid.defaults.onCancelEdit;
    var onCancelEdit = $.fn.treegrid.extensions.onCancelEdit = function (row) {
        if ($.isFunction(_onCancelEdit)) { _onCancelEdit.apply(this, arguments); }
        var t = $(this), opts = t.treegrid("options");
        disposeRowExtEditor(t, opts, row[opts.idField]);
        initRowDndExtensions(t, opts);
        initColumnRowTooltip(t, opts, row[opts.idField], row);
    };
    /******************** initExtensions   End ********************/



    var _loadFilter = $.fn.treegrid.defaults.loadFilter;
    var loadFilter = $.fn.treegrid.extensions.loadFilter = function (data, parent) {
        if ($.isFunction(_loadFilter)) { data = _loadFilter.apply(this, arguments); }
        var isArray = $.array.likeArray(data) && !$.util.isString(data), rows = isArray ? data : data.rows;
        if (!rows.length) { return data; }
        var t = $(this), opts = t.treegrid("options");
        rows = opts.dataPlain ? $.fn.tree.extensions.dataPlainConverter(rows, opts) : rows;
        if (parent != null && parent != undefined) { return isArray ? rows : { total: rows.length, rows: rows }; }
        return isArray ? rows : { total: data.length || rows.length, rows: rows };
    };

    var _onExpand = $.fn.treegrid.defaults.onExpand;
    var onExpand = $.fn.treegrid.extensions.onExpand = function (row) {
        if ($.isFunction(_onExpand)) { _onExpand.apply(this, arguments); }
        var t = $(this), opts = t.treegrid("options");
        if (opts.onlyNodeExpand) {
            var rows = t.treegrid("getNears", row[opts.idField]), animate = opts.animate
            opts.animate = false;
            $.each($.array.filter(rows, function (val) { return val[opts.idField] != row[opts.idField] && val.state == "open"; }), function () {
                t.treegrid("collapse", this[opts.idField]);
            });
            opts.animate = animate;
        }
    };


    var methods = $.fn.treegrid.extensions.methods = {

        //  覆盖 easyui-treegrid 的原生方法，以支持相应属性、事件和扩展功能；
        update: function (jq, param) { return jq.each(function () { updateRow(this, param); }); },

        //  覆盖 easyui-treegrid 的原生方法，以支持相应属性、事件和扩展功能；
        append: function (jq, param) { return jq.each(function () { appendRow(this, param); }); },

        //  覆盖 easyui-treegrid 的原生方法，以支持相应属性、事件和扩展功能；
        insert: function (jq, param) { return jq.each(function () { insertRow(this, param); }); },

        //  扩展 easyui-treegrid 的自定义方法；判断指定的 tree-node 是否被 check；该方法的参数 id 表示要判断的节点的 idField 值；
        //  返回值：如果参数 id 所表示的 tree-node 被 check，则返回 true，否则返回 false。
        isChecked: function (jq, id) { return isChecked(jq[0], id); },

        //  扩展 easyui-treegrid 的自定义方法；判断指定的 tree-node 是否被 select；该方法的参数 id 表示要判断的节点的 idField 值；
        //  返回值：如果参数 id 所表示的 tree-node 被 select，则返回 true，否则返回 false。
        isSelected: function (jq, id) { return isSelected(jq[0], id); },

        //  扩展 easyui-treegrid 的自定义方法；判断指定的 tree-node 是否开启行编辑状态；该方法的参数 id 表示要判断的节点的 idField 值；
        //  返回值：如果参数 id 所表示的 tree-node 正开启行编辑状态，则返回 true，否则返回 false。
        isEditing: function (jq, id) { return isEditing(jq[0], id); },

        //  扩展 easyui-treegrid 的自定义方法；获取当前表格中第一个开启了编辑状态的 tree-node 的节点 id 值(idField 属性所示值)；
        //  返回值：如果当前表格中存在开启了行编辑状态的行，则返回第一个编辑行 tree-node 的节点 id；否则返回 undefined。
        getEditingNodeId: function (jq) { return getEditingNodeId(jq[0]); },

        //  扩展 easyui-treegrid 的自定义方法；获取当前表格中所有开启了行编辑状态的 tree-node 的节点 id 值(idField 属性所示值)；
        //  返回值：返回一个数组，数组中包含当前表格中所有已经开启了行编辑状态的 tree-node 的节点 id；
        getEditingNodeIds: function (jq) { return getEditingNodeIds(jq[0]); },

        //  扩展 easyui-treegrid 的自定义方法；判断指定的 tree-node 是否为根节点；该方法定义如下参数：
        //      id: 用于判断的 tree-node 对象的 idField 值。
        //  返回值：如果指定的 jQuery 对象是该 easyui-treegrid 的根节点，则返回 true，否则返回 false。
        isRoot: function (jq, id) { return isRootNode(jq[0], id); },

        //  重写 easyui-treegrid 的方法 getLevel；修复该方法的部分 BUG；用于获取指定节点的级别；该方法的参数 target 表示要获取级别的节点的 idField 值；
        //  返回值：如果 id 表示的节点存在于此 easyui-treegrid，则返回表示其所在节点级别的数字(从 1 开始计数)，否则返回 0。
        getLevel: function (jq, id) { return getLevel(jq[0], id); },

        //  扩展 easyui-treegrid 的自定义方法；冻结指定的列；该方法的参数 field 表示要冻结的列的 field 值。
        //  返回值：返回表示当前 easyui-treegrid 的 jQuery 链式对象。
        //  注意：此方法在多行表头情况下无效。
        //      当前表格在执行此方法前必须存在至少一个冻结列，否则此方法无效；
        freezeColumn: function (jq, field) { return jq.each(function () { freezeColumn(this, field); }); },

        //  扩展 easyui-treegrid 的自定义方法；取消冻结指定的列；该方法的参数 field 表示要取消冻结的列的 field 值。
        //  返回值：返回表示当前 easyui-treegrid 的 jQuery 链式对象。
        //  注意：此方法在多行表头情况下无效。
        //      当前表格在执行此方法前必须存在至少一个非冻结列，否则此方法无效；
        unfreezeColumn: function (jq, field) { return jq.each(function () { unfreezeColumn(this, field); }); },

        //  扩展 easyui-treegrid 的自定义方法；移动指定的列到另一位置；该方法的参数 param 为一个 JSON-Object，定义包含如下属性：
        //      target: 表示目标位置列的 field 值；
        //      source: 表示要移动的列的 field 值；
        //      point:  表示移动到目标列的位置，String 类型，可选的值包括：
        //          before: 表示将 source 列移动至 target 列的左侧；
        //          after:  表示将 source 列移动值 target 列的右侧；
        //  返回值：返回表示当前 easyui-treegrid 的 jQuery 链式对象。
        //  注意：此方法在多行表头情况下无效。
        moveColumn: function (jq, param) { return jq.each(function () { moveColumn(this, param); }); },

        //  扩展 easyui-treegrid 的自定义方法；移动指定的列挪动一格位置；该方法的参数 param 为一个 JSON-Object，定义包含如下属性：
        //      field:  表示要挪动的列的 field 值；
        //      porint: 表示挪动 field 列的方式，String 类型，可选的值包括：
        //          before: 表示将该列向左挪动一格；
        //          after:  表示将该列向右挪动一格；
        //  返回值：返回表示当前 easyui-treegrid 的 jQuery 链式对象。
        //  注意：此方法在多行表头情况下无效。
        shiftColumn: function (jq, param) { return jq.each(function () { shiftColumn(this, param); }); },

        //  扩展 easyui-treegrid 的自定义方法；删除指定的列；该方法的参数 field 表示要删除的列的 field 值；
        //  返回值：返回表示当前 easyui-treegrid 的 jQuery 链式对象。
        deleteColumn: function (jq, field) { return jq.each(function () { deleteColumn(this, field); }); },

        //  扩展 easyui-treegrid 的自定义方法；删除指定的列并返回该列的 ColumnOption 值；该方法的参数 field 表示要删除的列的 field 值；
        //  返回值：返回参数 field 值所表示的列的 ColumnOption 值。如果当前 easyui-treegrid 不存在该列，则返回 null。
        popColumn: function (jq, field) { return popColumn(jq[0], param); },


        //  扩展 easyui-treegrid 的自定义方法；移动指定的节点到另一个位置；该方法定义如下参数：
        //      param:   这是一个 JSON-Object，该对象定义如下属性：
        //          target: 表示目标位置的 tree-node 对象的 idField 值；
        //          source: 表示要移动的 tree-node 对象的 idField 值；
        //          point:  表示移动到目标节点 target 的位置，String 类型，可选的值包括：
        //              "append":   表示追加为目标节点 target 的子节点，默认值；
        //              "top":      表示移动到目标节点 target 的上一格位置；
        //              "bottom":   表示追加为目标节点 target 的下一格位置；
        //  返回值：返回表示当前 easyui-treegrid 组件的 jQuery 对象。
        moveRow: function (jq, param) { return jq.each(function () { moveRow(this, param); }); },

        //  扩展 easyui-treegrid 的自定义方法；移动指定节点的位置；该方法的参数 param 为一个 JSON-Object 类型对象，包含如下属性：
        //      id: 表示要移动的 tree-node 对象的 idField 值；
        //      point:  表示移动 target 的方式，String 类型，可选的值报错：
        //      "up":       表示将 target 所表示的 tree-node 移动到上一格位置；
        //      "upLevel":  表示将 target 所表示的 tree-node 移动到上一级的末尾；
        //      "down":     表示将 target 所表示的 tree-node 移动到下一格位置；
        //      "downLevel":表示将 target 所表示的 tree-node 移动到下一级的末尾；
        //      如果不定义该值或者该值为空或该值不是上面四个之一，则不进行任何操作；
        //  返回值：返回表示当前 easyui-tree 组件的 jQuery 对象。
        shiftRow: function (jq, param) { return jq.each(function () { shiftRow(this, param); }); },

        //  扩展 easyui-treegrid 的自定义方法；判断两个 tree-node 之间的关系；该方法定义如下参数：
        //      param：  这是一个 JSON-Object，该对象定义如下属性：
        //          id1:    用于判断的第一个 tree-node 对象的 idField 值；
        //          id2:    用于判断的第二个 tree-node 对象的 idField 值；
        //  返回值：返回一个 String 类型的值：
        //      如果 id1 是 id2 的子节点，则返回 "child"；
        //      如果 id1 是 id2 的父节点，则返回 "parent"；
        //      如果 id1 和 id2 是具有同一个父级节点的平级节点，则返回 "sibling"；
        //      如果 id1 和 id2 既不是父子级关系，也不是具有同一个父级节点的平级节点关系，则返回 "normal"；
        compare: function (jq, param) { return compareNode(jq[0], param); },

        //  扩展 easyui-treegrid 的自定义方法；判断一个节点是否为另一个节点的子节点；该方法定义如下参数：
        //      param：  这是一个 JSON-Object，该对象定义如下属性：
        //          id1:    用于判断的第一个 tree-node 对象的 idField 值；
        //          id2:    用于判断的第二个 tree-node 对象的 idField 值；
        //  返回值：如果 tree-node id1 是 tree-node id2 的子节点，则返回 true，否则返回 false。
        isChild: function (jq, param) { return isChild(jq[0], param); },

        //  扩展 easyui-treegrid 的自定义方法；判断一个节点是否为另一个节点的父节点；该方法定义如下参数：
        //      param：  这是一个 JSON-Object，该对象定义如下属性：
        //          id1:    用于判断的第一个 tree-node 对象的 idField 值；
        //          id2:    用于判断的第二个 tree-node 对象的 idField 值；
        //  返回值：如果 tree-node id1 是 tree-node id2 的父节点，则返回 true，否则返回 false。
        isParent: function (jq, param) { return isParent(jq[0], param); },

        //  扩展 easyui-treegrid 的自定义方法；判断一个节点是否和另一个节点为具有同一个父节点的平级节点；该方法定义如下参数：
        //      param：  这是一个 JSON-Object，该对象定义如下属性：
        //          id1:    用于判断的第一个 tree-node 对象的 idField 值；
        //          id2:    用于判断的第二个 tree-node 对象的 idField 值；
        //  返回值：如果 tree-node id1 和 tree-node id2 是具有同一个父级节点的平级节点，则返回 true，否则返回 false。
        isSibling: function (jq, param) { return isSibling(jq[0], param); },

        //  扩展 easyui-treegrid 的自定义方法；获取指定节点的平级下一格位置的 tree-node 节点；该方法定义如下参数：
        //      id:  指定的表示 tree-node 对象的 idField 值；
        //  返回值：返回 tree-node id 的同级别下一格位置的 tree-node 节点 node 对象；
        //      如果该 tree-node id 为当前级别的最后一个节点即没有下一格节点；则返回 null。
        nextRow: function (jq, id) { return getNextRow(jq[0], id); },

        //  扩展 easyui-treegrid 的自定义方法；获取指定节点的平级上一格位置的 tree-node 节点；该方法定义如下参数：
        //      id:  指定的表示 tree-node 对象的 idField 值；
        //  返回值：返回 tree-node id 的同级别上一格位置的 tree-node 节点对象；该 tree-node 对象含有如下属性：
        //      如果该 tree-node id 为当前级别的第一个节点即没有上一格节点；则返回 null。
        prevRow: function (jq, id) { return getPrevRow(jq[0], id); },

        //  扩展 easyui-treegrid 的自定义方法；获取指定节点的同级所有节点(包含自身)；该方法定义如下参数：
        //      id:  指定的表示 tree-node 对象的 idField 值；
        //  返回值：返回 tree-node id 的同级别(具有和当前 tree-node id 同一个父级节点)所有节点构成的一个数组对象；
        //      如果传入的参数 id 是某个根节点的 id 或者未定义 id 参数，则该方法和 getRoots 方法返回的值相同；
        getNears: function (jq, id) { return getNears(jq[0], id); },

        //  扩展 easyui-treegrid 的自定义方法；获取指定节点的下一级所有节点；该方法定义如下参数：
        //      id:  指定的表示 tree-node 对象的 idField 值；
        //  返回值：返回 tree-node id 的下一级所有节点构成的一个数组对象；
        //      如果传入的参数 id 没有子节点，则返回一个包含 0 个元素的数组。
        //  备注：该方法和 getChildren 的不同之处在于，getChildren 方法返回的是 tree-node id 下的所有子节点内容；
        getNearChildren: function (jq, id) { return getNearChildren(jq[0], id); },


        //  扩展 easyui-treegrid 的自定义方法；启用当前表格的行拖动功能；该方法无参数；
        //  返回值：返回表示当前 easyui-treegrid 的 jQuery 链式对象。
        enableRowDnd: function (jq) { return jq.each(function () { enableRowDnd(this); }); },

        //  扩展 easyui-treegrid 的自定义方法；禁用当前表格的行拖动功能；该方法无参数；
        //  返回值：返回表示当前 easyui-treegrid 的 jQuery 链式对象。
        disableRowDnd: function (jq) { return jq.each(function () { disableRowDnd(this); }); },




        //  获取 easyui-treegrid 中当前页的行数据(包括根节点和子节点)所构成的一个集合；该方法的参数 cascade 定义为如下类型：
        //      Boolean 类型，默认为 false，表示是否连同子级节点数据一并返回；
        //  返回值：返回一个 Array 数组对象，数组中的每一个元素都表示一个 node；
        //      如果 cascade 为 true，则返回所有根节点以及子节点合并所构成的一个数组；
        //      如果 cascade 为 false，则仅返回所有根节点数据，同 getRoots 方法；
        //      如果 easyui-treegrid 的当前页没有数据，则返回一个长度为 0 的数组。
        getRows: function (jq, cascade) { return getRows(jq[0], cascade); },

        //  获取 easyui-treegrid 中当前页指定列所有行的单元格数据所构成的一个数组；该方法的参数 param 可以定义为如下两种类型：
        //      1、表示要获取的列的 field 值；
        //      2、JSON-Object 类型，包含如下属性：
        //          field: 要获取的数据的列的 field 名；
        //          cascade：Boolean 类型，默认为 false； 表示返回的结果集中是否还包括子节点数据；
        //  返回值：返回一个数组，数组中每一个元素都是其数据行的该列的值，数组的长度等于 grid.treegrid("getRows", cascade) 的长度；
        //          如果传入的列名不存在，则返回数组的长度同样等于 grid.treegrid("getRows") 的长度，只是每个元素的值都为 undefined.
        getColumnData: function (jq, param) { return getColumnData(jq[0], param); },

        //  获取 easyui-treegrid 中当前页指定行节点的 DOM-jQuery 对象元素集合；该方法的参数 param 可以定义为以下两种类型：
        //      1、表示要获取的行节点的 id 值；
        //      2、JSON-Object 对象，该对象需定义如下属性：
        //          id:     表示要获取的行节点的 id 值；
        //          cascade:Boolean 类型值，默认为 false，表示是否连同其子级节点的 DOM 行对象一并获取并返回。
        //  返回值：如果当前页存在 id 指示的行，则返回该行的 DOM-jQuery 对象集合，该集合中包含的 DOM 节点级别为一组 tr class="datagrid-row" 对象；
        //          否则返回一个空的 jQuery 对象。
        getRowDom: function (jq, param) { return getRowDom(jq[0], param); },

        //  获取当前 easyui-treegrid 中当前页指定 id 的节点数据对象；同 find 方法。
        getRowData: function (jq, id) { return getNode(jq[0], id); },

        //  获取当前 easyui-treegrid 中当前页指定 id 的节点数据对象；同 find 方法。
        getNode: function (jq, id) { return getNode(jq[0], id); },

        //  获取当前 easyui-treegrid 中当前页的多个节点所构成的一个数据集合；同 findRows 方法。
        getNodes: function (jq, param) { return findRows(jq[0], param); },

        //  获取 easyui-treegrid 中当前页指定单元格的 Dom-jQuery 对象元素；该方法的参数 pos 为一个 JSON-Object 对象，包含如下属性：
        //      field:  表示要获取的单元格位于哪个列；
        //      id:     表示要获取的单元格位于哪个行的节点 id；
        //  返回值：如果当前页存在指定列的指定行，则返回该列中指定行的 DOM-jQuery 对象，该对象中包含的 DOM 节点级别为一个 div class="datagrid-cell" 对象；
        //          否则返回一个空的 jQuery 对象。
        getCellDom: function (jq, pos) { return getCellDom(jq[0], pos); },

        //  获取 easyui-treegrid 中当前页指定单元格的数据；该方法的参数 pos 为一个 JSON-Object 对象，包含如下属性：
        //      field:  表示要获取的单元格位于哪个列；
        //      id:     表示要获取的单元格位于哪个行的节点 id；
        //  返回值：如果当前页存在指定列的指定行，则返回该列中指定行及指定列的单元格数据；否则返回 undefined。
        getCellData: function (jq, pos) { return getCellData(jq[0], pos); },

        //  获取 easyui-treegrid 中当前页指定单元格的显示数据(经过 formatter 格式化后的显示数据)；
        //  该方法的参数 pos 为一个 JSON-Object 对象，包含如下属性：
        //      field:  表示要获取的单元格位于哪个列；
        //      id:     表示要获取的单元格位于哪个行的节点 id；
        //  返回值：如果当前页存在指定列的指定行，则返回该列中指定行的单元格的显示数据(经过 formatter 格式化后的显示数据)；否则返回 undefined。
        getCellDisplay: function (jq, pos) { return getCellDisplay(jq[0], pos); },

        //  获取 easyui-treegrid 按指定列的去重复项后的行数据集合；该方法的参数 param 可以定义为如下两种类型：
        //      1、表示要获取的列的 field 值；
        //      2、JSON-Object 类型，包含如下属性：
        //          field: 要获取的数据的列的 field 名；
        //          cascade：Boolean 类型，默认为 false； 表示返回的结果集中是否还包括子节点数据；
        //  返回值：返回一个数组，数组中每一个元素都表示一个行数据；
        //      其结果相当于当前 easyui-treegrid 控件调用 getRows 返回后并经过对指定列去重复项后的结果；
        //      如果传入的列名不存在，则返回一个长度为 0 的数组对象.
        getDistinctRows: function (jq, param) { return getDistinctRows(jq[0], param); },

        //  获取 easyui-treegrid 指定列的值去重复项后的数据集合；该方法的参数 param 可以定义为如下两种类型：
        //      1、表示要获取的列的 field 值；
        //      2、JSON-Object 类型，包含如下属性：
        //          field: 要获取的数据的列的 field 名；
        //          cascade：Boolean 类型，默认为 false； 表示返回的结果集中是否还包括子节点数据；
        //  返回值：返回一个数组，数组中的每一个元素都表示某一行的相应 field 属性的值；
        //      其结果相当于当前 easyui-treegrid 控件调用 getColumnData 返回后并经过对指定列去重复项后的结果；
        //      如果传入的列名不存在，则返回一个长度为 0 的数组对象.
        getDistinctColumnData: function (jq, param) { return getDistinctColumnData(jq[0], param); },

        //  显示当前 easyui-treegrid 当前页数据中指定行的数据；该方法的参数 param 可以是以下两种类型：
        //      待查找的行数据的 idField(主键) 字段值；
        //      function 类型，该回调函数签名为 function(row, index, rows)，其中 row 表示行数据对象、index 表示行索引号、rows 表示当前 easyui-treegrid 所有节点对象集合；
        //          如果 param 参数为 function 类型，则 findRow 方法会对当前 easyui-treegrid 的当前页的每一行数据调用该回调函数；
        //          当回调函数返回 true 时，则表示找到需要查找的结果，立即停止循环调用并显示该行数据；
        //          如果回调函数始终未返回 true，则该回调函数会一直遍历 rows 直到最后。
        //  返回值：返回表示当前 easyui-treegrid 组件的 jQuery 链式对象。
        showRow: function (jq, param) { return jq.each(function () { showRow(this, param); }); },

        //  隐藏当前 easyui-treegrid 当前页数据中指定行的数据；该方法的参数 param 可以是以下两种类型：
        //      待查找的行数据的 idField(主键) 字段值；
        //      function 类型，该回调函数签名为 function(row, index, rows)，其中 row 表示行数据对象、index 表示行索引号、rows 表示当前 easyui-treegrid 所有节点对象集合；
        //          如果 param 参数为 function 类型，则 findRow 方法会对当前 easyui-treegrid 的当前页的每一行数据调用该回调函数；
        //          当回调函数返回 true 时，则表示找到需要查找的结果，立即停止循环调用并隐藏该行数据；
        //          如果回调函数始终未返回 true，则该回调函数会一直遍历 rows 直到最后。
        //  返回值：返回表示当前 easyui-treegrid 组件的 jQuery 链式对象。
        hideRow: function (jq, param) { return jq.each(function () { hideRow(this, param); }); },

        //  显示当前 easyui-treegrid 当前页数据中指定多行的数据；该方法的参数 param 可以是以下三种类型：
        //      Function 类型，该回调函数签名为 function(row, index, rows)，其中 row 表示行数据对象、index 表示行索引号、rows 表示当前 easyui-treegrid 所有节点对象集合；
        //          如果 param 参数为 Function 类型，则 showRows 方法会对当前 easyui-treegrid 的当前页的每一行数据调用该回调函数；
        //          当回调函数返回 true 时，则该行数据将会被显示；
        //      Array 类型，数组中的每一项都可以定义为如下类型：
        //          待查找的行数据的 idField(主键) 字段值；
        //          Function 类型；具体回调函数签名参考 showRow 方法中 param 参数为 function 类型时的定义；
        //          当 param 参数定义为 Array 类型时，则 showRows 方法会对数组中的每一项循环调用 showRow 方法；
        //      Boolean 类型且为 true：则 showRows 将会显示 easyui-treegrid 当前页的所有数据。
        //  返回值：返回表示当前 easyui-treegrid 组件的 jQuery 链式对象。
        showRows: function (jq, param) { return jq.each(function () { showRows(this, param); }); },

        //  隐藏当前 easyui-treegrid 当前页数据中指定多行的数据；该方法的参数 param 可以是以下三种类型：
        //      Function 类型，该回调函数签名为 function(row, index, rows)，其中 row 表示行数据对象、index 表示行索引号、rows 表示当前 easyui-treegrid 所有节点对象集合；
        //          如果 param 参数为 Function 类型，则 hideRows 方法会对当前 easyui-treegrid 的当前页的每一行数据调用该回调函数；
        //          当回调函数返回 true 时，则该行数据将会被隐藏；
        //      Array 类型，数组中的每一项都可以定义为如下类型：
        //          待查找的行数据的 idField(主键) 字段值；
        //          Function 类型；具体回调函数签名参考 hideRow 方法中 param 参数为 function 类型时的定义；
        //          当 param 参数定义为 Array 类型时，则 hideRows 方法会对数组中的每一项循环调用 hideRow 方法；
        //      Boolean 类型且为 true：则 hideRows 将会隐藏 easyui-treegrid 当前页的所有数据。
        //  返回值：返回表示当前 easyui-treegrid 组件的 jQuery 链式对象。
        hideRows: function (jq, param) { return jq.each(function () { hideRows(this, param); }); },

        //  获取当前 easyui-treegrid 当前页所有隐藏的行数据所构成的一个 Array 对象；该方法的参数 cascade 定义为如下类型：
        //      Boolean 类型，默认为 false，表示是否连同子级节点数据一并返回；
        //  返回值：返回一个 Array 数组对象，数组中的每一个元素都表示一个 node；
        //      如果 cascade 为 true，则返回所有根节点以及子节点合并所构成的一个数组；
        //      如果 cascade 为 false，则仅返回所有根节点数据，同 getRoots 方法；
        //      如果 easyui-treegrid 的当前页没有数据，则返回一个长度为 0 的数组。
        getHiddenRows: function (jq, cascade) { return getHiddenRows(jq[0], cascade); },

        //  获取当前 easyui-treegrid 当前页所有显示的行数据所构成的一个 Array 对象；该方法的参数 cascade 定义为如下类型：
        //      Boolean 类型，默认为 false，表示是否连同子级节点数据一并返回；
        //  返回值：返回一个 Array 数组对象，数组中的每一个元素都表示一个 node；
        //      如果 cascade 为 true，则返回所有根节点以及子节点合并所构成的一个数组；
        //      如果 cascade 为 false，则仅返回所有根节点数据，同 getRoots 方法；
        //      如果 easyui-treegrid 的当前页没有数据，则返回一个长度为 0 的数组。
        getVisibleRows: function (jq, cascade) { return getVisibleRows(jq[0], cascade); },

        //  对当前 easyui-treegrid 中进行高亮关键词查询；该方法的 param 可以定义为如下两种类型：
        //      1、String 类型值：表示要对所有列进行的高亮查询关键词；
        //      2、JSON-Object：表示对特定列进行高亮查询的参数，该对象类型参数包含如下属性：
        //          field:      表示要进行高亮查询的列；
        //          value:      表示要进行高亮查询的关键词；
        //          regular:    Boolean 类型值，默认为 false；指示该关键词是否为正则表达式；
        //          ignoreCase: Boolean 类型值，默认为 true；指示高亮查询时是否忽略大小写。
        //  返回值：返回表示当前 easyui-treegrid 组件的 jQuery 链式对象。
        livesearch: function (jq, param) { return jq.each(function () { livesearch(this, param); }); },

        //  重写 easyui-treegrid 的原生方法 find，使之功能更加丰富。
        //  获取当前 easyui-treegrid 当前页指定 id 的节点对象并返回；该方法的参数 param 可以定义为如下两种类型：
        //      待查找的行数据的 idField(主键) 字段值；
        //      function 类型，该回调函数签名为 function(row, index, rows)，其中 row 表示行数据对象、index 表示行索引号、rows 表示当前 easyui-treegrid 所有节点对象集合；
        //          如果 param 参数为 function 类型，则 findRow 方法会对当前 easyui-treegrid 的当前页的每一行数据调用该回调函数；
        //          当回调函数返回 true 时，则表示找到需要查找的结果，立即停止循环调用并返回该行数据；
        //          如果回调函数始终未返回 true，则该回调函数会一直遍历 rows 直到最后并返回 null。
        //  返回值：返回一个 JSON-Object，表示一个行节点数据对象；如果未找到相应数据，则返回 null。
        find: function (jq, param) { return findRow(jq[0], param); },

        //  获取当前 easyui-treegrid 中当前页指定 id 的节点数据对象；同 find 方法。
        findRow: function (jq, param) { return findRow(jq[0], param); },

        //  获取当前 easyui-treegrid 当前页上的指定行数据集合并返回；该方法的参数 param 可以定义为如下两种类型：
        //      Function 类型，该回调函数签名为 function(row, index, rows)，其中 row 表示行数据对象、index 表示行索引号、rows 表示当前 easyui-treegrid 所有节点对象集合；
        //          如果 param 参数为 Function 类型，则 findRows 方法会对当前 easyui-treegrid 的当前页的每一行数据调用该回调函数；
        //          当回调函数返回 true 时，则返回的结果集中将会包含该行数据；
        //          如果该回调函数始终未返回 true，则该方法最终返回一个长度为 0 的数组对象。
        //      Array 类型，数组中的每一项都可以定义为如下类型：
        //          待查找的行数据的 idField(主键) 字段值；
        //          Function 类型；具体回调函数签名参考 findRow 方法中 param 参数为 function 类型时的定义；
        //          当 param 参数定义为 Array 类型时，则 findRows 方法会对数组中的每一项循环调用 findRow 方法，并过滤掉 findRow 方法返回 null 的结果行；
        //  返回值：返回一个 Array 数组对象；数组中的每一项都是 JSON-Object 类型，表示一个行数据对象；如果未找到相应数据，则返回一个长度为 0 的数组对象。
        findRows: function (jq, param) { return findRows(jq[0], param); },

        remove: function (jq, id) { return jq.each(function () { removeRow(this, id); }); },

        //  删除 easyui-treegrid 中当前页指定的节点以及它所有的子节点；参数 param 表示要删除的内容；该参数可以是以下三种类型：
        //      表示要删除的行数据的 idField(主键) 字段值；
        //      Function 类型，该回调函数签名为 function(row, index, rows)，其中 row 表示行数据对象、index 表示行索引号、rows 表示当前 easyui-treegrid 所有节点对象集合；
        //          如果 param 参数为 Function 类型，则 deleteRow 方法会对当前 easyui-treegrid 的当前页的每一行数据调用该回调函数；
        //          当回调函数返回 true 时，则表示查找到了需要被删除的行，deleteRow 方法将会删除该行数据并立即停止和跳出循环操作；
        //  返回值：返回表示当前 easyui-treegrid 组件的 jQuery 链式对象。
        deleteRow: function (jq, param) { return jq.each(function () { deleteRow(this, param); }); },

        //  删除多行数据，参数 param 表示要删除的内容；该参数可以是以下两种类型：
        //      Function 类型，该回调函数签名为 function(row, index, rows)，其中 row 表示行数据对象、index 表示行索引号、rows 表示当前 easyui-treegrid 所有节点对象集合；
        //          如果 param 参数为 Function 类型，则 deleteRows 方法会对当前 easyui-treegrid 的当前页的每一行数据调用该回调函数；
        //          当回调函数返回 true 时，则表示查找到了需要被删除的行，deleteRows 方法将会删除该行数据，并遍历下一行数据直至数数据集的末尾；
        //      Array 类型，数组中的每一项目均表示要删除的行的行索引号或者 idField(主键) 字段值。
        //  返回值：返回表示当前 easyui-treegrid 组件的 jQuery 链式对象。
        deleteRows: function (jq, param) { return jq.each(function () { deleteRows(this, param); }); },

        //  设置 easyui-treegrid 中列的标题；参数 param 是一个 json 对象，包含如下属性：
        //      field: 列字段名称
        //      title: 列的新标题
        //  返回值：返回表示当前 easyui-treegrid 组件的 jQuery 链式对象。
        setColumnTitle: function (jq, param) { return jq.each(function () { setColumnTitle(this, param); }); },

        //  选中 easyui-treegrid 当前页的某行节点数据；该方法的参数 param 可以定义为如下两种类型：
        //      1、表示行节点的 id 值；
        //      2、JSON-Object 对象，该对象需定义如下属性：
        //          id:     表示要获取的行节点的 id 值；
        //          cascade:Boolean 类型值，默认为 false，表示是否连同其子级节点的 DOM 行对象一并获取并返回。
        //  返回值：返回表示当前 easyui-treegrid 组件的 jQuery 链式对象。
        select: function (jq, param) { return jq.each(function () { selectRow(this, param); }); },

        //  不选中 easyui-treegrid 当前页的某行节点数据；该方法的参数 param 可以定义为如下两种类型：
        //      1、表示行节点的 id 值；
        //      2、JSON-Object 对象，该对象需定义如下属性：
        //          id:     表示要获取的行节点的 id 值；
        //          cascade:Boolean 类型值，默认为 false，表示是否连同其子级节点的 DOM 行对象一并获取并返回。
        //  返回值：返回表示当前 easyui-treegrid 组件的 jQuery 链式对象。
        unselect: function (jq, param) { return jq.each(function () { unselectRow(this, param); }); },

        //  同 select 方法；
        selectRow: function (jq, param) { return jq.each(function () { selectRow(this, param); }); },

        //  同 unselect 方法；
        unselectRow: function (jq, param) { return jq.each(function () { unselectRow(this, param); }); },

        //  选则 easyui-treegrid 当前页的某行节点数据；该方法的参数 param 可以定义为如下两种类型：
        //      1、表示行节点的 id 值；
        //      2、JSON-Object 对象，该对象需定义如下属性：
        //          id:     表示要获取的行节点的 id 值；
        //          cascade:Boolean 类型值，默认为 false，表示是否连同其子级节点的 DOM 行对象一并获取并返回。
        //  返回值：返回表示当前 easyui-treegrid 组件的 jQuery 链式对象。
        check: function (jq, param) { return jq.each(function () { checkRow(this, param); }); },

        //  不选则 easyui-treegrid 当前页的某行节点数据；该方法的参数 param 可以定义为如下两种类型：
        //      1、表示行节点的 id 值；
        //      2、JSON-Object 对象，该对象需定义如下属性：
        //          id:     表示要获取的行节点的 id 值；
        //          cascade:Boolean 类型值，默认为 false，表示是否连同其子级节点的 DOM 行对象一并获取并返回。
        //  返回值：返回表示当前 easyui-treegrid 组件的 jQuery 链式对象。
        uncheck: function (jq, param) { return jq.each(function () { uncheckRow(this, param); }); },

        // 同 check 方法。
        checkRow: function (jq, param) { return jq.each(function () { checkRow(this, param); }); },

        // 同 uncheck 方法。
        uncheckRow: function (jq, param) { return jq.each(function () { uncheckRow(this, param); }); },

        //  设置当前 easyui-treegrid 控件的表头过滤器；该函数提供如下参数：
        //      columnFilter: 参见属性 columnFilter
        //  返回值：返回表示当前 easyui-treegrid 组件的 jQuery 链式对象。
        setColumnFilter: function (jq, columnFilter) { return jq.each(function () { setColumnFilter(this, columnFilter); }); },

        //  对当前 easyui-treegrid 控件按特定条件进行行过滤/显示操作；该方法的参数 param 可以定义为如下两种类型：
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
        //  返回值：返回表示当前 easyui-treegrid 组件的 jQuery 链式对象。
        columnFilterSelect: function (jq, param) { return jq.each(function () { columnFilterSelect(this, param); }); },

        //  设置当前 easyui-treegrid 控件的 offset 属性；该操作能让 offset 即可随浏览器窗口大小调整而生效或禁用；
        //  备注： 参数 offset 格式参考扩展属性 offset。
        //  返回值：返回表示当前 easyui-treegrid 组件的 jQuery 链式对象。
        setOffset: function (jq, offset) { return jq.each(function () { setOffset(this, offset); }); },

        //  将当前表格数据导出为 excel 文件；该函数定义了一个参数 isAll；
        //  参数 isAll 指示是否导出全部而非仅当前页数据，如果不传入该参数默认为 false 即导出当前页数据。
        //  当参数 isAll 为 true 并且 remotePaging 为 true 时，需要当前 easyui-treegrid 控件的 url 属性指示的服务器数据源支持查询所有数据
        //      （以 rows: 0 方式不分页查询所有数据）。
        //  返回值：返回表示当前 easyui-treegrid 组件的 jQuery 链式对象。
        exportExcel: function (jq, isAll) { return jq.each(function () { exportGrid(this, isAll); }); }

    };
    var defaults = $.fn.treegrid.extensions.defaults = {

        //  增加 easyui-treegrid 的自定义扩展属性，该属性表示当屏幕大小调整时候随屏幕大小尺寸调整而自身大小调整的偏移量；
        //  该参数为一个 JSON 格式对象，该对象定义如下属性：
        //      width: 表示相对于浏览器窗口宽度的偏移量，如果是正数则其宽度比浏览器窗口大，反之则其宽度比浏览器窗口小，int类型；
        //      height: 表示相对于浏览器窗口高度的偏移量，如果是正数则其高度比浏览器窗口大，反之则其高度比浏览器窗口小，int类型；
        //  备注：该参数默认为 null，表示不随屏幕尺寸大小调整而调整；
        //      如果未定义 width 或者 width: 0，则表示屏幕大小调整时 easyui-treegrid 的 width 属性撑满屏幕宽度；
        //      如果未定义 height 或者 height: 0，则表示屏幕大小调整时 easyui-treegrid 的 height 属性撑满屏幕宽度；
        offset: null,

        //  扩展 easyui-treegrid 的自定义属性，表示当前 easyui-treegrid 控件是否支持平滑数据格式。
        //  当支持平滑数据格式时，数据元素中不需要通过指定 children 来指定子节点，而是支持通过指定的 parentField 值所表示的列的值来指示其父级节点。
        //  Boolean 类型值，默认为 false。
        dataPlain: false,

        //  扩展 easyui-treegrid 的自定义属性，表示当前 easyui-treegrid 控件支持平滑数据格式时，程序用哪个 field 表示当前行数据的父级节点 idField 值
        //  String 类型值，默认为 "pid"。
        parentField: "pid",

        //  扩展 easyui-treegrid 的自定义属性，表示当左键点击带有子节点的条目时，是否自动展开/折叠相应节点。
        //  Boolean 类型，默认为 false。
        //  备注：该功能不会影响到 easyui-treegrid 的原生事件 onClick。
        toggleOnClick: false,

        //  扩展 easyui-treegrid 的自定义属性，表示同一级菜单节点下，只允许一个节点被展开。
        //  Boolean 类型，默认为 false。
        //  当该属性设置为 true 时，建议同时把 animate 属性设置为 false，以免影响菜单联动折叠时的美观效果。
        onlyNodeExpand: false,

        //  扩展 easyui-treegrid 的自定义属性，表示当前 easyui-treegrid 控件是否支持级联选择；
        //  Boolean 类型值，默认为 false。
        //  备注：在当 checkOnSelect、singleSelect 这两个属性都为 true 的情况下，不支持级联选择，此时属性 cascadeCheck 无效；
        cascadeCheck: false,

        //  覆盖 easyui-treegrid 的原生属性 loadFilter，以支持相应扩展功能(支持平滑数据格式)。
        loadFilter: loadFilter,

        //  覆盖 easyui-treegrid 的原生事件 onExpand，以支持相应扩展功能。
        onExpand: onExpand,


        //  增加 easyui-treegrid 的自定义扩展属性；
        //      该属性表示当设定了属性 rowContextMenu 时，是否将双击数据行 onDblClickRow 事件的响应函数
        //      设置为 rowContextMenu 的第 "dblClickRowMenuIndex" 个菜单项的点击响应函数，并将该菜单项的字体加粗；
        //  Boolean 类型值，默认为 true；
        //  备注：当设置了有效的属性 rowContextMenu 时候，该功能方有效。
        //      自动绑定的 onDblClickRow 的回调函数中将会调用 rowContextMenu 的第 "dblClickRowMenuIndex" 个菜单项的点击响应函数，但是回调函数中不能用到参数 e 和 menu。
        autoBindDblClickRow: true,

        //  增加 easyui-treegrid 的自定义扩展属性；
        //  该属性表示当设定了属性 autoBindDblClickRow: true，双击行数据触发的右键菜单项事件的索引号；
        //      意即触发第几个右键菜单项上的事件。
        //  Number 类型值，从 0 开始计数，默认为 0；
        //  备注：当设置了自定义属性 autoBindDblClickRow: true并且设置了有效的属性 rowContextMenu 时，该功能方有效；
        //      如果此索引值超出菜单数量范围，则无效。
        dblClickRowMenuIndex: 0,

        //  增加 easyui-treegrid 的自定义扩展属性，该属性表示是否启用右键点击表头或者行数据时候弹出菜单中具有 "导出数据" 菜单的功能；
        //  该属性可以定义为以下类型：
        //      Boolean 类型值，表示是否启用右键菜单中的“导出数据”菜单项功能，默认为 false。
        //      JSON-Object 类型，该 JSON-Object 可以包含如下属性：
        //          current:   Boolean 类型值，表示是否启用“导出当前页”的菜单项，默认为 true；
        //          all:   Boolean 类型值，表示是否启用“导出全部”的菜单项，默认为 true；
        //          submenu:    表示这四个菜单项是否以子菜单方式呈现，默认为 true；
        //  备注：当 enableRowContextMenu 属性设置为 true 时，该属性才有效。
        //  导出数据功能的方法尚未实现，所以...就让它保持默认为 false 吧。
        exportMenu: false,

        //  增加 easyui-treegrid 的自定义扩展属性，Boolean 类型值，该属性表示是否启用：
        //      当右键单击行数据时选择右键当前单击的行的功能，默认为 true；
        //  注意：当此参数设置为 true 时，右键点击行会对性能产生一定影响；当时数据量大(单页数据超过 100 行)时不建议使用。
        selectOnRowContextMenu: false,

        //  增加 easyui-treegrid 的自定义扩展属性，Boolean 类型值，该属性表示是否启用：
        //      右键(表头右键或行右键)点击时弹出的菜单项，如果是 disabled: true ，则不显示的功能，默认为 false；
        hideDisabledMenu: false,

        //  增加 easyui-treegrid 的自定义扩展属性，该属性表示表列头右键菜单，为一个 Array 对象；数组中的每一个元素都具有如下属性:
        //      id:         表示菜单项的 id；
        //      text:       表示菜单项的显示文本；
        //      iconCls:    表示菜单项的左侧显示图标；
        //      disabled:   表示菜单项是否被禁用(禁用的菜单项点击无效)；
        //      hideOnClick:    表示该菜单项点击后整个右键菜单是否立即自动隐藏；
        //      bold:           Boolean 类型值，默认为 false；表示该菜单项是否字体加粗；
        //      style:          JSON-Object 类型值，默认为 null；表示要附加到该菜单项的样式；
        //      handler:    表示菜单项的点击事件，该事件函数格式为 function(e, field, eventData, grid, item, menu)，其中 this 指向菜单项本身
        //  备注：具体格式参考 easyui-treegrid 的 toolbar 属性为 Array 对象类型的格式；
        //      当 enableHeaderContextMenu 属性为 true 时，该设置方有效。
        headerContextMenu: null,

        //  增加 easyui-treegrid 的自定义扩展属性，该属性表示数据行右键菜单，为一个 Array 对象；；数组中的每一个元素都具有如下属性:
        //      id:         表示菜单项的 id；
        //      text:       表示菜单项的显示文本；
        //      iconCls:    表示菜单项的左侧显示图标；
        //      disabled:   表示菜单项是否被禁用(禁用的菜单项点击无效)；
        //      hideOnClick:    表示该菜单项点击后整个右键菜单是否立即自动隐藏；
        //      bold:           Boolean 类型值，默认为 false；表示该菜单项是否字体加粗；
        //      style:          JSON-Object 类型值，默认为 null；表示要附加到该菜单项的样式；
        //      handler:    表示菜单项的点击事件，该事件函数格式为 function(e, row, eventData, grid, item, menu)，其中 this 指向菜单项本身
        //  备注：具体格式参考 easyui-treegrid 的 toolbar 属性为 Array 对象类型的格式；
        //      当 enableRowContextMenu 属性为 true 时，该设置方有效。
        rowContextMenu: null,

        //  增加 easyui-treegrid 的自定义扩展属性，该属性表示是否启用 easyui-treegrid 的表头列点击按钮菜单；
        //  Boolean 类型值，默认为 true。 
        enableHeaderClickMenu: true,

        //  增加 easyui-treegrid 的自定义扩展属性，该属性表示是否启用 easyui-treegrid 的表头右键菜单；
        //  Boolean 类型值，默认为 true。
        enableHeaderContextMenu: true,

        //  增加 easyui-treegrid 的自定义扩展属性，该属性表示是否启用 easyui-treegrid 的行右键菜单；
        //  Boolean 类型值，默认为 true。
        enableRowContextMenu: true,

        //  扩展 easyui-treegrid 的自定义属性，表示是否启用右键菜单中的“展开当前、折叠当前、展开当前所有、折叠当前所有”菜单项的功能；
        //  该属性可以定义为以下类型：
        //      Boolean 类型，表示是否启用这四个菜单项；
        //      JSON-Object 类型，该 JSON-Object 可以包含如下属性：
        //          expand:     布尔类型的值，也可是一个返回布尔值的函数，表示是否显示“展开当前”菜单；
        //          expandAll:  布尔类型的值，也可是一个返回布尔值的函数，表示是否显示“展开当前所有”菜单；
        //          collapse:   布尔类型的值，也可是一个返回布尔值的函数，表示是否显示“折叠当前”菜单；
        //          collapseAll: 布尔类型的值，也可是一个返回布尔值的函数，表示是否显示“折叠当前所有”菜单；
        //          submenu:    表示这四个菜单项是否以子菜单方式呈现，默认为 true；
        //          上面四个属性，如果参数的值为函数，则函数的签名为 function(e, node, treegrid, item, menu)。
        //  备注：当 enableRowContextMenu 属性设置为 true 时，该属性才有效。
        //      这四个菜单点击时，会自动触发 easyui-treegrid 的折叠/展开菜单项的相应事件。
        toggleMenu: true,

        //  扩展 easyui-treegrid 的自定义属性，表示是否启用右键菜单中的“上移、下移、上移一级、下移一级”菜单项的功能；
        //  该属性可以定义为以下类型：
        //      Boolean 类型，表示是否启用这四个菜单项，默认为 false；
        //      JSON-Object 类型，该 JSON-Object 可以包含如下属性：
        //          up:         布尔类型的值，也可是一个返回布尔值的函数，表示是否显示“上移”菜单；
        //          upLevel:    布尔类型的值，也可是一个返回布尔值的函数，表示是否显示“上移一级”菜单；
        //          down:       布尔类型的值，也可是一个返回布尔值的函数，表示是否显示“下移”菜单；
        //          downLevel:  布尔类型的值，也可是一个返回布尔值的函数，表示是否显示“下移一级”菜单；
        //          submenu:    表示这四个菜单项是否以子菜单方式呈现，默认为 true；
        //          上面四个属性，如果参数的值为函数，则函数的签名为 function(e, node, treegrid, item, menu)。
        //  备注：当 enableRowContextMenu 属性设置为 true 时，该属性才有效。
        //      这四个菜单点击时，会自动触发 easyui-treegrid 的 onDrop 事件。
        moveMenu: false,

        //  增加 easyui-treegrid 的自定义扩展属性，该属性表示是否启用右键菜单中的“翻页”菜单项的功能；
        //  该属性可以定义为以下类型：
        //      Boolean 类型值，表示是否启用右键菜单中的“翻页”菜单项功能，默认为 true。
        //      JSON-Object 类型，该 JSON-Object 可以包含如下属性：
        //          disabled:   Boolean 类型值，表示是否启用右键菜单中的“翻页”菜单项功能，默认为 true；
        //          submenu:    表示这四个菜单项是否以子菜单方式呈现，默认为 true；
        //  备注：当 enableRowContextMenu 属性设置为 true 时，该属性才有效。
        pagingMenu: false,

        //  增加 easyui-treegrid 的自定义扩展属性，该属性表示是否启用右键菜单中的“刷新当前页”菜单项的功能；
        //  Boolean 类型值，默认为 true。
        refreshMenu: true,

        //  增加 easyui-treegrid 的自定义扩展属性，该属性表示是否启用表格的行节点拖动功能；
        //  Boolean 类型值，默认为 false。
        dndRow: false,

        //  增加 easyui-treegrid 的自定义扩展属性，该属性表示是否启用行数据的 tooltip 功能；
        //  该属性可以是一个 Boolean 类型值；也可以是一个格式为 function(row) 的回调函数；
        //  如果该参数是一个回调函数，则表示启用行数据的 tooltip 功能，并且该函数的返回值为 tooltip 的 content 值。
        //  默认为 Boolean 类型，值为 false。
        //  注意：当启用该配置属性后，所有列的 tootip 属性就会自动失效。
        rowTooltip: false,

        //  增加 easyui-treegrid 的自定义扩展属性，该属性表示在触发 beginEdit 事件后，是否构建仿 ext-grid-rowediting 行编辑的“保存”和“取消”按钮面板；
        //  Boolean 类型值，默认为 true。
        extEditing: true,

        //  增加 easyui-treegrid 的自定义扩展属性，该属性表示在双击 data-row(数据行) 时，是否自动启用该行的编辑功能(执行 beginEdit 操作)；
        //  Boolean 类型值，默认为 false。
        autoEditing: false,

        //  增加 easyui-treegrid 的自定义扩展属性，该属性表示在 autoEditing: true 时自动触发行编辑效果的事件(双击行还是单击行)。
        //  String 类型值，可选的值为 "onClickRow" 和 "onDblClickRow"，默认为 "onDblClickRow"
        //  注意：该参数仅在 autoEditing: true 时才有效。
        //        因 "onDblClickRow" 在 autoBindDblClickRow: true 时会自动将右键菜单第一项绑定至行双击事件中；
        //        所以建议在 autoBindDblClickRow: true 且行右键菜单第一项为行编辑功能时，autoEditing 设置为 false 或 autoEditingEvent 设置为 "onClickRow"
        autoEditingEvent: "onDblClickRow",

        //  增加 easyui-treegrid 的自定义扩展属性，该属性表示在表格失去焦点(逻辑上失去焦点，实际上是判断页面上表格外的其他部分被点击)后，表格是否自动关闭行编辑状态。
        //  Boolean 类型值，默认为 true。
        finishEditOnBlur: true,

        //  增加 easyui-treegrid 的自定义扩展属性，该属性表示当 finishEditOnBlur: true，点击哪个区域会导致当前表格自动关闭行编辑状态。
        //  该属性可以是一个 HTML-DOM 对象、也可以是一个 jQuery-DOM 对象、或者一个 jquery-DOM selector。默认为 window.document。
        finishEditLocale: window.document,

        //  增加 easyui-treegrid 的自定义扩展属性，该属性表示当 finishEditOnBlur: true 时并且在表格失去焦点后将要触发表格自动关闭行编辑状态时，关闭行编辑状态所使用的方法。
        //  String 类型值，可选的值为 "endEdit" 或 "cancelEdit"，默认为 "endEdit"。
        finishEditMethod: "endEdit",

        //  增加 easyui-treegrid 的自定义扩展属性，该属性表示是否在一个时刻只允许一行数据开启编辑状态(当某行数据开启编辑状态时，其他正在编辑的行将会被自动执行 endEdit 操作)；
        //  Boolean 类型值，默认为 true。
        singleEditing: true,

        //  增加 easyui-treegrid 的自定义扩展属性，该属性表示当前表格的列过滤器设置参数；该参数是一个 JSON 格式的对象，该对象定义如下属性：
        //      panelHeight: 列过滤器控件面板的高度，默认为 100，该值最小为 60；
        //      position:   表示列过滤器的位置，String 类型，可以填入的值限定在以下范围：
        //          "top":  表示列过滤器被放置在表头的上方；
        //          "bottom":   表示列过滤器被放置在表头的下方；默认值。
        //  备注：关于列过滤器组件中每个列具体的过滤效果设置，参见扩展的 ColumnOption 属性(见本源码文件后面注释)；
        //  注意：
        //      1、如果不定义该参数，则表示当前 easyui-treegrid 控件不启用列过滤器功能；该参数不影响表头右键过滤功能；
        //      2、该功能不支持多行表头；也就是说如果当前 easyui-treegrid 设置了多行表头，则该功能无效；
        //      3、该功能仅实现本地数据过滤，也就是说该插件不会在处理远程数据请求时将过滤参数信息发送到远程服务器；
        //      4、当启用该功能时，easyui-treegrid 的属性 fitColumns 请保持默认值为 false，否则列头过滤器组件可能导致表头列不能对齐而布局混乱。
        columnFilter: null,

        //  
        showFilterText: undefined,

        //  覆盖 easyui-treegrid 的原生属性 loader，以支持相应扩展功能。
        loader: loader,

        //  覆盖 easyui-treegrid 的原生属性事件 onLoadSuccess，以支持相应扩展功能。
        //  注意：如果调用者需要在自己的代码中使用该事件，请以覆盖方式重写，而非直接重写。
        //  覆盖方式重写示例：
        //      grid.treegrid({
        //          onLoadSuccess: function(data) {
        //              $.fn.treegrid.extensions.onLoadSuccess.apply(this, arguments);  //这句一定要加上。
        //              ...                                     //这里是调用者的其他附加逻辑代码
        //          }
        //      });
        onLoadSuccess: onLoadSuccess,

        //  覆盖 easyui-treegrid 的原生属性事件 onResizeColumn，以支持相应扩展功能。
        //  注意：如果调用者需要在自己的代码中使用该事件，请以覆盖方式重写，而非直接重写。
        //  覆盖方式重写示例：
        //      grid.treegrid({
        //          onResizeColumn: function(data) {
        //              $.fn.treegrid.extensions.onResizeColumn.apply(this, arguments);  //这句一定要加上。
        //              ...                                     //这里是调用者的其他附加逻辑代码
        //          }
        //      });
        onResizeColumn: onResizeColumn,

        //  覆盖 easyui-treegrid 的原生属性事件 onBeforeEdit，以支持相应扩展功能。
        onBeforeEdit: onBeforeEdit,

        //  覆盖 easyui-treegrid 的原生属性事件 onAfterEdit，以支持相应扩展功能。
        //  注意：如果调用者需要在自己的代码中使用该事件，请以覆盖方式重写，而非直接重写。
        //  覆盖方式重写示例：
        //      grid.treegrid({
        //          onAfterEdit: function(data) {
        //              $.fn.treegrid.extensions.onAfterEdit.apply(this, arguments);  //这句一定要加上。
        //              ...                                     //这里是调用者的其他附加逻辑代码
        //          }
        //      });
        onAfterEdit: onAfterEdit,

        //  覆盖 easyui-treegrid 的原生属性事件 onCancelEdit，以支持相应扩展功能。
        onCancelEdit: onCancelEdit,

        //  扩展 easyui-treegrid 的自定义事件；该事件表示删除指定的列前触发的动作；该事件回调函数提供如下参数：
        //      field:  表示要被删除的列的 field 值。
        //  备注：如果该事件回调函数返回 false，则不进行删除列的操作。
        //  该事件函数中的 this 指向当前 easyui-treegrid 的 DOM 对象(非 jQuery 对象)；
        onBeforeDeleteColumn: function (field) { },

        //  扩展 easyui-treegrid 的自定义事件；该事件表示删除指定的列后触发的动作；该事件回调函数提供如下参数：
        //      field:  表示要被删除的列的 field 值。
        //  该事件函数中的 this 指向当前 easyui-treegrid 的 DOM 对象(非 jQuery 对象)；
        onDeleteColumn: function (field) { },

        //  扩展 easyui-treegrid 的自定义事件；该事件表示移动指定的列前触发的动作；该事件回调函数提供如下参数：
        //      source:  表示要被移动的列的 field 值。
        //      target:  表示目标位置的列的 field 值。
        //      point :  表示移动的方式；这是一个 String 类型值，可能的值包括：
        //          "before":   表示将列 source 移动至列 target 的前一格位置；
        //          "after" :   表示将列 source 移动至列 target 的后一格位置；
        //  备注：如果该事件回调函数返回 false，则不进行删除列的操作。
        //  该事件函数中的 this 指向当前 easyui-treegrid 的 DOM 对象(非 jQuery 对象)；
        onBeforeMoveColumn: function (source, target, point) { },

        //  扩展 easyui-treegrid 的自定义事件；该事件表示移动指定的列后触发的动作；该事件回调函数提供如下参数：
        //      source:  表示要被移动的列的 field 值。
        //      target:  表示目标位置的列的 field 值。
        //      point :  表示移动的方式；这是一个 String 类型值，可能的值包括：
        //          "before":   表示将列 source 移动至列 target 的前一格位置；
        //          "after" :   表示将列 source 移动至列 target 的后一格位置；
        //  该事件函数中的 this 指向当前 easyui-treegrid 的 DOM 对象(非 jQuery 对象)；
        onMoveColumn: function (source, target, point) { },

        //  扩展 easyui-treegrid 的自定义事件；该事件表示移动 tree-node 之前触发的动作；该事件回调函数提供如下三个参数：
        //          target: 表示目标位置的 tree-node 对象；
        //          source: 表示要移动的 tree-node 对象；
        //          point:  表示移动到目标节点 target 的位置，String 类型，可能的值包括：
        //              "append":   表示追加为目标节点 target 的子节点，默认值；
        //              "top":      表示移动到目标节点 target 的上一格位置；
        //              "bottom":   表示追加为目标节点 target 的下一格位置；
        //  该事件函数中的 this 指向当前 easyui-treegrid 的 DOM 对象(非 jQuery 对象)；
        //  如果该事件函数返回 false，则会立即停止移动数据节点操作；
        onBeforeDrop: function (target, source, point) { },

        //  扩展 easyui-treegrid 的自定义事件；该事件表示移动 tree-node 之后触发的动作；该事件回调函数提供如下三个参数：
        //          target: 表示目标位置的 tree-node 对象；
        //          source: 表示要移动的 tree-node 对象；
        //          point:  表示移动到目标节点 target 的位置，String 类型，可能的值包括：
        //              "append":   表示追加为目标节点 target 的子节点，默认值；
        //              "top":      表示移动到目标节点 target 的上一格位置；
        //              "bottom":   表示追加为目标节点 target 的下一格位置；
        //  该事件函数中的 this 指向当前 easyui-treegrid 的 DOM 对象(非 jQuery 对象)；
        onDrop: function (target, source, point) { },

        //  扩展 easyui-treegrid 的自定义事件；该事件表示拖动 data-row(数据行) 之前触发的动作；该事件回调函数提供如下参数：
        //      row:    表示被拖动的 data-row(数据行) 的行数据对象，是一个 JSON-Object。
        //  该事件函数中的 this 指向当前 easyui-treerid 的 DOM 对象(非 jQuery 对象)；
        //  备注：如果该事件函数返回 false，则取消当前的拖动 data-row(数据行) 操作。
        onBeforeDrag: function (row) { },

        //  扩展 easyui-treegrid 的自定义事件；该事件表示开始拖动 data-row(数据行) 时触发的动作；该事件回调函数提供如下参数：
        //      row:    表示被拖动的 data-row(数据行) 的行数据对象，是一个 JSON-Object。
        //  该事件函数中的 this 指向当前 easyui-treerid 的 DOM 对象(非 jQuery 对象)；
        onStartDrag: function (row) { },

        //  扩展 easyui-treegrid 的自定义事件；该事件表示结束拖动 data-row(数据行) 时触发的动作；该事件回调函数提供如下参数：
        //      row:    表示被拖动的 data-row(数据行) 的行数据对象，是一个 JSON-Object。
        //  该事件函数中的 this 指向当前 easyui-treerid 的 DOM 对象(非 jQuery 对象)；
        onStopDrag: function (row) { },

        //  扩展 easyui-treegrid 的自定义事件；该事件表示当有其他的 data-row(数据行) 被拖动至当前 data-row(数据行) 时所触发的动作；该事件回调函数提供如下两个参数：
        //      target: 表示当前 data-row(数据行) 的行数据对象，是一个 JSON-Object；
        //      source: 表示拖动过来的 data-row(数据行) 行数据对象，是一个 JSON-Object。
        //  该事件函数中的 this 指向当前 easyui-treerid 的 DOM 对象(非 jQuery 对象)；
        //  备注：如果该事件函数返回 false，则立即取消当前的 data-row(数据行) 接收拖动过来对象的操作，并禁用当前 data-row(数据行) 的 droppable 效果；
        onDragEnter: function (target, source) { },

        //  扩展 easyui-treegrid 的自定义事件；该事件表示当有其他的 data-row(数据行) 被拖动至当前 data-row(数据行) 后并在上面移动时所触发的动作；该事件回调函数提供如下两个参数：
        //      target: 表示当前 data-row(数据行) 的行数据对象，是一个 JSON-Object；
        //      source: 表示拖动过来的 data-row(数据行) 行数据对象，是一个 JSON-Object。
        //  该事件函数中的 this 指向当前 easyui-treerid 的 DOM 对象(非 jQuery 对象)；
        //  备注：如果该事件函数返回 false，则立即取消当前的 data-row(数据行) 接收拖动过来对象的操作，并禁用当前 data-row(数据行) 的 droppable 效果；
        onDragOver: function (target, source) { },

        //  扩展 easyui-treegrid 的自定义事件；该事件表示当有其他的 data-row(数据行) 被拖动至当前 data-row(数据行) 后并拖动离开时所触发的动作；该事件回调函数提供如下两个参数：
        //      target: 表示当前 data-row(数据行) 的行数据对象，是一个 JSON-Object；
        //      source: 表示拖动过来的 data-row(数据行) 行数据对象，是一个 JSON-Object。
        //  该事件函数中的 this 指向当前 easyui-treerid 的 DOM 对象(非 jQuery 对象)；
        onDragLeave: function (target, source) { },

        //  扩展 easyui-treegrid 的自定义事件；该事件表示执行 update 方法前所触发的动作；该事件回调函数提供如下两个参数：
        //      id:     表示要进行 update 操作的行的 idField 值；
        //      row:    表示要进行更新操作的新的行数据对象；
        //  该事件函数中的 this 指向当前 easyui-treerid 的 DOM 对象(非 jQuery 对象)；
        //  备注：如果该事件回调函数返回 false，则立即取消即将要执行的 update 操作。
        onBeforeUpdate: function (index, row) { },

        //  扩展 easyui-treegrid 的自定义事件；该事件表示执行 updateRow 方法后所触发的动作；该事件回调函数提供如下两个参数：
        //      id:  表示要进行 update 的行的 idField 值
        //      row:    表示要进行更新操作的新的行数据对象；
        //  该事件函数中的 this 指向当前 easyui-treerid 的 DOM 对象(非 jQuery 对象)；
        onUpdate: function (index, row) { },

        //  扩展 easyui-treegrid 的自定义事件；该事件表示执行 append 方法前所触发的动作；该事件回调函数提供如下参数：
        //      parent: 表示被添加至的父级节点的 idField 值；如果该值为 null 或者为 undefined，则表示数据被添加至根节点；
        //      data:   表示被添加的节点数据，是一个 Array 数组对象；数组中的每一项都是一个表示节点数据的 JSON-Object。
        //  该事件函数中的 this 指向当前 easyui-treerid 的 DOM 对象(非 jQuery 对象)；
        //  备注：如果该事件回调函数返回 false，则立即取消即将要执行的 append 操作。
        onBeforeAppend: function (parent, data) { },

        //  扩展 easyui-treegrid 的自定义事件；该事件表示执行 append 方法后所触发的动作；该事件回调函数提供如下参数：
        //      parent: 表示被添加至的父级节点的 idField 值；如果该值为 null 或者为 undefined，则表示数据被添加至根节点；
        //      data:   表示被添加的节点数据，是一个 Array 数组对象；数组中的每一项都是一个表示节点数据的 JSON-Object。
        onAppend: function (parent, data) { },

        //  扩展 easyui-treegrid 的自定义事件；该事件表示执行 insert 方法前所触发的动作；该事件回调函数提供如下三个参数：
        //      before: 如果该参数有值，则其值为某个节点的 idField 值，表示被插入的节点将会放置在该节点之前；
        //      after:  如果该参数有值，则其值为某个节点的 idField 值，表示被插入的节点将会放置在该节点之后；
        //      data:   表示被插入的节点数据，是一个 Array 数组对象；数组中的每一项都是一个表示节点数据的 JSON-Object。
        //  该事件函数中的 this 指向当前 easyui-treerid 的 DOM 对象(非 jQuery 对象)；
        //  备注：如果该事件回调函数返回 false，则立即取消即将要执行的 insert 操作。
        //      该回调函数的参数 before 和 after，两者只有一个会有值，另一个的值将会为 null 或者 undefined。
        onBeforeInsert: function (before, after, row) { },

        //  扩展 easyui-treegrid 的自定义事件；该事件表示执行 insert 方法后所触发的动作；该事件回调函数提供如下三个参数：
        //      before: 如果该参数有值，则其值为某个节点的 idField 值，表示被插入的节点将会放置在该节点之前；
        //      after:  如果该参数有值，则其值为某个节点的 idField 值，表示被插入的节点将会放置在该节点之后；
        //      data:   表示被插入的节点数据，是一个 Array 数组对象；数组中的每一项都是一个表示节点数据的 JSON-Object。
        //  该事件函数中的 this 指向当前 easyui-treerid 的 DOM 对象(非 jQuery 对象)；
        //  备注：如果该事件回调函数返回 false，则立即取消即将要执行的 insert 操作。
        //      该回调函数的参数 before 和 after，两者只有一个会有值，另一个的值将会为 null 或者 undefined。
        onInsert: function (before, after, data) { },

        onBeforeRemove: function (row) { },

        onRemove: function (row) { }
    };

    $.extend($.fn.treegrid.defaults, defaults);
    $.extend($.fn.treegrid.methods, methods);



    var view = {
        onBeforeRender: function (target, id, data) {
            if ($.isArray(id)) {
                data = { total: id.length, rows: id };
                id = null;
            }
            if (!data) { return false; }
            var state = $.data(target, "treegrid");
            var opts = state.options;
            if (data.length == undefined) {
                if (data.footer) { state.footer = data.footer; }
                if (data.total) { state.total = data.total; }
                data = this.transfer(target, id, data.rows);
            }
            setParent(data.length == undefined ? data.rows : data, id);
            var node = findRow(target, id);
            if (node) {
                if (node.children) {
                    node.children = node.children.concat(data);
                } else {
                    node.children = data;
                }
            } else {
                state.data = state.data.concat(data);
            }
            if (!opts.remoteSort) { this.sort(target, data); }
            this.treeNodes = data;
            this.treeLevel = $(target).treegrid("getLevel", id);

            function setParent(rows, id) {
                for (var i = 0; i < rows.length; i++) {
                    var row = rows[i];
                    row._parentId = id;
                    if (row.children && row.children.length) {
                        setParent(row.children, row[opts.idField]);
                    }
                }
            };
        }
    };

    $.extend($.fn.treegrid.defaults.view, view);

})(jQuery);
