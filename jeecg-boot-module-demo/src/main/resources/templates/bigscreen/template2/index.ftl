<!DOCTYPE html>
<#assign base=springMacroRequestContext.getContextUrl("")>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="renderer" content="webkit">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,user-scalable=no">

    <script type="text/javascript" src="${base}/bigscreen/template2/js/rem.js"></script>
    <link rel="stylesheet" href="${base}/bigscreen/template2/css/style.css">
    <title>智慧物流服务中心-首页</title>
</head>

<body style="visibility: hidden;">
<div class="container-flex" tabindex="0" hidefocus="true">
    <div class="box-left">
        <div class="left-top">
            <div class="current-num">
                <div>当前到件量</div>
                <p>123,456,789</p>
            </div>
        </div>
        <div class="left-center">
            <div class="title-box">
                <h6>派件入库量占比</h6>
            </div>
            <div class="chart-box pie-chart">
                <div id="pie"></div>
                <div>
                    <div class="pie-data">

                    </div>
                </div>
            </div>
        </div>
        <div class="left-bottom" class="select">
            <div class="title-box">
                <h6>广东省寄派件数据</h6>
                <img class="line-img" src="${base}/bigscreen/template2/images/line-blue.png" alt="">
                <button id="filBtn"><img src="${base}/bigscreen/template2/images/select_icon.png" alt="">筛选</button>
            </div>
            <div class="chart-box">
                <div class="filter-con" id="filCon" data-type="1">
                    <div class="select" tabindex="0" hidefocus="true">
                        <div class="select-div">
                            派件
                        </div>
                        <ul class="select-ul">
                            <li class="active" data-value="1">派件</li>
                            <li data-value="2">寄件</li>
                        </ul>
                    </div>
                    <div class="select" tabindex="0" hidefocus="true">
                        <div class="select-div">
                            公司
                        </div>
                        <ul class="select-ul company">
                            <li class="active" data-value="">公司</li>
                            <li data-value="1">顺丰</li>
                            <li data-value="2">京东</li>
                            <li data-value="2">EMS</li>
                        </ul>
                    </div>
                    <div class="select" tabindex="0" hidefocus="true">
                        <div class="select-div">
                            快件类型
                        </div>
                        <ul class="select-ul">
                            <li class="active" data-value="">快件类型</li>
                            <li data-value="0">文件</li>
                            <li data-value="1">物品</li>
                        </ul>
                    </div>
                </div>
                <div id="gdMap" class="gd-map"></div>
            </div>
        </div>
    </div>
    <div class="box-center">
        <div class="center-top">
            <h1>智慧物流服务中心</h1>
        </div>
        <div class="center-center">
            <div class="weather-box">
                <div class="data">
                    <p class="time" id="time">00:00:00</p>
                    <p id="date"></p>
                </div>
                <div class="weather">
                    <img id="weatherImg" src="${base}/bigscreen/template2/images/weather/weather_img01.png" alt="">
                    <div id="weather">
                        <p class="active">多云</p>
                        <p>16-22℃</p>
                        <p>深圳市南山区</p>
                    </div>
                </div>
            </div>
            <img src="${base}/bigscreen/template2/images/line_bg.png" alt="">
            <div class="select-box">
                <ul id="barType">
                    <li class="active" data-value="1">派件</li>
                    <li data-value="2">寄件</li>
                </ul>
                <div data-type="2">
                    <div class="select" tabindex="0" hidefocus="true">
                        <div class="select-div">
                            公司
                        </div>
                        <ul class="select-ul company">
                            <li class="active" data-value="">公司</li>
                            <li data-value="1">顺丰</li>
                            <li data-value="2">京东</li>
                            <li data-value="2">EMS</li>
                        </ul>
                    </div>
                    <div class="select" tabindex="0" hidefocus="true">
                        <div class="select-div">
                            快件类型
                        </div>
                        <ul class="select-ul">
                            <li class="active" data-value="">快件类型</li>
                            <li data-value="0">文件</li>
                            <li data-value="1">物品</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
        <div class="center-bottom">
            <div class="chart-box">
                <div id="chart4" style="width:100%;height:95%;"></div>
            </div>
            <div class="city-data">
                <div class="city-box">
                    <p id="titleQ"><span>全网</span>到珠海</p>
                    <ul class="city-btn" data-city="1">
                        <li class="active">全网</li>
                        <li>ABCDE</li>
                        <li>FGHIJ</li>
                        <li>KLMNO</li>
                        <li>PQRST</li>
                        <li>UVWXYZ</li>
                    </ul>
                    <ul class="city-div" id="city">

                    </ul>
                </div>
                <ul class="ranking-box">
                    <li><span></span>
                        <p>城市</p>
                        <p>派件</p>
                    </li>
                    <!--                        <li><span>1</span><p>上海</p><p>1sss25(万件)</p></li>-->
                </ul>
                <div class="enlarge-box">
                    <button class="enlarge-btn" id="fangda"></button>
                    <ul class="modal-btn">
                        <li>
                            <div></div>1</li>
                        <li>
                            <div></div>2</li>
                        <li>
                            <div></div>3</li>
                        <li>
                            <div></div>4</li>
                        <li>
                            <div></div>5</li>
                        <li>
                            <div></div>6</li>
                    </ul>
                </div>
            </div>
        </div>

    </div>
    <div class="box-right">
        <div class="right-top">
            <div class="title-box">
                <h6 id="barTitle">派件数据</h6>
                <img class="line-img" src="${base}/bigscreen/template2/images/line-blue.png" alt="">
                <button data-state=1 id="tabBtn"><img src="${base}/bigscreen/template2/images/chart_icon.png" alt=""><span>图表</span></button>
            </div>
            <p class="unit">单位：件</p>
            <div class="chart-box">
                <div id="chart3" style="width:100%;height:100%;"></div>
            </div>
            <div class="data-box" style="display:none;">
                <table class="table1">
                    <tr>
                        <td>入库件</td>
                        <td colspan="3" class="table-data dph-data1">0</td>
                    </tr>
                    <tr class="bg-color">
                        <td rowspan="2">在库件</td>
                        <td rowspan="2" class="table-data dph-data2">0</td>
                        <td>正常件</td>
                        <td class="table-data dph-data3">0</td>
                    </tr>
                    <tr class="bg-color">
                        <td>滞留件</td>
                        <td class="table-data dph-data5">0</td>
                    </tr>
                    <tr>
                        <td rowspan="2">出库件</td>
                        <td rowspan="2" class="dph-data6">0</td>
                        <td>派送件</td>
                        <td class="table-data dph-data7">0</td>
                    </tr>
                    <tr>
                        <td>自提件</td>
                        <td class="table-data dph-data8">0</td>
                    </tr>
                    <tr class="bg-color">
                        <td>退签件</td>
                        <td colspan="3" class="table-data dph-data9">0</td>
                    </tr>
                    <tr>
                        <td>丢失件</td>
                        <td colspan="3" class="table-data dph-data4">0</td>
                    </tr>
                </table>
                <table class="table1" style="display:none;">
                    <tr>
                        <td>入库件</td>
                        <td colspan="3" class="table-data mail-data1">1</td>
                    </tr>
                    <tr class="bg-color">
                        <td rowspan="2">在库件</td>
                        <td rowspan="2" class="table-data mail-data2">1</td>
                        <td>正常件</td>
                        <td class="table-data mail-data7">1</td>
                    </tr>
                    <tr class="bg-color">
                        <td>滞留件</td>
                        <td class="table-data mail-data4">1</td>
                    </tr>

                    <tr>
                        <td>出库件</td>
                        <td colspan="3" class="mail-data6">1</td>
                    </tr>
                    <tr class="bg-color">
                        <td>丢失件</td>
                        <td colspan="3" class="mail-data3">1</td>
                    </tr>
                    <tr>
                        <td>撤销件</td>
                        <td colspan="3" class="table-data mail-data5">1</td>
                    </tr>
                </table>
            </div>
        </div>
        <div class="right-center">
            <div class="title-box">
                <p id="switchBtn"><span class="active" data-dataType="income">收入数据</span><img class="line-img" src="${base}/bigscreen/template2/images/line-blue.png" alt=""><span data-dataType="expend">支出数据</span></p>
                <img class="line-img" src="${base}/bigscreen/template2/images/line-blue.png" alt="">
                <button id="dateBtn"><img src="${base}/bigscreen/template2/images/data_icon.png" alt="">日期</button>
            </div>
            <div class="data-box">
                <p class="data-number" id="totalProfit">123,456.5元</p>
                <div class="time-box" id="timeBox">
                    <div class="time-div">
                        <input class="time-input" type="text" value="" id="startTime">
                        <img src="${base}/bigscreen/template2/images/selsct_time.png" alt="">
                    </div>
                    <div class="time-div end">
                        <input class="time-input" type="text" value="" id="endTime">
                        <img src="${base}/bigscreen/template2/images/selsct_time.png" alt="">
                    </div>
                </div>
            </div>
        </div>
        <div class="right-bottom">
            <div class="title-box">
                <button id="setBtn"><img src="${base}/bigscreen/template2/images/settings_icon.png" alt="">设置</button>
            </div>
            <div class="data-box">
                <div class="settings-box">
                    <p><img src="${base}/bigscreen/template2/images/teacher_icon.png" alt="">今日值班：<span id="name_a"></span><span id="date_a"></span></p>
                    <p><img src="${base}/bigscreen/template2/images/people_iocn.png" alt="">负责人：<span id="lea_a"></span></p>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="container">
    <div class="pop-up">
        <span class="close-pop"></span>
        <h2 class="title">当前到件量</h2>
        <div class="pop-data-box">
            <p>123,456,789</p>
        </div>
    </div>

    <div class="pop-up">
        <span class="close-pop"></span>
        <h2 class="title">派件入库量占比</h2>
        <div class="chart-box pie-chart">
            <div id="pie1"></div>
            <div>
                <div class="pie-data">
                </div>
            </div>
        </div>
    </div>

    <div class="pop-up">
        <span class="close-pop"></span>
        <h2 class="title">广东省寄派件数据 </h2>
        <div class="filter-con pop-filter" style="display:flex" data-type="3">
            <div class="select" tabindex="0" hidefocus="true">
                <div class="select-div">
                    派件
                </div>
                <ul class="select-ul">
                    <li class="active" data-value="1">派件</li>
                    <li data-value="2">寄件</li>
                </ul>
            </div>
            <div class="select" tabindex="0" hidefocus="true">
                <div class="select-div">
                    公司
                </div>
                <ul class="select-ul company">
                    <li class="active" data-value="">公司</li>
                    <li data-value="1">顺丰</li>
                    <li data-value="2">京东</li>
                    <li data-value="2">EMS</li>
                </ul>
            </div>
            <div class="select" tabindex="0" hidefocus="true">
                <div class="select-div">
                    快件类型
                </div>
                <ul class="select-ul">
                    <li class="active" data-value="">快件类型</li>
                    <li data-value="0">文件</li>
                    <li data-value="1">物品</li>
                </ul>
            </div>
        </div>
        <div class="chart-box pop-chart">
            <div id="gdMaps" class="gd-map"></div>
        </div>
    </div>

    <div class="pop-up">
        <span class="close-pop"></span>
        <div class="filter-con pop-filters" style="display:flex" data-type="4">
            <div class="select-pop" tabindex="0" hidefocus="true">
                <ul id="barTypes">
                    <li class="active" data-value="1">派件</li>
                    <li data-value="2">寄件</li>
                </ul>
            </div>
            <div class="select" tabindex="0" hidefocus="true">
                <div class="select-div">
                    公司
                </div>
                <ul class="select-ul company">
                    <li class="active" data-value="">公司</li>
                    <li data-value="1">顺丰</li>
                    <li data-value="2">京东</li>
                    <li data-value="2">EMS</li>
                </ul>
            </div>
            <div class="select" tabindex="0" hidefocus="true">
                <div class="select-div">
                    快件类型
                </div>
                <ul class="select-ul">
                    <li class="active" data-value="">快件类型</li>
                    <li data-value="0">文件</li>
                    <li data-value="1">物品</li>
                </ul>
            </div>
        </div>
        <div class="cont-div">
            <div class="chart-box pop-charts">
                <div id="chart4s" style="width:100%;height:95%;"></div>
            </div>
        </div>
        <div class="cont-div">
            <h2 class="title" id="barTitles">派件数据</h2>
            <button class="btn-class" data-state=1 id="tabBtns"><img src="${base}/bigscreen/template2/images/chart_icon.png" alt=""><span>图表</span></button>
            <div class="chart-box pop-chart">
                <div id="chart3s" style="width:100%;height:90%;"></div>
            </div>
            <div class="data-box" style="top:25%;width:8.6rem;display:none;">
                <table class="table2">
                    <tr>
                        <td>入库件</td>
                        <td colspan="3" class="table-data dph-data1">0</td>
                    </tr>
                    <tr class="bg-color">
                        <td rowspan="2">在库件</td>
                        <td rowspan="2" class="table-data dph-data2">0</td>
                        <td>正常件</td>
                        <td class="table-data dph-data3">0</td>
                    </tr>
                    <tr class="bg-color">
                        <td>滞留件</td>
                        <td class="table-data dph-data5">0</td>
                    </tr>
                    <tr>
                        <td rowspan="2">出库件</td>
                        <td rowspan="2" class="dph-data6">0</td>
                        <td>派送件</td>
                        <td class="table-data dph-data7">0</td>
                    </tr>
                    <tr>
                        <td>自提件</td>
                        <td class="table-data dph-data8">0</td>
                    </tr>
                    <tr class="bg-color">
                        <td>退签件</td>
                        <td colspan="3" class="table-data dph-data9">0</td>
                    </tr>
                    <tr>
                        <td>丢失件</td>
                        <td colspan="3" class="table-data dph-data4">0</td>
                    </tr>
                </table>
                <table class="table2" style="display:none;">
                    <tr>
                        <td>入库件</td>
                        <td colspan="3" class="table-data mail-data1">0</td>
                    </tr>
                    <tr class="bg-color">
                        <td rowspan="2">在库件</td>
                        <td rowspan="2" class="table-data mail-data2">0</td>
                        <td>正常件</td>
                        <td class="table-data mail-data7">0</td>
                    </tr>
                    <tr class="bg-color">
                        <td>滞留件</td>
                        <td class="table-data mail-data4">0</td>
                    </tr>

                    <tr>
                        <td>出库件</td>
                        <td colspan="3" class="mail-data6">0</td>
                    </tr>
                    <tr class="bg-color">
                        <td>丢失件</td>
                        <td colspan="3" class="mail-data3">0</td>
                    </tr>
                    <tr>
                        <td>撤销件</td>
                        <td colspan="3" class="table-data mail-data5">0</td>
                    </tr>
                </table>
            </div>
        </div>
        <div class="cont-div">
            <h2 class="title" id="titles"></h2>
            <button class="btn-class" id="dateBtns"><img src="${base}/bigscreen/template2/images/data_icon.png" alt="">日期</button>
            <div class="data-box  pop-time">
                <div class="time-box" id="timeBoxs">
                    <div class="time-div">
                        <input class="time-input" type="text" value="" id="startTimes">
                        <img src="${base}/bigscreen/template2/images/selsct_time.png" alt="">
                    </div>
                    <div class="time-div end">
                        <input class="time-input" type="text" value="" id="endTimes">
                        <img src="${base}/bigscreen/template2/images/selsct_time.png" alt="">
                    </div>
                </div>
            </div>
            <div class="pop-data-box" id="totalProfits">
                <p></p>
            </div>
        </div>
        <div class="pop-data">
            <div class="city-data">
                <div class="city-box">
                    <p id="titleQs"><span>全网</span>到珠海</p>
                    <ul class="city-btn" data-city="2">
                        <li class="active">全网</li>
                        <li>ABCDE</li>
                        <li>FGHIJ</li>
                        <li>KLMNO</li>
                        <li>PQRST</li>
                        <li>UVWXYZ</li>
                    </ul>
                    <ul class="city-div" id="citys">

                    </ul>
                </div>
                <ul class="ranking-box">
                    <li><span></span>
                        <p>城市</p>
                        <p>派件</p>
                    </li>
                    <!--                        <li><span>1</span><p>上海</p><p>1sss25(万件)</p></li>-->
                </ul>

            </div>
        </div>
    </div>
    <div class="pop-up">
        <span class="close-pop"></span>
        <h2 class="title">设置</h2>
        <div class="set-div">
            <div class="set-box">
                <label class="four-f" for="">排班日期</label>
                <div class="time-div">
                    <input class="time-input" type="text" value="" id="times">
                    <img src="${base}/bigscreen/template2/images/selsct_time.png" alt="">
                </div>
            </div>
            <div class="set-box">
                <label for="">值班人</label>
                <input type="text" value="">
                <button class="plus" id="addT"></button>
                <button class="mineus" id="mineusT" style="display:none;"></button>
            </div>
            <div class="set-box">
                <label for="">负责人</label>
                <input type="text" value="">
                <button class="plus" id="addL"></button>
                <button class="mineus" id="mineusL" style="display:none;"></button>
                <button class="add-btn" id="addSet"><img src="${base}/bigscreen/template2/images/plus.png" alt="">添加</button>
            </div>
            <table class="table3">
                <thead>
                <tr>
                    <th>值班人</th>
                    <th>排班日期</th>
                    <th>负责人</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody id="tList">
                <!--
                                        <tr>
                                            <td colspan="4">
                                                <p style="width:9.6rem;">暂无数据</p>
                                            </td>
                                        </tr>
                -->
                <tr>
                    <td>1</td>
                    <td>1</td>
                    <td>1</td>
                    <td>1</td>
                </tr>
                <tr>
                    <td>1</td>
                    <td>1</td>
                    <td>1</td>
                    <td>1</td>
                </tr>
                <tr>
                    <td>1</td>
                    <td>1</td>
                    <td>1</td>
                    <td>1</td>
                </tr>
                <tr>
                    <td>1</td>
                    <td>1</td>
                    <td>1</td>
                    <td>1</td>
                </tr>
                <tr>
                    <td>1</td>
                    <td>1</td>
                    <td>1</td>
                    <td>1</td>
                </tr>
                <tr>
                    <td>1</td>
                    <td>1</td>
                    <td>1</td>
                    <td>1</td>
                </tr>
                <tr>
                    <td>1</td>
                    <td>1</td>
                    <td>1</td>
                    <td>1</td>
                </tr>
                </tbody>
            </table>
            <div class="pages-div" class="mineus">
                <button class="prev"></button>
                <p id="page"><span>0</span>/<span>0</span></p>
                <button class="next"></button>
                <input type="number">
                <button class="skip">跳转</button>
            </div>
        </div>
        <div class="tishi">日期已存在!</div>
        <div class="edit-div" style="display:none;">
            <h4>编辑</h4>
            <span class="close-edit"></span>
            <div class="set-box">
                <label for="">值班人</label>
                <input class="input-edit" id="editT" type="text" value="">
            </div>
            <div class="set-box">
                <label for="">负责人</label>
                <input class="input-edit" id="editL" type="text" value="">
            </div>
            <div class="set-box edit-box">
                <button id="qxEdit">取消</button>
                <button id="qdEdit">确定</button>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="${base}/bigscreen/template2/js/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="${base}/bigscreen/template2/js/layer/layer.min.js"></script>
