package org.jeecg.modules.business.domain.api.equick;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.RestUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * This class contains some key information and necessary procedures
 * to send a request body to mabang API, for example: target URL,
 * correspondent HTTP method, procedure to generate authorization.
 */
@Slf4j
public class EQuickRequest {
    private final static String URL = "http://api.equick.cn:8080/TrackTraceDataSet";
    private static final String CUSTOM_NO = "SZX2015567";
    private static final String PASSWORD = "1146d18063b4acda584836c8076fdbfe";

    private final String equickNumber;

    public EQuickRequest(String equickNumber) {
        this.equickNumber = equickNumber;
    }

    /**
     * Sent request to the mabang API with a request body.
     *
     * @return the response
     */
    public ResponseEntity<String> send() {
        int attempts = 0;
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        while (attempts++ < 5) {
            try {
                // adding the form data
                String bodyString = generateJsonString(equickNumber);
                return RestUtil.request(URL, HttpMethod.POST, headers, null, bodyString, String.class);
            } catch (Exception e) {
                log.error("Request failed on attempt n°" + attempts);
            }
        }
        return null;
    }

    private static String generateJsonString(String equickNumber) {
        JSONObject json = new JSONObject();
        JSONObject auth = new JSONObject();
        auth.put("CustomNo", CUSTOM_NO);
        auth.put("Password", PASSWORD);
        json.put("Authentication", auth);
        json.put("EquickWBNo", equickNumber);
        return json.toJSONString();
    }
}
