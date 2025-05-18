package org.jeecg.modules.openapi.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.JeecgSystemApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,classes = JeecgSystemApplication.class)
@AutoConfigureMockMvc
public class SampleOpenApiTest {
    @Autowired
    private MockMvc mockMvc;
    @Test
    public void test() throws Exception {
        String url = "/openapi/call/wYAu6xwg";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url)
                .param("id","a7d7e77e06c84325a40932163adcdaa6")
                .header("appkey","ak-8CVxh8aYRkzZ0Z2u")
                .header("signature","3ec15caeaf9b6281d0ab825795f61e2d")
                .header("timestamp","1747403650402");
        String result = mockMvc.perform(requestBuilder).andReturn().getResponse().getContentAsString();
        JSONObject jsonObject = JSON.parseObject(result);
        Assertions.assertEquals(true, jsonObject.getBoolean("success"));
        System.out.println(jsonObject);

    }
}
