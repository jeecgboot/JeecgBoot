/**
* jQuery EasyUI 1.3.6
* Copyright (c) 2009-2014 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL or commercial licenses
* To use it on other terms please contact author: info@jeasyui.com
* http://www.gnu.org/licenses/gpl.txt
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI tree Extensions 1.0 beta
* jQuery EasyUI tree 组件扩展
* jeasyui.extensions.tree.js
*
* 依赖项：
*   1、jquery.jdirk.js v1.0 beta late
*   2、jeasyui.extensions.js v1.0 beta late
*   3、jeasyui.extensions.menu.js v1.0 beta late
*   4、jeasyui.extensions.panel.js v1.0 beta late
*   5、jeasyui.extensions.window.js v1.0 beta late
*   6、jeasyui.extensions.dialog.js v1.0 beta late
*
* Copyright (c) 2013-2014 ChenJianwei personal All rights reserved.
* http://www.chenjianwei.org
*/
(function ($, undefined) {

    $.fn.tree.extensions = {};


    /******************** initExtensions Methods Begin ********************/

    function isRootNode(treeTarget, target) {
        var t = $(treeTarget);
        target = $(target)[0];
        return $.array.contains(t.tree("getRoots"), target, function (t1, t2) {
            return t1.target == t2;
        });
    };

    function showOption(treeTarget, target) {
        var t = $(treeTarget), opts, pos;
        if (target) {
            target = $(target)[0];
            opts = t.tree("getNode", target);
            pos = $(target).offset();
        } else {
            opts = t.tree("options");
            pos = t.offset();
        }
        $.extend(pos, { left: pos.left + 25, top: pos.top + 15 });
        $.easyui.showOption(opts, pos);
    };


    function getLevel(treeTarget, target) {
        var t = $(treeTarget), node = $(target);
        if (!t[0] || !node[0] || !node.is(".tree-node[node-id]") || !$.contains(t[0], node[0])) { return 0; }
        return node.parentsUntil("ul.tree", "ul").length + 1;
    };

    function moveNode(treeTarget, param) {
        if (!param || !param.source || !param.target || !param.point) { return; }
        if (!$.array.contains(["append", "top", "bottom"], param.point)) { param.point = "append"; }
        param.source = $(param.source)[0]; param.target = $(param.target)[0];
        if (param.source == param.target) { return; }
        var t = $(treeTarget), opts = t.tree("options");
        if ($.isFunction(opts.onBeforeDrop) && opts.onBeforeDrop.call(treeTarget, param.target, param.source, param.point) == false) { return; }
        if (t.tree("isParent", { target1: param.source, target2: param.target })) { return; }
        var node = t.tree("pop", param.source);
        switch (param.point) {
            case "append": t.tree("append", { parent: param.target, data: [node] }); break;
            case "top": t.tree("insert", { before: param.target, data: node }); break;
            case "bottom": t.tree("insert", { after: param.target, data: node }); break;
            default: t.tree("append", { parent: param.target, data: [node] }); break;
        }
        if (node && $.isFunction(opts.onDrop)) { opts.onDrop.call(treeTarget, param.target, param.source, param.point); }
    };

    function shiftNode(treeTarget, param) {
        if (!param || !param.target || !param.point || !$.array.contains(["up", "upLevel", "down", "downLevel"], param.point)) { return; }
        param.target = $(param.target)[0];
        var t = $(treeTarget), source = param.target, targetNode;
        switch (param.point) {
            case "up": targetNode = t.tree("prev", source); break;
            case "upLevel": targetNode = t.tree("getParent", source); break;
            case "down": targetNode = t.tree("next", source); break;
            case "downLevel": targetNode = t.tree("prev", source); break;
            default: break;
        }
        if (!targetNode) { return; }
        t.tree("move", { source: source, target: targetNode.target, point: param.point == "up" ? "top" : (param.point == "downLevel" ? "append" : "bottom") });
    };

    function compareNode(target, param) {
        if (isChild(target, param)) { return "child"; }
        if (isParent(target, param)) { return "parent"; }
        if (isSibling(target, param)) { return "sibling"; }
        return "normal";
    };

    function isChild(target, param) {
        var t = $(target),
            target1 = $(param.target1)[0], target2 = $(param.target2)[0];
        var children = t.tree("getChildren", target2);
        return $.array.some(children, function (val) { return val.target == target1; });
    };

    function isParent(target, param) {
        var t = $(target),
            target1 = $(param.target1)[0], target2 = $(param.target2)[0];
        var children = t.tree("getChildren", target1);
        return $.array.some(children, function (val) { return val.target == target2; });
    };

    function isSibling(target, param) {
        var t = $(target),
            target1 = $(param.target1)[0], target2 = $(param.target2)[0],
            p1 = t.tree("getParent", target1), p2 = t.tree("getParent", target2);
        return p1.target == p2.target;
    };

    function getNextNode(treeTarget, target) {
        var item = $(target);
        if (!item.hasClass("tree-node")) { return null; }
        var target = item[0], next = item.closest("li").next().children("div.tree-node");
        if (!next.length) { return null; }
        return next.length ? $(treeTarget).tree("getNode", next[0]) : null;
    };

    function getPrevNode(treeTarget, target) {
        var item = $(target);
        if (!item.hasClass("tree-node")) { return null; }
        var target = item[0], prev = item.closest("li").prev().children("div.tree-node");
        if (!prev.length) { return null; }
        return prev.length ? $(treeTarget).tree("getNode", prev[0]) : null;
    };

    function getNears(treeTarget, target) {
        var t = $(treeTarget); target = $(target);
        if (!$.contains(t[0], target[0]) || !target.is("div.tree-node")) { return null; }
        return target.closest("ul").find("li>div.tree-node").map(function () {
            return t.tree("getNode", this);
        });
    };

    function getNearChildren(treeTarget, target) {
        var t = $(treeTarget); target = $(target);
        if (!$.contains(t[0], target[0]) || !target.is("div.tree-node")) {
            return null;
        }
        return target.siblings("ul").find("li>div.tree-node").map(function () {
            return t.tree("getNode", this);
        });
    };

    function unselect(treeTarget, target) {
        $(target).removeClass("tree-node-selected");
    };

    function remoteLoad(target, param) {
        var t = $(target);
        if (!param) { return t.tree("reload"); }
        if (typeof param == "string") {
            t.tree("options").url = param;
            return remoteLoad(target, {});
        }
        var opts = t.tree("options"), queryParams = opts.queryParams;
        opts.queryParams = $.extend({}, queryParams, param);
        t.tree("reload");
    };

    function setNodeIcon(target, param) {
        if (!param || !param.target || !param.iconCls) { return; }
        $(target).tree("update", { target: param.target, iconCls: param.iconCls })
    };

    function setNodeText(target, param) {
        if (!param || !param.target || !param.text) { return; }
        $(target).tree("update", { target: param.target, text: param.text })
    };
    /******************** initExtensions Methods   End ********************/


    function initAutoToggle(t, opts, exts) {
        exts.onClickBak = opts.onClick;
        opts.onClick = function (node) {
            if ($.isFunction(exts.onClickBak)) { exts.onClickBak.apply(this, arguments); }
            if (opts.toggleOnClick) { t.tree("toggle", node.target); }
        };
    };

    function initContextMenu(t, opts, exts) {
        exts.onContextMenuBak = opts.onContextMenu;
        opts.onContextMenu = function (e, node) {
            if ($.isFunction(exts.onContextMenuBak)) { exts.onContextMenuBak.apply(this, arguments); }
            if (opts.selectOnContextMenu) { t.tree("select", node.target); }
            if (opts.enableContextMenu) {
                e.preventDefault();
                var items = parseContextMenuItems(t, opts, e, node);
                if (opts.autoBindDblClick && opts.dblClickMenuIndex >= 0 && $.util.likeArray(opts.contextMenu) && !$.util.isString(opts.contextMenu)
                    && opts.contextMenu.length > opts.dblClickMenuIndex) {
                    items[opts.dblClickMenuIndex].bold = true;
                }
                $.easyui.showMenu({ items: items, left: e.pageX, top: e.pageY });
            }
        };
    };

    function initBindDblClickEvent(t, opts, exts) {
        if (!$.array.likeArray(opts.contextMenu) || $.util.isString(opts.contextMenu) ||
            !opts.contextMenu.length || !opts.autoBindDblClick) { return; }
        opts.onDblClickBak = opts.onDblClick;
        opts.onDblClick = function (node) {
            if ($.isFunction(exts.onDblClickBak)) { exts.onDblClickBak.apply(this, arguments); }
            var items = parseContextMenuItems(t, opts, null, node);
            if (opts.dblClickMenuIndex >= 0 && items.length > opts.dblClickMenuIndex) {
                var item = items[opts.dblClickMenuIndex], handler = item.handler || item || onclick;
                handler(null, node, t, item, null);
            }
        };
    };


    function parseContextMenuItems(t, opts, e, node) {
        var toggle = opts.toggleMenu, move = opts.moveMenu;
        if (typeof toggle == "object") {
            toggle = $.extend({}, { expand: true, expandAll: true, collapse: true, collapseAll: true, submenu: false }, toggle);
        }
        if (typeof move == "object") {
            move = $.extend({}, { up: false, upLevel: false, down: false, downLevel: false, submenu: false }, move);
        }
        var mExpandAll = { text: "展开当前所有", iconCls: "icon-metro-expand", disabled: !(toggle == true || toggle.expandAll == true), handler: function () { t.tree("expandAll", node.target); } };
        var mExpand = { text: "展开当前", iconCls: "icon-metro-expand2", disabled: !(toggle == true || toggle.expand == true), handler: function () { t.tree("expand", node.target); } };
        var mCollapse = { text: "折叠当前", iconCls: "icon-metro-contract2", disabled: !(toggle == true || toggle.collapse == true), handler: function () { t.tree("collapse", node.target); } };
        var mCollapseAll = { text: "折叠当前所有", iconCls: "icon-metro-contract", disabled: !(toggle == true || toggle.collapseAll == true), handler: function () { t.tree("collapseAll", node.target); } };

        var mUpLevel = { text: "上移一级", iconCls: "icon-standard-arrow-up", disabled: !(move == true || move.upLevel == true), handler: function () { t.tree("shift", { point: "upLevel", target: node.target }); } };
        var mUp = { text: "上移", iconCls: "icon-standard-up", disabled: !(move == true || move.up == true), handler: function () { t.tree("shift", { point: "up", target: node.target }); } };
        var mDown = { text: "下移", iconCls: "icon-standard-down", disabled: !(move == true || move.down == true), handler: function () { t.tree("shift", { point: "down", target: node.target }); } };
        var mDownLevel = { text: "下移一级", iconCls: "icon-standard-arrow-down", disabled: !(move == true || move.downLevel == true), handler: function () { t.tree("shift", { point: "downLevel", target: node.target }); } };

        var mOpts = {
            text: "显示 Option", iconCls: "icon-standard-application-form", disabled: !opts.showOption, children: [
                { text: "树控件 Option", iconCls: "icon-hamburg-category", handler: function () { t.tree("showOption"); } },
                { text: "该节点 Option", iconCls: "tree-icon tree-file", handler: function () { t.tree("showOption", node.target); } }
            ]
        };

        var menus = [];
        var toggleMenu = [mExpandAll, mExpand, mCollapse, mCollapseAll], moveMenu = [mUpLevel, mUp, mDown, mDownLevel];
        if (t.tree("isRoot", node.target)) {
            $.array.insertRange(toggleMenu, 0, [
                { text: "展开所有", iconCls: "icon-standard-arrow-out", handler: function () { t.tree("expandAll"); } },
                { text: "折叠所有", iconCls: "icon-standard-arrow-in", handler: function () { t.tree("collapseAll"); } }, "-"
            ]);
        }
        if ($.array.likeArray(opts.contextMenu)) { $.array.merge(menus, opts.contextMenu, "-"); }
        if (opts.showOption) { $.array.merge(menus, mOpts, "-"); }
        $.array.merge(menus, typeof toggle == "object" && !toggle.submenu
                    ? $.array.merge(toggleMenu, "-") : [{ text: "展开/折叠", iconCls: "", disabled: !toggle, children: toggleMenu }, "-"]);
        $.array.merge(menus, typeof move == "object" && !move.submenu
                    ? moveMenu : { text: "上/下移(级)", iconCls: "", disabled: !move, children: moveMenu });
        return parseContextMenuMap(e, node, menus, t);
    };

    function parseContextMenuMap(e, node, menus, t) {
        return $.array.map(menus, function (value) {
            if (!value || $.util.isString(value)) { return value; }
            var ret = $.extend({}, value);
            ret.id = $.isFunction(value.id) ? value.id.call(ret, e, node, t) : value.id;
            ret.text = $.isFunction(value.text) ? value.text.call(ret, e, node, t) : value.text;
            ret.iconCls = $.isFunction(value.iconCls) ? value.iconCls.call(ret, e, node, t) : value.iconCls;
            ret.disabled = $.isFunction(value.disabled) ? value.disabled.call(ret, e, node, t) : value.disabled;
            ret.hideOnClick = $.isFunction(value.hideOnClick) ? value.hideOnClick.call(ret, e, node, t) : value.hideOnClick;
            ret.onclick = $.isFunction(value.onclick) ? function (e, item, menu) { value.onclick.call(this, e, node, t, item, menu); } : value.onclick;
            ret.handler = $.isFunction(value.handler) ? function (e, item, menu) { value.handler.call(this, e, node, t, item, menu); } : value.handler;
            if (ret.children && ret.children.length) { ret.children = parseContextMenuMap(e, node, ret.children, t); }
            return ret;
        });
    };


    /******************** initExtensions Begin ********************/
    var initExtensions = $.fn.tree.extensions.initExtensions = function (t, opts) {
        var exts = opts._extensions ? opts._extensions : opts._extensions = {};
        if (exts._initialized) { return; }
        initAutoToggle(t, opts, exts);
        initContextMenu(t, opts, exts);
        initBindDblClickEvent(t, opts, exts);
        exts._initialized = true;
    };

    var parseQueryParams = $.fn.tree.extensions.parseQueryParams = function (opts, param) {
        var ret = $.extend({}, param, opts.queryParams);
        return $.util.parseMapFunction(ret);
    };

    var loader = $.fn.tree.extensions.loader = function (param, success, error) {
        var t = $(this), opts = t.tree("options");
        initExtensions(t, opts);
        if (!opts.url) { return false; }
        param = parseQueryParams(opts, param);
        $.ajax({
            type: opts.method, url: opts.url, data: param, dataType: "json",
            success: function (data) { success(data); },
            error: function () { error.apply(this, arguments); }
        });
    };

    var _onExpand = $.fn.tree.defaults.onExpand;
    var onExpand = $.fn.tree.extensions.onExpand = function (node) {
        if ($.isFunction(_onExpand)) { _onExpand.apply(this, arguments); }
        var t = $(this), opts = t.tree("options");
        if (opts.onlyNodeExpand) {
            var nodes = t.tree("getNears", node.target), animate = opts.animate
            opts.animate = false;
            $.each($.array.filter(nodes, function (val) { return val.target != node.target && val.state == "open"; }), function () {
                t.tree("collapse", this.target);
            });
            opts.animate = animate;
        }
    };

    var _loadFilter = $.fn.tree.defaults.loadFilter;
    var loadFilter = $.fn.tree.extensions.loadFilter = function (data, parent) {
        if ($.isFunction(_loadFilter)) { data = _loadFilter.apply(this, arguments); }
        data = $.array.likeArray(data) && !$.util.isString(data) ? data : [];
        if (!data.length) { return data; }
        var t = $(this), opts = t.tree("options");
        return opts.dataPlain ? $.fn.tree.extensions.dataPlainConverter(data, opts) : data;
    };
    /******************** initExtensions   End ********************/

    $.fn.tree.extensions.dataPlainConverter = function (data, opts) {
        data = data || [];
        var ret = data, idField = opts.idField || "id", parentField = opts.parentField || "pid";
        if (opts.dataPlain) {
            var roots = $.array.filter(data, function (val) {
                if (val[parentField] == null || val[parentField] == undefined) { return true; }
                return !$.array.some(data, function (value) { return val[parentField] == value[idField]; });
            });
            var findChildren = function (array, value) {
                var temps = $.array.filter(array, function (val) {
                    return val[parentField] == null || val[parentField] == undefined ? false : val[parentField] == value[idField];
                });
                return $.array.map(temps, function (val) {
                    var children = findChildren(array, val);
                    if (children.length) {
                        val.children = $.array.likeArray(val.children) && !$.util.isString(val.children) ? val.children : [];
                        $.array.merge(val.children, children);
                    }
                    return val;
                });
            };
            ret = $.array.map(roots, function (val) {
                var children = findChildren(data, val);
                if (children.length) {
                    val.children = $.array.likeArray(val.children) && !$.util.isString(val.children) ? val.children : [];
                    $.array.merge(val.children, children);
                }
                return val;
            });
        }
        return ret;
    };



    $.fn.tree.extensions.cascadeToArray = function (data) {
        if ($.util.isEmptyObjectOrNull(data)) { return []; }
        if (!$.util.likeArrayNotString(data)) { data = [data]; }
        var ret = [], getNodeArray = function (node) {
            var tmp = $.extend({}, node), array = [tmp];
            if (!tmp.children || (tmp.children && !$.util.likeArrayNotString(tmp.children))) {
                return array;
            }
            $.each(tmp.children, function (i, n) {
                $.array.merge(array, getNodeArray(n));
            });
            tmp.children = undefined;
            return array;
        };
        $.each(data, function (i, n) {
            $.array.merge(ret, getNodeArray(n));
        });
        return ret;
    };



    var methods = $.fn.tree.extensions.methods = {
        //  扩展 easyui-tree 的自定义方法；判断制定的 tree-node 是否为根节点；该方法定义如下参数：
        //      target: 用于判断的 tree-node 的 jQuery 或 DOM 对象。
        //  返回值：如果指定的 jQuery 对象是该 easyui-tree 的根节点，则返回 true，否则返回 false。
        isRoot: function (jq, target) { return isRootNode(jq[0], target); },

        //  扩展 easyui-tree 的自定义方法；用于显示指定节点或树控件的属性信息；该方法定义如下参数：
        //      target: 要显示属性信息的 tree-node 的 jQuery 或 DOM 对象；该参数可选；如果不定义该参数，则显示树控件的属性信息；
        //  返回值：返回表示当前 easyui-tree 组件的 jQuery 对象。
        showOption: function (jq, target) { return jq.each(function () { showOption(this, target); }); },

        //  扩展 easyui-tree 的自定义方法；用于获取指定节点的级别；该方法的参数 target 表示要获取级别的 tree-node 节点的 jQuery 或 DOM 对象；
        //  返回值：如果 target 表示的 DOM 对象存在于此 easyui-tree，则返回表示其所在节点级别的数字(从 1 开始计数)，否则返回 0。
        getLevel: function (jq, target) { return getLevel(jq[0], target); },

        //  扩展 easyui-tree 的自定义方法；移动指定的节点到另一个位置；该方法定义如下参数：
        //      param:   这是一个 JSON-Object，该对象定义如下属性：
        //          target: 表示目标位置的 tree-node 的 jQuery 或 DOM 对象；
        //          source: 表示要移动的 tree-node 的 jQuery 或 DOM 对象；
        //          point:  表示移动到目标节点 target 的位置，String 类型，可选的值包括：
        //              "append":   表示追加为目标节点 target 的子节点，默认值；
        //              "top":      表示移动到目标节点 target 的上一格位置；
        //              "bottom":   表示追加为目标节点 target 的下一格位置；
        //  返回值：返回表示当前 easyui-tree 组件的 jQuery 对象。
        move: function (jq, param) { return jq.each(function () { moveNode(this, param); }); },

        //  扩展 easyui-tree 的自定义方法；移动指定节点的位置；该方法定义如下参数：
        //      param:  这是一个 JSON-Object，该对象定义如下属性：
        //          target: 表示要移动的 tree-node 的 jQuery 或 DOM 对象；
        //          point:  表示移动 target 的方式，String 类型，可选的值报错：
        //              "up":       表示将 target 所表示的 tree-node 移动到上一格位置；
        //              "upLevel":  表示将 target 所表示的 tree-node 移动到上一级的末尾；
        //              "down":     表示将 target 所表示的 tree-node 移动到下一格位置；
        //              "downLevel":表示将 target 所表示的 tree-node 移动到下一级的末尾；
        //              如果不定义该值或者该值为空或该值不是上面四个之一，则不进行任何操作；
        //  返回值：返回表示当前 easyui-tree 组件的 jQuery 对象。
        shift: function (jq, param) { return jq.each(function () { shiftNode(this, param); }); },

        //  扩展 easyui-tree 的自定义方法；判断两个 tree-node 之间的关系；该方法定义如下参数：
        //      param：  这是一个 JSON-Object，该对象定义如下属性：
        //          target1:    用于判断的第一个 tree-node 的 jQuery 或 DOM 对象；
        //          target2:    用于判断的第二个 tree-node 的 jQuery 或 DOM 对象；
        //  返回值：返回一个 String 类型的值：
        //      如果 target1 是 target2 的子节点，则返回 "child"；
        //      如果 target1 是 target2 的父节点，则返回 "parent"；
        //      如果 target1 和 target2 是具有同一个父级节点的平级节点，则返回 "sibling"；
        //      如果 target1 和 target2 既不是父子级关系，也不是具有同一个父级节点的平级节点关系，则返回 "normal"；
        compare: function (jq, param) { return compareNode(jq[0], param); },

        //  扩展 easyui-tree 的自定义方法；判断一个节点是否为另一个节点的子节点；该方法定义如下参数：
        //      param：  这是一个 JSON-Object，该对象定义如下属性：
        //          target1:    用于判断的第一个 tree-node 的 jQuery 或 DOM 对象；
        //          target2:    用于判断的第二个 tree-node 的 jQuery 或 DOM 对象；
        //  返回值：如果 tree-node target1 是 tree-node target2 的子节点，则返回 true，否则返回 false。
        isChild: function (jq, param) { return isChild(jq[0], param); },

        //  扩展 easyui-tree 的自定义方法；判断一个节点是否为另一个节点的父节点；该方法定义如下参数：
        //      param：  这是一个 JSON-Object，该对象定义如下属性：
        //          target1:    用于判断的第一个 tree-node 的 jQuery 或 DOM 对象；
        //          target2:    用于判断的第二个 tree-node 的 jQuery 或 DOM 对象；
        //  返回值：如果 tree-node target1 是 tree-node target2 的父节点，则返回 true，否则返回 false。
        isParent: function (jq, param) { return isParent(jq[0], param); },

        //  扩展 easyui-tree 的自定义方法；判断一个节点是否和另一个节点为具有同一个父节点的平级节点；该方法定义如下参数：
        //      param：  这是一个 JSON-Object，该对象定义如下属性：
        //          target1:    用于判断的第一个 tree-node 的 jQuery 或 DOM 对象；
        //          target2:    用于判断的第二个 tree-node 的 jQuery 或 DOM 对象；
        //  返回值：如果 tree-node target1 和 tree-node target2 是具有同一个父级节点的平级节点，则返回 true，否则返回 false。
        isSibling: function (jq, param) { return isSibling(jq[0], param); },

        //  扩展 easyui-tree 的自定义方法；获取指定节点的平级下一格位置的 tree-node 节点；该方法定义如下参数：
        //      target:  指定的表示 tree-node 的 jQuery 或 DOM 对象。
        //  返回值：返回 tree-node target 的同级别下一格位置的 tree-node 节点 node 对象；该 node 对象含有如下属性：
        //      id、text、iconCls、checked、state、attributes、target；
        //      如果该 tree-node target 为当前级别的最后一个节点即没有下一格节点；则返回 null。
        next: function (jq, target) { return getNextNode(jq[0], target); },

        //  扩展 easyui-tree 的自定义方法；获取指定节点的平级上一格位置的 tree-node 节点；该方法定义如下参数：
        //      target:  指定的表示 tree-node 的 jQuery 或 DOM 对象。
        //  返回值：返回 tree-node target 的同级别上一格位置的 tree-node 节点对象；该 tree-node 对象含有如下属性：
        //      id、text、iconCls、checked、state、attributes、target；
        //      如果该 tree-node target 为当前级别的第一个节点即没有上一格节点；则返回 null。
        prev: function (jq, target) { return getPrevNode(jq[0], target); },

        //  扩展 easyui-tree 的自定义方法；获取指定节点的同级所有节点(包含自身)；该方法定义如下参数：
        //      target:  指定的表示 tree-node 的 jQuery 或 DOM 对象。
        //  返回值：返回 tree-node target 的同级别(具有和当前 target 同一个父级节点)所有节点构成的一个数组对象；
        //      数组中每一个元素都是一个包含属性 id、text、iconCls、checked、state、attributes、target 的 tree-node 对象。
        //      如果传入的参数 target 是根节点或者未定义 target 参数，则该方法和 getRoots 方法返回的值相同；
        //      如果传入的参数 target 不是一个 div.tree-node 或者其不包含在当前 easyui-tree 中，则返回 null。
        getNears: function (jq, target) { return getNears(jq[0], target); },

        //  扩展 easyui-tree 的自定义方法；获取指定节点的下一级所有节点；该方法定义如下参数：
        //      target:  指定的表示 tree-node 的 jQuery 或 DOM 对象。
        //  返回值：返回 tree-node target 的下一级所有节点构成的一个数组对象；
        //      数组中每一个元素都是一个包含属性 id、text、iconCls、checked、state、attributes、target 的 tree-node 对象。
        //      如果传入的参数 target 没有子节点，则返回一个包含 0 个元素的数组。
        //      如果传入的参数 target 不是一个 div.tree-node 或者其不包含在当前 easyui-tree 中，则返回 null。
        //  备注：该方法和 getChildren 的不同之处在于，getChildren 方法返回的是 target 下的所有子节点内容；
        getNearChildren: function (jq, target) { return getNearChildren(jq[0], target); },

        //  扩展 easyui-tree 的自定义方法；用于取消指定树节点的选择状态；该方法定义如下参数：
        //      target:  指定的表示 tree-node 的 jQuery 或 DOM 对象。
        //  返回值：返回表示当前 easyui-tree 组件的 jQuery 对象。
        unselect: function (jq, target) { return jq.each(function () { unselect(this, target); }); },

        //  扩展 easyui-tree 的自定义方法；请求远程服务器地址并加载数据，并将返回的数据设置为当前 easyui-tree 的节点数据集；该方法定义如下参数：
        //      param：表示要进行远程请求的方式，该参数可以定义为以下类型：
        //          String 类型值：表示作为远程数据请求的目标 url 地址；
        //          JSON-Object 类型值：表示发送至远程服务器的查询参数；
        //      如果未定义参数 param，则相当于直接执行不带参数 { id } 的 reload 方法(reload 方法的执行默认会将指定节点的 id 作为参数发送到远程服务器地址)。
        //  返回值：返回表示当前 easyui-tree 组件的 jQuery 对象。
        load: function (jq, param) { return jq.each(function () { remoteLoad(this, param); }); },

        //  扩展 easyui-tree 的自定义方法；设置指定节点的图标；该方法定义如下参数：
        //      param: JSON-Object 类型值，该对象包含如下属性定义：
        //          target: 表示要设置图标的 easyui-tree node HTML-DOM 对象；
        //          iconCls:表示要设置的节点样式；
        //  返回值：返回表示当前 easyui-tree 组件的 jQuery 对象。
        setIcon: function (jq, param) { return jq.each(function () { setNodeIcon(this, param); }); },

        //  扩展 easyui-tree 的自定义方法；设置指定节点的显示文本；该方法定义如下参数：
        //      param: JSON-Object 类型值，该对象包含如下属性定义：
        //          target: 表示要设置图标的 easyui-tree node HTML-DOM 对象；
        //          text  : 表示要设置的显示文本值；
        //  返回值：返回表示当前 easyui-tree 组件的 jQuery 对象。
        setText: function (jq, param) { return jq.each(function () { setNodeText(this, param); }); }
    };
    var defaults = $.fn.tree.extensions.defaults = {

        //  增加 easyui-tree 的自定义扩展属性；
        //      该属性表示当设定了属性 contextMenu 时，是否将双击数据行 onDblClick 事件的响应函数
        //      设置为 contextMenu 的第一个菜单项的点击响应函数，并将该菜单项的字体加粗；
        //  Boolean 类型值，默认为 true；
        //  备注：当设置了自定义属性 contextMenu 时候，该功能方有效。
        //      自动绑定的 onClick 的回调函数中将会调用 contextMenu 的第 "dblClickMenuIndex" 个菜单项的点击响应函数，但是回调函数中不能用到参数 e 和 menu。
        autoBindDblClick: true,

        //  增加 easyui-tree 的自定义扩展属性；
        //  该属性表示当设定了属性 autoBindDblClick: true，双击行数据触发的右键菜单项事件的索引号；
        //      意即触发第几个右键菜单项上的事件。
        //  Number 类型值，从 0 开始计数，默认为 0；
        //  备注：当设置了自定义属性 autoBindDblClick: true 时，该功能方有效；如果此索引值超出菜单数量范围，则无效。
        dblClickMenuIndex: 0,

        //  扩展 easyui-tree 的自定义属性，表示当前 easyui-tree 控件是否支持平滑数据格式。
        //  当支持平滑数据格式时，数据元素中不需要通过指定 children 来指定子节点，而是支持通过 pid 属性来指示其父级节点。
        //  Boolean 类型值，默认为 false。
        dataPlain: false,

        //  扩展 easyui-treegrid 的自定义属性，表示当前 easyui-treeg 控件支持平滑数据格式时，程序用哪个 field 表示当前行数据的父级节点 idField 值
        //  String 类型值，默认为 "pid"。
        parentField: "pid",

        //  扩展 easyui-tree 的自定义属性，表示当右键点击 tree-node 时，是否自动选择被点击的 tree-node 对象；
        //  Boolean 类型值，默认为 false；
        selectOnContextMenu: false,

        //  扩展 easyui-tree 的自定义属性，表示当左键点击带有子节点的条目时，是否自动展开/折叠相应节点。
        //  Boolean 类型，默认为 false。
        //  备注：该功能不会影响到 easyui-tree 的原生事件 onClick。
        toggleOnClick: false,

        //  扩展 easyui-tree 的自定义属性，表示同一级菜单节点下，只允许一个节点被展开。
        //  Boolean 类型，默认为 false。
        //  当该属性设置为 true 时，建议同时把 animate 属性设置为 false，以免影响菜单联动折叠时的美观效果。
        onlyNodeExpand: false,

        //  扩展 easyui-tree 的自定义属性，表示是否启用当前 easyui-tree 组件的右键菜单。
        //  Boolean 类型，默认为 true。
        //  备注：该功能不会影响到 easyui-tree 的原生事件 onContextMenu。
        enableContextMenu: true,

        //  扩展 easyui-tree 的自定义属性，表示当前 easyui-tree 的右键菜单；
        //  这是一个数组类型，数组中的每一个元素都是一个 JSON-Object，该 JSON-Object 定义如下属性：
        //      id:         表示菜单项的 id；
        //      text:       表示菜单项的显示文本；
        //      iconCls:    表示菜单项的左侧显示图标；
        //      disabled:   表示菜单项是否被禁用(禁用的菜单项点击无效)；
        //      hideOnClick:    表示该菜单项点击后整个右键菜单是否立即自动隐藏；
        //      bold:           Boolean 类型值，默认为 false；表示该菜单项是否字体加粗；
        //      style:          JSON-Object 类型值，默认为 null；表示要附加到该菜单项的样式；
        //      handler:    表示菜单项的点击事件，该事件函数格式为 function(e, node, tree, item, menu)，其中 this 指向菜单项本身
        //  备注：当 enableContextMenu 属性设置为 true 时，该属性才有效。
        //  备注：该功能不会影响到 easyui-tree 的原生事件 onContextMenu。
        contextMenu: null,

        //  扩展 easyui-tree 的自定义属性，表示是否启用右键菜单中的“展开当前、折叠当前、展开当前所有、折叠当前所有”菜单项的功能；
        //  该属性可以定义为以下类型：
        //      Boolean 类型，表示是否启用这四个菜单项；
        //      JSON-Object 类型，该 JSON-Object 可以包含如下属性：
        //          expand:     布尔类型的值，也可是一个返回布尔值的函数，表示是否显示“展开当前”菜单；
        //          expandAll:  布尔类型的值，也可是一个返回布尔值的函数，表示是否显示“展开当前所有”菜单；
        //          collapse:   布尔类型的值，也可是一个返回布尔值的函数，表示是否显示“折叠当前”菜单；
        //          collapseAll: 布尔类型的值，也可是一个返回布尔值的函数，表示是否显示“折叠当前所有”菜单；
        //          submenu:    表示这四个菜单项是否以子菜单方式呈现，默认为 true；
        //          上面四个属性，如果参数的值为函数，则函数的签名为 function(e, node, tree, item, menu)。
        //  备注：当 enableContextMenu 属性设置为 true 时，该属性才有效。
        //      这四个菜单点击时，会自动触发 easyui-tree 的折叠/展开菜单项的相应事件。
        toggleMenu: true,

        //  扩展 easyui-tree 的自定义属性，表示是否启用右键菜单中的“上移、下移、上移一级、下移一级”菜单项的功能；
        //  备注：当 enableContextMenu 属性设置为 true 时，该属性才有效。
        //  该属性可以定义为以下类型：
        //      Boolean 类型，表示是否启用这四个菜单项，默认为 false；
        //      JSON-Object 类型，该 JSON-Object 可以包含如下属性：
        //          up:         布尔类型的值，也可是一个返回布尔值的函数，表示是否显示“上移”菜单；
        //          upLevel:    布尔类型的值，也可是一个返回布尔值的函数，表示是否显示“上移一级”菜单；
        //          down:       布尔类型的值，也可是一个返回布尔值的函数，表示是否显示“下移”菜单；
        //          downLevel:  布尔类型的值，也可是一个返回布尔值的函数，表示是否显示“下移一级”菜单；
        //          submenu:    表示这四个菜单项是否以子菜单方式呈现，默认为 true；
        //          上面四个属性，如果参数的值为函数，则函数的签名为 function(e, node, tree, item, menu)。
        //      这四个菜单点击时，会自动触发 easyui-tree 的 onDrop 事件。
        moveMenu: false,

        //  扩展 easyui-tree 的自定义属性，该属性表示在启用右键菜单的情况下，右键菜单项中是否显示 "显示 Tree 的 Option" 菜单项
        //  Boolean 类型值；默认为 false。
        showOption: false,

        //  表示当执行远程请求获取数据时，被一并发送到服务器的查询参数，参考 easyui-datagrid 中的 queryParams 属性定义；
        //  这是一个 JSON-Object 类型参数对象，其中每一个属性的值可以是值类型，也可以是返回值的函数。
        queryParams: {},

        //  覆盖 easyui-tree 的原生属性 loader，以支持相应扩展功能。
        loader: loader,

        //  覆盖 easyui-tree 的原生属性 loadFilter，以支持相应扩展功能(支持平滑数据格式)。
        loadFilter: loadFilter,

        //  覆盖 easyui-tree 的原生事件 onExpand，以支持相应扩展功能。
        onExpand: onExpand
    };

    $.extend($.fn.tree.defaults, defaults);
    $.extend($.fn.tree.methods, methods);

})(jQuery);