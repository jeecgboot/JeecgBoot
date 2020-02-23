package org.jeecg.modules.online.desform.test;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.RestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 表单设计器 API 接口单元测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SuppressWarnings({"FieldCanBeLocal", "SpringJavaAutowiredMembersInspection"})
public class DesformApiTest {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 测试地址：实际使用时替换成你自己的地址
     */
    private final String BASE_URL = "http://localhost:8080/jeecg-boot/desform/api/";

    // 请实际使用时替换成你自己的用户名和密码
    private final String USERNAME = "admin";
    private final String PASSWORD = "123456";

    /**
     * 表单code，实际使用时可以替换成你要测试的表单code
     */
    private final String DESFORM_CODE = "qingjiadan";

    /**
     * 测试用例：新增
     */
    @Test
    public void testAdd() {
        // 用户Token
        String token = this.getToken();
        // 请求地址
        String url = BASE_URL + DESFORM_CODE;
        // 请求 Header （用于传递Token）
        HttpHeaders headers = this.getHeaders(token);
        // 请求方式是 POST 代表提交新增数据
        HttpMethod method = HttpMethod.POST;

        System.out.println("请求地址：" + url);
        System.out.println("请求方式：" + method);
        System.out.println("请求Token：" + token);

        JSONObject params = new JSONObject();
        params.put("name", "张三");
        params.put("sex", "1");
        params.put("begin_time", "2019-12-27");
        params.put("remarks", "生病了");

        System.out.println("请求参数：" + params.toJSONString());

        // 利用 RestUtil 请求该url
        ResponseEntity<JSONObject> result = RestUtil.request(url, method, headers, null, params, JSONObject.class);
        if (result != null && result.getBody() != null) {
            System.out.println("返回结果：" + result.getBody().toJSONString());
        } else {
            System.out.println("查询失败");
        }
    }


    /**
     * 测试用例：修改
     */
    @Test
    public void testEdit() {
        // 数据Id
        String dataId = "f43ea15c654337fbcb2336dd5422ffc3";
        // 用户Token
        String token = this.getToken();
        // 请求地址
        String url = BASE_URL + DESFORM_CODE + "/" + dataId;
        // 请求 Header （用于传递Token）
        HttpHeaders headers = this.getHeaders(token);
        // 请求方式是 PUT 代表提交修改数据
        HttpMethod method = HttpMethod.PUT;

        System.out.println("请求地址：" + url);
        System.out.println("请求方式：" + method);
        System.out.println("请求Token：" + token);

        JSONObject params = new JSONObject();
        params.put("name", "李四");
        params.put("sex", "0");
        params.put("begin_time", "2019-12-27");
        params.put("remarks", "感冒了");

        System.out.println("请求参数：" + params.toJSONString());

        // 利用 RestUtil 请求该url
        ResponseEntity<JSONObject> result = RestUtil.request(url, method, headers, null, params, JSONObject.class);
        if (result != null && result.getBody() != null) {
            System.out.println("返回结果：" + result.getBody().toJSONString());
        } else {
            System.out.println("查询失败");
        }
    }


    /**
     * 测试用例：删除
     */
    @Test
    public void testDelete() {
        // 数据Id
        String dataId = "f43ea15c654337fbcb2336dd5422ffc3";
        // 用户Token
        String token = this.getToken();
        // 请求地址
        String url = BASE_URL + DESFORM_CODE + "/" + dataId;
        // 请求 Header （用于传递Token）
        HttpHeaders headers = this.getHeaders(token);
        // 请求方式是 DELETE 代表删除数据
        HttpMethod method = HttpMethod.DELETE;

        System.out.println("请求地址：" + url);
        System.out.println("请求方式：" + method);
        System.out.println("请求Token：" + token);

        // 利用 RestUtil 请求该url
        ResponseEntity<JSONObject> result = RestUtil.request(url, method, headers, null, null, JSONObject.class);
        if (result != null && result.getBody() != null) {
            System.out.println("返回结果：" + result.getBody().toJSONString());
        } else {
            System.out.println("查询失败");
        }
    }

    /**
     * 测试用例：查询记录
     */
    @Test
    public void testQuery() {
        // 数据Id
        String dataId = "18146ddaa062296442a9310a51baf67b";
        // 用户Token
        String token = this.getToken();
        // 请求地址
        String url = BASE_URL + DESFORM_CODE + "/" + dataId;
        // 请求 Header （用于传递Token）
        HttpHeaders headers = this.getHeaders(token);
        // 请求方式是 GET 代表获取数据
        HttpMethod method = HttpMethod.GET;

        System.out.println("请求地址：" + url);
        System.out.println("请求方式：" + method);
        System.out.println("请求Token：" + token);

        // 利用 RestUtil 请求该url
        ResponseEntity<JSONObject> result = RestUtil.request(url, method, headers, null, null, JSONObject.class);
        if (result != null && result.getBody() != null) {
            System.out.println("返回结果：" + result.getBody().toJSONString());
        } else {
            System.out.println("查询失败");
        }
    }

    private String getToken() {
        String token = JwtUtil.sign(USERNAME, PASSWORD);
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, 60);
        return token;
    }

    private HttpHeaders getHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        String mediaType = MediaType.APPLICATION_JSON_UTF8_VALUE;
        headers.setContentType(MediaType.parseMediaType(mediaType));
        headers.set("Accept", mediaType);
        headers.set("X-Access-Token", token);
        return headers;
    }

}
