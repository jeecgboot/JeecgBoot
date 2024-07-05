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
<script type="text/javascript" src="${ctx}/static/js/chart/gauge.js"></script>
<script type="text/javascript" src="${ctx}/static/js/chart/pie.js"></script>
<script type="text/javascript" src="${ctx}/static/js/chart/bar.js"></script>
<script type="text/javascript" src="${ctx}/static/js/chart/theme/shine.js"></script>
<script type="text/javascript" src="${ctx}/static/js/chart/theme/dark.js"></script>

<style>

.main {
    overflow:hidden;
    height:320px;
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
 <input type="hidden" id="temp"  value="${temp}" >
                         <div class="panel-header panel-header-noborder  " 	style="padding: 5px; height: auto">
	                      
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-arrow-refresh" plain="true" onclick="refresh()">刷新</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
	                       
                            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-tip" plain="true" title="支持多个纵轴指标展示！"></a>  
                            <span class="toolbar-item dialog-tool-separator" ></span>
                             
                                                                       
                         </div> 
                       
  </div>

   <div>
   <div class ="mainChar" style=""> 
      <div class="panel-body" style="padding:5px;" >
      <table class="formTable">
       <tr><td> 
        &nbsp;&nbsp;图表标题名称：<input id="title"  name="title" type="text"  value="图表标题" class="easyui-validatebox"   data-options="width:160" onkeyup="changeTitle()" />	 
       
        &nbsp;&nbsp;横坐标字段：
			 <select id="xcolumn" name="xcolumn"   class="esayui-combobox"   style="width:120px;" data-options="panelHeight:'auto'"    >
			    
			 </select>
					
		     	
	        &nbsp;&nbsp;纵坐标字段：
			  <select id="ycolumn" data-options="multiple:true,panelHeight:'auto'"  class="esayui-combobox" style="width:200px;">
			 
			 </select>
			   
				&nbsp;&nbsp;&nbsp;起始行：<input id="limitForm"  name="limitForm" type="text" value="0"   class="easyui-numberbox"   data-options="width:50,required:'required'"  />
		        &nbsp;&nbsp;行数：<input id="pageSize"  name="pageSize" type="text" value="20"   class="easyui-numberbox"   data-options="width:50,required:'required'"  />
		      
		 </td> 
		 </tr>
		 
		 <!--  
		 <tr>
		   <td> 
		    &nbsp;&nbsp;<input id=""  name="" type="checkbox" value="1"   />
		    图例项为一个字段的数据而非字段名,

              &nbsp;&nbsp;图例项所在字段：
               <select id="operation" name="operation" style="font-size:12px;" class="easyui-validatebox"   data-options="width: 150" onchange="" disabled="disabled" >
				   <option value="0" <c:if test="${dataSynchronize.operation=='0' }"> selected </c:if>  >ddd</option>  
				   <option value="1" <c:if test="${dataSynchronize.operation=='1' }"> selected </c:if>  >sss </option> 
				</select>
              &nbsp;&nbsp; 数据值字段：
                 <select id="operation" name="operation" style="font-size:12px;" class="easyui-validatebox"   data-options="width: 150" onchange="" disabled="disabled" >
				   <option value="0" <c:if test="${dataSynchronize.operation=='0' }"> selected </c:if>  >fff</option>  
				   <option value="1" <c:if test="${dataSynchronize.operation=='1' }"> selected </c:if>  >ggg </option> 
				</select>
		   </td> 
		 </tr>
		 -->
		  <tr>
		   <td align="center"> 		   
		   &nbsp;&nbsp;&nbsp;&nbsp;<input id="name"  name="name" type="button" value="更新图表" onclick="updateData()"   />
		  </td> 
		 </tr>
		 
		 </table>
      </div>
      
    </div>
       <div  id="graphic1" style="height:auto;width:auto;"> 
            <div id="main1" class="main"></div>
       </div> 
    </div>
 
<script type="text/javascript">
 
//var lastData = 11;
//var axisData;
var databaseConfigId;
var databaseName ;
var sql ;
//var questions1=0;
//var questions_qps = 0;

$(function(){ 
     databaseConfigId = $("#databaseConfigId").val();
	 databaseName = $("#databaseName").val();
	 var temp = $("#temp").val();
	 sql = parent.$('#selectDg'+ temp).datagrid('options').queryParams.sql
	// sql = $("#sql").val();
	 
	// alert(sql);
	// alert(temp);
	 updateData();
});

function refresh(){
	 // queryInfoItem();
	 window.location.reload(); //刷新当前页面.
}
 
  var myChart;
  
   // 使用  
  require(  
        [  
            'echarts',  
            'echarts/chart/line',
            'echarts/chart/gauge',
            'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载  
        ],  
        function (ec) {  
            // 基于准备好的dom，初始化echarts图表  
            myChart = ec.init( document.getElementById('main1') );   
            var option = {
             title : {
               text: '图表标题',
               subtext: ''
             },
             tooltip : {
               trigger: 'axis'
              },
              legend: {
                 data:['图例标题']
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
               data :[0,0,0]
             }
            ],
           yAxis : [
            {
                data: [0,0,0]
             }
            ],
           series : [
           {
               name:'VALUE',
               type:'line',
               data:[0,0,0]
            } 
           ]
        };
     
        // 为echarts对象加载数据   
            myChart.setOption(option);   
        }  
   );  
   
    function changeTitle(){
    	var option = {
             title : {
               text: $("#title").val(),
               subtext: ''
             }
    	};
    	myChart.setOption(option); 
    } 
    
    function updateData(){
    	var xValues=[];    // X轴坐标值
        var yValues=[];    // Y轴坐标值 , 多行处理
        var allLegends=[]; //全部标题字段
        var legends=[];    //选中的标题
        var yArray =[];
        var limitForm = $("#limitForm").val();
        var pageSize = $("#pageSize").val();
     //   myChart.clear;
     
    	$.ajax({
		 type:'post',
		 timeout:360000 ,
		 url : "${ctx}/system/graphic/getViewData/"+databaseConfigId,
		 data:{'sql':sql,'databaseName':databaseName ,'limitForm':limitForm ,'pageSize':pageSize },
         success : function(result) {
			// alert(  JSON.stringify( result ));
             var status = result.status ;
             myChart.showLoading();    //数据加载完之前先显示一段简单的loading动画
             if(status == 'success' ){
            	  var rows = result.rows;
            	  //alert(  rows.length );
            	   
            	   //循环数据集，取得字段名
            	  //  for(var i=0;i<rows.length;i++){ 
            	   if(rows.length >0 ){  
                       var oneRows = rows[0];
                       for( var obj in oneRows ){
                           allLegends.push( obj ); // 填入 标题数组,就是数据集的字段名
                       }
            	   }
            	  // alert( allLegends );
            	   
            	   //坐标字段下拉选项，填充字段名
            	   //第一次载入时，X,y轴字段下拉选择框为空,赋值
            	   if(  document.getElementById("xcolumn").length == 0 ){
            	    for(var x=0;x<allLegends.length;x++ ){ 
                    	   $("#xcolumn").append("<option value='"+ allLegends[x] +"'>"+allLegends[x]+"</option>");
                    	   $("#ycolumn").append("<option value='"+ allLegends[x] +"'>"+allLegends[x]+"</option>");
                       }
            	       $("#ycolumn ").get(0).selectedIndex=1;  //设置Select索引值为1的项选中
            	       $('#ycolumn').combobox({});
                   }
            	   
            	   var xCol = $("#xcolumn").val(); //X轴 坐标字段
            	  // var yCol = $("#ycolumn").val(); //Y轴坐标字段
            	   var yCol = $('#ycolumn').combobox('getText'); // Y轴坐标字段
            	   //legends.push( $.trim( yCol ) );
            	   legends =  yCol.split(",");
            	   
            	   //X轴 值 处理
                   for(var i=0;i<rows.length;i++){  
                       var oneRows = rows[i];
                       xValues.push(  rows[i][xCol] );    //X轴 值填入 数组，默认第一列为X横轴
                   }
                    
            	   //Y轴 多个指标 多列值的处理。
                   for(var a=0;a<legends.length;a++){
                	   var name= legends[a];
                	   var data =[];
                       for(var i=0;i<rows.length;i++){  
                           var dataValue = rows[i][name]  //取出y值 ，并填入y数组
                           data.push( dataValue ); 
                       }
                       var json ={};
                       json.name= name;
                       json.type= "line";
                       json.data= data;
                       yArray.push( json );
                      // yArray.push( JSON.stringify( json));
                   }
                  //  alert(  yValues  );
                    myChart.hideLoading();    //隐藏加载动画
                    myChart.setOption({       //加载数据图表
                    	legend: {
                             data:legends
                        },
                    	xAxis: [{
                            data: xValues
                        }],
                        series:yArray
                    });
                    
             }else{
            	 parent.$.messager.show({ title : "提示",msg: result.mess , position: "bottomRight" });
             }
             
        },
         error : function(errorMsg) {
             //请求失败时执行该函数
         alert("图表请求数据失败!");
         myChart.hideLoading();
         }
    	
      });
       
    }
    
</script>
 
</body>
</html>