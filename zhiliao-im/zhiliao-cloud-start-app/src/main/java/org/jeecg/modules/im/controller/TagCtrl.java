package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.Tag;
import org.jeecg.modules.im.service.TagService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 标签
 */
@Slf4j
@RestController
@RequestMapping("/a/tag")
public class TagCtrl extends BaseApiCtrl {
    @Resource
    private TagService tagService;
    /**
     * 查询用户所有的标签
     */
    @PostMapping("/allOfMy")
    public Result<Object> allOfMy(){
        return success(tagService.findAllOfUser(getCurrentUserId()));
    }

    /**
     * 创建并关联好友
     */
    @PostMapping("/create")
    public Result<Object> create(String name,String friendIds){
        return tagService.create(name,friendIds);
    }

    /**
     * 更新
     */
    @PostMapping("/update")
    public Result<Object> update(@Validated Tag tag, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return fail(bindingResult.getAllErrors().get(0));
        }
        return tagService.update(tag);
    }
    /**
     * 删除
     */
    @PostMapping("/del")
    public Result<Object> del(@RequestParam String id){
        return tagService.del(id);
    }
}
