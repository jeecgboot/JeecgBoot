package org.jeecg.modules.smartfuel.test.controller;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.JeecgSystemApplication;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.RestUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = JeecgSystemApplication.class)
class InfluxDBTestControllerTest {


    /**
     * 测试时如果结果数据量很大，需要把restUtil类61行的读取时间设大一点
     * 比如requestFactory.setReadTimeout(20000)，设置20s
     * 不然输出结果读取会报错 Caused by: java.net.SocketTimeoutException: Read timed out
     */
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 测试地址：实际使用时替换成你自己的地址
     */
    private final String BASE_URL = "http://localhost:8080/jeecg-boot/test/influxdbTest/";

    // 请实际使用时替换成你自己的用户名和密码
    private final String USERNAME = "admin";
    private final String PASSWORD = "123456";

    @Test
    void queryInterval() {
        // 用户Token
        String token = this.getToken();
        // 请求地址
        String url = BASE_URL + "QueryInterval";
        // 请求 Header （用于传递Token）
        HttpHeaders headers = this.getHeaders(token);
        // 请求方式是 GET 代表获取数据
        HttpMethod method = HttpMethod.GET;
        //各项请求参数
        //List<String> list = Arrays.asList("BF_IN[0]", "BF_IN[33]", "BF_IN[35]", "BF_IN[36]");
        //不能直接用List<String> list，url拼接的时候会把[]中括号带上，导致一头一尾参数不对，可以直接用下面String
        String list = "BF_IN[0], BF_IN[33], BF_IN[35], BF_IN[36]";
        String startTime = "2022-02-01T08:00:00";
        String endTime = "2022-03-01T08:00:00";
        String every = "10s";//间隔时间
        String number = "1";//数据源编号

        //拼接请求参数
        JSONObject variables = new JSONObject();
        variables.put("list", list);
        variables.put("startTime", startTime);
        variables.put("endTime", endTime);
        variables.put("every", every);
        variables.put("number", number);
        //打印请求相关参数
        System.out.println("请求地址：" + url);
        System.out.println("请求方式：" + method);
        System.out.println("请求Token：" + token);
        System.out.println("请求参数：" + variables);

        // 利用 RestUtil 请求该url
        ResponseEntity<JSONObject> result = RestUtil.request(url, method, headers, variables, null, JSONObject.class);
        if (result != null && result.getBody() != null) {
            System.out.println("返回结果：" + result.getBody().toJSONString());//数据太多时，不会全部打印出来，控制台输出行数有限制，默认1024kb,可以自行设置
        } else {
            System.out.println("查询失败");
        }
    }

    @Test
    void getIncrease() {
        // 用户Token
        String token = this.getToken();
        // 请求地址
        String url = BASE_URL + "GetIncrease";
        // 请求 Header （用于传递Token）
        HttpHeaders headers = this.getHeaders(token);
        // 请求方式是 GET 代表获取数据
        HttpMethod method = HttpMethod.GET;
        //各项请求参数
        //List<String> list = Arrays.asList("BF_IN[0]", "BF_IN[33]", "BF_IN[35]", "BF_IN[36]");
        //不能直接用List<String> list，url拼接的时候会把[]中括号带上，导致一头一尾参数不对，可以直接用下面String
        String list = "BF_IN[0], BF_IN[33], BF_IN[35], BF_IN[36]";
        String startTime = "2022-01-01T08:00:00";
        String endTime = "2022-03-01T08:00:00";
        String number = "1";//数据源编号
        //拼接请求参数
        JSONObject variables = new JSONObject();
        variables.put("list", list);
        variables.put("startTime", startTime);
        variables.put("endTime", endTime);
        variables.put("number", number);
        //打印请求相关参数
        System.out.println("请求地址：" + url);
        System.out.println("请求方式：" + method);
        System.out.println("请求Token：" + token);
        System.out.println("请求参数：" + variables);

        // 利用 RestUtil 请求该url
        ResponseEntity<JSONObject> result = RestUtil.request(url, method, headers, variables, null, JSONObject.class);
        if (result != null && result.getBody() != null) {
            System.out.println("返回结果：" + result.getBody().toJSONString());
        } else {
            System.out.println("查询失败");
        }
    }

