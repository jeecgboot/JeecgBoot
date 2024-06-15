package org.jeecg.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.modules.system.service.ISysDictService;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ComponentUtil {

    private static ISysDictService sysDictService;

    private static String alist_domain;

    private static String alist_token;

    @Autowired
    public void setBookDao(ISysDictService sysDictService) {
        ComponentUtil.sysDictService = sysDictService;
    }

    public static String queryDictTextByKey(String code,String key) {
        if (sysDictService == null) {
            sysDictService = (ISysDictService) SpringContextUtils.getBean("sysDictService");
        }
        return sysDictService.queryDictTextByKey(code,key);
    }

    /**
     * 获取alist token
     * @return
     * @throws JobExecutionException
     */
    public static String getAListToken() throws JobExecutionException {
        if (StringUtils.isBlank(alist_token)) {
            Map<String,Object> param = new HashMap<>();
            param.put("username", queryDictTextByKey("alist_param","username"));
            param.put("password", queryDictTextByKey("alist_param","password"));
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(param));
            Request request = new Request.Builder()
                    .url(getAListDomain() + Constant.ALIST_AUTH_LOGIN)
                    .method("POST", body)
                    .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = null;
            String responseBody = "";
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            try {
                response = client.newCall(request).execute();
                responseBody = response.body().string();
            } catch (IOException e) {
                throw new JobExecutionException(Constant.ALIST_AUTH_LOGIN + ": 获取token失败");
            }
            JSONObject result = JSONObject.parseObject(responseBody);
            alist_token = result.getJSONObject("data").getString("token");
        }
        return alist_token;
    }

    /**
     * 获取alist接口地址
     * @return
     */
    public static String getAListDomain() {
        if (StringUtils.isBlank(alist_domain)) {
            alist_domain = queryDictTextByKey("alist_param","url");
        }
        return alist_domain;
    }
}
