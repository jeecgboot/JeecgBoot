package org.jeecg.modules.openapi.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import java.security.MessageDigest;



public class SampleOpenApiTest {
    private final String base_url = "http://localhost:8080/jeecg-boot";
    private final String appKey = "ak-eAU25mrMxhtaZsyS";
    private final String searchKey = "rjxMqB6YyUXpSHAz4DCIz8vZ5aozQQiV";
    @Test
    public void test() throws Exception {
        String url = base_url+"/openapi/call/wYAu6xwg?id=a7d7e77e06c84325a40932163adcdaa6";
        JSONObject header = genTimestampAndSignature();
        HttpGet httpGet = new HttpGet(url);
        // 设置请求头
        httpGet.setHeader("Content-Type", "application/json");
        httpGet.setHeader("appkey",appKey);
        httpGet.setHeader("signature",header.get("signature").toString());
        httpGet.setHeader("timestamp",header.get("timestamp").toString());
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(httpGet);) {
            // 获取响应状态码
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("[debug] 响应状态码: " + statusCode);

            HttpEntity entity = response.getEntity();
            System.out.println(entity);
            // 获取响应内容
            String responseBody = EntityUtils.toString(response.getEntity());
            System.out.println("[debug] 响应内容: " + responseBody);

            // 解析JSON响应
            JSONObject res = JSON.parseObject(responseBody);
            System.out.println("[info] 调用成功： " + res.toJSONString());
        }

    }
    private JSONObject genTimestampAndSignature(){
        JSONObject jsonObject = new JSONObject();
        long timestamp = System.currentTimeMillis();
        jsonObject.put("timestamp",timestamp);
        jsonObject.put("signature", md5(appKey + searchKey + timestamp));
        return jsonObject;
    }

    /**
     * 生成md5
     * @param sourceStr
     * @return
     */
    protected String md5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes("utf-8"));
            byte[] hash = md.digest();
            int i;
            StringBuffer buf = new StringBuffer(32);
            for (int offset = 0; offset < hash.length; offset++) {
                i = hash[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (Exception e) {
            throw new RuntimeException("sign签名错误", e);
        }
        return result;
    }
}
