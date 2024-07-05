var dg, d, pwd, config, tableName, type, rowtype, colums = "",
    searchBG, databaseName, add, primary_key, saveSearch, selectRowCount = 0,
    heightStr = 300,
    sqlArray = [],
    columnsTemp = [],
    index = 0,
    messTemp = "",
    isAdd = !1,
    databaseConfigId;

function imgerror(a) {
    a.src = baseUrl + "/static/images/logo.png";
    a.onerror = null
}
$(function () {
    initSqlStudyTree();
    init3()
});

function init3() {
    $.ajax({
        type: "get",
        url: baseUrl + "/system/permission/i/allDatabaseList",
        success: function (a) {
            $("#databaseSelect").empty();
           // a = JSON.parse(a);
            $.each(a, function (b, c) {
                $("#databaseSelect").append("<option value='" + a[b].id + "' title='" + a[b].ip + ":" + a[b].port + "'   >\u3010" + a[b].databaseType + "\u3011" + a[b].name + " </option>")
            });
            databaseName = a[0].databaseName;
            $("#currentDbTitle").html(databaseName);
            initDataBase()
        }
    })
}
$("#databaseSelect").change(function () {
    databaseConfigId = $("#databaseSelect").val();
    initDataBase();
    $("#executeMessage").val("");
    executeMessage.setValue("");
    sql_autocomplete = !1;
    getDataBaseConfig()
});

function getDataBaseConfig() {
    $.ajax({
        type: "get",
        url: baseUrl + "/system/permission/i/getDataBaseConfig/" + databaseConfigId,
        success: function (a) {
            databaseName = a.databaseName;
            $("#currentDbTitle").html(databaseName)
        }
    })
}

function initDataBase() {
    databaseConfigId = $("#databaseSelect").val();
    dg = $("#pid").treegrid({
        method: "GET",
        url: baseUrl + "/system/permission/i/databaseList/" + databaseConfigId,
        fit: !0,
        fitColumns: !0,
        border: !1,
        idField: "id",
        treeField: "name",
        parentField: "pid",
        iconCls: "icon",
        animate: !0,
        rownumbers: !1,
        singleSelect: !0,
        striped: !0,
        columns: [
            [{
                field: "name",
                title: "&nbsp;&nbsp;\u8be6\u60c5",
                width: 210
            }]
        ],
        enableHeaderClickMenu: !1,
        enableHeaderContextMenu: !1,
        enableRowContextMenu: !1,
        onContextMenu: onContextMenu,
        dataPlain: !1,
        onClickRow: function (a) {
            tableName = a.name;
            type = a.type;
            columnsTemp.length = 0;
            $("#searchHistoryId").val("");
            $("#searchHistoryName").val("");
            $("#pid").treegrid("getRoot", a.id);
            "db" == type && (databaseName = a.name, $("#currentDbTitle").html(databaseName));
            "direct" == type && (databaseName = $("#pid").treegrid("getParent", a.id).name, $("#currentDbTitle").html(databaseName));
            "table" == type && (a = $("#pid").treegrid("getParent", a.id), databaseName = a = $("#pid").treegrid("getParent", a.id).name, $("#currentDbTitle").html(databaseName),
                selectRowCount = 0, $("#currentTableName").val(tableName), clickTable(tableName));
            "view" == type && (selectRowCount = 0, openView(tableName))
        }
    })
}

function designTable() {
    parent.window.mainpage.mainTabs.addModule("\u8bbe\u8ba1" + tableName + " @" + databaseName, baseUrl + "/system/permission/i/designTable/" + tableName + "/" + databaseName + "/" + databaseConfigId, "icon-hamburg-config")
}

function addNewTable() {
    parent.window.mainpage.mainTabs.addModule("\u65b0\u589e\u8868", baseUrl + "/system/permission/i/addNewTable/" + databaseName + "/" + databaseConfigId, "icon-hamburg-config")
}

function exportTable() {
    $.easyui.loading({
        msg: "\u5bfc\u51fa\u4e2d\uff0c\u8bf7\u7a0d\u7b49\uff01"
    });
    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: baseUrl + "/system/permission/i/exportTable/" + databaseConfigId,
        data: JSON.stringify({
            databaseName: databaseName,
            tableName: tableName
        }),
        datatype: "json",
        success: function (a) {
            var b = a.status;
            $.easyui.loaded();
            "success" == b ? (parent.$.messager.show({
                title: "\u63d0\u793a",
                msg: a.mess,
                position: "bottomRight"
            }), window.mainpage.mainTabs.addModule("\u5907\u4efd/\u5bfc\u51fa",
                baseUrl + "/system/permission/i/backupDatabase/" + databaseName + "/" + databaseConfigId, "icon-berlin-calendar"), $("#dg3").datagrid("reload")) : parent.$.messager.show({
                title: "\u63d0\u793a",
                msg: a.mess,
                position: "bottomRight"
            })
        }
    })
}

