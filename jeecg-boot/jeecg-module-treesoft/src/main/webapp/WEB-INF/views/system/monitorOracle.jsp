<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<%@ include file="/WEB-INF/views/include/codemirror.jsp"%>

<script type="text/javascript" src="${ctx}/static/js/echarts.js"></script>
<script type="text/javascript" src="${ctx}/static/js/codemirror.js"></script> 
<script type="text/javascript" src="${ctx}/static/js/chart/line.js"></script>
<script type="text/javascript" src="${ctx}/static/js/chart/bar.js"></script>
 
<style>

.main {
    overflow:hidden;
    height:250px;
    padding:0px;
    margin:10px;
    border: 1px solid #e3e3e3;
    -webkit-border-radius: 4px;
       -moz-border-radius: 4px;
            border-radius: 4px;
    -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
       -moz-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
            box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
}

.mainChar {
    overflow:hidden;
    padding :0px;
    margin:10px;
    border: 1px solid #e3e3e3;
    -webkit-border-radius: 4px;
       -moz-border-radius: 4px;
            border-radius: 4px;
    -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
       -moz-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
            box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
}

.main3 {
    overflow:hidden;
    height:250px;
    width:48%;
    float:left;
    padding:0px;
    margin:10px;
    border: 1px solid #e3e3e3;
    -webkit-border-radius: 4px;
       -moz-border-radius: 4px;
            border-radius: 4px;
    -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
       -moz-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
            box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
}
</style>
</head>
<body>
 <div id="tb2" style="height:auto">
 <input type="hidden" id="databaseConfigId"  value="${databaseConfigId}" >
 <input type="hidden" id="databaseName"  value="${databaseName}" >
                         <div class="panel-header panel-header-noborder  " 	style="padding: 5px; height: auto">
                         
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-table-gear" plain="true"   onclick="monitorItem()">详细状态参数</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
                         
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-arrow-refresh" plain="true" onclick="refresh()">刷新</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
	                       
                            <input type="checkbox" checked id="isAuto" >自动刷新  </input>
	                        <span class="toolbar-item dialog-tool-separator"></span>
                            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-tip" plain="true" title="刷新间隔10秒"></a>  
                            <span class="toolbar-item dialog-tool-separator" ></span>
                             
                             <a href="javascript:void(0)" class="easyui-linkbutton" plain="true"  >【${databaseType}】${name} ${ip}:${port} </a>
                            <span class="toolbar-item dialog-tool-separator"></span>                                            
                         </div> 
                       
  </div>

   <div>
   <div class ="mainChar" style="text-align:center"> 
     <div  style="margin:5px;padding:2px;height:auto;width:17%;border:1px solid #95B8E7;float:left; text-align:center;background:#00C0EF">
        <span class="l-monitor-title-text">连接线程数 </span> <br>
        <span style="font-size:23px;color:#fff;" id="sessions" title=连接数 >0 </span> 
      </div>
      
      
      <div  style="margin:5px;padding:2px;height:auto;width:17%;border:1px solid #95B8E7;float:left; text-align:center;background:#00A65A">
          <span class="l-monitor-title-text">缓冲区命中率</span> <br>
          <span style="font-size:23px;color:#fff;" id="hit_ratio" title="如果命中率低于70％，则应该加大init.ora参数中的DB_BLOCK_BUFFER的值">0 </span> 
     </div>
     
     <div  style="margin:5px;padding:2px;height:auto;width:17%;border:1px solid #95B8E7;float:left; text-align:center;background:#F39C12">
        <span class="l-monitor-title-text">共享池的命中率</span> <br> 
        <span style="font-size:23px;color:#fff;" id="hit_radio" title="假如共享池的命中率低于95%,就要考虑调整应用（通常是没使用bind var）或者增加内存">0 </span> 
     </div>
     
     <div  style="margin:5px;padding:2px;height:auto;width:17%;border:1px solid #95B8E7;float:left; text-align:center;background:#DD4B39" >
         <span class="l-monitor-title-text"> 日志缓冲占用率  </span> <br>
         <span style="font-size:23px;color:#fff;" id="log_buffer" title="假如 大于 1%,可以考虑增大log_buffer">0 </span> 
     </div>
     
     <div  style="margin:5px;padding:2px;height:auto;width:17%;border:1px solid #95B8E7;float:left; text-align:center;background:#3C8DBC">
          <span class="l-monitor-title-text"> 锁(lock)情况</span> <br>
          <span style="font-size:23px;color:#fff;" id="locks">0 </span> 
     </div>
    </div>
  
     <div  id="graphic1" style="height:auto;width:auto;"> 
            <div id="main1" class="main"></div>
     </div> 
      
       <div  id="graphic2" style="height:auto;width:auto;"> 
            <div id="main2" class="main"></div>
      </div> 
          
      <div  id="graphic3" style="height:auto;width:auto;"> 
            <div id="main3" class="main"></div>
      </div>         
           
      <div  id="graphic4" style="height:auto;width:auto;"> 
            <div id="main4" class="main" style="display:none"></div>
            <div id="main5" class="main" style="display:none"></div>
      </div> 
           
    </div>
 