    @Test
    void searchDI() {
        // 用户Token
        String token = this.getToken();
        // 请求地址
        String url = BASE_URL + "SearchDI";
        // 请求 Header （用于传递Token）
        HttpHeaders headers = this.getHeaders(token);
        // 请求方式是 GET 代表获取数据
        HttpMethod method = HttpMethod.GET;
        //各项请求参数
        //List<String> list = Arrays.asList("BF_IN[0]", "BF_IN[33]", "BF_IN[35]", "BF_IN[36]");
        //不能直接用List<String> list，url拼接的时候会把[]中括号带上，导致一头一尾参数不对，可以直接用下面String
        String list = "BF_IN[0], BF_IN[33], BF_IN[35], BF_IN[36]";
        String startTime = "2022-02-01T08:00:00";
        String endTime = "2022-03-01T08:00:00";
        String every = "10s";//间隔时间
        String number = "1";//数据源编号
        //拼接请求参数
        JSONObject variables = new JSONObject();
        variables.put("list", list);
        variables.put("startTime", startTime);
        variables.put("endTime", endTime);
        variables.put("every", every);
        variables.put("number", number);
        //打印请求相关参数
        System.out.println("请求地址：" + url);
        System.out.println("请求方式：" + method);
        System.out.println("请求Token：" + token);
        System.out.println("请求参数：" + variables);

        // 利用 RestUtil 请求该url
        ResponseEntity<JSONObject> result = RestUtil.request(url, method, headers, variables, null, JSONObject.class);
        if (result != null && result.getBody() != null) {
            System.out.println("返回结果：" + result.getBody().toJSONString());
        } else {
            System.out.println("查询失败");
        }

    }

    @Test
    void searchTotal() {
        // 用户Token
        String token = this.getToken();
        // 请求地址
        String url = BASE_URL + "SearchTotal";
        // 请求 Header （用于传递Token）
        HttpHeaders headers = this.getHeaders(token);
        // 请求方式是 GET 代表获取数据
        HttpMethod method = HttpMethod.GET;
        //各项请求参数
        //List<String> list = Arrays.asList("BF_IN[0]", "BF_IN[33]", "BF_IN[35]", "BF_IN[36]");
        //不能直接用List<String> list，url拼接的时候会把[]中括号带上，导致一头一尾参数不对，可以直接用下面String
        String list = "BF_IN[0], BF_IN[33], BF_IN[35], BF_IN[36]";
        String startTime = "2022-02-01T08:00:00";
        String endTime = "2022-02-15T08:00:00";
        String every = "10s";//间隔时间
        String number = "1";//数据源编号
        //拼接请求参数
        JSONObject variables = new JSONObject();
        variables.put("list", list);
        variables.put("startTime", startTime);
        variables.put("endTime", endTime);
        variables.put("every", every);
        variables.put("number", number);
        //打印请求相关参数
        System.out.println("请求地址：" + url);
        System.out.println("请求方式：" + method);
        System.out.println("请求Token：" + token);
        System.out.println("请求参数：" + variables);

        // 利用 RestUtil 请求该url
        ResponseEntity<JSONObject> result = RestUtil.request(url, method, headers, variables, null, JSONObject.class);
        if (result != null && result.getBody() != null) {
            System.out.println("返回结果：" + result.getBody().toJSONString());
        } else {
            System.out.println("查询失败");
        }
    }

    @Test
    void search() {
        // 用户Token
        String token = this.getToken();
        // 请求地址
        String url = BASE_URL + "Search";
        // 请求 Header （用于传递Token）
        HttpHeaders headers = this.getHeaders(token);
        // 请求方式是 GET 代表获取数据
        HttpMethod method = HttpMethod.GET;
        //各项请求参数
        //List<String> list = Arrays.asList("BF_IN[0]", "BF_IN[33]", "BF_IN[35]", "BF_IN[36]");
        //不能直接用List<String> list，url拼接的时候会把[]中括号带上，导致一头一尾参数不对，可以直接用下面String
        String list = "BF_IN[0], BF_IN[33], BF_IN[35], BF_IN[36]";
        String startTime = "2022-02-01T08:00:00";
        String endTime = "2022-02-10T08:00:00";
        String every = "10s";//间隔时间
        String number = "1";//数据源编号

        //拼接请求参数
        JSONObject variables = new JSONObject();
        variables.put("list", list);
        variables.put("startTime", startTime);
        variables.put("endTime", endTime);
        variables.put("every", every);
        variables.put("number", number);
        //打印请求相关参数
        System.out.println("请求地址：" + url);
        System.out.println("请求方式：" + method);
        System.out.println("请求Token：" + token);
        System.out.println("请求参数：" + variables);

        // 利用 RestUtil 请求该url
        ResponseEntity<JSONObject> result = RestUtil.request(url, method, headers, variables, null, JSONObject.class);
        if (result != null && result.getBody() != null) {
            System.out.println("返回结果：" + result.getBody().toJSONString());
        } else {
            System.out.println("查询失败");
        }
    }

