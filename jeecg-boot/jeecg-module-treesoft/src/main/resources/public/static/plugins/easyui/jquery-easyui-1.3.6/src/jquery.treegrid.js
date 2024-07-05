/**
 * jQuery EasyUI 1.3.6
 * 
 * Copyright (c) 2009-2014 www.jeasyui.com. All rights reserved.
 *
 * Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
 * To use it on other terms please contact us at info@jeasyui.com
 *
 */
(function ($) {
    function _1(_2) {
        var _3 = $.data(_2, "treegrid");
        var _4 = _3.options;
        $(_2).datagrid($.extend({}, _4, {
            url: null, data: null, loader: function () {
                return false;
            }, onBeforeLoad: function () {
                return false;
            }, onLoadSuccess: function () {
            }, onResizeColumn: function (_5, _6) {
                _20(_2);
                _4.onResizeColumn.call(_2, _5, _6);
            }, onSortColumn: function (_7, _8) {
                _4.sortName = _7;
                _4.sortOrder = _8;
                if (_4.remoteSort) {
                    _1f(_2);
                } else {
                    var _9 = $(_2).treegrid("getData");
                    _39(_2, 0, _9);
                }
                _4.onSortColumn.call(_2, _7, _8);
            }, onBeforeEdit: function (_a, _b) {
                if (_4.onBeforeEdit.call(_2, _b) == false) {
                    return false;
                }
            }, onAfterEdit: function (_c, _d, _e) {
                _4.onAfterEdit.call(_2, _d, _e);
            }, onCancelEdit: function (_f, row) {
                _4.onCancelEdit.call(_2, row);
            }, onSelect: function (_10) {
                _4.onSelect.call(_2, _41(_2, _10));
            }, onUnselect: function (_11) {
                _4.onUnselect.call(_2, _41(_2, _11));
            }, onCheck: function (_12) {
                _4.onCheck.call(_2, _41(_2, _12));
            }, onUncheck: function (_13) {
                _4.onUncheck.call(_2, _41(_2, _13));
            }, onClickRow: function (_14) {
                _4.onClickRow.call(_2, _41(_2, _14));
            }, onDblClickRow: function (_15) {
                _4.onDblClickRow.call(_2, _41(_2, _15));
            }, onClickCell: function (_16, _17) {
                _4.onClickCell.call(_2, _17, _41(_2, _16));
            }, onDblClickCell: function (_18, _19) {
                _4.onDblClickCell.call(_2, _19, _41(_2, _18));
            }, onRowContextMenu: function (e, _1a) {
                _4.onContextMenu.call(_2, e, _41(_2, _1a));
            }
        }));
        if (!_4.columns) {
            var _1b = $.data(_2, "datagrid").options;
            _4.columns = _1b.columns;
            _4.frozenColumns = _1b.frozenColumns;
        }
        _3.dc = $.data(_2, "datagrid").dc;
        if (_4.pagination) {
            var _1c = $(_2).datagrid("getPager");
            _1c.pagination({
                pageNumber: _4.pageNumber, pageSize: _4.pageSize, pageList: _4.pageList, onSelectPage: function (_1d, _1e) {
                    _4.pageNumber = _1d;
                    _4.pageSize = _1e;
                    _1f(_2);
                }
            });
            _4.pageSize = _1c.pagination("options").pageSize;
        }
    };
    function _20(_21, _22) {
        var _23 = $.data(_21, "datagrid").options;
        var dc = $.data(_21, "datagrid").dc;
        if (!dc.body1.is(":empty") && (!_23.nowrap || _23.autoRowHeight)) {
            if (_22 != undefined) {
                var _24 = _25(_21, _22);
                for (var i = 0; i < _24.length; i++) {
                    _26(_24[i][_23.idField]);
                }
            }
        }
        $(_21).datagrid("fixRowHeight", _22);
        function _26(_27) {
            var tr1 = _23.finder.getTr(_21, _27, "body", 1);
            var tr2 = _23.finder.getTr(_21, _27, "body", 2);
            tr1.css("height", "");
            tr2.css("height", "");
            var _28 = Math.max(tr1.height(), tr2.height());
            tr1.css("height", _28);
            tr2.css("height", _28);
        };
    };
    function _29(_2a) {
        var dc = $.data(_2a, "datagrid").dc;
        var _2b = $.data(_2a, "treegrid").options;
        if (!_2b.rownumbers) {
            return;
        }
        dc.body1.find("div.datagrid-cell-rownumber").each(function (i) {
            $(this).html(i + 1);
        });
    };
    function _2c(_2d) {
        var dc = $.data(_2d, "datagrid").dc;
        var _2e = dc.body1.add(dc.body2);
        var _2f = ($.data(_2e[0], "events") || $._data(_2e[0], "events")).click[0].handler;
        dc.body1.add(dc.body2).bind("mouseover", function (e) {
            var tt = $(e.target);
            var tr = tt.closest("tr.datagrid-row");
            if (!tr.length) {
                return;
            }
            if (tt.hasClass("tree-hit")) {
                tt.hasClass("tree-expanded") ? tt.addClass("tree-expanded-hover") : tt.addClass("tree-collapsed-hover");
            }
            e.stopPropagation();
        }).bind("mouseout", function (e) {
            var tt = $(e.target);
            var tr = tt.closest("tr.datagrid-row");
            if (!tr.length) {
                return;
            }
            if (tt.hasClass("tree-hit")) {
                tt.hasClass("tree-expanded") ? tt.removeClass("tree-expanded-hover") : tt.removeClass("tree-collapsed-hover");
            }
            e.stopPropagation();
        }).unbind("click").bind("click", function (e) {
            var tt = $(e.target);
            var tr = tt.closest("tr.datagrid-row");
            if (!tr.length) {
                return;
            }
            if (tt.hasClass("tree-hit")) {
                _30(_2d, tr.attr("node-id"));
            } else {
                _2f(e);
            }
            e.stopPropagation();
        });
    };
    function _31(_32, _33) {
        var _34 = $.data(_32, "treegrid").options;
        var tr1 = _34.finder.getTr(_32, _33, "body", 1);
        var tr2 = _34.finder.getTr(_32, _33, "body", 2);
        var _35 = $(_32).datagrid("getColumnFields", true).length + (_34.rownumbers ? 1 : 0);
        var _36 = $(_32).datagrid("getColumnFields", false).length;
        _37(tr1, _35);
        _37(tr2, _36);
        function _37(tr, _38) {
            $("<tr class=\"treegrid-tr-tree\">" + "<td style=\"border:0px\" colspan=\"" + _38 + "\">" + "<div></div>" + "</td>" + "</tr>").insertAfter(tr);
        };
    };
    function _39(_3a, _3b, _3c, _3d) {
        var _3e = $.data(_3a, "treegrid");
        var _3f = _3e.options;
        var dc = _3e.dc;
        _3c = _3f.loadFilter.call(_3a, _3c, _3b);
        var _40 = _41(_3a, _3b);
        if (_40) {
            var _42 = _3f.finder.getTr(_3a, _3b, "body", 1);
            var _43 = _3f.finder.getTr(_3a, _3b, "body", 2);
            var cc1 = _42.next("tr.treegrid-tr-tree").children("td").children("div");
            var cc2 = _43.next("tr.treegrid-tr-tree").children("td").children("div");
            if (!_3d) {
                _40.children = [];
            }
        } else {
            var cc1 = dc.body1;
            var cc2 = dc.body2;
            if (!_3d) {
                _3e.data = [];
            }
        }
        if (!_3d) {
            cc1.empty();
            cc2.empty();
        }
        if (_3f.view.onBeforeRender) {
            _3f.view.onBeforeRender.call(_3f.view, _3a, _3b, _3c);
        }
        _3f.view.render.call(_3f.view, _3a, cc1, true);
        _3f.view.render.call(_3f.view, _3a, cc2, false);
        if (_3f.showFooter) {
            _3f.view.renderFooter.call(_3f.view, _3a, dc.footer1, true);
            _3f.view.renderFooter.call(_3f.view, _3a, dc.footer2, false);
        }
        if (_3f.view.onAfterRender) {
            _3f.view.onAfterRender.call(_3f.view, _3a);
        }
        _3f.onLoadSuccess.call(_3a, _40, _3c);
        if (!_3b && _3f.pagination) {
            var _44 = $.data(_3a, "treegrid").total;
            var _45 = $(_3a).datagrid("getPager");
            if (_45.pagination("options").total != _44) {
                _45.pagination({ total: _44 });
            }
        }
        _20(_3a);
        _29(_3a);
        $(_3a).treegrid("setSelectionState");
        $(_3a).treegrid("autoSizeColumn");
    };
    function _1f(_46, _47, _48, _49, _4a) {
        var _4b = $.data(_46, "treegrid").options;
        var _4c = $(_46).datagrid("getPanel").find("div.datagrid-body");
        if (_48) {
            _4b.queryParams = _48;
        }
        var _4d = $.extend({}, _4b.queryParams);
        if (_4b.pagination) {
            $.extend(_4d, { page: _4b.pageNumber, rows: _4b.pageSize });
        }
        if (_4b.sortName) {
            $.extend(_4d, { sort: _4b.sortName, order: _4b.sortOrder });
        }
        var row = _41(_46, _47);
        if (_4b.onBeforeLoad.call(_46, row, _4d) == false) {
            return;
        }
        var _4e = _4c.find("tr[node-id=\"" + _47 + "\"] span.tree-folder");
        _4e.addClass("tree-loading");
        $(_46).treegrid("loading");
        var _4f = _4b.loader.call(_46, _4d, function (_50) {
            _4e.removeClass("tree-loading");
            $(_46).treegrid("loaded");
            _39(_46, _47, _50, _49);
            if (_4a) {
                _4a();
            }
        }, function () {
            _4e.removeClass("tree-loading");
            $(_46).treegrid("loaded");
            _4b.onLoadError.apply(_46, arguments);
            if (_4a) {
                _4a();
            }
        });
        if (_4f == false) {
            _4e.removeClass("tree-loading");
            $(_46).treegrid("loaded");
        }
    };
    function _51(_52) {
        var _53 = _54(_52);
        if (_53.length) {
            return _53[0];
        } else {
            return null;
        }
    };
    function _54(_55) {
        return $.data(_55, "treegrid").data;
    };
    function _56(_57, _58) {
        var row = _41(_57, _58);
        if (row._parentId) {
            return _41(_57, row._parentId);
        } else {
            return null;
        }
    };
    function _25(_59, _5a) {
        var _5b = $.data(_59, "treegrid").options;
        var _5c = $(_59).datagrid("getPanel").find("div.datagrid-view2 div.datagrid-body");
        var _5d = [];
        if (_5a) {
            _5e(_5a);
        } else {
            var _5f = _54(_59);
            for (var i = 0; i < _5f.length; i++) {
                _5d.push(_5f[i]);
                _5e(_5f[i][_5b.idField]);
            }
        }
        function _5e(_60) {
            var _61 = _41(_59, _60);
            if (_61 && _61.children) {
                for (var i = 0, len = _61.children.length; i < len; i++) {
                    var _62 = _61.children[i];
                    _5d.push(_62);
                    _5e(_62[_5b.idField]);
                }
            }
        };
        return _5d;
    };
    function _63(_64, _65) {
        if (!_65) {
            return 0;
        }
        var _66 = $.data(_64, "treegrid").options;
        var _67 = $(_64).datagrid("getPanel").children("div.datagrid-view");
        var _68 = _67.find("div.datagrid-body tr[node-id=\"" + _65 + "\"]").children("td[field=\"" + _66.treeField + "\"]");
        return _68.find("span.tree-indent,span.tree-hit").length;
    };
    function _41(_69, _6a) {
        var _6b = $.data(_69, "treegrid").options;
        var _6c = $.data(_69, "treegrid").data;
        var cc = [_6c];
        while (cc.length) {
            var c = cc.shift();
            for (var i = 0; i < c.length; i++) {
                var _6d = c[i];
                if (_6d[_6b.idField] == _6a) {
                    return _6d;
                } else {
                    if (_6d["children"]) {
                        cc.push(_6d["children"]);
                    }
                }
            }
        }
        return null;
    };
    function _6e(_6f, _70) {
        var _71 = $.data(_6f, "treegrid").options;
        var row = _41(_6f, _70);
        var tr = _71.finder.getTr(_6f, _70);
        var hit = tr.find("span.tree-hit");
        if (hit.length == 0) {
            return;
        }
        if (hit.hasClass("tree-collapsed")) {
            return;
        }
        if (_71.onBeforeCollapse.call(_6f, row) == false) {
            return;
        }
        hit.removeClass("tree-expanded tree-expanded-hover").addClass("tree-collapsed");
        hit.next().removeClass("tree-folder-open");
        row.state = "closed";
        tr = tr.next("tr.treegrid-tr-tree");
        var cc = tr.children("td").children("div");
        if (_71.animate) {
            cc.slideUp("normal", function () {
                $(_6f).treegrid("autoSizeColumn");
                _20(_6f, _70);
                _71.onCollapse.call(_6f, row);
            });
        } else {
            cc.hide();
            $(_6f).treegrid("autoSizeColumn");
            _20(_6f, _70);
            _71.onCollapse.call(_6f, row);
        }
    };
    function _72(_73, _74) {
        var _75 = $.data(_73, "treegrid").options;
        var tr = _75.finder.getTr(_73, _74);
        var hit = tr.find("span.tree-hit");
        var row = _41(_73, _74);
        if (hit.length == 0) {
            return;
        }
        if (hit.hasClass("tree-expanded")) {
            return;
        }
        if (_75.onBeforeExpand.call(_73, row) == false) {
            return;
        }
        hit.removeClass("tree-collapsed tree-collapsed-hover").addClass("tree-expanded");
        hit.next().addClass("tree-folder-open");
        var _76 = tr.next("tr.treegrid-tr-tree");
        if (_76.length) {
            var cc = _76.children("td").children("div");
            _77(cc);
        } else {
            _31(_73, row[_75.idField]);
            var _76 = tr.next("tr.treegrid-tr-tree");
            var cc = _76.children("td").children("div");
            cc.hide();
            var _78 = $.extend({}, _75.queryParams || {});
            _78.id = row[_75.idField];
            _1f(_73, row[_75.idField], _78, true, function () {
                if (cc.is(":empty")) {
                    _76.remove();
                } else {
                    _77(cc);
                }
            });
        }
        function _77(cc) {
            row.state = "open";
            if (_75.animate) {
                cc.slideDown("normal", function () {
                    $(_73).treegrid("autoSizeColumn");
                    _20(_73, _74);
                    _75.onExpand.call(_73, row);
                });
            } else {
                cc.show();
                $(_73).treegrid("autoSizeColumn");
                _20(_73, _74);
                _75.onExpand.call(_73, row);
            }
        };
    };
    function _30(_79, _7a) {
        var _7b = $.data(_79, "treegrid").options;
        var tr = _7b.finder.getTr(_79, _7a);
        var hit = tr.find("span.tree-hit");
        if (hit.hasClass("tree-expanded")) {
            _6e(_79, _7a);
        } else {
            _72(_79, _7a);
        }
    };
    function _7c(_7d, _7e) {
        var _7f = $.data(_7d, "treegrid").options;
        var _80 = _25(_7d, _7e);
        if (_7e) {
            _80.unshift(_41(_7d, _7e));
        }
        for (var i = 0; i < _80.length; i++) {
            _6e(_7d, _80[i][_7f.idField]);
        }
    };
    function _81(_82, _83) {
        var _84 = $.data(_82, "treegrid").options;
        var _85 = _25(_82, _83);
        if (_83) {
            _85.unshift(_41(_82, _83));
        }
        for (var i = 0; i < _85.length; i++) {
            _72(_82, _85[i][_84.idField]);
        }
    };
    function _86(_87, _88) {
        var _89 = $.data(_87, "treegrid").options;
        var ids = [];
        var p = _56(_87, _88);
        while (p) {
            var id = p[_89.idField];
            ids.unshift(id);
            p = _56(_87, id);
        }
        for (var i = 0; i < ids.length; i++) {
            _72(_87, ids[i]);
        }
    };
    function _8a(_8b, _8c) {
        var _8d = $.data(_8b, "treegrid").options;
        if (_8c.parent) {
            var tr = _8d.finder.getTr(_8b, _8c.parent);
            if (tr.next("tr.treegrid-tr-tree").length == 0) {
                _31(_8b, _8c.parent);
            }
            var _8e = tr.children("td[field=\"" + _8d.treeField + "\"]").children("div.datagrid-cell");
            var _8f = _8e.children("span.tree-icon");
            if (_8f.hasClass("tree-file")) {
                _8f.removeClass("tree-file").addClass("tree-folder tree-folder-open");
                var hit = $("<span class=\"tree-hit tree-expanded\"></span>").insertBefore(_8f);
                if (hit.prev().length) {
                    hit.prev().remove();
                }
            }
        }
        _39(_8b, _8c.parent, _8c.data, true);
    };
    function _90(_91, _92) {
        var ref = _92.before || _92.after;
        var _93 = $.data(_91, "treegrid").options;
        var _94 = _56(_91, ref);
        _8a(_91, { parent: (_94 ? _94[_93.idField] : null), data: [_92.data] });
        _95(true);
        _95(false);
        _29(_91);
        function _95(_96) {
            var _97 = _96 ? 1 : 2;
            var tr = _93.finder.getTr(_91, _92.data[_93.idField], "body", _97);
            var _98 = tr.closest("table.datagrid-btable");
            tr = tr.parent().children();
            var _99 = _93.finder.getTr(_91, ref, "body", _97);
            if (_92.before) {
                tr.insertBefore(_99);
            } else {
                var sub = _99.next("tr.treegrid-tr-tree");
                tr.insertAfter(sub.length ? sub : _99);
            }
            _98.remove();
        };
    };
    function _9a(_9b, _9c) {
        var _9d = $.data(_9b, "treegrid");
        $(_9b).datagrid("deleteRow", _9c);
        _29(_9b);
        _9d.total -= 1;
        $(_9b).datagrid("getPager").pagination("refresh", { total: _9d.total });
    };
    $.fn.treegrid = function (_9e, _9f) {
        if (typeof _9e == "string") {
            var _a0 = $.fn.treegrid.methods[_9e];
            if (_a0) {
                return _a0(this, _9f);
            } else {
                return this.datagrid(_9e, _9f);
            }
        }
        _9e = _9e || {};
        return this.each(function () {
            var _a1 = $.data(this, "treegrid");
            if (_a1) {
                $.extend(_a1.options, _9e);
            } else {
                _a1 = $.data(this, "treegrid", { options: $.extend({}, $.fn.treegrid.defaults, $.fn.treegrid.parseOptions(this), _9e), data: [] });
            }
            _1(this);
            if (_a1.options.data) {
                $(this).treegrid("loadData", _a1.options.data);
            }
            _1f(this);
            _2c(this);
        });
    };
    $.fn.treegrid.methods = {
        options: function (jq) {
            return $.data(jq[0], "treegrid").options;
        }, resize: function (jq, _a2) {
            return jq.each(function () {
                $(this).datagrid("resize", _a2);
            });
        }, fixRowHeight: function (jq, _a3) {
            return jq.each(function () {
                _20(this, _a3);
            });
        }, loadData: function (jq, _a4) {
            return jq.each(function () {
                _39(this, _a4.parent, _a4);
            });
        }, load: function (jq, _a5) {
            return jq.each(function () {
                $(this).treegrid("options").pageNumber = 1;
                $(this).treegrid("getPager").pagination({ pageNumber: 1 });
                $(this).treegrid("reload", _a5);
            });
        }, reload: function (jq, id) {
            return jq.each(function () {
                var _a6 = $(this).treegrid("options");
                var _a7 = {};
                if (typeof id == "object") {
                    _a7 = id;
                } else {
                    _a7 = $.extend({}, _a6.queryParams);
                    _a7.id = id;
                }
                if (_a7.id) {
                    var _a8 = $(this).treegrid("find", _a7.id);
                    if (_a8.children) {
                        _a8.children.splice(0, _a8.children.length);
                    }
                    _a6.queryParams = _a7;
                    var tr = _a6.finder.getTr(this, _a7.id);
                    tr.next("tr.treegrid-tr-tree").remove();
                    tr.find("span.tree-hit").removeClass("tree-expanded tree-expanded-hover").addClass("tree-collapsed");
                    _72(this, _a7.id);
                } else {
                    _1f(this, null, _a7);
                }
            });
        }, reloadFooter: function (jq, _a9) {
            return jq.each(function () {
                var _aa = $.data(this, "treegrid").options;
                var dc = $.data(this, "datagrid").dc;
                if (_a9) {
                    $.data(this, "treegrid").footer = _a9;
                }
                if (_aa.showFooter) {
                    _aa.view.renderFooter.call(_aa.view, this, dc.footer1, true);
                    _aa.view.renderFooter.call(_aa.view, this, dc.footer2, false);
                    if (_aa.view.onAfterRender) {
                        _aa.view.onAfterRender.call(_aa.view, this);
                    }
                    $(this).treegrid("fixRowHeight");
                }
            });
        }, getData: function (jq) {
            return $.data(jq[0], "treegrid").data;
        }, getFooterRows: function (jq) {
            return $.data(jq[0], "treegrid").footer;
        }, getRoot: function (jq) {
            return _51(jq[0]);
        }, getRoots: function (jq) {
            return _54(jq[0]);
        }, getParent: function (jq, id) {
            return _56(jq[0], id);
        }, getChildren: function (jq, id) {
            return _25(jq[0], id);
        }, getLevel: function (jq, id) {
            return _63(jq[0], id);
        }, find: function (jq, id) {
            return _41(jq[0], id);
        }, isLeaf: function (jq, id) {
            var _ab = $.data(jq[0], "treegrid").options;
            var tr = _ab.finder.getTr(jq[0], id);
            var hit = tr.find("span.tree-hit");
            return hit.length == 0;
        }, select: function (jq, id) {
            return jq.each(function () {
                $(this).datagrid("selectRow", id);
            });
        }, unselect: function (jq, id) {
            return jq.each(function () {
                $(this).datagrid("unselectRow", id);
            });
        }, collapse: function (jq, id) {
            return jq.each(function () {
                _6e(this, id);
            });
        }, expand: function (jq, id) {
            return jq.each(function () {
                _72(this, id);
            });
        }, toggle: function (jq, id) {
            return jq.each(function () {
                _30(this, id);
            });
        }, collapseAll: function (jq, id) {
            return jq.each(function () {
                _7c(this, id);
            });
        }, expandAll: function (jq, id) {
            return jq.each(function () {
                _81(this, id);
            });
        }, expandTo: function (jq, id) {
            return jq.each(function () {
                _86(this, id);
            });
        }, append: function (jq, _ac) {
            return jq.each(function () {
                _8a(this, _ac);
            });
        }, insert: function (jq, _ad) {
            return jq.each(function () {
                _90(this, _ad);
            });
        }, remove: function (jq, id) {
            return jq.each(function () {
                _9a(this, id);
            });
        }, pop: function (jq, id) {
            var row = jq.treegrid("find", id);
            jq.treegrid("remove", id);
            return row;
        }, refresh: function (jq, id) {
            return jq.each(function () {
                var _ae = $.data(this, "treegrid").options;
                _ae.view.refreshRow.call(_ae.view, this, id);
            });
        }, update: function (jq, _af) {
            return jq.each(function () {
                var _b0 = $.data(this, "treegrid").options;
                _b0.view.updateRow.call(_b0.view, this, _af.id, _af.row);
            });
        }, beginEdit: function (jq, id) {
            return jq.each(function () {
                $(this).datagrid("beginEdit", id);
                $(this).treegrid("fixRowHeight", id);
            });
        }, endEdit: function (jq, id) {
            return jq.each(function () {
                $(this).datagrid("endEdit", id);
            });
        }, cancelEdit: function (jq, id) {
            return jq.each(function () {
                $(this).datagrid("cancelEdit", id);
            });
        }
    };
    $.fn.treegrid.parseOptions = function (_b1) {
        return $.extend({}, $.fn.datagrid.parseOptions(_b1), $.parser.parseOptions(_b1, ["treeField", { animate: "boolean" }]));
    };
    var _b2 = $.extend({}, $.fn.datagrid.defaults.view, {
        render: function (_b3, _b4, _b5) {
            var _b6 = $.data(_b3, "treegrid").options;
            var _b7 = $(_b3).datagrid("getColumnFields", _b5);
            var _b8 = $.data(_b3, "datagrid").rowIdPrefix;
            if (_b5) {
                if (!(_b6.rownumbers || (_b6.frozenColumns && _b6.frozenColumns.length))) {
                    return;
                }
            }
            var _b9 = 0;
            var _ba = this;
            var _bb = _bc(_b5, this.treeLevel, this.treeNodes);
            $(_b4).append(_bb.join(""));
            function _bc(_bd, _be, _bf) {
                var _c0 = ["<table class=\"datagrid-btable\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody>"];
                for (var i = 0; i < _bf.length; i++) {
                    var row = _bf[i];
                    if (row.state != "open" && row.state != "closed") {
                        row.state = "open";
                    }
                    var css = _b6.rowStyler ? _b6.rowStyler.call(_b3, row) : "";
                    var _c1 = "";
                    var _c2 = "";
                    if (typeof css == "string") {
                        _c2 = css;
                    } else {
                        if (css) {
                            _c1 = css["class"] || "";
                            _c2 = css["style"] || "";
                        }
                    }
                    var cls = "class=\"datagrid-row " + (_b9++ % 2 && _b6.striped ? "datagrid-row-alt " : " ") + _c1 + "\"";
                    var _c3 = _c2 ? "style=\"" + _c2 + "\"" : "";
                    var _c4 = _b8 + "-" + (_bd ? 1 : 2) + "-" + row[_b6.idField];
                    _c0.push("<tr id=\"" + _c4 + "\" node-id=\"" + row[_b6.idField] + "\" " + cls + " " + _c3 + ">");
                    _c0 = _c0.concat(_ba.renderRow.call(_ba, _b3, _b7, _bd, _be, row));
                    _c0.push("</tr>");
                    if (row.children && row.children.length) {
                        var tt = _bc(_bd, _be + 1, row.children);
                        var v = row.state == "closed" ? "none" : "block";
                        _c0.push("<tr class=\"treegrid-tr-tree\"><td style=\"border:0px\" colspan=" + (_b7.length + (_b6.rownumbers ? 1 : 0)) + "><div style=\"display:" + v + "\">");
                        _c0 = _c0.concat(tt);
                        _c0.push("</div></td></tr>");
                    }
                }
                _c0.push("</tbody></table>");
                return _c0;
            };
        }, renderFooter: function (_c5, _c6, _c7) {
            var _c8 = $.data(_c5, "treegrid").options;
            var _c9 = $.data(_c5, "treegrid").footer || [];
            var _ca = $(_c5).datagrid("getColumnFields", _c7);
            var _cb = ["<table class=\"datagrid-ftable\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody>"];
            for (var i = 0; i < _c9.length; i++) {
                var row = _c9[i];
                row[_c8.idField] = row[_c8.idField] || ("foot-row-id" + i);
                _cb.push("<tr class=\"datagrid-row\" node-id=\"" + row[_c8.idField] + "\">");
                _cb.push(this.renderRow.call(this, _c5, _ca, _c7, 0, row));
                _cb.push("</tr>");
            }
            _cb.push("</tbody></table>");
            $(_c6).html(_cb.join(""));
        }, renderRow: function (_cc, _cd, _ce, _cf, row) {
            var _d0 = $.data(_cc, "treegrid").options;
            var cc = [];
            if (_ce && _d0.rownumbers) {
                cc.push("<td class=\"datagrid-td-rownumber\"><div class=\"datagrid-cell-rownumber\">0</div></td>");
            }
            for (var i = 0; i < _cd.length; i++) {
                var _d1 = _cd[i];
                var col = $(_cc).datagrid("getColumnOption", _d1);
                if (col) {
                    var css = col.styler ? (col.styler(row[_d1], row) || "") : "";
                    var _d2 = "";
                    var _d3 = "";
                    if (typeof css == "string") {
                        _d3 = css;
                    } else {
                        if (cc) {
                            _d2 = css["class"] || "";
                            _d3 = css["style"] || "";
                        }
                    }
                    var cls = _d2 ? "class=\"" + _d2 + "\"" : "";
                    var _d4 = col.hidden ? "style=\"display:none;" + _d3 + "\"" : (_d3 ? "style=\"" + _d3 + "\"" : "");
                    cc.push("<td field=\"" + _d1 + "\" " + cls + " " + _d4 + ">");
                    var _d4 = "";
                    if (!col.checkbox) {
                        if (col.align) {
                            _d4 += "text-align:" + col.align + ";";
                        }
                        if (!_d0.nowrap) {
                            _d4 += "white-space:normal;height:auto;";
                        } else {
                            if (_d0.autoRowHeight) {
                                _d4 += "height:auto;";
                            }
                        }
                    }
                    cc.push("<div style=\"" + _d4 + "\" ");
                    if (col.checkbox) {
                        cc.push("class=\"datagrid-cell-check ");
                    } else {
                        cc.push("class=\"datagrid-cell " + col.cellClass);
                    }
                    cc.push("\">");
                    if (col.checkbox) {
                        if (row.checked) {
                            cc.push("<input type=\"checkbox\" checked=\"checked\"");
                        } else {
                            cc.push("<input type=\"checkbox\"");
                        }
                        cc.push(" name=\"" + _d1 + "\" value=\"" + (row[_d1] != undefined ? row[_d1] : "") + "\">");
                    } else {
                        var val = null;
                        if (col.formatter) {
                            val = col.formatter(row[_d1], row);
                        } else {
                            val = row[_d1];
                        }
                        if (_d1 == _d0.treeField) {
                            for (var j = 0; j < _cf; j++) {
                                cc.push("<span class=\"tree-indent\"></span>");
                            }
                            if (row.state == "closed") {
                                cc.push("<span class=\"tree-hit tree-collapsed\"></span>");
                                cc.push("<span class=\"tree-icon tree-folder " + (row.iconCls ? row.iconCls : "") + "\"></span>");
                            } else {
                                if (row.children && row.children.length) {
                                    cc.push("<span class=\"tree-hit tree-expanded\"></span>");
                                    cc.push("<span class=\"tree-icon tree-folder tree-folder-open " + (row.iconCls ? row.iconCls : "") + "\"></span>");
                                } else {
                                    cc.push("<span class=\"tree-indent\"></span>");
                                    cc.push("<span class=\"tree-icon tree-file " + (row.iconCls ? row.iconCls : "") + "\"></span>");
                                }
                            }
                            cc.push("<span class=\"tree-title\">" + val + "</span>");
                        } else {
                            cc.push(val);
                        }
                    }
                    cc.push("</div>");
                    cc.push("</td>");
                }
            }
            return cc.join("");
        }, refreshRow: function (_d5, id) {
            this.updateRow.call(this, _d5, id, {});
        }, updateRow: function (_d6, id, row) {
            var _d7 = $.data(_d6, "treegrid").options;
            var _d8 = $(_d6).treegrid("find", id);
            $.extend(_d8, row);
            var _d9 = $(_d6).treegrid("getLevel", id) - 1;
            var _da = _d7.rowStyler ? _d7.rowStyler.call(_d6, _d8) : "";
            function _db(_dc) {
                var _dd = $(_d6).treegrid("getColumnFields", _dc);
                var tr = _d7.finder.getTr(_d6, id, "body", (_dc ? 1 : 2));
                var _de = tr.find("div.datagrid-cell-rownumber").html();
                var _df = tr.find("div.datagrid-cell-check input[type=checkbox]").is(":checked");
                tr.html(this.renderRow(_d6, _dd, _dc, _d9, _d8));
                tr.attr("style", _da || "");
                tr.find("div.datagrid-cell-rownumber").html(_de);
                if (_df) {
                    tr.find("div.datagrid-cell-check input[type=checkbox]")._propAttr("checked", true);
                }
            };
            _db.call(this, true);
            _db.call(this, false);
            $(_d6).treegrid("fixRowHeight", id);
        }, deleteRow: function (_e0, id) {
            var _e1 = $.data(_e0, "treegrid").options;
            var tr = _e1.finder.getTr(_e0, id);
            tr.next("tr.treegrid-tr-tree").remove();
            tr.remove();
            var _e2 = del(id);
            if (_e2) {
                if (_e2.children.length == 0) {
                    tr = _e1.finder.getTr(_e0, _e2[_e1.idField]);
                    tr.next("tr.treegrid-tr-tree").remove();
                    var _e3 = tr.children("td[field=\"" + _e1.treeField + "\"]").children("div.datagrid-cell");
                    _e3.find(".tree-icon").removeClass("tree-folder").addClass("tree-file");
                    _e3.find(".tree-hit").remove();
                    $("<span class=\"tree-indent\"></span>").prependTo(_e3);
                }
            }
            function del(id) {
                var cc;
                var _e4 = $(_e0).treegrid("getParent", id);
                if (_e4) {
                    cc = _e4.children;
                } else {
                    cc = $(_e0).treegrid("getData");
                }
                for (var i = 0; i < cc.length; i++) {
                    if (cc[i][_e1.idField] == id) {
                        cc.splice(i, 1);
                        break;
                    }
                }
                return _e4;
            };
        }, onBeforeRender: function (_e5, _e6, _e7) {
            if ($.isArray(_e6)) {
                _e7 = { total: _e6.length, rows: _e6 };
                _e6 = null;
            }
            if (!_e7) {
                return false;
            }
            var _e8 = $.data(_e5, "treegrid");
            var _e9 = _e8.options;
            if (_e7.length == undefined) {
                if (_e7.footer) {
                    _e8.footer = _e7.footer;
                }
                if (_e7.total) {
                    _e8.total = _e7.total;
                }
                _e7 = this.transfer(_e5, _e6, _e7.rows);
            } else {
                function _ea(_eb, _ec) {
                    for (var i = 0; i < _eb.length; i++) {
                        var row = _eb[i];
                        row._parentId = _ec;
                        if (row.children && row.children.length) {
                            _ea(row.children, row[_e9.idField]);
                        }
                    }
                };
                _ea(_e7, _e6);
            }
            var _ed = _41(_e5, _e6);
            if (_ed) {
                if (_ed.children) {
                    _ed.children = _ed.children.concat(_e7);
                } else {
                    _ed.children = _e7;
                }
            } else {
                _e8.data = _e8.data.concat(_e7);
            }
            this.sort(_e5, _e7);
            this.treeNodes = _e7;
            this.treeLevel = $(_e5).treegrid("getLevel", _e6);
        }, sort: function (_ee, _ef) {
            var _f0 = $.data(_ee, "treegrid").options;
            if (!_f0.remoteSort && _f0.sortName) {
                var _f1 = _f0.sortName.split(",");
                var _f2 = _f0.sortOrder.split(",");
                _f3(_ef);
            }
            function _f3(_f4) {
                _f4.sort(function (r1, r2) {
                    var r = 0;
                    for (var i = 0; i < _f1.length; i++) {
                        var sn = _f1[i];
                        var so = _f2[i];
                        var col = $(_ee).treegrid("getColumnOption", sn);
                        var _f5 = col.sorter || function (a, b) {
                            return a == b ? 0 : (a > b ? 1 : -1);
                        };
                        r = _f5(r1[sn], r2[sn]) * (so == "asc" ? 1 : -1);
                        if (r != 0) {
                            return r;
                        }
                    }
                    return r;
                });
                for (var i = 0; i < _f4.length; i++) {
                    var _f6 = _f4[i].children;
                    if (_f6 && _f6.length) {
                        _f3(_f6);
                    }
                }
            };
        }, transfer: function (_f7, _f8, _f9) {
            var _fa = $.data(_f7, "treegrid").options;
            var _fb = [];
            for (var i = 0; i < _f9.length; i++) {
                _fb.push(_f9[i]);
            }
            var _fc = [];
            for (var i = 0; i < _fb.length; i++) {
                var row = _fb[i];
                if (!_f8) {
                    if (!row._parentId) {
                        _fc.push(row);
                        _fb.splice(i, 1);
                        i--;
                    }
                } else {
                    if (row._parentId == _f8) {
                        _fc.push(row);
                        _fb.splice(i, 1);
                        i--;
                    }
                }
            }
            var _fd = [];
            for (var i = 0; i < _fc.length; i++) {
                _fd.push(_fc[i]);
            }
            while (_fd.length) {
                var _fe = _fd.shift();
                for (var i = 0; i < _fb.length; i++) {
                    var row = _fb[i];
                    if (row._parentId == _fe[_fa.idField]) {
                        if (_fe.children) {
                            _fe.children.push(row);
                        } else {
                            _fe.children = [row];
                        }
                        _fd.push(row);
                        _fb.splice(i, 1);
                        i--;
                    }
                }
            }
            return _fc;
        }
    });
    $.fn.treegrid.defaults = $.extend({}, $.fn.datagrid.defaults, {
        treeField: null, animate: false, singleSelect: true, view: _b2, loader: function (_ff, _100, _101) {
            var opts = $(this).treegrid("options");
            if (!opts.url) {
                return false;
            }
            $.ajax({
                type: opts.method, url: opts.url, data: _ff, dataType: "json", success: function (data) {
                    _100(data);
                }, error: function () {
                    _101.apply(this, arguments);
                }
            });
        }, loadFilter: function (data, _102) {
            return data;
        }, finder: {
            getTr: function (_103, id, type, _104) {
                type = type || "body";
                _104 = _104 || 0;
                var dc = $.data(_103, "datagrid").dc;
                if (_104 == 0) {
                    var opts = $.data(_103, "treegrid").options;
                    var tr1 = opts.finder.getTr(_103, id, type, 1);
                    var tr2 = opts.finder.getTr(_103, id, type, 2);
                    return tr1.add(tr2);
                } else {
                    if (type == "body") {
                        var tr = $("#" + $.data(_103, "datagrid").rowIdPrefix + "-" + _104 + "-" + id);
                        if (!tr.length) {
                            tr = (_104 == 1 ? dc.body1 : dc.body2).find("tr[node-id=\"" + id + "\"]");
                        }
                        return tr;
                    } else {
                        if (type == "footer") {
                            return (_104 == 1 ? dc.footer1 : dc.footer2).find("tr[node-id=\"" + id + "\"]");
                        } else {
                            if (type == "selected") {
                                return (_104 == 1 ? dc.body1 : dc.body2).find("tr.datagrid-row-selected");
                            } else {
                                if (type == "highlight") {
                                    return (_104 == 1 ? dc.body1 : dc.body2).find("tr.datagrid-row-over");
                                } else {
                                    if (type == "checked") {
                                        return (_104 == 1 ? dc.body1 : dc.body2).find("tr.datagrid-row-checked");
                                    } else {
                                        if (type == "last") {
                                            return (_104 == 1 ? dc.body1 : dc.body2).find("tr:last[node-id]");
                                        } else {
                                            if (type == "allbody") {
                                                return (_104 == 1 ? dc.body1 : dc.body2).find("tr[node-id]");
                                            } else {
                                                if (type == "allfooter") {
                                                    return (_104 == 1 ? dc.footer1 : dc.footer2).find("tr[node-id]");
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }, getRow: function (_105, p) {
                var id = (typeof p == "object") ? p.attr("node-id") : p;
                return $(_105).treegrid("find", id);
            }, getRows: function (_106) {
                return $(_106).treegrid("getChildren");
            }
        }, onBeforeLoad: function (row, _107) {
        }, onLoadSuccess: function (row, data) {
        }, onLoadError: function () {
        }, onBeforeCollapse: function (row) {
        }, onCollapse: function (row) {
        }, onBeforeExpand: function (row) {
        }, onExpand: function (row) {
        }, onClickRow: function (row) {
        }, onDblClickRow: function (row) {
        }, onClickCell: function (_108, row) {
        }, onDblClickCell: function (_109, row) {
        }, onContextMenu: function (e, row) {
        }, onBeforeEdit: function (row) {
        }, onAfterEdit: function (row, _10a) {
        }, onCancelEdit: function (row) {
        }
    });
})(jQuery);

