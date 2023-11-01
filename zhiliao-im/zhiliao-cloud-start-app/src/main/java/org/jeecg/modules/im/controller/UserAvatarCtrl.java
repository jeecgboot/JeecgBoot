package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.UserAvatar;
import org.jeecg.modules.im.service.UserAvatarService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户历史头像
 */
@RestController
@RequestMapping("/a/userAvatar")
public class UserAvatarCtrl extends BaseApiCtrl {
    @Resource
    private UserAvatarService userAvatarService;
    /**
     * 我的
     */
    @PostMapping("/all")
    public Result<Object> all(@RequestParam Integer userId){
        return userAvatarService.findMyAll(userId);
    }
    /**
     * 删除
     */
    @PostMapping("/del")
    public Result<Object> del(@RequestParam String id){
        UserAvatar avatar = userAvatarService.getById(id);
        if(avatar==null||!avatar.getUserId().equals(getCurrentUserId())){
            return fail("数据不存在");
        }
        return userAvatarService.del(id);
    }
}