    @Test
    void searchBatchMaps() {
        // 用户Token
        String token = this.getToken();
        // 请求地址
        String url = BASE_URL + "SearchBatchMaps";
        // 请求 Header （用于传递Token）
        HttpHeaders headers = this.getHeaders(token);
        // 请求方式是 GET 代表获取数据
        HttpMethod method = HttpMethod.GET;
        //各项请求参数
        //List<String> list = Arrays.asList("BF_IN[0]", "BF_IN[33]", "BF_IN[35]", "BF_IN[36]");
        //不能直接用List<String> list，url拼接的时候会把[]中括号带上，导致一头一尾参数不对，可以直接用下面String
        String list = "BF_IN[0], BF_IN[33], BF_IN[35], BF_IN[36]";
        String startTime = "2022-02-01T08:00:00";
        String endTime = "2022-02-05T08:00:00";
        String every = "10s";//间隔时间
        String number = "1";//数据源编号
        //拼接请求参数
        JSONObject variables = new JSONObject();
        variables.put("list", list);
        variables.put("startTime", startTime);
        variables.put("endTime", endTime);
        variables.put("every", every);
        variables.put("number", number);
        //打印请求相关参数
        System.out.println("请求地址：" + url);
        System.out.println("请求方式：" + method);
        System.out.println("请求Token：" + token);
        System.out.println("请求参数：" + variables);

        // 利用 RestUtil 请求该url
        ResponseEntity<JSONObject> result = RestUtil.request(url, method, headers, variables, null, JSONObject.class);
        if (result != null && result.getBody() != null) {
            System.out.println("返回结果：" + result.getBody().toJSONString());
        } else {
            System.out.println("查询失败");
        }
    }

    @Test
    void getRangeTagMarginValue() {
        // 用户Token
        String token = this.getToken();
        // 请求地址
        String url = BASE_URL + "GetRangeTagMarginValue";
        // 请求 Header （用于传递Token）
        HttpHeaders headers = this.getHeaders(token);
        // 请求方式是 GET 代表获取数据
        HttpMethod method = HttpMethod.GET;
        //各项请求参数
        //List<String> list = Arrays.asList("BF_IN[0]", "BF_IN[33]", "BF_IN[35]", "BF_IN[36]");
        //不能直接用List<String> list，url拼接的时候会把[]中括号带上，导致一头一尾参数不对，可以直接用下面String
        String list = "BF_IN[0], BF_IN[33], BF_IN[35], BF_IN[36]";
        String startTime = "2022-02-01T08:00:00";
        String endTime = "2022-02-05T08:00:00";
        String every = "10s";//间隔时间
        String number = "1";//数据源编号

        //拼接请求参数
        JSONObject variables = new JSONObject();
        variables.put("list", list);
        variables.put("startTime", startTime);
        variables.put("endTime", endTime);
        variables.put("every", every);
        variables.put("number", number);
        //打印请求相关参数
        System.out.println("请求地址：" + url);
        System.out.println("请求方式：" + method);
        System.out.println("请求Token：" + token);
        System.out.println("请求参数：" + variables);

        // 利用 RestUtil 请求该url
        ResponseEntity<JSONObject> result = RestUtil.request(url, method, headers, variables, null, JSONObject.class);
        if (result != null && result.getBody() != null) {
            System.out.println("返回结果：" + result.getBody().toJSONString());
        } else {
            System.out.println("查询失败");
        }
    }

