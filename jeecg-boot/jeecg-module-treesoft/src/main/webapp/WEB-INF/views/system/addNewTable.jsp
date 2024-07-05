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
                          
	       		           <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="addRowButton"  onclick="addRow2()"> 添加</a>
	       		           <span class="toolbar-item dialog-tool-separator"></span>
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="delButton"   onclick="deleteTableColumn()">删除</a>
	        	           <span class="toolbar-item dialog-tool-separator"></span>
	        	           <%--
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" id="editRowButton" onclick="editRow2()">修改</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
	                       --%>
	                       
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-up" plain="true" id="editRowButton" onclick="MoveUp()">上移</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-down" plain="true" id="editRowButton" onclick="MoveDown()">下移</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
	                      
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-arrow-refresh" plain="true" onclick="refresh()">刷新</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
                         
                           <a href="javascript:void(0)" class="easyui-linkbutton"  plain="true"  >&nbsp;</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
                         
                           <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" plain="true" id="editRowButton" onclick="openWindow()">保存</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" id="editRowButton" onclick="cancelChange()">取消</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
	                      
                         </div> 
                       
  </div>
  
  <div id="win" class="easyui-window" title="新增表名" style="width:300px;height:150px;"  data-options="iconCls:'icon-save',modal:true, closed:true,maximizable:false,minimizable:false,collapsible:false " > 
        <div style="padding:5px;text-align:center;">  
         <p>表名: <input type="text" id="tableNameStr"></p>  
        </div>    
        <div style="padding:5px;text-align:center;">  
            <a href="#" class="easyui-linkbutton" icon="icon-ok"   onclick="saveRow()" >确定</a>  
            <a href="#" class="easyui-linkbutton" icon="icon-cancel" onclick="closeWindow()">取消</a>  
        </div>  
</div>  
  
 <input type="hidden" id="databaseName" value="${databaseName}" >
 <input type="hidden" id="tableName"  value="${tableName}">
 <input type="hidden" id="databaseConfigId"  value="${databaseConfigId}">
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
    addRow2();
});

