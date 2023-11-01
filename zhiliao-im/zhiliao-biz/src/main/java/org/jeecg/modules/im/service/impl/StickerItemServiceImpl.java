package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.binarywang.java.emoji.EmojiConverter;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.StickerItem;
import org.jeecg.modules.im.entity.query_helper.QStickerItem;
import org.jeecg.modules.im.mapper.StickerItemMapper;
import org.jeecg.modules.im.service.StickerItemService;
import org.jeecg.modules.im.service.UploadService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;

/**
 * <p>
 * 贴纸项 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-27
 */
@Service
public class StickerItemServiceImpl extends BaseServiceImpl<StickerItemMapper, StickerItem> implements StickerItemService {

    @Resource
    private UploadService uploadService;
    @Autowired
    private StickerItemMapper stickerItemMapper;
    private EmojiConverter emojiConverter = EmojiConverter.getInstance();
    @Override
    public IPage<StickerItem> pagination(MyPage<StickerItem> page, QStickerItem q) {
        if(!isEmpty(q.getEmoji())){
            q.setEmoji(emojiConverter.toAlias(q.getEmoji()));
        }
        return stickerItemMapper.pagination(page,q);
    }

    @Override
    public Result<Object> createOrUpdate(StickerItem item) {
        if(item.getId()==null){
            item.setTsCreate(getTs());
            if(!isEmpty(item.getEmoji())) {
                item.setEmojiCode(emojiConverter.toAlias(item.getEmoji()));
            }
            if(!save(item)){
                return fail("添加失败");
            }
        }else{
            if(!isEmpty(item.getEmoji())){
                item.setEmojiCode(emojiConverter.toAlias(item.getEmoji()));
            }
            if(!updateById(item)){
                return fail("更新失败");
            }
        }
        return success();
    }

    @Override
    public Result<Object> del(String ids) {
        if(isEmpty(ids)){
            return fail();
        }
        stickerItemMapper.deleteBatchIds(Arrays.asList(StringUtils.split(ids,",")));
        return success();
    }
}
