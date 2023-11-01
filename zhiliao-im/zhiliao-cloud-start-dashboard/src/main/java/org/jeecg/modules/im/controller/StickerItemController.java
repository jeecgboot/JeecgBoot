package org.jeecg.modules.im.controller;

import com.github.binarywang.java.emoji.EmojiConverter;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.StickerItem;
import org.jeecg.modules.im.entity.query_helper.QStickerItem;
import org.jeecg.modules.im.service.StickerItemService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * 贴纸项
 */
@RestController
@RequestMapping("/im/stickerItem")
public class StickerItemController extends BaseBackController {
    @Resource
    private StickerItemService stickerItemService;
    private final EmojiConverter emojiConverter = EmojiConverter.getInstance();

    @RequestMapping("/pagination")
    public Result<Object> list(QStickerItem q){
        if(isNotEmpty(q.getEmoji())){
            q.setEmojiCode(emojiConverter.toAlias(q.getEmoji()));
        }
        return success(stickerItemService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }

    /**
     * 添加或更新
     */
    @RequestMapping("/createOrUpdate")
    public Result<Object> createOrUpdate(@RequestBody @Validated StickerItem item, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return fail(bindingResult.getAllErrors().get(0));
        }
        return stickerItemService.createOrUpdate(item);
    }

    /**
     * 批量删除
     */
    @RequestMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return stickerItemService.del(ids);
    }
    //详情
    @RequestMapping("/detail")
    public Result<Object> detail(@RequestParam String id){
        return success(stickerItemService.getById(id));
    }
}