    @Test
    void searchRangeChangeMaps() {
        // 用户Token
        String token = this.getToken();
        // 请求地址
        String url = BASE_URL + "SearchRangeChangeMaps";
        // 请求 Header （用于传递Token）
        HttpHeaders headers = this.getHeaders(token);
        // 请求方式是 GET 代表获取数据
        HttpMethod method = HttpMethod.GET;
        //各项请求参数
        //List<String> list = Arrays.asList("BF_IN[0]", "BF_IN[33]", "BF_IN[35]", "BF_IN[36]");
        //不能直接用List<String> list，url拼接的时候会把[]中括号带上，导致一头一尾参数不对，可以直接用下面String
        String list = "BF_IN[0], BF_IN[33], BF_IN[35], BF_IN[36]";
        String startTime = "2022-02-01T08:00:00";
        String endTime = "2022-02-05T08:00:00";
        String intervalSeconds = "10s";//间隔时间
        String number = "1";//数据源编号
        Double min = 1.0;
        Double max = 5.0;
        //拼接请求参数
        JSONObject variables = new JSONObject();
        variables.put("list", list);
        variables.put("startTime", startTime);
        variables.put("endTime", endTime);
        variables.put("intervalSeconds", intervalSeconds);
        variables.put("number", number);
        variables.put("min", min);
        variables.put("max", max);
        //打印请求相关参数
        System.out.println("请求地址：" + url);
        System.out.println("请求方式：" + method);
        System.out.println("请求Token：" + token);
        System.out.println("请求参数：" + variables);

        // 利用 RestUtil 请求该url
        ResponseEntity<JSONObject> result = RestUtil.request(url, method, headers, variables, null, JSONObject.class);
        if (result != null && result.getBody() != null) {
            System.out.println("返回结果：" + result.getBody().toJSONString());
        } else {
            System.out.println("查询失败");
        }
    }

    @Test
    void getRangeUpTimeSpan() {
        // 用户Token
        String token = this.getToken();
        // 请求地址
        String url = BASE_URL + "GetRangeUpTimeSpan";
        // 请求 Header （用于传递Token）
        HttpHeaders headers = this.getHeaders(token);
        // 请求方式是 GET 代表获取数据
        HttpMethod method = HttpMethod.GET;
        //各项请求参数
        //List<String> list = Arrays.asList("BF_IN[0]", "BF_IN[33]", "BF_IN[35]", "BF_IN[36]");
        //不能直接用List<String> list，url拼接的时候会把[]中括号带上，导致一头一尾参数不对，可以直接用下面String
        String list = "BF_IN[0], BF_IN[33], BF_IN[35], BF_IN[36]";
        String startTime = "2022-02-01T08:00:00";
        String endTime = "2022-02-05T08:00:00";
        String every = "10s";//间隔时间
        String number = "1";//数据源编号
        Double flagMinValue = 1.0;
        Double flagMaxValue = 5.0;
        Integer searchDay = 28;//验推天数  不能为0

        //拼接请求参数
        JSONObject variables = new JSONObject();
        variables.put("list", list);
        variables.put("startTime", startTime);
        variables.put("endTime", endTime);
        variables.put("every", every);
        variables.put("number", number);
        variables.put("flagMinValue", flagMinValue);
        variables.put("flagMaxValue", flagMaxValue);
        variables.put("searchDay", searchDay);
        //打印请求相关参数
        System.out.println("请求地址：" + url);
        System.out.println("请求方式：" + method);
        System.out.println("请求Token：" + token);
        System.out.println("请求参数：" + variables);

        // 利用 RestUtil 请求该url
        ResponseEntity<JSONObject> result = RestUtil.request(url, method, headers, variables, null, JSONObject.class);
        if (result != null && result.getBody() != null) {
            System.out.println("返回结果：" + result.getBody().toJSONString());
        } else {
            System.out.println("查询失败");
        }
    }

    @Test
    void getTimeBeforeValue() {
        // 用户Token
        String token = this.getToken();
        // 请求地址
        String url = BASE_URL + "GetTimeBeforeValue";
        // 请求 Header （用于传递Token）
        HttpHeaders headers = this.getHeaders(token);
        // 请求方式是 GET 代表获取数据
        HttpMethod method = HttpMethod.GET;
        //各项请求参数
        //List<String> list = Arrays.asList("BF_IN[0]", "BF_IN[33]", "BF_IN[35]", "BF_IN[36]");
        //不能直接用List<String> list，url拼接的时候会把[]中括号带上，导致一头一尾参数不对，可以直接用下面String
        String list = "BF_IN[0], BF_IN[33], BF_IN[35], BF_IN[36]";
        String startTime = "2022-02-01T08:00:00";
        String endTime = "2022-03-01T08:00:00";
        String number = "1";//数据源编号
        String searchDay = "7";//数据源编号
        //拼接请求参数
        JSONObject variables = new JSONObject();
        variables.put("list", list);
        variables.put("startTime", startTime);
        variables.put("endTime", endTime);
        variables.put("number", number);
        variables.put("searchDay", searchDay);
        //打印请求相关参数
        System.out.println("请求地址：" + url);
        System.out.println("请求方式：" + method);
        System.out.println("请求Token：" + token);
        System.out.println("请求参数：" + variables);

        // 利用 RestUtil 请求该url
        ResponseEntity<JSONObject> result = RestUtil.request(url, method, headers, variables, null, JSONObject.class);
        if (result != null && result.getBody() != null) {
            System.out.println("返回结果：" + result.getBody().toJSONString());
        } else {
            System.out.println("查询失败");
        }
    }

