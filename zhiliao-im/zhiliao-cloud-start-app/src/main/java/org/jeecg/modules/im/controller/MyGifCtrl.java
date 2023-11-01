package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.util.Kv;
import org.jeecg.modules.im.entity.CustomEmoji;
import org.jeecg.modules.im.entity.GifAlbum;
import org.jeecg.modules.im.entity.MyGif;
import org.jeecg.modules.im.service.GifAlbumService;
import org.jeecg.modules.im.service.GifService;
import org.jeecg.modules.im.service.MyGifService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 我的gif
 */
@RestController
@RequestMapping("/a/myGif")
public class MyGifCtrl extends BaseApiCtrl {
    @Resource
    private MyGifService myGifService;
    @Resource
    private GifService gifService;
    @Resource
    private GifAlbumService gifAlbumService;

    @RequestMapping("/all")
    public Result<Object> all(){
        List<GifAlbum> albums = gifAlbumService.findAll();
        Map map = new HashMap<>();
        for (GifAlbum album : albums) {
            map.put("album_id",album.getId());
            album.setGifs(gifService.listByMap(map));
        }
        Kv data = Kv.by("data",myGifService.findAll(getCurrentUserId()))
                .set("hot_emojis",gifService.findHotEmojis())
                .set("albums",albums);
        return success(data);
    }
    /**
     * 自己上传添加
     */
    @PostMapping("/add")
    public Result<Object> add(@RequestBody @Validated MyGif myGif, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return fail(bindingResult.getAllErrors().get(0));
        }
        return myGifService.save(myGif)?success():fail();
    }
    /**
     * 删除
     * gif id
     */
    @PostMapping("/del")
    public Result<Object> del(@RequestParam Integer gifId){
        MyGif myGif = myGifService.findByGifId(getCurrentUserId(),gifId);
        if(myGif==null){
            return fail();
        }
        return myGifService.removeById(myGif.getId())?success():fail();
    }
    /**
     * 更新顺序
     */
    @PostMapping("/update")
    public Result<Object> update(int gifId,int orderNo){
        MyGif myGif = myGifService.findByGifId(getCurrentUserId(),gifId);
        if(myGif==null){
            return fail();
        }
        myGif.setOrderNo(orderNo);
        return myGifService.updateById(myGif)?success():fail();
    }

    /**
     * 置顶
     * gif id
     */
    @PostMapping("/pin")
    public Result<Object> pin(int gifId){
        MyGif myGif = myGifService.findByGifId(getCurrentUserId(),gifId);
        if(myGif==null){
            return fail();
        }
        myGif.setTsPin(getTs());
        return myGifService.updateById(myGif)?success(myGif.getTsPin()):fail();
    }
}
