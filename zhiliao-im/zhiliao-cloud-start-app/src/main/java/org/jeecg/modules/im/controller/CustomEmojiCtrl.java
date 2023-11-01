package org.jeecg.modules.im.controller;

import com.github.binarywang.java.emoji.EmojiConverter;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.CustomEmoji;
import org.jeecg.modules.im.entity.query_helper.QCustomEmoji;
import org.jeecg.modules.im.service.CustomEmojiService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 自定义表情
 */
@RestController
@RequestMapping("/a/customEmoji")
public class CustomEmojiCtrl extends BaseApiCtrl {
    @Resource
    private CustomEmojiService customEmojiService;


    @RequestMapping("/all")
    public Result<Object> all(){
        return success(customEmojiService.findAll(getCurrentUserId()));
    }

    @RequestMapping("/search")
    public Result<Object> search(QCustomEmoji q){
        return success(customEmojiService.paginationApi(new MyPage<>(getPage(),getPageSize()),q));
    }
    /**
     * 自己上传添加
     */
    @PostMapping("/add")
    public Result<Object> add(@RequestBody @Validated CustomEmoji customEmoji, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return fail(bindingResult.getAllErrors().get(0));
        }
        return customEmojiService.save(customEmoji)?success():fail();
    }
    /**
     * 删除
     */
    @PostMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return customEmojiService.del(ids);
    }
    /**
     * 更新
     */
    @PostMapping("/update")
    public Result<Object> update(CustomEmoji customEmoji){
        return customEmojiService.updateById(customEmoji)?success():fail();
    }
    /**
     * 置顶
     */
    @PostMapping("/pin")
    public Result<Object> pin(int id){
        CustomEmoji emoji = customEmojiService.getById(id);
        if(emoji==null||!emoji.getUserId().equals(getCurrentUserId())){
            return fail();
        }
        emoji.setTsPin(getTs());
        return customEmojiService.updateById(emoji)?success(emoji.getTsPin()):fail();
    }
}