<script type="text/javascript">

var timeTicket
var lastData = 11;
var axisData;
var databaseConfigId;
var databaseName ;
//var questions1=0;
//var questions_qps = 0;

$(function(){ 
     databaseConfigId = $("#databaseConfigId").val();
	 databaseName = $("#databaseName").val();
     clearInterval(timeTicket);
     timeTicket = setInterval(function (){
        mainAddData();
     }, 10000);
                    	
	  queryInfoItem();
	  
	  $("#isAuto").change(function() {
       if( $(this).is(':checked') ){
    	   
		   timeTicket = setInterval(function (){
              mainAddData();
            }, 10000); 
	   }else{
		    clearInterval(timeTicket); 
	   }
     }); 
});
     
function refresh(){
	  queryInfoItem();
}

function monitorItem(){
	 parent.window.mainpage.mainTabs.addModule( '详细状态参数' ,'${ctx}/system/permission/i/monitorItem/'+databaseName +'/'+ databaseConfigId ,  'icon-berlin-statistics');
}
    
 //查询状态参数
 function queryInfoItem(){
	// databaseConfigId  = $('#databaseConfigId').val();
	 //alert( databaseConfigId  );
	  $.ajax({
		type:'get',
		url:"${ctx}/system/permission/i/queryDatabaseStatus/"+ databaseName +'/'+databaseConfigId,
		success: function(data){
			var status = data.status ;
			if(status == 'success' ){
				 $("#sessions").html( data.SESSIONS );
		         $("#hit_ratio").html( data.HIT_RATIO +"%" );
		         $("#hit_radio").html( data.HIT_RADIO +"%" );
		         $("#log_buffer").html(data.LOG_BUFFER +"%" );
		         $("#locks").html( data.LOCKS  ) ;
			}else{
				 parent.$.messager.show({ title : "提示",msg: data.mess , position: "Center" });
			}
		   
		}
      });
 }
 
  var myChart;
  var myChart2 ;
  var myChart3 ;
  
   // 使用  
  require(  
        [  
            'echarts',  
            'echarts/chart/line',
            'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载  
        ],  
        function (ec) {  
            // 基于准备好的dom，初始化echarts图表  
            myChart = ec.init(document.getElementById('main1'));   
            var option = {
             title : {
               text: '表空间使用情况',
               subtext: ''
             },
             tooltip : {
                trigger: 'axis',
                  axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                  }
              },
              legend: {
                 data:['空闲空间(M)','已使用(M)']
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
               data :[ ${tableSpaceName} ]
             }
            ],
            yAxis : [
             {
                type : 'value'                
             }
            ],
           series : [
            {
              name:'空闲空间(M)',
              type:'bar',
              barWidth : 25,
              stack: 'tableSpace',
              data:[ ${tableSpaceSizeFree} ]
            },
            {
               name:'已使用(M)',
               type:'bar',
               stack: 'tableSpace',
               data:[ ${tableSpaceSizeUsed} ]
            }
           ]
        };
     
        // 为echarts对象加载数据   
            myChart.setOption(option);   
        }  
   );  
   
   
  var read_old=0;
  var write_old=0;
  
  var Bytes_received_old=0;
  var Bytes_sent_old=0;
   
  function mainAddData(){
	var datetime = new Date();
     axisData = datetime.getHours()+":" + datetime.getMinutes() +":" + datetime.getSeconds();
	$.ajax({
	    type : "get",	  
		url:"${ctx}/system/permission/i/queryDatabaseStatus/"+ databaseName +'/'+databaseConfigId ,
		dataType : "json",
		success:function(data) {
		  // myChart.hideLoading();
		 
		  
		  if(Bytes_received_old == 0){
			  Bytes_received_old = data.Bytes_received ;
		  }
		  if(Bytes_sent_old == 0){
			  Bytes_sent_old = data.Bytes_sent ;
		  }
		  
		  // 动态数据接口 addData
          
		  
		  
		  if(read_old == 0){
			  read_old = data.PHYRDS;
		  }
		  if(write_old == 0){
			  write_old = data.PHYWRTS;
		  }
		  
		  myChart2.addData([
            [
              0,        // 系列索引
            Math.abs( (data.PHYRDS - read_old)) /10 , // 新增数据 read, 10秒 平均值
              false,    // 新增数据是否从队列头部插入
              false     // 是否增加队列长度，false则自定删除原有数据，队头插入删队尾，队尾插入删队头
            ],
            [
              1,        
              Math.abs( ( data.PHYWRTS - write_old )) /10, // 新增数据 write ,10秒 平均值
              false,     
              false,
              axisData  // 坐标轴标签, For example 18:10:12
            ] 
            
          ]);
		  
		 // alert( data.Com_select +" " + select_old );
		  
		  read_old  = data.PHYRDS;
		  write_old = data.PHYWRTS;
		  
		  Bytes_received_old = data.Bytes_received ;
		  Bytes_sent_old = data.Bytes_sent ; ;
		  
	      myChart3.addData([
               [
                0,        // 系列索引
                data.SESSIONS, // 新增数据
                false,    // 新增数据是否从队列头部插入
                false,    // 是否增加队列长度，false则自定删除原有数据，队头插入删队尾，队尾插入删队头
                axisData  // 坐标轴标签, For example 18:10:12
               ]
           ]);
	      
		  $("#sessions").html( data.SESSIONS );
		  $("#hit_ratio").html( data.HIT_RATIO +"%" );
		  $("#hit_radio").html( data.HIT_RADIO +"%" );
		  $("#log_buffer").html(data.LOG_BUFFER +"%" );
		  $("#locks").html( data.LOCKS  ) ;
		  
		},
		error: function(errorMsg) {
		  //  alert("不好意思，图表请求数据失败啦！");
		}
	});
  }

   // 使用  
  require(  
        [  
            'echarts',  
            'echarts/chart/line',
            'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载  
        ],  
      function (ec) {  
       // 基于准备好的dom，初始化echarts图表  
       myChart2 = ec.init(document.getElementById('main2'));   
          
        var option = {  
             title : {  
             text: '表空间的I/O读写(kb/sec)',  
             subtext: ''  
      },  
      tooltip : {  
        trigger: 'axis'  
      },  
      legend: {  
        data:['read','write']  
      },  
      toolbox: {  
        show : true,  
        feature : {  
            mark : {show: false},  
            dataView : {show: true, readOnly: false},  
            magicType: {show: true, type: ['line', 'bar']},  
            restore : {show: true},  
            saveAsImage : {show: true}  
        }  
      },  
      calculable : true,  
      xAxis : [  
        {  
        	type : 'category',  
            data : [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
        }  
      ],  
      yAxis : [  
        {  
            type : 'value',  
            data: [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
        }  
      ],  
      
      
      series : [  
        {  
               name:'read',  
               type:'line',  
               data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
        },
        {
               name:'write',
               type:'line',
               data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
        } 
       ]  
     };  
          // 为echarts对象加载数据   
           myChart2.setOption(option);   
        }  
   ); 
    
   // 使用  
  require(  
        [  
            'echarts',  
            'echarts/chart/line',
            'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载  
        ],  
        function (ec) {  
            // 基于准备好的dom，初始化echarts图表  
            myChart3 = ec.init(document.getElementById('main3'));   
          
            var option = {  
             title : {  
             text: '连接线程数',  
             subtext: ''  
           },  
      tooltip : {  
        trigger: 'axis'  
      },  
      legend: {  
        data:['连接线程数']  
      },  
      toolbox: {  
        show:true,  
        feature : {  
            mark : {show: false},  
            dataView : {show: true, readOnly: false},  
            magicType: {show: true, type: ['line', 'bar']},  
            restore : {show: true},  
            saveAsImage : {show: true}  
        }  
      },  
      calculable:true,  
      xAxis : [  
        {  
        	type:'category',  
            data :[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
        }  
      ],  
      yAxis : [  
        {  
            type:'value',  
            data: [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0] 
        }  
      ],  
      series : [  
        {  
            name:'连接线程数',  
            type:'line',  
            data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
        }  
       ]  
     };  
            // 为echarts对象加载数据   
            myChart3.setOption(option);   
        }  
   ); 
    
 // 使用  
  require(  
        [  
            'echarts',  
            'echarts/chart/line',
            'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载  
        ],  
        function (ec) {  
            // 基于准备好的dom，初始化echarts图表  
            var myChart = ec.init(document.getElementById('main4'));   
          
     var option = {  
      title : {  
        text: '内存占用率',  
        subtext: ''  
      },  
      tooltip : {  
        trigger: 'axis'  
      },  
      legend: {  
        data:['2015年']  
      },  
      toolbox: {  
        show : true,  
        feature : {  
            mark : {show: false},  
            dataView : {show: true, readOnly: false},  
            magicType: {show: true, type: ['line', 'bar']},  
            restore : {show: true},  
            saveAsImage : {show: true}  
        }  
      },  
      calculable : true,  
      xAxis : [  
        {  
        	type : 'category',  
            data : ['1月','2月','3月','4月','5月','总'] 
        }  
      ],  
      yAxis : [  
        {  
            type : 'value',  
            boundaryGap : [0, 0.01] 
        }  
      ],  
      series : [  
        {  
            name:'2015年',  
            type:'bar',  
            data:[48203, 53489, 119034, 184970, 231744, 630230]  
        }  
       ]  
     };  
  
        // 为echarts对象加载数据   
            myChart.setOption(option);   
        }  
   ); 
 
  // 使用  
  require(  
        [  
            'echarts',  
            'echarts/chart/line',
            'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载  
        ],  
        function (ec) {  
            // 基于准备好的dom，初始化echarts图表  
            var myChart = ec.init(document.getElementById('main5'));   
          
     var option = {  
      title : {  
        text: '内存占用率',  
        subtext: ''  
      },  
      tooltip : {  
        trigger: 'axis'  
      },  
      legend: {  
        data:['2015年']  
      },  
      toolbox: {  
        show : true,  
        feature : {  
            mark : {show: false },  
            dataView : {show: true, readOnly: false},  
            magicType: {show: true, type: ['line', 'bar']},  
            restore : {show: true},  
            saveAsImage : {show: true}  
        }  
      },  
      calculable : true,  
      xAxis : [  
        {  
            type : 'category',  
            data : ['1月','2月','3月','4月','5月','总'] 
        }  
      ],  
      yAxis : [  
        {  
        	type : 'value',  
            boundaryGap : [0, 0.01]  
             
        }  
      ],  
      series : [  
        {  
            name:'2015年',  
            type:'bar',  
            data:[48203, 53489, 119034, 184970, 231744, 630230]  
        }  
       ]  
     };  
  
        // 为echarts对象加载数据   
            myChart.setOption(option);   
        }  
   );              
      
   
</script>
 <%--
   <script type="text/javascript" src="${ctx}/static/js/echartsExample.js"></script>
 --%>
</body>
</html>