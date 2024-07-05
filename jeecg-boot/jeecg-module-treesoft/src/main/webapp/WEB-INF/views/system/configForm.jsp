<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>config</title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
	<div>
		<form id="mainform" action="${ctx }/system/permission/i/configUpdate" method="post">
			<input id="id" name="id" type="hidden" value="${config.id }" />
			<table class="formTable">
				<tr>
					<td>名称：</td>
					<td>
						<input id="name" name="name" type="text" value="${config.name }" class="easyui-validatebox" data-options="width: 150" />
					</td>
				</tr>
				<tr>
					<td>数据库类型：</td>
					<td>
						<select id="databaseType" name="databaseType" class="esayui-combobox" style="width: 150px">
							<option value="MySql" <c:if test="${config.databaseType=='MySql' }"> selected </c:if>>MySql</option>
							<option value="MariaDB" <c:if test="${config.databaseType=='MariaDB' }"> selected </c:if>>MariaDB</option>
							<option value="Oracle" <c:if test="${config.databaseType == 'Oracle' }">selected</c:if>>Oracle</option>
							<option value="PostgreSQL" <c:if test="${config.databaseType == 'PostgreSQL' }">selected</c:if>>PostgreSQL</option>
							<option value="MSSQL" <c:if test="${config.databaseType == 'MSSQL' }">selected</c:if>>SQL Server</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>默认数据库：</td>
					<td>
						<input id="databaseName" name="databaseName" type="text" value="${config.databaseName }" class="easyui-validatebox"
							data-options="width: 150,required:'required'" />
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-tip" plain="true" title="Oracle请填写实例名"></a>
					</td>
				</tr>
				<tr>
					<td>IP地址：</td>
					<td>
						<input id="ip" name="ip" type="text" value="${config.ip }" class="easyui-validatebox"
							data-options="width: 150,required:'required',validType:'length[3,80]'" />
					</td>
				</tr>
				<tr>
					<td>端口：</td>
					<td>
						<input id="port" name="port" type="text" value="${config.port }" class="easyui-validatebox" data-options="width: 150,required:'required'" />
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-tip" plain="true"
							title=" MySql 默认端口为:3306 &#13;MariaDB默认端口为:3306 &#13;Oracle默认端口为:1521 &#13;PostgreSQL默认端口:5432 &#13;MSSQL默认端口为:1433"></a>
					</td>
				</tr>
				<tr>
					<td>用户名：</td>
					<td>
						<input id="userName" name="userName" type="text" value="${config.userName }" class="easyui-validatebox" data-options="width:150" />
					</td>
				</tr>
				<tr>
					<td>密 码：</td>
					<td>
						<input id="password" name="password" type="password" value="${config.password }" class="easyui-validatebox" data-options="width:150" />
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-tip" plain="true" title="此处为空时，不更新原始密码"></a>
					</td>
				</tr>
				<tr>
					<td>是否默认：</td>
					<td>
						<select id="isdefault" name="isdefault" class="esayui-combobox" style="width: 150px">
							<option value="0" <c:if test="${config.isdefault =='0' }"> selected</c:if>>否</option>
							<option value="1" <c:if test="${config.isdefault =='1' }"> selected</c:if>>是</option>
						</select>
					</td>
				</tr>
				<tr>
					<td></td>
					<td>
						<span id="mess2"> </span>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<script type="text/javascript">
		var connSuccess = false;
		function configUpdate2() {
			var id = $('#id').val();
			var name = $('#name').val();
			var databaseType = $('#databaseType option:selected').val();
			var databaseName = $('#databaseName').val();
			var ip = $('#ip').val();
			var port = $('#port').val();
			var userName = $('#userName').val();
			var password = $('#password').val();
			var isdefault = $('#isdefault').val();
			var isValid = $("#mainform").form('validate');
			if (!isValid) {
				return isValid; // 返回false终止表单提交
			}
			$.ajax({
				type : 'POST',
				contentType : 'application/json;charset=utf-8',
				url : "${ctx}/system/permission/i/configUpdate",
				data : JSON.stringify({
					'id' : id,
					'name' : name,
					'databaseType' : databaseType,
					'databaseName' : databaseName,
					'ip' : ip,
					'port' : port,
					'userName' : userName,
					'password' : password,
					'isdefault' : isdefault
				}),
				datatype : "json",
				//成功返回之后调用的函数             
				success : function(data) {
					var status = data.status;
					if (status == 'success') {
						parent.$.messager.show({
							title : "提示",
							msg : data.mess,
							position : "bottomRight"
						});
						setTimeout(function() {
							config.panel('close');
							$("#dg3").datagrid('reload');
						}, 1500);
						parent.init3();
					} else {
						parent.$.messager.show({
							title : "提示",
							msg : data.mess,
							position : "bottomRight"
						});
					}
				}
			});
		}
		//测试连接
		function testConn() {
			$("#mess2").html("连接测试中...");
			$.ajax({
				type : 'POST',
				contentType : 'application/json;charset=utf-8',
				url : "${ctx}/system/permission/i/testConn",
				data : JSON.stringify({
					'databaseType' : $("#databaseType").val(),
					'databaseName' : $("#databaseName").val(),
					'userName' : $("#userName").val(),
					'password' : $("#password").val(),
					'port' : $("#port").val(),
					'ip' : $("#ip").val()
				}),
				datatype : "json",
				//成功返回之后调用的函数             
				success : function(data) {
					var status = data.status;
					if (status == 'success') {
						$("#mess2").html(data.mess);
						connSuccess = true;
						//alert( data.mess );
					} else {
						connSuccess = false;
						$("#mess2").html("连接失败！");
					}
				}
			});
		}
	</script>
</body>
</html>