function copyTable() {
    $.easyui.loading({
        msg: "\u590d\u5236\u4e2d\uff0c\u8bf7\u7a0d\u7b49\uff01"
    });
    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: baseUrl + "/system/permission/i/copyTable/" + databaseConfigId,
        data: JSON.stringify({
            databaseName: databaseName,
            tableName: tableName
        }),
        datatype: "json",
        success: function (a) {
            var b = a.status;
            $.easyui.loaded();
            "success" == b ? (parent.$.messager.show({
                title: "\u63d0\u793a",
                msg: a.mess,
                position: "bottomRight"
            }), $("#pid").treegrid("reload")) : parent.$.messager.show({
                title: "\u63d0\u793a",
                msg: a.mess,
                position: "bottomRight"
            })
        }
    })
}

function renameTable() {
    $.easyui.messager.prompt("\u63d0\u793a", "\u65b0\u8868\u540d :", function (a) {
        a && ($.easyui.loading({
            msg: "\u64cd\u4f5c\u4e2d\uff0c\u8bf7\u7a0d\u7b49\uff01"
        }), $.ajax({
            type: "POST",
            contentType: "application/json;charset=utf-8",
            url: baseUrl + "/system/permission/i/renameTable/" + databaseConfigId,
            data: JSON.stringify({
                databaseName: databaseName,
                tableName: tableName,
                newTableName: a
            }),
            datatype: "json",
            success: function (a) {
                var b = a.status;
                $.easyui.loaded();
                "success" == b ? (parent.$.messager.show({
                    title: "\u63d0\u793a",
                    msg: a.mess,
                    position: "bottomRight"
                }), $("#pid").treegrid("reload")) : parent.$.messager.show({
                    title: "\u63d0\u793a",
                    msg: a.mess,
                    position: "bottomRight"
                })
            }
        }))
    })
}

function dropTable() {
    $.easyui.messager.confirm("\u64cd\u4f5c\u63d0\u9192", "\u60a8\u786e\u5b9a\u8981\u5220\u9664\u8868 " + tableName + "\u5417\uff1f", function (a) {
        a && $.ajax({
            type: "POST",
            contentType: "application/json;charset=utf-8",
            url: baseUrl + "/system/permission/i/dropTable/" + databaseConfigId,
            data: JSON.stringify({
                databaseName: databaseName,
                tableName: tableName
            }),
            datatype: "json",
            success: function (a) {
                $("#pid").treegrid("reload");
                parent.$.messager.show({
                    title: "\u63d0\u793a",
                    msg: a.mess,
                    position: "bottomRight"
                })
            }
        })
    })
}

function dropDatabase() {
    $.easyui.messager.confirm("\u64cd\u4f5c\u63d0\u9192", "\u60a8\u786e\u5b9a\u8981\u5220\u9664\u6570\u636e\u5e93 " + databaseName + " \u5417\uff1f", function (a) {
        a && $.ajax({
            type: "POST",
            contentType: "application/json;charset=utf-8",
            url: baseUrl + "/system/permission/i/dropDatabase/" + databaseConfigId,
            data: JSON.stringify({
                databaseName: databaseName
            }),
            datatype: "json",
            success: function (a) {
                $("#pid").treegrid("reload");
                parent.$.messager.show({
                    title: "\u63d0\u793a",
                    msg: a.mess,
                    position: "bottomRight"
                })
            }
        })
    })
}

function clearTable() {
    $.easyui.messager.confirm("\u64cd\u4f5c\u63d0\u9192", "\u60a8\u786e\u5b9a\u8981\u6e05\u7a7a\u8868 " + tableName + "\u5417\uff1f", function (a) {
        a && $.ajax({
            type: "POST",
            contentType: "application/json;charset=utf-8",
            url: baseUrl + "/system/permission/i/clearTable/" + databaseConfigId,
            data: JSON.stringify({
                databaseName: databaseName,
                tableName: tableName
            }),
            datatype: "json",
            success: function (a) {
                parent.$.messager.show({
                    title: "\u63d0\u793a",
                    msg: a.mess,
                    position: "bottomRight"
                })
            }
        })
    })
}

function tableMess() {
    window.mainpage.mainTabs.addModule(tableName + "\u4fe1\u606f", baseUrl + "/system/permission/i/showTableMess/" + tableName + "/" + databaseName + "/" + databaseConfigId, "icon-berlin-calendar")
}

