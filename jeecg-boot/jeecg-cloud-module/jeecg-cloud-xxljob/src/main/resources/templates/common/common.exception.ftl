<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Error</title>
    <style type="text/css"> 
        body { background-color: #fff; color: #666; text-align: center; font-family: arial, sans-serif; }
        div.dialog {
            width: 80%;
            padding: 1em 4em;
            margin: 4em auto 0 auto;
            border: 1px solid #ccc;
            border-right-color: #999;
            border-bottom-color: #999;
        }
        h1 { font-size: 100%; color: #f00; line-height: 1.5em; }
    </style>
    
</head> 
</head>
<body> 

	<div class="dialog"> 
	    <h1>System Error</h1>
	    <p>${exceptionMsg}</p>
		<a href="javascript:window.location.href='${request.contextPath}/'">Back</a>
	    </p> 
	</div>

</body>
</html>