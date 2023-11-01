package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.MyStickerItem;
import org.jeecg.modules.im.service.MyStickerItemService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 我的贴纸项收藏
 */
@RestController
@RequestMapping("/a/myStickerItem")
public class MyStickerItemCtrl extends BaseApiCtrl {
    @Resource
    private MyStickerItemService myStickerItemService;

    @RequestMapping("/all")
    public Result<Object> all(){
        return success(myStickerItemService.findAll(getCurrentUserId()));
    }
    /**
     * 添加
     */
    @PostMapping("/add")
    public Result<Object> add(@RequestBody @Validated MyStickerItem myStickerItem, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return fail(bindingResult.getAllErrors().get(0));
        }
        return myStickerItemService.save(myStickerItem)?success():fail();
    }
    //从贴纸库里添加
    @PostMapping("/addStickerItem")
    public Result<Object> addStickerItem(@RequestParam Integer stickerItemId){
        return myStickerItemService.addStickerItem(stickerItemId);
    }
    /**
     * 删除
     */
    @PostMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return myStickerItemService.del(ids);
    }
    /**
     * 更新顺序
     */
    @PostMapping("/update")
    public Result<Object> update(MyStickerItem myGif){
        return myStickerItemService.updateById(myGif)?success():fail();
    }
}
