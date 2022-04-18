<#assign base=springMacroRequestContext.getContextUrl("")>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<meta http-equiv="Cache-Control" content="no-store"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<title>PDF预览</title>
</head>
<body>
<script type="text/javascript">

function openScanFile(title,token,bizNo,archivesNo){
 	 //var pdfUrl ="http://127.0.0.1:8080/jeecg-boot/generic/web/viewer.html?file="+encodeURIComponent("http://127.0.0.1:8080/jeecg-boot/test/jeecgDemo/getPdfUrl?title="+encodeURI(title));
 	 var pdfUrl ="${base}/generic/web/viewer.html?file="+encodeURIComponent("https://jeecgos.oss-cn-beijing.aliyuncs.com/files/site/java_p3c.pdf");
 	 
	 var vm=window.open(pdfUrl);
}

 window.addEventListener('message', function(event) {
     var data = event.data;
     var title = data.title;
	 var token = data.token;
     openScanFile(title,token);
 }, false);
 
</script>
</body>
</html>
