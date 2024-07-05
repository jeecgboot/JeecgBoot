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
<script type="text/javascript" src="${ctx}/static/js/chart/pie.js"></script>
<script type="text/javascript" src="${ctx}/static/js/chart/gauge.js"></script>
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
        
        <span style="font-size:23px;color:#fff;" id="max_used_connections" title="max_used_connections" >0 </span>  <br>
        <span class="l-monitor-title-text">最大连接数 </span>
      </div>
      
      
      <div  style="margin:5px;padding:2px;height:auto;width:17%;border:1px solid #95B8E7;float:left; text-align:center;background:#00A65A">
         
          <span style="font-size:23px;color:#fff;" id="Uptime">0 </span> <br>
           <span class="l-monitor-title-text">Uptime(day)</span> 
     </div>
     
     <div  style="margin:5px;padding:2px;height:auto;width:17%;border:1px solid #95B8E7;float:left; text-align:center;background:#F39C12">
         <span style="font-size:23px;color:#fff;" id="Threads_connected" title="Threads_connected">0 </span> <br> 
         <span class="l-monitor-title-text">当前连接数</span> 
     </div>
     
     <div  style="margin:5px;padding:2px;height:auto;width:17%;border:1px solid #95B8E7;float:left; text-align:center;background:#DD4B39" >
        
         <span style="font-size:23px;color:#fff;" id="Threads_running" title="Threads_running">0 </span> <br>
          <span class="l-monitor-title-text">running </span> 
     </div>
     <div  style="margin:5px;padding:2px;height:auto;width:17%;border:1px solid #95B8E7;float:left; text-align:center;background:#3C8DBC">
          
          <span style="font-size:23px;color:#fff;" id="Open_tables">0 </span> <br>
          <span class="l-monitor-title-text">Open_tables </span> 
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
            <div id="main4" class="main" style="float:left;width:46%;"></div>
            <div id="main5" class="main" style="float:right;width:46%;"></div>
      </div> 
           
    </div>
  <script type="text/javascript" src="${ctx}/static/js/monitorMySql.js"> </script> 
</body>
</html>