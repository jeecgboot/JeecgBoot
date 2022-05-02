
package org.jeecg.modules.test.xxljob;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 *  xxl-job定时任务测试
 * @author: zyf
 * @date: 2022/04/21
 */
@Component
@Slf4j
public class XxclJobTest {


    /**
     * 简单任务
     *
     * @param params
     * @return
     */

    @XxlJob(value = "xxclJobTest")
    public ReturnT<String> demoJobHandler(String params) {
        log.info("我是 jeecg-system 服务里的定时任务 xxclJobTest , 我执行了...............................");
        return ReturnT.SUCCESS;
    }

    public void init() {
        log.info("init");
    }

    public void destroy() {
        log.info("destory");
    }

}

