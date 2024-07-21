package com.bomaos.common.core.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RequestParamsUtil {

    public static Map<String, String> getParameterMap(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>(); //申明hashMap变量储存接收到的参数名用于排序
        Map requestParams = request.getParameterMap(); //获取请求的全部参数
        String valueStr = ""; //申明字符变量 保存接收到的变量
        for (
                Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            valueStr = values[0];
            //乱码解决，这段代码在出现乱码时使用。如果sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);//增加到params保存
        }

        return params;
    }
}
