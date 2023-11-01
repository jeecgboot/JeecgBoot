package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Sticker;
import org.jeecg.modules.im.entity.query_helper.QSticker;
import org.jeecg.modules.im.service.StickerService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 贴纸
 */
@RestController
@RequestMapping("/im/sticker")
public class StickerController extends BaseBackController {
    @Resource
    private StickerService stickerService;

    @RequestMapping("/pagination")
    public Result<Object> list(QSticker q){
        return success(stickerService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }

    /**
     * 创建或更新
     */
    @RequestMapping("/createOrUpdate")
    public Result<Object> createOrUpdate(@RequestBody @Validated Sticker sticker, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return fail(bindingResult.getAllErrors().get(0));
        }
        return stickerService.createOrUpdate(sticker);
    }

    @RequestMapping("/detail")
    public Result<Object> detail(Integer id){
        return success(stickerService.findById(id));
    }
}
