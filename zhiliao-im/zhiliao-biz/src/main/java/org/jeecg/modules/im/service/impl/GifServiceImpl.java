package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.binarywang.java.emoji.EmojiConverter;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Gif;
import org.jeecg.modules.im.entity.Gif;
import org.jeecg.modules.im.entity.query_helper.QGif;
import org.jeecg.modules.im.mapper.GifMapper;
import org.jeecg.modules.im.service.GifService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * gif收藏 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-26
 */
@Service
public class GifServiceImpl extends BaseServiceImpl<GifMapper, Gif> implements GifService {

    @Autowired
    private GifMapper gifMapper;
    private final EmojiConverter emojiConverter = EmojiConverter.getInstance();
    @Override
    public IPage<Gif> pagination(MyPage<Gif> page, QGif q) {
        return gifMapper.pagination(page, q);
    }
    @Override
    public IPage<Gif> paginationApi(MyPage<Gif> page, QGif q) {
        return gifMapper.paginationApi(page, q);
    }


    @Override
    public Result<Object> createOrUpdate(Gif gif) {
        if(gif.getId()==null){
            gif.setTsCreate(getTs());
            if(!isEmpty(gif.getEmoji())){
                gif.setEmojiCode(emojiConverter.toAlias(gif.getEmoji()));
            }
            if(!save(gif)){
                return fail("添加失败");
            }
        }else{
            if(!isEmpty(gif.getEmoji())) {
                gif.setEmojiCode(emojiConverter.toAlias(gif.getEmoji()));
            }
            if(!updateById(gif)){
                return fail("更新失败");
            }
        }
        return success();
    }

    //逻辑删除
    @Override
    public Result<Object> del(String ids) {
        if(isEmpty(ids)){
            return fail();
        }
        gifMapper.deleteBatchIds(Arrays.asList(StringUtils.split(ids,",")));
        return success();
    }

    @Override
    public Gif findByMd5(String md5) {
        return gifMapper.findByMd5(md5);
    }

    @Override
    public List<String> findHotEmojis() {
        return gifMapper.findHotEmojis();
    }
}
