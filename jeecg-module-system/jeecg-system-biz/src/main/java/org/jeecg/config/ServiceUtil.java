package org.jeecg.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.RestUtil;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ServiceUtil {

    private static ISysDictService sysDictService;

    private static String alist_domain;

    private static String alist_token;

    @Autowired
    public void setBookDao(ISysDictService sysDictService) {
        ServiceUtil.sysDictService = sysDictService;
    }

    public static String queryDictTextByKey(String code, String key) {
        if (sysDictService == null) {
            sysDictService = (ISysDictService) SpringContextUtils.getBean("sysDictService");
        }
        return sysDictService.queryDictTextByKey(code, key);
    }

    /**
     * 获取alist token
     *
     * @return
     */
    public static String getAListToken() {
        if (StringUtils.isBlank(alist_token)) {
            Map<String, Object> param = new HashMap<>();
            param.put("username", queryDictTextByKey("alist_param", "username"));
            param.put("password", queryDictTextByKey("alist_param", "password"));
            HttpHeaders headers = new HttpHeaders();
            String mediaType = org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
            headers.setContentType(org.springframework.http.MediaType.parseMediaType(mediaType));
            headers.set("User-Agent", "Apifox/1.0.0 (https://apifox.com)");
            String url = ServiceUtil.getAListDomain() + Constant.ALIST_AUTH_LOGIN;
            ResponseEntity<JSONObject> result = RestUtil.request(url, HttpMethod.POST, headers, null, JSON.toJSONString(param), JSONObject.class);
            alist_token = result.getBody().getJSONObject("data").getString("token");
        }
        return alist_token;
    }

    /**
     * 获取alist接口地址
     *
     * @return
     */
    public static String getAListDomain() {
        if (StringUtils.isBlank(alist_domain)) {
            alist_domain = queryDictTextByKey("alist_param", "url");
        }
        return alist_domain;
    }
}
