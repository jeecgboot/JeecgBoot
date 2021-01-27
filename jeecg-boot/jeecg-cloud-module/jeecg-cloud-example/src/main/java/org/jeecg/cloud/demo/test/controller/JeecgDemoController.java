package org.jeecg.cloud.demo.test.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.DictModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Slf4j
@Api(tags = "jeecg-demo")
@RestController
@RequestMapping("/test")
public class JeecgDemoController {


    @Resource
    private ISysBaseAPI sysBaseAPI;

    /**
     * 测试
     *
     * @return
     */
    @GetMapping("/demo")
    @ApiOperation(value = "测试方法", notes = "测试方法")
    public Result mockChange2() {
//        try{
//            //睡5秒，网关Hystrix3秒超时，会触发熔断降级操作
//            Thread.sleep(5000);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        List<DictModel> list = sysBaseAPI.queryAllDict();
        return Result.OK(list);
    }


}
