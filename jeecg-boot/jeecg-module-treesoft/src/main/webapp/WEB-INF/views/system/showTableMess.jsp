<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
 
</head>
<body>

 <div id="tb3" style="padding:5px;height:auto">
                         <div>
	       		           
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-arrow-refresh" plain="true" onclick="refresh()">刷新</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>                        
                           
                         </div> 
                       
  </div>
 <input type="hidden" id="databaseConfigId" value="${databaseConfigId}" >
 <input type="hidden" id="databaseName" value="${databaseName}" >
 <input type="hidden" id="tableName"  value="${tableName}">
<table id="dg3"></table> 
 
 
<script type="text/javascript">

var dgg;
var tableName;
var databaseName;
var databaseConfigId;
$(function(){  
	databaseName = $("#databaseName").val();
	tableName = $("#tableName").val();
	databaseConfigId = $("#databaseConfigId").val();
     initData();
});


function initData(){
	
	dgg=$('#dg3').datagrid({     
	method: "get",
    url: '${ctx}/system/permission/i/viewTableMess/'+tableName+'/'+databaseName+'/'+databaseConfigId, 
    fit : true,
	fitColumns : true,
	border : false,
	idField : 'treeSoftPrimaryKey',
	striped:true,
	pagination:true,
	rownumbers:true,
	pageNumber:1,
	pageSize : 20,
	pageList : [ 10, 20, 30, 40, 50 ],
	singleSelect:false,
    columns:[[    
		{field:'TREESOFTPRIMARYKEY',checkbox:true}, 		  
		{field:'propName',title:'名称', width:30   },		 
      	{field:'propValue',title:'信息',width:50   }        	
    ]], 
    checkOnSelect:true,
    selectOnCheck:true,
    extEditing:false,
    toolbar:'#tb3',
    autoEditing: true,     //该属性启用双击行时自定开启该行的编辑状态
    singleEditing: true
    
  
	}); 	
	 
}
  

function refresh(){
	    $('#dg3').datagrid('reload');
	    $("#dg3").datagrid('clearSelections').datagrid('clearChecked');
   }
</script>

</body>
</html>