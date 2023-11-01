package org.jeecg.modules.im.controller;

import com.github.binarywang.java.emoji.EmojiConverter;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.query_helper.QGif;
import org.jeecg.modules.im.service.GifService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * gif
 */
@RestController
@RequestMapping("/a/gif")
public class GifCtrl extends BaseApiCtrl {
    @Resource
    private GifService gifService;
    private final EmojiConverter emojiConverter = EmojiConverter.getInstance();

    @RequestMapping("/search")
    public Result<Object> search(QGif q){
        if(!isEmpty(q.getSearch())){
            q.setEmojiCode(emojiConverter.toAlias(q.getSearch()));
            q.setKeyword(q.getSearch());
        }
        return success(gifService.paginationApi(new MyPage<>(getPage(),getPageSize()),q));
    }

}
