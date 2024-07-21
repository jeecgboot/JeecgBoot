var myEchartsTheme = {
    color: ["#009688", "#1E9FFF", "#5FB878", "#FFB980", "#D87A80", "#8d98b3", "#e5cf0d", "#97b552", "#95706d", "#dc69aa", "#07a2a4", "#9a7fd1", "#588dd5", "#f5994e", "#c05050", "#59678c", "#c9ab00", "#7eb00a", "#6f5553", "#c14089"],
    title: {textStyle: {fontWeight: "normal", color: "#666"}},
    dataRange: {itemWidth: 15, color: ["#009688", "#e0ffff"]},
    toolbox: {color: ["#1e90ff", "#1e90ff", "#1e90ff", "#1e90ff"], effectiveColor: "#ff4500"},
    tooltip: {
        backgroundColor: "rgba(50,50,50,0.5)",
        axisPointer: {
            type: "line",
            lineStyle: {color: "#009688"},
            crossStyle: {color: "#008acd"},
            shadowStyle: {color: "rgba(200,200,200,0.2)"}
        }
    },
    dataZoom: {dataBackgroundColor: "#efefff", fillerColor: "rgba(182,162,222,0.2)", handleColor: "#008acd"},
    grid: {borderColor: "#eee"},
    categoryAxis: {
        axisLine: {lineStyle: {color: "#009688"}},
        axisTick: {show: !1},
        splitLine: {lineStyle: {color: ["#eee"]}}
    },
    valueAxis: {
        axisLine: {lineStyle: {color: "#009688"}},
        splitArea: {show: !0, areaStyle: {color: ["rgba(250,250,250,0.1)", "rgba(200,200,200,0.1)"]}},
        splitLine: {lineStyle: {color: ["#eee"]}}
    },
    polar: {
        axisLine: {lineStyle: {color: "#ddd"}},
        splitArea: {show: !0, areaStyle: {color: ["rgba(250,250,250,0.2)", "rgba(200,200,200,0.2)"]}},
        splitLine: {lineStyle: {color: "#ddd"}}
    },
    timeline: {
        lineStyle: {color: "#009688"},
        controlStyle: {normal: {color: "#009688"}, emphasis: {color: "#009688"}},
        symbol: "emptyCircle",
        symbolSize: 3
    },
    bar: {itemStyle: {normal: {barBorderRadius: 2}, emphasis: {barBorderRadius: 2}}},
    line: {smooth: !0, symbol: "emptyCircle", symbolSize: 3},
    k: {
        itemStyle: {
            normal: {
                color: "#d87a80",
                color0: "#2ec7c9",
                lineStyle: {color: "#d87a80", color0: "#2ec7c9"}
            }
        }
    },
    scatter: {symbol: "circle", symbolSize: 4},
    radar: {symbol: "emptyCircle", symbolSize: 3},
    map: {
        itemStyle: {
            normal: {areaStyle: {color: "#ddd"}, label: {textStyle: {color: "#d87a80"}}},
            emphasis: {areaStyle: {color: "#fe994e"}}
        }
    },
    force: {itemStyle: {normal: {linkStyle: {color: "#1e90ff"}}}},
    chord: {
        itemStyle: {
            normal: {
                borderWidth: 1,
                borderColor: "rgba(128, 128, 128, 0.5)",
                chordStyle: {lineStyle: {color: "rgba(128, 128, 128, 0.5)"}}
            },
            emphasis: {
                borderWidth: 1,
                borderColor: "rgba(128, 128, 128, 0.5)",
                chordStyle: {lineStyle: {color: "rgba(128, 128, 128, 0.5)"}}
            }
        }
    },
    gauge: {
        axisLine: {lineStyle: {color: [[.2, "#2ec7c9"], [.8, "#5ab1ef"], [1, "#d87a80"]], width: 10}},
        axisTick: {splitNumber: 10, length: 15, lineStyle: {color: "auto"}},
        splitLine: {length: 22, lineStyle: {color: "auto"}},
        pointer: {width: 5}
    },
    textStyle: {fontFamily: "微软雅黑, Arial, Verdana, sans-serif"}
};