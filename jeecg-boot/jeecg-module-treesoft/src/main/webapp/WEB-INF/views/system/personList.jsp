<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-control" content="no-cache">
<meta http-equiv="Cache" content="no-cache">
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

</head>
<body>

 <div id="tb3" style="padding:5px;height:auto">
                         <div>
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="delButton"   onclick="deletePerson()">删除</a>
	        	           <span class="toolbar-item dialog-tool-separator"></span>
	                       
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-arrow-refresh" plain="true" onclick="refresh()">刷新</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-help" plain="true" title="用户与数据库无关"></a>
	                       
                           <span class="toolbar-item dialog-tool-separator"></span> &nbsp;&nbsp;&nbsp; 
                                                                                       用户名：<input type="text" id="usernameSearch" class="easyui-validatebox" data-options="width:100,prompt: '用户名'"/>
                                &nbsp;姓名：<input type="text" id="realnameSearch" class="easyui-validatebox" data-options="width:100,prompt:'姓名'"/>
                                 <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-hamburg-search2" plain="true" onclick="personQuery()">查询</a>
                         </div> 
                       
  </div>
  <div id="dlgg" ></div>  
 
 <table id="dgForPerson"></table> 
<script type="text/javascript">
var dgg;
var person;
$(function(){  
    initData();
});

function personQuery(){
	 initData();
}