function onContextMenu(a, b) {
    a.preventDefault();
    $(this).treegrid("select", b.id);
    var c = b.name,
        e = b.type;
    "db" == e && (databaseName = b.name, $("#currentDbTitle").html(databaseName), $("#databaseMenu").menu("show", {
        left: a.pageX,
        top: a.pageY
    }));
    "direct" == e && (databaseName = $("#pid").treegrid("getParent", b.id).name, $("#currentDbTitle").html(databaseName));
    if ("table" == e) {
        $("#tableMenu").menu("show", {
            left: a.pageX,
            top: a.pageY
        });
        tableName = c;
        var f = $("#pid").treegrid("getParent", b.id);
        f = $("#pid").treegrid("getParent", f.id);
        databaseName = f.name;
        $("#currentDbTitle").html(databaseName)
    }
    "view" == e && ($("#viewMenu").menu("show", {
        left: a.pageX,
        top: a.pageY
    }), tableName = c, f = $("#pid").treegrid("getParent", b.id), f = $("#pid").treegrid("getParent", f.id), databaseName = f.name, $("#currentDbTitle").html(databaseName))
}

function initSqlStudyTree() {
    $("#sqlStudyList").treegrid({
        method: "GET",
        url: baseUrl + "/system/permission/i/selectSqlStudy",
        fit: !0,
        fitColumns: !0,
        border: !1,
        idField: "id",
        treeField: "title",
        parentField: "pid",
        iconCls: "icon",
        animate: !0,
        rownumbers: !1,
        singleSelect: !0,
        striped: !0,
        columns: [
            [{
                field: "title",
                title: "&nbsp;&nbsp;\u8be6\u60c5",
                width: 210
            }]
        ],
        enableHeaderClickMenu: !1,
        enableHeaderContextMenu: !1,
        enableRowContextMenu: !0,
        dataPlain: !1,
        onClickRow: function (a) {
            $("#mainTabs").tabs("select", 0);
            a = a.content;
            var b = $("#sqltextarea").val();
            $("#sqltextarea").val(b + "\n" + a);
            editor.setValue(b + "\n" + a);
            $("#searchHistoryId").val("");
            $("#searchHistoryName").val("")
        }
    })
}

function clickTable(a) {
    window.mainpage.mainTabs.addModule(a + " @" + databaseName, baseUrl + "/system/permission/i/showTableData/" + a + "/" + databaseName + "/" + databaseConfigId + "/table", "icon-berlin-calendar")
}

function openView(a) {
    window.mainpage.mainTabs.addModule(a + " @" + databaseName, baseUrl + "/system/permission/i/showTableData/" + a + "/" + databaseName + "/" + databaseConfigId + "/view", "icon-berlin-calendar")
}

function jsonFormat() {
    window.mainpage.mainTabs.addModule("Json\u683c\u5f0f\u5316", baseUrl + "/system/permission/i/jsonFormat", "icon-hamburg-sitemap")
}

function backupDatabase() {
    window.mainpage.mainTabs.addModule("\u5907\u4efd", baseUrl + "/system/permission/i/backupDatabase/" + databaseName + "/" + databaseConfigId, "icon-hamburg-database")
}

function ShowConfigPage() {
    window.mainpage.mainTabs.addModule("\u6570\u636e\u5e93\u914d\u7f6e", baseUrl + "/system/permission/i/config", "icon-hamburg-config")
}

function monitor() {
    window.mainpage.mainTabs.addModule("\u72b6\u6001\u76d1\u63a7", baseUrl + "/system/permission/i/monitor/" + databaseName + "/" + databaseConfigId, "icon-hamburg-equalizer")
}

function dataSynchronize() {
    window.mainpage.mainTabs.addModule("\u6570\u636e\u4ea4\u6362", baseUrl + "/system/permission/i/dataSynchronize/", "icon-hamburg-exchange")
}

function task() {
    window.mainpage.mainTabs.addModule("\u5b9a\u65f6\u4efb\u52a1", baseUrl + "/system/task/i/task/", "icon-hamburg-future")
}

function showSQL(a, b) {
    a = "select * from " + a + ";";
    b = editor.getValue();
    $("#sqltextarea").val(b + "\n" + a);
    editor.setValue(b + "\n" + a);
    $("#executeMessage").html("");
    executeMessage.setValue("")
}

function showViewSQL(a, b) {
    $("#mainTabs").tabs("select", 0);
    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: baseUrl + "/system/permission/i/getViewSql/" + databaseConfigId,
        data: JSON.stringify({
            databaseName: a,
            tableName: b
        }),
        datatype: "json",
        success: function (a) {
            if ("success" == a.status) {
                selectRowCount = 0;
                var b = $("#sqltextarea").val();
                $("#sqltextarea").val(b + "\n" + a.viewSql);
                editor.setValue(b + "\n" + a.viewSql)
            } else parent.$.messager.show({
                title: "\u63d0\u793a",
                msg: a.mess,
                position: "bottomRight"
            })
        }
    })
}

