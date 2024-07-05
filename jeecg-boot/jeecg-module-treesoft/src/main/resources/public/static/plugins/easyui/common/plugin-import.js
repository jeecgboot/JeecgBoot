
(function () {

    var plugins = {
        ueditor: [
            "<link href=\"../plugins/ueditor/ueditor1_4_3-utf8-net/themes/default/css/ueditor.css\" rel=\"stylesheet\"/>",
            "<script src=\"../plugins/ueditor/ueditor1_4_3-utf8-net/ueditor.config.js\"></script>",
            "<script src=\"../plugins/ueditor/ueditor1_4_3-utf8-net/ueditor.all.js\"></script>",
            "<script src=\"../plugins/ueditor/ueditor1_4_3-utf8-net/lang/zh-cn/zh-cn.js\"></script>",
            "<script src=\"../jeasyui-extensions/jquery.ueditor.js\"></script>"
        ],
        syntaxhighlighter: [
            "<script src=\"../plugins/syntaxhighlighter_3.0.83/scripts/shCore.min.js\"></script>",
            "<script src=\"../plugins/syntaxhighlighter_3.0.83/scripts/shBrushJScript.js\"></script>",
            "<script src=\"../plugins/syntaxhighlighter_3.0.83/scripts/shBrushXml.js\"></script>",
            "<link href=\"../plugins/syntaxhighlighter_3.0.83/styles/shCoreDefault.css\" rel=\"stylesheet\" type=\"text/css\"/>"
        ],
        my97: [
            "<script src=\"../plugins/My97DatePicker/WdatePicker.js\"></script>",
            "<script src=\"../jeasyui-extensions/jquery.my97.js\"></script>"
        ],
        codemirror: [
            "<link href=\"../plugins/codemirror-4.3/lib/codemirror.css\" rel=\"stylesheet\"/>",
            "<script src=\"../plugins/codemirror-4.3/lib/codemirror.js\"></script>",
            "<script src=\"../plugins/codemirror-4.3/mode/xml/xml.js\"></script>",
            "<script src=\"../plugins/codemirror-4.3/mode/javascript/javascript.js\"></script>",
            "<script src=\"../plugins/codemirror-4.3/mode/vbscript/vbscript.js\"></script>",
            "<script src=\"../plugins/codemirror-4.3/mode/css/css.js\"></script>",
            "<script src=\"../plugins/codemirror-4.3/mode/htmlmixed/htmlmixed.js\"></script>",
            "<script src=\"../jeasyui-extensions/jquery.codemirror.js\"></script>"
        ],
        euploadify: [
            "<link href=\"../plugins/uploadify/uploadify.css\" rel=\"stylesheet\"/>",
            "<script src=\"../plugins/uploadify/jquery.uploadify.js\"></script>",
            "<script src=\"../jeasyui-extensions/jquery.euploadify.js\"></script>"
        ]
    };

    var param = $.util.request["plugin"],
        params = param ? String(param).split(",") : [],
        list = ["syntaxhighlighter", "codemirror"],
        imported = [];

    if ($.array.contains(params, "all"), function (val) { return String(val).toLocaleLowerCase() == "all" }) {
        return $.each(plugins, function (name) { loadPlugin(name); });
    }

    $.each(list, function (i, name) { loadPlugin(name); });

    $.each(params, function (i, name) { loadPlugin(name); });

    function loadPlugin(name) {
        if ($.string.isNullOrWhiteSpace(name)) { return; }
        var plugin = plugins[name];
        if (plugin && !$.array.contains(imported, name)) {
            $.each(plugin, function (i, script) {
                //$(script).appendTo("head");
                document.write(script);
            });
            imported.push(name);
        }
    };

})();