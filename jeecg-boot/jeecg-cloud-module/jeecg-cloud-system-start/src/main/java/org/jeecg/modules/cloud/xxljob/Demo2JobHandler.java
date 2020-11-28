
package org.jeecg.modules.cloud.xxljob;;


import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.core.util.ShardingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * xxl-job定时任务测试
 */
@Component
@Slf4j
public class Demo2JobHandler {


    /**
     * 简单任务
     *
     * @param params
     * @return
     */

    @XxlJob(value = "demoJob2")
    public ReturnT<String> demoJobHandler(String params) {
        log.info("我是定时任务,我执行了...............................");
        return ReturnT.SUCCESS;
    }

    /**
     * 2、分片广播任务
     */

    @XxlJob("shardingJobHandler3")
    public ReturnT<String> shardingJobHandler(String param) throws Exception {

        // 分片参数
        ShardingUtil.ShardingVO shardingVO = ShardingUtil.getShardingVo();
        XxlJobLogger.log("分片参数：当前分片序号 = {}, 总分片数 = {}", shardingVO.getIndex(), shardingVO.getTotal());

        // 业务逻辑
        for (int i = 0; i < shardingVO.getTotal(); i++) {
            if (i == shardingVO.getIndex()) {
                XxlJobLogger.log("第 {} 片, 命中分片开始处理", i);
            } else {
                XxlJobLogger.log("第 {} 片, 忽略", i);
            }
        }

        return ReturnT.SUCCESS;
    }
}

