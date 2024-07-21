layui.define(function (exports) {

    var Player = function (e) {
        var t = {};

        function n(r) {
            if (t[r]) return t[r].exports;
            var a = t[r] = {i: r, l: !1, exports: {}};
            return e[r].call(a.exports, a, a.exports, n), a.l = !0, a.exports
        }

        return n.m = e, n.c = t, n.d = function (e, t, r) {
            n.o(e, t) || Object.defineProperty(e, t, {enumerable: !0, get: r})
        }, n.r = function (e) {
            "undefined" != typeof Symbol && Symbol.toStringTag && Object.defineProperty(e, Symbol.toStringTag, {value: "Module"}), Object.defineProperty(e, "__esModule", {value: !0})
        }, n.t = function (e, t) {
            if (1 & t && (e = n(e)), 8 & t) return e;
            if (4 & t && "object" == typeof e && e && e.__esModule) return e;
            var r = Object.create(null);
            if (n.r(r), Object.defineProperty(r, "default", {
                enumerable: !0,
                value: e
            }), 2 & t && "string" != typeof e) for (var a in e) n.d(r, a, function (t) {
                return e[t]
            }.bind(null, a));
            return r
        }, n.n = function (e) {
            var t = e && e.__esModule ? function () {
                return e.default
            } : function () {
                return e
            };
            return n.d(t, "a", t), t
        }, n.o = function (e, t) {
            return Object.prototype.hasOwnProperty.call(e, t)
        }, n.p = "", n(n.s = 7)
    }([function (e, t, n) {
        "use strict";
        Object.defineProperty(t, "__esModule", {value: !0});
        var r = function () {
                function e(e, t) {
                    for (var n = 0; n < t.length; n++) {
                        var r = t[n];
                        r.enumerable = r.enumerable || !1, r.configurable = !0, "value" in r && (r.writable = !0), Object.defineProperty(e, r.key, r)
                    }
                }

                return function (t, n, r) {
                    return n && e(t.prototype, n), r && e(t, r), t
                }
            }(), a = f(n(9)), i = f(n(2)), o = f(n(26)), l = f(n(5)), s = f(n(3)), u = f(n(27)), c = n(32), p = f(n(33)),
            d = n(4);

        function f(e) {
            return e && e.__esModule ? e : {default: e}
        }

        function g(e, t) {
            if (!e) throw new ReferenceError("this hasn't been initialised - super() hasn't been called");
            return !t || "object" != typeof t && "function" != typeof t ? e : t
        }

        var h = function (e) {
            function t(e) {
                !function (e, t) {
                    if (!(e instanceof t)) throw new TypeError("Cannot call a class as a function")
                }(this, t);
                var n = g(this, (t.__proto__ || Object.getPrototypeOf(t)).call(this, e));
                if (n.config = i.default.deepCopy({
                    width: 600,
                    height: 337.5,
                    ignores: [],
                    whitelist: [],
                    lang: (document.documentElement.getAttribute("lang") || navigator.language || "zh-cn").toLocaleLowerCase(),
                    inactive: 3e3,
                    volume: .6,
                    controls: !0,
                    controlsList: ["nodownload"]
                }, e), n.version = d.version, n.userTimer = null, n.waitTimer = null, n.database = new o.default, n.history = [], n.isProgressMoving = !1, n.root = i.default.findDom(document, "#" + n.config.id), n.controls = i.default.createDom("xg-controls", "", {
                    unselectable: "on",
                    onselectstart: "return false"
                }, "xgplayer-controls"), !n.root) {
                    var r = n.config.el;
                    if (!r || 1 !== r.nodeType) return n.emit("error", new s.default("use", n.config.vid, {
                        line: 32,
                        handle: "Constructor",
                        msg: "container id can't be empty"
                    })), !1, g(n, !1);
                    n.root = r
                }
                if (i.default.addClass(n.root, "xgplayer xgplayer-" + l.default.device + " xgplayer-nostart " + (n.config.controls ? "" : "no-controls")), n.root.appendChild(n.controls), n.config.fluid ? (n.root.style["max-width"] = "100%", n.root.style.width = "100%", n.root.style.height = "0", n.root.style["padding-top"] = 100 * n.config.height / n.config.width + "%", n.video.style.position = "absolute", n.video.style.top = "0", n.video.style.left = "0") : (n.config.width && ("number" != typeof n.config.width ? n.root.style.width = n.config.width : n.root.style.width = n.config.width + "px"), n.config.height && ("number" != typeof n.config.height ? n.root.style.height = n.config.height : n.root.style.height = n.config.height + "px")), n.config.execBeforePluginsCall && n.config.execBeforePluginsCall.forEach(function (e) {
                    e.call(n, n)
                }), n.config.controlStyle && "String" === i.default.typeOf(n.config.controlStyle)) {
                    var a = n;
                    fetch(a.config.controlStyle, {
                        method: "GET",
                        headers: {Accept: "application/json"}
                    }).then(function (e) {
                        e.ok && e.json().then(function (e) {
                            for (var t in e) e.hasOwnProperty(t) && (a.config[t] = e[t]);
                            a.pluginsCall()
                        })
                    }).catch(function (e) {
                        console.log("Fetch错误:" + e)
                    })
                } else n.pluginsCall();
                n.ev.forEach(function (e) {
                    var t = Object.keys(e)[0], r = n[e[t]];
                    r && n.on(t, r)
                }), ["focus", "blur"].forEach(function (e) {
                    n.on(e, n["on" + e.charAt(0).toUpperCase() + e.slice(1)])
                });
                var u = n;
                return n.mousemoveFunc = function () {
                    u.emit("focus"), u.config.closeFocusVideoFocus || u.video.focus()
                }, n.root.addEventListener("mousemove", n.mousemoveFunc), n.playFunc = function () {
                    u.emit("focus"), u.config.closePlayVideoFocus || u.video.focus()
                }, u.once("play", n.playFunc), setTimeout(function () {
                    n.emit("ready")
                }, 0), n.config.keyShortcut && "on" !== n.config.keyShortcut || ["video", "controls"].forEach(function (e) {
                    u[e].addEventListener("keydown", function (e) {
                        u.onKeydown(e, u)
                    })
                }), n.config.videoInit && i.default.hasClass(n.root, "xgplayer-nostart") && n.start(), u.config.rotate && (u.on("requestFullscreen", n.updateRotateDeg), u.on("exitFullscreen", n.updateRotateDeg)), u.once("destroy", function e() {
                    u.root.removeEventListener("mousemove", u.mousemoveFunc), u.off("destroy", e)
                }), n
            }

            return function (e, t) {
                if ("function" != typeof t && null !== t) throw new TypeError("Super expression must either be null or a function, not " + typeof t);
                e.prototype = Object.create(t && t.prototype, {
                    constructor: {
                        value: e,
                        enumerable: !1,
                        writable: !0,
                        configurable: !0
                    }
                }), t && (Object.setPrototypeOf ? Object.setPrototypeOf(e, t) : e.__proto__ = t)
            }(t, a.default), r(t, [{
                key: "start", value: function () {
                    var e = this, n = arguments.length > 0 && void 0 !== arguments[0] ? arguments[0] : this.config.url,
                        r = this.root, a = this;
                    n && "" !== n || this.emit("urlNull"), this.logParams.playSrc = n, this.canPlayFunc = function () {
                        var e = a.video.play();
                        void 0 !== e && e && e.then(function () {
                            a.emit("autoplay started")
                        }).catch(function () {
                            a.emit("autoplay was prevented"), t.util.addClass(a.root, "xgplayer-is-autoplay")
                        }), a.off("canplay", a.canPlayFunc)
                    }, "String" === i.default.typeOf(n) ? this.video.src = n : n.forEach(function (t) {
                        e.video.appendChild(i.default.createDom("source", "", {
                            src: "" + t.src,
                            type: "" + (t.type || "")
                        }))
                    }), this.logParams.pt = (new Date).getTime(), this.logParams.vt = this.logParams.pt, this.loadeddataFunc = function () {
                        a.logParams.vt = (new Date).getTime(), a.logParams.pt > a.logParams.vt && (a.logParams.pt = a.logParams.vt), a.logParams.vd = a.video.duration
                    }, this.once("loadeddata", this.loadeddataFunc), this.config.autoplay && this.on("canplay", this.canPlayFunc), r.insertBefore(this.video, r.firstChild), setTimeout(function () {
                        e.emit("complete")
                    }, 1)
                }
            }, {
                key: "reload", value: function () {
                    this.video.load(), this.reloadFunc = function () {
                        this.play()
                    }, this.once("loadeddata", this.reloadFunc)
                }
            }, {
                key: "destroy", value: function () {
                    var e = this, n = !(arguments.length > 0 && void 0 !== arguments[0]) || arguments[0], r = this;
                    this.root.parentNode;
                    for (var a in clearInterval(this.bulletResizeTimer), this._interval) clearInterval(this._interval[a]), this._interval[a] = null;

                    function i() {
                        for (var e in this.emit("destroy"), this.video.removeAttribute("src"), this.video.load(), n && (this.root.innerHTML = "", this.root.className = ""), this) delete this[e];
                        this.off("pause", i)
                    }

                    this.ev.forEach(function (t) {
                        var n = Object.keys(t)[0], r = e[t[n]];
                        r && e.off(n, r)
                    }), this.loadeddataFunc && this.off("loadeddata", this.loadeddataFunc), this.reloadFunc && this.off("loadeddata", this.reloadFunc), this.replayFunc && this.off("play", this.replayFunc), this.playFunc && this.off("play", this.playFunc), ["focus", "blur"].forEach(function (t) {
                        e.off(t, e["on" + t.charAt(0).toUpperCase() + t.slice(1)])
                    }), this.config.keyShortcut && "on" !== this.config.keyShortcut || ["video", "controls"].forEach(function (t) {
                        e[t] && e[t].removeEventListener("keydown", function (e) {
                            r.onKeydown(e, r)
                        })
                    }), this.paused ? i.call(this) : (this.pause(), this.once("pause", i)), function e(t, n, r) {
                        null === t && (t = Function.prototype);
                        var a = Object.getOwnPropertyDescriptor(t, n);
                        if (void 0 === a) {
                            var i = Object.getPrototypeOf(t);
                            return null === i ? void 0 : e(i, n, r)
                        }
                        if ("value" in a) return a.value;
                        var o = a.get;
                        return void 0 !== o ? o.call(r) : void 0
                    }(t.prototype.__proto__ || Object.getPrototypeOf(t.prototype), "destroy", this).call(this)
                }
            }, {
                key: "replay", value: function () {
                    var e = this, t = this._replay;
                    i.default.removeClass(this.root, "xgplayer-ended"), this.logParams = {
                        bc: 0,
                        bu_acu_t: 0,
                        played: [],
                        pt: (new Date).getTime(),
                        vt: (new Date).getTime(),
                        vd: 0
                    }, this.logParams.pt = (new Date).getTime(), this.logParams.vt = this.logParams.pt, this.replayFunc = function () {
                        e.logParams.vt = (new Date).getTime(), e.logParams.pt > e.logParams.vt && (e.logParams.pt = e.logParams.vt), e.logParams.vd = e.video.duration
                    }, this.once("play", this.replayFunc), this.logParams.playSrc = this.video.currentSrc, t && t instanceof Function ? t() : (this.currentTime = 0, this.play())
                }
            }, {
                key: "getFullscreen", value: function (e) {
                    e.requestFullscreen ? e.requestFullscreen() : e.mozRequestFullScreen ? e.mozRequestFullScreen() : e.webkitRequestFullscreen ? e.webkitRequestFullscreen(Element.ALLOW_KEYBOARD_INPUT) : player.video.webkitSupportsFullscreen ? player.video.webkitEnterFullscreen() : e.msRequestFullscreen ? e.msRequestFullscreen() : i.default.addClass(e, "xgplayer-is-cssfullscreen")
                }
            }, {
                key: "exitFullscreen", value: function (e) {
                    document.exitFullscreen ? document.exitFullscreen() : document.webkitExitFullscreen ? document.webkitExitFullscreen() : document.mozCancelFullScreen ? document.mozCancelFullScreen() : document.msExitFullscreen && document.msExitFullscreen(), i.default.removeClass(e, "xgplayer-is-cssfullscreen")
                }
            }, {
                key: "download", value: function () {
                    var e = (0, c.getAbsoluteURL)(player.config.url);
                    console.log(e), (0, p.default)(e)
                }
            }, {
                key: "pluginsCall", value: function () {
                    var e = this, n = this;
                    if (t.plugins) {
                        var r = this.config.ignores;
                        Object.keys(t.plugins).forEach(function (a) {
                            var i = t.plugins[a];
                            r.some(function (e) {
                                return a === e || a === "s_" + e
                            }) || (["pc", "tablet", "mobile"].some(function (e) {
                                return e === a
                            }) ? a === l.default.device && setTimeout(function () {
                                i.call(n, n)
                            }, 0) : i.call(e, e))
                        })
                    }
                }
            }, {
                key: "getPIP", value: function () {
                    var e = i.default.createDom("xg-pip-lay", "<div></div>", {}, "xgplayer-pip-lay");
                    this.root.appendChild(e);
                    var t = i.default.createDom("xg-pip-drag", '<div class="drag-handle"><span>点击按住可拖动视频</span></div>', {tabindex: 9}, "xgplayer-pip-drag");
                    this.root.appendChild(t);
                    new u.default(".xgplayer", {handle: ".drag-handle"});
                    i.default.addClass(this.root, "xgplayer-pip-active"), this.root.style.right = 0, this.root.style.bottom = "200px", this.root.style.top = "", this.root.style.left = "", this.root.style.width = "320px", this.root.style.height = "180px", this.config.pipConfig && (void 0 !== this.config.pipConfig.top && (this.root.style.top = this.config.pipConfig.top + "px", this.root.style.bottom = ""), void 0 !== this.config.pipConfig.bottom && (this.root.style.bottom = this.config.pipConfig.bottom + "px"), void 0 !== this.config.pipConfig.left && (this.root.style.left = this.config.pipConfig.left + "px", this.root.style.right = ""), void 0 !== this.config.pipConfig.right && (this.root.style.right = this.config.pipConfig.right + "px"), void 0 !== this.config.pipConfig.width && (this.root.style.width = this.config.pipConfig.width + "px"), void 0 !== this.config.pipConfig.height && (this.root.style.height = this.config.pipConfig.height + "px")), this.config.fluid && (this.root.style["padding-top"] = "");
                    var n = this;
                    ["click", "touchend"].forEach(function (t) {
                        e.addEventListener(t, function (e) {
                            e.preventDefault(), e.stopPropagation(), n.exitPIP()
                        })
                    })
                }
            }, {
                key: "exitPIP", value: function () {
                    i.default.removeClass(this.root, "xgplayer-pip-active"), this.root.style.right = "", this.root.style.bottom = "", this.root.style.top = "", this.root.style.left = "", this.config.fluid ? (this.root.style.width = "100%", this.root.style.height = "0", this.root.style["padding-top"] = 100 * this.config.height / this.config.width + "%") : (this.config.width && ("number" != typeof this.config.width ? this.root.style.width = this.config.width : this.root.style.width = this.config.width + "px"), this.config.height && ("number" != typeof this.config.height ? this.root.style.height = this.config.height : this.root.style.height = this.config.height + "px"));
                    var e = i.default.findDom(this.root, ".xgplayer-pip-lay");
                    e && e.parentNode && e.parentNode.removeChild(e);
                    var t = i.default.findDom(this.root, ".xgplayer-pip-drag");
                    t && t.parentNode && t.parentNode.removeChild(t)
                }
            }, {
                key: "updateRotateDeg", value: function () {
                    player.rotateDeg || (player.rotateDeg = 0);
                    var e = player.root.offsetWidth, t = player.root.offsetHeight, n = player.video.videoWidth,
                        r = player.video.videoHeight;
                    player.config.rotate.innerRotate || (player.root.style.width = t + "px", player.root.style.height = e + "px");
                    var a = void 0;
                    if (.25 === player.rotateDeg || .75 === player.rotateDeg) {
                        if (player.config.rotate.innerRotate) if (n / r > t / e) {
                            a = (r / n > t / e ? t * n / r : e) > t ? t > e ? e / t : t / e : t > e ? t / e : e / t
                        } else {
                            a = (r / n > t / e ? t : e * r / n) > e ? t > e ? e / t : t / e : t > e ? t / e : e / t
                        } else a = e >= t ? (e / t).toFixed(2) : (t / e).toFixed(2);
                        a = parseFloat(a.toFixed(5))
                    } else a = 1;
                    player.video.style.transformOrigin = "center center", player.video.style.transform = "rotate(" + player.rotateDeg + "turn) scale(" + a + ")", player.video.style.webKitTransform = "rotate(" + player.rotateDeg + "turn) scale(" + a + ")"
                }
            }, {
                key: "rotate", value: function () {
                    var e = arguments.length > 0 && void 0 !== arguments[0] && arguments[0],
                        t = (!(arguments.length > 1 && void 0 !== arguments[1]) || arguments[1], arguments.length > 2 && void 0 !== arguments[2] ? arguments[2] : 1);
                    player.rotateDeg || (player.rotateDeg = 0);
                    var n = e ? 1 : -1;
                    player.rotateDeg = (player.rotateDeg + 1 + .25 * n * t) % 1, this.updateRotateDeg(), player.emit("rotate", 360 * player.rotateDeg)
                }
            }, {
                key: "onFocus", value: function () {
                    var e = this;
                    i.default.removeClass(this.root, "xgplayer-inactive"), e.userTimer && clearTimeout(e.userTimer), e.userTimer = setTimeout(function () {
                        e.emit("blur")
                    }, e.config.inactive)
                }
            }, {
                key: "onBlur", value: function () {
                    this.paused || this.ended || i.default.addClass(this.root, "xgplayer-inactive")
                }
            }, {
                key: "onPlay", value: function () {
                    i.default.addClass(this.root, "xgplayer-playing"), i.default.removeClass(this.root, "xgplayer-pause")
                }
            }, {
                key: "onPause", value: function () {
                    i.default.addClass(this.root, "xgplayer-pause"), this.userTimer && clearTimeout(this.userTimer), this.emit("focus")
                }
            }, {
                key: "onEnded", value: function () {
                    i.default.addClass(this.root, "xgplayer-ended"), i.default.removeClass(this.root, "xgplayer-playing")
                }
            }, {
                key: "onSeeking", value: function () {
                }
            }, {
                key: "onSeeked", value: function () {
                    this.waitTimer && clearTimeout(this.waitTimer), i.default.removeClass(this.root, "xgplayer-isloading")
                }
            }, {
                key: "onWaiting", value: function () {
                    var e = this;
                    e.waitTimer && clearTimeout(e.waitTimer), e.waitTimer = setTimeout(function () {
                        i.default.addClass(e.root, "xgplayer-isloading")
                    }, 500)
                }
            }, {
                key: "onPlaying", value: function () {
                    this.waitTimer && clearTimeout(this.waitTimer), i.default.removeClass(this.root, "xgplayer-isloading xgplayer-nostart xgplayer-pause xgplayer-ended xgplayer-is-error xgplayer-replay"), i.default.addClass(this.root, "xgplayer-playing")
                }
            }, {
                key: "onKeydown", value: function (e, t) {
                    var n = e || window.event;
                    if (!n || 37 !== n.keyCode && 38 !== n.keyCode && 39 !== n.keyCode && 40 !== n.keyCode && 32 !== n.keyCode || t.emit("focus"), !n || 40 !== n.keyCode && 38 !== n.keyCode) n && 39 === n.keyCode ? t.currentTime + 10 <= t.duration ? t.currentTime += 10 : t.currentTime = t.duration - 1 : n && 37 === n.keyCode ? t.currentTime - 10 >= 0 ? t.currentTime -= 10 : t.currentTime = 0 : n && 32 === n.keyCode && (t.paused ? t.play() : t.pause()); else {
                        if (t.controls) {
                            var r = t.controls.querySelector(".xgplayer-slider");
                            r && (i.default.hasClass(r, "xgplayer-none") && i.default.removeClass(r, "xgplayer-none"), t.sliderTimer && clearTimeout(t.sliderTimer), t.sliderTimer = setTimeout(function () {
                                i.default.addClass(r, "xgplayer-none")
                            }, t.config.inactive))
                        }
                        n && 40 === n.keyCode ? t.volume - .1 >= 0 ? t.volume -= .1 : t.volume = 0 : n && 38 === n.keyCode && (t.volume + .1 <= 1 ? t.volume += .1 : t.volume = 1)
                    }
                }
            }], [{
                key: "install", value: function (e, n) {
                    t.plugins || (t.plugins = {}), t.plugins[e] || (t.plugins[e] = n)
                }
            }, {
                key: "use", value: function (e, n) {
                    t.plugins || (t.plugins = {}), t.plugins[e] = n
                }
            }]), t
        }();
        h.util = i.default, h.sniffer = l.default, h.Errors = s.default, t.default = h, e.exports = t.default
    }, function (e, t, n) {
        "use strict";
        var r = n(18)();
        e.exports = function (e) {
            return e !== r && null !== e
        }
    }, function (module, exports, __webpack_require__) {
        "use strict";
        Object.defineProperty(exports, "__esModule", {value: !0});
        var util = {
            createDom: function () {
                var e = arguments.length > 0 && void 0 !== arguments[0] ? arguments[0] : "div",
                    t = arguments.length > 1 && void 0 !== arguments[1] ? arguments[1] : "",
                    n = arguments.length > 2 && void 0 !== arguments[2] ? arguments[2] : {},
                    r = arguments.length > 3 && void 0 !== arguments[3] ? arguments[3] : "",
                    a = document.createElement(e);
                return a.className = r, a.innerHTML = t, Object.keys(n).forEach(function (t) {
                    var r = t, i = n[t];
                    "video" === e || "audio" === e ? i && a.setAttribute(r, i) : a.setAttribute(r, i)
                }), a
            }, hasClass: function (e, t) {
                return e.classList ? Array.prototype.some.call(e.classList, function (e) {
                    return e === t
                }) : !!e.className.match(new RegExp("(\\s|^)" + t + "(\\s|$)"))
            }, addClass: function (e, t) {
                e.classList ? t.replace(/(^\s+|\s+$)/g, "").split(/\s+/g).forEach(function (t) {
                    t && e.classList.add(t)
                }) : util.hasClass(e, t) || (e.className += " " + t)
            }, removeClass: function (e, t) {
                e.classList ? t.split(/\s+/g).forEach(function (t) {
                    e.classList.remove(t)
                }) : util.hasClass(e, t) && t.split(/\s+/g).forEach(function (t) {
                    var n = new RegExp("(\\s|^)" + t + "(\\s|$)");
                    e.className = e.className.replace(n, " ")
                })
            }, toggleClass: function (e, t) {
                t.split(/\s+/g).forEach(function (t) {
                    util.hasClass(e, t) ? util.removeClass(e, t) : util.addClass(e, t)
                })
            }, findDom: function () {
                var e = arguments.length > 0 && void 0 !== arguments[0] ? arguments[0] : document, t = arguments[1],
                    n = void 0;
                try {
                    n = e.querySelector(t)
                } catch (r) {
                    t.startsWith("#") && (n = e.getElementById(t.slice(1)))
                }
                return n
            }, padStart: function (e, t, n) {
                for (var r = String(n), a = t >> 0, i = Math.ceil(a / r.length), o = [], l = String(e); i--;) o.push(r);
                return o.join("").substring(0, a - l.length) + l
            }, format: function (e) {
                if (window.isNaN(e)) return "";
                var t = util.padStart(Math.floor(e / 3600), 2, 0),
                    n = util.padStart(Math.floor((e - 3600 * t) / 60), 2, 0),
                    r = util.padStart(Math.floor(e - 3600 * t - 60 * n), 2, 0);
                return ("00" === t ? [n, r] : [t, n, r]).join(":")
            }, event: function (e) {
                if (e.touches) {
                    var t = e.touches[0] || e.changedTouches[0];
                    e.clientX = t.clientX || 0, e.clientY = t.clientY || 0, e.offsetX = t.pageX - t.target.offsetLeft, e.offsetY = t.pageY - t.target.offsetTop
                }
                e._target = e.target || e.srcElement
            }, typeOf: function (e) {
                return Object.prototype.toString.call(e).match(/([^\s.*]+)(?=]$)/g)[0]
            }, deepCopy: function (e, t) {
                if ("Object" === util.typeOf(t) && "Object" === util.typeOf(e)) return Object.keys(t).forEach(function (n) {
                    "Object" !== util.typeOf(t[n]) || t[n] instanceof Node ? "Array" === util.typeOf(t[n]) ? e[n] = "Array" === util.typeOf(e[n]) ? e[n].concat(t[n]) : t[n] : e[n] = t[n] : e[n] ? util.deepCopy(e[n], t[n]) : e[n] = t[n]
                }), e
            }, getBgImage: function (e) {
                var t = (e.currentStyle || window.getComputedStyle(e, null)).backgroundImage;
                if (!t || "none" === t) return "";
                var n = document.createElement("a");
                return n.href = t.replace(/url\("|"\)/g, ""), n.href
            }, copyDom: function (e) {
                if (e && 1 === e.nodeType) {
                    var t = document.createElement(e.tagName);
                    return Array.prototype.forEach.call(e.attributes, function (e) {
                        t.setAttribute(e.name, e.value)
                    }), e.innerHTML && (t.innerHTML = e.innerHTML), t
                }
                return ""
            }, setInterval: function (e, t, n, r) {
                e._interval[t] || (e._interval[t] = setInterval(n.bind(e), r))
            }, clearInterval: function (e, t) {
                clearInterval(e._interval[t]), e._interval[t] = null
            }, createImgBtn: function (e, t, n, r) {
                var a = util.createDom("xg-" + e, "", {}, "xgplayer-" + e + "-img");
                if (a.style.backgroundImage = 'url("' + t + '")', n && r) {
                    var i = void 0, o = void 0, l = void 0;
                    ["px", "rem", "em", "pt", "dp", "vw", "vh", "vm", "%"].every(function (e) {
                        return !(n.indexOf(e) > -1 && r.indexOf(e) > -1) || (i = parseFloat(n.slice(0, n.indexOf(e)).trim()), o = parseFloat(r.slice(0, r.indexOf(e)).trim()), l = e, !1)
                    }), a.style.width = "" + i + l, a.style.height = "" + o + l, a.style.backgroundSize = "" + i + l + " " + o + l, a.style.margin = "start" === e ? "-" + o / 2 + l + " auto auto -" + i / 2 + l : "auto 5px auto 5px"
                }
                return a
            }, Hex2RGBA: function (hex, alpha) {
                var rgb = [];
                if (/^\#[0-9A-F]{3}$/i.test(hex)) {
                    var sixHex = "#";
                    hex.replace(/[0-9A-F]/gi, function (e) {
                        sixHex += e + e
                    }), hex = sixHex
                }
                return /^#[0-9A-F]{6}$/i.test(hex) ? (hex.replace(/[0-9A-F]{2}/gi, function (kw) {
                    rgb.push(eval("0x" + kw))
                }), "rgba(" + rgb.join(",") + ", " + alpha + ")") : "rgba(255, 255, 255, 0.1)"
            }
        };
        exports.default = util, module.exports = exports.default
    }, function (e, t, n) {
        "use strict";
        Object.defineProperty(t, "__esModule", {value: !0});
        var r = n(4);
        var a = {
            network: {code: 1, msg: "视频下载错误", remark: "只要视频下载错误就使用此类型，无论是video本身的超时还是xhr的分段请求超时或者资源不存在"},
            mse: {code: 2, msg: "流追加错误", remark: "追加流的时候如果类型不对、无法被正确解码则会触发此类错误"},
            parse: {code: 3, msg: "解析错误", remark: "mp4、hls、flv我们都是使用js进行格式解析，如果解析失败则会触发此类错误"},
            format: {code: 4, msg: "格式错误", remark: "如果浏览器不支持的格式导致播放错误"},
            decoder: {code: 5, msg: "解码错误", remark: "浏览器解码异常会抛出此类型错误"},
            runtime: {code: 6, msg: "语法错误", remark: "播放器语法错误"},
            timeout: {code: 7, msg: "播放超时", remark: "播放过程中无法正常请求下一个分段导致播放中断"},
            other: {code: 8, msg: "其他错误", remark: "不可知的错误或被忽略的错误类型"}
        };
        t.default = function e(t, n, i, o, l, s, u, c) {
            var p = arguments.length > 8 && void 0 !== arguments[8] ? arguments[8] : {
                line: "",
                handle: "",
                msg: "",
                version: ""
            };
            !function (e, t) {
                if (!(e instanceof t)) throw new TypeError("Cannot call a class as a function")
            }(this, e);
            var d = {};
            return d.playerVersion = r.version, d.errorType = t, d.domain = document.domain, d.duration = i, d.currentTime = n, d.networkState = o, d.readyState = l, d.currentSrc = u, d.src = s, d.ended = c, d.errd = p, d.ex = (a[t] || {}).msg, d
        }, e.exports = t.default
    }, function (e) {
        e.exports = {
            name: "xgplayer",
            version: "2.1.2",
            description: "video player",
            main: "./dist/index.js",
            bin: {xgplayer: "bin/xgplayer.js"},
            scripts: {
                prepare: "npm run build",
                build: "webpack --progress --display-chunks -p",
                watch: "webpack --progress --display-chunks -p --watch --mode development"
            },
            keywords: ["video", "player"],
            babel: {presets: ["es2015"], plugins: ["add-module-exports", "babel-plugin-bulk-import"]},
            repository: {type: "git", url: "git+https://github.com/bytedance/xgplayer.git"},
            author: "yinguohui@bytedance.com",
            license: "MIT",
            dependencies: {
                chalk: "^2.3.2",
                commander: "^2.15.1",
                "danmu.js": "0.1.0",
                deepmerge: "^1.5.0",
                downloadjs: "1.4.7",
                draggabilly: "^2.2.0",
                "event-emitter": "^0.3.5",
                "fs-extra": "^5.0.0",
                pasition: "^1.0.1",
                "request-frame": "^1.5.3"
            },
            browserslist: ["> 5%", "IE 9", "iOS 7", "Firefox > 20"],
            devDependencies: {
                autoprefixer: "^9.1.5",
                "babel-core": "^6.26.3",
                "babel-loader": "^7.1.4",
                "babel-plugin-add-module-exports": "^0.2.1",
                "babel-plugin-bulk-import": "^1.0.2",
                "babel-plugin-transform-object-rest-spread": "^6.26.0",
                "babel-plugin-transform-runtime": "^6.23.0",
                "babel-preset-es2015": "^6.24.1",
                chai: "^4.1.2",
                "core-js": "^2.5.4",
                "css-loader": "^0.28.11",
                "json-loader": "^0.5.7",
                "node-sass": "^4.8.3",
                "postcss-cssnext": "^3.1.0",
                "postcss-loader": "^2.1.5",
                "raw-loader": "^2.0.0",
                "sass-loader": "^6.0.7",
                "style-loader": "^0.20.3",
                sugarss: "^1.0.1",
                webpack: "^4.11.0",
                "webpack-cli": "^3.0.2",
                zlib: "^1.0.5"
            }
        }
    }, function (e, t, n) {
        "use strict";
        Object.defineProperty(t, "__esModule", {value: !0});
        var r = {
            get device() {
                return r.os.isPc ? "pc" : "mobile"
            }, get browser() {
                var e = navigator.userAgent.toLowerCase(), t = {
                    ie: /rv:([\d.]+)\) like gecko/,
                    firfox: /firefox\/([\d.]+)/,
                    chrome: /chrome\/([\d.]+)/,
                    opera: /opera.([\d.]+)/,
                    safari: /version\/([\d.]+).*safari/
                };
                return [].concat(Object.keys(t).filter(function (n) {
                    return t[n].test(e)
                }))[0]
            }, get os() {
                var e = navigator.userAgent, t = /(?:Windows Phone)/.test(e), n = /(?:SymbianOS)/.test(e) || t,
                    r = /(?:Android)/.test(e), a = /(?:Firefox)/.test(e),
                    i = /(?:iPad|PlayBook)/.test(e) || r && !/(?:Mobile)/.test(e) || a && /(?:Tablet)/.test(e),
                    o = /(?:iPhone)/.test(e) && !i;
                return {
                    isTablet: i,
                    isPhone: o,
                    isAndroid: r,
                    isPc: !(o || r || n || i),
                    isSymbian: n,
                    isWindowsPhone: t,
                    isFireFox: a
                }
            }
        };
        t.default = r, e.exports = t.default
    }, function (e, t, n) {
        "use strict";
        Object.defineProperty(t, "__esModule", {value: !0});
        var r = "function" == typeof Symbol && "symbol" == typeof Symbol.iterator ? function (e) {
            return typeof e
        } : function (e) {
            return e && "function" == typeof Symbol && e.constructor === Symbol && e !== Symbol.prototype ? "symbol" : typeof e
        }, a = Object.assign || function (e) {
            for (var t = 1; t < arguments.length; t++) {
                var n = arguments[t];
                for (var r in n) Object.prototype.hasOwnProperty.call(n, r) && (e[r] = n[r])
            }
            return e
        };
        var i = void 0, o = function e() {
                var t = this;
                !function (e, t) {
                    if (!(e instanceof t)) throw new TypeError("Cannot call a class as a function")
                }(this, e), this.set = function (e, n) {
                    var r = e, a = n;
                    if (null === a) return !1;
                    var i = "";
                    if (r.indexOf(".") > -1) {
                        var o = r.split(".");
                        i = o[0], r = o[1]
                    }
                    "os_version" === r && (a = "" + a), i ? "user" === i || "header" === i ? t.envInfo[i][r] = a : "headers" === i ? t.envInfo.header.headers[r] = a : t.envInfo.header.headers.custom[r] = a : t.envInfo.user.hasOwnProperty(r) ? ["user_type", "device_id", "ip_addr_id"].indexOf(r) > -1 ? t.envInfo.user[r] = Number(a) : ["user_id", "web_id", "user_unique_id", "ssid"].indexOf(r) > -1 ? t.envInfo.user[r] = String(a) : ["user_is_auth", "user_is_login"].indexOf(r) > -1 && (t.envInfo.user[r] = Boolean(a)) : t.envInfo.header.hasOwnProperty(r) ? t.envInfo.header[r] = a : t.envInfo.header.headers.hasOwnProperty(r) ? t.envInfo.header.headers[r] = a : t.envInfo.header.headers.custom[r] = a
                }, this.get = function () {
                    var e = {user: {}, header: {headers: {custom: {}}}}, n = t.envInfo, r = n.user, o = Object.keys(r),
                        l = Array.isArray(o), s = 0;
                    for (o = l ? o : o[Symbol.iterator](); ;) {
                        var u;
                        if (l) {
                            if (s >= o.length) break;
                            u = o[s++]
                        } else {
                            if ((s = o.next()).done) break;
                            u = s.value
                        }
                        var c = u;
                        r[c] !== i && (e.user[c] = r[c])
                    }
                    var p = n.header, d = Object.keys(p), f = Array.isArray(d), g = 0;
                    for (d = f ? d : d[Symbol.iterator](); ;) {
                        var h;
                        if (f) {
                            if (g >= d.length) break;
                            h = d[g++]
                        } else {
                            if ((g = d.next()).done) break;
                            h = g.value
                        }
                        var y = h;
                        p[y] !== i && "headers" !== y && (e.header[y] = p[y])
                    }
                    var x = n.header.headers, m = Object.keys(x), v = Array.isArray(m), b = 0;
                    for (m = v ? m : m[Symbol.iterator](); ;) {
                        var k;
                        if (v) {
                            if (b >= m.length) break;
                            k = m[b++]
                        } else {
                            if ((b = m.next()).done) break;
                            k = b.value
                        }
                        var w = k;
                        "custom" !== w && x[w] !== i && (e.header.headers[w] = x[w])
                    }
                    var _ = n.header.headers.custom, E = Object.keys(_);
                    if (E.length) {
                        var C = E, S = Array.isArray(C), P = 0;
                        for (C = S ? C : C[Symbol.iterator](); ;) {
                            var T;
                            if (S) {
                                if (P >= C.length) break;
                                T = C[P++]
                            } else {
                                if ((P = C.next()).done) break;
                                T = P.value
                            }
                            var O = T;
                            e.header.headers.custom[O] = _[O]
                        }
                    }
                    return {user: e.user, header: a({}, e.header, {headers: e.header.headers})}
                }, this.envInfo = {
                    user: {
                        user_unique_id: i,
                        user_type: i,
                        user_id: i,
                        user_is_auth: i,
                        user_is_login: i,
                        device_id: i,
                        web_id: i,
                        ip_addr_id: i,
                        ssid: i
                    },
                    header: {
                        app_id: i,
                        app_name: i,
                        app_install_id: i,
                        app_package: i,
                        app_channel: i,
                        app_version: i,
                        os_name: i,
                        os_version: i,
                        device_model: i,
                        ab_client: i,
                        ab_version: i,
                        traffic_type: i,
                        utm_source: i,
                        utm_medium: i,
                        utm_campaign: i,
                        client_ip: i,
                        device_brand: i,
                        os_api: i,
                        access: i,
                        language: i,
                        region: i,
                        app_language: i,
                        app_region: i,
                        creative_id: i,
                        ad_id: i,
                        campaign_id: i,
                        log_type: i,
                        rnd: i,
                        platform: i,
                        sdk_version: i,
                        province: i,
                        city: i,
                        timezone: i,
                        tz_offset: i,
                        tz_name: i,
                        sim_region: i,
                        carrier: i,
                        resolution: i,
                        browser: i,
                        browser_version: i,
                        referrer: i,
                        referrer_host: i,
                        headers: {utm_term: i, utm_content: i, custom: {}}
                    }
                }
            }, l = function (e) {
                var t = document.createElement("a");
                return t.href = e, t
            }, s = screen.width || 0, u = screen.height || 0, c = s + " x " + u, p = navigator.appVersion,
            d = navigator.userAgent, f = navigator.language, g = document.referrer, h = l(g).hostname,
            y = function (e) {
                var t = l(e).search;
                t = t.slice(1);
                var n = {};
                return t.split("&").forEach(function (e) {
                    var t = e.split("="), r = t[0], a = t[1];
                    n[r] = decodeURIComponent(void 0 === a ? "" : a)
                }), n
            }(location.href), x = "", m = "", v = "", b = "" + parseFloat(p), k = void 0, w = void 0;
        -1 !== (k = d.indexOf("Opera")) && (v = "Opera", b = d.substring(k + 6), -1 !== (k = d.indexOf("Version")) && (b = d.substring(k + 8))), -1 !== (k = d.indexOf("Edge")) ? (v = "Microsoft Edge", b = d.substring(k + 5)) : -1 !== (k = d.indexOf("MSIE")) ? (v = "Microsoft Internet Explorer", b = d.substring(k + 5)) : -1 !== (k = d.indexOf("Chrome")) ? (v = "Chrome", b = d.substring(k + 7)) : -1 !== (k = d.indexOf("Safari")) ? (v = "Safari", b = d.substring(k + 7), -1 !== (k = d.indexOf("Version")) && (b = d.substring(k + 8))) : -1 !== (k = d.indexOf("Firefox")) && (v = "Firefox", b = d.substring(k + 8)), -1 !== (w = b.indexOf(";")) && (b = b.substring(0, w)), -1 !== (w = b.indexOf(" ")) && (b = b.substring(0, w)), -1 !== (w = b.indexOf(")")) && (b = b.substring(0, w));
        for (var _, E, C = /Mobile|htc|mini|Android|iP(ad|od|hone)/.test(p) ? "wap" : "web", S = [{
            s: "Windows 10",
            r: /(Windows 10.0|Windows NT 10.0)/
        }, {s: "Windows 8.1", r: /(Windows 8.1|Windows NT 6.3)/}, {
            s: "Windows 8",
            r: /(Windows 8|Windows NT 6.2)/
        }, {s: "Windows 7", r: /(Windows 7|Windows NT 6.1)/}, {s: "Android", r: /Android/}, {
            s: "Sun OS",
            r: /SunOS/
        }, {s: "Linux", r: /(Linux|X11)/}, {s: "iOS", r: /(iPhone|iPad|iPod)/}, {
            s: "Mac OS X",
            r: /Mac OS X/
        }, {s: "Mac OS", r: /(MacPPC|MacIntel|Mac_PowerPC|Macintosh)/}], P = 0; P < S.length; P++) {
            var T = S[P];
            if (T.r.test(d)) {
                x = T.s;
                break
            }
        }

        function O(e, t) {
            var n = e.exec(t);
            return n && n[1] ? n[1] : ""
        }

        switch (/Windows/.test(x) && (m = O(/Windows (.*)/, x), x = "windows"), x) {
            case"Mac OS X":
                m = O(/Mac OS X (10[\.\_\d]+)/, d), x = "mac";
                break;
            case"Android":
                (E = O(/Android ([\.\_\d]+)/, _ = d)) || (E = O(/Android\/([\.\_\d]+)/, _)), m = E, x = "android";
                break;
            case"iOS":
                m = (m = /OS (\d+)_(\d+)_?(\d+)?/.exec(p)) ? m[1] + "." + m[2] + "." + (0 | m[3]) : "", x = "ios"
        }
        var L = {
                screen_size: c,
                browser: v,
                browser_version: b,
                platform: C,
                os_name: x,
                os_version: m,
                userAgent: d,
                screen_width: s,
                screen_height: u,
                device_model: x,
                language: f,
                referrer: g,
                referrer_host: h,
                utm_source: y.utm_source,
                utm_medium: y.utm_medium,
                utm_campaign: y.utm_campaign,
                utm_term: y.utm_term,
                utm_content: y.utm_content
            }, D = {
                get: function (e) {
                    var t = localStorage.getItem(e), n = t;
                    try {
                        t && "string" == typeof t && (n = JSON.parse(t))
                    } catch (e) {
                    }
                    return n
                }, set: function (e, t) {
                    try {
                        var n = "string" == typeof t ? t : JSON.stringify(t);
                        localStorage.setItem(e, n)
                    } catch (e) {
                    }
                }
            }, M = "__tea_cache_", R = 4001, z = 4e3, A = 4002, j = 4003, I = 500, B = 5001,
            N = "function" == typeof Symbol && "symbol" === r(Symbol.iterator) ? function (e) {
                return void 0 === e ? "undefined" : r(e)
            } : function (e) {
                return e && "function" == typeof Symbol && e.constructor === Symbol && e !== Symbol.prototype ? "symbol" : void 0 === e ? "undefined" : r(e)
            };
        var q = function e() {
            var t = this, n = arguments.length > 0 && void 0 !== arguments[0] ? arguments[0] : "";
            !function (e, t) {
                if (!(e instanceof t)) throw new TypeError("Cannot call a class as a function")
            }(this, e), this.init = function (e) {
                t.isLog = e
            }, this.info = function (e) {
                for (var n = arguments.length, r = Array(n > 1 ? n - 1 : 0), a = 1; a < n; a++) r[a - 1] = arguments[a];
                var i;
                t.isLog && (i = console).log.apply(i, [t.prefix + e].concat(r))
            }, this.warn = function (e) {
                for (var n = arguments.length, r = Array(n > 1 ? n - 1 : 0), a = 1; a < n; a++) r[a - 1] = arguments[a];
                var i;
                t.isLog && (i = console).warn.apply(i, [t.prefix + e].concat(r))
            }, this.error = function (e) {
                for (var n = arguments.length, r = Array(n > 1 ? n - 1 : 0), a = 1; a < n; a++) r[a - 1] = arguments[a];
                var i;
                t.isLog && (i = console).error.apply(i, [t.prefix + e].concat(r))
            }, this.dir = function () {
                var e;
                t.isLog && (e = console).dir.apply(e, arguments)
            }, this.table = function (e) {
                t.isLog && console.table(e)
            }, this.logJSON = function (e) {
                "object" === (void 0 === e ? "undefined" : N(e)) && t.isLog && t.info("", JSON.stringify(e, null, 2))
            }, this.deprecated = function (e) {
                for (var n = arguments.length, r = Array(n > 1 ? n - 1 : 0), a = 1; a < n; a++) r[a - 1] = arguments[a];
                t.warn.apply(t, ["[DEPRECATED]" + e].concat(r))
            }, this.throw = function (e) {
                throw t.error(t.prefix), new Error(e)
            };
            var r = n ? "[" + n + "]" : "";
            this.prefix = "[tea-sdk]" + r
        }, F = new q, U = function (e, t, n, r) {
            var a = new XMLHttpRequest;
            a.open("POST", e, !0), a.setRequestHeader("Content-Type", "application/json; charset=utf-8"), a.onload = function () {
                try {
                    var e = JSON.parse(a.responseText);
                    n && n(e)
                } catch (e) {
                    r && r()
                }
            }, a.onerror = function () {
                r && r()
            }, a.send(JSON.stringify(t))
        };
        var H = (new Date).getTimezoneOffset(), W = parseInt(-H / 60, 10), Y = 60 * H, V = void 0;
        try {
            V = "3.2.7"
        } catch (_) {
            V = "2.x"
        }
        var K = new (function (e) {
            function t() {
                !function (e, t) {
                    if (!(e instanceof t)) throw new TypeError("Cannot call a class as a function")
                }(this, t);
                var n = function (e, t) {
                    if (!e) throw new ReferenceError("this hasn't been initialised - super() hasn't been called");
                    return !t || "object" !== (void 0 === t ? "undefined" : r(t)) && "function" != typeof t ? e : t
                }(this, e.call(this));
                return n.initClientEnv = function () {
                    n.set("os_name", L.os_name), n.set("os_version", L.os_version), n.set("device_model", L.device_model), n.set("platform", L.platform), n.set("sdk_version", V), n.set("browser", L.browser), n.set("browser_version", L.browser_version), n.set("language", L.language), n.set("timezone", W), n.set("tz_offset", Y), n.set("resolution", L.screen_width + "x" + L.screen_height), n.set("screen_width", L.screen_width), n.set("screen_height", L.screen_height), n.set("referrer", L.referrer), n.set("referrer_host", L.referrer_host), n.set("utm_source", L.utm_source), n.set("utm_medium", L.utm_medium), n.set("utm_campaign", L.utm_campaign), n.set("utm_term", L.utm_term), n.set("utm_content", L.utm_content)
                }, n.initClientEnv(), n
            }

            return function (e, t) {
                if ("function" != typeof t && null !== t) throw new TypeError("Super expression must either be null or a function, not " + (void 0 === t ? "undefined" : r(t)));
                e.prototype = Object.create(t && t.prototype, {
                    constructor: {
                        value: e,
                        enumerable: !1,
                        writable: !0,
                        configurable: !0
                    }
                }), t && (Object.setPrototypeOf ? Object.setPrototypeOf(e, t) : e.__proto__ = t)
            }(t, e), t
        }(o));
        var X = new (function () {
            function e() {
                !function (e, t) {
                    if (!(e instanceof t)) throw new TypeError("Cannot call a class as a function")
                }(this, e)
            }

            return e.prototype.isString = function (e) {
                return "String" === Object.prototype.toString.call(e).slice(8, -1)
            }, e.prototype.isNumber = function (e) {
                return "Number" === Object.prototype.toString.call(e).slice(8, -1)
            }, e.prototype.isBoolean = function (e) {
                return "Boolean" === Object.prototype.toString.call(e).slice(8, -1)
            }, e.prototype.isFunction = function (e) {
                return "Function" === Object.prototype.toString.call(e).slice(8, -1)
            }, e.prototype.isNull = function (e) {
                return "Null" === Object.prototype.toString.call(e).slice(8, -1)
            }, e.prototype.isUndefined = function (e) {
                return "Undefined" === Object.prototype.toString.call(e).slice(8, -1)
            }, e.prototype.isObj = function (e) {
                return "Object" === Object.prototype.toString.call(e).slice(8, -1)
            }, e.prototype.isArray = function (e) {
                return "Array" === Object.prototype.toString.call(e).slice(8, -1)
            }, e.prototype.isFalse = function (e) {
                return "" === e || null == e || "null" === e || "undefined" === e || 0 === e || !1 === e || NaN === e
            }, e.prototype.isTrue = function (e) {
                return !this.isFalse(e)
            }, e.prototype.isLowIE = function () {
                return window.XDomainRequest
            }, e
        }());
        var J = function (e) {
            return function (e, t, n) {
                if ("string" == typeof e && "number" == typeof t && "number" == typeof n) {
                    var r, a = [];
                    n = n <= 25 ? n : n % 25;
                    var i = String.fromCharCode(n + 97);
                    r = e.split(i);
                    for (var o = 0; o < r.length; o++) {
                        var l = parseInt(r[o], n);
                        l = 1 * l ^ t;
                        var s = String.fromCharCode(l);
                        a.push(s)
                    }
                    return a.join("")
                }
            }(e, 64, 25)
        };

        function $(e) {
            return e ? (e ^ 16 * Math.random() >> e / 4).toString(10) : ([1e7] + -1e3 + -4e3 + -8e3 + -1e11).replace(/[018]/g, $)
        }

        var Q = function () {
            return $().replace(/-/g, "").slice(0, 19)
        }, G = Object.assign || function (e) {
            for (var t = 1; t < arguments.length; t++) {
                var n = arguments[t];
                for (var r in n) Object.prototype.hasOwnProperty.call(n, r) && (e[r] = n[r])
            }
            return e
        };
        var Z = {
            cn: "1fz22z22z1nz21z4mz4bz4bz1kz1az21z4az21z1lz21z21z1bz1iz4az1az1mz1k",
            sg: "1fz22z22z1nz21z4mz4bz4bz21z1ez18z1jz1gz49z1kz1az21z4az19z27z22z1cz1mz24z1cz20z21z1cz18z4az1az1mz1k",
            va: "1fz22z22z1nz21z4mz4bz4bz1kz18z1jz1gz24z18z49z1kz1az21z4az19z27z22z1cz1mz24z1cz20z21z1cz18z4az1az1mz1k"
        }, ee = function (e) {
            try {
                var t = document.cookie.match(new RegExp("(?:^|;)\\s*" + e + "=([^;]+)"));
                return decodeURIComponent(t ? t[1] : "")
            } catch (e) {
                return ""
            }
        }, te = function (e) {
            function t() {
                !function (e, t) {
                    if (!(e instanceof t)) throw new TypeError("Cannot call a class as a function")
                }(this, t);
                var n = function (e, t) {
                    if (!e) throw new ReferenceError("this hasn't been initialised - super() hasn't been called");
                    return !t || "object" !== (void 0 === t ? "undefined" : r(t)) && "function" != typeof t ? e : t
                }(this, e.call(this));
                return n.init = function (e) {
                    var t = e.app_id, r = e.channel, a = e.log, i = e.channel_domain, o = e.name;
                    if ("number" != typeof t) throw new Error("app_id 必须是一个数字，注意检查是否是以`string`的方式传入的？");
                    n.logger = new q(o), n.logger.init(a), n.initConfigs(e), n.initUrls(r, i), n.setEnv("app_id", t)
                }, n.initConfigs = function (e) {
                    var t = e.app_id, r = e.disable_ssid, a = e.disable_webid, i = e.disable_sdk_monitor;
                    n.app_id = t, n.evtDataCacheKey = M + "events_" + t, r && (n.logger.info("ssid已禁用，设置user_unique_id不会请求ssid接口。"), n.isSsidDisabled = !0), a && (n.logger.info("webid服务已禁用，ssid同时被禁用。将本地生成webid。"), n.isWebidDisabled = !0, n.isSsidDisabled = !0), i && (n.logger.info("SDK监控已禁用。"), n.isSdkMonitorDisabled = !0)
                }, n.initUrls = function (e, t) {
                    if ("internal" === e && (n.logger.warn("channel 的值 internal 已被废弃，已自动改为 cn。"), e = "cn"), !t && !Z[e]) throw new Error("channel 变量只能是 `cn`, `sg`,`va`");
                    var r = t || J(Z[e]);
                    r = r.replace(/\/+$/, ""), n.reportUrl = r + "/v1/list", n.userTokensPrefix = "" + r
                }, n.setEnv = function (e, t) {
                    if ("app_id" === e && n.checkUserToken(t), "user_unique_id" === e) {
                        if (n.blackUuid.some(function (e) {
                            return e === String(t)
                        })) return void n.logger.warn('设置了无效的值 {user_unique_id："%s"}。该操作已忽略。', t);
                        n.verifyTokens(t)
                    }
                    if ("web_id" === e) {
                        if (!t) return;
                        (!n.envInfo.user.user_unique_id || n.envInfo.user.user_unique_id && n.envInfo.user.user_unique_id === n.envInfo.user.web_id) && n.set("user_unique_id", t)
                    }
                    n.set(e, t)
                }, n.transferFromCookie = function () {
                    var e = n.tokensCacheKey, t = ee("tt_webid"), r = ee("__tea_sdk__ssid"),
                        a = ee("__tea_sdk__user_unique_id");
                    if (X.isLowIE()) {
                        if (t) {
                            var i = {web_id: t, ssid: t, user_unique_id: t};
                            D.set(e, JSON.stringify(i))
                        }
                        return !1
                    }
                    if (t && r && a) {
                        var o = {web_id: t, ssid: r, user_unique_id: a};
                        D.set(e, JSON.stringify(o))
                    }
                }, n.purifyBlackUuid = function () {
                    var e = arguments.length > 0 && void 0 !== arguments[0] ? arguments[0] : {};
                    if (n.blackUuid.some(function (t) {
                        return t === e.user_unique_id
                    })) {
                        var t = {};
                        return n.setUserTokens(t), n.logger.warn('检测到无效的用户标识，已重置用户状态。{user_unique_id: "%s"}', e.user_unique_id), t
                    }
                    return e
                }, n.getUserTokens = function () {
                    return D.get(n.tokensCacheKey) || {}
                }, n.setUserTokens = function () {
                    var e = arguments.length > 0 && void 0 !== arguments[0] ? arguments[0] : {};
                    return D.set(n.tokensCacheKey, e)
                }, n.checkUserToken = function (e) {
                    var t = M + "tokens_" + e;
                    n.tokensCacheKey = t, n.transferFromCookie();
                    var r = n.purifyBlackUuid(n.getUserTokens());
                    r.user_unique_id && r.web_id ? (n.envInfo.user.user_unique_id = r.user_unique_id, n.envInfo.user.web_id = r.web_id, n.envInfo.user.ssid = r.ssid || "", n.logger.info("初始化已经检测到了 webid user_unique_id，一般情况下不需要再次验证 id 了"), n.unlock()) : n.requestWebId(e)
                }, n.saveTokenToStorage = function (e) {
                    var t = e.web_id, r = e.ssid, a = e.user_unique_id;
                    n.setUserTokens({web_id: t, ssid: r, user_unique_id: a})
                }, n.requestWebId = function () {
                    n.isRequestWebId = !0;
                    var e = function (e) {
                        var t = n.envInfo.user.web_id || e.web_id, r = e.ssid;
                        n.isRequestWebId = !1, n.envInfo.user.ssid = r, n.envInfo.user.web_id = t, n.envInfo.user.user_unique_id = t, n.saveTokenToStorage({
                            web_id: t,
                            ssid: r,
                            user_unique_id: t
                        }), n.waitForVerifyTokens ? (n.lock(), n.verifyTokens(n.realUuid)) : (n.unlock(), n.callback && n.callback())
                    };
                    n.isWebidDisabled ? e({web_id: Q(), ssid: ""}) : function () {
                        var t = n.userTokensPrefix + "/v1/user/webid";
                        U(t, {
                            app_id: n.app_id,
                            url: location.href,
                            user_agent: L.userAgent,
                            referer: L.referrer,
                            user_unique_id: ""
                        }, function (t) {
                            0 !== t.e ? n.logger.error("请求 webid 失败。请联系管理员。") : e(t)
                        }, function () {
                            n.isRequestWebId = !1, n.logger.error("获取 webid 失败，数据将不会被上报")
                        })
                    }()
                }, n.verifyTokens = function (e) {
                    var t = n.tokensCacheKey;
                    if (n.waitForVerifyTokens = !1, n.realUuid = "" + e, n.isRequestWebId) return n.waitForVerifyTokens = !0, n.logger.info("正在请求 webid，requestSsid 将会在前者请求完毕之后被调用"), !1;
                    var r = n.getUserTokens();
                    if (r.user_unique_id === n.realUuid && r.ssid && r.web_id) n.logger.info("传入的 user_id/user_unique_id 与 缓存中的完全一致，无需再次请求"), n.unlock(); else {
                        n.lock(), n.envInfo.user.user_unique_id = n.realUuid;
                        var a = G({}, n.getUserTokens(), {user_unique_id: n.realUuid});
                        if (D.set(t, JSON.stringify(a)), X.isLowIE()) return n.unlock(), !1;
                        n.isSsidDisabled ? (n.unlock(), n.callback && n.callback()) : n.requestSsid()
                    }
                }, n.requestSsid = function () {
                    var e = n.getUserTokens(), t = n.userTokensPrefix + "/v1/user/ssid";
                    U(t, {app_id: n.app_id, web_id: e.web_id, user_unique_id: "" + e.user_unique_id}, function (t) {
                        if (n.unlock(), 0 !== t.e) n.logger.error("请求 ssid 失败~"); else {
                            n.envInfo.user.ssid = t.ssid;
                            var r = G({}, e, {ssid: t.ssid});
                            n.setUserTokens(r), n.logger.info("根据 user_unique_id 更新 ssid 成功！注意：在这之前不应该有数据被发出去"), n.callback && n.callback()
                        }
                    }, function () {
                        n.unlock(), n.logger.error("根据 user_unique_id 获取新 ssid 失败")
                    })
                }, n.setEvtParams = function (e) {
                    var t = G({}, e);
                    Object.keys(t).forEach(function (e) {
                        n.evtParams[e] = t[e]
                    })
                }, n.mergeEnvToEvents = function (e) {
                    var t = n.mergeEnv(), r = [], a = 0, i = void 0;
                    return e.forEach(function (e) {
                        var t = !!e.params.__disable_storage__;
                        void 0 === i ? i = t : (t !== i || r[a].length >= 5) && (a += 1, i = !i), r[a] = r[a] || [], r[a].push(e)
                    }), r.map(function (e) {
                        return {
                            events: e.map(function (e) {
                                var t = G({}, n.evtParams, e.params);
                                return delete t.__disable_storage__, G({}, e, {params: JSON.stringify(t)})
                            }),
                            user: t.user,
                            header: t.header,
                            verbose: n.debugMode ? 1 : void 0,
                            __disable_storage__: e[0].params.__disable_storage__
                        }
                    })
                }, n.mergeEnv = function () {
                    var e = n.get(), t = K.get(), r = G({}, e.user),
                        a = G({}, t.header.headers.custom, e.header.headers.custom),
                        i = G({}, t.header.headers, e.header.headers, {custom: a}), o = G({}, t.header, e.header);
                    return {user: r, header: G({}, o, {headers: JSON.stringify(i)})}
                }, n.evtParams = {}, n.reportUrl = "", n.userTokensPrefix = "", n.isSsidDisabled = !1, n.isWebidDisabled = !1, n.isSdkMonitorDisabled = !1, n.debugMode = !1, n.blackUuid = ["null", "undefined", "0", "", "None"], n.logger = function () {
                }, n
            }

            return function (e, t) {
                if ("function" != typeof t && null !== t) throw new TypeError("Super expression must either be null or a function, not " + (void 0 === t ? "undefined" : r(t)));
                e.prototype = Object.create(t && t.prototype, {
                    constructor: {
                        value: e,
                        enumerable: !1,
                        writable: !0,
                        configurable: !0
                    }
                }), t && (Object.setPrototypeOf ? Object.setPrototypeOf(e, t) : e.__proto__ = t)
            }(t, e), t.prototype.lock = function () {
                this.isUserTokensReady = !1
            }, t.prototype.unlock = function () {
                this.isUserTokensReady = !0
            }, t.prototype.enableDebugMode = function (e) {
                this.debugMode = e
            }, t
        }(o);
        var ne = function e() {
            var t = this;
            !function (e, t) {
                if (!(e instanceof t)) throw new TypeError("Cannot call a class as a function")
            }(this, e), this.set = function (e, n) {
                t.cache[e] = n
            }, this.get = function (e) {
                return t.cache[e]
            }, this.clean = function (e) {
                t.cache[e] = void 0
            }, this.cache = {}
        }, re = new ne;
        var ae = function () {
            function e(t) {
                var n = t.disable_storage, r = void 0 !== n && n;
                !function (e, t) {
                    if (!(e instanceof t)) throw new TypeError("Cannot call a class as a function")
                }(this, e), this._isPersistent = !r, this._storage = this._isPersistent ? D : new ne, this._storageKey = "", this._data = void 0
            }

            return e.prototype.setStorageKey = function (e) {
                this._storageKey = e
            }, e.prototype.getAllEvents = function () {
                var e = this.getData();
                Object.keys(e).reduce(function (t, n) {
                    return t.concat(e[n] || [])
                }, [])
            }, e.prototype.getData = function () {
                return this._checkIsDataInit(), this._data
            }, e.prototype.add = function (e) {
                var t = arguments.length > 1 && void 0 !== arguments[1] ? arguments[1] : [];
                this._checkIsDataInit(), 0 !== t.length && (this._data[e] = t, this._save())
            }, e.prototype.delete = function (e) {
                this._checkIsDataInit(), this._data[e] && (delete this._data[e], this._save())
            }, e.prototype._checkIsDataInit = function () {
                if (void 0 === this._data) try {
                    var e, t = this._getDataFromStorage();
                    if (X.isArray(t)) this._data = ((e = {})[Q()] = t, e), this._save(); else this._data = t
                } catch (e) {
                    this._data = {}
                }
            }, e.prototype._checkStorageKey = function () {
                if (!this._storageKey) throw new Error("must call setStorageKey('xxx') first")
            }, e.prototype._getDataFromStorage = function () {
                return this._checkStorageKey(), this._storage.get(this._storageKey) || {}
            }, e.prototype._save = function () {
                this._checkStorageKey(), this._storage.set(this._storageKey, this._data)
            }, e
        }(), ie = function (e, t) {
            try {
                var n = e.split("v1")[0];
                t.forEach(function (e) {
                    var t = function (e) {
                        var t = "";
                        for (var n in e) e.hasOwnProperty(n) && (t += "&" + n + "=" + encodeURIComponent(JSON.stringify(e[n])));
                        return t = "&" === t[0] ? t.slice(1) : t
                    }(e), r = new Image(1, 1);
                    r.onload = function () {
                        r = null
                    }, r.onerror = function () {
                        r = null
                    }, r.src = n + "/v1/gif?" + t
                })
            } catch (e) {
            }
        }, oe = function (e, t) {
            if (window.XDomainRequest) return ie(e, t);
            var n = new XMLHttpRequest;
            n.open("POST", e + "?rdn=" + Math.random(), !0), n.onload = function () {
            }, n.onerror = function () {
                n.abort()
            }, n.send(JSON.stringify(t))
        }, le = function e(t, n, r, a) {
            try {
                var i = t.split("v1")[0];
                if (!i) return void a(t, n, R);
                n.forEach(function (e) {
                    var o = function (e) {
                        var t = "";
                        for (var n in e) e.hasOwnProperty(n) && (t += "&" + n + "=" + encodeURIComponent(JSON.stringify(e[n])));
                        return t = "&" === t[0] ? t.slice(1) : t
                    }(e), l = new Image(1, 1);
                    l.onload = function () {
                        l = null, r()
                    }, l.onerror = function () {
                        l = null, a(t, n, z)
                    }, l.src = i + "/v1/gif?" + o
                })
            } catch (e) {
                a(t, n, A, e.message)
            }
        };
        var se = function e(t) {
            var n = this;
            !function (e, t) {
                if (!(e instanceof t)) throw new TypeError("Cannot call a class as a function")
            }(this, e), this.send = function (e) {
                var t = e.url, r = e.data, a = e.success, i = e.fail, o = e.eventError;
                if (function (e) {
                    var t = e.url, n = e.data, r = e.success, a = e.fail, i = e.notSure, o = e.isUnload, l = n;
                    if (window.XDomainRequest) le(t, l, r, a); else {
                        if (o) return window.navigator && window.navigator.sendBeacon ? (i(), void(window.navigator.sendBeacon(t, JSON.stringify(l)) ? r() : a(t, n, j))) : void le(t, l, r, a);
                        var s = new XMLHttpRequest;
                        s.open("POST", t + "?rdn=" + Math.random(), !0), s.onload = function () {
                            r(t, l, s.responseText)
                        }, s.onerror = function () {
                            s.abort(), a(t, l, I)
                        }, s.send(JSON.stringify(l))
                    }
                }({
                    url: t, data: r, success: function (e, t, r) {
                        a();
                        try {
                            var i = JSON.parse(r).e;
                            if (0 !== i) {
                                var l = "未知错误";
                                -2 === i && (l = "事件格式错误！请检查字段类型是否正确。"), n.logger.error("数据上报失败！", "错误码：" + i + "。错误信息：" + l), o(t, i), ce(e, t, i)
                            }
                        } catch (n) {
                            ce(e, t, B)
                        }
                    }, fail: function (e, t, r) {
                        n.logger.error("数据上报失败！", "错误码：" + r), i(t, r), ce(e, t, r)
                    }, notSure: e.notSure, isUnload: e.isUnload
                }), !n.isSdkMonitorDisabled && !n.isSdkOnLoadEventReady) {
                    n.isSdkOnLoadEventReady = !0;
                    try {
                        var l = r[0].header, s = r[0].user;
                        ue(t, {app_id: l.app_id, app_name: l.app_name, sdk_version: l.sdk_version, web_id: s.web_id})
                    } catch (e) {
                    }
                }
            }, this.logger = t.logger || F, this.isSdkOnLoadEventReady = !1, this.isSdkMonitorDisabled = !1
        }, ue = function (e, t) {
            try {
                var n = {
                    events: [{
                        event: "onload",
                        params: JSON.stringify({
                            app_id: t.app_id,
                            app_name: t.app_name || "",
                            sdk_version: t.sdk_version
                        }),
                        local_time_ms: Date.now()
                    }], user: {user_unique_id: t.web_id}, header: {app_id: 1338}
                };
                setTimeout(function () {
                    oe(e, [n])
                }, 16)
            } catch (e) {
            }
        }, ce = function (e, t, n) {
            try {
                var r = t[0].user, a = t[0].header, i = [];
                t.forEach(function (e) {
                    e.events.forEach(function (e) {
                        i.push(e)
                    })
                });
                var o = {
                    events: i.map(function (e) {
                        return {
                            event: "on_error",
                            params: JSON.stringify({
                                error_code: n,
                                app_id: a.app_id,
                                app_name: a.app_name || "",
                                error_event: e.event,
                                local_time_ms: e.local_time_ms,
                                tea_event_index: Date.now(),
                                params: e.params,
                                header: JSON.stringify(a),
                                user: JSON.stringify(r)
                            }),
                            local_time_ms: Date.now()
                        }
                    }), user: {user_unique_id: r.user_unique_id}, header: {app_id: 1338}
                };
                setTimeout(function () {
                    oe(e, [o])
                }, 16)
            } catch (e) {
            }
        };
        var pe = function (e) {
            function t(n) {
                !function (e, t) {
                    if (!(e instanceof t)) throw new TypeError("Cannot call a class as a function")
                }(this, t);
                var a = function (e, t) {
                    if (!e) throw new ReferenceError("this hasn't been initialised - super() hasn't been called");
                    return !t || "object" !== (void 0 === t ? "undefined" : r(t)) && "function" != typeof t ? e : t
                }(this, e.call(this));
                a.addListener = function () {
                    window.addEventListener("unload", function () {
                        a.report(!0)
                    }, !1), window.addEventListener("beforeunload", function () {
                        a.report(!0)
                    }, !1), document.addEventListener("visibilitychange", function () {
                        "hidden" === document.visibilityState && a.report(!0)
                    }, !1)
                }, a.setReady = function (e) {
                    a.isReady = e, a.eventSender.isSdkMonitorDisabled = a.isSdkMonitorDisabled, a.checkAndSendCachedStorageEvents(), a.report()
                }, a.eventReportTimer = null, a.event = function () {
                    var e = arguments.length > 0 && void 0 !== arguments[0] ? arguments[0] : [],
                        t = arguments.length > 1 && void 0 !== arguments[1] && arguments[1],
                        n = re.get(a.evtDataCacheKey) || [], r = t ? [].concat(e, n) : [].concat(n, e);
                    re.set(a.evtDataCacheKey, r), r.length >= 5 ? a.report() : (a.eventReportTimer && clearTimeout(a.eventReportTimer), a.eventReportTimer = setTimeout(function () {
                        a.report(), a.eventReportTimer = null
                    }, a.waitForBatchTime))
                }, a.report = function () {
                    var e = arguments.length > 0 && void 0 !== arguments[0] && arguments[0];
                    if (!a.isUserTokensReady) return !1;
                    if (!a.isReady) return !1;
                    var t = re.get(a.evtDataCacheKey) || [];
                    re.clean(a.evtDataCacheKey);
                    var n = a.mergeEnvToEvents(t);
                    a.sendData(n, e)
                }, a.sendData = function (e, t) {
                    var n = [], r = 0, i = void 0;
                    e.forEach(function (e) {
                        var t = !!e.__disable_storage__;
                        void 0 === i ? i = t : (t !== i || n[r].length >= 5) && (r += 1, i = !i), n[r] = n[r] || [], n[r].push(e)
                    }), n.forEach(function (e) {
                        var n = Q();
                        e[0].__disable_storage__ || a.eventStorage.add(n, e), a._sendData(n, e, t)
                    })
                }, a.checkAndSendCachedStorageEvents = function () {
                    var e = a.eventStorage.getData(), t = Object.keys(e);
                    t.length > 0 && t.forEach(function (t) {
                        a._sendData(t, e[t])
                    })
                }, a._sendData = function (e, t, n) {
                    a.isReporting = !0;
                    var r = function () {
                        a.isReporting = !1
                    };
                    a.eventSender.send({
                        url: a.reportUrl, data: t, success: function () {
                            r(), a.sendDataSuccess(e)
                        }, fail: function (e, t) {
                            r(), a.reportErrorCallback(e, t), setTimeout(function () {
                                a.report()
                            }, 3e3)
                        }, eventError: function (e, t) {
                            a.reportErrorCallback(e, t)
                        }, notSure: r, isUnload: n
                    })
                }, a.sendDataSuccess = function (e) {
                    a.eventStorage.delete(e), a.report()
                };
                var i = n.log, o = n.disable_storage, l = n.max_batch_num, s = void 0 === l ? 5 : l, u = n.batch_time,
                    c = void 0 === u ? 30 : u;
                return a.init(n), a.maxBatchNum = s, a.waitForBatchTime = c, a.isReady = !1, a.addListener(), a.enableDebugMode(!!i), a.eventStorage = new ae({disable_storage: o}), a.eventStorage.setStorageKey(a.evtDataCacheKey), a.eventSender = new se({logger: a.logger}), a.reportErrorCallback = function () {
                }, a
            }

            return function (e, t) {
                if ("function" != typeof t && null !== t) throw new TypeError("Super expression must either be null or a function, not " + (void 0 === t ? "undefined" : r(t)));
                e.prototype = Object.create(t && t.prototype, {
                    constructor: {
                        value: e,
                        enumerable: !1,
                        writable: !0,
                        configurable: !0
                    }
                }), t && (Object.setPrototypeOf ? Object.setPrototypeOf(e, t) : e.__proto__ = t)
            }(t, e), t
        }(te), de = Object.assign || function (e) {
            for (var t = 1; t < arguments.length; t++) {
                var n = arguments[t];
                for (var r in n) Object.prototype.hasOwnProperty.call(n, r) && (e[r] = n[r])
            }
            return e
        };
        var fe = function () {
            var e = +Date.now() + Number(("" + Math.random()).slice(2, 8));
            return function () {
                return e += 1
            }
        }(), ge = function e(t) {
            var n = this;
            !function (e, t) {
                if (!(e instanceof t)) throw new TypeError("Cannot call a class as a function")
            }(this, e), this.init = function (e) {
                if (!X.isObj(e)) throw new Error("init 的参数必须是Object类型");
                n.logger.init(e.log), n.channel = new pe(de({}, e, {name: n.name})), n.channel.callback = function () {
                    n.callbackSend && n.start()
                }
            }, this.config = function (e) {
                X.isObj(e) || n.logger.throw("config 参数必须是 {} 的格式"), e.log && (n.logger.init(!0), n.channel.enableDebugMode(!0), e.log = null);
                var t = Object.keys(e);
                if (!t.length) return !1;
                var r = t, a = Array.isArray(r), i = 0;
                for (r = a ? r : r[Symbol.iterator](); ;) {
                    var o;
                    if (a) {
                        if (i >= r.length) break;
                        o = r[i++]
                    } else {
                        if ((i = r.next()).done) break;
                        o = i.value
                    }
                    var l = o, s = e[l];
                    switch (l) {
                        case"evtParams":
                            n.channel.setEvtParams(s);
                            break;
                        case"disable_ssid":
                            n.logger.deprecated("(disable_ssid)请通过init函数来设置。"), s && (n.logger.info("ssid已禁用，设置user_unique_id不会请求ssid接口。"), n.channel.isSsidDisabled = s);
                            break;
                        case"disable_auto_pv":
                            s && (n.logger.info("已禁止默认上报predefine_pageview事件，需手动上报。"), n._autoSendPV = !1);
                            break;
                        case"_staging_flag":
                            "" + s == "1" && n.logger.info("根据_staging_flag设置，数据将会上报到stag 表。"), n.channel.setEvtParams({_staging_flag: Number(s)});
                            break;
                        case"reportErrorCallback":
                            "function" == typeof s && (n.channel.reportErrorCallback = s);
                            break;
                        default:
                            n.channel.setEnv(l, s)
                    }
                }
            }, this.send = function () {
                n.start()
            }, this.start = function () {
                if (n.channel.isUserTokensReady) {
                    if (n._isSendFuncCalled) return;
                    n._isSendFuncCalled = !0, n.logger.info("看到本提示，意味着用户信息已完全就绪，上报通道打开。用户标识如下："), n.logger.logJSON(n.channel.get().user), n._autoSendPV && n.predefinePageView(), n.channel.setReady(!0)
                } else n.callbackSend = !0
            }, this.predefinePageView = function () {
                var e = arguments.length > 0 && void 0 !== arguments[0] ? arguments[0] : {},
                    t = {title: document.title || location.pathname, url: location.href, url_path: location.pathname},
                    r = de({}, t, e);
                n.event("predefine_pageview", r, !0)
            }, this.event = function () {
                for (var e = arguments.length, t = Array(e), r = 0; r < e; r++) t[r] = arguments[r];
                var a = X.isBoolean(t[t.length - 1]), i = !!a && t[t.length - 1], o = a ? t.slice(0, t.length - 1) : t,
                    l = o[0], s = [];
                X.isArray(l) ? s = o : s[0] = o, s = s.map(function (e) {
                    return function (e, t) {
                        var n = e;
                        /^event\./.test(e) && (n = e.slice(6));
                        var r = t;
                        return X.isObj(r) || (r = {}), r.event_index = fe(), {
                            event: n,
                            params: r,
                            local_time_ms: +new Date
                        }
                    }.apply(void 0, e)
                }), n.channel.event(s, i)
            }, this._isSendFuncCalled = !1, this._autoSendPV = !0, this.name = t, this.logger = new q(t)
        };
        ge.exportMethods = ["init", "config", "send", "start", "predefinePageView"];
        t.default = function e(t) {
            var n = this;
            return function (e, t) {
                if (!(e instanceof t)) throw new TypeError("Cannot call a class as a function")
            }(this, e), this._exportCollect = function () {
                for (var e = arguments.length, t = Array(e), r = 0; r < e; r++) t[r] = arguments[r];
                n._isQueueProcessed ? n._executeCmd.apply(n, t) : (n.cmdQueue.push(t), n._processCmdQueue())
            }, this._processCmdQueue = function () {
                if (0 !== n.cmdQueue.length) {
                    var e, t, r, a, i = (e = n.cmdQueue, t = "init", r = "0", a = -1, e.forEach(function (e, n) {
                        (void 0 !== r ? e[r] : e) === t && (a = n)
                    }), a);
                    -1 !== i && (n._isQueueProcessed = !0, n._executeCmd.apply(n, n.cmdQueue[i]), n.cmdQueue.forEach(function (e, t) {
                        t !== i && n._executeCmd.apply(n, e)
                    }), n.cmdQueue = [])
                }
            }, this._executeCmd = function () {
                for (var e = arguments.length, t = Array(e), r = 0; r < e; r++) t[r] = arguments[r];
                var a, i, o = t[0];
                ge.exportMethods.indexOf(o) > -1 ? (a = n.colloctor)[o].apply(a, t.slice(1)) : (i = n.colloctor).event.apply(i, t)
            }, this.name = t || "Collector" + +new Date, this.cmdQueue = [], this.colloctor = new ge(this.name), this._isQueueProcessed = !1, this._processCmdQueue(), this._exportCollect.init = this._exportCollect.bind(this, "init"), this._exportCollect.config = this._exportCollect.bind(this, "config"), this._exportCollect.send = this._exportCollect.bind(this, "send"), this._exportCollect.start = this._exportCollect.bind(this, "start"), this._exportCollect.predefinePageView = this._exportCollect.bind(this, "predefinePageView"), this._exportCollect
        }, e.exports = t.default
    }, function (e, t, n) {
        e.exports = n(8)
    }, function (e, t, n) {
        "use strict";
        Object.defineProperty(t, "__esModule", {value: !0});
        var r = _(n(0)), a = _(n(34)), i = _(n(35)), o = _(n(36)), l = _(n(37)), s = _(n(38)), u = _(n(39)),
            c = _(n(40)),
            p = _(n(41)), d = _(n(42)), f = _(n(43)), g = _(n(44)), h = _(n(45)), y = _(n(46)), x = _(n(47)),
            m = _(n(48)),
            v = _(n(49)), b = _(n(50)), k = _(n(51)), w = _(n(6));
        _(n(52));

        function _(e) {
            return e && e.__esModule ? e : {default: e}
        }

        var E = {};

        function C(e, t, n) {
            var r = e;
            t.map(function (e, a) {
                r[e] = a == t.length - 1 ? n : r[e] || {}, r = r[e]
            })
        }

        C(E, ["controls", "collect"], w.default), C(E, ["controls", "cssFullscreen"], k.default), C(E, ["controls", "danmu"], b.default), C(E, ["controls", "definition"], v.default), C(E, ["controls", "download"], m.default), C(E, ["controls", "fullscreen"], x.default), C(E, ["controls", "i18n"], y.default), C(E, ["controls", "localPreview"], h.default), C(E, ["controls", "logger"], g.default), C(E, ["controls", "mobile"], f.default), C(E, ["controls", "pc"], d.default), C(E, ["controls", "pip"], p.default), C(E, ["controls", "play"], c.default), C(E, ["controls", "playNext"], u.default), C(E, ["controls", "replay"], s.default), C(E, ["controls", "rotate"], l.default), C(E, ["controls", "screenShot"], o.default), C(E, ["controls", "start"], i.default), C(E, ["controls", "volume"], a.default), t.default = r.default, e.exports = t.default
    }, function (e, t, n) {
        "use strict";
        Object.defineProperty(t, "__esModule", {value: !0});
        var r = function () {
            function e(e, t) {
                for (var n = 0; n < t.length; n++) {
                    var r = t[n];
                    r.enumerable = r.enumerable || !1, r.configurable = !0, "value" in r && (r.writable = !0), Object.defineProperty(e, r.key, r)
                }
            }

            return function (t, n, r) {
                return n && e(t.prototype, n), r && e(t, r), t
            }
        }(), a = l(n(10)), i = l(n(2)), o = l(n(3));

        function l(e) {
            return e && e.__esModule ? e : {default: e}
        }

        var s = function () {
            function e(t) {
                !function (e, t) {
                    if (!(e instanceof t)) throw new TypeError("Cannot call a class as a function")
                }(this, e), this.logParams = {
                    bc: 0,
                    bu_acu_t: 0,
                    played: []
                }, this._hasStart = !1, this.videoConfig = {
                    controls: !1,
                    autoplay: t.autoplay,
                    playsinline: t.playsinline,
                    "webkit-playsinline": t.playsinline,
                    "x5-playsinline": t.playsinline,
                    "x5-video-player-type": t["x5-video-player-type"],
                    "x5-video-player-fullscreen": t["x5-video-player-fullscreen"],
                    "x5-video-orientation": t["x5-video-orientation"],
                    airplay: t.airplay,
                    "webkit-airplay": t.airplay,
                    tabindex: 2,
                    mediaType: t.mediaType || "video"
                }, t.loop && (this.videoConfig.loop = "loop");
                var n = "";
                if (t.textTrack && Array.isArray(t.textTrack) && (navigator.userAgent.indexOf("Chrome") > -1 || navigator.userAgent.indexOf("Firefox") > -1) && (t.textTrack.some(function (e) {
                    if (e.src && e.label && e.default) return n += '<track src="' + e.src + '" ', e.kind && (n += 'kind="' + e.kind + '" '), n += 'label="' + e.label + '" ', e.srclang && (n += 'srclang="' + e.srclang + '" '), n += (e.default ? "default" : "") + ">", !0
                }), this.videoConfig.crossorigin = "anonymous"), t.textTrackStyle) {
                    var r = document.createElement("style");
                    this.textTrackStyle = r, document.head.appendChild(r);
                    var l = "";
                    for (var s in t.textTrackStyle) l += s + ": " + t.textTrackStyle[s] + ";";
                    var u = t.id ? "#" + t.id : t.el.id ? "#" + t.el.id : "." + t.el.className;
                    r.sheet.insertRule ? r.sheet.insertRule(u + " video::cue { " + l + " }", 0) : r.sheet.addRule && r.sheet.addRule(u + " video::cue", l)
                }
                this.video = i.default.createDom(this.videoConfig.mediaType, n, this.videoConfig, ""), t.autoplay && (this.video.autoplay = !0, t.autoplayMuted && (this.video.muted = !0)), this.ev = ["play", "playing", "pause", "ended", "error", "seeking", "seeked", "timeupdate", "waiting", "canplay", "canplaythrough", "durationchange", "volumechange", "loadeddata"].map(function (e) {
                    return t = {}, n = e, r = "on" + e.charAt(0).toUpperCase() + e.slice(1), n in t ? Object.defineProperty(t, n, {
                        value: r,
                        enumerable: !0,
                        configurable: !0,
                        writable: !0
                    }) : t[n] = r, t;
                    var t, n, r
                }), (0, a.default)(this), this._interval = {};
                var c = "0,0", p = this;
                this.ev.forEach(function (e) {
                    p.evItem = Object.keys(e)[0];
                    var t = Object.keys(e)[0];
                    p.video.addEventListener(Object.keys(e)[0], function () {
                        p.logParams && ("play" === t ? p.hasStart = !0 : "waiting" === t ? (p.logParams.bc++, p.inWaitingStart = (new Date).getTime()) : "playing" === t ? p.inWaitingStart && (p.logParams.bu_acu_t += (new Date).getTime() - p.inWaitingStart, p.inWaitingStart = void 0) : "loadeddata" === t ? p.logParams.played.push({
                            begin: 0,
                            end: -1
                        }) : "seeking" === t ? p.logParams.played.push({
                            begin: p.video.currentTime,
                            end: -1
                        }) : p && p.logParams && p.logParams.played && "timeupdate" === t && (p.logParams.played.length < 1 && p.logParams.played.push({
                            begin: p.video.currentTime,
                            end: -1
                        }), p.logParams.played[p.logParams.played.length - 1].end = p.video.currentTime), "error" === t ? p.video.error && p.emit(t, new o.default("other", p.currentTime, p.duration, p.networkState, p.readyState, p.currentSrc, p.src, p.ended, {
                            line: 41,
                            msg: p.error,
                            handle: "Constructor"
                        })) : p.emit(t, p), p.hasOwnProperty("_interval") && (["ended", "error", "timeupdate"].indexOf(t) < 0 ? (clearInterval(p._interval.bufferedChange), i.default.setInterval(p, "bufferedChange", function () {
                            for (var e = [], t = 0, n = p.video.buffered.length; t < n; t++) e.push([p.video.buffered.start(t), p.video.buffered.end(t)]);
                            e.toString() !== c && (c = e.toString(), p.emit("bufferedChange", e))
                        }, 200)) : "timeupdate" !== t && i.default.clearInterval(p, "bufferedChange")))
                    }, !1)
                })
            }

            return r(e, [{
                key: "destroy", value: function () {
                    this.textTrackStyle && this.textTrackStyle.parentNode.removeChild(this.textTrackStyle)
                }
            }, {
                key: "play", value: function () {
                    this.video.play()
                }
            }, {
                key: "pause", value: function () {
                    this.video.pause()
                }
            }, {
                key: "canPlayType", value: function () {
                    this.video.canPlayType()
                }
            }, {
                key: "getBufferedRange", value: function () {
                    var e = [0, 0], t = this.video, n = t.buffered, r = t.currentTime;
                    if (n) for (var a = 0, i = n.length; a < i && (e[0] = n.start(a), e[1] = n.end(a), !(e[0] <= r && r <= e[1])); a++) ;
                    return e[0] - r <= 0 && r - e[1] <= 0 ? e : [0, 0]
                }
            }, {
                key: "hasStart", get: function () {
                    return this._hasStart
                }, set: function (e) {
                    "boolean" != typeof e || !0 !== e || this._hasStart || (this._hasStart = !0, this.emit("hasstart"))
                }
            }, {
                key: "autoplay", set: function (e) {
                    this.video.autoplay = e
                }, get: function () {
                    return this.video.autoplay
                }
            }, {
                key: "buffered", get: function () {
                    return this.video.buffered
                }
            }, {
                key: "crossOrigin", get: function () {
                    return this.video.crossOrigin
                }, set: function (e) {
                    this.video.crossOrigin = e
                }
            }, {
                key: "currentSrc", get: function () {
                    return this.video.currentSrc
                }, set: function (e) {
                    this.video.currentSrc = e
                }
            }, {
                key: "currentTime", get: function () {
                    return this.video.currentTime
                }, set: function (e) {
                    this.video.currentTime = e
                }
            }, {
                key: "defaultMuted", get: function () {
                    return this.video.defaultMuted
                }, set: function (e) {
                    this.video.defaultMuted = e
                }
            }, {
                key: "duration", get: function () {
                    return this.video.duration
                }
            }, {
                key: "ended", get: function () {
                    return this.video.ended
                }
            }, {
                key: "error", get: function () {
                    var e = this.video.error;
                    if (!e) return null;
                    var t = [{en: "MEDIA_ERR_ABORTED", cn: "取回过程被用户中止"}, {
                        en: "MEDIA_ERR_NETWORK",
                        cn: "当下载时发生错误"
                    }, {en: "MEDIA_ERR_DECODE", cn: "当解码时发生错误"}, {en: "MEDIA_ERR_SRC_NOT_SUPPORTED", cn: "不支持音频/视频"}];
                    return this.lang ? this.lang[t[e.code - 1].en] : t[e.code - 1].en
                }
            }, {
                key: "loop", get: function () {
                    return this.video.loop
                }, set: function (e) {
                    this.video.loop = e
                }
            }, {
                key: "muted", get: function () {
                    return this.video.muted
                }, set: function (e) {
                    this.video.muted = e
                }
            }, {
                key: "networkState", get: function () {
                    var e = [{en: "NETWORK_EMPTY", cn: "音频/视频尚未初始化"}, {
                        en: "NETWORK_IDLE",
                        cn: "音频/视频是活动的且已选取资源，但并未使用网络"
                    }, {en: "NETWORK_LOADING", cn: "浏览器正在下载数据"}, {en: "NETWORK_NO_SOURCE", cn: "未找到音频/视频来源"}];
                    return this.lang ? this.lang[e[this.video.networkState].en] : e[this.video.networkState].en
                }
            }, {
                key: "paused", get: function () {
                    return this.video.paused
                }
            }, {
                key: "playbackRate", get: function () {
                    return this.video.playbackRate
                }, set: function (e) {
                    this.video.playbackRate = e
                }
            }, {
                key: "played", get: function () {
                    return this.video.played
                }
            }, {
                key: "preload", get: function () {
                    return this.video.preload
                }, set: function (e) {
                    this.video.preload = e
                }
            }, {
                key: "readyState", get: function () {
                    var e = [{en: "HAVE_NOTHING", cn: "没有关于音频/视频是否就绪的信息"}, {
                        en: "HAVE_METADATA",
                        cn: "关于音频/视频就绪的元数据"
                    }, {en: "HAVE_CURRENT_DATA", cn: "关于当前播放位置的数据是可用的，但没有足够的数据来播放下一帧/毫秒"}, {
                        en: "HAVE_FUTURE_DATA",
                        cn: "当前及至少下一帧的数据是可用的"
                    }, {en: "HAVE_ENOUGH_DATA", cn: "可用数据足以开始播放"}];
                    return this.lang ? this.lang[e[this.video.readyState].en] : e[this.video.readyState]
                }
            }, {
                key: "seekable", get: function () {
                    return this.video.seekable
                }
            }, {
                key: "seeking", get: function () {
                    return this.video.seeking
                }
            }, {
                key: "src", get: function () {
                    return this.video.src
                }, set: function (e) {
                    var t = this;
                    i.default.hasClass(this.root, "xgplayer-ended") || this.emit("urlchange", JSON.parse(JSON.stringify(t.logParams))), this.logParams = {
                        bc: 0,
                        bu_acu_t: 0,
                        played: [],
                        pt: (new Date).getTime(),
                        vt: (new Date).getTime(),
                        vd: 0
                    }, this.video.pause(), this.video.src = e, this.logParams.playSrc = e, this.logParams.pt = (new Date).getTime(), this.logParams.vt = this.logParams.pt, this.once("loadeddata", function e() {
                        t.logParams.vt = (new Date).getTime(), t.logParams.pt > t.logParams.vt && (t.logParams.pt = t.logParams.vt), t.logParams.vd = t.video.duration, t.off("loadeddata", e)
                    })
                }
            }, {
                key: "volume", get: function () {
                    return this.video.volume
                }, set: function (e) {
                    this.video.volume = e
                }
            }, {
                key: "fullscreen", get: function () {
                    return i.default.hasClass(this.root, "xgplayer-is-fullscreen") || i.default.hasClass(this.root, "xgplayer-fullscreen-active")
                }
            }, {
                key: "bullet", get: function () {
                    return !!i.default.findDom(this.root, "xg-bullet") && i.default.hasClass(i.default.findDom(this.root, "xg-bullet"), "xgplayer-has-bullet")
                }
            }, {
                key: "textTrack", get: function () {
                    return i.default.hasClass(this.root, "xgplayer-is-textTrack")
                }
            }, {
                key: "pip", get: function () {
                    return i.default.hasClass(this.root, "xgplayer-pip-active")
                }
            }]), e
        }();
        t.default = s, e.exports = t.default
    }, function (e, t, n) {
        "use strict";
        var r, a, i, o, l, s, u, c = "function" == typeof Symbol && "symbol" == typeof Symbol.iterator ? function (e) {
                return typeof e
            } : function (e) {
                return e && "function" == typeof Symbol && e.constructor === Symbol && e !== Symbol.prototype ? "symbol" : typeof e
            }, p = n(11), d = n(25), f = Function.prototype.apply, g = Function.prototype.call, h = Object.create,
            y = Object.defineProperty, x = Object.defineProperties, m = Object.prototype.hasOwnProperty,
            v = {configurable: !0, enumerable: !1, writable: !0};
        a = function (e, t) {
            var n, a;
            return d(t), a = this, r.call(this, e, n = function () {
                i.call(a, e, n), f.call(t, this, arguments)
            }), n.__eeOnceListener__ = t, this
        }, l = {
            on: r = function (e, t) {
                var n;
                return d(t), m.call(this, "__ee__") ? n = this.__ee__ : (n = v.value = h(null), y(this, "__ee__", v), v.value = null), n[e] ? "object" === c(n[e]) ? n[e].push(t) : n[e] = [n[e], t] : n[e] = t, this
            }, once: a, off: i = function (e, t) {
                var n, r, a, i;
                if (d(t), !m.call(this, "__ee__")) return this;
                if (!(n = this.__ee__)[e]) return this;
                if ("object" === (void 0 === (r = n[e]) ? "undefined" : c(r))) for (i = 0; a = r[i]; ++i) a !== t && a.__eeOnceListener__ !== t || (2 === r.length ? n[e] = r[i ? 0 : 1] : r.splice(i, 1)); else r !== t && r.__eeOnceListener__ !== t || delete n[e];
                return this
            }, emit: o = function (e) {
                var t, n, r, a, i;
                if (m.call(this, "__ee__") && (a = this.__ee__[e])) if ("object" === (void 0 === a ? "undefined" : c(a))) {
                    for (n = arguments.length, i = new Array(n - 1), t = 1; t < n; ++t) i[t - 1] = arguments[t];
                    for (a = a.slice(), t = 0; r = a[t]; ++t) f.call(r, this, i)
                } else switch (arguments.length) {
                    case 1:
                        g.call(a, this);
                        break;
                    case 2:
                        g.call(a, this, arguments[1]);
                        break;
                    case 3:
                        g.call(a, this, arguments[1], arguments[2]);
                        break;
                    default:
                        for (n = arguments.length, i = new Array(n - 1), t = 1; t < n; ++t) i[t - 1] = arguments[t];
                        f.call(a, this, i)
                }
            }
        }, s = {on: p(r), once: p(a), off: p(i), emit: p(o)}, u = x({}, s), e.exports = t = function (e) {
            return null == e ? h(u) : x(Object(e), s)
        }, t.methods = l
    }, function (e, t, n) {
        "use strict";
        var r = n(12), a = n(20), i = n(21), o = n(22);
        (e.exports = function (e, t) {
            var n, i, l, s, u;
            return arguments.length < 2 || "string" != typeof e ? (s = t, t = e, e = null) : s = arguments[2], null == e ? (n = l = !0, i = !1) : (n = o.call(e, "c"), i = o.call(e, "e"), l = o.call(e, "w")), u = {
                value: t,
                configurable: n,
                enumerable: i,
                writable: l
            }, s ? r(a(s), u) : u
        }).gs = function (e, t, n) {
            var l, s, u, c;
            return "string" != typeof e ? (u = n, n = t, t = e, e = null) : u = arguments[3], null == t ? t = void 0 : i(t) ? null == n ? n = void 0 : i(n) || (u = n, n = void 0) : (u = t, t = n = void 0), null == e ? (l = !0, s = !1) : (l = o.call(e, "c"), s = o.call(e, "e")), c = {
                get: t,
                set: n,
                configurable: l,
                enumerable: s
            }, u ? r(a(u), c) : c
        }
    }, function (e, t, n) {
        "use strict";
        e.exports = n(13)() ? Object.assign : n(14)
    }, function (e, t, n) {
        "use strict";
        e.exports = function () {
            var e, t = Object.assign;
            return "function" == typeof t && (t(e = {foo: "raz"}, {bar: "dwa"}, {trzy: "trzy"}), e.foo + e.bar + e.trzy === "razdwatrzy")
        }
    }, function (e, t, n) {
        "use strict";
        var r = n(15), a = n(19), i = Math.max;
        e.exports = function (e, t) {
            var n, o, l, s = i(arguments.length, 2);
            for (e = Object(a(e)), l = function (r) {
                try {
                    e[r] = t[r]
                } catch (e) {
                    n || (n = e)
                }
            }, o = 1; o < s; ++o) t = arguments[o], r(t).forEach(l);
            if (void 0 !== n) throw n;
            return e
        }
    }, function (e, t, n) {
        "use strict";
        e.exports = n(16)() ? Object.keys : n(17)
    }, function (e, t, n) {
        "use strict";
        e.exports = function () {
            try {
                return Object.keys("primitive"), !0
            } catch (e) {
                return !1
            }
        }
    }, function (e, t, n) {
        "use strict";
        var r = n(1), a = Object.keys;
        e.exports = function (e) {
            return a(r(e) ? Object(e) : e)
        }
    }, function (e, t, n) {
        "use strict";
        e.exports = function () {
        }
    }, function (e, t, n) {
        "use strict";
        var r = n(1);
        e.exports = function (e) {
            if (!r(e)) throw new TypeError("Cannot use null or undefined");
            return e
        }
    }, function (e, t, n) {
        "use strict";
        var r = n(1), a = Array.prototype.forEach, i = Object.create;
        e.exports = function (e) {
            var t = i(null);
            return a.call(arguments, function (e) {
                r(e) && function (e, t) {
                    var n;
                    for (n in e) t[n] = e[n]
                }(Object(e), t)
            }), t
        }
    }, function (e, t, n) {
        "use strict";
        e.exports = function (e) {
            return "function" == typeof e
        }
    }, function (e, t, n) {
        "use strict";
        e.exports = n(23)() ? String.prototype.contains : n(24)
    }, function (e, t, n) {
        "use strict";
        var r = "razdwatrzy";
        e.exports = function () {
            return "function" == typeof r.contains && (!0 === r.contains("dwa") && !1 === r.contains("foo"))
        }
    }, function (e, t, n) {
        "use strict";
        var r = String.prototype.indexOf;
        e.exports = function (e) {
            return r.call(this, e, arguments[1]) > -1
        }
    }, function (e, t, n) {
        "use strict";
        e.exports = function (e) {
            if ("function" != typeof e) throw new TypeError(e + " is not a function");
            return e
        }
    }, function (e, t, n) {
        "use strict";
        Object.defineProperty(t, "__esModule", {value: !0});
        var r = function () {
            function e(e, t) {
                for (var n = 0; n < t.length; n++) {
                    var r = t[n];
                    r.enumerable = r.enumerable || !1, r.configurable = !0, "value" in r && (r.writable = !0), Object.defineProperty(e, r.key, r)
                }
            }

            return function (t, n, r) {
                return n && e(t.prototype, n), r && e(t, r), t
            }
        }();
        var a = function () {
            function e() {
                var t = arguments.length > 0 && void 0 !== arguments[0] ? arguments[0] : {
                    name: "xgplayer",
                    version: 1,
                    db: null,
                    ojstore: {name: "xg-m4a", keypath: "vid"}
                };
                !function (e, t) {
                    if (!(e instanceof t)) throw new TypeError("Cannot call a class as a function")
                }(this, e), this.indexedDB = window.indexedDB || window.webkitindexedDB, this.IDBKeyRange = window.IDBKeyRange || window.webkitIDBKeyRange, this.myDB = t
            }

            return r(e, [{
                key: "openDB", value: function (e) {
                    var t = this, n = this, r = this.myDB.version || 1, a = n.indexedDB.open(n.myDB.name, r);
                    a.onerror = function (e) {
                    }, a.onsuccess = function (r) {
                        t.myDB.db = r.target.result, e.call(n)
                    }, a.onupgradeneeded = function (e) {
                        var t = e.target.result;
                        e.target.transaction;
                        t.objectStoreNames.contains(n.myDB.ojstore.name) || t.createObjectStore(n.myDB.ojstore.name, {keyPath: n.myDB.ojstore.keypath})
                    }
                }
            }, {
                key: "deletedb", value: function () {
                    this.indexedDB.deleteDatabase(this.myDB.name)
                }
            }, {
                key: "closeDB", value: function () {
                    this.myDB.db.close()
                }
            }, {
                key: "addData", value: function (e, t) {
                    for (var n = this.myDB.db.transaction(e, "readwrite").objectStore(e), r = void 0, a = 0; a < t.length; a++) (r = n.add(t[a])).onerror = function () {
                    }, r.onsuccess = function () {
                    }
                }
            }, {
                key: "putData", value: function (e, t) {
                    for (var n = this.myDB.db.transaction(e, "readwrite").objectStore(e), r = void 0, a = 0; a < t.length; a++) (r = n.put(t[a])).onerror = function () {
                    }, r.onsuccess = function () {
                    }
                }
            }, {
                key: "getDataByKey", value: function (e, t, n) {
                    var r = this, a = this.myDB.db.transaction(e, "readwrite").objectStore(e).get(t);
                    a.onerror = function () {
                        n.call(r, null)
                    }, a.onsuccess = function (e) {
                        var t = e.target.result;
                        n.call(r, t)
                    }
                }
            }, {
                key: "deleteData", value: function (e, t) {
                    this.myDB.db.transaction(e, "readwrite").objectStore(e).delete(t)
                }
            }, {
                key: "clearData", value: function (e) {
                    this.myDB.db.transaction(e, "readwrite").objectStore(e).clear()
                }
            }]), e
        }();
        t.default = a, e.exports = t.default
    }, function (e, t, n) {
        "use strict";
        var r, a;
        "function" == typeof Symbol && Symbol.iterator;
        /*!
     * Draggabilly v2.2.0
     * Make that shiz draggable
     * https://draggabilly.desandro.com
     * MIT license
     */
        !function (i, o) {
            r = [n(28), n(29)], void 0 === (a = function (e, t) {
                return function (e, t, n) {
                    function r(e, t) {
                        for (var n in t) e[n] = t[n];
                        return e
                    }

                    var a = e.jQuery;

                    function i(e, t) {
                        this.element = "string" == typeof e ? document.querySelector(e) : e, a && (this.$element = a(this.element)), this.options = r({}, this.constructor.defaults), this.option(t), this._create()
                    }

                    var o = i.prototype = Object.create(n.prototype);
                    i.defaults = {}, o.option = function (e) {
                        r(this.options, e)
                    };
                    var l = {relative: !0, absolute: !0, fixed: !0};

                    function s(e, t, n) {
                        return n = n || "round", t ? Math[n](e / t) * t : e
                    }

                    o._create = function () {
                        this.position = {}, this._getPosition(), this.startPoint = {x: 0, y: 0}, this.dragPoint = {
                            x: 0,
                            y: 0
                        }, this.startPosition = r({}, this.position);
                        var e = getComputedStyle(this.element);
                        l[e.position] || (this.element.style.position = "relative"), this.on("pointerDown", this.onPointerDown), this.on("pointerMove", this.onPointerMove), this.on("pointerUp", this.onPointerUp), this.enable(), this.setHandles()
                    }, o.setHandles = function () {
                        this.handles = this.options.handle ? this.element.querySelectorAll(this.options.handle) : [this.element], this.bindHandles()
                    }, o.dispatchEvent = function (e, t, n) {
                        var r = [t].concat(n);
                        this.emitEvent(e, r), this.dispatchJQueryEvent(e, t, n)
                    }, o.dispatchJQueryEvent = function (t, n, r) {
                        var a = e.jQuery;
                        if (a && this.$element) {
                            var i = a.Event(n);
                            i.type = t, this.$element.trigger(i, r)
                        }
                    }, o._getPosition = function () {
                        var e = getComputedStyle(this.element), t = this._getPositionCoord(e.left, "width"),
                            n = this._getPositionCoord(e.top, "height");
                        this.position.x = isNaN(t) ? 0 : t, this.position.y = isNaN(n) ? 0 : n, this._addTransformPosition(e)
                    }, o._getPositionCoord = function (e, n) {
                        if (-1 != e.indexOf("%")) {
                            var r = t(this.element.parentNode);
                            return r ? parseFloat(e) / 100 * r[n] : 0
                        }
                        return parseInt(e, 10)
                    }, o._addTransformPosition = function (e) {
                        var t = e.transform;
                        if (0 === t.indexOf("matrix")) {
                            var n = t.split(","), r = 0 === t.indexOf("matrix3d") ? 12 : 4, a = parseInt(n[r], 10),
                                i = parseInt(n[r + 1], 10);
                            this.position.x += a, this.position.y += i
                        }
                    }, o.onPointerDown = function (e, t) {
                        this.element.classList.add("is-pointer-down"), this.dispatchJQueryEvent("pointerDown", e, [t])
                    }, o.dragStart = function (e, t) {
                        this.isEnabled && (this._getPosition(), this.measureContainment(), this.startPosition.x = this.position.x, this.startPosition.y = this.position.y, this.setLeftTop(), this.dragPoint.x = 0, this.dragPoint.y = 0, this.element.classList.add("is-dragging"), this.dispatchEvent("dragStart", e, [t]), this.animate())
                    }, o.measureContainment = function () {
                        var e = this.getContainer();
                        if (e) {
                            var n = t(this.element), r = t(e), a = this.element.getBoundingClientRect(),
                                i = e.getBoundingClientRect(), o = r.borderLeftWidth + r.borderRightWidth,
                                l = r.borderTopWidth + r.borderBottomWidth, s = this.relativeStartPosition = {
                                    x: a.left - (i.left + r.borderLeftWidth),
                                    y: a.top - (i.top + r.borderTopWidth)
                                };
                            this.containSize = {
                                width: r.width - o - s.x - n.width,
                                height: r.height - l - s.y - n.height
                            }
                        }
                    }, o.getContainer = function () {
                        var e = this.options.containment;
                        if (e) return e instanceof HTMLElement ? e : "string" == typeof e ? document.querySelector(e) : this.element.parentNode
                    }, o.onPointerMove = function (e, t, n) {
                        this.dispatchJQueryEvent("pointerMove", e, [t, n])
                    }, o.dragMove = function (e, t, n) {
                        if (this.isEnabled) {
                            var r = n.x, a = n.y, i = this.options.grid, o = i && i[0], l = i && i[1];
                            r = s(r, o), a = s(a, l), r = this.containDrag("x", r, o), a = this.containDrag("y", a, l), r = "y" == this.options.axis ? 0 : r, a = "x" == this.options.axis ? 0 : a, this.position.x = this.startPosition.x + r, this.position.y = this.startPosition.y + a, this.dragPoint.x = r, this.dragPoint.y = a, this.dispatchEvent("dragMove", e, [t, n])
                        }
                    }, o.containDrag = function (e, t, n) {
                        if (!this.options.containment) return t;
                        var r = "x" == e ? "width" : "height", a = s(-this.relativeStartPosition[e], n, "ceil"),
                            i = this.containSize[r];
                        return i = s(i, n, "floor"), Math.max(a, Math.min(i, t))
                    }, o.onPointerUp = function (e, t) {
                        this.element.classList.remove("is-pointer-down"), this.dispatchJQueryEvent("pointerUp", e, [t])
                    }, o.dragEnd = function (e, t) {
                        this.isEnabled && (this.element.style.transform = "", this.setLeftTop(), this.element.classList.remove("is-dragging"), this.dispatchEvent("dragEnd", e, [t]))
                    }, o.animate = function () {
                        if (this.isDragging) {
                            this.positionDrag();
                            var e = this;
                            requestAnimationFrame(function () {
                                e.animate()
                            })
                        }
                    }, o.setLeftTop = function () {
                        this.element.style.left = this.position.x + "px", this.element.style.top = this.position.y + "px"
                    }, o.positionDrag = function () {
                        this.element.style.transform = "translate3d( " + this.dragPoint.x + "px, " + this.dragPoint.y + "px, 0)"
                    }, o.staticClick = function (e, t) {
                        this.dispatchEvent("staticClick", e, [t])
                    }, o.setPosition = function (e, t) {
                        this.position.x = e, this.position.y = t, this.setLeftTop()
                    }, o.enable = function () {
                        this.isEnabled = !0
                    }, o.disable = function () {
                        this.isEnabled = !1, this.isDragging && this.dragEnd()
                    }, o.destroy = function () {
                        this.disable(), this.element.style.transform = "", this.element.style.left = "", this.element.style.top = "", this.element.style.position = "", this.unbindHandles(), this.$element && this.$element.removeData("draggabilly")
                    }, o._init = function () {
                    }, a && a.bridget && a.bridget("draggabilly", i);
                    return i
                }(i, e, t)
            }.apply(t, r)) || (e.exports = a)
        }(window)
    }, function (e, t, n) {
        "use strict";
        var r, a, i = "function" == typeof Symbol && "symbol" == typeof Symbol.iterator ? function (e) {
            return typeof e
        } : function (e) {
            return e && "function" == typeof Symbol && e.constructor === Symbol && e !== Symbol.prototype ? "symbol" : typeof e
        };
        window, void 0 === (a = "function" == typeof(r = function () {
            function e(e) {
                var t = parseFloat(e), n = -1 == e.indexOf("%") && !isNaN(t);
                return n && t
            }

            var t = "undefined" == typeof console ? function () {
                } : function (e) {
                    console.error(e)
                },
                n = ["paddingLeft", "paddingRight", "paddingTop", "paddingBottom", "marginLeft", "marginRight", "marginTop", "marginBottom", "borderLeftWidth", "borderRightWidth", "borderTopWidth", "borderBottomWidth"],
                r = n.length;

            function a(e) {
                var n = getComputedStyle(e);
                return n || t("Style returned " + n + ". Are you running this code in a hidden iframe on Firefox? See https://bit.ly/getsizebug1"), n
            }

            var o, l = !1;

            function s(t) {
                if (function () {
                    if (!l) {
                        l = !0;
                        var t = document.createElement("div");
                        t.style.width = "200px", t.style.padding = "1px 2px 3px 4px", t.style.borderStyle = "solid", t.style.borderWidth = "1px 2px 3px 4px", t.style.boxSizing = "border-box";
                        var n = document.body || document.documentElement;
                        n.appendChild(t);
                        var r = a(t);
                        o = 200 == Math.round(e(r.width)), s.isBoxSizeOuter = o, n.removeChild(t)
                    }
                }(), "string" == typeof t && (t = document.querySelector(t)), t && "object" == (void 0 === t ? "undefined" : i(t)) && t.nodeType) {
                    var u = a(t);
                    if ("none" == u.display) return function () {
                        for (var e = {
                            width: 0,
                            height: 0,
                            innerWidth: 0,
                            innerHeight: 0,
                            outerWidth: 0,
                            outerHeight: 0
                        }, t = 0; t < r; t++) {
                            var a = n[t];
                            e[a] = 0
                        }
                        return e
                    }();
                    var c = {};
                    c.width = t.offsetWidth, c.height = t.offsetHeight;
                    for (var p = c.isBorderBox = "border-box" == u.boxSizing, d = 0; d < r; d++) {
                        var f = n[d], g = u[f], h = parseFloat(g);
                        c[f] = isNaN(h) ? 0 : h
                    }
                    var y = c.paddingLeft + c.paddingRight, x = c.paddingTop + c.paddingBottom,
                        m = c.marginLeft + c.marginRight, v = c.marginTop + c.marginBottom,
                        b = c.borderLeftWidth + c.borderRightWidth, k = c.borderTopWidth + c.borderBottomWidth,
                        w = p && o,
                        _ = e(u.width);
                    !1 !== _ && (c.width = _ + (w ? 0 : y + b));
                    var E = e(u.height);
                    return !1 !== E && (c.height = E + (w ? 0 : x + k)), c.innerWidth = c.width - (y + b), c.innerHeight = c.height - (x + k), c.outerWidth = c.width + m, c.outerHeight = c.height + v, c
                }
            }

            return s
        }) ? r.call(t, n, t, e) : r) || (e.exports = a)
    }, function (e, t, n) {
        "use strict";
        var r, a;
        "function" == typeof Symbol && Symbol.iterator;
        /*!
     * Unidragger v2.3.0
     * Draggable base class
     * MIT license
     */
        !function (i, o) {
            r = [n(30)], void 0 === (a = function (e) {
                return function (e, t) {
                    function n() {
                    }

                    var r = n.prototype = Object.create(t.prototype);
                    r.bindHandles = function () {
                        this._bindHandles(!0)
                    }, r.unbindHandles = function () {
                        this._bindHandles(!1)
                    }, r._bindHandles = function (t) {
                        for (var n = (t = void 0 === t || t) ? "addEventListener" : "removeEventListener", r = t ? this._touchActionValue : "", a = 0; a < this.handles.length; a++) {
                            var i = this.handles[a];
                            this._bindStartEvent(i, t), i[n]("click", this), e.PointerEvent && (i.style.touchAction = r)
                        }
                    }, r._touchActionValue = "none", r.pointerDown = function (e, t) {
                        var n = this.okayPointerDown(e);
                        n && (this.pointerDownPointer = t, e.preventDefault(), this.pointerDownBlur(), this._bindPostStartEvents(e), this.emitEvent("pointerDown", [e, t]))
                    };
                    var a = {TEXTAREA: !0, INPUT: !0, SELECT: !0, OPTION: !0},
                        i = {radio: !0, checkbox: !0, button: !0, submit: !0, image: !0, file: !0};
                    return r.okayPointerDown = function (e) {
                        var t = a[e.target.nodeName], n = i[e.target.type], r = !t || n;
                        return r || this._pointerReset(), r
                    }, r.pointerDownBlur = function () {
                        var e = document.activeElement;
                        e && e.blur && e != document.body && e.blur()
                    }, r.pointerMove = function (e, t) {
                        var n = this._dragPointerMove(e, t);
                        this.emitEvent("pointerMove", [e, t, n]), this._dragMove(e, t, n)
                    }, r._dragPointerMove = function (e, t) {
                        var n = {
                            x: t.pageX - this.pointerDownPointer.pageX,
                            y: t.pageY - this.pointerDownPointer.pageY
                        };
                        return !this.isDragging && this.hasDragStarted(n) && this._dragStart(e, t), n
                    }, r.hasDragStarted = function (e) {
                        return Math.abs(e.x) > 3 || Math.abs(e.y) > 3
                    }, r.pointerUp = function (e, t) {
                        this.emitEvent("pointerUp", [e, t]), this._dragPointerUp(e, t)
                    }, r._dragPointerUp = function (e, t) {
                        this.isDragging ? this._dragEnd(e, t) : this._staticClick(e, t)
                    }, r._dragStart = function (e, t) {
                        this.isDragging = !0, this.isPreventingClicks = !0, this.dragStart(e, t)
                    }, r.dragStart = function (e, t) {
                        this.emitEvent("dragStart", [e, t])
                    }, r._dragMove = function (e, t, n) {
                        this.isDragging && this.dragMove(e, t, n)
                    }, r.dragMove = function (e, t, n) {
                        e.preventDefault(), this.emitEvent("dragMove", [e, t, n])
                    }, r._dragEnd = function (e, t) {
                        this.isDragging = !1, setTimeout(function () {
                            delete this.isPreventingClicks
                        }.bind(this)), this.dragEnd(e, t)
                    }, r.dragEnd = function (e, t) {
                        this.emitEvent("dragEnd", [e, t])
                    }, r.onclick = function (e) {
                        this.isPreventingClicks && e.preventDefault()
                    }, r._staticClick = function (e, t) {
                        this.isIgnoringMouseUp && "mouseup" == e.type || (this.staticClick(e, t), "mouseup" != e.type && (this.isIgnoringMouseUp = !0, setTimeout(function () {
                            delete this.isIgnoringMouseUp
                        }.bind(this), 400)))
                    }, r.staticClick = function (e, t) {
                        this.emitEvent("staticClick", [e, t])
                    }, n.getPointerPoint = t.getPointerPoint, n
                }(i, e)
            }.apply(t, r)) || (e.exports = a)
        }(window)
    }, function (e, t, n) {
        "use strict";
        var r, a;
        "function" == typeof Symbol && Symbol.iterator;
        /*!
     * Unipointer v2.3.0
     * base class for doing one thing with pointer event
     * MIT license
     */
        !function (i, o) {
            r = [n(31)], void 0 === (a = function (e) {
                return function (e, t) {
                    function n() {
                    }

                    var r = n.prototype = Object.create(t.prototype);
                    r.bindStartEvent = function (e) {
                        this._bindStartEvent(e, !0)
                    }, r.unbindStartEvent = function (e) {
                        this._bindStartEvent(e, !1)
                    }, r._bindStartEvent = function (t, n) {
                        var r = (n = void 0 === n || n) ? "addEventListener" : "removeEventListener", a = "mousedown";
                        e.PointerEvent ? a = "pointerdown" : "ontouchstart" in e && (a = "touchstart"), t[r](a, this)
                    }, r.handleEvent = function (e) {
                        var t = "on" + e.type;
                        this[t] && this[t](e)
                    }, r.getTouch = function (e) {
                        for (var t = 0; t < e.length; t++) {
                            var n = e[t];
                            if (n.identifier == this.pointerIdentifier) return n
                        }
                    }, r.onmousedown = function (e) {
                        var t = e.button;
                        t && 0 !== t && 1 !== t || this._pointerDown(e, e)
                    }, r.ontouchstart = function (e) {
                        this._pointerDown(e, e.changedTouches[0])
                    }, r.onpointerdown = function (e) {
                        this._pointerDown(e, e)
                    }, r._pointerDown = function (e, t) {
                        e.button || this.isPointerDown || (this.isPointerDown = !0, this.pointerIdentifier = void 0 !== t.pointerId ? t.pointerId : t.identifier, this.pointerDown(e, t))
                    }, r.pointerDown = function (e, t) {
                        this._bindPostStartEvents(e), this.emitEvent("pointerDown", [e, t])
                    };
                    var a = {
                        mousedown: ["mousemove", "mouseup"],
                        touchstart: ["touchmove", "touchend", "touchcancel"],
                        pointerdown: ["pointermove", "pointerup", "pointercancel"]
                    };
                    return r._bindPostStartEvents = function (t) {
                        if (t) {
                            var n = a[t.type];
                            n.forEach(function (t) {
                                e.addEventListener(t, this)
                            }, this), this._boundPointerEvents = n
                        }
                    }, r._unbindPostStartEvents = function () {
                        this._boundPointerEvents && (this._boundPointerEvents.forEach(function (t) {
                            e.removeEventListener(t, this)
                        }, this), delete this._boundPointerEvents)
                    }, r.onmousemove = function (e) {
                        this._pointerMove(e, e)
                    }, r.onpointermove = function (e) {
                        e.pointerId == this.pointerIdentifier && this._pointerMove(e, e)
                    }, r.ontouchmove = function (e) {
                        var t = this.getTouch(e.changedTouches);
                        t && this._pointerMove(e, t)
                    }, r._pointerMove = function (e, t) {
                        this.pointerMove(e, t)
                    }, r.pointerMove = function (e, t) {
                        this.emitEvent("pointerMove", [e, t])
                    }, r.onmouseup = function (e) {
                        this._pointerUp(e, e)
                    }, r.onpointerup = function (e) {
                        e.pointerId == this.pointerIdentifier && this._pointerUp(e, e)
                    }, r.ontouchend = function (e) {
                        var t = this.getTouch(e.changedTouches);
                        t && this._pointerUp(e, t)
                    }, r._pointerUp = function (e, t) {
                        this._pointerDone(), this.pointerUp(e, t)
                    }, r.pointerUp = function (e, t) {
                        this.emitEvent("pointerUp", [e, t])
                    }, r._pointerDone = function () {
                        this._pointerReset(), this._unbindPostStartEvents(), this.pointerDone()
                    }, r._pointerReset = function () {
                        this.isPointerDown = !1, delete this.pointerIdentifier
                    }, r.pointerDone = function () {
                    }, r.onpointercancel = function (e) {
                        e.pointerId == this.pointerIdentifier && this._pointerCancel(e, e)
                    }, r.ontouchcancel = function (e) {
                        var t = this.getTouch(e.changedTouches);
                        t && this._pointerCancel(e, t)
                    }, r._pointerCancel = function (e, t) {
                        this._pointerDone(), this.pointerCancel(e, t)
                    }, r.pointerCancel = function (e, t) {
                        this.emitEvent("pointerCancel", [e, t])
                    }, n.getPointerPoint = function (e) {
                        return {x: e.pageX, y: e.pageY}
                    }, n
                }(i, e)
            }.apply(t, r)) || (e.exports = a)
        }(window)
    }, function (e, t, n) {
        "use strict";
        var r, a;
        "function" == typeof Symbol && Symbol.iterator;
        "undefined" != typeof window && window, void 0 === (a = "function" == typeof(r = function () {
            function e() {
            }

            var t = e.prototype;
            return t.on = function (e, t) {
                if (e && t) {
                    var n = this._events = this._events || {}, r = n[e] = n[e] || [];
                    return -1 == r.indexOf(t) && r.push(t), this
                }
            }, t.once = function (e, t) {
                if (e && t) {
                    this.on(e, t);
                    var n = this._onceEvents = this._onceEvents || {};
                    return (n[e] = n[e] || {})[t] = !0, this
                }
            }, t.off = function (e, t) {
                var n = this._events && this._events[e];
                if (n && n.length) {
                    var r = n.indexOf(t);
                    return -1 != r && n.splice(r, 1), this
                }
            }, t.emitEvent = function (e, t) {
                var n = this._events && this._events[e];
                if (n && n.length) {
                    n = n.slice(0), t = t || [];
                    for (var r = this._onceEvents && this._onceEvents[e], a = 0; a < n.length; a++) {
                        var i = n[a];
                        r && r[i] && (this.off(e, i), delete r[i]), i.apply(this, t)
                    }
                    return this
                }
            }, t.allOff = function () {
                delete this._events, delete this._onceEvents
            }, e
        }) ? r.call(t, n, t, e) : r) || (e.exports = a)
    }, function (e, t, n) {
        "use strict";
        Object.defineProperty(t, "__esModule", {value: !0});
        t.getAbsoluteURL = function (e) {
            if (!e.match(/^https?:\/\//)) {
                var t = document.createElement("div");
                t.innerHTML = '<a href="' + e + '">x</a>', e = t.firstChild.href
            }
            return e
        }
    }, function (e, t, n) {
        "use strict";
        var r, a, i;
        "function" == typeof Symbol && Symbol.iterator;
        a = [], void 0 === (i = "function" == typeof(r = function () {
            return function e(t, n, r) {
                var a, i, o = window, l = "application/octet-stream", s = r || l, u = t, c = !n && !r && u,
                    p = document.createElement("a"), d = function (e) {
                        return String(e)
                    }, f = o.Blob || o.MozBlob || o.WebKitBlob || d, g = n || "download";
                if (f = f.call ? f.bind(o) : Blob, "true" === String(this) && (s = (u = [u, s])[0], u = u[1]), c && c.length < 2048 && (g = c.split("/").pop().split("?")[0], p.href = c, -1 !== p.href.indexOf(c))) {
                    var h = new XMLHttpRequest;
                    return h.open("GET", c, !0), h.responseType = "blob", h.onload = function (t) {
                        e(t.target.response, g, l)
                    }, setTimeout(function () {
                        h.send()
                    }, 0), h
                }
                if (/^data:([\w+-]+\/[\w+.-]+)?[,;]/.test(u)) {
                    if (!(u.length > 2096103.424 && f !== d)) return navigator.msSaveBlob ? navigator.msSaveBlob(v(u), g) : b(u);
                    u = v(u), s = u.type || l
                } else if (/([\x80-\xff])/.test(u)) {
                    for (var y = 0, x = new Uint8Array(u.length), m = x.length; y < m; ++y) x[y] = u.charCodeAt(y);
                    u = new f([x], {type: s})
                }

                function v(e) {
                    for (var t = e.split(/[:;,]/), n = t[1], r = "base64" == t[2] ? atob : decodeURIComponent, a = r(t.pop()), i = a.length, o = 0, l = new Uint8Array(i); o < i; ++o) l[o] = a.charCodeAt(o);
                    return new f([l], {type: n})
                }

                function b(e, t) {
                    if ("download" in p) return p.href = e, p.setAttribute("download", g), p.className = "download-js-link", p.innerHTML = "downloading...", p.style.display = "none", document.body.appendChild(p), setTimeout(function () {
                        p.click(), document.body.removeChild(p), !0 === t && setTimeout(function () {
                            o.URL.revokeObjectURL(p.href)
                        }, 250)
                    }, 66), !0;
                    if (/(Version)\/(\d+)\.(\d+)(?:\.(\d+))?.*Safari\//.test(navigator.userAgent)) return /^data:/.test(e) && (e = "data:" + e.replace(/^data:([\w\/\-\+]+)/, l)), window.open(e) || confirm("Displaying New Document\n\nUse Save As... to download, then click back to return to this page.") && (location.href = e), !0;
                    var n = document.createElement("iframe");
                    document.body.appendChild(n), !t && /^data:/.test(e) && (e = "data:" + e.replace(/^data:([\w\/\-\+]+)/, l)), n.src = e, setTimeout(function () {
                        document.body.removeChild(n)
                    }, 333)
                }

                if (a = u instanceof f ? u : new f([u], {type: s}), navigator.msSaveBlob) return navigator.msSaveBlob(a, g);
                if (o.URL) b(o.URL.createObjectURL(a), !0); else {
                    if ("string" == typeof a || a.constructor === d) try {
                        return b("data:" + s + ";base64," + o.btoa(a))
                    } catch (e) {
                        return b("data:" + s + "," + encodeURIComponent(a))
                    }
                    (i = new FileReader).onload = function (e) {
                        b(this.result)
                    }, i.readAsDataURL(a)
                }
                return !0
            }
        }) ? r.apply(t, a) : r) || (e.exports = i)
    }, function (e, t, n) {
        "use strict";
        var r, a = n(0), i = (r = a) && r.__esModule ? r : {default: r};
        i.default.install("volume", function () {
            var e = this, t = e.root, n = i.default.util, r = void 0, a = void 0, o = void 0, l = void 0;

            function s() {
                e.volume = e.config.volume, r = e.controls.querySelector(".xgplayer-volume"), a = r.querySelector(".xgplayer-slider"), o = r.querySelector(".xgplayer-bar"), l = r.querySelector(".xgplayer-drag"), r.querySelector(".xgplayer-icon")
            }

            function u(t) {
                e.video.muted = !1, a.focus(), n.event(t);
                var r = o.getBoundingClientRect(), i = (t.clientX, t.clientY), s = l.getBoundingClientRect().height,
                    u = !1,
                    c = function (t) {
                        t.preventDefault(), t.stopPropagation(), n.event(t), u = !0;
                        var a = s - t.clientY + i, o = a / r.height;
                        l.style.height = a + "px", e.volume = Math.max(Math.min(o, 1), 0)
                    }, p = function t(i) {
                        if (i.preventDefault(), i.stopPropagation(), n.event(i), window.removeEventListener("mousemove", c), window.removeEventListener("touchmove", c), window.removeEventListener("mouseup", t), window.removeEventListener("touchend", t), !u) {
                            var o = r.height - (i.clientY - r.top), s = o / r.height;
                            l.style.height = o + "px", s <= 0 && (e.volume > 0 ? l.volume = e.video.volume : s = l.volume), e.volume = Math.max(Math.min(s, 1), 0)
                        }
                        a.volume = e.volume, u = !1
                    };
                return window.addEventListener("mousemove", c), window.addEventListener("touchmove", c), window.addEventListener("mouseup", p), window.addEventListener("touchend", p), !1
            }

            function c() {
                e.video.muted = !1, 0 === e.volume ? e.volume = a.volume : e.volume = 0
            }

            function p() {
                n.addClass(t, "xgplayer-volume-active"), r && r.focus()
            }

            function d() {
                n.removeClass(t, "xgplayer-volume-active")
            }

            e.once("canplay", s), e.on("volumeBarClick", u), e.on("volumeIconClick", c), e.on("volumeIconEnter", p), e.on("volumeIconLeave", d);
            var f = null;

            function g() {
                f && clearTimeout(f), f = setTimeout(function () {
                    n.removeClass(t, "xgplayer-volume-muted"), n.removeClass(t, "xgplayer-volume-small"), n.removeClass(t, "xgplayer-volume-large"), 0 === e.volume ? n.addClass(t, "xgplayer-volume-muted") : e.volume < .5 ? n.addClass(t, "xgplayer-volume-small") : n.addClass(t, "xgplayer-volume-large");
                    var r = o.getBoundingClientRect().height || 76;
                    l.style.height = e.volume * r + "px"
                }, 50)
            }

            e.on("volumechange", g), e.once("destroy", function t() {
                e.off("canplay", s), e.off("volumeBarClick", u), e.off("volumeIconClick", c), e.off("volumeIconEnter", p), e.off("volumeIconLeave", d), e.off("volumechange", g), e.off("destroy", t)
            })
        })
    }, function (e, t, n) {
        "use strict";
        var r, a = n(0), i = (r = a) && r.__esModule ? r : {default: r};
        i.default.install("start", function () {
            var e = this, t = e.root, n = i.default.util;

            function r() {
                n.removeClass(t, "xgplayer-is-enter")
            }

            function a() {
                n.removeClass(t, "xgplayer-is-enter")
            }

            function o() {
                n.hasClass(t, "xgplayer-nostart") ? (n.removeClass(t, "xgplayer-nostart"), n.addClass(t, "xgplayer-is-enter"), e.on("canplay", r), e.once("playing", a), t.querySelector("video") || e.start(), e.play()) : e.paused && (n.removeClass(t, "xgplayer-nostart xgplayer-isloading"), setTimeout(function () {
                    e.play()
                }, 10))
            }

            e.on("startBtnClick", o), e.once("destroy", function t() {
                e.off("canplay", r), e.off("playing", a), e.off("startBtnClick", o), e.off("destroy", t)
            })
        })
    }, function (e, t, n) {
        "use strict";
        var r, a = n(0);
        ((r = a) && r.__esModule ? r : {default: r}).default.install("screenShot", function () {
            var e = this;
            e.root;
            if (e.config.screenShot) {
                var t = document.createElement("canvas"), n = t.getContext("2d"), r = new Image;
                t.width = this.config.width || 600, t.height = this.config.height || 337.5;
                var a = function (e, t) {
                    var n = document.createElement("a");
                    n.href = e, n.download = t;
                    var r = document.createEvent("MouseEvents");
                    r.initMouseEvent("click", !0, !1, window, 0, 0, 0, 0, 0, !1, !1, !1, !1, 0, null), n.dispatchEvent(r)
                };
                e.on("screenShotBtnClick", i), e.once("destroy", function t() {
                    e.off("screenShotBtnClick", i), e.off("destroy", t)
                })
            }

            function i() {
                r.onload = function () {
                    n.drawImage(e.video, 0, 0, t.width, t.height), r.setAttribute("crossOrigin", "anonymous"), r.src = t.toDataURL("image/png").replace("image/png", "image/octet-stream");
                    var i = r.src.replace(/^data:image\/[^;]+/, "data:application/octet-stream");
                    a(i, "截图.png")
                }()
            }
        })
    }, function (e, t, n) {
        "use strict";
        var r, a = n(0);
        ((r = a) && r.__esModule ? r : {default: r}).default.install("rotate", function () {
            var e = this, t = e.config.rotate;

            function n() {
                e.rotate(t.clockwise, t.innerRotate)
            }

            t && (e.on("rotateBtnClick", n), e.once("destroy", function t() {
                e.off("rotateBtnClick", n), e.off("destroy", t)
            }))
        })
    }, function (e, t, n) {
        "use strict";
        var r, a = n(0), i = (r = a) && r.__esModule ? r : {default: r};
        i.default.install("replay", function () {
            var e = this, t = i.default.util, n = e.root;

            function r() {
                t.removeClass(n, "replay"), e.replay()
            }

            e.on("replayBtnClick", r), e.on("ended", function () {
                e.config.loop || t.addClass(n, "replay")
            }), e.once("destroy", function t() {
                e.off("replayBtnClick", r), e.off("destroy", t)
            })
        })
    }, function (e, t, n) {
        "use strict";
        var r, a = n(0);
        ((r = a) && r.__esModule ? r : {default: r}).default.install("playNext", function () {
            var e = this, t = (e.root, e.config.playNext);

            function n() {
                e.currentVideoIndex + 1 < t.urlList.length ? (e.currentVideoIndex++, e.video.autoplay = !0, e.src = t.urlList[e.currentVideoIndex], e.emit("playerNext", e.currentVideoIndex + 1)) : e.emit("urlList last")
            }

            e.currentVideoIndex = -1, e.on("playNextBtnClick", n), e.once("destroy", function t() {
                e.off("playNextBtnClick", n), e.off("destroy", t)
            })
        })
    }, function (e, t, n) {
        "use strict";
        var r, a = n(0);
        ((r = a) && r.__esModule ? r : {default: r}).default.install("play", function () {
            var e = this;

            function t() {
                e.ended || (e.paused ? e.play() : e.pause())
            }

            e.on("playBtnClick", t), e.once("destroy", function n() {
                e.off("playBtnClick", t), e.off("destroy", n)
            })
        })
    }, function (e, t, n) {
        "use strict";
        var r, a = n(0), i = (r = a) && r.__esModule ? r : {default: r};
        i.default.install("pip", function () {
            var e = this, t = i.default.util, n = e.root;

            function r() {
                t.hasClass(n, "xgplayer-pip-active") ? e.exitPIP() : e.getPIP()
            }

            e.on("pipBtnClick", r), e.once("destroy", function t() {
                e.off("pipBtnClick", r), e.off("destroy", t)
            })
        })
    }, function (e, t, n) {
        "use strict";
        var r, a = n(0), i = (r = a) && r.__esModule ? r : {default: r};
        i.default.install("pc", function () {
            var e = this, t = i.default.util, n = e.controls, r = e.root, a = 0, o = void 0;

            function l() {
                clearTimeout(e.leavePlayerTimer), e.emit("focus", e)
            }

            function s() {
                e.config.closePlayerBlur || (e.leavePlayerTimer = setTimeout(function () {
                    e.emit("blur", e)
                }, e.config.leavePlayerTime || 0))
            }

            function u(t) {
                e.config.autoplay && e.start()
            }

            e.onElementClick = function (e, n) {
                e.preventDefault(), e.stopPropagation();
                var r = this;
                r.config.closeVideoClick || (a++, o && clearTimeout(o), 1 === a ? o = setTimeout(function () {
                    if (t.hasClass(r.root, "xgplayer-nostart")) return !1;
                    r.ended || (r.paused ? r.play() : r.pause()), a = 0
                }, 200) : a = 0)
            }, e.video.addEventListener("click", function (t) {
                e.onElementClick(t, e.video)
            }, !1), e.onElementDblclick = function (e, t) {
                if (e.preventDefault(), e.stopPropagation(), !this.config.closeVideoDblclick) {
                    var r = n.querySelector(".xgplayer-fullscreen");
                    if (r) {
                        var a = void 0;
                        document.createEvent ? (a = document.createEvent("Event")).initEvent("click", !0, !0) : a = new Event("click"), r.dispatchEvent(a)
                    }
                }
            }, e.video.addEventListener("dblclick", function (t) {
                e.onElementDblclick(t, e.video)
            }, !1), r.addEventListener("mouseenter", l), r.addEventListener("mouseleave", s), n.addEventListener("mouseenter", function (t) {
                e.userTimer && clearTimeout(e.userTimer)
            }, !1), n.addEventListener("mouseleave", function (t) {
                e.config.closeControlsBlur || e.emit("focus", e)
            }, !1), e.once("ready", u), e.once("destroy", function t() {
                r.removeEventListener("mouseenter", l), r.removeEventListener("mouseleave", s), e.off("ready", u), e.off("destroy", t)
            })
        })
    }, function (e, t, n) {
        "use strict";
        var r, a = n(0), i = (r = a) && r.__esModule ? r : {default: r};
        i.default.install("mobile", function () {
            var e = this, t = i.default.util, n = (e.controls, e.root);

            function r(t) {
                e.config.autoplay && e.start()
            }

            e.onElementTouchend = function (e, r) {
                if (e.preventDefault(), e.stopPropagation(), t.hasClass(n, "xgplayer-inactive") ? this.emit("focus") : this.emit("blur"), !this.config.closeVideoTouch) {
                    if (t.hasClass(this.root, "xgplayer-nostart")) return !1;
                    this.ended || (this.paused ? this.play() : this.pause())
                }
            }, e.video.addEventListener("touchend", function (t) {
                e.onElementTouchend(t, e.video)
            }, !1), e.once("ready", r), e.once("destroy", function t() {
                e.off("ready", r), e.off("destroy", t)
            })
        })
    }, function (e, t, n) {
        "use strict";
        var r = o(n(0)), a = o(n(5)), i = o(n(6));

        function o(e) {
            return e && e.__esModule ? e : {default: e}
        }

        r.default.install("logger", function () {
            var e = this, t = r.default.util;
            if (!0 !== e.config.noLog) {
                var n = function () {
                    e.video.played;
                    var t = s(e.logParams.played), n = (new Date).getTime();
                    u();
                    var r = {
                        url: e.logParams.pluginSrc ? e.logParams.pluginSrc : e.logParams.playSrc,
                        vid: e.config.vid,
                        bc: e.logParams.bc - 1 > 0 ? e.logParams.bc - 1 : 0,
                        bb: e.logParams.bc - 1 > 0 ? 1 : 0,
                        bu_acu_t: e.logParams.bu_acu_t,
                        pt: e.logParams.pt,
                        vt: e.logParams.vt,
                        vd: 1e3 * e.logParams.vd,
                        watch_dur: parseFloat((1e3 * t).toFixed(3)),
                        cur_play_pos: parseFloat((1e3 * e.currentTime).toFixed(3)),
                        et: n
                    };
                    window.__xigua_log_sdk__("c", r)
                }, o = function () {
                    e.video.played;
                    var t = s(e.logParams.played), n = (new Date).getTime();
                    u();
                    var r = {
                        url: e.logParams.pluginSrc ? e.logParams.pluginSrc : e.logParams.playSrc,
                        vid: e.config.vid,
                        bc: e.logParams.bc - 1 > 0 ? e.logParams.bc - 1 : 0,
                        bb: e.logParams.bc - 1 > 0 ? 1 : 0,
                        bu_acu_t: e.logParams.bu_acu_t,
                        pt: e.logParams.pt,
                        vt: e.logParams.vt,
                        vd: 1e3 * e.logParams.vd,
                        watch_dur: parseFloat((1e3 * t).toFixed(3)),
                        cur_play_pos: parseFloat((1e3 * e.currentTime).toFixed(3)),
                        lt: n
                    };
                    window.__xigua_log_sdk__("d", r)
                }, l = function (t) {
                    e.video.played;
                    var n = s(e.logParams.played);
                    u();
                    var r = (new Date).getTime();
                    if (!(e.logParams.lastErrLog && r - e.logParams.lastErrLog <= 3e3)) {
                        e.logParams.lastErrLog = r;
                        var a = {
                            url: e.logParams.pluginSrc ? e.logParams.pluginSrc : e.logParams.playSrc,
                            vid: e.config.vid,
                            bc: e.logParams.bc - 1 > 0 ? e.logParams.bc - 1 : 0,
                            bb: e.logParams.bc - 1 > 0 ? 1 : 0,
                            bu_acu_t: e.logParams.bu_acu_t,
                            pt: e.logParams.pt,
                            vt: e.logParams.vt,
                            vd: 1e3 * e.logParams.vd,
                            watch_dur: parseFloat((1e3 * n).toFixed(3)),
                            err_msg: t.errd.msg,
                            line: t.errd.line,
                            et: r,
                            cur_play_pos: parseFloat((1e3 * e.currentTime).toFixed(3))
                        };
                        if (e.logParams.nologFunc && e.logParams.nologFunc(e)) return !0;
                        window.__xigua_log_sdk__("e", a)
                    }
                };
                window.__xigua_log_sdk__ || (window.__xigua_log_sdk__ = new i.default("tracker"), window.__xigua_log_sdk__.init({
                    app_id: 1300,
                    channel: "cn",
                    log: !1,
                    disable_sdk_monitor: !0
                }), window.__xigua_log_sdk__("config", {
                    evtParams: {
                        log_type: "logger",
                        page_url: document.URL,
                        domain: window.location.host,
                        pver: e.version,
                        ua: navigator.userAgent.toLowerCase()
                    }, disable_auto_pv: !0
                }), window.__xigua_log_sdk__.start()), e.config.uid && window.__xigua_log_sdk__("config", {user_unique_id: e.config.uid});
                var s = function () {
                    for (var e = arguments.length > 0 && void 0 !== arguments[0] ? arguments[0] : [], t = [], n = 0; n < e.length; n++) if (!(!e[n].end || e[n].begin < 0 || e[n].end < 0 || e[n].end < e[n].begin)) if (t.length < 1) t.push({
                        begin: e[n].begin,
                        end: e[n].end
                    }); else for (var r = 0; r < t.length; r++) {
                        var a = e[n].begin, i = e[n].end;
                        if (i < t[r].begin) {
                            t.splice(r, 0, {begin: a, end: i});
                            break
                        }
                        if (!(a > t[r].end)) {
                            var o = t[r].begin, l = t[r].end;
                            t[r].begin = Math.min(a, o), t[r].end = Math.max(i, l);
                            break
                        }
                        if (r > t.length - 2) {
                            t.push({begin: a, end: i});
                            break
                        }
                    }
                    for (var s = 0, u = 0; u < t.length; u++) s += t[u].end - t[u].begin;
                    return s
                }, u = function () {
                    e.logParams.pt && e.logParams.vt || (e.logParams.pt = (new Date).getTime(), e.logParams.vt = e.logParams.pt), e.logParams.pt > e.logParams.vt && (e.logParams.pt = e.logParams.vt)
                }, c = function (n) {
                    if (t.hasClass(e.root, "xgplayer-is-enter")) {
                        var r = (new Date).getTime(), a = {
                            url: e.logParams.pluginSrc ? e.logParams.pluginSrc : e.logParams.playSrc,
                            vid: e.config.vid,
                            pt: e.logParams.pt,
                            lt: r
                        };
                        window.__xigua_log_sdk__("b", a)
                    } else if (t.hasClass(e.root, "xgplayer-playing")) {
                        var i = s(e.logParams.played), o = (new Date).getTime();
                        u();
                        var l = {
                            url: e.logParams.pluginSrc ? e.logParams.pluginSrc : e.logParams.playSrc,
                            vid: e.config.vid,
                            bc: e.logParams.bc - 1 > 0 ? e.logParams.bc - 1 : 0,
                            bb: e.logParams.bc - 1 > 0 ? 1 : 0,
                            bu_acu_t: e.logParams.bu_acu_t,
                            pt: e.logParams.pt,
                            vt: e.logParams.vt,
                            vd: 1e3 * e.logParams.vd,
                            watch_dur: parseFloat((1e3 * i).toFixed(3)),
                            cur_play_pos: parseFloat((1e3 * e.currentTime).toFixed(3)),
                            lt: o
                        };
                        window.__xigua_log_sdk__("d", l)
                    }
                };
                "pc" === a.default.device ? window.addEventListener("beforeunload", c, !1) : "mobile" === a.default.device && window.addEventListener("pagehide", c, !1), e.on("routechange", c), e.on("ended", n), e.on("urlchange", o), e.on("error", l), e.once("destroy", function t() {
                    "pc" === a.default.device ? window.removeEventListener("beforeunload", c) : "mobile" === a.default.device && window.removeEventListener("pagehide", c), e.off("routechange", c), e.off("ended", n), e.off("urlchange", o), e.off("error", l), e.off("destroy", t)
                })
            }
        })
    }, function (e, t, n) {
        "use strict";
        var r, a = n(0);
        ((r = a) && r.__esModule ? r : {default: r}).default.install("localPreview", function () {
            var e = this, t = e.root;

            function n(n) {
                e.uploadFile = n.files[0];
                var r = URL.createObjectURL(e.uploadFile);
                util.hasClass(t, "xgplayer-nostart") ? (e.config.url = r, e.start()) : (e.src = r, e.play())
            }

            e.on("upload", n), e.once("destroy", function t() {
                e.off("upload", n), e.off("destroy", t)
            })
        })
    }, function (e, t, n) {
        "use strict";
        var r, a = n(0);
        ((r = a) && r.__esModule ? r : {default: r}).default.install("i18n", function () {
            var e = this, t = {}, n = e.constructor.util;
            t.en = {
                HAVE_NOTHING: "There is no information on whether audio/video is ready",
                HAVE_METADATA: "Audio/video metadata is ready ",
                HAVE_CURRENT_DATA: "Data about the current play location is available, but there is not enough data to play the next frame/millisecond",
                HAVE_FUTURE_DATA: "Current and at least one frame of data is available",
                HAVE_ENOUGH_DATA: "The available data is sufficient to start playing",
                NETWORK_EMPTY: "Audio/video has not been initialized",
                NETWORK_IDLE: "Audio/video is active and has been selected for resources, but no network is used",
                NETWORK_LOADING: "The browser is downloading the data",
                NETWORK_NO_SOURCE: "No audio/video source was found",
                MEDIA_ERR_ABORTED: "The fetch process is aborted by the user",
                MEDIA_ERR_NETWORK: "An error occurred while downloading",
                MEDIA_ERR_DECODE: "An error occurred while decoding",
                MEDIA_ERR_SRC_NOT_SUPPORTED: "Audio/video is not supported",
                REPLAY: "Replay",
                ERROR: "Network is offline",
                PLAY_TIPS: "Play",
                PAUSE_TIPS: "Pause",
                PLAYNEXT_TIPS: "Play next",
                DOWNLOAD_TIPS: "Download",
                ROTATE_TIPS: "Rotate",
                FULLSCREEN_TIPS: "Fullscreen",
                EXITFULLSCREEN_TIPS: "Exit fullscreen",
                CSSFULLSCREEN_TIPS: "Cssfullscreen",
                EXITCSSFULLSCREEN_TIPS: "Exit cssfullscreen",
                TEXTTRACK: "Caption",
                PIP: "Pip",
                SCREENSHOT: "Screenshot"
            }, t["zh-cn"] = {
                HAVE_NOTHING: "没有关于音频/视频是否就绪的信息",
                HAVE_METADATA: "音频/视频的元数据已就绪",
                HAVE_CURRENT_DATA: "关于当前播放位置的数据是可用的，但没有足够的数据来播放下一帧/毫秒",
                HAVE_FUTURE_DATA: "当前及至少下一帧的数据是可用的",
                HAVE_ENOUGH_DATA: "可用数据足以开始播放",
                NETWORK_EMPTY: "音频/视频尚未初始化",
                NETWORK_IDLE: "音频/视频是活动的且已选取资源，但并未使用网络",
                NETWORK_LOADING: "浏览器正在下载数据",
                NETWORK_NO_SOURCE: "未找到音频/视频来源",
                MEDIA_ERR_ABORTED: "取回过程被用户中止",
                MEDIA_ERR_NETWORK: "当下载时发生错误",
                MEDIA_ERR_DECODE: "当解码时发生错误",
                MEDIA_ERR_SRC_NOT_SUPPORTED: "不支持的音频/视频格式",
                REPLAY: "重播",
                ERROR: "网络连接似乎出现了问题",
                PLAY_TIPS: "播放",
                PAUSE_TIPS: "暂停",
                PLAYNEXT_TIPS: "下一集",
                DOWNLOAD_TIPS: "下载",
                ROTATE_TIPS: "旋转",
                FULLSCREEN_TIPS: "进入全屏",
                EXITFULLSCREEN_TIPS: "退出全屏",
                CSSFULLSCREEN_TIPS: "进入样式全屏",
                EXITCSSFULLSCREEN_TIPS: "退出样式全屏",
                TEXTTRACK: "字幕",
                PIP: "画中画",
                SCREENSHOT: "截图"
            }, t.jp = {
                HAVE_NOTHING: "オーディオ/ビデオが準備できているか情報がありません",
                HAVE_METADATA: "オーディオ/ビデオのメタデータは準備できています",
                HAVE_CURRENT_DATA: "現在の再生位置に関するデータは利用可能ですが、次のフレーム/ミリ秒を再生するのに十分なデータがありません",
                HAVE_FUTURE_DATA: "現在、少なくとも次のフレームのデータが利用可能です",
                HAVE_ENOUGH_DATA: "利用可能なデータは再生を開始するのに十分です",
                NETWORK_EMPTY: "オーディオ/ビデオが初期化されていません",
                NETWORK_IDLE: "オーディオ/ビデオはアクティブでリソースが選択されていますが、ネットワークが使用されていません",
                NETWORK_LOADING: "ブラウザーはデータをダウンロードしています",
                NETWORK_NO_SOURCE: "オーディオ/ビデオ のソースが見つかりません",
                MEDIA_ERR_ABORTED: "ユーザーによってフェッチプロセスが中止されました",
                MEDIA_ERR_NETWORK: "ダウンロード中にエラーが発生しました",
                MEDIA_ERR_DECODE: "デコード中にエラーが発生しました",
                MEDIA_ERR_SRC_NOT_SUPPORTED: "オーディオ/ビデオ の形式がサポートされていません",
                REPLAY: "リプレイ",
                ERROR: "ネットワークの接続に問題が発生しました",
                PLAY_TIPS: "プレイ",
                PAUSE_TIPS: "一時停止",
                PLAYNEXT_TIPS: "次をプレイ",
                DOWNLOAD_TIPS: "ダウンロード",
                ROTATE_TIPS: "回転",
                FULLSCREEN_TIPS: "フルスクリーン",
                EXITFULLSCREEN_TIPS: "フルスクリーンを終了",
                CSSFULLSCREEN_TIPS: "シアターモード",
                EXITCSSFULLSCREEN_TIPS: "シアターモードを終了",
                TEXTTRACK: "字幕",
                PIP: "ミニプレーヤー",
                SCREENSHOT: "スクリーンショット"
            }, Object.defineProperty(e, "lang", {
                get: function () {
                    return t[e.config.lang] || t.en
                }, set: function (e) {
                    "Object" === n.typeOf(e) && Object.keys(e).forEach(function (n) {
                        t[n] = e[n]
                    })
                }
            })
        })
    }, function (e, t, n) {
        "use strict";
        var r, a = n(0), i = (r = a) && r.__esModule ? r : {default: r};
        i.default.install("fullscreen", function () {
            var e = this, t = e.root, n = i.default.util;

            function r() {
                n.hasClass(t, "xgplayer-is-fullscreen") ? e.exitFullscreen(t) : e.getFullscreen(t)
            }

            function a() {
                var r = document.fullscreenElement || document.webkitFullscreenElement || document.mozFullScreenElement || document.msFullscreenElement;
                r && r === t ? (n.addClass(t, "xgplayer-is-fullscreen"), e.emit("requestFullscreen")) : (n.removeClass(t, "xgplayer-is-fullscreen"), e.emit("exitFullscreen"))
            }

            e.on("fullscreenBtnClick", r), ["fullscreenchange", "webkitfullscreenchange", "mozfullscreenchange", "MSFullscreenChange"].forEach(function (e) {
                document.addEventListener(e, a)
            }), e.once("destroy", function t() {
                e.off("fullscreenBtnClick", r), ["fullscreenchange", "webkitfullscreenchange", "mozfullscreenchange", "MSFullscreenChange"].forEach(function (e) {
                    document.removeEventListener(e, a)
                }), e.off("destroy", t)
            })
        })
    }, function (e, t, n) {
        "use strict";
        var r, a = n(0);
        ((r = a) && r.__esModule ? r : {default: r}).default.install("download", function () {
            var e = this;

            function t() {
                e.download()
            }

            e.on("downloadBtnClick", t), e.once("destroy", function n() {
                e.off("downloadBtnClick", t), e.off("destroy", n)
            })
        })
    }, function (e, t, n) {
        "use strict";
        var r, a = n(0);
        ((r = a) && r.__esModule ? r : {default: r}).default.install("definition", function () {
            var e = this;
            e.root;
            e.once("destroy", function t() {
                e.off("destroy", t)
            })
        })
    }, function (e, t, n) {
        "use strict";
        var r, a = n(0), i = (r = a) && r.__esModule ? r : {default: r};
        i.default.install("danmu", function () {
            var e = this, t = (e.root, i.default.util);
            e.on("initDefaultDanmu", function (n) {
                var r = e.root.querySelector("xg-danmu");
                if (t.addClass(r, "xgplayer-has-danmu"), !e.config.danmu.closeDefaultBtn) {
                    var a = function () {
                        n.start()
                    }, i = function () {
                        t.hasClass(e.danmuBtn, "danmu-switch-active") && n.pause()
                    }, o = function () {
                        t.hasClass(e.danmuBtn, "danmu-switch-active") && n.play()
                    }, l = function () {
                        t.hasClass(e.danmuBtn, "danmu-switch-active") && (n.stop(), n.start())
                    };
                    e.danmuBtn = t.copyDom(n.bulletBtn.createSwitch(!0)), e.controls.appendChild(e.danmuBtn), ["click", "touchend"].forEach(function (i) {
                        e.danmuBtn.addEventListener(i, function (i) {
                            i.preventDefault(), i.stopPropagation(), t.toggleClass(e.danmuBtn, "danmu-switch-active"), t.hasClass(e.danmuBtn, "danmu-switch-active") ? (t.addClass(r, "xgplayer-has-danmu"), e.once("timeupdate", a)) : (t.removeClass(r, "xgplayer-has-danmu"), n.stop())
                        })
                    }), e.onElementClick && r.addEventListener("click", function (t) {
                        e.onElementClick(t, r)
                    }, !1), e.onElementDblclick && r.addEventListener("dblclick", function (t) {
                        e.onElementDblclick(t, r)
                    }, !1), e.on("pause", i), e.on("play", o), e.on("seeked", l), e.once("destroy", function t() {
                        e.off("timeupdate", a), e.off("pause", i), e.off("play", o), e.off("seeked", l), e.off("destroy", t)
                    })
                }
            })
        })
    }, function (e, t, n) {
        "use strict";
        var r, a = n(0), i = (r = a) && r.__esModule ? r : {default: r};
        i.default.install("cssFullscreen", function () {
            var e = this, t = e.root, n = i.default.util;

            function r() {
                n.hasClass(t, "xgplayer-is-cssfullscreen") ? n.removeClass(t, "xgplayer-is-cssfullscreen") : n.addClass(t, "xgplayer-is-cssfullscreen")
            }

            e.on("cssFullscreenBtnClick", r), e.on("exitFullscreen", function () {
                n.removeClass(t, "xgplayer-is-cssfullscreen")
            }), e.once("destroy", function t() {
                e.off("cssFullscreenBtnClick", r), e.off("destroy", t)
            })
        })
    }, function (e, t, n) {
        "use strict";
        n(53), n(58), n(59), n(62), n(65), n(66), n(67), n(70), n(73), n(77), n(78), n(80), n(81), n(82), n(84), n(85), n(86), n(88), n(92), n(93), n(95), n(97), n(98), n(99)
    }, function (e, t, n) {
        var r = n(54);
        "string" == typeof r && (r = [[e.i, r, ""]]);
        var a = {hmr: !0, transform: void 0, insertInto: void 0};
        n(56)(r, a);
        r.locals && (e.exports = r.locals)
    }, function (e, t, n) {
        (e.exports = n(55)(!1)).push([e.i, '.xgplayer-skin-default{background:#000;width:100%;height:100%;position:relative;-webkit-user-select:none;-moz-user-select:none;user-select:none;-ms-user-select:none}.xgplayer-skin-default *{margin:0;padding:0;border:0;font-size:100%;font:inherit;vertical-align:baseline}.xgplayer-skin-default.xgplayer-is-fullscreen{width:100%!important;height:100%!important;padding-top:0!important;z-index:9999}.xgplayer-skin-default.xgplayer-is-fullscreen.xgplayer-inactive{cursor:none}.xgplayer-skin-default video{width:100%;height:100%;outline:none}.xgplayer-skin-default .xgplayer-none{display:none}.xgplayer-skin-default .xgplayer-play,.xgplayer-skin-default .xgplayer-play-img{position:relative;-webkit-order:0;-moz-box-ordinal-group:1;order:0;display:block;cursor:pointer}.xgplayer-skin-default .xgplayer-play-img .xgplayer-icon,.xgplayer-skin-default .xgplayer-play .xgplayer-icon{margin-top:3px;width:32px}.xgplayer-skin-default .xgplayer-play-img .xgplayer-icon div,.xgplayer-skin-default .xgplayer-play .xgplayer-icon div{position:absolute}.xgplayer-skin-default .xgplayer-play-img .xgplayer-icon .xgplayer-icon-play,.xgplayer-skin-default .xgplayer-play .xgplayer-icon .xgplayer-icon-play{display:block}.xgplayer-skin-default .xgplayer-play-img .xgplayer-icon .xgplayer-icon-pause,.xgplayer-skin-default .xgplayer-play .xgplayer-icon .xgplayer-icon-pause{display:none}.xgplayer-skin-default .xgplayer-play-img .xgplayer-tips .xgplayer-tip-play,.xgplayer-skin-default .xgplayer-play .xgplayer-tips .xgplayer-tip-play{display:block}.xgplayer-skin-default .xgplayer-play-img .xgplayer-tips .xgplayer-tip-pause,.xgplayer-skin-default .xgplayer-play .xgplayer-tips .xgplayer-tip-pause{display:none}.xgplayer-skin-default .xgplayer-play-img:hover,.xgplayer-skin-default .xgplayer-play:hover{opacity:.85}.xgplayer-skin-default .xgplayer-play-img:hover .xgplayer-tips,.xgplayer-skin-default .xgplayer-play:hover .xgplayer-tips{display:block}.xgplayer-skin-default.xgplayer-playing .xgplayer-play-img .xgplayer-icon .xgplayer-icon-play,.xgplayer-skin-default.xgplayer-playing .xgplayer-play .xgplayer-icon .xgplayer-icon-play{display:none}.xgplayer-skin-default.xgplayer-playing .xgplayer-play-img .xgplayer-icon .xgplayer-icon-pause,.xgplayer-skin-default.xgplayer-playing .xgplayer-play .xgplayer-icon .xgplayer-icon-pause{display:block}.xgplayer-skin-default.xgplayer-playing .xgplayer-play-img .xgplayer-tips .xgplayer-tip-play,.xgplayer-skin-default.xgplayer-playing .xgplayer-play .xgplayer-tips .xgplayer-tip-play{display:none}.xgplayer-skin-default.xgplayer-pause .xgplayer-play-img .xgplayer-icon .xgplayer-icon-play,.xgplayer-skin-default.xgplayer-pause .xgplayer-play .xgplayer-icon .xgplayer-icon-play,.xgplayer-skin-default.xgplayer-playing .xgplayer-play-img .xgplayer-tips .xgplayer-tip-pause,.xgplayer-skin-default.xgplayer-playing .xgplayer-play .xgplayer-tips .xgplayer-tip-pause{display:block}.xgplayer-skin-default.xgplayer-pause .xgplayer-play-img .xgplayer-icon .xgplayer-icon-pause,.xgplayer-skin-default.xgplayer-pause .xgplayer-play .xgplayer-icon .xgplayer-icon-pause{display:none}.xgplayer-skin-default.xgplayer-pause .xgplayer-play-img .xgplayer-tips .xgplayer-tip-play,.xgplayer-skin-default.xgplayer-pause .xgplayer-play .xgplayer-tips .xgplayer-tip-play{display:block}.xgplayer-skin-default.xgplayer-pause .xgplayer-play-img .xgplayer-tips .xgplayer-tip-pause,.xgplayer-skin-default.xgplayer-pause .xgplayer-play .xgplayer-tips .xgplayer-tip-pause{display:none}.xgplayer-skin-default .xgplayer-start{border-radius:50%;display:inline-block;width:70px;height:70px;background:rgba(0,0,0,.38);overflow:hidden;text-align:center;line-height:70px;vertical-align:middle;position:absolute;left:50%;top:50%;z-index:115;margin:-35px auto auto -35px;cursor:pointer}.xgplayer-skin-default .xgplayer-start div{position:absolute}.xgplayer-skin-default .xgplayer-start div svg{fill:hsla(0,0%,100%,.7);margin:14px}.xgplayer-skin-default .xgplayer-start .xgplayer-icon-play{display:block}.xgplayer-skin-default .xgplayer-start .xgplayer-icon-pause{display:none}.xgplayer-skin-default .xgplayer-start:hover{opacity:.85}.xgplayer-skin-default.xgplayer-playing .xgplayer-start,.xgplayer-skin-default.xgplayer-playing .xgplayer-start .xgplayer-icon-play{display:none}.xgplayer-skin-default.xgplayer-playing .xgplayer-start .xgplayer-icon-pause{display:block}.xgplayer-skin-default.xgplayer-pause .xgplayer-start{display:inline-block}.xgplayer-skin-default.xgplayer-pause .xgplayer-start .xgplayer-icon-play{display:block}.xgplayer-skin-default.replay .xgplayer-start,.xgplayer-skin-default.xgplayer-pause .xgplayer-start .xgplayer-icon-pause{display:none}.xgplayer-skin-default.replay .xgplayer-start .xgplayer-icon-play{display:block}.xgplayer-skin-default.replay .xgplayer-start .xgplayer-icon-pause{display:none}.xgplayer-skin-default .xgplayer-enter{display:none;position:absolute;left:0;top:0;width:100%;height:100%;background:url("//s2.pstatp.com/cdn/expire-1-M/byted-player-images/1.0.2/bg.svg") no-repeat 50%/cover;z-index:120}.xgplayer-skin-default .xgplayer-enter .xgplayer-enter-spinner{display:block;position:absolute;left:50%;top:50%;height:100px;width:100px;position:relative;-webkit-transform:translate(-50%,-50%);-ms-transform:translate(-50%,-50%);transform:translate(-50%,-50%)}.xgplayer-skin-default .xgplayer-enter .xgplayer-enter-spinner div{width:12%;height:26%;background-color:hsla(0,0%,100%,.7);position:absolute;left:44%;top:37%;opacity:0;border-radius:30px;-webkit-animation:fade 1s linear infinite;animation:fade 1s linear infinite}.xgplayer-skin-default .xgplayer-enter .xgplayer-enter-spinner div.xgplayer-enter-bar1{-webkit-transform:rotate(0deg) translateY(-142%);-ms-transform:rotate(0deg) translateY(-142%);transform:rotate(0deg) translateY(-142%);-webkit-animation-delay:0s;animation-delay:0s}.xgplayer-skin-default .xgplayer-enter .xgplayer-enter-spinner div.xgplayer-enter-bar2{-webkit-transform:rotate(30deg) translateY(-142%);-ms-transform:rotate(30deg) translateY(-142%);transform:rotate(30deg) translateY(-142%);-webkit-animation-delay:-.9163s;animation-delay:-.9163s}.xgplayer-skin-default .xgplayer-enter .xgplayer-enter-spinner div.xgplayer-enter-bar3{-webkit-transform:rotate(60deg) translateY(-142%);-ms-transform:rotate(60deg) translateY(-142%);transform:rotate(60deg) translateY(-142%);-webkit-animation-delay:-.833s;animation-delay:-.833s}.xgplayer-skin-default .xgplayer-enter .xgplayer-enter-spinner div.xgplayer-enter-bar4{-webkit-transform:rotate(90deg) translateY(-142%);-ms-transform:rotate(90deg) translateY(-142%);transform:rotate(90deg) translateY(-142%);-webkit-animation-delay:-.7497s;animation-delay:-.7497s}.xgplayer-skin-default .xgplayer-enter .xgplayer-enter-spinner div.xgplayer-enter-bar5{-webkit-transform:rotate(120deg) translateY(-142%);-ms-transform:rotate(120deg) translateY(-142%);transform:rotate(120deg) translateY(-142%);-webkit-animation-delay:-.6664s;animation-delay:-.6664s}.xgplayer-skin-default .xgplayer-enter .xgplayer-enter-spinner div.xgplayer-enter-bar6{-webkit-transform:rotate(150deg) translateY(-142%);-ms-transform:rotate(150deg) translateY(-142%);transform:rotate(150deg) translateY(-142%);-webkit-animation-delay:-.5831s;animation-delay:-.5831s}.xgplayer-skin-default .xgplayer-enter .xgplayer-enter-spinner div.xgplayer-enter-bar7{-webkit-transform:rotate(180deg) translateY(-142%);-ms-transform:rotate(180deg) translateY(-142%);transform:rotate(180deg) translateY(-142%);-webkit-animation-delay:-.4998s;animation-delay:-.4998s}.xgplayer-skin-default .xgplayer-enter .xgplayer-enter-spinner div.xgplayer-enter-bar8{-webkit-transform:rotate(210deg) translateY(-142%);-ms-transform:rotate(210deg) translateY(-142%);transform:rotate(210deg) translateY(-142%);-webkit-animation-delay:-.4165s;animation-delay:-.4165s}.xgplayer-skin-default .xgplayer-enter .xgplayer-enter-spinner div.xgplayer-enter-bar9{-webkit-transform:rotate(240deg) translateY(-142%);-ms-transform:rotate(240deg) translateY(-142%);transform:rotate(240deg) translateY(-142%);-webkit-animation-delay:-.3332s;animation-delay:-.3332s}.xgplayer-skin-default .xgplayer-enter .xgplayer-enter-spinner div.xgplayer-enter-bar10{-webkit-transform:rotate(270deg) translateY(-142%);-ms-transform:rotate(270deg) translateY(-142%);transform:rotate(270deg) translateY(-142%);-webkit-animation-delay:-.2499s;animation-delay:-.2499s}.xgplayer-skin-default .xgplayer-enter .xgplayer-enter-spinner div.xgplayer-enter-bar11{-webkit-transform:rotate(300deg) translateY(-142%);-ms-transform:rotate(300deg) translateY(-142%);transform:rotate(300deg) translateY(-142%);-webkit-animation-delay:-.1666s;animation-delay:-.1666s}.xgplayer-skin-default .xgplayer-enter .xgplayer-enter-spinner div.xgplayer-enter-bar12{-webkit-transform:rotate(330deg) translateY(-142%);-ms-transform:rotate(330deg) translateY(-142%);transform:rotate(330deg) translateY(-142%);-webkit-animation-delay:-.0833s;animation-delay:-.0833s}@-webkit-keyframes fade{0%{opacity:1}to{opacity:.25}}.xgplayer-skin-default.xgplayer-is-enter .xgplayer-enter{display:block}.xgplayer-skin-default .xgplayer-poster{display:none;position:absolute;left:0;top:0;width:100%;height:100%;z-index:100;background-size:cover;background-position:50%}.xgplayer-skin-default.xgplayer-nostart .xgplayer-poster{display:block}.xgplayer-skin-default .xgplayer-placeholder{-webkit-flex:1;-moz-box-flex:1;flex:1;-webkit-order:3;-moz-box-ordinal-group:4;order:3;display:block}.xgplayer-skin-default .xgplayer-fullscreen,.xgplayer-skin-default .xgplayer-fullscreen-img{position:relative;-webkit-order:13;-moz-box-ordinal-group:14;order:13;display:block;cursor:pointer}.xgplayer-skin-default .xgplayer-fullscreen-img .xgplayer-icon,.xgplayer-skin-default .xgplayer-fullscreen .xgplayer-icon{margin-top:3px}.xgplayer-skin-default .xgplayer-fullscreen-img .xgplayer-icon div,.xgplayer-skin-default .xgplayer-fullscreen .xgplayer-icon div{position:absolute}.xgplayer-skin-default .xgplayer-fullscreen-img .xgplayer-icon .xgplayer-icon-requestfull,.xgplayer-skin-default .xgplayer-fullscreen .xgplayer-icon .xgplayer-icon-requestfull{display:block}.xgplayer-skin-default .xgplayer-fullscreen-img .xgplayer-icon .xgplayer-icon-exitfull,.xgplayer-skin-default .xgplayer-fullscreen .xgplayer-icon .xgplayer-icon-exitfull{display:none}.xgplayer-skin-default .xgplayer-fullscreen-img .xgplayer-tips,.xgplayer-skin-default .xgplayer-fullscreen .xgplayer-tips{position:absolute;right:0;left:auto}.xgplayer-skin-default .xgplayer-fullscreen-img .xgplayer-tips .xgplayer-tip-requestfull,.xgplayer-skin-default .xgplayer-fullscreen .xgplayer-tips .xgplayer-tip-requestfull{display:block}.xgplayer-skin-default .xgplayer-fullscreen-img .xgplayer-tips .xgplayer-tip-exitfull,.xgplayer-skin-default .xgplayer-fullscreen .xgplayer-tips .xgplayer-tip-exitfull{display:none}.xgplayer-skin-default .xgplayer-fullscreen-img:hover,.xgplayer-skin-default .xgplayer-fullscreen:hover{opacity:.85}.xgplayer-skin-default .xgplayer-fullscreen-img:hover .xgplayer-tips,.xgplayer-skin-default .xgplayer-fullscreen:hover .xgplayer-tips{display:block}.xgplayer-skin-default.xgplayer-is-fullscreen .xgplayer-fullscreen-img .xgplayer-icon .xgplayer-icon-requestfull,.xgplayer-skin-default.xgplayer-is-fullscreen .xgplayer-fullscreen .xgplayer-icon .xgplayer-icon-requestfull{display:none}.xgplayer-skin-default.xgplayer-is-fullscreen .xgplayer-fullscreen-img .xgplayer-icon .xgplayer-icon-exitfull,.xgplayer-skin-default.xgplayer-is-fullscreen .xgplayer-fullscreen .xgplayer-icon .xgplayer-icon-exitfull{display:block}.xgplayer-skin-default.xgplayer-is-fullscreen .xgplayer-fullscreen-img .xgplayer-tips .xgplayer-tip-requestfull,.xgplayer-skin-default.xgplayer-is-fullscreen .xgplayer-fullscreen .xgplayer-tips .xgplayer-tip-requestfull{display:none}.xgplayer-skin-default.xgplayer-is-fullscreen .xgplayer-fullscreen-img .xgplayer-tips .xgplayer-tip-exitfull,.xgplayer-skin-default.xgplayer-is-fullscreen .xgplayer-fullscreen .xgplayer-tips .xgplayer-tip-exitfull{display:block}.xgplayer-skin-default .xgplayer-cssfullscreen,.xgplayer-skin-default .xgplayer-cssfullscreen-img{position:relative;-webkit-order:12;-moz-box-ordinal-group:13;order:12;display:block;cursor:pointer}.xgplayer-skin-default .xgplayer-cssfullscreen-img .xgplayer-icon,.xgplayer-skin-default .xgplayer-cssfullscreen .xgplayer-icon{width:32px;margin-top:5px}.xgplayer-skin-default .xgplayer-cssfullscreen-img .xgplayer-icon div,.xgplayer-skin-default .xgplayer-cssfullscreen .xgplayer-icon div{position:absolute}.xgplayer-skin-default .xgplayer-cssfullscreen-img .xgplayer-icon .xgplayer-icon-requestfull,.xgplayer-skin-default .xgplayer-cssfullscreen .xgplayer-icon .xgplayer-icon-requestfull{display:block}.xgplayer-skin-default .xgplayer-cssfullscreen-img .xgplayer-icon .xgplayer-icon-exitfull,.xgplayer-skin-default .xgplayer-cssfullscreen .xgplayer-icon .xgplayer-icon-exitfull{display:none}.xgplayer-skin-default .xgplayer-cssfullscreen-img .xgplayer-tips,.xgplayer-skin-default .xgplayer-cssfullscreen .xgplayer-tips{margin-left:-40px}.xgplayer-skin-default .xgplayer-cssfullscreen-img .xgplayer-tips .xgplayer-tip-requestfull,.xgplayer-skin-default .xgplayer-cssfullscreen .xgplayer-tips .xgplayer-tip-requestfull{display:block}.xgplayer-skin-default .xgplayer-cssfullscreen-img .xgplayer-tips .xgplayer-tip-exitfull,.xgplayer-skin-default .xgplayer-cssfullscreen .xgplayer-tips .xgplayer-tip-exitfull{display:none}.xgplayer-skin-default .xgplayer-cssfullscreen-img:hover,.xgplayer-skin-default .xgplayer-cssfullscreen:hover{opacity:.85}.xgplayer-skin-default .xgplayer-cssfullscreen-img:hover .xgplayer-tips,.xgplayer-skin-default .xgplayer-cssfullscreen:hover .xgplayer-tips{display:block}.xgplayer-skin-default.xgplayer-is-cssfullscreen .xgplayer-cssfullscreen-img .xgplayer-icon .xgplayer-icon-requestfull,.xgplayer-skin-default.xgplayer-is-cssfullscreen .xgplayer-cssfullscreen .xgplayer-icon .xgplayer-icon-requestfull{display:none}.xgplayer-skin-default.xgplayer-is-cssfullscreen .xgplayer-cssfullscreen-img .xgplayer-icon .xgplayer-icon-exitfull,.xgplayer-skin-default.xgplayer-is-cssfullscreen .xgplayer-cssfullscreen .xgplayer-icon .xgplayer-icon-exitfull{display:block}.xgplayer-skin-default.xgplayer-is-cssfullscreen .xgplayer-cssfullscreen-img .xgplayer-tips,.xgplayer-skin-default.xgplayer-is-cssfullscreen .xgplayer-cssfullscreen .xgplayer-tips{margin-left:-47px}.xgplayer-skin-default.xgplayer-is-cssfullscreen .xgplayer-cssfullscreen-img .xgplayer-tips .xgplayer-tip-requestfull,.xgplayer-skin-default.xgplayer-is-cssfullscreen .xgplayer-cssfullscreen .xgplayer-tips .xgplayer-tip-requestfull{display:none}.xgplayer-skin-default.xgplayer-is-cssfullscreen .xgplayer-cssfullscreen-img .xgplayer-tips .xgplayer-tip-exitfull,.xgplayer-skin-default.xgplayer-is-cssfullscreen .xgplayer-cssfullscreen .xgplayer-tips .xgplayer-tip-exitfull{display:block}.xgplayer-skin-default.xgplayer-is-fullscreen .xgplayer-cssfullscreen,.xgplayer-skin-default.xgplayer-is-fullscreen .xgplayer-cssfullscreen-img{display:none}.xgplayer-skin-default.xgplayer-is-cssfullscreen{position:fixed!important;left:0!important;top:0!important;width:100%!important;height:100%!important;z-index:99999!important}.lang-is-en .xgplayer-cssfullscreen-img .xgplayer-tips,.lang-is-en .xgplayer-cssfullscreen .xgplayer-tips,.lang-is-en.xgplayer-is-cssfullscreen .xgplayer-cssfullscreen-img .xgplayer-tips,.lang-is-en.xgplayer-is-cssfullscreen .xgplayer-cssfullscreen .xgplayer-tips{margin-left:-46px}.lang-is-jp .xgplayer-cssfullscreen-img .xgplayer-tips,.lang-is-jp .xgplayer-cssfullscreen .xgplayer-tips{margin-left:-120px}.lang-is-jp.xgplayer-is-cssfullscreen .xgplayer-cssfullscreen-img .xgplayer-tips,.lang-is-jp.xgplayer-is-cssfullscreen .xgplayer-cssfullscreen .xgplayer-tips{margin-left:-60px}.xgplayer-skin-default .xgplayer-volume{outline:none;-webkit-order:4;-moz-box-ordinal-group:5;order:4;width:28px;height:140px;display:block;position:relative;margin-top:-100px;z-index:18}.xgplayer-skin-default .xgplayer-volume .xgplayer-icon{margin-top:8px;cursor:pointer;position:absolute;bottom:-9px}.xgplayer-skin-default .xgplayer-volume .xgplayer-icon div{position:absolute}.xgplayer-skin-default .xgplayer-volume .xgplayer-icon .xgplayer-icon-large{display:block}.xgplayer-skin-default .xgplayer-volume .xgplayer-icon .xgplayer-icon-muted,.xgplayer-skin-default .xgplayer-volume .xgplayer-icon .xgplayer-icon-small{display:none}.xgplayer-skin-default .xgplayer-slider{display:none;position:absolute;width:28px;height:88px;background:rgba(0,0,0,.54);border-radius:1px;bottom:42px;outline:none}.xgplayer-skin-default .xgplayer-slider:after{content:" ";display:block;height:15px;width:28px;position:absolute;bottom:-15px;left:0;z-index:20}.xgplayer-skin-default .xgplayer-bar,.xgplayer-skin-default .xgplayer-drag{display:block;position:absolute;bottom:6px;left:12px;background:hsla(0,0%,100%,.3);border-radius:100px;width:4px;height:76px;outline:none;cursor:pointer}.xgplayer-skin-default .xgplayer-drag{bottom:0;left:0;background:#fa1f41;max-height:76px}.xgplayer-skin-default .xgplayer-drag:after{content:" ";display:inline-block;width:8px;height:8px;background:#fff;box-shadow:0 0 5px 0 rgba(0,0,0,.26);position:absolute;border-radius:50%;left:-2px;top:-6px}.xgplayer-skin-default.xgplayer-volume-active .xgplayer-slider,.xgplayer-skin-default.xgplayer-volume-large .xgplayer-volume .xgplayer-icon .xgplayer-icon-large{display:block}.xgplayer-skin-default.xgplayer-volume-large .xgplayer-volume .xgplayer-icon .xgplayer-icon-muted,.xgplayer-skin-default.xgplayer-volume-large .xgplayer-volume .xgplayer-icon .xgplayer-icon-small,.xgplayer-skin-default.xgplayer-volume-small .xgplayer-volume .xgplayer-icon .xgplayer-icon-large{display:none}.xgplayer-skin-default.xgplayer-volume-small .xgplayer-volume .xgplayer-icon .xgplayer-icon-small{display:block}.xgplayer-skin-default.xgplayer-volume-muted .xgplayer-volume .xgplayer-icon .xgplayer-icon-large,.xgplayer-skin-default.xgplayer-volume-muted .xgplayer-volume .xgplayer-icon .xgplayer-icon-small,.xgplayer-skin-default.xgplayer-volume-small .xgplayer-volume .xgplayer-icon .xgplayer-icon-muted{display:none}.xgplayer-skin-default.xgplayer-volume-muted .xgplayer-volume .xgplayer-icon .xgplayer-icon-muted{display:block}.xgplayer-skin-default .xgplayer-definition{-webkit-order:5;-moz-box-ordinal-group:6;order:5;width:60px;height:150px;z-index:18;position:relative;outline:none;display:none;cursor:default;margin-left:10px;margin-top:-119px}.xgplayer-skin-default .xgplayer-definition ul{display:none;list-style:none;width:78px;background:rgba(0,0,0,.54);border-radius:1px;position:absolute;bottom:30px;left:0;text-align:center;white-space:nowrap;margin-left:-10px;z-index:26;cursor:pointer}.xgplayer-skin-default .xgplayer-definition ul li{opacity:.7;font-family:PingFangSC-Regular;font-size:11px;color:hsla(0,0%,100%,.8);padding:6px 13px}.xgplayer-skin-default .xgplayer-definition ul li.selected,.xgplayer-skin-default .xgplayer-definition ul li:hover{color:#fff;opacity:1}.xgplayer-skin-default .xgplayer-definition .name{text-align:center;font-family:PingFangSC-Regular;font-size:13px;cursor:pointer;color:hsla(0,0%,100%,.8);position:absolute;bottom:0;width:60px;height:20px;line-height:20px;background:rgba(0,0,0,.38);border-radius:10px;display:inline-block;vertical-align:middle}.xgplayer-skin-default.xgplayer-definition-active .xgplayer-definition ul,.xgplayer-skin-default.xgplayer-is-definition .xgplayer-definition{display:block}.xgplayer-skin-default .xgplayer-time{-webkit-order:2;-moz-box-ordinal-group:3;order:2;font-family:ArialMT;font-size:13px;color:#fff;line-height:40px;text-align:center;display:inline-block}.xgplayer-skin-default .xgplayer-time span:after{content:"/";display:inline-block;padding:0 3px}.xgplayer-skin-default .xgplayer-time em{color:hsla(0,0%,100%,.5)}.xgplayer-skin-default .xgplayer-controls{display:-webkit-flex;display:-moz-box;display:flex;position:absolute;bottom:0;left:0;right:0;height:40px;background-image:linear-gradient(180deg,transparent,rgba(0,0,0,.37),rgba(0,0,0,.75),rgba(0,0,0,.75));z-index:10}.xgplayer-skin-default.no-controls .xgplayer-controls,.xgplayer-skin-default.xgplayer-inactive .xgplayer-controls,.xgplayer-skin-default.xgplayer-is-live .xgplayer-controls>*,.xgplayer-skin-default.xgplayer-nostart .xgplayer-controls{display:none}.xgplayer-skin-default.xgplayer-is-live .xgplayer-controls .xgplayer-fullscreen,.xgplayer-skin-default.xgplayer-is-live .xgplayer-controls .xgplayer-live,.xgplayer-skin-default.xgplayer-is-live .xgplayer-controls .xgplayer-placeholder,.xgplayer-skin-default.xgplayer-is-live .xgplayer-controls .xgplayer-play,.xgplayer-skin-default.xgplayer-is-live .xgplayer-controls .xgplayer-play-img,.xgplayer-skin-default.xgplayer-is-live .xgplayer-controls .xgplayer-volume{display:block}.xgplayer-skin-default .xgplayer-live{display:block;font-size:12px;color:#fff;line-height:40px;-webkit-order:1;-moz-box-ordinal-group:2;order:1}.xgplayer-skin-default .xgplayer-loading{display:none;width:100px;height:100px;overflow:hidden;-webkit-transform:scale(.7);-ms-transform:scale(.7);transform:scale(.7);position:absolute;left:50%;top:50%;margin:-70px auto auto -50px}.xgplayer-skin-default .xgplayer-loading svg{border-radius:50%;-webkit-transform-origin:center;-ms-transform-origin:center;transform-origin:center;-webkit-animation:loadingRotate 1s linear infinite;animation:loadingRotate 1s linear infinite}.xgplayer-skin-default .xgplayer-loading svg path{stroke:#ddd;stroke-dasharray:236;-webkit-animation:loadingDashOffset 2s linear infinite;animation:loadingDashOffset 2s linear infinite;animation-direction:alternate-reverse;fill:none;stroke-width:12px}.xgplayer-skin-default.xgplayer-nostart .xgplayer-loading{display:none}.xgplayer-skin-default.xgplayer-isloading .xgplayer-loading{display:block}.xgplayer-skin-default .xgplayer-progress{display:block;position:absolute;height:20px;line-height:20px;left:0;right:0;outline:none;top:-10px;z-index:35}.xgplayer-skin-default .xgplayer-progress-outer{background:hsla(0,0%,100%,.3);display:block;height:3px;line-height:3px;margin-top:8.5px;width:100%;position:relative;cursor:pointer}.xgplayer-skin-default .xgplayer-progress-cache,.xgplayer-skin-default .xgplayer-progress-played{display:block;height:100%;line-height:1;position:absolute;left:0;top:0}.xgplayer-skin-default .xgplayer-progress-cache{width:0;background:hsla(0,0%,100%,.5)}.xgplayer-skin-default .xgplayer-progress-played{display:block;width:0;background-image:linear-gradient(-90deg,#fa1f41,#e31106);border-radius:0 1.5px 1.5px 0}.xgplayer-skin-default .xgplayer-progress-btn{display:none;position:absolute;left:0;top:-6px;width:14px;height:16px;border-radius:5px;background:#fff;box-shadow:0 0 2px 0 rgba(0,0,0,.26)}.xgplayer-skin-default .xgplayer-progress-point{position:absolute}.xgplayer-skin-default .xgplayer-progress-point.xgplayer-tips{margin-left:0;top:-25px;display:none;z-index:100}.xgplayer-skin-default .xgplayer-progress-dot{display:inline-block;position:absolute;height:3px;width:5px;top:0;background:#fff;border-radius:6px;z-index:16}.xgplayer-skin-default .xgplayer-progress-dot .xgplayer-progress-tip{position:absolute;left:0;top:-40px;height:auto;line-height:30px;width:auto;-webkit-transform:scale(.8);-ms-transform:scale(.8);transform:scale(.8);background:rgba(0,0,0,.3);border-radius:6px;border:1px solid rgba(0,0,0,.8);cursor:default;white-space:nowrap;display:none}.xgplayer-skin-default .xgplayer-progress-dot-show .xgplayer-progress-tip{display:block}.xgplayer-skin-default .xgplayer-progress-thumbnail{position:absolute;-moz-box-sizing:border-box;box-sizing:border-box}.xgplayer-skin-default .xgplayer-progress-thumbnail.xgplayer-tips{margin-left:0;display:none;z-index:99}.xgplayer-skin-default .xgplayer-progress:focus .xgplayer-progress-outer,.xgplayer-skin-default .xgplayer-progress:hover .xgplayer-progress-outer{height:6px;margin-top:7px}.xgplayer-skin-default .xgplayer-progress:focus .xgplayer-progress-dot,.xgplayer-skin-default .xgplayer-progress:hover .xgplayer-progress-dot{height:6px}.xgplayer-skin-default .xgplayer-progress:focus .xgplayer-progress-btn,.xgplayer-skin-default .xgplayer-progress:hover .xgplayer-progress-btn{display:block}.xgplayer-skin-default.xgplayer-definition-active .xgplayer-progress,.xgplayer-skin-default.xgplayer-playbackrate-active .xgplayer-progress,.xgplayer-skin-default.xgplayer-texttrack-active .xgplayer-progress,.xgplayer-skin-default.xgplayer-volume-active .xgplayer-progress{z-index:15}.xgplayer-skin-default .xgplayer-replay{position:absolute;left:0;top:0;width:100%;height:100%;z-index:105;display:none;-webkit-justify-content:center;-moz-box-pack:center;justify-content:center;-webkit-align-items:center;-moz-box-align:center;align-items:center;background:rgba(0,0,0,.54);-webkit-flex-direction:column;-moz-box-orient:vertical;-moz-box-direction:normal;flex-direction:column}.xgplayer-skin-default .xgplayer-replay svg{background:rgba(0,0,0,.58);border-radius:100%;cursor:pointer}.xgplayer-skin-default .xgplayer-replay svg path{-webkit-transform:translate(20px,21px);-ms-transform:translate(20px,21px);transform:translate(20px,21px);fill:#ddd}.xgplayer-skin-default .xgplayer-replay svg:hover{background:rgba(0,0,0,.38)}.xgplayer-skin-default .xgplayer-replay svg:hover path{fill:#fff}.xgplayer-skin-default .xgplayer-replay .xgplayer-replay-txt{display:inline-block;font-family:PingFangSC-Regular;font-size:14px;color:#fff;line-height:34px}.xgplayer-skin-default.xgplayer.xgplayer-ended .xgplayer-controls{display:none}.xgplayer-skin-default.xgplayer.xgplayer-ended .xgplayer-replay{display:-webkit-flex;display:-moz-box;display:flex}.xgplayer-skin-default .xgplayer-playbackrate{-webkit-order:8;-moz-box-ordinal-group:9;order:8;width:60px;height:20px;z-index:18;position:relative;display:inline-block;cursor:default}.xgplayer-skin-default .xgplayer-playbackrate ul{display:none;list-style:none;width:78px;background:rgba(0,0,0,.54);border-radius:1px;position:absolute;bottom:20px;left:0;text-align:left;white-space:nowrap;z-index:26;cursor:pointer}.xgplayer-skin-default .xgplayer-playbackrate ul li{opacity:.7;font-family:PingFangSC-Regular;font-size:11px;color:hsla(0,0%,100%,.8);position:relative;padding:4px 0;text-align:center}.xgplayer-skin-default .xgplayer-playbackrate ul li.selected,.xgplayer-skin-default .xgplayer-playbackrate ul li:hover{color:#fff;opacity:1}.xgplayer-skin-default .xgplayer-playbackrate ul li:first-child{position:relative;margin-top:12px}.xgplayer-skin-default .xgplayer-playbackrate ul li:last-child{position:relative;margin-bottom:12px}.xgplayer-skin-default .xgplayer-playbackrate .name{height:20px;position:relative;top:11px;text-align:center;background:rgba(0,0,0,.38);color:hsla(0,0%,100%,.8);border-radius:10px;line-height:20px}.xgplayer-skin-default .xgplayer-playbackrate span{position:relative;top:19px;font-weight:700;text-shadow:0 0 4px rgba(0,0,0,.6)}.xgplayer-skin-default .xgplayer-playbackrate:hover{opacity:1}.xgplayer-skin-default.xgplayer-playbackrate-active .xgplayer-playbackrate ul{display:block}.xgplayer-skin-default .xgplayer-download{position:relative;-webkit-order:9;-moz-box-ordinal-group:10;order:9;display:block;cursor:pointer}.xgplayer-skin-default .xgplayer-download .xgplayer-icon{margin-top:3px}.xgplayer-skin-default .xgplayer-download .xgplayer-icon div{position:absolute}.xgplayer-skin-default .xgplayer-download .xgplayer-icon svg{position:relative;top:5px;left:5px}.xgplayer-skin-default .xgplayer-download .xgplayer-tips{margin-left:-20px}.xgplayer-skin-default .xgplayer-download .xgplayer-tips .xgplayer-tip-download{display:block}.xgplayer-skin-default .xgplayer-download:hover{opacity:.85}.xgplayer-skin-default .xgplayer-download:hover .xgplayer-tips{display:block}.lang-is-en .xgplayer-download .xgplayer-tips{margin-left:-32px}.lang-is-jp .xgplayer-download .xgplayer-tips{margin-left:-40px}.xgplayer-skin-default .danmu-switch{-webkit-order:6;-moz-box-ordinal-group:7;order:6;z-index:26}.xgplayer-skin-default .xgplayer-danmu{display:none;position:absolute;top:0;left:0;right:0;height:100%;overflow:hidden;z-index:9;outline:none}.xgplayer-skin-default .xgplayer-danmu>*{position:absolute;white-space:nowrap;z-index:9}.xgplayer-skin-default .xgplayer-danmu.xgplayer-has-danmu{display:block}.xgplayer-skin-default .xgplayer-panel{outline:none;-webkit-order:7;-moz-box-ordinal-group:8;order:7;width:40px;height:40px;display:inline-block;position:relative;font-family:PingFangSC-Regular;font-size:13px;color:hsla(0,0%,100%,.8);z-index:36}.xgplayer-skin-default .xgplayer-panel .xgplayer-panel-icon{cursor:pointer;position:absolute;margin-left:5px;top:10px}.xgplayer-skin-default .xgplayer-panel-active{display:block!important;bottom:30px}.xgplayer-skin-default .xgplayer-panel-slider{z-index:36;display:none;position:absolute;width:230px;height:230px;background:rgba(0,0,0,.54);border-radius:1px;padding:10px 20px;outline:none;left:-115px;bottom:40px}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-hidemode{padding-bottom:10px}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-hidemode-radio li{display:inline;list-style:none;cursor:pointer}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-hidemode ul{display:-webkit-flex;display:-moz-box;display:flex;-webkit-justify-content:space-around;justify-content:space-around}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-hidemode li{margin:0 12px;font-size:11px;color:#aaa}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-hidemode-font{margin-bottom:10px}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-transparency{display:block;margin-top:10px}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-transparency .xgplayer-transparency-line{-webkit-appearance:none;-moz-appearance:none;appearance:none;cursor:pointer;outline:none;width:150px;height:4px;background:#aaa;border-radius:4px;border-style:none;margin-left:10px;margin-top:-2px}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-transparency .xgplayer-transparency-line::-moz-focus-outer{border:0!important}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-transparency .xgplayer-transparency-color::-webkit-slider-runnable-track{outline:none;width:150px;height:4px;border-radius:4px}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-transparency .xgplayer-transparency-color::-moz-range-track{outline:none;background-color:#aaa;border-color:transparent;cursor:pointer;width:150px;height:4px;border-radius:4px}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-transparency .xgplayer-transparency-color::-ms-track{outline:none;background-color:#aaa;color:transparent;border-color:transparent;width:150px;height:4px;border-radius:4px}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-transparency .xgplayer-transparency-bar::-webkit-slider-thumb{outline:none;-webkit-appearance:none;-moz-appearance:none;appearance:none;border:6px solid #f85959;height:6px;width:6px;margin-top:-4px;border-radius:6px;cursor:pointer}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-transparency .xgplayer-transparency-bar::-moz-range-thumb{outline:none;-webkit-appearance:none;-moz-appearance:none;appearance:none;border:6px solid #f85959;height:0;width:0;border-radius:6px;cursor:pointer}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-transparency .xgplayer-transparency-bar::-ms-thumb{outline:none;-webkit-appearance:none;-moz-appearance:none;appearance:none;border:6px solid #f85959;height:6px;width:6px;border-radius:6px;cursor:pointer}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-transparency .xgplayer-transparency-bar::-moz-range-progress{outline:none;-webkit-appearance:none;-moz-appearance:none;appearance:none;height:4px;border-radius:4px;background:linear-gradient(90deg,#f85959,#f85959 100%,#aaa)}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-showarea{display:block;margin-top:8px}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-showarea-name{display:inline-block;position:relative;top:-10px}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-showarea-control{display:inline-block}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-showarea-control-up{width:150px;margin-left:10px;display:-moz-box;display:-webkit-flex;display:flex;-webkit-justify-content:space-between;-moz-box-pack:justify;justify-content:space-between;color:#aaa}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-showarea-control-down{position:relative;top:-10px}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-showarea-control-down-dots{display:-webkit-flex;display:-moz-box;display:flex;width:150px;margin-left:10px;-webkit-justify-content:space-between;-moz-box-pack:justify;justify-content:space-between}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-showarea-threequarters,.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-showarea-twoquarters{margin-left:-6px}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-showarea-full{margin-right:3px}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-showarea .xgplayer-showarea-line{-webkit-appearance:none;-moz-appearance:none;appearance:none;cursor:pointer;outline:none;width:150px;height:4px;background:#aaa;border-radius:4px;border-style:none;margin-left:10px;margin-top:-2px}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-showarea .xgplayer-showarea-line::-moz-focus-outer{border:0!important}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-showarea .xgplayer-showarea-color::-webkit-slider-runnable-track{outline:none;width:150px;height:4px;border-radius:4px}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-showarea .xgplayer-showarea-color::-moz-range-track{outline:none;background-color:#aaa;border-color:transparent;cursor:pointer;width:150px;height:4px;border-radius:4px}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-showarea .xgplayer-showarea-color::-ms-track{outline:none;background-color:#aaa;color:transparent;border-color:transparent;width:150px;height:4px;border-radius:4px}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-showarea .xgplayer-showarea-bar::-webkit-slider-thumb{outline:none;-webkit-appearance:none;-moz-appearance:none;appearance:none;border:6px solid #f85959;height:6px;width:6px;margin-top:-4px;border-radius:6px;cursor:pointer}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-showarea .xgplayer-showarea-bar::-moz-range-thumb{outline:none;-webkit-appearance:none;-moz-appearance:none;appearance:none;border:6px solid #f85959;height:0;width:0;border-radius:6px;cursor:pointer}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-showarea .xgplayer-showarea-bar::-ms-thumb{outline:none;-webkit-appearance:none;-moz-appearance:none;appearance:none;border:6px solid #f85959;height:6px;width:6px;border-radius:6px;cursor:pointer}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-showarea .xgplayer-showarea-full-dot,.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-showarea .xgplayer-showarea-onequarters-dot,.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-showarea .xgplayer-showarea-threequarters-dot,.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-showarea .xgplayer-showarea-twoquarters-dot,.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-showarea .xgplayer-showarea-zero-dot{width:3px;height:3px;border:3px solid #aaa;border-radius:50%;background-color:#aaa;position:relative;top:16px;z-index:-1}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmuspeed{display:block}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmuspeed-name{display:inline-block;position:relative;top:-10px}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmuspeed-control{display:inline-block}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmuspeed-control-up{width:150px;margin-left:10px;display:-moz-box;display:-webkit-flex;display:flex;-webkit-justify-content:space-between;-moz-box-pack:justify;justify-content:space-between;color:#aaa}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmuspeed-control-down{position:relative;top:-10px}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmuspeed-control-down-dots{display:-webkit-flex;display:-moz-box;display:flex;width:150px;margin-left:10px;-webkit-justify-content:space-between;-moz-box-pack:justify;justify-content:space-between}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmuspeed .xgplayer-danmuspeed-line{-webkit-appearance:none;-moz-appearance:none;appearance:none;cursor:pointer;outline:none;width:150px;height:4px;background:#aaa;border-radius:4px;border-style:none;margin-left:10px;margin-top:-2px}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmuspeed .xgplayer-danmuspeed-line::-moz-focus-outer{border:0!important}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmuspeed .xgplayer-danmuspeed-color::-webkit-slider-runnable-track{outline:none;width:150px;height:4px;border-radius:4px}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmuspeed .xgplayer-danmuspeed-color::-moz-range-track{outline:none;background-color:#aaa;border-color:transparent;cursor:pointer;width:150px;height:4px;border-radius:4px}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmuspeed .xgplayer-danmuspeed-color::-ms-track{outline:none;background-color:#aaa;color:transparent;border-color:transparent;width:150px;height:4px;border-radius:4px}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmuspeed .xgplayer-danmuspeed-bar::-webkit-slider-thumb{outline:none;-webkit-appearance:none;-moz-appearance:none;appearance:none;border:6px solid #f85959;height:6px;width:6px;margin-top:-4px;border-radius:6px;cursor:pointer}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmuspeed .xgplayer-danmuspeed-bar::-moz-range-thumb{outline:none;-webkit-appearance:none;-moz-appearance:none;appearance:none;border:6px solid #f85959;height:0;width:0;border-radius:6px;cursor:pointer}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmuspeed .xgplayer-danmuspeed-bar::-ms-thumb{outline:none;-webkit-appearance:none;-moz-appearance:none;appearance:none;border:6px solid #f85959;height:6px;width:6px;border-radius:6px;cursor:pointer}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmuspeed .xgplayer-danmuspeed-large-dot,.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmuspeed .xgplayer-danmuspeed-middle-dot,.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmuspeed .xgplayer-danmuspeed-small-dot{width:3px;height:3px;border:3px solid #aaa;border-radius:50%;background-color:#aaa;position:relative;top:16px;z-index:-1}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmufont{display:block}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmufont-name{display:inline-block;position:relative;top:-10px}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmufont-control{display:inline-block}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmufont-control-up{width:150px;margin-left:10px;display:-moz-box;display:-webkit-flex;display:flex;-webkit-justify-content:space-between;-moz-box-pack:justify;justify-content:space-between;color:#aaa}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmufont-control-down{position:relative;top:-10px}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmufont-control-down-dots{display:-webkit-flex;display:-moz-box;display:flex;width:150px;margin-left:10px;-webkit-justify-content:space-between;-moz-box-pack:justify;justify-content:space-between}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmufont .xgplayer-danmufont-line{-webkit-appearance:none;-moz-appearance:none;appearance:none;cursor:pointer;outline:none;width:150px;height:4px;background:#aaa;border-radius:4px;border-style:none;margin-left:10px;margin-top:-2px}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmufont .xgplayer-danmufont-line::-moz-focus-outer{border:0!important}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmufont .xgplayer-danmufont-color::-webkit-slider-runnable-track{outline:none;width:150px;height:4px;border-radius:4px}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmufont .xgplayer-danmufont-color::-moz-range-track{outline:none;background-color:#aaa;border-color:transparent;cursor:pointer;width:150px;height:4px;border-radius:4px}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmufont .xgplayer-danmufont-color::-ms-track{outline:none;background-color:#aaa;color:transparent;border-color:transparent;width:150px;height:4px;border-radius:4px}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmufont .xgplayer-danmufont-bar::-webkit-slider-thumb{outline:none;-webkit-appearance:none;-moz-appearance:none;appearance:none;border:6px solid #f85959;height:6px;width:6px;margin-top:-4px;border-radius:6px;cursor:pointer}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmufont .xgplayer-danmufont-bar::-moz-range-thumb{outline:none;-webkit-appearance:none;-moz-appearance:none;appearance:none;border:6px solid #f85959;height:0;width:0;border-radius:6px;cursor:pointer}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmufont .xgplayer-danmufont-bar::-ms-thumb{outline:none;-webkit-appearance:none;-moz-appearance:none;appearance:none;border:6px solid #f85959;height:6px;width:6px;border-radius:6px;cursor:pointer}.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmufont .xgplayer-danmufont-large-dot,.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmufont .xgplayer-danmufont-middle-dot,.xgplayer-skin-default .xgplayer-panel-slider .xgplayer-danmufont .xgplayer-danmufont-small-dot{width:3px;height:3px;border:3px solid #aaa;border-radius:50%;background-color:#aaa;position:relative;top:16px;z-index:-1}.xgplayer-skin-default .xgplayer-playnext{position:relative;-webkit-order:1;-moz-box-ordinal-group:2;order:1;display:block;cursor:pointer;top:-2px}.xgplayer-skin-default .xgplayer-playnext .xgplayer-icon div{position:absolute}.xgplayer-skin-default .xgplayer-playnext .xgplayer-tips .xgplayer-tip-playnext{display:block}.xgplayer-skin-default .xgplayer-playnext:hover{opacity:.85}.xgplayer-skin-default .xgplayer-playnext:hover .xgplayer-tips{display:block}.lang-is-en .xgplayer-playnext .xgplayer-tips{margin-left:-25px}.lang-is-jp .xgplayer-playnext .xgplayer-tips{margin-left:-38px}.xgplayer-skin-default .xgplayer-pip{-webkit-order:9;-moz-box-ordinal-group:10;order:9;position:relative;outline:none;display:block;cursor:pointer;height:20px;top:8px}.xgplayer-skin-default .xgplayer-pip .name{text-align:center;font-family:PingFangSC-Regular;font-size:13px;line-height:20px;height:20px;color:hsla(0,0%,100%,.8)}.xgplayer-skin-default .xgplayer-pip .name span{width:60px;height:20px;line-height:20px;background:rgba(0,0,0,.38);border-radius:10px;display:inline-block;vertical-align:middle}.xgplayer-skin-default .xgplayer-pip-lay{position:absolute;top:26px;left:0;width:100%;height:100%;z-index:130;cursor:pointer;background-color:transparent;display:none}.xgplayer-skin-default .xgplayer-pip-lay div{width:100%;height:100%}.xgplayer-skin-default .xgplayer-pip-drag{cursor:move;position:absolute;top:0;left:0;width:100%;height:26px;line-height:26px;background-image:linear-gradient(rgba(0,0,0,.3),transparent);z-index:130;display:none}.xgplayer-skin-default.xgplayer-pip-active{position:fixed;right:0;bottom:200px;width:320px!important;height:180px!important;z-index:110!important}.xgplayer-skin-default.xgplayer-pip-active .xgplayer-controls,.xgplayer-skin-default.xgplayer-pip-active .xgplayer-danmu{display:none}.xgplayer-skin-default.xgplayer-pip-active .xgplayer-pip-lay{display:block}.xgplayer-skin-default.xgplayer-pip-active .xgplayer-pip-drag{display:-webkit-flex;display:-moz-box;display:flex}.xgplayer-skin-default.xgplayer-inactive .xgplayer-pip-drag{display:none}.lang-is-jp .xgplayer-pip .name span{width:70px;height:20px}.xgplayer-skin-default .xgplayer-rotate{position:relative;-webkit-order:10;-moz-box-ordinal-group:11;order:10;display:block;cursor:pointer}.xgplayer-skin-default .xgplayer-rotate .xgplayer-icon{margin-top:7px;width:26px}.xgplayer-skin-default .xgplayer-rotate .xgplayer-icon div{position:absolute}.xgplayer-skin-default .xgplayer-rotate .xgplayer-tips{margin-left:-22px}.xgplayer-skin-default .xgplayer-rotate .xgplayer-tips .xgplayer-tip-rotate{display:block}.xgplayer-skin-default .xgplayer-rotate:hover{opacity:.85}.xgplayer-skin-default .xgplayer-rotate:hover .xgplayer-tips{display:block}.lang-is-en .xgplayer-rotate .xgplayer-tips{margin-left:-26px}.lang-is-jp .xgplayer-rotate .xgplayer-tips{margin-left:-38px}.xgplayer-skin-default .xgplayer-screenshot{-webkit-order:11;-moz-box-ordinal-group:12;order:11;position:relative;outline:none;display:block;cursor:pointer;height:20px;top:8px}.xgplayer-skin-default .xgplayer-screenshot .name{text-align:center;font-family:PingFangSC-Regular;font-size:13px;line-height:20px;height:20px;color:hsla(0,0%,100%,.8)}.xgplayer-skin-default .xgplayer-screenshot .name span{width:60px;height:20px;line-height:20px;background:rgba(0,0,0,.38);border-radius:10px;display:inline-block;vertical-align:middle}.lang-is-en .xgplayer-screenshot .name span,.lang-is-jp .xgplayer-screenshot .name span{width:75px;height:20px}.xgplayer-skin-default .xgplayer-texttrack{-webkit-order:7;-moz-box-ordinal-group:8;order:7;width:60px;height:150px;z-index:18;position:relative;outline:none;display:none;cursor:default;margin-top:-119px}.xgplayer-skin-default .xgplayer-texttrack ul{display:none;list-style:none;width:78px;background:rgba(0,0,0,.54);border-radius:1px;position:absolute;bottom:30px;left:0;text-align:center;white-space:nowrap;margin-left:-10px;z-index:26;cursor:pointer}.xgplayer-skin-default .xgplayer-texttrack ul li{opacity:.7;font-family:PingFangSC-Regular;font-size:11px;color:hsla(0,0%,100%,.8);padding:6px 13px}.xgplayer-skin-default .xgplayer-texttrack ul li.selected,.xgplayer-skin-default .xgplayer-texttrack ul li:hover{color:#fff;opacity:1}.xgplayer-skin-default .xgplayer-texttrack .name{text-align:center;font-family:PingFangSC-Regular;font-size:13px;cursor:pointer;color:hsla(0,0%,100%,.8);position:absolute;bottom:0;width:60px;height:20px;line-height:20px;background:rgba(0,0,0,.38);border-radius:10px;display:inline-block;vertical-align:middle}.xgplayer-skin-default.xgplayer-is-texttrack .xgplayer-texttrack,.xgplayer-skin-default.xgplayer-texttrack-active .xgplayer-texttrack ul{display:block}.xgplayer-skin-default .xgplayer-icon{display:block;width:40px;height:40px;overflow:hidden;fill:#fff}.xgplayer-skin-default .xgplayer-tips{background:rgba(0,0,0,.54);border-radius:1px;font-size:11px;padding:2px 4px;top:-30px;left:50%;margin-left:-16px;width:auto;white-space:nowrap}.xgplayer-skin-default .xgplayer-error,.xgplayer-skin-default .xgplayer-tips{display:none;position:absolute;font-family:PingFangSC-Regular;color:#fff;text-align:center}.xgplayer-skin-default .xgplayer-error{background:#000;left:0;top:0;width:100%;height:100%;z-index:125;font-size:14px;line-height:100%;-webkit-justify-content:center;-moz-box-pack:center;justify-content:center;-webkit-align-items:center;-moz-box-align:center;align-items:center}.xgplayer-skin-default .xgplayer-error .xgplayer-error-refresh{color:#fa1f41;padding:0 3px;cursor:pointer}.xgplayer-skin-default .xgplayer-error .xgplayer-error-text{line-height:18px;margin:auto 6px}.xgplayer-skin-default.xgplayer-is-error .xgplayer-error{display:-webkit-flex;display:-moz-box;display:flex}', ""])
    }, function (e, t, n) {
        "use strict";
        e.exports = function (e) {
            var t = [];
            return t.toString = function () {
                return this.map(function (t) {
                    var n = function (e, t) {
                        var n = e[1] || "", r = e[3];
                        if (!r) return n;
                        if (t && "function" == typeof btoa) {
                            var a = (o = r, "/*# sourceMappingURL=data:application/json;charset=utf-8;base64," + btoa(unescape(encodeURIComponent(JSON.stringify(o)))) + " */"),
                                i = r.sources.map(function (e) {
                                    return "/*# sourceURL=" + r.sourceRoot + e + " */"
                                });
                            return [n].concat(i).concat([a]).join("\n")
                        }
                        var o;
                        return [n].join("\n")
                    }(t, e);
                    return t[2] ? "@media " + t[2] + "{" + n + "}" : n
                }).join("")
            }, t.i = function (e, n) {
                "string" == typeof e && (e = [[null, e, ""]]);
                for (var r = {}, a = 0; a < this.length; a++) {
                    var i = this[a][0];
                    "number" == typeof i && (r[i] = !0)
                }
                for (a = 0; a < e.length; a++) {
                    var o = e[a];
                    "number" == typeof o[0] && r[o[0]] || (n && !o[2] ? o[2] = n : n && (o[2] = "(" + o[2] + ") and (" + n + ")"), t.push(o))
                }
            }, t
        }
    }, function (e, t, n) {
        var r, a, i = {}, o = (r = function () {
            return window && document && document.all && !window.atob
        }, function () {
            return void 0 === a && (a = r.apply(this, arguments)), a
        }), l = function (e) {
            var t = {};
            return function (e) {
                if ("function" == typeof e) return e();
                if (void 0 === t[e]) {
                    var n = function (e) {
                        return document.querySelector(e)
                    }.call(this, e);
                    if (window.HTMLIFrameElement && n instanceof window.HTMLIFrameElement) try {
                        n = n.contentDocument.head
                    } catch (e) {
                        n = null
                    }
                    t[e] = n
                }
                return t[e]
            }
        }(), s = null, u = 0, c = [], p = n(57);

        function d(e, t) {
            for (var n = 0; n < e.length; n++) {
                var r = e[n], a = i[r.id];
                if (a) {
                    a.refs++;
                    for (var o = 0; o < a.parts.length; o++) a.parts[o](r.parts[o]);
                    for (; o < r.parts.length; o++) a.parts.push(m(r.parts[o], t))
                } else {
                    var l = [];
                    for (o = 0; o < r.parts.length; o++) l.push(m(r.parts[o], t));
                    i[r.id] = {id: r.id, refs: 1, parts: l}
                }
            }
        }

        function f(e, t) {
            for (var n = [], r = {}, a = 0; a < e.length; a++) {
                var i = e[a], o = t.base ? i[0] + t.base : i[0], l = {css: i[1], media: i[2], sourceMap: i[3]};
                r[o] ? r[o].parts.push(l) : n.push(r[o] = {id: o, parts: [l]})
            }
            return n
        }

        function g(e, t) {
            var n = l(e.insertInto);
            if (!n) throw new Error("Couldn't find a style target. This probably means that the value for the 'insertInto' parameter is invalid.");
            var r = c[c.length - 1];
            if ("top" === e.insertAt) r ? r.nextSibling ? n.insertBefore(t, r.nextSibling) : n.appendChild(t) : n.insertBefore(t, n.firstChild), c.push(t); else if ("bottom" === e.insertAt) n.appendChild(t); else {
                if ("object" != typeof e.insertAt || !e.insertAt.before) throw new Error("[Style Loader]\n\n Invalid value for parameter 'insertAt' ('options.insertAt') found.\n Must be 'top', 'bottom', or Object.\n (https://github.com/webpack-contrib/style-loader#insertat)\n");
                var a = l(e.insertInto + " " + e.insertAt.before);
                n.insertBefore(t, a)
            }
        }

        function h(e) {
            if (null === e.parentNode) return !1;
            e.parentNode.removeChild(e);
            var t = c.indexOf(e);
            t >= 0 && c.splice(t, 1)
        }

        function y(e) {
            var t = document.createElement("style");
            return e.attrs.type = "text/css", x(t, e.attrs), g(e, t), t
        }

        function x(e, t) {
            Object.keys(t).forEach(function (n) {
                e.setAttribute(n, t[n])
            })
        }

        function m(e, t) {
            var n, r, a, i;
            if (t.transform && e.css) {
                if (!(i = t.transform(e.css))) return function () {
                };
                e.css = i
            }
            if (t.singleton) {
                var o = u++;
                n = s || (s = y(t)), r = k.bind(null, n, o, !1), a = k.bind(null, n, o, !0)
            } else e.sourceMap && "function" == typeof URL && "function" == typeof URL.createObjectURL && "function" == typeof URL.revokeObjectURL && "function" == typeof Blob && "function" == typeof btoa ? (n = function (e) {
                var t = document.createElement("link");
                return e.attrs.type = "text/css", e.attrs.rel = "stylesheet", x(t, e.attrs), g(e, t), t
            }(t), r = function (e, t, n) {
                var r = n.css, a = n.sourceMap, i = void 0 === t.convertToAbsoluteUrls && a;
                (t.convertToAbsoluteUrls || i) && (r = p(r));
                a && (r += "\n/*# sourceMappingURL=data:application/json;base64," + btoa(unescape(encodeURIComponent(JSON.stringify(a)))) + " */");
                var o = new Blob([r], {type: "text/css"}), l = e.href;
                e.href = URL.createObjectURL(o), l && URL.revokeObjectURL(l)
            }.bind(null, n, t), a = function () {
                h(n), n.href && URL.revokeObjectURL(n.href)
            }) : (n = y(t), r = function (e, t) {
                var n = t.css, r = t.media;
                r && e.setAttribute("media", r);
                if (e.styleSheet) e.styleSheet.cssText = n; else {
                    for (; e.firstChild;) e.removeChild(e.firstChild);
                    e.appendChild(document.createTextNode(n))
                }
            }.bind(null, n), a = function () {
                h(n)
            });
            return r(e), function (t) {
                if (t) {
                    if (t.css === e.css && t.media === e.media && t.sourceMap === e.sourceMap) return;
                    r(e = t)
                } else a()
            }
        }

        e.exports = function (e, t) {
            if ("undefined" != typeof DEBUG && DEBUG && "object" != typeof document) throw new Error("The style-loader cannot be used in a non-browser environment");
            (t = t || {}).attrs = "object" == typeof t.attrs ? t.attrs : {}, t.singleton || "boolean" == typeof t.singleton || (t.singleton = o()), t.insertInto || (t.insertInto = "head"), t.insertAt || (t.insertAt = "bottom");
            var n = f(e, t);
            return d(n, t), function (e) {
                for (var r = [], a = 0; a < n.length; a++) {
                    var o = n[a];
                    (l = i[o.id]).refs--, r.push(l)
                }
                e && d(f(e, t), t);
                for (a = 0; a < r.length; a++) {
                    var l;
                    if (0 === (l = r[a]).refs) {
                        for (var s = 0; s < l.parts.length; s++) l.parts[s]();
                        delete i[l.id]
                    }
                }
            }
        };
        var v, b = (v = [], function (e, t) {
            return v[e] = t, v.filter(Boolean).join("\n")
        });

        function k(e, t, n, r) {
            var a = n ? "" : r.css;
            if (e.styleSheet) e.styleSheet.cssText = b(t, a); else {
                var i = document.createTextNode(a), o = e.childNodes;
                o[t] && e.removeChild(o[t]), o.length ? e.insertBefore(i, o[t]) : e.appendChild(i)
            }
        }
    }, function (e, t, n) {
        "use strict";
        e.exports = function (e) {
            var t = "undefined" != typeof window && window.location;
            if (!t) throw new Error("fixUrls requires window.location");
            if (!e || "string" != typeof e) return e;
            var n = t.protocol + "//" + t.host, r = n + t.pathname.replace(/\/[^\/]*$/, "/");
            return e.replace(/url\s*\(((?:[^)(]|\((?:[^)(]+|\([^)(]*\))*\))*)\)/gi, function (e, t) {
                var a, i = t.trim().replace(/^"(.*)"$/, function (e, t) {
                    return t
                }).replace(/^'(.*)'$/, function (e, t) {
                    return t
                });
                return /^(#|data:|http:\/\/|https:\/\/|file:\/\/\/|\s*$)/i.test(i) ? e : (a = 0 === i.indexOf("//") ? i : 0 === i.indexOf("/") ? n + i : r + i.replace(/^\.\//, ""), "url(" + JSON.stringify(a) + ")")
            })
        }
    }, function (e, t, n) {
        "use strict";
        var r, a = n(0), i = (r = a) && r.__esModule ? r : {default: r};
        i.default.install("s_enter", function () {
            for (var e = this.root, t = i.default.util, n = "", r = 1; r <= 12; r++) n += '<div class="xgplayer-enter-bar' + r + '"></div>';
            var a = t.createDom("xg-enter", '<div class="xgplayer-enter-spinner">\n                                                  ' + n + "\n                                                </div>", {}, "xgplayer-enter");
            e.appendChild(a)
        })
    }, function (e, t, n) {
        "use strict";
        var r = o(n(0)), a = o(n(60)), i = o(n(61));

        function o(e) {
            return e && e.__esModule ? e : {default: e}
        }

        r.default.install("s_play", function () {
            var e = this, t = r.default.util, n = e.config.playBtn ? e.config.playBtn : {}, o = void 0;
            o = "img" === n.type ? t.createImgBtn("play", n.url.play, n.width, n.height) : t.createDom("xg-play", '<xg-icon class="xgplayer-icon">\n                                      <div class="xgplayer-icon-play">' + a.default + '</div>\n                                      <div class="xgplayer-icon-pause">' + i.default + "</div>\n                                     </xg-icon>", {}, "xgplayer-play");
            var l = {};
            l.play = e.lang.PLAY_TIPS, l.pause = e.lang.PAUSE_TIPS;
            var s = t.createDom("xg-tips", '<span class="xgplayer-tip-play">' + l.play + '</span>\n                                        <span class="xgplayer-tip-pause">' + l.pause + "</span>", {}, "xgplayer-tips");
            o.appendChild(s), e.once("ready", function () {
                e.controls.appendChild(o)
            }), ["click", "touchend"].forEach(function (t) {
                o.addEventListener(t, function (t) {
                    t.preventDefault(), t.stopPropagation(), e.emit("playBtnClick")
                })
            })
        })
    }, function (e, t, n) {
        "use strict";
        n.r(t), t.default = '<svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 40 40">\n  <path transform="scale(0.0320625 0.0320625)" d="M576,363L810,512L576,661zM342,214L576,363L576,661L342,810z"></path>\n</svg>\n'
    }, function (e, t, n) {
        "use strict";
        n.r(t), t.default = '<svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 40 40">\n  <path transform="scale(0.0320625 0.0320625)" d="M598,214h170v596h-170v-596zM256 810v-596h170v596h-170z"></path>\n</svg>\n'
    }, function (e, t, n) {
        "use strict";
        var r = o(n(0)), a = o(n(63)), i = o(n(64));

        function o(e) {
            return e && e.__esModule ? e : {default: e}
        }

        r.default.install("s_start", function () {
            var e = this, t = e.root, n = r.default.util,
                o = n.createDom("xg-start", '<div class="xgplayer-icon-play">' + a.default + '</div>\n                                      <div class="xgplayer-icon-pause">' + i.default + "</div>", {}, "xgplayer-start");
            e.once("ready", function () {
                t.appendChild(o)
            }), o.onclick = function (t) {
                t.preventDefault(), t.stopPropagation(), e.emit("startBtnClick")
            }, e.once("ready", function () {
                n.addClass(e.root, "xgplayer-skin-default"), e.config.lang && "en" === e.config.lang ? n.addClass(e.root, "lang-is-en") : "jp" === e.config.lang && n.addClass(e.root, "lang-is-jp")
            })
        })
    }, function (e, t, n) {
        "use strict";
        n.r(t), t.default = '<svg xmlns="http://www.w3.org/2000/svg" width="70" height="70" viewBox="0 0 70 70">\n  <path transform="scale(0.04,0.04)" d="M576,363L810,512L576,661zM342,214L576,363L576,661L342,810z"></path>\n</svg>\n'
    }, function (e, t, n) {
        "use strict";
        n.r(t), t.default = '<svg xmlns="http://www.w3.org/2000/svg" width="70" height="70" viewBox="0 0 70 70">\n  <path transform="scale(0.04 0.04)" d="M598,214h170v596h-170v-596zM256 810v-596h170v596h-170z"></path>\n</svg>\n'
    }, function (e, t, n) {
        "use strict";
        var r, a = n(0), i = (r = a) && r.__esModule ? r : {default: r};
        i.default.install("s_poster", function () {
            var e = this.root, t = i.default.util;
            if (this.config.poster) {
                var n = t.createDom("xg-poster", "", {}, "xgplayer-poster");
                n.style.backgroundImage = "url(" + this.config.poster + ")", e.appendChild(n)
            }
        })
    }, function (e, t, n) {
        "use strict";
        var r, a = n(0), i = (r = a) && r.__esModule ? r : {default: r};
        i.default.install("s_flex", function () {
            this.root;
            var e = i.default.util.createDom("xg-placeholder", "", {}, "xgplayer-placeholder");
            this.controls.appendChild(e)
        })
    }, function (e, t, n) {
        "use strict";
        var r = o(n(0)), a = o(n(68)), i = o(n(69));

        function o(e) {
            return e && e.__esModule ? e : {default: e}
        }

        r.default.install("s_fullscreen", function () {
            var e = this, t = r.default.util, n = e.config.fullscreenBtn ? e.config.fullscreenBtn : {}, o = void 0;
            o = "img" === n.type ? t.createImgBtn("fullscreen", n.url.request, n.width, n.height) : t.createDom("xg-fullscreen", '<xg-icon class="xgplayer-icon">\n                                             <div class="xgplayer-icon-requestfull">' + a.default + '</div>\n                                             <div class="xgplayer-icon-exitfull">' + i.default + "</div>\n                                           </xg-icon>", {}, "xgplayer-fullscreen");
            var l = {};
            l.requestfull = e.lang.FULLSCREEN_TIPS, l.exitfull = e.lang.EXITFULLSCREEN_TIPS;
            var s = t.createDom("xg-tips", '<span class="xgplayer-tip-requestfull">' + l.requestfull + '</span>\n                                        <span class="xgplayer-tip-exitfull">' + l.exitfull + "</span>", {}, "xgplayer-tips");
            o.appendChild(s), e.once("ready", function () {
                e.controls.appendChild(o)
            }), ["click", "touchend"].forEach(function (t) {
                o.addEventListener(t, function (t) {
                    t.preventDefault(), t.stopPropagation(), e.emit("fullscreenBtnClick")
                })
            })
        })
    }, function (e, t, n) {
        "use strict";
        n.r(t), t.default = '<svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 40 40">\n  <path transform="scale(0.0320625 0.0320625)" d="M598 214h212v212h-84v-128h-128v-84zM726 726v-128h84v212h-212v-84h128zM214 426v-212h212v84h-128v128h-84zM298 598v128h128v84h-212v-212h84z"></path>\n</svg>\n'
    }, function (e, t, n) {
        "use strict";
        n.r(t), t.default = '<svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 40 40">\n  <path transform="scale(0.0320625 0.0320625)" d="M682 342h128v84h-212v-212h84v128zM598 810v-212h212v84h-128v128h-84zM342 342v-128h84v212h-212v-84h128zM214 682v-84h212v212h-84v-128h-128z"></path>\n</svg>\n'
    }, function (e, t, n) {
        "use strict";
        var r = o(n(0)), a = o(n(71)), i = o(n(72));

        function o(e) {
            return e && e.__esModule ? e : {default: e}
        }

        r.default.install("s_cssFullscreen", function () {
            var e = this, t = r.default.util;
            if (e.config.cssFullscreen) {
                var n = t.createDom("xg-cssfullscreen", '<xg-icon class="xgplayer-icon">\n                                             <div class="xgplayer-icon-requestfull">' + a.default + '</div>\n                                             <div class="xgplayer-icon-exitfull">' + i.default + "</div>\n                                           </xg-icon>", {}, "xgplayer-cssfullscreen"),
                    o = {};
                o.requestfull = e.lang.CSSFULLSCREEN_TIPS, o.exitfull = e.lang.EXITCSSFULLSCREEN_TIPS;
                var l = t.createDom("xg-tips", '<span class="xgplayer-tip-requestfull">' + o.requestfull + '</span>\n                                        <span class="xgplayer-tip-exitfull">' + o.exitfull + "</span>", {}, "xgplayer-tips");
                n.appendChild(l), e.once("ready", function () {
                    e.controls.appendChild(n)
                }), ["click", "touchend"].forEach(function (t) {
                    n.addEventListener(t, function (t) {
                        t.preventDefault(), t.stopPropagation(), e.emit("cssFullscreenBtnClick")
                    })
                })
            }
        })
    }, function (e, t, n) {
        "use strict";
        n.r(t), t.default = '<svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 40 40">\n  <path transform="scale(0.028 0.028)" d="M843.617212 67.898413 175.411567 67.898413c-61.502749 0-111.367437 49.856501-111.367437 111.367437l0 668.205645c0 61.510936 49.864688 111.367437 111.367437 111.367437L843.617212 958.838931c61.510936 0 111.367437-49.856501 111.367437-111.367437L954.984648 179.26585C954.984648 117.754914 905.12917 67.898413 843.617212 67.898413zM398.146441 736.104057c15.380292 0 27.842115 12.461823 27.842115 27.842115 0 15.379269-12.461823 27.841092-27.842115 27.841092L259.725858 791.787264c-7.785314 0-14.781658-3.217275-19.838837-8.365528-5.383614-4.577249-8.791224-11.228739-8.791224-19.475564L231.095797 624.736621c0-15.371082 12.471033-27.842115 27.842115-27.842115 15.380292 0 27.842115 12.471033 27.842115 27.842115l-0.61603 71.426773 133.036969-133.037992 39.378869 39.378869L324.962651 736.113267 398.146441 736.104057zM419.199942 463.611943 286.162974 330.565764l0.61603 71.435982c0 15.380292-12.461823 27.842115-27.842115 27.842115-15.371082 0-27.842115-12.461823-27.842115-27.842115L231.094774 262.791172c0-8.256034 3.40761-14.908548 8.791224-19.476587 5.057179-5.148253 12.053524-8.374738 19.838837-8.374738l138.420583 0.00921c15.380292 0 27.842115 12.461823 27.842115 27.842115s-12.461823 27.842115-27.842115 27.842115l-73.175603-0.00921 133.607974 133.607974L419.199942 463.611943zM787.932981 763.946172c0 8.247848-3.40761 14.899338-8.791224 19.475564-5.057179 5.148253-12.053524 8.365528-19.839861 8.365528L620.881314 791.787264c-15.379269 0-27.841092-12.461823-27.841092-27.841092 0-15.380292 12.461823-27.842115 27.841092-27.842115l73.185836 0.00921L560.449967 602.50427l39.378869-39.378869L732.875015 696.163393l-0.62524-71.426773c0-15.371082 12.462846-27.842115 27.842115-27.842115 15.380292 0 27.842115 12.471033 27.842115 27.842115L787.934005 763.946172zM787.932981 402.000724c0 15.380292-12.461823 27.842115-27.842115 27.842115-15.379269 0-27.842115-12.461823-27.842115-27.842115l0.62524-71.435982L599.828836 463.611943l-39.378869-39.378869 133.617184-133.607974-73.185836 0.00921c-15.379269 0-27.841092-12.461823-27.841092-27.842115s12.461823-27.842115 27.841092-27.842115l138.421606-0.00921c7.785314 0 14.781658 3.226484 19.839861 8.374738 5.383614 4.568039 8.791224 11.219529 8.791224 19.476587L787.934005 402.000724z"></path>\n</svg>\n'
    }, function (e, t, n) {
        "use strict";
        n.r(t), t.default = '<svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 40 40">\n  <path transform="scale(0.028 0.028)" d="M834.56 81.92H189.44c-59.392 0-107.52 48.128-107.52 107.52v645.12c0 59.392 48.128 107.52 107.52 107.52h645.12c59.392 0 107.52-48.128 107.52-107.52V189.44c0-59.392-48.128-107.52-107.52-107.52zM458.24 727.04c0 14.848-12.288 26.624-26.624 26.624S404.48 741.888 404.48 727.04v-69.632L289.28 773.12c-10.752 10.24-27.648 10.24-37.888 0-10.24-10.752-10.24-27.648 0-37.888L366.592 619.52H296.96c-14.848 0-26.624-12.288-26.624-26.624s12.288-26.624 26.624-26.624h134.144c14.848 0 26.624 12.288 26.624 26.624V727.04z m0-295.936c0 14.848-12.288 26.624-26.624 26.624H296.96c-14.848 0-26.624-12.288-26.624-26.624S282.112 404.48 296.96 404.48h69.632L251.392 289.28c-10.24-10.752-10.24-27.648 0-37.888 5.12-5.12 12.288-7.68 18.944-7.68 6.656 0 13.824 2.56 18.944 7.68L404.48 366.592V296.96c0-14.848 12.288-26.624 26.624-26.624s26.624 12.288 26.624 26.624v134.144zM773.12 773.12c-10.752 10.24-27.648 10.24-37.888 0L619.52 657.408V727.04c0 14.848-12.288 26.624-26.624 26.624s-26.624-11.776-26.624-26.624v-134.144c0-14.848 12.288-26.624 26.624-26.624H727.04c14.848 0 26.624 12.288 26.624 26.624s-12.288 26.624-26.624 26.624h-69.632l115.2 115.2c10.752 10.752 10.752 27.648 0.512 38.4z m0-483.84L657.408 404.48H727.04c14.848 0 26.624 12.288 26.624 26.624 0 14.848-12.288 26.624-26.624 26.624h-134.144c-14.848 0-26.624-12.288-26.624-26.624V296.96c0-14.848 12.288-26.624 26.624-26.624s26.624 12.288 26.624 26.624v69.632L734.72 250.88c5.12-5.12 12.288-7.68 18.944-7.68s13.824 2.56 18.944 7.68c10.752 10.752 10.752 27.648 0.512 38.4z"></path>\n</svg>\n'
    }, function (e, t, n) {
        "use strict";
        var r = l(n(0)), a = l(n(74)), i = l(n(75)), o = l(n(76));

        function l(e) {
            return e && e.__esModule ? e : {default: e}
        }

        r.default.install("s_volume", function () {
            var e = this,
                t = r.default.util.createDom("xg-volume", '<xg-icon class="xgplayer-icon">\n                                         <div class="xgplayer-icon-large">' + o.default + '</div>\n                                         <div class="xgplayer-icon-small">' + i.default + '</div>\n                                         <div class="xgplayer-icon-muted">' + a.default + '</div>\n                                       </xg-icon>\n                                       <xg-slider class="xgplayer-slider" tabindex="2">\n                                         <xg-bar class="xgplayer-bar">\n                                           <xg-drag class="xgplayer-drag"></xg-drag>\n                                         </xg-bar>\n                                       </xg-slider>', {}, "xgplayer-volume");
            e.once("ready", function () {
                e.controls.appendChild(t)
            });
            var n = t.querySelector(".xgplayer-slider"), l = t.querySelector(".xgplayer-bar"),
                s = t.querySelector(".xgplayer-drag"), u = t.querySelector(".xgplayer-icon");
            s.style.height = 100 * e.config.volume + "%", n.volume = e.config.volume, ["mousedown", "touchstart"].forEach(function (t) {
                l.addEventListener(t, function (t) {
                    t.preventDefault(), t.stopPropagation(), e.emit("volumeBarClick", t)
                })
            }), ["click", "touchend"].forEach(function (t) {
                u.addEventListener(t, function (t) {
                    t.preventDefault(), t.stopPropagation(), e.emit("volumeIconClick")
                })
            }), u.addEventListener("mouseenter", function (t) {
                t.preventDefault(), t.stopPropagation(), e.emit("volumeIconEnter")
            }), ["blur", "mouseleave"].forEach(function (n) {
                t.addEventListener(n, function (t) {
                    t.preventDefault(), t.stopPropagation(), e.emit("volumeIconLeave")
                })
            })
        })
    }, function (e, t, n) {
        "use strict";
        n.r(t), t.default = '<svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 40 40">\n  <path transform="scale(0.0220625 0.0220625)" d="M358.4 358.4h-204.8v307.2h204.8l256 256v-819.2l-256 256z"></path>\n  <path transform="scale(0.0220625 0.0220625)" d="M920.4 439.808l-108.544-109.056-72.704 72.704 109.568 108.544-109.056 108.544 72.704 72.704 108.032-109.568 108.544 109.056 72.704-72.704-109.568-108.032 109.056-108.544-72.704-72.704-108.032 109.568z"></path>\n</svg>\n'
    }, function (e, t, n) {
        "use strict";
        n.r(t), t.default = '<svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 40 40">\n  <path transform="scale(0.0220625 0.0220625)" d="M358.4 358.4h-204.8v307.2h204.8l256 256v-819.2l-256 256z"></path>\n  <path transform="scale(0.0220625 0.0220625)" d="M795.648 693.248l-72.704-72.704c27.756-27.789 44.921-66.162 44.921-108.544s-17.165-80.755-44.922-108.546l0.002 0.002 72.704-72.704c46.713 46.235 75.639 110.363 75.639 181.248s-28.926 135.013-75.617 181.227l-0.021 0.021zM795.648 693.248l-72.704-72.704c27.756-27.789 44.921-66.162 44.921-108.544s-17.165-80.755-44.922-108.546l0.002 0.002 72.704-72.704c46.713 46.235 75.639 110.363 75.639 181.248s-28.926 135.013-75.617 181.227l-0.021 0.021z"></path>\n</svg>\n'
    }, function (e, t, n) {
        "use strict";
        n.r(t), t.default = '<svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 40 40">\n  <path transform="scale(0.0220625 0.0220625)" d="M358.4 358.4h-204.8v307.2h204.8l256 256v-819.2l-256 256z"></path>\n  <path transform="scale(0.0220625 0.0220625)" d="M940.632 837.632l-72.192-72.192c65.114-64.745 105.412-154.386 105.412-253.44s-40.299-188.695-105.396-253.424l-0.016-0.016 72.192-72.192c83.639 83.197 135.401 198.37 135.401 325.632s-51.762 242.434-135.381 325.612l-0.020 0.020zM795.648 693.248l-72.704-72.704c27.756-27.789 44.921-66.162 44.921-108.544s-17.165-80.755-44.922-108.546l0.002 0.002 72.704-72.704c46.713 46.235 75.639 110.363 75.639 181.248s-28.926 135.013-75.617 181.227l-0.021 0.021z"></path>\n</svg>\n'
    }, function (e, t, n) {
        "use strict";
        var r, a = n(0), i = (r = a) && r.__esModule ? r : {default: r};
        i.default.install("s_definition", function () {
            var e = this, t = e.root, n = i.default.util, r = i.default.sniffer, a = void 0,
                o = n.createDom("xg-definition", "", {tabindex: 3}, "xgplayer-definition");

            function l() {
                var r = e.definitionList, a = ["<ul>"], i = e.config.url, l = document.createElement("a");
                e.switchURL ? ["mp4", "hls", "__flv__", "dash"].every(function (t) {
                    return !e[t] || (e[t].url && (l.href = e[t].url), "__flv__" === t && (e[t]._options ? l.href = e[t]._options.url : l.href = e[t]._mediaDataSource.url), i = l.href, !1)
                }) : i = e.currentSrc || e.src, e.hls && (l.href = e.hls.url, i = l.href), r.forEach(function (t) {
                    l.href = t.url, e.dash ? a.push("<li url='" + t.url + "' cname='" + t.name + "' class='" + (t.selected ? "selected" : "") + "'>" + t.name + "</li>") : a.push("<li url='" + t.url + "' cname='" + t.name + "' class='" + (l.href === i ? "selected" : "") + "'>" + t.name + "</li>")
                });
                var s = r.filter(function (t) {
                    return l.href = t.url, e.dash ? !0 === t.selected : l.href === i
                });
                a.push("</ul><p class='name'>" + (s[0] || {name: ""}).name + "</p>");
                var u = t.querySelector(".xgplayer-definition");
                if (u) {
                    u.innerHTML = a.join("");
                    var c = u.querySelector(".name");
                    e.config.definitionActive && "hover" !== e.config.definitionActive || c.addEventListener("mouseenter", function (t) {
                        t.preventDefault(), t.stopPropagation(), n.addClass(e.root, "xgplayer-definition-active"), u.focus()
                    })
                } else {
                    o.innerHTML = a.join("");
                    var p = o.querySelector(".name");
                    e.config.definitionActive && "hover" !== e.config.definitionActive || p.addEventListener("mouseenter", function (t) {
                        t.preventDefault(), t.stopPropagation(), n.addClass(e.root, "xgplayer-definition-active"), o.focus()
                    }), e.controls.appendChild(o)
                }
            }

            function s(r) {
                e.definitionList = r, r && r instanceof Array && r.length > 1 && (n.addClass(t, "xgplayer-is-definition"), e.on("canplay", l))
            }

            function u() {
                e.currentTime = e.curTime, a || e.play()
            }

            "mobile" === r.device && (e.config.definitionActive = "click"), e.on("resourceReady", s), ["touchend", "click"].forEach(function (t) {
                o.addEventListener(t, function (t) {
                    t.preventDefault(), t.stopPropagation();
                    var i = e.definitionList, l = t.target || t.srcElement, s = document.createElement("a");
                    if (l && "li" === l.tagName.toLocaleLowerCase()) {
                        e.emit("beforeDefinitionChange", s.href);
                        var c, p = void 0;
                        if (Array.prototype.forEach.call(l.parentNode.childNodes, function (e) {
                            n.hasClass(e, "selected") && (p = e.getAttribute("cname"), n.removeClass(e, "selected"))
                        }), e.dash && i.forEach(function (e) {
                            e.selected = !1, e.name === l.innerHTML && (e.selected = !0)
                        }), n.addClass(l, "selected"), c = l.getAttribute("cname"), l.parentNode.nextSibling.innerHTML = "" + l.getAttribute("cname"), s.href = l.getAttribute("url"), e.switchURL) {
                            var d = document.createElement("a");
                            ["mp4", "hls", "__flv__", "dash"].every(function (t) {
                                return !e[t] || (e[t].url && (d.href = e[t].url), "__flv__" === t && (e[t]._options ? d.href = e[t]._options.url : d.href = e[t]._mediaDataSource.url), !1)
                            }), d.href === s.href || e.ended || e.switchURL(s.href)
                        } else e.hls && (document.createElement("a"), e.hls.url), s.href !== e.currentSrc && (e.curTime = e.currentTime, a = e.paused, e.ended || (e.src = s.href, e.once("canplay", u)));
                        e.emit("definitionChange", {
                            from: p,
                            to: c
                        }), "mobile" === r.device && n.removeClass(e.root, "xgplayer-definition-active")
                    } else "click" !== e.config.definitionActive || !l || "p" !== l.tagName.toLocaleLowerCase() && "em" !== l.tagName.toLocaleLowerCase() || (n.addClass(e.root, "xgplayer-definition-active"), o.focus());
                    e.emit("focus")
                }, !1)
            }), o.addEventListener("mouseleave", function (e) {
                e.preventDefault(), e.stopPropagation(), n.removeClass(t, "xgplayer-definition-active")
            }), e.once("destroy", function t() {
                e.off("resourceReady", s), e.off("canplay", l), e.off("canplay", u), e.off("destroy", t)
            })
        })
    }, function (e, t, n) {
        "use strict";
        var r = i(n(0)), a = i(n(79));

        function i(e) {
            return e && e.__esModule ? e : {default: e}
        }

        r.default.install("s_loading", function () {
            var e = this.root, t = r.default.util.createDom("xg-loading", "" + a.default, {}, "xgplayer-loading");
            this.once("ready", function () {
                e.appendChild(t)
            })
        })
    }, function (e, t, n) {
        "use strict";
        n.r(t), t.default = '<svg xmlns="http://www.w3.org/2000/svg" width="100" height="100" viewbox="0 0 100 100">\n  <path d="M100,50A50,50,0,1,1,50,0"></path>\n</svg>\n'
    }, function (e, t, n) {
        "use strict";
        var r, a = n(0), i = (r = a) && r.__esModule ? r : {default: r};
        i.default.install("s_progress", function () {
            var e = this, t = i.default.util,
                n = t.createDom("xg-progress", '<xg-outer class="xgplayer-progress-outer">\n                                                   <xg-cache class="xgplayer-progress-cache"></xg-cache>\n                                                   <xg-played class="xgplayer-progress-played"></xgplayer-played>\n                                                   <xg-progress-btn class="xgplayer-progress-btn"></xg-progress-btn>\n                                                   <xg-point class="xgplayer-progress-point xgplayer-tips"></xg-point>\n                                                   <xg-thumbnail class="xgplayer-progress-thumbnail xgplayer-tips"></xg-thumbnail>\n                                                 </xg-outer>', {tabindex: 1}, "xgplayer-progress"),
                r = void 0;
            e.controls.appendChild(n);
            var a = n.querySelector(".xgplayer-progress-played"), o = n.querySelector(".xgplayer-progress-btn"),
                l = n.querySelector(".xgplayer-progress-outer"), s = n.querySelector(".xgplayer-progress-cache"),
                u = n.querySelector(".xgplayer-progress-point"), c = n.querySelector(".xgplayer-progress-thumbnail");

            function p(r, a) {
                r.addEventListener("mouseenter", function (e) {
                    a && (t.addClass(r, "xgplayer-progress-dot-show"), t.addClass(n, "xgplayer-progress-dot-active"))
                }), r.addEventListener("mouseleave", function (e) {
                    a && (t.removeClass(r, "xgplayer-progress-dot-show"), t.removeClass(n, "xgplayer-progress-dot-active"))
                }), r.addEventListener("touchend", function (i) {
                    i.preventDefault(), i.stopPropagation(), a && (t.hasClass(r, "xgplayer-progress-dot-show") || Object.keys(e.dotArr).forEach(function (n) {
                        e.dotArr[n] && t.removeClass(e.dotArr[n], "xgplayer-progress-dot-show")
                    }), t.toggleClass(r, "xgplayer-progress-dot-show"), t.toggleClass(n, "xgplayer-progress-dot-active"))
                })
            }

            function d() {
                e.config.progressDot && "Array" === t.typeOf(e.config.progressDot) && e.config.progressDot.forEach(function (n) {
                    if (n.time >= 0 && n.time <= e.duration) {
                        var r = t.createDom("xg-progress-dot", n.text ? '<span class="xgplayer-progress-tip">' + n.text + "</span>" : "", {}, "xgplayer-progress-dot");
                        r.style.left = n.time / e.duration * 100 + "%", l.appendChild(r), e.dotArr[n.time] = r, p(r, n.text)
                    }
                })
            }

            e.dotArr = {}, e.once("canplay", d), e.addProgressDot = function (n, r) {
                if (!e.dotArr[n] && n >= 0 && n <= e.duration) {
                    var a = t.createDom("xg-progress-dot", "", {}, "xgplayer-progress-dot");
                    a.style.left = n / e.duration * 100 + "%", l.appendChild(a), e.dotArr[n] = a, p(a, r)
                }
            }, e.removeProgressDot = function (t) {
                if (t >= 0 && t <= e.duration && e.dotArr[t]) {
                    var n = e.dotArr[t];
                    n.parentNode.removeChild(n), n = null, e.dotArr[t] = null
                }
            }, e.removeAllProgressDot = function () {
                Object.keys(e.dotArr).forEach(function (t) {
                    if (e.dotArr[t]) {
                        var n = e.dotArr[t];
                        n.parentNode.removeChild(n), n = null, e.dotArr[t] = null
                    }
                })
            };
            var f = 0, g = 0, h = 0, y = 0, x = 0, m = 0, v = [];
            e.config.thumbnail && (f = e.config.thumbnail.pic_num, g = e.config.thumbnail.width, h = e.config.thumbnail.height, y = e.config.thumbnail.col, x = e.config.thumbnail.row, v = e.config.thumbnail.urls, c.style.width = g + "px", c.style.height = h + "px"), ["touchstart", "mousedown"].forEach(function (i) {
                n.addEventListener(i, function (i) {
                    if (i.preventDefault(), i.stopPropagation(), t.event(i), i._target === u || !e.config.allowSeekAfterEnded && e.ended) return !1;
                    n.focus(), r = n.getBoundingClientRect().width;
                    var l = a.getBoundingClientRect().left, s = function (n) {
                        n.preventDefault(), n.stopPropagation(), t.event(n), e.isProgressMoving = !0;
                        var i = n.clientX - l > r ? r : n.clientX - l, s = i / r * e.duration;
                        if (a.style.width = 100 * i / r + "%", i - 7 < 0 ? (o.style.left = "0px", o.style.transform = "") : i + 7 > r ? (o.style.left = r - 14 + "px", o.style.transform = "") : (o.style.left = "100%", o.style.transform = "translate(-50%, 0)"), "video" !== e.videoConfig.mediaType || e.dash || e.config.closeMoveSeek) {
                            var u = t.findDom(e.controls, ".xgplayer-time");
                            u && (u.innerHTML = "<span>" + t.format(s || 0) + "</span><em>" + t.format(e.duration))
                        } else e.currentTime = Number(s).toFixed(1);
                        e.emit("focus")
                    }, c = function i(u) {
                        if (u.preventDefault(), u.stopPropagation(), t.event(u), window.removeEventListener("mousemove", s), window.removeEventListener("touchmove", s, {passive: !1}), window.removeEventListener("mouseup", i), window.removeEventListener("touchend", i), n.blur(), !e.isProgressMoving || "audio" === e.videoConfig.mediaType || e.dash || e.config.closeMoveSeek) {
                            var c = u.clientX - l, p = c / r * e.duration;
                            a.style.width = 100 * c / r + "%", c - 7 < 0 ? (o.style.left = "0px", o.style.transform = "") : c + 7 > r ? (o.style.left = r - 14 + "px", o.style.transform = "") : (o.style.left = "100%", o.style.transform = "translate(-50%, 0)"), e.currentTime = Number(p).toFixed(1)
                        }
                        e.emit("focus"), e.isProgressMoving = !1
                    };
                    return window.addEventListener("mousemove", s), window.addEventListener("touchmove", s, {passive: !1}), window.addEventListener("mouseup", c), window.addEventListener("touchend", c), !1
                })
            }), n.addEventListener("mouseenter", function (r) {
                if (!e.config.allowSeekAfterEnded && e.ended) return !1;
                var a = n.getBoundingClientRect().left, i = n.getBoundingClientRect().width, o = function (r) {
                    var o = (r.clientX - a) / i * e.duration;
                    o = o < 0 ? 0 : o, u.textContent = t.format(o);
                    var l = u.getBoundingClientRect().width;
                    if (e.config.thumbnail) {
                        m = e.duration / f;
                        var s = Math.floor(o / m);
                        c.style.backgroundImage = "url(" + v[Math.ceil((s + 1) / (y * x)) - 1] + ")";
                        var p = s + 1 - y * x * (Math.ceil((s + 1) / (y * x)) - 1), d = Math.ceil(p / x) - 1,
                            b = p - d * x - 1;
                        c.style["background-position"] = "-" + b * g + "px -" + d * h + "px";
                        var k = r.clientX - a - g / 2;
                        k = (k = k > 0 ? k : 0) < i - g ? k : i - g, c.style.left = k + "px", c.style.top = -10 - h + "px", c.style.display = "block", u.style.left = k + g / 2 - l / 2 + "px"
                    } else {
                        var w = r.clientX - a - l / 2;
                        w = (w = w > 0 ? w : 0) > i - l ? i - l : w, u.style.left = w + "px"
                    }
                    t.hasClass(n, "xgplayer-progress-dot-active") ? u.style.display = "none" : u.style.display = "block"
                }, l = function (e) {
                    o(e)
                };
                n.addEventListener("mousemove", l, !1), n.addEventListener("mouseleave", function e(t) {
                    n.removeEventListener("mousemove", l, !1), n.removeEventListener("mouseleave", e, !1), o(t), u.style.display = "none", c.style.display = "none"
                }, !1), o(r)
            }, !1);
            var b = !1, k = function () {
                if (!r && n && (r = n.getBoundingClientRect().width), "audio" !== e.videoConfig.mediaType || !e.isProgressMoving || !e.dash) {
                    a.style.width = 100 * e.currentTime / e.duration + "%";
                    var t = e.currentTime / e.duration * r - 7;
                    if (t < 0) o.style.left = "0px", o.style.transform = "", b = !1; else if (t + 14 > r) o.style.left = r - 14 + "px", o.style.transform = "", b = !1; else {
                        if (b) return;
                        o.style.left = "100%", o.style.transform = "translate(-50%, 0)", b = !0
                    }
                }
            };
            e.on("timeupdate", k);
            var w = function () {
                var t = e.buffered;
                if (t && t.length > 0) {
                    for (var n = t.end(t.length - 1), r = 0, a = t.length; r < a; r++) if (e.currentTime >= t.start(r) && e.currentTime <= t.end(r)) {
                        n = t.end(r);
                        for (var i = r + 1; i < t.length; i++) if (t.start(i) - t.end(i - 1) >= 2) {
                            n = t.end(i - 1);
                            break
                        }
                        break
                    }
                    s.style.width = n / e.duration * 100 + "%"
                }
            }, _ = ["bufferedChange", "cacheupdate", "ended", "timeupdate"];
            _.forEach(function (t) {
                e.on(t, w)
            }), e.once("destroy", function t() {
                e.removeAllProgressDot(), e.off("canplay", d), e.off("timeupdate", k), _.forEach(function (t) {
                    e.off(t, w)
                }), e.off("destroy", t)
            })
        })
    }, function (e, t, n) {
        "use strict";
        var r, a = n(0), i = (r = a) && r.__esModule ? r : {default: r};
        i.default.install("s_time", function () {
            var e = this, t = (e.root, i.default.util),
                n = t.createDom("xg-time", "<span>" + (e.currentTime || t.format(0)) + "</span>\n                                           <em>" + (e.duration || t.format(0)) + "</em>", {}, "xgplayer-time");
            e.once("ready", function () {
                e.controls.appendChild(n)
            });
            var r = function () {
                "audio" === e.videoConfig.mediaType && e.isProgressMoving && e.dash || (n.innerHTML = "<span>" + t.format(e.currentTime || 0) + "</span><em>" + t.format(e.duration) + "</em>")
            };
            e.on("durationchange", r), e.on("timeupdate", r), e.once("destroy", function t() {
                e.off("durationchange", r), e.off("timeupdate", r), e.off("destroy", t)
            })
        })
    }, function (e, t, n) {
        "use strict";
        var r = i(n(0)), a = i(n(83));

        function i(e) {
            return e && e.__esModule ? e : {default: e}
        }

        r.default.install("s_replay", function () {
            var e = this, t = r.default.util, n = e.root;
            if (!(navigator.userAgent.indexOf("iPhone OS 9") > -1)) {
                var i = e.lang.REPLAY,
                    o = t.createDom("xg-replay", a.default + '\n                                         <xg-replay-txt class="xgplayer-replay-txt">' + i + "</xg-replay-txt>\n                                        ", {}, "xgplayer-replay");
                e.once("ready", function () {
                    n.appendChild(o)
                });
                var l = o.querySelector("svg");
                ["click", "touchend"].forEach(function (t) {
                    l.addEventListener(t, function (t) {
                        t.preventDefault(), t.stopPropagation(), e.emit("replayBtnClick")
                    })
                })
            }
        })
    }, function (e, t, n) {
        "use strict";
        n.r(t), t.default = '<svg class="xgplayer-replay-svg" xmlns="http://www.w3.org/2000/svg" width="78" height="78" viewbox="0 0 78 78">\n  <path d="M8.22708362,13.8757234 L11.2677371,12.6472196 C11.7798067,12.4403301 12.3626381,12.6877273 12.5695276,13.1997969 L12.9441342,14.1269807 C13.1510237,14.6390502 12.9036264,15.2218816 12.3915569,15.4287712 L6.8284538,17.6764107 L5.90126995,18.0510173 C5.38920044,18.2579068 4.80636901,18.0105096 4.5994795,17.49844 L1.97723335,11.0081531 C1.77034384,10.4960836 2.0177411,9.91325213 2.52981061,9.70636262 L3.45699446,9.33175602 C3.96906396,9.12486652 4.5518954,9.37226378 4.75878491,9.88433329 L5.67885163,12.1615783 C7.99551726,6.6766934 13.3983951,3 19.5,3 C27.7842712,3 34.5,9.71572875 34.5,18 C34.5,26.2842712 27.7842712,33 19.5,33 C15.4573596,33 11.6658607,31.3912946 8.87004692,28.5831991 C8.28554571,27.9961303 8.28762719,27.0463851 8.87469603,26.4618839 C9.46176488,25.8773827 10.4115101,25.8794641 10.9960113,26.466533 C13.2344327,28.7147875 16.263503,30 19.5,30 C26.127417,30 31.5,24.627417 31.5,18 C31.5,11.372583 26.127417,6 19.5,6 C14.4183772,6 9.94214483,9.18783811 8.22708362,13.8757234 Z"></path>\n</svg>\n'
    }, function (e, t, n) {
        "use strict";
        var r, a = n(0), i = (r = a) && r.__esModule ? r : {default: r};
        i.default.install("s_playbackRate", function () {
            var e = this, t = i.default.util;
            if (!e.config.playbackRate) return !1;
            e.config.playbackRate.sort(function (e, t) {
                return t - e
            });
            var n = t.createDom("xg-playbackrate", " ", {}, "xgplayer-playbackrate"), r = [];
            e.config.playbackRate.forEach(function (e) {
                r.push({name: "" + e, rate: e + "x", selected: !1})
            });
            var a = 1, o = ["<ul>"];
            r.forEach(function (t) {
                e.config.defaultPlaybackRate && e.config.defaultPlaybackRate.toString() === t.name ? (t.selected = !0, a = e.config.defaultPlaybackRate, e.once("playing", function () {
                    e.video.playbackRate = e.config.defaultPlaybackRate
                })) : "1.0" !== t.name && "1" !== t.name || e.config.defaultPlaybackRate && 1 !== e.config.defaultPlaybackRate || (t.selected = !0), o.push("<li cname='" + t.name + "' class='" + (t.selected ? "selected" : "") + "'>" + t.rate + "</li>")
            }), o.push("</ul><p class='name'>" + a + "x</p>");
            var l = e.root.querySelector(".xgplayer-playbackrate");
            if (l) {
                if (l.innerHTML = o.join(""), !l) return;
                l.querySelector(".name").addEventListener("mouseenter", function (n) {
                    n.preventDefault(), n.stopPropagation(), t.addClass(e.root, "xgplayer-playbackrate-active"), l.focus()
                })
            } else {
                if (n.innerHTML = o.join(""), !n) return;
                n.querySelector(".name").addEventListener("mouseenter", function (r) {
                    r.preventDefault(), r.stopPropagation(), t.addClass(e.root, "xgplayer-playbackrate-active"), n.focus()
                }), e.once("ready", function () {
                    e.controls.appendChild(n)
                })
            }
            ["touchend", "click"].forEach(function (i) {
                n.addEventListener(i, function (i) {
                    i.stopPropagation(), i.preventDefault();
                    var o = i.target;
                    if (o && "li" === o.tagName.toLocaleLowerCase()) {
                        var l, s = void 0;
                        r.forEach(function (n) {
                            n.selected = !1, o.textContent.replace(/\s+/g, "") === n.rate && (Array.prototype.forEach.call(o.parentNode.childNodes, function (e) {
                                t.hasClass(e, "selected") && (s = parseFloat(e.getAttribute("cname")), t.removeClass(e, "selected"))
                            }), n.selected = !0, e.video.playbackRate = 1 * n.name, a = 1 * n.name)
                        }), t.addClass(o, "selected"), l = parseFloat(o.getAttribute("cname")), o.parentNode.nextSibling.innerHTML = o.getAttribute("cname") + "x", e.emit("playbackrateChange", {
                            from: s,
                            to: l
                        })
                    } else !o || "p" !== o.tagName.toLocaleLowerCase() && "span" !== o.tagName.toLocaleLowerCase() || (t.addClass(e.root, "xgplayer-playbackrate-active"), n.focus());
                    e.emit("focus")
                }, !1)
            }), n.addEventListener("mouseleave", function (n) {
                n.preventDefault(), n.stopPropagation(), t.removeClass(e.root, "xgplayer-playbackrate-active")
            }), e.on("play", function () {
                e.video.playbackRate.toFixed(1) !== a.toFixed(1) && (e.video.playbackRate = a)
            })
        })
    }, function (e, t, n) {
        "use strict";
        var r, a = n(0), i = (r = a) && r.__esModule ? r : {default: r};
        i.default.install("s_localPreview", function () {
            var e = this, t = (e.root, i.default.util);
            if (e.config.preview && e.config.preview.uploadEl) {
                var n = t.createDom("xg-preview", '<input type="file">', {}, "xgplayer-preview"),
                    r = n.querySelector("input");
                e.config.preview.uploadEl.appendChild(n), r.onchange = function () {
                    e.emit("upload", r)
                }
            }
        })
    }, function (e, t, n) {
        "use strict";
        var r = i(n(0)), a = i(n(87));

        function i(e) {
            return e && e.__esModule ? e : {default: e}
        }

        r.default.install("s_download", function () {
            var e = this, t = r.default.util;
            if (e.config.download) {
                var n = t.createDom("xg-download", '<xg-icon class="xgplayer-icon">' + a.default + "</xg-icon>", {}, "xgplayer-download"),
                    i = e.lang.DOWNLOAD_TIPS,
                    o = t.createDom("xg-tips", '<span class="xgplayer-tip-download">' + i + "</span>", {}, "xgplayer-tips");
                n.appendChild(o), e.once("ready", function () {
                    e.controls.appendChild(n)
                }), ["click", "touchend"].forEach(function (t) {
                    n.addEventListener(t, function (t) {
                        t.preventDefault(), t.stopPropagation(), e.emit("downloadBtnClick")
                    })
                })
            }
        })
    }, function (e, t, n) {
        "use strict";
        n.r(t), t.default = '<svg xmlns="http://www.w3.org/2000/svg" width="24px" height="24px" viewBox="0 0 24 24">\n  <g id="Page-1" stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">\n    <g transform="translate(-488.000000, -340.000000)" fill="#FFFFFF">\n      <g id="Group-2">\n        <g id="volme_big-copy" transform="translate(488.000000, 340.000000)">\n          <rect id="Rectangle-18" x="11" y="4" width="2" height="12" rx="1"></rect>\n          <rect id="Rectangle-2" x="3" y="18" width="18" height="2" rx="1"></rect>\n          <rect id="Rectangle-2" transform="translate(4.000000, 17.500000) rotate(90.000000) translate(-4.000000, -17.500000) " x="1.5" y="16.5" width="5" height="2" rx="1"></rect><rect id="Rectangle-2-Copy-3" transform="translate(20.000000, 17.500000) rotate(90.000000) translate(-20.000000, -17.500000) " x="17.5" y="16.5" width="5" height="2" rx="1"></rect>\n          <path d="M9.48791171,8.26502656 L9.48791171,14.2650266 C9.48791171,14.8173113 9.04019646,15.2650266 8.48791171,15.2650266 C7.93562696,15.2650266 7.48791171,14.8173113 7.48791171,14.2650266 L7.48791171,7.26502656 C7.48791171,6.71274181 7.93562696,6.26502656 8.48791171,6.26502656 L15.4879117,6.26502656 C16.0401965,6.26502656 16.4879117,6.71274181 16.4879117,7.26502656 C16.4879117,7.81731131 16.0401965,8.26502656 15.4879117,8.26502656 L9.48791171,8.26502656 Z" id="Combined-Shape" transform="translate(11.987912, 10.765027) scale(1, -1) rotate(45.000000) translate(-11.987912, -10.765027) "></path>\n        </g>\n      </g>\n    </g>\n  </g>\n</svg>\n'
    }, function (e, t, n) {
        "use strict";
        var r = o(n(0)), a = o(n(89)), i = o(n(91));

        function o(e) {
            return e && e.__esModule ? e : {default: e}
        }

        r.default.install("s_danmu", function () {
            var e = this, t = e.root, n = r.default.util;
            if (e.config.danmu) {
                var o = n.createDom("xg-danmu", "", {}, "xgplayer-danmu");
                e.once("ready", function () {
                    t.appendChild(o)
                });
                var l = n.deepCopy({
                        container: o,
                        player: e.video,
                        comments: [],
                        area: {start: 0, end: 1}
                    }, e.config.danmu),
                    s = void 0;
                e.config.danmu.panel && (s = r.default.util.createDom("xg-panel", '<xg-panel-icon class="xgplayer-panel-icon">\n                                                ' + i.default + '\n                                              </xg-panel-icon>\n                                              <xg-panel-slider class="xgplayer-panel-slider">\n                                                <xg-hidemode class="xgplayer-hidemode">\n                                                  <p class="xgplayer-hidemode-font">屏蔽类型</p>\n                                                  <ul class="xgplayer-hidemode-radio">\n                                                    <li class="xgplayer-hidemode-scroll" id="false">滚动</li><li class="xgplayer-hidemode-top" id="false">顶部</li><li class="xgplayer-hidemode-bottom" id="false">底部</li><li class="xgplayer-hidemode-color" id="false">色彩</li>\n                                                  </ul>\n                                                </xg-hidemode>\n                                                <xg-transparency class="xgplayer-transparency">\n                                                  <span>不透明度</span>\n                                                  <input class="xgplayer-transparency-line xgplayer-transparency-color xgplayer-transparency-bar xgplayer-transparency-gradient" type="range" min="0" max="100" step="0.1" value="50"></input>\n                                                </xg-transparency>\n                                                <xg-showarea class="xgplayer-showarea">\n                                                  <div class="xgplayer-showarea-name">显示区域</div>\n                                                  <div class="xgplayer-showarea-control">\n                                                    <div class="xgplayer-showarea-control-up">\n                                                      <span class="xgplayer-showarea-control-up-item xgplayer-showarea-onequarters">1/4</span>\n                                                      <span class="xgplayer-showarea-control-up-item xgplayer-showarea-twoquarters selected-color">1/2</span>\n                                                      <span class="xgplayer-showarea-control-up-item xgplayer-showarea-threequarters">3/4</span>\n                                                      <span class="xgplayer-showarea-control-up-item xgplayer-showarea-full">1</span>\n                                                    </div>\n                                                    <div class="xgplayer-showarea-control-down">\n                                                      <div class="xgplayer-showarea-control-down-dots">\n                                                        <span class="xgplayer-showarea-onequarters-dot"></span>\n                                                        <span class="xgplayer-showarea-twoquarters-dot"></span>\n                                                        <span class="xgplayer-showarea-threequarters-dot"></span>\n                                                        <span class="xgplayer-showarea-full-dot"></span>\n                                                      </div>\n                                                      <input class="xgplayer-showarea-line xgplayer-showarea-color xgplayer-showarea-bar xgplayer-gradient" type="range" min="1" max="4" step="1" value="1">\n                                                    </div>\n                                                  </div>\n                                                </xg-showarea>\n                                                <xg-danmuspeed class="xgplayer-danmuspeed">\n                                                  <div class="xgplayer-danmuspeed-name">弹幕速度</div>\n                                                  <div class="xgplayer-danmuspeed-control">\n                                                    <div class="xgplayer-danmuspeed-control-up">\n                                                      <span class="xgplayer-danmuspeed-control-up-item xgplayer-danmuspeed-small">慢</span>\n                                                      <span class="xgplayer-danmuspeed-control-up-item xgplayer-danmuspeed-middle selected-color">中</span>\n                                                      <span class="xgplayer-danmuspeed-control-up-item xgplayer-danmuspeed-large">快</span>\n                                                    </div>\n                                                    <div class="xgplayer-danmuspeed-control-down">\n                                                      <div class="xgplayer-danmuspeed-control-down-dots">\n                                                        <span class="xgplayer-danmuspeed-small-dot"></span>\n                                                        <span class="xgplayer-danmuspeed-middle-dot"></span>\n                                                        <span class="xgplayer-danmuspeed-large-dot"></span>\n                                                      </div>\n                                                      <input class="xgplayer-danmuspeed-line xgplayer-danmuspeed-color xgplayer-danmuspeed-bar xgplayer-gradient" type="range" min="50" max="150" step="50" value="100">\n                                                    </div>\n                                                  </div>\n                                                </xg-danmuspeed>\n                                                <xg-danmufont class="xgplayer-danmufont">\n                                                  <div class="xgplayer-danmufont-name">字体大小</div>\n                                                  <div class="xgplayer-danmufont-control">\n                                                    <div class="xgplayer-danmufont-control-up">\n                                                      <span class="xgplayer-danmufont-control-up-item xgplayer-danmufont-small">小</span>\n                                                      <span class="xgplayer-danmufont-control-up-item xgplayer-danmufont-middle">中</span>\n                                                      <span class="xgplayer-danmufont-control-up-item xgplayer-danmufont-large selected-color">大</span>\n                                                    </div>\n                                                    <div class="xgplayer-danmufont-control-down">\n                                                      <div class="xgplayer-danmufont-control-down-dots">\n                                                        <span class="xgplayer-danmufont-small-dot"></span>\n                                                        <span class="xgplayer-danmufont-middle-dot"></span>\n                                                        <span class="xgplayer-danmufont-large-dot"></span>\n                                                      </div>\n                                                      <input class="xgplayer-danmufont-line xgplayer-danmufont-color xgplayer-danmufont-bar xgplayer-gradient" type="range" min="20" max="30" step="5" value="25">\n                                                    </div>\n                                                  </div>\n                                                </xg-danmufont>\n                                              </xg-panel-slider>', {tabindex: 7}, "xgplayer-panel"), e.once("ready", function () {
                    e.controls.appendChild(s)
                })), e.once("complete", function () {
                    var t = new a.default(l);
                    if (e.emit("initDefaultDanmu", t), e.danmu = t, e.config.danmu.panel) {
                        var n = s.querySelector(".xgplayer-panel-slider"), i = void 0;
                        ["mouseenter", "touchend", "click"].forEach(function (e) {
                            s.addEventListener(e, function (e) {
                                e.preventDefault(), e.stopPropagation(), r.default.util.addClass(n, "xgplayer-panel-active"), s.focus(), i = !0
                            })
                        }), s.addEventListener("mouseleave", function (e) {
                            e.preventDefault(), e.stopPropagation(), r.default.util.removeClass(n, "xgplayer-panel-active"), i = !1
                        }), n.addEventListener("mouseleave", function (e) {
                            e.preventDefault(), e.stopPropagation(), !1 === i && r.default.util.removeClass(n, "xgplayer-panel-active")
                        });
                        var o = e.config.danmu, u = {
                            scroll: s.querySelector(".xgplayer-hidemode-scroll"),
                            top: s.querySelector(".xgplayer-hidemode-top"),
                            bottom: s.querySelector(".xgplayer-hidemode-bottom"),
                            color: s.querySelector(".xgplayer-hidemode-color")
                        }, c = function (t) {
                            var n = t;
                            ["touchend", "click"].forEach(function (t) {
                                u[n].addEventListener(t, function (t) {
                                    "true" !== u[n].getAttribute("id") ? (u[n].style.color = "#f85959", u[n].setAttribute("id", "true"), e.danmu.hide(n)) : (u[n].style.color = "#aaa", u[n].setAttribute("id", "false"), e.danmu.show(n))
                                })
                            })
                        };
                        for (var p in u) c(p);
                        var d = s.querySelector(".xgplayer-transparency-line"),
                            f = s.querySelector(".xgplayer-transparency-gradient"), g = 50;
                        if (f.style.background = "linear-gradient(to right, #f85959 0%, #f85959 " + g + "%, #aaa " + g + "%, #aaa)", d.addEventListener("input", function (e) {
                            e.preventDefault(), e.stopPropagation(), g = e.target.value, f.style.background = "linear-gradient(to right, #f85959 0%, #f85959 " + g + "%, #aaa " + g + "%, #aaa)", o.comments.forEach(function (e) {
                                e.style.opacity = g / 100
                            })
                        }), s.querySelector(".xgplayer-showarea-line").addEventListener("input", function (t) {
                            t.preventDefault(), t.stopPropagation();
                            var n = t.target.value;
                            e.danmu.config.area.end = n / 100, e.config.danmu.area.end = n / 100, e.danmu.bulletBtn.main.channel.resize()
                        }), s.querySelector(".xgplayer-danmuspeed-line").addEventListener("input", function (e) {
                            e.preventDefault(), e.stopPropagation();
                            var t = e.target.value;
                            o.comments.forEach(function (e) {
                                e.duration = 100 * (200 - t)
                            })
                        }), s.querySelector(".xgplayer-danmufont-line").addEventListener("input", function (e) {
                            e.preventDefault(), e.stopPropagation();
                            var t = e.target.value;
                            o.comments.forEach(function (e) {
                                e.style.fontSize = t + "px"
                            })
                        }), navigator.userAgent.indexOf("Firefox") > -1) for (var h = 0; h < n.querySelectorAll("input").length; h++) n.querySelectorAll("input")[h].style.marginTop = "10px"
                    }
                })
            }
        })
    }, function (e, t, n) {
        "use strict";
        (function (e) {
            var n, r, a, i, o = "function" == typeof Symbol && "symbol" == typeof Symbol.iterator ? function (e) {
                return typeof e
            } : function (e) {
                return e && "function" == typeof Symbol && e.constructor === Symbol && e !== Symbol.prototype ? "symbol" : typeof e
            };
            window, i = function () {
                return function (e) {
                    var t = {};

                    function n(r) {
                        if (t[r]) return t[r].exports;
                        var a = t[r] = {i: r, l: !1, exports: {}};
                        return e[r].call(a.exports, a, a.exports, n), a.l = !0, a.exports
                    }

                    return n.m = e, n.c = t, n.d = function (e, t, r) {
                        n.o(e, t) || Object.defineProperty(e, t, {enumerable: !0, get: r})
                    }, n.r = function (e) {
                        "undefined" != typeof Symbol && Symbol.toStringTag && Object.defineProperty(e, Symbol.toStringTag, {value: "Module"}), Object.defineProperty(e, "__esModule", {value: !0})
                    }, n.t = function (e, t) {
                        if (1 & t && (e = n(e)), 8 & t) return e;
                        if (4 & t && "object" == (void 0 === e ? "undefined" : o(e)) && e && e.__esModule) return e;
                        var r = Object.create(null);
                        if (n.r(r), Object.defineProperty(r, "default", {
                            enumerable: !0,
                            value: e
                        }), 2 & t && "string" != typeof e) for (var a in e) n.d(r, a, function (t) {
                            return e[t]
                        }.bind(null, a));
                        return r
                    }, n.n = function (e) {
                        var t = e && e.__esModule ? function () {
                            return e.default
                        } : function () {
                            return e
                        };
                        return n.d(t, "a", t), t
                    }, n.o = function (e, t) {
                        return Object.prototype.hasOwnProperty.call(e, t)
                    }, n.p = "", n(n.s = 2)
                }([function (e, t, n) {
                    Object.defineProperty(t, "__esModule", {value: !0});
                    var r, a = (r = n(22)) && r.__esModule ? r : {default: r}, i = {};
                    i.domObj = new a.default, i.createDom = function () {
                        var e = arguments.length > 0 && void 0 !== arguments[0] ? arguments[0] : "div",
                            t = arguments.length > 1 && void 0 !== arguments[1] ? arguments[1] : "",
                            n = arguments.length > 2 && void 0 !== arguments[2] ? arguments[2] : {},
                            r = arguments.length > 3 && void 0 !== arguments[3] ? arguments[3] : "",
                            a = document.createElement(e);
                        return a.className = r, a.innerHTML = t, Object.keys(n).forEach(function (t) {
                            var r = t, i = n[t];
                            "video" === e || "audio" === e ? i && a.setAttribute(r, i) : a.setAttribute(r, i)
                        }), a
                    }, i.hasClass = function (e, t) {
                        return e.classList ? Array.prototype.some.call(e.classList, function (e) {
                            return e === t
                        }) : !!e.className.match(new RegExp("(\\s|^)" + t + "(\\s|$)"))
                    }, i.addClass = function (e, t) {
                        e.classList ? t.replace(/(^\s+|\s+$)/g, "").split(/\s+/g).forEach(function (t) {
                            t && e.classList.add(t)
                        }) : i.hasClass(e, t) || (e.className += " " + t)
                    }, i.removeClass = function (e, t) {
                        e.classList ? t.split(/\s+/g).forEach(function (t) {
                            e.classList.remove(t)
                        }) : i.hasClass(e, t) && t.split(/\s+/g).forEach(function (t) {
                            var n = new RegExp("(\\s|^)" + t + "(\\s|$)");
                            e.className = e.className.replace(n, " ")
                        })
                    }, i.toggleClass = function (e, t) {
                        t.split(/\s+/g).forEach(function (t) {
                            i.hasClass(e, t) ? i.removeClass(e, t) : i.addClass(e, t)
                        })
                    }, i.findDom = function () {
                        var e = arguments.length > 0 && void 0 !== arguments[0] ? arguments[0] : document,
                            t = arguments[1],
                            n = void 0;
                        try {
                            n = e.querySelector(t)
                        } catch (r) {
                            t.startsWith("#") && (n = e.getElementById(t.slice(1)))
                        }
                        return n
                    }, i.deepCopy = function (e, t) {
                        if ("Object" === i.typeOf(t) && "Object" === i.typeOf(e)) return Object.keys(t).forEach(function (n) {
                            "Object" !== i.typeOf(t[n]) || t[n] instanceof Node ? "Array" === i.typeOf(t[n]) ? e[n] = "Array" === i.typeOf(e[n]) ? e[n].concat(t[n]) : t[n] : e[n] = t[n] : e[n] ? i.deepCopy(e[n], t[n]) : e[n] = t[n]
                        }), e
                    }, i.typeOf = function (e) {
                        return Object.prototype.toString.call(e).match(/([^\s.*]+)(?=]$)/g)[0]
                    }, i.copyDom = function (e) {
                        if (e && 1 === e.nodeType) {
                            var t = document.createElement(e.tagName);
                            return Array.prototype.forEach.call(e.attributes, function (e) {
                                t.setAttribute(e.name, e.value)
                            }), e.innerHTML && (t.innerHTML = e.innerHTML), t
                        }
                        return ""
                    }, i.formatTime = function (e) {
                        var t = Math.floor(e);
                        return 1e3 * t + (e - t)
                    }, t.default = i, e.exports = t.default
                }, function (e, t, n) {
                    var r = n(13)();
                    e.exports = function (e) {
                        return e !== r && null !== e
                    }
                }, function (e, t, n) {
                    e.exports = n(3)
                }, function (e, t, n) {
                    Object.defineProperty(t, "__esModule", {value: !0});
                    var r, a = (r = n(4)) && r.__esModule ? r : {default: r};
                    n(26), t.default = a.default, e.exports = t.default
                }, function (e, t, n) {
                    Object.defineProperty(t, "__esModule", {value: !0});
                    var r = function () {
                        function e(e, t) {
                            for (var n = 0; n < t.length; n++) {
                                var r = t[n];
                                r.enumerable = r.enumerable || !1, r.configurable = !0, "value" in r && (r.writable = !0), Object.defineProperty(e, r.key, r)
                            }
                        }

                        return function (t, n, r) {
                            return n && e(t.prototype, n), r && e(t, r), t
                        }
                    }(), a = l(n(5)), i = l(n(21)), o = l(n(0));

                    function l(e) {
                        return e && e.__esModule ? e : {default: e}
                    }

                    var s = function () {
                        function e(t) {
                            !function (e, t) {
                                if (!(e instanceof t)) throw new TypeError("Cannot call a class as a function")
                            }(this, e), this.config = o.default.deepCopy({
                                overlap: !1,
                                area: {start: 0, end: 1},
                                live: !1,
                                comments: [],
                                direction: "r2l"
                            }, t), this.hideArr = [], (0, a.default)(this);
                            var n = this;
                            if (this.config.comments.forEach(function (e) {
                                e.duration = e.duration < 5e3 ? 5e3 : e.duration, e.mode || (e.mode = "scroll")
                            }), !this.config.container || 1 !== this.config.container.nodeType) return this.emit("error", "container id can't be empty"), !1;
                            if (this.container = this.config.container, this.config.containerStyle) {
                                var r = this.config.containerStyle;
                                Object.keys(r).forEach(function (e) {
                                    n.container.style[e] = r[e]
                                })
                            }
                            this.live = this.config.live, this.player = this.config.player, this.direction = this.config.direction, o.default.addClass(this.container, "danmu"), this.bulletBtn = new i.default(this), this.emit("ready")
                        }

                        return r(e, [{
                            key: "start", value: function () {
                                this.bulletBtn.main.start()
                            }
                        }, {
                            key: "pause", value: function () {
                                this.bulletBtn.main.pause()
                            }
                        }, {
                            key: "play", value: function () {
                                this.bulletBtn.main.play()
                            }
                        }, {
                            key: "stop", value: function () {
                                this.bulletBtn.main.stop()
                            }
                        }, {
                            key: "sendComment", value: function (e) {
                                e && e.id && e.duration && (e.el || e.txt) && (e.duration = e.duration < 5e3 ? 5e3 : e.duration, this.bulletBtn.main.data.push(e))
                            }
                        }, {
                            key: "setCommentID", value: function (e, t) {
                                var n = this.container.getBoundingClientRect();
                                e && t && (this.bulletBtn.main.data.some(function (n) {
                                    return n.id === e && (n.id = t, !0)
                                }), this.bulletBtn.main.queue.some(function (r) {
                                    return r.id === e && (r.id = t, r.pauseMove(n), r.startMove(n), !0)
                                }))
                            }
                        }, {
                            key: "setCommentDuration", value: function (e, t) {
                                var n = this.container.getBoundingClientRect();
                                e && t && (t = t < 5e3 ? 5e3 : t, this.bulletBtn.main.data.some(function (n) {
                                    return n.id === e && (n.duration = t, !0)
                                }), this.bulletBtn.main.queue.some(function (r) {
                                    return r.id === e && (r.duration = t, r.pauseMove(n), r.startMove(n), !0)
                                }))
                            }
                        }, {
                            key: "setAllDuration", value: function () {
                                var e = arguments.length > 0 && void 0 !== arguments[0] ? arguments[0] : "scroll",
                                    t = arguments[1], n = this.container.getBoundingClientRect();
                                t && (t = t < 5e3 ? 5e3 : t, this.bulletBtn.main.data.forEach(function (n) {
                                    e === n.mode && (n.duration = t)
                                }), this.bulletBtn.main.queue.forEach(function (r) {
                                    e === r.mode && (r.duration = t, r.pauseMove(n), r.startMove(n))
                                }))
                            }
                        }, {
                            key: "hide", value: function () {
                                var e = arguments.length > 0 && void 0 !== arguments[0] ? arguments[0] : "scroll";
                                this.hideArr.indexOf(e) < 0 && this.hideArr.push(e), this.bulletBtn.main.queue.filter(function (t) {
                                    return e === t.mode || "color" === e && t.color
                                }).forEach(function (e) {
                                    return e.remove()
                                })
                            }
                        }, {
                            key: "show", value: function () {
                                var e = arguments.length > 0 && void 0 !== arguments[0] ? arguments[0] : "scroll",
                                    t = this.hideArr.indexOf(e);
                                t > -1 && this.hideArr.splice(t, 1)
                            }
                        }, {
                            key: "setDirection", value: function () {
                                var e = arguments.length > 0 && void 0 !== arguments[0] ? arguments[0] : "r2l";
                                this.emit("changeDirection", e)
                            }
                        }]), e
                    }();
                    t.default = s, e.exports = t.default
                }, function (e, t, n) {
                    var r, a, i, l, s, u, c, p = n(6), d = n(20), f = Function.prototype.apply,
                        g = Function.prototype.call,
                        h = Object.create, y = Object.defineProperty, x = Object.defineProperties,
                        m = Object.prototype.hasOwnProperty, v = {configurable: !0, enumerable: !1, writable: !0};
                    a = function (e, t) {
                        var n, a;
                        return d(t), a = this, r.call(this, e, n = function () {
                            i.call(a, e, n), f.call(t, this, arguments)
                        }), n.__eeOnceListener__ = t, this
                    }, s = {
                        on: r = function (e, t) {
                            var n;
                            return d(t), m.call(this, "__ee__") ? n = this.__ee__ : (n = v.value = h(null), y(this, "__ee__", v), v.value = null), n[e] ? "object" == o(n[e]) ? n[e].push(t) : n[e] = [n[e], t] : n[e] = t, this
                        }, once: a, off: i = function (e, t) {
                            var n, r, a, i;
                            if (d(t), !m.call(this, "__ee__")) return this;
                            if (!(n = this.__ee__)[e]) return this;
                            if ("object" == o(r = n[e])) for (i = 0; a = r[i]; ++i) a !== t && a.__eeOnceListener__ !== t || (2 === r.length ? n[e] = r[i ? 0 : 1] : r.splice(i, 1)); else r !== t && r.__eeOnceListener__ !== t || delete n[e];
                            return this
                        }, emit: l = function (e) {
                            var t, n, r, a, i;
                            if (m.call(this, "__ee__") && (a = this.__ee__[e])) if ("object" == (void 0 === a ? "undefined" : o(a))) {
                                for (n = arguments.length, i = new Array(n - 1), t = 1; t < n; ++t) i[t - 1] = arguments[t];
                                for (a = a.slice(), t = 0; r = a[t]; ++t) f.call(r, this, i)
                            } else switch (arguments.length) {
                                case 1:
                                    g.call(a, this);
                                    break;
                                case 2:
                                    g.call(a, this, arguments[1]);
                                    break;
                                case 3:
                                    g.call(a, this, arguments[1], arguments[2]);
                                    break;
                                default:
                                    for (n = arguments.length, i = new Array(n - 1), t = 1; t < n; ++t) i[t - 1] = arguments[t];
                                    f.call(a, this, i)
                            }
                        }
                    }, u = {on: p(r), once: p(a), off: p(i), emit: p(l)}, c = x({}, u), e.exports = t = function (e) {
                        return null == e ? h(c) : x(Object(e), u)
                    }, t.methods = s
                }, function (e, t, n) {
                    var r = n(7), a = n(15), i = n(16), o = n(17);
                    (e.exports = function (e, t) {
                        var n, i, l, s, u;
                        return arguments.length < 2 || "string" != typeof e ? (s = t, t = e, e = null) : s = arguments[2], null == e ? (n = l = !0, i = !1) : (n = o.call(e, "c"), i = o.call(e, "e"), l = o.call(e, "w")), u = {
                            value: t,
                            configurable: n,
                            enumerable: i,
                            writable: l
                        }, s ? r(a(s), u) : u
                    }).gs = function (e, t, n) {
                        var l, s, u, c;
                        return "string" != typeof e ? (u = n, n = t, t = e, e = null) : u = arguments[3], null == t ? t = void 0 : i(t) ? null == n ? n = void 0 : i(n) || (u = n, n = void 0) : (u = t, t = n = void 0), null == e ? (l = !0, s = !1) : (l = o.call(e, "c"), s = o.call(e, "e")), c = {
                            get: t,
                            set: n,
                            configurable: l,
                            enumerable: s
                        }, u ? r(a(u), c) : c
                    }
                }, function (e, t, n) {
                    e.exports = n(8)() ? Object.assign : n(9)
                }, function (e, t, n) {
                    e.exports = function () {
                        var e, t = Object.assign;
                        return "function" == typeof t && (t(e = {foo: "raz"}, {bar: "dwa"}, {trzy: "trzy"}), e.foo + e.bar + e.trzy === "razdwatrzy")
                    }
                }, function (e, t, n) {
                    var r = n(10), a = n(14), i = Math.max;
                    e.exports = function (e, t) {
                        var n, o, l, s = i(arguments.length, 2);
                        for (e = Object(a(e)), l = function (r) {
                            try {
                                e[r] = t[r]
                            } catch (e) {
                                n || (n = e)
                            }
                        }, o = 1; o < s; ++o) t = arguments[o], r(t).forEach(l);
                        if (void 0 !== n) throw n;
                        return e
                    }
                }, function (e, t, n) {
                    e.exports = n(11)() ? Object.keys : n(12)
                }, function (e, t, n) {
                    e.exports = function () {
                        try {
                            return Object.keys("primitive"), !0
                        } catch (e) {
                            return !1
                        }
                    }
                }, function (e, t, n) {
                    var r = n(1), a = Object.keys;
                    e.exports = function (e) {
                        return a(r(e) ? Object(e) : e)
                    }
                }, function (e, t, n) {
                    e.exports = function () {
                    }
                }, function (e, t, n) {
                    var r = n(1);
                    e.exports = function (e) {
                        if (!r(e)) throw new TypeError("Cannot use null or undefined");
                        return e
                    }
                }, function (e, t, n) {
                    var r = n(1), a = Array.prototype.forEach, i = Object.create;
                    e.exports = function (e) {
                        var t = i(null);
                        return a.call(arguments, function (e) {
                            r(e) && function (e, t) {
                                var n;
                                for (n in e) t[n] = e[n]
                            }(Object(e), t)
                        }), t
                    }
                }, function (e, t, n) {
                    e.exports = function (e) {
                        return "function" == typeof e
                    }
                }, function (e, t, n) {
                    e.exports = n(18)() ? String.prototype.contains : n(19)
                }, function (e, t, n) {
                    var r = "razdwatrzy";
                    e.exports = function () {
                        return "function" == typeof r.contains && !0 === r.contains("dwa") && !1 === r.contains("foo")
                    }
                }, function (e, t, n) {
                    var r = String.prototype.indexOf;
                    e.exports = function (e) {
                        return r.call(this, e, arguments[1]) > -1
                    }
                }, function (e, t, n) {
                    e.exports = function (e) {
                        if ("function" != typeof e) throw new TypeError(e + " is not a function");
                        return e
                    }
                }, function (e, t, n) {
                    Object.defineProperty(t, "__esModule", {value: !0});
                    var r = function () {
                        function e(e, t) {
                            for (var n = 0; n < t.length; n++) {
                                var r = t[n];
                                r.enumerable = r.enumerable || !1, r.configurable = !0, "value" in r && (r.writable = !0), Object.defineProperty(e, r.key, r)
                            }
                        }

                        return function (t, n, r) {
                            return n && e(t.prototype, n), r && e(t, r), t
                        }
                    }(), a = o(n(0)), i = o(n(23));

                    function o(e) {
                        return e && e.__esModule ? e : {default: e}
                    }

                    var l = function () {
                        function e(t) {
                            !function (e, t) {
                                if (!(e instanceof t)) throw new TypeError("Cannot call a class as a function")
                            }(this, e), this.danmu = t, this.main = new i.default(t), t.config.defaultOff || this.main.start()
                        }

                        return r(e, [{
                            key: "createSwitch", value: function () {
                                var e = !(arguments.length > 0 && void 0 !== arguments[0]) || arguments[0];
                                return this.switchBtn = a.default.createDom("dk-switch", '<span class="txt">弹</span>', {}, "danmu-switch " + (e ? "danmu-switch-active" : "")), this.switchBtn
                            }
                        }]), e
                    }();
                    t.default = l, e.exports = t.default
                }, function (e, t, n) {
                    Object.defineProperty(t, "__esModule", {value: !0});
                    var r = function () {
                        function e(e, t) {
                            for (var n = 0; n < t.length; n++) {
                                var r = t[n];
                                r.enumerable = r.enumerable || !1, r.configurable = !0, "value" in r && (r.writable = !0), Object.defineProperty(e, r.key, r)
                            }
                        }

                        return function (t, n, r) {
                            return n && e(t.prototype, n), r && e(t, r), t
                        }
                    }(), a = function () {
                        function e(t) {
                            !function (e, t) {
                                if (!(e instanceof t)) throw new TypeError("Cannot call a class as a function")
                            }(this, e), t = {
                                initDOM: function () {
                                    return document.createElement("div")
                                }, initSize: 10
                            }, this.init(t)
                        }

                        return r(e, [{
                            key: "init", value: function (e) {
                                this.idleList = [], this.usingList = [], this._id = 0, this.options = e, this._expand(e.initSize)
                            }
                        }, {
                            key: "use", value: function () {
                                this.idleList.length || this._expand(1);
                                var e = this.idleList.shift();
                                return this.usingList.push(e), e
                            }
                        }, {
                            key: "unuse", value: function (e) {
                                var t = this.usingList.indexOf(e);
                                t < 0 || (this.usingList.splice(t, 1), e.innerHTML = "", e.textcontent = "", e.style = "", this.idleList.push(e))
                            }
                        }, {
                            key: "_expand", value: function (e) {
                                for (var t = 0; t < e; t++) this.idleList.push(this.options.initDOM(this._id++))
                            }
                        }]), e
                    }();
                    t.default = a, e.exports = t.default
                }, function (e, t, n) {
                    Object.defineProperty(t, "__esModule", {value: !0});
                    var r = function () {
                        function e(e, t) {
                            for (var n = 0; n < t.length; n++) {
                                var r = t[n];
                                r.enumerable = r.enumerable || !1, r.configurable = !0, "value" in r && (r.writable = !0), Object.defineProperty(e, r.key, r)
                            }
                        }

                        return function (t, n, r) {
                            return n && e(t.prototype, n), r && e(t, r), t
                        }
                    }(), a = l(n(24)), i = l(n(25)), o = l(n(0));

                    function l(e) {
                        return e && e.__esModule ? e : {default: e}
                    }

                    var s = function () {
                        function e(t) {
                            !function (e, t) {
                                if (!(e instanceof t)) throw new TypeError("Cannot call a class as a function")
                            }(this, e), this.danmu = t, this.container = t.container, this.channel = new a.default(t), this.data = [].concat(t.config.comments), this.queue = [], this.timer = null, this.retryTimer = null, this.interval = 2e3, this.status = "idle", t.on("bullet_remove", this.updateQueue.bind(this));
                            var n = this;
                            this.danmu.on("changeDirection", function (e) {
                                n.danmu.direction = e
                            })
                        }

                        return r(e, [{
                            key: "updateQueue", value: function (e) {
                                var t = this;
                                t.queue.some(function (n, r) {
                                    return n.id === e.bullet.id && (t.queue.splice(r, 1), !0)
                                })
                            }
                        }, {
                            key: "init", value: function (e, t) {
                                t || (t = this), t.data.sort(function (e, t) {
                                    return e.start - t.start
                                }), t.retryTimer || (t.retryTimer = setInterval(function () {
                                    t.readData(), t.dataHandle()
                                }, t.interval - 1e3))
                            }
                        }, {
                            key: "start", value: function () {
                                this.status = "playing", this.queue = [], this.container.innerHTML = "", this.channel.resetWithCb(this.init, this)
                            }
                        }, {
                            key: "stop", value: function () {
                                this.status = "closed", clearInterval(this.retryTimer), this.retryTimer = null, this.channel.reset(), this.queue = [], this.container.innerHTML = ""
                            }
                        }, {
                            key: "play", value: function () {
                                this.status = "playing";
                                var e = this.channel.channels, t = this.danmu.container.getBoundingClientRect();
                                e && e.length > 0 && ["scroll", "top", "bottom"].forEach(function (n) {
                                    for (var r = 0; r < e.length; r++) e[r].queue[n].forEach(function (e) {
                                        e.resized || (e.startMove(t), e.resized = !0)
                                    });
                                    for (var a = 0; a < e.length; a++) e[a].queue[n].forEach(function (e) {
                                        e.resized = !1
                                    })
                                })
                            }
                        }, {
                            key: "pause", value: function () {
                                this.status = "paused";
                                var e = this.channel.channels, t = this.danmu.container.getBoundingClientRect();
                                e && e.length > 0 && ["scroll", "top", "bottom"].forEach(function (n) {
                                    for (var r = 0; r < e.length; r++) e[r].queue[n].forEach(function (e) {
                                        e.pauseMove(t)
                                    })
                                })
                            }
                        }, {
                            key: "dataHandle", value: function () {
                                var e = this;
                                "paused" !== this.status && "closed" !== this.status && e.queue.length && e.queue.forEach(function (t) {
                                    "waiting" !== t.status && "paused" !== t.status || t.startMove(e.channel.containerPos)
                                })
                            }
                        }, {
                            key: "readData", value: function () {
                                var e = this, t = this.danmu, n = 0;
                                t.player && t.player.currentTime && (n = o.default.formatTime(t.player.currentTime));
                                var r = void 0, a = e.interval, l = e.channel, s = void 0;
                                t.player ? (s = e.data.filter(function (t) {
                                    return !t.start && e.danmu.hideArr.indexOf(t.mode) < 0 && (!t.color || e.danmu.hideArr.indexOf("color") < 0) && (t.start = n), e.danmu.hideArr.indexOf(t.mode) < 0 && (!t.color || e.danmu.hideArr.indexOf("color") < 0) && t.start - a <= n && n <= t.start + a
                                }), t.live && (e.data = e.data.filter(function (e) {
                                    return e.start || (e.start = n), e.start > n - 3 * a
                                }))) : s = e.data.filter(function (t) {
                                    return e.danmu.hideArr.indexOf(t.mode) < 0 && (!t.color || e.danmu.hideArr.indexOf("color") < 0)
                                }), s.length > 0 && s.forEach(function (n) {
                                    (r = new i.default(t, n)).attach(), l.addBullet(r).result ? (e.queue.push(r), r.topInit()) : r.detach()
                                })
                            }
                        }]), e
                    }();
                    t.default = s, e.exports = t.default
                }, function (e, t, n) {
                    Object.defineProperty(t, "__esModule", {value: !0});
                    var r = function () {
                        function e(e, t) {
                            for (var n = 0; n < t.length; n++) {
                                var r = t[n];
                                r.enumerable = r.enumerable || !1, r.configurable = !0, "value" in r && (r.writable = !0), Object.defineProperty(e, r.key, r)
                            }
                        }

                        return function (t, n, r) {
                            return n && e(t.prototype, n), r && e(t, r), t
                        }
                    }(), a = function () {
                        function e(t) {
                            !function (e, t) {
                                if (!(e instanceof t)) throw new TypeError("Cannot call a class as a function")
                            }(this, e), this.danmu = t, this.reset();
                            var n = this;
                            this.danmu.on("bullet_remove", function (e) {
                                n.removeBullet(e.bullet)
                            }), this.direction = t.direction, this.danmu.on("changeDirection", function (e) {
                                n.direction = e
                            }), this.containerPos = this.danmu.container.getBoundingClientRect(), this.containerWidth = this.containerPos.width, this.containerHeight = this.containerPos.height, this.containerLeft = this.containerPos.left, this.containerRight = this.containerPos.right, this.danmu.bulletResizeTimer = setInterval(function () {
                                n.containerPos = n.danmu.container.getBoundingClientRect(), (Math.abs(n.containerPos.width - n.containerWidth) >= 2 || Math.abs(n.containerPos.height - n.containerHeight) >= 2 || Math.abs(n.containerPos.left - n.containerLeft) >= 2 || Math.abs(n.containerPos.right - n.containerRight) >= 2) && (n.containerWidth = n.containerPos.width, n.containerHeight = n.containerPos.height, n.containerLeft = n.containerPos.left, n.containerRight = n.containerPos.right, n.resize(!0))
                            }, 50)
                        }

                        return r(e, [{
                            key: "resize", value: function () {
                                var e = arguments.length > 0 && void 0 !== arguments[0] && arguments[0],
                                    t = this.danmu.container, n = this;
                                setTimeout(function () {
                                    n.danmu.bulletBtn.main.data && n.danmu.bulletBtn.main.data.forEach(function (e) {
                                        e.bookChannelId && delete e.bookChannelId
                                    });
                                    var r = t.getBoundingClientRect();
                                    n.width = r.width, n.height = r.height, n.danmu.config.area && n.danmu.config.area.start >= 0 && n.danmu.config.area.end >= n.danmu.config.area.start && ("b2t" === n.direction ? n.width = n.width * (n.danmu.config.area.end - n.danmu.config.area.start) : n.height = n.height * (n.danmu.config.area.end - n.danmu.config.area.start)), n.container = t;
                                    var a, i = /mobile/gi.test(navigator.userAgent) ? 10 : 12;
                                    a = "b2t" === n.direction ? Math.floor(n.width / i) : Math.floor(n.height / i);
                                    for (var o = [], l = 0; l < a; l++) o[l] = {
                                        id: l,
                                        queue: {scroll: [], top: [], bottom: []},
                                        operating: {scroll: !1, top: !1, bottom: !1},
                                        bookId: {}
                                    };
                                    if (n.channels && n.channels.length <= o.length) {
                                        for (var s = function (t) {
                                            o[t] = {
                                                id: t,
                                                queue: {scroll: [], top: [], bottom: []},
                                                operating: {scroll: !1, top: !1, bottom: !1},
                                                bookId: {}
                                            }, ["scroll", "top"].forEach(function (r) {
                                                n.channels[t].queue[r].forEach(function (a) {
                                                    a.el && (o[t].queue[r].push(a), a.resized || (a.pauseMove(n.containerPos, e), a.startMove(n.containerPos), a.resized = !0))
                                                })
                                            }), n.channels[t].queue.bottom.forEach(function (r) {
                                                if (r.el) {
                                                    if (o[t + o.length - n.channels.length].queue.bottom.push(r), r.channel_id[0] + r.channel_id[1] - 1 === t) {
                                                        var a = [].concat(r.channel_id);
                                                        r.channel_id = [a[0] - n.channels.length + o.length, a[1]], r.top = r.channel_id[0] * i, n.danmu.config.area && n.danmu.config.area.start && (r.top += n.containerHeight * n.danmu.config.area.start), r.topInit()
                                                    }
                                                    r.resized || (r.pauseMove(n.containerPos, e), r.startMove(n.containerPos), r.resized = !0)
                                                }
                                            })
                                        }, u = 0; u < n.channels.length; u++) s(u);
                                        for (var c = function (e) {
                                            ["scroll", "top", "bottom"].forEach(function (t) {
                                                o[e].queue[t].forEach(function (e) {
                                                    e.resized = !1
                                                })
                                            })
                                        }, p = 0; p < o.length; p++) c(p);
                                        n.channels = o, "b2t" === n.direction ? n.channelWidth = i : n.channelHeight = i
                                    } else if (n.channels && n.channels.length > o.length) {
                                        for (var d = function (t) {
                                            o[t] = {
                                                id: t,
                                                queue: {scroll: [], top: [], bottom: []},
                                                operating: {scroll: !1, top: !1, bottom: !1},
                                                bookId: {}
                                            }, ["scroll", "top", "bottom"].forEach(function (r) {
                                                if ("top" === r && t > Math.floor(o.length / 2)) ; else if ("bottom" === r && t <= Math.floor(o.length / 2)) ; else {
                                                    var a = "bottom" === r ? t - o.length + n.channels.length : t;
                                                    n.channels[a].queue[r].forEach(function (l, s) {
                                                        if (l.el) {
                                                            if (o[t].queue[r].push(l), "bottom" === r && l.channel_id[0] + l.channel_id[1] - 1 === a) {
                                                                var u = [].concat(l.channel_id);
                                                                l.channel_id = [u[0] - n.channels.length + o.length, u[1]], l.top = l.channel_id[0] * i, n.danmu.config.area && n.danmu.config.area.start && (l.top += n.containerHeight * n.danmu.config.area.start), l.topInit()
                                                            }
                                                            l.resized || (l.pauseMove(n.containerPos, e), l.startMove(n.containerPos), l.resized = !0)
                                                        }
                                                        n.channels[a].queue[r].splice(s, 1)
                                                    })
                                                }
                                            })
                                        }, f = 0; f < o.length; f++) d(f);
                                        for (var g = function (e) {
                                            ["scroll", "top", "bottom"].forEach(function (t) {
                                                n.channels[e].queue[t].forEach(function (e) {
                                                    e.pauseMove(n.containerPos), e.remove()
                                                })
                                            })
                                        }, h = o.length; h < n.channels.length; h++) g(h);
                                        for (var y = function (e) {
                                            ["scroll", "top", "bottom"].forEach(function (t) {
                                                o[e].queue[t].forEach(function (e) {
                                                    e.resized = !1
                                                })
                                            })
                                        }, x = 0; x < o.length; x++) y(x);
                                        n.channels = o, "b2t" === n.direction ? n.channelWidth = i : n.channelHeight = i
                                    }
                                }, 10)
                            }
                        }, {
                            key: "addBullet", value: function (e) {
                                var t = this.danmu, n = this.channels, r = void 0, a = void 0, i = void 0;
                                if ("b2t" === this.direction ? (a = this.channelWidth, i = Math.ceil(e.width / a)) : (r = this.channelHeight, i = Math.ceil(e.height / r)), i > n.length) return {
                                    result: !1,
                                    message: "exceed channels.length, occupy=" + i + ",channelsSize=" + n.length
                                };
                                for (var o = !0, l = void 0, s = -1, u = 0, c = n.length; u < c; u++) if (n[u].queue[e.mode].some(function (t) {
                                    return t.id === e.id
                                })) return {result: !1, message: "exsited, channelOrder=" + u + ",danmu_id=" + e.id};
                                if ("scroll" === e.mode) for (var p = 0, d = n.length - i; p <= d; p++) {
                                    o = !0;
                                    for (var f = p; f < p + i; f++) {
                                        if ((l = n[f]).operating.scroll) {
                                            o = !1;
                                            break
                                        }
                                        if ((l.bookId.scroll || e.prior) && l.bookId.scroll !== e.id) {
                                            o = !1;
                                            break
                                        }
                                        l.operating.scroll = !0;
                                        var g = l.queue.scroll[0];
                                        if (g) {
                                            var h = g.el.getBoundingClientRect();
                                            if ("b2t" === this.direction) {
                                                if (h.bottom > this.containerPos.bottom) {
                                                    o = !1, l.operating.scroll = !1;
                                                    break
                                                }
                                            } else if (h.right > this.containerPos.right) {
                                                o = !1, l.operating.scroll = !1;
                                                break
                                            }
                                            var y, x = void 0, m = void 0, v = void 0, b = void 0;
                                            if ("b2t" === this.direction ? (m = (h.top - this.containerPos.top + h.height) / (x = (this.containerPos.height + h.height) / g.duration), v = this.containerPos.height, b = (this.containerPos.height + e.height) / e.duration) : (m = (h.left - this.containerPos.left + h.width) / (x = (this.containerPos.width + h.width) / g.duration), v = this.containerPos.width, b = (this.containerPos.width + e.width) / e.duration), y = v / b, t.config.bOffset || (t.config.bOffset = 0), x < b && m + t.config.bOffset > y) {
                                                o = !1, l.operating.scroll = !1;
                                                break
                                            }
                                        }
                                        l.operating.scroll = !1
                                    }
                                    if (o) {
                                        s = p;
                                        break
                                    }
                                } else if ("top" === e.mode) for (var k = 0, w = n.length - i; k <= w; k++) {
                                    o = !0;
                                    for (var _ = k; _ < k + i; _++) {
                                        if (_ > Math.floor(n.length / 2)) {
                                            o = !1;
                                            break
                                        }
                                        if ((l = n[_]).operating[e.mode]) {
                                            o = !1;
                                            break
                                        }
                                        if ((l.bookId[e.mode] || e.prior) && l.bookId[e.mode] !== e.id) {
                                            o = !1;
                                            break
                                        }
                                        if (l.operating[e.mode] = !0, l.queue[e.mode].length > 0) {
                                            o = !1, l.operating[e.mode] = !1;
                                            break
                                        }
                                        l.operating[e.mode] = !1
                                    }
                                    if (o) {
                                        s = k;
                                        break
                                    }
                                } else if ("bottom" === e.mode) for (var E = n.length - i; E >= 0; E--) {
                                    o = !0;
                                    for (var C = E; C < E + i; C++) {
                                        if (C <= Math.floor(n.length / 2)) {
                                            o = !1;
                                            break
                                        }
                                        if ((l = n[C]).operating[e.mode]) {
                                            o = !1;
                                            break
                                        }
                                        if ((l.bookId[e.mode] || e.prior) && l.bookId[e.mode] !== e.id) {
                                            o = !1;
                                            break
                                        }
                                        if (l.operating[e.mode] = !0, l.queue[e.mode].length > 0) {
                                            o = !1, l.operating[e.mode] = !1;
                                            break
                                        }
                                        l.operating[e.mode] = !1
                                    }
                                    if (o) {
                                        s = E;
                                        break
                                    }
                                }
                                if (-1 !== s) {
                                    for (var S = s, P = s + i; S < P; S++) (l = n[S]).operating[e.mode] = !0, l.queue[e.mode].unshift(e), e.prior && delete l.bookId[e.mode], l.operating[e.mode] = !1;
                                    return e.prior && (delete e.bookChannelId, t.bulletBtn.main.data.some(function (t) {
                                        return t.id === e.id && (delete t.bookChannelId, !0)
                                    })), e.channel_id = [s, i], "b2t" === this.direction ? (e.top = s * a, this.danmu.config.area && this.danmu.config.area.start && (e.top += this.containerWidth * this.danmu.config.area.start)) : (e.top = s * r, this.danmu.config.area && this.danmu.config.area.start && (e.top += this.containerHeight * this.danmu.config.area.start)), {
                                        result: e,
                                        message: "success"
                                    }
                                }
                                if (e.prior) if (e.bookChannelId) t.bulletBtn.main.data.some(function (t) {
                                    return t.id === e.id && (t.start += 2e3, !0)
                                }); else {
                                    s = -1;
                                    for (var T = 0, O = n.length - i; T <= O; T++) {
                                        o = !0;
                                        for (var L = T; L < T + i; L++) if (n[L].bookId[e.mode]) {
                                            o = !1;
                                            break
                                        }
                                        if (o) {
                                            s = T;
                                            break
                                        }
                                    }
                                    if (-1 !== s) {
                                        for (var D = s; D < s + i; D++) n[D].bookId[e.mode] = e.id;
                                        t.bulletBtn.main.data.some(function (t) {
                                            return t.id === e.id && (t.start += 2e3, t.bookChannelId = [s, i], !0)
                                        })
                                    }
                                }
                                return {result: !1, message: "no surplus will right"}
                            }
                        }, {
                            key: "removeBullet", value: function (e) {
                                for (var t = this.channels, n = e.channel_id, r = void 0, a = n[0], i = n[0] + n[1]; a < i; a++) if (r = t[a]) {
                                    r.operating[e.mode] = !0;
                                    var o = -1;
                                    r.queue[e.mode].some(function (t, n) {
                                        return t.id === e.id && (o = n, !0)
                                    }), o > -1 && r.queue[e.mode].splice(o, 1), r.operating[e.mode] = !1
                                }
                            }
                        }, {
                            key: "resetArea", value: function () {
                                var e = this.danmu.container, t = this, n = e.getBoundingClientRect();
                                t.width = n.width, t.height = n.height, t.danmu.config.area && t.danmu.config.area.start >= 0 && t.danmu.config.area.end >= t.danmu.config.area.start && ("b2t" === t.direction ? t.width = t.width * (t.danmu.config.area.end - t.danmu.config.area.start) : t.height = t.height * (t.danmu.config.area.end - t.danmu.config.area.start)), t.container = e;
                                var r, a = /mobile/gi.test(navigator.userAgent) ? 10 : 12;
                                r = "b2t" === t.direction ? Math.floor(t.width / a) : Math.floor(t.height / a);
                                for (var i = [], o = 0; o < r; o++) i[o] = {
                                    id: o,
                                    queue: {scroll: [], top: [], bottom: []},
                                    operating: {scroll: !1, top: !1, bottom: !1},
                                    bookId: {}
                                };
                                if (t.channels && t.channels.length <= i.length) {
                                    for (var l = function (e) {
                                        i[e] = {
                                            id: e,
                                            queue: {scroll: [], top: [], bottom: []},
                                            operating: {scroll: !1, top: !1, bottom: !1},
                                            bookId: {}
                                        }, ["scroll", "top"].forEach(function (n) {
                                            t.channels[e].queue[n].forEach(function (r) {
                                                r.el && (i[e].queue[n].push(r), r.resized || (r.pauseMove(t.containerPos, !1), r.startMove(t.containerPos), r.resized = !0))
                                            })
                                        }), t.channels[e].queue.bottom.forEach(function (n) {
                                            if (n.el) {
                                                if (i[e + i.length - t.channels.length].queue.bottom.push(n), n.channel_id[0] + n.channel_id[1] - 1 === e) {
                                                    var r = [].concat(n.channel_id);
                                                    n.channel_id = [r[0] - t.channels.length + i.length, r[1]], n.top = n.channel_id[0] * a, t.danmu.config.area && t.danmu.config.area.start && (n.top += t.containerHeight * t.danmu.config.area.start), n.topInit()
                                                }
                                                n.resized || (n.pauseMove(t.containerPos, !1), n.startMove(t.containerPos), n.resized = !0)
                                            }
                                        })
                                    }, s = 0; s < t.channels.length; s++) l(s);
                                    for (var u = function (e) {
                                        ["scroll", "top", "bottom"].forEach(function (t) {
                                            i[e].queue[t].forEach(function (e) {
                                                e.resized = !1
                                            })
                                        })
                                    }, c = 0; c < i.length; c++) u(c);
                                    t.channels = i, "b2t" === t.direction ? t.channelWidth = a : t.channelHeight = a
                                } else if (t.channels && t.channels.length > i.length) {
                                    for (var p = function (e) {
                                        i[e] = {
                                            id: e,
                                            queue: {scroll: [], top: [], bottom: []},
                                            operating: {scroll: !1, top: !1, bottom: !1},
                                            bookId: {}
                                        }, ["scroll", "top", "bottom"].forEach(function (n) {
                                            if ("top" === n && e > Math.floor(i.length / 2)) ; else if ("bottom" === n && e <= Math.floor(i.length / 2)) ; else {
                                                var r = "bottom" === n ? e - i.length + t.channels.length : e;
                                                t.channels[r].queue[n].forEach(function (o, l) {
                                                    if (o.el) {
                                                        if (i[e].queue[n].push(o), "bottom" === n && o.channel_id[0] + o.channel_id[1] - 1 === r) {
                                                            var s = [].concat(o.channel_id);
                                                            o.channel_id = [s[0] - t.channels.length + i.length, s[1]], o.top = o.channel_id[0] * a, t.danmu.config.area && t.danmu.config.area.start && (o.top += t.containerHeight * t.danmu.config.area.start), o.topInit()
                                                        }
                                                        o.resized || (o.pauseMove(t.containerPos, !1), o.startMove(t.containerPos), o.resized = !0)
                                                    }
                                                    t.channels[r].queue[n].splice(l, 1)
                                                })
                                            }
                                        })
                                    }, d = 0; d < i.length; d++) p(d);
                                    for (var f = function (e) {
                                        ["scroll", "top", "bottom"].forEach(function (t) {
                                            i[e].queue[t].forEach(function (e) {
                                                e.resized = !1
                                            })
                                        })
                                    }, g = 0; g < i.length; g++) f(g);
                                    t.channels = i, "b2t" === t.direction ? t.channelWidth = a : t.channelHeight = a
                                }
                            }
                        }, {
                            key: "reset", value: function () {
                                var e = this.danmu.container, t = this;
                                t.channels && t.channels.length > 0 && ["scroll", "top", "bottom"].forEach(function (e) {
                                    for (var n = 0; n < t.channels.length; n++) t.channels[n].queue[e].forEach(function (e) {
                                        e.pauseMove(t.containerPos), e.remove()
                                    })
                                }), setTimeout(function () {
                                    var n = e.getBoundingClientRect();
                                    t.width = n.width, t.height = n.height, t.danmu.config.area && t.danmu.config.area.start >= 0 && t.danmu.config.area.end >= t.danmu.config.area.start && ("b2t" === t.direction ? t.width = t.width * (t.danmu.config.area.end - t.danmu.config.area.start) : t.height = t.height * (t.danmu.config.area.end - t.danmu.config.area.start)), t.container = e;
                                    var r, a = /mobile/gi.test(navigator.userAgent) ? 10 : 12;
                                    r = "b2t" === t.direction ? Math.floor(t.width / a) : Math.floor(t.height / a);
                                    for (var i = [], o = 0; o < r; o++) i[o] = {
                                        id: o,
                                        queue: {scroll: [], top: [], bottom: []},
                                        operating: {scroll: !1, top: !1, bottom: !1},
                                        bookId: {}
                                    };
                                    t.channels = i, "b2t" === t.direction ? t.channelWidth = a : t.channelHeight = a
                                }, 200)
                            }
                        }, {
                            key: "resetWithCb", value: function (e, t) {
                                var n = this.danmu.container, r = this;
                                r.channels && r.channels.length > 0 && ["scroll", "top", "bottom"].forEach(function (e) {
                                    for (var t = 0; t < r.channels.length; t++) r.channels[t].queue[e].forEach(function (e) {
                                        e.pauseMove(r.containerPos), e.remove()
                                    })
                                });
                                var a = n.getBoundingClientRect();
                                r.width = a.width, r.height = a.height, r.danmu.config.area && r.danmu.config.area.start >= 0 && r.danmu.config.area.end >= r.danmu.config.area.start && ("b2t" === r.direction ? r.width = r.width * (r.danmu.config.area.end - r.danmu.config.area.start) : r.height = r.height * (r.danmu.config.area.end - r.danmu.config.area.start)), r.container = n;
                                var i, o = /mobile/gi.test(navigator.userAgent) ? 10 : 12;
                                i = "b2t" === r.direction ? Math.floor(r.width / o) : Math.floor(r.height / o);
                                for (var l = [], s = 0; s < i; s++) l[s] = {
                                    id: s,
                                    queue: {scroll: [], top: [], bottom: []},
                                    operating: {scroll: !1, top: !1, bottom: !1},
                                    bookId: {}
                                };
                                r.channels = l, r.channelHeight = o, e && e(!0, t)
                            }
                        }]), e
                    }();
                    t.default = a, e.exports = t.default
                }, function (e, t, n) {
                    Object.defineProperty(t, "__esModule", {value: !0});
                    var r, a = function () {
                        function e(e, t) {
                            for (var n = 0; n < t.length; n++) {
                                var r = t[n];
                                r.enumerable = r.enumerable || !1, r.configurable = !0, "value" in r && (r.writable = !0), Object.defineProperty(e, r.key, r)
                            }
                        }

                        return function (t, n, r) {
                            return n && e(t.prototype, n), r && e(t, r), t
                        }
                    }(), i = (r = n(0)) && r.__esModule ? r : {default: r}, o = function () {
                        function e(t, n) {
                            !function (e, t) {
                                if (!(e instanceof t)) throw new TypeError("Cannot call a class as a function")
                            }(this, e), this.danmu = t, this.duration = n.duration, this.id = n.id, this.container = t.container, this.start = n.start, this.prior = n.prior, this.color = n.color, this.bookChannelId = n.bookChannelId, this.direction = t.direction;
                            var r = this;
                            this.danmu.on("changeDirection", function (e) {
                                r.direction = e
                            });
                            var a = void 0;
                            if (this.domObj = i.default.domObj, n.el && 1 === n.el.nodeType) (a = this.domObj.use()).appendChild(i.default.copyDom(n.el)); else if ((a = this.domObj.use()).textContent = n.txt, n.style) {
                                var o = n.style;
                                Object.keys(o).forEach(function (e) {
                                    a.style[e] = o[e]
                                })
                            }
                            "top" === n.mode || "bottom" === n.mode ? this.mode = n.mode : this.mode = "scroll", this.el = a, this.status = "waiting";
                            var l = this.container.getBoundingClientRect();
                            this.el.style.left = l.width + "px"
                        }

                        return a(e, [{
                            key: "attach", value: function () {
                                this.container.appendChild(this.el), this.elPos = this.el.getBoundingClientRect(), "b2t" === this.direction ? (this.width = this.elPos.height, this.height = this.elPos.width) : (this.width = this.elPos.width, this.height = this.elPos.height)
                            }
                        }, {
                            key: "detach", value: function () {
                                this.container && this.el && (this.domObj.unuse(this.el), this.container.removeChild(this.el));
                                var e = this;
                                this.danmu.off("changeDirection", function (t) {
                                    e.direction = t
                                }), this.el = null
                            }
                        }, {
                            key: "topInit", value: function () {
                                if ("b2t" === this.direction) {
                                    var e = this.container.getBoundingClientRect();
                                    this.el.style.transformOrigin = "left top", this.el.style.transform = "translateX(-" + this.top + "px) translateY(" + e.height + "px) translateZ(0px) rotate(90deg)", this.el.style.transition = "transform 0s linear 0s"
                                } else this.el.style.top = this.top + "px"
                            }
                        }, {
                            key: "pauseMove", value: function (e) {
                                var t = arguments.length > 1 && void 0 !== arguments[1] && arguments[1];
                                if ("paused" !== this.status && (this.status = "paused", clearTimeout(this.removeTimer), this.el)) if (this.el.style.willChange = "auto", "scroll" === this.mode) {
                                    if (t) {
                                        var n, r = ((new Date).getTime() - this.moveTime) / 1e3 * this.moveV;
                                        n = this.moveMoreS - r >= 0 ? "b2t" === this.direction ? (this.moveMoreS - r) / this.moveContainerHeight * e.height : (this.moveMoreS - r) / this.moveContainerWidth * e.width : this.moveMoreS - r, "b2t" === this.direction ? this.el.style.transform = "translateX(-" + this.top + "px) translateY(" + n + "px) translateZ(0px) rotate(90deg)" : this.el.style.left = n + "px"
                                    } else "b2t" === this.direction ? this.el.style.transform = "translateX(-" + this.top + "px) translateY(" + (this.el.getBoundingClientRect().top - e.top) + "px) translateZ(0px) rotate(90deg)" : this.el.style.left = this.el.getBoundingClientRect().left - e.left + "px";
                                    "b2t" === this.direction ? this.el.style.transition = "transform 0s linear 0s" : (this.el.style.transform = "translateX(0px) translateY(0px) translateZ(0px)", this.el.style.transition = "transform 0s linear 0s")
                                } else this.pastDuration && this.startTime ? this.pastDuration = this.pastDuration + (new Date).getTime() - this.startTime : this.pastDuration = 1
                            }
                        }, {
                            key: "startMove", value: function (e) {
                                var t = this;
                                if (this.el && "start" !== this.status) if (this.status = "start", this.el.style.willChange = "transform", "scroll" === this.mode) if ("b2t" === this.direction) {
                                    this.moveV = (e.height + this.height) / this.duration * 1e3;
                                    var n = (t.el.getBoundingClientRect().bottom - e.top) / this.moveV;
                                    this.el.style.transition = "transform " + n + "s linear 0s", setTimeout(function () {
                                        t.el && (t.el.style.transform = "translateX(-" + t.top + "px) translateY(-" + t.height + "px) translateZ(0px) rotate(90deg)", t.moveTime = (new Date).getTime(), t.moveMoreS = t.el.getBoundingClientRect().top - e.top, t.moveContainerHeight = e.height, t.removeTimer = setTimeout(i, 1e3 * n))
                                    }, 20)
                                } else {
                                    this.moveV = (e.width + this.width) / this.duration * 1e3;
                                    var r = (t.el.getBoundingClientRect().right - e.left) / this.moveV;
                                    this.el.style.transition = "transform " + r + "s linear 0s", setTimeout(function () {
                                        t.el && (t.el.style.transform = "translateX(-" + (t.el.getBoundingClientRect().right - e.left) + "px) translateY(0px) translateZ(0px)", t.moveTime = (new Date).getTime(), t.moveMoreS = t.el.getBoundingClientRect().left - e.left, t.moveContainerWidth = e.width, t.removeTimer = setTimeout(i, 1e3 * r))
                                    }, 20)
                                } else {
                                    this.el.style.left = "50%", this.el.style.margin = "0 0 0 -" + this.width / 2 + "px", this.pastDuration || (this.pastDuration = 1);
                                    var a = this.duration >= this.pastDuration ? this.duration - this.pastDuration : 0;
                                    this.removeTimer = setTimeout(i, a), this.startTime = (new Date).getTime()
                                }

                                function i() {
                                    if (t.el) if ("scroll" === t.mode) {
                                        var e = t.danmu.container.getBoundingClientRect(),
                                            n = t.el.getBoundingClientRect();
                                        "b2t" === t.direction ? n && n.bottom <= e.top + 100 ? (t.status = "end", t.remove()) : (t.pauseMove(e), t.startMove(e)) : n && n.right <= e.left + 100 ? (t.status = "end", t.remove()) : (t.pauseMove(e), t.startMove(e))
                                    } else t.status = "end", t.remove()
                                }
                            }
                        }, {
                            key: "remove", value: function () {
                                var e = this;
                                this.removeTimer && clearTimeout(this.removeTimer), e.el && e.el.parentNode && (e.el.style.willChange = "auto", this.danmu.off("changeDirection", function (t) {
                                    e.direction = t
                                }), this.domObj.unuse(e.el), e.el.parentNode.removeChild(e.el), e.el = null, e.danmu.emit("bullet_remove", {bullet: e}))
                            }
                        }]), e
                    }();
                    t.default = o, e.exports = t.default
                }, function (e, t, n) {
                    var r = n(27);
                    "string" == typeof r && (r = [[e.i, r, ""]]);
                    n(29)(r, {hmr: !0, transform: void 0, insertInto: void 0}), r.locals && (e.exports = r.locals)
                }, function (e, t, n) {
                    (e.exports = n(28)(!1)).push([e.i, ".danmu{overflow:hidden;-webkit-user-select:none;-moz-user-select:none;user-select:none;-ms-user-select:none}.danmu>*{position:absolute;white-space:nowrap}.danmu-switch{width:32px;height:20px;border-radius:100px;background-color:#ccc;-webkit-box-sizing:border-box;box-sizing:border-box;outline:none;cursor:pointer;position:relative;text-align:center;margin:10px auto}.danmu-switch.danmu-switch-active{padding-left:12px;background-color:#f85959}.danmu-switch span.txt{width:20px;height:20px;line-height:20px;text-align:center;display:block;border-radius:100px;background-color:#ffffff;-webkit-box-shadow:-2px 0 0 0 rgba(0, 0, 0, .04);box-shadow:-2px 0 0 0 rgba(0, 0, 0, .04);font-family:PingFangSC;font-size:10px;font-weight:500;color:#f44336}\n", ""])
                }, function (e, t) {
                    e.exports = function (e) {
                        var t = [];
                        return t.toString = function () {
                            return this.map(function (t) {
                                var n = function (e, t) {
                                    var n, r = e[1] || "", a = e[3];
                                    if (!a) return r;
                                    if (t && "function" == typeof btoa) {
                                        var i = (n = a, "/*# sourceMappingURL=data:application/json;charset=utf-8;base64," + btoa(unescape(encodeURIComponent(JSON.stringify(n)))) + " */"),
                                            o = a.sources.map(function (e) {
                                                return "/*# sourceURL=" + a.sourceRoot + e + " */"
                                            });
                                        return [r].concat(o).concat([i]).join("\n")
                                    }
                                    return [r].join("\n")
                                }(t, e);
                                return t[2] ? "@media " + t[2] + "{" + n + "}" : n
                            }).join("")
                        }, t.i = function (e, n) {
                            "string" == typeof e && (e = [[null, e, ""]]);
                            for (var r = {}, a = 0; a < this.length; a++) {
                                var i = this[a][0];
                                "number" == typeof i && (r[i] = !0)
                            }
                            for (a = 0; a < e.length; a++) {
                                var o = e[a];
                                "number" == typeof o[0] && r[o[0]] || (n && !o[2] ? o[2] = n : n && (o[2] = "(" + o[2] + ") and (" + n + ")"), t.push(o))
                            }
                        }, t
                    }
                }, function (e, t, n) {
                    var r, a, i = {}, l = (r = function () {
                        return window && document && document.all && !window.atob
                    }, function () {
                        return void 0 === a && (a = r.apply(this, arguments)), a
                    }), s = function (e) {
                        var t = {};
                        return function (e) {
                            if ("function" == typeof e) return e();
                            if (void 0 === t[e]) {
                                var n = function (e) {
                                    return document.querySelector(e)
                                }.call(this, e);
                                if (window.HTMLIFrameElement && n instanceof window.HTMLIFrameElement) try {
                                    n = n.contentDocument.head
                                } catch (e) {
                                    n = null
                                }
                                t[e] = n
                            }
                            return t[e]
                        }
                    }(), u = null, c = 0, p = [], d = n(30);

                    function f(e, t) {
                        for (var n = 0; n < e.length; n++) {
                            var r = e[n], a = i[r.id];
                            if (a) {
                                a.refs++;
                                for (var o = 0; o < a.parts.length; o++) a.parts[o](r.parts[o]);
                                for (; o < r.parts.length; o++) a.parts.push(v(r.parts[o], t))
                            } else {
                                var l = [];
                                for (o = 0; o < r.parts.length; o++) l.push(v(r.parts[o], t));
                                i[r.id] = {id: r.id, refs: 1, parts: l}
                            }
                        }
                    }

                    function g(e, t) {
                        for (var n = [], r = {}, a = 0; a < e.length; a++) {
                            var i = e[a], o = t.base ? i[0] + t.base : i[0],
                                l = {css: i[1], media: i[2], sourceMap: i[3]};
                            r[o] ? r[o].parts.push(l) : n.push(r[o] = {id: o, parts: [l]})
                        }
                        return n
                    }

                    function h(e, t) {
                        var n = s(e.insertInto);
                        if (!n) throw new Error("Couldn't find a style target. This probably means that the value for the 'insertInto' parameter is invalid.");
                        var r = p[p.length - 1];
                        if ("top" === e.insertAt) r ? r.nextSibling ? n.insertBefore(t, r.nextSibling) : n.appendChild(t) : n.insertBefore(t, n.firstChild), p.push(t); else if ("bottom" === e.insertAt) n.appendChild(t); else {
                            if ("object" != o(e.insertAt) || !e.insertAt.before) throw new Error("[Style Loader]\n\n Invalid value for parameter 'insertAt' ('options.insertAt') found.\n Must be 'top', 'bottom', or Object.\n (https://github.com/webpack-contrib/style-loader#insertat)\n");
                            var a = s(e.insertInto + " " + e.insertAt.before);
                            n.insertBefore(t, a)
                        }
                    }

                    function y(e) {
                        if (null === e.parentNode) return !1;
                        e.parentNode.removeChild(e);
                        var t = p.indexOf(e);
                        t >= 0 && p.splice(t, 1)
                    }

                    function x(e) {
                        var t = document.createElement("style");
                        return void 0 === e.attrs.type && (e.attrs.type = "text/css"), m(t, e.attrs), h(e, t), t
                    }

                    function m(e, t) {
                        Object.keys(t).forEach(function (n) {
                            e.setAttribute(n, t[n])
                        })
                    }

                    function v(e, t) {
                        var n, r, a, i;
                        if (t.transform && e.css) {
                            if (!(i = t.transform(e.css))) return function () {
                            };
                            e.css = i
                        }
                        if (t.singleton) {
                            var o = c++;
                            n = u || (u = x(t)), r = w.bind(null, n, o, !1), a = w.bind(null, n, o, !0)
                        } else e.sourceMap && "function" == typeof URL && "function" == typeof URL.createObjectURL && "function" == typeof URL.revokeObjectURL && "function" == typeof Blob && "function" == typeof btoa ? (n = function (e) {
                            var t = document.createElement("link");
                            return void 0 === e.attrs.type && (e.attrs.type = "text/css"), e.attrs.rel = "stylesheet", m(t, e.attrs), h(e, t), t
                        }(t), r = function (e, t, n) {
                            var r = n.css, a = n.sourceMap, i = void 0 === t.convertToAbsoluteUrls && a;
                            (t.convertToAbsoluteUrls || i) && (r = d(r)), a && (r += "\n/*# sourceMappingURL=data:application/json;base64," + btoa(unescape(encodeURIComponent(JSON.stringify(a)))) + " */");
                            var o = new Blob([r], {type: "text/css"}), l = e.href;
                            e.href = URL.createObjectURL(o), l && URL.revokeObjectURL(l)
                        }.bind(null, n, t), a = function () {
                            y(n), n.href && URL.revokeObjectURL(n.href)
                        }) : (n = x(t), r = function (e, t) {
                            var n = t.css, r = t.media;
                            if (r && e.setAttribute("media", r), e.styleSheet) e.styleSheet.cssText = n; else {
                                for (; e.firstChild;) e.removeChild(e.firstChild);
                                e.appendChild(document.createTextNode(n))
                            }
                        }.bind(null, n), a = function () {
                            y(n)
                        });
                        return r(e), function (t) {
                            if (t) {
                                if (t.css === e.css && t.media === e.media && t.sourceMap === e.sourceMap) return;
                                r(e = t)
                            } else a()
                        }
                    }

                    e.exports = function (e, t) {
                        if ("undefined" != typeof DEBUG && DEBUG && "object" != ("undefined" == typeof document ? "undefined" : o(document))) throw new Error("The style-loader cannot be used in a non-browser environment");
                        (t = t || {}).attrs = "object" == o(t.attrs) ? t.attrs : {}, t.singleton || "boolean" == typeof t.singleton || (t.singleton = l()), t.insertInto || (t.insertInto = "head"), t.insertAt || (t.insertAt = "bottom");
                        var n = g(e, t);
                        return f(n, t), function (e) {
                            for (var r = [], a = 0; a < n.length; a++) {
                                var o = n[a];
                                (l = i[o.id]).refs--, r.push(l)
                            }
                            for (e && f(g(e, t), t), a = 0; a < r.length; a++) {
                                var l;
                                if (0 === (l = r[a]).refs) {
                                    for (var s = 0; s < l.parts.length; s++) l.parts[s]();
                                    delete i[l.id]
                                }
                            }
                        }
                    };
                    var b, k = (b = [], function (e, t) {
                        return b[e] = t, b.filter(Boolean).join("\n")
                    });

                    function w(e, t, n, r) {
                        var a = n ? "" : r.css;
                        if (e.styleSheet) e.styleSheet.cssText = k(t, a); else {
                            var i = document.createTextNode(a), o = e.childNodes;
                            o[t] && e.removeChild(o[t]), o.length ? e.insertBefore(i, o[t]) : e.appendChild(i)
                        }
                    }
                }, function (e, t) {
                    e.exports = function (e) {
                        var t = "undefined" != typeof window && window.location;
                        if (!t) throw new Error("fixUrls requires window.location");
                        if (!e || "string" != typeof e) return e;
                        var n = t.protocol + "//" + t.host, r = n + t.pathname.replace(/\/[^\/]*$/, "/");
                        return e.replace(/url\s*\(((?:[^)(]|\((?:[^)(]+|\([^)(]*\))*\))*)\)/gi, function (e, t) {
                            var a, i = t.trim().replace(/^"(.*)"$/, function (e, t) {
                                return t
                            }).replace(/^'(.*)'$/, function (e, t) {
                                return t
                            });
                            return /^(#|data:|http:\/\/|https:\/\/|file:\/\/\/|\s*$)/i.test(i) ? e : (a = 0 === i.indexOf("//") ? i : 0 === i.indexOf("/") ? n + i : r + i.replace(/^\.\//, ""), "url(" + JSON.stringify(a) + ")")
                        })
                    }
                }])
            }, "object" == o(t) && "object" == o(e) ? e.exports = i() : (r = [], void 0 === (a = "function" == typeof(n = i) ? n.apply(t, r) : n) || (e.exports = a))
        }).call(this, n(90)(e))
    }, function (e, t, n) {
        "use strict";
        e.exports = function (e) {
            return e.webpackPolyfill || (e.deprecate = function () {
            }, e.paths = [], e.children || (e.children = []), Object.defineProperty(e, "loaded", {
                enumerable: !0,
                get: function () {
                    return e.l
                }
            }), Object.defineProperty(e, "id", {
                enumerable: !0, get: function () {
                    return e.i
                }
            }), e.webpackPolyfill = 1), e
        }
    }, function (e, t, n) {
        "use strict";
        n.r(t), t.default = '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 40 40" width="40" height="40">\n  <path fill="#f85959" transform="scale(0.8 0.8)" d="M36.5,18.73a1.19,1.19,0,0,0,1-1.14V16.33a1.2,1.2,0,0,0-1-1.13l-.61-.08a1.75,1.75,0,0,1-1.3-.86l-.21-.36-.2-.36A1.72,1.72,0,0,1,34,12l.23-.58a1.18,1.18,0,0,0-.5-1.42l-1.1-.62a1.18,1.18,0,0,0-1.47.3l-.39.51a1.82,1.82,0,0,1-1.41.72c-.44,0-1.88-.27-2.22-.7l-.39-.49a1.18,1.18,0,0,0-1.48-.28l-1.09.64a1.19,1.19,0,0,0-.47,1.43l.25.59a1.87,1.87,0,0,1-.08,1.58c-.26.37-1.17,1.5-1.71,1.58l-.63.09a1.19,1.19,0,0,0-1,1.14l0,1.27a1.17,1.17,0,0,0,1,1.12l.61.08a1.74,1.74,0,0,1,1.3.87l.21.36.2.35A1.69,1.69,0,0,1,24,22.08l-.23.59a1.19,1.19,0,0,0,.5,1.42l1.1.62a1.19,1.19,0,0,0,1.48-.31l.38-.5a1.83,1.83,0,0,1,1.41-.72c.44,0,1.88.25,2.22.69l.39.49a1.18,1.18,0,0,0,1.48.28L33.86,24a1.19,1.19,0,0,0,.47-1.43L34.09,22a1.84,1.84,0,0,1,.07-1.58c.26-.37,1.17-1.5,1.72-1.58ZM31,18.94a2.76,2.76,0,0,1-4.65-1.2A2.71,2.71,0,0,1,27,15.13a2.76,2.76,0,0,1,4.64,1.2A2.7,2.7,0,0,1,31,18.94Z"/>\n  <path fill="#f85959" transform="scale(0.8 0.8)" d="M32,0H3.59A3.59,3.59,0,0,0,0,3.59v17A3.59,3.59,0,0,0,3.59,24.2H19.72a12.59,12.59,0,0,1-.81-1.2A11.73,11.73,0,0,1,35.54,7.28V3.59A3.59,3.59,0,0,0,32,0ZM13,14.18H4.29a1.52,1.52,0,0,1,0-3H13a1.52,1.52,0,0,1,0,3ZM16.45,8H4.29a1.51,1.51,0,0,1,0-3H16.45a1.51,1.51,0,1,1,0,3Z"/>\n</svg>\n'
    }, function (e, t, n) {
        "use strict";
        var r, a = n(0), i = (r = a) && r.__esModule ? r : {default: r};
        i.default.install("s_pip", function () {
            var e = this, t = i.default.util;
            if (e.config.pip) {
                var n = e.lang.PIP,
                    r = t.createDom("xg-pip", '<p class="name"><span>' + n + "</span></p>", {tabindex: 9}, "xgplayer-pip");
                e.once("ready", function () {
                    e.controls.appendChild(r)
                }), ["click", "touchend"].forEach(function (t) {
                    r.addEventListener(t, function (t) {
                        t.preventDefault(), t.stopPropagation(), e.emit("pipBtnClick")
                    })
                })
            }
        })
    }, function (e, t, n) {
        "use strict";
        var r = i(n(0)), a = i(n(94));

        function i(e) {
            return e && e.__esModule ? e : {default: e}
        }

        r.default.install("s_playNext", function () {
            var e = this, t = r.default.util, n = e.config.playNext;
            if (n && n.urlList) {
                var i = t.createDom("xg-playnext", '<xg-icon class="xgplayer-icon">' + a.default + "</xg-icon>", {}, "xgplayer-playnext"),
                    o = e.lang.PLAYNEXT_TIPS,
                    l = t.createDom("xg-tips", '<span class="xgplayer-tip-playnext">' + o + "</span>", {}, "xgplayer-tips");
                i.appendChild(l), e.once("ready", function () {
                    e.controls.appendChild(i)
                }), ["click", "touchend"].forEach(function (t) {
                    i.addEventListener(t, function (t) {
                        t.preventDefault(), t.stopPropagation(), e.emit("playNextBtnClick")
                    })
                })
            }
        })
    }, function (e, t, n) {
        "use strict";
        n.r(t), t.default = '<svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 40 40">\n  <path transform="scale(0.038 0.028)" d="M800 380v768h-128v-352l-320 320v-704l320 320v-352z"></path>\n</svg>\n'
    }, function (e, t, n) {
        "use strict";
        var r = i(n(0)), a = i(n(96));

        function i(e) {
            return e && e.__esModule ? e : {default: e}
        }

        r.default.install("s_rotate", function () {
            var e = this, t = r.default.util;
            if (e.config.rotate) {
                var n = t.createDom("xg-rotate", '<xg-icon class="xgplayer-icon">' + a.default + "</xg-icon>", {}, "xgplayer-rotate"),
                    i = e.lang.ROTATE_TIPS,
                    o = t.createDom("xg-tips", '<span class="xgplayer-tip-rotate">' + i + "</span>", {}, "xgplayer-tips");
                n.appendChild(o), e.once("ready", function () {
                    e.controls.appendChild(n)
                }), ["click", "touchend"].forEach(function (t) {
                    n.addEventListener(t, function (t) {
                        t.preventDefault(), t.stopPropagation(), e.emit("rotateBtnClick")
                    })
                })
            }
        })
    }, function (e, t, n) {
        "use strict";
        n.r(t), t.default = '<svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 40 40" fill="none">\n  <g clip-path="url(#clip0)">\n    <path transform="scale(1.5 1.5)" d="M11.6665 9.16663H4.1665C2.78579 9.16663 1.6665 10.2859 1.6665 11.6666V15.8333C1.6665 17.214 2.78579 18.3333 4.1665 18.3333H11.6665C13.0472 18.3333 14.1665 17.214 14.1665 15.8333V11.6666C14.1665 10.2859 13.0472 9.16663 11.6665 9.16663Z" fill="white"/>\n    <path transform="scale(1.5 1.5)" fill-rule="evenodd" clip-rule="evenodd" d="M3.88148 4.06298C3.75371 4.21005 3.67667 4.40231 3.67749 4.61242C3.67847 4.87253 3.79852 5.10435 3.98581 5.25646L6.99111 8.05895C7.32771 8.37283 7.85502 8.35443 8.16891 8.01782C8.48279 7.68122 8.46437 7.15391 8.12778 6.84003L6.62061 5.43457L9.8198 5.4224C9.82848 5.42239 9.8372 5.42221 9.84591 5.4219C10.9714 5.38233 12.0885 5.6285 13.0931 6.13744C14.0976 6.64635 14.957 7.40148 15.5908 8.33234C16.2246 9.2632 16.6122 10.3394 16.7177 11.4606C16.823 12.5819 16.6427 13.7115 16.1934 14.7442C16.0098 15.1661 16.203 15.6571 16.6251 15.8408C17.0471 16.0243 17.5381 15.8311 17.7216 15.4091C18.2833 14.1183 18.5087 12.7063 18.3771 11.3047C18.2453 9.90318 17.7607 8.55792 16.9684 7.39433C16.1761 6.23073 15.1021 5.28683 13.8463 4.65065C12.5946 4.01651 11.203 3.70872 9.80072 3.75583L6.43415 3.76862L7.96326 2.12885C8.27715 1.79225 8.25872 1.26494 7.92213 0.951061C7.58553 0.63718 7.05822 0.655585 6.74433 0.99219L3.90268 4.0395C3.89545 4.04724 3.88841 4.05509 3.88154 4.06303L3.88148 4.06298Z" fill="white"/>\n  </g>\n  <defs>\n    <clipPath id="clip0">\n      <rect width="40" height="40" fill="white"/>\n    </clipPath>\n  </defs>\n</svg>\n'
    }, function (e, t, n) {
        "use strict";
        var r, a = n(0), i = (r = a) && r.__esModule ? r : {default: r};
        i.default.install("s_screenShot", function () {
            var e = this, t = i.default.util;
            if (e.config.screenShot) {
                var n = e.lang.SCREENSHOT,
                    r = t.createDom("xg-screenshot", '<p class="name"><span>' + n + "</span></p>", {tabindex: 11}, "xgplayer-screenshot");
                e.once("ready", function () {
                    e.controls.appendChild(r)
                }), ["click", "touchend"].forEach(function (t) {
                    r.addEventListener(t, function (t) {
                        t.preventDefault(), t.stopPropagation(), e.emit("screenShotBtnClick")
                    })
                })
            }
        })
    }, function (e, t, n) {
        "use strict";
        var r, a = n(0), i = (r = a) && r.__esModule ? r : {default: r};
        i.default.install("s_textTrack", function () {
            if (!(navigator.userAgent.indexOf("Chrome") < 0)) {
                var e = this, t = e.root, n = i.default.util,
                    r = (i.default.sniffer, e.controls, n.createDom("xg-texttrack", "", {tabindex: 7}, "xgplayer-texttrack")),
                    a = e.config.textTrack;
                a && Array.isArray(a) && a.length > 1 && (n.addClass(e.root, "xgplayer-is-texttrack"), e.on("canplay", function () {
                    var i = ["<ul>"];
                    i.push("<li class=''}'>关闭</li>"), a.forEach(function (e) {
                        i.push("<li class='" + (e.default ? "selected" : "") + "'>" + e.label + "</li>")
                    });
                    var o = e.lang.TEXTTRACK;
                    i.push('</ul><p class="name"><em>' + o + "</em></p>");
                    var l = t.querySelector(".xgplayer-texttrack");
                    if (l) {
                        l.innerHTML = i.join("");
                        var s = l.querySelector(".name");
                        e.config.textTrackActive && "hover" !== e.config.textTrackActive || s.addEventListener("mouseenter", function (e) {
                            e.preventDefault(), e.stopPropagation(), n.addClass(t, "xgplayer-texttrack-active"), l.focus()
                        })
                    } else {
                        r.innerHTML = i.join("");
                        var u = r.querySelector(".name");
                        e.config.textTrackActive && "hover" !== e.config.textTrackActive || u.addEventListener("mouseenter", function (t) {
                            t.preventDefault(), t.stopPropagation(), n.addClass(e.root, "xgplayer-texttrack-active"), r.focus()
                        }), e.controls.appendChild(r)
                    }
                })), ["touchend", "click"].forEach(function (t) {
                    r.addEventListener(t, function (t) {
                        t.preventDefault(), t.stopPropagation();
                        var i = t.target || t.srcElement;
                        if (i && "li" === i.tagName.toLocaleLowerCase()) {
                            Array.prototype.forEach.call(i.parentNode.childNodes, function (e) {
                                n.removeClass(e, "selected")
                            }), n.addClass(i, "selected");
                            var o = e.root.getElementsByTagName("Track");
                            "关闭" === i.innerHTML ? (o[0].track.mode = "hidden", n.removeClass(e.root, "xgplayer-texttrack-active")) : (n.hasClass(e.root, "xgplayer-texttrack-active") || n.addClass(e.root, "xgplayer-texttrack-active"), o[0].track.mode = "showing", a.some(function (e) {
                                if (e.label === i.innerHTML) return o[0].src = e.src, e.kind && (o[0].kind = e.kind), o[0].label = e.label, e.srclang && (o[0].srclang = e.srclang), !0
                            }), e.emit("textTrackChange", i.innerHTML))
                        } else "click" !== e.config.textTrackActive || !i || "p" !== i.tagName.toLocaleLowerCase() && "em" !== i.tagName.toLocaleLowerCase() || (n.addClass(e.root, "xgplayer-texttrack-active"), r.focus())
                    }, !1)
                }), r.addEventListener("mouseleave", function (t) {
                    t.preventDefault(), t.stopPropagation(), n.removeClass(e.root, "xgplayer-texttrack-active")
                })
            }
        })
    }, function (e, t, n) {
        "use strict";
        var r, a = n(0), i = (r = a) && r.__esModule ? r : {default: r};
        i.default.install("s_error", function () {
            var e = this, t = e.root, n = i.default.util,
                r = n.createDom("xg-error", '<em class="xgplayer-error-text">请<span class="xgplayer-error-refresh">刷新</span>试试</em>', {}, "xgplayer-error");
            t.appendChild(r);
            var a = r.querySelector(".xgplayer-error-text"), o = null;

            function l() {
                e.error ? a.innerHTML = e.error : e.config.lang && "zh-cn" === e.config.lang ? a.innerHTML = e.lang.ERROR + '，请<span class="xgplayer-error-refresh">刷新</span>试试' : a.innerHTML = e.lang.ERROR + '，please try to <span class="xgplayer-error-refresh">refresh</span>', n.addClass(e.root, "xgplayer-is-error"), (o = r.querySelector(".xgplayer-error-refresh")) && ["touchend", "click"].forEach(function (t) {
                    o.addEventListener(t, function (t) {
                        t.preventDefault(), t.stopPropagation();
                        var n = t.target || t.srcElement;
                        n && "span" === n.tagName.toLocaleLowerCase() && (e.controls.style.display = "flex", e.reload())
                    })
                })
            }

            e.on("error", l), e.once("destroy", function t() {
                e.off("error", l), e.off("destroy", t)
            })
        })
    }]);
    //# sourceMappingURL=index.js.map

    exports('Player', Player);
});


