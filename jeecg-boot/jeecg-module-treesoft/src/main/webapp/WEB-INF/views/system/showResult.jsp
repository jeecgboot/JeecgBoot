<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title> delete </title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
 
</head>
<body>
 <div id="tb" style="padding:5px;height:auto">
                         <div>
	       		           <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="addRowButton"  onclick="addRow()"> 添加 </a>
	       		           <span class="toolbar-item dialog-tool-separator"></span>
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="delButton"   onclick="del()">删除</a>
	        	           <span class="toolbar-item dialog-tool-separator"></span>
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" id="editRowButton" onclick="editRow()">修改</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
	                       
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-arrow-refresh" plain="true" onclick="refresh()">刷新</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
                         </div> 
                       
  </div>
<table id="selectDg"></table> 
 <input type="hidden" id="sql"  value="${sql}">
 <input type="hidden" id="sqlIndex"  value="${sqlIndex }">

<script type="text/javascript">
 
var selectRowCount = 0;
//var columnsTemp = new Array();
var databaseName;
 
var sqlArray = new Array();
var sql;
  
$(function(){
	
	var sqls = $.trim(  $("#sqltextarea",window.parent.document).val()  );
	var arry = sqls.split(";");//以换行符为分隔符将内容分割成数组
	var tempStr;
	for (var i = 0; i < arry.length; i++) {  
		tempStr = $.trim( arry[i] );
       
        //判断注释的行 
        if( tempStr.indexOf("--")==0  ){
        	continue;
        }
        if( tempStr.indexOf("/*")==0  ){
        	continue;
        }
        if( tempStr == "" || tempStr == null ){
        	continue;
        }
        sqlArray.push( tempStr );
     }   
	
	  sql =  sqlArray[ $("#sqlIndex").val() ];
	  databaseName = $("#databaseSelect",window.parent.document).val();
	  query();
});
 

//查询方法  
function query() {  
    var url = "${ctx}/system/permission/i/executeSqlTest/"+databaseConfigId;
    $.post(url, {'sql':sql,'dbName':databaseName},showGrid,"json");  
}  

 //处理返回结果，并显示数据表格  
function showGrid( data ) {  
	// alert( data.columns );
	
    if (data.rows.length == 0) {  
        $.messager.alert("结果", "没有数据!", "info", null);  
    }  
    var options = {  
        url:"${ctx}/system/permission/i/executeSqlTest/"+databaseConfigId,
        method: "POST",
        queryParams: { 'sql':sql , 'dbName': databaseName },
        rownumbers: true ,
        fit : true,
	    fitColumns : true,
	    border : false,
	    striped:true,
	    pagination:false,
	    pageNumber:1,
	    pageSize : 20,
	    pageList : [ 10, 20, 30, 40, 50 ],
	    singleSelect:false,
        checkOnSelect:true,
        selectOnCheck:true,
        toolbar:'#tb2',
    
        onSelect:function(index, row){
    	   selectRowCount++;
    	  // alert( selectRowCount );
    	  //修改按钮只有选一行时 才有效
	      if(selectRowCount == 1){
		    $("#editRowButton").linkbutton("enable"); 
	      }else{
	        $("#editRowButton").linkbutton("disable"); 
	      }
       },
   
        onUnselect:function(index, row){
	      selectRowCount--;
	     // alert( selectRowCount );
	  	  //修改按钮只有选一行时 才有效
	     if(selectRowCount == 1){
		    $("#editRowButton").linkbutton("enable"); 
	     }else{
	        $("#editRowButton").linkbutton("disable"); 
	     }
      } 
    };  
    options.columns = eval(data.columns);//把返回的数组字符串转为对象，并赋于datagrid的column属性  
   // options.idField = data.primaryKey ;
   // primary_key = data.primaryKey ;
    var dataGrid = $("#selectDg");  
    dataGrid.datagrid(options);//根据配置选项，生成datagrid  
    dataGrid.datagrid("loadData", data.rows); //载入本地json格式的数据  
    
     $('#selectDg').datagrid('unselectAll');
	 $("#delButton").linkbutton("disable"); 
	 $("#editRowButton").linkbutton("disable");
	 $("#addRowButton").linkbutton("disable");
}  


   function refresh(){
	    $('#selectDg').datagrid('reload');
   }

</script>
</body>
</html>