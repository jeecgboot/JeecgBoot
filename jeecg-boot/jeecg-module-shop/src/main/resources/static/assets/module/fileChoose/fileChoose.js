layui.define(["jquery", "layer", "form", "upload", "util"], function (c) {
    var g = layui.jquery;
    var e = layui.layer;
    var f = layui.form;
    var d = layui.upload;
    var b = layui.util;
    var h = [{suffix: ["ppt", "pptx"], icon: "ppt"}, {suffix: ["doc", "docx"], icon: "doc"}, {
        suffix: ["xls", "xlsx"],
        icon: "xls"
    }, {suffix: ["pdf"], icon: "pdf"}, {suffix: ["html", "htm"], icon: "htm"}, {
        suffix: ["txt"],
        icon: "txt"
    }, {suffix: ["swf", "docx"], icon: "flash"}, {suffix: ["zip", "rar", "7z"], icon: "zip"}, {
        suffix: ["mp3", "wav"],
        icon: "mp3"
    }, {suffix: ["mp4", "3gp", "rmvb", "avi", "flv"], icon: "mp4"}, {suffix: ["psd"], icon: "psd"}, {
        suffix: ["ttf"],
        icon: "ttf"
    }, {suffix: ["apk"], icon: "apk"}, {suffix: ["exe"], icon: "exe"}, {
        suffix: ["torrent"],
        icon: "bt"
    }, {suffix: ["gif", "png", "jpeg", "jpg", "bmp"], icon: "img"}];
    var a = {};
    a.open = function (r) {
        var x = r.fileUrl;
        var l = r.listUrl;
        var u = r.where;
        var A = r.num;
        var v = r.onChoose;
        var i = r.upload;
        var p = r.dialog;
        var B = r.menu;
        var o = r.menuClick;
        var j = r.response ? r.response : {};
        var y = j.dir;
        var E = j.code;
        var s = j.url;
        var z = j.smUrl;
        var w = j.isDir;
        var F = j.name;
        var n = j.method;
        var k = j.parseData;
        var t = [];
        u || (u = {});
        (A != undefined) || (A = 1);
        i || (i = {});
        p || (p = {});
        y || (y = "dir");
        (E != undefined) || (E = 200);
        s || (s = "url");
        z || (z = "smUrl");
        w || (w = "isDir");
        F || (F = "name");
        n || (n = "get");
        p.id = "file-choose-dialog";
        p.type = 1;
        (p.title != undefined) || (p.title = "选择文件");
        p.content = "";
        p.area || (p.area = ["565px", "420px"]);
        (p.shade != undefined) || (p.shade = 0.1);
        p.fixed || (p.fixed = false);
        p.skin || (p.skin = "layer-file-choose");
        var q = r.success;
        p.success = function (G, H) {
            g(G).children(".layui-layer-content").load(layui.cache.base + "fileChoose/fileChoose.html", function () {
                C();
                q && q(G, index)
            })
        };
        e.open(p);

        function D(G) {
            G || (G = g("#fc-current-position").text());
            g(".file-choose-dialog .file-choose-loading-group").removeClass("layui-hide");
            u[y] = G;
            g("#file-choose-list").html("");
            g.ajax({
                url: l, type: n, data: u, dataType: "json", success: function (H) {
                    k && (H = k(H));
                    if (H.code == E) {
                        t = H.data;
                        g("#fc-btn-ok-sel").text("完成选择");
                        g("#file-choose-list").html(a.renderList({
                            fileUrl: x,
                            data: t,
                            multi: A > 1,
                            menu: B,
                            response: j
                        }));
                        f.render("checkbox")
                    } else {
                        e.msg(H.msg, {icon: 2, anim: 6});
                        g("#file-choose-list").html(a.getErrorHtml("加载失败", "layui-icon-face-cry"))
                    }
                    setTimeout(function () {
                        g(".file-choose-dialog .file-choose-loading-group").addClass("layui-hide")
                    }, 200)
                }
            })
        }

        function C() {
            (A > 1) || (g(".file-choose-dialog").addClass("hide-bottom"));
            D();
            g("#fc-btn-refresh").click(function () {
                D()
            });
            g("#fc-btn-back").click(function () {
                var G = g("#fc-current-position").text();
                if (G != "/") {
                    G = G.substring(0, G.lastIndexOf("/"));
                    G || (G = "/");
                    g("#fc-current-position").text(G);
                    D(G)
                }
            });
            i.elem = "#fc-btn-upload";
            i.data || (i.data = {});
            i.data.dir = function () {
                return g("#fc-current-position").text()
            };
            i.before = function () {
                e.load(2)
            };
            i.done = function (J, I, H) {
                e.closeAll("loading");
                if (J.code != E) {
                    e.msg(J.msg, {icon: 2})
                } else {
                    e.msg(J.msg, {icon: 1});
                    var G = J.dir ? J.dir : b.toDateString(new Date(), "/yyyy/MM/dd");
                    g("#fc-current-position").text(G);
                    D()
                }
            };
            i.error = function () {
                e.closeAll("loading");
                e.msg("上传失败", {icon: 2})
            };
            d.render(i);
            g("#fc-btn-ok-sel").click(function () {
                var G = [];
                g('input[lay-filter="file-choose-item-ck"]:checked').each(function () {
                    var H = g(this).parents(".file-choose-list-item").data("index");
                    G.push(t[H])
                });
                if (G.length <= 0) {
                    e.msg("请选择", {icon: 2, anim: 6})
                } else {
                    if (G.length > A) {
                        e.msg("最多只能选择" + A + "个", {icon: 2, anim: 6})
                    } else {
                        m(G)
                    }
                }
            });
            g(document).off("click.fcli").on("click.fcli", ".file-choose-dialog .file-choose-list-item", function (I) {
                var H = t[g(this).data("index")];
                if (H[w]) {
                    var J = g("#fc-current-position").text();
                    J += (J == "/" ? H[F] : ("/" + H[F]));
                    g("#fc-current-position").text(J);
                    D(J)
                } else {
                    var G = g(this).find(".file-choose-oper-menu");
                    g(".file-choose-dialog .file-choose-oper-menu").not(G).removeClass("show");
                    G.toggleClass("show");
                    I.stopPropagation()
                }
            });
            g(document).off("click.fclom").on("click.fclom", ".file-choose-dialog", function (G) {
                g(".file-choose-dialog .file-choose-oper-menu").removeClass("show");
                G.stopPropagation()
            });
            f.on("checkbox(file-choose-item-ck)", function (H) {
                var G = g('.file-choose-dialog input[lay-filter="file-choose-item-ck"]:checked').length;
                if (H.elem.checked) {
                    if (G > A) {
                        e.msg("最多只能选择" + A + "个", {icon: 2, anim: 6});
                        g(H.elem).prop("checked", false);
                        f.render("checkbox");
                        return
                    }
                    g(H.elem).parents(".file-choose-list-item").addClass("active")
                } else {
                    g(H.elem).parents(".file-choose-list-item").removeClass("active")
                }
                g("#fc-btn-ok-sel").text("完成选择" + (G > 0 ? ("(" + G + ")") : ""))
            });
            g(document).off("click.fclic").on("click.fclic", ".file-choose-dialog .file-choose-list-item-ck", function (G) {
                G.stopPropagation()
            });
            g(document).off("click.fclomi").on("click.fclomi", ".file-choose-dialog .file-choose-oper-menu-item", function () {
                var K = g(this).data("event");
                var J = g(this).parent().parent().data("index");
                if ("choose" == K) {
                    if (A > 1) {
                        g(this).parent().parent().find(".layui-form-checkbox").trigger("click")
                    } else {
                        m([t[J]])
                    }
                } else {
                    if ("preview" == K) {
                        var H = (x + t[J][s]);
                        if ("img" == a.getFileType(H)) {
                            var L = [], M = 0;
                            for (var I = 0; I < t.length; I++) {
                                var G = x + t[I][s];
                                if ("img" == a.getFileType(G)) {
                                    L.push({src: G, alt: t[I][F]})
                                }
                                if (H == G) {
                                    M = L.length - 1
                                }
                            }
                            e.photos({photos: {start: M, data: L}, shade: 0.1, closeBtn: true})
                        } else {
                            e.confirm("这不是图片类型，可能需要下载才能预览，确定要打开吗？", {
                                title: "温馨提示",
                                area: "260px",
                                shade: 0.1
                            }, function (N) {
                                e.close(N);
                                window.open(H)
                            })
                        }
                    } else {
                        o && o(K, t[J])
                    }
                }
            })
        }

        function m(G) {
            v && v(G);
            e.close(g("#fc-btn-ok-sel").parents(".layui-layer").attr("id").substring(11))
        }
    };
    a.renderList = function (l) {
        var r = l.fileUrl;
        var o = l.data;
        var t = l.multi;
        var u = l.menu;
        var j = l.response ? l.response : {};
        var n = j.url;
        var s = j.smUrl;
        var q = j.isDir;
        var A = j.name;
        (r == undefined) && (r = "");
        (o == undefined) && (o = []);
        (t == undefined) && (t = false);
        n || (n = "url");
        s || (s = "smUrl");
        q || (q = "isDir");
        A || (A = "name");
        var p = "";
        if (o.length <= 0) {
            p += a.getErrorHtml("没有文件")
        } else {
            for (var v = 0; v < o.length; v++) {
                var x = o[v];
                p += '<div class="file-choose-list-item" data-index="' + v + '">';
                var z = r + '/' + x[s], w = "";
                if (!x[s]) {
                    w = " img-icon";
                    z = a.getFileIcon(x[n], x[q])
                }
                var m = "background-image: url('" + z + "')";
                p += '   <div class="file-choose-list-item-img' + w + '" style="' + m + '"></div>';
                p += '   <div class="file-choose-list-item-name" title="' + x[A] + '">' + x[A] + "</div>";
                if (!x[q] && t) {
                    p += '   <div class="file-choose-list-item-ck layui-form">';
                    p += '       <input type="checkbox" lay-skin="primary" lay-filter="file-choose-item-ck"/>';
                    p += "   </div>"
                }
                if (!u) {
                    p += '<div class="file-choose-oper-menu">';
                    p += '   <div class="file-choose-oper-menu-item" data-event="preview">预览</div>';
                    p += '   <div class="file-choose-oper-menu-item" data-event="choose">选择</div>';
                    p += "</div>"
                } else {
                    if (u.length > 0) {
                        p += '<div class="file-choose-oper-menu">';
                        for (var y = 0; y < u.length; y++) {
                            var k = u[y];
                            p += '<div class="file-choose-oper-menu-item" data-event="' + k.event + '">' + k.name + "</div>"
                        }
                        p += "</div>"
                    }
                }
                p += "</div>"
            }
        }
        return p
    };
    a.getErrorHtml = function (k, j) {
        j || (j = "layui-icon-face-surprised");
        var i = "";
        i += '<div class="file-choose-empty">';
        i += '   <i class="layui-icon ' + j + '"></i>';
        i += "   <p>" + k + "</p>";
        i += "</div>";
        return i
    };
    a.getFileIcon = function (i, k) {
        var j = k ? "dir" : a.getFileType(i);
        return layui.cache.base + "fileChoose/img/" + j + ".png"
    };
    a.getFileType = function (l) {
        var n = "file";
        var o = l.substring(l.lastIndexOf(".") + 1);
        for (var m = 0; m < h.length; m++) {
            for (var k = 0; k < h[m].suffix.length; k++) {
                if (o.toLowerCase() == h[m].suffix[k]) {
                    n = h[m].icon;
                    break
                }
            }
        }
        return n
    };
    g("body").append("<style>.layer-file-choose { max-width: 100%;}@media screen and (max-width:768px){.layer-file-choose{max-width:98%;max-width:-moz-calc(100% - 30px);max-width:-webkit-calc(100% - 30px);max-width:calc(100% - 30px);left:0!important;right:0!important;margin:auto!important;margin-bottom:15px!important}}</style>");
    c("fileChoose", a)
});
