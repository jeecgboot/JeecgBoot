<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>捐赠</title>
 <%@ include file="/WEB-INF/views/include/easyui.jsp"%>
 
</head>


 <body>
<div class="inform">

<table class="formTable">
                   <tr>
				     <td>&nbsp;&nbsp;&nbsp; <img  style="width:180px; height:180px" src="${ctx}/static/images/31.jpg"    /></td>
				     <td>&nbsp;&nbsp;&nbsp; <img  style="width:180px; height:180px"  src="${ctx}/static/images/32.jpg"   /></td>
			       </tr>
		           <tr>
				     <td style="text-align: center;" >&nbsp;支付宝二维码 </td>
				     <td style="text-align: center;" >&nbsp;微信二维码  </td>
			       </tr>
		</table>
		<br>
		<span style="color: #fffl;font-size:18px;">&nbsp;&nbsp;&nbsp;&nbsp;<img src="http://www.treesoft.cn/picture/logo.png"  onerror="imgerror(this)" > 有梦想,肯实干,世界都会挺你！  </span>
		<br>
		<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;感谢您的支持，希望本软件能为您的工作带来便利！  </span>
		
</div>


<div class="Top_Record"  style="float:left;" >
 <ul> 
  <li>2017-09-14 　　网友长琴　 　　捐赠：10元</li>
  <li>2015-05-11 　　张先生 　　 　　捐赠：10元</li>
  <li>2015-05-18 　　恒龙信息 　　 　捐赠：30元</li>
  <li>2015-06-08 　　刘先生 　　 　　捐赠：20元</li>
  <li>2015-06-15 　　热心网友 　　 　捐赠：10元</li>
  <li>2015-07-05 　　福建网友 　　 　捐赠：100元</li>
  <li>2015-08-11 　　余先生 　　 　　捐赠：10元</li>
  <li>2015-08-18 　　林先生 　　 　　捐赠：5元</li>
</ul> 

</div> 

<div style="float:right;margin-right:20px"> 
 <img  style="width:120px; height:120px" src="${ctx}/static/images/30.gif"  /> 
</div>

<script>
function imgerror(img){
    img.src="${ctx}/static/images/logo.png";
    img.onerror=null;   
   }
</script> 
   
 
</body>
</html>