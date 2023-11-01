package org.jeecg.modules.im.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Sticker;
import org.jeecg.modules.im.entity.StickerItem;
import org.jeecg.modules.im.entity.query_helper.QSticker;
import org.jeecg.modules.im.service.StickerService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 贴纸
 */
@RestController
@RequestMapping("/a/sticker")
public class StickerCtrl extends BaseApiCtrl {
    @Resource
    private StickerService stickerService;

    @PostMapping("/getOne")
    public Result<Object> getOne(Integer id){
        return success(stickerService.findById(id));
    }
    @PostMapping("/getBigEmoji")
    public Result<Object> getBigEmoji(){
        return success(stickerService.getBigEmoji());
    }
    @PostMapping("/getEmojis")
    public Result<Object> getEmojis(){
        return success(stickerService.getEmojis());
    }
    @PostMapping("/getHot")
    public Result<Object> getHot(){
        return success(stickerService.getHot());
    }

    @PostMapping("/pagination")
    public Result<Object> pagination(QSticker q){
        q.setSenderId(getCurrentUserId());
        IPage<Sticker> page = stickerService.paginationApi(new MyPage<>(getPage(),getPageSize()),q);
        for (Sticker sticker : page.getRecords()) {
            for (StickerItem item : sticker.getItems()) {
                if(item.getOrigin()!=null&&item.getThumb()==null){
                    System.out.println(item.getId());
                }
            }
        }
        return success(page);
    }
}
