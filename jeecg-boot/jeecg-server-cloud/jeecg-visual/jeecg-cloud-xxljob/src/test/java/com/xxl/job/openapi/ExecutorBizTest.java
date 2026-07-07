package com.xxl.job.openapi;

import com.xxl.job.core.constant.Const;
import com.xxl.job.core.openapi.ExecutorBiz;
import com.xxl.job.core.openapi.model.*;
import com.xxl.job.core.constant.ExecutorBlockStrategyEnum;
import com.xxl.job.core.glue.GlueTypeEnum;
import com.xxl.tool.http.HttpTool;
import com.xxl.tool.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * executor api test
 *
 * Created by xuxueli on 17/5/12.
 */
public class ExecutorBizTest {
    private static final Logger logger = LoggerFactory.getLogger(ExecutorBizTest.class);

    private static String addressUrl = "http://127.0.0.1:9999/";
    private static String accessToken = "default_token";

    private ExecutorBiz buildClient(){
        return HttpTool.createClient()
                .url(addressUrl)
                .timeout(3 * 1000)
                .header(Const.XXL_JOB_ACCESS_TOKEN, accessToken)
                .proxy(ExecutorBiz.class);
    }

    @Test
    public void beat() throws Exception {
        ExecutorBiz executorBiz = buildClient();
        // Act
        final Response<String> retval = executorBiz.beat();

        // Assert result
        Assertions.assertNotNull(retval);
        Assertions.assertNull(((Response<String>) retval).getData());
        Assertions.assertEquals(200, retval.getCode());
        Assertions.assertNull(retval.getMsg());
    }

    @Test
    public void idleBeat(){
        ExecutorBiz executorBiz = buildClient();

        final int jobId = 0;

        // Act
        final Response<String> retval = executorBiz.idleBeat(new IdleBeatRequest(jobId));

        // Assert result
        Assertions.assertNotNull(retval);
        Assertions.assertNull(((Response<String>) retval).getData());
        Assertions.assertEquals(500, retval.getCode());
        Assertions.assertEquals("job thread is running or has trigger queue.", retval.getMsg());
    }

    @Test
    public void run(){
        ExecutorBiz executorBiz = buildClient();

        // trigger data
        final TriggerRequest triggerParam = new TriggerRequest();
        triggerParam.setJobId(1);
        triggerParam.setExecutorHandler("demoJobHandler");
        triggerParam.setExecutorParams(null);
        triggerParam.setExecutorBlockStrategy(ExecutorBlockStrategyEnum.COVER_EARLY.name());
        triggerParam.setGlueType(GlueTypeEnum.BEAN.name());
        triggerParam.setGlueSource(null);
        triggerParam.setGlueUpdatetime(System.currentTimeMillis());
        triggerParam.setLogId(1);
        triggerParam.setLogDateTime(System.currentTimeMillis());

        // Act
        final Response<String> retval = executorBiz.run(triggerParam);

        // Assert result
        Assertions.assertNotNull(retval);

    }

    @Test
    public void kill(){
        ExecutorBiz executorBiz = buildClient();

        final int jobId = 0;

        // Act
        final Response<String> retval = executorBiz.kill(new KillRequest(jobId));

        // Assert result
        Assertions.assertNotNull(retval);
        Assertions.assertNull(((Response<String>) retval).getData());
        Assertions.assertEquals(200, retval.getCode());
        Assertions.assertNull(retval.getMsg());
    }

    @Test
    public void log(){
        ExecutorBiz executorBiz = buildClient();

        final long logDateTim = 0L;
        final long logId = 0;
        final int fromLineNum = 0;

        // Act
        final Response<LogResult> retval = executorBiz.log(new LogRequest(logDateTim, logId, fromLineNum));

        // Assert result
        Assertions.assertNotNull(retval);
    }

}
