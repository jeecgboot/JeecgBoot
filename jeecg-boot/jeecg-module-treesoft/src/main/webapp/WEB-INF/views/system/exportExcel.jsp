<%@ page contentType="application/msexcel; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  

<%
  String base = request.getContextPath();
  response.setHeader("Content-disposition","inline; filename=data.xls");
%>


<html>
<head>
<title>历史数据</title>
</head>
<body>
<table  border="1px"> 
  <tr>   	
    <th>监测点编号</th>
    <th>监测点名称</th>
    <th>监测时间</th>
    <th>监测值</th>
   		
  </tr>
  
   <tr>   				 
    <td>2222</td>
    <td>3333</td>
    <td>wwwww</td>   						
    <td>44444</td>    
      						
  </tr>
  
</table>
<script type="text/javascript"> 
 close();
</script>  
</body>
</html>

