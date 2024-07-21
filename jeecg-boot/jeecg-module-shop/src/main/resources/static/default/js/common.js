layui.config({base: getProjectUrl() + "default/module/"}).extend({
    notice: "notice/notice",
    dropdown: "dropdown/dropdown"
}).use(["jquery", "element", "util", "admin", "layer"], function () {
    var f = layui.jquery;
    var e = layui.layer;
    var d = layui.element;
    var c = layui.util;
    var b = layui.admin;
    b.removeLoading();
    if (f(".ew-header").length > 0) {
        var a = [];
        f("[nav-id]").each(function () {
            a.push(f(this).attr("nav-id"))
        });
        d.on("nav(ew-header-nav)", function (g) {
            var j = f(g).attr("lay-href");
            if (j) {
                if (a.length == 0) {
                    location.href = j
                } else {
                    if (j.indexOf("#") != -1) {
                        var i = j.substring(j.indexOf("#") + 1);
                        var h = f('[nav-id="' + i + '"]');
                        if (h.length > 0) {
                            f("html,body").animate({scrollTop: h.offset().top - 70}, 300)
                        }
                    } else {
                        f("html").animate({scrollTop: 0}, 300)
                    }
                }
            }
        });
        if (a.length > 0) {
            f(document).on("click", "[nav-scroll]", function () {
                var h = f(this).attr("nav-scroll");
                var g = f('[nav-id="' + h + '"]');
                if (g.length > 0) {
                    f(".ew-header .layui-nav-item").removeClass("layui-this");
                    f('.ew-header a[lay-href$="#' + h + '"]').parent().addClass("layui-this");
                    f("html,body").animate({scrollTop: g.offset().top - 70}, 300)
                }
            })
        }
    }
    f("body").on("click", ".ew-nav-group .layui-nav-item", function (g) {
        if (b.getPageWidth() < 935) {
            f(".ew-nav-group .layui-nav-item>.layui-nav-child").removeClass("layui-anim layui-anim-upbit");
            var h = f(this).children(".layui-nav-child");
            if (h.hasClass("layui-show")) {
                h.removeClass("layui-show");
                f(this).find(".layui-nav-more").removeClass("layui-nav-mored")
            } else {
                f(".ew-nav-group .layui-nav-item>.layui-nav-child").removeClass("layui-show");
                f(".ew-nav-group .layui-nav-item>a>.layui-nav-more").removeClass("layui-nav-mored");
                h.addClass("layui-show");
                f(this).find(".layui-nav-more").addClass("layui-nav-mored")
            }
        }
    });
    f(function () {
        f("#backtop").click(function () {
            f("html,body").animate({scrollTop: "0px"}, 200)
        })
    });
    f("#btn-show").on("click", function (g) {
        f.get("/getShoppingNotes", function (h) {
            if (h.code == 0) {
                e.open({
                    type: 1,
                    title: "购物须知",
                    closeBtn: false,
                    area: "300px;",
                    shade: 0.4,
                    id: "LAY_layuipro",
                    btn: ["我知道啦"],
                    btnAlign: "c",
                    moveType: 1,
                    content: '<div style="padding: 20px; font-size: 16px; font-family: Content-font, Roboto, sans-serif; font-weight: 400;">' + h.data + "</div>",
                    success: function (i) {
                        var j = i.find(".layui-layer-btn");
                        j.click(function () {
                        })
                    }
                })
            }
        }, "json")
    })
});

function getProjectUrl() {
    var c = layui.cache.dir;
    if (!c) {
        var e = document.scripts, b = e.length - 1, f;
        for (var a = b; a > 0; a--) {
            if (e[a].readyState === "interactive") {
                f = e[a].src;
                break
            }
        }
        var d = f || e[b].src;
        c = d.substring(0, d.lastIndexOf("/") + 1)
    }
    return c.substring(0, c.indexOf("default"))
};