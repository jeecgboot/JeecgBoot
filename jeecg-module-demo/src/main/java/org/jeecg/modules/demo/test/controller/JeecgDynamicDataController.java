package org.jeecg.modules.demo.test.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.demo.test.entity.JeecgDemo;
import org.jeecg.modules.demo.test.service.IJeecgDemoService;
import org.jeecg.modules.demo.test.service.IJeecgDynamicDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: 动态数据源测试
 * @Author: zyf
 * @Date:2020-04-21
 */
@Slf4j
@Tag(name = "动态数据源测试")
@RestController
@RequestMapping("/test/dynamic")
public class JeecgDynamicDataController extends JeecgController<JeecgDemo, IJeecgDemoService> {

    @Autowired
    private IJeecgDynamicDataService jeecgDynamicDataService;


    /**
     * 动态切换数据源

     * @return
     */
    @PostMapping(value = "/test1")
    @AutoLog(value = "动态切换数据源")
    @Operation(summary = "动态切换数据源")
    public Result<List<JeecgDemo>> selectSpelByKey(@RequestParam(required = false) String dsName) {
        List<JeecgDemo> list = jeecgDynamicDataService.selectSpelByKey(dsName);
        return Result.OK(list);
    }


}
