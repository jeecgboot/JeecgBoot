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
@RequestMapping("/sharding")
public class JeecgShardingDemoController extends JeecgController<ShardingSysLog, IShardingSysLogService> {
    @Autowired
    private IShardingSysLogService shardingSysLogService;

    /**
     * 单库分表 —— 添加
     * @return
     */
    @PostMapping(value = "/test1")
    @ApiOperation(value = "单库分表插入", notes = "单库分表")
    public Result<?> add() {
        log.info("---------------------------------单库分表插入--------------------------------");
        int size = 10;
        for (int i = 0; i < size; i++) {
            ShardingSysLog shardingSysLog = new ShardingSysLog();
            shardingSysLog.setLogContent("jeecg");
            shardingSysLog.setLogType(i);
            shardingSysLog.setOperateType(i);
            shardingSysLogService.save(shardingSysLog);
        }
        return Result.OK("单库分表插入10条数据完成！");
    }

    /**
     * 单库分表 —— 查询
     * @return
     */
    @PostMapping(value = "/list1")
    @ApiOperation(value = "单库分表查询", notes = "单库分表")
    public Result<?> list() {
        return Result.OK(shardingSysLogService.list());
    }

    /**
     * 分库分表 - 插入
     * @return
     */
    @PostMapping(value = "/test2")
    @ApiOperation(value = "分库分表插入", notes = "分库分表")
    public Result<?> test2() {
        int start=20;
        int size=30;
        for (int i = start; i <= size; i++) {
            ShardingSysLog shardingSysLog = new ShardingSysLog();
            shardingSysLog.setLogContent("分库分表测试");
            shardingSysLog.setLogType(0);
            shardingSysLog.setOperateType(i);
            shardingSysLogService.save(shardingSysLog);
        }
        return Result.OK("分库分表插入10条数据完成！");
    }

    /**
     * 分库分表 - 查询
     * @return
     */
    @PostMapping(value = "/list2")
    @ApiOperation(value = "分库分表查询", notes = "分库分表")
    public Result<?> list2() {
        return Result.OK(shardingSysLogService.list());
    }

}
