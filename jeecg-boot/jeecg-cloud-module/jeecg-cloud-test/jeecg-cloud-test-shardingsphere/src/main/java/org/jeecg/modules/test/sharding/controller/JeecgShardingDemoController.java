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
     * 单库分表
     * @return
     */
    @PostMapping(value = "/test1")
    @AutoLog(value = "单库分表")
    @ApiOperation(value = "单库分表", notes = "分库分表添加")
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
    /**
     * 双库分表
     * @return
     */
    @PostMapping(value = "/test2")
    @AutoLog(value = "双库分表")
    @ApiOperation(value = "双库分表", notes = "双库分表")
    public Result<?> test2() {
        for (int i = 20; i <= 30; i++) {
            ShardingSysLog shardingSysLog = new ShardingSysLog();
            shardingSysLog.setLogContent("双库分表测试");
            shardingSysLog.setLogType(i);
            shardingSysLog.setOperateType(i);
            shardingSysLogService.save(shardingSysLog);
        }
        return Result.OK();
    }

}