function initData(){
	var ISNULLABLE = new Array();
	ISNULLABLE.push( { "value": "YES", "text": "YES" }   );
	ISNULLABLE.push( { "value": "NO", "text": "NO" }   );
	
	var ISPRIMARY = new Array();
	ISPRIMARY.push( { "value": "", "text": "" }   );
	ISPRIMARY.push( { "value": "PRI", "text": "YES" }   );
	ISPRIMARY.push( { "value": "NO", "text": "NO" }   );
	
	var Address = new Array();
	//Address.push( { "value": "varchar", "text": "varchar" }  );
	//Address.push( { "value": "int", "text": "int" }   );
	//Address.push( { "value": "datetime", "text": "datetime" }   );
	 
	
	Address.push( { "value": "char", "text": "char" }   );
    Address.push( { "value": "binary", "text": "binary" }   );
    Address.push( { "value": "bigint", "text": "bigint" }   );
    Address.push( { "value": "bit", "text": "bit" }   );
    Address.push( { "value": "blob", "text": "blob" }   );
    Address.push( { "value": "date", "text": "date" }   );
    Address.push( { "value": "datetime", "text": "datetime" }   );
    Address.push( { "value": "double", "text": "double" }   );
    Address.push( { "value": "decimal", "text": "decimal" }   );
    
    Address.push( { "value": "enum", "text": "enum" }   );
    Address.push( { "value": "float", "text": "float" }   );
    Address.push( { "value": "int", "text": "int" }   );
	Address.push( { "value": "integer", "text": "integer" }   );
	Address.push( { "value": "longtext", "text": "longtext" }   );
    Address.push( { "value": "longblob", "text": "longblob" }   );
    Address.push( { "value": "mediumint", "text": "mediumint" }   );
    Address.push( { "value": "mediumblob", "text": "mediumblob" }   );
    Address.push( { "value": "mediumtext", "text": "mediumtext" }   );
    Address.push( { "value": "number", "text": "number" }   );
    Address.push( { "value": "numeric", "text": "numeric" }   );
   
    Address.push( { "value": "real", "text": "real" }   );
    Address.push( { "value": "set", "text": "set" }   );
    Address.push( { "value": "smallint", "text": "smallint" }   );
    Address.push( { "value": "tinytext", "text": "tinytext" }   );
    Address.push( { "value": "text", "text": "text" }   );
    Address.push( { "value": "tinyblob", "text": "tinyblob" }   );
    Address.push( { "value": "timestamp", "text": "timestamp" }   );
    Address.push( { "value": "time", "text": "time" }   );
    Address.push( { "value": "varchar", "text": "varchar" }   );
    Address.push( { "value": "varchar2", "text": "varchar2" }   );
    Address.push( { "value": "varbinary", "text": "varbinary" }   );
    Address.push( { "value": "year", "text": "year" }   );
	
	dgg=$('#dg3').datagrid({     
	method: "get",
    url: null, 
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
		{field:'COLUMN_NAME',title:'字段名',sortable:true,width:30 , editor: { type: 'text', options: { required: true }}  },
		{field:'DATA_TYPE',title:'类型',sortable:true,width:30,editor: { type: 'combobox', options: { data: Address, valueField: "value", textField: "text" } }    },
      	{field:'CHARACTER_MAXIMUM_LENGTH',title:'长度',sortable:true,width:30, editor:'numberbox'},
      	{field:'COLUMN_COMMENT',title:'注释',width:50 , sortable:true, editor:'text'},
        {field:'IS_NULLABLE',title:'允许空值',  width:10, editor:{  type:'checkbox', options:{ on: 'YES',off: '', checked:'checked' } } }, 
      	{field:'COLUMN_KEY',title:'主键设置', width:10,editor:{  type:'checkbox',  options:{ on: 'PRI',off: '' } }  }
      	
    ]], 
    checkOnSelect:true,
    selectOnCheck:true,
    extEditing:false,
    toolbar:'#tb3',
    autoEditing: true,     //该属性启用双击行时自定开启该行的编辑状态
    singleEditing: true,
    onAfterEdit:function(rowIndex, rowData, changes){
        	//alert("edit");
    	  // submitUpdate(rowData);
    },
    onDblClickCell: function(index,field,value){
		  $(this).datagrid('beginEdit', index);
		  var ed = $(this).datagrid('getEditor', {index:index,field:field});
		  $(ed.target).focus();
	  } 
	}); 
}