    @Test
    void getValuesMax() {
        // 用户Token
        String token = this.getToken();
        // 请求地址
        String url = BASE_URL + "GetValuesMax";
        // 请求 Header （用于传递Token）
        HttpHeaders headers = this.getHeaders(token);
        // 请求方式是 GET 代表获取数据
        HttpMethod method = HttpMethod.GET;
        //各项请求参数
        //List<String> list = Arrays.asList("BF_IN[0]", "BF_IN[33]", "BF_IN[35]", "BF_IN[36]");
        //不能直接用List<String> list，url拼接的时候会把[]中括号带上，导致一头一尾参数不对，可以直接用下面String
        String list = "BF_IN[0], BF_IN[33], BF_IN[35], BF_IN[36]";
        String startTime = "2022-02-01T08:00:00";
        String endTime = "2022-02-19T08:00:00";
        String number = "1";//数据源编号
        //拼接请求参数
        JSONObject variables = new JSONObject();
        variables.put("list", list);
        variables.put("startTime", startTime);
        variables.put("endTime", endTime);
        variables.put("number", number);
        //打印请求相关参数
        System.out.println("请求地址：" + url);
        System.out.println("请求方式：" + method);
        System.out.println("请求Token：" + token);
        System.out.println("请求参数：" + variables);

        // 利用 RestUtil 请求该url
        ResponseEntity<JSONObject> result = RestUtil.request(url, method, headers, variables, null, JSONObject.class);
        if (result != null && result.getBody() != null) {
            System.out.println("返回结果：" + result.getBody().toJSONString());
        } else {
            System.out.println("查询失败");
        }
    }

    @Test
    void getValuesMin() {
        // 用户Token
        String token = this.getToken();
        // 请求地址
        String url = BASE_URL + "GetValuesMin";
        // 请求 Header （用于传递Token）
        HttpHeaders headers = this.getHeaders(token);
        // 请求方式是 GET 代表获取数据
        HttpMethod method = HttpMethod.GET;
        //各项请求参数
        //List<String> list = Arrays.asList("BF_IN[0]", "BF_IN[33]", "BF_IN[35]", "BF_IN[36]");
        //不能直接用List<String> list，url拼接的时候会把[]中括号带上，导致一头一尾参数不对，可以直接用下面String
        String list = "BF_IN[0], BF_IN[33], BF_IN[35], BF_IN[36]";
        String startTime = "2022-02-01T08:00:00";
        String endTime = "2022-03-01T08:00:00";
        String number = "1";//数据源编号
        //拼接请求参数
        JSONObject variables = new JSONObject();
        variables.put("list", list);
        variables.put("startTime", startTime);
        variables.put("endTime", endTime);
        variables.put("number", number);
        //打印请求相关参数
        System.out.println("请求地址：" + url);
        System.out.println("请求方式：" + method);
        System.out.println("请求Token：" + token);
        System.out.println("请求参数：" + variables);

        // 利用 RestUtil 请求该url
        ResponseEntity<JSONObject> result = RestUtil.request(url, method, headers, variables, null, JSONObject.class);
        if (result != null && result.getBody() != null) {
            System.out.println("返回结果：" + result.getBody().toJSONString());
        } else {
            System.out.println("查询失败");
        }
    }


