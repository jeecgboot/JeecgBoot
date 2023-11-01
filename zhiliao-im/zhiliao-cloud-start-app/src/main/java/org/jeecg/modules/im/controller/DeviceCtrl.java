package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.service.DeviceService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 设备
 */
@RestController
@RequestMapping("/a/device")
@Slf4j
public class DeviceCtrl extends BaseApiCtrl {

    @Resource
    private DeviceService deviceService;

    @PostMapping("/all")
    public Result<Object> all() {
        return success(deviceService.findAll(getCurrentUserId()));
    }
    //终止特定会话
    @PostMapping("/terminate")
    public Result<Object> terminate(@RequestParam Integer id,@RequestParam(defaultValue = "0",required = false) String except) {
        return success(deviceService.terminate(id,except.equals("1")));
    }
}
