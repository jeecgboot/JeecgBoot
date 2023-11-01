package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.MucConfig;
import org.jeecg.modules.im.service.MucConfigService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * 群组默认配置
 */
@RestController
@RequestMapping("/im/mucConfig")
public class MucConfigController extends BaseBackController {
    @Resource
    private MucConfigService service;

    /**
     * 详情
     */
    @RequestMapping("/detail")
    public Result<Object> detail(){
        return success(service.findDefault());
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    public Result<Object> update(@RequestBody @Validated MucConfig mucConfig, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return fail(bindingResult.getAllErrors().get(0));
        }
        return service.updateById(mucConfig)?success():fail();
    }
}
