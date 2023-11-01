package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.service.MyStickerService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 我的贴纸
 */
@RestController
@RequestMapping("/a/mySticker")
public class MyStickerCtrl extends BaseApiCtrl {
    @Resource
    private MyStickerService myStickerService;

    /**
     * 所有
     */
    @PostMapping("/all")
    public Result<Object> my(){
        return success(myStickerService.findMyAll(getCurrentUserId()));
    }
    /**
     * 添加
     */
    @PostMapping("/add")
    public Result<Object> add(@RequestParam Integer stickerId){
        return myStickerService.add(stickerId);
    }
    /**
     * 删除
     */
    @PostMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return myStickerService.del(ids);
    }
}
