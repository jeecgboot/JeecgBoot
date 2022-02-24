package org.jeecg.modules.test.sharding.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.test.sharding.entity.ShardingSysLog;
import org.jeecg.modules.test.sharding.service.IShardingSysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 分库分表测试
 * @author: zyf
 * @date: 2022/01/24
 * @version: V1.0
 */
@Slf4j
@Api(tags = "分库分表测试")
@RestController
@RequestMapping("/sharding/")
public class JeecgShardingDemoController extends JeecgController<ShardingSysLog, IShardingSysLogService> {
    @Autowired
    private IShardingSysLogService shardingSysLogService;

    /**
     * 添加
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "分库分表添加")
    @ApiOperation(value = "分库分表添加", notes = "分库分表添加")
    public Result<?> add() {
        for (int i = 0; i < 10; i++) {
            ShardingSysLog shardingSysLog = new ShardingSysLog();
            shardingSysLog.setLogContent("jeecg");
            shardingSysLog.setLogType(i);
            shardingSysLog.setOperateType(i);
            shardingSysLogService.save(shardingSysLog);
        }
        return Result.OK();
    }

}
