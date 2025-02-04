package org.jeecg.modules.business.domain.api.shouman;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.jeecg.common.util.RestUtil;
import org.jeecg.modules.business.entity.Shouman.ShoumanOrder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * This class contains some key information and necessary procedures
 * to send a request body to ShouMan API, for example target URL,
 * correspondent HTTP method, procedure to generate authorization.
 * <p>
 * Subclass should implement the send() method by specifying the real response class and constructing it.
 */
@Slf4j
public abstract class Request {
    private final static String BASE_URL = "http://43.192.16.96:8012/api";
    private static final HttpMethod METHOD = HttpMethod.POST;
    private static final String KEY = "BpQWy6AtvKcixjePQ4ZMuvBqUyIsXWWX";
    private static final String SHOP_CODE = "8ee5c82004c44049b9f22a0ed8dc4db3";
    private static final String SHIPPING_SERVICE_LEVEL_CATEGORY = "Standard";
    private final RequestBody body;

    public Request(RequestBody body) {
        this.body = body;
    }

    /**
     * Sent request to the mabang API with a request body.
     *
     * @return the response of the body or null, if response
     */
    public ResponseEntity<String> rawSend(ShoumanOrder shoumanOrder) {
        int attempts = 0;
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

        String bodyString = generateJsonBodyString(body);
        String signatureString = generateSignForJson(bodyString);
        String signatureMd5 = DigestUtils.md5Hex(signatureString).toUpperCase();
        shoumanOrder.setSignatureString(signatureString);
        shoumanOrder.setSignatureMd5(signatureMd5);
        headers.add("signature", signatureMd5);
        while (attempts++ < 5) {
            try {
                return RestUtil.request(BASE_URL + body.path(), METHOD, headers, null, bodyString, String.class);
            } catch (Exception e) {
                log.error("Request failed on attempt n°" + attempts);
            }
        }
        return null;
    }

    /**
     * Convert body's json parameters to json string with the necessary extra parameter to
     * send request.
     *
     * @param body body to convert
     * @return json string
     */
    private static String generateJsonBodyString(RequestBody body) {
        JSONObject param = new JSONObject();
        param.putAll(body.parameters());
        param.put("shipmentServiceLevelCategory", SHIPPING_SERVICE_LEVEL_CATEGORY);
        param.put("isSendStandardPro", true);
        param.put("shopCode", SHOP_CODE);
        return param.toJSONString();
    }

    private static String generateSignForJson(String jsonString) {
        log.info("JSON:{}", jsonString);
        if (jsonString.isEmpty()) {
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(jsonString, Feature.OrderedField);
        Map<String, Object> maps = jsonObject.getInnerMap();
        ArrayList<String> arrayList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : maps.entrySet()) {
            arrayList.add(entry.getKey() + "=" + entry.getValue() + "&");
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        String[] strArray = arrayList.toArray(new String[0]);
        Arrays.sort(strArray);
        StringBuilder stringBuffer = new StringBuilder();
        for (String param : strArray) {
            stringBuffer.append(param);
        }
        String params = stringBuffer + "key=" + KEY;
        log.info("Signature:{}", params);
        return params;
    }
}