function run() {
    var a = $.trim(editor.getValue()),
        b = editor.getSelection();
    "" != b && (a = b);
    var c = [];
    messTemp = "";
    if ("" == a || null == a) parent.$.messager.show({
        title: "\u63d0\u793a",
        msg: "\u4eb2\uff0c\u8bf7\u8f93\u5165SQL\u8bed\u53e5\uff01"
    });
    else if ("" == databaseName || null == databaseName) parent.$.messager.show({
        title: "\u63d0\u793a",
        msg: "\u4eb2\uff0c\u8bf7\u70b9\u51fb\u5de6\u4fa7\u6307\u5b9a\u6570\u636e\u5e93\uff01"
    });
    else {
        b = [];
        var e = a.replace(" ", "");
        0 == e.indexOf("createfunction") || 0 == e.indexOf("CREATEFUNCTION") ||
            0 == e.indexOf("createprocedure") || 0 == e.indexOf("CREATEPROCEDURE") || 0 == e.indexOf("DELIMITER") ? b.push(a) : b = a.split(";");
        for (e = 0; e < b.length; e++)
            if (a = $.trim(b[e]), 0 != a.indexOf("--") && 0 != a.indexOf("/*") && "" != a && null != a) {
                if (0 <= a.indexOf("update ") || 0 <= a.indexOf("UPDATE ") || 0 <= a.indexOf("delete ") || 0 <= a.indexOf("DELETE ") || 0 <= a.indexOf("drop ") || 0 <= a.indexOf("DROP ") || 0 <= a.indexOf("truncate ")) var f = !0;
                c.push(a)
            }
        $.cookie("noCaution") && (f = !1);
        f ? ($.easyui.messager.defaults = {
                ok: "\u786e\u8ba4",
                cancel: "\u4e0d\u518d\u63d0\u793a"
            },
            $.easyui.messager.confirm("\u64cd\u4f5c\u63d0\u9192", "SQL\u8bed\u53e5\u4e2d\u5305\u542bupdate\u3001delete\u3001drop\u7b49\u64cd\u4f5c\uff0c\u60a8\u786e\u5b9a\u6267\u884c\u5417\uff1f", function (a) {
                a ? (window.mainpage.searchTabs.closeAllTabs(), executeSQLArray(c, index)) : $.cookie("noCaution", !0, {
                    expires: 30,
                    path: "/"
                })
            }), $.easyui.messager.defaults = {
                ok: "\u786e\u8ba4",
                cancel: "\u53d6\u6d88"
            }) : (window.mainpage.searchTabs.closeAllTabs(), executeSQLArray(c, index))
    }
}

function saveSearchDialog() {
    saveSearch = $("#dlgg").dialog({
        title: "\u4fdd\u5b58\u67e5\u8be2",
        width: 380,
        height: 160,
        href: baseUrl + "/system/permission/i/searchHistory",
        maximizable: !0,
        modal: !0,
        buttons: [{
            text: "\u4fdd\u5b58",
            iconCls: "icon-ok",
            handler: function () {
                saveSearchHistory2()
            }
        }, {
            text: "\u53d6\u6d88",
            iconCls: "icon-cancel",
            handler: function () {
                saveSearch.panel("close")
            }
        }]
    })
}

function deleteSearchHistory(a) {
    $.easyui.messager.confirm("\u64cd\u4f5c\u63d0\u9192", "\u60a8\u786e\u5b9a\u8981\u5220\u9664\uff1f", function (b) {
        b && $.ajax({
            type: "POST",
            contentType: "application/json;charset=utf-8",
            url: baseUrl + "/system/permission/i/deleteSearchHistory",
            data: JSON.stringify({
                id: a
            }),
            datatype: "json",
            success: function (a) {
                searchBG.treegrid("reload");
                parent.$.messager.show({
                    title: "\u63d0\u793a",
                    msg: a.mess,
                    position: "bottomRight"
                })
            }
        })
    })
}
selectSearchHistory2();

