package org.jeecg.modules.system.test;

import org.jeecg.JeecgSystemApplication;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.RestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.ResponseEntity;

/**
 * 系统用户单元测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = JeecgSystemApplication.class)
@SuppressWarnings({"FieldCanBeLocal", "SpringJavaAutowiredMembersInspection"})
public class SysUserTest {
    /**
     * 测试地址：实际使用时替换成你自己的地址
     */
    private final String BASE_URL = "http://localhost:8080/jeecg-boot/sys/user/";
    //测试：用户名和密码
    private final String USERNAME = "admin";
    private final String PASSWORD = "123456";
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 测试用例：查询记录
     */
    @Test
    public void testQuery() {
        // 请求地址
        String url = BASE_URL + "list";
        // 请求 Header （用于传递Token）
        HttpHeaders headers = this.getHeaders();
        // 请求方式是 GET 代表获取数据
        HttpMethod method = HttpMethod.GET;

        System.out.println("请求地址：" + url);
        System.out.println("请求方式：" + method);

        // 利用 RestUtil 请求该url
        ResponseEntity<JSONObject> result = RestUtil.request(url, method, headers, null, null, JSONObject.class);
        if (result != null && result.getBody() != null) {
            System.out.println("返回结果：" + result.getBody().toJSONString());
        } else {
            System.out.println("查询失败");
        }
    }

    /**
     * 测试用例：新增
     */
    @Test
    public void testAdd() {
        // 请求地址
        String url = BASE_URL + "add" ;
        // 请求 Header （用于传递Token）
        HttpHeaders headers = this.getHeaders();
        // 请求方式是 POST 代表提交新增数据
        HttpMethod method = HttpMethod.POST;

        System.out.println("请求地址：" + url);
        System.out.println("请求方式：" + method);

        JSONObject params = new JSONObject();
        params.put("username", "wangwuTest");
        params.put("password", "123456");
        params.put("confirmpassword","123456");
        params.put("realname", "单元测试");
        params.put("activitiSync", "1");
        params.put("userIdentity","1");
        params.put("workNo","0025");

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
        String dataId = "1331795062924374018";
        // 请求地址
        String url = BASE_URL + "edit";
        // 请求 Header （用于传递Token）
        HttpHeaders headers = this.getHeaders();
        // 请求方式是 PUT 代表提交修改数据
        HttpMethod method = HttpMethod.PUT;

        System.out.println("请求地址：" + url);
        System.out.println("请求方式：" + method);

        JSONObject params = new JSONObject();
        params.put("username", "wangwuTest");
        params.put("realname", "单元测试1111");
        params.put("activitiSync", "1");
        params.put("userIdentity","1");
        params.put("workNo","0025");
        params.put("id",dataId);

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
        String dataId = "1331795062924374018";
        // 请求地址
        String url = BASE_URL + "delete" + "?id=" + dataId;
        // 请求 Header （用于传递Token）
        HttpHeaders headers = this.getHeaders();
        // 请求方式是 DELETE 代表删除数据
        HttpMethod method = HttpMethod.DELETE;

        System.out.println("请求地址：" + url);
        System.out.println("请求方式：" + method);

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

    private HttpHeaders getHeaders() {
        String token = this.getToken();
        System.out.println("请求Token：" + token);

        HttpHeaders headers = new HttpHeaders();
        String mediaType = MediaType.APPLICATION_JSON_VALUE;
        headers.setContentType(MediaType.parseMediaType(mediaType));
        headers.set("Accept", mediaType);
        headers.set("X-Access-Token", token);
        return headers;
    }
}
