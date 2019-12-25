<#assign base=springMacroRequestContext.getContextUrl("")>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>JEECG-boot大屏指挥室</title>
    <link href="${base}/bigscreen/template1/css/easyui.css" rel="stylesheet" type="text/css">
    <link href="${base}/bigscreen/template1/css/room.css" rel="stylesheet" type="text/css" />


    <script language="javascript" type="text/javascript" src="${base}/bigscreen/template1/js/jquery.min.js"></script>
    <script language="javascript" type="text/javascript" src="${base}/bigscreen/template1/js/jquery.easyui.min.js"></script>
    <script language="javascript" type="text/javascript" src="${base}/bigscreen/template1/js/echarts.min.js"></script>
    <script language="javascript" type="text/javascript" src="${base}/bigscreen/template1/js/echarts-wordcloud.js"></script>
    <script language="javascript" type="text/javascript" src="${base}/bigscreen/template1/js/china.js"></script>
    <script language="javascript" type="text/javascript" src="${base}/bigscreen/template1/js/geoCoord.js"></script>
    <script language="javascript" type="text/javascript" src="${base}/bigscreen/template1/js/room.js"></script>
</head>

<body>
    <div id="main">
        <!-- 刷新 -->
        <div id="refresh">
            <span id="refreshTime">最后刷新时间：2018-05-06 23:13.24</span>
        </div>
        <!-- 刷新结束 -->

        <!-- 进度条区域开始-->
        <div id="y_gauge1"></div>
        <div id="y_gauge2"></div>
        <div id="y_gauge3"></div>
        <div id="y_gauge4"></div>
        <!-- 进度条区域结束-->

        <!-- 螺旋图开始 -->
        <div id="orderStatus"></div>
        <div class="contentButton" style="top:822px;left:453px">
            <a class="a1" href="javascript:void(0);" onclick="javascript:openDialog('modalDlg');">&nbsp;</a>
        </div>
        <!-- 螺旋图结束 -->

        <!-- 地图开始 -->
        <div id="map"></div>
        <!-- 地图结束 -->

        <!-- 产品饼图开始 -->
        <div id="productPie"></div>
        <!-- 产品饼图结束 -->

        <!-- 业务进展图开始 -->
        <div id="businessProgress"></div>
        <div class="contentButton" style="top:822px;left:3679px">
            <a class="a1" href="javascript:void(0);">&nbsp;</a>
        </div>
        <!-- 业务进展图结束 -->

        <!-- 生产计划展示开始-->
        <div id="plan"></div>
        <div class="contentButton" style="top:1402px;left:453px">
            <a href="javascript:void(0);">&nbsp;</a>
        </div>
        <!-- 生产计划展示结束-->

        <!-- 生产质量展示开始-->
        <div id="quality"></div>
        <div class="contentButton" style="top:1402px;left:1532px">
            <a href="javascript:void(0);">&nbsp;</a>
        </div>
        <!-- 生产质量展示结束-->

        <!-- 客服及投诉展示开始-->
        <div id="produce">
            <table width="100%" cellpadding="6" cellspacing="0">
                <tr class="row1">
                    <td rowspan="2"><span id="currentDate">2018/04/25</span></td>
                    <td colspan="2">产品投诉</td>
                    <td colspan="2">物流投诉</td>
                    <td colspan="2">售后投诉</td>
                </tr>
                <tr class="row1">
                    <td>质量</td>
                    <td>服务</td>
                    <td>质量</td>
                    <td>服务</td>
                    <td>质量</td>
                    <td>服务</td>
                </tr>
                <tr class="row2">
                    <td>已处理</td>
                    <td>48</td>
                    <td>48</td>
                    <td>48</td>
                    <td>48</td>
                    <td>48</td>
                    <td>48</td>
                </tr>
                <tr class="row1">
                    <td>处理中</td>
                    <td>34</td>
                    <td>34</td>
                    <td>34</td>
                    <td>34</td>
                    <td>34</td>
                    <td>34</td>
                </tr>
                <tr class="row2">
                    <td>未处理</td>
                    <td>30</td>
                    <td>28</td>
                    <td>28</td>
                    <td>26</td>
                    <td>25</td>
                    <td>8</td>
                </tr>
                <tr class="row2">
                    <td>合计</td>
                    <td>30</td>
                    <td>28</td>
                    <td>28</td>
                    <td>26</td>
                    <td>25</td>
                    <td>8</td>
                </tr>
                <tr class="row1">
                    <td>总计</td>
                    <td colspan="2">22</td>
                    <td colspan="2">65</td>
                    <td colspan="2">44</td>
                </tr>
            </table>
        </div>
        <div class="contentButton" style="top:1402px;left:2598px">
            <a href="javascript:void(0);">&nbsp;</a>
        </div>
        <!-- 客服及投诉展示结束-->

        <!-- 词云展示开始-->
        <div id="wordCloud"></div>
        <div class="contentButton" style="top:1402px;left:3679px">
            <a href="javascript:void(0);">&nbsp;</a>
        </div>
        <!-- 词云结束-->

        <!-- 仪表盘区域开始-->
        <!-- <div id="gauge1"></div>
            <div class="gaugeTitle" style="left:2200px;top:480px;"><sapn id="vg1">32</sapn>&nbsp;m<sup>3</sup>/d</div>
            <div id="gauge2"></div>
            <div class="gaugeTitle" style="left:2550px;top:480px;"><sapn id="vg2">32</sapn>&nbsp;KVA</div>
            <div id="gauge3"></div>
            <div class="gaugeTitle" style="left:2910px;top:480px;"><sapn id="vg3">32</sapn>&nbsp;Nm<sup>3</sup>/h</div>
            <div id="gauge4"></div>
            <div class="gaugeTitle" style="left:2380px;top:750px;"><sapn id="vg4">32</sapn>&nbsp;m<sup>3</sup>/m</div>
            <div id="gauge5"></div>
            <div class="gaugeTitle" style="left:2730px;top:750px;"><sapn id="vg5">32</sapn>&nbsp;t/h</div> -->
        <!-- 仪表盘区域结束-->

        <!--弹出窗口-->
        <!--<div id="popWindow">
                <div style="padding:20px;font-size:32px; background-color:#051E3C;color:#B7E1FF; border-bottom:1px solid #09F">弹出窗口标题</div>
            </div>-->
        <!--弹出窗口结束---->
    </div>>

    <!--编辑系统用户的弹出窗口-->
    <div id="modalDlg" class="easyui-dialog" title="弹出窗口" data-options="modal:true,closed:true,buttons:
        [{
                    text:'确定',
                    iconCls:'icon-ok',
                    handler:function(){
                        $('#modalDlg').dialog('close');
                    }
                },{
                    text:'取消',
                    handler:function(){
                        $('#modalDlg').dialog('close');
                    }
                }]"
        style="padding:10px">
        <table width="100%" cellpadding="5">
            <tr>
                <td width="80" align="center">用户名称:</td>
                <td><input type="text" name="updateUsername" id="updateUsername" value=""></td>
            </tr>
            <tr>
                <td align="center">登录密码:</td>
                <td><input type="text" name="updateUserpass" id="updateUserpass" value=""></td>
            </tr>
            <tr>
                <td align="center">&nbsp;</td>
                <td height="30">如无需修改密码，请留空</td>
            </tr>
            <tr>
                <td align="center">用户类型:</td>
                <td>
                    <select name="updateUserType" id="updateUserType">
                        <option value="">--请选择--</option>
                        <option value="administrator">管理员</option>
                        <option value="user">系统用户</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td align="center">用户状态:</td>
                <td>
                    <input type="radio" name="updateUserStatus" id="updateUserStatus1" value="0"><label for="updateUserStatus1">启用</label>&nbsp;&nbsp;
                    <input type="radio" name="updateUserStatus" id="updateUserStatus2" value="1"><label for="updateUserStatus2">禁用</label>
                </td>
            </tr>
            <tr>
                <td align="center">用户说明:</td>
                <td>
                    <input type="text" name="updateUserDescription" id="updateUserDescription" value="">
                </td>
            </tr>
        </table>
    </div>
    <!--编辑系统用户的弹出窗口结束-->

</body>

</html>