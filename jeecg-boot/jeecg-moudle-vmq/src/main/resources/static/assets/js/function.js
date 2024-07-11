function creatOrder(payId, param, type, price, isHtml) {
    var code = -1;
    var sign = getMd5(payId + param + type + price);
    var reqParam = "payId=" + payId + "&type=" + type + "&price=" + price + "&sign=" + sign + "&param=" + param + "&isHtml=" + isHtml;
    $.ajax({
        type: 'POST',
        url: "/createOrder",
        async: false,
        data: reqParam,
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