function addRow2(){
	var data=$('#dg3').datagrid('getData');
	var length =  data.rows.length;
	//alert( length);
	$('#dg3').datagrid('insertRow',{
	 //  index: 0,	// 索引从0开始
	   index: length,	// 索引从0开始
	  row: { }
    });
	//  $('#dg3').datagrid("beginEdit",0 );
	 $('#dg3').datagrid("beginEdit", length );
   }

 //编辑 一行数据 delete
   function editRow2(){
	    
	   var checkedItems = $('#dg3').datagrid('getChecked');
	    
	   if(checkedItems.length == 0 ){
	    	parent.$.messager.show({ title : "提示",msg: "请先选择一行数据！", position: "bottomRight" });
		    return;
	   }
	    
	   $.each(checkedItems, function(index, item){
               var  index = $('#dg3').datagrid("getRowIndex", item );
               $('#dg3').datagrid("beginEdit",index );
      }); 
	 
   }

 
 function closeWindow(){
	 $('#win').window('close');  
 }
 
 
 function openWindow(){
	 $('#win').window('open');  
 }
 
 //保存新增.
   function saveRow(){  
	   endEdit()
	   var sstr  = $('#tableNameStr').val();
        
       if ( sstr==null  ) {
    	   parent.$.messager.show({ title : "提示",msg: "请输入表名！", position: "bottomRight" });
    	   return;
       }
	   
	   var effectRow = new Object();
	   
	   effectRow["databaseName"] = databaseName;
	   effectRow["tableName"] = sstr;
       
       var  allData=$('#dg3').datagrid('getData');
       
      // alert(  JSON.stringify(allData) );
       effectRow["inserted"] = JSON.stringify(allData);
       
       $.post("${ctx}/system/permission/i/saveNewTable/"+databaseConfigId, effectRow, function(rsp) {
                            if(rsp.status =="success"){
                            	//$('#dg3').datagrid('acceptChanges');
                            	parent.$.messager.show({ title : "提示",msg: "保存成功！", position: "bottomRight" });
                                //$.messager.alert("提示", "保存成功！");
                               // $dg.datagrid('acceptChanges');
                               // endEdit();
                               // cancelChange();
                                 $('#win').window('close');  
                            }else{
                            	 $.messager.alert("提示", rsp.mess );
                            }
                        }, "JSON").error(function() {
                            $.messager.alert("提示", "提交错误了！");
       });
       
   }

 
 
 
  function MoveUp() {
    var row = $("#dg3").datagrid('getSelected');
    var index = $("#dg3").datagrid('getRowIndex', row);
    mysort(index, 'up', 'dg3');
  }
  
  //下移
  function MoveDown() {
    var row = $("#dg3").datagrid('getSelected');
    var index = $("#dg3").datagrid('getRowIndex', row);
    mysort(index, 'down', 'dg3');
     
  }

  function mysort(index, type, gridname) {
    if ("up" == type) {
        if (index != 0) {
            var toup = $('#' + gridname).datagrid('getData').rows[index];
            var todown = $('#' + gridname).datagrid('getData').rows[index - 1];
            $('#' + gridname).datagrid('getData').rows[index] = todown;
            $('#' + gridname).datagrid('getData').rows[index - 1] = toup;
            $('#' + gridname).datagrid('refreshRow', index);
            $('#' + gridname).datagrid('refreshRow', index - 1);
            
            $('#' + gridname).datagrid('unselectRow', index );
            $('#' + gridname).datagrid('selectRow', index - 1);
            
           // var column_name =  toup.treeSoftPrimaryKey;
           // var column_name2 = "";
           // if(index >1){
           // 	var cunn = $('#' + gridname).datagrid('getData').rows[index - 2];
           // 	column_name2 = cunn.treeSoftPrimaryKey;
           // }
            
          //  dosort( column_name, column_name2);
        }
    } else if ("down" == type) {
        var rows = $('#' + gridname).datagrid('getRows').length;
        if (index != rows - 1) {
            var todown = $('#' + gridname).datagrid('getData').rows[index];
            var toup = $('#' + gridname).datagrid('getData').rows[index + 1];
            $('#' + gridname).datagrid('getData').rows[index + 1] = todown;
            $('#' + gridname).datagrid('getData').rows[index] = toup;
            $('#' + gridname).datagrid('refreshRow', index);
            $('#' + gridname).datagrid('refreshRow', index + 1);
            
            $('#' + gridname).datagrid('unselectRow', index );
            $('#' + gridname).datagrid('selectRow', index + 1);
            
          //  var column_name =  todown.treeSoftPrimaryKey;
           // var column_name2 = toup.treeSoftPrimaryKey;   
            
           // dosort( column_name, column_name2);
        }
    }
  }

   function refresh(){
	    $('#dg3').datagrid('reload');
	    $("#dg3").datagrid('clearSelections').datagrid('clearChecked');
   }
 

   //取消 修改
   function  cancelChange(){
	 endEdit();
	 refresh();
   }

   //关闭编辑
   function endEdit(){
	     var rows = $('#dg3').datagrid('getRows');
         for ( var i = 0; i < rows.length; i++) {
            $('#dg3').datagrid('endEdit', i);
         }
     }

   //删除表字段
   function deleteTableColumn(){
	 
	  var checkedItems = $('#dg3').datagrid('getChecked');
	  var length = checkedItems.length;
	  
	  if(checkedItems.length == 0 ){
		 parent.$.messager.show({ title : "提示",msg: "请先选择一行数据！", position: "bottomRight" });
		 return ;
	  }
	 
	 $.easyui.messager.confirm("操作提示", "您确定要删除"+length+"行数据？", function (c) {
                if (c) {
                	
                	var rows = $('#dg3').datagrid("getSelections"); 
                    var copyRows = [];
                    for ( var j= 0; j < rows.length; j++) {
                       copyRows.push(rows[j]);
        			 }
                     for(var i =0;i<copyRows.length;i++){    
                          var index = $('#dg3').datagrid('getRowIndex',copyRows[i]);
                           $('#dg3').datagrid('deleteRow',index); 
                     }
                     
                     parent.$.messager.show({ title : "提示",msg: "删除成功！", position: "bottomRight" });
                	}
            });
   }
   
</script>

</body>
</html>