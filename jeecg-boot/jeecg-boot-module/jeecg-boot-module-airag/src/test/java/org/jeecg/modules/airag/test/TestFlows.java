package org.jeecg.modules.airag.test;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @Description: 流程测试
 * @Author: chenrui
 * @Date: 2025/2/11 16:11
 */
@Slf4j
public class TestFlows {

    @Test
    public void testRunFlow(){
        String id = "1889499701976358913";
//        String id = "1889571074002247682"; //switch
//        String id = "1889608218175463425"; //脚本
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3Mzk1NDY0NDIsInVzZXJuYW1lIjoiamVlY2cifQ.CFIV79PUYmOAiqBKT3yjwihHWwf954DvS-4oKERmJVU";
        String request = request(id,token);
        System.out.println(request);
    }

    private String request(String id,String token) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://localhost:7008/airag/airagFlow/flow/run/" + id + "?field1=%25E5%2593%2588%25E5%2593%2588&field2=%25E4%25B8%25AD%25E5%259B%25BD")
                .get()
                .addHeader("X-Access-Token", token)
                .addHeader("Accept", "*/*")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .addHeader("User-Agent", "PostmanRuntime-ApipostRuntime/1.1.0")
                .addHeader("Connection", "keep-alive")
                .addHeader("Cookie", "JSESSIONID=442C48D3D1D0B2878A597AB6EBF2A07E")
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    // TODO author: chenrui for:完善用例,使用java方式调用 date:2025/2/14

}
