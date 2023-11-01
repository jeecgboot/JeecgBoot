package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.ChannelPermission;
import org.jeecg.modules.im.service.ChannelPermissionService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 频道管理员权限默认配置
 */
@RestController
@RequestMapping("/im/channelPermission")
public class ChannelPermissionController extends BaseBackController {
    @Resource
    private ChannelPermissionService service;

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
    public Result<Object> update(@RequestBody @Validated ChannelPermission permission, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return fail(bindingResult.getAllErrors().get(0));
        }
        return service.updateById(permission)?success():fail();
    }
}
