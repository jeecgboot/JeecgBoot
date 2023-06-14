package org.jeecg.modules.business.domain.api.cmk;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CMKRequest {
    private final static String URL = "https://cmk.itdida.com/itdida-api/queryTracks";
    private static final String API_KEY = "ITDIDA-APP-KEY-YR573T2VQ2";
    private static final RequestConfig REQUEST_CONFIG = RequestConfig.custom().build();

    private final List<String> trackingNumbers;

    public CMKRequest(List<String> trackingNumbers) {
        this.trackingNumbers = trackingNumbers;
    }

    /**
     * Sent request to the CMK API with a request body.
     *
     * @return the response
     */
    public HttpResponse send() {
        int attempts = 0;
        while (attempts++ < 5) {
            try {
                HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(REQUEST_CONFIG).build();
                HttpGet request = new HttpGet(URL + "?no=" + String.join(",", trackingNumbers));

                // adding the form data
                request.setHeader(HttpHeaders.AUTHORIZATION, API_KEY);
                return httpClient.execute(request);
            } catch (Exception e) {
                log.error("Request failed on attempt n°" + attempts);
            }
        }
        return null;
    }

}
