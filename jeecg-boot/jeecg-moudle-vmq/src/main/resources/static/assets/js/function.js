function logout() {
    $.post("/logout", function (data) {
        window.location.href = "index.html";
    });
}

function creatOrder(payId, param, type, price, isHtml,email) {
    var code = -1;
    var sign = getMd5(payId + param + type + price);
    $.ajax({
        type: 'POST',
        url: "/createOrder",
        async: false,
        datatype: "json",
        contentType: "application/json",
        data: JSON.stringify({"payId":payId,"type":type,"price":price,"sign":sign,"param":param,"isHtml":isHtml,"email":email}),
        success: function (data2) {
            if (isHtml == 1) {
                try {
                    eval(data2.replace("<script>", "").replace("<\/script>", ""));
                } catch{
                    alert(data2);
                }
            } else {
                var result = JSON.parse(data2);
                code = result.code;
                alert(data2);
            }
        }
    });
    return code;
}

function getMd5(content) {
    var sign = "";
    $.ajax({
        type: 'POST',
        url: "/md5",
        async: false,
        data: {"content": content},
        success: function (data1) {
            sign = data1;
        }
    });
    return sign;
}

function getToken() {
    var token = "";
    $.ajax({
        type: 'GET',
        url: "/getToken",
        async: false,
        data: {},
        success: function (data) {
            token = data;
        }
    });
    return token;
}

function formatDate(now) {
    if (now=="0"){
        return "无";
    }
    now = new Date(now);
    return now.getFullYear()
        + "-" + (now.getMonth()>8?(now.getMonth()+1):"0"+(now.getMonth()+1))
        + "-" + (now.getDate()>9?now.getDate():"0"+now.getDate())
        + " " + (now.getHours()>9?now.getHours():"0"+now.getHours())
        + ":" + (now.getMinutes()>9?now.getMinutes():"0"+now.getMinutes())
        + ":" + (now.getSeconds()>9?now.getSeconds():"0"+now.getSeconds());

}