package org.jeecg.modules.im.controller;


import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.ClientConfig;
import org.jeecg.modules.im.service.ClientConfigService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/im/clientConfig")
public class ClientConfigController extends BaseBackController {
    @Resource
    private ClientConfigService clientConfigService;

    @RequestMapping("/detail")
    public Result<Object> detail(){
        return success(clientConfigService.get());
    }
    /**
     * 更新
     */
    @RequestMapping("/update")
    public Result<Object> update(@RequestBody @Validated ClientConfig config, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return fail(bindingResult.getAllErrors().get(0));
        }
        return clientConfigService.updateById(config)?success():fail();
    }
}
