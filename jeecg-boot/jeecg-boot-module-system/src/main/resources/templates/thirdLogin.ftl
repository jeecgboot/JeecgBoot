<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>第三方登录</title>
</head>
<body>
登陆中...
<script>
    window.onload = function () {
        setTimeout(function (){
            var thirdLoginInfo = "${token!''}";
            if(!thirdLoginInfo){
                var thirdLoginModel = '${thirdLoginModel!""}';
                if(thirdLoginModel){
                    thirdLoginInfo = JSON.parse(thirdLoginModel);
                    thirdLoginInfo['isObj'] = true
                }
            }
            window.opener.postMessage(thirdLoginInfo, "*");
            window.close();
        },1000)
    }
</script>
</body>
</html>