function selectSearchHistory2() {
    searchBG = $("#searchHistoryList").treegrid({
        method: "GET",
        url: baseUrl + "/system/permission/i/selectSearchHistory",
        fit: !0,
        fitColumns: !0,
        border: !1,
        idField: "id",
        treeField: "name",
        parentField: "pid",
        iconCls: "icon",
        animate: !0,
        rownumbers: !1,
        singleSelect: !0,
        striped: !0,
        columns: [
            [{
                field: "sqls",
                title: "sqls",
                hidden: !0,
                width: 0
            }, {
                field: "database",
                title: "database",
                hidden: !0,
                width: 0
            }, {
                field: "name",
                title: "&nbsp;&nbsp;\u8be6\u60c5",
                width: 210
            }, {
                field: "_opt",
                title: "&nbsp;\u64cd\u4f5c",
                width: 30,
                formatter: function (a, b, c) {
                    return '<a href="javascript:deleteSearchHistory(\'' + b.id + '\')"><div class="icon-remove" style="width:16px;height:16px" title="delete"></div></a>'
                }
            }]
        ],
        enableHeaderClickMenu: !1,
        enableHeaderContextMenu: !1,
        enableRowContextMenu: !1,
        dataPlain: !1,
        onClickRow: function (a) {
            var b = editor.getValue();
            editor.setValue(b + "\n" + a.sqls);
            $("#searchHistoryId").val(a.id);
            $("#searchHistoryName").val(a.name)
        }
    })
}

function ShowPasswordDialog() {
    pwd = $("#dlgg").dialog({
        title: "\u4fee\u6539\u5bc6\u7801",
        width: 380,
        height: 160,
        href: baseUrl + "/system/permission/i/changePass",
        maximizable: !0,
        modal: !0,
        buttons: [{
            text: "\u4fdd\u5b58",
            iconCls: "icon-ok",
            handler: function () {
                changePass()
            }
        }, {
            text: "\u53d6\u6d88",
            iconCls: "icon-cancel",
            handler: function () {
                pwd.panel("close")
            }
        }]
    })
}

function refresh() {
    d.datagrid("reload")
}

function exportExcel4() {
    var a = $("#dg").datagrid("getRows");
    if (0 == a.length) parent.$.messager.show({
        title: "\u63d0\u793a",
        msg: "\u6ca1\u6709\u6570\u636e\u53ef\u4f9b\u5bfc\u51fa\uff01",
        position: "bottomRight"
    });
    else {
        for (var b = [], c = $("#dg").datagrid("getColumnFields"), e = 0; e < c.length; e++) {
            var f = $("#dg").datagrid("getColumnOption", c[e]),
                g = {};
            g.field = f.field;
            g.title = f.title;
            b.push(g)
        }
        c = {};
        c.rows = a;
        c.columns = b;
        c.sheetName = "TreeSoft Export data to Excel";
        $("#sContent").val(JSON.stringify(c));
        $("#form1").submit()
    }
}

function clearSQL() {
    $("#sqltextarea").val("");
    $("#executeMessage").val("");
    $("#searchHistoryId").val("");
    $("#searchHistoryName").val("");
    editor.setValue("");
    executeMessage.setValue("")
}

function contribute() {
    $("#addRow").dialog({
        title: "\u6350\u8d60",
        width: 480,
        height: 500,
        href: baseUrl + "/system/permission/i/contribute",
        maximizable: !0,
        modal: !0,
        buttons: [{
            text: "\u5173\u95ed",
            iconCls: "icon-cancel",
            handler: function () {
                $("#addRow").panel("close")
            }
        }]
    })
}

function persons() {
    window.mainpage.mainTabs.addModule("\u7528\u6237\u4fe1\u606f", baseUrl + "/system/person/i/person", "icon-hamburg-user")
}

function help() {
    $("#addRow").dialog({
        title: "\u5e2e\u52a9",
        width: 500,
        height: 600,
        href: baseUrl + "/system/permission/i/help",
        maximizable: !0,
        modal: !0,
        buttons: [{
            text: "\u5173\u95ed",
            iconCls: "icon-cancel",
            handler: function () {
                $("#addRow").panel("close")
            }
        }]
    })
}
var sql_autocomplete_in_progress = !1,
    sql_autocomplete = !1,
    editor = CodeMirror.fromTextArea(document.getElementById("sqltextarea"), {
        lineNumbers: !0,
        matchBrackets: !0,
        hintOptions: {
            completeSingle: !1,
            completeOnSingleClick: !0
        },
        indentUnit: 4,
        mode: "text/x-mysql",
        lineWrapping: !0,
        theme: "eclipse",
        autofocus: !0,
        extraKeys: {
            ctrl: "autocomplete",
            F7: function () {
                clearSQL()
            }, F8: function () {
                run()
            }
        }
    });
editor.on("inputRead", codemirrorAutocompleteOnInputRead);
editor.refresh();
editor.focus();
$(editor.getWrapperElement()).bind("keydown", catchKeypressesFromSqlTextboxes);

