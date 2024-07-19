
function logout() {
    $.post("logout", function (data) {
        window.location.href = "login";
    });
}

function creatOrder(payId, param, type, price, isHtml,email) {
    var code = -1;
    $.ajax({
        type: 'POST',
        url: "addOrder",
        async: false,
        datatype: "json",
        contentType: "application/json",
        data: JSON.stringify({"payId":payId,"type":type,"price":price,"sign":"","param":param,"isHtml":isHtml,"email":email}),
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


function getUserInfo() {
    var userMap;
    $.ajax({
        type: 'get',
        url: "getUserInfo",
        async: false,
        success: function (data) {
            userMap = data.data;
        }
    });
    return userMap;
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