    @Test
    void getStatusTagCount() {
        // 用户Token
        String token = this.getToken();
        // 请求地址
        String url = BASE_URL + "GetStatusTagCount";
        // 请求 Header （用于传递Token）
        HttpHeaders headers = this.getHeaders(token);
        // 请求方式是 GET 代表获取数据
        HttpMethod method = HttpMethod.GET;
        //各项请求参数
        //List<String> list = Arrays.asList("BF_IN[0]", "BF_IN[33]", "BF_IN[35]", "BF_IN[36]");
        //不能直接用List<String> list，url拼接的时候会把[]中括号带上，导致一头一尾参数不对，可以直接用下面String
        String list = "BF_IN[0], BF_IN[33], BF_IN[35], BF_IN[36]";
        String startTime = "2022-02-01T08:00:00";
        String endTime = "2022-02-05T08:00:00";
        String number = "1";//数据源编号
        //拼接请求参数
        JSONObject variables = new JSONObject();
        variables.put("list", list);
        variables.put("startTime", startTime);
        variables.put("endTime", endTime);
        variables.put("number", number);
        //打印请求相关参数
        System.out.println("请求地址：" + url);
        System.out.println("请求方式：" + method);
        System.out.println("请求Token：" + token);
        System.out.println("请求参数：" + variables);

        // 利用 RestUtil 请求该url
        ResponseEntity<JSONObject> result = RestUtil.request(url, method, headers, variables, null, JSONObject.class);
        if (result != null && result.getBody() != null) {
            System.out.println("返回结果：" + result.getBody().toJSONString());
        } else {
            System.out.println("查询失败");
        }
    }

    @Test
    void getStatusTagTimeSpan() {
        // 用户Token
        String token = this.getToken();
        // 请求地址
        String url = BASE_URL + "GetStatusTagTimeSpan";
        // 请求 Header （用于传递Token）
        HttpHeaders headers = this.getHeaders(token);
        // 请求方式是 GET 代表获取数据
        HttpMethod method = HttpMethod.GET;
        //各项请求参数
        //List<String> list = Arrays.asList("BF_IN[0]", "BF_IN[33]", "BF_IN[35]", "BF_IN[36]");
        //不能直接用List<String> list，url拼接的时候会把[]中括号带上，导致一头一尾参数不对，可以直接用下面String
        String list = "BF_IN[0], BF_IN[33], BF_IN[35], BF_IN[36]";
        String startTime = "2022-02-01T08:00:00";
        String endTime = "2022-03-01T13:00:00";
        String number = "1";//数据源编号
        //拼接请求参数
        JSONObject variables = new JSONObject();
        variables.put("list", list);
        variables.put("startTime", startTime);
        variables.put("endTime", endTime);
        variables.put("number", number);
        //打印请求相关参数
        System.out.println("请求地址：" + url);
        System.out.println("请求方式：" + method);
        System.out.println("请求Token：" + token);
        System.out.println("请求参数：" + variables);

        // 利用 RestUtil 请求该url
        ResponseEntity<JSONObject> result = RestUtil.request(url, method, headers, variables, null, JSONObject.class);
        if (result != null && result.getBody() != null) {
            System.out.println("返回结果：" + result.getBody().toJSONString());
        } else {
            System.out.println("查询失败");
        }
    }

    @Test
    void getDayAverage() {
        // 用户Token
        String token = this.getToken();
        // 请求地址
        String url = BASE_URL + "GetDayAverage";
        // 请求 Header （用于传递Token）
        HttpHeaders headers = this.getHeaders(token);
        // 请求方式是 GET 代表获取数据
        HttpMethod method = HttpMethod.GET;
        //各项请求参数
        //List<String> list = Arrays.asList("BF_IN[0]", "BF_IN[33]", "BF_IN[35]", "BF_IN[36]");
        //不能直接用List<String> list，url拼接的时候会把[]中括号带上，导致一头一尾参数不对，可以直接用下面String
        String list = "BF_IN[0], BF_IN[33], BF_IN[35], BF_IN[36]";
        String startTime = "2022-02-01T08:00:00";
        String endTime = "2022-02-11T08:00:00";
        String number = "1";//数据源编号
        //拼接请求参数
        JSONObject variables = new JSONObject();
        variables.put("list", list);
        variables.put("startTime", startTime);
        variables.put("endTime", endTime);
        variables.put("number", number);
        //打印请求相关参数
        System.out.println("请求地址：" + url);
        System.out.println("请求方式：" + method);
        System.out.println("请求Token：" + token);
        System.out.println("请求参数：" + variables);

        // 利用 RestUtil 请求该url
        ResponseEntity<JSONObject> result = RestUtil.request(url, method, headers, variables, null, JSONObject.class);
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