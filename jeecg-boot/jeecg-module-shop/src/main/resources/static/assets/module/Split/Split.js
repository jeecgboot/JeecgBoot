/*! Split.js - v1.5.9 */
!function (e, t) {
    if ("object" == typeof exports && "undefined" != typeof module) {
        module.exports = t()
    } else if ("function" == typeof define && define.amd) {
        define(t)
    } else if (window.layui && layui.define) {  // layui加载
        layui.define(function (exports) {
            layui.link(layui.cache.base + 'Split/Split.css');
            exports('Split', t());
        });
    } else {
        e.Split = t()
    }
    /*
    "object" == typeof exports && "undefined" != typeof module ? module.exports = t() : "function" == typeof define && define.amd ? define(t) : e.Split = t()
    */
}(this, function () {
    "use strict";
    var B = window, L = B.document, T = "addEventListener", N = "removeEventListener", R = "getBoundingClientRect",
        q = "horizontal", H = function () {
            return !1
        }, I = B.attachEvent && !B[T], i = ["", "-webkit-", "-moz-", "-o-"].filter(function (e) {
            var t = L.createElement("div");
            return t.style.cssText = "width:" + e + "calc(9px)", !!t.style.length
        }).shift() + "calc", s = function (e) {
            return "string" == typeof e || e instanceof String
        }, W = function (e) {
            if (s(e)) {
                var t = L.querySelector(e);
                if (!t) throw new Error("Selector " + e + " did not match a DOM element");
                return t
            }
            return e
        }, X = function (e, t, n) {
            var r = e[t];
            return void 0 !== r ? r : n
        }, Y = function (e, t, n, r) {
            if (t) {
                if ("end" === r) return 0;
                if ("center" === r) return e / 2
            } else if (n) {
                if ("start" === r) return 0;
                if ("center" === r) return e / 2
            }
            return e
        }, G = function (e, t) {
            var n = L.createElement("div");
            return n.className = "gutter gutter-" + t, n
        }, J = function (e, t, n) {
            var r = {};
            return s(t) ? r[e] = t : r[e] = I ? t + "%" : i + "(" + t + "% - " + n + "px)", r
        }, K = function (e, t) {
            var n;
            return (n = {})[e] = t + "px", n
        };
    return function (e, i) {
        void 0 === i && (i = {});
        var u, t, s, o, r, a, l = e;
        Array.from && (l = Array.from(l));
        var c = W(l[0]).parentNode, f = getComputedStyle ? getComputedStyle(c).flexDirection : null,
            m = X(i, "sizes") || l.map(function () {
                return 100 / l.length
            }), n = X(i, "minSize", 100), h = Array.isArray(n) ? n : l.map(function () {
                return n
            }), d = X(i, "expandToMin", !1), g = X(i, "gutterSize", 5), v = X(i, "gutterAlign", "center"),
            p = X(i, "snapOffset", 30), y = X(i, "dragInterval", 1), z = X(i, "direction", q),
            S = X(i, "cursor", z === q ? "col-resize" : "row-resize"), b = X(i, "gutter", G),
            _ = X(i, "elementStyle", J), E = X(i, "gutterStyle", K);

        function w(t, e, n, r) {
            var i = _(u, e, n, r);
            Object.keys(i).forEach(function (e) {
                t.style[e] = i[e]
            })
        }

        function k() {
            return a.map(function (e) {
                return e.size
            })
        }

        function x(e) {
            return "touches" in e ? e.touches[0][t] : e[t]
        }

        function M(e) {
            var t = a[this.a], n = a[this.b], r = t.size + n.size;
            t.size = e / this.size * r, n.size = r - e / this.size * r, w(t.element, t.size, this._b, t.i), w(n.element, n.size, this._c, n.i)
        }

        function U() {
            var e = a[this.a].element, t = a[this.b].element, n = e[R](), r = t[R]();
            this.size = n[u] + r[u] + this._b + this._c, this.start = n[s], this.end = n[o]
        }

        function O(s) {
            var o = function (e) {
                if (!getComputedStyle) return null;
                var t = getComputedStyle(e), n = e[r];
                return 0 === n ? null : n -= z === q ? parseFloat(t.paddingLeft) + parseFloat(t.paddingRight) : parseFloat(t.paddingTop) + parseFloat(t.paddingBottom)
            }(c);
            if (null === o) return s;
            var a = 0, u = [], e = s.map(function (e, t) {
                var n = o * e / 100, r = Y(g, 0 === t, t === s.length - 1, v), i = h[t] + r;
                return n < i ? (a += i - n, u.push(0), i) : (u.push(n - i), n)
            });
            return 0 === a ? s : e.map(function (e, t) {
                var n = e;
                if (0 < a && 0 < u[t] - a) {
                    var r = Math.min(a, u[t] - a);
                    a -= r, n = e - r
                }
                return n / o * 100
            })
        }

        function C(e) {
            if (!("button" in e && 0 !== e.button)) {
                var t = this, n = a[t.a].element, r = a[t.b].element;
                t.dragging || X(i, "onDragStart", H)(k()), e.preventDefault(), t.dragging = !0, t.move = function (e) {
                    var t, n = a[this.a], r = a[this.b];
                    this.dragging && (t = x(e) - this.start + (this._b - this.dragOffset), 1 < y && (t = Math.round(t / y) * y), t <= n.minSize + p + this._b ? t = n.minSize + this._b : t >= this.size - (r.minSize + p + this._c) && (t = this.size - (r.minSize + this._c)), M.call(this, t), X(i, "onDrag", H)())
                }.bind(t), t.stop = function () {
                    var e = this, t = a[e.a].element, n = a[e.b].element;
                    e.dragging && X(i, "onDragEnd", H)(k()), e.dragging = !1, B[N]("mouseup", e.stop), B[N]("touchend", e.stop), B[N]("touchcancel", e.stop), B[N]("mousemove", e.move), B[N]("touchmove", e.move), e.stop = null, e.move = null, t[N]("selectstart", H), t[N]("dragstart", H), n[N]("selectstart", H), n[N]("dragstart", H), t.style.userSelect = "", t.style.webkitUserSelect = "", t.style.MozUserSelect = "", t.style.pointerEvents = "", n.style.userSelect = "", n.style.webkitUserSelect = "", n.style.MozUserSelect = "", n.style.pointerEvents = "", e.gutter.style.cursor = "", e.parent.style.cursor = "", L.body.style.cursor = ""
                }.bind(t), B[T]("mouseup", t.stop), B[T]("touchend", t.stop), B[T]("touchcancel", t.stop), B[T]("mousemove", t.move), B[T]("touchmove", t.move), n[T]("selectstart", H), n[T]("dragstart", H), r[T]("selectstart", H), r[T]("dragstart", H), n.style.userSelect = "none", n.style.webkitUserSelect = "none", n.style.MozUserSelect = "none", n.style.pointerEvents = "none", r.style.userSelect = "none", r.style.webkitUserSelect = "none", r.style.MozUserSelect = "none", r.style.pointerEvents = "none", t.gutter.style.cursor = S, t.parent.style.cursor = S, L.body.style.cursor = S, U.call(t), t.dragOffset = x(e) - t.end
            }
        }

        z === q ? (u = "width", t = "clientX", s = "left", o = "right", r = "clientWidth") : "vertical" === z && (u = "height", t = "clientY", s = "top", o = "bottom", r = "clientHeight"), m = O(m);
        var D = [];

        function A(e) {
            var t = e.i === D.length, n = t ? D[e.i - 1] : D[e.i];
            U.call(n);
            var r = t ? n.size - e.minSize - n._c : e.minSize + n._b;
            M.call(n, r)
        }

        function j(e) {
            var s = O(e);
            s.forEach(function (e, t) {
                if (0 < t) {
                    var n = D[t - 1], r = a[n.a], i = a[n.b];
                    r.size = s[t - 1], i.size = e, w(r.element, r.size, n._b), w(i.element, i.size, n._c)
                }
            })
        }

        function F(n, r) {
            D.forEach(function (t) {
                if (!0 !== r ? t.parent.removeChild(t.gutter) : (t.gutter[N]("mousedown", t._a), t.gutter[N]("touchstart", t._a)), !0 !== n) {
                    var e = _(u, t.a.size, t._b);
                    Object.keys(e).forEach(function (e) {
                        a[t.a].element.style[e] = "", a[t.b].element.style[e] = ""
                    })
                }
            })
        }

        return (a = l.map(function (e, t) {
            var n, r, i, s = {element: W(e), size: m[t], minSize: h[t], i: t};
            if (0 < t && ((n = {
                a: t - 1,
                b: t,
                dragging: !1,
                direction: z,
                parent: c
            })._b = Y(g, t - 1 == 0, !1, v), n._c = Y(g, !1, t === l.length - 1, v), "row-reverse" === f || "column-reverse" === f)) {
                var o = n.a;
                n.a = n.b, n.b = o
            }
            if (!I && 0 < t) {
                var a = b(t, z, s.element);
                r = a, i = E(u, g, t), Object.keys(i).forEach(function (e) {
                    r.style[e] = i[e]
                }), n._a = C.bind(n), a[T]("mousedown", n._a), a[T]("touchstart", n._a), c.insertBefore(a, s.element), n.gutter = a
            }
            return w(s.element, s.size, Y(g, 0 === t, t === l.length - 1, v)), 0 < t && D.push(n), s
        })).forEach(function (e) {
            var t = e.element[R]()[u];
            t < e.minSize && (d ? A(e) : e.minSize = t)
        }), I ? {setSizes: j, destroy: F} : {
            setSizes: j, getSizes: k, collapse: function (e) {
                A(a[e])
            }, destroy: F, parent: c, pairs: D
        }
    }
});
//# sourceMappingURL=split.min.js.map
