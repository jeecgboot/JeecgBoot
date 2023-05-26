package org.jeecg.modules.business.domain.api.yd;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains some key information and necessary procedures
 * to send a request body to mabang API, for example: target URL,
 * correspondent HTTP method, procedure to generate authorization.
 */
@Slf4j
public class YDRequest {
    private final static String URL = "http://oms.ydhex.com/webservice/PublicService.asmx/ServiceInterfaceUTF8";
    private String appToken;
    private String appKey;

    private static final String SERVICE_METHOD = "gettrack";
    private static final RequestConfig REQUEST_CONFIG = RequestConfig.custom().build();

    private final List<String> billCodes;

    public YDRequest(String appToken, String appKey, List<String> billCodes) {
        this.appToken = appToken;
        this.appKey = appKey;
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
        String paramsJson = generateJsonString(billCodes);
        pairs.add(new BasicNameValuePair("appToken", appToken));
        pairs.add(new BasicNameValuePair("appKey", appKey));
        pairs.add(new BasicNameValuePair("serviceMethod", SERVICE_METHOD));
        pairs.add(new BasicNameValuePair("paramsJson", paramsJson));
        return pairs;
    }

    private static String generateJsonString(List<String> billCodes) {
        JSONObject param = new JSONObject();
        String billCodesWithComas = String.join(",", billCodes);
        param.put("tracking_number", billCodesWithComas);
        return param.toJSONString();
    }
}