function initData(){
	dgg=$("#dgForPerson").datagrid({     
	method: "post",
    url: '${ctx}/system/person/i/personList?v=' + Math.random(), 
    fit : true,
	fitColumns : true,
	border : false,
	idField : 'id',
	striped:true,
	pagination:true,
	rownumbers:true,
	pageNumber:1,
	pageSize : 20,
	pageList : [ 10, 20, 30, 40, 50 ],
	singleSelect:false,
	queryParams:{ username:$("#usernameSearch").val(),realname:$("#realnameSearch").val() },
    columns:[[    
	  	{field:'TREESOFTPRIMARYKEY',checkbox:true}, 
	  	{field:'id',title:'操作',align:'center',halign:'center',width:100,sortable:true,formatter: function(value,row,index){
			  return '<a  class="l-btn l-btn-small" href="javascript:editPersonFormOne(\'' +row["id"]+  '\')" ><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text" style="line-height: 18px;">修改</span><span class="l-btn-icon icon-edit">&nbsp;</span></span></a>  ';
		}},
	  	{field:'username',title:'用户名',halign:'center',width:100 },
	  	{field:'realname',title:'姓名',halign:'center',sortable:true,width:100 },
	  	{field:'role',title:'角色',halign:'center',width:80,sortable:true,formatter: function(value,row,index){
				if (row.role=='0'){
					return '普通用户';
				}else if (row.role=='1'){
					return '管理员';
				} else {
					return '审计员';
				}
		}},
        {field:'status',title:'状态',halign:'center',width:60,sortable:true,formatter: function(value,row,index){
				if (row.status){
					return '启用';
				} else {
					return '<font color="red">冻结</font>';
				}
		}},
		{field:'expiration',title:'有效期至',halign:'center',width:90 }
    ] ], 
    checkOnSelect:true,
    selectOnCheck:true,
    extEditing:false,
    toolbar:'#tb3',
    autoEditing: false,     //该属性启用双击行时自定开启该行的编辑状态
    singleEditing: false,
    onLoadSuccess: function (data) {
    	if( data.status=='fail'){
    		 parent.$.messager.show({ title : "提示",msg: data.mess , position: "center" });
    	}
    },
    onDblClickRow: function (rowIndex, rowData) {  
    	var id =rowData.id  ;
	    person = $("#dlgg").dialog({   
	    title: '查看',    
	    width: 380,    
	    height: 450,    
	    href:'${ctx}/system/person/i/editPersonForm/'+id,
	    maximizable:true,
	    modal:true,
	    buttons:[
	    	 
	    	{
			text:'取消',
			iconCls:'icon-cancel',
			handler:function(){
					person.panel('close');
				}
		 }]
	  });
    }
   }); 
  }

 //打开 新增 编辑 对话框
   function addPersonForm(){
	    person = $("#dlgg").dialog({   
	    title: ' 新增',    
	    width: 380,    
	    height: 450,    
	    href:'${ctx}/system/person/i/addPersonForm',
	    maximizable:true,
	    modal:true,
	    buttons:[ 
	    	{
			text:'保存',
			iconCls:'icon-ok',
			handler:function(){
	    		addUpdatePerson();
				//$("#mainform").submit(); 
			}
		},{
			text:'取消',
			iconCls:'icon-cancel',
			handler:function(){
					person.panel('close');
				}
		 }]
	  });
  }
  
 function editPersonFormOne(id){
	 person= $("#dlgg").dialog({   
	    title: '修改',    
	    width: 380,    
	    height: 450,    
	    href:'${ctx}/system/person/i/editPersonForm/'+id,
	    maximizable:true,
	    modal:true,
	    buttons:[ 
	    	{
			text:'保存',
			iconCls:'icon-ok',
			handler:function(){
	    		addUpdatePerson();
				// $("#mainform").submit(); 
			}
		},{
			text:'取消',
			iconCls:'icon-cancel',
			handler:function(){
					person.panel('close');
				}
		}]
	});
 }
 
 function editPersonDBConfig(id){
	    person= $("#dlgg").dialog({   
	    title:'&nbsp;&nbsp;数据库范围配置',    
	    width:650,    
	    height:450,    
	    href:'${ctx}/system/person/configListSelect/'+id,
	    maximizable:true,
	    modal:true,
	    buttons:[ 
	    	{
			text:'保存',
			iconCls:'icon-ok',
			handler:function(){
	    		savePersonConfigSelect();
			}
		},{
			text:'取消',
			iconCls:'icon-cancel',
			handler:function(){
					person.panel('close');
				}
		}]
	});
 }
 
 function editPersonForm(){
	var checkedItems = $("#dgForPerson").datagrid('getChecked');
	if(checkedItems.length == 0 ){
	      parent.$.messager.show({ title : "提示",msg: "请先选择一行数据！", position: "bottomRight" });
		  return;
	 }
	    
	 if(checkedItems.length >1 ){
	      parent.$.messager.show({ title : "提示",msg: "请选择一行数据！", position: "bottomRight" });
		  return;
	 }
	 
	 var id = checkedItems[0]['id']  ;
	 person= $("#dlgg").dialog({   
	    title: '修改',    
	    width: 380,    
	    height: 450,    
	    href:'${ctx}/system/person/i/editPersonForm/'+id,
	    maximizable:true,
	    modal:true,
	    buttons:[ 
	    	{
			text:'保存',
			iconCls:'icon-ok',
			handler:function(){
	    		addUpdatePerson();
				// $("#mainform").submit(); 
			}
		},{
			text:'取消',
			iconCls:'icon-cancel',
			handler:function(){
					person.panel('close');
				}
		}]
	});
 }
 
 
   //重置密码  
   function reset( id ){
	  var ids = [];
      ids.push( id );
	  $.ajax({
			    type:'POST',
		       	contentType:'application/json;charset=utf-8',
                url:"${ctx}/system/person/i/resetPersonPass",
                data: JSON.stringify( { 'ids':ids } ),
                datatype: "json", 
               //成功返回之后调用的函数             
                success:function(data){
                   var status = data.status ;
            	   if(status == 'success' ){
            	      parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
            	   }else{
            	      parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight",timeout:2000,icon:"warning" });
            	  }
            }  
       });
   }
	 
 
   //删除  
   function deletePerson(){
	 
	  var checkedItems = $("#dgForPerson").datagrid('getChecked');
	  var length = checkedItems.length;
	  
	  var data2=$("#dgForPerson").datagrid('getData');
	  var totalLength = data2.total;
	  
	  // alert('总数据量:'+data.total)
	  
	   if(totalLength - length <= 0 ){
		 parent.$.messager.show({ title : "提示",msg: "必须保留一行用户信息！", position: "bottomRight" });
		 return ;
	  }
	  
	  if(checkedItems.length == 0 ){
		 parent.$.messager.show({ title : "提示",msg: "请先选择一行数据！", position: "bottomRight" });
		 return ;
	  }
	 
	  var ids = [];
      $.each( checkedItems, function(index, item){
    	  ids.push( item["id"] );
     }); 
       
	 $.easyui.messager.confirm("操作提示", "您确定要删除"+length+"行数据吗？", function (c) {
                if (c) {
                	
                   $.ajax({
			        type:'POST',
		          	contentType:'application/json;charset=utf-8',
                    url:"${ctx}/system/person/i/deletePerson",
                    data: JSON.stringify( { 'ids':ids } ),
                    datatype: "json", 
                   //成功返回之后调用的函数             
                    success:function(data){
                       var status = data.status ;
            	       if(status == 'success' ){
            	    	   $("#dgForPerson").datagrid('reload');
            	    	   $("#dgForPerson").datagrid('clearSelections').datagrid('clearChecked');
            	            parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight" });
            	       }else{
            	    	    parent.$.messager.show({ title : "提示",msg: data.mess, position: "bottomRight",timeout:2000,icon:"warning" });
            	       }
            	     }  
                   });
                }
            });
   }
 
   function refresh(){
	    $("#dgForPerson").datagrid('reload');
	    $("#dgForPerson").datagrid('clearSelections').datagrid('clearChecked');
   }
   
   function register(){
	  $("#dlgg").dialog({  
	    title: "注册",    
	    width: 380,    
	    height: 240,  
	    href:"${ctx}/system/person/i/register" ,
	    maximizable:true,
	    modal:true,
	     buttons:[ 
	    	{
			text:'保存',
			iconCls:'icon-ok',
			handler:function(){
	    		registerUpdate();				 
			}
		},{
			text:'取消',
			iconCls:'icon-cancel',
			handler:function(){
				$("#dlgg").panel('close');
			}
		}]
	 });
   }     
</script>

</body>
</html>