function codemirrorAutocompleteOnInputRead(a) {
    sql_autocomplete_in_progress || a.options.hintOptions.tables && sql_autocomplete || (sql_autocomplete ? (a.options.hintOptions.tables = sql_autocomplete, a.options.hintOptions.defaultTable = sql_autocomplete_default_table) : (a.options.hintOptions.defaultTable = "", sql_autocomplete_in_progress = !0, $.ajax({
        type: "GET",
        url: baseUrl + "/system/permission/i/allTableAndColumn/" + databaseName + "/" + databaseConfigId,
        success: function (b) {
            sql_autocomplete = [];
            a.options.hintOptions.tables =
                b
        }, complete: function () {
            sql_autocomplete_in_progress = !1
        }
    })));
    if (!a.state.completionActive) {
        var b = a.getCursor();
        b = a.getTokenAt(b);
        var c = "";
        b.string.match(/^[.`\w@]\w*$/) && (c = b.string);
        0 < c.length && CodeMirror.commands.autocomplete(a)
    }
}

function catchKeypressesFromSqlTextboxes(a) {
    !a.ctrlKey || 13 != a.keyCode && 10 != a.keyCode || 0 < $("#sqltextarea").length || 0 < $("#sqltextarea").length && run()
}
var executeMessage = CodeMirror.fromTextArea(document.getElementById("executeMessage"), {
    mode: "text/x-mysql",
    theme: "eclipse",
    lineNumbers: !0,
    autofocus: !1,
    readOnly: !0
});

function selectTheme(a) {
    editor.setOption("theme", a);
    executeMessage.setOption("theme", a)
}

function selectTheme() {
    var a = document.getElementById("codeThemeSelector");
    a = a.options[a.selectedIndex].textContent;
    editor.setOption("theme", a);
    executeMessage.setOption("theme", a)
}
var executeSQLArray = function (a, b) {
        if (b >= a.length) parent.$.messager.show({
            title: "\u63d0\u793a",
            msg: "\u6267\u884c\u5b8c\u6210\uff01",
            position: "bottomRight"
        }), executeMessage.setValue(messTemp);
        else {
            var c = a[b];
            $.ajax({
                type: "post",
                timeout: 36E5,
                url: baseUrl + "/system/permission/i/executeSqlTest/" + databaseConfigId,
                data: {
                    sql: c,
                    databaseName: databaseName
                },
                success: function (e) {
                    var f = e.status;
                    null == e.status ? messTemp = messTemp + e + " \n\n" : "success" == f ? (messTemp = messTemp + "\u3010\u547d\u4ee4\u3011 " + c + "\n \u5f71\u54cd " +
                        e.totalCount + " \u884c\uff0c \n \u8fd0\u884c\u65f6\u95f4\uff1a" + e.time + "\u6beb\u79d2\u3002\n\n", null != e.rows && showResultTabGrid(e, c, b)) : messTemp = messTemp + "\u3010\u547d\u4ee4\u3011 " + c + "\n \u4fe1\u606f\uff1a" + e.mess + " \n\n";
                    executeSQLArray(a, b + 1)
                }
            })
        }
    },
    obj, willChangeRow = [];

function showResultTabGrid(a, b, c) {
    var e = "selectDg" + c,
        f = ' <div id="tb' + c + '" style="padding:5px;height:auto"> <div>  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-table-row-insert" plain="true" id="addRowButton' + c + '"  onclick="addRow2(' + c + ')"> \u6dfb\u52a0 </a>  <span class="toolbar-item dialog-tool-separator"></span>  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-table-row-delete" plain="true" id="delButton' + c + '"   onclick="del( ' + c + ')">\u5220\u9664</a>  <span class="toolbar-item dialog-tool-separator"></span>  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-table-edit" plain="true" id="editRowButton' +
        c + '" onclick="editRow2(' + c + ' )">\u4fee\u6539</a>  <span class="toolbar-item dialog-tool-separator"></span>  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-table-link" plain="true" title="\u5168\u90e8\u5bfc\u51fajson"  id="editRowButton' + c + '" onclick="exportDataToSQLFromSQL(' + c + ' )">\u5bfc\u51fa</a>  <span class="toolbar-item dialog-tool-separator"></span>  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" title="\u5168\u90e8\u5bfc\u51faexcel"  id="editRowButton' +
        c + '" onclick="exportDataToExcelFromSQL(' + c + ' )">\u5bfc\u51fa</a>  <span class="toolbar-item dialog-tool-separator"></span>  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-chart-bar" plain="true" title="\u56fe\u8868\u663e\u793a"  id="editRowButton' + c + '" onclick="graphicView(' + c + ' )">\u56fe\u8868</a>  <span class="toolbar-item dialog-tool-separator"></span>  <a href="javascript:void(0)" class="easyui-linkbutton"  plain="true"  >&nbsp;</a>  <span class="toolbar-item dialog-tool-separator"></span>  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" plain="true" id="saveRowButton' +
        c + '"  onclick="saveRow(' + c + ' )"> \u4fdd\u5b58 </a>  <span class="toolbar-item dialog-tool-separator"></span>  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" id="cancelButton' + c + '"  onclick="cancelChange(' + c + ')"> \u53d6\u6d88 </a>  <span class="toolbar-item dialog-tool-separator"></span>  </div>  </div> ';
    $("#searchTabs").tabs("add", {
        title: "\u7ed3\u679c" + (c + 1),
        content: f + " <table id=" + e + "></table>",
        closable: !0,
        tools: [{
            iconCls: "icon-berlin-calendar",
            handler: function () {}
        }]
    });
    b = {
        url: baseUrl + "/system/permission/i/executeSqlTest/" + databaseConfigId,
        method: "POST",
        queryParams: {
            sql: b,
            databaseName: databaseName
        },
        rownumbers: !0,
        fit: !0,
        fitColumns: !0,
        border: !1,
        striped: !0,
        pagination: !0,
        pageNumber: 1,
        pageSize: 20,
        pageList: [10, 20, 30, 40, 50],
        singleSelect: !1,
        checkOnSelect: !0,
        toolbar: "#tb" + c,
        extEditing: !1,
        autoEditing: !0,
        singleEditing: !0,
        selectOnCheck: !0,
        onBeginEdit: function (a, b) {
                obj = JSON.stringify(b)
            }, onAfterEdit: function (a, b, c) {
                willChangeRow.push({
                    oldData: eval("(" + obj + ")"),
                    changesData: c
                })
            },
            onSelect: function (a, b) {
                selectRowCount++;
                1 == selectRowCount ? $("#editRowButton").linkbutton("enable") : $("#editRowButton").linkbutton("disable")
            }, onUnselect: function (a, b) {
                selectRowCount--;
                1 == selectRowCount ? $("#editRowButton").linkbutton("enable") : $("#editRowButton").linkbutton("disable")
            }, onDblClickCell: function (a, b, c) {
                c = $(this).datagrid("getColumnFields", !0).concat($(this).datagrid("getColumnFields"));
                for (var e = 0; e < c.length; e++) {
                    var f = $(this).datagrid("getColumnOption", c[e]);
                    f.editor1 = f.editor;
                    c[e] !=
                        b && (f.editor = null)
                }
                $(this).datagrid("beginEdit", a);
                for (e = 0; e < c.length; e++) f = $(this).datagrid("getColumnOption", c[e]), f.editor = f.editor1
            }
    };
    b.columns = eval(a.columns);
    b.idField = a.primaryKey;
    b.tableName = a.tableName;
    e = $("#" + e);
    0 < c && (b.onDblClickCell = null);
    e.datagrid(b);
    e.datagrid("loadData", a.rows);
    "" == a.tableName && ($("#addRowButton" + c).linkbutton("disable"), $("#delButton" + c).linkbutton("disable"), $("#editRowButton" + c).linkbutton("disable"), $("#saveRowButton" + c).linkbutton("disable"), $("#cancelButton" +
        c).linkbutton("disable"));
    0 < c && ($("#addRowButton" + c).linkbutton("disable"), $("#delButton" + c).linkbutton("disable"), $("#editRowButton" + c).linkbutton("disable"), $("#saveRowButton" + c).linkbutton("disable"), $("#cancelButton" + c).linkbutton("disable"))
}

function exportDataToSQLFromSQL(a) {
    var b = $("#selectDg" + a).datagrid("options").queryParams.sql;
    a = $("#selectDg" + a).datagrid("options").queryParams.databaseName;
    $("#sql").val(b);
    $("#exportType").val("json");
    $("#databaseName").val(a);
    $("#databaseConfigId").val(databaseConfigId);
    $("#form3").submit()
}

function exportDataToExcelFromSQL(a) {
    var b = $("#selectDg" + a).datagrid("options").queryParams.sql;
    a = $("#selectDg" + a).datagrid("options").queryParams.databaseName;
    $("#sql").val(b);
    $("#exportType").val("excel");
    $("#databaseName").val(a);
    $("#databaseConfigId").val(databaseConfigId);
    $("#form3").submit()
}

function graphicView(a) {
    $("#selectDg" + a).datagrid("options");
    var b = $("#selectDg" + a).datagrid("options").queryParams.databaseName;
    window.mainpage.mainTabs.addModule("\u56fe\u8868\u663e\u793a", baseUrl + "/system/graphic/graphicPage/" + b + "/" + databaseConfigId + "/" + a, "icon-standard-chart-bar")
}

function addRow2(a) {
    isAdd = !0;
    $("#selectDg" + a).datagrid("insertRow", {
        index: 0,
        row: {}
    });
    $("#selectDg" + a).datagrid("beginEdit", 0)
}

function del(a) {
    var b = $("#selectDg" + a).datagrid("options"),
        c = b.idField,
        e = b.tableName;
    b = $("#selectDg" + a).datagrid("getChanges", "inserted");
    var f = $("#selectDg" + a).datagrid("getChanges", "updated");
    if (b.length || f.length) parent.$.messager.show({
        title: "\u63d0\u793a",
        msg: "\u8bf7\u5148\u4fdd\u5b58\u53d8\u66f4\u5185\u5bb9\uff01",
        position: "bottomRight"
    });
    else {
        var g = $("#selectDg" + a).datagrid("getChecked");
        b = g.length;
        0 == g.length ? parent.$.messager.show({
            title: "\u63d0\u793a",
            msg: "\u8bf7\u5148\u9009\u62e9\u4e00\u884c\u6570\u636e\uff01",
            position: "bottomRight"
        }) : $.easyui.messager.confirm("\u64cd\u4f5c\u63d0\u9192", "\u60a8\u786e\u5b9a\u8981\u5220\u9664" + b + "\u884c\u6570\u636e\uff1f", function (b) {
            b && $.ajax({
                type: "POST",
                contentType: "application/json;charset=utf-8",
                url: baseUrl + "/system/permission/i/deleteRows/" + databaseConfigId,
                data: JSON.stringify({
                    databaseName: databaseName,
                    tableName: e,
                    primary_key: c,
                    checkedItems: JSON.stringify(g)
                }),
                datatype: "json",
                success: function (b) {
                    "success" == b.status ? ($("#selectDg" + a).datagrid("reload"), $("#selectDg" +
                        a).datagrid("clearSelections").datagrid("clearChecked"), selectRowCount = 0, parent.$.messager.show({
                        title: "\u63d0\u793a",
                        msg: "\u5220\u9664\u6210\u529f\uff01",
                        position: "bottomRight"
                    })) : parent.$.messager.show({
                        title: "\u63d0\u793a",
                        msg: b.mess,
                        position: "bottomRight"
                    })
                }
            })
        })
    }
}

function saveRow(a) {
    endEdit(a);
    var b = $("#selectDg" + a).datagrid("getChanges", "inserted"),
        c = willChangeRow,
        e = {},
        f = $("#selectDg" + a).datagrid("options"),
        g = f.idField;
    f = f.tableName;
    e.databaseName = databaseName;
    e.tableName = f;
    e.primary_key = g;
    b.length && (e.inserted = JSON.stringify(b));
    c.length && (e.updated = JSON.stringify(c));
    (b.length || c.length) && $.post(baseUrl + "/system/permission/i/saveData/" + databaseConfigId, e, function (b) {
        willChangeRow = [];
        "success" == b.status ? (parent.$.messager.show({
            title: "\u63d0\u793a",
            msg: "\u4fdd\u5b58\u6210\u529f\uff01",
            position: "bottomRight"
        }), isAdd && $("#selectDg" + a).datagrid("acceptChanges")) : $.messager.alert("\u63d0\u793a", b.mess);
        isAdd = !1
    }, "JSON").error(function () {
        $.messager.alert("\u63d0\u793a", "\u63d0\u4ea4\u9519\u8bef\u4e86\uff01")
    })
}

function refresh(a) {
    $("#selectDg" + a).datagrid("reload");
    $("#selectDg" + a).datagrid("clearSelections").datagrid("clearChecked")
}

function endEdit(a) {
    for (var b = $("#selectDg" + a).datagrid("getRows"), c = 0; c < b.length; c++) $("#selectDg" + a).datagrid("endEdit", c)
}

function editRow2(a) {
    var b = $("#selectDg" + a).datagrid("getChecked");
    0 == b.length ? parent.$.messager.show({
        title: "\u63d0\u793a",
        msg: "\u8bf7\u5148\u9009\u62e9\u4e00\u884c\u6570\u636e\uff01",
        position: "bottomRight"
    }) : $.each(b, function (b, e) {
        b = $("#selectDg" + a).datagrid("getRowIndex", e);
        $("#selectDg" + a).datagrid("beginEdit", b)
    })
}

function cancelChange(a) {
    endEdit(a);
    refresh(a)
}

function refresh(a) {
    $("#selectDg" + a).datagrid("reload");
    $("#selectDg" + a).datagrid("clearSelections").datagrid("clearChecked")
}

function searchVersion() {
    $.ajax({
        type: "GET",
        timeout: 1E3,
        url: "http://www.treesoft.cn/treesoft/system/searchVersion",
        datatype: "json",
        success: function (a) {}, error: function (a) {}
    })
};