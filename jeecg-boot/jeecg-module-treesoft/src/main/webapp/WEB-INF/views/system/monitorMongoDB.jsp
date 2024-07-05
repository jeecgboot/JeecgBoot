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
                         <!--  
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-table-gear" plain="true"   onclick="monitorItem()">详细状态参数</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
                         -->
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
     <div  style="margin:5px;padding:2px;height:auto;width:17%;border:1px solid #95B8E7;float:left; text-align:center;center;background:#00C0EF">
        <span class="l-monitor-title-text" >collections </span> <br>
        <span style="font-size:23px;color:#fff;" id="collections" title="集合数量">0 </span> 
      </div>
      
      <div  style="margin:5px;padding:2px;height:auto;width:17%;border:1px solid #95B8E7;float:left; text-align:center;center;background:#00A65A">
          <span class="l-monitor-title-text">dataSize(MB) </span> <br>
          <span style="font-size:23px;color:#fff;" id="dataSize" title="数据库大小" >0 </span> 
     </div>
     
     <div  style="margin:5px;padding:2px;height:auto;width:17%;border:1px solid #95B8E7;float:left; text-align:center;background:#F39C12">
        <span class="l-monitor-title-text">connections</span> <br> 
        <span style="font-size:23px;color:#fff;" id="connections" title="当前连接数">0 </span> 
     </div>
     
     <div  style="margin:5px;padding:2px;height:auto;width:17%;border:1px solid #95B8E7;float:left; text-align:center;background:#DD4B39">
         <span class="l-monitor-title-text">mem(MB) </span> <br>
         <span style="font-size:23px;color:#fff;" id="mem" title="物理内存消耗" >0 </span> 
     </div>
     <div  style="margin:5px;padding:2px;height:auto;width:18%;border:1px solid #95B8E7;float:left; text-align:center;background:#3C8DBC">
          <span class="l-monitor-title-text">uptime(秒) </span> <br>
          <span style="font-size:23px;color:#fff;" id="uptime" title="mongod服务启动后到现在已经存活的秒数">0 </span> 
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
				 $("#collections").html(data.collections );
		         $("#dataSize").html( Math.round( data.dataSize/1024/1024*100 )/100 );
		         $("#connections").html( data.connections );
		         $("#mem").html(data.resident  );
		         $("#uptime").html( data.uptime  ) ;
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
               text: '网络流量(kb)',
               subtext: ''
             },
             tooltip : {
               trigger: 'axis'
              },
              legend: {
                 data:['Bytes_In','Bytes_out']
              },
            toolbox: {
               show : true,
               feature : {
                  mark : {show: false},
                  dataView : {show: true, readOnly: true},
                  magicType : {show: true, type: ['line', 'bar']},
                  restore : {show: true},
                  saveAsImage : {show: true}
                }
             }, 
            calculable : true,
            xAxis : [
             {
               type : 'category',
               data :[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
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
               name:'Bytes_In',
               type:'line',
               data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
            },
           {
               name:'Bytes_out',
               type:'line',
               data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
            }
           ]
        };
     
        // 为echarts对象加载数据   
            myChart.setOption(option);   
        }  
   );  
   
   
  var update_old=0;
  var query_old=0;
  var delete_old=0;
  var insert_old=0;
   
  var bytesIn_old=0;
  var bytesOut_old=0;
   
  function mainAddData(){
	var datetime = new Date();
     axisData = datetime.getHours()+":" + datetime.getMinutes() +":" + datetime.getSeconds();
	$.ajax({
	    type : "get",	  
		url:"${ctx}/system/permission/i/queryDatabaseStatus/"+ databaseName +'/'+databaseConfigId ,
		dataType : "json",
		success:function(data) {
		  // myChart.hideLoading();
		 
		 // bytesIn_old = data.bytesIn ;
		 // bytesOut_old = data.bytesOut ;
		  if(bytesIn_old == 0){
			  bytesIn_old = data.bytesIn ;
		  }
		  if(bytesOut_old == 0){
			  bytesOut_old = data.bytesOut ;
		  }
		  
		  // 动态数据接口 addData
          myChart.addData([
            [
              0,        // 系列索引
              Math.round(  parseInt( (data.bytesIn - bytesIn_old))/1024 ), // 新增数据
              false,     // 新增数据是否从队列头部插入
              false     // 是否增加队列长度，false则自定删除原有数据，队头插入删队尾，队尾插入删队头
            ],
            [
              1,        // 系列索引
              Math.round( parseInt((data.bytesOut - bytesOut_old))/1024 ), // 新增数据
              false,    // 新增数据是否从队列头部插入
              false,    // 是否增加队列长度，false则自定删除原有数据，队头插入删队尾，队尾插入删队头
              axisData  // 坐标轴标签, For example 18:10:12
            ]
         ]);
		  
		  
		  if(update_old == 0){
			  update_old = data.update;
		  }
		  if(query_old == 0){
			  query_old = data.query;
		  }
		  if(delete_old == 0){
			  delete_old = data.delete;
		  }
		  if(insert_old == 0){
			  insert_old = data.insert;
		  }
		  
		  myChart2.addData([
            [
              0,        // 系列索引
              Math.abs( ( data.update - update_old))  , // 新增数据 update 
              false,    // 新增数据是否从队列头部插入
              false     // 是否增加队列长度，false则自定删除原有数据，队头插入删队尾，队尾插入删队头
            ],
            [
              1,        
              Math.abs( ( data.query - query_old )) , // 新增数据 query
              false,     
              false   
            ],
            [
              2,        
              Math.abs( (data.delete - delete_old))  , // 新增数据delete
              false,     
              false    
            ],
            [
              3,        
              Math.abs((data.insert - insert_old))   , // 新增数据insert
              false,     
              false,
              axisData  // 坐标轴标签, For example 18:10:12
            ]
            
          ]);
		  
		 // alert( data.Com_select +" " + select_old );
		  
		  update_old  =data.update;
		  query_old =  data.query;
		  delete_old = data.delete;
	      insert_old = data.insert;
	     //alert(bytesIn_old  +','+ data.bytesIn )		  
		  bytesIn_old = data.bytesIn  ;
		  bytesOut_old = data.bytesOut ; ;
		  
	      myChart3.addData([
	    	  [
                 0,        // 系列索引
                 data.globalLock_total , // 新增数据   
                 false,    // 新增数据是否从队列头部插入
                 false     // 是否增加队列长度，false则自定删除原有数据，队头插入删队尾，队尾插入删队头
              ],
	    	  [
                 1,        // 系列索引
                data.globalLock_readers , // 新增数据   
                 false,    // 新增数据是否从队列头部插入
                 false     // 是否增加队列长度，false则自定删除原有数据，队头插入删队尾，队尾插入删队头
              ],
               [
                 2,        // 系列索引
                 data.globalLock_writers, // 新增数据
                 false,    // 新增数据是否从队列头部插入
                 false,    // 是否增加队列长度，false则自定删除原有数据，队头插入删队尾，队尾插入删队头
                 axisData  // 坐标轴标签,  
               ]
           ]);
	      
		  $("#collections").html( data.collections );
		  $("#dataSize").html(  Math.round( data.dataSize/1024/1024*100 )/100 );
		  $("#connections").html( data.connections  );
		  $("#mem").html(data.resident  );
		  $("#uptime").html( data.uptime  ) ;
		   
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
             text: '命令执行情况',  
             subtext: ''  
      },  
      tooltip : {  
        trigger: 'axis'  
      },  
      legend: {  
        data:['insert','query','update','delete']  
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
               name:'insert',  
               type:'line',  
               data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
        },
        {
               name:'query',
               type:'line',
               data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
        },
        {
               name:'update',
               type:'line',
               data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
        },
        {
               name:'delete',
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
             text: '全局锁信息',  
             subtext: ''  
           },  
      tooltip : {  
        trigger: 'axis'  
      },  
      legend: {  
        data:['globalLock_total','globalLock_readers','globalLock_writers']  
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
            name:'globalLock_total',  
            type:'line',  
            data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
        },
        {  
            name:'globalLock_readers',  
            type:'line',  
            data:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
        },
        {  
            name:'globalLock_writers',  
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