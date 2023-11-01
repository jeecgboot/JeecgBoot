package org.jeecg.modules.im.controller;


import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.GifAlbum;
import org.jeecg.modules.im.entity.query_helper.QGifAlbum;
import org.jeecg.modules.im.service.GifAlbumService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/im/gifAlbum")
public class GifAlbumController extends BaseBackController {
    @Resource
    GifAlbumService gifAlbumService;

    @RequestMapping("/pagination")
    public Result<Object> list(QGifAlbum q){
        return success(gifAlbumService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }
    /**
     * 更新
     */
    @RequestMapping("/createOrUpdate")
    public Result<Object> createOrUpdate(@RequestBody @Validated GifAlbum gifAlbum, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return fail(bindingResult.getAllErrors().get(0));
        }
        return gifAlbumService.createOrUpdate(gifAlbum);
    }

    /**
     * 批量删除
     */
    @RequestMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return gifAlbumService.del(ids);
    }

    @RequestMapping("/detail")
    public Result<Object> detail(@RequestParam String id){
        return success(gifAlbumService.getById(id));
    }
}
