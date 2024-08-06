package com.vmq.constant;

/**
 * 常量
 */
public interface Constant {
    // 字符窜常量
    String SUCCESS = "成功";
    String ERROR = "失败";
    String ORDER_PUSH = "ORDER_PUSH";
    String PAY_SUCCESS_EMAIL_TITLE = "【码支付】支付成功通知";
    String PAY_SUCCESS_EMAIL_TEMPLATE = "pay-success";

    // 数字常量
    int NUMBER_0 = 0;
    int NUMBER_1 = 1;
    int NUMBER_2 = 2;
    int NUMBER_5 = 5;
    int NUMBER_10 = 10;
    int NUMBER_30 = 30;
    int NUMBER_60 = 60;
    int NUMBER_120 = 120;

    // 时间戳计算
    long MIN2 = NUMBER_2 * 60 * 1000;
    long MIN5 = NUMBER_5 * 60 * 1000;
    long MIN10 = NUMBER_10 * 60 * 1000;
    long MIN120 = NUMBER_120 * 60 * 1000;

    // API接口
    // String aliTransScheme= "https://render.alipay.com/p/s/i?scheme=";
    String aliOpenUrl = "alipays://platformapi/startapp?appId=20000067&url=";
    @Deprecated // 无法从通知内容中获取金额，不推荐使用
    String aliTransURL = "alipays://platformapi/startapp?appId=09999988&actionType=toAccount&userId=%s&amount=%s&memo=%s&goBack=NO";
    String ALI_TRANS_URL = "alipays://platformapi/startapp?appId=20000123&actionType=scan&biz_data={\"s\": \"money\",\"u\": \"%s\",\"a\": \"%s\",\"m\": \"%s\"}";
    String ALI_ORDER_QUERY_URL = "https://mbillexprod.alipay.com/enterprise/tradeListQuery.json?_output_charset=utf-8&ctoken=";
}
