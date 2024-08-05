package com.vmq.utils;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.vmq.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PayQueryUtil {

    public static JSONArray getAliPayPage(int queryTime,String cookie) {
        String ctoken = StringUtils.getValueBetweenKey(cookie,"ctoken=",";");
        String billUserId = StringUtils.getValueBetweenKey(cookie,"CLUB_ALIPAY_COM=",";");
        JSONArray payArray = new JSONArray();
        String startTime = DateUtil.formatDateTime(DateUtil.date(System.currentTimeMillis() - queryTime * 60000));
        String endTime = DateUtil.formatDateTime(DateUtil.date(System.currentTimeMillis() + queryTime * 60000));
        String url = Constant.ALI_ORDER_QUERY_URL + ctoken;
        Map<String,Object> param = new HashMap<>();
        param.put("billUserId",billUserId);
        param.put("entityFilterType",1);
        param.put("tradeFrom","ALL");
        param.put("targetTradeOwner","USERID");
        param.put("pageNum",1);
        param.put("pageSize",20);
        param.put("startTime",startTime);
        param.put("endTime",endTime);
        param.put("status","ALL");
        param.put("sortType",0);
        param.put("_input_charset","gbk");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Referer","https://b.alipay.com");
        headers.add("Cookie",cookie);
        JSONObject entity = HttpRequest.sendPost(url,param,headers, JSONObject.class);
        if ("true".equals(entity.getString("success"))) {
            payArray = entity.getJSONObject("result").getJSONArray("detail");
            log.info("查询支付宝账单成功：{}", entity.getJSONObject("result").getJSONObject("paging"));
        }
        return payArray;
    }

}
