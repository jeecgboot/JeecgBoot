<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

</head>
<body>
<div>
              <input type="hidden" id="id"  name="id" />
              <input type="hidden" id="sql3"  name="sql3"  />
              <input type="hidden" id="dbName" name="dbName" />
		<table class="formTable">
			 <tr>
				<td>名称：</td>
				<td><input id="name" name="name" type="text"  class="easyui-validatebox" data-options="width: 300,required:'required'"/></td>
			</tr>
			 
			 <tr>
				<td> </td>
				<td>*请输入便于识别的名称。 </td>
			</tr>
			 
		</table>
</div>

<script type="text/javascript">

   $("#name").val(  $("#searchHistoryName",window.parent.document).val() );
   
   function saveSearchHistory2(){  
	   var id = $("#searchHistoryId",window.parent.document ).val();
	   var sql2 = editor.getValue();
	   var dbName = $("#databaseSelect",window.parent.document).val();
	   var name = $("#name").val();
	   var databaseConfigId =  $('#databaseSelect').val();
	   //alert( sql2 );
	   
	   if(  !sql2   ){
		   $.messager.show({ title : "提示",msg: "请输入SQL语句!" , position: "bottomRight" });
		   return false;
	   } 
	   $("#id").val(id );
	  
       var isValid = $("#mainform").form('validate');
       if( !isValid ){
    	   return isValid;	// 返回false终止表单提交
       }
	   
	  $.ajax({
			        type:'POST',
		          	contentType:'application/json;charset=utf-8',
                    url:"${ctx}/system/permission/i/saveSearchHistory" ,
                    data: JSON.stringify( { 'id':id  ,'sql':sql2 ,'dbName':dbName ,'name':name,'configId':databaseConfigId } ),
                    datatype: "json", 
                   //成功返回之后调用的函数             
                    success:function(data){
                       var status = data.status ;
            	       if(status == 'success' ){
            	            $.messager.show({ title : "提示",msg: data.mess , position: "bottomRight" });
            	            $("#searchHistoryId").val("")
    	                    setTimeout(function () {
                                saveSearch.panel('close');
                            }, 2000);
                            searchBG.treegrid('reload');
            	       }else{
            	    	    $.messager.show({ title : "提示",msg: data.mess , position: "bottomRight" });
            	       }
            	     }  
       });
   }
   
</script>
</body>
</html>