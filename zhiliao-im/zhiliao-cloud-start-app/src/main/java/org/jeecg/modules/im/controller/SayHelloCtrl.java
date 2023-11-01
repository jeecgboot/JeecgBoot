package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.util.Kv;
import org.jeecg.modules.im.entity.SayHello;
import org.jeecg.modules.im.service.SayHelloService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 加好友请求
 */
@RestController
@RequestMapping("/a/sayHello")
public class SayHelloCtrl extends BaseApiCtrl {
    @Resource
    private SayHelloService sayHelloService;

    /**
     * 发送加好友请求
     */
    @PostMapping("/sendAddRequest")
    public Result<Object> sendAddRequest(@RequestParam Integer toUserId, @RequestParam Integer resource, @RequestParam(required = false) String who, @RequestParam(required = false) String remark){
        return sayHelloService.sendAdd(getCurrentUserId(),toUserId,resource,who,remark);
    }
    /**
     * 发送关注请求
     */
    @PostMapping("/sendFollowRequest")
    public Result<Object> sendFollowRequest(@RequestParam Integer toUserId,@RequestParam Integer resource){
        return sayHelloService.sendFollow(getCurrentUserId(),toUserId,resource);
    }
    /**
     * 查询所有发出和收到的
     */
    @PostMapping("/findAll")
    public Result<Object> findAll(){
        List<SayHello> sendList = sayHelloService.findAllSend(getCurrentUserId());
        List<SayHello> receiveList = sayHelloService.findAllReceive(getCurrentUserId());
        return success(Kv.by("send",sendList).set("receive",receiveList));
    }

    /**
     * 回复
     */
    @PostMapping("/reply")
    public Result<Object> reply(@RequestParam Integer id,@RequestParam String msg){
        return sayHelloService.reply(id,msg,getCurrentUserId());
    }

    /**
     * 处理
     */
    @PostMapping("/deal")
    public Result<Object> deal(@RequestParam Integer id,@RequestParam boolean isAccept){
        return sayHelloService.deal(id,isAccept,getCurrentUserId());
    }
    //查询某条
    @PostMapping("/getById")
    public Result<Object> getById(@RequestParam Integer id){
        return success(sayHelloService.findById(getCurrentUserId(),id));
    }

}
