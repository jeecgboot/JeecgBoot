layui.define(["layer", "element"], function (b) {
    var d = layui.jquery;
    var c = layui.layer;
    var a = {
        popupRight: function (e) {
            if (e.title == undefined) {
                e.title = false;
                e.closeBtn = false
            }
            if (e.fixed == undefined) {
                e.fixed = true
            }
            e.anim = -1;
            e.offset = "r";
            e.shadeClose = true;
            e.area || (e.area = "336px");
            e.skin || (e.skin = "layui-anim layui-anim-rl layui-layer-adminRight");
            e.move = false;
            return a.open(e)
        }, open: function (h) {
            if (!h.area) {
                h.area = (h.type == 2) ? ["360px", "300px"] : "360px"
            }
            if (!h.skin) {
                h.skin = "layui-layer-admin"
            }
            if (h.fixed == undefined) {
                h.fixed = false
            }
            h.resize = h.resize != undefined ? h.resize : false;
            h.shade = h.shade != undefined ? h.shade : 0.1;
            var f = h.end;
            h.end = function () {
                c.closeAll("tips");
                f && f()
            };
            if (h.url) {
                (h.type == undefined) && (h.type = 1);
                var g = h.success;
                h.success = function (i, j) {
                    a.showLoading(i, 2);
                    d(i).children(".layui-layer-content").load(h.url, function () {
                        g ? g(i, j) : "";
                        a.removeLoading(i, false)
                    })
                }
            }
            var e = c.open(h);
            (h.data) && (a.layerData["d" + e] = h.data);
            return e
        }, layerData: {}, getLayerData: function (e, f) {
            if (e == undefined) {
                e = parent.layer.getFrameIndex(window.name);
                return parent.layui.admin.getLayerData(e, f)
            } else {
                if (e.toString().indexOf("#") == 0) {
                    e = d(e).parents(".layui-layer").attr("id").substring(11)
                }
            }
            var g = a.layerData["d" + e];
            if (f) {
                return g ? g[f] : g
            }
            return g
        }, putLayerData: function (f, h, e) {
            if (e == undefined) {
                e = parent.layer.getFrameIndex(window.name);
                return parent.layui.admin.putLayerData(f, h, e)
            } else {
                if (e.toString().indexOf("#") == 0) {
                    e = d(e).parents(".layui-layer").attr("id").substring(11)
                }
            }
            var g = a.getLayerData(e);
            g || (g = {});
            g[f] = h
        }, showLoading: function (i, h, f) {
            var g;
            if (i != undefined && (typeof i != "string") && !(i instanceof d)) {
                h = i.type;
                f = i.opacity;
                g = i.size;
                i = i.elem
            }
            (!i) && (i = "body");
            (h == undefined) && (h = 1);
            (g == undefined) && (g = "sm");
            g = " " + g;
            var e = ['<div class="ball-loader' + g + '"><span></span><span></span><span></span><span></span></div>', '<div class="rubik-loader' + g + '"></div>', '<div class="signal-loader' + g + '"><span></span><span></span><span></span><span></span></div>'];
            d(i).addClass("page-no-scroll");
            var j = d(i).children(".page-loading");
            if (j.length <= 0) {
                d(i).append('<div class="page-loading">' + e[h - 1] + "</div>");
                j = d(i).children(".page-loading")
            }
            f && j.css("background-color", "rgba(255,255,255," + f + ")");
            j.show()
        }, removeLoading: function (f, h, e) {
            if (!f) {
                f = "body"
            }
            if (h == undefined) {
                h = true
            }
            var g = d(f).children(".page-loading");
            if (e) {
                g.remove()
            } else {
                h ? g.fadeOut() : g.hide()
            }
            d(f).removeClass("page-no-scroll")
        }, putTempData: function (f, g) {
            var e = a.tableName + "_tempData";
            if (g != undefined && g != null) {
                layui.sessionData(e, {key: f, value: g})
            } else {
                layui.sessionData(e, {key: f, remove: true})
            }
        }, getTempData: function (f) {
            var e = a.tableName + "_tempData";
            var g = layui.sessionData(e);
            if (g) {
                return g[f]
            } else {
                return false
            }
        }, refresh: function () {
            location.reload()
        }, closeThisDialog: function () {
            parent.layer.close(parent.layer.getFrameIndex(window.name))
        }, closeDialog: function (e) {
            var f = d(e).parents(".layui-layer").attr("id").substring(11);
            c.close(f)
        }, iframeAuto: function () {
            parent.layer.iframeAuto(parent.layer.getFrameIndex(window.name))
        }, getPageHeight: function () {
            return document.documentElement.clientHeight || document.body.clientHeight
        }, getPageWidth: function () {
            return document.documentElement.clientWidth || document.body.clientWidth
        }, resizeTable: function (e) {
            setTimeout(function () {
                d(".layui-table-view").each(function () {
                    var f = d(this).attr("lay-id");
                    layui.table && layui.table.resize(f)
                })
            }, e == undefined ? 0 : e)
        }
    };
    a.events = {
        refresh: function () {
            a.refresh()
        }, back: function () {
            history.back()
        }, psw: function () {
            var e = d(this).data("url");
            a.open({
                id: "pswForm",
                type: 2,
                title: "修改密码",
                area: ["360px", "287px"],
                content: e ? e : "page/tpl/tpl-password.html"
            })
        }, logout: function () {
            var e = d(this).data("url");
            c.confirm("确定要退出登录吗？", {title: "温馨提示", skin: "layui-layer-admin", shade: 0.1}, function () {
                location.replace(e ? e : "/")
            })
        }, open: function () {
            var e = d(this).data();
            a.open(a.parseLayerOption(a.util.deepClone(e)))
        }, popupRight: function () {
            var e = d(this).data();
            a.popupRight(a.parseLayerOption(a.util.deepClone(e)))
        }, closeDialog: function () {
            a.closeThisDialog()
        }, closePageDialog: function () {
            a.closeDialog(this)
        }
    };
    a.util = {
        deepClone: function (h) {
            var e;
            var f = a.util.isClass(h);
            if (f === "Object") {
                e = {}
            } else {
                if (f === "Array") {
                    e = []
                } else {
                    return h
                }
            }
            for (var g in h) {
                var i = h[g];
                if (a.util.isClass(i) == "Object") {
                    e[g] = arguments.callee(i)
                } else {
                    if (a.util.isClass(i) == "Array") {
                        e[g] = arguments.callee(i)
                    } else {
                        e[g] = h[g]
                    }
                }
            }
            return e
        }, isClass: function (e) {
            if (e === null) {
                return "Null"
            }
            if (e === undefined) {
                return "Undefined"
            }
            return Object.prototype.toString.call(e).slice(8, -1)
        }
    };
    d(document).on("click", "*[ew-event]", function () {
        var e = d(this).attr("ew-event");
        var f = a.events[e];
        f && f.call(this, d(this))
    });
    d(document).on("mouseenter", "*[lay-tips]", function () {
        var e = d(this).attr("lay-tips");
        var f = d(this).attr("lay-direction");
        var g = d(this).attr("lay-bg");
        var h = d(this).attr("lay-offset");
        c.tips(e, this, {
            tips: [f || 1, g || "#303133"], time: -1, success: function (i, j) {
                if (h) {
                    h = h.split(",");
                    var l = h[0], k = h.length > 1 ? h[1] : undefined;
                    l && (d(i).css("margin-top", l));
                    k && (d(i).css("margin-left", k))
                }
            }
        })
    }).on("mouseleave", "*[lay-tips]", function () {
        c.closeAll("tips")
    });
    if (a.getPageWidth() < 768) {
        if (layui.device().os == "windows") {
            d("body").append("<style>@media screen and (max-width: 991px) {::-webkit-scrollbar{width:8px;height:9px;background:transparent}::-webkit-scrollbar-track{background:transparent}::-webkit-scrollbar-thumb{border-radius:5px;background-color:#c1c1c1}::-webkit-scrollbar-thumb:hover{background-color:#a8a8a8}.mini-bar::-webkit-scrollbar{width:5px;height:5px}.mini-bar::-webkit-scrollbar-thumb{border-radius:3px}}</style>")
        }
    }
    b("admin", a)
});