<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>person</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>

</head>
<body>
<div>
	<form id="mainform" action="${ctx}/system/person/i/personUpdate" method="post">
	   <input id="id" name="id" type="hidden" value="${person.id }"   />
		<table class="formTable">
			 <tr>
				<td>用 户 名：</td>
				<td>${person.username}</td>
		      </tr>
		      
		      <tr>
				<td>用户姓名：</td>
				<td>${person.realname}</td>
		      </tr>
			 
			  <tr>
				<td>用户角色： </td>
				<td>
				<select id="role" name="role" class="esayui-combobox" style="width:200px;"    >
				   <option value="0" <c:if test="${person.role=='0' }"> selected </c:if> >普通用户</option>  
				   <option value="1" <c:if test="${person.role=='1' }"> selected </c:if> >管理员</option> 
				   <option value="2" <c:if test="${person.role=='2' }"> selected </c:if> >审计员</option> 
				   <option value="3" <c:if test="${person.role=='3' }"> selected </c:if> >临时人员</option> 
				   <option value="4" <c:if test="${person.role=='4' }"> selected </c:if> >其他人员</option> 
				</select>
				
				</td>
			  </tr>
			 
			 <tr>
				<td>有效期至：</td>
				<td>
				   <input type="text" id="expiration" name="expiration" class="easyui-datetimebox"  value="${person.expiration}"   data-options="width:200,prompt: '截止日期'"/>
				   <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-help" plain="true" title="为空时将不限制访问截止日期."></a>
		        </td>
		     </tr>
		     
			 <tr>
				<td>备注说明：</td>
				<td>
				<textarea id="note"  name="note"  class="easyui-validatebox"   data-options="width:200" >${person.note }</textarea>
				</td>
		      </tr>
		      <tr>
				<td> &nbsp; </td>
				<td> &nbsp; </td>
		      </tr>
			 <tr>
			   <td>功能权限：</td>
			   <td>
			     <input type="checkbox" id="permission" name="permission" value="ordersApply" <c:if test="${fn:contains(person.permission,'ordersApply')}">checked="checked"</c:if> >工单申请&nbsp;&nbsp;</input> 
			     <input type="checkbox" id="permission" name="permission" value="ordersAudit" <c:if test="${fn:contains(person.permission,'ordersAudit')}">checked="checked"</c:if> >工单审核&nbsp;&nbsp;</input> 
			     <input type="checkbox" id="permission" name="permission" value="task" <c:if test="${fn:contains(person.permission,'task')}">checked="checked"</c:if>>定时任务</input><br>
			     			   
			     <input type="checkbox" id="permission" name="permission" value="synchronize" <c:if test="${fn:contains(person.permission,'synchronize')}">checked="checked"</c:if> >数据同步&nbsp;&nbsp;</input> 
			     <input type="checkbox" id="permission" name="permission" value="monitor" <c:if test="${fn:contains(person.permission,'monitor')}">checked="checked"</c:if> >状态监控&nbsp;&nbsp;</input>
			     <input type="checkbox" id="permission" name="permission" value="backdatabase" <c:if test="${fn:contains(person.permission,'backdatabase')}">checked="checked"</c:if>>备份还原&nbsp;</input><br>
			     
			     <input type="checkbox" id="permission" name="permission" value="log" <c:if test="${fn:contains(person.permission,'log')}">checked="checked"</c:if>>操作日志&nbsp;&nbsp;</input> 
			     <input type="checkbox" id="permission" name="permission" value="dataApi" <c:if test="${fn:contains(person.permission,'dataApi')}">checked="checked"</c:if>>数据API&nbsp;&nbsp;&nbsp;</input>
			     <input type="checkbox" id="permission" name="permission" value="json" <c:if test="${fn:contains(person.permission,'json')}">checked="checked"</c:if>>json格式化 </input><br>
			    
			     <input type="checkbox" id="permission" name="permission" value="person" <c:if test="${fn:contains(person.permission,'person')}">checked="checked"</c:if>>用户管理&nbsp;&nbsp;</input>
			     <input type="checkbox" id="permission" name="permission" value="config" <c:if test="${fn:contains(person.permission,'config')}">checked="checked"</c:if>>数据库配置 </input><br>
			     
			   </td>
		     </tr>
		     <tr>
				<td> &nbsp; </td>
				<td> &nbsp; </td>
		      </tr>
		      <tr>
			   <td>数据范围：  </td>
			   <td>
			        <c:forEach var="config" items="${ configList }"   >
		             <input type="checkbox" id="datascope" name="datascope" value="${config.id}"  <c:if test="${fn:contains(person.datascope, config.id )}">checked="checked"</c:if>  >${config.name} </input> <br>
		           </c:forEach>
			      
			   </td>
		     </tr>
		     
		</table>
		 
	</form>
</div>

<script type="text/javascript">

  var connSuccess = false;
  if(!$('#id').val() ==''){
    $('#username').attr("readonly",true);
  }

function addUpdatePerson(){  
	  var  id = $('#id').val();
	  var  username = $('#username').val();
	  var  password = $('#password').val();
	  var  realname = $('#realname').val();
      var  role = $('#role option:selected').val();
      var  status = $('#status option:selected').val();
      var  expiration = $('#expiration').datetimebox("getValue");
      var  note = $('#note').val();
      
      var test = $("input[name='permission']:checked");  
      var permission = "";   
        test.each(function(){  
           permission += $(this).val()+",";  
        })  
       permission = permission.substring(0,permission.length-1); 
        
       var scope = $("input[name='datascope']:checked");
       var datascope = "";   
        scope.each(function(){  
           datascope += $(this).val()+",";  
        })  
       datascope = datascope.substring(0,datascope.length-1); 
        
      // alert( permission );
      var isValid = $("#mainform").form('validate');
      if( !isValid ){
    	   return isValid;	// 返回false终止表单提交
      }
      
	  $.ajax({
			        type:'POST',
		          	contentType:'application/json;charset=utf-8',
                    url:"${ctx}/system/person/i/personUpdate" ,
                    data: JSON.stringify( { 'id':id ,'username':username ,'password':password ,'realname':realname ,'role':role ,'status':status ,'expiration':expiration ,'note':note,'permission':permission,'datascope':datascope} ),
                    datatype: "json", 
                   //成功返回之后调用的函数             
                    success:function(data){
                       var status = data.status ;
            	       if(status == 'success' ){
            	            parent.$.messager.show({ title : "提示",msg: data.mess , position: "bottomRight" });
            	            setTimeout(function () {
                                 person.panel('close');
                                 $("#dgForPerson").datagrid('reload');
                                  
                             }, 1500);
            	       }else{
            	    	    parent.$.messager.show({ title : "提示",msg: data.mess , position: "bottomRight",timeout:2000,icon:"warning" });
            	       }
            	     }  
       });
 }

</script>
</body>
</html>