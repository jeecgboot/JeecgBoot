package org.jeecg.modules.business.domain.api.jt;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains some key information and necessary procedures
 * to send a request body to mabang API, for example: target URL,
 * correspondent HTTP method, procedure to generate authorization.
 */
@Slf4j
public class JTRequest {
    private final static String URL = "http://api.yl-scm.com/yunlu-order-web/track/trackAction!trackForJson.action";
    private static final String E_COMPANY_ID = "WeiYaZhiTong";
    private static final String MSG_TYPE = "TRACKQUERY";
    private static final String DEV_KEY = "6cecd616641bc3f7f7fe49a945843532";
    private static final RequestConfig REQUEST_CONFIG = RequestConfig.custom().build();

    private final List<String> billCodes;

    public JTRequest(List<String> billCodes) {
        this.billCodes = billCodes;
    }

    /**
     * Sent request to the mabang API with a request body.
     *
     * @return the response
     */
    public HttpResponse send() {
        int attempts = 0;
        while (attempts++ < 5) {
            try {
                HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(REQUEST_CONFIG).build();
                HttpPost request = new HttpPost(URL);

                // adding the form data
                request.setEntity(new UrlEncodedFormEntity(generateFormData(), "UTF-8"));
                return httpClient.execute(request);
            } catch (Exception e) {
                log.error("Request failed on attempt n°" + attempts);
            }
        }
        return null;
    }

    /**
     * Convert body's json parameters to json string with necessary extra parameter to
     * send request.
     *
     * @return json string
     */
    private List<NameValuePair> generateFormData() {
        List<NameValuePair> pairs = new ArrayList<>();
        String logisticsInterface = generateJsonString(billCodes);
        pairs.add(new BasicNameValuePair("logistics_interface", logisticsInterface));
        String data_digest = new String(Base64.encodeBase64(
                DigestUtils.md5Hex(logisticsInterface + DEV_KEY)
                        .getBytes(StandardCharsets.UTF_8)));
        pairs.add(new BasicNameValuePair("data_digest", data_digest));
        pairs.add(new BasicNameValuePair("eccompanyid", E_COMPANY_ID));
        pairs.add(new BasicNameValuePair("msg_type", MSG_TYPE));
        return pairs;
    }

    private static String generateJsonString(List<String> billCodes) {
        JSONObject param = new JSONObject();
        String billCodesWithComas = String.join(",", billCodes);
        param.put("billcode", billCodesWithComas);
        param.put("lang", "en");
        return param.toJSONString();
    }
}
