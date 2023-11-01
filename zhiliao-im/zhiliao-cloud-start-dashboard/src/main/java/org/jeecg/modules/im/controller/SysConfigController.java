package org.jeecg.modules.im.controller;


import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.SysConfig;
import org.jeecg.modules.im.service.SysConfigService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/im/sysConfig")
public class SysConfigController extends BaseBackController {
    @Resource
    private SysConfigService sysConfigService;

    @RequestMapping("/detail")
    public Result<Object> detail(){
        return success(sysConfigService.get());
    }
    /**
     * 更新
     */
    @RequestMapping("/update")
    public Result<Object> update(@RequestBody @Validated SysConfig config, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return fail(bindingResult.getAllErrors().get(0));
        }
        return sysConfigService.updateById(config)?success():fail();
    }
}
