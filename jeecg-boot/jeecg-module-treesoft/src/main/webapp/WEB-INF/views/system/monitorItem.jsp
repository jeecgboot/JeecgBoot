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

 <div id="tb5" style="padding:5px;height:auto">
                         <div>
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-arrow-refresh" plain="true" onclick="refresh()">刷新</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
                         </div> 
  </div>
 <input type="hidden" id="databaseName" value="${databaseName}" >
 <input type="hidden" id="databaseConfigId" value="${databaseConfigId}" >
 <table id="dg5"></table> 
 
<script type="text/javascript">

var dgg5;
var databaseName;
var databaseConfigId;

$(function(){  
	databaseConfigId = $("#databaseConfigId").val();
	databaseName = $("#databaseName").val();
    initData2();
});

function initData2(){
	 
	dgg5=$('#dg5').datagrid({     
	method: "get",
    url:"${ctx}/system/permission/i/monitorItemValue/"+ databaseName+"/"+databaseConfigId ,
    fit : true,
	fitColumns : true,
	border : false,
	idField : 'fileName',
	striped:true,
	pagination:true,
	rownumbers:true,
	pageNumber:1,
	pageSize : 20,
	pageList : [ 10, 20, 30, 40, 50 ],
	singleSelect:false,
    columns:[[  
	    {field:'TREESOFTPRIMARYKEY',checkbox:true}, 
	  	{field:'Variable_name',title:'参数',sortable:true,width:100 },
        {field:'Value',title:'值',sortable:true,width:100},
        {field:'descript',title:'参数说明',sortable:true,width:100}	  	
    ]], 
    checkOnSelect:true,
    selectOnCheck:true,
    extEditing:false,
    toolbar:'#tb5',
    autoEditing: false,     //该属性启用双击行时自定开启该行的编辑状态
    singleEditing: false
   }); 
  }
  
   function refresh(){
	    $('#dg5').datagrid('reload');
	    $("#dg5").datagrid('clearSelections').datagrid('clearChecked');
   }
     
</script>

</body>
</html>