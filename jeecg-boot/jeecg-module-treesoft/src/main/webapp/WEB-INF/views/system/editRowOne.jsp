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
<div>
	<form id="mainform" action="${ctx }/system/permission/i/updateRows" method="POST">
		<table class="formTable">
		<input type="hidden" name="databaseName" value="${databaseName}" >
		<input type="hidden" name="tableName" value="${tableName}" >
		<input type="hidden" name="id" value="${id}" >
		<input type="hidden" name="idValues" value="${idValues}" >
		<input type="hidden" name="databaseConfigId" value="${databaseConfigId}" >
		   <c:forEach var="entity" items="${listAllColumn}" >
		   
		    <c:choose>
               <c:when test="${entity.data_type == 'DATETIME' }" >  
                   <tr>
				     <td>  <c:out value="${entity.column_name }"/>：   </td>
				     <td><input name="${entity.column_name }" type="text" value="${entity.column_value }"   class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss"  style="width:150"  <c:if test="${entity.is_nullable == 0 }"> data-options=" required:'required'" </c:if>  /></td>
			       </tr>
			       
               </c:when>
               <c:when test="${ entity.data_type == 'INT' || entity.data_type == 'SMALLINT' }" > 
                  <tr>
				    <td>   <c:out value="${entity.column_name }"/>：     </td>
				    <td><input name="${entity.column_name }" type="text" value="${entity.column_value }"  class="easyui-numberbox" style="width:150"  <c:if test="${entity.is_nullable == 0 }"> data-options=" required:'required'" </c:if> /></td>
			      </tr>
               </c:when>
               
                <c:when test="${ entity.data_type == 'CHAR' }" > 
                  <tr>
				    <td>   <c:out value="${entity.column_name }"/>：     </td>
				    <td><input name="${entity.column_name }" type="text" value="${entity.column_value }"  class="easyui-validatebox" style="width:150"    data-options="  <c:if test="${entity.is_nullable == 0 }">  required:true , </c:if>  validType:'length[1,1]'  "    /> </td>
			      </tr>
               </c:when>
               
                <c:otherwise> 
                   <tr>
				    <td>   <c:out value="${entity.column_name }"/>：   </td>
				    <td><input name="${entity.column_name }" type="text" value="${entity.column_value }"  class="easyui-validatebox" style="width:150"  <c:if test="${entity.is_nullable == 0 }"> data-options=" required:'required'" </c:if> /></td>
			      </tr>
                   
              </c:otherwise>
            </c:choose>
			 
		   </c:forEach>
			 
		</table>
	</form>
</div>
 
<script type="text/javascript">

//提交表单
$('#mainform').form({    
    onSubmit: function(){    
    	var isValid = $(this).form('validate');
		return isValid;	// 返回false终止表单提交
    },    
    success:function(data){   
    	//successTip(data,dg,d);
    	// parent.$.messager.show({ title : "提示",msg: "修改成功！", position: "bottomRight" });
    	// setTimeout(function () {
       //     parent.add.panel('close');
       //   }, 2000);
    	
    	 
    	var dataObj=eval("("+data+")");//转换为json对象
    	var status = dataObj.status ;
    	 
    	 if(status == 'success' ){
              parent.$.messager.show({ title : "提示",msg:  dataObj.mess, position: "bottomRight" });
          }else{
               parent.$.messager.alert("提示", dataObj.mess ,"error");
          }
    	 
    	 setTimeout(function () {
            parent.add.panel('close');
          }, 2000);
    	 parent.d.datagrid('reload');
    	 
     }    
});    
</script>

</body>
</html>