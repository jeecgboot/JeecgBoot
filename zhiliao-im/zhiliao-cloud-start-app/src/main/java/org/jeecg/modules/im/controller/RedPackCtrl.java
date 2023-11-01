package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.RedPack;
import org.jeecg.modules.im.service.RedPackService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 红包
 */
@Slf4j
@RestController
@RequestMapping("/a/redPack")
public class RedPackCtrl extends BaseApiCtrl {
    @Resource
    private RedPackService redPackService;
    //单聊发送
    @PostMapping("/send")
    public Result<Object> send(@Validated RedPack redPack){
        return redPackService.send(redPack);
    }
    //群聊发送
    @PostMapping("/sendMuc")
    public Result<Object> sendMuc(@Validated RedPack redPack){
        return redPackService.sendMuc(redPack);
    }
    //拆开
    @PostMapping("/open")
    public Result<Object> open(){
        return success();
    }
    //发送记录
    @PostMapping("/sendHistory")
    public Result<Object> sendHistory(){
        return success();
    }
    //拆开记录
    @PostMapping("/openHistory")
    public Result<Object> openHistory(){
        return success();
    }
    //填写感谢语
    @PostMapping("/setWords")
    public Result<Object> setWords(){
        return success();
    }
}
