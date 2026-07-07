package com.xxl.job.openapi;

import com.xxl.job.core.constant.RegistTypeEnum;
import com.xxl.job.core.openapi.AdminBiz;
import com.xxl.job.core.openapi.model.CallbackRequest;
import com.xxl.job.core.openapi.model.RegistryRequest;
import com.xxl.job.core.context.XxlJobContext;
import com.xxl.job.core.constant.Const;
import com.xxl.tool.http.HttpTool;
import com.xxl.tool.response.Response;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * admin api test
 *
 * @author xuxueli 2017-07-28 22:14:52
 */
public class AdminBizTest {
    private static final Logger logger = LoggerFactory.getLogger(AdminBizTest.class);

    private static String addressUrl = "http://127.0.0.1:8080";
    private static String accessToken = "default_token";

    private AdminBiz buildClient(){
        String finalUrl = addressUrl + "/api";

        return HttpTool.createClient()
                .url(finalUrl)
                .timeout(3 * 1000)
                .header(Const.XXL_JOB_ACCESS_TOKEN, accessToken)
                .proxy(AdminBiz.class);
    }

    @Test
    public void callback() throws Exception {
        AdminBiz adminBiz = buildClient();

        CallbackRequest param = new CallbackRequest();
        param.setLogId(1);
        param.setHandleCode(XxlJobContext.HANDLE_CODE_SUCCESS);

        List<CallbackRequest> callbackParamList = Arrays.asList(param);

        Response<String> returnT = adminBiz.callback(callbackParamList);
        assertTrue(returnT.isSuccess());
    }

    /**
     * registry executor
     *
     * @throws Exception
     */
    @Test
    public void registry() throws Exception {
        AdminBiz adminBiz = buildClient();

        RegistryRequest registryParam = new RegistryRequest(RegistTypeEnum.EXECUTOR.name(), "xxl-job-executor-example", "127.0.0.1:9999");

        Response<String> returnT = adminBiz.registry(registryParam);
        assertTrue(returnT.isSuccess());
    }

    /**
     * registry executor remove
     *
     * @throws Exception
     */
    @Test
    public void registryRemove() throws Exception {
        AdminBiz adminBiz = buildClient();

        RegistryRequest registryParam = new RegistryRequest(RegistTypeEnum.EXECUTOR.name(), "xxl-job-executor-example", "127.0.0.1:9999");

        Response<String> returnT = adminBiz.registryRemove(registryParam);
        assertTrue(returnT.isSuccess());

    }

    // ---------------------- job opt ----------------------

    @Test
    public void jobManage() throws Exception {
        // jobAdd、jobUpdate、jobRemove、jobStart、jobStop
    }

}
