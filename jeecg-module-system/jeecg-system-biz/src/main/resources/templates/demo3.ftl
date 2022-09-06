<!DOCTYPE html>
<html>
<head lang="en">
<title>Spring Boot FreeMarker</title>
</head>
<body>
	Freemarker HTML <br><br>
	
	Sessionid: ${sessionid!}<br><br>
	
    <font>
        <#list userList as item>
            ${item!}<br />
        </#list>
    </font>
</body>
</html>