<script type="text/javascript" src="${base}/bigscreen/template2/js/layer/laydate/laydate.js"></script>
<script type="text/javascript" src="${base}/bigscreen/template2/js/echarts.min.js"></script>
<script type="text/javascript" src="${base}/bigscreen/template2/js/china.js"></script>
<script type="text/javascript" src="${base}/bigscreen/template2/js/data/guangdong.js"></script>
<script type="text/javascript" src="${base}/bigscreen/template2/js/base.js"></script>
<script type="text/javascript">
    $('document').ready(function () {
        $("body").css('visibility', 'visible');
        var localData = [$('#teacher').val(), $('#start').val() + '/' + $('#end').val(), $('#leader').val()]
        localStorage.setItem("data", localData);
        $('#conBtn').on('click', function () {
            localData = [$('#teacher').val(), $('#start').val() + '/' + $('#end').val(), $('#leader').val()]
            if (typeof (Storage) !== "undefined") {
                localStorage.setItem("data", localData);
                var arr = localStorage.getItem("data").split(',');
                $('#name_a').html(arr[0]);
                $('#date_a').html(arr[1]);
                $('#lea_a').html(arr[2]);
            }
        })
        $('#fangda').on('click', function () {
            if ($(this).siblings('ul').is(":hidden")) {
                $(this).addClass('active').siblings('ul').show();
            } else {
                $(this).removeClass('active').siblings('ul').hide();
            }
        })

        $('.modal-btn>li').on('click', function () {
            var index = $(this).index();
            if (index <= 2) {
                $('.container').attr('style', 'visibility: visible').find('.pop-up').eq(index).attr('style', 'visibility: visible').siblings().attr('style', 'visibility: hidden');
            } else if (index > 2 && index < 5) {
                $('.container').attr('style', 'visibility: visible').find('.pop-up').eq(3).attr('style', 'visibility: visible').siblings().attr('style', 'visibility: hidden');
                if (index != 3) {
                    $('.pop-data .ranking-box').hide();
                } else {
                    $('.pop-data .ranking-box').show();
                }
                $('.cont-div').eq(index - 3).attr('style', 'visibility: visible').siblings('.cont-div').attr('style', 'visibility: hidden');
            } else if (index == 5) {
                $('.container').attr('style', 'visibility: visible').find('.pop-up').eq(3).attr('style', 'visibility: visible').siblings().attr('style', 'visibility: hidden');
                $('.pop-data .ranking-box').hide();
                if ($('#switchBtn').find('.active').data('datatype') == "income") {
                    $('#titles').html('收入数据');
                    $('#totalProfits').html('123,456.5元');
                    $('.cont-div').eq(2).attr('style', 'visibility: visible').siblings('.cont-div').attr('style', 'visibility: hidden');
                } else if ($('#switchBtn').find('.active').data('datatype') == 'expend') {
                    $('#titles').html('支出数据');
                    $('#totalProfits').html('32,111.4元');
                    $('.cont-div').eq(2).attr('style', 'visibility: visible').siblings('div').attr('style', 'visibility: hidden');
                }
            }
        })
    })
</script>



</html>