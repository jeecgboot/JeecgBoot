<!DOCTYPE html>
<html>
<head>
    <#-- import macro -->
    <#import "../common/common.macro.ftl" as netCommon>

    <!-- 1-style start -->
    <@netCommon.commonStyle />
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/bower_components/bootstrap-daterangepicker/daterangepicker.css">
    <!-- 1-style end -->

</head>
<body class="hold-transition" style="background-color: #ecf0f5;">
<div class="wrapper">
    <section class="content">

        <#-- 2-biz start -->

        <!-- 任务信息 -->
        <div class="row">

            <#-- 任务信息 -->
            <div class="col-md-4 col-sm-6 col-xs-12">
                <div class="info-box bg-aqua">
                    <span class="info-box-icon"><i class="fa fa-flag-o"></i></span>

                    <div class="info-box-content">
                        <span class="info-box-text">${I18n.job_dashboard_job_num}</span>
                        <span class="info-box-number">${jobInfoCount}</span>

                        <div class="progress">
                            <div class="progress-bar" style="width: 100%"></div>
                        </div>
                        <span class="progress-description">${I18n.job_dashboard_job_num_tip}</span>
                    </div>
                </div>
            </div>

            <#-- 调度信息 -->
            <div class="col-md-4 col-sm-6 col-xs-12" >
                <div class="info-box bg-yellow">
                    <span class="info-box-icon"><i class="fa fa-calendar"></i></span>

                    <div class="info-box-content">
                        <span class="info-box-text">${I18n.job_dashboard_trigger_num}</span>
                        <span class="info-box-number">${jobLogCount}</span>

                        <div class="progress">
                            <div class="progress-bar" style="width: 100%" ></div>
                        </div>
                        <span class="progress-description">
                                ${I18n.job_dashboard_trigger_num_tip}
                            <#--<#if jobLogCount gt 0>
                                调度成功率：${(jobLogSuccessCount*100/jobLogCount)?string("0.00")}<small>%</small>
                            </#if>-->
                            </span>
                    </div>
                </div>
            </div>

            <#-- 执行器 -->
            <div class="col-md-4 col-sm-6 col-xs-12">
                <div class="info-box bg-green">
                    <span class="info-box-icon"><i class="fa ion-ios-settings-strong"></i></span>

                    <div class="info-box-content">
                        <span class="info-box-text">${I18n.job_dashboard_jobgroup_num}</span>
                        <span class="info-box-number">${executorCount}</span>

                        <div class="progress">
                            <div class="progress-bar" style="width: 100%"></div>
                        </div>
                        <span class="progress-description">${I18n.job_dashboard_jobgroup_num_tip}</span>
                    </div>
                </div>
            </div>

        </div>

        <#-- 调度报表：时间区间筛选，左侧折线图 + 右侧饼图 -->
        <div class="row">
            <div class="col-md-12">
                <div class="box">
                    <div class="box-header with-border">
                        <h3 class="box-title">${I18n.job_dashboard_report}</h3>
                        <#--<input type="text" class="form-control" id="filterTime" readonly >-->

                        <!-- tools box -->
                        <div class="pull-right box-tools">
                            <button type="button" class="btn btn-primary btn-sm daterange pull-right" data-toggle="tooltip" id="filterTime" >
                                <i class="fa fa-calendar"></i>
                            </button>
                            <#--<button type="button" class="btn btn-primary btn-sm pull-right" data-widget="collapse" data-toggle="tooltip" title="" style="margin-right: 5px;" data-original-title="Collapse">
                                <i class="fa fa-minus"></i>
                            </button>-->
                        </div>
                        <!-- /. tools -->

                    </div>
                    <div class="box-body">
                        <div class="row">
                            <#-- 左侧折线图 -->
                            <div class="col-md-8">
                                <div id="lineChart" style="height: 350px;"></div>
                            </div>
                            <#-- 右侧饼图 -->
                            <div class="col-md-4">
                                <div id="pieChart" style="height: 350px;"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <#-- 2-biz end -->

    </section>
</div>

