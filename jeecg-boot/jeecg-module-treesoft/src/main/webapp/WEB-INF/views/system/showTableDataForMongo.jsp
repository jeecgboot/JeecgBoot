<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctx}/static/plugins/easyui/jeasyui-extensions/jeasyui.extensions.datatime.js" type="text/javascript"></script>
</head>

<body>
 <div id="tb2" style="padding:5px;height:auto">
                         <div>
                         
	       		           <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-table-row-insert" plain="true" id="addRowButton"  onclick="addRow2()"> 添加 </a>
	       		           <span class="toolbar-item dialog-tool-separator"></span>
	       		           
	       		            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-table-edit" plain="true" id="copyRowButton" onclick="copyRow()">复制</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
	                       	       		           
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-table-row-delete" plain="true" id="delButton"   onclick="del()">删除</a>
	        	           <span class="toolbar-item dialog-tool-separator"></span>
	        	           <!--  
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-table-edit" plain="true" id="editRowButton" onclick="editRow2()">修改</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
	                       -->
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-sitemap" plain="true" id="jsonShowButton" onclick="treeShow()" title="树状展示，JSON格式化">树状展示</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
	                        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-table-save" plain="true" id="exportDataToSQLButton" onclick="exportDataToSQL()" title="导出JSON">导出</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
	                       
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-arrow-refresh" id="refreshButton" plain="true" onclick="refresh()">刷新</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
                        
                           <a href="javascript:void(0)" class="easyui-linkbutton"  plain="true"  >&nbsp;</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
                        
                            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" plain="true" id="saveRowButton"  onclick="saveRow()"> 保存 </a>
	       		           <span class="toolbar-item dialog-tool-separator"></span>
                          
                           <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" id="cancelButton"  onclick="cancelChange()"> 取消 </a>
	       		           <span class="toolbar-item dialog-tool-separator"></span>
                        
                            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-tip" plain="true" title="可双击行编辑数据. &#13;钩选行进行复制."></a>
                          
                         </div> 
                       
  </div>
 <input type="hidden" id="databaseConfigId" value="${databaseConfigId}" >
 <input type="hidden" id="databaseName"    value="${databaseName}" >
 <input type="hidden" id="tableName"       value="${tableName}">
 <input type="hidden" id="objectType"      value="${objectType}">
<table id="dg2"></table> 

<div id="dlg2"></div>  
<div id="dlgg"   ></div>  
<div id="addRow" ></div>
 
<form id="form2" method="post"  action="${ctx}/system/permission/i/exportDataToSQL/${databaseConfigId}"  style="display:none"   >
   <input type="hidden" id="databaseName" name="databaseName" value="${databaseName}" >
   <input type="hidden" id="tableName"    name="tableName" value="${tableName}">
   <input type="hidden" id="checkedItems" name="checkedItems" >
   <input type="hidden" id="primary_key"  name="primary_key" >
</form>
  <script type="text/javascript" src="${ctx}/static/js/showTableDataForMongo.js"> </script> 
</body>
</html>