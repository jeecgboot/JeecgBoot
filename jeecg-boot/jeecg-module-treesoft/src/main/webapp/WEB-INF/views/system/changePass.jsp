<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

</head>
<body>
<div>
	<form id="mainform" action="${ctx }/system/permission/i/changePassUpdate" method="post" >
		<table class="formTable">
			 <tr>
				<td>旧密码：</td>
				<td><input name="oldPass" id="oldPass" type="text"  class="easyui-validatebox" data-options="width: 150,required:'required'"/></td>
			</tr>
			 
			<tr>
				<td>新密码：</td>
				<td><input name="newPass" id="newPass"type="text"  class="easyui-validatebox" data-options="width: 150,required:'required',validType:'length[6,20]'"/></td>
			</tr>
		</table>
		<span>&nbsp;&nbsp;提示：此密码仅为本系统使用，与数据库无关！ </span>
	</form>
</div>

<script type="text/javascript">
 
function changePass(){  
	  var  oldPass = $('#oldPass').val();
      var  newPass = $('#newPass').val();
	  $.ajax({
			        type:'POST',
		          	contentType:'application/json;charset=utf-8',
                    url:"${ctx}/system/permission/i/changePassUpdate" ,
                    data: JSON.stringify( { 'oldPass':oldPass  ,'newPass':newPass  } ),
                    datatype: "json", 
                   //成功返回之后调用的函数             
                    success:function(data){
                       var status = data.status ;
            	       if(status == 'success' ){
            	            parent.$.messager.show({ title : "提示",msg: data.mess , position: "bottomRight" });
            	            setTimeout(function () {
                            parent.pwd.panel('close');
                            }, 2000);
            	       }else{
            	    	    parent.$.messager.show({ title : "提示",msg: data.mess , position: "bottomRight" });
            	       }
            	     }  
       });
   }
 
</script>
</body>
</html>