<!-- 3-script start -->
<@netCommon.commonScript />
<!-- daterangepicker -->
<script src="${request.contextPath}/static/adminlte/bower_components/moment/moment.min.js"></script>
<script src="${request.contextPath}/static/adminlte/bower_components/bootstrap-daterangepicker/daterangepicker.js"></script>
<!-- echarts -->
<script src="${request.contextPath}/static/plugins/echarts/echarts.common.min.js"></script>
<script>
$(function () {

    // filter Time
    var rangesConf = {};
    rangesConf[I18n.daterangepicker_ranges_today] = [moment().startOf('day'), moment().endOf('day')];
    rangesConf[I18n.daterangepicker_ranges_yesterday] = [moment().subtract(1, 'days').startOf('day'), moment().subtract(1, 'days').endOf('day')];
    rangesConf[I18n.daterangepicker_ranges_this_month] = [moment().startOf('month'), moment().endOf('month')];
    rangesConf[I18n.daterangepicker_ranges_last_month] = [moment().subtract(1, 'months').startOf('month'), moment().subtract(1, 'months').endOf('month')];
    rangesConf[I18n.daterangepicker_ranges_recent_week] = [moment().subtract(1, 'weeks').startOf('day'), moment().endOf('day')];
    rangesConf[I18n.daterangepicker_ranges_recent_month] = [moment().subtract(1, 'months').startOf('day'), moment().endOf('day')];

    $('#filterTime').daterangepicker({
        autoApply:false,
        singleDatePicker:false,
        showDropdowns:false,        // 是否显示年月选择条件
        timePicker: true, 			// 是否显示小时和分钟选择条件
        timePickerIncrement: 10, 	// 时间的增量，单位为分钟
        timePicker24Hour : true,
        opens : 'left', //日期选择框的弹出位置
        ranges: rangesConf,
        locale : {
            format: 'YYYY-MM-DD HH:mm:ss',
            separator : ' - ',
            customRangeLabel : I18n.daterangepicker_custom_name ,
            applyLabel : I18n.system_ok ,
            cancelLabel : I18n.system_cancel ,
            fromLabel : I18n.daterangepicker_custom_starttime ,
            toLabel : I18n.daterangepicker_custom_endtime ,
            daysOfWeek : I18n.daterangepicker_custom_daysofweek.split(',') ,        // '日', '一', '二', '三', '四', '五', '六'
            monthNames : I18n.daterangepicker_custom_monthnames.split(',') ,        // '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'
            firstDay : 1
        },
        startDate: rangesConf[I18n.daterangepicker_ranges_recent_week][0] ,
        endDate: rangesConf[I18n.daterangepicker_ranges_recent_week][1]
    }, function (start, end, label) {
        freshChartDate(start, end);
    });
    freshChartDate(rangesConf[I18n.daterangepicker_ranges_recent_week][0], rangesConf[I18n.daterangepicker_ranges_recent_week][1]);

    /**
     * fresh Chart Date
     *
     * @param startDate
     * @param endDate
     */
    function freshChartDate(startDate, endDate) {
        $.ajax({
            type : 'POST',
            url : base_url + '/chartInfo',
            data : {
                'startDate':startDate.format('YYYY-MM-DD HH:mm:ss'),
                'endDate':endDate.format('YYYY-MM-DD HH:mm:ss')
            },
            dataType : "json",
            success : function(data){
                if (data.code == 200) {
                    lineChartInit(data.data)
                    pieChartInit(data.data);
                } else {
                    layer.open({
                        title: I18n.system_tips ,
                        btn: [ I18n.system_ok ],
                        content: (data.msg || I18n.job_dashboard_report_loaddata_fail ),
                        icon: '2'
                    });
                }
            }
        });
    }

    /**
     * line Chart Init
     */
    function lineChartInit(data) {
        var option = {
            title: {
                text: I18n.job_dashboard_date_report
            },
            tooltip : {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross',
                    label: {
                        backgroundColor: '#6a7985'
                    }
                }
            },
            legend: {
                data:[I18n.joblog_status_suc, I18n.joblog_status_fail, I18n.joblog_status_running]
            },
            toolbox: {
                feature: {
                    /*saveAsImage: {}*/
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis : [
                {
                    type : 'category',
                    boundaryGap : false,
                    data : data.triggerDayList
                }
            ],
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : [
                {
                    name:I18n.joblog_status_suc,
                    type:'line',
                    stack: 'Total',
                    areaStyle: {normal: {}},
                    data: data.triggerDayCountSucList
                },
                {
                    name:I18n.joblog_status_fail,
                    type:'line',
                    stack: 'Total',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    areaStyle: {normal: {}},
                    data: data.triggerDayCountFailList
                },
                {
                    name:I18n.joblog_status_running,
                    type:'line',
                    stack: 'Total',
                    areaStyle: {normal: {}},
                    data: data.triggerDayCountRunningList
                }
            ],
            color:['#00A65A', '#c23632', '#F39C12']
        };

        var lineChart = echarts.init(document.getElementById('lineChart'));
        lineChart.setOption(option);
    }

    /**
     * pie Chart Init
     */
    function pieChartInit(data) {
        var option = {
            title : {
                text: I18n.job_dashboard_rate_report ,
                /*subtext: 'subtext',*/
                x:'center'
            },
            tooltip : {
                trigger: 'item',
                formatter: "{b} : {c} ({d}%)"
            },
            legend: {
                orient: 'vertical',
                left: 'left',
                data: [I18n.joblog_status_suc, I18n.joblog_status_fail, I18n.joblog_status_running ]
            },
            series : [
                {
                    //name: '分布比例',
                    type: 'pie',
                    radius : '55%',
                    center: ['50%', '60%'],
                    data:[
                        {
                            name:I18n.joblog_status_suc,
                            value:data.triggerCountSucTotal
                        },
                        {
                            name:I18n.joblog_status_fail,
                            value:data.triggerCountFailTotal
                        },
                        {
                            name:I18n.joblog_status_running,
                            value:data.triggerCountRunningTotal
                        }
                    ],
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ],
            color:['#00A65A', '#c23632', '#F39C12']
        };
        var pieChart = echarts.init(document.getElementById('pieChart'));
        pieChart.setOption(option);
    }

});
</script>
<!-- 3-script end -->

</body>
</html>