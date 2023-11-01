package org.jeecg.modules.im.controller;


import com.github.binarywang.java.emoji.EmojiConverter;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Gif;
import org.jeecg.modules.im.entity.query_helper.QGif;
import org.jeecg.modules.im.service.GifService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/im/gif")
public class GifController extends BaseBackController {
    @Resource
    GifService gifService;
    private final EmojiConverter emojiConverter = EmojiConverter.getInstance();

    @RequestMapping("/pagination")
    public Result<Object> list(QGif q){
        if(isNotEmpty(q.getEmoji())){
            q.setEmojiCode(emojiConverter.toAlias(q.getEmoji()));
        }
        return success(gifService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }
    /**
     * 更新
     */
    @RequestMapping("/createOrUpdate")
    public Result<Object> createOrUpdate(@RequestBody @Validated Gif gif, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return fail(bindingResult.getAllErrors().get(0));
        }
        return gifService.createOrUpdate(gif);
    }

    /**
     * 批量删除
     */
    @RequestMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return gifService.del(ids);
    }

    @RequestMapping("/detail")
    public Result<Object> detail(@RequestParam String id){
        return success(gifService.getById(id));
    }
}
