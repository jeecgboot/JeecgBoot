package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Collection;
import org.jeecg.modules.im.entity.CustomEmoji;
import org.jeecg.modules.im.entity.MyGif;
import org.jeecg.modules.im.entity.query_helper.QCollection;
import org.jeecg.modules.im.service.CollectionService;
import org.jeecg.modules.im.service.MyGifService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 我的收藏
 */
@RestController
@RequestMapping("/a/collection")
public class CollectionCtrl extends BaseApiCtrl {
    @Resource
    private CollectionService collectionService;

    @RequestMapping("/all")
    public Result<Object> all(QCollection q){
        q.setUserId(getCurrentUserId());
        return success(collectionService.paginationApi(new MyPage<>(getPage(),getPageSize()),q));
    }
    /**
     * 添加
     */
    @PostMapping("/add")
    public Result<Object> add(Long msgId,Boolean isMuc){
        Collection collection = new Collection();
        collection.setUserId(getCurrentUserId());
        collection.setIsMuc(isMuc);
        collection.setTsCreate(getTs());
        collection.setMsgId(msgId);
        return collectionService.save(collection)?success():fail();
    }
    /**
     * 删除
     */
    @PostMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return collectionService.del(ids);
    }
    /**
     * 置顶
     */
    @PostMapping("/pin")
    public Result<Object> pin(int id){
        Collection collection = collectionService.getById(id);
        if(collection==null||!collection.getUserId().equals(getCurrentUserId())){
            return fail();
        }
        collection.setTsPin(getTs());
        return collectionService.updateById(collection)?success(collection.getTsPin()):fail();
    }
}
