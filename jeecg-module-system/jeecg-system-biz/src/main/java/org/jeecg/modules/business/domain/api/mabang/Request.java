package org.jeecg.modules.business.domain.api.mabang;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.jeecg.common.util.RestUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

import static java.lang.Thread.sleep;

/**
 * This class contains some key information and necessary procedures
 * to send a request body to mabang API, for example: target URL,
 * correspondent HTTP method, procedure to generate authorization.
 * <p>
 * Subclass should implement the send() method by specifying the real response class and constructing it.
 */
@Slf4j
public abstract class Request {
    private final static String URL = "https://gwapi.mabangerp.com/api/v2";
    private static final HttpMethod METHOD = HttpMethod.POST;
    private static final String APP_KEY = "87a1a0c46df86eb2683e74776894dae9";
    private static final String DEV_ID = "200809";

    private final RequestBody body;

    public Request(RequestBody body) {
        this.body = body;
    }

    /**
     * Sent request to the mabang API with a request body.
     *
     * @return the response of the body or null, if response
     */
    protected ResponseEntity<String> rawSend() {
        int attempts = 0;
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

        String bodyString = generateJsonBodyString(body);
        String signature = authorization(bodyString);
        headers.add("Authorization", signature);
        while (attempts++ < 5){
            try {
                return RestUtil.request(URL, METHOD, headers, null, bodyString, String.class);
            } catch (Exception e) {
                log.error("Request failed on attempt n°" + attempts);
            }
        }
        return null;
    }


    public abstract Response send();

    /**
     * By applying algorithm "HmacSHA256", encrypt http body with developer key that provided by mabang.
     *
     * @param body the json string to encrypt
     * @return encrypted string encoded by Hexadecimal.
     */
    private static String authorization(String body) {
        byte[] hmacSha256;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(APP_KEY.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);
            hmacSha256 = mac.doFinal(body.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate hmac-sha256", e);
        }
        return Hex.encodeHexString(hmacSha256);
    }

    /**
     * Convert body's json parameters to json string with necessary extra parameter to
     * send request.
     *
     * @param body body to convert
     * @return json string
     */
    private static String generateJsonBodyString(RequestBody body) {
        JSONObject param = new JSONObject();
        param.put("appkey", DEV_ID);
        param.put("timestamp", new Date().getTime() / 1000);
        param.put("api", body.api());
        param.put("data", body.parameters());
        return param.toJSONString();
    }

    public static void main(String[] args) {
        JSONObject param = new JSONObject();
        param.put("appkey", DEV_ID);
        param.put("timestamp", new Date().getTime() / 1000);
        JSONObject data = new JSONObject();
//        data.put("status", "6");
//        data.put("canSend", "3");
//        data.put("platformOrderIds", "5189621350562,5189458231458,5188388880546,5187950575778,5187139797154,5187080323234,5186827321506,5186723610786,5186707652770,5186650570914");
//        data.put("shopName", "JCH5");
        data.put("platformOrderId", "MABANGAPITEST");
        JSONArray stockDataArray = new JSONArray();
        JSONObject stockData = new JSONObject();
        stockData.put("stockSku", "PJ95430040-WIA");
        stockData.put("quantity", 1);
        stockData.put("isGift", 1);
        stockData.put("warehouseName", "SZBA宝安仓");
        stockData.put("type", "3");
        stockDataArray.add(stockData);
        data.put("stockData", stockDataArray.toJSONString());
//        data.put("paidtimeStart", "2022-09-13 00:00:00");
//        data.put("paidtimeEnd", "2022-09-15 00:00:00");
        param.put("data", data);
        param.put("api", "order-do-change-order");
//        param.put("api", "order-get-order-list");
        String body1 = param.toString();
        String authorization = authorization(body1);
        System.out.println(body1);
        System.out.println(authorization);
    }
}
