package com.vmq.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.*;
import com.alipay.api.request.AlipayDataBillAccountlogQueryRequest;
import com.alipay.api.request.AlipayDataBillSellQueryRequest;
import com.alipay.api.response.AlipayDataBillAccountlogQueryResponse;
import com.alipay.api.response.AlipayDataBillSellQueryResponse;
import com.vmq.constant.Constant;
import com.vmq.entity.VmqSetting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付宝工具类
 */
@Slf4j
public class AliPayUtil {
    private static AlipayClient alipayClient;

    private static void initAlipayClient(VmqSetting setting) {
        if (alipayClient == null) {
            synchronized (AliPayUtil.class) {
                if (alipayClient == null) {
                    String appId = setting.getAppId();
                    String privateKey = setting.getPrivateKey();
                    String alipayPublicKey = setting.getAlipayPublicKey();
                    alipayClient = new DefaultAlipayClient(Constant.ALI_GATEWAY_URL, appId, privateKey, Constant.JSON, Constant.UTF8, alipayPublicKey, Constant.RSA2);
                }
            }
        }
    }


    public static List<TradeItemResult> getAlipayBillSell(int queryTime,VmqSetting setting) {
        initAlipayClient(setting);
        // 创建API请求对象
        AlipayDataBillSellQueryRequest request = new AlipayDataBillSellQueryRequest();
        AlipayDataBillSellQueryModel model = new AlipayDataBillSellQueryModel();
        String startTime = DateUtil.formatDateTime(DateUtil.date(System.currentTimeMillis() - queryTime * Constant.MIN_UNIT));
        String endTime = DateUtil.formatDateTime(DateUtil.date(System.currentTimeMillis() + queryTime * Constant.MIN_UNIT));
        model.setStartTime(startTime);
        model.setEndTime(endTime);
        model.setPageNo("1");
        model.setPageSize("1000");
        request.setBizModel(model);
        List<TradeItemResult> billList = new ArrayList<>();
        try {
            AlipayDataBillSellQueryResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                billList = response.getDetailList();
                if (CollectionUtil.isNotEmpty(billList)) {
                    log.info("[{}]查询支付宝账单成功", setting.getUsername());
                }
            }
        } catch (AlipayApiException e) {
            log.error("获取支付宝账单失败：{}", e.getMessage());
        }
        return billList;
    }

    public static void getAlipayBillAccount(int queryTime,VmqSetting setting) {
        initAlipayClient(setting);
        AlipayDataBillAccountlogQueryRequest request = new AlipayDataBillAccountlogQueryRequest();
        String startTime = DateUtil.formatDateTime(DateUtil.date(System.currentTimeMillis() - queryTime * Constant.MIN_UNIT));
        String endTime = DateUtil.formatDateTime(DateUtil.date(System.currentTimeMillis() + queryTime * Constant.MIN_UNIT));
        JSONObject param = new JSONObject();
        param.put("start_time",startTime);
        param.put("end_time",endTime);
        param.put("page_no","1");
        param.put("page_size","1000");
        request.setBizContent(param.toJSONString());
        try {
            List<AccountLogItemResult> itemResultList = new ArrayList<>();
            AlipayDataBillAccountlogQueryResponse response = alipayClient.execute(request);
            if(response.isSuccess()){
                itemResultList = response.getDetailList();
                if (CollectionUtil.isNotEmpty(itemResultList)) {
                    log.info("[{}]查询支付宝账单成功", setting.getUsername());
                }
            }
        } catch (AlipayApiException e) {
            log.error("获取支付宝账单失败：{}", e.getMessage());
        }
    }

    public static JSONArray getAliPayByCookie(int queryTime, String cookie) {
        String ctoken = StringUtils.getValueBetweenKey(cookie,"ctoken=",";");
        String billUserId = StringUtils.getValueBetweenKey(cookie,"CLUB_ALIPAY_COM=",";");
        JSONArray payArray = new JSONArray();
        String startTime = DateUtil.formatDateTime(DateUtil.date(System.currentTimeMillis() - queryTime * Constant.MIN_UNIT));
        String endTime = DateUtil.formatDateTime(DateUtil.date(System.currentTimeMillis() + queryTime * Constant.MIN_UNIT));
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
